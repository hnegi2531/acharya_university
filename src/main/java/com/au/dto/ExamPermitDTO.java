package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamPermitDTO {

	private String auid;
	private Float totalDue;
	private String permittedBy;
	private Integer allowSem;
	private String remarks;
}
