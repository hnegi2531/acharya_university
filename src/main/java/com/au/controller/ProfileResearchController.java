package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
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

import com.au.dto.JobFileRequest;
import com.au.dto.JwtDetails;
import com.au.dto.MaintenanceDto;
import com.au.dto.ProfileResearchDto;
import com.au.model.ProfileResearch;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProfileResearchService;
import com.au.service.StoreIndentRequestService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@CrossOrigin
@RequestMapping("/api/${secretkey2}")
public class ProfileResearchController {
	
Logger log = LoggerFactory.getLogger(StoreIndentRequestController.class);
	
 	@Autowired
    private ProfileResearchService pr_Service;

    @Autowired
    private JwtTokenService jwt_service;

    
    @PostMapping("/createProfileResearch")
	public ResponseEntity<Object> createProfileResearch(@RequestBody @Valid ProfileResearchDto pr,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			pr.setCreatedBy(jwtDetails.getUserId());
			pr.setCreatedUsername(jwtDetails.getUserName());

		List<ProfileResearch> profile = pr_Service.createProfileResearch(pr);
		ResponseEntity<Object> profileresponse= ResponseHandler.generateResponse(true, HttpStatus.CREATED, profile);
		return profileresponse;
	} else {
		ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return response;
		}
	}
    
	@GetMapping("/allActiveProfileResearch")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ProfileResearch> profile = pr_Service.listAll();
		ResponseEntity<Object> profile_response= ResponseHandler.generateResponse(true, HttpStatus.OK, profile);
		return profile_response;
	} else {
		ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return response;
	}
	}
	
	
	@GetMapping("/getProfileResearch/{profileResearchId}")
	public ResponseEntity<Object> get(@PathVariable Integer profileResearchId) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	ProfileResearch profile = pr_Service.get(profileResearchId);
	    	ResponseEntity<Object> profile_response= ResponseHandler.generateResponse(true, HttpStatus.OK, profile);
			return profile_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllProfileResearch")
	public ResponseEntity<Object> fetchAllProfileResearch(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) throws JsonParseException, JsonMappingException, IOException {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> profile_filtered =  pr_Service.getAllDataFilteredByKeyword(pageable, keyword);
			return profile_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> profile_sorted = pr_Service.getAllSortedData(pageable1);
			return profile_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllProfileResearchForEmployee")
	public ResponseEntity<Object> fetchAllProfileResearchForEmployee(@RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		
		if(RateLimitController.bucket.tryConsume(1)) {
		    try {
		    	
		    	List<HashMap<String, Object>> profile = pr_Service.fetchAllProfileResearchForEmployee(jwtDetails.getUserId());
		    	ResponseEntity<Object> profile_response= ResponseHandler.generateResponse(true, HttpStatus.OK, profile);
				return profile_response;
		    } catch (NoSuchElementException e) {
		    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}
	
	@PutMapping("/updateProfileResearch/{profileResearchId}")
	public ResponseEntity<Object> update(@RequestBody @Valid ProfileResearch pr, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			  try {
	    	ProfileResearch profile_req =	pr_Service.updateProfileResearch(pr);
	    	
	    	ResponseEntity<Object> profile_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return profile_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/deactivateProfileResearch/{profileResearchId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer profileResearchId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			pr_Service.delete(profileResearchId);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateProfileResearch/{profileResearchId}")
	public ResponseEntity<Object> activate(@PathVariable Integer profileResearchId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			pr_Service.delete1(profileResearchId);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping(value = "/profileResearchUploadFile")
	public ResponseEntity<Object> profileResearchUploadFile(@ModelAttribute JobFileRequest pr) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		pr_Service.uploadFile(pr.getFile(), pr.getProfileResearchId());
		 ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	
	@GetMapping(path = "/profileResearchFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = pr_Service.viewFiles(fileName);
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
