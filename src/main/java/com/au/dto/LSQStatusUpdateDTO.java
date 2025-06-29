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
public class LSQStatusUpdateDTO {
	private String prospectOpportunityId;
	private String opportunityNote;
	private List<LSQStatusDTO> fields;

}
