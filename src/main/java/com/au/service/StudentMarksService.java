package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.ExternalMarksAssignmentDto;
import com.au.dto.JwtDetails;
import com.au.dto.StudentMarksAssignmentDto;
import com.au.dto.StudentMarksDto;
import com.au.dto.StudentMarksLockDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.StudentMarks;
import com.au.repository.CourseAssignmentRepository;
import com.au.repository.CourseStudentAssignmentRepository;
import com.au.repository.InternalSessionAssignmentRepository;
import com.au.repository.InternalTimeTableAssignmentRepository;
import com.au.repository.StudentMarksRepository;
import com.au.response.ResponseHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Service
public class StudentMarksService {

	@Autowired
	private StudentMarksRepository student_marks_repo;
	
	@Autowired
	private CourseAssignmentRepository courseAssignmentRepo;
	
	
	@Autowired
	private InternalTimeTableAssignmentRepository internalTimeTableAssignmentRepo;
	
	@Autowired
	private InternalSessionAssignmentRepository internalSessionAssignmentRepo;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private CourseStudentAssignmentRepository courseStudentAssignmentRepository;
	
	
	public List<StudentMarks> saveStudentMark(List<StudentMarks> stm) throws Exception {
		
		stm.stream().forEach(sm ->{
		if (student_marks_repo.getCount1(sm.getAc_year_id(),sm.getCourse_assignment_id(),sm.getInternal_id(),
				sm.getStudent_id()) >= 1) {
			
			throw new RuntimeException("The combination of academic,course,internal and student is Already Present !!!");
		}
		if(sm.getMarks_obtained_internal() < 0) {
			throw new RuntimeException("Marks should not be less then Zero !!!");
		}
		 student_marks_repo.save(sm);
		});
		return stm;
	}

	
	Integer count1 = 0;
	public List<StudentMarks> createExternalStudentMark(@Valid StudentMarksDto studentMarksDto, String jwtToken) throws Exception {
	    JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    List<StudentMarks> studentMarksList = new ArrayList<>();
	    
	    for (StudentMarksAssignmentDto studentMarksAssignment : studentMarksDto.getStudentMarksAssignment()) {
	        // Fetch student marks ID for each studentMarksAssignment
	        List<Integer> studentMarksId = student_marks_repo.getStudentMarksId(
	            studentMarksDto.getAc_year_id(),
	            studentMarksDto.getCourse_assignment_id(),
	            studentMarksAssignment.getStudent_id()
	        );
	        
	        if (ObjectUtils.isNotEmpty(studentMarksId)) {
	            // If the student marks already exist, update them
	            studentMarksId.stream().forEach(bId -> {
	                StudentMarks studentMarks1 = student_marks_repo.findById(bId).orElseThrow(() ->
	                    new ResourceNotFoundException("StudentMarks Details Not Found for ID: " + bId)
	                );
	                
	                // Update existing marks
	                studentMarks1.setStudent_id(studentMarksAssignment.getStudent_id());
	                studentMarks1.setMarks_obtained_external(studentMarksAssignment.getMarks_obtained_external());
	                studentMarks1.setPercentage(studentMarksAssignment.getPercentage());
	                studentMarks1.setActive(studentMarksDto.getActive());
	                studentMarks1.setModified_by(jwtDetails.getUserId());
	                studentMarks1.setModified_date(studentMarksDto.getModified_date());
	                studentMarks1.setModified_username(jwtDetails.getUserName());
	                
	                studentMarksList.add(studentMarks1);  // Add to the list for batch update
	            });
	        } else {
	            // If no existing marks found, create new entries
	            StudentMarks studentMarks = new StudentMarks();
	            studentMarks.setAc_year_id(studentMarksDto.getAc_year_id());
	            studentMarks.setCourse_assignment_id(studentMarksDto.getCourse_assignment_id());
	            studentMarks.setSchool_id(studentMarksDto.getSchool_id());
	            studentMarks.setCourse_id(studentMarksDto.getCourse_id());
	            studentMarks.setCurrent_year_sem(studentMarksDto.getCurrent_year_sem());
	            studentMarks.setExam_date(studentMarksDto.getExam_date());
	            studentMarks.setExternal_max_mark(studentMarksDto.getExternal_max_mark());
	            studentMarks.setExternal_min_mark(studentMarksDto.getExternal_min_mark());
	            studentMarks.setInternal_id(studentMarksDto.getInternal_id());
	            studentMarks.setInternal_session_id(studentMarksDto.getInternal_session_id());
	            studentMarks.setActive(studentMarksDto.getActive());
	            studentMarks.setProgram_specialization_id(studentMarksDto.getProgram_specialization_id());
	            studentMarks.setMarks_obtained_external(studentMarksAssignment.getMarks_obtained_external());
	            studentMarks.setStudent_id(studentMarksAssignment.getStudent_id());
	            studentMarks.setPercentage(studentMarksAssignment.getPercentage());
	            studentMarks.setCurrent_sem(studentMarksDto.getCurrent_sem()); 
	            studentMarks.setCurrent_year(studentMarksDto.getCurrent_year());
	            studentMarks.setCreated_by(studentMarksDto.getCreated_by());
	            studentMarks.setCreated_date(studentMarksDto.getCreated_date());
	            studentMarks.setCreated_username(studentMarksDto.getCreated_username());
	            
	            studentMarks.setFaculty_status(studentMarksDto.getFaculty_status());
	            studentMarks.setFaculty_status_date(studentMarksDto.getFaculty_status_date());
	            studentMarks.setHod_status(studentMarksDto.getHod_status());
	            studentMarks.setHod_status_date(studentMarksDto.getHod_status_date());
	            studentMarks.setHoi_status(studentMarksDto.getHoi_status());
	            studentMarks.setHoi_status_date(studentMarksDto.getHoi_status_date());

	            studentMarksList.add(studentMarks);  // Add to the list for batch insert
	        }
	    }

	    // Save all the updated or newly created student marks
	    student_marks_repo.saveAll(studentMarksList);
	    count1 = 0;  // Reset the counter
	    return studentMarksList;
	}
	
	
	public List<StudentMarks> createExternalMarksAssignment(@Valid ExternalMarksAssignmentDto marksDto,JwtDetails jwtDetails) {
		List<StudentMarks> listOfExternalMarksAssignment=new ArrayList<StudentMarks>();
	
	List<Integer> studentId	=courseStudentAssignmentRepository.getStudentIdz(marksDto.getCourse_assignment_id());
	studentId.stream().forEach(sId ->{
		Integer count = student_marks_repo.getCountForstudentMark(
				marksDto.getAc_year_id(),
				marksDto.getCourse_assignment_id(),sId);
		if (count >= 1) {
            throw new RuntimeException("The combination of academic,course,internal and student is Already Present !!!");
           }
		
		StudentMarks external_marks_assignment=new StudentMarks();
		external_marks_assignment.setStudent_id(sId);
		external_marks_assignment.setMarks_obtained_external(marksDto.getMarks_obtained_external());
		external_marks_assignment.setPercentage(marksDto.getPercentage());
		external_marks_assignment.setActive(marksDto.getActive());
		external_marks_assignment.setCreated_by(jwtDetails.getUserId());
		external_marks_assignment.setCreated_username(jwtDetails.getUserName());
		
		student_marks_repo.save(external_marks_assignment);
		listOfExternalMarksAssignment.add(external_marks_assignment);
	});
		
		return	listOfExternalMarksAssignment;
	}
	
	
	public List<StudentMarks> listAll() {
		return student_marks_repo.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = student_marks_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = student_marks_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	
	public ResponseEntity<Object> fetchAllExternalStudentMarksDetaillistAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = student_marks_repo.fetchAllExternalStudentMarksDetaillistAll1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllExternalStudentMarksDetaillistAll2(Pageable pageable) {
		Page<Object> response = student_marks_repo.fetchAllExternalStudentMarksDetaillistAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public StudentMarks get(Integer id) {
		return student_marks_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student Marks Not Found:" + id));
	}
	
	public StudentMarks updateStudentMarks(StudentMarks stm) {
		if(student_marks_repo.getCount(stm.getStudent_id(),stm.getCourse_id(),stm.getInternal_id()) >= 1) {
			throw new RuntimeException("Marks Already Present for the given student_id,course_id and internal_id");
		}
		else {
			return student_marks_repo.save(stm);
		}
		
	}
	
	public void delete(Integer id) {
		student_marks_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student Marks Not Found:" + id));
		student_marks_repo.update(id);
	}
	
	public void delete1(Integer id) {
		student_marks_repo.update1(id);

	}
	
	
	public List<Map<String, Object>> coursecodeConcateWithName(Integer ac_year_id, Integer year_sem) {
		return courseAssignmentRepo.coursecodeConcateWithName(ac_year_id,year_sem);
	}
	
	public List<Map<String, Object>> getCourseAssignmentDetails(Integer ac_year_id, Integer program_assignment_id,
			Integer year_sem) {
		return courseAssignmentRepo.getCourseAssignmentDetails(ac_year_id,program_assignment_id,year_sem);
	}
	
	public List<Map<String, Object>> getInternalDetailsData(Integer ac_year_id, Integer internal_id, Integer year_sem,
			Integer program_specialization_id, Integer course_assignment_id) {
		
		List<String> studentIds = internalTimeTableAssignmentRepo.getStudentIds(ac_year_id,internal_id,year_sem,program_specialization_id,course_assignment_id);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +studentIds);
		Set<Integer> std = new TreeSet<Integer>();

		List<Map<String, Object>> mp2 = new ArrayList<Map<String, Object>>();
		List<String> values = new ArrayList<String>();
	      for(String data: studentIds) {
	    	  if(data != null && !(data.isEmpty())) { 
		            values.add(data);
		         }
	      }
	      System.out.println("FFFILLLTTTRERRERDDDD " +values);
		String s = ",";
		values.stream().forEach(l -> {
			if(l.contains(s) ) {
				List<Integer> new_list = ResponseHandler.toConvertCommaSeperatedIdsAsList(l);
				System.out.println("##############2222222222222222########" +l );
				std.addAll(new_list);
			} else {
	
				std.add(Integer.parseInt(l));
				System.out.println("######################" +l );
			}
		});
		
		System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" +std );
		std.stream().forEach(st ->{
			System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::: "+st);
			HashMap<String, Object> mp = new HashMap<String, Object>();
			Map<Object, Object> data1=	internalSessionAssignmentRepo.getStudentDetailsData(st);
			System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{_______________________ "+data1.get("student_id"));
			Map<Object, Object> data2=	internalSessionAssignmentRepo.getInternalDetailsData(ac_year_id,internal_id,year_sem,program_specialization_id,course_assignment_id);
			System.out.println("***************************%%%%%%%%%%%%%%%%%%% "+data2.get("max_marks"));
			Map<Object, Object> data3=	student_marks_repo.getMarksObtain(internal_id,year_sem,course_assignment_id,st);
			mp.put("student_id", data1.get("student_id"));
			mp.put("auid", data1.get("auid"));
			mp.put("student_name", data1.get("student_name"));
			mp.put("marks_obtained_internal", data3.get("marks_obtained_internal"));
//			mp.put("total_marks_internal", data2.get("total_marks_internal"));
//			mp.put("section_id", data2.get("section_id"));
			mp.put("max_marks", data2.get("max_marks"));
			mp.put("min_marks", data2.get("min_marks"));

			mp2.add(mp);
			
		});
		
		return mp2;

	}

	public List<Map<String, Object>> getStudentMarkDetails(Integer student_id, Integer internal_session_id) {
		return student_marks_repo.getStudentMarkDetails(student_id,internal_session_id);
	}
	
	public List<StudentMarks> getStudentMarkDetailsByInternalSessionId(Integer internal_session_id) {
		return student_marks_repo.getStudentMarkDetailsByInternalSessionId(internal_session_id);
	}

	public List<Map<String, Object>> getStudentMarkDetailsBasedOnProctor(Integer id) {
		return student_marks_repo.getStudentMarkDetailsBasedOnProctor(id);
	}

	public List<Map<String, Object>> getAllActiveInternal(Integer student_id) {
		return student_marks_repo.getAllActiveInternal(student_id);
		
	}	
	

	/* ---------------------------  API FOR MOBILE APP ------------------------ */	
	
	public List<Map<String, Object>> getScorecardData(Integer student_id, Integer current_year_sem) {
		
		List<Map<String, Object>> score_card_data = student_marks_repo.getScorecardData(student_id, current_year_sem);
		return score_card_data;
	}
	
	public Map<String, Object> getGraphicalScorecardData(Integer current_year_sem, Integer total_marks_internal,
			Integer internal_id, Integer course_id) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		Integer value1 = total_marks_internal/4;
		Integer value2 = value1*2;
		Integer value3 = value1*3;
		
		Integer total_numbers_of_students = student_marks_repo.getTotalNumberOfStudents(current_year_sem,course_id, internal_id);
		Integer numbers_of_students_1 = student_marks_repo.getGraphicalScorecardData1(current_year_sem, internal_id, course_id,value1);
		Integer numbers_of_students_2 = student_marks_repo.getGraphicalScorecardData2(current_year_sem, internal_id, course_id,value1,value2);
		Integer numbers_of_students_3 = student_marks_repo.getGraphicalScorecardData3(current_year_sem, internal_id, course_id,value2,value3);
		Integer numbers_of_students_4 = student_marks_repo.getGraphicalScorecardData4(current_year_sem, internal_id, course_id,value3,total_marks_internal);
		
//		data.put("Total_Students", total_numbers_of_students);
//		data.put(0+"-"+value1, numbers_of_students_1);
//		data.put(value1+"-"+value2, numbers_of_students_2);
//		data.put(value2+"-"+value3, numbers_of_students_3);
//		data.put(value3+"-"+total_marks_internal, numbers_of_students_4);
		
		data.put("Total_Students", 100);
		data.put(0+"-"+value1, 10);
		data.put(value1+"-"+value2, 25);
		data.put(value2+"-"+value3, 50);
		data.put(value3+"-"+total_marks_internal, 15);
	
		
		return data;
	}
	
	public List<Map<String, Object>> getScorecardDataOnInternal(Integer student_id, Integer current_year_sem,Integer internal_id) {
		
		List<Map<String, Object>> score_card_data_on_internal = student_marks_repo.getScorecardDataOnInternal(student_id, current_year_sem,internal_id);
		return score_card_data_on_internal;
	}
	
	public List<Map<String, Object>> getStudentMarksWithFilteredData(Integer ac_year_id, Integer school_id,
			Integer dept_id, Integer program_specialization_id, String internal_short_name) {
	return student_marks_repo.getStudentMarksWithFilteredData(ac_year_id,school_id,dept_id,program_specialization_id,internal_short_name);
	}


	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer ac_year_id,
			Integer school_id, Integer dept_id, Integer program_specialization_id, String internal_short_name) {
		Page<Map<String, Object>> Notifications_filtered_response = student_marks_repo
				.getAllDataFilteredByKeyword(pageable, keyword, ac_year_id,school_id,dept_id,program_specialization_id,internal_short_name);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_filtered_response);
	}


	public ResponseEntity<Object> getAllSortedData(Pageable pageable1, Integer ac_year_id, Integer school_id,
			Integer dept_id, Integer program_specialization_id, String internal_short_name) {
		Page<Map<String, Object>> Notifications_sorted_response = student_marks_repo.getAllSortedData(pageable1,
				ac_year_id,school_id,dept_id,program_specialization_id,internal_short_name);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_sorted_response);
	}


	public List<Map<String, Object>> fetchStudentInternalsReportWithFilteredData(Integer student_id, Integer current_sem,
													Integer current_year, Integer course_assignment_id, String internal_short_name) {
		List<Map<String, Object>> sorted_response = student_marks_repo.fetchStudentInternalsReportWithFilteredData(student_id,current_sem,current_year, course_assignment_id, internal_short_name);
		return sorted_response;
	}
	
//	public List<Map<String, Object>> fetchFromStudentMarksAndAttendanceWithFilteredData(Integer ac_year_id, Integer current_sem, Integer current_year, Integer school_id,
//			 Integer dept_id, Integer program_specialization_id, String internal_short_name) {
//
//List<Map<String, Object>> sorted_response = student_marks_repo.fetchFromStudentMarksAndAttendanceWithFilteredData(ac_year_id, current_sem, current_year, school_id,dept_id, program_specialization_id, internal_short_name);
//return sorted_response;
//
//}


	public ResponseEntity<Object> getStudentMarksAndAttendanceWithFilteredDataKeyword(Pageable pageable, Object keyword,
			Integer ac_year_id, Integer current_sem, Integer current_year, Integer school_id, Integer dept_id,
			Integer program_specialization_id, String internal_short_name) {
		Page<Map<String, Object>> Notifications_filtered_response = student_marks_repo
				.getStudentMarksAndAttendanceWithFilteredDataKeyword(pageable, keyword,ac_year_id,current_sem,current_year,school_id,
						dept_id,program_specialization_id,internal_short_name);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_filtered_response);
	}


	public ResponseEntity<Object> getStudentMarksAndAttendanceWithFilteredDataData(Pageable pageable1,
			Integer ac_year_id, Integer current_sem, Integer current_year, Integer school_id, Integer dept_id,
			Integer program_specialization_id, String internal_short_name) {
		Page<Map<String, Object>> Notifications_filtered_response = student_marks_repo
				.getStudentMarksAndAttendanceWithFilteredDataData(pageable1,ac_year_id,current_sem,current_year,school_id,
						dept_id,program_specialization_id,internal_short_name);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_filtered_response);
	}


	public ResponseEntity<Object> fetchStudentMarksDataWithKeyword(Pageable pageable, Object keyword,
			Integer ac_year_id, Integer current_sem, Integer current_year, Integer school_id,
			Integer program_specialization_id, Integer course_assignment_id) {
		Page<Object> response1 = student_marks_repo.fetchStudentMarksDataWithKeyword(pageable, keyword,ac_year_id,current_sem,current_year,school_id,
				program_specialization_id,course_assignment_id);;
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}


	public ResponseEntity<Object> fetchStudentMarksDataWithoutKeyword(Pageable pageable1, Integer ac_year_id,
			Integer current_sem, Integer current_year, Integer school_id, Integer program_specialization_id,
			Integer course_assignment_id) {
		Page<Object> response = student_marks_repo.fetchStudentMarksDataWithoutKeyword(pageable1,ac_year_id,current_sem,current_year,school_id,
				program_specialization_id,course_assignment_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}


	public void updateStudentMarksLock(StudentMarksLockDto dto, List<Integer> marksIds, String jwtToken) {
	    for (Integer marksId : marksIds) {
	        StudentMarks studentMarks = student_marks_repo.findById(marksId)
	                .orElseThrow(() -> new NoSuchElementException("Student marks not found for ID: " + marksId));

	        // Conditionally update fields based on dto values

	        // Update faculty_status, faculty_status_date, and faculty_id only if they are not null
	        if (dto.getFaculty_status() != null) {
	            studentMarks.setFaculty_status(dto.getFaculty_status());
	        }
	        if (dto.getFaculty_status_date() != null) {
	            studentMarks.setFaculty_status_date(dto.getFaculty_status_date());
	        }
	        if (dto.getFaculty_id() != null) {
	            studentMarks.setFaculty_id(dto.getFaculty_id());
	        }

	        // Update hod_status, hod_status_date, and hod_id only if they are not null
	        if (dto.getHod_status() != null) {
	            studentMarks.setHod_status(dto.getHod_status());
	        }
	        if (dto.getHod_status_date() != null) {
	            studentMarks.setHod_status_date(dto.getHod_status_date());
	        }
	        if (dto.getHod_id() != null) {
	            studentMarks.setHod_id(dto.getHod_id());
	        }

	        // Update hoi_status, hoi_status_date, and hoi_id only if they are not null
	        if (dto.getHoi_status() != null) {
	            studentMarks.setHoi_status(dto.getHoi_status());
	        }
	        if (dto.getHoi_status_date() != null) {
	            studentMarks.setHoi_status_date(dto.getHoi_status_date());
	        }
	        if (dto.getHoi_id() != null) {
	            studentMarks.setHoi_id(dto.getHoi_id());
	        }

	        // Save updated studentMarks object
	        student_marks_repo.save(studentMarks);
	    }
	}



}
