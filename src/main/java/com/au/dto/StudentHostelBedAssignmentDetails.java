package com.au.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentHostelBedAssignmentDetails {
	
	private Integer studentId;
	private String studentName;
	private String auid;
	private String usn;
	private String acYear;
	private Date  occipiedDate;
	private Integer year;
	private Integer sem;
	private String bedName;
	private Double fixed;
	private Double due;
	private Double paid;
	private Double waiver;

}
