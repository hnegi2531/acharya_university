package com.au.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.au.dto.JwtDetails;
import com.au.dto.SubjectWorkLoadTypeDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.SubjectWorkLoadType;
import com.au.repository.SubjectWorkLoadTypeRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class SubjectWorkLoadTypeService {

	@Autowired
	private SubjectWorkLoadTypeRepository a_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> swt_filtered_response = a_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, swt_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> swt_sorted_response = a_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, swt_sorted_response);
	}
	
	public SubjectWorkLoadType saveSubjectWorkLoadType(SubjectWorkLoadType s) {
		return a_repo.save(s);
	}
	
	public SubjectWorkLoadType get(Integer id) {
        return a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("SubjectWorkLoadType Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	SubjectWorkLoadType ay = a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("SubjectWorkLoadType Not Found:"+id));    	
    	a_repo.delete(ay);
    }
	
	public SubjectWorkLoadType getSubjectWorkLoadTypeBySubjectId(Integer subjectId,Integer subject_work_load_type_id){
		return a_repo.getSubjectWorkLoadTypeBySubjectIdAndworkLoadId(subjectId,subject_work_load_type_id);
	}

	public List<SubjectWorkLoadType> saveSubjectWorkLoadType1(List<SubjectWorkLoadType> dto,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		List<SubjectWorkLoadType> list=dto; 
		 
		list.stream().forEach(l1->{
			//System.out.println(l1.getSubjectId()+"\t"+l1.getSubjectWorkLoadTypeId());
			
			SubjectWorkLoadType s2 = a_repo.getSubjectWorkLoadTypeBySubjectIdAndworkLoadId(l1.getSubjectId(), l1.getSubjectWorkLoadTypeId());
	
			System.out.println("--------------------");
			System.out.println(s2.getSubjectId()+"\t"+s2.getSubjectWorkLoadTypeId());
			
			s2.setAcademicWorkLoadTypeId(l1.getAcademicWorkLoadTypeId());
			s2.setHours(l1.getHours());
			
			s2.setModifiedBy(jwtDetails.getUserId());
			s2.setModifiedUsername(jwtDetails.getUserName());
			s2.setActive(l1.getActive());
			a_repo.save(s2);
		});
		return 	list;
	}

	public List<SubjectWorkLoadType> getSubjectWorkLoadTypeBySubjectId(Integer subjectId) {
		return a_repo.getSubjectWorkLoadTypeBySubjectId(subjectId);
	}
	
   
}
