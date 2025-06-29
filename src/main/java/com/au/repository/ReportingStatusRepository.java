package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.au.model.ReportingStatus;

@Repository
@Transactional
public interface ReportingStatusRepository extends JpaRepository<ReportingStatus, Integer>{

	@Query(value = "select rs from ReportingStatus rs where rs.active=true")
	public List<ReportingStatus> findAll1();
	
	@Modifying
	@Query(value = "update ReportingStatus rs set rs.active=false where rs.reporting_status_id=?1")
	public void updateReportingStatus(Integer id);

	@Modifying
	@Query(value = "update ReportingStatus rs set rs.active=true where rs.reporting_status_id=?1")
	public void updateReportingStatus1(Integer id);
	
	@Query(value = "Select new map(rs.reporting_status_id as id,rs.reporting_status as reporting_status,rs.reporting_short_name as reporting_short_name,"
			+ "rs.created_by as created_by,rs.modified_by as modified_by,rs.created_date as created_date,rs.modified_date as modified_date,"
			+ "rs.active as active,rs.created_username as created_username,rs.modified_username as modified_username,rs.student_count as student_count) From ReportingStatus rs "
			+ "Where CONCAT(IfNull(rs.reporting_status_id,''),'',IfNull(rs.reporting_status,''),'',IfNull(rs.reporting_short_name,''),'',IfNull(rs.created_date,''),'',IfNull(rs.created_by,''),'',IfNull(rs.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(rs.reporting_status_id as id,rs.reporting_status as reporting_status,rs.reporting_short_name as reporting_short_name,"
			+ "rs.created_by as created_by,rs.modified_by as modified_by,rs.created_date as created_date,rs.modified_date as modified_date,"
			+ "rs.active as active,rs.created_username as created_username,rs.modified_username as modified_username,rs.student_count as student_count) From ReportingStatus rs")
	public Page<Object> findAll3(Pageable pageable);
}
