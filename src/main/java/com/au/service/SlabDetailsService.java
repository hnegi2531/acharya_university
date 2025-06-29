package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.SlabDetails;
import com.au.repository.SlabDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class SlabDetailsService {

	@Autowired
	private SlabDetailsRepository sdr_repo;

	public List<SlabDetails> listAll() {
		return sdr_repo.findAll1();
	}
	
//	public List<SlabDetails> listAll1() {
//		return sdr_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		List<Map<String,Object>> response1 = sdr_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		List<Map<String,Object>> response = sdr_repo.findAll3(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	public SlabDetails saveSlabDetails(SlabDetails slabdetails) throws Exception {
		if(sdr_repo.fetchCountSlabName(slabdetails.getSlab_details_name()) >=1)
			throw new Exception("Slab Name Already Exist");
		else if(sdr_repo.fetchCountSlabShortName(slabdetails.getSlab_details_short_name()) >=1)
			throw new Exception("Short Name Already Exist");
		else
		return sdr_repo.save(slabdetails);
	}

	public SlabDetails get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return sdr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SlabDetails id Not Found:" + id));
	}

	public void delete(Integer id) {
		SlabDetails ay = sdr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SlabDetails id Not Found:" + id));
		sdr_repo.update(id);
	}

	public void delete1(Integer id) {
		SlabDetails ay = sdr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SlabDetails id Not Found:" + id));
		sdr_repo.update1(id);
		
	}

}
