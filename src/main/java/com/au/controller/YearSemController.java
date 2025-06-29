package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;
import com.au.dto.JwtDetails;
import com.au.model.YearSem;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.YearSemService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey3}")
@CrossOrigin
//@RequestMapping("/api")
public class YearSemController {

	Logger log = LoggerFactory.getLogger(YearSemController.class);

	@Autowired
	private YearSemService y_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/YearSem")
	public ResponseEntity<Object> saveSubjectType(@RequestBody @Valid YearSem r, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			r.setCreated_by(jwtDetails.getUserId());
			r.setCreated_username(jwtDetails.getUserName());
			YearSem ys = y_service.saveYearSem(r);
			ResponseEntity<Object> year_sem_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, ys);
			return year_sem_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
		
	}

	@GetMapping("/YearSem")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<YearSem> yeam_sem_list = y_service.listAll();
			ResponseEntity<Object> year_sem_response = ResponseHandler.generateResponse(true, HttpStatus.OK, yeam_sem_list);
			return year_sem_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllYearSemDetails")
	public List<YearSem> listAll1() {
		return y_service.listAll1();
	}

	@GetMapping("/YearSem/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			YearSem product = y_service.get(id);
			ResponseEntity<Object> year_sem_response = ResponseHandler.generateResponse(true, HttpStatus.OK, product);
			return year_sem_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> rs = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return rs;
		}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1 );
			return rs;
		}
	}

	@PutMapping("/YearSem/{id}")
	public ResponseEntity<YearSem> update(@RequestBody @Valid YearSem r, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		try {
			// YearSem existProduct = y_service.get(id);
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			r.setModified_by(jwtDetails.getUserId());
			r.setModified_username(jwtDetails.getUserName());

			y_service.saveYearSem(r);
			return new ResponseEntity<YearSem>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<YearSem>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/YearSem/{id}")
	public void delete(@PathVariable Integer id) {
		y_service.delete(id);
	}

	@DeleteMapping("/activateYearSem/{id}")
	public void delete1(@PathVariable Integer id) {
		y_service.delete1(id);
	}

}
