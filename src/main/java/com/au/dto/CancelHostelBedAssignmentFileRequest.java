package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CancelHostelBedAssignmentFileRequest {
	
	private Integer hostelBedAssignmentId;
	private MultipartFile file;

}
