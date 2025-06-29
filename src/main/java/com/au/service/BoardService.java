package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.Board;
import com.au.repository.BoardRepository;
import com.au.response.ResponseHandler;

@Service
public class BoardService {

	@Autowired
	private BoardRepository bo_repo;

	public List<Board> listAll() {
		return bo_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = bo_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = bo_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public Board saveBoard(Board board) throws Exception {

		if(bo_repo.countOfBoardUniqueName(board.getBoard_unique_name())>=1) {
			throw new Exception("Board Name Already exist");
		}else if(bo_repo.countOfBoardUniqueShortName(board.getBoard_unique_short_name())>=1) {
			throw new Exception("Short Name Already exist");
		}else {
			return	bo_repo.save(board);
		}
	}

	public Board saveBoarddetail(Board board) {
		return bo_repo.save(board);
	}

	public Board get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return bo_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Board id Not Found:" + id));
	}

	public void delete(Integer id) {
		Board ay = bo_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Board id Not Found:" + id));
		bo_repo.update(id);
	}

	public void delete1(Integer id) {
		Board ay = bo_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Board id Not Found:" + id));
		bo_repo.update1(id);
	}
}