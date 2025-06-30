package com.au.repository;

import java.util.HashMap;
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
import com.au.model.CourseAssignment;
import com.au.model.CoursePattern;

@Transactional
@Repository
public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, Integer> {

	@Query(value = "select ca from CourseAssignment ca where ca.active=true")
	public List<CourseAssignment> findAll1();

	@Query(value = "select new map(ca.course_assignment_id as id,c.course_name as course_name,ca.user_id As user_id,"
			+ "c.course_short_name as course_short_name,ca.created_date as created_date,ca.modified_date as modified_date,"
			+ "ca.course_category_id as course_category_id,ca.total_credit as total_credit,ca.lecture as lecture,"
			+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.dept_id as dept_id,"
			+ "ca.cie_marks as cie_marks,ca.see_marks as see_marks,ca.created_by as created_by,ca.modified_by as modified_by,"
			+ "ca.active as active,ca.school_id as school_id,ca.created_username as created_username,ca.year_sem as year_sem,"
			+ "ca.modified_username as modified_username,ct.course_type_name as course_type_name,c.course_code as course_code,"
			+ "cc.course_category_name as course_category_name,d.dept_name_short as dept_name_short,"
			+ "s.school_name_short as school_name_short) from CourseAssignment ca "
			+ "left join CourseType ct on ca.course_type_id=ct.course_type_id left join Course c on ca.course_id=c.course_id "
			+ "left join CourseCategory cc on ca.course_category_id=cc.course_category_id "
			+ "left join Department d on ca.dept_id=d.dept_id "
			+ "left join Schools s on ca.school_id=s.school_id "
			+ "left join Syllabus sy on ca.syllabus_id=sy.syllabus_id "
			+ "Where CONCAT(IfNull(ca.course_assignment_id,''),'',IfNull(c.course_name,''),'',IfNull(c.course_short_name,''),'',"
			+ "IfNull(ca.created_date,''),'',IfNull(ca.created_by,''),'',IfNull(ca.created_username,''),'',IfNull(ca.year_sem,''),'',"
			+ "IfNull(cc.course_category_name,''),'',IfNull(d.dept_name_short,''),'',IfNull(s.school_name_short,''),'',IfNull(c.course_code,'')) LIKE %?1%")
	public Page<Object> fetchAllDetail1(Pageable pageable, Object keyword);
	
	
	@Query(value = "select new map(ca.course_assignment_id as id,c.course_name as course_name,ca.user_id As user_id,"
			+ "c.course_short_name as course_short_name,ca.created_date as created_date,ca.modified_date as modified_date,"
			+ "ca.course_category_id as course_category_id,ca.total_credit as total_credit,ca.lecture as lecture,"
			+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.dept_id as dept_id,"
			+ "ca.cie_marks as cie_marks,ca.see_marks as see_marks,ca.created_by as created_by,ca.modified_by as modified_by,"
			+ "ca.active as active,ca.school_id as school_id,ca.created_username as created_username,ca.year_sem as year_sem,"
			+ "ca.modified_username as modified_username,ct.course_type_name as course_type_name,c.course_code as course_code,"
			+ "cc.course_category_name as course_category_name,d.dept_name_short as dept_name_short,"
			+ "s.school_name_short as school_name_short) from CourseAssignment ca "
			+ "left join CourseType ct on ca.course_type_id=ct.course_type_id left join Course c on ca.course_id=c.course_id "
			+ "left join CourseCategory cc on ca.course_category_id=cc.course_category_id "
			+ "left join Department d on ca.dept_id=d.dept_id "
			+ "left join Schools s on ca.school_id=s.school_id "
			+ "left join Syllabus sy on ca.syllabus_id=sy.syllabus_id")
	public Page<Object> fetchAllDetail2(Pageable pageable);
	
	
	@Query(value = "select new map(ca.course_assignment_id as id,c.course_name as course_name,ca.user_id As user_id,"
			+ "c.course_short_name as course_short_name,ca.created_date as created_date,ca.modified_date as modified_date,"
			+ "ca.course_category_id as course_category_id,ca.total_credit as total_credit,ca.lecture as lecture,"
			+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.dept_id as dept_id,"
			+ "ca.cie_marks as cie_marks,ca.see_marks as see_marks,ca.created_by as created_by,ca.modified_by as modified_by,"
			+ "ca.active as active,ca.school_id as school_id,ca.created_username as created_username,ca.year_sem as year_sem,"
			+ "ca.modified_username as modified_username,ct.course_type_name as course_type_name,c.course_code as course_code,"
			+ "cc.course_category_name as course_category_name,d.dept_name_short as dept_name_short,"
			+ "s.school_name_short as school_name_short) from CourseAssignment ca "
			+ "left join CourseType ct on ca.course_type_id=ct.course_type_id left join Course c on ca.course_id=c.course_id "
			+ "left join CourseCategory cc on ca.course_category_id=cc.course_category_id "
			+ "left join Department d on ca.dept_id=d.dept_id "
			+ "left join Schools s on ca.school_id=s.school_id "
			+ "left join Syllabus sy on ca.syllabus_id=sy.syllabus_id "
			+ "Where ca.user_id=?2 And CONCAT(IfNull(ca.course_assignment_id,''),'',IfNull(c.course_name,''),'',IfNull(c.course_short_name,''),'',"
			+ "IfNull(ca.created_date,''),'',IfNull(ca.created_by,''),'',IfNull(ca.created_username,''),'',IfNull(ca.year_sem,''),'',"
			+ "IfNull(cc.course_category_name,''),'',IfNull(d.dept_name_short,''),'',IfNull(s.school_name_short,''),'',IfNull(c.course_code,'')) LIKE %?1%")
	public Page<Object> listAll1BasedOnUseraId(Pageable pageable, Object keyword, Integer user_id);
	
	
	@Query(value = "select new map(ca.course_assignment_id as id,c.course_name as course_name,ca.user_id As user_id,"
			+ "c.course_short_name as course_short_name,ca.created_date as created_date,ca.modified_date as modified_date,"
			+ "ca.course_category_id as course_category_id,ca.total_credit as total_credit,ca.lecture as lecture,"
			+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.dept_id as dept_id,"
			+ "ca.cie_marks as cie_marks,ca.see_marks as see_marks,ca.created_by as created_by,ca.modified_by as modified_by,"
			+ "ca.active as active,ca.school_id as school_id,ca.created_username as created_username,ca.year_sem as year_sem,"
			+ "ca.modified_username as modified_username,ct.course_type_name as course_type_name,c.course_code as course_code,"
			+ "cc.course_category_name as course_category_name,d.dept_name_short as dept_name_short,"
			+ "s.school_name_short as school_name_short) from CourseAssignment ca "
			+ "left join CourseType ct on ca.course_type_id=ct.course_type_id left join Course c on ca.course_id=c.course_id "
			+ "left join CourseCategory cc on ca.course_category_id=cc.course_category_id "
			+ "left join Department d on ca.dept_id=d.dept_id "
			+ "left join Schools s on ca.school_id=s.school_id "
			+ "left join Syllabus sy on ca.syllabus_id=sy.syllabus_id Where ca.user_id=?1")
	public Page<Object> listAll2BasedOnUseraId(Pageable pageable, Integer user_id);

	@Modifying
	@Query(value = "update CourseAssignment ca set ca.active=false where ca.course_assignment_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update CourseAssignment ca set ca.active=true where ca.course_assignment_id=?1")
	public void update1(Integer id);

	@Query(value = "select new map(ca.course_assignment_id as course_assignment_id,ca.course_id as course_id,"
			+ "ca.course_type_id as course_type_id,ca.course_category_id as course_category_id,ca.dept_id as dept_id,"
			+ "ca.school_id as school_id,ca.syllabus_id as syllabus_id,ca.total_credit as total_credit,ca.lecture as lecture,"
			+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.cie_marks as cie_marks,"
			+ "ca.see_marks as see_marks,c.course_name as course_name,c.course_code as course_code,ca.year_sem as year_sem,"
			+ "cc.course_category_name as course_category_name)from CourseAssignment ca "
			+ "left join Course c on ca.course_id=c.course_id "
			+ "left join CourseCategory cc on ca.course_category_id=cc.course_category_id where ca.syllabus_id=?1 and ca.active=true")
	public List<HashMap<String, Object>> fetchAllSyllabusDetail(Integer syllabus_id);

	@Query(value = "select new map(ca.course_assignment_id as course_assignment_id,ca.ac_year_id as ac_year_id,ca.remarks as remarks,"
			+ "ca.program_id as program_id,ca.program_specialization_id as program_specialization_id,ca.course_id as course_id,"
			+ "ca.course_type_id as course_type_id,ca.course_category_id as course_category_id,ca.dept_id as dept_id,"
			+ "ca.school_id as school_id,ca.syllabus_id as syllabus_id,ca.total_credit as total_credit,ca.lecture as lecture,"
			+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.cie_marks as cie_marks,"
			+ "ca.see_marks as see_marks,c.course_name as course_name,c.course_code as course_code,ca.year_sem as year_sem,"
			+ "ca.created_date as modified_date,ca.modified_date as modified_date,ca.created_by as created_by,ca.program_assignment_id as program_assignment_id,"
			+ "ca.modified_by as modified_by,ca.active as active,ca.created_username as created_username,"
			+ "ca.modified_username as modified_username,ca.email as email,ca.emp_id as emp_id,ca.course_price as course_price,"
			+ "ca.course_price_usd as course_price_usd,cc.course_category_name as course_category_name)"
			+ "from CourseAssignment ca left join Course c on ca.course_id=c.course_id "
			+ "left join CourseCategory cc on ca.course_category_id=cc.course_category_id  where ca.course_assignment_id=?1")
	public List<HashMap<String, Object>> fetchAllSyllabusDetails(Integer course_assignment_id);

	@Query(value = "select new map(ca.lecture as lecture,ca.tutorial as tutorial,ca.practical as practical,"
			+ "ca.total_credit as total_credit,ca.duration as duration,ca.cie_marks as cie_marks,ca.see_marks as see_marks,"
			+ "ca.course_price as course_price,ca.course_price_usd as course_price_usd,ca.emp_id as emp_id,"
			+ "ca.email as email) from CourseAssignment ca where ca.ac_year_id=?1 and ca.school_id=?2 and ca.program_id=?3 "
			+ "and ca.program_specialization_id=?4 and ca.course_id=?5 and ca.active=true")
	public HashMap<String, Object> fetchAllCreditDetails(Integer ac_year_id, Integer school_id, Integer program_id,
			Integer program_specialization_id, Integer course_id);

	@Query(value = "select new map(ca.course_assignment_id as course_assignment_id) from CourseAssignment ca "
			+ "where ca.ac_year_id=?1 and ca.school_id=?2 and ca.program_id=?3 and ca.dept_id=?4 and ca.program_specialization_id=?5 "
			+ "and ca.course_id=?6 and ca.course_category_id=?7 and ca.year_sem=?8 and ca.active=true ")
	public HashMap<String, Object> fetchAllAmountDetails(Integer ac_year_id, Integer school_id, Integer program_id,Integer dept_id,
			Integer program_specialization_id, Integer course_id, Integer course_category_id, String year_sem);
	
	@Query(value = "select new map(ca.course_assignment_id as course_assignment_id,c.course_name as course_name,"
			+ "c.course_short_name as course_short_name,ca.created_date as created_date,ca.modified_date as modified_date,"
			+ "ca.course_category_id as course_category_id,ca.total_credit as total_credit,ca.lecture as lecture,"
			+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.dept_id as dept_id,"
			+ "ca.cie_marks as cie_marks,ca.see_marks as see_marks,ca.created_by as created_by,ca.modified_by as modified_by,"
			+ "ca.active as active,ca.school_id as school_id,ca.created_username as created_username,ca.year_sem as year_sem,"
			+ "ca.modified_username as modified_username,ct.course_type_name as course_type_name,c.course_code as course_code,"
			+ "cc.course_category_name as course_category_name,d.dept_name_short as dept_name_short,"
			+ "s.school_name_short as school_name_short, "
			+ "pro.program_short_name as program_short_name, prs.program_specialization_short_name as program_specialization_short_name,prt.program_type_name as program_type_name) from CourseAssignment ca "
			+ "left join CourseType ct on ca.course_type_id=ct.course_type_id left join Course c on ca.course_id=c.course_id "
			+ "left join CourseCategory cc on ca.course_category_id=cc.course_category_id "
			+ "left join Program pro on pro.program_id=ca.program_id "
			+ "left join ProgramSpecilization prs on prs.program_specialization_id=ca.program_specialization_id "
			+ "left join ProgramType prt on prt.program_type_id=ca.year_sem "
			+ "left join Department d on ca.dept_id=d.dept_id "
			+ "left join Schools s on ca.school_id=s.school_id "
			+ "left join Syllabus sy on ca.syllabus_id=sy.syllabus_id where ca.ac_year_id=?1 and ca.created_by=?2 and ca.active=true ")
	public List<HashMap<String, Object>> fetchAllCourseAssignmentDetails(Integer ac_year_id,Integer created_by);
	
	@Query(value = "select new map(ca.course_assignment_id as id,c.course_name as course_name,"
			+ "c.course_short_name as course_short_name,ca.program_id as program_id,ca.ac_year_id as ac_year_id,"
			+ "ca.course_category_id as course_category_id,ca.total_credit as total_credit,ca.lecture as lecture,"
			+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.dept_id as dept_id,"
			+ "ca.program_specialization_id as program_specialization_id,ca.see_marks as see_marks,ca.created_by as created_by,"
			+ "ca.active as active,ca.school_id as school_id,ca.created_username as created_username,ca.year_sem as year_sem,"
			+ "ca.modified_username as modified_username,ct.course_type_name as course_type_name,c.course_code as course_code,"
			+ "cc.course_category_name as course_category_name,d.dept_name_short as dept_name_short,p.program_short_name as program_short_name,"
			+ "s.school_name_short as school_name_short,ay.ac_year as ac_year,ps.program_specialization_short_name as program_specialization_short_name,"
			+ "CONCAT(ifNull(c.course_short_name,''),' - ',ifNull(ps.program_specialization_short_name,''),' - ',ifNull(cc.course_category_code,'')) as course_short_name_with_course_category) from CourseAssignment ca "
			+ "left join CourseType ct on ca.course_type_id=ct.course_type_id "
			+ "left join Academic_year ay on ca.ac_year_id=ay.ac_year_id "
			+ "inner join Course c on ca.course_id=c.course_id "
			+ "left join Program p on ca.program_id=p.program_id "
			+ "left join ProgramSpecilization ps on ca.program_specialization_id=ps.program_specialization_id "
			+ "inner join CourseCategory cc on ca.course_category_id=cc.course_category_id "
			+ "left join Department d on ca.dept_id=d.dept_id "
			+ "left join Schools s on ca.school_id=s.school_id "
			+ "left join Syllabus sy on ca.syllabus_id=sy.syllabus_id "
			+ "where ca.school_id=?1 and ca.program_specialization_id=?2 and ca.year_sem =?3 "
			+ "and ca.active=true and c.course_id in (Select ca1.course_id From CourseAssignment ca1 Where ca1.course_category_id in (Select cc1.course_category_id From CourseCategory cc1 where cc1.type LIKE '%Optional%')) ")
	public List<HashMap<String, Object>> fetchAllCourseDetailsForCourseMappingIndex(Integer school_id,Integer program_specialization_id,Integer year_sem);
	
	@Query(value = "Select sa.subjet_assign_id as subjet_assign_id,ca.course_assignment_id as course_assignment_id,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-',"
			+ "IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course_name_with_code "
			+ "from course_assignment ca "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join subject_assignment sa on sa.course_assignment_id=ca.course_assignment_id "
			+ "left join program_specialization ps on ps.program_specialization_id=ca.program_specialization_id "
			+ "left join user_details ua on ua.id=sa.user_id "
			+ "left join employee_details ed on ed.email=ua.email where ed.emp_id IN ?1",nativeQuery = true)
	public List<Map<String, Object>> fetchAllCourseDetailsForTimeTable(List<Integer> emp_ids);

	@Query(value = "Select sa.subjet_assign_id as subjet_assign_id,ca.course_assignment_id as course_assignment_id,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-',"
			+ "IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course_name_with_code "
			+ "from course_assignment ca "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join subject_assignment sa on sa.course_assignment_id=ca.course_assignment_id "
			+ "left join program_specialization ps on ps.program_specialization_id=ca.program_specialization_id "
			+ "left join user_details ua on ua.id=sa.user_id "
			+ "left join employee_details ed on ed.email=ua.email where ed.emp_id IN ?1 and ps.program_specialization_id = ?2",nativeQuery = true)
	public List<Map<String, Object>> fetchAllCourseDetailsForSectionTimeTable(List<Integer> emp_ids,Integer program_specialization_id);

	@Query(value = "Select sa.subjet_assign_id as subjet_assign_id, ca.course_assignment_id as course_assignment_id, Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-', IfNull(ca.year_sem,''),'-', IfNull(ps.program_specialization_short_name,'')) as course_name_with_code "
		+ "from batch_program_assignment bpa "
		+ "inner join program_specialization ps on ps.program_specialization_id = bpa.program_specialization_id "
		+ "inner join course_assignment ca on bpa.program_specialization_id  = ca.program_specialization_id "
		+ "inner join course c on c.course_id = ca.course_id "
		+ "inner join subject_assignment sa on sa.course_assignment_id  = ca.course_assignment_id "
		+ "inner join user_details ud on ud.id = sa.user_id "
		+ "inner join employee_details ed on ed.email = ud.email "
		+ "where ed.emp_id IN ?1 AND bpa.batch_assignment_id = ?2 group by ed.emp_id", nativeQuery = true)
	public List<Map<String, Object>> fetchAllCourseDetailsForBatchTimeTable(List<Integer> emp_ids, Integer batch_assignment_id);
	
	@Query(value ="Select new map(c.course_name as course_name,c.course_short_name as course_short_name,ca.course_assignment_coursecode as course_assignment_coursecode,"
			+ "ca.year_sem as year_sem,cc.course_category_name as course_category_name,cc.type as type,"
			+ "cc.course_category_code as course_category_code,ca.total_credit as total_credit,ca.course_assignment_id as course_assignment_id) From CourseAssignment ca "
			+ "Left Join Course c on ca.course_id=c.course_id "
			+ "Left Join CourseCategory cc on ca.course_category_id=cc.course_category_id "
			+ "Where ca.ac_year_id=?1 And ca.program_specialization_id=?2 And "
			+ "ca.year_sem =?3 And ca.course_category_id in (Select cc1.course_category_id From CourseCategory cc1 where cc1.type LIKE '%Mandatory%' And cc1.active=true)")
	public List<HashMap<String, Object>> mandatoryCourseAssignedToSpecialization(Integer ac_year_id,Integer program_specialization_id,Integer year_sem);
	
	@Query(value = "Select Count(*) From CourseAssignment ca where ca.ac_year_id=?1 And ca.program_specialization_id=?2 And ca.course_id=?3 And ca.active=true")
	public Integer countCourseAssignmentOnAcademicSpecializationAndCourse(Integer ac_year_id,Integer program_specialization_id,Integer course_id);


	@Query(value ="select cc.type from CourseCategory cc where cc.course_category_id=(select ca.course_category_id from CourseAssignment ca where ca.course_id=?1 and ca.school_id=?2 "
			+ "and ca.program_specialization_id=?3 and ca.ac_year_id=?4 and ca.year_sem=?5 and active=true)")
	public String getCourseCategoryType11(Integer course_id,Integer school_id,Integer program_specialization_id,Integer ac_year_id,Integer year_sem);

	@Query(value = "select ca.total_credit,ca.course_assignment_id,ca.course_id,"
			+ "ca.course_assignment_coursecode as course_code,c.course_name,cc.course_category_code "
			+ "from course_assignment ca "
			+ "left join course_student_assignment csa on csa.course_id=ca.course_id "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join course_category cc on cc.course_category_id=ca.course_category_id "
			+ "where ca.year_sem=?1 and ca.program_specialization_id=?2 and ca.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchCourseDetail(Integer year_sem,Integer prog_spec_id);
	
	@Query(value = "select sy.syllabus_id,sy.syllabus_objective,sy.duration,sy.module,sy.topic_name,sy.learning "
			+ "from syllabus sy where sy.course_assignment_id=?1 and sy.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchSyllabusDetails(Integer course_assignment_id);
	
	@Query(value = "select co.course_objective_id as course_objective_id,co.course_objective as course_objective,ca.course_assignment_coursecode as course_code from course_objective co "
			+ "left join course_assignment ca on ca.course_assignment_id=co.course_assignment_id where co.course_assignment_id=?1 and co.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchCourseObjective(Integer course_id);
	
	@Query(value = "select co.course_outcome_id as course_outcome_id,co.course_outcome_objective as course_outcome_objective,ca.course_assignment_coursecode as course_code,"
			+ "co.course_outcome_code as course_outcome_code,co.toxonomy As toxonomy,co.toxonomy_details As toxonomy_details from course_outcome co "
			+ "left join course_assignment ca on ca.course_assignment_id=co.course_assignment_id where co.course_assignment_id=?1 and co.active=true",nativeQuery=true)
	public List<Map<String, Object>> fetchCourseOutcome(Integer course_assignment_id);
	
	@Query(value = "select rb.title_of_book as title_of_book,rb.author as author,rb.yr_of_Publish as yr_of_Publish,rb.edition as edition,"
			+ "ca.course_assignment_coursecode as course_code,rb.eresource as eresource "
			+ "from reference_books rb left join course_assignment ca on ca.course_assignment_id=rb.course_assignment_id "
			+ " where rb.course_assignment_id=?1 and rb.active=true",nativeQuery=true)
	public List<Map<String, Object>> getReferenceBooksDetail(Integer course_assignment_id);
	
	@Query(value = "select ca.course_assignment_id as course_assignment_id,c.course_name as course_name,ca.year_sem as year_sem,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,ca.active as active,caa.course_category_code as course_category_code,"
			+ "sa.subjet_assign_id as subjet_assign_id,caa.course_category_name as course_category_name,ca.total_credit as total_credit,"
			+ "c.course_short_name as course_short_name,c.course_code as course_code,c.course_id as course_id from course_assignment ca "
			+ "left join course c ON c.course_id=ca.course_id "
			+ "left join course_category caa ON caa.course_category_id=ca.course_category_id "
			+ "left join subject_assignment sa ON sa.course_assignment_id=ca.course_assignment_id "
			+ "left join user_details ua on ua.id=sa.user_id "
			+ "left join employee_details ed on ed.email=ua.email where ed.emp_id=?1 And ca.active=true And sa.active=true",nativeQuery=true)
	public List<Map<String, Object>> getCourseDetailsData(Integer emp_id);

	@Query(value="select new map(ay.ac_year_id as ac_year_id,ay.ac_year as ac_year,"
			+ " emp.empcode as emp_code, ca.course_assignment_id as subject_assignment_id,"
			+ " ca.course_id as subject_id, c.course_name as subject_name, "
			+ " c.course_short_name as subject_name_short, c.course_code as subject_code ) from SubjectAssignment sa "
			+ " inner join CourseAssignment ca on ca.course_assignment_id=sa.course_assignment_id  "
			+ " left join EmployeeDetails emp on emp.emp_id=ca.emp_id "
			+ " left join Course c on c.course_id=ca.course_id "
			+ " left join Academic_year ay on ay.ac_year_id=ca.ac_year_id "
			+ "where sa.user_id=:user_id ")
	public List<HashMap<String,Object>> getCoursesByEmployeeId(@Param("user_id") Integer empId);
	
	
	@Query(value = "select ca.course_assignment_id as course_assignment_id,c.course_name as course_name,ca.year_sem as year_sem,"
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,ca.active as active,ca.total_credit as total_credit,"
			+ "pr.program_id As program_id,pr.program_name As program_name,pr.program_short_name As program_short_name,"
			+ "ps.program_specialization_id As program_specialization_id,ps.program_specialization_name As program_specialization_name,"
			+ "ps.program_specialization_short_name As  program_specialization_short_name,"
			+ "c.course_short_name as course_short_name,c.course_code as course_code,c.course_id as course_id from course_assignment ca "
			+ "left join course c ON c.course_id=ca.course_id  "
			+ "left join program pr ON pr.program_id=ca.program_id  "
			+ "left join program_specialization ps ON ps.program_specialization_id=ca.program_specialization_id  "
			+ "where ca.active=true And c.active=true",nativeQuery=true)
	public List<Map<String, Object>> getAllActiveCourseDetailsData();

		@Query(value = "Select ca.course_assignment_id as course_assignment_id,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-',"
			+ "IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course_name_with_code, "
			+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_name as course_name,c.course_id as course_id,c.course_code as course_code "
			+ "from course_assignment ca "
			+ "left join course c on c.course_id=ca.course_id "
			+ "left join program_specialization ps on ps.program_specialization_id=ca.program_specialization_id where ca.program_specialization_id=?1 and ca.year_sem=?2 and ca.school_id=?3 and ca.active=true",nativeQuery = true)
	public List<Map<String, Object>> courseDetailsForStudentsAssignment(Integer program_specialization_id,
			 Integer year_sem, Integer school_id);

		@Query(value = "Select ca.course_assignment_id as course_assignment_id,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-',"
				+ "IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course_name_with_code, "
				+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_name as course_name,c.course_id as course_id,c.course_code as course_code "
				+ "from course_assignment ca "
				+ "left join course c on c.course_id=ca.course_id "
				+ "left join program_specialization ps on ps.program_specialization_id=ca.program_specialization_id where ca.year_sem=?1 and ca.school_id=?2 and ca.course_id in (?3) and ca.active=true",nativeQuery = true)
		public List<Map<String, Object>> commonCourseDetailsForStudentsAssignment(Integer year_sem, Integer school_id,List<Integer> course_ids);

		@Query(value = "select new map(ca.course_assignment_id as id,ca.course_assignment_id as course_assignment_id,c.course_id as course_id,ca.course_assignment_coursecode as course_assignment_coursecode,cc.course_category_id as course_category_id,c.course_name as course_name,"
				+ "c.course_short_name as course_short_name,ca.course_assignment_coursecode as course_code,cc.course_category_name as course_category_name,"
				+ "Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-',"
				+ "IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course_name_with_code,"
				+ "cc.course_category_code as course_category_code ) from CourseAssignment ca "
				+ "left join Course c on ca.course_id=c.course_id and c.active=true "
				+ "left join CourseCategory cc on ca.course_category_id=cc.course_category_id "
				+ "left join ProgramSpecilization ps on ps.program_specialization_id=ca.program_specialization_id "
				+ "where ca.program_id=?1 and ca.program_specialization_id=?2 and ca.year_sem=?3 and ca.active=true ")
		public List<HashMap<String, Object>> fetchAllCourseDetail(Integer program_id, Integer program_specialization_id,Integer year_sem);
		
		
		
		@Query(value = "Select new map( ca.course_assignment_id as course_assignment_id,Concat(IfNull(c.course_name,''),'-',IfNull(ca.course_assignment_coursecode,''),'-',"
				+ "IfNull(ca.year_sem,''),'-',IfNull(ps.program_specialization_short_name,'')) as course_name_with_code, "
				+ "ca.course_assignment_coursecode as course_assignment_coursecode,c.course_name as course_name,c.course_id as course_id,c.course_code as course_code) "
				+ "from CourseAssignment ca "
				+ "left join Course c on c.course_id=ca.course_id and c.active=true "
				+ "left join ProgramSpecilization ps on ps.program_specialization_id=ca.program_specialization_id "
				+ "where ca.year_sem=?1 and ca.course_id in (?2) and ca.active=true")
		public List<HashMap<String, Object>> commonCourseDetailsbyYearOrSem(Integer year_sem, List<Integer> course_ids);
		
		
		@Query(value = "select new map(ca.course_assignment_id as id,c.course_name as course_name,"
				+ "c.course_short_name as course_short_name,ca.program_id as program_id,ca.ac_year_id as ac_year_id,"
				+ "ca.course_category_id as course_category_id,ca.total_credit as total_credit,ca.lecture as lecture,"
				+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.dept_id as dept_id,"
				+ "ca.program_specialization_id as program_specialization_id,ca.see_marks as see_marks,ca.created_by as created_by,"
				+ "ca.active as active,ca.school_id as school_id,ca.created_username as created_username,ca.year_sem as year_sem,"
				+ "ca.modified_username as modified_username,ct.course_type_name as course_type_name,c.course_code as course_code,"
				+ "cc.course_category_name as course_category_name,d.dept_name_short as dept_name_short,p.program_short_name as program_short_name,"
				+ "s.school_name_short as school_name_short,ay.ac_year as ac_year,ps.program_specialization_short_name as program_specialization_short_name,"
				+ "CONCAT(ifNull(c.course_short_name,''),' - ',ifNull(ps.program_specialization_short_name,''),' - ',ifNull(cc.course_category_code,'')) as course_short_name_with_course_category) from CourseAssignment ca "
				+ "left join CourseType ct on ca.course_type_id=ct.course_type_id "
				+ "left join Academic_year ay on ca.ac_year_id=ay.ac_year_id "
				+ "Left join Course c on ca.course_id=c.course_id "
				+ "left join Program p on ca.program_id=p.program_id "
				+ "left join ProgramSpecilization ps on ca.program_specialization_id=ps.program_specialization_id "
				+ "Left join CourseCategory cc on ca.course_category_id=cc.course_category_id "
				+ "left join Department d on ca.dept_id=d.dept_id "
				+ "left join Schools s on ca.school_id=s.school_id "
				+ "left join Syllabus sy on ca.syllabus_id=sy.syllabus_id "
				+ "where ca.active=true ")
		public List<HashMap<String, Object>> allCourseDetailFromCourseAssignment();

		@Query(value = "select cc.course_id as course_id,ca.course_assignment_id as course_assignment_id,"
				+ "Concat(IfNull(ca.course_assignment_coursecode,''),'-',IfNull(cc.course_name,'')) as courseName "
				+ "from CourseAssignment ca Left Join Course cc On cc.course_id=ca.course_id "
				+ "where ca.ac_year_id=?1 And ca.year_sem=?2")
		public List<Map<String, Object>> coursecodeConcateWithName(Integer ac_year_id, Integer year_sem);

		@Query(value = "select cc.course_id as course_id,ca.course_assignment_id as course_assignment_id,"
				+ "Concat(IfNull(ca.course_assignment_coursecode,''),'-',IfNull(cc.course_name,'')) as courseName "
				+ "from CourseAssignment ca Left Join Course cc On cc.course_id=ca.course_id "
				+ "where ca.ac_year_id=?1 And ca.active=true")
		public List<Map<String, Object>> courseConcateWithName(Integer ac_year_id);

		@Query(value = "select cc.course_id as course_id,ca.course_assignment_id as course_assignment_id,"
				+ "cc.course_name As course_name,cc.course_short_name As course_short_name,ca.course_assignment_coursecode As course_assignment_coursecode,"
				+ "Concat(IfNull(ca.course_assignment_coursecode,''),'-',IfNull(cc.course_name,'')) as courseName "
				+ "from CourseAssignment ca Left Join Course cc On cc.course_id=ca.course_id "
				+ "where ca.ac_year_id=?1 And ca.program_assignment_id=?2 And ca.year_sem=?3")
		public List<Map<String, Object>> getCourseAssignmentDetails(Integer ac_year_id,
				Integer program_assignment_id, Integer year_sem);

		
		@Query(value = "select new map(ca.course_assignment_id as id,c.course_name as course_name,ca.user_id As user_id,"
				+ "c.course_short_name as course_short_name,ca.created_date as created_date,ca.modified_date as modified_date,"
				+ "ca.course_category_id as course_category_id,ca.total_credit as total_credit,ca.lecture as lecture,"
				+ "ca.tutorial as tutorial,ca.practical as practical,ca.duration as duration,ca.dept_id as dept_id,"
				+ "ca.cie_marks as cie_marks,ca.see_marks as see_marks,ca.created_by as created_by,ca.modified_by as modified_by,"
				+ "ca.active as active,ca.school_id as school_id,ca.created_username as created_username,ca.year_sem as year_sem,"
				+ "ca.modified_username as modified_username,ct.course_type_name as course_type_name,c.course_code as course_code,"
				+ "cc.course_category_name as course_category_name,d.dept_name_short as dept_name_short,"
				+ "s.school_name_short as school_name_short) from CourseAssignment ca "
				+ "left join CourseType ct on ca.course_type_id=ct.course_type_id left join Course c on ca.course_id=c.course_id "
				+ "left join CourseCategory cc on ca.course_category_id=cc.course_category_id "
				+ "left join Department d on ca.dept_id=d.dept_id "
				+ "left join Schools s on ca.school_id=s.school_id "
				+ "left join Syllabus sy on ca.syllabus_id=sy.syllabus_id Where ca.user_id=?1")
		public List<Map<String, Object>> getCourseAssignmentDetailsBasedOnUserId(Integer user_id);
		
		@Query(value = "select new map(ca.course_assignment_id as course_assignment_id,c.course_id as subject_id,ca.course_assignment_coursecode as course_assignment_coursecode,"
				+ "c.course_name as subject_name,c.course_short_name as subject_name_short,c.course_code as subject_code,ca.year_sem as current_year ) from CourseAssignment ca "
				+ "left join Course c on ca.course_id=c.course_id and c.active=true "
				+ "where ca.program_specialization_id=:program_specialization_id and ca.year_sem=:year_sem and ca.active=true ")
		public List<HashMap<String, Object>> courseDetailBySpecializationAndYearSem(Integer program_specialization_id,Integer year_sem);
		
		
		@Query(value = "select ca.course_assignment_id from CourseAssignment ca where ca.program_assignment_id=?1 and ca.program_id=?2 "
				+ "and ca.program_specialization_id=?3 and ca.year_sem=?4 and ca.course_id=?5 and ca.active=true")
		public Integer getCourseAssignmentIdWithoutAcademicYear(Integer programAssignmentId, Integer programId,
				Integer programSpecializationId, Integer currentSem,Integer courseId);
		
		@Query(value = "Select ca.course_assignment_id from CourseAssignment ca where ca.course_id=?1 and ca.ac_year_id=?2 and ca.active=true")
		public List<Integer> courseAssignmentIdsByCourseAndAcadmicYear(Integer courseId, Integer acYearId);

	@Query(value = "Select Case When cc.course_category_name Like 'Common Course%' Then 'true' ELSE 'false' END from course_assignment ca " +
			"Left Join course_category cc On cc.course_category_id=ca.course_category_id where ca.course_assignment_id=?1 And ca.active=true",nativeQuery = true)
	String commonCourseValidationByCourseAssignmentId(Integer courseAssignmentId);

	@Query(value = "Select ca.course_id from course_assignment ca where ca.course_assignment_id=?1 And ca.active=true",nativeQuery = true)
	Integer courseIdByCourseAssignmentId(Integer courseAssignmentId);
}
