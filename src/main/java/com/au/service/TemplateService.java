package com.au.service;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Template;
import com.au.repository.TemplateRepository;
import com.au.response.ResponseHandler;

@Service
public class TemplateService {
	
	@Autowired
	private TemplateRepository tr_repo;
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		Page<Object> response1 = tr_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		Page<Object> response = tr_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public Template saveTemplate(Template template) {
		return tr_repo.save(template);
	}
	
	public Template get(Integer id) {
        return tr_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Template Id Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	Template s =tr_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("Template Id Not Found:"+id));    	
    	tr_repo.delete(s);
    }

	public List<HashMap<String, Object>> getTemplateDetails(Integer template_type_id) {
		return tr_repo.findTemplateDetails(template_type_id);
	}

}
