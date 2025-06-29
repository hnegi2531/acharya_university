package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ProctorStudentAssignment;

@Transactional
@Repository
public interface ProctorStudentAssignmentRepository extends JpaRepository<ProctorStudentAssignment, Integer> {

	@Query(value = "Select p from ProctorStudentAssignment p where p.active=true")
	public  List<ProctorStudentAssignment> findAll1();
	
	@Modifying
	@Query(value = "Update ProctorStudentAssignment psa set psa.active=false where psa.proctor_assign_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "Update ProctorStudentAssignment psa set psa.active=true where psa.proctor_assign_id=?1")
	public void update1(Integer id);

	@Query(value = "Select p from ProctorStudentAssignment p where p.proctor_assign_id=?1")
	public ProctorStudentAssignment findByproctor_assign_id(Integer proctor_assign_id);

//	@Query(value = "select new map(p.proctor_assign_id as proctor_assign_id,sd.student_name as student_name,"
//			+ "ed.employee_name as employee_name,ed.empcode as empcode,sd.student_email as student_email,sd.usn as usn,"
//			+ "sd.auid as auid,p.created_date as created_date,p.created_by as created_by,p.student_id as student_id,"
//			+ "p.created_username as created_username,p.active as active,p.school_id as school_id,"
//			+ "p.proctor_status as proctor_status,p.proctor_id as proctor_id)from ProctorStudentAssignment p "
//			+ "left join EmployeeDetails ed on p.proctor_id=ed.emp_id "
//			+ "left join Student_Details sd on p.student_id=sd.student_id")
//	public List<HashMap<String, Object>> findAlll();
	
	@Query(value = "Select new map(p.proctor_assign_id as id,sd.student_name as student_name,sd.acharya_email as acharya_email,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,sd.student_email as student_email,sd.usn as usn,cw.application_no_npf as application_no_npf,"
			+ "sd.auid as auid,p.created_date as created_date,p.created_by as created_by,p.student_id as student_id,"
			+ "ua.id As userId,ua.username As username,sc.school_name_short As school_name_short,sc.school_name As school_name,"
			+ "p.created_username as created_username,p.active as active,p.school_id as school_id,ph.chief_proctor_id As chief_proctor_id,"
			+ "p.proctor_status as proctor_status,p.emp_id as emp_id,sd.proctor_assign_status as proctor_assign_status) from ProctorStudentAssignment p "
			+ "left join EmployeeDetails ed on p.emp_id=ed.emp_id "
			+ "left join Schools sc on sc.school_id=p.school_id "
			+ "left join UserAuthentication ua on ua.email=ed.email "
			+ "left join ProctorHead ph on ph.emp_id=p.emp_id "
			+ "left join Student_Details sd on p.student_id=sd.student_id "
			+ "left join Candidate_Walkin cw on cw.candidate_id=sd.candidate_id "
			+ "where (:userId IS NULL OR ua.id = :userId) "
			+ "AND (:school_id IS NULL OR p.school_id = :school_id) "
			+ "AND CONCAT(IfNull(p.proctor_assign_id,''),'',IfNull(sd.student_name,''),'',IfNull(ed.employee_name,''),'',IfNull(ed.empcode,''),"
			+ "'',IfNull(sd.student_email,''),'',IfNull(sd.usn,''),'',IfNull(sd.auid,''),'',IfNull(p.proctor_status,''),"
			+ "'',IfNull(p.emp_id,''),'',IfNull(p.school_id,''),'',IfNull(p.student_id,''),'',IfNull(p.created_by,''),"
			+ "'',IfNull(p.created_date,'')) LIKE %:keyword% and p.proctor_status IS NOT NULL")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer userId, Integer school_id); 
	
	@Query(value = "Select new map(p.proctor_assign_id as id,sd.student_name as student_name,sd.acharya_email as acharya_email,"
			+ "ed.employee_name as employee_name,ed.empcode as empcode,sd.student_email as student_email,sd.usn as usn,"
			+ "sd.auid as auid,p.created_date as created_date,p.created_by as created_by,p.student_id as student_id,"
			+ "ua.id As userId,ua.username As username,sc.school_name_short As school_name_short,sc.school_name As school_name,"
			+ "p.created_username as created_username,p.active as active,p.school_id as school_id,ph.chief_proctor_id As chief_proctor_id,"
			+ "p.proctor_status as proctor_status,p.emp_id as emp_id,sd.proctor_assign_status as proctor_assign_status) from ProctorStudentAssignment p "
			+ "left join EmployeeDetails ed on p.emp_id=ed.emp_id "
			+ "left join Schools sc on sc.school_id=p.school_id "
			+ "left join UserAuthentication ua on ua.email=ed.email "
			+ "left join ProctorHead ph on ph.emp_id=p.emp_id "
			+ "left join Student_Details sd on p.student_id=sd.student_id "
			+ "left join Candidate_Walkin cw on cw.candidate_id=sd.candidate_id "
			+ "where (:userId IS NULL OR ua.id = :userId) "
			+ "AND (:school_id IS NULL OR sc.school_id = :school_id) "
			+ "And p.proctor_status IS NOT NULL")
	public Page<Object> getAllSortedData(Pageable pageable, Integer userId, Integer school_id);

	@Query(value = "Select new map(psa.student_id as student_id,s.school_name_short as school_name_short,sd.acharya_email as acharya_email,"
			+ "sd.school_id as school_id,sd.ac_year_id as ac_year_id,sd.program_id as program_id,"
			+ "sd.program_specialization_id as program_specialization_id,sd.auid as auid,sd.usn as usn,"
			+ "sd.student_name as student_name,p.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ac.ac_year as ac_year) "
			+ "from ProctorStudentAssignment psa "
			+ "left join Student_Details sd on psa.student_id=sd.student_id "
			+ "left join Schools s on sd.school_id=s.school_id "
			+ "left join Program p on sd.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on sd.program_specialization_id=ps.program_specialization_id "
			+ "left join Academic_year ac on sd.ac_year_id=ac.ac_year_id "
			+ "where psa.emp_id=?1 and psa.active=true")
	public List<HashMap<String, Object>> getAllList(Integer emp_id);

	@Modifying
	@Query(value = "Update ProctorStudentAssignment psa set psa.proctor_status=null where psa.proctor_assign_id=?1 and psa.active=true")
	public void updateProctorStatus(List<Integer> proctor_assign_id);
	
	@Query(value = "Select new map(psa.student_id as student_id,s.school_name_short as school_name_short,sd.acharya_email as acharya_email,"
			+ "sd.school_id as school_id,sd.ac_year_id as ac_year_id,sd.program_id as program_id,"
			+ "sd.program_specialization_id as program_specialization_id,sd.auid as auid,sd.usn as usn,"
			+ "sd.student_name as student_name,p.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ac.ac_year as ac_year) "
			+ "from ProctorStudentAssignment psa "
			+ "left join Student_Details sd on psa.student_id=sd.student_id "
			+ "left join Schools s on sd.school_id=s.school_id "
			+ "left join Program p on sd.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on sd.program_specialization_id=ps.program_specialization_id "
			+ "left join Academic_year ac on sd.ac_year_id=ac.ac_year_id "
			+ "where psa.emp_id=?1 and psa.proctor_status=1 and psa.active=true")
	public List<HashMap<String, Object>> getProctorStatusAssignedStudentDetailsList(Integer emp_id);
	
	@Query(value = "Select new map(psa.student_id as student_id,s.school_name_short as school_name_short,sd.acharya_email as acharya_email,"
			+ "sd.school_id as school_id,sd.ac_year_id as ac_year_id,sd.program_id as program_id,sd.mobile As studentMobile,"
			+ "sd.father_mobile As father_mobile,sd.mother_mobile As mother_mobile,rs.reporting_id As reporting_id,"
			+ "rs.current_sem As current_sem,rs.current_year As current_year,rs.reporting_date As reporting_date,"
			+ "sd.father_name As fatherName,sd.mother_name As motherName,"
			+ "SUM(CASE WHEN ivr.duration >= 30 THEN 1 ELSE 0 END) AS callCount,"
			+ "sd.program_specialization_id as program_specialization_id,sd.auid as auid,sd.usn as usn,cw.application_no_npf as application_no_npf,"
			+ "sd.student_name as student_name,p.program_short_name as program_short_name,psa.proctor_assign_id as proctor_assign_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ac.ac_year as ac_year,"
			+ "ed.employee_name as employee_name,psa.created_date as created_date,psa.created_by as created_by,"
			+ "ed.empcode as empcode,sd.student_email as student_email,psa.created_username as created_username,psa.active as active,"
			+ "psa.proctor_status as proctor_status,psa.emp_id as emp_id,sd.proctor_assign_status as proctor_assign_status) "
			+ "from ProctorStudentAssignment psa "
			+ "left join Student_Details sd on psa.student_id=sd.student_id "
			+ "left join  IvrCreation ivr on psa.student_id=ivr.student_id And psa.active=true "
			+ "left join ReportingStudents rs on rs.student_id=sd.student_id "
			+ "left join Candidate_Walkin cw on cw.candidate_id=sd.candidate_id "
			+ "left join Schools s on sd.school_id=s.school_id "
			+ "left join Program p on sd.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on sd.program_specialization_id=ps.program_specialization_id "
			+ "left join Academic_year ac on sd.ac_year_id=ac.ac_year_id "
			+ "left join EmployeeDetails ed on psa.emp_id = ed.emp_id "
			+ "where psa.emp_id=?1 and psa.proctor_status=1 and psa.active=true group by psa.student_id")
	public List<HashMap<String, Object>> getProctorStatusAssignedStudentDetailsListByUserId(Integer emp_id);
	
	@Query(value = "Select psa.student_id as student_id, "
			+ "SUM(CASE WHEN ivr.duration >= 30 THEN 1 ELSE 0 END) AS callCount,"
			+ "s.school_name_short as school_name_short,sd.acharya_email as acharya_email, "
			+ "sd.school_id as school_id,sd.ac_year_id as ac_year_id,sd.program_id as program_id,sd.mobile As studentMobile, "
			+ "sd.father_mobile As father_mobile,sd.mother_mobile As mother_mobile,rs.reporting_id As reporting_id, "
			+ "rs.current_sem As current_sem,rs.current_year As current_year,rs.reporting_date As reporting_date, "
			+ "sd.program_specialization_id as program_specialization_id,sd.auid as auid,sd.usn as usn,cw.application_no_npf as application_no_npf, "
			+ "sd.student_name as student_name,p.program_short_name as program_short_name,psa.proctor_assign_id as proctor_assign_id, "
			+ "ps.program_specialization_short_name as program_specialization_short_name,ac.ac_year as ac_year, "
			+ "ed.employee_name as employee_name,psa.created_date as created_date,psa.created_by as created_by, "
			+ "ed.empcode as empcode,sd.student_email as student_email,psa.created_username as created_username,psa.active as active, "
			+ "psa.proctor_status as proctor_status,psa.emp_id as emp_id,sd.proctor_assign_status as proctor_assign_status "
			+ "from  proctor_student_assignment psa  "
			+ "left join  student_details sd on psa.student_id=sd.student_id  "
			+ "left join  ivr_creation ivr on psa.student_id=ivr.student_id And psa.active=true "
			+ "left join  reporting_students rs on rs.student_id=sd.student_id  "
			+ "left join  candidate_walkin cw on cw.candidate_id=sd.candidate_id  "
			+ "left join  schools s on sd.school_id=s.school_id  "
			+ "left join  program p on sd.program_id=p.program_id  "
			+ "left join  program_specialization ps on sd.program_specialization_id=ps.program_specialization_id  "
			+ "left join  academic_year ac on sd.ac_year_id=ac.ac_year_id  "
			+ "left join  employee_details ed on psa.emp_id = ed.emp_id  "
			+ "where psa.emp_id=?1 and psa.active=true group by psa.student_id ",nativeQuery=true)
	public List<Map<String, Object>> getProctorStatusAssignedStudentsByUserId(Integer emp_id);
	
	@Query(value = "Select new map(p.proctor_assign_id as proctor_assign_id,ed.employee_name as employee_name,ed.empcode as empcode) from ProctorStudentAssignment p "
			+ "left join EmployeeDetails ed on p.emp_id=ed.emp_id Where p.student_id=?1 And p.active=true")
	public List<HashMap<String, Object>> getAssignedProctorDetailsByStudentId(Integer student_id);
	
	@Modifying
	@Query(value = "Update ProctorStudentAssignment psa set psa.proctor_status=null where psa.proctor_assign_id=?1")
	public void updateProctorStatus(Integer proctor_assign_id);

	@Query(value = "Select p from ProctorStudentAssignment p where p.student_id IN ?1 and p.active=true")
	public List<ProctorStudentAssignment> getProctorData(Set<Integer> studentIds);
	
	@Query(value = "Select new map(psa.student_id as student_id,s.school_name_short as school_name_short,sd.acharya_email as acharya_email,"
			+ "sd.school_id as school_id,sd.ac_year_id as ac_year_id,sd.program_id as program_id,"
			+ "sd.program_specialization_id as course_branch_assignment_id,sd.auid as auid,sd.usn as usn,"
			+ "sd.student_name as student_name,p.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ac.ac_year as ac_year,sd.date_of_admission as date_of_admission,"
			+ "rp.current_year as current_year,rp.current_sem as current_sem) from ProctorStudentAssignment psa "
			+ "left join Student_Details sd on psa.student_id=sd.student_id "
			+ "left join Schools s on sd.school_id=s.school_id "
			+ "left join Program p on sd.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on sd.program_specialization_id=ps.program_specialization_id "
			+ "left join Academic_year ac on sd.ac_year_id=ac.ac_year_id "
			+ "left join ReportingStudents rp on rp.student_id=psa.student_id And rp.active=true "
			+ "where psa.emp_id=?1 and psa.active=true")
	public List<HashMap<String, Object>> studentDetailsAssignedToProctor(Integer emp_id);

	
	@Query(value ="SELECT count(psa.student_id) As studentCount,ed.emp_id As emp_id,ed.employee_name As employee_name  "
			+ "FROM proctor_student_assignment psa "
			+ "left join employee_details ed on psa.emp_id = ed.emp_id "
			+ "left join user_details ua on ua.email = ed.email "
			+ "where ua.id=?1 And psa.active=true And ua.active=true",nativeQuery=true)
	public Map<String, Object> getCountOfStudentBasedOnUserId(Integer user_id);

	
	@Query(value ="SELECT count(psa.student_id) As studentCount,ed.emp_id As emp_id,ed.employee_name As employee_name, "
			  + "count(CASE WHEN sd.candidate_sex = 'Male' THEN 1 END) AS maleStudentCount, "
		        + "count(CASE WHEN sd.candidate_sex = 'Female' THEN 1 END) AS femaleStudentCount "
			+ "FROM proctor_student_assignment psa "
			+ "left join Student_Details sd on psa.student_id=sd.student_id "
			+ "left join employee_details ed on psa.emp_id = ed.emp_id "
			+ "where ed.emp_id=?1 And psa.active=true And ed.active=true",nativeQuery=true)
	public Map<String, Object> getCountOfStudentBasedOnEmpId(Integer emp_id);

	@Query(value = "Select p.emp_id from proctor_student_assignment p where p.student_id=:studetId And p.active=true",nativeQuery=true)
	public Integer getProctorId(Integer studetId);

	@Query(value = "Select new map(p.proctor_assign_id as proctor_assign_id,ed.employee_name as employee_name,ed.empcode as empcode,p.student_id as student_id) from ProctorStudentAssignment p "
			+ "left join EmployeeDetails ed on p.emp_id=ed.emp_id Where p.student_id in (?1) And p.active=true")
    List<HashMap<String, Object>> getAssignedProctorDetailsByStudentIds(List<Integer> convertCommaSeperatedIdsAsList);
}
