package com.au.dto;

import java.util.Date;

import lombok.Data;

@Data
public class NoDuesAssignmentResponse {
	
	private Integer no_dues_assignment_id;
	private Integer employee_Id;
	private Integer department_id;
	private Boolean no_due_status;
	private String comments;
	private Integer resignation_id;
	private Boolean active;
	private Date created_date;
	private Date modified_date;
	private Integer created_by;
	private Integer modified_by;
	private String created_username;
	private String modified_username;
	private String ip_address;
	
}
