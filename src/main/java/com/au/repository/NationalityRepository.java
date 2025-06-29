package com.au.repository;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.Nationality;

@Transactional
@Repository
public interface NationalityRepository extends JpaRepository<Nationality,Integer>{
	
	@Query(value = "Select na From Nationality na")
	public List<Nationality> allActiveDetailsList();

	
	@Query(value = "SELECT na.nationality_id as nationality_id,na.nationality as nationality,na.country_name as country_name from nationality na",nativeQuery=true)
	public List<Map<String, Object>> getAllActiveNationality();

}
