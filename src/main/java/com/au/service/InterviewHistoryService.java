package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.model.Interview;
import com.au.model.InterviewHistory;
import com.au.repository.InterviewHistoryRepository;

@Service
public class InterviewHistoryService {
	
	@Autowired
	private InterviewHistoryRepository ihr_ser;
	
	public void saveToInterviewHistory(@Valid List<Interview> interviews) {
		
		interviews.stream().forEach(i1 -> {
			InterviewHistory i = new InterviewHistory();
			i.setInterview_date(i1.getInterview_date());
			i.setInterview_id(i1.getInterview_id());
			i.setActive(i1.getActive());
			i.setApprove(i1.getApprove());
			i.setBody(i1.getBody());
			i.setComments(i1.getComments());
			i.setCreated_by(i1.getCreated_by());
			i.setCreated_date(i1.getCreated_date());
			i.setCreated_username(i1.getCreated_username());
			i.setJob_id(i1.getJob_id());
			i.setModified_by(i1.getModified_by());
			i.setModified_date(i1.getModified_date());
			i.setModified_username(i1.getModified_username());
			i.setSchedule(i1.getSchedule());
			ihr_ser.save(i);
		});
		
	}

}
