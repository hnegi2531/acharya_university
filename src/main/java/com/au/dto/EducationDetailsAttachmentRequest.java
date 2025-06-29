package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

public class EducationDetailsAttachmentRequest {

	private MultipartFile file;
	private Integer graduation_id;
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public Integer getGraduation_id() {
		return graduation_id;
	}
	public void setGraduation_id(Integer graduation_id) {
		this.graduation_id = graduation_id;
	}
	}
