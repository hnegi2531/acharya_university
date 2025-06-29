package com.au.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
public class HostelIdCardDto {

	private Integer hostelBedAssignmentId;
	@Temporal(TemporalType.DATE)
	private Date fromDate;
	
	private Integer studentId;
	private Integer idCardAcStatus;
}
