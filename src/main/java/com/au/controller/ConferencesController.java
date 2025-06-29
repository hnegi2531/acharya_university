package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.JwtDetails;
import com.au.model.Conferences;
import com.au.model.Publications;
import com.au.response.ResponseHandler;
import com.au.service.ConferencesService;
import com.au.service.JwtTokenService;
import com.au.service.PublicationsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class ConferencesController {

	Logger log = LoggerFactory.getLogger(ConferencesController.class);
	
	@Autowired
	private ConferencesService conference_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	
	@PostMapping("/saveConferences")
	public ResponseEntity<Object> savePublication(@RequestBody @Valid List<Conferences> conferences,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			conferences.stream().forEach(con -> {
				con.setCreated_by(jwtDetails.getUserId());
				con.setCreated_username(jwtDetails.getUserName());
			});
			List<Conferences> conference = conference_service.saveConferences(conferences);
			ResponseEntity<Object> conferences_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, conference);
		return conferences_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllActiveConferences")
	public ResponseEntity<Object> getAllActiveConferences() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Conferences> conference = conference_service.getAllActiveConferences();
		ResponseEntity<Object>conference_response= ResponseHandler.generateResponse(true, HttpStatus.OK, conference);
		return conference_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getConferences/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	Conferences conference = conference_service.get(id);
	    	ResponseEntity<Object> conference_response= ResponseHandler.generateResponse(true, HttpStatus.OK, conference);
			return conference_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/updateConferences/{id}")
	public ResponseEntity<Object> updateConferences(@RequestBody @Valid Conferences conferences,
			@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	conferences.setModified_by(jwtDetails.getUserId());
	    	conferences.setModified_username(jwtDetails.getUserName());
	    	conference_service.updateConferences(conferences);
	    	ResponseEntity<Object> publication_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return publication_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@DeleteMapping("/deActivateConferences/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			conference_service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateConferences/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			conference_service.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


	@GetMapping("/fetchAllConferences")
	public ResponseEntity<Object> fetchAllConferences(@RequestParam(value="percentageFilter") Integer percentageFilter) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> con_details = conference_service.fetchAllConferences(percentageFilter);
			ResponseEntity<Object> con_detail = ResponseHandler.generateResponse(true, HttpStatus.OK, con_details);
			return con_detail;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/conferenceDetailsBasedOnEmpId/{emp_id}")
	public ResponseEntity<Object> conferenceDetailsBasedOnEmpId(@PathVariable List<Integer> emp_id,@RequestParam(value="percentageFilter") Integer percentageFilter) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> con_details = conference_service.conferenceDetailsBasedOnEmpId(emp_id,percentageFilter);
			ResponseEntity<Object> con_detail = ResponseHandler.generateResponse(true, HttpStatus.OK, con_details);
			return con_detail;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/conferenceBasedOnEmpId")
	public ResponseEntity<Object> conferenceBasedOnEmpId(@RequestParam(value="emp_id",required = false) Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> shift_details = conference_service.conferenceBasedOnEmpId(emp_id);
			ResponseEntity<Object> shift_details_data = ResponseHandler.generateResponse(true, HttpStatus.OK, shift_details);
			return shift_details_data;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PostMapping(value = "/conferenceUploadFile")
	public ResponseEntity<Object> uploadFile(@RequestParam MultipartFile multipartFile,
			@RequestParam Integer conferences_id) throws IOException {
		conference_service.uploadFile(multipartFile, conferences_id);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}	
	
	
	@GetMapping(path = "/conferenceFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = conference_service.viewFiles(fileName);
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
	
	
	@PostMapping(value = "/conferenceCertificateUploadFile")
	public ResponseEntity<Object> uploadFileCertificate(@RequestParam MultipartFile multipartFile,
			@RequestParam Integer conferences_id) throws IOException {
		conference_service.uploadFileCertificate(multipartFile, conferences_id);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	
	@GetMapping(path = "/conferenceCertificateFileviews")
	public ResponseEntity<ByteArrayResource> certificateviewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = conference_service.conferenceCertificateFileviews(fileName);
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
