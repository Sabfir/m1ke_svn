package ua.mike.opinta.exceptions;

@SuppressWarnings("serial")
public class MikeException extends Exception {
	public MikeException() {
		super();
	}

	public MikeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MikeException(String message) {
		super(message);
	}
}