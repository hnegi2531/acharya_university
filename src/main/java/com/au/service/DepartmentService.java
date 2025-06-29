package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Department;
import com.au.repository.DepartmentAssignmentRepository;
import com.au.repository.DepartmentRepository;
import com.au.response.ResponseHandler;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository deptrepo;
	
	@Autowired
	private DepartmentAssignmentRepository deptAssign_repository;

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = deptrepo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = deptrepo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public List<Department> listAll1() {
		return deptrepo.findAll11();
	}

	public Department save_Department(Department dept) throws Exception {

		if (getDeptName(dept.getDept_name()) >= 1)
			throw new Exception("Department Name already exist");
		else if (getDeptShortName(dept.getDept_name_short()) >= 1) {
			throw new Exception("short name already exist");
		} else {
			deptrepo.save(dept);
		}
		return dept;
	}

	private Integer getDeptShortName(String dept_name_short) {
		return deptrepo.getcountOfDept_short_name(dept_name_short);
	}

	private Integer getDeptName(String dept_name) {
		return deptrepo.getcountOfDept_name(dept_name);
	}

	public Department saveDepartment(Department department) {
		return deptrepo.save(department);
	}

	public Department get(Integer id) {
		return deptrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
	}

	public void delete(Integer id) {
		Department dept = deptrepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		deptrepo.updateDept(id);
	}

	public void delete1(Integer id) {
		Department dept = deptrepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
		deptrepo.updateDept1(id);
	}

	public Integer getDeptCountByDnameSchool(String dept_name, Integer school_id) {
		return deptrepo.getDeptCountByDnameSchool(dept_name, school_id);
	}

	public List<Department> getDeptBySchholId(Integer school_id) {
		return deptrepo.getDeptBySchholId(school_id);
	}
	public Boolean existsByDeptname(String dept_name) {
		return deptrepo.existsByDeptName(dept_name);
	}
	
	public List<Map<String, Object>> fetchUnassignedSchoolIds(Integer dept_id) {
		return deptAssign_repository.fetchUnassignedSchoolIds(dept_id);
	}

	public List<Map<String, Object>> allNoDuesDetails() {
		return deptrepo.allNoDuesDetails();
	}

	public List<Map<String, Object>> getDepartmentBasedOnHodId() {
		return deptrepo.getDepartmentBasedOnHodId();
	}
	

}
