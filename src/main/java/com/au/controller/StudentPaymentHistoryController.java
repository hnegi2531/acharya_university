package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.StudentPaymentHistory;
import com.au.service.JwtTokenService;
import com.au.service.StudentPaymentHistoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class StudentPaymentHistoryController {
	
		Logger log=LoggerFactory.getLogger(StudentPaymentHistoryController.class);
		
		@Autowired
		private StudentPaymentHistoryService stu_pay_hs_ser;
		
		@Autowired
		private JwtTokenService jwt_service;
		
		@PostMapping("/studentPaymentHistory")
		public StudentPaymentHistory saveStudentPaymentHistory(@RequestBody @Valid StudentPaymentHistory sph , @RequestHeader("Authorization") String jwtToken)
									throws  JsonParseException, JsonMappingException, IOException {
						
					JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
					sph.setCreated_by(jwtDetails.getUserId());
					sph.setCreated_username(jwtDetails.getUserName());
					return stu_pay_hs_ser.saveStudentPaymentHistory(sph);
		}
		
		@GetMapping("/activeStudentPaymentHistoryDetail")
		public List<StudentPaymentHistory> getActiveDetails(){
					return stu_pay_hs_ser.getActiveDetails();
		}
		
		@GetMapping("/fetchAllStudentPaymentHistoryDetail")
		public List<StudentPaymentHistory> getAllDetails(){
					return stu_pay_hs_ser.getAllDetails();
		}
		
		@GetMapping("/studentPaymentHistoryDetail/{student_fee_payment_history_id}")
		public ResponseEntity<StudentPaymentHistory> getDetailById(@PathVariable Integer student_fee_payment_history_id){
			
					try {
						StudentPaymentHistory st_pay= stu_pay_hs_ser.getDetailById(student_fee_payment_history_id);
						return new ResponseEntity<StudentPaymentHistory>(st_pay, HttpStatus.OK);
					}catch (NoSuchElementException e_) {
						return new ResponseEntity<StudentPaymentHistory>(HttpStatus.NOT_FOUND);
					}
		}
		
		@PutMapping("/studentPaymentHistory/{student_fee_payment_history_id}")
		public ResponseEntity<StudentPaymentHistory> update(@RequestBody @Valid StudentPaymentHistory sph ,
							@PathVariable Integer student_fee_payment_history_id, @RequestHeader("Authorization") String jwtToken)
					throws  JsonParseException, JsonMappingException, IOException{
					try {
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						sph.setModified_by(jwtDetails.getUserId());
						sph.setModified_username(jwtDetails.getUserName());
						stu_pay_hs_ser.update(sph);
						   return new ResponseEntity<>(HttpStatus.OK);
				    } catch (NoSuchElementException e) {
				        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				    }
				}
		
		@DeleteMapping("/studentPaymentHistory/{student_fee_payment_history_id}")
			public void delete(@PathVariable Integer student_fee_payment_history_id) {
				stu_pay_hs_ser.delete(student_fee_payment_history_id);
			
		}
		
		@DeleteMapping("/activateStudentPaymentHistory/{student_fee_payment_history_id}")
			public void delete1(@PathVariable Integer student_fee_payment_history_id) {
				stu_pay_hs_ser.delete1(student_fee_payment_history_id);
			}
		
}
