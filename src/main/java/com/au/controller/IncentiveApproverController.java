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

import com.au.dto.JwtDetails;
import com.au.model.IncentiveApprover;
import com.au.model.Publications;
import com.au.response.ResponseHandler;
import com.au.service.IncentiveApproverService;
import com.au.service.JwtTokenService;
import com.au.service.PublicationsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class IncentiveApproverController {

	Logger log = LoggerFactory.getLogger(IncentiveApproverController.class);
		
		@Autowired
		private IncentiveApproverService approver_service;
	
		@Autowired
		private JwtTokenService jwt_service;
		
		
	@PostMapping("/saveIncentiveApprover")
	public ResponseEntity<Object> saveIncentiveApprover(@RequestBody @Valid List<IncentiveApprover> incentiveApprover,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			incentiveApprover.stream().forEach(ia -> {
				ia.setCreated_by(jwtDetails.getUserId());
				ia.setCreated_username(jwtDetails.getUserName());
			});
			List<IncentiveApprover> publication = approver_service.saveIncentiveApprover(incentiveApprover);
			ResponseEntity<Object> publications_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, publication);
		return publications_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}		
		
	@GetMapping("/getApproverDetailsData/{emp_id}")
	public ResponseEntity<Object> getApproverDetailsData(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> shift_details = approver_service.getApproverDetailsData(emp_id);
			ResponseEntity<Object> shift_details_data = ResponseHandler.generateResponse(true, HttpStatus.OK, shift_details);
			return shift_details_data;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getApproverDetailsDataForPatent/{emp_id}")
	public ResponseEntity<Object> getApproverDetailsDataForPatent(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> shift_details = approver_service.getApproverDetailsDataForPatent(emp_id);
			ResponseEntity<Object> shift_details_data = ResponseHandler.generateResponse(true, HttpStatus.OK, shift_details);
			return shift_details_data;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	
	@GetMapping("/getAllActiveIncentiveApprover")
	public ResponseEntity<Object> getAllActiveIncentiveApprover() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<IncentiveApprover> incentiveApprover = approver_service.getAllActiveIncentiveApprover();
		ResponseEntity<Object>publication_response= ResponseHandler.generateResponse(true, HttpStatus.OK, incentiveApprover);
		return publication_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getIncentiveApprover/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	IncentiveApprover incentiveApprover = approver_service.get(id);
	    	ResponseEntity<Object> publication_response= ResponseHandler.generateResponse(true, HttpStatus.OK, incentiveApprover);
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
	
	
	@PutMapping("/updateIncentiveApprover/{id}")
	public ResponseEntity<Object> updateIncentiveApprover(@RequestBody @Valid IncentiveApprover incentiveApprover,
			@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	incentiveApprover.setModified_by(jwtDetails.getUserId());
	    	incentiveApprover.setModified_username(jwtDetails.getUserName());
	    	approver_service.updateIncentiveApprover(incentiveApprover);
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
	
	@DeleteMapping("/deActivateIncentiveApprover/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			approver_service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateIncentiveApprover/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			approver_service.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllIncentiveApprover")
	public ResponseEntity<Object> fetchAllIncentiveApprover(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,@RequestParam(value="empId") Integer empId,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  approver_service.getAllDataFilteredByKeyword(pageable,empId, keyword);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = approver_service.getAllSortedData(pageable1,empId);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/incentiveApproverBasedOnEmpId/{emp_id}/{incentive_approver_id}")
	public ResponseEntity<Object> incentiveApproverBasedOnEmpId(@PathVariable Integer emp_id,@PathVariable Integer incentive_approver_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> shift_details = approver_service.incentiveApproverBasedOnEmpId(emp_id,incentive_approver_id);
			ResponseEntity<Object> shift_details_data = ResponseHandler.generateResponse(true, HttpStatus.OK, shift_details);
			return shift_details_data;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/checkIncentiveApprover/{id}")
	 public ResponseEntity<String> checkIncentiveApprover(@PathVariable Integer id) {
        String remarks = approver_service.checkIncentiveApprover(id);
        return ResponseEntity.ok(remarks);
    }
	
	@GetMapping("/checkApproverStatus/{id}/{emp_id}")
	 public ResponseEntity<String> checkApproverStatus(@PathVariable Integer id,@PathVariable Integer emp_id) {
       String remarks = approver_service.checkApproverStatus(id,emp_id);
       return ResponseEntity.ok(remarks);
   }
	
	
	@GetMapping("/checkIncentiveApproverRemarks/{id}")
	public ResponseEntity<List<Map<String, Object>>> checkIncentiveApproverRemarks(@PathVariable Integer id) {
        List<Map<String, Object>> incentiveDetails = approver_service.checkIncentiveApproverRemarks(id);
        return ResponseEntity.ok(incentiveDetails);
    }

	@GetMapping("/incentiveApproverReportByMonthAndYear")
	public ResponseEntity<Object> incentiveApproverReportByMonthAndYear(@RequestParam(value = "creditedMonth",required = false) Integer creditedMonth,
														  @RequestParam(value = "creditedYear",required = false) Integer creditedYear) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return approver_service.incentiveApproverReportByMonthAndYear(creditedMonth,creditedYear);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);

		}
	}

	@GetMapping("/incentiveApproverReport")
	public ResponseEntity<Object> incentiveApproverReport(@RequestParam(value = "creditedMonth", required = false) Integer creditedMonth,
																			 @RequestParam(value = "creditedYear", required = false) Integer creditedYear) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return approver_service.incentiveApproverReport(creditedMonth,creditedYear);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);

		}
	}
		
}
