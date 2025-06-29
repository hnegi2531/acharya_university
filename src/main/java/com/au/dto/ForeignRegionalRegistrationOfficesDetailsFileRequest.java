package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

public class ForeignRegionalRegistrationOfficesDetailsFileRequest {
	
	private MultipartFile passport_copy_document_file;
	private MultipartFile visa_copy_document_file;
	private MultipartFile residential_permit_copy_document_file;
	private MultipartFile aiu_equivalence_document_file;
	private Integer frrod_id;
	private Integer student_id;
	
	public ForeignRegionalRegistrationOfficesDetailsFileRequest() {
		super();

	}

	public MultipartFile getPassport_copy_document_file() {
		return passport_copy_document_file;
	}

	public void setPassport_copy_document_file(MultipartFile passport_copy_document_file) {
		this.passport_copy_document_file = passport_copy_document_file;
	}

	public MultipartFile getVisa_copy_document_file() {
		return visa_copy_document_file;
	}

	public void setVisa_copy_document_file(MultipartFile visa_copy_document_file) {
		this.visa_copy_document_file = visa_copy_document_file;
	}

	public MultipartFile getResidential_permit_copy_document_file() {
		return residential_permit_copy_document_file;
	}

	public void setResidential_permit_copy_document_file(MultipartFile residential_permit_copy_document_file) {
		this.residential_permit_copy_document_file = residential_permit_copy_document_file;
	}

	public MultipartFile getAiu_equivalence_document_file() {
		return aiu_equivalence_document_file;
	}

	public void setAiu_equivalence_document_file(MultipartFile aiu_equivalence_document_file) {
		this.aiu_equivalence_document_file = aiu_equivalence_document_file;
	}

	public Integer getFrrod_id() {
		return frrod_id;
	}

	public void setFrrod_id(Integer frrod_id) {
		this.frrod_id = frrod_id;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}
	
}
