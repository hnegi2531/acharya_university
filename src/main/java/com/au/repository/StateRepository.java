package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.State;

@Repository
public interface StateRepository extends JpaRepository<State,Integer>{

	@Query(value = "SELECT * FROM states where country_id=?1",nativeQuery = true)
	public List<State> getStateByCountryId(Integer countryId);
	
	@Query(value = "SELECT st.name FROM states st where st.id=?1",nativeQuery = true)
	public String getStateName(int id);
}
