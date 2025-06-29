package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.FeeAdmissionSubCategoryRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FeeAdmissionSubCategory;
import com.au.repository.FeeAdmissionSubCategoryRepository;
import com.au.response.ResponseHandler;

@Service
public class FeeAdmissionSubCategoryService {

	@Autowired
	private FeeAdmissionSubCategoryRepository fee_repo;

	public  ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return fee_repo.findAll1();
		Page<Object> response1 = fee_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public  ResponseEntity<Object> listAll2(Pageable pageable) {
		//return fee_repo.findAll2();
		Page<Object> response = fee_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<FeeAdmissionSubCategory> listAll1() {
		return fee_repo.findAll11();
	}

	public FeeAdmissionSubCategory save_FeeAdmission(FeeAdmissionSubCategory fee) {
		return fee_repo.save(fee);
	}

//	public FeeAdmissionSubCategory saveFeeAdmission(FeeAdmissionSubCategory fee) throws Exception {
//		if(getFeeAdmissionSubCategoryNameCount(fee.getFee_admission_sub_category_name())>=1)
//			throw new Exception("Fee Admission Sub Category Name Already Exist");
//		else if (getFeeAdmissionSubCategoryShortNameCount(fee.getFee_admission_sub_category_short_name()) >= 1) {
//			throw new Exception("Short Name Already Exist");
//		} else {
//			fee_repo.save(fee);
//		}
//		return fee;
//	}
	
		public List<FeeAdmissionSubCategory> saveFeeAdmission(@Valid FeeAdmissionSubCategoryRequest fee) throws Exception{
		List<FeeAdmissionSubCategory> fasc = new ArrayList<FeeAdmissionSubCategory>();
		fee.getFee_admission_category_id().stream().forEach(fa ->{
			FeeAdmissionSubCategory da = new FeeAdmissionSubCategory();
			
		if(fee_repo.getFeeAdmissionSubCategoryNameCount(fee.getFee_admission_sub_category_name(),fa)>=1) {
			throw new RuntimeException("Combination of Fee Admission Sub Category Name And short name Already Exist");
//		}else if (fee_repo.getFeeAdmissionSubCategoryShortNameCount(fee.getFee_admission_sub_category_short_name(),fa) >= 1) {
//			throw new RuntimeException("Short Name Already Exist");
		} else {
			da.setFee_admission_category_id(fa);
			da.setApprove_intake(fee.getApprove_intake());
			da.setBoard_unique_id(fee.getBoard_unique_id());
			da.setCreated_by(fee.getCreated_by());
			da.setModified_by(fee.getModified_by());
			da.setCreated_username(fee.getCreated_username());
			da.setModified_username(fee.getModified_username());
			da.setActive(fee.getActive());
			da.setFee_admission_sub_category_name(fee.getFee_admission_sub_category_name());
			da.setFee_admission_sub_category_short_name(fee.getFee_admission_sub_category_short_name());
			fee_repo.save(da);
			fasc.add(da);
		
		}
	});
		return  fasc;
	}

	public Integer getfeeAdmissionSubCategory(String fee_admission_sub_category_name,
			String fee_admission_sub_category_short_name, Integer fee_admission_category_id, Integer board_unique_id) {
		return fee_repo.getProgramSpecilization(fee_admission_sub_category_name, fee_admission_sub_category_short_name,
				fee_admission_category_id, board_unique_id);
	}

	public FeeAdmissionSubCategory get(Integer id) {
		return fee_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeAdmissionSubCategory  Not Found:" + id));
	}

	public void delete(Integer id) {
		FeeAdmissionSubCategory ay = fee_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeAdmissionSubCategory Not Found:" + id));
		fee_repo.update(id);
	}

	public List<FeeAdmissionSubCategory> getFeeAdmissionByFeeAdmissionCategoryId(Integer fee_admission_category_id) {
		return fee_repo.getFeeAdmissionByFeeAdmissionCategoryId(fee_admission_category_id);
	}

	public void delete1(Integer id) {
		FeeAdmissionSubCategory ay = fee_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeAdmissionSubCategory Not Found:" + id));
		fee_repo.update1(id);
	}

	public List<HashMap<String, Object>> getFeeAdmissionSubCategory(Integer fee_admission_sub_category_id) {
		return fee_repo.getBoardName(fee_admission_sub_category_id);
	}

	public List<Map<String, Object>> concateFeeAdmissionSubCategoryDetail() {
		return fee_repo.concateFeeAdmissionSubCategoryDetail();
	}
	
//	public Integer getFeeAdmissionSubCategoryNameCount(String fee_admission_sub_category_name,) {
//		return fee_repo.getFeeAdmissionSubCategoryNameCount(fee_admission_sub_category_name);
//	}
//	
//	public Integer getFeeAdmissionSubCategoryShortNameCount(String fee_admission_sub_category_short_name) {
//		return fee_repo.getFeeAdmissionSubCategoryShortNameCount(fee_admission_sub_category_short_name);
//	}
}
