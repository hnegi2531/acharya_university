package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.dto.EmployeeIdCardHistoryDetailsDto;
import com.au.model.EmployeeDetails;
import com.au.model.EmployeeIdCardHistory;

@Repository
@Transactional
public interface EmployeeIdCardHistoryRepository extends JpaRepository<EmployeeIdCardHistory,Integer>{

	int countByEmpIdAndActiveTrue(EmployeeDetails employeeDetails);
	
	@Query(value="select new com.au.dto.EmployeeIdCardHistoryDetailsDto(eich.employeeIdCardHistoryId, ed.emp_id, eich.issuedBy, eich.issuedByUsername, eich.issuedDate, eich.active,"
			+ " eich.remarks, eich.receiptDate, eich.receiptNo, ed.employee_name,eich.modifiedUsername as modifiedUsername,eich.modifiedDate as modifiedDate,"
			+ "ed.empcode as empCode,ed.to_date as endDate,ed.emp_image_attachment_path as empImageAttachmentPath, ed.date_of_joining as dateOfJoining,"
			+ "des.designation_name as designationName,des.designation_short_name as designationShortName,d.dept_name as departmentName,d.dept_name_short as departmentNameShort,"
			+ "sch.school_name as schoolName,sch.school_name_short as schoolNameShort,sch.display_name as displayName,ed.phd_status as phdStatus,ed.email as email,ed.mobile as mobile) From EmployeeIdCardHistory eich "
			+ "Left Join EmployeeDetails ed On ed.emp_id=eich.empId "
			+ "left join Schools sch on sch.school_id=ed.school_id "
			+ "left join Department d on ed.dept_id=d.dept_id "
			+ "left join Designation des on ed.designation_id=des.designation_id Where eich.active=true")
	List<EmployeeIdCardHistoryDetailsDto> employeeIdCardHistoryDetails();

	EmployeeIdCardHistory findByEmpId(EmployeeDetails employeeDetail);

}
