package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDetailsForTimeTable {
	
	private Date selectedDate;
	private String emploeeName;
	private String employeeCode;
	private String subjectCode;
	private String course;
	private String schoolShortName;
	private String weekDay;
	private String interval;
	private String blockCode;
	private String roomCode;
	private Integer year;
	private Integer sem;
	private String sectionName;
	private String batchName;
	private String programSpecializationShortName;
	private String programSpecializationName;
	private String courseCode;
	private Integer empId;

}
