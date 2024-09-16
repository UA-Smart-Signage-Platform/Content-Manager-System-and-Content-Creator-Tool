package pt.ua.deti.uasmartsignage.enums;

public enum Description {
    DELETE_FILE("File deleted: "),
    DELETE_DIRECTORY("Directory deleted: "),
    UPDATE_FILE("File renamed: ");

    private final String text;

    Description(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
