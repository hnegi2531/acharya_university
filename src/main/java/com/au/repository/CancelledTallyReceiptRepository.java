package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CancelledTallyReceipt;

@Repository
@Transactional
public interface CancelledTallyReceiptRepository extends JpaRepository<CancelledTallyReceipt, Integer> {
	
	@Query(value = "Select new map(tal_rec.cancelled_tally_receipt_id As id,tal_rec.tally_receipt_id As tally_receipt_id, tal_rec.active As active,"
			+ " tal_rec.auid As auid , tal_rec.bank_institute As bank_institute, tal_rec.created_date As created_date,tal_rec.cancel_remarks as cancel_remarks,"
			+ " tal_rec.created_username As created_username, tal_rec.dd_bank_name As dd_bank_name, tal_rec.dd_no As dd_no,"
			+ " tal_rec.deposited_bank As deposited_bank, tal_rec.fee_receipt As fee_receipt, tal_rec.financial_year As financial_year,"
			+ " tal_rec.modified_by As modified_by, tal_rec.modified_date As modified_date, tal_rec.particulars As particulars,"
			+ " tal_rec.received_from As received_from, tal_rec.received_in As received_in, tal_rec.received_type As received_type,"
			+ " tal_rec.remarks As remarks, tal_rec.school_name As school_name, tal_rec.student_id As student_id,tal_rec.cancel_remarks as cancel_remarks,"
			+ " tal_rec.student_name, tal_rec.total As total, tal_rec.total_amount As total_amount,"
			+ " tal_rec.transaction_date As transaction_date, tal_rec.transaction_no As transaction_no,"
			+ " tal_rec.transaction_type As transaction_type, tal_rec.usn As usn, tal_rec.vendor_id As vendor_id,ven.vendor_name As vendor_name)"
			+ " From CancelledTallyReceipt tal_rec Left Join Vendor ven on tal_rec.vendor_id=ven.vendor_id "
			+ "Where CONCAT(IfNull(tal_rec.tally_receipt_id,''),'',IfNull(tal_rec.student_name,''),'',IfNull(tal_rec.created_date,''),'',"
			+ "IfNull(tal_rec.created_username,''),'',IfNull(tal_rec.dd_bank_name,''),'',IfNull(tal_rec.dd_no,''),'',IfNull(tal_rec.deposited_bank,''),'',"
			+ "IfNull(tal_rec.fee_receipt,''),'',IfNull(tal_rec.particulars,''),'',IfNull(tal_rec.received_from,''),'',IfNull(tal_rec.received_in,''),'',"
			+ "IfNull(tal_rec.school_name,''),'',IfNull(ven.vendor_name,''),'',IfNull(tal_rec.transaction_date,'')) LIKE %?1%")
	public Page<Object> getAllCancelledTallyReceipt(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(tal_rec.cancelled_tally_receipt_id As id,tal_rec.tally_receipt_id As tally_receipt_id, tal_rec.active As active,"
			+ " tal_rec.auid As auid , tal_rec.bank_institute As bank_institute, tal_rec.created_date As created_date,tal_rec.cancel_remarks as cancel_remarks,"
			+ " tal_rec.created_username As created_username, tal_rec.dd_bank_name As dd_bank_name, tal_rec.dd_no As dd_no,"
			+ " tal_rec.deposited_bank As deposited_bank, tal_rec.fee_receipt As fee_receipt, tal_rec.financial_year As financial_year,"
			+ " tal_rec.modified_by As modified_by, tal_rec.modified_date As modified_date, tal_rec.particulars As particulars,"
			+ " tal_rec.received_from As received_from, tal_rec.received_in As received_in, tal_rec.received_type As received_type,"
			+ " tal_rec.remarks As remarks, tal_rec.school_name As school_name, tal_rec.student_id As student_id,tal_rec.cancel_remarks as cancel_remarks,"
			+ " tal_rec.student_name, tal_rec.total As total, tal_rec.total_amount As total_amount,"
			+ " tal_rec.transaction_date As transaction_date, tal_rec.transaction_no As transaction_no,"
			+ " tal_rec.transaction_type As transaction_type, tal_rec.usn As usn, tal_rec.vendor_id As vendor_id,ven.vendor_name As vendor_name)"
			+ " From CancelledTallyReceipt tal_rec Left Join Vendor ven on tal_rec.vendor_id=ven.vendor_id")
	public Page<Object> getAllCancelledTallyReceipt2(Pageable pageable);

}
