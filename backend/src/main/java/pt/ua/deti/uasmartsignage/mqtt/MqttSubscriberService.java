package pt.ua.deti.uasmartsignage.mqtt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ua.deti.uasmartsignage.configuration.MqttConfig;
import pt.ua.deti.uasmartsignage.exceptions.MqttException;
import jakarta.annotation.PostConstruct;

import pt.ua.deti.uasmartsignage.services.MonitorService;
import pt.ua.deti.uasmartsignage.services.TemplateGroupService;
import pt.ua.deti.uasmartsignage.services.LogsService;
import pt.ua.deti.uasmartsignage.services.MonitorGroupService;
import pt.ua.deti.uasmartsignage.models.Monitor;

import pt.ua.deti.uasmartsignage.models.MonitorsGroup;
import pt.ua.deti.uasmartsignage.models.Severity;

@Component
@Profile("!test & !integration-test")
public class MqttSubscriberService {

    private final ObjectMapper objectMapper;

    private final MonitorService monitorService;

    private final MonitorGroupService monitorGroupService;

    private final LogsService logsService;

    private final MqttConfig mqttConfig;

    private final TemplateGroupService templateGroupService;

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(MqttSubscriberService.class);

    public MqttSubscriberService(MqttConfig mqttConfig, ObjectMapper objectMapper, MonitorService monitorService, MonitorGroupService monitorGroupService, LogsService logsService, TemplateGroupService templateGroupService) {
        this.mqttConfig = mqttConfig;
        this.objectMapper = objectMapper;
        this.monitorService = monitorService;
        this.monitorGroupService = monitorGroupService;
        this.logsService = logsService;
        this.templateGroupService = templateGroupService;
    }

    @PostConstruct
    public void subscribeToRegistrationTopic() throws MqttSecurityException, org.eclipse.paho.client.mqttv3.MqttException {
        logger.info("Subscribing to registration topic");
        try {
            mqttConfig.getInstance().subscribe("register", (topic, mqttMessage) -> {
                String payload = new String(mqttMessage.getPayload());
               logger.info("Received message on topic {}: {}", topic, payload);

                try {
                    RegistrationMessage registrationMessage = objectMapper.readValue(payload, RegistrationMessage.class);
                    handleRegistrationMessage(registrationMessage);
                } catch (IOException e) {
                    logger.error("Error parsing registration message: {}", e.getMessage());
                }

            });
        } catch (MqttException e) {
            logger.error("Error subscribing to registration topic: {}", e.getMessage());
        }
    }

    @PostConstruct
    public void subscribeToKeepAliveTopic() throws MqttSecurityException, org.eclipse.paho.client.mqttv3.MqttException {
        logger.info("Subscribing to keep alive topic");
        try {
            mqttConfig.getInstance().subscribe("keepalive", (topic, mqttMessage) -> {
                String payload = new String(mqttMessage.getPayload());
                logger.info("Received message on topic {}: {}", topic, payload);

                try {
                    KeepAliveMessage keepAliveMessage = objectMapper.readValue(payload, KeepAliveMessage.class);
                    handleKeepAliveMessage(keepAliveMessage);
                } catch (IOException e) {
                    logger.error("Error parsing keep alive message: {}", e.getMessage());
                }

            });
        } catch (MqttException e) {
            logger.error("Error subscribing to keep alive topic: {}", e.getMessage());
        }
    }

    private void handleKeepAliveMessage(KeepAliveMessage keepAliveMessage) {
        logger.info("Received keep alive message: {}", keepAliveMessage);
        logger.info("Method: {}", keepAliveMessage.getMethod());
        logger.info("UUID: {}", keepAliveMessage.getUuid());
        
        if (!logsService.addKeepAliveLog(Severity.INFO, keepAliveMessage.getUuid(), keepAliveMessage.getMethod())) {
            logger.error("Failed to add log to InfluxDB");
        } else {
            logger.info("Added log to InfluxDB: {}", keepAliveMessage.getUuid());
        }

    }

    private void handleRegistrationMessage(RegistrationMessage registrationMessage) {
            logger.info("Received registration message: {}", registrationMessage);
            logger.info("Method: {}", registrationMessage.getMethod());
            logger.info("Name: {}", registrationMessage.getName());
            logger.info("Width: {}", registrationMessage.getWidth());
            logger.info("Height: {}", registrationMessage.getHeight());
            logger.info("UUID: {}", registrationMessage.getUuid());

            IMqttClient instance = mqttConfig.getInstance();

            Monitor tempMonitor = monitorService.getMonitorByUUID(registrationMessage.getUuid());
            logger.info("Monitor: {}", tempMonitor);

            if(tempMonitor == null){
                MonitorsGroup monitorsGroup = new MonitorsGroup();
                monitorsGroup.setName(registrationMessage.getName());
                monitorsGroup.setMadeForMonitor(true);
                monitorGroupService.saveGroup(monitorsGroup);
        
                Monitor monitor = new Monitor();
                monitor.setName(registrationMessage.getName());
                monitor.setGroup(monitorsGroup);
                monitor.setHeight(Integer.parseInt(registrationMessage.getHeight()));
                monitor.setWidth(Integer.parseInt(registrationMessage.getWidth()));
                monitor.setUuid(registrationMessage.getUuid());
                monitor.setPending(true);
        
                monitorService.saveMonitor(monitor);
            } 


            // Send confirmation message back
            try {
                ConfirmRegistrationMessage confirmMessage = new ConfirmRegistrationMessage();
                confirmMessage.setMethod("CONFIRM_REGISTER");
                
                String confirmMessageJson = objectMapper.writeValueAsString(confirmMessage);

                instance.publish(registrationMessage.getUuid(), new MqttMessage(confirmMessageJson.getBytes()));
            } catch (JsonProcessingException | org.eclipse.paho.client.mqttv3.MqttException e) {
                logger.error("Error sending confirmation message: {}", e.getMessage());
            }

            if (tempMonitor != null) {
                logger.info("Monitor already exists, sending all schedules to monitor");
                List<Monitor> monitors = new ArrayList<>();
                monitors.add(tempMonitor);
                templateGroupService.sendAllSchedulesToMonitorGroup(monitors);
            }
        }

    
}
