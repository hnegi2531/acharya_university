package com.au.controller;

import java.nio.file.NoSuchFileException;
import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.AcademicYearRequestDTO;
import com.au.dto.AttendanceResponseDTO;
import com.au.dto.LmsLoginRequest;
import com.au.response.ResponseHandler;
import com.au.service.LmsService;


@RestController
@RequestMapping("/api/${secretkey11}")
@CrossOrigin
public class LmsController {

	@Autowired
	private LmsService lmsService;
	
	
	
	@PostMapping("/login")
	public ResponseEntity<Object> lmsLogin(@RequestBody LmsLoginRequest lmsLoginRequest){
		return lmsService.lmsLogin(lmsLoginRequest);
	}	
	
	
	@PostMapping("/fetchTimeTableDetailsBySelectDateForEmployees")
	public ResponseEntity<Object> fetchTimeTableDetailsBySelectDateForEmployees(@RequestHeader("accessToken") String accessToken,
			@RequestParam(name="startingdate") String startingdate,@RequestParam(name="endingdate") String endingdate) throws ParseException {
	 return lmsService.fetchTimeTableDetailsBySelectDateForEmployees(accessToken,startingdate,endingdate);
		
	}
	
	@PostMapping("/fetchTimeTableDetailsBySelectDateForStudents")
	public ResponseEntity<Object> fetchTimeTableDetailsBySelectDateForStudents(@RequestHeader("accessToken") String accessToken,
			@RequestParam(name="startingdate") String startingdate,@RequestParam(name="endingdate") String endingdate) throws ParseException {
	 return lmsService.fetchTimeTableDetailsBySelectDateForStudents(accessToken,startingdate,endingdate);
		
	}	
	
	
	@GetMapping("/courses")
	public ResponseEntity<Object> getListofCoursesByEmployeeId(@RequestHeader("accessToken") String accessToken)  {
			return lmsService.getListofCoursesByEmployeeId(accessToken);
		}
	
	@GetMapping("/getAcademicYears")
	public ResponseEntity<Object> getAcademicYearsByEmployeeId(@RequestHeader("accessToken") String accessToken)  {
			return lmsService.getAcademicYearsByEmployeeId(accessToken);
	}
	
	@PostMapping("/getBatchAndSectionbyAcademicYear")
	public ResponseEntity<Object> getBatchAndSectionbyAcademicYear(@RequestHeader("accessToken") String accessToken,@RequestBody AcademicYearRequestDTO academicYearRequestDto) {
	  return lmsService.getBatchAndSectionbyAcademicYear(accessToken,academicYearRequestDto);
		
	}

	@GetMapping("/getStudentBatchAndSectionList/{time_table_id}")
	public ResponseEntity<Object> getStudentList(@RequestHeader("accessToken") String accessToken,@PathVariable("time_table_id") Integer timetableId )  {
			return lmsService.getStudentList(accessToken,timetableId);
	}
	
	@GetMapping("/getCurrentYearSectionAndBatchOfStudent")
	public ResponseEntity<Object> getCurrentYearSectionAndBatchOfStudent(@RequestHeader("accessToken") String accessToken){
		 return lmsService.getCurrentYearSectionAndBatchOfStudent(accessToken);
	}
	
	@GetMapping(path = "/getImage")
	public ResponseEntity<ByteArrayResource> downloadImageFile(@RequestHeader("accessToken") String accessToken,@RequestParam("imagepath") final String pathName) {
		
			try {
					final byte[] data = lmsService.downloadFile(accessToken,pathName);
					final ByteArrayResource resource = new ByteArrayResource(data);
					return ResponseEntity.ok().contentLength(data.length).header("Content-type","image/jpeg")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
			} catch (NoSuchFileException e) {
					return ResponseEntity.notFound().build();
			} catch (Exception e) {
					return ResponseEntity.badRequest().contentLength(0).body(null);
			}
			
	}
	
	@GetMapping("/studentDetailsByAuidForLms")
	public ResponseEntity<Object> getStudentDetailsByAuidForLms(@RequestParam("auid") String auid) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return lmsService.getStudentDetailsByAuidForLms(auid);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/assignedStudentDetailsToProctor")
	public ResponseEntity<Object> assignedStudentDetailsToProctor(@RequestHeader("accessToken") String accessToken) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return lmsService.assignedStudentDetailsToProctor(accessToken);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/schoolDetailsForLms")
	public ResponseEntity<Object> schoolDetailsForLms() {
		if (RateLimitController.bucket.tryConsume(1)) {
			return lmsService.schoolDetailsForLms();
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/studentListBySchoolSpecializationAcYearAndTokenFlag")
	public ResponseEntity<Object> studentListBySchoolSpecializationAcYearAndTokenFlag(@RequestParam("schoolId") Integer schoolId,
			@RequestParam("course_branch_assignment_id") Integer course_branch_assignment_id , @RequestParam("acYearId") Integer acYearId,
			@RequestParam("tokenFlag") Integer tokenFlag, @RequestHeader(value="accessToken",required = false) String accessToken) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return lmsService.studentListBySchoolSpecializationAcYearAndTokenFlag(schoolId,course_branch_assignment_id,acYearId,tokenFlag,accessToken);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/studentSubjects")
	public ResponseEntity<Object> studentSubjects(@RequestHeader(value="accessToken") String accessToken) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return lmsService.studentSubjects(accessToken);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/acYearDetails")
	public ResponseEntity<Object> acYearDetails(@RequestParam("tokenFlag") Integer tokenFlag) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return lmsService.acYearDetails(tokenFlag);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/specializationDetails")
	public ResponseEntity<Object> specializationDetails() {
		if (RateLimitController.bucket.tryConsume(1)) {
			return lmsService.specializationDetails();
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/batchOrSectionAssignedStudentDetails")
	public ResponseEntity<Object> batchOrSectionAssignedStudentDetails(@RequestParam(value="section_assignment_id",required = false) Integer section_assignment_id,
			@RequestParam(value="batch_assignment_id",required = false) Integer batch_assignment_id,@RequestHeader("accessToken") String accessToken) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return lmsService.batchOrSectionAssignedStudentDetails(section_assignment_id,batch_assignment_id,accessToken);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@PostMapping("/studentAttendanceByFaculty")
	public ResponseEntity<Object> studentAttendanceByFaculty(@RequestBody @Valid  AttendanceResponseDTO attendance,@RequestHeader("accessToken") String accessToken) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return lmsService.studentAttendanceByFaculty(attendance,accessToken);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
}
