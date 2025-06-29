package com.au.controller;

import java.io.IOException;
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
import com.au.model.HostelBlocks;
import com.au.response.ResponseHandler;
import com.au.service.HostelBlocksService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey6}")
@CrossOrigin
public class HostelBlocksController {

	@Autowired
	private HostelBlocksService hostelBlocksService;
	
	Logger log = LoggerFactory.getLogger(HostelBlocksController.class);
	
	@Autowired
	private JwtTokenService jwtTokenService;

	@PostMapping("/HostelBlocks")
	public ResponseEntity<Object> saveSubjectType(@RequestBody @Valid HostelBlocks r,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			r.setCreatedBy(jwtDetails.getUserId());
			r.setCreatedUsername(jwtDetails.getUserName());
			HostelBlocks hb = hostelBlocksService.saveHostelBlocks(r);
			ResponseEntity<Object> hb_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, hb);
			return hb_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

	@GetMapping("/fetchAllHostelBlocksDetails")
	public ResponseEntity<Object> getAllHostelBlocksDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> hb_filtered =  hostelBlocksService.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return hb_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> hb_sorted = hostelBlocksService.getAllSortedData(pageable1);
			return hb_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/HostelBlocks")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HostelBlocks> hb = hostelBlocksService.listAll();
			ResponseEntity<Object> hb_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hb);
			return hb_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/HostelBlocks/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			HostelBlocks hb = hostelBlocksService.get(id);
			ResponseEntity<Object> hb_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hb);
			return hb_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/HostelBlocks/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid HostelBlocks r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			hostelBlocksService.get(id);
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);

			r.setModifiedBy(jwtDetails.getUserId());
			r.setModifiedUsername(jwtDetails.getUserName());

			hostelBlocksService.updateHostelBlocks(r);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
		}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/HostelBlocks/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		hostelBlocksService.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateHostelBlocks/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		hostelBlocksService.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		 return response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
}

	@GetMapping("/fetchDetailsByDocorWardenType/{doctor_warden_type}")
	public ResponseEntity<Object> getAllDoctorWardenDetails(@PathVariable String doctor_warden_type){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> hb = hostelBlocksService.getAllDoctorWardenDetails(doctor_warden_type);
			ResponseEntity<Object> hb_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hb);
			return hb_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getDetailsOfFloorsAndRoomsForGridView/{block_id}")
	public ResponseEntity<Object> getDetailsOfFloorsAndRoomsForGridView(@PathVariable Integer block_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<Integer, Object> hb = hostelBlocksService.getDetailsOfFloorsAndRooms(block_id);
			System.out.println("+++++++++++++");
			ResponseEntity<Object> hb_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hb);
			return hb_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getHostelFloorDetails/{block_id}")
	public ResponseEntity<Object> getHostelFloorDetails(@PathVariable Integer block_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<Integer, Object> hb = hostelBlocksService.getHostelFloorDetails(block_id);
			ResponseEntity<Object> hb_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hb);
			return hb_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getHostelBlockDetailsByHostelFeeTemplate/{hostelFeeTemplateId}")
	public ResponseEntity<Object> getHostelBlockDetailsByHostelFeeTemplate(@PathVariable Integer hostelFeeTemplateId){
		if(RateLimitController.bucket.tryConsume(1)) {
			return hostelBlocksService.getHostelBlockDetailsByHostelFeeTemplate(hostelFeeTemplateId);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
}
