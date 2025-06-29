package com.au.service;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.au.exception.ResourceNotFoundException;
import com.au.model.EmployeeDetails;
import com.au.model.LeaveKitty;
import com.au.model.LeaveType;
import com.au.model.TimeTable;
import com.au.model.TimeTableEmployee;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.LeaveKittyRepository;
import com.au.repository.LeavePatternRepository;
import com.au.repository.TimeTableEmployeeRepository;
import com.au.repository.TimeTableRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class EmployeeLeavesService {
	
	@Autowired
	private LeaveKittyRepository empLeaveRepo;
	
	@Autowired
	private TimeTableEmployeeRepository t_repo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private LeavePatternRepository leave_pattern_repo;
	
	@Autowired
	private UserAuthenticationRepository authentication;
	
	
	public LeaveKitty save_Department(LeaveKitty empleaves) throws Exception {

		if (getEmpLeavesId(empleaves.getEmployee_leave_id()) >= 1)
			throw new Exception("Employee leaves id is  already exist");
		 else {
			empLeaveRepo.save(empleaves);
		}
		return empleaves;
	}
	
	private Integer getEmpLeavesId(Integer getEmployee_leave_id) {
		return empLeaveRepo.getcountOfEmpLeavesId(getEmployee_leave_id);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> roles_filtered_response = empLeaveRepo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> roles_sorted_response = empLeaveRepo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}
	
	
	public List<LeaveKitty> listAll() {
		return empLeaveRepo.findAll1();
	}
	
	public List<LeaveType> leaveTypesAvailableForEmployees(Integer emp_id) {
		EmployeeDetails ed = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_id);
		List<LeaveType> leave_type = leave_pattern_repo.getLeaves(ed.getEmp_type_id(),ed.getJob_type_id());
		List<LeaveType> leaveKitty = leave_pattern_repo.getLeavesForLeaveKitty(emp_id);
		leave_type.addAll(leaveKitty);
		return leave_type;
	}
	
	public LeaveKitty get(Integer id) {
		return empLeaveRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("LeaveKitty Not Found:" + id));
	}

	public LeaveKitty saveEmpLeaves(LeaveKitty empleave) {
		return empLeaveRepo.save(empleave);
	}
	
	public void delete(Integer id) {
		LeaveKitty empleaves = empLeaveRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeaveKitty Not Found:" + id));
		empLeaveRepo.updateEmpLeaves(id);
	}

	public void delete1(Integer id) {
		LeaveKitty empleaves = empLeaveRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LeaveKitty Not Found:" + id));
		empLeaveRepo.updateEmpLeaves1(id);
	}
	
	Date date;
	public List<Map<String, Object>> getTimeTableDetailsForEmployees(Integer emp_id, String from_date, String to_date) throws ParseException {
//		LocalDate fromDate = LocalDate.fromDateFields(from_date);
//		LocalDate toDate = LocalDate.fromDateFields(to_date);
//		
//			//get no of days between given dates
//			long daysDiff = Days.daysBetween(fromDate, toDate).getDays();
//			System.out.println("(((((((((((((((((()))))))))))))))))) "+daysDiff);
//			
//			List<Map<String, Object>> tt_list = new ArrayList<Map<String, Object>>();
//			
//			 date = from_date;
//			for(int i = 0 ; i<=daysDiff ; i++) {
//				SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
//	            
//	            // Format the Date object into the desired format
//	            String formattedDate = targetFormat.format(date);
//	            System.out.println("DDDDDDDDDDDDDDDDDDDD "+formattedDate);
//				System.out.println("(((((1111111111111 "+date);
//				System.out.println("(((((1111111111111 "+t_repo.getCountOfEmployeeAndSelectedDate(emp_id, formattedDate));
//				if(t_repo.getCountOfEmployeeAndSelectedDate(emp_id, formattedDate) >=1) {
//
//					Map<String, Object> tt = t_repo.getEmployeeTimeTableSchedule(emp_id,formattedDate);
//					System.out.println("(((((((((((((((((222222222222222222222222) "+tt.get("from_date"));
//					tt_list.add(tt);
//				}
//			//increment date by 1
//			Calendar c = Calendar.getInstance();
//			c.setTime(date);
//			c.add(Calendar.DATE, 1);
//			date = c.getTime();
//
//		
//		}
		
		List<Map<String, Object>> tt_list = new ArrayList<Map<String, Object>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");        
        Date fromDate = sdf.parse(from_date);
        Date toDate = sdf.parse(to_date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);

        while (!calendar.getTime().after(toDate)) {
            Date currentDate = calendar.getTime();
            
            String formattedDate = sdf.format(currentDate);
            System.out.println("LLLLLLLLLLLLLLLLLLLL "+formattedDate); // Prints each date
            
            if(t_repo.getCountOfEmployeeAndSelectedDate(emp_id, formattedDate) >=1) {

				List<Map<String, Object>> tt = t_repo.getEmployeeTimeTableSchedule(emp_id,formattedDate);
				tt_list.addAll(tt);
			}
            
            // Increment the calendar by one day
            calendar.add(Calendar.DATE, 1);
        }

		return tt_list;
	}
	
	public HashMap<String, Object> getLeaveApproversForEmployees(Integer emp_id) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		EmployeeDetails ed = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_id);
		HashMap<String, Object> leaveApprover1 = empDetail_repo.getLeaveApprover(ed.getLeave_approver1_emp_id());
		HashMap<String, Object> leaveApprover2 = empDetail_repo.getLeaveApprover(ed.getLeave_approver2_emp_id());
		
		hm.put("leave_approver1", leaveApprover1);
		hm.put("leave_approver2", leaveApprover2);
		return hm;
	}
	
	public void updateEmployeeLeave(EmployeeDetails ed) {
		List<Integer> list_of_leave_pattern_id = leave_pattern_repo.getLeavePatternId(ed.getJob_type_id(),ed.getEmp_type_id(),ed.getSchool_id());
		System.out.println("(((((((((((((((((((((((()))))))))))))))))))))))) "+list_of_leave_pattern_id);
		list_of_leave_pattern_id.stream().forEach(lp ->{
			LeaveKitty emp_leaves = new LeaveKitty();
			emp_leaves.setAccumulated_count(null);
			emp_leaves.setAccumulated_date(null);
			emp_leaves.setActive(ed.getActive());
			emp_leaves.setCarry_status(null);
			emp_leaves.setCreated_by(ed.getCreated_by());
			emp_leaves.setCreated_date(ed.getCreated_date());
			emp_leaves.setCreated_username(ed.getCreated_username());
			emp_leaves.setEmp_id(ed.getEmp_id());
			emp_leaves.setInitial_days_count(null);
			emp_leaves.setLeave_pattern_id(lp);
			emp_leaves.setModified_by(ed.getModified_by());
			emp_leaves.setModified_date(ed.getModified_date());
			emp_leaves.setModified_username(ed.getModified_username());
			emp_leaves.setUpdated_days_count(null);
			
			empLeaveRepo.save(emp_leaves);
		});
	}
	
	//Method for scheduler
	public void updateLeavesForPermanent() {
		List<Integer> employees = empDetail_repo.getAllEmployees();
		
		
		
	}

	public List<LeaveType> leaveTypesAvailableForEmployeesForHrScreen(Integer emp_id) {
		EmployeeDetails ed = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_id);
		List<LeaveType> leave_type = leave_pattern_repo.leaveTypesAvailableForEmployeesForHrScreen(ed.getEmp_type_id(),ed.getJob_type_id());
		//List<LeaveType> leaveKitty = leave_pattern_repo.getLeavesForLeaveKitty(emp_id);
		//leave_type.addAll(leaveKitty);
		return leave_type;
	}

	public void updateRejoinEmployeeLeave(EmployeeDetails rejoinEmployee, Integer rejoin_emp_id) {
		List<Integer> list_of_leave_pattern_id = leave_pattern_repo.getLeavePatternId(rejoinEmployee.getJob_type_id(),rejoinEmployee.getEmp_type_id(),rejoinEmployee.getSchool_id());
		System.out.println("(((((((((((((((((((((((()))))))))))))))))))))))) "+list_of_leave_pattern_id);
		list_of_leave_pattern_id.stream().forEach(lp ->{
			LeaveKitty emp_leaves = new LeaveKitty();
			emp_leaves.setAccumulated_count(null);
			emp_leaves.setAccumulated_date(null);
			emp_leaves.setActive(rejoinEmployee.getActive());
			emp_leaves.setCarry_status(null);
			emp_leaves.setCreated_by(rejoinEmployee.getCreated_by());
			emp_leaves.setCreated_date(rejoinEmployee.getCreated_date());
			emp_leaves.setCreated_username(rejoinEmployee.getCreated_username());
			emp_leaves.setEmp_id(rejoin_emp_id);
			emp_leaves.setInitial_days_count(null);
			emp_leaves.setLeave_pattern_id(lp);
			emp_leaves.setModified_by(rejoinEmployee.getModified_by());
			emp_leaves.setModified_date(rejoinEmployee.getModified_date());
			emp_leaves.setModified_username(rejoinEmployee.getModified_username());
			emp_leaves.setUpdated_days_count(null);
			
			empLeaveRepo.save(emp_leaves);
		});
		
	}

	
}
