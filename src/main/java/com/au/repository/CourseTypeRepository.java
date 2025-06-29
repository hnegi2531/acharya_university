package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CourseType;

@Repository
@Transactional
public interface CourseTypeRepository extends JpaRepository<CourseType, Integer> {

	@Query(value = "select ct from CourseType ct where ct.active=true")
	public List<CourseType> findAll1();

	@Modifying
	@Query(value = "update CourseType ct set ct.active=false where ct.course_type_id=?1")
	public void updateCourseType(Integer id);

	@Modifying
	@Query(value = "update CourseType ct set ct.active=true where ct.course_type_id=?1")
	public void updateCourseType1(Integer id);
	
	@Query(value ="Select new map(ct.course_type_id as id,ct.course_type_name as course_type_name,ct.course_type_code as course_type_code,"
			+ "ct.created_date as created_date,ct.modified_date as modified_date,ct.created_by as created_by,ct.modified_by as modified_by,"
			+ "ct.active as active,ct.created_username as created_username,ct.modified_username) From CourseType ct "
			+ "Where CONCAT(IfNull(ct.course_type_id,''),'',IfNull(ct.course_type_name,''),'',IfNull(ct.course_type_code,''),'',IfNull(ct.created_date,''),'',IfNull(ct.created_by,''),'',IfNull(ct.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(ct.course_type_id as id,ct.course_type_name as course_type_name,ct.course_type_code as course_type_code,"
			+ "ct.created_date as created_date,ct.modified_date as modified_date,ct.created_by as created_by,ct.modified_by as modified_by,"
			+ "ct.active as active,ct.created_username as created_username,ct.modified_username) From CourseType ct")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM CourseType ct where ct.course_type_name=?1 and ct.active=true")
	public Integer countOfCourseTypeName(String course_type_name);
	
	@Query(value = "SELECT count(*) FROM CourseType ct where ct.course_type_code=?1 and ct.active=true")
	public Integer countOfCourseShortName(String course_type_code);

}
