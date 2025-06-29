package com.au.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.au.dto.StudentApiResponsePhp;
import com.au.dto.StudentAuidDto;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.StudentDetailsRepository;

@Component
public class StudentReportingEventPublisher {
	
	Logger log = LoggerFactory.getLogger(StudentReportingEventPublisher.class);
	
	
	private final String API_URL = "https://acharyainstitutes.in/index.php?r=acerp-api-std/student_info_migrate&status=2&auid_str=";
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;
	

	@Async
	@EventListener
	public void handleStudentReportingEvent(StudentReportingEvent studentReportingEvent) {
		try {
		List<StudentAuidDto>  studentAuidDto=new ArrayList<>();
		if(ObjectUtils.isNotEmpty(studentReportingEvent.getAuidOrAuidWithoutIncrement())) {
			 StudentAuidDto dto = new StudentAuidDto();
		        dto.setAuidOrAuidWithoutIncrement(studentReportingEvent.getAuidOrAuidWithoutIncrement()); 
		        studentAuidDto.add(dto);
		} else {
			studentAuidDto=studentDetailsRepository.auidForStudentReporting();
		}
		if(ObjectUtils.isNotEmpty(studentAuidDto)) {
			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<>(headers);
			studentAuidDto.forEach(auid -> {
				ResponseEntity<StudentApiResponsePhp> responseEntity = restTemplate.exchange(
						API_URL + auid.getAuidOrAuidWithoutIncrement(), HttpMethod.GET, entity, StudentApiResponsePhp.class);
				StudentApiResponsePhp studentApiResponsePhp = responseEntity.getBody();
				if (studentApiResponsePhp != null && studentApiResponsePhp.isSuccess()) {
					updationOfReportingYearSemAndUsn(studentApiResponsePhp);
					
				}
			});
		}
		}catch(Exception e) {
			System.err.println(e.getMessage());
			log.error(" Updation Of Reporting Year And Sem of Student" +e.getMessage());
		}
		
	}

	private void updationOfReportingYearSemAndUsn(StudentApiResponsePhp studentApiResponsePhp) {
		try {
			
		if(ObjectUtils.isNotEmpty(studentApiResponsePhp.getData())) {
			studentApiResponsePhp.getData().stream().forEach(student -> {
				if (student.getCurrent_year() != null && student.getCurrent_sem() != null) {
					Integer currentYear = Integer.parseInt(student.getCurrent_year());
					Integer currentSem = Integer.parseInt(student.getCurrent_sem());
					System.out.println(student.getCurrent_year() + " " + student.getCurrent_sem() +student.getAuid());
					reportingStudentsRepository.updateCurrentYearOrSemByAuid(currentYear, currentSem, student.getAuid());
				}
				if (student.getUsn() != null) {
					String usn = student.getUsn();
					studentDetailsRepository.updateUsnOfStudentByAuid(usn, student.getAuid());
				}
			});
		}
		}catch (Exception e) {
			System.err.println(e.getMessage());
			log.error(" Updation Of Reporting Year And Sem of Student" +e.getMessage());
		}
	}
	

}
