package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.RouteAccountDetails;

@Repository
public interface RouteAccountDetailsRepository extends JpaRepository<RouteAccountDetails, Long> {

	@Query(value=" select r from RouteAccountDetails r where r.instituteId=:schoolId ")
	List<RouteAccountDetails> getAccountDetails(Integer schoolId);
}
