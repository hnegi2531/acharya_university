package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CourseCategory;

@Repository
@Transactional
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Integer> {

	@Query(value = "select cc from CourseCategory cc where cc.active=true")
	public List<CourseCategory> findAll1();

	@Modifying
	@Query(value = "update CourseCategory cc set cc.active=false where cc.course_category_id=?1")
	public void updateCourseCategory(Integer id);

	@Modifying
	@Query(value = "update CourseCategory cc set cc.active=true where cc.course_category_id=?1")
	public void updateCourseCategory1(Integer id);
	
	@Query(value = "Select new map(cc.course_category_id as id,cc.course_category_name as course_category_name,"
			+ "cc.course_category_code as course_category_code,cc.created_date as created_date,cc.modified_date as modified_date,"
			+ "cc.created_by as created_by,cc.modified_by as modified_by,cc.active as active,cc.created_username as created_username,"
			+ "cc.modified_username as modified_username,cc.type as type) From CourseCategory cc "
			+ "Where CONCAT(IfNull(cc.course_category_id,''),'',IfNull(cc.course_category_name,''),'',IfNull(cc.course_category_code,''),'',IfNull(cc.created_date,''),'',IfNull(cc.created_by,''),'',IfNull(cc.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(cc.course_category_id as id,cc.course_category_name as course_category_name,"
			+ "cc.course_category_code as course_category_code,cc.created_date as created_date,cc.modified_date as modified_date,"
			+ "cc.created_by as created_by,cc.modified_by as modified_by,cc.active as active,cc.created_username as created_username,"
			+ "cc.modified_username as modified_username,cc.type as type) From CourseCategory cc")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM CourseCategory cc where cc.course_category_name = ?1 and cc.active =true")
	public Integer countCourseCategoryName(String course_category_name);
	
	@Query(value = "SELECT count(*) FROM CourseCategory cc where cc.course_category_code = ?1 and cc.active =true")
	public Integer countCourseCategoryCode(String course_category_code);
	
	@Query(value = "SELECT count(*) FROM CourseCategory cc where cc.course_category_id != ?1 and cc.course_category_name = ?2 and cc.active =true")
	public Integer countCourseCategoryNameForUpdate(Integer course_category_id,String course_category_name);
	
	@Query(value = "SELECT count(*) FROM CourseCategory cc where cc.course_category_id != ?1 and cc.course_category_code = ?2 and cc.active =true")
	public Integer countCourseCategoryCodeForUpdate(Integer course_category_id,String course_category_code);

}
