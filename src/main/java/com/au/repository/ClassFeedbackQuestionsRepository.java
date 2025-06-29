package com.au.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.ClassFeedbackQuestions;


@Transactional
@Repository
public interface ClassFeedbackQuestionsRepository extends JpaRepository<ClassFeedbackQuestions, Integer> {

	
	@Query(value = "SELECT cfq from ClassFeedbackQuestions cfq where cfq.active=true")
	public List<ClassFeedbackQuestions> findAll11();
	
	@Query(value = "select new map(cfq.class_feedback_questions_id as id,cfq.question as question,cfq.school_id as school_id,"
			+ "cfq.created_username as created_username,cfq.modified_username as modified_username,cfq.created_date as created_date,"
			+ "cfq.modified_date as modified_date,cfq.created_by as created_by,cfq.modified_by as modified_by,"
			+ "cfq.active as active) from ClassFeedbackQuestions cfq "
			+ "where CONCAT(IfNull(cfq.created_username,''),'',IfNull(cfq.question,''),'',IfNull(cfq.school_id,''),'',"
			+ "IfNull(cfq.created_by,''),'',IfNull(cfq.created_date,'')) LIKE %?1%")
	public Page<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword);
	
	

	@Query(value = "select new map(cfq.class_feedback_questions_id as id,cfq.question as question,cfq.school_id as school_id,"
			+ "cfq.created_username as created_username,cfq.modified_username as modified_username,cfq.created_date as created_date,"
			+ "cfq.modified_date as modified_date,cfq.created_by as created_by,cfq.modified_by as modified_by,"
			+ "cfq.active as active) from ClassFeedbackQuestions cfq")
	public Page<Object> getAllSortedData(Pageable pageable);
	
	

	@Modifying
	@Query(value = "update ClassFeedbackQuestions cfq set cfq.active=false where cfq.class_feedback_questions_id=?1")
	public void delete1(Integer id);
	
	@Modifying
	@Query(value = "update ClassFeedbackQuestions cfq set cfq.active=true where cfq.class_feedback_questions_id=?1")
	public void delete2(Integer id);
	
	@Query(value = "SELECT cfq from ClassFeedbackQuestions cfq where cfq.school_id=?1 and cfq.active=true")
	public List<ClassFeedbackQuestions> getClassFeedbackQuestions(Integer school_id);
	
}
