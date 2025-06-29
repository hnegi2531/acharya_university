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
public class BulkTransactionDTO {
	private String orderID;
	private String transactionID;
	private String paymentId;
	private Date transactionDate;
	private String status;
	private Float amount;
	private String name;
	private String email;
	private String mobile;
}
