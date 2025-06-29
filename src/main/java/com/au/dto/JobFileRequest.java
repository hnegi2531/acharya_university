package com.au.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class JobFileRequest {

	private MultipartFile file;
	private Integer job_id;
	private Integer fee_template_id;
	private Integer leave_id;
	private Integer student_id;
	private Integer notification_id;
	
	private List<Integer> leave_apply_id;
	private Integer syllabus_id;
	private List<Integer> profileResearchId;
	private Integer acerpAmountId;
	private Integer documents_id;

	public Integer getSyllabus_id() {
		return syllabus_id;
	}

	public void setSyllabus_id(Integer syllabus_id) {
		this.syllabus_id = syllabus_id;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Integer getJob_id() {
		return job_id;
	}

	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

	public Integer getFee_template_id() {
		return fee_template_id;
	}

	public void setFee_template_id(Integer fee_template_id) {
		this.fee_template_id = fee_template_id;
	}

	public Integer getLeave_id() {
		return leave_id;
	}

	public void setLeave_id(Integer leave_id) {
		this.leave_id = leave_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}



	public List<Integer> getLeave_apply_id() {
		return leave_apply_id;
	}

	public void setLeave_apply_id(List<Integer> leave_apply_id) {
		this.leave_apply_id = leave_apply_id;
	}

	public Integer getNotification_id() {
		return notification_id;
	}

	public void setNotification_id(Integer notification_id) {
		this.notification_id = notification_id;
	}

	public List<Integer> getProfileResearchId() {
		return profileResearchId;
	}

	public void setProfileResearchId(List<Integer> profileResearchId) {
		this.profileResearchId = profileResearchId;
	}

	public Integer getAcerpAmountId() {
		return acerpAmountId;
	}

	public void setAcerpAmountId(Integer acerpAmountId) {
		this.acerpAmountId = acerpAmountId;
	}

	public Integer getDocuments_id() {
		return documents_id;
	}

	public void setDocuments_id(Integer documents_id) {
		this.documents_id = documents_id;
	}
	
	
}
