package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Syllabus;

@Transactional
@Repository
public interface Syllabus_Repository extends JpaRepository<Syllabus, Integer> {

	@Query(value = "select s from Syllabus s where s.active=true")
	public List<Syllabus> findAll1();
	
	@Query(value ="select new map(s.syllabus_id as id,s.created_date as created_date,"
			+ "ca.course_assignment_id as course_assignment_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.modified_date as modified_date,s.created_by as created_by,s.modified_by as modified_by,"
			+ "s.created_username as created_username,s.modified_username as modified_username,s.duration as duration,"
			+ "s.course_id as course_id,s.syllabus_objective as syllabus_objective,c.course_name as course_name,c.course_code as course_code,"
			+ "s.active as active,s.syllabus_code as syllabus_code,s.learning as learning,s.topic_name as topic_name ) from Syllabus s "
			+ "left join CourseAssignment ca on s.course_assignment_id=ca.course_assignment_id "
		    + "left join Course c on s.course_id=c.course_id "
			+ "Where CONCAT(IfNull(s.syllabus_id,''),'',IfNull(s.syllabus_objective,''),'',IfNull(s.course_id,''),'',"
			+ "IfNull(s.created_date,''),'',IfNull(s.created_by,''),'',IfNull(s.created_username,'')) LIKE %?1% group by s.course_assignment_id")
	public List<Map<String, Object>> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="select new map(s.syllabus_id as id,s.created_date as created_date,"
			+ "ca.course_assignment_id as course_assignment_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.modified_date as modified_date,s.created_by as created_by,s.modified_by as modified_by,"
			+ "s.created_username as created_username,s.modified_username as modified_username,s.duration as duration,"
			+ "s.course_id as course_id,s.syllabus_objective as syllabus_objective,c.course_name as course_name,c.course_code as course_code,"
			+ "s.active as active,s.syllabus_code as syllabus_code,s.learning as learning,s.topic_name as topic_name ) from Syllabus s "
		    + "left join CourseAssignment ca on s.course_assignment_id=ca.course_assignment_id "
		    + "left join Course c on c.course_id=ca.course_id group by s.course_assignment_id")
	 public List<Map<String, Object>> findAll3(Pageable pageable);

	@Modifying
	@Query(value = "update Syllabus s set s.active=false where s.syllabus_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Syllabus s set s.active=true where s.syllabus_id=?1")
	public void update1(Integer id);

	@Modifying
	@Query(value = "update Syllabus s set s.syllabus_path=?2 where s.syllabus_id=?1")
	public void updatePath(Integer syllabus_id, String t2);
	
	@Query(value = "select s from Syllabus s where s.course_assignment_id=?1 and s.active=true")
	public List<Syllabus> getSyllabusByCourseAssignmentId(Integer course_assignment_id);
	
	@Query(value = "select s from Syllabus s where s.course_id=?1 and s.active=true")
	public List<Syllabus> getSyllabusDetailsData(Integer course_id);

	
	@Query(value ="select s.syllabus_id as id,s.created_date as created_date,"
			+ "ca.course_assignment_id as course_assignment_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.modified_date as modified_date,s.created_by as created_by,s.modified_by as modified_by,"
			+ "s.created_username as created_username,s.modified_username as modified_username,s.duration as duration,"
			+ "s.course_id as course_id,s.syllabus_objective as syllabus_objective,c.course_name as course_name,c.course_code as course_code,"
			+ "s.active as active,s.syllabus_code as syllabus_code,s.learning as learning,s.topic_name as topic_name,"
			+ "concat(s.syllabus_objective,'-',c.course_name,'-',c.course_code) as syllabus_objective_concat from syllabus s "
		    + "left join course_assignment ca on s.course_assignment_id=ca.course_assignment_id "
		    + "left join course c on c.course_id=ca.course_id where s.course_assignment_id=?1 And s.active=true",nativeQuery=true)
	public List<Map<String, Object>> getSyllabusDetails(Integer course_assignment_id);
	
	@Query(value = "Select s from Syllabus s where s.course_assignment_id=?1 and s.active=true")
	public List<Syllabus> syllabusByCourseAssignment(Integer courseAsgsignmentId);
	
	@Query(value ="select s.syllabus_id as syllabus_id,s.created_date as created_date,s.syllabus_path as syllabus_path,"
			+ "ca.course_assignment_id as course_assignment_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.modified_date as modified_date,s.created_by as created_by,s.modified_by as modified_by,"
			+ "s.created_username as created_username,s.modified_username as modified_username,s.duration as duration,"
			+ "s.course_id as course_id,s.syllabus_objective as syllabus_objective,c.course_name as course_name,c.course_code as course_code,"
			+ "s.active as active,s.syllabus_code as syllabus_code,s.learning as learning,s.topic_name as topic_name,s.module as module,"
			+ "concat(s.syllabus_objective,'-',c.course_name,'-',c.course_code) as syllabus_objective_concat from syllabus s "
		    + "left join course_assignment ca on s.course_assignment_id=ca.course_assignment_id "
		    + "left join course c on c.course_id=ca.course_id where s.course_assignment_id in (?1) And s.active=true",nativeQuery=true)
	public List<Map<String, Object>> getSyllabusDetailsByIds(List<Integer> course_assignment_id);
	
}
