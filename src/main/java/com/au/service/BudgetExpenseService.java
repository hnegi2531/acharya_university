package com.au.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.au.exception.ResourceNotFoundException;
import com.au.model.BudgetExpense;
import com.au.repository.BudgetExpenseRepository;
import com.au.response.ResponseHandler;

@Service
public class BudgetExpenseService {
	
		@Autowired
		private BudgetExpenseRepository bud_repo;
		
		
		public BudgetExpense saveBudgetExpense( BudgetExpense bug) {
			return bud_repo.save(bug);
		}
		
		public List<BudgetExpense> activeBudgetExpenseDetails(){
			return bud_repo.activeBudgetExpenseDetails();
		}
		
		public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

			Page<Object> budget_exp_filtered_response = bud_repo.getAllDataFilteredByKeyword(pageable, keyword );
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, budget_exp_filtered_response);
			}

		public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

			Page<Object> budget_exp_sorted_response = bud_repo.getAllSortedData(pageable);
			return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, budget_exp_sorted_response);
		}
		
		public BudgetExpense BudgetExpenseDetailById(Integer budget_expense_id) {
			return bud_repo.findById(budget_expense_id).orElseThrow(() -> new ResourceNotFoundException("BudgetExpense id Not Found:" + budget_expense_id));
		}
		
		public BudgetExpense updateBudgetExpense(BudgetExpense bud) {
			return bud_repo.save(bud);
		}
		
		public void delete1(Integer budget_expense_id) {
			bud_repo.findById(budget_expense_id)
			.orElseThrow(() -> new ResourceNotFoundException("BudgetExpense Not Found:" + budget_expense_id));
			bud_repo.delete1(budget_expense_id);
		}
		
		public void delete2(Integer budget_expense_id) {
			bud_repo.findById(budget_expense_id)
			.orElseThrow(() -> new ResourceNotFoundException("BudgetExpense Not Found:" + budget_expense_id));
			bud_repo.delete2(budget_expense_id);
		}
		
		public BudgetExpense budgetExpenseBySchoolIdFinancialIdAndBudgetDate(Integer school_id,Integer financial_year_id, Date from_date, Date to_date ) {
			return bud_repo.budgetExpenseBySchoolIdFinancialIdAndBudgetDate(school_id,financial_year_id,from_date,to_date );
		}
		
		public BudgetExpense budgetExpenseBySchoolIdFinancialIdAndBudgetDate1(Integer school_id,Integer financial_year_id, Date from_date, Date to_date ) {
			return bud_repo.budgetExpenseBySchoolIdFinancialIdAndBudgetDate1(school_id,financial_year_id,from_date,to_date );
		}
}
