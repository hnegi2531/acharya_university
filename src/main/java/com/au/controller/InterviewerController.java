package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
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

import com.au.dto.JobFileRequest;
import com.au.dto.JwtDetails;
import com.au.model.Interviewer;
import com.au.repository.JobProfileRepository;
import com.au.response.ResponseHandler;
import com.au.service.InterviewService;
import com.au.service.InterviewerService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class InterviewerController {

	Logger log = LoggerFactory.getLogger(InterviewerController.class);

	@Autowired
	private InterviewerService is_service;
	
	@Autowired
	private InterviewService i_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private JobProfileRepository jpr_repo;

	@PostMapping("/Interviewer")
	public ResponseEntity<Object> saveInterview(@RequestBody @Valid Interviewer i) {
		if(RateLimitController.bucket.tryConsume(1)) {
		Interviewer interviewer = is_service.saveInterviewer(i);
		ResponseEntity<Object> interviewer_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, interviewer);
		return interviewer_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Interviewer")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Interviewer> interviewer = is_service.listAll();
			ResponseEntity<Object> interviewer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, interviewer);
			return interviewer_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Interviewer/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			Interviewer i = is_service.get(id);
			ResponseEntity<Object> i_response= ResponseHandler.generateResponse(true, HttpStatus.OK, i);
			return i_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	//by interviewer_ids
	@GetMapping("/listOfInterviewer/{id_list}")
	public ResponseEntity<Object> get(@PathVariable List<Integer> id_list) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			List<Interviewer> i = is_service.getlist(id_list);
			ResponseEntity<Object> i_response= ResponseHandler.generateResponse(true, HttpStatus.OK, i);
			return i_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/Interviewer/{id}")
	public ResponseEntity<Object> update(@RequestBody List<Interviewer> i, @PathVariable List<Integer> id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			i.stream().forEach(i1-> {
				i1.setHr_id(jwtDetails.getUserId());
				i1.setHr_name(jwtDetails.getUserName());
				
			});
			is_service.saveInterviewer(i);
			jpr_repo.UpdateCommentStatus(i.get(0).getJob_id());
			ResponseEntity<Object> i_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return i_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/Interviewer/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		is_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getAllInterviewerDeatils/{job_id}")
	public ResponseEntity<Object> getAllDeatils(@PathVariable Integer job_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> interviewer = is_service.getAllDeatils(job_id);
			ResponseEntity<Object> interviewer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, interviewer);
			return interviewer_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllInterviewerDetails/{emp_id}")
	public ResponseEntity<Object> listAll1(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> interviewer = is_service.listAll1(emp_id);
			ResponseEntity<Object> interviewer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, interviewer);
			return interviewer_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getAllInterviewerEmailDeatils/{interview_id}")
	public ResponseEntity<Object> getAllEmailDeatils(@PathVariable Integer interview_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> interviewer = is_service.getAllEmailDeatils(interview_id);
		ResponseEntity<Object> interviewer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, interviewer);
		return interviewer_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/JobProfileDetailForAppOnInterviewerEmail/{user_id}")
	public ResponseEntity<Object> getJobProfileDetails(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> job_profile_details_on_Interviewer_table_job_id = is_service.getJobProfileDetails(user_id);
			ResponseEntity<Object> job_profile_details_on_Interviewer_table_job_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, job_profile_details_on_Interviewer_table_job_id);
			return job_profile_details_on_Interviewer_table_job_id_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/setInterviewerCommentsFromApp/{user_id}/{job_id}/{interviewer_comments}")
	public ResponseEntity<Object> setInterviewerComments(@PathVariable Integer user_id,@PathVariable Integer job_id,@PathVariable String interviewer_comments) {
		if(RateLimitController.bucket.tryConsume(1)) {
			is_service.setInterviewerComments(user_id,job_id,interviewer_comments);
			jpr_repo.UpdateCommentStatus(job_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping(value = "/HrFeedbackUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute JobFileRequest jobfilerequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		is_service.uploadFile(jobfilerequest.getFile(), jobfilerequest.getJob_id());
		 ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(path = "/HrFeedbackFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = is_service.viewFiles(fileName);
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
