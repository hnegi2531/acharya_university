package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Graduation;
import com.au.repository.Graduation_Repository;
import com.au.response.ResponseHandler;

@Service
public class Graduation_Service {

	@Autowired
	private Graduation_Repository gr;

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> filtered_response = gr.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, filtered_response);
		}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
	
		Page<Object> response = gr.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<Graduation> listAllActivedata() {
		return gr.findAll1();
	}

	public Graduation save_Graduation(Graduation g) throws Exception {
		if(gr.countOfGraduationName(g.getGraduation_name())>=1) {
			throw new Exception("Graduation Name Already Exist");
		}else if(gr.countOfGraduationNameShort(g.getGraduation_name_short())>=1) {
			throw new Exception("Short Name Already Exist");
		}else {
			return	gr.save(g);
		}
	}

	public Graduation get(Integer id) {
		return gr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Graduation Not Found:" + id));
	}

	public void delete(Integer id) {
		Graduation ay = gr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Graduation  Not Found:" + id));
		gr.update(id);
	}

	public void delete1(Integer id) {
		Graduation ay = gr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Graduation  Not Found:" + id));
		gr.update1(id);
	}

	public Graduation save_Graduation1(Graduation g) {
		return gr.save(g);

	}

}
