package com.au.service;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.VisaExpiryHistory;
import com.au.repository.VisaExpiryHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class VisaExpiryHistoryService {

	@Autowired
	private VisaExpiryHistoryRepository visaExpiryHistoryRepository;
	
	public List<VisaExpiryHistory> saveVisaExpiryHistory(@Valid List<VisaExpiryHistory> veh) throws Exception {
		return	visaExpiryHistoryRepository.saveAll(veh);
	}
	
	public List<VisaExpiryHistory> listAll1() {
		return visaExpiryHistoryRepository.findAll11();
	}
	
	public VisaExpiryHistory get(Integer id) {
		return visaExpiryHistoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Visa Expiry History id  Not Found:" + id));
	}
	
	public List<VisaExpiryHistory> updateVisaExpiryHistory(@Valid List<VisaExpiryHistory> veh) {
		return visaExpiryHistoryRepository.saveAll(veh);
	}
	
	public void delete(Integer id) {
		visaExpiryHistoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Visa Expiry History Not Found:" + id));
		visaExpiryHistoryRepository.updateHistory(id);
	}
	
	public void delete1(Integer id) {
		visaExpiryHistoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Visa Expiry History Not Found:" + id));
		visaExpiryHistoryRepository.updatehistory1(id);
	}	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> visaExpiryHistoryFilteredResponse = visaExpiryHistoryRepository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, visaExpiryHistoryFilteredResponse);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> visaExpiryHistorySortedResponse = visaExpiryHistoryRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, visaExpiryHistorySortedResponse);
	}

	public List<HashMap<String, Object>> getVisaExpiryHistoryData(Integer empId) {
		return visaExpiryHistoryRepository.getVisaExpiryHistoryData(empId);
	}	
	
}
