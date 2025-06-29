package com.au.event;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.au.model.Student_Details;
import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;

@Component
public class BirthdayGreetingEventPublisher {
	
	@Autowired
	private StudentDetailsRepository stuRepo;
	
	@Autowired
	private ResponseHandler response_handler;
	
	private static final String email = "adsa@acharya.ac.in";
	
	@EventListener
	@Async
	public void handleBirthdayGreetingEventPublisher(BirthdayGreetingEvent birthdayGreetingEvent) {
		try {

		        List<Student_Details> studentsHavingBirthday = stuRepo.getAllStudentsHavingBirthday(birthdayGreetingEvent.getBirthdayDate());
		        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA "+studentsHavingBirthday.size());
		        studentsHavingBirthday.stream().forEach(stu -> {
		        	
		        	String[] studentEmail = new String[1];
		        	studentEmail[0] = stu.getAcharya_email();
		        	
		        	String birthdayGreeting = "Happy Birthday " + stu.getStudent_name() + " !! <br><br>"
		        			+ "Today, we celebrate not just you, but the wonderful family that supports you. <br><br>"
		        			+ "At Acharya, we value the unique journey each student takes, and we’re grateful for the love and encouragement you provide. This special day is a reminder of the growth, achievements, and shared moments that make our community thrive. <br><br>"
		        			+ "Wishing you a year filled with joy, new adventures, and lasting memories. We look forward to continuing this journey together ! <br><br>"
		        			+ "<span style='color: #9b33ff;'>A surprise is waiting for you in the Student Activity Office. Kindly visit to collect it.</span> <br> " + "<br>" + "<br> " + "<br>" + "<br> " + "<br>"
		        			+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br> "
		    				+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
		        	response_handler.sendMultipleSimpleEmailWithCc(studentEmail, birthdayGreeting, "Birthday Greetings", email);
		        });

		} catch(Exception e) {
			System.out.println("Exception occur:- " + e.getMessage());
		}
	}

}
