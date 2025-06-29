package com.au.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name="cancelled_hostel_fee_receipts")
@Data
public class CancelledHostelFeeReceipts {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer cancelled_hostel_fee_receipts_id;
	
	private Integer fee_receipt;
	private Integer school_id;
	private Integer financial_year_id;
	
	private String cancel_date;
	private String cancel_remarks;
	private Integer cancel_by;
	
	private Float inr_amount;
	private Float paid_amount;
	private Boolean active;

}
