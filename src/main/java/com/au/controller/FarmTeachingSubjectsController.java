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
import com.au.model.FarmTeachingSubjects;
import com.au.response.ResponseHandler;
import com.au.service.FarmTeachingSubjectsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class FarmTeachingSubjectsController {

	Logger log = LoggerFactory.getLogger(FarmTeachingSubjectsController.class);

	@Autowired
	private FarmTeachingSubjectsService fts_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/createFarmTeachingSubjects")
	public ResponseEntity<Object> createFarmTeachingSubjects(@RequestBody @Valid List<FarmTeachingSubjects> fts,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			fts.stream().forEach(farm -> {
				farm.setCreatedBy(jwtDetails.getUserId());
				farm.setCreatedUsername(jwtDetails.getUserName());
			});

			List<FarmTeachingSubjects> profile = fts_Service.createFarmTeachingSubjects(fts);
			ResponseEntity<Object> profileresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					profile);
			return profileresponse;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/allActiveFarmTeachingSubjects")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<FarmTeachingSubjects> profile = fts_Service.listAll();
			ResponseEntity<Object> profile_response = ResponseHandler.generateResponse(true, HttpStatus.OK, profile);
			return profile_response;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/getFarmTeachingSubjects/{teachingSubjectId}")
	public ResponseEntity<Object> get(@PathVariable Integer teachingSubjectId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				FarmTeachingSubjects profile = fts_Service.get(teachingSubjectId);
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

	@GetMapping("/fetchAllFarmTeachingSubjects")
	public ResponseEntity<Object> fetchAllFarmTeachingSubjects(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword)
			throws JsonParseException, JsonMappingException, IOException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> profile_filtered = fts_Service.getAllDataFilteredByKeyword(pageable, keyword);
				return profile_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> profile_sorted = fts_Service.getAllSortedData(pageable1);
				return profile_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateFarmTeachingSubjects/{teachingSubjectId}")
	public ResponseEntity<Object> update(@RequestBody @Valid FarmTeachingSubjects pr,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
				FarmTeachingSubjects profile_req = fts_Service.updateFarmTeachingSubjects(pr);

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

	@DeleteMapping("/deactivateFarmTeachingSubjects/{teachingSubjectId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer teachingSubjectId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			fts_Service.delete(teachingSubjectId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateFarmTeachingSubjects/{teachingSubjectId}")
	public ResponseEntity<Object> activate(@PathVariable Integer teachingSubjectId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			fts_Service.delete1(teachingSubjectId);
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
