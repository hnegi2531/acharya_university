package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.DepartmentDTO;
import com.au.model.Department;

@Repository
@Transactional
public interface DepartmentRepository extends JpaRepository<Department, Integer>
{
	
	@Query(value = "select new map(d.dept_id as id,d.dept_name as dept_name,d.web_status as web_status,d.common_service as common_service,"
			+ "d.dept_name_short as dept_name_short,d.created_username as created_username,d.modified_username as modified_username,d.no_dues_status as no_dues_status,"
			+ "d.created_date as created_date,d.modified_date as modified_date,d.created_by as created_by,d.dept_icon as dept_icon,d.hod_id as hod_id,d.comments as comments,"
			+ "d.modified_by as modified_by,d.active as active,ua.username as hodUserName ) from Department d "
			+ "left join UserAuthentication ua on ua.id=d.hod_id "
			+ "where CONCAT(IfNull(d.created_username,''),'',IfNull(d.dept_name,''),'',IfNull(d.dept_name_short,''),'',IfNull(d.common_service,''),"
			+ "'',IfNull(d.created_by,''),'',IfNull(d.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(d.dept_id as id,d.dept_name as dept_name,d.web_status as web_status,d.common_service as common_service,d.comments as comments,"
			+ "d.dept_name_short as dept_name_short,d.created_username as created_username,d.modified_username as modified_username,d.dept_icon as dept_icon,"
			+ "d.created_date as created_date,d.modified_date as modified_date,d.created_by as created_by,d.hod_id as hod_id,d.no_dues_status as no_dues_status,"
			+ "d.modified_by as modified_by,d.active as active,ua.username as hodUserName ) from Department d "
			+ "left join UserAuthentication ua on ua.id=d.hod_id ")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "select case when (count(*) > 0)  then true else false end from Department d where d.dept_name = :dept_name")
	public Boolean existsByDeptName(String dept_name);

	@Query(value = "SELECT count(*) FROM department where dept_name=?1 and school_id=?2 and active=1",nativeQuery = true)
	public Integer getDeptCountByDnameSchool(String dept_name,Integer school_id);

	@Query(value = "SELECT * FROM department where school_id=?1 and active=1",nativeQuery = true)
	public List<Department> getDeptBySchholId(Integer school_id);

	@Modifying
	@Query(value = "update Department d set d.active=false where d.dept_id=?1")
	public void updateDept(Integer id);
	
	@Modifying
	@Query(value = "update Department d set d.active=true where d.dept_id=?1")
	public void updateDept1(Integer id);

	@Query(value = "SELECT d from Department d where d.active=true")
	public List<Department> findAll11();
	
	
	@Query(value = "select new map(d.dept_id as id,d.dept_name as dept_name,d.web_status as web_status,d.common_service as common_service,d.comments as comments,"
			+ "d.dept_name_short as dept_name_short,d.created_username as created_username,d.modified_username as modified_username,d.dept_icon as dept_icon,"
			+ "d.created_date as created_date,d.modified_date as modified_date,d.created_by as created_by,d.hod_id as hod_id,d.no_dues_status as no_dues_status,"
			+ "d.modified_by as modified_by,d.active as active,ua.username as hodUserName ) from Department d "
			+ "left join UserAuthentication ua on ua.id=d.hod_id "
			+ "where d.active=true And d.hod_id is not null ")
	public List<Map<String, Object>> getDepartmentBasedOnHodId();

	@Query(value = "select count(*) from Department d where d.dept_name=?1")
	public Integer getcountOfDept_name(String dept_name);

	@Query(value = "select count(*) from Department d where d.dept_name_short=?1")
	public Integer getcountOfDept_short_name(String dept_name_short);
	
	@Query(value = "select d.dept_name from Department d where d.dept_id=?1")
	String getDepartment(Integer dept_id);

	@Query(value = "select new map(d.dept_id as id,d.dept_name as dept_name,d.web_status as web_status,d.common_service as common_service,"
			+ "d.dept_name_short as dept_name_short,d.created_username as created_username,d.modified_username as modified_username,"
			+ "d.created_date as created_date,d.modified_date as modified_date,d.created_by as created_by,"
			+ "d.modified_by as modified_by,d.active as active) from Department d "
			+ "where d.no_dues_status=true And d.active=true")
	public List<Map<String, Object>> allNoDuesDetails();

	@Query(value = "SELECT * FROM department where no_dues_status=true and active=true",nativeQuery = true)
	public List<Department> getDeptData();

	@Query(value = "select count(*) from Department where hod_id=?1")
	public Integer countBasedOnHodId(Integer userId);

	@Query(value="select new com.au.dto.DepartmentDTO( d.dept_id as departmentId, d.dept_name as departmentName  ) from Department d ")
	public List<DepartmentDTO> getDepartments();
	
	@Query(value="select new com.au.dto.DepartmentDTO( d.dept_id as departmentId, d.dept_name as departmentName  ) from Department d Where d.dept_id=:dept_id ")
	public DepartmentDTO departmentDetailsByDepartmentId(Integer dept_id);
	

	
}
