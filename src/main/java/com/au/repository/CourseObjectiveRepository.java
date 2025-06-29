package com.au.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CourseObjective;

@Transactional
@Repository
public interface CourseObjectiveRepository  extends JpaRepository<CourseObjective, Integer>{

	
	@Query(value = "select new map(co.course_objective_id as id,c.course_name as course_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "c.course_id as course_id,co.course_objective as course_objective,c.course_name as course_name,c.course_code as course_code,"
			+ "co.created_username as created_username,co.modified_username as modified_username,c.course_short_name as course_short_name,"
			+ "co.course_assignment_id as course_assignment_id,"
			+ "co.created_date as created_date,co.modified_date as modified_date,co.created_by as created_by,co.pre_requisite as pre_requisite,"
			+ "co.modified_by as modified_by,co.active as active) from CourseObjective co "
			+ "left join CourseAssignment ca on ca.course_assignment_id=co.course_assignment_id "
			+ "left join Course c on ca.course_id=c.course_id "
			+ "where CONCAT(IfNull(co.created_username,''),'',IfNull(co.course_objective,''),"
			+ "'',IfNull(co.created_by,''),'',IfNull(co.created_date,'',IfNull(c.course_id,''),'')) LIKE %?1% group by co.course_assignment_id")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(co.course_objective_id as id,c.course_name as course_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "c.course_id as course_id,co.course_objective as course_objective,c.course_name as course_name,c.course_code as course_code,"
			+ "co.course_assignment_id as course_assignment_id,"
			+ "co.created_username as created_username,co.modified_username as modified_username,c.course_short_name as course_short_name,"
			+ "co.created_date as created_date,co.modified_date as modified_date,co.created_by as created_by,co.pre_requisite as pre_requisite,"
			+ "co.modified_by as modified_by,co.active as active) from CourseObjective co "
			+ "left join CourseAssignment ca on ca.course_assignment_id=co.course_assignment_id "
			+ "left join Course c on ca.course_id=c.course_id group by co.course_assignment_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	

	@Query(value = "SELECT co from CourseObjective co where co.active=true")
	public List<CourseObjective> findAll11();
	
	
	@Modifying
	@Query(value = "update CourseObjective co set co.active=false where co.course_objective_id=?1")
	public void updateCourseObjective(Integer id);

	@Modifying
	@Query(value = "update CourseObjective co set co.active=true where co.course_objective_id=?1")
	public void updateCourseObjective1(Integer id);
	

	@Query(value = "select co.course_objective_id as course_objective_id,coc.course_outcome_id as course_outcome_id,c.course_name as course_name,"
			+ "co.course_assignment_id as course_assignment_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "co.pre_requisite as pre_requisite,co.course_objective as course_objective,c.course_id as course_id,"
			+ "coc.course_outcome_code as course_outcome_code,coc.course_outcome_objective as course_outcome_objective,"
			+ "ca.duration as duration,ca.lecture as lecture,ca.see_marks as see_marks,ca.total_credit as total_credit,"
			+ "ca.tutorial as tutorial from CourseObjective co "
			+ "left join CourseAssignment ca on ca.course_assignment_id=co.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id "
			+ "left join CourseOutcome coc on coc.course_assignment_id=co.course_assignment_id "
			+ "where co.course_assignment_id=?1 and co.active=true group by co.course_objective_id")
	public List<Map<String,Object>> getCourseObjectiveAndOutcome(Integer course_id);


	@Query(value = "select new map(co.course_objective_id as id,c.course_name as course_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "c.course_id as course_id,co.course_objective as course_objective,c.course_name as course_name,c.course_code as course_code,"
			+ "co.course_assignment_id as course_assignment_id,"
			+ "co.created_username as created_username,co.modified_username as modified_username,c.course_short_name as course_short_name,"
			+ "co.created_date as created_date,co.modified_date as modified_date,co.created_by as created_by,co.pre_requisite as pre_requisite,"
			+ "co.modified_by as modified_by,co.active as active) from CourseObjective co "
			+ "left join CourseAssignment ca on ca.course_assignment_id=co.course_assignment_id "
			+ "left join Course c on ca.course_id=c.course_id where co.course_assignment_id=?1 And co.active=true")
	public List<Map<String, Object>> getCourseObjectiveDetails(Integer course_assignment_id);
}
