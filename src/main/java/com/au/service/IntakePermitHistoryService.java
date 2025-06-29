package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.model.IntakePermitHistory;
import com.au.repository.IntakePermitHistoryRepository;

@Service
public class IntakePermitHistoryService {

	@Autowired
	private IntakePermitHistoryRepository intake_permit_history_repo;
	
	
	public List<IntakePermitHistory> saveIntakePermitHistory(List<IntakePermitHistory> iph) throws Exception {
		return intake_permit_history_repo.saveAll(iph);
	}
	
	public List<HashMap<String, Object>> getIntakePermitHistoryDetails(List<Integer> intake_history_ids) {
		return intake_permit_history_repo.getIntakePermitHistoryDetails(intake_history_ids);
	}
}
