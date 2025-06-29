package com.au.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.EventCreation;


@Transactional
@Repository
public interface EventCreationRepository extends JpaRepository<EventCreation , Integer>{
	
	
	@Query(value = "SELECT ec FROM EventCreation ec where ec.active=true")
	public List<EventCreation> findAll1();
	

	@Query(value ="Select ec.event_id as id,ec.event_name as event_name,ec.event_sub_name as event_sub_name,ec.guest_name as guest_name,"
			+ "ec.school_id as school_id,ec.event_description as event_description,ec.event_start_time as event_start_time,ec.active as active,"
			+ "ec.event_end_time as event_end_time,ec.remarks as remarks,ec.approver1_id as approver1_id,ec.created_date as created_date,ec.is_common as is_common,"
			+ "ec.approved_date as approved_date,ec.created_by as created_by,ec.created_username as created_username,ec.event_status As event_status,"
			+ "ec.approved_status As approved_status,ec.approved_by As approved_by,ed.leave_approver1_emp_id As Approver,ed.employee_name As employee_name,"
			+ "ed.email As email,ec.tt_status As tt_status,ec.summarize As summarize,ec.summarize_status As summarize_status,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,ec.school_id)) as school_name_short,"
			+ "ir.roomcode as roomcode,ib.block_short_name as block_short_name From event_creation ec "
			+ "Left Join user_details ua on ua.id=ec.approved_by "
			+ "Left Join employee_details ed on ed.email=ua.email "
			+ "Left Join event_blocked_rooms ebr on ebr.event_id=ec.event_id "
			+ "Left Join infrastructure_rooms ir on ir.room_id=ebr.room_id "
			+ "Left Join infrastructure_blocks ib on ib.block_id=ebr.block_id "
			+ "Where ec.approved_by=?2 And CONCAT(IfNull(ec.event_id,''),'',IfNull(ec.event_name,''),'',IfNull(ec.event_sub_name,''),'',"
			+ "IfNull(ec.event_start_time,''),'',IfNull(ec.event_end_time,''),'',IfNull(sc.school_name_short,'')) LIKE %?1%",nativeQuery=true)
	public List<Map<String, Object>> fetchAllEventCreationForApproverlistAll1(Pageable pageable, Integer userId,Object keyword);
	
	@Query(value ="Select ec.event_id as id,ec.event_name as event_name,ec.event_sub_name as event_sub_name,ec.guest_name as guest_name,"
			+ "ec.school_id as school_id,ec.event_description as event_description,ec.event_start_time as event_start_time,ec.active as active,"
			+ "ec.event_end_time as event_end_time,ec.remarks as remarks,ec.approver1_id as approver1_id,ec.created_date as created_date,ec.is_common as is_common,"
			+ "ec.approved_date as approved_date,ec.created_by as created_by,ec.created_username as created_username,ec.event_status As event_status,"
			+ "ec.approved_status As approved_status,ec.approved_by As approved_by,ed.leave_approver1_emp_id As Approver,ed.employee_name As employee_name,"
			+ "ed.email As email,ec.tt_status As tt_status,ec.summarize As summarize,ec.summarize_status As summarize_status,"
			+ "(Select GROUP_CONCAT(schools.school_name_short) From schools Where FIND_IN_SET(schools.school_id,ec.school_id)) as school_name_short,"
			+ "ir.roomcode as roomcode,ib.block_short_name as block_short_name From event_creation ec "
			+ "Left Join user_details ua on ua.id=ec.approved_by "
			+ "Left Join employee_details ed on ed.email=ua.email "
			+ "Left Join event_blocked_rooms ebr on ebr.event_id=ec.event_id "
			+ "Left Join infrastructure_rooms ir on ir.room_id=ebr.room_id "
			+ "Left Join infrastructure_blocks ib on ib.block_id=ebr.block_id where ec.approved_by=?1",nativeQuery=true)
	public List<Map<String, Object>> fetchAllEventCreationForApproverlistAll2(Pageable pageable1, Integer userId);
	
	@Modifying
	@Query(value = "Update EventCreation ec set ec.active=false where ec.event_id=?1")
	public void update(Integer event_id);
	
	@Modifying
	@Query(value = "Update EventCreation ec set ec.active=true where ec.event_id=?1")
	public void update1(Integer event_id);
	
	@Query(value ="Select ec.event_name as event_name,ec.event_description as event_description,ec.event_start_time as event_start_time,ec.tt_status As tt_status,ec.event_status As event_status,"
			+ "ec.event_end_time as event_end_time,(Select schools.school_name_short From schools Where schools.school_id=?2) as school_name_short,"
			+ "ir.roomcode as roomcode,ib.block_short_name as block_short_name From event_creation ec "
			+ "Inner Join event_blocked_rooms ebr on ebr.event_id=ec.event_id "
			+ "Left Join infrastructure_rooms ir on ir.room_id=ebr.room_id "
			+ "Left Join infrastructure_blocks ib on ib.block_id=ebr.block_id "
			+ "Where ec.school_id Like %?1% And now() between ec.event_start_time And ec.event_end_time And ec.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchEventDetailsOnSchoolId(String school_id,Integer school_id1);
	
	@Query(value ="Select ec.event_name as event_name,CONCAT(IfNull(date(ec.event_start_time),' '),' - ',IfNull(date(ec.event_end_time),' ')) as event_time From event_creation ec "
			+ "Where ec.school_id Like %?1% And (month(now()) between month(ec.event_start_time) And month(ec.event_end_time)) And (year(now()) between year(ec.event_start_time) And year(ec.event_end_time)) And ec.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchHolidayAndEventDetailsForAcademicCalendarOnSchoolId(String school_id);

	
	@Query(value ="Select ebr.room_id as room_id,ebr.event_id as event_id,ec.event_end_time as event_end_time,ec.event_start_time as event_start_time,ec.created_date As created_date,"
			+ "sc.school_id As school_id,sc.school_name as school_name,sc.school_name_short As school_name_short,ua.id As userId,ed.mobile As mobile,ec.approved_status As approved_status,"
			+ "ec.event_name As event_name,ec.event_description As event_description,ec.tt_status As tt_status,ec.event_status As event_status,"
			+ "ed.emp_id As emp_id,ed.email as email,ed.employee_name As employee_name,de.dept_id As dept_id,de.dept_name As dept_name,de.dept_name_short As dept_name_short "
			+ "FROM event_blocked_rooms ebr "
			+ "left join event_creation ec on ec.event_id = ebr.event_id "
			+ "left join schools sc on ec.school_id = sc.school_id "
			+ "left join user_details ua on ec.created_by = ua.id "
			+ "left join employee_details ed on ed.email = ua.email "
			+ "left join department de on ed.dept_id = de.dept_id "
			+ "where ebr.room_id = :room_id And "
			+ "date(ec.event_start_time) = :localDateTime ",nativeQuery=true)
//			+ "date(ec.event_start_time) = :localDateTime OR "
//			+ "date(ec.event_end_time) = :localDateTime ",nativeQuery=true)
	public List<Map<String, Object>> getEventReportDetails(Integer room_id, LocalDate localDateTime);

	
	@Query(value = "SELECT ec.event_id AS id, ec.event_name AS event_name, ec.event_sub_name AS event_sub_name, ec.guest_name AS guest_name, "
	        + "ec.school_id AS school_id, ec.event_description AS event_description, ec.event_start_time AS event_start_time, ec.active AS active, "
	        + "ec.event_end_time AS event_end_time, ec.remarks AS remarks, ec.approver1_id AS approver1_id, ec.created_date AS created_date, ec.is_common AS is_common, "
	        + "ec.approved_date AS approved_date, ec.created_by AS created_by, ec.created_username AS created_username, "
	        + "ec.approved_status AS approved_status, ec.approved_by AS approved_by, ec.tt_status AS tt_status, ec.event_status AS event_status, "
	        + "ir.room_id AS room_id, ft.facility_type_id AS facility_type_id, ec.summarize AS summarize, ec.summarize_status AS summarize_status, "
	        + "ft.facility_type_name AS facility_type_name, ft.facility_short_name AS facility_short_name, "
	        + "ib.block_id AS block_id, ib.block_name AS block_name, "
	        + "(SELECT GROUP_CONCAT(schools.school_name_short) FROM schools WHERE FIND_IN_SET(schools.school_id, ec.school_id)) AS school_name_short, "
	        + "ir.roomcode AS roomcode, ib.block_short_name AS block_short_name "
	        + "FROM event_creation ec "
	        + "INNER JOIN event_blocked_rooms ebr ON ebr.event_id = ec.event_id "
	        + "LEFT JOIN infrastructure_rooms ir ON ir.room_id = ebr.room_id "
	        + "LEFT JOIN infrastructure_floors ifl ON ir.floor_id = ifl.floor_id "
	        + "LEFT JOIN infrastructure_blocks ib ON ib.block_id = ifl.block_id "
	        + "LEFT JOIN infrastructure_facility_type ft ON ft.facility_type_id = ir.facility_type_id "
	        + "WHERE (:school_id IS NULL OR ec.school_id = :school_id) "
	        + "AND (:date IS NULL OR DATE(ec.event_start_time) = :date) "
	        + "AND (:facility_type_id IS NULL OR ft.facility_type_id = :facility_type_id) "
	        + "AND (:room_id IS NULL OR ir.room_id = :room_id) "
	        + "AND (:created_by IS NULL OR ec.created_by = :created_by) "
	        + "AND CONCAT(IFNULL(ec.event_id, ''), IFNULL(ec.event_name, ''), IFNULL(ec.event_sub_name, ''), IFNULL(ec.event_start_time, ''), "
	        + "IFNULL(ec.event_end_time, ''), IFNULL(ec.school_id, '')) LIKE %:keyword%",
	    countQuery = "SELECT COUNT(ec.event_id) "
	        + "FROM event_creation ec "
	        + "INNER JOIN event_blocked_rooms ebr ON ebr.event_id = ec.event_id "
	        + "LEFT JOIN infrastructure_rooms ir ON ir.room_id = ebr.room_id "
	        + "LEFT JOIN infrastructure_blocks ib ON ib.block_id = ebr.block_id "
	        + "LEFT JOIN infrastructure_facility_type ft ON ft.facility_type_id = ir.facility_type_id "
	        + "WHERE (:school_id IS NULL OR ec.school_id = :school_id) "
	        + "AND (:date IS NULL OR DATE(ec.event_start_time) = :date) "
	        + "AND (:facility_type_id IS NULL OR ft.facility_type_id = :facility_type_id) "
	        + "AND (:room_id IS NULL OR ir.room_id = :room_id) "
	        + "AND (:created_by IS NULL OR ec.created_by = :created_by) "
	        + "AND CONCAT(IFNULL(ec.event_id, ''), IFNULL(ec.event_name, ''), IFNULL(ec.event_sub_name, ''), IFNULL(ec.event_start_time, ''), "
	        + "IFNULL(ec.event_end_time, ''), IFNULL(ec.school_id, '')) LIKE %:keyword%",
	    nativeQuery = true)
	public Page<Map<String, Object>> fetchAllEventCreationDetails(Pageable pageable, Object keyword, Integer school_id,
	        String date, Integer room_id, Integer facility_type_id, Integer created_by);

	
	@Query(value = "SELECT ec.event_id AS id, ec.event_name AS event_name, ec.event_sub_name AS event_sub_name, ec.guest_name AS guest_name, "
	        + "ec.school_id AS school_id, ec.event_description AS event_description, ec.event_start_time AS event_start_time, ec.active AS active, "
	        + "ec.event_end_time AS event_end_time, ec.remarks AS remarks, ec.approver1_id AS approver1_id, ec.created_date AS created_date, ec.is_common AS is_common, "
	        + "ec.approved_date AS approved_date, ec.created_by AS created_by, ec.created_username AS created_username, "
	        + "ec.approved_status AS approved_status, ec.approved_by AS approved_by, ec.tt_status AS tt_status, ec.event_status AS event_status, "
	        + "ir.room_id AS room_id, ft.facility_type_id AS facility_type_id, ec.summarize AS summarize, ec.summarize_status AS summarize_status, "
	        + "ft.facility_type_name AS facility_type_name, ft.facility_short_name AS facility_short_name, "
	        + "ib.block_id AS block_id, ib.block_name AS block_name, "
	        + "(SELECT GROUP_CONCAT(schools.school_name_short) FROM schools WHERE FIND_IN_SET(schools.school_id, ec.school_id)) AS school_name_short, "
	        + "ir.roomcode AS roomcode, ib.block_short_name AS block_short_name "
	        + "FROM event_creation ec "
	        + "INNER JOIN event_blocked_rooms ebr ON ebr.event_id = ec.event_id "
	        + "LEFT JOIN infrastructure_rooms ir ON ir.room_id = ebr.room_id "
	        + "LEFT JOIN infrastructure_floors ifl ON ir.floor_id = ifl.floor_id "
	        + "LEFT JOIN infrastructure_blocks ib ON ib.block_id = ifl.block_id "
	        + "LEFT JOIN infrastructure_facility_type ft ON ft.facility_type_id = ir.facility_type_id "
	        + "WHERE (:school_id IS NULL OR ec.school_id = :school_id) "
	        + "AND (:date IS NULL OR DATE(ec.event_start_time) = :date) "
	        + "AND (:facility_type_id IS NULL OR ft.facility_type_id = :facility_type_id) "
	        + "AND (:room_id IS NULL OR ir.room_id = :room_id) "
	        + "AND (:created_by IS NULL OR ec.created_by = :created_by)", 
	    countQuery = "SELECT COUNT(ec.event_id) "
	        + "FROM event_creation ec "
	        + "INNER JOIN event_blocked_rooms ebr ON ebr.event_id = ec.event_id "
	        + "LEFT JOIN infrastructure_rooms ir ON ir.room_id = ebr.room_id "
	        + "LEFT JOIN infrastructure_blocks ib ON ib.block_id = ebr.block_id "
	        + "LEFT JOIN infrastructure_facility_type ft ON ft.facility_type_id = ir.facility_type_id "
	        + "WHERE (:school_id IS NULL OR ec.school_id = :school_id) "
	        + "AND (:date IS NULL OR DATE(ec.event_start_time) = :date) "
	        + "AND (:facility_type_id IS NULL OR ft.facility_type_id = :facility_type_id) "
	        + "AND (:room_id IS NULL OR ir.room_id = :room_id) "
	        + "AND (:created_by IS NULL OR ec.created_by = :created_by)", 
	    nativeQuery = true)
	public Page<Map<String, Object>> fetchAllEventCreationDetailsWOKeyword(Pageable pageable, Integer school_id,
			String date, Integer room_id, Integer facility_type_id, Integer created_by);

	

	

}
