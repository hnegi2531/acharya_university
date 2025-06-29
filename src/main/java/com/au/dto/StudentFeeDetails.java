package com.au.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFeeDetails {

	private String auid;
	private String feeReceiptNo;
	private Date receiptDate;
	private Float transactionAmount;
	private String transactionDate;
	private String transaction_type;
	private Integer financial_year_id;
	private String financial_year;	
	private String transaction_no;
	private Double receiptAmount;
	private String chequeDDno;
	
}
