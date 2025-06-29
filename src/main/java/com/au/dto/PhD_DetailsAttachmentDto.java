package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

public class PhD_DetailsAttachmentDto {
	
	private MultipartFile file;
	private Integer phd_id;
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public Integer getPhd_id() {
		return phd_id;
	}
	public void setPhd_id(Integer phd_id) {
		this.phd_id = phd_id;
	}
	
	

}
