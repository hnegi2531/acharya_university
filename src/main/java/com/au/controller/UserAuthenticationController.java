package com.au.controller;

import java.io.IOException;
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

import com.au.dto.EmployeeMedicalHistoryDto;
import com.au.dto.JwtDetails;
import com.au.dto.OtpCheckRequest;
import com.au.dto.ResetPasswordDto;
import com.au.dto.UserAuthenticationDto;
import com.au.dto.UserDetailsDto;
import com.au.dto.UserRoleRequest;
import com.au.dto.UserRoleRequestWithUserRole;
import com.au.model.UserAuthentication;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.UserAuthenticationService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.controller.RateLimitController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserAuthenticationController {

	@Autowired
	private UserAuthenticationService uservice;

	@Autowired
	private JwtTokenService jwt_service;

	Logger log = LoggerFactory.getLogger(UserAuthenticationController.class);

	@PostMapping("/UserAuthentication")
	public ResponseEntity<Object> saveUserAuthentication(@RequestBody @Valid UserRoleRequest u,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			UserAuthentication user = uservice.saveUserRole(u,jwtToken);
			ResponseEntity<Object> user_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, user);
			return user_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping("/UserAuthenticationWithUserRole")
	public ResponseEntity<Object> UserAuthenticationWithUserRole(@RequestBody @Valid UserRoleRequestWithUserRole u,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			UserAuthentication user = uservice.UserAuthenticationWithUserRole(u,jwtToken);
			ResponseEntity<Object> user_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, user);
			return user_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/UserAuthentication")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<UserAuthentication> user_list= uservice.listAll();
			ResponseEntity<Object> user_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, user_list);
			return user_list_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getGuestDetailsData")
	public ResponseEntity<Object> getGuestDetailsData() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<UserAuthentication> user_list= uservice.getGuestDetailsData();
			ResponseEntity<Object> user_list_response= ResponseHandler.generateResponse(true, HttpStatus.OK, user_list);
			return user_list_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllUserAuthenticationDetails")
	public ResponseEntity<Object> getAllUserDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> roles_filtered =  uservice.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
				return roles_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> roles_sorted = uservice.getAllSortedData(pageable1);
				return roles_sorted;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/UserAuthentication/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				UserAuthentication product = uservice.get(id);
				ResponseEntity<Object> user_response= ResponseHandler.generateResponse(true, HttpStatus.OK,product);
				return user_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/UserAuthentication/{id}")
	public ResponseEntity<Object> update(@RequestBody UserAuthentication u, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				u.setModified_by(jwtDetails.getUserId());
				u.setModified_username(jwtDetails.getUserName());
				uservice.saveUserAuthentication(u);
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	
	@PutMapping("/updateUserPassword/{userId}")
	public ResponseEntity<Object> updateUserPassword(@RequestBody UserAuthenticationDto dto,
	        @PathVariable Integer userId, @RequestHeader("Authorization") String jwtToken)
	        throws JsonParseException, JsonMappingException, IOException {

	    if(RateLimitController.bucket.tryConsume(1)) {
	        try {
	            uservice.updateUserPassword(dto, jwtToken, userId);  // Pass id to the service
	            ResponseEntity<Object> emp_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
	            return emp_response;
	        } catch (NoSuchElementException e) {
	            ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
	            return response;
	        }
	    } else {
	        ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	        return rs;
	    }
	}
	
	

	@DeleteMapping("/UserAuthentication/{id}")
	public ResponseEntity<Object> deactivateUser(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			uservice.delete(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@DeleteMapping("/activateUserAuthentication/{id}")
	public ResponseEntity<Object> activateUser(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			uservice.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getSubMenuDetails/{id}")
	public ResponseEntity<Object> getSubMenuDetail(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> subMenu_details = uservice.getSubMenuDetails(id);
			ResponseEntity<Object> subMenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK,subMenu_details);
			return subMenu_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping("/forgotPassword")
	public ResponseEntity<Object> processForgotPassword(@RequestParam(value ="url_domain",required = false) String url_domain,@RequestParam("username")  String username) 
			throws JsonProcessingException {

		if(RateLimitController.bucket.tryConsume(1)) {
			return uservice.processForgotPassword(url_domain, username);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@PutMapping("/resetPassword")
	public ResponseEntity<Object> c(@RequestBody @Valid ResetPasswordDto resetPasswordDto){
		if(RateLimitController.bucket.tryConsume(1)) {
			return uservice.resetPassword(resetPasswordDto.getToken(),resetPasswordDto.getPassword());
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
		
	}
	
	@GetMapping("/userDetailswithDepartment")
	public ResponseEntity<Object> userDetailswithDepartment() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> user_details_list = uservice.userDetailswithDepartment();
			ResponseEntity<Object> user_details_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, user_details_list);
			return user_details_list_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/changePassword")
	public ResponseEntity<Object> changePassword(@RequestParam("username") String username,@RequestParam("password")  String new_password){
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String,String> hm1= uservice.changePassword(username,new_password);
			ResponseEntity<Object> reset_password_response= ResponseHandler.generateResponse(true, HttpStatus.OK,hm1);
			return reset_password_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}
	
	@GetMapping("/staffUserDetails")
	public ResponseEntity<Object> staffUserDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String,Object>> staffUserDetails= uservice.staffUserDetails();
			ResponseEntity<Object> staffUserDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, staffUserDetails);
			return staffUserDetailsResponse;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}

   }
	

	
	@GetMapping("/getUserDetailsById/{id}")
	public ResponseEntity<Object> getUserDetailsById(@PathVariable Integer id){
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
		UserDetailsDto dto=	uservice.getUserDetailsById(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, dto);
			return response;
			}
			catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@PutMapping("/userPasswordUpdateByDefaultPassword/{userId}")
	public ResponseEntity<Object> userPasswordUpdateByDefaultPassword(@PathVariable Integer userId,
			@RequestHeader("Authorization") String jwtToken){
		if (RateLimitController.bucket.tryConsume(1)) {

			return uservice.userPasswordUpdateByDefaultPassword(userId, jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			
		}
	}	

	@GetMapping("/getDeptIdBasedOnUserId/{user_id}")
	public ResponseEntity<Object> getDeptIdBasedOnUserId(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			List<Map<String, Object>> product = uservice.getDeptIdBasedOnUserId(user_id);
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
	
	@GetMapping("/getSchoolIdBasedOnUserId/{user_id}")
	public ResponseEntity<Object> getSchoolIdBasedOnUserId(@PathVariable Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			Map<String, Object> product = uservice.getSchoolIdBasedOnUserId(user_id);
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
	
	@DeleteMapping("/activateUserByEmployeeId/{empId}")
	public ResponseEntity<Object> activateUserByEmployeeId(@PathVariable Integer empId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			uservice.activateUserByEmployeeId(empId);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PostMapping("/passwordChecker")
	public ResponseEntity<Object> passwordChecker(@RequestBody @Valid UserRoleRequest user){
		return uservice.passwordChecker(user);
	}
	
	@PostMapping("/resetPasswordOtpCheckr")
	public ResponseEntity<Object> resetPasswordOtpCheckr(@RequestBody @Valid OtpCheckRequest otp){
		return uservice.resetPasswordOtpCheckr(otp);
	}
	
	@PutMapping("/updatebookChapterApproverDesignationForUser/{userId}/{bookChapterApproverDesignation}")
	public ResponseEntity<Object> updatebookChapterApproverDesignationForUser(@PathVariable Integer userId,@PathVariable String bookChapterApproverDesignation,
			@RequestHeader("Authorization") String jwtToken){
		if (RateLimitController.bucket.tryConsume(1)) {

			return uservice.updatebookChapterApproverDesignationForUser(userId,bookChapterApproverDesignation, jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			
		}
	}

	@PostMapping("/forgotPasswordWhatsapp")
	public ResponseEntity<Object> processForgotPassword(@RequestParam("username")  String username)
			throws JsonProcessingException {

		if(RateLimitController.bucket.tryConsume(1)) {
			return uservice.processForgotPassword(username);
		}
		return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
	}
}
