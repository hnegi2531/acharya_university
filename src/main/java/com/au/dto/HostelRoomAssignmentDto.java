package com.au.dto;

import java.util.Date;

import lombok.Data;

@Data
public class HostelRoomAssignmentDto {
	
	private String name;
	private String contact;
	private String email;
	private String idProofNo;
	private Date fromDate;
	private Date toDate;
	private String status;
	private Integer hostelRoomId;
	private Boolean active;

}
