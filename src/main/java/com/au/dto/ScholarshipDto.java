package com.au.dto;

import com.au.model.Scholarship;
import com.au.model.ScholarshipApprovalStatus;
import com.au.model.ScholarshipAttachment;

public class ScholarshipDto {

	private Scholarship s;
	private ScholarshipApprovalStatus sas;
	private ScholarshipAttachment sa;
	public ScholarshipDto() {
		super();
	}
	public Scholarship getS() {
		return s;
	}
	public void setS(Scholarship s) {
		this.s = s;
	}
	public ScholarshipApprovalStatus getSas() {
		return sas;
	}
	public void setSas(ScholarshipApprovalStatus sas) {
		this.sas = sas;
	}
	public ScholarshipAttachment getSa() {
		return sa;
	}
	public void setSa(ScholarshipAttachment sa) {
		this.sa = sa;
	}
	
	
}
