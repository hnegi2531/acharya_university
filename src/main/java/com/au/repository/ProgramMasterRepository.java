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
import com.au.model.ProgramMaster;

@Transactional
@Repository
public interface ProgramMasterRepository extends JpaRepository<ProgramMaster, Integer> {

	@Query(value = "select pm from ProgramMaster pm where pm.active=true")
	public List<ProgramMaster> findAll1();

	@Query(value = "select new map(pm.master_id as master_id,pm.max_duration_in_years as max_duration_in_years,"
			+ "pm.credits_completion_first_year as credits_completion_first_year,"
			+ "pm.credits_completion_second_year as credits_completion_second_year,"
			+ "pm.credits_completion_third_year as credits_completion_third_year,"
			+ "pm.credits_completion_fourth_year as credits_completion_fourth_year,pm.total_min_credits as total_min_credits,"
			+ "pm.total_max_credits as total_max_credits,pm.created_date as created_date,pm.active as active,"
			+ "pm.created_username as created_username,p.program_short_name as program_short_name,"
			+ "s.school_name_short as school_name_short,ms.major_name as major_name,d.dept_name_short as dept_name_short)"
			+ " from ProgramMaster pm left join Program p on pm.program_id=p.program_id "
			+ "left join Schools s on pm.school_id=s.school_id left join MajorStruc ms on pm.major_id=ms.major_id "
			+ "left join Department d on pm.dept_id=d.dept_id left join Syllabus sy on pm.syllabus_id=sy.syllabus_id")
	public List<HashMap<String, Object>> fetchAllDetails();
	
	@Query(value = "select new map(pm.master_id as id,pm.max_duration_in_years as max_duration_in_years,"
			+ "pm.credits_completion_first_year as credits_completion_first_year,"
			+ "pm.credits_completion_second_year as credits_completion_second_year,"
			+ "pm.credits_completion_third_year as credits_completion_third_year,"
			+ "pm.credits_completion_fourth_year as credits_completion_fourth_year,pm.total_min_credits as total_min_credits,"
			+ "pm.total_max_credits as total_max_credits,pm.created_date as created_date,pm.active as active,"
			+ "pm.created_username as created_username,p.program_short_name as program_short_name,"
			+ "s.school_name_short as school_name_short,ms.major_name as major_name,d.dept_name_short as dept_name_short)"
			+ " from ProgramMaster pm left join Program p on pm.program_id=p.program_id "
			+ "left join Schools s on pm.school_id=s.school_id left join MajorStruc ms on pm.major_id=ms.major_id "
			+ "left join Department d on pm.dept_id=d.dept_id left join Syllabus sy on pm.syllabus_id=sy.syllabus_id "
			+ "where CONCAT(IfNull(pm.master_id,''),'',IfNull(p.program_short_name,''),'',IfNull(s.school_name_short,''),"
			+ "'',IfNull(ms.major_name,''),'',IfNull(pm.created_username,''),'',IfNull(d.dept_name_short,''),'',IfNull(pm.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(pm.master_id as id,pm.max_duration_in_years as max_duration_in_years,"
			+ "pm.credits_completion_first_year as credits_completion_first_year,"
			+ "pm.credits_completion_second_year as credits_completion_second_year,"
			+ "pm.credits_completion_third_year as credits_completion_third_year,"
			+ "pm.credits_completion_fourth_year as credits_completion_fourth_year,pm.total_min_credits as total_min_credits,"
			+ "pm.total_max_credits as total_max_credits,pm.created_date as created_date,pm.active as active,"
			+ "pm.created_username as created_username,p.program_short_name as program_short_name,"
			+ "s.school_name_short as school_name_short,ms.major_name as major_name,d.dept_name_short as dept_name_short)"
			+ " from ProgramMaster pm left join Program p on pm.program_id=p.program_id "
			+ "left join Schools s on pm.school_id=s.school_id left join MajorStruc ms on pm.major_id=ms.major_id "
			+ "left join Department d on pm.dept_id=d.dept_id left join Syllabus sy on pm.syllabus_id=sy.syllabus_id")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update ProgramMaster pm set pm.active=false where pm.master_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update ProgramMaster pm set pm.active=true where pm.master_id=?1")
	public void update1(Integer id);

}
