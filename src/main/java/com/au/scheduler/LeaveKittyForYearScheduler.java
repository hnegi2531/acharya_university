package com.au.scheduler;

import com.au.event.LeaveKittyForYearEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class LeaveKittyForYearScheduler {
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	

	public void generateLeaveKitty() {
		try {
		
			LeaveKittyForYearEvent leaveKittyForYearEvent = new LeaveKittyForYearEvent("leaveKittyForYear", null);
			applicationEventPublisher.publishEvent(leaveKittyForYearEvent);
		} catch (Exception e) {
			System.out.println("Exception occur:- " + e.getMessage());
		}

		
	}

}
