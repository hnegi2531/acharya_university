package com.au.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;

import com.au.dto.JwtDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseObjective;
import com.au.model.InternalSessionCreation;
import com.au.model.InternalStudentAssignment;
import com.au.model.UserAuthentication;
import com.au.repository.CourseAssignmentRepository;
import com.au.repository.CourseStudentAssignmentRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.InternalFacultyRoomAssignmentRepository;
import com.au.repository.InternalSessionCreationRepository;
import com.au.repository.InternalStudentAssignmentRepository;
import com.au.repository.LeaveApplyRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.TimeTableEmployeeRepository;
import com.au.repository.TimeTableRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class InternalStudentAssignmentService {
	
	@Autowired
	private InternalStudentAssignmentRepository itta_repo;
	
	@Autowired
	private TimeTableEmployeeRepository tte_repo;
	
	@Autowired
	private InternalFacultyRoomAssignmentRepository itt_repo;
	

	@Autowired
	private CourseAssignmentRepository repo_carepo;
	
	
	@Autowired
	private CourseStudentAssignmentRepository csa_carepo;
	
	@Autowired
	private EmployeeDetailsRepository empDetail_repo;
	
	@Autowired
	private TimeTableEmployeeRepository tter_ser;
	
	@Autowired
	private LeaveApplyRepository leaveapplyrepo;
	
	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private InternalSessionCreationRepository isa_repo;
	
	@Autowired
	private ProgramSpecilizationRepository ps_repo;
	
	@Autowired
	private TimeTableRepository timeTableRepository;
	
   public InternalStudentAssignment saveInternalStudentAssignments(@Valid InternalStudentAssignment itta) {
		
//		if(itta_repo.countOfInternalroomId(itta.getInternal_room_id())>=1) {
//			throw new RuntimeException("Internal Room Id Already Exist");
//		}else 
//	   List<Integer> list_std = ResponseHandler.toConvertCommaSeperatedIdsAsList(itta.getStudent_ids());
//	   System.out.println("{{{{{{{{{{{{{{{{{}}}}}}}}}}}}}}}}} "+list_std);
//	   if(itta_repo.getCountInternalStudentAssignments(itta.getRoom_id(), list_std,itta.getEmp_ids(),
//			   itta.getSelected_date(),itta.getTime_slots_id()) >= 1) {
//			throw new RuntimeException("room id  Already Exist with Combination Of room id and emp id and student id");
//		}
	   
//	   InternalSessionCreation data = isa_repo.getOne(itta.getInternal_session_id());
//	   
//	    if(itta_repo.getCountTimeTableEmployee(itta.getInternal_session_id()) >=1){
//		   throw new RuntimeException("Data  Already Exist with Combination Of employee id and time slot and selected Date and internal");
//	   }		
////	   
////	   else if(itta_repo.getCountOfEmployeeInInternalTimeTable(itta.getEmp_ids(),itta.getTime_slots_id()) >=1){
////		   throw new RuntimeException("Employee id  Already Exist with Combination Of employee id and time slot and Student id");
////	   }
//	 else {
//			itta_repo.save(itta);
//	     }	
		
	   return itta_repo.save(itta);
	 
   }




  public List<InternalStudentAssignment> listAll() {
		return itta_repo.findAll1();
	}
	
   
   public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		List<Map<String, Object>> response1 = itta_repo.findAll1(pageable, keyword );
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}
	
   
   public ResponseEntity<Object> listAll2(Pageable pageable) {
		List<Map<String, Object>> response = itta_repo.findAll2(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

   
   public InternalStudentAssignment get(Integer id) {
		return itta_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("InternalStudentAssignment Not Found:" + id));
	}


   
   public InternalStudentAssignment saveInternalStudentAssignments1(@Valid InternalStudentAssignment itta, Integer id) {

			return itta_repo.save(itta);

	
	}


	public void updateMultipleInternalStudentAssignment(List<Integer> ids, JwtDetails jwtDetails) {

		itta_repo.updateMultipleInternalStudentAssignment(ids, jwtDetails.getUserId(), jwtDetails.getUserName());


	}

   
   public void delete(Integer id) {
	   InternalStudentAssignment ay = itta_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Internal TimeTable Assignment Not Found:" + id));
	   itta_repo.update(id);
	}

	public void delete1(Integer id) {
		InternalStudentAssignment ay = itta_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Internal TimeTable Assignment Not Found:" + id));
		itta_repo.update1(id);
	}

	
	public List<Map<String, Object>> listAllIttaEmpBasedOnTimeAndDate1(Integer time_slots_id,String selected_date,Integer course_assignment_id) {
		return itta_repo.listAllIttaEmpBasedOnTimeAndDate11(time_slots_id,selected_date,course_assignment_id);
	}
	
	
	public List<Map<String, Object>> listAllIttaRoomBasedOnTimeAndDate1(Integer program_specialization_id,Integer course_assignment_id,Date date) {
		List<Integer> assigned_room_id = itta_repo.getAssignedRooms(program_specialization_id,course_assignment_id,date);
		System.out.println("((((((((((((((((((())))))))))))))))))) "+assigned_room_id);
		if(assigned_room_id.isEmpty()) {
		return itta_repo.listAllIttaRoomBasedOnTimeAndDate11();
		}
		else {
			return itta_repo.listAllUnassignedIttaRoomBasedOnTimeAndDate11(assigned_room_id);
		}
	//	if(itta_repo.getAssignedRooms(program_specialization_id,course_id,date) >= 1)
		
	}

	
	public List<HashMap<String, Object>> listIttaCourseBasedOnDate1(Integer internal_session_id,Date date_of_exam) {
		List<InternalSessionCreation> list_ca_id = itt_repo.getCourseAssIds(internal_session_id, date_of_exam);
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& "+list_ca_id);
				List<HashMap<String, Object>> hm = new ArrayList<HashMap<String, Object>>();
				list_ca_id.stream().forEach(l -> {
					
					HashMap<String, Object> hm1 = itt_repo.listIttaCourseBasedOnDate11(l.getCourse_assignment_id());
					hm1.put("active",l.getActive());
					hm1.put("internal_session_id", l.getInternal_session_id());
					hm1.put("date_of_exam", l.getDate_of_exam());
//					hm1.put("internal_id", l.getInternal_id());
					hm1.put("max_marks", l.getMax_marks());
					hm1.put("min_marks ", l.getMin_marks());
					hm1.put("time_slots_id", l.getTime_slots_id());
					hm1.put("week_day", l.getWeek_day());
					
					hm.add(hm1);
				});
		return hm;
		}
	

	public List<HashMap<String, Object>> listOfStudentDetails1(Integer internal_session_id,Integer course_assignment_id,Integer school_id,Integer program_specialization_id,Integer ac_year_id,Integer year_sem) {
	String course_type = repo_carepo.getCourseCategoryType11(course_assignment_id,school_id,program_specialization_id,ac_year_id,year_sem);
	System.out.println("((((((((((((((((((((((1)))))))))))))))))))))))) "+course_type);
	if(course_type.equalsIgnoreCase("optional"))
	{
		 List<Integer> std_list_from_course_student_assignment = csa_carepo.getStudentIds(course_assignment_id);
		 System.out.println("(((((((((((((((((((2)))))))))))))))))))))))))) "+std_list_from_course_student_assignment);
		 String s=itta_repo.getStudentIds(course_assignment_id,internal_session_id);
		 if(s!=null) {
		 List<Integer> std_list_from_itta_assignment = ResponseHandler.toConvertCommaSeperatedIdsAsList(itta_repo.getStudentIds(course_assignment_id,internal_session_id));
		 System.out.println("(((((((((((((((((((3)))))))))))))))))))))))))) "+std_list_from_itta_assignment);
	
		 ArrayList<Integer> duplicateList = new ArrayList<Integer>();
		 ArrayList<Integer> uniqueList = new ArrayList<Integer>();
		 
		 for (Integer item : std_list_from_course_student_assignment) {
			    if (std_list_from_itta_assignment.contains(item)) {
			    	 System.out.println("(((((((((((((((((((-------)))))))))))))))))))))))))) "+item);
			        duplicateList.add(item);
			    } else {
			    	 System.out.println("(((((((((((((((((((======================)))))))))))))))))))))))))) "+item);
			        uniqueList.add(item);
			    }
			}
		 System.out.println("(((((((((((((((((((4)))))))))))))))))))))))))) "+uniqueList);
		 List<HashMap<String, Object>> data = csa_carepo.getStudentData1(uniqueList);
		 return data;
		 }else {
		 return csa_carepo.getStudentData1Data(course_assignment_id);
		 }
	}
	else 
	{
		 return csa_carepo.getAllStudentData(school_id,program_specialization_id,ac_year_id);
	}

	}


	public List<Map<String, Object>> getUnoccupiedEmployeesForInternals(Integer time_slots_id, Date selected_date, String date) {
		
		List<Integer> emp_ids_from_internal_time_table = itta_repo.getEmplIdsFromInternalTimeTable(time_slots_id,selected_date);
		System.out.println("(((((((((((((((((((---1----)))))))))))))))))))))))))) " + emp_ids_from_internal_time_table);
		
		List<Integer> employees_on_leave = leaveapplyrepo.getEmplIdsOnLeave(date);
		System.out.println("(((((((((((((((((((-------)))))))))))))))))))))))))) " + employees_on_leave);
		
		List<Integer> unavailable_employees_list = new ArrayList<Integer>();
		unavailable_employees_list.addAll(emp_ids_from_internal_time_table);
		unavailable_employees_list.addAll(employees_on_leave);
		
		List<Integer> all_emp_ids = empDetail_repo.getAllEmployees1();
		System.out.println("(((((((((((((((((((----2---)))))))))))))))))))))))))) " + all_emp_ids);
		
		ArrayList<Integer> duplicateList = new ArrayList<Integer>();
		ArrayList<Integer> uniqueList = new ArrayList<Integer>();

		for (Integer item : all_emp_ids) {
			if (unavailable_employees_list.contains(item)) {
				System.out.println("(((((((((((((((((((---3----)))))))))))))))))))))))))) " + item);
				duplicateList.add(item);
			} else {
				System.out.println("(((((((((((((((((((=========4=============)))))))))))))))))))))))))) " + item);
				uniqueList.add(item);
			}
		}
		System.out.println("(((((((((((((((((((5)))))))))))))))))))))))))) " + uniqueList);
		List<Map<String, Object>> data = empDetail_repo.getEmployeeData1(uniqueList);
		return data;

	}
	

	public List<Map<String, Object>> listOfStudentDetails(Integer internal_student_assignment_id) {
		String studentIds = itta_repo.getStudentIds(internal_student_assignment_id);
		List<Integer> list_students = ResponseHandler.toConvertCommaSeperatedIdsAsList(studentIds);
		return itta_repo.listOfStudentDetails(list_students);
	}
	

	public List<Map<String, Object>> InternalStudentAssignmentDetailsByUserId(Integer userId) {
		UserAuthentication userDetails =userAuthenticationRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found: " + userId));
		Integer employeeId=empDetail_repo.getEmpId(userDetails.getEmail());
		return itta_repo.InternalStudentAssignmentDetailsByEmployeeId(employeeId);
	}
	
	public List<Map<String, Object>> InternalStudentAssignmentDetailsByEmployeeId(Integer employeeId) {
		return itta_repo.InternalStudentAssignmentDetailsByEmployeeId(employeeId);
	}
	
	public Map<String, Object> checkInternalExamAttendanceStatus(Integer internal_student_assignment_id) {
		return itta_repo.checkInternalExamAttendanceStatus(internal_student_assignment_id);
	}


	public List<Map<String, Object>> checkInternalExamAttendanceStatusList(List<Integer> internal_student_assignment_id) {
		return itta_repo.checkInternalExamAttendanceStatusList(internal_student_assignment_id);
	}

	public List<Map<String, Object>> internalStudentDetailsData(Integer time_slots_id, Integer room_id, String selected_date) {
	
	List<String> studentIds = 	itta_repo.getAllStudentIds(time_slots_id,room_id,selected_date);
	
	List<Map<String, Object>> stu = new ArrayList<Map<String, Object>>();
	Set<Integer> stud = new TreeSet<Integer>();

	List<String> values = new ArrayList<String>();
	  for(String data: studentIds) {
    	  if(data != null && !(data.isEmpty())) { 
	            values.add(data);
	         }
      }
	  String s = ",";
		values.stream().forEach(l -> {
			if(l.contains(s) ) {
				List<Integer> new_list = ResponseHandler.toConvertCommaSeperatedIdsAsList(l);
				stud.addAll(new_list);
			} else {
	
				stud.add(Integer.parseInt(l));
			}
		});
		
		stud.stream().forEach(st ->{
			Map<String, Object> studentData =	stu_repo.getStudentDetailsData(st);
			stu.add(studentData);
		});
		return stu;
	}




	public List<Map<String, Object>> getStudentDataByCourseAssignmentId(Integer program_specialization_id, Integer current_sem, Integer current_year, Integer course_assignment_id) {
        
        return itta_repo.getStudentDataByCourseAssignmentId(program_specialization_id, current_sem, current_year, course_assignment_id);
    }

	public List<InternalStudentAssignment> internalStudentIdsBasedOnDateAndTimeSlots(String date_of_exam, Integer time_slots_id) {
		return itta_repo.getInternalStudentIdsBasedOnDateAndTimeSlots(date_of_exam, time_slots_id);
	}


	public List<InternalStudentAssignment> get1(List<Integer> id) {
		return itta_repo.findAllById(id);
	}
	
	
}
