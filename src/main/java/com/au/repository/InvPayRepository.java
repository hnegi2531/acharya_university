package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.InvPay;



@Repository
public interface InvPayRepository  extends JpaRepository<InvPay, Long>{
	
	Boolean existsByEmpCodeAndMonthAndYearAndType(String empCode,Integer month, Integer year, String type);

	@Query(value="select inv from InvPay inv ")
	List<InvPay> getInvPayData();

//	@Query(value = "SELECT ip.inv_pay_id as id, ip.emp_code as emp_code, ep.employee_name as employee_name, ip.month as month, ip.year as year,"
//			+ "d.designation_name as designation_name, ip.remarks as remarks, ip.inv_pay as amount,ip.created_at as created_date,ua.username as createdUserName "
//			+ "FROM inv_pay ip "
//			+ "LEFT JOIN employee_details ep ON ep.empcode = ip.emp_code "
//			+ "LEFT JOIN user_details ua ON ua.id = ip.created_by "
//			+ "LEFT JOIN designation d ON ep.designation_id = d.designation_id "
//			+ "WHERE CONCAT(IFNULL(ip.emp_code,' '),' ', IFNULL(ip.employee_name,' '),' ', IFNULL(ip.month,' '),' ', IFNULL(ip.year,' '),' ', IFNULL(d.designation_name,' ')) LIKE %?1% ", nativeQuery=true)
//	public List<Map<String, Object>> getAllInvPayDataFilteredByKeyword(Pageable pageable, Object keyword);
//	
//	@Query(value = "SELECT ip.inv_pay_id as id, ip.emp_code as emp_code, ep.employee_name as employee_name, ip.month as month, ip.year as year,"
//			+ "d.designation_name as designation_name, ip.remarks as remarks, ip.inv_pay as amount,ip.created_at as created_date,ua.username as createdUserName "
//			+ "FROM inv_pay ip "
//			+ "LEFT JOIN employee_details ep ON ep.empcode = ip.emp_code "
//			+ "LEFT JOIN user_details ua ON ua.id = ip.created_by "
//			+ "LEFT JOIN designation d ON ep.designation_id = d.designation_id ", nativeQuery=true)
//		public List<Map<String, Object>> getAllInvPaySortedData(Pageable pageable);
	
	@Query(value = "SELECT new map(ip.invPayId as id, ip.empCode as emp_code, ep.employee_name as employee_name, ip.month as month, ip.year as year, ip.type as type, " 
			+ "d.designation_name as designation_name, ip.remarks as remarks, ip.invPay as amount,ip.createdAt as createdDate,ua.username as createdUserName) " +
	        "FROM InvPay ip " +
	        "LEFT JOIN EmployeeDetails ep ON ep.empcode = ip.empCode " +
	        "LEFT JOIN Designation d ON ep.designation_id = d.designation_id "
	        + " LEFT JOIN UserAuthentication ua ON ua.id = ip.createdBy " +
	        "WHERE CONCAT(IFNULL(ip.empCode,' '),' ', IFNULL(ip.employeeName,' '),' ', IFNULL(ip.month,' '),' ', IFNULL(ip.year,' '),' ', IFNULL(d.designation_name,' ')) LIKE %?1% ")
	public Page<Object> getAllInvPayDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value = "SELECT new map(ip.invPayId as id, ip.empCode as emp_code, ep.employee_name as employee_name, ip.month as month, ip.year as year, ip.type as type, " 
			+ "d.designation_name as designation_name, ip.remarks as remarks, ip.invPay as amount,ip.createdAt as createdDate,ua.username as createdUserName) " +
	        "FROM InvPay ip " +
	        " LEFT JOIN EmployeeDetails ep ON ep.empcode = ip.empCode"
	        + " LEFT JOIN UserAuthentication ua ON ua.id = ip.createdBy " +
	        "LEFT JOIN Designation d ON ep.designation_id = d.designation_id ")
		public Page<Object> getAllInvPaySortedData(Pageable pageable);


	InvPay findByEmpCodeAndMonthAndYear(String empCode,Integer month, Integer year);
   
	@Query(value="select i from InvPay i where i.empCode=:empCode and i.month=:month and i.year=:year  ", nativeQuery = false)
	List<InvPay> getByEmpCodeAndMonthAndYear(String empCode,Integer month, Integer year);

	@Query(value="Select CAST(sum(IfNull(ip.inv_pay,0)) AS DECIMAL(15,2)) as totalExtraRemuneration, ip.type as type From inv_pay ip Where ip.is_active=true group by ip.type", nativeQuery = true)
	List<Map<String, Object>> totalExtraRemunerationByMonthAndYear(Integer month, Integer year);
}
