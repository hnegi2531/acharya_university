package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.StageOfTheCase;
import com.au.repository.StageOfTheCaseRepository;
import com.au.response.ResponseHandler;

@Service
public class StageOfTheCaseService {
	
	@Autowired
	private StageOfTheCaseRepository stage_case__repo;

	public List<StageOfTheCase> listAll() {
		return stage_case__repo.findAll1();
	}

	public StageOfTheCase saveStageOfTheCase(StageOfTheCase stage_case) throws Exception {
		if(stage_case__repo.countOfStageOfThecaseName(stage_case.getStage_of_the_case_name())>=1) {
			throw new Exception("Stage Of The Case Name Already Exist");
		}else if(stage_case__repo.countOfStageOfTheCaseShortName(stage_case.getStage_of_the_case_short_name())>=1) {
			throw new Exception("Short Name Already Exist");
		}else {
			return stage_case__repo.save(stage_case);
		}
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return pr_repo.findAll();
		Page<Object> response1 = stage_case__repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return pr_repo.findAll();
		Page<Object> response = stage_case__repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public StageOfTheCase get(Integer stage_of_the_case_id) {
		return stage_case__repo.findById(stage_of_the_case_id).orElseThrow(() -> new ResourceNotFoundException("Stage Case Not Found:" + stage_of_the_case_id));
	}
	
	public StageOfTheCase updatestageOfTheCase(StageOfTheCase stage_case) {
		return stage_case__repo.save(stage_case);

	}
	
	public void delete(Integer stage_of_the_case_id) {
		stage_case__repo.findById(stage_of_the_case_id)
				.orElseThrow(() -> new ResourceNotFoundException("Stage Case Not Found:" + stage_of_the_case_id));
		stage_case__repo.update(stage_of_the_case_id);
	}

	public void delete1(Integer stage_of_the_case_id) {
		stage_case__repo.findById(stage_of_the_case_id)
		.orElseThrow(() -> new ResourceNotFoundException("Stage Case Not Found:" + stage_of_the_case_id));
		stage_case__repo.update1(stage_of_the_case_id);

	}

}
