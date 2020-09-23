package com.yirs.Demo.Exception;

public class TokenErrorException extends RuntimeException {
	public TokenErrorException(String message) {
		super(message);
	}

	public TokenErrorException() {
		super();
	}

}
