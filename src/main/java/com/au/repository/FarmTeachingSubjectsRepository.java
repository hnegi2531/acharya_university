package com.au.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.FarmTeachingSubjects;

@Repository
@Transactional
public interface FarmTeachingSubjectsRepository extends JpaRepository<FarmTeachingSubjects, Integer> {
	
	@Query(value = "select fts from FarmTeachingSubjects fts where fts.active=true")
	public List<FarmTeachingSubjects> findAll1();
	
	@Query(value = "SELECT new map("
	        + "fts.teachingSubjectId AS id, "
	        + "fts.empId AS empId, "
	        + "fts.month AS month, "
	        + "fts.year AS year, "
	        + "fts.courseAssignmentId AS courseAssignmentId, "
	        + "fts.currentYear AS currentYear, "
	        + "fts.currentSem AS currentSem, "
	        + "fts.modules AS modules, "
	        + "fts.useOfIctTool AS useOfIctTool, "
	        + "fts.duration AS duration, "
	        + "fts.totalClasses AS totalClasses, "
	        + "fts.active AS active, "
	        + "fts.courseType AS courseType, "
	        + "CONCAT(IFNULL(c.course_name, ''), '-', IFNULL(ca.course_assignment_coursecode, '')) AS course_with_coursecode, "
	        + "fts.createdBy AS createdBy, "
	        + "fts.modifiedBy AS modifiedBy, "
	        + "fts.createdDate AS createdDate, "
	        + "fts.modifiedDate AS modifiedDate, "
	        + "fts.createdUsername AS createdUsername, "
	        + "fts.modifiedUsername AS modifiedUsername, "
	        + "ed.email AS email, "
	        + "ed.employee_name AS employee_name, "
	        + "ed.empcode AS empcode, "
	        + "des.designation_name AS designation_name, "
	        + "des.designation_short_name AS designation_short_name, "
	        + "dept.dept_name_short AS dept_name_short, "
	        + "dept.dept_name AS dept_name, "
	        + "sch.school_name AS school_name) "
	    + "FROM FarmTeachingSubjects fts "
	    + "LEFT JOIN EmployeeDetails ed ON ed.emp_id = fts.empId "
	    + "LEFT JOIN Designation des ON des.designation_id = ed.designation_id "
	    + "LEFT JOIN Department dept ON dept.dept_id = ed.dept_id "
	    + "LEFT JOIN Schools sch ON sch.school_id = ed.school_id "
	    + "LEFT JOIN CourseAssignment ca ON ca.course_assignment_id = fts.courseAssignmentId "
	    + "LEFT JOIN Course c ON c.course_id = ca.course_id "
		+ "Where CONCAT(IfNull(fts.month,''),'',IfNull(fts.year,''),'',IfNull(fts.createdDate,''),'',IfNull(fts.createdUsername,'')) LIKE %?1%")
	Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);

	@Query(value = "SELECT new map("
	        + "fts.teachingSubjectId AS id, "
	        + "fts.empId AS empId, "
	        + "fts.month AS month, "
	        + "fts.year AS year, "
	        + "fts.courseAssignmentId AS courseAssignmentId, "
	        + "fts.currentYear AS currentYear, "
	        + "fts.currentSem AS currentSem, "
	        + "fts.modules AS modules, "
	        + "fts.useOfIctTool AS useOfIctTool, "
	        + "fts.duration AS duration, "
	        + "fts.totalClasses AS totalClasses, "
	        + "fts.active AS active, "
	        + "fts.courseType AS courseType, "
	        + "CONCAT(IFNULL(c.course_name, ''), '-', IFNULL(ca.course_assignment_coursecode, '')) AS course_with_coursecode, "
	        + "fts.createdBy AS createdBy, "
	        + "fts.modifiedBy AS modifiedBy, "
	        + "fts.createdDate AS createdDate, "
	        + "fts.modifiedDate AS modifiedDate, "
	        + "fts.createdUsername AS createdUsername, "
	        + "fts.modifiedUsername AS modifiedUsername, "
	        + "ed.email AS email, "
	        + "ed.employee_name AS employee_name, "
	        + "ed.empcode AS empcode, "
	        + "des.designation_name AS designation_name, "
	        + "des.designation_short_name AS designation_short_name, "
	        + "dept.dept_name_short AS dept_name_short, "
	        + "dept.dept_name AS dept_name, "
	        + "sch.school_name AS school_name) "
	    + "FROM FarmTeachingSubjects fts "
	    + "LEFT JOIN EmployeeDetails ed ON ed.emp_id = fts.empId "
	    + "LEFT JOIN Designation des ON des.designation_id = ed.designation_id "
	    + "LEFT JOIN Department dept ON dept.dept_id = ed.dept_id "
	    + "LEFT JOIN Schools sch ON sch.school_id = ed.school_id "
	    + "LEFT JOIN CourseAssignment ca ON ca.course_assignment_id = fts.courseAssignmentId "
	    + "LEFT JOIN Course c ON c.course_id = ca.course_id "
	)
	Page<Object> getAllSortedData(Pageable pageable);

	@Modifying
	@Query(value = "update FarmTeachingSubjects fts set fts.active=false where fts.teachingSubjectId=?1")
	public void updateFarmTeachingSubjects(Integer teachingSubjectId);

	@Modifying
	@Query(value = "update FarmTeachingSubjects fts set fts.active=true where fts.teachingSubjectId=?1")
	public void updateFarmTeachingSubjects1(Integer teachingSubjectId);

}
