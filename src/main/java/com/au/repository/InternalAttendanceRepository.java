package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.au.model.InternalAttendance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface InternalAttendanceRepository  extends JpaRepository<InternalAttendance, Integer> {

	
	@Query(value = "SELECT count(*) FROM InternalAttendance ia where ia.student_id=?1 and ia.exam_date in ?2 and ia.exam_time=?3 and ia.course_id=?4 and ia.active =true")
	public Integer getcountOfInternalAttendances(Integer student_id,String exam_date,String exam_time,Integer course_id);
	
	
	@Query(value = "select new map(ia.exam_attendance_id as id,ia.exam_date as exam_date,ia.exam_time as exam_time,ia.course_id as course_id,"
			+ "ia.student_id as student_id,ia.present_status as present_status,ia.emp_id as emp_id,ia.room_id as room_id,ia.internal_id as internal_id,"
			+ "ia.week_day as week_day,ia.exam_room_id as exam_room_id,ia.remarks as remarks,"
			+ "ia.created_username as created_username,ia.modified_username as modified_username,"
			+ "ia.created_date as created_date,ia.modified_date as modified_date,ia.created_by as created_by,"
			+ "ia.modified_by as modified_by,ia.active as active) from InternalAttendance ia "
			+ "where CONCAT(IfNull(ia.created_username,''),'',IfNull(ia.exam_date,''),'',IfNull(ia.exam_time,''),'',IfNull(ia.course_id,''),"
			+ "'',IfNull(ia.created_by,''),'',IfNull(ia.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	@Query(value = "select new map(ia.exam_attendance_id as id,ia.exam_date as exam_date,ia.exam_time as exam_time,ia.course_id as course_id,"
			+ "ia.student_id as student_id,ia.present_status as present_status,ia.emp_id as emp_id,ia.room_id as room_id,ia.internal_id as internal_id,"
			+ "ia.week_day as week_day,ia.exam_room_id as exam_room_id,ia.remarks as remarks,"
			+ "ia.created_username as created_username,ia.modified_username as modified_username,"
			+ "ia.created_date as created_date,ia.modified_date as modified_date,ia.created_by as created_by,"
			+ "ia.modified_by as modified_by,ia.active as active) from InternalAttendance ia")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Query(value = "SELECT ia from InternalAttendance ia where ia.active=true")
	public List<InternalAttendance> findAll11();
	
	
	@Modifying
	@Query(value = "update InternalAttendance ia set ia.active=false where ia.exam_attendance_id=?1")
	public void updateInternalAttendance(Integer id);
	
	@Modifying
	@Query(value = "update InternalAttendance ia set ia.active=true where ia.exam_attendance_id=?1")
	public void updateInternalAttendance1(Integer id);

	@Query(value = "select new map(ia.exam_attendance_id as id,ia.student_id as student_id,ia.present_status as present_status,ia.emp_id as emp_id,"
			+ "sd.student_name as student_name,sd.auid as auid,sd.usn as usn) "
			+ "from InternalAttendance ia "
			+ "Left join Student_Details sd On sd.student_id=ia.student_id "
			+ "Where ia.internal_session_id=?1 And ia.active=true")
	public List<HashMap<String, Object>> getInternalAttendanceDetailsOfStudent(Integer internal_session_id);

	@Query(value = "select new map(ia.exam_attendance_id as id,ia.student_id as student_id,ia.present_status as present_status,ia.emp_id as emp_id,"
			+ "sd.student_name as student_name,sd.auid as auid,sd.usn as usn) "
			+ "from InternalAttendance ia "
			+ "Left join Student_Details sd On sd.student_id=ia.student_id "
			+ "Where ia.internal_session_id in ?1 And ia.emp_id=?2 And ia.active=true")
	public List<Map<String, Object>> getInternalAttendanceDetailsOfStudentList(List<Integer> internal_session_id ,Integer emp_id);

	
}
