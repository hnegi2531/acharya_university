
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
import com.au.dto.DepartmentAssignmentRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.DepartmentAssignment;
import com.au.repository.DepartmentAssignmentRepository;
import com.au.response.ResponseHandler;

@Service
public class DepartmentAssignmentService {

	@Autowired
	private DepartmentAssignmentRepository deptAssign_repository;

	public List<DepartmentAssignment> saveDepartmentAssignmentRequest(DepartmentAssignmentRequest deptAssignReq) {
		List<DepartmentAssignment> lda = new ArrayList<DepartmentAssignment>();

		deptAssignReq.getSchool_id().stream().forEach(dar -> {
			DepartmentAssignment da = new DepartmentAssignment();
			if (getCountDepartmentAssignment(deptAssignReq.getDept_id(), dar) >= 1) {
				throw new RuntimeException("Department Assignmet of this combination alrteady exist");
			} else {
				da.setSchool_id(dar);
				da.setDept_id(deptAssignReq.getDept_id());
				da.setService_oriented(deptAssignReq.getService_oriented());
				da.setPriority(deptAssignReq.getPriority());
				da.setCreated_by(deptAssignReq.getCreated_by());
				da.setModified_by(deptAssignReq.getModified_by());
				da.setCreated_username(deptAssignReq.getCreated_username());
				da.setModified_username(deptAssignReq.getModified_username());
				da.setActive(deptAssignReq.getActive());
				saveDepartmentAssignment(da);
				lda.add(da);
			}
		});
		return lda;
	}

	public DepartmentAssignment get(Integer id) {
		// deptAssign_repository.findById1(id);
		return deptAssign_repository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Depart_Assign Id Not Found:" + id));
	}

	public void delete(Integer id) {
		DepartmentAssignment ms = deptAssign_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Depart_Assign Id Not Found:" + id));
		deptAssign_repository.update(id);
	}

	public void delete1(Integer id) {
		DepartmentAssignment ms = deptAssign_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Depart_Assign Id Not Found:" + id));
		deptAssign_repository.update1(id);
	}

	public List<DepartmentAssignment> listAll() {
		return deptAssign_repository.findAll1();
	}

	public Integer getDeptCountByDnameSchool(Integer dept_id, Integer school_id) {
		return deptAssign_repository.getDeptCountByDnameSchool(dept_id, school_id);
	}

	public List<DepartmentAssignment> getDeptBySchholId(Integer school_id) {
		return deptAssign_repository.getDeptBySchholId(school_id);
	}

	public List<HashMap<String, Object>> getDeptBySchholId1(Integer school_id) {
		return deptAssign_repository.findAll1(school_id);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = deptAssign_repository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = deptAssign_repository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

//	public List<HashMap<String, Object>> listAll1() {
//		return deptAssign_repository.findAll11();
//	}

	public DepartmentAssignment saveDepartmentAssignment(DepartmentAssignment da) {
		return deptAssign_repository.save(da);
	}

//	public List<DepartmentAssignment> saveDepartmentAssignment1(List<DepartmentAssignment> da) {
//		return deptAssign_repository.saveAll(da);
//	}
	
	public DepartmentAssignment saveDepartmentAssignment1(DepartmentAssignment da) {
		return deptAssign_repository.save(da);
	}
	
	

	public Integer getCountDepartmentAssignment(Integer dept_id, Integer school_id) {
		return deptAssign_repository.getCountDepartmentAssignment(dept_id, school_id);
	}

	public List<Map<String, Object>> getActiveDepartmentAssignmentBasedOnTag() {
		return deptAssign_repository.getActiveDepartmentAssignmentBasedOnTag();
	}

	
}
