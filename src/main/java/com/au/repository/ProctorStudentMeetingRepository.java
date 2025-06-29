package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.ProctorStudentMeeting;

@Repository
@Transactional
public interface ProctorStudentMeetingRepository extends JpaRepository<ProctorStudentMeeting,Integer> {
	
	@Modifying
	@Query(value = "update ProctorStudentMeeting psm set psm.active=false where psm.proctor_student_meeting_id=?1")
	public void updateProctorStudentMeeting(Integer id);

	@Modifying
	@Query(value = "update ProctorStudentMeeting psm set psm.active=true where psm.proctor_student_meeting_id=?1")
	public void updateProctorStudentMeeting1(Integer id);

	
	@Query(value = "select psm.proctor_student_meeting_id as id,psm.meeting_id as meeting_id,psm.chief_proctor_id as chief_proctor_id,"
			+ "psm.proctor_id as proctor_id,psm.emp_id as emp_id,psm.created_by as created_by,psm.modified_by as modified_by,"
			+ "psm.created_date as created_date,psm.modified_date as modified_date,psm.active as active,psm.student_id as student_id,"
			+ "psm.created_username as created_username,psm.modified_username as modified_username,"
			+ "psm.modeof_connect As modeof_connect,psm.student_parent As student_parent,"
			+ "psm.remarks as remarks,psm.date_of_meeting as date_of_meeting,psm.meeting_agenda as meeting_agenda,psm.feedback as feedback,"
			+ "psm.meeting_type as meeting_type,psm.faq_id as faq_id,psm.feedback_date as feedback_date,psm.mode_of_contact as mode_of_contact,"
			+ "psm.parent_name as parent_name,psm.school_id as school_id,sd.auid as auid,sd.usn as usn,sd.student_name as student_name,"
			+ "sc.school_name_short as school_name_short "
			+ "from proctor_student_meeting psm "
			+ "left join student_details sd on sd.student_id = psm.student_id "
			+ "left join schools sc on sc.school_id = psm.school_id "
			+ "left join employee_details ed on ed.emp_id = psm.emp_id "
			+ "where CONCAT(IfNull(psm.proctor_student_meeting_id,''),'',IfNull(psm.created_by,''),'',IfNull(psm.created_date,'')) LIKE %?1%",nativeQuery=true)
	public List<Map<String, Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select psm.proctor_student_meeting_id as id,psm.meeting_id as meeting_id,psm.chief_proctor_id as chief_proctor_id,"
			+ "psm.proctor_id as proctor_id,psm.emp_id as emp_id,psm.created_by as created_by,psm.modified_by as modified_by,"
			+ "psm.created_date as created_date,psm.modified_date as modified_date,psm.active as active,psm.student_id as student_id,"
			+ "psm.created_username as created_username,psm.modified_username as modified_username,"
			+ "psm.modeof_connect As modeof_connect,psm.student_parent As student_parent,"
			+ "CONCAT(ed.employee_name,'-',ed.empcode) as employeeName,"
			+ "psm.remarks as remarks,psm.date_of_meeting as date_of_meeting,psm.meeting_agenda as meeting_agenda,psm.feedback as feedback,"
			+ "psm.meeting_type as meeting_type,psm.faq_id as faq_id,psm.feedback_date as feedback_date,psm.mode_of_contact as mode_of_contact,"
			+ "psm.parent_name as parent_name,psm.school_id as school_id,sd.auid as auid,sd.usn as usn,sd.student_name as student_name,"
			+ "sc.school_name_short as school_name_short "
			+ "from proctor_student_meeting psm "
			+ "left join student_details sd on sd.student_id = psm.student_id "
			+ "left join schools sc on sc.school_id = psm.school_id "
			+ "left join employee_details ed on ed.emp_id = psm.emp_id",nativeQuery=true)
	public List<Map<String, Object>> getAllSortedData(Pageable pageable);
	

	@Query(value = "select psm.proctor_student_meeting_id as id,psm.meeting_id as meeting_id,psm.chief_proctor_id as chief_proctor_id,"
			+ "psm.proctor_id as proctor_id,psm.emp_id as emp_id,psm.created_by as created_by,psm.modified_by as modified_by,"
			+ "psm.created_date as created_date,psm.modified_date as modified_date,psm.active as active,psm.student_id as student_id,"
			+ "psm.created_username as created_username,psm.modified_username as modified_username,"
			+ "psm.modeof_connect As modeof_connect,psm.student_parent As student_parent,"
			+ "psm.remarks as remarks,psm.date_of_meeting as date_of_meeting,psm.meeting_agenda as meeting_agenda,psm.feedback as feedback,"
			+ "psm.meeting_type as meeting_type,psm.faq_id as faq_id,psm.feedback_date as feedback_date,psm.mode_of_contact as mode_of_contact,"
			+ "psm.parent_name as parent_name,psm.school_id as school_id,sd.auid as auid,sd.usn as usn,sd.student_name as student_name,"
			+ "sc.school_name_short as school_name_short "
			+ "from proctor_student_meeting psm "
			+ "left join student_details sd on sd.student_id = psm.student_id "
			+ "left join schools sc on sc.school_id = psm.school_id "
			+ "left join employee_details ed on ed.emp_id = psm.emp_id "
			+ "left join user_details ud on ed.email = ud.email "
			+ "where ud.id=?1 And CONCAT(IfNull(psm.proctor_student_meeting_id,''),'',IfNull(psm.created_by,''),'',IfNull(psm.created_date,'')) LIKE %?1%",nativeQuery=true)
	public List<Map<String, Object>> getAllDataFilteredByKeyword22(Pageable pageable, Object keyword, Integer user_id); 
	
	@Query(value = "select psm.proctor_student_meeting_id as id,psm.meeting_id as meeting_id,psm.chief_proctor_id as chief_proctor_id,"
			+ "psm.proctor_id as proctor_id,psm.emp_id as emp_id,psm.created_by as created_by,psm.modified_by as modified_by,"
			+ "psm.created_date as created_date,psm.modified_date as modified_date,psm.active as active,psm.student_id as student_id,"
			+ "psm.created_username as created_username,psm.modified_username as modified_username,"
			+ "psm.modeof_connect As modeof_connect,psm.student_parent As student_parent,"
			+ "CONCAT(ed.employee_name,'-',ed.empcode) as employeeName,"
			+ "psm.remarks as remarks,psm.date_of_meeting as date_of_meeting,psm.meeting_agenda as meeting_agenda,psm.feedback as feedback,"
			+ "psm.meeting_type as meeting_type,psm.faq_id as faq_id,psm.feedback_date as feedback_date,psm.mode_of_contact as mode_of_contact,"
			+ "psm.parent_name as parent_name,psm.school_id as school_id,sd.auid as auid,sd.usn as usn,sd.student_name as student_name,"
			+ "sc.school_name_short as school_name_short "
			+ "from proctor_student_meeting psm "
			+ "left join student_details sd on sd.student_id = psm.student_id "
			+ "left join schools sc on sc.school_id = psm.school_id "
			+ "left join employee_details ed on ed.emp_id = psm.emp_id "
			+ "left join user_details ud on ed.email = ud.email "
			+ "where ud.id=?1",nativeQuery=true)
	public List<Map<String, Object>> getAllSortedData22(Pageable pageable, Integer user_id);

	@Query(value = "select psm.proctor_student_meeting_id as id,psm.meeting_id as meeting_id,psm.chief_proctor_id as chief_proctor_id,"
			+ "psm.proctor_id as proctor_id,psm.emp_id as emp_id,psm.created_by as created_by,psm.modified_by as modified_by,"
			+ "psm.created_date as created_date,psm.modified_date as modified_date,psm.active as active,psm.student_id as student_id,"
			+ "psm.created_username as created_username,psm.modified_username as modified_username,"
			+ "psm.modeof_connect As modeof_connect,psm.student_parent As student_parent,"
			+ "CONCAT(ed.employee_name,'-',ed.empcode) as employeeName,"
			+ "psm.remarks as remarks,psm.date_of_meeting as date_of_meeting,psm.meeting_agenda as meeting_agenda,psm.feedback as feedback,"
			+ "psm.meeting_type as meeting_type,psm.faq_id as faq_id,psm.feedback_date as feedback_date,psm.mode_of_contact as mode_of_contact,"
			+ "psm.parent_name as parent_name,psm.school_id as school_id,sd.auid as auid,sd.usn as usn,sd.student_name as student_name,"
			+ "sc.school_name_short as school_name_short "
			+ "from proctor_student_meeting psm "
			+ "left join student_details sd on sd.student_id = psm.student_id "
			+ "left join schools sc on sc.school_id = psm.school_id "
			+ "left join employee_details ed on ed.emp_id = psm.emp_id "
			+ "left join user_details ud on ed.email = ud.email "
			+ "where psm.emp_id=?1",nativeQuery=true)
	public List<Map<String, Object>> getAllMailHistoryBasedOnMentor(Integer emp_id);	


}
