package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.PGApplicable;

@Repository
public interface PGApplicableRepository extends JpaRepository<PGApplicable, Integer>{

	
	@Query(value = "SELECT * FROM pg_applicable where pg_id=?1",nativeQuery = true)
	public PGApplicable findByPgId(Integer pg_id);
	
	
}
