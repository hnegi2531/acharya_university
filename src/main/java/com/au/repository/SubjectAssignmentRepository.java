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

import com.au.model.SubjectAssignment;

@Repository
@Transactional
public interface SubjectAssignmentRepository extends JpaRepository<SubjectAssignment, Integer>{

	@Query(value = "SELECT * FROM subject_workload_creation where subject_type=true",nativeQuery = true)
	public List<Map<String,Object>> fetchSubWorkLoadBySubtype();

	@Query(value = "SELECT * FROM subject_workload_creation where work_load_type=true",nativeQuery = true)
	public List<Map<String,Object>> fetchSubWorkLoadByWorkloadtype();

	@Modifying
	@Query(value = "update SubjectAssignment r set r.active=false where r.subjetAssignId=?1")
	public void updateAcademicWorkLoad(Integer id);

	@Modifying
	@Query(value = "update SubjectAssignment r set r.active=true where r.subjetAssignId=?1")
	public void updateAcademicWorkLoad1(Integer id);

	/*  @Query(value="select  c.course_short_name,course_name,sa.course_id,st.subject_type_name,sa.subject_type_id,sa.school_id,s.school_name,sa.program_id,p.program_name ,sa.program_specialization_id,\r\n"
			+ "ps.program_specialization_name,ps.program_specialization_short_name,p.program_short_name from \r\n"
			+ "subject_assignment sa inner join schools s on sa.school_id=s.school_id\r\n"
			+ "inner join program p on sa.program_id=p.program_id \r\n"
			+ "inner join program_specialization ps on \r\n"
			+ "sa.program_specialization_id = ps.program_specialization_id \r\n"
			+ "left join subject_type st on sa.subject_type_id = st.subject_type_id \r\n"
			+ "inner join Course c on sa.course_id=c.course_id",nativeQuery = true) */
	@Query(value = "SELECT * FROM subject_assignment where active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchAllDetails();

	//@Query(value = "SELECT count(program_id) FROM subject_assignment where program_specialization_id like ?1%",nativeQuery = true)
			  // or program_specialization_id like '%,id2,%' or program_specialization_id like '%?id3,'",nativeQuery = true)
//	public Integer countByProgramSpecializationIdLike(String programSpecializationId);
	
//	@Query(value = "SELECT  sa.active,sa.created_username,sa.created_by,sa.created_date,st.subject_type_name,sa.subject_type_id,"
//			+ "sa.program_specialization_short_name,sa.program_specialization_id,ps.program_specialization_name,sa.program_id,"
//			+ "pr.program_name,pr.program_short_name,sa.course_id,c.courset_name FROM subject_assignment sa "
//			+ "inner join Course c on sa.course_id=c.course_id inner join program pr on "
//			+ "sa.program_id=pr.program_id inner join program_specialization ps "
//			+ "on sa.program_specialization_id=ps.program_specialization_id inner join "
//			+ "subject_type st on sa.subject_type_id=st.subject_type_id",nativeQuery = true)
//	public List<Map<String, Object>> getSubjectAssignIndex();
	
	
	@Query(value = "SELECT * FROM subject_assignment",nativeQuery = true)
	public List<Map<String, Object>> getSubjectAssignIndex();

	
	@Query(value = "SELECT sa.created_date,sa.created_username,sa.created_by,sa.active,c.course_name,ed.employee_name,sa.course_id,sa.subjet_assign_id FROM subject_assignment sa "
			+ "left join course c on "
			+ " sa.course_id=c.course_id left join employee_details ed on sa.user_id=ed.emp_id",nativeQuery = true)
	public List<Map<String, Object>> subjectAssignIndexDetails1();
	
	@Query(value = "select new map(sa.subjetAssignId as id,sa.user_id as user_id,ca.course_assignment_id as course_assignment_id,"
			+ "sa.course_id as course_id,sa.remarks as remarks,sa.createdBy as createdBy,sa.modifiedBy as modifiedBy,"
			+ "sa.createdDate as createdDate,sa.modifiedDate as modifiedDate,sa.active as active,ua.username as username,"
			+ "c.course_name as course_name,c.duration as duration,c.course_short_name as course_short_name,c.course_code as course_code,"
			+ "ca.duration as couserAssignmentDuration,ca.lecture as lecture,ca.practical as practical,ca.see_marks as see_marks,ca.total_credit as total_credit,"
			+ "ca.tutorial as tutorial,ca.year_sem as year_sem,ca.course_assignment_coursecode as course_assignment_coursecode,ca.program_id as program_id,"
			+ "ca.program_specialization_id as program_specialization_id,ca.school_id as school_id,ca.program_assignment_id as program_assignment_id,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,p.display_name as display_name,p.program_code as program_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ps.auid_format as auid_format,ed.emp_id as emp_id,ed.empcode as empcode,ed.emp_id as emp_id,ed.employee_name as employee_name,"
			+ "ay.ac_year as ac_year,ay.ac_year_code as ac_year_code,ay.current_year as current_year,ed.dept_id As dept_id,"
			+ "sa.createdUsername as createdUsername,sa.modifiedUsername as modifiedUsername ) from SubjectAssignment sa "
			+ "left join CourseAssignment ca on sa.course_assignment_id=ca.course_assignment_id "
			+ "left join Course c on ca.course_id=c.course_id "
			+ "left join Program p on ca.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on ca.program_specialization_id=ps.program_specialization_id "
			+ "left join Academic_year ay on ca.ac_year_id=ay.ac_year_id "
			+ "left join UserAuthentication ua on sa.user_id=ua.id "
			+ "left join EmployeeDetails ed on ed.email=ua.email "
			+ "where (:school_id IS NULL OR ed.school_id = :school_id) "
			+ "AND (:dept_id IS NULL OR ed.dept_id = :dept_id) " 
			+ "AND (:userId IS NULL OR sa.user_id = :userId) "
			+ "And CONCAT(IfNull(sa.subjetAssignId,''),'',IfNull(sa.course_id,''),'',IfNull(sa.user_id,''),"
			+ "'',IfNull(sa.remarks,''),'',IfNull(sa.createdBy,''),'',IfNull(sa.createdDate,'')) LIKE %:keyword% ")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer school_id, Integer dept_id, Integer userId); 
	
	@Query(value = "select new map(sa.subjetAssignId as id,sa.user_id as user_id,ca.course_assignment_id as course_assignment_id,"
			+ "sa.course_id as course_id,sa.remarks as remarks,sa.createdBy as createdBy,sa.modifiedBy as modifiedBy,"
			+ "sa.createdDate as createdDate,sa.modifiedDate as modifiedDate,sa.active as active,ua.username as username,"
			+ "c.course_name as course_name,c.duration as duration,c.course_short_name as course_short_name,c.course_code as course_code,"
			+ "ca.duration as couserAssignmentDuration,ca.lecture as lecture,ca.practical as practical,ca.see_marks as see_marks,ca.total_credit as total_credit,"
			+ "ca.tutorial as tutorial,ca.year_sem as year_sem,ca.course_assignment_coursecode as course_assignment_coursecode,ca.program_id as program_id,"
			+ "ca.program_specialization_id as program_specialization_id,ca.school_id as school_id,ca.program_assignment_id as program_assignment_id,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,p.display_name as display_name,p.program_code as program_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ps.auid_format as auid_format,ed.emp_id as emp_id,ed.empcode as empcode,ed.emp_id as emp_id,ed.employee_name as employee_name,"
			+ "ay.ac_year as ac_year,ay.ac_year_code as ac_year_code,ay.current_year as current_year,ed.dept_id As dept_id,"
			+ "sa.createdUsername as createdUsername,sa.modifiedUsername as modifiedUsername ) from SubjectAssignment sa "
			+ "left join CourseAssignment ca on sa.course_assignment_id=ca.course_assignment_id "
			+ "left join Course c on ca.course_id=c.course_id "
			+ "left join Program p on ca.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on ca.program_specialization_id=ps.program_specialization_id "
			+ "left join Academic_year ay on ca.ac_year_id=ay.ac_year_id "
			+ "left join UserAuthentication ua on sa.user_id=ua.id "
			+ "left join EmployeeDetails ed on ed.email=ua.email "
			+  "WHERE (:school_id IS NULL OR ed.school_id = :school_id) " 
            + "AND (:dept_id IS NULL OR ed.dept_id = :dept_id) " 
            + "AND (:userId IS NULL OR sa.user_id = :userId) ")
	public Page<Object> getAllSortedData(Pageable pageable, Integer school_id, Integer dept_id, Integer userId);
	
	@Query(value = "Select c.course_id as course_id,ca.course_assignment_id as course_assignment_id,c.course_code as course_code,"
			+ "c.course_name as course_name,c.course_short_name as course_short_name,c.duration as duration,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(c.course_code,'')) as course_name_with_code From course c "
			+ "left join course_assignment ca on ca.course_id=c.course_id "
			+ "where ca.course_assignment_id  not in (Select DISTINCT sa1.course_assignment_id From subject_assignment sa1 where sa1.user_id=1 and sa1.active= true)",nativeQuery = true)
	public List<Map<String, Object>> subjectUnassignedDetails(Integer user_id);
	
	@Query(value = "SELECT count(*) FROM SubjectAssignment sa where sa.subjetAssignId !=?1 and sa.course_id =?2 and sa.user_id =?3 and sa.active=true")
	public Integer countOfSctionAssignmentOnSubjectAndUserForUpdate(Integer subjetAssignId,Integer course_id,Integer user_id);
	
	@Query(value = "Select csa.subjet_assign_id as subjet_assign_id,ca.course_assignment_id as course_assignment_id,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-',"
			+ "IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course_name_with_code "
			+ "From subject_assignment csa "
			+ "left join course_assignment ca on ca.course_assignment_id=csa.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join program_specialization ps on ps.program_specialization_id=ca.program_specialization_id "
			+ "where csa.user_id=?1 and csa.active=true",nativeQuery = true)
	public List<Map<String, Object>> getAssignedCoursesDetails(Integer emp_id);
	
	@Query(value = "select c.course_code as course_code,c.course_name as course_name,"
			+ "c.course_id as course_id,c.course_short_name as course_short_name from course c where c.course_id=( "
			+ "select sa.course_id from subject_assignment sa where sa.subjet_assign_id=?1 and sa.active=true)",nativeQuery = true)
	public Map<String,Object> getCourseNameBySubjectAssignmentId(Integer subjet_assign_id);
	
	@Query(value = "SELECT count(*) FROM SubjectAssignment sa where sa.course_assignment_id =?1 and sa.user_id =?2 and sa.active=true")
	public Integer countCourseAssignedToUser(Integer course_assignment_id,Integer user_id);

	@Query(value = "select new map(sa.subjetAssignId as id,sa.user_id as user_id,ca.course_assignment_id as course_assignment_id,"
			+ "sa.course_id as course_id,sa.remarks as remarks,sa.createdBy as createdBy,sa.modifiedBy as modifiedBy,"
			+ "sa.createdDate as createdDate,sa.modifiedDate as modifiedDate,sa.active as active,ua.username as username,"
			+ "c.course_name as course_name,c.duration as duration,c.course_short_name as course_short_name,c.course_code as course_code,"
			+ "ca.duration as couserAssignmentDuration,ca.lecture as lecture,ca.practical as practical,ca.see_marks as see_marks,ca.total_credit as total_credit,"
			+ "ca.tutorial as tutorial,ca.year_sem as year_sem,ca.course_assignment_coursecode as course_assignment_coursecode,ca.program_id as program_id,"
			+ "ca.program_specialization_id as program_specialization_id,ca.school_id as school_id,ca.program_assignment_id as program_assignment_id,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,p.display_name as display_name,p.program_code as program_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ps.auid_format as auid_format,ed.emp_id as emp_id,ed.empcode as empcode,ed.emp_id as emp_id,ed.employee_name as employee_name,"
			+ "ay.ac_year as ac_year,ay.ac_year_code as ac_year_code,ay.current_year as current_year,pt.program_type_id as program_type_id,pt.program_type_name as program_type_name,"
			+ "sa.createdUsername as createdUsername,sa.modifiedUsername as modifiedUsername ) from SubjectAssignment sa "
			+ "left join CourseAssignment ca on sa.course_assignment_id=ca.course_assignment_id "
			+ "left join Course c on ca.course_id=c.course_id "
			+ "left join Program p on ca.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on ca.program_specialization_id=ps.program_specialization_id "
			+ "left join Academic_year ay on ca.ac_year_id=ay.ac_year_id "
			+ "left join ProgramAssigment pa on ca.program_assignment_id=pa.program_assignment_id "
			+ "left join ProgramType pt on pa.program_type_id=pt.program_type_id "
			+ "left join UserAuthentication ua on sa.user_id=ua.id "
			+ "left join EmployeeDetails ed on ed.email=ua.email where sa.user_id=?1 And sa.active=true")
	public List<Map<String, Object>> getSubjectAssignmentDetailsData(Integer user_id);
	
	
	
	@Query(value = "select new map(sa.subjetAssignId as id,sa.user_id as user_id,ca.course_assignment_id as course_assignment_id,"
			+ "sa.course_id as course_id,sa.remarks as remarks,sa.createdBy as createdBy,sa.modifiedBy as modifiedBy,"
			+ "sa.createdDate as createdDate,sa.modifiedDate as modifiedDate,sa.active as active,ua.username as username,"
			+ "sc.school_id As school_id,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "ed1.empcode as createdbyEmpcode,ed1.employee_name as createdbyEmployeeName,"
			+ "c.course_name as course_name,c.duration as duration,c.course_short_name as course_short_name,c.course_code as course_code,"
			+ "ca.duration as couserAssignmentDuration,ca.lecture as lecture,ca.practical as practical,ca.see_marks as see_marks,ca.total_credit as total_credit,"
			+ "ca.tutorial as tutorial,ca.year_sem as year_sem,ca.course_assignment_coursecode as course_assignment_coursecode,ca.program_id as program_id,"
			+ "ca.program_specialization_id as program_specialization_id,ca.program_assignment_id as program_assignment_id,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,p.display_name as display_name,p.program_code as program_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ps.auid_format as auid_format,ed.empcode as empcode,ed.emp_id as emp_id,ed.employee_name as employee_name,"
			+ "ay.ac_year as ac_year,ay.ac_year_code as ac_year_code,ay.current_year as current_year,"
			+ "sa.createdUsername as createdUsername,sa.modifiedUsername as modifiedUsername ) from SubjectAssignment sa "
			+ "left join CourseAssignment ca on sa.course_assignment_id=ca.course_assignment_id "
			+ "left join Course c on ca.course_id=c.course_id "
			+ "left join Program p on ca.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on ca.program_specialization_id=ps.program_specialization_id "
			+ "left join Academic_year ay on ca.ac_year_id=ay.ac_year_id "
			+ "left join UserAuthentication ua on sa.user_id=ua.id "
			+ "left join EmployeeDetails ed on ed.email=ua.email "
			+ "left join UserAuthentication ua1 on sa.createdBy=ua1.id "
			+ "left join EmployeeDetails ed1 on ed1.email=ua1.email "
			+ "left join Schools sc on ed1.school_id=sc.school_id "
			+ "where (:school_id is null or sc.school_id = :school_id) "
			 + "And (:createdBy is null or sa.createdBy = :createdBy) "
			 + "And (:user_id is null or sa.user_id = :user_id) "
			+ "And CONCAT(IfNull(sa.subjetAssignId,''),'',IfNull(sa.course_id,''),'',IfNull(sa.user_id,''),"
			+ "'',IfNull(sa.remarks,''),'',IfNull(sa.createdBy,''),'',IfNull(sa.createdDate,'')) LIKE %:keyword% And ed1.active=true")
	public Page<Object> fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedByByKeyword(Pageable pageable, Object keyword, Integer school_id, Integer createdBy, Integer user_id); 
	
	@Query(value = "select new map(sa.subjetAssignId as id,sa.user_id as user_id,ca.course_assignment_id as course_assignment_id,"
			+ "sa.course_id as course_id,sa.remarks as remarks,sa.createdBy as createdBy,sa.modifiedBy as modifiedBy,"
			+ "sa.createdDate as createdDate,sa.modifiedDate as modifiedDate,sa.active as active,ua.username as username,"
			+ "sc.school_id As school_id,sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "ed1.empcode as createdbyEmpcode,ed1.employee_name as createdbyEmployeeName,"
			+ "c.course_name as course_name,c.duration as duration,c.course_short_name as course_short_name,c.course_code as course_code,"
			+ "ca.duration as couserAssignmentDuration,ca.lecture as lecture,ca.practical as practical,ca.see_marks as see_marks,ca.total_credit as total_credit,"
			+ "ca.tutorial as tutorial,ca.year_sem as year_sem,ca.course_assignment_coursecode as course_assignment_coursecode,ca.program_id as program_id,"
			+ "ca.program_specialization_id as program_specialization_id,ca.program_assignment_id as program_assignment_id,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,p.display_name as display_name,p.program_code as program_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ps.auid_format as auid_format,ed.empcode as empcode,ed.emp_id as emp_id,ed.employee_name as employee_name,"
			+ "ay.ac_year as ac_year,ay.ac_year_code as ac_year_code,ay.current_year as current_year,"
			+ "sa.createdUsername as createdUsername,sa.modifiedUsername as modifiedUsername ) from SubjectAssignment sa "
			+ "left join CourseAssignment ca on sa.course_assignment_id=ca.course_assignment_id "
			+ "left join Course c on ca.course_id=c.course_id "
			+ "left join Program p on ca.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on ca.program_specialization_id=ps.program_specialization_id "
			+ "left join Academic_year ay on ca.ac_year_id=ay.ac_year_id "
			+ "left join UserAuthentication ua on sa.user_id=ua.id "
			+ "left join EmployeeDetails ed on ed.email=ua.email "
			+ "left join UserAuthentication ua1 on sa.createdBy=ua1.id "
			+ "left join EmployeeDetails ed1 on ed1.email=ua1.email "
			+ "left join Schools sc on ed1.school_id=sc.school_id "
			+ "where (:school_id is null or sc.school_id = :school_id) "
			 + "And (:user_id is null or sa.user_id = :user_id) "
			 + "And (:createdBy is null or sa.createdBy = :createdBy) And ed1.active=true")
	public Page<Object> fetchAllSubjectAssignmentBasedOnSchoolIdAndCreatedByData(Pageable pageable, Integer school_id, Integer createdBy, Integer user_id);
	
	
	@Query(value ="Select new map(cae.subjetAssignId As id,cae.user_id As user_id,cae.remarks As remarks,"
			+ "ua.username As username,ua.email As email,ua.usertype As usertype,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "cae.course_id as course_id,c.course_name as course_name,c.course_code as course_code,c.duration as duration,"
			+ "c.course_short_name as course_short_name,cae.createdDate as createdDate,cae.modifiedDate as modifiedDate,"
			+ "cae.createdBy as createdBy,cae.modifiedBy as modifiedBy,cae.active as active,cae.createdUsername as createdUsername,"
			+ "cae.modifiedUsername as modifiedUsername ) From SubjectAssignment cae "
			+ "left join CourseAssignment ca on ca.course_assignment_id = cae.course_assignment_id "
			+ "left join Course c on c.course_id = ca.course_id "			
			+ "left join UserAuthentication ua on ua.id = cae.user_id where cae.user_id=?1 and ca.year_sem=?2 And cae.active=true")
	public List<Map<String, Object>> getCourseAssignmentEmployeeBasedOnUserIdAndYearSem(Integer user_id, Integer year_sem);
	
}
