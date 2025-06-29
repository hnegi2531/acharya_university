package com.au.dto;

import lombok.Data;

@Data
public class HostelBedChangeDto {

	private Integer studentId;

	private Integer hostelBlockId;

	private Integer hostelRoomId;

	private Integer hostelFeeTemplateId;

	private Integer hostelBedId;
	private String approveStatus;
	private Integer hoselBedAssignmentId;
	private Boolean active;
	
	/** The approver id. is userId*/
	private Integer approverId;

}
