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
import com.au.model.StudentAttachments;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StudentAttachmentsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentAttachmentsController {

	@Autowired
	private StudentAttachmentsService s_service;
	
	Logger log = LoggerFactory.getLogger(StudentAttachmentsController.class);
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/StudentAttachments")
	public ResponseEntity<Object> saveStudentAttachments(@RequestBody @Valid StudentAttachments s,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			s.setCreated_by(jwtDetails.getUserId());
			s.setCreated_username(jwtDetails.getUserName());
			StudentAttachments sa = s_service.saveStudentAttachments(s);
			ResponseEntity<Object> sa_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, sa);
			return sa_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/StudentAttachments")
	public ResponseEntity<Object> listAll(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<StudentAttachments> sa_list = s_service.listAll();
			ResponseEntity<Object> sa_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sa_list);
			return sa_list_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	
	@GetMapping("/StudentAttachments/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	StudentAttachments sa = s_service.get(id);
	    	ResponseEntity<Object> sa_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sa);
			return sa_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}

	@PutMapping("/StudentAttachments/{id}")
	public ResponseEntity<Object> update(@RequestBody StudentAttachments s, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	StudentAttachments existProduct = s_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			s.setModified_by(jwtDetails.getUserId());
			s.setModified_username(jwtDetails.getUserName());
	    	s_service.saveStudentAttachments(s);
	    	ResponseEntity<Object> sa_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return sa_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/StudentAttachments/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		s_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PostMapping(value = "/uploadStudentPersonalFiles")
	public ResponseEntity<Object> uploadFile(@RequestParam(value = "attachmentSubType", required = false) String subType,
	                                         @RequestParam MultipartFile multipartFile,
	                                         @RequestParam String lead_id,
	                                         @RequestParam String opportunity_id) throws IOException {

	    s_service.uploadFile(subType, multipartFile,lead_id, opportunity_id);
	    ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
	    return response;
	}


	
	@GetMapping(path = "/viewStudentFiles")
	public ResponseEntity<ByteArrayResource> viewStudentFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = s_service.viewFiles(fileName);

			// Extract file extension to determine content type
			String contentType = determineContentType(fileName);

			final ByteArrayResource resource = new ByteArrayResource(data);

			return ResponseEntity.ok().contentLength(data.length).header("Content-Type", contentType) 
					.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}

	private String determineContentType(String fileName) {
        String fileExtension = getFileExtension(fileName);

        switch (fileExtension.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                return "application/octet-stream";  // Fallback content type
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }

	
}
