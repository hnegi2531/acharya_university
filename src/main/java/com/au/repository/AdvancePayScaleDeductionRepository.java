package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.AdvancePayScaleDeduction;


@Transactional
@Repository
public interface AdvancePayScaleDeductionRepository extends JpaRepository<AdvancePayScaleDeduction,Integer>{

	@Query(value = "select new map(apsd.advance_id as id,apsd.loan_end_date as loan_end_date,"
			+ "apsd.principal_amount as principal_amount,apsd.loan_started_date as loan_started_date,apsd.tenure as tenure,"
			+ "apsd.emi_amount as emi_amount,apsd.emp_id as emp_id,apsd.lic_number As lic_number,"
			+ "apsd.loan_created_date as loan_created_date,month(apsd.created_date) as month,year(apsd.created_date) as year,"
			+ "apsd.loan_completed_date as loan_completed_date,apsd.completed_tenture as completed_tenture,"
			+ "apsd.current_tenture as current_tenture,apsd.category_name as category_name,apsd.school_id as school_id,"
			+ "apsd.deactivate_year as deactivate_year,apsd.deactivate_month as deactivate_month,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,ed.email as email,"
			+ "sc.school_name_short as school_name_short,sc.school_name as school_name,"
			+ "apsd.created_username as created_username,apsd.modified_username as modified_username,"
			+ "apsd.created_date as created_date,apsd.modified_date as modified_date,apsd.created_by as created_by,"
			+ "apsd.modified_by as modified_by,apsd.active as active) from AdvancePayScaleDeduction apsd "
			+ "left join Schools sc on sc.school_id=apsd.school_id "
			+ "left join EmployeeDetails ed on ed.emp_id=apsd.emp_id "
			+ "where CONCAT(IfNull(apsd.created_username,''),'',IfNull(apsd.emp_id,''),"
			+ "'',IfNull(apsd.created_by,''),'',IfNull(apsd.created_date,'',IfNull(apsd.loan_end_date,''),'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);	
	
	@Query(value = "select new map(apsd.advance_id as id,apsd.loan_end_date as loan_end_date,"
			+ "apsd.principal_amount as principal_amount,apsd.loan_started_date as loan_started_date,apsd.tenure as tenure,"
			+ "apsd.emi_amount as emi_amount,apsd.emp_id as emp_id,apsd.lic_number As lic_number,"
			+ "apsd.loan_created_date as loan_created_date,month(apsd.created_date) as month,year(apsd.created_date) as year,"
			+ "apsd.loan_completed_date as loan_completed_date,apsd.completed_tenture as completed_tenture,"
			+ "apsd.current_tenture as current_tenture,apsd.category_name as category_name,apsd.school_id as school_id,"
			+ "apsd.deactivate_year as deactivate_year,apsd.deactivate_month as deactivate_month,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,ed.email as email,"
			+ "sc.school_name_short as school_name_short,sc.school_name as school_name,"
			+ "apsd.created_username as created_username,apsd.modified_username as modified_username,"
			+ "apsd.created_date as created_date,apsd.modified_date as modified_date,apsd.created_by as created_by,"
			+ "apsd.modified_by as modified_by,apsd.active as active) from AdvancePayScaleDeduction apsd "
			+ "left join Schools sc on sc.school_id=apsd.school_id "
			+ "left join EmployeeDetails ed on ed.emp_id=apsd.emp_id")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update AdvancePayScaleDeduction apsd set apsd.active=false where apsd.advance_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update AdvancePayScaleDeduction apsd set apsd.active=true where apsd.advance_id=?1")
	public void activate(Integer id);

		@Query(value = "select amed.emi_id as id,apsd.loan_end_date as loan_end_date,amed.advance_id as advance_id,"
			+ "apsd.principal_amount as principal_amount,apsd.loan_started_date as loan_started_date,apsd.tenure as tenure,"
			+ "amed.emi_amount as emi_amount,amed.remaining_balance as remaining_balance,amed.category_name as category_name,"
			+ "amed.emp_id as emp_id,amed.month as month,amed.year as year,Concat(IfNull(amed.month,''),'-',IfNull(amed.year,'')) as month_year,"
			+ "apsd.loan_created_date as loan_created_date,apsd.lic_number As lic_number,"
			+ "apsd.loan_completed_date as loan_completed_date,apsd.completed_tenture as completed_tenture,"
			+ "apsd.current_tenture as current_tenture,apsd.school_id as school_id,"
			+ "apsd.deactivate_year as deactivate_year,apsd.deactivate_month as deactivate_month,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,ed.email as email,"
			+ "sc.school_name_short as school_name_short,sc.school_name as school_name,"
			+ "apsd.created_username as created_username,apsd.modified_username as modified_username,"
			+ "apsd.created_date as created_date,apsd.modified_date as modified_date,apsd.created_by as created_by,"
			+ "apsd.modified_by as modified_by,amed.active as active "
			+ "from advance_monthly_emi_deduction amed "
			+ "left join advance_payscale_deduction apsd on apsd.advance_id=amed.advance_id "
			+ "left join schools sc on sc.school_id=apsd.school_id "
			+ "left join employee_details ed on ed.emp_id=apsd.emp_id "
			+ "where amed.advance_id=?1 and amed.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchTimeTableDetailsByEmployeeId(Integer advance_id);

		@Modifying
		@Query(value = "update advance_monthly_emi_deduction amed set amed.emi_amount=0 where amed.emi_id=?1",nativeQuery=true)
		public void updateEmi(Integer emi_id);
		
		@Query(value = "SELECT amed.emi_id from advance_monthly_emi_deduction amed where amed.advance_id=?2 and amed.emi_id>=?1 and amed.active=true",nativeQuery=true)
		public List<Integer> getListOfEmiIds(Integer emi_id, Integer advance_id);
		
		@Query(value = "SELECT remaining_balance from advance_monthly_emi_deduction amed where amed.emi_id=?1 and amed.active=true",nativeQuery=true)
		public Double getRemainingBalance(Integer l);
		
		@Modifying
		@Query(value = "update advance_monthly_emi_deduction amed set amed.remaining_balance=?2 where amed.emi_id=?1 and amed.active=true",nativeQuery=true)
		public void updateRemainingBalance(Integer emi_id, Double remaining_balance);

}
