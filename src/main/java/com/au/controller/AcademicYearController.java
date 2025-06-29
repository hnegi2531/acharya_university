package com.au.controller;

import java.io.IOException;
import java.util.List;
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
import com.au.model.Academic_year;
import com.au.response.ResponseHandler;
//import com.au.model.Course;
import com.au.service.AcademicYearService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class AcademicYearController {

	Logger log = LoggerFactory.getLogger(AcademicYearController.class);

	@Autowired
	private AcademicYearService ac_service;

	@Autowired
	private JwtTokenService jwt_service;

	/*
	 * @ApiOperation(value = "Create User",notes = "Create New User",tags =
	 * {"User Management"})
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200,message = " Academic Year created Successfully"),
	 * 
	 * @ApiResponse(code = 404,message = "Invalid Data"),
	 * 
	 * @ApiResponse(code = 500,message = "INTERNAL SERVER ERROR") })
	 */

	@PostMapping("/academic_year")
	public ResponseEntity<Object> saveAcademicYear(@RequestBody @Valid Academic_year ac_year,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				ac_year.setCreated_by(jwtDetails.getUserId());
				ac_year.setCreated_username(jwtDetails.getUserName());
				Academic_year academic_year= ac_service.save_Academic_Year(ac_year);
				ResponseEntity<Object> academic_program_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, academic_year );
				return academic_program_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/academic_year")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Academic_year> ac_list = ac_service.listAll();
				ResponseEntity<Object> academic_year_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ac_list);
				return academic_year_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/academicYearGT")
	public ResponseEntity<Object> academicYearGT() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Academic_year> ac_list = ac_service.academicYearGT();
				ResponseEntity<Object> academic_year_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ac_list);
				return academic_year_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	/*@GetMapping("/fetchAllAcademic_yearDetail")
	public List<Academic_year> listAll1() {
		return ac_service.listAll1();
	}*/
	
	@GetMapping("/fetchAllAcademic_yearDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> academic_program_details =  ac_service.listAll1(pageable, keyword);//,column,value);
						return academic_program_details;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> academic_program_detail1 = ac_service.listAll2(pageable1);
						return academic_program_detail1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return ac_service.listAll1();
	}

	@GetMapping("/academic_year/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {

						Academic_year product = ac_service.get(id);
						ResponseEntity<Object> academic_yearlist_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
						return academic_yearlist_response;

				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PutMapping("/academic_year/{id}")
	public ResponseEntity<Object> update(@RequestBody Academic_year ac_year, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					ac_year.setModified_by(jwtDetails.getUserId());
					ac_year.setModified_username(jwtDetails.getUserName());
					ac_service.save_Academic_Year1(ac_year);
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/academic_year/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return ac_service.delete(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/activateAcademic_year/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return ac_service.delete1(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	/*
	 * @GetMapping("FetchAcademicYear/{ac_year_id}") public List<Integer>
	 * getAcademicYearByACYearId(@PathVariable Integer ac_year_id) { return
	 * (List<Integer>) ac_service.getAcademicYearByACYearId(ac_year_id); }
	 */

	@GetMapping("/FetchAcademicYear")
	public ResponseEntity<Object> getAcademicYearByACYearId() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Academic_year> ac_year_list = ac_service.getAcademicYearByACYearId();
				ResponseEntity<Object> academic_yearlist_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ac_year_list);
				return academic_yearlist_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}				
	}

	@GetMapping("/academic_year2")
	public ResponseEntity<Object> countRecords() {
		if(RateLimitController.bucket.tryConsume(1)) {
				Integer count = ac_service.countRecords();
				ResponseEntity<Object> count_response= ResponseHandler.generateResponse(true, HttpStatus.OK, count);
				return count_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/fetch1")
	public ResponseEntity<Object> fetch1() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Academic_year> ac_year_list = ac_service.findByAcYearId1();
				ResponseEntity<Object> ac_year_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ac_year_list);
				return ac_year_list_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

}
