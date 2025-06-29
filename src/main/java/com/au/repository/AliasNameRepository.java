package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.au.model.AliasName;

@Repository
public interface AliasNameRepository extends JpaRepository<AliasName, Integer> {

	
	@Query(value = "SELECT * FROM alias_name where active=true",nativeQuery =  true)
	public	List<AliasName>  findAll();
	
	
	
	@Query(value = "SELECT alias_name FROM alias_name  where active=true",nativeQuery = true)
	public List<String> getAliasNames();
	
	@Query(value = "Select new map(an.alias_id as id,an.alias_name as alias_name ,an.created_username as created_username,"
			+ "an.created_date as created_date,an.active as active) From AliasName an "
			+ "Where CONCAT(IfNull(an.alias_id,''),'',IfNull(an.alias_name,''),'',IfNull(an.created_username,''),'',IfNull(an.created_date,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(an.alias_id as id,an.alias_name as alias_name ,an.created_username as created_username,"
			+ "an.created_date as created_date,an.active as active) From AliasName an ")
	public Page<Object> findAll2(Pageable pageable);
	
	@Query(value = "SELECT an FROM AliasName an where an.alias_name=?1 and an.active=true")
	public AliasName getAliasDetailsByAliasNames(String alias_name);
	
}
