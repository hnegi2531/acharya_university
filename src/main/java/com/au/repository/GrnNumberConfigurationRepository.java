package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.GrnNumberConfiguration;

@Repository
public interface GrnNumberConfigurationRepository extends JpaRepository<GrnNumberConfiguration, Long> {
	GrnNumberConfiguration findByCurrentYear(Integer currentyear);
}
