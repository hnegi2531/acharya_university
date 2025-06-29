package com.au.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;  

@ResponseStatus(HttpStatus.NOT_FOUND)  
public class VoucherHeadNotFoundException extends RuntimeException{

	public VoucherHeadNotFoundException(String msg) {
		// TODO Auto-generated constructor stub
		super(msg);
	}
}
