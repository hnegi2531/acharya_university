package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeJobTypeDto {

	private Integer emp_id;
	private Integer job_type_id;
	private String job_short_name;
}
