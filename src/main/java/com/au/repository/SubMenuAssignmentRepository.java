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

import com.au.model.SubMenuAssignment;

@Transactional
@Repository
public interface SubMenuAssignmentRepository extends JpaRepository<SubMenuAssignment, Integer> {

	@Query(value = "select sma from SubMenuAssignment sma where sma.active=true")
	public List<SubMenuAssignment> findAll1();

	@Query(value = "select new map(sma.menu_assignment_id as id,sma.role_id as role_id,"
			+ "sma.submenu_ids as submenu_ids,"
			+ "sma.submenu_name as submenu_name,sma.created_by as created_by,sma.modified_by as modified_by,"
			+ "sma.created_date as created_date,sma.modified_date as modified_date,sma.active as active,"
			+ "sma.created_username as created_username,sma.modified_username as modified_username,"
			+ "r.role_name as role_name,r.role_short_name as role_short_name) "
			+ "from SubMenuAssignment sma left join Roles r on sma.role_id=r.role_id "
			+ "where CONCAT(IfNull(r.role_id,''),'',IfNull(r.role_name,''),'',IfNull(r.role_desc,''),'',IfNull(r.role_short_name,''),"
			+ "'',IfNull(r.created_by,''),'',IfNull(r.created_Date,''),'',IfNull(sma.submenu_name,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(sma.menu_assignment_id as id,sma.role_id as role_id,"
			+ "sma.submenu_ids as submenu_ids,sma.submenu_name as submenu_name,sma.created_by as created_by,sma.modified_by as modified_by,"
			+ "sma.created_date as created_date,sma.modified_date as modified_date,sma.active as active,"
			+ "sma.created_username as created_username,sma.modified_username as modified_username,"
			+ "r.role_name as role_name,r.role_short_name as role_short_name) "
			+ "from SubMenuAssignment sma left join Roles r on sma.role_id=r.role_id")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update SubMenuAssignment sma set sma.active=false where sma.menu_assignment_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update SubMenuAssignment sma set sma.active=true where sma.menu_assignment_id=?1")
	public void update1(Integer id);
/*
	@Query(value = "SELECT * FROM sub_menu_assignment where role_id=?1", nativeQuery = true)
	public List<SubMenuAssignment> fetchSubMenu1(Integer role_id);
*/
	@Query(value = "select new map(sma.menu_assignment_id as menu_assignment_id,sma.submenu_ids as submenu_ids,"
			+ "sma.submenu_name as submenu_name,sma.count as count,sma.created_by as created_by,sma.created_date as created_date,"
			+ "sma.active as active,sma.created_username as created_username)"
			+ " from SubMenuAssignment sma where sma.role_id IN ?1")
	public List<HashMap<String, Object>> fetchSubMenu(List<Integer> role_id);

	@Query(value ="select new map(ma.submenu_ids as submenu_ids,ma.submenu_name as submenu_name,m.menu_id as menu_id,"
			+ "m.menu_name as menu_name,mm.module_id as module_id,mm.module_name as module_name) from SubMenuAssignment ma "
			+ "left join SubMenu sm on ma.submenu_ids=sm.submenu_id "
			+ "left join Menu m on sm.menu_id=m.menu_id "
			+ "left join Module mm on mm.module_id=m.module_id where role_id=?1")
	public List<HashMap<String, Object>> fetchSubMenu2(Integer role_id);
	
	@Query(value = "SELECT count(*) FROM SubMenuAssignment sma where sma.role_id=?1 and sma.active=true")
	public Integer countOfProgramRoleId(Integer role_id);

	@Query(value = "Select sma.role_id from SubMenuAssignment sma "
			+ "where submenu_ids like ?1 or submenu_ids like ?2 or submenu_ids like ?3 or submenu_ids like ?4 and sma.active=true")
	public List<Integer> getAllAssignedRoleBySubmenuId(String submenu_ids1, String submenu_ids2, String submenu_ids3, String submenu_ids4);
	

}
