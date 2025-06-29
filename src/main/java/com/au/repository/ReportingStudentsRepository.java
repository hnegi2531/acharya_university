package com.au.repository;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.BatchDetailsByReportingStudent;
import com.au.dto.SectionDetailsResponse;
import com.au.model.ReportingStudents;

@Repository
@Transactional
public interface ReportingStudentsRepository extends JpaRepository<ReportingStudents, Integer> {
	
	@Modifying
	@Query(value = "update reporting_students rs set rs.section_id=:section_id where FIND_IN_SET(rs.student_id,:student_ids) > 0 and rs.distinct_status =true and rs.active=true",nativeQuery=true)
	public void updateSectionIdOfReportingStudents(Integer section_id,String student_ids);
	
	@Modifying
	@Query(value = "update reporting_students rs set rs.section_id=null where rs.section_id =?1 and rs.student_id in (?2) and rs.active=true",nativeQuery=true)
	public void updateSectionIdNullOfReportingStudents(Integer section_id,List<Integer> removed_student_ids);
	
//	@Query(value ="select new map(rs.reporting_id as id,rs.current_year_sem as current_year_sem,rs.eligible_reported_status as eligible_reported_status,"
//			+ "rs.remarks as eligible_reported_status,sd.student_name as student_name,sd.auid as auid,sd.usn as usn) from ReportingStudents rs "
//			+ "left join Student_Details sd on rs.student_id=sd.student_id where rs.ac_year_id=?1 and rs.school_id=?2 and rs.program_id=?3 and rs.current_year_sem=?4 and rs.active=true")
//	public List<HashMap<String, Object>> getAllStudentsWithReportStatus(Integer ac_year_id, Integer school_id, Integer program_id, Integer current_year_sem);
	
	@Query(value = "Select rs From ReportingStudents rs where rs.student_id=?1 and rs.active=true")
	public ReportingStudents getDetailsOfReportingStudentsByStudentId(Integer studentId);

    @Query(value ="select new map(rs.reporting_id as reporting_id,rs.created_by as created_by,rs.created_date as created_date,"
    		+ "rs.current_sem as current_sem,rs.current_year as current_year,rs.distinct_status as distinct_status,"
    		+ "rs.previous_sem as previous_sem,rs.previous_year as previous_year,rs.remarks as remarks,"
    		+ "rs.student_id as student_id,sd.auid as auid,sd.usn as usn) from ReportingStudents rs "
    		+ "left join Student_Details sd on sd.student_id=rs.student_id where rs.student_id=?1 And rs.active=true")
	public HashMap<String, Object> getreportingStudentData(Integer student_id);

	@Modifying
	@Query(value = "update reporting_students rs set rs.active=false where rs.student_id =?1 and rs.active=true",nativeQuery=true)
	public void deactivateReporitngStudentTable(Integer student_id);

		@Query(value=" select new com.au.dto.SectionDetailsResponse( sa.section_assignment_id as section_assignment_id,"
			+ " s.section_id as section_id, "
			+ " s.section_name as section_short_name )  from SectionAssignment sa "
			+ " left join Section s on s.section_id=sa.section_id "
			+ " where sa.current_year_sem=:currentyear and sa.section_id=:sectionId and FIND_IN_SET(:studentId,sa.student_ids) > 0 And sa.active=true ")
	public SectionDetailsResponse getCurrentYearSectionDetails(@Param("currentyear") Integer currentyear,@Param("sectionId") Integer sectionId,@Param("studentId") Integer studentId);

	    @Query(value="select new com.au.dto.BatchDetailsByReportingStudent( "
	    		+ "  b.batch_id as batch_id,"
	    		+ "  b.batch_short_name as batch_short_name,"
	    		+ " ba.batch_assignment_id as batch_assignment_id ) from BatchAssignment ba "
	    		+ " left join Batch b on b.batch_id=ba.batch_id "
	    		+ "where ba.current_year=:currentyear and FIND_IN_SET(:studentId,ba.student_ids) > 0 And ba.active=true  ")
		public List<BatchDetailsByReportingStudent> getCurrentYearBatchDetails(@Param("currentyear") Integer currentyear,@Param("studentId") Integer studentId);

	    @Query(value=" select new com.au.dto.SectionDetailsResponse( sa.section_assignment_id as section_assignment_id,"
				+ " sa.section_id as section_id, "
				+ " s.section_name as section_short_name )  from SectionAssignment sa "
				+ " left join Section s on s.section_id=sa.section_id "
				+ " where sa.current_year_sem=:currentsem and sa.section_id=:sectionId and FIND_IN_SET(:studentId,sa.student_ids) > 0 And sa.active=true ")
		public SectionDetailsResponse getCurrentSemSectionDetails(@Param("currentsem") Integer currentsem,@Param("sectionId") Integer sectionId,@Param("studentId") Integer studentId);

		    @Query(value="select new com.au.dto.BatchDetailsByReportingStudent( "
    		+ "  b.batch_id as batch_id,"
    		+ "  b.batch_short_name as batch_short_name,"
    		+ " ba.batch_assignment_id as batch_assignment_id ) from BatchAssignment ba "
    		+ " left join Batch b on b.batch_id=ba.batch_id "
    		+ " where ba.current_sem=:currentsem and FIND_IN_SET(:studentId,ba.student_ids) > 0 And ba.active=true ")
	public List<BatchDetailsByReportingStudent> getCurrentSemBatchDetails(@Param("currentsem") Integer currentsem,@Param("studentId") Integer studentId);

	
    @Query(value = "Select rs.current_sem From reporting_students rs where rs.student_id=?1 and rs.active=true",nativeQuery=true)    
    public Integer getSem(Integer studentId);
   
    @Query(value = "Select rs.current_year From reporting_students rs where rs.student_id=?1 and rs.active=true",nativeQuery=true)  
	public Integer getYear(Integer studentId);
    
    @Modifying
	@Query(value = "update reporting_students rs set rs.current_year=?1,rs.current_sem=?2 where rs.student_id =?3 and rs.active=true",nativeQuery=true)
	public void updateCurrentYearOrSem(Integer currentYear, Integer currentSem, Integer studentId);
    
    
    @Query(value = "Select Case "
			+ "When (date(rs.reporting_date) >= date(:commencementDate)) Then true Else false "
			+ "From reporting_students rs Where rs.student_id=:studentId And rs.active=true And rs.reporting_date is not null", nativeQuery = true)
	public boolean isReportedAfterClassCommencementDate(Integer studentId,Date commencementDate);

    @Modifying
   	@Query(value = "update reporting_students rs Inner Join Student_Details sd On sd.student_id=rs.student_id And sd.active=true "
   			+ "set rs.current_year=?1,rs.current_sem=?2 where sd.auid =?3 and rs.active=true",nativeQuery=true)
   	public void updateCurrentYearOrSemByAuid(Integer currentYear, Integer currentSem, String auid);
    
    
    @Modifying
	@Query(value = "update reporting_students rs set rs.active=true where rs.student_id =?1",nativeQuery=true)
	public void activateReporitngOfNewStudentTable(Integer studentId);

	@Query(value = "Select rs.current_sem From reporting_students rs where rs.student_id=?1 and rs.active=true",nativeQuery=true)
    Integer getCurrentSemByStudentId(Integer studentId);

	@Query(value = "Select rs.current_year From reporting_students rs where rs.student_id=?1 and rs.active=true",nativeQuery=true)
	Integer getCurrentYearByStudentId(Integer studentId);

	@Query(value = "Select rs.reporting_date From reporting_students rs where rs.student_id=?1 and rs.active=true",nativeQuery=true)
	Date getReportingDateByStudentId(Integer sd);
	
	@Query(value = "select count(*) from reporting_students rs where rs.student_id=?1 And rs.active=true",nativeQuery=true)
	public Integer getStudentId(Integer student_id);
}
