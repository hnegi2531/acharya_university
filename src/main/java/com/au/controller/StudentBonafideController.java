package com.au.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.*;
import com.au.dto.JwtDetails;
import com.au.dto.StudentOfferDto;
import com.au.model.StudentBonafide;
import com.au.model.Student_Details;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StudentBonafideService;
import com.au.service.StudentDetailsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentBonafideController {

Logger log = LoggerFactory.getLogger(StudentBonafideController.class);
	
	@Autowired
	private StudentBonafideService std_bonafide_Service;
	
	@Autowired
	private StudentDetailsService stu_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/studentBonafide")
	public ResponseEntity<Object> save_studentbonafide(@RequestBody @Valid StudentBonafide stdbonafide,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		stdbonafide.setCreated_by(jwtDetails.getUserId());
		stdbonafide.setCreated_username(jwtDetails.getUserName());
		
		StudentBonafide studentbonafide = std_bonafide_Service.save_studentbonafide(stdbonafide);
		
		ResponseEntity<Object> stdbonafide_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, studentbonafide);
		return stdbonafide_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/studentBonafide")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<StudentBonafide> stdbonafide = std_bonafide_Service.listAll1();
		ResponseEntity<Object> stdbonafide_response= ResponseHandler.generateResponse(true, HttpStatus.OK, stdbonafide);
		return stdbonafide_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/fetchAllStudentBonafide")
	public ResponseEntity<Object> getAllStudentBonafide(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> stdbonafide_filtered =  std_bonafide_Service.getAllDataFilteredByKeyword(pageable, keyword);
			return stdbonafide_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> stdbonafide_sorted = std_bonafide_Service.getAllSortedData(pageable1);
			return stdbonafide_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/studentBonafide/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	StudentBonafide stdbonafide = std_bonafide_Service.get(id);
	    	ResponseEntity<Object> stdbonafide_response= ResponseHandler.generateResponse(true, HttpStatus.OK, stdbonafide);
			return stdbonafide_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/studentBonafide/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid StudentBonafide stdbonafide, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	stdbonafide.setModified_by(jwtDetails.getUserId());
	    	stdbonafide.setModified_username(jwtDetails.getUserName());
	    	
	    	std_bonafide_Service.savestudentBonafide1(stdbonafide);
	    	 
	    	ResponseEntity<Object> stdbonafide_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return stdbonafide_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/studentBonafide/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			std_bonafide_Service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateStudentBonafide/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			std_bonafide_Service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/studentBonafideAuid")
	public ResponseEntity<Object> list1() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<String> stdbonafide = std_bonafide_Service.list1();
			ResponseEntity<Object> stdbonafide_response = ResponseHandler.generateResponse(true, HttpStatus.OK, stdbonafide);
			return stdbonafide_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/studentBonafideDetails")
	public ResponseEntity<Object> getStudentBonafideDetails(@RequestParam(value="auid") String auid,
			@RequestParam(value="bonafide_type") String bonafide_type) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> auidDetails = std_bonafide_Service.getStudentBonafideDetails(auid,bonafide_type);
				ResponseEntity<Object> psp_auid_response = ResponseHandler.generateResponse(true, HttpStatus.OK, auidDetails);
				return psp_auid_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/studentBonafideAddOnDetails")
	public ResponseEntity<Object> studentBonafideAddOnDetails(@RequestParam(value="auid") String auid,
			@RequestParam(value="bonafide_type") String bonafide_type) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<String,Object> auidDetails = std_bonafide_Service.studentBonafideAddOnDetails(auid,bonafide_type);
				ResponseEntity<Object> psp_auid_response = ResponseHandler.generateResponse(true, HttpStatus.OK, auidDetails);
				return psp_auid_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/studentBonafideDetailsDropDown")
	public ResponseEntity<Object> studentBonafideDetailsDropDown(@RequestParam(value="auid") String auid) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> auidDetails = std_bonafide_Service.studentBonafideDetailsDropDown(auid);
				ResponseEntity<Object> psp_auid_response = ResponseHandler.generateResponse(true, HttpStatus.OK, auidDetails);
				return psp_auid_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}	
	
	
	@PostMapping("/sendEmailForStudentOnboardingWithProvisionalAndBonafide")
    public ResponseEntity<Object> generatePdf(@ModelAttribute StudentOfferDto sd) {
        try {
        	std_bonafide_Service.sendEmailForStudentOnboardingWithProvisionalAndBonafide(sd);
        	ResponseEntity<Object> psp_auid_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return psp_auid_response;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF: " + e.getMessage());
        }
    }

	@PatchMapping("/updateHostelFeeTemplateId/{studentBonaFideId}")
	public ResponseEntity<Object> updateHostelFeeTemplateId(@PathVariable Integer studentBonaFideId, @RequestParam Integer hostelFeeTemplateId){
		if(RateLimitController.bucket.tryConsume(1)) {
			return std_bonafide_Service.updateHostelFeeTemplateId(studentBonaFideId, hostelFeeTemplateId);
		}else {
            return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
}
