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
import com.au.model.DoctorWarden;

@Repository
@Transactional
public interface DoctorWardenRepository extends JpaRepository<DoctorWarden, Integer>{

	@Query(value = "select case when (count(dw.user_id) > 0)  then true else false end from DoctorWarden dw where dw.user_id = :user_id")
	public Boolean existsByUserId(Integer user_id);
	
//	@Query(value = "select count(*) from DoctorWarden dw where dw.block_id=?1 and dw.floor_id=?2 and dw.doctorWardenType=?3 and active=true")
//	public Integer getWardenCountForBlockAndFloor(Integer block_id, Integer floor_id, String doctorWardenType);
//	
//	@Query(value = "select count(*) from DoctorWarden dw where dw.block_id=?1 and dw.doctorWardenType=?2 and active=true")
//	public Integer getDoctorCountForBlockAndFloor(Integer block_id, String doctorWardenType);
//	
	@Query(value = "select count(*) from DoctorWarden dw where dw.user_id=?1 and dw.hostel_block_id=?2 and dw.hostel_floor_id=?3 and dw.active=true")
	public Integer countOfWarden(Integer user_id, Integer hostel_block_id,Integer hostel_floor_id);
	
	@Query(value = "select count(*) from DoctorWarden dw where dw.user_id=?1 and dw.hostel_block_id=?2 and dw.active=true")
	public Integer countOfDoctor(Integer user_id,Integer hostel_block_id);

	@Query(value = "select h from DoctorWarden h where h.active=true")
	public List<DoctorWarden> findAll1();

	@Modifying
	@Query(value = "update DoctorWarden h set h.active=false where h.doctorId=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update DoctorWarden h set h.active=true where h.doctorId=?1")
	public void update1(Integer id);
	
	@Query(value = "SELECT * FROM doctor_warden where doctor_warden_type='WARDEN'",nativeQuery = true)
	public List<DoctorWarden> fetchWardens();
	
	@Query(value = "Select new map(dw.doctorId as id,dw.doctorWardenType as doctorWardenType,dw.doctorTypeId as doctorTypeId,"
			+ "dw.address as address,dw.mobile as mobile,dw.email as email,dw.createdDate as createdDate,dw.modifiedDate as modifiedDate,"
			+ "dw.createdBy as createdBy,dw.modifiedBy as modifiedBy,dw.createdUsername as createdUsername,hb.blockName as block_name,"
			+ "hb.blockShortName as block_short_name,hf.floorName as floor_name,ua.username as username,"
			+ "dw.modifiedUsername as modifiedUsername,dw.active as active) From DoctorWarden dw "
			+ "left join UserAuthentication ua on dw.user_id=ua.id "
			+ "left join HostelBlocks hb on dw.hostel_block_id=hb.hostelBlockId "
			+ "left join HostelFloor hf on dw.hostel_floor_id=hf.hostelFloorId "
			+ "Where CONCAT(IfNull(dw.doctorWardenType,''),'',IfNull(dw.mobile,''),'',IfNull(dw.email,''),'',IfNull(hb.blockName,''),"
			+ "'',IfNull(ua.username,''),'',IfNull(dw.createdDate,''),'',IfNull(hf.floorName,''),"
			+ "'',IfNull(dw.createdUsername,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(dw.doctorId as id,dw.doctorWardenType as doctorWardenType,dw.doctorTypeId as doctorTypeId,"
			+ "dw.address as address,dw.mobile as mobile,dw.email as email,dw.createdDate as createdDate,dw.modifiedDate as modifiedDate,"
			+ "dw.createdBy as createdBy,dw.modifiedBy as modifiedBy,dw.createdUsername as createdUsername,hb.blockName as block_name,"
			+ "hb.blockShortName as block_short_name,hf.floorName as floor_name,ua.username as username,"
			+ "dw.modifiedUsername as modifiedUsername,dw.active as active) From DoctorWarden dw "
			+ "left join UserAuthentication ua on dw.user_id=ua.id "
			+ "left join HostelBlocks hb on dw.hostel_block_id=hb.hostelBlockId "
			+ "left join HostelFloor hf on dw.hostel_floor_id=hf.hostelFloorId ")
	public Page<Object> findAll3(Pageable pageable);
	
//	@Query(value = "Select ua from user_details ua left join doctor_warden dw on ua.id=dw.user_id "
//			+ "where dw.user_id is null",nativeQuery=true)
	
	@Query(value = "select ua.id,ua.username as username  from user_details ua "
			+ "left join doctor_warden dw on ua.id = dw.user_id "
			+ "where dw.user_id is null and ua.active=true ",nativeQuery=true)
	public List<Map<String, Object>> fetchUnassignedUsers();
	
	
	@Query(value = "Select hb.hostel_block_id,hb.block_name,hb.block_short_name from hostel_blocks hb "
			+ "where hb.hostel_block_id NOT IN (Select dw.hostel_block_id from doctor_warden dw where dw.doctor_warden_type='Doctor' "
			+ "and dw.active=true) and hb.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchDoctorUnassignedBlocks();
	
	@Query(value = "select distinct hb.hostel_block_id,hb.block_short_name,hb.block_name from hostel_blocks hb "
			+ "left join hostel_floor hf on hb.hostel_block_id=hf.hostels_block_id "
			+ "left join doctor_warden dw on dw.hostel_floor_id=hf.hostel_floor_id and dw.doctor_warden_type='Warden' "
			+ "where dw.hostel_floor_id is null and hf.active=true",nativeQuery=true)
	public List<Map<String, Object>> UnassignedBlocksWithWarden();
	
	@Query(value = "Select hf.hostel_floor_id,hf.floor_name from hostel_floor hf where hf.hostel_floor_id NOT IN "
			+ "(Select dw.hostel_floor_id from doctor_warden dw where dw.doctor_warden_type='Warden' "
			+ "and dw.hostel_block_id=?1 and dw.active=true) and hf.hostels_block_id=?1 and hf.active=true",nativeQuery=true)
	public List<Map<String, Object>> UnassignedFloorsWithWarden(Integer hostels_block_id);
	
	@Query(value = "Select new map(dw.doctorId as id,dw.doctorWardenType as doctorWardenType,dw.doctorTypeId as doctorTypeId,hb.blockName as block_name,"
			+ "hb.blockShortName as block_short_name,hf.floorName as floor_name,ua.username as username,"
			+ "dw.modifiedUsername as modifiedUsername,dw.active as active) From DoctorWarden dw "
			+ "left join UserAuthentication ua on dw.user_id=ua.id "
			+ "left join HostelBlocks hb on dw.hostel_block_id=hb.hostelBlockId "
			+ "left join HostelFloor hf on dw.hostel_floor_id=hf.hostelFloorId where dw.user_id =?1 and dw.active=true")
	public List<HashMap<String, Object>> floorsAssignedToWarden(Integer user_id);
	
	@Query(value = "Select dw.doctor_warden_type From doctor_warden dw where dw.user_id =?1 and dw.active=true Limit 1",nativeQuery=true)
	public String getDoctorWardenType(Integer user_id);
}
