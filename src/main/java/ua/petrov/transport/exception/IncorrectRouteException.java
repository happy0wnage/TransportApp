package ua.petrov.transport.exception;

/**
 * Created by Владислав on 21.01.2016.
 */
public class IncorrectRouteException extends RuntimeException {

    public IncorrectRouteException() {
        super();
    }

    public IncorrectRouteException(String message) {
        super(message);
    }

    public IncorrectRouteException(String message, Throwable cause) {
        super(message, cause);
    }
}
