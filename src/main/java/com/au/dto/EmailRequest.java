package com.au.dto;

import java.util.List;
import com.au.model.HodComments;
import com.au.model.Interview;
import com.au.model.Offer;

public class EmailRequest {

	List<String> emails;

	List<HodComments> hc;

	Interview i;

	InterviewRequest ir;

	Offer o;

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public List<HodComments> getHc() {
		return hc;
	}

	public void setHc(List<HodComments> hc) {
		this.hc = hc;
	}

	public Interview getI() {
		return i;
	}

	public void setI(Interview i) {
		this.i = i;
	}

	public InterviewRequest getIr() {
		return ir;
	}

	public void setIr(InterviewRequest ir) {
		this.ir = ir;
	}

	public Offer getO() {
		return o;
	}

	public void setO(Offer o) {
		this.o = o;
	}

}
