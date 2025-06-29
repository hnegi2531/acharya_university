package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DraftOrderRequestDTO {

	private int pageNo;
	private int pageSize;
	private String sort;
	
	private String vendor;
	private String institute;
	private String createdDate;
	private Integer purchaseApproverId;
	private String poNo;
	private Integer amount;
	private Integer approverId;
	private Integer userId;
	private Integer instituteId;
	private String fromDate;
	private String toDate;
	private String poFilter;
	private String requestType;
}
