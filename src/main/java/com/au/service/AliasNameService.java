package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.au.exception.ResourceNotFoundException;
import com.au.model.AliasName;
import com.au.repository.AliasNameRepository;
import com.au.repository.VoucherHeadRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class AliasNameService {

	@Autowired
	private AliasNameRepository a_repo;
	
	
	
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		Page<Object> response1 = a_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		//return a_repo.findAll();
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		Page<Object> response = a_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		//return a_repo.findAll();
	}
	
	public AliasName save_AliasName(AliasName s) {
		return a_repo.save(s);
	}
	
	public AliasName get(Integer id) {
        return a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("AliasName Not Found:"+id));
    }
     
    public ResponseEntity<Object> delete(Integer id) {
    	AliasName ay = a_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("AliasName Not Found:"+id));    	
    	a_repo.delete(ay);
    	ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
    }
    
    public List<String> getAliasNames(){
    	
    	return a_repo.getAliasNames();
    }
    
}
