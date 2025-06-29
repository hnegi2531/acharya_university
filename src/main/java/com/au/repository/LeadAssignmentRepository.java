package com.au.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.LeadAssignment;


@Repository
@Transactional
public interface LeadAssignmentRepository extends JpaRepository<LeadAssignment, Integer>  {
	
	@Query(value = "select la from LeadAssignment la where la.active=true")
	public List<LeadAssignment> getActiveLeadAssignment();
	
	@Query(value = "Select new map(la.lead_assignment_id as id,la.lead_status as lead_status,la.user_id as user_id,"
			+ "la.candidate_id as candidate_id,la.created_date as created_date,"
			+ "la.modified_date as modified_date,la.created_by as created_by,la.modified_by as modified_by,la.active as active,"
			+ "la.created_username as created_username,la.modified_username as modified_username) from LeadAssignment la "
			+ "Where CONCAT(IfNull(la.lead_assignment_id,''),'',IfNull(la.created_by,''),'',IfNull(la.created_date,'')"
			+ ",'',IfNull(la.created_username,''),IfNull(la.lead_status,'')) LIKE %?1% ")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(la.lead_assignment_id as id,la.lead_status as lead_status,la.user_id as user_id,"
			+ "la.candidate_id as candidate_id,la.created_date as created_date,"
			+ "la.modified_date as modified_date,la.created_by as created_by,la.modified_by as modified_by,la.active as active,"
			+ "la.created_username as created_username,la.modified_username as modified_username) "
			+ "from LeadAssignment la ")
	public Page<Object> findAll2(Pageable pageable);
	
	@Modifying
	@Query(value = "update LeadAssignment la set la.active=false where la.lead_assignment_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update LeadAssignment la set la.active=true where la.lead_assignment_id=?1")
	public void update1(Integer id);

	@Query(value = "select count(la.candidate_id) from lead_assignment la where la.user_id=?1 and la.active=true", nativeQuery=true)
	public Integer getLeadAssignmentCount(Integer id);
	
	@Query(value = "select new map(cw.username as username,cw.program_assignment_id as program_assignment_id,cw.mail_sent_date as mail_sent_date, cw.npf_status as npf_status,cw.active as active,cw.candidate_email as candidate_email,sas.is_verified as is_verified,"
			+ "cw.application_no_npf as application_no_npf,cw.candidate_id as id,cw.created_date as created_date,cw.active as active,"
			+ "la.lead_status as lead_status1,ud.username as counselor_name,la.user_id as counselor_id,"
			+ "cw.created_by as created_by,cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
			+ "cw.created_username as created_username,cw.candidate_name as candidate_name, sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,cw.mobile_number as mobile_number,u.username as username,"
			+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,cw.lead_status as lead_status) "
			+ "from LeadAssignment la "
			+ "left join Candidate_Walkin cw on la.candidate_id=cw.candidate_id "
			+ "left join Schools sc on cw.school_id=sc.school_id "
			+ "left join Student_Details sd on sd.candidate_id=cw.candidate_id "
			+ "left join Program pr on cw.program_id=pr.program_id "
			+ "left join PreAdmissionProcess pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join ScholarshipApprovalStatus sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join UserAuthentication u on pap.created_by=u.id "
			+ "left join ProgramSpecilization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join LeadAssignment la on la.candidate_id=cw.candidate_id "
			+ "left join UserAuthentication ud on ud.id=la.user_id "
			+ "where sd.auid IS NULL")
	public List<Map<String, Object>> getLeadsAssignedToUser(Integer user_id);
	
	@Modifying
	@Query(value = "update LeadAssignment la set la.user_id=?1 where la.candidate_id=?2 And la.active=true")
	public void updateCounselorInLeadAssignment(Integer user_id, Integer candidate_id);

}
