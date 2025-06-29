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
import com.au.model.ProgramAssigment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.ProgramAssigmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class ProgramAssigmentController {

	Logger log = LoggerFactory.getLogger(ProgramAssigmentController.class);

	@Autowired
	private ProgramAssigmentService r_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/ProgramAssigment")
	public ResponseEntity<Object> saveProgramAssigment(@RequestBody @Valid ProgramAssigment r,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		r.setCreated_by(jwtDetails.getUserId());
		r.setCreated_username(jwtDetails.getUserName());
		ProgramAssigment prog_ass = r_service.saveProgramAssigment(r);
		ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, prog_ass);
		return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllProgramAssigmentDetail")
	public ResponseEntity<Object> getAllRolesDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> roles_filtered =  r_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return roles_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> roles_sorted = r_service.getAllSortedData(pageable1);
			return roles_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/ProgramAssigment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ProgramAssigment> prog_ass = r_service.listAll();
			ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.OK, prog_ass);
			return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/ProgramAssigment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			ProgramAssigment product = r_service.get(id);
			ResponseEntity<Object> product_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return product_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/ProgramAssigment/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid ProgramAssigment r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			// ProgramAssigment existProduct = r_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			r.setModified_by(jwtDetails.getUserId());
			r.setModified_username(jwtDetails.getUserName());
			r_service.save_ProgramAssigments(r);
			ResponseEntity<Object> program_ass_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return program_ass_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/ProgramAssigment/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		r_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateProgramAssigment/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		r_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchProgram1/{ac_year_id}/{school_id}")
	public ResponseEntity<Object> getProgramBySchool(@PathVariable Integer ac_year_id,@PathVariable Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> prog_ass = r_service.getProgramBySchool(ac_year_id,school_id);
			ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.OK, prog_ass);
			return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/FetchAcademicProgram/{ac_year_id}/{program_id}/{school_id}")
	public ResponseEntity<Object> getNumOfSemAndYearByProgram_IdAndAcYear_Id(@PathVariable Integer ac_year_id,@PathVariable Integer program_id,@PathVariable Integer school_id)
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ProgramAssigment> prog_ass = r_service.getNumOfSemAndYearByProgram_IdAndAcYear_Id(ac_year_id, program_id,school_id);
			ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.OK, prog_ass);
			return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllProgramsWithSpecializationBasedOnSchoolAndAcYear/{school_id}/{ac_year_id}")
	public ResponseEntity<Object> fetchAllProgramsWithSpecializationBasedOnSchoolAndAcYear(@PathVariable Integer school_id,@PathVariable Integer ac_year_id)
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> prog_with_spec = r_service.fetchAllProgramsWithSpecializationBasedOnSchoolAndAcYear(school_id,ac_year_id);
			ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.OK, prog_with_spec);
			return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllProgramsWithSpecializationBasedOnAcYear")
	public ResponseEntity<Object> fetchAllProgramsWithSpecializationBasedOnAcYear()
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> prog_with_spec = r_service.fetchAllProgramsWithSpecializationBasedOnAcYear();
			ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.OK, prog_with_spec);
			return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllProgramsWithSpecialization/{school_id}")
	public ResponseEntity<Object> fetchAllProgramsWithSpecialization(@PathVariable Integer school_id)
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> prog_with_spec = r_service.fetchAllProgramsWithSpecialization(school_id);
			ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.OK, prog_with_spec);
			return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllProgramsWithProgramType/{school_id}")
	public ResponseEntity<Object> fetchAllProgramsWithProgramType(@PathVariable Integer school_id)
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> program_with_program_type = r_service.fetchAllProgramsWithProgramType(school_id);
			ResponseEntity<Object> program_with_program_type_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program_with_program_type );
			return program_with_program_type_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllProgramsAndSpecializationWithProgramType")
	public ResponseEntity<Object> fetchAllProgramsAndSpecializationWithProgramType()
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> program_with_program_type = r_service.fetchAllProgramsAndSpecializationWithProgramType();
			ResponseEntity<Object> program_with_program_type_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program_with_program_type );
			return program_with_program_type_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/programsDetailsWithProgramType")
	public ResponseEntity<Object> programsDetailsWithProgramType()
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> program_with_program_type = r_service.programsDetailsWithProgramType();
			ResponseEntity<Object> program_with_program_type_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program_with_program_type );
			return program_with_program_type_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/ProgramAssigmentById/{program_assignment_id}")
	public ResponseEntity<Object> ProgramAssigmentById(@PathVariable Integer program_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			ProgramAssigment prog_ass = r_service.findAll11(program_assignment_id);
			ResponseEntity<Object> prog_ass_response= ResponseHandler.generateResponse(true, HttpStatus.OK, prog_ass);
			return prog_ass_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchProgramWithSpecialization/{ac_year_id}/{school_id}")
	public ResponseEntity<Object> fetchAllProgramsAndSpecializationWithProgramTypeOnAcademicYeearAndSchool(@PathVariable Integer ac_year_id,@PathVariable Integer school_id)
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> program_with_program_type = r_service.fetchAllProgramsAndSpecializationWithProgramTypeOnAcademicYeearAndSchool(ac_year_id,school_id);
			ResponseEntity<Object> program_with_program_type_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program_with_program_type );
			return program_with_program_type_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getProgramTypeBasedOnSchool/{school_id}")
	public ResponseEntity<Object> getProgramTypeBasedOnSchool(@PathVariable Integer school_id)
	{
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> program_with_program_type = r_service.getProgramTypeBasedOnSchool(school_id);
			ResponseEntity<Object> program_with_program_type_response= ResponseHandler.generateResponse(true, HttpStatus.OK, program_with_program_type );
			return program_with_program_type_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

}
