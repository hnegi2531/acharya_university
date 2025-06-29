package com.au.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterSalaryEvent {

	private Integer month;
	private Integer year;
	private Integer empId;
	
}
