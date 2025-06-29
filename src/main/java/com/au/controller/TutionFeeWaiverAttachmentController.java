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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.TutionFeeWaiverAttachmentFileRequestDto;
import com.au.model.TutionFeeWaiverAttachment;

import com.au.service.TutionFeeWaiverAttachmentService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TutionFeeWaiverAttachmentController {
	
	Logger log = LoggerFactory.getLogger(TutionFeeWaiverAttachmentController.class);
	
	@Autowired
	private TutionFeeWaiverAttachmentService tut_fee_atch_ser;
	
	@PostMapping("/tutionFeeWaiverAttachment")
	public TutionFeeWaiverAttachment saveTutionFeeWaiverAttachment(@RequestBody @Valid TutionFeeWaiverAttachment tut_fee_attachment) throws IOException {
		log.debug("Request {}", tut_fee_attachment);
		return tut_fee_atch_ser.saveAttachments(tut_fee_attachment);
	
	}
	
	@PostMapping("/tutionFeeWaiverUploadFile")
	public TutionFeeWaiverAttachment uploadFileTutionFeeWaiver(@ModelAttribute TutionFeeWaiverAttachmentFileRequestDto tutionFeeWaiverfilerequest) throws IOException{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("TutionFeeWaiverAttachment");
		return tut_fee_atch_ser.uploadFileTutionFeeWaiver(tutionFeeWaiverfilerequest.getFile() , tutionFeeWaiverfilerequest.getTution_fee_waiver_id());
		
	}
	
	@GetMapping(path = "/tutionFeeWaiverFileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileName") final String pathName) {
		try {
			final byte[] data = tut_fee_atch_ser.downloadFile(pathName);
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
	
	@GetMapping(value="/tutionFeeWaiverAttachment/{tution_fee_waiver_id}")
	public ResponseEntity<TutionFeeWaiverAttachment> get(@PathVariable Integer tution_fee_waiver_id ){
		try {
			TutionFeeWaiverAttachment details=tut_fee_atch_ser.get(tution_fee_waiver_id);
			log.debug("Request {}", tution_fee_waiver_id);
			return new ResponseEntity<TutionFeeWaiverAttachment>(details,HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<TutionFeeWaiverAttachment>(HttpStatus.NOT_FOUND);
		
		}
	
	}
	

}
