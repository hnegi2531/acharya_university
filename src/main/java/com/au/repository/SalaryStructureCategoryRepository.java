package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.SalaryStructureCategory;

@Transactional
@Repository
public interface SalaryStructureCategoryRepository extends JpaRepository<SalaryStructureCategory, Integer> {

	@Query(value = "select ss from SalaryStructureCategory ss where ss.active=true")
	public List<SalaryStructureCategory> findAll1();
	
	@Modifying
	@Query(value = "update SalaryStructureCategory ss set ss.active=false where ss.salary_structure_category_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update SalaryStructureCategory ss set ss.active=true where ss.salary_structure_category_id=?1")
	public void update1(Integer id);
	
	@Query(value = "Select new map(ssc.salary_structure_category_id as id,ssc.salary_structure_category_name as salary_structure_category_name,"
			+ "ssc.salary_structure_category_short_name as salary_structure_category_short_name,ssc.created_by as created_by,"
			+ "ssc.modified_by as modified_by,ssc.created_date as created_date,ssc.modified_date as modified_date,"
			+ "ssc.active as active,ssc.created_username as created_username,ssc.modified_username as modified_username) From SalaryStructureCategory ssc "
			+ "Where CONCAT(IfNull(ssc.salary_structure_category_id,''),'',IfNull(ssc.salary_structure_category_name,''),'',"
			+ "IfNull(ssc.salary_structure_category_short_name,''),'',IfNull(ssc.created_by,''),'',IfNull(ssc.created_date,''),'',IfNull(ssc.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ssc.salary_structure_category_id as id,ssc.salary_structure_category_name as salary_structure_category_name,"
			+ "ssc.salary_structure_category_short_name as salary_structure_category_short_name,ssc.created_by as created_by,"
			+ "ssc.modified_by as modified_by,ssc.created_date as created_date,ssc.modified_date as modified_date,"
			+ "ssc.active as active,ssc.created_username as created_username,ssc.modified_username as modified_username) From SalaryStructureCategory ssc")
	public Page<Object> findAll3(Pageable pageable);

}