package com.au.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.BulkFeeReceipt;

@Repository
@Transactional
public interface BulkFeeReceiptRepository extends JpaRepository<BulkFeeReceipt, Integer> {
	
	
	@Query(value = "select bfr from BulkFeeReceipt bfr where bfr.active=true")
	public List<BulkFeeReceipt> findAll11();
	
//	@Query(value = "select new map(vb.ob_id as id,vb.school_id as school_id,vb.opening_balance as opening_balance,"
//			+ "vb.vendor_id as vendor_id,vb.created_by as created_by,vb.modified_by as modified_by,"
//			+ "vb.created_date as created_date,vb.modified_date as modified_date,vb.active as active,"
//			+ "vb.created_username as created_username,vb.modified_username as modified_username,"
//			+ "sch.school_name_short as school_name_short,ve.vendor_name as vendor_name) from VendorOpeningBalance vb "
//			+ "Left join Schools sch on sch.school_id=vb.school_id "
//			+ "Left join Vendor ve on ve.vendor_id=vb.vendor_id "
//			+ "where CONCAT(IfNull(vb.ob_id,''),'',IfNull(vb.school_id,''),'',IfNull(vb.opening_balance,''),"
//			+ "'',IfNull(vb.vendor_id,''),'',IfNull(vb.created_date,''),'',IfNull(vb.created_by,'')) LIKE %?1%")
//	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
//	
//	@Query(value = "select new map(vb.ob_id as id,vb.school_id as school_id,vb.opening_balance as opening_balance,"
//			+ "vb.vendor_id as vendor_id,vb.created_by as created_by,vb.modified_by as modified_by,"
//			+ "vb.created_date as created_date,vb.modified_date as modified_date,vb.active as active,"
//			+ "vb.created_username as created_username,vb.modified_username as modified_username,"
//			+ "sch.school_name_short as school_name_short,ve.vendor_name as vendor_name) from VendorOpeningBalance vb "
//			+ "Left join Schools sch on sch.school_id=vb.school_id "
//			+ "Left join Vendor ve on ve.vendor_id=vb.vendor_id")
//	public Page<Object> getAllSortedData(Pageable pageable);
//	
	@Modifying
	@Query(value = "update BulkFeeReceipt bfr set bfr.active=false where bfr.bulk_fee_receipt_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update BulkFeeReceipt bfr set bfr.active=true where bfr.bulk_fee_receipt_id=?1")
	public void update1(Integer id);
	
	@Query(value = "Select ifNull(Max(bulk_fee_receipt),0) From bulk_fee_receipt where active=true",nativeQuery=true)
	public Integer getMaxId();
	
//	@Query(value ="Select distinct bfr.financial_year_id "
//			+ "from bulk_fee_receipt bfr "
//			+ "Where bfr.fee_receipt_id=?1 And bfr.transaction_type=?2  "
//			+ "And bfr.voucher_head_new_id=?3 And bfr.active=true",nativeQuery=true)
//	public List<Integer> getPaidYears(Integer fee_receipt_id,String transaction_type, Integer voucher_head_new_id);
	
	@Query(value ="select bfr.voucher_head_new_id as voucher_head_new_id,vhn.voucher_head as voucher_head,fr.fee_receipt as fee_receipt,"
			+ "bfr.amount_in_som as amount_in_som,bfr.amount as amount,fr.created_date as receipt_date,fr.created_username as cashier, fr.transaction_type as transaction_type, "
			+ "bfr.remarks as remarks,bfr.voucher_head_new_id as voucher_head_new_id,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,bfr.from_name as from_name from BulkFeeReceipt bfr "
			+ "Left join VoucherHeadNew vhn on bfr.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "Left join FeeReceipt fr on bfr.bulk_fee_receipt=fr.bulk_id "
			+ "where fr.fee_receipt_id=?1 and bfr.transaction_type=?2 and bfr.active=true and fr.active = true ")
	public List<Map<String, Object>> getBulkFeeReceiptCashData(Integer fee_receipt_id, String transaction_type);
	
	@Query(value ="select bfr.voucher_head_new_id as voucher_head_new_id,vhn.voucher_head as voucher_head,fr.fee_receipt as fee_receipt,"
			+ "bfr.amount_in_som as amount_in_som,bfr.amount as amount,fr.created_date as receipt_date,fr.created_username as cashier,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,bfr.from_name as from_name,bfr.remarks as remarks,bfr.voucher_head_new_id as voucher_head_new_id,"
			+ "bit.transaction_date as transaction_date,bit.transaction_no as transaction_no,bit.transaction_remarks as transaction_remarks, fr.transaction_type as transaction_type, fr.transactionMode as transaction_mode "
			+ "from BulkFeeReceipt bfr "
			+ "Left join VoucherHeadNew vhn on bfr.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "Left join FeeReceipt fr on bfr.bulk_fee_receipt=fr.bulk_id "
			+ "Left join BankImportTransaction bit on fr.bank_transaction_history_id = bit.bank_import_transaction_id  "
			+ "where fr.fee_receipt_id=?1 and fr.transaction_type=?2 and bfr.active=true and fr.active = true and fr.financial_year_id = ?3 ")
	public List<Map<String, Object>> getBulkFeeReceiptRtgsData(Integer fee_receipt_id, String transaction_type,Integer financial_year_id);
	
//	@Query(value ="Select  bfr.amount_in_som "
//			+ "from bulk_fee_receipt bfr "
//			+ "Where bfr.fee_receipt_id=?1 And bfr.financial_year_id=?2 And bfr.transaction_type=?3 And "
//			+ "bfr.voucher_head_new_id=?4 And bfr.active=true",nativeQuery=true)
//	public Double getAmount(Integer fee_receipt_id,Integer fin_year,String transaction_type,Integer vou);
//	
	@Query(value = "Select fr.created_username as created_username,sd.father_name as father_name,fr.fee_receipt as fee_receipt,"
	 		+ "fr.created_date as created_date,fr.created_by as created_by,fr.fee_receipt_id as fee_receipt_id,"
	 		+ "fr.paid_amount as paid_amount,fr.paid_year as paid_year,fr.amount_in_som as amount_in_som,"
	 		+ "fr.receipt_type as receipt_type,sc.school_name as school_name,"
	 		+ "dd.dd_number As dd_number,dd.dd_date As dd_date,dd.bank_name As dd_bank_name,"
	 		+ "fr.active as active,sd.student_name as student_name,sd.auid as auid,sd.usn as usn,sd.mobile as mobile,"
	 		+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year "
	 		+ "From fee_receipt fr "
	 		+ "left join student_details sd on fr.student_id = sd.student_id "
	 		+ "left join dd_details dd on dd.student_id = sd.student_id "
	 		+ "left join schools sc on fr.school_id=sc.school_id "
	 		+ "left join financial_year fy on fr.financial_year_id=fy.financial_year_id "
	 		+ "Where fr.fee_receipt_id=?1 And fr.active=true",nativeQuery=true)
	public List<Map<String, Object>> getDataForDisplayingStudentDetailsBulkFeeRcpt(Integer fee_receipt_id);
	
	@Query(value ="SELECT " +
			"    bfr.voucher_head_new_id AS voucher_head_new_id, " +
			"    vhn.voucher_head AS voucher_head, " +
			"    fr.fee_receipt AS fee_receipt, " +
			"    bfr.amount_in_som AS amount_in_som, " +
			"    bfr.amount AS amount, " +
			"    fr.created_date AS receipt_date, " +
			"    fr.created_username AS cashier, " +
			"    bfr.student_id AS student_id, " +
			"    bfr.remarks AS remarks, " +
			"    vhn.voucher_head_short_name AS voucher_head_short_name, " +
			"    bfr.from_name AS from_name, " +
			"    fr.transaction_type AS transaction_type, " +
			"    fr.transaction_mode AS transaction_mode " +
			"FROM  " +
			"    bulk_fee_receipt bfr " +
			"LEFT JOIN " +
			"    voucher_head_new vhn ON bfr.voucher_head_new_id = vhn.voucher_head_new_id " +
			"LEFT JOIN  " +
			"    fee_receipt fr ON bfr.bulk_fee_receipt = fr.bulk_id " +
			"WHERE " +
			"    bfr.student_id = ?1 " +
			"    AND fr.fee_receipt_id = ?2 " +
			"    AND fr.transaction_type = ?3 " +
			"    AND fr.financial_year_id = ?4 " +
			"    AND bfr.active = TRUE " +
			"    AND fr.active = TRUE ", nativeQuery = true)
	public List<Map<String, Object>> getBulkFeeReceiptCashDataByStudentId(Integer student_id, Integer fee_receipt_id, String transaction_type,Integer financial_year_id);
	
	@Query(value ="SELECT " +
			"    bfr.voucher_head_new_id AS voucher_head_new_id, " +
			"    vhn.voucher_head AS voucher_head, " +
			"    fr.fee_receipt AS fee_receipt, " +
			"    bfr.amount_in_som AS amount_in_som, " +
			"    bfr.amount AS amount, " +
			"    fr.created_date AS receipt_date, " +
			"    fr.created_username AS cashier, " +
			"    vhn.voucher_head_short_name AS voucher_head_short_name, " +
			"    bfr.from_name AS from_name, " +
			"    bfr.student_id AS student_id, " +
			"    bit.transaction_date AS transaction_date, " +
			"    bit.transaction_no AS transaction_no, " +
			"    bit.transaction_remarks AS transaction_remarks, " +
			"    fr.transaction_type AS transaction_type, " +
			"    fr.transaction_mode AS transaction_mode " +
			"FROM  " +
			"    bulk_fee_receipt bfr " +
			"LEFT JOIN  " +
			"    voucher_head_new vhn ON bfr.voucher_head_new_id = vhn.voucher_head_new_id " +
			"LEFT JOIN  " +
			"    fee_receipt fr ON bfr.bulk_fee_receipt = fr.bulk_id " +
			"LEFT JOIN " +
			"    bank_import_transaction bit ON fr.bank_transaction_history_id = bit.bank_import_transaction_id " +
			"WHERE " +
			"    bfr.student_id = ?1 " +
			"    AND fr.fee_receipt_id = ?2 " +
			"    AND fr.transaction_type = ?3 " +
			"    AND fr.financial_year_id = ?4 " +
			"    AND bfr.active = TRUE ", nativeQuery = true)
	public List<Map<String, Object>> getBulkFeeReceiptRtgsDataByStudentId(Integer student_id,Integer fee_receipt_id, String transaction_type,Integer financial_year_id);

	@Query(value = "Select fr.created_username as created_username,sd.father_name as father_name,fr.fee_receipt as fee_receipt,"
	 		+ "fr.created_date as created_date,fr.created_by as created_by,fr.fee_receipt_id as fee_receipt_id,"
	 		+ "fr.paid_amount as paid_amount,fr.paid_year as paid_year,fr.amount_in_som as amount_in_som,"
	 		+ "fr.receipt_type as receipt_type,sc.school_name as school_name,"
	 		+ "fr.active as active,sd.student_name as student_name,sd.auid as auid,sd.usn as usn,sd.mobile as mobile,"
	 		+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year "
	 		+ "From fee_receipt fr "
	 		+ "left join student_details sd on fr.student_id = sd.student_id "
	 		+ "left join schools sc on fr.school_id=sc.school_id "
	 		+ "left join financial_year fy on fr.financial_year_id=fy.financial_year_id "
	 		+ "Where fr.student_id=?1 and fr.fee_receipt_id=?2 And fr.financial_year_id=?3 And fr.active=true",nativeQuery=true)
	public List<Map<String, Object>> getDataForDisplayingStudentDetailsBulkFeeRcptByStudentId(Integer student_id,Integer fee_receipt_id,Integer financial_year_id);
	
	@Query(value ="select bfr.voucher_head_new_id as voucher_head_new_id,vhn.voucher_head as voucher_head,fr.fee_receipt as fee_receipt,"
			+ "bfr.amount_in_som as amount_in_som,bfr.amount as amount,fr.created_date as receipt_date,fr.created_username as cashier,"
			+ "bfr.remarks as remarks,bfr.fee_receipt_id as fee_receipt_id,bfr.transaction_type as transaction_type,"
			+ "bit.cheque_dd_no as cheque_dd_no,bit.transaction_date as transaction_date,b.bank_name as bank_name,b.bank_short_name as bank_short_name,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,bfr.from_name as from_name from BulkFeeReceipt bfr "
			+ "Left join VoucherHeadNew vhn on bfr.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "Left join FeeReceipt fr on bfr.fee_receipt_id=fr.fee_receipt_id "
			+ "left join BankImportTransaction bit on fr.bank_transaction_history_id=bit.bank_import_transaction_id "
			+ "left join Bank b on b.bank_id=bit.deposited_bank_id "
			+ "where fr.financial_year_id=?1 and fr.school_id=?2 and fr.fee_receipt=?3 and bfr.active=true")
	public List<Map<String, Object>> getDataForDisplayingBulkFeeReceiptAndCancel(Integer financial_year_id, Integer school_id, String fee_receipt);
	
	@Query(value = "Select fr.created_username as created_username,sd.father_name as father_name,fr.fee_receipt as fee_receipt,"
	 		+ "fr.created_date as created_date,fr.created_by as created_by,fr.fee_receipt_id as fee_receipt_id,"
	 		+ "fr.paid_amount as paid_amount,fr.paid_year as paid_year,fr.amount_in_som as amount_in_som,"
	 		+ "fr.receipt_type as receipt_type,sc.school_name as school_name,ft.fee_template_name as fee_template_name,"
	 		+ "fr.active as active,sd.student_id as student_id,sd.student_name as student_name,sd.auid as auid,sd.usn as usn,sd.mobile as mobile,"
	 		+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year "
	 		+ "From fee_receipt fr "
	 		+ "left join student_details sd on fr.student_id = sd.student_id "
	 		+ "left join fee_template ft on ft.fee_template_id=sd.fee_template_id "
	 		+ "left join schools sc on fr.school_id=sc.school_id "
	 		+ "left join financial_year fy on fr.financial_year_id=fy.financial_year_id "
	 		+ "Where fr.fee_receipt=?1 And fr.active=true",nativeQuery=true)
	public List<Map<String, Object>> getDataForDisplayingStudentDetailsBulkFeeRcptAndCancel(String fee_receipt);

	@Modifying
	@Query(value = "Update BulkFeeReceipt bfr Set bfr.active=false Where bfr.fee_receipt_id=?1")
	public void updateBulkFeeReceipt(Integer fee_receipt_id);

	
	@Query(value = "Select bfr From BulkFeeReceipt bfr where bfr.fee_receipt_id =?1")
	public List<BulkFeeReceipt> getBulkFeeReceiptDetails(Integer fee_receipt_id);

	@Query(value = "select ( (SELECT round(COALESCE(SUM(amount), 0),2) FROM bulk_fee_receipt where voucher_head_new_id=?1 and financial_year_id=?2 and school_id=?3 and  active=true) + "
			+ "(SELECT COALESCE(SUM(paid_amount), 0) FROM student_payment_history where voucher_head_new_id=?1 and financial_year_id=?2 and school_id=?3 and active=true))  as credit_amount",nativeQuery=true)
	public Double getSumOfCreditFromAllTables(Integer voucher_head_new_id, Integer financial_year_id, Integer school_id);

	@Query(value ="select bfr.voucher_head_new_id as voucher_head_new_id,vhn.voucher_head as voucher_head,fr.fee_receipt as fee_receipt,"
			+ "bfr.amount_in_som as amount_in_som,bfr.amount as amount,fr.created_date as receipt_date,fr.created_username as cashier,"
			+ "bfr.remarks as remarks,bfr.fee_receipt_id as fee_receipt_id,bfr.transaction_type as transaction_type,"
			+ "bit.cheque_dd_no as cheque_dd_no,bit.transaction_date as transaction_date,b.bank_name as bank_name,b.bank_short_name as bank_short_name,"
			+ "vhn.voucher_head_short_name as voucher_head_short_name,bfr.from_name as from_name from bulk_fee_receipt bfr "
			+ "Left join voucher_head_new vhn on bfr.voucher_head_new_id=vhn.voucher_head_new_id "
			+ "Left join fee_receipt fr on bfr.fee_receipt_id=fr.fee_receipt_id "
			+ "left join bank_import_transaction bit on fr.bank_transaction_history_id=bit.bank_import_transaction_id "
			+ "left join bank b on b.bank_id=bit.deposited_bank_id "
			+ "where bfr.school_id=?1 and bfr.voucher_head_new_id=?2 and bfr.financial_year_id=?3 and date(bfr.created_date)=date(?4) and bfr.active=true",nativeQuery=true)
	public List<Map<String, Object>> getTotalCreditDetailsBulkFeeRcpt(Integer school_id, Integer voucher_head_new_id,
			Integer financial_year_id, Date fromdate);

	@Query(value="select b from BulkFeeReceipt b where b.bankImportTransactionId=:bank_import_transaction_id and b.active = true ")
	public List<BulkFeeReceipt> getBulkFeeReceiptByBankImportId(Integer bank_import_transaction_id);
	
	
   public BulkFeeReceipt findByOrderId(String orderId);

	@Modifying
	@Transactional
	@Query(value = "Update BulkFeeReceipt bfr Set bfr.active=false Where bfr.bulk_fee_receipt=?1")
	public void updateBulkFeeReceiptByBulkId(Integer bulkId);


}
