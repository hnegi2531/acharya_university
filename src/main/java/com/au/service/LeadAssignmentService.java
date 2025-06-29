package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.model.LeadAssignment;
import com.au.repository.LeadAssignmentRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.au.exception.ResourceNotFoundException;


@Service
public class LeadAssignmentService {
	
	@Autowired
	private LeadAssignmentRepository lead_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	public LeadAssignment saveLeadAssignment(@Valid LeadAssignment la) {
	
			List<Integer> counselor_list = uar_repo.getOnlyCounselorIds();
			System.out.println("(((((((((((((((((()))))))))))))))))) "+counselor_list);
			
			Map<String, Integer> count_data = new HashMap<String, Integer>();

			counselor_list.stream().forEach(l -> {
				Integer count = lead_repo.getLeadAssignmentCount(l);
				count_data.put(l.toString(), count);
			});
		//						____________________
		//					   |					|
		//for getting smallest value(value of Key-Value pair) from HashMap
			Entry<String, Integer> min = null;
			for (Entry<String, Integer> entry : count_data.entrySet()) {
			    if (min == null || min.getValue() > entry.getValue()) {
			    	
			        min = entry;
			    }
			}			
				la.setUser_id(Integer.parseInt(min.getKey()));
				return lead_repo.save(la);
	}
	
	public List<LeadAssignment> getActiveLeadAssignment(){
		return lead_repo.getActiveLeadAssignment();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = lead_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		
		Page<Object> response = lead_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public LeadAssignment get(Integer id) {
        return lead_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("LeadAssignment Not Found:"+id));
    }
	
	 public ResponseEntity<Object> delete(Integer id) {
		 LeadAssignment ay = lead_repo.findById(id)
	        		.orElseThrow(()-> new ResourceNotFoundException("LeadAssignment Not Found:"+id));    	
	    	lead_repo.update(id);
	    	ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
	    }
	    
	    public ResponseEntity<Object> delete1(Integer id) {
	    	LeadAssignment ay = lead_repo.findById(id)
	        		.orElseThrow(()-> new ResourceNotFoundException("LeadAssignment Not Found:"+id));    	
	    	lead_repo.update1(id);
	    	ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
	    }
	    
	    public List<Map<String, Object>> getLeadsAssignedToUser(Integer user_id) {
	    	
	    	List<Map<String, Object>> data = lead_repo.getLeadsAssignedToUser(user_id);
	        return data;
	    }

		
		public List<LeadAssignment> updateLeadAssignments(@Valid List<LeadAssignment> la) {
			return lead_repo.saveAll(la);
		}


}
