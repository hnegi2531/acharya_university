package com.au.repository;

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
import com.au.model.HostelRooms;



@Repository
@Transactional
public interface HostelRoomsRepository extends JpaRepository<HostelRooms, Integer>{
	
	public Boolean existsByroomName(String roomName);
	
	@Modifying
	@Query(value = "update HostelRooms hr set hr.active=false where hr.hostelRoomId=?1")
	public void updateAcademicWorkLoad(Integer id);

	@Modifying
	@Query(value = "update HostelRooms hr set hr.active=true where hr.hostelRoomId=?1")
	public void updateAcademicWorkLoad1(Integer id);

	@Query(value = "select hr from HostelRooms hr where hr.active=true")
	List<HostelRooms> findAll1();

	@Query(value = "SELECT hr.hostel_room_id,hr.active,hr.created_username,hr.created_date,hr.created_by,hr.room_name, hb.block_name"
			+ " FROM hostel_rooms hr Left join hostel_blocks hb "
			+ "on hr.hostels_block_id=hb.hostel_block_id",nativeQuery = true)
	public List<Map<String, Object>> getAllRoomDetails();
	
	@Query(value ="Select new map(hr.hostelRoomId as id,hr.roomName as roomName,hr.standardAccessories as hrstandardAccessories,"
			+ "hr.roomTypeId as roomTypeId,hr.templateId as templateId,hr.hostelsBlockId as hostelsBlockId,hr.hostelsFloorId as hostelsFloorId,"
			+ "hr.createdBy as createdBy,hr.modifiedBy as modifiedBy,hr.createdDate as createdDate,hr.modifiedDate as modifiedDate,"
			+ "hr.active as active,hr.createdUsername as createdUsername,hr.modifiedUsername as modifiedUsername,"
			+ "hrt.roomType as roomType,hb.blockName as blockName,"
			+ "hb.blockShortName as blockShortName,hf.floorName as floorName) From HostelRooms hr "
			+ "Left join HostelRoomType hrt on hrt.roomTypeId=hr.roomTypeId "
			+ "Left join HostelBlocks hb on hb.hostelBlockId=hr.hostelsBlockId "
			+ "Left join HostelFloor hf on hf.hostelFloorId=hr.hostelsFloorId "
			+ "Where CONCAT(IfNull(hr.hostelRoomId,''),'',IfNull(hr.roomName,''),'',IfNull(hr.createdBy,''),'',IfNull(hr.createdDate,''),'',"
			+ "IfNull(hr.createdUsername,''),'',IfNull(hr.standardAccessories,''),'',"
			+ "IfNull(hrt.roomType,''),'',IfNull(hb.blockName,''),'',IfNull(hb.blockShortName,''),'',IfNull(hf.floorName,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(hr.hostelRoomId as id,hr.roomName as roomName,hr.standardAccessories as hrstandardAccessories,"
			+ "hr.roomTypeId as roomTypeId,hr.templateId as templateId,hr.hostelsBlockId as hostelsBlockId,hr.hostelsFloorId as hostelsFloorId,"
			+ "hr.createdBy as createdBy,hr.modifiedBy as modifiedBy,hr.createdDate as createdDate,hr.modifiedDate as modifiedDate,"
			+ "hr.active as active,hr.createdUsername as createdUsername,hr.modifiedUsername as modifiedUsername,"
			+ "hrt.roomType as roomType,hb.blockName as blockName,"
			+ "hb.blockShortName as blockShortName,hf.floorName as floorName) From HostelRooms hr "
			+ "Left join HostelRoomType hrt on hrt.roomTypeId=hr.roomTypeId "
			+ "Left join HostelBlocks hb on hb.hostelBlockId=hr.hostelsBlockId "
			+ "Left join HostelFloor hf on hf.hostelFloorId=hr.hostelsFloorId")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM HostelRooms hr where hr.hostelsBlockId=?1 and hr.hostelsFloorId=?2  and hr.active=true")
	public Integer countRoomsOnBlockFloor(Integer hostelsBlockId,Integer hostelsFloorId);
	
	@Query(value ="Select new map(hr.hostelRoomId as id,hr.roomName as roomName,hr.standardAccessories as hrstandardAccessories,"
			+ "hrt.roomType as roomType,hb.blockName as blockName,"
			+ "hb.blockShortName as blockShortName,hf.floorName as floorName) From HostelRooms hr "
			+ "Left join HostelRoomType hrt on hrt.roomTypeId=hr.roomTypeId "
			+ "Left join HostelBlocks hb on hb.hostelBlockId=hr.hostelsBlockId "
			+ "Left join HostelFloor hf on hf.hostelFloorId=hr.hostelsFloorId Where hr.hostelsBlockId=?1 and hr.hostelsFloorId=?2  and hr.active=true")
	public List<HashMap<String, Object>> allHostelOnBlockAndFloor(Integer hostelsBlockId,Integer hostelsFloorId);
	
	@Query(value ="Select GROUP_CONCAT(hr.room_name) From hostel_rooms hr where hr.hostel_room_id in (:hostel_room_id)",nativeQuery = true)
	public String getRoomNameCommaSeperated(List<Integer> hostel_room_id);
	
	@Query(value = "SELECT hr.hostel_room_id,hr.active,hr.created_username,hr.created_date,hr.created_by,hr.room_name,hrt.room_type,"
			+ "hrt.number_of_beds FROM hostel_rooms hr Left join hostel_room_type hrt on hrt.room_type_id=hr.room_type_id "
			+ "where hr.hostels_block_id=?1 and hr.hostels_floor_id=?2",nativeQuery = true)
	public List<Map<String, Object>> getAllRoomsDetails(Integer hostelsBlockId, Integer hostelsFloorId);
	
	@Query(value = "Select count(hr.hostel_room_id) from hostel_rooms hr where hr.hostels_block_id=?1",nativeQuery=true)
	public Integer getCountOfRooms(Integer hostelsBlockId);

	@Query(value = "SELECT hf.hostel_floor_id as hostel_floor_id,hf.floor_name as floor_name,hf.hostels_block_id as hostels_block_id,hf.wardens_id as wardens_id "
			+ " FROM hostel_floor hf Left join hostel_blocks hb on hb.hostel_block_id=hf.hostels_block_id "
			+ "where hf.hostels_block_id=?1 and hf.hostel_floor_id=?2",nativeQuery = true)	
	public List<Map<String, Object>> getHostelFloorDetails(Integer block_id, Integer id);

	@Query(value ="Select hr.hostel_room_id as id,hr.room_name as room_name,hr.standard_accessories as standard_accessories,"
			+ "hr.room_type_id as room_type_id,hr.template_id as template_id,hr.hostels_block_id as hostels_block_id,hr.hostels_floor_id as hostels_floor_id,"
			+ "hr.created_by as created_by,hr.modified_by as modified_by,hr.created_date as created_date,hr.modified_date as modified_date,"
			+ "hr.active as active,hr.created_username as created_username,hr.modified_username as modified_username,"
			+ "hb.hostel_bed_id as hostel_bed_id,hb.bed_name as bed_name From hostel_rooms hr "
			+ "Left join hostel_beds hb on hb.hostels_room_id=hr.hostel_room_id "
			+ "where hr.hostel_room_id=?1 And hb.active=true And hr.active=true",nativeQuery = true)	
	public List<Map<String, Object>> getAllHostelBedDetails(Integer hostelRoomId);
	
	@Query(value ="Select new map(hr.hostelRoomId as hostelRoomId,hr.roomName as roomName,hr.standardAccessories as hrstandardAccessories,"
			+ "hr.roomTypeId as roomTypeId,hr.templateId as templateId,hr.hostelsBlockId as hostelsBlockId,hr.hostelsFloorId as hostelsFloorId,"
			+ "hr.createdBy as createdBy,hr.modifiedBy as modifiedBy,hr.createdDate as createdDate,hr.modifiedDate as modifiedDate,"
			+ "hr.active as active,hr.createdUsername as createdUsername,hr.modifiedUsername as modifiedUsername,"
			+ "hrt.roomType as roomType,hb.blockName as blockName,"
			+ "hb.blockShortName as blockShortName,hf.floorName as floorName) From HostelRooms hr "
			+ "Left join HostelRoomType hrt on hrt.roomTypeId=hr.roomTypeId "
			+ "Left join HostelBlocks hb on hb.hostelBlockId=hr.hostelsBlockId "
			+ "Left join HostelFloor hf on hf.hostelFloorId=hr.hostelsFloorId "
			+ "Where hr.hostelRoomId Not in (Select Distinct hba.hostelRoom From HostelBedAssignment hba Where hba.active=true) "
			+ "And hr.hostelRoomId Not in (Select hra.hostelRoom From HostelRoomAssignment hra Where hra.vacant=false And hra.active=true) And hr.active=true ")
	public List<HashMap<String, Object>> fetchAllUnassignedRoom();
	
}
