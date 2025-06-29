package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.StudentMarks;

@Transactional
@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Integer>{
	
	@Query(value = "SELECT stm FROM StudentMarks stm where active=true")
	public List<StudentMarks> findAll1();
	
	@Query(value ="Select new map(stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
			+ "stm.marks_obtained_internal as marks_obtained_internal,stm.marks_obtained_external as marks_obtained_external,"
			+ "stm.total_marks_internal as total_marks_internal,"
			+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,"
			+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
			+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,"
			+ "stm.percentage as percentage,stm.grade as grade,stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,stm.current_sem as current_sem,stm.current_year as current_year,"
			+ "stm.ac_year_id as ac_year_id,stm.batch_id as batch_id,stm.section_id as section_id,stm.status as status,"
			+ "stm.exam_room_id as exam_room_id,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
			+ "stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
			+ "sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
			+ "sd.auid as studentAuid,isa.max_marks as max_marks,stm.course_assignment_id as course_assignment_id,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
			+ "isa.time_slots_id As time_slots_id,ts.starting_time As starting_time,ts.ending_time As ending_time,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,isa.min_marks as min_marks,"
			+ "isa.internal_name as internal_name,isa.internal_short_name as internal_short_name,"
			+ "sch.school_name as school_name,sch.school_name_short as school_name_short,isa.date_of_exam as date_of_exam,"
			+ "ay.ac_year as ac_year,bt.batch_name as batch_name,sc.section_name as section_name,ir.roomcode as roomcode) From StudentMarks stm "
			+ "Left Join Student_Details sd On sd.student_id=stm.student_id "
			+ "Left Join Schools sch On sch.school_id=sd.school_id "
			+ "Left Join CourseAssignment ca On ca.course_assignment_id=stm.course_assignment_id "
			+ "Left Join ProgramSpecilization ps On ca.program_specialization_id=ps.program_specialization_id "
			+ "Left Join Program p On ca.program_id=p.program_id "
			+ "Left Join Course c On c.course_id=ca.course_id "
			+ "Left Join InternalSessionCreation isa On stm.internal_session_id=isa.internal_session_id "
			+ "Left Join TimeSlots ts On ts.time_slots_id=isa.time_slots_id "
			+ "Left Join Academic_year ay On ay.ac_year_id=stm.ac_year_id "
			+ "Left Join Batch bt On bt.batch_id=stm.batch_id "
			+ "Left Join Section sc On sc.section_id=stm.section_id "
			+ "Left Join InfrastructureRooms ir On ir.room_id=stm.exam_room_id "
			+ "Where CONCAT(IfNull(stm.marks_id,''),'',IfNull(sd.student_name,''),'',IfNull(c.course_short_name,''),'',"
			+ "IfNull(stm.course_assignment_id,''),'',IfNull(stm.created_date,''),'',IfNull(stm.created_username,''),IfNull(sd.auid,'')) LIKE %?1% group by stm.marks_id")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
			+ "stm.marks_obtained_internal as marks_obtained_internal,stm.marks_obtained_external as marks_obtained_external,"
			+ "stm.total_marks_internal as total_marks_internal,"
			+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,"
			+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
			+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,"
			+ "stm.percentage as percentage,stm.grade as grade,stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,stm.current_sem as current_sem,stm.current_year as current_year,"
			+ "stm.ac_year_id as ac_year_id,stm.batch_id as batch_id,stm.section_id as section_id,stm.status as status,"
			+ "stm.exam_room_id as exam_room_id,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
			+ "stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
			+ "sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
			+ "sd.auid as studentAuid,isa.max_marks as max_marks,stm.course_assignment_id as course_assignment_id,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,isa.min_marks as min_marks,"
			+ "isa.internal_name as internal_name,isa.internal_short_name as internal_short_name,"
			+ "isa.time_slots_id As time_slots_id,ts.starting_time As starting_time,ts.ending_time As ending_time,"
			+ "sch.school_name as school_name,sch.school_name_short as school_name_short,isa.date_of_exam as date_of_exam,"
			+ "ay.ac_year as ac_year,bt.batch_name as batch_name,sc.section_name as section_name,ir.roomcode as roomcode) From StudentMarks stm "
			+ "Left Join Student_Details sd On sd.student_id=stm.student_id "
			+ "Left Join Schools sch On sch.school_id=sd.school_id "
			+ "Left Join CourseAssignment ca On ca.course_assignment_id=stm.course_assignment_id "
			+ "Left Join ProgramSpecilization ps On ca.program_specialization_id=ps.program_specialization_id "
			+ "Left Join Program p On ca.program_id=p.program_id "
			+ "Left Join Course c On c.course_id=ca.course_id "
			+ "Left Join InternalSessionCreation isa On stm.internal_session_id=isa.internal_session_id "
			+ "Left Join TimeSlots ts On ts.time_slots_id=isa.time_slots_id "
			+ "Left Join Academic_year ay On ay.ac_year_id=stm.ac_year_id "
			+ "Left Join Batch bt On bt.batch_id=stm.batch_id "
			+ "Left Join Section sc On sc.section_id=stm.section_id "
			+ "Left Join InfrastructureRooms ir On ir.room_id=stm.exam_room_id group by stm.marks_id")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update StudentMarks stm set stm.active=false where stm.marks_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update StudentMarks stm set stm.active=true where stm.marks_id=?1")
	public void update1(Integer id);
	
	@Query(value ="Select stm.marks_id as marks_id,it.internal_name as internal_type_name,"
			+ "stm.marks_obtained_internal as marks_obtained,isa.internal_short_name as internal_short_name,"
			+ "concat(c.course_code,'-',c.course_name) as course_name,stm.internal_id as internal_id,"
			+ "stm.total_marks_internal as total_marks,stm.course_id as course_id,"
			+ "stm.active as active,isa.internal_name as internal_name From StudentMarks stm "
			+ "Left Join Course c On c.course_id=stm.course_id "
			+ "Left Join InternalSessionAssignment isa On isa.internal_id=stm.internal_id "
			+ "left join InternalTypes it on it.internal_master_id=isa.internal_master_id where stm.student_id=?1 and stm.current_year_sem=?2 and stm.active=true")
	public List<Map<String, Object>> getScorecardData(Integer student_id, Integer current_year_sem);
	
	@Query(value = "select count(*) from StudentMarks stm where stm.student_id=?1 and stm.course_id=?2 and stm.internal_id=?3 and stm.active=true")
	public Integer getCount(Integer student_id, Integer course_id, Integer internal_id);
	
	@Query(value = "select count(stm.student_id) from StudentMarks stm where stm.student_id=?1 and stm.course_id=?2 and stm.internal_id=?3 and stm.active=true")
	public Integer getTotalNumberOfStudents(Integer student_id, Integer course_id, Integer internal_id);
	
	@Query(value = "select count(stm.student_id) from student_marks stm where stm.current_year_sem=?1 "
			+ "and stm.internal_id=?2 and stm.course_id=?3 and stm.marks_obtained_internal between 0 and ?4 and stm.active=true",nativeQuery=true)
	public Integer getGraphicalScorecardData1(Integer current_year_sem, Integer internal_id, Integer course_id,Integer value1);
	
	@Query(value = "select count(stm.student_id) from student_marks stm where stm.current_year_sem=?1 and stm.internal_id=?2 "
			+ "and stm.course_id=?3 and stm.marks_obtained_internal between ?4 and ?5 and stm.active=true",nativeQuery=true)
	public Integer getGraphicalScorecardData2(Integer current_year_sem, Integer internal_id, Integer course_id,Integer value1,Integer value2);
	
	
	@Query(value = "select count(stm.student_id) from student_marks stm where stm.current_year_sem=?1 and stm.internal_id=?2 "
			+ "and stm.course_id=?3 and stm.marks_obtained_internal between ?4 and ?5 and stm.active=true",nativeQuery=true)
	public Integer getGraphicalScorecardData3(Integer current_year_sem, Integer internal_id, Integer course_id,Integer value2, Integer value3);
	
	
	@Query(value = "select count(stm.student_id) from student_marks stm where stm.current_year_sem=?1 and stm.internal_id=?2 "
			+ "and stm.course_id=?3 and stm.marks_obtained_internal between ?4 and ?5 and stm.active=true",nativeQuery=true)
	public Integer getGraphicalScorecardData4(Integer current_year_sem,Integer internal_id, Integer course_id,Integer value3, Integer total_marks_internal);
	
	@Query(value ="Select stm.marks_id as marks_id,it.internal_name as internal_type_name,"
			+ "stm.marks_obtained_internal as marks_obtained,isa.internal_short_name as internal_short_name,"
			+ "concat(c.course_code,'-',c.course_name) as course_name,stm.internal_id as internal_id,"
			+ "stm.total_marks_internal as total_marks,stm.course_id as course_id,"
			+ "stm.active as active,isa.internal_name as internal_name From StudentMarks stm "
			+ "Left Join Course c On c.course_id=stm.course_id "
			+ "Left Join InternalSessionAssignment isa On isa.internal_id=stm.internal_id "
			+ "left join InternalTypes it on it.internal_master_id=isa.internal_master_id where stm.student_id=?1 and stm.current_year_sem=?2 and stm.internal_id =?3 and stm.active=true")
	public List<Map<String, Object>> getScorecardDataOnInternal(Integer student_id, Integer current_year_sem,Integer internal_id);
 
	 @Query(value="Select sm.marks_obtained_internal as marks_obtained_internal From student_marks sm "
		 		+ "Where sm.internal_id=?1 And sm.current_year_sem=?2 And sm.course_assignment_id=?3 And sm.student_id=?4 And sm.active=true" ,nativeQuery = true)
	public Map<Object, Object> getMarksObtain(Integer internal_id, Integer year_sem, Integer course_assignment_id,
			Integer st);

	 @Query(value ="Select new map(stm.marks_id as marks_id,stm.student_id as student_id,stm.course_assignment_id as course_assignment_id,"
		 		+ "stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,stm.ac_year_id as ac_year_id,"
		 		+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as courseName,"
		 		+ "sd.student_name as student_name,sd.auid as studentAuid,"
		 		+ "itt.max_marks as max_marks,stm.marks_obtained_internal as marks_obtained_internal,stm.percentage as percentage) "
		 		+ "From StudentMarks stm "
		 		+ "Left Join CourseAssignment ca On ca.course_assignment_id=stm.course_assignment_id "
		 		+ "Left Join Course c On ca.course_id=c.course_id "
		 		+ "Left Join InternalTimeTable itt On stm.internal_id=itt.internal_id "
		 		+ "Left Join Student_Details sd On sd.student_id=stm.student_id "
		 		+ "where stm.student_id=?1 And stm.internal_session_id=?2 And stm.active=true Group by stm.marks_id")
	public List<Map<String, Object>> getStudentMarkDetails(Integer student_id, Integer internal_session_id);

	 @Query(value ="Select new map(stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
				+ "stm.marks_obtained_internal as marks_obtained_internal,stm.marks_obtained_external as marks_obtained_external,"
				+ "stm.total_marks_internal as total_marks_internal,"
				+ "stm.percentage as percentage,stm.grade as grade,stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,"
				+ "stm.ac_year_id as ac_year_id,stm.batch_id as batch_id,stm.section_id as section_id,stm.status as status,"
				+ "stm.exam_room_id as exam_room_id,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
				+ "stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
				+ "sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
				+ "sd.auid as studentAuid,itt.max_marks as max_marks,stm.course_assignment_id as course_assignment_id,"
				+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
				+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
				+ "p.program_name as program_name,p.program_short_name as program_short_name,itt.min_marks as min_marks,"
				+ "isa.internal_name as internal_name,isa.internal_short_name as internal_short_name,"
				+ "ua.id as userId,ua.username as username,"
				+ "ay.ac_year as ac_year,bt.batch_name as batch_name,sc.section_name as section_name,ir.roomcode as roomcode) From UserAuthentication ua "
				+ "Left Join EmployeeDetails ed On ua.email=ed.email "
				+ "Left Join ProctorStudentAssignment psa On psa.emp_id=ed.emp_id "
				+ "Left Join Student_Details sd On sd.student_id=psa.student_id "
				+ "Left Join StudentMarks stm On psa.student_id=stm.student_id "
				+ "Left Join CourseAssignment ca On ca.course_assignment_id=stm.course_assignment_id "
				+ "Left Join ProgramSpecilization ps On ca.program_specialization_id=ps.program_specialization_id "
				+ "Left Join Program p On ca.program_id=p.program_id "
				+ "Left Join Course c On ca.course_id=c.course_id "
				+ "Left Join InternalTimeTable itt On stm.internal_id=itt.internal_id "
				+ "Left Join InternalSessionAssignment isa On stm.internal_id=isa.internal_id "
				+ "Left Join Academic_year ay On ay.ac_year_id=stm.ac_year_id "
				+ "Left Join Batch bt On bt.batch_id=stm.batch_id "
				+ "Left Join Section sc On sc.section_id=stm.section_id "
				+ "Left Join InfrastructureRooms ir On ir.room_id=stm.exam_room_id "
				+ "where ua.id=?1 And stm.active=true Group by stm.marks_id")
	public List<Map<String, Object>> getStudentMarkDetailsBasedOnProctor(Integer id);

	 @Query(value="Select distinct(stm.internal_id) as internal_id,isa.internal_short_name as internal_short_name,"
		 		+ "isa.internal_name as internal_name From student_marks stm "
		 		+ "Left Join internal_session_assignment isa On stm.internal_id=isa.internal_id "
			 	+ "Where stm.student_id=?1" ,nativeQuery = true)
	public List<Map<String, Object>> getAllActiveInternal(Integer student_id);

	 @Query("SELECT stm.marks_id FROM StudentMarks stm WHERE stm.ac_year_id = :ac_year_id AND stm.course_assignment_id = :course_assignment_id AND stm.student_id = :student_id")
	public List<Integer> getStudentMarksId(Integer ac_year_id, Integer course_assignment_id, Integer student_id);
	 
	 @Query(value = "Select count(*) From student_marks sm where sm.ac_year_id=?1 And sm.course_assignment_id=?2 And sm.internal_id=?3 And "
		 		+ "sm.student_id=?4",nativeQuery = true)
	public int getCount1(Integer ac_year_id, Integer course_assignment_id, Integer internal_id, Integer student_id);

	 @Query("SELECT COUNT(stm) FROM StudentMarks stm WHERE stm.ac_year_id = :ac_year_id AND stm.course_assignment_id = :course_assignment_id "
	 		+ "AND stm.internal_id = :internal_id AND stm.student_id = :student_id ")
  Integer getCount(@Param("ac_year_id") Integer ac_year_id, @Param("course_assignment_id") Integer course_assignment_id, 
		  @Param("internal_id") Integer internal_id, @Param("student_id") Integer student_id);

	 @Query(value = "select count(*) from StudentMarks stm where stm.ac_year_id=?1 and stm.course_assignment_id=?2 and stm.student_id=?3 and stm.active=true")
	public Integer getCountForstudentMark(Integer ac_year_id, Integer course_assignment_id, Integer sId);
	
	
	 
		@Query(value ="Select new map(stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
				+ "stm.marks_obtained_internal as marks_obtained_internal,stm.marks_obtained_external as marks_obtained_external,"
				+ "stm.total_marks_internal as total_marks_internal,"
				+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,"
				+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
				+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,"
				+ "isa.external_min_marks As external_min_marks,isa.external_max_marks As external_max_marks,"
				+ "isa.date_of_exam As date_of_exam,isa.current_year As current_year,isa.current_sem As current_sem,"
				+ "stm.percentage as percentage,stm.grade as grade,stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,"
				+ "stm.ac_year_id as ac_year_id,stm.batch_id as batch_id,stm.section_id as section_id,stm.status as status,"
				+ "stm.exam_room_id as exam_room_id,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
				+ "stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
				+ "sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
				+ "sd.auid as studentAuid,stm.course_assignment_id as course_assignment_id,"
				+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
				+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
				+ "p.program_name as program_name,p.program_short_name as program_short_name,"
				+ "isa.internal_name as internal_name,isa.internal_short_name as internal_short_name,"
				+ "ay.ac_year as ac_year,bt.batch_name as batch_name,sc.section_name as section_name) From StudentMarks stm "
				+ "Left Join Student_Details sd On sd.student_id=stm.student_id "
				+ "Left Join CourseAssignment ca On ca.course_assignment_id=stm.course_assignment_id "
				+ "Left Join ProgramSpecilization ps On ca.program_specialization_id=ps.program_specialization_id "
				+ "Left Join Program p On ca.program_id=p.program_id "
				+ "Left Join Course c On c.course_id=ca.course_id "
				+ "Left Join InternalSessionCreation isa On stm.internal_session_id=isa.internal_session_id "
				+ "Left Join Academic_year ay On ay.ac_year_id=stm.ac_year_id "
				+ "Left Join Batch bt On bt.batch_id=stm.batch_id "
				+ "Left Join Section sc On sc.section_id=stm.section_id "
				+ "Where CONCAT(IfNull(stm.marks_id,''),'',IfNull(sd.student_name,''),'',IfNull(c.course_short_name,''),'',"
				+ "IfNull(ca.course_assignment_coursecode,''),'',IfNull(stm.created_date,''),'',IfNull(stm.created_username,''),IfNull(sd.auid,'')) LIKE %?1% group by stm.marks_id")
		public Page<Object> fetchAllExternalStudentMarksDetaillistAll1(Pageable pageable, Object keyword);
		
		@Query(value ="Select new map(stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
				+ "stm.marks_obtained_internal as marks_obtained_internal,stm.marks_obtained_external as marks_obtained_external,"
				+ "stm.total_marks_internal as total_marks_internal,"
				+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,"
				+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
				+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,"
				+ "isa.external_min_marks As external_min_marks,isa.external_max_marks As external_max_marks,"
				+ "isa.date_of_exam As date_of_exam,isa.current_year As current_year,isa.current_sem As current_sem,"
				+ "stm.percentage as percentage,stm.grade as grade,stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,"
				+ "stm.ac_year_id as ac_year_id,stm.batch_id as batch_id,stm.section_id as section_id,stm.status as status,"
				+ "stm.exam_room_id as exam_room_id,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
				+ "stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
				+ "sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
				+ "sd.auid as studentAuid,stm.course_assignment_id as course_assignment_id,"
				+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
				+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
				+ "p.program_name as program_name,p.program_short_name as program_short_name,"
				+ "isa.internal_name as internal_name,isa.internal_short_name as internal_short_name,"
				+ "ay.ac_year as ac_year,bt.batch_name as batch_name,sc.section_name as section_name) From StudentMarks stm "
				+ "Left Join Student_Details sd On sd.student_id=stm.student_id "
				+ "Left Join CourseAssignment ca On ca.course_assignment_id=stm.course_assignment_id "
				+ "Left Join ProgramSpecilization ps On ca.program_specialization_id=ps.program_specialization_id "
				+ "Left Join Program p On ca.program_id=p.program_id "
				+ "Left Join Course c On c.course_id=ca.course_id "
				+ "Left Join InternalSessionCreation isa On stm.internal_session_id=isa.internal_session_id "
				+ "Left Join Academic_year ay On ay.ac_year_id=stm.ac_year_id "
				+ "Left Join Batch bt On bt.batch_id=stm.batch_id "
				+ "Left Join Section sc On sc.section_id=stm.section_id ")
		public Page<Object> fetchAllExternalStudentMarksDetaillistAll2(Pageable pageable);

		
		@Query(value = "SELECT stm FROM StudentMarks stm where internal_session_id=?1 and active=true")
		public List<StudentMarks> getStudentMarkDetailsByInternalSessionId(Integer internal_session_id);
		
		
		@Query(value = "SELECT stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
				+ "	stm.marks_obtained_internal as marks_obtained_internal,stm.marks_obtained_external as marks_obtained_external,"
				+ "	stm.total_marks_internal as total_marks_internal,sd.usn As usn,"
				+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,"
				+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
				+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,"
				+ "	stm.percentage as percentage,stm.grade as grade,stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,stm.current_sem as current_sem,stm.current_year as current_year,"
				+ "	stm.ac_year_id as ac_year_id,stm.batch_id as batch_id,stm.section_id as section_id,stm.status as status,"
				+ "	stm.exam_room_id as exam_room_id,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
				+ "	stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
				+ "	sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
				+ "	sd.auid as studentAuid,isa.max_marks as max_marks,stm.course_assignment_id as course_assignment_id,"
				+ "	ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
				+ "	ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
				+ "	p.program_name as program_name,p.program_short_name as program_short_name,isa.min_marks as min_marks,"
				+ "	isa.internal_name as internal_name,isa.internal_short_name as internal_short_name,"
				+ "	isa.time_slots_id As time_slots_id,ts.starting_time As starting_time,ts.ending_time As ending_time,"
				+ "	sch.school_name as school_name,sch.school_name_short as school_name_short,isa.date_of_exam as date_of_exam,"
				+ "	ay.ac_year as ac_year,bt.batch_name as batch_name,sc.section_name as section_name,ir.roomcode as roomcode "
				+ "FROM student_marks stm "
				+ "LEFT JOIN student_details sd ON sd.student_id = stm.student_id "
				+ "LEFT JOIN schools sch ON sch.school_id = stm.school_id "
				+ "LEFT JOIN course_assignment ca ON ca.course_assignment_id = stm.course_assignment_id "
				+ "LEFT JOIN program_specialization ps ON ca.program_specialization_id = ps.program_specialization_id "
				+ "LEFT JOIN program p ON ca.program_id = p.program_id "
				+ "LEFT JOIN department d ON ps.dept_id = d.dept_id "
				+ "LEFT JOIN course c ON c.course_id = ca.course_id "
				+ "LEFT JOIN internal_session_creation isa ON stm.internal_session_id = isa.internal_session_id "
				+ "LEFT JOIN internal_types ity ON ity.internal_master_id = isa.internal_master_id "
				+ "Left Join time_slots ts On ts.time_slots_id=isa.time_slots_id "
				+ "LEFT JOIN academic_year ay ON ay.ac_year_id = stm.ac_year_id "
				+ "LEFT JOIN batch bt ON bt.batch_id = stm.batch_id "
				+ "LEFT JOIN section sc ON sc.section_id = stm.section_id "
				+ "LEFT JOIN infrastructure_rooms ir On ir.room_id=stm.exam_room_id "
				+ " WHERE (:ac_year_id IS NULL OR stm.ac_year_id = :ac_year_id) "
				+ "AND (:school_id IS NULL OR stm.school_id = :school_id) "
				+ "AND (:program_specialization_id IS NULL OR stm.program_specialization_id = :program_specialization_id) "
				+ "AND (:internal_short_name IS NULL OR ity.internal_short_name = :internal_short_name) "
				+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id) "
				+ "AND stm.internal_session_id IS NOT NULL group by stm.marks_id", nativeQuery = true)
		public List<Map<String, Object>> getStudentMarksWithFilteredData(Integer ac_year_id, Integer school_id,
				Integer dept_id, Integer program_specialization_id, String internal_short_name);
		
		
		@Query(value = "SELECT stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
				+ "	stm.marks_obtained_internal as marks_obtained_internal,stm.marks_obtained_external as marks_obtained_external,"
				+ "	stm.total_marks_internal as total_marks_internal,sd.usn As usn,"
				+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,"
				+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
				+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,"
				+ "	stm.percentage as percentage,stm.grade as grade,stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,stm.current_sem as current_sem,stm.current_year as current_year,"
				+ "	stm.ac_year_id as ac_year_id,stm.batch_id as batch_id,stm.section_id as section_id,stm.status as status,"
				+ "	stm.exam_room_id as exam_room_id,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
				+ "	stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
				+ "	sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
				+ "	sd.auid as studentAuid,isa.max_marks as max_marks,stm.course_assignment_id as course_assignment_id,"
				+ "	ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
				+ "	ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
				+ "	p.program_name as program_name,p.program_short_name as program_short_name,isa.min_marks as min_marks,"
				+ "	isa.internal_name as internal_name,isa.internal_short_name as internal_short_name,"
				+ "	isa.time_slots_id As time_slots_id,ts.starting_time As starting_time,ts.ending_time As ending_time,"
				+ "	sch.school_name as school_name,sch.school_name_short as school_name_short,isa.date_of_exam as date_of_exam,"
				+ "	ay.ac_year as ac_year,bt.batch_name as batch_name,sc.section_name as section_name,ir.roomcode as roomcode "
				+ "FROM student_marks stm "
				+ "LEFT JOIN student_details sd ON sd.student_id = stm.student_id "
				+ "LEFT JOIN schools sch ON sch.school_id = stm.school_id "
				+ "LEFT JOIN course_assignment ca ON ca.course_assignment_id = stm.course_assignment_id "
				+ "LEFT JOIN program_specialization ps ON ca.program_specialization_id = ps.program_specialization_id "
				+ "LEFT JOIN program p ON ca.program_id = p.program_id "
				+ "LEFT JOIN department d ON ps.dept_id = d.dept_id "
				+ "LEFT JOIN course c ON c.course_id = ca.course_id "
				+ "LEFT JOIN internal_session_creation isa ON stm.internal_session_id = isa.internal_session_id "
				+ "LEFT JOIN internal_types ity ON ity.internal_master_id = isa.internal_master_id "
				+ "Left Join time_slots ts On ts.time_slots_id=isa.time_slots_id "
				+ "LEFT JOIN academic_year ay ON ay.ac_year_id = stm.ac_year_id "
				+ "LEFT JOIN batch bt ON bt.batch_id = stm.batch_id "
				+ "LEFT JOIN section sc ON sc.section_id = stm.section_id "
				+ "LEFT JOIN infrastructure_rooms ir On ir.room_id=stm.exam_room_id "
				+ " WHERE (:ac_year_id IS NULL OR stm.ac_year_id = :ac_year_id) "
				+ "AND (:school_id IS NULL OR stm.school_id = :school_id) "
				+ "AND (:program_specialization_id IS NULL OR stm.program_specialization_id = :program_specialization_id) "
				+ "AND (:internal_short_name IS NULL OR ity.internal_short_name = :internal_short_name) "
				+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id) AND stm.internal_session_id IS NOT NULL "
				+ "And CONCAT(IfNull(stm.marks_id,''),'',IfNull(sd.student_name,''),'',IfNull(c.course_short_name,''),'',"
				+ "IfNull(ca.course_assignment_coursecode,''),'',IfNull(stm.created_date,''),'',IfNull(stm.created_username,''),IfNull(sd.auid,'')) LIKE %:keyword% group by stm.marks_id", nativeQuery = true)
		public Page<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword,
				Integer ac_year_id, Integer school_id, Integer dept_id, Integer program_specialization_id,
				 String internal_short_name);

		@Query(value = "SELECT stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
				+ "	stm.marks_obtained_internal as marks_obtained_internal,stm.marks_obtained_external as marks_obtained_external,"
				+ "	stm.total_marks_internal as total_marks_internal,sd.usn As usn,"
				+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,"
				+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
				+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,"
				+ "	stm.percentage as percentage,stm.grade as grade,stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,stm.current_sem as current_sem,stm.current_year as current_year,"
				+ "	stm.ac_year_id as ac_year_id,stm.batch_id as batch_id,stm.section_id as section_id,stm.status as status,"
				+ "	stm.exam_room_id as exam_room_id,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
				+ "	stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
				+ "	sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
				+ "	sd.auid as studentAuid,isa.max_marks as max_marks,stm.course_assignment_id as course_assignment_id,"
				+ "	ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
				+ "	ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
				+ "	p.program_name as program_name,p.program_short_name as program_short_name,isa.min_marks as min_marks,"
				+ "	isa.internal_name as internal_name,isa.internal_short_name as internal_short_name,"
				+ "	isa.time_slots_id As time_slots_id,ts.starting_time As starting_time,ts.ending_time As ending_time,"
				+ "	sch.school_name as school_name,sch.school_name_short as school_name_short,isa.date_of_exam as date_of_exam,"
				+ "	ay.ac_year as ac_year,bt.batch_name as batch_name,sc.section_name as section_name,ir.roomcode as roomcode "
				+ "FROM student_marks stm "
				+ "LEFT JOIN student_details sd ON sd.student_id = stm.student_id "
				+ "LEFT JOIN schools sch ON sch.school_id = stm.school_id "
				+ "LEFT JOIN course_assignment ca ON ca.course_assignment_id = stm.course_assignment_id "
				+ "LEFT JOIN program_specialization ps ON ca.program_specialization_id = ps.program_specialization_id "
				+ "LEFT JOIN program p ON ca.program_id = p.program_id "
				+ "LEFT JOIN department d ON ps.dept_id = d.dept_id "
				+ "LEFT JOIN course c ON c.course_id = ca.course_id "
				+ "LEFT JOIN internal_session_creation isa ON stm.internal_session_id = isa.internal_session_id "
				+ "LEFT JOIN internal_types ity ON ity.internal_master_id = isa.internal_master_id "
				+ "Left Join time_slots ts On ts.time_slots_id=isa.time_slots_id "
				+ "LEFT JOIN academic_year ay ON ay.ac_year_id = stm.ac_year_id "
				+ "LEFT JOIN batch bt ON bt.batch_id = stm.batch_id "
				+ "LEFT JOIN section sc ON sc.section_id = stm.section_id "
				+ "LEFT JOIN infrastructure_rooms ir On ir.room_id=stm.exam_room_id "
				+ " WHERE (:ac_year_id IS NULL OR stm.ac_year_id = :ac_year_id) "
				+ "AND (:school_id IS NULL OR stm.school_id = :school_id) "
				+ "AND (:program_specialization_id IS NULL OR stm.program_specialization_id = :program_specialization_id) "
				+ "AND (:internal_short_name IS NULL OR ity.internal_short_name = :internal_short_name) "
				+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id) "
				+ "AND stm.internal_session_id IS NOT NULL group by stm.marks_id", nativeQuery = true)
	public Page<Map<String, Object>> getAllSortedData(Pageable pageable1, Integer ac_year_id, Integer school_id,
	        Integer dept_id, Integer program_specialization_id, String internal_short_name);

	@Query(value = "SELECT stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
			+ "	stm.marks_obtained_internal as marks_obtained_internal,"
			+ "	stm.total_marks_internal as total_marks_internal,"
			+ "	stm.percentage as percentage,stm.grade as grade,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
			+ "	stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
			+ "	sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
			+ "	sd.auid as studentAuid,stm.course_assignment_id as course_assignment_id,"
			+ "	ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
			+ "	ity.internal_name as internal_name,ity.internal_short_name as internal_short_name "
			+ "FROM student_marks stm "
			+ "LEFT JOIN student_details sd ON sd.student_id = stm.student_id "
			+ "LEFT JOIN course_assignment ca ON ca.course_assignment_id = stm.course_assignment_id "
			+ "LEFT JOIN course c ON c.course_id = ca.course_id "
			+ "LEFT JOIN internal_session_creation isa ON stm.internal_session_id = isa.internal_session_id "
			+ "LEFT JOIN internal_types ity ON ity.internal_master_id = isa.internal_master_id "
			+ "WHERE (:student_id IS NULL OR stm.student_id = :student_id) "
			+ "AND (:current_sem IS NULL OR stm.current_sem = :current_sem) "
			+ "AND (:current_year IS NULL OR stm.current_year = :current_year) "
			+ "AND (:course_assignment_id IS NULL OR stm.course_assignment_id = :course_assignment_id) "
			+ "AND (:internal_short_name IS NULL OR ity.internal_short_name = :internal_short_name) "
			+ "AND stm.internal_session_id IS NOT NULL group by stm.marks_id", nativeQuery = true)
	public List<Map<String, Object>> fetchStudentInternalsReportWithFilteredData(Integer student_id, Integer current_sem,
													   Integer current_year, Integer course_assignment_id, String internal_short_name);

//	@Query(value = "SELECT stm.marks_id as id,stm.student_id as student_id,c.course_id as course_id,"
//			+ "	stm.marks_obtained_internal as marks_obtained_internal,stm.marks_obtained_external as marks_obtained_external,"
//			+ " sum(case when ia.present_status='p' then 1 else 0 end) as present,"
//			+ " count(*) as total,"
//			+ " sum(case when ia.present_status='A' then 1 else 0 end) as absent,"
//			+ " sum(case when stm.marks_obtained_internal>=isa.min_marks then 1 else 0 end) as pass,"
//			+ " sum(case when stm.marks_obtained_internal<isa.min_marks then 1 else 0 end) as fail,"
//			+ " ROUND((SUM(CASE WHEN stm.marks_obtained_internal >= isa.min_marks THEN 1 ELSE 0 END) /  NULLIF(COUNT(DISTINCT ia.student_id), 0)) * 100, 2) AS pass_percentage, "
//			+ "	stm.total_marks_internal as total_marks_internal,ed.employee_name As employee_name,"
//			+ "	stm.percentage as percentage,stm.grade as grade,stm.internal_id as internal_id,stm.current_year_sem as current_year_sem,stm.current_sem as current_sem,stm.current_year as current_year,"
//			+ "	stm.ac_year_id as ac_year_id,stm.batch_id as batch_id,stm.section_id as section_id,stm.status as status,"
//			+ "	stm.exam_room_id as exam_room_id,stm.active as active,stm.created_by as created_by,stm.modified_by as modified_by,"
//			+ "	stm.created_date as created_date,stm.modified_date as modified_date,stm.created_username as created_username,"
//			+ "	sd.student_name as student_name,c.course_short_name as course_short_name,c.course_name as course_name,"
//			+ "	sd.auid as studentAuid,isa.max_marks as max_marks,stm.course_assignment_id as course_assignment_id,"
//			+ "	ca.course_assignment_coursecode as course_assignment_coursecode,c.course_code as course_code,"
//			+ "	ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
//			+ "	p.program_name as program_name,p.program_short_name as program_short_name,isa.min_marks as min_marks,"
//			+ "	isa.internal_name as internal_name,isa.internal_short_name as internal_short_name,"
//			+ "	isa.time_slots_id As time_slots_id,ts.starting_time As starting_time,ts.ending_time As ending_time,"
//			+ "	sch.school_name as school_name,sch.school_name_short as school_name_short,isa.date_of_exam as date_of_exam,"
//			+ "	ay.ac_year as ac_year,bt.batch_name as batch_name,sc.section_name as section_name,ir.roomcode as roomcode "
//			+ "FROM student_marks stm "
//			+ "LEFT JOIN internal_attendance ia ON ia.internal_session_id = ia.internal_session_id "
//			+ "LEFT JOIN employee_details ed ON ia.emp_id = ed.emp_id "
//			+ "LEFT JOIN student_details sd ON sd.student_id = stm.student_id "
//			+ "LEFT JOIN schools sch ON sch.school_id = stm.school_id "
//			+ "LEFT JOIN course_assignment ca ON ca.course_assignment_id = stm.course_assignment_id "
//			+ "LEFT JOIN program_specialization ps ON ca.program_specialization_id = ps.program_specialization_id "
//			+ "LEFT JOIN program p ON ca.program_id = p.program_id "
//			+ "LEFT JOIN department d ON ps.dept_id = d.dept_id "
//			+ "LEFT JOIN course c ON c.course_id = ca.course_id "
//			+ "LEFT JOIN internal_session_creation isa ON stm.internal_session_id = isa.internal_session_id "
//			+ "LEFT JOIN internal_types ity ON ity.internal_master_id = isa.internal_master_id "
//			+ "Left Join time_slots ts On ts.time_slots_id=isa.time_slots_id "
//			+ "LEFT JOIN academic_year ay ON ay.ac_year_id = stm.ac_year_id "
//			+ "LEFT JOIN batch bt ON bt.batch_id = stm.batch_id "
//			+ "LEFT JOIN section sc ON sc.section_id = stm.section_id "
//			+ "LEFT JOIN infrastructure_rooms ir On ir.room_id=stm.exam_room_id "
//			+ " WHERE (:ac_year_id IS NULL OR stm.ac_year_id = :ac_year_id) "
//			+ "AND (:school_id IS NULL OR stm.school_id = :school_id) "
//			+ "AND (:current_sem IS NULL OR stm.current_sem = :current_sem) "
//			+ "AND (:current_year IS NULL OR stm.current_year = :current_year) "
//			+ "AND (:program_specialization_id IS NULL OR stm.program_specialization_id = :program_specialization_id) "
//			+ "AND (:school_id IS NULL OR stm.school_id = :school_id) "
//			+ "AND (:program_specialization_id IS NULL OR stm.program_specialization_id = :program_specialization_id) "
//			+ "AND (:internal_short_name IS NULL OR ity.internal_short_name = :internal_short_name) "
//			+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id) "
//			+ "AND stm.internal_session_id IS NOT NULL group by stm.marks_id", nativeQuery = true)
//	List<Map<String, Object>> fetchFromStudentMarksAndAttendanceWithFilteredData(Integer ac_year_id, Integer current_sem, Integer current_year, Integer school_id,
//																		  Integer dept_id, Integer program_specialization_id, String internal_short_name);

	@Query(value ="SELECT stm.marks_id AS id,c.course_id AS course_id,ia.present_status AS present_status, "
			+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,"
			+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
			+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,"
			+ "stm.marks_obtained_internal AS marks_obtained_internal,sd.usn AS usn,COUNT(DISTINCT stm.student_id) AS total_students, "
			+ "COUNT(DISTINCT CASE WHEN ia.present_status = 'p' THEN stm.student_id END) AS present, "
			+ "(COUNT(DISTINCT stm.student_id) - COUNT(DISTINCT CASE WHEN ia.present_status = 'p' THEN stm.student_id END)) AS absent, "
			+ "COUNT(DISTINCT CASE WHEN stm.student_id IN (SELECT DISTINCT stm1.student_id FROM student_marks stm1   "
			+ "JOIN internal_attendance ia1 ON ia1.internal_session_id = stm1.internal_session_id   "
			+ "JOIN internal_session_creation isa1 ON stm1.internal_session_id = isa1.internal_session_id   "
			+ "WHERE ia1.present_status = 'p' AND stm1.marks_obtained_internal >= isa1.min_marks) THEN stm.student_id END) AS pass, "
			+ "COUNT(DISTINCT CASE WHEN stm.student_id NOT IN (SELECT DISTINCT stm1.student_id FROM student_marks stm1   "
			+ "JOIN internal_attendance ia1 ON ia1.internal_session_id = stm1.internal_session_id   "
			+ "JOIN internal_session_creation isa1 ON stm1.internal_session_id = isa1.internal_session_id   "
			+ "WHERE ia1.present_status = 'p' AND stm1.marks_obtained_internal >= isa1.min_marks) AND stm.student_id IN ( "
			+ "SELECT DISTINCT stm2.student_id FROM student_marks stm2   "
			+ "JOIN internal_attendance ia2 ON ia2.internal_session_id = stm2.internal_session_id   "
			+ "WHERE ia2.present_status = 'p') THEN stm.student_id END) AS fail, "
			+ "ROUND((COUNT(DISTINCT CASE WHEN stm.student_id IN (SELECT DISTINCT stm1.student_id   "
			+ "FROM student_marks stm1 JOIN internal_attendance ia1 ON ia1.internal_session_id = stm1.internal_session_id   "
			+ "JOIN internal_session_creation isa1 ON stm1.internal_session_id = isa1.internal_session_id   "
			+ "WHERE ia1.present_status = 'p' AND stm1.marks_obtained_internal >= isa1.min_marks) THEN stm.student_id END) * 100.0)   "
			+ "/ NULLIF(COUNT(DISTINCT CASE WHEN ia.present_status = 'p' THEN stm.student_id END), 0), 0) AS pass_percentage, "
			+ "stm.total_marks_internal AS total_marks_internal,stm.percentage AS percentage,stm.grade AS grade, "
			+ "stm.internal_id AS internal_id,stm.current_year_sem AS current_year_sem,stm.current_sem AS current_sem, "
			+ "stm.current_year AS current_year,stm.ac_year_id AS ac_year_id,stm.batch_id AS batch_id, "
			+ "stm.section_id AS section_id,stm.status AS status,stm.exam_room_id AS exam_room_id, "
			+ "stm.active AS active,stm.created_by AS created_by,stm.modified_by AS modified_by, "
			+ "stm.created_date AS created_date,stm.modified_date AS modified_date,stm.created_username AS created_username, "
			+ "c.course_short_name AS course_short_name,c.course_name AS course_name,isa.max_marks AS max_marks, "
			+ "ca.course_assignment_coursecode AS course_assignment_coursecode,c.course_code AS course_code, "
			+ "GROUP_CONCAT(DISTINCT ed1.employee_name ORDER BY ed1.emp_id SEPARATOR ', ') AS employee_names,stm.course_assignment_id AS course_assignment_id, "
			+ "ps.program_specialization_name AS program_specialization_name,ps.program_specialization_short_name AS program_specialization_short_name, "
			+ "p.program_name AS program_name,p.program_short_name AS program_short_name,isa.min_marks AS min_marks, "
			+ "isa.internal_name AS internal_name,isa.internal_short_name AS internal_short_name,isa.time_slots_id AS time_slots_id, "
			+ "ts.starting_time AS starting_time,ts.ending_time AS ending_time,sch.school_name AS school_name,sch.school_name_short AS school_name_short, "
			+ "isa.date_of_exam AS date_of_exam,ay.ac_year AS ac_year,bt.batch_name AS batch_name,sc.section_name AS section_name, "
			+ "ir.roomcode AS roomcode,COUNT(DISTINCT stm.internal_session_id) AS total_internal_sessions "
			+ "FROM student_marks stm   "
			+ "LEFT JOIN internal_attendance ia ON ia.internal_session_id = stm.internal_session_id   "
			+ "LEFT JOIN employee_details ed ON ia.emp_id = ed.emp_id   "
			+ "LEFT JOIN student_details sd ON sd.student_id = stm.student_id   "
			+ "LEFT JOIN schools sch ON sch.school_id = stm.school_id   "
			+ "LEFT JOIN course_assignment ca ON ca.course_assignment_id = stm.course_assignment_id   "
			+ "LEFT JOIN program_specialization ps ON ca.program_specialization_id = ps.program_specialization_id   "
			+ "LEFT JOIN program p ON ca.program_id = p.program_id   "
			+ "LEFT JOIN department d ON ps.dept_id = d.dept_id   "
			+ "LEFT JOIN course c ON c.course_id = ca.course_id   "
			+ "LEFT JOIN internal_session_creation isa ON stm.internal_session_id = isa.internal_session_id   "
			+ "LEFT JOIN internal_types ity ON ity.internal_master_id = isa.internal_master_id   "
			+ "LEFT JOIN time_slots ts ON ts.time_slots_id = isa.time_slots_id   "
			+ "LEFT JOIN academic_year ay ON ay.ac_year_id = stm.ac_year_id   "
			+ "LEFT JOIN batch bt ON bt.batch_id = stm.batch_id   "
			+ "LEFT JOIN section sc ON sc.section_id = stm.section_id   "
			+ "LEFT JOIN infrastructure_rooms ir ON ir.room_id = stm.exam_room_id   "
			+ "LEFT JOIN subject_assignment sa ON sa.course_assignment_id = stm.course_assignment_id "
			+ "LEFT JOIN user_details ud ON ud.id = sa.user_id "
			+ "LEFT JOIN employee_details ed1 ON ud.email = ed1.email "
			+ " WHERE (:ac_year_id IS NULL OR stm.ac_year_id = :ac_year_id)  "
			+ "AND (:school_id IS NULL OR stm.school_id = :school_id)  "
			+ "AND (:current_sem IS NULL OR stm.current_sem = :current_sem)  "
			+ "AND (:current_year IS NULL OR stm.current_year = :current_year)  "
			+ "AND (:program_specialization_id IS NULL OR stm.program_specialization_id = :program_specialization_id)  "
			+ "AND (:internal_short_name IS NULL OR ity.internal_short_name = :internal_short_name)  "
			+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id)  "
			+ "And CONCAT(IfNull(stm.marks_id,''),'',IfNull(sd.student_name,''),'',IfNull(c.course_short_name,''),'',"
			+ "IfNull(ca.course_assignment_coursecode,''),'',IfNull(stm.created_date,''),'',IfNull(stm.created_username,''),IfNull(sd.auid,'')) LIKE %:keyword% "
			+ "And stm.internal_session_id IS NOT NULL And ia.present_status is not null "
			+ "GROUP BY c.course_id, stm.course_assignment_id ,stm.internal_session_id "
			+ "ORDER BY stm.marks_id DESC", nativeQuery = true)
	public Page<Map<String, Object>> getStudentMarksAndAttendanceWithFilteredDataKeyword(Pageable pageable,
			Object keyword, Integer ac_year_id, Integer current_sem, Integer current_year, Integer school_id,
			Integer dept_id, Integer program_specialization_id, String internal_short_name);

	
	@Query(value ="SELECT stm.marks_id AS id,c.course_id AS course_id,ia.present_status AS present_status, "
			+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,"
			+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
			+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,"
			+ "stm.marks_obtained_internal AS marks_obtained_internal,sd.usn AS usn,COUNT(DISTINCT stm.student_id) AS total_students, "
			+ "COUNT(DISTINCT CASE WHEN ia.present_status = 'p' THEN stm.student_id END) AS present, "
			+ "(COUNT(DISTINCT stm.student_id) - COUNT(DISTINCT CASE WHEN ia.present_status = 'p' THEN stm.student_id END)) AS absent, "
			+ "COUNT(DISTINCT CASE WHEN stm.student_id IN (SELECT DISTINCT stm1.student_id FROM student_marks stm1   "
			+ "JOIN internal_attendance ia1 ON ia1.internal_session_id = stm1.internal_session_id   "
			+ "JOIN internal_session_creation isa1 ON stm1.internal_session_id = isa1.internal_session_id   "
			+ "WHERE ia1.present_status = 'p' AND stm1.marks_obtained_internal >= isa1.min_marks) THEN stm.student_id END) AS pass, "
			+ "COUNT(DISTINCT CASE WHEN stm.student_id NOT IN (SELECT DISTINCT stm1.student_id FROM student_marks stm1   "
			+ "JOIN internal_attendance ia1 ON ia1.internal_session_id = stm1.internal_session_id JOIN internal_session_creation isa1 ON stm1.internal_session_id = isa1.internal_session_id   "
			+ "WHERE ia1.present_status = 'p' AND stm1.marks_obtained_internal >= isa1.min_marks) AND stm.student_id IN ( "
			+ "SELECT DISTINCT stm2.student_id FROM student_marks stm2 JOIN internal_attendance ia2 ON ia2.internal_session_id = stm2.internal_session_id "
			+ "WHERE ia2.present_status = 'p') THEN stm.student_id END) AS fail, "
			+ "ROUND((COUNT(DISTINCT CASE WHEN stm.student_id IN (SELECT DISTINCT stm1.student_id   "
			+ "FROM student_marks stm1 JOIN internal_attendance ia1 ON ia1.internal_session_id = stm1.internal_session_id   "
			+ "JOIN internal_session_creation isa1 ON stm1.internal_session_id = isa1.internal_session_id   "
			+ "WHERE ia1.present_status = 'p' AND stm1.marks_obtained_internal >= isa1.min_marks) THEN stm.student_id END) * 100.0)   "
			+ "/ NULLIF(COUNT(DISTINCT CASE WHEN ia.present_status = 'p' THEN stm.student_id END), 0), 0) AS pass_percentage, "
			+ "stm.total_marks_internal AS total_marks_internal,stm.percentage AS percentage,stm.grade AS grade, "
			+ "stm.internal_id AS internal_id,stm.current_year_sem AS current_year_sem,stm.current_sem AS current_sem, "
			+ "stm.current_year AS current_year,stm.ac_year_id AS ac_year_id,stm.batch_id AS batch_id, "
			+ "stm.section_id AS section_id,stm.status AS status,stm.exam_room_id AS exam_room_id, "
			+ "stm.active AS active,stm.created_by AS created_by,stm.modified_by AS modified_by, "
			+ "stm.created_date AS created_date,stm.modified_date AS modified_date,stm.created_username AS created_username, "
			+ "c.course_short_name AS course_short_name,c.course_name AS course_name,isa.max_marks AS max_marks, "
			+ "ca.course_assignment_coursecode AS course_assignment_coursecode,c.course_code AS course_code, "
			+ "GROUP_CONCAT(DISTINCT ed1.employee_name ORDER BY ed1.emp_id SEPARATOR ', ') AS employee_names,stm.course_assignment_id AS course_assignment_id, "
			+ "ps.program_specialization_name AS program_specialization_name,ps.program_specialization_short_name AS program_specialization_short_name, "
			+ "p.program_name AS program_name,p.program_short_name AS program_short_name,isa.min_marks AS min_marks, "
			+ "isa.internal_name AS internal_name,isa.internal_short_name AS internal_short_name,isa.time_slots_id AS time_slots_id, "
			+ "ts.starting_time AS starting_time,ts.ending_time AS ending_time,sch.school_name AS school_name,sch.school_name_short AS school_name_short, "
			+ "isa.date_of_exam AS date_of_exam,ay.ac_year AS ac_year,bt.batch_name AS batch_name,sc.section_name AS section_name, "
			+ "ir.roomcode AS roomcode,COUNT(DISTINCT stm.internal_session_id) AS total_internal_sessions "
			+ "FROM student_marks stm   "
			+ "LEFT JOIN internal_attendance ia ON ia.internal_session_id = stm.internal_session_id   "
			+ "LEFT JOIN employee_details ed ON ia.emp_id = ed.emp_id   "
			+ "LEFT JOIN student_details sd ON sd.student_id = stm.student_id   "
			+ "LEFT JOIN schools sch ON sch.school_id = stm.school_id   "
			+ "LEFT JOIN course_assignment ca ON ca.course_assignment_id = stm.course_assignment_id   "
			+ "LEFT JOIN program_specialization ps ON ca.program_specialization_id = ps.program_specialization_id   "
			+ "LEFT JOIN program p ON ca.program_id = p.program_id   "
			+ "LEFT JOIN department d ON ps.dept_id = d.dept_id   "
			+ "LEFT JOIN course c ON c.course_id = ca.course_id   "
			+ "LEFT JOIN internal_session_creation isa ON stm.internal_session_id = isa.internal_session_id   "
			+ "LEFT JOIN internal_types ity ON ity.internal_master_id = isa.internal_master_id   "
			+ "LEFT JOIN time_slots ts ON ts.time_slots_id = isa.time_slots_id   "
			+ "LEFT JOIN academic_year ay ON ay.ac_year_id = stm.ac_year_id   "
			+ "LEFT JOIN batch bt ON bt.batch_id = stm.batch_id   "
			+ "LEFT JOIN section sc ON sc.section_id = stm.section_id   "
			+ "LEFT JOIN infrastructure_rooms ir ON ir.room_id = stm.exam_room_id   "
			+ "LEFT JOIN subject_assignment sa ON sa.course_assignment_id = stm.course_assignment_id "
			+ "LEFT JOIN user_details ud ON ud.id = sa.user_id "
			+ "LEFT JOIN employee_details ed1 ON ud.email = ed1.email "
			+ " WHERE (:ac_year_id IS NULL OR stm.ac_year_id = :ac_year_id)  "
			+ "AND (:school_id IS NULL OR stm.school_id = :school_id)  "
			+ "AND (:current_sem IS NULL OR stm.current_sem = :current_sem)  "
			+ "AND (:current_year IS NULL OR stm.current_year = :current_year)  "
			+ "AND (:program_specialization_id IS NULL OR stm.program_specialization_id = :program_specialization_id)  "
			+ "AND (:internal_short_name IS NULL OR isa.internal_short_name = :internal_short_name)  "
			+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id)  "
			+ "AND stm.internal_session_id IS NOT NULL And ia.present_status is not null "
			+ "GROUP BY c.course_id, stm.course_assignment_id ,stm.internal_session_id "
			+ "ORDER BY stm.marks_id DESC", nativeQuery = true)
public Page<Map<String, Object>> getStudentMarksAndAttendanceWithFilteredDataData(Pageable pageable1,
		Integer ac_year_id, Integer current_sem, Integer current_year, Integer school_id, Integer dept_id,
		Integer program_specialization_id, String internal_short_name);


	
	@Query(value ="Select new map(stm.marks_id as id,stm.student_id as student_id,stm.marks_obtained_internal as marks_obtained_internal,stm.external_min_mark As external_min_mark,"
			+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,stm.marks_obtained_external As marks_obtained_external,"
			+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
			+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,ed1.employee_name As facultyName,ed1.empcode As facultyEmpcode,"
			+ "ed2.employee_name As hodName,ed2.empcode As hodEmpcode,ed3.employee_name As hoiName,ed3.empcode As hoiEmpcode,"
			+ "isa.internal_short_name As internal_short_name,sd.student_name As student_name,sd.auid As auid,sd.usn AS usn,s.section_name As section_name) From StudentMarks stm "
			+ "Left Join Student_Details sd On sd.student_id=stm.student_id "
			+ "Left Join InternalSessionCreation isa On stm.internal_session_id=isa.internal_session_id "
			+ "LEFT JOIN SectionAssignment sa ON FIND_IN_SET(sd.student_id, sa.student_ids) > 0 "
			+ "LEFT join Section s on s.section_id=sa.section_id "
			+ "LEFT join EmployeeDetails ed1 on stm.faculty_id=ed1.emp_id "
			+ "LEFT join EmployeeDetails ed2 on stm.hod_id=ed2.emp_id "
			+ "LEFT join EmployeeDetails ed3 on stm.hoi_id=ed3.emp_id "
			+ " WHERE (:ac_year_id IS NULL OR stm.ac_year_id = :ac_year_id)  "
			+ "AND (:school_id IS NULL OR stm.school_id = :school_id)  "
			+ "AND (:current_sem IS NULL OR stm.current_sem = :current_sem)  "
			+ "AND (:current_year IS NULL OR stm.current_year = :current_year)  "
			+ "AND (:program_specialization_id IS NULL OR stm.program_specialization_id = :program_specialization_id)  "
			+ "AND (:course_assignment_id IS NULL OR stm.course_assignment_id = :course_assignment_id)  "
			+ "And CONCAT(IfNull(stm.marks_id,''),'',IfNull(sd.student_name,''),'',IfNull(sd.auid,''),'',"
			+ "IfNull(stm.course_assignment_id,''),'',IfNull(stm.created_date,''),'',IfNull(stm.created_username,''),IfNull(sd.usn,'')) LIKE %:keyword% ")
	public Page<Object> fetchStudentMarksDataWithKeyword(Pageable pageable, Object keyword, Integer ac_year_id,
			Integer current_sem, Integer current_year, Integer school_id, Integer program_specialization_id,
			Integer course_assignment_id);
	
	@Query(value ="Select new map(stm.marks_id as id,stm.student_id as student_id,stm.marks_obtained_internal as marks_obtained_internal,stm.external_min_mark As external_min_mark,"
			+ "stm.faculty_status As faculty_status,stm.hod_status As hod_status,stm.hoi_status As hoi_status,stm.marks_obtained_external As marks_obtained_external,"
			+ "stm.faculty_status_date As faculty_status_date,stm.hod_status_date As hod_status_date,stm.hoi_status_date As hoi_status_date,"
			+ "stm.faculty_id As faculty_id,stm.hod_id As hod_id,stm.hoi_id As hoi_id,ed1.employee_name As facultyName,ed1.empcode As facultyEmpcode,"
			+ "ed2.employee_name As hodName,ed2.empcode As hodEmpcode,ed3.employee_name As hoiName,ed3.empcode As hoiEmpcode,"
			+ "isa.internal_short_name As internal_short_name,sd.student_name As student_name,sd.auid As auid,sd.usn AS usn,s.section_name As section_name) From StudentMarks stm "
			+ "Left Join Student_Details sd On sd.student_id=stm.student_id "
			+ "Left Join InternalSessionCreation isa On stm.internal_session_id=isa.internal_session_id "
			+ "LEFT JOIN SectionAssignment sa ON FIND_IN_SET(sd.student_id, sa.student_ids) > 0 "
			+ "LEFT join Section s on s.section_id=sa.section_id "
			+ "LEFT join EmployeeDetails ed1 on stm.faculty_id=ed1.emp_id "
			+ "LEFT join EmployeeDetails ed2 on stm.hod_id=ed2.emp_id "
			+ "LEFT join EmployeeDetails ed3 on stm.hoi_id=ed3.emp_id "
			+ " WHERE (:ac_year_id IS NULL OR stm.ac_year_id = :ac_year_id)  "
			+ "AND (:school_id IS NULL OR stm.school_id = :school_id)  "
			+ "AND (:current_sem IS NULL OR stm.current_sem = :current_sem)  "
			+ "AND (:current_year IS NULL OR stm.current_year = :current_year)  "
			+ "AND (:program_specialization_id IS NULL OR stm.program_specialization_id = :program_specialization_id)  "
			+ "AND (:course_assignment_id IS NULL OR stm.course_assignment_id = :course_assignment_id) ")
	public Page<Object> fetchStudentMarksDataWithoutKeyword(Pageable pageable1, Integer ac_year_id, Integer current_sem,
			Integer current_year, Integer school_id, Integer program_specialization_id, Integer course_assignment_id);


	
}
