package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.PaySlipLockDate;

@Transactional
@Repository
public interface PaySlipLockDateRepository extends JpaRepository<PaySlipLockDate,Integer>{

	@Query(value = "Select count(*) From PaySlipLockDate pl where pl.school_id=?1 And pl.month=?2 And pl.year=?3 ")
	public Integer getCountOfMonthAndYear(Integer school_id,String month, String year);

		@Query(value = "SELECT psld.emp_id FROM PaySlipLockDate psld where psld.month = ?1 And psld.year = ?2 And "
			+ "psld.active =true")
	public List<String> getEmpIds(String month, String year);

		@Query(value = "Select psld.pay_slip_lock_date_id as id,psld.school_id as school_id,psld.emp_id as emp_id,psld.month as month,"
				+ "psld.year as year,psld.display_date as display_date,psld.created_by as created_by,psld.modified_by as modified_by,"
				+ "psld.created_date as created_date,psld.modified_date as modified_date,psld.active as active,"
				+ "psld.created_username as created_username,psld.modified_username as modified_username,"
				+ "sc.school_name_short as school_name_short,sc.school_name as school_name,"
				+ "(Select GROUP_CONCAT(EmployeeDetails.employee_name) From EmployeeDetails Where FIND_IN_SET(EmployeeDetails.emp_id,psld.emp_id)) as employeeName,"
				+ "(Select GROUP_CONCAT(EmployeeDetails.empcode) From EmployeeDetails Where FIND_IN_SET(EmployeeDetails.emp_id,psld.emp_id)) as employeeCode,"
				+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode "
				+ "From payslip_lockdate psld "
				+ "left join employee_details ed on ed.emp_id=psld.emp_id "
				+ "left join schools sc on sc.school_id=psld.school_id "
				+ "Where CONCAT(IfNull(psld.emp_id,''),'',IfNull(psld.created_by,''),'',IfNull(psld.created_date,''),'',"
				+ "IfNull(psld.school_id,'')) LIKE %?1% ", nativeQuery = true)
		public Page<Map<String, Object>> findAll1(Pageable pageable, Object keyword);
		
		
		@Query(value = "Select psld.pay_slip_lock_date_id as id,psld.school_id as school_id,psld.emp_id as emp_id,psld.month as month,"
				+ "psld.year as year,psld.display_date as display_date,psld.created_by as created_by,psld.modified_by as modified_by,"
				+ "psld.created_date as created_date,psld.modified_date as modified_date,psld.active as active,"
				+ "psld.created_username as created_username,psld.modified_username as modified_username,"
				+ "sc.school_name_short as school_name_short,sc.school_name as school_name,"
				+ "(Select GROUP_CONCAT(employee_details.employee_name) From employee_details Where FIND_IN_SET(employee_details.emp_id,psld.emp_id)) as employeeName,"
				+ "(Select GROUP_CONCAT(employee_details.empcode) From employee_details Where FIND_IN_SET(employee_details.emp_id,psld.emp_id)) as employeeCode,"
				+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode "
				+ "From payslip_lockdate psld "
				+ "left join employee_details ed on ed.emp_id=psld.emp_id "
				+ "left join schools sc on sc.school_id=psld.school_id", nativeQuery = true)
		public Page<Map<String, Object>> findAll2(Pageable pageable);			

		
		@Query(value = "Select psld.pay_slip_lock_date_id as id,psld.school_id as school_id,psld.emp_id as emp_id,psld.month as month,"
				+ "psld.year as year,psld.display_date as display_date,psld.created_by as created_by,psld.modified_by as modified_by,"
				+ "psld.created_date as created_date,psld.modified_date as modified_date,psld.active as active,"
				+ "psld.created_username as created_username,psld.modified_username as modified_username,"
				+ "(Select GROUP_CONCAT(employee_details.employee_name) From employee_details Where FIND_IN_SET(employee_details.emp_id,psld.emp_id)) as employeeName,"
				+ "(Select GROUP_CONCAT(employee_details.empcode) From employee_details Where FIND_IN_SET(employee_details.emp_id,psld.emp_id)) as employeeCode,"
				+ "sc.school_name_short as school_name_short,sc.school_name as school_name,"
				+ "ed.email as email,ed.employee_name as employee_name,ed.empcode as empcode "
				+ "From Payslip_lockdate psld "
				+ "left join employee_details ed on ed.emp_id=psld.emp_id "
				+ "left join schools sc on sc.school_id=psld.school_id "
				+ "where psld.emp_id=?1 And psld.month=?2 And psld.year=?3 And psld.active=true",nativeQuery = true)
		public List<Map<String,Object>> getPaySlipByEmployeeId(Integer emp_id, Integer month, Integer year);


	@Query(value = "Select pl From PaySlipLockDate pl where pl.month=?1 And pl.year=?2 And pl.active=true")
	PaySlipLockDate findByMonthAndYearAndActive(String month, String year, boolean b);

	@Modifying
	@Query(value = "update PaySlipLockDate pld set pld.active=false where pld.pay_slip_lock_date_id=?1")
	public void deactivate(Integer pay_slip_lock_date_id);
	
	@Modifying
	@Query(value = "update PaySlipLockDate pld set pld.active=true where pld.pay_slip_lock_date_id=?1")
	public void activate(Integer pay_slip_lock_date_id);

	@Query(value = "Select pl From PaySlipLockDate pl where pl.month=?1 And pl.year=?2 And pl.school_id =?3 And pl.active=true")
	PaySlipLockDate findByMonthAndYearAndActiveAndSchoolId(String month, String year, Integer schoolId);

	@Query(value="select * from payslip_lockdate where month=:month and year=:year and active=1 order by created_date desc limit 1 ",nativeQuery = true)
	PaySlipLockDate getFirstByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);
}
