package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Roles;

@Transactional
@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

	@Query(value = "select r from Roles r where r.active=true")
	public List<Roles> findAll1();

	@Modifying
	@Query(value = "update Roles r set r.active=false where r.role_id=?1")
	public void updateToDeactive(Integer id);

	@Modifying
	@Query(value = "update Roles r set r.active=true where r.role_id=?1")
	public void updateToActive(Integer id);

	@Query(value = "select new map(r.role_id as id,r.role_name as role_name,r.role_desc as role_desc,r.access as access,"
			+ "r.role_short_name as role_short_name,r.back_date as back_date,r.created_by as created_by,r.modified_by as modified_by,"
			+ "r.created_Date as created_Date,r.modified_Date as modified_Date,r.active as active,"
			+ "r.created_username as created_username,r.modified_username as modified_username,sma.count as count,r.lms_status as lms_status) "
			+ "from Roles r left join SubMenuAssignment sma on r.role_id=sma.role_id "
			+ "where CONCAT(IfNull(r.role_id,''),'',IfNull(r.role_name,''),'',IfNull(r.role_desc,''),'',IfNull(r.access,''),"
			+ "'',IfNull(r.role_short_name,''),'',IfNull(r.back_date,''),'',IfNull(r.created_by,''),'',IfNull(r.created_Date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(r.role_id as id,r.role_name as role_name,r.role_desc as role_desc,r.access as access,"
			+ "r.role_short_name as role_short_name,r.back_date as back_date,r.created_by as created_by,r.modified_by as modified_by,"
			+ "r.created_Date as created_Date,r.modified_Date as modified_Date,r.active as active,"
			+ "r.created_username as created_username,r.modified_username as modified_username,sma.count as count,r.lms_status as lms_status) "
			+ "from Roles r left join SubMenuAssignment sma on r.role_id=sma.role_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "Select r From Roles r  where r.role_id=?1")
	public Roles findByRole_id(Integer role_id);
	
	@Query(value = "select count(*) from Roles r where r.role_name=?1 and active=true")
	public Integer getCountRoleName(String role_name);
	
	@Query(value = "select count(*) from Roles r where r.role_short_name=?1 and active=true")
	public Integer getCountRoleShortName(String role_short_name);
	
	@Query(value = "select r from Roles r left join UserRole ur on ur.role_id=r.role_id left join UserAuthentication ud on ud.id=ur.id   where ud.id=?1")	
	public Roles getUserRolebyuserId(Integer userId);

	@Query(value = "Select r From Roles r where r.role_name=?1 And r.active=true")
	public Roles findRoleByRoleName( String role_name);

	@Query(value = "select * from user_role where id = :empId", nativeQuery = true)
    Integer findByEmpId(Integer empId);
}
