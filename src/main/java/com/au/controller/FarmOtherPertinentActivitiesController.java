package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.FarmUploads;
import com.au.dto.JobFileRequest;
import com.au.dto.JwtDetails;
import com.au.model.FarmOtherPertinentActivities;
import com.au.response.ResponseHandler;
import com.au.service.FarmOtherPertinentActivitiesService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class FarmOtherPertinentActivitiesController {
	
	Logger log = LoggerFactory.getLogger(FarmOtherPertinentActivitiesController.class);

	@Autowired
	private FarmOtherPertinentActivitiesService op_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/createFarmOtherPertinentActivities")
	public ResponseEntity<Object> createFarmOtherPertinentActivities(@RequestBody @Valid List<FarmOtherPertinentActivities> fts,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			fts.stream().forEach(farm -> {
				farm.setCreatedBy(jwtDetails.getUserId());
				farm.setCreatedUsername(jwtDetails.getUserName());
			});

			List<FarmOtherPertinentActivities> profile = op_Service.createFarmOtherPertinentActivities(fts);
			ResponseEntity<Object> profileresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					profile);
			return profileresponse;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/allActiveFarmOtherPertinentActivities")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<FarmOtherPertinentActivities> profile = op_Service.listAll();
			ResponseEntity<Object> profile_response = ResponseHandler.generateResponse(true, HttpStatus.OK, profile);
			return profile_response;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/getFarmOtherPertinentActivities/{otherPertinentId}")
	public ResponseEntity<Object> get(@PathVariable Integer otherPertinentId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				FarmOtherPertinentActivities profile = op_Service.get(otherPertinentId);
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

	@GetMapping("/fetchAllFarmOtherPertinentActivities")
	public ResponseEntity<Object> fetchAllFarmOtherPertinentActivities(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword)
			throws JsonParseException, JsonMappingException, IOException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> profile_filtered = op_Service.getAllDataFilteredByKeyword(pageable, keyword);
				return profile_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> profile_sorted = op_Service.getAllSortedData(pageable1);
				return profile_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateFarmOtherPertinentActivities/{otherPertinentId}")
	public ResponseEntity<Object> update(@RequestBody @Valid FarmOtherPertinentActivities pr,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
				FarmOtherPertinentActivities profile_req = op_Service.updateFarmOtherPertinentActivities(pr);

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

	@DeleteMapping("/deactivateFarmOtherPertinentActivities/{otherPertinentId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer otherPertinentId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			op_Service.delete(otherPertinentId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateFarmOtherPertinentActivities/{otherPertinentId}")
	public ResponseEntity<Object> activate(@PathVariable Integer otherPertinentId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			op_Service.delete1(otherPertinentId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping(value = "/farmOtherPertinentActivitiesUploadFile")
	public ResponseEntity<Object> farmOtherPertinentActivitiesUploadFile(@ModelAttribute FarmUploads pr) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		op_Service.uploadFile(pr.getFile(), pr.getOtherPertinentId());
		 ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	
	@GetMapping(path = "/farmOtherPertinentActivitiesFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = op_Service.viewFiles(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	
}
