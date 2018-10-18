package web.schach.gruppe6.network.exceptions;

import java.io.IOException;

public class ServerErrorException extends IOException {
	
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
