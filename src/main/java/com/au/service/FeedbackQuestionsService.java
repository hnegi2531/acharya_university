package com.au.service;

import java.util.ArrayList;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.au.dto.FeedbackQuestionsDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FeedbackQuestions;
import com.au.model.Shift;
import com.au.repository.FeedbackQuestionsRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class FeedbackQuestionsService {
	
	@Autowired
	private FeedbackQuestionsRepository fc_repo;
	
	
	public List<FeedbackQuestions> listAll(){
		return fc_repo.findAll11();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = fc_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
	
	Page<Object> response = fc_repo.findAll2(pageable);
	return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public FeedbackQuestions get(Integer id) {
    return fc_repo.findById(id)
    		.orElseThrow(()-> new ResourceNotFoundException("feedback questions Not Found:"+id));
	}
 
	public FeedbackQuestions save_academic_school(FeedbackQuestions bq) {
		
			if ((fc_repo.checkFeedbackQuestionsForUpdate(bq.getSchool_id(),bq.getFeedback_questions())) >= 1) {
				throw new RuntimeException("Combination of Feedback Question and School Name is Already Exist!!!");
			}
			
			else {
				return fc_repo.save(bq);
			}
		}
 
		
	
	
	public ResponseEntity<Object> delete(Integer id) {
		FeedbackQuestions ay = fc_repo.findById(id)
	    		.orElseThrow(()-> new ResourceNotFoundException("academic_school Not Found:"+id));    	
		fc_repo.update(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	public ResponseEntity<Object> delete1(Integer id) {
		FeedbackQuestions ay = fc_repo.findById(id)
	    		.orElseThrow(()-> new ResourceNotFoundException("academic_school Not Found:"+id));    	
		fc_repo.update1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}	
		
	public List<FeedbackQuestions> getFeedbackQuestions(@Valid FeedbackQuestionsDto fq) throws Exception {
	
			
		List<FeedbackQuestions> list = new ArrayList<>();
		
		fq.getSchool_id().stream().forEach(f -> {
			if(fc_repo.countOfcombination(f,fq.getFeedback_questions()) >=1) {
				throw new RuntimeException("combination of Institute id or Feedback question Already Exist");
			}
			else {
			
			FeedbackQuestions fqq= new FeedbackQuestions();
			fqq.setActive(fq.getActive());
			fqq.setCreated_by(fq.getCreated_by());
			fqq.setCreated_date(fq.getCreated_date());
			fqq.setCreated_username(fq.getCreated_username());
			fqq.setFeedback_id(fq.getFeedback_id());
			fqq.setFeedback_questions(fq.getFeedback_questions());
			fqq.setModified_by(fq.getModified_by());
			fqq.setModified_date(fq.getModified_date());
			fqq.setModified_username(fq.getModified_username());
			fqq.setSchool_id(f);
			
			list.add(fqq);
			}
		});
		
		
		return fc_repo.saveAll(list);
		}
		
		
		
	}
