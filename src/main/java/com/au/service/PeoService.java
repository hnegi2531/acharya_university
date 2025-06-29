package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Peo;
import com.au.repository.PeoRepository;

@Service
public class PeoService 
{

	@Autowired
	private PeoRepository a_repo;
	
	public List<Peo> listAll(){
		return a_repo.findAll();
	}
	
	public Peo savePeo(Peo s) {
		return a_repo.save(s);
	}
	
	public Peo get(Integer id) {
        return a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Peo Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	Peo ay = a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Peo Not Found:"+id));    	
    	a_repo.delete(ay);
    }
    
  
}
