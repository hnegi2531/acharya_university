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

import com.au.model.ProgramSpecilization;

@Transactional
@Repository
public interface ProgramSpecilizationRepository extends JpaRepository<ProgramSpecilization, Integer> {

	@Query(value = "SELECT * FROM program_specialization where school_id=?1 and program_id=?2 and active=1", nativeQuery = true)
	public List<ProgramSpecilization> findById1(Integer program_id, Integer school_id);

	@Query(value = "SELECT count(*) FROM program_assignment where school_id=?1", nativeQuery = true)
	public Integer findById2(Integer school_id);

	@Query(value = "SELECT count(*) FROM program_specialization where school_id=?1 and  program_id=?2 "
			+ "and program_specialization_name=?3 and active =true", nativeQuery = true)
	public Integer getProgramSpecilizationWithName(Integer school_id, Integer program_id,
			String program_specialization_name);

	@Query(value = "SELECT count(*) FROM program_specialization where school_id=?1 and  program_id=?2 "
			+ "and program_specialization_short_name=?3 and active =true", nativeQuery = true)
	public Integer getProgramSpecilizationWithShortName(Integer school_id, Integer program_id,
			String program_specialization_short_name);

	@Query(value = "SELECT count(*) FROM program_specialization where school_id=?1 and  program_id=?2 "
			+ "and program_specialization_name=?3 and program_specialization_short_name=?4 and active =true", nativeQuery = true)
	public Integer getProgramSpecilizationWithBoth(Integer school_id, Integer program_id,
			String program_specialization_name, String program_specialization_short_name);

	@Query(value = "SELECT count(*) FROM program_specialization where school_id=?1 and dept_id=?2 and  program_id=?3 "
			+ "and  auid_format=?4 and active =true", nativeQuery = true)
	public Integer getProgramSpecilization1(Integer school_id, Integer dept_id, Integer program_id, String auid_format);

	@Query(value = "SELECT auid_format FROM program_specialization where program_specialization_id=?1", nativeQuery = true)
	public String getProgramAuid(Integer program_specialization_id);

	@Query(value = "select CONCAT(ps.program_specialization_short_name,'-',(select p.program_short_name from program p where "
			+ "ps.program_id=p.program_id and p.active=true)) as program_specialization_name,ps.program_specialization_id as program_specialization_id from program_specialization ps where "
			+ "ps.active=true", nativeQuery = true)
	public List<Map<String, Object>> findAll11();

	@Query(value = "select ps.program_specialization_id as id,ps.program_assignment_id as program_assignment_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ps.auid_format as auid_format,ps.program_id as program_id,ps.school_id as school_id,ps.dept_id as dept_id,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "ps.created_by as created_by,ps.created_date as created_date,"
			+ "ps.active as active,ps.created_username as created_username,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name from program_specialization ps "
			+ "left join schools sc on ps.school_id=sc.school_id " + "left join department d on ps.dept_id=d.dept_id "
			+ "Inner join program_assignment pa on ps.program_assignment_id=pa.program_assignment_id "
			+ "left join program p on pa.program_id=p.program_id "
			+ "Where CONCAT(IfNull(ps.program_specialization_id,''),'',IfNull(ps.program_specialization_name,''),'',"
			+ "IfNull(ps.program_specialization_short_name,''),'',IfNull(ps.auid_format,''),'',IfNull(ps.auid_format,''),'',"
			+ "IfNull(ps.created_by,''),'',IfNull(ps.created_date,''),'',IfNull(ps.created_username,''),'',"
			+ "IfNull(ps.created_username,''),'',IfNull(sc.school_name,''),'',IfNull(d.dept_name,''),'',"
			+ "IfNull(p.program_name,'')) LIKE %?1% ", nativeQuery = true)
	public List<Map<String, Object>> findAll1(Pageable pageable, Object keyword);

	@Query(value = "select ps.program_specialization_id as id,ps.program_assignment_id as program_assignment_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,"
			+ "ps.auid_format as auid_format,ps.program_id as program_id,ps.school_id as school_id,ps.dept_id as dept_id,"
			+ "ps.program_specialization_name as program_specialization_name,"
			+ "ps.created_by as created_by,ps.created_date as created_date,"
			+ "ps.active as active,ps.created_username as created_username,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short,d.dept_name as dept_name,d.dept_name_short as dept_name_short,"
			+ "p.program_name as program_name,p.program_short_name as program_short_name from program_specialization ps "
			+ "left join schools sc on ps.school_id=sc.school_id " + "left join department d on ps.dept_id=d.dept_id "
			+ "Inner join program_assignment pa on ps.program_assignment_id=pa.program_assignment_id "
			+ "left join program p on pa.program_id=p.program_id", nativeQuery = true)
	public List<Map<String, Object>> findAll2(Pageable pageable);

	@Modifying
	@Query(value = "update ProgramSpecilization ps set ps.active=false where ps.program_specialization_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update ProgramSpecilization ps set ps.active=true where ps.program_specialization_id=?1")
	public void update1(Integer id);

	@Query(value = "select ps from ProgramSpecilization ps where ps.school_id=?1 and ps.program_id=?2 and ps.dept_id=?3 "
			+ "and ps.active=true")
	public List<ProgramSpecilization> getAllCourseDetailss(Integer school_id, Integer program_id, Integer dept_id);

	@Query(value = "Select GROUP_CONCAT(ps.program_specialization_short_name ORDER BY program_specialization_id ASC) From program_specialization ps where ps.program_specialization_id in (:program_specialization_id)", nativeQuery = true)
	public String getCommaSeperartedProgramSpecializationShortName(List<Integer> program_specialization_id);

	@Query(value = "SELECT count(*) FROM ProgramSpecilization ps where ps.program_specialization_name=?1 and ps.active=true")
	public Integer countOfSpecilizationName(String program_specialization_name);

	@Query(value = "SELECT count(*) FROM ProgramSpecilization ps where ps.program_specialization_short_name=?1 and ps.active=true")
	public Integer countOfSpecilizationShortName(String program_specialization_short_name);

	@Query(value = "SELECT count(*) FROM ProgramSpecilization ps where ps.auid_format=?1 and ps.active=true")
	public Integer countOfSpecilizationAuid(String auid_format);

	@Query(value = "SELECT count(*) FROM ProgramSpecilization ps where ps.program_specialization_id != ?1 and ps.program_specialization_name = ?2 and ps.active=true")
	public Integer countOfSpecilizationNameForUpdate(Integer program_specialization_id,
			String program_specialization_name);

	@Query(value = "SELECT count(*) FROM ProgramSpecilization ps where ps.program_specialization_id != ?1 and ps.program_specialization_short_name = ?2 and ps.active=true")
	public Integer countOfSpecilizationShortNameForUpdate(Integer program_specialization_id,
			String program_specialization_short_name);

	@Query(value = "SELECT count(*) FROM ProgramSpecilization ps where ps.program_specialization_id != ?1 and ps.auid_format = ?2 and ps.active=true")
	public Integer countOfSpecilizationAuidForUpdate(Integer program_specialization_id, String auid_format);

	@Query(value = "SELECT count(*) FROM ProgramSpecilization ps where ps.program_specialization_id != ?1 and ps.school_id=?2 and ps.program_id=?3 "
			+ "and  ps.program_specialization_name=?4 and ps.active =true")
	public Integer countProgramSpecilizationNameForUpdate(Integer program_specialization_id, Integer school_id,
			Integer program_id, String program_specialization_name);

	@Query(value = "SELECT count(*) FROM ProgramSpecilization ps where ps.program_specialization_id != ?1 and ps.school_id=?2 and ps.program_id=?3 "
			+ "and  ps.program_specialization_short_name=?4 and ps.active =true")
	public Integer countProgramSpecilizationShortNameForUpdate(Integer program_specialization_id, Integer school_id,
			Integer program_id, String program_specialization_short_name);

	@Query(value = "SELECT count(*) FROM ProgramSpecilization ps where ps.program_specialization_id != ?1 and ps.school_id=?2 and ps.program_id=?3 "
			+ "and  ps.program_specialization_name=?3 and  ps.program_specialization_short_name=?4 and ps.active =true")
	public Integer countProgramSpecilizationBothForUpdate(Integer program_specialization_id, Integer school_id,
			Integer program_id, String program_specialization_name, String program_specialization_short_name);

	@Query(value = "select t.program_specialization_short_name from program_specialization t where t.program_specialization_id=?1", nativeQuery = true)
	public String getProgramSpecilizationShortName(Integer id);
	
	@Query(value = "select t.program_specialization_name from program_specialization t where t.program_specialization_id=?1", nativeQuery = true)
	public String getProgramSpecilizationName(Integer id);

	@Query(value = "select t.auid_format from program_specialization t where t.program_specialization_id=?1", nativeQuery = true)
	public String getProgramSpecilizationAuidName(Integer id);

	@Query(value = "select pa.program_id as programId, "
			+ " p.program_name as programName, "
			+ " pa.number_of_semester as numberOfSem, "
			+ " pa.number_of_years as numberOfYear, "
			+ " pt.program_type_name as programTypeName "
			+ " from program_assignment pa  "
			+ " left join program p on p.program_id=pa.program_id "
			+ " left join program_type pt on pt.program_type_id=pa.program_type_id where (:schoolId is null or pa.school_id = :schoolId) "
			+ "	and (:acYearId is null or pa.ac_year_id = :acYearId) group by pa.program_id ", nativeQuery = true)
	public List<Map<String, Object>> getProgramsDetails(Integer schoolId, Integer acYearId);

	@Query(value = "select t.program_assignment_id from program_specialization t where t.program_specialization_id=?1", nativeQuery = true)
	public Integer getProgramAssignmentId(Integer pg);
	
	@Query(value = "select t.program_id from program_specialization t where t.program_specialization_id=?1", nativeQuery = true)
	public Integer getProgramId(Integer pg);

	@Query(value="SELECT p.program_specialization_id as programSpecializationId, p.program_specialization_short_name as programSpecializationShortName FROM program_specialization p where p.program_id=:programId and p.school_id=:schoolId and active=true",nativeQuery = true)
	public List<Map<String, Object>> getProgramSpecializationDetails(Integer programId, Integer schoolId);
	
	@Query(value = "select ps.program_specialization_id as course_branch_assignment_id,"
			+ "ps.program_specialization_short_name as course_branch_short_name,"
			+ "ps.auid_format as auid_format,ps.program_id as course_id,ps.school_id as institute_id,"
			+ "ps.program_specialization_name as course_branch_name,"
			+ "sc.school_name as institute_name,sc.school_name_short as institute_name_short,"
			+ "p.program_name as course_name,p.program_short_name as course_short_name from program_specialization ps "
			+ "left join schools sc on ps.school_id=sc.school_id " 
			+ "left join department d on ps.dept_id=d.dept_id "
			+ "Inner join program_assignment pa on ps.program_assignment_id=pa.program_assignment_id "
			+ "left join program p on pa.program_id=p.program_id", nativeQuery = true)
	public List<Map<String, Object>> specializationDetails();

	@Query(value = "Select Group_concat(ps.program_specialization_short_name) From program_specialization ps Where ps.program_specialization_id in (?1)", nativeQuery = true)
	public String specializationNamesWithCommaSeperated(List<Integer> savedSpecializtionIds);

}
