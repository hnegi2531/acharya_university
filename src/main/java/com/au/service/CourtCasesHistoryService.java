package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.au.model.CourtCases;
import com.au.model.CourtCasesHistory;
import com.au.repository.CourtCasesHistoryRepository;

@Service
public class CourtCasesHistoryService {
	
	
	@Autowired
	private CourtCasesHistoryRepository court_cases_his_repo;
	
	
	
	public List<HashMap<String, Object>> courtCaseDetailsOnCaseNo(Integer court_cases_id) {
		return court_cases_his_repo.courtCaseDetailsOnCaseNo(court_cases_id);
	}
	
	
	public void saveDataInCourtCaseHistory(CourtCases court_cases) {
		CourtCasesHistory cch=new CourtCasesHistory();
		cch.setCourt_cases_id(court_cases.getCourt_cases_id());
		cch.setAdvocate_or_firm_name(court_cases.getAdvocate_or_firm_name());
		cch.setAdvocate_or_firm_contact_no(court_cases.getAdvocate_or_firm_contact_no());
		cch.setCase_no(court_cases.getCase_no());
		cch.setAppeal_ref_no(court_cases.getAppeal_ref_no());
		cch.setPlaintiffs(court_cases.getPlaintiffs());
		cch.setDefendants(court_cases.getDefendants());
		cch.setCourt_id(court_cases.getCourt_id());
		cch.setLast_hearing_date(court_cases.getLast_hearing_date());
		cch.setNext_hearing_date(court_cases.getNext_hearing_date());
		cch.setStage_of_the_case(court_cases.getStage_of_the_case());
		cch.setCase_content(court_cases.getCase_content());
		cch.setCase_status(court_cases.getCase_status());
		cch.setCase_type(court_cases.getCase_type());
		cch.setRemarks(court_cases.getRemarks());
		cch.setActive(court_cases.getActive());
		cch.setCreated_date(court_cases.getCreated_date());
		cch.setFronted_use_last_hearing_date(court_cases.getFronted_use_last_hearing_date());
		cch.setFronted_use_next_hearing_date(court_cases.getFronted_use_next_hearing_date());
		court_cases_his_repo.save(cch);
	}
	
	public List<HashMap<String, Object>> courtCaseDetailsHistoryOnId(Integer court_cases_history_id){
		return court_cases_his_repo.courtCaseDetailsHistoryOnId(court_cases_history_id);
	}
}
