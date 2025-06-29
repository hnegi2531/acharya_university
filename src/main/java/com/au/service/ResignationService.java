package com.au.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CalenderYear;
import com.au.model.Department;
import com.au.model.FinancialYear;
import com.au.model.Resignation;
import com.au.model.StoreIndentRequest;
import com.au.repository.CalenderYearRepository;
import com.au.repository.DepartmentRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.NoDuesAssignmentRepository;
import com.au.repository.ResignationRepository;
import com.au.repository.School_Repository;
import com.au.response.ResponseHandler;

@Service
public class ResignationService {
	
	@Autowired
	private ResignationRepository resignation_repo;
	
	@Autowired
	private ResponseHandler response_handler;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private DepartmentRepository deptrepo;
	
	@Autowired
	private NoDuesAssignmentRepository noDuesAssignmentRepository;
	
	@Autowired
	private CalenderYearRepository calenderYearRepository;
	
	@Autowired
	private School_Repository schoolRepo;
	
	private final Integer EMPLOYEE_COUNT=1;
	private final String HEAD_HR_EMAIL= "headhr@acharya.ac.in";
//	private final String HEAD_HR_EMAIL= "divyakumari@acharya.ac.in";
//	private final String hod_email= "hanumanthab@acharya.ac.in";
	private final String reporting_email= "santanu2596@acharya.ac.in";
	private final String reporting_email1= "arun2870@acharya.ac.in";
	
	public Resignation saveResignation(Resignation res) throws Exception {
		if(resignation_repo.checkResignationDataOfEmployee(res.getEmp_id()) >=EMPLOYEE_COUNT) {
			throw new RuntimeException("Resignation Is Already Applied For This Employee");
		} else{
			String schoolName = schoolRepo.getSchoolShortNameForEmployee(res.getEmp_id());
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime now = LocalDateTime.now();
			String date = dtf.format(now);
			LocalDate date1 = null;
			date1 = LocalDate.parse(date);
			CalenderYear calenderYear = calenderYearRepository.getCalenderYearForRelievingNo(date1);
			
			Integer year = Integer.parseInt(Integer.toString(calenderYear.getCalender_year()).substring(2));      //24
			Resignation latestRelievingNo = resignation_repo.getLatestRelievingNo();
			
			String relieving_number = null;
			String count_for_id = resignation_repo.getMaxRelievingNumberCount();
			
			if (count_for_id == null) {
				relieving_number = String.format("%04d", 1);
			}else {
				String abc = count_for_id.substring(10);
				Integer count = Integer.valueOf(abc) + 1;
				relieving_number = String.format("%04d", count);
			}
			
			res.setRelieving_number("RL/"+schoolName+"/"+year+"/"+relieving_number);
			Resignation res_data = resignation_repo.save(res);

			String reason = res_data.getEmployee_reason() == null ? "" :res_data.getEmployee_reason();
			String additionalReason=res_data.getAdditional_reason() == null ? "" :res_data.getAdditional_reason().toString();
			
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String stringResignationDate = df.format(res_data.getCreated_date());
			
			LocalDate today = res_data.getRequested_relieving_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate rrd = today.plusDays(1);//while fetching date from DB, minus 1 day we are getting 
			DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String requested_relieving_date_string = rrd.format(pattern);
			
			String hod_email = empDetail_repo.getHodEmailData(res_data.getEmp_id());
			
			List<Map<String, Object>> emp_data = empDetail_repo.getEmployeeDetailByEmpId(res_data.getEmp_id());
			System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV ");
			List<String> emails = Arrays.asList(HEAD_HR_EMAIL ,hod_email ,(emp_data.get(0).get("reporting_email")).toString());
//			List<String> emails = Arrays.asList(HEAD_HR_EMAIL ,hod_email ,reporting_email ,reporting_email1);
			System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF "+(emp_data.get(0).get("reporting_email")).toString());
			String[] strarray = emails.toArray(new String[0]);

			String content = "Dear Sir/Madam," + "<br/>" + "<br/>"
					+ "This is to notify that the below staff has resigned on " + stringResignationDate.substring(0, 10)
					+ "<br/>" + "<br/>"
					+ "Employee Name : " +(emp_data.get(0).get("employee_name")).toString()  + "<br/>"
							+ "Employee Code : " +(emp_data.get(0).get("empcode")).toString()  + "<br/>"
									+ "Designation : " +(emp_data.get(0).get("designation_name")).toString()  + "<br/>"
											+ "Department : " +(emp_data.get(0).get("dept_name_short")).toString()  + "<br/>"
													+ "Requested relieving date : " + requested_relieving_date_string.substring(0, 10)  + "<br/>"
															+ "Reason : " + reason  + "<br/>"
																	+ "Additional remarks : " + additionalReason  + "<br/>" +"<br/>" +"<br/>"
					
																	
					+ "<html><b>Please note that staff has to submit the resignation letter hard copy to HR Department with HOD signature immediately after e-resign! </b></html>" +"<br/>" +"<br/>"
					
					+ "Regards<br/>"
					+ "Team ERP <br/> "
					+ "Acharya Institutes";
			String genderType =null;
			Character ch =	empDetail_repo.getGender(res_data.getEmp_id());
			 if (ch== 'M') {
				 genderType = "Mr.";
			 }else
			 {
				 genderType = "Ms."; 
			 }
			
			String subject = "Staff Resigned -" + " " + genderType + " " +(emp_data.get(0).get("employee_name")).toString()  ;
			response_handler.sendMultipleSimpleEmail(strarray, content, subject);
			return res_data;
		}
	}
	
	public List<Resignation> listAll() {
		return resignation_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = resignation_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = resignation_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	
	public ResponseEntity<Object> listAllWithKeyword(Pageable pageable, Object keyword , Integer UserId) {
		Page<Object> response1 = resignation_repo.listAllWithKeyword(pageable, keyword , UserId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAllWithOutKeyword(Pageable pageable1, Integer userId) {
		Page<Object> response = resignation_repo.listAllWithOutKeyword(pageable1,userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	
	public ResponseEntity<Object> listAllWithLeaveApprKeyword(Pageable pageable, Object keyword, Integer userId) {
		Page<Object> response1 = resignation_repo.listAllWithLeaveApprKeyword(pageable, keyword,userId );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAllLeaveApprWithOutKeyword(Pageable pageable1, Integer userId) {
		Page<Object> response = resignation_repo.listAllLeaveApprWithOutKeyword(pageable1,userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}	
	
	public ResponseEntity<Object> fetchAllResignationHistoryDetails(Pageable pageable, Object keyword) {
		Page<Object> response1 = resignation_repo.fetchAllResignationHistoryDetails(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllResignationHistoryDetails1(Pageable pageable) {
		Page<Object> response = resignation_repo.fetchAllResignationHistoryDetails1(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	
	
	public Resignation get(Integer id) {
		return resignation_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Resignation Data Not Found:" + id));
	}
	
	public Resignation updateResignation(Resignation res) {
		return resignation_repo.save(res);
	}
	
	public void delete(Integer id) {
		resignation_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Resignation Data Not Found:" + id));
		resignation_repo.update(id);
	}
	
	public void delete1(Integer id) {
		resignation_repo.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Employee Resignation Data Not Found:" + id));
		resignation_repo.update1(id);
	}

	public Integer checkEmpIdIsAlreadyPresentOrNot(Integer emp_id) {
		if (resignation_repo.checkEmpIdIsAlreadyPresentOrNot(emp_id) >=1) {
			throw new RuntimeException("Employee already applied for resignation !!!");
		}
		return emp_id;
	}
	
	boolean check;
	public Boolean checkNoDuesStatus(Integer resignation_id) {

	Integer empId = resignation_repo.getemployeeId(resignation_id);
	List<Department> department =	deptrepo.getDeptData();
	department.stream().forEach(dept ->{
		 Integer count =	noDuesAssignmentRepository.existsById(empId, dept.getDept_id());
		 check = (count == null );
		  if (!check) 
			  throw new RuntimeException("dues are not clear from department !!!");
		
		});
	return check;
	}

	public List<Map<String, Object>> getAlResignationWithRelievingNo(Integer resignation_id) {
		return resignation_repo.getAlResignationWithRelievingNo(resignation_id);
	}
	
	
	public List<Map<String, Object>> getAllResignationDetailsData(Integer resignation_id) {
		return resignation_repo.getAllResignationDetailsData(resignation_id);
	}

}
