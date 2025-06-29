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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.au.dto.VendorFileRequest;

import com.au.model.VendorAttachment;
import com.au.response.ResponseHandler;
import com.au.service.VendorAttachmentService;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey5}")
@CrossOrigin
public class VendorAttachmentController {
	
		Logger log = LoggerFactory.getLogger(VendorAttachmentController.class);
		
		@Autowired
		VendorAttachmentService ven_service;
		
		
		@PostMapping("/vendorAttachment")
		public ResponseEntity<Object> saveVendorAttachments(@RequestBody @Valid VendorAttachment vendor_attachments) throws IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
			log.debug("Request {}", vendor_attachments);
			VendorAttachment va = ven_service.saveAttachments(vendor_attachments);
			ResponseEntity<Object> va_response= ResponseHandler.generateResponse(true, HttpStatus.OK, va);
			return va_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
		}
		
		@PostMapping(value="/vendorUploadFile")
		public ResponseEntity<Object> uploadFile(@ModelAttribute VendorFileRequest vendorfilerequest) throws IOException{
			if(RateLimitController.bucket.tryConsume(1)) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				System.out.println("Hello-------" + auth.getDetails());
				log.debug("Message For VendorAttachment");
				VendorAttachment va = ven_service.uploadFile(vendorfilerequest.getFile() , vendorfilerequest.getVendor_id() );
				ResponseEntity<Object> va_response= ResponseHandler.generateResponse(true, HttpStatus.OK, va);
				return va_response;
				} else {
					ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
					return rs;
				}
			
		}
		
		@GetMapping(path = "/vendorFileDownload")
		public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileName") final String pathName) {
			try {
				final byte[] data = ven_service.downloadFile(pathName);
				final ByteArrayResource resource = new ByteArrayResource(data);
				return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
						.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
						.header("Cache-Control", "no-cache").body(resource);
			} catch (NoSuchFileException e) {
				return ResponseEntity.notFound().build();
			} catch (Exception e) {
				log.error(e.getMessage());
				return ResponseEntity.badRequest().contentLength(0).body(null);
			}
		}
		
		@GetMapping(value="/vendorAttachment/{vendor_id}")
		public ResponseEntity<Object> get(@PathVariable Integer vendor_id ){
			if(RateLimitController.bucket.tryConsume(1)) {
			try {
				VendorAttachment details=ven_service.get(vendor_id);
				log.debug("Request {}", vendor_id);
				ResponseEntity<Object> va_response= ResponseHandler.generateResponse(true, HttpStatus.OK, details);
				return va_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
		}
		
		@GetMapping(value="/vendorAttachmentDetails/{vendor_id}")
		public ResponseEntity<Object> vendorAttachmentDetails(@PathVariable Integer vendor_id) throws Exception{
			if(RateLimitController.bucket.tryConsume(1)) {
				try {
					List<Map<String, Object>> emp_report = ven_service.vendorAttachmentDetails(vendor_id);
					ResponseEntity<Object> emp_report_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp_report);
					return emp_report_response;

				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response;
				}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}

}
