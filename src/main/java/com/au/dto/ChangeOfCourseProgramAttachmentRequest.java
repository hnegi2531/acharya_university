package com.au.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class ChangeOfCourseProgramAttachmentRequest {
	

	private MultipartFile file;
	private Integer newStudentId;
	private Integer amount;
	private String remarks;
	

}
