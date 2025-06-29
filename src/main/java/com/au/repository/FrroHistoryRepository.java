package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.au.model.FrroHistory;

public interface FrroHistoryRepository extends JpaRepository<FrroHistory, Integer> {

	@Query(value="select fr from FrroHistory fr where fr.studentId=:studentId ")
	List<FrroHistory> getFrroHistory(Integer studentId);

}
