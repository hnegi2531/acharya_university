package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FineConcessionDTO {
	
    private String auid;
	
	private Integer currentSem;
	
	private Integer currentYear;
	
	private Integer totalDue;
	
	private Integer concessionAmount;
	
	private String tillDate;
	
	private String remarks;
	
	private String file;
}
