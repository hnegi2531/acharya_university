package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.HostelRoomType;
import com.au.repository.HostelRoomTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelRoomTypeService {

	@Autowired
	private HostelRoomTypeRepository repo;
	
	public List<HostelRoomType> listAll() {
		return repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public HostelRoomType saveHostelRoomType(HostelRoomType s) {
		if(repo.existsByroomType(s.getRoomType())) {
			throw new RuntimeException("Room Type already Exist!!!");
		}
		if(repo.existsBynumberOfBeds(s.getNumberOfBeds())) {
			throw new RuntimeException("Number Of beds already Exist!!!");
		}
		return repo.save(s);
	}

	public HostelRoomType saveHostelRoomType1(HostelRoomType r) {
		return repo.save(r);
	}
	
	public HostelRoomType get(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("HostelRoomType Not Found:" + id));
	}

	public void delete(Integer id) {
		HostelRoomType cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelRoomType Not Found:" + id));
		repo.update(id);
	}

	public void delete1(Integer id) {
		HostelRoomType cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelRoomType Not Found:" + id));
		repo.update1(id);

	}

	

	
}
