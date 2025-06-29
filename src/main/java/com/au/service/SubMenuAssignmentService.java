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
import com.au.model.SubMenuAssignment;
import com.au.repository.RolesRepository;
import com.au.repository.SubMenuAssignmentRepository;
import com.au.repository.SubMenuRepository;
import com.au.response.ResponseHandler;

@Service
public class SubMenuAssignmentService {

	@Autowired
	private SubMenuAssignmentRepository subMenuAssign_repo;
	
	@Autowired
	private SubMenuRepository subMenu_repo;
	
	@Autowired
	private RolesRepository rolesRepository;

	public List<SubMenuAssignment> listAll() {
		return subMenuAssign_repo.findAll1();
	}

	public SubMenuAssignment saveSubMenuAssignment(SubMenuAssignment submenuassign) throws Exception{
		if(subMenuAssign_repo.countOfProgramRoleId(submenuassign.getRole_id())>=1) {
			throw new Exception("Submenu Assignment of this combination already exist");
		}else {
			List<Integer> submenu_id =ResponseHandler.toConvertCommaSeperatedIdsAsList(submenuassign.getSubmenu_ids());
			//frontend should send submenu_ids in Asc order
			String submenu_names =subMenu_repo.getSubmenuNameAsCommaSeperated(submenu_id); 
			submenuassign.setSubmenu_name(submenu_names);
			return subMenuAssign_repo.save(submenuassign);
		}
	}

	public SubMenuAssignment get(Integer id) {
		return subMenuAssign_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubMenuAssignment Id Not Found:" + id));
	}
	
	public SubMenuAssignment updateSubMenuAssignment(SubMenuAssignment submenuassign) {
		List<Integer> submenu_id =ResponseHandler.toConvertCommaSeperatedIdsAsList(submenuassign.getSubmenu_ids());
		//frontend should send submenu_ids in Asc order
		String submenu_names =subMenu_repo.getSubmenuNameAsCommaSeperated(submenu_id);
		submenuassign.setSubmenu_name(submenu_names);
		return subMenuAssign_repo.save(submenuassign);
	}

	public void delete(Integer id) {
		SubMenuAssignment ys = subMenuAssign_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubMenuAssignment Id Not Found:" + id));
		subMenuAssign_repo.update(id);
	}

	public void delete1(Integer id) {
		SubMenuAssignment ys = subMenuAssign_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubMenuAssignment Id Not Found:" + id));
		subMenuAssign_repo.update1(id);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> response1 = subMenuAssign_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
	
		Page<Object> response = subMenuAssign_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
/*
	public List<SubMenuAssignment> fetchdetails1(Integer role_id) {
		return subMenuAssign_repo.fetchSubMenu1(role_id);
	}
*/

	public List<HashMap<String, Object>> fetchdetails(List<Integer> role_id) {
		return subMenuAssign_repo.fetchSubMenu(role_id);
	}

	public List<HashMap<String, Object>> fetchdetails2(Integer role_id) {
		return subMenuAssign_repo.fetchSubMenu2(role_id);
	}

	public HashMap<String, Object> getAllAssignedRoleBySubmenuId(Integer submenu_id) {
		HashMap<String, Object> response1 = new HashMap<>();
		String submenu_ids1 = submenu_id + ",%";
		String submenu_ids2 = "%," + submenu_id + ",%";
		String submenu_ids3 = "%," + submenu_id;
		String submenu_ids4 = submenu_id.toString();
		List<Integer> assignedRoleIds=subMenuAssign_repo.getAllAssignedRoleBySubmenuId(submenu_ids1, submenu_ids2, submenu_ids3, submenu_ids4);
		response1.put("AssignedRole", rolesRepository.findAllById(assignedRoleIds));
		return response1;
	}
}
