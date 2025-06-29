package com.au.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.JwtDetails;
import com.au.model.AddOnFeeVoucherHeadWise;
import com.au.repository.AddOnFeeVoucherHeadWiseRepository;
import com.au.response.ResponseHandler;

@Service
public class AddOnFeeVoucherHeadWiseService {
	
	@Autowired
	private AddOnFeeVoucherHeadWiseRepository adonvh_repo;
	
	
	@Autowired
	private JwtTokenService jwt_service;

	private Logger logger = LoggerFactory.getLogger(AddOnFeeVoucherHeadWiseService.class);

	public AddOnFeeVoucherHeadWise createAddOnFeeVoucherHeadWise(AddOnFeeVoucherHeadWise pr, JwtDetails jwtDetails) throws Exception {	
		return	adonvh_repo.save(pr);

	}

	public List<AddOnFeeVoucherHeadWise> listAll() {
		return adonvh_repo.findAll1();
	}

	public AddOnFeeVoucherHeadWise get(Integer addOnFeeVoucherHeadWiseId) {
		return adonvh_repo.findById(addOnFeeVoucherHeadWiseId)
				.orElseThrow(() -> new ResourceNotFoundException("AddOnFeeVoucherHeadWise Not Found:" + addOnFeeVoucherHeadWiseId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> AddOnFeeVoucherHeadWise_filtered_response = adonvh_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, AddOnFeeVoucherHeadWise_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> AddOnFeeVoucherHeadWise_response = adonvh_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, AddOnFeeVoucherHeadWise_response);
	}

	public AddOnFeeVoucherHeadWise updateAddOnFeeVoucherHeadWise(AddOnFeeVoucherHeadWise pr) {
		return	adonvh_repo.save(pr);
	    
	}

	public void delete(Integer addOnFeeVoucherHeadWiseId) {
		AddOnFeeVoucherHeadWise sir = adonvh_repo.findById(addOnFeeVoucherHeadWiseId)
				.orElseThrow(() -> new ResourceNotFoundException("AddOnFeeVoucherHeadWise Not Found:" + addOnFeeVoucherHeadWiseId));
		adonvh_repo.updateAddOnFeeVoucherHeadWise(addOnFeeVoucherHeadWiseId);
	}

	public void delete1(Integer addOnFeeVoucherHeadWiseId) {
		AddOnFeeVoucherHeadWise sir = adonvh_repo.findById(addOnFeeVoucherHeadWiseId)
				.orElseThrow(() -> new ResourceNotFoundException("AddOnFeeVoucherHeadWise Not Found:" + addOnFeeVoucherHeadWiseId));
		adonvh_repo.updateAddOnFeeVoucherHeadWise1(addOnFeeVoucherHeadWiseId);
	}

}
