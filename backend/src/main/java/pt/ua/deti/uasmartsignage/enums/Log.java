package pt.ua.deti.uasmartsignage.enums;

public enum Log {
    ERROR("Failed to add log to InfluxDB"),
    OBJECTNOTFOUND("Object with ID %s not found"),
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
