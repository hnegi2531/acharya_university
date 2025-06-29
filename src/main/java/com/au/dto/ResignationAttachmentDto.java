package com.au.dto;

import java.util.List;

import com.au.model.ResignationAttachment;

public class ResignationAttachmentDto {
	
	private List<ResignationAttachment> rad;
	private Integer resignation_id;
	private Integer emp_id;
	private Boolean active;
	
	public ResignationAttachmentDto() {
		super();
	}

	public List<ResignationAttachment> getRad() {
		return rad;
	}

	public void setRad(List<ResignationAttachment> rad) {
		this.rad = rad;
	}

	public Integer getResignation_id() {
		return resignation_id;
	}

	public void setResignation_id(Integer resignation_id) {
		this.resignation_id = resignation_id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(Integer emp_id) {
		this.emp_id = emp_id;
	}

}
