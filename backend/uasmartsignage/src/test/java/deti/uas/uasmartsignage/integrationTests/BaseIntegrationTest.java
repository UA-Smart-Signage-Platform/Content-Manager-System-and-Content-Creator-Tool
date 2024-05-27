package deti.uas.uasmartsignage.integrationTests;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.File;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseIntegrationTest {

    public static DockerComposeContainer container;

    private static final String DOCKER_COMPOSE_FILE_PATH = "src/test/resources/docker-compose.yml";
    private static final String POSTGRES_SERVICE_NAME = "postgres_db";
    private static final String MQTT_SERVICE_NAME = "mqtt";
    private static final String INFLUXDB_SERVICE_NAME = "influxdb";

    static {
        container = (DockerComposeContainer) new DockerComposeContainer<>(new File(DOCKER_COMPOSE_FILE_PATH))
                .withExposedService(POSTGRES_SERVICE_NAME, 5432)
                .withExposedService(MQTT_SERVICE_NAME, 1883)
                        .withExposedService(INFLUXDB_SERVICE_NAME, 8086);
        container.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        String postgresHost = container.getServiceHost(POSTGRES_SERVICE_NAME, 5432);
        Integer postgresPort = container.getServicePort(POSTGRES_SERVICE_NAME, 5432);
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://" + postgresHost + ":" + postgresPort + "/uas");
        registry.add("spring.datasource.username", () -> "spring");
        registry.add("spring.datasource.password", () -> "springpass");

        String mqttHost = container.getServiceHost(MQTT_SERVICE_NAME, 1883);
        Integer mqttPort = container.getServicePort(MQTT_SERVICE_NAME, 1883);
        registry.add("spring.mqtt.broker", () -> "tcp://"+ mqttHost + ":" + mqttPort);

        String influxdbHost = container.getServiceHost(INFLUXDB_SERVICE_NAME, 8086);
        System.out.println("afd"+influxdbHost);
        Integer influxdbPort = container.getServicePort(INFLUXDB_SERVICE_NAME, 8086);
        System.out.println("ars"+influxdbPort);
        registry.add("spring.influxdb.url", () -> "http://"+ influxdbHost + ":" + influxdbPort);

    }
}
