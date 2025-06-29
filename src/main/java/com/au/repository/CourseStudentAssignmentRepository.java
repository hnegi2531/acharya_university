package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CourseStudentAssignment;


@Repository
@Transactional
public interface CourseStudentAssignmentRepository extends JpaRepository<CourseStudentAssignment , Integer>{

	@Query(value ="Select count(*) From CourseStudentAssignment csa Where csa.course_assignment_id=?1 and csa.student_id in ?2 and csa.active=true")
	public Integer getCountofCourseStudentAssignment(Integer course_assignment_id,List<Integer> student_id);
	
	@Query(value = "select csa from CourseStudentAssignment csa where csa.active=true")
	public List<CourseStudentAssignment> findAll1();
	
	@Query(value ="Select new map(csa.course_student_assignment_id as id,csa.course_id as course_id,csa.student_id as student_id,csa.current_year_sem  as current_year_sem ,"
			+ "csa.created_date as created_date,csa.modified_date as modified_date,csa.created_by as created_by,csa.active as active,"
			+ "c.course_name as course_name,c.duration as duration,c.course_short_name as course_short_name,c.course_code as course_code,"
			+ "ca.duration as couserAssignmentDuration,ca.lecture as lecture,ca.practical as practical,ca.see_marks as see_marks,ca.total_credit as total_credit,"
			+ "ca.tutorial as tutorial,ca.year_sem as year_sem,ca.course_assignment_coursecode as course_assignment_coursecode,ca.program_id as program_id,"
			+ "ca.program_specialization_id as program_specialization_id,ca.school_id as school_id,ca.program_assignment_id as program_assignment_id,"
			+ "csa.created_username as created_username,sd.student_name as student_name,sd.auid as auid,c.course_short_name as course_short_name) From CourseStudentAssignment csa "
			+ "left join Student_Details sd on sd.student_id=csa.student_id "
			+ "left join CourseAssignment ca on csa.course_assignment_id=ca.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id "
			+ "Where CONCAT(IfNull(csa.course_student_assignment_id,''),'',IfNull(csa.created_date,''),'',IfNull(csa.created_by,''),'',IfNull(csa.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(csa.course_student_assignment_id as id,csa.course_id as course_id,csa.student_id as student_id,csa.current_year_sem  as current_year_sem ,"
			+ "csa.created_date as created_date,csa.modified_date as modified_date,csa.created_by as created_by,csa.active as active,"
			+ "c.course_name as course_name,c.duration as duration,c.course_short_name as course_short_name,c.course_code as course_code,"
			+ "ca.duration as couserAssignmentDuration,ca.lecture as lecture,ca.practical as practical,ca.see_marks as see_marks,ca.total_credit as total_credit,"
			+ "ca.tutorial as tutorial,ca.year_sem as year_sem,ca.course_assignment_coursecode as course_assignment_coursecode,ca.program_id as program_id,"
			+ "ca.program_specialization_id as program_specialization_id,ca.school_id as school_id,ca.program_assignment_id as program_assignment_id,"
			+ "csa.created_username as created_username,sd.student_name as student_name,sd.auid as auid,c.course_short_name as course_short_name) From CourseStudentAssignment csa "
			+ "left join Student_Details sd on sd.student_id=csa.student_id "
			+ "left join CourseAssignment ca on csa.course_assignment_id=ca.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update CourseStudentAssignment csa set csa.active=false where csa.course_student_assignment_id in ?1")
	public void updateDeactivate(List<Integer> ids);

	@Modifying
	@Query(value = "update CourseStudentAssignment csa set csa.active=true where csa.course_student_assignment_id=?1")
	public void updatActivate(Integer id);
	
	@Query(value = "select csa.student_id from course_student_assignment csa where csa.course_assignment_id=?1 and csa.active=true",nativeQuery=true)
	public List<Integer> assignedCourseStudentId(Integer course_id);
	
	@Query(value = "SELECT new map(s.student_id as student_id,s.auid as auid,s.usn as usn,s.student_name as student_name,s.active as active) FROM CourseStudentAssignment csa "
			+ "Left join Student_Details s on csa.student_id=s.student_id "
			+ "where s.student_id in ?1")
	public List<HashMap<String, Object>> getAssignedCourseStudentDetails(List<Integer> student_ids);
	
	@Query(value = "SELECT new map(s.student_id as student_id,s.auid as auid,s.usn as usn,s.student_name as student_name,s.active as active,"
			+ "rs.current_year As current_year,rs.current_sem As current_sem,rs.reporting_date As reporting_date,rs.eligible_reported_status As eligible_reported_status) "
			+ "FROM Student_Details s "
			+ "left join ReportingStudents rs on rs.student_id=s.student_id "
			+ "where s.student_id not in ?1 and (?2 is null Or s.ac_year_id=?2) and s.program_specialization_id =?3 and rs.current_sem=?4 and s.active=true ")
	public List<HashMap<String, Object>> getUnassignedCourseStudentDetailsOnSem(List<Integer> student_ids ,Integer ac_year_id,Integer program_specialization_id,Integer current_sem);
	
	@Query(value = "SELECT new map(s.student_id as student_id,s.auid as auid,s.usn as usn,s.student_name as student_name,s.active as active,"
			+ "rs.current_year As current_year,rs.current_sem As current_sem,rs.reporting_date As reporting_date,rs.eligible_reported_status As eligible_reported_status) "
			+ "FROM Student_Details s "
			+ "left join ReportingStudents rs on rs.student_id=s.student_id "
			+ "where (?1 is null Or s.ac_year_id=?1) and s.program_specialization_id =?2 and rs.current_sem=?3 and s.active=true ")
	public List<HashMap<String, Object>> getUnassignedCourseStudentDetailsOnSem(Integer ac_year_id,Integer program_specialization_id,Integer current_sem);
	
	@Query(value = "SELECT new map(s.student_id as student_id,s.auid as auid,s.usn as usn,s.student_name as student_name,s.active as active,"
			+ "rs.current_year As current_year,rs.current_sem As current_sem,rs.reporting_date As reporting_date,rs.eligible_reported_status As eligible_reported_status) "
			+ "FROM Student_Details s "
			+ "left join ReportingStudents rs on rs.student_id=s.student_id "
			+ "where s.student_id not in ?1 and (?2 is null Or s.ac_year_id=?2) and s.program_specialization_id =?3 and rs.current_year=?4 and s.active=true ")
	public List<HashMap<String, Object>> getUnassignedCourseStudentDetailsOnYear(List<Integer> student_ids ,Integer ac_year_id,Integer program_specialization_id,Integer current_year);
	
	@Query(value = "SELECT new map(s.student_id as student_id,s.auid as auid,s.usn as usn,s.student_name as student_name,s.active as active,"
			+ "rs.current_year As current_year,rs.current_sem As current_sem,rs.reporting_date As reporting_date,rs.eligible_reported_status As eligible_reported_status) "
			+ "FROM Student_Details s "
			+ "left join ReportingStudents rs on rs.student_id=s.student_id "
			+ "where (?1 is null Or s.ac_year_id=?1) and s.program_specialization_id =?2 and rs.current_year=?3 and s.active=true ")
	public List<HashMap<String, Object>> getUnassignedCourseStudentDetailsOnYear(Integer ac_year_id,Integer program_specialization_id,Integer current_year);
	
	@Query(value ="Select new map(csa.course_student_assignment_id as course_student_assignment_id,csa.student_id as student_id) From CourseStudentAssignment csa Where csa.student_id in ?1 and csa.active=true")
	public List<HashMap<String,Object>> courseStudentAssignmentIdsOnStudentIds(List<Integer> student_ids);
	
	@Query(value ="Select new map(c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,ca.year_sem as year_sem,cc.course_category_name as course_category_name,cc.type as type,"
			+ "cc.course_category_code as course_category_code,ca.total_credit as total_credit,ca.course_assignment_id as course_assignment_id) From CourseStudentAssignment csa "
			+ "Inner join CourseAssignment ca on ca.course_assignment_id=csa.course_assignment_id "
			+ "Left Join CourseCategory cc on ca.course_category_id=cc.course_category_id "
			+ "left join Course c on c.course_id=ca.course_id Where csa.student_id=?1 and csa.active=true")
	public List<HashMap<String,Object>> getOtherCourseOFStudent(Integer student_id);
	 

	@Query(value = "Select new map(sd.student_id as student_id,sd.student_name as student_name,sd.auid as auid,sd.usn as usn) from Student_Details sd "
			+ "where sd.student_id IN (select csa.student_id from CourseStudentAssignment csa where csa.course_id=?1 )")
	public  List<HashMap<String,Object>> getStudentData(Integer course_id);
	
	@Query(value = "Select new map(sd.student_id as student_id,sd.student_name as student_name,sd.auid as auid,sd.usn as usn) from Student_Details sd "
			+ "where sd.student_id IN ?1 and sd.active=true")
	public  List<HashMap<String,Object>> getStudentData1(List<Integer> student_ids);
	
	@Query(value = "select csa.student_id from CourseStudentAssignment csa where csa.course_id=?1 and csa.active=true")
	public List<Integer> getStudentIds(Integer course_id);
	
	@Query(value = "Select new map(sd.student_id as student_id,sd.student_name as student_name,sd.auid as auid,sd.usn as usn) from Student_Details sd "
			+ "where sd.student_id IN (select sdd.student_id from Student_Details sdd where sdd.school_id=?1 and sdd.program_specialization_id=?2 and sdd.ac_year_id=?3)")
	public List<HashMap<String,Object>> getAllStudentData(Integer school_id,Integer program_specialization_id,Integer ac_year_id);
	
	@Query(value = "select c.course_name,c.course_code,cc.course_category_code,ca.course_id,ca.course_assignment_id,"
			+ "ca.total_credit from course_student_assignment csa "
			+ "Inner join student_details sd On sd.student_id=csa.student_id "
			+ "Inner Join course_assignment ca On (sd.ac_year_id=ca.ac_year_id And sd.program_specialization_id=ca.program_specialization_id And csa.course_id=ca.course_id) "
			+ "left join course_category cc on cc.course_category_id=ca.course_category_id "
			+ "left join course c on c.course_id=csa.course_id where csa.student_id=?1 And csa.current_year_sem=?2 And csa.active=true",nativeQuery=true)
	List<Map<String, Object>> fetchAssignedCoursesOfStudent(Integer student_id,Integer current_year_sem);
	
	@Query(value = "Select new map(sd.student_id as student_id,sd.student_name as student_name,sd.auid as auid,sd.usn as usn) from Student_Details sd "
			+ "where sd.student_id IN (select csa.student_id from CourseStudentAssignment csa where csa.course_assignment_id=?1 )")
	public  List<HashMap<String,Object>> getStudentData1Data(Integer course_assignment_id);

		@Query(value = "select csa.course_assignment_id from CourseStudentAssignment csa where csa.student_id=?1 and csa.active=true")
	public List<Integer> getcoursesAssignedToStudent(Integer student_id);
	
	@Query(value ="Select count(*) From CourseStudentAssignment csa Where csa.course_assignment_id in (?1) and csa.student_id=?2 and csa.active=true")
	public Integer getCountofCourseStudentAssignment1(List<Integer> course_assignment_ids,Integer stud_id);
	
	
	@Query(value = "select csa.student_id from CourseStudentAssignment csa where csa.course_assignment_id=?1 and csa.active=true")
	public List<Integer> getStudentIdz(Integer course_assignment_id);
	
	@Query(value = "select c.course_name as subject_name,c.course_code as subject_code,ca.course_id as subject_id,"
			+ "ca.course_assignment_id as course_assignment_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "csa.current_year_sem as current_year,c.course_short_name as subject_name_short from course_student_assignment csa "
			+ "Inner Join course_assignment ca On csa.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id where csa.student_id=:studentId And ca.year_sem=:currentYearSem And csa.active=true",nativeQuery=true)
	List<Map<String, Object>> subjectAssignedToStudent(Integer studentId,Integer currentYearSem);
	
	

}
