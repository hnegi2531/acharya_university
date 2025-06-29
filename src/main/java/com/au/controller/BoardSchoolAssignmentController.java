package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
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
import com.au.model.Board_School_Assignment;
import com.au.response.ResponseHandler;
import com.au.service.BoardSchoolAssignmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class BoardSchoolAssignmentController {

	@Autowired
	private BoardSchoolAssignmentService bs_service;

	@Autowired
	private JwtTokenService jwt_service;

	Logger log = LoggerFactory.getLogger(BoardSchoolAssignmentController.class);

	@PostMapping("/Board_School_Assignment")
	public ResponseEntity<Object> saveAcademicYear(@RequestBody @Valid Board_School_Assignment bs,	@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			bs.setCreated_by(jwtDetails.getUserId());
			bs.setCreated_username(jwtDetails.getUserName());
			Board_School_Assignment bs1 = bs_service.save_Board_School_Assignment(bs);
			ResponseEntity<Object> board_school_assignment_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, bs1);
			return board_school_assignment_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	

	}

	@GetMapping("/Board_School_Assignment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Board_School_Assignment> board_school_assignment_list = bs_service.listAll();
			ResponseEntity<Object> board_school_assignment_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, board_school_assignment_list);
			return board_school_assignment_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@GetMapping("/fetchAllBoard_School_AssignmentDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> board_school_assignment_sorted = bs_service.listAll1(pageable, keyword);//,column,value);
					return board_school_assignment_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> board_school_assignment_pageable = bs_service.listAll2(pageable1);
					return board_school_assignment_pageable;
				}
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
		//return bs_service.listAll1();
	}

	@GetMapping("/Board_School_Assignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

					Board_School_Assignment product = bs_service.get(id);
					ResponseEntity<Object> board_school_assignment_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
					return board_school_assignment_response_by_id;

			} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/Board_School_Assignment/{id}")
	public ResponseEntity<Object> update(@RequestBody Board_School_Assignment bs,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
					// Board existProduct = fts_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					bs.setModified_by(jwtDetails.getUserId());
					bs.setModified_username(jwtDetails.getUserName());
					bs_service.save_Board_School_Assignment(bs);
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

	@DeleteMapping("/Board_School_Assignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			bs_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateBoard_School_Assignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			bs_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Board_School_Assignment1/{id}")
	public ResponseEntity<Object> getBoardBySchool(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Integer get_board_id_by_school_id = bs_service.getBoardBySchool(id);
				ResponseEntity<Object> get_board_id_by_school_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, get_board_id_by_school_id);
				return get_board_id_by_school_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		

	}

	@GetMapping("/FetchBoardSchoolAssignment/{school_id}")
	public ResponseEntity<Object> getBoardSchool(@PathVariable Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> board_details_by_school_id = bs_service.getBoardSchool(school_id);
			ResponseEntity<Object> board_details_by_school_id_response= ResponseHandler.generateResponse(true, HttpStatus.OK, board_details_by_school_id);
			return board_details_by_school_id_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

}
