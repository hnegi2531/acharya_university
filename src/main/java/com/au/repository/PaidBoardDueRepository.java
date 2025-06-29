package com.au.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.au.model.PaidBoardDue;
import com.au.model.StudentDues;

@Transactional
@Repository
public interface PaidBoardDueRepository extends JpaRepository<PaidBoardDue, Integer>{

	@Query(value="Select brd.board_unique_name as board_unique_name,"
			+ "brd.board_unique_short_name as board_unique_short_name,"
			+ "sum(to_pay) as toPay,sum(received) as received,sum(balance) as balance,"
			+ "pbd.board_unique_id as board_unique_id  From paid_board_due pbd "
			+ "Inner Join board brd On brd.board_unique_id=pbd.board_unique_id "
			+ "group by pbd.board_unique_id",nativeQuery=true)
	List<Map<String, Object>> paidBoardReportBasedOnFeeAdmissionCategory();
	
	@Query(value="Select sch.school_id as school_id, sch.school_name as school_name,"
			+ "sum(to_pay) as toPay,sum(received) as received,sum(balance) as balance,"
			+ "pbd.board_unique_id as board_unique_id  From paid_board_due pbd "
			+ "Inner Join student_details sd On sd.student_id=pbd.student_id "
			+ "Left Join schools sch On sch.school_id=sd.school_id "
			+ "Where pbd.board_unique_id=:boardUniqueId group by sd.school_id ",nativeQuery=true)
	List<Map<String, Object>> paidBoardReportBasedOnSchoolByBoard(Integer boardUniqueId);
	
	@Query(value="Select sch.school_id as school_id, sch.school_name as school_name,"
			+ "sum(to_pay) as toPay,sum(received) as received,sum(balance) as balance,"
			+ "pbd.board_unique_id as board_unique_id,"
			+ "sd.ac_year_id as ac_year_id,ac.ac_year as ac_year From paid_board_due pbd "
			+ "Inner Join student_details sd On sd.student_id=pbd.student_id "
			+ "Left Join schools sch On sch.school_id=sd.school_id "
			+ "Left Join academic_year ac On ac.ac_year_id=sd.ac_year_id "
			+ "Where pbd.board_unique_id=:boardUniqueId And sd.school_id=:schoolId group by sd.ac_year_id ",nativeQuery=true)
	List<Map<String, Object>> paidBoardReportBasedOnAcademicYearByBoardAndSchool(Integer boardUniqueId,
			Integer schoolId);
	@Query(value="Select sch.school_id as school_id, sch.school_name as school_name,"
			+ "pbd.to_pay as toPay,pbd.received as received,pbd.balance as balance,"
			+ "sd.student_name as student_name,sd.auid as auid,sd.usn as usn,rs.current_year As current_year,rs.current_sem As current_sem,"
			+ "sd.acharya_email as acharya_email,sd.program_id as program_id,sd.program_specialization_id as program_specialization_id,"
			+ "sd.program_assignment_id as program_assignment_id,pt.program_type_name As program_type_name,pt.program_type_code As program_type_code,"
			+ "pbd.board_unique_id as board_unique_id,bo.board_unique_name As board_unique_name,bo.board_unique_short_name As board_unique_short_name,"
			+ "fac.fee_admission_category_id As fee_admission_category_id,fac.fee_admission_category_type As fee_admission_category_type,fac.fee_admission_category_short_name As fee_admission_category_short_name," 
			+ "sd.ac_year_id as ac_year_id,ac.ac_year as ac_year,pr.program_name as program_name,"
			+ "pr.program_short_name as program_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "ps.program_specialization_short_name as program_specialization_short_name From paid_board_due pbd "
			+ "Inner Join student_details sd On sd.student_id=pbd.student_id "
			+ "Inner Join reporting_students rs On sd.student_id=rs.student_id "
			+ "left join fee_admission_category fac on fac.fee_admission_category_id=sd.fee_admission_category_id "
			+ "Left Join schools sch On sch.school_id=sd.school_id "
			+ "Left Join board bo On bo.board_unique_id=pbd.board_unique_id "
			+ "Left Join academic_year ac On ac.ac_year_id=sd.ac_year_id "
			+ "Left Join program pr On pr.program_id=sd.program_id "
			+ "Left Join program_specialization ps On ps.program_specialization_id=sd.program_specialization_id "
			+ "Left Join program_assignment pa On pa.program_assignment_id=sd.program_assignment_id "
			+ "Left Join program_type pt On pa.program_type_id=pt.program_type_id "
			+ "Where pbd.board_unique_id=:boardUniqueId And sd.school_id=:schoolId And sd.ac_year_id=:academicYearId ",nativeQuery=true)
	List<Map<String, Object>> studentDetailsByBoardSchoolAcademicYear(Integer boardUniqueId, Integer schoolId,
			Integer academicYearId);
	
	@Query(value = "CALL paid_board_due_by_student_id(:studentId)", nativeQuery = true)
	void callStudentPaidBoardDue(@Param("studentId") int studentId);

}
