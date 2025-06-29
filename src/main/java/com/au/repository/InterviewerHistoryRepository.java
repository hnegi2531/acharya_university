package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.InterviewHistory;
import com.au.model.InterviewerHistory;

@Transactional
@Repository
public interface InterviewerHistoryRepository extends JpaRepository<InterviewerHistory,Integer>{

	
}
