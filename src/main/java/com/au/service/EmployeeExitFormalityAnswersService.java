package com.au.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.au.dto.EmployeeExitFormalityAnswersDto;
import com.au.model.EmployeeExitFormalityAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.au.exception.ResourceNotFoundException;
import com.au.repository.EmployeeExitFormalityAnswersRepository;
import com.au.response.ResponseHandler;

@Service
@Transactional
public class EmployeeExitFormalityAnswersService {

	@Autowired
	private EmployeeExitFormalityAnswersRepository eefa_repo;
	
	List<String> l;
	int count=0;
	public List<EmployeeExitFormalityAnswers> getFormalityAnswers(@Valid EmployeeExitFormalityAnswersDto eefa) throws Exception {
		
			String[] num = eefa.getAnswers().split(",");
			l = Arrays.asList(num);
			System.out.println("((((((((((((((((((())))))))))))))))))) "+l);
		
		
		List<EmployeeExitFormalityAnswers> list = new ArrayList<>();
		
		eefa.getEefqid().stream().forEach(e -> {
			
				EmployeeExitFormalityAnswers eefaa= new EmployeeExitFormalityAnswers();
				eefaa.setActive(eefa.getActive());
				eefaa.setCreated_by(eefa.getCreated_by());
				eefaa.setCreated_date(eefa.getCreated_date());
				eefaa.setCreated_username(eefa.getCreated_username());
				eefaa.setEefaid(eefa.getEefaid());
				eefaa.setEmp_id(eefa.getEmp_id());
				eefaa.setAnswers(num[count]);
				eefaa.setModified_by(eefa.getModified_by());
				eefaa.setModified_date(eefa.getModified_date());
				eefaa.setModified_username(eefa.getModified_username());
				eefaa.setEefqid(e);
			
			list.add(eefaa);
			count++;
		});
	
		count=0;
		return eefa_repo.saveAll(list);
		
		   }
	
		
	public List<EmployeeExitFormalityAnswers> listAll(){
		return eefa_repo.findAll11();
	}
	
   public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = eefa_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	  }
	
   
   public ResponseEntity<Object> listAll2(Pageable pageable){
	 Page<Object> response = eefa_repo.findAll2(pageable);
	    return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	    }
	
   
   public EmployeeExitFormalityAnswers get(Integer id) {
	    return eefa_repo.findById(id)
	    		.orElseThrow(()-> new ResourceNotFoundException("Employee Exit Formality Answers Not Found:"+id));
		}
  
   public EmployeeExitFormalityAnswers save_answers(EmployeeExitFormalityAnswers eefas) {
		
	   return eefa_repo.save(eefas);
	}
   

	public ResponseEntity<Object> delete(Integer id) {
		EmployeeExitFormalityAnswers ay = eefa_repo.findById(id)
	    		.orElseThrow(()-> new ResourceNotFoundException("Employee Exit Formality Answers Not Found:"+id));    	
		eefa_repo.update(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	public ResponseEntity<Object> delete1(Integer id) {
		EmployeeExitFormalityAnswers ay = eefa_repo.findById(id)
	    		.orElseThrow(()-> new ResourceNotFoundException("Employee Exit Formality Answers Not Found:"+id));    	
		eefa_repo.update1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}	
   
	public List<HashMap<String, Object>> listAllAnswers(Integer emp_id){
		return eefa_repo.listAllAnswers1(emp_id);
	}
	
	
}
