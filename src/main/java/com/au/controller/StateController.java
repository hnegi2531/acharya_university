package com.au.controller;

import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.au.model.State;
import com.au.service.StateService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class StateController {

	@Autowired
	private StateService s;
	
	Logger log = LoggerFactory.getLogger(StateController.class);
	
	@GetMapping("/State")
	public List<State> listAll(){
		return s.listAll();
	}

	
	@GetMapping("/State/{id}")
	public ResponseEntity<State> get(@PathVariable Integer id) {
	    try {	    	
	    	State product = s.get(id);
	    	log.debug("request ---------{}",id);
	        return new ResponseEntity<State>(product, HttpStatus.OK);
	    } catch (NoSuchElementException e) {
	        return new ResponseEntity<State>(HttpStatus.NOT_FOUND);
	    }      
	}

	@GetMapping("/State1/{countryId}")
	public List<State> getStateByCountryId(@PathVariable Integer countryId){
    	return s.getStateByCountryId(countryId);
    }
	
}
