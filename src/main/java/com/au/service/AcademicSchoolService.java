package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.AcademicSchoolRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.academic_school;
import com.au.repository.AcademicSchoolRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class AcademicSchoolService {

	@Autowired
	private AcademicSchoolRepository as_repo;

	public List<academic_school> listAll(){
		return as_repo.findAll11();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = as_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		
		Page<Object> response = as_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public academic_school save_academic_school(academic_school a) {
			return as_repo.save(a);
	}
	
	public List<Integer> findByAcYearSchoolId(Integer ac_year_id){
		return as_repo.findByAcYearSchoolId(ac_year_id);
	}
	
	public List<academic_school> getAcaSchool(@Valid AcademicSchoolRequest bs) {
		
		List<academic_school> list = new ArrayList<>();
		
		bs.getSchool_id().stream().forEach(t->{
			List<Integer> list1 = findByAcYearSchoolId(bs.getAc_year_id());
			System.out.println(list1);
			if(list1.contains(t)) {
				System.out.println("academic year is already exist with this school");
			}
			else {
			academic_school a1 = new academic_school();
			a1.setSchool_id(t);
			a1.setAc_year_id(bs.getAc_year_id());
			a1.setTo_date(bs.getTo_date());
			a1.setFrom_date(bs.getFrom_date());
			a1.setCreated_date(bs.getCreated_date());
			a1.setModified_date(bs.getModified_date());
			a1.setCreated_by(bs.getCreated_by());
			a1.setModified_by(bs.getModified_by());
			a1.setCreated_username(bs.getCreated_username());
			a1.setModified_username(bs.getModified_username());
			a1.setActive(bs.isActive());		
			save_academic_school(a1);
			list.add(a1);
			}
		});
		
				return list;
	}
	
	
	public academic_school get(Integer id) {
        return as_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("academic_school Not Found:"+id));
    }
     
    public ResponseEntity<Object> delete(Integer id) {
    	academic_school ay = as_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("academic_school Not Found:"+id));    	
    	as_repo.update(id);
    	ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
    }
    
    public ResponseEntity<Object> delete1(Integer id) {
    	academic_school ay = as_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("academic_school Not Found:"+id));    	
    	as_repo.update1(id);
    	ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
    }

	
    
}
