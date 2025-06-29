package com.au.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.au.service.TriggerService;

@Component
public class StudentReportingShcedular {
	
	
	Logger log = LoggerFactory.getLogger(StudentReportingShcedular.class);
	
	@Autowired
	private TriggerService triggerService;
	
	@Scheduled(cron = "0 0 18 * * *")
	private void updateStudentReportingCurrentYearSemAndUsn() {
		triggerService.studentReportingTrigger(null);
	}
}
