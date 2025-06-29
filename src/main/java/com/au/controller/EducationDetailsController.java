package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.au.dto.EducationDetailsAttachmentsDto;
import com.au.model.EducationDetails;
import com.au.model.EducationDetailsAttachment;
import com.au.response.ResponseHandler;
import com.au.service.EducationDetailsService;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class EducationDetailsController {

	Logger log = LoggerFactory.getLogger(EducationDetailsController.class);

	@Autowired
	private EducationDetailsService eds_service;

	@GetMapping("/getEducationDocsAttachmentById/{empId}")
	public ResponseEntity<?> getEmployeeIDsAttachmentByEmpId(@PathVariable("empId") Integer empId ){
		List<EducationDetailsAttachment> obj=	eds_service.getEducationDocsByEmpId(empId);
		return new ResponseEntity<>(obj, HttpStatus.OK);
		
	}
	
	
	@DeleteMapping("/educationDocsAttachmentDeactivate/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			eds_service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping("/EducationDetails")
	public List<EducationDetails> saveEducationDetails(@RequestBody @Valid List<EducationDetails> educationdetails) {
		log.debug("Request {}", educationdetails);
		return eds_service.saveEducationDetails(educationdetails);
	}
	
	
	@PostMapping("/newEducationDetails")
	public List<EducationDetails> newsaveEducationDetails(@RequestBody @Valid List<EducationDetails> educationdetails) {
		log.debug("Request {}", educationdetails);
		return eds_service.newsaveEducationDetails(educationdetails);
	}

	@PostMapping(value="/uploadEducationDetailsAttachment")
	public ResponseEntity<Object> uploadCandidateAttachment(@ModelAttribute EducationDetailsAttachmentsDto cwad) throws IOException{

		  EducationDetailsAttachment list=  eds_service.uploadFile(cwad);
//			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(list, HttpStatus.OK);
			return new ResponseEntity<>(list, HttpStatus.OK);

			}
		
	@GetMapping(path = "/fileDownloadOfEducationDetailsAttachment")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("attachment_path") final String attachment_path) {
		try {
			final byte[] data = eds_service.downloadFile(attachment_path);
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
	
	@GetMapping("/getEducationDetails")
	public List<EducationDetails> listAll() {
		return eds_service.listAll();

	}

	@GetMapping("/getEducationDetails/{id}")
	public ResponseEntity<EducationDetails> get(@PathVariable Integer id) {
		try {

			EducationDetails product = eds_service.get(id);
			log.debug("Request {}", id);
			return new ResponseEntity<EducationDetails>(product, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<EducationDetails>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/EducationDetails/{id}")
	public ResponseEntity<EducationDetails> update(@RequestBody @Valid List<EducationDetails> educationdetails,
			@PathVariable Integer id) {
		try {
			EducationDetails existProduct = eds_service.get(id);
			eds_service.saveEducationDetails(educationdetails);
			log.debug("Request {}", educationdetails);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/EducationDetails/{id}")
	public void delete(@PathVariable Integer id) {
		eds_service.delete(id);
	}
	
	@GetMapping("/fetchEducationDetails/{job_id}")
	public List<HashMap<String, Object>> fetchEducationDetails(@PathVariable Integer job_id) {
		return eds_service.fetchEducationDetails(job_id);
	}

	
	@PostMapping("/createEducationDetails")
	public ResponseEntity<Object> createEducationDetails(@RequestBody @Valid List<EducationDetails> ed)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			EducationDetails expDetails = new EducationDetails();
			ed.stream().forEach(f -> {
					f.setEdu_id(expDetails.getEdu_id());
			});
			List<EducationDetails> cos = eds_service.createEducationDetails(ed);
			ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cos);
		return co_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
