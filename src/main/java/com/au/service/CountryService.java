package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Country;
import com.au.repository.CountryRepository;

@Service
public class CountryService {

	@Autowired
	private CountryRepository c_repo;
	
	public List<Country> listAll(){
		return c_repo.findAll();
	}
	
	public Country save_Country(Country a) {
			return c_repo.save(a);
	}
	
	public Country get(Integer id) {
        return c_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Country Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	Country ay = c_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Country Not Found:"+id));    	
    	c_repo.delete(ay);
    }
}
