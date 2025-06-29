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
import com.au.model.SlabStructure;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SlabStructureService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SlabStructureController {

	Logger log = LoggerFactory.getLogger(SlabStructureController.class);

	@Autowired
	private SlabStructureService sss_service;
	
	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SlabStructure")
	public ResponseEntity<Object> saveslabstructure(@RequestBody @Valid SlabStructure bs,@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				bs.setCreated_by(jwtDetails.getUserId());
				bs.setCreated_username(jwtDetails.getUserName());
				SlabStructure slab_structure = sss_service.saveSlabStructure(bs);
				ResponseEntity<Object> slab_structure_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, slab_structure);
				return slab_structure_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/SlabStructure")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<SlabStructure>  list_slab_structure = sss_service.listAll();
				ResponseEntity<Object> list_slab_structure_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_slab_structure);
				return list_slab_structure_response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
	}
	
	@GetMapping("/fetchAllSlabStructureDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> slab_structure_sorted = sss_service.listAll1(pageable, keyword);//,column,value);
						return slab_structure_sorted;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> slab_structure_pageable = sss_service.listAll2(pageable1);
						return slab_structure_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return sss_service.listAll1();
	}

	@GetMapping("/SlabStructure/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						SlabStructure product = sss_service.get(id);
						ResponseEntity<Object> slab_structure_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return slab_structure_response_by_id;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/SlabStructure/{id}")
	public ResponseEntity<Object> update(@RequestBody SlabStructure bs, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					bs.setModified_by(jwtDetails.getUserId());
					bs.setModified_username(jwtDetails.getUserName());
					sss_service.saveSlabStructure(bs);
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

	@DeleteMapping("/SlabStructure/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				sss_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/deleteSlabStructure/{id}")
	public ResponseEntity<Object> delete12(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				sss_service.deletes(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateSlabStructure/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				sss_service.delete1(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/getAllValues")
	public ResponseEntity<Object> getAllValues(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> min_max_value = sss_service.getAllValues();
				ResponseEntity<Object> min_max_value_response_by_slab_details_id = ResponseHandler.generateResponse(true, HttpStatus.OK, min_max_value);
				return min_max_value_response_by_slab_details_id;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}