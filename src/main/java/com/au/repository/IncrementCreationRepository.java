package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.EmployeeListForIncrementCreationResponseDTO;
import com.au.dto.EmployeeListForIncrementCreationrequestDTO;
import com.au.dto.IncrementCreationResponseDTO;
import com.au.model.IncrementCreation;



@Transactional
@Repository
public interface IncrementCreationRepository extends JpaRepository<IncrementCreation, Long> {

	@Query(value="select new com.au.dto.EmployeeListForIncrementCreationResponseDTO( e.emp_id as empId, "
	        + "e.employee_name as employeeName, d.designation_name as designation, de.dept_name as department, e.email as email, e.date_of_joining as dateofJoining, e.empcode as empCode, "
	        + "e.ctc as ctc, e.grosspay_ctc as grosspay_ctc, sc.school_name as schoolName, sc.school_name_short as schoolShortName, "
	        + "e.permanent_status as permanent_status , et.empTypeShortName As empTypeShortName,de.dept_name_short As dept_name_short) "
	        + "from EmployeeDetails e left join Designation d on d.designation_id=e.designation_id "
	        + "left join Department de on de.dept_id=e.dept_id "
	        + "left join EmployeeType et on et.empTypeId=e.emp_type_id "
	        + "left join Schools sc on sc.school_id=e.school_id "
	        + "where e.active=1 "
	        + "and (e.employee_name like %:#{#request.employeeName}% or :#{#request.employeeName} is null) "
	        + "and (d.designation_name like %:#{#request.designation}% or :#{#request.designation} is null) "
	        + "and (de.dept_name like %:#{#request.department}% or :#{#request.department} is null) "
	        + "and (e.email like %:#{#request.email}% or :#{#request.email} is null) "
	        + "and (e.date_of_joining = :#{#request.dateofJoining} or :#{#request.dateofJoining} is null)"
	        + "and (e.empcode = :#{#request.empCode} or :#{#request.empCode} is null) ORDER BY e.empcode DESC")
	List<EmployeeListForIncrementCreationResponseDTO> getEmployeeListForIncrementCreation(
	        @Param("request") EmployeeListForIncrementCreationrequestDTO employeeListForIncrementCreationRequestDTO);
	
	
	@Query(value="select new com.au.dto.EmployeeListForIncrementCreationResponseDTO( e.emp_id as empId, "
	        + "e.employee_name as employeeName, d.designation_name as designation, de.dept_name as department, e.email as email, e.date_of_joining as dateofJoining, e.empcode as empCode, "
	        + "e.ctc as ctc, e.grosspay_ctc as grosspay_ctc, sc.school_name as schoolName, sc.school_name_short as schoolShortName, "
	        + "e.permanent_status as permanent_status , et.empTypeShortName As empTypeShortName,de.dept_name_short As dept_name_short) "
	        + "from EmployeeDetails e left join Designation d on d.designation_id=e.designation_id "
	        + "left join Department de on de.dept_id=e.dept_id "
	        + "left join EmployeeType et on et.empTypeId=e.emp_type_id "
	        + "left join Schools sc on sc.school_id=e.school_id "
	        + "where e.active=1 And e.emp_type_id=1 "
	        + "and (e.employee_name like %:#{#request.employeeName}% or :#{#request.employeeName} is null) "
	        + "and (d.designation_name like %:#{#request.designation}% or :#{#request.designation} is null) "
	        + "and (de.dept_name like %:#{#request.department}% or :#{#request.department} is null) "
	        + "and (e.email like %:#{#request.email}% or :#{#request.email} is null) "
	        + "and (e.date_of_joining = :#{#request.dateofJoining} or :#{#request.dateofJoining} is null)"
	        + "and (e.empcode = :#{#request.empCode} or :#{#request.empCode} is null) ORDER BY e.empcode DESC")
	List<EmployeeListForIncrementCreationResponseDTO> getEmployeeOrrListForIncrementCreation(
	        @Param("request") EmployeeListForIncrementCreationrequestDTO employeeListForIncrementCreationRequestDTO);
	

	@Query(value="select new com.au.dto.IncrementCreationResponseDTO("
	        + " ic.incrementCreationId, ic.empId, e.empcode, ic.previousDesignation, ic.proposedDesignation, "
	        + " ic.previousDesignationId, ic.previousDepartment, ic.proposedDepartment, ic.previousDepartmentId,"
	        + " ic.previousSalaryStructure, ic.proposedSalaryStructure, ic.previousSalaryStructureId,"
	        + " ic.previousBasic, ic.proposedBasic, ic.previousSplPay, ic.proposedSplPay, ic.previousGrosspay, ic.proposedGrosspay,"
	        + " ic.previousCtc, ic.proposedCtc, ic.grossDifference, ic.ctcDifference, ic.month, ic.year,"
	        + " ic.proposedDesignationId, ic.proposedDepartmentId, ic.proposedSalaryStructureId, ic.previousMedicalReimburesment, ic.proposedMedicalReimburesment,"
	        + " ic.created_date, ic.remarks, u.username, e.date_of_joining, e.employee_name,ic.isFinalize, ic.isApproved, ic.isRejected,"
	        + " ic.attachmentPath, ic.attachmentPathType,ic.active,e.school_id,e.dept_id,sc.school_name_short) "
	        + " from IncrementCreation ic "
	        + "left join EmployeeDetails e on e.emp_id = ic.empId "
	        + "left join Schools sc on e.school_id = sc.school_id "
	        + "left join UserAuthentication u on u.id = ic.createdBy "
	        + "where (:school_id is null or e.school_id = :school_id) "
			+ "And (:dept_id is null or e.dept_id = :dept_id) "
			+ "And (:month is null or ic.month = :month) And ic.active=true "
	        + "order by ic.created_date desc ")
	List<IncrementCreationResponseDTO> getIncrementCreationList(Integer school_id, Integer dept_id, Integer month);

	IncrementCreation findByIncrementCreationId(Long incrementCreationId);

	@Query(value="select new com.au.dto.IncrementCreationResponseDTO("
	        + " ic.incrementCreationId, ic.empId, e.empcode, ic.previousDesignation, ic.proposedDesignation, "
	        + " ic.previousDesignationId, ic.previousDepartment, ic.proposedDepartment, ic.previousDepartmentId,"
	        + " ic.previousSalaryStructure, ic.proposedSalaryStructure, ic.previousSalaryStructureId,"
	        + " ic.previousBasic, ic.proposedBasic, ic.previousSplPay, ic.proposedSplPay, ic.previousGrosspay, ic.proposedGrosspay,"
	        + " ic.previousCtc, ic.proposedCtc, ic.grossDifference, ic.ctcDifference, ic.month, ic.year,"
	        + " ic.proposedDesignationId, ic.proposedDepartmentId, ic.proposedSalaryStructureId, ic.previousMedicalReimburesment, ic.proposedMedicalReimburesment,"
	        + " ic.created_date, ic.remarks, u.username, e.date_of_joining, e.employee_name,ic.isFinalize, ic.isApproved, ic.isRejected,"
	        + " ic.attachmentPath, ic.attachmentPathType,ic.active,e.school_id,e.dept_id,sc.school_name_short) from IncrementCreation ic "
	        + "left join EmployeeDetails e on e.emp_id = ic.empId "
	        + "left join Schools sc on e.school_id = sc.school_id "
	        + "left join UserAuthentication u on u.id = ic.createdBy "
	        + "where (:school_id is null or e.school_id = :school_id) "
			+ "And (:dept_id is null or e.dept_id = :dept_id) "
			+ "And (:month is null or ic.month = :month) "
	        + "And ic.isFinalize=1 order by ic.created_date desc")
	List<IncrementCreationResponseDTO> getIncrementFinalizeList(Integer school_id, Integer dept_id, Integer month);
	
	@Query(value="select new com.au.dto.IncrementCreationResponseDTO("
	        + " ic.incrementCreationId, ic.empId, e.empcode, ic.previousDesignation, ic.proposedDesignation, "
	        + " ic.previousDesignationId, ic.previousDepartment, ic.proposedDepartment, ic.previousDepartmentId,"
	        + " ic.previousSalaryStructure, ic.proposedSalaryStructure, ic.previousSalaryStructureId,"
	        + " ic.previousBasic, ic.proposedBasic, ic.previousSplPay, ic.proposedSplPay, ic.previousGrosspay, ic.proposedGrosspay,"
	        + " ic.previousCtc, ic.proposedCtc, ic.grossDifference, ic.ctcDifference, ic.month, ic.year,"
	        + " ic.proposedDesignationId, ic.proposedDepartmentId, ic.proposedSalaryStructureId, ic.previousMedicalReimburesment, ic.proposedMedicalReimburesment,"
	        + " ic.created_date, ic.remarks, u.username, e.date_of_joining, e.employee_name,ic.isFinalize,"
	        + " ic.isApproved, ic.isRejected, ic.attachmentPath, ic.attachmentPathType,ic.active,e.school_id,e.dept_id,sc.school_name_short) from IncrementCreation ic "
	        + "left join EmployeeDetails e on e.emp_id = ic.empId "
	        + "left join Schools sc on e.school_id = sc.school_id "
	        + "left join UserAuthentication u on u.id = ic.createdBy "
	    	+ "where (:school_id is null or e.school_id = :school_id) "
			+ "And (:dept_id is null or e.dept_id = :dept_id) "
			+ "And (:month is null or ic.month = :month) "
			+ "And ic.isApproved=1 order by ic.created_date desc")
	List<IncrementCreationResponseDTO> getIncrementApprovedList(Integer school_id, Integer dept_id, Integer month);

	@Query(value=" select ic from IncrementCreation ic where ic.incrementCreationId in (:ids)")
	List<IncrementCreation> getAllInIncrementIds(@Param("ids") List<Long> incrementIds);

	@Query(value=" select ic from IncrementCreation ic where ic.incrementCreationId=?1")
	IncrementCreation getDataByid(Long incrementCreationId);

	@Query(value=" select count(*) from IncrementCreation ic where ic.empId=?1 and ic.month=?2 And ic.active=true")
	public Integer count(Integer empId, Integer month);

	
	@Query(value=" select ic from IncrementCreation ic where ic.incrementCreationId=?1")
	IncrementCreation getDataByid1(Long incrementCreationId);

	@Modifying
	@Query(value = "update IncrementCreation ic set ic.active=false where ic.incrementCreationId=?1")
	public void deactivate(Long incrementCreationId);

	@Modifying
	@Query(value = "update IncrementCreation ic set ic.active=true where ic.incrementCreationId=?1")
	public void activate(Long incrementCreationId);

	@Query(value = "select * from increment_creation where is_approved = 1 and is_finalize = 1 " +
			"and month = :month and year = :year ", nativeQuery = true)
	List<IncrementCreation> getAllIncrementsOfMonthYear(Integer month, Integer year);

	@Query(value = "select emp_id from increment_creation where is_approved = 1 and is_finalize = 1 " +
			"and month = :month and year = :year ", nativeQuery = true)
	List<Integer> getAllEmployeeIds(Integer month, Integer year);

	@Query(value = "select emp_id from increment_creation where is_approved = 1 and is_finalize = 1 " +
			"and emp_id = :empId and month = :month and year = :year", nativeQuery = true)
	IncrementCreation findByEmpIdMonthAndYear(Integer empId, Integer month, Integer year);
}
