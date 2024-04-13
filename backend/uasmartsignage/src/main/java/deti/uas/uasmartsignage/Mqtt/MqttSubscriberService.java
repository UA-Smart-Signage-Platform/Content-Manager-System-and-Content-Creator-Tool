package deti.uas.uasmartsignage.Mqtt;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
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
public class MqttSubscriberService {

    private final ObjectMapper objectMapper;

    private final MonitorService monitorService;

    private final MonitorGroupService monitorGroupService;

    public MqttSubscriberService(ObjectMapper objectMapper, MonitorService monitorService, MonitorGroupService monitorGroupService) {
        this.objectMapper = objectMapper;
        this.monitorService = monitorService;
        this.monitorGroupService = monitorGroupService;
    }

    @PostConstruct
    public void subscribeToRegistrationTopic() throws MqttSecurityException, org.eclipse.paho.client.mqttv3.MqttException {
        System.out.println("Subscribing to registration topic");
        try {
            MqttConfig.getInstance().subscribe("register", (topic, mqttMessage) -> {
                String payload = new String(mqttMessage.getPayload());
                System.out.println("Received message on topic: " + topic);

                try {
                    RegistrationMessage registrationMessage = objectMapper.readValue(payload, RegistrationMessage.class);
                    handleRegistrationMessage(registrationMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void handleRegistrationMessage(RegistrationMessage registrationMessage) {
        System.out.println("Received registration message:");
        System.out.println("Method: " + registrationMessage.getMethod());
        System.out.println("Name: " + registrationMessage.getName());
        System.out.println("Width: " + registrationMessage.getWidth());
        System.out.println("Height: " + registrationMessage.getHeight());
        System.out.println("UUID: " + registrationMessage.getUuid());

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName(registrationMessage.getName());
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
            e.printStackTrace();
        }



    }
    
}
