package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CondonationDTO {

	private Long condonationId;
	
	private Integer studentId;

	private Integer courseId;

	private Integer totalClassTaken;

	private Integer totalClass;

	private Integer additionClass;

	private Float percentage;

	private String condonationType;

	private String remarks;

}
