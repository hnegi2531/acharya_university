package com.au.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.AcademicProgramNotFoundException;
import com.au.exception.ResourceNotFoundException;
import com.au.model.AcademicProgram;
import com.au.repository.AcademicProgramRepository;
import com.au.response.ResponseHandler;

@Service
public class AcademicProgramService {

	private AcademicProgramRepository ap_repo;

	public AcademicProgramService(AcademicProgramRepository ap_repo) {
		this.ap_repo = ap_repo;
	}

	public List<AcademicProgram> listAll() {
		return ap_repo.findAll1();
	}

	public AcademicProgram save_AcademicProgram(AcademicProgram a) {

		if (getProgram(a.getAc_year_id(), a.getProgram_id()) >= 1) {
			throw new AcademicProgramNotFoundException("AcademicProgram already exist");
		} else {
			return ap_repo.save(a);
		}
	}

	public AcademicProgram get(Integer id) {
		return ap_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("AcademicProgram Not Found:" + id));
	}

	public ResponseEntity<Object> delete(Integer id) {
		AcademicProgram ay = ap_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AcademicProgram Not Found:" + id));
		ap_repo.update(id);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}

	public ResponseEntity<Object> delete1(Integer id) {
		AcademicProgram ay = ap_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AcademicProgram Not Found:" + id));
		ap_repo.update1(id);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;

	}

	/*
	 * public List<AcademicProgram>
	 * getNumOfSemAndYearByProgram_IdAndAcYear_Id(Integer ac_year_id, Integer
	 * program_id) { return
	 * ap_repo.getNumOfSemAndYearByProgram_IdAndAcYear_Id(ac_year_id, program_id); }
	 */
	public Integer getProgram(Integer ac_year_id, Integer program_id) {
		return ap_repo.getProgram(ac_year_id, program_id);
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = ap_repo.findAll11(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);

	}

	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = ap_repo.findAll12(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);

	}

	public AcademicProgram saveAcademicProgram(AcademicProgram bs) {
		return ap_repo.save(bs);

	}

}
