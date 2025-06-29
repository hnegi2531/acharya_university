package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.StudentIdCardHistory;

@Repository
@Transactional
public interface StudentIdCardHistoryRepository extends JpaRepository<StudentIdCardHistory,Integer> {

	@Query(value="select new map(sich.student_id_card_history_id as student_id_card_history_id,sich.remarks as remarks,sich.receipt_no as receipt_no,sich.receipt_date as receipt_date,"
			+ "sich.valid_till as valid_till,sich.current_year as history_current_year,sich.created_date as created_date,sich.created_username as created_username,sd.student_id as student_id,"
			+ "sd.student_name as student_name, sd.auid as auid, sd.mobile as mobile,"
			+ "sd.usn as usn, sd.date_of_admission as date_of_admission, rs.reporting_date as reporting_date, rs.current_year as current_year, rs.current_sem as current_sem,"
			+ "sch.display_name as display_name,sd.student_image_path as studentImagePath,sch.school_name_short as schoolNameShort,"
			+ "concat(pr.display_name, '-',ps.program_specialization_short_name) as programWithSpecialization,sch.school_id as schoolId,sicb.student_id_card_bucket_id as studentIdCardBucketId) From StudentIdCardHistory sich "
			+ "Inner join Student_Details sd on sd.student_id = sich.student_id "
			+ "left join ReportingStudents rs on rs.student_id = sd.student_id "
			+ "left join Schools sch on sch.school_id = sd.school_id "
			+ "left join Program pr on sd.program_id = pr.program_id " 
			+ "left join StudentIdCardBucket sicb on sicb.studentIdCardHistory = sich.student_id_card_history_id And sicb.active=true "
		    + "left join ProgramSpecilization ps on sd.program_specialization_id = ps.program_specialization_id ")
	List<HashMap<String, Object>> studentIdCardHistoryDetails();

}
