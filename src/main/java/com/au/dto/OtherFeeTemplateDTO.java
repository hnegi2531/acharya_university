package com.au.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtherFeeTemplateDTO {

	private Integer otherFeeTemplateId;

	private Integer schoolId;

	private Integer acYearId;

	private Integer programId;
	
	private Integer fee_admission_category_id;
	private Integer currency_type_id;
	private Integer fee_template_id;

	private List<Integer> programSpecializationId;

	private String feetype;
	
	private List<OtherFeeDetailsDTO> otherFeeDetailsDTOs=new ArrayList<>();

}
