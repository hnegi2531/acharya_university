package com.au.exception;

import java.util.Date;
import java.util.Map;

public class NotBlankError {
	
	private Date timestamp;
	private Map<String, String> message;
	private String details;
	
	public NotBlankError(Date timestamp, Map<String, String> message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String, String> getMessage() {
		return message;
	}

	public void setMessage(Map<String, String> message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	
	
	
	

}
