package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.EducationDetails;

@Transactional
@Repository
public interface EducationDetailsRepository extends JpaRepository<EducationDetails, Integer>{

	@Query(value = "select new map(ed.edu_id as edu_id,ed.job_id as job_id,ed.graduation_id as graduation_id,"
			+ "ed.graduation as graduation,ed.school as school,ed.university as university,"
			+ "ed.academic_score as academic_score,ed.academic_year_joining as academic_year_joining,"
			+ "ed.academic_year_completed as academic_year_completed,"
			+ "ed.active as active,g.graduation_name as graduation_name) "
			+ "from EducationDetails ed left join Graduation g on ed.graduation_id=g.graduation_id where job_id=?1")
	public List<HashMap<String, Object>> getEducationDetails(Integer job_id);


	@Query(value = "SELECT ed.edu_id AS edu_id,ed.job_id AS job_id,ed.graduation_id AS graduation_id,ed.graduation AS graduation,"
			+ "ed.school AS school,ed.university AS university,ed.academic_score AS academic_score,"
			+ "	ed.academic_year_joining AS academic_year_joining,ed.academic_year_completed AS academic_year_completed,"
			+ "ed.active AS active,ed.convocation AS convocation,ed.attach AS attach FROM education_details ed where ed.job_id=?1",nativeQuery=true)
	public List<Map<String, Object>> educationDetailsOnDate(Integer jid);
	
	

}
