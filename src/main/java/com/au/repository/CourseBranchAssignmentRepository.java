package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.CourseBranchAssignment;

@Repository
public interface CourseBranchAssignmentRepository extends JpaRepository<CourseBranchAssignment, Integer>{
	
	@Query(value = "Select new map(cba.course_branch_assignment_id as id,cba.course_branch_id as course_branch_id,"
			+ "cba.course_id as course_id,cba.dept_id as dept_id,cba.school_id as school_id,cba.auid_format as auid_format,"
			+ "cba.created_date as created_date,cba.modified_date as modified_date,cba.created_by as created_by,"
			+ "cba.modified_by as modified_by,cba.active as active,co.course_short_name as course_short_name,"
			+ "cb.course_branch_short_name as course_branch_short_name,d.dept_name_short as dept_name_short,"
			+ "sch.school_name_short as school_name_short) From CourseBranchAssignment cba "
			+ "Left join CourseBranch cb on cba.course_branch_id=cb.course_branch_id "
			+ "Left join Course co on cba.course_id=co.course_id "
			+ "Left join Department d on cba.dept_id=d.dept_id "
			+ "Left join Schools sch on cba.school_id=sch.school_id "
			+ "Where CONCAT(IfNull(cba.course_branch_assignment_id,''),'',IfNull(cba.auid_format,''),'',IfNull(cba.created_date,''),'',"
			+ "IfNull(cba.created_by,''),'',IfNull(co.course_short_name,''),'',IfNull(cb.course_branch_short_name,''),'',"
			+ "IfNull(d.dept_name_short,''),'',IfNull(sch.school_name_short,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(cba.course_branch_assignment_id as id,cba.course_branch_id as course_branch_id,"
			+ "cba.course_id as course_id,cba.dept_id as dept_id,cba.school_id as school_id,cba.auid_format as auid_format,"
			+ "cba.created_date as created_date,cba.modified_date as modified_date,cba.created_by as created_by,"
			+ "cba.modified_by as modified_by,cba.active as active,co.course_short_name as course_short_name,"
			+ "cb.course_branch_short_name as course_branch_short_name,d.dept_name_short as dept_name_short,"
			+ "sch.school_name_short as school_name_short) From CourseBranchAssignment cba "
			+ "Left join CourseBranch cb on cba.course_branch_id=cb.course_branch_id "
			+ "Left join Course co on cba.course_id=co.course_id "
			+ "Left join Department d on cba.dept_id=d.dept_id "
			+ "Left join Schools sch on cba.school_id=sch.school_id")
	public Page<Object> findAll2(Pageable pageable);

}
