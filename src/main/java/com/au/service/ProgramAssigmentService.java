package com.au.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.ProgramAssigment;
import com.au.repository.ProgramAssigmentRepository;
import com.au.repository.ProgramTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class ProgramAssigmentService {

	@Autowired
	private ProgramAssigmentRepository r_repo;
	
	@Autowired
	private ProgramTypeRepository pr_repo;
	
	
	public List<ProgramAssigment> listAll(){
		return r_repo.findAll1();
	}
	
	public ProgramAssigment saveProgramAssigment(ProgramAssigment r) {
		if(getProgramAssignmentCount(r.getProgram_id(),r.getSchool_id(),r.getProgram_type_id())>=1) {
			throw new RuntimeException("Combination Of School, Program and Program Type is already exist");
		}
		r.setProgram_type(pr_repo.fetchProgramType(r.getProgram_type_id()));
		return r_repo.save(r);
	}
	
	public ProgramAssigment get(Integer id) {
		return r_repo.findById(id)
		    	.orElseThrow(()-> new ResourceNotFoundException("ProgramAssigment Not Found:"+id));    	
		  //  	return r_repo.findDetailsById(id);

    }
     
    public void delete(Integer id) {
    	ProgramAssigment cc = r_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("ProgramAssigment Not Found:"+id));    	
    	r_repo.update(id);
    }
    
    public void delete1(Integer id) {
    	ProgramAssigment cc = r_repo.findById(id)
    	.orElseThrow(()-> new ResourceNotFoundException("ProgramAssigment Not Found:"+id));    	
    	r_repo.update1(id);
    }

//	public List<HashMap<String, Object>> listAll1() {
//		return r_repo.fetchAllDetail();
//	}
    
    public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = r_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = r_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}

	public List<Map<String, Object>> getProgramBySchool(Integer ac_year_id,Integer school_id) {
		return r_repo.fetchProgramDetail(ac_year_id,school_id);
	}
	
	public List<ProgramAssigment> getNumOfSemAndYearByProgram_IdAndAcYear_Id(Integer ac_year_id, Integer program_id,Integer school_id) {
		return r_repo.getNumOfSemAndYearByProgram_IdAndAcYear_Id(ac_year_id, program_id,school_id);
	}
	public Integer getProgramAssignmentCount(Integer program_id,Integer school_id,Integer program_type_id) {
		return r_repo.getProgramAssignmentCount(program_id,school_id,program_type_id);
	}

	public ProgramAssigment save_ProgramAssigments(@Valid ProgramAssigment r) {
		return r_repo.save(r);
		
	}
	
	public List<HashMap<String, Object>> fetchAllProgramsWithSpecialization(Integer school_id) {
		return r_repo.fetchAllProgramsWithSpecialization(school_id);
	}
	
	public List<HashMap<String, Object>> fetchAllProgramsWithSpecializationBasedOnSchoolAndAcYear(Integer school_id, Integer ac_year_id) {
		return r_repo.fetchAllProgramsWithSpecializationBasedOnSchoolAndAcYear(school_id,ac_year_id);
	}
	
	
	public List<Map<String, Object>> fetchAllProgramsWithSpecializationBasedOnAcYear() {
		return r_repo.fetchAllProgramsWithSpecializationBasedOnAcYear();
	}


	public List<HashMap<String, Object>> fetchAllProgramsWithProgramType(Integer school_id) {
		List<HashMap<String, Object>> program_with_program_type = r_repo.fetchAllProgramsWithProgramType(school_id);
		Map<String, String> concatenated_program_assignment_of_same_program =program_with_program_type.stream().collect(Collectors.toMap(e -> e.get("program_id").toString(),
				e -> e.get("program_assignment_id").toString(),(a,b) -> a+","+b));
		List<Integer> program_assignment_id_of_repeated_program=new ArrayList<>();
		
		concatenated_program_assignment_of_same_program.entrySet().forEach(r ->{
			if(r.getValue().split(",").length > 1) {
				List<Integer> p=ResponseHandler.toConvertCommaSeperatedIdsAsList(r.getValue());
				program_assignment_id_of_repeated_program.addAll(p);
			}
		});
		program_assignment_id_of_repeated_program.stream().forEach(q ->{
			program_with_program_type.stream().forEach(s ->{
				if(s.get("program_assignment_id") == q) {
					s.put("program_name",s.get("program_name") +"-" +s.get("program_type_code"));
				}
			});
			
		});
		return program_with_program_type;
	}
	
	public List<HashMap<String, Object>> fetchAllProgramsAndSpecializationWithProgramType() {
		List<HashMap<String, Object>> program_with_program_type = r_repo.fetchAllProgramsAndSpecializationWithProgramType();
		Map<String, String> concatenated_program_assignment_of_same_program =program_with_program_type.stream().collect(Collectors.toMap(e -> e.get("program_id").toString(),
				e -> e.get("program_assignment_id").toString(),(a,b) -> a+","+b));
		
		List<Integer> program_assignment_id_of_repeated_program=new ArrayList<>();
		
		concatenated_program_assignment_of_same_program.entrySet().forEach(r ->{
			if(r.getValue().split(",").length > 1) {
				List<Integer> p=ResponseHandler.toConvertCommaSeperatedIdsAsList(r.getValue());
				p.stream().forEach(asignment_id ->{
					if( Collections.frequency(p, asignment_id) ==1) {
						program_assignment_id_of_repeated_program.add(asignment_id);
					}
				});

			}
		});
		
		program_assignment_id_of_repeated_program.stream().forEach(q ->{
			program_with_program_type.stream().forEach(s ->{
				if(s.get("program_assignment_id") == q) {
					s.put("program_name",s.get("program_name")+"-" +s.get("program_type_code"));
				}
			});

		});
		return program_with_program_type;
	}
	
	public List<HashMap<String, Object>> programsDetailsWithProgramType() {
		List<HashMap<String, Object>> program_with_program_type = r_repo.programsDetailsWithProgramType();
		Map<String, String> concatenated_program_assignment_of_same_program =program_with_program_type.stream().collect(Collectors.toMap(e -> e.get("program_id").toString(),
				e -> e.get("program_assignment_id").toString(),(a,b) -> a+","+b));
		List<Integer> program_assignment_id_of_repeated_program=new ArrayList<>();
		
		concatenated_program_assignment_of_same_program.entrySet().forEach(r ->{
			if(r.getValue().split(",").length > 1) {
				List<Integer> p=ResponseHandler.toConvertCommaSeperatedIdsAsList(r.getValue());
				program_assignment_id_of_repeated_program.addAll(p);
			}
		});
		program_assignment_id_of_repeated_program.stream().forEach(q ->{
			program_with_program_type.stream().forEach(s ->{
				if(s.get("program_assignment_id") == q) {
					s.put("program_name",s.get("program_name") +"-" +s.get("program_type_code"));
				}
			});
			
		});
		return program_with_program_type;
	}

	
	public ProgramAssigment findAll11(Integer program_assignment_id) {
		return r_repo.findAll11(program_assignment_id);

    }

	public List<HashMap<String, Object>> fetchAllProgramsAndSpecializationWithProgramTypeOnAcademicYeearAndSchool(Integer ac_year_id,Integer school_id) {
		List<HashMap<String, Object>> program_with_program_type = r_repo.fetchAllProgramsAndSpecializationWithProgramTypeOnAcademicYeearAndSchool(ac_year_id,school_id);
		Map<String, String> concatenated_program_assignment_of_same_program =program_with_program_type.stream().collect(Collectors.toMap(e -> e.get("program_id").toString(),
				e -> e.get("program_assignment_id").toString(),(a,b) -> a+","+b));
		
		List<Integer> program_assignment_id_of_repeated_program=new ArrayList<>();
		
		concatenated_program_assignment_of_same_program.entrySet().forEach(r ->{
			if(r.getValue().split(",").length > 1) {
				List<Integer> p=ResponseHandler.toConvertCommaSeperatedIdsAsList(r.getValue());
				p.stream().forEach(asignment_id ->{
					if( Collections.frequency(p, asignment_id) ==1) {
						program_assignment_id_of_repeated_program.add(asignment_id);
					}
				});

			}
		});
		
		program_assignment_id_of_repeated_program.stream().forEach(q ->{
			program_with_program_type.stream().forEach(s ->{
				if(s.get("program_assignment_id") == q) {
					s.put("program_name",s.get("program_name")+"-" +s.get("program_type_code"));
				}
			});

		});
		return program_with_program_type;
	}

	public List<Map<String, Object>> getProgramTypeBasedOnSchool(Integer school_id) {
		return r_repo.getProgramTypeBasedOnSchool(school_id);
	}


}
