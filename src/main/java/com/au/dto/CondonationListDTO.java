package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CondonationListDTO {

	private Long condonationId;
	private String auid;
	private String usn;
	private Integer year;
	private Integer sem;
	private String course;
	private String reason;
	private String remarks;
	private Integer additionClass;
	
	
}
