package pt.ua.deti.uasmartsignage.constants;

public enum OPERATION {
    DELETE_FILE("deleteFile"),
    DELETE_DIRECTORY("deleteDirectory"),
    UPDATE_FILE("updateFileName");

    private final String text;

    OPERATION(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}