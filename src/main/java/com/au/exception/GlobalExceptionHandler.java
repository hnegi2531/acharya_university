package com.au.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	Logger log=LoggerFactory.getLogger(GlobalExceptionHandler.class);
	// handle specific exceptions
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {

		ErrorDetails error = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
	}

	
	// handle API exceptions
	@ExceptionHandler(APIException.class)
	public ResponseEntity<?> handleAPIException(APIException exception, WebRequest request) {

		ErrorDetails error = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ProgramSpecilizationNotFoundException.class)
	public ResponseEntity<?> handleAPIException(ProgramSpecilizationNotFoundException exception, WebRequest request) {

		ErrorDetails error = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
	}
	
	
	// handle global exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest request) {
		ErrorDetails error = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		
	    if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
	    	ErrorDetails error1 = new ErrorDetails(new Date(),"Please Check Existing Data!!!", request.getDescription(false));
	    	return new ResponseEntity(error1, HttpStatus.ALREADY_REPORTED);
	    } else if(exception.getCause() instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException){
	    	ErrorDetails error2 = new ErrorDetails(new Date(), "Json Structure passing in RequestBody is not matched", request.getDescription(false));
	        return new ResponseEntity(error2, HttpStatus.CONFLICT);
	    }else {
			return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ExceptionHandler(CredentialFailures.class)
	public ResponseEntity<?> credentialException(CredentialFailures exception, WebRequest request) {

		ErrorDetails error = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
	}
	
	
	@ExceptionHandler(RequestRejectedException.class)
	public ResponseEntity<?> requestRejectedExceptionHEntity(RequestRejectedException exception, WebRequest request) {
		ErrorDetails error = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
	}
	
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInvalidArgument(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        NotBlankError blankError=new NotBlankError(new Date(),errorMap,request.getDescription(false));
        return new ResponseEntity(blankError, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleInvalidArgument(SQLIntegrityConstraintViolationException exception, WebRequest request) {
        
        ErrorDetails error = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    } 
    
	
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
//        // Return a response with the status code and the message from the exception
//        return new ResponseEntity<>(ex.getReason(), ex.getStatus());
//    }
    
    @Autowired
    private HttpServletRequest request;  // Autowire HttpServletRequest to get the request path

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        // Get the current request path dynamically
        String requestPath = request.getRequestURI();

        // Create a custom error response structure
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getReason(),
                requestPath  // Use the dynamic path from the current request
        );

        // Return the custom error response
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    // Custom error response structure
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        // Getters and Setters
        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

}
