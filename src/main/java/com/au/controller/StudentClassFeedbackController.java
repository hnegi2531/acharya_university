package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.StudentClassFeedback;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StudentClassFeedbackService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentClassFeedbackController {
	

		Logger log = LoggerFactory.getLogger(DepartmentController.class);
		
		@Autowired
		private StudentClassFeedbackService scf_ser;
		
		@Autowired
		private JwtTokenService jwt_service;
		
		@PostMapping("/studentClassFeedback")
		public ResponseEntity<Object> saveStudentClassFeedback(@RequestBody @Valid StudentClassFeedback scf,@RequestHeader("Authorization") String jwtToken)
				throws Exception,JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			scf.setCreated_by(jwtDetails.getUserId());
			scf.setCreated_username(jwtDetails.getUserName());
			StudentClassFeedback feedback = scf_ser.saveStudentClassFeedback(scf);
			ResponseEntity<Object> feedback_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, feedback);
			return feedback_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
			}
		}
		
//		@GetMapping("/fetchAllStudentClassFeedback")
//		public ResponseEntity<Object> getAllStudentClassFeedback(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
//				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
//			
//			if(RateLimitController.bucket.tryConsume(1)) {
//			Sort sorted = Sort.by(Direction.DESC, sort );
//			if(keyword != null) {	
//				Pageable pageable = PageRequest.of(page, page_size,sorted);
//				System.out.println("page, page_size, sorted, keyword");
//				ResponseEntity<Object> scf_filtered =  scf_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
//				return scf_filtered;
//			} else {
//				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
//				System.out.println("page, page_size, sorted");
//				ResponseEntity<Object> scf_sorted = scf_ser.getAllSortedData(pageable1);
//				return scf_sorted;
//			}
//			} else {
//				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//				return rs;
//			}
//		}
		
		@GetMapping("/studentClassFeedback")
		public ResponseEntity<Object> listAll() {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<StudentClassFeedback> feedback = scf_ser.listAllFeedback();
			ResponseEntity<Object> feedback_response= ResponseHandler.generateResponse(true, HttpStatus.OK, feedback);
			return feedback_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		}
		
		
		@GetMapping("/studentClassFeedback/{student_class_feedback_id}")
		public ResponseEntity<Object> getStudentClassFeedback(@PathVariable Integer student_class_feedback_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
		    try {
		    	
		    	StudentClassFeedback feedback = scf_ser.getStudentClassFeedback(student_class_feedback_id);
		    	ResponseEntity<Object> feedback_response= ResponseHandler.generateResponse(true, HttpStatus.OK, feedback);
				return feedback_response;
		    } catch (NoSuchElementException e) {
		    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}

		@PutMapping("/updateStudentClassFeedback/{student_class_feedback_id}")
		public ResponseEntity<Object> updateStudentClassFeedback(@RequestBody @Valid StudentClassFeedback scf, @PathVariable Integer student_class_feedback_id,
				@RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
		    try {
		    	
		    	//Department existProduct = deptService.get(id);
		    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		    	scf.setModified_by(jwtDetails.getUserId());
		    	scf.setModified_username(jwtDetails.getUserName());
		    	scf_ser.updateStudentClassFeedback(scf);
		    	ResponseEntity<Object> scf_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return scf_response;
		    } catch (NoSuchElementException e) {
		    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			} 
		}
		
		
		@DeleteMapping("/deactivateStudentClassFeedback/{student_class_feedback_id}")
		public ResponseEntity<Object> deactivate(@PathVariable Integer student_class_feedback_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				scf_ser.delete(student_class_feedback_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}

		@DeleteMapping("/activateStudentClassFeedback/{student_class_feedback_id}")
		public ResponseEntity<Object> activate(@PathVariable Integer student_class_feedback_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				scf_ser.delete1(student_class_feedback_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		

		@GetMapping("/checkStudentClassFeedback/{student_id}/{time_table_employee_id}")
		public ResponseEntity<Object> checkStudentClassFeedback(@PathVariable Integer student_id,
				@PathVariable Integer time_table_employee_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				Boolean feedback = scf_ser.checkStudentClassFeedback(student_id, time_table_employee_id);
			ResponseEntity<Object> feedback_response= ResponseHandler.generateResponse(true, HttpStatus.OK, feedback);
			return feedback_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		}

}
