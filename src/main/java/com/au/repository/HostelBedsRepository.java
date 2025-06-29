package com.au.repository;

import java.util.Collection;
import java.util.HashMap;
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

import com.au.model.HostelBeds;

@Repository
@Transactional
public interface HostelBedsRepository extends JpaRepository<HostelBeds, Integer>{

	@Query(value = "select hb from HostelBeds hb where hb.active=true")
	public List<HostelBeds> findAll1();
	
	@Modifying
	@Query(value = "update HostelBeds hb set hb.active=false where hb.hostelBedId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update HostelBeds hb set hb.active=true where hb.hostelBedId=?1")
	public void update1(Integer id);
	
	@Query(value = "SELECT hbed.active,hbed.hostel_bed_id, hb.block_name,hr.room_name,hbed.bed_name FROM hostel_blocks hb inner join "
			+ "hostel_rooms hr on hr.hostels_block_id = hb.hostel_block_id inner join "
			+ "hostel_beds hbed on hr.hostel_room_id=hbed.hostels_room_id",nativeQuery = true)
	public List<Map<String, Object>> getHosteBedDetails();
	
	@Query(value = "Select new map(hb.hostelBedId as id,hb.hostelBedNumber as hostelBedNumber,hb.bedName as bedName,"
			+ "hb.hostelsFloorId as hostelsFloorId,hb.hostelsBlockId as hostelsBlockId,hb.hostelsRoomId as hostelsRoomId,"
			+ "hb.createdDate as createdDate,hb.modifiedDate as modifiedDate,hb.createdBy as createdBy,hb.modifiedBy as modifiedBy,"
			+ "hb.active as active,hb.createdUsername as createdUsername,hb.modifiedUsername as modifiedUsername,"
			+ "hf.floorName as floorName,hbl.blockShortName as blockShortName,hr.roomName as roomName,hr.roomTypeId as roomTypeId) From HostelBeds hb "
			+ "left Join HostelFloor hf On hf.hostelFloorId=hb.hostelsFloorId "
			+ "left Join HostelBlocks hbl On hbl.hostelBlockId=hb.hostelsBlockId "
			+ "left Join HostelRooms hr On hr.hostelRoomId=hb.hostelsRoomId "
			+ "Where CONCAT(IfNull(hb.hostelBedId,''),'',IfNull(hb.hostelBedNumber,''),'',IfNull(hb.bedName,''),'',"
			+ "IfNull(hf.floorName,''),'',IfNull(hbl.blockShortName ,''),'',IfNull(hr.roomName,''),'',IfNull(hb.createdDate,''),'',"
			+ "IfNull(hb.createdUsername,''),'',IfNull(hb.createdBy,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(hb.hostelBedId as id,hb.hostelBedNumber as hostelBedNumber,hb.bedName as bedName,"
			+ "hb.hostelsFloorId as hostelsFloorId,hb.hostelsBlockId as hostelsBlockId,hb.hostelsRoomId as hostelsRoomId,"
			+ "hb.createdDate as createdDate,hb.modifiedDate as modifiedDate,hb.createdBy as createdBy,hb.modifiedBy as modifiedBy,"
			+ "hb.active as active,hb.createdUsername as createdUsername,hb.modifiedUsername as modifiedUsername,"
			+ "hf.floorName as floorName,hbl.blockShortName as blockShortName,hr.roomName as roomName,hr.roomTypeId as roomTypeId) From HostelBeds hb "
			+ "left Join HostelFloor hf On hf.hostelFloorId=hb.hostelsFloorId "
			+ "left Join HostelBlocks hbl On hbl.hostelBlockId=hb.hostelsBlockId "
			+ "left Join HostelRooms hr On hr.hostelRoomId=hb.hostelsRoomId ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT hb.hostelsFloorId FROM HostelBeds hb where hb.hostelsBlockId=?1")
	public List<Integer> getListOfFloorIds(Integer hostelsBlockId);

	@Query(value = "SELECT hb.hostelsRoomId FROM HostelBeds hb where hb.hostelsFloorId=?1")
	public List<Integer> getListOfRoomIds(Integer hostelsFloorId);
	
	@Query(value = "SELECT new map(hb.hostelBedId as hostelBedId,hb.hostelBedNumber as hostelBedNumber,hb.bedName as bedName,"
			+ "hb.hostelsRoomId as hostelsRoomId,hrm.roomTypeId as roomTypeId,hrm.numberOfBeds as numberOfBeds,"
			+ "hrm.roomType as roomType,hrm.nomenclature as nomenclature,hr.roomName as roomName) "
			+ "FROM HostelBeds hb "
			+ "left join HostelRooms hr on hb.hostelsRoomId=hr.hostelRoomId "
			+ "left join HostelRoomType hrm on hrm.roomTypeId=hr.roomTypeId "
			+ "where hb.hostelsRoomId=?1")
	public List<HashMap<String, Object>> getAllBedDetails(Integer room_id);
	
	@Query(value = "SELECT hb.hostelsRoomId FROM HostelBeds hb where hb.hostelsBlockId=?1 and hb.hostelsFloorId=?2")
	public List<Integer> getListOfRoomIdsByBlockAndFloor(Integer hostelsBlockId, Integer hostelsFloorId);
	
	@Query(value = "Select count(hb.hostel_bed_id) from hostel_beds hb where hb.hostels_block_id=?1",nativeQuery=true)
	public Integer getCountOfBeds(Integer hostelsBlockId);
	
	@Query(value = "Select hb.hostel_block_id as id,hb.block_name as block_name,"
			+ "(Select count(hf.hostel_floor_id) from hostel_floor hf where hf.hostels_block_id = hb.hostel_block_id) as countOfFloors,"
			+ "(Select count(hr.hostel_room_id) from hostel_rooms hr where hr.hostels_block_id=hb.hostel_block_id) as countOfRooms,"
			+ "(Select count(hb.hostel_bed_id) from hostel_beds hb where hb.hostels_block_id=hb.hostel_block_id) as countOfBeds "
			+ "From hostel_blocks hb",nativeQuery=true)
	public List<Map<String, Object>> getCountOfFloorsRoomsBeds();

	@Query(value = "Select new map(hr.hostelRoomId as hostelRoomId,hr.roomName as roomName,hr.standardAccessories as hrstandardAccessories,"
			+ "hrt.roomType as roomType,hb.blockName as blockName,hr.roomTypeId as roomTypeId,"
			+ "hb.blockShortName as blockShortName,hb.hostelBlockId as hostelBlockId,hf.floorName as floorName,hf.hostelFloorId as hostelFloorId,"
			+ "hbd.hostelBedId as hostelBedId,hbd.bedName as bedName,hbd.bedStatus as bedStatus,std.student_id as studentId,"
			+ "std.student_name as studentName,std.auid as auid,hba.createdDate as BlockedDate,hba.hostelBedAssignmentId as hostelBedAssignmentId,"
			+ "hba.cancelledRemarks as cancelledRemarksStatus,hba.active as occupiedStatusOfBed) From HostelBeds hbd "
			+ "left join HostelRooms hr on hbd.hostelsRoomId=hr.hostelRoomId "
			+ "Left join HostelRoomType hrt on hrt.roomTypeId=hr.roomTypeId "
			+ "Left join HostelBlocks hb on hb.hostelBlockId=hbd.hostelsBlockId "
			+ "Left join HostelFloor hf on hf.hostelFloorId=hbd.hostelsFloorId "
			+ "Left join HostelBedAssignment hba on (hba.hostelBed=hbd.hostelBedId And hba.cancelledRemarks Is Null And hba.vacateBy is null And hba.active = true) "
			+ "Left join Student_Details std on hba.student=std.student_id "
			+ "Where hbd.hostelsBlockId=:hostelsBlockId And (:hostelsFloorId IS NUll OR hbd.hostelsFloorId=:hostelsFloorId) And hr.roomTypeId=:roomTypeId "
			+ "And hbd.hostelsRoomId Not in (Select hra.hostelRoom From HostelRoomAssignment hra Where hra.vacant=true and hra.active=true) And hbd.active=true And hr.active=true ")
	public List<HashMap<String, Object>> hostelBedsByHostelBlockAndFloor(
			@Param("hostelsBlockId") Integer hostelsBlockId, @Param("hostelsFloorId") Integer hostelsFloorId,
			@Param("roomTypeId") Integer roomTypeId);

	@Modifying
	@Query(value = "update HostelBeds hb set hb.bedStatus=?1 where hb.hostelBedId=?2")
	public void updateBedStatusByBedId(String bedStatus, Integer hostelBedId);
	
	@Query(value ="Select new map(hr.hostelRoomId as hostelRoomId,hr.roomName as roomName,hr.standardAccessories as hrstandardAccessories,"
			+ "hrt.roomType as roomType,hb.blockName as blockName,hr.roomTypeId as roomTypeId,"
			+ "hb.blockShortName as blockShortName,hf.floorName as floorName,hbd.hostelBedId as hostelBedId,hbd.bedName as bedName,hbd.bedStatus as bedStatus,"
			+ "hf.hostelFloorId as hostelFloorId,hb.hostelBlockId as hostelBlockId) From HostelBeds hbd "
			+ "left join HostelRooms hr on hbd.hostelsRoomId=hr.hostelRoomId "
			+ "Left join HostelRoomType hrt on hrt.roomTypeId=hr.roomTypeId "
			+ "Left join HostelBlocks hb on hb.hostelBlockId=hbd.hostelsBlockId "
			+ "Left join HostelFloor hf on hf.hostelFloorId=hbd.hostelsFloorId "
			+ "Where hbd.hostelBedId Not In (Select hba.hostelBed From HostelBedAssignment hba Where hba.active=true) "
			+ "And hbd.hostelsRoomId Not in (Select hra.hostelRoom From HostelRoomAssignment hra Where hra.vacant=true and hra.active=true) And hbd.active=true And hr.active=true ")
	public List<HashMap<String, Object>> unassignedBedDetails();
	
	@Modifying
	@Query(value = "update HostelBeds hb set hb.bedStatus=?1 where hb.hostelsRoomId=?2")
	public void updateBedStatusByRoomId(String bedStatus, Integer hostelsRoomId);

	@Query(value ="Select new map(hr.hostelRoomId as hostelRoomId,hr.roomName as roomName,hr.standardAccessories as hrstandardAccessories,"
			+ "hrt.roomType as roomType,hb.blockName as blockName,hr.roomTypeId as roomTypeId,"
			+ "hb.blockShortName as blockShortName,hf.floorName as floorName,hbd.hostelBedId as hostelBedId,hbd.bedName as bedName,hbd.bedStatus as bedStatus,"
			+ "hf.hostelFloorId as hostelFloorId,hb.hostelBlockId as hostelBlockId,std.student_id as studentId,std.student_name as studentName,"
			+ "std.auid as auid,hba.createdDate as BlockedDate,hba.hostelBedAssignmentId as hostelBedAssignmentId,"
			+ "hba.cancelledRemarks as cancelledRemarksStatus,hba.active as occupiedStatusOfBed) From HostelBeds hbd "
			+ "left join HostelRooms hr on hbd.hostelsRoomId=hr.hostelRoomId "
			+ "Left join HostelRoomType hrt on hrt.roomTypeId=hr.roomTypeId "
			+ "Left join HostelBlocks hb on hb.hostelBlockId=hbd.hostelsBlockId "
			+ "Left join HostelFloor hf on hf.hostelFloorId=hbd.hostelsFloorId "
			+ "Left join HostelBedAssignment hba on (hba.hostelBed=hbd.hostelBedId And hba.cancelledRemarks Is Null And hba.active = true) "
			+ "Left join Student_Details std on hba.student=std.student_id "
			+ "Where hbd.hostelsBlockId=:hostelsBlockId And (:roomTypeId Is Null OR hr.roomTypeId=:roomTypeId) And hbd.active=true ")
	public List<HashMap<String, Object>> hostelBedsByHostelBlockId(Integer hostelsBlockId, Integer roomTypeId);

	public List<HostelBeds> findByHostelsRoomId(Integer hostelRoomId);

	@Query(value = "Select hb.hostel_block_id as id,hb.block_name as block_name,"
			+ "(Select count(hf.hostel_floor_id) from hostel_floor hf where hf.hostels_block_id = hb.hostel_block_id ) as countOfFloors,"
			+ "(Select count(hr.hostel_room_id) from hostel_rooms hr where hr.hostels_block_id=hb.hostel_block_id And (:academicYearId is Null Or hr.ac_year_id=:academicYearId)) as countOfRooms,"
			+ "(Select count(hbe.hostel_bed_id) from hostel_beds hbe where hbe.hostels_block_id=hb.hostel_block_id And (:academicYearId is Null Or hbe.ac_year_id=:academicYearId)) as countOfBeds,"
			+ "(Select count(hbeos.hostel_bed_id) from hostel_beds hbeos where hbeos.hostel_bed_id in "
			+ "(SELECT distinct hostel_bed_id FROM hostel_bed_assignment hba Where hba.ac_year_id=:academicYearId and hba.hostel_block_id=hb.hostel_block_id and (hba.cancelled_remarks is null or ifnull(hba.cancelled_remarks,'NOT CANCELLED') = 'NOT CANCELLED') "
			+ "and from_date is not null and hba.active=true) And (hbeos.bed_status='Occupied' Or hbeos.bed_status='Occupied-Assigned') And hbeos.active=true) as occupiedCountBeds "
			+ "From hostel_blocks hb Where hb.active=true group by hb.hostel_block_id ",nativeQuery=true)
	List<Map<String, Object>> getCountOfFloorsRoomsBedsByAcademicyear(Integer academicYearId);
}
