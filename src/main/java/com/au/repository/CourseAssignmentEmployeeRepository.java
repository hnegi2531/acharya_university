package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CourseAssignmentEmployee;

@Repository
@Transactional
public interface CourseAssignmentEmployeeRepository extends JpaRepository<CourseAssignmentEmployee, Integer> {
	
	
	@Query(value="SELECT COUNT(*) FROM course_assignment_employee cae where cae.user_id=?1 And cae.course_id=?2 And cae.active=true",nativeQuery = true)
	public Integer getCountOfCourseEmployee(Integer user_id, Integer course_id);

	@Query(value = "select cae from CourseAssignmentEmployee cae where cae.active=true")
	public List<CourseAssignmentEmployee> getAllActiveCourseAssignmentEmployee();

	@Modifying
	@Query(value = "update CourseAssignmentEmployee c set c.active=false where c.course_assignment_employee_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update CourseAssignmentEmployee c set c.active=true where c.course_assignment_employee_id=?1")
	public void activate(Integer id);

	
	@Query(value ="Select new map(cae.course_assignment_employee_id As id,cae.user_id As user_id,cae.remark As remark,"
			+ "ua.username As username,ua.email As email,ua.usertype As usertype,ca.course_assignment_id As course_assignment_id,"
			+ "cae.course_id as course_id,c.course_name as course_name,c.course_code as course_code,c.duration as duration,"
			+ "c.course_short_name as course_short_name,cae.created_date as created_date,cae.modified_date as modified_date,"
			+ "cae.created_by as created_by,cae.modified_by as modified_by,cae.active as active,cae.created_username as created_username,"
			+ "cae.modified_username as modified_username ) From CourseAssignmentEmployee cae "
			+ "left join Course c on cae.course_id = c.course_id "
			+ "left join CourseAssignment ca on ca.course_id = c.course_id "
			+ "left join UserAuthentication ua on ua.id = cae.user_id "
			+ "Where cae.user_id=?2 And CONCAT(IfNull(cae.course_id,''),'',IfNull(c.course_name,''),'',IfNull(c.course_code,''),'',IfNull(c.course_short_name,''),'',"
			+ "IfNull(cae.created_date,''),'',IfNull(cae.created_by,''),'',IfNull(cae.created_username,'')) LIKE %?1% Group by cae.course_assignment_employee_id")
	public Page<Object> findAll1(Pageable pageable, Object keyword, Integer user_id);
	
	@Query(value ="Select new map(cae.course_assignment_employee_id As id,cae.user_id As user_id,cae.remark As remark,"
			+ "ua.username As username,ua.email As email,ua.usertype As usertype,ca.course_assignment_id As course_assignment_id,"
			+ "cae.course_id as course_id,c.course_name as course_name,c.course_code as course_code,c.duration as duration,"
			+ "c.course_short_name as course_short_name,cae.created_date as created_date,cae.modified_date as modified_date,"
			+ "cae.created_by as created_by,cae.modified_by as modified_by,cae.active as active,cae.created_username as created_username,"
			+ "cae.modified_username as modified_username ) From CourseAssignmentEmployee cae "
			+ "left join Course c on cae.course_id = c.course_id "
			+ "left join CourseAssignment ca on ca.course_id = c.course_id "
			+ "left join UserAuthentication ua on ua.id = cae.user_id "
			+ "where cae.user_id=?1 Group by cae.course_assignment_employee_id")
	public Page<Object> findAll2(Pageable pageable, Integer user_id);

	@Query(value ="Select new map(cae.course_assignment_employee_id As id,cae.user_id As user_id,cae.remark As remark,"
			+ "ua.username As username,ua.email As email,ua.usertype As usertype,"
			+ "cae.course_id as course_id,c.course_name as course_name,c.course_code as course_code,c.duration as duration,"
			+ "c.course_short_name as course_short_name,cae.created_date as created_date,cae.modified_date as modified_date,"
			+ "cae.created_by as created_by,cae.modified_by as modified_by,cae.active as active,cae.created_username as created_username,"
			+ "cae.modified_username as modified_username ) From CourseAssignmentEmployee cae "
			+ "left join Course c on cae.course_id = c.course_id "
			+ "left join UserAuthentication ua on ua.id = cae.user_id where cae.user_id=?1 And cae.active=true")
	public List<Map<String, Object>> getCourseAssignmentEmployeeBasedOnUserId(Integer user_id);
	


	@Query(value ="Select COUNT(cae.course_id) as count,cae.user_id As user_id,ua.username As username,ua.email As email,ua.usertype As usertype "
			+ "From course_assignment_employee cae "
			+ "left join user_details ua on ua.id = cae.user_id "
			+ "where cae.user_id=?1 And cae.active=true",nativeQuery=true)
	public Map<String, Object> getCountOfCourseBasedOnUserId(Integer user_id);

	@Query(value ="Select COUNT(cae.course_id) as Coursecount,cae.user_id As user_id,ua.username As username "
			+ "From course_assignment_employee cae "
			+ "left join user_details ua on ua.id = cae.user_id "
			+ "left join employee_details ed on ua.email = ed.email "
			+ "where ed.emp_id=?1 And cae.active=true",nativeQuery=true)
	public Map<String, Object> getCountOfCourseBasedOnEmpId(Integer emp_id);
	
}
