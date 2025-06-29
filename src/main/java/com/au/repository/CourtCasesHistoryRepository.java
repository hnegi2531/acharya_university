package com.au.repository;

import java.util.HashMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CourtCasesHistory;

@Transactional
@Repository
public interface CourtCasesHistoryRepository extends JpaRepository<CourtCasesHistory,Integer>{
	
	
	@Query(value = "SELECT new map(cch.court_cases_history_id as court_cases_history_id,cch.court_cases_id as id,cch.advocate_or_firm_name as advocate_or_firm_name,"
			+ "cch.advocate_or_firm_contact_no as advocate_or_firm_contact_no,cch.case_no as case_no,cch.appeal_ref_no as appeal_ref_no,"
			+ "cch.plaintiffs as plaintiffs,cch.defendants as defendants,cch.court_id as court_id,cch.last_hearing_date as last_hearing_date,"
			+ "cch.next_hearing_date as next_hearing_date,cch.stage_of_the_case as stage_of_the_case,cch.case_content as case_content,"
			+ "cch.case_status as case_status,cch.case_type as case_type,cch.remarks as remarks,ct.court_name as court_name,ct.court_short_name as court_short_name,"
			+ "cch.active as active,cch.created_date as created_date,cch.fronted_use_next_hearing_date as fronted_use_next_hearing_date,"
			+ "cch.fronted_use_last_hearing_date as fronted_use_last_hearing_date) FROM CourtCasesHistory cch "
			+ "Left Join Court ct On ct.court_id=cch.court_id where cch.court_cases_id=?1")
	public List<HashMap<String, Object>> courtCaseDetailsOnCaseNo(Integer court_cases_id);
	
	@Query(value = "SELECT new map(cch.court_cases_history_id as court_cases_history_id,cch.court_cases_id as id,cch.advocate_or_firm_name as advocate_or_firm_name,"
			+ "cch.advocate_or_firm_contact_no as advocate_or_firm_contact_no,cch.case_no as case_no,cch.appeal_ref_no as appeal_ref_no,"
			+ "cch.plaintiffs as plaintiffs,cch.defendants as defendants,cch.court_id as court_id,cch.last_hearing_date as last_hearing_date,"
			+ "cch.next_hearing_date as next_hearing_date,cch.stage_of_the_case as stage_of_the_case,cch.case_content as case_content,"
			+ "cch.case_status as case_status,cch.case_type as case_type,cch.remarks as remarks,ct.court_name as court_name,ct.court_short_name as court_short_name,"
			+ "cch.active as active,cch.created_date as created_date,cch.fronted_use_next_hearing_date as fronted_use_next_hearing_date,"
			+ "cch.fronted_use_last_hearing_date as fronted_use_last_hearing_date) FROM CourtCasesHistory cch "
			+ "Left Join Court ct On ct.court_id=cch.court_id where cch.court_cases_history_id=?1")
	public List<HashMap<String, Object>> courtCaseDetailsHistoryOnId(Integer court_cases_history_id);

}
