package com.au.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FinancialYear;

@Repository
@Transactional
public interface FinancialYearRepository extends JpaRepository<FinancialYear, Integer>{

	@Query(value = "select g from FinancialYear g where g.active=true")
	public List<FinancialYear> findAll1();
	
	@Modifying
	@Query(value = "update FinancialYear g set g.active=false where g.financial_year_id=?1")
	public void updateFinancialYear(Integer fee_template_id);
	

	@Modifying
	@Query(value = "update FinancialYear g set g.active=true where g.financial_year_id=?1")
	public void updateFinancialYear1(Integer id);
	
	@Query(value = "Select * From financial_year where active = true and curdate() between from_date and to_date ", nativeQuery = true)
	public FinancialYear getFinancialYearIdOnCurrentYear();
	
	@Query(value = "Select new map(fy.financial_year_id as id,fy.financial_year as financial_year,fy.year as year,"
			+ "fy.from_date as from_date,fy.to_date as to_date,fy.created_date as created_date,"
			+ "fy.modified_date as modified_date,fy.created_by as created_by,fy.modified_by as modified_by,"
			+ "fy.created_username as created_username,fy.modified_username as modified_username,fy.active as active) From FinancialYear fy "
			+ "Where CONCAT(IfNull(fy.financial_year_id,''),'',IfNull(fy.financial_year,''),'',IfNull(fy.from_date,''),'',"
			+ "IfNull(fy.to_date,''),'',IfNull(fy.created_by,''),'',IfNull(fy.created_username,''),'',IfNull(fy.created_date,''),'',IfNull(fy.year,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(fy.financial_year_id as id,fy.financial_year as financial_year,fy.year as year,"
			+ "fy.from_date as from_date,fy.to_date as to_date,fy.created_date as created_date,"
			+ "fy.modified_date as modified_date,fy.created_by as created_by,fy.modified_by as modified_by,"
			+ "fy.created_username as created_username,fy.modified_username as modified_username,fy.active as active) From FinancialYear fy")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM FinancialYear fy where fy.financial_year=?1 and fy.active=true")
	public Integer countOfFinancialYear(String financial_year);
	
	@Query(value = "SELECT count(*) FROM FinancialYear fy where fy.year=?1 and fy.active=true")
	public Integer countOfYear(Integer year);

	@Query(value = "select * from financial_year where ?1 between from_date and to_date",nativeQuery=true)
	public FinancialYear getFinancialYearDataForIndent(LocalDate date1);
	
	@Query(value = "SELECT * FROM financial_year " +
			"WHERE ?1 BETWEEN DATE(from_date) AND DATE(to_date)",nativeQuery=true)
	public FinancialYear getFinancialYearData(Date date1);

	@Query(value = "select * from financial_year where financial_year_id = ?1 and active = true ", nativeQuery = true)
	FinancialYear getFinancialYearByFinancialYearId(Integer fcYearId);

	@Query(value = "select financial_year_id from acharya_erp.financial_year where :now between from_date and to_date", nativeQuery = true)
	Integer getFinancialYearIdOnCurrentDate(LocalDate now);
}
