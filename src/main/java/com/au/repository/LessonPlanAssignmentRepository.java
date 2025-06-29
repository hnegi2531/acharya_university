package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.LessonPlanAssignment;

@Transactional
@Repository
public interface LessonPlanAssignmentRepository extends JpaRepository<LessonPlanAssignment, Integer> {
	
	@Query(value ="Select new map(lpa.lesson_assignment_id as id,lpa.lesson_id as lesson_id,lpa.plan_date as plan_date,"
			+ "lpa.contents as contents,lpa.teaching_aid as teaching_aid,lpa.created_date as created_date,modified_date as modified_date,"
			+ "lpa.created_by as created_by,lpa.modified_by as modified_by,lpa.created_username as created_username,"
			+ "lpa.ict_text as ict_text,lpa.attachment_path as attachment_path,"
			+ "lpa.modified_username as modified_username,lpa.active as active) From LessonPlanAssignment lpa "
			+ "Where CONCAT(IfNull(lpa.lesson_assignment_id,''),'',IfNull(lpa.plan_date,''),'',IfNull(lpa.contents,''),'',"
			+ "IfNull(lpa.teaching_aid,''),'',IfNull(lpa.created_date,''),'',IfNull(lpa.created_by,''),'',IfNull(lpa.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(lpa.lesson_assignment_id as id,lpa.lesson_id as lesson_id,lpa.plan_date as plan_date,"
			+ "lpa.contents as contents,lpa.teaching_aid as teaching_aid,lpa.created_date as created_date,modified_date as modified_date,"
			+ "lpa.created_by as created_by,lpa.modified_by as modified_by,lpa.created_username as created_username,"
			+ "lpa.ict_text as ict_text,lpa.attachment_path as attachment_path,"
			+ "lpa.modified_username as modified_username,lpa.active as active) From LessonPlanAssignment lpa")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "Select count(*) from lesson_plan_assignment lpa where "
			+ "lpa.plan_date=?1 and lpa.contents=?2 and lpa.teaching_aid=?3 and lpa.created_by=?4",nativeQuery=true)
	public Integer getCountOfCombination(String plan_date,String contents,String teaching_aid,Integer user_id);

}
