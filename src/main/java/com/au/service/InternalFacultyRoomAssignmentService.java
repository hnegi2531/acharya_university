package com.au.service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.InternalFacultyRoomAssignmentDto;
import com.au.dto.InternalFacultyRoomAssignmentDtoUpdate;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.InternalFacultyRoomAssignment;
import com.au.model.InternalRoom;
import com.au.model.UserAuthentication;
import com.au.repository.CourseRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.InfrastructureRoomsRepository;
import com.au.repository.InternalFacultyRoomAssignmentRepository;
import com.au.repository.InternalSessionCreationRepository;
import com.au.repository.LeaveApplyRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.TimeTableEmployeeRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;




@Service
public class InternalFacultyRoomAssignmentService {
	
	@Autowired
	private InternalFacultyRoomAssignmentRepository itt_repo;
	
	@Autowired
	private CourseRepository course_repository;

	@Autowired
	private InternalSessionCreationRepository isa_repo;
	
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
	public InfrastructureRoomsRepository rooms_repo;
	

	@Autowired
	private JwtTokenService jwt_service;
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	public List<InternalFacultyRoomAssignment> saveInternalFacultyRoomAssignment(InternalFacultyRoomAssignmentDto internalFacultyRoomAssignmentDto, String jwtToken) throws Exception {
		
		List<InternalFacultyRoomAssignment> listInternalFacultyRoomAssignment = new ArrayList<InternalFacultyRoomAssignment>();
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		internalFacultyRoomAssignmentDto.getInternal_session_id().stream().forEach(internalSession -> {
			
			internalFacultyRoomAssignmentDto.getEmp_ids().stream().forEach(emp -> {
				
				InternalFacultyRoomAssignment newInternalRoom = new InternalFacultyRoomAssignment();
				
				newInternalRoom.setCreated_by(jwtDetails.getUserId());
				newInternalRoom.setCreated_username(jwtDetails.getUserName());
				newInternalRoom.setActive(internalFacultyRoomAssignmentDto.getActive());
				newInternalRoom.setInternal_session_id(internalSession);
				newInternalRoom.setEmp_ids(emp);
				
				newInternalRoom.setRooms(rooms_repo.findById(internalFacultyRoomAssignmentDto.getRoom_id()).get());
				
				listInternalFacultyRoomAssignment.add(newInternalRoom);
				
			});
			
		});
		
			
		
			return itt_repo.saveAll(listInternalFacultyRoomAssignment);
			
		}
	
	
	 public List<InternalFacultyRoomAssignment> listAllActiveInternalFacultyRoomAssignment() {
			List<InternalFacultyRoomAssignment> itts=itt_repo.findAll();
			return itts;
		}
	    
	 
	 public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer ac_year_id,
															   Integer school_id, Integer dept_id, Integer program_specialization_id, String internal_short_name) {
			Page<Object> InternalFacultyRoomAssignment_filtered_response = itt_repo.getAllDataFilteredByKeyword(pageable, keyword, ac_year_id,school_id,dept_id,program_specialization_id,internal_short_name);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, InternalFacultyRoomAssignment_filtered_response);
		}
		
	public ResponseEntity<Object> getAllSortedData(Pageable pageable1, Integer ac_year_id, Integer school_id,
												   Integer dept_id, Integer program_specialization_id, String internal_short_name) {

			Page<Object> InternalFacultyRoomAssignment_sorted = itt_repo.getAllSortedData(pageable1,
					ac_year_id,school_id,dept_id,program_specialization_id,internal_short_name);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, InternalFacultyRoomAssignment_sorted);
		}
	    
		
	public InternalFacultyRoomAssignment get(Integer id) {
			return itt_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("InternalFacultyRoomAssignment Not Found:" + id));
		}
		
	public InternalFacultyRoomAssignment saveInternalFacultyRoomAssignments(InternalFacultyRoomAssignmentDtoUpdate internalFacultyRoomAssignmentDtoUpdate, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		InternalFacultyRoomAssignment newInternalRoom = modelMapper.map(internalFacultyRoomAssignmentDtoUpdate, InternalFacultyRoomAssignment.class);
		
		newInternalRoom.setModified_by(jwtDetails.getUserId());
		newInternalRoom.setModified_username(jwtDetails.getUserName());

		newInternalRoom.setRooms(rooms_repo.findById(internalFacultyRoomAssignmentDtoUpdate.getRoom_id()).get());
		return itt_repo.save(newInternalRoom);
	}

	
	public void delete(Integer id) {
		InternalFacultyRoomAssignment itt = itt_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("InternalFacultyRoomAssignment Not Found:" + id));
		itt_repo.updateDept(id);
	}

	public void delete1(Integer id) {
		InternalFacultyRoomAssignment itt = itt_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("InternalFacultyRoomAssignment Not Found:" + id));
		itt_repo.updateDept1(id);
	}
	

	public List<Map<String, Object>> listAllActiveInternalFacultyRoomAssignmentData(Integer school_id,Integer program_id,Integer program_specialization_id,Integer ac_year_id,Integer year_sem) {
		return course_repository.listActivecourseDatas(school_id,program_id,program_specialization_id,ac_year_id,year_sem);
	}
	
	
	public List<Map<String, Object>> listAllActiveInternalFacultyRoomAssignmentDataBasisOfDOE(Integer internal_session_id) {
		return course_repository.listAllActiveInternalFacultyRoomAssignmentDataBasisOfDOE(internal_session_id);
	}


	public List<Map<String, Object>> getCoursesForInternals(Integer ac_year_id,Integer program_specialization_id,Integer year_sem) {
		
		return course_repository.getCoursesForInternals( ac_year_id,program_specialization_id,year_sem);
	}
	
	public Set<Map<String, Object>> getCoursesForInternalsOnSem(Integer school_id,Integer ac_year_id,Integer program_specialization_id, Integer current_sem) {

		List<Map<String, Object>> assignedCourse = course_repository.getCoursesForInternalsOnSem(school_id, ac_year_id, program_specialization_id, current_sem);
		List<Map<String, Object>> commonCourse = course_repository.getCommonCoursesForInternals(program_specialization_id, current_sem);

		List<Map<String, Object>> allCourses = new ArrayList<>();
		allCourses.addAll(assignedCourse);
		allCourses.addAll(commonCourse);
		Set<Map<String, Object>> response = allCourses.stream()
				.collect(Collectors.toMap(
						course -> (String) course.get("course_with_coursecode"),
						course -> course,
						(existing, replacement) -> existing
				))
				.values()
				.stream()
				.collect(Collectors.toSet());

		return response;
	}
	
	public Set<Map<String, Object>> getCoursesForInternalsOnyear(Integer school_id,Integer ac_year_id,Integer program_specialization_id, Integer current_year) {
		List<Map<String, Object>> assignedCourse= course_repository.getCoursesForInternalsOnyear( school_id,ac_year_id,program_specialization_id,current_year);
		List<Map<String, Object>> commonCourse=course_repository.getCommonCoursesForInternals(program_specialization_id,current_year);

		List<Map<String, Object>> allCourses = new ArrayList<>();
		allCourses.addAll(assignedCourse);
		allCourses.addAll(commonCourse);

		Set<Map<String, Object>> response = allCourses.stream()
				.collect(Collectors.toMap(
						course -> (String) course.get("course_with_coursecode"),
						course -> course,
						(existing, replacement) -> existing
				))
				.values()
				.stream()
				.collect(Collectors.toSet());

		return response;
	}
	
public List<Map<String, Object>> getUnoccupiedEmployeesForInternals(Integer time_slots_id, String date) {
		
		List<Integer> emp_ids_from_internal_time_table = itt_repo.getEmplIdsFromInternalTimeTable(time_slots_id,date);
		System.out.println("(((((((((((((((((((---1----)))))))))))))))))))))))))) " + emp_ids_from_internal_time_table);
		
		List<Integer> emp_ids_from_time_table_employee = tter_ser.getEmplIdsFromTimeTableEmployeeForInternals(date,time_slots_id);
		System.out.println("(((((((((((((((((((---2----)))))))))))))))))))))))))) " + emp_ids_from_time_table_employee);
		
		List<Integer> employees_on_leave = leaveapplyrepo.getEmplIdsOnLeave(date);
		System.out.println("(((((((((((((((((((-------)))))))))))))))))))))))))) " + employees_on_leave);
		
		List<Integer> unavailable_employees_list = new ArrayList<Integer>();
		unavailable_employees_list.addAll(emp_ids_from_internal_time_table);
		unavailable_employees_list.addAll(emp_ids_from_time_table_employee);
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


public List<Map<String, Object>> getRoomsForInternals() {
	List<Map<String, Object>> rooms = rooms_repo.getRoomsForInternals();
	return rooms;
}


public void saveInternalFacultyRoomAssignments(List<Integer> internal_room_assignment_ids, Integer new_emp_id,String jwtToken) throws JsonParseException, JsonMappingException, IOException {
	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	List<InternalFacultyRoomAssignment> allInternalFacultyRoomAssignment = itt_repo.findAllById(internal_room_assignment_ids);
	
	allInternalFacultyRoomAssignment.stream().forEach(itt -> {
		InternalFacultyRoomAssignment newInternalRoom = modelMapper.map(itt, InternalFacultyRoomAssignment.class);		
		
		newInternalRoom.setModified_by(jwtDetails.getUserId());
		newInternalRoom.setModified_username(jwtDetails.getUserName());
		newInternalRoom.setEmp_ids(new_emp_id);
		
		itt_repo.save(newInternalRoom);
		
	});
	
}

public void saveInternalFacultyRoomAssignments1(List<Integer> internal_room_assignment_ids, Integer new_room_id,String jwtToken) throws JsonParseException, JsonMappingException, IOException {
	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	List<InternalFacultyRoomAssignment> allInternalFacultyRoomAssignment = itt_repo.findAllById(internal_room_assignment_ids);
	
	allInternalFacultyRoomAssignment.stream().forEach(itt -> {
		InternalFacultyRoomAssignment newInternalRoom = modelMapper.map(itt, InternalFacultyRoomAssignment.class);		
		
		newInternalRoom.setModified_by(jwtDetails.getUserId());
		newInternalRoom.setModified_username(jwtDetails.getUserName());
		newInternalRoom.setRooms(rooms_repo.findById(new_room_id).get());
		
		itt_repo.save(newInternalRoom);
		
	});
	
}
	
//	public List<Map<String, Object>> listOfStudentDetails(Integer internal_timetable_assignment_id) {
//		String studentIds = itt_repo.getStudentIds(internal_timetable_assignment_id);
//		List<Integer> list_students = ResponseHandler.toConvertCommaSeperatedIdsAsList(studentIds);
//		return itt_repo.listOfStudentDetails(list_students);
//	}
//	
//	
//	public List<Map<String, Object>> internalTimeTableAssignmentDetailsByUserId(Integer userId) {
//		UserAuthentication userDetails =userAuthenticationRepository.findById(userId)
//				.orElseThrow(() -> new ResourceNotFoundException("User Not Found: " + userId));
//		Integer employeeId=empDetail_repo.getEmpId(userDetails.getEmail());
//		return itt_repo.internalTimeTableAssignmentDetailsByEmployeeId(employeeId);
//	}
//	
	public List<Map<String, Object>> internalTimeTableAssignmentDetailsByEmployeeId(Integer employeeId) {
		return itt_repo.internalTimeTableAssignmentDetailsByEmployeeId(employeeId);
	}
	
	public List<Map<String, Object>> internalTimeTableAssignmentDetailsByInternalSessionId(Integer internal_session_id) {
		 List<Map<String, Object>> internalTimeTableAssignmentDetailsByInternalSessionId = itt_repo.internalTimeTableAssignmentDetailsByInternalSessionId(internal_session_id);
		 System.out.println("AAAAAAAAAAAAAAAAAAAA "+internalTimeTableAssignmentDetailsByInternalSessionId.size());
		 return internalTimeTableAssignmentDetailsByInternalSessionId;
	}

	public String getStudentIdsByInternalSessionId(Integer internal_session_id) {
		return itt_repo.getStudentIdsByInternalSessionId(internal_session_id);
	}


	public List<Map<String, Object>> internalTimeTableAssignmentDetailsByLecturerEmployeeId(Integer emp_id) {
		
		return itt_repo.internalTimeTableAssignmentDetailsByEmployeeId(emp_id);
	}
	
	 public ResponseEntity<Object> getAllDataFilteredByKeyword1(Pageable pageable, Object keyword, Integer user_id) {
		 Page<Map<String, Object>> InternalFacultyRoomAssignment_filtered_response = itt_repo.getAllDataFilteredByKeyword1(pageable, keyword, user_id);
			return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, InternalFacultyRoomAssignment_filtered_response);
		}
		
	public ResponseEntity<Object> getAllSortedData1(Pageable pageable, Integer user_id) {

		Page<Map<String, Object>> InternalFacultyRoomAssignment_sorted = itt_repo.getAllSortedData1(pageable, user_id);
			return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, InternalFacultyRoomAssignment_sorted);
		}
	
//	public Map<String, Object> checkInternalExamAttendanceStatus(Integer internalTimetableAssignmentId) {
//		return itt_repo.checkInternalExamAttendanceStatus(internalTimetableAssignmentId);
//	}
//	
//	
//	
//	
//	public List<Map<String, Object>> internalStudentDetailsData(Integer time_slots_id, Integer room_id, String selected_date) {
//	
//	List<String> studentIds = 	itt_repo.getAllStudentIds(time_slots_id,room_id,selected_date);
//	
//	List<Map<String, Object>> stu = new ArrayList<Map<String, Object>>();
//	Set<Integer> stud = new TreeSet<Integer>();
//	
//	List<String> values = new ArrayList<String>();
//	  for(String data: studentIds) {
//		  if(data != null && !(data.isEmpty())) { 
//	            values.add(data);
//	         }
//	  }
//	  String s = ",";
//		values.stream().forEach(l -> {
//			if(l.contains(s) ) {
//				List<Integer> new_list = ResponseHandler.toConvertCommaSeperatedIdsAsList(l);
//				stud.addAll(new_list);
//			} else {
//	
//				stud.add(Integer.parseInt(l));
//			}
//		});
//		
//		stud.stream().forEach(st ->{
//			Map<String, Object> studentData =	stu_repo.getStudentDetailsData(st);
//			stu.add(studentData);
//		});
//		return stu;
//	}



}
