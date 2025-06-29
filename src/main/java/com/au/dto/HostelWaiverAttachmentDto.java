package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

public class HostelWaiverAttachmentDto {
	
	private MultipartFile file;
	private Integer hostel_waiver_id;
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public Integer getHostel_waiver_id() {
		return hostel_waiver_id;
	}
	public void setHostel_waiver_id(Integer hostel_waiver_id) {
		this.hostel_waiver_id = hostel_waiver_id;
	}
	
}
