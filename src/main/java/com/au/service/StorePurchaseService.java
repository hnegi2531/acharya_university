package com.au.service;

import com.au.model.StorePurchase;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.StorePurchaseRepository;
import com.au.response.ResponseHandler;

@Service
public class StorePurchaseService {
	
	@Autowired
	private StorePurchaseRepository sp_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	
	public StorePurchase savestorePurchase(StorePurchase sp) throws Exception {
		if (getEmpCount(sp.getEmp_id()) >= 1)
			throw new Exception("Employee Name already exist");
		else {
			sp_repo.save(sp);
		}
		return sp;
	}

    private Integer getEmpCount(Integer emp_id) {
		return sp_repo.getEmpCount(emp_id);
	}

	public List<StorePurchase> listAllActiveStorePurchase() {
		List<StorePurchase> sp=sp_repo.findAll1();
		return sp;
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> ic_filtered_response = sp_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ic_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
        Page<Object> ic_sorted_response = sp_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ic_sorted_response);
	}
	
	
	public StorePurchase get(Integer id) {
		return sp_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store-Purchase Id Not Found:" + id));
		}
	
	public StorePurchase saveUpdateStorePurchase(StorePurchase sp) {
             return sp_repo.save(sp);
	}
	
	
	
	public void deactivate(Integer id) {
		StorePurchase sp = sp_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store-Purchase Id Not Found:" + id));
		sp_repo.updateToDeactive(id);
	
		
	}

	public void activate(Integer id) {
		StorePurchase sp = sp_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store-Purchase Id Not Found:" + id));
		sp_repo.updateToActive(id);
		
	}
	
	public List<Map<String,Object>> getEmpNameConcatWithCode() {	
		return sp_repo.getEmpNameConcatWithCode();
	}

}
