package com.au.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.dto.HostelFeeReceiptDto;
import com.au.model.HostelFeeReceiptVoucherHeadWise;

@Repository
@Transactional
public interface HostelFeeReceiptVoucherHeadWiseRepository extends JpaRepository<HostelFeeReceiptVoucherHeadWise, Integer>{

	@Query(value = "SELECT new com.au.dto.HostelFeeReceiptDto(hfrcv.createdUsername, " +
			"hfrcv.createdDate, " +
			"hfrcv.createdBy, " +
			"fr.fee_receipt_id, " +
			"hfrcv.active, " +
			"sd.student_name, " +
			"sd.auid, " +
			"sd.usn, " +
			"ac.ac_year, " +
			"sch.school_id, " +
			"sch.school_name, " +
			"sch.school_name_short, " +
			"fy.financial_year, " +
			"sd.acharya_email, " +
			"bit.transaction_no, " +
			"bit.bank_import_transaction_id, " +
			"bit.cheque_dd_no, " +
			"bit.transaction_date, " +
			"fr.transaction_type, " +
			"fr.transactionMode, " +
			"fr.hostel_status, " +
			"vhn.voucher_head, "+
			"vhn.voucher_head_new_id, "+
			"hfrcv.totalAmount, "+
			"hfrcv.payingAmount, "+
			"hfrcv.balanceAmount, " +
			"fr.remarks, " +
			"hfrcv.receivedFrom, " +
			"hfrcv.cashier ) " +
			"FROM HostelFeeReceiptVoucherHeadWise hfrcv "+
			"Left Join VoucherHeadNew vhn On vhn.voucher_head_new_id=hfrcv.voucherHead " +
			"Left Join FeeReceipt fr On fr.fee_receipt_id=hfrcv.feeReceipt " +
			"Left Join Student_Details sd On sd.student_id=hfrcv.student " +
			"Left Join Academic_year ac On ac.ac_year_id=hfrcv.acYear " +
			"Left Join FinancialYear fy On fy.financial_year_id=fr.financial_year_id "+
			"Left join BankImportTransaction bit on fr.bank_transaction_history_id=bit.bank_import_transaction_id "+
			"Left Join Schools sch On sch.school_id=hfrcv.school "+
			"WHERE hfrcv.feeReceipt.fee_receipt_id = :feeReceiptId",nativeQuery = false)
	List<HostelFeeReceiptDto> hostelFeeReceiptDetailsByFeeReceiptId(Integer feeReceiptId);

	@Modifying
	@Query(value = "Update hostel_fee_receipt_voucher_head_wise hfrv Set hfrv.active=false Where hfrv.fee_receipt_id=?1 And hfrv.student_id=?2",nativeQuery = true)
	public void deactive(Integer fee_receipt_id, Integer student_id);

	
	 @Query(value="select hw.paying_amount As paying_amount,hw.balance_amount As balance_amount,hw.total_amount As total_amount,"
	 		+ "hw.hostel_fee_receipt_voucher_head_wise_id As hostel_fee_receipt_voucher_head_wise_id,"
	 		+ "vhn.voucher_head As voucher_head,vhn.voucher_head_short_name As voucher_head_short_name,vhn.voucher_head_new_id As voucher_head_new_id "
	 		+ "from hostel_fee_receipt_voucher_head_wise hw "
	 		+ "left join voucher_head_new vhn on vhn.voucher_head_new_id = hw.voucher_head_new_id "
		 	+ "where hw.fee_receipt_id =?1 And hw.student_id =?2 And hw.active=true ", nativeQuery = true)
	List<Map<String, Object>> getHeadwiseData(Integer feeReceiptId, Integer studentId);

    @Query(value = "select * from hostel_fee_receipt_voucher_head_wise where fee_receipt_id = ?1 and student_id = ?2 and ac_year_id = ?3 and active = true ", nativeQuery = true)
    List<HostelFeeReceiptVoucherHeadWise> getVoucherHeadBYFeeReceipt(Integer feeReceiptId, Integer studentId, Integer acYearId);
}
