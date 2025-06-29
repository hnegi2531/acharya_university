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
import com.au.model.Student;

@Repository
@Transactional
public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query(value = "select s from Student s where s.active=true")
	public List<Student> findAll1();

	@Modifying
	@Query(value = "update Student s set s.active=false where s.stud_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update Student s set s.active=true where s.stud_id=?1")
	public void update1(Integer id);

//	@Query(value = "SELECT sch.school_name_short,sch.school_name,s.student_id,s.active,s.address,s.auid,s.created_by,s.created_date,"
//			+ "s.created_username,s.doa,s.dob,s.email,s.modified_by,s.modified_date,s.modified_username,s.phone_no,s.school_id,"
//			+ "s.student_f_name,s.student_l_name FROM student s "
//			+ "left join schools sch on s.school_id=sch.school_id", nativeQuery = true)
//	public List<Map<String, Object>> fetchAllStudentDetails();
	
	@Query(value = "select new map(sch.school_name_short as school_name_short,sch.school_name as school_name,s.stud_id as id,"
			+ "s.active as active,s.address as address,s.auid as auid,s.created_by as created_by,s.created_Date as created_Date,"
			+ "s.created_username as created_username,s.doa as doa,s.dob as dob,s.email as email,s.modified_by as modified_by,"
			+ "s.modified_Date as modified_Date,s.modified_username as modified_username,s.phone_no as phone_no,s.school_id as school_id,"
			+ "s.student_f_name as student_f_name,s.student_l_name as student_l_name) "
			+ "FROM Student s left join Schools sch on s.school_id=sch.school_id "
			+ "where CONCAT(IfNull(s.stud_id,''),'',IfNull(sch.school_name,''),'',IfNull(sch.school_name_short,''),'',IfNull(s.auid,''),"
			+ "'',IfNull(s.email,''),'',IfNull(s.school_id,''),'',IfNull(s.student_f_name,''),'',IfNull(s.student_l_name,''),"
			+ "'',IfNull(s.created_by,''),'',IfNull(s.created_Date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword); 
	
	@Query(value = "select new map(sch.school_name_short as school_name_short,sch.school_name as school_name,s.stud_id as id,"
			+ "s.active as active,s.address as address,s.auid as auid,s.created_by as created_by,s.created_Date as created_Date,"
			+ "s.created_username as created_username,s.doa as doa,s.dob as dob,s.email as email,s.modified_by as modified_by,"
			+ "s.modified_Date as modified_Date,s.modified_username as modified_username,s.phone_no as phone_no,s.school_id as school_id,"
			+ "s.student_f_name as student_f_name,s.student_l_name as student_l_name) "
			+ "FROM Student s left join Schools sch on s.school_id=sch.school_id")
	public Page<Object> getAllSortedData(Pageable pageable);

}
