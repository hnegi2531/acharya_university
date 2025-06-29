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
import com.au.model.City;
import com.au.service.CityService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CityController {

	@Autowired
	private CityService c_service;
	
	Logger log = LoggerFactory.getLogger(CityController.class);
	
	
	@GetMapping("/City")
	public List<City> listAll() {
		return c_service.listAll();
	}

	@GetMapping("/City/{id}")
	public ResponseEntity<City> get(@PathVariable Integer id) {
		try {

			City product = c_service.get(id);
			return new ResponseEntity<City>(product, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<City>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/City1/{state_id}/{country_id}")
	public List<City> getCityByStateAndCountry(@PathVariable Integer state_id,@PathVariable Integer country_id){
		return c_service.getCityByStateAndCountry(state_id, country_id);
	}
}
