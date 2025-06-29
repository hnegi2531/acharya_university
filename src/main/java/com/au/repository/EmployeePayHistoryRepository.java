package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.EmployeePayHistoryDTO;
import com.au.dto.PaySlipDetails;
import com.au.model.EmployeePayHistory;



@Transactional
@Repository
public interface EmployeePayHistoryRepository extends JpaRepository<EmployeePayHistory, Integer>{

	@Query(value=" select * from emp_pay_history eph where eph.emp_id=?1 and eph.month=?2 and eph.year=?3",nativeQuery=true)
	EmployeePayHistory findByEmpIdAndMonthAndYear(Integer emp_id,Integer month, Integer year);
	
	@Query(value=" select  eph.emp_pay_history_id as id,eph.advance1 as advance1,eph.advance2 as advance2,eph.emp_id as emp_id,eph.pay_days as pay_days,"
			+ "eph.inv_pay as inv_pay,eph.basic as basic,eph.hra as hra,eph.da as da,eph.cca as cca,eph.mr as mr,"
			+ "eph.fr as fr,eph.ta as ta,eph.other_allow as other_allow,eph.spl_1 as spl_1,eph.gross_pay as gross_pay,eph.pf as pf,eph.pt as pt,"
			+ "eph.esi as esi,eph.tds as tds,eph.total_earning as total_earning,eph.total_deduction as total_deduction,eph.net_pay as net_pay,"
			+ "eph.pf_account_no as pf_account_no,eph.pf_earnings as pf_earnings,eph.contribution_epf as contribution_epf,eph.epf_difference as epf_difference,"
			+ "eph.pension_fund as pension_fund,eph.esi_earnings as esi_earnings,eph.esi_contribution_employee as esi_contribution_employee,"
			+ "eph.year as year,eph.month as month,eph.created_by as created_by,eph.modified_by as modified_by,eph.fromdate as fromdate,"
			+ "eph.todate as todate,eph.dept_id as dept_id,eph.school_id as school_id,eph.bank as bank,eph.transport as transport,"
			+ "eph.salary_approve_status as salary_approve_status,eph.salaray_block_date as salaray_block_date,eph.new_join_status as new_join_status,"
			+ "eph.bank_account_no as bank_account_no,eph.created_date as created_date,eph.modified_date as modified_date,"
			+ "e.empcode as empcode,e.employee_name as employee_name,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "dep.dept_name as dept_name,dep.dept_name_short as dept_name_short,des.designation_name as designation_name,"
			+ "des.designation_short_name as designation_short_name,jt.job_type as job_type,jt.job_short_name as job_short_name,et.emp_type as emp_type, eph.er as er, eph.tax as tax, "
			+ " tss.salary_structure as salary_structure, e.date_of_joining as date_of_joining, e.pinfl as pinfl,eph.month as month, eph.year as year,eph.advance as advance,,eph.lic as lic "
			+ "from emp_pay_history eph "
			+ "left join employee_details e on e.emp_id=eph.emp_id "
			+ "left join department dep on dep.dept_id=eph.dept_id "
			+ "left join designation des on des.designation_id=e.designation_id "
			+ "left join schools sc on sc.school_id=e.school_id "
			+ "left join jobtype jt on jt.job_type_id=e.job_type_id "
			+ "left join employee_type et on et.emp_type_id=e.emp_type_id"
			+ " left join salary_structure_details tsd on tsd.salary_structure_id=e.salary_structure_id"
			+ " left join salary_structure tss on tss.salary_structure_id=tsd.salary_structure_id "
			+ "where CONCAT(IFNULL(eph.dept_id,' '),' ', IFNULL(eph.school_id ,' '),' ', IFNULL(eph.month,' '),' ', IFNULL(eph.year,' '),' ') LIKE %:keyword% order by eph.created_date desc ",nativeQuery=true)
	public Page<List<Map<String, Object>>> getEmployeePayHistoryWithKeyword(@Param("keyword")  Object keyword, Pageable pageable );


	@Query(value=" select  eph.emp_pay_history_id as id,eph.advance1 as advance1,eph.advance2 as advance2,eph.emp_id as emp_id,eph.pay_days as pay_days,"
			+ "eph.inv_pay as inv_pay,eph.basic as basic,eph.hra as hra,eph.da as da,eph.cca as cca,eph.mr as mr,"
			+ "eph.fr as fr,eph.ta as ta,eph.other_allow as other_allow,eph.spl_1 as spl_1,eph.gross_pay as gross_pay,eph.pf as pf,eph.pt as pt,"
			+ "eph.esi as esi,eph.tds as tds,eph.total_earning as total_earning,eph.total_deduction as total_deduction,eph.net_pay as net_pay,"
			+ "eph.pf_account_no as pf_account_no,eph.pf_earnings as pf_earnings,eph.contribution_epf as contribution_epf,eph.epf_difference as epf_difference,"
			+ "eph.pension_fund as pension_fund,eph.esi_earnings as esi_earnings,eph.esi_contribution_employee as esi_contribution_employee,"
			+ "eph.year as year,eph.month as month,eph.created_by as created_by,eph.modified_by as modified_by,eph.fromdate as fromdate,"
			+ "eph.todate as todate,eph.dept_id as dept_id,eph.school_id as school_id,eph.bank as bank,eph.transport as transport,"
			+ "eph.salary_approve_status as salary_approve_status,eph.salaray_block_date as salaray_block_date,eph.new_join_status as new_join_status,"
			+ "eph.bank_account_no as bank_account_no,eph.created_date as created_date,eph.modified_date as modified_date,"
			+ "e.empcode as empcode,e.employee_name as employee_name,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "dep.dept_name as dept_name,dep.dept_name_short as dept_name_short,des.designation_name as designation_name,"
			+ "des.designation_short_name as designation_short_name,jt.job_type as job_type,jt.job_short_name as job_short_name,et.emp_type as emp_type,eph.er as er, eph.tax as tax,"
			+ " tss.salary_structure as salary_structure, e.date_of_joining as date_of_joining, e.annual_salary as annual_salary, e.pinfl as pinfl,eph.month as month, eph.year as year,eph.advance as advance,eph.lic as lic   " 
			+ "from emp_pay_history eph "
			+ "left join employee_details e on e.emp_id=eph.emp_id "
			+ "left join department dep on dep.dept_id=eph.dept_id "
			+ "left join designation des on des.designation_id=e.designation_id "
			+ "left join schools sc on sc.school_id=e.school_id "
			+ "left join jobtype jt on jt.job_type_id=e.job_type_id "
			+ "left join employee_type et on et.emp_type_id=e.emp_type_id "
			+ " left join salary_structure_details tsd on tsd.salary_structure_id=e.salary_structure_id"
			+ " left join salary_structure tss on tss.salary_structure_id=tsd.salary_structure_id "
			+ "where  (:schoolId IS NULL OR eph.school_id=:schoolId) AND (:deptId IS NULL OR eph.dept_id=:deptId) AND  (:month IS NULL OR eph.month=:month) AND  (:year IS NULL OR eph.year=:year)  order by eph.created_date desc ",nativeQuery=true)
	public Page<List<Map<String, Object>>> getEmployeePayHistory(@Param("schoolId") Integer school_id ,@Param("deptId") Integer dept_id,@Param("month") Integer month,@Param("year") Integer year, Pageable pageable );

	@Query(value = "SELECT new com.au.dto.PaySlipDetails(" +
            "e.empcode as empCode, e.employee_name as employeeName, " +
            "e.date_of_joining as dateOfJoining, d.dept_name as departmentName, de.designation_name as designationName, " +
            "eph.pay_days as payDays, CONCAT(e.bank_id, ' - ', e.bank_branch) as bankName, e.bank_account_no as accountNo, " +
            "e.mfo as mfo, eph.basic as basic, eph.hra as hra, eph.da as da, eph.cca as cca, eph.ta as ta, " +
            "eph.mr as mr, eph.fr as fr, eph.other_allow as otherAllow, eph.spl_1 as spl1, eph.gross_pay as grossPay, " +
            "eph.pf as pf, eph.pt as pt, eph.esi as esi, eph.tds as tds, eph.advance1 as advance1, eph.advance2 as advance2, " +
            "eph.total_earning as totalEarning, eph.total_deduction as totalDeduction, eph.net_pay as netPay, " +
            "eph.pf_account_no as pfAccountNo, eph.pf_earnings as pfEarnings, eph.contribution_epf as contributionEpf, " +
            "eph.epf_difference as epfDifference, eph.pension_fund as pensionFund, eph.esi_earnings as esiEarnings, " +
            "eph.esi_contribution_employee as esiContributionEmployee, e.passportno as passportNo, " +
            "eph.er as er, eph.tax as tax, e.pinfl as pinfl, eph.month as month, eph.year as year, eph.advance as advance, " +
            "eph.remarks as remarks, sc.school_name as schoolName,eph.pTax As pTax,e.uan_no As uan_no,e.transport_deassign_month As transport_deassign_month," +
			"e.pf_no As pf_no,e.pan_no As pan_no, sc.org_name as school_org_name, sc.school_name_short as school_short_name," +
			" e.ctc as ctc,eph.lic as lic, eph.examRemuneration as examRemuneration ) " +
            "FROM EmployeePayHistory eph " +
            "LEFT JOIN EmployeeDetails e ON e.emp_id = eph.emp_id " +
            "LEFT JOIN Schools sc ON sc.school_id = e.school_id " +
            "LEFT JOIN Department d ON d.dept_id = e.dept_id " +
            "LEFT JOIN Designation de ON de.designation_id = e.designation_id " +
            "WHERE eph.emp_pay_history_id = :emp_pay_history_id group by e.empcode", nativeQuery = false)
    PaySlipDetails getPaySlipDetails(@Param("emp_pay_history_id") Integer emp_pay_history_id);
	
	
	@Query(value=" select new com.au.dto.EmployeePayHistoryDTO( eph.emp_pay_history_id as id,"
	        + " e.empcode as empcode, e.employee_name as employee_name, dep.dept_name as dept_name, des.designation_name as designation_name, jt.job_type as job_type,"
	        + " et.empType as employee_type, tss.salary_structure as salary_structure, e.date_of_joining as date_of_joining, eph.pay_days as pay_days, e.grosspay_ctc as master_salary, eph.basic as basic,"
	        + " eph.er as er, eph.total_earning as total_earning, eph.tax as tax, eph.total_deduction as total_deduction, eph.pf as pf, eph.net_pay as netpay, eph.advance as advance, eph.pTax as pTax, eph.remarks as remarks, e.pinfl as pinfl,eph.month as month, eph.year as year,"
	        + " eph.hra as hra, eph.da as da, eph.cca as cca, eph.ta as ta, eph.mr as mr, eph.fr as fr, eph.other_allow as other_allow, eph.spl_1 as spl_1, eph.gross_pay as gross_pay,"
	        + " eph.pt as pt, eph.esi as esi, eph.tds as tds, eph.advance1 as advance1, eph.advance2 as advance2, eph.net_pay as net_pay, eph.pf_account_no as pf_account_no, eph.pf_earnings as pf_earnings,"
	        + " eph.contribution_epf as contribution_epf, eph.epf_difference as epf_difference, eph.pension_fund as pension_fund, eph.esi_earnings as esi_earnings, eph.esi_contribution_employee as esi_contribution_employee, "
	        + " sc.school_name_short as schoolShortName,dep.dept_name_short as departmentShortName,des.designation_short_name as  designationShortName,  eph.lic as lic,e.gender as gender) " 
	        + "from EmployeePayHistory eph "
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
	public Page<EmployeePayHistoryDTO> getEmployeePayHistoryData(@Param("schoolId") Integer school_id, @Param("deptId") Integer dept_id, @Param("month") Integer month, @Param("year") Integer year, @Param("keyword") Object keyword, Pageable pageable);

	@Query(value=" select new com.au.dto.EmployeePayHistoryDTO( eph.emp_pay_history_id as id,"
	        + " e.empcode as empcode, e.employee_name as employee_name, dep.dept_name as dept_name, des.designation_name as designation_name, jt.job_type as job_type,"
	        + " et.empType as employee_type, tss.salary_structure as salary_structure, e.date_of_joining as date_of_joining, eph.pay_days as pay_days, e.grosspay_ctc as master_salary, eph.basic as basic,"
	        + " eph.er as er, eph.total_earning as total_earning, eph.tax as tax, eph.total_deduction as total_deduction, eph.pf as pf, eph.net_pay as netpay, eph.advance as advance, eph.pTax as pTax, eph.remarks as remarks, e.pinfl as pinfl,eph.month as month, eph.year as year,"
	        + " eph.hra as hra, eph.da as da, eph.cca as cca, eph.ta as ta, eph.mr as mr, eph.fr as fr, eph.other_allow as other_allow, eph.spl_1 as spl_1, eph.gross_pay as gross_pay,"
	        + " eph.pt as pt, eph.esi as esi, eph.tds as tds, eph.advance1 as advance1, eph.advance2 as advance2, eph.net_pay as net_pay, eph.pf_account_no as pf_account_no, eph.pf_earnings as pf_earnings,"
	        + " eph.contribution_epf as contribution_epf, eph.epf_difference as epf_difference, eph.pension_fund as pension_fund, eph.esi_earnings as esi_earnings, eph.esi_contribution_employee as esi_contribution_employee, sc.school_name_short as schoolShortName, dep.dept_name_short as deptpartmentShortName,des.designation_short_name as  designationShortName, eph.lic as lic,e.gender as gender  ) " 
			+ "from EmployeePayHistory eph "
			+ "left join EmployeeDetails e on e.emp_id=eph.emp_id "
			+ "left join Department dep on dep.dept_id=eph.dept_id "
			+ "left join Designation des on des.designation_id=e.designation_id "
			+ "left join Schools sc on sc.school_id=e.school_id "
			+ "left join JobType jt on jt.job_type_id=e.job_type_id "
			+ "left join EmployeeType et on et.empTypeId=e.emp_type_id "
			+ " left join SalaryStructureDetails tsd on tsd.salary_structure_id=e.salary_structure_id"
			+ " left join SalaryStructure tss on tss.salary_structure_id=tsd.salary_structure_id "
			 +" WHERE" +
		        " (:schoolId IS NULL OR eph.school_id = :schoolId)" +
		        " AND (:deptId IS NULL OR eph.dept_id = :deptId)" +
		        " AND (:month IS NULL OR eph.month = :month)" +
		        " AND (:year IS NULL OR eph.year = :year)" +
		        " group by e.empcode  ",nativeQuery=false)
	List<EmployeePayHistoryDTO> getEmployeePayHistoryList(@Param("schoolId") Integer school_id ,@Param("deptId") Integer dept_id,@Param("month") Integer month,@Param("year") Integer year);
	
	@Query(value=" select new map( e.empcode as empCode, e.employee_name as employeeName,eph.emp_pay_history_id as id,"
			+ "e.date_of_joining as dateOfJoining,  d.dept_name as departmentName, de.designation_name as designationName,"
			+ "eph.pay_days as payDays, e.bank_branch as bankName, e.bank_account_no as accountNo,e.mfo as mfo,"
			+ "eph.basic as basic,eph.pf as pf,eph.pTax as pt, eph.hra as hra, eph.inv_pay as inv_pay,eph.mr as mr,eph.fr as fr,"
			+ "eph.pf_account_no as pf_account_no,eph.pf_earnings as pf_earnings,eph.total_earning as total_earning,"
			+ "eph.total_deduction as total_deduction, e.passportno as passportno, eph.er as er, eph.tax as tax,e.emp_id as emp_id,"
			+ "e.pinfl as pinfl,eph.month as month, eph.year as year,eph.advance as advance,eph.remarks as remarks,ua.id as user_id, eph.lic as lic) from EmployeePayHistory eph "
			+ "left join EmployeeDetails e on e.emp_id=eph.emp_id "
			+ "left join Department d on d.dept_id=e.dept_id "
			+ "left join Designation de on de.designation_id=e.designation_id "
			+ "left join UserAuthentication ua on ua.email=e.email "
			+ "where eph.emp_id=?1 And eph.month=?2 And eph.year=?3",nativeQuery = false)
	List<HashMap<String,Object>> paySlipOfUser(Integer emp_id,Integer month,Integer year);
	
	@Query(value="SELECT  " +
			"  ROUND(CAST(IFNULL(SUM(eph.total_earning), 0) AS DOUBLE), 0) AS s439, " +
			"  ROUND(CAST(IFNULL(SUM(eph.advance), 0) AS DOUBLE), 0) AS s332, " +
			"  ROUND(CAST(IFNULL(SUM(eph.net_pay), 0) AS DOUBLE), 0) AS s440, " +
			"  ROUND(CAST(IFNULL(SUM(eph.tds), 0) AS DOUBLE), 0) AS s331, " +
			"  ROUND(CAST(IFNULL(SUM(eph.pt), 0) AS DOUBLE), 0) AS s11, " +
			"  ROUND(CAST(IFNULL(SUM(eph.exam_remuneration), 0) AS DOUBLE), 0) AS s441, " +
			"  ROUND(CAST(IFNULL(SUM(eph.lic), 0) AS DOUBLE), 0) AS s330, " +
			"  ROUND(CAST(IFNULL(SUM(eph.esi_contribution_employee), 0) AS DOUBLE), 0) AS s10, " +
			"  ROUND(CAST((IFNULL(SUM(eph.contribution_epf), 0) + IFNULL(SUM(eph.contribution_epf), 0) / 100) AS DOUBLE), 0) AS s8, " +
			"  ROUND(CAST((IFNULL(SUM(eph.esi), 0) + IFNULL(SUM(eph.esi_contribution_employee), 0)) AS DOUBLE), 0) AS s485, " +
			"  ROUND(CAST((IFNULL(SUM(eph.pf), 0) + IFNULL(SUM(eph.contribution_epf), 0) + IFNULL(SUM(eph.contribution_epf), 0) / 100) AS DOUBLE), 0) AS s486, " +
			"  ROUND(CAST(IFNULL(SUM(eph.pf), 0) AS DOUBLE), 0) AS s7, " +
			"  ROUND(CAST(IFNULL(SUM(eph.esi), 0) AS DOUBLE), 0) AS s9, " +
			"  ROUND(CAST(( " +
			"    IFNULL(SUM(eph.net_pay), 0) +  " +
			"    IFNULL(SUM(eph.tds), 0) +  " +
			"    IFNULL(SUM(eph.pt), 0) +  " +
			"    IFNULL(SUM(eph.pf), 0) +  " +
			"    IFNULL(SUM(eph.esi), 0) +  " +
			"    IFNULL(SUM(eph.lic), 0) +  " +
			"    IFNULL(SUM(eph.advance), 0) " +
			"  ) AS DOUBLE), 0) AS totalCredit " +
			"FROM emp_pay_history eph  " +
			"WHERE eph.month = :month  " +
			"  AND eph.year = :year  " +
			"  AND eph.school_id = :schoolId " +
			"  AND eph.pay_days != CAST(0 AS DOUBLE)",nativeQuery=true)
	Map<String, Object> employeePayHistoryDataForJournalVoucher(Integer month,Integer year,Integer schoolId);

	@Query(value=" select * from emp_pay_history eph where eph.emp_id=?1",nativeQuery=true)
	EmployeePayHistory getDataByEmpId(Integer emp_id);

	@Modifying
	@Query(value = "update EmployeePayHistory ml set ml.new_join_status=2 where ml.emp_id=?1")
	void updateDataByEmpId(Integer emp_id);


	@Query(value = "SELECT new com.au.dto.PaySlipDetails(" +
			"e.empcode as empCode, e.employee_name as employeeName, " +
			"e.date_of_joining as dateOfJoining, d.dept_name as departmentName, de.designation_name as designationName, " +
			"eph.pay_days as payDays, e.bank_branch as bankName, e.bank_account_no as accountNo, " +
			"e.mfo as mfo, eph.basic as basic, eph.hra as hra, eph.da as da, eph.cca as cca, eph.ta as ta, " +
			"eph.mr as mr, eph.fr as fr, eph.other_allow as otherAllow, eph.spl_1 as spl1, eph.gross_pay as grossPay, " +
			"eph.pf as pf, eph.pt as pt, eph.esi as esi, eph.tds as tds, eph.advance1 as advance1, eph.advance2 as advance2, " +
			"eph.total_earning as totalEarning, eph.total_deduction as totalDeduction, eph.net_pay as netPay, " +
			"eph.pf_account_no as pfAccountNo, eph.pf_earnings as pfEarnings, eph.contribution_epf as contributionEpf, " +
			"eph.epf_difference as epfDifference, eph.pension_fund as pensionFund, eph.esi_earnings as esiEarnings, " +
			"eph.esi_contribution_employee as esiContributionEmployee, e.passportno as passportNo, " +
			"eph.er as er, eph.tax as tax, e.pinfl as pinfl, eph.month as month, eph.year as year, eph.advance as advance, " +
			"eph.remarks as remarks, sc.school_name as schoolName,eph.pTax As pTax,e.uan_no As uan_no," +
			"e.transport_deassign_month As transport_deassign_month,e.pf_no As pf_no,e.pan_no As pan_no, sc.org_name as school_org_name, " +
			"sc.school_name_short as school_short_name, e.ctc as ctc,eph.lic as lic,eph.examRemuneration As examRemuneration ) " +
			"FROM EmployeePayHistory eph " +
			"LEFT JOIN EmployeeDetails e ON e.emp_id = eph.emp_id " +
			"LEFT JOIN Schools sc ON sc.school_id = e.school_id " +
			"LEFT JOIN Department d ON d.dept_id = e.dept_id " +
			"LEFT JOIN Designation de ON de.designation_id = e.designation_id " +
			"WHERE eph.emp_id = :empId and eph.month = :month and eph.year = :year", nativeQuery = false)
	PaySlipDetails getPaySlipDetailsOfEmployee(Integer empId, Integer month, Integer year);

	@Query(value=" select new map(eph.emp_pay_history_id as id,"
			+ " e.empcode as empcode, e.employee_name as employee_name, dep.dept_name as dept_name, des.designation_name as designation_name,"
			+ " eph.pay_days as pay_days, e.grosspay_ctc as master_salary, eph.basic as basic,"
			+ " eph.er as er, eph.total_earning as total_earning, eph.tax as tax, eph.total_deduction as total_deduction, eph.pf as pf, eph.net_pay as netpay, eph.advance as advance, eph.pTax as pTax, eph.remarks as remarks, e.pinfl as pinfl,eph.month as month, eph.year as year,"
			+ " eph.hra as hra, eph.da as da, eph.cca as cca, eph.ta as ta, eph.mr as mr, eph.fr as fr, eph.other_allow as other_allow, eph.spl_1 as spl_1, eph.gross_pay as gross_pay,"
			+ " eph.pt as pt, eph.esi as esi, eph.tds as tds, eph.advance1 as advance1, eph.advance2 as advance2, eph.net_pay as net_pay, eph.pf_account_no as pf_account_no, eph.pf_earnings as pf_earnings,"
			+ " eph.contribution_epf as contribution_epf, eph.epf_difference as epf_difference, eph.pension_fund as pension_fund, eph.esi_earnings as esi_earnings, eph.esi_contribution_employee as esi_contribution_employee, "
			+ " sc.school_name_short as schoolShortName,dep.dept_name_short as departmentShortName,des.designation_short_name as  designationShortName,  eph.lic as lic, e.uan_no as uan_no," +
			" e.contract_empcode as contract_empcode,ap.lic_number as lic_number,e.bank_ifsccode as bank_ifsccode, e.bank_account_no as bank_account_no, e.date_of_joining as date_of_joining, e.pan_no as pan_no, e.bank_id as bank_id) "
			+ "from EmployeePayHistory eph "
			+ "left join EmployeeDetails e on e.emp_id=eph.emp_id "
			+ "Left join AdvancePayScaleDeduction ap on ap.emp_id=e.emp_id "
			+ "left join Department dep on dep.dept_id=eph.dept_id "
			+ "left join Designation des on des.designation_id=e.designation_id "
			+ "left join Schools sc on sc.school_id=eph.school_id "
			+ "left join JobType jt on jt.job_type_id=e.job_type_id "
			+ "left join EmployeeType et on et.empTypeId=e.emp_type_id "
			+ "left join SalaryStructureDetails tsd on tsd.salary_structure_id=e.salary_structure_id "
			+ "left join SalaryStructure tss on tss.salary_structure_id=tsd.salary_structure_id "
			+ "WHERE "
			+ "(:schoolId IS NULL OR eph.school_id = :schoolId) "
			+ "AND (:deptId IS NULL OR eph.dept_id = :deptId) "
			+ "AND (:month IS NULL OR eph.month = :month) "
			+ "AND (:year IS NULL OR eph.year = :year) group by e.empcode", nativeQuery=false)
	public List<HashMap<String, Object>> getEmployeePayHistoryData(@Param("schoolId") Integer school_id, @Param("deptId") Integer dept_id, @Param("month") Integer month, @Param("year") Integer year);

	@Query(value="with pay_summary As ( " +
			" select CAST(SUM(IFNULL(eph.net_pay, 0)) AS DECIMAL(15,2)) as totalNetPay," +
			"        eph.emp_id," +
			"case " +
			"   when sc.school_name_short is null Then 'NA' Else sc.school_name_short End  as schoolNameShort," +
			" Case " +
			"    When  e.bank_id is null Then 'NA'Else e.bank_id End as bankId from emp_pay_history eph " +
			"inner join employee_details e on e.emp_id=eph.emp_id " +
			"left join schools sc on sc.school_id=eph.school_id " +
			"WHERE (:month is null Or eph.month = :month) AND  (:year is null Or eph.year = :year) group by eph.school_id,e.bank_id " +
			") " +
			"Select totalNetPay, schoolNameShort, bankId From pay_summary", nativeQuery=true)
	List<Map<String, Object>> payReportOfEmployeeBySchoolAndBank(Integer month, Integer year);

	@Query(value ="SELECT count(emp_id) as attendanceCount," +
			"(Select count(emp_id) From emp_pay_history eph where (:month is null Or eph.month=:month) and (:year is null Or eph.year=:year)) as paidAttendanceCount " +
			"From employee_sheets es Where (:month is null Or es.month=:month) and (:year is null Or es.year=:year) ", nativeQuery=true)
	List<Map<String, Object>> employeesCountFromAttendanceAndPaySheet(Integer month, Integer year);

	@Query(value = "SELECT CAST(Sum(IfNull(eph.basic,0)) AS DECIMAL(15,2)) as basic, CAST(Sum(IfNull(eph.hra,0)) AS DECIMAL(15,2)) as hra," +
			"CAST(Sum(IfNull(eph.da,0)) AS DECIMAL(15,2)) as da, CAST(Sum(IfNull(eph.cca,0)) AS DECIMAL(15,2)) as cca, CAST(Sum(IfNull(eph.ta,0)) AS DECIMAL(15,2)) as ta, " +
			"CAST(Sum(IfNull(eph.spl_1,0)) AS DECIMAL(15,2)) as spl1, CAST(Sum(IfNull(eph.gross_pay,0)) AS DECIMAL(15,2)) as grossPay, " +
			"CAST(Sum(IfNull(eph.pf,0)) AS DECIMAL(15,2)) as pf, CAST(Sum(IfNull(eph.pt,0)) AS DECIMAL(15,2)) as pt, CAST(Sum(IfNull(eph.esi,0)) AS DECIMAL(15,2)) as esi, CAST(Sum(IfNull(eph.tds,0)) AS DECIMAL(15,2)) as tds, " +
			"CAST(Sum(IfNull(eph.total_earning,0)) AS DECIMAL(15,2)) as totalEarning, CAST(Sum(IfNull(eph.total_deduction,0)) AS DECIMAL(15,2)) as totalDeduction, CAST(Sum(IfNull(eph.net_pay,0)) AS DECIMAL(15,2)) as netPay, " +
			"CAST(Sum(IfNull(eph.pf_earnings,0)) AS DECIMAL(15,2)) as pfEarnings, CAST(Sum(IfNull(eph.contribution_epf,0)) AS DECIMAL(15,2)) as contributionEpf, " +
			"CAST(Sum(IfNull(eph.epf_difference,0)) AS DECIMAL(15,2)) as epfDifference, CAST(Sum(IfNull(eph.pension_fund,0)) AS DECIMAL(15,2)) as pensionFund, " +
			"CAST(Sum(IfNull(eph.esi_contribution_employee,0)) AS DECIMAL(15,2)) as esiContributionEmployee," +
			"CAST(Sum(IfNull(eph.advance,0)) AS DECIMAL(15,2)) as advance,CAST(Sum(IfNull(eph.lic,0)) AS DECIMAL(15,2)) as lic,CAST(Sum(IfNull(eph.transport,0)) AS DECIMAL(15,2)) as transport  " +
			"FROM emp_pay_history eph " +
			"WHERE (:month is null Or eph.month = :month) and (:year is null Or eph.year = :year)", nativeQuery = true)
	Map<String, Object> totalSalarySlipByMonthAndYear(Integer month, Integer year);
}
