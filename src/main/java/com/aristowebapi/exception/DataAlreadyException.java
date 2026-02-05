package com.aristowebapi.exception;

public class DataAlreadyException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public DataAlreadyException(String message)
	{
		super(message);
	}
}
