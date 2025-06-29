package com.au.repository;

import com.au.model.FeeReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public interface FeeReceiptRepository extends JpaRepository<FeeReceipt , Integer> {
	
	@Query(value="Select frc From FeeReceipt frc Where frc.active=true")
	public List<FeeReceipt> getActiveFeeReceipt();
	
	@Modifying
	@Query(value = "Update FeeReceipt frc Set frc.active=false Where frc.fee_receipt_id=?1")
	public void delete1(Integer fee_receipt_id);
	
	@Modifying
	@Query(value = "Update FeeReceipt frc Set frc.active=true Where frc.fee_receipt_id=?1")
	public void activateFeeReceipt(Integer fee_receipt_id);
	
	@Query(value = "Select ifNull(Max(fee_receipt),0) From fee_receipt"
			+ " where school_id=?1 And financial_year_id=?2 And hostel_status=?3",nativeQuery=true)
	public Integer getMaxId(Integer school_id, Integer financial_year_id, Integer hostel_status);
	
	/* @Query(value = "Select new map(frc.fee_receipt_id as fee_receipt_id, frc.fee_receipt as fee_receipt, frc.student_id as frcstudent_id, frc.receipt_type as receipt_type,"
			+ " frc.created_date as created_date, sph.student_fee_payment_history_id as student_fee_payment_history_id,"
			+ " sph.balance_amount as balance_amount, sph.fee_template_id as fee_template_id, sph.paid_amount as paid_amount,"
			+ " sph.paid_year as paid_year, sph.to_pay as to_pay, sph.total_amount as total_amount,"
			+ " sph.voucher_head_new_id as voucher_head_new_id, std.ac_year_id as ac_year_id, std.auid as auid,"
			+ " std.candidate_sex as candidate_sex, std.current_country as current_country, std.current_state as current_state,"
			+ " std.date_of_admission as date_of_admission, std.dateofbirth as dateofbirth, std.fee_admission_category_id as fee_admission_category_id,"
			+ " std.fee_template_id as stdfee_template_id, std.firstname as firstname, std.joining_year as joining_year,"
			+ " std.lastname as lastname, std.mobile as mobile, std.nationality as nationality, std.program_id as program_id,"
			+ " std.program_specialization_id as program_specialization_id, std.school_id as stdschool_id, std.acharya_email as acharya_email,"
			+ " std.student_name as student_name,std.usn as usn, std.fee_admission_category_id as fee_admission_category_id, vhn.voucher_head as voucher_head,pr.program_name as program_name,"
			+ " pr.program_short_name as program_short_name, ps.program_specialization_name as program_specialization_name,"
			+ " ps.program_specialization_short_name as program_specialization_short_name, fac.fee_admission_category_type as fee_admission_category_type,"
			+ " ft.fee_template_name as fee_template_name, ft.program_type_id as program_type_id, fts.year10_amt as year10_amt,"
			+ " fts.year11_amt as year11_amt, fts.year12_amt as year12_amt, fts.year1_amt as year1_amt, fts.year2_amt as year2_amt,"
			+ " fts.year3_amt as year3_amt, fts.year4_amt as year4_amt, fts.year5_amt as year5_amt, fts.year6_amt as year6_amt,"
			+ " fts.year7_amt as year7_amt, fts.year8_amt as year8_amt, fts.year9_amt as year9_amt )"
			+ " From FeeReceipt frc Inner Join StudentPaymentHistory sph On frc.fee_receipt = sph.fee_receipt "
			+ " Inner Join Student_Details std On frc.student_id = std.student_id "
			+ " Inner Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ " Inner Join FeeTemplateSubAmount fts On std.fee_template_id = fts.fee_template_id "
			+ " Left Join VoucherHeadNew vhn On sph.voucher_head_new_id = vhn.voucher_head_new_id "
			+ " Left Join Program pr On std.program_id = pr.program_id "
			+ " Left Join ProgramSpecilization ps On std.program_specialization_id = ps.program_specialization_id "
			+ " Left Join FeeAdmissionCategory fac On std.fee_admission_category_id = fac.fee_admission_category_id  Where frc.student_id=?1 And frc.active=true")
	public List<HashMap<String, Object>> getAllDataOfFeeReceiptForFormating(Integer student_id);*/
	
	
	 @Query(value = "Select new map(frc.fee_receipt_id as fee_receipt_id, frc.fee_receipt as fee_receipt, frc.student_id as frcstudent_id, frc.receipt_type as receipt_type,"
				+ " frc.created_date as created_date, sph.student_fee_payment_history_id as student_fee_payment_history_id,sph.inr_value as inr_value,"
				+ " sph.balance_amount as balance_amount, sph.fee_template_id as fee_template_id,sph.paid_amount as paid_amount,"
				+ " sph.paid_year as paid_year, sph.to_pay as to_pay, sph.total_amount as total_amount,"
				+ " sph.voucher_head_new_id as voucher_head_new_id, vhn.voucher_head as voucher_head, vhn.voucher_head_short_name as voucher_head_short_name) From FeeReceipt frc"
				+ " Inner Join StudentPaymentHistory sph On frc.student_id =sph.student_id And frc.financial_year_id =sph.financial_year_id And frc.fee_receipt_id =sph.fee_receipt_id"
				+ " Left Join VoucherHeadNew vhn on vhn.voucher_head_new_id=sph.voucher_head_new_id"
				+ " Where frc.student_id=?1 And frc.active=true")
	public List<HashMap<String, Object>> getAllDataOfFeeReceiptForFormating(Integer student_id);
	 
		//@Query(value = "Select COALESCE(Max(fee_receipt),0) From FeeReceipt frc where school_id=?1 And financial_year_id=?2 And hostel_status=?3")
		//public Integer getMaxId1(Integer school_id, Integer financial_year_id, Integer hostel_status);
	 
	 @Query(value ="Select new map(frc.fee_receipt_id as id,frc.change_course_id as change_course_id,frc.fee_payment_id as fee_payment_id,"
		 		+ "frc.remarks as remarks,frc.bus_fee_receipt_id as bus_fee_receipt_id,frc.student_id as student_id,"
		 		+ "frc.ac_year_id as ac_year_id,frc.fee_receipt as fee_receipt,frc.school_id as school_id,"
		 		+ "frc.financial_year_id as financial_year_id,frc.paid_amount as paid_amount,frc.transaction_type as transaction_type,"
		 		+ "frc.exam_id as exam_id,frc.bulk_id as bulk_id,frc.receipt_type as receipt_type,frc.received_in as received_in,"
		 		+ "frc.inr_value as inr_value,frc.paid_year as paid_year,frc.cancel_by as cancel_by,frc.cancel_date as cancel_date,"
		 		+ "frc.cancel_remarks as cancel_remarks,frc.print_status as print_status,frc.hostel_bulk_id as hostel_bulk_id,"
		 		+ "frc.hostel_fee_payment_id as hostel_fee_payment_id,frc.hostel_status as hostel_status,"
		 		+ "frc.bank_transaction_history_id as bank_transaction_history_id,frc.vendor_id as vendor_id,"
		 		+ "frc.created_username as created_username,frc.modified_username as modified_username,frc.amount_in_som as amount_in_som,"
		 		+ "frc.created_date as created_date,frc.modified_date as modified_date,frc.created_by as created_by,"
		 		+ "frc.modified_by as modified_by,frc.active as active,sd.student_name as student_name,ac.ac_year as ac_year,"
		 		+ "sd.auid as auid,fy.from_date as from_date,fy.to_date as to_date,"
		 		+ "bit.bank_import_transaction_id as bank_import_transaction_id,bit.cheque_dd_no as cheque_dd_no,bit.paid as paid,"
		 		+ "bit.transaction_no as transaction_no,ba.bank_name as bank_name,ba.bank_short_name as bank_short_name,ba.bank_id as bank_id,"
		 		+ "ft.fee_template_id as fee_template_id,frc.voucher_head_new_id as voucher_head_new_id,"
		 		+ "ft.fee_template_name as fee_template_name,"
		 		+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year) From FeeReceipt frc "
		 		+ "left join Student_Details sd on frc.student_id = sd.student_id "
		 		+ "left join Academic_year ac on frc.ac_year_id=ac.ac_year_id "
		 		+ "left join Schools sc on frc.school_id=sc.school_id "
		 		+ "left join BankImportTransaction bit on frc.bank_transaction_history_id=bit.bank_import_transaction_id "
		 		+ "left join Bank ba on bit.deposited_bank_id=ba.bank_id "
		 		+ "left join FeeTemplate ft on ft.fee_template_id=sd.fee_template_id "
		 		+ "left join FinancialYear fy on frc.financial_year_id=fy.financial_year_id "
		 		+ "Where frc.active=true And CONCAT(IfNull(frc.fee_receipt_id,''),'',IfNull(frc.paid_amount,''),'',IfNull(frc.transaction_type,''),'',"
		 		+ "IfNull(frc.receipt_type,''),'',IfNull(frc.paid_year,''),'',IfNull(frc.cancel_date,''),'',IfNull(frc.cancel_remarks,''),'',"
		 		+ "IfNull(frc.print_status,''),'',IfNull(frc.hostel_status,''),'',IfNull(frc.created_username,''),'',IfNull(frc.created_date,''),'',"
		 		+ "IfNull(frc.created_by,''),'',IfNull(sd.student_name,''),'',IfNull(ac.ac_year,''),'',IfNull(sc.school_name_short,''),'',"
		 		+ "IfNull(fy.financial_year,''),'',IfNull(frc.fee_receipt,'')) LIKE %?1%")
		 public Page<Object> findAll1(Pageable pageable, Object keyword);
		 
		 @Query(value ="Select new map(frc.fee_receipt_id as id,frc.change_course_id as change_course_id,frc.fee_payment_id as fee_payment_id,"
			 		+ "frc.remarks as remarks,frc.bus_fee_receipt_id as bus_fee_receipt_id,frc.student_id as student_id,"
			 		+ "frc.ac_year_id as ac_year_id,frc.fee_receipt as fee_receipt,frc.school_id as school_id,"
			 		+ "frc.financial_year_id as financial_year_id,frc.paid_amount as paid_amount,frc.transaction_type as transaction_type,"
			 		+ "frc.exam_id as exam_id,frc.bulk_id as bulk_id,frc.receipt_type as receipt_type,frc.received_in as received_in,"
			 		+ "frc.inr_value as inr_value,frc.paid_year as paid_year,frc.cancel_by as cancel_by,frc.cancel_date as cancel_date,"
			 		+ "frc.cancel_remarks as cancel_remarks,frc.print_status as print_status,frc.hostel_bulk_id as hostel_bulk_id,"
			 		+ "frc.hostel_fee_payment_id as hostel_fee_payment_id,frc.hostel_status as hostel_status,"
			 		+ "frc.bank_transaction_history_id as bank_transaction_history_id,frc.vendor_id as vendor_id,"
			 		+ "frc.created_username as created_username,frc.modified_username as modified_username,frc.amount_in_som as amount_in_som,"
			 		+ "frc.created_date as created_date,frc.modified_date as modified_date,frc.created_by as created_by,"
			 		+ "frc.modified_by as modified_by,frc.active as active,sd.student_name as student_name,ac.ac_year as ac_year,"
			 		+ "sd.auid as auid,fy.from_date as from_date,fy.to_date as to_date,"
			 		+ "bit.bank_import_transaction_id as bank_import_transaction_id,bit.cheque_dd_no as cheque_dd_no,bit.paid as paid,"
			 		+ "bit.transaction_no as transaction_no,ba.bank_name as bank_name,ba.bank_short_name as bank_short_name,ba.bank_id as bank_id,"
			 		+ "ft.fee_template_id as fee_template_id,frc.voucher_head_new_id as voucher_head_new_id,"
			 		+ "ft.fee_template_name as fee_template_name,"
			 		+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year) From FeeReceipt frc "
			 		+ "left join Student_Details sd on frc.student_id = sd.student_id "
			 		+ "left join Academic_year ac on frc.ac_year_id=ac.ac_year_id "
			 		+ "left join Schools sc on frc.school_id=sc.school_id "
			 		+ "left join BankImportTransaction bit on frc.bank_transaction_history_id=bit.bank_import_transaction_id "
			 		+ "left join Bank ba on bit.deposited_bank_id=ba.bank_id "
			 		+ "left join FeeTemplate ft on ft.fee_template_id=sd.fee_template_id "
			 		+ "left join FinancialYear fy on frc.financial_year_id=fy.financial_year_id "
			 		+ "where frc.active=true")
		 public Page<Object> findAll2(Pageable pageable);

		@Query(value = "select * from fee_receipt ORDER BY fee_receipt_id Desc LIMIT 1",nativeQuery = true)
		public FeeReceipt getLatestFeeReceipt();

		 @Query(value = "select ifNull(fee_receipt,0) from fee_receipt ORDER BY fee_receipt_id Desc LIMIT 1",nativeQuery=true)
			public String getFeeReceipt();

		 @Query(value = "select fr.receipt_type from fee_receipt fr where fr.fee_receipt=?1 And fr.financial_year_id=?2 and fr.school_id=?3 and fr.active=true",nativeQuery = true)
			public String getReceiptType(String fee_receipt, Integer financial_year_id, Integer school_id);

			@Query(value ="select bfr.voucher_head_new_id as voucher_head_new_id,vhn.voucher_head as voucher_head,fr.fee_receipt as fee_receipt,"
					+ "bfr.amount_in_som as amount_in_som,bfr.amount as amount,fr.created_date as receipt_date,fr.created_username as cashier,"
					+ "bfr.remarks as remarks,bfr.fee_receipt_id as fee_receipt_id,bfr.transaction_type as transaction_type,"
					+ "bit.cheque_dd_no as cheque_dd_no,bit.transaction_date as transaction_date,b.bank_name as bank_name,b.bank_short_name as bank_short_name,"
					+ "vhn.voucher_head_short_name as voucher_head_short_name,bfr.from_name as from_name from BulkFeeReceipt bfr "
					+ "Left join VoucherHeadNew vhn on bfr.voucher_head_new_id=vhn.voucher_head_new_id "
					+ "Left join FeeReceipt fr on bfr.bulk_fee_receipt=fr.bulk_id "
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
			 		+ "Where fr.fee_receipt=?1 And fr.active=true And fr.school_id=?2 And fr.financial_year_id=?3 ",nativeQuery=true)
			public List<Map<String, Object>> getDataForDisplayingStudentDetailsBulkFeeRcptAndCancel(String fee_receipt, Integer schoolId, Integer fcYearId);

		@Modifying
		@Query(value = "Update FeeReceipt frc Set frc.active=false Where frc.fee_receipt_id=?1")
		public void cancelFeeReceipt1(Integer fee_receipt_id);
		
		@Modifying
		@Query(value = "Update FeeReceipt frc Set frc.bank_transaction_history_id=?1 Where frc.fee_receipt=?2 And frc.bank_transaction_history_id is null")
		public void updationOfBankTransactionHistoryId( Integer bank_import_transaction_id,String fee_receipt);

				@Modifying
		@Query(value = "Update FeeReceipt frc Set frc.student_id=?4 Where frc.fee_receipt_id=?1 And frc.fee_receipt=?2 And frc.student_id=?3")
		public void updateFeeReceiptStudentId(Integer fee_receipt_id,String fee_receipt,Integer oldStudentId,Integer newStudentId);

				
				@Query(value ="Select new map(frc.fee_receipt_id as id,frc.change_course_id as change_course_id,frc.fee_payment_id as fee_payment_id,"
				 		+ "frc.remarks as remarks,frc.bus_fee_receipt_id as bus_fee_receipt_id,frc.student_id as student_id,"
				 		+ "frc.ac_year_id as ac_year_id,frc.fee_receipt as fee_receipt,frc.school_id as school_id,"
				 		+ "frc.financial_year_id as financial_year_id,frc.paid_amount as paid_amount,frc.transaction_type as transaction_type,"
				 		+ "frc.exam_id as exam_id,frc.bulk_id as bulk_id,frc.receipt_type as receipt_type,frc.received_in as received_in,"
				 		+ "frc.inr_value as inr_value,frc.paid_year as paid_year,frc.cancel_by as cancel_by,frc.cancel_date as cancel_date,"
				 		+ "frc.cancel_remarks as cancel_remarks,frc.print_status as print_status,frc.hostel_bulk_id as hostel_bulk_id,"
				 		+ "frc.hostel_fee_payment_id as hostel_fee_payment_id,frc.hostel_status as hostel_status,"
				 		+ "frc.bank_transaction_history_id as bank_transaction_history_id,frc.vendor_id as vendor_id,"
				 		+ "frc.created_username as created_username,frc.modified_username as modified_username,frc.amount_in_som as amount_in_som,"
				 		+ "frc.created_date as created_date,frc.modified_date as modified_date,frc.created_by as created_by,"
				 		+ "frc.modified_by as modified_by,frc.active as active,sd.student_name as student_name,ac.ac_year as ac_year,"
				 		+ "sd.auid as auid,fy.from_date as from_date,fy.to_date as to_date,"
				 		+ "bit.bank_import_transaction_id as bank_import_transaction_id,bit.cheque_dd_no as cheque_dd_no,bit.paid as paid,"
				 		+ "bit.transaction_no as transaction_no,ba.bank_name as bank_name,ba.bank_short_name as bank_short_name,ba.bank_id as bank_id,"
				 		+ "ft.fee_template_id as fee_template_id,frc.voucher_head_new_id as voucher_head_new_id,"
				 		+ "ft.fee_template_name as fee_template_name,pr.program_name as program_name,pr.program_short_name as program_short_name,"
				 		+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year, ps.program_specialization_name as program_specialization_name,"
				 		+ "ps.program_specialization_short_name as program_specialization_short_name) From FeeReceipt frc "
				 		+ "left join Student_Details sd on frc.student_id = sd.student_id "
				 		+ "left join Program pr on sd.program_id=pr.program_id "
				 		+ "left join ProgramSpecilization ps on sd.program_specialization_id = ps.program_specialization_id "
				 		+ "left join Academic_year ac on frc.ac_year_id=ac.ac_year_id "
				 		+ "left join Schools sc on frc.school_id=sc.school_id "
				 		+ "left join BankImportTransaction bit on frc.bank_transaction_history_id=bit.bank_import_transaction_id "
				 		+ "left join Bank ba on bit.deposited_bank_id=ba.bank_id "
				 		+ "left join FeeTemplate ft on ft.fee_template_id=sd.fee_template_id "
				 		+ "left join FinancialYear fy on frc.financial_year_id=fy.financial_year_id "
				 		+ "where frc.student_id=?1 and frc.active=true")
				public List<HashMap<String,Object>> feeReceiptByStudenId(Integer oldStudentId);
				
				@Query(value = "select * from fee_receipt Where financial_year_id=?1 And school_id=?2 ORDER BY fee_receipt_id Desc LIMIT 1",nativeQuery=true)
				public FeeReceipt getLastFeeReceiptByFinancialIdAndSchoolId(Integer financial_year_id, Integer school_id);

            	@Query(value = "select * from fee_receipt Where financial_year_id=?1 And school_id=?2 And hostel_status=?3 And active = true ORDER BY fee_receipt_id Desc LIMIT 1",nativeQuery=true)
            	public FeeReceipt getLastFeeReceiptByFinancialIdAndSchoolIdAndHostelStatus(Integer financial_year_id, Integer school_id,Integer hostelStatus);

	           @Query(value = "select * from fee_receipt Where financial_year_id=?1 And hostel_status=?2 ORDER BY fee_receipt_id Desc LIMIT 1",nativeQuery=true)
	           public FeeReceipt getLastFeeReceiptByFinancialIdAndHostelStatus(Integer financial_year_id,Integer hostelStatus);


	@Query(value = "select * from fee_receipt Where financial_year_id=?1 ORDER BY fee_receipt_id Desc LIMIT 1",nativeQuery=true)
				public FeeReceipt getLastFeeReceiptByFinancialId(Integer financial_year_id);

	
				@Query(value ="Select new map(frc.fee_receipt_id as id,frc.change_course_id as change_course_id,frc.fee_payment_id as fee_payment_id,"
				 		+ "frc.remarks as remarks,frc.bus_fee_receipt_id as bus_fee_receipt_id,frc.student_id as student_id,"
				 		+ "frc.ac_year_id as ac_year_id,frc.fee_receipt as fee_receipt,frc.school_id as school_id,"
				 		+ "frc.financial_year_id as financial_year_id,frc.paid_amount as paid_amount,frc.transaction_type as transaction_type,"
				 		+ "frc.exam_id as exam_id,frc.bulk_id as bulk_id,frc.receipt_type as receipt_type,frc.received_in as received_in,"
				 		+ "frc.inr_value as inr_value,frc.paid_year as paid_year,frc.cancel_by as cancel_by,frc.cancel_date as cancel_date,"
				 		+ "frc.cancel_remarks as cancel_remarks,frc.print_status as print_status,frc.hostel_bulk_id as hostel_bulk_id,"
				 		+ "frc.hostel_fee_payment_id as hostel_fee_payment_id,frc.hostel_status as hostel_status,"
				 		+ "frc.bank_transaction_history_id as bank_transaction_history_id,frc.vendor_id as vendor_id,"
				 		+ "frc.created_username as created_username,frc.modified_username as modified_username,frc.amount_in_som as amount_in_som,"
				 		+ "frc.created_date as created_date,frc.modified_date as modified_date,frc.created_by as created_by,"
				 		+ "frc.modified_by as modified_by,frc.active as active,sph.student_fee_payment_history_id as student_fee_payment_history_id,"
				 		+ "sph.balance_amount as balance_amount,sph.transcation_type as transcation_type,sph.total_amount as total_amount,sph.to_pay as to_pay,"
				 		+ "sph.remarks as sphRemarks,sph.paid_year as sphPaid_year,sph.paid_amount as sphPaid_amount,vhn.voucher_head_new_id as voucher_head_new_id,"
				 		+ "vhn.voucher_head as voucher_head,vhn.voucher_head_short_name as voucher_head_short_name,vhn.voucher_type as voucher_type,"
				 		+ "sc.school_name as school_name,sc.school_name_short as school_name_short,bit.bank_import_transaction_id as bank_import_transaction_id,"
				 		+ "bit.transaction_no as transaction_no,bit.transaction_date as transaction_date,fy.financial_year as financial_year,"
				 		+ "ac.ac_year as ac_year,ac.ac_year_code as ac_year_code,ac.current_year as current_year,sd.student_name as student_name,sd.auid as auid) From FeeReceipt frc "
				 		+ "Left join StudentPaymentHistory sph on frc.fee_receipt_id = sph.fee_receipt_id "
				 		+ "left join VoucherHeadNew vhn on sph.voucher_head_new_id = vhn.voucher_head_new_id "
				 		+ "left join Schools sc on frc.school_id = sc.school_id "
				 		+ "left join Academic_year ac on frc.ac_year_id = ac.ac_year_id "
				 		+ "left join BankImportTransaction bit on frc.bank_transaction_history_id = bit.bank_import_transaction_id "
				 		+ "left join FinancialYear fy on frc.financial_year_id = fy.financial_year_id "
				 		+ "left join Student_Details sd on frc.student_id = sd.student_id "
				 		+ "where frc.active=true And frc.fee_receipt_id=?1 ")		
				public List<HashMap<String, Object>> getFeeReceiptDetailsData(Integer fee_receipt_id);
				
				@Modifying
				@Query(value = "Update FeeReceipt frc Set frc.exam_id=?1 Where frc.fee_receipt_id=?2")
				public void updateExamIdInFeeReceipt(String examFeeReceiptIds, Integer fee_receipt_id);
				
		@Query(value ="Select new map(frc.fee_receipt_id as id,frc.change_course_id as change_course_id,frc.fee_payment_id as fee_payment_id,"
		 		+ "frc.remarks as remarks,frc.bus_fee_receipt_id as bus_fee_receipt_id,frc.student_id as student_id,"
		 		+ "frc.ac_year_id as ac_year_id,frc.fee_receipt as fee_receipt,frc.school_id as school_id,"
		 		+ "frc.financial_year_id as financial_year_id,frc.paid_amount as paid_amount,frc.transaction_type as transaction_type,"
		 		+ "frc.exam_id as exam_id,frc.bulk_id as bulk_id,frc.receipt_type as receipt_type,frc.received_in as received_in,"
		 		+ "frc.inr_value as inr_value,frc.paid_year as paid_year,frc.cancel_by as cancel_by,frc.cancel_date as cancel_date,"
		 		+ "frc.cancel_remarks as cancel_remarks,frc.print_status as print_status,frc.hostel_bulk_id as hostel_bulk_id,"
		 		+ "frc.hostel_fee_payment_id as hostel_fee_payment_id,frc.hostel_status as hostel_status,"
		 		+ "frc.bank_transaction_history_id as bank_transaction_history_id,frc.vendor_id as vendor_id,"
		 		+ "frc.created_username as created_username,frc.modified_username as modified_username,frc.amount_in_som as amount_in_som,"
		 		+ "frc.created_date as created_date,frc.modified_date as modified_date,frc.created_by as created_by,"
		 		+ "frc.modified_by as modified_by,frc.active as active,"
		 		+ "sd.usn As usn,sd.mobile As mobile,sd.fee_admission_category_id As fee_admission_category_id,fac.fee_admission_category_type As fee_admission_category_type,"
		 		+ "fac.fee_admission_category_short_name As fee_admission_category_short_name,"
		 		+ "sc.school_name as school_name,sc.school_name_short as school_name_short,bit.bank_import_transaction_id as bank_import_transaction_id,"
		 		+ "bit.transaction_no as transaction_no,bit.transaction_date as transaction_date,fy.financial_year as financial_year,"
		 		+ "ac.ac_year as ac_year,ac.ac_year_code as ac_year_code,ac.current_year as current_year,sd.student_name as student_name,sd.auid as auid) From FeeReceipt frc "
		 		+ "left join Schools sc on frc.school_id = sc.school_id "
		 		+ "left join Academic_year ac on frc.ac_year_id = ac.ac_year_id "
		 		+ "left join BankImportTransaction bit on frc.bank_transaction_history_id = bit.bank_import_transaction_id "
		 		+ "left join FinancialYear fy on frc.financial_year_id = fy.financial_year_id "
		 		+ "left join Student_Details sd on frc.student_id = sd.student_id "
		 		+ "left join FeeAdmissionCategory fac on fac.fee_admission_category_id = sd.fee_admission_category_id "
		 		+ "where frc.active=true And frc.fee_receipt_id=?1 ")		
		public List<HashMap<String, Object>> getFeeReceiptAndStudentDetailsByFeeReceiptId(Integer fee_receipt_id);
		



		@Query(value=" select fr.fee_receipt as receiptNo, fr.created_date as createdDate, fr.paid_amount as amount, fr.receipt_type as receiptType,"
				+ "fr.fee_receipt_id As fee_receipt_id,fr.transaction_type As transaction_type,fr.financial_year_id As financial_year_id,"
				+ " fy.financial_year as year from FeeReceipt fr  "
				+ " left join FinancialYear fy on fy.financial_year_id=fr.financial_year_id  where fr.student_id=:studentId ")
		public List<Map<String, Object>> getFeeReceiptDetails(Integer studentId);
		
		@Query(value="Select fr.fee_receipt as cocReceiptNo, fr.created_date as createdDate, fr.paid_amount as cocPaidAmount, fr.receipt_type as receiptType,"
				+ "fr.fee_receipt_id As fee_receipt_id,fr.transaction_type As transaction_type From FeeReceipt fr Where fr.student_id=:studentId And fr.receipt_type ='COC Fee' And active=true ")
		public List<Map<String, Object>> changeOfCourseFeePaidStatusByStudentId(Integer studentId);

		@Query(value = "select fr from FeeReceipt fr where fr.financial_year_id=?1 And fr.active=true")
		public List<FeeReceipt> getFeerecepitType(Integer financial_year_id);
		
		@Query(value ="Select sum(sph.total_amount) "
				+ "from student_payment_history sph "
				+ "Where sph.financial_year_id=?1 And sph.voucher_head_new_id=?1 And sph.active=true",nativeQuery=true)
		public Double getPaidAmount(Integer financial_year_id,Integer voucher_head_id);
		
		@Query(value ="Select sum(bfr.amount) "
				+ "from bulk_fee_receipt bfr "
				+ "Where bfr.financial_year_id=?1 And bfr.voucher_head_new_id=?1 And bfr.active=true",nativeQuery=true)
		public Double getbulkPaidAmount(Integer financial_year_id,Integer voucher_head_id);
		
		@Query(value="select f from FeeReceipt f where f.bank_transaction_history_id=:bankImportId and f.active = true ")
       public List<FeeReceipt> getFeeReceiptsByBankImportId(Integer bankImportId);
		
		
		
	@Query(value ="Select frc.fee_receipt_id as id,frc.change_course_id as change_course_id,frc.fee_payment_id as fee_payment_id,"
 		+ "frc.remarks as remarks,frc.bus_fee_receipt_id as bus_fee_receipt_id,frc.student_id as student_id,"
 		+ "frc.ac_year_id as ac_year_id,frc.fee_receipt as fee_receipt,frc.school_id as school_id,"
 		+ "frc.financial_year_id as financial_year_id,frc.paid_amount as paid_amount,frc.transaction_type as transaction_type,"
 		+ "frc.exam_id as exam_id,frc.bulk_id as bulk_id,frc.receipt_type as receipt_type,frc.received_in as received_in,"
 		+ "frc.inr_value as inr_value,frc.paid_year as paid_year,frc.cancel_by as cancel_by,frc.cancel_date as cancel_date,"
 		+ "frc.cancel_remarks as cancel_remarks,frc.print_status as print_status,frc.hostel_bulk_id as hostel_bulk_id,"
 		+ "frc.hostel_fee_payment_id as hostel_fee_payment_id,frc.hostel_status as hostel_status,"
 		+ "frc.bank_transaction_history_id as bank_transaction_history_id,frc.vendor_id as vendor_id,"
 		+ "frc.created_username as created_username,frc.modified_username as modified_username,frc.amount_in_som as amount_in_som,"
 		+ "frc.created_date as created_date,frc.modified_date as modified_date,frc.created_by as created_by,"
 		+ "frc.modified_by as modified_by,frc.active as active,sd.student_name as student_name,ac.ac_year as ac_year,"
 		+ "sd.auid as auid,fy.from_date as from_date,fy.to_date as to_date,"
 		+ "bit.bank_import_transaction_id as bank_import_transaction_id,bit.cheque_dd_no as cheque_dd_no,bit.paid as paid,"
 		+ "bit.transaction_no as transaction_no,ba.bank_name as bank_name,ba.bank_short_name as bank_short_name,ba.bank_id as bank_id,"
 		+ "ft.fee_template_id as fee_template_id,frc.voucher_head_new_id as voucher_head_new_id,"
 		+ "ft.fee_template_name as fee_template_name,"
 		+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year, sd.fee_template_id as template, "
		+ " bit.transaction_date as transaction_date, bfr.from_name as bulk_user_name, dd.dd_number as dd_number, dd.cleared_date as dd_cleared_date, dd.bank_name as dd_bank_name, dd.dd_date as dd_date  From fee_receipt frc "
 		+ "left join student_details sd on frc.student_id = sd.student_id "
 		+ "left join academic_year ac on frc.ac_year_id=ac.ac_year_id "
 		+ "left join schools sc on frc.school_id=sc.school_id "
 		+ "left join bank_import_transaction bit on frc.bank_transaction_history_id=bit.bank_import_transaction_id "
 		+ "left join bank ba on frc.bank_id=ba.bank_id "
 		+ "left join fee_template ft on ft.fee_template_id=sd.fee_template_id "
 		+ "left join financial_year fy on frc.financial_year_id=fy.financial_year_id "
		+ "left join bulk_fee_receipt bfr on bfr.bulk_fee_receipt = frc.bulk_id "
		+ "left join dd_details dd on dd.fee_receipt = frc.fee_receipt_id  "
 		+ "Where CONCAT(IfNull(frc.fee_receipt_id,''),'',IfNull(frc.paid_amount,''),'',IfNull(frc.transaction_type,''),'',"
 		+ "IfNull(frc.receipt_type,''),'',IfNull(frc.paid_year,''),'',IfNull(frc.cancel_date,''),'',IfNull(frc.cancel_remarks,''),'',"
 		+ "IfNull(frc.print_status,''),'',IfNull(frc.hostel_status,''),'',IfNull(frc.created_username,''),'',IfNull(frc.created_date,''),'',"
 		+ "IfNull(frc.created_by,''),'',IfNull(sd.student_name,''),'',IfNull(ac.ac_year,''),'',IfNull(sc.school_name_short,''),'',"
 		+ "IfNull(fy.financial_year,''),'',IfNull(frc.fee_receipt,'')) LIKE %:keyword% "
		+ "AND (DATE(frc.created_date) >= :minDate) "
 	    + "AND (:start IS NULL OR DATE(frc.created_date) >= :start) "
	        + "AND (:end IS NULL OR DATE(frc.created_date) <= :end) "
	        + "AND (:school_id IS NULL OR frc.school_id = :school_id) "
	        + "ORDER BY frc.created_date DESC", nativeQuery = true)		
	public List<Map<String, Object>> FeeReceiptListAll1(Pageable pageable, Object keyword, Integer school_id, LocalDate start, LocalDate end, LocalDate minDate);


	@Query(value ="Select frc.fee_receipt_id as id,frc.change_course_id as change_course_id,frc.fee_payment_id as fee_payment_id,"
			+ "frc.remarks as remarks,frc.bus_fee_receipt_id as bus_fee_receipt_id,frc.student_id as student_id,"
			+ "frc.ac_year_id as ac_year_id,frc.fee_receipt as fee_receipt,frc.school_id as school_id,"
			+ "frc.financial_year_id as financial_year_id,frc.paid_amount as paid_amount,frc.transaction_type as transaction_type,"
			+ "frc.exam_id as exam_id,frc.bulk_id as bulk_id,frc.receipt_type as receipt_type,frc.received_in as received_in,"
			+ "frc.inr_value as inr_value,frc.paid_year as paid_year,frc.cancel_by as cancel_by,frc.cancel_date as cancel_date,"
			+ "frc.cancel_remarks as cancel_remarks,frc.print_status as print_status,frc.hostel_bulk_id as hostel_bulk_id,"
			+ "frc.hostel_fee_payment_id as hostel_fee_payment_id,frc.hostel_status as hostel_status,"
			+ "frc.bank_transaction_history_id as bank_transaction_history_id,frc.vendor_id as vendor_id,"
			+ "frc.created_username as created_username,frc.modified_username as modified_username,frc.amount_in_som as amount_in_som,"
			+ "frc.created_date as created_date,frc.modified_date as modified_date,frc.created_by as created_by,"
			+ "frc.modified_by as modified_by,frc.active as active,sd.student_name as student_name,ac.ac_year as ac_year,"
			+ "sd.auid as auid,fy.from_date as from_date,fy.to_date as to_date,"
			+ "bit.bank_import_transaction_id as bank_import_transaction_id,bit.cheque_dd_no as cheque_dd_no,bit.paid as paid,"
			+ "bit.transaction_no as transaction_no,ba.bank_name as bank_name,ba.bank_short_name as bank_short_name,ba.bank_id as bank_id,"
			+ "ft.fee_template_id as fee_template_id,frc.voucher_head_new_id as voucher_head_new_id,"
			+ "ft.fee_template_name as fee_template_name,"
			+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year, sd.fee_template_id as template, "
			+ " bit.transaction_date as transaction_date,  bfr.from_name as bulk_user_name, dd.dd_number as dd_number, dd.cleared_date as dd_cleared_date, dd.bank_name as dd_bank_name, dd.dd_date as dd_date  From fee_receipt frc "
			+ "left join student_details sd on frc.student_id = sd.student_id "
			+ "left join academic_year ac on frc.ac_year_id=ac.ac_year_id "
			+ "left join schools sc on frc.school_id=sc.school_id "
			+ "left join bank_import_transaction bit on frc.bank_transaction_history_id=bit.bank_import_transaction_id "
			+ "left join bank ba on frc.bank_id=ba.bank_id "
			+ "left join fee_template ft on ft.fee_template_id=sd.fee_template_id "
			+ "left join financial_year fy on frc.financial_year_id=fy.financial_year_id "
			+ "left join bulk_fee_receipt bfr on bfr.bulk_fee_receipt = frc.bulk_id "
			+ "left join dd_details dd on dd.fee_receipt = frc.fee_receipt_id  "
 		  + "WHERE "
 	        + "  (:start IS NULL OR DATE(frc.created_date) >= :start) "
			+ "AND (DATE(frc.created_date) >= :minDate) "
 	        + "AND (:end IS NULL OR DATE(frc.created_date) <= :end) "
 	       + "AND (:school_id IS NULL OR frc.school_id = :school_id) "
 	        + "ORDER BY frc.created_date DESC", nativeQuery = true)		
	 public List<Map<String, Object>> FeeReceiptListAll12(Pageable pageable, Integer school_id, LocalDate start, LocalDate end, LocalDate minDate);

	 @Query(value="SELECT created_by,receipt_type, SUM(paid_amount) AS total_paid_amount FROM fee_receipt "
	 		+ "where (:start IS NULL OR DATE(created_date) >= :start) "
	 		+ "And (:end IS NULL OR DATE(created_date) <= :end) "
	 		+ "GROUP BY created_by, receipt_type ORDER BY created_by, receipt_type ", nativeQuery = true)
	public List<Map<String, Object>> getFeeReceipyWiseAndUserWiseData(LocalDate start, LocalDate end);

	 @Query(value="select fee_receipt_id from fee_receipt "
	 		+ " where student_id = :student_id and school_id = :school_id "
	 		+ " and bank_transaction_history_id = :bank_import_transaction_id and financial_year_id = :fcYearId ", nativeQuery = true)
	public Integer getFeeReceiptId(Integer student_id, Integer bank_import_transaction_id, Integer fcYearId,
			Integer school_id);

	@Query(value="select f from FeeReceipt f where f.bank_transaction_history_id=:bankImportTransactionId and f.receipt_type=:receiptType and f.active = true ")
	List<FeeReceipt> getFeeReceiptsByBankImportIdAndReceiptType(Integer bankImportTransactionId, String receiptType);

	@Query(value = "select b.auid AS auid, fr.fee_receipt AS feeReceipt, b.transaction_no AS paymentId , b.order_id AS orderID, fr.paid_amount AS amount, fr.receipt_type AS receiptType, rppd.paid_year AS paidYear, b.transaction_date AS transactionDate from fee_receipt fr " +
			"left join bank_import_transaction  b " +
			"on b.bank_import_transaction_id =  fr.bank_transaction_history_id " +
			"left join razor_pay_transaction rpt " +
			"on rpt.order_id = b.order_id " +
			"left join razor_pay_payment_details rppd " +
			"on rppd.razor_pay_transaction_id = rpt.razor_pay_transaction_id " +
			"where ?1 is null or CAST(fr.created_date AS DATE) like concat(?1,'%') " +
			"and fr.fee_receipt is not null order by fr.created_date desc;",nativeQuery = true)
	List<Map<String,Object>> getFeeReceiptsByDate(@Param("date")String date);


	@Query("SELECT CASE WHEN COUNT(fr) > 0 THEN true ELSE false END " +
			"FROM FeeReceipt fr " +
			"JOIN BankImportTransaction b ON b.bank_import_transaction_id = fr.bank_transaction_history_id " +
			"WHERE b.order_id = :orderId AND fr.active = true AND b.active = true")
	boolean existsByBankTransactionHistoryId(@Param("orderId") String orderId);

	 @Query(value="select fr.* from fee_receipt fr where fr.financial_year_id =?1 And fr.fee_receipt =?2 And fr.receipt_type =?3 And fr.active=true", nativeQuery = true)
	public FeeReceipt getFeeReceiptDetails(Integer financial_year_id, Integer fee_receipt, String receipt_type);

	 

	 @Query(value="SELECT fr.fee_receipt_id as fee_receipt_id,fr.student_id as student_id,sd.auid As auid,sd.usn As usn,sd.acharya_email As acharya_email,"
	 		+ "sd.father_name As father_name,sd.student_name As student_name,sd.mobile As mobile,sc.school_id As school_id,sc.school_name As school_name,"
	 		+ "ft.fee_template_id As fee_template_id,ft.fee_template_name As fee_template_name,rs.reporting_id As reporting_id,rs.current_year As current_year,"
	 		+ "pr.program_name as program_name,pr.program_short_name as program_short_name,ps.program_specialization_name as program_specialization_name,"
	 		+ "ps.program_specialization_short_name as program_specialization_short_name,sc.school_name_short As school_name_short,rs.current_sem As current_sem "
	 		+ "FROM fee_receipt fr "
	 		+ "left join student_details sd on fr.student_id = sd.student_id "
	 		+ "left join schools sc on sc.school_id = sd.school_id "
	 		+ "left join program pr on sd.program_id=pr.program_id "
	 		+ "left join program_specialization ps on sd.program_specialization_id = ps.program_specialization_id "
	 		+ "left join fee_template ft on ft.fee_template_id=sd.fee_template_id "
	 		+ "left join reporting_students rs on rs.student_id=sd.student_id "
	 		+ "where fr.financial_year_id =?1 And fr.fee_receipt =?2 And fr.receipt_type =?3 And fr.active=true Group by fr.fee_receipt_id ", nativeQuery = true)
	public Map<String,Object> getFeeReceiptDetailsForHeadwise(Integer financial_year_id, Integer fee_receipt, String receipt_type);

	@Query(value = "select fr.created_username as createdUsername, fr.transaction_type as transactionType, " +
			"COALESCE(SUM(fr.inr_value), 0) as inrValue, fr.received_in as receivedIn " +
			"from FeeReceipt fr " +
			"where DATE(fr.created_date) between DATE(:fromDate) and DATE(:toDate) " +
			"GROUP BY fr.created_username, fr.transaction_type")
	List<Map<String, Object>> findByDateAndTransactionType(String fromDate, String toDate);

	@Query("select sc.school_id as schoolId, sc.school_name_short as schoolName, fr.transaction_type as transactionType, " +
			"COALESCE(SUM(fr.inr_value), 0) as inrValue, fr.received_in as receivedIn " +
			"from FeeReceipt fr " +
			"left join Schools sc on sc.school_id = fr.school_id " +
			"where (:fromDate is null or DATE(fr.created_date) >= DATE(:fromDate)) " +
			"and (:toDate is null or DATE(fr.created_date) <= DATE(:toDate)) " +
			"GROUP BY fr.transaction_type, sc.school_name_short, sc.school_id, fr.received_in " +
			"ORDER BY sc.school_id")
	List<Map<String, Object>> getFeeReceiptDetailsBySchools(String fromDate, String toDate);

	@Query(value = " select * from fee_receipt where transaction_type = ?1 ORDER BY fee_receipt_id Desc LIMIT 1 ",nativeQuery = true)
	public FeeReceipt getLatestFeeReceiptForDD(String transactionType);

	@Query(value = " select fee_receipt_id from fee_receipt where fee_receipt = ?1  and bulk_id = ?3  and financial_year_id = ?2 and active = true ",nativeQuery = true)
	Integer getFeeReceiptIdByFeeReceiptAndFcYearAndBulkId(Integer feeReceipt, Integer fcYear, Integer bulkId);

	@Query(value = " SELECT fr.bank_transaction_history_id FROM FeeReceipt fr WHERE fr.fee_receipt_id =:feeReceiptId ")
	public Integer getBankImportByFeeReceiptId(Integer feeReceiptId);
	
	@Query(value = "SELECT COALESCE(SUM(fr.paid_amount), 0) as paidAmount FROM fee_receipt fr "
			+ "where fr.transaction_type= 'CASH' And DATE(fr.created_date) = DATE(?1) And fr.active=true",nativeQuery = true)
	public Float getPaidAmount(String selected_date);

	@Query(value = "SELECT COALESCE(SUM(fr.paid_amount), 0) as Hos FROM fee_receipt fr "
			+ "where fr.transaction_type= 'CASH' And DATE(fr.created_date) = DATE(?1) And fr.active=true And hostel_status =1",nativeQuery = true)
	public Map<String, Object> getPaidAmountHos(String selected_date);

	@Query(value = "SELECT COALESCE(SUM(fr.paid_amount), 0) as paidAmount,fr.school_id As school_id,sc.school_name_short As school_name_short "
			+ "FROM fee_receipt fr "
			+ "left join schools sc on sc.school_id = fr.school_id "
			+ "where fr.transaction_type= 'CASH' And DATE(fr.created_date) = DATE(?1) "
			+ "And fr.active=true And hostel_status =0 group by fr.school_id",nativeQuery = true)
	public List<Map<String, Object>> getPaidAmountWoHos(String selected_date);
}
