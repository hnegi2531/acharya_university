package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

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
import com.au.model.ProgramTranscript;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProgramTranscriptService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class ProgramTranscriptController {
	
	@Autowired
	private ProgramTranscriptService trans_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/ProgramTranscript")
	public ResponseEntity<Object> ProgramTranscript(@RequestBody ProgramTranscript prog, @RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			prog.setCreated_by(jwtDetails.getUserId());
			prog.setCreated_username(jwtDetails.getUserName());
			ProgramTranscript program_trans = trans_ser.saveProgramTranscript(prog);
			ResponseEntity<Object> program_trans_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, program_trans);
			return program_trans_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		    }
	}
	
	@GetMapping("/ProgramTranscript")
	public ResponseEntity<Object> listAll1() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ProgramTranscript> program_trans = trans_ser.listAll1();
		ResponseEntity<Object> program_trans_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program_trans);
		return program_trans_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}	
	
//	@GetMapping("/fetchAllProgramTranscriptDetails")
//	public List<ProgramTranscript> listAll() {
//		return trans_ser.listAll();
//	}
	
	@GetMapping("/fetchAllProgramTranscript")
	public ResponseEntity<Object> getAllProgramTranscriptDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> roles_filtered =  trans_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return roles_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> roles_sorted = trans_ser.getAllSortedData(pageable1);
			return roles_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/ProgramTranscript/{trans_id}")
	public ResponseEntity<Object> get(@PathVariable Integer trans_id ) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			ProgramTranscript pt = trans_ser.get(trans_id);
			ResponseEntity<Object> program_trans_response= ResponseHandler.generateResponse(true, HttpStatus.OK, pt);
			return program_trans_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/ProgramTranscript/{trans_id}")
	public ResponseEntity<Object> updateProgramTranscript(@RequestBody @Valid ProgramTranscript prog , 
			@PathVariable Integer trans_id , @RequestHeader("Authorization") String jwtToken )
					throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			prog.setModified_by(jwtDetails.getUserId());
			prog.setModified_username(jwtDetails.getUserName());
			trans_ser.saveUpdateProgramTranscript(prog);
			ResponseEntity<Object> program_trans_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return program_trans_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/deactivateProgramTranscript/{trans_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer trans_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		trans_ser.delete(trans_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

		
	@DeleteMapping("/activateProgramTranscript/{trans_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer trans_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		trans_ser.delete1(trans_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/allUnassignedProgramDetail/{trans_id}")
	public ResponseEntity<Object> fetchUnassignedProgramDetail(@RequestBody @PathVariable Integer trans_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String, Object>> unchecked_program=trans_ser.fetchUnassignedProgramDetail(trans_id);
		ResponseEntity<Object> unchecked_program_response= ResponseHandler.generateResponse(true, HttpStatus.OK, unchecked_program);
		return unchecked_program_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	

}
