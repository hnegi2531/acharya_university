package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.au.dto.UserRoleDto;
import com.au.model.UserRole;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.UserRoleService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserRoleController {

	Logger log = LoggerFactory.getLogger(UserRoleController.class);

	@Autowired
	private UserRoleService urs_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/UserRole")
	public ResponseEntity<Object> saveUserRoles(@RequestBody @Valid UserRoleDto ur,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		ur.setCreated_by(jwtDetails.getUserId());
		ur.setCreated_username(jwtDetails.getUserName());
		List<UserRole> user_role= urs_service.saveUserRoleDetails(ur, jwtToken);
		ResponseEntity<Object> user_role_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, user_role);
		return user_role_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/UserRole")
	public ResponseEntity<Object> listAllActiveUserRole() {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<UserRole> user_role = urs_service.listAll();
		ResponseEntity<Object> user_role_response= ResponseHandler.generateResponse(true, HttpStatus.OK, user_role);
		return user_role_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	

	@GetMapping("/fetchAllUserRoleDetails")
	public ResponseEntity<Object> getAllUserRoleData(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> user_role_filtered_reponse =  urs_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return user_role_filtered_reponse;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> user_role_sorted_reponse = urs_service.getAllSortedData(pageable1);
			return user_role_sorted_reponse;
		}
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/UserRole/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			UserRole product = urs_service.get(id);
			ResponseEntity<Object> user_roles_response= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return user_roles_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
}

	@PutMapping("/UserRole/{id}")
	public ResponseEntity<Object> update(@RequestBody UserRole ur, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			ur.setModified_by(jwtDetails.getUserId());
			ur.setModified_username(jwtDetails.getUserName());
			urs_service.saveUserRole(ur);
			ResponseEntity<Object> user_roles_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return user_roles_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@DeleteMapping("/UserRole/{id}")
	public ResponseEntity<Object> deactivateUserRole(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		urs_service.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateUserRole/{id}")
	public ResponseEntity<Object> activateUserRole(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    urs_service.activate(id);
	    ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/findRoles/{id}")
	public ResponseEntity<Object> getDetails(@RequestBody @PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> roles_find= urs_service.getRoleDetails(id);
		ResponseEntity<Object> active_roles_response= ResponseHandler.generateResponse(true, HttpStatus.OK, roles_find);
		return active_roles_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/findActiveRoles/{id}/{role_id}")
	public ResponseEntity<Object> getDetail(@RequestBody @PathVariable Integer id, @PathVariable Integer role_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> active_roles= urs_service.getActiveRoleDetails(id,role_id);
		ResponseEntity<Object> active_roles_response= ResponseHandler.generateResponse(true, HttpStatus.OK, active_roles);
		return active_roles_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/userDetailsByRoleId/{role_id}")
	public ResponseEntity<Object> userDetailsByRoleId(@PathVariable Integer role_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> active_roles= urs_service.userDetailsByRoleId(role_id);
		ResponseEntity<Object> active_roles_response= ResponseHandler.generateResponse(true, HttpStatus.OK, active_roles);
		return active_roles_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getUserDetailsBasedOnRole/{role_id}")
	public ResponseEntity<Object> getUserDetailsBasedOnRole(@PathVariable Integer role_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> active_roles= urs_service.getUserDetailsBasedOnRole(role_id);
		ResponseEntity<Object> active_roles_response= ResponseHandler.generateResponse(true, HttpStatus.OK, active_roles);
		return active_roles_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
}
