package com.au.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class FeeHeadAmountRestrictionAttachmentRequest {
	
	private List<Integer> feeHeadAmountRestrictionIds;
	private MultipartFile file;

}
