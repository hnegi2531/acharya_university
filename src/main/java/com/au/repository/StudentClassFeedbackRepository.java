package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.StudentClassFeedback;

@Repository
@Transactional
public interface StudentClassFeedbackRepository extends JpaRepository<StudentClassFeedback, Integer> {
	
	@Query(value = "select count(*) from StudentClassFeedback scf where scf.student_id=?1 and scf.time_table_employee_id=?2 and scf.active=true")
	public Integer getCountOfFeedback(Integer student_id, Integer time_table_id);
	
//	@Query(value = "select new map(scf.dept_id as id,scf.dept_name as dept_name,d.web_status as web_status,d.common_service as common_service,"
//			+ "d.dept_name_short as dept_name_short,d.created_username as created_username,d.modified_username as modified_username,"
//			+ "d.created_date as created_date,d.modified_date as modified_date,d.created_by as created_by,"
//			+ "d.modified_by as modified_by,d.active as active) from StudentClassFeedback scf "
//			+ "where CONCAT(IfNull(scf.created_username,''),'',IfNull(scf.dept_name,''),'',IfNull(scf.dept_name_short,''),'',IfNull(scf.common_service,''),"
//			+ "'',IfNull(d.created_by,''),'',IfNull(d.created_date,'')) LIKE %?1%")
//	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
//	
//	@Query(value = "select new map(d.dept_id as id,d.dept_name as dept_name,d.web_status as web_status,d.common_service as common_service,"
//			+ "d.dept_name_short as dept_name_short,d.created_username as created_username,d.modified_username as modified_username,"
//			+ "d.created_date as created_date,d.modified_date as modified_date,d.created_by as created_by,"
//			+ "scf")
//	public Page<Object> getAllSortedData(Pageable pageable);
	
	@Query(value = "SELECT scf from StudentClassFeedback scf where scf.active=true")
	public List<StudentClassFeedback> findAllFeedback();
	
	@Modifying
	@Query(value = "update StudentClassFeedback scf set scf.active=false where scf.student_class_feedback_id=?1")
	public void updateStudentClassFeedback(Integer student_class_feedback_id);
	
	@Modifying
	@Query(value = "update StudentClassFeedback scf set scf.active=true where scf.student_class_feedback_id=?1")
	public void updateStudentClassFeedback1(Integer student_class_feedback_id);

}
