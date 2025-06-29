package com.au.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.dto.PreadmissionDto;
import com.au.model.Academic_year;
import com.au.model.PreAdmissionProcess;
import com.au.model.ScholarshipApprovalStatus;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.PreAdmissionProcessService;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class PreAdmissionProcessController {

	@Autowired
	private PreAdmissionProcessService p_service;
	
	Logger log = LoggerFactory.getLogger(PreAdmissionProcessController.class);
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/PreAdmissionProcess1")
	public ResponseEntity<Object> savePreAdmissionProcess1(@RequestBody @Valid PreAdmissionProcess p,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				p.setCreated_by(jwtDetails.getUserId());
				p.setCreated_username(jwtDetails.getUserName());
				PreAdmissionProcess pre_addmission = p_service.save_PreAdmissionProcess(p);
				ResponseEntity<Object> pre_addmission_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, pre_addmission);
				return pre_addmission_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PostMapping("/PreAdmissionProcess")
	public ResponseEntity<Object> savePreAdmissionProcess(@RequestBody @Valid PreadmissionDto p,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				//p.setCreated_by(jwtDetails.getUserId());
				PreAdmissionProcess p1 = p.getPap();
				p1.setCreated_by(jwtDetails.getUserId());
				p1.setCreated_username(jwtDetails.getUserName());
				p.setPap(p1);
				ScholarshipApprovalStatus pre_addmission = p_service.getPreadmissionProcess(p,jwtToken);
				ResponseEntity<Object> pre_addmission_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, pre_addmission);
				return pre_addmission_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/PreAdmissionProcess")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> pre_addmission_sorted = p_service.listAll1(pageable, keyword);//,column,value);
					return pre_addmission_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object>  pre_addmission_pageable = p_service.listAll2(pageable1);
					return pre_addmission_pageable;
				}
		//return p_service.listAll();
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	
	@GetMapping("/PreAdmissionProcess/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						PreAdmissionProcess product = p_service.get(id);
						ResponseEntity<Object> pre_addmission_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return pre_addmission_response_by_id;
	        
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/PreAdmissionProcess/{id}")
	public ResponseEntity<Object> update(@RequestBody PreAdmissionProcess p, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						PreAdmissionProcess existProduct = p_service.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						p.setModified_by(jwtDetails.getUserId());
						p.setModified_username(jwtDetails.getUserName());
			
						p_service.save_PreAdmissionProcess(p);
						ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
						return response;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@DeleteMapping("/PreAdmissionProcess/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				p_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/deactivatePreAdmissionProcess/{id}")
	public ResponseEntity<Object> deactivatePreAdmissionProcess(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				p_service.deactivatePreAdmissionProcess(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activatePreAdmissionProcess/{id}")
	public ResponseEntity<Object> activatePreAdmissionProcess(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				p_service.activatePreAdmissionProcess(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/FindCandidateIdPreAddmission")
	public ResponseEntity<Object> findCandidateId(){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> candidate_id = p_service.findAllCandidate();
				ResponseEntity<Object> candidate_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, candidate_id);
				return candidate_id_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/findAllDetailsPreAdmission/{candidate_id}")
      public ResponseEntity<Object> findAllDetail(@PathVariable Integer candidate_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> details_by_candidate_id = p_service.findAllDetails(candidate_id);
				ResponseEntity<Object> details_by_candidate_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_by_candidate_id);
				return details_by_candidate_id_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
      
	}
	
	@GetMapping("/findAllDetailsPreAdmission1/{candidate_id}")
    public ResponseEntity<Object> findAllDetailsPreAdmission1(@PathVariable Integer candidate_id){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> details_by_candidate_id = p_service.findAllDetailsPreAdmission1(candidate_id);
				ResponseEntity<Object> details_by_candidate_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, details_by_candidate_id);
				return details_by_candidate_id_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
    
	}
		
	//scholarship details api(school,program,program specilization)
	@GetMapping("/fetchscholarship/{cid}")
	public  ResponseEntity<Object>  listAll2(@PathVariable Integer cid){
		if(RateLimitController.bucket.tryConsume(1)) {
				List<HashMap<String, Object>> scholarship_and_candidate_deatils = p_service.listAll2(cid);
				ResponseEntity<Object> scholarship_and_candidate_deatils_response = ResponseHandler.generateResponse(true, HttpStatus.OK, scholarship_and_candidate_deatils);
				return scholarship_and_candidate_deatils_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getFeeDetails")
	public ResponseEntity<Object> getFeeDetails(@RequestParam("candidateId") Integer  candidateId) {
		 return  p_service.getFeeDetails(candidateId);
		
	}
	

	
}
