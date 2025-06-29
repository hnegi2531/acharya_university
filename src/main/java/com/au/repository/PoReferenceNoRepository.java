package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.POReferenceNoConfig;

@Repository
public interface PoReferenceNoRepository extends JpaRepository<POReferenceNoConfig, Integer> {
	POReferenceNoConfig findByCurrentYear(Integer currentyear);
}
