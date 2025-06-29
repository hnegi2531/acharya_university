package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.StudentAttendance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional
@Repository 
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Integer>{
	
	
	@Query(value = "select sta from StudentAttendance sta where sta.active=true")
	public List<StudentAttendance> findAll1();
	
	@Query(value ="Select new map(sta.student_attendance_id as id,sta.school_id as school_id,sta.ac_year_id as ac_year_id,"
			+ "sta.course_id as course_id,sta.remarks as remarks,sta.description as description,sta.student_id as student_id,"
			+ "sta.present_status as present_status,sta.offline_status as offline_status,sta.batch_id as batch_id,sta.rating as rating,"
			+ "sta.section_id as section_id,sta.date_of_class as date_of_class,sta.time_slots_id as time_slots_id,"
			+ "sta.lesson_id as lesson_id,sta.time_table_id as time_table_id,sta.year_or_sem as year_or_sem,sta.active as active,"
			+ "sta.created_username as created_username,sta.modified_username as modified_username,sta.created_date as created_date,"
			+ "sta.modified_date as modified_date,sta.created_by as created_by,sta.modified_by as modified_by,"
			+ "sch.school_name_short as school_name_short,ay.ac_year as ac_year,c.course_short_name as course_short_name,"
			+ "std.student_name as student_name,bat.batch_short_name as batch_short_name,sec.section_name as section_name,"
			+ "ts.starting_time as starting_time,ts.ending_time as ending_time,lp.subject_name_short as subject_name_short,"
			+ "tt.class_interval as class_interval) From StudentAttendance sta "
			+ "Left Join Schools sch On sch.school_id=sta.school_id "
			+ "Left Join Academic_year ay On ay.ac_year_id=sta.ac_year_id "
			+ "Left Join Course c On c.course_id=sta.course_id "
			+ "Left Join Student_Details std On std.student_id=sta.student_id "
			+ "Left Join Batch bat On bat.batch_id=sta.batch_id "
			+ "Left Join Section sec On sec.section_id=sta.section_id "
			+ "Left Join TimeSlots ts On ts.time_slots_id=sta.time_slots_id "
			+ "Left Join LessonPlan lp On lp.lesson_id=sta.lesson_id "
			+ "Left Join TimeTable tt On tt.time_table_id=sta.time_table_id "
			+ "Where CONCAT(IfNull(sta.student_attendance_id,''),'',IfNull(sta.offline_status,''),'',IfNull(sta.created_username,''),'',"
			+ "IfNull(sch.school_name_short,''),'',IfNull(ay.ac_year,''),'',IfNull(c.course_short_name,''),'',IfNull(bat.batch_short_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(sta.student_attendance_id as id,sta.school_id as school_id,sta.ac_year_id as ac_year_id,"
			+ "sta.course_id as course_id,sta.remarks as remarks,sta.description as description,sta.student_id as student_id,"
			+ "sta.present_status as present_status,sta.offline_status as offline_status,sta.batch_id as batch_id,"
			+ "sta.section_id as section_id,sta.date_of_class as date_of_class,sta.time_slots_id as time_slots_id,sta.rating as rating,"
			+ "sta.lesson_id as lesson_id,sta.time_table_id as time_table_id,sta.year_or_sem as year_or_sem,sta.active as active,"
			+ "sta.created_username as created_username,sta.modified_username as modified_username,sta.created_date as created_date,"
			+ "sta.modified_date as modified_date,sta.created_by as created_by,sta.modified_by as modified_by,"
			+ "sch.school_name_short as school_name_short,ay.ac_year as ac_year,c.course_short_name as course_short_name,"
			+ "std.student_name as student_name,bat.batch_short_name as batch_short_name,sec.section_name as section_name,"
			+ "ts.starting_time as starting_time,ts.ending_time as ending_time,lp.subject_name_short as subject_name_short,"
			+ "tt.class_interval as class_interval) From StudentAttendance sta "
			+ "Left Join Schools sch On sch.school_id=sta.school_id "
			+ "Left Join Academic_year ay On ay.ac_year_id=sta.ac_year_id "
			+ "Left Join Course c On c.course_id=sta.course_id "
			+ "Left Join Student_Details std On std.student_id=sta.student_id "
			+ "Left Join Batch bat On bat.batch_id=sta.batch_id "
			+ "Left Join Section sec On sec.section_id=sta.section_id "
			+ "Left Join TimeSlots ts On ts.time_slots_id=sta.time_slots_id "
			+ "Left Join LessonPlan lp On lp.lesson_id=sta.lesson_id "
			+ "Left Join TimeTable tt On tt.time_table_id=sta.time_table_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update StudentAttendance sta set sta.active=false where sta.student_attendance_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update StudentAttendance sta set sta.active=true where sta.student_attendance_id=?1")
	public void update1(Integer id);
	
	@Query(value = "Select  sum(case when sta.present_status=true then 1 else 0 end) as present,count(*) as total,"
			+ " CAST((((sum(case when sta.present_status=true then 1 else 0 end)) / count(*)) * 100) as UNSIGNED) as percentage,"
			+ "sta.course_id as course_id From student_attendance sta where sta.student_id=?1 and sta.year_or_sem=?2 and sta.active=true group by sta.course_id ",nativeQuery=true)
	public List<Map<String,Object>> studentAttendanceDetailsForMobileApp(Integer student_id,Integer year_sem);

	@Query(value = "Select date(sta.date_of_class) as date_of_class,sta.present_status,"
			+ "sta.course_id as course_id,sta.time_table_id as time_table_id,CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots From student_attendance sta "
			+ "Left Join time_slots ts On ts.time_slots_id=sta.time_slots_id "
			+ "where sta.student_id=?1 and sta.year_or_sem=?2 and sta.course_id=?3 and sta.active=true ",nativeQuery=true)
	public List<Map<String,Object>> studentAttendanceDetailsOfAllCourseForAbsentAndPresent(Integer student_id,Integer year_sem,Integer course_id);
	
	@Query(value = "Select  sum(case when sta.present_status=true then 1 else 0 end) as present,count(*) as total,"
			+ "c.course_name as course_name,c.course_code as course_code,c.course_short_name as course_short_name,"
			+ "CAST((((sum(case when sta.present_status=true then 1 else 0 end)) / count(*)) * 100) as UNSIGNED) as percentage,"
			+ "sta.course_id as course_id,sta.course_assignment_id as course_assignment_id From student_attendance sta left join course c on c.course_id=sta.course_id "
			+ "where sta.student_id=?1 and sta.year_or_sem=?2 and sta.active=true group by sta.course_id,sta.course_assignment_id ",nativeQuery=true)
	public List<Map<String,Object>> studentAttendanceDetails(Integer student_id,Integer year_sem);
	
	@Query(value ="Select sa.student_attendance_id as student_attendance_id,"
			+ "concat(CAST(sa.date_of_class as DATE),'  ',ts.starting_time,'-',ts.ending_time) as date_and_time_of_class,"
			+ "concat(lpa.contents,'-',lpa.teaching_aid) as concatenated_content_teachingaid,"
			+ "sa.present_status as present_status,"
			+ "sa.active as active,sy.module as module,sy.syllabus_objective as syllabus_objective,"
			+ "sy.topic_name as topic_name From student_attendance sa "
			+ "Left Join lesson_plan lp On lp.lesson_id=sa.lesson_id "
			+ "Left Join lesson_plan_assignment lpa On lpa.lesson_assignment_id=sa.lesson_assignment_id "
			+ "Left Join time_slots ts On ts.time_slots_id=sa.time_slots_id "
			+ "inner join time_table tt on tt.time_table_id=sa.time_table_id "
			+ "Left Join syllabus sy On sy.syllabus_id=sa.syllabus_id "
			+ "where sa.student_id=?1 and sa.course_id=?2 and (tt.section_assignment_id in (?3) Or tt.batch_assignment_id in (?4)) "
			+ "and sa.course_assignment_id=?5 and sa.time_table_id is not null and sa.active=true",nativeQuery=true)
	public List<Map<String, Object>> getPresentAbsentData(Integer student_id, Integer course_id,List<Integer> sectionAssignmentIds,List<Integer> batchAssignmentIds,Integer courseAssignmentId);
	
	@Query(value ="Select sum(case when sa.present_status=true then 1 else 0 end) as total_present_count,"
			+ "sum(case when sa.present_status=false then 1 else 0 end) as total_absent_count From student_attendance sa "
			+ "where sa.student_id=?1 and sa.course_id=?2 and sa.active=true",nativeQuery=true)
	public Map<String, Object> getPresentAbsentCount(Integer student_id, Integer course_id);
	
	@Query(value ="select sa.present_status from StudentAttendance sa where sa.student_id=?1 and sa.time_table_id=?2 and sa.time_slots_id=?3 and sa.active=true")
	public Boolean getCheckPresentOrAbsentForFeedback(Integer student_id,Integer time_table_id,Integer time_slots_id);
	
	@Query(value ="select DISTINCT sass.user_id,ed.employee_name from subject_assignment sass "
			+ "inner join user_details ud on sass.user_id=ud.id "
			+ "left join employee_details ed on ed.email=ud.email where sass.subjet_assign_id IN"
			+ "(select DISTINCT tte.subject_assignment_id from time_table_employee tte where tte.time_table_id IN"
			+ "(select sa.time_table_id from student_attendance sa where sa.student_id=?1))",nativeQuery=true)
	public List<Map<String, Object>> getFacultiesForFeedback(Integer student_id);
	
	@Query(value = "select sta.lesson_assignment_id from StudentAttendance sta "
			+ "Left Join Student_Details std On std.student_id=sta.student_id "
			+ "Left Join Program p On p.program_id=std.program_id "
			+ "Left Join ProgramSpecilization ps On ps.program_specialization_id=std.program_specialization_id "
			+ "where sta.ac_year_id=?1 And p.program_id=?2 And ps.program_specialization_id=?3 And sta.course_assignment_id=?4 And sta.year_or_sem=?5 And sta.active=true")
	List<Integer> getAssignedLessonPlanAssignmentIds(Integer ac_year_id,Integer program_id,Integer program_specialization_id,Integer course_assignment_id, Integer year_sem);

	@Query(value = "select sta.lesson_assignment_id from StudentAttendance sta "
			+ "Left Join Student_Details std On std.student_id=sta.student_id "
			+ "Left Join Program p On p.program_id=std.program_id "
			+ "Left Join ProgramSpecilization ps On ps.program_specialization_id=std.program_specialization_id "
			+ "where sta.ac_year_id=?1 And p.program_id in (?2) And ps.program_specialization_id in (?3) And sta.course_assignment_id=?4 And sta.year_or_sem=?5 And sta.active=true")
	List<Integer> getAssignedLessonPlanAssignmentIdsForMobile(Integer ac_year_id,List<Integer> program_id,List<Integer> program_specialization_id,Integer course_assignment_id, Integer year_sem);

	@Query(value = "select distinct sta.lesson_assignment_id from StudentAttendance sta "
			+ "Where sta.ac_year_id=?1 And sta.course_assignment_id=?2 And sta.year_or_sem=?3 And sta.active=true")
	public List<Integer> getAssignedLessonPlanAssignmentIdsByAcademicYearCourseYearOrSem(Integer ac_year_id,Integer course_assignment_id, Integer year_sem);

		@Query(value = "Select distinct sta.course_id as course_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "c.course_name as course_name,sta.course_assignment_id as course_assignment_id,"
			+ "concat(c.course_name,'-',ca.course_assignment_coursecode) as course_name_with_course_assignment_code From student_attendance sta "
			+ "Left Join course_assignment ca on ca.course_assignment_id=sta.course_assignment_id "
			+ "Left Join course c On c.course_id=sta.course_id "
			+ "where sta.ac_year_id=?1 And ca.program_assignment_id=?2 And ca.program_id=?3 And sta.year_or_sem=?4 And sta.active=true And ca.active=true ",nativeQuery=true)
	public List<Map<String, Object>> getCourseDetailsForAttendanceReport(Integer ac_year_id,Integer program_assignment_id,Integer program_id,Integer current_year_sem);

			@Query(value = "Select  sum(case when sta.present_status=true then 1 else 0 end) as present,count(*) as total,"
			+ " CAST((((sum(case when sta.present_status=true then 1 else 0 end)) / count(*)) * 100) as UNSIGNED) as percentage,"
			+ "sta.course_id as course_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "c.course_name as course_name,sta.time_table_id as time_table_id,sta.student_id as student_id,sta.ac_year_id as ac_year_id,"
			+ "sta.year_or_sem as year_or_sem,sta.course_assignment_id as course_assignment_id From student_attendance sta "
			+ "Left Join course_assignment ca on ca.course_assignment_id=sta.course_assignment_id "
			+ "Left Join course c On c.course_id=sta.course_id "
			+ "inner join time_table tt on tt.time_table_id=sta.time_table_id "
			+ "where sta.student_id=?1 And sta.ac_year_id=?2 and (tt.section_assignment_id in (?3) Or tt.batch_assignment_id in (?4)) "
			+ "And sta.year_or_sem=?5 and sta.time_table_id is not null and sta.active=true group by sta.course_id,sta.course_assignment_id Order by sta.course_assignment_id Asc ",nativeQuery=true)
	public List<Map<String, Object>> studentAttendanceDetailByStudentIdAcademicYearAndCurrentYearSem(Integer student_id,
			Integer ac_year_id, List<Integer> sectionAssignmentIds, List<Integer> batchAssignmentIds,
			Integer year_or_sem);

	@Query(value = "Select  sum(case when sta.present_status=true then 1 else 0 end) as present,count(*) as total,"
			+ " CAST((((sum(case when sta.present_status=true then 1 else 0 end)) / count(*)) * 100) as UNSIGNED) as percentage,"
			+ "sta.course_id as course_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "c.course_name as course_name,sta.time_table_id as time_table_id,sta.student_id as student_id,sta.ac_year_id as ac_year_id,"
			+ "sta.year_or_sem as year_or_sem,sta.course_assignment_id as course_assignment_id From student_attendance sta "
			+ "Left Join course_assignment ca on ca.course_assignment_id=sta.course_assignment_id "
			+ "Left Join course c On c.course_id=sta.course_id "
			+ "inner join time_table tt on tt.time_table_id=sta.time_table_id "
			+ "where sta.student_id=?1 and (tt.section_assignment_id in (?2) Or tt.batch_assignment_id in (?3)) "
			+ "And sta.year_or_sem=?4 and sta.time_table_id is not null and sta.active=true group by sta.course_id,sta.course_assignment_id Order by sta.course_assignment_id Asc ",nativeQuery=true)
	public List<Map<String, Object>> studentAttendanceDetailByStudentIdAndCurrentYearSem(Integer student_id, List<Integer> sectionAssignmentIds, List<Integer> batchAssignmentIds,
																									 Integer year_or_sem);

	@Query(value = "Select  sum(case when sta.present_status=true then 1 else 0 end) as present,count(*) as total,"
			+ " CAST((((sum(case when sta.present_status=true then 1 else 0 end)) / count(*)) * 100) as UNSIGNED) as percentage,"
			+ "sta.course_id as course_id,ca.course_assignment_coursecode as course_assignment_coursecode,tt.batch_assignment_id as batch_assignment_id,"
			+ "c.course_name as course_name,sta.time_table_id as time_table_id,sta.student_id as student_id,"
			+ "sta.ac_year_id as ac_year_id,tt.section_assignment_id as section_assignment_id,"
			+ "sta.year_or_sem as year_or_sem,sta.course_assignment_id as course_assignment_id From student_attendance sta "
			+ "Left Join course_assignment ca on ca.course_assignment_id=sta.course_assignment_id "
			+ "Left Join course c On c.course_id=sta.course_id "
			+ "inner join time_table tt on tt.time_table_id=sta.time_table_id "
			+ "Where sta.student_id=?1 And sta.ac_year_id=?2 And sta.year_or_sem=?3 "
			+ "And sta.time_table_id is not null and sta.active=true group by sta.course_id,sta.course_assignment_id Order by sta.course_assignment_id Asc ",nativeQuery=true)
	public List<Map<String,Object>> studentAttendanceDetailReportForSection(Integer studentId,Integer acYearId,Integer yearOrSem);
	
	@Query(value ="Select sta.student_attendance_id as id,sta.school_id as school_id,sta.ac_year_id as ac_year_id,"
			+ "sta.course_id as course_id,sta.remarks as remarks,sta.description as description,sta.student_id as student_id,"
			+ "sta.present_status as present_status,sta.offline_status as offline_status,sta.batch_id as batch_id,"
			+ "sta.section_id as section_id,sta.date_of_class as date_of_class,sta.time_slots_id as time_slots_id,"
			+ "sta.lesson_id as lesson_id,sta.time_table_id as time_table_id,sta.year_or_sem as year_or_sem,sta.active as active,"
			+ "sta.created_username as created_username,sta.modified_username as modified_username,sta.created_date as created_date,"
			+ "sta.modified_date as modified_date,sta.created_by as created_by,sta.modified_by as modified_by,"
			+ "sch.school_name_short as school_name_short,ay.ac_year as ac_year,c.course_short_name as course_short_name,"
			+ "std.student_name as student_name,bat.batch_short_name as batch_short_name,sec.section_name as section_name,"
			+ "ts.starting_time as starting_time,ts.ending_time as ending_time,lp.subject_name_short as subject_name_short,"
			+ "lpa.plan_date as plan_date,lpa.contents as contents,lpa.teaching_aid as teaching_aid,sta.emp_id as emp_id,"
			+ "lpa.learning_style As learning_style,lpa.type As type,lpa.teaching_mode As teaching_mode,"
			+ "lpa.lesson_assignment_id as lesson_assignment_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "rs.current_sem As current_sem,rs.current_year As current_year,rs.reporting_id As reporting_id,rs.created_date As reportingDate,std.usn As usn,"
			+ "tt.class_interval as class_interval,std.auid as auid,sta.course_assignment_id as course_assignment_id,sy.syllabus_objective as syllabus_objective,"
			+ "(SELECT GROUP_CONCAT(CAST(CASE WHEN sta_sub.present_status = 1 THEN 'true' ELSE 'false' END AS CHAR)) FROM student_attendance sta_sub "
			+ "WHERE sta_sub.student_id = sta.student_id AND sta_sub.course_assignment_id = sta.course_assignment_id "
			+ "AND sta_sub.active = TRUE ORDER BY sta_sub.student_attendance_id DESC LIMIT 3)  AS last_three_attendance,"
			+ "(SELECT CAST((((sum(case when sta_count.present_status=true then 1 else 0 end)) / count(*)) * 100) as UNSIGNED) FROM student_attendance sta_count "
			+"WHERE sta_count.student_id = sta.student_id "
			+"AND sta_count.course_assignment_id = sta.course_assignment_id AND sta_count.active = TRUE )  AS present_percentage "
			+"From student_attendance sta "
			+"Left Join schools sch On sch.school_id=sta.school_id "
			+"Left Join academic_year ay On ay.ac_year_id=sta.ac_year_id "
			+"Left Join course c On c.course_id=sta.course_id "
			+"Left Join course_assignment ca On ca.course_assignment_id=sta.course_assignment_id "
			+"Left Join student_details std On std.student_id=sta.student_id "
			+"Left Join reporting_students rs On std.student_id=rs.student_id "
			+"Left Join batch bat On bat.batch_id=sta.batch_id "
			+"Left Join section sec On sec.section_id=sta.section_id "
			+"Left Join time_slots ts On ts.time_slots_id=sta.time_slots_id "
			+"Left Join lesson_plan lp On lp.lesson_id=sta.lesson_id "
			+"Left Join syllabus sy On sy.syllabus_id=sta.syllabus_id "
			+"Left Join lesson_plan_assignment lpa On lpa.lesson_assignment_id=sta.lesson_assignment_id "
			+"Left Join time_table tt On tt.time_table_id=sta.time_table_id Where sta.time_table_id=:time_table_id and sta.active=true group by sta.student_id ",nativeQuery=true)
	public List<Map<String,Object>> studentAttendanceDetailsForReport( Integer time_table_id);

	@Query(value = "Select  sta.present_status as present_status,sta.student_attendance_id as student_attendance_id,sta.date_of_class as date_of_class,"
			+ "sta. year_or_sem as year_or_sem,concat(ts.starting_time,'-',ts.ending_time) as time_slot,sta.time_slots_id as time_slots_id,"
			+ "sta.course_id as course_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "c.course_name as course_name,sta.time_table_id as time_table_id,sta.student_id as student_id,sta.ac_year_id as ac_year_id,"
			+ "sta.course_assignment_id as course_assignment_id,"
			+ "concat(c.course_name,'-',ca.course_assignment_coursecode) as course_name_with_course_assignment_code,"
			+ "concat(ed.employee_name,'-',ed.empcode) as employee_name From student_attendance sta "
			+ "Left Join course_assignment ca on ca.course_assignment_id=sta.course_assignment_id "
			+ "Left Join course c On c.course_id=sta.course_id "
			+ "Left Join time_slots ts On ts.time_slots_id=sta.time_slots_id "
			+ "inner join time_table tt on tt.time_table_id=sta.time_table_id "
			+ "Left Join employee_details ed On ed.emp_id=sta.emp_id "
			+ "where sta.student_id=?1 And sta.ac_year_id=?2 And sta.year_or_sem=?3 And sta.course_assignment_id=?4 And tt.section_assignment_id in (?5) And sta.active=true ",nativeQuery=true)
	public List<Map<String,Object>> getDetailedStudentAttendanceReportOfCourse(Integer student_id,Integer ac_year_id,Integer year_or_sem,Integer course_assignment_id,List<Integer> sectionAssignmentId);

	@Query(value="Select sta.time_table_id FROM student_attendance sta "
			+ "Where sta.ac_year_id=?1 And sta.year_or_sem=?2 And sta.course_assignment_id=?3 And sta.student_id=?4 And sta.section_id=?5 And sta.present_status=true And sta.active=true",nativeQuery=true)
	public List<Integer> attendanceTakenTimeTableIdsBySectionWithCourseAssignmentId(Integer acYearId, Integer currentYearOrSem,
			Integer courseAssignmentId, Integer studentId, Integer sectionId);

	@Query(value = "Select  sum(case when sta.present_status=true then 1 else 0 end) as present,count(*) as total,"
			+ " CAST((((sum(case when sta.present_status=true then 1 else 0 end)) / count(*)) * 100) as UNSIGNED) as percentage,"
			+ "sta.course_id as course_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "c.course_name as course_name,sta.time_table_id as time_table_id,sta.student_id as student_id,sta.ac_year_id as ac_year_id,"
			+ "sta.year_or_sem as year_or_sem,sta.course_assignment_id as course_assignment_id From student_attendance sta "
			+ "Left Join course_assignment ca on ca.course_assignment_id=sta.course_assignment_id "
			+ "Left Join course c On c.course_id=sta.course_id "
			+ "where sta.student_id=?1 And sta.ac_year_id=?2 And sta.year_or_sem=?3 And date(sta.date_of_class) >= date(?4) and sta.active=true group by sta.course_id,sta.course_assignment_id Order by sta.course_assignment_id Asc ",nativeQuery=true)
	public List<Map<String,Object>> studentAttendanceDetailByStudentIdAcademicYearCurrentYearSemAndCommencementDate(Integer student_id,Integer ac_year_id,Integer year_or_sem,Date commencementDate);

	@Query(value = "Select count(*) From student_attendance sta where sta.course_id=?1 And sta.ac_year_id=?2 And sta.year_or_sem=?3 and sta.active=true",nativeQuery=true)
	public Integer totalAttendanceCountOfCourseByCourseAcademicYearAndCurrentYearSem(Integer course_id,Integer ac_year_id,Integer year_sem);
	
	@Query(value = "Select sta.course_id as course_id,c.course_code as course_code,"
			+ "c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "sta.year_or_sem as year_or_sem,sta.course_assignment_id as course_assignment_id From student_attendance sta "
			+ "inner join time_table tt on tt.time_table_id=sta.time_table_id "
			+ "Left Join course_assignment ca on ca.course_assignment_id=sta.course_assignment_id "
			+ "Left Join course c On c.course_id=sta.course_id "
			+ "where sta.student_id=?1 and (tt.section_assignment_id in (?2) Or tt.batch_assignment_id in (?3)) "
			+ "and sta.time_table_id is not null and sta.active=true group by sta.course_id,sta.course_assignment_id",nativeQuery=true)
	public List<Map<String, Object>> courseDetails(Integer student_id, List<Integer> sectionAssignmentIds,
			List<Integer> batchAssignmentIds);

	@Query(value ="Select sta.student_attendance_id as student_attendance_id,"
			+ "sta.present_status as present_status,sta.batch_id as batch_id,"
			+ "sta.section_id as section_id,sta.time_slots_id as time_slots_id,"
			+ "sta.time_table_id as time_table_id,sta.year_or_sem as year_or_sem,sta.active as active,"
			+ "sta.created_username as created_username,sta.created_by as created_by,"
			+ "bat.batch_short_name as batch_short_name,sec.section_name as section_name,"
			+ "ts.starting_time as starting_time,ts.ending_time as ending_time,"
			+ "concat(ts.starting_time,'-',ts.ending_time) as time_slot,ir.roomcode as roomcode,"
			+ "tt.selected_date as selected_date,tit.interval_type_short as interval_type_short,"
			+ "concat(ed.employee_name,'-',ed.empcode) as employee_name_code,"
			+ "lpa.plan_date as plan_date,lpa.contents as contents,lpa.teaching_aid as teaching_aid From student_attendance sta "
			+ "Left Join batch bat On bat.batch_id=sta.batch_id "
			+ "Left Join section sec On sec.section_id=sta.section_id "
			+ "Left Join time_slots ts On ts.time_slots_id=sta.time_slots_id "
			+ "Left Join time_table tt On tt.time_table_id=sta.time_table_id "
			+ "Left Join employee_details ed On ed.emp_id=sta.emp_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join lesson_plan_assignment lpa on lpa.lesson_assignment_id=sta.lesson_assignment_id "
			+ "Where sta.student_id=?1 And sta.ac_year_id=?2 And sta.course_assignment_id=?3 And sta.course_id=?4 And sta.year_or_sem=?5 And sta.active=true ",nativeQuery=true)
	public List<Map<String, Object>> getDetailedStudentAttendanceOfStudentByCourse(Integer student_id,Integer ac_year_id,Integer course_assignment_id,Integer course_id,Integer current_year_sem);
	
	@Query(value ="Select sa.student_attendance_id as student_attendance_id,"
			+ "concat(ts.starting_time,'-',ts.ending_time) as time_slot,CAST(sa.date_of_class as DATE) as date_of_class,"
			+ "concat(CAST(sa.date_of_class as DATE),'  ',ts.starting_time,'-',ts.ending_time) as date_and_time_of_class,"
			+ "concat(lpa.contents,'-',lpa.teaching_aid) as concatenated_content_teachingaid,"
			+ "sa.present_status as present_status,lpa.type as type,lpa.teaching_mode as teaching_mode,lpa.learning_style as learning_style,"
			+ "sa.active as active,sy.module as module,sy.syllabus_objective as syllabus_objective,"
			+ "sy.topic_name as topic_name From student_attendance sa "
			+ "Left Join lesson_plan lp On lp.lesson_id=sa.lesson_id "
			+ "Left Join lesson_plan_assignment lpa On lpa.lesson_assignment_id=sa.lesson_assignment_id "
			+ "Left Join time_slots ts On ts.time_slots_id=sa.time_slots_id "
			+ "inner join time_table tt on tt.time_table_id=sa.time_table_id "
			+ "Left Join syllabus sy On sy.syllabus_id=sa.syllabus_id "
			+ "where sa.student_id=?1 and sa.course_id=?2 and (tt.section_assignment_id in (?3) Or tt.batch_assignment_id in (?4)) "
			+ "and sa.year_or_sem=?5 and sa.time_table_id is not null and sa.active=true",nativeQuery=true)
	public List<Map<String, Object>> getAttendanceData(Integer student_id, Integer course_id,List<Integer> sectionAssignmentIds,List<Integer> batchAssignmentIds, Integer year_or_sem);

	
	@Query(value=" select  s.auid as auid,s.student_id As studentId , s.student_name as studentName,s.usn As usn,"
			+ "ROUND((SUM(CASE WHEN sa.present_status = 1 THEN 1 ELSE 0 END) / COUNT(sa.student_attendance_id)) * 100,2)   AS actualPercentage,"
			+ " sa.student_attendance_id  as studentAttendenceId,sa.section_id AS section_id,sa.course_id AS course_id,"
			+ "rs.current_year AS current_year,rs.current_sem AS current_sem,fsa.percentage AS assignPercentage "
			+ "from student_details s "
			+ " left join student_attendance sa on sa.student_id=s.student_id "
			+ " left join reporting_students rs on rs.student_id=s.student_id "
			+ " left join time_table t on t.time_table_id=sa.time_table_id "
			+ " left join freeze_student_attendence fsa on fsa.institute_id=s.school_id "	
			+ " where rs.current_year=:year and rs.current_sem=:sem and s.program_specialization_id=:program_specialization_id And sa.course_id=:courseId "
			+ " group by  s.auid,s.student_name,sa.course_id "
			+ " HAVING ROUND((SUM(CASE WHEN present_status = 1 THEN 1 ELSE 0 END) / COUNT(student_attendance_id)) * 100, 2) < fsa.percentage ", nativeQuery = true)
	public List<Map<String,Object>> getStudentHaveLessAttendence(Integer year, Integer sem,Integer program_specialization_id, Integer courseId);

	@Query(value = "select sta.student_attendance_id as student_attendance_id, sta.present_status as present_status,sta.batch_id as batch_id,"
			+ "sta.section_id as section_id,sta.time_slots_id as time_slots_id, DATE_FORMAT(sta.date_of_class, '%d-%m-%Y') as date_of_class,"
			+ "sta.time_table_id as time_table_id,sta.year_or_sem as year_or_sem,sta.active as active,bat.batch_short_name as batch_short_name,bat.batch_name as batch_name,"
			+ "std.auid as auid, std.student_id As student_id, std.student_name as student_name,std.usn as usn, "
			+ "sec.section_name as section_name,concat(ts.starting_time,'-',ts.ending_time) as time_slot,rs.reporting_date as reporting_date,"		
			+ "concat(c.course_name,'-',ca.course_assignment_coursecode) as course_name_with_course_assignment_code,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "concat(ed.employee_name,'-',ed.empcode) as employee_name, sch.school_name_short as school_name_short,sch.school_name as school_name "
			+ " from student_attendance sta "
			+ "Left Join schools sch On sch.school_id=sta.school_id "
			+ "Left Join reporting_students rs On rs.student_id=sta.student_id "
			+ "Left Join course c On c.course_id=sta.course_id "
			+ "Left Join course_assignment ca On ca.course_assignment_id=sta.course_assignment_id "
			+ "Left Join department dept on dept.dept_id=ca.dept_id "
			+ "Left Join student_details std On std.student_id=sta.student_id "
			+ "Left Join section sec On sec.section_id=sta.section_id "
			+ "Left Join batch bat On bat.batch_id=sta.batch_id "
			+ "Left Join time_slots ts On ts.time_slots_id=sta.time_slots_id "
			+ "Left Join employee_details ed On ed.emp_id=sta.emp_id "
			+ "where sta.ac_year_id=:ac_year_id and sta.emp_id=:emp_id and sta.course_assignment_id=:course_assignment_id "
			+ "and (:section_id IS NULL OR sta.section_id = :section_id) and  sta.active=true", nativeQuery = true)
	public List<Map<String,Object>> getDetailedStudentAttendanceReportSectionwiseForEmployee(Integer ac_year_id, Integer emp_id, Integer course_assignment_id, Integer section_id);

	@Query(value=" select e.emp_id as empId, e.employee_name as employeeName, e.empcode as empCode from student_attendance sa left "
			+ "	  join time_table t on sa.time_table_id=t.time_table_id left join "
			+ "	  subject_assignment s on s.subjet_assign_id=t.subject_assignment_id left join "
			+ "	  user_details u on u.id=s.user_id left join employee_details e on "
			+ "	  e.email=u.email where sa.student_id=?1 and sa.year_or_sem=?2 and e.active=1 "
			+ "	  group by e.emp_id, e.employee_name, e.empcode",nativeQuery=true)
	public List<Map<String, Object >> getEmployeeListByCuurentYear(Integer studentId, Integer year);
	
	@Query(value="select e.emp_id as empId, e.employee_name as employeeName, e.empcode as empCode from student_attendance sa "
			+ "	left join time_table t on sa.time_table_id=t.time_table_id "
			+ "	left join subject_assignment s on s.subjet_assign_id=t.subject_assignment_id "
			+ "	left join user_details u on u.id=s.user_id "
			+ "	left join employee_details e on e.email=u.email "
			+ "where sa.student_id=?1 and sa.year_or_sem=?2 and e.active=1 group by e.emp_id, e.employee_name, e.empcode",nativeQuery=true)
	public List<Map<String, Object >> getEmployeeListByCuurentSem(Integer studentId, Integer sem);

	@Modifying
	@Query(value=" update StudentAttendance s set s.isEligibleForFeedback=1 where s.student_attendance_id=:studentAttendenceId ")
	public void updateStudentAttendenceForEligibleFeedback(Integer studentAttendenceId);

	@Query(value=" select * from student_attendance sa where sa.student_id=:studentId ORDER BY sa.created_date DESC LIMIT 1 ", nativeQuery = true)
	 public StudentAttendance getStudentAttendanceByStudentId(Integer studentId);


	@Query(value = "Select  sum(case when sta.present_status=true then 1 else 0 end) as present,count(*) as total,"
			+ " CAST((((sum(case when sta.present_status=true then 1 else 0 end)) / count(*)) * 100) as UNSIGNED) as percentage,"
			+ "sta.course_id as course_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "c.course_name as course_name,sta.time_table_id as time_table_id,sta.student_id as student_id,sta.ac_year_id as ac_year_id,"
			+ "sta.year_or_sem as year_or_sem,sta.course_assignment_id as course_assignment_id From student_attendance sta "
			+ "Left Join course_assignment ca on ca.course_assignment_id=sta.course_assignment_id "
			+ "Left Join course c On c.course_id=sta.course_id "
			+ "inner join time_table tt on tt.time_table_id=sta.time_table_id "
			+ "where sta.student_id in (?1) And sta.ac_year_id=?2 And sta.year_or_sem=?3 "
			+ "and tt.section_assignment_id in (?4) And sta.time_table_id is not null and sta.active=true group by sta.student_id,sta.course_id,sta.course_assignment_id Order by sta.course_assignment_id Asc ",nativeQuery=true)
	List<Map<String, Object>> studentAttendanceDetailReportForSectionBulk(List<Integer> convertCommaSeperatedIdsAsList, Integer acYearId, Integer currentYearSem, List<Integer> sectionAssignmentIds);
}
