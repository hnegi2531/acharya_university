package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.au.model.ExamPermit;

public interface ExamPermitRepository  extends JpaRepository<ExamPermit, Integer>{

	Boolean existsByAuidAndAllowSem(String auid, Integer allowSem);

}
