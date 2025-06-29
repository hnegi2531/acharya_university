package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.CourseBranch;

@Repository
public interface CourseBranchRepository extends JpaRepository<CourseBranch, Integer>{
	
	@Query(value ="Select new map(cb.course_branch_id as id,cb.course_branch_name as course_branch_name,"
			+ "cb.course_branch_short_name as course_branch_short_name,cb.created_date as created_date,"
			+ "cb.modified_date as modified_date,cb.created_by as created_by,cb.modified_by as modified_by,cb.active as active) From CourseBranch cb "
			+ "Where CONCAT(IfNull(cb.course_branch_id,''),'',IfNull(cb.course_branch_name,''),'',IfNull(cb.course_branch_short_name,''),'',IfNull(cb.created_date,''),'',IfNull(cb.created_by,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(cb.course_branch_id as id,cb.course_branch_name as course_branch_name,"
			+ "cb.course_branch_short_name as course_branch_short_name,cb.created_date as created_date,"
			+ "cb.modified_date as modified_date,cb.created_by as created_by,cb.modified_by as modified_by,cb.active as active) From CourseBranch cb")
	public Page<Object> findAll2(Pageable pageable);

}
