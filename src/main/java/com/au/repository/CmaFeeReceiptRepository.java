package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CmaFeeReceipt;
import com.au.model.PettyCash;

@Transactional
@Repository
public interface CmaFeeReceiptRepository extends JpaRepository<CmaFeeReceipt, Integer>{

	@Query(value = "SELECT cfr from CmaFeeReceipt cfr where cfr.active=true")
	public List<CmaFeeReceipt> getAllCmaFeeReceipt();

	@Modifying
	@Query(value = "update CmaFeeReceipt cfr set cfr.active=false where cfr.cma_fee_receipt_id=?1")
	public void deactivateCmaFeeReceipt(Integer id);

	@Modifying
	@Query(value = "update CmaFeeReceipt cfr set cfr.active=true where cfr.cma_fee_receipt_id=?1")
	public void activateCmaFeeReceipt(Integer id);
	
	
	@Query(value = "select new map(cfr.cma_fee_receipt_id AS id,"
			+ "    cfr.cma_receipt_id AS cma_receipt_id,"
			+ "    cfr.transacation_type AS transacation_type,"
			+ "    cfr.amount AS amount,"
			+ "    cfr.total_amount AS total_amount,"
			+ "    cfr.student_id AS student_id,"
			+ "    cfr.school_id AS school_id,"
			+ "    cfr.financial_year_id AS financial_year_id,"
			+ "    cfr.paid_year AS paid_year,"
			+ "    cfr.bank_import_transaction_id AS bank_import_transaction_id,"
			+ "    cfr.remarks AS remarks,"
			+ "    cfr.waiver_status AS waiver_status,"
			+ "    cfr.receipt_type AS receipt_type,"
			+ "    cfr.created_by AS created_by,"
			+ "    cfr.modified_by AS modified_by,"
			+ "    cfr.created_date AS created_date,"
			+ "    cfr.modified_date AS modified_date,"
			+ "    cfr.active AS active,"
			+ "	   sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "		sd.student_name AS student_name,sd.auid As auid,sd.acharya_email As acharya_email,"
			+ "		fy. financial_year As financial_year,bit.transaction_date As transaction_date,bit.transaction_no As transaction_no,"
			+ "    cfr.created_username AS created_username,"
			+ "    cfr.modified_username AS modified_username ) "
			+ "   from CmaFeeReceipt cfr "
			+ "	 left join Schools sc on sc.school_id=cfr.school_id "
			+ "	 left join Student_Details sd on sd.student_id=cfr.student_id "
			+ "	 left join FinancialYear fy on fy.financial_year_id=cfr.financial_year_id "
			+ "	 left join BankImportTransaction bit on bit.bank_import_transaction_id=cfr.bank_import_transaction_id "
			  + "where CONCAT(IfNull(cfr.transacation_type,''),'',IfNull(fy.financial_year,''),'',IfNull(sc.school_name_short,''),'',"
		        + "IfNull(sd.student_name,''),'',IfNull(cfr.receipt_type,''),'',IfNull(cfr.cma_receipt_id,''),'',IfNull(sd.auid,'')) "
		        + "LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(cfr.cma_fee_receipt_id AS id,"
			+ "    cfr.cma_receipt_id AS cma_receipt_id,"
			+ "    cfr.transacation_type AS transacation_type,"
			+ "    cfr.amount AS amount,"
			+ "    cfr.total_amount AS total_amount,"
			+ "    cfr.student_id AS student_id,"
			+ "    cfr.school_id AS school_id,"
			+ "    cfr.financial_year_id AS financial_year_id,"
			+ "    cfr.paid_year AS paid_year,"
			+ "    cfr.bank_import_transaction_id AS bank_import_transaction_id,"
			+ "    cfr.remarks AS remarks,"
			+ "    cfr.waiver_status AS waiver_status,"
			+ "    cfr.receipt_type AS receipt_type,"
			+ "    cfr.created_by AS created_by,"
			+ "    cfr.modified_by AS modified_by,"
			+ "    cfr.created_date AS created_date,"
			+ "    cfr.modified_date AS modified_date,"
			+ "    cfr.active AS active,"
			+ "	   sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "		sd.student_name AS student_name,sd.auid As auid,sd.acharya_email As acharya_email,"
			+ "		fy. financial_year As financial_year,bit.transaction_date As transaction_date,bit.transaction_no As transaction_no,"
			+ "    cfr.created_username AS created_username,"
			+ "    cfr.modified_username AS modified_username ) "
			+ "   from CmaFeeReceipt cfr "
			+ "	 left join Schools sc on sc.school_id=cfr.school_id "
			+ "	 left join Student_Details sd on sd.student_id=cfr.student_id "
			+ "	 left join FinancialYear fy on fy.financial_year_id=cfr.financial_year_id "
			+ " left join BankImportTransaction bit on bit.bank_import_transaction_id=cfr.bank_import_transaction_id ")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "SELECT cfr.cma_receipt_id From cma_fee_receipt cfr where cfr.school_id=?1 And cfr.financial_year_id=?2 And cfr.receipt_type=?3 "
			+ " ORDER BY cfr.cma_fee_receipt_id DESC LIMIT 1",nativeQuery=true)
	public Integer getLatestData(Integer school_id, Integer financial_year_id, String receipt_type);

	@Query(value = "select * From cma_fee_receipt where school_id=?1 And financial_year_id=?2 And receipt_type=?3 "
			+ " ORDER BY cma_fee_receipt_id Desc LIMIT 1",nativeQuery = true)
	public CmaFeeReceipt getLatestServiceTicketId(Integer school_id, Integer financial_year_id, String receipt_type);

	@Query(value=" select c from CmaFeeReceipt c where c.bank_import_transaction_id=:bankImportTransactionId ")
	public List<CmaFeeReceipt> getCmaFeeReceiptsByBankImportId(Integer bankImportTransactionId);

	@Query(value="select cma_fee_receipt_id from cma_fee_receipt where financial_year_id=?1 order by cma_receipt_id desc limit 1",nativeQuery=true)
	public Integer getLatestReceitpNumber(Integer fcYear);

	@Query(value="select sum(cma.amount) from cma_fee_receipt cma where cma.paid_year=?1 and  cma.student_id=?2 and cma.receipt_type='Add On Fee' and cma.active=true",nativeQuery=true)
	public Double getCmaFeeReceiptForAddOnFeePaidDetails(int paid_year, Integer student_id);

	@Query(value = "select new map(cfr.cma_fee_receipt_id AS cma_fee_receipt_id,cfr.cma_receipt_id AS cma_receipt_id,cfr.total_amount AS total_amount, cfr.created_date AS created_date,"
			+ "sc.school_name As school_name,sc.school_name_short As school_name_short,sd.student_name AS student_name,sd.auid As auid,sd.acharya_email As acharya_email,fy.financial_year As financial_year,"
			+ "cfr.receipt_type AS receipt_type,ps.program_specialization_name As program_specialization_name,ps.program_specialization_short_name As program_specialization_short_name,"
			+ "p.program_name As program_name,p.program_short_name As program_short_name) "
			+ "from CmaFeeReceipt cfr "
			+ "left join Schools sc on sc.school_id=cfr.school_id "
			+ "left join Student_Details sd on sd.student_id=cfr.student_id "
			+ "left join FinancialYear fy on fy.financial_year_id=cfr.financial_year_id "
			+ "LEFT JOIN ProgramSpecilization ps on ps.program_specialization_id=sd.program_specialization_id "
			+ "LEFT JOIN Program p on p.program_id=sd.program_id "
			+ "where cfr.cma_receipt_id=?1 and cfr.financial_year_id=?2 and cfr.student_id=?3 and cfr.active=true")
	public List<HashMap<String, Object>> getCmaFeeReceiptByReceiptId(Integer cma_receipt_id, Integer financial_year_id, Integer student_id);
	
	@Query(value = "select new map(cfr.uniformReceiptId AS uniform_receipt_id,"
			+ "    cfr.fcYearId AS fc_year_id,"
			+ "    cfr.studentId AS student_id,"
			+ "    cfr.amount as amount,"
			+ "    cfr.issuedStatus AS issued_status,"
			+ "    cfr.cancelStatus AS cancel_status,"
			+ "    cfr.orderId AS order_id,"
			+ "    cfr.type as type, "
			+ "    cfr.transactionType AS transaction_type,"
			+ "    cfr.otherFeeTemplateId AS other_fee_template_id, "
			+ "    cfr.uniformReceiptNo AS uniform_receipt_no, "
			+ "    cfr.paydetails as paydetails, "
			+ "    cfr.transactionDate AS transaction_date, "
			+ "    cfr.quantity as quantity, "
			+ "    cfr.cgst_input as cgst_input, "
			+ "    cfr.cgst_output as cgst_output, "
			+ "    cfr.sgst_input as sgst_input, "
			+ "    cfr.sgst_output as sgst_output, "
			+ "    cfr.gst as gst, "
			+ "    cfr.env_item_id as env_item_id, "
			+ "    cfr.year as year, "
			+ "    cfr.sem as sem, "
			+ "    cfr.total_amount as total_amount, "
			+ "    cfr.schoolId AS school_id, "
			+ "    cfr.bankImportTransactionId AS bank_import_transaction_id, cfr.createdDate AS created_date,"
			+ "sc.school_name As school_name,sc.school_name_short As school_name_short,sd.student_name AS student_name,sd.auid As auid,sd.acharya_email As acharya_email,fy.financial_year As financial_year,"
			+ "ps.program_specialization_name As program_specialization_name,ps.program_specialization_short_name As program_specialization_short_name,"
			+ "p.program_name As program_name,p.program_short_name As program_short_name) "
			+ "from UniformReceipt cfr "
			+ "left join Schools sc on sc.school_id=cfr.schoolId "
			+ "left join Student_Details sd on sd.student_id=cfr.studentId "
			+ "left join FinancialYear fy on fy.financial_year_id=cfr.fcYearId "
			+ "LEFT JOIN ProgramSpecilization ps on ps.program_specialization_id=sd.program_specialization_id "
			+ "LEFT JOIN Program p on p.program_id=sd.program_id "
			+ "where cfr.uniformReceiptNo=?1 and cfr.fcYearId=?2 and cfr.studentId=?3 and cfr.active=true and cfr.type='package'")
	public List<HashMap<String, Object>> getUniformFeeReceiptByReceiptId(Integer uniform_receipt_no, Long fc_year_id, Long studentId);

	@Query(value = "select * from cma_fee_receipt where order_id = ?1 and active = true ",nativeQuery = true)
	List<CmaFeeReceipt> getCmaFeeReceiptByOrderId(String orderId);
}

