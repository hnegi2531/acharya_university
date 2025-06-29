package com.au.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.model.IntakeAssignmentHistory;

import com.au.repository.IntakeAssignmentHistoryRepository;


@Service
public class IntakeAssignmentHistoryService {

	@Autowired
	private IntakeAssignmentHistoryRepository intake_assignment_history_repo;
	
	
	public List<IntakeAssignmentHistory> saveIntakeAssignmentHistory(List<IntakeAssignmentHistory> iah) throws Exception {

		return intake_assignment_history_repo.saveAll(iah);

	}
	
	public List<HashMap<String,Object>> intakeAssignmentHistoryDetails(Integer intake_id) {
		return intake_assignment_history_repo.intakeAssignmentHistoryDetails(intake_id);
	}
	
}