package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CancelledFeeReceipts;

import java.time.LocalDate;

@Repository
@Transactional
public interface CancelledFeeReceiptsRepository extends JpaRepository<CancelledFeeReceipts, Integer> {
	
	@Query(value = "Select new map(tal_rec.cancelled_fee_receipt_id As id,tal_rec.bulk_fee_receipt_id As bulk_fee_receipt_id, tal_rec.bulk_fee_receipt As bulk_fee_receipt,"
			+ " tal_rec.fee_receipt_id As fee_receipt_id , tal_rec.amount_in_som As amount_in_som, tal_rec.fee_receipt as fee_receipt,"
			+ "tal_rec.created_date As created_date,tal_rec.created_username As created_username,tal_rec.created_by As created_by,"
			+ "tal_rec.remarks as remarks,tal_rec.financial_year_id As financial_year_id,tal_rec.fee_template as fee_template, tal_rec.cheque_dd_no as cheque_dd_no,tal_rec.bank_name as bank_name,"
			+ "tal_rec.modified_by As modified_by, tal_rec.modified_date As modified_date, tal_rec.received_in As received_in,fr.receipt_type as receipt_type,"
			+ "tal_rec.amount_in_som As amount_in_som, tal_rec.amount As amount,fy.financial_year as financial_year,sc.school_name_short as school_name_short,"
			+ "sc.school_name as school_name,sd.student_name as student_name,sd.auid as auid,ed.employee_name as employee_name, b.transaction_date as transaction_date ) "
			+ "From CancelledFeeReceipts tal_rec "
			+ "left join FeeReceipt fr on fr.fee_receipt_id=tal_rec.fee_receipt_id "
			+ "left join FinancialYear fy on fy.financial_year_id=tal_rec.financial_year_id "
			+ "left join Schools sc on sc.school_id=tal_rec.school_id "
			+ "left join Student_Details sd on sd.student_id=tal_rec.student_id "
			+ "left join EmployeeDetails ed on ed.emp_id=tal_rec.created_by "
			+ "left join BankImportTransaction b on b.bank_import_transaction_id = fr.bank_transaction_history_id "
			+ "Where CONCAT(IfNull(tal_rec.cancelled_fee_receipt_id,''),'',IfNull(tal_rec.bulk_fee_receipt_id,''),'',IfNull(tal_rec.created_date,''),'',"
			+ "IfNull(tal_rec.created_username,''),'',IfNull(tal_rec.bulk_fee_receipt,''),'',IfNull(tal_rec.fee_receipt_id,''),'',IfNull(tal_rec.amount_in_som,''),'',"
			+ "IfNull(tal_rec.fee_receipt,''),'',IfNull(tal_rec.remarks,''),'',IfNull(tal_rec.received_in,'')) LIKE %:keyword% "
			+ "AND (:start IS NULL OR CAST(tal_rec.created_date AS LocalDate) >= :start) "
			+ "AND (:end IS NULL OR CAST(tal_rec.created_date AS LocalDate) <= :end) "
			+ "AND (:school_id IS NULL OR tal_rec.school_id = :school_id) "
			+ "ORDER BY tal_rec.created_date DESC")
	public Page<Object> getAllCancelledReceipt(Pageable pageable, Object keyword, Integer school_id, LocalDate start, LocalDate end);
	
	
	@Query(value = "Select new map(tal_rec.cancelled_fee_receipt_id As id,tal_rec.bulk_fee_receipt_id As bulk_fee_receipt_id,tal_rec.bulk_fee_receipt As bulk_fee_receipt,"
			+ "tal_rec.fee_receipt_id As fee_receipt_id,tal_rec.amount_in_som As amount_in_som,tal_rec.fee_receipt as fee_receipt,"
			+ "tal_rec.created_date As created_date,tal_rec.created_username As created_username,tal_rec.created_by As created_by,fr.receipt_type as receipt_type,"
			+ "tal_rec.remarks as remarks,tal_rec.financial_year_id As financial_year_id,tal_rec.fee_template as fee_template, tal_rec.cheque_dd_no as cheque_dd_no,tal_rec.bank_name as bank_name,"
			+ "tal_rec.modified_by As modified_by, tal_rec.modified_date As modified_date, tal_rec.received_in As received_in,"
			+ "tal_rec.amount_in_som As amount_in_som,tal_rec.amount As amount,fy.financial_year as financial_year,sc.school_name_short as school_name_short,"
			+ "sc.school_name as school_name,sd.student_name as student_name,sd.auid as auid,ed.employee_name as employee_name,b.transaction_date as transaction_date ) "
			+ "From CancelledFeeReceipts tal_rec "
			+ "left join FeeReceipt fr on fr.fee_receipt_id=tal_rec.fee_receipt_id "
			+ "left join FinancialYear fy on fy.financial_year_id=tal_rec.financial_year_id "
			+ "left join Schools sc on sc.school_id=tal_rec.school_id "
			+ "left join Student_Details sd on sd.student_id=tal_rec.student_id "
			+ "left join EmployeeDetails ed on ed.emp_id=tal_rec.created_by "
			+ "left join BankImportTransaction b on b.bank_import_transaction_id = fr.bank_transaction_history_id "
			+ "WHERE (:start IS NULL OR CAST(tal_rec.created_date AS LocalDate) >= :start) "
			+ "AND (:end IS NULL OR CAST(tal_rec.created_date AS LocalDate) <= :end) "
			+ "AND (:school_id IS NULL OR tal_rec.school_id = :school_id) "
			+ "ORDER BY tal_rec.created_date DESC")
	public Page<Object> getAllCancelledReceipt2(Pageable pageable, Integer school_id, LocalDate start, LocalDate end);

}
