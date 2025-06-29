package com.au.repository;

import java.util.HashMap;
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

import com.au.model.LeavePattern;
import com.au.model.LeaveType;

@Repository
@Transactional
public interface LeavePatternRepository extends JpaRepository<LeavePattern, Integer> {

	@Query(value = "select h from LeavePattern h where h.active=true")
	public List<LeavePattern> findAll1();

	@Modifying
	@Query(value = "update LeavePattern h set h.active=false where h.leave_pattern_id=?1")
	public void updateLeavePattern(Integer id);

	@Modifying
	@Query(value = "update LeavePattern h set h.active=true where h.leave_pattern_id=?1")
	public void updateLeavePattern1(Integer id);

	@Query(value = "SELECT lp.leave_id,lp.leave_pattern_id,s.school_name,s.school_name_short,lp.year,lp.leave_days_permit,"
			+ "jt.job_type,et.emp_type,lt.leave_type " + "FROM leave_pattern lp " + "        inner JOIN "
			+ "    leave_type lt ON lp.leave_id = lt.leave_id " + "        inner JOIN "
			+ "    employee_type et ON lp.emp_type_id = et.emp_type_id " + "        inner JOIN "
			+ "    job_type jt ON lp.job_type_id = jt.job_type_id "
			+ "    inner join schools s  ON lp.school_id=s.school_id " + "    where lp.year=?1 "
			+ "    AND lp.school_id=?2 AND  lp.active=true AND lp.leave_id=?3", nativeQuery = true)
	public List<Map<String, Object>> fetchLeavePatternByYearAndSchool(Integer year, Integer school_id,
			Integer leave_id);

	/*
	 * @Query(value =
	 * "Select new map(lp.leave_id,lp.leave_pattern_id,s.school_name,s.school_name_short,lp.year,lp.leave_days_permit,"
	 * + "jt.job_type,et.emp_type)" +
	 * "From LeavePattern lp left join EmployeeType et on lp.emp_type_id = et.emp_type_id "
	 * + "left join JobType jt on lp.job_type_id = jt.job_type_id " +
	 * "left Schools s on lp.school_id=s.school_id where lp.year=?1  AND lp.school_id=?2 AND  lp.active=true AND lp.leave_id=?3"
	 * ) public List<HashMap<String, Object>>
	 * fetchLeavePatternByYearAndSchool(Integer year,Integer school_id,Integer
	 * leave_id);
	 */

	/*
	 * @Query(value =
	 * "select lp.leave_pattern_id,lp.leave_days_permit as leave_days_permit," +
	 * "lp.job_type_id,lp.emp_type_id,s.school_name_short,jt.job_type,et.emp_type,lp.active "
	 * + "from leave_pattern lp" + "			      LEFT JOIN" +
	 * "			    leave_type lt ON lp.leave_id = lt.leave_id" +
	 * "			        LEFT JOIN" +
	 * "			    employee_type et ON lp.emp_type_id = et.emp_type_id" +
	 * "			       LEFT JOIN" +
	 * "			   job_type jt ON lp.job_type_id = jt.job_type_id" +
	 * "			    left join schools s  ON lp.school_id=s.school_id " +
	 * "		   where lp.year=?1 " +
	 * "			  AND lp.school_id=?2 AND  lp.active=true " +
	 * "group by lp.leave_id,lp.job_type_id,lp.emp_type_id" + " ",nativeQuery =
	 * true) public List<Map<String, Object>>
	 * fetchLeavePatternByYearAndSchool(Integer year,Integer school_id);
	 */

	@Query(value = "SELECT lp.school_id,lp.leave_pattern_id,s.school_name,s.school_name_short,lp.year FROM leave_pattern lp left join schools s "
			+ "on lp.school_id=s.school_id", nativeQuery = true)
	public List<Map<String, Object>> fetchLeavePatternYearshool();

	@Query(value = "select count(*) from leave_pattern where year=?1  and school_id in (?2) and "
			+ "leave_id=?3 and emp_type_id in (?4) and job_type_id in (?5)", nativeQuery = true)
	public Integer validationLeavePattern(Integer year, List<Integer> list, Integer leave_id, List<Integer> list2,
			List<Integer> list3);

	@Query(value = "SELECT lp.leave_pattern_id as id,lp.leave_id as leave_id,lp.leave_days_permit as leave_days_permit,"
			+ "lp.emp_type_id as emp_type_id,lp.job_type_id as job_type_id,lp.school_id as school_id,"
			+ "lp.year as year,lp.specal_remarks as specal_remarks,lp.created_date as created_date,lp.modified_date as modified_date,"
			+ "lp.created_by as created_by,lp.modified_by as modified_by,lp.created_username as created_username,"
			+ "lp.modified_username as modified_username,lp.active as active,lt.leave_type_short as leave_type_short,"
			+ "lt.leave_type as leave_type,s.school_name as school_name,s.school_name_short as school_name_short From leave_pattern lp "
			+ "left join leave_type lt on lt.leave_id = lp.leave_id "
			+ "left join schools s on lp.school_id=s.school_id where lp.year=?1 and lp.school_id=?2", nativeQuery = true)
	public List<Map<String, Object>> fetchLeavePatternByYear(Integer year, Integer school_id);

	@Query(value = "select * from leave_pattern where year=?1 and school_id=?2", nativeQuery = true)
	public List<LeavePattern> fetchLeavePatternByYears(Integer year, Integer school_id);

	@Query(value = "select distinct leave_id  from leave_pattern lp", nativeQuery = true)
	public List<String> fetchleavetype();

	/*
	 * @Query(value = "select distinct leave_id  from leave_pattern lp",nativeQuery
	 * = true) public List<Integer> fetchleavetype();
	 */

	@Query(value = "Select new map(lp.leave_pattern_id as id,lp.leave_id as leave_id,lp.leave_days_permit as leave_days_permit,"
			+ "lp.year as year,lp.specal_remarks as specal_remarks,lp.created_date as created_date,lp.modified_date as modified_date,"
			+ "lp.created_by as created_by,lp.modified_by as modified_by,lp.created_username as created_username,"
			+ "lp.modified_username as modified_username,lp.active as active,lt.leave_type_short as leave_type_short,"
			+ "sc.school_name_short as school_name_short,et.empTypeShortName as empTypeShortName,jt.job_short_name as job_short_name) From LeavePattern lp "
			+ "Left Join LeaveType lt on lt.leave_id = lp.leave_id "
			+ "Left Join Schools sc on sc.school_id = lp.school_id "
			+ "Left Join EmployeeType et on et.empTypeId = lp.emp_type_id "
			+ "Left Join JobType jt on jt.job_type_id = lp.job_type_id "
			+ "Where CONCAT(IfNull(lp.year,''),'',IfNull(lp.specal_remarks,''),'',IfNull(lp.created_date,''),'',"
			+ "IfNull(lp.created_username,''),'',IfNull(lt.leave_type_short,''),'',IfNull(sc.school_name_short,''),'',"
			+ "IfNull(et.empTypeShortName,''),'',IfNull(jt.job_short_name,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);

	@Query(value = "Select new map(lp.leave_pattern_id as id,lp.leave_id as leave_id,lp.leave_days_permit as leave_days_permit,"
			+ "lp.year as year,lp.specal_remarks as specal_remarks,lp.created_date as created_date,lp.modified_date as modified_date,"
			+ "lp.created_by as created_by,lp.modified_by as modified_by,lp.created_username as created_username,"
			+ "lp.modified_username as modified_username,lp.active as active,lt.leave_type_short as leave_type_short,"
			+ "sc.school_name_short as school_name_short,et.empTypeShortName as empTypeShortName,jt.job_short_name as job_short_name) From LeavePattern lp "
			+ "Left Join LeaveType lt on lt.leave_id = lp.leave_id "
			+ "Left Join Schools sc on sc.school_id = lp.school_id "
			+ "Left Join EmployeeType et on et.empTypeId = lp.emp_type_id "
			+ "Left Join JobType jt on jt.job_type_id = lp.job_type_id")
	public Page<Object> findAll3(Pageable pageable);

//	@Query(value = "select lt from LeaveType lt where lt.leave_id IN "
//			+ "(select lp.leave_id from LeavePattern lp where lp.emp_type_id=?1 and lp.job_type_id=?2 and lp.active = true)")
//	@Query(value = "select lt from LeaveType lt where lt.leave_id IN "
//			+ "(select lp.leave_id from LeavePattern lp where lp.emp_type_id=?1 and lp.job_type_id=?2 and lp.active = true)")
	@Query(value = "select lt from LeaveType lt where  lt.hr_initialization_status=false And "
			+ "( lt.is_attendance=false or lt.is_attendance is null) And lt.type not in ('Holiday')")
	public List<LeaveType> getLeaves(Integer emp_type_id, Integer job_type_id);

	@Query(value = "select count(*) from leave_pattern where year=?1  and school_id=?2 and "
			+ "leave_id=?3 and emp_type_id=?4 and job_type_id=?5", nativeQuery = true)
	public Integer validationLeavePattern(Integer year, Integer school_id, Integer leave_id, Integer emp_type_id,
			Integer job_type_id);

	@Query(value = "select lp.leave_pattern_id from LeavePattern lp where lp.emp_type_id=?1 and lp.job_type_id=?2 and lp.school_id=?3 and lp.active=true")
	public List<Integer> getLeavePatternId(Integer emp_type_id, Integer job_type_id, Integer school_id);

	@Query(value = "select lp.leave_pattern_id from LeavePattern lp where lp.emp_type_id=?1 and lp.job_type_id=?2 and lp.school_id=?3 and lp.year=?4 and lp.leave_id=1 and lp.active=true")
	public Integer fetchLeavePatternId(Integer emp_type_id, Integer job_type_id, Integer school_id, Integer year);

	@Query(value = "select lp.leave_pattern_id from LeavePattern lp where lp.emp_type_id=?1 and lp.job_type_id=?2 and lp.school_id=?3 and lp.year=?4 and lp.leave_id=?5 and lp.active=true")
	public Integer fetchLeavePatternId1(Integer emp_type_id, Integer job_type_id, Integer school_id, Integer year,
			Integer leave_id);

	@Query(value = "select lt from LeaveType lt where ( lt.hr_initialization_status=false or lt.hr_initialization_status=true) and lt.type not in ('Holiday')")
	public List<LeaveType> leaveTypesAvailableForEmployeesForHrScreen(Integer emp_type_id, Integer job_type_id);

	@Query(value = "select lt from LeaveType lt where lt.leave_id IN "
			+ "(select lp.leave_id from LeaveKitty el left join LeavePattern lp on lp.leave_pattern_id=el.leave_pattern_id "
			+ "where el.emp_id=:emp_id and lp.active = true and el.active=true) and lt.is_attendance=true  And lt.type not in ('Holiday')")
	public List<LeaveType> getLeavesForLeaveKitty(Integer emp_id);

	
	@Query(value = "select  l.leave_pattern_id from leave_pattern l where l.emp_type_id=:empTypeId and l.job_type_id=:jobTypeId and  l.active=true and l.leave_id=:leaveId and l.school_id=:school_id order by l.created_date desc limit 1 ", nativeQuery = true)
	public Integer getLeavePatternIdEmpTypeIdAndJobTypeId(Integer empTypeId, Integer jobTypeId, Integer leaveId , Integer school_id);

	@Query(value = "select l from LeavePattern l where l.emp_type_id=:empTypeId and l.job_type_id=:jobTypeId and l.school_id=:schoolId and l.year=:year and l.active=1")
	public List<LeavePattern> getLeavePatternByEmpTypeIdAndJobTypeIdAndSchoolId(@Param("empTypeId") Integer empTypeId,
			@Param("jobTypeId") Integer jobTypeId, @Param("schoolId") Integer schoolId,@Param("year") Integer year);

	@Query("SELECT lp.leave_id as leaveId, lk.accumulated_count as accumulatedCount FROM LeavePattern lp " +
			"JOIN LeaveKitty lk  ON lk.leave_pattern_id = lp.leave_pattern_id " +
			"WHERE lk.emp_id = :empId")
	List<Map<Integer, Double>> findLeaveIdPatternIdAndAccumulatedCountByEmpId(@Param("empId") Integer empId);

}
