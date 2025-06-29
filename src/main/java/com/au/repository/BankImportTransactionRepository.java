package com.au.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.BankImportTransaction;


@Transactional
@Repository
public interface BankImportTransactionRepository extends JpaRepository<BankImportTransaction, Integer> {

	
	@Query(value = "SELECT bit from BankImportTransaction bit where bit.active=true")
	public List<BankImportTransaction> findAll11();


	@Query(value = "select new map(bit.bank_import_transaction_id as id,bit.transaction_date as transaction_date, " +
			" bit.cheque_dd_no as bank_details,bit.deposited_bank_id as deposited_bank_id,bit.receipt_no as receipt_no, " +
			" bit.student_id as student_id,bit.amount as amount,bit.start_row as start_row,bit.end_row as end_row,bit.voucher_head_new_id as voucher_head_new_id, " +
			" bit.transaction_remarks as transaction_remarks,bit.fc_year_id as fc_year_id,bit.transaction_no as pay_id, " +
			" bit.dollor as dollor,bit.dollor_rate as dollor_rate,bit.school_id as school_id,bit.paid as paid,bit.balance as balance, " +
			" bit.created_username as created_username,bit.modified_username as modified_username,bit.created_Date as import_date, " +
			" bit.modified_Date as modified_Date,bit.created_by as created_by,bit.modified_by as modified_by,bit.active as active, " +
			" vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,bit.total_usd as total_usd,bit.exachange_rate as exachange_rate, " +
			" s.school_name as school_name,s.school_name_short as inst,bit.order_id As order_id,bit.settlement_id As settlement_id, " +
			" bit.bank_usd_amt as bank_usd_amt,bit.bank_inr_amt as bank_inr_amt, bit.transactionType as transaction_type, sd.auid as auid, sd.acharya_email as email, " +
			"sd.mobile as phone_no, bank.bank_name as bank ) from BankImportTransaction bit  " +
			" left join Schools s on s.school_id = bit.school_id  " +
			" left join VoucherHeadNew vh on vh.voucher_head_new_id = bit.voucher_head_new_id  " +
			" left join Bank bank on bank.bank_id = bit.deposited_bank_id " +
			" left join Student_Details sd on sd.student_id  = bit.student_id "+
			"where bit.balance > 0 " +
			" and bit.receiptStatus = 'P' " +
			" and bit.active = true " +
			"and CONCAT(IfNull(bit.created_username,''),'',IfNull(bit.cheque_dd_no,''),'',IfNull(bit.deposited_bank_id,''),''," +
			"IfNull(bit.created_by,''),'',IfNull(bit.created_Date,'') " +
			" ) LIKE %:keyword% " +
			" AND (DATE(bit.created_Date) >= :minDate) " +
			" AND (:start IS NULL OR DATE(bit.created_Date) >= :start) " +
			" AND (:end IS NULL OR DATE(bit.created_Date) <= :end) " +
			" AND (:school_id IS NULL OR bit.school_id = :school_id) " +
	        " AND (:bankId IS NULL OR bit.deposited_bank_id = :bankId)  ")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer school_id, Date start, Date end, Date minDate, Integer bankId);


	@Query(value = "select new map(bit.bank_import_transaction_id as id,bit.transaction_date as transaction_date, " +
			" bit.cheque_dd_no as bank_details,bit.deposited_bank_id as deposited_bank_id,bit.receipt_no as receipt_no, " +
			" bit.student_id as student_id,bit.amount as amount,bit.start_row as start_row,bit.end_row as end_row,bit.voucher_head_new_id as voucher_head_new_id, " +
			" bit.transaction_remarks as transaction_remarks,bit.fc_year_id as fc_year_id,bit.transaction_no as pay_id, " +
			" bit.dollor as dollor,bit.dollor_rate as dollor_rate,bit.school_id as school_id,bit.paid as paid,bit.balance as balance, " +
			" bit.created_username as created_username,bit.modified_username as modified_username,bit.created_Date as import_date, " +
			" bit.modified_Date as modified_Date,bit.created_by as created_by,bit.modified_by as modified_by,bit.active as active, " +
			" vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,bit.total_usd as total_usd,bit.exachange_rate as exachange_rate, " +
			" s.school_name as school_name,s.school_name_short as inst,bit.order_id As order_id,bit.settlement_id As settlement_id, " +
			" bit.bank_usd_amt as bank_usd_amt,bit.bank_inr_amt as bank_inr_amt, bit.transactionType as transaction_type, sd.auid as auid, sd.acharya_email as email, " +
			"sd.mobile as phone_no, bank.bank_name as bank ) from BankImportTransaction bit  " +
			" left join Schools s on s.school_id = bit.school_id  " +
			" left join VoucherHeadNew vh on vh.voucher_head_new_id = bit.voucher_head_new_id  " +
			" left join Bank bank on bank.bank_id = bit.deposited_bank_id " +
			" left join Student_Details sd on sd.student_id  = bit.student_id "+
			 "where bit.balance > 0 " +
			" and bit.active = true " +
			" and bit.receiptStatus = 'P' " +
			"  AND (DATE(bit.created_Date) >= :minDate) " +
			"  AND (:start IS NULL OR DATE(bit.created_Date) >= :start) " +
			"  AND (:end IS NULL OR DATE(bit.created_Date) <= :end)  " +
			"  AND (:school_id IS NULL OR bit.school_id = :school_id) " +
			" AND (:bankId IS NULL OR bit.deposited_bank_id = :bankId)  ")
	public Page<Object> getAllSortedData(Pageable pageable,Integer school_id, Date start, Date end, Date minDate,Integer bankId);
	
	
	@Modifying
	@Query(value = "update BankImportTransaction bit set bit.active=false where bit.bank_import_transaction_id=?1")
	public void delete1(Integer id);
	
	@Modifying
	@Query(value = "update BankImportTransaction bit set bit.active=true where bit.bank_import_transaction_id=?1")
	public void delete2(Integer id);


	@Query(value = "select count(*) from BankImportTransaction bit where bit.deposited_bank_id=?1 and bit.cheque_dd_no=?2 and "
			+ "bit.transaction_date=?3 and bit.transaction_no=?4 and bit.amount=?5")
	public Integer checkValidate(Integer deposited_bank_id, String cheque_dd_no, String transaction_date,
			String transaction_no, Double amount);
	
	@Query(value = "select bit.bank_import_transaction_id as id,bit.transaction_date as transaction_date,"
			+ "bit.cheque_dd_no as cheque_dd_no,bit.deposited_bank_id as deposited_bank_id,bit.receipt_no as receipt_no,"
			+ "bit.student_id as student_id,bit.amount as amount,bit.start_row as start_row,bit.end_row as end_row,bit.voucher_head_new_id as voucher_head_new_id,"
			+ "bit.transaction_remarks as transaction_remarks,bit.fc_year_id as fc_year_id,bit.transaction_no as transaction_no,"
			+ "bit.dollor as dollor,bit.dollor_rate as dollor_rate,bit.school_id as school_id,bit.paid as paid,bit.balance as balance,"
			+ "bit.created_username as created_username,bit.modified_username as modified_username,bit.created_Date as created_Date,"
			+ "bit.modified_Date as modified_Date,bit.created_by as created_by,bit.modified_by as modified_by,bit.active as active,"
			+ "vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,bit.total_usd as total_usd,bit.exachange_rate as exachange_rate,"
			+ "s.school_name as school_name,s.school_name_short as school_name_short,"
			+ "bit.bank_usd_amt as bank_usd_amt,bit.bank_inr_amt as bank_inr_amt from bank_import_transaction bit "
			+ "left join schools s on s.school_id = bit.school_id "
			+ "left join voucher_head_new vh on vh.voucher_head_new_id = bit.voucher_head_new_id "
			+ "where bit.amount=?1 And (bit.balance>0  or bit.balance is null) And bit.active=true",nativeQuery=true)
	public List<Map<String, Object>> bankImportTransactionDetailsOnAmount(Double amount);
	
	@Query(value = "SELECT new map(b.bank_name as bank_name,b.bank_short_name as bank_short_name) from BankImportTransaction bit "
			+ "left join Bank b on b.bank_id = bit.deposited_bank_id "
			+ "where bit.deposited_bank_id=?1 And bit.active=true")
	public List<HashMap<String, Object>> bankImportTransaction(Integer deposited_bank_id);
	
	
	@Query(value = "select new map(bit.bank_import_transaction_id as id,bit.transaction_date as transaction_date, " +
			" bit.cheque_dd_no as bank_details,bit.deposited_bank_id as deposited_bank_id,bit.receipt_no as receipt_no, " +
			" bit.student_id as student_id,bit.amount as amount,bit.start_row as start_row,bit.end_row as end_row,bit.voucher_head_new_id as voucher_head_new_id, " +
			" bit.transaction_remarks as transaction_remarks,bit.fc_year_id as fc_year_id,bit.transaction_no as pay_id, " +
			" bit.dollor as dollor,bit.dollor_rate as dollor_rate,bit.school_id as school_id,bit.paid as paid,bit.balance as balance, " +
			" bit.created_username as created_username,bit.modified_username as modified_username,bit.created_Date as import_date, " +
			" bit.modified_Date as modified_Date,bit.created_by as created_by,bit.modified_by as modified_by,bit.active as active, " +
			" vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,bit.total_usd as total_usd,bit.exachange_rate as exachange_rate, " +
			" s.school_name as school_name,s.school_name_short as inst, " +
			" bit.bank_usd_amt as bank_usd_amt,bit.bank_inr_amt as bank_inr_amt, bit.transactionType as transaction_type, sd.auid as auid, sd.acharya_email as email, " +
			"sd.mobile as phone_no, bank.bank_name as bank ) from BankImportTransaction bit  " +
			" left join Schools s on s.school_id = bit.school_id  " +
			" left join VoucherHeadNew vh on vh.voucher_head_new_id = bit.voucher_head_new_id  " +
			" left join Bank bank on bank.bank_id = bit.deposited_bank_id " +
			" left join Student_Details sd on sd.student_id  = bit.student_id "+
			"where bit.balance = 0 " +
			" and bit.receiptStatus = 'S' " +
			" and bit.active = true " +
			"and CONCAT(IfNull(bit.created_username,''),'',IfNull(bit.cheque_dd_no,''),'',IfNull(bit.deposited_bank_id,''),''," +
			"  IfNull(bit.created_by,''),'',IfNull(bit.created_Date,'')) LIKE %:keyword%" +
			"  AND (DATE(bit.created_Date) >= :minDate) " +
			"  AND (:start IS NULL OR DATE(bit.created_Date) >= :start) " +
			"  AND (:end IS NULL OR DATE(bit.created_Date) <= :end)  " +
			"  AND (:school_id IS NULL OR bit.school_id = :school_id) ")
	public Page<Object> getAllImportTransactionDetailsForClearedHistoryFilteredByKeyword(Pageable pageable, Object keyword, Integer school_id, Date start, Date end, Date minDate);
	
	
	@Query(value = "select new map(bit.bank_import_transaction_id as id,bit.transaction_date as transaction_date, " +
			" bit.cheque_dd_no as bank_details,bit.deposited_bank_id as deposited_bank_id,bit.receipt_no as receipt_no, " +
			" bit.student_id as student_id,bit.amount as amount,bit.start_row as start_row,bit.end_row as end_row,bit.voucher_head_new_id as voucher_head_new_id, " +
			" bit.transaction_remarks as transaction_remarks,bit.fc_year_id as fc_year_id,bit.transaction_no as pay_id, " +
			" bit.dollor as dollor,bit.dollor_rate as dollor_rate,bit.school_id as school_id,bit.paid as paid,bit.balance as balance, " +
			" bit.created_username as created_username,bit.modified_username as modified_username,bit.created_Date as import_date, " +
			" bit.modified_Date as modified_Date,bit.created_by as created_by,bit.modified_by as modified_by,bit.active as active, " +
			" vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,bit.total_usd as total_usd,bit.exachange_rate as exachange_rate, " +
			" s.school_name as school_name,s.school_name_short as inst, " +
			" bit.bank_usd_amt as bank_usd_amt,bit.bank_inr_amt as bank_inr_amt, bit.transactionType as transaction_type, sd.auid as auid, sd.acharya_email as email, " +
			"sd.mobile as phone_no, bank.bank_name as bank ) from BankImportTransaction bit  " +
			" left join Schools s on s.school_id = bit.school_id  " +
			" left join VoucherHeadNew vh on vh.voucher_head_new_id = bit.voucher_head_new_id  " +
			" left join Bank bank on bank.bank_id = bit.deposited_bank_id " +
			" left join Student_Details sd on sd.student_id  = bit.student_id "+
			"where bit.balance = 0 " +
			" and bit.active = true " +
			" and bit.receiptStatus = 'S' " +
			"  AND (DATE(bit.created_Date) >= :minDate) " +
			"  AND (:start IS NULL OR DATE(bit.created_Date) >= :start) " +
			"  AND (:end IS NULL OR DATE(bit.created_Date) <= :end)  " +
			"  AND (:school_id IS NULL OR bit.school_id = :school_id) ")
	public Page<Object> getAllImportTransactionDetailsForClearedHistorySortedData(Pageable pageable, Integer school_id, Date start, Date end, Date minDate);
	
	@Modifying
	@Query(value = "DELETE From BankImportTransaction bit where bit.bank_import_transaction_id=?1")
	public void deleteBankImportTransaction(Integer bank_import_transaction_id);
	
	@Query(value = "select new map(bit.bank_import_transaction_id as id,bit.transaction_date as transaction_date,bit.total_usd as total_usd,bit.exachange_rate as exachange_rate,"
			+ "bit.cheque_dd_no as cheque_dd_no,bit.deposited_bank_id as deposited_bank_id,bit.receipt_no as receipt_no,b.bank_short_name as bank_short_name,"
			+ "bit.student_id as student_id,bit.amount as amount,bit.start_row as start_row,bit.end_row as end_row,b.bank_name as bank_name,"
			+ "bit.transaction_remarks as transaction_remarks,bit.fc_year_id as fc_year_id,bit.transaction_no as transaction_no,"
			+ "bit.dollor as dollor,bit.dollor_rate as dollor_rate,bit.school_id as school_id,bit.paid as paid,bit.balance as balance,"
			+ "bit.created_username as created_username,bit.modified_username as modified_username,bit.created_Date as created_Date,"
			+ "bit.modified_Date as modified_Date,bit.created_by as created_by,bit.modified_by as modified_by,bit.active as active,"
			+ "bit.bank_usd_amt as bank_usd_amt,bit.bank_inr_amt as bank_inr_amt) from BankImportTransaction bit "
			+ "left join Bank b on b.bank_id = bit.deposited_bank_id "
			+ "where CONCAT(IfNull(bit.created_username,''),'',IfNull(bit.cheque_dd_no,''),'',IfNull(bit.deposited_bank_id,''),'',"
			+ "IfNull(bit.created_by,''),'',IfNull(bit.created_Date,'')) LIKE %?1% and "
			+ "bit.active = false")
	public Page<Object> getAllInactiveDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	
	@Query(value = "select new map(bit.bank_import_transaction_id as id,bit.transaction_date as transaction_date,bit.total_usd as total_usd,bit.exachange_rate as exachange_rate,"
			+ "bit.cheque_dd_no as cheque_dd_no,bit.deposited_bank_id as deposited_bank_id,bit.receipt_no as receipt_no,b.bank_short_name as bank_short_name,"
			+ "bit.student_id as student_id,bit.amount as amount,bit.start_row as start_row,bit.end_row as end_row,b.bank_name as bank_name,"
			+ "bit.transaction_remarks as transaction_remarks,bit.fc_year_id as fc_year_id,bit.transaction_no as transaction_no,"
			+ "bit.dollor as dollor,bit.dollor_rate as dollor_rate,bit.school_id as school_id,bit.paid as paid,bit.balance as balance,"
			+ "bit.created_username as created_username,bit.modified_username as modified_username,bit.created_Date as created_Date,"
			+ "bit.modified_Date as modified_Date,bit.created_by as created_by,bit.modified_by as modified_by,bit.active as active,"
			+ "bit.bank_usd_amt as bank_usd_amt,bit.bank_inr_amt as bank_inr_amt) from BankImportTransaction bit "
			+ "left join Bank b on b.bank_id = bit.deposited_bank_id "
			+ "Where bit.active = false")
	public Page<Object> getAllInactiveSortedData(Pageable pageable);	
	
	
	@Query(value = "select new map(bit.bank_import_transaction_id as id,bit.transaction_date as transaction_date,"
			+ "bit.cheque_dd_no as cheque_dd_no,bit.deposited_bank_id as deposited_bank_id,bit.receipt_no as receipt_no,b.bank_short_name as bank_short_name,"
			+ "bit.student_id as student_id,bit.amount as amount,bit.start_row as start_row,bit.end_row as end_row,b.bank_name as bank_name,"
			+ "bit.transaction_remarks as transaction_remarks,bit.fc_year_id as fc_year_id,bit.transaction_no as transaction_no,"
			+ "bit.dollor as dollor,bit.dollor_rate as dollor_rate,bit.school_id as school_id,bit.paid as paid,bit.balance as balance,"
			+ "bit.created_username as created_username,bit.modified_username as modified_username,bit.created_Date as created_Date,"
			+ "bit.modified_Date as modified_Date,bit.created_by as created_by,bit.modified_by as modified_by,bit.active as active,"
			+ "vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,bit.total_usd as total_usd,bit.exachange_rate as exachange_rate,"
			+ "bit.bank_usd_amt as bank_usd_amt,bit.bank_inr_amt as bank_inr_amt) from BankImportTransaction bit "
			+ "left join Schools s on s.school_id = bit.school_id "
			+ "left join VoucherHeadNew vh on vh.voucher_head_new_id = bit.voucher_head_new_id "
			+ "left join Bank b on b.bank_id = bit.deposited_bank_id Where bit.bank_import_transaction_id=?1")
	public List<HashMap<String, Object>> bankImportTransactionWithVoucherName(Integer bank_import_transaction_id);


	@Query(value = "select bit.balance from BankImportTransaction bit Where bit.receipt_no=?1 And bit.fc_year_id=?2 And bit.school_id=?3 And bit.active=true ")
	public Double getBalanceAmount(String fee_receipt, Integer financial_year_id, Integer schoolId);


	@Modifying
	@Transactional
	@Query(value = "Update BankImportTransaction bfr Set bfr.balance=?1,bfr.receiptStatus = 'P' Where bfr.receipt_no=?2 And bfr.fc_year_id=?3 And bfr.school_id=?4 ")
	public void updateBalanceAmountInBankImportTransaction(Double balance,String fee_receipt, Integer fcYearId, Integer schoolId);


	@Query(value = "select new map(bit.bank_import_transaction_id as bank_import_transaction_id,bit.receipt_no as receipt_no) from BankImportTransaction bit Where bit.receipt_no is not null")
	public List<HashMap<String,Object>> getReceiptNoAndBankImportTransactionId();
	
	@Query(value = "select bit from BankImportTransaction bit Where bit.order_id=:orderId AND bit.active = true ")
	public BankImportTransaction getByOrderId(String orderId);

	@Query(value = "select rft.transaction_date as transacationDate," +
			"rft.order_id as orderId," +
			"rft.payment_id as paymentID," +
			"rppd.amount as transactionAmount," +
			"rppd.payment_type as paymentType," +
			"b.auid as auid," +
			"b.settlement_id as settlementId," +
			"b.settlement_utr as settlementUtr," +
			"b.created_date as settlementDate," +
			"rpsk.merchant_id as merchantId" +
			" from razor_pay_transaction rft " +
			" left join razor_pay_payment_details rppd" +
			" on rppd.razor_pay_transaction_id = rft.razor_pay_transaction_id" +
			" left join bank_import_transaction b" +
			" on b.order_id = rft.order_id" +
			" left join razor_pay_secret_keys rpsk" +
			" on rpsk.school_id = b.school_id" +
			" where rft.order_id = ?1",nativeQuery = true)
    List<Map<String, Object>> getpaymentInformationByOrderId(String orderId);

	@Query(value = "select * from bank_import_transaction  b " +
			"join razor_pay_transaction rpt " +
			"on rpt.order_id = b.order_id " +
			"where rpt.transaction_type = ?1 " +
			"and b.active = 1 " +
			"and b.receipt_status = 'P' ",nativeQuery = true)
	List<BankImportTransaction> getAllPendingBankImportTransactionByReceiptType(String receiptType);

	@Query(value = "select * from bank_import_transaction b where b.active = 1 and b.receipt_status = 'P' and b.balance > 0",nativeQuery = true)
	List<BankImportTransaction> findAllPendingBankImportTransactions();

	@Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
			"FROM BankImportTransaction b WHERE b.order_id = :orderId AND b.active = true ")
	boolean existsBankImportTransactionByOrderId(@Param("orderId") String orderId);

	@Query(value = "SELECT bit from BankImportTransaction bit where bit.bank_import_transaction_id=?1")
	public BankImportTransaction getBankImportTransaction(Integer bank_import_transaction_id);

	@Transactional
	@Modifying
	@Query("UPDATE BankImportTransaction bfr SET bfr.receiptStatus='P' WHERE bfr.receiptStatus='I' AND bfr.active = true ")
	void updateBankImportInprogressToPending();

	@Query(value = "select * from bank_import_transaction where settlement_id = ?1 and receipt_status = 'P' and balance > 0 ", nativeQuery = true)
    public List<BankImportTransaction> getPendingBankImportTransactionBySettlementId(String settlementId);

	@Query(value = "select * from bank_import_transaction where settlement_id = ?1 and receipt_status = 'S' and balance <= 0 ", nativeQuery = true)
	public List<BankImportTransaction> getReceiptGeneratedBankImportTransactionBySettlementId(String settlementId);
}
