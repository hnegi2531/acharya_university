package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.au.exception.ResourceNotFoundException;
import com.au.model.PGApplicable;
import com.au.repository.PGApplicableRepository;

@Service
@Transactional
public class PGApplicableService {

	@Autowired
	private PGApplicableRepository pg_repo;

	public List<PGApplicable> listAll(){
		return pg_repo.findAll();
	}
	
	public PGApplicable save_PGApplicable(PGApplicable academic) {
		return pg_repo.save(academic);
	}
	
	public PGApplicable get(Integer id) {
        return pg_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("PGApplicable Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	PGApplicable ay = pg_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("PGApplicable Not Found:"+id));    	
    	pg_repo.delete(ay);
    }
	
}
