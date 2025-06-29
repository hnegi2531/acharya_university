package com.au.repository;

import com.au.model.RTGSFeeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public interface RTGSFeeHistoryRepository extends JpaRepository<RTGSFeeHistory , Integer>{

	
	
	@Query(value="Select rtgsfh From RTGSFeeHistory rtgsfh Where rtgsfh.active=true")
	public List<RTGSFeeHistory > getActiveRTGSFeeHistory();
	
	@Query(value ="Select new map(rtgsfh.rtgs_fee_history_id as id,rtgsfh.student_id as student_id,rtgsfh.school_id as school_id,"
	 		+ "rtgsfh.remarks as remarks,rtgsfh.receipt_no as receipt_no,rtgsfh.fc_year_id as fc_year_id,"
	 		+ "rtgsfh.paid as paid,rtgsfh.rtgs_net_amount as rtgs_net_amount,rtgsfh.bank_transaction_history_id as bank_transaction_history_id,"
	 		+ "rtgsfh.rtgs_balance_amount as rtgs_balance_amount,rtgsfh.receipt_type as receipt_type,rtgsfh.created_username as created_username,"
	 		+ "rtgsfh.modified_username as modified_username,rtgsfh.created_date as created_date,rtgsfh.modified_date as modified_date,rtgsfh.created_by as created_by,"
	 		+ "rtgsfh.modified_by as modified_by,rtgsfh.active as active,sd.student_name as student_name,sc.school_name_short as school_name_short,"
	 		+ "fy.financial_year as financial_year) From RTGSFeeHistory rtgsfh "
	 		+ "left join Student_Details sd on rtgsfh.student_id = sd.student_id "
	 		+ "left join Schools sc on rtgsfh.school_id=sc.school_id "
	 		+ "left join FinancialYear fy on rtgsfh.fc_year_id=fy.financial_year_id "
	 		+ "Where CONCAT(IfNull(rtgsfh.rtgs_fee_history_id,''),'',IfNull(rtgsfh.receipt_no,''),'',"
	 		+ "IfNull(rtgsfh.created_username,''),'',IfNull(rtgsfh.created_date,''),'',"
	 		+ "IfNull(rtgsfh.created_by,''),'',IfNull(sd.student_name,''),'',IfNull(sc.school_name_short,''),'',"
	 		+ "IfNull(fy.financial_year,'')) LIKE %?1%")
	 public Page<Object> getAllRTGSFeeHistory1(Pageable pageable, Object keyword);
	 
	@Query(value ="Select new map(rtgsfh.rtgs_fee_history_id as id,rtgsfh.student_id as student_id,rtgsfh.school_id as school_id,"
	 		+ "rtgsfh.remarks as remarks,rtgsfh.receipt_no as receipt_no,rtgsfh.fc_year_id as fc_year_id,"
	 		+ "rtgsfh.paid as paid,rtgsfh.rtgs_net_amount as rtgs_net_amount,rtgsfh.bank_transaction_history_id as bank_transaction_history_id,"
	 		+ "rtgsfh.rtgs_balance_amount as rtgs_balance_amount,rtgsfh.receipt_type as receipt_type,rtgsfh.created_username as created_username,"
	 		+ "rtgsfh.modified_username as modified_username,rtgsfh.created_date as created_date,rtgsfh.modified_date as modified_date,rtgsfh.created_by as created_by,"
	 		+ "rtgsfh.modified_by as modified_by,rtgsfh.active as active,sd.student_name as student_name,sc.school_name_short as school_name_short,"
	 		+ "fy.financial_year as financial_year) From RTGSFeeHistory rtgsfh "
	 		+ "left join Student_Details sd on rtgsfh.student_id = sd.student_id "
	 		+ "left join Schools sc on rtgsfh.school_id=sc.school_id "
	 		+ "left join FinancialYear fy on rtgsfh.fc_year_id=fy.financial_year_id ")
	 public Page<Object> getAllRTGSFeeHistory2(Pageable pageable);
	
	@Modifying
	@Query(value = "Update RTGSFeeHistory rtgsfh Set rtgsfh.active=false Where rtgsfh.rtgs_fee_history_id=?1")
	public void delete1(Integer rtgs_fee_history_id);
	
	@Modifying
	@Query(value = "Update RTGSFeeHistory rtgsfh Set rtgsfh.active=true Where rtgsfh.rtgs_fee_history_id=?1")
	public void activateRTGSFeeHistory(Integer fee_receipt_id);
	
	@Query(value="Select new map(rtgsfh.rtgs_fee_history_id as id,rtgsfh.student_id as student_id,rtgsfh.school_id as school_id,"
			+ "rtgsfh.remarks as remarks,rtgsfh.receipt_no as receipt_no,rtgsfh.fc_year_id as fc_year_id,"
			+ "rtgsfh.paid as paid,rtgsfh.rtgs_net_amount as rtgs_net_amount,rtgsfh.bank_transaction_history_id as bank_transaction_history_id,"
			+ "rtgsfh.rtgs_balance_amount as rtgs_balance_amount,rtgsfh.receipt_type as receipt_type,rtgsfh.created_username as created_username,"
			+ "rtgsfh.modified_username as modified_username,rtgsfh.created_date as created_date,rtgsfh.modified_date as modified_date,rtgsfh.created_by as created_by,"
			+ "rtgsfh.modified_by as modified_by,rtgsfh.active as active,sd.student_name as student_name,sc.school_name_short as school_name_short,"
			+ "fy.financial_year as financial_year,bit.transaction_date as transaction_date,sd.auid as auid,sd.usn as usn) From RTGSFeeHistory rtgsfh "
			+ "left join Student_Details sd on rtgsfh.student_id = sd.student_id "
			+ "left join Schools sc on rtgsfh.school_id=sc.school_id "
			+ "left join FinancialYear fy on rtgsfh.fc_year_id=fy.financial_year_id "
			+ "left join BankImportTransaction bit on bit.bank_import_transaction_id=rtgsfh.bank_transaction_history_id "
			+ "Where rtgsfh.bank_transaction_history_id=?1 And rtgsfh.active=true")
	public List<HashMap<String,Object>> allRTGSFeeHistoryDetails(Integer bank_transaction_history_id);

	@Modifying
	@Transactional
	@Query(value = "Update RTGSFeeHistory rtgs Set rtgs.active=false Where rtgs.receipt_no=?1 and rtgs.fc_year_id=?2 and rtgs.school_id=?3 ")
	public void updateRTGSFeeHistory(String fee_receipt,Integer fcYearId, Integer schoolId);

	@Query(value="select r from RTGSFeeHistory r where r.bank_transaction_history_id=:bankImportId and r.receipt_type=:receiptType  and r.active = true ")
	public List<RTGSFeeHistory> getRtgsFeeHistories(Integer bankImportId, String receiptType);

	@Modifying
	@Query(value = "Update RTGSFeeHistory rfh Set rfh.active=false Where rfh.bank_transaction_history_id=?1 And rfh.receipt_no=?2")
	public void deactive(Integer bank_transaction_history_id, String fee_receipt);

	@Query(value="Select rtgs.receipt_no as receipt_no,rtgs.rtgs_net_amount as rtgs_net_amount,rtgs.rtgs_balance_amount as rtgs_balance_amount," +
			"rtgs.paid as paid,rtgs.rtgs_fee_history_id as rtgs_fee_history_id From rtgs_fee_history rtgs " +
//			"Left join fee_receipt fr on fr.fee_receip=rtgs.receipt_no And fr." +
			"Where rtgs.fc_year_id=?1 And rtgs.receipt_no=?2 And rtgs.receipt_type ='Bulk' And rtgs.active=true",nativeQuery = true)
	List<Map<String, Object>> rtgsAmountForPaidAtBoardTag(Integer fcYearId, String receiptNo);

	@Modifying
	@Query(value = "Update RTGSFeeHistory rtgs Set rtgs.paid=?1,rtgs.rtgs_balance_amount=?2 Where rtgs.rtgs_fee_history_id=?3")
	void updatePaidAndBalanceAmount(Double paidAmount, Double rtgsBalanceAmount, Integer rtgsFeeHistoryId);


}
