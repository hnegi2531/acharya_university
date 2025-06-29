package com.au.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Interview;
import com.au.model.Interviewer;

@Transactional
@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer, Integer> {

	@Query(value = "select new map(i.emp_id as emp_id,i.interviewer_name as interviewer_name,ua.username as username,i.job_id as job_id,i.email as email,"
			+ "jp.mail_sent_to_candidate as mail_sent_to_candidate,i.interview_id as interview_id,i.created_date as created_date,"
			+ "i.modified_date as modified_date,i.hr_name as hr_name,it.frontend_use_datetime as frontend_use_datetime,"
			+ "jp.resume_headline as resume_headline,i.hr_remarks as hr_remarks,i.interviewer_id as interviewer_id,i.hr_date as hr_date,"
			+ "jp.mail_sent_to_candidate as mail_sent_to_candidate,i.interviewer_comments as interviewer_comments,jp.mailSentToCandidateDate as mailSentToCandidateDate,"
			+ "it.designation_id as designation_id,it.body as body,it.comments as comments,jp.mail_sent_status as mail_sent_status,"
			+ "it.interview_date as interview_date,it.approve as approve,it.created_by as created_by,it.active as active,jp.mailSentToInterviewersDate as mailSentToInterviewersDate,"
			+ "jp.comment_status as comment_status,d.designation_name as designation_name,d.designation_short_name as designation_short_name,"
			+ "it.created_username as created_username,jp.firstname as firstname,jp.email as candidate_email,it.schedule as schedule)"
			+ "from Interviewer i "
			+ "join JobProfile jp on i.job_id=jp.job_id "
			+ "join Interview it on i.interview_id=it.interview_id "
			+ "join UserAuthentication ua on ua.email=i.email "
			+ "left join Designation d on it.designation_id=d.designation_id where i.job_id=?1 and i.active=true")
	public List<HashMap<String, Object>> getAllDeatils(Integer job_id);
	
	@Query(value = "select new map(hc.interviewer_comments as interviewer_comments,hc.emp_id as emp_id,hc.interviewer_id as interviewer_id,"
			+ "jp.job_id as job_id,jp.firstname as firstname,edd.employee_name as employee_name,hc.email as email,"
			+ "hc.interview_id as interview_id,jp.reference_no as reference_no,hc.hr_remarks as hr_remarks,"
			+ "jp.resume_headline as resume_headline,jp.email as email,hc.interviewer_name as interviewer_name,hc.email as employeeEmail,"
			+ "jp.key_skills as key_skills,hc.created_date as created_date,g.graduation_name_short as graduation_name_short,"
			+ "ed.graduation as graduation) from Interviewer hc join JobProfile jp on jp.job_id=hc.job_id "
			+ "join EducationDetails ed on hc.job_id=ed.job_id "
			+ "join Graduation g on ed.graduation_id=g.graduation_id join EmployeeDetails edd on hc.emp_id=edd.emp_id "
			+ "where hc.emp_id=?1")
	public List<HashMap<String, Object>> findAll1(Integer emp_id);

	@Query(value = "select new map(i.email as email) from Interviewer i where i.interview_id=?1")
	public List<HashMap<String, Object>> getinterviewerEmail(Integer interview_id);
	
	@Query(value = "Select i From Interviewer i where i.interviewer_id IN :id_list")
	public List<Interviewer> findByIds(List<Integer> id_list);
	
	@Query(value = "select i from Interviewer i where i.job_id=?1")
	public List<Interviewer> findByJobId(Integer job_id);
	
	@Query(value = "select count(*) from Interviewer i where i.job_id=?1")
	public Integer getCountInterviewer(Integer job_id);
	
	@Modifying
	@Query(value = "update interviewer i set i.active=false where i.job_id=?1",nativeQuery=true)
	public void deleteExistingData(Integer job_id);
	
	@Query(value = "select i.job_id from Interviewer i where i.email=?1")
	public List<Integer> getAllJobIdFromInterviewer(String email);
	
	
	@Modifying
	@Query(value = "update Interviewer i set i.interviewer_comments=?3 where i.email=?1 and i.job_id=?2")
	public void setInterviewerComments(String email,Integer job_id,String interviewer_comments);



}
