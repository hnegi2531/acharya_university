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
import com.au.model.ApplicantDetails;
import com.au.model.CourseObjective;
import com.au.response.ResponseHandler;
import com.au.service.ApplicantDetailsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ApplicantDetailsController {

	@Autowired
	private ApplicantDetailsService a_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	Logger log = LoggerFactory.getLogger(ApplicantDetailsController.class);
	
	@PostMapping("/ApplicantDetails")
	public ResponseEntity<Object> saveApplicantDetails(@RequestBody @Valid List<ApplicantDetails> app,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				app.stream().forEach(a -> {

					a.setCreated_by(jwtDetails.getUserId());
					a.setCreated_username(jwtDetails.getUserName());
				});
				
				List<ApplicantDetails> apd= a_service.saveApplicantDetails(app);
				ResponseEntity<Object> applicant_details_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, apd);
				return applicant_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/ApplicantDetails")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword){
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> applicant_details_reponse = a_service.listAll1(pageable, keyword);//,column,value);
						return applicant_details_reponse;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> applicant_details_reponse1 = a_service.listAll2(pageable1);
						return applicant_details_reponse1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return a_service.listAll();
	}

	
	@GetMapping("/ApplicantDetails/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						ApplicantDetails product = a_service.get(id);
						ResponseEntity<Object> applicant_details_by_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return applicant_details_by_id_response;
						//return new ResponseEntity<ApplicantDetails>(product, HttpStatus.OK);
	        
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
						//return new ResponseEntity<ApplicantDetails>(HttpStatus.NOT_FOUND);
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/ApplicantDetails/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<ApplicantDetails> courseObjective,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken) throws Exception{
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				courseObjective.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				a_service.update(courseObjective);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (JsonParseException | JsonMappingException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	
	@DeleteMapping("/ApplicantDetails/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return	a_service.delete(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	
	@GetMapping("/getApplicationDetails/{studentId}")
	public ResponseEntity<Object> getApplicationDetails(@PathVariable Integer studentId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> studentDetails = a_service.getApplicationDetails(studentId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					studentDetails);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
}
