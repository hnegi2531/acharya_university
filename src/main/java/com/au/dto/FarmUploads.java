package com.au.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class FarmUploads {
	
	private MultipartFile file;
	private Integer teachingsId;
	private Integer professionalActivityId;
	private Integer facultyServiceId;
	private List<Integer> otherPertinentId;
	
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public Integer getTeachingsId() {
		return teachingsId;
	}
	public void setTeachingsId(Integer teachingsId) {
		this.teachingsId = teachingsId;
	}
	public Integer getProfessionalActivityId() {
		return professionalActivityId;
	}
	public void setProfessionalActivityId(Integer professionalActivityId) {
		this.professionalActivityId = professionalActivityId;
	}
	public Integer getFacultyServiceId() {
		return facultyServiceId;
	}
	public void setFacultyServiceId(Integer facultyServiceId) {
		this.facultyServiceId = facultyServiceId;
	}
	public List<Integer> getOtherPertinentId() {
		return otherPertinentId;
	}
	public void setOtherPertinentId(List<Integer> otherPertinentId) {
		this.otherPertinentId = otherPertinentId;
	}
	
	

}
