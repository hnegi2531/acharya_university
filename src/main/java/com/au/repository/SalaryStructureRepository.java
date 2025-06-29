package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.SalaryStructureDTO;
import com.au.model.SalaryStructure;

@Transactional
@Repository
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Integer> {

	@Query(value="select s from SalaryStructure s where s.active=true")
	public List<SalaryStructure> findAll1();
	
	@Modifying
	@Query(value = "update SalaryStructure ss set ss.active=false where ss.salary_structure_id=?1")
	public void update(Integer salary_structure_id);
	
	@Modifying
	@Query(value = "update SalaryStructure ss set ss.active=true where ss.salary_structure_id=?1")
	public void update1(Integer salary_structure_id);
	
	@Query(value = "Select new map(ss.salary_structure_id as id,ss.salary_structure as salary_structure,ss.print_name as print_name,"
			+ "ss.created_by as created_by,ss.modified_by as modified_by,ss.created_date as created_date,ss.modified_date as modified_date,"
			+ "ss.active as active,ss.remarks as remarks,ss.created_username as created_username,ss.modified_username as modified_username) From SalaryStructure ss "
			+ "Where CONCAT(IfNull(ss.salary_structure_id,''),'',IfNull(ss.salary_structure,''),'',IfNull(ss.print_name,''),'',"
			+ "IfNull(ss.created_by,''),'',IfNull(ss.created_date,''),'',IfNull(ss.remarks,''),'',IfNull(ss.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ss.salary_structure_id as id,ss.salary_structure as salary_structure,ss.print_name as print_name,"
			+ "ss.created_by as created_by,ss.modified_by as modified_by,ss.created_date as created_date,ss.modified_date as modified_date,"
			+ "ss.active as active,ss.remarks as remarks,ss.created_username as created_username,ss.modified_username as modified_username) From SalaryStructure ss")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM SalaryStructure ss where ss.salary_structure=?1 and ss.active=true")
	public Integer countOfSalaryStructure(String salary_structure);
	
	@Query(value = "SELECT count(*) FROM SalaryStructure ss where ss.print_name=?1 and ss.active=true")
	public Integer countOfPrintName(String print_name);
	
	@Query(value = "SELECT count(*) FROM SalaryStructure ss where ss.salary_structure_id !=?1 and ss.salary_structure=?2 and ss.active=true")
	public Integer countOfSalaryStructureForUpdate(Integer salary_structure_id ,String salary_structure);
	
	@Query(value = "SELECT count(*) FROM SalaryStructure ss where ss.salary_structure_id !=?1 and ss.print_name=?2 and ss.active=true")
	public Integer countOfPrintNameForUpdate(Integer salary_structure_id , String print_name);
	
	@Query(value = "select ss from SalaryStructure ss where ss.salary_structure_id=?1 and ss.active=true")
	public SalaryStructure getSalaryStructure(Integer salary_structure_id);

	@Query(value = "select ss.salary_structure from SalaryStructure ss where ss.salary_structure_id=?1 and ss.active=true")
	public String getSalaryStructureName(Integer salary_structure_id);

	@Query(value=" select new  com.au.dto.SalaryStructureDTO( s.salary_structure_id as salaryStructureId, s.salary_structure as salaryStructureName ) from SalaryStructure s  ")
	public List<SalaryStructureDTO> getsalaryStructures();

}
