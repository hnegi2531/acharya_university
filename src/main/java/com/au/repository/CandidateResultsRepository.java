package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CandidateResults;

@Transactional
@Repository
public interface CandidateResultsRepository extends JpaRepository<CandidateResults, Integer> {
	
	@Query(value = "select new map(cr.candidate_results_id as id,cr.applicant_name as applicant_name,cr.application_no as application_no,"
			+ "cr.created_by as created_by,cr.created_date as created_date,cr.percentage as percentage,cr.result as result,"
			+ "ed.exam_date as exam_date,ed.exam_center as exam_center,cr.type as type, cr.grade as grade,"
			+ "ed.internal_master_id as internal_master_id,ed.start_time as start_time,ed.end_time as end_time,ed.address as address,"
			+ "ed.duration as duration,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "ed.windows_start_date as windows_start_date,ed.windows_end_date as windows_end_date,"
			+ "ed.modified_by as modified_by,ed.active as active) "
			+ "from CandidateResults cr "
			+ "left join ExamDetails ed on cr.exam_details_id=ed.exam_details_id "
			+ "left join InternalTypes it on it.internal_master_id = ed.internal_master_id "
		    + "where CONCAT(IfNull(ed.created_username,''),'',IfNull(ed.exam_date,''),'',IfNull(ed.exam_center,''),'',"
			+ "'',IfNull(ed.created_by,''),'',IfNull(ed.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(cr.candidate_results_id as id,cr.applicant_name as applicant_name,cr.application_no as application_no,"
			+ "cr.created_by as created_by,cr.created_date as created_date,cr.percentage as percentage,cr.result as result,"
			+ "ed.exam_date as exam_date,ed.exam_center as exam_center,cr.type as type, cr.grade as grade,"
			+ "ed.internal_master_id as internal_master_id,ed.start_time as start_time,ed.end_time as end_time,ed.address as address,"
			+ "ed.duration as duration,it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "ed.windows_start_date as windows_start_date,ed.windows_end_date as windows_end_date,"
			+ "ed.modified_by as modified_by,ed.active as active) "
			+ "from CandidateResults cr "
			+ "left join ExamDetails ed on cr.exam_details_id=ed.exam_details_id "
			+ "left join InternalTypes it on it.internal_master_id = ed.internal_master_id")
	public Page<Object> getAllSortedData(Pageable pageable);

	
	@Query(value = "SELECT count(result) FROM candidate_results where exam_details_id=?1 and active=true and result like '%Pass' ",nativeQuery = true)
	public Integer getCountOfPassedResult(Integer id); 
	
	@Query(value = "SELECT count(result) FROM candidate_results where exam_details_id=?1 and active=true and result like '%Fail' ",nativeQuery = true)
	public Integer getCountOfFailedResult(Integer id); 
	
	@Query(value = "SELECT count(result) FROM candidate_results where exam_details_id=?1 and active=true and result like '%Absent' ",nativeQuery = true)
	public Integer getCountOfAbsentResult(Integer id);

	@Query(value = "SELECT ed.exam_details_id as exam_details_id FROM candidate_results cr "
			+ "left join exam_details ed on cr.exam_details_id=ed.exam_details_id where cr.active=true ",nativeQuery = true)
	public List<Integer> getExamDate();

	@Query(value = "Select new map(cr.candidate_results_id as id,cr.applicant_name as applicant_name,cr.application_no as application_no,"
			+ "cw.mobile_number as mobile_number,cw.candidate_name as candidate_name,"
			+ "cw.candidate_id as candidate_id,cr.exam_details_id as exam_details_id,"
			+ "ed.exam_date as exam_date,cr.type as type, cr.grade as grade,cr.result as result) FROM CandidateResults cr "
			+ "left join ExamDetails ed on cr.exam_details_id=ed.exam_details_id " 
			+ "left join Candidate_Walkin cw on cr.application_no=cw.application_no_npf ")
	public List<HashMap<String, Object>> getAllDataOfCandidateResult();

}
