package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveCancelDTO {

	private Integer cancelBy;
	private String cancelComment;
	private Integer leaveApplyId;
}