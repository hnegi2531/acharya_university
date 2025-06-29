package com.au.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniformFeeDTO {
	private String mobile;
	private Integer total;
	private Integer studentId;
	private Integer schoolId;
	private Integer currentYear;
	private Integer currentSem;
	private Integer acYearId;
	private List<UniformFeeDetails> uniformFeeDetails;
}
