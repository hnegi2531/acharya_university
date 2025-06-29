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

import com.au.model.LessonPlan;
import com.au.model.Program;

@Repository
@Transactional
public interface LessonPlanRepository extends JpaRepository<LessonPlan, Integer>{

	@Modifying
	@Query(value = "update LessonPlan l set l.active=false where l.lesson_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update LessonPlan l set l.active=true where l.lesson_id=?1")
	public void update1(Integer id);
	
	@Query(value ="Select new map(lp.lesson_id as id,lp.subject_id as subject_id,"
			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,"
			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,lp.subject_assignment_id as subject_assignment_id,"
			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,lp.user_id as user_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book) From LessonPlan lp "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id "
			+ "Where CONCAT(IfNull(lp.lesson_id,''),'',IfNull(lp.subject_name_short,''),'',IfNull(lp.year_sem,''),'',"
			+ "IfNull(lp.created_date,''),'',IfNull(lp.created_by,''),'',IfNull(lp.created_date,''),'',"
			+ "IfNull(lp.created_username,''),'',IfNull(sch.school_name_short,''),'',IfNull(pr.program_short_name,''),'',"
			+ "IfNull(ss.subjectNameShort,''),'',IfNull(ac.ac_year,''),'',IfNull(ps.program_specialization_short_name,''),'',"
			+ "IfNull(sec.section_name,''),'',IfNull(rb.title_of_book,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(lp.lesson_id as id,lp.subject_id as subject_id,"
			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,"
			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,lp.subject_assignment_id as subject_assignment_id,"
			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,lp.user_id as user_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book) From LessonPlan lp "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value ="Select new map(lpa.lesson_assignment_id as id,lp.lesson_id as lesson_id,lpa.plan_date as plan_date,lpa.contents as contents,lpa.teaching_aid as teaching_aid,"
			+ "c.course_name as course_name,c.course_short_name as course_short_name,lp.subject_id as subject_id,lpa.ict_text as ict_text,lpa.attachment_name as attachment_name,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,lp.user_id as user_id,lp.subject_assignment_id as subject_assignment_id,"
			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,lpa.attachment_path as attachment_path,"
			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,"
			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "ed.employee_name as employee_name,ed.emp_id as emp_id,dept.dept_id as dept_id,dept.dept_name as dept_name,"
			+ "dept.dept_name_short as dept_name_short,c.course_code as course_code,ed.empcode as empcode,"
			+ "ca.course_assignment_id as course_assignment_id,pa.program_assignment_id as program_assignment_id,"
			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book) "
			+ "From LessonPlanAssignment lpa "
			+ "Left join LessonPlan lp on lpa.lesson_id=lp.lesson_id "
			+ "Left join ProgramAssigment pa on pa.program_assignment_id=lp.program_assignment_id "
			+ "Left join CourseAssignment ca on ca.course_assignment_id=lp.course_assignment_id "
			+ "Left join Course c on c.course_id=ca.course_id "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
			+ "Left join UserAuthentication ua on ua.id = lp.user_id "
			+ "Left join EmployeeDetails ed on ed.email = ua.email "
			+ "Left join Department dept on ed.dept_id = dept.dept_id "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id where lp.ac_year_id=?1 ")
	public List<HashMap<String, Object>> fetchLessonPlanByAcYear(Integer ac_year_id);

	@Query(value ="Select count(*) from lesson_plan lp where lp.ac_year_id=?1 and lp.program_assignment_id=?2 and lp.program_id=?3 "
			+ "and lp.program_specialization_id=?4 and lp.course_assignment_id=?5 and lp.school_id=?6 and lp.year_sem=?7 and lp.employee_id=?8 and lp.active=true",nativeQuery =true)
	public Integer checkEstingData(Integer ac_year_id,  Integer program_assignment_id, Integer program_id,  
			Integer program_specialization_id,Integer course_assignment_id, Integer school_id, Integer year_sem, Integer emp_id);
	
	
	@Query(value = "Select new map(lp.lesson_id as id,lp.subject_id as subject_id,c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,lp.user_id as user_id,lp.subject_assignment_id as subject_assignment_id,"
			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,"
			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,"
			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book,lpa.contents as contents,"
			+ "lpa.plan_date as plan_date,lpa.teaching_aid as teaching_aid,lpa.lesson_assignment_id as lesson_assignment_id,"
			+ "lp.employee_id as employee_id,lpa.ict_text as ict_text,lpa.attachment_path as attachment_path,lpa.type as type,"
			+ "lpa.teaching_mode as teaching_mode,lpa.learning_style as learning_style) "
			+ "From LessonPlan lp "
			+ "Inner join CourseAssignment ca on ca.course_assignment_id=lp.course_assignment_id "
			+ "Left join Course c on c.course_id=ca.course_id "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
			+ "Inner join LessonPlanAssignment lpa on lpa.lesson_id = lp.lesson_id "
//			+ "Left join EmployeeDetails ed on ed.emp_id = lp.created_by "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id where lp.ac_year_id=?1 And lp.program_id=?2 And "
			+ "lp.program_specialization_id=?3 And lp.course_assignment_id=?4 And lp.year_sem=?5 And lp.employee_id=?6 And lpa.lesson_assignment_id not in (?7) "
			+ "And ((?8 = true AND lpa.ict_text IS NOT NULL) OR (?8 = false)) And lp.active=true")
	public List<HashMap<String, Object>> getLessonPlanByAcademicYearProgramSpecilizationSchoolSectionCourseYearOrSem(
			Integer ac_year_id, Integer program_id, Integer program_specialization_id, Integer course_assignment_id, Integer year_sem,Integer employee_id,List<Integer> lesson_assignment_ids, Boolean ict_status);
	
	@Query(value = "select lpa.lesson_assignment_id as lesson_assignment_id,lpa.active as active,lpa.contents as contents,lpa.created_by as created_by,"
			+ "lpa.created_date as created_date,lpa.created_username as created_username,lpa.lesson_id as lesson_id,"
			+ "lpa.modified_by as modified_by,lpa.modified_date as modified_date,lpa.modified_username as modified_username,"
			+ "c.course_id As course_id,c.course_code As course_code,ua.username as username,"
			+ "lpa.plan_date as plan_date,lpa.teaching_aid as teaching_aid,lpa.type as type,lpa.teaching_mode as teaching_mode,"
			+ "lpa.learning_style as learning_style,lpa.book_id as book_id,lpa.ict_text as ict_text,lpa.attachment_path as attachment_path,"
			+ "sa.date_of_class as date_of_class, pr.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,ba.batch_name as batch_name "
			+ "from lesson_plan_assignment lpa "
			+ "left join lesson_plan lp on lp.lesson_id=lpa.lesson_id "
			+ "Left join user_details ua on ua.id = lp.user_id "
			+ "left join course_assignment ca on lp.course_assignment_id=ca.course_assignment_id "
			+ "Left join program pr on pr.program_id = ca.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id = ca.program_specialization_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join student_attendance sa on sa.lesson_assignment_id=lpa.lesson_assignment_id "
			+ "left join section sec on sa.section_id=sec.section_id "
			+ "left join batch ba on ba.batch_id=sa.batch_id "
			+ "where lpa.lesson_id=?1 and lpa.active=true group by lpa.lesson_assignment_id",nativeQuery =true)
	public List<Map<String, Object>> getLessonPlanDataByLessonId(Integer lesson_id);
	
	@Query(value = "Select new map(lp.lesson_id as id,lp.subject_id as subject_id,c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,lp.user_id as user_id,lp.subject_assignment_id as subject_assignment_id,"
			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,"
			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,"
			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book,lpa.contents as contents,"
			+ "lpa.plan_date as plan_date,lpa.teaching_aid as teaching_aid,lpa.lesson_assignment_id as lesson_assignment_id,lp.employee_id as employee_id) "
			+ "From LessonPlan lp "
			+ "Inner join CourseAssignment ca on ca.course_assignment_id=lp.course_assignment_id "
			+ "Left join Course c on c.course_id=ca.course_id "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
			+ "Inner join LessonPlanAssignment lpa on lpa.lesson_id = lp.lesson_id "
//			+ "Left join EmployeeDetails ed on ed.emp_id = lp.created_by "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id where lp.ac_year_id=?1 And lp.course_assignment_id=?2 And lp.year_sem=?3 And "
			+ "lp.employee_id=?4 And lpa.lesson_assignment_id not in (?5) And ((?6 = true AND lpa.ict_text IS NOT NULL) OR (?6 = false)) And lp.active=true")
	public List<HashMap<String, Object>> getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId(Integer ac_year_id,
			Integer course_assignment_id, Integer year_sem, Integer employee_id, List<Integer> lesson_assignment_ids,Boolean ict_status);
	
//	@Query(value = "Select new map(lp.lesson_id as id,lp.subject_id as subject_id,c.course_name as course_name,c.course_short_name as course_short_name,"
//			+ "ca.course_assignment_coursecode as course_assignment_coursecode,ed.employee_name as employee_name,ed.empcode as empcode,"
//			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,"
//			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
//			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
//			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
//			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,"
//			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
//			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
//			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book,lpa.contents as contents,"
//			+ "lpa.plan_date as plan_date,lpa.teaching_aid as teaching_aid,lpa.lesson_assignment_id as lesson_assignment_id,lp.employee_id as employee_id) "
//			+ "From LessonPlan lp "
//			+ "Inner join CourseAssignment ca on ca.course_assignment_id=lp.course_assignment_id "
//			+ "Left join Course c on c.course_id=ca.course_id "
//			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
//			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
//			+ "Left join Schools sch on sch.school_id = lp.school_id "
//			+ "Left join Program pr on pr.program_id = lp.program_id "
//			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
//			+ "Left join Section sec on sec.section_id = lp.section_id "
//			+ "Inner join LessonPlanAssignment lpa on lpa.lesson_id = lp.lesson_id "
//			+ "Left join EmployeeDetails ed on ed.emp_id = lp.employee_id "
//			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id where lp.ac_year_id=?1 And lp.employee_id=?2 And lp.active=true")
	
	@Query(value = "Select new map(lp.lesson_id as id,lp.subject_id as subject_id,c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,ed.employee_name as employee_name,ed.empcode as empcode,"
			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,lp.user_id as user_id,lp.subject_assignment_id as subject_assignment_id,"
			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,c.course_code As course_code,"
			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book,"
			+ "lp.employee_id as employee_id) "
			+ "From LessonPlan lp "
			+ "Inner join CourseAssignment ca on ca.course_assignment_id=lp.course_assignment_id "
			+ "Left join Course c on c.course_id=ca.course_id "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
//			+ "Inner join LessonPlanAssignment lpa on lpa.lesson_id = lp.lesson_id "
			+ "Left join EmployeeDetails ed on ed.emp_id = lp.employee_id "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id where lp.ac_year_id=?1 And lp.employee_id=?2 And lp.active=true")
	public List<HashMap<String, Object>> getLessonPlanByAcademicYearAndEmployeeId(Integer ac_year_id,Integer employee_id);
	
	@Query(value = "Select new map(lpa.lesson_assignment_id as id,lp.subject_id as subject_id,c.course_name as course_name,c.course_short_name as course_short_name,"
	+ "ca.course_assignment_coursecode as course_assignment_coursecode,ed.employee_name as employee_name,ed.empcode as empcode,"
	+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,"
	+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
	+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
	+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
	+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,"
	+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
	+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
	+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book,lpa.contents as contents,"
	+ "lpa.plan_date as plan_date,lpa.teaching_aid as teaching_aid,lp.employee_id as employee_id) "
			+ "From LessonPlanAssignment lpa "
			+ "Inner join  LessonPlan lp on lp.lesson_id = lpa.lesson_id "
			+ "Inner join CourseAssignment ca on ca.course_assignment_id=lp.course_assignment_id "
			+ "Left join Course c on c.course_id=ca.course_id "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
			+ "Left join EmployeeDetails ed on ed.emp_id = lp.employee_id "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id where lpa.lesson_id=?1 And lp.active=true")
	public List<HashMap<String, Object>> getDataByLessonId(Integer lesson_id);

	@Query(value ="Select new map(lpa.lesson_assignment_id as id,lp.lesson_id as lesson_id,lpa.plan_date as plan_date,lpa.contents as contents,lpa.teaching_aid as teaching_aid,"
			+ "c.course_name as course_name,c.course_short_name as course_short_name,lp.subject_id as subject_id,lpa.ict_text as ict_text,lpa.attachment_name as attachment_name,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,lp.user_id as user_id,lp.subject_assignment_id as subject_assignment_id,"
			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,lpa.attachment_path as attachment_path,"
			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,"
			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "ed.employee_name as employee_name,ed.emp_id as emp_id,dept.dept_id as dept_id,dept.dept_name as dept_name,"
			+ "dept.dept_name_short as dept_name_short,c.course_code as course_code,ed.empcode as empcode,"
			+ "ca.course_assignment_id as course_assignment_id,pa.program_assignment_id as program_assignment_id,"
			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book) "
			+ "From LessonPlanAssignment lpa "
			+ "Left join LessonPlan lp on lpa.lesson_id=lp.lesson_id "
			+ "Left join ProgramAssigment pa on pa.program_assignment_id=lp.program_assignment_id "
			+ "Left join CourseAssignment ca on ca.course_assignment_id=lp.course_assignment_id "
			+ "Left join Course c on c.course_id=ca.course_id "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
			+ "Left join UserAuthentication ua on ua.id = lp.user_id "
			+ "Left join EmployeeDetails ed on ed.email = ua.email "
			+ "Left join Department dept on ed.dept_id = dept.dept_id "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id where lp.ac_year_id=?1 And lp.created_by=?2 ")
	public List<HashMap<String, Object>> getLessonPlanBasedOnAcYearIdAndUserId(Integer ac_year_id, Integer user_id);
	
	@Query(value ="Select * from lesson_plan lp where lp.ac_year_id=?1 and lp.course_assignment_id=?2 and lp.user_id=?3 and lp.active=true",nativeQuery =true)
	public LessonPlan createdLessonPlan(Integer ac_year_id, Integer course_assignment_id, Integer employee_id);

	@Query(value = "Select new map(lp.lesson_id as id,lp.subject_id as subject_id,c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,"
			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,"
			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book,lpa.contents as contents,"
			+ "lpa.plan_date as plan_date,lpa.teaching_aid as teaching_aid,lpa.lesson_assignment_id as lesson_assignment_id,lp.employee_id as employee_id) "
			+ "From LessonPlan lp "
			+ "Inner join CourseAssignment ca on ca.course_assignment_id=lp.course_assignment_id "
			+ "Left join Course c on c.course_id=ca.course_id "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
			+ "Inner join LessonPlanAssignment lpa on lpa.lesson_id = lp.lesson_id "
//			+ "Left join EmployeeDetails ed on ed.emp_id = lp.created_by "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id where lp.ac_year_id=?1 And lp.course_assignment_id=?2 And lp.year_sem=?3 And "
			+ "lp.employee_id=?4 And lpa.lesson_assignment_id not in (?5) "
			  + "AND ((?6 = true AND lpa.ict_text IS NOT NULL AND lpa.attachment_path IS NOT NULL) "
		        + "      OR (?6 = false AND (lpa.ict_text IS NULL OR lpa.attachment_path IS NULL))) "
			+ "And lp.active=true")
	public List<HashMap<String, Object>> getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId1(Integer ac_year_id, Integer course_assignment_id, Integer year_sem,Integer employee_id,List<Integer> lesson_assignment_ids, Boolean ict_status);

	
	
	@Query(value = "Select new map(lp.lesson_id as lesson_id,lp.subject_id as subject_id,c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,lp.user_id as user_id,lp.subject_assignment_id as subject_assignment_id,"
			+ "lp.ac_year_id as ac_year_id,lp.year_sem as year_sem,lp.school_id as school_id,"
			+ "lp.program_id as program_id,lp.program_specialization_id as program_specialization_id,lp.section_id as section_id,"
			+ "lp.book_id as book_id,lp.created_date as created_date,lp.modified_date as modified_date,lp.created_by as created_by,"
			+ "lp.modified_by as modified_by,lp.created_username as created_username,lp.modified_username as modified_username,"
			+ "lp.active as active,ss.subjectNameShort as subject_name_short,ac.ac_year as ac_year,"
			+ "sch.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "CONCAT(rb.title_of_book,'-',rb.author,'-',rb.yr_of_Publish ) as title_of_book,lpa.contents as contents,"
			+ "lpa.plan_date as plan_date,lpa.teaching_aid as teaching_aid,lpa.lesson_assignment_id as lesson_assignment_id,"
			+ "lp.employee_id as employee_id,lpa.ict_text as ict_text,lpa.attachment_path as attachment_path,lpa.type as type,"
			+ "lpa.teaching_mode as teaching_mode,lpa.learning_style as learning_style) "
			+ "From LessonPlan lp "
			+ "Inner join CourseAssignment ca on ca.course_assignment_id=lp.course_assignment_id "
			+ "Left join Course c on c.course_id=ca.course_id "
			+ "Left join StdSubjects ss on ss.subjectId=lp.subject_id "
			+ "Left join Academic_year ac on ac.ac_year_id = lp.ac_year_id "
			+ "Left join Schools sch on sch.school_id = lp.school_id "
			+ "Left join Program pr on pr.program_id = lp.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id = lp.program_specialization_id "
			+ "Left join Section sec on sec.section_id = lp.section_id "
			+ "Inner join LessonPlanAssignment lpa on lpa.lesson_id = lp.lesson_id "
			+ "Left join ReferenceBooks rb on rb.book_id = lp.book_id where lp.ac_year_id=?1 And lp.program_id in (?2) And "
			+ "lp.program_specialization_id in (?3) And lp.course_assignment_id=?4 And lp.year_sem=?5 And lp.employee_id=?6 And lpa.lesson_assignment_id not in (?7) "
			+ "And lp.active=true")
	public List<HashMap<String, Object>> getLessonPlanForMobile(Integer ac_year_id, List<Integer> program_id,
			List<Integer> program_specialization_id, Integer course_assignment_id, Integer year_sem, Integer employee_id,
			List<Integer> assignedLessonPlanAssignmentIds);
	
	
}
