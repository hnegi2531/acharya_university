package com.au.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.HolidayCalender;

@Repository
@Transactional
public interface HolidayCalenderRepository extends JpaRepository<HolidayCalender, Integer> {

	public Boolean existsByholidayName(String holidayName);

	@Query(value = "select h from HolidayCalender h where h.active=true")
	public List<HolidayCalender> findAll1();

	@Modifying
	@Query(value = "update HolidayCalender h set h.active=false where h.holidayCalendarId=?1")
	public void updateHolidayCalender(Integer id);

	@Modifying
	@Query(value = "update HolidayCalender h set h.active=true where h.holidayCalendarId=?1")
	public void updateHolidayCalender1(Integer id);

//	@Query(value = "SELECT count(*) FROM holiday_calender where leave_id=?1 and school_id in (?2) and job_type_id in (?3) and dept_id in (?4)",nativeQuery = true)
//	public Integer fetchCountByHolidayTypeSchoolJobType(Integer leave_id,List<Integer> school_id,List<Integer> job_type_id, List<Integer> dept_id);

	@Query(value = "SELECT count(*) FROM holiday_calender where leave_id=?1 and school_id in (?2) and from_date in (?3)", nativeQuery = true)
	public Integer fetchCountByHolidayTypeSchoolJobType(Integer leave_id, List<Integer> school_id, Date fromDate);

	@Query(value = "SELECT hc.from_date,jt.job_type,s.school_name_short,hc.holiday_calendar_id,hc.holiday_name,lt.leave_type,hc.day as day,"
			+ "hc.created_username,hc.created_date,hc.active FROM " + "holiday_calender hc left join "
			+ "	leave_type lt on hc.leave_id=lt.leave_id " + "    left join schools s on hc.school_id=s.school_id "
			+ "    left join job_type jt on hc.job_type_id = jt.job_type_id"
			+ "    where  not  lt.leave_type = \"Declared Holiday\"", nativeQuery = true)
	public List<Map<String, Object>> fetchHolidayCalenderDetails();

	@Query(value = "SELECT hc.from_date,jt.job_type,s.school_name_short,hc.holiday_calendar_id,hc.holiday_name,lt.leave_type,hc.day as day,"
			+ "hc.created_username,hc.created_date,hc.active FROM " + "holiday_calender hc left join "
			+ "leave_type lt on hc.leave_id=lt.leave_id " + "left join schools s on hc.school_id=s.school_id "
			+ "left join job_type jt on hc.job_type_id = jt.job_type_id "
			+ "where lt.leave_type = \"Declared Holiday\"", nativeQuery = true)
	public List<Map<String, Object>> fetchHolidayCalenderDetails1();

	@Query(value = "select hc.holiday_calendar_id as id,hc.leave_id as leave_id,hc.from_date as from_date,hc.job_type_id as job_type_id,"
			+ "s.school_name as school_name,s.school_name_short as school_name_short,hc.day as day,hc.holiday_description as holiday_description,"
			+ "hc.days_count as days_count,hc.holiday_name as holiday_name,hc.created_by as created_by,hc.modified_by as modified_by,"
			+ "(select group_concat(job_type.job_short_name) from job_type where position(',' + cast(job_type.job_type_id as char) + ',' in concat(',',hc.job_type_id,',')) >0) as job_type_short_name,"
			+ "hc.created_date as created_date,hc.modified_date as modified_date,hc.active as active,hc.leave_type_short as leave_type_short,hc.leave_type as leave_type,"
			+ "hc.created_username as created_username,hc.modified_username as modified_username,hc.school_id as school_id "
			+ "from holiday_calender hc left join schools s on hc.school_id=s.school_id "
			+ "where CONCAT(IfNull(hc.holiday_calendar_id,''),'',IfNull(hc.leave_id,''),'',IfNull(hc.from_date,''),'',IfNull(hc.school_id,''),"
			+ "'',IfNull(hc.leave_type,''),'',IfNull(hc.days_count,''),'',IfNull(hc.holiday_name,''),'',IfNull(hc.leave_type_short,''),"
			+ "'',IfNull(hc.job_type_id,''),'',IfNull(hc.created_by,''),'',IfNull(hc.created_date,'')) LIKE %?1%", nativeQuery = true)
	public List<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "select hc.holiday_calendar_id as id,hc.leave_id as leave_id,hc.from_date as from_date,hc.job_type_id as job_type_id,"
			+ "s.school_name as school_name,s.school_name_short as school_name_short,hc.day as day,hc.holiday_description as holiday_description,"
			+ "hc.days_count as days_count,hc.holiday_name as holiday_name,hc.created_by as created_by,hc.modified_by as modified_by,"
			+ "(select group_concat(job_type.job_short_name) from job_type where position(',' + cast(job_type.job_type_id as char) + ',' in concat(',',hc.job_type_id,',')) >0) as job_type_short_name,"
			+ "hc.created_date as created_date,hc.modified_date as modified_date,hc.active as active,hc.leave_type_short as leave_type_short,hc.leave_type as leave_type,"
			+ "hc.created_username as created_username,hc.modified_username as modified_username,hc.school_id as school_id "
			+ "from holiday_calender hc left join schools s on hc.school_id=s.school_id", nativeQuery = true)
	public List<Map<String, Object>> getAllSortedData(Pageable pageable);

	@Query(value = "SELECT count(*) FROM holiday_calender where holiday_name=?1 and from_date=?2", nativeQuery = true)
	public Integer countHoliday(String holiday_name, Date fromDate);

	@Query(value = "SELECT count(*) FROM holiday_calender where  from_date=?1", nativeQuery = true)
	public Integer countHolidayByDate(Date fromDate);

	@Query(value = "SELECT hc.from_date as from_date,hc.day as day,hc.holiday_name as holiday_name,hc.leave_type as leave_type FROM holiday_calender hc "
			+ "where hc.school_id=?1 And (Month(hc.from_date)=Month(now()) And Year(hc.from_date)=Year(now())) And hc.leave_type NOT LIKE '%Restricted Holiday%' And hc.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchDeclaredHolidayDetailsOnSchooId(Integer schoolId);

	@Query(value = "SELECT hc.from_date as from_date,hc.day as day,hc.holiday_name as holiday_name,hc.leave_type as leave_type FROM holiday_calender hc "
			+ "where hc.school_id is null And  (Month(hc.from_date)=Month(now()) And Year(hc.from_date)=Year(now())) And leave_type NOT LIKE '%Restricted Holiday%' And hc.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchGeneralHolidayDetails();

	@Query(value = "SELECT date(hc.from_date) as from_date,hc.holiday_name as holiday_name,hc.leave_type as leave_type FROM holiday_calender hc "
			+ "where hc.school_id=?1 And (Month(hc.from_date)=Month(now()) And Year(hc.from_date)=Year(now())) And hc.leave_type NOT LIKE '%Restricted Holiday%' And hc.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchDeclaredHolidayDetailsForAcademicCalendarOnSchooId(Integer schoolId);

	@Query(value = "SELECT date(hc.from_date) as from_date,hc.holiday_name as holiday_name,hc.leave_type as leave_type FROM holiday_calender hc "
			+ "where hc.school_id is null And  (Month(hc.from_date)=Month(now()) And Year(hc.from_date)=Year(now())) And leave_type NOT LIKE '%Restricted Holiday%' And hc.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchGeneralHolidayDetailsForAcademicCalendar();

	@Query(value = "Select hc.leave_type_short from holiday_calender hc where hc.from_date=?1 and hc.active=true", nativeQuery = true)
	public Optional<?> getleaveType(Date date);

	
	@Query(value = "Select hc.leave_type_short from holiday_calender hc where date(hc.from_date)=STR_TO_DATE(:date, '%Y-%m-%d') and hc.active=true limit 1",nativeQuery = true)
	public String getleaveType(String date);

	@Query(value = "SELECT COUNT(*) FROM holiday_calender h WHERE MONTH(h.from_date)=:currentMonth and YEAR(h.from_date)=:currentYear and h.leave_type_short!='DH' ", nativeQuery = true)
	public Integer countTotalHolidayInAMonthAndYear(@Param("currentMonth") int currentMonth,
			@Param("currentYear") int currentYear);

	@Query(value = "Select hc.leave_type_short from holiday_calender hc where DATE(STR_TO_DATE(hc.from_date,'%Y-%m-%d'))=STR_TO_DATE(:date, '%d-%m-%Y') and hc.active=1 ", nativeQuery = true)
	public String getHolidayFromFromDate(@Param("date") String date);

	@Query(value = "select hc.holiday_calendar_id as holiday_calendar_id,hc.from_date as from_date,hc.days_count as days_count,"
			+ "hc.holiday_name as holiday_name,hc.job_type_id as job_type_id,hc.school_id as school_id,hc.leave_id as leave_id,"
			+ "hc.dept_id as dept_id,s.school_name as school_name,s.school_name_short as school_name_short,hc.holiday_description as holiday_description,"
			+ "hc.leave_type_short as leave_type_short,hc.leave_type as leave_type,"
			+ "concat(lt.leave_type_short,'-',hc.holiday_name)  as concat_holiday_name " 
			+ "from holiday_calender hc "
			+ "left join schools s on hc.school_id=s.school_id " 
			+ "left join leave_type lt on hc.leave_id=lt.leave_id "
			+ "where hc.active=true and hc.school_id=?1 ", nativeQuery = true)
	public List<Map<String, Object>> listAllHolidayCalenderData(Integer schoolId);
	
	@Query(value = "select hc.holiday_calendar_id as holiday_calendar_id,hc.from_date as from_date,hc.days_count as days_count,"
			+ "hc.holiday_name as holiday_name,hc.job_type_id as job_type_id,hc.school_id as school_id,hc.leave_id as leave_id,"
			+ "hc.dept_id as dept_id,s.school_name as school_name,s.school_name_short as school_name_short,hc.holiday_description as holiday_description,"
			+ "hc.leave_type_short as leave_type_short,hc.leave_type as leave_type,"
			+ "concat(lt.leave_type_short,'-',hc.holiday_name)  as concat_holiday_name " 
			+ "from holiday_calender hc "
			+ "left join schools s on hc.school_id=s.school_id " 
			+ "left join leave_type lt on hc.leave_id=lt.leave_id "
			+ "where hc.active=true and hc.leave_type_short not in ('DH') ", nativeQuery = true)
	public List<Map<String, Object>> listAllHolidayCalenderDataForStudentWithOutSchoolId();
	
	@Query(value = "select hc.holiday_calendar_id as holiday_calendar_id,hc.from_date as from_date,hc.days_count as days_count,"
			+ "hc.holiday_name as holiday_name,hc.job_type_id as job_type_id,hc.school_id as school_id,hc.leave_id as leave_id,"
			+ "hc.dept_id as dept_id,s.school_name as school_name,s.school_name_short as school_name_short,hc.holiday_description as holiday_description,"
			+ "hc.leave_type_short as leave_type_short,hc.leave_type as leave_type,"
			+ "concat(lt.leave_type_short,'-',hc.holiday_name)  as concat_holiday_name " 
			+ "from holiday_calender hc "
			+ "left join schools s on hc.school_id=s.school_id " 
			+ "left join leave_type lt on hc.leave_id=lt.leave_id "
			+ "where hc.active=true and hc.leave_type_short not in ('DH') ", nativeQuery = true)
	public List<Map<String, Object>> listAllHolidayCalenderDataWithOutSchoolId();

	
	@Query(value = "Select * from holiday_calender hc where DATE(hc.from_date)=:date and hc.active=1 limit 1  ", nativeQuery = true)
	public HolidayCalender getHolidayDetailsFromDate(@Param("date") String date);

	@Query(value = "SELECT  hc.leave_type_short "
			+ "FROM holiday_calender hc  left join leave_type lt on lt.leave_id=hc.leave_id "
			+ "WHERE DATE(hc.from_date)=:date  and hc.active=1 and find_in_set(:deptId,hc.dept_id) and find_in_set(:jobTypeId,hc.job_type_id) and hc.school_id=:schoolId and lt.type='Holiday' ",nativeQuery = true)
	public String getHolidayFromFromDate(@Param("date") String date,@Param("deptId") Integer deptId,@Param("jobTypeId") Integer jobTypeId,Integer schoolId);

	@Query(value = "SELECT  hc.leave_type_short "
			+ "FROM holiday_calender hc left join leave_type lt on lt.leave_id=hc.leave_id "
			+ "WHERE DATE(hc.from_date)=:date  and hc.active=1 and (hc.dept_id is null or hc.dept_id='' ) and ( hc.job_type_id is null or hc.job_type_id='' ) and (hc.school_id is null or hc.school_id='' ) and lt.type='Holiday' ",nativeQuery = true)
	public String getHolidayFromFromDateForGH(@Param("date") String date);

	@Query(value = "Select hc.leave_type_short from holiday_calender hc where DATE(hc.from_date)<:date and hc.active=1 order by holiday_calendar_id desc limit 7  ", nativeQuery = true)
	public List<String> validateClubbedLeavesWithHolidays(String date);
	
	@Query(value = "Select * from holiday_calender hc where DATE(hc.from_date)=DATE_FORMAT(STR_TO_DATE(:date,'%d-%m-%Y'), '%Y-%m-%d') and hc.leave_id=:leaveId and hc.active=1 limit 1  ", nativeQuery = true)
	public HolidayCalender getHolidayDetailsFromDateAndLeaveId(String date, Integer leaveId);

	
	@Query(value = "select hc.holiday_calendar_id as holiday_calendar_id,hc.from_date as from_date,hc.days_count as days_count,"
			+ "hc.holiday_name as holiday_name,hc.job_type_id as job_type_id,hc.school_id as school_id,hc.leave_id as leave_id,"
			+ "hc.dept_id as dept_id,s.school_name as school_name,s.school_name_short as school_name_short,hc.holiday_description as holiday_description,"
			+ "hc.leave_type_short as leave_type_short,hc.leave_type as leave_type,"
			+ "concat(lt.leave_type_short,'-',hc.holiday_name)  as concat_holiday_name " 
			+ "from holiday_calender hc "
			+ "left join schools s on hc.school_id=s.school_id " 
			+ "left join leave_type lt on hc.leave_id=lt.leave_id "
			+ "where hc.active=true and hc.school_id=:schoolId And (FIND_IN_SET(:deptId, hc.dept_id) > 0) "
			+ "And (FIND_IN_SET(:jobTypeId, hc.job_type_id) > 0)", nativeQuery = true)
	public List<Map<String, Object>> listAllHolidayCalenderDataWith(Integer schoolId, Integer deptId, Integer jobTypeId);
}
