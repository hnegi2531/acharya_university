package com.au.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.StudentIdCardBucket;

@Repository
@Transactional
public interface StudentIdCardBucketRepository extends JpaRepository<StudentIdCardBucket,Integer>{
	
	@Query(value="select new map(sicb.student_id_card_bucket_id as student_id_card_bucket_id,sicb.remarks as remarks,sicb.receipt_no as receipt_no,sicb.receipt_date as receipt_date,"
			+ "sicb.valid_till as valid_till,sicb.current_year as history_current_year,sicb.created_date as created_date,sicb.created_username as created_username,sd.student_id as student_id,"
			+ "sd.student_name as student_name, sd.auid as auid, sd.mobile as mobile,sicb.valid_till as valid_till,"
			+ "sd.usn as usn, sd.date_of_admission as date_of_admission, rs.reporting_date as reporting_date, rs.current_year as current_year, rs.current_sem as current_sem,"
			+ "sch.display_name as display_name,sd.student_image_path as studentImagePath,sch.school_name_short as schoolNameShort,"
			+ "concat(pr.display_name, '-',ps.program_specialization_short_name) as programWithSpecialization,sch.school_id as schoolId) From StudentIdCardBucket sicb "
			+ "Inner join Student_Details sd on sd.student_id = sicb.student_id "
			+ "left join ReportingStudents rs on rs.student_id = sd.student_id "
			+ "left join Schools sch on sch.school_id = sd.school_id "
			+ "left join Program pr on sd.program_id = pr.program_id " 
		    + "left join ProgramSpecilization ps on sd.program_specialization_id = ps.program_specialization_id Where sicb.active=true ")
	List<HashMap<String, Object>> studentIdCardBucketDetails();

	@Modifying
	@Query(value="Update StudentIdCardBucket sicb Set sicb.active=false Where sicb.student_id_card_bucket_id in ?1 ")
	void deactivateStudentDetailsFromBucket(List<Integer> studentIdCardBucketIds);

}
