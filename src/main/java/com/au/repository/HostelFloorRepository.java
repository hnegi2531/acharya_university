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

import com.au.dto.WardenRequest;
import com.au.model.HostelFloor;

@Repository
@Transactional
public interface HostelFloorRepository extends JpaRepository<HostelFloor, Integer>{

	@Query(value = "select hf from HostelFloor hf where hf.active=true")
	public List<HostelFloor> findAll1();
	
	@Modifying
	@Query(value = "update HostelFloor hf set hf.active=false where hf.hostelFloorId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update HostelFloor hf set hf.active=true where hf.hostelFloorId=?1")
	public void update1(Integer id);

	@Query(value = "SELECT * FROM hostel_floor where hostels_block_id=?1",nativeQuery = true)
	public List<HostelFloor> getFloorByBlockid(Integer block_id);

	@Query(value="SELECT  wa.name as Warden_Name,hb.block_name,hf.floor_name,hf.created_date,hf.created_username,"
			+ "hf.active,hf.created_by,hf.hostel_floor_id "
			+ "FROM hostel_floor hf "
			+ "left join hostel_blocks hb on hf.hostels_block_id=hb.hostel_block_id "
			+ "left join doctor_warden wa on wa.doctor_id = hf.wardens_id",nativeQuery =true)
	public List<Map<String, Object>> fetchhostelFloorIndex();
	
	@Query(value = "Select new map(hf.hostelFloorId as id,hf.floorName as floorName,hf.hostelsBlockId as hostelsBlockId,"
			+ "hf.wardensId as wardensId,hf.totalNoOfRooms as totalNoOfRooms,hf.noOfRoomsFree as noOfRoomsFree,hf.createdBy as createdBy,"
			+ "hf.modifiedBy as modifiedBy,hf.createdDate as createdDate,hf.modifiedDate as modifiedDate,hf.createdUsername as createdUsername,"
			+ "hf.modifiedUsername as modifiedUsername,hf.active as active,hb.blockName as blockName,hb.blockShortName as blockShortName,"
			+ "hw.wardenName as wardenName) From HostelFloor hf "
			+ "Left join HostelBlocks hb on hb.hostelBlockId=hf.hostelsBlockId "
			+ "Left join HostelWarden hw on hw.wardenId=hf.wardensId "
			+ "Where CONCAT(IfNull(hf.hostelFloorId,''),'',IfNull(hf.floorName,''),'',IfNull(hf.totalNoOfRooms,''),'',"
			+ "IfNull(hf.noOfRoomsFree,''),'',IfNull(hf.createdBy,''),'',IfNull(hf.createdDate,''),'',IfNull(hf.createdUsername,''),'',"
			+ "IfNull(hb.blockName,''),'',IfNull(hb.blockShortName,''),'',IfNull(hw.wardenName,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(hf.hostelFloorId as id,hf.floorName as floorName,hf.hostelsBlockId as hostelsBlockId,"
			+ "hf.wardensId as wardensId,hf.totalNoOfRooms as totalNoOfRooms,hf.noOfRoomsFree as noOfRoomsFree,hf.createdBy as createdBy,"
			+ "hf.modifiedBy as modifiedBy,hf.createdDate as createdDate,hf.modifiedDate as modifiedDate,hf.createdUsername as createdUsername,"
			+ "hf.modifiedUsername as modifiedUsername,hf.active as active,hb.blockName as blockName,hb.blockShortName as blockShortName,"
			+ "hw.wardenName as wardenName) From HostelFloor hf "
			+ "Left join HostelBlocks hb on hb.hostelBlockId=hf.hostelsBlockId "
			+ "Left join HostelWarden hw on hw.wardenId=hf.wardensId")
	public Page<Object> findAll3(Pageable pageable);
	
	
	@Query(value ="Select hf.floorName From HostelFloor hf where hf.hostelFloorId=?1 and hf.active=true")
	public String getFloorName(Integer hostelFloorId);
	
	@Query(value ="Select GROUP_CONCAT(hf.floor_name) From hostel_floor hf where hf.hostel_floor_id in (:hostel_floor_id)",nativeQuery = true)
	public String getFloorShortNameCommaSeperated(List<Integer> hostel_floor_id);
	
	@Query(value = "SELECT hf.floorName as floorName,hf.hostelFloorId as hostelFloorId FROM HostelFloor hf where hf.hostelsBlockId=?1")
	public List<Map<String, Object>> getFloorsByBlockid(Integer hostels_block_id);
	
	@Query(value = "SELECT hf.hostelFloorId FROM HostelFloor hf where hf.hostelsBlockId=?1")
	public List<Integer> getListOfFloorIds(Integer block_id);
	
	@Query(value = "Select count(hf.hostel_floor_id) from hostel_floor hf where hf.hostels_block_id=?1",nativeQuery=true)
	public Integer getCountOfFloors(Integer hostelsBlockId);

}
