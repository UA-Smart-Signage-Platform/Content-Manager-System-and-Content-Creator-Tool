package pt.ua.deti.uasmartsignage.constants;

public enum LOG {
    SUCCESS("Added log to InfluxDB: {}"),
    ERROR("Failed to add log to InfluxDB");

    private final String text;

    LOG(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
