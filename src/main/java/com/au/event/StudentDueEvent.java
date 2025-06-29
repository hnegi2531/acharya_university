package com.au.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDueEvent {
    
	private Integer schoolId;
	
	private Integer programId;
	
	private Integer studentId;
	
	private String eventName;
}
