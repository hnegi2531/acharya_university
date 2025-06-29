package com.au.repository;

import java.util.Date;
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

import com.au.model.InternalFacultyRoomAssignment;
import com.au.model.InternalSessionCreation;

@Transactional
@Repository
public interface InternalFacultyRoomAssignmentRepository extends JpaRepository<InternalFacultyRoomAssignment, Integer> {
	
//	@Query(value = "select count(*) from InternalFacultyRoomAssignment itt where itt.internal_session_id=?1 and itt.course_assignment_id=?2 and itt.date_of_exam=?3 and itt.time_slots_id=?4 and active=true")
//	public Integer getDateOfExamWithTimeSlotId(Integer internal_id,Integer course_assignment_id,Date date_of_exam,Integer time_slots_id);
	
	
//	@Query(value = "select count(*) from InternalFacultyRoomAssignment itt where itt.course_assignment_id=?1 and active=true")
//	public Integer getCountInternalFacultyRoomAssignmentCourse(Integer course_assignment_id);
	
	
	
	@Query(value = "select new map(itt.internal_room_assignment_id as id,isa.exam_time as exam_time,isa.course_assignment_id as course_assignment_id,"
			+ "isa.date_of_exam as date_of_exam,isa.internal_session_id as internal_session_id,isa.time_slots_id as time_slots_id,"
			+ "itt.active as active,isa.week_day as week_day,isa.program_specialization_id as program_specialization_id,sch.school_name as school_name,sch.school_name_short as school_name_short,"
			+ "isa.max_marks as max_marks,isa.min_marks as min_marks,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "rm.roomcode as roomcode,ps.program_specialization_name as program_specialization_name,p.program_name as program_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_short_name as program_short_name,"
			+ "concat(IfNull(ts.starting_time,''),' - ',IfNull(ts.ending_time,'')) as timeSlot,ed.employee_name as facultyName,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode, istu.attendance_status as attendance_status,"
			+ "isa.school_id as school_id,isa.year_sem as year_sem,isa.program_id as program_id,isa.ac_year_id as ac_year_id,"
			+ "isa.current_year as current_year, isa.current_sem as current_sem,istu.internal_student_assignment_id as internal_student_assignment_id,istu.student_ids as student_ids,"
			+ "itt.created_by as created_by,itt.modified_by as modified_by,itt.created_date as created_date,itt.modified_date as modified_date,"
			+ "itt.created_username as created_username,itt.modified_username as modified_username,itt.emp_ids as emp_ids,itt.rooms.room_id as room_id) "
			+ "from InternalFacultyRoomAssignment itt "
			+ "left join InternalSessionCreation isa on itt.internal_session_id=isa.internal_session_id "
			+ "left join InternalStudentAssignment istu on istu.internal_room_assignment_id=itt.internal_room_assignment_id "
			+ "left join InternalTypes it on it.internal_master_id=isa.internal_master_id "
			+ "left join Schools sch on sch.school_id=isa.school_id "
			+ "left join TimeSlots ts on ts.time_slots_id=isa.time_slots_id "
			+ "left join InfrastructureRooms rm on rm.room_id=itt.rooms.room_id "
			+ "left join ProgramSpecilization ps on isa.program_specialization_id=ps.program_specialization_id "
			+ "left join Program p on ps.program_id=p.program_id "
			+ "LEFT JOIN Department d ON ps.dept_id = d.dept_id "
			+ "left join EmployeeDetails ed on ed.emp_id=itt.emp_ids "
			+ "left join CourseAssignment ca on isa.course_assignment_id=ca.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id "
			+ " WHERE (:ac_year_id IS NULL OR isa.ac_year_id = :ac_year_id) "
			+ "AND (:school_id IS NULL OR isa.school_id = :school_id) "
			+ "AND (:program_specialization_id IS NULL OR isa.program_specialization_id = :program_specialization_id) "
			+ "AND (:internal_short_name IS NULL OR it.internal_short_name = :internal_short_name) "
			+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id) "
			+ "AND isa.internal_session_id IS NOT NULL "
			+ "and CONCAT(IfNull(itt.created_username,''),'',IfNull(isa.time_slots_id,''),'',IfNull(isa.date_of_exam,''),"
			+ "'',IfNull(itt.created_by,''),'',IfNull(itt.created_date,'')) LIKE %:keyword%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer ac_year_id,
													Integer school_id, Integer dept_id, Integer program_specialization_id, String internal_short_name);
	
	
	@Query(value = "select new map(itt.internal_room_assignment_id as id,isa.exam_time as exam_time,isa.course_assignment_id as course_assignment_id,"
			+ "isa.date_of_exam as date_of_exam,isa.internal_session_id as internal_session_id,isa.time_slots_id as time_slots_id,"
			+ "itt.active as active,isa.week_day as week_day,isa.program_specialization_id as program_specialization_id,sch.school_name as school_name,sch.school_name_short as school_name_short,"
			+ "isa.max_marks as max_marks,isa.min_marks as min_marks,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "rm.roomcode as roomcode,ps.program_specialization_name as program_specialization_name,p.program_name as program_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_short_name as program_short_name,"
			+ "concat(IfNull(ts.starting_time,''),' - ',IfNull(ts.ending_time,'')) as timeSlot,ed.employee_name as facultyName,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode, istu.attendance_status as attendance_status,"
			+ "isa.school_id as school_id,isa.year_sem as year_sem,isa.program_id as program_id,isa.ac_year_id as ac_year_id,"
			+ "isa.current_year as current_year, isa.current_sem as current_sem,istu.internal_student_assignment_id as internal_student_assignment_id,istu.student_ids as student_ids,"
			+ "itt.created_by as created_by,itt.modified_by as modified_by,itt.created_date as created_date,itt.modified_date as modified_date,"
			+ "itt.created_username as created_username,itt.modified_username as modified_username,itt.emp_ids as emp_ids,itt.rooms.room_id as room_id) "
			+ "from InternalFacultyRoomAssignment itt "
			+ "left join InternalSessionCreation isa on itt.internal_session_id=isa.internal_session_id "
			+ "left join InternalStudentAssignment istu on istu.internal_room_assignment_id=itt.internal_room_assignment_id "
			+ "left join InternalTypes it on it.internal_master_id=isa.internal_master_id "
			+ "left join Schools sch on sch.school_id=isa.school_id "
			+ "left join TimeSlots ts on ts.time_slots_id=isa.time_slots_id "
			+ "left join InfrastructureRooms rm on rm.room_id=itt.rooms.room_id "
			+ "left join ProgramSpecilization ps on isa.program_specialization_id=ps.program_specialization_id "
			+ "left join Program p on ps.program_id=p.program_id "
			+ "LEFT JOIN Department d ON ps.dept_id = d.dept_id "
			+ "left join EmployeeDetails ed on ed.emp_id=itt.emp_ids "
			+ "left join CourseAssignment ca on isa.course_assignment_id=ca.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id "
			+ " WHERE (:ac_year_id IS NULL OR isa.ac_year_id = :ac_year_id) "
			+ "AND (:school_id IS NULL OR isa.school_id = :school_id) "
			+ "AND (:program_specialization_id IS NULL OR isa.program_specialization_id = :program_specialization_id) "
			+ "AND (:internal_short_name IS NULL OR it.internal_short_name = :internal_short_name) "
			+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id) "
			+ "AND isa.internal_session_id IS NOT NULL ")
	public Page<Object> getAllSortedData(Pageable pageable1, Integer ac_year_id, Integer school_id,
										 Integer dept_id, Integer program_specialization_id, String internal_short_name);
	
	
	
	
	@Modifying
	@Query(value = "update InternalFacultyRoomAssignment itt set itt.active=false where itt.internal_room_assignment_id=?1")
	public void updateDept(Integer id);
	
	
	@Modifying
	@Query(value = "update InternalFacultyRoomAssignment itt set itt.active=true where itt.internal_room_assignment_id=?1")
	public void updateDept1(Integer id);
	
	@Query(value = "select isa from InternalSessionCreation isa "
			+ "where isa.internal_session_id=?1 and isa.date_of_exam=?2 and isa.active=true")
	public List<InternalSessionCreation> getCourseAssIds(Integer internal_session_id,Date date_of_exam);
	
	@Query(value = "Select new map(c.course_id as course_id,c.course_name as course_name,ca.course_assignment_id as course_assignment_id,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "concat(c.course_name,'-',c.course_code) as course,c.course_short_name as course_short_name) from CourseAssignment ca "
			+ "left join Course c on ca.course_id=c.course_id "
			+ "where ca.course_assignment_id =?1 and ca.active=true")
	public HashMap<String, Object> listIttaCourseBasedOnDate11(Integer course_assignment_id);
	
	
	
	@Query(value = "select itta.student_ids from InternalStudentAssignment itta left join "
			+ "InternalFacultyRoomAssignment itt on itt.internal_session_id=itta.internal_session_id "
			+ "left join InternalSessionCreation isa on itt.internal_session_id=isa.internal_session_id "
			+ "where isa.course_assignment_id=1 and itta.internal_session_id=1")
	public List<String> getStudentIds(Integer course_assignment_id, Integer internal_session_id);
	
	
	@Query(value = "Select itta1.student_ids as student_ids,sd.student_name as student_name,sd.auid as auid,sd.usn as usn,"
			+ "itta1.internal_session_id as internal_session_id from internal_student_assignment itta1 "
			+"left join student_details sd on sd.student_id=itta1.student_ids "
			+ "where itta1.internal_id not in (select itt1.internal_session_id from internal_faculty_room_assignment itt1 "
			+ "left join internal_session_creation isa on itt.internal_session_id=isa.internal_session_id "
			+ "where isa.course_assignment_id=?1 and itt1.internal_session_id=?2)",nativeQuery = true)
	public List<Map<String, Object>> listOfStudentDetails11(Integer course_assignment_id,Integer internal_session_id);


//	@Query(value = "select Distinct tte.emp_ids from InternalTimeTableAssignment tte "
//			+ "where tte.time_slots_id=?1 and tte.selected_date=?2 and tte.active=true")
//	public List<Integer> getEmplIdsFromInternalTimeTable(Integer time_slots_id2, String selected_date2);
	
	@Query(value = "select Distinct(tte.emp_ids) from InternalFacultyRoomAssignment tte "
			+ "left join InternalSessionCreation isa on tte.internal_session_id=isa.internal_session_id "
			+ "where isa.time_slots_id=?1 and isa.date_of_exam=?2 and tte.active=true")
	public List<Integer> getEmplIdsFromInternalTimeTable(Integer time_slots_id2, String date_of_exam);


		@Query(value = "select itta.student_ids FROM internal_timetable_assignment itta "
			+ "where itta.internal_timetable_assignment_id=?1 and itta.active=true",nativeQuery = true)
	public String getStudentIds(Integer internal_timetable_assignment_id);


		@Query(value = "Select itta.internal_student_assignment_id as id,group_concat(itta.internal_student_assignment_id) as concatenated_internal_student_assignment_id,"
			+ "itta.internal_room_assignment_id as internal_room_assignment_id,group_concat(itta.internal_session_id) as internal_session_id,itta.remarks as itta_remarks,"
			+ "itta.created_by as created_by,itta.created_date as created_date,it.date as date,REPLACE(Group_concat(itta.student_ids),',,',',') as student_ids,"
			+ "ts.starting_time as starting_time,ts.ending_time as ending_time,CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "CONCAT(ed.employee_name,' - ',ed.empcode) as employeeNameWithCode,it.emp_ids As emp_ids,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,ir1.roomcode as roomcode,"
			+ "pa.program_type as program_type,pa.program_assignment_id as program_assignment_id,"
			+ "itta.active as active,itta.created_username as created_username,ed.employee_name as employee_name,"
			+ "s.school_name as school_name,s.school_name_short as school_name_short,ac.ac_year as ac_year,ac.ac_year_code as ac_year_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "isa.week_day as week_day,isa.min_marks as min_marks,isa.max_marks as max_marks,"
			+ "c.course_name as course_name,c.course_short_name as course_short_name,ca.course_assignment_id as course_assignment_id,"
			+ "isa.exam_time as exam_time,isa.time_slots_id as time_slots_id,isa.date_of_exam as date_of_exam,"
			+ "isa.external_max_marks as external_max_marks,isa.external_min_marks as external_min_marks,isa.ac_year_id as ac_year_id,"
			+ "isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,isa.remarks as isa_remarks,isa.year_sem as year_sem,"
			+ "isa.school_id as school_id,isa.program_id as program_id,isa.program_specialization_id as program_specialization_id,"
			+ "ity.internal_short_name as internal_short_name,itta.attendance_status as attendance_status "
			+ "from internal_student_assignment itta "
			+ "Left join internal_faculty_room_assignment it on itta.internal_room_assignment_id=it.internal_room_assignment_id "
			+ "Inner join internal_session_creation isa on itta.internal_session_id=isa.internal_session_id "
			+ "Left join internal_types ity on ity.internal_master_id=isa.internal_master_id "
			+ "left join academic_year ac on ac.ac_year_id=isa.ac_year_id "
			+ "left join employee_details ed on ed.emp_id=it.emp_ids "
			+ "left join infrastructure_rooms ir1 on ir1.room_id=it.room_id "
			+ "left join program p on p.program_id=isa.program_id "
			+ "left join course_assignment ca on ca.course_assignment_id=isa.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join schools s on s.school_id=isa.school_id "
			+ "left join time_slots ts on ts.time_slots_id=isa.time_slots_id "
			+ "left join program_specialization ps on ps.program_specialization_id=isa.program_specialization_id "
			+ "left join program_assignment pa on pa.program_assignment_id=ps.program_assignment_id "
			+ "Where it.emp_ids=?1 And itta.active=true and it.active=true and isa.active=true Group By isa.date_of_exam,isa.time_slots_id,it.room_id,it.emp_ids  ",nativeQuery=true)
	public List<Map<String, Object>> internalTimeTableAssignmentDetailsByEmployeeId(Integer employeeId);
		
			@Query(value = "SELECT itta.internal_student_assignment_id AS id, "
				+ "isa.date_of_exam AS date_of_exam, "
				+ "isa.internal_name AS internal_name, "
				+ "ity.internal_short_name AS internal_short_name, "
				+ "isa.exam_time AS exam_time, "
				+ "isa.time_slots_id AS time_slots_id, "
				+ "isa.max_marks AS max_marks, "
				+ "isa.min_marks AS min_marks, "
				+ "itta.student_ids AS student_ids, "
				+ "it.emp_ids AS emp_ids, "
				+ "ts.starting_time AS starting_time, "
				+ "ts.ending_time AS ending_time, "
				+ "CONCAT(ts.starting_time, ' - ', ts.ending_time) AS timeSlots, "
				+ "CONCAT(IFNULL(c.course_name, ''), '-', IFNULL(ca.course_assignment_coursecode, '')) AS course_with_coursecode, "
				+ "CONCAT(ed.employee_name, ' - ', ed.empcode) AS employeeNameWithCode, "
				+ "p.program_name AS program_name, "
				+ "p.program_short_name AS program_short_name, "
				+ "ir1.roomcode AS roomcode, "
				+ "pa.program_type AS program_type, "
				+ "pa.program_assignment_id AS program_assignment_id, "
				+ "itta.active AS active, "
				+ "itta.created_username AS created_username, "
				+ "ed.employee_name AS employee_name, "
				+ "s.school_name AS school_name, "
				+ "s.school_name_short AS school_name_short, "
				+ "ac.ac_year AS ac_year, "
				+ "ac.ac_year_code AS ac_year_code, "
				+ "ps.program_specialization_name AS program_specialization_name, "
				+ "ps.program_specialization_short_name AS program_specialization_short_name, "
				+ "isa.school_id AS school_id, "
				+ "isa.program_id AS program_id, "
				+ "isa.program_specialization_id AS program_specialization_id, "
				+ "isa.ac_year_id AS ac_year_id, "
				+ "isa.course_assignment_id AS course_assignment_id, "
				+ "isa.year_sem AS year_sem, "
				+ "isa.current_sem AS current_sem, "
				+ "isa.current_year AS current_year, "
				+ "itta.attendance_status AS attendance_status "
				+ "FROM internal_student_assignment itta "
				+ "LEFT JOIN internal_faculty_room_assignment it ON itta.internal_room_assignment_id = it.internal_room_assignment_id "
				+ "INNER JOIN internal_session_creation isa ON itta.internal_session_id = isa.internal_session_id "
				+ "LEFT JOIN internal_types ity ON ity.internal_master_id = isa.internal_master_id "
				+ "LEFT JOIN academic_year ac ON ac.ac_year_id = isa.ac_year_id "
				+ "LEFT JOIN employee_details ed ON ed.emp_id = it.emp_ids "
				+ "LEFT JOIN infrastructure_rooms ir1 ON ir1.room_id = it.room_id "
				+ "LEFT JOIN program p ON p.program_id = isa.program_id "
				+ "LEFT JOIN course_assignment ca ON ca.course_assignment_id = isa.course_assignment_id "
				+ "LEFT JOIN course c ON c.course_id = ca.course_id "
				+ "LEFT JOIN schools s ON s.school_id = isa.school_id "
				+ "LEFT JOIN time_slots ts ON ts.time_slots_id = isa.time_slots_id "
				+ "LEFT JOIN program_specialization ps ON ps.program_specialization_id = isa.program_specialization_id "
				+ "LEFT JOIN program_assignment pa ON pa.program_assignment_id = ps.program_assignment_id "
				+ "WHERE itta.internal_session_id = ?1 "
				+ "AND itta.active = true "
				+ "AND it.active = true "
				+ "AND isa.active = true", nativeQuery=true)
	public List<Map<String, Object>> internalTimeTableAssignmentDetailsByInternalSessionId(Integer internal_session_id);

		@Query(value = "select Group_concat(itta.student_ids) as student_ids FROM internal_student_assignment itta "
				+ "where itta.internal_session_id=?1 and itta.active=true",nativeQuery = true)
	public String getStudentIdsByInternalSessionId(Integer internal_session_id);
	
	
		@Query(value = "select isa.internal_session_id as id,isa.exam_time as exam_time,isa.course_assignment_id as course_assignment_id,"
				+ "isa.date_of_exam as date_of_exam,isa.internal_session_id as internal_session_id,isa.time_slots_id as time_slots_id,"
				+ "isa.week_day as week_day,isa.program_specialization_id as program_specialization_id,sch.school_name as school_name,sch.school_name_short as school_name_short,"
				+ "isa.max_marks as max_marks,isa.min_marks as min_marks,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
				+ "ps.program_specialization_name as program_specialization_name,p.program_name as program_name,"
				+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_short_name as program_short_name,"
				+ "concat(IfNull(ts.starting_time,''),' - ',IfNull(ts.ending_time,'')) as timeSlot,"
				+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
				+ "isa.school_id as school_id,isa.year_sem as year_sem,isa.program_id as program_id,isa.ac_year_id as ac_year_id,"
				+ "isa.current_year as current_year, isa.current_sem as current_sem "
				+ "from subject_assignment sa "
				+ "left join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
				+ "left join course c on c.course_id=ca.course_id "
				+ "Inner join internal_session_creation isa on isa.course_assignment_id=ca.course_assignment_id "
				+ "left join internal_types it on it.internal_master_id=isa.internal_master_id "
				+ "left join schools sch on sch.school_id=isa.school_id "
				+ "left join time_slots ts on ts.time_slots_id=isa.time_slots_id "
				+ "left join program_specialization ps on isa.program_specialization_id=ps.program_specialization_id "
				+ "left join program p on ps.program_id=p.program_id "
				+ "where sa.user_id=?1 and CONCAT(IfNull(isa.created_username,''),'',IfNull(isa.time_slots_id,''),'',IfNull(isa.date_of_exam,''),"
				+ "'',IfNull(isa.created_by,''),'',IfNull(isa.created_date,'')) LIKE %?1%", nativeQuery=true)
		public Page<Map<String, Object>> getAllDataFilteredByKeyword1(Pageable pageable, Object keyword, Integer user_id);
		
		
		@Query(value = "select isa.internal_session_id as id,isa.exam_time as exam_time,isa.course_assignment_id as course_assignment_id,"
				+ "isa.date_of_exam as date_of_exam,isa.internal_session_id as internal_session_id,isa.time_slots_id as time_slots_id,"
				+ "isa.week_day as week_day,isa.program_specialization_id as program_specialization_id,sch.school_name as school_name,sch.school_name_short as school_name_short,"
				+ "isa.max_marks as max_marks,isa.min_marks as min_marks,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
				+ "ps.program_specialization_name as program_specialization_name,p.program_name as program_name,"
				+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_short_name as program_short_name,"
				+ "concat(IfNull(ts.starting_time,''),' - ',IfNull(ts.ending_time,'')) as timeSlot,"
				+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
				+ "isa.school_id as school_id,isa.year_sem as year_sem,isa.program_id as program_id,isa.ac_year_id as ac_year_id,"
				+ "isa.current_year as current_year, isa.current_sem as current_sem "
				+ "from subject_assignment sa "
				+ "left join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
				+ "left join course c on c.course_id=ca.course_id "
				+ "Inner join internal_session_creation isa on isa.course_assignment_id=ca.course_assignment_id "
				+ "left join internal_types it on it.internal_master_id=isa.internal_master_id "
				+ "left join schools sch on sch.school_id=isa.school_id "
				+ "left join time_slots ts on ts.time_slots_id=isa.time_slots_id "
				+ "left join program_specialization ps on isa.program_specialization_id=ps.program_specialization_id "
				+ "left join program p on ps.program_id=p.program_id where sa.user_id=?1", nativeQuery=true)
		public Page<Map<String, Object>> getAllSortedData1(Pageable pageable, Integer user_id);


}
