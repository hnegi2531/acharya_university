package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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

import com.au.dto.JwtDetails;
import com.au.dto.ResignationAttachmentDto;
import com.au.model.Resignation;
import com.au.model.ResignationAttachment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ResignationAttachmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class ResignationAttachmentController {
	
Logger log = LoggerFactory.getLogger(ResignationAttachmentController.class);
	
	@Autowired
	private ResignationAttachmentService ra_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@GetMapping(path = "/resignationFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = ra_service.viewFiles(fileName);
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
	
	@PostMapping(value="/uploadFileResignationAttachment")
	public ResponseEntity<Object> uploadFileResignationAttachment(@ModelAttribute ResignationAttachmentDto rad)
			throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			log.debug("Message For resignation Attachment");
			ra_service.uploadFileResignationAttachment(rad);
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}	
	

	@DeleteMapping("/resignationAttachment/{resignation_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer resignation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ra_service.deactivated(resignation_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
	}

	@DeleteMapping("/activateResignationAttachment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ra_service.activated(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}		
	}
	
	@GetMapping("/getResignationAttachment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				ResignationAttachment product = ra_service.get(id);
				ResponseEntity<Object> resignation_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return resignation_response_by_id;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}		
	}	
	
	@GetMapping("/getResignationAttachmentBasedOnEmployeeId/{emp_id}")
	public ResponseEntity<Object> getResignationAttachmentBasedOnEmployeeId(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<String, Object> employee = ra_service.getResignationAttachmentBasedOnEmployeeId(emp_id);
			ResponseEntity<Object> employee_response= ResponseHandler.generateResponse(true, HttpStatus.OK, employee);
			return employee_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	

}
