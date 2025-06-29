package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.AdvanceMonthlyEmiDeduction;



@Repository
@Transactional
public interface AdvanceMonthlyEmiDeductionRepository extends JpaRepository<AdvanceMonthlyEmiDeduction, Integer>{
   
	@Query(value="select adv from AdvanceMonthlyEmiDeduction adv where adv.active=:active and adv.emp_id=:empId and adv.month=:month and adv.year=:year ",nativeQuery=false)
	public List<AdvanceMonthlyEmiDeduction> getByActiveAndEmp_idAndMonthAndYear(boolean active,Integer empId,String month,String year);

	
	@Query(value ="Select count(*) From advance_monthly_emi_deduction amed Where amed.emp_id=?1 and amed.month=?2 and amed.year=?3 "
			+ "and amed.active=true",nativeQuery=true)
	public int getCount(Integer ps, String month, String year);

	@Modifying
	@Query(value = "update advance_monthly_emi_deduction amed set amed.active=false where amed.advance_id=?1",nativeQuery=true)
	public void deactivateAllEmisByAdvanceId(Integer advance_id); 
	
	@Modifying
	@Query(value = "update advance_monthly_emi_deduction amed set amed.active=true where amed.advance_id=?1",nativeQuery=true)
	public void activateAllEmisByAdvanceId(Integer advance_id); 


}
