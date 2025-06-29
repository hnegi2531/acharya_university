package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.DollarToInrConversion;
import com.au.repository.DollarToInrConversionRepository;
import com.au.response.ResponseHandler;

@Service
public class DollarToInrConversionService {

	@Autowired
	private DollarToInrConversionRepository dollar_to_inr_currency_conv_repo;
	
	
	public List<DollarToInrConversion> listAll() {
		return dollar_to_inr_currency_conv_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = dollar_to_inr_currency_conv_repo.findAll11(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = dollar_to_inr_currency_conv_repo.findAll12(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public DollarToInrConversion dollarToInrConversion(DollarToInrConversion dtucc){

		return dollar_to_inr_currency_conv_repo.save(dtucc);

	}

	public DollarToInrConversion get(Integer id) {
		return dollar_to_inr_currency_conv_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("DollarToInrConversion Not Found:" + id));
	}

	public void deActivate(Integer id) {
		dollar_to_inr_currency_conv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DollarToInrConversion Not Found:" + id));
		dollar_to_inr_currency_conv_repo.deActivate(id);
		
	}

	public void activate(Integer id) {
		dollar_to_inr_currency_conv_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DollarToInrConversion Not Found:" + id));
		dollar_to_inr_currency_conv_repo.activate(id);
		
	}


}
