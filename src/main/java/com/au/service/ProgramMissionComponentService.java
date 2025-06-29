package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.ProgramMissionComponent;
import com.au.repository.ProgramMissionComponentRepository;

@Service
public class ProgramMissionComponentService {

	@Autowired
	private ProgramMissionComponentRepository a_repo;
	
	public List<ProgramMissionComponent> listAll(){
		return a_repo.findAll();
	}
	
	public ProgramMissionComponent saveProgramMissionComponent(ProgramMissionComponent s) {
		return a_repo.save(s);
	}
	
	public ProgramMissionComponent get(Integer id) {
        return a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ProgramMissionComponent Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	ProgramMissionComponent ay = a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ProgramMissionComponent Not Found:"+id));    	
    	a_repo.delete(ay);
    }
    
}
