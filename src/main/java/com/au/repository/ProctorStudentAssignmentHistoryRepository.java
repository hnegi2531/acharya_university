package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ProctorStudentAssignmentHistory;


@Transactional
@Repository
public interface ProctorStudentAssignmentHistoryRepository extends JpaRepository<ProctorStudentAssignmentHistory, Integer> {


	@Query(value = "Select new map(p.proctor_assign_history_id as id,p.proctor_assign_id as proctor_assign_id,sd.student_name as student_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,sd.student_email as student_email,sd.usn as usn,"
			+ "sd.auid as auid,p.created_date as created_date,p.created_by as created_by,p.student_id as student_id,"
			+ "p.created_username as created_username,p.active as active,p.school_id as school_id,"
			+ "p.proctor_status as proctor_status,p.emp_id as emp_id,sd.proctor_assign_status as proctor_assign_status) from ProctorStudentAssignmentHistory p "
			+ "left join EmployeeDetails ed on p.emp_id=ed.emp_id "
			+ "left join Schools sc on sc.school_id=p.school_id "
			+ "left join UserAuthentication ua on ua.email=ed.email "
			+ "left join Student_Details sd on p.student_id=sd.student_id "
			+ "where (:userId IS NULL OR ua.id = :userId) "
			+ "AND (:school_id IS NULL OR p.school_id = :school_id) "
			+ "And CONCAT(IfNull(p.proctor_assign_id,''),'',IfNull(sd.student_name,''),'',IfNull(ed.employee_name,''),'',IfNull(ed.empcode,''),"
			+ "'',IfNull(sd.student_email,''),'',IfNull(sd.usn,''),'',IfNull(sd.auid,''),'',IfNull(p.proctor_status,''),"
			+ "'',IfNull(p.emp_id,''),'',IfNull(p.school_id,''),'',IfNull(p.student_id,''),'',IfNull(p.created_by,''),"
			+ "'',IfNull(p.created_date,'')) LIKE %:keyword%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer userId, Integer school_id); 
	
	@Query(value = "Select new map(p.proctor_assign_history_id as id,p.proctor_assign_id as proctor_assign_id,sd.student_name as student_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,sd.student_email as student_email,sd.usn as usn,"
			+ "sd.auid as auid,p.created_date as created_date,p.created_by as created_by,p.student_id as student_id,"
			+ "p.created_username as created_username,p.active as active,p.school_id as school_id,"
			+ "p.proctor_status as proctor_status,p.emp_id as emp_id,sd.proctor_assign_status as proctor_assign_status) from ProctorStudentAssignmentHistory p "
			+ "left join EmployeeDetails ed on p.emp_id=ed.emp_id "
			+ "left join Schools sc on sc.school_id=p.school_id "
			+ "left join UserAuthentication ua on ua.email=ed.email "
			+ "left join Student_Details sd on p.student_id=sd.student_id "
			+ "where (:userId IS NULL OR ua.id = :userId) "
			+ "AND (:school_id IS NULL OR p.school_id = :school_id) ")
	public Page<Object> getAllSortedData(Pageable pageable, Integer userId, Integer school_id);
	
	
	@Query(value = "Select new map(p.proctor_assign_history_id as id,p.proctor_assign_id as proctor_assign_id,sd.student_name as student_name,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,sd.student_email as student_email,sd.usn as usn,"
			+ "sd.auid as auid,p.created_date as created_date,p.created_by as created_by,p.student_id as student_id,"
			+ "p.created_username as created_username,p.active as active,p.school_id as school_id,"
			+ "p.proctor_status as proctor_status,p.emp_id as emp_id,sd.proctor_assign_status as proctor_assign_status) from ProctorStudentAssignmentHistory p "
			+ "left join EmployeeDetails ed on p.emp_id=ed.emp_id "
			+ "left join Student_Details sd on p.student_id=sd.student_id Where p.emp_id=?1")
	public List<HashMap<String, Object>> proctorStudentAssignmentHistoryDetailByEmployeeId(Integer empId);

}
