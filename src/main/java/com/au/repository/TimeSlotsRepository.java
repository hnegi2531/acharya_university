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
import com.au.model.TimeSlots;

@Transactional
@Repository
public interface TimeSlotsRepository extends JpaRepository<TimeSlots, Integer>{
	
	@Query(value = "SELECT * FROM time_slots where active=true", nativeQuery = true)
	public List<TimeSlots> findAll1();
	
	
	@Query(value ="Select new map(ts.time_slots_id as id,ts.starting_time as starting_time,ts.class_time_table as class_time_table,"
			+ "ts.ending_time as ending_time,ts.created_date as created_date,ts.modified_date as modified_date,ts.duration as duration,"
			+ "ts.created_by as created_by,ts.modified_by as modified_by,ts.active as active,"
			+ "ts.created_username as created_username,ts.modified_username as modified_username,"
			+ "ts.starting_time_for_fornted as starting_time_for_fornted,ts.ending_time_for_fornted as ending_time_for_fornted,sc.school_name_short as school_name_short) From TimeSlots ts "
			+ "Left join Schools sc on sc.school_id=ts.school_id "
			+ "Where CONCAT(IfNull(ts.time_slots_id,''),'',IfNull(ts.starting_time,''),'',IfNull(ts.ending_time,''),'',IfNull(ts.created_date,''),'',IfNull(ts.created_by,''),'',IfNull(ts.created_username,''),'',IfNull(sc.school_name_short,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(ts.time_slots_id as id,ts.starting_time as starting_time,ts.class_time_table as class_time_table,"
			+ "ts.ending_time as ending_time,ts.created_date as created_date,ts.modified_date as modified_date,ts.duration as duration,"
			+ "ts.created_by as created_by,ts.modified_by as modified_by,ts.active as active,"
			+ "ts.created_username as created_username,ts.modified_username as modified_username,"
			+ "ts.starting_time_for_fornted as starting_time_for_fornted,ts.ending_time_for_fornted as ending_time_for_fornted,sc.school_name_short as school_name_short) From TimeSlots ts "
			+ "Left join Schools sc on sc.school_id=ts.school_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update TimeSlots ts set ts.active=false where ts.time_slots_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update TimeSlots ts set ts.active=true where ts.time_slots_id=?1")
	public void update1(Integer id);
	
	@Query(value = "Select Count(*)  From TimeSlots ts where ts.starting_time=?1 and ts.ending_time=?2 and ts.school_id=?3 and ts.active=true")
	public Integer countTimeSlots(String starting_time,String ending_time,Integer school_id);
	
	@Query(value = "Select Count(*)  From TimeSlots ts where ts.time_slots_id != ?1 and ts.starting_time=?2 and ts.ending_time=?3 and ts.school_id=?4 and ts.active=true")
	public Integer countTimeSlotsForUpdate(Integer time_slots_id,String starting_time,String ending_time,Integer school_id);
	
	@Query(value = "Select CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots,ts.time_slots_id as time_slots_id from time_slots ts where "
			+ "ts.school_id=?1 and ts.active=true",nativeQuery=true)
	public List<Map<String,Object>> getTimeSlots(Integer school_id);
	
	@Query(value = "Select CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots,ts.time_slots_id as time_slots_id from time_slots ts where "
			+ "ts.school_id=?1 and ts.class_time_table=false and ts.active=true",nativeQuery=true)
	public List<Map<String,Object>> getTimeSlotsForInternals(Integer school_id);
	
	
	@Query(value = "SELECT * FROM time_slots where active=true and class_time_table=false",nativeQuery=true)
	public List<TimeSlots> findAll11();
	
//	@Query(value = "SELECT * FROM time_slots where active=true",nativeQuery=true)
//	public List<TimeSlots> getTimeSlotMinAndMax();
	
	
	
	@Query(value = "SELECT ts FROM TimeSlots ts where ts.time_slots_id IN (select tte.time_slots_id from TimeTableEmployee tte where "
			+ "date(tte.selected_date)=date(:from_date) and tte.emp_id IN(:emp_id) and tte.active=true) and ts.active=true")
	public List<TimeSlots> getTimeSlotsAssignedForTheDay(Date from_date, List<Integer> emp_id);
	
	@Query(value = "SELECT count(*) FROM TimeSlots ts where ts.time_slots_id IN (select tte.time_slots_id from TimeTableEmployee tte where "
			+ "date(tte.selected_date)=date(:from_date) and tte.emp_id IN (:emp_id) and tte.active=true) and ts.active=true")
	public Integer getTimeSlotsAssignedForTheDay1(Date from_date, List<Integer> emp_id);

	@Query(value = "SELECT ts FROM TimeSlots ts where ts.time_slots_id IN (select tt.time_slots_id from TimeTable tt where "
			+ "date(tt.selected_date)=date(:from_date) and tt.room_id IN(:room_id) and tt.active=true) and ts.active=true")
	public List<TimeSlots> getTimeSlotsOfRoomsAssignedForTheDay(Date from_date, Integer room_id);

}
