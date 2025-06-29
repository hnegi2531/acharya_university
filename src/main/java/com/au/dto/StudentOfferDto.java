package com.au.dto;

import java.util.List;

import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;


public class StudentOfferDto {
	
	private Integer candidate_id;
	private Integer student_id;
	
	@Transient
	private MultipartFile file;
	
	@Transient
	private List<MultipartFile> files;

	public StudentOfferDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCandidate_id() {
		return candidate_id;
	}

	public void setCandidate_id(Integer candidate_id) {
		this.candidate_id = candidate_id;
	}


	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Integer getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Integer student_id) {
		this.student_id = student_id;
	}

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
	
	

}
