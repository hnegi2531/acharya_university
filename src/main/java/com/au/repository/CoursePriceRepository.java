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
import com.au.model.CoursePrice;

@Transactional
@Repository
public interface CoursePriceRepository extends JpaRepository<CoursePrice, Integer> {
	
	@Query(value = "select cp from CoursePrice cp where cp.active=true")
	public List<CoursePrice> findAll1();
	
	@Query(value = "select new map(cp.course_price_id as course_price_id,cp.year_sem as year_sem,cp.course_mode as course_mode,"
			+ "cp.course_price as course_price,cp.course_price_usd as course_price_usd,cp.created_date as created_date,"
			+ "cp.created_by as created_by,cp.created_username as created_username,cp.active as active,ay.ac_year as ac_year,"
			+ "s.school_name_short as school_name_short,p.program_short_name as program_short_name,"
			+ "d.dept_name_short as dept_name_short,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "c.course_code as course_code,cc.course_category_code as course_category_code) from CoursePrice cp "
			+ "left join Academic_year ay on cp.ac_year_id=ay.ac_year_id "
			+ "left join Schools s on cp.school_id=s.school_id "
			+ "left join Program p on cp.program_id=p.program_id "
			+ "left join Department d on cp.dept_id=d.dept_id "
			+ "left join ProgramSpecilization ps on cp.program_specialization_id=ps.program_specialization_id "
			+ "left join Course c on cp.course_id=c.course_id "
			+ "left join CourseCategory cc on cp.course_category_id=cc.course_category_id "
			+ "Where CONCAT(IfNull(cp.course_price_id,''),'',IfNull(cp.course_mode,''),'',IfNull(cp.course_price,''),'',"
			+ "IfNull(cp.course_price_usd,''),'',IfNull(cp.created_date,''),'',IfNull(s.school_name_short,''),'',"
			+ "IfNull(p.program_short_name,''),'',IfNull(dept_name_short,''),'',IfNull(ps.program_specialization_short_name,''),'',"
			+ "IfNull(c.course_code,''),'',IfNull(cc.course_category_code,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	
	@Query(value = "select new map(cp.course_price_id as course_price_id,cp.year_sem as year_sem,cp.course_mode as course_mode,"
			+ "cp.course_price as course_price,cp.course_price_usd as course_price_usd,cp.created_date as created_date,"
			+ "cp.created_by as created_by,cp.created_username as created_username,cp.active as active,ay.ac_year as ac_year,"
			+ "s.school_name_short as school_name_short,p.program_short_name as program_short_name,"
			+ "d.dept_name_short as dept_name_short,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "c.course_code as course_code,cc.course_category_code as course_category_code) from CoursePrice cp "
			+ "left join Academic_year ay on cp.ac_year_id=ay.ac_year_id "
			+ "left join Schools s on cp.school_id=s.school_id "
			+ "left join Program p on cp.program_id=p.program_id "
			+ "left join Department d on cp.dept_id=d.dept_id "
			+ "left join ProgramSpecilization ps on cp.program_specialization_id=ps.program_specialization_id "
			+ "left join Course c on cp.course_id=c.course_id "
			+ "left join CourseCategory cc on cp.course_category_id=cc.course_category_id")
	public Page<Object> findAll3(Pageable pageable);

	@Modifying
	@Query(value = "update CoursePrice cp set cp.active=false where cp.course_price_id=?1")
	public void updateCoursePrice(Integer id);

	@Modifying
	@Query(value = "update CoursePrice cp set cp.active=true where cp.course_price_id=?1")
	public void updateCoursePrice1(Integer id);

}
