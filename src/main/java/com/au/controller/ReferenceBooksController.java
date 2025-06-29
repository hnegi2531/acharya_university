package com.au.controller;

import java.io.IOException;
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
import com.au.model.ReferenceBooks;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ReferenceBooksService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class ReferenceBooksController {

	@Autowired
	private ReferenceBooksService r_repo;

	Logger log = LoggerFactory.getLogger(ReasonFeeExcemptionController.class);
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/ReferenceBooks")
	public ResponseEntity<Object> saveCourse(@RequestBody @Valid ReferenceBooks r,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		r.setCreated_by(jwtDetails.getUserId());
		r.setCreated_username(jwtDetails.getUserName());
		ReferenceBooks reference_books = r_repo.saveReferenceBooks(r);
		ResponseEntity<Object> reference_books_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, reference_books);
		return reference_books_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/ReferenceBooks")
	public ResponseEntity<Object> getAllReferenceBooks(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> reference_books_filtered =  r_repo.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return reference_books_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> reference_books_sorted = r_repo.getAllSortedData(pageable1);
			return reference_books_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	
	@GetMapping("/ReferenceBooks/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	ReferenceBooks product = r_repo.get(id);
	    	ResponseEntity<Object> reference_books_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return reference_books_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}   
	}

	@PutMapping("/ReferenceBooks/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid ReferenceBooks r, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	ReferenceBooks existProduct = r_repo.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			r.setModified_by(jwtDetails.getUserId());
			r.setModified_username(jwtDetails.getUserName());
	    	r_repo.updateReferenceBooks(r);
	    	ResponseEntity<Object> reference_books_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return reference_books_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/ReferenceBooks/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		r_repo.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateReferenceBooks/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		r_repo.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		 return response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/referenceBooksDetails/{school_id}/{program_specialization_id}")
	public ResponseEntity<Object> getReferenceBooks(@PathVariable Integer school_id,@PathVariable Integer program_specialization_id){

		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> referencebooks = r_repo.getReferenceBooks(school_id,program_specialization_id);
		ResponseEntity<Object> referencebooks_response= ResponseHandler.generateResponse(true, HttpStatus.OK, referencebooks);
		return referencebooks_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	//APi for lesson_plan dropdown
	@GetMapping("/referenceBooksForLessonPlan/{school_id}/{program_specialization_id}")
	public ResponseEntity<Object> getReferenceBooksForLessonPlan(@PathVariable Integer school_id,@PathVariable Integer program_specialization_id){

		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> referencebooks = r_repo.getReferenceBooksForLessonPlan(school_id,program_specialization_id);
		ResponseEntity<Object> referencebooks_response= ResponseHandler.generateResponse(true, HttpStatus.OK, referencebooks);
		return referencebooks_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	
	@GetMapping("/ReferenceBooksDetails/{id}")
	public ResponseEntity<Object> getDetails(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				
			List<Map<String, Object>> referencebooks = r_repo.getDetails(id);
				ResponseEntity<Object> referencebooks_response = ResponseHandler.generateResponse(true, HttpStatus.OK, referencebooks);
				return referencebooks_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/referenceBooksDetails/{program_specialization_id}")
	public ResponseEntity<Object> getReferenceBooks(@PathVariable Integer program_specialization_id){

		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> referencebooks = r_repo.getReferenceBooks(program_specialization_id);
		ResponseEntity<Object> referencebooks_response= ResponseHandler.generateResponse(true, HttpStatus.OK, referencebooks);
		return referencebooks_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
}
		
