package com.au.service;


import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.model.HostelHeadWiseAmt;
import com.au.model.VoucherHeadNew;
import com.au.repository.HostelHeadWiseAmtRepository;
import com.au.repository.VoucherHeadNewRepository;
import com.au.response.ResponseHandler;

@Service
public class HostelHeadWiseAmtService {
	
	@Autowired
	private HostelHeadWiseAmtRepository hostel_head_wise_amt_repo;
	
	@Autowired
	private VoucherHeadNewRepository vocher_head_new_repo;
	
	public HostelHeadWiseAmt saveHostelHeadWiseAmt(HostelHeadWiseAmt s) {
		return hostel_head_wise_amt_repo.save(s);
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = hostel_head_wise_amt_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = hostel_head_wise_amt_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public List<VoucherHeadNew> voucherHeadNewOnHostelStatus() {
		return vocher_head_new_repo.voucherHeadNewOnHostelStatus();
	}
	
	public List<HashMap<String, Object>> hostelHeadWiseAmtOnFeeTemplateId(Integer hostel_fee_template_id){
		return hostel_head_wise_amt_repo.hostelHeadWiseAmtOnFeeTemplateId(hostel_fee_template_id);
	}
}
