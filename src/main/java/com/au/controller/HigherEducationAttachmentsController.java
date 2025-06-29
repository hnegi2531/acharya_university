package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
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

import com.au.dto.HigherEducationAttachmentDto;
import com.au.dto.JwtDetails;
import com.au.model.HigherEducationAttachments;
import com.au.service.HigherEducationAttachmentsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class HigherEducationAttachmentsController {
	
	Logger log = LoggerFactory.getLogger(HigherEducationAttachmentsController.class);
	
	@Autowired
	private HigherEducationAttachmentsService hea_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/higherEducationAttachment")
	public HigherEducationAttachments savehostelwaiverAttachments(@RequestBody @Valid HigherEducationAttachments he_attachments) throws IOException {
		log.debug("Request {}", he_attachments);
		return hea_ser.saveAttachments(he_attachments);
	}
	
	@PostMapping(value="/higherEducationUploadFile")
	public HigherEducationAttachments uploadFile(@ModelAttribute HigherEducationAttachmentDto hefilerequest) throws IOException{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//System.out.println("Hello-------***" + auth.getDetails());
		//log.debug("Message For higherEducationAttachment");
		return hea_ser.uploadFile(hefilerequest.getFile() ,hefilerequest.getJob_id());
		
	}
	
	@GetMapping(path = "/higherEducationFileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileName") final String pathName) {
		
		try {
			final byte[] data = hea_ser.downloadFile(pathName);
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
	
	@GetMapping(value="/higherEducationAttachment/{he_attachment_id}")
	public ResponseEntity<HigherEducationAttachments> get1(@PathVariable Integer he_attachment_id ) {
		try {
			HigherEducationAttachments details=hea_ser.get(he_attachment_id);
			log.debug("Request {}", he_attachment_id);
			return new ResponseEntity<HigherEducationAttachments>(details,HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<HigherEducationAttachments>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/higherEducationAttachment/{he_attachment_id}")
	public ResponseEntity<HigherEducationAttachments> updateHigherEducationAttachments(@RequestBody HigherEducationAttachments he,
			@PathVariable Integer he_attachment_id,@RequestHeader ("Authorization") String jwtToken) 
					throws Exception, JsonParseException, JsonMappingException, IOException {
		
		try {
			JwtDetails jwtdetails = jwt_service.callJwtToken(jwtToken);
			he.setModified_by(jwtdetails.getUserId());
			he.setModified_username(jwtdetails.getUserName());
			hea_ser.saveUpdateHigherEducationAttachments(he);
			return new ResponseEntity<HigherEducationAttachments>(HttpStatus.OK);
		}catch(NoSuchElementException e) {
			return new ResponseEntity<HigherEducationAttachments>(HttpStatus.NOT_FOUND);
		}
	}
	
	/*
	 * @DeleteMapping("/deactivatehigherEducationAttachment/{he_attachment_id}")
	 * public void deactivateHigherEducationAttachment(@PathVariable Integer
	 * he_attachment_id) {
	 * hea_ser.deactivateHigherEducationAttachment(he_attachment_id); }
	 * 
	 * @DeleteMapping("/activatehigherEducationAttachment/{he_attachment_id}")
	 * public void activateHigherEducationAttachment(@PathVariable Integer
	 * he_attachment_id) {
	 * hea_ser.activateHigherEducationAttachment(he_attachment_id); }
	 */
	
	@GetMapping("/getHigherEducationAttachmentPathDeatils/{job_id}")
	public HigherEducationAttachments getAllDeatils(@PathVariable Integer job_id) {
		return hea_ser.getAllDeatils(job_id);
	}

}
