package com.au.exception;

public class DuplicatedNameException extends Exception{
	
	
	
	private static final long serialVersionUID = 1L;

	public DuplicatedNameException(String message){
        super(message);
    }

    public DuplicatedNameException(String message, Throwable anException){
        super(message, anException);
    }

}
