package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.DdDetails;

@Transactional
@Repository
public interface DdDetailsRepository extends JpaRepository<DdDetails, Integer> {
	
	@Query(value = "Select dd From DdDetails dd Where dd.active=true")
	public List<DdDetails> getActiveDetails();
	
	@Query(value = "Select new map(dd.dd_id as dd_id, dd.active as active, dd.bank_name as bank_name, dd.cleared_date as cleared_date,"
			+ " dd.cleared_remarks as cleared_remarks, dd.cleared_status as cleared_status, dd.created_date as created_date,"
			+ " dd.created_username as created_username, dd.dd_amount as dd_amount, dd.dd_date as dd_date, dd.dd_number as dd_number,"
			+ " dd.deposited_into as deposited_into, dd.fee_receipt as fee_receipt, dd.financial_year_id as financial_year_id,"
			+ " dd.modified_date as modified_date, dd.modified_username as modified_username, dd.receipt_amount as receipt_amount,"
			+ " dd.receipt_type as receipt_type, dd.remarks as remarks, dd.school_id as school_id, dd.student_id as student_id, sch.school_name_short as school_name_short)"
			+ " From DdDetails dd Left Join Schools sch On dd.school_id=sch.school_id")
	public List<HashMap<String, Object>> getAllDetails();
	
	@Query(value = "Select new map(dd.dd_id as id, dd.active as active, dd.bank_name as bank_name, dd.cleared_date as cleared_date,"
			+ " dd.cleared_remarks as cleared_remarks, dd.cleared_status as cleared_status,dd.created_by as created_by, dd.created_date as created_date,"
			+ " dd.created_username as created_username, dd.dd_amount as dd_amount, dd.dd_date as dd_date, dd.dd_number as dd_number,"
			+ " dd.deposited_into as deposited_into, dd.fee_receipt as fee_receipt, dd.financial_year_id as financial_year_id,"
			+ " dd.modified_date as modified_date, dd.modified_username as modified_username, dd.receipt_amount as receipt_amount,"
			+ " dd.receipt_type as receipt_type, dd.remarks as remarks, dd.school_id as school_id, dd.student_id as student_id, sch.school_name_short as school_name_short, b.bank_name as deposited_bank )"
			+ " From DdDetails dd Left Join Schools sch On dd.school_id=sch.school_id "
			+ " Left Join Bank b on b.bank_id = dd.deposited_into  "
			+ "where CONCAT(IfNull(dd.dd_id,''),'',IfNull(dd.bank_name,''),'',IfNull(dd.cleared_date,''),'',IfNull(dd.cleared_status,''),"
			+ "'',IfNull(dd.dd_amount,''),'',IfNull(dd.dd_date,''),'',IfNull(dd.dd_amount,''),'',IfNull(dd.fee_receipt,''),"
			+ "'',IfNull(dd.financial_year_id,''),'',IfNull(dd.receipt_amount,''),'',IfNull(dd.school_id,''),'',IfNull(dd.student_id,''),"
			+ "'',IfNull(sch.school_name_short,''),'',IfNull(dd.receipt_type,''),'',IfNull(dd.created_by,''),'',IfNull(dd.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "Select new map(dd.dd_id as id, dd.active as active, dd.bank_name as bank_name, dd.cleared_date as cleared_date,"
			+ " dd.cleared_remarks as cleared_remarks, dd.cleared_status as cleared_status,dd.created_by as created_by, dd.created_date as created_date,"
			+ " dd.created_username as created_username, dd.dd_amount as dd_amount, dd.dd_date as dd_date, dd.dd_number as dd_number,"
			+ " dd.deposited_into as deposited_into, dd.fee_receipt as fee_receipt, dd.financial_year_id as financial_year_id,"
			+ " dd.modified_date as modified_date, dd.modified_username as modified_username, dd.receipt_amount as receipt_amount,"
			+ " dd.receipt_type as receipt_type, dd.remarks as remarks, dd.school_id as school_id, dd.student_id as student_id, sch.school_name_short as school_name_short,b.bank_name as deposited_bank )"
			+ " From DdDetails dd Left Join Schools sch On dd.school_id=sch.school_id"
	        + " Left Join Bank b on b.bank_id = dd.deposited_into  ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "Update DdDetails dd Set dd.active = false Where dd.dd_id=?1")
	public void delete1(Integer dd_id);
	
	@Modifying
	@Query(value = "Update DdDetails dd Set dd.active = true Where dd.dd_id=?1")
	public void delete2(Integer dd_id);

}
