package com.au.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.FinancialYear;
import com.au.model.MealBill;
import com.au.model.MealType;
import com.au.model.StoreIndentRequest;
import com.au.repository.FinancialYearRepository;
import com.au.repository.MealBillRepository;
import com.au.repository.MealTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class MealBillService {

	@Autowired
	private MealBillRepository mealBillRepository;
	
	@Autowired
	private FinancialYearRepository financial_year_repo;

	public List<MealBill> createMealBill(@Valid  List<MealBill> mealBill) throws Exception {

		mealBill.stream().forEach(bill -> {
			if (mealBillRepository.getCountOfMealBill(bill.getMonth_year(),bill.getVoucher_head_new_id(),bill.getSchool_id()) >= 1) {
				throw new RuntimeException("The date is already locked for this meal bill !!!");
			}

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime now = LocalDateTime.now();
			String date = dtf.format(now);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			LocalDate date1 = null;
			date1 = LocalDate.parse(date);

			FinancialYear f_year = financial_year_repo.getFinancialYearDataForIndent(date1); // financial year details of one particular id.
				String year1 = f_year.getFinancial_year().substring(2, 5); // 24-
				String year2 = f_year.getFinancial_year().substring(7, 9); // 25
			
				MealBill latestMealBillNumber = mealBillRepository.getLatestMealBillNumber(); // based on latest indent_ticket , id will come
																					

			String mealBillNumber = null;
			String count_for_id = mealBillRepository.getMaxStoreIndentRequestCount();

			if (count_for_id == null) {
				mealBillNumber = String.valueOf(1);
			} else if (f_year.getFinancial_year_id() == latestMealBillNumber.getFinancial_year_id()) {
				String abc = count_for_id.split("/")[0]; 
				Integer count = Integer.valueOf(abc) + 1;
				mealBillNumber = String.valueOf( count);
			} else {
				 mealBillNumber = String.valueOf(1);

			}

			bill.setBill_number(mealBillNumber + "/" + year1 + "" + year2);
			bill.setFinancial_year_id(f_year.getFinancial_year_id());

		});

		return mealBillRepository.saveAll(mealBill);
	}

	public List<MealBill> getAllActiveMealBill() {
		return mealBillRepository.getAllActiveMealBill();
	}
	
	
	public MealBill get(Integer meal_bill_id) {
		return mealBillRepository.findById(meal_bill_id)
				.orElseThrow(() -> new ResourceNotFoundException("Meal Bill Not Found:" + meal_bill_id));
	}
	
	public MealBill updateMealBill(MealBill cmt) {
		return mealBillRepository.save(cmt);
	}
	
	public void deactivate(Integer meal_bill_id) {
		mealBillRepository.findById(meal_bill_id).orElseThrow(() -> new RuntimeException("Meal Bill Not Found:" + meal_bill_id));
		mealBillRepository.deactivate(meal_bill_id);
	}

	public void activate(Integer meal_bill_id) {
		mealBillRepository.findById(meal_bill_id).orElseThrow(() -> new RuntimeException("Meal Bill Not Found:" + meal_bill_id));
		mealBillRepository.activate(meal_bill_id);
	}
	
	public ResponseEntity<Object> fetchAllMealTypeDetails(Pageable pageable, String bill_number,Object keyword) {
		
		Page<Object> response1 = mealBillRepository.findAll2(pageable,bill_number, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllMealTypeDetails1(Pageable pageable ,String bill_number) {
		
		Page<Object> response = mealBillRepository.findAll3(pageable,bill_number);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> fetchAllMealBillDetailsGrouped(Pageable pageable,Object keyword) {
		
		Page<Object> response1 = mealBillRepository.findAll4(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> fetchAllMealBillDetailsGrouped1(Pageable pageable) {
		
		Page<Object> response = mealBillRepository.findAll5(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public String checkExitingData(String month_year, Integer voucher_head_new_id, Integer school_id) {
		if (mealBillRepository.getCountOfMealBill(month_year,voucher_head_new_id,school_id) >= 1) {
			throw new RuntimeException("The date is already locked for this meal bill !!!");
		}
		return "date is not locked yet";
	}



}
