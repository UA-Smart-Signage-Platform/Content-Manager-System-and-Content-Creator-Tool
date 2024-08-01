package pt.ua.deti.uasmartsignage.constants;

public enum DESCRIPTION {
    DELETE_FILE("File deleted: "),
    DELETE_DIRECTORY("Directory deleted: "),
    UPDATE_FILE("File renamed: ");

    private final String text;

    DESCRIPTION(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
