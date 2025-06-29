package com.au.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.Collectors;

import com.au.model.ClassCommencementDetails;
import com.au.model.Student_Details;
import com.au.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.au.controller.RateLimitController;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Section;
import com.au.model.SectionAssignment;
import com.au.response.ResponseHandler;

@Service
public class SectionAssignmentService {

	@Autowired
	private SectionAssignmentRepository sectionAssignmentRepository;
	
	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;
	
	@Autowired
	private Academic_year_repository academicYearRepository;
	
	@Autowired
	private ProgramAssigmentRepository programAssigmentRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private SectionService sectionService;

	@Autowired
	private StudentDueRepository studentDueRepository;

	@Autowired
	private StudentPermissionRepository studentPermissionRepository;

	@Autowired
	private ClassCommencementDetailsRepository classCommencementDetailsRepository;
	
	private final ModelMapper modelMapper = new ModelMapper();

	public List<SectionAssignment> listAll() {
		return sectionAssignmentRepository.findAll1();
	}

	public ResponseEntity<Object> fetchAllSectionAssignmentDetails1(Pageable pageable,  Integer ac_year_id,
			Integer school_id, Integer program_id, Integer program_specialization_id, Integer section_id,Integer current_year_sem,Object keyword) {
		List<Map<String,Object>> response1 = sectionAssignmentRepository.fetchAllSectionAssignmentDetails2(pageable,ac_year_id,school_id,program_id,
				program_specialization_id,section_id,current_year_sem, keyword);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllSectionAssignmentDetails2(Pageable pageable,Integer ac_year_id,
				Integer school_id, Integer program_id, Integer program_specialization_id, Integer section_id,Integer current_year_sem) {
		List<Map<String,Object>> response = sectionAssignmentRepository.fetchAllSectionAssignmentDetails3(pageable,ac_year_id,school_id,program_id,
				program_specialization_id,section_id,current_year_sem);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}
	
	public SectionAssignment saveSectionAssignment(SectionAssignment s) throws Exception{
		if(sectionAssignmentRepository.countOfAssignedSection(s.getAc_year_id(),s.getSchool_id(),s.getProgram_id(),s.getProgram_specialization_id(),s.getSection_id(),s.getCurrent_year_sem(),s.getProgram_assignment_id())>=1) {
			throw new RuntimeException("Section Assignment Already Existing Combination with Academic Year, School, Program, Program Specialization, Section and Current Year Or Sem ");
		}else {
			reportingStudentsRepository.updateSectionIdOfReportingStudents(s.getSection_id(),s.getStudent_ids());
			return sectionAssignmentRepository.save(s);
		}

	}

	public SectionAssignment updateSectionAssignment(SectionAssignment s,List<Integer> removed_student_ids) {
		if(sectionAssignmentRepository.countOfAssignedSectionForUpdate(s.getSection_assignment_id(),s.getAc_year_id(),s.getSchool_id(),s.getProgram_id(),s.getProgram_specialization_id(),s.getSection_id(),s.getCurrent_year_sem())>=1) {
			throw new RuntimeException("Section Assignment Already Existing with Academic Year, School, Program, Program Specialization, Section and Current Year Or Sem ");
		}else {
			reportingStudentsRepository.updateSectionIdNullOfReportingStudents(s.getSection_id(),removed_student_ids);
			return sectionAssignmentRepository.save(s);
		}

	}
	
	public SectionAssignment sectionAssignmentOfStudentFromIndex(SectionAssignment s) {
		reportingStudentsRepository.updateSectionIdOfReportingStudents(s.getSection_id(),s.getStudent_ids());
		return sectionAssignmentRepository.save(s);

	}

	public SectionAssignment get(Integer id) {
		return sectionAssignmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SectionAssignment Not Found:" + id));
	}

	public void delete(Integer id) {
		SectionAssignment cc = sectionAssignmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SectionAssignment Not Found:" + id));
		sectionAssignmentRepository.updateSectionAssignment(id);
	}

	public void delete1(Integer id) {
		SectionAssignment cc = sectionAssignmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SectionAssignment Not Found:" + id));
		sectionAssignmentRepository.updateSectionAssignment1(id);
	}

	public List<HashMap<String, Object>> getSectionNames(Integer ac_year_id, Integer school_id, Integer program_id,
			Integer program_specialization_id, Integer current_year_sem) {
		return sectionAssignmentRepository.getSectionName(ac_year_id,school_id,program_id,program_specialization_id,current_year_sem);
	}
	
	public SectionAssignment promotingOfAssinedSectionStudents(Integer section_assignment_id,String new_student_ids,JwtDetails jwtDetails) throws Exception{
		SectionAssignment section_assignment=get(section_assignment_id);
		Integer next_ac_year_id;
		if(programAssigmentRepository.getProgramTypeOnProgramAssignmentId(section_assignment.getProgram_assignment_id()).equalsIgnoreCase("Semester") && section_assignment.getCurrent_year_sem() %2==0) {
			next_ac_year_id=academicYearRepository.getNextAcademicYearId(section_assignment.getAc_year_id());
		}else if(programAssigmentRepository.getProgramTypeOnProgramAssignmentId(section_assignment.getProgram_id()).equalsIgnoreCase("Yearly")) {
			next_ac_year_id=academicYearRepository.getNextAcademicYearId(section_assignment.getAc_year_id());
		}else {
			next_ac_year_id=section_assignment.getAc_year_id();
		}
		
		if (sectionAssignmentRepository.countOfAssignedSection(next_ac_year_id, section_assignment.getSchool_id(),
				section_assignment.getProgram_id(), section_assignment.getProgram_specialization_id(),
				section_assignment.getSection_id(), section_assignment.getCurrent_year_sem() + 1,section_assignment.getProgram_assignment_id()) >= 1) {
			throw new RuntimeException(
					"Section Assignment Already Existing with Next Academic Year, School, Program, Program Specialization, Section and Increased Current Year Or Sem ");
		}else {

			SectionAssignment sa=new SectionAssignment();
			sa.setAc_year_id(next_ac_year_id);
			sa.setSection_id(section_assignment.getSection_id());
			sa.setSchool_id(section_assignment.getSchool_id());
			sa.setProgram_id(section_assignment.getProgram_id());
			sa.setProgram_specialization_id(section_assignment.getProgram_specialization_id());
			sa.setCurrent_year_sem(section_assignment.getCurrent_year_sem()+1);
			sa.setStudent_ids(new_student_ids);
			sa.setRemarks(section_assignment.getRemarks());
			sa.setCreated_by(jwtDetails.getUserId());
			sa.setCreated_username(jwtDetails.getUserName());
			sa.setProgram_assignment_id(section_assignment.getProgram_assignment_id());
			return sectionAssignmentRepository.save(sa);
		}
	}
	
	public List<Map<String, Object>> studentDetailsForPromoting(@PathVariable Integer section_assignment_id){
		SectionAssignment section_assignment=get(section_assignment_id);
		Integer next_ac_year_id = null;
		if(programAssigmentRepository.getProgramTypeOnProgramAssignmentId(section_assignment.getProgram_assignment_id()).equalsIgnoreCase("Semester") && section_assignment.getCurrent_year_sem() %2 ==0) {
			next_ac_year_id=academicYearRepository.getNextAcademicYearId(section_assignment.getAc_year_id());
			List<Map<String, Object>>  student_details_for_promoting = sectionAssignmentRepository.studentDetailsForPromotingOnSem(next_ac_year_id,section_assignment.getProgram_specialization_id(),section_assignment.getCurrent_year_sem()+1);
			return student_details_for_promoting;
		}else if(programAssigmentRepository.getProgramTypeOnProgramAssignmentId(section_assignment.getProgram_assignment_id()).equalsIgnoreCase("Yearly")) {
			next_ac_year_id=academicYearRepository.getNextAcademicYearId(section_assignment.getAc_year_id());
			List<Map<String, Object>>  student_details_for_promoting = sectionAssignmentRepository.studentDetailsForPromotingOnYear(next_ac_year_id,section_assignment.getProgram_specialization_id(),section_assignment.getCurrent_year_sem()+1);
			return student_details_for_promoting;
		}else {
			next_ac_year_id=section_assignment.getAc_year_id();
			List<Map<String, Object>>  student_details_for_promoting = sectionAssignmentRepository.studentDetailsForPromotingOnSem(next_ac_year_id,section_assignment.getProgram_specialization_id(),section_assignment.getCurrent_year_sem()+1);
			return student_details_for_promoting;
		}
			
	}
	
	Integer count=0;
	Integer count2;
	public Integer getCountOfStudentsAssignedToSection(Integer section_id) {
		
		List<SectionAssignment> list= sectionAssignmentRepository.getCountOfStudentsAssignedToSection(section_id);
		list.stream().forEach(l -> {
		String stud_ids = l.getStudent_ids();
		if(stud_ids != null) {
			String[] num =stud_ids.split(",");
			for(int i=0;i<num.length;i++) {
				count++;
			}
		}
		});
		count2=count;
		count=0;
		return count2;
	}

//	public List<HashMap<String,Object>> assignedStudentDetailsBySectionAssignmentId(Integer section_assignment_id) {
//		ZoneId fixedZone = ZoneId.of("Asia/Kolkata");
//		LocalDate todayDate = LocalDate.now(fixedZone);
//
//		SectionAssignment sectionAssignment=sectionAssignmentRepository.activeSectionAssignmentDetail(section_assignment_id);
//		if(sectionAssignment != null && StringUtils.isNotBlank(sectionAssignment.getStudent_ids())) {
//			List<Integer> studentIds = ResponseHandler.toConvertCommaSeperatedIdsAsList(sectionAssignment.getStudent_ids())
//					.parallelStream().distinct().collect(Collectors.toList());
//			List<Map<String,Object>>  reportedStudents=studentDetailsRepository.getAllReportedStudents(studentIds);
//			List<HashMap<String,Object>> responseWithDueStatus=new ArrayList<>();
//			reportedStudents.stream().forEach(st -> {
//				HashMap<String,Object> map=modelMapper.map(st,HashMap.class);
//				Boolean dueStatus=studentTillYearOrSemWithExemptDays((Integer)st.get("student_id"));
//
//
//			String permissionDate=studentPermissionRepository.getPermissionDate((String) st.get("auid"),(Integer)st.get("current_sem"),(Integer)st.get("current_year"));
//			if(dueStatus && StringUtils.isNotEmpty(permissionDate)){
//				map.put("due_status",false);
//				map.put("attendance_permitted_date",permissionDate);
////			}else{
////				Student_Details studentDetails = studentDetailsRepository.getStudentByAuid((String) st.get("auid"));
////				ClassCommencementDetails classCommencementDetails = classCommencementDetailsRepository
////						.getClassCommencementDetailsForStudentLateFee(studentDetails.getSchool_id(), studentDetails.getProgram_assignment_id(),
////							studentDetails.getProgram_specialization_id(), (Integer)st.get("current_sem") == 0 ?(Integer)st.get("current_sem") : (Integer)st.get("current_year"));
////				if (dueStatus && ObjectUtils.isNotEmpty(classCommencementDetails) &&
////						classCommencementDetails.getFrom_date() != null &&
////						(!classCommencementDetails.getFrom_date().toInstant()
////								.atZone(fixedZone)
////								.toLocalDate()
////								.isBefore(todayDate) ||
////						classCommencementDetails.getFrom_date().toInstant()
////								.atZone(fixedZone)
////								.toLocalDate()
////								.isEqual(todayDate))){
////					map.put("due_status", false);
////					map.put("attendance_permitted_date", classCommencementDetails.getFrom_date());
////				}else{
////				map.put("due_status",dueStatus);
////				map.put("attendance_permitted_date",null);
////				}
////			}
////			responseWithDueStatus.add(map);
////		});
////		return responseWithDueStatus;
//			}else{
//				String fromDate = classCommencementDetailsRepository
//						.getFromDateFromClassCommencementDetails((Integer) st.get("school_id"),(Integer) st.get("program_assignment_id"),
//								(Integer) st.get("program_specialization_id"), (Integer)st.get("current_sem") == 0 ?(Integer)st.get("current_year") : (Integer)st.get("current_sem"));
//				if (dueStatus && ObjectUtils.isNotEmpty(fromDate)){
//					map.put("due_status", false);
//					map.put("attendance_permitted_date", fromDate);
//				}else{
//				map.put("due_status",dueStatus);
//				map.put("attendance_permitted_date",null);
//				}
//			}
//			responseWithDueStatus.add(map);
//		});
//		return responseWithDueStatus;
//
//		}
//		return new ArrayList<HashMap<String,Object>>();
//	}
	
	public List<HashMap<String, Object>> assignedStudentDetailsBySectionAssignmentId(Integer sectionAssignmentId) {
	    ZoneId fixedZone = ZoneId.of("Asia/Kolkata");
	    LocalDate todayDate = LocalDate.now(fixedZone);

	    SectionAssignment sectionAssignment = sectionAssignmentRepository.activeSectionAssignmentDetail(sectionAssignmentId);
	    if (sectionAssignment == null || StringUtils.isBlank(sectionAssignment.getStudent_ids())) {
	        return Collections.emptyList();
	    }

	    List<Integer> studentIds = ResponseHandler
	            .toConvertCommaSeperatedIdsAsList(sectionAssignment.getStudent_ids())
	            .stream().distinct().collect(Collectors.toList());

	    List<Map<String, Object>> reportedStudents = studentDetailsRepository.getAllReportedStudents(studentIds);

	    Map<Integer, String> programTypeMap = new HashMap<>();
	    Map<Boolean, List<Integer>> groupedStudents = new HashMap<>();
	    groupedStudents.put(true, new ArrayList<>());
	    groupedStudents.put(false, new ArrayList<>());

	    Map<Integer, Integer> studentSemesterMap = new HashMap<>();
	    Map<Integer, Integer> studentYearMap = new HashMap<>();

	    for (Map<String, Object> st : reportedStudents) {
	        Integer studentId = (Integer) st.get("student_id");
	        String programType = programAssigmentRepository.getProgramTypeByStudentId(studentId);
	        boolean isSemester = SEMESTER_PROGRAM_TYPE.equalsIgnoreCase(programType);
	        programTypeMap.put(studentId, programType);

	        if (isSemester) {
	            groupedStudents.get(true).add(studentId);
	            studentSemesterMap.put(studentId, (Integer) st.get("current_sem"));
	        } else {
	            groupedStudents.get(false).add(studentId);
	            studentYearMap.put(studentId, (Integer) st.get("current_year"));
	        }
	    }

	    Map<Integer, Double> studentDues = new HashMap<>();

	    // Batch dues fetch for semester students
	    Map<Integer, List<Integer>> semesterGrouped = studentSemesterMap.entrySet().stream()
	            .collect(Collectors.groupingBy(
	                    Map.Entry::getValue,
	                    Collectors.mapping(Map.Entry::getKey, Collectors.toList())
	            ));

	    for (Map.Entry<Integer, List<Integer>> entry : semesterGrouped.entrySet()) {
	        Integer sem = entry.getKey();
	        List<Integer> students = entry.getValue();
	        List<Map<String, Object>> dues = studentDueRepository.getTotalDueForSemesterStudents(students, sem);
	        for (Map<String, Object> map : dues) {
	            studentDues.put((Integer) map.get("student_id"), ((Number) map.get("total_due")).doubleValue());
	        }
	    }

	    // Batch dues fetch for year students
	    Map<Integer, List<Integer>> yearGrouped = studentYearMap.entrySet().stream()
	            .collect(Collectors.groupingBy(
	                    Map.Entry::getValue,
	                    Collectors.mapping(Map.Entry::getKey, Collectors.toList())
	            ));

	    for (Map.Entry<Integer, List<Integer>> entry : yearGrouped.entrySet()) {
	        Integer year = entry.getKey();
	        List<Integer> students = entry.getValue();
	        List<Map<String, Object>> dues = studentDueRepository.getTotalDueForYearStudents(students, year);
	        for (Map<String, Object> map : dues) {
	            studentDues.put((Integer) map.get("student_id"), ((Number) map.get("total_due")).doubleValue());
	        }
	    }

	    // Final response with due status and permitted date
	    List<HashMap<String, Object>> responseWithDueStatus = new ArrayList<>(reportedStudents.size());
	    for (Map<String, Object> st : reportedStudents) {
	        HashMap<String, Object> map = modelMapper.map(st, HashMap.class);

	        Integer studentId = (Integer) st.get("student_id");
	        Integer currentSem = (Integer) st.get("current_sem");
	        Integer currentYear = (Integer) st.get("current_year");
	        String auid = (String) st.get("auid");

	        Double dues = studentDues.getOrDefault(studentId, 0.0);

	        LocalDate admissionDate = LocalDate.parse(studentDetailsRepository
	                .dateOfAdmissionByStudentId(studentId).toString());
	        long daysSinceAdmission = ChronoUnit.DAYS.between(admissionDate, todayDate);

	        boolean dueStatus = dues > NO_DUE_VALUE && daysSinceAdmission > NEW_ADMISSION_DUE_EXEMPT_DAYS;

	        String permittedDate = null;
	        if (dueStatus) {
	            permittedDate = studentPermissionRepository.getPermissionDate(auid, currentSem, currentYear);
	            if (StringUtils.isNotEmpty(permittedDate)) {
	                dueStatus = false;
	            } else {
	                Integer schoolId = (Integer) st.get("school_id");
	                Integer programAssignmentId = (Integer) st.get("program_assignment_id");
	                Integer specializationId = (Integer) st.get("program_specialization_id");
	                Integer semOrYear = currentSem != 0 ? currentSem : currentYear;

	                permittedDate = classCommencementDetailsRepository
	                        .getFromDateFromClassCommencementDetails(schoolId, programAssignmentId, specializationId, semOrYear);

	                if (ObjectUtils.isNotEmpty(permittedDate)) {
	                    dueStatus = false;
	                } else {
	                    permittedDate = null;
	                }
	            }
	        }

	        map.put("due_status", dueStatus);
	        map.put("attendance_permitted_date", permittedDate);
	        responseWithDueStatus.add(map);
	    }

	    return responseWithDueStatus;
	}

	public List<Map<String, Object>> sectionDetailsByAcademicYearAndSection(Integer ac_year_id,Integer program_specialization_id,Integer current_year_sem) {
		return sectionAssignmentRepository.sectionDetailsByAcademicYearAndSection(ac_year_id,program_specialization_id,current_year_sem);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> assignedStudentBySectionAssignmentId(Integer sectionAssignmentId) {
		Section section=sectionService.get(sectionAssignmentRepository.findById(sectionAssignmentId).get().getSection_id());
		List<Integer> studentIds=ResponseHandler.toConvertCommaSeperatedIdsAsList(sectionAssignmentRepository.findById(sectionAssignmentId).get().getStudent_ids());
		List<Map<String, Object>> studentDetails= studentDetailsRepository.assignedStudentByStudentId(studentIds);
		List<Map<String, Object>>  response=new ArrayList<Map<String,Object>>();
		studentDetails.stream().forEach(sd -> {
			Map<String, Object> mapData=modelMapper.map(sd, Map.class);
			mapData.put("section_id", section.getSection_id());
			mapData.put("section_name", section.getSection_name());
			response.add(mapData);
		});
		
		return response;
	}

	public ResponseEntity<Object> fetchAllSectionAssignmentDetailsBasedOnSchoolId(Pageable pageable, Integer school_id,
			Object keyword, Integer dept_id) {
		List<Map<String,Object>> response1 = sectionAssignmentRepository.fetchAllSectionAssignmentDetailsBasedOnSchoolId(pageable,school_id, keyword ,dept_id);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> fetchAllSectionAssignmentDetailsBasedOnSchoolId1(Pageable pageable1,
			Integer school_id, Integer dept_id) {
		List<Map<String,Object>> response = sectionAssignmentRepository.fetchAllSectionAssignmentDetailsBasedOnSchoolId1(pageable1,school_id ,dept_id);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> fetchAllSectionAssignmentDetailsBasedOnUserId(Pageable pageable, Integer created_by,
			Object keyword) {
		List<Map<String,Object>> response1 = sectionAssignmentRepository.fetchAllSectionAssignmentDetailsBasedOnUserId(pageable,created_by, keyword);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> fetchAllSectionAssignmentDetailsBasedOnUserId1(Pageable pageable1,
			Integer created_by) {
		List<Map<String,Object>> response = sectionAssignmentRepository.fetchAllSectionAssignmentDetailsBasedOnUserId1(pageable1,created_by);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	public List<Map<String, Object>> studentDetailsBasedOnSectionAssignmentId(Integer section_assignment_id) {
		List<Integer> studentIds=ResponseHandler.toConvertCommaSeperatedIdsAsList(sectionAssignmentRepository.findById(section_assignment_id).get().getStudent_ids());
		List<Map<String, Object>> studentDetails = new ArrayList<Map<String, Object>>();
		studentIds.stream().forEach(student ->{
			Map<String, Object> studentDetailsBasedOnSectionAssignmentId = studentDetailsRepository.studentDetailsBasedOnSectionAssignmentId(student);
			studentDetails.add(studentDetailsBasedOnSectionAssignmentId);
		});
		
		return studentDetails;
	}

	public List<Map<String, Object>> getCourseDetailData(Integer school_id, Integer ac_year_id,
			Integer program_specialization_id, Integer year_sem) {
		List<Map<String,Object>> response = sectionAssignmentRepository.getCourseDetailData(school_id,ac_year_id,program_specialization_id,year_sem);
		return response;
	}

	public List<Map<String, Object>> getSectionDetailData(Integer school_id, Integer ac_year_id,
			Integer program_specialization_id, Integer current_year_sem) {
		List<Map<String,Object>> response = sectionAssignmentRepository.getSectionDetailData(school_id,ac_year_id,program_specialization_id,current_year_sem);
		return response;
	}

  private final String SEMESTER_PROGRAM_TYPE = "Semester";
	private final Double NO_DUE_VALUE = 0.0;
	private final Integer NEW_ADMISSION_DUE_EXEMPT_DAYS = 15;

	public Boolean studentTillYearOrSemWithExemptDays(Integer studentId) {
		Double dues = 0.0;

		String programType = programAssigmentRepository.getProgramTypeByStudentId(studentId);
		if (ObjectUtils.isNotEmpty(programType) && StringUtils.equalsIgnoreCase(programType, SEMESTER_PROGRAM_TYPE)) {
			Integer currentSem = reportingStudentsRepository.getCurrentSemByStudentId(studentId);
			dues = studentDueRepository.totalDueTillCurrentSemOfStudent(currentSem, studentId);
		} else {
			Integer currentYear = reportingStudentsRepository.getCurrentYearByStudentId(studentId);
			dues = studentDueRepository.totalDueTillCurrentSemOfStudent(currentYear, studentId);
		}
		Optional<Long> totalDaysOptional = Optional.ofNullable(studentDetailsRepository.dateOfAdmissionByStudentId(studentId))
				.map(Object::toString)
				.map(LocalDate::parse)
				.map(date -> ChronoUnit.DAYS.between(date, LocalDate.now()));

		long totalDays = totalDaysOptional.orElse(0L);
		return (dues > NO_DUE_VALUE && totalDays > NEW_ADMISSION_DUE_EXEMPT_DAYS);


	}
	
}
