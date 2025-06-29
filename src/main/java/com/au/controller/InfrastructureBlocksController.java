package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

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
import com.au.model.InfrastructureBlocks;
import com.au.repository.InfrastructureFloorsRepository;
import com.au.response.ResponseHandler;
import com.au.service.InfrastructureBlocksService;
import com.au.service.InfrastructureFloorsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class InfrastructureBlocksController {
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private InfrastructureBlocksService block_ser;
	
	@Autowired
	private InfrastructureFloorsRepository floors_repo;
	
	@Autowired
	private InfrastructureFloorsService floors_ser;
	
	@PostMapping("/blocks")
	public ResponseEntity<Object> saveBlocks(@Valid @RequestBody InfrastructureBlocks blocks,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			blocks.setCreated_by(jwtDetails.getUserId());
			blocks.setCreated_username(jwtDetails.getUserName());
			//block_ser.checkForExistingData(blocks);
			InfrastructureBlocks ib = block_ser.saveBlocks(jwtDetails,blocks);
			ResponseEntity<Object> blocks_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, ib);
			return blocks_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/blocks")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<InfrastructureBlocks> list_blocks = block_ser.listAll();
			ResponseEntity<Object> list_blocks_response = ResponseHandler.generateResponse(true, HttpStatus.OK,list_blocks);
			return list_blocks_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllBlocksDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> block_sorted = block_ser.getAllDataFilteredByKeyword(pageable,keyword);
				return block_sorted;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> block_pageable = block_ser.getAllSortedData(pageable1);
				return block_pageable;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/blocks/{block_id}")
	public ResponseEntity<Object> get(@PathVariable Integer block_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				InfrastructureBlocks blocks = block_ser.getBlock(block_id);
				ResponseEntity<Object> block_response = ResponseHandler.generateResponse(true, HttpStatus.OK,blocks);
				return block_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/blocks/{block_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid InfrastructureBlocks ib, @PathVariable Integer block_id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				ib.setModified_by(jwtDetails.getUserId());
				ib.setModified_username(jwtDetails.getUserName());
				block_ser.saveUpdateBlock(ib);
				floors_ser.checkForExistingData(ib.getBlock_id());
				floors_ser.createFloors(jwtDetails, ib.getBlock_id(),ib.getBasement(),ib.getTotal_no_of_floor(),ib.getSchool_id());
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/deactivateBlock/{block_id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer block_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			block_ser.deactivate(block_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateBlock/{block_id}")
	public ResponseEntity<Object> activate(@PathVariable Integer block_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			block_ser.activate(block_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

}
