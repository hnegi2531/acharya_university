package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Program;

@Repository
@Transactional
public interface ProgramRepository extends JpaRepository<Program, Integer> {
/*
	@Query("SELECT u FROM Program u where u.school_id=?1 and active=1")
	public List<Program> findBySchoolId(Integer school_id);

	@Query("select count(u) from Program u where u.school_id=?1")
	public Integer countRecords(Integer id);

	@Query(value = "SELECT count(*) FROM program where program_name=?1 and school_id=?2", nativeQuery = true)
	public Integer getProgramByPnameSchool(String program_name, Integer school_id);

	@Query(value = "SELECT * FROM program where school_id=?1 and active=1", nativeQuery = true)
	public List<Program> getProgramBySchool(Integer school_id);

	@Query(value = "SELECT program_short_name FROM program where program_id=?1", nativeQuery = true)
	public String getProgramShortName(Integer program_id);

	@Query(value = "select new map(p.program_id as program_id,p.program_name as program_name,"
			+ "p.program_short_name as program_short_name,p.school_id as school_id,p.created_by as created_by,"
			+ "p.modified_by as modified_by,p.created_date as created_date,p.modified_date as modified_date,"
			+ "p.active as active,p.graduation_id as graduation_id,p.created_username as created_username,"
			+ "p.modified_username as modified_username,sc.school_name as school_name,"
			+ "pt.program_type_name as program_type_name,g.graduation_name as graduation_name,"
			+ "g.graduation_name_short as graduation_name_short) from Program p "
			+ "left join Schools sc on p.school_id=sc.school_id "
			+ "left join ProgramType pt on p.program_type_id=pt.program_type_id "
			+ "left join Graduation g on p.graduation_id=g.graduation_id")
	public List<HashMap<String, Object>> findAll1();
*/
	
	@Query(value = "select new map(p.program_id as id,p.program_name as program_name,"
			+ "p.program_short_name as program_short_name,p.created_by as created_by,p.display_name as display_name,"
			+ "p.modified_by as modified_by,p.created_date as created_date,p.modified_date as modified_date,"
			+ "p.active as active,p.created_username as created_username,p.program_code as program_code,"
			+ "p.modified_username as modified_username) from Program p "
			+ "where CONCAT(IfNull(p.program_id,''),'',IfNull(p.program_name,''),'',IfNull(p.program_short_name,''),"
			+ "'',IfNull(p.created_by,''),'',IfNull(p.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	@Query(value = "select new map(p.program_id as id,p.program_name as program_name,"
			+ "p.program_short_name as program_short_name,p.created_by as created_by,p.display_name as display_name,"
			+ "p.modified_by as modified_by,p.created_date as created_date,p.modified_date as modified_date,"
			+ "p.active as active,p.created_username as created_username,p.program_code as program_code,"
			+ "p.modified_username as modified_username) from Program p")
	public Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update Program p set p.active=false where p.program_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Program p set p.active=true where p.program_id=?1")
	public void update1(Integer id);

	@Query("SELECT p FROM Program p where p.active=true")
	public List<Program> findAll11();
	
	@Query(value="SELECT COUNT(*) FROM program where program_name=?1 and active=true",nativeQuery = true)
	public Integer getProgramCount(String program_name);
	
	@Query(value="SELECT COUNT(*) FROM program where program_short_name=?1 and active=true",nativeQuery = true)
	public Integer getProgramShortCount(String program_short_name);

	@Query(value="SELECT p.program_id as programId, p.program_short_name as programShortName FROM program p where p.program_id=:program_id and active=true",nativeQuery = true)
	public Map<String, Object> getProgramDetails(Integer program_id);

	@Query(value = "Select GROUP_CONCAT(p.program_short_name ORDER BY program_id ASC) From program p where p.program_id in (:program_id)", nativeQuery = true)
	public String getCommaSeperartedProgramShortName(List<Integer> program_id);

	@Query(value = "Select pt.program_type_name FROM ProgramType pt where pt.program_type_id= (select pa.program_type_id from ProgramAssigment pa where pa.program_assignment_id=?1 and pa.active=true) and pt.active=true")
	public String getProgramType(Integer program_assignment_id);

	
	@Query(value = "select pr.program_id As program_id,pr.program_name As program_name,pr.program_short_name,ps.school_id As school_id,ps.program_specialization_id as program_specialization_id, "
			+ "pa.program_assignment_id As program_assignment_id,pa.number_of_semester As number_of_semester,pa.number_of_years As number_of_years,"
			+ "CONCAT(IfNull(pr.program_short_name,''),'-',IfNull(ps.program_specialization_short_name,'')) as specialization_with_program "
			+ " FROM employee_details ed  "
			+ " left join program_specialization ps on ed.school_id = ps.school_id "
			+ " left join program pr on ps.program_id = pr.program_id "
			+ " left join program_assignment pa on pa.program_id = pr.program_id  "
			+ " where ed.emp_id=?1 Group by ps.program_specialization_id ", nativeQuery = true)
	public List<Map<String, Object>> getAllActiveProgramDetails(Integer emp_id);

	@Query(value = "select pr.program_id As program_id,pr.program_name As program_name,pr.program_short_name,ps.school_id As school_id,ps.program_specialization_id as program_specialization_id, "
			+ "pa.program_assignment_id As program_assignment_id,pa.number_of_semester As number_of_semester,pa.number_of_years As number_of_years "
			+ " FROM employee_details ed  "
			+ " left join program_specialization ps on ed.school_id = ps.school_id "
			+ " left join program pr on ps.program_id = pr.program_id "
			+ " left join program_assignment pa on pa.program_id = pr.program_id  "
			+ " where ed.emp_id=?1 Group by pr.program_id ", nativeQuery = true)
	public List<Map<String, Object>> getAllActiveProgramDetails1(Integer emp_id);


	//if either program_name or program_short_name is null this query will handle
	@Query("select concat(p.program_name, '-', p.program_short_name) from Program p where p.active = true")
	List<String> getConcatenatedProgramNamesWithFallback();

}
