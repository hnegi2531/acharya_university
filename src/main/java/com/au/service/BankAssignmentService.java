package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.BankAssignment;
import com.au.repository.BankAssignmentRepository;
import com.au.response.ResponseHandler;

@Service
public class BankAssignmentService {

	@Autowired
	private BankAssignmentRepository bank_assign_repository;

	public List<BankAssignment> listAll() {
		return bank_assign_repository.findAll1();
	}

	public List<BankAssignment> listAll1() {
		return bank_assign_repository.findAll();
	}
	

//	public List<BankAssignment> fetchAllBanknDetails() {
//		return bank_assign_repository.findAll();
//	}
	
	public ResponseEntity<Object> fetchAllBanknDetails1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = bank_assign_repository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllBanknDetails2(Pageable pageable) {
		
		Page<Object> response = bank_assign_repository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public BankAssignment saveBankAssignment(BankAssignment s) throws Exception{
		if(bank_assign_repository.countOfBankAndSchool(s.getBank_id(),s.getSchool_id()) >= 1) {
			throw new Exception("This bank already Assign to that school !!!");
		}else {	
			BankAssignment bank = bank_assign_repository.save(s);
			return bank;
		}
	}

	public BankAssignment get(Integer id) {
		return bank_assign_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Not Found:" + id));
	}

	public void delete(Integer id) {
		BankAssignment cc = bank_assign_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Not Found:" + id));
		bank_assign_repository.updateBank(id);
	}

	public void delete1(Integer id) {
		BankAssignment cc = bank_assign_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bank Not Found:" + id));
		bank_assign_repository.updateBank1(id);

	}

}
