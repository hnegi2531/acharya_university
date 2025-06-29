package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Publications;
import com.au.model.StudentNoDue;

@Repository
@Transactional
public interface StudentNoDueRepository extends JpaRepository<StudentNoDue, Integer>{

	@Query(value = "SELECT snd from StudentNoDue snd where snd.active=true")
	public List<StudentNoDue> getAllActiveStudentNoDue();
	
	
	@Modifying
	@Query(value = "update StudentNoDue snd set snd.active=false where snd.student_no_due_id=?1")
	public void deactivate(Integer id);

	@Modifying
	@Query(value = "update StudentNoDue snd set snd.active=true where snd.student_no_due_id=?1")
	public void activate(Integer id);
	
	
	
	@Query(value = "select new map(snd.student_no_due_id as id,snd.student_id as student_id,snd.dept_id as dept_id,snd.date as date,"
			+ "snd.status as status,snd.attachment_path as attachment_path,snd.attachment_name as attachment_name,snd.comment As comment,"
			+ "sd.student_name As student_name,sd.usn As usn,sd.acharya_email As acharya_email,sd.auid As auid,snd.not_applicable_status As not_applicable_status,"
			+ "sd.program_specialization_id As program_specialization_id,sd.program_assignment_id As program_assignment_id,sd.program_id As program_id,"
			+ "sd.fee_admission_category_id As fee_admission_category_id,de.dept_name As dept_name,de.dept_name_short As dept_name_short,"
			+ "de.no_dues_status As no_dues_status,rs.reporting_id As reporting_id,rs.eligible_reported_status As eligible_reported_status,"
			+ "rs.current_sem As current_sem,rs.current_year As current_year,ps.program_specialization_name As program_specialization_name,"
			+ "ps.program_specialization_short_name As program_specialization_short_name,p.program_name As program_name,p.program_short_name As program_short_name,"
			+ "snd.created_username as created_username,snd.modified_username as modified_username,snd.active as active,"
			+ "snd.created_date as created_date,snd.modified_date as modified_date,snd.created_by as created_by,"
			+ "fac.fee_admission_category_id As fee_admission_category_id,fac.fee_admission_category_type As fee_admission_category_type,fac.fee_admission_category_short_name As fee_admission_category_short_name,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "snd.modified_by as modified_by) from StudentNoDue snd "
			+ "left join Student_Details sd on sd.student_id=snd.student_id "
			+ "left join ReportingStudents rs on rs.student_id=snd.student_id "
			+ "left join ProgramSpecilization ps on ps.program_specialization_id=sd.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on fac.fee_admission_category_id=sd.fee_admission_category_id "
			+ "left join FeeAdmissionSubCategory fasc on fac.fee_admission_category_id=fasc.fee_admission_category_id "
			+ "left join Program p on p.program_id=sd.program_id "
			+ "left join Department de on de.dept_id=snd.dept_id "
			+ "Where sd.school_id=?2 and sd.program_id=?3 and sd.program_specialization_id=?4 and rs.current_sem=?5 And sd.active=true And "
			+ "CONCAT(IfNull(sd.auid,''),'',IfNull(p.program_short_name,''),"
			+ "'',IfNull(ps.program_specialization_short_name,''),'',IfNull(sd.student_name,''),'',IfNull(de.dept_name_short,''),'') LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Integer school_id, Integer program_id,Integer program_specialization_id, Integer current_sem, Object keyword);
	
	
	@Query(value = "select new map(snd.student_no_due_id as id,snd.student_id as student_id,snd.dept_id as dept_id,snd.date as date,"
			+ "snd.status as status,snd.attachment_path as attachment_path,snd.attachment_name as attachment_name,snd.comment As comment,"
			+ "sd.student_name As student_name,sd.usn As usn,sd.acharya_email As acharya_email,sd.auid As auid,snd.not_applicable_status As not_applicable_status,"
			+ "sd.program_specialization_id As program_specialization_id,sd.program_assignment_id As program_assignment_id,sd.program_id As program_id,"
			+ "sd.fee_admission_category_id As fee_admission_category_id,de.dept_name As dept_name,de.dept_name_short As dept_name_short,"
			+ "de.no_dues_status As no_dues_status,rs.reporting_id As reporting_id,rs.eligible_reported_status As eligible_reported_status,"
			+ "rs.current_sem As current_sem,rs.current_year As current_year,ps.program_specialization_name As program_specialization_name,"
			+ "ps.program_specialization_short_name As program_specialization_short_name,p.program_name As program_name,p.program_short_name As program_short_name,"
			+ "snd.created_username as created_username,snd.modified_username as modified_username,snd.active as active,"
			+ "snd.created_date as created_date,snd.modified_date as modified_date,snd.created_by as created_by,"
			+ "fac.fee_admission_category_id As fee_admission_category_id,fac.fee_admission_category_type As fee_admission_category_type,fac.fee_admission_category_short_name As fee_admission_category_short_name,"
			+ "fasc.fee_admission_sub_category_id As fee_admission_sub_category_id,fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,"
			+ "fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "snd.modified_by as modified_by) from StudentNoDue snd "
			+ "left join Student_Details sd on sd.student_id=snd.student_id "
			+ "left join ReportingStudents rs on rs.student_id=snd.student_id "
			+ "left join ProgramSpecilization ps on ps.program_specialization_id=sd.program_specialization_id "
			+ "left join FeeAdmissionCategory fac on fac.fee_admission_category_id=sd.fee_admission_category_id "
			+ "left join FeeAdmissionSubCategory fasc on fac.fee_admission_category_id=fasc.fee_admission_category_id "
			+ "left join Program p on p.program_id=sd.program_id "
			+ "left join Department de on de.dept_id=snd.dept_id "
			+ "Where sd.school_id=?1 and sd.program_id=?2 and sd.program_specialization_id=?3 "
			+ "and rs.current_sem=?4 And sd.active=true")
	public Page<Object> getAllSortedData(Pageable pageable, Integer school_id, Integer program_id, Integer program_specialization_id, Integer current_sem);

	@Query(value = "SELECT snd.student_no_due_id from StudentNoDue snd where snd.student_id=?1 And snd.active=true")
	public List<Integer> getStudentNoDueId(Integer student_id);



}
