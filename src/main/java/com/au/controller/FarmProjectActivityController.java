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
import com.au.model.FarmProjectActivity;
import com.au.response.ResponseHandler;
import com.au.service.FarmProjectActivityService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class FarmProjectActivityController {
	
	Logger log = LoggerFactory.getLogger(FarmProjectActivityController.class);

	@Autowired
	private FarmProjectActivityService pa_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/createFarmProjectActivity")
	public ResponseEntity<Object> createFarmProjectActivity(@RequestBody @Valid List<FarmProjectActivity> fts,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			fts.stream().forEach(farm -> {
				farm.setCreatedBy(jwtDetails.getUserId());
				farm.setCreatedUsername(jwtDetails.getUserName());
			});

			List<FarmProjectActivity> profile = pa_Service.createFarmProjectActivity(fts);
			ResponseEntity<Object> profileresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					profile);
			return profileresponse;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/allActiveFarmProjectActivity")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<FarmProjectActivity> profile = pa_Service.listAll();
			ResponseEntity<Object> profile_response = ResponseHandler.generateResponse(true, HttpStatus.OK, profile);
			return profile_response;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/getFarmProjectActivity/{projectActivityId}")
	public ResponseEntity<Object> get(@PathVariable Integer projectActivityId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				FarmProjectActivity profile = pa_Service.get(projectActivityId);
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

	@GetMapping("/fetchAllFarmProjectActivity")
	public ResponseEntity<Object> fetchAllFarmProjectActivity(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword)
			throws JsonParseException, JsonMappingException, IOException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> profile_filtered = pa_Service.getAllDataFilteredByKeyword(pageable, keyword);
				return profile_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> profile_sorted = pa_Service.getAllSortedData(pageable1);
				return profile_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateFarmProjectActivity/{projectActivityId}")
	public ResponseEntity<Object> update(@RequestBody @Valid FarmProjectActivity pr,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
				FarmProjectActivity profile_req = pa_Service.updateFarmProjectActivity(pr);

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

	@DeleteMapping("/deactivateFarmProjectActivity/{projectActivityId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer projectActivityId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			pa_Service.delete(projectActivityId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateFarmProjectActivity/{projectActivityId}")
	public ResponseEntity<Object> activate(@PathVariable Integer projectActivityId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			pa_Service.delete1(projectActivityId);
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
