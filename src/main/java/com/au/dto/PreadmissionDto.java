package com.au.dto;

import com.au.model.Candidate_Walkin;
import com.au.model.PreAdmissionProcess;
import com.au.model.Scholarship;
import com.au.model.ScholarshipApprovalStatus;

public class PreadmissionDto {

	private PreAdmissionProcess pap;
	private Candidate_Walkin cw;
	private Scholarship s;
	private ScholarshipApprovalStatus sas;

	public PreAdmissionProcess getPap() {
		return pap;
	}
	public void setPap(PreAdmissionProcess pap) {
		this.pap = pap;
	}
	public Candidate_Walkin getCw() {
		return cw;
	}
	public void setCw(Candidate_Walkin cw) {
		this.cw = cw;
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
	
	
	
}
