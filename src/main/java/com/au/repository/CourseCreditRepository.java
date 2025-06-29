package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.CourseCredit;

@Transactional
@Repository
public interface CourseCreditRepository extends JpaRepository<CourseCredit, Integer> {

	@Query(value = "select cc from CourseCredit cc where cc.active=true")
	public List<CourseCredit> findAll11();

//	@Query(value = "select new map(cc.course_credit__id as course_credit__id,cc.min_credit as min_credit,"
//			+ "cc.max_credit as max_credit,cc.created_date as created_date,cc.active as active,"
//			+ "cc.created_username as created_username,cc.program_type as program_type,"
//			+ "ps.program_specialization_short_name as program_specialization_short_name)"
//			+ "from CourseCredit cc left join ProgramSpecilization ps "
//			+ "on cc.program_specialization_id=ps.program_specialization_id")
//	public List<HashMap<String, Object>> fetchAllDetails();
	
	@Query(value = "select new map(cc.course_credit__id as id,cc.min_credit as min_credit,"
			+ "cc.max_credit as max_credit,cc.created_date as created_date,cc.active as active,"
			+ "cc.created_username as created_username,cc.program_type as program_type,"
			+ "ps.program_specialization_short_name as program_specialization_short_name) "
			+ "from CourseCredit cc left join ProgramSpecilization ps "
			+ "on cc.program_specialization_id=ps.program_specialization_id "
			+ "Where CONCAT(IfNull(cc.course_credit__id,''),'',IfNull(cc.min_credit,''),'',IfNull(cc.max_credit,''),'',"
			+ "IfNull(cc.created_date,''),'',IfNull(cc.created_username,''),'',IfNull(cc.program_type,''),'',IfNull(ps.program_specialization_short_name,'')) LIKE %?1%")
	public Page<Object> fetchAllDetails1(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(cc.course_credit__id as id,cc.min_credit as min_credit,"
			+ "cc.max_credit as max_credit,cc.created_date as created_date,cc.active as active,"
			+ "cc.created_username as created_username,cc.program_type as program_type,"
			+ "ps.program_specialization_short_name as program_specialization_short_name) "
			+ "from CourseCredit cc left join ProgramSpecilization ps "
			+ "on cc.program_specialization_id=ps.program_specialization_id")
	public Page<Object> fetchAllDetails2(Pageable pageable);

	@Modifying
	@Query(value = "update CourseCredit cc set cc.active=false where cc.course_credit__id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update CourseCredit cc set cc.active=true where cc.course_credit__id=?1")
	public void update1(Integer id);

}
