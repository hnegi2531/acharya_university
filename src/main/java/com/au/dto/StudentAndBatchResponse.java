package com.au.dto;

import java.util.List;



public class StudentAndBatchResponse {

	private BatchDetailsResponse batchdetails;
	
	private List<StudentDetailsResponse> studentDetails;

	public StudentAndBatchResponse() {
	
	}

	public StudentAndBatchResponse(BatchDetailsResponse batchdetails, List<StudentDetailsResponse> studentDetails) {
		
		this.batchdetails = batchdetails;
		this.studentDetails = studentDetails;
	}

	public BatchDetailsResponse getBatchdetails() {
		return batchdetails;
	}

	public void setBatchdetails(BatchDetailsResponse batchdetails) {
		this.batchdetails = batchdetails;
	}

	public List<StudentDetailsResponse> getStudentDetails() {
		return studentDetails;
	}

	public void setStudentDetails(List<StudentDetailsResponse> studentDetails) {
		this.studentDetails = studentDetails;
	}
}
