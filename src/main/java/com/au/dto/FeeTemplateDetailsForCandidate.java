package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeeTemplateDetailsForCandidate {

	private Integer schoolId;
	private Integer programId;
	private Integer programSpecializationId;
	private Integer acYearId;
	private Integer feeTemplateId;
	
	private String currenyType;
	private Integer year1;
	private Integer year2;
	private Integer year3;
	private Integer year4;
	private Integer year5;
	private Integer year6;
	private Integer year7;
	private Integer year8;

}
