package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.au.dto.FacultyDetailsForTimeTable;
import com.au.dto.JwtDetails;
import com.au.dto.TimeTableDto;
import com.au.model.Batch;
import com.au.model.ProgramSpecilization;
import com.au.model.TimeIntervalTypes;
import com.au.model.TimeTable;
import com.au.response.ResponseHandler;
import com.au.service.CourseAssignmentService;
import com.au.service.JwtTokenService;
import com.au.service.TimeIntervalTypesService;
import com.au.service.TimeTableService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
public class TimeTableController {

	@Autowired
	private TimeTableService timeTableService;
	
	@Autowired
	private CourseAssignmentService courseAssignmentService;
	
	@Autowired
	private TimeIntervalTypesService timeIntervalTypesService;
	

	Logger log = LoggerFactory.getLogger(TimeTableController.class);

	@Autowired
	private JwtTokenService jwtTokenService;

	//for same date
	@PostMapping("/TimeTable")
	public ResponseEntity<Object> saveTimeTable(@RequestBody @Valid TimeTableDto r, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			r.setCreated_by(jwtDetails.getUserId());
			r.setCreated_username(jwtDetails.getUserName());
			List<TimeTable> timeTable = timeTableService.saveTimeTableOnSec(r); //correct
			timeTableService.updateTimetableEmployee(timeTable,r);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, timeTable);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	//for different date
	@PostMapping("/createMultipleTimeTable")
	public ResponseEntity<Object> saveMultipleTimeTable(@RequestBody @Valid TimeTableDto r, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);

			r.setCreated_by(jwtDetails.getUserId());
			r.setCreated_username(jwtDetails.getUserName());

			List<TimeTable> timeTable = timeTableService.saveMultipleTimeTableOnSec(r);
			timeTableService.updateTimetableEmployee(timeTable,r);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, timeTable);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/TimeTable")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<TimeTable> listTimeTable = timeTableService.listAll();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listTimeTable);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/fetchAllTimeTableDetails")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				return timeTableService.listAll1(pageable, keyword);
			}else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				return timeTableService.listAll2(pageable1);
			}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/TimeTable/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				TimeTable product = timeTableService.get(id);
				return ResponseHandler.generateResponse(true,HttpStatus.OK, product);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}

	@PutMapping("/TimeTable/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid TimeTable r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				timeTableService.get(id);
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);

				r.setModified_by(jwtDetails.getUserId());
				r.setModified_username(jwtDetails.getUserName());

				timeTableService.saveTimeTable(r);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/TimeTable/{ids}")
	public ResponseEntity<Object> delete(@PathVariable List<Integer> ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			timeTableService.delete(ids);
			return  ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		}else {	
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/activateTimeTable/{ids}")
	public ResponseEntity<Object> delete1(@PathVariable List<Integer> ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			timeTableService.delete1(ids);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getTimeSlotsForTimeTable/{schoolId}")
	public ResponseEntity<Object> getTimeSlots(@PathVariable Integer schoolId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> listTimeTable = timeTableService.getTimeSlots(schoolId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listTimeTable);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getTimeSlotsForInternals/{schoolId}")
	public ResponseEntity<Object> getTimeSlotsForInternals(@PathVariable Integer schoolId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> listTimeTable = timeTableService.getTimeSlotsForInternals(schoolId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listTimeTable);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getAllEmployeesForTimeTable/{fromDate}/{toDate}/{timeSlotsId}/{day}")
	public ResponseEntity<Object> getAllEmployeesForTimeTable(@PathVariable String fromDate,@PathVariable String toDate,
			@PathVariable Integer timeSlotsId, @PathVariable String day) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(fromDate);
			Date date2=df.parse(toDate);
			List<Map<String,Object>> listTimeTable = timeTableService.getAllEmployeesForTimeTable(date1, date2, timeSlotsId, day);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listTimeTable);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getAllBatchesForTimeTable")
	public ResponseEntity<Object> getAllBatchesForTimeTable(@RequestParam(value = "school_id") Integer school_id,
			@RequestParam(value = "ac_year_id") Integer ac_year_id,
			@RequestParam(value = "current_year", required = false) Integer current_year,
			@RequestParam(value = "current_sem", required = false) Integer current_sem,
			@RequestParam(value = "interval_type_id", required = false) Integer interval_type_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if (current_sem != null) {
				List<Map<String, Object>> batchesOnSem = timeTableService.getAllBatchesForTimeTableOnSem(school_id, ac_year_id,
						current_sem, interval_type_id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, batchesOnSem);
			} else {
				List<Map<String, Object>> batchesOnYear = timeTableService.getAllBatchesForTimeTableOnYear(school_id, ac_year_id,
						current_year, interval_type_id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, batchesOnYear);
			}

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/getAllSectionsForTimeTable/{schoolId}/{programSpecializationId}/{acYearId}/{currentYearSem}")
	public ResponseEntity<Object> getAllSectionsForTimeTable(@PathVariable Integer schoolId,@PathVariable Integer programSpecializationId,
			@PathVariable Integer acYearId,@PathVariable Integer currentYearSem) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> sections = timeTableService.getAllSectionsForTimeTable(schoolId,programSpecializationId,acYearId,currentYearSem);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, sections);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/fetchAllCourseDetailsForTimeTable/{empIds}")
	public ResponseEntity<Object> getAllCourseDetailsForTimeTable(@PathVariable List<Integer> empIds) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> courseAmountDetailsByIds = courseAssignmentService.fetchAllCourseDetailsForTimeTable(empIds);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,courseAmountDetailsByIds);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	} 
	
	@GetMapping("/TimeIntervalTypesInSectionDropdownOfTimetable")
	public ResponseEntity<Object> getTimeIntervalTypesInSectionDropdown() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<TimeIntervalTypes> listTimeIntervalTypes = timeIntervalTypesService.getTimeIntervalTypesInSectionDropdown();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listTimeIntervalTypes);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/fetchIntervalTypesInBatchDropDownOfTimetable")
	public ResponseEntity<Object> getTimeIntervalTypesInBatchDropdown() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<TimeIntervalTypes> intervalTypes = timeIntervalTypesService.getTimeIntervalTypesInBatchDropdown();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, intervalTypes);
			
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/fetchEmployeeTimetableSchedule/{emp_ids}/{from_date}/{to_date}/{day}")
	public ResponseEntity<Object> getEmployeeTimetableSchedule(@PathVariable List<Integer> emp_ids, @PathVariable String from_date, 
			@PathVariable String to_date, @PathVariable String day) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date from_date1=df.parse(from_date);
			Date to_date1=df.parse(to_date);
			List<Map<String, Object>> intervalTypes = timeTableService.getEmployeeTimetableSchedule(emp_ids,from_date1,to_date1, day);
			ResponseEntity<Object> intervalTypes_response = ResponseHandler.generateResponse(true, HttpStatus.OK, intervalTypes);
			return intervalTypes_response;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	// index API for time table 
	@GetMapping("/fetchAllTimeTableDetailsForIndex")
	public ResponseEntity<Object> fetchAllAdministrationEmailIdsDetail(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,@RequestParam(value="ac_year_id") Integer ac_year_id,
			@RequestParam(value="current_sem", required = false) Integer current_sem,
			@RequestParam(value="current_year", required = false) Integer current_year,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  timeTableService.getAllDataFilteredByKeyword(pageable, keyword ,ac_year_id,current_sem,current_year);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = timeTableService.getAllSortedData(pageable1,ac_year_id,current_sem,current_year);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllTimeTableDetailsBasedOnAcYearIdAndUserId/{ac_year_id}/{User_id}")
	public ResponseEntity<Object> fetchAllTimeTableDetailsBasedOnAcYearIdAndUserId(@PathVariable Integer ac_year_id , @PathVariable Integer User_id) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> intervalTypes = timeTableService.fetchAllTimeTableDetailsBasedOnAcYearIdAndUserId(ac_year_id , User_id);
			ResponseEntity<Object> intervalTypes_response = ResponseHandler.generateResponse(true, HttpStatus.OK, intervalTypes);
			return intervalTypes_response;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		
	}
	
	
			

	
	@GetMapping("/fetchAllTimeTableDetailsBasedOnAcYearIdAndDeptId/{ac_year_id}/{dept_id}")
	public ResponseEntity<Object> fetchAllTimeTableDetailsBasedOnAcYearIdAndDeptId(@PathVariable Integer ac_year_id , @PathVariable Integer dept_id) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> intervalTypes = timeTableService.fetchAllTimeTableDetailsBasedOnAcYearIdAndDeptId(ac_year_id , dept_id);
			ResponseEntity<Object> intervalTypes_response = ResponseHandler.generateResponse(true, HttpStatus.OK, intervalTypes);
			return intervalTypes_response;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		
	}
	
	@GetMapping("/fetchAllTimeTableDetailsBasedOnAcYearIdAndSchoolId/{ac_year_id}/{school_id}")
	public ResponseEntity<Object> fetchAllTimeTableDetailsBasedOnAcYearIdAndSchoolId(@PathVariable Integer ac_year_id , @PathVariable Integer school_id) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> intervalTypes = timeTableService.fetchAllTimeTableDetailsBasedOnAcYearIdAndSchoolId(ac_year_id , school_id);
			ResponseEntity<Object> intervalTypes_response = ResponseHandler.generateResponse(true, HttpStatus.OK, intervalTypes);
			return intervalTypes_response;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		
	}
	
	// index API for time table section
		@GetMapping("/fetchAllSectionTimeTableDetailsForIndex/{acYearId}")
		public ResponseEntity<Object> fetchAllTimeTableDetailsForIndex(@PathVariable Integer acYearId) throws ParseException {
			if (RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> intervalTypes = timeTableService.fetchAllTimeTableDetailsForIndex(acYearId);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, intervalTypes);
				
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
						ResponseHandler.message1);
			}
			
		}
		
		//index API for time table batch
		@GetMapping("/fetchAllBatchTimeTableDetailsForIndex/{acYearId}")
		public ResponseEntity<Object> fetchAllBatchTimeTableDetailsForIndex(@PathVariable Integer acYearId) throws ParseException {
			if (RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> intervalTypes = timeTableService.fetchAllBatchTimeTableDetailsForIndex(acYearId);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, intervalTypes);
				
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
						ResponseHandler.message1);
			}
			
		}
		
	
	@GetMapping("/fetchYearSemForBatchTimeTable/{acYearId}/{schoolId}")
	public ResponseEntity<Object> fetchYearSemForBatchTimeTable(@PathVariable Integer acYearId, @PathVariable Integer schoolId)
			throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			Integer intervalTypes = timeTableService.fetchYearSemForBatchTimeTable(acYearId, schoolId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, intervalTypes);
			
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
		
	}
	
	@GetMapping("/timeTableDetailsOfStudentOrEmployeeForMobile")
	public ResponseEntity<Object> timeTableDetailsOfStudentOrEmployeeForMobile(
			@RequestParam(value = "student_id", required = false) Integer student_id,
			@RequestParam(value = "year", required = false) Integer year,@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "employee_id", required = false) Integer employee_id,@RequestHeader("Authorization") String jwtToken) throws ParseException, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {

			return timeTableService.timeTableDetailsOfStudentOrEmployeeForMobile(student_id,
					year,month,employee_id,jwtToken);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@PutMapping("/updateEmployeeIdForSwapping/{timeTableId}/{oldEmpId}/{newEmpId}/{subjectAssignmentId}")
	public ResponseEntity<Object> updateEmployeeIdForSwapping(@PathVariable Integer timeTableId, @PathVariable Integer oldEmpId,
			@PathVariable Integer newEmpId, @PathVariable Integer subjectAssignmentId, @RequestHeader("Authorization") String jwtToken)

			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				timeTableService.updateEmployeeIdForSwapping(timeTableId,oldEmpId,newEmpId,subjectAssignmentId);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	@GetMapping("/courseAssignedToEmployeeForStudentMarks")
	public ResponseEntity<Object> courseAssignedToEmployeeForStudentMarks(@RequestParam(value ="user_id") Integer user_id,
			@RequestParam(value ="ac_year_id") Integer ac_year_id,@RequestParam(value ="school_id") Integer school_id,
			@RequestParam(value ="program_id") Integer program_id,@RequestParam(value ="program_specialization_id") Integer program_specialization_id,
			@RequestParam(value ="section_id") Integer section_id,@RequestParam(value ="current_sem",required=false) Integer current_sem,
			@RequestParam(value ="current_year",required=false) Integer current_year){
		if (RateLimitController.bucket.tryConsume(1)) {

			List<Map<String,Object>> assignedCourseOfEmployee = timeTableService.courseAssignedToEmployeeForStudentMarks(user_id,ac_year_id,
					school_id,program_id,program_specialization_id,section_id,current_sem,current_year);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,assignedCourseOfEmployee);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getEmployeeMonthlyWorkedHours")
	public ResponseEntity<Object> getEmployeeMonthlyWorkedHours(@RequestParam(value = "from_month") String from_month,
			@RequestParam(value = "to_month") String to_month,@RequestParam(value = "school_id",required=false) Integer school_id,
			@RequestParam(value = "dept_id",required=false) List<Integer> dept_id) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM");
			Date date1=df.parse(from_month);
			Date date2=df.parse(to_month);
			List<HashMap<Object, Object>> assignedCourseOfEmployee = timeTableService.getEmployeeMonthlyWorkedHours(date1,date2,dept_id,school_id);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,assignedCourseOfEmployee);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/coursesAssignedDetailsForTimeTableView")
	public ResponseEntity<Object> coursesAssignedDetailsForTimeTAbleView(
			@RequestParam(value = "ac_year_id") Integer ac_year_id,
			@RequestParam(value = "school_id") Integer school_id,
			@RequestParam(value = "program_id") Integer program_id,
			@RequestParam(value = "program_specialization_id") Integer program_specialization_id,
			@RequestParam(value = "section_id") Integer section_id,
			@RequestParam(value = "current_sem", required = false) Integer current_sem,
			@RequestParam(value = "current_year", required = false) Integer current_year,
			@RequestParam(value = "selected_date") String selected_date
			) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {

			List<Map<String, Object>> assigned_course_of_employee = timeTableService.coursesAssignedDetailsForTimeTAbleView(
					ac_year_id, school_id, program_id, program_specialization_id, section_id, current_sem,
					current_year,selected_date);
			return ResponseHandler.generateResponse(true,HttpStatus.OK, assigned_course_of_employee);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getEmployeeSubjectDailyWorkedHours/{fromMonth}/{toMonth}/{empId}")
	public ResponseEntity<Object> getEmployeeSubjectDailyWorkedHours(@PathVariable String fromMonth, @PathVariable String toMonth,
			@PathVariable Integer empId) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(fromMonth);
			Date date2=df.parse(toMonth);
			List<HashMap<Object, Object>> assignedCourseOfEmployee = timeTableService.getEmployeeSubjectDailyWorkedHours(date1,date2,empId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,assignedCourseOfEmployee);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getEmployeesDailyWorkedHours/{fromMonth}/{toMonth}/{deptId}")
	public ResponseEntity<Object> getEmployeesDailyWorkedHours(@PathVariable String fromMonth, @PathVariable String toMonth,
			@PathVariable Integer deptId) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(fromMonth);
			Date date2=df.parse(toMonth);
			List<HashMap<Object, Object>> assignedCourseOfEmployee = timeTableService.getEmployeesDailyWorkedHours(date1,date2,deptId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,assignedCourseOfEmployee);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getSubjectWiseAllDetails/{empId}/{date}/{subjectAssignmentId}")
	public ResponseEntity<Object> getSubjectWiseAllDetails(@PathVariable Integer empId, @PathVariable String date,
			@PathVariable Integer subjectAssignmentId) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(date);
			List<Map<String, Object>> data = timeTableService.getEmployeesDailyWorkedHours(empId,date1,subjectAssignmentId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,data);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getTimeTableDataRoomWiseByFacilityType/{facilityTypeId}/{fromDate}/{toDate}")
	public ResponseEntity<Object> getTimeTableDataRoomWiseByFacilityType(@PathVariable Integer facilityTypeId, @PathVariable String fromDate,
			@PathVariable String toDate) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(fromDate);
			Date date2=df.parse(toDate);
			List<HashMap<Object, Object>> timeTableDataRoomwise = timeTableService.getTimeTableDataRoomWiseByFacilityType(date1,date2,facilityTypeId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,timeTableDataRoomwise);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getTimeTableDataRoomWiseByFacilityType1/{facilityTypeId}/{fromDate}")
	public ResponseEntity<Object> getTimeTableDataRoomWiseByFacilityType1(@PathVariable Integer facilityTypeId, @PathVariable String fromDate) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(fromDate);

			List<HashMap<Object,Object>> timeTableDataRoomWise = timeTableService.getTimeTableDataRoomWiseByFacilityType1(date1,facilityTypeId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,timeTableDataRoomWise);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getTimeTableData/{facilityTypeId}/{fromDate}/{toDate}")
	public ResponseEntity<Object> getTimeTableData(@PathVariable Integer facilityTypeId, @PathVariable String fromDate,
			@PathVariable String toDate) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = df.parse(fromDate);
			Date date2 = df.parse(toDate);
			Map<Object, Object> timeTableDataRoomWise = timeTableService.getTimeTableData(date1, date2, facilityTypeId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, timeTableDataRoomWise);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/fetchTimeTableDetailsForCalender/{userId}")
	public ResponseEntity<Object> timeTableDetailsOfUserForCalender(@PathVariable Integer userId) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> timeTableDetailsOfUserForCalender = timeTableService.timeTableDetailsOfUserForCalender(userId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, timeTableDetailsOfUserForCalender);
			
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);

		}
		
	}
	
	@GetMapping("/fetchTimeTableDetailsByEmployeeId/{employeeId}")
	public ResponseEntity<Object> fetchTimeTableDetailsByEmployeeId(@PathVariable Integer employeeId) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> fetchTimeTableDetailsByEmployeeId = timeTableService.fetchTimeTableDetailsByEmployeeId(employeeId);
			ResponseEntity<Object> fetchTimeTableDetailsByEmployeeIdResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, fetchTimeTableDetailsByEmployeeId);
			return fetchTimeTableDetailsByEmployeeIdResponse;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		
	}
	
	@GetMapping("/timeTableDetailsOfStudentForWeb/{id}")
	public ResponseEntity<Object> timeTableDetailsOfStudentForWeb(@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken) throws ParseException, JsonParseException, JsonMappingException, IOException{
		if (RateLimitController.bucket.tryConsume(1)) {
//			if(selected_date == null) {
//				selected_date =new Date().toString();
//			}
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			List<Map<String,Object>> time_table_details_of_student = timeTableService.timeTableDetailsOfStudentForWeb(id,jwtDetails);
			ResponseEntity<Object> time_table_details_of_student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,time_table_details_of_student);
			return time_table_details_of_student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@GetMapping("/getAllEmployeesForTimeTable/{from_date}/{to_date}/{time_slots_id}")
	public ResponseEntity<Object> getAllEmployeesForTimeTable(@PathVariable String from_date,@PathVariable String to_date,
			@PathVariable Integer time_slots_id) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(from_date);
			Date date2=df.parse(to_date);
			List<Map<String,Object>> list_time_table = timeTableService.getAllEmployeesForTimeTable(date1, date2, time_slots_id);
			ResponseEntity<Object> list_time_table_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_time_table);
			return list_time_table_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateRoomForSwapping/{timeTableId}/{roomId}")
	public ResponseEntity<Object> updateRoomForSwapping(@PathVariable Integer timeTableId,@PathVariable Integer roomId)

			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				timeTableService.updateRoomForSwapping(timeTableId,roomId);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/checkAttendanceStatus/{time_table_id}")
	public ResponseEntity<Object> checkAttendanceStatus(@PathVariable Integer time_table_id) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			Map<String, Object> checkAttendanceStatus = timeTableService.checkAttendanceStatus(time_table_id);
			ResponseEntity<Object> checkAttendanceStatusResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, checkAttendanceStatus);
			return checkAttendanceStatusResponse;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		
	}	
	
	@GetMapping("/fetchDetailsOfFacultyByTimeTableId/{time_table_id}/{emp_id}")
	public ResponseEntity<Object> fetchDetailsOfFacultyByTimeTableId(@PathVariable Integer time_table_id,@PathVariable Integer emp_id) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<FacultyDetailsForTimeTable> facultyDetails = timeTableService.fetchDetailsOfFacultyByTimeTableId(time_table_id,emp_id);
			ResponseEntity<Object> facultyDetailsResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, facultyDetails);
			return facultyDetailsResponse;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
		
	}
	
	@GetMapping("/programSpecilizationByBatchAssignmentIdOrSectionAssignmentId")
	public ResponseEntity<Object> programSpecilizationForOnBatchAssignmentIdOrSectionAssignmentId(
			@RequestParam(value = "batchAssignmentId", required = false) Integer batchAssignmentId,
			@RequestParam(value = "sectionAssignmentId", required = false) Integer sectionAssignmentId
			) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {

			List<ProgramSpecilization> programSpecilizationDeails = timeTableService.programSpecilizationForOnBatchAssignmentIdOrSectionAssignmentId(batchAssignmentId,sectionAssignmentId);
			ResponseEntity<Object> programSpecilizationDeailsResponse = ResponseHandler.generateResponse(true,
					HttpStatus.OK, programSpecilizationDeails);
			return programSpecilizationDeailsResponse;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/employeeTimeTableDetailsForMobile")
	public ResponseEntity<Object> employeeTimeTableDetailsForMobile(
			@RequestParam(value = "year") Integer year,@RequestParam(value = "month") Integer month,@RequestHeader("Authorization") String jwtToken) throws ParseException, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {

			return timeTableService.employeeTimeTableDetailsForMobile(year,month,jwtToken);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/fetchTimeTableDetailsForIndex")
	public ResponseEntity<Object> fetchTimeTableDetailsForIndex(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "ac_year_id", required = false) Integer ac_year_id,
			@RequestParam(value = "school_id", required = false) Integer school_id,
			@RequestParam(value = "dept_id", required = false) Integer dept_id,
			@RequestParam(value = "program_id", required = false) Integer program_id,
			@RequestParam(value = "program_specialization_id", required = false) Integer program_specialization_id,
			@RequestParam(value = "selected_date", required = false) String selected_dateStr,
			 @RequestParam(value = "from_date", required = false) String from_dateStr,
		        @RequestParam(value = "to_date", required = false) String to_dateStr,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value="current_sem", required = false) Integer current_sem,
			@RequestParam(value="current_year", required = false) Integer current_year,
			@RequestParam(value="active", required = false) Boolean active,
			@RequestParam(value = "keyword", required = false) Object keyword) throws ParseException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);

			Date selected_date = null;
	        String formattedDate = null;
	        Date from_date = null;
	        String formattedFromDate = null;
	        Date to_date = null;
	        String formattedToDate = null;
			
		

			if (selected_dateStr != null && !selected_dateStr.trim().isEmpty()) {
				selected_date = new SimpleDateFormat("yyyy-MM-dd").parse(selected_dateStr);
				// After parsing, format the date to "yyyy-MM-dd"
				formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(selected_date);
				System.out.println("Formatted Selected Date: " + formattedDate);
			}
			
			 // Parse and format from_date
	        if (from_dateStr != null && !from_dateStr.trim().isEmpty()) {
	            from_date = new SimpleDateFormat("yyyy-MM-dd").parse(from_dateStr);
	            formattedFromDate = new SimpleDateFormat("yyyy-MM-dd").format(from_date);
	            System.out.println("Formatted From Date: " + formattedFromDate);
	        }

	        // Parse and format to_date
	        if (to_dateStr != null && !to_dateStr.trim().isEmpty()) {
	            to_date = new SimpleDateFormat("yyyy-MM-dd").parse(to_dateStr);
	            formattedToDate = new SimpleDateFormat("yyyy-MM-dd").format(to_date);
	            System.out.println("Formatted To Date: " + formattedToDate);
	        }
			
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> oc_filtered = timeTableService.fetchTimeTableDetailsForIndexByKeyword(pageable, keyword,
						ac_year_id, school_id, dept_id, program_id,program_specialization_id, formattedDate,formattedFromDate, formattedToDate,current_sem,current_year ,userId,active );
				return oc_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> oc_sorted = timeTableService.fetchTimeTableDetailsForIndexWOKeyword(pageable1,ac_year_id,
						school_id, dept_id, program_id,program_specialization_id, formattedDate,formattedFromDate, formattedToDate, current_sem,current_year ,userId ,active);
				return oc_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
}
