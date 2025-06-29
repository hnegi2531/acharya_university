package com.au.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class EducationDetailsAttachmentsDto {

    private Integer empId;
	
	private MultipartFile document;
	
	private Integer graduationId;

	public MultipartFile getDocument() {
		return document;
	}

	public void setDocument(MultipartFile document) {
		this.document = document;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public Integer getGraduationId() {
		return graduationId;
	}

	public void setGraduationId(Integer graduationId) {
		this.graduationId = graduationId;
	}	
}
