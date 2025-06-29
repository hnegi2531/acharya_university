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

import com.au.model.ProgramAssigment;

@Repository
@Transactional
public interface ProgramAssigmentRepository extends JpaRepository<ProgramAssigment, Integer>{

	
	@Query(value = "select new map(pa.program_assignment_id as id,pa.program_id as program_id,"
			+ "pa.school_id as school_id,pa.graduation_id as graduation_id,pa.program_type as program_type,"
			+ "pa.ac_year_id as ac_year_id,pa.number_of_years as number_of_years,pa.created_date as created_date,"
			+ "pa.modified_date as modified_date,pa.created_by as created_by,pa.modified_by as modified_by,"
			+ "pa.active as active,pa.created_username as created_username,pa.modified_username as modified_username,"
			+ "pa.number_of_semester as number_of_semester,ay.ac_year as ac_year,sc.school_name_short as school_name_short,"
			+ "p.program_short_name as program_short_name,g.graduation_name_short as graduation_name_short,pt.program_type_name as program_type_name) from ProgramAssigment pa "
			+ "left join Academic_year ay on pa.ac_year_id=ay.ac_year_id left join Schools sc on pa.school_id=sc.school_id "
			+ "left join Program p on pa.program_id=p.program_id left join Graduation g on pa.graduation_id=g.graduation_id "
			+ "left join ProgramType pt on pa.program_type_id=pt.program_type_id "
			+ "where CONCAT(IfNull(pa.program_assignment_id,''),'',IfNull(pa.program_type,''),'',IfNull(sc.school_name_short,''),'',IfNull(p.program_short_name,''),"
			+ "'',IfNull(g.graduation_name_short,''),'',IfNull(pa.created_by,''),'',IfNull(pa.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(pa.program_assignment_id as id,pa.program_id as program_id,"
			+ "pa.school_id as school_id,pa.graduation_id as graduation_id,pa.program_type as program_type,"
			+ "pa.ac_year_id as ac_year_id,pa.number_of_years as number_of_years,pa.created_date as created_date,"
			+ "pa.modified_date as modified_date,pa.created_by as created_by,pa.modified_by as modified_by,"
			+ "pa.active as active,pa.created_username as created_username,pa.modified_username as modified_username,"
			+ "pa.number_of_semester as number_of_semester,ay.ac_year as ac_year,sc.school_name_short as school_name_short,"
			+ "p.program_short_name as program_short_name,g.graduation_name_short as graduation_name_short,pt.program_type_name as program_type_name) from ProgramAssigment pa "
			+ "left join Academic_year ay on pa.ac_year_id=ay.ac_year_id left join Schools sc on pa.school_id=sc.school_id "
			+ "left join Program p on pa.program_id=p.program_id left join Graduation g on pa.graduation_id=g.graduation_id "
			+ "left join ProgramType pt on pa.program_type_id=pt.program_type_id")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Query(value =  "select pt from ProgramAssigment pt where pt.active=true" )
	public List<ProgramAssigment> findAll1();
	
	@Modifying
	@Query(value = "update ProgramAssigment pt set pt.active=false where pt.program_assignment_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update ProgramAssigment pt set pt.active=true where pt.program_assignment_id=?1")
	public void update1(Integer id);

	@Query(value = "select pt.program_assignment_id as program_assignment_id,pt.program_id as program_id,pt.number_of_semester as number_of_semester,"
			+ "pt.number_of_years as number_of_years,p.program_name as program_name,p.program_short_name as program_short_name,"
			+ "ay.ac_year as ac_year,pty.program_type_name as program_type_name,pty.program_type_code as program_type_code "
			+ "from program_assignment pt "
			+ "left join academic_year ay on pt.ac_year_id=ay.ac_year_id "
			+ "left join program p on pt.program_id=p.program_id "
			+ "left join program_type pty on pty.program_type_id=pt.program_type_id "
			+ "Where pt.program_assignment_id in (Select max(program_assignment_id) from program_assignment where ac_year_id <= ?1 and school_id=?2 and active=true  group by program_id)",nativeQuery=true)
	public List<Map<String, Object>> fetchProgramDetail(Integer ac_year_id,Integer school_id);

	@Query(value = "select * from program_assignment pt where pt.program_assignment_id in (Select max(program_assignment_id) from program_assignment where ac_year_id <= ?1 and program_id=?2 and school_id=?3 and active=true  group by program_id)",nativeQuery = true)
	public List<ProgramAssigment> getNumOfSemAndYearByProgram_IdAndAcYear_Id(Integer ac_year_id, Integer program_id, Integer school_id);
	
	@Query(value = "select Count(*) from program_assignment where  program_id=?1 and school_id=?2 and program_type_id =?3 and active=true",nativeQuery = true)
	public Integer getProgramAssignmentCount(Integer program_id, Integer school_id,Integer program_type_id);
	
	@Query(value = "select new map(pa.program_assignment_id as id,pa.program_id as program_id,"
			+ "pa.school_id as school_id,pa.graduation_id as graduation_id,pa.program_type as program_type,"
			+ "pa.ac_year_id as ac_year_id,pa.number_of_years as number_of_years,pa.created_date as created_date,"
			+ "pa.modified_date as modified_date,pa.created_by as created_by,pa.modified_by as modified_by,"
			+ "pa.active as active,pa.created_username as created_username,pa.modified_username as modified_username,"
			+ "pa.number_of_semester as number_of_semester,ay.ac_year as ac_year,sc.school_name as school_name,sc.school_name_short as school_name_short,"
			+ "p.program_short_name as program_short_name,p.program_name as program_name,g.graduation_name as graduation_name,g.graduation_name_short as graduation_name_short,"
			+ "pt.program_type_name as program_type_name) from ProgramAssigment pa "
			+ "left join Academic_year ay on pa.ac_year_id=ay.ac_year_id left join Schools sc on pa.school_id=sc.school_id "
			+ "left join Program p on pa.program_id=p.program_id left join Graduation g on pa.graduation_id=g.graduation_id "
			+ "left join ProgramType pt on pa.program_type_id=pt.program_type_id where pa.program_assignment_id=?1")
	public List<HashMap<String, Object>> findDetailsById(Integer id);
	
	@Query(value = "select p.program_id,p.program_name,p.program_short_name from program p where p.program_id IN(select distinct pa.program_id "
			+ "from program_assignment pa where pa.ac_year_id=?1 and pa.school_id=?2 and pa.active=true) and p.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchProgramIds(Integer ac_year_id, Integer school_id);
	
	@Query(value ="Select pt.program_type_name From ProgramAssigment pa "
			+ "Left join ProgramType pt on pa.program_type_id=pt.program_type_id where ac_year_id <= ?1 and pa.program_id=?2 and pa.school_id=?3 and pa.active=true")
	public String  getProgramType(Integer ac_year_id,Integer program_id,Integer school_id);

	@Query(value = "select * from program_assignment where program_id=?1 and school_id=?2 and active=true",nativeQuery = true)
	public List<ProgramAssigment> getNumOfSemAndYearByProgram_IdAndSchool_Id(Integer program_id, Integer school_id);
	
	@Query(value = "select new map(pt.program_id as program_id,pt.number_of_semester as number_of_semester,pty.program_type_name as program_type_name,"
			+ "pt.number_of_years as number_of_years,p.program_name as program_name,p.program_short_name as program_short_name,pty.program_type_code as program_type_code,"
			+ "ps.program_specialization_id as program_specialization_id,CONCAT(IfNull(ps.program_specialization_short_name,''),'-',IfNull(p.program_short_name,'')) as specialization_with_program) from ProgramAssigment pt "
			+ "left join ProgramType pty on pty.program_type_id=pt.program_type_id "
			+ "inner join ProgramSpecilization ps on (pt.program_id=ps.program_id And ps.school_id=pt.school_id) "
			+ "inner join Program p on pt.program_id=p.program_id where pt.school_id=?1 and pt.active=true")
	public List<HashMap<String, Object>> fetchProgramWithSpecializationBySchoolId(Integer school_id);
	
	@Query(value = "select new map(pt.program_assignment_id as program_assignment_id,pt.program_id as program_id,pt.number_of_semester as number_of_semester,pty.program_type_name as program_type_name,"
			+ "pt.number_of_years as number_of_years,p.program_name as program_name,p.program_short_name as program_short_name,pty.program_type_code as program_type_code,pt.ac_year_id as ac_year_id,"
			+ "ay.ac_year as ac_year,ps.program_specialization_id as program_specialization_id,ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "CONCAT(IfNull(ps.program_specialization_short_name,''),'-',IfNull(p.program_short_name,'')) as specialization_with_program,ps.dept_id as dept_id,"
			+ "CONCAT(IfNull(ps.program_specialization_name,''),'-',IfNull(p.program_short_name,'')) as specialization_with_program1) from ProgramAssigment pt "
			+ "left join Academic_year ay on pt.ac_year_id=ay.ac_year_id "
			+ "Inner join ProgramType pty on pty.program_type_id=pt.program_type_id "
			+ "Inner join ProgramSpecilization ps on pt.program_assignment_id=ps.program_assignment_id "
			+ "Inner join Program p on pt.program_id=p.program_id where pt.school_id =?1 And pt.active=true")
	public List<HashMap<String, Object>> fetchAllProgramsWithSpecialization(Integer school_id);
	
	
	@Query(value = "select new map(pt.program_assignment_id as program_assignment_id,pt.program_id as program_id,pt.number_of_semester as number_of_semester,pty.program_type_name as program_type_name,"
			+ "pt.number_of_years as number_of_years,p.program_name as program_name,p.program_short_name as program_short_name,pty.program_type_code as program_type_code,pt.ac_year_id as ac_year_id,"
			+ "ay.ac_year as ac_year,ps.program_specialization_id as program_specialization_id,ps.program_specialization_short_name as program_specialization_short_name,CONCAT(IfNull(ps.program_specialization_short_name,''),'-',IfNull(p.program_short_name,'')) as specialization_with_program) from ProgramAssigment pt "
			+ "left join Academic_year ay on pt.ac_year_id=ay.ac_year_id "
			+ "left join ProgramType pty on pty.program_type_id=pt.program_type_id "
			+ "left join ProgramSpecilization ps on pt.program_assignment_id=ps.program_assignment_id "
			+ "Left join Program p on pt.program_id=p.program_id where pt.school_id =?1 And pt.ac_year_id=?2 And pt.active=true")
	public List<HashMap<String, Object>> fetchAllProgramsWithSpecializationBasedOnSchoolAndAcYear(Integer school_id,Integer ac_year_id);
	
	@Query(value = "select  ps.program_specialization_id as program_specialization_id,pt.program_assignment_id as program_assignment_id,pt.program_id as program_id,"
			+ "pt.number_of_semester as number_of_semester,pty.program_type_name as program_type_name,ps.school_id As school_id,"
			+ "pt.number_of_years as number_of_years,p.program_name as program_name,p.program_short_name as program_short_name,pty.program_type_code as program_type_code,"
			+ "pt.ac_year_id as ac_year_id,ps.program_specialization_name As program_specialization_name,"
			+ "ay.ac_year as ac_year,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "CONCAT(IfNull(ps.program_specialization_short_name,''),'-',IfNull(p.program_short_name,'')) as specialization_with_program "
			+ "from program_specialization ps "
			+ "Left join program p on ps.program_id=p.program_id "
			+ "left join program_assignment pt on pt.program_assignment_id=ps.program_assignment_id "
			+ "left join program_type pty on pty.program_type_id=pt.program_type_id "
			+ "left join academic_year ay on pt.ac_year_id=ay.ac_year_id "
			+ " where ps.active=true group by ps.program_specialization_id",nativeQuery=true)
	public List<Map<String, Object>> fetchAllProgramsWithSpecializationBasedOnAcYear();
	
	@Query(value="Select pty.program_type_name From ProgramAssigment pt "
			+ "left join ProgramType pty on pty.program_type_id=pt.program_type_id Where pt.program_assignment_id=?1 ")
	public String getProgramTypeOnProgramAssignmentId(Integer program_assignment_id);
	
	@Query(value ="Select ifNull((SUBSTRING(g.graduation_name_short,1,1)),' ') From program_assignment pt "
			+ "Left join graduation_type g On g.graduation_id=pt.graduation_id "
			+ "Where pt.ac_year_id =?1 and pt.school_id=?2 and pt.program_assignment_id=?3 and pt.active=true",nativeQuery=true)
	public String getGraduationShortName(Integer ac_year_id,Integer school_id,Integer program_assignment_id);
	
	@Query(value = "select MAX(number_of_semester) from program_assignment where school_id=?2 and ac_year_id=?1 ORDER BY number_of_semester DESC",nativeQuery=true)
	public Integer fetchYearSemForBatchTimeTable(Integer ac_year_id, Integer school_id);
	
	@Query(value = "select new map(pt.program_assignment_id as program_assignment_id,pt.program_id as program_id,pt.number_of_semester as number_of_semester,pty.program_type_name as program_type_name,"
			+ "pt.number_of_years as number_of_years,p.program_name as program_name1,p.program_name as program_name,p.program_short_name as program_short_name,"
			+ "pty.program_type_code as program_type_code,pt.ac_year_id as ac_year_id,pty.program_type_id as program_type_id,"
			+ "ay.ac_year as ac_year) from ProgramAssigment pt "
			+ "left join Academic_year ay on pt.ac_year_id=ay.ac_year_id "
			+ "left join ProgramType pty on pty.program_type_id=pt.program_type_id "
			+ "Left join Program p on pt.program_id=p.program_id where pt.school_id =?1 And pt.active=true")
	public List<HashMap<String, Object>> fetchAllProgramsWithProgramType(Integer school_id);
	
	
	@Query(value = "select new map(pt.program_assignment_id as program_assignment_id,pt.program_id as program_id,p.program_name as program,"
			+ "pt.number_of_semester as number_of_semester,pty.program_type_name as program_type_name,p.program_short_name as program_short_name,"
			+ "pt.number_of_years as number_of_years,concat(ifnull(p.program_short_name,''),'-',ifnull(ps.program_specialization_name,'')) as program_name,"
			+ "pty.program_type_code as program_type_code,pt.ac_year_id as ac_year_id,pty.program_type_id as program_type_id,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "pt.graduation_id as graduation_id,gd.graduation_name_short as graduation_name_short,"
			+ "ay.ac_year as ac_year,ps.program_specialization_id as program_specialization_id) from ProgramAssigment pt "
			+ "left join Academic_year ay on pt.ac_year_id=ay.ac_year_id "
			+ "left join ProgramType pty on pty.program_type_id=pt.program_type_id "
			+ "left join ProgramSpecilization ps on ps.program_assignment_id=pt.program_assignment_id "
			+ "left join Graduation gd on pt.graduation_id=gd.graduation_id "
			+ "Left join Program p on pt.program_id=p.program_id where pt.active=true")
	public List<HashMap<String, Object>> fetchAllProgramsAndSpecializationWithProgramType();
	
	@Query(value = "select new map(pt.program_assignment_id as program_assignment_id,pt.program_id as program_id,pt.number_of_semester as number_of_semester,pty.program_type_name as program_type_name,"
			+ "pt.number_of_years as number_of_years,p.program_name as program_name1,p.program_name as program_name,p.program_short_name as program_short_name,"
			+ "pty.program_type_code as program_type_code,pt.ac_year_id as ac_year_id,pty.program_type_id as program_type_id,"
			+ "ay.ac_year as ac_year) from ProgramAssigment pt "
			+ "left join Academic_year ay on pt.ac_year_id=ay.ac_year_id "
			+ "left join ProgramType pty on pty.program_type_id=pt.program_type_id "
			+ "Left join Program p on pt.program_id=p.program_id where pt.active=true")
	public List<HashMap<String, Object>> programsDetailsWithProgramType();

	@Query(value =  "select pt from ProgramAssigment pt where pt.program_assignment_id=?1" )
	public ProgramAssigment findAll11(Integer program_assignment_id);

	@Query(value = "select new map(pt.program_assignment_id as program_assignment_id,pt.program_id as program_id,p.program_name as program,"
			+ "pt.number_of_semester as number_of_semester,pty.program_type_name as program_type_name,p.program_short_name as program_short_name,"
			+ "pt.number_of_years as number_of_years,concat(ifnull(p.program_short_name,''),'-',ifnull(ps.program_specialization_name,'')) as program_name,"
			+ "pty.program_type_code as program_type_code,pt.ac_year_id as ac_year_id,pty.program_type_id as program_type_id,"
			+ "ps.program_specialization_name as program_specialization_name,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "pt.graduation_id as graduation_id,gd.graduation_name_short as graduation_name_short,"
			+ "ay.ac_year as ac_year,ps.program_specialization_id as program_specialization_id,pt.school_id as school_id) from ProgramAssigment pt "
			+ "left join Academic_year ay on pt.ac_year_id=ay.ac_year_id "
			+ "left join ProgramType pty on pty.program_type_id=pt.program_type_id "
			+ "left join ProgramSpecilization ps on ps.program_assignment_id=pt.program_assignment_id "
			+ "left join Graduation gd on pt.graduation_id=gd.graduation_id "
			+ "Left join Program p on pt.program_id=p.program_id where pt.ac_year_id =?1 And pt.school_id=?2 And pt.active=true")
	public List<HashMap<String, Object>> fetchAllProgramsAndSpecializationWithProgramTypeOnAcademicYeearAndSchool(Integer ac_year_id,Integer school_id);

	@Query(value ="Select pt.program_type_name From ProgramAssigment pa "
			+ "Left join ProgramType pt on pa.program_type_id=pt.program_type_id where pa.program_assignment_id=?1")
	public String  getProgramTypeByProgramAssignmentId(Integer program_assignment_id);

	@Query(value=" select pt from ProgramAssigment pt where pt.school_id=:schoolId ")
	public List<ProgramAssigment> getProgramsBySchoolId(Integer schoolId);

	
	@Query(value ="SELECT pt.program_type_name As program_type_name,pt.program_type_code As program_type_code,p.program_name As program_name,"
			+ "p.program_short_name As program_short_name,p.program_id As program_id,pt.program_type_id As program_type_id,pa.program_assignment_id As program_assignment_id "
			+ "FROM program_assignment pa "
			+ "left join program p on p.program_id=pa.program_id "
			+ "left join program_type pt on pt.program_type_id=pa.program_type_id "
			+ "where pa.school_id=?1 And pa.active=true",nativeQuery=true)
	public List<Map<String, Object>> getProgramTypeBasedOnSchool(Integer school_id);

	@Query(value=" select p from ProgramAssigment p  where p.program_id=:programId and p.school_id=:schoolId ")
	public ProgramAssigment getByProgramId(Integer programId, Integer schoolId);


	@Query(value ="Select pt.program_type_name From Student_Details sd "
			+ "Inner join ProgramAssigment pa on pa.program_assignment_id=sd.program_assignment_id "
			+ "Inner join ProgramType pt on pa.program_type_id=pt.program_type_id where sd.student_id=?1 And sd.active=true")
	String getProgramTypeByStudentId(Integer studentId);
}
