package com.au.dto;

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
public class FeeAdmissionSubCategoryRequest {

	private Integer fee_admission_sub_category_id;
	private String fee_admission_sub_category_name;
	private String fee_admission_sub_category_short_name;


	private Date created_date;
	private Date modified_date;
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	private String created_username;
	private String modified_username;
	
	private Integer board_unique_id;
	private Boolean approve_intake;
	
	private List<Integer> fee_admission_category_id; 
}
