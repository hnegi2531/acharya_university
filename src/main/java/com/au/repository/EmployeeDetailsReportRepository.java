package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.EmployeeDetails;

@Transactional
@Repository
public interface EmployeeDetailsReportRepository extends JpaRepository<EmployeeDetails, Integer> {
	
	@Query(value = "select count(edh.gender) as gender_count,edh.gender as gender,"
			+ "sc.school_name_short as school_name_short from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id = :school_id and edh.active=true And r.role_name not in ('Guest User') group by edh.gender",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportONGender(Integer school_id);
	
	@Query(value = "select sc.school_name_short as school_name_short,dg.designation_name as designation_short_name,r.role_name As role_name,"
			+ "count(dg.designation_name) as designation_count "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "inner join designation dg on edh.designation_id=dg.designation_id "
			+ "Where edh.school_id = :school_id and edh.active=true And r.role_name not in ('Guest User') group by dg.designation_short_name",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnDesignation(Integer school_id);
	
	@Query(value = "select sc.school_name_short as school_name_short,de.dept_name_short as dept_name_short,"
			+ "count(de.dept_name_short) as department_count "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "inner join department de on edh.dept_id=de.dept_id "
			+ "Where edh.school_id = :school_id and edh.active=true And r.role_name not in ('Guest User') group by de.dept_name_short",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnDepartment(Integer school_id);
	
	@Query(value = "select sc.school_name_short as school_name_short,et.emp_type as emp_type_short_name,"
			+ "count(et.emp_type) as employee_type_count,org.org_name as org_name,org.org_type as org_type "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Left join organization org on org.org_id=sc.org_id "
			+ "inner join employee_type et on edh.emp_type_id=et.emp_type_id "
			+ "Where edh.school_id = :school_id and edh.active=true And r.role_name not in ('Guest User') group by et.emp_type_short_name",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnEmployeeType(Integer school_id);
	
	@Query(value = "select sc.school_name_short as school_name_short,jt.job_type as job_short_name,"
			+ "count(jt.job_type) as job_type_count "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "inner join job_type jt on edh.job_type_id=jt.job_type_id "
			+ "Where edh.school_id = :school_id and edh.active=true And r.role_name not in ('Guest User') group by jt.job_short_name",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnJobType(Integer school_id);
	
	@Query(value = "select sc.school_name_short as school_name_short,"
			+ "concat(sft.shift_name,'- (',SUBSTRING_INDEX(sft.shift_start_time,':',2),'-',SUBSTRING_INDEX(sft.shift_end_time,':',2),')') as shift_name,"
			+ "count(sft.shift_name) as shift_count "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Inner join shift sft on edh.shift_category_id=sft.shift_category_id "
			+ "Where edh.school_id = :school_id and edh.active=true And r.role_name not in ('Guest User') group by sft.shift_name",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnShift(Integer school_id);
	
	@Query(value = "select sc.school_name_short as school_name_short,edh.martial_status as martial_status,"
			+ "count(edh.martial_status) as martial_status_count "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id = :school_id and edh.active=true And r.role_name not in ('Guest User') group by edh.martial_status",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnMaritalStatus(Integer school_id);
	
	@Query(value = "select sc.school_name_short as school_name_short,ifnull(edh.exp_in_years,0) as exp_in_years,"
			+ "count(edh.master_code) as exp_in_years_count "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id = :school_id And edh.exp_in_years between :first_gruop_number and :second_group_number and edh.active=true And r.role_name not in ('Guest User') group by edh.exp_in_years",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnExperienceInYear(Integer school_id,Integer first_gruop_number,Double second_group_number);
	
	
	@Query(value = "select sc.school_name_short as school_name_short,ifnull(edh.exp_in_years*12 + edh.exp_in_months,0) as exp_in_months,"
			+ "count(edh.exp_in_years+edh.exp_in_months) as exp_in_months_count "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id = :school_id and edh.active=true And r.role_name not in ('Guest User') group by edh.exp_in_years*12 +edh.exp_in_months ",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnExperienceInMonth(Integer school_id);
	
	
	@Query(value = "select sc.school_name_short as school_name_short,"
			+ "count(edh.school_id) as school_count  "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "where edh.active=true And r.role_name not in ('Guest User') group by edh.school_id",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnSchools();
	
	
	@Query(value = "select sc.school_name_short as school_name_short,edh.date_of_joining as date_of_joining,"
			+ "count(month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))) as date_of_joining_count "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id = :school_id and edh.active=true And r.role_name not in ('Guest User') group by month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y')) ",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnJoiningDate(Integer school_id);
	
	@Query(value = "select sc.school_name_short as school_name_short,edh.dateofbirth as dateofbirth,"
			+ "year(curdate()) - year(edh.dateofbirth) as  employee_age,count(year(curdate()) - year(edh.dateofbirth))as age_count "
			+ "from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id =?1 And (year(curdate()) - year(edh.dateofbirth)) between ?2 and ?3 And edh.active=true "
			+ "And r.role_name not in ('Guest User') group by (year(curdate()) - year(edh.dateofbirth))",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnDateOfBirth(Integer school_id,Integer first_age_group,Integer second_age_group);

	@Query(value = "select distinct edh.school_id from employee_details edh where edh.active=true",nativeQuery=true)
	public List<Integer> getAllSchoolId();
	
	@Query(value = "select sc.school_name_short as school_name_short,edh.date_of_joining as date_of_joining,"
			+ "monthname(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))as month,count(month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))) as joining_in_month_count from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id =:school_id and year(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))= :year and edh.active=true "
			+ "And r.role_name not in ('Guest User') group by month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnMonthWiseOfJoiningYear(Integer school_id,Integer year);
	
	@Query(value = "select sc.school_name_short as school_name_short,edh.date_of_joining as date_of_joining,"
			+ "monthname(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))as month,count(month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))) as joining_in_month_count from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id =:school_id and year(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))= :year and edh.active=true And r.role_name not in ('Guest User') "
			+ "group by month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnMonthWiseOfJoiningYearOnSchool(Integer year,Integer school_id);
	
	@Query(value = "select sc.school_name_short as school_name_short,res.relieving_date as relieving_date,"
			+ "monthname(res.relieving_date)as month,count(month(res.relieving_date)) as relieving_in_month_count from resignation res "
			+"Inner join employee_details edh On edh.emp_id=res.emp_id "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id =:school_id and year(res.relieving_date)= :year and res.active=true And r.role_name not in ('Guest User') "
			+ "group by month(res.relieving_date)",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeRelievingReportDataOnMonthWise(Integer school_id,Integer year);
	
	@Query(value = "select sc.school_name_short as school_name_short,edh.date_of_joining as date_of_joining,"
			+ "monthname(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))as month,count(month(STR_TO_DATE(edh.date_of_joining,'%d-%m-%Y'))) as joining_in_month_count from employee_details edh "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id =:school_id and year(edh.date_of_joining)= :year and edh.active=false And r.role_name not in ('Guest User') "
			+ "group by month(edh.date_of_joining)",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeDetailsForReportOnMonthWiseOfJoiningYearInactiveData(Integer school_id,Integer year);
	
	@Query(value = "select sc.school_name_short as school_name_short,res.relieving_date as relieving_date,"
			+ "monthname(res.relieving_date)as month,count(month(res.relieving_date)) as relieving_in_month_count from resignation res "
			+"Inner join employee_details edh On edh.emp_id=res.emp_id "
			+ "left join user_details ud on ud.email=edh.email  "
			+ "left join user_role ur on ur.id=ud.id  "
			+ "left join roles r on ur.role_id=r.role_id "
			+ "Inner join schools sc on edh.school_id=sc.school_id "
			+ "Where edh.school_id =:school_id and year(res.relieving_date)= :year and res.active=false And r.role_name not in ('Guest User') "
			+ "group by month(res.relieving_date)",nativeQuery=true)
	public List<Map<String, Object>> getEmployeeRelievingReportDataOnMonthWiseInactiveData(Integer school_id,Integer year);

	@Query(value="  select e from EmployeeDetails e where e.contract_empcode=:contract_empcode ")
	public EmployeeDetails findByContractCode(String contract_empcode);
}
