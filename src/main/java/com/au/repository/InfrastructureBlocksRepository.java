package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.InfrastructureBlocks;

@Transactional
@Repository
public interface InfrastructureBlocksRepository extends JpaRepository<InfrastructureBlocks, Integer> {

	@Query(value = "select count(*) from InfrastructureBlocks b where b.block_name=?1 and active=true")
	public Integer getCountBlockName(String block_name);
	
	@Query(value = "select count(*) from InfrastructureBlocks b where b.block_short_name=?1 and active=true")
	public Integer getCountBlockShortName(String block_short_name);
	
	@Query(value = "select count(*) from InfrastructureBlocks b where b.blockcode=?1 and active=true")
	public Integer getCountBlockCode(String blockcode);
	
	@Query(value = "SELECT count(*) FROM InfrastructureBlocks where institute_id=?1 and block_name=?2  and block_short_name=?3 and active=true")
	public Integer getBlockNameAndShortNameCount(Integer institute_id, String block_name, String block_short_name);
	
	@Query(value = "select b from InfrastructureBlocks b where b.active=true")
	public  List<InfrastructureBlocks> findAll1();
	
	@Query(value ="Select new map(b.block_id as id,b.block_name as block_name,b.block_short_name as block_short_name,ft.facility_type_name as facility_type_name,ft.facility_short_name as facility_short_name,"
			+ "b.school_id as school_id,b.blockcode as blockcode,b.total_no_of_floor as total_no_of_floor,b.total_built_up_area as total_built_up_area,s.school_name as school_name,s.school_name_short as school_name_short,"
			+ "b.survey_number as survey_number,b.document_number as document_number,b.facility_type_id as facility_type_id,b.remarks as remarks,b.basement as basement,b.show_in_event as show_in_event,"
			+ "b.active as active,b.created_date as created_date,b.modified_date as modified_date,b.created_by as created_by,"
			+ "b.modified_by as modified_by,b.created_username as created_username,b.modified_username as modified_username)"
			+ " From InfrastructureBlocks b left join InfrastructureFacilityType ft on b.facility_type_id=ft.facility_type_id left join Schools s on s.school_id=b.school_id "
			+ "Where CONCAT(IfNull(b.block_id,''),'',IfNull(b.block_name,''),'',IfNull(b.block_short_name,''),'',IfNull(ft.facility_type_name,''),'',IfNull(ft.facility_short_name,''),'',IfNull(s.school_name,''),'',IfNull(s.school_name_short,''),"
			+ "'',IfNull(b.school_id,''),'',IfNull(b.blockcode,''),'',IfNull(b.total_no_of_floor,''),'',IfNull(b.total_built_up_area,''),"
			+ "'',IfNull(b.survey_number,''),'',IfNull(b.document_number,''),'',IfNull(b.facility_type_id,''),'',IfNull(b.remarks,''),'',IfNull(b.basement,''),'',IfNull(b.show_in_event,''),"
			+ "'',IfNull(b.active,''),'',IfNull(b.created_date,''),'',IfNull(b.created_by,''),'',IfNull(b.created_username,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(b.block_id as id,b.block_name as block_name,b.block_short_name as block_short_name,ft.facility_type_name as facility_type_name,ft.facility_short_name as facility_short_name,"
			+ "b.school_id as school_id,b.blockcode as blockcode,b.total_no_of_floor as total_no_of_floor,b.total_built_up_area as total_built_up_area,s.school_name as school_name,s.school_name_short as school_name_short,"
			+ "b.survey_number as survey_number,b.document_number as document_number,b.facility_type_id as facility_type_id,b.remarks as remarks,b.basement as basement,b.show_in_event as show_in_event,"
			+ "b.active as active,b.created_date as created_date,b.modified_date as modified_date,b.created_by as created_by,"
			+ "b.modified_by as modified_by,b.created_username as created_username,b.modified_username as modified_username)"
			+ " From InfrastructureBlocks b left join InfrastructureFacilityType ft on b.facility_type_id=ft.facility_type_id left join Schools s on s.school_id=b.school_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update InfrastructureBlocks ib set ib.active=false where ib.block_id=?1")
	public void updateToDeactivate(Integer id);

	@Modifying
	@Query(value = "update InfrastructureBlocks ib set ib.active=true where ib.block_id=?1")
	public void updateToActivate(Integer id);
	
	@Query(value = "Select ib.blockcode from InfrastructureBlocks ib where ib.block_id=?1 and ib.active=true")
	public String getBlockCode(Integer block_id);
	
	
	
	
}
