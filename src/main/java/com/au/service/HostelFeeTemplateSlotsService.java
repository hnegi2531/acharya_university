package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.model.HostelFeeTemplateSlots;
import com.au.model.HostelHeadWiseAmt;
import com.au.repository.HostelFeeTemplateSlotsRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelFeeTemplateSlotsService {

	@Autowired
	private HostelFeeTemplateSlotsRepository hostel_fee_templae_slots;

	public HostelFeeTemplateSlots saveHostelFeeTemplateSlots(HostelFeeTemplateSlots s) {

		return hostel_fee_templae_slots.save(s);

	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = hostel_fee_templae_slots.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = hostel_fee_templae_slots.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public List<HostelFeeTemplateSlots> getHostelFeeTemplateSlots(Integer hostel_fee_template_id){
		return hostel_fee_templae_slots.getHostelFeeTemplateSlots(hostel_fee_template_id);
	}
}
