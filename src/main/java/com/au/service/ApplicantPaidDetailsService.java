package com.au.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.ApplicantPaidDetails;
import com.au.repository.ApplicantPaidDetailsRepository;

@Service
public class ApplicantPaidDetailsService {
	
	@Autowired
	private ApplicantPaidDetailsRepository applicantPaidDetailsRepository;

	
	public ApplicantPaidDetails saveApplicantPaidDetails(@Valid ApplicantPaidDetails dp)throws Exception {
		return	applicantPaidDetailsRepository.save(dp);
	}
	
	
	public List<ApplicantPaidDetails> getAllActiveApplicantPaidDetails() {
		 return applicantPaidDetailsRepository.getAllActiveApplicantPaidDetails();
	}
	
	public ApplicantPaidDetails get(Integer id) {
		return applicantPaidDetailsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Daily planner Not Found:" + id));
	}
	
	
	public ApplicantPaidDetails updateApplicantPaidDetails(@Valid ApplicantPaidDetails tm) {
		return applicantPaidDetailsRepository.save(tm);
	}


	public List<Map<String, Object>> getApplicantPaidDetailsBasedOnId(String lead_id, String opportunity_id) {
		return applicantPaidDetailsRepository.getApplicantPaidDetailsBasedOnId(lead_id,opportunity_id);
	}
}
