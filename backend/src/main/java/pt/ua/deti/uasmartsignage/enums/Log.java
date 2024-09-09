package pt.ua.deti.uasmartsignage.enums;

public enum Log {
    SUCCESS("Added log to InfluxDB: {}"),
    ERROR("Failed to add log to InfluxDB"),
    FILENOTFOUND("File with ID {} not found"),
    USERDIR(System.getProperty("user.dir"));

    private final String text;

    Log(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
