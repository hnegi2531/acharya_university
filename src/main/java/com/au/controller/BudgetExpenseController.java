package com.au.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.JwtDetails;
import com.au.model.BudgetExpense;
import com.au.response.ResponseHandler;
import com.au.service.BudgetExpenseService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class BudgetExpenseController {
	
		Logger log=LoggerFactory.getLogger(BudgetExpenseController.class);
		
		@Autowired
		private BudgetExpenseService bud_ser;
		
		@Autowired
		private JwtTokenService jwt_service;
		
		@PostConstruct
		void started() {
		  TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		}
		
		
		@PostMapping("/budgetExpense")
		public ResponseEntity<Object> saveBudgetExpense(@RequestBody @Valid BudgetExpense bug , @RequestHeader("Authorization") String jwtToken)
				throws JsonParseException, JsonMappingException, IOException{
			if(RateLimitController.bucket.tryConsume(1)) {
			
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				bug.setCreated_by(jwtDetails.getUserId());
				bug.setCreated_username(jwtDetails.getUserName());
				BudgetExpense budget_exp = bud_ser.saveBudgetExpense(bug);
				ResponseEntity<Object> budget_exp_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, budget_exp);
				return budget_exp_response;
				} else {
					ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
					return rs;
				}
		}
		
		@GetMapping("/activeBudgetExpense")
		public ResponseEntity<Object> activeBudgetExpenseDetails(){
			if(RateLimitController.bucket.tryConsume(1)) {
				List<BudgetExpense> budget_exp = bud_ser.activeBudgetExpenseDetails();
				ResponseEntity<Object> budget_exp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, budget_exp);
				return budget_exp_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/allBudgetExpense")
		public ResponseEntity<Object> getAllBudgetExpense(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
			
			if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> budget_exp_filtered =  bud_ser.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
				return budget_exp_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> budget_exp_sorted = bud_ser.getAllSortedData(pageable1);
				return budget_exp_sorted;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/budgetExpenseById/{budget_expense_id}")
		public ResponseEntity<Object> BudgetExpenseDetailById(@PathVariable Integer budget_expense_id){
			if(RateLimitController.bucket.tryConsume(1)) {
			try {
				BudgetExpense be=bud_ser.BudgetExpenseDetailById(budget_expense_id);
				log.debug("Request {}", budget_expense_id);
				ResponseEntity<Object> budget_exp_response= ResponseHandler.generateResponse(true, HttpStatus.OK, be);
				return budget_exp_response;

			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response;
			}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@PutMapping("/updateBudgetExpense/{budget_expense_id}")
		public ResponseEntity<Object> updateBudgetExpense(@RequestBody BudgetExpense bud ,
				@PathVariable Integer budget_expense_id, @RequestHeader("Authorization") String jwtToken) throws 
					JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
				try {
					JwtDetails jwtDetails=jwt_service.callJwtToken(jwtToken);
					bud.setModified_by(jwtDetails.getUserId());
					bud.setModified_username(jwtDetails.getUserName());
					bud_ser.updateBudgetExpense(bud);
					ResponseEntity<Object> budget_exp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return budget_exp_response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response;
				}
				} else {
					ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
					return rs;
				}
			
		}
		
		@DeleteMapping("/activateBudgetExpense/{budget_expense_id}")
		public ResponseEntity<Object> delete1(@PathVariable Integer budget_expense_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
			bud_ser.delete1(budget_expense_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@DeleteMapping("/deactivateBudgetExpense/{budget_expense_id}")
		public ResponseEntity<Object> delete2(@PathVariable Integer budget_expense_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
			bud_ser.delete2(budget_expense_id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}
		
		@GetMapping("/budgetExpenseBySchoolIdFinancialIdAndBudgetDate/{school_id}/{financial_year_id}/{from_date}/{to_date}")
		public ResponseEntity<Object> budgetExpenseBySchoolIdFinancialIdAndBudgetDate(@PathVariable Integer school_id,
				@PathVariable Integer financial_year_id, @PathVariable String from_date, @PathVariable String to_date ) throws ParseException  {
			if(RateLimitController.bucket.tryConsume(1)) {
			
				  DateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
				  Date d1 = (Date)sdformat.parse(from_date);
				  System.out.println("d1 "  + d1);
			      Date d2 = (Date)sdformat.parse(to_date);
			      System.out.println("d2 "   + d2);
			      Calendar cal1=Calendar.getInstance();
			      Calendar cal2=Calendar.getInstance();
			      cal1.setTime(d1);
			      cal2.setTime(d2);
			      int x1=cal1.get(Calendar.MONTH) + 1;
				  String y1=((x1 < 10) ? "0" : "") + x1;
				  int x2=cal2.get(Calendar.MONTH) + 1;
				  String y2=((x2 < 10) ? "0" : "") + x2;
			      String formatedDate1 =cal1.get(Calendar.YEAR) + "-" + y1 + "-" + cal1.get(Calendar.DATE);
				  String formatedDate2 =cal2.get(Calendar.YEAR) + "-" + y2 + "-" + cal2.get(Calendar.DATE);
				  System.out.println("formatedDate 1: " + formatedDate1); 
				  System.out.println("formatedDate 2: " + formatedDate2);
				 // LocalDate localDate = LocalDate.parse(formatedDate1);
				  //System.out.println(localDate);
				 DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-d");
				  DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyy-MM-d");
				  LocalDate  d3 = LocalDate.parse(formatedDate1, df1);
				  Date d5=java.sql.Date.valueOf(d3);
				  System.out.println(d5);
				  LocalDate  d4 = LocalDate.parse(formatedDate2, df2);
				  Date d6=java.sql.Date.valueOf(d4);
				  System.out.println(d6);
				  BudgetExpense bud1= bud_ser.budgetExpenseBySchoolIdFinancialIdAndBudgetDate(school_id,financial_year_id,d5,d6 );
				  if(bud1==null) {
					  throw new RuntimeException("data not found on combination of school_id,financial_year_id,from_date and to_date");
				  }else {
					  ResponseEntity<Object> bud1_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bud1);
					  return bud1_response;
				  }
			} else {
						ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
						return rs;
					}
			
		}
		
		/*@GetMapping("/budgetExpenseBySchoolIdFinancialIdAndBudgetDate1/{school_id}/{financial_year_id}/{from_date}/{to_date}")
		public BudgetExpense budgetExpenseBySchoolIdFinancialIdAndBudgetDate1(@PathVariable Integer school_id,
				@PathVariable Integer financial_year_id, @PathVariable String from_date, @PathVariable String to_date ) throws ParseException{
			return bud_ser.budgetExpenseBySchoolIdFinancialIdAndBudgetDate1(school_id,financial_year_id,new SimpleDateFormat("yyyy-MM-dd").parse(from_date), new SimpleDateFormat("yyyy-MM-dd").parse(to_date) );
		}*/
		
		/*("/budgetExpenseBySchoolIdFinancialIdAndBudgetDate1/{school_id}/{financial_year_id}/{from_date}/{to_date}")
		public BudgetExpense budgetExpenseBySchoolIdFinancialIdAndBudgetDate1(@PathVariable Integer school_id,
				@PathVariable Integer financial_year_id, @PathVariable Date from_date, @PathVariable Date to_date ) throws ParseException{
			return bud_ser.budgetExpenseBySchoolIdFinancialIdAndBudgetDate1(school_id,financial_year_id, from_date, to_date );
		}*/

}
