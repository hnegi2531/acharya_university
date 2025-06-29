package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.EmployeeContractsAttachment;

@Transactional
@Repository
public interface EmployeeContractsRepo extends JpaRepository<EmployeeContractsAttachment, Long>{

	List<EmployeeContractsAttachment> findByEmployeeId(Integer empId);

	@Query(value="SELECT * FROM employee_official_contracts_attachment eca where eca.document_type='PROFESSIONAL_EXPERIENCE' and eca.employee_id=?1 And eca.active=true", nativeQuery = true)
	List<EmployeeContractsAttachment> findByEmployeeIdAndDocumentType(Integer emp_id);

	@Modifying
	@Query(value = "update EmployeeContractsAttachment eia set eia.active=false where eia.id=?1")
	public void employeeWorkExperienceDeactivate(Long id);
	
	@Query(value = "SELECT eia FROM EmployeeContractsAttachment eia where eia.active=true And eia.employeeId=?1")
	public List<EmployeeContractsAttachment> getEmpContractsByEmpId(Integer emp_id);

}
