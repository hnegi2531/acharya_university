package com.au.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Organization;
import com.au.repository.Org_Repository;
import com.au.response.ResponseHandler;

@Service
public class Org_service {

	@Autowired
	private Org_Repository org_repo;

	public List<Organization> listAll() {
		return org_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return org_repo.findAll2();
		Page<Object> response1 = org_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return org_repo.findAll3();
		Page<Object> response = org_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public Organization save_Org(Organization org) throws Exception {
		if (getOrgName(org.getOrg_name()) >= 1)
			throw new Exception("Organization Already Exist");
		else if(getOrgType(org.getOrg_type()) >= 1)
			throw new Exception("organation type  Already Exist");
		
		else {
				org_repo.save(org);
				}
		return org;
	}
	
	
	private Integer getOrgName(String org_name) {
		return org_repo.getcountName(org_name);
	}
	
	private Integer getOrgType(String org_type) {
		return org_repo.getcountType(org_type);
	}
	
	
    public Organization get(Integer id) {
		return org_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organization Not Found:" + id));
	}

	public void delete(Integer id) {
		Organization ay = org_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Organization Not Found:" + id));
		org_repo.update(id);
	}

	public void delete1(Integer id) {
		Organization ay = org_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Organization Not Found:" + id));
		org_repo.update1(id);
	}

	public Organization save_Org1(Organization org) {
		return org_repo.save(org);

	}
	
	 public HashMap<String,Boolean> orgValidation(String org_name, String org_type){
		 HashMap<String,Boolean> valiadtion=new HashMap<>();
		 if(org_name != null & org_type == null) {
			 Boolean b =org_repo.validationForOrgName(org_name);
			 valiadtion.put("school_name", b);
			 return valiadtion;
			 
		 }else if(org_type != null & org_name == null) {
			 Boolean b =org_repo.validationForOrgType(org_type);
			 valiadtion.put("school_name_short", b);
			 return valiadtion;
		 }else {
			 Boolean b =org_repo.validationForOrgName(org_name);
			 Boolean b1 =org_repo.validationForOrgType(org_type);
			 valiadtion.put("org_name", b);
			 valiadtion.put("org_type", b1);
			 return valiadtion;
		}
	 }

}
