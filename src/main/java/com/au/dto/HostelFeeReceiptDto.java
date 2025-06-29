package com.au.dto;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HostelFeeReceiptDto {
	
	private String createdUsername;
	private Date created_date;
	private Integer createdBy;
	private Integer feeReceiptId;
	private Boolean active;
	private String studentName;
	private String auid;
	private String usn;
	private String acYear;
	private Integer schoolId;
    private String schoolName;
    private String schoolNameShort;
    private String financialYear;
    private String acharyaEmail;
    private String transactionNo;
    private Integer bankTransactionHistoryId;
    private String chequeDdNo;
    private String transactionDate;
    private String transactionType;
    private String transactionMode;
    private Integer hostelStatus;
    private String  voucherHead;
    private Integer voucherHeadNewId;
    private Float totalAmount;
    private Float payingAmount;
    private Float balanceAmount;
    private String remarks;
	private String receivedFrom;
	private String cashier;
    
    
    public HostelFeeReceiptDto(String createdUsername, Date created_date, Integer createdBy, Integer feeReceiptId,
            Boolean active, String studentName, String auid, String usn, String acYear, Integer schoolId,
            String schoolName, String schoolNameShort, String financialYear, String acharyaEmail,
            String transactionNo, Integer bankTransactionHistoryId, String chequeDdNo,
            String transactionDate, String transactionType, String transactionMode, Integer hostelStatus,
            String voucherHead, Integer voucherHeadNewId, Float totalAmount, Float payingAmount,
            Float balanceAmount, String remarks, String receivedFrom, String cashier) {

		this.createdUsername = createdUsername;
		this.created_date = created_date;
		this.createdBy = createdBy;
		this.feeReceiptId = feeReceiptId;
		this.active = active;
		this.studentName = studentName;
		this.auid = auid;
		this.usn = usn;
		this.acYear = acYear;
		this.schoolId = schoolId;
		this.schoolName = schoolName;
		this.schoolNameShort = schoolNameShort;
		this.financialYear = financialYear;
		this.acharyaEmail = acharyaEmail;
		this.transactionNo = transactionNo;
		this.bankTransactionHistoryId = bankTransactionHistoryId;
		this.chequeDdNo = chequeDdNo;
		this.transactionDate = transactionDate;
		this.transactionType = transactionType;
		this.transactionMode = transactionMode;
		this.hostelStatus = hostelStatus;
		this.voucherHead = voucherHead;
		this.voucherHeadNewId = voucherHeadNewId;
		this.totalAmount = totalAmount;
		this.payingAmount = payingAmount;
		this.balanceAmount = balanceAmount;
		this.remarks = remarks;
		this.receivedFrom = receivedFrom;
		this.cashier = cashier;
		}


}
