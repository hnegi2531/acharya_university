
package com.au.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Address;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.JobProfile;

@Transactional
@Repository
public interface JobProfileRepository extends JpaRepository<JobProfile, Integer> {

	@Modifying
	@Query(value = "update JobProfile jp set jp.active=0 where jp.job_id=?1")
	public void update(Integer id);

	@Query(value = "select jp from JobProfile jp where jp.job_id=?1")
	public JobProfile findReferenceNo(Integer job);

//	@Query(value = "select new map (jp.job_id as id,jp.first_name as first_name,jp.last_name as last_name,"
//			+ "jp.key_skills as key_skills,jp.email as email,jp.resume_headline as resume_headline,"
//			+ "jp.reference_no as reference_no,g.graduation_name_short as graduation_name_short,"
//			+ "ed.graduation_name as graduation_name,i.approve as approve,i.subject as subject,i.body as body,"
//			+ "i.comments as comments,i.interview_date as interview_date,i.interview_id as interview_id,i.schedule as schedule,"
//			+ "o.offerstatus as offerstatus,o.ctc_status as ctc_status,o.offer_id as offer_id,o.mail as mail,"
//			+ "o.employee_type as employee_type,edd.employee_status as employee_status) "
//			+ "from JobProfile jp left join EducationDetails ed on jp.job_id=ed.job_id "
//			+ "left join Graduation g on ed.graduation_id=g.graduation_id left join Interview i on jp.job_id=i.job_id "
//			+ "left join Offer o on jp.job_id=o.job_id left join EmployeeDetails edd on jp.job_id=edd.job_id")
//	public List<HashMap<String, Object>> findAll1();
	
	
//	@Query(value = "select new map (jp.job_id as id,jp.first_name as first_name,jp.last_name as last_name,"
//			+ "jp.key_skills as key_skills,jp.email as email,jp.resume_headline as resume_headline,"
//			+ "jp.reference_no as reference_no,"
//			+ "CONCAT(Select ed.graduation_name,',' From EducationDetails ed where jp.job_id=ed.job_id ) as graduation_name,i.approve as approve,i.subject as subject,i.body as body,"
//			+ "i.comments as comments,i.interview_date as interview_date,i.interview_id as interview_id,i.schedule as schedule,"
//			+ "o.offerstatus as offerstatus,o.ctc_status as ctc_status,o.offer_id as offer_id,o.mail as mail,"
//			+ "o.employee_type as employee_type,edd.employee_status as employee_status) "
//			+ "from JobProfile jp "
//			+ "left join Interview i on jp.job_id=i.job_id "
//			+ "left join Offer o on jp.job_id=o.job_id left join EmployeeDetails edd on jp.job_id=edd.job_id")
//	public List<HashMap<String, Object>> findAll1();
	
	
	@Query(value = "SELECT jp.job_id AS id, jp.firstname AS firstname, jp.mail_sent_status AS mail_sent_status, jp.comment_status AS comment_status, "
	        + "jp.mail_sent_to_candidate AS mail_sent_to_candidate, jp.key_skills AS key_skills, jp.email AS email, jp.created_date AS created_date, "
	        + "jp.resume_headline AS resume_headline, "
	        + "(SELECT GROUP_CONCAT(gt.graduation_name_short) FROM graduation_type gt WHERE gt.graduation_id IN (SELECT ed.graduation_id FROM education_details ed WHERE jp.job_id=ed.job_id)) AS graduation_name_short, "
	        + "jp.reference_no AS reference_no, "
	        + "(SELECT GROUP_CONCAT(ed.graduation) FROM education_details ed WHERE jp.job_id=ed.job_id) AS graduation, "
	        + "i.approve AS approve, i.subject AS subject, i.body AS body, i.comments AS comments, i.interview_date AS interview_date, "
	        + "i.interview_id AS interview_id, i.schedule AS schedule, o.consolidated_amount AS consolidated_amount, "
	        + "o.offerstatus AS offerstatus, o.ctc_status AS ctc_status, o.offer_id AS offer_id, o.mail AS mail, "
	        + "i.frontend_use_datetime AS frontend_use_datetime, o.employee_type AS employee_type, edd.employee_status AS employee_status, "
	        + "jp.hr_status AS hr_status, org.org_name AS org_name, org.org_type AS org_type, jt.job_type AS job_type, jt.job_short_name AS job_short_name, "
	        + "jp.hr_remark AS hr_remark, jp.mail_sent_to_interviewers_date AS mail_sent_to_interviewers_date, "
	        + "jp.mail_sent_to_candidate_date AS mail_sent_to_candidate_date, de.designation_name AS designation_name, de.designation_short_name AS designation_short_name "
	        + "FROM job_profile jp "
	        + "LEFT JOIN interview i ON jp.job_id=i.job_id AND i.active=true "
	        + "LEFT JOIN job_type jt ON jt.job_type_id=jp.job_type_id "
	        + "LEFT JOIN designation de ON de.designation_id=jp.designation_id "
	        + "LEFT JOIN offer o ON jp.job_id=o.job_id "
	        + "LEFT JOIN employee_details edd ON jp.job_id=edd.job_id "
	        + "LEFT JOIN schools sc ON sc.school_id=o.school_id "
	        + "LEFT JOIN organization org ON org.org_id=sc.org_id "
	        + "WHERE CONCAT(IFNULL(jp.job_id, ''), IFNULL(jp.firstname, ''), IFNULL(jp.key_skills, ''), IFNULL(jp.email, ''), "
	        + "IFNULL(jp.created_date, ''), IFNULL(i.interview_date, ''), IFNULL(i.schedule, ''), IFNULL(o.offer_id, ''), "
	        + "IFNULL(o.mail, ''), IFNULL(o.employee_type, ''), IFNULL(jp.reference_no, '')) LIKE %:keyword% "
	        + "AND jp.emp_code_status IS NULL "
	        + "AND (:start IS NULL OR DATE(jp.created_date) >= :start) "
	        + "AND (:end IS NULL OR DATE(jp.created_date) <= :end) "
	        + "ORDER BY jp.created_date DESC", nativeQuery=true)
	public List<Map<String, Object>> findAll1(Pageable pageable, Object keyword, LocalDate start, LocalDate end);


	@Query(value = "SELECT jp.created_date AS created_date, i.comments AS comments, jp.mail_sent_status AS mail_sent_status, jp.comment_status AS comment_status, "
	        + "jp.mail_sent_to_candidate AS mail_sent_to_candidate, "
	        + "(SELECT GROUP_CONCAT(ed.graduation) FROM education_details ed WHERE jp.job_id=ed.job_id) AS graduation, "
	        + "i.approve AS approve, i.subject AS subject, i.body AS body, "
	        + "(SELECT GROUP_CONCAT(g.graduation_name_short) FROM graduation_type g WHERE g.graduation_id IN (SELECT ed.graduation_id FROM education_details ed WHERE jp.job_id=ed.job_id)) AS graduation_short_name, "
	        + "jp.reference_no AS reference_no, jp.job_id AS id, jp.firstname AS firstname, jp.key_skills AS key_skills, jp.email AS email, "
	        + "jp.resume_headline AS resume_headline, i.interview_date AS interview_date, i.interview_id AS interview_id, "
	        + "i.schedule AS schedule, o.consolidated_amount AS consolidated_amount, o.offerstatus AS offerstatus, "
	        + "o.ctc_status AS ctc_status, o.offer_id AS offer_id, o.mail AS mail, "
	        + "i.frontend_use_datetime AS frontend_use_datetime, o.employee_type AS employee_type, "
	        + "edd.employee_status AS employee_status, jp.hr_status AS hr_status, org.org_name AS org_name, "
	        + "org.org_type AS org_type, jt.job_type AS job_type, jt.job_short_name AS job_short_name, "
	        + "jp.hr_remark AS hr_remark, jp.mail_sent_to_interviewers_date AS mail_sent_to_interviewers_date, "
	        + "jp.mail_sent_to_candidate_date AS mail_sent_to_candidate_date, de.designation_name AS designation_name, "
	        + "de.designation_short_name AS designation_short_name "
	        + "FROM job_profile jp "
	        + "LEFT JOIN interview i ON jp.job_id=i.job_id AND i.active=true "
	        + "LEFT JOIN job_type jt ON jt.job_type_id=jp.job_type_id "
	        + "LEFT JOIN designation de ON de.designation_id=jp.designation_id "
	        + "LEFT JOIN offer o ON jp.job_id=o.job_id "
	        + "LEFT JOIN employee_details edd ON jp.job_id=edd.job_id "
	        + "LEFT JOIN schools sc ON sc.school_id=o.school_id "
	        + "LEFT JOIN organization org ON org.org_id=sc.org_id "
	        + "WHERE jp.emp_code_status IS NULL "
	        + "AND (:start IS NULL OR DATE(jp.created_date) >= :start) "
	        + "AND (:end IS NULL OR DATE(jp.created_date) <= :end) "
	        + "ORDER BY jp.created_date DESC", nativeQuery=true)
	public List<Map<String, Object>> findAll2(Pageable pageable, LocalDate start, LocalDate end);



	
	
	
//	@Query(value = "select new map(jp.job_id as id,jp.first_name as first_name,jp.last_name as last_name,"
//			+ "jp.gender as gender,jp.dateofbirth as dateofbirth,jp.mobile as mobile,jp.email as email,"
//			+ "jp.martial_status as martial_status,jp.door_no as door_no,jp.street as street,jp.locality as locality,"
//			+ "jp.pincode as pincode,jp.key_skills as key_skills,jp.link as link,jp.linkedin_id as linkedin_id,"
//			+ "jp.resume_headline as resume_headline,jp.current_location as current_location,"
//			+ "jp.reference_no as reference_no,jp.created_date as created_date,jp.modified_date as modified_date,"
//			+ "jp.active as active,jp.country_id as country_id,jp.state_id as state_id,jp.city_id as city_id,"
//			+ "c.name as country_name,s.name as state_name,ct.name as city_name) "
//			+ "from JobProfile jp join Country c on jp.country_id=c.id "
//			+ "join State s on jp.state_id=s.id join City ct on jp.city_id=ct.id ")
//	public List<HashMap<String, Object>> findAll1();

	@Query(value = "select new map(jp.job_id as job_id,jp.firstname as firstname,"
			+ "jp.gender as gender,jp.dateofbirth as dateofbirth,jp.mobile as mobile,jp.email as email,"
			+ "jp.martial_status as martial_status,jp.street as street,jp.locality as locality,"
			+ "jp.pincode as pincode,jp.key_skills as key_skills,jp.link as link,jp.linkedin_id as linkedin_id,"
			+ "jp.resume_headline as resume_headline,jp.current_location as current_location,"
			+ "jp.reference_no as reference_no,jp.created_date as created_date,jp.modified_date as modified_date,"
			+ "jp.active as active,jp.country_id as country_id,jp.state_id as state_id,jp.city_id as city_id,"
			+ "jp.mailSentToInterviewersDate as mailSentToInterviewersDate,jp.mailSentToCandidateDate as mailSentToCandidateDate,"
			+ "c.name as country_name,s.name as state_name,ct.name as city_name) "
			+ "from JobProfile jp join Country c on jp.country_id=c.id "
			+ "join State s on jp.state_id=s.id join City ct on jp.city_id=ct.id where job_id=?1")
	public List<HashMap<String, Object>> getAllJobDetails(Integer id);

	@Query(value = "select firstname from JobProfile jp where jp.job_id=?1")
	public String getFirstname(Integer job_id);

//	@Query(value = "select last_name from JobProfile jp where jp.job_id=?1")
//	public String getLastname(Integer job_id);

	@Query(value = "select jp from JobProfile jp where jp.job_id=?1")
	public JobProfile getNameAndEmail(Integer job_id);
	
	@Query(value = "select new map(jp.job_id as job_id,jp.firstname as firstname,jp.mail_sent_status as mail_sent_status,jp.comment_status as comment_status,"
			+ "jp.gender as gender,jp.dateofbirth as dateofbirth,jp.mobile as mobile,jp.email as email,jp.mail_sent_to_candidate as mail_sent_to_candidate,"
			+ "jp.martial_status as martial_status,jp.street as street,jp.locality as locality,jp.emp_code_status as emp_code_status,jp.hr_status as hr_status,"
			+ "jp.pincode as pincode,jp.key_skills as key_skills,jp.link as link,jp.linkedin_id as linkedin_id,jp.hr_status as hr_status,jp.hr_remark as hr_remark,"
			+ "jp.resume_headline as resume_headline,jp.current_location as current_location,jp.hr_feedback_attachment as hr_feedback_attachment,"
			+ "jp.reference_no as reference_no,jp.created_date as created_date,jp.modified_date as modified_date,jp.marks_scored as marks_scored,"
			+ "jp.active as active,jp.country_id as country_id,jp.state_id as state_id,jp.city_id as city_id,"
			+ "jp.mailSentToInterviewersDate as mailSentToInterviewersDate,jp.mailSentToCandidateDate as mailSentToCandidateDate,"
			+ "c.name as country_name,s.name as state_name,ct.name as city_name) "
			+ "from JobProfile jp join Country c on jp.country_id=c.id "
			+ "join State s on jp.state_id=s.id join City ct on jp.city_id=ct.id where job_id=?1")
	public HashMap<String,Object> getNameAndEmailByJobId(Integer job_id);

	@Query(value = "select email from JobProfile jp where jp.job_id=?1")
	public String getEmail(Integer job_id);
	
	@Query(value = " select reference_no from JobProfile jp where jp.job_id=?1 and jp.active=1")
	public String fetchReferenceNo(Integer job_id);

	@Query(value = "select resume_headline from JobProfile jp where jp.job_id=?1")
	public String getjobType(Integer job_id);

	@Query(value = "select count(*) from JobProfile jp where jp.firstname=?1 and jp.dateofbirth=?2 and jp.active=?1")
	public Integer getCountNameAndDob(String firstname,Date dateofbirth);
	
	@Query(value = "select count(*) from JobProfile jp where jp.email=?1 and jp.active=?1")
	public Integer getCountEmail(String email);
	
	
//	@Query(value = "select max(reference_no) from JobProfile jp")
//	public String fetchgetReference_no();
	
//	@Query(value = "SELECT reference_no From (select * from job_profile ORDER BY job_id DESC LIMIT 2) ORDER BY job_id LIMIT 1",nativeQuery=true)
//	public String fetchgetReference_no();
	
//	@Query(value = "select reference_no from job_profile order by job_id DESC LIMIT 0,1",nativeQuery=true)
//	public String fetchgetReference_no();
	
	@Query(value = "select max(cast(left(reference_no, POSITION('/' IN reference_no) - 1) as UNSIGNED)) AS REFFF from job_profile "
			+ "where reference_no is not null",nativeQuery=true)
	public String fetchgetReference_no();
	
	@Query(value = "SELECT job_id From job_profile ORDER BY  job_id desc LIMIT 1",nativeQuery=true)
	public Integer getLatestJobId();
	
	@Query(value = "select case when (count(jp.email) > 0)  then true else false end from JobProfile jp where jp.email = :email")
	public Boolean checkEmailIsPresent(String email);
	
	@Query(value = "select gender from JobProfile where job_id=?1")
	public Character getGender(Integer job_id);

	@Query(value = "select new map(jp.job_id as job_id,jp.firstname as firstname,"
			+ "jp.gender as gender,jp.dateofbirth as dateofbirth,jp.mobile as mobile,jp.email as email,"
			+ "jp.martial_status as martial_status,jp.street as street,jp.locality as locality,"
			+ "jp.pincode as pincode,jp.key_skills as key_skills,jp.link as link,jp.linkedin_id as linkedin_id,"
			+ "jp.resume_headline as resume_headline,jp.current_location as current_location,"
			+ "jp.reference_no as reference_no,jp.created_date as created_date,jp.modified_date as modified_date,"
			+ "jp.active as active,jp.country_id as country_id,jp.state_id as state_id,jp.city_id as city_id,"
			+ "jp.mailSentToInterviewersDate as mailSentToInterviewersDate,jp.mailSentToCandidateDate as mailSentToCandidateDate,"
			+ "c.name as country_name,s.name as state_name,ct.name as city_name) "
			+ "from JobProfile jp left join Country c on jp.country_id=c.id "
			+ "left join State s on jp.state_id=s.id left join City ct on jp.city_id=ct.id where jp.job_id=?1")
	public HashMap<String, Object> getJobProfile(Integer job_id);
	
	
	@Query(value = "select jp.job_id as job_id,jp.firstname as firstname,jp.email as email,jp.key_skills as key_skills,"
			+ "jp.resume_headline as resume_headline,jp.active as active,"
			+ "(Select GROUP_CONCAT(ed.graduation) From education_details ed where jp.job_id=ed.job_id) as graduation "
			+ "from job_profile jp where jp.job_id IN (?1) and jp.active = 1",nativeQuery=true)
	public List<Map<String, Object>> getAllDetailFromJobProfile(List<Integer> job_id);

	@Modifying
	@Query(value = "update JobProfile jp set jp.mail_sent_status=1, jp.mailSentToInterviewersDate=?2 where jp.job_id=?1")
	public void updateMailSentStatus(Integer job_id, String date);
	
	@Modifying
	@Query(value = "update JobProfile jp set jp.comment_status=1 where jp.job_id=?1")
	public void UpdateCommentStatus(Integer job_id);
	
	@Modifying
	@Query(value = "update JobProfile jp set jp.mail_sent_status=null where jp.job_id=?1")
	public void updateMailSentStatusToNull(Integer job_id);
	
	@Modifying
	@Query(value = "update JobProfile jp set jp.mail_sent_to_candidate=1, jp.mailSentToCandidateDate=?2 where jp.job_id=?1")
	public void updateMailSentToCandidate(Integer job_id, String date);
	
	@Query(value = "select jp.current_location from JobProfile jp where jp.job_id=?1")
	public String getAddress(Integer job_id);
	
	@Query(value = "select concat(jp.street,' ',jp.locality,' ',ct.name,' - ',jp.pincode) from JobProfile jp left join City ct on jp.city_id=ct.id where jp.job_id=?1")
	public String getConcatenatedAddress(Integer job_id);
	
	@Query(value = "select i.comments as comments,jp.reference_no as reference_no,intr.interviewer_comments as interviewer_comments,"
			+ "jp.job_id as id,jp.firstname as firstname,jp.key_skills as key_skills,jp.resume_headline as resume_headline,"
			+ "i.interview_date as interview_date,i.frontend_use_datetime as frontend_use_datetime,"
			+ "substring(i.interview_date,1,10) as only_date "
			+ "from job_profile jp "
			+ "left join interview i on jp.job_id=i.job_id "
			+ "left join employee_details edd on jp.job_id=edd.job_id "
			+ "Left join interviewer intr on jp.job_id=intr.job_id "
			+ "join user_details ua on ua.email=intr.email "
			+ "where intr.email=?1 and intr.active=true and i.active=true",nativeQuery=true)
	public List<Map<String, Object>> jobProfileDetailsOnUserId(String email);
	
//	@Query(value = "select jp.created_date as created_date,i.comments as comments,jp.mail_sent_status as mail_sent_status,jp.comment_status as comment_status,jp.mail_sent_to_candidate as mail_sent_to_candidate,"
//			+ "(Select GROUP_CONCAT(ed.graduation) From education_details ed where jp.job_id=ed.job_id) as graduation,i.approve as approve,i.subject as subject,i.body as body,"
//			+ "(Select GROUP_CONCAT(g.graduation_name_short) From graduation_type g where g.graduation_id in (Select ed.graduation_id from education_details ed Where jp.job_id=ed.job_id)) as graduation_short_name,jp.reference_no as reference_no,"
//			+ "jp.job_id as id,jp.firstname as firstname,jp.key_skills as key_skills,jp.email as email,jp.resume_headline as resume_headline,intr.interviewer_comments as interviewer_comments,"
//			+ "i.interview_date as interview_date,i.interview_id as interview_id,i.schedule as schedule,ua.username as username,"
//			+ "jp.mail_sent_to_interviewers_date as mail_sent_to_interviewers_date,jp.mail_sent_to_candidate_date as mail_sent_to_candidate_date,"
//			+ "o.offerstatus as offerstatus,o.ctc_status as ctc_status,o.offer_id as offer_id,o.mail as mail,i.frontend_use_datetime as frontend_use_datetime,"
//			+ "o.employee_type as employee_type,edd.employee_status as employee_status,intr.email as interviewer_email,substring(i.interview_date,1,10) as only_date "
//			+ "from job_profile jp "
//			+ "left join interview i on jp.job_id=i.job_id "
//			+ "left join offer o on jp.job_id=o.job_id "
//			+ "left join employee_details edd on jp.job_id=edd.job_id "
//			+ "Left join interviewer intr on jp.job_id=intr.job_id "
//			+ "join user_details ua on ua.email=intr.email "
//			+ "where intr.email=?1",nativeQuery=true)
	
	@Modifying
	@Query(value = "update JobProfile jp set jp.emp_code_status=1 where jp.job_id=?1")
	public void updateEmpCodeStatus(Integer job_id);
	
	@Query(value = "select reference_no from JobProfile where job_id=?1")
	public String getReferenceNumber(Integer job_id);
	
	@Modifying
	@Query(value = "update JobProfile jp set jp.hr_status=?1 , jp.hr_remark=?2 where jp.job_id=?3")
	public Object updateJobProfileHrStatus( String hr_status, String hr_remark ,Integer job_id);
	

	@Modifying
	@Query(value = "update JobProfile jp set jp.hr_feedback_attachment=?2 where jp.job_id=?1")
	public void updatePath(Integer job_id, String t1);

	@Query(value = "select count(*) from JobProfile jp where jp.mobile=?1 and jp.active=?1")
	public Integer countMobileNumber(String mobile);

	@Query(value = "select mobile from JobProfile where job_id=?1")
	public String getMobile(Integer job_id);

	
	@Query(value = "select jp.job_id AS job_id,jp.firstname AS firstname,jp.gender AS gender,jp.dateofbirth AS dateofbirth,"
			+ "jp.mobile AS mobile,jp.email AS email,jp.martial_status AS martial_status,jp.street AS street,"
			+ "jp.locality AS locality,jp.pincode AS pincode,jp.key_skills AS key_skills,jp.link AS link,"
			+ "jp.linkedin_id AS linkedin_id,jp.resume_headline AS resume_headline,jp.current_location AS current_location,"
			+ "jp.reference_no AS reference_no,jp.created_date AS created_date,jp.modified_date AS modified_date,"
			+ "jp.active AS active,jp.country_id AS country_id,jp.state_id AS state_id,jp.city_id AS city_id,"
			+ "jp.mail_sent_status AS mail_sent_status,jp.comment_status AS comment_status,jp.mail_sent_to_candidate AS mail_sent_to_candidate,"
			+ "jp.emp_code_status AS emp_code_status,jp.job_type_id AS job_type_id,jp.designation_id AS designation_id,jp.hr_status AS hr_status,"
			+ "jp.hr_remark AS hr_remark,jp.hr_feedback_attachment AS hr_feedback_attachment,jp.marks_scored AS marks_scored,"
			+ "jp.mail_sent_to_candidate_date AS mail_sent_to_candidate_date,jp.mail_sent_to_interviewers_date AS mail_sent_to_interviewers_date,"
			+ "c.name as country_name,s.name as state_name,ct.name as city_name "
			+ "from job_profile jp "
			+ "left join countries c on jp.country_id=c.id "
			+ "left join states s on jp.state_id=s.id "
			+ "left join cities ct on jp.city_id=ct.id "
			+ "where jp.created_date LIKE ?1%  ",nativeQuery=true)
	public List<Map<String, Object>> jobProfileDetailsOnDate(String created_date);

	
	@Query(value = "select jp.job_id AS job_id,jp.firstname AS firstname,jp.gender AS gender,jp.dateofbirth AS dateofbirth,"
			+ "jp.mobile AS mobile,jp.email AS email,jp.martial_status AS martial_status,jp.street AS street,"
			+ "jp.locality AS locality,jp.pincode AS pincode,jp.key_skills AS key_skills,jp.link AS link,"
			+ "jp.linkedin_id AS linkedin_id,jp.resume_headline AS resume_headline,jp.current_location AS current_location,"
			+ "jp.reference_no AS reference_no,jp.created_date AS created_date,jp.modified_date AS modified_date,"
			+ "jp.active AS active,jp.country_id AS country_id,jp.state_id AS state_id,jp.city_id AS city_id,"
			+ "jp.mail_sent_status AS mail_sent_status,jp.comment_status AS comment_status,jp.mail_sent_to_candidate AS mail_sent_to_candidate,"
			+ "jp.emp_code_status AS emp_code_status,jp.job_type_id AS job_type_id,jp.designation_id AS designation_id,jp.hr_status AS hr_status,"
			+ "jp.hr_remark AS hr_remark,jp.hr_feedback_attachment AS hr_feedback_attachment,jp.marks_scored AS marks_scored,"
			+ "jp.mail_sent_to_candidate_date AS mail_sent_to_candidate_date,jp.mail_sent_to_interviewers_date AS mail_sent_to_interviewers_date,"
			+ "c.name as country_name,s.name as state_name,ct.name as city_name "
			+ "from job_profile jp "
			+ "left join countries c on jp.country_id=c.id "
			+ "left join states s on jp.state_id=s.id "
			+ "left join cities ct on jp.city_id=ct.id "
			+ "where jp.job_id=?1 ",nativeQuery=true)
	public Map<String, Object> jobProfileDetailsDataOnDate(Integer jobId);

}
