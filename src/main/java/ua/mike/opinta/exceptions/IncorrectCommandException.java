package ua.mike.opinta.exceptions;

@SuppressWarnings("serial")
public class IncorrectCommandException extends Exception {
	public IncorrectCommandException() {
		super();
	}

	public IncorrectCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectCommandException(String message) {
		super(message);
	}
}