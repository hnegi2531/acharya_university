package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.DesignationDTO;
import com.au.model.Designation;

@Transactional
@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {
	
	@Query(value="select d from Designation d where d.active=true")
	public List<Designation> findAll1();
	
	@Modifying
	@Query(value = "update Designation d set d.active=false where d.designation_id=?1")
	public void update(Integer designation_id);
	
	@Modifying
	@Query(value = "update Designation d set d.active=true where d.designation_id=?1")
	public void update1(Integer designation_id);
	
	@Query(value = "select new map(des.designation_id as id,des.designation_name as designation_name,des.priority as priority,"
			+ "des.designation_short_name as designation_short_name,des.created_by as created_by,des.modified_by as modified_by,"
			+ "jt.job_type as job_type,jt.job_short_name as job_short_name,des.duration As duration,"
			+ "des.working_hours As working_hours,des.job_description As job_description,"
			+ "des.created_date as created_date,des.modified_date as modified_date,des.active as active,des.job_type_id as job_type_id,"
			+ "des.created_username as created_username,des.modified_username as modified_username) "
			+ "from Designation des "
			+ "left join JobType jt on jt.job_type_id=des.job_type_id "
			+ "where CONCAT(IfNull(des.designation_id,''),'',IfNull(des.designation_name,''),'',IfNull(des.priority,''),"
			+ "'',IfNull(des.designation_short_name,''),'',IfNull(des.created_by,''),'',IfNull(des.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(des.designation_id as id,des.designation_name as designation_name,des.priority as priority,"
			+ "des.designation_short_name as designation_short_name,des.created_by as created_by,des.modified_by as modified_by,"
			+ "jt.job_type as job_type,jt.job_short_name as job_short_name,des.duration As duration,"
			+ "des.working_hours As working_hours,des.job_description As job_description,"
			+ "des.created_date as created_date,des.modified_date as modified_date,des.active as active,des.job_type_id as job_type_id,"
			+ "des.created_username as created_username,des.modified_username as modified_username) "
			+ "from Designation des "
			+ "left join JobType jt on jt.job_type_id=des.job_type_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM Designation des where des.designation_name=?1 and des.active=true")
	public Integer countOfDesignationName(String designation_name);
	
	@Query(value = "SELECT count(*) FROM Designation des where des.designation_short_name=?1 and des.active=true")
	public Integer countOfDesignationShortName(String designation_short_name);
	
	@Query(value = "SELECT count(*) FROM Designation des where des.priority=?1 and des.active=true")
	public Integer countOfPriority(Integer priority);
	
	@Query(value = "select des.designation_name from Designation des where des.designation_id=?1 and des.active=true")
	public String getDesignation(Integer designation_id);

	@Query(value="select new  com.au.dto.DesignationDTO( d.designation_id as designationId, d.designation_name as designationName )   from Designation d  where d.active=true")
	public List<DesignationDTO> getDesignations();

}
