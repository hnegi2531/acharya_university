package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.TallyReceipt;

@Transactional
@Repository
public interface TallyReceiptRepository extends JpaRepository <TallyReceipt , Integer> {

	@Query(value = "Select tal_rec From TallyReceipt tal_rec where tal_rec.active = true")
	public List<TallyReceipt> getActiveTallyReceipt();

	@Query(value = "Select new map(tal_rec.tally_receipt_id As id, tal_rec.active As active,"
			+ " tal_rec.auid As auid , tal_rec.bank_institute As bank_institute, tal_rec.created_date As created_date,"
			+ " tal_rec.created_username As created_username, tal_rec.dd_bank_name As dd_bank_name, tal_rec.dd_no As dd_no,"
			+ " tal_rec.deposited_bank As deposited_bank, tal_rec.fee_receipt As fee_receipt, tal_rec.financial_year As financial_year,"
			+ " tal_rec.modified_by As modified_by, tal_rec.modified_date As modified_date, tal_rec.particulars As particulars,"
			+ " tal_rec.received_from As received_from, tal_rec.received_in As received_in, tal_rec.received_type As received_type,"
			+ " tal_rec.remarks As remarks, tal_rec.school_name As school_name, tal_rec.student_id As student_id,"
			+ " tal_rec.student_name, tal_rec.total As total, tal_rec.total_amount As total_amount,"
			+ " tal_rec.transaction_date As transaction_date, tal_rec.transaction_no As transaction_no,"
			+ " tal_rec.transaction_type As transaction_type, tal_rec.usn As usn, tal_rec.vendor_id As vendor_id,ven.vendor_name As vendor_name)"
			+ " From TallyReceipt tal_rec Left Join Vendor ven on tal_rec.vendor_id=ven.vendor_id "
			+ "Where CONCAT(IfNull(tal_rec.tally_receipt_id,''),'',IfNull(tal_rec.student_name,''),'',IfNull(tal_rec.created_date,''),'',"
			+ "IfNull(tal_rec.created_username,''),'',IfNull(tal_rec.dd_bank_name,''),'',IfNull(tal_rec.dd_no,''),'',IfNull(tal_rec.deposited_bank,''),'',"
			+ "IfNull(tal_rec.fee_receipt,''),'',IfNull(tal_rec.particulars,''),'',IfNull(tal_rec.received_from,''),'',IfNull(tal_rec.received_in,''),'',"
			+ "IfNull(tal_rec.school_name,''),'',IfNull(ven.vendor_name,''),'',IfNull(tal_rec.transaction_date,'')) LIKE %?1%")
	public Page<Object> getAllTallyReceipt2(Pageable pageable, Object keyword);

	@Query(value = "Select new map(tal_rec.tally_receipt_id As id, tal_rec.active As active,"
			+ " tal_rec.auid As auid , tal_rec.bank_institute As bank_institute, tal_rec.created_date As created_date,"
			+ " tal_rec.created_username As created_username, tal_rec.dd_bank_name As dd_bank_name, tal_rec.dd_no As dd_no,"
			+ " tal_rec.deposited_bank As deposited_bank, tal_rec.fee_receipt As fee_receipt, tal_rec.financial_year As financial_year,"
			+ " tal_rec.modified_by As modified_by, tal_rec.modified_date As modified_date, tal_rec.particulars As particulars,"
			+ " tal_rec.received_from As received_from, tal_rec.received_in As received_in, tal_rec.received_type As received_type,"
			+ " tal_rec.remarks As remarks, tal_rec.school_name As school_name, tal_rec.student_id As student_id,"
			+ " tal_rec.student_name, tal_rec.total As total, tal_rec.total_amount As total_amount,"
			+ " tal_rec.transaction_date As transaction_date, tal_rec.transaction_no As transaction_no,"
			+ " tal_rec.transaction_type As transaction_type, tal_rec.usn As usn, tal_rec.vendor_id As vendor_id,ven.vendor_name As vendor_name)"
			+ " From TallyReceipt tal_rec Left Join Vendor ven on tal_rec.vendor_id=ven.vendor_id")
	public Page<Object> getAllTallyReceipt3(Pageable pageable);

	@Modifying
	@Query(value = "Update TallyReceipt tal_rec Set active = false Where tally_receipt_id=?1")
	public void delete1(Integer tally_receipt_id);

	@Modifying
	@Query(value = "Update TallyReceipt tal_rec Set active = true Where tally_receipt_id=?1")
	public void delete2(Integer tally_receipt_id);

	@Modifying
	@Transactional
	@Query(value = "Update TallyReceipt tal Set tal.active=false Where tal.fee_receipt_id=?1")
	public void updateTallyReceipt(Integer fee_receipt_id);

	@Query("SELECT t FROM TallyReceipt t WHERE t.fee_receipt_id = :feeReceiptId AND t.active = true ")
	List<TallyReceipt> findByFeeReceiptID(@Param("feeReceiptId") Integer feeReceiptId);

	@Query("SELECT t FROM TallyReceipt t WHERE t.transaction_no = :transactionNo and t.active = true ")
	List<TallyReceipt> findByTransactionNo(String transactionNo);

	
	@Modifying
	@Query(value = "Update TallyReceipt tr Set tr.active=false Where tr.fee_receipt=?1 And tr.received_type=?2 And tr.student_id=?3")
	public void deactive(String fee_receipt, String receipt_type, Integer student_id);

	@Query("SELECT t FROM TallyReceipt t WHERE t.fee_receipt_id =?1")
	public List<TallyReceipt> getTallyReceiptData(Integer fee_receipt_id);
}
