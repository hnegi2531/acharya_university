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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import com.au.model.Publications;
import com.au.model.StudentNoDue;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.PublicationsService;
import com.au.service.StudentNoDueService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentNoDueController {

	Logger log = LoggerFactory.getLogger(CourseObjectiveController.class);
	
	@Autowired
	private StudentNoDueService studentNoDueService;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/saveStudentNoDue")
	public ResponseEntity<Object> saveStudentNoDue(@RequestBody @Valid List<StudentNoDue> studentNoDue,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			studentNoDue.stream().forEach(snd -> {
				snd.setCreated_by(jwtDetails.getUserId());
				snd.setCreated_username(jwtDetails.getUserName());
			});
			List<StudentNoDue> noDue = studentNoDueService.saveStudentNoDue(studentNoDue);
			ResponseEntity<Object> noDue_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, noDue);
		return noDue_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getAllActiveStudentNoDue")
	public ResponseEntity<Object> getAllActiveStudentNoDue() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<StudentNoDue> studentNoDue = studentNoDueService.getAllActiveStudentNoDue();
		ResponseEntity<Object> studentNoDue_response= ResponseHandler.generateResponse(true, HttpStatus.OK, studentNoDue);
		return studentNoDue_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getStudentNoDue/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	StudentNoDue studentNoDue = studentNoDueService.get(id);
	    	ResponseEntity<Object> studentNoDue_response= ResponseHandler.generateResponse(true, HttpStatus.OK, studentNoDue);
			return studentNoDue_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/updateStudentNoDue/{id}")
	public ResponseEntity<Object> updateStudentNoDue(@RequestBody @Valid StudentNoDue studentNoDue,
			@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	studentNoDue.setModified_by(jwtDetails.getUserId());
	    	studentNoDue.setModified_username(jwtDetails.getUserName());
	    	studentNoDueService.updateStudentNoDue(studentNoDue);
	    	ResponseEntity<Object> publication_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return publication_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@DeleteMapping("/deActivateStudentNoDue/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			studentNoDueService.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateStudentNoDue/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			studentNoDueService.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/fetchAllStudentNoDue")
	public ResponseEntity<Object> fetchAllAdministrationEmailIdsDetail(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, 
			@RequestParam(value="school_id",required = false) Integer school_id,
			@RequestParam(value="program_id",required = false) Integer program_id,
			@RequestParam(value="program_specialization_id",required = false) Integer program_specialization_id,
			@RequestParam(value="current_sem",required = false) Integer current_sem,
			@RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  studentNoDueService.getAllDataFilteredByKeyword(pageable,school_id,program_id,program_specialization_id,current_sem, keyword);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = studentNoDueService.getAllSortedData(pageable1,school_id,program_id,program_specialization_id,current_sem);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	
	@PostMapping(value = "/studentNoDueUploadFile")
	public ResponseEntity<Object> uploadFile(@RequestParam MultipartFile multipartFile,
			@RequestParam Integer student_id) throws IOException {
		studentNoDueService.uploadFile(multipartFile, student_id);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}	
	
	
	@GetMapping(path = "/studentNoDueFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = studentNoDueService.viewFiles(fileName);
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
	

}
