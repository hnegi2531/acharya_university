package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.CandidateWalkinAttachmentsDto;
import com.au.model.CandidateWalkinAttachments;
import com.au.response.ResponseHandler;
import com.au.service.CandidateWalkinAttachmentsService;


@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class CandidateWalkinAttachmentsController {

Logger log = LoggerFactory.getLogger(CandidateWalkinAttachmentsController.class);
	
	@Autowired
	private CandidateWalkinAttachmentsService candidate_attachments_service;
	
	@PostMapping(value="/uploadCandidateAttachment")
	public ResponseEntity<Object> uploadCandidateAttachment(@ModelAttribute CandidateWalkinAttachmentsDto cwad) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			log.debug("Message For Candidate Attachment");
			candidate_attachments_service.uploadFile(cwad);
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}
	
	@GetMapping(path = "/fileDownloadOfCandidateAttachment")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("attachment_path") final String attachment_path) {
		try {
			final byte[] data = candidate_attachments_service.downloadFile(attachment_path);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + attachment_path + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@GetMapping(path = "/imageDownloadOfCandidateAttachment")
	public ResponseEntity<ByteArrayResource> imageDownloadOfCandidateAttachment(@RequestParam("attachment_path") final String attachment_path) {
		try {
			final byte[] data = candidate_attachments_service.downloadFile(attachment_path);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","image/jpeg")
					.header("Content-disposition", "attachment; filename=\"" + attachment_path + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@GetMapping(value="/CandidateAttachmentDetails/{candidate_id}")
	public ResponseEntity<Object> get(@PathVariable Integer candidate_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<CandidateWalkinAttachments> details=candidate_attachments_service.get(candidate_id);
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
	
	@GetMapping(value="/candidatePhotoAttachmentDetails")
	public ResponseEntity<Object> candidatePhotoAttachmentDetails(@RequestParam("application_no_npf") String application_no_npf){
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				Map<String,Object> details=candidate_attachments_service.candidatePhotoAttachmentDetails(application_no_npf);
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
	
	@PostMapping(value="/uploadCandidateAttachmentFromStudentProfile")
	public ResponseEntity<Object> uploadCandidateAttachmentFromStudentProfile(@ModelAttribute CandidateWalkinAttachmentsDto cwad) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			log.debug("Message For Candidate Attachment");
			candidate_attachments_service.uploadCandidateAttachmentFromProfile(cwad);
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}
		
}

