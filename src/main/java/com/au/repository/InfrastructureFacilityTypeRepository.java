package com.au.repository;

import java.time.LocalDate;
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

import com.au.model.InfrastructureFacilityType;
import com.au.model.JobType;

@Transactional
@Repository
public interface InfrastructureFacilityTypeRepository extends JpaRepository<InfrastructureFacilityType, Integer> {
	
	@Query(value = "select count(*) from InfrastructureFacilityType ft where ft.facility_type_name=?1 and active=true")
	public Integer getCountFacilityType(String facility_type_name);
	
	@Query(value = "select count(*) from InfrastructureFacilityType ft where ft.facility_short_name=?1 and active=true")
	public Integer getCountFacilityShortName(String facility_short_name);
	
	@Query(value = "select count(*) from InfrastructureFacilityType ft where ft.facility_code=?1 and active=true")
	public Integer getCountFacilityCode(String facility_code);
	
	@Query(value = "select ft from InfrastructureFacilityType ft where ft.active=true")
	public  List<InfrastructureFacilityType> findAll1();
	
	@Query(value ="Select new map(ft.facility_type_id as id,ft.facility_type_name as facility_type_name,ft.facility_short_name as facility_short_name,"
			+ "ft.facility_code as facility_code,ft.description as description,ft.timetable_status as timetable_status,ft.remarks as remarks,"
			+ "ft.active as active,ft.created_date as created_date,ft.modified_date as modified_date,ft.created_by as created_by,ft.tt_status As tt_status,"
			+ "ft.modified_by as modified_by,ft.created_username as created_username,ft.modified_username as modified_username)"
			+ " From InfrastructureFacilityType ft "
			+ "Where CONCAT(IfNull(ft.facility_type_id,''),'',IfNull(ft.facility_type_name,''),'',IfNull(ft.facility_short_name,''),"
			+ "'',IfNull(ft.facility_code,''),'',IfNull(ft.description,''),'',IfNull(ft.timetable_status,''),'',IfNull(ft.remarks,''),"
			+ "'',IfNull(ft.active,''),'',IfNull(ft.created_date,''),'',IfNull(ft.created_by,''),'',IfNull(ft.created_username,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(ft.facility_type_id as id,ft.facility_type_name as facility_type_name,ft.facility_short_name as facility_short_name,"
			+ "ft.facility_code as facility_code,ft.description as description,ft.timetable_status as timetable_status,ft.remarks as remarks,"
			+ "ft.active as active,ft.created_date as created_date,ft.modified_date as modified_date,ft.created_by as created_by,ft.tt_status As tt_status,"
			+ "ft.modified_by as modified_by,ft.created_username as created_username,ft.modified_username as modified_username)"
			+ " From InfrastructureFacilityType ft")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update InfrastructureFacilityType ft set ft.active=false where ft.facility_type_id=?1")
	public void updateToDeactivate(Integer id);

	@Modifying
	@Query(value = "update InfrastructureFacilityType ft set ft.active=true where ft.facility_type_id=?1")
	public void updateToActivate(Integer id);
	
	@Query(value = "select ft.facility_code from InfrastructureFacilityType ft where ft.facility_type_id=?1 and ft.active=true")
	public String getFacilityCode(Integer facility_type_id);

	@Query(value = "select ift.facility_type_id As facility_type_id,ir.room_id As room_id,"
			+ "Concat(IfNull(ift.facility_type_name,''),'-',IfNull(ir.roomcode,'')) as RoomNameConcate,ift.timetable_status As tt_status,"
			+ "ift.facility_type_name as facility_type_name,ift.facility_short_name As facility_short_name , ir.roomcode as roomcode "
			+ " FROM infrastructure_facility_type ift "
			+ "left join acharya_erp.infrastructure_rooms ir on ir.facility_type_id=ift.facility_type_id "
			+ " where ir.show_in_event=true And ift.active=true And ift.timetable_status=false group by ift.facility_type_id",nativeQuery=true)
	public List<Map<String, Object>> getFacilityTypeBasedOnEvent();
	
	
	@Query(value = "select ift.facility_type_id As facility_type_id,ib.block_id As block_id,ib.block_name As block_name "
			+ " FROM infrastructure_blocks ib "
			+ " left join infrastructure_facility_type ift on ib.facility_type_id=ift.facility_type_id "
			+ "  where ift.active=true And ift.timetable_status=true ",nativeQuery=true)
	public List<Map<String, Object>> getFacilityTypeForTimeTable();

	
	@Query(value = "SELECT ir.room_id AS room_id, ir.facility_type_id AS facility_type_id, ir.roomcode AS roomcode, " +
            "ift.facility_type_name AS facility_type_name, ift.facility_short_name AS facility_short_name,ift.tt_status As tt_status, " +
            "ib.block_name AS block_name, ib.block_short_name AS block_short_name, ir.room_status AS room_status,ec.approved_status As approved_status, " + 
            "ec.event_start_time AS event_start_time, ec.event_end_time AS event_end_time,ebr.event_id as event_id " +
            "FROM infrastructure_facility_type ift " +
            "LEFT JOIN infrastructure_rooms ir ON ir.facility_type_id = ift.facility_type_id " +
            "LEFT JOIN infrastructure_blocks ib ON ir.block_id = ib.block_id " +
            "LEFT JOIN event_blocked_rooms ebr ON ir.room_id = ebr.room_id " +
            "LEFT JOIN event_creation ec ON ec.event_id = ebr.event_id " +
            "WHERE ec.active=true And ift.facility_type_id = :facility_type_id " +
            "AND ((ec.event_start_time BETWEEN :startDate AND :endDate) " +
            "OR (ec.event_end_time BETWEEN :startDate AND :endDate) " +
            "OR (ec.event_start_time IS NULL) " +
            "OR (ec.event_end_time IS NULL)) " +
            "GROUP BY ir.room_id, ec.event_start_time, ec.event_end_time", nativeQuery = true)
	List<Map<String, Object>> getEventRoomAvailability(
				@Param("facility_type_id") Integer facility_type_id,
				@Param("startDate") LocalDate startDate,
				@Param("endDate") LocalDate endDate);

	@Query(value = "SELECT ir.room_id AS room_id, ir.facility_type_id AS facility_type_id, ir.roomcode AS roomcode, " +
            "ift.facility_type_name AS facility_type_name, ift.facility_short_name AS facility_short_name,ift.tt_status As tt_status, " +
            "ib.block_name AS block_name, ib.block_short_name AS block_short_name, ir.room_status AS room_status,ec.approved_status As approved_status, " + 
            "ec.event_start_time AS event_start_time, ec.event_end_time AS event_end_time,ebr.event_id as event_id " +
            "FROM infrastructure_facility_type ift " +
            "LEFT JOIN infrastructure_rooms ir ON ir.facility_type_id = ift.facility_type_id " +
            "LEFT JOIN infrastructure_blocks ib ON ir.block_id = ib.block_id " +
            "LEFT JOIN event_blocked_rooms ebr ON ir.room_id = ebr.room_id " +
            "LEFT JOIN event_creation ec ON ec.event_id = ebr.event_id " +
            "WHERE ec.active=true And ift.facility_type_id = :facility_type_id And ift.timetable_status=false And ir.show_in_event=true "
            + " group by ir.roomcode,ift.facility_type_name,ib.block_name", nativeQuery = true)
	public List<Map<String, Object>> getEventRoomAvailability1(Integer facility_type_id);

	
	
}
