package com.au.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ChangeOfCourseProgramAttachment;


@Repository
@Transactional
public interface ChangeOfCourseProgramRepository extends JpaRepository<ChangeOfCourseProgramAttachment, Integer> {
	
	
	@Query(value = "select ccpa from ChangeOfCourseProgramAttachment ccpa where ccpa.newStudentId=?1 and ccpa.active=true")
	public ChangeOfCourseProgramAttachment changeOfCourseProgramAttachmentDetail(Integer studentId);
	
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
	        + "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
	        + "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.old_student_id as old_student_id,"
	        + "s.course_approver_status as course_approver_status,osd.auid as old_auid,osd.program_specialization_id as old_program_specialization_id,osd.school_id as old_school_id) "
	        + "FROM Student_Details s "
	        + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
	        + "LEFT JOIN Program pr ON s.program_id = pr.program_id "
	        + "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
	        + "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
	        + "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
	        + "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
	        + "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
	        + "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
	        + "LEFT JOIN City c ON c.id = s.permanant_city "
	        + "LEFT JOIN State st ON st.id = s.permanant_state "
	        + "LEFT JOIN Country con ON con.id = s.permanant_country "
	        + "LEFT JOIN City cc ON cc.id = s.current_city "
	        + "LEFT JOIN State stc ON stc.id = s.current_state "
	        + "LEFT JOIN Country conc ON conc.id = s.current_country "
	        + "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
	        + "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
	        + "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
	        + "LEFT JOIN Student_Details osd ON osd.student_id=s.old_student_id And osd.course_approver_status=2 "
	        + "WHERE CONCAT(IFNULL(sc.school_name_short, ''), IFNULL(s.auid, ''), IFNULL(pr.program_short_name, ''), IFNULL(ps.program_specialization_short_name, ''), "
	        + "IFNULL(s.student_name, ''), IFNULL(s.date_of_admission, ''), IFNULL(fac.fee_admission_category_short_name, ''), IFNULL(s.created_by, '')) LIKE %?1% And s.old_student_id is not null")
	public Page<Object> initiatedChangeOfCourseProgramStudentDetailsSerach(Pageable pageable, Object keyword);
	
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
	        + "s.permanent_address as permanent_address, con.name as permanent_country,s.deassign_status as deassign_status, "
	        + "c.name as permanent_city, st.name as permanent_state, s.current_address as current_address, cc.name as current_city, "
	        + "stc.name as current_state, conc.name as current_country, s.current_pincode as current_pincode, std.totalFix as fixed_fee, std.totalPaid as fee_paid, "
	        + "ss.approved_amount as grant,cw.application_no_npf as application_no_npf,s.old_student_id as old_student_id,"
	        + "s.course_approver_status as course_approver_status,osd.auid as old_auid,osd.program_specialization_id as old_program_specialization_id,osd.school_id as old_school_id) "
	        + "FROM Student_Details s "
	        + "LEFT JOIN Schools sc ON s.school_id = sc.school_id "
	        + "LEFT JOIN Program pr ON s.program_id = pr.program_id "
	        + "LEFT JOIN FeeTemplate ft ON s.fee_template_id = ft.fee_template_id "
	        + "LEFT JOIN FeeAdmissionCategory fac ON s.fee_admission_category_id = fac.fee_admission_category_id "
	        + "LEFT JOIN Academic_year ay ON s.ac_year_id = ay.ac_year_id "
	        + "LEFT JOIN ReportingStudents rs ON s.student_id = rs.student_id "
	        + "LEFT JOIN ProgramSpecilization ps ON s.program_specialization_id = ps.program_specialization_id "
	        + "LEFT JOIN StudentDues std ON std.studentId = s.student_id "
	        + "LEFT JOIN City c ON c.id = s.permanant_city "
	        + "LEFT JOIN State st ON st.id = s.permanant_state "
	        + "LEFT JOIN Country con ON con.id = s.permanant_country "
	        + "LEFT JOIN City cc ON cc.id = s.current_city "
	        + "LEFT JOIN State stc ON stc.id = s.current_state "
	        + "LEFT JOIN Country conc ON conc.id = s.current_country "
	        + "LEFT JOIN ScholarshipApprovalStatus ss ON ss.student_id = s.student_id "
	        + "LEFT JOIN UserAuthentication ua ON s.created_by = ua.id "
	        + "LEFT JOIN Candidate_Walkin cw ON cw.candidate_id = s.candidate_id And s.active=true "
	        + "LEFT JOIN Student_Details osd ON osd.student_id=s.old_student_id And osd.course_approver_status=2 Where s.old_student_id is not null ")
	public Page<Object> initiatedChangeOfCourseProgramStudentDetailsSorting(Pageable pageable);

	public ChangeOfCourseProgramAttachment findByNewStudentIdAndActiveTrue(Integer newStudentId);
	
	@Modifying
	@Query(value = "Update ChangeOfCourseProgramAttachment ccpa Set ccpa.approvalStatus=true Where ccpa.newStudentId=:newStudentId And ccpa.active=true")
	public void updateApprovalStatusByNewStudentId(Integer newStudentId);
}
