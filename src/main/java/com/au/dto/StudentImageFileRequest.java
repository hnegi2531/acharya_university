package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

public class StudentImageFileRequest {
	
	private Integer student_id;
	private MultipartFile image_file;
	private String auid;

	public Integer getStudent_id() {
		return student_id;
	}
	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}
	public MultipartFile getImage_file() {
		return image_file;
	}
	public void setImage_file(MultipartFile image_file) {
		this.image_file = image_file;
	}
	public String getAuid() {
		return auid;
	}
	public void setAuid(String auid) {
		this.auid = auid;
	}
	
	

}
