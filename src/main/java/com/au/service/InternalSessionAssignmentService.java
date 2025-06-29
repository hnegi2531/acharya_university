package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.InternalSessionAssignment;
import com.au.repository.InternalSessionAssignmentRepository;
import com.au.response.ResponseHandler;

@Service
public class InternalSessionAssignmentService {

	@Autowired
	private InternalSessionAssignmentRepository isa_repo;
	
	
	
    public InternalSessionAssignment saveInternalSessionAssignment(InternalSessionAssignment isas) throws Exception {
		
		if(isa_repo.getInternalSessionAssignments(isas.getProgram_specialization_id(),isas.getAc_year_id(),isas.getSchool_id()) >= 1) {
			throw new Exception("Combination Of Program Specialization Id, Ac_year Id And School Id Already Present");
	
//		} else if(isa_repo.getCountOfAcademicYear(isas.getAc_year_id()) >= 1) {
//			throw new Exception("Ac_year id Already Exist");
//		
//		} else if(isa_repo.getCountOfIsaSchool(isas.getSchool_id()) >= 1) {
//			throw new Exception("School Id Already Exist");
//		
//		} else if(isa_repo.getCountOfIsaYearSem(isas.getSchool_id()) >= 1) {
//			throw new Exception("Year_Sem Already Exist");
	//	}
//		 else if(isa_repo.getCountOfFrom_Date(isas.getFrom_date()) >= 1) {
//			throw new Exception("Internal Starting Date Already Exist");
//	
//		} else if(isa_repo.getCountOfTo_Date(isas.getFrom_date()) >= 1) {
//			throw new Exception("Internal Ending Date Already Exist");
//		
		}
		else {
			InternalSessionAssignment isa=isa_repo.save(isas);
		
			return isa;
		}
	}
//    
//    
//    public List<InternalSessionAssignment> listAllActiveInternalSessionAssignment() {
//		List<InternalSessionAssignment> isas=isa_repo.findAll();
//		return isas;
//	}
//    
//    public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
//    	List<Map<String,Object>> internalSessionAssignment_filtered_response = isa_repo.getAllDataFilteredByKeyword(pageable, keyword);
//		return ResponseHandler.generateResponse(true, HttpStatus.OK, internalSessionAssignment_filtered_response);
//	}
//	
//	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
//
//		List<Map<String,Object>> internalSessionAssignment_sorted = isa_repo.getAllSortedData(pageable);
//		return ResponseHandler.generateResponse(true, HttpStatus.OK, internalSessionAssignment_sorted);
//	}
//    
//	public InternalSessionAssignment get(Integer id) {
//		return isa_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Internal Session Assignment Not Found:" + id));
//	}
//	
	public InternalSessionAssignment saveInternalSessionAssignments(InternalSessionAssignment isas) {
		return isa_repo.save(isas);
	}
//
//	
//	public void delete(Integer id) {
//		InternalSessionAssignment isa = isa_repo.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
//		isa_repo.updateDept(id);
//	}
//
//	public void delete1(Integer id) {
//		InternalSessionAssignment isa = isa_repo.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Department Not Found:" + id));
//		isa_repo.updateDept1(id);
//	}
//
//	public List<Map<String, Object>> internal_idbasedOnSessionAssignment1(Integer school_id,Integer program_id,Integer program_specialization_id,Integer ac_year_id,Integer year_sem,Integer course_id) {
//		return isa_repo.internal_idbasedOnSessionAssignment11(school_id,program_id,program_specialization_id,ac_year_id,year_sem,course_id);
//	}
//	
}
