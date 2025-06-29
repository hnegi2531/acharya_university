package com.au.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvPayDTO {
	
	private String employeeName;
	
	private String empCode;
	
	private  Double invPay;
	
    private Integer month;
	
	private Integer year;
	
	private String remarks;
	
	private String type;

}
