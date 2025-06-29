package com.au.dto;

import lombok.Data;

@Data
public class StudentOfferAcceptanceRequest {
	
	
	private Integer candidate_id;
	private  String   accepted_date;
	private  String  ip_address;
	private String offer_voucher_code;
	private Boolean active;


}
