package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Organization;

@Transactional
@Repository
public interface Org_Repository extends JpaRepository<Organization	, Integer>{

	@Query(value = "select o from Organization o where o.active=true")
	public List<Organization> findAll1();
	
	@Modifying
	@Query(value = "update Organization o set o.active=false where o.org_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Organization o set o.active=true where o.org_id=?1")
	public void update1(Integer id);

	@Query(value="select * from organization where org_name=?1 and org_type=?2 and active=true", nativeQuery = true)
	public List<Organization> getorganisation(String org_name, String org_type);
	
	@Query(value = "select count(*) from Organization o where o.org_name=?1 and o.active=true")
	public Integer getcountName(String org_name);
	
	@Query(value = "select count(*) from Organization o where o.org_type=?1 and o.active=true")
	public Integer getcountType(String org_type);
	
	@Query(value ="Select new map(o.org_id as id,o.org_name as org_name,o.org_type as org_type,o.created_username as created_username,"
			+ "o.address as address,"
			+ "o.modified_username as modified_username,o.created_date as created_date,o.modified_date as modified_date,"
			+ "o.created_by as created_by,o.modified_by as modified_by,o.active as active) From Organization o "
			+ "Where CONCAT(IfNull(o.org_id,''),'',IfNull(o.org_name,''),'',IfNull(o.org_type,''),'',IfNull(o.created_username,''),'',IfNull(o.created_date,''),'',IfNull(o.created_by,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(o.org_id as id,o.org_name as org_name,o.org_type as org_type,o.created_username as created_username,"
			+ "o.address as address,"
			+ "o.modified_username as modified_username,o.created_date as created_date,o.modified_date as modified_date,"
			+ "o.created_by as created_by,o.modified_by as modified_by,o.active as active) From Organization o")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "Select o.org_name From Organization o Where o.org_id=?1")
	public String getOrgnizationName(Integer org_id);
	
	@Query(value = "select case when (count(o.org_name) > 0)  then true else false end from Organization o where o.org_name = :org_name")
	public Boolean validationForOrgName(String org_name);
	
	@Query(value = "select case when (count(o.org_type) > 0)  then true else false end from Organization o where o.org_type = :org_type")
	public Boolean validationForOrgType(String org_type);

	
}
