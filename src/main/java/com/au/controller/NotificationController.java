package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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
import com.au.model.LeaveKitty;
import com.au.model.Notifications;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.NotificationService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey4}")
@CrossOrigin
public class NotificationController {
	
	Logger log = LoggerFactory.getLogger(NotificationController.class);
	
	@Autowired
	private NotificationService n_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/notification")
	private ResponseEntity<Object> saveNotification(@RequestBody Notifications notf, @RequestHeader ("Authorization") String jwtToken)
	throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		notf.setCreated_by(jwtDetails.getUserId());
		notf.setCreated_username(jwtDetails.getUserName());
		Notifications nfy = n_ser.saveNotification(notf);
		ResponseEntity<Object> nfy_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, nfy);
		return nfy_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
		
	}
	
	@GetMapping("/getNotification")
	private ResponseEntity<Object> listAll1() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Notifications> nfy = n_ser.listAll1();
			ResponseEntity<Object> nfy_response= ResponseHandler.generateResponse(true, HttpStatus.OK, nfy);
			return nfy_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllNotifications")
	private List<Notifications> listAll() {
		return n_ser.listAll();
	}
	
	@GetMapping("/fetchAllNotificationsForIndex")
	public ResponseEntity<Object> fetchAllAdministrationEmailIdsDetail(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  n_ser.getAllDataFilteredByKeyword(pageable, keyword);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = n_ser.getAllSortedData(pageable1);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllNotificationsForIndexBasedOnUser")
	public ResponseEntity<Object> fetchAllNotificationsForIndexBasedOnUser(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,@RequestParam(value="userId",required = false) Integer userId,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  n_ser.fetchAllNotificationsForIndexBasedOnUserByKeyword(pageable, keyword,userId);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = n_ser.fetchAllNotificationsForIndexBasedOnUserData(pageable1,userId);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getNotification/{notification_id}")
	private ResponseEntity<Object> get(@PathVariable Integer notification_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			Notifications nf = n_ser.get(notification_id);
			ResponseEntity<Object> nf_response= ResponseHandler.generateResponse(true, HttpStatus.OK, nf);
			return nf_response;
		} catch(NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/Notifications/{notification_id}")
	public ResponseEntity<Object> updateNotification(@RequestBody Notifications notf, @PathVariable Integer notification_id,
			@RequestHeader ("Authorization") String jwtToken) throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			notf.setModified_by(jwtDetails.getUserId());
			notf.setModified_username(jwtDetails.getUserName());
			n_ser.saveUpdateNotification(notf);
			ResponseEntity<Object> nf_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return nf_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
		
	}
	
	@DeleteMapping("/notification/{notification_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer notification_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		n_ser.delete(notification_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/activateNotification/{notification_id}")
		public ResponseEntity<Object> delete1(@PathVariable Integer notification_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			n_ser.delete1(notification_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
	
	@PostMapping(value = "/notificationUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute JobFileRequest jobfilerequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// JwtDetails jwtdetails= (JwtDetails) auth.getDetails();
		// System.out.println(jwtdetails.getUserId());
		System.out.println("Hello-------" + auth.getDetails());
		log.debug("Message For Attachment");
		n_ser.uploadFile(jobfilerequest.getFile(), jobfilerequest.getNotification_id());
		 ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	
	@GetMapping(path = "/notificationFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = n_ser.viewFiles(fileName);
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
	
	@GetMapping("/getLatestFiveNotifications")
	private ResponseEntity<Object> getLatestFiveNotifications() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Notifications> nfy = n_ser.getLatestFiveNotifications();
		ResponseEntity<Object> nfy_response= ResponseHandler.generateResponse(true, HttpStatus.OK, nfy);
		return nfy_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getDepartmentWithSchools/{school_ids}")
	private ResponseEntity<Object> getDepartmentWithSchools(@PathVariable List<Integer> school_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> nfy = n_ser.getDepartmentWithSchools(school_ids);
		ResponseEntity<Object> nfy_response= ResponseHandler.generateResponse(true, HttpStatus.OK, nfy);
		return nfy_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	
	@GetMapping("/getCountOfNotification/{dept_ids}")
	private ResponseEntity<Object> getCountOfNotification(@PathVariable Integer dept_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Map<String, Object> nfy = n_ser.getCountOfNotification(dept_ids);
		ResponseEntity<Object> nfy_response= ResponseHandler.generateResponse(true, HttpStatus.OK, nfy);
		return nfy_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getNotificationDataBasedOnDept/{dept_ids}")
	private ResponseEntity<Object> getNotificationDataBasedOnDept(@PathVariable Integer dept_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> nfy = n_ser.getNotificationDataBasedOnDept(dept_ids);
		ResponseEntity<Object> nfy_response= ResponseHandler.generateResponse(true, HttpStatus.OK, nfy);
		return nfy_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getNotificationDataOfToday/{dept_ids}")
	private ResponseEntity<Object> getNotificationDataOfToday(@PathVariable Integer dept_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> nfy = n_ser.getNotificationDataOfToday(dept_ids);
		ResponseEntity<Object> nfy_response= ResponseHandler.generateResponse(true, HttpStatus.OK, nfy);
		return nfy_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
