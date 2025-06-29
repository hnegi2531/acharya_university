package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ExamDetails;

@Repository
@Transactional
public interface ExamDetailsRepository extends JpaRepository<ExamDetails, Integer> {
	
	
	@Query(value = "SELECT ed from ExamDetails ed where ed.active=true")
	public List<ExamDetails> findAll11();
	
	
	
	@Query(value = "select new map(ed.exam_details_id as id,ed.exam_date as exam_date,ed.exam_center as exam_center,"
			+ "ed.created_username as created_username,ed.modified_username as modified_username,"
			+ "ed.created_date as created_date,ed.modified_date as modified_date,ed.created_by as created_by,"
			+ "ed.internal_master_id as internal_master_id,ed.start_time as start_time,ed.end_time as end_time,ed.address as address,"
			+ "ed.duration as duration,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "ed.windows_start_date as windows_start_date,ed.windows_end_date as windows_end_date,"
			+ "ed.modified_by as modified_by,ed.active as active) from ExamDetails ed "
			+ "left join InternalTypes it on it.internal_master_id = ed.internal_master_id "
		    + "where CONCAT(IfNull(ed.created_username,''),'',IfNull(ed.exam_date,''),'',IfNull(ed.exam_center,''),'',"
			+ "'',IfNull(ed.created_by,''),'',IfNull(ed.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(ed.exam_details_id as id,ed.exam_date as exam_date,ed.exam_center as exam_center,"
			+ "ed.created_username as created_username,ed.modified_username as modified_username,"
			+ "ed.created_date as created_date,ed.modified_date as modified_date,ed.created_by as created_by,"
			+ "ed.internal_master_id as internal_master_id,ed.start_time as start_time,ed.end_time as end_time,ed.address as address,"
			+ "ed.duration as duration,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "ed.windows_start_date as windows_start_date,ed.windows_end_date as windows_end_date,"
			+ "ed.modified_by as modified_by,ed.active as active) from ExamDetails ed "
			+ "left join InternalTypes it on it.internal_master_id = ed.internal_master_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Modifying
	@Query(value = "update ExamDetails ed set ed.active=false where ed.exam_details_id=?1")
	public void updateExamDetail(Integer id);
	
	@Modifying
	@Query(value = "update ExamDetails ed set ed.active=true where ed.exam_details_id=?1")
	public void updateExamDetail1(Integer id);

	@Query(value = "select ed.exam_details_id as exam_details_id,"
			+ "concat(ed.exam_center,'-',ed.exam_date) as exam_center from ExamDetails ed")
	public List<Map<String,Object>> getExamCenterWithDate();
	
	@Query(value = "select new map(ed.exam_details_id as exam_details_id,ed.exam_date as exam_date,ed.exam_center as exam_center,"
			+ "ed.created_username as created_username,ed.modified_username as modified_username,"
			+ "ed.created_date as created_date,ed.modified_date as modified_date,ed.created_by as created_by,"
			+ "ed.internal_master_id as internal_master_id,ed.start_time as start_time,ed.end_time as end_time,ed.address as address,"
			+ "ed.duration as duration,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "ed.windows_start_date as windows_start_date,ed.windows_end_date as windows_end_date,"
			+ "ed.modified_by as modified_by,ed.active as active) from ExamDetails ed "
			+ "left join InternalTypes it on it.internal_master_id = ed.internal_master_id Where ed.active=true")
	public List<Map<String,Object>> getExamDetailsForHallTicketGeneration();
}
