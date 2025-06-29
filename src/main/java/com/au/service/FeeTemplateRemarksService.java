package com.au.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseAssignmentEmployee;
import com.au.model.CourseObjective;
import com.au.model.FeeTemplateRemarks;
import com.au.repository.CourseObjectiveRepository;
import com.au.repository.FeeTemplateRemarksRepository;

@Service
public class FeeTemplateRemarksService {

	
	@Autowired
	private FeeTemplateRemarksRepository feeTemplateRemarksRepository;
	
	
	public List<FeeTemplateRemarks> saveFeeTemplateRemarks(@Valid List<FeeTemplateRemarks> co) throws Exception {
		return	feeTemplateRemarksRepository.saveAll(co);
	}


	public List<FeeTemplateRemarks> listAll1() {
		return feeTemplateRemarksRepository.findAll11();
	}
	
	
	public List<FeeTemplateRemarks> updateFeeTemplateRemarks(List<FeeTemplateRemarks> cos) {
		return feeTemplateRemarksRepository.saveAll(cos);
	}

	public FeeTemplateRemarks get(Integer id) {
		return feeTemplateRemarksRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeTemplateRemarks Not Found:" + id));
	}
	
	public void deactivate(Integer id) {
		feeTemplateRemarksRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeTemplateRemarks Not Found:" + id));
		this.feeTemplateRemarksRepository.deactivate(id);
	}

	public void activate(Integer id) {
		feeTemplateRemarksRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FeeTemplateRemarks Not Found:" + id));
		this.feeTemplateRemarksRepository.activate(id);
	}


	public List<Map<String, Object>> getFeeTemplateRemarksDetails(Integer fee_template) {
		return feeTemplateRemarksRepository.getFeeTemplateRemarksDetails(fee_template);
	}
	
}
