package com.au.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.*;

import com.au.dto.JobFileRequest;
import com.au.dto.MaintenanceDto;
import com.au.model.ServiceTicketMaintenance;
import com.au.response.ResponseHandler;
import com.au.service.MaintenanceService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/Maintenance")
@CrossOrigin
public class MaintenanceController {

	Logger log = LoggerFactory.getLogger(LeaveApplyController.class);

	@Autowired
	private MaintenanceService service;

	@PatchMapping("/serviceRequest/{ticketId}")
	public ResponseEntity<Object> partialUpdateServiceTicket(@PathVariable Long ticketId,
			@RequestBody MaintenanceDto maintenanceDto, @RequestHeader("Authorization") String jwtToken) {
		if (RateLimitController.bucket.tryConsume(1)) {
			return service.partialUpdateServiceTicket(ticketId, maintenanceDto, jwtToken);
		}
		return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	}

	@PostMapping("/createMaintenance")
	public ResponseEntity<Object> createMaintenance(@RequestBody List<MaintenanceDto> dtoList,
	                                                @RequestHeader("Authorization") String jwtToken) throws Exception {

	    if (RateLimitController.bucket.tryConsume(1)) {
	        // Call the service to process all maintenance requests
	        List<ServiceTicketMaintenance> responses = service.createMaintenance(dtoList, jwtToken);
	        return ResponseHandler.generateResponse(true, HttpStatus.CREATED, responses);

	    } else {
	        ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
	                ResponseHandler.message1);
	        return rs;
	    }
	}

	@PostMapping
	public ResponseEntity<Object> attendMaintenance(@RequestBody MaintenanceDto dto,
			@RequestHeader("Authorization") String jwtToken) throws Exception {

		if (RateLimitController.bucket.tryConsume(1)) {

			ServiceTicketMaintenance response = service.attendMaintenance(dto, jwtToken);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, response);

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}


	@PutMapping("/{id}")
	public ResponseEntity<Object> updateMaintenance(@RequestBody @Valid MaintenanceDto dto, @PathVariable Long id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {

		if (RateLimitController.bucket.tryConsume(1)) {
			ServiceTicketMaintenance response = service.updateMaintenance(dto, id, jwtToken);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, response);

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}

	}

	@GetMapping("/getAllServiceDetailsByUserIdAndDeptId/{user_id}/{dept_id}")
	public ResponseEntity<Object> getAllServiceByUserId(@PathVariable Integer user_id, @PathVariable Integer dept_id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> allServiceByDeptTag = service.getAllServiceByUserIdAndDeptId(user_id, dept_id);
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.OK,
					allServiceByDeptTag);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllServiceTypeDetails")
	public ResponseEntity<Object> getAllDept(@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "dept_id", required = false) Integer dept_id,
			@RequestParam(value = "page") Integer page, @RequestParam(value = "page_size") Integer page_size,
			@RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) throws ParseException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				if (fromDate != null && toDate != null) {
					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> dept_filtered = service.getAllDataFilteredByKeywordWithDate(pageable,
							keyword, fromDate, toDate, dept_id);// ,column,value);
					return dept_filtered;
				} else {

					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> dept_filtered = service.getAllDataFilteredByKeyword(pageable, keyword,
							dept_id);// ,column,value);
					return dept_filtered;

				}

			} else {
				if (fromDate != null && toDate != null) {
					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> dept_filtered = service.getAllDataFilteredWithDate(pageable, fromDate,
							toDate, dept_id);// ,column,value);
					return dept_filtered;
				} else {

					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> dept_filtered = service.getAllSortedData(pageable, dept_id);// ,column,value);
					return dept_filtered;

				}
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getServiceTypeDetailsById/{id}")
	public ResponseEntity<Object> get(@PathVariable Long id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				ServiceTicketMaintenance product = service.get(id);
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

	@GetMapping("/fetchAllServiceTypeHistory")
	public ResponseEntity<Object> fetchAllServiceTypeHistory(
			@RequestParam(value = "dept_id", required = false) Integer dept_id,
			@RequestParam(value = "page") Integer page, @RequestParam(value = "page_size") Integer page_size,
			@RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) throws ParseException {
		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				if (dept_id != null) {
					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> job_profile_sorted = service.fetchAllServiceTypeHistory(pageable, keyword,
							dept_id);// ,column,value);
					return job_profile_sorted;
				} else {

					Pageable pageable = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> job_profile_sorted = service.fetchAllServiceTypeHistory(pageable, keyword);// ,column,value);
					return job_profile_sorted;

				}

			} else {
				if (dept_id != null) {
					Pageable pageable1 = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> job_profile_pageable = service.fetchAllServiceTypeHistory1(pageable1,
							dept_id);
					return job_profile_pageable;
				} else {

					Pageable pageable1 = PageRequest.of(page, page_size, sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> job_profile_pageable = service.fetchAllServiceTypeHistory12(pageable1);
					return job_profile_pageable;

				}

			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllPendingStatusDetails")
	public ResponseEntity<Object> fetchAllPendingStatusDetails(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "dept_id", required = false) Integer dept_id,
			@RequestParam(value = "complaintStatus") String complaintStatus, @RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) throws ParseException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				if (dept_id != null) {
					if (fromDate != null && toDate != null) {
						Pageable pageable = PageRequest.of(page, page_size, sorted);
						System.out.println("page, page_size, sorted, keyword1111111");
						ResponseEntity<Object> dept_filtered = service.getAllDataFiltered(pageable, keyword, fromDate,
								toDate, dept_id, complaintStatus);// ,column,value);
						return dept_filtered;
					} else {

						Pageable pageable = PageRequest.of(page, page_size, sorted);
						System.out.println("page, page_size, sorted, keyword2222222222");
						ResponseEntity<Object> dept_filtered = service.getAllData(pageable, keyword, dept_id,
								complaintStatus);// ,column,value);
						return dept_filtered;

					}
				} else {
					if (fromDate != null && toDate != null) {
						Pageable pageable = PageRequest.of(page, page_size, sorted);
						System.out.println("page, page_size, sorted, keyword1111111");
						ResponseEntity<Object> dept_filtered = service.getAllDataFilteredWODept(pageable, keyword,
								fromDate, toDate, complaintStatus);// ,column,value);
						return dept_filtered;
					} else {

						Pageable pageable = PageRequest.of(page, page_size, sorted);
						System.out.println("page, page_size, sorted, keyword2222222222");
						ResponseEntity<Object> dept_filtered = service.getAllDataWODept(pageable, keyword,
								complaintStatus);// ,column,value);
						return dept_filtered;

					}
				}
			} else {
				if (dept_id != null) {
					if (fromDate != null && toDate != null) {
						Pageable pageable = PageRequest.of(page, page_size, sorted);
						System.out.println("page, page_size, sorted, keyword3333333");
						ResponseEntity<Object> dept_filtered = service.getAllDataFiltered12(pageable, fromDate, toDate,
								dept_id, complaintStatus);// ,column,value);
						return dept_filtered;
					} else {

						Pageable pageable = PageRequest.of(page, page_size, sorted);
						System.out.println("page, page_size, sorted, keyword4444444");
						ResponseEntity<Object> dept_filtered = service.getAllData12(pageable, dept_id, complaintStatus);// ,column,value);
						return dept_filtered;

					}
				} else {
					if (fromDate != null && toDate != null) {
						Pageable pageable = PageRequest.of(page, page_size, sorted);
						System.out.println("page, page_size, sorted, keyword3333333");
						ResponseEntity<Object> dept_filtered = service.getAllDataFiltered12WODept(pageable, fromDate,
								toDate, complaintStatus);// ,column,value);
						return dept_filtered;
					} else {

						Pageable pageable = PageRequest.of(page, page_size, sorted);
						System.out.println("page, page_size, sorted, keyword4444444");
						ResponseEntity<Object> dept_filtered = service.getAllData12WODept(pageable, complaintStatus);// ,column,value);
						return dept_filtered;
					}
				}
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getServicStatusDetailsForGraph")
	public ResponseEntity<Object> getServicStatusDetailsForGraph(
			@RequestParam(value = "dept_id", required = false) Integer dept_id,
			@RequestParam(value = "complaintStatus", required = false) String complaintStatus) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			if (complaintStatus != null) {
				HashMap<String, Object> statusDetails1 = service.getServicStatusDetailswithStatusForGraph(dept_id,
						complaintStatus);
				ResponseEntity<Object> statusDetails_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						statusDetails1);
				return statusDetails_response;
			} else {
				HashMap<String, Object> statusDetails = service.getServicStatusDetailsForGraph(dept_id);
				ResponseEntity<Object> statusDetails_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						statusDetails);
				return statusDetails_response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/serviceRequestEmailToStaffByDeptId/{id}")
	public Object serviceRequestEmailToStaffByDeptId(@PathVariable Long id) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				service.serviceRequestEmailToStaffByDeptId(id);
				ResponseEntity<Object> service = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return service;
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

	@PostMapping(value = "/maintenanceUploadFile")
	public ResponseEntity<Object> maintenanceUploadFile(@ModelAttribute MaintenanceDto maintenance) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		service.uploadFile(maintenance.getFile(), maintenance.getId());
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	@GetMapping(path = "/maintenanceFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = service.viewFiles(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);

			String contentType = Files.probeContentType(Paths.get(fileName));
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			return ResponseEntity.ok().contentLength(data.length).header("Content-type", contentType)
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	
	@GetMapping("/fetchServiceThroughEvent")
	public ResponseEntity<Object> fetchServiceThroughEvent(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="userId") Integer userId,
			@RequestParam(value="event_id") Integer event_id, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> event_creation_sorted = service.fetchServiceThroughEvent(pageable,userId,event_id, keyword);//,column,value);
					return event_creation_sorted;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> event_creation_pageable = service.fetchServiceThroughEventWOKeyword(pageable1,event_id,userId);
					return event_creation_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

//	@GetMapping("/fetchAllPendingStatusDetails")
//	public ResponseEntity<Object> fetchAllPendingStatusDetails(@RequestParam(value = "fromDate",required = false) String fromDate,
//			@RequestParam(value = "toDate",required = false) String toDate,
//			@RequestParam(value = "dept_id",required = false) Integer dept_id,
//			@RequestParam(value = "complaintStatus") String complaintStatus,
//			@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
//			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) throws ParseException {	
//		
//		if(RateLimitController.bucket.tryConsume(1)) {
//		Sort sorted = Sort.by(Direction.DESC, sort );
//		if(keyword != null) {	
//			if(dept_id != null) {
//			Pageable pageable = PageRequest.of(page, page_size,sorted);
//				System.out.println("page, page_size, sorted, keyword");
//				ResponseEntity<Object> dept_filtered = service.getAllDataFilteredByKeywordWithStatusDept(pageable, keyword,fromDate,toDate,dept_id,complaintStatus);
//				return dept_filtered;
//			}else {
//				
//				Pageable pageable = PageRequest.of(page, page_size,sorted);
//				System.out.println("page, page_size, sorted, keyword");
//				ResponseEntity<Object> dept_filtered = service.getAllDataFilteredByKeywordWithStatus(pageable,keyword,complaintStatus,fromDate,toDate);//,column,value);
//				return dept_filtered;
//				
//				
//				}
//			}else {
//				if(dept_id != null) {
//				Pageable pageable = PageRequest.of(page, page_size,sorted);
//					System.out.println("page, page_size, sorted, keyword");
//					ResponseEntity<Object> dept_filtered = service.getAllDataFilteredWithstatus(pageable,fromDate,toDate,dept_id,complaintStatus);
//					return dept_filtered;
//				}else {
//					Pageable pageable = PageRequest.of(page, page_size,sorted);
//					System.out.println("page, page_size, sorted, keyword");
//					ResponseEntity<Object> dept_filtered = service.getAllDataFilteredWithstatusDept(pageable,fromDate,toDate,complaintStatus);//,column,value);
//					return dept_filtered;
//					}
//			}	
//				
//			}else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}

}
