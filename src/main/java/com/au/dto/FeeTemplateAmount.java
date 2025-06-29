package com.au.dto;

import java.util.List;
import com.au.model.ApproveHistory;
import com.au.model.FeeTemplate;
import com.au.model.FeeTemplateHistory;
import com.au.model.FeeTemplateSubAmount;
import com.au.model.FeeTemplateSubAmountHistory;

public class FeeTemplateAmount {

	FeeTemplate ft;

	List<FeeTemplateSubAmount> ftsa;
	
	ApproveHistory ah;
	
	List<FeeTemplateSubAmountHistory> ftsah;
	
	FeeTemplateHistory fth;

	public FeeTemplate getFt() {
		return ft;
	}

	public void setFt(FeeTemplate ft) {
		this.ft = ft;
	}

	public List<FeeTemplateSubAmount> getFtsa() {
		return ftsa;
	}

	public void setFtsa(List<FeeTemplateSubAmount> ftsa) {
		this.ftsa = ftsa;
	}

	public ApproveHistory getAh() {
		return ah;
	}

	public void setAh(ApproveHistory ah) {
		this.ah = ah;
	}

	public List<FeeTemplateSubAmountHistory> getFtsah() {
		return ftsah;
	}

	public void setFtsah(List<FeeTemplateSubAmountHistory> ftsah) {
		this.ftsah = ftsah;
	}

	public FeeTemplateHistory getFth() {
		return fth;
	}

	public void setFth(FeeTemplateHistory fth) {
		this.fth = fth;
	}
}
