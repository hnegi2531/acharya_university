package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.HolidayType;
import com.au.model.LeaveType;

@Repository
@Transactional
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {

	@Query(value = "select h from LeaveType h where h.active=true")
	public List<LeaveType> findAll1();

	@Modifying
	@Query(value = "update LeaveType h set h.active=false where h.leave_id=?1")
	public void updateLeaveType(Integer id);

	@Modifying
	@Query(value = "update LeaveType h set h.active=true where h.leave_id=?1")
	public void updateLeaveType1(Integer id);

	@Query(value = "Select new map(lt.leave_id as id,lt.leave_type as leave_type,lt.leave_type_short as leave_type_short,lt.remarks as remarks,lt.is_attendance as is_attendance,"
			+ "lt.leave_type_attachment_required as leave_type_attachment_required,lt.hr_initialization_status as hr_initialization_status,"
			+ "lt.created_by as created_by,lt.modified_by as modified_by,lt.type as type,lt.unit as unit,lt.created_date as created_date,"
			+ "lt.modified_date as modified_date,lt.active as active,lt.created_username as created_username,lt.modified_username as modified_username) From LeaveType lt "
			+ "Where CONCAT(IfNull(lt.leave_id,''),'',IfNull(lt.leave_type,''),'',IfNull(lt.leave_type_short,''),'',IfNull(lt.type,''),'',IfNull(lt.created_date,''),'',IfNull(lt.created_username,'')) LIKE %?1% ")
	public Page<Object> findAll2(Pageable pageable, Object keyword);

	@Query(value = "Select new map(lt.leave_id as id,lt.leave_type as leave_type,lt.leave_type_short as leave_type_short,"
			+ "lt.remarks as remarks,lt.is_attendance as is_attendance,lt.hr_initialization_status as hr_initialization_status,"
			+ "lt.leave_type_attachment_required as leave_type_attachment_required,"
			+ "lt.created_by as created_by,lt.modified_by as modified_by,lt.type as type,lt.unit as unit,lt.created_date as created_date,"
			+ "lt.modified_date as modified_date,lt.active as active,lt.created_username as created_username,lt.modified_username as modified_username) From LeaveType lt")
	public Page<Object> findAll3(Pageable pageable);

	@Query(value = "select h from LeaveType h where h.type='Holiday' and h.active=true")
	public List<LeaveType> fetchHolidayTypeLeaves();

	@Query(value = "SELECT count(*) FROM LeaveType lt where lt.leave_type=?1 and lt.active=true")
	public Integer countLeaveName(String leave_type);

	@Query(value = "SELECT count(*) FROM LeaveType lt where lt.leave_type_short=?1 and lt.active=true")
	public Integer countLeaveShortName(String leave_type_short);

	@Query(value = "SELECT * FROM leave_type where leave_id=?1 and active=true", nativeQuery = true)
	public LeaveType fetchLeaveType(Integer leave_id);

	@Modifying
	@Query(value = "update LeaveType lt set lt.leave_type_path=?2 where lt.leave_id=?1")
	public void updatePath(Integer leave_id, String t1);

	@Query(value = "select lt from LeaveType lt " + "where lt.active=true And lt.type like '%Attendence%' ")
	public List<LeaveType> getLeaveTypeForAttendence();

	@Query(value = "select lt.hr_initialization_status from LeaveType lt where lt.leave_id=?1 and lt.active=true")
	public Boolean getHrStatus(Integer leave_id);

	@Query(value = "select lt from LeaveType lt "
			+ "where lt.active=true And lt.type like '%Leave%' And lt.is_attendance=true")
	public List<LeaveType> getLeaveTypeForLeaveAndAttendence();

	@Query(value="select l.leave_type_short from LeaveType l ")
	public List<String> getLeaveTypeShortName();

	
	@Query(value="select l.leave_type_short from LeaveType l where l.leave_id=?1 And l.active=true")
	public String getleaveTypeData(Integer leave_id);

	
	@Query(value="select l.leave_id from LeaveType l where l.leave_type_short=?1 And l.active=true")
	public Integer getLeaveIdOfVacationLeave(String leaveTypeShort);

	@Query(value="select l.leave_id from LeaveType l where l.leave_type_short=:leave_short_name And l.active=true")
	public Integer getLeaveIdByLeaveTypeShort(String leave_short_name);

	@Query(value="select l.leave_id from LeaveType l where l.leave_type_short='OD' And l.active=true")
	public Integer getOnDutyLeaveId();

	@Query(value = "select l.leave_id from LeaveType l where l.leave_type_short = :leaveShortName")
    Integer findByLeaveShortName(String leaveShortName);
}
