package com.au.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import com.au.dto.*;
import com.au.service.TriggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.exception.ResourceNotFoundException;
import com.au.model.EmployeeDetails;
import com.au.model.RegistrationFeeTransaction;
import com.au.model.Student_Details;
import com.au.model.UserAuthentication;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.au.service.EmployeeDetailsService;
import com.au.service.JwtTokenService;
import com.au.service.StudentDetailsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.itextpdf.text.DocumentException;

import com.au.controller.RateLimitController;



@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class StudentDetailsController {

	Logger log = LoggerFactory.getLogger(StudentDetailsController.class);

	@Autowired
	private StudentDetailsService studentDetailsService;

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;

	@Autowired
	private EmployeeDetailsService employeeDetailsService;

	@Autowired
	TriggerService triggerService;
	
	@GetMapping("/getStudentDues/{studentId}")
	public ResponseEntity<Object> getStudentDues(@PathVariable Integer studentId){
		List<YearlyData> studentDues = studentDetailsService.getStudentDues(studentId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, studentDues);
			
	}
	
//	@GetMapping("/getStudentDuesByStudent/{studentId}")
//	public ResponseEntity<Object> getStudentDuesByStudent(@PathVariable Integer studentId){
//		List<YearlyData> studentDues = studentDetailsService.getStudentDues(studentId);
//		return ResponseHandler.generateResponse(true, HttpStatus.OK, studentDues);
//			
//	}

	@PostMapping("/Student_Details")
	public ResponseEntity<Object> saveStudentDetails(@RequestBody @Valid ApplicationDto stu,
			@RequestHeader("Authorization") String jwtToken){
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				Student_Details s1 = stu.getSd();
				s1.setCreated_by(jwtDetails.getUserId());
				s1.setCreated_username(jwtDetails.getUserName());
				stu.setSd(s1);
				Student_Details sd = studentDetailsService.getStuDetails(stu, jwtToken);
				ResponseEntity<Object> sd_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, sd);
				return sd_response;
			}catch(Exception e) {
				throw new RuntimeException(""+ e.getMessage());
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Student_Details")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Student_Details> sd_list = studentDetailsService.listAll();
			ResponseEntity<Object> sd_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sd_list);
			return sd_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/activeStudent_Details")
	public ResponseEntity<Object> activeStudentDetailsList() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Student_Details> sd_list = studentDetailsService.activeStudentDetailsList();
			ResponseEntity<Object> sd_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sd_list);
			return sd_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Student_Details/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				Student_Details sd = studentDetailsService.get(id);
				ResponseEntity<Object> sd_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sd);
				return sd_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/Student_Details/{id}")
	public ResponseEntity<Object> update(@RequestBody Student_Details st, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				Student_Details existProduct = studentDetailsService.get(id);
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				st.setModified_by(jwtDetails.getUserId());
				st.setModified_username(jwtDetails.getUserName());
				studentDetailsService.saveStudent_Details(st);
				ResponseEntity<Object> sd_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return sd_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/Student_Details/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			studentDetailsService.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/st1/{email}")
	public ResponseEntity<Object> studentEmailExistorNot(@PathVariable String email) {
		if (RateLimitController.bucket.tryConsume(1)) {
			String s_email = studentDetailsService.studentEmailExistorNot(email);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, s_email);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Student_DetailsAuid/{student_id}")
	public ResponseEntity<Object> auidNext(@PathVariable Integer student_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> stu_details = studentDetailsService.auidNext(student_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Student_Details1/{ac_year_id}/{school_id}/{program_id}/{program_specialization_id}")
	public ResponseEntity<Object> getAuidFilterCount(@PathVariable Integer ac_year_id, @PathVariable Integer school_id,
			@PathVariable Integer program_id, @PathVariable Integer program_specialization_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Integer i = studentDetailsService.getAuidFilterCount(ac_year_id, school_id, program_id, program_specialization_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, i);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchSectionAssignDetails/{ac_year_id}/{school_id}/{program_id}/{program_specialization_id}/{section_id}/{current_sem}/{current_year}")
	public ResponseEntity<Object> fetchSectionAssignDetails(@PathVariable Integer ac_year_id,
			@PathVariable Integer school_id, @PathVariable Integer program_id,
			@PathVariable Integer program_specialization_id, @PathVariable Integer section_id,
			@PathVariable Integer current_sem, @PathVariable Integer current_year) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> stu_details = studentDetailsService.fetchSectionAssignDetails(ac_year_id, school_id,
					program_id, program_specialization_id, section_id, current_sem, current_year);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getStudentList/{ac_year_id}/{school_id}/{program_id}/{program_specialization_id}")
	public ResponseEntity<Object> getStudentLists(@PathVariable Integer ac_year_id, @PathVariable Integer school_id,
			@PathVariable Integer program_id, @PathVariable Integer program_specialization_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> stu_details = studentDetailsService.getStudentlist(ac_year_id, school_id, program_id,
					program_specialization_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/updateProctorForStudent/{student_id}/{proctor_assign_id}")
	public ResponseEntity<Object> updateProctor(@PathVariable Integer student_id,@PathVariable Integer proctor_assign_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			studentDetailsService.updateproctor(student_id,proctor_assign_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/studentDetailsIndex")
	public ResponseEntity<Object> getStudentIndex(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,
			@RequestParam(value="ac_year_id",required = false) Integer ac_year_id, @RequestParam(value="school_id",required = false) Integer school_id,
			@RequestParam(value="program_id",required = false) Integer program_id,@RequestParam(value="program_specialization_id",required = false) Integer program_specialization_id,
			@RequestParam(value="fee_admission_category_id",required = false) Integer fee_admission_category_id,
			@RequestParam(value="userId",required = false) Integer userId,
			@RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> abc =  studentDetailsService.getStudentIndex2(pageable, keyword, ac_year_id,school_id,program_id,program_specialization_id,fee_admission_category_id,userId);
				return abc;
			}
			else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> xyz = studentDetailsService.getStudentIndex11(pageable1, ac_year_id,school_id,program_id,program_specialization_id,fee_admission_category_id,userId );
				return xyz;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return (ResponseEntity<Object>) rs;
		}		
	}
	
	
	@GetMapping("/inActiveStudentDetailsIndex")
	public ResponseEntity<Object> inActiveStudentDetailsIndex(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,
			@RequestParam(value="ac_year_id",required = false) Integer ac_year_id, @RequestParam(value="school_id",required = false) Integer school_id,
			@RequestParam(value="program_id",required = false) Integer program_id,@RequestParam(value="program_specialization_id",required = false) Integer program_specialization_id,
			@RequestParam(value="fee_admission_category_id",required = false) Integer fee_admission_category_id,
			@RequestParam(value="userId",required = false) Integer userId,
			@RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> abc =  studentDetailsService.getinActiveStudentDetailsIndex1(pageable, keyword, ac_year_id,school_id,program_id,program_specialization_id,fee_admission_category_id,userId);
				return abc;
			}
			else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> xyz = studentDetailsService.getinActiveStudentDetailsIndex2(pageable1, ac_year_id,school_id,program_id,program_specialization_id,fee_admission_category_id,userId );
				return xyz;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return (ResponseEntity<Object>) rs;
		}		
	}
	
	
	@GetMapping("/studentDetailsIndexCustomExpoet")
	public ResponseEntity<Object> studentDetailsIndexCustomExpoet(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="ac_year_id") Integer ac_year_id,
			@RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> abc =  studentDetailsService.getStudentIndexCustomExpoet(pageable, keyword, ac_year_id);
					return abc;
			}
			else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> xyz = studentDetailsService.getStudentIndexCustomExpoet1(pageable1, ac_year_id );
					return xyz;
			}
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return (ResponseEntity<Object>) rs;
	}		
}

	@GetMapping("/studentDetailsIndex/{ac_year_id}/{school_id}")
	public ResponseEntity<Object> getStudentIndex(@PathVariable Integer ac_year_id, @PathVariable Integer school_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> stu_details = studentDetailsService.getStudentIndex(ac_year_id, school_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/studentDetailsIndex/{ac_year_id}/{school_id}/{program_id}")
	public ResponseEntity<Object> getStudentIndex(@PathVariable Integer ac_year_id, @PathVariable Integer school_id,
			@PathVariable Integer program_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> stu_details = studentDetailsService.getStudentIndex(ac_year_id, school_id, program_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/studentDetailsIndex/{ac_year_id}/{school_id}/{program_id}/{program_specialization_id}")
	public ResponseEntity<Object> getStudentIndex(@PathVariable Integer ac_year_id, @PathVariable Integer school_id,
			@PathVariable Integer program_id, @PathVariable Integer program_specialization_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> stu_details = studentDetailsService.getStudentIndex(ac_year_id, school_id, program_id,
					program_specialization_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/studentDetailsForMobileApp")
	public ResponseEntity<Object> studentDetailsForMobileApp(@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try{
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				UserAuthentication user_details = userAuthenticationRepository.findById(jwtDetails.getUserId())
						.orElseThrow(() -> new ResourceNotFoundException("User Not Found:" + jwtDetails.getUserId()));
				if (user_details.getUsertype().equalsIgnoreCase("student")) {

					HashMap<String, Object> list3 = studentDetailsService.studentDetailsForMobileApp(user_details.getEmail());
					return ResponseHandler.generateResponse(true, HttpStatus.OK, list3);
				} else if (user_details.getUsertype().equalsIgnoreCase("staff")) {

					HashMap<String, Object> list4 = employeeDetailsService.EmployeeDetailsForMobileApp(user_details.getEmail());
					return ResponseHandler.generateResponse(true, HttpStatus.OK, list4);
				} else {
					return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
							HttpStatus.NOT_FOUND);
				}
			}catch(Exception e){
				log.error(" Mobile Api Profile Data : ",e.getMessage());
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,
						e.getMessage());
			}

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/studentDetailsByAuid/{auid}")
	public ResponseEntity<Object> getStudentDetail(@PathVariable String auid) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> stu_details = studentDetailsService.getStudentDetail(auid);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/inActiveStudentDetailsByAuid/{auid}")
	public ResponseEntity<Object> inActiveStudentDetailsByAuid(@PathVariable String auid) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> stu_details = studentDetailsService.inActiveStudentDetailsByAuid(auid);
			return  ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/fetchStudentDetailForSectionAssignment")
	public ResponseEntity<Object> fetchStudentDetailForSectionAssignment(@RequestParam(value ="ac_year_id",required = false) Integer ac_year_id,
			@RequestParam(value ="school_id") Integer school_id, @RequestParam(value ="program_id") Integer program_id,
			@RequestParam(value ="program_specialization_id") Integer program_specialization_id,
			@RequestParam(value ="current_sem",required = false) Integer current_sem,
			@RequestParam(value ="current_year",required = false) Integer current_year) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
			List<Map<String, Object>> stu_details_on_sem = studentDetailsService.fetchStudentDetailForSectionAssignmentOnSem(ac_year_id, school_id,
					program_id, program_specialization_id, current_sem);
			ResponseEntity<Object> student_response_on_sem = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details_on_sem);
			return student_response_on_sem;
			}else{
				List<Map<String, Object>> stu_details_on_year = studentDetailsService.fetchStudentDetailForSectionAssignmentOnYear(ac_year_id, school_id,
						program_id, program_specialization_id,  current_year);
				ResponseEntity<Object> student_response_on_year = ResponseHandler.generateResponse(true, HttpStatus.OK,
						stu_details_on_year);
				return student_response_on_year;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllStudentDetailForSectionAssignmentFromIndex/{school_id}/{program_id}/{program_specialization_id}/{current_year_sem}/{program_assignment_id}")
	public ResponseEntity<Object> fetchAllUnAssignedStudentDetailForSectionAssignmentFromIndex(@PathVariable Integer school_id,@PathVariable Integer program_id,
			@PathVariable Integer program_specialization_id,@PathVariable Integer current_year_sem, @PathVariable Integer program_assignment_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> stu_details = 
					studentDetailsService.fetchAllUnAssignedStudentDetailForSectionAssignmentFromIndex(school_id,program_id, program_specialization_id, current_year_sem,program_assignment_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllStudentDetailForSectionAssignmentForUpdate/{school_id}/{program_id}/{program_specialization_id}/{current_year_sem}/{section_id}/{program_Assignment_id}")
	public ResponseEntity<Object> fetchAllAssignedStudentDetailForSectionAssignmentForUpdate(@PathVariable Integer school_id,@PathVariable Integer program_id,
			@PathVariable Integer program_specialization_id,@PathVariable Integer current_year_sem,@PathVariable Integer section_id,@PathVariable Integer program_Assignment_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> stu_details = 
					studentDetailsService.fetchAllAssignedStudentDetailForSectionAssignmentForUpdate(school_id,program_id, program_specialization_id, current_year_sem,section_id,program_Assignment_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping(value = "/studentImageUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute StudentImageFileRequest studentimagefilerequest) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Hello-------" + auth.getDetails());
			log.debug("Message For Student Image");
			studentDetailsService.uploadFile(studentimagefilerequest.getStudent_id(), studentimagefilerequest.getImage_file());
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}
	
	@GetMapping(path = "/studentImageDownload")
	public ResponseEntity<ByteArrayResource> downloadImageFile(@RequestParam("student_image_attachment_path") final String pathName) {
		
			try {
					final byte[] data = studentDetailsService.downloadFile(pathName);
					final ByteArrayResource resource = new ByteArrayResource(data);
					return ResponseEntity.ok().contentLength(data.length).header("Content-type","image/jpeg")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
			} catch (NoSuchFileException e) {
					return ResponseEntity.notFound().build();
			} catch (Exception e) {
					log.error(e.getMessage());
					return ResponseEntity.badRequest().contentLength(0).body(null);
			}
			
	}
	
	@GetMapping("/fetchAllStudentDetailForStudentMarks/{school_id}/{program_assignment_id}/{program_id}/{program_specialization_id}/{current_year_sem}/{section_id}")
	public ResponseEntity<Object> fetchAllStudentDetailForStudentMarks(@PathVariable Integer school_id,@PathVariable Integer program_assignment_id,@PathVariable Integer program_id,
			@PathVariable Integer program_specialization_id,@PathVariable Integer current_year_sem,@PathVariable Integer section_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> stu_details = 
					studentDetailsService.fetchAllStudentDetailForStudentMarks(school_id,program_assignment_id,program_id, program_specialization_id, current_year_sem,section_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/studentViewDetailsByAuid/{auid}")
	public ResponseEntity<Object> getStudentViewDetailsByAuid(@PathVariable String auid) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> stu_details = studentDetailsService.getStudentViewDetailsByAuid(auid);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllStudentDetailsData/{student_id}")
	public ResponseEntity<Object> getAllStudentDetailsData(@PathVariable Integer student_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> stu_details = studentDetailsService.getAllStudentDetailsData(student_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getStudentDetailsForIdCard")
	public ResponseEntity<Object> getStudentDetailsForIdCard() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>>  student = studentDetailsService.getStudentDetailsForIdCard();
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getStudentDueDetails")
	public ResponseEntity<Object> getStudentDueDetails(@RequestParam(value="student_id") Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<StudentFeeDetails>  student = studentDetailsService.getStudentDueDetails(student_id);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getStudentFeeDetails")
	public ResponseEntity<Object> getStudentFeeDetails(@RequestParam(value="student_id") Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			StudentFeeDetailsDTO student = studentDetailsService.getStudentFeeDetails(student_id);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/studentDetailsDueIndexForReportedAndEligible")
	public ResponseEntity<Object> getStudentDetailsDueIndexForReportedAndEligible(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="ac_year_id") Integer ac_year_id,
			@RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword == null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> abc =  studentDetailsService.getStudentDetailsDueForReportedAndEligibleIndex1(pageable, ac_year_id);
					return abc;
			}
			else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> xyz = studentDetailsService.getStudentDetailsDueForReportedAndEligibleIndex2(pageable1,keyword ,ac_year_id );
					return xyz;
			}
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return (ResponseEntity<Object>) rs;
	}			
	}	

	@GetMapping("/studentDetailsDueIndexForNotReportedAndNotEligible")
	public ResponseEntity<Object> getStudentDetailsDueIndexForNotReportedAndNotEligible(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort,@RequestParam(value="ac_year_id") Integer ac_year_id,
				@RequestParam(value="keyword",required = false) Object keyword) {
			if(RateLimitController.bucket.tryConsume(1)) {
					Sort sorted = Sort.by(Direction.DESC, sort );
					if(keyword == null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> abc =  studentDetailsService.getStudentDetailsDueIndexForNotReportedAndNotEligibleIndex1(pageable, ac_year_id);
						return abc;
				}
				else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> xyz = studentDetailsService.getStudentDetailsDueIndexForNotReportedAndNotEligibleIndex2(pageable1,keyword ,ac_year_id );
						return xyz;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return (ResponseEntity<Object>) rs;
		}		
		
	}	
	
	@GetMapping("/fetchStudentDetailForInternals")
	public ResponseEntity<Object> fetchStudentDetailForSectionAssignment(
			@RequestParam(value ="program_specialization_id") Integer program_specialization_id,
			@RequestParam(value ="current_sem",required = false) Integer current_sem,
			@RequestParam(value ="current_year",required = false) Integer current_year) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if(current_sem != null) {
			List<Map<String, Object>> stu_details_on_sem = studentDetailsService.fetchStudentDetailForSectionAssignmentOnSem(program_specialization_id, current_sem);
			ResponseEntity<Object> student_response_on_sem = ResponseHandler.generateResponse(true, HttpStatus.OK,
					stu_details_on_sem);
			return student_response_on_sem;
			}else{
				List<Map<String, Object>> stu_details_on_year = studentDetailsService.fetchStudentDetailForSectionAssignmentOnYear(program_specialization_id,  current_year);
				ResponseEntity<Object> student_response_on_year = ResponseHandler.generateResponse(true, HttpStatus.OK,
						stu_details_on_year);
				return student_response_on_year;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/studentDetailsByStudentIds/{studentIds}")
	public ResponseEntity<Object> studentDetailsByStudentIds(@PathVariable List<Integer> studentIds) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Student_Details> sd_list = studentDetailsService.studentDetailsByStudentIds(studentIds);
			ResponseEntity<Object> sd_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, sd_list);
			return sd_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getTotalStudentAndFeeDueForProgram")
	public ResponseEntity<Object> getTotalStudentAndFeeDueForProgram(@RequestParam(value="program_id",required = false) Integer programId,@RequestParam(value="category_id",required = false) Integer categoryId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<TotalStudentCountAndFeeDueDto> student = studentDetailsService.getTotalStudentAndFeeDueForProgram(programId,categoryId);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getSumOfAllByCategoryId")
	public ResponseEntity<Object> getSumOfAllByCategoryId(@RequestParam(value="category_id",required = false) Integer categoryId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<SumOfAllFromStudentDuesDto> student = studentDetailsService.getSumOfAllByCategoryId(categoryId);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}		
	
	@GetMapping("/getStudentListWithTotalDues")
	public ResponseEntity<Object> getStudentListWithTotalDues(@RequestParam(value="program_id",required = false) Integer programId,@RequestParam(value="category_id",required = false) Integer categoryId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<StudentListWithTotalDues> student = studentDetailsService.getStudentListWithTotalDues(programId,categoryId);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getCourseDetailData/{student_id}")
	public ResponseEntity<Object> getCourseDetailData(@PathVariable Integer student_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>>  student = studentDetailsService.getCourseDetailData(student_id);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/studenDetailsForIdCard")
	public ResponseEntity<Object> studenDetailsForIdCard(@RequestParam(value = "schoolId") Integer schoolId,
			@RequestParam(value = "programAssignmentId") Integer programAssignmentId,
			@RequestParam(value = "programId", required = false) Integer programId,
			@RequestParam(value = "programSpecializationId", required = false) Integer programSpecializationId,
			@RequestParam(value = "currentYearOrSem") Integer currentYearOrSem) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<StudenDetailsForIdCard>  student = studentDetailsService.studenDetailsForIdCard(schoolId,programAssignmentId,programId,programSpecializationId,currentYearOrSem);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, student);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@PostMapping("/spotStudent_Details")
	public ResponseEntity<Object> saveSpotStudent_Details(@RequestBody @Valid ApplicationDto stu,
			@RequestHeader("Authorization") String jwtToken){
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				Student_Details s1 = stu.getSd();
				s1.setCreated_by(jwtDetails.getUserId());
				s1.setCreated_username(jwtDetails.getUserName());
				stu.setSd(s1);
				Student_Details sd = studentDetailsService.getSpotStuDetails(stu, jwtToken);
				ResponseEntity<Object> sd_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, sd);
				return sd_response;
			}catch(Exception e) {
				throw new RuntimeException(""+ e.getMessage());
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@GetMapping("/checkPreferredNameForEmail/{email_preferred_name}")
	public ResponseEntity<Object> checkPreferredNameForEmail(@PathVariable String email_preferred_name) {
		if(RateLimitController.bucket.tryConsume(1)) {
			String student = studentDetailsService.checkPreferredNameForEmail(email_preferred_name);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/checkAuidIsAlreadyPresentOrNot/{auid}")
	public ResponseEntity<Object> checkAuidIsAlreadyPresentOrNot(@PathVariable String auid) {
		if(RateLimitController.bucket.tryConsume(1)) {
			String student = studentDetailsService.checkAuidIsAlreadyPresentOrNot(auid);
			ResponseEntity<Object> student_response= ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@PutMapping("/updateUsnDetailsData/{auid}")
	public ResponseEntity<Object> updateUsnDetailsData(@RequestBody EmployeeUsnUpdateDto dto,@PathVariable String auid,
			@RequestHeader("Authorization") String jwtToken)
					throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			studentDetailsService.updateUsnDetailsData(dto, jwtToken);
			ResponseEntity<Object> std_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return std_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	

	@PostMapping("/studentDueReportTrigger")
	public ResponseEntity<Object> studentDueReportTrigger(@RequestParam(value="schoolId", required = false) Integer schoolId,@RequestParam(value="programId", required = false) Integer programId,@RequestParam(value="studentId", required = false) Integer studentId) {
		 return  studentDetailsService.studentDueReportTrigger(schoolId,programId,studentId);
		
	}

	@GetMapping("/getStudentDetailsBasedOnAuidAndStrudentId")
	public ResponseEntity<Object> getStudentDetailsBasedOnAuidAndStrudentId(@RequestParam(value = "auid", required = false) String auid,
			@RequestParam(value = "student_id", required = false) Integer student_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			if (auid != null) {
				List<Map<String, Object>>  student = studentDetailsService.getStudentDetailsBasedOnAuid(auid);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			}else {
				List<Map<String, Object>>  student = studentDetailsService.getStudentDetailsBasedOnStrudentId(student_id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			}
		
		}	else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/studentFeetemplate")
	public ResponseEntity<Object> studentFeetemplate(@RequestParam("studentId") Integer studentId) {
		 return  studentDetailsService.studentFeetemplate(studentId);
		
	}
	
	@GetMapping("/schoolWiseDueReport")
	public ResponseEntity<Object> schoolWiseDueReport(@RequestHeader("Authorization") String jwtToken) throws IOException {
		JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
		 return  studentDetailsService.schoolWiseDueReport(jwtDetails);
		
	}

	@GetMapping("/allSchoolWiseDueReport")
	public ResponseEntity<Object> allSchoolWiseDueReport()  {
		return  studentDetailsService.allSchoolWiseDueReport();

	}
	
	@GetMapping("/branchWiseDueReport")
	public ResponseEntity<Object> branchWiseDueReport(@RequestParam("schoolId") Integer schoolId) {
		 return  studentDetailsService.branchWiseDueReport(schoolId);
		
	}
	
	@GetMapping("/studentWiseDueReport")
	public ResponseEntity<Object> studentWiseDueReport(@RequestParam(value="sem", required=false) Integer sem,
			@RequestParam(value="schoolId", required=false) Integer schoolId,@RequestParam(value="programId", required=false) Integer programId, @RequestParam(value="programSpecializationId", required=false) Integer programSpecializationId,
			@RequestParam(value="pageSize", required=false) Integer pageNo,@RequestParam(value="pageNo", required=false) Integer offset) {
		 return  studentDetailsService.studentWiseDueReport(sem,schoolId,programId,programSpecializationId,pageNo,offset);
		
	}
	
	@PostMapping(value = "/studentImageUploadFileFromPhpServer")
	public ResponseEntity<Object> studentImageUploadFileFromPhpServer(@ModelAttribute StudentImageFileRequest studentimagefilerequest) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Hello-------" + auth.getDetails());
			log.debug("Message For Student Image");
			studentDetailsService.studentImageUploadFileFromPhpServer(studentimagefilerequest.getAuid(), studentimagefilerequest.getImage_file());
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}
	
	
	@GetMapping("/getStudentDetailsForPermission")
	public ResponseEntity<Object> getStudentDetailsForPermission(@RequestParam("studentId") Integer studentId) {
		 return  studentDetailsService.getStudentDetailsForPermission(studentId);
		
	}
	
	@PostMapping("/saveStudentForPermission")
	public ResponseEntity<Object> saveStudentForPermission(@RequestBody StudentPermissionRequest studentPermissionDTO, @RequestHeader("Authorization") String token) {
		 return  studentDetailsService.saveStudentForPermission(studentPermissionDTO.getStudentPermissionDTO(),token);
		
	}
	
	@PostMapping(value = "/uploadStudentPermissionFile",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadStudentPermissionFile(@RequestPart("file") MultipartFile file,@RequestPart("fileType") String fileType,@RequestPart("studentId") String studentId) {
		return studentDetailsService.uploadStudentPermissionFile(file, fileType,studentId);	
	}
	
	@GetMapping("/getTotalDueofStudent")
	public ResponseEntity<Object> getTotalDueofStudent(@RequestParam("studentId") Integer studentId,@RequestParam("currentSem") Integer currentSem) {
		 return  studentDetailsService.getTotalDueofStudent(studentId,currentSem);
		
	}
	
	@PostMapping("/saveExamPermission")
	public ResponseEntity<Object> saveExamPermission(@RequestBody ExamPermitDTO examPermitDTO) {
		 return  studentDetailsService.saveExamPermission(examPermitDTO);
		
	}

	@PostMapping("/updateStudentForPermission")
	public ResponseEntity<Object> updateStudentForPermission(@RequestBody StudentPermissionDTO studentPermissionDTO,@RequestHeader("Authorization") String token) {
		 return  studentDetailsService.updateStudentForPermission(studentPermissionDTO,token);
		
	}
	
	@GetMapping("/getStudentPermissionList")
	public ResponseEntity<Object> getStudentPermissionList() {
		 return  studentDetailsService.getStudentPermissionList();
		
	}
	
	@DeleteMapping("/deleteStudentPermission")
	public ResponseEntity<Object> deleteStudentPermission(@RequestParam("auid") String auid,@RequestParam("currentSem") Integer currentSem,@RequestParam("permissionType") String permissionType) {
		 return  studentDetailsService.deleteStudentPermission(auid,currentSem,permissionType);
		
	}
	

	@GetMapping("/activateStudentPermission")
	public ResponseEntity<Object> activateStudentPermission(@RequestParam("auid") String auid,@RequestParam("currentSem") Integer currentSem,@RequestParam("permissionType") String permissionType) {
		 return  studentDetailsService.activateStudentPermission(auid,currentSem,permissionType);
		
	}

	@GetMapping("/studentWiseDueReportByStudentId/{studentId}")
	public ResponseEntity<Object> studentWiseDueReportByStudentId(@PathVariable Integer studentId) {
		 return  studentDetailsService.studentWiseDueReportByStudentId(studentId);
		
	}


	@GetMapping(path = "/studentPermissionFileDownload")
	public ResponseEntity<ByteArrayResource> studentPermissionFileDownload(@RequestParam("pathName") final String pathName) {
		try {
			final byte[] data = studentDetailsService.studentPermissionFileDownload(pathName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (Exception e) {
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@GetMapping("/getStudentDetailsForTransaction")
	public ResponseEntity<Object> getStudentDetailsForTransaction(@RequestParam("studentId") Integer studentId) {
		 return  studentDetailsService.getStudentDetailsForTransaction(studentId);
		
	}
	
	@PostMapping("/studentTransaction")
	public ResponseEntity<Object> studentTransaction(@RequestBody StudentTransactionDTO studentTransactionDTO) {
		 return  studentDetailsService.studentTransaction(studentTransactionDTO);
		
	}
	
	@PostMapping("/bulkPayment")
	public ResponseEntity<Object> bulkPayment(@RequestBody BulkPaymentDTO bulkPaymentDTO) {
		 return  studentDetailsService.bulkPayment(bulkPaymentDTO);
		
	}
	
	
	@GetMapping("/getTransactionDetailsData")
	public ResponseEntity<Object> getTransactionDetailsData() {
		 return  studentDetailsService.getTransactionDetailsData();
		
	}
	
	
	
	@PostMapping("/paymentStatus")
	public ResponseEntity<Object> paymentStatus(@RequestBody PaymentRequestDTO paymentRequestDTO) {
		 return  studentDetailsService.paymentStatus(paymentRequestDTO);
		
	}



	@PostMapping("/emailToCandidateRegardingOfferLetter")
	public Object triggerEmailWithPdfContent(@ModelAttribute StudentOfferDto sd) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				studentDetailsService.triggerEmailToStudentWithPdfContent(sd.getCandidate_id(), sd.getFile());
				ResponseEntity<Object> offer_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return offer_response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response;
				}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	

	
	@PostMapping("/examFee")
	public ResponseEntity<Object> examFee(@RequestBody ExamFeeDTO examFeeDTO) {
		 return  studentDetailsService.examFee(examFeeDTO);
		
	}
	
	@PostMapping("/uniformFee")
	public ResponseEntity<Object> uniformFee(@RequestBody UniformFeeDTO uniformFeeDTO) {
		 return  studentDetailsService.uniformFee(uniformFeeDTO);
		
	}
	
	@GetMapping("/getTransactionDetails")
	public ResponseEntity<Object> getTransactionDetails(@RequestParam("studentId") Integer studentId) {
		 return  studentDetailsService.getTransactionDetails(studentId);
		
	}

	@PostMapping("/registrationFee")
	public ResponseEntity<Object> registrationFee(@RequestBody BulkPaymentDTO bulkPaymentDTO) {
		 return  studentDetailsService.registrationFee(bulkPaymentDTO);
		
	}
	

	
	@GetMapping("/studentNoDueStudentDetails")
	public ResponseEntity<Object> studentNoDueStudentDetails(@RequestParam(value="school_id") Integer school_id,
			@RequestParam(value="program_id") Integer program_id,
			@RequestParam(value="program_specialization_id") Integer program_specialization_id,
			@RequestParam(value="current_sem") Integer current_sem,
			@RequestParam(value="current_year") Integer current_year) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String, Object>> unassigned_school=studentDetailsService.studentNoDueStudentDetails(school_id,program_id,program_specialization_id,current_sem,current_year);
		ResponseEntity<Object> unassigned_school_response= ResponseHandler.generateResponse(true, HttpStatus.OK, unassigned_school);
		return unassigned_school_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	

	@GetMapping("/getRegistrationFeeDetails")
	public ResponseEntity<Object> getRegistrationFeeDetails(@RequestParam("candidateId") Integer candidateId) {
		 return  studentDetailsService.getRegistrationFeeDetails(candidateId);
		
	}

	@PostMapping("/registrationFeePaymentStatus")
	public ResponseEntity<Object> registrationFeePaymentStatus(@RequestBody PaymentRequestDTO paymentRequestDTO) {
		 return  studentDetailsService.registrationFeePaymentStatus(paymentRequestDTO);
		
	}
	
	@GetMapping("/getCandidateTransactionDetails")
	public ResponseEntity<Object> getCandidateTransactionDetails(@RequestParam("candidateId") Integer candidateId) {
		 return  studentDetailsService.getCandidateTransactionDetails(candidateId);
		
	}
	
	@GetMapping("/studentDetailsForChangeOfCourse/{oldStudentId}")
	public ResponseEntity<Object> studentDetailsForChangeOFCourse(@PathVariable Integer oldStudentId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> studentDetails = studentDetailsService.studentDetailsForChangeOFCourse(oldStudentId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					studentDetails);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	

	@PutMapping("/studentDetailsImageCheck/{auid}")
	public ResponseEntity<Object> studentDetailsImageCheck(@PathVariable String auid) {
		if (RateLimitController.bucket.tryConsume(1)) {
			studentDetailsService.studentDetailsImageCheck(auid);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					"File Checking Started");
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getStudentAddOnAndUniformStationaryAmount/{studentId}")
	public ResponseEntity<Object> getStudentAddOnAndUniformStationaryAmount(@PathVariable Integer studentId) {
		 return  studentDetailsService.getStudentAddOnAndUniformStationaryAmount(studentId);
		
	}
	
	@PatchMapping("updateStudentDetailsPartially/{student_id}")
    public ResponseEntity<Student_Details> updateStudent(@PathVariable Integer student_id, @RequestBody Map<String, Object> updates,
    		@RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
        try {
        	
        	JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
        	Student_Details updatedStudent = studentDetailsService.updateStudentDetails(student_id, updates, jwtDetails);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
	
    @PostMapping("/uniformFeePaymentStatus")
	public ResponseEntity<Object> uniformFeePaymentStatus(@RequestBody PaymentRequestDTO paymentRequestDTO) {
		 return  studentDetailsService.uniformFeePaymentStatus(paymentRequestDTO);
		
	}
    
    @GetMapping("/getUniformTransactionDetails")
	public ResponseEntity<Object> getUniformTransactionDetails(@RequestParam("studentId") Integer studentId) {
		 return  studentDetailsService.getUniformTransactionDetails(studentId);
		
	}
    

    @GetMapping("/studentDetailsByUser")
	public ResponseEntity<Object> studentDetailsByUser(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="acYearId") Integer acYearId,
			@RequestParam(value="keyword",required = false) Object keyword,@RequestParam(value="userId") Integer userId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				return  studentDetailsService.searchedAndSortedStudentDetailsByUser(pageable, keyword, acYearId,userId);
			}
			else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				return studentDetailsService.sortedStudentDetailsByUser(pageable1, acYearId,userId );
			}
		}else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}		
	}
    
    @GetMapping("/studentDetailsBySchoolId")
  	public ResponseEntity<Object> studentDetailsBySchoolId(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer page_size,
  			@RequestParam(value="sort") String sort,@RequestParam(value="acYearId") Integer acYearId,
  			@RequestParam(value="keyword",required = false) Object keyword,@RequestParam(value="school_id") Integer school_id) {
  		if(RateLimitController.bucket.tryConsume(1)) {
  			Sort sorted = Sort.by(Direction.DESC, sort );
  			if(keyword != null) {	
  				Pageable pageable = PageRequest.of(page, page_size,sorted);
  				return  studentDetailsService.searchedAndSortedStudentDetailsBySchoolId(pageable, keyword, acYearId,school_id);
  			}
  			else {
  				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
  				return studentDetailsService.sortedStudentDetailsBySchoolId(pageable1, acYearId,school_id );
  			}
  		}else {
  			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
  		}		
  	}
    
    @GetMapping("/studentDetailsByAdmissionCategory")
  	public ResponseEntity<Object> studentDetailsByAdmissionCategory(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer page_size,
  			@RequestParam(value="sort") String sort,@RequestParam(value="acYearId") Integer acYearId,
  			@RequestParam(value="keyword",required = false) Object keyword,@RequestParam(value="fee_admission_category_id") Integer fee_admission_category_id) {
  		if(RateLimitController.bucket.tryConsume(1)) {
  			Sort sorted = Sort.by(Direction.DESC, sort );
  			if(keyword != null) {	
  				Pageable pageable = PageRequest.of(page, page_size,sorted);
  				return  studentDetailsService.searchedAndSortedStudentDetailsByAdmissionCategory(pageable, keyword, acYearId,fee_admission_category_id);
  			}
  			else {
  				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
  				return studentDetailsService.sortedStudentDetailsByAdmissionCategory(pageable1, acYearId,fee_admission_category_id );
  			}
  		}else {
  			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
  		}		
  	}
    
    @GetMapping("/studentDetailsByDept")
  	public ResponseEntity<Object> studentDetailsByDept(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer page_size,
  			@RequestParam(value="sort") String sort,
  			@RequestParam(value="acYearId",required = false) Integer acYearId,
  			@RequestParam(value="keyword",required = false) Object keyword,
  			@RequestParam(value="dept_id",required = false) Integer dept_id ,
  			@RequestParam(value="school_id",required = false) Integer school_id) {
  		if(RateLimitController.bucket.tryConsume(1)) {
  			Sort sorted = Sort.by(Direction.DESC, sort );
  			if(keyword != null) {	
  				Pageable pageable = PageRequest.of(page, page_size,sorted);
  				return  studentDetailsService.studentDetailsByDept(pageable, keyword, acYearId,dept_id,school_id);
  			}
  			else {
  				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
  				return studentDetailsService.studentDetailsByDept(pageable1, acYearId,dept_id,school_id );
  			}
  		}else {
  			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
  		}		
  	}
    
   @GetMapping("/InactiveStudentDetailsByDept")
  	public ResponseEntity<Object> InactiveStudentDetailsByDept(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer page_size,
  			@RequestParam(value="sort") String sort,
  			@RequestParam(value="acYearId",required = false) Integer ac_year_id,
  			@RequestParam(value="keyword",required = false) Object keyword,
  			@RequestParam(value="dept_id",required = false) Integer dept_id ,
  			@RequestParam(value="school_id",required = false) Integer school_id) {
  		if(RateLimitController.bucket.tryConsume(1)) {
  			Sort sorted = Sort.by(Direction.DESC, sort );
  			if(keyword != null) {	
  				Pageable pageable = PageRequest.of(page, page_size,sorted);
  				return  studentDetailsService.InactiveStudentDetailsByDept1(pageable, keyword, ac_year_id,dept_id,school_id);
  			}
  			else {
  				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
  				return studentDetailsService.InactiveStudentDetailsByDept2(pageable1, ac_year_id,dept_id,school_id );
  			}
  		}else {
  			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
  		}		
  	}
   

    @PostMapping("/bulkPaymentStatus")
	public ResponseEntity<Object> bulkPaymentStatus(@RequestBody PaymentRequestDTO paymentRequestDTO) {
		 return  studentDetailsService.bulkPaymentStatus(paymentRequestDTO);
		
	}
    
    @GetMapping("/getBulkTransactionDetails")
  	public ResponseEntity<Object> getBulkTransactionDetails() {
  		 return  studentDetailsService.getBulkTransactionDetails();
  		
  	}
    
    @GetMapping("/getTransactionDetailsForPhp")
	public ResponseEntity<Object> getTransactionDetailsForPhp(@RequestParam(value= "month", required = false) Integer month,@RequestParam(value="year" , required = false) Integer year) {
		 return  studentDetailsService.getTransactionDetailsForPhp(month,year);
		
	}
    
    @GetMapping("/getStudentDetailsByAuid/{auid}")
   	public ResponseEntity<Object> getStudentDetailsByAuid(@PathVariable String auid) {
   		 return  studentDetailsService.getStudentDetailsByAuid(auid);
   		
   	}

    @PostMapping("/saveFineConcession")
   	public ResponseEntity<Object> saveFineConcession(@RequestBody FineConcessionDTO fineConcessionDTO) {
   		 return  studentDetailsService.saveFineConcession(fineConcessionDTO);
   		
   	}
    
    @GetMapping("/getTotalLateFee")
   	public ResponseEntity<Object> getTotalLateFee(@RequestParam(value = "auid") String
   			auid) {
   		 return  studentDetailsService.getTotalLateFee(auid);
   		
   	}
    
    @GetMapping("/getFineConcession")
   	public ResponseEntity<Object> getFineConcession() {
   		 return  studentDetailsService.getFineConcession();
   		
   	}
    
    @GetMapping("/getFineConcessionByAuid")
   	public ResponseEntity<Object> getFineConcessionByAuid(@RequestParam(value = "auid") String auid) {
   		 return  studentDetailsService.getFineConcessionByAuid(auid);
   		
   	}
    
    @PutMapping("/updateFineConcessionByAuid")
   	public ResponseEntity<Object> updateFineConcessionByAuid(@RequestBody FineConcessionDTO
   			fineConcessionDTO) {
   		 return  studentDetailsService.updateFineConcessionByAuid(fineConcessionDTO);
   		
   	}

    @PostMapping("/paymentCaptureFromRazorpay")
   	public ResponseEntity<Object> paymentCaptureFromRazorpay(@RequestBody String
   			payload) {
   		 return  studentDetailsService.paymentCaptureFromRazorpay(payload);
   		
   	}
    
    @PostMapping("/hostelFee")
	public ResponseEntity<Object> hostelFee(@RequestBody StudentTransactionDTO studentTransactionDTO) {
		 return  studentDetailsService.hostelFee(studentTransactionDTO);
		
	}
    

	@GetMapping("/getBulkPayTransaction")
	public ResponseEntity<Object> getBulkPayTransaction(@RequestParam(value="transaction_id" ,required = false) String transaction_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> studentDetails = studentDetailsService.getBulkPayTransaction(transaction_id);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					studentDetails);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
		}
	}

    @GetMapping("/uniformfeereceipts")
	public Map<String,Object> uniformfeereceipts(@RequestParam(value = "datee", required= false) String datee, @RequestParam(value = "lot", required = false) Integer lot, @RequestParam(value = "type", required = false) String type,  @RequestParam(value = "fromDate", required = false) String fromDate, @RequestParam(value = "toDate", required = false) String toDate,@RequestParam(value = "insname", required = false) String insname, @RequestParam(value = "auid", required = false) String auid ) {
		 return  studentDetailsService.uniformfeereceipts(datee, fromDate , toDate, lot,insname,type, auid);
		
	}
    
    @GetMapping("/validateauid")
   	public Map<String,Object> validateauid(@RequestParam("auid")  String auid ) {
   		 return  studentDetailsService.validateauid(auid);
   		
   	}
    
    @GetMapping("/uniformdetailstemplate")
   	public Map<String,Object> uniformdetailstemplate(@RequestParam("utemp_id")  Integer utemp_id ) {
   		 return  studentDetailsService.uniformdetailstemplate(utemp_id);
   		
   	}
    
    @GetMapping("/uniformadmission-count")
   	public Map<String,Object> uniformadmissioncount() {
   		 return  studentDetailsService.uniformadmissioncount();
   		
   	}

    @GetMapping("/uniformnames")
   	public Map<String,Object> uniformnames(@RequestParam("ac_year_id")  Integer ac_year_id) {
   		 return  studentDetailsService.uniformnames(ac_year_id);
   		
   	}
    
    @GetMapping("/acyear")
   	public Map<String,Object> acyear() {
   		 return  studentDetailsService.acyear();
   		
   	}
    
    @GetMapping("/institutes")
   	public Map<String,Object> institutes() {
   		 return  studentDetailsService.institutes();
   		
   	}
   
    @GetMapping("/getCountOfAdmissionDate")
   	public List<Map<String,Object>> getCountOfAdmissionDate(@RequestParam("date_of_admission")  String date_of_admission ,
   			@RequestParam(value="school_id" ,required = false) Integer school_id ) {
   		 return  studentDetailsService.getCountOfAdmissionDate(date_of_admission ,school_id);
   		
   	}
    
    @GetMapping("/registrationFeeDetailsForStudent")
   	public List<Map<String,Object>> registrationFeeDetailsForStudent(@RequestParam("studentId")  Integer studentId) {
   		 return  studentDetailsService.getRegistrationFeeDetailsOfStudent(studentId);
   		
   	}
    
    
    @GetMapping("/getStudentDetailsBasedOnUserId")
   	public Map<String, Object> getStudentDetailsBasedOnUserId(@RequestParam("userId")  Integer userId) {
   		 return  studentDetailsService.getStudentDetailsBasedOnUserId(userId);
   		
   	}
	   @PostMapping("/registrationFeeReceiptGeneration")
	public ResponseEntity<Object> registrationFeeReceiptGenerationForPendingRequest()
	   {
		   triggerService.registrationFeeReceiptGenerationForPendingRequest();
		   return ResponseHandler.generateResponse1(true, HttpStatus.OK, "Registration Fee Receipt Generating");
	   }

	   @PostMapping("/generateFeeReceipts")
	   public ResponseEntity<Object> generateFeeReceipts(@RequestParam("orderId") String orderId)
	   {
		   triggerService.generateFeeReceiptTrigger(orderId);
		   return ResponseHandler.generateResponse1(true, HttpStatus.OK, "generateFeeReceipts from bank import");
	   }

	@GetMapping("/studentsForPaidAtBoardTag/{feeTemplateId}/{yearOrSem}")
	public ResponseEntity<Object> studentsForPaidAtBoardTag(@PathVariable  Integer feeTemplateId,@PathVariable  Integer yearOrSem) {
		return  studentDetailsService.studentsForPaidAtBoardTag(feeTemplateId,yearOrSem);

	}

	@PostMapping("/generateAllFeeReceipts")
	public ResponseEntity<Object> generateFeeReceipts()
	{
		triggerService.generateAllFeeReceipts();
		return ResponseHandler.generateResponse1(true, HttpStatus.OK, "generateAllFeeReceipts from bank import");
	}

	@PostMapping("/generateAddOnReceipt")
	public ResponseEntity<Object> generateAddOnReceipt(@RequestParam("orderId") String orderId)
	{
		return studentDetailsService.generateCmaReceipt(orderId);
	}

	@PostMapping("/generateUniformLooseReceipt")
	public ResponseEntity<Object> generateUniformLooseReceipt(@RequestParam("orderId") String orderId)
	{
		studentDetailsService.generateUniFormReceipt(orderId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", orderId);
	}

	@PostMapping("/generateUniformPackageReceipt")
	public ResponseEntity<Object> generateUniformPackageReceipt(@RequestParam("orderId") String orderId)
	{
		studentDetailsService.generateUniFormPackageReceipt(orderId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", orderId);
	}

	@PostMapping("/generateAddOnReceiptForBulk")
	public ResponseEntity<Object> generateAddOnReceiptForBulk(@RequestParam("orderId") String orderId)
	{
		studentDetailsService.generateCmaReceiptForBulk(orderId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", orderId);
	}
}