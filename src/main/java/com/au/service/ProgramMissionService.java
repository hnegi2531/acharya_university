package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.AcademicSchoolVision;
import com.au.model.ProgramMission;
import com.au.repository.ProgramMissionRepository;
import com.au.response.ResponseHandler;

@Service
public class ProgramMissionService {

	@Autowired
	private ProgramMissionRepository pm_repo;
	
	
	public List<ProgramMission> listAll(){
		return pm_repo.findAll1();
	}

	
	public ProgramMission saveProgramMission(ProgramMission s) {
		return pm_repo.save(s);
	}
	
	public ProgramMission get(Integer id) {
        return pm_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ProgramMission Not Found:"+id));
    }
     
	 public ResponseEntity<Object> delete(Integer id) {
	    	ProgramMission cc = pm_repo.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("AcademicSchoolVision Not Found:" + id));
			pm_repo.updateprogram_mission(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;

		}
	    
    
    public ResponseEntity<Object> delete1(Integer id) {
    	ProgramMission cc = pm_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("AcademicSchoolVision Not Found:" + id));
		pm_repo.updateprogram_mission1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;

	}
    
   public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = pm_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		
		Page<Object> response = pm_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
    
   
    
}
