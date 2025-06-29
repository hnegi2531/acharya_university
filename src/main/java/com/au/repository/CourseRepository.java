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
import com.au.model.Course;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository<Course, Integer> {

	@Query(value = "select c from Course c where c.active=true")
	public List<Course> findAll1();

	@Modifying
	@Query(value = "update Course c set c.active=false where c.course_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Course c set c.active=true where c.course_id=?1")
	public void update1(Integer id);

	@Query(value = "SELECT distinct(c.course_name),c.course_id as course_id,c.course_code as course_code from course c "
			+ "left join course_assignment ca on c.course_id=ca.course_id left join course_category cc "
			+ "on ca.course_category_id=cc.course_category_id where cc.course_category_code "
			+ "IN ('DSE','OEC','SESB','SEVB') and ca.active=true",nativeQuery = true)
	public List<Map<String, Object>> fetchAll();
	@Query(value ="Select new map(c.course_id as id,c.course_name as course_name,c.course_code as course_code,c.duration as duration,"
			+ "c.course_short_name as course_short_name,c.created_date as created_date,c.modified_date as modified_date,"
			+ "c.created_by as created_by,c.modified_by as modified_by,c.active as active,c.created_username as created_username,"
			+ "c.modified_username as modified_username,ctd.category_detail as category_detail,c.org_id as org_id,org.org_type as org_type) From Course c "
			+ "left join CategoryTypeDetails ctd on ctd.category_details_id = c.category_details_id "
			+ "left join Organization org on org.org_id = c.org_id "
			+ "Where CONCAT(IfNull(c.course_id,''),'',IfNull(c.course_name,''),'',IfNull(c.course_code,''),'',IfNull(c.course_short_name,''),'',IfNull(c.created_date,''),'',IfNull(c.created_by,''),'',IfNull(c.created_username,'')) LIKE %?1%")
	public Page<Object> findAll2(Pageable pageable, Object keyword);
	
	@Query(value ="Select new map(c.course_id as id,c.course_name as course_name,c.course_code as course_code,c.duration as duration,"
			+ "c.course_short_name as course_short_name,c.created_date as created_date,c.modified_date as modified_date,"
			+ "c.created_by as created_by,c.modified_by as modified_by,c.active as active,c.created_username as created_username,"
			+ "c.modified_username as modified_username,ctd.category_detail as category_detail,c.org_id as org_id,org.org_type as org_type) From Course c "
			+ "left join CategoryTypeDetails ctd on ctd.category_details_id = c.category_details_id "
			+ "left join Organization org on org.org_id = c.org_id ")
	public Page<Object> findAll3(Pageable pageable);
	
	@Query(value = "SELECT count(*) FROM Course c where c.course_name = ?1 and c.course_code =?2 and c.active=true")
	public Integer countOfCourse(String course_name,String course_code);
	
	@Query(value = "SELECT count(*) FROM Course c where c.org_id =?1 and c.course_name = ?2 and c.course_code =?3 and c.active=true")
	public Integer countOfCourseWithOrgnizationId(Integer org_id,String course_name,String course_code);
	
	
	@Query(value = "SELECT count(*) FROM Course c where c.org_id =?1 and c.course_name = ?2 and c.active=true")
	public Integer countOfCourseNameWithOrgId(Integer org_id,String course_name);
	
	@Query(value = "SELECT count(*) FROM Course c where c.course_name = ?1 and c.active=true")
	public Integer countOfCourseName(String course_name);
	
	@Query(value = "SELECT count(*) FROM Course c where c.org_id =?1 and c.course_code = ?2 and c.active=true")
	public Integer countOfCourseCodeWithOrgId(Integer org_id,String course_code);
	
	@Query(value = "SELECT count(*) FROM Course c where c.course_code = ?1 and c.active=true")
	public Integer countOfCourseCode(String course_code);
	
	@Query(value = "SELECT count(*) FROM Course c where c.course_id=?1 and c.org_id Is Not null ")
	public Integer checkingOfCourseCreatedByOrganization(Integer course_id);

	@Query(value = "select c.course_id as course_id,concat(c.course_name,'-',c.course_short_name) as course from Course c where c.course_id IN"
			+ "(select ca.course_id from CourseAssignment ca where ca.school_id=?1 and ca.program_id=?2 and "
			+ "ca.program_specialization_id=?3 and ca.year_sem=?4 and ca.active=true) and c.active=true")
	public List<Map<String,Object>> getCoursesForLessonPlan(Integer school_id, Integer program_id,Integer program_specialization_id, String year_sem);
	
	@Modifying
	@Query(value = "update Course c set c.course_code=?2 where c.course_id=?1 and c.org_id IS Not Null")
	public void updateCourseCode(Integer course_id,String course_code);
	
	@Query(value = "Select c.course_id as course_id,c.course_name as course_name,"
			+ "concat(c.course_name,'-',c.course_code) as course,c.course_short_name as course_short_name from course c "
			+ "where c.course_id in (select ca.course_id from course_assignment ca where ca.school_id=?1 and ca.program_id=?2 and ca.program_specialization_id=?3 and ca.ac_year_id=?4 and ca.year_sem=?5)",nativeQuery = true)
	public List<Map<String, Object>> listActivecourseDatas(Integer school_id,Integer program_id,Integer program_specialization_id,Integer ac_year_id,Integer year_sem);
	
	@Query(value = "select ca.course_assignment_id , c.course_id ,Concat(IfNull(c.course_name,''),'-',IfNull(c.course_code,''),'-',"
			+ "IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course,c.course_code as course_code,ca.course_assignment_coursecode as course_assignment_coursecode "
			+ "from course_assignment ca "
			+ "left join course c on ca.course_id=c.course_id "
			+ "left join program_specialization ps on ps.program_specialization_id=ca.program_specialization_id "
			+ "where ca.active=true",nativeQuery = true)
	public List<Map<String,Object>> getCoursesConcate();
	
	

	@Query(value = "Select i.internal_time_table_id as id,i.exam_time as exam_time,i.week_day as week_day,i.date_of_exam as date_of_exam,"
			+ "c.course_id as course_id,c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "i.time_slots_id as time_slots_id,i.max_marks as max_marks,i.min_marks as min_marks,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots from course c "
			+ "left join internal_time_table i on i.course_id=c.course_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=i.time_slots_id "
			+ "where c.course_id in (select itt.course_id from internal_time_table itt where itt.internal_id=?1)",nativeQuery = true)
	public List<Map<String, Object>> listAllActiveInternalTimeTableDataBasisOfDOE(Integer internal_id);
	
	
	@Query(value = "select cc.course_id as course_id,concat(cc.course_name,'-',cc.course_code) as course from Course cc ")
	public List<Map<String,Object>> getCourseConcat11();


	@Query(value = "select new map(c.course_id as course_id,c.course_name as course_name,c.course_short_name as course_short_name,ca.course_assignment_id As course_assignment_id,"
			+ "c.course_code as course_code) from CourseAssignment ca  "
			+ "left join Course c ON c.course_id=ca.course_id "
			+ "left join CourseStudentAssignment csa ON ca.course_assignment_id=csa.course_assignment_id "
			+ "where csa.student_id=?1 And ca.active=true")
	public List<HashMap<String, Object>> getCourseData(Integer student_id);
	
	@Query(value = "Select i.internal_room_assignment_id as id,isa.exam_time as exam_time,isa.week_day as week_day,isa.date_of_exam as date_of_exam,"
			+ "c.course_id as course_id,c.course_name as course_name,c.course_short_name as course_short_name,"
			+ "ca.course_assignment_id as course_assignment_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode,"
			+ "isa.time_slots_id as time_slots_id,isa.max_marks as max_marks,isa.min_marks as min_marks,"
			+ "CONCAT(tsl.starting_time,' - ',tsl.ending_time) as timeSlots from internal_faculty_room_assignment i "
			+ "left join internal_session_creation isa on i.internal_session_id=isa.internal_session_id "
			+ "left join course_assignment ca on ca.course_assignment_id=isa.course_assignment_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "Left join time_slots tsl on tsl.time_slots_id=isa.time_slots_id "
			+ "where i.internal_session_id=?1 and i.active=true",nativeQuery = true)
	public List<Map<String, Object>> listAllActiveInternalFacultyRoomAssignmentDataBasisOfDOE(Integer internal_session_id);
	
	@Query(value = "Select c.course_id as course_id,ca.course_assignment_id as course_assignment_id,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode "
			+ "from course_assignment cass "
			+ "left join course c on cass.course_id=ca.course_id "
			+ "where cass.ac_year_id=?1 and cass.program_specialization_id=?2 and cass.year_sem=?3 and cass.active=true)",nativeQuery = true)
	public List<Map<String, Object>> getCoursesForInternals(Integer ac_year_id,Integer program_specialization_id,Integer year_sem);

	@Query(value = "Select c.course_id as course_id,ca.course_assignment_id as course_assignment_id,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode "
			+ "from course_assignment ca "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join subject_assignment sa on sa.course_assignment_id=ca.course_assignment_id "
			+ "left join time_table tt on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left join batch_program_assignment bpa on bpa.batch_assignment_id=tt.batch_assignment_id "
			+ "where "
			+ "tt.school_id=:school_id and tt.ac_year_id=:ac_year_id and "
			+ "ca.program_specialization_id =:program_specialization_id and tt.current_sem=:current_sem and tt.active=true "
			+ "Group by c.course_id",nativeQuery = true)
	public List<Map<String, Object>> getCoursesForInternalsOnSem(Integer school_id, Integer ac_year_id,Integer program_specialization_id, Integer current_sem);

    @Query(value = "Select c.course_id as course_id,ca.course_assignment_id as course_assignment_id,"
            + "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode "
            + "from course_assignment ca "
            + "Inner join course c on c.course_id=ca.course_id "
            + "Inner join category_type_details ctd on ctd.category_details_id = c.category_details_id "
            + "where ca.program_specialization_id =:program_specialization_id and ca.year_sem=:year_sem and ca.active=true and ctd.category_detail Like '%Common Course%'",nativeQuery = true)
    public List<Map<String, Object>> getCommonCoursesForInternals(Integer program_specialization_id, Integer year_sem);

	@Query(value = "Select c.course_id as course_id,ca.course_assignment_id as course_assignment_id,"
			+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,'')) as course_with_coursecode "
			+ "from course_assignment ca "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join subject_assignment sa on sa.course_assignment_id=ca.course_assignment_id "
			+ "left join time_table tt on sa.subjet_assign_id=tt.subject_assignment_id "
			+ "left join batch_program_assignment bpa on bpa.batch_assignment_id=tt.batch_assignment_id "
			+ "where tt.school_id=?1 and tt.ac_year_id=?2 and "
			+ "ca.program_specialization_id =?3 and tt.current_year=?4 and tt.active=true Group by c.course_id",nativeQuery = true)
	public List<Map<String, Object>> getCoursesForInternalsOnyear(Integer school_id, Integer ac_year_id,Integer program_specialization_id, Integer current_year);

	@Query(value = "select c.org_id from Course c where c.course_id=?1 And c.active=true")
	public Integer getOrgId(Integer course_id);

	@Query(value = "select c.course_code from Course c where c.course_id=?1 And c.active=true")
	public String getCourseCode(Integer course_id);

	@Query(value = "select c.course_id from Course c "
			+ "Inner join CategoryTypeDetails ctd on ctd.category_details_id = c.category_details_id Where ctd.category_detail LIKE '%Common%'")
	public List<Integer> commonCourseIds();
	
	@Query(value = "select c from Course c where c.course_short_name=:courseShortName ")
	public Course findCourseByNameAndShortName(String courseShortName);

//	@Query(value = "select c.course_id, c.course_name, c.course_short_name, c.course_code, c.duration from course where c.course_id= ")
//	public Map<String, Object> getCourseDetails(Integer course_id);

}


