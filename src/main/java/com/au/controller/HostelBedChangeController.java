package com.au.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.au.dto.HostelBedChangeDto;
import com.au.response.ResponseHandler;
import com.au.service.HostelBedChangeService;

@RestController
@RequestMapping("/api/${secretkey6}")
@CrossOrigin
public class HostelBedChangeController {
	
	Logger log=LoggerFactory.getLogger(HostelBedChangeController.class);
	
	@Autowired
	private HostelBedChangeService hostelBedChangeService;
	
	@PostMapping("/hostelBedChange")
	public ResponseEntity<Object> hostelBedChange(
			@RequestBody @Valid HostelBedChangeDto hostelBedChangeDto,
			@RequestHeader("Authorization") String jwtToken) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedChangeService.hostelBedChange(hostelBedChangeDto, jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@PutMapping("/approvalOfBedChange/{hostelBedChangeId}")
	public ResponseEntity<Object> approvalOfBedChange(
			@RequestBody @Valid HostelBedChangeDto hostelBedChangeDto,
			@PathVariable Integer hostelBedChangeId,@RequestHeader("Authorization") String jwtToken) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return hostelBedChangeService.approvalOfBedChange(hostelBedChangeDto, hostelBedChangeId,jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/fetchAllHostelBedChange")
	public ResponseEntity<Object> fetchAllHostelBedAssignment(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer pageSize,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, pageSize,sorted);
					return hostelBedChangeService.filteredAndSortedResponses(pageable, keyword);
				}else {
					Pageable pageable1 = PageRequest.of(page, pageSize,sorted);
					return hostelBedChangeService.sortedResponses(pageable1);
				}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}	
	}

}
