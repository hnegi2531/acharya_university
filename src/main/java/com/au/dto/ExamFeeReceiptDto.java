package com.au.dto;

import java.util.HashMap;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;

import com.au.model.BankImportTransaction;
import com.au.model.FeeReceipt;
import com.au.model.TallyReceipt;

import lombok.Data;

@Data
public class ExamFeeReceiptDto {
	
	private Integer studentId;
	private Integer acYearId;
	private List<VoucherHeadAndYearSemDto> voucherHeadAndYearSemDtos;
	private Integer schoolId;
	private String remarks;
	private String receivedIn;
	private Boolean active;
	private List<TallyReceipt> tallyReceipt;
	private FeeReceipt feeReceipt;
	private BankImportTransaction bankImportTransaction; 

}
