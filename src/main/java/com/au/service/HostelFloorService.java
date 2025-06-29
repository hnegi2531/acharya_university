package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.WardenRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.HostelFloor;
import com.au.repository.HostelFloorRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelFloorService {

	@Autowired
	private HostelFloorRepository repo;

	public List<HostelFloor> listAll() {
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

	public HostelFloor saveHostelFloor(HostelFloor s) {

		return repo.save(s);
	}

	public HostelFloor saveHostelFloor1(HostelFloor r) {
		return repo.save(r);
	}

	public HostelFloor get(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("HostelFloor Not Found:" + id));
	}

	public void delete(Integer id) {
		HostelFloor cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelFloor Not Found:" + id));
		repo.update(id);
	}

	public void delete1(Integer id) {
		HostelFloor cc = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HostelFloor Not Found:" + id));
		repo.update1(id);

	}

	public List<HostelFloor> getFloorByBlockid(Integer block_id) {
		return repo.getFloorByBlockid(block_id);
	}

	public void updateWardenId(WardenRequest w, Integer hostel_floor_id) {
		HostelFloor h = get(hostel_floor_id);
		h.setWardensId(w.getWardensId());		
		repo.save(h);
	}

	public List<Map<String, Object>> fetchhostelFloorIndex() {
		return repo.fetchhostelFloorIndex();
	}

}
