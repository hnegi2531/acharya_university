package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.JwtDetails;
import com.au.model.PaySlipLockDate;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.PaySlipLockDateRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class PaySlipLockDateService {
	
	@Autowired
	private PaySlipLockDateRepository paySlip_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	public List<PaySlipLockDate> savePaySlipLockDate(@Valid List<PaySlipLockDate> paySlipLockDates) throws Exception {
		List<PaySlipLockDate> PaySlipLockDateList=new ArrayList<PaySlipLockDate>();
		paySlipLockDates.stream().forEach(paySlip -> {
		if (paySlip_repo.getCountOfMonthAndYear(paySlip.getSchool_id(),paySlip.getMonth(),paySlip.getYear()) >= 1)
			throw new RuntimeException("The combination of school,month and year is Already Present !!!");
		
		if (paySlip.getEmp_id() != null) {
		List<Integer> employees = ResponseHandler.toConvertCommaSeperatedIdsAsList(paySlip.getEmp_id());
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +employees);
		List<Integer> emps = new ArrayList<Integer>();
		List<String> list = paySlip_repo.getEmpIds(paySlip.getMonth(),paySlip.getYear());
		System.out.println(")))))))))))))))))))))(((((((((((((((((((((( "+list);
		 List<String> values = new ArrayList<String>();
	      for(String data: list) {
	         if(data != null) { 
	            values.add(data);
	         }
	      }
		System.out.println(")))))))))))))))))____________)))(((((((((((((((((((((( "+values);
		String s = ",";
		values.stream().forEach(l -> {
			if(l.contains(s) ) {
				List<Integer> new_list = ResponseHandler.toConvertCommaSeperatedIdsAsList(l);
				System.out.println("##############2222222222222222########" +l );
				emps.addAll(new_list);
			} else {
	
				emps.add(Integer.parseInt(l));
				System.out.println("######################" +l );
			}
		});
		ArrayList<Integer> duplicateList = new ArrayList<Integer>();
		ArrayList<Integer> uniqueList = new ArrayList<Integer>();
		System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ "+emps);
		for (Integer item : employees) {
			if (emps.contains(item)) {
				System.out.println("(((((((((((((((((((---3----)))))))))))))))))))))))))) " + item);
				throw new RuntimeException("Pay slip already generated .Please check !!!");
			} else {
				System.out.println("(((((((((((((((((((=========4=============)))))))))))))))))))))))))) " + item);
				uniqueList.add(item);
				paySlip_repo.save(paySlip);
				PaySlipLockDateList.add(paySlip);
			}
		}
	}else {	
		paySlip_repo.save(paySlip);
		PaySlipLockDateList.add(paySlip);
	}
		
		});
		return PaySlipLockDateList;
	}
	

	 public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		 Page<Map<String, Object>> response1 = paySlip_repo.findAll1(pageable, keyword );
		 return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, response1);
		  }
		
	 public ResponseEntity<Object> listAll2(Pageable pageable){
		 Page<Map<String, Object>> response = paySlip_repo.findAll2(pageable);
		 return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, response);
		    }


	   public List<PaySlipLockDate> updatePaySlipLockDate(@Valid List<PaySlipLockDate> veh) {
			return paySlip_repo.saveAll(veh);
		}	
	   
	   public List<PaySlipLockDate> get(List<Integer> id) {
			return paySlip_repo.findAllById(id);
		}
	   
	   public List<Map<String,Object>> getEmployeeNameConcateWithEmployeeCode() {	
			return empDetail_repo.getEmployeeNameConcateWithEmployeeCode();
		}	
	   
	   public List<Map<String,Object>> getPaySlipByEmployeeId(Integer emp_id, Integer month, Integer year) {
			return paySlip_repo.getPaySlipByEmployeeId(emp_id,month,year);
		}
	   
	   public PaySlipLockDate updatepaySlipLockDate(PaySlipLockDate ps, String jwtToken) 
				throws JsonParseException, JsonMappingException, IOException {
			
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			PaySlipLockDate paySlip = paySlip_repo.findById(ps.getPay_slip_lock_date_id())
			.orElseThrow(()-> new ResourceNotFoundException("Employee not found"));
			
			paySlip.setEmp_id(ps.getEmp_id());
			paySlip.setDisplay_date(ps.getDisplay_date());
			paySlip.setModified_by(jwtDetails.getUserId());
			paySlip.setModified_username(jwtDetails.getUserName());
			return paySlip_repo.save(paySlip);
		}	
	   
	   
		public void deactivate(Integer pay_slip_lock_date_id) {
			paySlip_repo.findById(pay_slip_lock_date_id).orElseThrow(() -> new RuntimeException("pay slip Not Found:" + pay_slip_lock_date_id));
			paySlip_repo.deactivate(pay_slip_lock_date_id);
		}

		public void activate(Integer pay_slip_lock_date_id) {
			paySlip_repo.findById(pay_slip_lock_date_id).orElseThrow(() -> new RuntimeException("pay slip Not Found:" + pay_slip_lock_date_id));
			paySlip_repo.activate(pay_slip_lock_date_id);
		}


}
