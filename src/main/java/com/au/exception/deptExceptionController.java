package com.au.exception;

import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.ControllerAdvice; 
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice 
public class deptExceptionController { 
public ResponseEntity<Object> exception(DeptNotFoundException exception)
{ 
	return new ResponseEntity<>("dept not found", HttpStatus.NOT_FOUND); 
} 
}


