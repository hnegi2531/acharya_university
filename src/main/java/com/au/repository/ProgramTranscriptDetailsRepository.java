package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.ProgramTranscriptDetails;

@Repository
@Transactional
public interface ProgramTranscriptDetailsRepository  extends JpaRepository<ProgramTranscriptDetails, Integer>{


//	@Query(value="SELECT new map(pt.created_username as created_username,ptd.transcript as transcript,"
//			+ "ptd.transcript_short_name as transcript_short_name,"
//			+ "pt.transcript_id as transcript_id,pt.trans_id as trans_id,pt.program_id as program_id,"
//			+ "pt.active as active,pt.created_date as created_date,pt.created_by as created_by,"
//			+ "pt.foriegn_status as foriegn_status,p.program_name as program_name,"
//			+ "p.program_short_name as program_short_name)from ProgramTranscriptDetails pt "
//			+ "left join Program p on pt.program_id = p.program_id left join ProgramTranscript ptd on pt.trans_id=ptd.trans_id")
//	public List<HashMap<String, Object>> getProgramTranscriptDetails();
	
	@Query(value = "SELECT new map(pt.transcript_id as transcript_id,pt.trans_id as trans_id,pt.program_id as program_id,ptd.show_status As show_status,"
			+ "pt.is_submitted As is_submitted,pt.fee_admission_sub_category_id As fee_admission_sub_category_id,fac.fee_admission_category_id As fee_admission_category_id,"
			+ "fac.fee_admission_category_short_name As fee_admission_category_short_name,fac.fee_admission_category_type As fee_admission_category_type,"
			+ "fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "pt.created_username as created_username,pt.active as active,pt.created_date as created_date,pt.created_by as created_by,sts.stu_transcript_id as stu_transcript_id,"
			+ "ptd.transcript as transcript,ptd.transcript_short_name as transcript_short_name)"
			+ "FROM ProgramTranscriptDetails pt "
			+ "left join ProgramTranscript ptd on pt.trans_id=ptd.trans_id "
			+ "left join FeeAdmissionSubCategory fasc on pt.fee_admission_sub_category_id = fasc.fee_admission_sub_category_id "
			+ "left join FeeAdmissionCategory fac on fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join StudentTranscriptSubmission sts on sts.transcript_id=pt.transcript_id "
			+ "where pt.program_id like ?1 and pt.active=true Group by pt.transcript_id")
	public List<HashMap<String, Object>> fetchingData(Integer program_id);
	
	@Query(value="SELECT new map(pt.created_username as created_username,ptd.transcript as transcript,"
			+ "ptd.transcript_short_name as transcript_short_name,"
			+ "pt.is_submitted As is_submitted,pt.fee_admission_sub_category_id As fee_admission_sub_category_id,fac.fee_admission_category_id As fee_admission_category_id,"
			+ "fac.fee_admission_category_short_name As fee_admission_category_short_name,fac.fee_admission_category_type As fee_admission_category_type,"
			+ "pt.transcript_id as id,pt.trans_id as trans_id,pt.program_id as program_id,"
			+ "fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "pt.active as active,pt.created_date as created_date,pt.created_by as created_by,p.program_name as program_name,"
			+ "p.program_short_name as program_short_name) from ProgramTranscriptDetails pt "
			+ "left join Program p on pt.program_id = p.program_id "
			+ "left join FeeAdmissionSubCategory fasc on pt.fee_admission_sub_category_id = fasc.fee_admission_sub_category_id "
			+ "left join FeeAdmissionCategory fac on fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join ProgramTranscript ptd on pt.trans_id=ptd.trans_id "
			+ "where CONCAT(IfNull(ptd.transcript,''),'',IfNull(pt.transcript_id,''),'',IfNull(p.program_short_name,''),"
			+ "'',IfNull(p.program_name,''),'',IfNull(pt.created_by,''),'',IfNull(pt.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value="SELECT new map(pt.created_username as created_username,ptd.transcript as transcript,"
			+ "ptd.transcript_short_name as transcript_short_name,"
			+ "pt.is_submitted As is_submitted,pt.fee_admission_sub_category_id As fee_admission_sub_category_id,fac.fee_admission_category_id As fee_admission_category_id,"
			+ "fac.fee_admission_category_short_name As fee_admission_category_short_name,fac.fee_admission_category_type As fee_admission_category_type,"
			+ "pt.transcript_id as id,pt.trans_id as trans_id,pt.program_id as program_id,"
			+ "fasc.fee_admission_sub_category_name As fee_admission_sub_category_name,fasc.fee_admission_sub_category_short_name As fee_admission_sub_category_short_name,"
			+ "pt.active as active,pt.created_date as created_date,pt.created_by as created_by,p.program_name as program_name,"
			+ "p.program_short_name as program_short_name) from ProgramTranscriptDetails pt "
			+ "left join Program p on pt.program_id = p.program_id "
			+ "left join FeeAdmissionSubCategory fasc on pt.fee_admission_sub_category_id = fasc.fee_admission_sub_category_id "
			+ "left join FeeAdmissionCategory fac on fasc.fee_admission_category_id = fac.fee_admission_category_id "
			+ "left join ProgramTranscript ptd on pt.trans_id=ptd.trans_id")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Query(value = "SELECT count(*) FROM ProgramTranscriptDetails where program_id=?1 and trans_id=?2 and active=true")
	public Integer getProgramCountFromTranscriptionDetails(Integer pid, Integer trans_id);

	@Modifying
	@Query(value = "update ProgramTranscriptDetails p set p.active=false where p.transcript_id=?1")	
	public void updateProgramTranscript(Integer id);

	@Modifying
	@Query(value = "update ProgramTranscriptDetails p set p.active=true where p.transcript_id=?1")
	public void updateDept1(Integer id);
	
	@Query(value = "Select p.program_id as program_id,p.program_name as program_name,p.program_short_name as program_short_name from program p "
			+ "where p.program_id NOT IN (Select ptd.program_id from program_transcrption_details ptd where ptd.trans_id=?1) and p.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchUnassignedProgramDetail(Integer trans_id);
}
