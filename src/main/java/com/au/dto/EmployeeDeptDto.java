package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDeptDto {

	
	private Integer emp_id;
	private Integer school_id;
	private Integer dept_id;
	private String dept_name_short;
	private String school_name_short;
}
