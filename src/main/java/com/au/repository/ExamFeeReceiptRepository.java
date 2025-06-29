package com.au.repository;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.model.ExamFeeReceipt;

@Repository
@Transactional
public interface ExamFeeReceiptRepository extends JpaRepository<ExamFeeReceipt,Integer>{

	@Query(value="Select new map(efr.examFeeReceiptId as examFeeReceiptId,efr.paidYear as paidYear,efr.remarks as remarks,efr.receivedIn as receivedIn,"
			+ "efr.createdDate as createdDate,efr.createdUsername as createdUsername,std.student_name as studentName,fy.financial_year as financialYear,"
			+ "ay.ac_year as acYear,vhn.voucher_head as voucherHead,sch.school_name as schoolName,fr.paid_amount as paid_amount) From ExamFeeReceipt efr "
			+ "Left Join efr.student std "
			+ "Left Join efr.financialYear fy "
			+ "Left Join efr.acYear ay "
			+ "Left Join efr.voucherHeadNew vhn "
			+ "Left Join efr.school sch "
			+ "Left Join efr.feeReceipt fr ")
	List<HashMap<String, Object>> getExamFeeReceipt();
	
	
	@Query(value="Select new map(efr.examFeeReceiptId as examFeeReceiptId,efr.paidYear as paidYear,efr.remarks as remarks,efr.receivedIn as receivedIn,efr.amount As amount,"
			+ "efr.createdDate as createdDate,efr.createdUsername as createdUsername,std.student_name as studentName,fy.financial_year as financialYear,"
			+ "ay.ac_year as acYear,vhn.voucher_head as voucherHead,vhn.voucher_head_new_id as voucherHeadNewId) From ExamFeeReceipt efr "
			+ "Left Join efr.student std "
			+ "Left Join efr.financialYear fy "
			+ "Left Join efr.acYear ay "
			+ "Left Join efr.voucherHeadNew vhn "
			+ "Left Join efr.school sch "
			+ "Left Join efr.feeReceipt fr Where fr.fee_receipt_id=?1 And fr.active=true")
	List<HashMap<String, Object>> examFeeReceiptByFeeReceiptId(Integer feeReceiptId);

	@Query("SELECT e FROM ExamFeeReceipt e WHERE e.feeReceipt.fee_receipt_id = :feeReceiptId and e.active = true ")
	List<ExamFeeReceipt> findByFeeReceiptId(@Param("feeReceiptId") Integer feeReceiptId);

	@Modifying
	@Transactional
	@Query("UPDATE ExamFeeReceipt e SET e.active = false WHERE e.feeReceipt.fee_receipt_id=:feeReceiptId ")
    public void deactivateExamReceiptByFeeReceiptId(Integer feeReceiptId);
}
