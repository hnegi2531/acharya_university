package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.VisaExpiryHistory;


@Transactional
@Repository
public interface VisaExpiryHistoryRepository extends JpaRepository<VisaExpiryHistory, Integer>{

	@Query(value = "SELECT veh from VisaExpiryHistory veh where veh.active=true")
	public List<VisaExpiryHistory> findAll11();
	
	
	@Modifying
	@Query(value = "update VisaExpiryHistory veh set veh.active=false where veh.visa_expiry_history_id=?1")
	public void updateHistory(Integer id);

	@Modifying
	@Query(value = "update VisaExpiryHistory veh set veh.active=true where veh.visa_expiry_history_id=?1")
	public void updatehistory1(Integer id);	
	
	@Query(value = "select new map(veh.visa_expiry_history_id as id,veh.emp_id as emp_id,veh.visa_expiry_date as visa_expiry_date,"
			+ "veh.visa_document_number as visa_document_number,ed.employee_name as employee_name,ed.mobile as mobile,ed.email as email,"
			+ "veh.created_username as created_username,veh.modified_username as modified_username,veh.remarks as remarks,"
			+ "veh.created_date as created_date,veh.modified_date as modified_date,veh.created_by as created_by,"
			+ "veh.modified_by as modified_by,veh.active as active) from VisaExpiryHistory veh "
			+ "Left Join EmployeeDetails ed On ed.emp_id = veh.emp_id "
			+ "where CONCAT(IfNull(veh.created_username,''),'',IfNull(veh.emp_id,''),'',IfNull(veh.visa_expiry_date,''),'',"
			+ "IfNull(veh.created_by,''),'',IfNull(ed.employee_name,'',IfNull(veh.visa_document_number,''),'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	
	
	@Query(value = "select new map(veh.visa_expiry_history_id as id,veh.emp_id as emp_id,veh.visa_expiry_date as visa_expiry_date,"
			+ "veh.visa_document_number as visa_document_number,ed.employee_name as employee_name,ed.mobile as mobile,ed.email as email,"
			+ "veh.created_username as created_username,veh.modified_username as modified_username,veh.remarks as remarks,"
			+ "veh.created_date as created_date,veh.modified_date as modified_date,veh.created_by as created_by,"
			+ "veh.modified_by as modified_by,veh.active as active) from VisaExpiryHistory veh "
			+ "Left Join EmployeeDetails ed On ed.emp_id = veh.emp_id")
	public Page<Object> getAllSortedData(Pageable pageable);


	@Query(value = "select new map(veh.visa_expiry_history_id as id,veh.emp_id as emp_id,veh.visa_expiry_date as visa_expiry_date,"
			+ "veh.visa_document_number as visa_document_number,ed.employee_name as employee_name,ed.mobile as mobile,ed.email as email,"
			+ "veh.created_username as created_username,veh.modified_username as modified_username,veh.remarks as remarks,"
			+ "veh.created_date as created_date,veh.modified_date as modified_date,veh.created_by as created_by,"
			+ "veh.modified_by as modified_by,veh.active as active) from VisaExpiryHistory veh "
			+ "Left Join EmployeeDetails ed On ed.emp_id = veh.emp_id "
			+ "where veh.emp_id=?1 ")
	public List<HashMap<String, Object>> getVisaExpiryHistoryData(Integer empId);		
}
