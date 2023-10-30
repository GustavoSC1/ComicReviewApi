package com.gustavo.comicreviewapi.services.exceptions;

public class AuthenticationErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AuthenticationErrorException(String msg) {
		super(msg);
	}
	
	public AuthenticationErrorException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
