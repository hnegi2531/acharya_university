package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ContraVoucher;
import com.au.model.PaymentVoucher;

@Repository
@Transactional
public interface ContraVoucherRepository extends JpaRepository<ContraVoucher, Integer>{

	@Query(value = "SELECT cv from ContraVoucher cv where cv.active=true")
	public List<ContraVoucher> allActiveContraVoucher();
	
	@Modifying
	@Query(value = "update ContraVoucher cv set cv.active=false where cv.contra_voucher_id=?1")
	public void deactivate(Integer id);
	
	@Modifying
	@Query(value = "update ContraVoucher cv set cv.active=true where cv.contra_voucher_id=?1")
	public void activate(Integer id);
	
	@Query(value = "select * from contra_voucher "
			+ " where active=true And financial_year_id=?1 And school_id=?2 "
			+ "ORDER BY contra_voucher_id Desc LIMIT 1",nativeQuery = true)
	public ContraVoucher getLatestContratVoucher(Integer financial_year_id, Integer school_id);
	
	@Query(value = "SELECT cv.voucher_no from contra_voucher cv "
			+ " where cv.active=true And cv.financial_year_id=?1 And cv.school_id=?2 "
			+ "ORDER BY cv.contra_voucher_id DESC LIMIT 1",nativeQuery=true)
	public Integer getLatestData(Integer financial_year_id, Integer school_id);
	
	@Query(value ="Select new map(cv.contra_voucher_id AS id,cv.school_id AS school_id,cv.bank_id AS bank_id,cv.date_of_deposit AS date_of_deposit,"
			+ " cv.selected_date AS selected_date,cv.net_amount AS net_amount,cv.deposited_amount AS deposited_amount,cv.row_created_date AS row_created_date,"
			+ "cv.balance AS balance,cv.closing_cash AS closing_cash,cv.cash_received AS cash_received,cv.cash_payment AS cash_payment,"
			+ "cv.cash_summary AS cash_summary,cv.remarks AS remarks,cv.financial_year_id AS financial_year_id,cv.voucher_no AS voucher_no,"
			+ "cv.cancel_voucher AS cancel_voucher,cv.voucher_remarks AS voucher_remarks,cv.cancelled_by AS cancelled_by,"
			+ "cv.cancelled_date AS cancelled_date,cv.inter_school_id AS inter_school_id,cv.created_date AS created_date,"
			+ "cv.modified_date AS modified_date,cv.created_by AS created_by,cv.modified_by AS modified_by,"
			+ "s.school_name As school_name,s.school_name_short As school_name_short,cv.total_amount As total_amount,"
			+ "ba.bank_name As bank_name,ba.bank_short_name As bank_short_name,fy.financial_year As financial_year,"
			+ "cv.active AS active,cv.created_username AS created_username,cv.modified_username AS modified_username) From ContraVoucher cv "
			+ "left join Schools s on s.school_id = cv.school_id "
			+ "left join Bank ba on ba.bank_id = cv.bank_id "
			+ "left join FinancialYear fy on fy.financial_year_id = cv.financial_year_id "
			+ "Where CONCAT(IfNull(cv.bank_id,''),'',IfNull(cv.contra_voucher_id,''),'',IfNull(s.school_name_short,''),'',IfNull(cv.created_username,'')) LIKE %?1% "
			+ "group by cv.voucher_no,cv.financial_year_id,cv.school_id ")
	public Page<Object> fetchAllContraVoucher(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(cv.contra_voucher_id AS id,cv.school_id AS school_id,cv.bank_id AS bank_id,cv.date_of_deposit AS date_of_deposit,"
			+ " cv.selected_date AS selected_date,cv.net_amount AS net_amount,cv.deposited_amount AS deposited_amount,cv.row_created_date AS row_created_date,"
			+ "cv.balance AS balance,cv.closing_cash AS closing_cash,cv.cash_received AS cash_received,cv.cash_payment AS cash_payment,"
			+ "cv.cash_summary AS cash_summary,cv.remarks AS remarks,cv.financial_year_id AS financial_year_id,cv.voucher_no AS voucher_no,"
			+ "cv.cancel_voucher AS cancel_voucher,cv.voucher_remarks AS voucher_remarks,cv.cancelled_by AS cancelled_by,"
			+ "cv.cancelled_date AS cancelled_date,cv.inter_school_id AS inter_school_id,cv.created_date AS created_date,"
			+ "cv.modified_date AS modified_date,cv.created_by AS created_by,cv.modified_by AS modified_by,"
			+ "s.school_name As school_name,s.school_name_short As school_name_short,cv.total_amount As total_amount,"
			+ "ba.bank_name As bank_name,ba.bank_short_name As bank_short_name,fy.financial_year As financial_year,"
			+ "cv.active AS active,cv.created_username AS created_username,cv.modified_username AS modified_username) From ContraVoucher cv "
			+ "left join Schools s on s.school_id = cv.school_id "
			+ "left join Bank ba on ba.bank_id = cv.bank_id "
			+ "left join FinancialYear fy on fy.financial_year_id = cv.financial_year_id "
			+ "group by cv.voucher_no,cv.financial_year_id,cv.school_id ")
	public Page<Object> fetchAllContraVoucherWoKeyword(Pageable pageable);

	@Query(value = "select COALESCE(SUM(cv.deposited_amount), 0) as totalBalance FROM contra_voucher cv "
			+ "where DATE(cv.selected_date) = DATE(?1) And cv.active=true",nativeQuery = true)
	public Double getBalance(String selected_date);

	@Query(value = "select cv.inter_school_id As school_id, COALESCE(SUM(cv.deposited_amount), 0) as deposited_amount,"
			+ "sc.school_name_short As school_name_short from contra_voucher cv "
			+ "left join schools sc on sc.school_id = cv.inter_school_id "
			+ "where cv.active = true and DATE(cv.selected_date) = DATE(?1) "
			+ "group by cv.inter_school_id", nativeQuery = true)
	public List<Map<String, Object>> getContraAmountGroupBy(String selected_date);

	@Query(value ="Select new map(cv.contra_voucher_id AS id,cv.school_id AS school_id,cv.bank_id AS bank_id,cv.date_of_deposit AS date_of_deposit,"
			+ " cv.selected_date AS selected_date,cv.net_amount AS net_amount,cv.deposited_amount AS deposited_amount,cv.row_created_date AS row_created_date,"
			+ "cv.balance AS balance,cv.closing_cash AS closing_cash,cv.cash_received AS cash_received,cv.cash_payment AS cash_payment,"
			+ "cv.cash_summary AS cash_summary,cv.remarks AS remarks,cv.financial_year_id AS financial_year_id,cv.voucher_no AS voucher_no,"
			+ "cv.cancel_voucher AS cancel_voucher,cv.voucher_remarks AS voucher_remarks,cv.cancelled_by AS cancelled_by,"
			+ "cv.cancelled_date AS cancelled_date,cv.inter_school_id AS inter_school_id,cv.created_date AS created_date,"
			+ "cv.modified_date AS modified_date,cv.created_by AS created_by,cv.modified_by AS modified_by,"
			+ "s.school_name As school_name,s.school_name_short As school_name_short,cv.total_amount As total_amount,"
			+ "ba.bank_name As bank_name,ba.bank_short_name As bank_short_name,fy.financial_year As financial_year,"
			+ "cv.active AS active,cv.created_username AS created_username,cv.modified_username AS modified_username) From ContraVoucher cv "
			+ "left join Schools s on s.school_id = cv.school_id "
			+ "left join Bank ba on ba.bank_id = cv.bank_id "
			+ "left join FinancialYear fy on fy.financial_year_id = cv.financial_year_id "
			+ "where cv.voucher_no=?1 And cv.school_id=?2 And cv.financial_year_id=?3 And cv.active=true")
	public List<Map<String, Object>> getContraVoucherData(Integer voucher_no, Integer school_id,
			Integer financial_year_id);
}
