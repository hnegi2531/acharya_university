package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Interview;
import com.au.model.InterviewHistory;

@Transactional
@Repository
public interface InterviewHistoryRepository extends JpaRepository<InterviewHistory,Integer>{


}
