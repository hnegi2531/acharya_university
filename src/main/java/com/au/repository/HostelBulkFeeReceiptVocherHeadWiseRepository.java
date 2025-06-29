package com.au.repository;

import javax.transaction.Transactional;

import com.au.dto.HostelFeeReceiptDto;
import com.au.model.HostelFeeReceiptVoucherHeadWise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.HostelBulkFeeReceiptVocherHeadWise;

import java.util.List;

@Transactional
@Repository
public interface HostelBulkFeeReceiptVocherHeadWiseRepository extends JpaRepository<HostelBulkFeeReceiptVocherHeadWise, Integer> {

    @Query(value = " select * from hostel_bulk_fee_receipt_vocher_head_wise where fee_receipt_id = ?1 and student_id = ?2 and ac_year_id = ?3 ",nativeQuery = true)
   public List<HostelFeeReceiptVoucherHeadWise> getVoucherHeadByHostelBulkFeeReceipt(Integer feeReceiptId, Integer studentId, Integer acYearId);


//	@Query("SELECT new com.au.dto.HostelFeeReceiptDto(hfrcv.createdUsername, " +
//		       "hfrcv.createdDate, " +
//		       "hfrcv.createdBy, " +
//		       "hfrcv.feeReceipt.fee_receipt_id, " +
//		       "hfrcv.active, " +
//		       "hfrcv.student.student_name, " +
//		       "hfrcv.student.auid, " +
//		       "hfrcv.student.usn, " +
//		       "hfrcv.acYear.ac_year, " +
//		       "hfrcv.school.school_id, " +
//		       "hfrcv.school.school_name, " +
//		       "hfrcv.school.school_name_short, " +
//		       "fy.financial_year, " +
//		       "hfrcv.student.acharya_email, " +
//		       "bit.transaction_no, " +
//		       "bit.bank_import_transaction_id, " +
//		       "bit.cheque_dd_no, " +
//		       "bit.transaction_date, " +
//		       "hfrcv.feeReceipt.transaction_type, " +
//		       "hfrcv.feeReceipt.transactionMode, " +
//		       "hfrcv.feeReceipt.hostel_status, " +
//		       "vhn.voucher_head, "+
//		       "vhn.voucher_head_new_id, "+
//		       "hfrcv.totalAmount, "+
//		       "hfrcv.payingAmount, "+
//		       "hfrcv.balanceAmount, " +
//		       "fr.remarks) " +
//		       "FROM HostelFeeReceiptVoucherHeadWise hfrcv "+
//		       "Left Join FinancialYear fy On fy.financial_year_id=hfrcv.feeReceipt.financial_year_id "+
//		       "Left join BankImportTransaction bit on hfrcv.feeReceipt.bank_transaction_history_id=bit.bank_import_transaction_id "+
//		       "Left Join hfrcv.voucherHead vhn " +
//		       "Left Join hfrcv.feeReceipt fr " +
//			   "LEFT JOIN hfrcv.student student " +
//			   "LEFT JOIN hfrcv.acYear acYear " +
//			   "LEFT JOIN hfrcv.school school " +
//		       "WHERE hfrcv.feeReceipt.fee_receipt_id = :feeReceiptId")
//		List<HostelFeeReceiptDto> hostelFeeReceiptDetailsByFeeReceiptId(Integer feeReceiptId);


}
