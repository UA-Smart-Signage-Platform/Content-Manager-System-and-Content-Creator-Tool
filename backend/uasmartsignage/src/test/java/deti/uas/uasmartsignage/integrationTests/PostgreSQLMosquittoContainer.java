package deti.uas.uasmartsignage.integrationTests;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class PostgreSQLMosquittoContainer extends GenericContainer<PostgreSQLMosquittoContainer> {

    public static final int POSTGRES_PORT = 5432;
    public static final int MOSQUITTO_PORT = 1883;

    public PostgreSQLMosquittoContainer() {
        super(DockerImageName.parse("postgres-mosquitto:latest"));

        addExposedPorts(POSTGRES_PORT, MOSQUITTO_PORT);
        waitingFor(Wait.forLogMessage(".*ready to accept connections.*", 1));
    }

    public int getPostgresPort() {
        return getMappedPort(POSTGRES_PORT);
    }

    public int getMosquittoPort() {
        return getMappedPort(MOSQUITTO_PORT);
    }
}