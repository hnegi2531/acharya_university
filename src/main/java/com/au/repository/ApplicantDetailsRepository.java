package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.ApplicantDetails;

@Repository
public interface ApplicantDetailsRepository extends JpaRepository<ApplicantDetails, Integer>{

	@Query(value = "SELECT * FROM applicant_details where applicant_id=?1",nativeQuery = true)
	public ApplicantDetails findByAppId(Integer applicant_id);
	
	@Query(value = "Select new map(ads.applicant_id as id,ads.board_university_id as board_university_id,bu.board_university_type as board_university_type ,ads.board_university as board_university,"
			+ "ads.college_name as college_name,ads.subjects_studied as subjects_studied,ads.marks_total as marks_total,"
			+ "ads.course as course,ads.total_obtained as total_obtained,ads.percentage_scored as percentage_scored,"
			+ "ads.entrance_exam_name as entrance_exam_name,ads.state as state,ads.year_of_entrance as year_of_entrance,"
			+ "ads.entrance_score as entrance_score,ads.first_language as first_language,ads.active as active,"
			+ "ads.created_username as created_username,ads.second_language as second_language,ads.auid as auid,"
			+ "ads.created_by as created_by,ads.created_date as created_date,ads.qualifying_year as qualifying_year,"
			+ "ads.remarks as remarks,ads.passed_year as passed_year,ads.candidate_id as candidate_id) From ApplicantDetails ads "
			+ "left join BoardUniversity bu on ads.board_university_id=bu.board_university_id "
			+ "Where CONCAT(IfNull(ads.applicant_id,''),'',IfNull(ads.college_name,''),'',IfNull(ads.course,'')"
			+ ",'',IfNull(ads.state,''),'',IfNull(ads.created_username,''),'',IfNull(ads.auid,''),'',IfNull(ads.created_by,'')"
			+ ",'',IfNull(ads.created_date,''),'',IfNull(ads.remarks,'')) LIKE %?1% ")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(ads.applicant_id as id,ads.board_university_id as board_university_id,bu.board_university_type as board_university_type,ads.board_university as board_university,"
			+ "ads.college_name as college_name,ads.subjects_studied as subjects_studied,ads.marks_total as marks_total,"
			+ "ads.course as course,ads.total_obtained as total_obtained,ads.percentage_scored as percentage_scored,"
			+ "ads.entrance_exam_name as entrance_exam_name,ads.state as state,ads.year_of_entrance as year_of_entrance,"
			+ "ads.entrance_score as entrance_score,ads.first_language as first_language,ads.active as active,"
			+ "ads.created_username as created_username,ads.second_language as second_language,ads.auid as auid,"
			+ "ads.created_by as created_by,ads.created_date as created_date,ads.qualifying_year as qualifying_year,"
			+ "ads.remarks as remarks,ads.passed_year as passed_year,ads.candidate_id as candidate_id) From ApplicantDetails ads left join BoardUniversity bu on ads.board_university_id=bu.board_university_id")
	public Page<Object> findAll2(Pageable pageable);

	
	@Query(value = "Select new map(  ads.applicant_id AS id,ads.board_university_id AS board_university_id,ads.qualifying_exam_year AS qualifying_exam_year,"
			+ "ads.std_id AS std_id,ads.board_university AS board_university,ads.college_name AS college_name,ads.subjects_studied AS subjects_studied,"
			+ "ads.marks_total AS marks_total,ads.course AS course,ads.total_obtained AS total_obtained,ads.percentage_scored AS percentage_scored,"
			+ "ads.entrance_exam_name AS entrance_exam_name,ads.state AS state,ads.year_of_entrance AS year_of_entrance,ads.entrance_score AS entrance_score,"
			+ "ads.first_language AS first_language,ads.active AS active,ads.created_username AS created_username,ads.modified_username AS modified_username,"
			+ "ads.second_language AS second_language,ads.auid AS auid,ads.optional_subject AS optional_subject,ads.optional_max_mark AS optional_max_mark,"
			+ "ads.optional_min_mark AS optional_min_mark,ads.optional_percentage AS optional_percentage,ads.entrance_exam_date AS entrance_exam_date,"
			+ "ads.rank_obtained AS rank_obtained,ads.created_by AS created_by,ads.modified_by AS modified_by,ads.created_date AS created_date,"
			+ "ads.modified_date AS modified_date,ads.qualifying_year AS qualifying_year,ads.remarks AS remarks,ads.passed_year AS passed_year,"
			+ "ads.candidate_id AS candidate_id, ads.pdf_content AS pdf_content,"
			+ "sd.acharya_email As acharya_email,sd.student_name As student_name,sd.auid As auid,bu.board_university_type as board_university_type ) From ApplicantDetails ads "
			+ "left join BoardUniversity bu on ads.board_university_id=bu.board_university_id "
			+ "left join Student_Details sd on ads.std_id=sd.student_id "
			+ "where ads.std_id=?1 And sd.active=true")
	public List<Map<String, Object>> getApplicationDetails(Integer studentId);
	
}
