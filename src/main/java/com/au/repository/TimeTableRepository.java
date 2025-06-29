package com.au.repository;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.BatchByAcademicYear;
import com.au.dto.SectionByAcademicYear;
import com.au.model.TimeTable;

@Repository
@Transactional
public interface TimeTableRepository  extends JpaRepository<TimeTable, Integer>{

	@Query(value = "select h from TimeTable h where h.active=true")
	public List<TimeTable> findAll1();

	@Modifying
	@Query(value = "update TimeTable h set h.active=false where h.time_table_id in ?1")
	public void updateTimeTable(List<Integer> time_table_ids);

	@Modifying
	@Query(value = "update TimeTable h set h.active=true where h.time_table_id in ?1")
	public void updateTimeTable1(List<Integer> time_table_ids);
	
	@Query(value ="Select tt.time_table_id as id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,tt.emp_id as emp_id,ir.roomcode as roomcode,tt.selected_date as selected_date,"
			+ "(select (LENGTH(seca.student_ids) - LENGTH(REPLACE(seca.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tt.active as active,tt.created_username as created_username,tt.modified_username as modified_username,c.course_code as course_code,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name From time_table tt "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Inner join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id",nativeQuery =true)
	public List<Map<String,Object>> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select tt.time_table_id as id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,tt.emp_id as emp_id,ir.roomcode as roomcode,tt.selected_date as selected_date,"
			+ "(select (LENGTH(seca.student_ids) - LENGTH(REPLACE(seca.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tt.active as active,tt.created_username as created_username,tt.modified_username as modified_username,c.course_code as course_code,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name From time_table tt "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Inner join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course c on c.course_id=sbja.course_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Inner join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id",nativeQuery =true)
	public List<Map<String,Object>> findAll3(Pageable pageable);
	
	@Query(value = "Select tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.interval_type_id as intervalTypeId,tte.emp_id as emp_id,hr.roomName as roomName,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,"
			+ "tt.floor_id as floor_id,tte.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "c.course_code as course_code,ca.course_assignment_coursecode as course_assignment_coursecode,c.course_short_name as course_short_name,"
			+ "ca.year_sem as year_sem,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "tit.intervalTypeShort as intervalTypeShort,emp.employee_name as employee_name From TimeTableEmployee tte "
			+ "Left join TimeTable tt on tte.time_table_id=tt.time_table_id "
			+ "Left join Academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join Schools sch on sch.school_id=tt.school_id "
			+ "Left join Program pr on pr.program_id=tt.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join TimeIntervalTypes tit on tit.intervalTypeId=tt.interval_type_id "
			+ "Left join EmployeeDetails emp on emp.emp_id=tte.emp_id "
			+ "Left join SectionAssignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join Section s on s.section_id=seca.section_id "
			+ "Left join BatchAssignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join Batch b on b.batch_id=ba.batch_id "
			+ "Left join HostelRooms hr on hr.hostelRoomId=tt.room_id "
			+ "Left join TimeSlots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Left join SubjectAssignment sa on sa.subjetAssignId=tt.subject_assignment_id "
			+ "Left join CourseAssignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "Left join Course c on c.course_id=ca.course_id where tte.emp_id IN ?1 and tte.selected_date between(?2) And (?3) and tt.week_day=?4 and tt.section_assignment_id IS NOT NULL")
	public List<Map<String, Object>> getEmployeeTimetableSchedule(List<Integer> emp_ids, Date from_date, Date to_date, String day);
	
	@Query(value ="Select tte.time_table_employee_id as id,tt.time_table_id as time_table_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,ir.roomcode as roomcode,tt.program_assignment_id as program_assignment_id,"
	//		+ "(select (LENGTH(seca.student_ids) - LENGTH(REPLACE(seca.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tte.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.emp_id as emp_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,tt.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,c.course_short_name as course_short_name,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "emp.empcode as empcode,emp.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "pr.program_name as program_name,c.course_code as course_code,tt.batch_assignment_id as batch_assignment_id,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "CONCAT(pr.program_short_name,'-',ps.program_specialization_short_name) as ConcateProgAndspecialization,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.section_id as section_id From time_table tt "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join department dept on emp.dept_id=dept.dept_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id where tt.ac_year_id=?1 Order by tt.time_table_id DESC",nativeQuery =true)
	public List<Map<String,Object>> fetchAllTimeTableDetails(Integer ac_year_id);
	
	@Query(value ="Select tt.time_table_id as id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,tt.emp_id as emp_id,hr.room_name as room_name,"
			+ "(select (LENGTH(seca.student_ids) - LENGTH(REPLACE(seca.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tt.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.subject_assignment_id as subject_assignment_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "CONCAT((select s.section_name from section s where s.section_id IN(select seca.section_id from "
			+ "section_assignment seca where seca.section_assignment_id=tt.section_assignment_id)),' - ',(select seca.student_ids from "
			+ "section_assignment seca where seca.section_assignment_id=tt.section_assignment_id)) as section_with_student_ids,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name From time_table tt "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Inner join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join hostel_rooms hr on hr.hostel_room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id where tt.ac_year_id=?1",nativeQuery =true)
	public List<Map<String,Object>> fetchAllTimeTableDetailsForIndex(Integer ac_year_id);
	
	@Query(value ="Select tt.time_table_id as id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,tt.emp_id as emp_id,hr.room_name as room_name,"
			+ "(select (LENGTH(ba.student_ids) - LENGTH(REPLACE(ba.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tt.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "b.batch_name as batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "CONCAT((select s.batch_name from batch s where s.batch_id IN(select seca.batch_id from "
			+ "batch_assignment seca where seca.batch_assignment_id=tt.batch_assignment_id)),' - ',(select seca.student_ids from "
			+ "batch_assignment seca where seca.batch_assignment_id=tt.batch_assignment_id)) as batch_with_student_ids,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name From time_table tt "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Inner join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join hostel_rooms hr on hr.hostel_room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id where tt.ac_year_id=?1",nativeQuery =true)
	public List<Map<String,Object>> fetchAllBatchTimeTableDetailsForIndex(Integer ac_year_id);
	
	@Query(value = "Select tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.interval_type_id as intervalTypeId,tte.emp_id as emp_id,hr.roomName as roomName,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,"
			+ "tt.floor_id as floor_id,tte.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "c.course_code as course_code,c.course_short_name as course_short_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "tit.intervalTypeShort as intervalTypeShort,emp.employee_name as employee_name From TimeTableEmployee tte "
			+ "Left join TimeTable tt on tte.time_table_id=tt.time_table_id "
			+ "Left join Academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join Schools sch on sch.school_id=tt.school_id "
			+ "Left join Program pr on pr.program_id=tt.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join TimeIntervalTypes tit on tit.intervalTypeId=tt.interval_type_id "
			+ "Left join EmployeeDetails emp on emp.emp_id=tte.emp_id "
			+ "Left join SectionAssignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join Section s on s.section_id=seca.section_id "
			+ "Left join BatchAssignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join Batch b on b.batch_id=ba.batch_id "
			+ "Left join HostelRooms hr on hr.hostelRoomId=tt.room_id "
			+ "Left join TimeSlots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Left join SubjectAssignment sa on sa.subjetAssignId=tt.subject_assignment_id "
			+ "Left join Course c on c.course_id=sa.course_id where tte.emp_id=?1 and tte.selected_date >= ?2 and tt.section_assignment_id IS NOT NULL")
	public List<Map<String, Object>> getTimeTableDetailsForEmployees(Integer emp_id, Date selected_date);
	
	@Query(value ="Select c.course_code as course_code,c.course_name as course_name,c.course_short_name as course_short_name,emp.employee_name as employee_name,ir.roomcode as roomcode,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,tt.is_online as is_online,c.course_id as course_id,tt.time_table_id as time_table_id,"
			+ "tte.selected_date as date_of_class,s.section_name as section_name,b.batch_name as batch_name,tsl.starting_time as start_time,tsl.ending_time as end_time,"
			+ "tt.interval_type_id as interval_type_id,tit.interval_type_short as interval_type_short,tit.interval_type_name as interval_type_name,tt.attendance_status as attendance_status From time_table tt "
			+ "Inner join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id  "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Inner join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join infrastructure_rooms ir On ir.room_id=tt.room_id "
			+ "left join section_assignment sa on sa.section_assignment_id=tt.section_assignment_id "
			+ "Left join section s on s.section_id=sa.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id  "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id Where (tt.section_assignment_id=?1 OR tt.batch_assignment_id=?2) And "
			+ "date(tte.selected_date)=date(?3) And tt.active=true ",nativeQuery=true)
	public List<Map<String,Object>> timeTableDetailsOfStudent(Integer section_assignment_id,Integer batch_assignment_id, Date date);
	
	@Query(value = "Select sat.student_id As student_id,c.course_code as course_code,c.course_name as course_name,c.course_short_name as course_short_name, "
			+ "emp.employee_name as employee_name,ir.roomcode as roomcode,tt.selected_date as selected_date, "
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,tt.is_online as is_online,c.course_id as course_id,tt.time_table_id as time_table_id, "
			+ " tte.selected_date as date_of_class,s.section_name as section_name,b.batch_name as batch_name,tsl.starting_time as start_time,tsl.ending_time as end_time, "
			+ "tt.interval_type_id as interval_type_id,tit.interval_type_short as interval_type_short,tit.interval_type_name as interval_type_name, "
			+ "tt.active As active,"
			+ "tt.attendance_status as attendance_status,sat.present_status as present_status  "
			+ "From time_table tt  "
			+ "left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id  "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id   "
			+ "Left join course c on c.course_id=ca.course_id  "
			+ "left join time_table_employee tte on tte.time_table_id=tt.time_table_id  "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id  "
			+ "Left join infrastructure_rooms ir On ir.room_id=tt.room_id  "
			+ "left join section_assignment sa on sa.section_assignment_id=tt.section_assignment_id  "
			+ "Left join section s on s.section_id=sa.section_id  "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id   "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id 	 "
			+ "Left join student_attendance sat on (sat.time_table_id=tt.time_table_id And sat.student_id=?5) "
			+ "Left join batch b on b.batch_id=ba.batch_id  "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id  "
			+ "where (tt.section_assignment_id in (?1)  or tt.batch_assignment_id in (?2)) And month(tt.selected_date)=?3 And year(tt.selected_date)=?4 And tt.active=true ", nativeQuery = true)
	public List<Map<String, Object>> timeTableDetailsOfStudentWithAttendance1(List<Integer> section_assignment_id,List<Integer> batch_assignment_id, Integer month, Integer year, Integer student_id);
	
	@Modifying
	@Query( value = " update time_table_employee tte set tte.emp_id=?3,tte.subject_assignment_id=?4 where tte.time_table_id=?1 and tte.emp_id=?2 and tte.active=true",nativeQuery=true)
	public void updateEmployeeIdForSwapping(Integer time_table_id,Integer old_emp_id, Integer new_emp_id,Integer subject_assignment_id);
	
	@Modifying
	@Query( value = " update time_table tt set tt.subject_assignment_id=?2 where tt.time_table_id=?1 and tt.active=true",nativeQuery=true)
	public void updateSubjectAssignmentIdForSwapping(Integer time_table_id, Integer subject_assignment_id);
	
	@Query(value ="Select c.course_id as course_id,concat(c.course_name,'-',c.course_code) as course_name_code,"
			+ "tt.room_id as room_id,tt.subject_assignment_id as subject_assignment_id From time_table tt "
			+ "Inner join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "left join section_assignment sa on sa.section_assignment_id=tt.section_assignment_id "
			+ "Left join Section s on s.section_id=sa.section_id "			
			+ "Inner join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course c on c.course_id=sbja.course_id "
			+ "Where tte.emp_id=?1 and tt.ac_year_id=?2 and tt.school_id=?3 "
			+ "and tt.program_id=?4 and tt.program_specialization_id =?5 and s.section_id =?6 and tt.current_sem=?7 and tt.active=true group by c.course_id",nativeQuery=true)
	public List<Map<String,Object>> courseAssignedToEmployeeForStudentMarksOnSem(Integer emp_id, Integer ac_year_id,Integer school_id,
			Integer program_id,Integer program_specialization_id,Integer section_id,Integer current_sem);
	
	@Query(value ="Select c.course_id as course_id,concat(c.course_name,'-',c.course_code) as course_name_code,"
			+ "tt.room_id as room_id,tt.subject_assignment_id as subject_assignment_id From time_table tt "
			+ "Inner join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "left join section_assignment sa on sa.section_assignment_id=tt.section_assignment_id "
			+ "Left join section s on s.section_id=sa.section_id "
			+ "Inner join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course c on c.course_id=sbja.course_id "
			+ "Where tte.emp_id=?1 and tt.ac_year_id=?2 and tt.school_id=?3 "
			+ "and tt.program_id=?4 and tt.program_specialization_id=?5 and s.section_id =?6 and tt.current_year=?7 and tt.active=true group by c.course_id",nativeQuery=true)
	public List<Map<String,Object>> courseAssignedToEmployeeForStudentMarksOnYear(Integer emp_id, Integer ac_year_id,Integer school_id,
			Integer program_id,Integer program_specialization_id,Integer section_id,Integer current_year);
	
	@Query(value ="Select tt.time_table_id as id,concat(IfNull(ts.starting_time,' '),'-',IfNull(ts.ending_time,' '),' (', ifNull(c.course_code,' '),')') as title,"
			+ "tt.room_id as room_id,tt.section_assignment_id,tt.subject_assignment_id as subject_assignment_id,"
			+ "tt.interval_type_id as interval_type_id,tt.ac_year_id as ac_year_id,tt.block_id as block_id,"
			+ "tt.current_sem as current_sem,tt.current_year as current_year,tt.school_id as school_id,tt.week_day as week_day,"
			+ "tt.selected_date as start,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.time_slots_id as time_slots_id,ir.roomcode as roomcode,s.section_name as section_name,c.course_id as course_id,"
			+ "tit.interval_type_short as interval_type_short,c.course_short_name as course_short_name,"
			+ "ib.blockcode as blockcode,sch.school_name_short as school_name_short,c.course_name as course_name,c.course_code as course_code,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "concat(IfNull(ts.starting_time,' '),'-',IfNull(ts.ending_time,' ')) as time From time_table tt "
			+ "left join section_assignment sa on sa.section_assignment_id=tt.section_assignment_id "
			+ "Left join section s on s.section_id=sa.section_id "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course c on c.course_id=sbja.course_id "
			+ "Left join time_slots ts On ts.time_slots_id=tt.time_slots_id "
			+ "Left join infrastructure_rooms ir On ir.room_id=tt.room_id "
			+ "Left join time_interval_types tit On tit.interval_type_id=tt.interval_type_id "
			+ "Left join academic_year ay On ay.ac_year_id=tt.ac_year_id "
			+ "Left join infrastructure_blocks ib On ib.block_id=tt.block_id "
			+ "Left join schools sch On sch.school_id=tt.school_id "
			+ "Left join program pr On pr.program_id=tt.program_id "
			+ "Left join program_specialization ps On ps.program_specialization_id=tt.program_specialization_id "
			+ "Where tt.ac_year_id=?1 and tt.school_id=?2 "
			+ "and tt.program_id=?3 and tt.program_specialization_id =?4 and s.section_id =?5 and tt.current_sem=?6 and Month(tt.selected_date)=Month(?7) And Year(tt.selected_date)=Year(?7) and tt.active=true",nativeQuery=true)
	public List<Map<String,Object>> coursesAssignedDetailsForTimeTableViewOnSem(Integer ac_year_id,Integer school_id,
			Integer program_id,Integer program_specialization_id,Integer section_id,Integer current_sem,Date selected_date);
	
	@Query(value ="Select tt.time_table_id as id,concat(IfNull(ts.starting_time,' '),'-',IfNull(ts.ending_time,' '),' (', ifNull(c.course_code,' '),')') as title,"
			+ "tt.room_id as room_id,tt.section_assignment_id,tt.subject_assignment_id as subject_assignment_id,"
			+ "tt.interval_type_id as interval_type_id,tt.ac_year_id as ac_year_id,tt.block_id as block_id,"
			+ "tt.current_sem as current_sem,tt.current_year as current_year,tt.school_id as school_id,tt.week_day as week_day,"
			+ "tt.selected_date as start,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.time_slots_id as time_slots_id,ir.roomcode as roomcode,s.section_name as section_name,c.course_id as course_id,"
			+ "tit.interval_type_short as interval_type_short,c.course_short_name as course_short_name,"
			+ "ib.blockcode as blockcode,sch.school_name_short as school_name_short,c.course_name as course_name,c.course_code as course_code,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "concat(IfNull(ts.starting_time,' '),'-',IfNull(ts.ending_time,' ')) as time From time_table tt "
			+ "left join section_assignment sa on sa.section_assignment_id=tt.section_assignment_id "
			+ "Left join section s on s.section_id=sa.section_id "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course c on c.course_id=sbja.course_id "
			+ "Left join time_slots ts On ts.time_slots_id=tt.time_slots_id "
			+ "Left join infrastructure_rooms ir On ir.room_id=tt.room_id "
			+ "Left join time_interval_types tit On tit.interval_type_id=tt.interval_type_id "
			+ "Left join academic_year ay On ay.ac_year_id=tt.ac_year_id "
			+ "Left join infrastructure_blocks ib On ib.block_id=tt.block_id "
			+ "Left join schools sch On sch.school_id=tt.school_id "
			+ "Left join program pr On pr.program_id=tt.program_id "
			+ "Left join program_specialization ps On ps.program_specialization_id=tt.program_specialization_id "
			+ "Where tt.ac_year_id=?1 and tt.school_id=?2 "
			+ "and tt.program_id=?3 and tt.program_specialization_id=?4 and s.section_id =?5 and tt.current_year=?6 and Month(tt.selected_date)=Month(?7)And Year(tt.selected_date)=Year(?7) and tt.active=true ",nativeQuery=true)
	public List<Map<String,Object>> coursesAssignedDetailsForTimeTableViewOnYear(Integer ac_year_id,Integer school_id,
			Integer program_id,Integer program_specialization_id,Integer section_id,Integer current_year,Date selected_date);
	
	@Query(value = "select Distinct(tt.room_id) from time_table tt where tt.room_id IN "
			+ "(select ir.room_id from infrastructure_rooms ir where ir.facility_type_id=?1 and ir.active=true)",nativeQuery=true)
	public List<Integer> getAllRoomIds(Integer facility_type_id);
	
	@Query(value ="Select tte.time_table_employee_id as time_table_employee_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,ir.roomcode as roomcode,tt.time_table_id as time_table_id,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tte.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.emp_id as emp_id,tt.batch_assignment_id as batch_assignment_id,"
			+ "tt.floor_id as floor_id,"
			+ "CASE "
	        + "   WHEN tt.current_sem = 1 THEN 1 "
	        + "   WHEN tt.current_sem = 2 THEN 1 "
	        + "   WHEN tt.current_sem = 3 THEN 2 "
	        + "   WHEN tt.current_sem = 4 THEN 2 "
	        + "   WHEN tt.current_sem = 5 THEN 3 "
	        + "   WHEN tt.current_sem = 6 THEN 3 "
	        + "   WHEN tt.current_sem = 7 THEN 4 "
	        + "   WHEN tt.current_sem = 8 THEN 4 "
	        + "   WHEN tt.current_sem = 9 THEN 5 "
	        + "   WHEN tt.current_sem = 10 THEN 5 "
	        + "   WHEN tt.current_sem = 11 THEN 6 "
	        + "   WHEN tt.current_sem = 12 THEN 6 "
	        + "   ELSE tt.current_year "
	        + " END as current_year,tt.current_sem as current_sem,tte.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,c.course_code As course_code,"
			+ "s.section_name as section_name,b.batch_name as batch_name,c.course_short_name as course_short_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,tsl.starting_time as starting_time,tsl.ending_time as ending_time,"
			+ "tit.interval_type_short as interval_type_short,tit.interval_type_name as interval_type_name,emp.employee_name as employee_name,pr.program_name as program_name,"
			+ "ps.program_specialization_name as program_specialization_name,sch.school_name as school_name,c.course_id as course_id,"
			+ "s.section_id as section_id,b.batch_id as batch_id,ca.course_assignment_id as course_assignment_id,tt.attendance_status as attendance_status, "
			+ "tsl.starting_time as start_time,tsl.ending_time as end_time,tte.selected_date as date_of_class,pt.program_type_name as program_type_name  From time_table tt "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program_assignment pas on pas.program_assignment_id=tt.program_assignment_id "
			+ "Left join program_type pt on pt.program_type_id=pas.program_type_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id where tte.emp_id=?1 And tt.active=true",nativeQuery =true)
	public List<Map<String,Object>> timeTableDetailsOfUserForCalender(Integer emp_id);

	@Query(value = "SELECT c.course_code AS course_code, c.course_name AS course_name, c.course_short_name AS course_short_name, "
	        + "emp.employee_name AS employee_name, ir.roomcode AS roomcode, emp.emp_id AS emp_id, "
	        + "tt.program_assignment_id AS program_assignment_id, tte.selected_date AS selected_date, tte.created_date AS created_date, "
	        + "ca.course_assignment_coursecode AS course_assignment_coursecode, ca.course_assignment_id AS course_assignment_id, "
	        + "emp.empcode AS empcode, tte.created_by AS created_by, tte.created_username AS created_username, "
	        + "tit.interval_type_short AS interval_type_short, CONCAT(tsl.starting_time, ' - ', tsl.ending_time) AS timeSlots, "
	        + "tt.is_online AS is_online, c.course_id AS course_id, tt.time_table_id AS time_table_id, "
	        + "tt.attendance_status AS attendance_status, sa.present_status AS present_status,sa.student_attendance_id As student_attendance_id,sa.student_id As student_id "
	        + "FROM time_table tt "
	        + "INNER JOIN subject_assignment sbja ON sbja.subjet_assign_id = tt.subject_assignment_id "
	        + "LEFT JOIN course_assignment ca ON ca.course_assignment_id = sbja.course_assignment_id "
	        + "LEFT JOIN course c ON c.course_id = ca.course_id "
	        + "INNER JOIN time_table_employee tte ON tte.time_table_id = tt.time_table_id "
	        + "LEFT JOIN employee_details emp ON emp.emp_id = tte.emp_id "
	        + "LEFT JOIN infrastructure_rooms ir ON ir.room_id = tt.room_id "
	        + "LEFT JOIN time_interval_types tit ON tit.interval_type_id = tt.interval_type_id "
	        + "LEFT JOIN time_slots tsl ON tsl.time_slots_id = tt.time_slots_id "
	        + "LEFT JOIN student_attendance sa ON sa.time_table_id = tt.time_table_id  And sa.time_slots_id=tt.time_slots_id And sa.student_id =?1 "
	        + "WHERE (tt.section_assignment_id IN (?2) OR tt.batch_assignment_id IN (?3)) "
	        + "AND tt.active = true", nativeQuery = true)
	public List<Map<String, Object>> timeTableDetailsOfStudentForWeb(Integer studentIds, List<Integer> section_assignment_id, List<Integer> batch_assignment_id);

	@Query(value="select t.time_table_id as time_table_id,t.interval_type_id as interval_type_id, "
			+ "t.selected_date as selected_date, "
			+ "CASE "
	        + "   WHEN t.current_sem = 1 THEN 1 "
	        + "   WHEN t.current_sem = 2 THEN 1 "
	        + "   WHEN t.current_sem = 3 THEN 2 "
	        + "   WHEN t.current_sem = 4 THEN 2 "
	        + "   WHEN t.current_sem = 5 THEN 3 "
	        + "   WHEN t.current_sem = 6 THEN 3 "
	        + "   WHEN t.current_sem = 7 THEN 4 "
	        + "   WHEN t.current_sem = 8 THEN 4 "
	        + "   WHEN t.current_sem = 9 THEN 5 "
	        + "   WHEN t.current_sem = 10 THEN 5 "
	        + "   WHEN t.current_sem = 11 THEN 6 "
	        + "   WHEN t.current_sem = 12 THEN 6 "
	        + "   ELSE t.current_year "
	        + " END as current_year,t.current_sem as current_sem,"
			+ " t.section_assignment_id as section_assignment_id,t.batch_assignment_id as batch_assignment_id,"
			+ " t.subject_assignment_id as subject_assignment_id,CONCAT(tsl.starting_time,' - ',tsl.ending_time) as intervals,"
			+ " t.school_id as institute_id,t.program_assignment_id as program_assignment_id,"
			+ " t.program_specialization_id as course_branch_assignment_id,"
			+ " t.program_id as  course_assignment_id, "
			+ " tsl.starting_time as start_time,tsl.ending_time as end_time, "
			+ " emp.empcode as empID, c.course_short_name as subject_name_short,"
			+ " c.course_id as subject_id , sch.school_name_short as institute_name_short,"
			+ " dp.dept_id as branch_id,dp.dept_name as branch_name,"
			+ " ps.program_specialization_name as course_branch_short_name,"
			+ " ifNull(b.batch_short_name,'NA') as batch_short_name,"
			+ " p.program_short_name as course_short_name,"
			+ " DATE_FORMAT(STR_TO_DATE(tsl.starting_time, '%h:%i %p'), '%H:%i:%s.0000000') as sTime,"
			+ " DATE_FORMAT(STR_TO_DATE(tsl.ending_time, '%h:%i %p'), '%H:%i:%s.0000000') as eTime,"
			+ " emp.employee_name as presenter_name, "
			+ " sch.school_name as institute_name,t.is_online as online,tsl.duration as duration,"
			+ " ifNull(s.section_name,'NA') as section_short_name "
			+ "from time_table t "
			+ " left join time_slots tsl on tsl.time_slots_id=t.time_slots_id "
			+ " left join time_table_employee tte on tte.time_table_id=t.time_table_id "
			+ "	left join employee_details emp on emp.emp_id=tte.emp_id "
			+ " left join department dp on dp.dept_id=emp.dept_id "
			+ " left join batch_assignment ba on ba.batch_assignment_id=t.batch_assignment_id"
			+ " left join batch b on b.batch_id=ba.batch_id "
			+ "	Left join section_assignment seca on seca.section_assignment_id=t.section_assignment_id "	
			+ "	Left join section s on s.section_id=seca.section_id "
			+ " Left join subject_assignment sa on sa.subjet_assign_id=t.subject_assignment_id "
			+ " Left join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ " Left join course c on c.course_id=ca.course_id "
			+ " left join schools sch on sch.school_id=t.school_id"
			+ " left join program_specialization ps on ps.program_specialization_id=t.program_specialization_id "
			+ " left join program p on p.program_id=ps.program_id "
			+ "where date(t.selected_date)=date(:date) and tte.emp_id=:emp_id",nativeQuery = true)
	public List<Map<String,Object>> fetchTimeTableDetailsBySelectDateForEmployees(@Param("date") Date selectedDate,@Param("emp_id") Integer emp_id);

	@Query(value=" select pt.program_type_name from student_details s "
			+ "left join program_assignment pa on pa.program_assignment_id=s.program_assignment_id "
			+ "and pa.program_id=s.program_id "
			+ "left join program_type pt on pt.program_type_id=pa.program_type_id "
			+ "where s.student_id=?1 ",nativeQuery=true)
	public String getCurrentYearOrSem(Integer student_id);

	@Query(value="select t.time_table_id as time_table_id,t.interval_type_id as interval_type_id, "
			+ "t.selected_date as selected_date, "
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "rs.current_year as current_year,rs.current_sem as current_sem, "
			+ "t.section_assignment_id as section_assignment_id,t.batch_assignment_id as batch_assignment_id, "
			+ " t.subject_assignment_id as subject_assignment_id,t.intervals as intervals,"
			+ " t.school_id as institute_id,t.program_assignment_id as program_assignment_id,"
			+ " t.program_specialization_id as course_branch_assignment_id,"
			+ " t.program_id as  course_assignment_id, "
			+ " tsl.starting_time as startTime,tsl.ending_time as endTime, "
			+ "  c.course_short_name as subject_short_name, "
			+ " c.course_id as subject_id , sch.school_name_short as institute_name_short,"
			+ " ps.program_specialization_name as course_branch_short_name,"
			+ " b.batch_short_name as batch_short_name,"
			+ " p.program_short_name as course_short_name,  "
			+ " sch.school_name as institute_name "
			+ "from reporting_students rs  "
			+ " left join batch_assignment ba on ba.current_year=rs.current_year "
			+ " left join section_assignment sa on sa.current_year_sem=rs.current_year "
			+ " inner join time_table t on t.batch_assignment_id=ba.batch_assignment_id and t.section_assignment_id=sa.section_assignment_id "
			+ " left join time_slots tsl on tsl.time_slots_id=t.time_slots_id "
			+ " left join course_student_assignment sb on sb.student_id=rs.student_id"
			+ " left join batch b on b.batch_id=ba.batch_id "
			+ " left join course c on c.course_id=sb.course_id "
			+ " left join schools sch on sch.school_id=t.school_id"
			+ " left join program_specialization ps on ps.program_specialization_id=t.program_specialization_id "
			+ " left join program p on p.program_id=ps.program_id "
			+ "where t.selected_date=:date and rs.student_id=:student_id ",nativeQuery=true)
	public List<Map<String, Object>> fetchTimeTableDetailsBySelectDateAndCurrentYearForStudents(@Param("date") String date,@Param("student_id") Integer student_id);

	@Query(value="select t.time_table_id as time_table_id,t.interval_type_id as interval_type_id, "
			+ "t.selected_date as selected_date, "
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "rs.current_year as current_year,rs.current_sem as current_sem, "
			+ "t.section_assignment_id as section_assignment_id,t.batch_assignment_id as batch_assignment_id, "
			+ " t.subject_assignment_id as subject_assignment_id,t.intervals as intervals,"
			+ " t.school_id as institute_id,"
			+ " t.program_specialization_id as course_branch_assignment_id,t.program_assignment_id as program_assignment_id,"
			+ " t.program_id as  course_assignment_id, "
			+ " tsl.starting_time as startTime,tsl.ending_time as endTime, "
			+ "  c.course_short_name as subject_short_name, "
			+ " c.course_id as subject_id , sch.school_name_short as institute_name_short,"
			+ " ps.program_specialization_name as course_branch_short_name,"
			+ " b.batch_short_name as batch_short_name,"
			+ " p.program_short_name as course_short_name, "
			+ " sch.school_name as institute_name "
			+ " from reporting_students rs "
			+ " left join batch_assignment ba on ba.current_year=rs.current_year "
			+ " left join section_assignment sa on sa.current_year_sem=rs.current_year "
			+ " inner join time_table t on t.batch_assignment_id=ba.batch_assignment_id and t.section_assignment_id=sa.section_assignment_id "
			+ " left join time_slots tsl on tsl.time_slots_id=t.time_slots_id "
			+ " left join course_student_assignment sb on sb.student_id=rs.student_id"
			+ " left join batch b on b.batch_id=ba.batch_id "
			+ " left join course c on c.course_id=sb.course_id "
			+ " left join schools sch on sch.school_id=t.school_id"
			+ " left join program_specialization ps on ps.program_specialization_id=t.program_specialization_id "
			+ " left join program p on p.program_id=ps.program_id "
			+ "where t.selected_date=:date and rs.student_id=:student_id ",nativeQuery=true)
	public List<Map<String, Object>> fetchTimeTableDetailsBySelectDateAndCurrentSemForStudents(@Param("date") String date,@Param("student_id") Integer student_id);

	@Query(value="select distinct new com.au.dto.BatchByAcademicYear( ba.batch_assignment_id as batch_assignment_id, "
			+ " CASE "
	        + "   WHEN t.current_sem = 1 THEN 1 "
	        + "   WHEN t.current_sem = 2 THEN 1 "
	        + "   WHEN t.current_sem = 3 THEN 2 "
	        + "   WHEN t.current_sem = 4 THEN 2 "
	        + "   WHEN t.current_sem = 5 THEN 3 "
	        + "   WHEN t.current_sem = 6 THEN 3 "
	        + "   WHEN t.current_sem = 7 THEN 4 "
	        + "   WHEN t.current_sem = 8 THEN 4 "
	        + "   WHEN t.current_sem = 9 THEN 5 "
	        + "   WHEN t.current_sem = 10 THEN 5 "
	        + "   WHEN t.current_sem = 11 THEN 6 "
	        + "   WHEN t.current_sem = 12 THEN 6 "
	        + "   ELSE t.current_year "
	        + " END as current_year,t.current_sem as current_sem,"
			+ "p.program_short_name as course_name_short,"
			+ " ba.remarks as remarks,"
			+ " b.batch_short_name as batch_short_name, "
			+ "b.batch_name as batch_name ) from TimeTable t"
			+ " Inner join TimeTableEmployee tte on tte.time_table_id=t.time_table_id "
			+ " Inner join BatchAssignment ba on ba.batch_assignment_id=t.batch_assignment_id "
			+ " left join Batch b on b.batch_id=ba.batch_id "
			+ " left join Program p on p.program_id=ba.program_id "
			+ " left join ProgramSpecilization ps on ps.program_specialization_id=ba.program_specialization_id "
			+ "where t.ac_year_id=:academicYear and tte.emp_id=:empId group by ba.batch_assignment_id ")
	public List<BatchByAcademicYear> getBatchByAcademicYearAndEmployeeId(@Param("academicYear") Integer academicYear,@Param("empId") Integer empId);

	@Query(value="select distinct new com.au.dto.SectionByAcademicYear( "
			+ "  sa.section_assignment_id as section_assignment_id,"
			+ "  sa.section_id as section_id, "
			+ "  t.program_id as course_assignment_id,"
			+ "  t.program_specialization_id as course_branch_assignment_id,"
			+ "  ps.program_specialization_short_name as course_branch_short_name,"
			+ "  p.program_short_name as course_short_name,"
			+ "  t.ac_year_id as ac_year_id,"
			+ "  s.section_name as section_short_name,"
			+ "  CASE "
	        + "   WHEN t.current_sem = 1 THEN 1 "
	        + "   WHEN t.current_sem = 2 THEN 1 "
	        + "   WHEN t.current_sem = 3 THEN 2 "
	        + "   WHEN t.current_sem = 4 THEN 2 "
	        + "   WHEN t.current_sem = 5 THEN 3 "
	        + "   WHEN t.current_sem = 6 THEN 3 "
	        + "   WHEN t.current_sem = 7 THEN 4 "
	        + "   WHEN t.current_sem = 8 THEN 4 "
	        + "   WHEN t.current_sem = 9 THEN 5 "
	        + "   WHEN t.current_sem = 10 THEN 5 "
	        + "   WHEN t.current_sem = 11 THEN 6 "
	        + "   WHEN t.current_sem = 12 THEN 6 "
	        + "   ELSE t.current_year "
	        + " END as current_year,t.current_sem as current_sem ) from TimeTable t"
			+ " Inner join TimeTableEmployee tte on tte.time_table_id=t.time_table_id  "
			+ " Inner join SectionAssignment sa on sa.section_assignment_id=t.section_assignment_id"
			+ " left join Section  s on s.section_id=sa.section_id "
			+ " left join ProgramSpecilization ps on ps.program_specialization_id=t.program_specialization_id"
			+ " left join Program p on p.program_id=t.program_id   "
			+ " where t.ac_year_id=:academicYear and tte.emp_id=:empId group by sa.section_assignment_id ")
	public List<SectionByAcademicYear> getSectionByAcademicYearAndEmployeeId(@Param("academicYear") Integer academicYear,@Param("empId") Integer empId);

	@Query(value="select new map(t.batch_assignment_id as assignment_id,b.batch_name as name, ba.student_ids as student_ids ) from TimeTable t "
			+ " left join BatchAssignment ba on ba.batch_assignment_id=t.batch_assignment_id "
			+ " left join Batch b on b.batch_id=ba.batch_id  "
			+ " where t.time_table_id=:time_table_id")
	public HashMap<String, Object> getBatchAssigmentNameandId(@Param("time_table_id") Integer timetableId);
	
	@Modifying
	@Query( value = " update time_table tt set tt.room_id=?2 where tt.time_table_id=?1 ",nativeQuery=true)
	public void updateRoomForSwapping(Integer timeTableId, Integer roomId);
	
	@Query(value="select new map(tt.attendance_status as attendance_status,tt.time_table_id as time_table_id) from TimeTable tt  where tt.time_table_id in (?1) And tt.active=true")
	public List<Map<String,Object>> checkAttendanceStatus(List<Integer> timeTableIds);
	
	
	@Modifying
	@Query(value = "update time_table tt set tt.attendance_status=1 "
			+ "where tt.time_table_id in (?1) And tt.active=true",nativeQuery=true)
	public void updateAttendanceStatus(List<Integer> timeTableIds);

//	@Query(value="Select cast(count(time_table_id) as UNSIGNED) From time_table tt "
//			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
//			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
//			+ "left Join course c on c.course_id=ca.course_id "
//			+ "Where tt.ac_year_id=?1 And (tt.section_assignment_id in (?2) or tt.batch_assignment_id in (?3)) "
//			+ "And tt.current_sem=?4 And c.course_id=?5 And tt.attendance_status =1 And tt.active=true",nativeQuery=true)
//	public Integer totalClassOfCourseBySemBySectionAssignmentAndBatchAssignment(Integer acYearId, List<Integer> sectionAssignmentIds,List<Integer> batchAssignmentIds, Integer currentYearSem,Integer courseId);


	@Query(value="Select cast(count(time_table_id) as UNSIGNED) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Where tt.ac_year_id=?1 And (tt.section_assignment_id in (?2) or tt.batch_assignment_id in (?3)) "
			+ "And tt.current_year=?4 And c.course_id=?5 And tt.attendance_status =1 And tt.active=true",nativeQuery=true)
	public Integer totalClassOfCourseByYearBySectionAssignmentAndBatchAssignment(Integer acYearId, List<Integer> sectionAssignmentIds,List<Integer> batchAssignmentIds, Integer currentYearSem,Integer courseId);


	@Query(value="Select cast(count(time_table_id) as UNSIGNED) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Where tt.ac_year_id=?1 And (tt.section_assignment_id in (?2) or tt.batch_assignment_id in (?3)) "
			+ "And tt.current_year=?4 And c.course_id=?5 And tt.attendance_status =1 And tt.active=true",nativeQuery=true)
	public Integer totalClassOfCourseBySemBySectionAssignmentAndBatchAssignment(Integer currentAcademicYearId,
			List<Integer> sectionAssignmentIds, List<Integer> batchAssignmentIds, Integer year_or_sem, Integer integer);
	
	@Query(value ="Select c.course_code as course_code,c.course_name as course_name,c.course_short_name as subject_name_short,emp.employee_name as employee_name,ir.roomcode as roomcode,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as intervals,tt.is_online as online,c.course_id as subject_id,tt.time_table_id as time_table_id,"
			+ "tte.selected_date as selected_date,ifNull(s.section_name,'NA') as section_name,b.batch_name as batch_name,ifNull(b.batch_short_name,'NA') as batch_short_name,tsl.starting_time as start_time,tsl.ending_time as end_time,"
			+ "tt.interval_type_id as interval_type_id,tit.interval_type_short as interval_type_short,tit.interval_type_name as interval_type_name,"
			+ "sd.program_specialization_id as course_branch_assignment_id,rs.current_year as current_year,rs.current_sem as current_sem,"
			+ "ca.course_assignment_id as course_assignment_id,tt.section_assignment_id as section_assignment_id,"
			+ " DATE_FORMAT(STR_TO_DATE(tsl.starting_time, '%h:%i %p'), '%H:%i:%s.0000000') as sTime,"
			+ " DATE_FORMAT(STR_TO_DATE(tsl.ending_time, '%h:%i %p'), '%H:%i:%s.0000000') as eTime,"
			+ "tt.batch_assignment_id as batch_assignment_id,tt.subject_assignment_id as subject_assignment_id,tsl.duration as duration,"
			+ "ps.program_specialization_short_name as course_branch_short_name,p.program_short_name as course_short_name From time_table tt "
			+ "Inner join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id  "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join infrastructure_rooms ir On ir.room_id=tt.room_id "
			+ "left join section_assignment sa on sa.section_assignment_id=tt.section_assignment_id "
			+ "Left join section s on s.section_id=sa.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Inner join student_details sd on (sd.student_id=?5 And sd.active=true) "
			+ "Left join reporting_students rs on (rs.student_id=sd.student_id and rs.active=true) "
			+ "Left join program_specialization ps on ps.program_specialization_id=sd.program_specialization_id "
			+ "Left join program p on p.program_id=sd.program_id "
			+ "Where (tt.section_assignment_id in (?1) OR tt.batch_assignment_id in (?2)) "
			+ "And (date(tt.selected_date) between date(?3) And date(?4)) And tt.active=true ",nativeQuery=true)
	public List<Map<String,Object>> timeTableDetailsOfStudentForLMS(List<Integer> sectionAssignmentId,List<Integer> batchAssignmentId, Date startingdate, Date endingdate,Integer studentId);

	@Query(value="Select cast(count(time_table_id) as UNSIGNED) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Where tt.ac_year_id=?1 And tt.batch_assignment_id=?2 "
			+ "And tt.current_sem=?3 And c.course_id=?4 And tt.attendance_status =1 And tt.active=true",nativeQuery=true)
	public Integer totalClassOfCourseBySemAndBatchAssignemntId(Integer acYearId, Integer batchAssignmentId, Integer currentYearSem,Integer courseId);

	@Query(value="Select cast(count(time_table_id) as UNSIGNED) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Where tt.ac_year_id=?1 And tt.section_assignment_id in (?2) "
			+ "And tt.current_sem=?3 And c.course_id=?4 And tt.attendance_status =1 And tt.active=true",nativeQuery=true)
	public Integer totalClassOfCourseBySemAndSectionAssignemntId(Integer acYearId, List<Integer> sectionAssignmentId, Integer currentYearSem,Integer courseId);

	@Query(value="Select cast(count(time_table_id) as UNSIGNED) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Where tt.ac_year_id=?1 And tt.section_assignment_id  in (?2) "
			+ "And tt.current_year=?3 And c.course_id=?4 And tt.attendance_status =1 And tt.active=true",nativeQuery=true)
	public Integer totalClassOfCourseByYearAndSectionAssignemntId(Integer acYearId, List<Integer> sectionAssignmentId, Integer currentYearSem,Integer courseId);

	@Query(value="Select cast(count(time_table_id) as UNSIGNED) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Where tt.ac_year_id=?1 And tt.batch_assignment_id=?2 "
			+ "And tt.current_year=?3 And c.course_id=?4 And tt.attendance_status =1 And tt.active=true",nativeQuery=true)
	public Integer totalClassOfCourseByYearAndBatchAssignemntId(Integer acYearId, Integer batchAssignmentId, Integer currentYearSem,Integer courseId);
	
	@Query(value="select tt.attendance_status as attendance_status,tt.time_table_id as time_table_id "
			+ "from time_table tt  where tt.time_table_id=?1 And tt.active=true",nativeQuery =true)
	public Map<String, Object> checkAttendanceStatus(Integer time_table_id);
	
	@Query(value ="Select tte.time_table_employee_id as time_table_employee_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,ir.roomcode as roomcode,tt.time_table_id as time_table_id,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tte.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.program_assignment_id as program_assignment_id,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.emp_id as emp_id,tt.batch_assignment_id as batch_assignment_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,tt.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,c.course_id As course_id,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,c.course_code As course_code,"
			+ "s.section_name as section_name,b.batch_name as batch_name,c.course_short_name as course_short_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,tsl.starting_time as starting_time,tsl.ending_time as ending_time,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name,pr.program_name as program_name,"
			+ "ps.program_specialization_name as program_specialization_name,sch.school_name as school_name,emp.empcode as empcode,"
			+ "ib.blockcode as blockcode,seca.student_ids as section_assigned_student_ids,ba.student_ids as batch_assigned_student_ids From time_table tt "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join infrastructure_blocks ib On ib.block_id=tt.block_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id where tte.time_table_id=?1 And tte.emp_id=?2",nativeQuery =true)
	public List<Map<String,Object>> fetchDetailsOfFacultyByTimeTableId(Integer time_table_id,Integer emp_id);
	
	@Query(value="select t.time_table_id as time_table_id,t.interval_type_id as interval_type_id, "
			+ "t.selected_date as selected_date, "
			+ "CASE "
	        + "   WHEN t.current_sem = 1 THEN 1 "
	        + "   WHEN t.current_sem = 2 THEN 1 "
	        + "   WHEN t.current_sem = 3 THEN 2 "
	        + "   WHEN t.current_sem = 4 THEN 2 "
	        + "   WHEN t.current_sem = 5 THEN 3 "
	        + "   WHEN t.current_sem = 6 THEN 3 "
	        + "   WHEN t.current_sem = 7 THEN 4 "
	        + "   WHEN t.current_sem = 8 THEN 4 "
	        + "   WHEN t.current_sem = 9 THEN 5 "
	        + "   WHEN t.current_sem = 10 THEN 5 "
	        + "   WHEN t.current_sem = 11 THEN 6 "
	        + "   WHEN t.current_sem = 12 THEN 6 "
	        + "   ELSE t.current_year "
	        + " END as current_year,t.current_sem as current_sem,"
			+ "t.section_assignment_id as section_assignment_id,t.batch_assignment_id as batch_assignment_id, "
			+ " t.subject_assignment_id as subject_assignment_id,CONCAT(tsl.starting_time,' - ',tsl.ending_time) as intervals,"
			+ " t.school_id as institute_id,t.program_assignment_id as program_assignment_id,"
			+ " t.program_specialization_id as course_branch_assignment_id,"
			+ " t.program_id as  course_assignment_id, "
			+ " tsl.starting_time as start_time,tsl.ending_time as end_time, "
			+ " emp.empcode as empID, c.course_short_name as subject_name_short,"
			+ " c.course_id as subject_id , sch.school_name_short as institute_name_short,"
			+ " dp.dept_id as branch_id,dp.dept_name as branch_name,"
			+ " ps.program_specialization_name as course_branch_short_name,"
			+ " ifNull(b.batch_short_name,'NA') as batch_short_name,"
			+ " p.program_short_name as course_short_name,"
			+ " DATE_FORMAT(STR_TO_DATE(tsl.starting_time, '%h:%i %p'), '%H:%i:%s.0000000') as sTime,"
			+ " DATE_FORMAT(STR_TO_DATE(tsl.ending_time, '%h:%i %p'), '%H:%i:%s.0000000') as eTime,"
			+ " emp.employee_name as presenter_name, "
			+ " sch.school_name as institute_name,t.is_online as online,tsl.duration as duration,"
			+ " ifNull(s.section_name,'NA') as section_short_name "
			+ "from time_table t "
			+ " left join time_slots tsl on tsl.time_slots_id=t.time_slots_id "
			+ " left join time_table_employee tte on tte.time_table_id=t.time_table_id "
			+ "	left join employee_details emp on emp.emp_id=tte.emp_id "
			+ " left join department dp on dp.dept_id=emp.dept_id "
			+ " left join batch_assignment ba on ba.batch_assignment_id=t.batch_assignment_id"
			+ " left join batch b on b.batch_id=ba.batch_id "
			+ "	Left join section_assignment seca on seca.section_assignment_id=t.section_assignment_id "	
			+ "	Left join section s on s.section_id=seca.section_id "
			+ " Left join subject_assignment sa on sa.subjet_assign_id=t.subject_assignment_id "
			+ " Left join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ " Left join course c on c.course_id=ca.course_id "
			+ " left join schools sch on sch.school_id=t.school_id"
			+ " left join program_specialization ps on ps.program_specialization_id=t.program_specialization_id "
			+ " left join program p on p.program_id=ps.program_id "
			+ "where (date(t.selected_date) between date(:startingDate) And date(:endingDate)) and tte.emp_id=:emp_id And t.active=true",nativeQuery = true)
	public List<Map<String,Object>> fetchTimeTableDetailsBetweenBySelectDateForEmployees(@Param("startingDate") Date startingDate,@Param("endingDate") Date endingDate,@Param("emp_id") Integer emp_id);

	@Query(value="Select distinct count(*) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Inner join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join infrastructure_rooms ir On ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Where tt.ac_year_id=?1 And tt.current_sem=?2 And tt.section_assignment_id in (?3) "
			+ "And ca.course_assignment_id=?4 And tt.time_table_id not in (?5) And tt.attendance_status =1 And tt.active=true ",nativeQuery=true)
	public Integer absentDetailCountOfStudentForSetionBySemWithCourseAssignemtId(Integer acYearId,Integer currentYearOrSem,List<Integer> sectionAssignmentIds, Integer courseAssignemntId, List<Integer> timeTableIds);

	@Query(value="Select distinct count(*) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Inner join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join infrastructure_rooms ir On ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Where tt.ac_year_id=?1 And tt.current_year=?2 And tt.section_assignment_id in (?3) "
			+ "And ca.course_assignment_id=?4 And tt.time_table_id not in (?5) And tt.attendance_status =1 And tt.active=true ",nativeQuery=true)
	public Integer absentDetailCountOfStudentForSetionByYearWithCourseAssignemtId(Integer acYearId,Integer currentYearOrSem,
			List<Integer> sectionAssignmentIds, Integer courseAssignemntId, List<Integer> timeTableIds);

	@Query(value="Select distinct tt.current_sem as year_or_sem,tt.time_slots_id as time_slots_id,"
			+ "tsl.starting_time as starting_time,tsl.ending_time as ending_time,"
			+ "concat(tsl.starting_time,'-',tsl.ending_time) as time_slot,ir.roomcode as roomcode,"
			+ "b.batch_short_name as batch_short_name,s.section_name as section_name,"
			+ "tt.selected_date as selected_date,tit.interval_type_short as interval_type_short,"
			+ "group_concat(concat(emp.employee_name,'-',emp.empcode)) as employee_name_code,group_concat(tte.emp_id) as emp_id,"
			+ "tt.time_table_id as time_table_id,c.course_id as course_id,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_name as course_name,"
			+ "concat(c.course_name,'-',ca.course_assignment_coursecode) as course_name_with_course_assignment_code From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Inner join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join infrastructure_rooms ir On ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Where tt.ac_year_id=?1 And tt.current_sem=?2 And tt.section_assignment_id in (?3) "
			+ "And ca.course_assignment_id=?4 And tt.time_table_id not in (?5) And tt.attendance_status =1 And tt.active=true group by tt.time_table_id",nativeQuery=true)
	public List<Map<String, Object>> absentDetailOfStudentForSetionBySemWithCourseAssignemtId(Integer acYearId,Integer currentYearOrSem,List<Integer> sectionAssignmentIds, Integer courseAssignemntId, List<Integer> timeTableIds);

	@Query(value="Select distinct tt.current_year as year_or_sem,tt.time_slots_id as time_slots_id,"
			+ "tsl.starting_time as starting_time,tsl.ending_time as ending_time,"
			+ "concat(tsl.starting_time,'-',tsl.ending_time) as time_slot,ir.roomcode as roomcode,"
			+ "b.batch_short_name as batch_short_name,s.section_name as section_name,"
			+ "tt.selected_date as selected_date,tit.interval_type_short as interval_type_short,"
			+ "group_concat(concat(emp.employee_name,'-',emp.empcode)) as employee_name_code,group_concat(tte.emp_id) as emp_id,"
			+ "tt.time_table_id as time_table_id,c.course_id as course_id,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_name as course_name,"
			+ "concat(c.course_name,'-',ca.course_assignment_coursecode) as course_name_with_course_assignment_code From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Inner join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join infrastructure_rooms ir On ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Where tt.ac_year_id=?1 And tt.current_year=?2 And tt.section_assignment_id in (?3) "
			+ "And ca.course_assignment_id=?4 And tt.time_table_id not in (?5) And tt.attendance_status =1 And tt.active=true group by tt.time_table_id",nativeQuery=true)
	public List<Map<String, Object>> absentDetailOfStudentForSetionByYearWithCourseAssignemtId(Integer acYearId,Integer currentYearOrSem,
			List<Integer> sectionAssignmentIds, Integer courseAssignemntId, List<Integer> timeTableIds);
	
	@Query(value ="Select tte.time_table_employee_id as id,tt.time_table_id as time_table_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,ir.roomcode as roomcode,tt.program_assignment_id as program_assignment_id,"
	//		+ "(select (LENGTH(seca.student_ids) - LENGTH(REPLACE(seca.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tte.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.emp_id as emp_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,tt.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,c.course_short_name as course_short_name,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "emp.empcode as empcode,emp.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "pr.program_name as program_name,c.course_code as course_code,tt.batch_assignment_id as batch_assignment_id,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "CONCAT(pr.program_short_name,'-',ps.program_specialization_short_name) as ConcateProgAndspecialization,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.section_id as section_id From time_table tt "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join department dept on emp.dept_id=dept.dept_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id where tt.ac_year_id=?1 And tt.created_by=?2 Order by tt.time_table_id DESC",nativeQuery =true)
	public List<Map<String,Object>> fetchAllTimeTableDetailsBasedOnAcYearIdAndUserId(Integer ac_year_id ,Integer user_id);
	
	
	@Query(value ="Select tte.time_table_employee_id as id,tt.time_table_id as time_table_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,ir.roomcode as roomcode,tt.program_assignment_id as program_assignment_id,"
	//		+ "(select (LENGTH(seca.student_ids) - LENGTH(REPLACE(seca.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tte.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.emp_id as emp_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,tt.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,c.course_short_name as course_short_name,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "emp.empcode as empcode,emp.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "pr.program_name as program_name,c.course_code as course_code,tt.batch_assignment_id as batch_assignment_id,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "CONCAT(pr.program_short_name,'-',ps.program_specialization_short_name) as ConcateProgAndspecialization,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.section_id as section_id From time_table tt "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join department de on emp.dept_id=de.dept_id "
			+ "Left join department dept on emp.dept_id=dept.dept_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id where tt.ac_year_id=?1 And de.dept_id=?2 Order by tt.time_table_id DESC",nativeQuery =true)
	public List<Map<String,Object>> fetchAllTimeTableDetailsBasedOnAcYearIdAndDeptId(Integer ac_year_id ,Integer dept_id);

	
	@Query(value ="Select tte.time_table_employee_id as id,tt.time_table_id as time_table_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,ir.roomcode as roomcode,tt.program_assignment_id as program_assignment_id,"
	//		+ "(select (LENGTH(seca.student_ids) - LENGTH(REPLACE(seca.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tte.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.emp_id as emp_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,tt.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,c.course_short_name as course_short_name,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "emp.empcode as empcode,emp.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "pr.program_name as program_name,c.course_code as course_code,tt.batch_assignment_id as batch_assignment_id,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "CONCAT(pr.program_short_name,'-',ps.program_specialization_short_name) as ConcateProgAndspecialization,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.section_id as section_id From time_table tt "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join department dept on emp.dept_id=dept.dept_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id where tt.ac_year_id=?1 And tt.school_id=?2 Order by tt.time_table_id DESC",nativeQuery =true)
	public List<Map<String, Object>> fetchAllTimeTableDetailsBasedOnAcYearIdAndSchoolId(Integer ac_year_id,
			Integer school_id);
	
	@Query(value ="Select tte.time_table_employee_id as time_table_employee_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,ir.roomcode as roomcode,tt.time_table_id as time_table_id,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tte.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.emp_id as emp_id,tt.batch_assignment_id as batch_assignment_id,"
			+ "tt.floor_id as floor_id,"
			+ "CASE "
	        + "   WHEN tt.current_sem = 1 THEN 1 "
	        + "   WHEN tt.current_sem = 2 THEN 1 "
	        + "   WHEN tt.current_sem = 3 THEN 2 "
	        + "   WHEN tt.current_sem = 4 THEN 2 "
	        + "   WHEN tt.current_sem = 5 THEN 3 "
	        + "   WHEN tt.current_sem = 6 THEN 3 "
	        + "   WHEN tt.current_sem = 7 THEN 4 "
	        + "   WHEN tt.current_sem = 8 THEN 4 "
	        + "   WHEN tt.current_sem = 9 THEN 5 "
	        + "   WHEN tt.current_sem = 10 THEN 5 "
	        + "   WHEN tt.current_sem = 11 THEN 6 "
	        + "   WHEN tt.current_sem = 12 THEN 6 "
	        + "   ELSE tt.current_year "
	        + " END as current_year,tt.current_sem as current_sem,tte.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,c.course_short_name as course_short_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,tsl.starting_time as starting_time,tsl.ending_time as ending_time,"
			+ "tit.interval_type_short as interval_type_short,tit.interval_type_name as interval_type_name,emp.employee_name as employee_name,pr.program_name as program_name,"
			+ "ps.program_specialization_name as program_specialization_name,sch.school_name as school_name,c.course_id as course_id,"
			+ "s.section_id as section_id,b.batch_id as batch_id,ca.course_assignment_id as course_assignment_id,tt.attendance_status as attendance_status, "
			+ "tsl.starting_time as start_time,tsl.ending_time as end_time,tte.selected_date as date_of_class,pt.program_type_name as program_type_name  From time_table tt "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program_assignment pas on pas.program_assignment_id=tt.program_assignment_id "
			+ "Left join program_type pt on pt.program_type_id=pas.program_type_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id where tte.emp_id=?1 And Year(tt.selected_date)=?2 And Month(tt.selected_date)=?3 And tt.active=true",nativeQuery =true)
	public List<Map<String,Object>> employeeTimeTableDetailsForMobile(Integer emp_id,Integer year, Integer month);
	
	@Query(value="Select tt.time_table_id From time_table tt Where date(tt.selected_date)=date(:date) And tt.is_online=true And tt.attendance_status is null And tt.active=true",nativeQuery =true)
	public Set<Integer> getOnlineTimeTableByDate(LocalDate date);

	
	@Query(value ="Select tte.time_table_employee_id as time_table_employee_id,tt.time_table_id as id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,ir.roomcode as roomcode,tt.program_assignment_id as program_assignment_id,"
	//		+ "(select (LENGTH(seca.student_ids) - LENGTH(REPLACE(seca.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tte.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.emp_id as emp_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,tt.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,c.course_short_name as course_short_name,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "emp.empcode as empcode,emp.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "pr.program_name as program_name,c.course_code as course_code,tt.batch_assignment_id as batch_assignment_id,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "CONCAT(pr.program_short_name,'-',ps.program_specialization_short_name) as ConcateProgAndspecialization,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.section_id as section_id From time_table tt "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join department dept on emp.dept_id=dept.dept_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "where (:ac_year_id IS NULL OR tt.ac_year_id = :ac_year_id) "
			+ "And (:current_sem IS NULL OR tt.current_sem = :current_sem) "
			+ "And (:current_year IS NULL OR tt.current_year = :current_year) "
			+ "And CONCAT(IfNull(c.course_code,''),'',IfNull(emp.empcode,''),'',IfNull(emp.employee_name,''),"
			+ "'',IfNull(sch.school_name_short,''),'',IfNull(pr.program_short_name as program_short_name,''),'',IfNull(ps.program_specialization_name as program_specialization_name,''),"
			+ "'',IfNull(dept.dept_name_short,''),"
			+ "'',IfNull(tt.created_by,''),'',IfNull(tt.created_date,'')) LIKE %:keyword% ", nativeQuery = true)
	public Page<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer ac_year_id, Integer current_sem, Integer current_year);

	
	@Query(value ="Select tte.time_table_employee_id as time_table_employee_id,tt.time_table_id as id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.ac_year_id as ac_year_id,tt.school_id as school_id,tt.program_id as program_id,tt.program_specialization_id as program_specialization_id,"
			+ "tt.interval_type_id as interval_type_id,ir.roomcode as roomcode,tt.program_assignment_id as program_assignment_id,"
	//		+ "(select (LENGTH(seca.student_ids) - LENGTH(REPLACE(seca.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "tt.created_by as created_by,tt.modified_by as modified_by,tt.created_date as created_date,tt.modified_date as modified_date,"
			+ "tte.active as active,tt.created_username as created_username,tt.modified_username as modified_username,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tte.emp_id as emp_id,"
			+ "tt.floor_id as floor_id,tt.current_year as current_year,tt.current_sem as current_sem,tt.selected_date as selected_date,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,c.course_short_name as course_short_name,"
			+ "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "emp.empcode as empcode,emp.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,"
			+ "pr.program_name as program_name,c.course_code as course_code,tt.batch_assignment_id as batch_assignment_id,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "CONCAT(pr.program_short_name,'-',ps.program_specialization_short_name) as ConcateProgAndspecialization,"
			+ "tit.interval_type_short as interval_type_short,emp.employee_name as employee_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "s.section_id as section_id From time_table tt "
			+ "Left join subject_assignment sbja on sbja.subjet_assign_id=tt.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sbja.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join time_table_employee tte on tte.time_table_id=tt.time_table_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join department dept on emp.dept_id=dept.dept_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "where (:ac_year_id IS NULL OR tt.ac_year_id = :ac_year_id) "
			+ "And (:current_sem IS NULL OR tt.current_sem = :current_sem) "
			+ "And (:current_year IS NULL OR tt.current_year = :current_year) ", nativeQuery = true)
	public Page<Map<String, Object>> getAllSortedData(Pageable pageable1, Integer ac_year_id, Integer current_sem, Integer current_year);
	

	@Query(value = "SELECT tte.time_table_employee_id as time_table_employee_id, tt.time_table_id as id, " +
            "tt.from_date as from_date, tt.to_date as to_date, tt.is_online as is_online, " +
            "tt.ac_year_id as ac_year_id, tt.school_id as school_id, tt.program_id as program_id, " +
            "tt.program_specialization_id as program_specialization_id, tt.interval_type_id as interval_type_id, " +
            "ir.roomcode as roomcode, tt.program_assignment_id as program_assignment_id, tt.created_by as created_by, " +
            "tt.modified_by as modified_by, tt.created_date as created_date, tt.modified_date as modified_date, " +
            "tte.active as active, tt.created_username as created_username, tt.modified_username as modified_username, " +
            "tt.class_interval as class_interval, tt.intervals as intervals, tt.section_assignment_id as section_assignment_id, " +
            "tt.time_slots_id as time_slots_id, tt.week_day as week_day, tt.block_id as block_id, tte.emp_id as emp_id, " +
            "tt.floor_id as floor_id, tt.current_year as current_year, tt.current_sem as current_sem, tt.selected_date as selected_date, " +
            "tt.year as year, ay.ac_year as ac_year, sch.school_name_short as school_name_short, c.course_name as course_name, " +
            "pr.program_short_name as program_short_name, ps.program_specialization_short_name as program_specialization_short_name, " +
            "s.section_name as section_name, b.batch_name as batch_name, c.course_short_name as course_short_name, " +
            "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name, " +
            "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots, " +
            "emp.empcode as empcode, emp.dept_id as dept_id, dept.dept_name as dept_name, dept.dept_name_short as dept_name_short, " +
            "pr.program_name as program_name, c.course_code as course_code, tt.batch_assignment_id as batch_assignment_id, " +
            "ps.program_specialization_name as program_specialization_name, " +
            "CONCAT(pr.program_short_name,'-',ps.program_specialization_short_name) as ConcateProgAndspecialization, " +
            "tit.interval_type_short as interval_type_short, emp.employee_name as employee_name, " +
            "ca.course_assignment_coursecode as course_assignment_coursecode, s.section_id as section_id " +
            "FROM time_table tt " +
            "LEFT JOIN subject_assignment sbja ON sbja.subjet_assign_id = tt.subject_assignment_id " +
            "LEFT JOIN course_assignment ca ON ca.course_assignment_id = sbja.course_assignment_id " +
            "LEFT JOIN course c ON c.course_id = ca.course_id " +
            "LEFT JOIN academic_year ay ON ay.ac_year_id = tt.ac_year_id " +
            "LEFT JOIN schools sch ON sch.school_id = tt.school_id " +
            "LEFT JOIN program pr ON pr.program_id = tt.program_id " +
            "LEFT JOIN program_specialization ps ON ps.program_specialization_id = tt.program_specialization_id " +
            "LEFT JOIN time_interval_types tit ON tit.interval_type_id = tt.interval_type_id " +
            "LEFT JOIN time_table_employee tte ON tte.time_table_id = tt.time_table_id " +
            "LEFT JOIN employee_details emp ON emp.emp_id = tte.emp_id " +
            "LEFT JOIN department dept ON emp.dept_id = dept.dept_id " +
            "LEFT JOIN section_assignment seca ON seca.section_assignment_id = tt.section_assignment_id " +
            "LEFT JOIN section s ON s.section_id = seca.section_id " +
            "LEFT JOIN batch_assignment ba ON ba.batch_assignment_id = tt.batch_assignment_id " +
            "LEFT JOIN batch b ON b.batch_id = ba.batch_id " +
            "LEFT JOIN infrastructure_rooms ir ON ir.room_id = tt.room_id " +
            "LEFT JOIN time_slots tsl ON tsl.time_slots_id = tt.time_slots_id " +
            "WHERE (:ac_year_id IS NULL OR tt.ac_year_id = :ac_year_id) " +
            "AND (:school_id IS NULL OR tt.school_id = :school_id) " +
            "AND (:dept_id IS NULL OR dept.dept_id = :dept_id) " +
            "AND (:program_id IS NULL OR tt.program_id = :program_id) " +
            "AND (:program_specialization_id IS NULL OR tt.program_specialization_id = :program_specialization_id) " +
            "AND (:formattedDate IS NULL OR tt.selected_date = STR_TO_DATE(:formattedDate, '%Y-%m-%d')) " +
            "AND (:formattedFromDate IS NULL OR tt.selected_date >= STR_TO_DATE(:formattedFromDate, '%Y-%m-%d')) " +
            "AND (:formattedToDate IS NULL OR tt.selected_date <= STR_TO_DATE(:formattedToDate, '%Y-%m-%d')) " +
            "AND (:userId IS NULL OR tt.created_by = :userId)" +
            "And (:current_sem IS NULL OR tt.current_sem = :current_sem) " +
             "And (:current_year IS NULL OR tt.current_year = :current_year) " +
             "And (:current_year IS NULL OR tt.current_year = :current_year) " +
             "AND (:active IS NULL OR tte.active = :active) " +
            "AND CONCAT(IFNULL(c.course_code,''), '', IFNULL(emp.empcode,''), '', IFNULL(emp.employee_name,''), '', " +
            "IFNULL(sch.school_name_short,''), '', IFNULL(pr.program_short_name,''), '', IFNULL(ps.program_specialization_name,''), " +
            "IFNULL(dept.dept_name_short,''), '', IFNULL(tt.created_by,''), '', IFNULL(tt.created_date,'')) LIKE %:keyword%",
            countQuery = "SELECT COUNT(tt.time_table_id) FROM time_table tt " +
                    "LEFT JOIN subject_assignment sbja ON sbja.subjet_assign_id = tt.subject_assignment_id " +
                    "LEFT JOIN course_assignment ca ON ca.course_assignment_id = sbja.course_assignment_id " +
                    "LEFT JOIN course c ON c.course_id = ca.course_id " +
                    "LEFT JOIN academic_year ay ON ay.ac_year_id = tt.ac_year_id " +
                    "LEFT JOIN schools sch ON sch.school_id = tt.school_id " +
                    "LEFT JOIN program pr ON pr.program_id = tt.program_id " +
                    "LEFT JOIN program_specialization ps ON ps.program_specialization_id = tt.program_specialization_id " +
                    "LEFT JOIN time_interval_types tit ON tit.interval_type_id = tt.interval_type_id " +
                    "LEFT JOIN time_table_employee tte ON tte.time_table_id = tt.time_table_id " +
                    "LEFT JOIN employee_details emp ON emp.emp_id = tte.emp_id " +
                    "LEFT JOIN department dept ON emp.dept_id = dept.dept_id " +
                    "LEFT JOIN section_assignment seca ON seca.section_assignment_id = tt.section_assignment_id " +
                    "LEFT JOIN section s ON s.section_id = seca.section_id " +
                    "LEFT JOIN batch_assignment ba ON ba.batch_assignment_id = tt.batch_assignment_id " +
                    "LEFT JOIN batch b ON b.batch_id = ba.batch_id " +
                    "LEFT JOIN infrastructure_rooms ir ON ir.room_id = tt.room_id " +
                    "LEFT JOIN time_slots tsl ON tsl.time_slots_id = tt.time_slots_id " +
                    "WHERE (:ac_year_id IS NULL OR tt.ac_year_id = :ac_year_id) " +
                    "AND (:school_id IS NULL OR tt.school_id = :school_id) " +
                    "AND (:dept_id IS NULL OR dept.dept_id = :dept_id) " +
                    "AND (:program_id IS NULL OR tt.program_id = :program_id) " +
                    "AND (:program_specialization_id IS NULL OR tt.program_specialization_id = :program_specialization_id) " +
                    "And (:current_sem IS NULL OR tt.current_sem = :current_sem) " +
                    "And (:current_year IS NULL OR tt.current_year = :current_year) " +
                    "AND (:formattedDate IS NULL OR tt.selected_date = STR_TO_DATE(:formattedDate, '%Y-%m-%d')) " +
                    "AND (:formattedFromDate IS NULL OR tt.selected_date >= STR_TO_DATE(:formattedFromDate, '%Y-%m-%d')) " +
                    "AND (:formattedToDate IS NULL OR tt.selected_date <= STR_TO_DATE(:formattedToDate, '%Y-%m-%d')) " +
                    "AND (:userId IS NULL OR tt.created_by = :userId) "  +
                    "AND (:active IS NULL OR tte.active = :active) " ,
       nativeQuery = true)
	public Page<Map<String, Object>> fetchTimeTableDetailsForIndexByKeyword(Pageable pageable, Object keyword,
			Integer ac_year_id, Integer school_id, Integer dept_id, Integer program_id,Integer program_specialization_id, String formattedDate,
			String formattedFromDate,String formattedToDate, Integer userId ,Integer current_year, Integer current_sem, Boolean active);

	@Query(value = "SELECT tte.time_table_employee_id as time_table_employee_id, tt.time_table_id as id, " +
            "tt.from_date as from_date, tt.to_date as to_date, tt.is_online as is_online, " +
            "tt.ac_year_id as ac_year_id, tt.school_id as school_id, tt.program_id as program_id, " +
            "tt.program_specialization_id as program_specialization_id, tt.interval_type_id as interval_type_id, " +
            "ir.roomcode as roomcode, tt.program_assignment_id as program_assignment_id, tt.created_by as created_by, " +
            "tt.modified_by as modified_by, tt.created_date as created_date, tt.modified_date as modified_date, " +
            "tte.active as active, tt.created_username as created_username, tt.modified_username as modified_username, " +
            "tt.class_interval as class_interval, tt.intervals as intervals, tt.section_assignment_id as section_assignment_id, " +
            "tt.time_slots_id as time_slots_id, tt.week_day as week_day, tt.block_id as block_id, tte.emp_id as emp_id, " +
            "tt.floor_id as floor_id, tt.current_year as current_year, tt.current_sem as current_sem, tt.selected_date as selected_date, " +
            "tt.year as year, ay.ac_year as ac_year, sch.school_name_short as school_name_short, c.course_name as course_name, " +
            "pr.program_short_name as program_short_name, ps.program_specialization_short_name as program_specialization_short_name, " +
            "s.section_name as section_name, b.batch_name as batch_name, c.course_short_name as course_short_name, " +
            "Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as concat_batch_name, " +
            "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots, " +
            "emp.empcode as empcode, emp.dept_id as dept_id, dept.dept_name as dept_name, dept.dept_name_short as dept_name_short, " +
            "pr.program_name as program_name, c.course_code as course_code, tt.batch_assignment_id as batch_assignment_id, " +
            "ps.program_specialization_name as program_specialization_name, " +
            "CONCAT(pr.program_short_name,'-',ps.program_specialization_short_name) as ConcateProgAndspecialization, " +
            "tit.interval_type_short as interval_type_short, emp.employee_name as employee_name, " +
            "ca.course_assignment_coursecode as course_assignment_coursecode, s.section_id as section_id " +
            "FROM time_table tt " +
            "LEFT JOIN subject_assignment sbja ON sbja.subjet_assign_id = tt.subject_assignment_id " +
            "LEFT JOIN course_assignment ca ON ca.course_assignment_id = sbja.course_assignment_id " +
            "LEFT JOIN course c ON c.course_id = ca.course_id " +
            "LEFT JOIN academic_year ay ON ay.ac_year_id = tt.ac_year_id " +
            "LEFT JOIN schools sch ON sch.school_id = tt.school_id " +
            "LEFT JOIN program pr ON pr.program_id = tt.program_id " +
            "LEFT JOIN program_specialization ps ON ps.program_specialization_id = tt.program_specialization_id " +
            "LEFT JOIN time_interval_types tit ON tit.interval_type_id = tt.interval_type_id " +
            "LEFT JOIN time_table_employee tte ON tte.time_table_id = tt.time_table_id " +
            "LEFT JOIN employee_details emp ON emp.emp_id = tte.emp_id " +
            "LEFT JOIN department dept ON emp.dept_id = dept.dept_id " +
            "LEFT JOIN section_assignment seca ON seca.section_assignment_id = tt.section_assignment_id " +
            "LEFT JOIN section s ON s.section_id = seca.section_id " +
            "LEFT JOIN batch_assignment ba ON ba.batch_assignment_id = tt.batch_assignment_id " +
            "LEFT JOIN batch b ON b.batch_id = ba.batch_id " +
            "LEFT JOIN infrastructure_rooms ir ON ir.room_id = tt.room_id " +
            "LEFT JOIN time_slots tsl ON tsl.time_slots_id = tt.time_slots_id " +
            "WHERE (:ac_year_id IS NULL OR tt.ac_year_id = :ac_year_id) " +
            "AND (:school_id IS NULL OR tt.school_id = :school_id) " +
            "AND (:dept_id IS NULL OR dept.dept_id = :dept_id) " +
            "AND (:program_id IS NULL OR tt.program_id = :program_id) " +
            "AND (:program_specialization_id IS NULL OR tt.program_specialization_id = :program_specialization_id) " +
            "AND (:formattedDate IS NULL OR tt.selected_date = STR_TO_DATE(:formattedDate, '%Y-%m-%d')) " +
            "AND (:formattedFromDate IS NULL OR tt.selected_date >= STR_TO_DATE(:formattedFromDate, '%Y-%m-%d')) " +
            "AND (:formattedToDate IS NULL OR tt.selected_date <= STR_TO_DATE(:formattedToDate, '%Y-%m-%d')) " +
            "AND (:userId IS NULL OR tt.created_by = :userId)" +
            "And (:current_sem IS NULL OR tt.current_sem = :current_sem) " +
            "And (:current_year IS NULL OR tt.current_year = :current_year) " +
            "AND (:active IS NULL OR tte.active = :active) " ,
    countQuery = "SELECT COUNT(tt.time_table_id) FROM time_table tt " +
                 "LEFT JOIN subject_assignment sbja ON sbja.subjet_assign_id = tt.subject_assignment_id " +
                 "LEFT JOIN course_assignment ca ON ca.course_assignment_id = sbja.course_assignment_id " +
                 "LEFT JOIN course c ON c.course_id = ca.course_id " +
                 "LEFT JOIN academic_year ay ON ay.ac_year_id = tt.ac_year_id " +
                 "LEFT JOIN schools sch ON sch.school_id = tt.school_id " +
                 "LEFT JOIN program pr ON pr.program_id = tt.program_id " +
                 "LEFT JOIN program_specialization ps ON ps.program_specialization_id = tt.program_specialization_id " +
                 "LEFT JOIN time_interval_types tit ON tit.interval_type_id = tt.interval_type_id " +
                 "LEFT JOIN time_table_employee tte ON tte.time_table_id = tt.time_table_id " +
                 "LEFT JOIN employee_details emp ON emp.emp_id = tte.emp_id " +
                 "LEFT JOIN department dept ON emp.dept_id = dept.dept_id " +
                 "LEFT JOIN section_assignment seca ON seca.section_assignment_id = tt.section_assignment_id " +
                 "LEFT JOIN section s ON s.section_id = seca.section_id " +
                 "LEFT JOIN batch_assignment ba ON ba.batch_assignment_id = tt.batch_assignment_id " +
                 "LEFT JOIN batch b ON b.batch_id = ba.batch_id " +
                 "LEFT JOIN infrastructure_rooms ir ON ir.room_id = tt.room_id " +
                 "LEFT JOIN time_slots tsl ON tsl.time_slots_id = tt.time_slots_id " +
                 "WHERE (:ac_year_id IS NULL OR tt.ac_year_id = :ac_year_id) " +
                 "AND (:school_id IS NULL OR tt.school_id = :school_id) " +
                 "AND (:dept_id IS NULL OR dept.dept_id = :dept_id) " +
                 "AND (:program_id IS NULL OR tt.program_id = :program_id) " +
                 "AND (:program_specialization_id IS NULL OR tt.program_specialization_id = :program_specialization_id) " +
                 "AND (:formattedDate IS NULL OR tt.selected_date = STR_TO_DATE(:formattedDate, '%Y-%m-%d')) " +
                 "AND (:formattedFromDate IS NULL OR tt.selected_date >= STR_TO_DATE(:formattedFromDate, '%Y-%m-%d')) " +
                 "AND (:formattedToDate IS NULL OR tt.selected_date <= STR_TO_DATE(:formattedToDate, '%Y-%m-%d')) " +
                 "AND (:userId IS NULL OR tt.created_by = :userId)" +
                 "And (:current_sem IS NULL OR tt.current_sem = :current_sem) " +
                 "And (:current_year IS NULL OR tt.current_year = :current_year) " +
                 "AND (:active IS NULL OR tte.active = :active) " ,
    nativeQuery = true)
	public Page<Map<String, Object>> fetchTimeTableDetailsForIndexWOKeyword(Pageable pageable1, Integer ac_year_id,
			Integer school_id, Integer dept_id, Integer program_id,Integer program_specialization_id, String formattedDate, String formattedFromDate,
			String formattedToDate, Integer current_sem, Integer current_year, Integer userId, Boolean active);


	@Query(value="Select cast(count(time_table_id) as UNSIGNED) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Where tt.ac_year_id=?1 And (tt.section_assignment_id in (?2) or tt.batch_assignment_id in (?3)) "
			+ "And tt.current_sem=?4 And c.course_id=?5 And ca.course_assignment_id=?6 And tt.attendance_status =1 And tt.active=true",nativeQuery=true)
	public Integer totalClassOfCourseBySem(Integer acYearId, List<Integer> sectionAssignmentId, List<Integer> batchAssignmentId, Integer currentYearSem,Integer courseId,Integer courseAssignmentId);

	@Query(value="Select cast(count(time_table_id) as UNSIGNED) From time_table tt "
			+ "left Join subject_assignment sa on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left Join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "left Join course c on c.course_id=ca.course_id "
			+ "Where tt.ac_year_id=?1 And (tt.section_assignment_id in (?2) or tt.batch_assignment_id in (?3)) "
			+ "And tt.current_year=?4 And c.course_id=?5 And ca.course_assignment_id=?6 And tt.attendance_status =1 And tt.active=true",nativeQuery=true)
	public Integer totalClassOfCourseByYear(Integer acYearId, List<Integer> sectionAssignmentId, List<Integer> batchAssignmentId, Integer currentYearSem,Integer courseId,Integer courseAssignmentId);


}	
