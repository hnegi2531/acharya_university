package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.StockNumberConfiguration;

@Repository
public interface StockNumberConfigurationRepository extends JpaRepository<StockNumberConfiguration, Long> {

	StockNumberConfiguration findByCurrentYear(Integer currentyear);
}
