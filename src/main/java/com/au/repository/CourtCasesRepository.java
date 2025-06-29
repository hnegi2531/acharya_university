package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CourtCases;

@Transactional
@Repository
public interface CourtCasesRepository extends JpaRepository<CourtCases,Integer>{
	
	@Query(value = "SELECT count(*) FROM CourtCases cc where cc.case_no=?1 and cc.active=true")
	public Integer countOfCaseNo(String case_no);
	
	@Query(value = "SELECT cc FROM CourtCases cc where cc.active=true")
	public List<CourtCases> findAll1();
	
	@Query(value = "SELECT new map(cc.court_cases_id as id,cc.advocate_or_firm_name as advocate_or_firm_name,"
			+ "cc.advocate_or_firm_contact_no as advocate_or_firm_contact_no,cc.case_no as case_no,cc.appeal_ref_no as appeal_ref_no,"
			+ "cc.plaintiffs as plaintiffs,cc.defendants as defendants,cc.court_id as court_id,cc.last_hearing_date as last_hearing_date,"
			+ "cc.next_hearing_date as next_hearing_date,cc.stage_of_the_case as stage_of_the_case,cc.case_content as case_content,"
			+ "cc.case_status as case_status,cc.case_type as case_type,cc.remarks as remarks,ct.court_name as court_name,ct.court_short_name as court_short_name,"
			+ "cc.active as active,cc.created_date as created_date,cc.modified_date as modified_date,cc.fronted_use_next_hearing_date as fronted_use_next_hearing_date,"
			+ "cc.fronted_use_last_hearing_date as fronted_use_last_hearing_date) FROM CourtCases cc "
			+ "Left Join Court ct On ct.court_id=cc.court_id "
			+ "Where CONCAT(IfNull(cc.court_cases_id,''),'',IfNull(cc.advocate_or_firm_name,''),'',IfNull(cc.advocate_or_firm_contact_no,''),'',"
			+ "IfNull(cc.case_no,''),'',IfNull(cc.appeal_ref_no,''),'',IfNull(cc.plaintiffs,''),'',IfNull(cc.defendants,''),'',"
			+ "IfNull(cc.last_hearing_date,''),'',IfNull(cc.next_hearing_date,''),'',IfNull(cc.stage_of_the_case,''),'',IfNull(cc.case_content,''),'',IfNull(cc.case_status,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "SELECT new map(cc.court_cases_id as id,cc.advocate_or_firm_name as advocate_or_firm_name,"
			+ "cc.advocate_or_firm_contact_no as advocate_or_firm_contact_no,cc.case_no as case_no,cc.appeal_ref_no as appeal_ref_no,"
			+ "cc.plaintiffs as plaintiffs,cc.defendants as defendants,cc.court_id as court_id,cc.last_hearing_date as last_hearing_date,"
			+ "cc.next_hearing_date as next_hearing_date,cc.stage_of_the_case as stage_of_the_case,cc.case_content as case_content,"
			+ "cc.case_status as case_status,cc.case_type as case_type,cc.remarks as remarks,ct.court_name as court_name,ct.court_short_name as court_short_name,"
			+ "cc.active as active,cc.created_date as created_date,cc.modified_date as modified_date,cc.fronted_use_next_hearing_date as fronted_use_next_hearing_date,"
			+ "cc.fronted_use_last_hearing_date as fronted_use_last_hearing_date) FROM CourtCases cc "
			+ "Left Join Court ct On ct.court_id=cc.court_id")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update CourtCases cc set cc.active=false where cc.court_cases_id=?1")
	public void deactivate(Integer court_id);
	
	@Modifying
	@Query(value = "update CourtCases cc set cc.active=true where cc.court_id=?1")
	public void activate(Integer court_id);
	
	@Query(value = "SELECT count(cc) FROM CourtCases cc where cc.court_cases_id != ?1 and cc.case_no=?2 and cc.active=true")
	public Integer countforUpdateCaseNo(Integer court_cases_id,String case_no);

}
