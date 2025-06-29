package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.au.model.academic_school;

@Repository
public interface AcademicSchoolRepository extends JpaRepository<academic_school	, Integer>{

	@Query(value = "SELECT school_id FROM academic_school where ac_year_id=?1  ",nativeQuery = true)
	public List<Integer> findByAcYearSchoolId(Integer ac_year_id);

    @Query(value = "select a from academic_school a where a.active=true")
	public List<academic_school> findAll11();
	
	@Query(value = "Select new map(a.academic_school_id as id,a.ac_year_id as ac_year_id,"
			+ "a.school_id as school_id,a.from_date as from_date,a.to_date as to_date,a.created_date as created_date,"
			+ "a.modified_date as modified_date,a.created_by as created_by,a.modified_by as modified_by,a.active as active,"
			+ "a.created_username as created_username,a.modified_username as modified_username,ac.ac_year as ac_year,"
			+ "sc.school_name_short as school_name_short) from academic_school a "
			+ "left join Academic_year ac on a.ac_year_id=ac.ac_year_id left join Schools sc on a.school_id=sc.school_id "
			+ "Where CONCAT(IfNull(academic_school_id,''),'',IfNull(a.created_by,''),'',IfNull(a.created_date,''),'',IfNull(a.created_username,''),IfNull(sc.school_name_short,'')) LIKE %?1% ")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(a.academic_school_id as id,a.ac_year_id as ac_year_id,"
			+ "a.school_id as school_id,a.from_date as from_date,a.to_date as to_date,a.created_date as created_date,"
			+ "a.modified_date as modified_date,a.created_by as created_by,a.modified_by as modified_by,a.active as active,"
			+ "a.created_username as created_username,a.modified_username as modified_username,ac.ac_year as ac_year,"
			+ "sc.school_name_short as school_name_short) from academic_school a "
			+ "left join Academic_year ac on a.ac_year_id=ac.ac_year_id left join Schools sc on a.school_id=sc.school_id ")
	public Page<Object> findAll2(Pageable pageable);
	
	@Modifying
	@Query(value = "update academic_school a set a.active=false where a.academic_school_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update academic_school a set a.active=true where a.academic_school_id=?1")
	public void update1(Integer id);
	

	
}
