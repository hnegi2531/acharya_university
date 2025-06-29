package com.au.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.CandidateFollowUp;



@Transactional
@Repository
public interface CandidateFollowUpRepository extends JpaRepository<CandidateFollowUp, Integer>{
	
	@Query(value = "select cfu from CandidateFollowUp cfu where cfu.active=true")
    public List<CandidateFollowUp> findAll1();
	
	@Modifying
	@Query(value = "update CandidateFollowUp cfu set cfu.active=false where cfu.candidate_followup_id=?1")
	public void updateCandidateFollowUp(Integer candidate_followup_id);

	@Modifying
	@Query(value = "update CandidateFollowUp cfu set cfu.active=true where cfu.candidate_followup_id=?1")
	public void updateCandidateFollowUp1(Integer candidate_followup_id);
	
	
	@Query(value = "select new map(cfu.candidate_followup_id as id,cfu.candidate_id as candidate_id,cw.candidate_name as candidate_name,"
			+ "cfu.follow_up_remarks as follow_up_remarks,cfu.follow_up_date as follow_up_date,"
			+ "cfu.created_date as created_date,cfu.created_by as created_by,cfu.created_username as created_username,"
			+ "cfu.modified_date as modified_date,cfu.modified_username as modified_username,cfu.active as active,"
			+ "cfu.modified_by as modified_by) "
			+ "from CandidateFollowUp cfu left join Candidate_Walkin cw on cfu.candidate_id=cw.candidate_id "
            + "where CONCAT(IfNull(cfu.candidate_followup_id,''),'',IfNull(cfu.candidate_id,''),"
			+ "'',IfNull(cfu.follow_up_remarks,''),'',IfNull(cfu.follow_up_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(cfu.candidate_followup_id as id,cfu.candidate_id as candidate_id,cw.candidate_name as candidate_name,"
			+ "cfu.follow_up_remarks as follow_up_remarks,cfu.follow_up_date as follow_up_date,"
			+ "cfu.created_date as created_date,cfu.created_by as created_by,cfu.created_username as created_username,"
			+ "cfu.modified_date as modified_date,cfu.modified_username as modified_username,cfu.active as active,"
			+ "cfu.modified_by as modified_by) "
			+ "from CandidateFollowUp cfu left join Candidate_Walkin cw on cfu.candidate_id=cw.candidate_id")
   public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Query(value = "select cfu from CandidateFollowUp cfu where cfu.candidate_id=?1 and cfu.active=true")
    public List<CandidateFollowUp> getData(Integer candidate_id);
    
//    @Query(value = "select * from candidate_follow_up cfu where Date(cfu.follow_up_date)=STR_TO_DATE(:follow_up_date, '%Y-%m-%d') "
//    		+ "and cfu.active=true",nativeQuery=true)
//    public List<CandidateFollowUp> getDataByFollowUpDate(String follow_up_date);
    
    
    @Query(value ="select cw.username as username,cw.application_status as application_status,"
    		+ "cw.program_assignment_id as program_assignment_id,cw.exam_date as exam_date,"
    		+ "cw.mail_sent_date as mail_sent_date, cw.npf_status as npf_status,cw.candidate_email as candidate_email,"
    		+ "cw.application_no_npf as application_no_npf,"
    		+ "cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
    		+ "sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
    		+ "ps.program_specialization_short_name as program_specialization_short_name,cw.mobile_number as mobile_number,"
    		+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,"
    		+ "cw.form_filled_percentage as form_filled_percentage,sas.is_verified as is_verified,cw.result_status as result_status,"
    		+ "cr.result as result,cr.percentage as percentage,cfu.modified_by as modified_by,"
    		+ "cfu.candidate_followup_id as id,cfu.candidate_id as candidate_id,cw.candidate_name as candidate_name,"
			+ "cfu.follow_up_remarks as follow_up_remarks,cfu.follow_up_date as follow_up_date,"
			+ "cfu.created_date as created_date,cfu.created_by as created_by,cfu.created_username as created_username,"
			+ "cfu.modified_date as modified_date,cfu.modified_username as modified_username,cfu.active as active "
			+ "from candidate_follow_up cfu "
			+ "left join candidate_walkin cw on cfu.candidate_id=cw.candidate_id "
			+ "left join schools sc on cw.school_id=sc.school_id "
			+ "left join student_details sd on sd.candidate_id=cw.candidate_id "
			+ "left join program pr on cw.program_id=pr.program_id "
			+ "left join preadmission_process pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join scholarship_approved_status sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join user_details u on pap.created_by=u.id "
			+ "left join program_specialization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join candidate_results cr on cr.application_no=cw.application_no_npf "
			+ "WHERE date(cfu.follow_up_date) between date(:from_date) And date(:to_date) and cfu.active=true ",nativeQuery=true)
    public List<Map<String, Object>> fetchCandidateFollowUpData(Date from_date, Date to_date);
    
    @Query(value ="select cw.username as username,cw.application_status as application_status,"
    		+ "cw.program_assignment_id as program_assignment_id,cw.exam_date as exam_date,"
    		+ "cw.mail_sent_date as mail_sent_date, cw.npf_status as npf_status,cw.candidate_email as candidate_email,"
    		+ "sas.is_verified as is_verified,cw.form_filled_percentage as form_filled_percentage,"
    		+ "cw.application_no_npf as application_no_npf,"
    		+ "cw.link_exp as link_exp,cw.counselor_status as counselor_status,sas.pre_approval_status as pre_approval_status,"
    		+ "sc.school_name_short as school_name_short,pr.program_short_name as program_short_name,"
    		+ "ps.program_specialization_short_name as program_specialization_short_name,cw.mobile_number as mobile_number,"
    		+ "pap.is_scholarship as is_scholarship,sas.is_approved as is_approved,pap.fee_template_id as fee_template_id,"
    		+ "cw.result_status as result_status,cr.result as result,cr.percentage as percentage,cfu.modified_by as modified_by,"
    		+ "cfu.candidate_followup_id as id,cfu.candidate_id as candidate_id,cw.candidate_name as candidate_name,"
			+ "cfu.follow_up_remarks as follow_up_remarks,cfu.follow_up_date as follow_up_date,"
			+ "cfu.created_date as created_date,cfu.created_by as created_by,cfu.created_username as created_username,"
			+ "cfu.modified_date as modified_date,cfu.modified_username as modified_username,cfu.active as active from candidate_follow_up cfu "
			+ "left join candidate_walkin cw on cfu.candidate_id=cw.candidate_id "
			+ "left join schools sc on cw.school_id=sc.school_id "
			+ "left join student_details sd on sd.candidate_id=cw.candidate_id "
			+ "left join program pr on cw.program_id=pr.program_id "
			+ "left join preadmission_process pap on cw.candidate_id=pap.candidate_id and pap.active=true "
			+ "left join scholarship_approved_status sas on cw.candidate_id=sas.candidate_id and sas.active=true "
			+ "left join user_details u on pap.created_by=u.id "
			+ "left join program_specialization ps on cw.program_specilaization_id = ps.program_specialization_id "
			+ "left join candidate_results cr on cr.application_no=cw.application_no_npf "
			+ "WHERE date(cfu.follow_up_date) between date(:from_date) And date(:to_date) and "
			+ "cfu.created_by=:created_by and cfu.active=true ",nativeQuery=true)
    public List<Map<String, Object>> fetchCandidateFollowUpData1(Integer created_by, Date from_date, Date to_date);
    
}

