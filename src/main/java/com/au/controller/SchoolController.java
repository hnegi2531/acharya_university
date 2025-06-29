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
import com.au.model.Schools;
import com.au.response.ResponseHandler;
import com.au.service.EncryptionDecriptionService;
import com.au.service.JwtTokenService;
import com.au.service.RSAEncryptionService;
import com.au.service.School_Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey4}")
@CrossOrigin
public class SchoolController {

	Logger log = LoggerFactory.getLogger(SchoolController.class);

	@Autowired
	private School_Service sc_service;

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private EncryptionDecriptionService e_service;

	@Autowired
	private RSAEncryptionService rsa_service;

	@PostMapping("/school")
	public ResponseEntity<Object> saveSchool(@RequestBody @Valid Schools school,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			school.setCreated_by(jwtDetails.getUserId());
			school.setCreated_username(jwtDetails.getUserName());
			Schools schools = sc_service.save_School(school);
			ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					schools);
			return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}

	}

	@GetMapping("/school")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Schools> schools = sc_service.listAll();
			ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					schools);
			return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getSchoolDetails")
	public ResponseEntity<Object> getSchoolDetails() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> schools = sc_service.getSchoolDetails();
			ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					schools);
			return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/testEncryptionSchool")
	public ResponseEntity<Object> listAll3() {
		if (RateLimitController.bucket.tryConsume(1)) {
			System.out.println(e_service.encryption(sc_service.listAll().toString()));
			System.out.println(e_service.decryption(e_service.encryption(sc_service.listAll().toString())));
			String schools = e_service.encryption(sc_service.listAll().toString());
			ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					schools);
			return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/testEncryptionSchool15")
	public ResponseEntity<Object> listAll15() {
		if (RateLimitController.bucket.tryConsume(1)) {
			System.out.println(e_service.encryption("vikash"));
			System.out.println(e_service.decryption(e_service.encryption("vikash")));
			String schools = e_service.encryption("vikash");
			ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					schools);
			return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/school16")
	public ResponseEntity<Object> listAll16() throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			System.out.println(rsa_service.doEncryption("Hello my name is vikash"));
			System.out.println(rsa_service.doDecryption(rsa_service.doEncryption("Hello my name is vikash")));
			String schools = rsa_service.doEncryption("Hello my name is vikash");
			ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					schools);
			return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/testEncryptionSchool1")
	public ResponseEntity<Object> listAll13() throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			System.out.println(rsa_service.doEncryption(sc_service.listAll().toString()));
			System.out.println(rsa_service.doDecryption(rsa_service.doEncryption(sc_service.listAll().toString())));
			String schools = rsa_service.doEncryption(sc_service.listAll().toString());
			ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					schools);
			return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllSchoolDetail")
	public ResponseEntity<Object> getAllSchoolsData(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword) {

		if (RateLimitController.bucket.tryConsume(1)) {

			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> abc = sc_service.getAllDataFilteredByKeyword(pageable, keyword);// ,column,value);
				return abc;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> xyz = sc_service.getAllSortedData(pageable1);
				return xyz;
			}

		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/school/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				Schools school = sc_service.get(id);
				ResponseEntity<Object> school_response= ResponseHandler.generateResponse(true, HttpStatus.OK, school);
				return school_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,HttpStatus.NOT_FOUND);
				return response1;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/school/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid Schools school, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				// Schools existSchool = sc_service.get(id);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				school.setModified_by(jwtDetails.getUserId());
				school.setModified_username(jwtDetails.getUserName());
				sc_service.save_School1(school);
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return response;
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

	@DeleteMapping("/school/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			sc_service.delete(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateSchool/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if (RateLimitController.bucket.tryConsume(1)) {
			sc_service.delete1(id);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/school1/{school_name}")
	public ResponseEntity<Object> getschoolCount(@PathVariable String school_name) {
		if (RateLimitController.bucket.tryConsume(1)) {
			Integer schools = sc_service.getschoolCount(school_name);
			ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.OK, schools);
			return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/schoolValidation")
	public ResponseEntity<Object> schoolValidation(@RequestParam(value = "school_name",required = false) String school_name,@RequestParam(value = "school_id") Integer school_id,
			@RequestParam(value = "school_name_short",required = false) String school_name_short, @RequestParam(value = "ref_no",required = false) String ref_no){
		if (RateLimitController.bucket.tryConsume(1)) {
				HashMap<String,Boolean> valiadtion = sc_service.schoolValidation(school_name,school_name_short,ref_no,school_id);
				ResponseEntity<Object> schools_response = ResponseHandler.generateResponse(true, HttpStatus.OK, valiadtion);
				return schools_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}		
	}

}
