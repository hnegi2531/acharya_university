package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.au.dto.EventImageAttachmentsRequest;
import com.au.dto.JwtDetails;
import com.au.model.EmployeeDetails;
import com.au.model.EventImageAttachments;
import com.au.response.ResponseHandler;
import com.au.service.EventImageAttachmentsService;
import com.au.service.JwtTokenService;

@RestController
@RequestMapping("/api/${secretkey4}")
@CrossOrigin
public class EventImageAttachmentsController {
	
	Logger log = LoggerFactory.getLogger(EventImageAttachmentsController.class);
	
	@Autowired
	private EventImageAttachmentsService event_image_attch_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping(value="/eventImageAttachmentsUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute EventImageAttachmentsRequest eiar,@RequestHeader("Authorization") String jwtToken) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Hello-------" + auth.getDetails());
			log.debug("Message For Event Image Attachments");
			event_image_attch_ser.uploadFile(eiar.getFile() , eiar.getEvent_id(),eiar.getImage_upload_timing(),jwtDetails,eiar.getActive());
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}
	
	@GetMapping(path = "/eventImageAttachmentsImageDownload")
	public ResponseEntity<ByteArrayResource> downloadImageFile(@RequestParam("event_image_path") final String event_image_path) {
		
			try {
					final byte[] data = event_image_attch_ser.downloadFile(event_image_path);
					final ByteArrayResource resource = new ByteArrayResource(data);
					return ResponseEntity.ok().contentLength(data.length).header("Content-type","image/jpeg")
					.header("Content-disposition", "attachment; filename=\"" + event_image_path + "\"")
					.header("Cache-Control", "no-cache").body(resource);
			} catch (NoSuchFileException e) {
					return ResponseEntity.notFound().build();
			} catch (Exception e) {
					log.error(e.getMessage());
					return ResponseEntity.badRequest().contentLength(0).body(null);
			}
			
	}
	
	@GetMapping("/eventImageAttachmentsDetails/{event_id}")
	public ResponseEntity<Object> eventImageAttachmentsDetails(@PathVariable Integer event_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EventImageAttachments> event_image_attchment_details = event_image_attch_ser.eventImageAttachmentsDetails(event_id);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK,event_image_attchment_details);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

}
