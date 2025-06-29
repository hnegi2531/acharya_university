package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.City;
import com.au.repository.CityRepository;

@Service
public class CityService {

	@Autowired
	private CityRepository c_repo;
	
	
	public List<City> listAll() {
		return c_repo.findAll();
	}

	public City saveBoard(City c) {
		return c_repo.save(c);
	}

	public City get(Integer id) {
		if (id.equals(5)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return c_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("City  Not Found:" + id));
	}

	public void delete(Integer id) {
		City ay = c_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("City Not Found:" + id));
		c_repo.delete(ay);
	}
	
	public List<City> getCityByStateAndCountry(Integer state_id,Integer country_id){
		return c_repo.getCityByStateAndCountry(state_id, country_id);
	}
}
