package com.au.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;


import lombok.Data;

@Data
public class FeePaymentWindowDto {
	
	private Integer fee_payment_window_id;

	private Integer school_id;
	private String voucher_head_new_id;
	private Integer user_id;
	private Double amount;
	private Integer fixed;
	private Boolean externalStatus;
	private String remarks;
	private String attachmentPath;
	private String attachmentFile;
	private String window_type;
	private Date fromDate;
	private Date toDate;
	private String program_id;
	private Boolean Status;
	private Boolean active;
}
