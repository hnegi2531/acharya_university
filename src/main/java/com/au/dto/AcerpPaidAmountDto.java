package com.au.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.au.model.AcerpAmount;

import lombok.Data;

@Data
public class AcerpPaidAmountDto {
	

	private String auid;
	private Integer studentId;

	private Boolean active;
	private List<AcerpAmount> acerpAmount;
	
	

}
