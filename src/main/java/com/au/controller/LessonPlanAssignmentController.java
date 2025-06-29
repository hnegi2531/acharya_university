package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
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
import com.au.dto.JwtDetails;
import com.au.dto.LessonPlanAttachmentDto;
import com.au.model.CourseObjective;
import com.au.model.LessonPlanAssignment;
import com.au.model.Peo;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.LessonPlanAssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class LessonPlanAssignmentController {

	@Autowired
	private LessonPlanAssignmentService a_service;

	@Autowired
	private JwtTokenService jwt_service;

	Logger log = LoggerFactory.getLogger(LessonPlanAssignmentController.class);

	@PostMapping("/lessonPlanAssignment")
	public ResponseEntity<Object> savelessonPlan(@RequestBody @Valid List<LessonPlanAssignment> bs,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			bs.stream().forEach(f -> {

			f.setCreated_by(jwtDetails.getUserId());
			f.setCreated_username(jwtDetails.getUserName());
			});
			List<LessonPlanAssignment> lesson_plan_assignment = a_service.savLessonPlanAssignment(bs);
			ResponseEntity<Object> lesson_plan_assignment_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, lesson_plan_assignment);
			return lesson_plan_assignment_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllLessonPlanAssignmentDetails")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> lesson_plan_assignment_sorted = a_service.listAll1(pageable, keyword);//,column,value);
				return lesson_plan_assignment_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> lesson_plan_assignment_pageable = a_service.listAll2(pageable1);
				return lesson_plan_assignment_pageable;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}

	@GetMapping("/lessonPlanAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				LessonPlanAssignment product = a_service.get(id);
				ResponseEntity<Object> program_type_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return program_type_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/lessonPlanAssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody LessonPlanAssignment bs, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				LessonPlanAssignment existProduct = a_service.get(id);
				bs.setModified_by(jwtDetails.getUserId());
				bs.setModified_username(jwtDetails.getUserName());
				a_service.updateLessonPlanAssignment(bs);
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

	@DeleteMapping("/deleteLessonPlanAssignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			a_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping("/LessonPlanAssignmentUsingCsvFile")
	public ResponseEntity<Object> LessonPlanAssignmentUsingCsvFile(@ModelAttribute LessonPlanAssignment lsa,
			@RequestHeader("Authorization") String jwtToken)throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			lsa.setCreated_by(jwtDetails.getUserId());
			lsa.setCreated_username(jwtDetails.getUserName());
			List<LessonPlanAssignment> data = a_service.getDataFromFile(lsa.getFile(),jwtToken);
			ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.OK, data);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@PostMapping(value = "/lessonPlanUploadFile")
	public ResponseEntity<Object> lessonPlanUploadFile(@ModelAttribute LessonPlanAttachmentDto lpa,@RequestHeader("Authorization") String jwtToken) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("Message For Attachment");
		a_service.uploadFile(lpa.getFile(), lpa.getLesson_assignment_id(),jwtToken);
		 ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	
	
	@GetMapping(path = "/fileDownloadOfLessonPlanAttachment")
	public ResponseEntity<ByteArrayResource> fileDownloadOfLessonPlanAttachment(@RequestParam("attachment_path") final String attachment_path) {
		try {
			final byte[] data = a_service.fileDownloadOfLessonPlanAttachment(attachment_path);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + attachment_path + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}	
	
	@GetMapping(path = "/imageDownloadOfLessonPlanAttachment")
	public ResponseEntity<ByteArrayResource> imageDownloadOfLessonPlanAttachment(@RequestParam("attachment_path") final String attachment_path) {
		try {
			final byte[] data = a_service.downloadFile(attachment_path);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","image/jpeg")
					.header("Content-disposition", "attachment; filename=\"" + attachment_path + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}	

}
