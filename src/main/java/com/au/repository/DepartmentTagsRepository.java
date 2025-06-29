package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.DepartmentTags;

@Transactional
@Repository
public interface DepartmentTagsRepository extends JpaRepository<DepartmentTags, Integer> {

	@Query(value = "select dt from DepartmentTags dt where dt.active=true")
	public List<DepartmentTags> findAll1();
	
	@Modifying
	@Query(value = "update DepartmentTags dt set dt.active=false where dt.tag_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update DepartmentTags dt set dt.active=true where dt.tag_id=?1")
	public void update1(Integer id);

	@Query(value="SELECT * FROM department_tags where tag_name=?1 and tag_short_name=?2 and active=true", nativeQuery = true)
	public List<DepartmentTags> checkexist(String tag_name, String tag_short_name);

	@Query(value = "select new map(dt.tag_id as id,dt.tag_name as tag_name,dt.tag_short_name as tag_short_name,"
			+ "dt.service_oriented as service_oriented,dt.created_username as created_username,"
			+ "dt.modified_username as modified_username,dt.show_in_event as show_in_event,"
			+ "dt.created_date as created_date,dt.modified_date as modified_date,dt.created_by as created_by,"
			+ "dt.modified_by as modified_by,dt.active as active,dt.service_oriented as service_oriented) from DepartmentTags dt "
			+ "where CONCAT(IfNull(dt.tag_id,''),'',IfNull(dt.tag_name,''),'',IfNull(dt.tag_short_name,''),'',IfNull(dt.show_in_event,''),"
			+ "'',IfNull(dt.service_oriented,''),'',IfNull(dt.created_by,''),'',IfNull(dt.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(dt.tag_id as id,dt.tag_name as tag_name,dt.tag_short_name as tag_short_name,"
			+ "dt.service_oriented as service_oriented,dt.created_username as created_username,"
			+ "dt.modified_username as modified_username,dt.show_in_event as show_in_event,"
			+ "dt.created_date as created_date,dt.modified_date as modified_date,dt.created_by as created_by,"
			+ "dt.modified_by as modified_by,dt.active as active,dt.service_oriented as service_oriented) from DepartmentTags dt ")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM DepartmentTags dt where dt.tag_name=?1 and dt.active=true")
	public Integer countOfServiceTagName(String tag_name);
	
	@Query(value = "SELECT count(*) FROM DepartmentTags dt where dt.tag_short_name=?1 and dt.active=true")
	public Integer countOfServiceTagShortName(String tag_short_name);
}
