package com.au.controller;

import com.au.dto.*;
import com.au.exception.ResourceNotFoundException;
import com.au.model.*;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.au.service.*;
import com.au.util.EncryptionUtilAll;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class EmployeeDetailsController {

	Logger log = LoggerFactory.getLogger(EmployeeDetailsController.class);

	@Autowired
	private EmployeeDetailsService empDetails_service;

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private HodCommentsService hcs_service;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private TdsDeductionService tdsDeductionService;
	
	
	@PostMapping("/EmployeeDetails")
	public ResponseEntity<Object> saveEmployeeDetails(@RequestBody @Valid EmployeeDetails employeeDetails,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			employeeDetails.setCreated_by(jwtDetails.getUserId());
			employeeDetails.setCreated_username(jwtDetails.getUserName());
			EmployeeDetails emp_det = empDetails_service.saveEmployeeDetails(employeeDetails);
			ResponseEntity<Object> emp_det_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, emp_det);
			return emp_det_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	
	@PutMapping("/EmployeeMedicalHistory/{id}")
	public ResponseEntity<Object> updateEmployeeMedicalHistory(@RequestBody EmployeeMedicalHistoryDto dto,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			empDetails_service.updateEmployeeMedicalHistory(dto, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping(value="/uploadEmployeeIDsAttachment")
	public ResponseEntity<EmployeeIDsAttachment> uploadCandidateAttachment(@ModelAttribute EmployeeIDsAttachmentRequestDTO cwad) throws IOException{

		EmployeeIDsAttachment obj=	empDetails_service.uploadFile(cwad);
//			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return new ResponseEntity<EmployeeIDsAttachment>(obj, HttpStatus.OK);


			}
	
	@PostMapping(value="/uploadEmployeeContractsAttachment")
	public ResponseEntity<EmployeeContractsAttachment> uploadEmployeeContractsAttachment(@ModelAttribute EmployeeIDsAttachmentRequestDTO cwad) throws IOException{

		EmployeeContractsAttachment obj=	empDetails_service.uploadContractsFile(cwad);
//			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return new ResponseEntity<EmployeeContractsAttachment>(obj, HttpStatus.OK);


			}
	
	@GetMapping("/getEmployeeContractssAttachmentById/{empId}")
	public ResponseEntity<?> getEmployeeContractssAttachmentById(@PathVariable("empId") Integer empId ){
		List<EmployeeContractsAttachment> obj=	empDetails_service.getEmpContractsByEmpId(empId);
		return new ResponseEntity<>(obj, HttpStatus.OK);
		
	}
	
	@GetMapping("/getEmployeeIDsAttachmentById/{empId}")
	public ResponseEntity<?> getEmployeeIDsAttachmentByEmpId(@PathVariable("empId") Integer empId ){
		List<EmployeeIDsAttachment> obj=	empDetails_service.getEmpIdsByEmpId(empId);
		return new ResponseEntity<>(obj, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/employeeIDsAttachmenDeactivate/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Long id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			empDetails_service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllEmployeeDetails")
	public ResponseEntity<Object> getAllEmployeeDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword, 
			@RequestHeader("Authorization") String jwtToken ,
			@RequestParam(value="dept_id",required = false) Integer dept_id, @RequestParam(value="designation_id",required = false) Integer designation_id ,
			@RequestParam(value="job_type_id",required = false) Integer job_type_id, @RequestParam(value="school_id",required = false) Integer school_id) throws JsonParseException, JsonMappingException, IOException {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> employee_filtered =  empDetails_service.getAllDataFilteredByKeyword(pageable, keyword, jwtToken ,dept_id,school_id,designation_id,job_type_id);//,column,value);
			return employee_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> employee_sorted = empDetails_service.getAllSortedData(pageable1, jwtToken , dept_id,school_id,designation_id,job_type_id);
			return employee_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllEmployeeDetailsBasedOnUserId")
	public ResponseEntity<Object> fetchAllEmployeeDetailsBasedOnUserId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword, 
			@RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> employee_filtered =  empDetails_service.getAllDataFilteredByKeywordUserId(pageable, keyword, jwtToken);//,column,value);
			return employee_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> employee_sorted = empDetails_service.getAllSortedDataUserId(pageable1, jwtToken);
			return employee_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	
	@GetMapping("/fetchAllInActiveEmployeeDetails")
	public ResponseEntity<Object> fetchAllInActiveEmployeeDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> employee_filtered =  empDetails_service.getAllInActiveDataFilteredByKeyword(pageable, keyword);//,column,value);
			return employee_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> employee_sorted = empDetails_service.getAllInActiveSortedData(pageable1);
			return employee_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	

	@GetMapping("/EmployeeDetails")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EmployeeDetails> emp = empDetails_service.listAll();
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/activeEmployeeDetailsForProctor")
	public ResponseEntity<Object> employeeDetailsForProctor() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EmployeeDetails> emp = empDetails_service.activeEmployeeDetailsForProctor();
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/EmployeeDetails/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			List<Map<String, Object>> emp = empDetails_service.get(id);
			log.debug("request ---------{}", id);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/EmployeeDetails/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid EmployeeDetails employeeDetails,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			employeeDetails.setModified_by(jwtDetails.getUserId());
			employeeDetails.setModified_username(jwtDetails.getUserName());
			empDetails_service.saveEmployeeDetail(employeeDetails);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/EmployeeDetails/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		empDetails_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/SendEmailForEmployee/{school_id}/{dept_id}/{job_id}")
	public ResponseEntity<Object> saveEmployeeDetail(@RequestBody EmailRequest emails, @PathVariable Integer school_id,
			@PathVariable Integer dept_id, @PathVariable Integer job_id) throws IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		String content = "Dear Sir,\r\n" + "\r\n" + "\r\n"
				+ "Please check the attached resume and confirm us to schedule the candidate for interview.\r\n"
				+ "\r\n" + "Kindly login to ERP Portal to give comment.\r\n" + "\r\n" + "\r\n" + "\r\n" + "Regards\r\n"
				+ "\r\n" + "HR Dept,\r\n" + "Acharya Institutes\r\n" + "Bangalore";
		System.out.println(emails.getEmails());
		empDetails_service.sendMail(emails, content, "Fwd: Candidate for Interview Schedule.");
		List<HodComments> hod_comments = hcs_service.saveHodComments(emails.getHc());
		ResponseEntity<Object> hod_comments_response= ResponseHandler.generateResponse(true, HttpStatus.OK, hod_comments);
		return hod_comments_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/GetEmailForEmployees/{school_id}/{dept_id}")
	public ResponseEntity<Object> saveEmployeeDetails(@RequestBody @PathVariable Integer school_id, @PathVariable Integer dept_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			String[] emails = empDetails_service.getEmailId(school_id, dept_id);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emails);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/GetEmailForEmployee/{school_id}/{dept_id}")
	public ResponseEntity<Object> saveEmployeeDetailssss(@RequestBody @PathVariable Integer school_id,
			@PathVariable Integer dept_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> emp = empDetails_service.getEmailIds(school_id, dept_id);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getEmailForCourseAssign")
	public ResponseEntity<Object> getEmail(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> emp = empDetails_service.getEmailDetails();
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getProctorDetails")
	public ResponseEntity<Object> getProctorDetail(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> emp = empDetails_service.getProctorDetails();
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/GetEmployeeName/{school_id}")
	public ResponseEntity<Object> saveEmployeeNames(@PathVariable Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> emp = empDetails_service.getEmployeeNames(school_id);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/GetEmployeeNames")
	public ResponseEntity<Object> saveEmployeeName() {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String, Object>> emp = empDetails_service.getEmployeeName();
		ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
		return emp_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@DeleteMapping("/updateProctorHead/{emp_id}")
	public ResponseEntity<Object> update(@RequestBody EmployeeDetails employeeDetails, @PathVariable List<Integer> emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		empDetails_service.updateProctor(employeeDetails.getChief_proctor_id(),emp_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/EmployeeDetailsForBudgetExpense")
	public ResponseEntity<Object> EmployeeDetailsForBudgetExpense(@RequestHeader("Authorization") String jwtToken) 
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			UserAuthentication   user_details=uar_repo.findById(jwtDetails.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found:" + jwtDetails.getUserId()));
			List<HashMap<String , Object>> emp = empDetails_service.EmployeeDetailsForBudgetExpense(user_details.getEmail());
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping(value="/employeeDetailsUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute EmployeeDetailsFileRequest empfilerequest) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Hello-------" + auth.getDetails());
			log.debug("Message For VendorAttachment");
			empDetails_service.uploadFile(empfilerequest.getFile() , empfilerequest.getEmp_id(),empfilerequest.getImage_file() );
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}
	
	@GetMapping(path = "/employeeDetailsFileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileName") final String pathName) {
		try {
			final byte[] data = empDetails_service.downloadFile(pathName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
//	@GetMapping(path = "/employeeDetailsImageDownload")
//	public ResponseEntity<ByteArrayResource> downloadImageFile(@RequestParam("emp_image_attachment_path") final String pathName) {
//		
//			try {
//					final byte[] data = empDetails_service.downloadFile(pathName);
//					final ByteArrayResource resource = new ByteArrayResource(data);
//					return ResponseEntity.ok().contentLength(data.length).header("Content-type","image/jpeg")
//					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
//					.header("Cache-Control", "no-cache").body(resource);
//			} catch (NoSuchFileException e) {
//				System.out.println("image download NoSuchFileException " +e.getMessage());
//					return ResponseEntity.notFound().build();
//			} catch (Exception e) {
//					log.error(e.getMessage());
//					System.out.println("image download " +e.getMessage());
//					return ResponseEntity.badRequest().contentLength(0).body(null);
//			}
//			
//	}
	@GetMapping(path = "/employeeDetailsImageDownload")
	public ResponseEntity<ByteArrayResource> downloadImageFile(@RequestParam("emp_image_attachment_path") final String pathName) {
	    try {
	        System.out.println("📥 Requested image path: " + pathName);

	        byte[] data = empDetails_service.downloadFile(pathName);
	        ByteArrayResource resource = new ByteArrayResource(data);

	        return ResponseEntity.ok()
	                .contentLength(data.length)
	                .header("Content-Type", "image/jpeg")
	                .header("Content-Disposition", "attachment; filename=\"" + pathName + "\"")
	                .header("Cache-Control", "no-cache")
	                .body(resource);

	    } catch (NoSuchFileException e) {
	        System.err.println("🚫 NoSuchFileException: " + e.getMessage());
	        return ResponseEntity.notFound().build();

	    } catch (Exception e) {
	        System.err.println("❌ General Exception: " + e.getMessage());
	        e.printStackTrace(); // Print full stack for debug
	        return ResponseEntity.badRequest().contentLength(0).body(null);
	    }
	}


	
	@GetMapping(value = "/getRolesByEmployeeEmail/{email}")
	public ResponseEntity<Object> getRolesByEmployeeEmail(@PathVariable String email) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			List<Map<String, Object>> emp = empDetails_service.getRolesByEmployeeEmail(email);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	} 
	
//	@PostMapping("/emailToStaffsRegardingNewRecruit/{job_id}/{emp_id}")
//	public Object emailToStaffsRegardingNewRecruit(@PathVariable Integer job_id,@PathVariable Integer emp_id) throws Exception {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			try {
//				empDetails_service.sendMailToStaffs(job_id,emp_id);
//		        ResponseEntity<Object> offer_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
//				return offer_response;
//				} catch (NoSuchElementException e) {
//					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
//					return response;
//				}
//			} else {
//				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//				return rs;
//			}
//	}
	
	@PostMapping("/emailToStaffsRegardingNewRecruit")
	//public Object emailForOffer(@RequestParam("pdf_content") String pdf_content,@RequestParam("candidate_id") Integer candidate_id) throws Exception {
	public Object emailToStaffsRegardingNewRecruit(@RequestBody @Valid EmployeeDetails ed) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				empDetails_service.emailToStaffsRegardingNewRecruit(ed.getSalary_structure_email_content(),ed.getJob_id(),ed.getEmp_id());
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
	
	@PostMapping("/emailToReportingOfficerAndNewEmployee/{job_id}/{emp_id}")
	public Object emailToReportingOfficerAndNewEmployee(@PathVariable Integer job_id, @PathVariable Integer emp_id) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				empDetails_service.emailToReportingOfficerAndNewEmployee(job_id, emp_id);
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
	
	@PostMapping("/emailToPrincipalAndTeam/{job_id}/{emp_id}")
	public Object emailToPrincipalAndTeam(@PathVariable Integer job_id, @PathVariable Integer emp_id) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				empDetails_service.emailToPrincipalAndTeam(job_id, emp_id);
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
	
	@GetMapping(value = "/getEmployeeDataByUserID/{user_id}")
	public ResponseEntity<Object> getEmployeeDataByUserID(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			EmployeeDetails emp = empDetails_service.getEmployeeDataByUserID(user_id);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	} 
	
//	@GetMapping("/getEmployeesUnderDepartment/{emp_id}")
//	public ResponseEntity<Object> getEmployeesUnderDepartment(@PathVariable Integer emp_id) {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<Map<String, Object>> leaveApply = empDetails_service.getEmployeesUnderDepartment(emp_id);
//		ResponseEntity<Object> leave_Apply_response= ResponseHandler.generateResponse(true, HttpStatus.OK, leaveApply);
//		return leave_Apply_response;
//	} else {
//		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//		return rs;
//	}
//	}
	
	@GetMapping("/getEmployeesUnderDepartment/{emp_id}/{date}/{time_slots_id}")
	public ResponseEntity<Object> getEmployeesUnderDepartment(@PathVariable Integer emp_id,@PathVariable String date,
			@PathVariable Integer time_slots_id) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date selected_date=df.parse(date);
			List<Map<String, Object>> leaveApply = empDetails_service.getEmployeesUnderDepartment(emp_id,selected_date,time_slots_id,date);
		ResponseEntity<Object> leave_Apply_response= ResponseHandler.generateResponse(true, HttpStatus.OK, leaveApply);
		return leave_Apply_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	
}
	
	@PostMapping("/makeEmployeePermanent/{emp_id}")
	public Object makeEmployeePermanent(@PathVariable Integer emp_id, @RequestHeader("Authorization") String jwtToken,@RequestBody EmployeePermanentDto employeePermanentDto ) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				String message = empDetails_service.makeEmployeePermanent(emp_id, jwtDetails,employeePermanentDto.getPermanentRemarks());
				return ResponseHandler.generateResponse(true, HttpStatus.OK, message);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/employeeDetailsForTimeTableView/{time_table_id}/{selected_date}")
	public ResponseEntity<Object> employeeDetailsForTimeTableView(@PathVariable Integer time_table_id,@PathVariable String selected_date) throws ParseException{
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			 Date date = df.parse(selected_date);
			List<Map<String,Object>> employee_details=empDetails_service.employeeDetailsForTimeTableView(time_table_id,date);
			ResponseEntity<Object> employee_details_response=ResponseHandler.generateResponse(true, HttpStatus.OK, employee_details);
			return employee_details_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateEmployeeDetailsData/{id}")
	public ResponseEntity<Object> updateEmployeeDetailsData(@RequestBody EmployeeDetails edo,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			edo.setModified_by(jwtDetails.getUserId());
			edo.setModified_username(jwtDetails.getUserName());
			empDetails_service.updateEmployeeDetailsData(edo);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@PutMapping("/updateEmployeeById/{id}")
	public ResponseEntity<?> updateEmployeeById(@RequestBody @Valid EmployeeDetails ed, @PathVariable Integer id,@RequestHeader("Authorization")  String jwtToken)
	      throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					ed.setModified_by(jwtDetails.getUserId());
					ed.setModified_username(jwtDetails.getUserName());
					empDetails_service.updateEmployeeById(ed);
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
	
	@GetMapping("/getEmployeeDetailsData")
	public ResponseEntity<Object> getEmployeeDetailsData(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> emp = empDetails_service.getEmployeeDetailsData();
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getStaffDetailsData")
	public ResponseEntity<Object> getStaffDetailsData() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>>  emp = empDetails_service.getStaffDetailsData();
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getEmployeeDetailsData/{id}")
	public ResponseEntity<Object> getEmployeeDetailsData(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

				EmployeeDetails ed = empDetails_service.getEmployeeDetailsData(id);
				ResponseEntity<Object> ed_response= ResponseHandler.generateResponse(true, HttpStatus.OK, ed);
				return ed_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

//		@GetMapping("/getEmployeeDetailsForIdCard")
//		public ResponseEntity<Object> getEmployeeDataForIdCard(@RequestParam(value = "schoolId") Integer schoolId,
//				@RequestParam(value = "departmentId", required = false) Integer departmentId) {
//			if(RateLimitController.bucket.tryConsume(1)) {
//				List<Map<String, Object>>  emp = empDetails_service.getEmployeeDataForIdCard(schoolId,departmentId);
//				return ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
//			} else {
//				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			}
//		}

	@GetMapping("/getEmployeeDetailsForIdCard")
	public ResponseEntity<Object> getEmployeeDataForIdCard(
			@RequestParam(value = "deptId", required = false) Integer deptId,
			@RequestParam(value = "schoolId", required = false) Integer schoolId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>>  emp = empDetails_service.getEmployeeDataForIdCard(deptId,schoolId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/getEmployeeDetailsForIdCardWoHistory")
	public ResponseEntity<Object> getEmployeeDetailsForIdCardWoHistory(
			@RequestParam(value = "deptId", required = false) Integer deptId,
			@RequestParam(value = "schoolId", required = false) Integer schoolId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>>  emp = empDetails_service.getEmployeeDetailsForIdCardWoHistory(deptId,schoolId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@PutMapping("/EmployeeKeySkills/{id}")
	public ResponseEntity<Object> EmployeeKeySkills(@RequestBody EmployeeDetails edo,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			empDetails_service.updateEmployeeKeySkills(edo, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/deactivateEmployeeDetails/{emp_id}")
	public ResponseEntity<Object> deactivateEmployeeDetails(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			empDetails_service.deactivateEmployeeDetails(emp_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping(value = "/getEmployeeDetailsByUserID/{user_id}")
	public ResponseEntity<Object> getEmployeeDetailsByUserID(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			HashMap<String, Object> emp = empDetails_service.getEmployeeDetailsByUserID(user_id);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping(value = "/getEmployeeDetailsBasedOnUserID/{user_id}")
	public ResponseEntity<Object> getEmployeeDetailsBasedOnUserID(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			Map<String, Object> emp = empDetails_service.getEmployeeDetailsBasedOnUserID(user_id);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getEmployeeDetailsByJobId/{job_id}")
	public ResponseEntity<?> getEmployeeDetailsByJobId(@PathVariable Integer job_id ){
		if(RateLimitController.bucket.tryConsume(1)) {
			EmployeeDetails employeeDetails=empDetails_service.getEmployeeDetailsByJobId(job_id);
			ResponseEntity<Object> employeeDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetails);
			return employeeDetailsResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	

	@PutMapping("/updateEmployeeDetailsAfterRejoin/{emp_id}")
	public ResponseEntity<Object> updateEmployeeDetailsAfterRejoin(@RequestBody EmployeeDetails employeeDetails,
			@PathVariable Integer emp_id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			employeeDetails.setModified_by(jwtDetails.getUserId());
			employeeDetails.setModified_username(jwtDetails.getUserName());
			empDetails_service.updateEmployeeDetailsAfterRejoin(employeeDetails);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getEmployeeNameConcateWithEmployeeCodeAndDept")
	public ResponseEntity<Object> getEmployeeNameConcateWithEmployeeCodeAndDept() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> cos = empDetails_service.getEmployeeNameConcateWithEmployeeCodeAndDept();
		ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		return co_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/fetchAllNewJoineeDetailsData")
	public ResponseEntity<Object> fetchAllNewJoineeDetailsData(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> employee_filtered =  empDetails_service.getAllNewJoineeDetailsDataKeyword(pageable, keyword);//,column,value);
			return employee_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> employee_sorted = empDetails_service.getAllNewJoineeDetailsDataSortedData(pageable1);
			return employee_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping(value = "/getNewJoineeDetailsByEmpID/{emp_id}")
	public ResponseEntity<Object> getNewJoineeDetailsByEmpID(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			HashMap<String, Object> emp = empDetails_service.getNewJoineeDetailsByEmpID(emp_id);
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getEmployeeById/{id}")
	public ResponseEntity<Object> getEmployeeById(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
	    	EmployeeDetails product = empDetails_service.getEmployeeById(id);
			ResponseEntity<Object> bank_list_response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return bank_list_response_by_id;
			} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
	}
	 }else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}		
	}
	
	
//	public ResponseEntity<Object> getListOfEmployeeAttendence(@RequestBody EmployeeSheetRequestDTO employeeSheetRequestDTO){
//		return empDetails_service.getListOfEmployeeAttendence(employeeSheetRequestDTO);
//	}
	

	@GetMapping("/employeeAttendance")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value = "month",required = false) Integer month,
			@RequestParam(value = "year",required = false) Integer year,
			@RequestParam(value = "school_id",required = false) Integer school_id,
			@RequestParam(value = "dept_id",required = false) Integer dept_id,
			@RequestParam(value = "empTypeShortName",required = false) String empTypeShortName,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			if(empTypeShortName.equals("CON")) {
				if(dept_id != null && school_id !=null) {
					
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  empDetails_service.getAllDataFilteredByKeywordWithCon(pageable, keyword, month, year, school_id, dept_id, empTypeShortName);
			return oc_filtered;
			}else if(dept_id == null && school_id !=null){
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> oc_filtered =  empDetails_service.getAllDataFilteredByKeywordWithConAndWithSclId(pageable, keyword, month, year,school_id, empTypeShortName);
				return oc_filtered;
			}else if(dept_id != null && school_id ==null){
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> oc_filtered =  empDetails_service.getAllDataFilteredByKeywordWithConAndWithDeptId(pageable, keyword, month, year,dept_id, empTypeShortName);
				return oc_filtered;
			}else {
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> oc_filtered =  empDetails_service.getAllDataFilteredByKeywordWithConWoSclIdDeptId(pageable, keyword, month, year, empTypeShortName);
				return oc_filtered;
			}
				
			}else {
					if(dept_id != null && school_id !=null) {
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> oc_filtered =  empDetails_service.getAllDataFilteredByKeywordWoCon(pageable, keyword, month, year, school_id, dept_id);
					return oc_filtered;
					}else if(dept_id == null && school_id !=null) {
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> oc_filtered =  empDetails_service.getAllDataFilteredByKeywordWoConAndWithSclId(pageable, keyword, month, year,school_id);
						return oc_filtered;
					}else if(dept_id != null && school_id ==null) {
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> oc_filtered =  empDetails_service.getAllDataFilteredByKeywordWoAndWithDeptId(pageable, keyword, month, year,dept_id);
						return oc_filtered;
					}else {
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> oc_filtered =  empDetails_service.getAllDataFilteredByKeywordWoConSclIdDeptId(pageable, keyword, month, year);
						return oc_filtered;
						}
					}
		
		}else {
			if(empTypeShortName.equals("CON")) {
				if(dept_id != null && school_id !=null) {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = empDetails_service.getAllSortedDataWithCon(pageable1, month, year, school_id, dept_id, empTypeShortName);
			return oc_sorted;
			}else if(dept_id == null && school_id !=null) {
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> oc_filtered =  empDetails_service.getAllSortedKeywordWithConAndWithSclId(pageable, month, year,school_id, empTypeShortName);
				return oc_filtered;
			}else if(dept_id != null && school_id ==null) {
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> oc_filtered =  empDetails_service.getAllSortedKeywordWithConAndWithDeptId(pageable, month, year,dept_id, empTypeShortName);
				return oc_filtered;
			}else {
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> oc_filtered =  empDetails_service.getAllSortedKeywordWithConWoSclIdDeptId(pageable, month, year, empTypeShortName);
				return oc_filtered;
			}
				
				
			}else {
				if(dept_id != null && school_id !=null) {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> oc_sorted = empDetails_service.getAllSortedDataWoCon(pageable1, month, year, school_id, dept_id);
				return oc_sorted;
			  }else if(dept_id == null && school_id !=null) {
				  Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> oc_sorted = empDetails_service.getAllSortedDataWoConAndwithSclId(pageable1, month, year,school_id);
					return oc_sorted;
			  }else if(dept_id != null && school_id ==null) {
				  Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> oc_sorted = empDetails_service.getAllSortedDataWoConAndWithDeptId(pageable1, month, year,dept_id);
					return oc_sorted;
			  }else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> oc_sorted = empDetails_service.getAllSortedDataWoConSclIdDeptId(pageable1, month, year);
					return oc_sorted;
			  		}
				}
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
		
	
	@PostMapping("/getWorkingDays") 
	public ResponseEntity<?> getWorkingDays(@RequestParam("month") Integer month,
	  @RequestParam("year") Integer year ) {
	 return	empDetails_service.getWorkingDays(month,year);
		
	  }		
	
	@GetMapping("/getAttendanceOfEmployeeByEmployeeId/{emp_id}/{from_date}/{to_date}")
	public ResponseEntity<?> getAttendanceOfEmployeeByEmployeeId(@PathVariable Integer emp_id,@PathVariable String from_date,
			@PathVariable String to_date ) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
				
			SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM");
			Date fDate = inFormat.parse(from_date);
			Date TDate = inFormat.parse(to_date);
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!" +fDate);
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@ " +TDate);
			List<Map<String,Object>> employeeDetails=empDetails_service.getAttendanceOfEmployeeByEmployeeId(emp_id,fDate,TDate);
			ResponseEntity<Object> employeeDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetails);
			return employeeDetailsResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	

	@GetMapping("/getEmployeePayHistory")
	public ResponseEntity<?> getEmployeePayHistory(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size, @RequestParam(value="keyword",required = false) Object keyword,@RequestParam(value= "school_id",required = false) Integer school_id,
			@RequestParam(value="dept_id",required = false) Integer dept_id,@RequestParam(value= "month",required = false) Integer month,@RequestParam(value="year",required = false) Integer year ) {
		if(RateLimitController.bucket.tryConsume(1)) {
			PaginationDTO employeeDetails=empDetails_service.getEmployeePayHistory(school_id, dept_id, month, year,page,page_size,keyword);
			ResponseEntity<Object> employeeDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetails);
			return employeeDetailsResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping(value = "/uploadTdsDeductionFile",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadTdsDeductionFile(@RequestPart("file") MultipartFile file,@RequestParam("month") Integer month,@RequestParam("year") Integer year,
			@RequestHeader("Authorization") String jwtToken) {
		return tdsDeductionService.uploadTdsDeductionFile(file,month,year,jwtToken);
		
	}
	
	@PostMapping(value = "/saveTds")
	public ResponseEntity<Object> saveTds(@RequestBody TdsDeductionDTO tdsDeductionDTO,
			@RequestHeader("Authorization") String jwtToken) {
		return tdsDeductionService.saveTds(tdsDeductionDTO,jwtToken);
		
	}
	
	@GetMapping(value = "/getTdsList")
	public ResponseEntity<Object> getTdsList(@RequestParam(value="page") Integer page,@RequestParam(value="pageSize") Integer pageSize) {
		return tdsDeductionService.getTdsList(page,pageSize);
		
	}
	
	@GetMapping("/getPaySlipDetails")
	public ResponseEntity<?> getPaySlipDetails(@RequestParam("emp_pay_history_id") Integer emp_pay_history_id ) {
		if(RateLimitController.bucket.tryConsume(1)) {
			PaySlipDetails employeeDetails=empDetails_service.getPaySlipDetails(emp_pay_history_id);
			ResponseEntity<Object> employeeDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetails);
			return employeeDetailsResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateNewJoineeDetails/{id}")
	public ResponseEntity<Object> updateNewJoineeDetails(@RequestBody EmployeeDetails edo,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			empDetails_service.updateNewJoineeDetails(edo, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@PutMapping("/updateEmployeeDetailsUpdatableData/{id}")
	public ResponseEntity<Object> updateEmployeeDetailsUpdatableData(@RequestBody EmployeeDetailsUpdateDto edDto,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			empDetails_service.updateEmployeeDetailsUpdatableData(edDto, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}		
	
	@GetMapping("/fetchAllLeaveApplyDetailByApproverId/{user_id}")
	public ResponseEntity<Object> fetchAllLeaveApplyDetailByApproverId(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> empleave = empDetails_service.fetchAllLeaveApplyDetailByApproverId(user_id);
		ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, empleave);
		return empleaves_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@PostMapping(value = "/employeePermanentFileUpload")
	public ResponseEntity<Object> employeePermanentFileUpload(@ModelAttribute EmployeeDetailsFileRequest empfilerequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		empDetails_service.uploadFile(empfilerequest.getFile(), empfilerequest.getEmp_id());
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(path = "/employeePermanentFileViews")
	public ResponseEntity<ByteArrayResource> employeePermanentFileViews(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = empDetails_service.viewFiles(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@GetMapping("/getAllActiveEmployeeDetails")
	public ResponseEntity<Object> getAllActiveEmployeeDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> emp = empDetails_service.getAllActiveEmployeeDetails();
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getAllActiveEmployeeDetailsWithUserId")
	public ResponseEntity<Object> getAllActiveEmployeeDetailsWithUserId() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> emp = empDetails_service.getAllActiveEmployeeDetailsWithUserId();
			ResponseEntity<Object> emp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
			return emp_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	

	@PostMapping(value="/uploadImageFile") 
	public ResponseEntity<Object> uploadImageFile(@ModelAttribute EmployeeDetailsFileRequest empfilerequest) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Hello-------" + auth.getDetails());
			log.debug("Message For VendorAttachment");
			empDetails_service.uploadImageFile(empfilerequest.getImage_file1(),empfilerequest.getEmpId());
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}

	}	
	
	@PostMapping(value="/employeeDetailsUploadFile2")
	public ResponseEntity<Object> uploadFile2(@ModelAttribute EmployeeDetailsFileRequest empfilerequest) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Hello-------" + auth.getDetails());
			log.debug("Message For VendorAttachment");
			empDetails_service.uploadFile2(empfilerequest.getFile2() , empfilerequest.getEmp_id());
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}	
	
	@PostMapping("/rejoinEmployeeDetails/{emp_id}")
	public ResponseEntity<Object> rejoinEmployeeDetails(@PathVariable Integer emp_id,@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			EmployeeDetails emp_det = empDetails_service.rejoinEmployeeDetails(emp_id,jwtDetails);
			ResponseEntity<Object> emp_det_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, emp_det);
			return emp_det_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/getEmployeeDetailsForReporting")
	public ResponseEntity<Object> getEmployeeDetailsForReporting(){
	  return	empDetails_service.getEmployeeDetailsForReporting();
	}
	
	
	@PutMapping("/updateDeptAndSchoolOfEmployee/{emp_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid EmployeeDeptDto dto, 
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				empDetails_service.updateDeptByEmpId(dto, jwtToken);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);

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
	
	@PutMapping("/updateJobTypeOfEmployee/{emp_id}")
	public ResponseEntity<Object> updateJobTypeOfEmployee(@RequestBody @Valid EmployeeJobTypeDto dto, 
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				empDetails_service.updateJobTypeOfEmployee(dto, jwtToken);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);

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
	 
	@GetMapping("/getEmployeeDetailsForReportingById")
	public ResponseEntity<Object> getEmployeeDetailsForReportingById(@RequestParam("empId") Integer empId){
	  return	empDetails_service.getEmployeeDetailsForReportingById(empId);
	}
	
	@GetMapping("/getEmployeeDetailsForFeedbackReporting")
	public ResponseEntity<Object> getEmployeeDetailsForFeedbackReporting(){
	  return	empDetails_service.getEmployeeDetailsForFeedbackReporting();
	}
	
	
	
	@PostMapping(value = "/saveInvPayDetails")
	public ResponseEntity<Object> saveInvPayDetails(@RequestBody InvPayDTO invPayDto,
			@RequestHeader("Authorization") String jwtToken) {
		return empDetails_service.saveInvPayDetails(invPayDto,jwtToken); 
		
	} 
	
	@PostMapping(value = "/uploadInvPayFile",consumes =  { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadInvPayFile(@RequestPart("file") MultipartFile file,@RequestParam("month") Integer month,@RequestParam("year") Integer year,
			@RequestHeader("Authorization") String jwtToken) {
		return empDetails_service.uploadInvPayFile(file,month,year,jwtToken);
		
	}
	
	@GetMapping(value = "/getInvPayData")
	public ResponseEntity<Object> getInvPayData(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> employee_filtered =  empDetails_service.getAllInvPayDataFilteredByKeyword(pageable, keyword);//,column,value);
				return employee_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> employee_sorted = empDetails_service.getAllInvPaySortedData(pageable1);
				return employee_sorted;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}
	
	@PostMapping("/calculateMasterSalaryTrigger")
	public ResponseEntity<Object> calculateMasterSalaryTrigger(@RequestParam("month") Integer month,@RequestParam("year") Integer year,
															   @RequestParam(value = "empId",required = false) Integer empId){
		 triggerService.calculateMasterSalaryTrigger(month,year, empId);
		 return ResponseHandler.generateResponse(true, HttpStatus.OK, "Salary trigger started");
	}	
	
	@GetMapping("/getEmployeeMasterSalary")
	public ResponseEntity<?> getEmployeeMasterSalary(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size, @RequestParam(value="keyword",required = false) Object keyword,
			@RequestParam(value= "school_id",required = false) Integer school_id,
			@RequestParam(value="dept_id",required = false) Integer dept_id,@RequestParam(value= "month",required = false) Integer month,@RequestParam(value="year",required = false) Integer year ) {

		
			PaginationDTO employeeDetails=empDetails_service.getEmployeeMasterSalary(school_id, dept_id, month, year,page,page_size,keyword);
			ResponseEntity<Object> employeeDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetails);
			return employeeDetailsResponse;
	

	}
	
	@PostMapping("/calculateEmployeeSalaryTrigger")
	public ResponseEntity<?> calculateEmployeeSalaryTrigger(@RequestParam("month") Integer month, @RequestParam("year") Integer year,
															@RequestParam(value = "empId", required = false) Integer empId ) {
		String triggerName = "calculateEmployeeSalaryTrigger";
		String status = triggerService.getStatusOfTheTrigger(triggerName);
		if(status.equals("ACTIVE")){
			return ResponseHandler.generateResponse(true, HttpStatus.CONFLICT, "BioMetric Attendance Trigger already running", null);
		}
		triggerService.employeeSalaryTrigger(month, year, empId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "Salary Trigger Started",null);
	  }
	
	@PostMapping("/employeeLeaveTriggerForRejoinedEmployees") 
	public ResponseEntity<?> employeeLeaveTriggerForRejoinedEmployees() {
	 return	empDetails_service.employeeLeaveTriggerForRejoinedEmployees();
		
	  }
	
	
	@PostMapping("/employeeLeavePatternTrigger") 
	public ResponseEntity<?> employeeLeavePatternTrigger() {
		String triggerName = "employeeLeavePatternTrigger";
		String status = triggerService.getStatusOfTheTrigger(triggerName);
		if(status.equals("ACTIVE")){
			return ResponseHandler.generateResponse(true, HttpStatus.CONFLICT, "BioMetric Attendance Trigger already running", null);
		}
	 return	empDetails_service.employeeLeavePatternTrigger();
		
	  }	
	
	
	@GetMapping("/getEmpLeaveApproverBasedOnUserId/{user_id}")
	public ResponseEntity<Object> getEmpLeaveApproverBasedOnUserId(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			List<Map<String, Object>> product = empDetails_service.getEmpLeaveApproverBasedOnUserId(user_id);
			ResponseEntity<Object> response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return response_by_id;
	    } catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getDeptIdAndSchoolIdBasedOnUser/{user_id}")
	public ResponseEntity<Object> getDeptIdAndSchoolIdBasedOnUser(@PathVariable Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				Map<String, Object> product = empDetails_service.getDeptIdAndSchoolIdBasedOnUser(user_id);
				ResponseEntity<Object> response_by_id = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
				return response_by_id;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getEmpDetailsBasedOnApprover/{emp_id}")
	public ResponseEntity<Object> getEmpDetailsBasedOnApprover(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			List<Map<String, Object>> product = empDetails_service.getEmpDetailsBasedOnApprover(emp_id);
			ResponseEntity<Object> response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return response_by_id;
	    } catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/getEmployeeMasterSalaryById")
	public ResponseEntity<?> getEmployeeMasterSalaryById(@RequestParam("id") Integer id ) {

		return empDetails_service.getEmployeeMasterSalaryById(id);
	

	}	
	
	@GetMapping("/getSchoolDetailsBasedOnEmpId/{emp_id}")
	public ResponseEntity<Object> getSchoolDetailsBasedOnEmpId(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			Map<String, Object> product = empDetails_service.getSchoolDetailsBasedOnEmpId(emp_id);
			ResponseEntity<Object> response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return response_by_id;
	    } catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getDeptAndDesignationBasedOnEmpId/{emp_ids}")
	public ResponseEntity<Object> getDeptAndDesignationBasedOnEmpIds(@PathVariable String emp_ids) {
	    if (RateLimitController.bucket.tryConsume(1)) {
	        try {
	            // Call the service method with the emp_ids string
	            List<Map<String, Object>> productList = empDetails_service.getDeptAndDesignationBasedOnEmpIds(emp_ids);

	            // Return the response with the data
	            ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.OK, productList);
	            return response;
	        } catch (NoSuchElementException e) {
	            // Handle case where no data is found for the emp_ids
	            ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
	            return response;
	        }
	    } else {
	        // Handle rate-limiting scenario
	        ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	        return response;
	    }
	}
	
	
	@GetMapping("/getcountOfDesignationBasedOnHod/{leave_approver1_emp_id}")
	public ResponseEntity<Object> getcountOfDesignationBasedOnHod(@PathVariable Integer leave_approver1_emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			List<Map<String, Object>> product = empDetails_service.getcountOfDesignationBasedOnHod(leave_approver1_emp_id);
			ResponseEntity<Object> response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return response_by_id;
	    } catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getEmployeeDetailsDataBasedOnEmpId/{leave_approver1_emp_id}/{designation_id}")
	public ResponseEntity<Object> getEmployeeDetailsDataBasedOnEmpId(@PathVariable Integer leave_approver1_emp_id,@PathVariable Integer designation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			List<Map<String, Object>> product = empDetails_service.getEmployeeDetailsDataBasedOnEmpId(leave_approver1_emp_id,designation_id);
			ResponseEntity<Object> response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return response_by_id;
	    } catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
    @GetMapping("/getHodStudentCount/{leave_approver1_emp_id}")
	public ResponseEntity<Object> getHodStudentCount(@PathVariable Integer leave_approver1_emp_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> student =  empDetails_service.getHodStudentCount(leave_approver1_emp_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
    
    @GetMapping("/getCountOfEmployeeAndStrudent/{leave_approver1_emp_id}")
	public ResponseEntity<Object> getCountOfEmployeeAndStrudent(@PathVariable Integer leave_approver1_emp_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Map<String, Object> student =  empDetails_service.getCountOfEmployeeAndStrudent(leave_approver1_emp_id);
			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
			return student_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
    
    
    @GetMapping("/getEmployeeDetailsDataBasedOnReportId/{report_id}/{designation_id}")
	public ResponseEntity<Object> getEmployeeDetailsDataBasedOnReportId(@PathVariable Integer report_id,@PathVariable Integer designation_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			List<Map<String, Object>> product = empDetails_service.getEmployeeDetailsDataBasedOnReportId(report_id,designation_id);
			ResponseEntity<Object> response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return response_by_id;
	    } catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

    @GetMapping("/getHoiStudentCount/{report_id}")
   	public ResponseEntity<Object> getHoiStudentCount(@PathVariable Integer report_id) {
   		if (RateLimitController.bucket.tryConsume(1)) {
   			List<Map<String, Object>> student =  empDetails_service.getHoiStudentCount(report_id);
   			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
   			return student_response;
   		} else {
   			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
   					ResponseHandler.message1);
   			return rs;
   		}
   	}
 
    @GetMapping("/getCountOfEmployeeAndStrudentBasedOnHoi/{report_id}")
   	public ResponseEntity<Object> getCountOfEmployeeAndStrudentBasedOnHoi(@PathVariable Integer report_id) {
   		if (RateLimitController.bucket.tryConsume(1)) {
   			Map<String, Object> student =  empDetails_service.getCountOfEmployeeAndStrudentBasedOnHoi(report_id);
   			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
   			return student_response;
   		} else {
   			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
   					ResponseHandler.message1);
   			return rs;
   		}
   	}
    
    @GetMapping("/getCountOfDesignationBasedOnHoi/{report_id}")
   	public ResponseEntity<Object> getCountOfDesignationBasedOnHoi(@PathVariable Integer report_id) {
   		if (RateLimitController.bucket.tryConsume(1)) {
   			List<Map<String, Object>> student =  empDetails_service.getCountOfDesignationBasedOnHoi(report_id);
   			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
   			return student_response;
   		} else {
   			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
   					ResponseHandler.message1);
   			return rs;
   		}
   	}
    
    @GetMapping("/getCountOfJobTypeBasedOnHoi/{report_id}")
  	public ResponseEntity<Object> getCountOfJobTypeBasedOnHoi(@PathVariable Integer report_id) {
  		if (RateLimitController.bucket.tryConsume(1)) {
  			List<Map<String, Object>> student =  empDetails_service.getCountOfJobTypeBasedOnHoi(report_id);
  			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
  			return student_response;
  		} else {
  			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
  					ResponseHandler.message1);
  			return rs;
  		}
  	}
    
    @GetMapping("/getCountOfGenderBasedOnHoi/{report_id}")
  	public ResponseEntity<Object> getCountOfGenderBasedOnHoi(@PathVariable Integer report_id) {
  		if (RateLimitController.bucket.tryConsume(1)) {
  			List<Map<String, Object>> student =  empDetails_service.getCountOfGenderBasedOnHoi(report_id);
  			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
  			return student_response;
  		} else {
  			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
  					ResponseHandler.message1);
  			return rs;
  		}
  	}
    
    
    @GetMapping("/getCountOfDeptBasedOnHoi/{report_id}")
   	public ResponseEntity<Object> getCountOfDeptBasedOnHoi(@PathVariable Integer report_id) {
   		if (RateLimitController.bucket.tryConsume(1)) {
   			List<Map<String, Object>> student =  empDetails_service.getCountOfDeptBasedOnHoi(report_id);
   			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
   			return student_response;
   		} else {
   			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
   					ResponseHandler.message1);
   			return rs;
   		}
   	}
    
    @GetMapping("/getCountOfAgeGroupBasedOnHoi/{report_id}")
   	public ResponseEntity<Object> getCountOfAgeGroupBasedOnHoi(@PathVariable Integer report_id) {
   		if (RateLimitController.bucket.tryConsume(1)) {
   			List<Map<String, Object>> student =  empDetails_service.getCountOfAgeGroupBasedOnHoi(report_id);
   			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
   			return student_response;
   		} else {
   			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
   					ResponseHandler.message1);
   			return rs;
   		}
   	}
    
    
    @GetMapping("/getCountOfDateOfJoiningMonthWiseBasedOnHoi/{report_id}")
   	public ResponseEntity<Object> getCountOfDateOfJoiningMonthWiseBasedOnHoi(@PathVariable Integer report_id) {
   		if (RateLimitController.bucket.tryConsume(1)) {
   			List<Map<String, Object>> student =  empDetails_service.getCountOfDateOfJoiningMonthWiseBasedOnHoi(report_id);
   			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
   			return student_response;
   		} else {
   			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
   					ResponseHandler.message1);
   			return rs;
   		}
   	}
    
    
    @GetMapping("/getEmployeeDetailsBasedOnProctor")
   	public ResponseEntity<Object> getEmployeeDetailsBasedOnProctor() {
   		if (RateLimitController.bucket.tryConsume(1)) {
   			List<Map<String, Object>> student =  empDetails_service.getEmployeeDetailsBasedOnProctor();
   			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
   			return student_response;
   		} else {
   			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
   					ResponseHandler.message1);
   			return rs;
   		}
   	}
    
    
	@PutMapping("/updateProctorHead/{emp_id}")
	public ResponseEntity<Object> updateProctorHead(@RequestBody @Valid ProctorHeadDto proctorHeadDto,
			@PathVariable List<Integer> emp_id, @RequestHeader("Authorization") String jwtToken) throws Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
		
				empDetails_service.updateProctorHead(proctorHeadDto,jwtToken);
				ResponseEntity<Object> ph_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return ph_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
	
	
	@GetMapping("/fetchAllEmployeeDetailsBasedOnProctor")
	public ResponseEntity<Object> fetchAllEmployeeDetailsBasedOnProctor(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value= "school_id",required = false) Integer school_id,
			@RequestParam(value="dept_id",required = false) Integer dept_id,
			@RequestParam(value="emp_id",required = false) Integer emp_id,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> employee_filtered =  empDetails_service.fetchAllEmployeeDetailsBasedOnProctor(pageable, keyword ,school_id,dept_id,emp_id);//,column,value);
			return employee_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> employee_sorted = empDetails_service.fetchAllEmployeeDetailsBasedOnProctor(pageable1,school_id,dept_id,emp_id);
			return employee_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateContractEmpCodeOfEmployee/{emp_id}")
	public ResponseEntity<Object> updateContractEmpCodeOfEmployee(@RequestBody @Valid EmployeeCodeDto dto, 
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				empDetails_service.updateContractEmpCodeOfEmployee(dto, jwtToken);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);

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
	
	
	@PostMapping("/assignLeaveKitty")
	public ResponseEntity<Object> assignWlToEmpId(@RequestParam(required = false) Integer empId)
			throws JsonParseException, JsonMappingException, IOException {
				triggerService.leaveKittyForYearTrigger(empId);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "LeaveKittyForYear Trigger running", null);
		
	}
	
	
	@GetMapping("/getEmployeeWorkExperienceFileById/{empId}")
	public ResponseEntity<?> getEmployeeWorkExperienceFileById(@PathVariable("empId") Integer empId ){
		List<EmployeeContractsAttachment> obj=	empDetails_service.getEmployeeWorkExperienceFileById(empId);
		return new ResponseEntity<>(obj, HttpStatus.OK);
		}
	
	@DeleteMapping("/employeeWorkExperienceDeactivate/{id}")
	public ResponseEntity<Object> employeeWorkExperienceDeactivate(@PathVariable Long id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			empDetails_service.employeeWorkExperienceDeactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	
    @GetMapping("/getEmployeeDetailsDetailsDataBasedOnEmpId/{empId}")
   	public ResponseEntity<Object> getEmployeeDetailsDetailsDataBasedOnEmpId(@PathVariable Integer empId) {
   		if (RateLimitController.bucket.tryConsume(1)) {
   			List<Map<String, Object>> student =  empDetails_service.getEmployeeDetailsDetailsDataBasedOnEmpId(empId);
   			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
   			return student_response;
   		} else {
   			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
   					ResponseHandler.message1);
   			return rs;
   		}
   	}

	@GetMapping("/paySlipOfUser")
	public ResponseEntity<?> paySlipOfUser(@RequestParam("user_id") Integer user_id,@RequestParam("Date") String Date) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			PaySlipDetails paySlipDetails = empDetails_service.paySlipOfUser(user_id,Date);
	        return ResponseHandler.generateResponse(true, HttpStatus.OK, paySlipDetails);
		} else {
	        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	  @GetMapping("/getEmployeeDetailsDataBasedOnEmpId")
	   	public ResponseEntity<Object> getEmployeeDetailsDataBasedOnEmpId() {
	   		if (RateLimitController.bucket.tryConsume(1)) {
	   			List<Map<String, Object>> student =  empDetails_service.getEmployeeDetailsDataBasedOnEmpId();
	   			ResponseEntity<Object> student_response = ResponseHandler.generateResponse(true, HttpStatus.OK, student);
	   			return student_response;
	   		} else {
	   			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
	   					ResponseHandler.message1);
	   			return rs;
	   		}
	   	}
	  
	  @GetMapping("/getEmployeeDetailsDataBasedOnEmpId11111")
	  public ResponseEntity<Object> getEmployeeDetailsDataBasedOnEmpId11111(@RequestParam("empId") Integer empId) {
	      if (RateLimitController.bucket.tryConsume(1)) {
	          Map<String, Object> employeeDetails = empDetails_service.getEmployeeDetailsDataBasedOnEmpId11111(empId);
	          ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetails);
	          return response;
	      } else {
	          ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	          return rs;
	      }
	  }
	  
	  
		@GetMapping("/encryptedEmployeeDetails/{id}")
		public ResponseEntity<Object> encryptedEmployeeDetails(@PathVariable Integer id) {
			if (RateLimitController.bucket.tryConsume(1)) {
		          Map<String, Object> employeeDetails = empDetails_service.encryptedEmployeeDetails(id);
		          ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetails);
		          return response;
		      } else {
		          ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		          return rs;
		      }
		  }
		
		@GetMapping("/encryptedPaySlipOfUser")
		public ResponseEntity<?> encryptedPaySlipOfUser(@RequestParam("user_id") Integer user_id, @RequestParam("Date") String Date) throws ParseException {
		    if (RateLimitController.bucket.tryConsume(1)) {
		        PaySlipDetails paySlipDetails = empDetails_service.paySlipOfUser(user_id, Date);

		        try {
		            // Convert the PaySlipDetails to JSON string
		            ObjectMapper objectMapper = new ObjectMapper();
		            String jsonData = objectMapper.writeValueAsString(paySlipDetails);

		            // Encrypt the JSON string data
		            String encryptedData = EncryptionUtilAll.encrypt(jsonData);

		            // Create a response map with encrypted pay slip data
		            Map<String, Object> encryptedResponse = new HashMap<>();
		            encryptedResponse.put("encryptedPaySlipDetails", encryptedData);

		            return ResponseHandler.generateResponse(true, HttpStatus.OK, encryptedResponse);
		        } catch (Exception e) {
		            // Handle encryption error
		            e.printStackTrace();
		            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Error during encryption");
		        }
		    } else {
		        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		    }
		}	
		
		@GetMapping(path = "/employeeDetailsFileview")
		public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
			try {
				final byte[] data = empDetails_service.viewFiles1(fileName);
				final ByteArrayResource resource = new ByteArrayResource(data);
				return ResponseEntity.ok().contentLength(data.length).header("Content-type", "emp_id/pdf")
						.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
						.header("Cache-Control", "no-cache").body(resource);
			} catch (NoSuchFileException e) {
				return ResponseEntity.notFound().build();
			} catch (Exception e) {
				log.error(e.getMessage());
				return ResponseEntity.badRequest().contentLength(0).body(null);
			}

		}

	@GetMapping("/employeePayHistoryReport")
	public ResponseEntity<Object> employeePayHistoryReport(@RequestParam(value="schoolId",required = false) Integer schoolId,@RequestParam(value="deptId",required = false) Integer deptId, @RequestParam(value="month",required = false) Integer month,@RequestParam(value= "year",required = false) Integer year) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return empDetails_service.employeePayHistoryReport(schoolId, deptId, month, year);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);

		}
	}

	@GetMapping("/payReportOfEmployeeBySchoolAndBank")
	public ResponseEntity<Object> payReportOfEmployeeBySchoolAndBank(@RequestParam(value="month",required = false) Integer month,@RequestParam(value= "year",required = false) Integer year) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return empDetails_service.payReportOfEmployeeBySchoolAndBank(month, year);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);

		}
	}

	@GetMapping("/employeesCountFromAttendanceAndPaySheet")
	public ResponseEntity<Object> employeesCountFromAttendanceAndPaySheet(@RequestParam(value="month",required = false) Integer month,@RequestParam(value= "year",required = false) Integer year) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return empDetails_service.employeesCountFromAttendanceAndPaySheet(month, year);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);

		}
	}

	@GetMapping("/totalSalarySlipByMonthAndYear")
	public ResponseEntity<Object> totalSalarySlipByMonthAndYear(@RequestParam(value="month",required = false) Integer month,@RequestParam(value= "year",required = false) Integer year) {
		if(RateLimitController.bucket.tryConsume(1)) {
			return empDetails_service.totalSalarySlipByMonthAndYear(month, year);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);

		}
	}
	  
}