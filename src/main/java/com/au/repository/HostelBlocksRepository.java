package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.HostelBlocks;

@Repository
@Transactional
public interface HostelBlocksRepository  extends JpaRepository<HostelBlocks, Integer>{ 

	public Boolean existsByblockName(String blockName);
	public Boolean existsByblockShortName(String blockShortName);
	
	@Query(value = "select h from HostelBlocks h where h.active=true")
	public List<HostelBlocks> findAll1();
	
	@Modifying
	@Query(value = "update HostelBlocks h set h.active=false where h.hostelBlockId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update HostelBlocks h set h.active=true where h.hostelBlockId=?1")
	public void update1(Integer id);
	
	
	@Query(value = "SELECT * FROM doctor_warden where doctor_warden_type=?1 and active=true",nativeQuery = true)
	public List<Map<String, Object>> getAllDoctorWardenDetails(String doctor_warden_type);
	
	@Query(value = "select new map(hb.hostelBlockId as id,hb.blockName as blockName,hb.blockShortName as blockShortName,"
			+ "hb.hostelType as hostelType,hb.createdBy as createdBy,hb.modifiedBy as modifiedBy,"
			+ "hb.createdDate as createdDate,hb.modifiedDate as modifiedDate,hb.active as active,"
			+ "hb.createdUsername as createdUsername,hb.modifiedUsername as modifiedUsername,hb.doctorsName as doctorsName,"
			+ "hb.serviceProviderId as serviceProviderId,hb.wardensId as wardensId,hb.noOfFreeBeds as noOfFreeBeds,"
			+ "hb.totalBeds as totalBeds,hb.totalFreeRooms as totalFreeRooms,hb.totalNoRooms as totalNoRooms,"
			+ "hb.totalFloors as totalFloors,hb.address as address,hb.noOfOccupiedBed as noOfOccupiedBed,"
			+ "hb.noOfBedUnderMaintenance as noOfBedUnderMaintenance,hb.remarks as remarks) "
			+ "from HostelBlocks hb "
			+ "where CONCAT(IfNull(hb.hostelBlockId,''),'',IfNull(hb.blockName,''),'',IfNull(hb.blockShortName,''),'',IfNull(hb.remarks,''),"
			+ "'',IfNull(hb.hostelType,''),'',IfNull(hb.doctorsName,''),'',IfNull(hb.serviceProviderId,''),'',IfNull(hb.noOfBedUnderMaintenance,''),"
			+ "'',IfNull(hb.wardensId,''),'',IfNull(hb.noOfFreeBeds,''),'',IfNull(hb.totalBeds,''),'',IfNull(hb.totalFreeRooms,''),"
			+ "'',IfNull(hb.totalNoRooms,''),'',IfNull(hb.totalFloors,''),'',IfNull(hb.address,''),'',IfNull(hb.noOfOccupiedBed,''),"
			+ "'',IfNull(hb.createdBy,''),'',IfNull(hb.createdDate,'')) LIKE %?1% ")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(hb.hostelBlockId as id,hb.blockName as blockName,hb.blockShortName as blockShortName,"
			+ "hb.hostelType as hostelType,hb.createdBy as createdBy,hb.modifiedBy as modifiedBy,"
			+ "hb.createdDate as createdDate,hb.modifiedDate as modifiedDate,hb.active as active,"
			+ "hb.createdUsername as createdUsername,hb.modifiedUsername as modifiedUsername,hb.doctorsName as doctorsName,"
			+ "hb.serviceProviderId as serviceProviderId,hb.wardensId as wardensId,hb.noOfFreeBeds as noOfFreeBeds,"
			+ "hb.totalBeds as totalBeds,hb.totalFreeRooms as totalFreeRooms,hb.totalNoRooms as totalNoRooms,"
			+ "hb.totalFloors as totalFloors,hb.address as address,hb.noOfOccupiedBed as noOfOccupiedBed,"
			+ "hb.noOfBedUnderMaintenance as noOfBedUnderMaintenance,hb.remarks as remarks) "
			+ "from HostelBlocks hb")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value ="Select hb.blockShortName From HostelBlocks hb where hb.hostelBlockId=?1 and hb.active=true")
	public String getBlockShortName(Integer hostelBlockId);
	
	@Query(value ="Select GROUP_CONCAT(hb.block_short_name) From hostel_blocks hb where hb.hostel_block_id in (:hostel_block_id)",nativeQuery = true)
	public String getBlockShortNameCommaSeperated(List<Integer> hostel_block_id);
	
	@Query(value = "SELECT * FROM doctor_warden where doctor_warden_type=?1 and active=true",nativeQuery = true)
	public List<Map<String, Object>> getDetailsOfFloorsAndRooms(Integer block_id);
	
	@Modifying
	@Query(value = "update HostelBlocks h set h.totalNoRooms=?2 where h.hostelBlockId=?1")
	public void updateTotalNoOfRooms(Integer hostel_block_id, Integer totalNoOfRooms);
	
	@Query(value ="Select hb.blockName From HostelBlocks hb where hb.hostelBlockId=?1 and hb.active=true")
	public String getBlockName(Integer hostelBlockId);
	
	public List<HostelBlocks> findAllByHostelBlockIdInAndActiveTrue(List<Integer> hostelBlockIds);
	
}
