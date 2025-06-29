package com.au.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.model.Country;
import com.au.service.CountryService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CountryController {

	@Autowired
	private CountryService c_service;
	
	@GetMapping("/Country")
	public List<Country> listAll() {
		return c_service.listAll();
	}

	@GetMapping("/Country/{id}")
	public ResponseEntity<Country> get(@PathVariable Integer id) {
		try {

			Country product = c_service.get(id);
			return new ResponseEntity<Country>(product, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<Country>(HttpStatus.NOT_FOUND);
		}
	}

}
