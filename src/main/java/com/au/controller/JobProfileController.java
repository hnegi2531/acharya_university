package com.au.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.au.model.JobProfile;
import com.au.response.ResponseHandler;
import com.au.service.JobProfileService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class JobProfileController {
	
	Logger log = LoggerFactory.getLogger(JobProfileController.class);

	@Autowired
	private JobProfileService jps_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	

	@PostMapping("/JobProfile")
	public JobProfile saveAcademicYear(@RequestBody @Valid JobProfile jobProfile)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		log.debug("Request {}", jobProfile);
		return jps_service.saveJobProfile(jobProfile);
	}

	@GetMapping("/getJobProfile")
	public List<JobProfile> listAll() {
		return jps_service.listAll();

	}
	
	@GetMapping("/fetchAllJobProfileDetails")
	public ResponseEntity<Object> listAll1(
		    @RequestParam(value = "page") Integer page,
		    @RequestParam(value = "page_size") Integer page_size,
		    @RequestParam(value = "sort") String sort,
		    @RequestParam(value = "keyword", required = false) Object keyword,
		    @RequestParam(value = "date_range", required = false) String dateRange,
		    @RequestParam(value = "start_date", required = false) String startDate,
		    @RequestParam(value = "end_date", required = false) String endDate) {

		    Sort sorted = Sort.by(Direction.DESC, sort);
		    Pageable pageable = PageRequest.of(page, page_size, sorted);

		    // Create a filter for date range
		    LocalDate start = null;
		    LocalDate end = null;

		    if (dateRange != null) {
		        switch (dateRange) {
		            case "week":
		                start = LocalDate.now().minusWeeks(1);
		                end = LocalDate.now();
		                break;
		            case "month":
		                start = LocalDate.now().minusMonths(1);
		                end = LocalDate.now();
		                break;
		            case "3month":
		                start = LocalDate.now().minusMonths(3);
		                end = LocalDate.now();
		                break;
		            case "custom":
		                if (startDate != null && endDate != null) {
		                    // Convert the input dates into LocalDate
		                    try {
		                        start = LocalDate.parse(startDate);
		                        end = LocalDate.parse(endDate);
		                    } catch (DateTimeParseException e) {
		                        return ResponseEntity.badRequest().body("Invalid date format. Ensure the date format is 'yyyy-MM-dd'.");
		                    }
		                }
		                break;
		        }
		    }

		    // Pass the filter and pageable to service
		    if (keyword != null) {
		        return jps_service.listAll1(pageable, keyword, start, end);
		    } else {
		        return jps_service.listAll2(pageable, start, end);
		    }
		}


	@GetMapping("/JobProfile/{id}")
	public List<HashMap<String, Object>> get(@PathVariable Integer id) {
			return jps_service.getJobDetails(id);	
	}
	
	@GetMapping("/getJobProfileById/{id}")
	public ResponseEntity<Object> getJobProfileById(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JobProfile job = jps_service.getJobProfileById(id);	
			ResponseEntity<Object> job_response= ResponseHandler.generateResponse(true, HttpStatus.OK, job);
			return job_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/JobProfile/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid JobProfile jobProfile, @PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
	    	//JobProfile existProduct = jps_service.get(id);	
	    	JobProfile job = jps_service.saveJobProfiles(jobProfile);
	    	ResponseEntity<Object> job_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return job_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
 
	@DeleteMapping("/JobProfile/{id}")
	public void delete(@PathVariable Integer id) {
		jps_service.delete(id);
	}
	
	@GetMapping("/getJobProfileNameAndEmail/{job_id}")
	public HashMap<String,Object> getNameAndEmailByJobId(@PathVariable Integer job_id) {
			return jps_service.getNameAndEmailByJobId(job_id);	
	}
	
	@GetMapping("/JobProfileReferenceNo/{job_id}")
	public String getReferenceNo(@PathVariable Integer job_id) {
			return jps_service.getReferenceNo(job_id);	
	}
	
	@GetMapping("/checkEmail/{email}")
	public HashMap<String,Boolean> checkEmail(@PathVariable String email) {
		return jps_service.checkEmail(email);
	}
	
	@GetMapping("/getAllApplicantDetails/{job_id}")
	public HashMap<String, Object> getAllApplicantDetails(@PathVariable Integer job_id) {
		return jps_service.getAllApplicantDetails(job_id);
	}
	
	@GetMapping("/jobProfileDetailsOnUserId/{id}")
	public ResponseEntity<Object> jobProfileDetailsOnUserId(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> job_profile_details = jps_service.jobProfileDetailsOnUserId(id);
			ResponseEntity<Object> job_profile_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, job_profile_details);
			return job_profile_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateJobProfileHrStatus/{job_id}")
	public ResponseEntity<JobProfile> updateJobProfileHrStatus(@PathVariable Integer job_id, @RequestParam String hr_status ,
			 @RequestParam String hr_remark) {
	    try {
			jps_service.updateJobProfileHrStatus(hr_status ,hr_remark ,job_id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/jobProfileDetailsOnDate/{created_date}")
	public ResponseEntity<Object> jobProfileDetailsOnDate(@PathVariable String created_date) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Object> job_profile_details = jps_service.jobProfileDetailsOnDate(created_date);
			ResponseEntity<Object> job_profile_details_response = ResponseHandler.generateResponse(true, HttpStatus.OK, job_profile_details);
			return job_profile_details_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
}
