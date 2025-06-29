package com.au.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import com.au.dto.JwtDetails;
import com.au.dto.SubjectAssignLoadDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.StdSubjectAssignment;
import com.au.model.SubjectWorkLoadType;
import com.au.repository.StdSubjectAssignmentRepository;
import com.au.repository.SubjectWorkLoadTypeRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class StdSubjectAssignmentService {

	@Autowired
	private StdSubjectAssignmentRepository s_repo;
	
	@Autowired
	private SubjectWorkLoadTypeRepository sw_repo;
	
	
	@Autowired
	private JwtTokenService jwt_service;
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> ssa_filtered_response = s_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ssa_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> ssa_sorted_response = s_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, ssa_sorted_response);
	}

	public StdSubjectAssignment saveStdSubjectAssignment(SubjectAssignLoadDto s,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		
		StdSubjectAssignment s1 = new StdSubjectAssignment();
		
		s1.setSubjectId(s.getSubjectId());
		s1.setSchoolId(s.getSchoolId());
		s1.setProgramId(s.getProgramId());
		s1.setProgramSpecializationId(s.getProgramSpecializationId());
		s1.setProgramSpecializationShortName(s.getProgramSpecializationShortName());
		s1.setActive(s.getActive());
		s1.setCreatedBy(jwtDetails.getUserId());
		s1.setCreatedUsername(jwtDetails.getUserName());
		s1.setMaxCieMarks(s.getMaxCieMarks());
		s1.setMaxSemYearMarks(s.getMaxSemYearMarks());
		s1.setMinCieMarks(s.getMinCieMarks());
		s1.setMinSemYearMarks(s.getMinSemYearMarks());
		s1.setSubjectInstituteMaxHours(s.getSubjectInstituteMaxHours());
		s1.setSubjectTypeId(s.getSubjectTypeId());
		s1.setSubjectUniversityMaxHours(s.getSubjectUniversityMaxHours());
		s1.setCredits(s.getCredits());
		
		StdSubjectAssignment s2 =  s_repo.save(s1);
		
		s.getSw_hours().entrySet().stream().forEach(kv->{
			SubjectWorkLoadType sw_type = new SubjectWorkLoadType();
			sw_type.setSubjectId(s2.getSubjectId());			
			
			sw_type.setAcademicWorkLoadTypeId(kv.getKey());
			sw_type.setHours(kv.getValue());
			sw_type.setSubjetAssignId(s1.getSubjetAssignId());
			sw_type.setCreatedBy(jwtDetails.getUserId());
			sw_type.setCreatedUsername(jwtDetails.getUserName());
			sw_type.setActive(true);
			sw_repo.save(sw_type);
		});
		
		
		return s1;
	}

	public StdSubjectAssignment get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("StdSubjectAssignment Not Found:" + id));
	}

	public void delete(Integer id) {
		StdSubjectAssignment cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AcademicWorkLoadType Not Found:" + id));
		s_repo.updateAcademicWorkLoad(id);
	}

	public void delete1(Integer id) {
		StdSubjectAssignment cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AcademicWorkLoadType Not Found:" + id));
		s_repo.updateAcademicWorkLoad1(id);

	}

	public void saveStdSubjectAssignment1(@Valid StdSubjectAssignment r) {
		s_repo.save(r);
	}

	public List<Map<String, Object>> fetchAllDetails() {
		return s_repo.fetchAllDetails();
	}
/*
	public Integer findByprogramSpecializationIdContaining(String id1) {
		return s_repo.countByProgramSpecializationIdLike(id1);
	}
*/
	
	public List<Map<String, Object>> getSubjectAssignIndex(){
		return s_repo.getSubjectAssignIndex();
	}
	
	public List<Map<String,Object>> fetchSubWorkLoadBySubtype(){
		return s_repo.fetchSubWorkLoadBySubtype();
	}


	public List<Map<String,Object>> fetchSubWorkLoadByWorkloadtype(){
		return s_repo.fetchSubWorkLoadByWorkloadtype();
	}
}
