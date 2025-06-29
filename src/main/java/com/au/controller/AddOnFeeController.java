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
import com.au.model.AddOnFee;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.AddOnFeeService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class AddOnFeeController {
	
	Logger log = LoggerFactory.getLogger(AddOnFeeController.class);

	@Autowired
	private AddOnFeeService twp_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/createAddOnFee")
	public ResponseEntity<Object> createAddOnFee(@RequestBody @Valid AddOnFee addon,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			addon.setCreatedBy(jwtDetails.getUserId());
			addon.setCreatedUsername(jwtDetails.getUserName());
			AddOnFee AddOnFee = twp_Service.createAddOnFee(addon, jwtDetails);
			ResponseEntity<Object> vacationresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					AddOnFee);
			return vacationresponse;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/allActiveAddOnFee")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<AddOnFee> vacation = twp_Service.listAll();
			ResponseEntity<Object> vacation_response = ResponseHandler.generateResponse(true, HttpStatus.OK, vacation);
			return vacation_response;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/getAddOnFee/{addOnFeeId}")
	public ResponseEntity<Object> get(@PathVariable Integer addOnFeeId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				AddOnFee response = twp_Service.get(addOnFeeId);
				ResponseEntity<Object> tech_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						response);
				return tech_response;
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

	@GetMapping("/fetchAllAddOnFee")
	public ResponseEntity<Object> fetchAllAddOnFee(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword)
			throws JsonParseException, JsonMappingException, IOException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> vacation_filtered = twp_Service.getAllDataFilteredByKeyword(pageable, keyword);
				return vacation_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> vacation_sorted = twp_Service.getAllSortedData(pageable1);
				return vacation_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateAddOnFeeById/{addOnFeeId}")
	public ResponseEntity<Object> update(@RequestBody @Valid AddOnFee pr,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
				pr.setModifiedBy(jwtDetails.getUserId());
				pr.setModifiedUsername(jwtDetails.getUserName());
				AddOnFee response = twp_Service.updateAddOnFee(pr);

				ResponseEntity<Object> tech_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return tech_response;
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

	@DeleteMapping("/deactivateAddOnFee/{addOnFeeId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer addOnFeeId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			twp_Service.delete(addOnFeeId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateAddOnFee/{addOnFeeId}")
	public ResponseEntity<Object> activate(@PathVariable Integer addOnFeeId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			twp_Service.delete1(addOnFeeId);
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
