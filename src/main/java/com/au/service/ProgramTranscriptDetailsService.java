package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ProgramTranscriptDetails;
import com.au.model.ProgramTranscriptionRequest;
import com.au.repository.ProgramTranscriptDetailsRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ProgramTranscriptDetailsService {

	@Autowired
	private ProgramTranscriptDetailsRepository p_repo;

	@Autowired
	private JwtTokenService jwt_service;

	public List<ProgramTranscriptDetails> listAll() {
		return p_repo.findAll();
	}

	public ProgramTranscriptDetails saveProgramTranscriptDetails(ProgramTranscriptDetails p) {
		return p_repo.save(p);
	}

	public ProgramTranscriptDetails get(Integer id) {
		return p_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramTranscriptDetails Not Found:" + id));
	}
	
	public List<HashMap<String, Object>> listAll4(Integer program_id) {
		List<HashMap<String, Object>> hs = new ArrayList<>();
		List<HashMap<String, Object>> response = p_repo.fetchingData(program_id);
		hs.addAll(response);
		return hs;
	}

	public void delete(Integer id) {
		ProgramTranscriptDetails ay = p_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramTranscriptDetails Not Found:" + id));
		p_repo.updateProgramTranscript(id);
	}

	/*
	 * public void delete(Integer id) { Department dept = deptrepo.findById(id)
	 * .orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" +
	 * id)); deptrepo.updateDept(id); }
	 */
	public List<ProgramTranscriptDetails> getProgramTrasanscription(ProgramTranscriptionRequest p,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		List<ProgramTranscriptDetails> list = new ArrayList<ProgramTranscriptDetails>();

		p.getProgram_id().stream().forEach(pid -> {

			if (getProgramCountFromTranscriptionDetails(pid, p.getTrans_id()) > 0) {
				throw new RuntimeException("Program Assignment of this combination already exist");
			} else {
			
			ProgramTranscriptDetails p1 = new ProgramTranscriptDetails();
			p1.setTrans_id(p.getTrans_id());
			p1.setActive(p.getActive());
			p1.setProgram_id(pid);
			p1.setCreated_by(jwtDetails.getUserId());
			p1.setForiegn_status(p.getForiegn_status());
			p1.setCreated_username(jwtDetails.getUserName());
			p1.setIs_submitted(p.getIs_submitted());
			p1.setFee_admission_sub_category_id(p.getFee_admission_sub_category_id());
			// saveProgramTranscriptDetails(p1);
			p_repo.save(p1);
			list.add(p1);
			}
		});
		return list;
	

	}
		public Integer getProgramCountFromTranscriptionDetails(Integer pid, Integer trans_id) {
			return p_repo.getProgramCountFromTranscriptionDetails(pid, trans_id);
		}

//	public List<HashMap<String, Object>> getProgramTranscriptDetails() {
//		return p_repo.getProgramTranscriptDetails();
//	}
		
		public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
			Page<Object> roles_filtered_response = p_repo.getAllDataFilteredByKeyword(pageable, keyword);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
		}

		public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
			Page<Object> roles_sorted_response = p_repo.getAllSortedData(pageable);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
		}

	public void delete1(Integer id) {
		ProgramTranscriptDetails p = p_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramTranscriptDetails Not Found:" + id));
		p_repo.updateDept1(id);
	}

	public List<ProgramTranscriptDetails> getProgramTranscription1(List<ProgramTranscriptDetails> p) {
		return p_repo.saveAll(p);
	}
	
//	public List<HashMap<String, Object>> fetchdetails(List<Integer> program_id) {
//		return p_repo.fetchdetails(program_id);
//	}
	
}
