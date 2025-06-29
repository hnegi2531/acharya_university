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

import com.au.dto.JwtDetails;

import com.au.model.FamilyStructure;

import com.au.response.ResponseHandler;
import com.au.service.FamilyStructureService;
import com.au.service.JwtTokenService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class FamilyStructureController {

	Logger log = LoggerFactory.getLogger(FamilyStructureController.class);

	@Autowired
	private FamilyStructureService familyStructureService;

	@Autowired
	private JwtTokenService jwtTokenService;

	@PostMapping("/familystructure")
	public ResponseEntity<Object> saveRoles(@RequestBody @Valid List<FamilyStructure> family,
			@RequestHeader("Authorization") String jwtToken) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				family.stream().forEach(f -> {

					f.setCreated_by(jwtDetails.getUserId());
					f.setCreated_username(jwtDetails.getUserName());
				});
				List<FamilyStructure> familyStructures = familyStructureService.saveFamily(family);
				return ResponseHandler.generateResponse(true, HttpStatus.CREATED, familyStructures);
			} catch (JsonParseException | JsonMappingException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}

	@GetMapping("/fetchAllfamilystructureDetail")
	public ResponseEntity<Object> getAllDept(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				return familyStructureService.getAllDataFilteredByKeyword(pageable, keyword);
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				return familyStructureService.getAllSortedData(pageable1);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/familystructure")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<FamilyStructure> familyStructures = familyStructureService.listAll1();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, familyStructures);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@GetMapping("/familystructure/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				FamilyStructure familyStructures = familyStructureService.get(id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, familyStructures);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@PutMapping("/updateFamilystructure/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<FamilyStructure> family,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				family.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				familyStructureService.updateFamilystructure(family);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (JsonParseException | JsonMappingException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/familystructure/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			familyStructureService.delete(id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

	@DeleteMapping("/activatefamilystructure/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			familyStructureService.delete1(id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}

	@GetMapping("/getFamilyStructureDetailsData/{empId}")
	public ResponseEntity<Object> getFamilyStructureDetailsData(@PathVariable Integer empId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> familyData = familyStructureService.getFamilyStructureDetailsData(empId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, familyData);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
}
