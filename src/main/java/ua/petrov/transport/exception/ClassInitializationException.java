package ua.petrov.transport.exception;
/**
 *
 * @author Vladyslav_Yemelianov
 **/
public class ClassInitializationException extends RuntimeException {

	public ClassInitializationException() {
		super();
	}

	public ClassInitializationException(String message) {
		super(message);
	}

	public ClassInitializationException(Throwable cause) {
		super(cause);
	}
	

}

