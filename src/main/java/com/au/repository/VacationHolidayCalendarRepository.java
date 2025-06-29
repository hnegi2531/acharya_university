package com.au.repository;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.VacationHolidayCalendar;

@Repository
@Transactional
public interface VacationHolidayCalendarRepository extends JpaRepository<VacationHolidayCalendar,Integer> {
	
	@Query(value = "select vhc from VacationHolidayCalendar vhc where vhc.active=true")
	public List<VacationHolidayCalendar> findAll1();
	
    @Query("SELECT new map(vhc.vacationId as id, vhc.permittedDays as permittedDays, vhc.fromDate as fromDate, "
            + "vhc.toDate as toDate, vhc.leaveId as leaveId, vhc.schoolId as schoolId, "
            + "vhc.frontendUseFromDate as frontendUseFromDate, vhc.frontendUseToDate as frontendUseToDate, "
            + "vhc.acYearId as acYearId, vhc.createdBy as createdBy,vhc.modifiedBy as modifiedBy, vhc.createdDate as createdDate, "
            + "vhc.modifiedDate as modifiedDate, vhc.createdUsername as createdUsername, vhc.modifiedUsername as modifiedUsername,"
            + "vhc.active as active,sc.school_name as school_name, sc.school_name_short as school_name_short,"
            + "ac.ac_year as ac_year, lt.leave_type as leave_type, lt.leave_type_short as leave_type_short) "
            + "FROM VacationHolidayCalendar vhc "
            + "left join Schools sc on sc.school_id=vhc.schoolId "
            + "left join Academic_year ac on ac.ac_year_id=vhc.acYearId "
            + "left join LeaveType lt on lt.leave_id=vhc.leaveId "
		+ "Where CONCAT(IfNull(vhc.fromDate,''),'',IfNull(vhc.toDate,''),'',IfNull(vhc.createdDate,''),'',IfNull(vhc.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

    @Query("SELECT new map(vhc.vacationId as id, vhc.permittedDays as permittedDays, vhc.fromDate as fromDate, "
            + "vhc.toDate as toDate, vhc.leaveId as leaveId, vhc.schoolId as schoolId,"
            + "vhc.frontendUseFromDate as frontendUseFromDate, vhc.frontendUseToDate as frontendUseToDate, "
            + "vhc.acYearId as acYearId, vhc.createdBy as createdBy,vhc.modifiedBy as modifiedBy, vhc.createdDate as createdDate, "
            + "vhc.modifiedDate as modifiedDate, vhc.createdUsername as createdUsername, vhc.modifiedUsername as modifiedUsername,"
            + "vhc.active as active,sc.school_name as school_name, sc.school_name_short as school_name_short,"
            + "ac.ac_year as ac_year, lt.leave_type as leave_type, lt.leave_type_short as leave_type_short) "
            + "FROM VacationHolidayCalendar vhc "
            + "left join Schools sc on sc.school_id=vhc.schoolId "
            + "left join Academic_year ac on ac.ac_year_id=vhc.acYearId "
            + "left join LeaveType lt on lt.leave_id=vhc.leaveId ")
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update VacationHolidayCalendar vhc set vhc.active=false where vhc.vacationId=?1")
	public void updateVacationHolidayCalendar(Integer vacationId);

	@Modifying
	@Query(value = "update VacationHolidayCalendar vhc set vhc.active=true where vhc.vacationId=?1")
	public void updateVacationHolidayCalendar1(Integer vacationId);

	@Query(value = "SELECT count(*) FROM vacation_holiday_calendar where "
			+ "(STR_TO_DATE(?1,'%d-%m-%Y') "
			+ "between STR_TO_DATE(from_date,'%d-%m-%Y') and STR_TO_DATE(to_date,'%d-%m-%Y') "
			+ "or "
			+ "STR_TO_DATE(?2,'%d-%m-%Y') "
			+ "between STR_TO_DATE(from_date,'%d-%m-%Y') and STR_TO_DATE(to_date,'%d-%m-%Y')) and school_id=?3 and active=true", nativeQuery=true)
	public Integer checkValidation(String fromDate, String toDate, Integer schoolId);

	@Query(value = "SELECT count(*) FROM vacation_holiday_calendar vhc " +
			"WHERE (STR_TO_DATE(:fromDate, '%d-%m-%Y') BETWEEN STR_TO_DATE(vhc.from_date, '%Y-%m-%d') AND STR_TO_DATE(vhc.to_date, '%Y-%m-%d')) " +
			"AND (STR_TO_DATE(:toDate, '%d-%m-%Y') BETWEEN STR_TO_DATE(vhc.from_date, '%Y-%m-%d') AND STR_TO_DATE(vhc.to_date, '%Y-%m-%d')) " +
			"AND vhc.leave_id = :leaveId AND vhc.school_id = :schoolId AND vhc.active = TRUE", nativeQuery=true)
    Integer getFromDateAndSchoolIdAndLeaveId(String fromDate, String toDate, Integer schoolId, Integer leaveId);
}
