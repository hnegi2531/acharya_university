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

import com.au.dto.EmployeeSheetRequestDTO;
import com.au.dto.EmployeeSheetResponseDTO;
import com.au.model.EmployeeSheet;

@Repository
public interface EmployeeSheetRepository extends JpaRepository<EmployeeSheet, Integer> {

	EmployeeSheet findByEmpId(Integer emp_id);

	EmployeeSheet findByEmpIdAndMonth(Integer emp_id, int currentMonth);

	@Query(value = "select new com.au.dto.EmployeeSheetResponseDTO(" + "   es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) " + " from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ " where (:#{#employeeSheetRequestDTO.month} IS NULL OR es.month=:#{#employeeSheetRequestDTO.month})"
			+ " and (:#{#employeeSheetRequestDTO.year} IS NULL OR es.year=:#{#employeeSheetRequestDTO.year}) "
			+ " and (:#{#employeeSheetRequestDTO.school_id} IS NULL OR es.school_id=:#{#employeeSheetRequestDTO.school_id}) "
			+ " and (:#{#employeeSheetRequestDTO.dept_id} IS NULL OR  es.dept_id=:#{#employeeSheetRequestDTO.dept_id}) and et.empTypeShortName!='CON' group by es.empCode ", nativeQuery = false)
	List<EmployeeSheetResponseDTO> getListOfEmployeeAttendence(EmployeeSheetRequestDTO employeeSheetRequestDTO);

	@Query(value = "select * from employee_sheets es where es.month=?1 ", nativeQuery = true)
	List<EmployeeSheet> getPreviousMonthRecords(int month);
    
	@Query(value=" select * from employee_sheets es where es.emp_id=:empId and es.month=:month and es.year=:year ", nativeQuery = true)
	EmployeeSheet findByEmpIdAndMonthAndYear(Integer empId, Integer month, Integer year);

	@Query(value = "select" + " CASE " + "   WHEN :day = 1 THEN day1 " + "   WHEN :day = 2 THEN day2 "
			+ "   WHEN :day = 3 THEN day3 " + "   WHEN :day = 4 THEN day4 " + "   WHEN :day = 5 THEN day5 "
			+ "   WHEN :day = 6 THEN day6 " + "   WHEN :day = 7 THEN day7 " + "   WHEN :day = 8 THEN day8 "
			+ "   WHEN :day = 9 THEN day9 " + "   WHEN :day = 10 THEN day10 " + "   WHEN :day = 11 THEN day11 "
			+ "   WHEN :day = 12 THEN day12 " + "   WHEN :day = 13 THEN day13 " + "   WHEN :day = 14 THEN day14 "
			+ "   WHEN :day = 15 THEN day15 " + "   WHEN :day = 16 THEN day16 " + "   WHEN :day = 17 THEN day17 "
			+ "   WHEN :day = 18 THEN day18 " + "   WHEN :day = 19 THEN day19 " + "   WHEN :day = 20 THEN day20 "
			+ "   WHEN :day = 21 THEN day21 " + "   WHEN :day = 22 THEN day22 " + "   WHEN :day = 23 THEN day23 "
			+ "   WHEN :day = 24 THEN day24 " + "   WHEN :day = 25 THEN day25 " + "   WHEN :day = 26 THEN day26 "
			+ "   WHEN :day = 27 THEN day27 " + "   WHEN :day = 28 THEN day28 " + "   WHEN :day = 29 THEN day29 "
			+ "   WHEN :day = 30 THEN day30 " + "   WHEN :day = 31 THEN day31 " + "ELSE NULL END AS result "
			+ " from employee_sheets es where es.emp_id=:empId and es.month=:month and  es.year=:year ", nativeQuery = true)
	Map<String, String> getDayValue(@Param("empId") Integer empId, @Param("month") int month, @Param("day") int day,
			@Param("year") int year);

	List<EmployeeSheet> findByMonthAndYear(Integer month, Integer year);

	List<EmployeeSheet> findByMonthAndYearAndEmpId(Integer month, Integer year, Integer empId);

	@Query(value = "select es.emp_id from employee_sheets es where es.month=:month and  es.year=:year  ", nativeQuery = true)
	List<Integer> getEmployeeIdByMonthAndYear(@Param("month") int month, @Param("year") int year);


	@Query(value="select es from EmployeeSheet es where es.month=:month and es.year=:year and es.paydays!=0 ")
	List<EmployeeSheet> getByMonthAndYearAndPaydays(@Param("month") int month, @Param("year") int year);

	@Query(value = "select new com.au.dto.EmployeeSheetResponseDTO(" + "   es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) " + " from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " + " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ " where (:#{#employeeSheetRequestDTO.month} IS NULL OR es.month=:#{#employeeSheetRequestDTO.month})"
			+ " and (:#{#employeeSheetRequestDTO.year} IS NULL OR es.year=:#{#employeeSheetRequestDTO.year}) "
			+ " and (:#{#employeeSheetRequestDTO.school_id} IS NULL OR es.school_id=:#{#employeeSheetRequestDTO.school_id}) "
			+ " and (:#{#employeeSheetRequestDTO.dept_id} IS NULL OR  es.dept_id=:#{#employeeSheetRequestDTO.dept_id}) and et.empTypeShortName='CON' group by es.empCode ", nativeQuery = false)
	List<EmployeeSheetResponseDTO> getListOfEmployeeAttendenceConsultant(
			EmployeeSheetRequestDTO employeeSheetRequestDTO);

	
	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where CONCAT(IfNull(es.employeeSheetId,''),'',IfNull(es.empCode,''),'',IfNull(es.empId,''),'',IfNull(es.month,''),"
			+ "'',IfNull(es.school_id,''),'',IfNull(ed.employee_name,''),'',IfNull(es.year,''),'') LIKE %?1% "
			+ "and et.empTypeShortName!='CON' And es.month=?2 And es.year=?3 And es.school_id=?4 And es.dept_id=?5 group by es.empCode ")
	Page<Object> getAllDataFilteredByKeywordWoCon(Pageable pageable, Object keyword, Integer month, Integer year,
			Integer school_id, Integer dept_id);
	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where CONCAT(IfNull(es.employeeSheetId,''),'',IfNull(es.empCode,''),'',IfNull(es.empId,''),'',IfNull(es.month,''),"
			+ "'',IfNull(ed.employee_name,''),'',IfNull(es.year,''),'') LIKE %?1% "
			+ "and et.empTypeShortName!='CON' And es.month=?2 And es.year=?3 group by es.empCode ")
	Page<Object> getAllDataFilteredByKeywordWoConSclIdDeptId(Pageable pageable, Object keyword, Integer month, Integer year);

	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where CONCAT(IfNull(es.employeeSheetId,''),'',IfNull(es.empCode,''),'',IfNull(es.empId,''),'',IfNull(es.month,''),"
			+ "'',IfNull(es.school_id,''),'',IfNull(ed.employee_name,''),'',IfNull(es.year,''),'') LIKE %?1% "
			+ " And es.month=?2 And es.year=?3 And es.school_id=?4 And es.dept_id=?5 And et.empTypeShortName='CON' group by es.empCode ")
	Page<Object> getAllDataFilteredByKeywordWithCon(Pageable pageable, Object keyword, Integer month, Integer year,
			Integer school_id, Integer dept_id, String empTypeShortName);
	
	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where CONCAT(IfNull(es.employeeSheetId,''),'',IfNull(es.empCode,''),'',IfNull(es.empId,''),'',IfNull(es.month,''),"
			+ "'',IfNull(ed.employee_name,''),'',IfNull(ed.date_of_joining,''),'',IfNull(es.year,''),'') LIKE %?1% "
			+ " And es.month=?2 And es.year=?3 And et.empTypeShortName='CON' group by es.empCode ")
	Page<Object> getAllDataFilteredByKeywordWithConWoSclIdDeptId(Pageable pageable, Object keyword, Integer month, Integer year,String empTypeShortName);
	
	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where et.empTypeShortName='CON' And es.month=?1 And es.year=?2 And es.school_id=?3 And es.dept_id=?4 group by es.empCode ")
	Page<Object> getAllSortedDataWithCon(Pageable pageable1, Integer month, Integer year, Integer school_id,
			Integer dept_id, String empTypeShortName);
	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where et.empTypeShortName='CON' And es.month=?1 And es.year=?2 group by es.empCode ")
	Page<Object> getAllSortedKeywordWithConWoSclIdDeptId(Pageable pageable1, Integer month, Integer year, String empTypeShortName);
	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where et.empTypeShortName!='CON' And es.month=?1 And es.year=?2 And es.school_id=?3 And es.dept_id=?4 group by es.empCode ")
	Page<Object> getAllSortedDataWoCon(Pageable pageable1, Integer month, Integer year, Integer school_id,
			Integer dept_id);
	
	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where et.empTypeShortName!='CON' And es.month=?1 And es.year=?2 group by es.empCode ")
	Page<Object> getAllSortedDataWoConSclIdDeptId(Pageable pageable1, Integer month, Integer year);

	
	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where CONCAT(IfNull(es.employeeSheetId,''),'',IfNull(es.empCode,''),'',IfNull(es.empId,''),'',IfNull(es.month,''),"
			+ "'',IfNull(es.school_id,''),'',IfNull(ed.employee_name,''),'',IfNull(es.year,''),'') LIKE %?1% "
			+ " And es.month=?2 And es.year=?3 And es.school_id=?4 And et.empTypeShortName='CON' group by es.empCode ")
	Page<Object> getAllDataFilteredByKeywordWithConAndWithSclId(Pageable pageable, Object keyword, Integer month,
			Integer year, Integer school_id, String empTypeShortName);

	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where CONCAT(IfNull(es.employeeSheetId,''),'',IfNull(es.empCode,''),'',IfNull(es.empId,''),'',IfNull(es.month,''),"
			+ "'',IfNull(es.school_id,''),'',IfNull(ed.employee_name,''),'',IfNull(es.year,''),'') LIKE %?1% "
			+ " And es.month=?2 And es.year=?3 And es.dept_id=?4 And et.empTypeShortName='CON' group by es.empCode ")
	Page<Object> getAllDataFilteredByKeywordWithConAndWithDeptId(Pageable pageable, Object keyword, Integer month,
			Integer year, Integer dept_id, String empTypeShortName);

	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where CONCAT(IfNull(es.employeeSheetId,''),'',IfNull(es.empCode,''),'',IfNull(es.empId,''),'',IfNull(es.month,''),"
			+ "'',IfNull(es.school_id,''),'',IfNull(ed.employee_name,''),'',IfNull(es.year,''),'') LIKE %?1% "
			+ "and et.empTypeShortName!='CON' And es.month=?2 And es.year=?3 And es.school_id=?4 group by es.empCode ")
	Page<Object> getAllDataFilteredByKeywordWoConAndWithSclId(Pageable pageable, Object keyword, Integer month,
			Integer year, Integer school_id);

	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where CONCAT(IfNull(es.employeeSheetId,''),'',IfNull(es.empCode,''),'',IfNull(es.empId,''),'',IfNull(es.month,''),"
			+ "'',IfNull(es.school_id,''),'',IfNull(ed.employee_name,''),'',IfNull(es.year,''),'') LIKE %?1% "
			+ "and et.empTypeShortName!='CON' And es.month=?2 And es.year=?3 And es.dept_id=?4 group by es.empCode ")
	Page<Object> getAllDataFilteredByKeywordWoAndWithDeptId(Pageable pageable, Object keyword, Integer month,
			Integer year, Integer dept_id);

	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where et.empTypeShortName='CON' And es.month=?1 And es.year=?2 And es.school_id=?3 group by es.empCode ")
	Page<Object> getAllSortedKeywordWithConAndWithSclId(Pageable pageable, Integer month, Integer year,
			Integer school_id, String empTypeShortName);

	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where et.empTypeShortName='CON' And es.month=?1 And es.year=?2 And es.dept_id=?3 group by es.empCode ")
	Page<Object> getAllSortedKeywordWithConAndWithDeptId(Pageable pageable, Integer month, Integer year,
			Integer dept_id, String empTypeShortName);

	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where et.empTypeShortName!='CON' And es.month=?1 And es.year=?2 And es.school_id=?3 group by es.empCode ")
	Page<Object> getAllSortedDataWoConAndwithSclId(Pageable pageable1, Integer month, Integer year, Integer school_id);

	
	@Query(value = "SELECT NEW map(es.employeeSheetId as id,es.empId as empId,"
			+ "   es.empCode as empCode," + " ed.employee_name as employee_name,"
			+ "   ds.designation_short_name as designation," + "   s.school_name_short as school_name, ed.date_of_joining as date_of_joining,"
			+ "   d.dept_name_short as dept_name," + "   es.day1 as day1," + "   es.day2 as day2," + "   es.day3 as day3,"
			+ " es.day4 as day4," + " es.day5 as day5," + " es.day6 as day6," + " es.day7 as day7,"
			+ " es.day8 as day8," + " es.day9 as day9," + " es.day10 as day10," + " es.day11 as day11,"
			+ " es.day12 as day12," + " es.day13 as day13," + " es.day14 as day14," + " es.day15 as day15,"
			+ " es.day16 as day16," + " es.day17 as day17," + " es.day18 as day18," + " es.day19 as day19,"
			+ " es.day20 as day20," + " es.day21 as day21," + " es.day22 as day22," + " es.day23 as day23,"
			+ " es.day24 as day24," + " es.day25 as day25," + " es.day26 as day26," + " es.day27 as day27,"
			+ " es.day28 as day28," + " es.day29 as day29," + " es.day30 as day30," + " es.day31 as day31,"
			+ " es.paydays as paydays," + " es.presentdays as presentday," + " es.leavetaken as leaveTaken,"
			+ " es.absentdays as absentday," + " es.generalWO as generalWo," + " bt.startTime as startTime,"
			+ " bt.endTime as endTime, es.workingDays as workingDays ) from EmployeeSheet es "
			+ " left join Department d on d.dept_id=es.dept_id " 
			+ " left join Schools s on s.school_id=es.school_id"
			+ " left join Designation ds on ds.designation_id=es.designation_id "
			+ " left join EmployeeDetails ed on ed.emp_id=es.empId "
			+ " left join BiometricAttendance bt on bt.empId=es.empId "
			+ " left join EmployeeType et on et.empTypeId=ed.emp_type_id "
			+ "where et.empTypeShortName!='CON' And es.month=?1 And es.year=?2 And es.dept_id=?3 group by es.empCode ")
	Page<Object> getAllSortedDataWoConAndWithDeptId(Pageable pageable1, Integer month, Integer year, Integer dept_id);
	
	@Transactional
	@Modifying
	@Query(value="delete from EmployeeSheet es where es.month=:month and es.year=:year")
	void deleteByMonthAndYear(Integer month, Integer year);


}
