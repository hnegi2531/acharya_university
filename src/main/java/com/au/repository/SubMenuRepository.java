package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.SubMenu;

@Transactional
@Repository
public interface SubMenuRepository extends JpaRepository<SubMenu, Integer> {

	@Query(value = "select sm from SubMenu sm where sm.active=true")
	public List<SubMenu> findAll1();

	@Query(value = "select new map (sm.submenu_id as id,sm.submenu_name as submenu_name,sm.submenu_url as submenu_url,"
			+ "sm.submenu_desc as submenu_desc,sm.menu_id as menu_id,sm.created_by as created_by,sm.modified_by as modified_by,"
			+ "sm.created_date as created_date,sm.modified_date as modified_date,sm.active as active,sm.status as status,"
			+ "sm.created_username as created_username,sm.modified_username as modified_username,m.menu_name as menu_name,"
			+ "m.menu_short_name as menu_short_name) from SubMenu sm left join Menu m on sm.menu_id=m.menu_id")
	public List<HashMap<String, Object>> findAll2();
	
	@Query(value = "select new map (sm.submenu_id as id,sm.submenu_name as submenu_name,sm.submenu_url as submenu_url,sm.mask As mask,"
			+ "sm.submenu_desc as submenu_desc,sm.menu_id as menu_id,sm.created_by as created_by,sm.modified_by as modified_by,"
			+ "sm.created_date as created_date,sm.modified_date as modified_date,sm.active as active,sm.status as status,"
			+ "sm.created_username as created_username,sm.modified_username as modified_username,m.menu_name as menu_name,"
			+ "mo.module_name as module_name,mo.module_short_name as module_short_name,"
			+ "m.menu_short_name as menu_short_name) from SubMenu sm left join Menu m on sm.menu_id=m.menu_id "
			+ "left join Module mo on m.module_id=mo.module_id "
			+ "where CONCAT(IfNull(sm.submenu_id,''),'',IfNull(sm.submenu_name,''),'',IfNull(sm.submenu_url,''),"
			+ "'',IfNull(sm.created_by,''),'',IfNull(sm.created_date,''),'',IfNull(sm.submenu_desc,''),'',"
			+ "IfNull(m.menu_name,''),'',IfNull(m.menu_short_name,''),'',IfNull(sm.status,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value="select new map (sm.submenu_id as id,sm.submenu_name as submenu_name,sm.submenu_url as submenu_url,sm.mask As mask,"
			+ "sm.submenu_desc as submenu_desc,sm.menu_id as menu_id,sm.created_by as created_by,sm.modified_by as modified_by,"
			+ "sm.created_date as created_date,sm.modified_date as modified_date,sm.active as active,sm.status as status,"
			+ "sm.created_username as created_username,sm.modified_username as modified_username,m.menu_name as menu_name,"
			+ "mo.module_name as module_name,mo.module_short_name as module_short_name,"
			+ "m.menu_short_name as menu_short_name) from SubMenu sm left join Menu m on sm.menu_id=m.menu_id "
			+ "left join Module mo on m.module_id=mo.module_id ")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "Select new map(count(role_id) as countOfRoleId) from SubMenuAssignment sma "
			+ "left join SubMenu sm on sma.submenu_ids=sm.submenu_id "
			+ "where submenu_ids like ?1 or submenu_ids like ?2 or submenu_ids like ?3 or submenu_ids like ?4 and sm.active=true")
	public List<HashMap<String, Object>> findAll8(String submenu_ids4, String submenu_ids5, String submenu_ids6, String submenu_ids7);

	@Query(value = "Select new map (sma.role_id as role_id,r.role_name as role_name) from SubMenuAssignment sma "
			+ "left join Roles r on sma.role_id=r.role_id " + "left join SubMenu sm on sma.submenu_ids=sm.submenu_id "
			+ "where submenu_ids like ?1 or submenu_ids like ?2 or submenu_ids like ?3 or submenu_ids like ?4 and sm.active=true")
	public List<HashMap<String, Object>> findAll9(String submenu_ids1, String submenu_ids2, String submenu_ids3, String submenu_ids8);

	@Modifying
	@Query(value = "update SubMenu sm set sm.active=false where sm.submenu_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update SubMenu sm set sm.active=true where sm.submenu_id=?1")
	public void update1(Integer id);

	@Query(value = "Select new map (sm.submenu_id as submenu_id,sm.submenu_name as submenu_name,m.menu_id as menu_id,m.menu_icon_name as menu_icon_name,sm.mask As mask,"
			+ "sm.submenu_url as submenu_url,m.menu_name as menu_name,mm.module_id as module_id,mm.module_name as module_name) "
			+ "from SubMenu sm Inner join Menu m on sm.menu_id=m.menu_id "
			+ "Inner join Module mm on mm.module_id=m.module_id where sm.submenu_id IN ?1 and sm.active=true")
	public List<HashMap<String, Object>> findAllDetails(List<Integer> submenu_id);

	@Query(value = "Select new map (sm.submenu_id as submenu_id,sm.submenu_name as submenu_name)"
			+ "from SubMenu sm where sm.submenu_id NOT IN :submenu_id and sm.active=true")
	public List<HashMap<String, Object>> fetchUncheckedSubMenudetails(@Param("submenu_id") List<Integer> submenu_id);

	@Query(value = "select submenu_id from SubMenu sm where sm.menu_id=?1")
	public List<Integer> fetchSubMenuDetails(Integer menu_id);

	@Query(value = "select submenu_ids from SubMenuAssignment sma where sma.role_id=?1")
	public String findSubMenuDetails(Integer role_id);

	@Query(value = "SELECT new map(sm.submenu_id as submenu_id,sm.submenu_name as submenu_name,sm.submenu_url as submenu_url) "
			+ "from SubMenu sm where sm.submenu_id IN (:list1)")
	public List<HashMap<String, Object>> finalSubMenuDetails(List<Integer> list1);

	@Query(value = "select sm.user_ids as user_ids from SubMenu sm where sm.submenu_id=?1 and sm.active=true")
	public String getAssignedUser(Integer submenu_id);

	@Query(value = "select new map(ua.id as user_id,ua.username as username) from UserAuthentication ua where ua.id NOT IN :submenu_id and ua.active=true")
	public List<HashMap<String,Object>> getUnassignedUser(List<Integer> submenu_id);

	@Modifying
	@Query(value = "update sub_menu sm set sm.user_ids=:user_ids where sm.submenu_id=:submenu_id",nativeQuery = true)
	public Object saveDetails(String user_ids,Integer submenu_id);
	
	@Query(value = "select sm from SubMenu sm where sm.submenu_id=:submenu_id")
	public SubMenu findBySubMenuId(Integer submenu_id);

	@Query(value = "select count(*) from SubMenu sm where sm.submenu_name=?1 and sm.active=true")
	public Integer getSubMenuName(String submenu_name);
	
	@Query(value = "select count(*) from SubMenu sm where sm.submenu_url=?1 and sm.active=true")
	public Integer getSubMenuUrl(String submenu_url);
	
	@Query(value ="Select GROUP_CONCAT(sm.submenu_name ORDER BY submenu_id ASC) From sub_menu sm where sm.submenu_id in (:submenu_id)",nativeQuery = true)
	public String getSubmenuNameAsCommaSeperated(List<Integer> submenu_id);

	@Query(value = "Select new map (sm.submenu_id as submenu_id,sm.submenu_name as submenu_name) from SubMenu sm "
			+ "where user_ids like ?1 or user_ids like ?2 or user_ids like ?3 or sm.user_ids=?4 and sm.active=true")
	public List<HashMap<String, Object>> getAssignedSubMenuDetailsByUserId(String userIdCase1, String userIdCase2, String userIdCase3,
			String userIdCase4);


}
