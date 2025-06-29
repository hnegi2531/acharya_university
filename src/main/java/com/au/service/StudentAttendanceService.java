package com.au.service;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.au.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.StudentDetailsAttendanceDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.ReportingStudents;
import com.au.model.StudentAttendance;
import com.au.model.Student_Details;
import com.au.model.TimeTable;
import com.au.response.ResponseHandler;

import com.au.model.BatchAssignment;
import com.au.model.Condonation;
import com.au.model.SectionAssignment;


@Service
public class StudentAttendanceService {
	
	@Autowired
	private StudentAttendanceRepository student_attendance_repository;
	
	@Autowired
	private StudentDetailsRepository student_details_repo;
	
	@Autowired
	private ReportingStudentsRepository reporting_student_repo;
	
	@Autowired
	private TimeTableRepository timeTableRepo;
	
	@Autowired
	private BatchAssignmentRepository batchAssignmentRepository;
	
	@Autowired
	private SectionAssignmentRepository sectionAssignmentRepository;
	
	@Autowired
	private SubjectAssignmentRepository subjectAssignmentRepository;
	
	@Autowired
	private CourseAssignmentRepository courseAssignmentRepository;
	
	
	@Autowired
	private CondonationRepository condotionRepository;
	
	@Autowired
	private ProctorStudentAssignmentRepository proctorStudentAssignmentRepository;
	
	@Autowired
	private StudentDetailsService studentDetailsService;
	
	@Autowired
	private Academic_year_repository academicYearRepository;
	
	@Autowired
	private ProgramAssigmentRepository programAssigmentRepository;
	
	@Autowired
	public ClassCommencementDetailsRepository classCommencementDetailsRepository;
	
	@Autowired
	private InternalSessionAssignmentRepository internalSessionAssignmentRepository;
	
	@Autowired
	private StudentMarksRepository studentMarksRepository;
	
	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;
	
	@Autowired
	private ProgramSpecilizationService programSpecilizationService;
	
	@Autowired
	private SubjectAssignmentRepository s_repo;
	
	@Autowired
	private SectionAssignmentService sectionAssignmentService;
	
	@Autowired
	private BatchAssignmentService batchAssignmentService;
	
	@Autowired
	private CourseRepository course_repository;

	@Autowired
	private BatchProgramAssignmentRepository batchProgramAssignmentRepository;
	
	private final Integer VALUE_FOR_IN_QUERY=0; 
	
	private final ModelMapper modelMapper = new ModelMapper();
	
	Logger log = LoggerFactory.getLogger(StudentAttendanceService.class);

	public List<StudentAttendance> saveStudentAttendance(List<StudentAttendance>  studentAttendance,Integer sectionAssignmentId) throws Exception {
		List<Integer> timeTableIds = studentAttendance.stream().map(ich -> ich.getTime_table_id()).distinct().collect(Collectors.toList());

		List<Map<String,Object>> attendanceStatus= timeTableRepo.checkAttendanceStatus(timeTableIds);
		attendanceStatus.stream().forEach(as -> {
			if(! attendanceStatus.isEmpty() && ((Integer)as.get("attendance_status") == 1 || (Integer)as.get("attendance_status") == 2)) {
				throw new RuntimeException("Attendance is already taken for time table id : "+ (Integer)as.get("time_table_id"));
			}
		});

			Set<StudentAttendance> uniqueAttendances = new HashSet<>(studentAttendance);
			Optional<SectionAssignment> sa=sectionAssignmentRepository.findById(sectionAssignmentId);
			
			List<Integer> studentIdsFromSectionAssignment = Arrays.stream(sa.get().getStudent_ids().split(","))
					.filter(e -> e.length() != 0).map(Integer::parseInt).distinct().collect(Collectors.toList());
			
			List<Integer> studentIdsFromRequestBody = uniqueAttendances.stream().map(sta -> sta.getStudent_id()).collect(Collectors.toList());
			
			studentIdsFromSectionAssignment.removeAll(studentIdsFromRequestBody);
			
//			Date commencementDate = classCommencementDetailsRepository
//					.getCommencementDateByAcademicYearIdSpecializationAndYearSem(
//							studentAttendance.get(0).getAc_year_id(), sa.get().getProgram_specialization_id(),
//							studentAttendance.get(0).getYear_or_sem());
			
			if(!studentIdsFromSectionAssignment.isEmpty()) {
				studentIdsFromSectionAssignment.stream().distinct().forEach(sid -> {
//					if(reportingStudentsRepository.isReportedAfterClassCommencementDate(sid,commencementDate)) {
						StudentAttendance sat=new StudentAttendance();
						sat.setAc_year_id(studentAttendance.get(0).getAc_year_id());
						sat.setActive(true);
						sat.setBatch_id(studentAttendance.get(0).getBatch_id());
						sat.setCourse_assignment_id(studentAttendance.get(0).getCourse_assignment_id());
						sat.setCourse_id(studentAttendance.get(0).getCourse_id());
						sat.setCreated_by(studentAttendance.get(0).getCreated_by());
						sat.setCreated_username(studentAttendance.get(0).getCreated_username());
						sat.setDate_of_class(studentAttendance.get(0).getDate_of_class());
						sat.setDescription(studentAttendance.get(0).getDescription());
						sat.setEmp_id(studentAttendance.get(0).getEmp_id());
						sat.setLesson_assignment_id(studentAttendance.get(0).getLesson_assignment_id());
						sat.setLesson_id(studentAttendance.get(0).getLesson_id());
						sat.setOffline_status(studentAttendance.get(0).getOffline_status());
						sat.setPresent_status(false);
						sat.setRemarks(studentAttendance.get(0).getRemarks());
						sat.setSchool_id(studentAttendance.get(0).getSchool_id());
						sat.setSection_id(studentAttendance.get(0).getSection_id());
						sat.setStudent_id(sid);
						sat.setTime_slots_id(studentAttendance.get(0).getTime_slots_id());
						sat.setTime_table_id(studentAttendance.get(0).getTime_table_id());
						sat.setYear_or_sem(studentAttendance.get(0).getYear_or_sem());
						sat.setSyllabus_id(studentAttendance.get(0).getSyllabus_id());
					    uniqueAttendances.add(sat);
//					}
					
				});
			}
			System.out.println(uniqueAttendances);
			List<StudentAttendance> studentAttendanceResponse =  student_attendance_repository.saveAll(uniqueAttendances);
			timeTableRepo.updateAttendanceStatus(timeTableIds);
			return studentAttendanceResponse;

		
	}
	
	public List<StudentAttendance> saveStudentAttendanceOnSectionForLMS(List<StudentAttendance>  studentAttendance,Integer sectionAssignmentId) throws Exception {
		List<Integer> timeTableIds = studentAttendance.stream().map(ich -> ich.getTime_table_id()).distinct().collect(Collectors.toList());

		List<Map<String,Object>> attendanceStatus= timeTableRepo.checkAttendanceStatus(timeTableIds);
		attendanceStatus.stream().forEach(as -> {
			if(! attendanceStatus.isEmpty() && ((Integer)as.get("attendance_status") == 1 || (Integer)as.get("attendance_status") == 2)) {
				throw new RuntimeException("Attendance is already taken for time table id : "+ (Integer)as.get("time_table_id"));
			}
		});

			Set<StudentAttendance> uniqueAttendances = new HashSet<>(studentAttendance);
			Optional<SectionAssignment> sa=sectionAssignmentRepository.findById(sectionAssignmentId);
			
			List<Integer> studentIdsFromSectionAssignment = Arrays.stream(sa.get().getStudent_ids().split(","))
					.filter(e -> e.length() != 0).map(Integer::parseInt).distinct().collect(Collectors.toList());
			
			List<Integer> studentIdsFromRequestBody = uniqueAttendances.stream().map(sta -> sta.getStudent_id()).collect(Collectors.toList());
			
			studentIdsFromSectionAssignment.removeAll(studentIdsFromRequestBody);
			
//			Date commencementDate = classCommencementDetailsRepository
//					.getCommencementDateByAcademicYearIdSpecializationAndYearSem(
//							studentAttendance.get(0).getAc_year_id(), sa.get().getProgram_specialization_id(),
//							studentAttendance.get(0).getYear_or_sem());
			
			if(!studentIdsFromSectionAssignment.isEmpty()) {
				studentIdsFromSectionAssignment.stream().distinct().forEach(sid -> {
//					if(reportingStudentsRepository.isReportedAfterClassCommencementDate(sid,commencementDate)) {
						StudentAttendance sat=new StudentAttendance();
						sat.setAc_year_id(studentAttendance.get(0).getAc_year_id());
						sat.setActive(true);
						sat.setBatch_id(studentAttendance.get(0).getBatch_id());
						sat.setCourse_assignment_id(studentAttendance.get(0).getCourse_assignment_id());
						sat.setCourse_id(studentAttendance.get(0).getCourse_id());
						sat.setCreated_by(studentAttendance.get(0).getCreated_by());
						sat.setCreated_username(studentAttendance.get(0).getCreated_username());
						sat.setDate_of_class(studentAttendance.get(0).getDate_of_class());
						sat.setDescription(studentAttendance.get(0).getDescription());
						sat.setEmp_id(studentAttendance.get(0).getEmp_id());
						sat.setLesson_assignment_id(studentAttendance.get(0).getLesson_assignment_id());
						sat.setLesson_id(studentAttendance.get(0).getLesson_id());
						sat.setOffline_status(studentAttendance.get(0).getOffline_status());
						sat.setPresent_status(false);
						sat.setRemarks(studentAttendance.get(0).getRemarks());
						sat.setSchool_id(studentAttendance.get(0).getSchool_id());
						sat.setSection_id(studentAttendance.get(0).getSection_id());
						sat.setStudent_id(sid);
						sat.setTime_slots_id(studentAttendance.get(0).getTime_slots_id());
						sat.setTime_table_id(studentAttendance.get(0).getTime_table_id());
						sat.setYear_or_sem(studentAttendance.get(0).getYear_or_sem());
						sat.setSyllabus_id(studentAttendance.get(0).getSyllabus_id());
					    uniqueAttendances.add(sat);
//					}
					
				});
			}
					
			List<StudentAttendance> studentAttendanceResponse =  student_attendance_repository.saveAll(uniqueAttendances);
			timeTableRepo.updateAttendanceStatus(timeTableIds);
			return studentAttendanceResponse;

		
	}
	
	public List<StudentAttendance> saveStudentAttendanceBatch(@Valid List<StudentAttendance> studentAttandance,
			Integer batchAssignmentId) throws Exception {
		List<Integer> timeTableIds = studentAttandance.parallelStream().
				map(ich -> ich.getTime_table_id()).distinct().collect(Collectors.toList());

		List<Map<String,Object>> attendanceStatus= timeTableRepo.checkAttendanceStatus(timeTableIds);
		attendanceStatus.stream().forEach(as -> {
			if(! attendanceStatus.isEmpty() && ((Integer)as.get("attendance_status") == 1 || (Integer)as.get("attendance_status") == 2)) {
				throw new RuntimeException("Attendance is already taken for time table id : "+ (Integer)as.get("time_table_id"));
			}
		});

			Set<StudentAttendance> uniqueAttendances = new HashSet<>(studentAttandance);
			Optional<BatchAssignment> batch=batchAssignmentRepository.findById(batchAssignmentId);
			
			List<Integer> studentIdsFromBatchAssignment = Arrays.stream(batch.get().getStudent_ids().split(","))
					.filter(e -> e.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
			
            List<Integer> studentIdsFromRequestBody = uniqueAttendances.stream().
            		map(sta -> sta.getStudent_id()).collect(Collectors.toList());
			
            studentIdsFromBatchAssignment.removeAll(studentIdsFromRequestBody);
            
//            Date commencementDate = classCommencementDetailsRepository
//					.getCommencementDateByAcademicYearIdSpecializationAndYearSem(
//							studentAttandance.get(0).getAc_year_id(), batch.get().getProgram_specialization_id(),
//							studentAttandance.get(0).getYear_or_sem()); 
            
            if(!studentIdsFromBatchAssignment.isEmpty()) {
            	studentIdsFromBatchAssignment.stream().distinct().forEach(sid -> {
//					if(reportingStudentsRepository.isReportedAfterClassCommencementDate(sid,commencementDate)) {
						StudentAttendance sat=new StudentAttendance();
						sat.setAc_year_id(studentAttandance.get(0).getAc_year_id());
						sat.setActive(true);
						sat.setBatch_id(studentAttandance.get(0).getBatch_id());
						sat.setCourse_assignment_id(studentAttandance.get(0).getCourse_assignment_id());
						sat.setCourse_id(studentAttandance.get(0).getCourse_id());
						sat.setCreated_by(studentAttandance.get(0).getCreated_by());
						sat.setCreated_username(studentAttandance.get(0).getCreated_username());
						sat.setDate_of_class(studentAttandance.get(0).getDate_of_class());
						sat.setDescription(studentAttandance.get(0).getDescription());
						sat.setEmp_id(studentAttandance.get(0).getEmp_id());
						sat.setLesson_assignment_id(studentAttandance.get(0).getLesson_assignment_id());
						sat.setLesson_id(studentAttandance.get(0).getLesson_id());
						sat.setOffline_status(studentAttandance.get(0).getOffline_status());
						sat.setPresent_status(false);
						sat.setRemarks(studentAttandance.get(0).getRemarks());
						sat.setSchool_id(studentAttandance.get(0).getSchool_id());
						sat.setSection_id(studentAttandance.get(0).getSection_id());
						sat.setStudent_id(sid);
						sat.setTime_slots_id(studentAttandance.get(0).getTime_slots_id());
						sat.setTime_table_id(studentAttandance.get(0).getTime_table_id());
						sat.setYear_or_sem(studentAttandance.get(0).getYear_or_sem());
						sat.setSyllabus_id(studentAttandance.get(0).getSyllabus_id());
					    uniqueAttendances.add(sat);
//					}
					
				});
			}
		
		
		List<StudentAttendance> studentAttendanceResponse =  student_attendance_repository.saveAll(uniqueAttendances);
		timeTableRepo.updateAttendanceStatus(timeTableIds);
		return studentAttendanceResponse;

		
	}
	
	public List<StudentAttendance> saveStudentAttendanceBatchForLMS(@Valid List<StudentAttendance> studentAttandance,
			Integer batchAssignmentId) throws Exception {
		List<Integer> timeTableIds = studentAttandance.parallelStream().
				map(ich -> ich.getTime_table_id()).distinct().collect(Collectors.toList());

		List<Map<String,Object>> attendanceStatus= timeTableRepo.checkAttendanceStatus(timeTableIds);
		attendanceStatus.stream().forEach(as -> {
			if(! attendanceStatus.isEmpty() && ((Integer)as.get("attendance_status") == 1 || (Integer)as.get("attendance_status") == 2)) {
				throw new RuntimeException("Attendance is already taken for time table id : "+ (Integer)as.get("time_table_id"));
			}
		});

			Set<StudentAttendance> uniqueAttendances = new HashSet<>(studentAttandance);
			Optional<BatchAssignment> batch=batchAssignmentRepository.findById(batchAssignmentId);
			
			List<Integer> studentIdsFromBatchAssignment = Arrays.stream(batch.get().getStudent_ids().split(","))
					.filter(e -> e.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
			
            List<Integer> studentIdsFromRequestBody = uniqueAttendances.stream().
            		map(sta -> sta.getStudent_id()).collect(Collectors.toList());
			
            studentIdsFromBatchAssignment.removeAll(studentIdsFromRequestBody);
            
//            Date commencementDate = classCommencementDetailsRepository
//					.getCommencementDateByAcademicYearIdSpecializationAndYearSem(
//							studentAttandance.get(0).getAc_year_id(), batch.get().getProgram_specialization_id(),
//							studentAttandance.get(0).getYear_or_sem()); 
            
            if(!studentIdsFromBatchAssignment.isEmpty()) {
				studentIdsFromBatchAssignment.stream().distinct().forEach(sid -> {
//					if(reportingStudentsRepository.isReportedAfterClassCommencementDate(sid,commencementDate)) {
					StudentAttendance sat = new StudentAttendance();
					sat.setAc_year_id(studentAttandance.get(0).getAc_year_id());
					sat.setActive(true);
					sat.setBatch_id(studentAttandance.get(0).getBatch_id());
					sat.setCourse_assignment_id(studentAttandance.get(0).getCourse_assignment_id());
					sat.setCourse_id(studentAttandance.get(0).getCourse_id());
					sat.setCreated_by(studentAttandance.get(0).getCreated_by());
					sat.setCreated_username(studentAttandance.get(0).getCreated_username());
					sat.setDate_of_class(studentAttandance.get(0).getDate_of_class());
					sat.setDescription(studentAttandance.get(0).getDescription());
					sat.setEmp_id(studentAttandance.get(0).getEmp_id());
					sat.setLesson_assignment_id(studentAttandance.get(0).getLesson_assignment_id());
					sat.setLesson_id(studentAttandance.get(0).getLesson_id());
					sat.setOffline_status(studentAttandance.get(0).getOffline_status());
					sat.setPresent_status(false);
					sat.setRemarks(studentAttandance.get(0).getRemarks());
					sat.setSchool_id(studentAttandance.get(0).getSchool_id());
					sat.setSection_id(studentAttandance.get(0).getSection_id());
					sat.setStudent_id(sid);
					sat.setTime_slots_id(studentAttandance.get(0).getTime_slots_id());
					sat.setTime_table_id(studentAttandance.get(0).getTime_table_id());
					sat.setYear_or_sem(studentAttandance.get(0).getYear_or_sem());
					sat.setSyllabus_id(studentAttandance.get(0).getSyllabus_id());
					uniqueAttendances.add(sat);
//					}

				});

			}
		
		List<StudentAttendance> studentAttendanceResponse =  student_attendance_repository.saveAll(uniqueAttendances);
		timeTableRepo.updateAttendanceStatus(timeTableIds);
		return studentAttendanceResponse;

		
	}

	public List<StudentAttendance> listAll() {
		return student_attendance_repository.findAll1();
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = student_attendance_repository.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = student_attendance_repository.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public StudentAttendance get(Integer id) {
		return student_attendance_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student Attendance Not Found :" + id));
	}
	
	public List<StudentAttendance> updateStudentAttendance(List<StudentAttendance> studentAttandance, List<Integer> id) {
		List<Integer> studentIds=studentAttandance.stream().filter(e -> ObjectUtils.isNotEmpty(e.getStudent_attendance_id())).distinct().map(StudentAttendance::getStudent_attendance_id).collect(Collectors.toList());
		if(new HashSet<>(studentIds).equals(new HashSet<>(id))) {
			return student_attendance_repository.saveAll(studentAttandance);
		}else {
			throw new RuntimeException("Student Attendance Id is not present");
		}
	}
	
	public void delete(Integer id) {
		student_attendance_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student Attendance Not Found:" + id));
		student_attendance_repository.update(id);
	}
	
	public void delete1(Integer id) {
		student_attendance_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Course Not Found:" + id));
		student_attendance_repository.update1(id);
	}
	
	
	public List<Map<String,Object>> studentAttendanceDetailsForMobileApp(Integer student_id){
		String program_type=student_details_repo.getProgramTypeOfStudent(student_id);
		ReportingStudents reporting_student=reporting_student_repo.getDetailsOfReportingStudentsByStudentId(student_id);
		Integer current_year_sem_of_student;
		List<Map<String,Object>> student_attendace;
		Map<String,Object> overall_percentage=new HashMap<>();

		if(program_type.equalsIgnoreCase("%Semester%")) {
			current_year_sem_of_student=reporting_student.getCurrent_sem();
			student_attendace=student_attendance_repository.studentAttendanceDetailsForMobileApp(student_id,current_year_sem_of_student);
		}else {
			current_year_sem_of_student=reporting_student.getCurrent_year();
			student_attendace=student_attendance_repository.studentAttendanceDetailsForMobileApp(student_id,current_year_sem_of_student);
		}

		IntSummaryStatistics average_percentage =student_attendace.stream().mapToInt(e -> ((BigInteger)e.get("percentage")).intValue()).summaryStatistics();
		overall_percentage.put("overall_average_percentage",average_percentage.getAverage());
		student_attendace.add(overall_percentage);
		return student_attendace;
	}
	
	public List<Map<String,Object>> studentAttendanceDetailsOfAllCourseForAbsentAndPresent(Integer student_id,Integer course_id){
		String program_type=student_details_repo.getProgramTypeOfStudent(student_id);
		ReportingStudents reporting_student=reporting_student_repo.getDetailsOfReportingStudentsByStudentId(student_id);
		Integer current_year_sem_of_student;
		List<Map<String,Object>> all_courses_student_attendance;

		if(program_type.equalsIgnoreCase("%Semester%")) {
			current_year_sem_of_student=reporting_student.getCurrent_sem();
			all_courses_student_attendance=student_attendance_repository.studentAttendanceDetailsOfAllCourseForAbsentAndPresent(student_id,current_year_sem_of_student,course_id);
		}else {
			current_year_sem_of_student=reporting_student.getCurrent_year();
			all_courses_student_attendance=student_attendance_repository.studentAttendanceDetailsOfAllCourseForAbsentAndPresent(student_id,current_year_sem_of_student,course_id);
		}
		return all_courses_student_attendance;
	}
	
	public List<Map<String,Object>> studentAttendanceDetails(Integer student_id,Integer year_or_sem){
		List<Map<String,Object>> student_attendace;
			student_attendace=student_attendance_repository.studentAttendanceDetails(student_id, year_or_sem);
		return student_attendace;
	}
	
	
	public List<Map<String, Object>> getPresentAbsentData(Integer student_id) {
		
		StudentDetailsAttendanceDto studentDetails = student_details_repo.studentDetailForAttendanceForMobile(student_id);
		String programTypeForCurrentYearOrSem=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(studentDetails.getProgramAssignmentId());
		List<Integer> sectionAssignmentIds;
		List<Integer> batchAssignmentIds;
		Integer courseAssignmentId;
		Integer currentYearORSem;
		if(programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")) {
			sectionAssignmentIds=allAssignedSectionAssignemtOfStudentWithoutAcademicYear(student_id,studentDetails.getProgramAssignmentId(),studentDetails.getProgramId(),studentDetails.getProgramSpecializationId(),studentDetails.getCurrentSem());
			batchAssignmentIds=batchAssignmentRepository.allBatchAssignedOfStudentWithoutAcademicYear(student_id,studentDetails.getCurrentSem());
//			if(course_assignment_id == null) {
//			courseAssignmentId = courseAssignmentRepository.getCourseAssignmentIdWithoutAcademicYear(studentDetails.getProgramAssignmentId(), studentDetails.getProgramId(),
//					studentDetails.getProgramSpecializationId(), studentDetails.getCurrentSem(),course_id);
//			}
			currentYearORSem=studentDetails.getCurrentSem();
		}else {
			sectionAssignmentIds=allAssignedSectionAssignemtOfStudentWithoutAcademicYear(student_id,studentDetails.getProgramAssignmentId(),studentDetails.getProgramId(),studentDetails.getProgramSpecializationId(),studentDetails.getCurrentYear());
			batchAssignmentIds=batchAssignmentRepository.allBatchAssignedOfStudentWithoutAcademicYear(student_id,studentDetails.getCurrentYear());
//			if(course_assignment_id == null) {
//			courseAssignmentId = courseAssignmentRepository.getCourseAssignmentIdWithoutAcademicYear(studentDetails.getProgramAssignmentId(), studentDetails.getProgramId(),
//					studentDetails.getProgramSpecializationId(), studentDetails.getCurrentYear(),course_id);
//			}
			currentYearORSem=studentDetails.getCurrentYear();
		}
//		Map<String,Object> present_absent_count=new HashMap<>();
//		Map<String,Object> count = student_attendance_repository.getPresentAbsentCount(student_id, course_id);
		List<Map<String, Object>> courseDetails=student_attendance_repository.courseDetails(student_id,sectionAssignmentIds,batchAssignmentIds);
		List<Map<String, Object>> result=new ArrayList<>();
		courseDetails.stream().forEach(e -> {
			Map<String,Object> course=modelMapper.map(e,new TypeToken<Map<String, Object>>(){}.getType());
			List<Map<String, Object>> attendance = student_attendance_repository.getPresentAbsentData(student_id,(Integer) e.get("course_id"),sectionAssignmentIds,batchAssignmentIds,(Integer)e.get("course_assignment_id"));
			course.put("details", attendance);
			result.add(course);
		});
		return result;
	}
	
	private List<Integer> allAssignedSectionAssignemtOfStudentWithoutAcademicYear(Integer studentId, Integer programAssignmentId,
			Integer programId, Integer programSpecializationId, Integer currentYearSem) {
		String programTypeForCurrentYearOrSem=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(programAssignmentId);
		if(programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")) {
        	return sectionAssignmentRepository.allAssignedSectionAssignemtOfStudentWithoutAcademicYearBySem(studentId,programAssignmentId,programId,programSpecializationId,currentYearSem);
        	
        }else {
        	return sectionAssignmentRepository.allAssignedSectionAssignemtOfStudentWithoutAcademicYearByYear(studentId,programAssignmentId,programId,programSpecializationId,currentYearSem);

        }
		
		
	}
	
	public Boolean getCheckPresentOrAbsentForFeedback(Integer student_id,Integer time_table_id, Integer time_slots_id){
			Boolean student_attendance = student_attendance_repository.getCheckPresentOrAbsentForFeedback(student_id, time_table_id, time_slots_id);			
		return student_attendance; 
	}
	
	public List<Map<String, Object>> getFacultiesForFeedback(Integer student_id){
		List<Map<String, Object>> faculties = student_attendance_repository.getFacultiesForFeedback(student_id);			
		return faculties; 
	}
	
	public List<Map<String, Object>> getAssignedCoursesDetails(Integer user_id) {
		return s_repo.getAssignedCoursesDetails(user_id);	
	}

	public List<Map<String, Object>> getCourseDetailsForAttendanceReport(Integer ac_year_id,Integer program_assignment_id,Integer program_id,Integer current_year_sem){
		return student_attendance_repository.getCourseDetailsForAttendanceReport(ac_year_id,program_assignment_id,program_id,current_year_sem);
	}

		public List<Map<String, Object>> getAttendanceReportForStudentProfileByStudentId(Integer studentId, Integer year_or_sem){
		
		ReportingStudents studentReportingDetails=reporting_student_repo.getDetailsOfReportingStudentsByStudentId(studentId);
	
		if(studentReportingDetails == null) {
			throw new ResourceNotFoundException("Reporting Details Of Student Not Found:" + studentId);
		}
		Student_Details studentDetails=studentDetailsService.get(studentId);
		Integer currentAcademicYearId=academicYearRepository.getCurrentAcademicYearId();
//		List<Integer> sectionAssignmentIds =allAssignedSectionAssignemtOfStudent(studentId,currentAcademicYearId,studentDetails.getProgram_assignment_id(),studentDetails.getProgram_id(),studentDetails.getProgram_specialization_id(),year_or_sem);
		List<Integer> sectionAssignmentIds =allAssignedSectionAssignemtOfStudentWithoutAcademicYear(studentId,studentDetails.getProgram_assignment_id(),studentDetails.getProgram_id(),studentDetails.getProgram_specialization_id(),year_or_sem);
//		List<Integer> batchAssignmentIds=batchAssignmentRepository.allBatchAssignedOfStudent(studentId,currentAcademicYearId,year_or_sem);
		List<Integer> batchAssignmentIds=batchAssignmentRepository.allBatchAssignedOfStudentWithoutAcademicYear(studentId,year_or_sem);
		System.out.println("currentAcademicYearId " +currentAcademicYearId);
		String programType=programAssigmentRepository.getProgramTypeByProgramAssignmentId(studentDetails.getProgram_assignment_id());
//		List<Map<String, Object>> studentAttendanceDetail = student_attendance_repository.studentAttendanceDetailByStudentIdAcademicYearAndCurrentYearSem(studentId, currentAcademicYearId,sectionAssignmentIds, batchAssignmentIds, year_or_sem);
		List<Map<String, Object>> studentAttendanceDetail = student_attendance_repository.studentAttendanceDetailByStudentIdAndCurrentYearSem(studentId,sectionAssignmentIds, batchAssignmentIds, year_or_sem);
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA "+sectionAssignmentIds);
		System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBB "+batchAssignmentIds);
		List<Map<String, Object>> studentAttendanceDetailWithTotal=new ArrayList<>();
		
		
		studentAttendanceDetail.stream().forEach(sad -> {

			if(programType.equalsIgnoreCase("Semester")) {
				try {
					Integer totalClassCount=timeTableRepo.totalClassOfCourseBySemBySectionAssignmentAndBatchAssignment(currentAcademicYearId,sectionAssignmentIds,batchAssignmentIds,year_or_sem,(Integer)sad.get("course_id"));
					Map<String, Object> sadMapper=modelMapper.map(sad, Map.class);
					sadMapper.put("total", totalClassCount);
					Integer persentCount=((BigDecimal)sad.get("present")).intValue();
					double persentRatio=(double)persentCount/totalClassCount ;
					int percentage=(int)Math.round(persentRatio * 100);
					sadMapper.put("percentage", percentage);
					studentAttendanceDetailWithTotal.add(sadMapper);
					Condonation condonationDetail=condotionRepository.findByStudentIdAndCourseIdAndStatus(studentId,(Integer)sad.get("course_id"),Condonation.Status.APPROVED);
					sadMapper.put("condonationDetail", condonationDetail);
				}catch(Exception e){
					log.info("Error {}",e.getMessage());
				}
			}else {
				try {
					Integer totalClassCount=timeTableRepo.totalClassOfCourseByYearBySectionAssignmentAndBatchAssignment(currentAcademicYearId,sectionAssignmentIds,batchAssignmentIds,year_or_sem,(Integer)sad.get("course_id"));
					Map<String, Object> sadMapper=modelMapper.map(sad, Map.class); 
					sadMapper.put("total", totalClassCount);
					sadMapper.put("percentage", (Integer)Math.round(((Integer)sad.get("present")/totalClassCount)*100));
					studentAttendanceDetailWithTotal.add(sadMapper);
					Condonation condonationDetail=condotionRepository.findByStudentIdAndCourseIdAndStatus(studentId,(Integer)sad.get("course_id"),Condonation.Status.APPROVED);
					sadMapper.put("condonationDetail", condonationDetail);
				}catch(Exception e){
					log.info("Error {}",e.getMessage());
				}
			}
		});
		
		return studentAttendanceDetailWithTotal;
	}
	
		
		private List<Integer> allAssignedSectionAssignemtOfStudent(Integer studentId,Integer acYearId, Integer programAssignmentId,
				Integer programId, Integer programSpecializationId, Integer currentYearSem) {
			String programTypeForCurrentYearOrSem=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(programAssignmentId);
			if(programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")) {
	        	return sectionAssignmentRepository.allAssignedSectionAssignemtOfStudentBySem(studentId,acYearId,programAssignmentId,programId,programSpecializationId,currentYearSem);
	        	
	        }else {
	        	return sectionAssignmentRepository.allAssignedSectionAssignemtOfStudentByYear(studentId,acYearId,programAssignmentId,programId,programSpecializationId,currentYearSem);

	        }
			
			
		}

		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> studentAttendanceReportSectionwise(Integer acYearId, Integer programAssignmentId, Integer programId,
		        Integer programSpecializationId, Integer sectionId, Integer currentYearSem) {

		    List<Map<String, Object>> studentAttendanceReport = new ArrayList<>();
//		    Map<String, Object> sectionAssignmentDetails = sectionAssignemntDetails(acYearId, programAssignmentId, programId, programSpecializationId, sectionId, currentYearSem);
			Map<String, Object> sectionAssignmentDetails = sectionAssignemntDetails(acYearId, programAssignmentId, programId, programSpecializationId, sectionId, currentYearSem);
		    String studentIds = sectionAssignmentDetails.get("studentIdsInStringForSection").toString();
		    List<Integer> sectionAssignmentIds = (List<Integer>) sectionAssignmentDetails.get("sectionAssignmentIds");
		    String programTypeForCurrentYearOrSem = programAssigmentRepository.getProgramTypeOnProgramAssignmentId(programAssignmentId);
		    Date commencementDate = classCommencementDetailsRepository.getCommencementDateByAcademicYearIdSpecializationAndYearSem(acYearId, programSpecializationId, currentYearSem);
		    List<Map<String, Object>> validStudentAttendanceReport = new ArrayList<>(); // List to hold only valid student data

		    // Iterate over each student ID
		    ResponseHandler.toConvertCommaSeperatedIdsAsList(studentIds).stream().distinct().forEachOrdered(sd -> {
		        Map<String, Object> studentFinalData = new HashMap<>();
		        StudentDetailsAttendanceDto studentDetails = student_details_repo.studentDetailForAttendance(sd);

		        if (studentDetails == null) {
		            return;  // Skip this student and continue with the next one
		        }
		        List<HashMap<String, Object>> proctorDetails = proctorStudentAssignmentRepository.getAssignedProctorDetailsByStudentId(sd);
		        List<Map<String, Object>> studentAttendanceDetail = student_attendance_repository.studentAttendanceDetailReportForSection(sd, acYearId, currentYearSem);
		        List<Map<String, Object>> studentAttendanceDetailWithTotal = new ArrayList<>();
				List<Integer> batchAssignmentIds=batchAssignmentRepository.allBatchAssignedOfStudent(sd,acYearId,currentYearSem);
		        studentAttendanceDetail.forEach(sad -> {
					Integer totalClassCount=null;
		            if (programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")) {
		                try {
//							totalClassCount = timeTableRepo.totalClassOfCourseBySemAndSectionAssignemntId(acYearId, sectionAssignmentIds,batchAssignmentIds, currentYearSem, (Integer) sad.get("course_id"));
							totalClassCount = timeTableRepo.totalClassOfCourseBySem(acYearId, sectionAssignmentIds,batchAssignmentIds, currentYearSem, (Integer) sad.get("course_id"),(Integer) sad.get("course_assignment_id"));
							Map<String, Object> sadMapper = modelMapper.map(sad, Map.class);
							if(totalClassCount != 0) {
								sadMapper.put("total", totalClassCount);
								Integer presentCount = ((BigDecimal) sad.get("present")).intValue();
								double presentRatio = (double) presentCount / totalClassCount;
								int percentage = (int) Math.round(presentRatio * 100);
								sadMapper.put("percentage", percentage);
							}else {
								sadMapper.put("total", 0);
								sadMapper.put("percentage", 0);
								sadMapper.put("present", 0);
							}
		                    studentAttendanceDetailWithTotal.add(sadMapper);

		                    Condonation condonationDetail = condotionRepository.findByStudentIdAndCourseIdAndStatus(sd, (Integer) sad.get("course_id"), Condonation.Status.APPROVED);
		                    sadMapper.put("condonationDetail", condonationDetail != null ? condonationDetail : null);

		                } catch (Exception e) {
		                    throw new RuntimeException("No data available for attendance detail: " + e.getMessage());
		                }
		            } else {
		                try {
//		                    totalClassCount = timeTableRepo.totalClassOfCourseByYearAndSectionAssignemntId(acYearId, sectionAssignmentIds, currentYearSem, (Integer) sad.get("course_id"));
		                    totalClassCount = timeTableRepo.totalClassOfCourseByYear(acYearId, sectionAssignmentIds ,batchAssignmentIds, currentYearSem, (Integer) sad.get("course_id"),(Integer) sad.get("course_assignment_id"));
							Map<String, Object> sadMapper = modelMapper.map(sad, Map.class);
							if(totalClassCount != 0) {
								sadMapper.put("total", totalClassCount);
								sadMapper.put("percentage", (int) Math.round(((Integer) sad.get("present") / totalClassCount) * 100));
							}else {
								sadMapper.put("total", 0);
								sadMapper.put("percentage", 0);
								sadMapper.put("present", 0);
							}
		                    studentAttendanceDetailWithTotal.add(sadMapper);

		                    Condonation condonationDetail = condotionRepository.findByStudentIdAndCourseIdAndStatus(sd, (Integer) sad.get("course_id"), Condonation.Status.APPROVED);
		                    sadMapper.put("condonationDetail", condonationDetail != null ? condonationDetail : null);

		                } catch (Exception e) {
		                    throw new RuntimeException("No data available for attendance detail: " + e.getMessage());
		                }
		            }
		        });

		        studentFinalData.put("studentName", studentDetails.getStudentName());
		        studentFinalData.put("auid", studentDetails.getAuid());
		        studentFinalData.put("proctorName", proctorDetails);
		        studentFinalData.put("dateOfReporting", studentDetails.getReportingDate());
		        studentFinalData.put("studentAttendanceDetail", studentAttendanceDetailWithTotal);
		        studentFinalData.put("classCommencementDate", commencementDate);
		        studentFinalData.put("sectionAssignmentIds", sectionAssignmentIds);

		        // Add student data to final report list only if details are valid
		        validStudentAttendanceReport.add(studentFinalData);
		    });

		    // If no valid student data found, throw exception
		    if (validStudentAttendanceReport.isEmpty()) {
		        throw new RuntimeException("No attendance data available for students.");
		    }

		    // Return only students with valid details
		    return validStudentAttendanceReport;
		}

//	@SuppressWarnings("unchecked")
//	public List<Map<String, Object>> studentAttendanceReportSectionwise(Integer acYearId, Integer programAssignmentId, Integer programId,
//																		Integer programSpecializationId, Integer sectionId, Integer currentYearSem) {
//
//		List<Map<String, Object>> studentAttendanceReport = new ArrayList<>();
//		Map<String, Object> sectionAssignmentDetails = sectionAssignemntDetails(acYearId, programAssignmentId, programId, programSpecializationId, sectionId, currentYearSem);
//		String studentIds = sectionAssignmentDetails.get("studentIdsInStringForSection").toString();
//		List<Integer> sectionAssignmentIds = (List<Integer>) sectionAssignmentDetails.get("sectionAssignmentIds");
//		String programTypeForCurrentYearOrSem = programAssigmentRepository.getProgramTypeOnProgramAssignmentId(programAssignmentId);
//		Date commencementDate = classCommencementDetailsRepository.getCommencementDateByAcademicYearIdSpecializationAndYearSem(acYearId, programSpecializationId, currentYearSem);
//		List<Map<String, Object>> validStudentAttendanceReport = new ArrayList<>();
//		List<Integer> students=ResponseHandler.toConvertCommaSeperatedIdsAsList(studentIds);
//		List<StudentDetailsAttendanceDto> allStudentDetails = student_details_repo.studentDetailForAttendanceBulk(students);
//		Map<Integer, List<StudentDetailsAttendanceDto>> groupByStudent=allStudentDetails.stream().collect(Collectors.groupingBy(StudentDetailsAttendanceDto::getStudentId));
//
//		List<HashMap<String, Object>>  proctors= proctorStudentAssignmentRepository.getAssignedProctorDetailsByStudentIds(students);
//
//		Map<Integer, List<HashMap<String, Object>>> proctorDetails=Optional.ofNullable(proctors).orElse(new ArrayList<>()).stream()
//				.collect(Collectors.groupingBy(detail -> (Integer) detail.get("student_id")));
//
//		List<Map<String, Object>> studentAttendanceDetail = student_attendance_repository.studentAttendanceDetailReportForSectionBulk(students, acYearId, currentYearSem, sectionAssignmentIds);
//
//		Map<Integer , List<Map<String, Object>>> allAttendanceDetails=Optional.ofNullable(studentAttendanceDetail).orElse(new ArrayList<>()).stream()
//				.collect(Collectors.groupingBy(detail -> (Integer) detail.get("student_id")));
//
//		allAttendanceDetails.entrySet().parallelStream().forEachOrdered(sd -> {
//			Map<String, Object> studentFinalData = new HashMap<>();
//
//
//			List<Map<String, Object>> studentAttendanceDetailWithTotal = new ArrayList<>();
//
//			sd.getValue().forEach(sad -> {
//					try {
//						Integer totalClassCount = programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")
//								? timeTableRepo.totalClassOfCourseBySemAndSectionAssignemntIds(acYearId, sectionAssignmentIds, currentYearSem, (Integer) sad.get("course_id"))
//								:timeTableRepo.totalClassOfCourseByYearAndSectionAssignemntIds(acYearId, sectionAssignmentIds, currentYearSem, (Integer) sad.get("course_id"));
//						Map<String, Object> sadMapper = modelMapper.map(sad, Map.class);
//						sadMapper.put("total", totalClassCount);
//						Integer presentCount = ((BigDecimal) sad.get("present")).intValue();
//						double presentRatio = (double) presentCount / totalClassCount;
//						int percentage = (int) Math.round(presentRatio * 100);
//						sadMapper.put("percentage", percentage);
//						studentAttendanceDetailWithTotal.add(sadMapper);
//
//						Condonation condonationDetail = condotionRepository.findByStudentIdAndCourseIdAndStatus(sd.getKey(), (Integer) sad.get("course_id"), Condonation.Status.APPROVED);
//						sadMapper.put("condonationDetail", condonationDetail != null ? condonationDetail : null);
//
//					} catch (Exception e) {
//						throw new RuntimeException("No data available for attendance detail: " + e.getMessage());
//					}
//			});
//
//			studentFinalData.put("studentName", groupByStudent.get(sd.getKey()).get(0).getStudentName());
//			studentFinalData.put("auid", groupByStudent.get(sd.getKey()).get(0).getAuid());
//			studentFinalData.put("proctorName", proctorDetails.get(sd.getKey()));
//			studentFinalData.put("dateOfReporting", groupByStudent.get(sd.getKey()).get(0).getReportingDate());
//			studentFinalData.put("studentAttendanceDetail", studentAttendanceDetailWithTotal);
//			studentFinalData.put("classCommencementDate", commencementDate);
//			studentFinalData.put("sectionAssignmentIds", sectionAssignmentIds);
//
//			// Add student data to final report list only if details are valid
//			validStudentAttendanceReport.add(studentFinalData);
//		});
//
//
//		return validStudentAttendanceReport;
//	}

		
		private Map<String, Object> sectionAssignemntDetails(Integer acYearId, Integer programAssignmentId,
				Integer programId, Integer programSpecializationId, Integer sectionId, Integer currentYearSem) {
			Map<String,Object> responseDetail=new HashMap<String,Object>();
			StringBuilder studentIdsInStringForSection=null;
			List<Integer> sectionAssignmentIds=null;
			String programTypeForCurrentYearOrSem=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(programAssignmentId);
			if(programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")) {
//	        	List<SectionAssignment> sectionAssignmentDetail=sectionAssignmentRepository.getSectionAssignedStudentDetailsBySem(acYearId,programAssignmentId,programId,programSpecializationId,sectionId,currentYearSem);
	        	List<SectionAssignment> sectionAssignmentDetail=sectionAssignmentRepository.sectionAssignedStudentDetailsBySem(acYearId,programId,programSpecializationId,sectionId,currentYearSem);
	        	if (sectionAssignmentDetail != null) {
	        		String concatenatedStudentIds= sectionAssignmentDetail.stream().map(SectionAssignment::getStudent_ids).collect(Collectors.joining(","));
	        		studentIdsInStringForSection=new StringBuilder(concatenatedStudentIds);
	        		sectionAssignmentIds=sectionAssignmentDetail.stream().map(SectionAssignment::getSection_assignment_id).collect(Collectors.toList());
	    			
	    			
	    		}
	        }else {
//	        	List<SectionAssignment> sectionAssignmentDetail=sectionAssignmentRepository.getSectionAssignedStudentDetailsByYear(acYearId,programAssignmentId,programId,programSpecializationId,sectionId,currentYearSem);
	        	List<SectionAssignment> sectionAssignmentDetail=sectionAssignmentRepository.sectionAssignedStudentDetailsByYear(acYearId,programId,programSpecializationId,sectionId,currentYearSem);
	        	if (sectionAssignmentDetail != null) {
	        		String concatenatedStudentIds= sectionAssignmentDetail.stream().map(SectionAssignment::getStudent_ids).collect(Collectors.joining(","));
	        		studentIdsInStringForSection=new StringBuilder(concatenatedStudentIds);
	        		sectionAssignmentIds=sectionAssignmentDetail.parallelStream().map(SectionAssignment::getSection_assignment_id).collect(Collectors.toList());
	    		} else {
	    			throw new ResourceNotFoundException("Section Assignment Details Not Found");
	    		}
	        }
			

			if(studentIdsInStringForSection.toString().isEmpty()) {
				throw new ResourceNotFoundException("Student Is Not Assigned In Any section For Academic Year ,Specialization , Batch And Current_Year_Sem ");
			} 
			
			responseDetail.put("studentIdsInStringForSection", studentIdsInStringForSection.toString());
			responseDetail.put("sectionAssignmentIds", sectionAssignmentIds);
			return responseDetail;
		}
		
		public List<Map<String,Object>> studentAttendanceDetailsForReport( Integer time_table_id) {
			return student_attendance_repository.studentAttendanceDetailsForReport(time_table_id);
	}

		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> getDetailedStudentAttendanceReportSectionwise(Integer ac_year_id,
				Integer program_assignment_id, Integer program_id, Integer program_specialization_id, Integer section_id,
				Integer current_year_sem, Integer course_id, Integer course_assignment_id) {
			List<Map<String, Object>> studentAttendanceReport = new ArrayList<>();

			Map<String, Object> sectionAssignmentDetails = sectionAssignemntDetails(ac_year_id, program_assignment_id,
					program_id, program_specialization_id, section_id, current_year_sem);
			String studentIds = sectionAssignmentDetails.get("studentIdsInStringForSection").toString();
			List<Integer> sectionAssignmentIds = (List<Integer>) sectionAssignmentDetails.get("sectionAssignmentIds");
			Date commencementDate = classCommencementDetailsRepository
					.getCommencementDateByAcademicYearIdSpecializationAndYearSem(ac_year_id, program_specialization_id,
							current_year_sem);

			ResponseHandler.toConvertCommaSeperatedIdsAsList(studentIds).stream().parallel().forEachOrdered(sd -> {
				Map<String, Object> studentFinalData = new HashMap<>();
				StudentDetailsAttendanceDto studentDetails = student_details_repo.studentDetailForAttendance(sd);
				List<HashMap<String, Object>> proctorDetails = proctorStudentAssignmentRepository
						.getAssignedProctorDetailsByStudentId(sd);

				Map<String, Object> studentAttendanceDetail = new HashMap<>();
				studentFinalData.put("studentName", studentDetails.getStudentName());
				studentFinalData.put("auid", studentDetails.getAuid());
				studentFinalData.put("proctorName", proctorDetails);
				studentFinalData.put("dateOfReporting", studentDetails.getReportingDate());
				studentFinalData.put("commencementDate", commencementDate);
				studentFinalData.put("studentAttendanceDetail", studentAttendanceDetail);
				studentFinalData.put("sectionAssignmentIds", sectionAssignmentIds);

				List<Map<String, Object>> studentAttendanceDetailOfCourse = student_attendance_repository
						.getDetailedStudentAttendanceReportOfCourse(sd, ac_year_id, current_year_sem, course_assignment_id,
								sectionAssignmentIds);
				Map<Boolean, List<Map<String, Object>>> forCountOfAbsent = studentAttendanceDetailOfCourse.stream()
						.collect(Collectors.partitioningBy(e -> (Boolean) e.get("present_status")));
				Integer absentDetailsCountOfStudent = absentDetailCountOfStudentForSetionWithCourseAssignmentId(
						ac_year_id, current_year_sem, sectionAssignmentIds,course_assignment_id, sd, section_id);
				if (ObjectUtils.isNotEmpty(studentAttendanceDetailOfCourse)) {
					if (forCountOfAbsent.get(false).size() == absentDetailsCountOfStudent) {
						studentAttendanceDetail.put(studentAttendanceDetailOfCourse.get(0)
								.get("course_name_with_course_assignment_code").toString(),
								studentAttendanceDetailOfCourse);
					} else {
						List<Map<String, Object>> attendanceReportWithTotalAbsent = new ArrayList<>();
						List<Map<String, Object>> absentDetailsOfStudent = absentDetailOfStudentForSetionWithCourseAssignmentId(
								ac_year_id, current_year_sem, sectionAssignmentIds, course_assignment_id, sd, section_id);
						attendanceReportWithTotalAbsent.addAll(forCountOfAbsent.get(true));
						absentDetailsOfStudent.parallelStream().forEachOrdered(ads -> {
							Map<String, Object> absentDetails = new HashMap<>();
							absentDetails.put("present_status", false);
							absentDetails.put("student_attendance_id", null);
							absentDetails.put("date_of_class", ads.get("selected_date"));
							absentDetails.put("year_or_sem", current_year_sem);
							absentDetails.put("time_slot", ads.get("time_slot"));
							absentDetails.put("time_slots_id", ads.get("time_slots_id"));
							absentDetails.put("course_id", ads.get("course_id"));
							absentDetails.put("course_assignment_coursecode", ads.get("course_assignment_coursecode"));
							absentDetails.put("course_name", ads.get("course_name"));
							absentDetails.put("time_table_id", ads.get("time_table_id"));
							absentDetails.put("student_id", sd);
							absentDetails.put("ac_year_id", ac_year_id);
							absentDetails.put("course_assignment_id", course_assignment_id);
							absentDetails.put("course_name_with_course_assignment_code",
									ads.get("course_name_with_course_assignment_code"));
							absentDetails.put("employee_name", ads.get("employee_name"));
							attendanceReportWithTotalAbsent.add(absentDetails);
						});
						studentAttendanceDetail.put(studentAttendanceDetailOfCourse.get(0)
								.get("course_name_with_course_assignment_code").toString(),
								attendanceReportWithTotalAbsent);
					}
				}
				
				studentAttendanceReport.add(studentFinalData);
			});
			return studentAttendanceReport;
		}

		public List<Map<String, Object>> absentDetailOfStudentForSetionWithCourseAssignmentId(Integer acYearId,Integer currentYearOrSem,List<Integer> sectionAssignmentIds,Integer courseAssignmentId,Integer studentId,Integer sectionId) {
			List<Integer> programAssignmentIds=sectionAssignmentRepository.programAssignmentIdsBySectionAssignmentId(sectionAssignmentIds);
			List<Integer> presentStatusTimeTableIds=student_attendance_repository.attendanceTakenTimeTableIdsBySectionWithCourseAssignmentId(acYearId,currentYearOrSem,courseAssignmentId,studentId,sectionId);
			String programTypeForCurrentYearOrSem=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(programAssignmentIds.get(0));
			if(programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")) {
				return timeTableRepo.absentDetailOfStudentForSetionBySemWithCourseAssignemtId(acYearId,currentYearOrSem,sectionAssignmentIds,courseAssignmentId,presentStatusTimeTableIds);
			}else {
				return timeTableRepo.absentDetailOfStudentForSetionByYearWithCourseAssignemtId(acYearId,currentYearOrSem,sectionAssignmentIds,courseAssignmentId,presentStatusTimeTableIds);
			}
			
		}

		public Integer absentDetailCountOfStudentForSetionWithCourseAssignmentId(Integer acYearId,Integer currentYearOrSem,List<Integer> sectionAssignmentIds,Integer courseAssignmentId,Integer studentId,Integer sectionId) {
			List<Integer> programAssignmentIds=sectionAssignmentRepository.programAssignmentIdsBySectionAssignmentId(sectionAssignmentIds);
			List<Integer> presentStatusTimeTableIds=student_attendance_repository.attendanceTakenTimeTableIdsBySectionWithCourseAssignmentId(acYearId,currentYearOrSem,courseAssignmentId,studentId,sectionId);
			if(ObjectUtils.isEmpty(presentStatusTimeTableIds)) {
				presentStatusTimeTableIds.add(VALUE_FOR_IN_QUERY);
			}
			String programTypeForCurrentYearOrSem=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(programAssignmentIds.get(0));
			if(programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")) {
				return timeTableRepo.absentDetailCountOfStudentForSetionBySemWithCourseAssignemtId(acYearId,currentYearOrSem,sectionAssignmentIds,courseAssignmentId,presentStatusTimeTableIds);
			}else {
				return timeTableRepo.absentDetailCountOfStudentForSetionByYearWithCourseAssignemtId(acYearId,currentYearOrSem,sectionAssignmentIds,courseAssignmentId,presentStatusTimeTableIds);
			}
			
		}

		private String studentIdsInString=null;
		public List<Map<String, Object>> getStudentAttendanceReportSectionwise(Integer ac_year_id,Integer program_assignment_id,Integer program_id,
				Integer program_specialization_id,Integer section_id,Integer current_year_sem){
			List<Map<String, Object>> studentAttendanceReport=new ArrayList<>();   

			List<SectionAssignment> sectionAssignmentDetail=sectionAssignmentRepository.getSectionAssignedStudentDetails(ac_year_id,program_assignment_id,program_id,program_specialization_id,section_id,current_year_sem);
			Date commencementDate=classCommencementDetailsRepository.getCommencementDateByAcademicYearIdSpecializationAndYearSem(ac_year_id,program_specialization_id,current_year_sem);
			if (sectionAssignmentDetail != null) {
				sectionAssignmentDetail.parallelStream().forEach(sad -> {
					if (sad.getStudent_ids() != null) {
						if(studentIdsInString == null) {
							studentIdsInString="";
							studentIdsInString = studentIdsInString + sad.getStudent_ids();
						} else {
							studentIdsInString = studentIdsInString + sad.getStudent_ids();
						}
						
					}
				});
			} else {
				throw new ResourceNotFoundException("Section Assignment Details Not Found");
			}
			
			List<Integer> studentIds=new ArrayList<>();
			if(studentIdsInString != null) {
				studentIds=ResponseHandler.toConvertCommaSeperatedIdsAsList(studentIdsInString).stream().distinct().collect(Collectors.toList());
			} else {
				throw new ResourceNotFoundException("Student Is Not Assigned In Any Section This Academic Year,Program ,Specialization , Section And Current_Year_Sem ");
			}
			

			studentIds.stream().parallel().forEach(sd -> {
				Map<String, Object> studentFinalData=new HashMap<>();
				Student_Details studentDetails=studentDetailsService.get(sd);
				ReportingStudents reporting_student=reporting_student_repo.getDetailsOfReportingStudentsByStudentId(sd);
				List<HashMap<String, Object>> proctorDetails=proctorStudentAssignmentRepository.getAssignedProctorDetailsByStudentId(sd);
//				List<Map<String, Object>> studentAttendanceDetail=student_attendance_repository.studentAttendanceDetailByAcademicYearReportingDateAndCurrentYearSem(sd,ac_year_id,reporting_student.getReporting_date(),current_year_sem);
				List<Map<String, Object>> studentAttendanceDetail=student_attendance_repository.studentAttendanceDetailByStudentIdAcademicYearCurrentYearSemAndCommencementDate(sd,ac_year_id,current_year_sem,commencementDate);
				studentAttendanceDetail.parallelStream().forEach(sad -> {
					Integer totalAttendanceCount=student_attendance_repository.totalAttendanceCountOfCourseByCourseAcademicYearAndCurrentYearSem((Integer)sad.get("course_id"),ac_year_id,current_year_sem);
				});
				studentFinalData.put("studentName", studentDetails.getStudent_name());
				studentFinalData.put("auid", studentDetails.getAuid());
				studentFinalData.put("proctorName", proctorDetails);
				studentFinalData.put("dateOfReporting", reporting_student.getReporting_date());
				studentFinalData.put("studentAttendanceDetail", studentAttendanceDetail);
				studentAttendanceReport.add(studentFinalData);
			});
			studentIdsInString=null;
			return studentAttendanceReport;
		}

		public ResponseEntity<Object> studentDetailsForAttendanceByTimeTable(Integer timeTableId) {
			try {
				List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
				TimeTable timeTable=timeTableRepo.findById(timeTableId).get();
				if(ObjectUtils.isNotEmpty(timeTable) && ObjectUtils.isEmpty(timeTable.getAttendance_status())) {
					String programType=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(timeTable.getProgram_assignment_id());
					Predicate<Map<String,Object>> checkReportedStatusBySem=e -> (Integer)e.get("current_sem") == 1 ? e.get("reporting_date") != null: (Integer)e.get("current_sem") >1 ;
					Predicate<Map<String,Object>> checkReportedStatusByYear=e -> (Integer)e.get("current_year") == 1 ? e.get("reporting_date") != null: (Integer)e.get("current_year") >1 ;
					if(ObjectUtils.isNotEmpty(timeTable.getSection_assignment_id())) {
						List<HashMap<String, Object>> studentDetailsBySection=sectionAssignmentService.assignedStudentDetailsBySectionAssignmentId(timeTable.getSection_assignment_id());
						if(programType.equalsIgnoreCase("Semester"))
							result=studentDetailsBySection.stream().filter(checkReportedStatusBySem::test).collect(Collectors.toList());
						else
							result=studentDetailsBySection.stream().filter(checkReportedStatusByYear::test).collect(Collectors.toList());
						return ResponseHandler.generateResponse(true,HttpStatus.OK, result);
					}else {
						List<HashMap<String,Object>>  studentDetailsByBatch=batchAssignmentService.assignedStudentDetailsByBatchAssignmentId(timeTable.getBatch_assignment_id());
						if(programType == null)
							programType=batchProgramAssignmentRepository.getProgramTypeByBatchAssignmentId(timeTable.getBatch_assignment_id());
						if(programType.equalsIgnoreCase("Semester"))
							result=studentDetailsByBatch.stream().filter(checkReportedStatusBySem::test).collect(Collectors.toList());
						else
							result=studentDetailsByBatch.stream().filter(checkReportedStatusByYear::test).collect(Collectors.toList());
						return ResponseHandler.generateResponse(true,HttpStatus.OK, result);
					}
				}else {
					return ResponseHandler.generateResponse(true,HttpStatus.OK, "Time Table is not Found Or Attendance is already is taken");
				}

			} catch(Exception e) {
				return ResponseHandler.generateResponse(false,
						HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}

		public List<Map<String, Object>> getDetailedStudentAttendanceOfStudentByCourse(Integer student_id,Integer ac_year_id,Integer course_assignment_id,Integer course_id,Integer current_year_sem){
			 return student_attendance_repository.getDetailedStudentAttendanceOfStudentByCourse(student_id,ac_year_id,course_assignment_id,course_id,current_year_sem);
			
		}
		
		public Map<String, Object> getAttendanceData(Integer student_id,Integer course_id,Integer year_or_sem) {
			
			StudentDetailsAttendanceDto studentDetails = student_details_repo.studentDetailForAttendanceForMobile(student_id);
			String programTypeForCurrentYearOrSem=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(studentDetails.getProgramAssignmentId());
			List<Integer> sectionAssignmentIds;
			List<Integer> batchAssignmentIds;
			Integer courseAssignmentId;
			Integer currentYearORSem;
			if(programTypeForCurrentYearOrSem.equalsIgnoreCase("Semester")) {
				sectionAssignmentIds=allAssignedSectionAssignemtOfStudentWithoutAcademicYear(student_id,studentDetails.getProgramAssignmentId(),studentDetails.getProgramId(),studentDetails.getProgramSpecializationId(),year_or_sem);
				batchAssignmentIds=batchAssignmentRepository.allBatchAssignedOfStudentWithoutAcademicYear(student_id,year_or_sem);
//				if(course_assignment_id == null) {
//				courseAssignmentId = courseAssignmentRepository.getCourseAssignmentIdWithoutAcademicYear(studentDetails.getProgramAssignmentId(), studentDetails.getProgramId(),
//						studentDetails.getProgramSpecializationId(), studentDetails.getCurrentSem(),course_id);
//				}
				currentYearORSem=studentDetails.getCurrentSem();
			}else {
				sectionAssignmentIds=allAssignedSectionAssignemtOfStudentWithoutAcademicYear(student_id,studentDetails.getProgramAssignmentId(),studentDetails.getProgramId(),studentDetails.getProgramSpecializationId(),year_or_sem);
				batchAssignmentIds=batchAssignmentRepository.allBatchAssignedOfStudentWithoutAcademicYear(student_id,year_or_sem);
//				if(course_assignment_id == null) {
//				courseAssignmentId = courseAssignmentRepository.getCourseAssignmentIdWithoutAcademicYear(studentDetails.getProgramAssignmentId(), studentDetails.getProgramId(),
//						studentDetails.getProgramSpecializationId(), studentDetails.getCurrentYear(),course_id);
//				}
				currentYearORSem=studentDetails.getCurrentYear();
			}
//			Map<String,Object> present_absent_count=new HashMap<>();
//			Map<String,Object> count = student_attendance_repository.getPresentAbsentCount(student_id, course_id);
//			List<Map<String, Object>> courseDetails=student_attendance_repository.courseDetails(student_id,sectionAssignmentIds,batchAssignmentIds);
			
//			Map<String, Object> course = course_repository.getCourseDetails(course_id);
			Map<String, Object> result=new HashMap<>();
				List<Map<String, Object>> attendance = student_attendance_repository.getAttendanceData(student_id, course_id, sectionAssignmentIds,batchAssignmentIds, year_or_sem);
				result.put("attendanceDetails", attendance);
//				result.put("courseDetails", course);
			return result;
		}

		public Map<Object, List<Map<String, Object>>> getDetailedStudentAttendanceReportSectionwiseForEmployee(Integer ac_year_id, Integer emp_id, Integer course_assignment_id, Integer section_id) {
			 List<Map<String, Object>> detailedStudentAttendanceReportSectionwiseForEmployee = student_attendance_repository.
					 getDetailedStudentAttendanceReportSectionwiseForEmployee(ac_year_id, emp_id, course_assignment_id, section_id);

			 Map<Object, List<Map<String, Object>>> collect = detailedStudentAttendanceReportSectionwiseForEmployee.stream().collect(Collectors.groupingBy(data -> data.get("date_of_class")));
			 return collect;
		}
	
}
