package com.au.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CreditSystem;

@Transactional
@Repository
public interface CreditSystemRepository extends JpaRepository<CreditSystem, Integer> {

	@Query(value = "select cs from CreditSystem cs where cs.active=true")
	public List<CreditSystem> findAll1();

	@Query(value = "select new map(cs.credit_system_id as id,cs.grade as grade,cs.min_marks as min_marks,"
			+ "cs.max_marks as max_marks,cs.grade_points as grade_points,cs.performance as performance,"
			+ "cs.created_date as created_date,cs.created_username as created_username,cs.active as active) from CreditSystem cs "
			+ "Where CONCAT(IfNull(cs.credit_system_id,''),'',IfNull(cs.grade,''),'',IfNull(cs.grade_points,''),'',IfNull(cs.performance,''),'',IfNull(cs.created_date,''),'',IfNull(cs.created_username,'')) LIKE %?1%")
	public Page<Object> fetchAllDetails1(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(cs.credit_system_id as id,cs.grade as grade,cs.min_marks as min_marks,"
			+ "cs.max_marks as max_marks,cs.grade_points as grade_points,cs.performance as performance,"
			+ "cs.created_date as created_date,cs.created_username as created_username,cs.active as active) from CreditSystem cs")
	public Page<Object> fetchAllDetails2(Pageable pageable);

	@Modifying
	@Query(value = "update CreditSystem cs set cs.active=false where cs.credit_system_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update CreditSystem cs set cs.active=true where cs.credit_system_id=?1")
	public void update1(Integer id);

}
