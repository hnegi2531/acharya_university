package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.BankAssignment;

@Transactional
@Repository
public interface BankAssignmentRepository extends JpaRepository<BankAssignment, Integer> {
	
	@Query(value = "select h from BankAssignment h where h.active=true")
	public List<BankAssignment> findAll1();

	@Modifying
	@Query(value = "update BankAssignment h set h.active=false where h.bank_assignment_id=?1")
	public void updateBank(Integer id);

	@Modifying
	@Query(value = "update BankAssignment h set h.active=true where h.bank_assignment_id=?1")
	public void updateBank1(Integer id);
	
	@Query(value = "Select new map(ba.bank_assignment_id as id,ba.acc_name as acc_name,ba.acc_number as acc_number,"
			+ "ba.ifsc_code as ifsc_code,ba.swift_code as swift_code,ba.bank_branch_name as bank_branch_name,"
			+ "ba.created_date as created_date,ba.modified_date as modified_date,ba.active as active,ba.internal_status as internal_status,"
			+ "ba.created_username as created_username,ba.modified_username as modified_username,ba.created_by as created_by,"
			+ "ba.modified_by as modified_by,ba.school_id as school_id,ba.opening_balance as opening_balance,"
			+ "ba.bank_balance as bank_balance,ba.bank_id as bank_id,vh.is_common as is_common,vh.is_salaries as is_salaries,"
			+ "ba.bank_balance_modified_by as bank_balance_modified_by,ba.bank_balance_modified_date as bank_balance_modified_date,"
			+ "vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,vh.hostel_status as hostel_status,"
			+ "vh.is_vendor as is_vendor,vh.budget_head as budget_head,vh.cash_or_bank as cash_or_bank,sch.school_name as school_name,"
			+ "ba.ledger_id as ledger_id,sch.school_name_short as school_name_short,le.ledger_short_name as ledger_short_name) From BankAssignment ba "
			+ "Left Join Schools sch On sch.school_id = ba.school_id "
			+ "Left Join Ledger le On le.ledger_id = ba.ledger_id "
			+ "Left Join VoucherHeadNew vh On vh.voucher_head_new_id = ba.bank_id "
			+ "Where CONCAT(IfNull(ba.bank_assignment_id,''),'',IfNull(ba.acc_name,''),'',IfNull(ba.acc_number,''),'',IfNull(ba.ifsc_code,''),'',"
			+ "IfNull(ba.swift_code,''),'',IfNull(ba.bank_branch_name,''),'',IfNull(ba.created_date,''),'',IfNull(ba.created_username,''),'',"
			+ "IfNull(ba.opening_balance,''),'',IfNull(ba.bank_balance,''),'',IfNull(ba.bank_balance_modified_by,''),'',"
			+ "IfNull(ba.bank_balance_modified_date,''),'',IfNull(vh.voucher_head_short_name,''),'',IfNull(sch.school_name_short,''),'',IfNull(le.ledger_short_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	
	@Query(value = "Select new map(ba.bank_assignment_id as id,ba.acc_name as acc_name,ba.acc_number as acc_number,"
			+ "ba.ifsc_code as ifsc_code,ba.swift_code as swift_code,ba.bank_branch_name as bank_branch_name,"
			+ "ba.created_date as created_date,ba.modified_date as modified_date,ba.active as active,ba.internal_status as internal_status,"
			+ "ba.created_username as created_username,ba.modified_username as modified_username,ba.created_by as created_by,"
			+ "ba.modified_by as modified_by,ba.school_id as school_id,ba.opening_balance as opening_balance,"
			+ "ba.bank_balance as bank_balance,ba.bank_id as bank_id,vh.is_common as is_common,vh.is_salaries as is_salaries,"
			+ "ba.bank_balance_modified_by as bank_balance_modified_by,ba.bank_balance_modified_date as bank_balance_modified_date,"
			+ "vh.voucher_head as voucher_head,vh.voucher_head_short_name as voucher_head_short_name,vh.hostel_status as hostel_status,"
			+ "vh.is_vendor as is_vendor,vh.budget_head as budget_head,vh.cash_or_bank as cash_or_bank,sch.school_name as school_name,"
			+ "ba.ledger_id as ledger_id,sch.school_name_short as school_name_short,le.ledger_short_name as ledger_short_name) From BankAssignment ba "
			+ "Left Join Schools sch On sch.school_id = ba.school_id "
			+ "Left Join Ledger le On le.ledger_id = ba.ledger_id "
			+ "Left Join VoucherHeadNew vh On vh.voucher_head_new_id = ba.bank_id ")
	public Page<Object> findAll3(Pageable pageable);

	@Query(value = "SELECT count(*) From bank_assignment ba where ba.bank_id=?1 and ba.school_id=?2 and ba.active=true",nativeQuery=true)
	public Integer countOfBankAndSchool(Integer bank_id, Integer school_id);
	

}
