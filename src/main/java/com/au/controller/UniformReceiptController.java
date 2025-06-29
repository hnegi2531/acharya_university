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
import com.au.model.UniformReceipt;
import com.au.response.ResponseHandler;
import com.au.service.UniformReceiptService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class UniformReceiptController {
	
	Logger log = LoggerFactory.getLogger(UniformReceiptController.class);

	@Autowired
	private UniformReceiptService uniformReceiptService;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/createUniformReceipt")
	public ResponseEntity<Object> createUniformReceipt(@RequestBody @Valid UniformReceipt acerp,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			acerp.setCreatedBy(jwtDetails.getUserId());
			acerp.setCreatedUsername(jwtDetails.getUserName());
			UniformReceipt UniformReceipt = uniformReceiptService.createUniformReceipt(acerp, jwtDetails);
			ResponseEntity<Object> vacationresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					UniformReceipt);
			return vacationresponse;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}
	
	@PostMapping("/createMultipleUniformReceipt")
	public ResponseEntity<Object> createMultipleUniformReceipt(@RequestBody @Valid List<UniformReceipt> acerp,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			
			acerp.stream().forEach(uni -> {
				uni.setCreatedBy(jwtDetails.getUserId());
				uni.setCreatedUsername(jwtDetails.getUserName());
			});
			
			List<UniformReceipt> UniformReceipt = uniformReceiptService.createMultipleUniformReceipt(acerp, jwtDetails);
			ResponseEntity<Object> vacationresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					UniformReceipt);
			return vacationresponse;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}


	@GetMapping("/allActiveUniformReceipt")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<UniformReceipt> vacation = uniformReceiptService.listAll();
			ResponseEntity<Object> vacation_response = ResponseHandler.generateResponse(true, HttpStatus.OK, vacation);
			return vacation_response;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/getUniformReceipt/{uniformReceiptId}")
	public ResponseEntity<Object> get(@PathVariable Integer uniformReceiptId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				UniformReceipt response = uniformReceiptService.get(uniformReceiptId);
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

	@GetMapping("/fetchAllUniformReceipt")
	public ResponseEntity<Object> fetchAllUniformReceipt(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword)
			throws JsonParseException, JsonMappingException, IOException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> vacation_filtered = uniformReceiptService.getAllDataFilteredByKeyword(pageable, keyword);
				return vacation_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> vacation_sorted = uniformReceiptService.getAllSortedData(pageable1);
				return vacation_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateUniformReceiptById/{uniformReceiptId}")
	public ResponseEntity<Object> update(@RequestBody @Valid UniformReceipt pr,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
				pr.setModifiedBy(jwtDetails.getUserId());
				pr.setModifiedUsername(jwtDetails.getUserName());
				UniformReceipt response = uniformReceiptService.updateUniformReceipt(pr);

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

	@DeleteMapping("/deactivateUniformReceipt/{uniformReceiptId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer uniformReceiptId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			uniformReceiptService.delete(uniformReceiptId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateUniformReceipt/{uniformReceiptId}")
	public ResponseEntity<Object> activate(@PathVariable Integer uniformReceiptId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			uniformReceiptService.delete1(uniformReceiptId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getUniformTransactions")
	public ResponseEntity<Object> getUniformTransactions(@RequestParam Integer fcYearId, @RequestParam Integer month){
		if (RateLimitController.bucket.tryConsume(1)) {
			return uniformReceiptService.getUniformTransactions(fcYearId, month);
		} else {
            return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		} 
	}

	@GetMapping("/getDateWiseUniformTransactions")
	public ResponseEntity<Object> getDateWiseUniformTransactions(@RequestParam String date){
		if (RateLimitController.bucket.tryConsume(1)) {
			return uniformReceiptService.getDateWiseUniformTransactions(date);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
}
