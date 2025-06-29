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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.JobFileRequest;
import com.au.model.Attachments;
import com.au.repository.JobProfileRepository;
import com.au.service.AttachmentsService;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class AttachmentsController {

	Logger log = LoggerFactory.getLogger(AttachmentsController.class);

	@Autowired
	private AttachmentsService as_service;
	
	@Autowired
	private JobProfileRepository jpr_repo;

	@PostMapping("/Attachments")
	public Attachments saveAttachments(@RequestBody @Valid Attachments attachments) throws IOException {
		log.debug("Request {}", attachments);
		return as_service.saveAttachments(attachments);
	}

//	FOR DATA UPLOAD FROM BACKEND USE THIS API.
	@PostMapping(value = "/JobUploadFileForData")
	public String uploadFile1(@ModelAttribute JobFileRequest jobfilerequest) throws Exception {
//		jobfilerequest.setJob_id(jpr_repo.getLatestJobId());
//		System.out.println("fsdfsfsdfgssgsfg" +jobfilerequest.getJob_id());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		//System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		return as_service.uploadFile(jobfilerequest.getFile(), jobfilerequest.getJob_id());
	}
	
	@PostMapping(value = "/JobUploadFile")
	public String uploadFile(@ModelAttribute JobFileRequest jobfilerequest) throws Exception {
		jobfilerequest.setJob_id(jpr_repo.getLatestJobId());
		System.out.println("fsdfsfsdfgssgsfg" +jobfilerequest.getJob_id());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		//System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		return as_service.uploadFileForPhp(jobfilerequest.getFile(), jobfilerequest.getJob_id());
	}

	@GetMapping(path = "/jobFileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = as_service.downloadFile(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}

	@GetMapping(path = "/jobFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = as_service.viewFiles(fileName);
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

	@GetMapping("/Attachments")
	public List<Attachments> listAll() {
		return as_service.listAll();

	}

	@GetMapping("/Attachments/{id}")
	public ResponseEntity<Attachments> get(@PathVariable Integer id) {
		try {

			Attachments product = as_service.get(id);
			log.debug("Request {}", id);
			return new ResponseEntity<Attachments>(product, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<Attachments>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/Attachments/{id}")
	public ResponseEntity<Attachments> update(@RequestBody @Valid Attachments attachments, @PathVariable Integer id)
			throws IOException {
		try {
			// Attachments existProduct = as_service.get(id);
			as_service.saveAttachments(attachments);
			log.debug("Request {}", attachments);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/Attachments/{id}")
	public void delete(@PathVariable Integer id) {
		as_service.delete(id);
	}

	@GetMapping("/getAttachmentPathDeatils/{job_id}")
	public Attachments getAllDeatils(@PathVariable Integer job_id) {
		return as_service.getAllDeatils(job_id);
	}

}
