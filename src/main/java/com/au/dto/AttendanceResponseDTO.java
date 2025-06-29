package com.au.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class AttendanceResponseDTO {
	
	 	@JsonProperty("status")
	    private boolean status;
	    
	    @JsonProperty("data")
	    private Data data;
	    
	    @JsonProperty("message")
	    private String message;

}
