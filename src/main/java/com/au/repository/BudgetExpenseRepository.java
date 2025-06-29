package com.au.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.BudgetExpense;

@Transactional
@Repository
public interface BudgetExpenseRepository extends JpaRepository<BudgetExpense , Integer>{
	
	@Query(value = "Select be From BudgetExpense be Where active=true")
	public List<BudgetExpense> activeBudgetExpenseDetails();
	
//	@Query(value = "Select new map(be.budget_expense_id as budget_expense_id, be.active as active, be.created_date as created_date, "
//			+ "be.created_username as created_username, be.financial_year_id as financial_year_id, be.from_date as from_date, be.remarks as remarks, "
//			+ "be.school_id as school_id, be.to_date as to_date,fy.financial_year as financial_year,sc.school_name_short as school_name_short)"
//			+ "From BudgetExpense be Left join FinancialYear fy On be.financial_year_id=fy.financial_year_id "
//			+ "Left join Schools sc On be.school_id=sc.school_id")
//	public List<HashMap<String , Object>> findAll1();
	
	@Query(value = "Select new map(be.budget_expense_id as id,be.active as active,be.created_date as created_date,be.created_by as created_by,"
			+ "be.created_username as created_username,be.financial_year_id as financial_year_id, be.from_date as from_date,be.remarks as remarks,"
			+ "be.school_id as school_id,be.to_date as to_date,fy.financial_year as financial_year,sc.school_name_short as school_name_short) "
			+ "From BudgetExpense be Left join FinancialYear fy On be.financial_year_id=fy.financial_year_id "
			+ "Left join Schools sc On be.school_id=sc.school_id "
			+ "where CONCAT(IfNull(be.budget_expense_id,''),'',IfNull(be.financial_year_id,''),'',IfNull(be.from_date,''),'',IfNull(be.school_id,''),"
			+ "'',IfNull(sc.school_name_short,''),'',IfNull(fy.financial_year,''),'',IfNull(be.created_by,''),'',IfNull(be.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "Select new map(be.budget_expense_id as id, be.active as active, be.created_date as created_date, "
			+ "be.created_username as created_username, be.financial_year_id as financial_year_id, be.from_date as from_date, be.remarks as remarks, "
			+ "be.school_id as school_id, be.to_date as to_date,fy.financial_year as financial_year,sc.school_name_short as school_name_short) "
			+ "From BudgetExpense be Left join FinancialYear fy On be.financial_year_id=fy.financial_year_id "
			+ "Left join Schools sc On be.school_id=sc.school_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "Update BudgetExpense be Set be.active=true Where be.budget_expense_id=?1")
	public void delete1(Integer budget_expense_id);
	@Modifying
	@Query(value = "Update BudgetExpense be Set be.active=false Where be.budget_expense_id=?1")
	public void delete2(Integer budget_expense_id);
	
	@Query(value = "Select * From budget_expense be where be.school_id=?1 And be.financial_year_id=?2 And be.from_date Between ?3 And ?4 And be.to_date Between ?3 And ?4 And be.active=true",nativeQuery=true)
	public BudgetExpense budgetExpenseBySchoolIdFinancialIdAndBudgetDate(Integer school_id,Integer financial_year_id,Date from_date,Date to_date);
	
	
	@Query(value = "Select * From budget_expense be where be.school_id=?1 And be.financial_year_id=?2 And be.from_date >= ?3  And be.to_date <=?4 And be.active=true",nativeQuery=true)
	public BudgetExpense budgetExpenseBySchoolIdFinancialIdAndBudgetDate1(Integer school_id,Integer financial_year_id,Date from_date,Date to_date);
	
	/*@Query(value = "Select be From BudgetExpense be Where budget_expense_id=?1")
	public BudgetExpense findById1(Integer budget_expense_id);*/
}
