package com.au.service;

import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ExperienceDetails;
import com.au.repository.ExperienceDetailsRepository;
import com.au.repository.JobProfileRepository;

@Service
public class ExperienceDetailsService {

	@Autowired
	private ExperienceDetailsRepository edr_repo;
	
	@Autowired
	private JobProfileRepository jpr_repo;

	public List<ExperienceDetails> listAll() {
		return edr_repo.findAll();
	}

	public List<ExperienceDetails> saveExperienceDetails(List<ExperienceDetails> experiencedetails) {
		experiencedetails.stream().forEach(exp -> {
			exp.setJob_id(jpr_repo.getLatestJobId());
			System.out.println(exp.getJob_id());
		});
		return edr_repo.saveAll(experiencedetails);
	}

	public ExperienceDetails get(Integer id) {
		if (id.equals(0)) {
			throw new RuntimeException("Opps Exception raised....");
		}
		return edr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ExperienceDetails id Not Found:" + id));
	}

	public void delete(Integer id) {
		ExperienceDetails ay = edr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ExperienceDetails id Not Found:" + id));
		edr_repo.delete(ay);
	}

	public ExperienceDetails saveExperienceDetails1(@Valid ExperienceDetails experiencedetails) {
		return edr_repo.save(experiencedetails);
		
	}

	public List<HashMap<String, Object>> fetchExperienceDetails(Integer job_id) {
		return edr_repo.getExperienceDetails(job_id);
	}
	
	public List<ExperienceDetails> updateExperienceDetails(@Valid List<ExperienceDetails> veh) {
		return edr_repo.saveAll(veh);
	}

	public List<ExperienceDetails> createExperienceDetails(@Valid List<ExperienceDetails> ed) {
		 return	edr_repo.saveAll(ed);
	}
	
}
