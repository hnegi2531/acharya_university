package com.au.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.repository.ApproverCreationRepository;
import com.au.response.ResponseHandler;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ApproverCreation;

@Service
public class ApproverCreationService {
	
	Logger log = LoggerFactory.getLogger(ApproverCreationService.class);

	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Autowired
	private ApproverCreationRepository approverCreationRepository;
	
public ApproverCreation saveApproverCreation(ApproverCreation ac) throws Exception {
		
		ApproverCreation ac1 = new ApproverCreation();
		
		if(ac.getBill_approver()==true) {
			if(approverCreationRepository.getCountBill_approver(ac.getUser_id(),ac.getBill_approver())>=1) {
				throw new Exception("User is already assigned as Bill Approver !");
			} else {
				 ac1 = approverCreationRepository.save(ac);
			}
		}else if(ac.getFood_approver()==true) {
			if(approverCreationRepository.getCountFood_approver(ac.getUser_id(),ac.getFood_approver())>=1) {
				throw new Exception("User is already assigned as Food Approver !");
			} else {
				 ac1 = approverCreationRepository.save(ac);
			}
		}else if(ac.getPurchase_approver()==true) {
		    if(approverCreationRepository.getCountPurchase_approver(ac.getUser_id(),ac.getPurchase_approver())>=1) {
				throw new Exception("User is already assigned as Purchase Approver !");    	
			} else {
				 ac1 = approverCreationRepository.save(ac);
			}
		}else if(ac.getTravel_approver()==true) {
			if(approverCreationRepository.getCountTravel_approver(ac.getUser_id(),ac.getTravel_approver())>=1) {
			    throw new Exception("User is already assigned as Travel Approver !");
			} else {
				 ac1 = approverCreationRepository.save(ac);
			}
		}else {
			 ac1 = approverCreationRepository.save(ac);
			}
		return ac1;
		
	}
	
	public List<ApproverCreation> listAll() {
		return approverCreationRepository.findAll1();
	}
	
	public List<ApproverCreation> listAll1() {
		return approverCreationRepository.findAll();
	}

	public ApproverCreation get(Integer id) {
		return approverCreationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ApproverCreation Not Found:" + id));
	}

	public void delete(Integer id) {
		approverCreationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ApproverCreation Not Found:" + id));
		approverCreationRepository.updateApproverCreation(id);
	}

	public void delete1(Integer id) {
		approverCreationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ApproverCreation Not Found:" + id));
		approverCreationRepository.updateApproverCreation1(id);

	}
	
	public ResponseEntity<Object> fetchAllApproverCreationDetails1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = approverCreationRepository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllApproverCreationDetails2(Pageable pageable) {
		
		Page<Object> response = approverCreationRepository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public Boolean checkUserIsFoodApproverOrNot(Integer userId) {
		return approverCreationRepository.checkUserIsFoodApproverOrNot(userId);
	}



}
