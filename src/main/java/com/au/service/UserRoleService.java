package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import com.au.dto.JwtDetails;
import com.au.dto.UserRoleDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.UserRole;
import com.au.repository.UserRoleRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class UserRoleService {

	@Autowired
	private UserRoleRepository urr_repo;
	
	@Autowired
	private JwtTokenService jwt_service;

	public List<UserRole> listAll() {
		return urr_repo.findAllUserRole();
	}

	public UserRole saveUserRole(UserRole userrole) {
		return urr_repo.save(userrole);
	}

	public UserRole get(Integer id) {
		return urr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("UserRole Id Not Found:" + id));
	}

	public void deactivate(Integer id) {
		UserRole ay = urr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("UserRole Id Not Found:" + id));
		urr_repo.updateToDeactivate(id);
		
	}

	public void activate(Integer id) {
		UserRole ay = urr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("UserRole Id Not Found:" + id));
		urr_repo.updateToActivate(id);
		
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		//String dynamicQuery1 = CONCAT(cw.ac_year_id, ' ',cw.candidate_name) LIKE %:keyword%;
		//String dynamicQuery1 = column +"="+keyword +" and "+column+"="+ value;
		Page<Object> filetered_response = urr_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, filetered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> sorted_response = urr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, sorted_response);
	}

	public List<HashMap<String, Object>> getRoleDetails(Integer id) {
		try {
			System.out.println(" getRoleDetails : "+ id );
			return urr_repo.fetchRoleDetails(id);
		}catch(Exception e) {
			System.out.println("Erroe : "+e.getMessage());
			return null;
		}
		
	}

	public List<UserRole> saveUserRoleDetails(@Valid UserRoleDto ur,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		List<UserRole> lda = new ArrayList<UserRole>();
		ur.getRole_id().stream().forEach(r -> {
			UserRole userRole = new UserRole();
			userRole.setId(ur.getId());
			userRole.setRole_id(r);
			userRole.setActive(ur.getActive());
			userRole.setCreated_by(jwtDetails.getUserId());
			userRole.setCreated_username(jwtDetails.getUserName());
			saveUserRole(userRole);
			lda.add(userRole);
		});
		return lda;

	}

	public List<HashMap<String, Object>> getActiveRoleDetails(Integer id, Integer role_id) {
		return urr_repo.fetchRoleDetails(id,role_id);
	}
	
	public List<HashMap<String, Object>> userDetailsByRoleId(Integer role_id) {
		return urr_repo.userDetailsByRoleId(role_id);
	}

	public List<HashMap<String, Object>> getUserDetailsBasedOnRole(Integer role_id) {
		return urr_repo.getUserDetailsBasedOnRole(role_id);
	}
}
