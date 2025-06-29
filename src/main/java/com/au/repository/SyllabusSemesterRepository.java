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
import com.au.model.SyllabusSemester;

@Transactional
@Repository
public interface SyllabusSemesterRepository extends JpaRepository<SyllabusSemester, Integer>{
	
	@Query(value = "select ss from SyllabusSemester ss where ss.active=true")
	public List<SyllabusSemester> findAll1();

	@Query(value = "select new map(ss.syllabus_sem_id as id,ss.created_Date as created_Date,"
			+ "ss.created_username as created_username,ss.active as active,ay.ac_year as ac_year,c.course_name as course_name) "
			+ "from SyllabusSemester ss left join Academic_year ay on ss.ac_year_id=ay.ac_year_id "
			+ "left join Course c on ss.course_id=c.course_id "
			+ "Where CONCAT(IfNull(ss.syllabus_sem_id,''),'',IfNull(ss.created_Date,''),'',IfNull(ss.created_username,''),'',IfNull(ay.ac_year,''),'',IfNull(c.course_name,'')) LIKE %?1%")
	public Page<Object> fetchAllDetails1(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(ss.syllabus_sem_id as id,ss.created_Date as created_Date,"
			+ "ss.created_username as created_username,ss.active as active,ay.ac_year as ac_year,c.course_name as course_name) "
			+ "from SyllabusSemester ss left join Academic_year ay on ss.ac_year_id=ay.ac_year_id "
			+ "left join Course c on ss.course_id=c.course_id")
	public Page<Object> fetchAllDetails2(Pageable pageable);

	@Modifying
	@Query(value = "update SyllabusSemester ss set ss.active=false where ss.syllabus_sem_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update SyllabusSemester ss set ss.active=true where ss.syllabus_sem_id=?1")
	public void update1(Integer id);



	

}
