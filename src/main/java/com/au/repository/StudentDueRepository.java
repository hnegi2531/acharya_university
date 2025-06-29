package com.au.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.StudentDueEventDTO;
import com.au.dto.StudentFeeDetails;
import com.au.dto.StudentFeeDetailsDTO;
import com.au.dto.StudentFeeTemplateDTO;
import com.au.dto.StudentListWithTotalDues;
import com.au.dto.SumOfAllFromStudentDuesDto;
import com.au.dto.TotalStudentCountAndFeeDueDto;
import com.au.model.StudentDues;
import com.au.model.Student_Details;


@Transactional
public interface StudentDueRepository extends JpaRepository<StudentDues, Integer>{

	@Query(value = "CALL student_due_details(:studentId)", nativeQuery = true)
	StudentDues callStudentDueDetails(@Param("studentId") int studentId);

	@Query(value = " select new com.au.dto.StudentFeeDetails( sd.auid as auid, fr.fee_receipt as feeReceiptNo,"
			+ "fr.created_date as receiptDate, fr.paid_amount as transactionAmount, bit.transaction_date as transactionDate,"
			+ "fr.transaction_type as transaction_type,fr.financial_year_id as financial_year_id,fy.financial_year as financial_year,"
			+ "bit.transaction_no as transaction_no,bit.paid as receiptAmount, bit.cheque_dd_no as chequeDDno ) "
			+ "from Student_Details sd " 
			+ "left join  FeeReceipt fr on fr.student_id=sd.student_id "
			+ "left join  FinancialYear fy on fr.financial_year_id=fy.financial_year_id "
			+ "left join  BankImportTransaction bit on bit.bank_import_transaction_id=fr.bank_transaction_history_id "
			+ "where sd.student_id=:student_id and fr.active=1 ")
	List<StudentFeeDetails> getStudentDueDetails(@Param("student_id") Integer student_id);

	@Query(value = "SELECT new com.au.dto.StudentFeeDetailsDTO( sd.auid as auid, "
			+ "(SELECT COALESCE(SUM(fr.paid_amount), 0) FROM FeeReceipt fr WHERE fr.student_id=sd.student_id) as feePaid, "
			+ "(SELECT COALESCE(std.totalDue, 0) FROM StudentDues std WHERE std.studentId=sd.student_id) as feeDue, "
			+ "COALESCE(sas.approved_amount, 0) as grant ) " + "FROM Student_Details sd "
			+ "LEFT JOIN ScholarshipApprovalStatus sas ON sas.candidate_id=sd.candidate_id "
			+ "WHERE sd.student_id=:studentId", nativeQuery = false)
	StudentFeeDetailsDTO getStudentFeeDetails(@Param("studentId") Integer student_id);

	@Query(value = "select new com.au.dto.TotalStudentCountAndFeeDueDto( p.program_id as programId, p.program_name as programName, count(s.studentId) as studentCount, sum(s.totalDue) as totalDues   ) from StudentDues s left join Program p on p.program_id=s.programId  "
			+ " left join ReportingStudents rs on rs.student_id=s.studentId left join Student_Details sd on rs.student_id=sd.student_id  where p.program_id is not null  and (rs.eligible_reported_status=1 or rs.eligible_reported_status=3) and s.totalDue!=0 and sd.active=1 group by p.program_id  ")
	public List<TotalStudentCountAndFeeDueDto> getTotalStudentAndFeeDueForProgram(Integer programId);

	@Query(value = "select new com.au.dto.TotalStudentCountAndFeeDueDto( count(s.studentId) as studentCount, sum(s.totalDue) as totalDues, p.fee_admission_category_id as categoryId, p.fee_admission_category_type as categoryName, p.fee_admission_category_short_name as categoryShortName  ) from StudentDues s "
			+ "left join FeeAdmissionCategory p on p.fee_admission_category_id=s.categoryId "
			+ "left join ReportingStudents rs on rs.student_id=s.studentId "
			+ " left join Student_Details sd on rs.student_id=sd.student_id "
			+ " where p.fee_admission_category_id is not null   and (rs.eligible_reported_status=1 or rs.eligible_reported_status=3) and s.totalDue!=0 and  sd.active=1 group by p.fee_admission_category_id ")
	List<TotalStudentCountAndFeeDueDto> getTotalStudentAndFeeDueForCategory(Integer categoryId);

	@Query(value = " select new com.au.dto.SumOfAllFromStudentDuesDto( s.categoryId as categoryId, fc.fee_admission_category_type as categoryTypeName, fc.fee_admission_category_short_name as categoryShortName, sum(s.totalDue) as sumOfDue, sum(s.totalFix) as sumOfFixed, sum(s.totalGrant) as sumOfGrant, sum(s.totalPaid) as sumOfPaid,COUNT(s.studentId) as studentCount ) from StudentDues s left join FeeAdmissionCategory fc on fc.fee_admission_category_id=s.categoryId "
			+ "  left join ReportingStudents rs on rs.student_id=s.studentId "
			+ "  left join Student_Details sd on rs.student_id=sd.student_id "
			+ "  where s.categoryId=:categoryId  and (rs.eligible_reported_status=1 or rs.eligible_reported_status=3)  and s.totalDue!=0 and sd.active=1 group by s.categoryId ")
	List<SumOfAllFromStudentDuesDto> getSumOfAllByCategoryId(Integer categoryId);

	@Query(value = "select new com.au.dto.StudentListWithTotalDues( s.auid as auid, s.student_name as studentName, p.program_name as programmeName, s.mobile as phoneNo, sd.totalDue as totalDue ) from StudentDues sd left join Program p on p.program_id=sd.programId "
			+ " left join Student_Details s on s.student_id=sd.studentId left join ReportingStudents rs on rs.student_id=s.student_id where p.program_id=:programId and  sd.totalDue!=0 and (rs.eligible_reported_status=1 or rs.eligible_reported_status=3) and s.active=1 ", nativeQuery = false)
	List<StudentListWithTotalDues> getStudentListWithTotalDues(Integer programId);

	@Query(value = "select new com.au.dto.StudentListWithTotalDues( s.auid as auid, s.student_name as studentName,  s.mobile as phoneNo, sd.totalDue as totalDue,p.fee_admission_category_type as categoryName, p.fee_admission_category_short_name as categoryShortName   ) from StudentDues sd left join FeeAdmissionCategory p on p.fee_admission_category_id=sd.categoryId "
			+ " left join Student_Details s on s.student_id=sd.studentId left join ReportingStudents rs on rs.student_id=s.student_id where p.fee_admission_category_id=:categoryId  and (rs.eligible_reported_status=1 or rs.eligible_reported_status=3) and s.active=1", nativeQuery = false)
	List<StudentListWithTotalDues> getStudentListWithTotalDuesForCategory(@Param("categoryId") Integer categoryId);

	@Query(value="select new com.au.dto.StudentDueEventDTO( s.student_id, s.auid, rs.current_sem, rs.current_year, s.school_id, s.program_id, s.program_specialization_id, s.fee_admission_category_id, s.ac_year_id,  s.fee_template_id  ) from Student_Details s  join ReportingStudents rs on rs.student_id=s.student_id where  s.active=1 order by s.student_id desc ")
	List<StudentDueEventDTO> getStudentDueEventDTODetails();

	@Query(value="select st from StudentDues st where st.studentId=:studentId")
	StudentDues getBySId(@Param("studentId") Integer studentId);

	@Query(value=" select new  com.au.dto.StudentFeeTemplateDTO("
			+ "ft.fee_template_name, ac.ac_year, s.school_name, p.program_short_name, ps.program_specialization_short_name,"
			+ " fc.fee_admission_category_short_name, p.program_id, ps.program_specialization_id, ac.ac_year_id,s.school_id, ft.fee_template_id    "
			+ ") from Student_Details sd  "
			+ " left join FeeTemplate ft on ft.fee_template_id=sd.fee_template_id"
			+ " left join Program p on p.program_id=sd.program_id"
			+ " left join ProgramSpecilization ps on ps.program_specialization_id=sd.program_specialization_id "
			+ " left join Academic_year ac on ac.ac_year_id=sd.ac_year_id "
			+ " left join Schools s on s.school_id=sd.school_id "
			+ " left join FeeAdmissionCategory fc on fc.fee_admission_category_id=sd.fee_admission_category_id "
			+ " where sd.student_id=:studentId  ")
	StudentFeeTemplateDTO getStudentDetails(Integer studentId);


	@Query(value="select SUM(st.totalDue) from StudentDues st where st.schoolId=:schoolId group by st.schoolId")
	public Double getSumOfSchoolDue(Integer schoolId);

	@Query(value=" select sum(s1due) as sem1,sum(s2due) as sem2,sum(s3due) as sem3,sum(s4due) as sem4,sum(s5due) as sem5 ,sum(s6due) as sem6,sum(s7due) as sem7,sum(s8due) as sem8,sum(s9due) as sem9,sum(s10due) as sem10,sum(s11due) as sem11,sum(s12due) as sem12 from  StudentDues st where st.programSpecializationId=:programSpecializationId group by st.programSpecializationId ")
	Map<String, Object> getProgramSpecializationWiseSemesterSum(Integer programSpecializationId);

	@Query(value=" select sd from StudentDues sd where sd.studentId=:studentId ")
	StudentDues getStudentDueDetailsByStudentId(Integer studentId);

	@Query(value="select new com.au.dto.StudentDueEventDTO( s.student_id, s.auid, rs.current_sem, rs.current_year, s.school_id, s.program_id, s.program_specialization_id, s.fee_admission_category_id, s.ac_year_id, s.fee_template_id  ) from Student_Details s join ReportingStudents rs on rs.student_id=s.student_id where  s.active=1 and s.school_id=:schoolId order by s.student_id desc ")
	List<StudentDueEventDTO> getStudentDueEventDTODetailsBySchoolId(Integer schoolId);

	@Query(value="select new com.au.dto.StudentDueEventDTO( s.student_id, s.auid, rs.current_sem, rs.current_year, s.school_id, s.program_id, s.program_specialization_id, s.fee_admission_category_id, s.ac_year_id, s.fee_template_id  ) from Student_Details s  join ReportingStudents rs on rs.student_id=s.student_id where  s.active=1 and s.program_id=:programId order by s.student_id desc ")
	List<StudentDueEventDTO> getStudentDueEventDTODetailsByProgramId(Integer programId);

	@Query(value="select new com.au.dto.StudentDueEventDTO( s.student_id, s.auid, rs.current_sem, rs.current_year, s.school_id, s.program_id, s.program_specialization_id, s.fee_admission_category_id, s.ac_year_id, s.fee_template_id  ) from Student_Details s join ReportingStudents rs on rs.student_id=s.student_id where  s.active=1 and s.student_id=:studentId ")
	List<StudentDueEventDTO> getStudentDueEventDTODetailsByStudentId(Integer studentId);

//
//	@Query(value = "SELECT "
//			+ "CASE "
//			+ "WHEN :current_sem = 1 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0)) "
//			+ "WHEN :current_sem = 2 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0)) "
//			+ "WHEN :current_sem = 3 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0)) "
//			+ "WHEN :current_sem = 4 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s4adondue, 0)) "
//			+ "WHEN :current_sem = 5 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s4adondue, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s5adondue, 0)) "
//			+ "WHEN :current_sem = 6 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s4adondue, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s5adondue, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s6adondue, 0)) "
//			+ "WHEN :current_sem = 7 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s4adondue, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s5adondue, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s6adondue, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s7adondue, 0)) "
//			+ "WHEN :current_sem = 8 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s4adondue, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s5adondue, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s6adondue, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s7adondue, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s8adondue, 0)) "
//			+ "WHEN :current_sem = 9 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s4adondue, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s5adondue, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s6adondue, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s7adondue, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s8adondue, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s9adondue, 0)) "
//			+ "WHEN :current_sem = 10 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s4adondue, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s5adondue, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s6adondue, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s7adondue, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s8adondue, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s9adondue, 0) + COALESCE(sd.s10due, 0) + COALESCE(sd.s10adondue, 0)) "
//			+ "WHEN :current_sem = 11 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s4adondue, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s5adondue, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s6adondue, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s7adondue, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s8adondue, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s9adondue, 0) + COALESCE(sd.s10due, 0) + COALESCE(sd.s10adondue, 0) + COALESCE(sd.s11due, 0) + COALESCE(sd.s11adondue, 0)) "
//			+ "WHEN :current_sem = 12 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s1adondue, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s2adondue, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s3adondue, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s4adondue, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s5adondue, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s6adondue, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s7adondue, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s8adondue, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s9adondue, 0) + COALESCE(sd.s10due, 0) + COALESCE(sd.s10adondue, 0) + COALESCE(sd.s11due, 0) + COALESCE(sd.s11adondue, 0) + COALESCE(sd.s12due, 0) + COALESCE(sd.s12adondue, 0)) "
//			+ "ELSE 0 END AS dueAmount "
//			+ "FROM StudentDues sd WHERE sd.studentId = :student_id")

	@Query(value = "SELECT "
			+ "CASE "
			+ "WHEN :current_sem = 1 THEN (COALESCE(sd.s1due, 0)) "
			+ "WHEN :current_sem = 2 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0)) "
			+ "WHEN :current_sem = 3 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0)) "
			+ "WHEN :current_sem = 4 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0)) "
			+ "WHEN :current_sem = 5 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0)) "
			+ "WHEN :current_sem = 6 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0)) "
			+ "WHEN :current_sem = 7 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0)) "
			+ "WHEN :current_sem = 8 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0)) "
			+ "WHEN :current_sem = 9 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s9due, 0)) "
			+ "WHEN :current_sem = 10 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s10due, 0)) "
			+ "WHEN :current_sem = 11 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s10due, 0) + COALESCE(sd.s11due, 0)) "
			+ "WHEN :current_sem = 12 THEN (COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s10due, 0) + COALESCE(sd.s11due, 0) + COALESCE(sd.s12due, 0)) "
			+ "ELSE 0 END AS dueAmount "
			+ "FROM StudentDues sd WHERE sd.studentId = :student_id")
	Double totalDueTillCurrentSemOfStudent(Integer current_sem, Integer student_id);


	@Query(value = "select * from student_dues WHERE student_id = :studentId", nativeQuery = true)
	StudentDues checkDataPresentOrNot(Integer studentId);
	
	@Query(value="SELECT CASE "
	        + "WHEN :semDue = 's1due' THEN s1due "
	        + "WHEN :semDue = 's2due' THEN s2due "
	        + "WHEN :semDue = 's3due' THEN s3due "
	        + "WHEN :semDue = 's4due' THEN s4due "
	        + "WHEN :semDue = 's5due' THEN s5due "
	        + "WHEN :semDue = 's6due' THEN s6due "
	        + "WHEN :semDue = 's7due' THEN s7due "
	        + "WHEN :semDue = 's8due' THEN s8due "
	        + "WHEN :semDue = 's9due' THEN s9due "
	        + "WHEN :semDue = 's10due' THEN s10due "
	        + "WHEN :semDue = 's11due' THEN s11due "
	        + "WHEN :semDue = 's12due' THEN s12due "
	        + "END AS dueAmount "
	        + "FROM student_dues "
	        + "WHERE student_id = :studentId", nativeQuery = true)
	Float checkDuesOnSemForGeneratingNoDues(Integer studentId, String semDue);

	@Query(value = "select s1adondue, s2adondue, s3adondue, s4adondue, s5adondue, s6adondue, s7adondue, s8adondue, s9adondue, s10adondue,"
			+ " s11adondue, s12adondue  from student_dues WHERE student_id = :studentId", nativeQuery = true)
	public Map<String, Object> getSemesterAddOnDue(Integer studentId);


	@Query(value = 
		    "SELECT " +
		    "sd.student_id, " +
		    "CASE " +
		    "WHEN :sem = 1 THEN COALESCE(sd.s1due, 0) " +
		    "WHEN :sem = 2 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) " +
		    "WHEN :sem = 3 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) " +
		    "WHEN :sem = 4 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) " +
		    "WHEN :sem = 5 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) " +
		    "WHEN :sem = 6 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) " +
		    "WHEN :sem = 7 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) " +
		    "WHEN :sem = 8 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0) " +
		    "WHEN :sem = 9 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s9due, 0) " +
		    "WHEN :sem = 10 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s10due, 0) " +
		    "WHEN :sem = 11 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s10due, 0) + COALESCE(sd.s11due, 0) " +
		    "WHEN :sem = 12 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) + COALESCE(sd.s5due, 0) + COALESCE(sd.s6due, 0) + COALESCE(sd.s7due, 0) + COALESCE(sd.s8due, 0) + COALESCE(sd.s9due, 0) + COALESCE(sd.s10due, 0) + COALESCE(sd.s11due, 0) + COALESCE(sd.s12due, 0) " +
		    "ELSE 0 END AS total_due " +
		    "FROM student_dues sd " +
		    "WHERE sd.student_id IN (:studentIds)",
		    nativeQuery = true)
		List<Map<String, Object>> getTotalDueForSemesterStudents(@Param("studentIds") List<Integer> studentIds,
		                                                         @Param("sem") Integer sem);

	@Query(value = 
		    "SELECT " +
		    "sd.student_id, " +
		    "CASE " +
		    "WHEN :year = 1 THEN COALESCE(sd.s1due, 0) " +
		    "WHEN :year = 2 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) " +
		    "WHEN :year = 3 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) " +
		    "WHEN :year = 4 THEN COALESCE(sd.s1due, 0) + COALESCE(sd.s2due, 0) + COALESCE(sd.s3due, 0) + COALESCE(sd.s4due, 0) " +
		    "ELSE 0 END AS total_due " +
		    "FROM student_dues sd " +
		    "WHERE sd.student_id IN (:studentIds)",
		    nativeQuery = true)
		List<Map<String, Object>> getTotalDueForYearStudents(@Param("studentIds") List<Integer> studentIds,
		                                                     @Param("year") Integer year);

	@Query(value=" select sum(s1adondue) as sem1,sum(s2adondue) as sem2,sum(s3adondue) as sem3,sum(s4adondue) as sem4,sum(s5adondue) as sem5 ,sum(s6adondue) as sem6,sum(s7adondue) as sem7,sum(s8adondue) as sem8,sum(s9adondue) as sem9,sum(s10adondue) as sem10,sum(s11adondue) as sem11,sum(s12adondue) as sem12 from  StudentDues st where st.programSpecializationId=:programSpecializationId group by st.programSpecializationId ")
	public Map<String, Object> getProgramSpecializationWiseSemesterAddOnSum(Integer programSpecializationId);


}
