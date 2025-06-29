package com.au.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.JwtDetails;
import com.au.dto.LockDateRequestDTO;
import com.au.dto.LockDateResponsseDTO;
import com.au.model.EmployeePayHistory;
import com.au.model.EmployeeSheet;
import com.au.model.LockDates;
import com.au.model.SalaryStructureDetails;
import com.au.model.SalaryStructureHead;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.EmployeePayHistoryRepository;
import com.au.repository.EmployeeSheetRepository;
import com.au.repository.LockDateRepository;
import com.au.repository.SalaryStructureDetailsRepository;
import com.au.repository.SalaryStructureHeadRepository;
import com.au.response.ResponseHandler;



@Service
public class LockDateService {

	@Autowired
	private LockDateRepository lockDateRepository;
	
	@Autowired
	private EmployeeSheetRepository employeeSheetRepository;
	
	@Autowired
	private EmployeePayHistoryRepository employeePayHistoryRepository;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	private SalaryStructureDetailsRepository salaryStructureDetailsRepository;
	
	@Autowired
	private SalaryStructureHeadRepository salaryStructureHeadRepository;
	
	@Autowired
	private JwtTokenService jwtTokenService;

	private final ModelMapper modelMapper = new ModelMapper();

	public ResponseEntity<Object> saveLockDates(String token, LockDateRequestDTO lockDateRequestDTO) {
		if (lockDateRepository.getCountOfMonthAndYear(lockDateRequestDTO.getLock_month(),lockDateRequestDTO.getLock_year()) >= 1)
			throw new RuntimeException("The combination of lock month and lock year is Already Present !!!");
		
		try {
			JwtDetails jwtDetails=null;
             if(StringUtils.isNotEmpty(token)) {
            	jwtDetails=jwtTokenService.callJwtToken(token);
             }
			
			LockDates lockDates = modelMapper.map(lockDateRequestDTO, LockDates.class);
			lockDates.setActive(1);
			lockDates.setCreated_by(StringUtils.isNotEmpty(jwtDetails.getUserName())?jwtDetails.getUserName():"");
			lockDateRepository.save(lockDates);

			System.out.println("Received leave_lock_date: " + lockDateRequestDTO.getLeave_lock_date());
			//Timer timer = new Timer();
			/*
			 * if (lockDateRequestDTO.getLeave_lock_date() != null) {
			 * System.out.println(lockDateRequestDTO.getLeave_lock_date()); Date parsedDate
			 * = new
			 * SimpleDateFormat("yyyy-MM-dd").parse(lockDateRequestDTO.getLeave_lock_date())
			 * ;
			 * 
			 * Calendar leaveLockCalendar =
			 * Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
			 * leaveLockCalendar.setTime(parsedDate);
			 * leaveLockCalendar.set(Calendar.HOUR_OF_DAY, 22);
			 * leaveLockCalendar.set(Calendar.MINUTE, 00);
			 * 
			 * timer.schedule(new TimerTask() {
			 * 
			 * @Override public void run() { calculateSalary(leaveLockCalendar); }
			 * 
			 * }, leaveLockCalendar.getTime()); }
			 */

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}

	}

	public ResponseEntity<Object> getLockDatesList(LockDateRequestDTO lockDateRequestDTO) {
		try {
			Pageable pageable=PageRequest.of(lockDateRequestDTO.getPageNo(),lockDateRequestDTO.getPageSize(), Sort.by("created_date"));
			Page<LockDateResponsseDTO> lockDates = lockDateRepository.getLockDatesList(lockDateRequestDTO,pageable);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", lockDates);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}

	public ResponseEntity<Object> getLockDateById(Integer lockId) {
		try {
			LockDates lockDates = lockDateRepository.getLockDateById(lockId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", lockDates);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}

	public ResponseEntity<Object> deleteLockDate(Integer lockId) {
		try {
			LockDates lockDates = lockDateRepository.getLockDateById(lockId);
			lockDates.setActive(0);
			lockDateRepository.save(lockDates);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "INACTIVE SUCCESSFULLY", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}
	
	public ResponseEntity<Object> activeLockDate(Integer lockId) {
		try {
			LockDates lockDates = lockDateRepository.getLockDateById(lockId);
			lockDates.setActive(1);
			lockDateRepository.save(lockDates);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "ACTIVE SUCCESSFULLY", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}	

	public ResponseEntity<Object> updateLockDates(Integer lockId, LockDateRequestDTO lockDateRequestDTO,String token) {
		
		try {
			JwtDetails jwtDetails=null;
            if(StringUtils.isNotEmpty(token)) {
           	jwtDetails=jwtTokenService.callJwtToken(token);
            }
			
			LockDates lockDates = lockDateRepository.getLockDateById(lockId);
			modelMapper.map(lockDateRequestDTO, lockDates);
			lockDates.setActive(1);
			lockDates.setModified_by(StringUtils.isNotEmpty(jwtDetails.getUserName())?jwtDetails.getUserName():"");
			lockDateRepository.save(lockDates);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "UPDATE SUCCESSFULLY", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}
	}

     public void calculateSalary(Calendar leaveLockCalendar) {
    	int month=leaveLockCalendar.get(Calendar.MONTH);
		List<EmployeeSheet> previousMonthRecord=employeeSheetRepository.getPreviousMonthRecords(month);
		previousMonthRecord.forEach(p->{
			List<Map<String, Object>> employeeDetails=employeeDetailsRepository.getEmployeeDetailById(p.getEmpId());
			if(ObjectUtils.isNotEmpty(employeeDetails)) {
			Map<String, Object> employee=  employeeDetails.stream().findFirst().get();
			
			Float annualSalary=  (Float) employee.get("annual_salary");
			Integer emp_id=  (Integer) employee.get("emp_id");
			Integer salary_structure_id= (Integer) employee.get("salary_structure_id");
			EmployeeSheet empSheet=	previousMonthRecord.stream().filter(e -> e.getEmpId().equals(emp_id)).findFirst().get();
			Float basic=0.0f;
			//Float basic=annualSalary*empSheet.getPresentdays()/empSheet.getPaydays();
			List<SalaryStructureDetails> salaryStructureDetails=salaryStructureDetailsRepository.getSalaryStructureDetails(salary_structure_id);
			Map<String,String> salaryhead=new HashMap<String,String>();
			Map<String,Object> componentMap=new HashMap<String,Object>();
			for (SalaryStructureDetails salary : salaryStructureDetails) {
			SalaryStructureHead salaryStructureHead=salaryStructureHeadRepository.getSalaryStructureHead(salary.getSalary_structure_head_id());
			salaryhead.put(salaryStructureHead.getPrint_name(), salaryStructureHead.getCategory_name_type());
			componentMap=calculateComponent(salaryStructureHead,salary,basic,componentMap);
			}
			EmployeePayHistory employeePayHistory=new EmployeePayHistory();
			employeePayHistory.setBasic(basic);
			employeePayHistory.setDa((Float) componentMap.get("da"));
			employeePayHistory.setHra((Float) componentMap.get("hra"));
			employeePayHistory.setCca((Float) componentMap.get("cca"));
			employeePayHistory.setPt((Float) componentMap.get("pt"));
			Float totalEarning=0.0f;
			Float totalDeduction=0.0f;
			for(Entry<String, String> k:salaryhead.entrySet()) {
				if(StringUtils.equals(k.getValue(), "Earning") && ObjectUtils.isNotEmpty(componentMap.get(k.getKey()))) {
					totalEarning=totalEarning+(Float) componentMap.get(k.getKey());
				}
				if(StringUtils.equals(k.getValue(), "Deduction") && ObjectUtils.isNotEmpty(componentMap.get(k.getKey()))) {
					totalDeduction=totalDeduction+(Float) componentMap.get(k.getKey());
				}
			}
			employeePayHistory.setTotal_deduction(totalDeduction);
			employeePayHistory.setTotal_earning(totalEarning);
			employeePayHistory.setGross_pay(totalEarning);
			Float netpay=totalEarning-totalDeduction;
			employeePayHistory.setNet_pay(netpay);
			//employeePayHistory.setPay_days(p.getPaydays());
			employeePayHistory.setYear(p.getYear());
			employeePayHistory.setMonth(p.getMonth());
			employeePayHistory.setEmp_id(emp_id);
			EmployeePayHistory empPayHistory=employeePayHistoryRepository.findByEmpIdAndMonthAndYear(emp_id,p.getMonth(),p.getYear());
			if(ObjectUtils.isEmpty(empPayHistory)) {
	        employeePayHistoryRepository.save(employeePayHistory);
			}
			}
		});
		
	}

	private Map<String, Object> calculateComponent(SalaryStructureHead salaryStructureHead, SalaryStructureDetails salary, Float basic, Map<String, Object> componentMap) {
		Float da=null;
		Float hra=null;
		Float pt=null;
		Float cca=null;
		if(salaryStructureHead.getSalary_structure_head_id()==2) {
			da=(salary.getPercentage()/100)*basic;
			componentMap.put("da", ObjectUtils.isNotEmpty(da)? da:0);	
		}
		if(salaryStructureHead.getSalary_structure_head_id()==3) {
			hra=(salary.getPercentage()/100)*(basic+(Float) componentMap.get("da"));
			componentMap.put("hra", ObjectUtils.isNotEmpty(hra)? hra:0);	
		}
		if(salaryStructureHead.getSalary_structure_head_id()==4) {
			pt=(salary.getPercentage()/100)*basic;
			componentMap.put("pt",ObjectUtils.isNotEmpty(pt)? pt:0);	
		}
		if(salaryStructureHead.getSalary_structure_head_id()==5) {
			cca=(salary.getPercentage()/100)*(basic+(Float) componentMap.get("da"));
			componentMap.put("cca", ObjectUtils.isNotEmpty(cca)? cca:0);
		}
		
		return componentMap;
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> filterList = lockDateRepository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, filterList);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> filterList = lockDateRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, filterList);
	}

	public List<HashMap<String, Object>> getLockDateDetailsData(Integer lock_month,Integer lock_year) {
		return lockDateRepository.getLockDateDetailsData(lock_month,lock_year);
	}	
	
	
}
