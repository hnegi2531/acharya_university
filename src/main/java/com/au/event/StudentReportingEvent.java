package com.au.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentReportingEvent {
	
	private String auidOrAuidWithoutIncrement;
	
	private String eventName;

}
