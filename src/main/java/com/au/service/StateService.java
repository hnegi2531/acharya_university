package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;

import com.au.model.State;
import com.au.repository.StateRepository;

@Service
public class StateService {

	@Autowired
	private StateRepository s_repo;
	
	
	public List<State> listAll(){
		return s_repo.findAll();
	}
	
	public State saveState(State s) {
		return s_repo.save(s);
	}
	
	public State get(Integer id) {
        return s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("State Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	State existingCourse = s_repo.findById(id)
    	        .orElseThrow(()-> new ResourceNotFoundException("State Not Found:"+id));
    	this.s_repo.delete(existingCourse);
    }
	
    
    public List<State> getStateByCountryId(Integer countryId){
    	return s_repo.getStateByCountryId(countryId);
    }
}
