package com.au.scheduler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.au.event.BirthdayGreetingEvent;

@Component
public class BirthdayGreetingEventScheduler {
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	
//	@Scheduled(cron = "0 0 12 * * *")
	@Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Kolkata")
	public void frroPassportAndVisaExpiryScheduler() {

		try {	
		 	LocalDate today = LocalDate.now();

	        BirthdayGreetingEvent birthday = new BirthdayGreetingEvent(today);
	        applicationEventPublisher.publishEvent(birthday);
		} catch (Exception e) {
			System.out.println("Exception occur:- " + e.getMessage());
		}
	}

}
