package com.au.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.InternalTypes;


@Transactional
@Repository
public interface InternalTypesRepository extends JpaRepository<InternalTypes, Integer>{

	
	@Query(value = "select count(*) from InternalTypes it where it.internal_name=?1 and active=true")
	public Integer getInternalName(String internal_name);
	
	
	@Query(value = "select count(*) from InternalTypes it where it.internal_short_name=?1 and active=true")
	public Integer getCountInternalShortName(String internal_short_name);
	
	@Query(value = "select new map(it.internal_master_id as id,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "it.remarks as remarks,it.created_by as created_by,it.modified_by as modified_by,"
			+ "it.created_date as created_date,it.modified_date as modified_date,it.active as active,"
			+ "it.created_username as created_username,it.modified_username as modified_username) from InternalTypes it "
			+ "where CONCAT(IfNull(it.created_username,''),'',IfNull(it.internal_name,''),'',IfNull(it.internal_short_name,''),'',IfNull(it.remarks,''),"
			+ "'',IfNull(it.created_by,''),'',IfNull(it.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(it.internal_master_id as id,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "it.remarks as remarks,it.created_by as created_by,it.modified_by as modified_by,"
			+ "it.created_date as created_date,it.modified_date as modified_date,it.active as active,"
			+ "it.created_username as created_username,it.modified_username as modified_username) from InternalTypes it")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Modifying
	@Query(value = "update InternalTypes it set it.active=false where it.internal_master_id=?1")
	public void updateDept(Integer id);
	
	@Modifying
	@Query(value = "update InternalTypes it set it.active=true where it.internal_master_id=?1")
	public void updateDept1(Integer id);

	
	
}
