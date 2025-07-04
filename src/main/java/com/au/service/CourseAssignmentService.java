package com.au.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.CourseAssignmentRequest;
import com.au.exception.ResourceNotFoundException;
import com.au.model.CourseAssignment;
import com.au.model.CoursePattern;
import com.au.repository.CourseAssignmentRepository;
import com.au.repository.CourseRepository;
import com.au.repository.CourseStudentAssignmentRepository;
import com.au.repository.Org_Repository;
import com.au.repository.ProgramAssigmentRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class CourseAssignmentService {

	@Autowired
	private CourseAssignmentRepository courseAssignmentRepository;
	
	@Autowired
	private CourseRepository course_repository;
	
	@Autowired
	private ProgramAssigmentRepository r_repo;
	
	@Autowired
	private StudentDetailsRepository stu_repo;

	@Autowired
	private Org_Repository org_repo;
	
	@Autowired
	private CourseStudentAssignmentRepository cors_stu_assign_repo;
	
	private final ModelMapper modelMapper = new ModelMapper();

	public List<CourseAssignment> listAll() {
		return courseAssignmentRepository.findAll();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = courseAssignmentRepository.fetchAllDetail1(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = courseAssignmentRepository.fetchAllDetail2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}
	
	public ResponseEntity<Object> listAll1BasedOnUseraId(Pageable pageable, Object keyword, Integer user_id) {
		Page<Object> response1 = courseAssignmentRepository.listAll1BasedOnUseraId(pageable, keyword, user_id );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2BasedOnUseraId(Pageable pageable, Integer user_id) {
		Page<Object> response = courseAssignmentRepository.listAll2BasedOnUseraId(pageable, user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}

	public CourseAssignment save_CourseAssignment(CourseAssignmentRequest car) {

		Integer orgId = course_repository.getOrgId(car.getCourse_id());
		String orgName = org_repo.getOrgnizationName(orgId);
		
		if(orgName.equalsIgnoreCase("ACHARYA UNIVERSITY")) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + orgName);
		if (courseAssignmentRepository.countCourseAssignmentOnAcademicSpecializationAndCourse(car.getAc_year_id(),
				car.getProgram_specialization_id(), car.getCourse_id())>=1) {
			throw new RuntimeException("Course Already Assigned To Academic Year And Program Specialization");
		} else {
//			String course_code = car.getSchool_name_short().substring(0, 2);
			String course_code = r_repo.getGraduationShortName(car.getAc_year_id(), car.getSchool_id(), car.getProgram_assignment_id());
			course_code += car.getAc_year().substring(2, 4);
			course_code += car.getProgram_specialization_short_name().substring(0, 2);

			int three_digit_random_number = (int) (Math.random() * (1000 - 100 + 1) + 100);
			course_code += three_digit_random_number;
			course_code += car.getCourse_type_name().substring(0, 1);
			course_code += car.getCourse_category_code().substring(0, 1);
			System.out.println("######################################################### " +course_code);
			if (course_repository.checkingOfCourseCreatedByOrganization(car.getCourse_id()) == 1) {
				course_repository.updateCourseCode(car.getCourse_id(), course_code);
			}
			CourseAssignment ca = new CourseAssignment();
			ca.setAc_year_id(car.getAc_year_id());
			ca.setSchool_id(car.getSchool_id());
			ca.setProgram_id(car.getProgram_id());
			ca.setDept_id(car.getDept_id());
			ca.setProgram_specialization_id(car.getProgram_specialization_id());
			ca.setCourse_id(car.getCourse_id());
			ca.setCourse_type_id(car.getCourse_type_id());
			ca.setCourse_category_id(car.getCourse_category_id());
			ca.setSyllabus_id(car.getSyllabus_id());
			ca.setEmp_id(car.getEmp_id());
			ca.setEmail(car.getEmail());
			ca.setYear_sem(car.getYear_sem());
			ca.setTotal_credit(car.getTotal_credit());
			ca.setLecture(car.getLecture());
			ca.setTutorial(car.getTutorial());
			ca.setPractical(car.getPractical());
			ca.setDuration(car.getDuration());
			ca.setCie_marks(car.getCie_marks());
			ca.setSee_marks(car.getSee_marks());
			ca.setCourse_price(car.getCourse_price());
			ca.setCourse_price_usd(car.getCourse_price_usd());
			ca.setCreated_by(car.getCreated_by());
			ca.setCreated_username(car.getCreated_username());
			ca.setActive(car.getActive());
			ca.setRemarks(car.getRemarks());
			ca.setProgram_assignment_id(car.getProgram_assignment_id());
			ca.setUser_id(car.getUser_id());
			ca.setCourse_assignment_coursecode(course_code);
			return courseAssignmentRepository.save(ca);
		}
	}else {
		
		if (courseAssignmentRepository.countCourseAssignmentOnAcademicSpecializationAndCourse(car.getAc_year_id(),
				car.getProgram_specialization_id(), car.getCourse_id())>=1) {
			throw new RuntimeException("Course Already Assigned To Academic Year And Program Specialization");
		} else {
//			String course_code = car.getSchool_name_short().substring(0, 2);
//			String course_code = r_repo.getGraduationShortName(car.getAc_year_id(), car.getSchool_id(), car.getProgram_assignment_id());
//			course_code += car.getAc_year().substring(2, 4);
//			course_code += car.getProgram_specialization_short_name().substring(0, 2);
//
//			int three_digit_random_number = (int) (Math.random() * (1000 - 100 + 1) + 100);
//			course_code += three_digit_random_number;
//			course_code += car.getCourse_type_name().substring(0, 1);
//			course_code += car.getCourse_category_code().substring(0, 1);
//
//			if (course_repository.checkingOfCourseCreatedByOrganization(car.getCourse_id()) == 1) {
//				course_repository.updateCourseCode(car.getCourse_id(), course_code);
//			}
			String cc = course_repository.getCourseCode(car.getCourse_id());
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " +cc);
			CourseAssignment ca1 = new CourseAssignment();
			ca1.setAc_year_id(car.getAc_year_id());
			ca1.setSchool_id(car.getSchool_id());
			ca1.setProgram_id(car.getProgram_id());
			ca1.setDept_id(car.getDept_id());
			ca1.setProgram_specialization_id(car.getProgram_specialization_id());
			ca1.setCourse_id(car.getCourse_id());
			ca1.setCourse_type_id(car.getCourse_type_id());
			ca1.setCourse_category_id(car.getCourse_category_id());
			ca1.setSyllabus_id(car.getSyllabus_id());
			ca1.setEmp_id(car.getEmp_id());
			ca1.setEmail(car.getEmail());
			ca1.setYear_sem(car.getYear_sem());
			ca1.setTotal_credit(car.getTotal_credit());
			ca1.setLecture(car.getLecture());
			ca1.setTutorial(car.getTutorial());
			ca1.setPractical(car.getPractical());
			ca1.setDuration(car.getDuration());
			ca1.setCie_marks(car.getCie_marks());
			ca1.setSee_marks(car.getSee_marks());
			ca1.setCourse_price(car.getCourse_price());
			ca1.setCourse_price_usd(car.getCourse_price_usd());
			ca1.setCreated_by(car.getCreated_by());
			ca1.setCreated_username(car.getCreated_username());
			ca1.setActive(car.getActive());
			ca1.setRemarks(car.getRemarks());
			ca1.setProgram_assignment_id(car.getProgram_assignment_id());
			ca1.setUser_id(car.getUser_id());
			ca1.setCourse_assignment_coursecode(cc);
			return courseAssignmentRepository.save(ca1); 

		}
	}
		
	}

	public CourseAssignment get(Integer id) {
		return courseAssignmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseAssignment Not Found:" + id));
	}
	
	public CourseAssignment updateCourseAssignment(CourseAssignment c) {
		return courseAssignmentRepository.save(c);
	}

	public void delete(Integer id) {
		courseAssignmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseAssignment Not Found:" + id));
		courseAssignmentRepository.update(id);
	}

	public void delete1(Integer id) {
		courseAssignmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CourseAssignment Not Found:" + id));
		courseAssignmentRepository.update1(id);
	}

	public List<HashMap<String, Object>> fetchAllSyllabusDetails(Integer syllabus_id) {
		return courseAssignmentRepository.fetchAllSyllabusDetail(syllabus_id);
	}

	public List<HashMap<String, Object>> fetch(Integer course_assignment_id) {
		return courseAssignmentRepository.fetchAllSyllabusDetails(course_assignment_id);
	}

	public HashMap<String, Object> fetchCreditDetails(Integer ac_year_id, Integer school_id, Integer program_id,
			Integer program_specialization_id, Integer course_id) {
		return courseAssignmentRepository.fetchAllCreditDetails(ac_year_id,school_id,program_id,program_specialization_id,course_id);
	}

	public HashMap<String, Object> fetchAmountDetails(Integer ac_year_id, Integer school_id, Integer program_id,Integer dept_id,
			Integer program_specialization_id, Integer course_id, Integer course_category_id, String year_sem) {
		return courseAssignmentRepository.fetchAllAmountDetails(ac_year_id,school_id,program_id,dept_id,program_specialization_id,course_id,course_category_id,year_sem);
	}
	
	public List<HashMap<String, Object>> fetchCourseAssignmentDetails(Integer ac_year_id,Integer created_by) {
		return courseAssignmentRepository.fetchAllCourseAssignmentDetails(ac_year_id, created_by);
	}
	
	public List<HashMap<String, Object>> fetchAllCourseDetailsForCourseMappingIndex(Integer school_id,Integer program_specialization_id,Integer year_sem) {
		return courseAssignmentRepository.fetchAllCourseDetailsForCourseMappingIndex(school_id,program_specialization_id,year_sem);
	}
	
	public List<Map<String, Object>> fetchAllCourseDetailsForTimeTable(List<Integer> emp_ids) {
		return courseAssignmentRepository.fetchAllCourseDetailsForTimeTable(emp_ids);
	}
	
	public List<Map<String, Object>> fetchAllCourseDetailsForSectionTimeTable(List<Integer> emp_ids,Integer program_specialization_id,Integer year_sem) {
		return courseAssignmentRepository.fetchAllCourseDetailsForSectionTimeTable(emp_ids,program_specialization_id,year_sem);
	}

	public List<Map<String, Object>> fetchAllCourseDetailsForBatchTimeTable(List<Integer> emp_ids,Integer batch_assignment_id) {
		return courseAssignmentRepository.fetchAllCourseDetailsForBatchTimeTable(emp_ids,batch_assignment_id);
	}

	public List<Map<String, Object>> fetchCourseDetail(Integer year_sem, Integer student_id) {
		List<Map<String, Object>> final_response=new ArrayList<>();
		Integer prog_spec_id = stu_repo.getProgramSpecializationId(student_id);
		List<Map<String, Object>> course_assigned_data= courseAssignmentRepository.fetchCourseDetail(year_sem, prog_spec_id );
		course_assigned_data.stream().forEach(course_assigned ->{
			final_response.add(course_assigned);
		});
		
//		List<Map<String, Object>> student_assigned_courses= cors_stu_assign_repo.fetchAssignedCoursesOfStudent(student_id,year_sem);
//		student_assigned_courses.stream().forEach(assigned_courses ->{
//			final_response.add(assigned_courses);
//		});
		return final_response;
	}
	
	public List<Map<String, Object>> fetchSyllabusDetails(Integer course_assignment_id) {
		return courseAssignmentRepository.fetchSyllabusDetails(course_assignment_id);
	}
	
	public List<Map<String, Object>> fetchCourseObjective(Integer course_assignment_id) {
		return courseAssignmentRepository.fetchCourseObjective(course_assignment_id);
	}
	
	public List<Map<String, Object>> fetchCourseOutcome(Integer course_assignment_id) {
		return courseAssignmentRepository.fetchCourseOutcome(course_assignment_id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReferenceBooksDetail(Integer course_assignment_id) {
		List<Map<String, Object>> referenceBooks=courseAssignmentRepository.getReferenceBooksDetail(course_assignment_id);
		List<Map<String, Object>>  response=new ArrayList<Map<String,Object>>();
		referenceBooks.stream().forEach(rb -> {
			Map<String, Object> mapData=modelMapper.map(rb, Map.class);
			if(ObjectUtils.isNotEmpty(rb.get("eresource"))) {
				
				List<String> eresourceList=Arrays.stream(rb.get("eresource").toString().split(","))
						.map(String::trim)
						.collect(Collectors.toList());
				mapData.put("eresource",eresourceList);
				
			}
			response.add(mapData);
		});

		return response;
	}
	
	public List<Map<String, Object>> getCourseDetailsData(Integer emp_id) {
		return courseAssignmentRepository.getCourseDetailsData(emp_id);
	}

	public List<Map<String, Object>> getAllActiveCourseDetailsData() {
		return courseAssignmentRepository.getAllActiveCourseDetailsData();
	}

	public List<HashMap<String, Object>> fetchAllCourseDetail(Integer program_id, Integer program_specialization_id, Integer year_sem) {
		List<HashMap<String, Object>> specializationAssignedCourses= courseAssignmentRepository.fetchAllCourseDetail(program_id,program_specialization_id,year_sem);
		List<Integer> commonCourseIds=course_repository.commonCourseIds();
		List<HashMap<String,Object>> commonCourses=courseAssignmentRepository.commonCourseDetailsbyYearOrSem(year_sem,commonCourseIds);
		commonCourses.parallelStream().forEach(cmc -> {
			specializationAssignedCourses.add( cmc);
		});
		
		return specializationAssignedCourses;
	}
	
	public List<HashMap<String, Object>> allCourseDetailFromCourseAssignment() {
		return courseAssignmentRepository.allCourseDetailFromCourseAssignment();
	}

	public List<Map<String, Object>> getCourseAssignmentDetailsBasedOnUserId(Integer user_id) {
		return courseAssignmentRepository.getCourseAssignmentDetailsBasedOnUserId(user_id);
	}
}
