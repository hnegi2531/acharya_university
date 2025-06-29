package com.au.dto;

import com.au.model.ProctorHeadHistory;
import com.au.model.ProctorStudentAssignment;

public class ProctorHeadHistoryDto {

	ProctorStudentAssignment psa;
	ProctorHeadHistory phh;

	public ProctorStudentAssignment getPsa() {
		return psa;
	}

	public void setPsa(ProctorStudentAssignment psa) {
		this.psa = psa;
	}

	public ProctorHeadHistory getPhh() {
		return phh;
	}

	public void setPhh(ProctorHeadHistory phh) {
		this.phh = phh;
	}

}
