package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

public class ScholarshipAttachmentDto {
	
	private MultipartFile file;
	private Integer scholarship_id;
	 private Integer candidate_id;
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public Integer getScholarship_id() {
		return scholarship_id;
	}
	public void setScholarship_id(Integer scholarship_id) {
		this.scholarship_id = scholarship_id;
	}
	public Integer getCandidate_id() {
		return candidate_id;
	}
	public void setCandidate_id(Integer candidate_id) {
		this.candidate_id = candidate_id;
	}

}
