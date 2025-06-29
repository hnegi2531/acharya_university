package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JobFileRequest;
import com.au.dto.JwtDetails;
import com.au.model.FamilyStructure;
import com.au.model.Syllabus;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.Syllabus_Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class SyllabusController {
	Logger log = LoggerFactory.getLogger(SyllabusController.class);

	@Autowired
	private Syllabus_Service sy_service;

	@Autowired
	private JwtTokenService jwt_service;

	
	@PostMapping("/syllabusObjective")
	public ResponseEntity<Object> saveCourseObjective(@RequestBody @Valid List<Syllabus> s,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			s.stream().forEach(f -> {

			f.setCreated_by(jwtDetails.getUserId());
			f.setCreated_username(jwtDetails.getUserName());
			});
			List<Syllabus> sy = sy_service.saveSyllabusObjective(s);
			ResponseEntity<Object> syllabus_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, sy);
		return syllabus_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/syllabus")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Syllabus> list_syllabus = sy_service.listAll();
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/fetchAllSyllabusDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> syllabus_sorted = sy_service.listAll1(pageable, keyword);//,column,value);
					return syllabus_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> syllabus_pageable = sy_service.listAll2(pageable1);
					return syllabus_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		

	}

	@GetMapping("/syllabus/{id}")
	public ResponseEntity<Object> get(@PathVariable List<Integer> id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			List<Syllabus> syllabus = sy_service.get(id);
			ResponseEntity<Object> syllabus_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, syllabus);
			return syllabus_response_by_id;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
			return response1;
		}
	} else {
		ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		return rs;
	}		
	}

	@PutMapping("/syllabus/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<Syllabus> family,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken) throws Exception{
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				family.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				sy_service.saveSyllabusObjective(family);
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

	@DeleteMapping("/syllabus/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			sy_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/activatesyllabus/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			sy_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}


	@PostMapping(value = "/syllabusUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute JobFileRequest jobfilerequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		sy_service.uploadFile(jobfilerequest.getFile(), jobfilerequest.getSyllabus_id());
		 ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	
	
	@GetMapping(path = "/syllabusFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = sy_service.viewFiles(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@GetMapping("/getSyllabusByCourseAssignmentId/{course_assignment_id}")
	public ResponseEntity<Object> getSyllabusByCourseAssignmentId(@PathVariable Integer course_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Syllabus> list_syllabus = sy_service.getSyllabusByCourseAssignmentId(course_assignment_id);
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getSyllabusDetailsData/{course_id}")
	public ResponseEntity<Object> getSyllabusDetailsData(@PathVariable Integer course_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Syllabus> list_syllabus = sy_service.getSyllabusDetailsData(course_id);
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}	
	
	@GetMapping("/getSyllabusDetails/{course_assignment_id}")
	public ResponseEntity<Object> getSyllabusDetails(@PathVariable Integer course_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> list_syllabus = sy_service.getSyllabusDetails(course_assignment_id);
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/syllabusByCourseAssignment/{courseAsgsignmentId}")
	public ResponseEntity<Object> syllabusByCourseAssignment(@PathVariable Integer courseAsgsignmentId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Syllabus> list_syllabus = sy_service.syllabusByCourseAssignment(courseAsgsignmentId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			 
		}		
	}
	
	@GetMapping("/syllabusByCourseIdAndAcYearId/{courseId}/{acYearId}")
	public ResponseEntity<Object> syllabusByCourseIdAndAcYearId(@PathVariable Integer courseId,@PathVariable Integer acYearId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> list_syllabus = sy_service.syllabusByCourseAssignment(courseId,acYearId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			 
		}		
	}
}
