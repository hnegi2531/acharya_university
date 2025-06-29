package com.au.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Budget;

@Transactional
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer>{

	@Query(value = "SELECT bu from Budget bu where bu.active=true")
	public List<Budget> getAllPettyCash();
	
	@Modifying
	@Query(value = "update Budget bu set bu.active=false where bu.budget_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update Budget bu set bu.active=true where bu.budget_id=?1")
	public void activate(Integer id);
	
	
	
	@Query(value = "select new map(bu.budget_id as id,"
	        + " bu.emp_id AS emp_id,"
	        + " bu.financial_year_id AS financial_year_id,"
	        + " bu.school_id AS school_id,"
	        + " bu.dept_id AS dept_id,"
	        + " bu.ledger_id AS ledger_id,"
	        + " bu.voucher_head_new_id AS voucher_head_new_id,"
	        + " bu.proposed_amount AS proposed_amount,"
	        + " bu.recommended_amount AS recommended_amount,"
	        + " bu.approved_amount AS approved_amount,"
	        + " bu.remark AS remark,"
	        + " bu.created_by AS created_by,"
	        + " bu.modified_by AS modified_by,"
	        + " bu.created_date AS created_date,"
	        + " bu.modified_date AS modified_date,"
	        + " bu.active AS active,"
	        + " bu.lock_status AS lock_status,"
	        + " bu.lock_date AS lock_date,"
	        + " ed.email AS email,ed.empcode AS empcode,ed.employee_name AS employee_name,"
	        + " fy.financial_year AS financial_year,fy.from_date AS from_date,fy.to_date AS to_date,"
	        + " sc.school_name AS school_name,sc.school_name_short AS school_name_short,"
	        + " de.dept_name AS dept_name,de.dept_name_short AS dept_name_short,"
	        + " vhn.voucher_head AS voucher_head,vhn.voucher_head_short_name AS voucher_head_short_name,vhn.voucher_type AS voucher_type,"
	        + " le.ledger_name AS ledger_name,le.ledger_short_name AS ledger_short_name,"
	        + " bu.created_username AS created_username,"
	        + " bu.modified_username AS modified_username ) "
	        + "from Budget bu "
	        + "left join EmployeeDetails ed on ed.emp_id=bu.emp_id "
	        + "left join FinancialYear fy on fy.financial_year_id=bu.financial_year_id "
	        + "left join Schools sc on sc.school_id=bu.school_id "
	        + "left join Department de on de.dept_id=bu.dept_id "
	        + "left join VoucherHeadNew vhn on vhn.voucher_head_new_id=bu.voucher_head_new_id "
	        + "left join Ledger le on le.ledger_id=bu.ledger_id "
	        + "where CONCAT(IfNull(bu.emp_id,''),'',IfNull(bu.financial_year_id,''),'',IfNull(bu.school_id,''),'',"
	        + "IfNull(bu.dept_id,''),'',IfNull(bu.ledger_id,''),'',IfNull(bu.voucher_head_new_id,''),'',IfNull(bu.budget_id,'')) "
	        + "LIKE %?1% GROUP BY bu.financial_year_id,bu.school_id,bu.dept_id ")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(bu.budget_id as id,"
	        + " bu.emp_id AS emp_id,"
	        + " bu.financial_year_id AS financial_year_id,"
	        + " bu.school_id AS school_id,"
	        + " bu.dept_id AS dept_id,"
	        + " bu.ledger_id AS ledger_id,"
	        + " bu.voucher_head_new_id AS voucher_head_new_id,"
	        + " bu.proposed_amount AS proposed_amount,"
	        + " bu.recommended_amount AS recommended_amount,"
	        + " bu.approved_amount AS approved_amount,"
	        + " bu.remark AS remark,"
	        + " bu.created_by AS created_by,"
	        + " bu.modified_by AS modified_by,"
	        + " bu.created_date AS created_date,"
	        + " bu.modified_date AS modified_date,"
	        + " bu.active AS active,"
	        + " bu.lock_status AS lock_status,"
	        + " bu.lock_date AS lock_date,"
	        + " ed.email AS email,ed.empcode AS empcode,ed.employee_name AS employee_name,"
	        + " fy.financial_year AS financial_year,fy.from_date AS from_date,fy.to_date AS to_date,"
	        + " sc.school_name AS school_name,sc.school_name_short AS school_name_short,"
	        + " de.dept_name AS dept_name,de.dept_name_short AS dept_name_short,"
	        + " vhn.voucher_head AS voucher_head,vhn.voucher_head_short_name AS voucher_head_short_name,vhn.voucher_type AS voucher_type,"
	        + " le.ledger_name AS ledger_name,le.ledger_short_name AS ledger_short_name,"
	        + " bu.created_username AS created_username,"
	        + " bu.modified_username AS modified_username ) "
	        + "from Budget bu "
	        + "left join EmployeeDetails ed on ed.emp_id=bu.emp_id "
	        + "left join FinancialYear fy on fy.financial_year_id=bu.financial_year_id "
	        + "left join Schools sc on sc.school_id=bu.school_id "
	        + "left join Department de on de.dept_id=bu.dept_id "
	        + "left join VoucherHeadNew vhn on vhn.voucher_head_new_id=bu.voucher_head_new_id "
	        + "left join Ledger le on le.ledger_id=bu.ledger_id GROUP BY bu.financial_year_id,bu.school_id,bu.dept_id ")
	public Page<Object> getAllSortedData(Pageable pageable);

	 @Query("SELECT COUNT(b) FROM Budget b WHERE b.school_id = :school_id AND b.dept_id = :dept_id AND b.financial_year_id = :financial_year_id AND b.voucher_head_new_id = :voucher_head_new_id")
  Integer getCount(@Param("school_id") Integer school_id, @Param("dept_id") Integer dept_id, @Param("financial_year_id") Integer financial_year_id, @Param("voucher_head_new_id") Integer voucher_head_new_id);

	 
	 @Query(value = "select new map(bu.budget_id as id,"
		        + " bu.emp_id AS emp_id,"
		        + " bu.financial_year_id AS financial_year_id,"
		        + " bu.school_id AS school_id,"
		        + " bu.dept_id AS dept_id,"
		        + " bu.ledger_id AS ledger_id,"
		        + " bu.voucher_head_new_id AS voucher_head_new_id,"
		        + " bu.proposed_amount AS proposed_amount,"
		        + " bu.recommended_amount AS recommended_amount,"
		        + " bu.approved_amount AS approved_amount,"
		        + " bu.remark AS remark,"
		        + " bu.created_by AS created_by,"
		        + " bu.modified_by AS modified_by,"
		        + " bu.created_date AS created_date,"
		        + " bu.modified_date AS modified_date,"
		        + " bu.active AS active,"
		        + " bu.lock_status AS lock_status,"
		        + " bu.lock_date AS lock_date,"
		        + " ed.email AS email,ed.empcode AS empcode,ed.employee_name AS employee_name,"
		        + " fy.financial_year AS financial_year,fy.from_date AS from_date,fy.to_date AS to_date,"
		        + " sc.school_name AS school_name,sc.school_name_short AS school_name_short,"
		        + " de.dept_name AS dept_name,de.dept_name_short AS dept_name_short,"
		        + " vhn.voucher_head AS voucher_head,vhn.voucher_head_short_name AS voucher_head_short_name,vhn.voucher_type AS voucher_type,"
		        + " le.ledger_name AS ledger_name,le.ledger_short_name AS ledger_short_name,"
		        + " bu.created_username AS created_username,"
		        + " bu.modified_username AS modified_username ) "
		        + "from Budget bu "
		        + "left join EmployeeDetails ed on ed.emp_id=bu.emp_id "
		        + "left join FinancialYear fy on fy.financial_year_id=bu.financial_year_id "
		        + "left join Schools sc on sc.school_id=bu.school_id "
		        + "left join Department de on de.dept_id=bu.dept_id "
		        + "left join VoucherHeadNew vhn on vhn.voucher_head_new_id=bu.voucher_head_new_id "
		        + "left join Ledger le on le.ledger_id=bu.ledger_id "
		        + "where bu.financial_year_id=?1 And bu.school_id=?2 And bu.dept_id=?3 And bu.active=true")
	public List<Map<String, Object>> getAllBudgetDetailsData(Integer financial_year_id, Integer school_id, Integer dept_id);

	 @Query("SELECT b FROM Budget b WHERE b.school_id = :school_id AND b.dept_id = :dept_id AND b.financial_year_id = :financial_year_id AND b.voucher_head_new_id = :voucher_head_new_id")
	    List<Budget> findBudgets(
	            @Param("school_id") Integer school_id,
	            @Param("dept_id") Integer dept_id,
	            @Param("financial_year_id") Integer financial_year_id,
	            @Param("voucher_head_new_id") Integer voucher_head_new_id
	    );

	 @Query("SELECT b.budget_id FROM Budget b WHERE b.school_id = :school_id AND b.dept_id = :dept_id AND b.financial_year_id = :financial_year_id")
	public List<Integer> getBudgetId(Integer school_id, Integer dept_id, Integer financial_year_id);

	 @Query(value = "SELECT bu from Budget bu where bu.budget_id in ?1 And bu.active=true")
	public List<Budget> getbudgetData(List<Integer> budget_id);
}
