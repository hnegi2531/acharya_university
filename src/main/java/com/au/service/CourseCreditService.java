package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.dto.JwtDetails;
import com.au.dto.ProgramSpecilizationDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseCredit;
import com.au.repository.CourseCreditRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class CourseCreditService {

	@Autowired
	private CourseCreditRepository ccr_repo;

	@Autowired
	private JwtTokenService jwt_service;

	public List<CourseCredit> listAll() {
		return ccr_repo.findAll11();
	}
	
//	public List<HashMap<String, Object>> listAll1() {
//		return ccr_repo.fetchAllDetails();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return ccr_repo.fetchAllDetails();
		Page<Object> response1 = ccr_repo.fetchAllDetails1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return ccr_repo.fetchAllDetails();
		Page<Object> response = ccr_repo.fetchAllDetails2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public CourseCredit saveCourseCredit(@Valid CourseCredit cc) {
		return ccr_repo.save(cc);
	}

	public List<CourseCredit> saveCourseCredits(@Valid ProgramSpecilizationDto psd, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		List<CourseCredit> list = new ArrayList<CourseCredit>();
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		psd.getProgram_type().keySet().stream().forEach(a -> {
			CourseCredit courseCredit = new CourseCredit();
			courseCredit.setProgram_specialization_id(psd.getProgram_specialization_id());
			courseCredit.setProgram_type(a);
			courseCredit.setMin_credit(psd.getProgram_type().get(a).getMin_credit());
			courseCredit.setMax_credit(psd.getProgram_type().get(a).getMax_credit());
			courseCredit.setActive(psd.getActive());
			courseCredit.setCreated_by(jwtDetails.getUserId());
			courseCredit.setCreated_username(jwtDetails.getUserName());
			saveCourseCredit(courseCredit);
			list.add(courseCredit);
		});
		return list;
	}

	public CourseCredit get(Integer id) {
		return ccr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseCredit Id Not Found:" + id));
	}

	public void delete(Integer id) {
		CourseCredit ay = ccr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseCredit Id Not Found:" + id));
		ccr_repo.update(id);
	}

	public void delete1(Integer id) {
		CourseCredit ay = ccr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseCredit Id Not Found:" + id));
		ccr_repo.update1(id);
	}

	
}
