package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

public class TutionFeeWaiverAttachmentFileRequestDto {
	
	private MultipartFile file;
	private Integer tution_fee_waiver_id;
	
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public Integer getTution_fee_waiver_id() {
		return tution_fee_waiver_id;
	}
	public void setTution_fee_waiver_id(Integer tution_fee_waiver_id) {
		this.tution_fee_waiver_id = tution_fee_waiver_id;
	}
	
	

}
