package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.JobType;

@Transactional
@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Integer>{

	@Query(value = "select j from JobType j where j.active=true")
	public  List<JobType> findAll1();
	
	@Modifying
	@Query(value = "update JobType jt set jt.active=false where jt.job_type_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update JobType jt set jt.active=true where jt.job_type_id=?1")
	public void update1(Integer id);
	
	@Query(value ="Select new map(j.job_type_id as id,j.job_type as job_type,j.job_short_name as job_short_name,"
			+ "j.created_date as created_date,j.modified_date as modified_date,j.created_by as created_by,"
			+ "j.modified_by as modified_by,j.active as active,j.created_username as created_username,j.modified_username as modified_username) From JobType j "
			+ "Where CONCAT(IfNull(j.job_type_id,''),'',IfNull(j.job_type,''),'',IfNull(j.job_short_name,''),'',IfNull(j.created_date,''),'',IfNull(j.created_by,''),'',IfNull(j.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(j.job_type_id as id,j.job_type as job_type,j.job_short_name as job_short_name,"
			+ "j.created_date as created_date,j.modified_date as modified_date,j.created_by as created_by,"
			+ "j.modified_by as modified_by,j.active as active,j.created_username as created_username,j.modified_username as modified_username) From JobType j")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "select count(*) from JobType jt where jt.job_type=?1 and active=true")
	public Integer getCountJobType(String jobtype);

	@Query(value = "select count(*) from JobType jt where jt.job_short_name=?1 and active=true")
	public Integer getCountJobShortName(String job_short_name);
	
	@Query(value = "select case when (count(jt.job_type) > 0)  then true else false end from JobType jt where jt.job_type = :job_type")
	public Boolean validationForJobType(String job_type);
	
	@Query(value = "select case when (count(jt.job_short_name) > 0)  then true else false end from JobType jt where jt.job_short_name = :job_short_name")
	public Boolean validationForJobShortName(String job_short_name);
	
	@Query(value = "select j.job_short_name from JobType j where j.job_type_id=?1")
	public String getJobType(Integer job_type_id);
	
	@Query(value = "select j.job_type from JobType j where j.job_type_id=?1")
	public String getJobTypeName(Integer job_type_id);
}
