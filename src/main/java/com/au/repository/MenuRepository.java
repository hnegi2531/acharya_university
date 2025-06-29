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

import com.au.model.Menu;

@Transactional
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

	@Query(value = "select m from Menu m where m.active=true")
	public List<Menu> findAll1();

	@Query(value = "select new map(m.menu_id as id,m.menu_name as menu_name,m.menu_short_name as menu_short_name,"
			+ "m.module_id as module_id,m.menu_desc as menu_desc,m.created_by as created_by,m.modified_by as modified_by,"
			+ "m.created_date as created_date,m.created_date as created_date,m.active as active,m.created_username as created_username,"
			+ "m.modified_username as modified_username,mo.module_name as module_name,"
			+ "mo.module_short_name as module_short_name) "
			+ "from Menu m left join Module mo on m.module_id=mo.module_id "
			+ "where CONCAT(IfNull(m.menu_name,''),'',IfNull(m.menu_short_name,''),'',IfNull(m.menu_desc,''),"
			+ "'',IfNull(mo.module_name,''),'',IfNull(m.created_by,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value="select new map(m.menu_id as id,m.menu_name as menu_name,m.menu_short_name as menu_short_name,"
			+ "m.module_id as module_id,m.menu_desc as menu_desc,m.created_by as created_by,m.modified_by as modified_by,"
			+ "m.created_date as created_date,m.created_date as created_date,m.active as active,m.created_username as created_username,"
			+ "m.modified_username as modified_username,mo.module_name as module_name,"
			+ "mo.module_short_name as module_short_name) "
			+ "from Menu m left join Module mo on m.module_id=mo.module_id")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update Menu m set m.active=false where m.menu_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Menu m set m.active=true where m.menu_id=?1")
	public void update1(Integer id);
	
	@Query(value = "select new map(me.menu_name as menu_name,me.menu_short_name as menu_short_name,me.menu_id as menu_id)"
			+ " from Menu me left join Module m on me.module_id=m.module_id where me.module_id=?1")
	public List<HashMap<String, Object>> fetchMenuDetail(Integer module_id);
	
	@Query(value = "select m from Menu m where m.menu_id=?1")
	public Menu findByMenuId(Integer menu_id);
	
	
	@Query(value = "SELECT count(*) FROM Menu m where m.menu_name=?1 and m.active=true")
	public Integer countOfMenuName(String menu_name);
	
	@Query(value = "SELECT count(*) FROM Menu m where m.menu_short_name=?1 and m.active=true")
	public Integer countOfMenuShortName(String menu_short_name);
	
	@Query(value ="Select me.menu_id as menu_id,concat(me.menu_name,'(',ifNull(m.module_name,\" \"),')') as menu_module_name From menu me "
			+ "left join module m on me.module_id=m.module_id where me.active=true",nativeQuery = true)
	public List<Map<String,Object>> menuNameConcatWithModule();
	
	@Query(value = "SELECT count(*) FROM Menu me where me.module_id=?1 and me.menu_name=?2 and me.active=true")
	public Integer countOfMenuName(Integer module_id,String menu_name);
	
	@Query(value = "SELECT count(*) FROM Menu me where me.module_id=?1 and me.menu_short_name=?2 and me.active=true")
	public Integer countOfMenuShortName(Integer module_id,String menu_short_name);
	
	@Query(value = "SELECT count(*) FROM Menu me where me.menu_id != ?1 and me.menu_name=?2 and me.active=true")
	public Integer countOfUpdatMenuName(Integer menu_id,String menu_name);
	
	@Query(value = "SELECT count(*) FROM Menu me where me.menu_id != ?1 and me.menu_short_name=?2 and me.active=true")
	public Integer countOfUpdateMenuShortName(Integer menu_id,String menu_short_name);
}
