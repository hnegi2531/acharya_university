package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.NoDuesAssignmentDto;
import com.au.model.Department;
import com.au.model.EmployeeDetails;
import com.au.model.NoDuesAssignment;
import com.au.model.Resignation;



@Transactional
@Repository
public interface NoDuesAssignmentRepository extends JpaRepository<NoDuesAssignment, Integer> {
	
		@Query(value ="Select new map(no.no_dues_assignment_id as id,res.resignation_id as resignation_id,de.dept_id as dept_id,"
				+ "no.ip_address as ip_address,de.dept_name as dept_name,de.dept_name_short as dept_name_short,de.comments as Hodcomments,"
				+ "ua.username as noDueApproverName,no.approver_date as approver_date,no.approver_id as approver_id,"
				+ "no.ip_address as ip_address,no.comments as comments ) From NoDuesAssignment no "
				+ "left join Resignation res on res.resignation_id = no.resignation "
				+ "Left Join Department de On de.hod_id=no.approver_id "
				+ "Left Join UserAuthentication ua On ua.id=no.approver_id "
				+ "Where res.resignation_id=?1 And no.active=true ")
	public List<HashMap<String, Object>> getNoDueAssignmentData(Integer resignation_id);


		@Query(value = "select Count(*) from no_dues_assignment where emp_id=?1 And dept_id=?2 And no_due_status=1",nativeQuery = true)
		public Integer existsById(Integer empId, Integer dept);


		
		@Query(value ="Select na.no_dues_assignment_id as id,res.emp_id as emp_id,res.comments as comments,na.approver_date as approver_date,"
				+ "na.ip_address as ip_address,na.comments as noDueComments,res.resignation_id as resignation_id,na.approver_id as approver_id,"
				+ "na.created_date as created_date,na.modified_date as modified_date,na.created_by as created_by,na.modified_by as modified_by,na.active as active,"
				+ "ua.username as noDueApproverName,na.no_due_status as no_due_status,ed.emp_id as ApproverEmployeeId,ed.employee_name as ApproverName,ed.empcode as ApproverEmpcode,"
				+ "d.dept_id as ApproverDept_id,d.dept_name as ApproverDept_name,d.dept_name_short as ApproverDept_name_short,ed1.date_of_joining as ApproverDate_of_joining,d.comments as parameter,"
				+ "dgn.designation_short_name as ApproverDesignation_short_name,dgn.designation_name as ApproverDesignation_name,dgn.designation_id as ApproverDesignation_id,"
				+ "ed1.employee_name as employeeName,ed1.empcode as empcode,ed1.date_of_joining as date_of_joining,d1.comments as deptcomments,"
				+ "d1.dept_id as dept_id,d1.dept_name as dept_name,d1.dept_name_short as dept_name_short,"
				+ "res.employee_reason as employee_reason,res.reason as reason,"
				+ "dgn1.designation_short_name as designation_short_name,dgn1.designation_name as designation_name,dgn1.designation_id as designation_id,"
				+ "na.created_username as created_username,na.modified_username as modified_username From no_dues_assignment na "
				+ "left join resignation res on res.resignation_id = na.resignation_id "
				+ "Left Join user_details ua On ua.id=na.approver_id "
				+ "left join employee_details ed on ed.email = ua.email "
				+ "left join department d on ed.dept_id = d.dept_id "
				+ "Left Join designation dgn On ed.designation_id=dgn.designation_id "
				+ "left join employee_details ed1 on ed1.emp_id = na.emp_id "
				+ "left join department d1 on na.dept_id = d1.dept_id "
				+ "Left Join designation dgn1 On ed1.designation_id=dgn1.designation_id "
				+ "where res.resignation_id=?1 And na.active=true",nativeQuery = true)
		public List<Map<String, Object>> getAllNoDueAssignmentData(Integer resignation_id);
		
		
		@Query(value ="Select new map(no.no_dues_assignment_id as id,res.emp_id as emp_id,res.comments as comments,no.approver_date as approver_date,"
				+ "no.ip_address as ip_address,no.comments as noDueComments,res.resignation_id as resignation_id,no.approver_id as approver_id,"
				+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,res.nodues_approve_status as nodues_approve_status,"
				+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
				+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
				+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
				+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
				+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,de.comments as Hodcomments,"
				+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
				+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,no.created_date as noDueCreatedDate,"
				+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name ) From NoDuesAssignment no "
				+ "left join Resignation res on res.resignation_id = no.resignation "
				+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
				+ "left join EmployeeDetails ed on ed.emp_id = res.emp_id "
				+ "Left Join Department de On ed.dept_id=de.dept_id "
				+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
				+ "Where no.no_due_status=1 And CONCAT(IfNull(res.resignation_id,''),'',IfNull(res.applied_date,''),'',IfNull(res.relieving_date,''),'',"
				+ "IfNull(res.created_by,''),'',IfNull(ed.employee_name,''),'',IfNull(res.created_username,'')) LIKE %?1% Group by no.no_dues_assignment_id")
		public Page<Object> listAllWithKeyword(Pageable pageable, Object keyword);

		
		@Query(value ="Select new map(no.no_dues_assignment_id as id,res.emp_id as emp_id,res.comments as comments,no.approver_date as approver_date,"
				+ "no.ip_address as ip_address,no.comments as noDueComments,res.resignation_id as resignation_id,no.approver_id as approver_id,"
				+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,res.nodues_approve_status as nodues_approve_status,"
				+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
				+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
				+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
				+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
				+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,de.comments as Hodcomments,"
				+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
				+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,no.created_date as noDueCreatedDate,"
				+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name ) From NoDuesAssignment no "
				+ "left join Resignation res on res.resignation_id = no.resignation "
				+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
				+ "left join EmployeeDetails ed on ed.emp_id = res.emp_id "
				+ "Left Join Department de On ed.dept_id=de.dept_id "
				+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
				+ "Where no.no_due_status=1 Group by no.no_dues_assignment_id")
		public Page<Object> listAllWithOutKeyword(Pageable pageable1);


		@Query(value ="Select new map(no.no_dues_assignment_id as id,res.emp_id as emp_id,res.comments as comments,no.approver_date as approver_date,"
				+ "no.ip_address as ip_address,no.comments as noDueComments,res.resignation_id as resignation_id,no.approver_id as approver_id,"
				+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,res.nodues_approve_status as nodues_approve_status,"
				+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
				+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
				+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
				+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
				+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,de.comments as Hodcomments,"
				+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
				+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,no.created_date as noDueCreatedDate,"
				+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name ) From NoDuesAssignment no "
				+ "left join Resignation res on res.resignation_id = no.resignation "
				+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
				+ "left join EmployeeDetails ed on ed.emp_id = res.emp_id "
				+ "Left Join Department de On ed.dept_id=de.dept_id "
				+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
				+ "left join EmployeeDetails led on led.emp_id = ed.leave_approver1_emp_id "
				+ "left join UserAuthentication u on u.email = led.email "
				+ "Where u.id=?2 And no.no_due_status=1 And CONCAT(IfNull(res.resignation_id,''),'',IfNull(res.applied_date,''),'',IfNull(res.relieving_date,''),'',"
				+ "IfNull(res.created_by,''),'',IfNull(ed.employee_name,''),'',IfNull(res.created_username,'')) LIKE %?1% Group by no.no_dues_assignment_id")
		public Page<Object> listAllWithLeaveApprKeyword(Pageable pageable, Object keyword, Integer userId);

		
		@Query(value ="Select new map(no.no_dues_assignment_id as id,res.emp_id as emp_id,res.comments as comments,no.approver_date as approver_date,"
				+ "no.ip_address as ip_address,no.comments as noDueComments,res.resignation_id as resignation_id,no.approver_id as approver_id,"
				+ "res.applied_date as applied_date,res.requested_relieving_date as requested_relieving_date,res.nodues_approve_status as nodues_approve_status,"
				+ "res.status as status,res.relieving_date as relieving_date,res.reason as reason,res.nodues_approve_status as nodues_approve_status,"
				+ "res.employee_reason as employee_reason,res.additional_reason as additional_reason,res.created_date as created_date,"
				+ "res.modified_date as modified_date,res.created_by as created_by,res.modified_by as modified_by,res.active as active,"
				+ "res.created_username as created_username,res.modified_username as modified_username,res.resignation_status as resignation_status,"
				+ "ed.empcode as empcode,ed.date_of_joining as date_of_joining,ed.dept_id as dept_id,ed.email as email,de.comments as Hodcomments,"
				+ "rea.resignation_attachment_id as resignation_attachment_id,rea.attachment_path as attachment_path,"
				+ "de.dept_name as dept_name,de.dept_name_short as dept_name_short,dgn.designation_name as designation_name,no.created_date as noDueCreatedDate,"
				+ "ed.employee_name as employee_name,dgn.designation_short_name as designation_short_name ) From NoDuesAssignment no "
				+ "left join Resignation res on res.resignation_id = no.resignation "
				+ "Left Join ResignationAttachment rea On rea.resignation_id=res.resignation_id "
				+ "left join EmployeeDetails ed on ed.emp_id = res.emp_id "
				+ "Left Join Department de On ed.dept_id=de.dept_id "
				+ "Left Join Designation dgn On ed.designation_id=dgn.designation_id "
				+ "left join EmployeeDetails led on led.emp_id = ed.leave_approver1_emp_id "
				+ "left join UserAuthentication u on u.email = led.email "
				+ "Where u.id=?1 And no.no_due_status=1 Group by no.no_dues_assignment_id")
		public Page<Object> listAllLeaveApprWithOutKeyword(Pageable pageable1, Integer userId);

		
//		@Query(value = "SELECT st from NoDuesAssignment st where st.no_dues_assignment_id=?1 And st.active=true")
//		public Optional<NoDuesAssignment> getAllData(Integer no_dues_assignment_id);		


}
