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
import com.au.model.CourseObjective;
import com.au.model.CourseOutcome;
import com.au.model.Syllabus;
import com.au.response.ResponseHandler;
import com.au.service.CourseOutcomeService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class CourseOutcomeController {


	Logger log = LoggerFactory.getLogger(CourseOutcomeController.class);
	
	@Autowired
	private CourseOutcomeService coc_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/courseOutCome")
	public ResponseEntity<Object> saveCourseOutCome(@RequestBody @Valid List<CourseOutcome> coc,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			coc.stream().forEach(f -> {

			f.setCreated_by(jwtDetails.getUserId());
			f.setCreated_username(jwtDetails.getUserName());
			});
			List<CourseOutcome> cocs = coc_service.saveCourseOutCome(coc);
			ResponseEntity<Object> coc_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cocs);
		return coc_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllCourseOutComeDetail")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> coc_filtered =  coc_service.getAllDataFilteredByKeyword(pageable, keyword);
			return coc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> coc_sorted = coc_service.getAllSortedData(pageable1);
			return coc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/courseOutCome")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<CourseOutcome> cocs = coc_service.listAll1();
		ResponseEntity<Object> coc_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cocs);
		return coc_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/courseOutCome/{id}")
	public ResponseEntity<Object> get(@PathVariable List<Integer> id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	List<CourseOutcome> cocs = coc_service.get(id);
	    	ResponseEntity<Object> coc_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cocs);
			return coc_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/courseOutComes/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<CourseOutcome> courseOutCome,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken) throws Exception{
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				courseOutCome.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				coc_service.saveCourseOutComes(courseOutCome);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (JsonParseException | JsonMappingException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	
	@DeleteMapping("/courseOutCome/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			coc_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateCourseOutComes/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			coc_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCourseOutCome/{course_assignment_id}")
	public ResponseEntity<Object> getCourseOutCome(@PathVariable Integer course_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<CourseOutcome> list_syllabus = coc_service.getCourseOutCome(course_assignment_id);
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getCourseOutComeDetails/{course_assignment_id}")
	public ResponseEntity<Object> getCourseOutComeDetails(@PathVariable Integer course_assignment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> list_syllabus = coc_service.getCourseOutComeDetails(course_assignment_id);
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getToxonomyDetails")
	public ResponseEntity<Object> getToxonomyDetails(@RequestParam(value="toxonomy") String toxonomy) {
		if(RateLimitController.bucket.tryConsume(1)) {
			String list_syllabus = coc_service.getToxonomyDetails(toxonomy);
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}		
}
