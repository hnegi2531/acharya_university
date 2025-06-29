package com.au.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.au.dto.JwtDetails;
import com.au.dto.PreadmissionDto;
import com.au.dto.ScholarshipStudentidUpdate;
import com.au.exception.ResourceNotFoundException;
import com.au.model.PreAdmissionProcess;
import com.au.model.Scholarship;
import com.au.model.ScholarshipApprovalStatus;
import com.au.repository.ScholarshipApprovalStatusRepository;
import com.au.repository.ScholarshipRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ScholarshipService {

	@Autowired
	private ScholarshipRepository pre_scholar_repo;
	
	@Autowired
	private ScholarshipApprovalStatusRepository sas_repo;
	
	@Autowired
	private JwtTokenService jwt_service;

//	public List<Scholarship> listAll(){
//		return pre_scholar_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = pre_scholar_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		
		Page<Object> response = pre_scholar_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public Scholarship saveScholarship(Scholarship p) {
		return pre_scholar_repo.save(p);
	}
	
	public Scholarship get(Integer id) {
        return pre_scholar_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("Scholarship Not Found:"+id));
    }
     
    public void delete(Integer id) {
    	Scholarship existingCourse = pre_scholar_repo.findById(id)
    	        .orElseThrow(()-> new ResourceNotFoundException("Scholarship Not Found:"+id));
    	this.pre_scholar_repo.updateScholarship(id);
    }

  
    public List<HashMap<String, Object>> fetchScholarship(){
    	return pre_scholar_repo.fetchScholarship();
    }

	public List<HashMap<String, Object>> get1(Integer sid) {
		// TODO Auto-generated method stub
		return pre_scholar_repo.get1(sid);
	}

//	public List<HashMap<String, Object>> get2(){
//		// TODO Auto-generated method stub
//		return pre_scholar_repo.get3();
//	}
	
public ResponseEntity<Object> listAllApprovedData(Pageable pageable, Object keyword){
		
		Page<Object> response1 = pre_scholar_repo.findAllApprovedData(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAllApprovedData1(Pageable pageable){
		
		Page<Object> response = pre_scholar_repo.findAllApprovedData1(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	
	public ResponseEntity<Object> listAllApprovedData4(Pageable pageable, Object keyword){
		Page<Object> response1 = pre_scholar_repo.findAllApprovedData4(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAllApprovedData14(Pageable pageable){
		Page<Object> response = pre_scholar_repo.findAllApprovedData14(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public Scholarship update1(@Valid ScholarshipStudentidUpdate s1) {

		Scholarship sch = pre_scholar_repo.getScholarshipByCid(s1.getS2().getCandidate_id());		
		sch.setStudent_id(s1.getS2().getStudent_id());
		pre_scholar_repo.save(sch);
		return sch;
	}

	public void deactivateScholarship(Integer id) {

    	pre_scholar_repo.update(id);
	}

	public void activateScholarship(Integer id) {

		pre_scholar_repo.update1(id);
	}
	
	public ScholarshipApprovalStatus saveDirectScholarship(PreadmissionDto pdto,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			Scholarship s1 = pre_scholar_repo.save(pdto.getS());

			ScholarshipApprovalStatus sasa = new ScholarshipApprovalStatus();
			sasa.setScholarship_id(s1.getScholarship_id());
			sasa.setCandidate_id(s1.getCandidate_id());

			sasa.setCreated_by(jwtDetails.getUserId());

			sasa.setActive(pdto.getSas().getActive());
			sasa.setApplied_date(pdto.getSas().getApplied_date());
			sasa.setApproval(pdto.getSas().getApproval());
			sasa.setApproved_by(pdto.getSas().getApproved_by());
			sasa.setApproved_date(pdto.getSas().getApplied_date());
			sasa.setCancel_date(pdto.getSas().getCancel_date());
			sasa.setCancel_remarks(pdto.getSas().getCancel_remarks());
			sasa.setModified_date(pdto.getSas().getModified_date());
			sasa.setCreated_username(pdto.getS().getCreated_username());
			// sasa.setModified_username(pdto.getSas().getModified_username());
			sasa.setComments(pdto.getSas().getComments());
			sasa.setCounselor_id(pdto.getSas().getCounselor_id());
			sasa.setIs_approved(pdto.getSas().getIs_approved());
			sasa.setIs_verified(pdto.getSas().getIs_verified());
			sasa.setPrev_approved_amount(pdto.getSas().getPrev_approved_amount());
			sasa.setStudent_id(pdto.getSas().getStudent_id());
			sasa.setUpdated_approved_amount_date(pdto.getSas().getUpdated_approved_amount_date());
			sasa.setVerified_amount(pdto.getSas().getVerified_amount());
			sasa.setVerified_by(pdto.getSas().getVerified_by());
			sasa.setVerified_date(pdto.getSas().getVerified_date());
			sasa.setApproved_amount(pdto.getSas().getApproved_amount());
			sasa.setPre_approval_status(pdto.getSas().getPre_approval_status());

			sasa.setYear1_amount(pdto.getSas().getYear1_amount());
			sasa.setYear2_amount(pdto.getSas().getYear2_amount());
			sasa.setYear3_amount(pdto.getSas().getYear3_amount());
			sasa.setYear4_amount(pdto.getSas().getYear4_amount());
			sasa.setYear5_amount(pdto.getSas().getYear5_amount());
			sasa.setYear6_amount(pdto.getSas().getYear6_amount());
			sasa.setYear7_amount(pdto.getSas().getYear7_amount());
			sasa.setYear8_amount(pdto.getSas().getYear8_amount());
			sasa.setYear9_amount(pdto.getSas().getYear9_amount());
			sasa.setYear10_amount(pdto.getSas().getYear10_amount());
			sasa.setYear11_amount(pdto.getSas().getYear11_amount());
			sasa.setYear12_amount(pdto.getSas().getYear12_amount());
			sasa.setRequestedByRemarks(pdto.getSas().getRequestedByRemarks());
			sasa.setIpAddress(pdto.getSas().getIpAddress());
			
			sas_repo.save(sasa);
		

		return sasa;
		
	}

	  public HttpStatus checkForScholarshipAlreadyPresentOrNot(Integer student_id){
			if(pre_scholar_repo.getCountOfStudent(student_id) >= 1) {
				throw new RuntimeException("Scholarship already created for the given AUID!!!");
			} else {
			
	    	return HttpStatus.OK;
	    }
	  }
	  
	  public ResponseEntity<Object> listAllApprovedData1(Pageable pageable, Object keyword, Integer ac_year_id){
			
			Page<Object> response1 = pre_scholar_repo.findAllApprovedData1(pageable, keyword, ac_year_id );
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		}
		
		public ResponseEntity<Object> listAllApprovedData11(Pageable pageable, Integer ac_year_id){
			
			Page<Object> response = pre_scholar_repo.findAllApprovedData11(pageable, ac_year_id);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		}	
	
}
