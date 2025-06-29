package com.au.scheduler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.au.event.FrroPassportAndVisaExpiry;
import com.au.repository.ForeignRegionalRegistrationOfficesDetailsRepository;

@Component
public class FrroPassportAndVisaExpiryScheduler {
	
	@Autowired
	private ForeignRegionalRegistrationOfficesDetailsRepository frrod_repo;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	
//	@Scheduled(cron = "0 0/1 * * * *")
	public void frroPassportAndVisaExpiryScheduler() {
		
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); 
		
		LocalDate currentDate = LocalDate.now();
	    String todaysDate = formatter.format(currentDate);
	    
		LocalDate plusDays20 = currentDate.plusDays(20);
		String expectedExpiryDate20 = formatter.format(plusDays20); 
		
		LocalDate plusDays15 = currentDate.plusDays(15);
		String expectedExpiryDate15 = formatter.format(plusDays15); 
		
		LocalDate plusDays07 = currentDate.plusDays(7);
		String expectedExpiryDate07 = formatter.format(plusDays07); 
		
		LocalDate  plusDays00= currentDate.minusDays(1);
		String expired = formatter.format(plusDays00); 
	        
	        
		FrroPassportAndVisaExpiry frroPassportAndVisaExpiry=new FrroPassportAndVisaExpiry(todaysDate,expectedExpiryDate20,expectedExpiryDate15,expectedExpiryDate07,expired);
		applicationEventPublisher.publishEvent(frroPassportAndVisaExpiry);
		
	}
	
}
