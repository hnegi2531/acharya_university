package com.au.repository;

import java.util.Date;
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
import com.au.model.Bank;

@Transactional
@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {

		@Query(value = "select h from Bank h where h.active=true")
	public List<Bank> findAll1();

	@Modifying
	@Query(value = "update Bank h set h.active=false where h.bank_id=?1")
	public void updateBank(Integer id);

	@Modifying
	@Query(value = "update Bank h set h.active=true where h.bank_id=?1")
	public void updateBank1(Integer id);
	
	@Query(value ="Select new map(ba.bank_id as id,ba.bank_name as bank_name,ba.created_by as created_by,ba.bank_balance as bank_balance,"
			+ "ba.modified_by as modified_by,ba.bank_short_name as bank_short_name,ba.created_date as created_date,"
			+ "ba.modified_date as modified_date,ba.active as active,ba.bank_branch_name as bank_branch_name,ba.account_name as account_name,"
			+ "ba.account_number as account_number,ba.opening_balance as opening_balance,ba.swift_code as swift_code,"
			+ "ba.school_id as school_id,s.school_name as school_name,s.school_name_short as school_name_short,"
			+ "ba.voucher_head_new_id as voucher_head_new_id,ba.ifsc_code as ifsc_code, ba.bank_group_id as bank_group_id,"
			+ "vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,"
			+ "ba.created_username as created_username,ba.modified_username as modified_username, ba.balanceUpdatedBy as balanceUpdatedBy, "
			+ "ba.balanceUpdatedOn as balanceUpdatedOn,ba.previousUpdatedOn as previousUpdatedOn) From Bank ba "
			+ "left join Schools s on s.school_id = ba.school_id "
			+ "left join VoucherHeadNew vh on vh.voucher_head_new_id = ba.voucher_head_new_id "
			+ "Where CONCAT(IfNull(ba.bank_id,''),'',IfNull(ba.bank_short_name,''),'',IfNull(ba.created_date,''),'',IfNull(ba.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(ba.bank_id as id,ba.bank_name as bank_name,ba.created_by as created_by,ba.bank_balance as bank_balance,"
			+ "ba.modified_by as modified_by,ba.bank_short_name as bank_short_name,ba.created_date as created_date,"
			+ "ba.modified_date as modified_date,ba.active as active,ba.bank_branch_name as bank_branch_name,ba.account_name as account_name,"
			+ "ba.account_number as account_number,ba.opening_balance as opening_balance,ba.swift_code as swift_code,"
			+ "ba.school_id as school_id,s.school_name as school_name,s.school_name_short as school_name_short,"
			+ "ba.voucher_head_new_id as voucher_head_new_id,ba.ifsc_code as ifsc_code, ba.bank_group_id as bank_group_id,"
			+ "vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,"
			+ "ba.created_username as created_username,ba.modified_username as modified_username,ba.balanceUpdatedBy as balanceUpdatedBy,"
			+ "ba.balanceUpdatedOn as balanceUpdatedOn,ba.previousUpdatedOn as previousUpdatedOn) From Bank ba "
			+ "left join Schools s on s.school_id = ba.school_id "
			+ "left join VoucherHeadNew vh on vh.voucher_head_new_id = ba.voucher_head_new_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value="SELECT COUNT(*) FROM bank b where b.bank_name=?1 and b.account_number=?2 and b.active=true",nativeQuery = true)
	public Integer getCountOfBankName(String bank_name,String account_number);
	
	@Query(value="SELECT COUNT(*) FROM bank b where  b.account_number=?1 ",nativeQuery = true)
	public Integer getCountOfBankAccount(String account_number);
	
	
	@Query(value="SELECT COUNT(*) FROM bank b where b.bank_short_name=?1 and b.account_number=?2 and  b.active=true",nativeQuery = true)
	public Integer getCountOfBankShortName(String bank_short_name,String account_number);

	@Query(value="SELECT b.opening_balance FROM bank b where b.bank_id=?1 and b.active=true",nativeQuery = true)
	public Double getOutstandingBalance(Integer bank_id);
	
	@Query(value="SELECT b.bank_balance FROM bank b where b.bank_id=?1 and b.active=true",nativeQuery = true)
	public Double getBankBalance(Integer bank_id);

	@Query(value="select sum(debit) FROM payment_voucher where bank_id =:bank_id and financial_year_id =:financial_year_id "
			+ "and active=true",nativeQuery = true)
	public Double getBanksTotalDebit(Integer bank_id, Integer financial_year_id);

	@Query(value="SELECT sum(paid_amount) FROM fee_receipt where bank_id=:bank_id and financial_year_id =:financial_year_id and active=true",nativeQuery = true)
	public Double getBanksTotalCredit(Integer bank_id, Integer financial_year_id);

	@Query(value="select sum(debit) FROM payment_voucher where bank_id =:bank_id and financial_year_id =:financial_year_id and month(date)=:i "
			+ "and active=true",nativeQuery = true)
	public Double getTotalDebitOfMonth(Integer bank_id, Integer financial_year_id, Integer i);

	@Query(value="SELECT sum(paid_amount) FROM fee_receipt where bank_id=:bank_id and financial_year_id =:financial_year_id and "
			+ "month(created_date)= :j and active=true",nativeQuery = true)
	public Double getTotalCreditOfMonth(Integer bank_id, Integer financial_year_id, Integer j);
	


	@Query(value="select sum(debit) FROM payment_voucher where bank_id =:bank_id and financial_year_id =:financial_year_id and date(date)=date(:day) "
			+ "and active=true ",nativeQuery = true)
	public Double getTotalDebitOfDay(Integer bank_id, Integer financial_year_id, Date day);

	@Query(value="SELECT sum(paid_amount) FROM fee_receipt where bank_id=:bank_id and financial_year_id =:financial_year_id and "
			+ "date(created_date)= date(:day) and active=true",nativeQuery = true)
	public Double getTotalCreditOfDay(Integer bank_id, Integer financial_year_id, Date day);
	
	
	@Query(value = "select ( (SELECT round(COALESCE(SUM(debit), 0),2) FROM payment_voucher where voucher_head_id=?1 and financial_year_id=?2 and school_id=?3 and month(date)=?4 and active=true) + "
			+ "(SELECT round(COALESCE(SUM(debit), 0),2) FROM journal_voucher where voucher_head_id=?1 and financial_year_id=?2 and school_id=?3 and month(date)=?4 and active=true))  as debit_amount",nativeQuery=true)
	public Double getTotalDebitOfMonthVoucherHeadWise(Integer voucher_head_id, Integer financial_year_id,Integer school_id, Integer i);
	
	@Query(value = "select ( (SELECT round(COALESCE(SUM(amount), 0),2) FROM bulk_fee_receipt where voucher_head_new_id=?1 and financial_year_id=?2 and school_id=?3 and month(created_date)=?4 and active=true) + "
			+ "(SELECT round(COALESCE(SUM(paid_amount), 0),2) FROM student_payment_history where voucher_head_new_id=?1 and financial_year_id=?2 and school_id=?3 and month(created_date)=?4 and active=true))  as debit_amount",nativeQuery=true)
	public Double getTotalCreditOfMonthVoucherHeadWise(Integer voucher_head_new_id, Integer financial_year_id,Integer school_id, Integer j);	
	
	
	@Query(value = " select ( (SELECT round(COALESCE(SUM(debit), 0),2) FROM payment_voucher where voucher_head_id=?1 and financial_year_id=?2 and school_id=?3 "
			+ " and date(date)=date(?4) and active=true) + "
			+ "(SELECT round(COALESCE(SUM(debit), 0),2) FROM journal_voucher where voucher_head_id=?1 and financial_year_id=?2 and school_id=?3 "
			+ " and date(date)=date(?4) and active=true))  as debit_amount",nativeQuery=true)
	public Double getTotalDebitOfDayVoucherHeadWise(Integer voucher_head_id,Integer school_id, Integer financial_year_id, Date d);

	@Query(value = "select ( (SELECT round(COALESCE(SUM(amount), 0),2) FROM bulk_fee_receipt where voucher_head_new_id=?1 and financial_year_id=?2 and school_id=?3 "
			+ "and date(created_date)=date(?4) and active=true) + "
			+ "(SELECT round(COALESCE(SUM(paid_amount), 0),2) FROM student_payment_history where voucher_head_new_id=?1 and financial_year_id=?2 and school_id=?3 "
			+ "and date(created_date)=date(?4) and active=true))  as debit_amount",nativeQuery=true)
	public Double getTotalCreditOfDayVoucherHeadWise(Integer voucher_head_new_id, Integer school_id, Integer financial_year_id, Date d);

	
	@Query(value ="Select new map(ba.bank_id as id,ba.bank_name as bank_name,ba.created_by as created_by,ba.bank_balance as bank_balance,"
			+ "ba.modified_by as modified_by,ba.bank_short_name as bank_short_name,ba.created_date as created_date,"
			+ "ba.modified_date as modified_date,ba.active as active,ba.bank_branch_name as bank_branch_name,ba.account_name as account_name,"
			+ "ba.account_number as account_number,ba.opening_balance as opening_balance,ba.swift_code as swift_code,"
			+ "ba.school_id as school_id,s.school_name as school_name,s.school_name_short as school_name_short,"
			+ "ba.voucher_head_new_id as voucher_head_new_id,ba.ifsc_code as ifsc_code, ba.bank_group_id as bank_group_id,"
			+ "vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,"
			+ "ba.created_username as created_username,ba.modified_username as modified_username) From Bank ba "
			+ "left join Schools s on s.school_id = ba.school_id "
			+ "left join VoucherHeadNew vh on vh.voucher_head_new_id = ba.voucher_head_new_id where ba.school_id=?1 And ba.active=true")
	public List<Map<String, Object>> bankDetailsBasedOnSchoolId(Integer school_id);
	
	
	@Query(value ="Select new map(ba.bank_id as id,ba.bank_name as bank_name,ba.created_by as created_by,ba.bank_balance as bank_balance,"
			+ "ba.modified_by as modified_by,ba.bank_short_name as bank_short_name,ba.created_date as created_date,"
			+ "ba.modified_date as modified_date,ba.active as active,ba.bank_branch_name as bank_branch_name,ba.account_name as account_name,"
			+ "ba.account_number as account_number,ba.opening_balance as opening_balance,ba.swift_code as swift_code,"
			+ "ba.school_id as school_id,s.school_name as school_name,s.school_name_short as school_name_short,"
			+ "ba.voucher_head_new_id as voucher_head_new_id,ba.ifsc_code as ifsc_code, ba.bank_group_id as bank_group_id,"
			+ "vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,"
			+ "ba.created_username as created_username,ba.modified_username as modified_username) From Bank ba "
			+ "left join Schools s on s.school_id = ba.school_id "
			+ "left join VoucherHeadNew vh on vh.voucher_head_new_id = ba.voucher_head_new_id where ba.active=true")
	public List<Map<String, Object>> getAllbankDetailsData();


	@Query(value = "SELECT b FROM Bank b WHERE b.bank_id = :bankId and b.active = true ")
	Bank findByBankID(@Param("bankId") Integer bankId);

}
