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
public class ServiceTicketDto {
	
	private String complaintDetails;
	private String floorAndExtension;
	private Integer blockId;
	private Integer tagId;
	private Long serviceTypeId;
	private Integer userId;
	private Boolean ticketStatus;
	private String attendedBy;
	private Integer created_by;
	private Integer modified_by;
	private Boolean active;
	private String created_username;
	private String modified_username;
	

}
