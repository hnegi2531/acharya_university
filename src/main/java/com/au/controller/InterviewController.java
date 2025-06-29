package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.EmailRequest;
import com.au.dto.InterviewRequest;
import com.au.dto.JwtDetails;
import com.au.model.Interview;
import com.au.repository.InterviewRepository;
import com.au.repository.JobProfileRepository;
import com.au.response.ResponseHandler;
import com.au.service.InterviewHistoryService;
import com.au.service.InterviewService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class InterviewController {

	Logger log = LoggerFactory.getLogger(InterviewController.class);

	@Autowired
	private InterviewService is_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private JobProfileRepository jpr_repo;

	@PostMapping("/Interview")
	public ResponseEntity<Object> saveInterview(@RequestBody @Valid Interview i,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			i.setCreated_by(jwtDetails.getUserId());
			i.setCreated_username(jwtDetails.getUserName());
			Interview interview = is_service.saveInterviews(i);
			ResponseEntity<Object> interview_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					interview);
			return interview_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/InterviewSchedulesssss/{job_id}")
	public ResponseEntity<Object> saveInterviews(@RequestBody @Valid EmailRequest emails, @PathVariable Integer job_id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				// JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				// emails.getI().setCreated_by(jwtDetails.getUserId());
				// emails.getI().setCreated_username(jwtDetails.getUserName());
				System.out.println(emails.getEmails());
				// Interview interview = is_service.saveInterviews(emails.getI());
				is_service.sendMail(emails, job_id);
				// return interview;
				ResponseEntity<Object> interview_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return interview_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	//Send Mail To Candidate
	@PostMapping("/emailForInterview/{job_id}")
	public Object emailForInterview(@RequestBody @Valid EmailRequest emails, @PathVariable Integer job_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				is_service.sendMailToCandidate(emails, job_id);
				String todaysDate = ResponseHandler.getStringTypeTodaysDateDDMMYYYY();
				jpr_repo.updateMailSentToCandidate(job_id, todaysDate);
				ResponseEntity<Object> interview_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,HttpStatus.OK);
				return interview_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	//Save Interview and Interviewers
	@PostMapping("/saveInterviewSchedule/{job_id}")
	public ResponseEntity<Object> saveInterviewsssss(@RequestBody @Valid InterviewRequest emails,
			@PathVariable Integer job_id, @RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {

			is_service.getExistingDataForHistory(job_id);

			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			emails.getInterview().setCreated_by(jwtDetails.getUserId());
			emails.getInterview().setCreated_username(jwtDetails.getUserName());
			System.out.println(emails.getEmails());
			Interview interview = is_service.saveInterview(emails);
			jpr_repo.updateMailSentStatusToNull(job_id);
			System.out.println("======================");
			ResponseEntity<Object> interview_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					interview);
			return interview_response;

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	//Send Mail To Interviewers
	@PostMapping("/sendMailToInterviewers/{job_id}")
	public Object emailToInterviewers(@RequestBody @Valid InterviewRequest emails,
			@PathVariable Integer job_id, @RequestHeader("Authorization") String jwtToken) 
					throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				is_service.sendMails(emails, job_id);
				String todaysDate = ResponseHandler.getStringTypeTodaysDateDDMMYYYY();
				jpr_repo.updateMailSentStatus(job_id, todaysDate);
				ResponseEntity<Object> interview_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,HttpStatus.OK);
				return interview_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Interview")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Interview> interview = is_service.listAll();
			ResponseEntity<Object> interview_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					interview);
			return interview_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Interview/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				Interview i = is_service.get(id);
				ResponseEntity<Object> interview_response = ResponseHandler.generateResponse(true, HttpStatus.OK, i);
				return interview_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	

	@PutMapping("/Interview/{id}")
	public ResponseEntity<Object> update(@RequestBody Interview i, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				i.setModified_by(jwtDetails.getUserId());
				i.setModified_username(jwtDetails.getUserName());
				Interview interview = is_service.saveInterviews(i);
				ResponseEntity<Object> interview_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						interview);
				return interview_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/Interview/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			is_service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getAllInterviewDeatils/{job_id}")
	public ResponseEntity<Object> getAllDeatils(@PathVariable Integer job_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> interview = is_service.getAllDeatils(job_id);
				ResponseEntity<Object> interview_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						interview);
				return interview_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
}
