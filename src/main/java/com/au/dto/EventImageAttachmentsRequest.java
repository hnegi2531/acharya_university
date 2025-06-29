package com.au.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class EventImageAttachmentsRequest {
	
	private List<MultipartFile> file;
	private Integer event_id;
	private String image_upload_timing;
	private Boolean active;
	
	public EventImageAttachmentsRequest() {
		super();
	}

	public List<MultipartFile> getFile() {
		return file;
	}

	public void setFile(List<MultipartFile> file) {
		this.file = file;
	}

	public Integer getEvent_id() {
		return event_id;
	}

	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}

	public String getImage_upload_timing() {
		return image_upload_timing;
	}

	public void setImage_upload_timing(String image_upload_timing) {
		this.image_upload_timing = image_upload_timing;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
}
