package com.au.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.au.dto.TutionFeeWaiverDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.TutionFeeWaiver;
import com.au.model.TutionFeeWaiverReport;
import com.au.model.TutionFeeWaiverSubAmount;
import com.au.repository.TutionFeeWaiverReportRepository;
import com.au.repository.TutionFeeWaiverSubAmountRepository;
import com.au.response.ResponseHandler;

@Service
public class TutionFeeWaiverReportService {
	
		@Autowired
		private TutionFeeWaiverReportRepository tut_repo;
		
		@Autowired
		private TutionFeeWaiverService tut_waive_ser;
		
		@Autowired
		private TutionFeeWaiverSubAmountRepository tfwsa_repo;
		
		
		public List<TutionFeeWaiverReport> saveTutionFeeWaiverReport(TutionFeeWaiverDto tut_report) {
			
			
				List<TutionFeeWaiverReport> list_tut_report=new ArrayList<>();
				//List<TutionFeeWaiverReport> list_tut_report1=new ArrayList<>();
				/*tut_report.getYear_sem().entrySet().stream().forEach(fee_report2 -> {
					list_tut_report1.add(fee_report2);
				});*/
				/*if(tut_repo.CountOfTutionFeeWaiverReportByTution_fee_waiver_id(list_tut_report1.get(0).getTution_fee_waiver_id())>0){
						throw new RuntimeException("Tution Fee waiver Report Details present of tution_fee_waiver_id :- " + list_tut_report1.get(0).getTution_fee_waiver_id());
				}
				
				else {
				
						tut_report.getTut_fee_report().stream().forEach(fee_report -> {
						
						TutionFeeWaiverReport fee_report1=new TutionFeeWaiverReport();
						fee_report1.setTution_fee_waiver_id(fee_report.getTution_fee_waiver_id());
						fee_report1.setWaiver_amount(fee_report.getWaiver_amount());
						fee_report1.setIs_approved(fee_report.getIs_approved());
						fee_report1.setApproved_by(fee_report.getApproved_by());
						fee_report1.setApproved_date(fee_report.getApproved_date());
						fee_report1.setYear_sem(fee_report.getYear_sem());
						fee_report1.setCreated_username(fee_report.getCreated_username());
						fee_report1.setModified_username(fee_report.getModified_username());
						fee_report1.setCreated_by(fee_report.getCreated_by());
						fee_report1.setModified_by(fee_report.getModified_by());
						fee_report1.setActive(fee_report.getActive());
						tut_repo.save(fee_report1);
						list_tut_report.add(fee_report1);
						
						});
				}*/
				
				TutionFeeWaiver tut_fee1=tut_waive_ser.saveTutionFeeWaiver(tut_report.getTut_fee_wavier());
				if(tut_repo.CountOfTutionFeeWaiverReportByTution_fee_waiver_id(tut_report.getTution_fee_waiver_id())>0){
					throw new RuntimeException("Tution Fee waiver Report Details present of tution_fee_waiver_id :- " + tut_report.getTution_fee_waiver_id());
			}
			
			else {
			
					tut_report.getYear_sem().entrySet().stream().forEach(fee_report -> {
					
					TutionFeeWaiverReport fee_report1=new TutionFeeWaiverReport();
					fee_report1.setTution_fee_waiver_id(tut_fee1.getTution_fee_waiver_id());
					fee_report1.setYearly_waiver_amount(fee_report.getValue());
					fee_report1.setYear_sem(fee_report.getKey());
					fee_report1.setCreated_username(tut_report.getCreated_username());
					fee_report1.setModified_username(tut_report.getModified_username());
					fee_report1.setCreated_by(tut_report.getCreated_by());
					fee_report1.setModified_by(tut_report.getModified_by());
					fee_report1.setActive(tut_report.getActive());
					tut_repo.save(fee_report1);
					list_tut_report.add(fee_report1);
					
					});
					
					tut_report.getTut_fee_waiver_sub_amount().stream().forEach(f -> {
			
						TutionFeeWaiverSubAmount tfwsa = new TutionFeeWaiverSubAmount();
						tfwsa.setCreated_username(tut_report.getCreated_username());
						tfwsa.setCreated_by(tut_report.getCreated_by());
						tfwsa.setVoucher_head_new_id(f.getVoucher_head_new_id());
						tfwsa.setTution_fee_waiver_id(tut_fee1.getTution_fee_waiver_id());
						tfwsa.setOffer_waiver_amount(f.getOffer_waiver_amount());
						tfwsa.setActive(f.getActive());
						tfwsa.setYear1_amt(f.getYear1_amt());
						tfwsa.setYear2_amt(f.getYear2_amt());
						tfwsa.setYear3_amt(f.getYear3_amt());
						tfwsa.setYear4_amt(f.getYear4_amt());
						tfwsa.setYear5_amt(f.getYear5_amt());
						tfwsa.setYear6_amt(f.getYear6_amt());
						tfwsa.setYear7_amt(f.getYear7_amt());
						tfwsa.setYear8_amt(f.getYear8_amt());
						tfwsa.setYear9_amt(f.getYear9_amt());
						tfwsa.setYear10_amt(f.getYear10_amt());
						tfwsa.setYear11_amt(f.getYear11_amt());
						tfwsa.setYear12_amt(f.getYear12_amt());
						
						tfwsa_repo.save(tfwsa);
						
					});
					
				
			}
				
			
				return list_tut_report;
		}
			
				
		public List<TutionFeeWaiverReport> activeTutionFeeWaiverReportDetails(){
			return tut_repo.activeTutionFeeWaiverReportDetails();
		}
		
		public ResponseEntity<Object> fetchAllTutionFeeWaiverReportDetails1(Pageable pageable, Object keyword){
			Page<Object> response1 = tut_repo.findAll2(pageable, keyword );
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		}
		
		public ResponseEntity<Object> fetchAllTutionFeeWaiverReportDetails2(Pageable pageable){
			Page<Object> response = tut_repo.findAll3(pageable);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		}
		
		public TutionFeeWaiverReport getTutionFeeWaiverReportDetailById(@PathVariable Integer tution_fee_waiver_report_id) {
			return tut_repo.findById(tution_fee_waiver_report_id)
					.orElseThrow(() -> new ResourceNotFoundException("Tutuion_Fee_Waiver_Details Not Found : " + tution_fee_waiver_report_id));
		}
		
		public List<TutionFeeWaiverReport> updateTutionFeeWaiverReportDetails(List<TutionFeeWaiverReport> tut_report){
			return tut_repo.saveAll(tut_report);
		}
		
		public void delete1(Integer tution_fee_waiver_report_id) {
			tut_repo.delete1(tution_fee_waiver_report_id);
		}
		
		public void delete2(Integer tution_fee_waiver_report_id) {
			tut_repo.delete2(tution_fee_waiver_report_id);
		}
		
		public List<TutionFeeWaiverReport> getTutionFeeWaiverReportDetailByTutionFeeWaiverId(Integer tution_fee_waiver_id){
			return tut_repo.getTutionFeeWaiverReportDetailByTutionFeeWaiverId(tution_fee_waiver_id);
		}
		
		


}
