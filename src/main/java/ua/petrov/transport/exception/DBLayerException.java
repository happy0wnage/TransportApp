package ua.petrov.transport.exception;

/**
 * Created by Владислав
 */
public class DBLayerException extends RuntimeException {

    public DBLayerException() {
    }

    public DBLayerException(String message) {
        super(message);
    }

    public DBLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBLayerException(Throwable cause) {
        super(cause);
    }
}
