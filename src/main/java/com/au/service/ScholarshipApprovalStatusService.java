package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.au.dto.ScholarshipApprovalStatusStudentIdUpdateDto;
import com.au.dto.ScholarshipCancelDto;
import com.au.dto.ScholarshipDto;
import com.au.event.StudentDueEvent;
import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseBranch;
import com.au.model.Department;
import com.au.model.Scholarship;
import com.au.model.ScholarshipApprovalStatus;
import com.au.repository.ScholarshipApprovalStatusRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentTranscriptSubmissionRepository;
import com.au.response.ResponseHandler;

@Service
public class ScholarshipApprovalStatusService {

	@Autowired
	private ScholarshipApprovalStatusRepository s_repo;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private StudentTranscriptSubmissionRepository sts_repo;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
//	public List<ScholarshipApprovalStatus> listAll(){
//		return s_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		
		Page<Object> response1 = s_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable){
		
		Page<Object> response = s_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public ScholarshipApprovalStatus save_ScholarshipApprovalStatus(ScholarshipApprovalStatus s) {
		StudentDueEvent studentDueEvent=new StudentDueEvent(null,null,s.getStudent_id(),null);
		applicationEventPublisher.publishEvent(studentDueEvent);
		return s_repo.save(s);
	}
	
	public ScholarshipApprovalStatus get(Integer id) {
        return s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ScholarshipApprovalStatus Not Found:"+id));
    }
    
	public ScholarshipApprovalStatus update(ScholarshipDto sdto) 
	{
		ScholarshipApprovalStatus sas = s_repo.findByScholarshipId(sdto.getSas().getScholarship_id());

		sas.setCandidate_id(sdto.getSas().getCandidate_id());
		sas.setActive(sdto.getSas().getActive());		
		sas.setApplied_date(sdto.getSas().getApplied_date());
		sas.setApproval(sdto.getSas().getApproval());
		sas.setApproved_amount(sdto.getSas().getApproved_amount());
		sas.setApproved_by(sdto.getSas().getApproved_by());
		sas.setScholarship_id(sdto.getSas().getScholarship_id());
		sas.setApproved_date(sdto.getSas().getApproved_date());
		sas.setComments(sdto.getSas().getComments());
		sas.setCounselor_id(sdto.getSas().getCounselor_id());
//		sas.setCreated_by(sdto.getSas().getCreated_by());
		sas.setModified_by(sdto.getSas().getModified_by());
		sas.setModified_date(sdto.getSas().getModified_date());
		sas.setModified_username(sdto.getSas().getModified_username());
	
		sas.setDoc1(sdto.getSas().getDoc1());
		sas.setIs_approved(sdto.getSas().getIs_approved());
		sas.setIs_verified(sdto.getSas().getIs_verified());
		sas.setPrev_approved_amount(sdto.getSas().getPrev_approved_amount());
		sas.setStudent_id(sdto.getSas().getStudent_id());
		sas.setVerified_amount(sdto.getSas().getVerified_amount());
		sas.setVerified_by(sdto.getSas().getVerified_by());
		sas.setVerified_date(sdto.getSas().getVerified_date());
		sas.setPre_approval_status(sdto.getSas().getPre_approval_status());
		sas.setPre_approval_date(sdto.getSas().getPre_approval_date());
		sas.setPre_approver_remarks(sdto.getSas().getPre_approver_remarks());
		sas.setVerifier_remarks(sdto.getSas().getVerifier_remarks());
		
		sas.setYear1_amount(sdto.getSas().getYear1_amount());
		sas.setYear2_amount(sdto.getSas().getYear2_amount());
		sas.setYear3_amount(sdto.getSas().getYear3_amount());
		sas.setYear4_amount(sdto.getSas().getYear4_amount());
		sas.setYear5_amount(sdto.getSas().getYear5_amount());
		sas.setYear6_amount(sdto.getSas().getYear6_amount());
		sas.setYear7_amount(sdto.getSas().getYear7_amount());
		sas.setYear8_amount(sdto.getSas().getYear8_amount());
		sas.setYear9_amount(sdto.getSas().getYear9_amount());
		sas.setYear10_amount(sdto.getSas().getYear10_amount());
		sas.setYear11_amount(sdto.getSas().getYear11_amount());
		sas.setYear12_amount(sdto.getSas().getYear12_amount());
		
		sas.setRequestedByRemarks(sdto.getSas().getRequestedByRemarks());
		sas.setIpAddress(sdto.getSas().getIpAddress());
		sas.setCancelBy(sdto.getSas().getCancelBy());
		sas.setCancel_date(sdto.getSas().getCancel_date());
		sas.setCancel_remarks(sdto.getSas().getCancel_remarks());
		StudentDueEvent studentDueEvent=new StudentDueEvent(null,null,sas.getStudent_id(),null);
		applicationEventPublisher.publishEvent(studentDueEvent);
		
		s_repo.save(sas);
		return sas;
    }
	
    public void delete(Integer id) {
    	ScholarshipApprovalStatus ay = s_repo.findById(id)
        		.orElseThrow(()-> new ResourceNotFoundException("ScholarshipApprovalStatus Not Found:"+id));    	
    	s_repo.delete(ay);
    }
    
	public void deactivatescholarshipapprovalstatus(Integer id) {
	
		s_repo.update(id);
	}

	public void activateScholarshipapprovalstatus(Integer id) {

		s_repo.update1(id);
	}

	public ScholarshipApprovalStatus update2(ScholarshipCancelDto sdto1) {
		ScholarshipApprovalStatus sas1 = s_repo.findByScholarshipId(sdto1.getSas1().getScholarship_id());

		sas1.setCancel_remarks(sdto1.getSas1().getCancel_remarks());
		sas1.setActive(false);
		return sas1;
	}

	public ScholarshipApprovalStatus update1(@Valid ScholarshipApprovalStatusStudentIdUpdateDto s) {
		// TODO Auto-generated method stub
		ScholarshipApprovalStatus s1 = s_repo.getScholarshipByCandidateId(s.getS3().getCandidate_id());		
		s1.setStudent_id(s.getS3().getStudent_id());
		s_repo.save(s1);
		return s1;
	}
	
	public HashMap<String,Object> getDataForTestimonials(Integer student_id) {
		
		HashMap<String, Object> formatedRersponse = new HashMap<>();
		
		HashMap<String, Object> stu = stu_repo.studentDataForTestimonials(student_id);
		List<HashMap<String, Object>> transcript_details = sts_repo.getTranscriptDetails(student_id);
		
		formatedRersponse.put("Student_details", stu);
		formatedRersponse.put("Student_Transcript_Details", transcript_details);
		
		return formatedRersponse;
	}
	
	public List<HashMap<String,Object>> getIsVerifiedDataForIndex() {
		List<HashMap<String,Object>> is_verified_data = s_repo.getIsVerifiedDataForIndex();
		return is_verified_data;
	}
	
	public List<HashMap<String,Object>> getIsApprovedDataForIndex(Integer ac_year_id) {
		List<HashMap<String,Object>> is_approved_data = s_repo.getIsApprovedDataForIndex(ac_year_id);
		return is_approved_data;
	}
	
	public List<ScholarshipApprovalStatus> getYearWiseDataByStudentId(Integer student_id) {
		List<ScholarshipApprovalStatus> is_approved_data = s_repo.getYearWiseDataByStudentId(student_id);
		return is_approved_data;
	}
   
   
}
