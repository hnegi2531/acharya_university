package com.au.controller;

import java.io.IOException;
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
import org.springframework.stereotype.Controller;
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
import com.au.model.CandidateWalkinDump;
import com.au.model.Candidate_Walkin;
import com.au.model.Program;
import com.au.response.ResponseHandler;
import com.au.service.CandidateWalkinDumpService;
import com.au.service.JwtTokenService;
import com.au.service.ProgramService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class CandidateWalkinDumpController {
	
	Logger log = LoggerFactory.getLogger(ProgramController.class);

	@Autowired
	private CandidateWalkinDumpService cwd_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/saveCandidateWalkinDump")
	public ResponseEntity<Object> saveCandidateWalkinDump(@RequestBody @Valid CandidateWalkinDump p)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			CandidateWalkinDump cwd = cwd_service.saveCandidateWalkinDump(p);
			ResponseEntity<Object> program_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cwd);
			return program_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}
	
	@GetMapping("/Candidate_Walkin_dump")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<CandidateWalkinDump> list_candidate_walkin_dump = cwd_service.listAll();
				ResponseEntity<Object> list_candidate_walkin_dump_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_candidate_walkin_dump);
				return list_candidate_walkin_dump_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllFilteredCandidateWalkinDumpDetails")
	public ResponseEntity<Object> getAllCandidateWalkinDumpData(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> abc =  cwd_service.getAllDataFilteredByKeyword(pageable, keyword);
						return abc;
				}
				else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> xyz = cwd_service.getAllSortedData(pageable1);
						return xyz;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/Candidate_Walkin_dump/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

				CandidateWalkinDump product = cwd_service.get(id);
					ResponseEntity<Object> candidate_walkin_dump_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
					return candidate_walkin_dump_response_by_id;

			} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/Candidate_Walkin_dump/{id}")
	public ResponseEntity<Object> update(@RequestBody CandidateWalkinDump cwd, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					CandidateWalkinDump existProduct = cwd_service.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						cwd.setModified_by(jwtDetails.getUserId());
						cwd.setModified_username(jwtDetails.getUserName());
                             cwd_service.updateCandidateWalkinDump(cwd);
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
	

	@DeleteMapping("/Candidate_Walkin_Dump/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			cwd_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@DeleteMapping("/activateCandidate_Walkin_dump/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			cwd_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
}
