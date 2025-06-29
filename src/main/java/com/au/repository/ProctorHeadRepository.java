package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ProctorHead;

@Transactional
@Repository
public interface ProctorHeadRepository extends JpaRepository<ProctorHead, Integer> {

	@Query(value = "select ph from ProctorHead ph where ph.active=true")
	public List<ProctorHead> findAll1();

//	@Query(value = "select new map(ph.chief_proctor_id as chief_proctor_id,ph.employee_name as employee_name,"
//			+ "ph.emp_id as emp_id,ph.school_id as school_id,ph.created_date as created_date,ph.active as active,"
//			+ "ph.created_username as created_username,s.school_name_short as school_name_short) from ProctorHead ph "
//			+ "left join Schools s on ph.school_id=s.school_id")
//	public List<HashMap<String, Object>> fetchAllDetails();
	
	@Query(value = "select new map(ph.chief_proctor_id as id,ed.employee_name as employee_name,ed.email as email,ph.created_by as created_by,"
			+ "concat(ed.employee_name,'-',ed.empcode, '-', d.dept_name) as concat_employee_name,ph.remarks as remarks,"
			+ "ph.emp_id as emp_id,ph.created_date as created_date,ph.active as active,"
			+ "ph.created_username as created_username) from ProctorHead ph "
			+ "Inner join EmployeeDetails ed on ed.emp_id=ph.emp_id "
			+ "left join Department d on ed.dept_id=d.dept_id "
			+ "where CONCAT(IfNull(ph.chief_proctor_id,''),'',IfNull(ph.emp_id,''),'',IfNull(ed.employee_name,''),"
			+ "'',IfNull(ed.email,''),'',IfNull(ph.created_by,''),'',IfNull(ph.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(ph.chief_proctor_id as id,ed.employee_name as employee_name,ed.email as email,ph.created_by as created_by,"
			+ "concat(ed.employee_name,'-',ed.empcode, '-', d.dept_name) as concat_employee_name,ph.remarks as remarks,"
			+ "ph.emp_id as emp_id,ph.created_date as created_date,ph.active as active,"
			+ "ph.created_username as created_username) from ProctorHead ph "
			+ "Inner join EmployeeDetails ed on ed.emp_id=ph.emp_id "
			+ "left join Department d on ed.dept_id=d.dept_id ")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update ProctorHead ph set ph.active=false where ph.chief_proctor_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update ProctorHead ph set ph.active=true where ph.chief_proctor_id=?1")
	public void update1(Integer id);
	
	@Query(value = "select ph.emp_id from ProctorHead ph where ph.active=true")
	public List<Integer> getEmpIds();
	
	@Query(value = "select new map(ph.chief_proctor_id as id,ed.employee_name as employee_name,ed.email as email,ph.created_by as created_by,"
			+ "concat(ed.employee_name,'-',ed.empcode, '-', d.dept_name) as concat_employee_name,ph.remarks as remarks,"
			+ "ph.emp_id as emp_id,ph.created_date as created_date,ph.active as active,"
			+ "ph.created_username as created_username) from ProctorHead ph "
			+ "Inner join EmployeeDetails ed on ed.emp_id=ph.emp_id "
			+ "left join Department d on ed.dept_id=d.dept_id where ph.active=true And ed.active=true")
	public List<HashMap<String, Object>> getAllActiveProctors();

	

//	@Query(value = "select new map(ph.user_id as user_id,ph.employee_name as employee_name) from ProctorHead ph "
//			+ "where ph.school_id=?1")
//	public List<HashMap<String, Object>> fetchProctorDetail(Integer school_id);
//
//	@Query(value = "select new map(ed.employee_name as employee_name,ed.emp_id as emp_id) from EmployeeDetails ed "
//			+ "where ed.chief_proctor_id=?1")
//	public List<HashMap<String, Object>> getProctorDetail(Integer chief_proctor_id);

}
