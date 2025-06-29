package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Module;

@Transactional
@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {

	
	@Query(value = "select new map(m.module_id as id,m.module_name as module_name,"
			+ "m.module_short_name as module_short_name,m.created_by as created_by,m.modified_by as modified_by,"
			+ "m.created_date as created_date,m.modified_date as modified_date,m.active as active,"
			+ "m.created_username as created_username,m.modified_username as modified_username) from Module m "
			+ "where CONCAT(IfNull(m.module_id,''),'',IfNull(m.module_name,''),'',IfNull(m.module_short_name,''),"
			+ "'',IfNull(m.created_date,''),'',IfNull(m.created_by,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value="select new map(m.module_id as id,m.module_name as module_name,"
			+ "m.module_short_name as module_short_name,m.created_by as created_by,m.modified_by as modified_by,"
			+ "m.created_date as created_date,m.modified_date as modified_date,m.active as active,"
			+ "m.created_username as created_username,m.modified_username as modified_username) from Module m")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "select m from Module m where m.active=true")
	public List<Module> findAll1();

	@Modifying
	@Query(value = "update Module m set m.active=false where m.module_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Module m set m.active=true where m.module_id=?1")
	public void update1(Integer id);
	
	@Query(value = "select m from Module m where m.module_id=?1")
	public Module findByModuleId(Integer module_id);
		
	@Query(value = "SELECT count(*) FROM Module m where m.module_name=?1 and m.active=true")
	public Integer countModuleName(String module_name);
	
	@Query(value = "SELECT count(*) FROM Module m where m.module_short_name=?1 and m.active=true")
	public Integer countgetModuleShortName(String module_short_name);
	
	@Query(value = "select count(*) FROM module m where m.module_id and m.active=true",nativeQuery=true)
	public Integer getCountForMaximumModule();

	
}
