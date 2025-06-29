package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.model.SectionAssignmentHistory;
import com.au.repository.SectionAssignmentHistoryRepository;

@Service
public class SectionAssignmentHistoryService {
	
	@Autowired
	private SectionAssignmentHistoryRepository sec_assign_his_repo;
	
	
	
	public SectionAssignmentHistory SectionAssignmentHistory(SectionAssignmentHistory sah ) throws Exception{

		return sec_assign_his_repo.save(sah);
	}
	
	public List<SectionAssignmentHistory> sectionAssignmentHistoryOnSectionAssignmentId(Integer section_assignment_id) {
		return sec_assign_his_repo.sectionAssignmentHistoryOnSectionAssignmentId(section_assignment_id);
	}

}
