package com.au.repository;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.InternalRoom;

@Repository
@Transactional
public interface InternalRoomRepository extends JpaRepository<InternalRoom, Integer> {

	@Query(value = "select ir.internal_room_id as id,rm.roomcode as roomcode,"
			+ "ir.internal_session_id as internal_session_id,ir.active as active,"
			+ "ir.created_by as created_by,ir.modified_by as modified_by,ir.created_date as created_date,ir.modified_date as modified_date,"
			+ "ir.created_username as created_username,ir.modified_username as modified_username, "
			+ "isa.max_marks as max_marks,isa.min_marks as min_marks,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "isa.school_id as school_id,isa.year_sem as year_sem,isa.program_id as program_id,isa.ac_year_id as ac_year_id,isa.exam_time as exam_time,"
			+ "isa.date_of_exam as date_of_exam,isa.week_day as week_day,isa.program_specialization_id as program_specialization_id "
			+ "from internal_room_assignment ir "
			+ "left join internal_session_creation isa on ir.internal_session_id=isa.internal_session_id "
			+ "left join infrastructure_rooms rm on rm.room_id=ir.room_id "
			+ "left join course_assignment ca on isa.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "where CONCAT(IfNull(ir.created_username,''),'',IfNull(ir.room_id,''),'',IfNull(ir.internal_room_id,''),"
			+ "'',IfNull(ir.created_by,''),'',IfNull(ir.created_date,'')) LIKE %?1%", nativeQuery = true)
	Page<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "select ir.internal_room_id as id,rm.roomcode as roomcode,"
			+ "ir.internal_session_id as internal_session_id,ir.active as active,"
			+ "ir.created_by as created_by,ir.modified_by as modified_by,ir.created_date as created_date,ir.modified_date as modified_date,"
			+ "ir.created_username as created_username,ir.modified_username as modified_username, "
			+ "isa.max_marks as max_marks,isa.min_marks as min_marks,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "isa.school_id as school_id,isa.year_sem as year_sem,isa.program_id as program_id,isa.ac_year_id as ac_year_id,isa.exam_time as exam_time,"
			+ "isa.date_of_exam as date_of_exam,isa.week_day as week_day,isa.program_specialization_id as program_specialization_id "
			+ "from internal_room_assignment ir "
			+ "left join internal_session_creation isa on ir.internal_session_id=isa.internal_session_id "
			+ "left join infrastructure_rooms rm on rm.room_id=ir.room_id "
			+ "left join course_assignment ca on isa.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id ", nativeQuery = true)
	Page<Map<String, Object>> getAllSortedData(Pageable pageable);
	
	
	@Modifying
	@Query(value = "update InternalRoom itt set itt.active=false where itt.internal_room_id=?1")
	public void updateInternalRoom(Integer id);
	
	
	@Modifying
	@Query(value = "update InternalRoom itt set itt.active=true where itt.internal_room_id=?1")
	public void updateInternalRoom1(Integer id);

}
