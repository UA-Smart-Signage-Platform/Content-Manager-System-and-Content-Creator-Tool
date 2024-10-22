package pt.ua.deti.uasmartsignage.configuration;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MqttConfig {

    private final Environment env;

    @Bean
    public MqttClient mqttClient() throws MqttException {
        String serverURI = env.getProperty("mqtt.serverURI");
        String clientId = env.getProperty("mqtt.clientId");
        String username = env.getProperty("mqtt.username");
        String password = env.getProperty("mqtt.password");

        MqttClient client = new MqttClient(serverURI, clientId);
        MqttConnectOptions options = new MqttConnectOptions();

        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setUserName(username);
        if (password != null) options.setPassword(password.toCharArray());

        client.connect(options);
        return client;
    }
}