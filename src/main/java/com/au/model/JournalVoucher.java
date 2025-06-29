package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@Entity
@Table(name = "journal_voucher")
public class JournalVoucher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer journal_voucher_id;
	
	private Integer journal_voucher_number;			
	private Integer school_id;
	private String date;
	private Integer bank_id;
	private Integer fee_head_id;
	private Double paying_now;
	private String cheque_dd_no;
	private String remarks;
	private String pay_to;
	private String expensense_head;
	private Integer financial_year_id;
	private Double credit;
	private Double  debit;
	private Double credit_total;
 	private Double debit_total;
 	private Integer dept_id;
 	private Integer vendor_active;
 	private Integer vendor_id;
	private Integer salary_structure_head_id;
	private Integer salary_status;
	private Integer month;
	private Integer year;
	private Integer nature_id;
	private Integer cancel_voucher; 
	private String voucher_remarks;
    private Integer cancelled_by;
	private String cancelled_date;
	private Integer draft_journal_voucher_id;
	private String purchase_ref_number;
	private String reference_number;
	private Integer po_bill_id;
	private Integer payment_mode;
	private Integer purchase_order_id;
	private Integer ledger_id;
	private Integer voucher_head_id;     //voucher_head_new_id
	
	private String actual_date;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date created_date;

	@Column(name = "modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date modified_date;

	@Column(updatable = false)
	private Integer created_by;
	private Integer modified_by;
	
	private Boolean active;
	
	@Column(updatable = false)
	private String created_username;
	private String modified_username;
	
	private String contract_number;
	private String attachment_path;
	private String fileType;
	
	private Integer approver_id;
 	private Integer approved_status;
 	private String approved_date;
 	private Integer voucher_head_new_id;
 	private Integer inter_school_id;
 	private Integer more;
	
	@Transient
	private MultipartFile file;

	private String type;
	private String draftCreatedName;
	private Integer env_bill_details_id;
	private String cancelled_remarks;
	
	public JournalVoucher() {
		super();
	}


}
