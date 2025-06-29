package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Program;
import com.au.repository.ProgramRepository;
import com.au.response.ResponseHandler;

@Service
public class ProgramService {
	
	@Autowired
	private ProgramRepository pro_repo;
	
//	public List<Program> listAll(){
//		return pro_repo.findAll();
//	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = pro_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = pro_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}
	
	public List<Program> listAll1(){
		return pro_repo.findAll11();
	}
	
	
/*	
	public ResponseEntity<Program> save_Program(Program p) {
		if(getProgramByPnameSchool(p.getProgram_name(), p.getSchool_id())>=1)
		{
			throw new ProgramNotFoundException("Program already exist");
		}
		else {		
		//	return pro_repo.save(p);
			return new ResponseEntity<Program>(pro_repo.save(p),HttpStatus.OK);
		}
	}
	*/
	
	public Program save_Program(Program p) throws Exception {
		if(getProgramCount(p.getProgram_name())>=1)
			throw new Exception("Program Already Exist");
		else if (getProgramShortCount(p.getProgram_short_name()) >= 1) {
			throw new Exception("Short Name Already Exist");
		} else {
			pro_repo.save(p);
		}
		return p;
		
	}
	
	
	public Program get(Integer id) {
        return pro_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ProgramType Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	Program p=pro_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("ProgramType Not Found:"+id));    	
    	pro_repo.update(id);
    }
    
    public void delete1(Integer id) {
    	Program p=pro_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("ProgramType Not Found:"+id));    	
    	pro_repo.update1(id);
    }
/*
	public List<Program> findById(Integer id) {
		System.out.println("hello");
		return pro_repo.findBySchoolId(id);
	}

	public Integer countRecords(Integer id) {
		// TODO Auto-generated method stub
		return pro_repo.countRecords(id);
	}
	
	public Integer getProgramByPnameSchool(String program_name,Integer school_id) {
		return pro_repo.getProgramByPnameSchool(program_name, school_id);
	}
	
	
	 public List<Program> getProgramBySchool(Integer school_id){
		 return pro_repo.getProgramBySchool(school_id);
	 }
*/
    public Program save_ProgramType1(@Valid Program p) {
          return pro_repo.save(p);		
	}
    
    public Integer getProgramCount(String program_name) {
    	return pro_repo.getProgramCount(program_name);
    }
    
    public Integer getProgramShortCount(String program_short_name) {
    	return pro_repo.getProgramShortCount(program_short_name);
    }

	public List<Map<String, Object>> getAllActiveProgramDetails(Integer emp_id) {
		return pro_repo.getAllActiveProgramDetails(emp_id);
	}


	public List<Map<String, Object>> getAllActiveProgramDetails1(Integer emp_id) {
		return pro_repo.getAllActiveProgramDetails1(emp_id);
	}

	public ResponseEntity<Object> fetchAllProgramWithProgramName() {

		List<String> allPrograms = pro_repo.getConcatenatedProgramNamesWithFallback();
		if (allPrograms != null)
			return ResponseHandler.generateResponse(true, HttpStatus.OK, allPrograms);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, null);
	}
}