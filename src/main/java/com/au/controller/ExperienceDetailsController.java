package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.au.model.ExperienceDetails;
import com.au.response.ResponseHandler;
import com.au.service.ExperienceDetailsService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class ExperienceDetailsController {

	Logger log = LoggerFactory.getLogger(ExperienceDetailsController.class);

	@Autowired
	private ExperienceDetailsService eds_service;

	@PostMapping("/ExperienceDetails")
	public List<ExperienceDetails> saveExperienceDetails(@RequestBody @Valid List<ExperienceDetails> experiencedetails) {
		log.debug("Request {}", experiencedetails);
		return eds_service.saveExperienceDetails(experiencedetails);
	}

	@GetMapping("/getExperienceDetails")
	public List<ExperienceDetails> listAll() {
		return eds_service.listAll();

	}

	@GetMapping("/getExperienceDetails/{id}")
	public ResponseEntity<ExperienceDetails> get(@PathVariable Integer id) {
		try {

			ExperienceDetails product = eds_service.get(id);
			log.debug("Request {}", id);
			return new ResponseEntity<ExperienceDetails>(product, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<ExperienceDetails>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/ExperienceDetails/{id}")
	public ResponseEntity<ExperienceDetails> update(@RequestBody @Valid List<ExperienceDetails> experiencedetails,
			@PathVariable Integer id) {
		try {
			ExperienceDetails existProduct = eds_service.get(id);
			eds_service.saveExperienceDetails(experiencedetails);
			log.debug("Request {}", experiencedetails);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/ExperienceDetails/{id}")
	public void delete(@PathVariable Integer id) {
		eds_service.delete(id);
	}
	
	@GetMapping("/fetchExperienceDetails/{job_id}")
	public List<HashMap<String, Object>> fetchExperienceDetails(@PathVariable Integer job_id) {
		return eds_service.fetchExperienceDetails(job_id);
	}
	
	@PutMapping("/updateExperienceDetails/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<ExperienceDetails> pv, @PathVariable List<Integer> id)
			throws JsonParseException, JsonMappingException, IOException {
	if(RateLimitController.bucket.tryConsume(1)) {
	 try {
	  
	   eds_service.updateExperienceDetails(pv);
	   ResponseEntity<Object> familyStructure_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
	   return familyStructure_response;
	 	} catch (NoSuchElementException e) {
	 		ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
		return response;
	 	}
		} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	} 
}
	
	@PostMapping("/createExperienceDetails")
	public ResponseEntity<Object> createExperienceDetails(@RequestBody @Valid List<ExperienceDetails> ed)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			ExperienceDetails expDetails = new ExperienceDetails();
			ed.stream().forEach(f -> {
					f.setExp_id(expDetails.getExp_id());
			});
			List<ExperienceDetails> cos = eds_service.createExperienceDetails(ed);
			ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, cos);
		return co_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
