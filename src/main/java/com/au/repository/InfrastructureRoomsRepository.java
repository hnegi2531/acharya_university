package com.au.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.HostelRooms;
import com.au.model.InfrastructureRooms;

@Transactional
@Repository
public interface InfrastructureRoomsRepository extends JpaRepository<InfrastructureRooms, Integer>{
	
	@Query(value = "select rm from InfrastructureRooms rm where rm.active=true")
	public  List<InfrastructureRooms> findAll1();
	
	@Query(value ="Select new map(rm.room_id as id,rm.roomcode as roomcode,rm.manual_room_no as manual_room_no,ib.block_name as block_name,ib.block_short_name as block_short_name,"
			+ "rm.school_id as school_id,rm.block_id as block_id,rm.floor_id as floor_id,rm.remarks as remarks,rm.no_of_rooms as no_of_rooms,"
			+ "s.school_name as school_name,s.school_name_short as school_name_short,ft.facility_type_name as facility_type_name,ft.facility_short_name as facility_short_name,"
			+ "rm.description as description,rm.show_in_event as show_in_event,rm.strength as strength,rm.facility_type_id as facility_type_id,rm.area as area,if.floor_name as floor_name,"
			+ "rm.active as active,rm.created_date as created_date,rm.modified_date as modified_date,rm.created_by as created_by,"
			+ "rm.modified_by as modified_by,rm.created_username as created_username,rm.modified_username as modified_username)"
			+ " From InfrastructureRooms rm left join InfrastructureFacilityType ft on rm.facility_type_id=ft.facility_type_id"
			+ " left join InfrastructureBlocks ib on ib.block_id=rm.block_id left join Schools s on s.school_id=rm.school_id"
			+ " left join InfrastructureFloors if on if.floor_id=rm.floor_id "
			+ "Where CONCAT(IfNull(rm.room_id,''),'',IfNull(rm.roomcode,''),'',IfNull(rm.manual_room_no,''),"
			+ "'',IfNull(rm.school_id,''),'',IfNull(rm.block_id,''),'',IfNull(rm.floor_id,''),'',IfNull(ib.block_name,''),'',IfNull(ib.block_short_name,''),'',IfNull(s.school_name,''),'',IfNull(s.school_name_short,''),'',IfNull(if.floor_name,''),"
			+ "'',IfNull(rm.remarks,''),'',IfNull(rm.no_of_rooms,''),'',IfNull(rm.description,''),"
			+ "'',IfNull(rm.facility_type_id,''),'',IfNull(rm.area,''),'',IfNull(rm.strength,''),"
			+ "'',IfNull(rm.created_date,''),'',IfNull(rm.created_by,''),'',IfNull(rm.created_username,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(rm.room_id as id,rm.roomcode as roomcode,rm.manual_room_no as manual_room_no,ib.block_name as block_name,ib.block_short_name as block_short_name,"
			+ "rm.school_id as school_id,rm.block_id as block_id,rm.floor_id as floor_id,rm.remarks as remarks,rm.no_of_rooms as no_of_rooms,"
			+ "s.school_name as school_name,s.school_name_short as school_name_short,ft.facility_type_name as facility_type_name,ft.facility_short_name as facility_short_name,"
			+ "rm.description as description,rm.show_in_event as show_in_event,rm.strength as strength,rm.facility_type_id as facility_type_id,rm.area as area,if.floor_name as floor_name,"
			+ "rm.active as active,rm.created_date as created_date,rm.modified_date as modified_date,rm.created_by as created_by,"
			+ "rm.modified_by as modified_by,rm.created_username as created_username,rm.modified_username as modified_username)"
			+ " From InfrastructureRooms rm left join InfrastructureFacilityType ft on rm.facility_type_id=ft.facility_type_id"
			+ " left join InfrastructureBlocks ib on ib.block_id=rm.block_id left join Schools s on s.school_id=rm.school_id"
			+ " left join InfrastructureFloors if on if.floor_id=rm.floor_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update InfrastructureRooms rm set rm.active=false where rm.room_id=?1")
	public void updateToDeactivate(Integer id);

	@Modifying
	@Query(value = "update InfrastructureRooms rm set rm.active=true where rm.room_id=?1")                                      
	public void updateToActivate(Integer id);
	
	@Modifying
	@Query(value = "delete from infrastructure_rooms rm where rm.block_id=?1 and rm.school_id=?2 and rm.floor_id=?3",nativeQuery=true)
	public void deleteExistingData(Integer block_id,Integer school_id,Integer floor_id);
	
	@Query(value = "SELECT count(*) FROM infrastructure_rooms where block_id=?1 and school_id=?2 and floor_id=?3", nativeQuery = true)
	public Integer checkForExistingData(Integer block_id,Integer school_id,Integer floor_id);
	
	@Query(value = "select * from infrastructure_rooms ir where ir.room_id IN"
			+ "(select ir.room_id from infrastructure_rooms where ir.facility_type_id IN"
			+ "(select ft.facility_type_id from infrastructure_facility_type ft where ft.timetable_status=1))",nativeQuery=true)
	public List<InfrastructureRooms> getAllActiveRoomsForTimeTable();
	
	

	@Query(value = "select CONCAT(IfNull(ir.roomcode,''),'-',IfNull(ir.manual_room_no,'')) as concate_room_name ,ir.room_id as room_id from infrastructure_rooms ir "
			+ "where ir.facility_type_id in (select ft.facility_type_id from infrastructure_facility_type ft where ft.timetable_status !=0 ) "
			+ "and ir.room_id not in (Select distinct tt.room_id From time_table tt left join time_table_employee tte on "
			+ "tte.time_table_id=tt.time_table_id Where tte.time_slots_id=?1 And date(tte.selected_date) in (?2) and tt.week_day=?3 and tte.active=true) And  ir.active=true",nativeQuery=true)
	public List<Map<String,Object>> getAllActiveRoomsForTimeTableBsn(Integer time_slot_id,List<Date> dates, String day); 

	@Query(value = "select CONCAT(ir.roomcode,'-',(select b.block_short_name from infrastructure_blocks b where "
			+ "ir.block_id=b.block_id and b.active=true)) as concate_room_name ,ir.room_id as room_id from infrastructure_rooms ir where ir.facility_type_id in "
			+ "(select ft.facility_type_id from infrastructure_facility_type ft where ft.timetable_status !=0 ) "                                                
			+ " and ir.active=true",nativeQuery=true)
	public List<Map<String,Object>> getAllActiveRoomsForTimeTableBsn();

	@Query(value = "select CONCAT(ir.roomcode,'-',(select ft.facility_short_name from infrastructure_facility_type ft where "
			+ "ir.facility_type_id=ft.facility_type_id and ft.active=true)) as concate_room_name ,ir.room_id as room_id from infrastructure_rooms ir "
			+ "where ir.facility_type_id in (select ft.facility_type_id from infrastructure_facility_type ft where ft.timetable_status !=0 ) "
			+ "and ir.room_id not in (Select distinct tt.room_id From time_table tt Where tt.time_slots_id=?1 And date(tt.selected_date) between ?2 And ?3 And tt.active=true) And  ir.active=true",nativeQuery=true)
	public List<Map<String,Object>> getAllActiveRoomsForTimeTableBsn(Integer time_slot_id,Date from_date,Date to_date);
	
	@Query(value = "select CONCAT(IfNull(ir.roomcode,''),'-',IfNull(ir.manual_room_no,'')) as concate_room_name ,ir.room_id as room_id from infrastructure_rooms ir "
			+ "where ir.facility_type_id in (select ft.facility_type_id from infrastructure_facility_type ft where ft.timetable_status !=0 ) "
			+ "and ir.room_id not in (Select distinct tt.room_id From time_table tt Where tt.time_slots_id=?1 And date(tt.selected_date)=date(?2) And tt.active=true) And  ir.active=true",nativeQuery=true)
	public List<Map<String, Object>> roomsForTimeTableRoomSwapping(Integer time_slot_id, Date date1);

	@Query(value = "select ir.roomcode, ir.room_id from infrastructure_rooms ir left join infrastructure_facility_type ft on ir.facility_type_id=ft.facility_type_id " +
			"where ir.facility_type_id in (4,18,29,38,41,72) and ir.active=true and ft.timetable_status=true",nativeQuery=true)
	public List<Map<String, Object>> getRoomsForInternals();

	@Query(value = "SELECT RIGHT(roomcode, 2) FROM infrastructure_rooms where block_id=?1 And floor_id=?2 And school_id =?3  order by created_date desc limit 1",nativeQuery=true)
	public Integer getLatestRoomCode(Integer block_id, Integer floor_id, Integer school_id);

	
	@Query(value = "SELECT ir.room_id As room_id,tt.selected_date As selected_date,ir.roomcode As roomcode,f.floor_id As floor_id,"
			+ "tt.time_slots_id As time_slots_id,ts.starting_time As starting_time,ts.ending_time As ending_time "
			+ "FROM infrastructure_rooms ir "
			+ "left join time_table tt on tt.room_id = ir.room_id "
			+ "left join infrastructure_blocks ib on ib.block_id = ir.block_id "
			+ "left join infrastructure_floors f on f.floor_id = ir.floor_id "
			+ "left join time_slots ts on ts.time_slots_id=tt.time_slots_id " 
			+ "where (:block_id IS NULL OR ib.block_id = :block_id) And "
			+ "(:floor_id IS NULL OR f.floor_id = :floor_id) "
			+ "And month(tt.selected_date)= :month And year(tt.selected_date)= :year And tt.active=true "
			+ "Group by ir.room_id,tt.selected_date,ir.roomcode,f.floor_id ",nativeQuery=true)
	public List<Map<String, Object>> getEventRoomAvailabilityForTimeTable(Integer block_id, Integer floor_id,
			Integer month, Integer year); 

	

}
