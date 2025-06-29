package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.StudentPaymentHistory;

@Repository
@Transactional
public interface StudentPaymentHistoryRepository extends JpaRepository<StudentPaymentHistory , Integer>{
			
		@Query(value = "Select sph From StudentPaymentHistory sph Where sph.active = true")
		public List<StudentPaymentHistory> getActiveDetails();
		
		@Modifying
		@Query(value = "Update StudentPaymentHistory sph Set sph.active = false Where sph.student_fee_payment_history_id =?1")
		public void delete1(Integer student_fee_payment_history_id);
		
		@Modifying
		@Query(value = "Update StudentPaymentHistory sph Set sph.active = true Where sph.student_fee_payment_history_id =?1")
		public void delete2(Integer student_fee_payment_history_id);
		
		


		@Query(value = "Select COALESCE(sum(sph.paid_amount),0)  From student_payment_history sph where student_id=?1 and fee_receipt=?2 and paid_year=?3",nativeQuery=true)
		public Double sumOfPaidAmount1(Integer student_id, Integer fee_receipt, Integer paid_year );
		
		@Query(value = " Select ifNull(sum(sph.paid_amount),0) From student_payment_history sph "
				+ "Inner join fee_receipt frc On frc.student_id =sph.student_id And frc.financial_year_id =sph.financial_year_id And frc.fee_receipt =sph.fee_receipt "
				+ "Where sph.student_id=?1 And sph.voucher_head_new_id=?2 And sph.paid_year =?3 And sph.school_id=?4 And sph.active=true",nativeQuery=true)
		public Double getPaidAmount(Integer student_id,  Integer voucher_head_new_id,Integer paid_year,Integer school_id);

		@Query(value = "Select COALESCE(sum(sph.paid_amount),0)  From student_payment_history sph where student_id=?1 and receipt_id=?2 and paid_year=?3",nativeQuery=true)
		public Double sumOfPaidAmount(Integer student_id, Integer receipt_id, Integer paid_year );

		@Query("SELECT SUM(COALESCE(sp.paid_amount, 0)) " + "FROM StudentPaymentHistory sp "
				+ "WHERE sp.paid_year=:year AND sp.student_id=:studentId AND sp.active = 1")
		Float findYearPaidByStudentId(@Param("year") Integer year, @Param("studentId") Integer studentId);

		@Query(value = "Select distinct sph.voucher_head_new_id " 
				+ "from student_payment_history sph "
				+ "Left Join fee_receipt fr On fr.fee_receipt_id=sph.fee_receipt_id "
				+ "Where sph.student_id=?1 And sph.financial_year_id=?2 And sph.fee_receipt=?3 And sph.transcation_type=?4  "
				+ "And fr.hostel_status=?5 And sph.active=true", nativeQuery = true)
		public List<Integer> getVoucherHeadNewId(Integer student_id, Integer financial_year_id, String fee_receipt1,
				String transaction_type, Integer hostel_status);

		@Query(value = "Select distinct sph.paid_year " 
				+ "from student_payment_history sph "
				+ "Left Join fee_receipt fr On fr.fee_receipt_id=sph.fee_receipt_id "
				+ "Where sph.student_id=?1 And sph.financial_year_id=?2 And sph.fee_receipt=?3 And sph.transcation_type=?4  "
				+ "And fr.hostel_status=?5 And sph.voucher_head_new_id=?6 And sph.active=true", nativeQuery = true)
		public List<Integer> getPaidYears(Integer student_id, Integer financial_year_id, String fee_receipt1,
				String transaction_type, Integer hostel_status, Integer voucher_head_new_id);

		@Query(value = "Select  sph.inr_value " 
				+ "from student_payment_history sph "
				+ "Left Join fee_receipt fr On fr.fee_receipt_id=sph.fee_receipt_id "
				+ "Where sph.student_id=?1 And sph.financial_year_id=?2 And sph.fee_receipt=?3 And sph.transcation_type=?4  "
				+ "And fr.hostel_status=?5 And sph.paid_year=?6 And sph.voucher_head_new_id=?7 And sph.active=true", nativeQuery = true)
		public Double getAmount(Integer student_id, Integer financial_year_id, String fee_receipt1,
				String transaction_type, Integer hostel_status, Integer paid_year, Integer vou);

		@Query(value = "Select frc.created_username as created_username,sd.father_name as father_name,sph.remarks as remarks,"
				+ "frc.created_date as created_date,frc.created_by as created_by,frc.fee_receipt_id as fee_receipt_id,"
				+ "frc.active as active,sd.student_name as student_name,sd.auid as auid,sd.usn as usn,ac.ac_year as ac_year,"
				+ "scl.school_id as school_id,scl.school_name as school_name,scl.school_name_short as school_name_short,"
				+ "fy.financial_year as financial_year,sd.acharya_email as acharya_email,bit.transaction_no as transaction_no, "
				+ "frc.bank_transaction_history_id as bank_transaction_history_id,bit.cheque_dd_no as cheque_dd_no,"
				+ "bit.transaction_date as transaction_date,frc.transaction_type as transaction_type,frc.transaction_mode as transaction_mode,"
				+ "frc.hostel_status as hostel_status,dd.dd_number As dd_number,dd.dd_date As dd_date,dd.bank_name As dd_bank_name "
				+ " From fee_receipt frc " 
				+ "left join student_details sd on frc.student_id = sd.student_id "
				+ "left join dd_details dd on dd.student_id = sd.student_id "
				+ "left join schools scl on sd.school_id = scl.school_id "
				+ "left join student_payment_history sph on frc.fee_receipt_id = sph.fee_receipt_id "
				+ "left join academic_year ac on frc.ac_year_id=ac.ac_year_id "
				+ "left join schools sc on frc.school_id=sc.school_id "
				+ "left join financial_year fy on frc.financial_year_id=fy.financial_year_id "
				+ "left join bank_import_transaction bit on frc.bank_transaction_history_id=bit.bank_import_transaction_id "
				+ "Where frc.student_id=?1 And frc.financial_year_id=?2 And frc.fee_receipt=?3 And frc.transaction_type=?4 "
				+ "And frc.hostel_status=?5 And frc.active=true", nativeQuery = true)
		public List<Map<String, Object>> getDataForDisplayingFeeReceipt11(Integer student_id, Integer financial_year_id,
				String fee_receipt1, String transaction_type, Integer hostel_status);

		@Query(value = "Select distinct sph.voucher_head_new_id " + "from student_payment_history sph "
				+ "Left Join fee_receipt fr On fr.fee_receipt_id=sph.fee_receipt_id "
				+ "Where fr.financial_year_id=?1 And fr.school_id=?2 And sph.fee_receipt=?3 And sph.active=true", nativeQuery = true)
		public List<Integer> getVoucherHeadNewId(Integer financial_year_id, Integer school_id, String fee_receipt);
		
		@Query(value = "Select distinct sph.paid_year " + "from student_payment_history sph "
				+ "Left Join fee_receipt fr On fr.fee_receipt_id=sph.fee_receipt_id "
				+ "Where fr.financial_year_id=?1 And fr.school_id=?2 And sph.fee_receipt=?3  "
				+ "And sph.voucher_head_new_id=?4 And sph.active=true", nativeQuery = true)
		public List<Integer> getPaidYears(Integer financial_year_id, Integer school_id, String fee_receipt,
				Integer voucher_head_new_id);
		
		@Query(value = "Select  sph.paid_amount " + "from student_payment_history sph "
				+ "Left Join fee_receipt fr On fr.fee_receipt_id=sph.fee_receipt_id "
				+ "Where fr.financial_year_id=?1 And fr.school_id=?2 And sph.fee_receipt=?3 and "
				+ "sph.paid_year=?4 And sph.voucher_head_new_id=?5 And sph.active=true", nativeQuery = true)
		public Integer getAmount(Integer financial_year_id, Integer school_id, String fee_receipt, Integer paid_year,
				Integer vou);
		
		@Query(value = "Select frc.created_username as created_username,sd.father_name as father_name,sd.mobile as mobile,"
				+ "frc.created_date as created_date,frc.created_by as created_by,frc.fee_receipt_id as fee_receipt_id,frc.fee_receipt as fee_receipt,"
				+ "ft.fee_template_name as fee_template_name,ps.program_specialization_name as program_specialization_name,"
				+ "bit.cheque_dd_no as cheque_dd_no,bit.transaction_date as transaction_date,b.bank_name as bank_name,b.bank_short_name as bank_short_name,"
				+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_name as program_name,p.program_short_name as program_short_name,"
				+ "frc.active as active,sd.student_name as student_name,sd.student_id as student_id,sd.auid as auid,sd.usn as usn,ac.ac_year as ac_year,"
				+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year,frc.receipt_type as receipt_type, "
				+ "rs.current_sem as current_sem, rs.current_year as current_year "
				+ "From fee_receipt frc " + "left join student_details sd on frc.student_id = sd.student_id "
				+ "left join fee_template ft on ft.fee_template_id=sd.fee_template_id "
				+ "left join program_specialization ps on ps.program_specialization_id=sd.program_specialization_id "
				+ "left join program p on p.program_id=sd.program_id "
				+ "left join academic_year ac on frc.ac_year_id=ac.ac_year_id "
				+ "left join schools sc on frc.school_id=sc.school_id "
				+ "left join bank_import_transaction bit on frc.bank_transaction_history_id=bit.bank_import_transaction_id "
				+ "left join bank b on b.bank_id=bit.deposited_bank_id "
				+ "left join financial_year fy on frc.financial_year_id=fy.financial_year_id "
				+ "left join reporting_students rs on rs.student_id = sd.student_id "
				+ "Where frc.financial_year_id=?1 And frc.school_id=?2 And frc.fee_receipt=?3 And frc.active=true", nativeQuery = true)
		public List<Map<String, Object>> getDataForDisplayingAndCancelFeeReceipt(Integer financial_year_id,
				Integer school_id, String fee_receipt);
		
		@Query(value = "Select sth.student_fee_payment_history_id as student_fee_payment_history_id,frc.created_username as created_username,sd.father_name as father_name,"
				+ "frc.created_date as created_date,frc.created_by as created_by,frc.fee_receipt_id as fee_receipt_id,frc.fee_receipt as fee_receipt,"
				+ "frc.active as active,sd.student_name as student_name,sd.auid as auid,sd.usn as usn,ac.ac_year as ac_year,"
				+ "sc.school_name_short as school_name_short,fy.financial_year as financial_year,sd.mobile as mobile,"
				+ "ft.fee_template_name as fee_template_name,ps.program_specialization_name as program_specialization_name,"
				+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_name as program_name,p.program_short_name as program_short_name,"
				+ "sth.voucher_head_new_id as voucher_head_new_id,vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,"
				+ "sth.paid_amount as paid_amount,sth.paid_year as paid_year,sth.remarks as remarks,"
				+ "sth.to_pay as to_pay,sth.total_amount as total_amount,"
				+ "sth.transcation_type as transcation_type " + "From student_payment_history sth "
				+ "left join fee_receipt frc on frc.fee_receipt_id=sth.fee_receipt_id "
				+ "left join voucher_head_new vh on vh.voucher_head_new_id=sth.voucher_head_new_id "
				+ "left join student_details sd on frc.student_id = sd.student_id "
				+ "left join fee_template ft on ft.fee_template_id=sd.fee_template_id "
				+ "left join program_specialization ps on ps.program_specialization_id=sd.program_specialization_id "
				+ "left join program p on p.program_id=sd.program_id "
				+ "left join academic_year ac on frc.ac_year_id=ac.ac_year_id "
				+ "left join schools sc on frc.school_id=sc.school_id "
				+ "left join financial_year fy on frc.financial_year_id=fy.financial_year_id "
				+ "Where frc.financial_year_id=?1 And frc.school_id=?2 And frc.fee_receipt=?3 And frc.active=true", nativeQuery = true)
		public List<Map<String, Object>> getDataForDisplayingAndCancelFeeReceipt1(Integer financial_year_id,
				Integer school_id, String fee_receipt);

	@Modifying
	@Transactional
	@Query(value = "Update StudentPaymentHistory sph Set sph.active=false Where sph.fee_receipt_id=?1")
	public void updateStudentPaymentHistory(Integer fee_receipt_id);
	
	@Query(value = "Select max(sph.paid_year) " 
			+ "from student_payment_history sph "
			+ "Left Join fee_receipt fr On fr.fee_receipt_id=sph.fee_receipt_id "
			+ "Where sph.student_id=?1 And sph.financial_year_id=?2 And sph.fee_receipt=?3 And sph.transcation_type=?4  "
			+ "And fr.hostel_status=?5 And sph.active=true group by sph.voucher_head_new_id Order By max(sph.paid_year) Desc Limit 1 ", nativeQuery = true)
	public Integer maxPaidYearOfStudent(Integer student_id, Integer financial_year_id, String fee_receipt1,
			String transaction_type, Integer hostel_status);
	
//	@Query(value = " Select ifNull(sum(sph.paid_amount),0) From student_payment_history sph "
//			+ "Inner join fee_receipt frc On frc.student_id =sph.student_id And frc.financial_year_id =sph.financial_year_id And frc.fee_receipt_id =sph.fee_receipt_id "
//			+ "Where frc.ac_year_id=?1 And sph.fee_template_id=?2 And sph.voucher_head_new_id=?3 And sph.student_id=?4 "
//			+ "And frc.receipt_type='Hostel Fee' And frc.hostel_status = 1 And sph.active=true",nativeQuery=true)
//	public Double hostelPaidAmount(Integer acYearId, Integer hostelFeeTemplateId, Integer feeHeadId,Integer studentId);
	
	@Query(value = " Select ifNull(sum(hfrvh.paying_amount),0) From hostel_fee_receipt_voucher_head_wise hfrvh "
			+ "Inner join fee_receipt frc On frc.student_id =hfrvh.student_id And frc.fee_receipt_id =hfrvh.fee_receipt_id "
			+ "Where frc.ac_year_id=?1 And hfrvh.hostel_fee_template_id=?2 And hfrvh.voucher_head_new_id=?3 And hfrvh.student_id=?4 And hfrvh.active=true",nativeQuery=true)
	public Double hostelPaidAmount(Integer acYearId, Integer hostelFeeTemplateId, Integer feeHeadId,Integer studentId);

	@Query(value="select s from StudentPaymentHistory s where s.bankImportTransactionId=:bankImportTransactionId and s.active = true ")
	public List<StudentPaymentHistory> getStudnentPaymentHistoryByBankImportId(Integer bankImportTransactionId);


}
