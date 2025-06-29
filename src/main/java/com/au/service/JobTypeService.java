package com.au.service;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.JobType;
import com.au.repository.JobTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class JobTypeService {

	@Autowired
	private JobTypeRepository j_repo;

	public List<JobType> listAll() {
		return j_repo.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		// return j_repo.findAll2();
		Page<Object> response1 = j_repo.findAll2(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAll2(Pageable pageable) {
		// return j_repo.findAll3();
		Page<Object> response = j_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public JobType saveJobType(JobType jobType) throws Exception {
		if (getCountJobType(jobType.getJob_type()) >= 1)
			throw new Exception("Job Type Already Exist");
		else if (getCountJobShortName(jobType.getJob_short_name()) >= 1)
			throw new Exception("Short Name Already Exist");
		else {
			j_repo.save(jobType);
		}
		return jobType;

	}

	public JobType get(Integer id) {
		return j_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobType Not Found:" + id));
	}

	public void delete(Integer id) {
		JobType ay = j_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobType Not Found:" + id));
		j_repo.update(id);
	}

	public void delete1(Integer id) {
		JobType ay = j_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("JobType Not Found:" + id));
		j_repo.update1(id);
	}

	public JobType saveJobType1(@Valid JobType jobType) {
		return j_repo.save(jobType);

	}

	public Integer getCountJobType(String jobtype) {
		return j_repo.getCountJobType(jobtype);
	}

	public Integer getCountJobShortName(String job_short_name) {
		return j_repo.getCountJobShortName(job_short_name);
	}

	public HashMap<String, Boolean> jobValidation(String job_type, String job_short_name) {
		HashMap<String, Boolean> valiadtion = new HashMap<>();
		if (job_type != null & job_short_name == null) {
			Boolean b = j_repo.validationForJobType(job_type);
			valiadtion.put("job_type", b);
			return valiadtion;

		} else if (job_short_name != null & job_type == null) {
			Boolean b = j_repo.validationForJobShortName(job_short_name);
			valiadtion.put("job_short_name", b);
			return valiadtion;
		} else {
			Boolean b = j_repo.validationForJobType(job_type);
			Boolean b1 = j_repo.validationForJobShortName(job_short_name);
			valiadtion.put("job_type", b);
			valiadtion.put("job_short_name", b1);
			return valiadtion;
		}
	}
}
