package com.au.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.ProctorHeadDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.EmployeeDetails;
import com.au.model.ProctorHead;
import com.au.repository.ProctorHeadRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ProctorHeadService {

	@Autowired
	private ProctorHeadRepository phr_repo;
	
	@Autowired
	private JwtTokenService jwt_service;

	public List<ProctorHead> listAll() {
		return phr_repo.findAll1();
	}

//	public List<HashMap<String, Object>> listAll1() {
//		return phr_repo.fetchAllDetails();
//	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> ph_filtered_response = phr_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ph_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> ph_sorted_response = phr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ph_sorted_response);
	}

	public ProctorHead saveProctorHead(ProctorHead proctorHead) {
		return phr_repo.save(proctorHead);
	}

	public ProctorHead get(Integer id) {
		return phr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proctor Head Id Not Found:" + id));
	}

	public void delete(Integer id) {
		ProctorHead ph = phr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proctor Head Id Not Found:" + id));
		phr_repo.update(id);
	}

	public void delete1(Integer id) {
		ProctorHead ph = phr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proctor Head Id Not Found:" + id));
		phr_repo.update1(id);
	}
	
	public List<HashMap<String, Object>> getAllActiveProctors() {
		return phr_repo.getAllActiveProctors();
	}



//	public List<HashMap<String, Object>> getProctorDetail(Integer school_id) {
//		return phr_repo.fetchProctorDetail(school_id);
//	}
//
//	public List<HashMap<String, Object>> getProctorDetails(Integer chief_proctor_id) {
//		return phr_repo.getProctorDetail(chief_proctor_id);
//	}
}
