package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.AliasName;
import com.au.model.VoucherHeadNew;
import com.au.repository.AliasNameRepository;
import com.au.repository.VoucherHeadNewRepository;
import com.au.response.ResponseHandler;

@Service
public class VoucherHeadNewService {

	@Autowired
	private VoucherHeadNewRepository voucherHeadNewRepository;
	
	@Autowired
	AliasNameRepository aliasNameRepository;

	public List<VoucherHeadNew> listAll() {
		return voucherHeadNewRepository.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = voucherHeadNewRepository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = voucherHeadNewRepository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public VoucherHeadNew saveVoucherHeadNew(VoucherHeadNew s) throws Exception{
		String vh = s.getVoucher_head();
		AliasName al1 = new AliasName();

		List<String> list2 = aliasNameRepository.getAliasNames();
		if(voucherHeadNewRepository.countOfVoucherHead(s.getVoucher_head())>=1) {
			throw new Exception("Name Already exist");
		}else if(voucherHeadNewRepository.countOfVoucherHeadShortName(s.getVoucher_head_short_name())>=1) {
			throw new Exception("Short Name Already exist");
		}else if (list2.contains(vh)) {
			throw new RuntimeException("This Vocher Head already exist in Alias Table");
		} else {
			al1.setAlias_name(vh);
			aliasNameRepository.save(al1);
			return voucherHeadNewRepository.save(s);
		}
		
	}

	public VoucherHeadNew get(Integer id) {
		return voucherHeadNewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("VoucherHeadNew Not Found:" + id));
	}

	public void delete(Integer id) {
		voucherHeadNewRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("VoucherHeadNew Not Found:" + id));
		voucherHeadNewRepository.update(id);
	}

	public void delete1(Integer id) {
		 voucherHeadNewRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("VoucherHeadNew Not Found:" + id));
		voucherHeadNewRepository.update1(id);

	}
	
	public VoucherHeadNew saveVoucherHeadNew1(VoucherHeadNew s) {
		VoucherHeadNew voucherHeadNewbyId=get(s.getVoucher_head_new_id());
		return voucherHeadNewRepository.save(s);
	}
	
	public List<Map<String, Object>> fetchUnassignedSchoolDetails(Integer voucher_head_new_id) {
		return voucherHeadNewRepository.fetchUnassignedSchoolDetails(voucher_head_new_id);
	}
	
	public List<VoucherHeadNew> VoucherHeadNewDetailsOnIsSalaries() {
		return voucherHeadNewRepository.VoucherHeadNewDetailsOnIsSalaries();
	}
	
	public List<HashMap<String, Object>> getVoucherHeadNewData() {
		return voucherHeadNewRepository.getVoucherHeadNewData();
	}
	
	
	public List<HashMap<String, Object>> getVoucherHeadNewDataOutflow() {
		return voucherHeadNewRepository.getVoucherHeadNewDataOutflow();
	}

	public List<VoucherHeadNew> fetchVoucherHeadNewDetailsBasedOnCashOrBank() {
		return voucherHeadNewRepository.fetchVoucherHeadNewDetailsBasedOnCashOrBank();
	}
	
	public List<VoucherHeadNew> voucherHeadDetailsOnHostelStatus() {
		return voucherHeadNewRepository.voucherHeadDetailsOnHostelStatus();
	}

	public List<HashMap<String, Object>> fetchVoucherHeadIds(Integer school_id) {
		return voucherHeadNewRepository.fetchVoucherHeadIds(school_id);
	}

	public List<VoucherHeadNew> VoucherHeadNewDetailsOnJournal() {
		return voucherHeadNewRepository.VoucherHeadNewDetailsOnJournal();
	}

	public List<VoucherHeadNew> VoucherHeadNewDetailsWoJournal() {
		return voucherHeadNewRepository.VoucherHeadNewDetailsWoJournal();
	}


	
	

}
