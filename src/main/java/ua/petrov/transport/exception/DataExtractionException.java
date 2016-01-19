package ua.petrov.transport.exception;

/**
 * @author Vladyslav
 */
public class DataExtractionException extends RuntimeException {
    public DataExtractionException() {
    }

    public DataExtractionException(String message) {
        super(message);
    }

    public DataExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataExtractionException(Throwable cause) {
        super(cause);
    }
}
