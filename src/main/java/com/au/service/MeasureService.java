package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Measure;
import com.au.model.StoresStock;
import com.au.repository.MeasureRepository;
import com.au.response.ResponseHandler;

@Service
public class MeasureService {
	
	@Autowired
	private MeasureRepository m_repo;
	
	
	public Measure createMeasure(Measure ms) throws Exception {
		if(m_repo.getCountMeasureName(ms.getMeasure_name()) >=1) {
			throw new Exception("Measure Name Already Exist");
		}else if(m_repo.getCountMeasureShortName(ms.getMeasure_short_name()) >=1) {
			throw new Exception("Short Name Already Exist");
		}else {
		 m_repo.save(ms);
		}
		return ms;
	}
	
	public List<Measure> getActiveMeasure(){
		return m_repo.findAll1();
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		
		Page<Object> measures_filtered_response = m_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, measures_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> measures_sorted_response = m_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, measures_sorted_response);
	}
	
	public Measure get(Integer measure_id) {
		return  m_repo.findById(measure_id).orElseThrow(() -> new ResourceNotFoundException("Measure Not Found:" + measure_id));
	}
	
	public Measure update(Measure ms) {
		return m_repo.save(ms);
	}
	
	public void delete(Integer measure_id) {
		m_repo.findById(measure_id)
		.orElseThrow(() -> new ResourceNotFoundException("Measure Not Found:" + measure_id));
		m_repo.update(measure_id);
	}
	
	public void delete1(Integer measure_id) {
		m_repo.findById(measure_id)
		.orElseThrow(() -> new ResourceNotFoundException("Measure Not Found:" + measure_id));
		m_repo.update1(measure_id);
	}
}
