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

import com.au.dto.InternalSessionCreationDto;
import com.au.dto.JwtDetails;
import com.au.model.ExternalMarks;
import com.au.model.InternalSessionCreation;
import com.au.response.ResponseHandler;
import com.au.service.InternalSessionCreationService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class InternalSessionCreationController {
	
	Logger log = LoggerFactory.getLogger(InternalSessionCreationController.class);

	@Autowired
	private InternalSessionCreationService internalSessionCreationService;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	

	@PostMapping("/createExternalMarks")
	public ResponseEntity<Object> saveInternalSessionCreation(@RequestBody @Valid List<InternalSessionCreation> isa,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			isa.stream().forEach(intSessCrea ->{
				intSessCrea.setCreated_by(jwtDetails.getUserId());
				intSessCrea.setCreated_username(jwtDetails.getUserName());
			});
			List<InternalSessionCreation> isas = internalSessionCreationService.saveInternalSessionCreation(isa);
			ResponseEntity<Object> InternalSessionCreation_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, isas);
		return InternalSessionCreation_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateExternalMark/{id}")
	public ResponseEntity<Object> updateExternalMark(@RequestBody @Valid InternalSessionCreation internalSessionCreation,
			@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
	    	internalSessionCreation.setModified_by(jwtDetails.getUserId());
	    	internalSessionCreation.setModified_username(jwtDetails.getUserName());
	    	internalSessionCreationService.updateExternalMark(internalSessionCreation);
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
	
	@GetMapping("/fetchAllExternalExamDetail")
	public ResponseEntity<Object> fetchAllExternalExamDetail(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> InternalSessionCreation_filtered =  internalSessionCreationService.getAllDataFilteredByKeywordExternal(pageable, keyword);
			return InternalSessionCreation_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> InternalSessionCreation_sorted = internalSessionCreationService.getAllSortedDataExternal(pageable1);
			return InternalSessionCreation_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping("/internalSessionAssignment1")
	public ResponseEntity<Object> saveInternalSessionCreation1(@RequestBody @Valid List<InternalSessionCreation> isa,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			isa.stream().forEach(intSessCrea ->{
				intSessCrea.setCreated_by(jwtDetails.getUserId());
				intSessCrea.setCreated_username(jwtDetails.getUserName());
			});
			List<InternalSessionCreation> isas = internalSessionCreationService.saveInternalSessionCreation1(isa);
			ResponseEntity<Object> InternalSessionCreation_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, isas);
		return InternalSessionCreation_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/internalSessionAssignment1")
	public ResponseEntity<Object> findAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<InternalSessionCreation> isas = internalSessionCreationService.findAll();
			ResponseEntity<Object> InternalSessionCreation_response= ResponseHandler.generateResponse(true, HttpStatus.OK, isas);
			return InternalSessionCreation_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getActiveInternalSessionAssignment1")
	public ResponseEntity<Object> listAllActiveInternalSessionCreation() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<InternalSessionCreation> isas = internalSessionCreationService.listAllActiveInternalSessionCreation();
			ResponseEntity<Object> InternalSessionCreation_response= ResponseHandler.generateResponse(true, HttpStatus.OK, isas);
			return InternalSessionCreation_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllInternalSessionAssignment1")
	public ResponseEntity<Object> getAllInternalTypesDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
															 @RequestParam(value="ac_year_id", required = false) Integer ac_year_id,
															 @RequestParam(value="school_id", required = false) Integer school_id,
															 @RequestParam(value="dept_id", required = false) Integer dept_id,
															 @RequestParam(value="program_specialization_id", required = false) Integer program_specialization_id,
															 @RequestParam(value="internal_short_name", required = false) String internal_short_name,
															 @RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> InternalSessionCreation_filtered =  internalSessionCreationService.getAllDataFilteredByKeyword(pageable, keyword ,ac_year_id,school_id,dept_id, program_specialization_id, internal_short_name);
			return InternalSessionCreation_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> InternalSessionCreation_sorted = internalSessionCreationService.getAllSortedData(pageable1,ac_year_id,school_id,dept_id, program_specialization_id, internal_short_name);
			return InternalSessionCreation_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/internalSessionAssignment1/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	InternalSessionCreation isas = internalSessionCreationService.get(id);
	    	ResponseEntity<Object> InternalSessionCreation_response= ResponseHandler.generateResponse(true, HttpStatus.OK, isas);
			return InternalSessionCreation_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	

	@PutMapping("/internalSessionAssignment1/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<InternalSessionCreation> isa, @PathVariable List<Integer> id,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
	    	isa.stream().forEach(data -> {
		    	data.setModified_by(jwtDetails.getUserId());
		    	data.setModified_username(jwtDetails.getUserName());
	    	});

	    	internalSessionCreationService.saveInternalSessionCreations(isa);
	    	ResponseEntity<Object> InternalSessionCreation_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return InternalSessionCreation_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@DeleteMapping("/internalSessionAssignment1/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			internalSessionCreationService.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activate1/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			internalSessionCreationService.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/internalDetailsForStudentAttendance1/{school_id}/{program_id}/{program_specialization_id}/{ac_year_id}/{year_sem}/{course_id}")
	public ResponseEntity<Object> internal_idbasedOnSessionAssignment(@PathVariable Integer school_id,@PathVariable Integer program_id,
			@PathVariable Integer program_specialization_id,@PathVariable Integer ac_year_id,
			@PathVariable Integer year_sem,@PathVariable  Integer course_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> itts1 = internalSessionCreationService.internal_session_idbasedOnSessionAssignment11(school_id,program_id,program_specialization_id,ac_year_id,year_sem,course_id);
			ResponseEntity<Object> internalTimeTable_response= ResponseHandler.generateResponse(true, HttpStatus.OK, itts1);
			return internalTimeTable_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/deactivateInternalSessionAssignment1/{internal_session_id}")
	public ResponseEntity<Object> deactivateInternalSessionCreation(@PathVariable Integer internal_session_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			String message = internalSessionCreationService.deactivateInternalSessionCreation(internal_session_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, message);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getProgramSpecializationFromInternalSessionAssignment1/{ac_year_id}/{internal_master_id}/{year_sem}")
	public ResponseEntity<Object> getProgramSpecializationFromInternalSessionCreation(@PathVariable Integer ac_year_id, @PathVariable Integer internal_master_id, @PathVariable Integer year_sem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> data = internalSessionCreationService.getProgramSpecializationFromInternalSessionCreation(ac_year_id,internal_master_id, year_sem);
		ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, data);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateInternalSessionAssignment1/{internal_session_id}")
	public ResponseEntity<Object> activateInternalSessionCreation(@PathVariable Integer internal_session_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			String message = internalSessionCreationService.activateInternalSessionCreation(internal_session_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, message);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllActiveInternalId1/{ac_year_id}/{program_specialization_id}/{year_sem}")
	public ResponseEntity<Object> getAllActiveInternalId(@PathVariable Integer ac_year_id, 
			@PathVariable Integer program_specialization_id, @PathVariable Integer year_sem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> data = internalSessionCreationService.getAllActiveInternalId(ac_year_id,program_specialization_id, year_sem);
		ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, data);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getAllActivecourseid1/{internal_session_id}/{year_sem}")
	public ResponseEntity<Object> getAllActivecourseid(@PathVariable Integer internal_session_id, @PathVariable Integer year_sem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> data = internalSessionCreationService.getAllActivecourseid(internal_session_id,year_sem);
		ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, data);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getAllActiveInternalSessionAssignment1")
	public ResponseEntity<Object> getAllActiveInternalSessionCreation() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<InternalSessionCreation> isas = internalSessionCreationService.getAllActiveInternalSessionCreation();
			ResponseEntity<Object> InternalSessionCreation_response= ResponseHandler.generateResponse(true, HttpStatus.OK, isas);
			return InternalSessionCreation_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	

	@GetMapping("/getInternalDetailsDataByAllParameters/{school_id}/{ac_year_id}/{internal_master_id}/{year_sem}/{program_specialization_id}")
	public ResponseEntity<Object> getInternalDetailsData(@PathVariable Integer school_id, @PathVariable Integer ac_year_id, 
			@PathVariable Integer internal_master_id, @PathVariable Integer year_sem,@PathVariable Integer program_specialization_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> data = internalSessionCreationService.getInternalDetailsData(school_id, ac_year_id,internal_master_id, year_sem,program_specialization_id);
				ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, data);
				return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@GetMapping("/getCoursesOnDateOfExamAndTimeSlotsId/{date_of_exam}/{time_slots_id}")
	public ResponseEntity<Object> getCoursesOnDateOfExamAndTimeSlots(@PathVariable String date_of_exam, @PathVariable Integer time_slots_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> coursesOnDateOfExamAndTimeSlots = internalSessionCreationService.getCoursesOnDateOfExamAndTimeSlots(date_of_exam, time_slots_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, coursesOnDateOfExamAndTimeSlots);
			return response;
		}	else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAssignedCoursesOnDateOfExamAndTimeSlotsIdAndRoomId/{date_of_exam}/{time_slots_id}/{room_id}")
	public ResponseEntity<Object> getAssignedCoursesOnDateOfExamAndTimeSlotsIdAndRoomId(@PathVariable String date_of_exam, @PathVariable Integer time_slots_id, @PathVariable Integer room_id){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> coursesOnDateOfExamAndTimeSlots = internalSessionCreationService.getAssignedCoursesOnDateOfExamAndTimeSlotsIdAndRoomId(date_of_exam, time_slots_id, room_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, coursesOnDateOfExamAndTimeSlots);
			return response;
		}	else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/studentsForExternalMarksAssignment")
	public ResponseEntity<Object> studentsForExternalMarksAssignment(@RequestParam(value = "acYearId") Integer acYearId,
																	 @RequestParam(value = "currentYear") Integer currentYear,
																	 @RequestParam(value = "currentSem") Integer currentSem,
																	 @RequestParam(value = "specializationId") Integer specializationId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return internalSessionCreationService.studentsForExternalMarksAssignment(acYearId, currentYear, currentSem,specializationId);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	

}
