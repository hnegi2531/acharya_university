package com.au.dto;

import lombok.Data;

@Data
public class NoDuesAssignmentRequest {

	

	private Integer employee_Id;
	private Integer department_id;
	private Boolean no_due_status;
	private String comments;
	private Integer resignation_id;
	private Boolean active;
	private String ip_address;
	private Integer approver_id;
	private String approver_date;
	
}
