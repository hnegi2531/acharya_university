package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.LeaveKitty;
import com.au.model.LeaveKitty;

@Repository
@Transactional
public interface LeaveKittyRepository extends JpaRepository<LeaveKitty, Integer>{
	@Query(value = "select count(*) from LeaveKitty el where el.employee_leave_id=?1")
	public Integer getcountOfEmpLeavesId(Integer getEmployee_leave_id);

	@Query(value = "select new map(el.employee_leave_id as id,el.leave_pattern_id as leave_pattern_id,el.emp_id as emp_id,el.initial_days_count as initial_days_count,"
			+ "el.updated_days_count as updated_days_count,el.created_username as created_username,el.modified_username as modified_username,el.accumulated_date as accumulated_date,"
			+ "el.created_date as created_date,el.modified_date as modified_date,el.created_by as created_by,el.accumulated_count as accumulated_count,el.carry_status as carry_status,"
			+ "el.modified_by as modified_by,el.active as active) from LeaveKitty el "
			+ "where CONCAT(IfNull(el.created_username,''),'',IfNull(el.leave_pattern_id,''),'',"
			+ "'',IfNull(el.created_by,''),'',IfNull(el.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "select new map(el.employee_leave_id as id,el.leave_pattern_id as leave_pattern_id,el.emp_id as emp_id,el.initial_days_count as initial_days_count,"
			+ "el.updated_days_count as updated_days_count,el.created_username as created_username,el.modified_username as modified_username,el.accumulated_date as accumulated_date,"
			+ "el.created_date as created_date,el.modified_date as modified_date,el.created_by as created_by,el.accumulated_count as accumulated_count,el.carry_status as carry_status,"
			+ "el.modified_by as modified_by,el.active as active) from LeaveKitty el ")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "SELECT el from LeaveKitty el where el.active=true")
	public List<LeaveKitty> findAll1();

	@Modifying
	@Query(value = "update LeaveKitty el set el.active=false where el.employee_leave_id=?1")
	public void updateEmpLeaves(Integer id);

	@Modifying
	@Query(value = "update LeaveKitty el set el.active=true where el.employee_leave_id=?1")
	public void updateEmpLeaves1(Integer id);

	@Modifying
	@Query(value = "update leave_kitty el set el.updated_days_count=el.updated_days_count+1 where emp_id IN "
			+ "(select ed.emp_id from employee_details ed where ed.emp_type_id=2)", nativeQuery = true)
	public void updateLeavesEveryMonthOn20th();

	@Modifying
	@Query(value = "update leave_kitty el set el.updated_days_count=el.updated_days_count+6 where emp_id IN "
			+ "(select ed.emp_id from employee_details ed where ed.emp_type_id=1)", nativeQuery = true)
	public void updateLeavesEveryJanuaryAndJulyOn1st();

	@Query(value = "select el.accumulated_count from LeaveKitty el where el.emp_id=?1 and el.leave_pattern_id=?2 and el.active=true")
	public Double getUpdatedDaysCount(Integer emp_id, Integer leave_pattern_id);

	@Modifying
	@Query(value = "update LeaveKitty el set el.updated_days_count=?3 where el.emp_id=?1 and el.leave_pattern_id=?2")
	public void updateAvailabeLeaves(Integer emp_id, Integer leave_pattern_id, Integer leaved_to_be_added);

	@Query(value = "select el.accumulated_count from leave_kitty el where el.leave_pattern_id=?1 and el.emp_id=?2  and el.active=true", nativeQuery = true)
	public Double getAcculamatedDaysCount(Integer leave_pattern_id, Integer emp_id);

	@Modifying
	@Query(value = "update LeaveKitty el set el.accumulated_count=?3 where el.leave_pattern_id=?1 and el.emp_id=?2 ")
	public void updateAcculamatedDaysCoun(Integer leave_pattern_id, Integer emp_id, Double updateCount);

	
	@Query(value=" select el from LeaveKitty el where el.emp_id=:empId and el.leave_pattern_id=:leavePatternId")
	public LeaveKitty getByEmpIdAndLeaveId(Integer empId,Integer leavePatternId);
	
	@Modifying
	@Query(value = "update LeaveKitty el set el.accumulated_count=?3 where el.leave_pattern_id=?1 and el.emp_id=?2 ")
	public void updateAcculamatedDaysCountAfterCancel(Integer leave_pattern_id, Integer emp_id, Double updateCount);

	@Query(value = "select el.accumulated_count from leave_kitty el where el.leave_pattern_id=?1 and el.emp_id=?2 and el.active=true", nativeQuery = true)
	public Double getAvailableLeaves(Integer leavePatternId, Integer empId);

}
