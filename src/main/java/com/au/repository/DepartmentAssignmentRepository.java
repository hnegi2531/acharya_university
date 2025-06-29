package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.DepartmentAssignment;

@Transactional
@Repository
public interface DepartmentAssignmentRepository extends JpaRepository<DepartmentAssignment, Integer> {
	
	@Query(value = "SELECT count(*) FROM dept_assign where dept_id=?1 and school_id=?2 and active=true",nativeQuery = true)
	public Integer getDeptCountByDnameSchool(Integer dept_id,Integer school_id);

	@Query(value = "SELECT * FROM dept_assign where school_id=?1 and active=true",nativeQuery = true)
	public List<DepartmentAssignment> getDeptBySchholId(Integer school_id);
	
//	@Query(value = "select new map(da.dept_assign_id as dept_assign_id,da.dept_id as dept_id,d.dept_name as dept_name,"
//			+ "d.dept_name_short as dept_name_short,da.created_username as created_username,"
//			+ "da.modified_username as modified_username,da.school_id as school_id,sc.school_name as school_name,sc.school_name_short as school_name_short,"
//			+ "da.created_date as created_date,da.modified_date as modified_date,da.created_by as created_by,"
//			+ "da.modified_by as modified_by,da.active as active,da.tag_id as tag_id,da.priority as priority,"
//			+ "da.service_oriented as service_oriented) from DepartmentAssignment da "
//			+ "left join Schools sc on da.school_id=sc.school_id left join Department d on da.dept_id=d.dept_id")
//	public List<HashMap<String, Object>> findAll11();

	@Query(value = "select new map(da.dept_assign_id as id,da.dept_id as dept_id,d.dept_name as dept_name,"
			+ "d.dept_name_short as dept_name_short,da.created_username as created_username,"
			+ "da.modified_username as modified_username,da.school_id as school_id,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "da.created_date as created_date,da.modified_date as modified_date,da.created_by as created_by,"
			+ "da.modified_by as modified_by,da.active as active,da.priority as priority,"
			+ "da.service_oriented as service_oriented) from DepartmentAssignment da "
			+ "left join Schools sc on da.school_id=sc.school_id left join Department d on da.dept_id=d.dept_id "
			+ "where CONCAT(IfNull(da.dept_assign_id,''),'',IfNull(d.dept_name,''),'',IfNull(d.dept_name_short,''),'',IfNull(sc.school_name_short,''),"
			+ "'',IfNull(sc.school_name,''),'',IfNull(da.created_by,''),'',IfNull(da.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(da.dept_assign_id as id,da.dept_id as dept_id,d.dept_name as dept_name,"
			+ "d.dept_name_short as dept_name_short,da.created_username as created_username,"
			+ "da.modified_username as modified_username,da.school_id as school_id,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "da.created_date as created_date,da.modified_date as modified_date,da.created_by as created_by,"
			+ "da.modified_by as modified_by,da.active as active,da.priority as priority,"
			+ "da.service_oriented as service_oriented) from DepartmentAssignment da "
			+ "left join Schools sc on da.school_id=sc.school_id left join Department d on da.dept_id=d.dept_id ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update DepartmentAssignment da set da.active=false where da.dept_assign_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update DepartmentAssignment da set da.active=true where da.dept_assign_id=?1")
	public void update1(Integer id);

	@Query(value = "select da from DepartmentAssignment da where da.active=true")
	public List<DepartmentAssignment> findAll1();

	
	@Query(value = "select new map(da.dept_assign_id as dept_assign_id,da.dept_id as dept_id,d.dept_name as dept_name,"
			+ "d.dept_name_short as dept_name_short,da.created_username as created_username,"
			+ "da.modified_username as modified_username,da.school_id as school_id,sc.school_name as school_name,"
			+ "sc.school_name_short as school_name_short,da.created_date as created_date,da.modified_date as modified_date,"
			+ "da.created_by as created_by,da.modified_by as modified_by,da.active as active,"
			+ "da.priority as priority,da.service_oriented as service_oriented) from DepartmentAssignment da "
			+ "left join Schools sc on da.school_id=sc.school_id left join Department d on da.dept_id=d.dept_id "
			+ "where da.school_id=?1 and da.active=true")
	public List<HashMap<String, Object>> findAll1(Integer school_id);
	
	@Query(value = "SELECT count(*) FROM dept_assign where dept_id=?1 and school_id=?2 and active=true",nativeQuery = true)
	public Integer getCountDepartmentAssignment(Integer dept_id,Integer school_id);
	
	@Query(value = "select new map(da.dept_assign_id as id,da.dept_id as dept_id,d.dept_name as dept_name,"
			+ "d.dept_name_short as dept_name_short,da.created_username as created_username,"
			+ "da.modified_username as modified_username,da.school_id as school_id,sc.school_name as school_name,"
			+ "sc.school_name_short as school_name_short,da.created_date as created_date,da.modified_date as modified_date,"
			+ "da.created_by as created_by,da.modified_by as modified_by,da.active as active,"
			+ "da.priority as priority,da.service_oriented as service_oriented) from DepartmentAssignment da "
			+ "left join Schools sc on da.school_id=sc.school_id left join Department d on da.dept_id=d.dept_id "
			+ "where da.dept_assign_id=?1 and da.active=true")
	public List<HashMap<String, Object>> findById1(Integer id);
	
	@Query(value = "Select s.school_id as school_id,s.school_name as school_name,s.school_name_short as school_name_short from schools s "
			+ "where s.school_id NOT IN (Select da.school_id from dept_assign da where da.dept_id=?1) and s.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchUnassignedSchoolIds(Integer dept_id);
	
	@Query(value = "select GROUP_CONCAT(da.dept_id) from dept_assign da where da.school_id=?1 and da.active=true",nativeQuery = true)
	public String fetchDepartmentIds(Integer school_id);
	
	@Query(value = "select new map(d.dept_id as id,d.dept_name as dept_name,d.web_status as web_status,d.common_service as common_service,"
			+ "d.dept_name_short as dept_name_short,d.created_username as created_username,d.modified_username as modified_username,"
			+ "d.created_date as created_date,d.modified_date as modified_date,d.created_by as created_by,d.dept_icon as dept_icon,"
			+ "d.modified_by as modified_by,d.active as active) from Department d "
			+ "where d.common_service=true And d.active=true")
	public List<Map<String, Object>> getActiveDepartmentAssignmentBasedOnTag();

	
	@Query(value = "select d.dept_id as dept_id,d.dept_name as dept_name,d.dept_name_short as dept_name_short from dept_assign da "
			+ "left join department d on d.dept_id=da.dept_id  where da.school_id=?1 ",nativeQuery = true)
	public List<Map<String, Object>> getDeptDetailsBasedOnSchoolId(Integer school_id);

	
}
