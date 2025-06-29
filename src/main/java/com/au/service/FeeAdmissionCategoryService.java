package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FeeAdmissionCategory;
import com.au.repository.FeeAdmissionCategoryRepository;
import com.au.response.ResponseHandler;

@Service
public class FeeAdmissionCategoryService {

	@Autowired
	private FeeAdmissionCategoryRepository fee_repo;

	public List<FeeAdmissionCategory> listAll() {
		return fee_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = fee_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		//return fee_repo.findAll();
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = fee_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		//return fee_repo.findAll();
	}

	public FeeAdmissionCategory saveFeeAdmissionCategory(FeeAdmissionCategory admissionCategory) throws Exception {
		if(getFeeAdmissionCategoryTypeCount(admissionCategory.getFee_admission_category_type())>=1)
			throw new Exception("Admission Category Name Already exist");
		else if (getFeeAdmissionCategoryShortNameCount(admissionCategory.getFee_admission_category_short_name()) >= 1) {
			throw new Exception("Short Name Already Exist");
		} else {
			fee_repo.save(admissionCategory);
		}
		return admissionCategory;
	}

	public FeeAdmissionCategory get(Integer id) {
		return fee_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeAdmissionCategory  Not Found:" + id));
	}

	public void delete(Integer id) {
		FeeAdmissionCategory ay = fee_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeAdmissionCategory Not Found:" + id));
		fee_repo.update(id);
	}

	public void delete1(Integer id) {
		FeeAdmissionCategory ay = fee_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeAdmissionCategory Not Found:" + id));
		fee_repo.update1(id);
	}

	public FeeAdmissionCategory saveFeeAdmissionCategory1(FeeAdmissionCategory fee) {
		return fee_repo.save(fee);

	}
	
	public Integer getFeeAdmissionCategoryTypeCount(String fee_admission_category_type) {
		return fee_repo.getFeeAdmissionCategoryCount(fee_admission_category_type);
	}
	
	public Integer getFeeAdmissionCategoryShortNameCount(String fee_admission_category_short_name) {
		return fee_repo.getFeeAdmissionCategoryShortNameCount(fee_admission_category_short_name);
	}
}
