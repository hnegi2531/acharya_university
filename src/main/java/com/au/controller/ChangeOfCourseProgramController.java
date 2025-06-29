package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.au.dto.ChangeOfCourseProgramAttachmentRequest;
import com.au.dto.ChangeOfCourseProgramDto;
import com.au.response.ResponseHandler;
import com.au.service.ChangeOfCourseProgramService;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class ChangeOfCourseProgramController {
	
	Logger log = LoggerFactory.getLogger(ChangeOfCourseProgramController.class);
	
	@Autowired
	private ChangeOfCourseProgramService changeOfCourseProgramService;

	
	
	@PostMapping("/changeOfCourseProgram")
	public ResponseEntity<Object> changeOfCourseProgram(@RequestBody @Valid ChangeOfCourseProgramDto  changeOfCourseProgramDto,
			@RequestHeader("Authorization") String jwtToken){
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				return  changeOfCourseProgramService.changeOfCourseProgram(changeOfCourseProgramDto, jwtToken);
			}catch(Exception e) {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
						e.getMessage());
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	
	@PostMapping(value="/changeOfCourseProgramUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute ChangeOfCourseProgramAttachmentRequest attachmentfilerequest,@RequestHeader("Authorization") String jwtToken) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			SecurityContextHolder.getContext().getAuthentication();
			log.debug("Message For VendorAttachment");
			changeOfCourseProgramService.uploadFile(attachmentfilerequest,attachmentfilerequest.getFile() , attachmentfilerequest.getNewStudentId(),jwtToken );
			return  ResponseHandler.generateResponseForPutApiAndDeleteApi(true,HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping(path = "/changeOfCourseProgramFileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("changeOfCourseProgramAttachmentPath") final String pathName) {
		try {
			final byte[] data = changeOfCourseProgramService.downloadFile(pathName);
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
	
	@GetMapping(value="/changeOfCourseProgramAttachmentDetail/{studentId}")
	public ResponseEntity<Object> changeOfCourseProgramAttachmentDetail(@PathVariable Integer studentId){
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				return changeOfCourseProgramService.changeOfCourseProgramAttachmentDetail(studentId);

			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping(value="/initiatedChangeOfCourseProgramStudentDetails")
	public ResponseEntity<Object> initiatedChangeOfCourseProgramStudentDetails(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer pageSize,
	@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, pageSize,sorted);
				return changeOfCourseProgramService.initiatedChangeOfCourseProgramStudentDetailsSerach(pageable, keyword);
			}else {
				Pageable pageable1 = PageRequest.of(page, pageSize,sorted);
				return changeOfCourseProgramService.initiatedChangeOfCourseProgramStudentDetailsSorting(pageable1);
			}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@PutMapping(value="/approveChangeOfCourseProgramRequest")
	public ResponseEntity<Object> approveChangeOfCourseProgramRequest(@RequestBody @Valid ChangeOfCourseProgramDto  changeOfCourseProgramDto,@RequestHeader("Authorization") String jwtToken){
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				return changeOfCourseProgramService.approveChangeOfCourseProgramRequest(changeOfCourseProgramDto,jwtToken);

			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}

}
