package com.au.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;import com.au.dto.CourseStudentAssignmentDto;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseStudentAssignment;
import com.au.model.ReportingStudents;
import com.au.model.Student_Details;
import com.au.repository.Academic_year_repository;
import com.au.repository.CourseAssignmentRepository;
import com.au.repository.CourseRepository;
import com.au.repository.CourseStudentAssignmentRepository;
import com.au.repository.InternalSessionCreationRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class CourseStudentAssignmentService {
	
	@Autowired
	private CourseStudentAssignmentRepository cors_stu_assign_repo;
	
	@Autowired
	private StudentDetailsRepository student_details_repo;
	
	@Autowired
	private ReportingStudentsRepository reporting_student_repo;
	
	@Autowired
	private StudentDetailsService student_details_service;
	
	@Autowired
	private CourseAssignmentRepository course_assignment_repo;
	
	@Autowired
	private CourseRepository course_repository;
	
	@Autowired
	private InternalSessionCreationRepository isa_repo;
	
	
	public List<CourseStudentAssignment> saveCourseStudentAssignment(CourseStudentAssignmentDto cors_stu_dto,JwtDetails jwtDetails){
		List<CourseStudentAssignment> list_cors_stu_assign=new ArrayList<CourseStudentAssignment>();
		if(cors_stu_assign_repo.getCountofCourseStudentAssignment(cors_stu_dto.getCourse_assignment_id(),cors_stu_dto.getStudent_id())>=1) {
			throw new RuntimeException("Course Already Assigned To Students");
		}else {
			
			cors_stu_dto.getStudent_id().stream().forEach(stu -> {
				CourseStudentAssignment cors_stu_assign=new CourseStudentAssignment();
				cors_stu_assign.setCourse_id(cors_stu_dto.getCourse_id());
				cors_stu_assign.setStudent_id(stu);
				cors_stu_assign.setCurrent_year_sem(cors_stu_dto.getCurrent_year_sem());
				cors_stu_assign.setCreated_by(jwtDetails.getUserId());
				cors_stu_assign.setCreated_username(jwtDetails.getUserName());
				cors_stu_assign.setActive(cors_stu_dto.getActive());
				cors_stu_assign.setCourse_assignment_id(cors_stu_dto.getCourse_assignment_id());
				
				cors_stu_assign_repo.save(cors_stu_assign);
				list_cors_stu_assign.add(cors_stu_assign);
			});
		}
		return list_cors_stu_assign;
	}
	
	public List<CourseStudentAssignment> listAll() {	
		return cors_stu_assign_repo.findAll1();
	}
	
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = cors_stu_assign_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = cors_stu_assign_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public CourseStudentAssignment get(Integer id) {
		return cors_stu_assign_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Student Assignment Not Found:" + id));
	}
	
	public CourseStudentAssignment updateCourseStudentAssignment(CourseStudentAssignment course_stu_assign) {
		return cors_stu_assign_repo.save(course_stu_assign);
	}
	
	public void delete(List<Integer> ids) {
		cors_stu_assign_repo.updateDeactivate(ids);
	}

	public void delete1(Integer id) {
		cors_stu_assign_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Not Found:" + id));
		cors_stu_assign_repo.updatActivate(id);
	}
	
	public HashMap<String, Object> getStudentDetailsForCourseAssignment(Integer course_assignment_id,Integer ac_year_id,Integer program_specialization_id,
			Integer current_year,Integer current_sem) {
		HashMap<String, Object> response = new HashMap<>();
		List<Integer> student_ids=cors_stu_assign_repo.assignedCourseStudentId(course_assignment_id);
		List<HashMap<String, Object>> course_assigned_student_details=cors_stu_assign_repo.getAssignedCourseStudentDetails(student_ids);
		if(current_sem != null) {
			if(student_ids.size() == 0) {
				List<HashMap<String, Object>> course_unassigned_student_details_on_sem=cors_stu_assign_repo.getUnassignedCourseStudentDetailsOnSem(ac_year_id,program_specialization_id,current_sem);
				response.put("course_unassigned_student_details_on_sem",course_unassigned_student_details_on_sem);
				return response;
			}else {
				List<HashMap<String, Object>> course_unassigned_student_details_on_sem=cors_stu_assign_repo.getUnassignedCourseStudentDetailsOnSem(student_ids,ac_year_id,program_specialization_id,current_sem);
				response.put("course_assigned_student_details",course_assigned_student_details);
				response.put("course_unassigned_student_details_on_sem",course_unassigned_student_details_on_sem);
				return response;
			}
		}else {
			if(student_ids.size() == 0) {
				List<HashMap<String, Object>> course_unassigned_student_details_on_year=cors_stu_assign_repo.getUnassignedCourseStudentDetailsOnYear(ac_year_id,program_specialization_id,current_year);
				response.put("course_unassigned_student_details_on_year",course_unassigned_student_details_on_year);
				return response;
			} else {
				List<HashMap<String, Object>> course_unassigned_student_details_on_year=cors_stu_assign_repo.getUnassignedCourseStudentDetailsOnYear(student_ids,ac_year_id,program_specialization_id,current_year);
				response.put("course_assigned_student_details",course_assigned_student_details);
				response.put("course_unassigned_student_details_on_year",course_unassigned_student_details_on_year);
				return response;
			}
		}
	}
	
	public List<HashMap<String,Object>> courseStudentAssignmentIdsOnStudentIds(List<Integer> student_ids){
		return cors_stu_assign_repo.courseStudentAssignmentIdsOnStudentIds(student_ids);
	}
	
	public Set<HashMap<String, Object>> coursesAssignedToStudent(Integer student_id){
		String program_type=student_details_repo.getProgramTypeOfStudent(student_id);
//		Student_Details student_details=student_details_service.get(student_id);
//		ReportingStudents reporting_student=reporting_student_repo.getDetailsOfReportingStudentsByStudentId(student_id);
//		List<HashMap<String,Object>> mandatory_course_of_specialization;
		Set<HashMap<String,Object>> all_courses_of_student=new  HashSet<>();
		if(program_type.equalsIgnoreCase("%Semester%")) {
//			mandatory_course_of_specialization =course_assignment_repo.mandatoryCourseAssignedToSpecialization(student_details.getAc_year_id(),student_details.getProgram_specialization_id(),reporting_student.getCurrent_sem());
//			mandatory_course_of_specialization.stream().forEach(mcs ->{
//				mcs.put("program_type", program_type);
//				all_courses_of_student.add(mcs);
//			});
			List<HashMap<String,Object>> other_courses_of_student=cors_stu_assign_repo.getOtherCourseOFStudent(student_id);
			other_courses_of_student.stream().forEach(ocs ->{
				ocs.put("program_type", program_type);
				all_courses_of_student.add(ocs);
			});
		}else {
//			mandatory_course_of_specialization =course_assignment_repo.mandatoryCourseAssignedToSpecialization(student_details.getAc_year_id(),student_details.getProgram_specialization_id(),reporting_student.getCurrent_year());
//			mandatory_course_of_specialization.stream().forEach(mcs ->{
//				mcs.put("program_type", program_type);
//				all_courses_of_student.add(mcs);
//			});
			List<HashMap<String,Object>> other_courses_of_student=cors_stu_assign_repo.getOtherCourseOFStudent(student_id);
			other_courses_of_student.stream().forEach(ocs ->{
				ocs.put("program_type", program_type);
				all_courses_of_student.add(ocs);
			});
		}
		
		return all_courses_of_student;
	}

	public List<Map<String,Object>> courseDetailsForStudentsAssignment( Integer program_specialization_id,
			 Integer year_sem, Integer school_id){
		List<Map<String,Object>> assignedCourses= course_assignment_repo.courseDetailsForStudentsAssignment(program_specialization_id,
				  year_sem, school_id);
		List<Integer> commonCourseIds=course_repository.commonCourseIds();
		List<Map<String,Object>> commonCourses=course_assignment_repo.commonCourseDetailsForStudentsAssignment(year_sem,school_id,commonCourseIds);
		commonCourses.parallelStream().forEach(cmc -> {
			assignedCourses.add(cmc);
		});
		return assignedCourses;
	}
	
	public List<CourseStudentAssignment> assignMultipleCourseToStudent(@Valid CourseStudentAssignmentDto cors_stu_dto,
			JwtDetails jwtDetails) {
	
		List<CourseStudentAssignment> list_stu_cour_assign=new ArrayList<CourseStudentAssignment>();
		
		
		if(cors_stu_assign_repo.getCountofCourseStudentAssignment1(cors_stu_dto.getCourse_assignment_ids(), cors_stu_dto.getStud_id())>=1) {
			throw new RuntimeException("Courses Already Assigned To Student");
		}else {
			
			cors_stu_dto.getCourse_assignment_ids().stream().forEach(cou -> {
				CourseStudentAssignment stu_cour_assign=new CourseStudentAssignment();
				stu_cour_assign.setStudent_id(cors_stu_dto.getStud_id());
				stu_cour_assign.setCourse_assignment_id(cou);
				stu_cour_assign.setCreated_by(jwtDetails.getUserId());
				stu_cour_assign.setCreated_username(jwtDetails.getUserName());
				stu_cour_assign.setActive(cors_stu_dto.getActive());
				cors_stu_assign_repo.save(stu_cour_assign);
				list_stu_cour_assign.add(stu_cour_assign);
			});
		}
		return list_stu_cour_assign;
	}

	public List<Integer> getcoursesAssignedToStudent(Integer student_id){
		return cors_stu_assign_repo.getcoursesAssignedToStudent(student_id);
	}
	
//	public List<Map<String, Object>> getStudentDetailData(Integer course_assignment_id, Integer internal_session_id, Integer ac_year_id) {
//	    List<Map<String, Object>> studentDetails = new ArrayList<>(); 
//	    Set<Map<String, Object>> uniqueStudentDetails = new HashSet<>();
//	    List<Integer> studentId = cors_stu_assign_repo.getStudentIdz(course_assignment_id);
//	    Map<String, Object> studentMarks = isa_repo.getStudentMarks(internal_session_id,course_assignment_id);
//	   
//	   
//	    studentId.stream().forEach(sId -> {
//	        List<Map<String, Object>> details = student_details_repo.getStudentDetailsBasedOnStudentId(sId);
//	        uniqueStudentDetails.addAll(details); // This will automatically ensure uniqueness
//	        uniqueStudentDetails.add(studentMarks);
//	       });
//
//	    studentDetails.addAll(uniqueStudentDetails);
//	    return studentDetails;
//	}
	
	
	public List<Map<String, Object>> getStudentDetailData(Integer course_assignment_id, Integer internal_session_id, Integer ac_year_id) {
	    List<Map<String, Object>> studentDetails = new ArrayList<>(); 
	    Set<Map<String, Object>> uniqueStudentDetails = new HashSet<>();
	    
	    // Get student IDs for the course assignment
	    List<Integer> studentIds = cors_stu_assign_repo.getStudentIdz(course_assignment_id);
	    
	    // Fetch the external marks (like external_max_marks, external_min_marks, etc.) from the session
	    Map<String, Object> studentMarks = isa_repo.getStudentMarks(internal_session_id, course_assignment_id);

	    // Extract the external_max_marks and external_min_marks to apply to all students
	    Integer externalMaxMarks = (Integer) studentMarks.get("external_max_marks");
	    Integer externalMinMarks = (Integer) studentMarks.get("external_min_marks");

	    // Iterate through the student IDs and merge the details with the marks
	    studentIds.forEach(sId -> {
	        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + sId);
	        
	        // Fetch student details for each student ID
	        List<Map<String, Object>> detailsList = student_details_repo.getStudentDetailsBasedOnStudentId(sId);
	        
	        // For each student's details, merge the marks data
	        detailsList.forEach(studentDetail -> {
	            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + studentDetail);
	            
	            // Create a new HashMap to modify the student detail
	            Map<String, Object> modifiedDetail = new HashMap<>(studentDetail);
	            
	            // Merge external marks into student details
	            modifiedDetail.put("external_max_marks", externalMaxMarks);  // Add max marks
	            modifiedDetail.put("external_min_marks", externalMinMarks);  // Add min marks
	            
	            // Add the modified data to the uniqueStudentDetails set (ensures uniqueness)
	            uniqueStudentDetails.add(modifiedDetail);
	        });
	    });

	    // Add all unique student details to the final list
	    studentDetails.addAll(uniqueStudentDetails);
	    
	    return studentDetails;
	}
	
}
