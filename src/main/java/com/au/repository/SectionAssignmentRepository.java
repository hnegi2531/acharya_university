package com.au.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.Section;
import com.au.model.SectionAssignment;

@Repository
@Transactional
public interface SectionAssignmentRepository extends JpaRepository<SectionAssignment, Integer> {
	@Query(value = "select h from SectionAssignment h where h.active=true")
	public List<SectionAssignment> findAll1();

	@Modifying
	@Query(value = "update SectionAssignment r set r.active=false where r.section_assignment_id=?1")
	public void updateSectionAssignment(Integer id);

	@Modifying
	@Query(value = "update SectionAssignment r set r.active=true where r.section_assignment_id=?1")
	public void updateSectionAssignment1(Integer id);

	@Query(value = "SELECT sa.section_assignment_id as id,sa.remarks as remarks,sa.section_assignment_id as section_assignment_id,"
			+ "sa.current_year_sem as current_year_sem,sa.active as active,sec.section_name as section_name,sa.created_by as created_by,"
			+ "(select (LENGTH(sa.student_ids) - LENGTH(REPLACE(sa.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "sa.section_id as section_id,ay.ac_year as ac_year,sa.ac_year_id as ac_year_id,sa.created_date as created_date,sa.created_username as created_username,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "sa.program_specialization_id as program_specialization_id,p.program_short_name as program_short_name,sa.student_ids as student_ids,"
			+ "p.program_name as program_name,sa.program_id as program_id,sa.school_id as school_id,sch.school_name as school_name,"
			+ "pa.program_assignment_id As program_assignment_id,pa.program_type As program_type,pa.program_type_id As program_type_id,"
			+ "sch.school_name_short as school_name_short FROM section_assignment sa "
			+ "left join schools sch on sa.school_id=sch.school_id "
			+ "left join program p on sa.program_id=p.program_id "
			+ "left join program_assignment pa on sa.program_assignment_id=pa.program_assignment_id "
			+ "left join program_specialization ps on sa.program_specialization_id=ps.program_specialization_id "
			+ "left join academic_year ay on sa.ac_year_id=ay.ac_year_id "
			+ "left join section sec on sa.section_id=sec.section_id "
			+ "Where (:section_id is null or sa.section_id = :section_id) And "
			+ "(:ac_year_id is null or sa.ac_year_id = :ac_year_id) And "
			+ "(:school_id is null or sa.school_id = :school_id) And "
			+ "(:current_year_sem is null or sa.current_year_sem = :current_year_sem) And "
			+ "(:program_id is null or sa.program_id = :program_id) And "
			+ "(:program_specialization_id is null or sa.program_specialization_id = :program_specialization_id) And "
			+ "CONCAT(IfNull(sa.section_assignment_id,''),'',IfNull(sa.current_year_sem,''),'',"
			+ "IfNull(sec.section_name,''),'',IfNull(ay.ac_year,''),'',IfNull(ps.program_specialization_short_name,''),'',"
			+ "IfNull(ps.program_specialization_name,''),'',IfNull(p.program_short_name,''),'',IfNull(p.program_name,''),'',"
			+ "IfNull(sch.school_name,''),'',IfNull(sch.school_name_short,'')) LIKE %:keyword% group by sa.section_assignment_id",nativeQuery =true)
	public List<Map<String,Object>> fetchAllSectionAssignmentDetails2(Pageable pageable,  Integer ac_year_id,
			Integer school_id, Integer program_id, Integer program_specialization_id, Integer section_id,Integer current_year_sem,Object keyword);
	
	@Query(value = "SELECT sa.section_assignment_id as id,sa.remarks as remarks,sa.section_assignment_id as section_assignment_id,"
			+ "sa.current_year_sem as current_year_sem,sa.active as active,sec.section_name as section_name,sa.created_by as created_by,"
			+ "(select (LENGTH(sa.student_ids) - LENGTH(REPLACE(sa.student_ids,\",\",\"\")) + 1)) as count_of_students,"
			+ "sa.section_id as section_id,ay.ac_year as ac_year,sa.ac_year_id as ac_year_id,sa.created_date as created_date,sa.created_username as created_username,"
			+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
			+ "sa.program_specialization_id as program_specialization_id,p.program_short_name as program_short_name,sa.student_ids as student_ids,"
			+ "p.program_name as program_name,sa.program_id as program_id,sa.school_id as school_id,sch.school_name as school_name,"
			+ "pa.program_assignment_id As program_assignment_id,pa.program_type As program_type,pa.program_type_id As program_type_id,"
			+ "sch.school_name_short as school_name_short FROM section_assignment sa "
			+ "left join schools sch on sa.school_id=sch.school_id "
			+ "left join program p on sa.program_id=p.program_id "
			+ "left join program_assignment pa on sa.program_assignment_id=pa.program_assignment_id "
			+ "left join program_specialization ps on sa.program_specialization_id=ps.program_specialization_id "
			+ "left join academic_year ay on sa.ac_year_id=ay.ac_year_id "
			+ "left join section sec on sa.section_id=sec.section_id "
			+ "Where (:section_id is null or sa.section_id = :section_id) And "
			+ "(:ac_year_id is null or sa.ac_year_id = :ac_year_id) And "
			+ "(:school_id is null or sa.school_id = :school_id) And "
			+ "(:current_year_sem is null or sa.current_year_sem = :current_year_sem) And "
			+ "(:program_id is null or sa.program_id = :program_id) And "
			+ "(:program_specialization_id is null or sa.program_specialization_id = :program_specialization_id) group by sa.section_assignment_id ",nativeQuery =true)
	public List<Map<String,Object>> fetchAllSectionAssignmentDetails3(Pageable pageable,Integer ac_year_id,
			Integer school_id, Integer program_id, Integer program_specialization_id, Integer section_id,Integer current_year_sem);

	@Query(value = "select new map(sa.section_assignment_id as section_assignment_id,sa.section_id as section_id,"
			+ "s.section_name as section_name) from SectionAssignment sa left join Section s on sa.section_id=s.section_id "
			+ "where sa.ac_year_id=?1 and sa.school_id=?2 and sa.program_id=?3 and sa.program_specialization_id=?4 "
			+ "and sa.current_year_sem=?5")
	public List<HashMap<String, Object>> getSectionName(Integer ac_year_id, Integer school_id, Integer program_id,
			Integer program_specialization_id, Integer current_year_sem);
	
	@Query(value = "Select count(*) From SectionAssignment sa "
			+ "where sa.ac_year_id=?1 and sa.school_id=?2 and sa.program_id =?3 and sa.program_specialization_id=?4 and "
			+ "sa.section_id=?5 and sa.current_year_sem =?6 and sa.program_assignment_id=?7 and sa.active=true")
	public Integer countOfAssignedSection(Integer ac_year_id, Integer school_id, Integer program_id,
			Integer program_specialization_id, Integer section_id, Integer current_year_sem,
			Integer program_assignment_id);
	
	@Query(value ="Select count(*) From SectionAssignment sa where sa.section_assignment_id != ?1 and sa.ac_year_id=?2 and sa.school_id=?3 and sa.program_id =?4 and sa.program_specialization_id=?5 and sa.section_id=?6 and sa.current_year_sem =?7 and sa.active=true")
	public Integer countOfAssignedSectionForUpdate(Integer section_assignment_id,Integer ac_year_id,Integer school_id,Integer program_id,Integer program_specialization_id,Integer section_id,Integer current_year_sem);
	
//	@Query(value = "Select s From Section s where s.section_id IN (select sa.section_id from SectionAssignment sa where sa.school_id=?1 and "
//			+ "sa.program_id=?2 and sa.ac_year_id=?3 and sa.current_year_sem=?4) and b.active=true")
//	public List<Map<String, Object>> fetchAllSectionsForTimeTableOnSem(Integer school_id,Integer program_id,Integer ac_year_id,Integer current_year_sem);
//	
//	@Query(value = "Select s From Section s where s.section_id IN (select sa.section_id from SectionAssignment sa where sa.school_id=?1 and "
//			+ "sa.program_id=?2 and sa.ac_year_id=?3 and sa.current_year_sem=?4) and b.active=true")
//	public List<Map<String, Object>> fetchAllSectionsForTimeTableOnYear(Integer school_id,Integer program_id,Integer ac_year_id,Integer current_year_sem);
	
	@Query(value = "select new map(sa.section_assignment_id as section_assignment_id,sa.section_id as section_id,"
			+ "s.section_name as section_name) from SectionAssignment sa left join Section s on sa.section_id=s.section_id "
			+ "where sa.ac_year_id=?3 and sa.school_id=?1 and sa.program_specialization_id=?2 "
			+ "and sa.current_year_sem=?4 And sa.active=true And s.active=true")
	public List<HashMap<String, Object>> getAllSectionsForTimeTable(Integer school_id,Integer program_specialization_id,Integer ac_year_id,Integer current_year_sem);
	
	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,rs.current_sem,"
			+ "ay.ac_year,ay.ac_year_id,rs.reporting_date FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id "
			+ "left join academic_year ay on ay.ac_year_id = ?1 " 
			+ "where s.program_specialization_id=?2 and rs.current_sem=?3 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>>  studentDetailsForPromotingOnSem(Integer ac_year_id,Integer program_specialization_id,Integer current_year_sem);
	
	@Query(value = "SELECT s.student_id,s.auid,s.usn,s.student_name,s.active,rs.eligible_reported_status,rs.current_year,"
			+ "ay.ac_year,ay.ac_year_id,rs.reporting_date FROM student_details s "
			+ "left join reporting_students rs on rs.student_id=s.student_id "
			+ "left join academic_year ay on ay.ac_year_id = ?1 " 
			+ "where s.program_specialization_id=?2 and rs.current_year=?3 and s.active=true", nativeQuery = true)
	public List<Map<String, Object>>  studentDetailsForPromotingOnYear(Integer ac_year_id,Integer program_specialization_id,Integer current_year_sem);
	
	@Query(value = "select sa from SectionAssignment sa where sa.section_id=?1 and sa.active=true")
	public List<SectionAssignment> getCountOfStudentsAssignedToSection(Integer section_id);
	
	@Query(value ="Select sa.section_assignment_id From section_assignment sa "
			+ "Where FIND_IN_SET(?1,sa.student_ids) > 0 And sa.current_year_sem =?2 And sa.ac_year_id =?3 And sa.active=true",nativeQuery=true)
	public List<Integer> getSectionAssignmentId(Integer student_id,Integer current_year_sem,Integer ac_year_id);
	
	@Query(value ="Select sa.section_assignment_id From section_assignment sa "
			+ "Where FIND_IN_SET(?1,sa.student_ids) > 0 And sa.current_year_sem =?2 And sa.active=true",nativeQuery=true)
	public List<Integer> getSectionAssignmentId(Integer student_id,Integer current_year_sem);
	
	@Query(value ="Select sa from SectionAssignment sa "
			+ "Where sa.school_id=?1 And sa.program_assignment_id=?2 And sa.program_id =?3 And sa.program_specialization_id=?4 And sa.section_id =?5 And sa.current_year_sem=?6 And sa.active=true")
	public List<SectionAssignment> getSectionAssignmentDetailsForStudentIds(Integer school_id,Integer program_assignment_id,Integer program_id, Integer program_specialization_id,Integer section_id, Integer current_year_sem);
	

	@Query(value ="Select sa.section_assignment_id From section_assignment sa "
			+ "Where FIND_IN_SET(:student_id,sa.student_ids) > 0 ",nativeQuery=true)
	public Integer getSectionAssignmentIdByStudentId(Integer student_id);

	@Query(value ="Select sa.section_id From section_assignment sa "
			+ "Where FIND_IN_SET(?1,sa.student_ids) > 0 ",nativeQuery=true)
	public Integer getSectionIdByStudentId(Integer student_id);

	@Query(value = "select sa from SectionAssignment sa where sa.section_assignment_id=?1 And sa.active=true")
	public SectionAssignment activeSectionAssignmentDetail(Integer section_assignment_id);

	@Query(value = "select s.section_name as section_name,s.section_id as section_id From section s "
			+ "Where s.section_id in (select sa.section_id From section_assignment sa Where sa.ac_year_id=?1 And sa.program_specialization_id=?2 And sa.current_year_sem=?3 And sa.active=true) And s.active=true",nativeQuery=true)
	public List<Map<String, Object>> sectionDetailsByAcademicYearAndSection(Integer ac_year_id,Integer program_specialization_id,Integer current_year_sem);

	@Query(value = "Select sa.section_assignment_id from section_assignment sa where FIND_IN_SET(?1,sa.student_ids) > 0 And sa.ac_year_id=?2 "
			+ "And sa.program_assignment_id=?3 And sa.program_id=?4 And sa.program_specialization_id=?5 And sa.current_year_sem=?6 And sa.active=true",nativeQuery=true)
	public List<Integer> allAssignedSectionAssignemtOfStudentBySem(Integer studentId,Integer ac_year_id, Integer program_assignment_id,
			Integer program_id, Integer program_specialization_id, Integer current_year_sem);

		@Query(value = "Select sa.section_assignment_id from section_assignment sa where FIND_IN_SET(?1,sa.student_ids) > 0 And sa.ac_year_id=?2 "
			+ "And sa.program_assignment_id=?3 And sa.program_id=?4 And sa.program_specialization_id=?5 And sa.current_year=?6 And sa.active=true",nativeQuery=true)
	public List<Integer> allAssignedSectionAssignemtOfStudentByYear(Integer studentId,Integer ac_year_id, Integer program_assignment_id,
			Integer program_id, Integer program_specialization_id, Integer current_year_sem);

		@Query(value = "Select sa from SectionAssignment sa where sa.ac_year_id=?1 And sa.program_assignment_id=?2 And sa.program_id=?3 And sa.program_specialization_id=?4 And sa.section_id=?5 And sa.current_year_sem=?6 And sa.active=true")
		public List<SectionAssignment> getSectionAssignedStudentDetailsBySem(Integer ac_year_id, Integer program_assignment_id,
				Integer program_id, Integer program_specialization_id, Integer section_id, Integer current_year_sem);

		@Query(value = "Select sa from SectionAssignment sa where sa.ac_year_id=?1 And sa.program_id=?2 And sa.program_specialization_id=?3 And sa.section_id=?4 And sa.current_year_sem=?5 And sa.active=true")
		public List<SectionAssignment> sectionAssignedStudentDetailsBySem(Integer ac_year_id, Integer program_id, Integer program_specialization_id, Integer section_id, Integer current_year_sem);

		@Query(value = "Select sa from SectionAssignment sa where sa.ac_year_id=?1 And sa.program_assignment_id=?2 And sa.program_id=?3 And sa.program_specialization_id=?4 And sa.section_id=?5 And sa.current_year=?6 And sa.active=true")
		public List<SectionAssignment> getSectionAssignedStudentDetailsByYear(Integer ac_year_id, Integer program_assignment_id,
				Integer program_id, Integer program_specialization_id, Integer section_id, Integer current_year_sem);

	@Query(value = "Select sa from SectionAssignment sa where sa.ac_year_id=?1 And sa.program_id=?2 And sa.program_specialization_id=?3 And sa.section_id=?4 And sa.current_year=?5 And sa.active=true")
	public List<SectionAssignment> sectionAssignedStudentDetailsByYear(Integer ac_year_id, Integer program_id, Integer program_specialization_id, Integer section_id, Integer current_year_sem);
		
		@Query(value = "select sa.program_specialization_id from SectionAssignment sa where sa.section_assignment_id=?1 and sa.active=true")
		public List<Integer> programSpecilizationIdsBySectionAssignmentId(Integer section_assignment_id);

		@Query(value = "Select sa.program_assignment_id from SectionAssignment sa where sa.section_assignment_id in (?1) and sa.active=true")
		public List<Integer> programAssignmentIdsBySectionAssignmentId(List<Integer> sectionAssignmentIds);

		@Query(value = "Select sa from SectionAssignment sa where sa.ac_year_id=?1 And sa.program_assignment_id=?2 And sa.program_id=?3 And sa.program_specialization_id=?4 And sa.section_id=?5 And sa.current_year_sem=?6 And sa.active=true")
		public List<SectionAssignment> getSectionAssignedStudentDetails(Integer ac_year_id, Integer program_assignment_id,
				Integer program_id, Integer program_specialization_id, Integer section_id, Integer current_year_sem);

		
		@Query(value = "SELECT sa.section_assignment_id as id,sa.remarks as remarks,sa.section_assignment_id as section_assignment_id,"
				+ "sa.current_year_sem as current_year_sem,sa.active as active,sec.section_name as section_name,sa.created_by as created_by,"
				+ "(select (LENGTH(sa.student_ids) - LENGTH(REPLACE(sa.student_ids,\",\",\"\")) + 1)) as count_of_students,"
				+ "sa.section_id as section_id,ay.ac_year as ac_year,sa.ac_year_id as ac_year_id,sa.created_date as created_date,sa.created_username as created_username,"
				+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
				+ "sa.program_specialization_id as program_specialization_id,p.program_short_name as program_short_name,sa.student_ids as student_ids,"
				+ "pa.program_assignment_id As program_assignment_id,pa.program_type As program_type,pa.program_type_id As program_type_id,"
				+ "de.dept_id As dept_id,de.dept_name_short As dept_name_short,de.dept_name As dept_name,"
				+ "p.program_name as program_name,sa.program_id as program_id,sa.school_id as school_id,sch.school_name as school_name,"
				+ "sch.school_name_short as school_name_short FROM section_assignment sa "
				+ "left join schools sch on sa.school_id=sch.school_id "
				+ "left join program p on sa.program_id=p.program_id "
				+ "left join program_assignment pa on sa.program_assignment_id=pa.program_assignment_id "
				+ "left join program_specialization ps on sa.program_specialization_id=ps.program_specialization_id "
				+ "left join department de on ps.dept_id=de.dept_id "
				+ "left join academic_year ay on sa.ac_year_id=ay.ac_year_id "
				+ "left join section sec on sa.section_id=sec.section_id "
				+ "Where (:school_id is null or sa.school_id = :school_id) "
				+ "And (:dept_id is null or de.dept_id = :dept_id) "
				+ "And CONCAT(IfNull(sa.section_assignment_id,''),'',IfNull(sa.current_year_sem,''),'',"
				+ "IfNull(sec.section_name,''),'',IfNull(ay.ac_year,''),'',IfNull(ps.program_specialization_short_name,''),'',"
				+ "IfNull(ps.program_specialization_name,''),'',IfNull(p.program_short_name,''),'',IfNull(p.program_name,''),'',"
				+ "IfNull(sch.school_name,''),'',IfNull(sch.school_name_short,'')) LIKE %:keyword% Group by sa.section_assignment_id",nativeQuery =true)
		public List<Map<String, Object>> fetchAllSectionAssignmentDetailsBasedOnSchoolId(Pageable pageable, Integer school_id,
				Object keyword, Integer dept_id);

		@Query(value = "SELECT sa.section_assignment_id as id,sa.remarks as remarks,sa.section_assignment_id as section_assignment_id,"
				+ "sa.current_year_sem as current_year_sem,sa.active as active,sec.section_name as section_name,sa.created_by as created_by,"
				+ "(select (LENGTH(sa.student_ids) - LENGTH(REPLACE(sa.student_ids,\",\",\"\")) + 1)) as count_of_students,"
				+ "sa.section_id as section_id,ay.ac_year as ac_year,sa.ac_year_id as ac_year_id,sa.created_date as created_date,sa.created_username as created_username,"
				+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
				+ "sa.program_specialization_id as program_specialization_id,p.program_short_name as program_short_name,sa.student_ids as student_ids,"
				+ "pa.program_assignment_id As program_assignment_id,pa.program_type As program_type,pa.program_type_id As program_type_id,"
				+ "de.dept_id As dept_id,de.dept_name_short As dept_name_short,de.dept_name As dept_name,"
				+ "p.program_name as program_name,sa.program_id as program_id,sa.school_id as school_id,sch.school_name as school_name,"
				+ "sch.school_name_short as school_name_short FROM section_assignment sa "
				+ "left join schools sch on sa.school_id=sch.school_id "
				+ "left join program p on sa.program_id=p.program_id "
				+ "left join program_assignment pa on sa.program_assignment_id=pa.program_assignment_id "
				+ "left join program_specialization ps on sa.program_specialization_id=ps.program_specialization_id "
				+ "left join department de on ps.dept_id=de.dept_id "
				+ "left join academic_year ay on sa.ac_year_id=ay.ac_year_id "
				+ "left join section sec on sa.section_id=sec.section_id "
				+ "Where (:school_id is null or sa.school_id = :school_id) "
				+ "And (:dept_id is null or de.dept_id = :dept_id) "
				+ "Group by sa.section_assignment_id",nativeQuery =true)
		public List<Map<String,Object>> fetchAllSectionAssignmentDetailsBasedOnSchoolId1(Pageable pageable, Integer school_id, Integer dept_id);

		
		@Query(value = "SELECT sa.section_assignment_id as id,sa.remarks as remarks,sa.section_assignment_id as section_assignment_id,"
				+ "sa.current_year_sem as current_year_sem,sa.active as active,sec.section_name as section_name,sa.created_by as created_by,"
				+ "(select (LENGTH(sa.student_ids) - LENGTH(REPLACE(sa.student_ids,\",\",\"\")) + 1)) as count_of_students,"
				+ "sa.section_id as section_id,ay.ac_year as ac_year,sa.ac_year_id as ac_year_id,sa.created_date as created_date,sa.created_username as created_username,"
				+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
				+ "sa.program_specialization_id as program_specialization_id,p.program_short_name as program_short_name,sa.student_ids as student_ids,"
				+ "pa.program_assignment_id As program_assignment_id,pa.program_type As program_type,pa.program_type_id As program_type_id,"
				+ "p.program_name as program_name,sa.program_id as program_id,sa.school_id as school_id,sch.school_name as school_name,"
				+ "sch.school_name_short as school_name_short FROM section_assignment sa "
				+ "left join schools sch on sa.school_id=sch.school_id "
				+ "left join program p on sa.program_id=p.program_id "
				+ "left join program_assignment pa on sa.program_assignment_id=pa.program_assignment_id "
				+ "left join program_specialization ps on sa.program_specialization_id=ps.program_specialization_id "
				+ "left join academic_year ay on sa.ac_year_id=ay.ac_year_id "
				+ "left join section sec on sa.section_id=sec.section_id "
				+ "Where sa.created_by=?2 And CONCAT(IfNull(sa.section_assignment_id,''),'',IfNull(sa.current_year_sem,''),'',"
				+ "IfNull(sec.section_name,''),'',IfNull(ay.ac_year,''),'',IfNull(ps.program_specialization_short_name,''),'',"
				+ "IfNull(ps.program_specialization_name,''),'',IfNull(p.program_short_name,''),'',IfNull(p.program_name,''),'',"
				+ "IfNull(sch.school_name,''),'',IfNull(sch.school_name_short,'')) LIKE %?1% Group by sa.section_assignment_id",nativeQuery =true)
		public List<Map<String, Object>> fetchAllSectionAssignmentDetailsBasedOnUserId(Pageable pageable,
				Integer created_by, Object keyword);

		@Query(value = "SELECT sa.section_assignment_id as id,sa.remarks as remarks,sa.section_assignment_id as section_assignment_id,"
				+ "sa.current_year_sem as current_year_sem,sa.active as active,sec.section_name as section_name,sa.created_by as created_by,"
				+ "(select (LENGTH(sa.student_ids) - LENGTH(REPLACE(sa.student_ids,\",\",\"\")) + 1)) as count_of_students,"
				+ "sa.section_id as section_id,ay.ac_year as ac_year,sa.ac_year_id as ac_year_id,sa.created_date as created_date,sa.created_username as created_username,"
				+ "ps.program_specialization_short_name as program_specialization_short_name,ps.program_specialization_name as program_specialization_name,"
				+ "sa.program_specialization_id as program_specialization_id,p.program_short_name as program_short_name,sa.student_ids as student_ids,"
				+ "pa.program_assignment_id As program_assignment_id,pa.program_type As program_type,pa.program_type_id As program_type_id,"
				+ "p.program_name as program_name,sa.program_id as program_id,sa.school_id as school_id,sch.school_name as school_name,"
				+ "sch.school_name_short as school_name_short FROM section_assignment sa "
				+ "left join schools sch on sa.school_id=sch.school_id "
				+ "left join program p on sa.program_id=p.program_id "
				+ "left join program_assignment pa on sa.program_assignment_id=pa.program_assignment_id "
				+ "left join program_specialization ps on sa.program_specialization_id=ps.program_specialization_id "
				+ "left join academic_year ay on sa.ac_year_id=ay.ac_year_id "
				+ "left join section sec on sa.section_id=sec.section_id where sa.created_by=?1 Group by sa.section_assignment_id",nativeQuery =true)
		public List<Map<String,Object>> fetchAllSectionAssignmentDetailsBasedOnUserId1(Pageable pageable1, Integer created_by);
		
		@Query(value ="Select sa.section_assignment_id From section_assignment sa "
				+ "Where FIND_IN_SET(:student_id,sa.student_ids) > 0 And sa.current_year_sem=:current_sem And sa.active=true ",nativeQuery=true)
		public List<Integer> getSectionAssignmentIdBySem(Integer student_id, Integer current_sem);
		
		@Query(value ="Select sa.section_assignment_id From section_assignment sa "
				+ "Where FIND_IN_SET(:student_id,sa.student_ids) > 0 And sa.current_year=:current_year And sa.active=true ",nativeQuery=true)
		public List<Integer> getSectionAssignmentIdByYear(Integer student_id, Integer current_year);
		
		
		@Query(value = "Select sa.section_assignment_id from section_assignment sa where FIND_IN_SET(?1,sa.student_ids) > 0 "
				+ "And sa.program_assignment_id=?2 And sa.program_id=?3 And sa.program_specialization_id=?4 And sa.current_year_sem=?5 And sa.active=true",nativeQuery=true)
		public List<Integer> allAssignedSectionAssignemtOfStudentWithoutAcademicYearBySem(Integer studentId, Integer program_assignment_id,
				Integer program_id, Integer program_specialization_id, Integer current_year_sem);
		
		@Query(value = "Select sa.section_assignment_id from section_assignment sa where FIND_IN_SET(?1,sa.student_ids) > 0 "
				+ "And sa.program_assignment_id=?2 And sa.program_id=?3 And sa.program_specialization_id=?4 And sa.current_year=?5 And sa.active=true",nativeQuery=true)
		public List<Integer> allAssignedSectionAssignemtOfStudentWithoutAcademicYearByYear(Integer studentId, Integer program_assignment_id,
				Integer program_id, Integer program_specialization_id, Integer current_year_sem);

		@Query(value = "SELECT ca.course_assignment_id As course_assignment_id,c.course_name As course_name,ca.course_id As course_id, "
				+ "concat(c.course_name,'-',c.course_code) as concateCourse,ca.program_specialization_id As program_specialization_id, "
				+ "ca.program_assignment_id As program_assignment_id,ca.program_id As program_id "
				+ "FROM student_attendance sa  "
				+ "left join course_assignment ca on ca.course_assignment_id = sa.course_assignment_id "
				+ "left join course c on ca.course_id = c.course_id "
				+ "where (:school_id IS NULL OR sa.school_id = :school_id) "
				+ "And (:ac_year_id IS NULL OR ca.ac_year_id = :ac_year_id) "
				+ "And (:program_specialization_id IS NULL OR ca.program_specialization_id = :program_specialization_id) "
				+ "And (:year_sem IS NULL OR ca.year_sem = :year_sem) And sa.active=true group by ca.course_assignment_id",nativeQuery=true)
		public List<Map<String, Object>> getCourseDetailData(Integer school_id, Integer ac_year_id,
				Integer program_specialization_id, Integer year_sem);

		@Query(value = "select sa.section_id As section_id,s.section_name As section_name,sa.section_assignment_id As section_assignment_id "
				+ "FROM acharya_erp.section_assignment sa "
				+ "left join acharya_erp.section s on s.section_id=sa.section_id "
				+ "where(:school_id IS NULL OR sa.school_id = :school_id) "
				+ "And (:ac_year_id IS NULL OR sa.ac_year_id = :ac_year_id) "
				+ "And (:program_specialization_id IS NULL OR sa.program_specialization_id = :program_specialization_id) "
				+ "And (:current_year_sem IS NULL OR sa.current_year_sem = :current_year_sem) "
				+ "And sa.active=true ",nativeQuery=true)
		public List<Map<String, Object>> getSectionDetailData(Integer school_id, Integer ac_year_id,
				Integer program_specialization_id, Integer current_year_sem);

		@Query(value = "select sa.section_id FROM section_assignment sa "
				+ "left join section s on s.section_id=sa.section_id "
				+ "WHERE FIND_IN_SET(?1, sa.student_ids) > 0",nativeQuery=true)
		public Integer getSectionId(Integer student_id);
}