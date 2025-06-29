package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.EmployeeType;

@Repository
@Transactional
public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Integer>{

	public Boolean existsByEmpType(String empType);
	public Boolean existsByEmpTypeShortName(String empTypeShortName);
	
	@Query(value = "select h from EmployeeType h where h.active=true")
	public List<EmployeeType> findAll1();

	@Modifying
	@Query(value = "update EmployeeType h set h.active=false where h.empTypeId=?1")
	public void updateEmployeeType(Integer id);

	@Modifying
	@Query(value = "update EmployeeType h set h.active=true where h.empTypeId=?1")
	public void updateEmployeeType1(Integer id);
	
	@Query(value = "select new map(et.empTypeId as id,et.empType as empType,et.empTypeShortName as empTypeShortName,"
			+ "et.createdUsername as createdUsername,et.modifiedUsername as modifiedUsername,"
			+ "et.createdDate as createdDate,et.modifiedDate as modifiedDate,et.createdBy as createdBy,"
			+ "et.modifiedBy as modifiedBy,et.active as active) from EmployeeType et "
			+ "where CONCAT(IfNull(et.empTypeId,''),'',IfNull(et.empType,''),'',IfNull(et.empTypeShortName,''),"
			+ "'',IfNull(et.createdUsername,''),'',IfNull(et.createdBy,''),'',IfNull(et.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(et.empTypeId as id,et.empType as empType,et.empTypeShortName as empTypeShortName,"
			+ "et.createdUsername as createdUsername,et.modifiedUsername as modifiedUsername,"
			+ "et.createdDate as createdDate,et.modifiedDate as modifiedDate,et.createdBy as createdBy,"
			+ "et.modifiedBy as modifiedBy,et.active as active) from EmployeeType et")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = " select e.empTypeShortName from EmployeeType e where empTypeId=?1")
	public String getEmployeeTypeName(Integer empTypeId);
}
