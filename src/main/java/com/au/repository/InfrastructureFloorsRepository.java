package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.InfrastructureFloors;

@Transactional
@Repository
public interface InfrastructureFloorsRepository extends JpaRepository<InfrastructureFloors, Integer> {
	
	@Query(value = "select f from InfrastructureFloors f where f.block_id=?1")
	public List<InfrastructureFloors> getFloorsByBlockId(Integer block_id);
	
	@Query(value = "select f from InfrastructureFloors f where f.active=true")
	public  List<InfrastructureFloors> findAll1();
	
	@Modifying
	@Query(value = "update InfrastructureFloors f set f.active=false where f.floor_id=?1")
	public void updateToDeactivate(Integer id);

	@Modifying
	@Query(value = "update InfrastructureFloors f set f.active=true where f.floor_id=?1")
	public void updateToActivate(Integer id);
	
	@Query(value ="Select new map(f.floor_id as id,f.floor_name as floor_name,f.floor_no as floor_no,s.school_name as school_name,s.school_name_short as school_name_short,"
			+ "f.block_id as block_id,f.floorcode as floorcode,ib.block_name as block_name,ib.block_short_name as block_short_name,ib.blockcode as blockcode,"
			+ "f.active as active,f.created_date as created_date,f.modified_date as modified_date,f.created_by as created_by,"
			+ "f.modified_by as modified_by,f.created_username as created_username,f.modified_username as modified_username)"
			+ " From InfrastructureFloors f left join InfrastructureBlocks ib on ib.block_id=f.block_id left join Schools s on s.school_id=f.school_id "
			+ "Where CONCAT(IfNull(f.floor_id,''),'',IfNull(f.floor_name,''),'',IfNull(f.floor_no,''),"
			+ "'',IfNull(f.block_id,''),'',IfNull(f.floorcode,''),'',IfNull(s.school_name,''),'',IfNull(ib.block_name,''),'',IfNull(ib.blockcode,''),"
			+ "'',IfNull(f.active,''),'',IfNull(f.created_date,''),'',IfNull(f.created_by,''),'',IfNull(f.created_username,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(f.floor_id as id,f.floor_name as floor_name,f.floor_no as floor_no,s.school_name as school_name,s.school_name_short as school_name_short,"
			+ "f.block_id as block_id,f.floorcode as floorcode,ib.block_name as block_name,ib.block_short_name as block_short_name,ib.blockcode as blockcode,"
			+ "f.active as active,f.created_date as created_date,f.modified_date as modified_date,f.created_by as created_by,"
			+ "f.modified_by as modified_by,f.created_username as created_username,f.modified_username as modified_username)"
			+ " From InfrastructureFloors f left join InfrastructureBlocks ib on ib.block_id=f.block_id left join Schools s on s.school_id=f.school_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "Select f.floor_name from InfrastructureFloors f where f.floor_id=?1 and f.active=true")
	public String getFloorName(Integer floor_id);
	
	@Query(value = "SELECT count(*) FROM infrastructure_floors where block_id=?1", nativeQuery = true)
	public Integer checkForExistingData(Integer block_id);
	
	@Modifying
	@Query(value = "delete from infrastructure_floors f where f.block_id=?1",nativeQuery=true)
	public void deleteExistingData(Integer block_id);
	
	

}
