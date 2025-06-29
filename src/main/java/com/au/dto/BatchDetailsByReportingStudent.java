package com.au.dto;

public class BatchDetailsByReportingStudent {

	private Integer batch_id;
	private String batch_name;
	private Integer batch_assignment_id;
	
	public BatchDetailsByReportingStudent() {
		
	}

	public BatchDetailsByReportingStudent(Integer batch_id, String batch_name, Integer batch_assignment_id) {
		
		this.batch_id = batch_id;
		this.batch_name = batch_name;
		this.batch_assignment_id = batch_assignment_id;
	}

	public Integer getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(Integer batch_id) {
		this.batch_id = batch_id;
	}

	public String getBatch_name() {
		return batch_name;
	}

	public void setBatch_name(String batch_name) {
		this.batch_name = batch_name;
	}

	public Integer getBatch_assignment_id() {
		return batch_assignment_id;
	}

	public void setBatch_assignment_id(Integer batch_assignment_id) {
		this.batch_assignment_id = batch_assignment_id;
	}
}
