package com.au.dto;

import org.springframework.data.jpa.repository.JpaRepository;

import com.au.model.City;

public interface CityDao extends JpaRepository<City	, Integer>{

	
}
