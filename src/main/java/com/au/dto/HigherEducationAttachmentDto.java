package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

public class HigherEducationAttachmentDto {
	
	private MultipartFile file;
	private Integer he_attachment_id;
	private Integer job_id;
	
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public Integer getHe_attachment_id() {
		return he_attachment_id;
	}
	public void setHe_attachment_id(Integer he_attachment_id) {
		this.he_attachment_id = he_attachment_id;
	}
	
	public Integer getJob_id() {
		return job_id;
	}
	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}

}
