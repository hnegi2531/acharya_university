package com.au.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.au.model.LeaveKitty;
import com.au.repository.LeaveKittyRepository;

@Lazy(false)
@Component
public class TaskSchedulerService {
	
	@Autowired
	private LeaveKittyRepository empLeaveRepo;
	
	@Autowired
	private EmployeeLeavesService empLeaveService;
	
//	"0 0 18 * * MON-FRI" means every weekday at 6:00 PM. 
//
//	"0 0 */1 * * *" means every hour on the hour.
//
//	"0 0 */8 * * *" means every 8 hours on the hour.
//
//	"0 0 12 1 * *" means 12:00 PM on the first day of every month. 
	
//	0 15 10 ? * 6#3	Fire at 10:15 AM on the third Friday of every month
	
//	0/1 means start at hour 0 and repeat each 1 hour
//	1/1 is start first day of the month and execute each 1 day
	
//   @Scheduled(cron="0/1 * * * * *")  -- For Every Second
//	 @Scheduled(cron="0 0/1 * * * *")  -- For Every Minute  -- @Scheduled(cron="0 0 0/1 * * *") --  @Scheduled(cron="1 * * * * *")
//	 @Scheduled(cron="0 * * * * *")  -- For Every Minute

	
	// "0 0 0 1 1,7 *"  --> Every 1st,January and 1st,July of every Year at 00:00am
	
	
	//@Scheduled(cron="0 0 0 ? JAN,JUL * *") For Permanent Staffs  --->  0 0 12 1 1/6 ? *
	
//	  @Scheduled(cron="0 15 15 25 FEB *")
//	 @Scheduled(cron="0 55 10 27 2 ?")
	
/*	1 2 3 4 5 6 Index
	- - - - - -
	* * * * * * command to be executed
	- - - - - -
	| | | | | | 
	| | | | | ------- Day of week (MON - SUN)
	| | | | --------- Month (1 - 12)
	| | | ----------- Day of month (1 - 31)
	| |-------------- Hour (0 - 23)
	| --------------- Minute (0 - 59)
	----------------- Seconds (0 - 59) */
//						   ___________________________________________________________________________
//						  |  _____________________________											  |
//						  |	|							  |										      |
/*
 * @Scheduled(cron="0 0 3 20 * *") // Credit Leave on 20th of every month at
 * 8:30am (8:30 - 5:30 = 3:00) public void updateLeavesEveryMonthOn20th() throws
 * InterruptedException {
 * 
 * empLeaveRepo.updateLeavesEveryMonthOn20th();
 * 
 * DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
 * LocalDateTime now = LocalDateTime.now(); System.out.println(dtf.format(now));
 * 
 * System.out.
 * println("{{{{{{{{{---- S-C-H-E-D-U-L-E-R (--- 1 ---) W-O-R-K-I-N-G ----}}}}}}}}}"
 * );
 * 
 * }
 */
	 
//	 @Scheduled(cron="0 0 3 1 1,7 *")  // For Every January and July credit Leave on 1st at 8:30am
//	  public void testingScheduler() throws InterruptedException {
//		 
//		 empLeaveRepo.updateLeavesEveryJanuaryAndJulyOn1st();
//		  
//		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//		   LocalDateTime now = LocalDateTime.now();
//		   System.out.println(dtf.format(now));
//		      
//		      Date date1 = new Date();
//		      System.out.print("{{{-- S-C-H-E-D-U-L-E-R(- 2 -)W-O-R-K-I-N-G ----}}}"+date1);
//		  
//	  }


}
