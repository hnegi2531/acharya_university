package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.model.Interview;
import com.au.model.InterviewHistory;
import com.au.model.Interviewer;
import com.au.model.InterviewerHistory;
import com.au.repository.InterviewerHistoryRepository;

@Service
public class InterviewerHistoryService {
	
	@Autowired
	private InterviewerHistoryRepository irhr_ser;
	
	public void saveToInterviewerHistory(@Valid List<Interviewer> interviewers) {
		
		interviewers.stream().forEach(i1 -> {
			InterviewerHistory ir = new InterviewerHistory();
			ir.setEmail(i1.getEmail());
			ir.setEmp_id(i1.getEmp_id());
			ir.setCreated_date(i1.getCreated_date());
			ir.setHr_date(i1.getHr_date());
			ir.setHr_id(i1.getHr_id());
			ir.setHr_name(i1.getHr_name());
			ir.setHr_remarks(i1.getHr_remarks());
			ir.setInterview_id(i1.getInterview_id());
			ir.setInterviewer_comments(i1.getInterviewer_comments());
			ir.setInterviewer_id(i1.getInterviewer_id());
			ir.setJob_id(i1.getJob_id());
			ir.setModified_date(i1.getModified_date());
			ir.setInterviewer_name(i1.getInterviewer_name());
			
			irhr_ser.save(ir);
		});
		
	}

}
