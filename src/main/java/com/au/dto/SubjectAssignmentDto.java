package com.au.dto;

import java.util.List;



public class SubjectAssignmentDto {
	
	private Integer course_id;  // on the basis of subject table
	private Integer user_id;
	private String remarks;

	private Integer createdBy;
	private Integer modifiedBy;

	private Boolean active;
	private String createdUsername;
	private String modifiedUsername;
	private List<Integer> course_assignment_id;
	
	public SubjectAssignmentDto() {
		super();

	}

	



	public Integer getCourse_id() {
		return course_id;
	}





	public void setCourse_id(Integer course_id) {
		this.course_id = course_id;
	}





	public List<Integer> getCourse_assignment_id() {
		return course_assignment_id;
	}





	public void setCourse_assignment_id(List<Integer> course_assignment_id) {
		this.course_assignment_id = course_assignment_id;
	}





	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCreatedUsername() {
		return createdUsername;
	}

	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	public String getModifiedUsername() {
		return modifiedUsername;
	}

	public void setModifiedUsername(String modifiedUsername) {
		this.modifiedUsername = modifiedUsername;
	}
	
	
	
	

}
