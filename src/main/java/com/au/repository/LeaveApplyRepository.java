package com.au.repository;

import com.au.model.LeaveApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public interface LeaveApplyRepository extends JpaRepository<LeaveApply, Integer>
{


	@Query(value = "select count(*) from LeaveApply la where la.emp_id=?1")
	public Integer getcountOfEmp_id(Integer emp_id);


	@Query(value = "select count(*) from LeaveApply la where la.employee_leave_id=?1")
	public Integer getcountOfEmployee_leave_id(Integer employee_leave_id);


	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,ed.employee_name as employee_name) from LeaveApply la "
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "where CONCAT(IfNull(la.created_username,''),'',IfNull(la.employee_leave_id,''),'',IfNull(la.emp_id,''),'',IfNull(la.no_of_days_applied,''),"
			+ "'',IfNull(la.created_by,''),'',IfNull(la.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);


	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,ed.employee_name as employee_name) from LeaveApply la "
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id")
	public Page<Object> getAllSortedData(Pageable pageable);


	@Query(value = "SELECT la from LeaveApply la where la.active=true")
	public List<LeaveApply> findAll11();



	@Modifying
	@Query(value = "update LeaveApply la set la.active=false where la.leave_apply_id=?1")
	public void updateDept(Integer id);

	@Modifying
	@Query(value = "update LeaveApply la set la.active=true where la.leave_apply_id=?1")
	public void updateDept1(Integer id);

	@Modifying
	@Query(value = "update LeaveApply la set la.leave_apply_attachment_path=?2 where la.leave_apply_id=?1")
	public void updatePath(Integer leave_apply_id, String t2);

	@Modifying
	@Query(value = "update LeaveApply la set la.leave_apply_attachment_path2=?2 where la.leave_apply_id=?1")
	public void updatePath2(Integer leave_apply_id, String t2);


	@Query(value = "SELECT la from LeaveApply la where la.active=true")
	public List<LeaveApply> listEmailByDept11();

	@Query(value = "select count(*) from leave_apply la where la.emp_id=?1 and month(STR_TO_DATE(la.from_date, '%d-%m-%Y')) =?2 and la.leave_id=?3",nativeQuery=true)
	public Integer getCountOfOneHourPermission(Integer emp_id, Integer d, Integer leave_id);


	@Query(value = "SELECT * FROM leave_apply la  WHERE "
			+ "        ( "
			+ "            STR_TO_DATE(:date, '%d-%m-%Y') BETWEEN STR_TO_DATE(la.from_date, '%d-%m-%Y') AND STR_TO_DATE(la.to_date, '%d-%m-%Y') "
			+ "        )  "
			+ "        AND la.emp_id =:empId  "
			+ "		   AND la.active = true "
			+ "        AND la.approved_status = 2 ",nativeQuery=true)
   LeaveApply getLeaveByFromDateAndEmpId(@Param("date") String date, @Param("empId") Integer empId);

	@Query(value = "SELECT distinct(la.leave_id) from LeaveApply la "
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "where la.active=true And ed.emp_id=?1 And year(STR_TO_DATE(la.from_date,'%d-%m-%Y'))=?2 And la.approved_status=2")
	public List<Integer> getLeaveIdByUserId(Integer emp_id, Integer fdd);

	@Query(value = "select sum(la.no_of_days_applied) from leave_apply la "
			+ "where la.leave_id=?1 And year(STR_TO_DATE(la.from_date,'%d-%m-%Y'))=?2 And "
			+ "la.emp_id=?3 And la.active=true And la.approved_status=2", nativeQuery = true)
	public Double getCountOfLeaveType(Integer ld, Integer fdd, Integer emp_id);

	@Query(value = "select lt.leave_type from LeaveType lt where lt.leave_id=?1 And lt.active=true")
	public String getNameOfLeave(Integer ld);


	@Query(value = " select  e.emp_id as emp_id ,CONCAT( e.employee_name , ',' ,e.empcode, ',' ,d.dept_name) as employeeDetails,"
			+ "e.leave_approver1_emp_id as leave_approver1_emp_id,e.leave_approver2_emp_id as leave_approver2_emp_id,e.report_id As report_id,"
			+ "ed1.employee_name As approverName1,ed2.employee_name As approverName2,ed3.employee_name As reportName "
			+ "from employee_details e "
			+ "left join department d on d.dept_id=e.dept_id "
			+ "left join employee_details ed1 on ed1.emp_id=e.leave_approver1_emp_id "
			+ "left join employee_details ed2 on ed2.emp_id=e.leave_approver2_emp_id "
			+ "left join employee_details ed3 on ed3.emp_id=e.report_id "
			+ "where e.active=1  ", nativeQuery = true)
	public List<Map<String, Object>> getAllEmployeesForLeaveApply();



	@Query(value = "select la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name "
			+ "from leave_apply la "
			+ "left join employee_details ed on ed.emp_id=la.emp_id "
			+ "left join employee_details ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join employee_details ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join leave_type lt on lt.leave_id=la.leave_id "
			+ "left join schools s on s.school_id=ed.school_id "
			+ "left join department d on d.dept_id=ed.dept_id "
			+ "left join designation des on des.designation_id=ed.designation_id "
			+ "left join user_details ua on ua.id=la.cancel_by "
			+ "where la.emp_id=?1 And la.leave_id=?2 And la.active=true And la.approved_status=2",nativeQuery=true)
	public List<Map<String, Object>> getLeaveKettyDetailsByUserIdAndLeaveId(Integer emp_id, Integer leave_id);


	@Query(value = "select distinct(la.year) from LeaveApply la")
	public List<String> getDistinctYear();


	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required AS leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " + "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " + "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by "
			+ "where CONCAT(IfNull(lt.type,''),'',IfNull(lt.leave_type_short,''),'',IfNull(lt.leave_type,''),'',IfNull(la.no_of_days_applied,''),'',"
			+ "IfNull(la.from_date,''),'',IfNull(la.to_date,''),'',IfNull(la.leave_comments,''),'',IfNull(ed.employee_name,''),'',"
			+ "IfNull(ed.empcode,''),'',IfNull(ed1.employee_name,''),'',IfNull(ed2.employee_name,''),'',"
			+ "IfNull(la.approved_status,''),'',IfNull(la.created_by,''),'',IfNull(la.created_date,'')) LIKE %?1% and la.year=?2")
	public Page<Object> getAllDataFilteredByKeywordLeaveApplyDetailsByYear(Pageable pageable, Object keyword, String year);

	@Query(value = "select emp.emp_id from EmployeeDetails emp where emp.school_id = "
			+ "(select emp1.school_id from EmployeeDetails emp1 where emp1.emp_id=?1 and emp1.active=true) and emp.active=true")
	public List<Integer> getEmployeeIdsList(Integer emp_id);


	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " + "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " + "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by "
			+ "where CONCAT(IfNull(lt.type,''),'',IfNull(lt.leave_type_short,''),'',IfNull(lt.leave_type,''),'',IfNull(la.no_of_days_applied,''),'',"
			+ "IfNull(la.from_date,''),'',IfNull(la.to_date,''),'',IfNull(la.leave_comments,''),'',IfNull(ed.employee_name,''),'',"
			+ "IfNull(ed.empcode,''),'',IfNull(ed1.employee_name,''),'',IfNull(ed2.employee_name,''),'',"
			+ "IfNull(la.approved_status,''),'',IfNull(la.created_by,''),'',IfNull(la.created_date,'')) LIKE %?1% and la.year=?2 and la.emp_id IN (?3)")
	public Page<Object> getAllDataFilteredByKeywordLeaveApplyDetailsByInstitueAndYear(Pageable pageable, Object keyword, String year, List<Integer> emp_id);

	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " 
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " 
			+ "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by "
			+ "where CONCAT(IfNull(lt.type,''),'',IfNull(lt.leave_type_short,''),'',IfNull(lt.leave_type,''),'',IfNull(la.no_of_days_applied,''),'',"
			+ "IfNull(la.from_date,''),'',IfNull(la.to_date,''),'',IfNull(la.leave_comments,''),'',IfNull(ed.employee_name,''),'',"
			+ "IfNull(ed.empcode,''),'',IfNull(ed1.employee_name,''),'',IfNull(ed2.employee_name,''),'',"
			+ "IfNull(la.approved_status,''),'',IfNull(la.created_by,''),'',IfNull(la.created_date,'')) LIKE %?1% and la.year=?2 and (ed.leave_approver1_emp_id=?3 or ed.leave_approver2_emp_id=?3)")
	public Page<Object> getAllDataFilteredByKeywordLeaveApplyDetailsByApproverId(Pageable pageable, Object keyword, String year, Integer emp_id);


	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " 
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " 
			+ "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by where la.year=?1")
	public Page<Object> getAllSortedLeaveApplyDetailsByYear(Pageable pageable, String year);

	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " + "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " + "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by where la.year=?1 and la.emp_id IN (?2)")
	public Page<Object> getAllSortedLeaveApplyDetailsByInstitueAndYear(Pageable pageable, String year, List<Integer> emp_id);


	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " 
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " 
			+ "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by where la.year=?1 and (ed.leave_approver1_emp_id=?2 or ed.leave_approver2_emp_id=?2)")
	public Page<Object> getAllSortedLeaveApplyDetailsByApproverId(Pageable pageable, String year, Integer emp_id);


	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " 
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " 
			+ "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "where CONCAT(IfNull(lt.type,''),'',IfNull(lt.leave_type_short,''),'',IfNull(lt.leave_type,''),'',IfNull(la.no_of_days_applied,''),'',"
			+ "IfNull(la.from_date,''),'',IfNull(la.to_date,''),'',IfNull(la.leave_comments,''),'',IfNull(ed1.employee_name,''),'',"
			+ "IfNull(ed2.employee_name,''),'',IfNull(ed.employee_name,''),'',"
			+ "IfNull(la.approved_status,''),'',IfNull(la.created_by,''),'',IfNull(la.created_date,'')) LIKE %?1% and "
			+ "la.approved_status=1 and ((la.leave_approved_by1=?3 and la.leave_app1_status IS NULL) or (la.leave_approved_by2=?3 and la.leave_app1_status=true)) "
			+ "and str_to_date(la.from_date, '%d-%m-%Y')>=?2")
	public Page<Object> getAllDataFilteredByKeywordForApprovers1(Pageable pageable, Object keyword,
			 java.time.LocalDate currentDate,Integer approver_id);

	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " 
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " 
			+ "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id where "
			+ "la.approved_status=1 and ((la.leave_approved_by1=?2 and la.leave_app1_status IS NULL) or (la.leave_approved_by2=?2 and la.leave_app1_status=true)) "
			+ "and str_to_date(la.from_date, '%d-%m-%Y')>=?1")
	public Page<Object> getAllSortedDataForApprovers2(Pageable pageable, java.time.LocalDate currentDate, Integer approver_id);

	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " 
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " 
			+ "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "where CONCAT(IfNull(lt.type,''),'',IfNull(lt.leave_type_short,''),'',IfNull(lt.leave_type,''),'',IfNull(la.no_of_days_applied,''),'',"
			+ "IfNull(la.from_date,''),'',IfNull(la.to_date,''),'',IfNull(la.leave_comments,''),'',IfNull(ed1.employee_name,''),'',"
			+ "IfNull(ed2.employee_name,''),'',IfNull(ed.employee_name,''),'',"
			+ "IfNull(la.approved_status,''),'',IfNull(la.created_by,''),'',IfNull(la.created_date,'')) LIKE %?1% and "
			+ "la.approved_status=1 and ((la.leave_approved_by1=?3 and la.leave_app1_status IS NULL) or (la.leave_approved_by2=?3 and la.leave_app1_status=true)) "
			+ "and str_to_date(la.from_date, '%d-%m-%Y')>=?2")
	public Page<Object> getAllDataFilteredByKeywordForApprovers3(Pageable pageable, Object keyword,
			java.time.LocalDate localDate, Integer approver_id);


	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " 
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " 
			+ "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id where "
			+ "la.approved_status=1 and ((la.leave_approved_by1=?2 and la.leave_app1_status IS NULL) or (la.leave_approved_by2=?2 and la.leave_app1_status=true)) "
			+ "and str_to_date(la.from_date, '%d-%m-%Y')>=?1")
	public Page<Object> getAllSortedDataForApprovers4(Pageable pageable, java.time.LocalDate localDate, Integer approver_id);

	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type as leave_type,lt.leave_type_short as leave_type_short,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.email as employee_email,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name,"
			+ "ed1.email as approver_1_email,ed2.email as approver_2_email) "
			+ "from LeaveApply la "
			+ "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " + "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by where la.leave_apply_id=?1")
	public HashMap<String, Object> getApproverDataSendingEmail(Integer leave_apply_id);


	@Query(value = "SELECT * FROM leave_apply la \r\n"
			+ "WHERE la.emp_id=:emp_id and (la.approved_status=1 or la.approved_status=2 ) and "
			+ "(MONTH(str_to_date(la.from_date,'%d-%m-%Y'))=MONTH(str_to_date(:from_date,'%d-%m-%Y')) or \r\n"
			+ "MONTH(str_to_date(la.from_date,'%d-%m-%Y'))=MONTH(str_to_date(:from_date,'%d-%m-%Y')+INTERVAL 1 MONTH) or \r\n"
			+ "MONTH(str_to_date(la.from_date,'%d-%m-%Y'))=MONTH(str_to_date(:from_date,'%d-%m-%Y')-INTERVAL 1 MONTH) and \r\n"
			+ "YEAR(str_to_date(la.from_date,'%d-%m-%Y'))=YEAR(str_to_date(:from_date,'%d-%m-%Y')) or \r\n"
			+ "YEAR(str_to_date(la.from_date,'%d-%m-%Y'))=YEAR(str_to_date(:from_date,'%d-%m-%Y')+INTERVAL 1 YEAR)) and la.active=true", nativeQuery = true)
	public List<LeaveApply> checkValidationForLeaveOnFromDate(String from_date, Integer emp_id);


	@Query(value=" select count(*) from leave_apply where  STR_TO_DATE(:strDate, '%d-%m-%Y') between STR_TO_DATE(:from_date, '%d-%m-%Y') and "
			+ "STR_TO_DATE(:to_date, '%d-%m-%Y') and emp_id=:emp_id  and approved_status != 3  and active=true", nativeQuery = true)
	public Integer checkValidationForDatesAppliedAndLeaveType(String strDate, String from_date, String to_date,Integer emp_id);


	@Query(value = "SELECT la.emp_id from leave_apply la where STR_TO_DATE(?1,'%d-%m-%Y') "
			+ "between STR_TO_DATE(la.from_date, '%d-%m-%Y') and STR_TO_DATE(la.to_date, '%d-%m-%Y') and la.active=true", nativeQuery = true)
	public List<Integer> getEmplIdsOnLeave(String date);

	@Query(value = "select count(*) from leave_apply la where la.emp_id=?1 And month(STR_TO_DATE(la.to_date, '%Y-%m-%d'))=?2 "
			+ "And la.leave_id=?3  and la.approved_status != 3  And la.active=true",nativeQuery=true)
	public Integer getCountOfAppliedOneHourPermission(Integer emp_id, Integer month ,Integer leave_id);

	@Query(value = "select count(*) from leave_apply la where la.emp_id=?1  "
			+ " and la.leave_id=?2 and la.year=?3 and la.active=true  and ( la.approved_status=1 or   la.approved_status=2 )",nativeQuery=true)
	public Integer getCountLeaveApply(Integer emp_id, Integer leave_id, String year);


	@Query(value = "select ed.email as approver_1_email,ed.email as approver_2_email "
			+ "from employee_details ed "
			+ "left join leave_apply la on ed.emp_id=la.emp_id "
			+ "left join employee_details ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join employee_details ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "where ed.emp_id=?1 order by la.created_date desc limit 1",nativeQuery=true)
	public Map<String, Object> getApproverDataSendingEmail11(Integer emp_id);


		@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " + "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " + "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by "
			+ "where CONCAT(IfNull(lt.type,''),'',IfNull(lt.leave_type_short,''),'',IfNull(lt.leave_type,''),'',IfNull(la.no_of_days_applied,''),'',"
			+ "IfNull(la.from_date,''),'',IfNull(la.to_date,''),'',IfNull(la.leave_comments,''),'',IfNull(ed1.employee_name,''),'',IfNull(ed2.employee_name,''),'',"
			+ "IfNull(la.approved_status,''),'',IfNull(la.created_by,''),'',IfNull(la.created_date,'')) LIKE %?1% and la.emp_id=?2 and la.year=?3")
	public Page<Object> getAllDataFilteredByKeywordForEndUser(Pageable pageable, Object keyword, Integer emp_id, String year);

	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,la.leave_apply_attachment_path2 as leave_apply_attachment_path2,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " + "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " + "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by where la.emp_id=?1 and la.year=?2")
	public Page<Object> getAllSortedDataForEndUser(Pageable pageable, Integer emp_id, String year);

	@Query(value = "select count(*) from leave_apply la where la.from_date=?1 and la.leave_id=?2 and la.emp_id IN (?3) and la.active=true and la.approved_status != 3", nativeQuery=true)
	public Integer checkForAlreadyAppliedOrNot(String from_date, Integer leave_id, List<Integer> emp_id);

	@Query(value = "select count(*) from leave_apply la where la.compoff_worked_date=?1 and la.leave_id=?2 and la.emp_id IN (?3) and la.cancel_by IS NULL", nativeQuery=true)
	public Integer checkForCompOffAlreadyAppliedOrNot(String from_date, Integer leave_id, List<Integer> emp_id);

	@Query(value = "select count(*) from leave_apply la where la.from_date=?1 and la.leave_id=?3 and la.approved_status=2"
			+ " and la.emp_id = ?2 and la.active=true", nativeQuery=true)
	public Integer checkForAppliedOnDuty(String compOff_worked_date, Integer l, Integer onDutyLeaveId);

    @Query(value=" select la from LeaveApply la where la.from_date=:from_date and la.to_date=:to_date and la.emp_id=:emp_id "
    		+ "and (la.approved_status=1 or la.approved_status=2 ) ")
	public LeaveApply existsBetweenDate(String from_date, String to_date, Integer emp_id);

    @Query(value=" SELECT DATE_SUB(?1, INTERVAL 1 DAY)", nativeQuery=true)
	public Date getOneDayBeforeDate(String date);

    @Query(value="select * from leave_apply la where (la.from_date=?1 or la.to_date=?1) "
    		+ " and la.emp_id=?2 and la.leave_id IN (1,2) and la.approved_status != 3  and la.active=true and (la.approved_status=1 or la.approved_status=2) ", nativeQuery=true)
	public LeaveApply checkOneDayBeforeLeaveAppliedOrNot(String prevDate, Integer emp_id);

    @Query(value="select * from leave_apply la where (la.from_date=?1 or la.to_date=?1) "
    		+ " and la.emp_id=?2 and la.leave_id IN (1,2) and la.active=true and (la.approved_status=1 or la.approved_status=2) ", nativeQuery=true)
	public LeaveApply checkTwoDayBeforeLeaveAppliedOrNot(String prevDate, Integer emp_id);

    @Query(value="select * from leave_apply la where (la.from_date=?1 or la.to_date=?1) "
    		+ " and la.emp_id=?2 and la.leave_id IN (1,2) and la.active=true and (la.approved_status=1 or la.approved_status=2) ", nativeQuery=true)
	public LeaveApply checkThreeDayBeforeLeaveAppliedOrNot(String prevDate, Integer emp_id);

    @Query(value="select * from leave_apply la where (la.from_date=?1 or la.to_date=?1) "
    		+ " and la.emp_id=?2 and la.leave_id IN (1,2) and la.active=true and (la.approved_status=1 or la.approved_status=2) ", nativeQuery=true)
	public LeaveApply checkOneDayAfterLeaveAppliedOrNot(String nextDate, Integer emp_id);

//    @Query(value=" select * from leave_apply la "
//    		+ " where (MONTH(str_to_date(la.from_date,'%d-%m-%Y'))=MONTH(str_to_date(:fromDate,'%d-%m-%Y')) "
//    		+ " and la.emp_id=?2 and la.active=true and la.approved_status=2) ", nativeQuery=true)
//	public List<LeaveApply> checkAlreadyAppliedLeaves(String fromDate, Integer emp_id);

    @Query(value = "select * from leave_apply la where la.from_date=?1 and la.emp_id = ?2 and la.leave_id=?3 and la.approved_status != 3 and la.active=true", nativeQuery=true)
	public LeaveApply checkValidationForLeaveOnFromDateByLeaveType(String from_date, Integer l, Integer leave_id);

    @Query(value = "select * from leave_apply la where la.emp_id = ?1 and la.leave_id=?2 and la.approved_status != 3 and la.active=true", nativeQuery=true)
	public LeaveApply checkValidationForLeaveByLeaveType(Integer emp_id, Integer leave_id);

	@Modifying
	@Query(value = "update leave_apply la set la.leave_approved_by1=?2 where la.leave_approved_by1=?1 and la.approved_status=1 and la.active=true", nativeQuery=true)
	public void updateLeaveApplyApprover1(Integer key, Integer value);

	@Modifying
	@Query(value = "update leave_apply la set la.leave_approved_by2=?2 where la.leave_approved_by2=?1 and la.approved_status=1 and la.active=true", nativeQuery=true)
	public void updateLeaveApplyApprover2(Integer key, Integer value);


	@Query(value = "select la.from_date as fromDate, la.to_date as toDate from leave_apply la " +
			"where la.emp_id = ?1 and la.leave_id = ?2 and la.approved_status= 2", nativeQuery = true)
	Map<String, String> findByEmp_IdAndLeave_Id(Integer empId, Integer leaveId);


	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,ed3.employee_name as cancelled_by_name,"
			+ "el.updated_days_count as updated_days_count,ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " + "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join EmployeeDetails ed3 on ed3.emp_id=la.cancel_by "
			+ "left join LeaveKitty el on el.emp_id = la.emp_id "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " + "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by "
			+ "where CONCAT(IfNull(lt.type,''),'',IfNull(lt.leave_type_short,''),'',IfNull(lt.leave_type,''),'',IfNull(la.no_of_days_applied,''),'',"
			+ "IfNull(la.from_date,''),'',IfNull(la.to_date,''),'',IfNull(la.leave_comments,''),'',IfNull(ed1.employee_name,''),'',"
			+ "IfNull(ed2.employee_name,''),'',IfNull(ed.employee_name,''),'',IfNull(ed3.employee_name,''),'',IfNull(la.cancel_date,''),'',"
			+ "IfNull(la.approved_status,''),'',IfNull(la.created_by,''),'',IfNull(la.created_date,'')) LIKE %?1% and "
			+ "la.emp_id IN ?2 and la.approved_status=2 or la.approved_status=3 and str_to_date(la.from_date, '%d-%m-%Y')>=?3")
	public Page<Object> getAllApprovedOrCancelledLeavesDataFilteredByKeyword(Pageable pageable, Object keyword,
			List<Integer> emp_ids);

	@Query(value = "select new map(la.leave_apply_id as id,la.employee_leave_id as employee_leave_id,la.emp_id as emp_id,la.no_of_days_applied as no_of_days_applied,la.cancel_comments as cancel_comments,"
			+ "la.total_leaves_applicable as total_leaves_applicable,la.created_username as created_username,la.modified_username as modified_username,la.leave_apply_attachment_path as leave_apply_attachment_path,"
			+ "la.created_date as created_date,la.modified_date as modified_date,la.created_by as created_by,la.alternative_emails as alternative_emails,"
			+ "la.remaining_days_left as remaining_days_left,la.leave_comments as leave_comments,la.reporting_approver_comment as reporting_approver_comment,la.reporting_approver1_comment as reporting_approver1_comment,"
			+ "la.leave_app1_status as leave_app1_status,la.leave_app2_status as leave_app2_status,la.alternative_lecturer_name as alternative_lecturer_name,la.contact_no as contact_no,la.year as year,"
			+ "la.shift as shift,la.special_leave_id as special_leave_id,la.cancel_by as cancel_by,la.cancel_date as cancel_date,la.leave_approved_date as leave_approved_date,"
			+ "la.compoff_worked_date as compoff_worked_date,la.leave_approved2_date as leave_approved2_date, la.leave_approved_by1 as leave_approved_by1,la.leave_approved_by2 as leave_approved_by2,"
			+ "la.modified_by as modified_by,la.active as active,la.approved_status as approved_status,la.from_date as from_date,la.to_date as to_date,la.approved_status as approved_status,"
			+ "lt.type as type,lt.leave_type_short as leave_type_short,lt.leave_type as leave_type,s.school_name_short as school_name_short,s.school_name as school_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.id as cancelby_id,ua.username as cancelled_username,lt.leave_type_attachment_required As leave_type_attachment_required,"
			+ "des.designation_id as designation_id,des.designation_name as designation_name,des.designation_name as designation_name,"
			+ "el.updated_days_count as updated_days_count,ed1.employee_name as approver_1_name,ed2.employee_name as approver_2_name) "
			+ "from LeaveApply la " + "left join EmployeeDetails ed on ed.emp_id=la.emp_id "
			+ "left join EmployeeDetails ed1 on ed1.emp_id=la.leave_approved_by1 "
			+ "left join EmployeeDetails ed2 on ed2.emp_id=la.leave_approved_by2 "
			+ "left join LeaveKitty el on el.emp_id = la.emp_id "
			+ "left join LeaveType lt on lt.leave_id=la.leave_id " + "left join Schools s on s.school_id=ed.school_id "
			+ "left join Department d on d.dept_id=ed.dept_id "
			+ "left join Designation des on des.designation_id=ed.designation_id "
			+ "left join UserAuthentication ua on ua.id=la.cancel_by "
			+ "where la.emp_id IN ?1 and la.approved_status=2 or la.approved_status=3")
	public Page<Object> getAllApprovedOrCancelledLeavesgetAllSortedData(Pageable pageable, List<Integer> emp_ids);


	 @Query(value = "Select la.emp_id from LeaveApply la where la.leave_app1_status IS NULL and la.leave_approved_by1=?1 and la.active=true")
	  	public List<Integer> getLeaveAppliedEmpIdsUnderSpecificApproversId_1(Integer id);

     @Query(value = "Select la.emp_id from LeaveApply la where la.leave_approved_by2=?1 and la.active=true")
  	public List<Integer> getLeaveAppliedEmpIdsUnderSpecificApproversId_2(String id);


 	@Query(value = "SELECT la.emp_id from LeaveApply la where la.leave_app1_status=true and la.emp_id IN ?1 and la.active=true")
 	public List<Integer> getApprovedByApprovaer_1(List<Integer> emp_ids);

	@Query(value = "select count(*) from leave_apply la where " +
			"month(STR_TO_DATE(la.from_date,'%d-%m-%Y')) = month(STR_TO_DATE(:from_date,'%d-%m-%Y')) and " +
			"year(STR_TO_DATE(la.from_date,'%d-%m-%Y')) = year(STR_TO_DATE(:from_date,'%d-%m-%Y')) and la.leave_id=:leave_id and la.emp_id =:emp_id and la.active=true and la.approved_status != 3", nativeQuery=true)
	public Integer checkVacationLeaveAlreadyAppliedOrNot(String from_date, Integer leave_id, Integer emp_id);

	@Query(value = "SELECT ed.emp_id as empId, ed.empcode as empcode, ed.employee_name as employeeName,ed.date_of_joining as DOJ, d.dept_name as departmentName," +
			" j.job_type as jobType, et.emp_type_short_name as employmentType," +
			"SUM(CASE WHEN lt.leave_type_short = 'CL' THEN la.no_of_days_applied ELSE 0 END) AS CL, " +
			"SUM(CASE WHEN lt.leave_type_short = 'EL' THEN la.no_of_days_applied ELSE 0 END) AS EL, " +
			"SUM(CASE WHEN lt.leave_type_short = 'VL' THEN la.no_of_days_applied ELSE 0 END) AS VL, " +
			"SUM(CASE WHEN lt.leave_type_short = 'ML' THEN la.no_of_days_applied ELSE 0 END) AS ML, " +
			"SUM(CASE WHEN lt.leave_type_short = 'PL' THEN la.no_of_days_applied ELSE 0 END) AS PL, " +
			"SUM(CASE WHEN lt.leave_type_short = 'WL' THEN la.no_of_days_applied ELSE 0 END) AS WL, " +
			"SUM(CASE WHEN lt.leave_type_short = 'MA' THEN la.no_of_days_applied ELSE 0 END) AS MA, " +
			"SUM(CASE WHEN lt.leave_type_short = 'GH' THEN la.no_of_days_applied ELSE 0 END) AS GH, " +
			"SUM(CASE WHEN lt.leave_type_short = 'DH' THEN la.no_of_days_applied ELSE 0 END) AS DH, " +
			"SUM(CASE WHEN lt.leave_type_short = 'SL' THEN la.no_of_days_applied ELSE 0 END) AS SL, " +
			"SUM(CASE WHEN lt.leave_type_short = 'OD' THEN la.no_of_days_applied ELSE 0 END) AS OD, " +
			"SUM(CASE WHEN lt.leave_type_short = 'SD' THEN la.no_of_days_applied ELSE 0 END) AS SD, " +
			"SUM(CASE WHEN lt.leave_type_short = 'VD' THEN la.no_of_days_applied ELSE 0 END) AS VD, " +
			"SUM(CASE WHEN lt.leave_type_short = 'ED' THEN la.no_of_days_applied ELSE 0 END) AS ED, " +
			"SUM(CASE WHEN lt.leave_type_short = 'CP' THEN la.no_of_days_applied ELSE 0 END) AS CP, " +
			"SUM(CASE WHEN lt.leave_type_short = 'AL' THEN la.no_of_days_applied ELSE 0 END) AS AL, " +
			"SUM(CASE WHEN lt.leave_type_short = 'PR' THEN la.no_of_days_applied ELSE 0 END) AS PR, " +
			"SUM(CASE WHEN lt.leave_type_short = 'RH' THEN la.no_of_days_applied ELSE 0 END) AS RH, " +
			"SUM(CASE WHEN lt.leave_type_short = 'UD' THEN la.no_of_days_applied ELSE 0 END) AS UD, " +
			"SUM(CASE WHEN lt.leave_type_short = 'SP' THEN la.no_of_days_applied ELSE 0 END) AS SP, " +
			"SUM(CASE WHEN lt.leave_type_short = 'RL' THEN la.no_of_days_applied ELSE 0 END) AS RL " +
			"FROM leave_apply la " +
			"LEFT JOIN employee_details ed ON ed.emp_id = la.emp_id " +
			"LEFT JOIN leave_type lt ON lt.leave_id = la.leave_id " +
			"LEFT JOIN schools sc ON sc.school_id = ed.school_id " +
			"LEFT JOIN department d ON d.dept_id = ed.dept_id " +
			"left join job_type j ON j.job_type_id = ed.job_type_id " +
			"left join employee_type et ON et.emp_type_id = ed.emp_type_id " +
			"WHERE la.year = :year and ed.school_id = :schoolId and la.active = 1 and la.approved_status = 2 and ed.emp_type_id != 3 " +
			"and (:deptId IS NULL OR ed.dept_id = :deptId) " +
			"and (:leaveId IS NULL OR la.leave_id = :leaveId) " +
			"GROUP BY la.emp_id " +
			"ORDER BY la.emp_id", nativeQuery = true)
    List<Map<String, Object>> getAllLeaves(Integer year, Integer schoolId, Integer deptId, Integer leaveId);


	@Query(value = "SELECT  " +
			"    lt.leave_type AS leaveType,  " +
			"    ed1.employee_name As employeeName,  " +
			"    ed1.empcode As empcode,  " +
			"    ed2.employee_name AS app_1,  " +
			"    ed3.employee_name AS app_2,  " +
			"    d.dept_name As deptName,   " +
			"    la.leave_apply_id AS leave_apply_id, " +
			"    la.employee_leave_id AS employee_leave_id, " +
			"    la.emp_id AS emp_id, " +
			"    la.no_of_days_applied AS no_of_days_applied, " +
			"    la.total_leaves_applicable AS total_leaves_applicable, " +
			"    la.remaining_days_left AS remaining_days_left, " +
			"    la.leave_comments AS leave_comments, " +
			"    la.reporting_approver_comment AS reporting_approver_comment, " +
			"    la.reporting_approver1_comment AS reporting_approver1_comment, " +
			"    la.leave_app1_status AS leave_app1_status, " +
			"    la.leave_app2_status AS leave_app2_status, " +
			"    la.alternative_lecturer_name AS alternative_lecturer_name, " +
			"    la.contact_no AS contact_no, " +
			"    la.year AS year, " +
			"    la.shift AS shift, " +
			"    la.special_leave_id AS special_leave_id, " +
			"    la.cancel_by AS cancel_by, " +
			"    la.cancel_date AS cancel_date, " +
			"    la.leave_approved_date AS leave_approved_date, " +
			"    la.compoff_worked_date AS compoff_worked_date, " +
			"    la.leave_approved2_date AS leave_approved2_date, " +
			"    la.leave_approved_by1 AS leave_approved_by1, " +
			"    la.leave_approved_by2 AS leave_approved_by2, " +
			"    la.leave_id AS leave_id, " +
			"    la.from_date AS from_date, " +
			"    la.to_date AS to_date, " +
			"    la.leave_apply_attachment_path AS leave_apply_attachment_path, " +
			"    la.leave_apply_attachment_path2 AS leave_apply_attachment_path2, " +
			"    la.dept_name_short AS dept_name_short, " +
			"    la.web_status AS web_status, " +
			"    la.created_date AS created_date, " +
			"    la.modified_date AS modified_date, " +
			"    la.created_by AS created_by, " +
			"    la.modified_by AS modified_by, " +
			"    la.active AS active, " +
			"    la.created_username AS created_username, " +
			"    la.modified_username AS modified_username, " +
			"    la.alternative_emails AS alternative_emails, " +
			"    la.cancel_comments AS cancel_comments, " +
			"    la.approved_status AS approved_status " +
			"FROM " +
			"    leave_apply la  " +
			"LEFT JOIN " +
			"    employee_details ed1 ON ed1.emp_id = la.emp_id  " +
			"LEFT JOIN " +
			"    employee_details ed2 ON ed2.emp_id = la.leave_approved_by1 " +
			"LEFT JOIN " +
			"    employee_details ed3 ON ed3.emp_id = la.leave_approved_by2 " +
			"LEFT JOIN " +
			"    department d ON d.dept_id = ed1.dept_id " +
			"LEFT JOIN " +
			"    leave_type lt ON lt.leave_id = la.leave_id  " +
			"WHERE " +
			"    la.leave_id = :leaveId " +
			"    AND ed1.emp_id = :empId " +
			"    AND la.year = :year  " +
			"    AND la.active = 1 " +
			"    AND la.approved_status = 2", nativeQuery = true)
    List<Map<String, String>> findAllByLeaveIdAndYearAndEmpId(Integer leaveId, Integer empId, Integer year);

	@Query(value = "SELECT COUNT(*) " +
			"FROM leave_apply la " +
			"WHERE year = :year AND emp_id = :empId AND approved_status != 3 AND active = 1  AND no_of_days_applied > 0.5 " +
			"AND STR_TO_DATE(:currentDate, '%d-%m-%Y') BETWEEN STR_TO_DATE(la.from_date, '%d-%m-%Y') AND STR_TO_DATE(la.to_date, '%d-%m-%Y') ",nativeQuery = true)
	Integer countOfLeavesAppliedOnTheDate(String currentDate, Integer empId, String year);

	@Query(value = "select * from leave_apply where from_date like :fromDate and emp_id = :empId and approved_status != 3 and active = 1", nativeQuery = true)
	LeaveApply getLeaveAppliedByFromDateAndEmpId(String fromDate, Integer empId);

	@Query(value = "select count(*) from leave_apply where from_date like :fromDate and emp_id = :empId and approved_status != 3 and active = 1", nativeQuery = true)
	int getCountOfLeavesAppliedOnTheGivenDate(String fromDate, Integer empId);

	@Query(value = "SELECT * FROM leave_apply la  WHERE "
			+ "        ( "
			+ "            STR_TO_DATE(:formattedDate, '%d-%m-%Y') BETWEEN STR_TO_DATE(la.from_date, '%d-%m-%Y') AND STR_TO_DATE(la.to_date, '%d-%m-%Y') "
			+ "        )  "
			+ "        AND la.emp_id =:empId  "
			+ "		   AND la.active = true "
			+ "        AND la.approved_status = 2 ",nativeQuery=true)
	List<LeaveApply> getAllLeavesByFromDateAndEmpId(String formattedDate, Integer empId);


	@Query(value = "select * from leave_apply la " +
			"where (STR_TO_DATE(:formattedDate, '%d-%m-%Y') BETWEEN STR_TO_DATE(la.from_date, '%Y-%m-%d') AND STR_TO_DATE(la.to_date, '%Y-%m-%d')) " +
			"and leave_id = :leaveId and emp_id = :empId and no_of_days_applied > 0.5 and active = 1 and approved_status != 3 ", nativeQuery = true)
	LeaveApply getLeaveByFromDateAndEmpIdAndLeaveId(String formattedDate, Integer empId, Integer leaveId);

	@Query(value ="SELECT * FROM leave_apply la " +
			"WHERE la.leave_id = 4 " +
			"  AND ( " +
			"    (:year is Null OR YEAR(STR_TO_DATE(la.from_date, '%d-%m-%Y')) < :year) OR " +
			"    ((:year is Null OR YEAR(STR_TO_DATE(la.from_date, '%d-%m-%Y')) = :year) AND " +
			"     (:month is Null OR MONTH(STR_TO_DATE(la.from_date, '%d-%m-%Y')) <= :month)) " +
			"    AND " +
			"    (:year is Null OR YEAR(STR_TO_DATE(la.to_date, '%d-%m-%Y')) > :year) OR " +
			"    ((:year is Null OR YEAR(STR_TO_DATE(la.to_date, '%d-%m-%Y')) = :year)  AND " +
			"	  (:month is Null OR MONTH(STR_TO_DATE(la.to_date, '%d-%m-%Y')) <= :month)) " +
			"  )", nativeQuery = true)
	List<Map<String, Object>> maternityLeaveAppliedEmp(Integer month, Integer year);
}
