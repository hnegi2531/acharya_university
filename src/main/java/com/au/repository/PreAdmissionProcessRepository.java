package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.FeeSubAmountDetailsForCandidate;
import com.au.dto.FeeTemplateDetailsForCandidate;
import com.au.model.Candidate_Walkin;
import com.au.model.PreAdmissionProcess;

@Repository
@Transactional
public interface PreAdmissionProcessRepository extends JpaRepository<PreAdmissionProcess, Integer> {

	@Query(value = "select new map(pap.candidate_id as candidate_id) from PreAdmissionProcess pap")
	public List<HashMap<String, Object>> findAllcandidateId();

	@Query(value = "select cw.candidate_name as candidateName, cw.candidate_id as candidateId,cw.application_no_npf as application_no_npf,"
			+ "cw.username as username, cw.visitor_id as visitorId, cw.candidate_sex as candidateSex, cw.date_of_birth as dateOfBirth,cw.npf_status as npf_status,"
			+ "cw.candidate_last_name as candidateLastName, cw.mobile_number as mobileNumber,cw.aadhar As aadhar,"
			+ "cw.ac_year_id as acYearId, cw.created_username as createdUsername, cw.modified_username as modifiedUsername,"
			+ "cw.father_name as fatherName, cw.city_id as cityId, cw.state_id as stateId, cw.country_id as countryId, cw.school_id as schoolId,"
			+ "cw.program_id as programId, cw.program_specilaization_id as programSpecializationId, cw.remarks as remarks, cw.rep_name as repName,"
			+ "cw.active as active, cw.candidate_email as candidateEmail, cw.nationality as nationality,cw.rural_urban As rural_urban,"
			+ "cw.category as category, cw.caste as caste, cw.religion as religion, cw.place_of_birth as placeOfBirth, cw.blood_group as bloodGroup,"
			+ "cw.present_address as presentAddress, cw.present_pincode as presentPincode, cw.present_country as presentCountry, cw.present_state as presentState,cw.present_city_id as presentCity,"
			+ "cw.permanent_address as permanentAddress, cw.permanent_pincode as permanentPincode, cw.permanent_country as permanentCountry,"
			+ "cw.permanent_state as permanentState, cw.permanent_city as permanentCity, cw.father_occupation as fatherOccupation, cw.father_email as fatherEmail,"
			+ "cw.father_mobile as fatherMobile, cw.mother_name as motherName, cw.mother_mobile as motherMobile, cw.guardian_name as guardianName,"
			+ "cw.guardian_mobile as guardianMobile,cw.permanant_adress1 As permanant_adress1,cw.is_nri As cw_is_nri,cw.entrance_exam_result As entrance_exam_result,"
			+ "cw.entrance_exam_date As entrance_exam_date,cw.rank_obtained As rank_obtained,cw.alternate_number As alternate_number,"
			+ "pap.school_id as school_id,pap.program_assignment_id as program_assignment_id,pap.program_id as program_id,"
			+ "cw.father_annual_income As father_annual_income,cw.father_qualification As father_qualification,cw.mother_annual_income As mother_annual_income,"
			+ "cw.mother_email As mother_email,cw.mother_occupation As mother_occupation,cw.mother_qualification As mother_qualification,"
			+ "cw.guardian_email As guardian_email,cw.guardian_occupation As guardian_occupation,"
			+ "cw.sslc_school_name As sslc_school_name,cw.sslc_year_of_passing As sslc_year_of_passing,cw.sslc_board As sslc_board,cw.sslc_registration_number As sslc_registration_number,"
			+ "cw.sslc_percentage_grade As sslc_percentage_grade,cw.sslc_subject_max_marks AS sslc_subject_max_marks,cw.sslc_subject_marks_obtain As sslc_subject_marks_obtain,"
			+ "cw.ug_percentage_grade AS ug_percentage_grade,cw.ug_subject_max_marks AS ug_subject_max_marks,cw.puc_school_name AS puc_school_name,"
			+ "cw.puc_board AS puc_board,cw.is_puc_result AS is_puc_result,cw.puc_mode_of_study AS puc_mode_of_study,cw.puc_registration_number AS puc_registration_number,"
			+ "cw.puc_year_of_passing AS puc_year_of_passing,cw.puc_percentage_grade AS puc_percentage_grade,cw.puc_subjects AS puc_subjects,"
			+ "cw.puc_subject_max_marks AS puc_subject_max_marks,cw.puc_subject_marks_obtain AS puc_subject_marks_obtain,cw.puc_percentage_obtain AS puc_percentage_obtain,"
			+ "cw.ug_board AS ug_board,cw.ug_registration_number AS ug_registration_number,cw.ug_school_name AS ug_school_name,"
			+ "cw.ug_year_of_passing AS ug_year_of_passing,cw.ug_subject_marks_obtain AS ug_subject_marks_obtain,"
			+ "cw.present_address1 As present_address1,cw.marital_status As marital_status,cw.whatsapp_number As whatsapp_number,"
			+ "ad.applicant_id AS applicant_id,ad.board_university_id AS board_university_id,ad.qualifying_exam_year AS qualifying_exam_year,"
			+ "ad.std_id AS std_id,ad.board_university AS board_university,ad.college_name AS college_name,ad.subjects_studied AS subjects_studied,"
			+ "ad.marks_total AS marks_total,ad.course AS course,ad.total_obtained AS total_obtained,ad.percentage_scored AS percentage_scored,"
			+ "ad.entrance_exam_name AS entrance_exam_name,ad.state AS state,ad.year_of_entrance AS year_of_entrance,"
			+ "ad.entrance_score AS entrance_score,ad.first_language AS first_language,ad.second_language AS second_language,"
			+ "ad.optional_max_mark AS optional_max_mark,ad.optional_min_mark AS optional_min_mark,ad.optional_subject AS optional_subject,"
			+ "ad.optional_percentage AS optional_percentage,ad.entrance_exam_date AS entrance_exam_dateAd,ad.rank_obtained AS rank_obtainedAd,"
			+ "ad.qualifying_year AS qualifying_year,ad.remarks AS remarksAd,ad.passed_year AS passed_year,ad.pdf_content AS pdf_content,"
			+ "pap.program_specialization_id as program_specialization_id,pap.ac_year_id as ac_year_id,"
			+ "pap.fee_template_id as fee_template_id,ft.lat_year_sem as lat_year_sem, fac.is_regular as is_regular,"
			+ "ft.fee_template_name as fee_template_name,pap.fee_admission_category_id as fee_admission_category_id,"
			+ "ps.program_specialization_name as program_specialization_name,pap.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name, ft.is_nri as is_nri,"
			+ "pap.program_start as program_start,ay.ac_year as ac_year,cw.link_exp as link_exp,"
			+ "pass.number_of_semester as number_of_semester, pass.number_of_years as number_of_years, pass.program_type as program_type,"
			+ "pg.program_name as program_name,pg.program_short_name as program_short_name,fac.year_sem as year_sem,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,"
			+ "pt.program_type_name as feeTemplate_program_type_name,ft.laptop_status as laptop_status,"
			+ "ct.name as permanentCityName,st.name as permanentStateName,con.name as permanentCountryName,org.org_id As org_id,org.org_name as org_name,org.org_type As org_type,"
			+ "nat.nationality As nationalityName,ct1.name as presentCityName,st1.name as presentStateName,con1.name as presentCountryName,"
			+ "soa.created_date as offerAcceptedDate,soa.ip_address as ip_address, pap.created_date as preAdmissionCreatedDate "
			+ "from preadmission_process pap "
			+ "left join fee_admission_category fac on pap.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join academic_year ay on pap.ac_year_id=ay.ac_year_id "
			+ "left join program_specialization ps on pap.program_specialization_id=ps.program_specialization_id "
			+ "left join fee_template ft on pap.fee_template_id=ft.fee_template_id "
			+ "left join program_type pt on pt.program_type_id=ft.program_type_id "
			+ "left join program pg on pap.program_id=pg.program_id "
			+ "left join program_assignment pass on pass.program_id=pg.program_id AND pass.program_assignment_id = "
			+ "(SELECT MAX(pa.program_assignment_id) FROM program_assignment pa WHERE pa.program_id = pg.program_id ) "
			+ "left join schools sc on pap.school_id=sc.school_id "
			+ "left join organization org on org.org_id=sc.org_id "
			+ "left join candidate_walkin cw on pap.candidate_id=cw.candidate_id "
			+ "left join nationality nat on nat.nationality_id=cw.nationality "
			+ "left join applicant_details ad on ad.candidate_id=cw.candidate_id "
			+ "left join cities ct on ct.id = cw.permanent_city "
			+ "left join states st on st.id = cw.permanent_state "
			+ "left join countries con on con.id = cw.permanent_country "
			+ "left join cities ct1 on ct1.id = cw.present_city_id "
			+ "left join states st1 on st1.id = cw.present_state "
			+ "left join countries con1 on con1.id = cw.present_country "
			+ "left join student_offer_acceptance soa on soa.candidate_id=cw.candidate_id "
			+ "where pap.candidate_id=?1 and pap.active=true order by soa.student_offer_acceptance_id desc limit 1", nativeQuery=true)
	public List<Map<String, Object>> findAllCandidateDetails(Integer candidate_id);
	
	@Query(value = "select cw.candidate_name as candidateName, cw.candidate_id as candidateId,cw.application_no_npf as application_no_npf,"
			+ "cw.username as username, cw.visitor_id as visitorId, cw.candidate_sex as candidateSex, cw.date_of_birth as dateOfBirth,cw.npf_status as npf_status,"
			+ "cw.candidate_last_name as candidateLastName, cw.mobile_number as mobileNumber,cw.aadhar As aadhar,"
			+ "cw.ac_year_id as acYearId, cw.created_username as createdUsername, cw.modified_username as modifiedUsername,"
			+ "cw.father_name as fatherName, cw.city_id as cityId, cw.state_id as stateId, cw.country_id as countryId, cw.school_id as schoolId,"
			+ "cw.program_id as programId, cw.program_specilaization_id as programSpecializationId, cw.remarks as remarks, cw.rep_name as repName,"
			+ "cw.active as active, cw.candidate_email as candidateEmail, cw.nationality as nationality,cw.rural_urban As rural_urban,"
			+ "cw.category as category, cw.caste as caste, cw.religion as religion, cw.place_of_birth as placeOfBirth, cw.blood_group as bloodGroup,"
			+ "cw.present_address as presentAddress, cw.present_pincode as presentPincode, cw.present_country as presentCountry, cw.present_state as presentState,cw.present_city_id as presentCity,"
			+ "cw.permanent_address as permanentAddress, cw.permanent_pincode as permanentPincode, cw.permanent_country as permanentCountry,"
			+ "cw.permanent_state as permanentState, cw.permanent_city as permanentCity, cw.father_occupation as fatherOccupation, cw.father_email as fatherEmail,"
			+ "cw.father_mobile as fatherMobile, cw.mother_name as motherName, cw.mother_mobile as motherMobile, cw.guardian_name as guardianName,"
			+ "cw.guardian_mobile as guardianMobile,cw.permanant_adress1 As permanant_adress1,cw.is_nri As cw_is_nri,cw.entrance_exam_result As entrance_exam_result,"
			+ "cw.entrance_exam_date As entrance_exam_date,cw.rank_obtained As rank_obtained,cw.alternate_number As alternate_number,"
			+ "cw.father_annual_income As father_annual_income,cw.father_qualification As father_qualification,cw.mother_annual_income As mother_annual_income,"
			+ "cw.mother_email As mother_email,cw.mother_occupation As mother_occupation,cw.mother_qualification As mother_qualification,"
			+ "cw.guardian_email As guardian_email,cw.guardian_occupation As guardian_occupation,"
			+ "cw.sslc_school_name As sslc_school_name,cw.sslc_year_of_passing As sslc_year_of_passing,cw.sslc_board As sslc_board,cw.sslc_registration_number As sslc_registration_number,"
			+ "cw.sslc_percentage_grade As sslc_percentage_grade,cw.sslc_subject_max_marks AS sslc_subject_max_marks,cw.sslc_subject_marks_obtain As sslc_subject_marks_obtain,"
			+ "cw.ug_percentage_grade AS ug_percentage_grade,cw.ug_subject_max_marks AS ug_subject_max_marks,cw.puc_school_name AS puc_school_name,"
			+ "cw.puc_board AS puc_board,cw.is_puc_result AS is_puc_result,cw.puc_mode_of_study AS puc_mode_of_study,cw.puc_registration_number AS puc_registration_number,"
			+ "cw.puc_year_of_passing AS puc_year_of_passing,cw.puc_percentage_grade AS puc_percentage_grade,cw.puc_subjects AS puc_subjects,"
			+ "cw.puc_subject_max_marks AS puc_subject_max_marks,cw.puc_subject_marks_obtain AS puc_subject_marks_obtain,cw.puc_percentage_obtain AS puc_percentage_obtain,"
			+ "cw.ug_board AS ug_board,cw.ug_registration_number AS ug_registration_number,cw.ug_school_name AS ug_school_name,"
			+ "cw.ug_year_of_passing AS ug_year_of_passing,cw.ug_subject_marks_obtain AS ug_subject_marks_obtain,"
			+ "cw.present_address1 As present_address1,cw.marital_status As marital_status,cw.whatsapp_number As whatsapp_number,"
			+ "ad.applicant_id AS applicant_id,ad.board_university_id AS board_university_id,ad.qualifying_exam_year AS qualifying_exam_year,"
			+ "ad.std_id AS std_id,ad.board_university AS board_university,ad.college_name AS college_name,ad.subjects_studied AS subjects_studied,"
			+ "ad.marks_total AS marks_total,ad.course AS course,ad.total_obtained AS total_obtained,ad.percentage_scored AS percentage_scored,"
			+ "ad.entrance_exam_name AS entrance_exam_name,ad.state AS state,ad.year_of_entrance AS year_of_entrance,"
			+ "ad.entrance_score AS entrance_score,ad.first_language AS first_language,ad.second_language AS second_language,"
			+ "ad.optional_max_mark AS optional_max_mark,ad.optional_min_mark AS optional_min_mark,ad.optional_subject AS optional_subject,"
			+ "ad.optional_percentage AS optional_percentage,ad.entrance_exam_date AS entrance_exam_dateAd,ad.rank_obtained AS rank_obtainedAd,"
			+ "ad.qualifying_year AS qualifying_year,ad.remarks AS remarksAd,ad.passed_year AS passed_year,ad.pdf_content AS pdf_content,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,"
			+ "cw.link_exp as link_exp,"
			+ "pass.number_of_semester as number_of_semester, pass.number_of_years as number_of_years, pass.program_type as program_type,"
			+ "pg.program_name as program_name,pg.program_short_name as program_short_name,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "pt.program_type_name as feeTemplate_program_type_name,"
			+ "ct.name as permanentCityName,st.name as permanentStateName,con.name as permanentCountryName,org.org_id As org_id,org.org_name as org_name,org.org_type As org_type,"
			+ "nat.nationality As nationalityName,ct1.name as presentCityName,st1.name as presentStateName,con1.name as presentCountryName,"
			+ "soa.created_date as offerAcceptedDate,soa.ip_address as ip_address "
			+ "from candidate_walkin cw "
			+ "left join program_specialization ps on cw.program_specilaization_id=ps.program_specialization_id "
			+ "left join program pg on cw.program_id=pg.program_id "
			+ "left join program_assignment pass on pass.program_id=pg.program_id AND pass.program_assignment_id = "
			+ "(SELECT MAX(pa.program_assignment_id) FROM program_assignment pa WHERE pa.program_id = pg.program_id ) "
			+ "left join program_type pt on pt.program_type_id=pass.program_type_id "
			+ "left join schools sc on cw.school_id=sc.school_id "
			+ "left join organization org on org.org_id=sc.org_id "
			+ "left join nationality nat on nat.nationality_id=cw.nationality "
			+ "left join applicant_details ad on ad.candidate_id=cw.candidate_id "
			+ "left join cities ct on ct.id = cw.permanent_city "
			+ "left join states st on st.id = cw.permanent_state "
			+ "left join countries con on con.id = cw.permanent_country "
			+ "left join cities ct1 on ct1.id = cw.present_city_id "
			+ "left join states st1 on st1.id = cw.present_state "
			+ "left join countries con1 on con1.id = cw.present_country "
			+ "left join student_offer_acceptance soa on soa.candidate_id=cw.candidate_id "
			+ "where cw.candidate_id=?1 and cw.active=true order by soa.student_offer_acceptance_id desc limit 1", nativeQuery=true)
	public List<Map<String, Object>> findAllDetailsPreAdmission1(Integer candidate_id);

	@Query(value = "select new map(p.school_id as school_id,p.program_assignment_id as program_assignment_id,sch.school_name as school_name,s.scholarship_id as scholarship_id,"
			+ "sch.school_name_short as school_name_short,p.program_id as program_id"
			+ ",pr.program_name as program_name ,pr.program_short_name as program_short_name,"
			+ "p.program_specialization_id as program_specialization_id,ps.program_specialization_name as program_specialization_name,"
			+ "p.fee_admission_category_id as fee_admission_category_id,"
			+ "fee.fee_admission_category_type as fee_admission_category_type,p.ac_year_id as ac_year_id,"
			+ "ay.ac_year as ac_year,p.fee_template_id as fee_template_id,ft.fee_template_name as fee_template_name,"
			+ "ft.currency_type_id as currency_type_id,cu.currency_type_name as currency_type_name) "
			+ "from PreAdmissionProcess p left join  Schools sch on p.school_id=sch.school_id"
			+ " left join Program pr on p.program_id= pr.program_id left join ProgramSpecilization ps"
			+ " on p.program_specialization_id = ps.program_specialization_id "
			+ " left join FeeAdmissionCategory fee on p.fee_admission_category_id= fee.fee_admission_category_id "
			+ " left join Academic_year ay on p.ac_year_id=ay.ac_year_id "
			+ " left join Scholarship s on p.candidate_id=s.candidate_id "
			+ "left join FeeTemplate ft on p.fee_template_id=ft.fee_template_id "
			+ "left join Currency_Type cu on ft.currency_type_id=cu.currency_type_id     where p.candidate_id=?1  and p.active=true   ")
	public List<HashMap<String, Object>> listAll2(Integer cid);

	@Query(value = "select new map(sas.scholarship_approved_status_id as scholarship_approved_status_id,s.scholarship_id as scholarship_id,"
			+ "p.candidate_id as candidate_id,sas.approved_by as approved_by,"
			+ "sas.is_approved as is_approved,sas.counselor_id as counselor_id,"
			+ "sas.prev_approved_amount as prev_approved_amount,sas.approved_amount as approved_amount,"
			+ "p.student_name as student_name,ft.fee_template_name as fee_template_name,"
			+ "s.requested_scholarship as requested_scholarship,s.created_date as created_date)"
			+ " from Scholarship s  " + "inner join PreAdmissionProcess  p  on s.candidate_id=p.candidate_id  "
			+ "inner join FeeTemplate ft on ft.fee_template_id=p.fee_template_id  "
			+ "inner join ScholarshipApprovalStatus sas on sas.candidate_id=p.candidate_id where p.active=true")
	public List<HashMap<String, Object>> getdetails();

	@Query(value = "select new map(pap.pre_admission_id as id,pap.student_name as student_name,pap.is_scholarship as is_scholarship,"
			+ "pap.date_of_birth as date_of_birth,pap.address as address,pap.process_type as process_type,pap.active as active,"
			+ "pap.created_by as created_by,pap.modified_by as modified_by,pap.created_date as created_date,pap.modified_date as modified_date,"
			+ "pap.gender as gender,pap.mobile as mobile,pap.receipt as receipt,pap.created_username as created_username,"
			+ "pap.modified_username as modified_username,ft.fee_template_name as fee_template_name,pap.is_hostel as is_hostel,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,"
			+ "ps.program_specialization_name as program_specialization_name,pap.program_assignment_id as program_assignment_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,"
			+ "pap.program_start as program_start,ay.ac_year as ac_year,cw.candidate_name as candidate_name,"
			+ "pg.program_name as program_name,pg.program_short_name as program_short_name,"
			+ "sc.school_name as school_name,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,cw.father_name as father_name) "
			+ "from PreAdmissionProcess pap left join FeeAdmissionCategory fac on pap.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join Academic_year ay on pap.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on pap.program_specialization_id=ps.program_specialization_id "
			+ "left join FeeTemplate ft on pap.fee_template_id=ft.fee_template_id "
			+ "left join Program pg on pap.program_id=pg.program_id "
			+ "left join Schools sc on pap.school_id=sc.school_id "
			+ "left join Candidate_Walkin cw on pap.candidate_id=cw.candidate_id "
			+ "left join FeeAdmissionSubCategory fasc on pap.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id "
			+ "Where CONCAT(IfNull(pap.pre_admission_id,''),'',IfNull(pap.student_name,''),'',IfNull(pap.date_of_birth,''),'',"
			+ "IfNull(pap.process_type,''),'',IfNull(pap.created_by,''),'',IfNull(pap.created_date,''),'',IfNull(pap.created_date,''),'',"
			+ "IfNull(pap.gender,''),'',IfNull(pap.created_username,''),'',IfNull(ft.fee_template_name,''),'',"
			+ "IfNull(fasc.fee_admission_sub_category_short_name,''),'',IfNull(ps.program_specialization_short_name,''),'',"
			+ "IfNull(pap.program_start,''),'',IfNull(ay.ac_year,''),'',IfNull(cw.candidate_name,''),'',IfNull(pg.program_name,''),'',"
			+ "IfNull(pg.program_short_name,''),'',IfNull(sc.school_name,''),'',IfNull(fac.fee_admission_category_type,''),'',"
			+ "IfNull(fac.fee_admission_category_short_name,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);

	@Query(value = "select new map(pap.pre_admission_id as id,pap.student_name as student_name,pap.is_scholarship as is_scholarship,"
			+ "pap.date_of_birth as date_of_birth,pap.address as address,pap.process_type as process_type,pap.active as active,"
			+ "pap.created_by as created_by,pap.modified_by as modified_by,pap.created_date as created_date,pap.modified_date as modified_date,"
			+ "pap.gender as gender,pap.mobile as mobile,pap.receipt as receipt,pap.created_username as created_username,"
			+ "pap.modified_username as modified_username,ft.fee_template_name as fee_template_name,pap.is_hostel as is_hostel,"
			+ "fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,"
			+ "ps.program_specialization_name as program_specialization_name,pap.fee_admission_sub_category_id as fee_admission_sub_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,pap.program_assignment_id as program_assignment_id,"
			+ "pap.program_start as program_start,ay.ac_year as ac_year,cw.candidate_name as candidate_name,"
			+ "pg.program_name as program_name,pg.program_short_name as program_short_name,"
			+ "sc.school_name as school_name,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,cw.father_name as father_name) "
			+ "from PreAdmissionProcess pap left join FeeAdmissionCategory fac on pap.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join Academic_year ay on pap.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on pap.program_specialization_id=ps.program_specialization_id "
			+ "left join FeeTemplate ft on pap.fee_template_id=ft.fee_template_id "
			+ "left join Program pg on pap.program_id=pg.program_id "
			+ "left join Schools sc on pap.school_id=sc.school_id "
			+ "left join Candidate_Walkin cw on pap.candidate_id=cw.candidate_id "
			+ "left join FeeAdmissionSubCategory fasc on pap.fee_admission_sub_category_id=fasc.fee_admission_sub_category_id")
	public Page<Object> findAll2(Pageable pageable);

	@Modifying
	@Query(value = "update PreAdmissionProcess d set d.active=false where d.candidate_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update PreAdmissionProcess d set d.active=true where d.pre_admission_id=?1")
	public void update1(Integer id);

	@Query(value = " select new com.au.dto.FeeTemplateDetailsForCandidate( pp.school_id, pp.program_id,"
			+ " pp.program_specialization_id, pp.ac_year_id,pp.fee_template_id ,ct.currency_type_short_name, ft.fee_year1_amt"
			+ "  ,ft.fee_year2_amt,ft.fee_year3_amt,ft.fee_year4_amt,ft.fee_year5_amt,ft.fee_year6_amt,ft.fee_year7_amt,ft.fee_year8_amt  ) from PreAdmissionProcess pp "
			+ " left join FeeTemplate ft on ft.fee_template_id=pp.fee_template_id"
			+ " left join Currency_Type ct on ct.currency_type_id=ft.currency_type_id"
			+ " where pp.candidate_id=:candidateId and pp.active=true")
	public FeeTemplateDetailsForCandidate getFeeTemplateDetails(Integer candidateId);

	@Query(value = " select new com.au.dto.FeeSubAmountDetailsForCandidate( vhn.voucher_head, vhn.voucher_head_new_id, fsa.year1_amt, fsa.year2_amt, fsa.year3_amt, "
			+ " fsa.year4_amt, fsa.year5_amt,fsa.year6_amt,fsa.year7_amt,fsa.year8_amt,fsa.year9_amt,fsa.year10_amt,fsa.year11_amt,fsa.year12_amt ) from PreAdmissionProcess pp "
			+ " left join FeeTemplate ft on ft.fee_template_id=pp.fee_template_id "
			+ " left join FeeTemplateSubAmount fsa on fsa.fee_template_id=ft.fee_template_id "
			+ " left join VoucherHeadNew vhn on vhn.voucher_head_new_id=fsa.voucher_head_new_id "
			+ " where pp.candidate_id=:candidateId and pp.active=true    ")
	public List<FeeSubAmountDetailsForCandidate> getFeeSubAmountDetails(Integer candidateId);

	
	@Modifying
	@Query(value = "update PreAdmissionProcess d set d.mail_sent_status=true where d.candidate_id=?1 and d.active=true")
	public void updateMailSentStatus(Integer candidate_id);

	@Query(value=" select c from PreAdmissionProcess c where c.candidate_id=:candidateId and c.active=true")
	public PreAdmissionProcess findByCandidateId(Integer candidateId);

    @Query(value=" select c from PreAdmissionProcess c left join FeeAdmissionCategory f on f.fee_admission_category_id=c.fee_admission_category_id where c.candidate_id=:candidateId and f.fee_admission_category_short_name='INT' ")
	public PreAdmissionProcess getInternationalAdmission(Integer candidateId);


    
    @Query(value=" select count(*) from PreAdmissionProcess c where c.candidate_id=?1 and c.active=true")
	public Integer getCheckCandidateDetails(Integer candidate_id);

	@Query(value = "select ct.currency_type_short_name from preadmission_process pp " +
			"left join fee_template_history  fth " +
			"on fth.fee_template_id = pp.fee_template_id " +
			"left join currency_type ct on ct.currency_type_id=fth.currency_type_id " +
			"where pp.candidate_id = ?1 " +
			"and ct.active  = 1 " +
			"and pp.active = 1 " +
			"and fth.active order by pp.created_date desc limit 1 ", nativeQuery = true)
	public String getCurrencyTypeByCandidateId(@Param("candidateId") Integer candidateId);

}
