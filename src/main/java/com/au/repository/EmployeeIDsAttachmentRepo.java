package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.EmployeeIDsAttachment;

@Transactional
@Repository
public interface EmployeeIDsAttachmentRepo extends JpaRepository<EmployeeIDsAttachment, Long>{

	EmployeeIDsAttachment findByEmployeeIdAndDocumentType(Integer empId, String documentType);


	@Query(value = "SELECT eia FROM EmployeeIDsAttachment eia where eia.active=true And eia.employeeId=?1")
	List<EmployeeIDsAttachment> getEmployeeIDsAttachmentDetails(Integer emp_id);


	@Modifying
	@Query(value = "update EmployeeIDsAttachment eia set eia.active=false where eia.id=?1")
	public void updateEmployeeIDsAttachment(Long id);

	
	
}
