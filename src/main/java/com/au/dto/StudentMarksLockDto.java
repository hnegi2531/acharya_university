package com.au.dto;

import java.util.Date;

import com.au.model.StudentMarks;

import lombok.Data;

@Data
public class StudentMarksLockDto {

	private Integer marks_id;
	private Boolean faculty_status;
	private Boolean hod_status;
	private Boolean hoi_status;
	
	private String faculty_status_date;
	private String hod_status_date;
	private String hoi_status_date;
	
	private Integer faculty_id;
	private Integer hod_id;
	private Integer hoi_id;
}
