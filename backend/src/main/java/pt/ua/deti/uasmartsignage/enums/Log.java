package pt.ua.deti.uasmartsignage.enums;

public enum Log {
    SUCCESS("Added log to InfluxDB: %s"),
    ERROR("Failed to add log to InfluxDB"),
    FILENOTFOUND("File with ID %s not found"),
    USERDIR(System.getProperty("user.dir"));

    private final String text;

    Log(final String text) {
        this.text = text;
    }

    public String format(Object... args) {
        return String.format(text, args);
    }

    @Override
    public String toString() {
        return text;
    }
}
