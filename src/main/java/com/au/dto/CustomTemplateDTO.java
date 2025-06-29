package com.au.dto;

import java.util.Date;

import javax.persistence.Lob;
import lombok.Data;

@Data
public class CustomTemplateDTO {

	private Integer userId;

	private String userCode;

	private Integer categoryTypeId;

	private Integer categoryDetailId;

	@Lob
	private String content;

	private String categoryShortName;
	
	private Integer createdBy;
	
	private String usertype;
	
	private Boolean withLetterHead;
	
	private String templateType;
	
	private Integer school_id;
}