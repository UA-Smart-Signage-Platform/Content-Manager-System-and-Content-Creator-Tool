package pt.ua.deti.uasmartsignage.services;

import java.util.List;
import java.util.Optional;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.dto.mqtt.ConfirmRegistrationMessage;
import pt.ua.deti.uasmartsignage.dto.mqtt.KeepAliveMessage;
import pt.ua.deti.uasmartsignage.dto.mqtt.MqttPayload;
import pt.ua.deti.uasmartsignage.dto.mqtt.RegistrationMessage;
import pt.ua.deti.uasmartsignage.dto.mqtt.RulesMessage;
import pt.ua.deti.uasmartsignage.dto.mqtt.embedded.MqttRule;
import pt.ua.deti.uasmartsignage.dto.mqtt.embedded.MqttSchedule;
import pt.ua.deti.uasmartsignage.enums.MqttMethod;
import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.events.RuleCreatedEvent;
import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.models.MonitorGroup;
import pt.ua.deti.uasmartsignage.models.Rule;
import pt.ua.deti.uasmartsignage.repositories.MonitorGroupRepository;
import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
import pt.ua.deti.uasmartsignage.repositories.RuleRepository;

@Service
@RequiredArgsConstructor
public class BrokerService {

    @Value("${ip.address}")
    private String serverAddress;

    private final MqttClient mqttClient;
    private final MonitorService monitorService;
    private final RuleService ruleService;
    private final LogsService logsService;
    private final FileService fileService;
    private final MonitorGroupRepository monitorGroupRepository;
    private final ObjectMapper objectMapper;

    private final String REGISTRATION_TOPIC = "register";
    private final String KEEPALIVE_TOPIC = "keepalive";

    @EventListener
    public void handleRuleCreatedEvent(RuleCreatedEvent event) {
        Rule rule = event.getRule();
        System.out.println("sending rule to " + rule.getGroupId());
        sendRulesToMonitorGroup(rule.getGroupId());
    }

    public void sendRulesToMonitorGroup(Long groupId){

        List<Monitor> monitors = monitorService.getMonitorsByGroup(groupId);
        if (monitors == null)
            return;

        for (Monitor monitor : monitors){
            RulesMessage message = new RulesMessage();
            message.setMethod(MqttMethod.RULES.name());
            List<Rule> rules = ruleService.getAllRulesForGroup(groupId);
    
            for (Rule rule : rules){
                MqttRule mqttRule = new MqttRule();
                mqttRule.setHtml(ruleService.ruleToHTML(rule, monitor.getWidth(), monitor.getHeight()));
                mqttRule.setSchedule(rule.getSchedule().toMqttFormat());
                message.addMqttRule(mqttRule);

                for (Long value : rule.getMediaWidgetValues()){
                    Optional<CustomFile> file = fileService.getFileById(value);
                    file.ifPresent(customFile -> {
                        if ("directory".equals(customFile.getType())) {
                            ; // TODO: go through directory
                        } else {
                            // TODO: don't hardcode url
                            message.addFile(serverAddress + "/api/files/download/" + customFile.getId());
                        }
                    });
                }           
            }

            sendMessageToMonitor(monitor.getId(), message);
        }
    }

    public void sendMessageToMonitor(Long monitorId, MqttPayload message) {

        Monitor monitor = monitorService.getMonitorById(monitorId);
        if (monitor == null)
            return;

        MqttMessage mqttMessage;
        try {
            mqttMessage = new MqttMessage(objectMapper.writeValueAsString(message).getBytes());
            mqttClient.publish(monitor.getUuid(), mqttMessage);
        } catch (JsonProcessingException | MqttException exception) {
            logsService.addBackendLog(Severity.ERROR,
                    this.getClass().getSimpleName(),
                    "sendMessageToMonitorGroup",
                    exception.getMessage());
            return;
        }
    }

    @PostConstruct
    private void subscribeToRegistrationTopic() throws MqttSecurityException, MqttException {
        mqttClient.subscribe(REGISTRATION_TOPIC, (topic, message) -> {
            String payload = new String(message.getPayload());
            RegistrationMessage registrationMessage;

            try {
                registrationMessage = objectMapper.readValue(payload, RegistrationMessage.class);
                handleRegistrationMessage(registrationMessage);
            } catch (JsonMappingException exception) {
                logsService.addBackendLog(Severity.ERROR,
                        this.getClass().getSimpleName(),
                        "subscribeToRegistrationTopic",
                        exception.getMessage());
                return;
            }

        });
    }

    private void handleRegistrationMessage(RegistrationMessage registrationMessage) {

        Monitor monitor = monitorService.getMonitorByUUID(registrationMessage.getUuid());

        // if it's a new monitor, add it to the database
        // and create a default group for it
        if (monitor == null) {
            MonitorGroup group = new MonitorGroup();
            group.setId(null);
            group.setName(registrationMessage.getName());
            group.setDefaultGroup(true);
            monitorGroupRepository.save(group);

            monitor = new Monitor();
            monitor.setName(registrationMessage.getName());
            monitor.setGroup(group);
            monitor.setHeight(Integer.parseInt(registrationMessage.getHeight()));
            monitor.setWidth(Integer.parseInt(registrationMessage.getWidth()));
            monitor.setUuid(registrationMessage.getUuid());
            monitor.setPending(true);

            monitor = monitorService.saveMonitor(monitor);
        }

        // Send confirmation message back
        try {
            ConfirmRegistrationMessage confirmMessage = new ConfirmRegistrationMessage();
            confirmMessage.setMethod(MqttMethod.CONFIRM_REGISTER.name());

            String confirmMessageJson = objectMapper.writeValueAsString(confirmMessage);

            mqttClient.publish(registrationMessage.getUuid(), new MqttMessage(confirmMessageJson.getBytes()));
        } catch (JsonProcessingException | MqttException exception) {
            logsService.addBackendLog(Severity.ERROR,
                    this.getClass().getSimpleName(),
                    "handleRegistrationMessage",
                    exception.getMessage());
        }

        // If the monitor already existed
        // send him what he should be playing
        if (monitor != null) {
            sendRulesToMonitorGroup(monitor.getGroup().getId());
        }
    }

    @PostConstruct
    public void subscribeToKeepAliveTopic() throws MqttSecurityException, org.eclipse.paho.client.mqttv3.MqttException {
        mqttClient.subscribe(KEEPALIVE_TOPIC, (topic, message) -> {
            String payload = new String(message.getPayload());
            KeepAliveMessage keepAliveMessage;

            try {
                keepAliveMessage = objectMapper.readValue(payload, KeepAliveMessage.class);
                logsService.addKeepAliveLog(Severity.INFO, keepAliveMessage.getUuid(), keepAliveMessage.getMethod());
            } catch (JsonMappingException exception) {
                logsService.addBackendLog(Severity.ERROR,
                        this.getClass().getSimpleName(),
                        "subscribeToRegistrationTopic",
                        exception.getMessage());
                return;
            }

        });
    }
}
