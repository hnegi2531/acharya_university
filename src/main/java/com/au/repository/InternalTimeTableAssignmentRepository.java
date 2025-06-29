package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.au.model.InternalTimeTableAssignment;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Modifying;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public interface InternalTimeTableAssignmentRepository extends JpaRepository<InternalTimeTableAssignment, Integer>{

	
	
	@Query(value = "select itta.student_ids FROM internal_timetable_assignment itta "
			+ "left join internal_session_assignment isa on itta.internal_id=isa.internal_id "
			+ "left join internal_time_table itt on itt.internal_id=isa.internal_id "
			+ "where isa.ac_year_id=?1 And isa.internal_id=?2 And isa.year_sem=?3 And isa.program_specialization_id=?4 And "
			+ "itt.course_assignment_id=?5 And itta.active=true",nativeQuery = true)
	public List<String> getStudentIds(Integer ac_year_id, Integer internal_id, Integer year_sem,
			Integer program_specialization_id,Integer course_assignment_id);


	
//	@Query(value = "SELECT count(*) FROM InternalTimeTableAssignment itta where itta.internal_room_id=?1 and itta.active=true")
//	 public Integer countOfInternalroomId(Integer internal_room_id);
//	
//	
//	@Query(value = "SELECT count(*) FROM InternalTimeTableAssignment itta where itta.room_id=?1 and itta.student_ids in ?2 "
//			+ "and itta.emp_ids=?3 and itta.selected_date=?4 and itta.time_slots_id=?5 and  itta.active =true")
//	public Integer getCountInternalTimeTableAssignments(Integer room_id, List<Integer> list_std,Integer emp_ids,Date selected_date,Integer time_slots_id);
//	
//	
//	@Query(value = "SELECT count(*) FROM InternalTimeTableAssignment itta where itta.room_id=?1 and itta.emp_ids=?2 and itta.time_slots_id=?3 and itta.selected_date=?4 and itta.active =true")
//	public Integer getCountTimeTableEmployee(Integer room_id,Integer emp_ids, Integer time_slots_id, Date selected_date);
//	
//	
//	
//	@Query(value = "select itta from InternalTimeTableAssignment itta where itta.active=true")
//	public List<InternalTimeTableAssignment> findAll1();
//
//
//	@Query(value = "select itta.internal_room_id as id,itta.student_ids as student_ids,itta.emp_ids as emp_ids,itta.room_id as room_id,"
//			+ "itta.internal_time_table_id as internal_time_table_id,itta.internal_id as internal_id,itta.remarks as remarks,"
//			+ "itta.week_day as week_day,itta.created_by as created_by,itta.created_date as created_date,"
//			+ "p.program_name as program_name,p.program_short_name as program_short_name,ir1.roomcode as roomcode,"
//			+ "itta.active as active,itta.created_username as created_username,ed.employee_name as employee_name,"
//			+ "s.school_name as school_name,s.school_name_short as school_name_short,"
//			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
//			+ "it.week_day as week_day,it.min_marks as min_marks,it.max_marks as max_marks,"
//			+ "c.course_name as course_name,c.course_short_name as course_short_name,"
//			+ "it.course_id as course_id,it.exam_time as exam_time,it.time_slots_id as time_slots_id,it.date_of_exam as date_of_exam,"
//			+ "isa.from_date as from_date,isa.to_date as to_date,isa.ac_year_id as ac_year_id,isa.internal_short_name as internal_short_name,"
//			+ "(select (LENGTH(itta.student_ids) - LENGTH(REPLACE(itta.student_ids,\",\",\"\")) + 1)) as count_of_students,"
//			+ "isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,isa.remarks as remarks,isa.year_sem as year_sem,"
//			+ "isa.school_id as school_id,isa.program_id as program_id,isa.program_specialization_id as program_specialization_id from internal_timetable_assignment itta "
//			+ "left join internal_time_table it on itta.internal_time_table_id=it.internal_time_table_id "
//			+ "left join internal_session_assignment isa on itta.internal_id=isa.internal_id "
//			+ "left join employee_details ed on ed.emp_id=itta.emp_ids "
//			+ "left join infrastructure_rooms ir1 on ir1.room_id=itta.room_id "
//			+ "left join program p on p.program_id=isa.program_id "
//			+ "left join course c on c.course_id=it.course_id "
//			+ "left join schools s on s.school_id=isa.school_id "
//			+ "left join program_specialization ps on ps.program_specialization_id=isa.program_specialization_id "
//			+ "Where CONCAT(IfNull(itta.internal_room_id,''),'',IfNull(itta.student_ids,''),'',"
//		    + "IfNull(itta.created_by,''),'',IfNull(itta.created_date,''),'',IfNull(itta.created_username,''),'',"
//			+ "IfNull(itta.created_username,''),'',IfNull(itta.internal_time_table_id,''),'',IfNull(isa.from_date,''),'',"
//			+ "IfNull(it.date_of_exam,'')) LIKE %?1% ",nativeQuery=true)
//	public List<Map<String, Object>> findAll1(Pageable pageable, Object keyword);
//	
//	
//	@Query(value = "select itta.internal_room_id as id,itta.student_ids as student_ids,itta.emp_ids as emp_ids,itta.room_id as room_id,"
//			+ "itta.internal_time_table_id as internal_time_table_id,itta.internal_id as internal_id,itta.remarks as itta_remarks,"
//			+ "itta.created_by as created_by,itta.created_date as created_date,"
//			+ "p.program_name as program_name,p.program_short_name as program_short_name,ir1.roomcode as roomcode,"
//			+ "itta.active as active,itta.created_username as created_username,ed.employee_name as employee_name,"
//			+ "s.school_name as school_name,s.school_name_short as school_name_short,"
//			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
//			+ "it.week_day as week_day,it.min_marks as min_marks,it.max_marks as max_marks,"
//			+ "c.course_name as course_name,c.course_short_name as course_short_name,"
//			+ "it.course_id as course_id,it.exam_time as exam_time,it.time_slots_id as time_slots_id,it.date_of_exam as date_of_exam,"
//			+ "isa.from_date as from_date,isa.to_date as to_date,isa.ac_year_id as ac_year_id,isa.internal_short_name as internal_short_name,"
//			+ "(select (LENGTH(itta.student_ids) - LENGTH(REPLACE(itta.student_ids,\",\",\"\")) + 1)) as count_of_students,"
//			+ "isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,isa.remarks as isa_remarks,isa.year_sem as year_sem,"
//			+ "isa.school_id as school_id,isa.program_id as program_id,isa.program_specialization_id as program_specialization_id from internal_timetable_assignment itta "
//			+ "left join internal_time_table it on itta.internal_time_table_id=it.internal_time_table_id "
//			+ "left join internal_session_assignment isa on itta.internal_id=isa.internal_id "
//			+ "left join employee_details ed on ed.emp_id=itta.emp_ids "
//			+ "left join infrastructure_rooms ir1 on ir1.room_id=itta.room_id "
//			+ "left join program p on p.program_id=isa.program_id "
//			+ "left join course c on c.course_id=it.course_id "
//			+ "left join schools s on s.school_id=isa.school_id "
//			+ "left join program_specialization ps on ps.program_specialization_id=isa.program_specialization_id",nativeQuery=true)
//	public List<Map<String, Object>> findAll2(Pageable pageable);
//	
//	
//	@Modifying
//	@Query(value = "update InternalTimeTableAssignment itta set itta.active=false where itta.internal_room_id=?1")
//	public void update(Integer id);
//
//	@Modifying
//	@Query(value = "update InternalTimeTableAssignment itta set itta.active=true where itta.internal_room_id=?1")
//	public void update1(Integer id);
//	
//	
//	@Query(value = "SELECT count(*) FROM InternalTimeTableAssignment itta where itta.emp_ids=?1 and itta.time_slots_id in ?2 and itta.student_ids=?3 and itta.active =true")
//	public Integer getCountOfEmployeeInInternalTimeTable(Integer emp_ids,Integer time_slots_id, String student_ids);
//	
//
//	@Query(value = "Select concat(ed.employee_name, '-',d.dept_name_short) as employee_name,ed.emp_id "
//			+ " from employee_details ed "
//			+ "left join department d on d.dept_id=ed.dept_id "
//			+ "where ed.emp_id not in (select itta1.emp_ids from internal_timetable_assignment itta1 "
//			+ "left join internal_time_table itt on itt.internal_time_table_id=itta1.internal_time_table_id where "
//			+ " itta1.time_slots_id=?1 and itta1.selected_date=?2 and itt.course_id=?3)",nativeQuery = true)
//	public List<Map<String, Object>> listAllIttaEmpBasedOnTimeAndDate11(Integer time_slots_id,String selected_date, Integer course_id);
//	
//	@Query(value = "select itta1.room_id from internal_timetable_assignment itta1 "
//			+ "left join internal_time_table itt on itt.internal_time_table_id=itta1.internal_time_table_id "
//			+ "left join internal_session_assignment isa on itta1.internal_id=isa.internal_id "
//			+ "where isa.program_specialization_id=?1 and itt.course_id=?2 and itta1.selected_date=?3",nativeQuery = true)
//	public List<Integer> getAssignedRooms(Integer program_specialization_id,Integer course_id, Date date);
//
//	@Query(value = "select ir.room_id,ir.roomcode from infrastructure_rooms ir "
//			+ "where ir.facility_type_id IN (select ift.facility_type_id from infrastructure_facility_type ift where ift.timetable_status=1)",nativeQuery = true)
//	public List<Map<String, Object>> listAllIttaRoomBasedOnTimeAndDate11();
//	
//	
//	@Query(value = "select ir.room_id,ir.roomcode from infrastructure_rooms ir "
//			+ "where ir.room_id NOT IN ?1 and ir.facility_type_id IN (select ift.facility_type_id from infrastructure_facility_type ift where ift.timetable_status=1)",nativeQuery = true)
//	public List<Map<String, Object>> listAllUnassignedIttaRoomBasedOnTimeAndDate11(List<Integer> room_id);
//	
//	@Query(value = "select itta.student_ids FROM internal_timetable_assignment itta "
//			+ "left join internal_time_table itt on itt.internal_time_table_id=itta.internal_time_table_id "
//			+ "where itt.course_id=?1 and itta.internal_id=?2 and itta.active=true",nativeQuery = true)
//	public String getStudentIds(Integer course_id, Integer internal_id);
//	
//	

//	
//	

	
}
