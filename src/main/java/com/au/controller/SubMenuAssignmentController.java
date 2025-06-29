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
import com.au.model.SubMenuAssignment;
import com.au.repository.SubMenuAssignmentRepository;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.SubMenuAssignmentService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SubMenuAssignmentController {

	Logger log = LoggerFactory.getLogger(SubMenuAssignmentController.class);

	@Autowired
	private SubMenuAssignmentService submenuassign_service;

	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private SubMenuAssignmentRepository subMenuAssign_repo;

	@PostMapping("/SubMenuAssignment")
	public ResponseEntity<Object> savesubMenuAssign(@RequestBody @Valid SubMenuAssignment sma,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		sma.setCreated_by(jwtDetails.getUserId());
		sma.setCreated_username(jwtDetails.getUserName());
		SubMenuAssignment submenuass = submenuassign_service.saveSubMenuAssignment(sma);
	//	sma.setCount_role(subMenuAssign_repo.findAll8());
	//	SubMenuAssignment bs2 = submenuassign_service.saveSubMenuAssignment(sma);
		ResponseEntity<Object> created_submenu_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, submenuass);
		return created_submenu_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/SubMenuAssignment")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<SubMenuAssignment> submenu_ass_list = submenuassign_service.listAll();
			ResponseEntity<Object> created_submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, submenu_ass_list);
			return created_submenu_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

	@GetMapping("/fetchAllSubMenuAssignmentDetails")
	public ResponseEntity<Object> getAllSubMenuAssignmentDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> abc =  submenuassign_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return abc;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> xyz = submenuassign_service.getAllSortedData(pageable1);
			return xyz;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/SubMenuAssignment/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			SubMenuAssignment st = submenuassign_service.get(id);
			ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, st);
			return submenu_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/SubMenuAssignment/{id}")
	public ResponseEntity<Object> update(@RequestBody SubMenuAssignment sma, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			sma.setModified_by(jwtDetails.getUserId());
			sma.setModified_username(jwtDetails.getUserName());
			submenuassign_service.updateSubMenuAssignment(sma);
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/SubMenuAssignment/{id}")
	public ResponseEntity<Object> deactivateSubMenu(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		submenuassign_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateSubMenuAssignment/{id}")
	public ResponseEntity<Object> activateSubMenu(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		submenuassign_service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
/*
	@GetMapping("/fetchSubMenuDetail/{role_id}")
	public List<SubMenuAssignment> fetchdetails1(@RequestBody @PathVariable Integer role_id) {
		return submenuassign_service.fetchdetails1(role_id);
	}
*/
	@GetMapping("/fetchSubMenuDetailsOnRoleId/{role_id}")
	public ResponseEntity<Object> fetchdetails(@RequestBody @PathVariable List<Integer> role_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> sub_menu_details=submenuassign_service.fetchdetails(role_id);
		ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sub_menu_details);
		return submenu_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/Details/{role_id}")
	public ResponseEntity<Object> fetchdetails2(@RequestBody @PathVariable Integer role_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> details = submenuassign_service.fetchdetails2(role_id);
			ResponseEntity<Object> submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, details);
			return submenu_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/getAllAssignedRoleBySubmenuId/{submenu_id}")
	public ResponseEntity<Object> getAllAssignedRoleBySubmenuId(@PathVariable Integer submenu_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			HashMap<String, Object>  submenu_related = submenuassign_service.getAllAssignedRoleBySubmenuId(submenu_id);
			ResponseEntity<Object> unchecked_submenu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, submenu_related);
			return unchecked_submenu_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
}
