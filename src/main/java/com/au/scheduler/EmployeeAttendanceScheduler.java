package com.au.scheduler;

import com.au.event.AttendanceSheetEvent;
import com.au.repository.EmployeeSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class EmployeeAttendanceScheduler {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private EmployeeSheetRepository employeeSheetRepository;

	public void bioTransactionScheduler() {
		try {
			System.out.println("Employee Attendance Scheduler Started ");
			LocalDate currentDate = LocalDate.now();
			int currentMonth = currentDate.getMonthValue();
			int currentYear = currentDate.getYear();
			System.out.println("Month: " + currentMonth + " Year: " + currentYear);
			runPreviousMonthAttendanceSheet(currentMonth, currentYear);
			AttendanceSheetEvent attendanceSheetEvent=new AttendanceSheetEvent(currentMonth, currentYear,null,null);
			applicationEventPublisher.publishEvent(attendanceSheetEvent);
		} catch (Exception e) {
			System.out.println("Exception occur in EmployeeAttendanceScheduler " + e.getMessage());
			if (e instanceof DataAccessException) {
				SQLException sqlException = (SQLException) ((DataAccessException) e).getMostSpecificCause();
                System.out.println("SQL Error Code: " + sqlException.getErrorCode());
                System.out.println("SQL State: " + sqlException.getSQLState());
            }
		}
	}

	private void runPreviousMonthAttendanceSheet(int currentMonth, int currentYear) {
		int month = 0;
		int year = 0;
		if(currentMonth == 1){
			month = 12;
			year = currentYear - 1;
		}else {
			month = currentMonth - 1;
			year = currentYear;
		}
		AttendanceSheetEvent attendanceSheetEvent=new AttendanceSheetEvent(month, year,null,null);
		applicationEventPublisher.publishEvent(attendanceSheetEvent);
	}

}
