package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomTemplateListDTO {


	private String referenceNo;

	private Date createdDate;
	private String usertype;
	private String categoryDetail;
	private String createdBy;
	private Boolean withLetterHead;
	private String templateType;
	private Integer school_id;
	private String school_name;
	private String content;
	private String categoryShortName;
	private String school_name_short;
	private Integer org_id;
	private String org_name;
	private String org_type;
}
