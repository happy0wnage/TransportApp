package ua.petrov.transport.exception;

/**
 * @user Vladyslav
 */
public class ApplicationInitializationException extends RuntimeException {
    public ApplicationInitializationException() {
    }

    public ApplicationInitializationException(String message) {
        super(message);
    }

    public ApplicationInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationInitializationException(Throwable cause) {
        super(cause);
    }
}
