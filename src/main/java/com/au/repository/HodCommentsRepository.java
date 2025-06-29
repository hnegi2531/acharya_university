package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.HodComments;

@Transactional
@Repository
public interface HodCommentsRepository extends JpaRepository<HodComments, Integer> {

	@Query(value = "select new map(hc.hod_comment_id as hod_comment_id,hc.created_by as created_by,"
			+ "hc.created_username as created_username,hc.active as active,jp.job_id as job_id,jp.firstname as firstname,"
			+ "jp.reference_no as reference_no,jp.resume_headline as resume_headline,jp.email as email,"
			+ "jp.key_skills as key_skills,hc.created_date as created_date,g.graduation_name_short as graduation_name_short,"
			+ "ed.graduation as graduation) from HodComments hc join JobProfile jp on jp.job_id=hc.job_id "
			+ "join EducationDetails ed on hc.job_id=ed.job_id join Graduation g on ed.graduation_id=g.graduation_id")
	public List<HashMap<String, Object>> findAll1();
	
//	@Query(value = "select new map(hc.hod_comment_id as hod_comment_id,hc.created_by as created_by,"
//			+ "hc.created_username as created_username,hc.active as active,hc.job_id as job_id,jp.firstname as firstname,"
//			+ "jp.reference_no as reference_no,jp.resume_headline as resume_headline,jp.email as email,"
//			+ "jp.key_skills as key_skills,hc.created_date as created_date,g.graduation_name_short as graduation_name_short,"
//			+ "ed.graduation as graduation) from HodComments hc join JobProfile jp on jp.job_id=hc.job_id "
//			+ "join EducationDetails ed on hc.job_id=ed.job_id join Graduation g on ed.graduation_id=g.graduation_id where hc.job_id=?1")
//	public List<Map<String, Object>> fetchAllHodComment(Integer job_id);
	
	@Query(value = "select hc.job_id,hc.modified_date,hc.hod_comments,jp.firstname,sc.school_name_short,d.dept_name_short "
			+ "from hod_comments hc join user_details ud on hc.created_by=ud.id join employee_details ed "
			+ "on ud.email=ed.email join schools sc on ed.school_id=sc.school_id join department d "
			+ "on ed.dept_id=d.dept_id join job_profile jp on hc.job_id=jp.job_id where hc.job_id=?1", nativeQuery = true)
	public List<Map<String, Object>> fetchAllHodComment(Integer job_id);
	
	@Query(value = "select new map(hc.hod_comment_id as id,hc.hod_comments as hod_comments,hc.job_id as job_id,"
			+ "hc.created_by as created_by,hc.modified_by as modified_by,"
			+ "hc.created_date as created_date,hc.modified_date as modified_date,hc.active as active,"
			+ "hc.created_username as created_username,hc.modified_username as modified_username) "
			+ "from HodComments hc "
			+ "where CONCAT(IfNull(hc.hod_comment_id,''),'',IfNull(hc.hod_comments,''),'',IfNull(hc.job_id,''),"
			+ "'',IfNull(hc.active,''),'',IfNull(hc.created_by,''),'',IfNull(hc.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(hc.hod_comment_id as id,hc.hod_comments as hod_comments,hc.job_id as job_id,"
			+ "hc.created_by as created_by,hc.modified_by as modified_by,"
			+ "hc.created_date as created_date,hc.modified_date as modified_date,hc.active as active,"
			+ "hc.created_username as created_username,hc.modified_username as modified_username) "
			+ "from HodComments hc")
	public Page<Object> getAllSortedData(Pageable pageable);

}
