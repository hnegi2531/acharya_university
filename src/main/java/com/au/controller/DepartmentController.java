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
import com.au.model.Department;
import com.au.response.ResponseHandler;
import com.au.service.DepartmentService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class DepartmentController {

	Logger log = LoggerFactory.getLogger(DepartmentController.class);
	
	@Autowired
	private DepartmentService deptService;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/dept")
	public ResponseEntity<Object> saveDept(@RequestBody @Valid Department dept,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		dept.setCreated_by(jwtDetails.getUserId());
		dept.setCreated_username(jwtDetails.getUserName());
		Department department = deptService.save_Department(dept);
		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, department);
		return department_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/fetchAllDeptDetail")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> dept_filtered =  deptService.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return dept_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> dept_sorted = deptService.getAllSortedData(pageable1);
			return dept_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/dept")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Department> department = deptService.listAll1();
		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, department);
		return department_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/dept/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	Department department = deptService.get(id);
	    	ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, department);
			return department_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/dept/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid Department dept, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	//Department existProduct = deptService.get(id);
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	dept.setModified_by(jwtDetails.getUserId());
	    	dept.setModified_username(jwtDetails.getUserName());
	    	deptService.saveDepartment(dept);
	    	ResponseEntity<Object> department_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return department_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/dept/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		deptService.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateDept/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		deptService.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
/*	
	@GetMapping("/fetchDept/{dept_name}/{school_id}")
	public Integer getDeptCountByDnameSchool(@PathVariable String dept_name,@PathVariable Integer school_id) {
		return deptService.getDeptCountByDnameSchool(dept_name, school_id);
	}
	
	@GetMapping("/fetchdept1/{school_id}")
	public List<Department> getDeptBySchholId(@PathVariable Integer school_id){
    	return deptService.getDeptBySchholId(school_id);
    }
	*/
	
	@GetMapping("de/{dept_name}")
	public ResponseEntity<Object> existsByDeptname(@PathVariable String dept_name) {
		if(RateLimitController.bucket.tryConsume(1)) {
		System.out.println("((((((((((((((((())))))))))))))))1111111112222222");
		Boolean value = deptService.existsByDeptname(dept_name);
		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, value);
		return department_response;
	}
		else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/allUnassignedSchoolToDepartment/{dept_id}")
	public ResponseEntity<Object> fetchUnassignedSchoolDetails(@RequestBody @PathVariable Integer dept_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String, Object>> unassigned_school=deptService.fetchUnassignedSchoolIds(dept_id);
		ResponseEntity<Object> unassigned_school_response= ResponseHandler.generateResponse(true, HttpStatus.OK, unassigned_school);
		return unassigned_school_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/allNoDuesDetails")
	public ResponseEntity<Object> allNoDuesDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String, Object>> unassigned_school=deptService.allNoDuesDetails();
		ResponseEntity<Object> unassigned_school_response= ResponseHandler.generateResponse(true, HttpStatus.OK, unassigned_school);
		return unassigned_school_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getDepartmentBasedOnHodId")
	public ResponseEntity<Object> getDepartmentBasedOnHodId() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> department = deptService.getDepartmentBasedOnHodId();
		ResponseEntity<Object> department_response= ResponseHandler.generateResponse(true, HttpStatus.OK, department);
		return department_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
}
