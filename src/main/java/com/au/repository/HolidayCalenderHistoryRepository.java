package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.au.model.HolidayCalender;
import com.au.model.HolidayCalenderHistory;

public interface HolidayCalenderHistoryRepository extends JpaRepository<HolidayCalenderHistory, Integer>{

	@Query(value = "select hch from HolidayCalenderHistory hch where hch.active=true")
	public List<HolidayCalenderHistory> findAll1();
	
	@Query(value = "select new map(hch.hch_id as id,hch.holidayCalendarId as holidayCalendarId,hch.leave_id as leave_id,hch.fromDate as fromDate,hch.jobTypeId as jobTypeId,"
			+ "hch.daysCount as daysCount,hch.holidayName as holidayName,hch.createdBy as createdBy,hch.modifiedBy as modifiedBy,"
			+ "hch.createdDate as createdDate,hch.modifiedDate as modifiedDate,hch.active as active,hch.leave_type_short as leave_type_short,"
			+ "hch.createdUsername as createdUsername,hch.modifiedUsername as modifiedUsername,hch.schoolId as schoolId) "
			+ "from HolidayCalenderHistory hch left join Schools s on hch.schoolId=s.school_id "
			+ "left join JobType jt on hch.jobTypeId=jt.job_type_id "
			+ "where CONCAT(IfNull(hch.hch_id,''),'',IfNull(hch.leave_id,''),'',IfNull(hch.fromDate,''),'',IfNull(hch.schoolId,''),"
			+ "'',IfNull(hch.jobTypeId,''),'',IfNull(hch.daysCount,''),'',IfNull(hch.holidayName,''),'',IfNull(hch.leave_type_short,''),"
			+ "'',IfNull(hch.jobTypeId,''),'',IfNull(hch.createdBy,''),'',IfNull(hch.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(hch.hch_id as id,hch.holidayCalendarId as holidayCalendarId,hch.leave_id as leave_id,hch.fromDate as fromDate,hch.jobTypeId as jobTypeId,"
			+ "hch.daysCount as daysCount,hch.holidayName as holidayName,hch.createdBy as createdBy,hch.modifiedBy as modifiedBy,"
			+ "hch.createdDate as createdDate,hch.modifiedDate as modifiedDate,hch.active as active,hch.leave_type_short as leave_type_short,"
			+ "hch.createdUsername as createdUsername,hch.modifiedUsername as modifiedUsername,hch.schoolId as schoolId) "
			+ "from HolidayCalenderHistory hch left join Schools s on hch.schoolId=s.school_id "
			+ "left join JobType jt on hch.jobTypeId=jt.job_type_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update HolidayCalenderHistory hch set hch.active=false where hch.hch_id=?1")
	public void updateToDeactive(Integer id);

	@Modifying
	@Query(value = "update HolidayCalenderHistory hch set hch.active=true where hch.hch_id=?1")
	public void updateToActive(Integer id);

	public HolidayCalenderHistory save(HolidayCalender existProduct);

	
}
