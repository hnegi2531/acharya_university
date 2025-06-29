package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.DailyPlanner;

@Transactional
@Repository
public interface DailyPlannerRepository extends JpaRepository<DailyPlanner, Integer>{

	@Query(value = "SELECT dp from DailyPlanner dp where dp.active=true")
	public List<DailyPlanner> getAllActiveDailyPlanner();

	
	@Modifying
	@Query(value = "update DailyPlanner dp set dp.active=false where dp.daily_planner_id=?1")
	public void deactivate(Integer id);
	
	
	@Modifying
	@Query(value = "update DailyPlanner dp set dp.active=true where dp.daily_planner_id=?1")
	public void activate(Integer id);
	
	
	@Query(value = "select new map(dp.daily_planner_id as id,dp.created_date as created_date,dp.modified_date as modified_date,dp.created_by as created_by,"
			+ "dp.created_username as created_username,dp.modified_username as modified_username,dp.modified_by as modified_by,dp.active as active,"
			+ "dp.emp_id as emp_id,dp.task_title as task_title,dp.task_priority as task_priority,dp.description as description,dp.task_status as task_status,"
			+ "dp.from_date as from_date,dp.to_date as to_date,dp.from_time as from_time,dp.to_time as to_time,"
			+ "dp.type As type,dp.contribution_type As contribution_type,dp.task_type As task_type,"
			+ "ed.email as email,ed.empcode as empcode,ed.employee_name as employee_name ) from DailyPlanner dp "
			+ "left join EmployeeDetails ed on dp.emp_id = ed.emp_id "
			+ "where (:emp_id is null or ed.emp_id = :emp_id) And CONCAT(IfNull(dp.emp_id,''),'',IfNull(ed.empcode,''),'',IfNull(dp.task_priority,''),"
			+ "'',IfNull(dp.created_by,''),'',IfNull(dp.created_date,'',IfNull(dp.daily_planner_id,''),'')) LIKE %:keyword%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer emp_id); 
	
	
	@Query(value = "select new map(dp.daily_planner_id as id,dp.created_date as created_date,dp.modified_date as modified_date,dp.created_by as created_by,"
			+ "dp.created_username as created_username,dp.modified_username as modified_username,dp.modified_by as modified_by,dp.active as active,"
			+ "dp.emp_id as emp_id,dp.task_title as task_title,dp.task_priority as task_priority,dp.description as description,dp.task_status as task_status,"
			+ "dp.from_date as from_date,dp.to_date as to_date,dp.from_time as from_time,dp.to_time as to_time,"
			+ "dp.type As type,dp.contribution_type As contribution_type,dp.task_type As task_type,"
			+ "ed.email as email,ed.empcode as empcode,ed.employee_name as employee_name ) from DailyPlanner dp "
			+ "left join EmployeeDetails ed on dp.emp_id = ed.emp_id where (:emp_id is null or ed.emp_id = :emp_id)")
	public Page<Object> getAllSortedData(Pageable pageable, Integer emp_id);


	@Query(value = "select dp.daily_planner_id as id,dp.created_date as created_date,dp.modified_date as modified_date,dp.created_by as created_by,"
			+ "dp.created_username as created_username,dp.modified_username as modified_username,dp.modified_by as modified_by,dp.active as active,"
			+ "dp.emp_id as emp_id,dp.task_title as task_title,dp.task_priority as task_priority,dp.description as description,dp.task_status as task_status,"
			+ "dp.from_date as from_date,dp.to_date as to_date,dp.from_time as from_time,dp.to_time as to_time,"
			+ "dp.type As type,dp.contribution_type As contribution_type,dp.task_type As task_type,"
			+ "ed.email as email,ed.empcode as empcode,ed.employee_name as employee_name from daily_planner dp "
			+ "left join employee_details ed on dp.emp_id = ed.emp_id where dp.emp_id=?1 And dp.active=true ORDER BY dp.created_date DESC", nativeQuery = true)
	public List<Map<String, Object>> getAllActiveDailyPlannerBasedOnEmpId(Integer emp_id);

	@Query(value = "select new map(dp.daily_planner_id as id,dp.created_date as created_date,dp.modified_date as modified_date,dp.created_by as created_by,"
			+ "dp.created_username as created_username,dp.modified_username as modified_username,dp.modified_by as modified_by,dp.active as active,"
			+ "dp.emp_id as emp_id,dp.task_title as task_title,dp.task_priority as task_priority,dp.description as description,dp.task_status as task_status,"
			+ "dp.from_date as from_date,dp.to_date as to_date,dp.from_time as from_time,dp.to_time as to_time,"
			+ "dp.type As type,dp.contribution_type As contribution_type,dp.task_type As task_type,"
			+ "ed.email as email,ed.empcode as empcode,ed.employee_name as employee_name ) from DailyPlanner dp "
			+ "left join EmployeeDetails ed on dp.emp_id = ed.emp_id "
			+ "where (:userId is null or dp.created_by = :userId) And CONCAT(IfNull(dp.emp_id,''),'',IfNull(ed.empcode,''),'',IfNull(dp.task_priority,''),"
			+ "'',IfNull(dp.created_by,''),'',IfNull(dp.created_date,'',IfNull(dp.daily_planner_id,''),'')) LIKE %:keyword%")
	public Page<Object> fetchAllDailyPlannerBasedOnUserKeyword(Pageable pageable, Object keyword, Integer userId);

	@Query(value = "select new map(dp.daily_planner_id as id,dp.created_date as created_date,dp.modified_date as modified_date,dp.created_by as created_by,"
			+ "dp.created_username as created_username,dp.modified_username as modified_username,dp.modified_by as modified_by,dp.active as active,"
			+ "dp.emp_id as emp_id,dp.task_title as task_title,dp.task_priority as task_priority,dp.description as description,dp.task_status as task_status,"
			+ "dp.from_date as from_date,dp.to_date as to_date,dp.from_time as from_time,dp.to_time as to_time,"
			+ "dp.type As type,dp.contribution_type As contribution_type,dp.task_type As task_type,"
			+ "ed.email as email,ed.empcode as empcode,ed.employee_name as employee_name ) from DailyPlanner dp "
			+ "left join EmployeeDetails ed on dp.emp_id = ed.emp_id "
			+ "where (:userId is null or dp.created_by = :userId)")
	public Page<Object> fetchAllDailyPlannerBasedOnUserSortedData(Pageable pageable, Integer userId);
}
