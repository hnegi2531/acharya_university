package com.au.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.ExamHallTicket;

@Repository
@Transactional
public interface ExamHallTicketRepository extends JpaRepository<ExamHallTicket, Integer>{

	
	@Query(value = "SELECT eht from ExamHallTicket eht where eht.active=true")
	public List<ExamHallTicket> findAll11();
	
	
	@Query(value = "select count(*) from ExamHallTicket eht where eht.candidate_id=?1 and eht.active=true")
	public Integer getcountOfHallTicket(Integer candidate_id);
	
	@Query(value = "select eht.exam_result from exam_hall_ticket eht where eht.candidate_id=?1 and eht.active=true "
			+ "order by eht.exam_hall_ticket_id desc limit 1",nativeQuery=true)
	public String checkCandidateExamResult(Integer candidate_id);
	
	@Query(value = "select new map(eht.exam_hall_ticket_id as id,eht.application_number as application_number,"
			+ "eht.exam_details_id as exam_details_id,eht.created_username as created_username,eht.modified_username as modified_username,"
			+ "eht.created_date as created_date,eht.modified_date as modified_date,eht.created_by as created_by,"
			+ "cw.candidate_name as candidate_name,ed.exam_date as exam_date,ed.exam_center as exam_center,"
			+ "eht.exam_result as exam_result,eht.candidate_id as candidate_id,eht.duration as duration,sd.passport_no as passport_no,"
			+ "eht.modified_by as modified_by,eht.active as active) from ExamHallTicket eht "
			+ "left join Candidate_Walkin cw on cw.candidate_id = eht.candidate_id "
			+ "left join Student_Details sd on sd.candidate_id = eht.candidate_id "
			+ "left join ExamDetails ed on ed.exam_details_id = eht.exam_details_id "
			+ "where CONCAT(IfNull(eht.created_username,''),'',IfNull(eht.application_number,''),'',IfNull(eht.candidate_id,''),'',"
			+ "'',IfNull(eht.created_by,''),'',IfNull(eht.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(eht.exam_hall_ticket_id as id,eht.application_number as application_number,"
			+ "eht.exam_details_id as exam_details_id,eht.created_username as created_username,eht.modified_username as modified_username,"
			+ "eht.created_date as created_date,eht.modified_date as modified_date,eht.created_by as created_by,"
			+ "cw.candidate_name as candidate_name,ed.exam_date as exam_date,ed.exam_center as exam_center,"
			+ "eht.exam_result as exam_result,eht.candidate_id as candidate_id,eht.duration as duration,sd.passport_no as passport_no,"
			+ "eht.modified_by as modified_by,eht.active as active) from ExamHallTicket eht "
			+ "left join Candidate_Walkin cw on cw.candidate_id = eht.candidate_id "
			+ "left join Student_Details sd on sd.candidate_id = eht.candidate_id "
			+ "left join ExamDetails ed on ed.exam_details_id = eht.exam_details_id")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Modifying
	@Query(value = "update ExamHallTicket eht set eht.active=false where eht.exam_hall_ticket_id=?1")
	public void updateExamHallTickets(Integer id);
	
	@Modifying
	@Query(value = "update ExamHallTicket eht set eht.active=true where eht.exam_hall_ticket_id=?1")
	public void updateExamHallTickets1(Integer id);
	
	
	@Query(value = "select cw.candidate_id as candidate_id,cw.application_no_npf as application_no_npf,"
			+ "concat(cw.candidate_name,'-',cw.application_no_npf) as candidate_name from Candidate_Walkin cw")
	public List<Map<String,Object>> getCandidateNameConcatWithApplicationNumber();
	

	@Query(value = "select cw.candidate_name as candidate_name,cw.candidate_last_name as candidate_last_name,cw.photo as photo,"
			+ "cwa.attachment_path as attachment_path,eht.duration as duration,cwa.attachment_purpose as attachment_purpose,"
			+ "it.internal_name as internal_name,it.internal_short_name as internal_short_name,"
			+ "eht.application_number as application_number,ed.exam_date as exam_date,ed.address as address "
			+ "from exam_hall_ticket eht "
			+ "left join candidate_walkin cw on cw.candidate_id = eht.candidate_id "
			+ "left join candidate_walkin_attachments cwa on cwa.candidate_id = eht.candidate_id "
			+ "left join exam_details ed on ed.exam_details_id = eht.exam_details_id "
			+ "left join internal_types it on it.internal_master_id = ed.internal_master_id "
			+ "where eht.application_number=?1 and (cwa.attachment_purpose LIKE '%photo%' or cwa.attachment_purpose LIKE '%image%')"
			+ " and eht.active=true",nativeQuery = true)
	public List<Map<String,Object>> getAdmitCardDetail(String application_number);
	
	
}

