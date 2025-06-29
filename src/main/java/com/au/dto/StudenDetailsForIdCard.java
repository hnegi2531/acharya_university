package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudenDetailsForIdCard {
	
	private Integer studentId;
	private String studentName;
	private String auid;
	private String mobile;
	private String usn; 
	private Date dateOfAdmission;
	private Date reportingDate;
	private Integer currentYear;
	private Integer currentSem;
	private String displayName;
	private String studentImagePath;
	private String programWithSpecialization;
	private String schoolNameShort;
	private Boolean idCardBucketStatus;

}
