package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
import com.au.dto.ProctorStudentAssignmentDto;
import com.au.model.ProctorStudentAssignment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProctorStudentAssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey7}")
@CrossOrigin
public class ProctorStudentAssignmentController {

	Logger log = LoggerFactory.getLogger(ProctorStudentAssignmentController.class);

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private ProctorStudentAssignmentService psas_service;

	@PostMapping("/ProctorStudentAssignment")
	public ResponseEntity<Object> saveProctorStudentAssignment(@RequestBody ProctorStudentAssignmentDto p,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ProctorStudentAssignment> proc_stu_assignment = psas_service.saveProctorStudentAssignments(p, jwtToken);
			ResponseEntity<Object> proc_stu_assignment_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, proc_stu_assignment);
			return proc_stu_assignment_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

	@GetMapping("/ProctorStudentAssignment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ProctorStudentAssignment> proc_stu_assignment = psas_service.listAll();
			ResponseEntity<Object> proc_stu_assignment_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, proc_stu_assignment);
			return proc_stu_assignment_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

//	@GetMapping("/fetchAllProctorStudentAssignmentDetail")
//	public List<HashMap<String, Object>> listAll1() {
//		return psas_service.listAll1();
//	}
	
	@GetMapping("/fetchAllProctorStudentAssignmentDetail")
	public ResponseEntity<Object> getAllProctorHeadDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			  @RequestParam(value="UserId", required = false) Integer userId,
		        @RequestParam(value="school_id", required = false) Integer school_id,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> proc_stu_assignment_filtered =  psas_service.getAllDataFilteredByKeyword(pageable, keyword ,userId ,school_id);//,column,value);
			return proc_stu_assignment_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> proc_stu_assignment_sorted = psas_service.getAllSortedData(pageable1,userId ,school_id);
			return proc_stu_assignment_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/ProctorStudentAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			ProctorStudentAssignment proctorStudentAssignment = psas_service.get(id);
			ResponseEntity<Object> proctorStudentAssignment_response= ResponseHandler.generateResponse(true, HttpStatus.OK, proctorStudentAssignment);
			return proctorStudentAssignment_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	/*
	 * @PutMapping("/ProctorStudentAssignment/{id}") public
	 * ResponseEntity<ProctorStudentAssignment> update(@RequestBody @Valid
	 * ProctorHeadHistoryDto p, @PathVariable Integer
	 * id,@RequestHeader("Authorization") String jwtToken) throws
	 * Exception,JsonParseException, JsonMappingException, IOException { try {
	 * JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	 * p.getPsa().setModified_by(jwtDetails.getUserId());
	 * p.getPsa().setModified_username(jwtDetails.getUserName());
	 * psas_service.saveProctorStudentAssignmentss(p,jwtToken); return new
	 * ResponseEntity<ProctorStudentAssignment>(HttpStatus.OK); } catch
	 * (NoSuchElementException e) { return new
	 * ResponseEntity<ProctorStudentAssignment>(HttpStatus.NOT_FOUND); } }
	 */

	@PutMapping("/ProctorStudentAssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody List<ProctorStudentAssignment> p,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			// ProctorStudentAssignment existProduct = psas_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			p.stream().forEach(model -> {
				model.setModified_by(jwtDetails.getUserId());
				model.setModified_username(jwtDetails.getUserName());
			});
			psas_service.saveProctorStudentAssignment(p);
			ResponseEntity<Object> proctorStudentAssignment_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return proctorStudentAssignment_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/ProctorStudentAssignment/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		psas_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateProctorStudentAssignment/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		psas_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllStudentDetailsList/{emp_id}")
	public ResponseEntity<Object> getAllList(@PathVariable Integer emp_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> proctorStudentAssignment = psas_service.getAllStudentList(emp_id);
			ResponseEntity<Object> proctorStudentAssignment_response= ResponseHandler.generateResponse(true, HttpStatus.OK, proctorStudentAssignment);
			return proctorStudentAssignment_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getProctorStatusAssignedStudentDetailsList/{emp_id}")
	public ResponseEntity<Object> getProctorStatusAssignedStudentDetailsList(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> proctorStudentAssignment = psas_service.getProctorStatusAssignedStudentDetailsList(emp_id);
			ResponseEntity<Object> proctorStudentAssignment_response= ResponseHandler.generateResponse(true, HttpStatus.OK, proctorStudentAssignment);
			return proctorStudentAssignment_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getProctorStatusAssignedStudentDetailsListByUserId/{user_id}")
	public ResponseEntity<Object> getProctorStatusAssignedStudentDetailsListByUserId(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> proctorStudentAssignment = psas_service.getProctorStatusAssignedStudentDetailsListByUserId(user_id);
			ResponseEntity<Object> proctorStudentAssignment_response= ResponseHandler.generateResponse(true, HttpStatus.OK, proctorStudentAssignment);
			return proctorStudentAssignment_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getProctorStatusAssignedStudentsByUserId/{user_id}")
	public ResponseEntity<Object> getProctorStatusAssignedStudentsByUserId(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> proctorStudentAssignment = psas_service.getProctorStatusAssignedStudentsByUserId(user_id);
			ResponseEntity<Object> proctorStudentAssignment_response= ResponseHandler.generateResponse(true, HttpStatus.OK, proctorStudentAssignment);
			return proctorStudentAssignment_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getCountOfStudentBasedOnUserId/{user_id}")
	public ResponseEntity<Object> getCountOfStudentBasedOnUserId(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<String,Object> cos = psas_service.getCountOfStudentBasedOnUserId(user_id);
		ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		return co_response;
		} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
				}
		}
}
