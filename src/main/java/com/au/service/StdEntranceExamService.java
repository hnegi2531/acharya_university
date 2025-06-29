package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.au.exception.ResourceNotFoundException;
import com.au.model.StdEntranceExam;
import com.au.repository.StdEntranceExamRepository;

@Service
public class StdEntranceExamService {

	@Autowired
	private StdEntranceExamRepository s_repo;
	
	public List<StdEntranceExam> listAll(){
		return s_repo.findAll();
	}
	
	public StdEntranceExam saveStdEntranceExam(StdEntranceExam s) {
		return s_repo.save(s);
	}
	
	public StdEntranceExam get(Integer id) {
        return s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("StdEntranceExam Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	StdEntranceExam s =s_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("StdEntranceExam Not Found:"+id));    	
    	s_repo.delete(s);
    }

	public List<StdEntranceExam> getEntranceDetails(Integer studentId) {
		
		return s_repo.getEntranceExamByStudentId(studentId);
	}
}
