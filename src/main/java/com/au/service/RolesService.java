package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Roles;
import com.au.repository.RolesRepository;
import com.au.response.ResponseHandler;

@Service
public class RolesService {

	@Autowired
	private RolesRepository rr_repo;
	
	public Roles saveRoles(Roles roles) throws Exception {
		if(rr_repo.getCountRoleName(roles.getRole_name()) >= 1) {
			throw new Exception("Role name Already present");
		} else if(rr_repo.getCountRoleShortName(roles.getRole_short_name()) >= 1) {
			throw new Exception("Short Name Already Exist");
		} else {
		roles.setLms_role(roles.getLms_status());	
		Roles role=rr_repo.save(roles);
		return role;
		}
	}

	public List<Roles> listAllActiveRoles() {
		List<Roles> roles=rr_repo.findAll();
		return roles;
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		//String dynamicQuery1 = CONCAT(cw.ac_year_id, ' ',cw.candidate_name) LIKE %:keyword%;
		//String dynamicQuery1 = column +"="+keyword +" and "+column+"="+ value;
		Page<Object> roles_filtered_response = rr_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> roles_sorted_response = rr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}
	
	public Roles saveRole(Roles role) {
		return rr_repo.save(role);
	}

	public Roles get(Integer id) {
		return rr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Roles Id Not Found:" + id));
		
	}

	public void deactivate(Integer id) {
		Roles ay = rr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Roles Id Not Found:" + id));
		 rr_repo.updateToDeactive(id);
	
		
	}

	public void activate(Integer id) {
		Roles ay = rr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Roles Id Not Found:" + id));
		rr_repo.updateToActive(id);
		
	}
}
