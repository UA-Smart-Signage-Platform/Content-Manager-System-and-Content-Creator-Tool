package deti.uas.uasmartsignage.exceptions;

public class IdpUsernameFetchException extends RuntimeException {
    public IdpUsernameFetchException(String message) {
        super(message);
    }

    public IdpUsernameFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}