package com.au.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.au.event.LeaveKittyEvent;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.LeaveKittyRepository;
import com.au.repository.EmployeeTypeRepository;
import com.au.repository.LeaveApplyRepository;
import com.au.repository.LeavePatternRepository;
import com.au.repository.LeaveTypeRepository;

@Component
public class LeaveKittyScheduler {
	
	@Autowired
	private LeavePatternRepository leavePatternRepository;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	private LeaveTypeRepository leaveTypeRepository;

	@Autowired
	private EmployeeTypeRepository employeeTypeRepository;

	@Autowired
	private LeaveKittyRepository LeaveKittyRepository;

	@Autowired
	private LeaveApplyRepository leaveApplyRepository;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void generateLeaveKitty() {
		try {
			LeaveKittyEvent leaveKittyEvent = new LeaveKittyEvent("leaveKitty", null);
			applicationEventPublisher.publishEvent(leaveKittyEvent);
		} catch (Exception e) {
			System.out.println("Exception occur:- " + e.getMessage());
		}

		
	}
	
	
}
