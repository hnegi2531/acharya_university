package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.au.dto.IncrementCreationResponseDTO;
import com.au.model.TemporaryIncrementCreation;



public interface TemporaryIncrementCreationRepository extends JpaRepository<TemporaryIncrementCreation, Long>{

	@Query(value="select new com.au.dto.IncrementCreationResponseDTO("
	        + " ic.incrementCreationId, ic.empId, e.empcode, ic.previousDesignation, ic.proposedDesignation, "
	        + " ic.previousDesignationId, ic.previousDepartment, ic.proposedDepartment, ic.previousDepartmentId,"
	        + " ic.previousSalaryStructure, ic.proposedSalaryStructure, ic.previousSalaryStructureId,"
	        + " ic.previousBasic, ic.proposedBasic, ic.previousSplPay, ic.proposedSplPay, ic.previousGrosspay, ic.proposedGrosspay,"
	        + " ic.previousCtc, ic.proposedCtc, ic.grossDifference, ic.ctcDifference, ic.month, ic.year,"
	        + " ic.proposedDesignationId, ic.proposedDepartmentId, ic.proposedSalaryStructureId, ic.previousMedicalReimburesment, ic.proposedMedicalReimburesment,"
	        + " ic.created_date, ic.remarks, u.username, e.date_of_joining, e.employee_name, ic.isFinalize) from TemporaryIncrementCreation ic left join"
	        + " EmployeeDetails e on e.emp_id = ic.empId left join UserAuthentication u on u.id = ic.createdBy where ic.batchId=(select max(ic2.batchId) from TemporaryIncrementCreation ic2)")
	List<IncrementCreationResponseDTO> getTemporaryIncrementCreationList();

}
