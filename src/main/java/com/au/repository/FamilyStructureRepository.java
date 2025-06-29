package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.au.model.FamilyStructure;


@Transactional
@Repository
public interface FamilyStructureRepository extends JpaRepository<FamilyStructure, Integer> {
	
	
	
	@Query(value = "select new map(fs.family_structure_id as id,fs.emp_id as emp_id,fs.dob as dob,fs.relationship as relationship,fs.occupation as occupation,fs.dependent as dependent,"
			+ "fs.created_username as created_username,fs.modified_username as modified_username,"
			+ "fs.created_date as created_date,fs.modified_date as modified_date,fs.created_by as created_by,"
			+ "fs.modified_by as modified_by,fs.active as active) from FamilyStructure fs "
			+ "where CONCAT(IfNull(fs.created_username,''),'',IfNull(fs.emp_id,''),'',IfNull(fs.relationship,''),'',IfNull(fs.occupation,''),"
			+ "'',IfNull(fs.created_by,''),'',IfNull(fs.created_date,'',IfNull(fs.dependent,''),'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	
	@Query(value = "select new map(fs.family_structure_id as id,fs.emp_id as emp_id,fs.dob as dob,fs.relationship as relationship,fs.occupation as occupation,fs.dependent as dependent,"
			+ "fs.created_username as created_username,fs.modified_username as modified_username,"
			+ "fs.created_date as created_date,fs.modified_date as modified_date,fs.created_by as created_by,"
			+ "fs.modified_by as modified_by,fs.active as active) from FamilyStructure fs")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Query(value = "SELECT fs from FamilyStructure fs where fs.active=true")
	public List<FamilyStructure> findAll11();
	
	@Modifying
	@Query(value = "update FamilyStructure fs set fs.active=false where fs.family_structure_id=?1")
	public void updateDept(Integer id);

	@Modifying
	@Query(value = "update FamilyStructure fs set fs.active=true where fs.family_structure_id=?1")
	public void updateDept1(Integer id);
	
	@Query(value = "select fs.family_structure_id as id,fs.emp_id as emp_id,fs.age as age,"
			+ "fs.relationship as relationship,fs.name as name,fs.contact_number as contact_number,"
			+ "ed.employee_name as employee_name,ed.mobile as mobile,ed.email as email,"
			+ "fs.created_username as created_username,fs.modified_username as modified_username,"
			+ "fs.created_date as created_date,fs.modified_date as modified_date,fs.created_by as created_by,"
			+ "fs.modified_by as modified_by,fs.active as active from family_structure fs "
			+ "Left Join employee_details ed On ed.emp_id = fs.emp_id "
			+ "where fs.emp_id=?1 ",nativeQuery=true)
	public List<Map<String, Object>> getFamilyStructureDetailsData(Integer empId);


	@Query(value = "select new map(fs.name as family_name,fs.age as age,fs.contact_number as contact_number,fs.relationship as relationship) from FamilyStructure fs where fs.emp_id=?1 And fs.relationship in ('Father', 'Spouse') And fs.active=true")
	public List<HashMap<String, Object>> getFamilyDetailsByEmployeeId(Integer employeeId);
	

}
