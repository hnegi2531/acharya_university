package com.au.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simplesystemsmanagement.model.ResourceDataSyncNotFoundException;
import com.au.model.StudentPaymentHistory;
import com.au.repository.StudentPaymentHistoryRepository;

@Service
public class StudentPaymentHistoryService {
	
		@Autowired
		private StudentPaymentHistoryRepository stu_pay_hs_repo;
		
		
		public StudentPaymentHistory saveStudentPaymentHistory(StudentPaymentHistory sph) {
			return stu_pay_hs_repo.save(sph);
		}
		
		public List<StudentPaymentHistory> getActiveDetails(){
			return stu_pay_hs_repo.getActiveDetails();
		}
		
		public List<StudentPaymentHistory> getAllDetails(){
			return stu_pay_hs_repo.findAll();
		}
		
		public StudentPaymentHistory getDetailById(Integer student_fee_payment_history_id) {
			return stu_pay_hs_repo.findById(student_fee_payment_history_id).orElseThrow(() -> 
					new ResourceDataSyncNotFoundException("Student payment History Details Not Found" + student_fee_payment_history_id));
		}
		
		public StudentPaymentHistory update(StudentPaymentHistory sph) {
			return stu_pay_hs_repo.save(sph);
		}
		
		public void delete(Integer student_fee_payment_history_id) {
			
			stu_pay_hs_repo.findById(student_fee_payment_history_id).orElseThrow(() ->
			new ResourceDataSyncNotFoundException("Student payment History Details Not Found" + student_fee_payment_history_id));
			stu_pay_hs_repo.delete1(student_fee_payment_history_id);
			
		}
		
		public void delete1(Integer student_fee_payment_history_id) {
			
			stu_pay_hs_repo.findById(student_fee_payment_history_id).orElseThrow(() ->
			new ResourceDataSyncNotFoundException("Student payment History Details Not Found" + student_fee_payment_history_id));
			stu_pay_hs_repo.delete2(student_fee_payment_history_id);
			
		}

}
