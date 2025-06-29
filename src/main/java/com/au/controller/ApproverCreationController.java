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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.dto.JwtDetails;
import com.au.model.ApproverCreation;
import com.au.response.ResponseHandler;
import com.au.service.ApproverCreationService;
import com.au.service.JwtTokenService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApproverCreationController {

	Logger log = LoggerFactory.getLogger(ApproverCreationController.class);

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private ApproverCreationService approverCreationService;

	@PostMapping("/approverCreation")
	public ResponseEntity<Object> saveApproverCreation(@RequestBody @Valid ApproverCreation ac,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			ac.setCreated_by(jwtDetails.getUserId());
			ac.setCreated_username(jwtDetails.getUserName());
			ApproverCreation approverCreation = approverCreationService.saveApproverCreation(ac);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED,approverCreation);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/getActiveApproverCreation")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<ApproverCreation> approverCreationList = approverCreationService.listAll();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, approverCreationList);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/fetchAllApproverCreation")
	public ResponseEntity<Object> fetchAllApproverCreation(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				return approverCreationService.fetchAllApproverCreationDetails1(pageable, keyword);
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				return approverCreationService.fetchAllApproverCreationDetails2(pageable1);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/getApproverCreationById/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				ApproverCreation product = approverCreationService.get(id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@PutMapping("/updateApproverCreation/{id}")
	public ResponseEntity<?> update(@RequestBody @Valid ApproverCreation approverCreation, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				approverCreation.setModified_by(jwtDetails.getUserId());
				approverCreation.setModified_username(jwtDetails.getUserName());
				approverCreationService.saveApproverCreation(approverCreation);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/deactivateApproverCreation/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			approverCreationService.delete(id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateApproverCreation/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			approverCreationService.delete1(id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/checkUserIsFoodApproverOrNot/{user_id}")
	public ResponseEntity<Object> checkUserIsFoodApproverOrNot(@PathVariable Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Boolean approverCreationList = approverCreationService.checkUserIsFoodApproverOrNot(user_id);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, approverCreationList);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

}
