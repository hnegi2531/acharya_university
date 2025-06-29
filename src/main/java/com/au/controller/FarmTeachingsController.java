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
import com.au.dto.JwtDetails;
import com.au.model.FarmTeachings;
import com.au.response.ResponseHandler;
import com.au.service.FarmTeachingsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class FarmTeachingsController {
	
	Logger log = LoggerFactory.getLogger(FarmTeachingsController.class);
	
	@Autowired
    private FarmTeachingsService ft_Service;

    @Autowired
    private JwtTokenService jwt_service;

    
    @PostMapping("/createFarmTeachings")
	public ResponseEntity<Object> createFarmTeachings(@RequestBody @Valid FarmTeachings fts,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			fts.setCreatedBy(jwtDetails.getUserId());
			fts.setCreatedUsername(jwtDetails.getUserName());

			FarmTeachings profile = ft_Service.createFarmTeachings(fts);
		ResponseEntity<Object> profileresponse= ResponseHandler.generateResponse(true, HttpStatus.CREATED, profile);
		return profileresponse;
	} else {
		ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return response;
		}
	}
    
	@GetMapping("/allActiveFarmTeachings")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<FarmTeachings> profile = ft_Service.listAll();
		ResponseEntity<Object> profile_response= ResponseHandler.generateResponse(true, HttpStatus.OK, profile);
		return profile_response;
	} else {
		ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return response;
	}
	}
	
	
	@GetMapping("/getFarmTeachings/{teachingsId}")
	public ResponseEntity<Object> get(@PathVariable Integer teachingsId) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	FarmTeachings profile = ft_Service.get(teachingsId);
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
	
	@GetMapping("/fetchAllFarmTeachings")
	public ResponseEntity<Object> fetchAllFarmTeachings(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) throws JsonParseException, JsonMappingException, IOException {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> profile_filtered =  ft_Service.getAllDataFilteredByKeyword(pageable, keyword);
			return profile_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> profile_sorted = ft_Service.getAllSortedData(pageable1);
			return profile_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/updateFarmTeachings/{teachingsId}")
	public ResponseEntity<Object> update(@RequestBody @Valid FarmTeachings pr, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			  try {
	    	FarmTeachings profile_req =	ft_Service.updateFarmTeachings(pr);
	    	
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
	
	
	@DeleteMapping("/deactivateFarmTeachings/{teachingsId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer teachingsId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ft_Service.delete(teachingsId);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateFarmTeachings/{teachingsId}")
	public ResponseEntity<Object> activate(@PathVariable Integer teachingsId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ft_Service.delete1(teachingsId);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping(value = "/farmTeachingsUploadFile")
	public ResponseEntity<Object> FarmTeachingsUploadFile(@ModelAttribute FarmUploads pr) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		ft_Service.uploadFile(pr.getFile(), pr.getTeachingsId());
		 ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	
	@GetMapping(path = "/farmTeachingsFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = ft_Service.viewFiles(fileName);
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
