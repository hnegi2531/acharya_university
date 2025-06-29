package com.au.service;

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
import com.au.exception.ResourceNotFoundException;
import com.au.model.Schools;
import com.au.repository.Org_Repository;
import com.au.repository.School_Repository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class School_Service {

	@Autowired
	private School_Repository sc_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private Org_Repository org_repo;

	public List<Schools> listAll() {
		return sc_repo.findAll12();
	}
	
	public List<Map<String, Object>> getSchoolDetails() {
		return sc_repo.getSchoolDetails();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		// String dynamicQuery1 = CONCAT(cw.ac_year_id, ' ',cw.candidate_name) LIKE
		// %:keyword%;
		// String dynamicQuery1 = column +"="+keyword +" and "+column+"="+ value;
		List<Map<String, Object>> schools_filtered_response = sc_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, schools_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		List<Map<String, Object>> schools_sorted_response = sc_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, schools_sorted_response);
	}

	public Schools save_School(Schools s) throws Exception {
		if (getschoolCount(s.getSchool_name()) >= 1) {
			throw new Exception("School Already Exist");
		} else if (getSchoolShortCount(s.getSchool_name_short()) >= 1) {
			throw new Exception("Short Name Already Exist");
		} else if (getPriorityCount(s.getPriority()) >= 1){
			throw new Exception("Priority Already Exist");
		} else {	
			s.setOrg_name(org_repo.getOrgnizationName(s.getOrg_id()));
			sc_repo.save(s);
		}
		return s;
	}

	/*
	 * 
	 * List<Schools> list = sc_repo.getSchoolNames(s.getSchool_name(),
	 * s.getSchool_name_short()); if (list != null && list.size() != 0) { throw new
	 * Exception("School already exist"); } else { List<Schools> list1 =
	 * sc_repo.getpriority(s.getPriority()); if (list1 != null && list1.size() != 0)
	 * { throw new Exception("Prority already present"); } else { try {
	 * sc_repo.save(s); } catch (Exception e) { if
	 * (e.getMessage().contains("school_name_UNIQUE")) { throw new
	 * Exception("School Name already present"); } else if
	 * (e.getMessage().contains("school_name_short_UNIQUE")) { throw new
	 * Exception("School short name already present"); } else { e.printStackTrace();
	 * } } } } return s; }
	 */

	private Integer getSchoolShortCount(String school_name_short) {
		return sc_repo.getschoolshortCount(school_name_short);
	}

	public Schools get(Integer id) {
		return sc_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("School id Not Found:" + id));
	}

	public void delete(Integer id) {
		Schools s = sc_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("School id Not Found:" + id));
		sc_repo.updateSchool(id);
	}

	public void delete1(Integer id) {
		Schools s = sc_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("School id Not Found:" + id));
		sc_repo.updateSchool1(id);
	}

	public Integer getschoolCount(String school_name) {
		return sc_repo.getschoolCount(school_name);
	}

	public Schools save_School1(@Valid Schools school) throws Exception{
		
			school.setOrg_name(org_repo.getOrgnizationName(school.getOrg_id()));
			return sc_repo.save(school);
		

	}
	
	public Integer getPriorityCount(Integer priority) {
		return sc_repo.getPriorityCount(priority);
	}
	
	 public HashMap<String,Boolean> schoolValidation(String school_name, String school_name_short, String ref_no,Integer school_id){
		 HashMap<String,Boolean> valiadtion=new HashMap<>();
		 if(school_name != null & school_name_short == null & ref_no == null) {
			 Boolean b =sc_repo.validationForSchoolName(school_name,school_id);
			 valiadtion.put("school_name", b);
			 return valiadtion;
			 
		 }else if(school_name_short != null & school_name == null & ref_no == null) {
			 Boolean b =sc_repo.validationForSchoolNameShort(school_name_short,school_id);
			 valiadtion.put("school_name_short", b);
			 return valiadtion;
		}else if(ref_no != null & school_name == null & school_name_short == null) {
			Boolean b =sc_repo.validationForReferenceNumber(ref_no,school_id);
			 valiadtion.put("ref_no", b);
			 return valiadtion;
		 }else {
			 Boolean b =sc_repo.validationForSchoolName(school_name,school_id);
			 Boolean b1 =sc_repo.validationForSchoolNameShort(school_name_short,school_id);
			 Boolean b2 =sc_repo.validationForReferenceNumber(ref_no,school_id);
			 valiadtion.put("school_name", b);
			 valiadtion.put("school_name_short", b1);
			 valiadtion.put("ref_no", b2);
			 return valiadtion;
		}
		
	 }



}
