package deti.uas.uasmartsignage.Mqtt;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import deti.uas.uasmartsignage.Configuration.MqttConfig;
import deti.uas.uasmartsignage.exceptions.MqttException;
import jakarta.annotation.PostConstruct;

import deti.uas.uasmartsignage.Services.MonitorService;
import deti.uas.uasmartsignage.Services.MonitorGroupService;
import deti.uas.uasmartsignage.Models.Monitor;

import deti.uas.uasmartsignage.Models.MonitorsGroup;

@Component
@Profile("!test & !integration-test")
public class MqttSubscriberService {

    private final ObjectMapper objectMapper;

    private final MonitorService monitorService;

    private final MonitorGroupService monitorGroupService;

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(MqttSubscriberService.class);

    public MqttSubscriberService(ObjectMapper objectMapper, MonitorService monitorService, MonitorGroupService monitorGroupService) {
        this.objectMapper = objectMapper;
        this.monitorService = monitorService;
        this.monitorGroupService = monitorGroupService;
    }

    @PostConstruct
    public void subscribeToRegistrationTopic() throws MqttSecurityException, org.eclipse.paho.client.mqttv3.MqttException {
        logger.info("Subscribing to registration topic");
        try {
            MqttConfig.getInstance().subscribe("register", (topic, mqttMessage) -> {
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

    private void handleRegistrationMessage(RegistrationMessage registrationMessage) {
        logger.info("Received registration message: {}", registrationMessage);
        logger.info("Method: {}", registrationMessage.getMethod());
        logger.info("Name: {}", registrationMessage.getName());
        logger.info("Width: {}", registrationMessage.getWidth());
        logger.info("Height: {}", registrationMessage.getHeight());
        logger.info("UUID: {}", registrationMessage.getUuid());

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

        // Send confirmation message back
        try {
            ConfirmRegistrationMessage confirmMessage = new ConfirmRegistrationMessage();
            confirmMessage.setMethod("CONFIRM_REGISTER");
            
            String confirmMessageJson = objectMapper.writeValueAsString(confirmMessage);

            MqttConfig.getInstance().publish(registrationMessage.getUuid(), new MqttMessage(confirmMessageJson.getBytes()));
        } catch (JsonProcessingException | org.eclipse.paho.client.mqttv3.MqttException e) {
            logger.error("Error sending confirmation message: {}", e.getMessage());
        }



    }
    
}
