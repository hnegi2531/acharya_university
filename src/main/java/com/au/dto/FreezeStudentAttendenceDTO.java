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
public class FreezeStudentAttendenceDTO {

	private Long freezeId;
	
	private String academicYear;
	
	private List<Integer> instituteIds;
	
	private Integer percentage;
	
	private String institute;
	
	private Date createdDate;
	
	private String createdBy;
	private Integer instituteId;
	private Boolean active;
	

	public FreezeStudentAttendenceDTO(Long freezeId, String academicYear, Integer percentage, String institute,
			Date createdDate, String createdBy,Integer instituteId,Boolean active) {
		
		this.freezeId = freezeId;
		this.academicYear = academicYear;
		this.percentage = percentage;
		this.institute = institute;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.instituteId=instituteId;
		this.active=active;
	}

	
	
}
