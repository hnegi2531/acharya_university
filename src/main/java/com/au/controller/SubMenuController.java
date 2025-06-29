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
import com.au.model.SubMenu;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SubMenuService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SubMenuController {

	Logger log = LoggerFactory.getLogger(SubMenuController.class);

	@Autowired
	private SubMenuService submenu_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/SubMenu")
	public ResponseEntity<Object> savesubMenu(@RequestBody @Valid SubMenu sm,
			@RequestHeader("Authorization") String jwtToken) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		sm.setCreated_by(jwtDetails.getUserId());
		sm.setCreated_username(jwtDetails.getUserName());
		SubMenu submenu = submenu_service.saveSubMenu(sm);
		ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, submenu);
		return submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/SubMenu")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<SubMenu> sub_menu_list= submenu_service.listAll();
		ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sub_menu_list);
		return submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchSubMenuDetails")
	public ResponseEntity<Object> listAll10() {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> sub_menu_list= submenu_service.listAll10();
			ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sub_menu_list);
			return submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllSubMenuDetails")
	public ResponseEntity<Object> getAllSubMenuDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> abc =  submenu_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return abc;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> xyz = submenu_service.getAllSortedData(pageable1);
			return xyz;
		}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllRoleDetails/{submenu_ids}")
	public ResponseEntity<Object> listAll11(@PathVariable Integer submenu_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> sub_menu_list = submenu_service.listAll11(submenu_ids);
			ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sub_menu_list);
			return submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/SubMenu/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			SubMenu st = submenu_service.get(id);
			ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, st);
			return submenu_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/SubMenu/{id}")
	public ResponseEntity<Object> update(@RequestBody SubMenu sm, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			sm.setModified_by(jwtDetails.getUserId());
			sm.setModified_username(jwtDetails.getUserName());
			submenu_service.saveSubMenus(sm);
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

	@DeleteMapping("/SubMenu/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		 submenu_service.delete(id);
		 ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateSubMenu/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		submenu_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}

	@GetMapping("/allDetails/{submenu_id}")
	public ResponseEntity<Object> fetchdetails(@RequestBody @PathVariable List<Integer> submenu_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> submenu_list = submenu_service.fetchdetails(submenu_id);
			ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, submenu_list);
			return submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/alluncheckedSubMenuDetail/{submenu_id}")
	public ResponseEntity<Object> fetchUncheckedSubMenudetails(@RequestBody @PathVariable List<Integer> submenu_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> unchecked_submenu=submenu_service.fetchUncheckedSubMenudetails(submenu_id);
		ResponseEntity<Object> unchecked_submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, unchecked_submenu);
		return unchecked_submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/subMenuDetails/{menu_id}")
	public ResponseEntity<Object> fetchdetails4(@RequestBody @PathVariable Integer menu_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Integer> submenu_list = submenu_service.fetchSubMenu(menu_id);
			ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, submenu_list);
			return submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/findSubMenuDetails/{role_id}")
	public ResponseEntity<Object> fetchSubMenu1(@RequestBody @PathVariable Integer role_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		String sub = submenu_service.fetchSubMenu1(role_id);
		ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sub);
		return submenu_response;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}

	@GetMapping("/subMenuDetails/{menu_id}/{role_id}")
	public ResponseEntity<Object> fetchdetails5(@RequestBody @PathVariable Integer menu_id,
			@PathVariable Integer role_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> submenu = submenu_service.getCommons(menu_id, role_id);
			ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, submenu);
			return submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getSubMenuRelatedUser/{submenu_id}")
	public ResponseEntity<Object> getSubMenuRelatedUser(@PathVariable Integer submenu_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		HashMap<String, Object> submenu_related = submenu_service.getSubMenuRelatedUsers(submenu_id);
		ResponseEntity<Object> unchecked_submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, submenu_related);
		return unchecked_submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PostMapping("/postUserDetails/{submenu_id}")
	public ResponseEntity<Object> postUserDetail(@RequestBody SubMenu sm, @PathVariable Integer submenu_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		Object ob= submenu_service.getUserDetails(sm.getUser_ids(), submenu_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponse(true, HttpStatus.OK, ob);
		return response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllAssignedUserBySubmenuId/{submenu_id}")
	public ResponseEntity<Object> getAllAssignedUserBySubmenuId(@PathVariable Integer submenu_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object>  submenu_related = submenu_service.getAllAssignedUserBySubmenuId(submenu_id);
			ResponseEntity<Object> unchecked_submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, submenu_related);
			return unchecked_submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAssignedSubMenuDetailsByUserId/{userId}")
	public ResponseEntity<Object> getAssignedSubMenuDetailsByUserId(@PathVariable Integer userId) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object> subMenu_details = submenu_service.getAssignedSubMenuDetailsByUserId(userId);
			ResponseEntity<Object> subMenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK,subMenu_details);
			return subMenu_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
}
