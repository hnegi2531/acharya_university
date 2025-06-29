package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ExperienceDetails;

@Transactional
@Repository
public interface ExperienceDetailsRepository extends JpaRepository<ExperienceDetails, Integer> {

	@Query(value = "select new map(ed.exp_id as exp_id,ed.job_id as job_id,ed.employer_name as employer_name,ed.skills as skills,"
			+ "ed.designation as designation,ed.exp_in_years as exp_in_years,"
			+ "ed.exp_in_months as exp_in_months,ed.annual_salary_lakhs as annual_salary_lakhs) "
			+ "from ExperienceDetails ed where ed.job_id=?1")
	public List<HashMap<String, Object>> getExperienceDetails(Integer job_id);

	
	@Query(value = "SELECT ex.exp_id AS exp_id,ex.job_id AS job_id,ex.employer_name AS employer_name,ex.designation AS designation,"
			+ "ex.skills AS skills,ex.exp_in_years AS exp_in_years,ex.exp_in_months AS exp_in_months,"
			+ "ex.annual_salary_lakhs AS annual_salary_lakhs,ex.exp_doj AS exp_doj,ex.exp_dol AS exp_dol "
			+ "FROM experience_details ex where ex.job_id=?1",nativeQuery=true)
	public List<Map<String, Object>> experienceDetailsOnDate(Integer jid);

}
