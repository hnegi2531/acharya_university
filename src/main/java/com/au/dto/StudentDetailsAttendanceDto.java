package com.au.dto;

import java.util.Date;


import lombok.Data;

@Data
public class StudentDetailsAttendanceDto {
	
	
	
	private String studentName;
	private String auid;
	private Date reportingDate;
	private Integer acYearId;
	private Integer programAssignmentId;
	private Integer programId;
	private Integer programSpecializationId;
	private Integer currentSem;
	private Integer currentYear;
	private Integer studentId;
	
	public StudentDetailsAttendanceDto(String studentName, String auid, Date reportingDate) {
		super();
		this.studentName = studentName;
		this.auid = auid;
		this.reportingDate = reportingDate;
	}

	public StudentDetailsAttendanceDto(String studentName, String auid, Date reportingDate, Integer studentId) {
		super();
		this.studentName = studentName;
		this.auid = auid;
		this.reportingDate = reportingDate;
		this.studentId=studentId;
	}

	public StudentDetailsAttendanceDto(String studentName, String auid, Date reportingDate, Integer acYearId,
			Integer programAssignmentId, Integer programId, Integer programSpecializationId,Integer currentSem,Integer currentYear,Integer studentId) {
		super();
		this.studentName = studentName;
		this.auid = auid;
		this.reportingDate = reportingDate;
		this.acYearId = acYearId;
		this.programAssignmentId = programAssignmentId;
		this.programId = programId;
		this.programSpecializationId = programSpecializationId;
		this.currentSem = currentSem;
		this.currentYear = currentYear;
		this.studentId=studentId;
	}
	
	

}

