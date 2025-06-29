package com.au.dto;

import org.springframework.web.multipart.MultipartFile;

public class VendorFileRequest {
	
	private MultipartFile file;
	private Integer vendor_id;
	
	private Integer payment_voucher_id;
	private Integer draft_payment_voucher_id;
	private Integer voucher_no;
	private String  indent_ticket;
	private Integer store_indent_request_id;
	
	private Integer draft_journal_voucher_id;
	private Integer journal_voucher_number;	
	
	private Integer deposited_amount_id;
	
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public Integer getVendor_id() {
		return vendor_id;
	}
	public void setVendor_id(Integer vendor_id) {
		this.vendor_id = vendor_id;
	}
	public Integer getPayment_voucher_id() {
		return payment_voucher_id;
	}
	public void setPayment_voucher_id(Integer payment_voucher_id) {
		this.payment_voucher_id = payment_voucher_id;
	}
	public Integer getDraft_payment_voucher_id() {
		return draft_payment_voucher_id;
	}
	public void setDraft_payment_voucher_id(Integer draft_payment_voucher_id) {
		this.draft_payment_voucher_id = draft_payment_voucher_id;
	}
	public Integer getVoucher_no() {
		return voucher_no;
	}
	public void setVoucher_no(Integer voucher_no) {
		this.voucher_no = voucher_no;
	}
	public String getIndent_ticket() {
		return indent_ticket;
	}
	public void setIndent_ticket(String indent_ticket) {
		this.indent_ticket = indent_ticket;
	}
	public Integer getStore_indent_request_id() {
		return store_indent_request_id;
	}
	public void setStore_indent_request_id(Integer store_indent_request_id) {
		this.store_indent_request_id = store_indent_request_id;
	}
	public Integer getDraft_journal_voucher_id() {
		return draft_journal_voucher_id;
	}
	public void setDraft_journal_voucher_id(Integer draft_journal_voucher_id) {
		this.draft_journal_voucher_id = draft_journal_voucher_id;
	}
	public Integer getJournal_voucher_number() {
		return journal_voucher_number;
	}
	public void setJournal_voucher_number(Integer journal_voucher_number) {
		this.journal_voucher_number = journal_voucher_number;
	}
	
	public Integer getDeposited_amount_id() {
		return deposited_amount_id;
	}
	public void setDeposited_amount_id(Integer deposited_amount_id) {
		this.deposited_amount_id = deposited_amount_id;
	}
	
}
