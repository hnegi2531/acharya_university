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

import com.au.model.StudentTranscriptSubmission;

@Repository
@Transactional
public interface StudentTranscriptSubmissionRepository extends JpaRepository<StudentTranscriptSubmission, Integer>{

	
	@Query(value = "select new map(sts.stu_transcript_id as stu_transcript_id,sts.will_submit_by as will_submit_by,"
			+ "sts.is_collected as is_collected,sts.transcript_locker_number as transcript_locker_number,sts.submitted_date as submitted_date,"
			+ "sts.created_by as created_by,sts.collected_by_institute as collected_by_institute,sts.not_applicable as not_applicable,"
			+ "sts.will_submit_by as will_submit_by,sts.created_date as created_date,sts.created_username as created_username,"
			+ "prt.transcript as transcript,prt.transcript_short_name as transcript_short_name) from StudentTranscriptSubmission sts "
			+ "inner join ProgramTranscriptDetails td on td.transcript_id=sts.transcript_id "
			+ "left join ProgramTranscript prt on prt.trans_id=td.trans_id "
			+ "where sts.student_id=?1")
	List<HashMap<String, Object>> getTranscriptDetails(Integer student_id);
	
	@Query(value = "select new map(sts.stu_transcript_id as id,sts.transcript_id as transcript_id,sts.student_id as student_id,"
			+ "sts.is_collected as is_collected,sts.created_by as created_by,sts.modified_by as modified_by,"
			+ "sts.created_date as created_date,sts.collected_by_institute as collected_by_institute,sts.active as active,"
			+ "sts.not_applicable as not_applicable,sts.will_submit_by as will_submit_by) "
			+ "from StudentTranscriptSubmission sts "
			+ "where CONCAT(IfNull(sts.stu_transcript_id,''),'',IfNull(sts.not_applicable,''),'',IfNull(sts.student_id,''),"
			+ "'',IfNull(sts.is_collected,''),'',IfNull(sts.created_by,''),'',IfNull(sts.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(sts.stu_transcript_id as id,sts.transcript_id as transcript_id,sts.student_id as student_id,"
			+ "sts.is_collected as is_collected,sts.created_by as created_by,sts.modified_by as modified_by,"
			+ "sts.created_date as created_date,sts.collected_by_institute as collected_by_institute,sts.active as active,"
			+ "sts.not_applicable as not_applicable,sts.will_submit_by as will_submit_by) "
			+ "from StudentTranscriptSubmission sts")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
	@Modifying
	@Query(value = "update StudentTranscriptSubmission sts set sts.student_id=?1 where sts.student_id=?2")
	public void updateOldStudentIdToNewStudentId(Integer newStudentId,Integer oldStudentId);

}
