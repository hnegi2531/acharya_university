package com.au.controller;

import java.io.IOException;
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
import com.au.dto.SubjectAssignLoadDto;
import com.au.model.StdSubjectAssignment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StdSubjectAssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class StdSubjectAssignmentController {

	@Autowired
	private StdSubjectAssignmentService s_service;

	Logger log = LoggerFactory.getLogger(StdSubjectAssignmentController.class);
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/StdSubjectAssignment")
	public ResponseEntity<Object> saveAcademicSchoolVision(@RequestBody @Valid SubjectAssignLoadDto r,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			r.setCreatedBy(jwtDetails.getUserId());
			r.setCreatedUsername(jwtDetails.getUserName());
			StdSubjectAssignment ssa = s_service.saveStdSubjectAssignment(r,jwtToken);
			ResponseEntity<Object> ssa_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, ssa);
			return ssa_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

	@GetMapping("/StdSubjectAssignment")
	public ResponseEntity<Object> getAllStdSubjectAssignment(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> ssa_filtered =  s_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return ssa_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> ssa_sorted = s_service.getAllSortedData(pageable1);
			return ssa_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/StdSubjectAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			StdSubjectAssignment ssa = s_service.get(id);
			ResponseEntity<Object> ssa_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ssa);
			return ssa_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/StdSubjectAssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid StdSubjectAssignment r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			StdSubjectAssignment existProduct = s_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			r.setModifiedBy(jwtDetails.getUserId());
			r.setModifiedUsername(jwtDetails.getUserName());

			s_service.saveStdSubjectAssignment1(r);
			ResponseEntity<Object> ssa_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return ssa_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/StdSubjectAssignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		s_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateStdSubjectAssignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		s_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	/*
	 * @GetMapping("/fetchAllSubjectDetails") public List<Map<String, Object>>
	 * findSubjectDetails(){ return s_service.fetchAllDetails();
	 * 
	 * }
	 */
/*
	@GetMapping("fetch/{id1}")
	public Integer findByprogramSpecializationIdContaining(@PathVariable String id1) {
		System.out.println(s_service.findByprogramSpecializationIdContaining(id1));
		return s_service.findByprogramSpecializationIdContaining(id1);
	}
*
*/
/*
 * @GetMapping("/fetchSubjectAssign") public List<Map<String, Object>>
 * getSubjectAssignIndex(){ return s_service.getSubjectAssignIndex(); }
 * 
 * @GetMapping("/fetchSubWorkloadBySubtype") public List<Map<String,Object>>
 * fetchSubWorkLoadBySubtype(){ return s_service.fetchSubWorkLoadBySubtype(); }
 * 
 * @GetMapping("/fetchSubWorkloadByWorkloadtype") public
 * List<Map<String,Object>> fetchSubWorkLoadByWorkloadtype(){ return
 * s_service.fetchSubWorkLoadByWorkloadtype(); }
 */
}
