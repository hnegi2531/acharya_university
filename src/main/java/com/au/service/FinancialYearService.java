package com.au.service;




import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.FinancialYear;
import com.au.repository.FinancialYearRepository;
import com.au.response.ResponseHandler;

@Service
public class FinancialYearService {

	@Autowired
	private FinancialYearRepository financial_year_repo;
	
	public List<FinancialYear> listAll() {
		return financial_year_repo.findAll1();
	}
	
//	public List<FinancialYear> listAll1() {
//		return financial_year_repo.findAll();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		
		Page<Object> response1 = financial_year_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		
		Page<Object> response = financial_year_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	
	
	public FinancialYear saveFinancialYear(FinancialYear s) throws Exception   {
			
		
		/*String date1 =s.getFrom_date().toString();
		  String date2 = s.getTo_date().toString();
		  System.out.println(date1 + " ......... "  + date2);
		  //String dateStr = "Mon Jun 18 00:00:00 IST 2012";
		  DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy" , Locale.ENGLISH);
		  Date date3 = (Date)formatter.parse(date1);
		  Date date4 = (Date)formatter.parse(date2);
		  System.out.println(".......0 " + date3 + " ......... " + date4);        

		  Calendar cal1 = Calendar.getInstance();
		  Calendar cal2 = Calendar.getInstance();
		  cal1.setTime(date3);
		  cal2.setTime(date4);
		  int x1=cal1.get(Calendar.MONTH) + 1;
		  String y1=((x1 < 10) ? "0" : "") + x1;
		  int x2=cal2.get(Calendar.MONTH) + 1;
		  String y2=((x2 < 10) ? "0" : "") + x2;
		  String formatedDate1 =cal1.get(Calendar.YEAR) + "-" + y1 + "-" + cal1.get(Calendar.DATE);
		  String formatedDate2 =cal2.get(Calendar.YEAR) + "-" + y2 + "-" + cal2.get(Calendar.DATE);
		  System.out.println("formatedDate 1: " + formatedDate1); 
		  System.out.println("formatedDate 2: " + formatedDate2);  
		  
		  LocalDate localDate = LocalDate.parse(formatedDate1);
		  System.out.println(localDate);
		 DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-d");
		  DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-d");
		  LocalDate  d1 = LocalDate.parse(formatedDate1, df1);
		  System.out.println(d1);
		  LocalDate  d2 = LocalDate.parse(formatedDate2, df2);

		  Long datediff = ChronoUnit.DAYS.between(d1,d2);
		  System.out.println(d1 + " ...... " + d2 + " ....... " + datediff);
		// java 8 way - 1
		// Fetching the diff using between() method
		//long noOfDaysDifference = ChronoUnit.DAYS.between(localDate1, localDate2);

		// print diff in days
		//System.out.println("Java 8 way 1 between() - No of days diff is : " + noOfDaysDifference);

		// java 8 way - 2
		// using until() method
		//noOfDaysDifference = localDate1.until(localDate2, ChronoUnit.DAYS);

		//System.out.println("Java 8 way 2 untill() - No of days diff is : " + noOfDaysDifference);
		

		if(datediff== 365 | datediff== 366){
				return financial_year_repo.save(s);
			}else{
				 throw new RuntimeException("the year gap between From_date and To_date is not 1 year" + s.getFrom_date() + s.getTo_date());
			}*/
		if(financial_year_repo.countOfFinancialYear(s.getFinancial_year())>=1) {
			throw new Exception("Financial year Already Exist");
		}else if(financial_year_repo.countOfYear(s.getYear())>=1) {
			throw new Exception("Year Already Exist");
		}else {
			return financial_year_repo.save(s);
		}	

	}
	public FinancialYear saveFinancialYear1(FinancialYear r) {
		return financial_year_repo.save(r);
	}

	public FinancialYear get(Integer id) {
		return financial_year_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("FinancialYear Not Found:" + id));
	}

	public void delete(Integer id) {
		FinancialYear cc = financial_year_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FinancialYear Not Found:" + id));
		financial_year_repo.updateFinancialYear(id);
	}

	public void delete1(Integer id) {
		FinancialYear cc = financial_year_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("FinancialYear Not Found:" + id));
		financial_year_repo.updateFinancialYear1(id);

	}
	
	public FinancialYear getFinancialYearIdOnCurrentYear() {
		return financial_year_repo.getFinancialYearIdOnCurrentYear();
	}

}
