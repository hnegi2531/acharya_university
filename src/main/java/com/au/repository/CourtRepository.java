package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Court;

@Transactional
@Repository
public interface CourtRepository extends JpaRepository<Court,Integer>{
	
	@Query(value = "SELECT count(*) FROM Court ct where ct.court_name=?1 and ct.active=true")
	public Integer countOfCourtName(String court_name);
	
	@Query(value = "SELECT count(*) FROM Court ct where ct.court_short_name=?1 and ct.active=true")
	public Integer countOfCourtShortName(String court_short_name);
	
	@Query(value = "SELECT ct FROM Court ct where ct.active=true")
	public List<Court> findAll1();
	
	@Query(value = "SELECT new map(ct.court_id as id,ct.court_name as court_name,ct.court_short_name as court_short_name,"
			+ "ct.active as active,ct.created_date as created_date,ct.modified_date as modified_date) FROM Court ct "
			+ "Where CONCAT(IfNull(ct.court_id,''),'',IfNull(ct.court_name,''),'',IfNull(ct.court_short_name,''),'',IfNull(ct.created_date,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "SELECT new map(ct.court_id as id,ct.court_name as court_name,ct.court_short_name as court_short_name,"
			+ "ct.active as active,ct.created_date as created_date,ct.modified_date as modified_date) FROM Court ct")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update Court ct set ct.active=false where ct.court_id=?1")
	public void deactivate(Integer court_id);
	
	@Modifying
	@Query(value = "update Court ct set ct.active=true where ct.court_id=?1")
	public void activate(Integer court_id);

}
