package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.model.ProctorHeadHistory;
import com.au.repository.ProctorHeadHistoryRepository;

@Service
public class ProctorHeadHistoryService {

	@Autowired
	private ProctorHeadHistoryRepository phhr_repo;
	
	public List<ProctorHeadHistory> saveProctorHeadHistory1(List<ProctorHeadHistory> proctor_assign) {
		return	phhr_repo.saveAll(proctor_assign);		
	}

	public void saveProctorHeadHistory(List<Integer> proctor_assign_id, String userName) {
			phhr_repo.insertData(proctor_assign_id,userName);		
	}

	public List<ProctorHeadHistory> listAll() {
		return phhr_repo.findAll();
	}

	public List<ProctorHeadHistory> findByProctorHeadHistory(Integer student_id) {
		return phhr_repo.allHistoryDetails(student_id);
	}

	public ProctorHeadHistory saveProctorHeadHistory1(ProctorHeadHistory phh) {
		return phhr_repo.save(phh);
		
	}
}
