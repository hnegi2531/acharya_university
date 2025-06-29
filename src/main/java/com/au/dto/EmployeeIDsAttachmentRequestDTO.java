package com.au.dto;


import org.springframework.web.multipart.MultipartFile;

public class EmployeeIDsAttachmentRequestDTO {

	private Integer empId;
	
	private MultipartFile document;
	
	private String documentType;

	public MultipartFile getDocument() {
		return document;
	}

	public void setDocument(MultipartFile document) {
		this.document = document;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	
	
}
