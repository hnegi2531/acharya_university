package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.HrStatusHistory;

@Repository
@Transactional
public interface HrStatusHistoryRepository extends JpaRepository<HrStatusHistory, Integer> {

	
	@Query(value = "SELECT hrsh from HrStatusHistory hrsh where hrsh.active=true")
	public List<HrStatusHistory> findAll11();	
	
	@Modifying
	@Query(value = "update HrStatusHistory hrsh set hrsh.active=false where hrsh.hr_Status_History_id=?1")
	public void deactivateHrStatusHistory(Integer id);
	
	@Modifying
	@Query(value = "update HrStatusHistory hrsh set hrsh.active=true where hrsh.hr_Status_History_id=?1")
	public void activateHrStatusHistory(Integer id);	
	
	@Query(value = "select new map(hrsh.hr_Status_History_id as id,hrsh.job_id as job_id,hrsh.hr_status as hr_status,hrsh."
			+ "hr_remark as hr_remark,jp.firstname as firstname,jp.mobile as mobile,jp.email as email,jp.job_type_id as job_type_id,"
			+ "jp.hr_status as hr_status_jp,jp.hr_remark as hr_remark_jp,"
			+ "hrsh.created_username as created_username,hrsh.modified_username as modified_username,"
			+ "hrsh.created_date as created_date,hrsh.modified_date as modified_date,hrsh.created_by as created_by,"
			+ "hrsh.modified_by as modified_by,hrsh.active as active) from HrStatusHistory hrsh "
			+ "left join JobProfile jp on jp.job_id = hrsh.job_id "
			+ "where CONCAT(IfNull(hrsh.created_username,''),'',IfNull(hrsh.hr_status,''),'',IfNull(hrsh.hr_remark,''),"
			+ "'',IfNull(hrsh.created_by,''),'',IfNull(hrsh.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(hrsh.hr_Status_History_id as id,hrsh.job_id as job_id,hrsh.hr_status as hr_status,hrsh."
			+ "hr_remark as hr_remark,jp.firstname as firstname,jp.mobile as mobile,jp.email as email,jp.job_type_id as job_type_id,"
			+ "jp.hr_status as hr_status_jp,jp.hr_remark as hr_remark_jp,"
			+ "hrsh.created_username as created_username,hrsh.modified_username as modified_username,"
			+ "hrsh.created_date as created_date,hrsh.modified_date as modified_date,hrsh.created_by as created_by,"
			+ "hrsh.modified_by as modified_by,hrsh.active as active) from HrStatusHistory hrsh "
			+ "left join JobProfile jp on jp.job_id = hrsh.job_id")
	public Page<Object> getAllSortedData(Pageable pageable);

	
	@Query(value = "select new map(hrsh.hr_Status_History_id as hr_Status_History_id,hrsh.job_id as job_id,hrsh.created_by as created_by,"
			+ "hrsh.created_date as created_date,hrsh.hr_status as hr_status_jp,hrsh.hr_remark as hr_remark_jp,"
			+ "hrsh.created_username as created_username,jp.hr_status as hr_status,jp.hr_remark as hr_remark) from HrStatusHistory hrsh "
			+ "left join JobProfile jp on jp.job_id = hrsh.job_id where jp.job_id=?1")
	public List<HashMap<String, Object>> getJobprofileDetails(Integer job_id);	

}
