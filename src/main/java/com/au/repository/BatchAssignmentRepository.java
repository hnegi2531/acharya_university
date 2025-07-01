package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Batch;
import com.au.model.BatchAssignment;
import com.au.model.BatchProgramAssignment;

@Repository
@Transactional
public interface BatchAssignmentRepository extends JpaRepository<BatchAssignment, Integer>{

	@Modifying
	@Query(value = "update BatchAssignment ba set ba.active=false where ba.batch_assignment_id=?1")
	public void update(Integer fee_template_id);

	@Modifying
	@Query(value = "update BatchAssignment ba set ba.active=true where ba.batch_assignment_id=?1")
	public void update1(Integer id);
	
	@Query(value ="Select ba.batch_assignment_id as id,ba.batch_id as batch_id,ba.school_id as school_id,ba.interval_type_id as interval_type_id,"
			+ "ba.program_id as program_id,ba.ac_year_id as ac_year_id,ba.current_year as current_year,ba.guest_uesr_ids as guest_uesr_ids,"
			+ "(select (LENGTH(ba.student_ids) - LENGTH(REPLACE(ba.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "ba.current_sem as current_sem,ba.student_ids as student_ids,ba.batch_type as batch_type,ba.remarks as remarks, concat(ifnull(b.batch_name,''),'-',ifnull(ba.remarks,'')) as batch_name,"
			+ "ba.batch_master_id as batch_master_id,ba.created_date as created_date,ba.modified_date as modified_date,"
			+ "ba.created_by as created_by,ba.modified_by as modified_by,ba.created_username as created_username,tit.interval_type_short as interval_type_short,"
			+ "ba.modified_username as modified_username,ba.active as active,b.batch_short_name as batch_short_name,"
			+ "sch.school_name_short as school_name_short, GROUP_CONCAT(ps.program_specialization_short_name) as program_specialization_short_name,"
			+ "ay.ac_year as ac_year,bs.program_specialization_id As program_specialization_id "
			+ "From batch_assignment ba "
			+ "left join batch_program_assignment bs on bs.batch_assignment_id=ba.batch_assignment_id "
			+ "left join program_specialization ps on ps.program_specialization_id=bs.program_specialization_id "
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join schools sch on sch.school_id=ba.school_id "
			+ "Left join program pr on pr.program_id=ba.program_id "
			+ "Left join academic_year ay on ay.ac_year_id=ba.ac_year_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=ba.interval_type_id "
			+ "Where (:batch_id is null or ba.batch_id = :batch_id) And "
			+ "(:ac_year_id is null or ba.ac_year_id = :ac_year_id) And "
			+ "(:school_id is null or ba.school_id = :school_id) And "
			+ "((:current_year_sem is null or ba.current_sem = :current_year_sem) OR  (:current_year_sem is null or ba.current_year = :current_year_sem)) And "
			+ "(:program_specialization_id is null or bs.program_specialization_id = :program_specialization_id) And "
			+ "CONCAT(IfNull(ba.batch_assignment_id,''),'',IfNull(ba.current_year,''),'',IfNull(ba.batch_type,''),'',"
			+ "IfNull(ba.created_date,''),'',IfNull(ba.created_username,''),'',IfNull(b.batch_short_name,''),'',"
			+ "IfNull(sch.school_name_short,''),'',IfNull(pr.program_short_name,''),'',IfNull(ay.ac_year,'')) LIKE %:keyword% group by ba.batch_assignment_id ",nativeQuery=true)
	public List<Map<String, Object>> findAll2(Pageable pageable, Integer ac_year_id, Integer school_id, Integer program_specialization_id, Integer batch_id,Integer current_year_sem, Object keyword);
	
	@Query(value ="Select ba.batch_assignment_id as id,ba.batch_id as batch_id,ba.school_id as school_id,ba.interval_type_id as interval_type_id,"
			+ "ba.program_id as program_id,ba.ac_year_id as ac_year_id,ba.current_year as current_year,ba.guest_uesr_ids as guest_uesr_ids,"
			+ "(select (LENGTH(ba.student_ids) - LENGTH(REPLACE(ba.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "ba.current_sem as current_sem,ba.student_ids as student_ids,ba.batch_type as batch_type,ba.remarks as remarks, concat(ifnull(b.batch_name,''),'-',ifnull(ba.remarks,'')) as batch_name,"
			+ "ba.batch_master_id as batch_master_id,ba.created_date as created_date,ba.modified_date as modified_date,"
			+ "ba.created_by as created_by,ba.modified_by as modified_by,ba.created_username as created_username,tit.interval_type_short as interval_type_short,"
			+ "ba.modified_username as modified_username,ba.active as active,b.batch_short_name as batch_short_name,"
			+ "sch.school_name_short as school_name_short, GROUP_CONCAT(ps.program_specialization_short_name) as program_specialization_short_name,"
			+ "ay.ac_year as ac_year,bs.program_specialization_id As program_specialization_id "
			+ "From batch_assignment ba "
			+ "left join batch_program_assignment bs on bs.batch_assignment_id=ba.batch_assignment_id "
			+ "left join program_specialization ps on ps.program_specialization_id=bs.program_specialization_id "
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join schools sch on sch.school_id=ba.school_id "
			+ "Left join program pr on pr.program_id=ba.program_id "
			+ "Left join academic_year ay on ay.ac_year_id=ba.ac_year_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=ba.interval_type_id "
			+ "Where (:batch_id is null or ba.batch_id = :batch_id) And "
			+ "(:ac_year_id is null or ba.ac_year_id = :ac_year_id) And "
			+ "(:school_id is null or ba.school_id = :school_id) And "
			+ "((:current_year_sem is null or ba.current_sem = :current_year_sem) OR  (:current_year_sem is null or ba.current_year = :current_year_sem)) And "
			+ "(:program_specialization_id is null or bs.program_specialization_id = :program_specialization_id) group by ba.batch_assignment_id ",nativeQuery=true)
	public List<Map<String, Object>> findAll3(Pageable pageable, Integer ac_year_id, Integer school_id, Integer program_specialization_id, Integer batch_id,Integer current_year_sem);
	
	@Query(value = "SELECT count(*) FROM BatchAssignment ba Inner join BatchProgramAssignment bpa on bpa.batch_assignment_id=ba.batch_assignment_id where ba.school_id=?1 and bpa.program_specialization_id IN ?2 and ba.current_year =?3 and ba.ac_year_id=?4 and FIND_IN_SET(?5,ba.student_ids) > 0 and ba.active=true")
	public Integer getCountOfBatchAssignmentOnYear(Integer school_id,List<Integer> program_id,Integer current_year,Integer ac_year_id, Integer student_id);
	
	@Query(value = "SELECT count(*) FROM BatchAssignment ba Inner join BatchProgramAssignment bpa on bpa.batch_assignment_id=ba.batch_assignment_id where ba.school_id=?1 and bpa.program_specialization_id IN ?2 and ba.current_sem =?3 and ba.ac_year_id=?4 and FIND_IN_SET(?5,ba.student_ids) > 0 and ba.active=true")
	public Integer getCountOfBatchAssignmentOnSem(Integer school_id,List<Integer> program_id,Integer current_sem,Integer ac_year_id, Integer student_id);
	
	@Query(value = "SELECT ba FROM BatchAssignment ba Inner join BatchProgramAssignment bpa on bpa.batch_assignment_id=ba.batch_assignment_id where ba.school_id=?1 and bpa.program_id in ?2 and ba.current_sem =?3 and ba.ac_year_id=?4 and ba.batch_id=?5 and ba.active=true")
	public List<BatchAssignment> fetchStudentDetailForBatchAssignmentOnSem(Integer school_id,List<Integer> program_id,Integer current_sem,Integer ac_year_id,Integer batch_id);
	
	@Query(value = "SELECT ba FROM BatchAssignment ba Inner join BatchProgramAssignment bpa on bpa.batch_assignment_id=ba.batch_assignment_id where ba.school_id=?1 and bpa.program_id in ?2 and ba.current_year =?3 and ba.ac_year_id=?4 and ba.batch_id=?5 and ba.active=true")
	public List<BatchAssignment> fetchStudentDetailForBatchAssignmentOnYear(Integer school_id,List<Integer> program_id,Integer current_year,Integer ac_year_id,Integer batch_id);
	
	@Query(value = "SELECT ba.guest_uesr_ids FROM batch_assignment ba Inner join batch_program_assignment bpa on bpa.batch_assignment_id=ba.batch_assignment_idwhere ba.school_id=?1 and bpa.program_id=?2 and ba.current_sem =?3 and ba.ac_year_id=?4 and ba.batch_id=?5 and ba.active=true",nativeQuery=true)
	public String getGuestUserIDsOnSem(Integer school_id,Integer program_id,Integer current_sem,Integer ac_year_id,Integer batch_id);
	
	@Query(value = "SELECT ba.guest_uesr_ids FROM batch_assignment ba Inner join batch_program_assignment bpa on bpa.batch_assignment_id=ba.batch_assignment_idwhere ba.school_id=?1 and bpa.program_id=?2 and ba.current_year =?3 and ba.ac_year_id=?4 and ba.batch_id=?5 and ba.active=true",nativeQuery=true)
	public String getGuestUserIDsOnYear(Integer school_id,Integer program_id,Integer current_year,Integer ac_year_id,Integer batch_id);
	
	@Query(value ="Select ifnull(Max(ba.batch_master_id),0) FROM batch_assignment ba Where ba.ac_year_id=?1",nativeQuery=true)
	public Integer getMaxBatchMasterId(Integer ac_year_id);
	
	@Query(value = "select ba.batch_assignment_id,b.batch_id,b.batch_name,b.batch_short_name from batch_assignment ba left join batch b on ba.batch_id = b.batch_id where ba.interval_type_id=?1 and ba.active=true",nativeQuery=true)
	public List<Map<String, Object>> getAllBatchesForTimeTable(Integer interval_type_id);
	
	@Query(value = "select ba from BatchAssignment ba where ba.batch_id=?1 and ba.active=true")
	public List<BatchAssignment> getCountOfStudentsAssignedToBatch(Integer batch_id);
	
	@Query(value = "select ba.batch_id as batch_id,ba.batch_assignment_id as batch_assignment_id,Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as batch_name,"
			+ "b.batch_short_name as batch_short_name,b.remarks as remarks "
			+ "from batch_assignment ba "
			+ "left join batch b on b.batch_id=ba.batch_id "
			+ "where ba.school_id=?1 and ba.ac_year_id=?2 and ba.current_sem=?3 and ba.interval_type_id=?4 and b.active=true",nativeQuery=true)
	public List<Map<String, Object>> getAllBatchesForTimeTableOnSem(Integer school_id,Integer ac_year_id,Integer current_sem,Integer interval_type_id);

	@Query(value = "select ba.batch_id as batch_id,ba.batch_assignment_id as batch_assignment_id,Concat(IfNull(b.batch_name,''),'-',IfNull(ba.remarks,'')) as batch_name,"
			+ "b.batch_short_name as batch_short_name,b.remarks as remarks "
			+ "from batch_assignment ba "
			+ "left join batch b on b.batch_id=ba.batch_id "
			+ "where ba.school_id=?1 and ba.ac_year_id=?2 and ba.current_year=?3 and ba.interval_type_id=?4 and b.active=true",nativeQuery=true)
	public List<Map<String, Object>> getAllBatchesForTimeTableOnYear(Integer school_id,Integer ac_year_id,Integer current_year, Integer interval_type_id);
	
	@Query(value ="Select ba.batch_assignment_id From batch_assignment ba Where FIND_IN_SET(:student_id,ba.student_ids) > 0 And ba.current_sem =:current_sem And ba.active=true",nativeQuery=true)
	public List<Integer> getBatchAssignmentIdOnSem(Integer student_id,Integer current_sem);
	
	@Query(value ="Select ba.batch_assignment_id From batch_assignment ba Where FIND_IN_SET(:student_id,ba.student_ids) > 0 And ba.current_sem =:current_sem "
			+ "And ba.ac_year_id =:academic_year_id And ba.active=true",nativeQuery=true)
	public List<Integer> getBatchAssignmentIdOnSem(Integer student_id,Integer current_sem, Integer academic_year_id);
	
	@Query(value ="Select ba.batch_assignment_id From batch_assignment ba Where FIND_IN_SET(:student_id,ba.student_ids) > 0 And ba.current_year =:current_year And ba.active=true",nativeQuery=true)
	public List<Integer> getBatchAssignmentIdOnYear(Integer student_id,Integer current_year);
	
	@Query(value = "SELECT count(*) FROM batch_assignment ba where ba.remarks=?1 and ba.active=true",nativeQuery=true)
	public Integer checkRemarksValidation(String remarks);
	
	@Query(value = "SELECT distinct ba.program_specialization_id FROM batch_assignment ba where ba.batch_assignment_id=?1 and ba.active=true",nativeQuery=true)
	public List<Integer> checkRemarksValidation(Integer batch_assignment_id);

	@Query(value = "SELECT count(*) FROM batch_assignment ba WHERE  ba.batch_id  = ?1 AND IfNull(TRIM(ba.remarks),'') = ?2 AND ba.active=true",nativeQuery=true)
	public Integer checkRemarksValidationWithBatchId(Integer batchId, String remarks);
	
	
	@Query(value = "select ba from BatchAssignment ba where ba.batch_assignment_id=?1 and ba.active=true")
	public BatchAssignment activeBatchAssignmentDetail(Integer batch_assignment_id);
	
	@Query(value = "Select ba from BatchAssignment ba "
			+ "Inner join BatchProgramAssignment bpa on bpa.batch_assignment_id=ba.batch_assignment_id where ba.ac_year_id=?1 And bpa.program_specialization_id=?2 And ba.batch_id=?3 And ba.current_sem=?4 And ba.active=true")
	public List<BatchAssignment> getBatchAssignedStudentDetailsBySem(Integer acYearId, Integer programSpecializationId, Integer batchId, Integer currentSem);
	
	@Query(value = "Select ba from BatchAssignment ba "
			+ "Inner join BatchProgramAssignment bpa on bpa.batch_assignment_id=ba.batch_assignment_id where ba.ac_year_id=?1 And bpa.program_specialization_id=?2 And ba.batch_id=?3 And ba.current_year=?4 And ba.active=true")
	public List<BatchAssignment> getBatchAssignedStudentDetailsByYear(Integer acYearId, Integer programSpecializationId, Integer batchId, Integer currentYear);
	
	
	@Query(value = "select b.batch_name as batch_name,b.batch_id as batch_id FROM batch b "
			+ "Where b.batch_id in (select ba.batch_id From batch_assignment ba Inner join batch_program_assignment bpa on bpa.batch_assignment_id=ba.batch_assignment_id Where ba.ac_year_id=?1 And bpa.program_specialization_id=?2 And ba.current_sem=?3 And ba.active=true) And b.active=true",nativeQuery=true)
	public List<Map<String, Object>> batchByAcademicYearAndSpecializationIdBySem(Integer acYearId,Integer programSpecializationId,Integer currentSem);
	
	@Query(value = "select b.batch_name as batch_name,b.batch_id as batch_id FROM batch b "
			+ "Where b.batch_id in (select ba.batch_id From batch_assignment ba Inner join batch_program_assignment bpa on bpa.batch_assignment_id=ba.batch_assignment_id Where ba.ac_year_id=?1 And bpa.program_specialization_id=?2 And ba.current_year=?3 And ba.active=true) And b.active=true",nativeQuery=true)
	public List<Map<String, Object>> batchByAcademicYearAndSpecializationIdByYear(Integer acYearId,Integer programSpecializationId,Integer currentYear);
	
	@Query(value = "Select ba from BatchAssignment ba "
			+ "where ba.ac_year_id=?1 And ba.batch_id=?2 And (ba.current_sem=?3 OR ba.current_year=?3) And ba.active=true")
	public List<BatchAssignment> getBatchAssignedStudentDetailsByYearOrSem(Integer acYearId, Integer batchId, Integer currentSem);
	
	@Query(value = "select b.batch_name as batch_name,b.batch_id as batch_id FROM batch b "
			+ "Where b.batch_id in (select ba.batch_id From batch_assignment ba Where ba.ac_year_id=?1 And (ba.current_sem=?2 OR ba.current_year=?2) And ba.active=true) And b.active=true",nativeQuery=true)
	public List<Map<String, Object>> batchByAcademicYear(Integer acYearId,Integer currentSem);
	
	@Query(value = "Select ba.batch_assignment_id from batch_assignment ba "
			+ "where FIND_IN_SET(?1,ba.student_ids) > 0 And ba.ac_year_id=?2 And (ba.current_sem=?3 OR ba.current_year=?3) And ba.active=true",nativeQuery=true)
	public List<Integer> allBatchAssignedOfStudent(Integer student_id,Integer acYearId, Integer currentSem);
	
	@Query(value = "Select ba.batch_assignment_id from batch_assignment ba "
			+ "where FIND_IN_SET(?1,ba.student_ids) > 0 And (ba.current_sem=?2 OR ba.current_year=?2) And ba.active=true",nativeQuery=true)
	public List<Integer> allBatchAssignedOfStudentWithoutAcademicYear(Integer student_id, Integer currentSem);
	
	@Query(value = "Select ba.batch_id from batch_assignment ba Where ba.batch_assignment_id in (?1) And ba.active=true",nativeQuery=true)
	public List<Integer> batchIdsByBatchAssignmentIds(List<Integer> batchAssignmentIds);

	@Query(value ="Select sa.section_assignment_id From section_assignment sa "
			+ "Where FIND_IN_SET(?1,sa.student_ids) > 0 And sa.current_year_sem =?2 And sa.active=true",nativeQuery=true)
	public List<Integer> getSectionAssignmentId(Integer student_id,Integer current_year_sem);

	@Query(value = "SELECT bpa FROM BatchProgramAssignment bpa where bpa.batch_assignment_id=?1 And bpa.active=true")
	public List<BatchProgramAssignment> getBatchProgramAssignment(Integer batch_assignment_id);


	@Query(value ="Select ba.batch_assignment_id From batch_assignment ba Where FIND_IN_SET(:student_id,ba.student_ids) > 0 And ba.current_sem =:current_sem And ba.active=true",nativeQuery=true)
	public List<Integer> batchAssignmentIdOnSem(Integer student_id,Integer current_sem);
	
	@Query(value ="Select ba.batch_assignment_id From batch_assignment ba Where FIND_IN_SET(:student_id,ba.student_ids) > 0 And ba.current_year =:current_year And ba.active=true",nativeQuery=true)
	public List<Integer> batchAssignmentIdOnYear(Integer student_id,Integer current_year);


	@Query(value ="Select ba.batch_assignment_id as id,ba.batch_id as batch_id,ba.school_id as school_id,ba.interval_type_id as interval_type_id,"
			+ "ba.program_id as program_id,ba.ac_year_id as ac_year_id,ba.current_year as current_year,ba.guest_uesr_ids as guest_uesr_ids,"
			+ "ba.current_sem as current_sem,ba.student_ids as student_ids,ba.batch_type as batch_type,ba.remarks as remarks,"
			+ "(select (LENGTH(ba.student_ids) - LENGTH(REPLACE(ba.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "ba.batch_master_id as batch_master_id,ba.created_date as created_date,ba.modified_date as modified_date,"
			+ "ba.created_by as created_by,ba.modified_by as modified_by,ba.created_username as created_username,tit.interval_type_short as interval_type_short,"
			+ "ba.modified_username as modified_username,ba.active as active,b.batch_short_name as batch_short_name, concat(ifnull(b.batch_name,''),'-',ifnull(ba.remarks,'')) as batch_name,"
			+ "de.dept_id As dept_id,de.dept_name_short As dept_name_short,de.dept_name As dept_name,"
			+ "sch.school_name_short as school_name_short,GROUP_CONCAT(ps.program_specialization_short_name) as program_specialization_short_name,"
			+ "ay.ac_year as ac_year "
			+ "From batch_assignment ba "
			+ "left join batch_program_assignment bs on bs.batch_assignment_id=ba.batch_assignment_id "
			+ "left join program_specialization ps on ps.program_specialization_id=bs.program_specialization_id "
			+ "left join department de on ps.dept_id=de.dept_id "
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join schools sch on sch.school_id=ba.school_id "
			+ "Left join program pr on pr.program_id=ba.program_id "
			+ "Left join academic_year ay on ay.ac_year_id=ba.ac_year_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=ba.interval_type_id group by ba.batch_assignment_id "
			+ "where (:schoolId is null or ba.school_id = :schoolId) "
			 + "And (:createdBy is null or ba.created_by = :createdBy) "
			 + "And (:dept_id is null or de.dept_id = :dept_id) "
			+ "And CONCAT(IfNull(ba.batch_assignment_id,''),'',IfNull(ba.current_year,''),'',IfNull(ba.batch_type,''),'',"
			+ "IfNull(ba.created_date,''),'',IfNull(ba.created_username,''),'',IfNull(b.batch_short_name,''),'',"
			+ "IfNull(sch.school_name_short,''),'',IfNull(pr.program_short_name,''),'',IfNull(ay.ac_year,'')) LIKE %:keyword%",nativeQuery=true)
	public List<Map<String, Object>> fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBylistAll1(Pageable pageable, Object keyword, Integer schoolId, Integer createdBy, Integer dept_id);
	
	@Query(value ="Select ba.batch_assignment_id as id,ba.batch_id as batch_id,ba.school_id as school_id,ba.interval_type_id as interval_type_id,"
			+ "ba.program_id as program_id,ba.ac_year_id as ac_year_id,ba.current_year as current_year,ba.guest_uesr_ids as guest_uesr_ids,"
			+ "ba.current_sem as current_sem,ba.student_ids as student_ids,ba.batch_type as batch_type,ba.remarks as remarks,"
			+ "(select (LENGTH(ba.student_ids) - LENGTH(REPLACE(ba.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "ba.batch_master_id as batch_master_id,ba.created_date as created_date,ba.modified_date as modified_date,"
			+ "ba.created_by as created_by,ba.modified_by as modified_by,ba.created_username as created_username,tit.interval_type_short as interval_type_short,"
			+ "ba.modified_username as modified_username,ba.active as active,b.batch_short_name as batch_short_name, concat(ifnull(b.batch_name,''),'-',ifnull(ba.remarks,'')) as batch_name,"
			+ "de.dept_id As dept_id,de.dept_name_short As dept_name_short,de.dept_name As dept_name,"
			+ "sch.school_name_short as school_name_short,GROUP_CONCAT(ps.program_specialization_short_name) as program_specialization_short_name,"
			+ "ay.ac_year as ac_year "
			+ "From batch_assignment ba "
			+ "left join batch_program_assignment bs on bs.batch_assignment_id=ba.batch_assignment_id "
			+ "left join program_specialization ps on ps.program_specialization_id=bs.program_specialization_id "
			+ "left join department de on ps.dept_id=de.dept_id "
			+ "Left join batch b on b.batch_id=ba.batch_id "
			+ "Left join schools sch on sch.school_id=ba.school_id "
			+ "Left join program pr on pr.program_id=ba.program_id "
			+ "Left join academic_year ay on ay.ac_year_id=ba.ac_year_id "
			+ "Left join time_interval_types tit on tit.interval_type_id=ba.interval_type_id "
			+ "where (:schoolId is null or ba.school_id = :schoolId) "
			 + "And (:createdBy is null or ba.created_by = :createdBy) "
			 + "And (:dept_id is null or de.dept_id = :dept_id) "
			+ "group by ba.batch_assignment_id ",nativeQuery=true)
	public List<Map<String, Object>> fetchAllBatchAssignmentDetailsBasedOnSchoolAndCreatedBylistAll2(Pageable pageable, Integer schoolId, Integer createdBy, Integer dept_id);

}
