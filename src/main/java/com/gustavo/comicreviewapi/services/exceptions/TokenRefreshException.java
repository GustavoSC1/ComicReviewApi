package com.gustavo.comicreviewapi.services.exceptions;

public class TokenRefreshException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public TokenRefreshException(String msg) {
		super(msg);
	}
	
	public TokenRefreshException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
