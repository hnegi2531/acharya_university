package com.au.dto;

import lombok.Data;

@Data
public class GrnDraftJournalVoucherDto {

	private String grn_no;
	private Integer draft_journal_voucher_id;
	private Integer paymentVoucherId;
	private Integer journal_voucher_id;
	private Integer draft_payment_voucher_id;
}
