package com.au.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.TutionFeeWaiver;
import com.au.repository.TutionFeeWaiverRepository;
import com.au.response.ResponseHandler;



@Service
public class TutionFeeWaiverService {
	
		@Autowired
		private TutionFeeWaiverRepository tut_waive_repo;
		
		
		
				
		public TutionFeeWaiver saveTutionFeeWaiver(TutionFeeWaiver tut) {
			/*tut.getTut_fee_report().stream().forEach(fee_report -> {
				TutionFeeWaiverReport fee_report1=new TutionFeeWaiverReport();
				fee_report1.setTution_fee_waiver_id(fee_report.getTution_fee_waiver_id());
				fee_report1.setWaiver_amount(fee_report.getWaiver_amount());
				fee_report1.setIs_approved(fee_report.getIs_approved());
				fee_report1.setApproved_by(fee_report.getApproved_by());
				fee_report1.setApproved_date(fee_report.getApproved_date());
				fee_report.setYear_sem(fee_report.getYear_sem());
				fee_report.setCreated_username(fee_report.getCreated_username());
				fee_report.setModified_username(fee_report.getModified_username());
				fee_report.setCreated_by(fee_report.getCreated_by());
				fee_report.setModified_by(fee_report.getModified_by());
				fee_report.setActive(fee_report.getActive());
				tut_repo.save(fee_report1);
				
			});*/
			if((tut_waive_repo.countOfTutionFeeDetail(tut.getStudent_id()))>0) {
				throw new RuntimeException("Tution Fee waiver Details present of student id :- " + tut.getStudent_id());
			}else {
			
			return tut_waive_repo.save(tut);
			}
		}
		
		public List<TutionFeeWaiver> getActiveDetails(){
			return tut_waive_repo.getActiveDetails();
		}
		
		public ResponseEntity<Object> getAllDetails1(Pageable pageable, Object keyword){
			Page<Object> response1 = tut_waive_repo.findAll2(pageable, keyword );
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		}
		
		public ResponseEntity<Object> getAllDetails2(Pageable pageable){
			Page<Object> response = tut_waive_repo.findAll3(pageable);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		}
		
		public Map<String ,Object> getTutionFeeDetailsById(Integer tution_fee_waiver_id) {
			return tut_waive_repo.findById1(tution_fee_waiver_id);
					
		}
		
		public TutionFeeWaiver updateTutionFeeWaiver(TutionFeeWaiver tut) {
			return tut_waive_repo.save(tut);
		}
		
		public void delete1(Integer tution_fee_waiver_id) {
			tut_waive_repo.delete1(tution_fee_waiver_id);
		}
		
		public void delete2(Integer tution_fee_waiver_id) {
			tut_waive_repo.delete2(tution_fee_waiver_id);
		}

		public TutionFeeWaiver getTutionFeeDetailsByStudentId(Integer student_id) {
			return tut_waive_repo.getTutionFeeDetailsByStudentId(student_id);
		}
		
		public Integer countOfTutionFeeDetail(Integer student_id) {
			return tut_waive_repo.countOfTutionFeeDetail(student_id);
		}
		
		public List<Map<String, Object>> fetchTutionFeeWaiverForEdit(Integer tution_fee_waiver_id) {
			return tut_waive_repo.fetchTutionFeeWaiverForEdit(tution_fee_waiver_id);
		}
		
}
