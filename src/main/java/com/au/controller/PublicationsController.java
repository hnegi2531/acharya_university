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
import com.au.model.Publications;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.PublicationsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class PublicationsController {

	Logger log = LoggerFactory.getLogger(PublicationsController.class);
	
	@Autowired
	private PublicationsService publication_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/savePublication")
	public ResponseEntity<Object> savePublication(@RequestBody @Valid List<Publications> publications,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			publications.stream().forEach(pub -> {
				pub.setCreated_by(jwtDetails.getUserId());
				pub.setCreated_username(jwtDetails.getUserName());
			});
			List<Publications> publication = publication_service.savePublication(publications);
			ResponseEntity<Object> publications_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, publication);
		return publications_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getAllActivePublication")
	public ResponseEntity<Object> getAllActivePublication() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Publications> publication = publication_service.getAllActivePublication();
		ResponseEntity<Object>publication_response= ResponseHandler.generateResponse(true, HttpStatus.OK, publication);
		return publication_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getPublications/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	Publications publication = publication_service.get(id);
	    	ResponseEntity<Object> publication_response= ResponseHandler.generateResponse(true, HttpStatus.OK, publication);
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
	
	@PutMapping("/updatePublication/{id}")
	public ResponseEntity<Object> updatePublication(@RequestBody @Valid Publications publication,
			@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	publication.setModified_by(jwtDetails.getUserId());
	    	publication.setModified_username(jwtDetails.getUserName());
	    	publication_service.updatePublication(publication);
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
	
	@DeleteMapping("/deActivatePublication/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			publication_service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activatePublication/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			publication_service.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


//	@GetMapping("/fetchAllPublication")
//	public ResponseEntity<Object> fetchAllAdministrationEmailIdsDetail(@RequestParam(value = "page") Integer page,
//																	   @RequestParam(value = "page_size") Integer page_size,
//																	   @RequestParam(value = "sort") String sort, @RequestParam(value = "keyword", required = false) Object keyword,
//																	   @RequestParam(value = "percentageFilter") Integer percentageFilter) {
//
//		if(RateLimitController.bucket.tryConsume(1)) {
//		Sort sorted = Sort.by(Direction.DESC, sort );
//		if(keyword != null) {	
//			Pageable pageable = PageRequest.of(page, page_size,sorted);
//			System.out.println("page, page_size, sorted, keyword");
//			ResponseEntity<Object> oc_filtered =  publication_service.getAllDataFilteredByKeyword(pageable, keyword,percentageFilter);
//			return oc_filtered;
//		} else {
//			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
//			System.out.println("page, page_size, sorted");
//			ResponseEntity<Object> oc_sorted = publication_service.getAllSortedData(pageable1,percentageFilter);
//			return oc_sorted;
//		}
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}	
	
	
	@PostMapping(value = "/publicationUploadFile")
	public ResponseEntity<Object> uploadFile(@RequestParam MultipartFile multipartFile,
			@RequestParam Integer publications_id) throws IOException {
		publication_service.uploadFile(multipartFile, publications_id);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}	
	
	
	@GetMapping(path = "/publicationsFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = publication_service.viewFiles(fileName);
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
	
	@GetMapping("/publicationDetailsBasedOnEmpId/{emp_id}")
	public ResponseEntity<Object> publicationDetailsBasedOnEmpId(@PathVariable List<Integer> emp_id,@RequestParam(value="percentageFilter") Integer percentageFilter) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> shift_details = publication_service.publicationDetailsBasedOnEmpId(emp_id,percentageFilter);
			ResponseEntity<Object> shift_details_data = ResponseHandler.generateResponse(true, HttpStatus.OK, shift_details);
			return shift_details_data;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/publicationBasedOnEmpId")
	public ResponseEntity<Object> publicationBasedOnEmpId(@RequestParam(value="emp_id",required = false) Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> shift_details = publication_service.publicationBasedOnEmpId(emp_id);
			ResponseEntity<Object> shift_details_data = ResponseHandler.generateResponse(true, HttpStatus.OK, shift_details);
			return shift_details_data;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllPublication")
	public ResponseEntity<Object> fetchAllPublication(@RequestParam(value="percentageFilter") Integer percentageFilter) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> shift_details = publication_service.fetchAllPublication(percentageFilter);
			ResponseEntity<Object> shift_details_data = ResponseHandler.generateResponse(true, HttpStatus.OK, shift_details);
			return shift_details_data;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
