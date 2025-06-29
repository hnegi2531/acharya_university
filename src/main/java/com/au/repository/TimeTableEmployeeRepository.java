package com.au.repository;

import java.util.Date;
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

import com.au.model.TimeTableEmployee;

@Repository
@Transactional
public interface TimeTableEmployeeRepository extends JpaRepository<TimeTableEmployee, Integer>{
	
	@Modifying
	@Query(value = "update TimeTableEmployee h set h.active=false where h.time_table_employee_id in ?1")
	public void updateTimeTable(List<Integer> time_table_employee_ids);

	@Modifying
	@Query(value = "update TimeTableEmployee h set h.active=true where h.time_table_employee_id = ?1")
	public void updateTimeTable1(List<Integer> time_table_employee_ids);
	
	@Query(value = "select count(*) from time_table_employee tte left join time_table tt on tt.time_table_id=tte.time_table_id "
			+ "where tte.emp_id in ?1 and tte.time_slots_id=?2 and Date(tte.selected_date)=Date(?3) and tte.active=true",nativeQuery=true)
	public Integer getCount(List<Integer> emp_id, Integer time_slots_id, Date date);
	
	@Query(value = "select count(*) from time_table_employee tte left join time_table tt on tt.time_table_id=tte.time_table_id "
			+ "where  tte.section_assignment_id=?1 and tte.time_slots_id=?2 and Date(tte.selected_date)=Date(?3) and tte.active=true",nativeQuery=true)
	public Integer getCount1( Integer section_assignment_id, Integer time_slots_id, Date date);
	
	@Query(value = "select count(*) from time_table_employee tte left join time_table tt on tt.time_table_id=tte.time_table_id "
			+ "where tte.emp_id in ?1 and tte.time_slots_id=?2 and Date(tte.selected_date)=Date(?3) and tte.active=true",nativeQuery=true)
	public Integer getCount2(List<Integer> emp_id, Integer time_slots_id, Date date);
	
	@Query(value = "select count(*) from time_table_employee tte left join time_table tt on tt.time_table_id=tte.time_table_id "
			+ "where tte.section_assignment_id=?1 and tte.time_slots_id=?2 and Date(tte.selected_date)=Date(?3) and tte.active=true",nativeQuery=true)
	public Integer getCount3( Integer section_assignment_id, Integer time_slots_id, Date date);
	
	
	@Query(value = "select count(*) from TimeTableEmployee tte Inner join TimeTable tt on tt.time_table_id=tte.time_table_id "
			+ "where tte.emp_id=?1 and tte.selected_date=STR_TO_DATE(?2,'%Y-%m-%d') and tte.active=true")
	public Integer getCountOfEmployeeAndSelectedDate(Integer emp_id, String formattedDate);

	@Query(value = "Select tte.time_table_employee_id as id,tte.time_table_id as time_table_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.interval_type_id as intervalTypeId,tte.emp_id as emp_id,ir.manual_room_no as manual_room_no, tte.active as active,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,tt.current_year as current_year,tt.current_sem as current_sem,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,ir.roomcode as roomcode,"
			+ "tt.floor_id as floor_id,tte.selected_date as selected_date,tte.subject_assignment_id as subject_assignment_id,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "c.course_code as course_code,c.course_short_name as course_short_name,pt.program_type_name as program_type_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,"
			+ "tit.interval_type_short as intervalTypeShort,emp.employee_name as employee_name From time_table_employee tte "
			+ "Left join time_table tt on tte.time_table_id=tt.time_table_id "
			+ "Left join academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join schools sch on sch.school_id=tt.school_id "
			+ "Left join program pr on pr.program_id=tt.program_id "
			+ "Left join program_assignment pa on pa.program_id=pr.program_id and pa.school_id=sch.school_id "
			+ "Left join program_type pt on pt.program_type_id=pa.program_type_id "
			+ "Left join program_specialization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=tt.interval_type_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join section_assignment seca on seca.section_assignment_id=tt.section_assignment_id "
			+ "Left join section s on s.section_id=seca.section_id "
			+ "Left join batch_assignment ba on ba.batch_assignment_id=tt.batch_assignment_id 	"
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Left join subject_assignment sa on sa.subjet_assign_id=tte.subject_assignment_id "
			+ "Left join course_assignment ca on ca.course_assignment_id=sa.course_assignment_id "
			+ "Left join course c on c.course_id=ca.course_id "
			+ "where tte.emp_id=?1 and "
			+ "tte.selected_date = STR_TO_DATE(?2,'%Y-%m-%d') and tte.active=true", nativeQuery=true)
	public List<Map<String, Object>> getEmployeeTimeTableSchedule(Integer emp_id, String formattedDate);
	
	@Query(value = "select Distinct tte.emp_id from TimeTableEmployee tte "
			+ "where tte.time_slots_id=?2 and tte.selected_date=?1 and tte.active=true")
	public List<Integer> getEmplIdsFromTimeTableEmployee(Date date,Integer time_slots_id);
	
	@Query(value = "select sum(ts.duration) as duration,emp.emp_id as emp_id,emp.employee_name as employee_name,et.emp_type as emp_type,"
			+ "month(tte.selected_date) as month  FROM time_table_employee tte "
			+ "inner join time_slots ts on ts.time_slots_id=tte.time_slots_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join employee_type et on et.emp_type_id=emp.emp_type_id "
			+ "WHERE (month(tte.selected_date) between month(:from_month) And month(:to_month)) and "
			+ "year(tte.selected_date) =year(:from_month) and tte.emp_id=:emp_id and tte.active=true GROUP BY month(tte.selected_date)",nativeQuery=true)
	public List<Map<String,Object>> getEmployeeMonthlyWorkedHours(Date from_month,Date to_month,Integer emp_id);
	
//	@Query(value = "select sum(ts.duration) FROM time_table_employee tte inner join time_slots ts on ts.time_slots_id=tte.time_slots_id "
//			+ "WHERE tte.selected_date=?1 and tte.emp_id=?2 and tte.active=true",nativeQuery=true)
//	public Integer getEmployeeMonthlyWorkedHours(Date month, Integer emp_id);
	
	@Query(value = "select tte.selected_date from time_table_employee tte where tte.emp_id=?1 and tte.selected_date Like '%?2%' ",nativeQuery=true)
	public Date getselecteddate(Integer emp_id, String selected_date);
	
	@Query(value = "select DISTINCT(tte.subject_assignment_id) from time_table_employee tte where month(tte.selected_date)=month(:date1) "
			+ "and year(tte.selected_date) =year(:date1) and tte.emp_id=:emp_id and tte.active=true",nativeQuery=true)
	public List<Integer> getAllEmployeesSubjectFromTimeTable(Date date1, Integer emp_id);
	
	@Query(value = "select sum(ts.duration) as duration,c.course_name as course_name,tte.subject_assignment_id as subject_assignment_id,"
			+ "day(tte.selected_date) as day  FROM time_table_employee tte "
			+ "inner join time_slots ts on ts.time_slots_id=tte.time_slots_id "
			+ "Left join subject_assignment sa on sa.subjet_assign_id=tte.subject_assignment_id "
			+ "Left join course c on c.course_id=sa.course_id "
			+ "WHERE day(tte.selected_date) between day(:from_date) And day(:to_date) and "
			+ "month(tte.selected_date) =month(:from_date) and "
			+ "year(tte.selected_date) =year(:from_date) and tte.subject_assignment_id=:subject_assignment_id and tte.emp_id=:emp_id "
			+ "and tte.active=true GROUP BY day(tte.selected_date)",nativeQuery=true)
	public List<Map<String,Object>> getEmployeeSubjectDailyWorkedHours(Date from_date,Date to_date,Integer subject_assignment_id, Integer emp_id);
	
	@Query(value = "select sum(ts.duration) as duration,emp.emp_id as emp_id,emp.employee_name as employee_name,"
			+ "c.course_name as course_name,tte.subject_assignment_id as subject_assignment_id,"
			+ "day(tte.selected_date) as day  FROM time_table_employee tte "
			+ "inner join time_slots ts on ts.time_slots_id=tte.time_slots_id "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id "
			+ "Left join subject_assignment sa on sa.subjet_assign_id=tte.subject_assignment_id "
			+ "Left join course c on c.course_id=sa.course_id "
			+ "WHERE day(tte.selected_date) between day(:from_date) And day(:to_date) and "
			+ "month(tte.selected_date) =month(:from_date) and "
			+ "year(tte.selected_date) =year(:from_date) and tte.emp_id=:emp_id and tte.subject_assignment_id=:s_id "
			+ "and tte.active=true GROUP BY day(tte.selected_date)",nativeQuery=true)
	public List<Map<String,Object>> getEmployeesDailyWorkedHours(Date from_date,Date to_date, Integer emp_id, Integer s_id);
	
	@Query(value = "Select tte.time_table_employee_id as id,tte.time_table_id as time_table_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.interval_type_id as intervalTypeId,tte.emp_id as emp_id,ir.manual_room_no as manual_room_no,tte.active as active,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tsl.duration as duration,"
			+ "tt.floor_id as floor_id,tte.selected_date as selected_date,tte.subject_assignment_id as subject_assignment_id,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "c.course_code as course_code,c.course_short_name as course_short_name,ir.roomcode as roomcode,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,emp.empcode as empcode,tt.current_sem as current_sem,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,tt.current_year as current_year,"
			+ "tit.intervalTypeShort as intervalTypeShort,emp.employee_name as employee_name From TimeTableEmployee tte "
			+ "Left join TimeTable tt on tte.time_table_id=tt.time_table_id "
			+ "Left join Academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join Schools sch on sch.school_id=tt.school_id "
			+ "Left join Program pr on pr.program_id=tt.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join TimeIntervalTypes tit on tit.intervalTypeId=tt.interval_type_id "
			+ "Left join EmployeeDetails emp on emp.emp_id=tte.emp_id "
			+ "Left join SectionAssignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join Section s on s.section_id=seca.section_id "
			+ "Left join BatchAssignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join Batch b on b.batch_id=ba.batch_id "
			+ "Left join InfrastructureRooms ir on ir.room_id=tt.room_id "
			+ "Left join TimeSlots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Left join SubjectAssignment sa on sa.subjetAssignId=tte.subject_assignment_id "
			+ "Left join Course c on c.course_id=sa.course_id where tte.emp_id=?1 and tte.selected_date = ?2 and tte.subject_assignment_id=?3")
	public List<Map<String, Object>> getEmployeesDailyWorkedHours(Integer emp_id, Date selected_date, Integer subject_assignment_id);
	
	@Query(value = "select count(*) as count, day(tte.selected_date) as day, tt.room_id as room_id,"
			+ "ts.time_slots_id as time_slots_id,concat(IfNull(ts.starting_time,''),'-',IfNull(ts.ending_time,'')) as slot,"
			+ "ir.manual_room_no as manual_room_no, ir.roomcode as roomcode FROM time_table_employee tte "
			+ "left join time_table tt on tt.time_table_id=tte.time_table_id "
			+ "left join time_slots ts on ts.time_slots_id=tte.time_slots_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "WHERE day(tte.selected_date) between day(:from_date) And day(:to_date) and "
			+ "month(tte.selected_date) =month(:from_date) and "
			+ "year(tte.selected_date) =year(:from_date) and tt.room_id=:room_id "
			+ "and tte.active=true GROUP BY day(tte.selected_date)",nativeQuery=true)
	public List<Map<String,Object>> getTimeTableDataRoomWiseByFacilityType(Date from_date,Date to_date, Integer room_id);
	
	@Query(value = "Select new map(tte.time_table_employee_id as id,tte.time_table_id as time_table_id,tt.from_date as from_date,tt.to_date as to_date,tt.is_online as is_online,"
			+ "tt.interval_type_id as intervalTypeId,tte.emp_id as emp_id,ir.manual_room_no as manual_room_no,tte.active as active,"
			+ "tt.class_interval as class_interval,tt.intervals as intervals,tt.section_assignment_id as section_assignment_id,"
			+ "tt.time_slots_id as time_slots_id,tt.week_day as week_day,tt.block_id as block_id,tsl.duration as duration,"
			+ "tt.floor_id as floor_id,tte.selected_date as selected_date,tte.subject_assignment_id as subject_assignment_id,"
			+ "tt.year as year,ay.ac_year as ac_year,sch.school_name_short as school_name_short,c.course_name as course_name,"
			+ "c.course_code as course_code,c.course_short_name as course_short_name,ir.roomcode as roomcode,tt.room_id as room_id,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "s.section_name as section_name,b.batch_name as batch_name,emp.empcode as empcode,tt.current_sem as current_sem,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots,tt.current_year as current_year,"
			+ "tit.intervalTypeShort as intervalTypeShort,emp.employee_name as employee_name) From TimeTableEmployee tte "
			+ "Left join TimeTable tt on tte.time_table_id=tt.time_table_id "
			+ "Left join Academic_year ay on ay.ac_year_id=tt.ac_year_id "
			+ "Left join Schools sch on sch.school_id=tt.school_id "
			+ "Left join Program pr on pr.program_id=tt.program_id "
			+ "Left join ProgramSpecilization ps on ps.program_specialization_id=tt.program_specialization_id "
			+ "Left join TimeIntervalTypes tit on tit.intervalTypeId=tt.interval_type_id "
			+ "Left join EmployeeDetails emp on emp.emp_id=tte.emp_id "
			+ "Left join SectionAssignment seca on seca.section_assignment_id=tt.section_assignment_id "	
			+ "Left join Section s on s.section_id=seca.section_id "
			+ "Left join BatchAssignment ba on ba.batch_assignment_id=tt.batch_assignment_id "	
			+ "Left join Batch b on b.batch_id=ba.batch_id "
			+ "Left join InfrastructureRooms ir on ir.room_id=tt.room_id "
			+ "Left join TimeSlots tsl on tsl.time_slots_id=tt.time_slots_id "
			+ "Left join SubjectAssignment sa on sa.subjetAssignId=tte.subject_assignment_id "
			+ "Left join Course c on c.course_id=sa.course_id where "
			+ "month(tte.selected_date) =month(:from_date) and "
			+ "year(tte.selected_date) =year(:from_date) and tt.room_id=:room_id")
	public List<HashMap<Object, Object>> getTimeTableDataRoomWiseByFacilityType1(Date from_date, Integer room_id);
	
	@Query(value = "select day(tte.selected_date) as day, tt.room_id as room_id,"
			+ "tte.time_slots_id as time_slots_id,concat(IfNull(ts.starting_time,''),'-',IfNull(ts.ending_time,'')) as slot,"
			+ "ir.manual_room_no as manual_room_no, ir.roomcode as roomcode FROM time_table_employee tte "
			+ "left join time_table tt on tt.time_table_id=tte.time_table_id "
			+ "left join time_slots ts on ts.time_slots_id=tte.time_slots_id "
			+ "Left join infrastructure_rooms ir on ir.room_id=tt.room_id "
			+ "WHERE day(tte.selected_date) between day(:from_date) And day(:to_date) and "
			+ "month(tte.selected_date) =month(:from_date) and "
			+ "year(tte.selected_date) =year(:from_date) and tt.room_id=:room_id "
			+ "and tte.active=true GROUP BY tte.time_table_employee_id",nativeQuery=true)
	public List<Map<String,Object>> getTimeTableData(Date from_date,Date to_date, Integer room_id);
	
	@Query(value ="Select tte.time_table_employee_id as id,tte.time_table_id as time_table_id,tte.emp_id as emp_id,"
			+ "emp.employee_name as employee_name,emp.empcode as empcode From time_table_employee tte "
			+ "Left join employee_details emp on emp.emp_id=tte.emp_id Where tte.time_table_id=?1 and tte.active=true",nativeQuery=true)
	public List<Map<String, Object>> timeTableEmployeeDetailsOnTimeTableId(Integer time_table_id);

	@Modifying
	@Query(value = "update TimeTableEmployee tte set tte.active=false where tte.time_table_id in ?1")
	public void deactivateTimeTableEmployeeByTimeTableId(List<Integer> time_table_ids);
	
	
	@Modifying
	@Query(value = "update TimeTableEmployee tte set tte.active=true where tte.time_table_id in ?1")
	public void deactivateTimeTableEmployeeByTimeTableId1(List<Integer> time_table_ids);

	@Query(value="select distinct new map(ay.ac_year_id as ac_year_id, ay.ac_year as ac_year) from TimeTableEmployee tte "
			+ " left join TimeTable t on t.time_table_id=tte.time_table_id"
			+ " inner join Academic_year ay on ay.ac_year_id=t.ac_year_id   "
			+ " where tte.emp_id=:empId")
	public List<HashMap<String,Object>> getAcademicYearsByEmployeeId(@Param("empId") Integer empId);

	
	@Query(value = "select Distinct tte.emp_id from time_table_employee tte where tte.time_slots_id=?2 and tte.selected_date=STR_TO_DATE(?1, '%d-%m-%Y') and tte.active=true",nativeQuery=true)
	public List<Integer> getEmplIdsFromTimeTableEmployeeForInternals(String date,Integer time_slots_id);
	
	@Query(value = "select count(*) from time_table_employee tte left join time_table tt on tt.time_table_id=tte.time_table_id "
			+ "where  tte.batch_assignment_id=?1 and tte.time_slots_id=?2 and Date(tte.selected_date)=Date(?3) and tte.active=true",nativeQuery=true)
	public Integer getCountByBatchAssignment(Integer batch_assignment_id, Integer time_slots_id, Date date);

	@Query(value = "select count(*) from TimeTableEmployee tte Inner join TimeTable tt on tt.time_table_id=tte.time_table_id "
			+ "where tte.emp_id in ?1 and tte.section_assignment_id=?2 and tte.time_slots_id=?3 and tt.selected_date=?4 and tte.active=true")
	public Integer getCount3(List<Integer> emp_id, Integer section_assignment_id, Integer time_slots_id, Date date);

}
