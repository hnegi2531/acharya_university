package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Candidate_Walkin;
import com.au.model.Student_Details;

@Repository
@Transactional
public interface CandidateWalkinRepository extends JpaRepository<Candidate_Walkin, Integer> {

	@Query(value = "SELECT candidate_id FROM candidate_walkin where candidate_id=?1", nativeQuery = true)
	public Candidate_Walkin findByCandidateId(Integer pg_id);

	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,cw.school_id As school_id,cw.application_status as application_status,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,pap.created_date as offerCreatedDate,"
			+ "cw.result_status as result_status,pap.fee_admission_category_id as fee_admission_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks," +
			"cw.rep_name as rep_name,na.nationality as nationality,cou.name as country_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join Nationality na on na.nationality_id = cw.nationality "
			+ "left join Country cou on cw.country_id=cou.id  "
			+ "where CONCAT(IfNull(cw.created_date,''),'',IfNull(cw.candidate_name,''),'',"
			+ "IfNull(pr.program_short_name,''),'',IfNull(cw.candidate_id,''),'',"
			+ "IfNull(cw.candidate_email,''),'',IfNull(cw.school_npf,'')) LIKE %?1% and sd.auid IS NULL")
	public Page<Object> getCandidateDetailsSearchData(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,cw.school_id As school_id,cw.application_status as application_status,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,pap.created_date as offerCreatedDate,"
			+ "cw.result_status as result_status,pap.fee_admission_category_id as fee_admission_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks," +
			"cw.rep_name as rep_name,na.nationality as nationality,cou.name as country_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join Nationality na on na.nationality_id = cw.nationality "
			+ "left join Country cou on cw.country_id=cou.id  "
			+ "where CONCAT(IfNull(cw.created_date,''),'',IfNull(cw.candidate_name,''),'',"
			+ "IfNull(pr.program_short_name,''),'',IfNull(cw.candidate_id,''),'',"
			+ "IfNull(cw.candidate_email,''),'',IfNull(cw.school_npf,'')) LIKE %?1% and cw.counselor_id=?2 and sd.auid IS NULL")
	public Page<Object> getCandidateDetailsSortData(Pageable pageable,  Object keyword, Integer user_id);
	
	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,cw.school_id As school_id,cw.application_status as application_status,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,pap.created_date as offerCreatedDate,"
			+ "cw.result_status as result_status,pap.fee_admission_category_id as fee_admission_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks," +
			"cw.rep_name as rep_name,na.nationality as nationality,cou.name as country_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join Nationality na on na.nationality_id = cw.nationality "
			+ "left join Country cou on cw.country_id=cou.id "
			+ "where sd.auid IS NULL and cw.active=true")
	public Page<Object> getCandidateDetailsSearchData1(Pageable pageable);
	
	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,cw.school_id As school_id,cw.application_status as application_status,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,pap.created_date as offerCreatedDate,"
			+ "cw.result_status as result_status,pap.fee_admission_category_id as fee_admission_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks," +
			"cw.rep_name as rep_name,na.nationality as nationality,cou.name as country_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join Nationality na on na.nationality_id = cw.nationality "
			+ "left join Country cou on cw.country_id=cou.id "
			+ "where sd.auid IS NULL and cw.counselor_id=?1 and cw.active=true")
	public Page<Object> getCandidateDetailsSortData1(Pageable pageable, Integer user_id);

//	@Modifying
//	@Query(value = "update Candidate_Walkin cw set cw.active=false,cw.npf_status=null where cw.candidate_id=?1")
//	@Query(value = "update candidate_walkin cw set cw.active=false , cw.npf_status=null where candidate_id=?1",nativeQuery = true)
//	public void updateCandidateWakin(Candidate_Walkin ay);
	
	@Modifying
	@Query(value = "update candidate_walkin cw set cw.active=false where candidate_id=?1",nativeQuery = true)
	public void updateCandidateWakin(Integer candidate_id);
	
	@Modifying
	@Query(value = "update candidate_walkin cw set cw.active=true where candidate_id=?1",nativeQuery = true)
	public void updateCandidateWakin1(Integer candidate_id);

	@Query(value="select cw from Candidate_Walkin cw where cw.active=true")
	public List<Candidate_Walkin> findAll1();
	
	@Query(value="select cw from Candidate_Walkin cw where cw.active=true")
	public List<Candidate_Walkin> findAll6();

	@Query(value = "select new map(c.candidate_name as candidate_name,c.father_name as father_name,"
			+ "c.date_of_birth as date_of_birth,c.candidate_sex as candidate_sex,"
			+ "c.nationality as nationality,c.candidate_email as candidate_email,c.state_id as state_id,"
			+ "s.name as state_name,c.country_id as country_id,cou.name as country_name,c.school_id as school_id,"
			+ "c.mobile_number as mobile_number,c.lead_status as lead_status  ) from  Candidate_Walkin c "
			+ "left join Country cou on c.country_id=cou.id "
			+ "left join State s on c.state_id=s.id where c.candidate_id=?1")
	public List<HashMap<String, Object>> listAll2(Integer cid);
	
	//@Query(value="select cw from Candidate_Walkin cw where  CONCAT(IfNull(candidate_name,''), ' ',ifNull(father_name,''), ' ',ifNull(date_of_visit,''), ' ',ifNull(date_of_birth,''), ' ',ifNull(candidate_sex,'')) LIKE %?1% and cw.active=true")
	//public Page<Object> getRecords(Pageable pageable, String keyword);
	
	//@Query(value="select cw from candidate_walkin cw where :dQyery and cw.active=true",nativeQuery=true)
	//column +"="+keyword +" and "+column"="value;
	//public Page<Object> getRecords(Pageable pageable, String dQyery);
	
	
	@Query(value = "select new map(cw.username as username,cw.application_status as application_status,cw.program_assignment_id as program_assignment_id, cw.mail_sent_date as mail_sent_date,cw.npf_status as npf_status,cw.active as active,s.requested_scholarship as requested_scholarship,"
			+ "cw.application_no_npf as application_no_npf,sas.pre_approval_status as pre_approval_status,cw.candidate_id as id,cw.created_date as created_date,cw.link_exp as link_exp,"
			+ "cw.candidate_name as candidate_name, sc.school_name_short  as school_name_short,pr.program_short_name as program_short_name,cw.result_status as result_status,"
			+ "cw.result_score as result_score,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.mobile_number as mobie_number,cw.counselor_status as counselor_status,"
			+ "pap.is_scholarship as is_scholarship,pap.created_username as created_username,sas.is_approved as is_approved, cw.exam_details_id as exam_details_id,cw.ac_year_id as ac_year_id,exam.exam_date as exam_date,"
			+ "cw.passport_expiry_date as passport_expiry_date,"
			+ "sa.scholarship_attachment_id as scholarship_attachment_id,sa.scholarship_attachment_path as scholarship_attachment_path,"
			+ "sa.scholarship_attachment_file_name as scholarship_attachment_file_name,sa.scholarship_attachement_type as scholarship_attachement_type,"
			+ "sas.approved_date as approved_date,sas.approved_by as approved_by,ua.id as userId,ua.username as approvedByusername,"
			+ "sas.pre_approval_date as pre_approval_date,sas.pre_approver_by as pre_approver_by,uaa.username as preApproverByName,"
			+ "cw.lead_status as lead_status ) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id "
			+ "left join UserAuthentication ua on sas.approved_by=ua.id "
			+ "left join UserAuthentication uaa on sas.pre_approver_by=uaa.id "
			+ "left join Scholarship s on cw.candidate_id=s.candidate_id "
			+ "left join ScholarshipAttachment sa on cw.candidate_id=sa.candidate_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join ExamDetails exam on exam.exam_details_id=cw.exam_details_id "
			+ "where CONCAT(IfNull(cw.created_date,''),'',IfNull(cw.candidate_name,''),'',"
			+ "IfNull(ps.program_specialization_short_name,''),'',IfNull(cw.candidate_id,''),'',"
			+ "IfNull(pr.program_short_name,''),'',IfNull(sc.school_name_short,'')) LIKE %?1% and pap.is_scholarship=1 and pap.active=true and sas.active=true and s.active=true And sas.pre_approval_status IS null")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(cw.username as username,cw.application_status as application_status,cw.program_assignment_id as program_assignment_id,cw.mail_sent_date as mail_sent_date, cw.npf_status as npf_status,cw.active as active,s.requested_scholarship as requested_scholarship,"
			+ "cw.application_no_npf as application_no_npf,sas.pre_approval_status as pre_approval_status,cw.candidate_id as id,cw.created_date as created_date,cw.link_exp as link_exp,"
			+ "cw.candidate_name as candidate_name, sc.school_name_short  as school_name_short,pr.program_short_name as program_short_name,cw.result_status as result_status,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.mobile_number as mobie_number,cw.exam_details_id as exam_details_id,cw.counselor_status as counselor_status,"
			+ "pap.is_scholarship as is_scholarship,pap.created_username as created_username,sas.is_approved as is_approved,cw.ac_year_id as ac_year_id,"
			+ "cw.passport_expiry_date as passport_expiry_date,"
			+ "cw.result_score as result_score,exam.exam_date as exam_date,"
			+ "sa.scholarship_attachment_id as scholarship_attachment_id,sa.scholarship_attachment_path as scholarship_attachment_path,"
			+ "sa.scholarship_attachment_file_name as scholarship_attachment_file_name,sa.scholarship_attachement_type as scholarship_attachement_type,"
			+ "sas.approved_date as approved_date,sas.approved_by as approved_by,ua.id as userId,ua.username as approvedByusername,"
			+ "sas.pre_approval_date as pre_approval_date,sas.pre_approver_by as pre_approver_by,uaa.username as preApproverByName,"
			+ "cw.lead_status as lead_status ) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id "
			+ "left join UserAuthentication ua on sas.approved_by=ua.id "
			+ "left join UserAuthentication uaa on sas.pre_approver_by=uaa.id "
			+ "left join Scholarship s on cw.candidate_id=s.candidate_id "
			+ "left join ScholarshipAttachment sa on cw.candidate_id=sa.candidate_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join ExamDetails exam on exam.exam_details_id=cw.exam_details_id "
			+ "where pap.is_scholarship=1 and pap.active=true and sas.active=true and s.active=true And sas.pre_approval_status IS null")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	//@Query(value="select cw from candidate_walkin cw where ?0 and cw.active=true")
	//column +"="+keyword +" and "+column"="value;
	//public Page<Object> getRecords2(Pageable pageable, String dQuery);
	
	
	/*@Query(value="select cw from Candidate_Walkin cw where :column=:keyword and :column=:value and cw.active=true")
	public Page<Object> getRecords(Pageable pageable, String keyword,Object column,Object value);*/
	
	
	
//	@Query(value = "select new map(cw.username as username, cw.npf_status as npf_status, "
//			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as candidate_id,cw.created_date as created_date,"
//			+ "cw.candidate_name as candidate_name, sc.school_name  as school_name,pr.program_name as program_name,"
//			+ "ps.program_specialization_name as program_specialization_name,cw.mobile_number as mobie_number,"
//			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved, cw.ac_year_id as ac_year_id) "
//			+ "from Candidate_Walkin cw left join Schools sc on cw.school_id=sc.school_id "
//			+ "left join Program pr on cw.program_id=pr.program_id "
//			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id "
//			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id "
//			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
//			+ "where CONCAT(cw.ac_year_id, ' ',cw.candidate_name, ' ',ps.program_specialization_name) LIKE %?1% and cw.active=true")
//	public Page<Object> getRecords10(Pageable pageable, String keyword);
//	
//	@Query(value = "select new map(cw.username as username, cw.npf_status as npf_status, "
//			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as candidate_id,cw.created_date as created_date,"
//			+ "cw.candidate_name as candidate_name, sc.school_name  as school_name,pr.program_name as program_name,"
//			+ "ps.program_specialization_name as program_specialization_name,cw.mobile_number as mobie_number,"
//			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved, cw.ac_year_id as ac_year_id)  "
//			+ "from Candidate_Walkin cw left join Schools sc on cw.school_id=sc.school_id "
//			+ "left join Program pr on cw.program_id=pr.program_id "
//			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id "
//			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id "
//			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
//			+ "where cw.active=true")
//	public Page<Object> getAllData11(Pageable pageable);
//	
//	@Query(value="select cw from Candidate_Walkin cw where CONCAT(ac_year_id, ' ',cw.candidate_name, ' ',npf_status, ' ',father_name, ' ',"
//			+ "date_of_visit, ' ',date_of_birth, ' ',candidate_sex) LIKE %?1% and cw.active=true")
//	public Page<Candidate_Walkin> getRecords101(Pageable pageable, String keyword);
//	
//	@Query(value="select cw from Candidate_Walkin cw where cw.active=true")
//	public Page<Candidate_Walkin> getAllData111(Pageable pageable);
//	
//	@Query(value = "select new map(cw.username as username, cw.npf_status as npf_status,"
//			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as candidate_id,cw.created_date as created_date,"
//			+ "cw.candidate_name as candidate_name, sc.school_name  as school_name,pr.program_name as program_name,"
//			+ "ps.program_specialization_name as program_specialization_name,cw.mobile_number as mobie_number,"
//			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved, cw.ac_year_id as ac_year_id) "
//			+ "from Candidate_Walkin cw left join Schools sc on cw.school_id=sc.school_id "
//			+ "left join Program pr on cw.program_id=pr.program_id "
//			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id "
//			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id "
//			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
//			+ "where cw.candidate_name=?1  or ps.program_specialization_name=?1   or pr.program_name=?1 or sc.school_name=?1")
//	public Page<Candidate_Walkin> getFilteredData(Pageable pageable, Object keyword);
//	
	@Query(value ="SELECT MAX(CAST(SUBSTRING(cw.application_no_npf, LOCATE('DA', cw.application_no_npf) + 2) AS UNSIGNED)) AS max_value"
			+ " From candidate_walkin cw Where cw.ac_year_id=?1 And cw.application_no_npf Like ?2% and cw.active=true", nativeQuery = true)
	public Integer countOfCandidateWalkin(Integer ac_year_id,String aplicationNoWithoutCount);
	
	@Query(value = "select new map(cw.candidate_name as candidate_name, sc.school_name_short  as school_name_short,sc.school_name as school_name,"
			+ "pr.program_short_name as program_short_name,cw.candidate_email as candidate_email,ua.email as counselorEmail,"			
			+ "pr.program_name as program_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join UserAuthentication ua on cw.counselor_id=ua.id "
			+ "where cw.candidate_id=?1")
	public List<HashMap<String, Object>> getCandidateDetailsByCandidateId(Integer candidate_id);
	
	
	@Query(value="select cw from Candidate_Walkin cw where  cw.school_id=?1 and  cw.ac_year_id=?2 and cw.active=true")
	public List<Candidate_Walkin> fetchCandidate_Walkin(Integer school_id, Integer ac_year_id);
	
	@Query(value="select * from candidate_walkin where created_date between "
			+ "STR_TO_DATE(:from_date, '%d-%m-%Y') and STR_TO_DATE(:to_date, '%d-%m-%Y') and school_id=:school_id "
			+ "and  ac_year_id=:ac_year_id and active=true", nativeQuery = true)
	public List<Candidate_Walkin> fetchCandidate_Walkin1(Integer school_id, Integer ac_year_id,String from_date, String to_date);
	
	@Query(value="Select cw from Candidate_Walkin cw where cw.application_no_npf = ?1 And cw.active=true")
	public Candidate_Walkin candidateWalkinDetailOnApplicationNpf(String application_no_npf);
	
//	@Query(value ="Select cw.update_id From candidate_walkin cw Where cw.is_telegram_verified =true And date(cw.created_date)=date(now()) Limit 1",nativeQuery=true)
//	public Integer getLastUpdateIdOfTelegram();
//	
//	@Modifying
//	@Query(value="Update Candidate_Walkin cw Set cw.update_id=?2,cw.is_telegram_verified=true,cw.telegram_chat_id=?3 where cw.application_no_npf = ?1 And cw.active=true")
//	public void updateTelegramUpdateId(String application_no_npf,Integer update_id,String telegram_chat_id);
//	
	@Modifying
	@Query(value="Update Candidate_Walkin cw Set cw.form_filled_percentage='100',cw.application_status='Application Completed' where cw.candidate_id = ?1 And cw.active=true")
	public void updateFormFilledPrecentageOfCandidate(Integer candidate_id);
	
	@Modifying
	@Query(value = "update candidate_walkin cw set cw.lead_status=?2 where candidate_id=?1",nativeQuery = true)
	public void updateLeadStatus(Integer candidate_id,String lead_status);
	
	@Modifying
	@Query(value = "update candidate_walkin cw set cw.npf_status=2, link_exp=?2 where candidate_id=?1",nativeQuery = true)
	public void updateNpfStatusOfBeforeAcceptingOffer(Integer candidate_id, String formattedDate);
	
	
	@Modifying
	@Query(value = "update candidate_walkin cw set cw.npf_status=3 where candidate_id=?1",nativeQuery = true)
	public void updateNpfStatusOfAcceptingOffer(Integer candidate_id);
	
	
	@Modifying
	@Query(value = "update candidate_walkin cw set cw.counselor_status=1 where candidate_id=?1",nativeQuery = true)
	public void updateCounselorStatus(Integer candidate_id);

	@Query(value = "select new map(cw.username as username,cw.application_status as application_status,cw.program_assignment_id as program_assignment_id, cw.mail_sent_date as mail_sent_date,cw.npf_status as npf_status,cw.active as active,s.requested_scholarship as requested_scholarship,"
			+ "cw.application_no_npf as application_no_npf,sas.pre_approval_status as pre_approval_status,cw.candidate_id as id,cw.created_date as created_date,cw.link_exp as link_exp,"
			+ "cw.candidate_name as candidate_name, sc.school_name_short  as school_name_short,pr.program_short_name as program_short_name,cw.result_status as result_status,"
			+ "cw.result_score as result_score,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.mobile_number as mobie_number,cw.counselor_status as counselor_status,"
			+ "pap.is_scholarship as is_scholarship,pap.created_username as created_username,sas.is_approved as is_approved, cw.exam_details_id as exam_details_id,cw.ac_year_id as ac_year_id,exam.exam_date as exam_date,"
			+ "cw.passport_expiry_date as passport_expiry_date,"
			+ "sa.scholarship_attachment_id as scholarship_attachment_id,sa.scholarship_attachment_path as scholarship_attachment_path,"
			+ "sa.scholarship_attachment_file_name as scholarship_attachment_file_name,sa.scholarship_attachement_type as scholarship_attachement_type,"
			+ "sas.approved_date as approved_date,sas.approved_by as approved_by,ua.id as userId,ua.username as approvedByusername,sas.pre_approver_remarks as pre_approver_remarks,"
			+ "sas.pre_approval_date as pre_approval_date,sas.pre_approver_by as pre_approver_by,uaa.username as preApproverByName,"
			+ "cw.lead_status as lead_status ) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id "
			+ "left join UserAuthentication ua on sas.approved_by=ua.id "
			+ "left join UserAuthentication uaa on sas.pre_approver_by=uaa.id "
			+ "left join Scholarship s on cw.candidate_id=s.candidate_id "
			+ "left join ScholarshipAttachment sa on cw.candidate_id=sa.candidate_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join ExamDetails exam on exam.exam_details_id=cw.exam_details_id "
			+ "where CONCAT(IfNull(cw.created_date,''),'',IfNull(cw.candidate_name,''),'',"
			+ "IfNull(ps.program_specialization_short_name,''),'',IfNull(cw.candidate_id,''),'',"
			+ "IfNull(pr.program_short_name,''),'',IfNull(sc.school_name_short,'')) LIKE %?1% and pap.is_scholarship=1 "
			+ "And sas.pre_approval_status Is Not NULL")
	public Page<Object> getAllDataFilteredByKeyword1(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(cw.username as username,cw.application_status as application_status,cw.program_assignment_id as program_assignment_id,cw.mail_sent_date as mail_sent_date, cw.npf_status as npf_status,cw.active as active,s.requested_scholarship as requested_scholarship,"
			+ "cw.application_no_npf as application_no_npf,sas.pre_approval_status as pre_approval_status,cw.candidate_id as id,cw.created_date as created_date,cw.link_exp as link_exp,"
			+ "cw.candidate_name as candidate_name, sc.school_name_short  as school_name_short,pr.program_short_name as program_short_name,cw.result_status as result_status,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.mobile_number as mobie_number,cw.exam_details_id as exam_details_id,cw.counselor_status as counselor_status,"
			+ "pap.is_scholarship as is_scholarship,pap.created_username as created_username,sas.is_approved as is_approved,cw.ac_year_id as ac_year_id,"
			+ "cw.passport_expiry_date as passport_expiry_date,"
			+ "cw.result_score as result_score,exam.exam_date as exam_date,"
			+ "sa.scholarship_attachment_id as scholarship_attachment_id,sa.scholarship_attachment_path as scholarship_attachment_path,"
			+ "sa.scholarship_attachment_file_name as scholarship_attachment_file_name,sa.scholarship_attachement_type as scholarship_attachement_type,"
			+ "sas.approved_date as approved_date,sas.approved_by as approved_by,ua.id as userId,ua.username as approvedByusername,sas.pre_approver_remarks as pre_approver_remarks,"
			+ "sas.pre_approval_date as pre_approval_date,sas.pre_approver_by as pre_approver_by,uaa.username as preApproverByName,"
			+ "cw.lead_status as lead_status ) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id "
			+ "left join UserAuthentication ua on sas.approved_by=ua.id "
			+ "left join UserAuthentication uaa on sas.pre_approver_by=uaa.id "
			+ "left join Scholarship s on cw.candidate_id=s.candidate_id "
			+ "left join ScholarshipAttachment sa on cw.candidate_id=sa.candidate_id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join ExamDetails exam on exam.exam_details_id=cw.exam_details_id "
			+ "where pap.is_scholarship=1 "
			+ "And sas.pre_approval_status Is Not NULL")
	public Page<Object> getAllSortedData1(Pageable pageable);

	@Query(value ="Select cw.application_no_npf From candidate_walkin cw Where cw.ac_year_id=?1 And cw.application_no_npf LIKE '%DA/%' Order by cw.candidate_id Desc Limit 1",nativeQuery=true)
	public String getApplicationNoNpf(Integer ac_year_id);
	

//	@Query(value = "Select new map(cw.username as username,cw.application_status as application_status,cw.repeat_status as repeat_status,"
//			+ "cw.sur_name as sur_name,cw.middle_name as middle_name,cw.family_name as family_name,"
//			+ "cw.passport_expiry_date as passport_expiry_date,cw.program_specilization_npf as program_specilization_npf,cw.program_npf as program_npf, cw.school_npf as school_npf,"
//			+ "cw.program_assignment_id as program_assignment_id,cw.mail_sent_date as mail_sent_date,cw.dtm as dtm,cw.result_score as result_score,"
//			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,ct.sign_time as paid_date_click,"
//			+ "sas.is_verified as is_verified,cw.lead_status as lead_status,pt.normal_performed_time as paid_date_payme,"
//			+ "la.lead_status as lead_status1,ud.username as counselor_name,la.user_id as counselor_id,la.lead_assignment_id as lead_assignment_id,"
//			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,cw.telegram_number as telegram_number,"
//			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,exam.exam_date as exam_date,"
//			+ "cw.created_username as created_username,cw.candidate_name as candidate_name,"
//			+ "cw.exam_details_id as exam_details_id,cw.mobile_number as mobile_number,cw.dtm_attchment_path as dtm_attchment_path,"
//			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,cw.form_filled_percentage as form_filled_percentage,"
//			+ "cw.result_status as result_status,cr.type as type, cr.grade as grade,cr.result as result,cr.percentage as percentage,"
//			+ "cw.gov as gov,cw.candidate_sex as candidate_sex,"
//			+ "cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.is_puc_result as is_puc_result,cw.remarks as remarks,cw.rep_name as rep_name,cw.pinfl as pinfl) from Candidate_Walkin cw "
//			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
//			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
//			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
//			+ "left join UserAuthentication u on pap.created_by=u.id "
//			+ "left join CandidateResults cr on cr.application_no=cw.application_no_npf "
//			+ "left join ExamDetails exam on exam.exam_details_id=cw.exam_details_id "
//			+ "left join LeadAssignment la on la.candidate_id=cw.candidate_id "
//			+ "left join UserAuthentication ud on ud.id=la.user_id "
//			+ "left join PaymeTransactions pt on pt.candidate_id=cw.candidate_id and pt.state=2 "
//			+ "left join ClickPayment ct on ct.candidate_id=cw.candidate_id And ct.action=1 And ct.error=0 And ct.error_note='Success' Where cw.ac_year_id >= 19 ")
	
	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,"
			+ "cw.result_status as result_status,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks,cw.rep_name as rep_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "where sd.auid IS NULL and cw.active=true")
	public Page<Object> getCandidateDetailsSortDataWithoutAuidValidation(Pageable pageable);
	
//	@Query(value = "Select new map(cw.username as username,cw.application_status as application_status,cw.repeat_status as repeat_status,"
//			+ "cw.sur_name as sur_name,cw.middle_name as middle_name,cw.family_name as family_name,cw.dtm as dtm,cw.result_score as result_score,"
//			+ "cw.program_assignment_id as program_assignment_id,cw.mail_sent_date as mail_sent_date,cw.result_score as result_score,"
//			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,ct.sign_time as paid_date_click,"
//			+ "cw.passport_expiry_date as passport_expiry_date,cw.program_specilization_npf as program_specilization_npf,cw.program_npf as program_npf, cw.school_npf as school_npf,"
//			+ "sas.is_verified as is_verified,cw.lead_status as lead_status,pt.normal_performed_time as paid_date_payme,"
//			+ "la.lead_status as lead_status1,ud.username as counselor_name,la.user_id as counselor_id,la.lead_assignment_id as lead_assignment_id,"
//			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,cw.telegram_number as telegram_number,"
//			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,exam.exam_date as exam_date,"
//			+ "cw.created_username as created_username,cw.candidate_name as candidate_name,"
//			+ "cw.exam_details_id as exam_details_id,cw.mobile_number as mobile_number,cw.dtm_attchment_path as dtm_attchment_path,"
//			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,cw.form_filled_percentage as form_filled_percentage,"
//			+ "cw.result_status as result_status,cr.type as type, cr.grade as grade,cr.result as result,cr.percentage as percentage,"
//			+ "cw.dtm_attchment_path as dtm_attchment_path,"
//			+ "cw.gov as gov,cw.candidate_sex as candidate_sex,"
//			+ "cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.is_puc_result as is_puc_result,cw.remarks as remarks,cw.rep_name as rep_name,cw.pinfl as pinfl) from Candidate_Walkin cw "
//			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
//			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
//			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
//			+ "left join UserAuthentication u on pap.created_by=u.id "
//			+ "left join CandidateResults cr on cr.application_no=cw.application_no_npf "
//			+ "left join ExamDetails exam on exam.exam_details_id=cw.exam_details_id "
//			+ "left join LeadAssignment la on la.candidate_id=cw.candidate_id "
//			+ "left join UserAuthentication ud on ud.id=la.user_id "
//			+ "left join PaymeTransactions pt on pt.candidate_id=cw.candidate_id and pt.state=2 "
//			+ "left join ClickPayment ct on ct.candidate_id=cw.candidate_id And ct.action=1 And ct.error=0 And ct.error_note='Success' "
//			+ "Where cw.ac_year_id >= 19 and cw.counselor_id=?1 And cw.active=true")
	
	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,cw.result_status as result_status,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks,cw.rep_name as rep_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "where sd.auid IS NULL and cw.counselor_id=?1 and cw.active=true")
	public Page<Object> getCandidateDetailsSortDataWithoutAuidValidationOnUserId(Pageable pageable,Integer user_id);


//	@Query(value = "SELECT cw.telegram_chat_id FROM candidate_walkin cw where cw.candidate_id IN (Select sd.candidate_id from student_details sd where sd.student_id IN (?1) and sd.active=true) and cw.active=true", nativeQuery = true)
//	public List<String> getTelegramChatId(List<Integer> student_ids);

	@Query(value = "SELECT cw.npf_status from candidate_walkin cw where cw.candidate_id=?1 and cw.active=true", nativeQuery = true)
	public Integer getNpfStatus(Integer candidate_id);

	@Query(value=" select c from  Candidate_Walkin c where c.candidate_id=:candidateId ")
	public Candidate_Walkin getByCandidateId(Integer candidateId);

	
	@Query(value = "select count(*) from Candidate_Walkin cw where cw.lead_id=?1 And cw.active=true ")
	public Integer getcountOfLeadId(String lead_id);
	@Query(value = "select count(*) from Candidate_Walkin cw where cw.lead_id=?1 And cw.opportunity_id=?2")
	public Integer getcountOfLeadIdAndOpportunityId(String lead_id,String opportunity_id);
	@Query(value = "select count(*) from Candidate_Walkin cw where cw.opportunity_id=?1")
	public Integer getcountOfOpportunityId(String opportunity_id);

	@Query(value="select cw from Candidate_Walkin cw where cw.active=true ")
	public List<Candidate_Walkin> getCandidateWalkinDataLsq();

	@Query(value="select cw from Candidate_Walkin cw where cw.lead_id=?1 And cw.active=true")
	public List<Candidate_Walkin> getCandidateWalkinDataLsqWithLeadId(String lead_id);

	@Query(value="select cw from Candidate_Walkin cw where cw.opportunity_id=?1 And cw.active=true")
	public List<Candidate_Walkin> getCandidateWalkinDataLsqWithOpportunityId(String opportunity_id);

	@Query(value="select cw from Candidate_Walkin cw where cw.lead_id=?1 And cw.opportunity_id=?2 And cw.active=true")
	public Candidate_Walkin getCandidateWalkinDetails(String lead_id, String opportunity_id);

	@Query(value="select cw.candidate_id from Candidate_Walkin cw where cw.lead_id=?1 And cw.opportunity_id=?2 And cw.active=true")
	public Integer getByCandidateId(String lead_id, String opportunity_id);
	
	@Query(value="select cw.candidate_id from Candidate_Walkin cw where cw.lead_id=?1 And cw.active=true")
	public List<Integer> getByCandidateIdLeadId(String lead_id);
	
	@Query(value="select cw.candidate_id from Candidate_Walkin cw where cw.lead_id=?1 And cw.active=true")
	public Integer getByCandidateId1(String lead_id);
	
	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,cw.school_id As school_id,cw.application_status as application_status,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,pap.created_date as offerCreatedDate,"
			+ "cw.result_status as result_status,pap.fee_admission_category_id as fee_admission_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks,cw.rep_name as rep_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "where CONCAT(IfNull(cw.created_date,''),'',IfNull(cw.candidate_name,''),'',"
			+ "IfNull(pr.program_short_name,''),'',IfNull(cw.candidate_id,''),'',"
			+ "IfNull(cw.candidate_email,''),'',IfNull(cw.school_npf,'')) LIKE %?1% and sd.auid IS NULL and (cw.nationality != 103 OR (cw.nationality = 103 AND cw.is_nri = true))")
	public Page<Object> fetchNonIndianDataFromCandidateWalkinSearchData(Pageable pageable, Object keyword); 
	
	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,cw.school_id As school_id,cw.application_status as application_status,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,pap.created_date as offerCreatedDate,"
			+ "cw.result_status as result_status,pap.fee_admission_category_id as fee_admission_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks,cw.rep_name as rep_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "where CONCAT(IfNull(cw.created_date,''),'',IfNull(cw.candidate_name,''),'',"
			+ "IfNull(pr.program_short_name,''),'',IfNull(cw.candidate_id,''),'',"
			+ "IfNull(cw.candidate_email,''),'',IfNull(cw.school_npf,'')) LIKE %?1% and cw.counselor_id=?2 and sd.auid IS NULL and (cw.nationality != 103 OR (cw.nationality = 103 AND cw.is_nri = true))")
	public Page<Object> fetchNonIndianDataFromCandidateWalkinSortData(Pageable pageable,  Object keyword, Integer user_id);
	
	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,cw.school_id As school_id,cw.application_status as application_status,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,pap.created_date as offerCreatedDate,"
			+ "cw.result_status as result_status,pap.fee_admission_category_id as fee_admission_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks,cw.rep_name as rep_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "where sd.auid IS NULL and cw.active=true and (cw.nationality != 103 OR (cw.nationality = 103 AND cw.is_nri = true))")
	public Page<Object> fetchNonIndianDataFromCandidateWalkinSearchData1(Pageable pageable);
	
	@Query(value = "Select new map(u.username as username,"
			+ "cw.mail_sent_date as mail_sent_date,cw.school_id As school_id,cw.application_status as application_status,"
			+ "cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,"
			+ "sas.is_verified as is_verified,cw.counselor_id as counselor_id,cw.counselor_name as counselor_name,cw.counselor_remarks as counselor_remarks,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "cw.mobile_number as mobile_number,pr.program_name as program_name,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,pap.created_date as offerCreatedDate,"
			+ "cw.result_status as result_status,pap.fee_admission_category_id as fee_admission_category_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "cw.candidate_sex as candidate_sex,cw.school_npf as school_npf,cw.date_of_birth as date_of_birth,cw.remarks as remarks,cw.rep_name as rep_name) from Candidate_Walkin cw "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "where sd.auid IS NULL and cw.counselor_id=?1 and cw.active=true and (cw.nationality != 103 OR (cw.nationality = 103 AND cw.is_nri = true))")
	public Page<Object> fetchNonIndianDataFromCandidateWalkinSortData1(Pageable pageable, Integer user_id);

	@Query(value = "select " +
	            "  CASE " +
	            "  WHEN ed.gender = 'M' THEN CONCAT('Mr. ', ed.employee_name) " +
	            "  WHEN ed.gender = 'F' THEN CONCAT('Ms. ', ed.employee_name) " +
	            "  ELSE ed.employee_name " +
	            "  END as counsellorName " +
	            "  from EmployeeDetails ed " +
	            "  where ed.emp_id = (" +
	            "  select cw.counselor_id " +
	            "  from Candidate_Walkin cw " +
	            "  where cw.candidate_id = ?1 " +
	            "  and cw.active = true" +
	            ")")
	public String getCounsellorName(Integer candidate_id);

	@Query(value="select cw from Candidate_Walkin cw where cw.active=true and cw.lead_id=:lead_id ")
	public List<Candidate_Walkin> getAllByLeadId(String lead_id);

	@Query(value="select cw.counsellor_email from Candidate_Walkin cw where cw.active=true and cw.candidate_id=?1 ")
	public String getCounsellorEmail(Integer candidate_id);

	
	@Query(value="select cw from Candidate_Walkin cw where cw.active=true and cw.candidate_id=?1 And cw.active=true")
	public Candidate_Walkin getExistingCandidate(Integer id);

	@Query(value="SELECT sd.student_id FROM candidate_walkin cw "
			+ "left join student_details sd on sd.candidate_id = cw.candidate_id "
			+ "where ((:lead_id IS NULL OR cw.lead_id = :lead_id) or (:opportunity_id IS NULL OR cw.opportunity_id = :opportunity_id)) ", nativeQuery = true)
	public Integer getStudentId(String lead_id, String opportunity_id);

	
}