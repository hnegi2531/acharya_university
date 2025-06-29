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
import com.au.dto.JwtDetails;
import com.au.model.Program;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProgramService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class ProgramController {

	Logger log = LoggerFactory.getLogger(ProgramController.class);

	@Autowired
	private ProgramService pro_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/Program")
	public ResponseEntity<Object> saveProgram(@RequestBody @Valid Program p,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			p.setCreated_by(jwtDetails.getUserId());
			p.setCreated_username(jwtDetails.getUserName());
			log.info("ProgramController : "+"saveProgram : "+p);
			System.out.println("ProgramController : "+"saveProgram : "+p);
			Program program = pro_service.save_Program(p);
			ResponseEntity<Object> program_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, program);
			return program_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

	}

//	@GetMapping("/fetchAllProgramDetail")
//	public List<Program> listAll() {
//		return pro_service.listAll();
//	}
	
	@GetMapping("/fetchAllProgramDetail")
	public ResponseEntity<Object> getAllProgramDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> roles_filtered =  pro_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
				return roles_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> roles_sorted = pro_service.getAllSortedData(pageable1);
				return roles_sorted;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}


	@GetMapping("/Program")
	public ResponseEntity<Object> listAll1() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Program> program = pro_service.listAll1();
		ResponseEntity<Object> program_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program);
		return program_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}

	@GetMapping("/Program/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			Program program = pro_service.get(id);
			ResponseEntity<Object> program_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program);
			return program_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/Program/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid Program p, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			//Program existProduct = pro_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			p.setModified_by(jwtDetails.getUserId());
            p.setModified_username(jwtDetails.getUserName());
			pro_service.save_ProgramType1(p);
			ResponseEntity<Object> program_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return program_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/Program/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		pro_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateProgram/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		pro_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getAllActiveProgramDetails/{emp_id}")
	public ResponseEntity<Object> getAllActiveProgramDetails(@PathVariable Integer emp_id)
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> program_with_program_type = pro_service.getAllActiveProgramDetails(emp_id);
			ResponseEntity<Object> program_with_program_type_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program_with_program_type );
			return program_with_program_type_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getAllActiveProgramDetailsUnique/{emp_id}")
	public ResponseEntity<Object> getAllActiveProgramDetails1(@PathVariable Integer emp_id)
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> program_with_program_type = pro_service.getAllActiveProgramDetails1(emp_id);
			ResponseEntity<Object> program_with_program_type_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program_with_program_type );
			return program_with_program_type_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
/*
	@GetMapping("/Program1/{id}")
	public List<Program> get1(@PathVariable Integer id) {*/
		// try {

//		List<Program> school = pro_service.findById(id);
		/*
		 * return new List<Program>(school, HttpStatus.OK); } catch
		 * (NoSuchElementException e) { return new List<Program>(HttpStatus.NOT_FOUND);
		 * }
		 */
//		return school;
	//}
/*
	@GetMapping("/pro1/{id}")
	public Integer countRecords(@PathVariable("id") Integer id) {
		return pro_service.countRecords(id);
	}

	@GetMapping("/fetchProgram/{program_name}/{school_id}")
	public Integer getProgramByPnameSchool(@PathVariable String program_name, @PathVariable Integer school_id) {
		return pro_service.getProgramByPnameSchool(program_name, school_id);
	}

	@GetMapping("/fetchProgram1/{school_id}")
	public List<Program> getProgramBySchool(@PathVariable Integer school_id) {
		return pro_service.getProgramBySchool(school_id);
	}
	*/


	@GetMapping("/fetchAllProgramWithProgramName")
	public ResponseEntity<Object> fetchAllProgramWithProgramName() {
		if(RateLimitController.bucket.tryConsume(1)) {
			return pro_service.fetchAllProgramWithProgramName();
		}
		return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	}
}
