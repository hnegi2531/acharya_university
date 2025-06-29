package com.au.dto;

import java.util.List;

import com.au.model.SubjectWorkLoadType;

public class SubjectWorkLoadTypeDto {
	private List<SubjectWorkLoadType> subDto;
	SubjectWorkLoadType swd;
	public SubjectWorkLoadTypeDto() {
		super();
	}

	public List<SubjectWorkLoadType> getSubDto() {
		return subDto;
	}

	public void setSubDto(List<SubjectWorkLoadType> subDto) {
		this.subDto = subDto;
	}

	public SubjectWorkLoadType getSwd() {
		return swd;
	}

	public void setSwd(SubjectWorkLoadType swd) {
		this.swd = swd;
	}

	@Override
	public String toString() {
		return "SubjectWorkLoadTypeDto [subDto=" + subDto + ", swd=" + swd + "]";
	}

	
	
	
}
