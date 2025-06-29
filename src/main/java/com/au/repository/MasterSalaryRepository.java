package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.EmployeePayHistoryDTO;
import com.au.dto.MasterSalaryHistoryDTO;
import com.au.dto.PaySlipDetails;
import com.au.model.MasterSalary;

@Repository
public interface MasterSalaryRepository extends JpaRepository<MasterSalary, Integer>{

	@Query(value=" select * from master_salary eph where eph.emp_id=?1 and eph.month=?2 and eph.year=?3",nativeQuery=true)
	MasterSalary findByEmpIdAndMonthAndYear(Integer empId, Integer month, Integer year);

	@Query(value=" select new com.au.dto.EmployeePayHistoryDTO( eph.emp_pay_history_id as id,"
	        + " e.empcode as empcode, e.employee_name as employee_name, dep.dept_name as dept_name, des.designation_name as designation_name, jt.job_type as job_type,"
	        + " et.empType as employee_type, tss.salary_structure as salary_structure, e.date_of_joining as date_of_joining, eph.pay_days as pay_days, e.grosspay_ctc as master_salary, eph.basic as basic,"
	        + " eph.er as er, eph.total_earning as total_earning, eph.tax as tax, eph.total_deduction as total_deduction, eph.pf as pf, eph.net_pay as netpay, eph.advance as advance, eph.pTax as pTax, eph.remarks as remarks, e.pinfl as pinfl,eph.month as month, eph.year as year,"
	        + " eph.hra as hra, eph.da as da, eph.cca as cca, eph.ta as ta, eph.mr as mr, eph.fr as fr, eph.other_allow as other_allow, eph.spl_1 as spl_1, eph.gross_pay as gross_pay,"
	        + " eph.pt as pt, eph.esi as esi, eph.tds as tds, eph.advance1 as advance1, eph.advance2 as advance2, eph.net_pay as net_pay, eph.pf_account_no as pf_account_no, eph.pf_earnings as pf_earnings,"
	        + " eph.contribution_epf as contribution_epf, eph.epf_difference as epf_difference, eph.pension_fund as pension_fund, eph.esi_earnings as esi_earnings, eph.esi_contribution_employee as esi_contribution_employee, sc.school_name_short as schoolShortName,dep.dept_name_short as departmentShortName,des.designation_short_name as  designationShortName, eph.lic as lic,e.gender as gender ) " 
	        + "from MasterSalary eph "
	        + "left join EmployeeDetails e on e.emp_id=eph.emp_id "
	        + "left join Department dep on dep.dept_id=eph.dept_id "
	        + "left join Designation des on des.designation_id=e.designation_id "
	        + "left join Schools sc on sc.school_id=e.school_id "
	        + "left join JobType jt on jt.job_type_id=e.job_type_id "
	        + "left join EmployeeType et on et.empTypeId=e.emp_type_id "
	        + "left join SalaryStructureDetails tsd on tsd.salary_structure_id=e.salary_structure_id "
	        + "left join SalaryStructure tss on tss.salary_structure_id=tsd.salary_structure_id "
	        + "WHERE "
	        + "(:schoolId IS NULL OR eph.school_id = :schoolId) "
	        + "AND (:deptId IS NULL OR eph.dept_id = :deptId) "
	        + "AND (:month IS NULL OR eph.month = :month) "
	        + "AND (:year IS NULL OR eph.year = :year) "
	        + "AND (:keyword IS NULL OR "
	        + "LOWER(eph.school_id) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
	        + "LOWER(eph.dept_id) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
	        + "LOWER(eph.year) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
	        + "LOWER(eph.month) LIKE LOWER(CONCAT('%', :keyword, '%')) ) group by e.empcode", nativeQuery=false)
	Page<EmployeePayHistoryDTO> getEmployeePayHistoryData(Integer schoolId, Integer deptId, Integer month,
			Integer year, Object keyword, Pageable pageable);

	
	@Query(value = "SELECT new com.au.dto.MasterSalaryHistoryDTO(" +
            "e.empcode as empCode, e.employee_name as employeeName, " +
            "e.date_of_joining as dateOfJoining, d.dept_name as departmentName, de.designation_name as designationName, " +
            "eph.pay_days as payDays, e.bank_branch as bankName, e.bank_account_no as accountNo, " +
            "e.mfo as mfo, eph.basic as basic, eph.hra as hra, eph.da as da, eph.cca as cca, eph.ta as ta, " +
            "eph.mr as mr, eph.fr as fr, eph.other_allow as otherAllow, eph.spl_1 as spl1, eph.gross_pay as grossPay, " +
            "eph.pf as pf, eph.pTax as pt, eph.esi as esi, eph.tds as tds, eph.advance1 as advance1, eph.advance2 as advance2, " +
            "eph.total_earning as totalEarning, eph.total_deduction as totalDeduction, eph.net_pay as netPay, " +
            "eph.pf_account_no as pfAccountNo, eph.pf_earnings as pfEarnings, eph.contribution_epf as contributionEpf, " +
            "eph.epf_difference as epfDifference, eph.pension_fund as pensionFund, eph.esi_earnings as esiEarnings, " +
            "eph.esi_contribution_employee as esiContributionEmployee, e.passportno as passportNo, " +
            "eph.er as er, eph.tax as tax, e.pinfl as pinfl, eph.month as month, eph.year as year, eph.advance as advance, " +
            "eph.remarks as remarks, sc.school_name as schoolName) " +
            "FROM MasterSalary eph " +
            "LEFT JOIN EmployeeDetails e ON e.emp_id = eph.emp_id " +
            "LEFT JOIN Schools sc ON sc.school_id = e.school_id " +
            "LEFT JOIN Department d ON d.dept_id = e.dept_id " +
            "LEFT JOIN Designation de ON de.designation_id = e.designation_id " +
            "WHERE eph.emp_pay_history_id = :emp_pay_history_id group by e.empcode", nativeQuery = false)
   MasterSalaryHistoryDTO getMasterPaySlipDetails(@Param("emp_pay_history_id") Integer empPayHistoryId);
	
  
	
	
}
