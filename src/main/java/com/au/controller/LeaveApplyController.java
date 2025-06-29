package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
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

import com.au.dto.JobFileRequest;
import com.au.dto.JwtDetails;
import com.au.dto.LeaveApplyDto;
import com.au.dto.LeaveApplyUpdateDto;
import com.au.dto.LeaveCancelDTO;
import com.au.model.EmployeeDetails;
import com.au.model.LeaveApply;
import com.au.model.LeaveType;
import com.au.repository.LeaveApplyRepository;
import com.au.repository.LockDateRepository;
import com.au.response.ResponseHandler;
import com.au.service.EmployeeLeavesService;
import com.au.service.JwtTokenService;
import com.au.service.LeaveApplyService;
import com.au.service.LeaveTypeService;
import com.au.service.TriggerService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LeaveApplyController {

	Logger log = LoggerFactory.getLogger(LeaveApplyController.class);
	
	
	@Autowired
	private LeaveApplyService leaveapplyService;
	
	@Autowired
	private EmployeeLeavesService empLeaveService;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private LeaveTypeService s_service;

	@Autowired
	private LockDateRepository lockDateRepository;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private LeaveApplyRepository leaveapplyrepo;
	
	
	@PostMapping("/leaveApply")
	public ResponseEntity<Object> saveLeaveApplyCL(@RequestBody @Valid LeaveApplyDto la,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			la.setCreated_by(jwtDetails.getUserId());
			la.setCreated_username(jwtDetails.getUserName());
			List<LeaveApply> leaveApply = leaveapplyService.save_LeaveApply(la, jwtDetails);
            return ResponseHandler.generateResponse(true, HttpStatus.CREATED, leaveApply);
		} else {
            return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	
	
	@GetMapping("/fetchAllleaveApplyDetail")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
            return leaveapplyService.getAllDataFilteredByKeyword(pageable, keyword);
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
            return leaveapplyService.getAllSortedData(pageable1);
		}
		} else {
            return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/leaveApply")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<LeaveApply> leaveApply = leaveapplyService.listAll1();
		ResponseEntity<Object> leave_Apply_response= ResponseHandler.generateResponse(true, HttpStatus.OK, leaveApply);
		return leave_Apply_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/leaveApply/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	LeaveApply leaveApply = leaveapplyService.get(id);
	    	ResponseEntity<Object> leave_Apply_response= ResponseHandler.generateResponse(true, HttpStatus.OK, leaveApply);
			return leave_Apply_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/leaveApply/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid LeaveApply la, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	la.setModified_by(jwtDetails.getUserId());
	    	la.setModified_username(jwtDetails.getUserName());
	    	leaveapplyService.saveLeaveApply(la);
	    	ResponseEntity<Object> leave_Apply_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return leave_Apply_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@DeleteMapping("/leaveApply/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			leaveapplyService.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateLeaveApply/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			leaveapplyService.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PostMapping(value = "/leaveApplyUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute JobFileRequest jobfilerequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		leaveapplyService.uploadFile(jobfilerequest.getFile(), jobfilerequest.getLeave_apply_id());
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(path = "/leaveApplyFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = leaveapplyService.viewFiles(fileName);
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
	
	@PostMapping(value = "/leaveApplyUploadFile2")
	public ResponseEntity<Object> uploadFile2(@ModelAttribute JobFileRequest jobfilerequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		leaveapplyService.uploadFile2(jobfilerequest.getFile(), jobfilerequest.getLeave_apply_id());
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(path = "/leaveApplyFileviews2")
	public ResponseEntity<ByteArrayResource> viewFiles2(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = leaveapplyService.viewFiles2(fileName);
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

	
	@GetMapping("/emailidOfEmployeeByDept/{emp_id}")
	public ResponseEntity<Object> listEmailByDept(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EmployeeDetails> leaveApply = leaveapplyService.listEmailByDept1(emp_id);
		ResponseEntity<Object> leave_Apply_response= ResponseHandler.generateResponse(true, HttpStatus.OK, leaveApply);
		return leave_Apply_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}

	
	@GetMapping("/getUpdatedDaysCount/{emp_id}/{leave_id}")
	public ResponseEntity<Object> getUpdatedDaysCount(@PathVariable Integer emp_id, @PathVariable Integer leave_id) {
		Double updated_days_count = leaveapplyService.getUpdatedDaysCount(emp_id, leave_id);
		ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, updated_days_count);
		return empleaves_response;
	}
	
	@GetMapping("/checkAvailabilityOfCompOff/{emp_id}/{date}")
	public ResponseEntity<Object> checkAvailabilityOfCompOff(@PathVariable Integer emp_id,@PathVariable String date) throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
			Date date1=df.parse(date);
			Boolean compoff_availability = leaveapplyService.checkAvailabilityOfCompOff(emp_id,date1);
			if(compoff_availability == null) {
				ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, "Comp-Off Not Available for the selected date");
				return empleaves_response;
			} else {
				
				ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, "Comp-Off available for the selected date");
				return empleaves_response;
			}
		
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
//	@PostMapping("/applyOneHourPermission")
//	public ResponseEntity<Object> applyOneHourPermission(@RequestBody @Valid LeaveApply la,@RequestHeader("Authorization") String jwtToken)
//			throws Exception,JsonParseException, JsonMappingException, IOException  {
//		if(RateLimitController.bucket.tryConsume(1)) {
//
//			LeaveApply one_hour_permission = leaveapplyService.applyOneHourPermission(la,la.getEmp_id(),la.getFrom_date(),la.getLeave_id(),la.getShift());
//
//				ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, one_hour_permission);
//				return empleaves_response;
//		
//	} else {
//		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//		return rs;
//		}
//	}-+
	
	@GetMapping(value = "/getLeaveKettyDetails/{user_id}")
	public ResponseEntity<Object> getLeaveKettyDetails(@PathVariable Integer user_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				List<HashMap<String, Object>> emp = leaveapplyService.getLeaveKettyDetails(user_id);
				ResponseEntity<Object> emp_response = ResponseHandler.generateResponse(true, HttpStatus.OK, emp);
				return emp_response;

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
	
	@GetMapping("/getAllEmployeesForLeaveApply")
	public ResponseEntity<Object> getAllEmployeesForLeaveApply() {
		return leaveapplyService.getAllEmployeesForLeaveApply();
	}	
	
	@GetMapping("/getLeaveKettyDetailsByUserIdAndLeaveId/{user_id}/{leave_id}")
	public ResponseEntity<Object> getLeaveKettyDetailsByUserIdAndLeaveId(@PathVariable Integer user_id,
			@PathVariable Integer leave_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> leaveDetails = leaveapplyService.getLeaveKettyDetailsByUserIdAndLeaveId(user_id,
					leave_id);
			ResponseEntity<Object> empleaves_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					leaveDetails);
			return empleaves_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	

	@PostMapping("/cancelLeavesOfEmployee")
	public ResponseEntity<Object> cancelLeavesOfEmployee(@RequestBody LeaveCancelDTO leaveCancelDTO, @RequestHeader("Authorization") String jwtToken ) throws JsonParseException, JsonMappingException, IOException {
		return leaveapplyService.cancelLeavesOfEmployee(leaveCancelDTO, jwtToken);
	}	
	
	@GetMapping("/getDistinctYear")
	public ResponseEntity<Object> getDistinctYear() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<String> distinct_year = leaveapplyService.getDistinctYear();
			ResponseEntity<Object> leave_Apply_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					distinct_year);
			return leave_Apply_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getAllLeaveApplyDetails")
	public ResponseEntity<Object> getAllLeaveApplyDetails(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword,
			@RequestParam(value = "emp_id", required = false) Integer emp_id, @RequestParam(value = "role", required = false) String role,
			@RequestParam(value = "year", required = false) String year) {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> leaveapply_filtered = leaveapplyService
						.getAllDataFilteredByKeywordLeaveApplyDetails(pageable, keyword, emp_id, role, year);
				return leaveapply_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> leaveapply_sorted = leaveapplyService.getAllSortedLeaveApplyDetails(pageable1, emp_id, role, year);
				return leaveapply_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}	
	
	@GetMapping("/getAllLeaveApplyForApprovers")
	public ResponseEntity<Object> getAllLeaveApplyForApprovers(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword,
			@RequestParam(value = "approver_id") Integer approver_id) {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);

			LocalDate today = LocalDate.now();
			LocalDate minusMonths = today.minusMonths(1);
			Date checkMonthAndYearInLockDateTable = lockDateRepository.checkMonthAndYearInLockDateTable(minusMonths);

			LocalDate lockdate; 
			if(checkMonthAndYearInLockDateTable == null) {
				lockdate = minusMonths.withDayOfMonth(minusMonths.lengthOfMonth());
			} else {
				lockdate = checkMonthAndYearInLockDateTable.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); 
			}

			if (today.isAfter(lockdate)) {
				if (keyword != null) {
					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> leaveapply_filtered = leaveapplyService
							.getAllDataFilteredByKeywordForApprovers1(pageable, keyword, today.withDayOfMonth(1), approver_id);
					return leaveapply_filtered;
				} else {
					Pageable pageable1 = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> leaveapply_sorted = leaveapplyService.getAllSortedDataForApprovers2(pageable1, today.withDayOfMonth(1), approver_id);
					return leaveapply_sorted;
				}
			} else {
				if (keyword != null) {
					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> leaveapply_filtered = leaveapplyService
							.getAllDataFilteredByKeywordForApprovers3(pageable, keyword, minusMonths.withDayOfMonth(1), approver_id);
					return leaveapply_filtered;
				} else {
					Pageable pageable1 = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> leaveapply_sorted = leaveapplyService.getAllSortedDataForApprovers4(pageable1,
							 minusMonths.withDayOfMonth(1), approver_id);
					return leaveapply_sorted;
				}
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}	
	
	@PostMapping("/emailToEmployeeForApprovalOfLeaveRequest/{leave_apply_id}")
	public Object emailToEmployeeForApprovalOfLeaveRequest(@PathVariable Integer leave_apply_id) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				leaveapplyService.emailToEmployeeForApprovalOfLeaveRequest(leave_apply_id);
				ResponseEntity<Object> leaveapply = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return leaveapply;
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

	@PostMapping("/emailToEmployeeForLeaveCancellation/{leave_apply_id}")
	public Object emailToEmployeeForLeaveCancellation(@PathVariable Integer leave_apply_id) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				leaveapplyService.emailToEmployeeForLeaveCancellation(leave_apply_id);
				ResponseEntity<Object> leaveapply = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return leaveapply;
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
	
	@GetMapping("/getLeaveTypeForAttendence")
	public ResponseEntity<Object> getLeaveTypeForAttendence() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<LeaveType> list_leave_type = s_service.getLeaveTypeForAttendence();
				ResponseEntity<Object> list_leave_type_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_leave_type);
				return list_leave_type_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/leaveTypesAvailableForEmployeesForHrScreen/{emp_id}")
	public ResponseEntity<Object> leaveTypesAvailableForEmployeesForHrScreen(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<LeaveType> empleave = empLeaveService.leaveTypesAvailableForEmployeesForHrScreen(emp_id);
		ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, empleave);
		return empleaves_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@PostMapping("/emailToApproverForApprovingLeaveRequest/{emp_id}")
	public Object emailToApproverForLeaveRequest(@PathVariable Integer emp_id) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				leaveapplyService.emailToApproverForLeaveRequest(emp_id);
				ResponseEntity<Object> leaveapply = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return leaveapply;
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
	
	@PostMapping("/emailToAlternateStaffSelectedByLeaveApplier/{new_emp_id}/{leave_apply_id}")
	public Object emailToAlternateStaffSelectedByLeaveApplier(@PathVariable Integer new_emp_id,
			@PathVariable Integer leave_apply_id) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				leaveapplyService.emailToAlternateStaffSelectedByLeaveApplier(new_emp_id, leave_apply_id);
				ResponseEntity<Object> leaveapply = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return leaveapply;
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
	
	
	
	@GetMapping("/getAllLeaveApplyForEndUser")
	public ResponseEntity<Object> getAllLeaveApplyForEndUser(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword,
			@RequestParam(value = "emp_id") Integer emp_id, @RequestParam(value = "year") String year) {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> leaveapply_filtered = leaveapplyService
						.getAllDataFilteredByKeywordForEndUser(pageable, keyword, emp_id,year);
				return leaveapply_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> leaveapply_sorted = leaveapplyService.getAllSortedDataForEndUser(pageable1,emp_id, year);
				return leaveapply_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}	
	
	@PostMapping("/leaveApplyTrigger")
	public ResponseEntity<Object> leaveApplyTrigger(@RequestParam("date") String date){
			{
		
		     triggerService.leaveApplyTrigger(date);
			return ResponseHandler.generateResponse(true, HttpStatus.OK,
					"Leave Apply trigger started");
		
		}
	}

	@GetMapping("/getDataByLeaveApproversId")
	public ResponseEntity<Object> getDataByLeaveApproversId(@RequestParam(value = "user_id",  required = false) Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> empleave = leaveapplyService.getDataByLeaveApproversId(user_id);
		ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, empleave);
		return empleaves_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@PutMapping("/updateLeaveApplyApprover")
	public ResponseEntity<Object> updateLeaveApplyApprover(@RequestBody @Valid LeaveApplyUpdateDto la, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {

	    	leaveapplyService.updateLeaveApplyApprover(la, jwtToken);
	    	ResponseEntity<Object> leave_Apply_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return leave_Apply_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}

//	@PostMapping("/applyOneHourPermissionDetails")
//	public ResponseEntity<Object> applyOneHourPermissionDetails(@RequestBody @Valid LeaveApply la,@RequestHeader("Authorization") String jwtToken)
//			throws Exception,JsonParseException, JsonMappingException, IOException  {
//		if(RateLimitController.bucket.tryConsume(1)) {
//
//			LeaveApply oneHourPermission = leaveapplyService.applyOneHourPermissionDetails(la,la.getEmp_id(),la.getFrom_date(),la.getLeave_id(),la.getShift());
//
//				ResponseEntity<Object> empleaves_response= ResponseHandler.generateResponse(true, HttpStatus.OK, oneHourPermission);
//				return empleaves_response;
//		
//	} else {
//		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//		return rs;
//		}
//	}	
	
	
	@GetMapping("/getAllApprovedOrCancelledLeaves")
	public ResponseEntity<Object> getAllApprovedOrCancelledLeaves(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword,
			@RequestParam(value = "approver_id") Integer approver_id) {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);

			List<Integer> emp_ids_1 = leaveapplyrepo.getLeaveAppliedEmpIdsUnderSpecificApproversId_1(approver_id);
			System.out.println("<<<<<<<<<<<<<<<<????????1111111111??????????>>>>>>>>>>>>>>>> " + emp_ids_1);

			List<Integer> emp_ids_2 = leaveapplyrepo.getApprovedByApprovaer_1(
					leaveapplyrepo.getLeaveAppliedEmpIdsUnderSpecificApproversId_2(approver_id.toString()));
			System.out.println("<<<<<<<<<<<<<<<<????????????22222222222??????>>>>>>>>>>>>>>>> " + emp_ids_2);

			List<Integer> all_emps = new ArrayList<Integer>();
			all_emps.addAll(emp_ids_1);
			all_emps.addAll(emp_ids_2);
			System.out.println("<   :::::::::::::::::::::::   > " + all_emps);

			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> leaveapply_filtered = leaveapplyService
						.getAllApprovedOrCancelledLeavesDataFilteredByKeyword(pageable, keyword, all_emps);
				return leaveapply_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> leaveapply_sorted = leaveapplyService
						.getAllApprovedOrCancelledLeavesgetAllSortedData(pageable1, all_emps);
				return leaveapply_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/leaveDetails")
	public ResponseEntity<Object> leaveDetails(@RequestParam Integer year, @RequestParam Integer schoolId,
											   @RequestParam(required = false) Integer deptId, @RequestParam(required = false) Integer leaveId){
 		return leaveapplyService.getLeaveDetails(year, schoolId, deptId, leaveId);
	}

	@GetMapping("/leaveDetailsOfLeaveType")
	public ResponseEntity<Object> leaveDetailsByLeaveId(@RequestParam String leaveShortName, @RequestParam Integer empId, @RequestParam Integer year){
		return leaveapplyService.getLeaveDetailsByLeaveId(leaveShortName, empId, year);
	}
}
