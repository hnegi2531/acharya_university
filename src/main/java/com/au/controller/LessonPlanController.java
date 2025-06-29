package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.au.dto.JwtDetails;
import com.au.dto.LessonPlanAttachmentDto;
import com.au.dto.LessonPlanDto;
import com.au.model.LessonPlan;
import com.au.model.LessonPlanAssignment;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.LessonPlanService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class LessonPlanController {

	@Autowired
	private LessonPlanService s_service;

	@Autowired
	private JwtTokenService jwt_service;

	Logger log = LoggerFactory.getLogger(LessonPlanController.class);

	@PostMapping("/lessonPlan")
	public ResponseEntity<Object> postData(@RequestBody LessonPlanDto lessonPlanDto, @RequestHeader("Authorization") String jwtToken)throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<LessonPlanAssignment> lesson_plan_assignment = s_service.getData(lessonPlanDto, jwtToken);
			ResponseEntity<Object> lesson_plan_assignment_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, lesson_plan_assignment);
			return lesson_plan_assignment_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/LessonPlan")
	public ResponseEntity<Object> postDataFromFile(@ModelAttribute LessonPlan lessonPlanDto,
			@RequestHeader("Authorization") String jwtToken)throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			lessonPlanDto.setCreated_by(jwtDetails.getUserId());
			lessonPlanDto.setCreated_username(jwtDetails.getUserName());
			List<LessonPlanAssignment> data = s_service.getDataFromFile(lessonPlanDto.getFile(),jwtToken);
			ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.OK, data);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/downloadCSVFile/{fileName:.+}")
	public ResponseEntity<Resource> getFile() {
		String filename = "tutorials.csv";
		InputStreamResource file = new InputStreamResource(s_service.load());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/csv")).body(file);
	}

	@GetMapping("/LessonPlan")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> lesson_plan_sorted = s_service.listAll1(pageable, keyword);//,column,value);
				return lesson_plan_sorted;
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> program_type_pageable = s_service.listAll2(pageable1);
				return program_type_pageable;
			}
			//		return s_service.listAll();
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/LessonPlan/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				LessonPlan product = s_service.get(id);
				ResponseEntity<Object> lesson_plan_response_by_id = ResponseHandler.generateResponse(true,HttpStatus.OK, product);
				return lesson_plan_response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/LessonPlan/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid LessonPlan r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				LessonPlan existProduct = s_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				r.setModified_by(jwtDetails.getUserId());
				r.setModified_username(jwtDetails.getUserName());
				s_service.saveLessonPlan(r);
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

	@DeleteMapping("/LessonPlan/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			s_service.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateLessonPlan/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			s_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getLessonPlan/{ac_year_id}")
	public ResponseEntity<Object> getLessonPlanByAcYear(@PathVariable Integer ac_year_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> lessonplan = s_service.getLessonPlanByAcYear(ac_year_id);
		ResponseEntity<Object> lessonplan_response= ResponseHandler.generateResponse(true, HttpStatus.OK, lessonplan);
		return lessonplan_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getLessonPlanBasedOnAcYearIdAndUserId/{ac_year_id}/{user_id}")
	public ResponseEntity<Object> getLessonPlanBasedOnAcYearIdAndUserId(@PathVariable Integer ac_year_id, @PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> lessonplan = s_service.getLessonPlanBasedOnAcYearIdAndUserId(ac_year_id,user_id);
		ResponseEntity<Object> lessonplan_response= ResponseHandler.generateResponse(true, HttpStatus.OK, lessonplan);
		return lessonplan_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	

	@GetMapping("/getLessonPlanByAcademicYearProgramSpecilizationCourseYearOrSemAndEmployeeId/{ac_year_id}/{program_id}/{program_specialization_id}/{course_assignment_id}/{year_sem}/{employee_id}/{ict_status}")
	public ResponseEntity<Object> getLessonPlanByAcademicYearProgramSpecilizationCourseYearOrSemAndEmployeeId(
			@PathVariable Integer ac_year_id, @PathVariable Integer program_id,
			@PathVariable Integer program_specialization_id, @PathVariable Integer course_assignment_id,
			@PathVariable Integer year_sem,@PathVariable Integer employee_id,@PathVariable Boolean ict_status) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> lessonplan = s_service
					.getLessonPlanByAcademicYearProgramSpecilizationCourseYearOrSemAndEmployeeId(ac_year_id, program_id,
							program_specialization_id, course_assignment_id, year_sem,employee_id,ict_status);
			ResponseEntity<Object> lessonplan_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					lessonplan);
			return lessonplan_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getLessonPlanDataByLessonId/{lesson_id}")
	public ResponseEntity<Object> getLessonPlanDataByLessonId(@PathVariable Integer lesson_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> lessonplan = s_service.getLessonPlanDataByLessonId(lesson_id);
		ResponseEntity<Object> lessonplan_response= ResponseHandler.generateResponse(true, HttpStatus.OK, lessonplan);
		return lessonplan_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getLessonPlanByAcademicYearProgramSpecilizationCourseYearOrSemAndEmployeeId/{ac_year_id}/{course_assignment_id}/{year_sem}/{employee_id}/{ict_status}")
	public ResponseEntity<Object> getLessonPlanByAcademicYearProgramSpecilizationCourseYearOrSemAndEmployeeId(
			@PathVariable Integer ac_year_id,  @PathVariable Integer course_assignment_id,
			@PathVariable Integer year_sem,@PathVariable Integer employee_id,@PathVariable Boolean ict_status) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> lessonplan = s_service
					.getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId(ac_year_id, course_assignment_id, year_sem,employee_id,ict_status);
			ResponseEntity<Object> lessonplan_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					lessonplan);
			return lessonplan_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId/{ac_year_id}/{course_assignment_id}/{year_sem}/{employee_id}/{ict_status}")
	public ResponseEntity<Object> getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId1(
			@PathVariable Integer ac_year_id,  @PathVariable Integer course_assignment_id,
			@PathVariable Integer year_sem,@PathVariable Integer employee_id ,@PathVariable Boolean ict_status) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> lessonplan = s_service
					.getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId1(ac_year_id, course_assignment_id, year_sem,employee_id,ict_status);
			ResponseEntity<Object> lessonplan_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					lessonplan);
			return lessonplan_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getLessonPlanByAcademicYearAndEmployeeId/{ac_year_id}/{user_id}")
	public ResponseEntity<Object> getLessonPlanByAcademicYearAndEmployeeId(
			@PathVariable Integer ac_year_id,@PathVariable Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> lessonplan = s_service.getLessonPlanByAcademicYearAndEmployeeId(ac_year_id,user_id);
			ResponseEntity<Object> lessonplan_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					lessonplan);
			return lessonplan_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getDataByLessonId/{lesson_id}")
	public ResponseEntity<Object> getDataByLessonId(@PathVariable Integer lesson_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> lessonplan = s_service.getDataByLessonId(lesson_id);
		ResponseEntity<Object> lessonplan_response= ResponseHandler.generateResponse(true, HttpStatus.OK, lessonplan);
		return lessonplan_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getLessonPlanForMobile/{ac_year_id}/{program_id}/{program_specialization_id}/{course_assignment_id}/{year_sem}/{employee_id}")
	public ResponseEntity<Object> getLessonPlanForMobile(
			@PathVariable Integer ac_year_id, @PathVariable String program_id,
			@PathVariable String program_specialization_id, @PathVariable Integer course_assignment_id,
			@PathVariable Integer year_sem,@PathVariable Integer employee_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return s_service.getLessonPlanForMobile(ac_year_id, program_id,
							program_specialization_id, course_assignment_id, year_sem,employee_id);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
		
}
