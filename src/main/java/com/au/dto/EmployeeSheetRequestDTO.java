package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSheetRequestDTO {

	private Integer month;
	private Integer year;
	private Integer school_id;
	private Integer dept_id;
	private Boolean isConsultant;
}
