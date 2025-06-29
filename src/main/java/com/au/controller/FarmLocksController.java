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
import com.au.model.FarmLocks;
import com.au.model.FarmLocks;
import com.au.response.ResponseHandler;
import com.au.service.FarmLocksService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class FarmLocksController {

	Logger log = LoggerFactory.getLogger(FarmLocksController.class);

	@Autowired
	private FarmLocksService fl_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/createFarmLocks")
	public ResponseEntity<Object> createFarmLocks(@RequestBody @Valid FarmLocks fts,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			fts.setCreatedBy(jwtDetails.getUserId());
			fts.setCreatedUsername(jwtDetails.getUserName());
			FarmLocks profile = fl_Service.createFarmLocks(fts);
			ResponseEntity<Object> profileresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					profile);
			return profileresponse;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/allActiveFarmLocks")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<FarmLocks> profile = fl_Service.listAll();
			ResponseEntity<Object> profile_response = ResponseHandler.generateResponse(true, HttpStatus.OK, profile);
			return profile_response;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/getFarmLocks/{lockId}")
	public ResponseEntity<Object> get(@PathVariable Integer lockId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				FarmLocks profile = fl_Service.get(lockId);
				ResponseEntity<Object> profile_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						profile);
				return profile_response;
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

	@GetMapping("/fetchAllFarmLocks")
	public ResponseEntity<Object> fetchAllFarmLocks(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword)
			throws JsonParseException, JsonMappingException, IOException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> profile_filtered = fl_Service.getAllDataFilteredByKeyword(pageable, keyword);
				return profile_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> profile_sorted = fl_Service.getAllSortedData(pageable1);
				return profile_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateFarmLocks/{lockId}")
	public ResponseEntity<Object> update(@RequestBody @Valid FarmLocks pr,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
				FarmLocks profile_req = fl_Service.updateFarmLocks(pr);

				ResponseEntity<Object> profile_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return profile_response;
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

	@DeleteMapping("/deactivateFarmLocks/{lockId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer lockId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			fl_Service.delete(lockId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateFarmLocks/{lockId}")
	public ResponseEntity<Object> activate(@PathVariable Integer lockId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			fl_Service.delete1(lockId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
//	@PutMapping("/updateChallengesFacedInFarmLocks/{lockId}/{challengesFaced}")
//	public ResponseEntity<Object> updateChallengesFacedInFarmLocks(@PathVariable @Valid String challengesFaced,@PathVariable @Valid String lockId,
//			@RequestHeader("Authorization") String jwtToken)
//			throws JsonParseException, JsonMappingException, IOException {
//		if (RateLimitController.bucket.tryConsume(1)) {
//			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//			try {
//				FarmLocks profile_req = fl_Service.updateFarmLocks(challengesFaced);
//
//				ResponseEntity<Object> profile_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
//						HttpStatus.OK);
//				return profile_response;
//			} catch (NoSuchElementException e) {
//				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
//						HttpStatus.NOT_FOUND);
//				return response;
//			}
//		} else {
//			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
//					ResponseHandler.message1);
//			return rs;
//		}
//	}
	
}
