package com.au.dto;

import java.util.List;
import lombok.Data;

@Data
public class CandidateWalkinAttachmentsDto {

	
	private List<CandidateWalkinAttachmentRequest> cwar;
	private Integer candidate_id;
	private Boolean active;
}
