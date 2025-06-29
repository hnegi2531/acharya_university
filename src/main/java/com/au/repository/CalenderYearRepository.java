package com.au.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CalenderYear;
import com.au.model.FinancialYear;

@Repository
@Transactional
public interface CalenderYearRepository extends JpaRepository<CalenderYear, Integer>{

	@Query(value = "select g from CalenderYear g where g.active=true")
	public List<CalenderYear> findAll1();
	
	@Modifying
	@Query(value = "update CalenderYear g set g.active=false where g.calender_year_id=?1")
	public void updateCalenderYear(Integer fee_template_id);
	

	@Modifying
	@Query(value = "update CalenderYear g set g.active=true where g.calender_year_id=?1")
	public void updateCalenderYear1(Integer id);
	
	@Query(value = "Select new map(cy.calender_year_id as id,cy.from_date as from_date,cy.to_date as to_date,cy.remarks as remarks,cy.calender_year as calender_year,"
			+ "cy.created_date as created_date,cy.modified_date as modified_date,cy.created_by as created_by,cy.modified_by as modified_by,"
			+ "cy.created_username as created_username,cy.modified_username as modified_username,cy.active as active) From CalenderYear cy "
			+ "Where CONCAT(IfNull(cy.from_date,''),'',IfNull(cy.to_date,''),'',IfNull(cy.calender_year,''),'',IfNull(cy.remarks,''),'',IfNull(cy.created_date,''),'',IfNull(cy.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(cy.calender_year_id as id,cy.from_date as from_date,cy.to_date as to_date,cy.remarks as remarks,cy.calender_year as calender_year,"
			+ "cy.created_date as created_date,cy.modified_date as modified_date,cy.created_by as created_by,cy.modified_by as modified_by,"
			+ "cy.created_username as created_username,cy.modified_username as modified_username,cy.active as active) From CalenderYear cy")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM CalenderYear cy where cy.calender_year=?1 and cy.active=true")
	public Integer countOfCalenderYear(Integer calender_year);
	
	@Query(value = "SELECT cy.calender_year FROM CalenderYear cy where cy.active=true and cy.calender_year > 2024")
	public List<Integer> getCalenderYear();

	@Query(value = "select * from calender_year where ?1 between from_date and to_date",nativeQuery=true)
	public CalenderYear getCalenderYearForRelievingNo(LocalDate date1);
}
