package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Graduation;

@Transactional
@Repository
public interface Graduation_Repository extends JpaRepository<Graduation	, Integer>{

	
	@Query(value = "select new map(g.graduation_id as id,g.graduation_name as graduation_name,g.graduation_name_short as graduation_name_short,"
			+ "g.created_Date as created_Date,g.active as active,g.created_by as created_by,g.created_username as created_username,"
			+ "g.modified_username as modified_username) from Graduation g "
			+ "where CONCAT(IfNull(g.graduation_name,''),'',IfNull(g.graduation_name_short,''),'',IfNull(g.created_Date,''),"
			+ "'',IfNull(g.created_username,''),'',IfNull(g.created_by,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(g.graduation_id as id,g.graduation_name as graduation_name,g.graduation_name_short as graduation_name_short,"
			+ "g.created_Date as created_Date,g.active as active,g.created_username as created_username,"
			+ "g.modified_username as modified_username) from Graduation g")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "select g from Graduation g where g.active=true")
	public List<Graduation> findAll1();
	
	@Modifying
	@Query(value = "update Graduation g set g.active=false where g.graduation_id=?1")
	public void update(Integer fee_template_id);
	

	@Modifying
	@Query(value = "update Graduation g set g.active=true where g.graduation_id=?1")
	public void update1(Integer id);
	
	@Query(value = "SELECT count(*) FROM Graduation g where g.graduation_name=?1 and g.active=true")
	public Integer countOfGraduationName(String graduation_name);
	
	@Query(value = "SELECT count(*) FROM Graduation g where g.graduation_name_short=?1 and g.active=true")
	public Integer countOfGraduationNameShort(String graduation_name_short);

}
