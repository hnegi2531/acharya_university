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
import com.au.model.Resignation;
import com.au.model.StoreIndentRequest;

@Transactional
@Repository
public interface ResignationRepository extends JpaRepository<Resignation, Integer>{
	
	@Query(value = "SELECT res FROM Resignation res where res.active=true")
	public List<Resignation> findAll1();
	
	@Query(value ="Select new map(res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,de.comments as Hodcomments,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,u.id as leaveApproverUserId,"
			+ "dde.dept_id as leaveApproverdept_id,dde.dept_name as leaveApproverdept_name,dde.dept_name_short as leaveApproverdept_name_short,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "dgn.designation_short_name as designation_short_name,res.resignation_status as resignation_status,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.designation_id as designation_id,ed.dept_id as dept_id,"
			+ "jt.job_type as job_type,jt.job_short_name as job_short_name,jt.job_type_id as job_type_id,sc.school_id as school_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,org.org_id as org_id,org.org_name as org_name,org.org_type as org_type,"
			+ "ed.employee_name as employee_name,ed.job_id as job_id,ua.id as user_id) From Resignation res "
			+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
			+ "Left Join EmployeeDetails ed On ed.emp_id=res.emp_id "
			+ "left join UserAuthentication ua on ua.email=ed.email and ua.active=true "
			+ "Left Join Department de On ed.dept_id=de.dept_id "
			+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
			+ "Left Join JobType jt On ed.job_type_id=jt.job_type_id "
			+ "Left Join Schools sc On ed.school_id=sc.school_id "
			+ "Left Join Organization org On sc.org_id=org.org_id "
			+ "left join EmployeeDetails led on led.emp_id = ed.leave_approver1_emp_id "
			+ "left join UserAuthentication u on u.email = led.email "
			+ "Left Join Department dde On led.dept_id=dde.dept_id "
			+ "Where res.status=0 And CONCAT(IfNull(res.resignation_id,''),'',IfNull(res.applied_date,''),'',IfNull(res.relieving_date,''),'',"
			+ "IfNull(res.created_by,''),'',IfNull(ed.employee_name,''),'',IfNull(res.created_username,'')) LIKE %?1% group by res.resignation_id")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,de.comments as Hodcomments,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,u.id as leaveApproverUserId,"
			+ "dde.dept_id as leaveApproverdept_id,dde.dept_name as leaveApproverdept_name,dde.dept_name_short as leaveApproverdept_name_short,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "dgn.designation_short_name as designation_short_name,res.resignation_status as resignation_status,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.designation_id as designation_id,ed.dept_id as dept_id,"
			+ "jt.job_type as job_type,jt.job_short_name as job_short_name,jt.job_type_id as job_type_id,sc.school_id as school_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,org.org_id as org_id,org.org_name as org_name,org.org_type as org_type,"
			+ "ed.employee_name as employee_name,ed.job_id as job_id,ua.id as user_id) From Resignation res "
			+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
			+ "Left Join EmployeeDetails ed On ed.emp_id=res.emp_id "
			+ "left join UserAuthentication ua on ua.email=ed.email and ua.active=true "
			+ "Left Join Department de On ed.dept_id=de.dept_id "
			+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
			+ "Left Join JobType jt On ed.job_type_id=jt.job_type_id "
			+ "Left Join Schools sc On ed.school_id=sc.school_id "
			+ "Left Join Organization org On sc.org_id=org.org_id "
			+ "left join EmployeeDetails led on led.emp_id = ed.leave_approver1_emp_id "
			+ "left join UserAuthentication u on u.email = led.email "
			+ "Left Join Department dde On led.dept_id=dde.dept_id "
			+ "Where res.status=0 group by res.resignation_id")
	public Page<Object> findAll3(Pageable pageable);
	
	@Modifying
	@Query(value = "update Resignation res set res.active=false where res.resignation_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update Resignation res set res.active=true where res.resignation_id=?1")
	public void update1(Integer id);
	
	@Query(value="select  date_format(str_to_date(r.relieving_date,'%Y-%m-%d'),'%d-%m-%Y') as relievingDate from resignation r where r.emp_id=( select e.emp_id from employee_details e where e.empcode=?1  )  ",nativeQuery = true)
	public String getReleivingDateByEmpCode(String empCode);
	
	@Query(value = "SELECT Count(res) FROM Resignation res where res.emp_id=?1 And res.active=true And res.status not in (2)")
	public Integer checkResignationDataOfEmployee(Integer emp_id );
	
	@Query(value="select DATE_FORMAT(r.relieving_date, '%Y-%m-%d') as relievingDate from resignation r " +
			"where r.emp_id = ?1 and r.active = 1 and r.relieving_date is not null",nativeQuery = true)
	String getRelievingDate(Integer emp_id);

	
	@Query(value ="Select new map(res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,de.comments as Hodcomments,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name ) From Resignation res "
			+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
			+ "left join NoDuesAssignment na on na.resignation = res.resignation_id "
			+ "left join EmployeeDetails ed on ed.emp_id = res.emp_id "
			+ "Left Join Department de On ed.dept_id=de.dept_id "
			+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
			+ "Where na.approver_id=?2 And na.no_due_status is null And ed.active=true And (res.nodues_approve_status=1 OR res.nodues_approve_status=2) And rea.active=true And CONCAT(IfNull(res.resignation_id,''),'',IfNull(res.applied_date,''),'',IfNull(res.relieving_date,''),'',"
			+ "IfNull(res.created_by,''),'',IfNull(ed.employee_name,''),'',IfNull(res.created_username,'')) LIKE %?1% Group by res.resignation_id")
	public Page<Object> listAllWithKeyword(Pageable pageable, Object keyword, Integer userId);

	
	@Query(value ="Select new map(res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,de.comments as Hodcomments,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name ) From Resignation res "
			+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
			+ "left join NoDuesAssignment na on na.resignation = res.resignation_id "
			+ "left join EmployeeDetails ed on ed.emp_id = res.emp_id "
			+ "Left Join Department de On ed.dept_id=de.dept_id "
			+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
			+ "Where ed.active=true And (res.nodues_approve_status=1 OR res.nodues_approve_status=2) And rea.active=true And na.approver_id=?1 And na.no_due_status is null Group by res.resignation_id")
	public Page<Object> listAllWithOutKeyword(Pageable pageable1, Integer userId);


	@Query(value ="Select new map(res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,de.comments as Hodcomments,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,u.username as leaveApproverName,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name ) From Resignation res "
			+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
			+ "left join NoDuesAssignment na on na.resignation = res.resignation_id "
			+ "left join EmployeeDetails ed on ed.emp_id = res.emp_id "
			+ "Left Join Department de On ed.dept_id=de.dept_id "
			+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
			+ "left join EmployeeDetails led on led.emp_id = ed.leave_approver1_emp_id "
			+ "left join UserAuthentication u on u.email = led.email "
			+ "Where na.approver_id=?2 And ed.active=true And (res.nodues_approve_status=1 OR res.nodues_approve_status=2) And rea.active=true And na.no_due_status is null And CONCAT(IfNull(res.resignation_id,''),'',IfNull(res.applied_date,''),'',IfNull(res.relieving_date,''),'',"
			+ "IfNull(res.created_by,''),'',IfNull(ed.employee_name,''),'',IfNull(res.created_username,'')) LIKE %?1% Group by res.resignation_id")
	public Page<Object> listAllWithLeaveApprKeyword(Pageable pageable, Object keyword, Integer userId);

	
	@Query(value ="Select new map(res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,de.comments as Hodcomments,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,u.username as leaveApproverName,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name ) From Resignation res "
			+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
			+ "left join NoDuesAssignment na on na.resignation = res.resignation_id "
			+ "left join EmployeeDetails ed on ed.emp_id = res.emp_id "
			+ "Left Join Department de On ed.dept_id=de.dept_id "
			+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
			+ "left join EmployeeDetails led on led.emp_id = ed.leave_approver1_emp_id "
			+ "left join UserAuthentication u on u.email = led.email "
			+ "Where na.approver_id=?1 And ed.active=true And (res.nodues_approve_status=1 OR res.nodues_approve_status=2) And rea.active=true And na.no_due_status is null Group by res.resignation_id")
	public Page<Object> listAllLeaveApprWithOutKeyword(Pageable pageable1, Integer userId);
	
	
	
	
	@Query(value = "select Count(*) from resignation where emp_id=?1 And active=true And status in (0,1)",nativeQuery = true)
	public int checkEmpIdIsAlreadyPresentOrNot(Integer emp_id);

	@Query(value = "SELECT res.emp_id FROM Resignation res where res.resignation_id=?1 And res.active=true")
	public Integer getemployeeId(Integer resignation_id);

	
	@Query(value = "select * from resignation ORDER BY resignation_id Desc LIMIT 1",nativeQuery = true)
	public Resignation getLatestRelievingNo();

	
	@Query(value = "select ifNull(relieving_number,0) from resignation ORDER BY resignation_id Desc LIMIT 1",nativeQuery = true)
	public String getMaxRelievingNumberCount();

	
	@Query(value ="Select res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,res.nodues_approve_status as nodues_approve_status,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,de.comments as Hodcomments,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ed.school_id as school_id,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name  From resignation res "
			+ "Left Join resignation_attachment rea On rea.resignation_id=res.resignation_id "
			+ "left join employee_details ed on ed.emp_id = res.emp_id "
			+ "Left Join department de On ed.dept_id=de.dept_id "
			+ "Left Join designation dgn On ed.designation_id=dgn.designation_id "
			+ "Left Join schools sc On ed.school_id=sc.school_id "
			+ "where res.resignation_id=?1 And res.active=true ",nativeQuery = true)
	public List<Map<String, Object>> getAlResignationWithRelievingNo(Integer resignation_id);
	
	
	
	@Query(value ="Select new map(res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,de.comments as Hodcomments,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,u.id as leaveApproverUserId,"
			+ "dde.dept_id as leaveApproverdept_id,dde.dept_name as leaveApproverdept_name,dde.dept_name_short as leaveApproverdept_name_short,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "dgn.designation_short_name as designation_short_name,res.resignation_status as resignation_status,et.empTypeShortName as empTypeShortName,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.designation_id as designation_id,ed.dept_id as dept_id,"
			+ "jt.job_type as job_type,jt.job_short_name as job_short_name,jt.job_type_id as job_type_id,sc.school_id as school_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,org.org_id as org_id,org.org_name as org_name,org.org_type as org_type,"
			+ "ed.employee_name as employee_name,ed.job_id as job_id,ua.id as user_id, off.offer_id as offer_id) From Resignation res "
			+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
			+ "Left Join EmployeeDetails ed On ed.emp_id=res.emp_id "
			+ "Left Join EmployeeType et On et.empTypeId=ed.emp_type_id "
			+ "Left Join Offer off On off.job_id=ed.job_id "
			+ "left join UserAuthentication ua on ua.email=ed.email and ua.active=true "
			+ "Left Join Department de On ed.dept_id=de.dept_id "
			+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
			+ "Left Join JobType jt On ed.job_type_id=jt.job_type_id "
			+ "Left Join Schools sc On ed.school_id=sc.school_id "
			+ "Left Join Organization org On sc.org_id=org.org_id "
			+ "left join EmployeeDetails led on led.emp_id = ed.leave_approver1_emp_id "
			+ "left join UserAuthentication u on u.email = led.email "
			+ "Left Join Department dde On led.dept_id=dde.dept_id "
			+ "Where res.status=1 And CONCAT(IfNull(res.resignation_id,''),'',IfNull(res.applied_date,''),'',IfNull(res.relieving_date,''),'',"
			+ "IfNull(res.created_by,''),'',IfNull(ed.employee_name,''),'',IfNull(res.created_username,'')) LIKE %?1% group by res.resignation_id")
	public Page<Object> fetchAllResignationHistoryDetails(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,de.comments as Hodcomments,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,u.id as leaveApproverUserId,"
			+ "dde.dept_id as leaveApproverdept_id,dde.dept_name as leaveApproverdept_name,dde.dept_name_short as leaveApproverdept_name_short,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "dgn.designation_short_name as designation_short_name,res.resignation_status as resignation_status,et.empTypeShortName as empTypeShortName,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.designation_id as designation_id,ed.dept_id as dept_id,"
			+ "jt.job_type as job_type,jt.job_short_name as job_short_name,jt.job_type_id as job_type_id,sc.school_id as school_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,org.org_id as org_id,org.org_name as org_name,org.org_type as org_type,"
			+ "ed.employee_name as employee_name,ed.job_id as job_id,ua.id as user_id, off.offer_id as offer_id) From Resignation res "
			+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
			+ "Left Join EmployeeDetails ed On ed.emp_id=res.emp_id "
			+ "Left Join EmployeeType et On et.empTypeId=ed.emp_type_id "
			+ "Left Join Offer off On off.job_id=ed.job_id "
			+ "left join UserAuthentication ua on ua.email=ed.email and ua.active=true "
			+ "Left Join Department de On ed.dept_id=de.dept_id "
			+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
			+ "Left Join JobType jt On ed.job_type_id=jt.job_type_id "
			+ "Left Join Schools sc On ed.school_id=sc.school_id "
			+ "Left Join Organization org On sc.org_id=org.org_id "
			+ "left join EmployeeDetails led on led.emp_id = ed.leave_approver1_emp_id "
			+ "left join UserAuthentication u on u.email = led.email "
			+ "Left Join Department dde On led.dept_id=dde.dept_id "
			+ "Where res.status=1 group by res.resignation_id")
	public Page<Object> fetchAllResignationHistoryDetails1(Pageable pageable);

	@Query(value ="Select res.resignation_id as id,res.emp_id as emp_id,res.comments as comments,res.relieving_number as relieving_number,"
			+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,res.nodues_approve_status as nodues_approve_status,"
			+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,de.comments as Hodcomments,"
			+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
			+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
			+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
			+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,"
			+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,ed.school_id as school_id,"
			+ "org.org_id as org_id,org.org_name as org_name,org.org_type as org_type,ed.gender as gender,"
			+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
			+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name  From resignation res "
			+ "Left Join resignation_attachment rea On rea.resignation_id=res.resignation_id "
			+ "left join employee_details ed on ed.emp_id = res.emp_id "
			+ "Left Join department de On ed.dept_id=de.dept_id "
			+ "Left Join designation dgn On ed.designation_id=dgn.designation_id "
			+ "Left Join schools sc On ed.school_id=sc.school_id "
			+ "Left Join organization org On sc.org_id=org.org_id "
			+ "where res.resignation_id=?1 And res.active=true ",nativeQuery = true)
	public List<Map<String, Object>> getAllResignationDetailsData(Integer resignation_id);
	
}

