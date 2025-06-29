package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.StdEntranceExam;

@Repository
@Transactional
public interface StdEntranceExamRepository extends JpaRepository<StdEntranceExam,Integer>{

	
	@Query(value = "Select * from std_entrance_exam where student_id=?1",nativeQuery = true)
	List<StdEntranceExam> getEntranceExamByStudentId(Integer studentId);

}
