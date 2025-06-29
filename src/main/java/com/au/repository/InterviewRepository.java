package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Interview;


@Transactional
@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {

	@Query(value = "select interview_date from Interview i where i.job_id=?1 and i.active=true")
	public String getInterviewDate(Integer job_id);
	
	@Query(value = "select ictStatus from Interview i where i.job_id=?1 and i.active=true")
	public Boolean getIctStatus(Integer job_id);
	
//	@Query(value = "select position from Interview i where i.job_id=?1")
//	public String getPosition(Integer job_id);

//	@Query(value = "select i from Interview i where i.job_id=?1")
//	public Interview getAllInterviewDetail(Integer job_id);
	
	@Query(value = "select i.interview_id as id,i.subject as subject,i.designation_id as designation_id,i.body as body,"
			+ "d.designation_name as designation_name,d.designation_short_name as designation_short_name,i.ict_status as ict_status,"
			+ "i.job_id as job_id,i.comments as comments,i.interview_date as interview_date,i.frontend_use_datetime as frontend_use_datetime,"
			+ "(Select GROUP_CONCAT(ir.email ORDER BY interviewer_id ASC) From interviewer ir where ir.interview_id=i.interview_id) as employeeEmails,"
			+ "(Select GROUP_CONCAT(ir.interviewer_name ORDER BY interviewer_id ASC) From interviewer ir where ir.interview_id=i.interview_id) as employeeName,"
			+ "(Select GROUP_CONCAT(ir.interviewer_id ORDER BY interviewer_id ASC) From interviewer ir where ir.interview_id=i.interview_id) as interviewer_ids,"
			+ "i.approve as approve,i.schedule as schedule from interview i left join designation d on i.designation_id=d.designation_id "
			+ "where i.job_id=?1 ORDER BY interview_id DESC Limit 1",nativeQuery=true)
	public List<Map<String, Object>> getAllInterviewDetail(Integer job_id);
	
	@Query(value = "select i from Interview i where i.job_id=?1")
	public List<Interview> findByJobId(Integer job_id);
	
	@Query(value = "select count(*) from Interview i where i.job_id=?1 and i.active=true")
	public Integer getCountInterview(Integer job_id);
	
	@Modifying
	@Query(value = "update  interview i set i.active=false where i.job_id=?1 and i.active=true",nativeQuery=true)
	public void deleteExistingData(Integer job_id);
	
	@Query(value = "select i.designation_id from Interview i where i.job_id=?1 and i.active=true")
	public Integer getDesignationId(Integer job_id);

	@Query(value = "select ua.email from UserAuthentication ua where ua.id=(select i.created_by from Interview i where i.job_id=?1 and i.active=true)")
	public String getCreatedByEmailToAddcc(Integer job_id);

	@Query(value = "select i.comments from Interview i where i.job_id=?1 and i.active=true")
	public String getComments(Integer job_id);

	
}
