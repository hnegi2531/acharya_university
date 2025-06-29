package com.au.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class DateInfo {
	
	@JsonProperty("isToday")
    private boolean isToday;

    @JsonProperty("isFuture")
    private boolean isFuture;


}
