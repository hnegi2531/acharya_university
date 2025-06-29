package com.au.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.au.dto.DepartmentAssignmentRequest;
import com.au.dto.JwtDetails;
import com.au.model.DepartmentAssignment;
import com.au.response.ResponseHandler;
import com.au.service.DepartmentAssignmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class DepartmentAssignmentController {

	Logger log = LoggerFactory.getLogger(DepartmentAssignmentController.class);

	@Autowired
	private DepartmentAssignmentService da_service;

	@Autowired
	private JwtTokenService jwt_service;
	
//	@GetMapping("/fetchAllDepartmentAssignmentDetail")
//	public ResponseEntity<Object> getAllDept(){
//		if(RateLimitController.bucket.tryConsume(1)) {
//		List<HashMap<String, Object>> dept = da_service.listAll1();
//		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, dept);
//		return department_response;
//		}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
	
	@GetMapping("/fetchAllDepartmentAssignmentDetail")
	public ResponseEntity<Object> getAllDeptAssignment(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> dept_assign_filtered =  da_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return dept_assign_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> dept_assign_sorted = da_service.getAllSortedData(pageable1);
			return dept_assign_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/DepartmentAssignment")
	public ResponseEntity<Object> saveDepartmentAssignment(@Valid @RequestBody DepartmentAssignmentRequest da,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		da.setCreated_by(jwtDetails.getUserId());
		da.setCreated_username(jwtDetails.getUserName());
		List<DepartmentAssignment> dept = da_service.saveDepartmentAssignmentRequest(da);
		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, dept);
		return department_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/DepartmentAssignment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<DepartmentAssignment> dept = da_service.listAll();
			ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, dept);
			return department_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

	@GetMapping("/DepartmentAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			DepartmentAssignment da = da_service.get(id);
			ResponseEntity<Object> roles_response= ResponseHandler.generateResponse(true, HttpStatus.OK, da);
			return roles_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

//	@PutMapping("/DepartmentAssignment/{id}")
//	public ResponseEntity<Object> update(@RequestBody @Valid List<DepartmentAssignment> da,
//			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
//			throws JsonParseException, JsonMappingException, IOException {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			try {
//			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//			da.stream().forEach(d->{
//				d.setCreated_by(jwtDetails.getUserId());
//				d.setCreated_username(jwtDetails.getUserName());
//			});
//			da.stream().forEach(d->{
//				d.setModified_by(jwtDetails.getUserId());
//				d.setModified_username(jwtDetails.getUserName());				
//			});
//			
//			da_service.saveDepartmentAssignment1(da);
//			ResponseEntity<Object> role_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//			return role_response;
//		} catch (NoSuchElementException e) {
//			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
//			return response;
//		}
//			
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
	
	@PutMapping("/DepartmentAssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid DepartmentAssignment da,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			da.setModified_by(jwtDetails.getUserId());
			da.setModified_username(jwtDetails.getUserName());				
			da_service.saveDepartmentAssignment1(da);
			ResponseEntity<Object> role_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return role_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
			
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/DepartmentAssignment/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		da_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateDepartmentAssignment/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		da_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchDept/{dept_id}/{school_id}")
	public ResponseEntity<Object> getDeptCountByDnameSchool(@PathVariable Integer dept_id,@PathVariable Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Integer count = da_service.getDeptCountByDnameSchool(dept_id, school_id);
			ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, count);
			return department_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/fetchdept1/{school_id}")
	public ResponseEntity<Object> getDeptBySchholId(@PathVariable Integer school_id){
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> dept = da_service.getDeptBySchholId1(school_id);
		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, dept);
		return department_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getActiveDepartmentAssignmentBasedOnTag")
	public ResponseEntity<Object> getActiveDepartmentAssignmentBasedOnTag() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> dept = da_service.getActiveDepartmentAssignmentBasedOnTag();
			ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, dept);
			return department_response;
			}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}	
	
}