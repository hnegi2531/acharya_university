package com.au.dto;

import com.au.model.ApplicantDetails;
import com.au.model.AttachmentSubCategory;
import com.au.model.Candidate_Walkin;
import com.au.model.PGApplicable;
import com.au.model.StudentAttachments;

public class Candidate_walkinRequest {
	
	private Candidate_Walkin cd;
	private StudentAttachments stu_attach;
	private PGApplicable pgapp;
	//private ApplicantDetails ad;
	
	private AttachmentSubCategory as;
	
	public Candidate_Walkin getCd() {
		return cd;
	}
	
	
	public void setCd(Candidate_Walkin cd) {
		cd.setCreated_by(cd.getCreated_by());
		this.cd = cd;
	}
	public StudentAttachments getStu_attach() {
		return stu_attach;
	}
	public void setStu_attach(StudentAttachments stu_attach) {
		this.stu_attach = stu_attach;
	}
	public PGApplicable getPgapp() {
		return pgapp;
	}
	public void setPgapp(PGApplicable pgapp) {
		this.pgapp = pgapp;
	}
	
	public AttachmentSubCategory getAs() {
		return as;
	}
	public void setAs(AttachmentSubCategory as) {
		this.as = as;
	}
}
