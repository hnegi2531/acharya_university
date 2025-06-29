package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.au.model.Program;
import com.au.model.ProgramTranscriptDetails;
import com.au.model.ProgramTranscriptionRequest;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProgramTranscriptDetailsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class ProgramTranscriptDetailsController {

	@Autowired
	private ProgramTranscriptDetailsService p_service;
	
	
	@Autowired
	private JwtTokenService jwt_service;

	Logger log = LoggerFactory.getLogger(ProgramTranscriptDetailsController.class);
	
	@PostMapping("/ProgramTranscriptDetails")
	public ResponseEntity<Object> saveAcademicYear(@RequestBody @Valid ProgramTranscriptionRequest p,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			p.setCreated_by(jwtDetails.getUserId());
			p.setCreated_username(jwtDetails.getUserName());
			List<ProgramTranscriptDetails> pt_details = p_service.getProgramTrasanscription(p,jwtToken);
			ResponseEntity<Object> pt_details_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, pt_details);
			return pt_details_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/ProgramTranscriptDetails")
	public ResponseEntity<Object> listAll(){
	  if(RateLimitController.bucket.tryConsume(1)) {
		List<ProgramTranscriptDetails> pt_details = p_service.listAll();
		ResponseEntity<Object> pt_details_response= ResponseHandler.generateResponse(true, HttpStatus.OK, pt_details);
		return pt_details_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
//	@GetMapping("/fetchProgramTranscriptDetails")
//	public List<HashMap<String , Object>> getAllProgramTranscriptDetails(){
//		return p_service.getProgramTranscriptDetails();
//	}
	
	@GetMapping("/fetchAllProgramTranscriptDetails")
	public ResponseEntity<Object> getAllProgramTranscriptDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> roles_filtered =  p_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return roles_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> roles_sorted = p_service.getAllSortedData(pageable1);
			return roles_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	
	@GetMapping("/ProgramTranscriptDetails/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	ProgramTranscriptDetails product = p_service.get(id);
	    	ResponseEntity<Object> pt_details_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return pt_details_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("fetchProgramTranscriptDetails/{program_id}")
	public ResponseEntity<Object> get1(@PathVariable Integer program_id) {
		  if(RateLimitController.bucket.tryConsume(1)) {
			  List<HashMap<String, Object>> pt_details = p_service.listAll4(program_id);
			  ResponseEntity<Object> pt_details_response= ResponseHandler.generateResponse(true, HttpStatus.OK, pt_details);
				return pt_details_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	

	@PutMapping("/ProgramTranscriptDetails/{ids}")
	public ResponseEntity<Object> update(@RequestBody List<ProgramTranscriptDetails> p, @PathVariable List<Integer> ids,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
	    	//ProgramTranscriptDetails existProduct = p_service.get(id);
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			p.stream().forEach(p1->{
				p1.setCreated_by(jwtDetails.getUserId());
				p1.setCreated_username(jwtDetails.getUserName());
			});
			p.stream().forEach(p1->{
				p1.setModified_by(jwtDetails.getUserId());
				p1.setModified_username(jwtDetails.getUserName());
			});
	    	//p_service.saveProgramTranscriptDetails(p);
			p_service.getProgramTranscription1(p);
			ResponseEntity<Object> pt_details_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return pt_details_response;
		} catch(NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

}	
	
	@DeleteMapping("/ProgramTranscriptDetails/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		p_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateProgramTranscriptDetails/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		p_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	

}
