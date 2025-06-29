package com.au.repository;

import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CommencementType;
import com.au.model.Department;


@Repository
@Transactional
public interface CommencementTypeRepository extends JpaRepository<CommencementType, Integer> {

	
	@Query(value = "select count(*) from CommencementType ct where ct.commencement_type=?1")
	public Integer getcountOfCommencementType(String commencement_type);
	
	
	@Query(value = "select new map(ct.commencement_id as id,ct.commencement_type as commencement_type,ct.date_selection as date_selection,"
			+ "ct.created_username as created_username,ct.modified_username as modified_username,ct.restriction_status as restriction_status,"
			+ "ct.created_date as created_date,ct.modified_date as modified_date,ct.created_by as created_by,"
			+ "ct.modified_by as modified_by,ct.active as active) from CommencementType ct "
			+ "where CONCAT(IfNull(ct.created_username,''),'',IfNull(ct.commencement_id,''),'',IfNull(ct.commencement_type,''),'',IfNull(ct.modified_username,''),"
			+ "'',IfNull(ct.created_by,''),'',IfNull(ct.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(ct.commencement_id as id,ct.commencement_type as commencement_type,ct.date_selection as date_selection,"
			+ "ct.created_username as created_username,ct.modified_username as modified_username,ct.restriction_status as restriction_status,"
			+ "ct.created_date as created_date,ct.modified_date as modified_date,ct.created_by as created_by,"
			+ "ct.modified_by as modified_by,ct.active as active) from CommencementType ct")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Query(value = "SELECT ct from CommencementType ct where ct.active=true")
	public List<CommencementType> findAll11();

	@Modifying
	@Query(value = "update CommencementType ct set ct.active=false where ct.commencement_id=?1")
	public void updateCommencementType(Integer id);
	
	@Modifying
	@Query(value = "update CommencementType ct set ct.active=true where ct.commencement_id=?1")
	public void updateCommencementType1(Integer id);


		@Query(value = "select new map(ct.commencement_id as commencement_id,ct.commencement_type as commencement_type,"
			+ "ct.date_selection as date_selection,ct.restriction_status as restriction_status,ct.category_details_id as category_details_id,"
			+ "ct.created_username as created_username,ct.modified_username as modified_username,"
			+ "ct.created_date as created_date,ct.modified_date as modified_date,ct.created_by as created_by,"
			+ "ct.modified_by as modified_by,ct.active as active,ctd.category_detail as category_detail) from CommencementType ct "
			+ "Left Join CategoryTypeDetails ctd On ctd.category_details_id=ct.category_details_id ")
	public List<HashMap<String, Object>> getCommencementTypeDetails();
	
	
}
