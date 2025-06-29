package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ProgramType;
import com.au.repository.ProgramTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class ProgramTypeService {

	@Autowired
	private ProgramTypeRepository pr_repo;

	public List<ProgramType> listAll() {
		return pr_repo.findAll1();
	}

	public ProgramType saveProgramType(ProgramType programType) throws Exception {
		if(pr_repo.countOfProgramTypeName(programType.getProgram_type_name())>=1) {
			throw new Exception("ProgramType Name Already Exist");
		}else if(pr_repo.countOfProgramShortName(programType.getProgram_type_code())>=1) {
			throw new Exception("Short Name Already Exist");
		}else {
			return pr_repo.save(programType);
		}
	}

	public ProgramType get(Integer id) {
		return pr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProgramType Not Found:" + id));
	}

	public void delete(Integer id) {
		pr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ProgramType Not Found:" + id));
		pr_repo.update(id);
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return pr_repo.findAll();
		Page<Object> response1 = pr_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return pr_repo.findAll();
		Page<Object> response = pr_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public void delete1(Integer id) {
		pr_repo.update1(id);

	}

	public ProgramType save_ProgramType1(ProgramType p) {
		return pr_repo.save(p);

	}
}
