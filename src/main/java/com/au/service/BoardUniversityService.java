package com.au.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.BoardUniversity;
import com.au.repository.BoardUniversityRepository;
import com.au.response.ResponseHandler;

@Service
public class BoardUniversityService {
	
	@Autowired
	private BoardUniversityRepository bu_repo;
	
	public BoardUniversity saveBoardUniversity(BoardUniversity boarduniversity) throws Exception {
		if(bu_repo.countOfboardName(boarduniversity.getBoard_university_name(),boarduniversity.getBoard_university_type()) >=1) {
			throw new Exception("Board Name Already Exist");
		}
		else {
			return bu_repo.save(boarduniversity);
		}
	}
	
	public List<BoardUniversity> listAll() {
		return bu_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return pr_repo.findAll();
		Page<Object> response1 = bu_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return pr_repo.findAll();
		Page<Object> response = bu_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public BoardUniversity get(Integer id) {
		return bu_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProgramType Not Found:" + id));
	}

	public BoardUniversity save_ProgramType1(BoardUniversity b) {
		return bu_repo.save(b);

	}
	
	public void delete(Integer id) {
		bu_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramType Not Found:" + id));
		bu_repo.update(id);
	}
	
	public void delete1(Integer id) {
		bu_repo.update11(id);

	}
	
	public List<BoardUniversity> listAll12(String board_university_type) {
		return bu_repo.findAll12(board_university_type);
	}

	

}
