package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{ 
	
	@Query(value = "SELECT con.name FROM Country con where con.id=?1")
	String getCountryName(int id); 

}
