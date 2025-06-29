package com.au.exception;

public class DeptNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeptNotFoundException(String msg)
	{
		super(msg);
	}
}
