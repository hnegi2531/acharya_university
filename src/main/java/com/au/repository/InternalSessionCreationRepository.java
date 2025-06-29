package com.au.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.InternalSessionCreation;

@Transactional
@Repository
public interface InternalSessionCreationRepository extends JpaRepository<InternalSessionCreation, Integer> {
	
	@Query(value = "SELECT isa from InternalSessionCreation isa where isa.active=true")
	public List<InternalSessionCreation> listAllActiveInternalSessionCreation();
	
	@Query(value = "select count(*) from InternalSessionCreation isa where isa.ac_year_id=?1 and isa.program_specialization_id=?2 and isa.course_assignment_id=?3 and "
			+ "isa.internal_master_id=?4 and isa.year_sem=?5 and active=true")
	public Integer getInternalSessionCreations( Integer ac_year_id,Integer program_specialization_id, Integer course_assignment_id ,Integer internal_master_id, Integer year_sem);
	
	@Query(value = "select count(*) from InternalSessionCreation isa where isa.ac_year_id=?1 and isa.program_specialization_id=?2 and isa.course_assignment_id=?3 and "
			+ "isa.current_sem=?4 and isa.current_year=?5 and isa.internal_master_id=?6 and active=true")
	public int getCount(Integer ac_year_id, Integer program_specialization_id, Integer course_assignment_id,
			Integer current_sem, Integer current_year, Integer internal_master_id);
	
//	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.program_specialization_id=?1 and active=true")
//	public Integer getInternalSessionAssignments(Integer program_specialization_id);
//	
//	
//	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.ac_year_id=?1 and active=true")
//	public Integer getCountOfAcademicYear(Integer ac_year_id);
//	
//	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.school_id=?1 and active=true")
//	public Integer getCountOfIsaSchool(Integer school_id);
//	
//	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.year_sem=?1 and active=true")
//	public Integer getCountOfIsaYearSem(Integer year_sem);  
//	
//	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.from_date=?1 and active=true")
//	public Integer getCountOfFrom_Date(Date from_date);  
//	
//	@Query(value = "select count(*) from InternalSessionAssignment isa where isa.to_date=?1 and active=true")
//	public Integer getCountOfTo_Date(Date to_date);
	
	@Query(value = "select isa.internal_session_id as id,isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,"
			+ "isa.ac_year_id as ac_year_id,isa.internal_short_name as internal_short_name,CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots,"
			+ "isa.date_of_exam as date_of_exam,isa.exam_time as exam_time,isa.time_slots_id as time_slots_id,isa.week_day as week_day,isa.min_marks as min_marks,isa.max_marks as max_marks,"
			+ "isa.course_assignment_id as course_assignment_id,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "isa.school_id as school_id,ps.program_id as program_id,isa.program_specialization_id as program_specialization_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_short_name as program_short_name,"
			+ "isa.year_sem as year_sem,ay.ac_year as ac_year,isa.created_by as created_by,isa.created_username as created_username,isa.created_date as created_date,"
			+ "isa.modified_date as modified_date,isa.modified_by as modified_by,isa.modified_username as modified_username,isa.active as active,"
//			+ "(select ((LENGTH(group_concat(itta2.student_ids))) - ((LENGTH(REPLACE((group_concat(itta2.student_ids)),\",\",\"\")))-1)) as count From internal_student_assignment itta2 "
//			+ "left join internal_session_creation isa on itta2.internal_session_id=isa.internal_session_id) as countOfStudent,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short, isa.current_sem as current_sem, isa.current_year as current_year "
			+ "from internal_session_creation isa "
			+ "left join academic_year ay on isa.ac_year_id=ay.ac_year_id "
			+ "left join schools sc on isa.school_id=sc.school_id "
			+ "left join program_specialization ps on isa.program_specialization_id=ps.program_specialization_id "
			+ "left join program p on ps.program_id=p.program_id "
			+ "LEFT JOIN department d ON ps.dept_id = d.dept_id "
			+ "left join course_assignment ca on isa.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join time_slots ts on ts.time_slots_id=isa.time_slots_id "
			+ "left join internal_types it on isa.internal_master_id=it.internal_master_id "
			+ " WHERE (:ac_year_id IS NULL OR isa.ac_year_id = :ac_year_id) "
			+ "AND (:school_id IS NULL OR isa.school_id = :school_id) "
			+ "AND (:program_specialization_id IS NULL OR isa.program_specialization_id = :program_specialization_id) "
			+ "AND (:internal_short_name IS NULL OR it.internal_short_name = :internal_short_name) "
			+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id) "
			+ "AND isa.internal_session_id IS NOT NULL "
			+ "AND CONCAT(IfNull(isa.created_username,''),'',IfNull(isa.internal_name,''),'',IfNull(isa.internal_short_name,''),'',IfNull(isa.remarks,''),"
			+ "'',IfNull(isa.created_by,''),'',IfNull(isa.created_date,'')) LIKE %:keyword% and isa.active=true",nativeQuery=true)
	public List<Map<String,Object>> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer ac_year_id,
																Integer school_id, Integer dept_id, Integer program_specialization_id, String internal_short_name);
	
	
	
	@Query(value = "select isa.internal_session_id as id,isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,"
			+ "isa.ac_year_id as ac_year_id,isa.internal_short_name as internal_short_name,CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots,"
			+ "isa.date_of_exam as date_of_exam,isa.exam_time as exam_time,isa.time_slots_id as time_slots_id,isa.week_day as week_day,isa.min_marks as min_marks,isa.max_marks as max_marks,"
			+ "isa.course_assignment_id as course_assignment_id,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "isa.school_id as school_id,ps.program_id as program_id,isa.program_specialization_id as program_specialization_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_short_name as program_short_name,"
			+ "isa.year_sem as year_sem,ay.ac_year as ac_year,isa.created_by as created_by,isa.created_username as created_username,isa.created_date as created_date,"
			+ "isa.modified_date as modified_date,isa.modified_by as modified_by,isa.modified_username as modified_username,isa.active as active,"
//			+ "(select ((LENGTH(group_concat(itta2.student_ids))) - ((LENGTH(REPLACE((group_concat(itta2.student_ids)),\",\",\"\")))-1)) as count From internal_student_assignment itta2 "
//			+ "left join internal_session_creation isa on itta2.internal_session_id=isa.internal_session_id) as countOfStudent,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short, isa.current_sem as current_sem, isa.current_year as current_year "
			+ "from internal_session_creation isa "
			+ "left join academic_year ay on isa.ac_year_id=ay.ac_year_id "
			+ "left join schools sc on isa.school_id=sc.school_id "
			+ "left join program_specialization ps on isa.program_specialization_id=ps.program_specialization_id "
			+ "left join program p on ps.program_id=p.program_id "
			+ "LEFT JOIN department d ON ps.dept_id = d.dept_id "
			+ "left join course_assignment ca on isa.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join time_slots ts on ts.time_slots_id=isa.time_slots_id "
			+ "left join internal_types it on isa.internal_master_id=it.internal_master_id and isa.active=true "
			+ " WHERE (:ac_year_id IS NULL OR isa.ac_year_id = :ac_year_id) "
			+ "AND (:school_id IS NULL OR isa.school_id = :school_id) "
			+ "AND (:program_specialization_id IS NULL OR isa.program_specialization_id = :program_specialization_id) "
			+ "AND (:internal_short_name IS NULL OR it.internal_short_name = :internal_short_name) "
			+ "AND (:dept_id IS NULL OR ps.dept_id = :dept_id) "
			+ "AND isa.internal_session_id IS NOT NULL ",nativeQuery=true)
	public List<Map<String,Object>> getAllSortedData(Pageable pageable1, Integer ac_year_id, Integer school_id,
													 Integer dept_id, Integer program_specialization_id, String internal_short_name);
	
	
	@Modifying
	@Query(value = "update InternalSessionCreation isa set isa.active=false where isa.internal_session_id=?1")
	public void updateDept(Integer id);
	
	
	@Modifying
	@Query(value = "update InternalSessionCreation isa set isa.active=true where isa.internal_session_id=?1")
	public void updateDept1(Integer id);

	
	
	@Query(value = "Select isa.internal_name,isa.internal_short_name "
			+ "from  internal_session_creation isa "
			+ "left join internal_student_assignment itt on itt.internal_session_id=isa.internal_session_id "
			+ "left join course_assignment ca on isa.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "where isa.school_id=?1 and isa.program_id=?2 and isa.program_specialization_id=?3 and isa.ac_year_id=?4 and isa.year_sem=?5 and c.course_id=?6 and isa.active=true",nativeQuery = true)
	public List<Map<String, Object>> internal_session_idbasedOnSessionAssignment11(Integer school_id,Integer program_id,Integer program_specialization_id,Integer ac_year_id,Integer year_sem,Integer course_id);

	@Modifying
	@Query(value = "update InternalSessionCreation isa set isa.active=false where isa.internal_session_id=?1")
	public void deactivateInternalSessionCreation(Integer internal_session_id);

	@Modifying
	@Query(value = "update InternalFacultyRoomAssignment itt set itt.active=false where itt.internal_session_id=?1")
	public void deactivateInternalFacultyRoomAssignment(Integer internal_session_id);

	@Modifying
	@Query(value = "update InternalStudentAssignment itta set itta.active=false where itta.internal_session_id=?1")
	public void deactivateInternalStudentAssignment(Integer internal_session_id);
	
	@Query(value = "select isa.internal_session_id from internal_session_creation isa where isa.ac_year_id=?1 and isa.program_specialization_id IN ?2 and isa.year_sem=?3 "
			+ "and isa.internal_master_id=?4 and isa.active=true",nativeQuery = true)
	public List<Integer> getInternalIds(Integer ac_year_id, List<Integer> program_specialization_id, Integer year_sem, Integer internal_master_id);

	
	@Query(value = "select ps.program_specialization_id,ps.program_specialization_name,ps.program_specialization_short_name "
			+ "from program_specialization ps where program_specialization_id IN "
			+ "(select isa.program_specialization_id from internal_session_creation isa where isa.ac_year_id=?1 and isa.internal_master_id = ?2 "
			+ "and isa.year_sem=?3 and isa.active=true) and ps.active=true",nativeQuery = true)
	public List<Map<String, Object>> getProgramSpecialization(Integer ac_year_id, Integer internal_master_id,
			Integer year_sem);
	
	@Modifying
	@Query(value = "update InternalSessionCreation isa set isa.active=true where isa.internal_session_id=?1")
	public void activateInternalSessionCreation(Integer internal_session_id);

	@Modifying
	@Query(value = "update InternalFacultyRoomAssignment itt set itt.active=true where itt.internal_session_id=?1")
	public void activateInternalFacultyRoomAssignment(Integer internal_session_id);

	@Modifying
	@Query(value = "update InternalStudentAssignment itta set itta.active=true where itta.internal_session_id=?1")
	public void activateInternalStudentAssignment(Integer internal_session_id);

	
	@Query(value = "select isa.internal_session_id as internal_session_id,isa.internal_name as internal_name,"
			+ "isa.internal_short_name as internal_short_name "
			+ "from internal_session_creation isa "
			+ "where isa.ac_year_id=?1 And isa.program_specialization_id=?2 and isa.year_sem=?3 And isa.active=true",nativeQuery = true)
	public List<Map<String, Object>> getAllActiveInternalId(Integer ac_year_id, Integer program_specialization_id,
			Integer year_sem);

	
	@Query(value = "select itt.internal_session_id as internal_session_id,cc.course_name as course_name,itt.course_assignment_id as course_assignment_id,"
			+ "cc.course_short_name as course_short_name,ca.course_assignment_coursecode as course_assignment_coursecode,ca.course_id as course_id,"
			+ "Concat(IfNull(ca.course_assignment_coursecode,''),'-',IfNull(cc.course_name,'')) as courseName "
			+ "from internal_session_creation isa "
			+"left join internal_faculty_room_assignment itt on isa.internal_session_id=itt.internal_session_id "
			+"left join course_assignment ca on ca.course_assignment_id=isa.course_assignment_id "
			+"left join course cc on cc.course_id=ca.course_id "
			+ "where isa.internal_session_id=?1 And isa.year_sem=?2 And itt.active=true group by isa.course_assignment_id",nativeQuery = true)
	public List<Map<String, Object>> getAllActivecourseid(Integer internal_session_id, Integer year_sem);

	@Query(value = "select sd.student_id as student_id,sd.auid as auid,sd.student_name as student_name "
			+ "from student_details sd "
			+ "where sd.student_id =?1 and sd.active=true",nativeQuery = true)
	public Map<Object, Object> getStudentDetailsData(Integer st);
	
	@Query(value = "select isa.max_marks as max_marks,isa.min_marks as min_marks,isa.internal_session_id as internal_session_id "
			+ "from internal_session_creation isa "
			+ "where isa.internal_session_id=?2 and isa.ac_year_id=?1 And isa.year_sem=?3 And isa.program_specialization_id=?4 And "
			+ "isa.course_assignment_id=?5 group by isa.internal_session_id",nativeQuery = true)
			public Map<Object, Object> getInternalDetailsData(Integer ac_year_id, Integer internal_session_id, Integer year_sem,
					Integer program_specialization_id,Integer course_assignment_id);

	
	@Query(value = "SELECT isa from InternalSessionCreation isa where isa.active=true "
			+ "Group by isa.ac_year_id,isa.program_specialization_id,isa.internal_master_id,isa.year_sem")
	public List<InternalSessionCreation> getAllActiveInternalSessionCreation();
	
//	@Query(value = "select isa.internal_session_id as internal_session_id,isa.internal_name as internal_name,"
//			+ "DATE_FORMAT(isa.to_date,'%d-%m-%Y') as to_date,DATE_FORMAT(isa.from_date,'%d-%m-%Y') as from_date,"
//			+ "it.internal_short_name as internal_short_name,DATE(isa.from_date) as dateForArgument from internal_session_creation isa "
//			+ "Left join internal_types it on it.internal_master_id=isa.internal_master_id "
//			+ "where isa.ac_year_id=?1 And isa.program_specialization_id=?2 and isa.year_sem=?3 And isa.active=true",nativeQuery = true)
//	public List<Map<String,Object>> internalSessionDetailsAssignedToSpecialization(Integer ac_year_id,Integer program_specialization_id,Integer current_year_sem);
	
	
	
	
	@Query(value = "select new map( isa.internal_session_id as id,isa.internal_master_id as internal_master_id,isa.date_of_exam as date_of_exam,"
			+ "isa.ac_year_id as ac_year_id,it.internal_short_name as internal_short_name,it.internal_name As internal_name,isa.current_year As current_year,"
			+ "isa.school_id As school_id,isa.course_assignment_id AS course_assignment_id,isa.program_specialization_id As program_specialization_id,"
			+ "isa.created_by as created_by,isa.created_username as created_username,isa.created_date as created_date,isa.year_sem As year_sem,"
			+ "isa.modified_date as modified_date,isa.modified_by as modified_by,isa.modified_username as modified_username,isa.active as active,"
			+ "ay.ac_year As ac_year,sc.school_name As school_name,sc.school_name_short As school_name_short,isa.percentage As percentage,"
			+ "ca.course_assignment_coursecode As course_assignment_coursecode,c.course_id As course_id,c.course_name As course_name,isa.current_sem As current_sem,"
			+ "c.course_short_name As course_short_name,c.course_code As course_code,isa.external_min_marks AS external_min_marks,isa.external_max_marks As external_max_marks,"
			+ "ps.program_specialization_name As program_specialization_name,ps.program_specialization_short_name As program_specialization_short_name,"
			+ "Concat(IfNull(c.course_code,''),'-',IfNull(c.course_name,'')) as courseName,"
			+ "pa.program_assignment_id As program_assignment_id,pa.number_of_semester As number_of_semester,pa.number_of_years As number_of_years) from InternalSessionCreation isa "
			+ "left join InternalTypes it on it.internal_master_id=isa.internal_master_id "
			+ "left join Academic_year ay on ay.ac_year_id=isa.ac_year_id "
		    + "left join Schools sc on sc.school_id=isa.school_id "
		    + "left join CourseAssignment ca on ca.course_assignment_id=isa.course_assignment_id "
		    + "left join Course c on c.course_id=ca.course_id "
		    + "left join ProgramSpecilization ps on ps.program_specialization_id=isa.program_specialization_id "
		    + "left join ProgramAssigment pa on pa.program_assignment_id=ps.program_assignment_id "
			+ "where isa.external_max_marks is not null And CONCAT(IfNull(isa.created_username,''),'',IfNull(ca.course_assignment_coursecode,''),'',IfNull(c.course_short_name,''),'',IfNull(ay.ac_year,''),"
			+ "'',IfNull(isa.created_by,''),'',IfNull(isa.created_date,'')) LIKE %?1% ")
	public List<Map<String,Object>> getAllDataFilteredByKeywordExternal(Pageable pageable, Object keyword);
	
	
	@Query(value = "select new map( isa.internal_session_id as id,isa.internal_master_id as internal_master_id,isa.date_of_exam as date_of_exam,"
			+ "isa.ac_year_id as ac_year_id,it.internal_short_name as internal_short_name,it.internal_name As internal_name,isa.current_year As current_year,"
			+ "isa.school_id As school_id,isa.course_assignment_id AS course_assignment_id,isa.program_specialization_id As program_specialization_id,"
			+ "isa.created_by as created_by,isa.created_username as created_username,isa.created_date as created_date,isa.year_sem As year_sem,"
			+ "isa.modified_date as modified_date,isa.modified_by as modified_by,isa.modified_username as modified_username,isa.active as active,"
			+ "ay.ac_year As ac_year,sc.school_name As school_name,sc.school_name_short As school_name_short,isa.percentage As percentage,"
			+ "ca.course_assignment_coursecode As course_assignment_coursecode,c.course_id As course_id,c.course_name As course_name,isa.current_sem As current_sem,"
			+ "c.course_short_name As course_short_name,c.course_code As course_code,isa.external_min_marks AS external_min_marks,isa.external_max_marks As external_max_marks,"
			+ "ps.program_specialization_name As program_specialization_name,ps.program_specialization_short_name As program_specialization_short_name,"
			+ "Concat(IfNull(c.course_code,''),'-',IfNull(c.course_name,'')) as courseName,"
			+ "pa.program_assignment_id As program_assignment_id,pa.number_of_semester As number_of_semester,pa.number_of_years As number_of_years) from InternalSessionCreation isa "
			+ "left join InternalTypes it on it.internal_master_id=isa.internal_master_id "
			+ "left join Academic_year ay on ay.ac_year_id=isa.ac_year_id "
		    + "left join Schools sc on sc.school_id=isa.school_id "
		    + "left join CourseAssignment ca on ca.course_assignment_id=isa.course_assignment_id "
		    + "left join Course c on c.course_id=ca.course_id "
		    + "left join ProgramSpecilization ps on ps.program_specialization_id=isa.program_specialization_id "
		    + "left join ProgramAssigment pa on pa.program_assignment_id=ps.program_assignment_id where isa.external_max_marks is not null")
	public List<Map<String,Object>> getAllSortedDataExternal(Pageable pageable);

	@Query(value = "select isa.external_max_marks as external_max_marks,isa.external_min_marks as external_min_marks,isa.internal_session_id as internal_session_id,isa.percentage AS percentage "
			+ "from internal_session_creation isa "
			+ "where isa.internal_session_id=?1 And isa.course_assignment_id=?2 And isa.active=true",nativeQuery = true)
	public Map<String, Object> getStudentMarks(Integer internal_session_id, Integer course_assignment_id);
	
	@Query(value = "select isa.internal_session_id as id,isa.internal_master_id as internal_master_id,isa.internal_name as internal_name,"
			+ "isa.ac_year_id as ac_year_id,isa.internal_short_name as internal_short_name,"
			+ "isa.date_of_exam as date_of_exam,isa.exam_time as exam_time,isa.time_slots_id as time_slots_id,isa.week_day as week_day,isa.min_marks as min_marks,isa.max_marks as max_marks,"
			+ "isa.course_assignment_id as course_assignment_id,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "isa.school_id as school_id,ps.program_id as program_id,isa.program_specialization_id as program_specialization_id,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,p.program_short_name as program_short_name,"
			+ "isa.year_sem as year_sem,ay.ac_year as ac_year,isa.created_by as created_by,isa.created_username as created_username,isa.created_date as created_date,"
			+ "isa.modified_date as modified_date,isa.modified_by as modified_by,isa.modified_username as modified_username,isa.active as active,"
			+ "CONCAT(ts.starting_time,' - ',ts.ending_time) as timeSlots,"
//			+ "(select ((LENGTH(group_concat(itta2.student_ids))) - ((LENGTH(REPLACE((group_concat(itta2.student_ids)),\",\",\"\")))-1)) as count From internal_student_assignment itta2 "
//			+ "left join internal_session_creation isa on itta2.internal_session_id=isa.internal_session_id) as countOfStudent,"
			+ "sc.school_name as school_name,sc.school_name_short as school_name_short from internal_session_creation isa "
			+ "left join academic_year ay on isa.ac_year_id=ay.ac_year_id "
			+ "left join schools sc on isa.school_id=sc.school_id "
			+ "left join program_specialization ps on isa.program_specialization_id=ps.program_specialization_id "
			+ "left join program p on ps.program_id=p.program_id "
			+ "left join time_slots ts on ts.time_slots_id=isa.time_slots_id "
			+ "left join course_assignment ca on isa.course_assignment_id=ca.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join internal_types it on isa.internal_master_id=it.internal_master_id "
			+ "where isa.active=true and isa.school_id=?1 and "
			+ "isa.ac_year_id=?2 and isa.internal_master_id=?3 and isa.year_sem=?4 and isa.program_specialization_id=?5",nativeQuery=true)
	public List<Map<String, Object>> getAllActiveInternalSessionCreation1(Integer schoolId, Integer ac_year_id,Integer internal_master_id, Integer year_sem, Integer program_specialization_id);
	
	@Query(value = "select isa.internal_session_id as internal_session_id,isa.course_assignment_id as course_assignment_id,isa.min_marks as min_marks, isa.max_marks as max_marks,"
			+ "isa.program_specialization_id As program_specialization_id,ps.school_id As school_id,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-',IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course "
			+ "from InternalSessionCreation isa "
			+ "left join CourseAssignment ca on ca.course_assignment_id=isa.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id "
			+ "left join ProgramSpecilization ps on isa.program_specialization_id=ps.program_specialization_id "
			+ "where isa.date_of_exam=?1 and isa.time_slots_id=?2 and isa.active=true")
	public List<Map<String, Object>> getCoursesOnDateOfExamAndTimeSlots(String date_of_exam, Integer time_slots_id);
	
	@Query(value = "select isa.course_assignment_id as course_assignment_id, "
			+ "isa.program_specialization_id As program_specialization_id,ps.school_id As school_id,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-',IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course,"
			+ "ifra.internal_session_id as internal_session_id, ifra.emp_ids as emp_ids,ed.employee_name as employee_name,ifra.internal_room_assignment_id as internal_room_assignment_id "
			+ "from InternalFacultyRoomAssignment ifra "
			+ "left join InternalSessionCreation isa on isa.internal_session_id=ifra.internal_session_id "
			+ "left join EmployeeDetails ed on ed.emp_id=ifra.emp_ids "
			+ "left join CourseAssignment ca on ca.course_assignment_id=isa.course_assignment_id "
			+ "left join Course c on c.course_id=ca.course_id "
			+ "left join ProgramSpecilization ps on isa.program_specialization_id=ps.program_specialization_id "
			+ "where isa.date_of_exam=?1 and isa.time_slots_id=?2 and ifra.rooms.room_id=?3 and ifra.active=true and isa.active=true")
	public List<Map<String, Object>> getAssignedCoursesOnDateOfExamAndTimeSlotsIdAndRoomId(String date_of_exam, Integer time_slots_id, Integer room_id);

}
