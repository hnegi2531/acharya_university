package com.au.dto;

import com.au.model.FeeTemplate;
import com.au.model.FeeTemplateHistory;

public class FeeTemplateHistoryRequest {
	
	 FeeTemplate ftt;
	 FeeTemplateHistory fth;
	 
	public FeeTemplate getFtt() {
		return ftt;
	}
	public void setFtt(FeeTemplate ftt) {
		this.ftt = ftt;
	}
	public FeeTemplateHistory getFth() {
		return fth;
	}
	public void setFth(FeeTemplateHistory fth) {
		this.fth = fth;
	}
	 
	
}
