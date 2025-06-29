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

import com.au.model.Scholarship;

@Repository
@Transactional
public interface ScholarshipRepository extends JpaRepository<Scholarship	, Integer>{

	
	@Query(value = "SELECT * FROM scholarship where candidate_id=?1 and active=true",nativeQuery = true)
	public Scholarship getScholarshipByCid(Integer cid);

/*	
	@Query(value = "SELECT new map(sas.created_username as created_username,sas.year1_amount as year1_amount,sas.year2_amount as year2_amount,sas.year3_amount as year3_amount,sas.year4_amount as year4_amount,sas.year5_amount as year5_amount,sas.year6_amount as year6_amount,s.requested_scholarship as requested_scholarship,s.scholarship_id as scholarship_id,s.student_id as student_id,s.active as active,s.award as award,s.award_details as award_details,s.candidate_id as candidate_id,s.created_by as created_by,s.created_date as created_date,s.created_username as created_username,s.exemption_received as exemption_received,s.exemption_type as exemption_type,s.modified_by as modified_by,s.modified_date as modified_date,s.modified_username as modified_username,s.parent_income as parent_income,s.reason as reason,s.residence as residence,s.occupation as occupation,sas.is_approved as is_approved,sas.is_verified as is_verified,sas.verified_by as verified_by,sas.verified_amount as verified_amount) FROM Scholarship s left join ScholarshipApprovalStatus sas on s.candidate_id = sas.candidate_id  where s.candidate_id=?1")
	public List<HashMap<String, Object>> get1(Integer cid);
*/
	
	@Query(value = "SELECT new map(sas.is_approved as is_approved,sas.approval as approval,sas.scholarship_approved_status_id as scholarship_approved_status_id,"
			+ "sas.created_username as created_username,sas.year1_amount as year1_amount,sas.year2_amount as year2_amount,"
			+ "sas.year3_amount as year3_amount,sas.year4_amount as year4_amount,sas.prev_approved_amount as prev_approved_amount,"
			+ "sas.year5_amount as year5_amount,sas.year6_amount as year6_amount,sas.year7_amount as year7_amount,"
			+ "sas.year8_amount as year8_amount,sas.year9_amount as year9_amount,sas.year10_amount as year10_amount,"
			+ "sas.year11_amount as year11_amount,sas.year12_amount as year12_amount,sas.verifier_remarks as verifier_remarks,"
			+ "s.requested_scholarship as requested_scholarship,s.scholarship_id as scholarship_id,"
			+ "s.student_id as student_id,s.active as active,s.award as award,"
			+ "s.award_details as award_details,s.candidate_id as candidate_id,s.created_by as created_by,"
			+ "s.created_date as created_date,s.created_username as created_username,"
			+ "s.exemption_received as exemption_received,s.exemption_type as exemption_type,s.modified_by as modified_by,"
			+ "s.modified_date as modified_date,s.modified_username as modified_username,s.parent_income as parent_income,"
			+ "s.reason as reason,s.residence as residence,s.occupation as occupation,"
			+ "sas.is_approved as is_approved,sas.is_verified as is_verified,sas.verified_by as verified_by,"
			+ "sas.verified_amount as verified_amount,sas.approved_amount as approved_amount,"
			+ "sas.requestedByRemarks as requestedByRemarks,sas.ipAddress as ipAddress,"
			+ "ua.username as verifierName,ua1.username as approversName,sas.verified_date as verified_date,"
			+ "sas.approved_date as approved_date,sas.verifier_remarks as verifier_remarks,sas.comments as approversRemarks) "
			+ "FROM Scholarship s "
			+ "left join ScholarshipApprovalStatus sas on s.scholarship_id = sas.scholarship_id  "
			+ "left join UserAuthentication ua on ua.id=sas.verified_by "
			+ "left join UserAuthentication ua1 on ua1.id=sas.approved_by "
			+ "where s.scholarship_id=?1")
	public List<HashMap<String, Object>> get1(Integer sid);

	

	@Query(value="SELECT new map(sas.comments as comments,sd.auid as auid,sas.cancel_remarks  as cancel_remarks,s.created_username as created_username,"
			+ "sas.prev_approved_amount as prev_approved_amount,sas.approved_by as approved_by,sas.approved_amount as approved_amount,"
			+ "p.candidate_id as candidate_id,s.scholarship_id as scholarship_id ,sas.is_approved as is_approved,p.student_name as student_name,"
			+ "p.fee_template_id as fee_template_id,sas.created_date as created_date,ft.fee_template_name as fee_template_name,"
			+ "sas.counselor_id as counselor_id,s.requested_scholarship as requested_scholarship) FROM ScholarshipApprovalStatus sas "
			+ "left join PreAdmissionProcess p on sas.candidate_id=p.candidate_id "
			+ "left join FeeTemplate ft on ft.fee_template_id=p.fee_template_id "
			+ "left join Scholarship s on s.candidate_id=sas.candidate_id "
			+ "left join Student_Details sd on  s.candidate_id=sd.candidate_id ")
	public List<HashMap<String, Object>> fetchScholarship();

	
	@Modifying
	@Query(value = "update Scholarship sc set sc.active=false where sc.scholarship_id=?1")
	public void updateScholarship(Integer id);
	
	
	
	
//	@Query(value = "SELECT new map(sd.auid as auid,sas.verified_date as verified_date,sas.verified_amount as verified_amount,s.scholarship_id as scholarship_id,s.candidate_id as candidate_id ,"
//			+ "sc.school_name_short as school_name_short,sas.is_verified as is_verified,sas.counselor_id as counselor_id,"
//			+ "s.created_username as created_username,s.created_date as created_date,ua.username as username,"
//			+ "s.requested_scholarship as requested_scholarship ,p.student_name as student_name,"
//			+ "p.school_id as school_id,sa.scholarship_attachment_id as scholarship_attachment_id)"
//			+ "from Scholarship s left join ScholarshipApprovalStatus sas on s.scholarship_id=sas.scholarship_id "
//			+ "left join PreAdmissionProcess p on p.candidate_id=s.candidate_id "
//			+ "left join UserAuthentication ua on ua.id=sas.counselor_id "
//			+ "left join ScholarshipAttachment sa  on s.candidate_id= sa.candidate_id "
//			+ "left join Schools sc on p.school_id=sc.school_id "
//			+ "left join Student_Details sd on s.candidate_id=sd.candidate_id where  sas.is_approved='yes' ")
//	public List<HashMap<String, Object>> get3();
	
	@Query(value ="SELECT new map(sas.scholarship_approved_status_id as id,sd.auid as auid,sd.student_name as student_name,"
			+ "sas.prev_approved_amount as prev_approved_amount,s.scholarship_id as scholarship_id,s.candidate_id as candidate_id,sd.student_id as student_id,"
			+ "sc.school_name_short as school_name_short,sas.is_approved as is_approved,sas.is_verified as is_verified,cw.username as username,"
			+ "s.created_username as created_username,s.created_date as created_date,ua.username as counselorUsername,cw.npf_status as npf_status,"
			+ "s.requested_scholarship as requested_scholarship ,cw.candidate_name as candidate_name,cw.mobile_number as mobie_number,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.application_no_npf as application_no_npf,"
			+ "sas.pre_approval_date as pre_approval_date,sas.verified_by as verified_by,sas.approved_by as approved_by,"
			+ "sas.verified_date as verified_date,uaa.username as verifiedName,s.reason as reason,"
			+ "sa.scholarship_attachment_path as scholarship_attachment_path,sa.scholarship_attachment_file_name as scholarship_attachment_file_name,"
			+ "sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "p.school_id as school_id,sa.scholarship_attachment_id as scholarship_attachment_id) "
			+ "from Scholarship s left join ScholarshipApprovalStatus sas on s.scholarship_id=sas.scholarship_id "
			+ "left join PreAdmissionProcess p on p.candidate_id=s.candidate_id and p.active=true "
			+ "left join Candidate_Walkin cw on s.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on ua.id=sas.counselor_id "
			+ "left join UserAuthentication uaa on uaa.id=sas.verified_by "
			+ "left join ScholarshipAttachment sa  on s.candidate_id= sa.candidate_id and sa.active=true "
			+ "left join Schools sc on p.school_id=sc.school_id "
			+ "left join Student_Details sd on s.candidate_id=sd.candidate_id and sd.active=true "
			+ "where CONCAT(IfNull(cw.created_date,''),'',IfNull(cw.candidate_name,''),'',"
			+ "IfNull(ps.program_specialization_short_name,''),'',IfNull(cw.candidate_id,''),'',"
			+ "IfNull(pr.program_short_name,''),'',IfNull(sc.school_name_short,'')) LIKE %?1% and (sd.active = true or sd.active is null) And   sas.is_verified is null And sas.scholarship_approved_status_id is not null group by sas.scholarship_approved_status_id")
	public Page<Object> findAllApprovedData(Pageable pageable, Object keyword);
	
	@Query(value ="SELECT new map(sas.scholarship_approved_status_id as id,sd.auid as auid,sd.student_name as student_name,"
			+ "sas.prev_approved_amount as prev_approved_amount,s.scholarship_id as scholarship_id,s.candidate_id as candidate_id,sd.student_id as student_id,"
			+ "sc.school_name_short as school_name_short,sas.is_approved as is_approved,sas.is_verified as is_verified,cw.username as username,"
			+ "s.created_username as created_username,s.created_date as created_date,ua.username as counselorUsername,cw.npf_status as npf_status,"
			+ "s.requested_scholarship as requested_scholarship ,cw.candidate_name as candidate_name,cw.mobile_number as mobie_number,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.application_no_npf as application_no_npf,"
			+ "sas.pre_approval_date as pre_approval_date,sas.verified_by as verified_by,sas.approved_by as approved_by,"
			+ "sas.verified_date as verified_date,uaa.username as verifiedName,s.reason as reason,"
			+ "sa.scholarship_attachment_path as scholarship_attachment_path,sa.scholarship_attachment_file_name as scholarship_attachment_file_name,"
			+ "sas.requestedByRemarks as requestedByRemarks,sas.verifier_remarks as verifier_remarks,"
			+ "sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "p.school_id as school_id,sa.scholarship_attachment_id as scholarship_attachment_id) "
			+ "from Scholarship s left join ScholarshipApprovalStatus sas on s.scholarship_id=sas.scholarship_id "
			+ "left join PreAdmissionProcess p on p.candidate_id=s.candidate_id and p.active=true "
			+ "left join Candidate_Walkin cw on s.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on ua.id=sas.counselor_id "
			+ "left join UserAuthentication uaa on uaa.id=sas.verified_by "
			+ "left join ScholarshipAttachment sa  on s.candidate_id= sa.candidate_id and sa.active=true "
			+ "left join Schools sc on p.school_id=sc.school_id "
			+ "left join Student_Details sd on s.candidate_id=sd.candidate_id and sd.active=true "
			+ "where (sd.active = true or sd.active is null) And   sas.is_verified is null And sas.scholarship_approved_status_id is not null group by sas.scholarship_approved_status_id")
	public Page<Object> findAllApprovedData1(Pageable pageable);
	
	
	@Query(value ="SELECT new map(sas.scholarship_approved_status_id as id,sd.auid as auid,sd.student_name as student_name,"
			+ "sas.prev_approved_amount as prev_approved_amount,s.scholarship_id as scholarship_id,s.candidate_id as candidate_id,sd.student_id as student_id,"
			+ "sc.school_name_short as school_name_short,sas.is_approved as is_approved,sas.is_verified as is_verified,cw.username as username,"
			+ "s.created_username as created_username,s.created_date as created_date,ua.username as counselorUsername,cw.npf_status as npf_status,"
			+ "s.requested_scholarship as requested_scholarship ,cw.candidate_name as candidate_name,cw.mobile_number as mobie_number,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.application_no_npf as application_no_npf,"
			+ "sas.pre_approval_date as pre_approval_date,sas.verified_by as verified_by,sas.approved_by as approved_by,"
			+ "sas.verified_date as verified_date,uaa.username as verifiedName,"
			+ "sa.scholarship_attachment_path as scholarship_attachment_path,sa.scholarship_attachment_file_name as scholarship_attachment_file_name,"
			+ "sas.requestedByRemarks as requestedByRemarks,sas.verifier_remarks as verifier_remarks,"
			+ "sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "p.school_id as school_id,sa.scholarship_attachment_id as scholarship_attachment_id) "
			+ "from Scholarship s left join ScholarshipApprovalStatus sas on s.scholarship_id=sas.scholarship_id "
			+ "left join PreAdmissionProcess p on p.candidate_id=s.candidate_id "
			+ "left join Candidate_Walkin cw on s.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on ua.id=sas.counselor_id "
			+ "left join UserAuthentication uaa on uaa.id=sas.verified_by "
			+ "left join ScholarshipAttachment sa  on s.candidate_id= sa.candidate_id "
			+ "left join Schools sc on p.school_id=sc.school_id "
			+ "left join Student_Details sd on s.candidate_id=sd.candidate_id "
			+ "where CONCAT(IfNull(cw.created_date,''),'',IfNull(cw.candidate_name,''),'',"
			+ "IfNull(ps.program_specialization_short_name,''),'',IfNull(cw.candidate_id,''),'',"
			+ "IfNull(pr.program_short_name,''),'',IfNull(sc.school_name_short,'')) LIKE %?1% and sas.is_verified Like '%yes%'")
	public Page<Object> findAllApprovedData4(Pageable pageable, Object keyword);
	
	@Query(value ="SELECT new map(sas.scholarship_approved_status_id as id,sd.auid as auid,sd.student_name as student_name,"
			+ "sas.prev_approved_amount as prev_approved_amount,s.scholarship_id as scholarship_id,s.candidate_id as candidate_id,sd.student_id as student_id,"
			+ "sc.school_name_short as school_name_short,sas.is_approved as is_approved,sas.is_verified as is_verified,cw.username as username,"
			+ "s.created_username as created_username,s.created_date as created_date,ua.username as counselorUsername,cw.npf_status as npf_status,"
			+ "s.requested_scholarship as requested_scholarship ,cw.candidate_name as candidate_name,cw.mobile_number as mobie_number,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.application_no_npf as application_no_npf,"
			+ "sas.pre_approval_date as pre_approval_date,sas.verified_by as verified_by,sas.approved_by as approved_by,"
			+ "sas.verified_date as verified_date,uaa.username as verifiedName,"
			+ "sa.scholarship_attachment_path as scholarship_attachment_path,sa.scholarship_attachment_file_name as scholarship_attachment_file_name,"
			+ "sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "p.school_id as school_id,sa.scholarship_attachment_id as scholarship_attachment_id) "
			+ "from Scholarship s left join ScholarshipApprovalStatus sas on s.scholarship_id=sas.scholarship_id "
			+ "left join PreAdmissionProcess p on p.candidate_id=s.candidate_id "
			+ "left join Candidate_Walkin cw on s.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on ua.id=sas.counselor_id "
			+ "left join UserAuthentication uaa on uaa.id=sas.verified_by "
			+ "left join ScholarshipAttachment sa  on s.candidate_id= sa.candidate_id "
			+ "left join Schools sc on p.school_id=sc.school_id "
			+ "left join Student_Details sd on s.candidate_id=sd.candidate_id "
			+ "where sas.is_verified Like '%yes%'")
	public Page<Object> findAllApprovedData14(Pageable pageable);
	
	
	
	
	@Query(value ="SELECT new map(sas.scholarship_approved_status_id as id,sd.auid as auid,sd.student_id as student_id,sd.student_name as student_name,"
			+ "sas.prev_approved_amount as prev_approved_amount,s.scholarship_id as scholarship_id,s.candidate_id as candidate_id,"
			+ "sc.school_name_short as school_name_short,sas.is_approved as is_approved,sas.is_verified as is_verified,cw.username as username,"
			+ "s.created_username as created_username,s.created_date as created_date,ua.username as counselorUsername,cw.npf_status as npf_status,"
			+ "s.requested_scholarship as requested_scholarship ,cw.candidate_name as candidate_name,cw.mobile_number as mobie_number,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.application_no_npf as application_no_npf,"
			+ "sas.pre_approval_date as pre_approval_date,sas.verified_by as verified_by,sas.approved_by as approved_by,"
			+ "sas.verified_date as verified_date,uaa.username as verifiedName,sas.verified_amount as verified_amount,"
			+ "sa.scholarship_attachment_path as scholarship_attachment_path,sa.scholarship_attachment_file_name as scholarship_attachment_file_name,"
			+ "sas.requestedByRemarks as requestedByRemarks,sas.verifier_remarks as verifier_remarks,"
			+ "sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "p.school_id as school_id,sa.scholarship_attachment_id as scholarship_attachment_id) "
			+ "from Scholarship s left join ScholarshipApprovalStatus sas on s.scholarship_id=sas.scholarship_id "
			+ "left join PreAdmissionProcess p on p.candidate_id=s.candidate_id "
			+ "left join Candidate_Walkin cw on s.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on ua.id=sas.counselor_id "
			+ "left join UserAuthentication uaa on uaa.id=sas.verified_by "
			+ "left join ScholarshipAttachment sa  on s.candidate_id= sa.candidate_id "
			+ "left join Schools sc on p.school_id=sc.school_id "
			+ "left join Student_Details sd on s.candidate_id=sd.candidate_id "
			+ "where CONCAT(IfNull(cw.created_date,''),'',IfNull(cw.candidate_name,''),'',"
			+ "IfNull(ps.program_specialization_short_name,''),'',IfNull(cw.candidate_id,''),'',"
			+ "IfNull(pr.program_short_name,''),'',IfNull(sc.school_name_short,'')) LIKE %?1% and sas.pre_approval_status=1 "
			+ "And sas.is_verified is not null and sd.ac_year_id=?2")
	public Page<Object> findAllApprovedData1(Pageable pageable, Object keyword, Integer ac_year_id);
	
	@Query(value ="SELECT new map(sas.scholarship_approved_status_id as id,sd.auid as auid,sd.student_name as student_name,"
			+ "sas.prev_approved_amount as prev_approved_amount,s.scholarship_id as scholarship_id,s.candidate_id as candidate_id,"
			+ "sc.school_name_short as school_name_short,sas.is_approved as is_approved,sas.is_verified as is_verified,cw.username as username,"
			+ "s.created_username as created_username,s.created_date as created_date,ua.username as counselorUsername,cw.npf_status as npf_status,"
			+ "s.requested_scholarship as requested_scholarship ,cw.candidate_name as candidate_name,cw.mobile_number as mobie_number,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.application_no_npf as application_no_npf,"
			+ "sas.pre_approval_date as pre_approval_date,sas.verified_by as verified_by,sas.approved_by as approved_by,"
			+ "sas.verified_date as verified_date,uaa.username as verifiedName,sas.verified_amount as verified_amount,"
			+ "sa.scholarship_attachment_path as scholarship_attachment_path,sa.scholarship_attachment_file_name as scholarship_attachment_file_name,"
			+ "sas.requestedByRemarks as requestedByRemarks,sas.verifier_remarks as verifier_remarks,"
			+ "sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "p.school_id as school_id,sa.scholarship_attachment_id as scholarship_attachment_id) "
			+ "from Scholarship s left join ScholarshipApprovalStatus sas on s.scholarship_id=sas.scholarship_id "
			+ "left join PreAdmissionProcess p on p.candidate_id=s.candidate_id and p.active=true "
			+ "left join Candidate_Walkin cw on s.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join UserAuthentication ua on ua.id=sas.counselor_id "
			+ "left join UserAuthentication uaa on uaa.id=sas.verified_by "
			+ "left join ScholarshipAttachment sa  on s.candidate_id= sa.candidate_id and sa.active=true "
			+ "left join Schools sc on p.school_id=sc.school_id "
			+ "left join Student_Details sd on s.candidate_id=sd.candidate_id and sd.active=true "
			+ "where sas.pre_approval_status=1 And sas.is_verified is not null and cw.ac_year_id=?1")
	public Page<Object> findAllApprovedData11(Pageable pageable, Integer ac_year_id);	
	
//	@Query(value = "SELECT new map(sd.auid as auid,sas.verified_date as verified_date,sas.verified_amount as verified_amount,s.scholarship_id as scholarship_id,s.candidate_id as candidate_id ,"
//			+ "sc.school_name_short as school_name_short,sas.is_verified as is_verified,sas.counselor_id as counselor_id,"
//			+ "s.created_username as created_username,s.created_date as created_date,ua.username as username,"
//			+ "s.requested_scholarship as requested_scholarship ,p.student_name as student_name,"
//			+ "ps.program_specialization_name as program_specialization_name,"
//			+ "ps.program_specialization_short_name as program_specialization_short_name,"
//			+ "pg.program_name as program_name,pg.program_short_name as program_short_name,"
//			+ "p.school_id as school_id,sa.scholarship_attachment_id as scholarship_attachment_id)"
//			+ "from Scholarship s left join ScholarshipApprovalStatus sas on s.scholarship_id=sas.scholarship_id "
//			+ "left join PreAdmissionProcess p on p.candidate_id=s.candidate_id "
//			+ "left join UserAuthentication ua on ua.id=sas.counselor_id "
//			+ "left join ProgramSpecilization ps on p.program_specialization_id=ps.program_specialization_id "
//			+ "left join Program pg on p.program_id=pg.program_id "
//			+ "left join ScholarshipAttachment sa on s.candidate_id= sa.candidate_id "
//			+ "left join Schools sc on p.school_id=sc.school_id "
//			+ "left join Student_Details sd on s.candidate_id=sd.candidate_id")
//	public List<HashMap<String, Object>> get3();

	@Query(value ="Select new map(s.scholarship_id as id,s.student_id as student_id,s.parent_income as parent_income,"
			+ "s.residence as residence,s.exemption_received as exemption_received,s.exemption_type as exemption_type,"
			+ "s.award as award,s.award_details as award_details,s.reason as reason,s.requested_scholarship as requested_scholarship,"
			+ "s.candidate_id as candidate_id,s.created_date as created_date,s.modified_date as modified_date,"
			+ "s.created_by as created_by,s.modified_by as modified_by,s.active as active,s.created_username as created_username,"
			+ "s.modified_username as modified_username,s.occupation as occupation) From Scholarship s "
			+ "Where CONCAT(IfNull(s.scholarship_id,''),'',IfNull(s.requested_scholarship,''),'',IfNull(s.award,''),'',IfNull(s.created_date,''),'',IfNull(s.created_by,''),'',IfNull(s.created_username,'')) LIKE %?1%")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(s.scholarship_id as id,s.student_id as student_id,s.parent_income as parent_income,"
			+ "s.residence as residence,s.exemption_received as exemption_received,s.exemption_type as exemption_type,"
			+ "s.award as award,s.award_details as award_details,s.reason as reason,s.requested_scholarship as requested_scholarship,"
			+ "s.candidate_id as candidate_id,s.created_date as created_date,s.modified_date as modified_date,"
			+ "s.created_by as created_by,s.modified_by as modified_by,s.active as active,s.created_username as created_username,"
			+ "s.modified_username as modified_username,s.occupation as occupation) From Scholarship s")
	public Page<Object> findAll2(Pageable pageable);
	
	@Modifying
	@Query(value = "update Scholarship d set d.active=false where d.candidate_id=?1")
	public void update(Integer id);
	
	@Modifying
	@Query(value = "update Scholarship d set d.active=true where d.candidate_id=?1")
	public void update1(Integer id);
	
	@Query(value = "select count(*) from Scholarship where student_id=?1 and active=true")
	public Integer getCountOfStudent(Integer student_id);
	
	@Query(value = "Select sch.scholarship_id from scholarship sch where sch.student_id=?1 and sch.active=true",nativeQuery=true)
	public List<Integer> getAllScholarshipIdOFStudent(Integer student_id);
}
