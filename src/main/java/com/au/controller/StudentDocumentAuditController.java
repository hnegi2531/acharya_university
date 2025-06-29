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
import com.au.model.StudentDocumentAudit;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StudentDocumentAuditService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentDocumentAuditController {

	Logger log = LoggerFactory.getLogger(StudentDocumentAuditController.class);

	@Autowired
	private StudentDocumentAuditService studentDocumentAuditService;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/saveStudentDocumentAudit")
	public ResponseEntity<Object> saveStudentDocumentAudit(@RequestBody @Valid StudentDocumentAudit sda,
			@RequestHeader("Authorization") String jwtToken) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			sda.setCreated_by(jwtDetails.getUserId());
			sda.setCreated_username(jwtDetails.getUserName());

			StudentDocumentAudit tms = studentDocumentAuditService.saveStudentDocumentAudit(sda);
			ResponseEntity<Object> tm_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, tms);
			return tm_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getAllActiveStudentDocumentAudit")
	public ResponseEntity<Object> getAllActiveStudentDocumentAudit() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<StudentDocumentAudit> tms = studentDocumentAuditService.getAllActiveStudentDocumentAudit();
			ResponseEntity<Object> tm_response = ResponseHandler.generateResponse(true, HttpStatus.OK, tms);
			return tm_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getStudentDocumentAudit/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				StudentDocumentAudit dps = studentDocumentAuditService.get(id);
				ResponseEntity<Object> tm_response = ResponseHandler.generateResponse(true, HttpStatus.OK, dps);
				return tm_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateStudentDocumentAudit/{id}")
	public ResponseEntity<Object> updateStudentDocumentAudit(@RequestBody @Valid StudentDocumentAudit dp,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				// Department existProduct = deptService.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				dp.setModified_by(jwtDetails.getUserId());
				dp.setModified_username(jwtDetails.getUserName());
				studentDocumentAuditService.updateStudentDocumentAudit(dp);
				ResponseEntity<Object> tm_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return tm_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/deactiveStudentDocumentAudit/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			studentDocumentAuditService.deactivate(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateStudentDocumentAudit/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			studentDocumentAuditService.activate(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllStudentDocumentAudit")
	public ResponseEntity<Object> fetchAllStudentDocumentAudit(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> oc_filtered = studentDocumentAuditService.getAllDataFilteredByKeyword(pageable,
						keyword);
				return oc_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> oc_sorted = studentDocumentAuditService.getAllSortedData(pageable1);
				return oc_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
}
