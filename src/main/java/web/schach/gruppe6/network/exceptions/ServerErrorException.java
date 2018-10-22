package web.schach.gruppe6.network.exceptions;

public class ServerErrorException extends Exception {
	
	public ServerErrorException() {
	}
	
	public ServerErrorException(String message) {
		super(message);
	}
	
	public ServerErrorException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ServerErrorException(Throwable cause) {
		super(cause);
	}
}
