package com.au.dto;

import java.util.Date;

import com.au.model.IncrementCreation;

import lombok.Data;

@Data
public class EmployeeListForIncrementCreationrequestDTO {

	private String employeeName;
	private String designation;
	private String department;
	private String email;
	private Date dateofJoining;
	private String empCode;
}
