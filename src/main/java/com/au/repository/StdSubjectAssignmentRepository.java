package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import com.au.model.StdSubjectAssignment;

@Repository
@Transactional
public interface StdSubjectAssignmentRepository extends JpaRepository<StdSubjectAssignment, Integer>{

	@Query(value = "SELECT * FROM subject_workload_creation where subject_type=true",nativeQuery = true)
	public List<Map<String,Object>> fetchSubWorkLoadBySubtype();

	@Query(value = "SELECT * FROM subject_workload_creation where work_load_type=true",nativeQuery = true)
	public List<Map<String,Object>> fetchSubWorkLoadByWorkloadtype();

	@Modifying
	@Query(value = "update StdSubjectAssignment r set r.active=false where r.subjetAssignId=?1")
	public void updateAcademicWorkLoad(Integer id);

	@Modifying
	@Query(value = "update StdSubjectAssignment r set r.active=true where r.subjetAssignId=?1")
	public void updateAcademicWorkLoad1(Integer id);

	
/*	@Query(value="select  sub.subject_name,sa.subject_id,st.subject_type_name,sa.subject_type_id,sa.school_id,s.school_name,sa.program_id,p.program_name ,sa.program_specialization_id,\r\n"
			+ "ps.program_specialization_name,ps.program_specialization_short_name,p.program_short_name from \r\n"
			+ "subject_assignment sa inner join schools s on sa.school_id=s.school_id\r\n"
			+ "inner join program p on sa.program_id=p.program_id \r\n"
			+ "inner join program_specialization ps on \r\n"
			+ "sa.program_specialization_id = ps.program_specialization_id \r\n"
			+ "left join subject_type st on sa.subject_type_id = st.subject_type_id \r\n"
			+ "inner join subjects sub on sa.subject_id=sub.subject_id",nativeQuery = true)*/
	@Query(value = "SELECT * FROM sub_assign_index1",nativeQuery = true)
	public List<Map<String, Object>> fetchAllDetails();

	//@Query(value = "SELECT count(program_id) FROM subject_assignment where program_specialization_id like ?1%",nativeQuery = true)
			  // or program_specialization_id like '%,id2,%' or program_specialization_id like '%?id3,'",nativeQuery = true)
//	public Integer countByProgramSpecializationIdLike(String programSpecializationId);
	/*
	@Query(value = "SELECT  sa.active,sa.created_username,sa.created_by,sa.created_date,st.subject_type_name,sa.subject_type_id,sa.program_specialization_short_name,sa.program_specialization_id,ps.program_specialization_name,sa.program_id,pr.program_name,pr.program_short_name,sa.subject_id,sub.subject_name FROM subject_assignment sa "
			+ "inner join subjects sub "
			+ "on sa.subject_id=sub.subject_id inner join program pr on "
			+ "sa.program_id=pr.program_id inner join program_specialization ps "
			+ "on sa.program_specialization_id=ps.program_specialization_id inner join "
			+ "subject_type st on sa.subject_type_id=st.subject_type_id",nativeQuery = true)
	public List<Map<String, Object>> getSubjectAssignIndex();
	*/
	
	@Query(value = "SELECT * FROM sub_assign_index1",nativeQuery = true)
	public List<Map<String, Object>> getSubjectAssignIndex();
	
	@Query(value = "select new map(ssa.subjetAssignId as id,ssa.subjectId as subjectId,ssa.subjectUniversityMaxHours as subjectUniversityMaxHours,"
			+ "ssa.credits as credits,ssa.subjectInstituteMaxHours as subjectInstituteMaxHours,ssa.subjectTypeId as subjectTypeId,"
			+ "ssa.maxCieMarks as maxCieMarks,ssa.maxSemYearMarks as maxSemYearMarks,ssa.minCieMarks as minCieMarks,ssa.minSemYearMarks as minSemYearMarks,"
			+ "ssa.schoolId as schoolId,ssa.programId as programId,ssa.programSpecializationId as programSpecializationId,ssa.programSpecializationShortName as programSpecializationShortName,"
			+ "ssa.modifiedBy as modifiedBy,ssa.createdDate as createdDate,ssa.modifiedDate as modifiedDate,ssa.active as active,"
			+ "ssa.createdBy as createdBy,ssa.createdUsername as createdUsername,ssa.modifiedUsername as modifiedUsername) "
			+ "from StdSubjectAssignment ssa "
			+ "where CONCAT(IfNull(ssa.subjetAssignId,''),'',IfNull(ssa.subjectId,''),'',IfNull(ssa.subjectUniversityMaxHours,''),"
			+ "'',IfNull(ssa.credits,''),'',IfNull(ssa.subjectInstituteMaxHours,''),'',IfNull(ssa.subjectTypeId,''),"
			+ "'',IfNull(ssa.maxCieMarks,''),'',IfNull(ssa.maxSemYearMarks,''),'',IfNull(ssa.minCieMarks,''),'',IfNull(ssa.minSemYearMarks,''),"
			+ "'',IfNull(ssa.schoolId,''),'',IfNull(ssa.programId,''),'',IfNull(ssa.programSpecializationId,''),"
			+ "'',IfNull(ssa.programSpecializationShortName,''),'',IfNull(ssa.createdBy,''),'',IfNull(ssa.createdDate,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(ssa.subjetAssignId as id,ssa.subjectId as subjectId,ssa.subjectUniversityMaxHours as subjectUniversityMaxHours,"
			+ "ssa.credits as credits,ssa.subjectInstituteMaxHours as subjectInstituteMaxHours,ssa.subjectTypeId as subjectTypeId,"
			+ "ssa.maxCieMarks as maxCieMarks,ssa.maxSemYearMarks as maxSemYearMarks,ssa.minCieMarks as minCieMarks,ssa.minSemYearMarks as minSemYearMarks,"
			+ "ssa.schoolId as schoolId,ssa.programId as programId,ssa.programSpecializationId as programSpecializationId,ssa.programSpecializationShortName as programSpecializationShortName,"
			+ "ssa.modifiedBy as modifiedBy,ssa.createdDate as createdDate,ssa.modifiedDate as modifiedDate,ssa.active as active,"
			+ "ssa.createdBy as createdBy,ssa.createdUsername as createdUsername,ssa.modifiedUsername as modifiedUsername) "
			+ "from StdSubjectAssignment ssa")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	
}
