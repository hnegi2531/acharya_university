package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CoursePattern;
import com.au.model.Department;

@Transactional
@Repository
public interface CoursePatternRepository extends JpaRepository<CoursePattern, Integer>{

	@Query(value = "select count(*) from CoursePattern cp where cp.ac_year_id=?1 and cp.school_id=?2 and cp.course_category_id=?3 and cp.program_id=?4 and cp.active=true")
	public Integer getCountOfCoursePattern(Integer ac_year_id,Integer school_id,Integer course_category_id,Integer program_id);
	
	@Query(value = "select new map(cp.course_pattern_id as id,cp.ac_year_id as ac_year_id,cp.school_id as school_id,"
			+ "cp.percentage_of_credit as percentage_of_credit,cp.credits as credits,cp.program_id as program_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "ac.ac_year_code as ac_year_code,ac.ac_year as ac_year,cc.course_category_name as course_category_name,"
			+ "cc.course_category_code as course_category_code,p.program_name as program_name,p.program_short_name as program_short_name,"
			+ "cp.created_username as created_username,cp.modified_username as modified_username,cp.course_category_id as course_category_id,"
			+ "cp.created_date as created_date,cp.modified_date as modified_date,cp.created_by as created_by,"
			+ "cp.modified_by as modified_by,cp.active as active) from CoursePattern cp "
			+ "left join Schools sc on cp.school_id=sc.school_id "
			+ "left join Academic_year ac on cp.ac_year_id=ac.ac_year_id "
			+ "left join CourseCategory cc on cp.course_category_id=cc.course_category_id "
			+ "left join Program p on cp.program_id=p.program_id "
			+ "where CONCAT(IfNull(cp.course_pattern_id,''),'',IfNull(cp.ac_year_id,''),'',IfNull(cp.school_id,''),'',IfNull(cp.course_category_id,''),"
			+ "'',IfNull(cp.created_by,''),'',IfNull(cp.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(cp.course_pattern_id as id,cp.ac_year_id as ac_year_id,cp.school_id as school_id,"
			+ "cp.percentage_of_credit as percentage_of_credit,cp.credits as credits,cp.program_id as program_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "ac.ac_year_code as ac_year_code,ac.ac_year as ac_year,cc.course_category_name as course_category_name,"
			+ "cc.course_category_code as course_category_code,p.program_name as program_name,p.program_short_name as program_short_name,"
			+ "cp.created_username as created_username,cp.modified_username as modified_username,cp.course_category_id as course_category_id,"
			+ "cp.created_date as created_date,cp.modified_date as modified_date,cp.created_by as created_by,"
			+ "cp.modified_by as modified_by,cp.active as active) from CoursePattern cp "
			+ "left join Schools sc on cp.school_id=sc.school_id "
			+ "left join Academic_year ac on cp.ac_year_id=ac.ac_year_id "
			+ "left join CourseCategory cc on cp.course_category_id=cc.course_category_id "
			+ "left join Program p on cp.program_id=p.program_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "SELECT cp from CoursePattern cp where cp.active=true")
	public List<CoursePattern> findAll11();
	
	@Modifying
	@Query(value = "update CoursePattern cp set cp.active=false where cp.course_pattern_id=?1")
	public void deactivateCoursePattern(Integer id);
	
	@Modifying
	@Query(value = "update CoursePattern cp set cp.active=true where cp.course_pattern_id=?1")
	public void activateCoursePattern(Integer id);
	
	
	
}
