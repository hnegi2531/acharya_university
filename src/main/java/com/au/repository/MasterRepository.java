package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.Master;

@Transactional
@Repository
public interface MasterRepository extends JpaRepository<Master, Integer> {

	@Query(value = "select m from Master m where m.active=true")
	public List<Master> findAll1();
	
	@Query(value = "select new map(m.master_id as id,m.ac_year_id as ac_year_id,m.school_id as school_id,m.program_id as program_id,"
			+ "m.created_by as created_by,m.modified_by as modified_by,m.dept_id as dept_id,m.program_specialization_id as program_specialization_id,"
			+ "m.created_date as created_date,m.modified_date as modified_date,m.active as active,m.syllabus_id as syllabus_id,"
			+ "m.created_username as created_username,m.modified_username as modified_username) "
			+ "from Master m "
			+ "where CONCAT(IfNull(m.master_id,''),'',IfNull(m.ac_year_id,''),'',IfNull(m.school_id,''),"
			+ "'',IfNull(m.created_by,''),'',IfNull(m.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(m.master_id as id,m.ac_year_id as ac_year_id,m.school_id as school_id,m.program_id as program_id,"
			+ "m.created_by as created_by,m.modified_by as modified_by,m.dept_id as dept_id,m.program_specialization_id as program_specialization_id,"
			+ "m.created_date as created_date,m.modified_date as modified_date,m.active as active,m.syllabus_id as syllabus_id,"
			+ "m.created_username as created_username,m.modified_username as modified_username) "
			+ "from Master m")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update Master m set m.active=false where m.master_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Master m set m.active=true where m.master_id=?1")
	public void update1(Integer id);

}
