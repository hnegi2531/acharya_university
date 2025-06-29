package com.au.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;
@Data
public class CandidateWalkinAttachmentRequest {

	private MultipartFile file;
	private String attachment_purpose;
}
