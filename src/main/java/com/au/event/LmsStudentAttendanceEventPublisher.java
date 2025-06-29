package com.au.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.LocalDate;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.au.dto.AttendanceResponseDTO;
import com.au.model.Batch;
import com.au.model.Course;
import com.au.model.ProgramSpecilization;
import com.au.model.Section;
import com.au.model.StudentAttendance;
import com.au.model.TimeTable;
import com.au.model.UserAuthentication;
import com.au.repository.BatchRepository;
import com.au.repository.CourseRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.ProgramAssigmentRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.SectionRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.TimeTableRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.service.StudentAttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class LmsStudentAttendanceEventPublisher {


	private TimeTableRepository timeTableRepository;


	private ProgramSpecilizationRepository programSpecilizationRepository;


	private CourseRepository courseRepository;


	private BatchRepository batchRepository;


	private SectionRepository sectionRepository;


	private ProgramAssigmentRepository programAssigmentRepository;


	private StudentAttendanceService studentAttendanceService;


	private UserAuthenticationRepository userAuthenticationRepository;

	private StudentDetailsRepository studentDetailsRepository;

	private EmployeeDetailsRepository employeeDetailsRepository;

	private static final String LMS_ATTENDANCE_URL="https://alive-core-api.alive.university/api/v1/attendence/";

	private  Section section;
	private Batch batch;

	@Async
	@EventListener
	public void handleLmsStudentAttendanceEvent(LmsAttendanceEvent event) {
		try {
			Lock rl=new ReentrantLock(true);
			rl.lock();
			Set<Integer> timeTableIds=new HashSet<>();
			if(ObjectUtils.isNotEmpty(event) && ObjectUtils.isNotEmpty(event.getDate())) {
				timeTableIds=timeTableRepository.getOnlineTimeTableByDate(event.getDate());
			}else {
				timeTableIds=timeTableRepository.getOnlineTimeTableByDate(LocalDate.now());
			}
			
			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<>(headers);
			rl.unlock();
			if(ObjectUtils.isNotEmpty(timeTableIds)) {
				for(Integer timeTableId:timeTableIds) {
					ResponseEntity<AttendanceResponseDTO> attendanceDto=restTemplate.exchange(
							LMS_ATTENDANCE_URL + timeTableId, HttpMethod.GET, entity, AttendanceResponseDTO.class);
					AttendanceResponseDTO attendance=attendanceDto.getBody();
					UserAuthentication userDetails=userAuthenticationRepository.userDetailsByEmployeeCode(attendance.getData().getClassDetails().getEmpID());
					Integer employeeId=employeeDetailsRepository.getEmpIdByEmpCode(attendance.getData().getClassDetails().getEmpID());
					List<StudentAttendance> studentAttendances=new ArrayList<>();
					TimeTable timeTime=timeTableRepository.findById(timeTableId).get();
					ProgramSpecilization specialization=programSpecilizationRepository.findById(Integer.valueOf(attendance.getData().getClassDetails().getCourseBranchAssignmentId())).get();
					String programType=programAssigmentRepository.getProgramTypeOnProgramAssignmentId(specialization.getProgram_assignment_id());
					if(ObjectUtils.isNotEmpty(attendance.getData().getClassDetails().getSectionShortName())) 
						section=sectionRepository.findBySectionName(attendance.getData().getClassDetails().getSectionShortName());

					if(ObjectUtils.isNotEmpty(attendance.getData().getClassDetails().getBatchShortName()))
						batch=batchRepository.findByBatchShortName(attendance.getData().getClassDetails().getBatchShortName());

					attendance.getData().getStudentAttendance().stream().forEach(st -> {
						Integer studentId=studentDetailsRepository.getStudentIdByAuid(st);
						StudentAttendance sa=new StudentAttendance();
						sa.setSchool_id(Integer.valueOf(attendance.getData().getClassDetails().getInstituteId()));
						sa.setAc_year_id(timeTime.getAc_year_id());
						Course cr=courseRepository.findCourseByNameAndShortName(attendance.getData().getClassDetails().getSubjectNameShort());
						sa.setCourse_id(cr.getCourse_id());
						sa.setRemarks("Lms Attendance");
						sa.setDescription(attendance.getMessage());
						sa.setStudent_id(studentId);
						sa.setPresent_status(true);
						sa.setOffline_status(timeTime.getIs_online());

						if(ObjectUtils.isNotEmpty(batch)) 
							sa.setBatch_id(batch.getBatch_id());
						else 
							sa.setBatch_id(null);


						if(ObjectUtils.isNotEmpty(section)) 
							sa.setSection_id(section.getSection_id());
						else 
							sa.setSection_id(null);

						sa.setDate_of_class(timeTime.getSelected_date());
						sa.setTime_slots_id(timeTime.getTime_slots_id());
						sa.setTime_table_id(timeTime.getTime_table_id());
						Integer currentOrSem=programType.equalsIgnoreCase("Semester") ? timeTime.getCurrent_sem(): timeTime.getCurrent_year();
						sa.setYear_or_sem(currentOrSem);
						sa.setActive(true);
						sa.setCreated_username(userDetails.getUsername());
						sa.setCreated_by(userDetails.getId());
						sa.setCourse_assignment_id(Integer.valueOf(attendance.getData().getClassDetails().getInstituteId()));
						sa.setEmp_id(employeeId);
						studentAttendances.add(sa);

					});
					if(! attendance.getData().getClassDetails().getBatchAssignmentId().equals("0")) {
						studentAttendanceService.saveStudentAttendanceBatchForLMS(studentAttendances,Integer.valueOf(attendance.getData().getClassDetails().getBatchAssignmentId()));
					}else {
						studentAttendanceService.saveStudentAttendanceOnSectionForLMS(studentAttendances,Integer.valueOf(attendance.getData().getClassDetails().getSectionAssignmentId()));
					}
				}
			}
			section=null;
			batch=null;
			log.info("Attendance is taken !!");
		} catch (Exception e) {
			log.error("Lms Student Attendance Trigger Error : " +e.getMessage());
		}

	}


}
