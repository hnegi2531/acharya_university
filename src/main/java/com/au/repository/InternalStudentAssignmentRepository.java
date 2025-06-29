package com.au.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.InternalStudentAssignment;


@Repository
@Transactional
public interface InternalStudentAssignmentRepository extends JpaRepository<InternalStudentAssignment, Integer>  {
	
//	@Query(value = "SELECT count(*) FROM InternalStudentAssignment itta where itta.internal_student_assignment_id=?1 and itta.active=true")
//	 public Integer countOfInternalroomId(Integer internal_student_assignment_id);
//	
//	
//	@Query(value = "SELECT count(*) FROM InternalStudentAssignment itta "
//			+ "left join InternalFacultyRoomAssignment it on it.internal_room_assignment_id=itta.internal_room_assignment_id "
//			+ "left join InternalSessionCreation isa on it.internal_session_id=isa.internal_session_id "
//			+ "where isa.room_id=?1 and itta.student_ids in (?2) "
//			+ "and it.emp_ids=?3 and isa.selected_date=?4 and isa.time_slots_id=?5 and  itta.active =true")
//	public Integer getCountInternalStudentAssignments(Integer room_id, List<Integer> list_std,Integer emp_ids,Date selected_date,Integer time_slots_id);
	
	
//	@Query(value = "SELECT count(*) FROM InternalStudentAssignment itta "
//			+ "left join InternalFacultyRoomAssignment it on it.internal_room_assignment_id=itta.internal_room_assignment_id "
//			+ "left join InternalSessionCreation isa on itta.internal_session_id=isa.internal_session_id "
//			+ "where it.room_id=?1 and it.emp_ids=?2 and isa.time_slots_id=?3 and isa.date_of_exam=?4 and itta.internal_session_id=?5 and itta.active =true")
//	public Integer getCountTimeTableEmployee(Integer internal_session_id);
	
	
	
	@Query(value = "select itta from InternalStudentAssignment itta where itta.active=true")
	public List<InternalStudentAssignment> findAll1();


	@Query(value = "select itta.internal_student_assignment_id as id,itta.student_ids as student_ids,it.emp_ids as emp_ids,it.room_id as room_id,"
			+ "itta.internal_room_assignment_id as internal_room_assignment_id,itta.internal_session_id as internal_session_id,isa.remarks as remarks,"
			+ "itta.created_by as created_by,itta.created_date as created_date,"
			+ "ts.starting_time as starting_time,ts.ending_time as ending_time,CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "CONCAT(ed.employee_name,' - ',ed.empcode) as employeeNameWithCode,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,ir1.roomcode as roomcode,"
			+ "itta.active as active,itta.created_username as created_username,ed.employee_name as employee_name,"
			+ "s.school_name as school_name,s.school_name_short as school_name_short,ac.ac_year as ac_year,ac.ac_year_code as ac_year_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "isa.week_day as week_day,isa.min_marks as min_marks,isa.max_marks as max_marks,"
			+ "pa.program_type as program_type,pa.program_assignment_id as program_assignment_id,"
			+ "c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "c.course_id as course_id,isa.exam_time as exam_time,isa.time_slots_id as time_slots_id,isa.date_of_exam as date_of_exam,"
			+ "isa.ac_year_id as ac_year_id,isa.internal_short_name as internal_short_name,"
			+ "(select (LENGTH(itta.student_ids) - LENGTH(REPLACE(itta.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,isa.year_sem as year_sem,"
			+ "isa.school_id as school_id,isa.program_id as program_id,isa.program_specialization_id as program_specialization_id from internal_student_assignment itta "
			+ "left join internal_faculty_room_assignment it on itta.internal_room_assignment_id=it.internal_room_assignment_id "
			+ "left join internal_session_creation isa on itta.internal_session_id=isa.internal_session_id "
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
			+ "Where CONCAT(IfNull(itta.internal_student_assignment_id,''),'',IfNull(itta.student_ids,''),'',"
		    + "IfNull(itta.created_by,''),'',IfNull(itta.created_date,''),'',IfNull(itta.created_username,''),'',"
			+ "IfNull(itta.created_username,''),'',IfNull(itta.internal_room_assignment_id,''),'',"
			+ "IfNull(isa.date_of_exam,'')) LIKE %?1% ",nativeQuery=true)
	public List<Map<String, Object>> findAll1(Pageable pageable, Object keyword);
	
	
	@Query(value = "select itta.internal_student_assignment_id as id,itta.student_ids as student_ids,it.emp_ids as emp_ids,it.room_id as room_id,"
			+ "itta.internal_room_assignment_id as internal_room_assignment_id,itta.internal_session_id as internal_session_id,isa.remarks as remarks,"
			+ "itta.created_by as created_by,itta.created_date as created_date,"
			+ "ts.starting_time as starting_time,ts.ending_time as ending_time,CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "CONCAT(ed.employee_name,' - ',ed.empcode) as employeeNameWithCode,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,ir1.roomcode as roomcode,"
			+ "itta.active as active,itta.created_username as created_username,ed.employee_name as employee_name,"
			+ "s.school_name as school_name,s.school_name_short as school_name_short,ac.ac_year as ac_year,ac.ac_year_code as ac_year_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "isa.week_day as week_day,isa.min_marks as min_marks,isa.max_marks as max_marks,"
			+ "pa.program_type as program_type,pa.program_assignment_id as program_assignment_id,"
			+ "c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "c.course_id as course_id,isa.exam_time as exam_time,isa.time_slots_id as time_slots_id,isa.date_of_exam as date_of_exam,"
			+ "isa.ac_year_id as ac_year_id,isa.internal_short_name as internal_short_name,"
			+ "(select (LENGTH(itta.student_ids) - LENGTH(REPLACE(itta.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,isa.year_sem as year_sem,"
			+ "isa.school_id as school_id,isa.program_id as program_id,isa.program_specialization_id as program_specialization_id from internal_student_assignment itta "
			+ "left join internal_faculty_room_assignment it on itta.internal_room_assignment_id=it.internal_room_assignment_id "
			+ "left join internal_session_creation isa on itta.internal_session_id=isa.internal_session_id "
			+ "left join academic_year ac on ac.ac_year_id=isa.ac_year_id "
			+ "left join employee_details ed on ed.emp_id=it.emp_ids "
			+ "left join infrastructure_rooms ir1 on ir1.room_id=it.room_id "
			+ "left join program p on p.program_id=isa.program_id "
			+ "left join course_assignment ca on ca.course_assignment_id=isa.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join schools s on s.school_id=isa.school_id "
			+ "left join time_slots ts on ts.time_slots_id=isa.time_slots_id "
			+ "left join program_specialization ps on ps.program_specialization_id=isa.program_specialization_id "
			+ "left join program_assignment pa on pa.program_assignment_id=ps.program_assignment_id ",nativeQuery=true)
	public List<Map<String, Object>> findAll2(Pageable pageable);
	
	
	@Modifying
	@Query(value = "update InternalStudentAssignment itta set itta.active=false where itta.internal_student_assignment_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update InternalStudentAssignment itta set itta.active=true where itta.internal_student_assignment_id=?1")
	public void update1(Integer id);
	
	
//	@Query(value = "SELECT count(*) FROM InternalStudentAssignment itta where itta.emp_ids=?1 and itta.time_slots_id in ?2 and itta.student_ids=?3 and itta.active =true")
//	public Integer getCountOfEmployeeInInternalTimeTable(Integer emp_ids,Integer time_slots_id, String student_ids);
	

	@Query(value = "Select concat(ed.employee_name, '-',d.dept_name_short) as employee_name,ed.emp_id "
			+ " from employee_details ed "
			+ "left join department d on d.dept_id=ed.dept_id "
			+ "where ed.emp_id not in (select Distinct(itt.emp_ids) from internal_faculty_room_assignment itt "
			+ "left join internal_session_creation isa on itt.internal_session_id=isa.internal_session_id "
			+ "where isa.time_slots_id=?1 and isa.date_of_exam=?2 and isa.course_assignment_id=?3 and itt.active=true) and ed.active=true",nativeQuery = true)
	public List<Map<String, Object>> listAllIttaEmpBasedOnTimeAndDate11(Integer time_slots_id,String date_of_exam, Integer course_assignment_id);
	
	@Query(value = "select itt.room_id internal_faculty_room_assignment itt "
			+ "left join internal_session_creation isa on itt.internal_session_id=isa.internal_session_id "
			+ "where isa.program_specialization_id=?1 and isa.course_assignment_id=?2 and isa.date_of_exam=?3",nativeQuery = true)
	public List<Integer> getAssignedRooms(Integer program_specialization_id,Integer course_assignment_id, Date date);

	@Query(value = "select ir.room_id,ir.roomcode from infrastructure_rooms ir "
			+ "where ir.facility_type_id IN (select ift.facility_type_id from infrastructure_facility_type ift where ift.timetable_status=1)",nativeQuery = true)
	public List<Map<String, Object>> listAllIttaRoomBasedOnTimeAndDate11();
	
	
	@Query(value = "select ir.room_id,ir.roomcode from infrastructure_rooms ir "
			+ "where ir.room_id NOT IN ?1 and ir.facility_type_id IN (select ift.facility_type_id from infrastructure_facility_type ift where ift.timetable_status=1)",nativeQuery = true)
	public List<Map<String, Object>> listAllUnassignedIttaRoomBasedOnTimeAndDate11(List<Integer> room_id);
	
	@Query(value = "select itta.student_ids FROM internal_student_assignment itta "
			+ "left join internal_faculty_room_assignment itt on itt.internal_room_assignment_id=itta.internal_room_assignment_id "
			+ "left join internal_session_creation isa on itt.internal_session_id=isa.internal_session_id "
			+ "where isa.course_assignment_id=?1 and itta.internal_session_id=?2 and itta.active=true",nativeQuery = true)
	public String getStudentIds(Integer course_assignment_id, Integer internal_session_id);

	
	@Query(value = "select Distinct(tte.emp_ids) from InternalFacultyRoomAssignment tte "
			+ "left join InternalSessionCreation isa on tte.internal_session_id=isa.internal_session_id "
			+ "where isa.time_slots_id=?1 and isa.date_of_exam=?2 and tte.active=true")
	public List<Integer> getEmplIdsFromInternalTimeTable(Integer time_slots_id2, Date date_of_exam);
	

	@Query(value = "select itta.student_ids FROM internal_student_assignment itta "
			+ "where itta.internal_student_assignment_id=?1 and itta.active=true",nativeQuery = true)
	public String getStudentIds(Integer internal_student_assignment_id);
	
	
	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.current_year,rs.current_sem FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.student_id IN (?1) and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> listOfStudentDetails(List<Integer> student_ids);


	@Query(value = "Select itta.internal_student_assignment_id as id,REPLACE(Group_concat(itta.student_ids),',,',',') as student_ids,it.emp_ids as emp_ids,it.room_id as room_id,"
			+ "itta.internal_room_assignment_id as internal_room_assignment_id,itta.internal_session_id as internal_session_id,"
			+ "itta.created_by as created_by,itta.created_date as created_date,"
			+ "ts.starting_time as starting_time,ts.ending_time as ending_time,CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "CONCAT(ed.employee_name,' - ',ed.empcode) as employeeNameWithCode,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,ir1.roomcode as roomcode,"
			+ "pa.program_type as program_type,pa.program_assignment_id as program_assignment_id,"
			+ "itta.active as active,itta.created_username as created_username,ed.employee_name as employee_name,"
			+ "s.school_name as school_name,s.school_name_short as school_name_short,ac.ac_year as ac_year,ac.ac_year_code as ac_year_code,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "isa.week_day as week_day,isa.min_marks as min_marks,isa.max_marks as max_marks,"
			+ "c.course_name as course_name,c.course_short_name as course_short_name,ca.course_assignment_id as course_assignment_id,"
			+ "c.course_id as course_id,isa.exam_time as exam_time,isa.time_slots_id as time_slots_id,isa.date_of_exam as date_of_exam,"
			+ "isa.ac_year_id as ac_year_id,"
			+ "isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,isa.remarks as isa_remarks,isa.year_sem as year_sem,"
			+ "isa.school_id as school_id,isa.program_id as program_id,isa.program_specialization_id as program_specialization_id,"
			+ "ity.internal_short_name as internal_short_name,itta.attendance_status as attendance_status from internal_student_assignment itta "
			+ "left join internal_faculty_room_assignment it on itta.internal_room_assignment_id=it.internal_room_assignment_id "
			+ "left join internal_session_creation isa on itta.internal_session_id=isa.internal_session_id "
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
			+ "Where it.emp_ids=?1 And itta.active=true Group By isa.date_of_exam,isa.time_slots_id,it.room_id,it.emp_ids ",nativeQuery=true)
	public List<Map<String, Object>> InternalStudentAssignmentDetailsByEmployeeId(Integer employeeId);


	
	@Query(value = "select itta.student_ids FROM internal_student_assignment itta "
			+ "left join internal_faculty_room_assignment it on itta.internal_room_assignment_id=it.internal_room_assignment_id "
			+ "left join internal_session_creation isa on itta.internal_session_id=isa.internal_session_id "
			+ "where isa.ac_year_id=?1 And isa.internal_session_id=?2 And isa.year_sem=?3 And isa.program_specialization_id=?4 And "
			+ "isa.course_assignment_id=?5 And itta.active=true",nativeQuery = true)
	public List<String> getStudentIds(Integer ac_year_id, Integer internal_session_id, Integer year_sem,
			Integer program_specialization_id,Integer course_assignment_id);
	
	 @Modifying
	 @Query(value = "Update InternalStudentAssignment itta set itta.attendance_status=true where itta.internal_student_assignment_id=?1")
	 public void updateAttendanceStatus(Integer internal_student_assignment_id);
	 
	 
	 @Query(value="Select itta.attendance_status as attendance_status,itta.internal_student_assignment_id as internal_student_assignment_id "
				+ "From internal_student_assignment itta where itta.internal_student_assignment_id in ?1 And itta.active=true ",nativeQuery =true)
		public List<Map<String, Object>> checkInternalExamAttendanceStatusList(List<Integer> internal_student_assignment_id);
	 
	 @Query(value="Select itta.attendance_status as attendance_status,itta.internal_student_assignment_id as internal_student_assignment_id "
				+ "From internal_student_assignment itta where itta.internal_student_assignment_id=?1 And itta.active=true ",nativeQuery =true)
		public Map<String, Object> checkInternalExamAttendanceStatus(Integer internal_student_assignment_id);

	 @Query(value="Select itta.student_ids as student_ids From internal_student_assignment itta "
				+ "left join internal_faculty_room_assignment it on itta.internal_room_assignment_id=it.internal_room_assignment_id "
				+ "left join internal_session_creation isa on itta.internal_session_id=isa.internal_session_id "
				+ "where isa.time_slots_id=?1 And it.room_id=?2 And isa.date_of_exam=STR_TO_DATE(?3, '%Y-%m-%d') And itta.active=true ",nativeQuery =true)
	public List<String> getAllStudentIds(Integer time_slots_id, Integer room_id, String selected_date);
	

	 @Query(value = "select sd.student_id, sd.student_name, sd.auid, sc.section_name, sd.usn "
		 		+ " from course_student_assignment  csa "
				+ "	left join student_details sd on sd.student_id=csa.student_id "
				+ " left join reporting_students rs on rs.student_id = sd.student_id "
				+ "	left join section sc on sc.section_id=rs.section_id "
				+ "	where sd.program_specialization_id=?1 and rs.current_sem=?2 and rs.current_year=?3 and csa.course_assignment_id=?4 "
				+ "And csa.active=true And sd.active=true And rs.active=true And rs.eligible_reported_status not in (5) Group by sd.student_id",nativeQuery=true)
		List<Map<String, Object>> getStudentDataByCourseAssignmentId(Integer program_specialization_id, Integer current_sem, Integer current_year, Integer course_assignment_id);
	 
	 @Query(value="Select * From internal_student_assignment itta "
				+ "left join internal_session_creation isa on itta.internal_session_id=isa.internal_session_id "
				+ "where isa.date_of_exam=?1 And isa.time_slots_id=?2 And isa.active=true And itta.active=true ",nativeQuery =true)
	public List<InternalStudentAssignment> getInternalStudentIdsBasedOnDateAndTimeSlots(String date_of_exam, Integer time_slots_id);

	@Modifying
	@Query(value = "Update internal_student_assignment itta set itta.attendance_status=true, itta.modified_by=?2, itta.modified_username=?3 " +
			"where itta.internal_student_assignment_id IN (?1) and itta.active=true",nativeQuery =true)
    public void updateMultipleInternalStudentAssignment(List<Integer> ids, Integer userId, String userName);

}
