package pt.ua.deti.uasmartsignage.enums;

public enum Operation {
    DELETE_FILE("deleteFile"),
    DELETE_DIRECTORY("deleteDirectory"),
    UPDATE_FILE("updateFileName");

    private final String text;

    Operation(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
