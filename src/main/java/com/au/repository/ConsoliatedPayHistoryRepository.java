package com.au.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.au.dto.ConsoliatedPayHistoryDTO;
import com.au.model.ConsoliatedPayHistory;

@Repository
public interface ConsoliatedPayHistoryRepository extends JpaRepository<ConsoliatedPayHistory, Integer> {

	@Query(value = " select new com.au.dto.ConsoliatedPayHistoryDTO( cp.consoliatedPayHistoryId, cp.empId, COALESCE(cp.remainingAmount, e.consolidated_amount), cp.month, cp.year,"
			+ " cp.totalAmount, cp.payingAmount, cp.tds, cp.netPay, et.empTypeShortName, jt.job_short_name, sc.school_name_short,"
			+ "    d.dept_name_short, de.designation_short_name, e.from_date, e.to_date,cp.splPay, cp.transportAmount, " +
			"e.bank_account_no, e.bank_id, e.bank_ifsccode, e.pan_no,e.employee_name, " +
//			"es.paydays," +
			"e.empcode   ) from ConsoliatedPayHistory cp  left join  EmployeeDetails e on e.emp_id=cp.empId  "
			+ "left join EmployeeType et on et.empTypeId=e.emp_type_id"
			+ " left join JobType jt on jt.job_type_id=e.job_type_id left join Department d on d.dept_id=e.dept_id"
			+ " left join Designation de on de.designation_id=e.designation_id"
//			+ "  left join EmployeeSheet es on es.empId=e.emp_id and es.month=:month and es.year=:year  "
			+ " left join Schools sc on sc.school_id=e.school_id where cp.empId=:empId and cp.month=:month and cp.year=:year   ", nativeQuery = false)
	ConsoliatedPayHistoryDTO getEmployeeDetailsForConsoliation(Integer empId, Integer month, Integer year);

	@Query(value = " select e.consolidated_amount from EmployeeDetails e where e.emp_id=:empId ", nativeQuery = false)
	public Float getConsoliatedAmountByEmpId(Integer empId);

	@Query(value = "select e.remaining_amount from consoliated_pay_history e where e.emp_id=:empId order by e.created_date desc limit 1 ", nativeQuery = true)
	public Float findTopByEmpIdOrderByCreatedDateDesc(@Param("empId") Integer empId);

/*	@Query(value = " select new com.au.dto.ConsoliatedPayHistoryDTO( cp.consoliatedPayHistoryId, cp.empId, COALESCE(cp.remainingAmount, e.consolidated_amount), cp.month, cp.year,"
			+ " cp.totalAmount, cp.payingAmount, cp.tds, cp.netPay, et.empTypeShortName, jt.job_short_name, sc.school_name_short,"
			+ "    d.dept_name_short, de.designation_short_name, e.from_date, e.to_date,cp.splPay, " +
			"cp.transportAmount, e.bank_account_no, e.bank_id, e.bank_ifsccode, e.pan_no,e.employee_name, " +
//			"es.paydays," +
			"e.empcode    ) from ConsoliatedPayHistory cp  left join  EmployeeDetails e on e.emp_id=cp.empId  "
			+ "left join EmployeeType et on et.empTypeId=e.emp_type_id"
			+ " left join JobType jt on jt.job_type_id=e.job_type_id left join Department d on d.dept_id=e.dept_id"
			+ " left join Designation de on de.designation_id=e.designation_id"
//			+ "  left join EmployeeSheet es on es.empId=e.emp_id and es.month=:month and es.year=:year  "
			+ " left join Schools sc on sc.school_id=e.school_id where  cp.month=:month and cp.year=:year   ", nativeQuery = false)*/
	@Query(value = "SELECT cph.emp_id As empId, sum(cph.paying_amount) As payingAmount,\n" +
			"cph.month As month, cph.year As year, sum(cph.tds) As tds, sum(cph.net_pay )As netPay,\n" +
			"et.emp_type_short_name As employeeType, jt.job_short_name As jobType, sc.school_name_short As institute,\n" +
			"d.dept_name_short As department, de.designation_short_name As designation, sum(cph.spl_pay) As splPay,\n" +
			"sum(cph.transport_amount) As transportAmount, ed.bank_account_no As accountNo, ed.bank_id As bank, ed.bank_ifsccode As ifsc,\n" +
			"ed.pan_no As pan, ed.employee_name As employeeName, ed.empcode As empCode\n" +
			"FROM consoliated_pay_history cph\n" +
			"left join employee_details ed on ed.emp_id = cph.emp_id\n" +
			"left join employee_type et on et.emp_type_id = ed.emp_type_id\n" +
			"left join job_type jt on jt.job_type_id = ed.job_type_id\n" +
			"left join department d on d.dept_id = ed.dept_id\n" +
			"left join designation de on de.designation_id = ed.designation_id\n" +
			"left join schools sc on sc.school_id = ed.school_id\n" +
			"where month = :month and year = :year\n" +
			"GROUP BY cph.emp_id", nativeQuery = true)
	List<Map<String, Object>> getConsoliationList(Integer month, Integer year);

	Boolean existsByMonthAndYear(Integer month, Integer year);

	ConsoliatedPayHistory findByEmpIdAndMonthAndYear(Integer empId, Integer month, Integer year);

	@Query(value = "select " +
            "e.emp_id as empId, " +
            "e.employee_name as employeeName, " +
            "e.empcode as empCode, " +
            "et.emp_type_short_name as employeeType, " +
            "sc.school_name_short as schoolName, " +
            "ca.from_date as fromDate, " +
            "ca.to_date as toDate, " +
			"ca.consoliated_amount_id as consoliatedAmountId," +
            "e.mobile as phoneNo, " +
            "ca.subject as subject, " +
            "e.email as email, " +
			"ca.consoliated_amount As consoliatedAmount," +
			"ca.remaining_amount as remainingAmount," +
			"cph.paying_amount AS payingAmount, " +
			"cph.consoliated_pay_history_id As consoliatedPayHistoryId,"
            + "es.paydays " +

        "from " +
            "consoliated_amount ca " +
		"left join " +
			"employee_details e on e.emp_id = ca.emp_id " +
        "left join " +
            "employee_type et on et.emp_type_id = e.emp_type_id " +
        "left join " +
            "schools sc on sc.school_id = e.school_id " +
        "left join " +
            "consoliated_pay_history cph on cph.consoliated_amount_id = ca.consoliated_amount_id and cph.month = :month and cph.year = :year " +
        "left join " +
            "employee_sheets es on es.emp_id = e.emp_id and es.month = :month and es.year = :year " +
            "where " +
            "et.emp_type_short_name = 'CON' " +
            "and e.active = 1 " +
			"and e.emp_type_id = 3 " +
			"and (:year > YEAR(ca.from_date) OR (:year = YEAR(ca.from_date) AND :month >= MONTH(ca.from_date)))  \n" +
			"  AND  \n" +
			"    (:year < YEAR(ca.to_date)  OR (:year = YEAR(ca.to_date) AND :month <= MONTH(ca.to_date))) " +
        "group by " +
			"ca.subject," +
            "ca.from_date, " +
            "ca.to_date, " +
            "ca.consoliated_amount_id, " +
            "ca.consoliated_amount, " +
            "cph.remaining_amount, " +
            "cph.paying_amount, " +
            "cph.total_amount," +
			"cph.consoliated_pay_history_id," +
             "es.paydays ",
        nativeQuery = true)
    List<Map<String, Object>> getConsultants(Integer month, Integer year);

/*	@Query(value = " select new com.au.dto.ConsoliatedPayHistoryDTO( cp.consoliatedPayHistoryId, cp.empId, COALESCE(cp.remainingAmount, e.consolidated_amount), cp.month, cp.year,"
			+ " cp.totalAmount, cp.payingAmount, cp.tds, cp.netPay, et.empTypeShortName, jt.job_short_name, sc.school_name_short,"
			+ "    d.dept_name_short, de.designation_short_name, e.from_date, e.to_date,cp.splPay, cp.transportAmount, " +
			"e.bank_account_no, e.bank_id, e.bank_ifsccode, e.pan_no,e.employee_name, " +
//			"es.paydays," +
			"e.empcode  ) from ConsoliatedPayHistory cp  left join  EmployeeDetails e on e.emp_id=cp.empId  "
			+ "left join EmployeeType et on et.empTypeId=e.emp_type_id"
			+ " left join JobType jt on jt.job_type_id=e.job_type_id left join Department d on d.dept_id=e.dept_id"
			+ " left join Designation de on de.designation_id=e.designation_id"
//			+ " left join EmployeeSheet es on es.empId=e.emp_id "
			+ " left join Schools sc on sc.school_id=e.school_id where  cp.empId=:empId ", nativeQuery = false)*/

	@Query(value = "select ca.from_date as fromDate,ca.to_date as toDate,ca.subject as subject,ca.consoliated_amount as consoliatedAmount,cph.emp_id as empId, " +
			"sum(cph.paying_amount) as payingAmount, sum(cph.net_pay) as netPay, \n" +
			"(ca.consoliated_amount - sum(cph.paying_amount)) remainingAmount,cph.consoliated_amount_id as consoliatedAmountId " +
			"from consoliated_pay_history cph\n" +
			"left join consoliated_amount ca on ca.consoliated_amount_id = cph.consoliated_amount_id\n" +
			"where cph.emp_id = :empId\n" +
			"GROUP BY cph.consoliated_amount_id, cph.emp_id", nativeQuery = true)
	List<Map<String, Object>> getConsoliationListByEmpId(Integer empId);

	Boolean existsByMonthAndYearAndEmpId(Integer month, Integer year, Integer empId);

	/*@Query(value = " select new com.au.dto.ConsoliatedPayHistoryDTO( cp.consoliatedPayHistoryId, cp.empId, COALESCE(cp.remainingAmount, e.consolidated_amount), cp.month, cp.year,"
			+ " cp.totalAmount, cp.payingAmount, cp.tds, cp.netPay, et.empTypeShortName, jt.job_short_name, sc.school_name_short,"
			+ "    d.dept_name_short, de.designation_short_name, e.from_date, e.to_date,cp.splPay, " +
			"cp.transportAmount, e.bank_account_no, e.bank_id, e.bank_ifsccode, e.pan_no,e.employee_name, " +
//			"es.paydays," +
			"e.empcode    ) from ConsoliatedPayHistory cp  left join  EmployeeDetails e on e.emp_id=cp.empId  "
			+ "left join EmployeeType et on et.empTypeId=e.emp_type_id"
			+ " left join JobType jt on jt.job_type_id=e.job_type_id left join Department d on d.dept_id=e.dept_id"
			+ " left join Designation de on de.designation_id=e.designation_id"
//			+ "  left join EmployeeSheet es on es.empId=e.emp_id and es.month=:month and es.year=:year  "
			+ " left join Schools sc on sc.school_id=e.school_id where  cp.month=:month and cp.year=:year and sc.school_id=:schoolId", nativeQuery = false)*/

	@Query(value = "SELECT cph.emp_id As empId, sum(cph.paying_amount) As payingAmount,\n" +
			"cph.month As month, cph.year As year, sum(cph.tds) As tds, sum(cph.net_pay )As netPay,\n" +
			"et.emp_type_short_name As employeeType, jt.job_short_name As jobType, sc.school_name_short As institute,\n" +
			"d.dept_name_short As department, de.designation_short_name As designation, sum(cph.spl_pay) As splPay,\n" +
			"sum(cph.transport_amount) As transportAmount, ed.bank_account_no As accountNo, ed.bank_id As bank, ed.bank_ifsccode As ifsc,\n" +
			"ed.pan_no As pan, ed.employee_name As employeeName, ed.empcode As empCode\n" +
			"FROM consoliated_pay_history cph\n" +
			"left join employee_details ed on ed.emp_id = cph.emp_id\n" +
			"left join employee_type et on et.emp_type_id = ed.emp_type_id\n" +
			"left join job_type jt on jt.job_type_id = ed.job_type_id\n" +
			"left join department d on d.dept_id = ed.dept_id\n" +
			"left join designation de on de.designation_id = ed.designation_id\n" +
			"left join schools sc on sc.school_id = ed.school_id\n" +
			"where month = :month and year = :year and ed.school_id = :schoolId " +
			"GROUP BY cph.emp_id", nativeQuery = true)
	List<Map<String, Object>> getConsoliationListBySchoolId(Integer month, Integer year, Integer schoolId);

	@Query(value = "select cph.remainingAmount from ConsoliatedPayHistory cph where cph.consoliatedAmountId = :consoliatedAmountId " +
			" and cph.month = :month and cph.year = :year")
	Optional<Float> getRemainingAmount(Integer consoliatedAmountId, Integer month, Integer year);

	ConsoliatedPayHistory findByConsoliatedAmountId(Integer consoliatedAmountId);

	List<ConsoliatedPayHistory> findByconsoliatedAmountId(Integer consoliatedAmountId);
}
