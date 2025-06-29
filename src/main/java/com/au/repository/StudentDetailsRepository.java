package com.au.repository;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.au.dto.*;
import com.au.model.RegistrationFeeTransaction;
import com.au.model.Student_Details;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;

import java.time.LocalDate;
import java.util.*;

@Repository
@Transactional
public interface StudentDetailsRepository extends JpaRepository<Student_Details, Integer> {

	@Query(value = "SELECT max(substr(auid,10,12)) FROM student_details where ac_year_id=?1 and school_id=?2 "
			+ "and  program_id=?3 and program_specialization_id=?4", nativeQuery = true)
	public Integer getMaxStudentCount(Integer ac_year_id, Integer school_id, Integer program_id,
			Integer program_specialization_id);

	@Query(value = "SELECT count(*) FROM student_details", nativeQuery = true)
	public Integer getMaxStudentCount();

	@Query(value = "SELECT acerp_email FROM student_details where acerp_email=?1", nativeQuery = true)
	public String studentEmailExistorNot(String email);

	@Query(value = "SELECT * FROM student_details where student_id=?1 AND active = true ", nativeQuery = true)
	public Student_Details getStudentByStudentId(Integer student_id);

	@Query(value = "select new map(st.student_id as student_id,st.board_university_id as board_university_id,st.program_assignment_id as program_assignment_id,st.photo as photo,ft.fee_template_name as fee_template_name,"
			+ "st.fee_template_id as fee_template_id,st.date_of_admission as date_of_admission,"
			+ "st.nationality as nationality,st.caste as caste,st.religion as religion,"
			+ "st.acharya_email as acharya_email,fac.fee_admission_category_type as fee_admission_category_type ,"
			+ "st.fee_admission_category_id as fee_admission_category_id,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ps.program_specialization_name as program_specialization_name,st.program_specialization_id as program_specialization_id,"
			+ "pr.program_short_name as program_short_name,pr.program_name as program_name,st.program_id as program_id,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,st.school_id as school_id,"
			+ "st.student_name as student_name,st.auid as auid,st.ac_year_id as ac_year_id,ay.ac_year as ac_year )"
			+ "from Student_Details st left join Academic_year ay on st.ac_year_id=ay.ac_year_id "
			+ "left join Schools sc on st.school_id=sc.school_id "
			+ "left join Program pr on st.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on st.program_specialization_id = ps.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on st.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join FeeTemplate ft on ft.fee_template_id=st.fee_template_id where st.student_id=?1 and st.active=true")
	public List<HashMap<String, Object>> auidNext(Integer student_id);

	@Query(value = "SELECT count(*) FROM student_details where ac_year_id=?1 and school_id=?2 and  program_id=?3 "
			+ "and program_specialization_id=?4", nativeQuery = true)
	public Integer getAuidFilterCount(Integer ac_year_id, Integer school_id, Integer program_id,
			Integer program_specialization_id);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id " + "where "
			+ "s.ac_year_id=?1 and s.school_id=?2 and s.program_id=?3 and s.program_specialization_id=?4 "
			+ "and rs.section_id=?5 and rs.current_sem=?6 and rs.current_year=?7", nativeQuery = true)
	public List<Map<String, Object>> fetchSectionAssignDetails(Integer ac_year_id, Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer section_id, Integer current_sem,
			Integer current_year);

	@Query(value = "select new map(sd.auid as auid,sd.student_id as student_id,CONCAT(sd.student_name,'(',sd.auid,'-',IfNull(sd.usn,''),')') as student_name,"
			+ "sd.student_name As studentName,rs.reporting_date As reporting_date,rs.eligible_reported_status As eligible_reported_status,"
			+ "rs.reporting_id As reporting_id,rs.current_year As current_year,rs.current_sem As current_sem) from Student_Details sd "
			+ "left join ReportingStudents rs on rs.student_id=sd.student_id "
			+ "where sd.ac_year_id=?1 and school_id=?2 and program_id=?3 "
			+ "and program_specialization_id=?4 and sd.active=true and sd.proctor_assign_status=null")
	public List<HashMap<String, Object>> getstudentList(Integer ac_year_id, Integer school_id, Integer program_id,
			Integer program_specialization_id);

	@Modifying
	@Query(value = "update Student_Details sd set sd.proctor_assign_status=1 where sd.student_id=?1")
	public void update(Integer student_id);

	@Modifying
	@Query(value = "update Student_Details sd set sd.proctor_assign_status=null where sd.student_id=?1")
	public void updateStudentDetail(Integer student_id);

	@Query(value = "SELECT new map(s.student_id as id ,s.auid as auid, s.usn as usn, s.mobile as mobile, s.acharya_email as acharya_email,"
			+ "s.date_of_admission as date_of_admission,s.firstname as firstname, s.student_name as student_name, s.program_id as program_id, s.program_specialization_id as program_specialization_id,"
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, s.created_username as created_username,"
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "pr.program_name as program_name,pr.program_short_name as program_short_name,ft.fee_template_name as fee_template_name,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,s.old_auid_format as old_auid_format,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name, ua.username as username, ay.ac_year as ac_year)"
			+ "from Student_Details s left join Schools sc on s.school_id=sc.school_id "
			+ "left join Program pr on s.program_id=pr.program_id "
			+ "left join FeeTemplate ft on s.fee_template_id=ft.fee_template_id "
			+ "left join FeeAdmissionCategory fac on s.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join Academic_year ay on s.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on s.program_specialization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on s.created_by=ua.id where s.ac_year_id=?1")
	public List<HashMap<String, Object>> getStudentIndex(Integer ac_year_id);

	@Query(value = "SELECT new map(s.student_id as id ,s.auid as auid, s.usn as usn, s.mobile as mobile, s.acharya_email as acharya_email,"
			+ "s.date_of_admission as date_of_admission,s.firstname as firstname, s.student_name as student_name, s.program_id as program_id, s.program_specialization_id as program_specialization_id,"
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, s.created_username as created_username,"
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name,sc.school_name_short as school_name_short,pr.program_name as program_name,"
			+ "ps.program_specialization_name as program_specialization_name, ua.username as username, ay.ac_year as ac_year)"
			+ "from Student_Details s left join Schools sc on s.school_id=sc.school_id "
			+ "left join Program pr on s.program_id=pr.program_id "
			+ "left join Academic_year ay on s.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on s.program_specialization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on s.created_by=ua.created_by where s.ac_year_id=?1 and s.school_id=?2")
	public List<HashMap<String, Object>> getStudentIndex(Integer ac_year_id, Integer school_id);

	@Query(value = "SELECT new map(s.student_id as id ,s.auid as auid, s.usn as usn, s.mobile as mobile, s.acharya_email as acharya_email,"
			+ "s.date_of_admission as date_of_admission,s.firstname as firstname, s.student_name as student_name, s.program_id as program_id, s.program_specialization_id as program_specialization_id,"
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, s.created_username as created_username,"
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name,sc.school_name_short as school_name_short,pr.program_name as program_name, "
			+ "ps.program_specialization_name as program_specialization_name, ua.username as username, ay.ac_year as ac_year)"
			+ "from Student_Details s left join Schools sc on s.school_id=sc.school_id "
			+ "left join Program pr on s.program_id=pr.program_id "
			+ "left join Academic_year ay on s.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on s.program_specialization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on s.created_by=ua.created_by where s.ac_year_id=?1 and s.school_id=?2 and s.program_id=?3 ")
	public List<HashMap<String, Object>> getStudentIndex(Integer ac_year_id, Integer school_id, Integer program_id);

	@Query(value = "SELECT new map(s.student_id as id ,s.auid as auid, s.usn as usn, s.mobile as mobile, s.acharya_email as acharya_email,"
			+ "s.date_of_admission as date_of_admission,s.firstname as firstname, s.student_name as student_name, s.program_id as program_id, s.program_specialization_id as program_specialization_id,"
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, s.created_username as created_username,"
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name,sc.school_name_short as school_name_short,pr.program_name as program_name, "
			+ "ps.program_specialization_name as program_specialization_name, ua.username as username, ay.ac_year as ac_year)"
			+ "from Student_Details s left join Schools sc on s.school_id=sc.school_id "
			+ "left join Program pr on s.program_id=pr.program_id "
			+ "left join Academic_year ay on s.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on s.program_specialization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on s.created_by=ua.created_by where s.ac_year_id=?1 and s.school_id=?2 and s.program_id=?3 and s.program_specialization_id=?4")
	public List<HashMap<String, Object>> getStudentIndex(Integer ac_year_id, Integer school_id, Integer program_id,
			Integer program_specialization_id);

	@Query(value = "SELECT * FROM student_details where active=true", nativeQuery = true)
	public List<Student_Details> activeStudentDetailsList();

	@Query(value = "Select new map(std.ac_year_id as ac_year_id, std.auid as auid, std.candidate_sex as candidate_sex,"
			+ "std.current_country as current_country, std.current_state as current_state, std.father_name as father_name,"
			+ "std.date_of_admission as date_of_admission, std.dateofbirth as dateofbirth, std.fee_admission_category_id as fee_admission_category_id,"
			+ "std.fee_template_id as fee_template_id, std.firstname as firstname, std.joining_year as joining_year, sc.school_name_short as school_name_short,"
			+ "std.lastname as lastname, std.mobile as mobile, std.nationality as nationality, std.program_id as program_id,"
			+ "std.program_specialization_id as program_specialization_id, std.school_id as school_id, std.acharya_email as acharya_email,"
			+ "std.student_name as student_name, std.usn as usn, std.fee_admission_category_id as fee_admission_category_id, pr.program_name as program_name,"
			+ "pr.program_short_name as program_short_name, ps.program_specialization_name as program_specialization_name, ay.ac_year as ac_year,"
			+ "ps.program_specialization_short_name as program_specialization_short_name, fac.fee_admission_category_type as fee_admission_category_type,"
			+ "ft.fee_template_name as fee_template_name, ft.program_type_id as program_type_id,ft.Is_paid_at_board as Is_paid_at_board,"
			+ "pt.program_type_name as fee_template_program_type_name,pt1.program_type_name as program_assignment_program_type_name,"
			+ "ft.lat_year_sem as lat_year_sem, fac.is_regular as is_regular,"
			+ "std.old_student_id as old_student_id,std.old_std_id_readmn as old_std_id_readmn,std.old_auid_format as old_auid_format,re.semOrYear as semOrYear,"
			+ "pra.number_of_semester as number_of_semester, pra.number_of_years as number_of_years, st.name as state_name, con.name as country_name)"
			+ "From Student_Details std "
			+ "Inner Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ "left join Readmission re on re.newStudentId=std.student_id "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "left join ProgramAssigment pra on std.program_assignment_id=pra.program_assignment_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on std.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join State st on std.current_state = st.id "
			+ "left join Country con on std.current_country = con.id "
			+ "left join ProgramType pt on ft.program_type_id = pt.program_type_id "
			+ "left join ProgramType pt1 on pra.program_type_id = pt1.program_type_id where std.student_id=?1 ")
	public List<HashMap<String, Object>> studentDetailsForFeeReceipt(Integer student_id);

	@Query(value = "Select new map(std.ac_year_id as ac_year_id, std.auid as auid, std.candidate_sex as candidate_sex,"
			+ "std.current_country as current_country, std.current_state as current_state,"
			+ "std.date_of_admission as date_of_admission, std.dateofbirth as dateofbirth, std.fee_admission_category_id as fee_admission_category_id,"
			+ "std.fee_template_id as fee_template_id, std.firstname as firstname, std.joining_year as joining_year, sc.school_name_short as school_name_short,"
			+ "std.lastname as lastname, std.mobile as mobile, std.nationality as nationality, std.program_id as program_id,"
			+ "std.program_specialization_id as program_specialization_id, std.school_id as school_id, std.acharya_email as acharya_email,"
			+ "std.student_name as student_name, std.usn as usn, std.fee_admission_category_id as fee_admission_category_id, pr.program_name as program_name,"
			+ "pr.program_short_name as program_short_name, ps.program_specialization_name as program_specialization_name, ay.ac_year as ac_year,"
			+ "ps.program_specialization_short_name as program_specialization_short_name, fac.fee_admission_category_type as fee_admission_category_type,"
			+ "ft.fee_template_name as fee_template_name, ft.program_type_id as program_type_id, pt.program_type_name as program_type_name,"
			+ "pra.number_of_semester as number_of_semester, pra.number_of_years as number_of_years)"
			+ "From Student_Details std Inner Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "left join ProgramAssigment pra on std.program_id=pra.program_id and std.school_id=pra.school_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on std.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join ProgramType pt on ft.program_type_id = pt.program_type_id where std.student_id=?1 And std.ac_year_id=?2 And std.active=true")
	public List<HashMap<String, Object>> studentDetailsForFeeReceipt1(Integer student_id, Integer ac_year_id);

	@Query(value = "Select new map(std.auid as auid,sc.school_name as school_name,std.usn as usn,"
			+ "std.student_name as student_name,std.blood_group as blood_group,std.school_id as school_id,"
			+ "concat(pr.program_short_name,'-',ps.program_specialization_short_name) as program_specialization_name, ay.ac_year as ac_year,std.student_id as student_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,sec.section_name as section_name,"
			+ "concat(rst.current_year,'/',rst.current_sem) as current_year,std.student_image_path as student_image_path,"
			+ "std.acharya_email as acharya_email,std.student_email as student_email,std.mobile as mobile,std.blood_group as blood_group,"
			+ "std.father_email as father_email,std.father_mobile as father_mobile,std.mother_email as mother_email,"
			+ "std.mother_mobile as mother_mobile,std.permanent_address as permanent_address,con.name as permanant_country,"
			+ "std.permanant_pincode as permanant_pincode,c.name as permanant_city,st.name as permanant_state,"
			+ "std.current_address as current_address,c1.name as current_city,st1.name as current_state,"
			+ "con1.name as current_country,std.current_pincode as current_pincode,ed.employee_name as mentor_name,ed.email as mentor_email,"
			+ "std.guardian_phone as guardian_phone,std.father_name as father_name,std.mother_name as mother_name,std.dateofbirth as dateofbirth, std.ac_year_id as acYearId, rst.current_sem as sem, rst.current_year as year ) From Student_Details std "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "Inner join ReportingStudents rst On rst.student_id =std.student_id "
			+ "Left join Section sec On sec.section_id=rst.section_id " 
			+ "Left join City c On c.id=std.permanant_city "
			+ "Left join State st On st.id=std.permanant_state "
			+ "Left join Country con On con.id=std.permanant_country " 
			+ "Left join City c1 On c1.id=std.current_city "
			+ "Left join State st1 On st1.id=std.current_state " 
			+ "Left join Country con1 On con1.id=std.current_country "
			+ "Left join ProctorStudentAssignment psa On psa.student_id=std.student_id And psa.active=true "
			+ "Left join EmployeeDetails ed On ed.emp_id=psa.emp_id where std.acharya_email=?1 And std.active=true")
	public HashMap<String, Object> studentDetailsForMobileApp(String acharya_email);

	@Query(value = "Select new map(std.auid as id, std.candidate_sex as candidate_sex, std.father_name as father_name,std.passport_no as passport_no,"
			+ "std.dateofbirth as dateofbirth, sc.school_name_short as school_name_short, std.acharya_email as acharya_email,"
			+ "std.student_name as student_name, std.usn as usn, pr.program_name as program_name, std.local_adress1 as local_adress1,"
			+ "std.permanant_adress1 as permanant_adress1, std.mother_name as mother_name,std.visa_no as visa_no,std.usn as usn,"
			+ "pr.program_short_name as program_short_name, ps.program_specialization_name as program_specialization_name, ay.ac_year as ac_year,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,"
			+ "st.name as state_name, con.name as country_name) "
			+ "From Student_Details std Inner Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "left join State st on std.current_state = st.id "
			+ "left join Country con on std.current_country = con.id "
			+ "where CONCAT(IfNull(std.auid,''),'',IfNull(std.candidate_sex,''),'',IfNull(sc.school_name_short,''),"
			+ "'',IfNull(std.father_name,''),'',IfNull(std.acharya_email,''),'',IfNull(std.dateofbirth,''),"
			+ "'',IfNull(std.student_name,''),'',IfNull(pr.program_short_name,''),'',IfNull(ps.program_specialization_short_name,''),"
			+ "'',IfNull(pr.program_name,''),'',IfNull(ps.program_specialization_name,''),'',IfNull(ay.ac_year,''),"
			+ "'',IfNull(st.name,''),'',IfNull(con.name,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "Select new map(std.auid as id, std.candidate_sex as candidate_sex, std.father_name as father_name,std.passport_no as passport_no,"
			+ "std.dateofbirth as dateofbirth, sc.school_name_short as school_name_short, std.acharya_email as acharya_email,"
			+ "std.student_name as student_name, std.usn as usn, pr.program_name as program_name, std.local_adress1 as local_adress1,"
			+ "std.permanant_adress1 as permanant_adress1, std.mother_name as mother_name,std.visa_no as visa_no,"
			+ "pr.program_short_name as program_short_name, ps.program_specialization_name as program_specialization_name, ay.ac_year as ac_year,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,"
			+ "st.name as state_name, con.name as country_name) "
			+ "From Student_Details std Inner Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "left join State st on std.current_state = st.id "
			+ "left join Country con on std.current_country = con.id")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "SELECT ifNull(MAX(CAST(SUBSTRING(std.student_master_code, 4, length(std.student_master_code)-3) AS UNSIGNED)), 0) FROM student_details std", nativeQuery = true)
	public Integer maxStudentMasterCode();

	@Query(value = "SELECT count(*) FROM student_details std Where std.ac_year_id=?1 and std.program_specialization_id=?2 And std.active=true", nativeQuery = true)
	public Integer countOfStudentForOldAuidFormat(Integer ac_year_id, Integer program_specialization_id);

	@Query(value = "select new map(std.student_name as student_name,std.auid as auid,std.usn as usn,std.date_of_admission as date_of_admission,"
			+ "fac.fee_admission_category_type as fee_admission_category_type,ft.fee_template_name as fee_template_name,rst.current_year as current_year,rst.current_sem as current_sem) "
			+ "from Student_Details std  " + "left join FeeTemplate ft on ft.fee_template_id=std.fee_template_id "
			+ "left join ReportingStudents rst On rst.student_id =std.student_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id "
			+ "where std.fee_template_id=?1")
	public List<HashMap<String, Object>> findStudentDetailsByFeeTemplateId(Integer fee_template_id);

	@Query(value = "select COUNT(sd.student_id) as student_count,ft.fee_template_name as fee_template_name,ft.fee_template_id as fee_template_id from Student_Details sd "
			+ "left join FeeTemplate ft on sd.fee_template_id=ft.fee_template_id group by sd.fee_template_id order by ft.created_date desc")
	public List<Map<String, Object>> fetchCountOfStudentDetailsByFeeTemplateId();

	@Query(value = "Select new map(std.student_id as student_id,std.ac_year_id as ac_year_id, std.auid as auid, std.candidate_sex as candidate_sex,"
			+ "std.current_country as current_country, std.current_state as current_state, std.father_name as father_name,cw.candidate_id as candidate_id,"
			+ "std.date_of_admission as date_of_admission, std.dateofbirth as dateofbirth, std.fee_admission_category_id as fee_admission_category_id,"
			+ "std.fee_template_id as fee_template_id, std.firstname as firstname, std.joining_year as joining_year, sc.school_name_short as school_name_short,"
			+ "std.lastname as lastname, std.mobile as mobile, std.nationality as nationality, std.program_id as program_id,std.student_email as student_email,"
			+ "std.program_specialization_id as program_specialization_id, std.school_id as school_id, std.acharya_email as acharya_email,"
			+ "std.student_name as student_name, std.usn as usn, std.fee_admission_category_id as fee_admission_category_id, pr.program_name as program_name,"
			+ "pr.program_short_name as program_short_name, ps.program_specialization_name as program_specialization_name,"
			+ "ct.currency_type_id As currency_type_id,ct.currency_type_name As currency_type_name,sc.school_name As school_name,"
			+ "ay.ac_year as ac_year,ay.ac_year_code as ac_year_code,pt.program_type_code as program_type_code,pt.program_type_name as program_type_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name, fac.fee_admission_category_type as fee_admission_category_type,"
			+ "ft.fee_template_name as fee_template_name, ft.program_type_id as program_type_id, pt.program_type_name as program_type_name,"
			+ "pra.number_of_semester as number_of_semester, pra.number_of_years as number_of_years, st.name as state_name, con.name as country_name) "
			+ "From Student_Details std "
			+ "Left Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ "Left Join Currency_Type ct On ct.currency_type_id = ft.currency_type_id "
			+ "left join Candidate_Walkin cw on cw.candidate_id=std.candidate_id "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "left join ProgramAssigment pra on std.program_assignment_id=pra.program_assignment_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on std.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join State st on std.current_state = st.id "
			+ "left join Country con on std.current_country = con.id "
			+ "left join ProgramType pt on pra.program_type_id = pt.program_type_id where std.auid=?1 And std.active=true")
	public List<HashMap<String, Object>> getStudentDetail(String auid);

	@Query(value = "Select new map(std.student_id as student_id,std.ac_year_id as ac_year_id, std.auid as auid, std.candidate_sex as candidate_sex,"
			+ "std.current_country as current_country, std.current_state as current_state, std.father_name as father_name,cw.candidate_id as candidate_id,"
			+ "std.date_of_admission as date_of_admission, std.dateofbirth as dateofbirth, std.fee_admission_category_id as fee_admission_category_id,"
			+ "std.fee_template_id as fee_template_id, std.firstname as firstname, std.joining_year as joining_year, sc.school_name_short as school_name_short,"
			+ "std.lastname as lastname, std.mobile as mobile, std.nationality as nationality, std.program_id as program_id,std.id_barcode_generated as id_barcode_generated,"
			+ "std.program_specialization_id as program_specialization_id, std.school_id as school_id, std.acharya_email as acharya_email,"
			+ "sc.school_name as school_name,rs.reporting_id as reporting_id,rs.current_year as current_year,"
			+ "rs.current_sem as current_sem,std.old_student_id as old_student_id,"
			+ "std.student_name as student_name, std.usn as usn, std.fee_admission_category_id as fee_admission_category_id, pr.program_name as program_name,"
			+ "pr.program_short_name as program_short_name, ps.program_specialization_name as program_specialization_name, ay.ac_year as ac_year,"
			+ "ps.program_specialization_short_name as program_specialization_short_name, fac.fee_admission_category_type as fee_admission_category_type,"
			+ "ft.fee_template_name as fee_template_name, ft.program_type_id as program_type_id, pt.program_type_name as program_type_name,"
			+ "pra.number_of_semester as number_of_semester, pra.number_of_years as number_of_years, st.name as state_name, con.name as country_name)"
			+ "From Student_Details std Left Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ "left join Candidate_Walkin cw on cw.candidate_id=std.candidate_id "
			+ "left join ReportingStudents rs on std.student_id=rs.student_id "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "Inner join ProgramAssigment pra on std.program_assignment_id=pra.program_assignment_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on std.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join State st on std.current_state = st.id "
			+ "left join Country con on std.current_country = con.id "
			+ "left join ProgramType pt on pra.program_type_id = pt.program_type_id where std.auid=?1 ")
	public List<HashMap<String, Object>> inActiveStudentDetailsByAuid(String auid);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,rs.reporting_date as reporting_date,"
			+ "rs.current_year as current_year,rs.current_sem as current_sem FROM student_details s "
			+ "LEFT JOIN reporting_students rs ON rs.student_id = s.student_id "
			+ "where (s.ac_year_id = ?1 OR ?1 IS NULL) and s.school_id=?2 and s.program_id=?3 and s.program_specialization_id=?4 and rs.current_sem=?5 and s.active=true "
			+ "AND rs.student_id NOT IN (SELECT ss.student_ids FROM section_assignment ss WHERE ss.school_id=?2 and ss.program_id=?3 and ss.program_specialization_id=?4 "
			+ "and ss.current_year_sem=?5 and ss.active=true And FIND_IN_SET(rs.student_id, ss.student_ids) > 0) "
			+ "GROUP BY rs.student_id ", nativeQuery = true)
	public List<Map<String, Object>> fetchStudentDetailForSectionAssignmentOnSem(Integer ac_year_id, Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer current_sem);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,"
			+ "rs.current_year as current_year,rs.current_sem as current_sem,rs.reporting_date as reporting_date FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id " 
			+ "where (s.ac_year_id = ?1 OR ?1 IS NULL) and s.school_id=?2 and s.program_id=?3 and s.program_specialization_id=?4 and rs.current_year=?5 and rs.section_id is null and s.active=true "
			+ "AND rs.student_id NOT IN (SELECT ss.student_ids FROM section_assignment ss WHERE ss.school_id=?2 and ss.program_id=?3 and ss.program_specialization_id=?4 "
			+ "and ss.current_year_sem=?5 and ss.active=true And FIND_IN_SET(rs.student_id, ss.student_ids) > 0) "
			+ "GROUP BY rs.student_id ", nativeQuery = true)
	public List<Map<String, Object>> fetchStudentDetailForSectionAssignmentOnYear(Integer ac_year_id, Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer current_year);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,pa.program_assignment_id As program_assignment_id,"
			+ "rs.section_id,rs.current_year as current_year,rs.current_sem as current_sem,rs.reporting_date as reporting_date FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id "
			+ "left join program_assignment pa on pa.program_assignment_id=s.program_assignment_id "
			+ "where s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_sem=?4 and rs.section_id is null and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllSectionUnAssignedStudentDetailOnSem(Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer current_year_sem);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,pa.program_assignment_id As program_assignment_id,"
			+ "rs.section_id,rs.current_year as current_year,rs.current_sem as current_sem,rs.reporting_date as reporting_date FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id "
			+ "left join program_assignment pa on pa.program_assignment_id=s.program_assignment_id "
			+ "where s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_year=?4 and rs.section_id is null and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllSectionUnAssignedStudentDetailOnYear(Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer current_year_sem);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,rs.section_id,"
			+ "sc.section_name,rs.reporting_date,rs.current_year ,rs.current_sem  FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id "
			+ "left join section sc on rs.section_id=sc.section_id "
			+ "where s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_sem=?4 and rs.section_id = ?5 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllSectionAssignedStudentDetailOnSemForUpdate(Integer school_id, Integer program_id, Integer program_specialization_id, Integer current_year_sem,
			Integer section_id);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,rs.section_id,"
			+ "sc.section_name,rs.reporting_date FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id "
			+ "left join section sc on rs.section_id=sc.section_id "
			+ "where s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_year=?4 and rs.section_id = ?5 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllSectionAssignedStudentDetailOnYearForUpdate(Integer school_id, Integer program_id, Integer program_specialization_id, Integer current_year_sem,
			Integer section_id);

	@Modifying
	@Query(value = "update Student_Details sd set sd.student_image_path=?2 where sd.student_id=?1")
	public void updatePath(Integer leave_id, String t1);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,rs.current_year,rs.current_sem,rs.reporting_date FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.school_id=?1 and s.program_specialization_id in ?2 and rs.current_sem=?3 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchStudentDetailForBatchAssignmentOnSem(Integer school_id,
			List<Integer> program_specialization_id, Integer current_sem);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,rs.current_year,rs.current_sem,rs.reporting_date FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where  s.school_id=?1 and s.program_specialization_id in ?2 and rs.current_year=?3  and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchStudentDetailForBatchAssignmentOnYear(Integer school_id,
			List<Integer> program_specialization_id, Integer current_year);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,rs.created_date as created_date,"
			+ "rs.reporting_date as reporting_date,rs.current_year as current_year,rs.current_sem as current_sem "
			+ "FROM student_details s " + "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.student_id in ?1 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAssignedStudentDetails(List<Integer> student_ids);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.ac_year_id =?1 and s.school_id =?2 and s.student_id not in ?3 and s.program_specialization_id in ?4 and rs.current_sem=?5 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchoolOnSem(Integer ac_year_id, Integer school_id,
			List<Integer> student_ids, List<Integer> program_specialization_id, Integer current_year_sem);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.ac_year_id =?1 and s.school_id =?2 and s.student_id not in ?3 and s.program_specialization_id in ?4 and rs.current_year=?5 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchoolOnYear(Integer ac_year_id, Integer school_id,
			List<Integer> student_ids, List<Integer> program_specialization_id, Integer current_year_sem);
	
	
	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.ac_year_id =?1 and s.student_id not in ?2 and s.program_specialization_id in ?3 and rs.current_sem=?4 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchoolOnSem1(Integer ac_year_id,
			List<Integer> student_ids, List<Integer> program_specialization_id, Integer current_year_sem);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.ac_year_id =?1 and s.student_id not in ?2 and s.program_specialization_id in ?3 and rs.current_year=?4 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchoolOnYear1(Integer ac_year_id,
			List<Integer> student_ids, List<Integer> program_specialization_id, Integer current_year_sem);
	

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.ac_year_id =?1 and s.school_id =?2 and s.program_specialization_id in ?3 and rs.current_sem=?4 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchoolWOStudentOnSem(Integer ac_year_id,
			Integer school_id, List<Integer> program_specialization_id, Integer current_year_sem);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.ac_year_id =?1 and s.school_id =?2 and s.program_specialization_id in ?3 and rs.current_year=?4 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchoolWOStudentOnYear(Integer ac_year_id,
			Integer school_id, List<Integer> program_specialization_id, Integer current_year_sem);

	
	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.ac_year_id =?1 and s.program_specialization_id in ?2 and rs.current_sem=?3 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchoolWOStudentOnSem1(Integer ac_year_id,
			 List<Integer> program_specialization_id, Integer current_year_sem);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.ac_year_id =?1  and s.program_specialization_id in ?2 and rs.current_year=?3 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailsOfSchoolWOStudentOnYear1(Integer ac_year_id,
			 List<Integer> program_specialization_id, Integer current_year_sem);
	
	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where  s.program_specialization_id in ?1 and rs.current_sem=?2 and s.ac_year_id =?3 and s.student_id not in ?4 and s.school_id in ?5 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailForBatchAssignmentFromIndexOnSem(
			List<Integer> program_specialization_id, Integer current_sem, Integer ac_year_id, List<Integer> student_ids,
			List<Integer> unassigned_school_id);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.program_specialization_id in ?1 and rs.current_year=?2 and s.ac_year_id =?3 and s.student_id not in ?4 and s.school_id in ?5 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchUnAssignedStudentDetailForBatchAssignmentFromIndexOnYear(
			List<Integer> program_specialization_id, Integer current_year, Integer ac_year_id,
			List<Integer> student_ids, List<Integer> unassigned_school_id);

	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status,s.date_of_admission as date_of_admission "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.school_id=?1 and s.program_id=?2 and s.ac_year_id=?3 and s.program_specialization_id=?4 and rs.current_sem=?5 and rs.eligible_reported_status=1 "
			+ "and rs.reporting_date IS NULL and s.active=true and (s.course_approver_status != 0 Or s.course_approver_status is Null) ", nativeQuery = true)
	public List<Map<String, Object>> fetchAllStudentDetailsToReportOnSem(Integer school_id, Integer program_id,
			Integer ac_year_id,Integer program_specialization_id, Integer current_sem);

	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status,s.date_of_admission as date_of_admission "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where  s.school_id=?1 and s.program_id=?2 and s.ac_year_id=?3 and s.program_specialization_id=?4 and rs.current_year=?5 and rs.eligible_reported_status=1 "
			+ "and rs.reporting_date IS NULL and s.active=true and (s.course_approver_status != 0 Or s.course_approver_status is Null) ", nativeQuery = true)
	public List<Map<String, Object>> fetchAllStudentDetailsToReportOnYear(Integer school_id, Integer program_id,
			Integer ac_year_id,Integer program_specialization_id, Integer current_year);

	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.school_id=?1 and s.program_id=?2 and rs.current_sem=?3 and rs.eligible_reported_status IN(1,2) and  "
			+ "rs.reporting_date IS NOT NULL and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllStudentsWithNoStatusAndNotEligibleOnSem(Integer school_id,
			Integer program_id, Integer current_sem);

	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where  s.school_id=?1 and s.program_id=?2 and rs.current_year=?3 and rs.eligible_reported_status IN(1,2) and "
			+ "rs.reporting_date IS NOT NULL and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllStudentsWithNoStatusAndNotEligibleOnYear(Integer school_id,
			Integer program_id, Integer current_year);

	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_sem=?4 and rs.eligible_reported_status=?5 and "
			+ "s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllStudentDetailssWithEligibleStatusThreeOnSem(Integer school_id,
			Integer program_id,Integer program_specialization_id, Integer current_sem,Integer eligible_reported_status);

	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where  s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_year=?4 and rs.eligible_reported_status=?5 and "
			+ " s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllStudentDetailssWithEligibleStatusThreeOnYear(Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer current_year, Integer eligible_reported_status);

	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status,rs.modified_date as modified_date,rs.modified_username as modified_username "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_sem=?4 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllStudentDetailssWithEligibleStatusOnSem(Integer school_id,
			Integer program_id,Integer program_specialization_id, Integer current_sem);

	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status,rs.modified_date as modified_date,rs.modified_username as modified_username "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where  s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_year=?4 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllStudentDetailssWithEligibleStatusOnYear(Integer school_id,
			Integer program_id,Integer program_specialization_id, Integer current_year);

	@Query(value = "select new map(st.student_id as student_id, st.usn as usn, st.program_assignment_id as program_assignment_id, st.photo as photo, ft.fee_template_name as fee_template_name,"
	        + " st.blood_group As blood_group, st.student_email As student_email, st.mother_name As mother_name, st.student_image_path As student_image_path, st.permanent_address As permanent_address,"
	        + " st.current_address As current_address, st.local_adress1 As local_adress1, st.whatsapp_number As whatsapp_number, st.nationality As nationality, st.father_mobile As father_mobile,"
	        + " st.mother_mobile As mother_mobile, st.bank_name As bank_name, st.account_holder_name As account_holder_name, st.account_number As account_number, st.bank_branch As bank_branch,"
	        + " st.ifsc_code As ifsc_code, st.adhar_number As adhar_number, cw.application_no_npf As application_no_npf, st.local_pincode As local_pincode,"
	        + " st.permanant_adress1 As permanant_adress1, st.permanant_pincode As permanant_pincode, st.current_adress1 As current_adress1, st.current_pincode As current_pincode,"
	        + " c1.id As permanant_city_id1, c1.name As permanant_city_name, c2.id As local_city_id, c2.name As local_city_name, c3.id As current_city_id, c3.name As current_city_name,"
	        + " s1.id As permanant_state_id, s1.name As permanant_state_name, s2.id As local_state_id, s2.name As local_state_name, s3.id As current_state_id, s3.name As current_state_name,"
	        + " co1.id As permanant_country_id, co1.name As permanant_country_name, co2.id As local_country_id, co2.name As local_country_name, co3.id As current_country_id, co3.name As current_country_name,"
	        + " st.fee_template_id as fee_template_id, st.mobile as mobile, cw.visitor_id as visitor_id, cw.candidate_sex as candidate_sex, st.father_name as father_name,"
	        + " ft.fee_template_name as fee_template_name, st.caste as caste, cw.candidate_name as candidate_name, st.dateofbirth as dateofbirth, st.adhar_number As adhar_number,"
	        + " st.acharya_email as acharya_email, fac.fee_admission_category_type as fee_admission_category_type , st.bank_branch As bank_branch, st.ifsc_code As ifsc_code,"
	        + " st.active As active, st.religion As religion, na.nationality As nationalityName, st.father_email As father_email, st.father_occupation As father_occupation,"
	        + " st.father_income As father_income, st.mother_email As mother_email, st.mother_occupation As mother_occupation, st.mother_income As mother_income,"
	        + " st.guardian_name As guardian_name, st.guardian_phone As guardian_phone, st.bank_name As bank_name, st.account_holder_name As account_holder_name,"
	        + " st.fee_admission_category_id as fee_admission_category_id, ps.program_specialization_short_name as program_specialization_short_name,"
	        + " ps.program_specialization_name as program_specialization_name, st.program_specialization_id as program_specialization_id, fac.is_regular As is_regular,"
	        + " pr.program_short_name as program_short_name, pr.program_name as program_name, st.program_id as program_id, st.email_preferred_name As email_preferred_name,"
	        + " sc.school_name as school_name, sc.school_name_short as school_name_short, st.school_id as school_id, st.alternate_number As alternate_number,"
	        + " st.student_name as student_name, st.auid as auid, st.ac_year_id as ac_year_id, ay.ac_year as ac_year, st.firstname as firstname, st.lastname as lastname,"
	        + " st.candidate_sex as candidate_sex, st.parents_mobile as parents_mobile, st.parents_email as parents_email, st.current_address as current_address,"
	        + " st.permanent_address as permanent_address, st.guardian_name as guardian_name, st.guardian_phone as guardian_phone, st.blood_group as blood_group, "
	        + " st.religion as religion, st.caste as caste, st.surname as surname, st.permanant_country as permanant_country, st.permanant_pincode as permanant_pincode,"
	        + " st.permanant_city as permanant_city, st.permanant_adress1 as permanant_adress1, st.local_email as local_email, st.local_phone as local_phone,"
	        + " st.local_mobile as local_mobile, st.local_state as local_state, st.local_country as local_country, st.local_pincode as local_pincode, st.local_city as local_city,"
	        + " st.local_adress1 as local_adress1, st.current_city as current_city, st.current_email as current_email, st.current_phone as current_phone, "
	        + " st.current_mobile as current_mobile, st.current_state as current_state, st.current_country as current_country, st.current_pincode as current_pincode,"
	        + " st.current_adress1 as current_adress1, st.permanant_state as permanant_state, st.permanant_mobile as permanant_mobile, st.permanant_phone as permanant_phone,"
	        + " st.permanant_email as permanant_email, st.account_holder_name as account_holder_name, st.bank_name as bank_name, st.account_number as account_number,"
	        + " st.bank_branch as bank_branch, st.ifsc_code as ifsc_code, st.p_city as p_city, st.c_city as c_city, st.l_city as l_city, st.father_occupation as father_occupation,"
	        + " st.mother_occupation as mother_occupation, st.father_income as father_income, st.mother_income as mother_income, st.father_email as father_email,"
	        + " st.mother_email as mother_email, st.father_mobile as father_mobile, st.mother_mobile as mother_mobile, st.auid as auid, st.usn as usn,"
	        + " st.candidate_id as candidate_id, st.dateofjoining as dateofjoining, st.school_id as school_id, st.created_date as created_date, st.modified_date as modified_date,"
	        + " st.created_by as created_by, st.modified_by as modified_by, st.active as active, st.ac_year_id as ac_year_id, st.fee_admission_category_id as fee_admission_category_id,"
	        + " st.program_id as program_id, st.program_specialization_id as program_specialization_id, st.fee_template_id as fee_template_id, st.allotment_number as allotment_number,"
	        + " st.visitor_id as visitor_id, st.lateral_sem as lateral_sem, st.deassign_status as deassign_status, st.course_approver_status as course_approver_status,"
	        + " st.date_of_admission as date_of_admission, st.joining_year as joining_year, st.joining_sem as joining_sem, st.student_email as student_email, st.scholarship_status as scholarship_status,"
	        + " st.entrance_test_no as entrance_test_no, st.rank1 as rank1, st.order_no_date as order_no_date, st.category_reserved as category_reserved,"
	        + " st.category_alloted as category_alloted, st.rural_urban as rural_urban, st.special_category as special_category, st.karnataka_medium as karnataka_medium,"
	        + " st.board_admission_status as board_admission_status, st.entranct_test_type as entranct_test_type, st.board_admission_date as board_admission_date,"
	        + " st.photo_upload_status as photo_upload_status, st.re_admission_status as re_admission_status, st.proctor_assign_status as proctor_assign_status,"
	        + " st.old_student_id as old_student_id, st.idcard_ac_status as idcard_ac_status, st.passed_status as passed_status, st.old_std_id_readmn as old_std_id_readmn,"
	        + " st.created_username as created_username, st.modified_username as modified_username, st.student_master_code as student_master_code, st.passport_no as passport_no,"
	        + " st.visa_no as visa_no, st.student_image_path as student_image_path, st.program_assignment_id as program_assignment_id, st.board_university_id as board_university_id,"
	        + " st.mother_qualification as mother_qualification, st.father_qualification as father_qualification, st.caste_category as caste_category, st.old_auid_format as old_auid_format,"
	        + " st.email_preferred_name as email_preferred_name, st.id_barcode_generated as id_barcode_generated, st.laptop_issued_status as laptop_issued_status,"
	        + " st.laptop_issued_date as laptop_issued_date, st.id_card_issued_year as id_card_issued_year, st.pdfContent as pdfContent, st.id_card_bucket_status as id_card_bucket_status,"
	        + " st.marital_status as marital_status, st.whatsapp_number as whatsapp_number, st.guardian_email as guardian_email, st.guardian_occupation as guardian_occupation,"
	        + " st.alternate_number as alternate_number, st.guardian_relation_to_student as guardian_relation_to_student)"
	        + " from Student_Details st "
	        + " left join Candidate_Walkin cw on cw.candidate_id=st.candidate_id "
	        + " left join Academic_year ay on st.ac_year_id=ay.ac_year_id "
	        + " left join Schools sc on st.school_id=sc.school_id "
	        + " left join Program pr on st.program_id=pr.program_id "
	        + " left join City c1 on st.permanant_city=c1.id "
	        + " left join City c2 on st.local_city=c2.id "
	        + " left join City c3 on st.current_city=c3.id "
	        + " left join State s1 on st.permanant_state=s1.id "
	        + " left join State s2 on st.local_state=s2.id "
	        + " left join State s3 on st.current_state=s3.id "
	        + " left join Country co1 on st.permanant_country=co1.id "
	        + " left join Country co2 on st.local_country=co2.id "
	        + " left join Country co3 on st.current_country=co3.id "
	        + " left join Nationality na on st.nationality=na.nationality_id "
	        + " left join ProgramSpecilization ps on st.program_specialization_id = ps.program_specialization_id "
	        + " left join FeeAdmissionCategory fac on st.fee_admission_category_id=fac.fee_admission_category_id "
	        + " left join FeeTemplate ft on ft.fee_template_id=st.fee_template_id "
	        + " where st.student_id=?1 and st.active=true")
	public HashMap<String, Object> studentDataForTestimonials(Integer student_id);

	@Query(value = "Select pt.program_type_name From Student_Details st "
			+ "Inner Join ProgramAssigment pa on pa.program_assignment_id =st.program_assignment_id "
			+ "left Join ProgramType pt on pa.program_type_id=pt.program_type_id Where st.student_id =?1")
	public String getProgramTypeOfStudent(Integer student_id);

	@Query(value = "Select sd.student_name as student_name,sd.auid as auid,sd.student_id as student_id,"
			+ "ia.present_status as present_status,itt.max_marks as max_marks,itt.min_marks as min_marks From student_details sd "
			+ "Left Join internal_attendance ia On ia.student_id=sd.student_id "
			+ "Left Join internal_time_table itt On itt.internal_id=ia.internal_id "
			+ "Where sd.student_id in ?1 And sd.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchAllStudentDetailForStudentMarks(List<Integer> student_ids);

	@Query(value = "SELECT std.program_specialization_id FROM student_details std Where std.student_id=?1 and std.active=true", nativeQuery = true)
	public Integer getProgramSpecializationId(Integer student_id);

	@Query(value = "SELECT * FROM student_details s WHERE s.acharya_email = :email", nativeQuery = true)
	public Student_Details findByAcharyaEmail(String email);

	@Query(value = "Select max(sd.auid) From student_details sd Where sd.ac_year_id=?1 And sd.program_id=?2 And sd.program_specialization_id=?3 And sd.auid is not null ", nativeQuery = true)
	public String getLastStudentDetailsByProgramSpecializationId(Integer ac_year_id, Integer program_id,
			Integer program_specialization_id);

	@Query(value = "Select max(Cast(substring(sd.auid,10) as Unsigned)) From student_details sd Where sd.ac_year_id=?1 And sd.program_id=?2 And sd.program_specialization_id=?3 And sd.auid is not null", nativeQuery = true)
	public Integer getLastStudentDetailsAuidCount(Integer ac_year_id, Integer program_id,
			Integer program_specialization_id);

	@Query(value = "select new map( s.acharya_email as email, s.firstname as firstName, s.lastname as lastName, s.student_name as studentName, s.nationality as nationality, s.mobile as mobile, s.dateofbirth  as dateOfBirth, s.father_name as fatherName, s.mother_name as motherName, s.current_address as currentAddress, s.permanent_address as permanentAddress, s.auid as auid, s.date_of_admission as dateOfAdmission, s.dateofjoining as dateOfJoining ) from Student_Details s where s.auid=:auid")
	public List<HashMap<String, Object>> getStudentViewDetailsByAuid(@Param("auid") String auid);

	@Query(value = "Select sd.student_id as student_id,sd.program_id as program_id,"
			+ "sd.program_specialization_id as program_specialization_id,sd.auid as auid,sd.blood_group as blood_group,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name,"
			+ "sd.id_barcode_generated as id_barcode_generated,sd.student_name as student_name,"
			+ "ps.program_specialization_name as program_specialization_name,sd.student_image_path as student_image_path,"
			+ "ps.program_specialization_short_name as program_specialization_short_name From student_details sd "
			+ "left join program p on sd.program_id=p.program_id "
			+ "left join program_specialization ps on sd.program_specialization_id=ps.program_specialization_id "
			+ "where sd.active=true And sd.id_barcode_generated is null", nativeQuery = true)
	public List<Map<String, Object>> getStudentDetailsForIdCard();

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.id_barcode_generated as id_barcode_generated, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid,std.totalGrant as total_grant, "
			+ " CASE WHEN (rs.current_year = 1 OR rs.current_sem = 1) AND ss.is_approved = 'yes' THEN ss.year1_amount"
			+ " WHEN (rs.current_year = 2 OR rs.current_sem = 2) AND ss.is_approved = 'yes' THEN ss.year2_amount "
			+ " WHEN (rs.current_year = 3 OR rs.current_sem = 3) AND ss.is_approved = 'yes' THEN ss.year3_amount "
			+ " WHEN (rs.current_year = 4 OR rs.current_sem = 4) AND ss.is_approved = 'yes' THEN ss.year4_amount "
			+ " WHEN (rs.current_year = 5 OR rs.current_sem = 5) AND ss.is_approved = 'yes' THEN ss.year5_amount "
			+ " WHEN (rs.current_year = 6 OR rs.current_sem = 6) AND ss.is_approved = 'yes' THEN ss.year6_amount "
			+ " WHEN (rs.current_year = 7 OR rs.current_sem = 7) AND ss.is_approved = 'yes' THEN ss.year7_amount "
			+ " WHEN (rs.current_year = 8 OR rs.current_sem = 8) AND ss.is_approved = 'yes' THEN ss.year8_amount "
			+ " WHEN (rs.current_year = 9 OR rs.current_sem = 9) AND ss.is_approved = 'yes' THEN ss.year9_amount "
			+ " WHEN (rs.current_year = 10 OR rs.current_sem = 10) AND ss.is_approved = 'yes' THEN ss.year10_amount "
			+ " WHEN (rs.current_year = 11 OR rs.current_sem = 11) AND ss.is_approved = 'yes' THEN ss.year11_amount "
			+ " WHEN (rs.current_year = 12 OR rs.current_sem = 12) AND ss.is_approved = 'yes' THEN ss.year12_amount "
			+ "  ELSE 0 END as grant ) " + "FROM Student_Details s "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country " + "LEFT JOIN City cc ON cc.id = s.current_city "
			+ "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "WHERE s.ac_year_id = ?1 AND (rs.eligible_reported_status=1 OR rs.eligible_reported_status=3) and s.active=1 ")
	public Page<Object> getStudentDetailsDueForReportedAndEligibleIndex1(Pageable pageable, Integer ac_year_id);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid,std.totalGrant as total_grant, "
			+ " CASE WHEN (rs.current_year = 1 OR rs.current_sem = 1) AND ss.is_approved = 'yes' THEN ss.year1_amount"
			+ " WHEN (rs.current_year = 2 OR rs.current_sem = 2) AND ss.is_approved = 'yes' THEN ss.year2_amount "
			+ " WHEN (rs.current_year = 3 OR rs.current_sem = 3) AND ss.is_approved = 'yes' THEN ss.year3_amount "
			+ " WHEN (rs.current_year = 4 OR rs.current_sem = 4) AND ss.is_approved = 'yes' THEN ss.year4_amount "
			+ " WHEN (rs.current_year = 5 OR rs.current_sem = 5) AND ss.is_approved = 'yes' THEN ss.year5_amount "
			+ " WHEN (rs.current_year = 6 OR rs.current_sem = 6) AND ss.is_approved = 'yes' THEN ss.year6_amount "
			+ " WHEN (rs.current_year = 7 OR rs.current_sem = 7) AND ss.is_approved = 'yes' THEN ss.year7_amount "
			+ " WHEN (rs.current_year = 8 OR rs.current_sem = 8) AND ss.is_approved = 'yes' THEN ss.year8_amount "
			+ " WHEN (rs.current_year = 9 OR rs.current_sem = 9) AND ss.is_approved = 'yes' THEN ss.year9_amount "
			+ " WHEN (rs.current_year = 10 OR rs.current_sem = 10) AND ss.is_approved = 'yes' THEN ss.year10_amount "
			+ " WHEN (rs.current_year = 11 OR rs.current_sem = 11) AND ss.is_approved = 'yes' THEN ss.year11_amount "
			+ " WHEN (rs.current_year = 12 OR rs.current_sem = 12) AND ss.is_approved = 'yes' THEN ss.year12_amount "
			+ "  ELSE 0 END as grant ) " + "FROM Student_Details s "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country " + "LEFT JOIN City cc ON cc.id = s.current_city "
			+ "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "WHERE CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %?1% "
			+ "AND s.ac_year_id = ?2 AND (rs.eligible_reported_status=1 OR rs.eligible_reported_status=3) and s.active=1 ")
	public Page<Object> getStudentDetailsDueForReportedAndEligibleIndex2(Pageable pageable, Object keyword,
			Integer ac_year_id);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.id_barcode_generated as id_barcode_generated, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid,std.totalGrant as total_grant, "
			+ "CASE WHEN (rs.current_year = 1 OR rs.current_sem = 1) AND ss.is_approved = 'yes' THEN ss.year1_amount"
			+ " WHEN (rs.current_year = 2 OR rs.current_sem = 2) AND ss.is_approved = 'yes' THEN ss.year2_amount "
			+ " WHEN (rs.current_year = 3 OR rs.current_sem = 3) AND ss.is_approved = 'yes' THEN ss.year3_amount "
			+ " WHEN (rs.current_year = 4 OR rs.current_sem = 4) AND ss.is_approved = 'yes' THEN ss.year4_amount "
			+ " WHEN (rs.current_year = 5 OR rs.current_sem = 5) AND ss.is_approved = 'yes' THEN ss.year5_amount "
			+ " WHEN (rs.current_year = 6 OR rs.current_sem = 6) AND ss.is_approved = 'yes' THEN ss.year6_amount "
			+ " WHEN (rs.current_year = 7 OR rs.current_sem = 7) AND ss.is_approved = 'yes' THEN ss.year7_amount "
			+ " WHEN (rs.current_year = 8 OR rs.current_sem = 8) AND ss.is_approved = 'yes' THEN ss.year8_amount "
			+ " WHEN (rs.current_year = 9 OR rs.current_sem = 9) AND ss.is_approved = 'yes' THEN ss.year9_amount "
			+ " WHEN (rs.current_year = 10 OR rs.current_sem = 10) AND ss.is_approved = 'yes' THEN ss.year10_amount "
			+ " WHEN (rs.current_year = 11 OR rs.current_sem = 11) AND ss.is_approved = 'yes' THEN ss.year11_amount "
			+ " WHEN (rs.current_year = 12 OR rs.current_sem = 12) AND ss.is_approved = 'yes' THEN ss.year12_amount "
			+ "  ELSE 0 END as grant ) " + " FROM Student_Details s "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country " + "LEFT JOIN City cc ON cc.id = s.current_city "
			+ "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "WHERE s.ac_year_id = ?1 AND (rs.eligible_reported_status=2 OR rs.eligible_reported_status=4) and s.active=1 ")
	public Page<Object> getStudentDetailsDueIndexForNotReportedAndNotEligibleIndex1(Pageable pageable,
			Integer ac_year_id);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid,std.totalGrant as total_grant, "
			+ "CASE WHEN (rs.current_year = 1 OR rs.current_sem = 1) AND ss.is_approved = 'yes' THEN ss.year1_amount"
			+ " WHEN (rs.current_year = 2 OR rs.current_sem = 2) AND ss.is_approved = 'yes' THEN ss.year2_amount "
			+ " WHEN (rs.current_year = 3 OR rs.current_sem = 3) AND ss.is_approved = 'yes' THEN ss.year3_amount "
			+ " WHEN (rs.current_year = 4 OR rs.current_sem = 4) AND ss.is_approved = 'yes' THEN ss.year4_amount "
			+ " WHEN (rs.current_year = 5 OR rs.current_sem = 5) AND ss.is_approved = 'yes' THEN ss.year5_amount "
			+ " WHEN (rs.current_year = 6 OR rs.current_sem = 6) AND ss.is_approved = 'yes' THEN ss.year6_amount "
			+ " WHEN (rs.current_year = 7 OR rs.current_sem = 7) AND ss.is_approved = 'yes' THEN ss.year7_amount "
			+ " WHEN (rs.current_year = 8 OR rs.current_sem = 8) AND ss.is_approved = 'yes' THEN ss.year8_amount "
			+ " WHEN (rs.current_year = 9 OR rs.current_sem = 9) AND ss.is_approved = 'yes' THEN ss.year9_amount "
			+ " WHEN (rs.current_year = 10 OR rs.current_sem = 10) AND ss.is_approved = 'yes' THEN ss.year10_amount "
			+ " WHEN (rs.current_year = 11 OR rs.current_sem = 11) AND ss.is_approved = 'yes' THEN ss.year11_amount "
			+ " WHEN (rs.current_year = 12 OR rs.current_sem = 12) AND ss.is_approved = 'yes' THEN ss.year12_amount "
			+ "  ELSE 0 END as grant ) " + " FROM Student_Details s "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country " + "LEFT JOIN City cc ON cc.id = s.current_city "
			+ "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "WHERE CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %?1% "
			+ "AND s.ac_year_id = ?2 AND (rs.eligible_reported_status=2 OR rs.eligible_reported_status=4) and s.active=1 ")
	public Page<Object> getStudentDetailsDueIndexForNotReportedAndNotEligibleIndex2(Pageable pageable, Object keyword,
			Integer ac_year_id);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.program_specialization_id=?1 and rs.current_sem=?2 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchStudentDetailForSectionAssignmentOnSem(Integer program_specialization_id,
			Integer current_sem);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id " + "where "
			+ "s.program_specialization_id=?1 " + "and rs.current_year=?2 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> fetchStudentDetailForSectionAssignmentOnYear(Integer program_specialization_id,
			Integer current_year);

	@Query(value = "SELECT sd.student_id as student_id,sd.student_name as student_name,sd.auid as auid,"
			+ "sd.program_id as program_id,sd.program_specialization_id as program_specialization_id,"
			+ "ca.course_assignment_id as course_assignment_id,c.course_id as course_id,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_name as program_name, "
			+ "sd.created_username as created_username,p.program_short_name as program_short_name,ca.year_sem as year_sem,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,ca.duration as duration,ca.lecture as lecture,"
			+ "ca.remarks as remarks,ca.see_marks as see_marks,ca.total_credit as total_credit,ca.tutorial as tutorial,"
			+ "sy.syllabus_id as syllabus_id,sy.duration as syllabusDuration,sy.syllabus_objective as syllabus_objective,"
			+ "ct.course_type_name as course_type_name,ca.course_type_id as course_type_id,ca.course_category_id as course_category_id,"
			+ "cc.course_category_name as course_category_name,cc.type as courseCategoryName,cp.course_pattern_id as course_pattern_id,"
			+ "cp.credits as courseCredits,cp.percentage_of_credit as percentage_of_credit,"
			+ "c.course_name as course_name,c.course_short_name as course_short_name from student_details sd "
			+ "left join program p on sd.program_id=p.program_id "
			+ "left join program_specialization ps on sd.program_specialization_id=ps.program_specialization_id "
			+ "left join course_student_assignment csa on sd.student_id=csa.student_id "
			+ "left join course_assignment ca on csa.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join course_type ct on ct.course_type_id=ca.course_type_id "
			+ "left join syllabus sy on sy.course_assignment_id=ca.course_assignment_id "
			+ "left join course_category cc on cc.course_category_id=ca.course_category_id "
			+ "left join course_pattern cp on cc.course_category_id=cp.course_category_id "
			+ "where sd.student_id=?1 And sd.active=true group by csa.course_assignment_id", nativeQuery = true)
	public List<Map<String, Object>> getCourseDetailData(Integer student_id);

	@Query(value = "select new com.au.dto.GetStudentForFeedback( sd.student_id, sd.student_name, sd.auid, rs.current_year, rs.current_sem, s.section_name, sd.school_id, c.course_name ,sd.usn,ac.ac_year_id,"
			+ "sd.program_specialization_id , c.course_code , ac.ac_year) from Student_Details sd "
			+ " left join ReportingStudents rs on rs.student_id=sd.student_id "
			+ " left join StudentAttendance sa on sa.student_id=sd.student_id "
			+ " left join Section s on s.section_id=sa.section_id "
			+ " left join Course c on sa.course_id=c.course_id "
			+ " left join Academic_year ac on ac.ac_year_id=sa.ac_year_id "
			+ "where sd.student_id=:studentId and c.course_id=:courseId  group by sd.student_id,c.course_id ")
	public GetStudentForFeedback getStudentForFeedback(@Param("studentId") Integer studentId,
			@Param("courseId") Integer courseId);

	@Query(value = "Select std.student_id as student_id,rs.current_year as current_year, rs.current_sem as current_sem,std.student_name as student_name,std.acharya_email as acharya_email,"
			+ "std.auid as auid,cw.application_no_npf as application_no_npf  " + "FROM student_details std "
			+ "LEFT JOIN reporting_students rs ON std.student_id = rs.student_id "
			+ "LEFT JOIN candidate_walkin cw on cw.candidate_id=std.candidate_id "
			+ "Where std.student_id=?1 and std.active=true", nativeQuery = true)
	public Map<String, Object> getStudentDetailsData(Integer st);

	@Query("select new com.au.dto.StudenDetailsForIdCard(sd.student_id, sd.student_name, sd.auid, sd.mobile, sd.usn, sd.date_of_admission, rs.reporting_date, rs.current_year, "
			+ "rs.current_sem, sch.display_name,sd.student_image_path as studentImagePath,concat(pr.display_name, '-',ps.program_specialization_short_name) as programWithSpecialization,"
			+ "sch.school_name_short as schoolNameShort,sd.id_card_bucket_status as idCardBucketStatus) "
			+ "from Student_Details sd " + "left join ReportingStudents rs on rs.student_id = sd.student_id "
			+ "left join Schools sch on sch.school_id = sd.school_id "
			+ "left join Program pr on sd.program_id = pr.program_id "
			+ "left join ProgramSpecilization ps on sd.program_specialization_id = ps.program_specialization_id "
			+ "where  sd.school_id = :schoolId "
			+ "and (:programAssignmentId is null or sd.program_assignment_id = :programAssignmentId) "
			+ "and (:programId is null or sd.program_id = :programId) "
			+ "and (:programSpecializationId is null or sd.program_specialization_id = :programSpecializationId) "
			+ "and (sd.id_card_issued_year is null OR sd.id_card_issued_year != rs.current_sem) "
			+ "and rs.eligible_reported_status in (1, 3) " + "and rs.current_sem=:currentYearOrSem "
			+ "and sd.active = true")
	List<StudenDetailsForIdCard> findStudentDetailsForIdCardBySem(@Param("schoolId") Integer schoolId,
			@Param("programAssignmentId") Integer programAssignmentId, @Param("programId") Integer programId,
			@Param("programSpecializationId") Integer programSpecializationId, Integer currentYearOrSem);

	@Query("select new com.au.dto.StudenDetailsForIdCard(sd.student_id, sd.student_name, sd.auid, sd.mobile, sd.usn, sd.date_of_admission, rs.reporting_date, rs.current_year, "
			+ "rs.current_sem, sch.display_name,sd.student_image_path as studentImagePath,concat(pr.display_name, '-',ps.program_specialization_short_name) as programWithSpecialization,"
			+ "sch.school_name_short as schoolNameShort,sd.id_card_bucket_status as idCardBucketStatus) "
			+ "from Student_Details sd " + "left join ReportingStudents rs on rs.student_id = sd.student_id "
			+ "left join Schools sch on sch.school_id = sd.school_id "
			+ "left join Program pr on sd.program_id = pr.program_id "
			+ "left join ProgramSpecilization ps on sd.program_specialization_id = ps.program_specialization_id "
			+ "where  sd.school_id = :schoolId "
			+ "and (:programAssignmentId is null or sd.program_assignment_id = :programAssignmentId) "
			+ "and (:programId is null or sd.program_id = :programId) "
			+ "and (:programSpecializationId is null or sd.program_specialization_id = :programSpecializationId) "
			+ "and (sd.id_card_issued_year is null OR sd.id_card_issued_year != rs.current_sem) "
			+ "and rs.eligible_reported_status in (1, 3) " + "and rs.current_year=:currentYearOrSem "
			+ "and sd.active = true")
	List<StudenDetailsForIdCard> findStudentDetailsForIdCardByYear(@Param("schoolId") Integer schoolId,
			@Param("programAssignmentId") Integer programAssignmentId, @Param("programId") Integer programId,
			@Param("programSpecializationId") Integer programSpecializationId, Integer currentYearOrSem);

	@Query(value = "Select IfNUll(sd.id_card_issued_year,0) From student_details sd Where sd.student_id=?1", nativeQuery = true)
	public Integer getIdCardIssuedYear(Integer integer);

	/**
	 * id_card_issued_year is updating current sem of student
	 * 
	 */
	@Modifying
	@Query(value = "update Student_Details sd set sd.id_card_issued_year=?2 where sd.student_id=?1")
	public void updateIdCardIssuedYear(Integer studentId, Integer currentYear);

	@Query(value = "select Count(*) from student_details where email_preferred_name=?1 and active=true", nativeQuery = true)
	public int getCountOfPreferredName(String email_preferred_name);

	@Query(value = "SELECT * FROM student_details where auid=?1", nativeQuery = true)
	public Student_Details findByAuid(String auid);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "csa.rejected_remarks as rejected_remarks,csa.rejected_by as rejected_by,csa.rejected_date as rejected_date,"
			+ "csa.approved_remarks as csa_approved_remarks,csa.attachment_path as csa_attachment_path,csa.attachment_name as csa_attachment_name,"
			+ "csa.remarks as csa_remarks,csa.approved_by as csa_approved_by,csa.approved_date as csa_approved_date,"
			+ "csa.created_date as created_date,csa.modified_date as modified_date,csa.created_by as csa_created_by,"
			+ "ua1.username as approvedByName,ua2.username as created_by_Name,csa.cancel_id as cancel_id,"
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant) " + "FROM Student_Details s "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country " + "LEFT JOIN City cc ON cc.id = s.current_city "
			+ "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "left join CancelAddmissions csa on s.auid=csa.auid "
			+ "left join UserAuthentication ua1 on ua1.id=csa.approved_by "
			+ "left join UserAuthentication ua2 on ua2.id=csa.created_by "
			+ "WHERE s.ac_year_id = ?1 And s.deassign_status=2 AND "
			+ "(rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2)")
	public Page<Object> getStudentInactiveIndex1(Pageable pageable1, Integer ac_year_id);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "csa.rejected_remarks as rejected_remarks,csa.rejected_by as rejected_by,csa.rejected_date as rejected_date,"
			+ "csa.approved_remarks as csa_approved_remarks,csa.attachment_path as csa_attachment_path,csa.attachment_name as csa_attachment_name,"
			+ "csa.remarks as csa_remarks,csa.approved_by as csa_approved_by,csa.approved_date as csa_approved_date,"
			+ "csa.created_date as created_date,csa.modified_date as modified_date,csa.created_by as csa_created_by,"
			+ "ua1.username as approvedByName,ua2.username as created_by_Name,csa.cancel_id as cancel_id,"
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant) " + "FROM Student_Details s "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country " + "LEFT JOIN City cc ON cc.id = s.current_city "
			+ "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "left join CancelAddmissions csa on s.auid=csa.auid "
			+ "left join UserAuthentication ua1 on ua1.id=csa.approved_by "
			+ "left join UserAuthentication ua2 on ua2.id=csa.created_by "
			+ "WHERE CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %?1% "
			+ "AND s.ac_year_id = ?2 And s.deassign_status=2 AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2)")
	public Page<Object> getStudentInactiveIndex2(Pageable pageable, Object keyword, Integer ac_year_id);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile,"
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email,s.current_address as current_address,"
			+ "s.program_specialization_id as program_specialization_id,s.joining_year as joining_year, s.joining_sem as joining_sem,"
			+ "sc.school_name as school_name, s.school_id as school_id,fr.hostel_status as hostel_status,"
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name,s.candidate_id as candidate_id,"
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "s.id_barcode_generated as id_barcode_generated,ay.ac_year as ac_year,std.totalDue as feeDue, "
			+ " s.father_name as father_name,ps.program_specialization_short_name as program_specialization_short_name,  "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile,"
			+ "s.permanent_address as permanent_address,s.deassign_status as deassign_status,"
			+ "csa.rejected_remarks as rejected_remarks,csa.rejected_by as rejected_by,csa.rejected_date as rejected_date,"
			+ "csa.approved_remarks as csa_approved_remarks,csa.attachment_path as csa_attachment_path,csa.cancel_id as cancel_id,"
			+ "csa.attachment_name as csa_attachment_name,ua1.username as approvedByName,ua2.username as created_by_Name,"
			+ "csa.remarks as csa_remarks,csa.approved_by as csa_approved_by,csa.approved_date as csa_approved_date,"
			+ "csa.created_date as created_date,csa.modified_date as modified_date,csa.created_by as csa_created_by,"
			+ "dept.dept_id as dept_id,dept.dept_name as dept_name,dept.dept_name_short as dept_name_short,s.dateofbirth as dateofbirth,"
			+ "std.totalFix as fixed_fee, std.totalPaid as fee_paid ) " + "FROM Student_Details s "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN FeeReceipt fr ON s.student_id = fr.student_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN Department dept ON dept.dept_id = ps.dept_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "left join CancelAddmissions csa on s.auid=csa.auid "
			+ "left join UserAuthentication ua1 on ua1.id=csa.approved_by "
			+ "left join UserAuthentication ua2 on ua2.id=csa.created_by "
			+ "WHERE s.student_id = ?1 And s.deassign_status=2")
	public List<HashMap<String, Object>> Student_DetailsWithCancelAdmissionDetailsData(Integer student_id);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile,s.notes As notes, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name,s.created_by As created_by, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username,s.school_id As school_id, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.id_barcode_generated as id_barcode_generated, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_type as fee_admission_category_type, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,s.old_std_id_readmn as old_std_id_readmn,s.old_student_id As old_student_id,"
			+ "u.username as CounselorUserName, emp.employee_name as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status,pa.program_type As program_type, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status,"
			+ "sda.audit_status as audit_status,sda.student_document_audit_id as student_document_audit_id) "
			+ "FROM Student_Details s "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN ProgramAssigment pa ON s.program_assignment_id = pa.program_assignment_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "LEFT JOIN EmployeeDetails emp ON emp.email = u.email "
			+ "left join StudentDocumentAudit sda on sda.student_id=s.student_id "
			+ "where (:ac_year_id is null or s.ac_year_id = :ac_year_id) "
		    + "And (:school_id is null or s.school_id = :school_id) "
		    + "And (:program_id is null or s.program_id = :program_id) "
		    + "And (:program_specialization_id is null or s.program_specialization_id = :program_specialization_id) "
		    + "And (:fee_admission_category_id is null or s.fee_admission_category_id = :fee_admission_category_id) "
		    + "And (:userId is null or s.created_by = :userId) "
			+ "And s.active=true And (s.course_approver_status not in (0,1) OR s.course_approver_status is null ) group by s.student_id")
	public Page<Object> getStudentIndex1(Pageable pageable1, Integer ac_year_id, Integer school_id, 
			Integer program_id, Integer program_specialization_id, Integer fee_admission_category_id, Integer userId);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile,s.notes As notes, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile,pa.program_type As program_type,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,s.old_std_id_readmn As old_std_id_readmn,s.old_student_id As old_student_id,"
			+ "u.username as CounselorUserName, emp.employee_name as counselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion,"
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status,"
			+ "sda.audit_status as audit_status,sda.student_document_audit_id as student_document_audit_id) "
			+ "FROM Student_Details s "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN ProgramAssigment pa ON s.program_assignment_id = pa.program_assignment_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "LEFT JOIN EmployeeDetails emp ON emp.email = u.email "
			+ "left join StudentDocumentAudit sda on sda.student_id=s.student_id "
			+ "where (:ac_year_id is null or s.ac_year_id = :ac_year_id) "
		    + "And (:school_id is null or s.school_id = :school_id) "
		    + "And (:program_id is null or s.program_id = :program_id) "
		    + "And (:program_specialization_id is null or s.program_specialization_id = :program_specialization_id) "
		    + "And (:fee_admission_category_id is null or s.fee_admission_category_id = :fee_admission_category_id) "
		    + "And (:userId is null or s.created_by = :userId) "
			+ "And CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''),IFNULL(u.username, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_username, '')) LIKE %:keyword% "
			+ " And s.active=true And (s.course_approver_status not in (0,1) OR s.course_approver_status is null ) group by s.student_id")
	public Page<Object> getStudentIndex2(Pageable pageable, Object keyword, Integer ac_year_id, Integer school_id , 
			Integer program_id, Integer program_specialization_id, Integer fee_admission_category_id, Integer userId);

	@Query(value = "SELECT * FROM student_details where student_id IN (?1) and active=true", nativeQuery = true)
	public List<Student_Details> getStudentData(Set<Integer> studentIds);

	@Query(value = "SELECT s.student_name FROM student_details s WHERE s.acharya_email = :email And s.active=true", nativeQuery = true)
	public String studentNameByEmail(String email);

	@Query(value = "select Count(*) from student_details where auid=?1 and active=true", nativeQuery = true)
	public int checkAuidIsAlreadyPresentOrNot(String auid);

	@Query(value = "select st from Student_Details st where st.acharya_email=(select ua.email from UserAuthentication ua where ua.id=?1)")
	public Student_Details getStudentDataByUserID(Integer user_id);

	@Query(value = "select new com.au.dto.StudentDetailsResponse( sd.student_name as student_name," + " sd.usn as usn,"
			+ " rs.current_year as current_year," + " rs.current_sem as current_sem,sd.program_id as program_id,"
			+ "sd.program_specialization_id as program_specialization_id,sd.program_assignment_id as program_assignment_id ) from Student_Details sd "
			+ " left join ReportingStudents rs on rs.student_id=sd.student_id  "
			+ "where sd.student_id in (:studentId)")
	public List<StudentDetailsResponse> getStudentDetailsFromBatchAssignment(
			@Param("studentId") List<Integer> studentId);

	@Query(value = "select * from student_details st where st.auid=?1 And st.active=true", nativeQuery = true)
	public Optional<Student_Details> getStudentDetailsData(String auid);

	@Query(value = "select Count(*) from student_details where usn=?1 and active=true", nativeQuery = true)
	public int checkUsn(String usn);

//	@Query(value = "select s.section_name As section_name,sd.auid as auid,sd.student_name as student_name,rs.reporting_date as reporting_date,"
//			+ "rs.reporting_id as reporting_id,rs.reported_ac_year_id as reported_ac_year_id,rs.remarks as remarks,"
//			+ "rs.distinct_status as distinct_status,rs.previous_sem as previous_sem,rs.previous_year as previous_year,"
//			+ "rs.eligible_reported_status as eligible_reported_status,rs.year_back_status as year_back_status,rs.section_id as section_id,"
//			+ "sd.student_id as student_id,rs.current_year as current_year,rs.current_sem as current_sem,sd.usn as usn "
//			+ "from student_details sd "
//			+ "left join reporting_students rs on sd.student_id=rs.student_id "
//			+ "left join section s on s.section_id=rs.section_id "
//			+ "Where sd.student_id in (?1) And sd.active=true " , nativeQuery = true)
//	public List<Map<String, Object>> getAllReportedStudents(List<Integer> studentIds);
	@Query(value = "select s.section_name As section_name,sd.auid as auid,sd.student_name as student_name,rs.reporting_date as reporting_date,"
			+ "rs.reporting_id as reporting_id,rs.reported_ac_year_id as reported_ac_year_id,rs.remarks as remarks,"
			+ "rs.distinct_status as distinct_status,rs.previous_sem as previous_sem,rs.previous_year as previous_year,"
			+ "rs.eligible_reported_status as eligible_reported_status,rs.year_back_status as year_back_status,rs.section_id as section_id,"
			+ "sd.student_id as student_id,rs.current_year as current_year,rs.current_sem as current_sem,sd.usn as usn, "
			+ "sd.program_assignment_id as program_assignment_id, sd.program_specialization_id as program_specialization_id, sd.school_id as school_id "
			+ "from student_details sd "
			+ "left join reporting_students rs on sd.student_id=rs.student_id "
			+ "left join section s on s.section_id=rs.section_id "
			+ "Where sd.student_id in (?1) And sd.active=true " , nativeQuery = true)
	public List<Map<String, Object>> getAllReportedStudents(List<Integer> studentIds);

	@Query(value = "select sd.student_id as student_id,sd.student_name as student_name,sd.auid as auid,rs.current_sem as current_sem,rs.current_year as current_year,rs.created_date as created_date,"
			+ "rs.reporting_date As reporting_date,"
			+ "(select Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) from batch_assignment ba "
			+ "left join batch b on b.batch_id=ba.batch_id where ba.batch_assignment_id=?2) as concat_batch_name "
			+ "from student_details sd " 
			+ "left join reporting_students rs on rs.student_id=sd.student_id "
			+ "where sd.student_id in (?1) And sd.active=true", nativeQuery = true)
	public List<Map<String, Object>> studentDetailsWithBatchName(List<Integer> studentIds, Integer batch_assignment_id);

	@Modifying
	@Query(value = "update Student_Details sd set sd.candidate_id=null where sd.student_id=?1")
	public void setCandidateIdToNullForReadmission(Integer student_id);

	@Modifying
	@Query(value = "update Student_Details sd set sd.auid=null where sd.student_id=?1")
	public void setAuidToNullForReadmission(Integer student_id);

	@Modifying
	@Query(value = "update Student_Details sd set sd.active=false where sd.student_id=?1")
	public void deactivateStudentIdForReadmission(Integer studentId);

	@Modifying
	@Query(value = "update StudentTranscriptSubmission sd set sd.student_id=?2 where sd.student_id=?1 and sd.active=true")
	public void updateStudentTranscriptSubmission(Integer studentId, Integer newStudentId);

	@Modifying
	@Query(value = "update ApplicantDetails sd set sd.std_id=?2 where sd.std_id=?1 and sd.active=true")
	public void updateApplicantDetails(Integer studentId, Integer newStudentId);

	@Modifying
	@Query(value = "update PGApplicable sd set sd.std_id=?2 where sd.std_id=?1 and sd.active=true")
	public void updatePGApplicable(Integer studentId, Integer newStudentId);

	@Modifying
	@Query(value = "update StdEntranceExam sd set sd.student_id=?2 where sd.student_id=?1 and sd.active=true")
	public void updateStdEntranceExam(Integer studentId, Integer newStudentId);

	@Modifying
	@Query(value = "update ReportingStudents sd set sd.student_id=?2 where sd.student_id=?1 and sd.active=true")
	public void updateReportingStudents(Integer studentId, Integer newStudentId);

	@Modifying
	@Query(value = "update StdReportingStudentsHistory sd set sd.student_id=?2 where sd.student_id=?1 and sd.active=true")
	public void updateStdReportingStudentsHistory(Integer studentId, Integer newStudentId);

	@Modifying
	@Query(value = "update StudentMarks sd set sd.student_id=?2 where sd.student_id=?1 and sd.active=true")
	public void updateStudentMarks(Integer studentId, Integer newStudentId);

	@Modifying
	@Query(value = "update ProctorStudentAssignment sd set sd.student_id=?2 where sd.student_id=?1 and sd.active=true")
	public void updateProctorStudentAssignment(Integer studentId, Integer newStudentId);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile,s.acharya_email as acharya_email,s.candidate_id as candidate_id,"
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name,s.school_id as school_id, "
			+ "s.program_id as program_id, s.program_specialization_id as program_specialization_id,s.candidate_sex as candidate_sex,s.father_name as father_name,"
			+ "Concat(IfNull(ay.current_year,''),'-',IfNull((ay.current_year + pa.number_of_years),'')) as academic_batch,"
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name,ft.fee_template_name as fee_template_name,"
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name,fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem,"
			+ "org.org_id as org_id,org.org_name as org_name,org.org_type as org_type,sc.ref_no as ref_no, s.re_admission_status as re_admission_status,"
			+ "s.nationality as nationality,coun.name as CountryName,ft.fee_template_id As fee_template_id,"
			+ "re.oldAuid as oldAuid,re.oldStudentId as oldStudentId,re.semOrYear as semOrYear, ay1.ac_year as readmission_ac_year,"
			+ "sc.user_id_for_email As user_id_for_email,ua.id As userId,ua.email As principalEmail,"
			+ " ca.cancel_id as cancel_id, ca.approved_date as approved_date,re1.newAuid as newAuid, re1.newStudentId as newStudentId,"
			+ "na.nationality_id As nationality_id,na.nationality As nationalityName,rs.eligible_reported_status As eligible_reported_status,"
			+ "ed.emp_id As emp_id,ed.employee_name As proctorName,ed.empcode As ProctorEmpcode,sc.principal_sign As principal_sign, "
			+ "pt.program_type_id as program_type_id,pt.program_type_name as feeProgram_type_name,pt.program_type_code as program_type_code,"
			+ "pa.program_type As program_type_name,ed1.employee_name as CounselorName,se.section_name As section_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year_id as ac_year_id,ay.ac_year as ac_year,"
			+ "pa.number_of_years as number_of_years, pa.number_of_semester as number_of_semester) "
			+ "FROM Student_Details s "
			+ "LEFT JOIN CancelAddmissions ca ON s.auid = ca.auid and ca.approved_date IS NOT NULL " 
			+ "LEFT JOIN Readmission re ON re.newStudentId = s.student_id " 
			+ "LEFT JOIN Readmission re1 ON re1.oldStudentId = s.student_id "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN UserAuthentication ua ON ua.id = sc.user_id_for_email "
			+ "LEFT JOIN Organization org ON org.org_id = sc.org_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeTemplate ft ON ft.fee_template_id = s.fee_template_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_sub_category_id = ft.fee_admission_sub_category_id "
			+ "LEFT JOIN ProgramType pt ON ft.program_type_id = pt.program_type_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "left join Academic_year ay on s.ac_year_id = ay.ac_year_id "
			+ "left join Academic_year ay1 on re.acYearId = ay1.ac_year_id "
			+ "left join ProgramAssigment pa on s.program_assignment_id = pa.program_assignment_id "
			+ "left join Country coun on s.nationality = coun.id "
			+ "left join Nationality na on s.nationality = na.nationality_id "
			+ "left join ProctorStudentAssignment psa on s.student_id = psa.student_id "
			+ "left join EmployeeDetails ed on psa.emp_id = ed.emp_id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id "
			+ "left join EmployeeDetails ed1 on cw.counselor_id = ed1.emp_id "
			+ "LEFT JOIN SectionAssignment sa ON FIND_IN_SET(s.student_id, sa.student_ids) > 0 "
			+ "LEFT join Section se on se.section_id=sa.section_id "
			+ "WHERE s.auid=?1 And s.active=true")
	public List<Map<String, Object>> getStudentDetailsBasedOnStrudentId(String auid);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile,s.acharya_email as acharya_email,s.candidate_id as candidate_id,"
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name,s.school_id as school_id, "
			+ "s.program_id as program_id, s.program_specialization_id as program_specialization_id,s.candidate_sex as candidate_sex,s.father_name as father_name,"
			+ "Concat(IfNull(ay.current_year,''),'-',IfNull((ay.current_year + pa.number_of_years),'')) as academic_batch,"
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name,ft.fee_template_name as fee_template_name,"
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name,fasc.fee_admission_sub_category_short_name as fee_admission_sub_category_short_name,"
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_type as fee_admission_category_type,"
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem,"
			+ "sc.user_id_for_email As user_id_for_email,ua.id As userId,ua.email As principalEmail,"
			+ "org.org_id as org_id,org.org_name as org_name,org.org_type as org_type,sc.ref_no as ref_no, s.re_admission_status as re_admission_status,"
			+ "s.nationality as nationality,coun.name as CountryName,ft.fee_template_id As fee_template_id,"
			+ "re.oldAuid as oldAuid,re.oldStudentId as oldStudentId,re.semOrYear as semOrYear, ay1.ac_year as readmission_ac_year,"
			+ " ca.cancel_id as cancel_id, ca.approved_date as approved_date,re1.newAuid as newAuid, re1.newStudentId as newStudentId,"
			+ "na.nationality_id As nationality_id,na.nationality As nationalityName,rs.eligible_reported_status As eligible_reported_status,"
			+ "ed.emp_id As emp_id,ed.employee_name As proctorName,ed.empcode As ProctorEmpcode,"
			+ "re.oldAuid as oldAuid,re.oldStudentId as oldStudentId,re.semOrYear as semOrYear, ay1.ac_year as readmission_ac_year, ca.cancel_id as cancel_id, ca.approved_date as approved_date,"
			+ "ed.emp_id As emp_id,ed.employee_name As proctorName,ed.empcode As ProctorEmpcode,sc.principal_sign As principal_sign, "
			+ "pt.program_type_id as program_type_id,pt.program_type_name as feeProgram_type_name,pt.program_type_code as program_type_code,"
			+ "pa.program_type As program_type_name,ed1.employee_name as CounselorName,se.section_name As section_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ay.ac_year_id as ac_year_id,ay.ac_year as ac_year,"
			+ "pa.number_of_years as number_of_years, pa.number_of_semester as number_of_semester) "
			+ "FROM Student_Details s "
			+ "LEFT JOIN CancelAddmissions ca ON s.auid = ca.auid and ca.approved_date IS NOT NULL " 
			+ "LEFT JOIN Readmission re ON re.newStudentId = s.student_id " 
			+ "LEFT JOIN Readmission re1 ON re1.oldStudentId = s.student_id "
			+ "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN UserAuthentication ua ON ua.id = sc.user_id_for_email "
			+ "LEFT JOIN Organization org ON org.org_id = sc.org_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeTemplate ft ON ft.fee_template_id = s.fee_template_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_sub_category_id = ft.fee_admission_sub_category_id "
			+ "LEFT JOIN ProgramType pt ON ft.program_type_id = pt.program_type_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "left join Academic_year ay on s.ac_year_id = ay.ac_year_id "
			+ "left join Academic_year ay1 on re.acYearId = ay1.ac_year_id "
			+ "left join ProgramAssigment pa on s.program_assignment_id = pa.program_assignment_id "
			+ "left join Country coun on s.nationality = coun.id "
			+ "left join Nationality na on s.nationality = na.nationality_id "
			+ "left join ProctorStudentAssignment psa on s.student_id = psa.student_id "
			+ "left join EmployeeDetails ed on psa.emp_id = ed.emp_id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id "
			+ "left join EmployeeDetails ed1 on cw.counselor_id = ed1.emp_id "
			+ "LEFT JOIN SectionAssignment sa ON FIND_IN_SET(s.student_id, sa.student_ids) > 0 "
			+ "LEFT join Section se on se.section_id=sa.section_id "
			+ "WHERE s.student_id=?1 And s.active=true")
	public List<Map<String, Object>> getStudentDetailsBasedOnStrudentId(Integer student_id);

	@Query(value = "select student_id from Student_Details where auid=?1 and active=true")
	public Integer getStudentIdByAuid(String auid);

//	@Query(value = "SELECT * FROM student_details s left join candidate_walkin cw on cw.candidate_id=s.candidate_id WHERE cw.telegram_chat_id=?1 and cw.active=true", nativeQuery = true)
//	public Student_Details getStudentDataByTelegramChatId(String telegram_chat_id);

	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,rs.section_id,rs.current_year,rs.current_sem,"
			+ "sc.section_name,rs.reporting_date FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id "
			+ "left join section sc on rs.section_id=sc.section_id "
			+ "where s.student_id in (?1)  and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> assignedStudentByStudentId(List<Integer> studentIds);

	@Query(value = " select  sd.student_name as studentName, sd.auid as auid,"
			+ " rs.current_year as currentYear, rs.current_sem as currentSem, ft.fee_template_name as feeTemplateName,"
			+ " sd.program_id as programId, sd.program_specialization_id as programSpecializationId, sd.school_id as schoolId, sd.ac_year_id as acYearId, sd.student_id as studentId from student_details sd left join reporting_students rs on rs.student_id=sd.student_id "
			+ " left join fee_template ft on ft.fee_template_id=sd.fee_template_id where rs.current_sem=:sem and sd.school_id=:schoolId  and sd.program_id=:programId limit :pageSize offset :offset  ", nativeQuery = true)
	public List<Map<String, Object>> getStudentDetailsForDueReport(Integer sem, Integer schoolId, Integer programId,
			Integer pageSize, Integer offset);

	@Query(value = " select  new com.au.dto.StudentWiseDueReport( sd.student_name, sd.auid, rs.current_year, rs.current_sem,"
			+ "  ft.fee_template_name, sd.school_id,sd.ac_year_id, sd.program_id, sd.program_specialization_id ,s.s1due,s.s2due,s.s3due,s.s4due,s.s5due,s.s6due,s.s7due,s.s8due,s.s9due,s.s10due,s.s11due,s.s12due,cast(s.totalDue as big_decimal),s.totalAddOn as addOn, pa.number_of_semester  )   from StudentDues s "
			+" left join Student_Details sd  on s.studentId=sd.student_id "
			+ " left join ReportingStudents rs on rs.student_id=sd.student_id "
			+ " left join FeeTemplate ft on ft.fee_template_id=sd.fee_template_id"
			+ " left join ProgramAssigment pa on pa.program_assignment_id=sd.program_assignment_id "
			+ " where sd.school_id=:schoolId  and sd.program_id=:programId and sd.program_specialization_id =:programSpecializationId ")
	public List<StudentWiseDueReport> getStudentDetailsForDueReportBySchoolIdAndProgramId(Integer schoolId,
			Integer programId, Integer programSpecializationId);

	@Query(value = " select  sd.student_name as studentName, sd.auid as auid,"
			+ " rs.current_year as currentYear, rs.current_sem as currentSem, ft.fee_template_name as feeTemplateName,"
			+ " sd.program_id as programId, sd.program_specialization_id as programSpecializationId, sd.school_id as schoolId, "
			+ "sd.ac_year_id as acYearId, sd.student_id as studentId from Student_Details sd "
			+ "left join ReportingStudents rs on rs.student_id=sd.student_id "
			+ "left join FeeTemplate ft on ft.fee_template_id=sd.fee_template_id where sd.student_id=?1 and sd.active=true")
	public Map<String, Object> getStudentDetailsForDueReportByStudentId(Integer studentId);

	@Query(value = "select school_id from Student_Details where student_id=?1 and active=true")
	public Integer schoolIdOfStudentByStudentId(Integer studentId);

	@Query(value = "select student_id from student_details where auid=?1 and active=true", nativeQuery = true)
	public Integer getStudentIds(String auid);

	@Query(value = "select s.auid as auid, s.student_name as studentName, rs.current_year as currentYear, rs.current_sem as currentSem,"
			+ " sc.school_name_short as schoolName, p.program_short_name as programName, ps.program_specialization_short_name as programSpecializationName, s.father_name as fatherName,"
			+ " s.usn as usn " + "from student_details s "
			+ " left join reporting_students rs on rs.student_id=s.student_id "
			+ " left join program p on p.program_id=s.program_id "
			+ " left join program_specialization ps on ps.program_specialization_id=s.program_specialization_id"
			+ " left join schools sc on sc.school_id=s.school_id"
			+ " left join fee_admission_category fa on fa.fee_admission_category_id=s.fee_admission_category_id "
			+ "where s.student_id=:studentId  ", nativeQuery = true)
	public Map<String, Object> getStudentDetailsForPermission(Integer studentId);

	@Query(value = "Select std.auid FROM student_details std Where std.candidate_id=?1 and std.active=true ", nativeQuery = true)
	public String getAuidByCandidateId(Integer candidateId);

	boolean existsByAuid(String auid);

	@Query(value = "SELECT " + "s.school_id AS Institute, " + "sc.school_name, "
			+ "SUM(CASE WHEN f.is_regular = true THEN 1 ELSE 0 END) AS 'Student Entry', "
			+ "SUM(CASE WHEN f.is_regular = false THEN 1 ELSE 0 END) AS 'Lateral Entry', "
			+ "SUM(CASE WHEN s.active = false THEN 1 ELSE 0 END) AS 'InActive', "
			+ "SUM(CASE WHEN rs.eligible_reported_status = 5 THEN 1 ELSE 0 END) AS 'Graduates', "
			+ "(SUM(CASE WHEN f.is_regular = true THEN 1 ELSE 0 END) + "
			+ "SUM(CASE WHEN f.is_regular = false THEN 1 ELSE 0 END) - "
			+ "SUM(CASE WHEN s.active = false THEN 1 ELSE 0 END)) AS 'Total' " + "FROM student_details s "
			+ "LEFT JOIN schools sc ON sc.school_id = s.school_id "
			+ "LEFT JOIN fee_admission_category f ON f.fee_admission_category_id = s.fee_admission_category_id "
			+ "LEFT JOIN reporting_students rs ON rs.student_id = s.student_id " + "WHERE s.ac_year_id = :acYearId "
			+ "GROUP BY s.school_id " + "ORDER BY s.school_id", nativeQuery = true)
	public List<Map<String, Object>> misInstituteWiseReport(Integer acYearId);

	@Query(value = "SELECT ay.ac_year AS year, "
			+ "SUM(CASE WHEN f.is_regular = true THEN 1 ELSE 0 END) AS 'Student Entry', "
			+ "SUM(CASE WHEN f.is_regular = false THEN 1 ELSE 0 END) AS 'Lateral Entry', "
			+ "SUM(CASE WHEN s.active = false THEN 1 ELSE 0 END) AS 'InActive', "
			+ "SUM(CASE WHEN rs.eligible_reported_status = 5 THEN 1 ELSE 0 END) AS 'Graduates', "
			+ "(SUM(CASE WHEN f.is_regular = true THEN 1 ELSE 0 END) + "
			+ "SUM(CASE WHEN f.is_regular = false THEN 1 ELSE 0 END) - "
			+ "SUM(CASE WHEN s.active = false THEN 1 ELSE 0 END)) AS 'Total' " + "FROM student_details s "
			+ "LEFT JOIN fee_admission_category f ON f.fee_admission_category_id = s.fee_admission_category_id "
			+ "LEFT JOIN reporting_students rs ON rs.student_id = s.student_id "
			+ "LEFT JOIN academic_year ay ON ay.ac_year_id = s.ac_year_id " + "GROUP BY ay.ac_year "
			+ "ORDER BY ay.ac_year", nativeQuery = true)
	public List<Map<String, Object>> misYearWiseReport();

	@Query(value = "    SELECT " + "    DATE(s.created_date) AS created_day,  "
			+ "    SUM(CASE WHEN s.lateral_sem IS NULL THEN 1 ELSE 0 END) AS 'Student Entry', "
			+ "    SUM(CASE WHEN s.lateral_sem IS NOT NULL THEN 1 ELSE 0 END) AS 'Laternal Entry', "
			+ "    COUNT(*) AS 'Total' " + "FROM " + "    student_details s " + "WHERE "
			+ "    MONTH(s.created_date) =:month " + "    AND YEAR(s.created_date) =:year " + "GROUP BY "
			+ "    created_day  " + "ORDER BY " + "    created_day;", nativeQuery = true)
	public List<Map<String, Object>> misDayWiseReport(Integer month, Integer year);

	@Query(value = "  SELECT "
			+ "    concat(p.program_short_name,'-', ps.program_specialization_short_name ) as course, "
			+ "    SUM(CASE WHEN s.lateral_sem IS NULL THEN 1 ELSE 0 END) AS 'Student Entry', "
			+ "    SUM(CASE WHEN s.lateral_sem IS NOT NULL THEN 1 ELSE 0 END) AS 'Laternal Entry', "
			+ "    COUNT(*) AS 'Total' " + "FROM " + "    student_details s "
			+ "   left join program p on p.program_id=s.program_id "
			+ "   left join program_specialization ps on ps.program_specialization_id=s.program_specialization_id "
			+ "WHERE " + "      s.ac_year_id=:acYearId AND  s.school_id=:schoolId " + "GROUP BY "
			+ "  s.program_id , s.program_specialization_id ", nativeQuery = true)
	public List<Map<String, Object>> misProgramWiseReport(Integer acYearId, Integer schoolId);

	@Query(value = "Select s.student_id as student_id,s.student_name As student_name, s.auid as auid, s.usn as usn "
			+ "From student_details s "
			+ "WHERE s.student_id=?1 And s.active=true " , nativeQuery = true)
	public List<Map<String, Object>> getStudentDetailsBasedOnStudentId(Integer sId);

	@Modifying
	@Query(value = "Update Student_Details sd set sd.usn=:usn where sd.student_id=:studentId")
	public void updateUsnOfStudentByStudentId(String usn, Integer studentId);

	@Query(value = "SELECT new map(s.student_id as student_id,s.student_name As student_name, s.auid as auid, s.usn as usn, s.acharya_email As acharya_email ) "
			+ "FROM Student_Details s " + "WHERE s.student_id=?1 And s.active=true ")
	public Map<String, Object> getData(Integer sId);

	@Query(value = "SELECT distinct new  com.au.dto.StudentAuidDto(substring(sd.auid,1,5) as auidOrAuidWithoutIncrement ) FROM Student_Details sd Where sd.active=true And sd.auid is not null", nativeQuery = false)
	public List<StudentAuidDto> auidForStudentReporting();

	@Modifying
	@Query(value = "Update Student_Details sd set sd.usn=:usn where sd.auid=:auid And sd.active=true")
	public void updateUsnOfStudentByAuid(String usn, String auid);

	@Modifying
	@Query(value = "update Student_Details sd set sd.id_card_bucket_status=true where sd.student_id=?1")
	public void updateIdCardBucketStatus(Integer studentId);

	@Query(value = "Select sd.course_approver_status From Student_Details sd where sd.student_id=?1 ")
	public Integer getPreviousCourseApproverStatus(Integer oldStudentId);

	@Query(value = "SELECT * FROM student_details where student_id=?1 and active=true", nativeQuery = true)
	public Student_Details getStudentByStudentIdAndActive(Integer studentId);

	/* change Of course is initiated */
	@Modifying
	@Query(value = "update Student_Details sd set sd.course_approver_status=2 where sd.student_id=?1 ")
	void updateChangeOfCourseApproverStatus(Integer oldStudentId);

	@Query(value = "select sd.student_id as student_id,sd.active as active,"
			+ "sd.student_name As student_name,sd.usn As usn,sd.acharya_email As acharya_email,sd.auid As auid,"
			+ "sd.program_specialization_id As program_specialization_id,sd.program_assignment_id As program_assignment_id,sd.program_id As program_id,"
			+ "sd.fee_admission_category_id As fee_admission_category_id,du.total_due As total_due,du.total_fix As total_fix,"
			+ "sd.candidate_id As candidate_id, cw.npf_status As npf_status,"
			+ "rs.reporting_id As reporting_id,rs.eligible_reported_status As eligible_reported_status,"
			+ "rs.current_sem As current_sem,rs.current_year As current_year,ps.program_specialization_name As program_specialization_name,"
			+ "fac.fee_admission_category_type As fee_admission_category_type,fac.fee_admission_category_short_name As fee_admission_category_short_name,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "ps.program_specialization_short_name As program_specialization_short_name,p.program_name As program_name,p.program_short_name As program_short_name From student_details sd "
			+ "LEFT JOIN reporting_students rs on rs.student_id=sd.student_id "
			+ "LEFT JOIN candidate_walkin cw on cw.candidate_id=sd.candidate_id "
			+ "LEFT JOIN program_specialization ps on ps.program_specialization_id=sd.program_specialization_id "
			+ "LEFT JOIN program p on p.program_id=sd.program_id "
			+ "LEFT JOIN fee_admission_category fac ON sd.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN fee_admission_sub_category fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN student_dues du on du.student_id=sd.student_id "
			+ "Where sd.school_id=?1 and sd.program_id=?2 and sd.program_specialization_id=?3 "
			+ "and (rs.current_sem=?4 or rs.current_year=?5) And sd.active=true group by sd.student_id", nativeQuery = true)
	public List<Map<String, Object>> studentNoDueStudentDetails(Integer school_id, Integer program_id,
			Integer program_specialization_id, Integer current_sem, Integer current_year);

	@Query(value = " SELECT "
			+ "        vh.voucher_head_new_id AS voucherHeadId, "
			+ "        vh.voucher_head AS voucherHead, "
			+ "        fc.year1_amt AS amount, "
			+ "        cw.school_id AS schoolId, "
			+ "        cw.candidate_name AS candidateName, "
			+ "        pg.program_name AS program, "
			+ "        ps.program_specialization_name AS programSpecialization, "
			+ "        cw.candidate_email AS email, "
			+ "        cw.npf_status AS npfStatus, "
			+ "        cw.link_exp AS linkExp, "
			+ "        cw.mobile_number AS mobile, "
			+ "        cw.application_no_npf AS applicationNoNpf, "
			+ "        ct.currency_type_short_name as currencyType  "
			+ "    FROM "
			+ "        preadmission_process p  "
			+ "    LEFT JOIN "
			+ "        fee_template_sub_amount fc              "
			+ "            ON fc.fee_template_id = p.fee_template_id  "
			+ "    LEFT JOIN "
			+ "        fee_template ft              "
			+ "            ON ft.fee_template_id = p.fee_template_id  "
			+ "    LEFT JOIN "
			+ "        currency_type ct              "
			+ "            ON ct.currency_type_id = ft.currency_type_id  "
			+ "    LEFT JOIN "
			+ "        voucher_head_new vh              "
			+ "            ON vh.voucher_head_new_id = fc.voucher_head_new_id  "
			+ "    LEFT JOIN "
			+ "        program pg              "
			+ "            ON pg.program_id = p.program_id  "
			+ "    LEFT JOIN "
			+ "        program_specialization ps              "
			+ "            ON ps.program_specialization_id = p.program_specialization_id  "
			+ "    LEFT JOIN "
			+ "        candidate_walkin cw              "
			+ "            ON cw.candidate_id = p.candidate_id  "
			+ "    WHERE "
			+ "        p.candidate_id =:candidateId          "
			+ "        AND vh.voucher_head = 'Registration Fee'  "
			+ "     AND p.active=1 "
			+ "    GROUP BY "
			+ "        vh.voucher_head_new_id;", 
	        nativeQuery = true)
	public Map<String, Object> getRegistrationFeeDetails(Integer candidateId);

	@Query(value = " select new com.au.dto.StudentDetailsbyEmail( s.student_id as studentId, CONCAT(s.auid,' - ',s.student_name) as studentName, s.auid as auid ) from Student_Details s where s.acharya_email=:email  and s.active=true", nativeQuery = false)
	public StudentDetailsbyEmail getStudentByEmail(String email);

	@Query(value = "select new map(st.student_id as newStudentId,fac.fee_admission_category_type as feeAdmissionCategoryType ,"
			+ "st.fee_admission_category_id as feeAdmissionCategory_id,ps.program_specialization_short_name as programSpecializationShortName,"
			+ "ps.program_specialization_name as programSpecializationName,st.program_specialization_id as programSpecializationId,"
			+ "pr.program_short_name as programShortName,pr.program_name as programName,st.program_id as programId,"
			+ "sc.school_name as schoolName,sc.school_name_short as schoolNameShort,st.school_id as schoolId,"
			+ "st.student_name as studentName,st.ac_year_id as acYearId,ay.ac_year as acYear,na.nationality_id As nationalityId,na.nationality As nationality,"
			+ "cocpa.changeOfCourseProgramAttachmentPath as changeOfCourseProgramAttachmentPath,cocpa.remarks as remarks )"
			+ "from Student_Details st " + "left join Academic_year ay on st.ac_year_id=ay.ac_year_id "
			+ "left join Schools sc on st.school_id=sc.school_id "
			+ "left join Program pr on st.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on st.program_specialization_id = ps.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on st.fee_admission_category_id=fac.fee_admission_category_id "
			+ "left join Nationality na ON na.nationality_id = st.nationality "
			+ "left join ChangeOfCourseProgramAttachment cocpa ON cocpa.newStudentId = st.student_id "
			+ "left join FeeTemplate ft on ft.fee_template_id=st.fee_template_id where st.old_student_id=?1 And st.course_approver_status= 0 ")
	public List<HashMap<String, Object>> studentDetailsForChangeOFCourse(Integer oldStudentId);

	@Query(value = "  SELECT " + "    s.candidate_sex as candidate_sex, " + "    COUNT(*) AS 'Total',"
			+ "  ay.ac_year as acYear," + "  sc.school_name_short as schoolName " + "FROM " + "    student_details s "
			+ " left join academic_year ay on ay.ac_year_id=s.ac_year_id"
			+ " left join schools sc on sc.school_id=s.school_id " + "WHERE "
			+ "    (:acYearId IS NULL OR s.ac_year_id = :acYearId) AND "
			+ "    (:schoolId IS NULL OR s.school_id = :schoolId) " + "  GROUP BY s.candidate_sex ", nativeQuery = true)
	public List<Map<String, Object>> misGenderWiseReport(Integer acYearId, Integer schoolId);

	@Query(value = "Select st.student_id as student_id,st.student_image_path as student_image_path from student_details st "
			+ "WHERE st.student_image_path IS NOT NULL AND st.auid LIKE CONCAT(?1, '%') And st.active=true", nativeQuery = true)
	public List<Map<String, Object>> getImageAndStudentId(String auid);

	@Modifying
	@Query(value = "update Student_Details sd set sd.student_image_path=null where sd.student_id=?1")
	public void updateStudentImagePath(Integer studentId);

	@Query(value = "  SELECT " + "      CASE " + "          WHEN (:schoolId IS NOT NULL OR :acYearId IS NOT NULL) "
			+ "               AND :countryId IS NULL AND :stateId IS NULL AND :cityId IS NULL THEN c.name "
			+ "          WHEN :countryId IS NOT NULL AND :stateId IS NULL AND :cityId IS NULL THEN st.name "
			+ "          WHEN :stateId IS NOT NULL AND :cityId IS NULL THEN ct.name "
			+ "          WHEN :cityId IS NOT NULL THEN ct.name " + "      END AS name, " + "      CASE "
			+ "          WHEN (:schoolId IS NOT NULL OR :acYearId IS NOT NULL) "
			+ "               AND :countryId IS NULL AND :stateId IS NULL AND :cityId IS NULL THEN s.current_country "
			+ "          WHEN :countryId IS NOT NULL AND :stateId IS NULL AND :cityId IS NULL THEN s.current_state "
			+ "          WHEN :stateId IS NOT NULL AND :cityId IS NULL THEN s.current_city "
			+ "          WHEN :cityId IS NOT NULL THEN s.current_city " + "      END AS id, "
			+ "      COUNT(*) AS Total " + "FROM " + "    student_details s "
			+ "    LEFT JOIN program p ON p.program_id = s.program_id "
			+ "    LEFT JOIN program_specialization ps ON ps.program_specialization_id = s.program_specialization_id "
			+ "    LEFT JOIN countries c ON c.id = s.current_country "
			+ "    LEFT JOIN states st ON st.id = s.current_state "
			+ "    LEFT JOIN cities ct ON ct.id = s.current_city " + "WHERE "
			+ "    (:schoolId IS NULL OR s.school_id = :schoolId) AND "
			+ "    (:acYearId IS NULL OR s.ac_year_id = :acYearId) AND "
			+ "    (:countryId IS NULL OR s.current_country = :countryId) AND "
			+ "    (:stateId IS NULL OR s.current_state = :stateId) AND "
			+ "    (:cityId IS NULL OR s.current_city = :cityId) " + "GROUP BY " + "    CASE "
			+ "        WHEN (:schoolId IS NOT NULL OR :acYearId IS NOT NULL) "
			+ "             AND :countryId IS NULL AND :stateId IS NULL AND :cityId IS NULL THEN s.current_country "
			+ "        WHEN :countryId IS NOT NULL AND :stateId IS NULL AND :cityId IS NULL THEN s.current_state "
			+ "        WHEN :stateId IS NOT NULL AND :cityId IS NULL THEN s.current_city "
			+ "        WHEN :cityId IS NOT NULL THEN s.current_city " + "    END", nativeQuery = true)
	public List<Map<String, Object>> misGeoLocationReport(Integer schoolId, Integer acYearId, Integer countryId,
			Integer stateId, Integer cityId);

	@Query(value = "select new com.au.dto.StudentDetailsAttendanceDto(sd.student_name as student_name,sd.auid as auid,rs.reporting_date as reportingDate) From Student_Details sd "
			+ "left join ReportingStudents rs on rs.student_id=sd.student_id Where sd.student_id=?1")
	public StudentDetailsAttendanceDto studentDetailForAttendance(Integer studentId);

	@Query(value = "Select new map(std.student_id as student_id, sc.school_name_short as institute_name,"
			+ "std.program_id as program_id,std.student_name as student_name, std.usn as usn, pr.program_name as course_name,std.acharya_email as acerp_email,"
			+ "pr.program_short_name as course_short_name, ps.program_specialization_name as course_branch_name,"
			+ "ay.ac_year as ac_year,ps.program_specialization_short_name as course_branch_short_name,"
			+ "rs.current_year as current_year,rs.current_sem as current_sem,std.auid as auid ) From Student_Details std "
			+ "Left Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ "left join Candidate_Walkin cw on cw.candidate_id=std.candidate_id "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "left join ProgramAssigment pra on std.program_assignment_id=pra.program_assignment_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on std.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join ReportingStudents rs on rs.student_id=std.student_id where std.auid=?1 And std.active=true")
	public HashMap<String, Object> getStudentDetailsByAuidForLms(String auid);
	
	@Query(value = "Select new map(std.student_id as student_id, sc.school_name_short as institute_name_short,"
			+ "std.program_id as program_id,std.student_name as student_name, std.usn as usn, pr.program_name as course_name,std.acharya_email as acharya_email,"
			+ "pr.program_short_name as course_short_name, ps.program_specialization_name as course_branch_name,"
			+ "ay.ac_year as ac_year,ps.program_specialization_short_name as course_branch_short_name,"
			+ "rs.current_year as current_year,rs.current_sem as current_sem,std.auid as auid ) From Student_Details std "
			+ "Left Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ "left join Candidate_Walkin cw on cw.candidate_id=std.candidate_id "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "left join ProgramAssigment pra on std.program_assignment_id=pra.program_assignment_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on std.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join ReportingStudents rs on rs.student_id=std.student_id "
			+ "where std.school_id=:schoolId And std.program_specialization_id =:specializationId "
			+ "And std.ac_year_id=:acYearId And std.active=true")
	public List<HashMap<String, Object>> studentDetailsBySchoolSpecializationAcYear(Integer schoolId,
			Integer specializationId, Integer acYearId);
	
	
	@Query(value=" select new com.au.dto.programAndTypeDetailsOfStudentDto(sd.program_assignment_id as programAssignmentId,"
			+ "sd.program_id as programId,pt.program_type_id as ProgramTypeId,sd.program_specialization_id as programSpecializationId,"
			+ "pr.program_name as programName,pr.program_short_name as programShortName, pt.program_type_name as programType,"
			+ "pa.number_of_years as numberOfYears,pa.number_of_semester as numberOfSemester,ps.program_specialization_name as programSpecializationName,"
			+ "ps.program_specialization_short_name as programSpecializationShortName ) from Student_Details sd "
			+ "Inner join ProgramAssigment pa on pa.program_assignment_id=sd.program_assignment_id "
			+ "left join Program pr on pr.program_id=sd.program_id "
			+ "left join ProgramType pt on pt.program_type_id=pa.program_type_id "
			+ "left join ProgramSpecilization ps on ps.program_specialization_id=sd.program_specialization_id "
			+ "where sd.student_id=:studentId ", nativeQuery = false)
	public programAndTypeDetailsOfStudentDto programAndTypeDetailsOfStudentDto(Integer studentId);

	@Query(value = "select s.student_id, s.student_name,s.date_of_admission,p.program_short_name, ps.program_specialization_short_name,rs.created_by, rs.reporting_date  from student_details s"
	        + "    LEFT JOIN program p ON p.program_id = s.program_id "
			+ "    LEFT JOIN program_specialization ps ON ps.program_specialization_id = s.program_specialization_id "
			+ "    LEFT JOIN reporting_students rs on rs.student_id=s.student_id "
			+ "    LEFT JOIN countries c ON c.id = s.current_country "
			+ "    LEFT JOIN states st ON st.id = s.current_state "
			+ "    LEFT JOIN cities ct ON ct.id = s.current_city " + "WHERE "
			+ "    (:schoolId IS NULL OR s.school_id = :schoolId) AND "
			+ "    (:acYearId IS NULL OR s.ac_year_id = :acYearId) AND "
			+ "    (:countryId IS NULL OR s.current_country = :countryId) AND "
			+ "    (:stateId IS NULL OR s.current_state = :stateId) AND "
			+ "    (:cityId IS NULL OR s.current_city = :cityId) " + "GROUP BY " + "    CASE "
			+ "        WHEN (:schoolId IS NOT NULL OR :acYearId IS NOT NULL) "
			+ "             AND :countryId IS NULL AND :stateId IS NULL AND :cityId IS NULL THEN s.current_country "
			+ "        WHEN :countryId IS NOT NULL AND :stateId IS NULL AND :cityId IS NULL THEN s.current_state "
			+ "        WHEN :stateId IS NOT NULL AND :cityId IS NULL THEN s.current_city "
			+ "        WHEN :cityId IS NOT NULL THEN s.current_city END ", nativeQuery = true)
	public List<Map<String, Object>> getStudentDetailsGeoloactionWise(Integer schoolId, Integer acYearId,
			Integer countryId, Integer stateId, Integer cityId);
	
	
	
	
	@Query(value = "SELECT new map(sd.student_id AS student_id,sd.account_holder_name AS account_holder_name,sd.account_number AS account_number,"
			+ "sd.acharya_email AS acharya_email,sd.active AS active,sd.adhar_number AS adhar_number,sd.allotment_number AS allotment_number,"
			+ "sd.auid AS auid,sd.bank_branch AS bank_branch,sd.bank_name AS bank_name,sd.blood_group AS blood_group,"
			+ "sd.board_admission_date AS board_admission_date,sd.board_admission_status AS board_admission_status,"
			+ "sd.c_city AS c_city,sd.candidate_sex AS candidate_sex,sd.caste AS caste,sd.caste_category AS caste_category,"
			+ "sd.category_alloted AS category_alloted,sd.category_reserved AS category_reserved,sd.course_approver_status AS course_approver_status,"
			+ "sd.created_by AS created_by,sd.created_date AS created_date,sd.created_username AS created_username,"
			+ "sd.current_address AS current_address,sd.current_adress1 AS current_adress1,sd.current_city AS current_city,"
			+ "sd.current_country AS current_country,sd.current_email AS current_email,sd.current_mobile AS current_mobile,"
			+ "sd.current_phone AS current_phone,sd.current_pincode AS current_pincode,sd.current_state AS current_state,"
			+ "sd.date_of_admission AS date_of_admission,sd.dateofbirth AS dateofbirth,sd.dateofjoining AS dateofjoining,"
			+ "sd.email_preferred_name AS email_preferred_name,sd.entrance_test_no AS entrance_test_no,"
			+ "sd.entranct_test_type AS entranct_test_type,sd.father_email AS father_email,sd.father_income AS father_income,"
			+ "sd.father_mobile AS father_mobile,sd.father_name AS father_name,sd.father_occupation AS father_occupation,"
			+ "sd.father_qualification AS father_qualification,sd.joining_sem AS joining_sem,sd.joining_year AS joining_year,"
			+ "sd.mobile AS mobile,sd.mother_mobile AS mother_mobile,sd.mother_name AS mother_name,sd.nationality AS nationality,"
			+ "sd.rank1 AS rank1,sd.religion AS religion,sd.rural_urban AS rural_urban,sd.special_category AS special_category,"
			+ "sd.student_email AS student_email,sd.student_name AS student_name,sd.surname AS surname,"
			+ "sd.usn AS usn,sd.marital_status AS marital_status,sd.whatsapp_number AS whatsapp_number,sd.alternate_number AS alternate_number) "
			+ "FROM Student_Details sd " 
			+ "LEFT JOIN ReportingStudents rs ON sd.student_id = rs.student_id "
			+ "LEFT JOIN City c ON c.id = sd.permanant_city " 
			+ "LEFT JOIN State st ON st.id = sd.permanant_state "
			+ "LEFT JOIN Country con ON con.id = sd.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = sd.nationality "
			+ "WHERE CONCAT(IFNULL(sd.student_id, ''), IFNULL(sd.auid, ''), IFNULL(sd.acharya_email, ''), IFNULL(sd.adhar_number, ''), "
			+ "IFNULL(sd.student_name, ''), IFNULL(sd.date_of_admission, ''), IFNULL(sd.father_name, ''), IFNULL(sd.created_by, '')) LIKE %?1% "
			+ "AND sd.ac_year_id = ?2 And sd.active=true AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (sd.course_approver_status not in (0,1) OR sd.course_approver_status is null ) group by sd.student_id")
	public Page<Object> getStudentIndexCustomExpoet(Pageable pageable, Object keyword, Integer ac_year_id);
	
	
	@Query(value = "SELECT new map(sd.student_id AS student_id,sd.account_holder_name AS account_holder_name,sd.account_number AS account_number,"
			+ "sd.acharya_email AS acharya_email,sd.active AS active,sd.adhar_number AS adhar_number,sd.allotment_number AS allotment_number,"
			+ "sd.auid AS auid,sd.bank_branch AS bank_branch,sd.bank_name AS bank_name,sd.blood_group AS blood_group,"
			+ "sd.board_admission_date AS board_admission_date,sd.board_admission_status AS board_admission_status,"
			+ "sd.c_city AS c_city,sd.candidate_sex AS candidate_sex,sd.caste AS caste,sd.caste_category AS caste_category,"
			+ "sd.category_alloted AS category_alloted,sd.category_reserved AS category_reserved,sd.course_approver_status AS course_approver_status,"
			+ "sd.created_by AS created_by,sd.created_date AS created_date,sd.created_username AS created_username,"
			+ "sd.current_address AS current_address,sd.current_adress1 AS current_adress1,sd.current_city AS current_city,"
			+ "sd.current_country AS current_country,sd.current_email AS current_email,sd.current_mobile AS current_mobile,"
			+ "sd.current_phone AS current_phone,sd.current_pincode AS current_pincode,sd.current_state AS current_state,"
			+ "sd.date_of_admission AS date_of_admission,sd.dateofbirth AS dateofbirth,sd.dateofjoining AS dateofjoining,"
			+ "sd.email_preferred_name AS email_preferred_name,sd.entrance_test_no AS entrance_test_no,"
			+ "sd.entranct_test_type AS entranct_test_type,sd.father_email AS father_email,sd.father_income AS father_income,"
			+ "sd.father_mobile AS father_mobile,sd.father_name AS father_name,sd.father_occupation AS father_occupation,"
			+ "sd.father_qualification AS father_qualification,sd.joining_sem AS joining_sem,sd.joining_year AS joining_year,"
			+ "sd.mobile AS mobile,sd.mother_mobile AS mother_mobile,sd.mother_name AS mother_name,sd.nationality AS nationality,"
			+ "sd.rank1 AS rank1,sd.religion AS religion,sd.rural_urban AS rural_urban,sd.special_category AS special_category,"
			+ "sd.student_email AS student_email,sd.student_name AS student_name,sd.surname AS surname,pa.program_type As program_type,"
			+ "sd.usn AS usn,sd.marital_status AS marital_status,sd.whatsapp_number AS whatsapp_number,sd.alternate_number AS alternate_number) "
			+ "FROM Student_Details sd " 
			+ "LEFT JOIN ReportingStudents rs ON sd.student_id = rs.student_id "
			+ "LEFT JOIN ProgramAssigment pa ON sd.program_assignment_id = pa.program_assignment_id "
			+ "LEFT JOIN City c ON c.id = sd.permanant_city " 
			+ "LEFT JOIN State st ON st.id = sd.permanant_state "
			+ "LEFT JOIN Country con ON con.id = sd.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = sd.nationality "
			+ "WHERE sd.ac_year_id = ?1 And sd.active=true AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (sd.course_approver_status not in (0,1) OR sd.course_approver_status is null ) group by sd.student_id")
	public Page<Object> getStudentIndexCustomExpoet1(Pageable pageable1, Integer ac_year_id);

	@Query(value = "Select new map(std.student_id as student_id, sc.school_name_short as institute_name,"
			+ "std.program_id as program_id,std.student_name as student_name, std.usn as usn, pr.program_name as course_name,std.acharya_email as acerp_email,"
			+ "pr.program_short_name as course_short_name, ps.program_specialization_name as course_branch_name,"
			+ "ay.ac_year as ac_year,ps.program_specialization_short_name as course_branch_short_name,"
			+ "rs.current_year as current_year,rs.current_sem as current_sem,std.auid as auid ) From Student_Details std "
			+ "Left Join FeeTemplate ft On std.fee_template_id = ft.fee_template_id "
			+ "left join Candidate_Walkin cw on cw.candidate_id=std.candidate_id And std.active=true "
			+ "left join Schools sc on std.school_id=sc.school_id "
			+ "left join Program pr on std.program_id=pr.program_id "
			+ "left join ProgramAssigment pra on std.program_assignment_id=pra.program_assignment_id "
			+ "left join Academic_year ay on std.ac_year_id=ay.ac_year_id "
			+ "left join ProgramSpecilization ps on std.program_specialization_id = ps.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on std.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join ReportingStudents rs on rs.student_id=std.student_id Where std.student_id in (:assignedStudentIds) ")
	public List<HashMap<String,Object>> assignedBatchOrSectionStudentDetails(List<Integer> assignedStudentIds);

	@Query(value = "select * from student_details sd where sd.date_of_admission=date(now()) and sd.active= true", nativeQuery = true)
	public List<Student_Details> studentAdmissionDataByCurrentDate();

	
	@Query(value="SELECT  "
			+ "    COUNT(*) AS total, "
			+ "    ay.ac_year_code as academicYear, "
			+ "    ay.ac_year_id as academicYearId, "
			+ "    CASE  "
			+ "        WHEN :countryId IS NOT NULL THEN c.name "
			+ "        WHEN :stateId IS NOT NULL THEN st.name "
			+ "        WHEN :cityId IS NOT NULL THEN ct.name "
			+ "        ELSE c.name "
			+ "    END AS name "
			+ "FROM  "
			+ "    student_details s "
			+ "LEFT JOIN  "
			+ "    countries c ON c.id = s.current_country "
			+ "LEFT JOIN  "
			+ "    states st ON st.id = s.current_state "
			+ "LEFT JOIN  "
			+ "    cities ct ON ct.id = s.current_city "
			+ "LEFT JOIN  "
			+ "    academic_year ay ON ay.ac_year_id = s.ac_year_id "
			+ "LEFT JOIN  "
			+ "    schools sc ON sc.school_id = s.school_id "
			+ "WHERE  "
			+ "    (c.id =:countryId OR :countryId IS NULL) "
			+ "    AND (st.id =:stateId OR :stateId IS NULL) "
			+ "    AND (ct.id =:cityId OR :cityId IS NULL)"
			+ "    AND  (sc.school_id =:schoolId OR :schoolId IS NULL) "
			+ "GROUP BY  "
			+ "    ay.ac_year_id ", nativeQuery = true)
	public List<Map<String , Object>> misGeolocationUpdateQuery(Integer schoolId,Integer countryId,Integer stateId,Integer cityId);
	
	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.id_barcode_generated as id_barcode_generated, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_type as fee_admission_category_type, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE s.ac_year_id = ?1 AND s.created_by=?2 And s.active=true AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (s.course_approver_status not in (0,1) OR s.course_approver_status is null ) group by s.student_id")
	public Page<Object> sortedStudentDetailsByUser(Pageable pageable1, Integer acYearId, Integer userId);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion,"
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %?1% "
			+ "AND s.ac_year_id = ?2 AND s.created_by=?3 AND s.active=true AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (s.course_approver_status not in (0,1) OR s.course_approver_status is null )  group by s.student_id")
	public Page<Object> searchedAndSortedStudentDetailsByUser(Pageable pageable, Object keyword, Integer acYearId, Integer userId);

	@Modifying
	@Query(value = "update ReportingStudents sd set sd.active=false where sd.student_id=?1 and sd.active=true")
	public void deactivateReportingStudentForReadmission(Integer student_id);


	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion,"
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %?1% "
			+ "AND s.ac_year_id = ?2 AND s.school_id=?3 AND s.active=true AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (s.course_approver_status not in (0,1) OR s.course_approver_status is null )  group by s.student_id")
	public Page<Object> searchedAndSortedStudentDetailsBySchoolId(Pageable pageable, Object keyword, Integer acYearId, Integer school_id);
	
	
	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.id_barcode_generated as id_barcode_generated, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_type as fee_admission_category_type, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE s.ac_year_id = ?1 AND s.school_id=?2 And s.active=true AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (s.course_approver_status not in (0,1) OR s.course_approver_status is null ) group by s.student_id")
	public Page<Object> sortedStudentDetailsBySchoolId(Pageable pageable1, Integer acYearId, Integer school_id);

	@Query(value="select s.school_id from Student_Details s where s.student_id=:studentId and s.active = true")
	public Integer getStudentSchoolId(Integer studentId);

	@Query(value="select s.ac_year_id from Student_Details s where s.student_id=:studentId And s.active=true")
	public Integer getStudentAcademicYearId(Integer studentId);

	
	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion,"
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %?1% "
			+ "AND s.ac_year_id = ?2 AND s.fee_admission_category_id=?3 AND s.active=true AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (s.course_approver_status not in (0,1) OR s.course_approver_status is null )  group by s.student_id")
	public Page<Object> searchedAndSortedStudentDetailsByAdmissionCategory(Pageable pageable, Object keyword,
			Integer acYearId, Integer fee_admission_category_id);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.id_barcode_generated as id_barcode_generated, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_type as fee_admission_category_type, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE s.ac_year_id = ?1 AND s.fee_admission_category_id=?2 And s.active=true AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (s.course_approver_status not in (0,1) OR s.course_approver_status is null ) group by s.student_id")
	public Page<Object> sortedStudentDetailsByAdmissionCategory(Pageable pageable1, Integer acYearId,
			Integer fee_admission_category_id);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "d.dept_id As dept_id,d.dept_name As dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion,"
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN Department d ON ps.dept_id = d.dept_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %:keyword% "
			+ "AND (:acYearId is null or s.ac_year_id = :acYearId) "
			+ "AND (:dept_id is null or d.dept_id = :dept_id) "
			+ "And (:school_id is null or s.school_id = :school_id) "
			+ "AND s.active=true "
			+ "AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (s.course_approver_status not in (0,1) OR s.course_approver_status is null )  group by s.student_id")
	public Page<Object> studentDetailsByDept(Pageable pageable, Object keyword, Integer acYearId, Integer dept_id , Integer school_id);
	

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.id_barcode_generated as id_barcode_generated, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_type as fee_admission_category_type, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, "
			+ "d.dept_id As dept_id,d.dept_name As dept_name,d.dept_name_short as dept_name_short,"
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,s.school_id As school_id,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN Department d ON ps.dept_id = d.dept_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE  (:acYearId is null or s.ac_year_id = :acYearId) "
			+ "AND (:dept_id is null or d.dept_id = :dept_id) "
			+ "And (:school_id is null or s.school_id = :school_id) "
			+ "And s.active=true AND (rs.eligible_reported_status = 3 OR rs.eligible_reported_status = 4 OR "
			+ "rs.eligible_reported_status = 1 OR rs.eligible_reported_status = 2) "
			+ "And (s.course_approver_status not in (0,1) OR s.course_approver_status is null ) group by s.student_id")
	public Page<Object> studentDetailsByDept(Pageable pageable1, Integer acYearId, Integer dept_id , Integer school_id);

	
	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name,s.created_by As created_by, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username,s.active As active, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion,"
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "where (:ac_year_id is null or s.ac_year_id = :ac_year_id) "
		    + "And (:school_id is null or s.school_id = :school_id) "
		    + "And (:program_id is null or s.program_id = :program_id) "
		    + "And (:program_specialization_id is null or s.program_specialization_id = :program_specialization_id) "
		    + "And (:fee_admission_category_id is null or s.fee_admission_category_id = :fee_admission_category_id) "
		    + "And (:userId is null or s.created_by = :userId) "
			+ "And CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %:keyword% "
			+ " And s.active=false  group by s.student_id")
	public Page<Object> getinActiveStudentDetailsIndex1(Pageable pageable, Object keyword, Integer ac_year_id,
			Integer school_id, Integer program_id, Integer program_specialization_id, Integer fee_admission_category_id,
			Integer userId);

	
	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name,s.active As active, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username,s.school_id As school_id, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.id_barcode_generated as id_barcode_generated, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_type as fee_admission_category_type, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "where (:ac_year_id is null or s.ac_year_id = :ac_year_id) "
		    + "And (:school_id is null or s.school_id = :school_id) "
		    + "And (:program_id is null or s.program_id = :program_id) "
		    + "And (:program_specialization_id is null or s.program_specialization_id = :program_specialization_id) "
		    + "And (:fee_admission_category_id is null or s.fee_admission_category_id = :fee_admission_category_id) "
		    + "And (:userId is null or s.created_by = :userId) "
			+ "And s.active=false  group by s.student_id")
	public Page<Object> getinActiveStudentDetailsIndex2(Pageable pageable1, Integer ac_year_id, Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer fee_admission_category_id, Integer userId);

	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_sem=?4 and rs.eligible_reported_status in (4,6) and "
			+ "rs.reporting_date is null and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> allNotReportedStudentDetailsBySem(Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer current_sem);
	
	@Query(value = "SELECT rs.reporting_id as id,s.student_id,s.auid,s.usn,s.student_name,rs.eligible_reported_status,"
			+ "rs.remarks,rs.current_year,rs.current_sem,rs.active,rs.created_by,rs.created_date,rs.created_username,"
			+ "rs.distinct_status,rs.previous_sem,rs.previous_year,rs.reported_ac_year_id,"
			+ "rs.reporting_date,rs.section_id,rs.year_back_status "
			+ "FROM student_details s Inner join reporting_students rs on rs.student_id=s.student_id "
			+ "where  s.school_id=?1 and s.program_id=?2 and s.program_specialization_id=?3 and rs.current_year=?4 and rs.eligible_reported_status in (4,6) and "
			+ "rs.reporting_date is null and s.active=true", nativeQuery = true)
	public List<Map<String, Object>> allNotReportedStudentDetailsByYear(Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer current_year);
	
	@Query(value = "select new com.au.dto.StudentDetailsAttendanceDto(sd.student_name as student_name,sd.auid as auid,rs.reporting_date as reportingDate,"
			+ "sd.ac_year_id as acYearId,sd.program_assignment_id as programAssignmentId,sd.program_id as programId,"
			+ "sd.program_specialization_id as programSpecializationId,rs.current_sem as currentSem,rs.current_year as currentYear,sd.student_id as studentId) From Student_Details sd "
			+ "left join ReportingStudents rs on rs.student_id=sd.student_id Where sd.student_id=?1")
	public StudentDetailsAttendanceDto studentDetailForAttendanceForMobile(Integer studentId);

	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile,s.active As active, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.school_id as school_id, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "fac.fee_admission_category_type as fee_admission_category_type, s.id_barcode_generated as id_barcode_generated, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, s.created_by as created_by, "
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "d.dept_id As dept_id,d.dept_name As dept_name,d.dept_name_short as dept_name_short,"
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,ay.ac_year_id As ac_year_id,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion,"
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN Department d ON ps.dept_id = d.dept_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE  (:ac_year_id is null or s.ac_year_id = :ac_year_id) "
			+ "AND (:dept_id is null or d.dept_id = :dept_id) "
			+ "And (:school_id is null or s.school_id = :school_id) "
			+ "And s.active=false And "
			+ "CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
			+ "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %:keyword%  group by s.student_id")
	public Page<Object> InactiveStudentDetailsByDept1(Pageable pageable, Object keyword, Integer ac_year_id,Integer dept_id, Integer school_id);
	
	@Query(value = "SELECT new map(s.student_id as id, s.auid as auid, s.usn as usn, s.mobile as mobile,s.active As active, "
			+ "s.date_of_admission as date_of_admission, s.firstname as firstname, s.student_name as student_name, "
			+ "s.program_id as program_id, s.acharya_email as acharya_email, rs.distinct_status as distinct_status, "
			+ "s.program_specialization_id as program_specialization_id, s.created_username as created_username, "
			+ "s.joining_year as joining_year, s.joining_sem as joining_sem, s.fee_template_id as fee_template_id, "
			+ "s.fee_admission_category_id as fee_admission_category_id, sc.school_name as school_name, s.id_barcode_generated as id_barcode_generated, "
			+ "pr.program_name as program_name, pr.program_short_name as program_short_name, ft.fee_template_name as fee_template_name, "
			+ "fac.fee_admission_category_short_name as fee_admission_category_short_name,fac.fee_admission_category_type as fee_admission_category_type, s.old_auid_format as old_auid_format, "
			+ "ps.program_specialization_name as program_specialization_name, sc.school_name_short as school_name_short, "
			+ "rs.reporting_id as reporting_id, rs.current_year as current_year, rs.current_sem as current_sem, "
			+ "d.dept_id As dept_id,d.dept_name As dept_name,d.dept_name_short as dept_name_short,ay.ac_year_id As ac_year_id,"
			+ "rs.eligible_reported_status as eligible_reported_status, rs.year_back_status as year_back_status, "
			+ "ua.username as username, ay.ac_year as ac_year, ps.program_specialization_short_name as program_specialization_short_name, "
			+ "ft.fee_year_total_amount as fee_year_total_amount, std.totalDue as feeDue, s.candidate_id as candidate_id, s.father_name as father_name, "
			+ "s.mother_name as mother_name, s.father_mobile as father_mobile, s.mother_mobile as mother_mobile, "
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "u.username as CounselorName,u.id As CounselorId,s.nationality As nationality,na.nationality As Na_nationality,s.religion As religion, "
			+ "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
			+ "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
			+ "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
			+ "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.course_approver_status as course_approver_status) "
			+ "FROM Student_Details s " + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
			+ "LEFT JOIN Program pr ON s.program_id = pr.program_id "
			+ "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
			+ "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN FeeAdmissionSubCategory fasc ON fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
			+ "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
			+ "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
			+ "LEFT JOIN Department d ON ps.dept_id = d.dept_id "
			+ "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
			+ "LEFT JOIN City c ON c.id = s.permanant_city " + "LEFT JOIN State st ON st.id = s.permanant_state "
			+ "LEFT JOIN Country con ON con.id = s.permanant_country "
			+ "LEFT JOIN Nationality na ON na.nationality_id = s.nationality "
			+ "LEFT JOIN City cc ON cc.id = s.current_city " + "LEFT JOIN State stc ON stc.id = s.current_state "
			+ "LEFT JOIN Country conc ON conc.id = s.current_country "
			+ "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
			+ "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
			+ "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "WHERE  (:ac_year_id is null or s.ac_year_id = :ac_year_id) "
			+ "AND (:dept_id is null or d.dept_id = :dept_id) "
			+ "And (:school_id is null or s.school_id = :school_id) And s.active=false group by s.student_id ")
	public Page<Object> InactiveStudentDetailsByDept2(Pageable pageable1, Integer ac_year_id, Integer dept_id, Integer school_id);

	@Query(value = "SELECT sd.student_id FROM student_details sd "
			+ "where (sd.mobile=:custnumber or sd.father_mobile =:custnumber or sd.mother_mobile=:custnumber) And sd.active=true", nativeQuery = true)
	public Integer getStudentDetailsDataForIvr(String custnumber);

	@Query(value="select s.fee_template_id from Student_Details s where s.student_id=:studentId and s.active = true ")
	public Integer getFeeTemplateIdByStudentId(Integer studentId);
	
	@Query(value = "SELECT * FROM student_details where auid=?1 and active = true ", nativeQuery = true)
	public Student_Details getStudentByAuid(String auid);

	@Query(value = "SELECT s.auid FROM student_details s where s.student_id=?1", nativeQuery = true)
	public String getAuidByStudentId(Integer studentId);

	
	@Query(value = "select count(*) from student_details sd where sd.candidate_id=?1 And sd.active=true" , nativeQuery = true)
	public Integer getCountOfCandidateID(Integer candidate_id);

	@Query(value = "select sd.mobile As mobile,sd.father_mobile As father_mobile,sd.mother_mobile As mother_mobile "
			+ "from student_details sd where sd.student_id=?1 And sd.active=true" , nativeQuery = true)
	public Map<String, Object> getMobileNumber(Integer student_id);


	@Query(value = "select ct.currency_type_short_name from student_details s left join fee_template ft on ft.fee_template_id=s.fee_template_id "
			+ "	left join currency_type ct on ct.currency_type_id=ft.currency_type_id where s.student_id=:studentId " , nativeQuery = true)
	public String getCurrenyTypeByStudentId(Integer studentId);

	@Query(value = "select sd.student_id As student_id,sd.auid As auid,sd.usn as usn, sd.mobile as mobile,sd.active As active,"
			+ " sd.student_name as student_name,sd.acharya_email as acharya_email,rs.reporting_id As reporting_id,rs.current_year As current_year,"
			+ "rs.current_sem As current_sem,rs.reporting_date As reporting_date,rs.eligible_reported_status As eligible_reported_status,rs.reported_ac_year_id As reported_ac_year_id "
			+ "from student_details sd "
			+ "LEFT JOIN reporting_students rs ON sd.student_id = rs.student_id "
			+ "where sd.student_id=?1 And sd.active=true" , nativeQuery = true)
	public Map<String, Object> studentDetailsBasedOnSectionAssignmentId(Integer student);


	@Query("SELECT s.student_name, s.mobile FROM Student_Details s WHERE s.active = true AND s.acharya_email = :email")
	Optional<Tuple> findMobileByActiveTrueAndAcharyaEmail(@Param("email") String acharyaEmail);


	@Query(value = "select s.current_country, s.current_state, s.current_city, s.ac_year_id from student_details s", nativeQuery = true)
	List<Object[]> findAllWithRequiredFieldsRaw();


	@Query(value = "SELECT con.name name, ac.current_year academicYear, ac.ac_year_id academicYearId, COUNT(*) AS total, con.id id FROM student_details sd " +
			"JOIN countries con ON sd.current_country = con.id " +
			"JOIN academic_year ac ON sd.ac_year_id = ac.ac_year_id " +
			"WHERE sd.active = 1 " +
			"AND (:schoolId IS NULL OR sd.school_id = :schoolId) " +
			"GROUP BY sd.ac_year_id, con.name " +
			"ORDER BY con.name, ac.current_year", nativeQuery = true)
	List<Map<String, Object>> getGeoData(Integer schoolId);

	@Query(value = "SELECT st.name name, ac.current_year academicYear, ac.ac_year_id academicYearId, COUNT(*) AS total, st.id id " +
			"FROM student_details sd " +
			"JOIN states st ON st.id = sd.current_state " +
			"JOIN academic_year ac ON sd.ac_year_id = ac.ac_year_id " +
			"WHERE  current_country = :countryId AND sd.active = 1 " +
			"AND (:schoolId IS NULL OR sd.school_id = :schoolId) " +
			"GROUP BY sd.ac_year_id, name " +
			"ORDER BY name, academicYear", nativeQuery = true)
	List<Map<String, Object>> getGeoData(Integer countryId, Integer schoolId);

	@Query(value = "SELECT ct.name name, ac.current_year academicYear, ac.ac_year_id academicYearId, COUNT(*) AS total, ct.id id " +
			"FROM student_details sd " +
			"JOIN cities ct ON ct.id = sd.current_city " +
			"JOIN academic_year ac ON sd.ac_year_id = ac.ac_year_id " +
			"WHERE current_country = :countryId AND current_state = :stateId AND sd.active = 1 " +
			"AND (:schoolId IS NULL OR sd.school_id = :schoolId) " +
			"GROUP BY sd.ac_year_id, name " +
			"ORDER BY name, academicYear", nativeQuery = true)
	List<Map<String, Object>> getGeoData(Integer countryId, Integer stateId, Integer schoolId);

	@Query(value = "SELECT ct.name name, ac.current_year academicYear, ac.ac_year_id academicYearId,COUNT(*) AS total " +
			"FROM student_details sd " +
			"JOIN cities ct ON ct.id = sd.current_city " +
			"JOIN academic_year ac ON sd.ac_year_id = ac.ac_year_id " +
			"WHERE current_country = :countryId AND current_state = :stateId AND current_city = :cityId AND sd.active = 1 " +
			"AND (:schoolId IS NULL OR sd.school_id = :schoolId) " +
			"GROUP BY sd.ac_year_id  " +
			"ORDER BY academicYear", nativeQuery = true)
	List<Map<String, Object>> getGeoData(Integer countryId, Integer stateId, Integer cityId, Integer schoolId);

	@Query(value = "SELECT bpt.razor_pay_transaction_id AS razor_pay_transaction_id,bpt.amount AS amount,bpt.code AS code,"
			+ "    bpt.created_date AS created_date,bpt.description AS description,bpt.email AS email,bpt.mobile AS mobile,"
			+ "    bpt.modified_date AS modified_date,bpt.name AS name,bpt.order_id AS order_id,bpt.paid_year AS paid_year,"
			+ "    bpt.payment_id AS payment_id,bpt.payment_type AS payment_type,bpt.reason AS reason,"
			+ "    bpt.receipt_id AS receipt_id,bpt.remarks AS remarks,bpt.school_id AS school_id,"
			+ "	    sc.school_name As school_name,sc.school_name_short As school_name_short,"
			+ "    bpt.signature AS signature,bpt.source AS source,bpt.status AS status,bpt.transaction_id AS transaction_id,"
			+ "    bpt.step AS step,bpt.transaction_date AS transaction_date,bpt.transaction_type AS transaction_type "
			+ "from bulk_pay_transaction bpt "
			+ "LEFT JOIN schools sc ON sc.school_id = bpt.school_id "
			+ "where (:transaction_id IS NULL OR bpt.transaction_id = :transaction_id) " , nativeQuery = true)
	public List<Map<String, Object>> getBulkPayTransaction(String transaction_id);

	@Query(value = " SELECT " +
			"    oft.ac_year_id AS ac_year_id, " +
			"    oft.program_id AS course_id, " +
			"    oft.program_specialization_id AS course_branch_id, " +
			"    s.school_id AS institute_id, " +
			"    p.program_short_name AS course_short_name, " +
			"    ps.program_specialization_short_name AS course_branch_short_name, " +
			"    s.school_name_short AS institute_name_short, " +
			"    oft.uniform_number AS uniform_name, " +
			"    ofd.total AS total_amount, " +
			"    oft.created_by, " +
			"    oft.created_date, " +
			"    oft.modified_by, " +
			"    oft.modified_date, " +
			"    oft.active, " +
			"    oft.other_fee_template_id AS id " +
			" FROM  " +
			" other_fee_template oft " +
			" LEFT JOIN  " +
			"    program p ON p.program_id = oft.program_id " +
			" LEFT JOIN  " +
			"    program_specialization ps ON ps.program_specialization_id = oft.program_specialization_id  " +
			" LEFT JOIN  " +
			"    other_fee_details ofd ON ofd.template_id = oft.other_fee_template_id " +
			" LEFT JOIN  " +
			" schools s ON s.school_id = oft.school_id " +
			" WHERE  " +
			"    oft.ac_year_id = ?1" +
			"   and  oft.feetype = 'Uniform And Stationery Fee' "+
			" ORDER BY  " +
			"    s.created_date DESC ", nativeQuery = true)
	public List<Map<String, Object>> getStudentDetailsForUniformTemplate(Integer ac_year_id);

	
	@Query(value = "SELECT count(sd.student_id) As studentCount,sd.ac_year_id As ac_year_id,sd.school_id As school_id,"
			+ "sc.school_name_short As school_name_short,ac.ac_year As ac_year "
			+ "FROM student_details sd "
			+ "left JOIN schools sc on sc.school_id= sd.school_id "
			+ "LEFT JOIN academic_year ac ON ac.ac_year_id = sd.ac_year_id "
			+ "WHERE sd.ac_year_id = :acYearId AND sd.date_of_admission <= CAST(:cutoffDate AS DATE) AND sd.active = 1 "
			+ "group by sd.school_id,sd.ac_year_id ", nativeQuery = true)
	List<Map<String, Object>> getAdmissionCountsWOSclId(@Param("acYearId") Integer acYearId, @Param("cutoffDate") String cutoffDate);

	
	@Query(value = " select count(sd.student_id) As studentCount ,sd.ac_year_id As ac_year_id ,sd.school_id As school_id,sc.school_name_short As school_name_short,ac.ac_year As ac_year,"
			+ " sd.program_id As program_id,p.program_short_name As program_short_name,"
			+ "sd.program_specialization_id As program_specialization_id , ps.program_specialization_short_name As program_specialization_short_name "
			+ " from student_details sd "
			+ " left join schools sc on sc.school_id= sd.school_id "
			+ "LEFT JOIN academic_year ac ON ac.ac_year_id = sd.ac_year_id "
			+ "LEFT JOIN program p ON p.program_id = sd.program_id "
			+ "LEFT JOIN program_specialization ps ON ps.program_specialization_id = sd.program_specialization_id "
			+ " WHERE sd.ac_year_id = :acYearId AND sd.date_of_admission <= CAST(:cutoffDate AS DATE) AND sd.active = 1 And sd.school_id=:school_id "
			+ " group by sd.school_id,sd.ac_year_id, sd.program_id,ps.program_specialization_id ", nativeQuery = true)
	public List<Map<String, Object>> getCountOfAdmissionDate(@Param("acYearId") Integer acYearId, @Param("cutoffDate") String cutoffDate , @Param("school_id") Integer school_id);

	@Query(value = "SELECT * FROM student_details where day(dateofbirth)=day(?1) and  month(dateofbirth)=month(?1) and active=true", nativeQuery=true)
	public List<Student_Details> getAllStudentsHavingBirthday(LocalDate formattedDate);

	@Query(value = "SELECT * FROM student_details where candidate_id = :candidateId and active = true ", nativeQuery = true)
	public Student_Details getStudentByCandidateId(Integer candidateId);

	@Query(value = "SELECT " +
			"    rft.registration_fee_transaction_id AS registrationFeeTransactionId, " +
			"    rft.amount AS amount, " +
			"    rft.candidate_id AS candidateId, " +
			"    rft.code AS code, " +
			"    rft.created_date AS createdDate, " +
			"    rft.description AS description, " +
			"    rft.mobile_number AS mobileNumber, " +
			"    rft.modified_date AS modifiedDate, " +
			"    rft.order_id AS orderId, " +
			"    rft.payment_id AS paymentId, " +
			"    rft.reason AS reason, " +
			"    rft.receipt_id AS receiptId, " +
			"    rft.signature AS signature, " +
			"    rft.source AS source, " +
			"    rft.status AS status, " +
			"    rft.step AS step, " +
			"    rft.transaction_date AS transactionDate, " +
			"    rft.voucher_id AS voucherId, " +
			"    rft.transaction_id AS transactionId, " +
			"    rft.receipt_status AS receiptStatus, " +
			"    rft.settlement_id AS settlementId, " +
			"    rft.settlementutr AS settlementUtr, " +
			"    rft.dollar_value AS dollarValue, " +
			"    rft.school_id AS schoolId " +
			"FROM student_details sd  " +
			"JOIN registration_fee_transaction rft ON sd.candidate_id = rft.candidate_id " +
			"WHERE sd.student_id = ?1  " +
			"AND sd.active = true  " +
			"AND rft.status = 'success' ", nativeQuery = true)
	public List<Map<String,Object>> getRegistrationFeeDetailsOfStudent(Integer studentId);

	@Query(value = " select c.course_id as courseId,c.course_name as courseName,c.course_code as courseCode "
			+ "from student_attendance s left join time_table tt on tt.time_table_id=s.time_table_id "
			+ "left join time_table_employee te on te.time_table_id=tt.time_table_id "
			+ "left join subject_assignment sa on te.subject_assignment_id=sa.subjet_assign_id "
			+ "left join course_assignment co on co.course_assignment_id=sa.course_assignment_id "
			+ "left join course c on c.course_id=co.course_id where te.emp_id=:empId and s.student_id=:studentId group by c.course_id, c.course_name, c.course_code ", nativeQuery = true)
	public List<Map<String, Object>> getCourseListFromTimeTableForStudent(Integer empId,Integer studentId);

	
	
	@Query(value=" select count(rs) from ReportingStudents rs where rs.section_id=:sectionId")
	public Long getStudentCountBySectionId(Integer sectionId);

	@Query(value = " select sc.school_id as schoolId, sc.school_name as schoolName "
			+ "from  student_details s "
			+ "left join schools sc on sc.school_id=s.school_id where s.student_id=:student_id ", nativeQuery = true)
	public Map<String, Object> getStudentInstitute(Integer student_id);

	
	
	@Query(value = "select sd.student_id As student_id,sd.auid As auid "
			+ "FROM student_details sd "
			+ "left join acharya_erp.user_details ud on ud.email=sd.acharya_email where ud.id=?1 And ud.active=true", nativeQuery = true)
	public Map<String, Object> getStudentDetailsBasedOnUserId(Integer userId);

	@Query(value = "SELECT * FROM student_details where student_id=?1 And active=true", nativeQuery = true)
	public Student_Details get1(Integer sd);


	@Query(value = "SELECT sd.date_of_admission FROM student_details sd where sd.student_id=?1 And sd.active=true", nativeQuery = true)
    Date dateOfAdmissionByStudentId(Integer studentId);

	@Query(value = "select new map(std.student_name as student_name,std.auid as auid,std.usn as usn,std.date_of_admission as date_of_admission,"
			+ "fac.fee_admission_category_type as fee_admission_category_type,ft.fee_template_name as fee_template_name,rst.current_year as current_year,rst.current_sem as current_sem) "
			+ "from Student_Details std  " + "left join FeeTemplate ft on ft.fee_template_id=std.fee_template_id "
			+ "left join ReportingStudents rst On rst.student_id =std.student_id "
			+ "left join FeeAdmissionCategory fac on ft.fee_admission_category_id=fac.fee_admission_category_id "
			+ "where std.fee_template_id in (?1)")
		List<HashMap<String, Object>> findStudentDetailsByFeeTemplateIds(List<Integer> feeTemplateIds);

	@Query(value = "select new com.au.dto.StudentDetailsAttendanceDto(sd.student_name as student_name,sd.auid as auid,rs.reporting_date as reportingDate,sd.student_id as studentId) From Student_Details sd "
			+ "left join ReportingStudents rs on rs.student_id=sd.student_id Where sd.student_id in (?1) And sd.active=true And rs.active=true")
	List<StudentDetailsAttendanceDto> studentDetailForAttendanceBulk(List<Integer> convertCommaSeperatedIdsAsList);

	@Query(value = "SELECT std.student_name as student_name,std.auid as auid,std.usn as usn," +
            "std.student_id as student_id,std.school_id As school_id,sch.school_name_short As school_name_short," +
            "std.program_specialization_id As program_specialization_id , ps.program_specialization_short_name As program_specialization_short_name FROM student_details std " +
            "Left Join schools sch on sch.school_id= std.school_id " +
            "Left Join program_specialization ps ON ps.program_specialization_id = sd.program_specialization_id where std.fee_template_id=?1 And std.active=true", nativeQuery = true)
	List<Map<String, Object>> studentsForPaidAtBoardTag(Integer feeTemplateId);
}
