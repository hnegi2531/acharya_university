package com.au.dto;

public class BatchDetailsResponse {

	private Integer assignment_id;
	private String name;
	
	public BatchDetailsResponse() {
		
	}

	public BatchDetailsResponse(Integer assignment_id, String name) {
		this.assignment_id = assignment_id;
		this.name = name;
	}

	public Integer getAssignment_id() {
		return assignment_id;
	}

	public void setAssignment_id(Integer assignment_id) {
		this.assignment_id = assignment_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
