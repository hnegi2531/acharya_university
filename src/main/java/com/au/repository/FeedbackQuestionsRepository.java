package com.au.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.au.model.FeedbackQuestions;



@Repository
public interface FeedbackQuestionsRepository extends JpaRepository<FeedbackQuestions, Integer> {

	
	
	@Query(value = "SELECT count(*) FROM FeedbackQuestions fq where fq.school_id=?1 and fq.feedback_questions=?2 and fq.active=true")
	public Integer countOfcombination(Integer school_id,String feedback_questions);
	
	
	@Query(value = "select fq from FeedbackQuestions fq where fq.active=true")
	public List<FeedbackQuestions> findAll11();
	
	@Query(value = "Select new map(fq.feedback_id as id,fq.school_id as school_id,sc.school_name as school_name,"
			+ "fq.created_date as created_date,sc.school_name_short as school_name_short,fq.feedback_questions as feedback_questions,"
			+ "fq.modified_date as modified_date,fq.created_by as created_by,fq.modified_by as modified_by,fq.active as active,"
			+ "fq.created_username as created_username,fq.modified_username as modified_username)"
			+ " from FeedbackQuestions fq "
			+ "left join Schools sc on fq.school_id=sc.school_id "
			+ "Where CONCAT(IfNull(school_id,''),'',IfNull(fq.created_by,''),'',IfNull(fq.created_date,''),'',IfNull(fq.created_username,'')) LIKE %?1% ")
	public Page<Object> findAll1(Pageable pageable, Object keyword);
	
	@Query(value = "Select new map(fq.feedback_id as id,fq.school_id as school_id,sc.school_name as school_name,"
			+ "fq.created_date as created_date,sc.school_name_short as school_name_short,fq.feedback_questions as feedback_questions,"
			+ "fq.modified_date as modified_date,fq.created_by as created_by,fq.modified_by as modified_by,fq.active as active,"
			+ "fq.created_username as created_username,fq.modified_username as modified_username)"
			+ " from FeedbackQuestions fq "
			+ "left join Schools sc on fq.school_id=sc.school_id ")
	public Page<Object> findAll2(Pageable pageable);
	
	@Modifying
	@Query(value = "update FeedbackQuestions fq set fq.active=false where fq.feedback_id=?1")
	public void update(Integer id);

	@Modifying
	@Query(value = "update FeedbackQuestions fq set fq.active=true where fq.feedback_id=?1")
	public void update1(Integer id);
	

	@Query(value = "select count(*) from FeedbackQuestions fq where fq.school_id = ?1 and fq.feedback_questions = ?2 and fq.active = true")
	public Integer checkFeedbackQuestionsForUpdate(Integer school_id,String feedback_questions);
	
}
