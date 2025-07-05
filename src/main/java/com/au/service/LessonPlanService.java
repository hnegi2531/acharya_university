package com.au.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.au.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.CSVHelperDto;
import com.au.dto.JwtDetails;
import com.au.dto.LessonPlanDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.LessonPlan;
import com.au.model.LessonPlanAssignment;
import com.au.response.ResponseHandler;

@Service
public class LessonPlanService {
	

	
	@Autowired
	private LessonPlanRepository lessonPlanRepository;

	@Autowired
	private LessonPlanAssignmentRepository lpa_repo;
	
	@Autowired
	private CSVHelperDto cvshelper;

	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;
	
	@Autowired
	private StudentAttendanceRepository studentAttendanceRepository;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	@Autowired
	private Syllabus_Repository syllabusRepository;

	@Autowired
	private CourseAssignmentRepository courseAssignmentRepository;

	@Autowired
	private Syllabus_Service syllabusService;
	
	private final Integer DEFAULT_LESSON_PLAN_ASSIGNMENT_ID=0;

	public void saveFile(MultipartFile file,Integer lesson_id, Integer user_id, String username) throws Exception {
		
			List<LessonPlanAssignment> lessonPlanAss = cvshelper.csvToTutorials(file.getInputStream(),user_id);
			lessonPlanAss.stream().forEach(lesson->{
				lesson.setLesson_id(lesson_id);
				lesson.setCreated_by(user_id);
				lesson.setCreated_username(username);
			});
			lpa_repo.saveAll(lessonPlanAss);
	
	}

//	public List<LessonPlan> listAll() {
//		return lessonPlanRepository.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword){
		Page<Object> response1 = lessonPlanRepository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = lessonPlanRepository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public LessonPlan saveLessonPlan(LessonPlan s) {
		s.setEmployee_id(employeeDetailsRepository.getEmpId1(userAuthenticationRepository.getEmail1(s.getCreated_by())));
		return lessonPlanRepository.save(s);
	}


	public LessonPlan get(Integer id) {
		return lessonPlanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("LessonPlan Not Found:" + id));
	}

	public void delete(Integer id) {
		LessonPlan cc = lessonPlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LessonPlan Not Found:" + id));
		lessonPlanRepository.update(id);
	}

	public void delete1(Integer id) {
		LessonPlan cc = lessonPlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("LessonPlan Not Found:" + id));
		lessonPlanRepository.update1(id);

	}

	public List<LessonPlanAssignment> getData(LessonPlanDto lp, String jwtToken)
			throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		
		Integer rrr = lessonPlanRepository.checkEstingData(lp.getLp().getAc_year_id(),lp.getLp().getProgram_assignment_id() ,
				lp.getLp().getProgram_id(),lp.getLp().getProgram_specialization_id(),lp.getLp().getCourse_assignment_id() ,
				lp.getLp().getSchool_id(),lp.getLp().getYear_sem(),lp.getLp().getEmployee_id());
		
		System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ "+rrr);
		
//		Integer emp = employeeDetailsRepository.getEmpId1(userAuthenticationRepository.getEmail1(jwtDetails.getUserId()));
		
//		if(lessonPlanRepository.checkEstingData(lp.getLp().getAc_year_id(),lp.getLp().getProgram_assignment_id() ,
//				lp.getLp().getProgram_id(),lp.getLp().getProgram_specialization_id(),lp.getLp().getCourse_assignment_id() ,
//					lp.getLp().getSchool_id(),lp.getLp().getYear_sem(),emp) >=1 ) {
//			
//			throw new Exception("Data already present for the given academic year, program, year_sem and employee. Please add from index !");
//			
//		} else {
//		else if(lpa_repo.getCountOfCombination(lp.getLpa().get(0).getPlan_date(),lp.getLpa().get(0).getContents(), 
//				lp.getLpa().get(0).getTeaching_aid(), jwtDetails.getUserId()) >=1) {
//			throw new Exception("Data already present for the given plan_date, contents, teaching_aid !");
//		}
		
		LessonPlan lessonPlan=lessonPlanRepository.createdLessonPlan(lp.getLp().getAc_year_id(),lp.getLp().getCourse_assignment_id(),lp.getLp().getUser_id());
		if(ObjectUtils.isEmpty(lessonPlan)) {
		lessonPlan = new LessonPlan();
		lessonPlan.setAc_year_id(lp.getLp().getAc_year_id());
		lessonPlan.setSchool_id(lp.getLp().getSchool_id());
		lessonPlan.setProgram_id(lp.getLp().getProgram_id());
		lessonPlan.setProgram_specialization_id(lp.getLp().getProgram_specialization_id());
		lessonPlan.setYear_sem(lp.getLp().getYear_sem());
		lessonPlan.setSection_id(lp.getLp().getSection_id());
		lessonPlan.setSubject_id(lp.getLp().getSubject_id());
		lessonPlan.setSubject_name_short(lp.getLp().getSubject_name_short());
		lessonPlan.setBook_id(lp.getLp().getBook_id());
		lessonPlan.setActive(lp.getLp().getActive());
		lessonPlan.setCreated_by(jwtDetails.getUserId());
		lessonPlan.setCreated_username(jwtDetails.getUserName());
		lessonPlan.setCourse_assignment_id(lp.getLp().getCourse_assignment_id());
		lessonPlan.setProgram_assignment_id(lp.getLp().getProgram_assignment_id());
		lessonPlan.setEmployee_id(employeeDetailsRepository.getEmpId1(userAuthenticationRepository.getEmail1(jwtDetails.getUserId())));
		lessonPlan.setSubject_assignment_id(lp.getLp().getSubject_assignment_id());
		lessonPlan.setUser_id(lp.getLp().getUser_id());
		LessonPlan lessonplan = saveLessonPlan(lessonPlan);
		
		}

		
		List<LessonPlanAssignment> lessonPlanAssignment=saveLessonPlanAssignment(lp,lessonPlan,new ArrayList<LessonPlanAssignment>(),jwtDetails);
		
		
		return lessonPlanAssignment;
	}
	

	private List<LessonPlanAssignment> saveLessonPlanAssignment(LessonPlanDto lp, LessonPlan lessonPlan,
			ArrayList<LessonPlanAssignment> arrayList, JwtDetails jwtDetails) {
		lp.getLpa().stream().forEach(plan -> {
			
			
			LessonPlanAssignment lessonplanAssignment = new LessonPlanAssignment();
			lessonplanAssignment.setLesson_id(lessonPlan.getLesson_id());
			lessonplanAssignment.setPlan_date(plan.getPlan_date());
			lessonplanAssignment.setContents(plan.getContents());
			lessonplanAssignment.setTeaching_aid(plan.getTeaching_aid());
			lessonplanAssignment.setActive(true);
			lessonplanAssignment.setCreated_by(jwtDetails.getUserId());
			lessonplanAssignment.setCreated_username(jwtDetails.getUserName());
			lessonplanAssignment.setBook_id(lessonPlan.getBook_id());
			lessonplanAssignment.setIct_text(plan.getIct_text());
			lessonplanAssignment.setType(plan.getType());
			lessonplanAssignment.setTeaching_mode(plan.getTeaching_mode());
			lessonplanAssignment.setLearning_style(plan.getLearning_style());
			saveLessonPlanAssignment(lessonplanAssignment);
			arrayList.add(lessonplanAssignment);
			});
		return arrayList;
	}

	private LessonPlanAssignment saveLessonPlanAssignment(LessonPlanAssignment lessonplanAssignment) {
		return lpa_repo.save(lessonplanAssignment);
	}

	public ByteArrayInputStream load() {
		List<LessonPlanAssignment> tutorials = lpa_repo.findAll();
		ByteArrayInputStream in = CSVHelperDto.tutorialsToCSV(tutorials);
		return in;
	}

	public List<LessonPlanAssignment> getDataFromFile(MultipartFile file, String jwtToken)    //, LessonPlan lessonPlanDto
			throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		
		List<LessonPlanAssignment> lessonPlanAssignment= new ArrayList<LessonPlanAssignment>();
		List<LessonPlanAssignment> lessonPlanAssignment1= new ArrayList<LessonPlanAssignment>();
		
		if (CSVHelperDto.hasCSVFormat(file)) {
			 lessonPlanAssignment = readFile(file,jwtDetails.getUserId(),jwtDetails.getUserName());
		}
		
		return lessonPlanAssignment;
	}
	
	public List<LessonPlanAssignment> readFile(MultipartFile file, Integer user_id, String username) throws Exception {
		
		List<LessonPlanAssignment> lessonPlanAss = cvshelper.csvToTutorials(file.getInputStream(),user_id);
		lessonPlanAss.stream().forEach(lesson->{
			
			lesson.setCreated_by(user_id);
			lesson.setCreated_username(username);
			lesson.setActive(true);
		});
		return lessonPlanAss;
		//lpa_repo.saveAll(lessonPlanAss);

}
	
	public List<HashMap<String, Object>> getLessonPlanByAcYear(Integer ac_year_id){
		List<HashMap<String, Object>> lessonPlans=lessonPlanRepository.fetchLessonPlanByAcYear(ac_year_id);
		return lessonPlans;
	}
	
	public List<HashMap<String, Object>> getLessonPlanBasedOnAcYearIdAndUserId(Integer ac_year_id, Integer user_id) {
		List<HashMap<String, Object>> lessonPlans=lessonPlanRepository.getLessonPlanBasedOnAcYearIdAndUserId(ac_year_id,user_id);
//		return lessonPlanRepository.getLessonPlanBasedOnAcYearIdAndUserId(ac_year_id,user_id);
		return lessonPlans.stream()
        .collect(Collectors.toMap(
            map -> map.get("ac_year_id") + "_" + map.get("course_assignment_id") + "_" + map.get("emp_id"),
            map -> map,
            (existing, replacement) -> existing)) 
        .values()
        .stream()
        .collect(Collectors.toList());
	}
	
	public List<HashMap<String, Object>> getLessonPlanByAcademicYearProgramSpecilizationCourseYearOrSemAndEmployeeId(
			Integer ac_year_id, Integer program_id, Integer program_specialization_id, Integer course_assignment_id, Integer year_sem,Integer employee_id, Boolean ict_status) {
		List<Integer> assignedLessonPlanAssignmentIds=studentAttendanceRepository.getAssignedLessonPlanAssignmentIds(ac_year_id,
				program_id, program_specialization_id, course_assignment_id, year_sem);
		if(assignedLessonPlanAssignmentIds.isEmpty()) {
			assignedLessonPlanAssignmentIds.add(DEFAULT_LESSON_PLAN_ASSIGNMENT_ID);
		}
		return lessonPlanRepository.getLessonPlanByAcademicYearProgramSpecilizationSchoolSectionCourseYearOrSem(ac_year_id,
				program_id, program_specialization_id, course_assignment_id, year_sem, employee_id,assignedLessonPlanAssignmentIds,ict_status);
	}
	
	public List<Map<String, Object>> getLessonPlanDataByLessonId(Integer lesson_id){
		return lessonPlanRepository.getLessonPlanDataByLessonId(lesson_id);
	}
	
	public List<HashMap<String, Object>> getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId(Integer ac_year_id, Integer course_assignment_id, Integer year_sem,Integer employee_id, Boolean ict_status) {
		List<Integer> assignedLessonPlanAssignmentIds=studentAttendanceRepository.getAssignedLessonPlanAssignmentIdsByAcademicYearCourseYearOrSem(ac_year_id, course_assignment_id, year_sem);
		if(assignedLessonPlanAssignmentIds.isEmpty()) {
			assignedLessonPlanAssignmentIds.add(DEFAULT_LESSON_PLAN_ASSIGNMENT_ID);
		}
		return lessonPlanRepository.getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId(ac_year_id,course_assignment_id, year_sem, employee_id,assignedLessonPlanAssignmentIds,ict_status);
	}
	
	public List<HashMap<String, Object>> getLessonPlanByAcademicYearAndEmployeeId(Integer ac_year_id, Integer user_id){
		Integer emp_id = uar_repo.getEmployee_id(user_id);
		return lessonPlanRepository.getLessonPlanByAcademicYearAndEmployeeId(ac_year_id,emp_id);
	}
	
	
	public List<HashMap<String, Object>> getDataByLessonId(Integer lesson_id){
		return lessonPlanRepository.getDataByLessonId(lesson_id);
	}

	public List<HashMap<String, Object>> getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId1(Integer ac_year_id, Integer course_assignment_id, Integer year_sem,Integer employee_id, Boolean ict_status) {
		List<Integer> assignedLessonPlanAssignmentIds=studentAttendanceRepository.getAssignedLessonPlanAssignmentIdsByAcademicYearCourseYearOrSem(ac_year_id, course_assignment_id, year_sem);
		if(assignedLessonPlanAssignmentIds.isEmpty()) {
			assignedLessonPlanAssignmentIds.add(DEFAULT_LESSON_PLAN_ASSIGNMENT_ID);
		}
		return lessonPlanRepository.getLessonPlanByAcademicYearCourseYearOrSemAndEmployeeId1(ac_year_id,course_assignment_id, year_sem, employee_id,assignedLessonPlanAssignmentIds,ict_status);
	}

	public 	ResponseEntity<Object> getLessonPlanForMobile(Integer ac_year_id, String program_id,
			String program_specialization_id, Integer course_assignment_id, Integer year_sem, Integer employee_id) {
		Map<String , Object> courseAndSyllabus=new HashMap<>();
		List<Integer> program_ids=ResponseHandler.toConvertCommaSeperatedIdsAsList(program_id);
		List<Integer> specializations_ids=ResponseHandler.toConvertCommaSeperatedIdsAsList(program_specialization_id);
		List<Integer> assignedLessonPlanAssignmentIds=studentAttendanceRepository.getAssignedLessonPlanAssignmentIdsForMobile(ac_year_id,
				program_ids, specializations_ids, course_assignment_id, year_sem);
		if(assignedLessonPlanAssignmentIds.isEmpty()) {
			assignedLessonPlanAssignmentIds.add(DEFAULT_LESSON_PLAN_ASSIGNMENT_ID);
		}
		List<HashMap<String, Object>> lessonPlan= lessonPlanRepository.getLessonPlanForMobile(ac_year_id,
				program_ids, specializations_ids, course_assignment_id, year_sem, employee_id,assignedLessonPlanAssignmentIds);
		List<Map<String, Object>> syllabusDetails=syllabusDetails(course_assignment_id,ac_year_id);
		courseAndSyllabus.put("lessonPlan", lessonPlan);
		courseAndSyllabus.put("syllabusDetails", syllabusDetails);
//		if(ObjectUtils.isNotEmpty(lessonPlan) && ObjectUtils.isNotEmpty(syllabusDetails)) {
//			return ResponseHandler.generateResponse(true, HttpStatus.OK,courseAndSyllabus);
//		}
		return ResponseHandler.generateResponse(true, HttpStatus.OK,courseAndSyllabus);
	}

	private List<Map<String, Object>> syllabusDetails(Integer courseAssignmentId, Integer ac_year_id) {
		List<Map<String, Object>> syllabus=new ArrayList<>();
		String commonCourseStatus=courseAssignmentRepository.commonCourseValidationByCourseAssignmentId(courseAssignmentId);
		if(ObjectUtils.isNotEmpty(commonCourseStatus) &&  commonCourseStatus.equalsIgnoreCase("false")) {
			 syllabus = syllabusRepository.getSyllabusDetails(courseAssignmentId);
		}else {
			Integer courseId=courseAssignmentRepository.courseIdByCourseAssignmentId(courseAssignmentId);
			syllabus =syllabusService.syllabusByCourseAssignment(courseId,ac_year_id);
		}
		return syllabus;
	}

}
