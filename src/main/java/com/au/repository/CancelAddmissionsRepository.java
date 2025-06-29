package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.CancelAddmissions;

@Repository
public interface CancelAddmissionsRepository extends JpaRepository<CancelAddmissions, Integer> {

	@Query(value ="select new map(csa.cancel_id as id,csa.auid as auid,csa.student_name as student_name,csa.modified_by as modified_by,"
			+ "csa.hostel_remarks as hostel_remarks,csa.remarks as remarks,sda.student_id as student_id,"
			+ "csa.approved_by as approved_by,csa.approved_date as approved_date,csa.attachment_path as attachment_path,"
			+ "csa.attachment_name as attachment_name,csa.active as active,ed.employee_name as approver_name,"
			+ "csa.created_date as created_date,csa.modified_date as modified_date,csa.created_by as created_by,"
			+ "ua.username as approvedByName,sda.deassign_status as deassign_status,csa.approved_remarks as approved_remarks,"
			+ "csa.rejected_remarks as rejected_remarks,csa.rejected_by as rejected_by,csa.rejected_date as rejected_date,"
			+ "csa.created_username as created_username,sd.school_name as school_name,sd.school_name_short as school_name_short) "
			+ "From CancelAddmissions csa "
			+ "left join Schools sd on sd.school_id=csa.school_id "
			+ "left join Student_Details sda on sda.auid=csa.auid "
			+ "left join UserAuthentication ua on ua.id=csa.approved_by "
			+ "left join EmployeeDetails ed on ed.email=ua.email "
			+ "Where sda.active=true And CONCAT(IfNull(csa.cancel_id,''),'',IfNull(csa.auid,'')"
			+ ",'',IfNull(csa.created_date,''),'',IfNull(csa.created_by,''),'',IfNull(csa.created_username,''))"
			+ " LIKE %?1% and (csa.approved_date Is null or csa.rejected_date Is null) and csa.active=true and sda.active=true")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="select new map(csa.cancel_id as id,csa.auid as auid,csa.student_name as student_name,csa.modified_by as modified_by,"
			+ "csa.hostel_remarks as hostel_remarks,csa.remarks as remarks,sda.student_id as student_id,"
			+ "csa.approved_by as approved_by,csa.approved_date as approved_date,csa.attachment_path as attachment_path,"
			+ "csa.attachment_name as attachment_name,csa.active as active,ed.employee_name as approver_name,"
			+ "csa.created_date as created_date,csa.modified_date as modified_date,csa.created_by as created_by,"
			+ "ua.username as approvedByName,sda.deassign_status as deassign_status,csa.approved_remarks as approved_remarks,"
			+ "csa.rejected_remarks as rejected_remarks,csa.rejected_by as rejected_by,csa.rejected_date as rejected_date,"
			+ "csa.created_username as created_username,sd.school_name as school_name,sd.school_name_short as school_name_short, "
			+ "ed.employee_name as approversName,ed1.employee_name as rejectedByName) "
			+ "From CancelAddmissions csa "
			+ "left join Schools sd on sd.school_id=csa.school_id "
			+ "left join Student_Details sda on sda.auid=csa.auid "
			+ "left join UserAuthentication ua on ua.id=csa.approved_by "
			+ "left join EmployeeDetails ed on ed.email=ua.email "
			+ "left join UserAuthentication ua1 on ua1.id=csa.rejected_by "
			+ "left join EmployeeDetails ed1 on ed1.email=ua1.email "
			+ " Where "
			+ "csa.approved_date Is null and csa.rejected_date Is null and csa.active=true and sda.active=true")
	public Page<Object> findAll3(Pageable pageable);

	
	
	@Query(value ="select new map(csa.cancel_id as id,csa.auid as auid,csa.student_name as student_name,csa.modified_by as modified_by,"
			+ "csa.hostel_remarks as hostel_remarks,csa.remarks as remarks,sda.student_id as student_id,"
			+ "csa.approved_by as approved_by,csa.approved_date as approved_date,csa.attachment_path as attachment_path,"
			+ "csa.attachment_name as attachment_name,csa.active as active,ed.employee_name as approver_name,"
			+ "csa.created_date as created_date,csa.modified_date as modified_date,csa.created_by as created_by,"
			+ "ua.username as approvedByName,sda.deassign_status as deassign_status,csa.approved_remarks as approved_remarks,"
			+ "csa.rejected_remarks as rejected_remarks,csa.rejected_by as rejected_by,csa.rejected_date as rejected_date,"
			+ "csa.created_username as created_username,sd.school_name as school_name,sd.school_name_short as school_name_short,"
			+ "ed.employee_name as approversName,ed1.employee_name as rejectedByName) "
			+ "From CancelAddmissions csa "
			+ "left join Schools sd on sd.school_id=csa.school_id "
			+ "left join Student_Details sda on sda.auid=csa.auid "
			+ "left join UserAuthentication ua on ua.id=csa.approved_by "
			+ "left join EmployeeDetails ed on ed.email=ua.email "
			+ "left join UserAuthentication ua1 on ua1.id=csa.rejected_by "
			+ "left join EmployeeDetails ed1 on ed1.email=ua1.email "
			+ "Where CONCAT(IfNull(csa.cancel_id,''),'',IfNull(csa.auid,''),'',IfNull(csa.created_date,'')"
			+ ",'',IfNull(csa.created_by,''),'',IfNull(csa.created_username,'')) LIKE %?1% "
			+ "and (csa.approved_date != null or csa.rejected_date != null) and csa.active=true")
	public Page<Object> listAllReport(Pageable pageable, Object keyword);
	
	@Query(value ="select new map(csa.cancel_id as id,csa.auid as auid,csa.student_name as student_name,csa.modified_by as modified_by,"
			+ "csa.hostel_remarks as hostel_remarks,csa.remarks as remarks,sda.student_id as student_id,"
			+ "csa.approved_by as approved_by,csa.approved_date as approved_date,csa.attachment_path as attachment_path,"
			+ "csa.attachment_name as attachment_name,csa.active as active,ed.employee_name as approver_name,"
			+ "csa.created_date as created_date,csa.modified_date as modified_date,csa.created_by as created_by,"
			+ "ua.username as approvedByName,sda.deassign_status as deassign_status,csa.approved_remarks as approved_remarks,"
			+ "csa.rejected_remarks as rejected_remarks,csa.rejected_by as rejected_by,csa.rejected_date as rejected_date,"
			+ "csa.created_username as created_username,sd.school_name as school_name,sd.school_name_short as school_name_short,"
			+ "ed.employee_name as approversName,ed1.employee_name as rejectedByName) "
			+ "From CancelAddmissions csa "
			+ "left join Schools sd on sd.school_id=csa.school_id "
			+ "left join Student_Details sda on sda.auid=csa.auid "
			+ "left join UserAuthentication ua on ua.id=csa.approved_by "
			+ "left join EmployeeDetails ed on ed.email=ua.email "
			+ "left join UserAuthentication ua1 on ua1.id=csa.rejected_by "
			+ "left join EmployeeDetails ed1 on ed1.email=ua1.email "
			+ "where (csa.approved_date Is Not null or csa.rejected_date Is Not null) and csa.active=true ")
	public Page<Object> listAllReport1(Pageable pageable);

	@Query(value ="select sd.totalDue from StudentDues sd where sd.studentId=:studentId")
	public Float checkDuesClearOrNot(Integer studentId);
}
