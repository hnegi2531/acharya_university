package com.au.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.BudgetDto;
import com.au.dto.BudgetItemDTO;
import com.au.dto.BudgetStatusDto;
import com.au.dto.EmployeeMedicalHistoryDto;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.BatchAssignment;
import com.au.model.Budget;
import com.au.model.CourseObjective;
import com.au.model.EmployeeDetails;
import com.au.repository.BudgetRepository;
import com.au.repository.DepartmentAssignmentRepository;
import com.au.repository.VoucherHeadNewRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class BudgetService {

	@Autowired
	private BudgetRepository budgetRepository;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private DepartmentAssignmentRepository deptAssign_repository;
	
	@Autowired
	private VoucherHeadNewRepository voucherHeadNewRepository;
	
	 Integer count1 = 0;
	public List<Budget> createBudget(@Valid BudgetDto budgetDto,String jwtToken) throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		List<Budget> budgetList = new ArrayList();
		 List<Integer> budgetId = budgetRepository.getBudgetId(
         		budgetDto.getSchool_id(),
         		budgetDto.getDept_id(),
         		budgetDto.getFinancial_year_id()
             
         );
		System.out.println("========================== " + ObjectUtils.isNotEmpty(budgetId));
		 if (ObjectUtils.isNotEmpty(budgetId)) {
			    budgetId.stream().forEach(bId -> {
			        // Check if count1 is within bounds
			            Budget budget1 = budgetRepository.findById(bId).orElseThrow(() -> new ResourceNotFoundException(" Budget Details Not Found " + bId));
			            
			            System.out.println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ "+count1);
			            budget1.setEmp_id(budgetDto.getEmp_id());
			            budget1.setLedger_id(budgetDto.getLedgerItems().get(count1).getLedger_id());
			            budget1.setProposed_amount(budgetDto.getLedgerItems().get(count1).getProposed_amount());
			            budget1.setRecommended_amount(budgetDto.getLedgerItems().get(count1).getRecommended_amount());
			            budget1.setApproved_amount(budgetDto.getLedgerItems().get(count1).getApproved_amount());
			            budget1.setRemark(budgetDto.getRemark());
			            budget1.setActive(budgetDto.getActive());

			            budget1.setModified_by(jwtDetails.getUserId());
			            budget1.setModified_date(budgetDto.getModified_date()); // Set the current date for modified date
			            budget1.setModified_username(jwtDetails.getUserName());

			            budgetList.add(budget1);
			            count1++;
			    });
			    
			} else {
				System.out.println("77777777777777777777777777777777");
				for (BudgetItemDTO ledgerItem : budgetDto.getLedgerItems()) {
		            Integer count = budgetRepository.getCount(
		            		budgetDto.getSchool_id(),
		            		budgetDto.getDept_id(),
		            		budgetDto.getFinancial_year_id(),
		                ledgerItem.getVoucher_head_new_id()
		            );
		 System.out.println("################################### " +count);
		            if (count >= 1) {
		                throw new RuntimeException("Budget with the combination of School, Department, Financial Year, and Voucher Head already exists!");
		            }
				}
		
			for (BudgetItemDTO ledgerItem : budgetDto.getLedgerItems()) {
	            	Budget budget = new Budget();
	            budget.setEmp_id(budgetDto.getEmp_id());
	            budget.setFinancial_year_id(budgetDto.getFinancial_year_id());
	            budget.setSchool_id(budgetDto.getSchool_id());
	            budget.setDept_id(budgetDto.getDept_id());
	            budget.setLedger_id(ledgerItem.getLedger_id());
	            budget.setVoucher_head_new_id(ledgerItem.getVoucher_head_new_id());
	            budget.setProposed_amount(ledgerItem.getProposed_amount());
	            budget.setRecommended_amount(ledgerItem.getRecommended_amount());
	            budget.setApproved_amount(ledgerItem.getApproved_amount());
	            budget.setRemark(budgetDto.getRemark());
	            budget.setActive(budgetDto.getActive());
	
	            budget.setCreated_by(budgetDto.getCreated_by());
	            budget.setCreated_date(budgetDto.getCreated_date());
	            budget.setCreated_username(budgetDto.getCreated_username());
//	            budget.setModified_by(jwtDetails.getUserId());
//	            budget.setModified_date(budgetDto.getModified_date()); // Set the current date for modified date
//	            budget.setModified_username(jwtDetails.getUserName());
	            budget.setActive(budgetDto.getActive());
	            budget.setLock_status(budgetDto.getLock_status());
	            budget.setLock_date(budgetDto.getLock_date());
	            
	            budgetList.add(budget);
	        }
		 }
            budgetRepository.saveAll(budgetList);
            count1 = 0;
		return budgetList;
    }		        
//	 public List<Budget> createBudget(@Valid BudgetDto budgetDto) throws Exception {
//	        List<Budget> budgetList = new ArrayList<>();
//
//	        // Step 1: Iterate over ledger items to check for existing budgets and process accordingly
//	        for (BudgetItemDTO ledgerItem : budgetDto.getLedgerItems()) {
//	            Budget budget;
//	            Optional<Budget> existingBudgetOptional;
//
//	            // Check if budget_id is provided to update existing budget
//	            if (ledgerItem.getBudget_id() != null) {
//	                existingBudgetOptional = budgetRepository.findById(ledgerItem.getBudget_id());
//
//	                if (existingBudgetOptional.isPresent()) {
//	                    // Update existing budget
//	                    budget = existingBudgetOptional.get();
//	                    budget.setEmp_id(budgetDto.getEmp_id());
//	                    budget.setLedger_id(ledgerItem.getLedger_id());
//	                    budget.setProposed_amount(ledgerItem.getProposed_amount());
//	                    budget.setRecommended_amount(ledgerItem.getRecommended_amount());
//	                    budget.setApproved_amount(ledgerItem.getApproved_amount());
//	                    budget.setRemark(budgetDto.getRemark());
//	                    budget.setActive(budgetDto.getActive());
//
//	                    budget.setModified_by(budgetDto.getModified_by());
//	                    budget.setModified_date(budgetDto.getModified_date()); // Set the current date for modified date
//	                    budget.setModified_username(budgetDto.getModified_username());
//	                } else {
//	                    throw new RuntimeException("Budget with ID " + ledgerItem.getBudget_id() + " does not exist.");
//	                }
//	            } else {
//	                // Check if a budget exists with the same combination
//	                Integer count = budgetRepository.getCount(
//	                        budgetDto.getSchool_id(),
//	                        budgetDto.getDept_id(),
//	                        budgetDto.getFinancial_year_id(),
//	                        ledgerItem.getVoucher_head_new_id()
//	                );
//
//	                if (count > 0) {
//	                    throw new RuntimeException("Budget with the combination of School, Department, Financial Year, and Voucher Head already exists!");
//	                }
//
//	                // Create a new budget
//	                budget = new Budget();
//	                budget.setEmp_id(budgetDto.getEmp_id());
//	                budget.setFinancial_year_id(budgetDto.getFinancial_year_id());
//	                budget.setSchool_id(budgetDto.getSchool_id());
//	                budget.setDept_id(budgetDto.getDept_id());
//	                budget.setLedger_id(ledgerItem.getLedger_id());
//	                budget.setVoucher_head_new_id(ledgerItem.getVoucher_head_new_id());
//	                budget.setProposed_amount(ledgerItem.getProposed_amount());
//	                budget.setRecommended_amount(ledgerItem.getRecommended_amount());
//	                budget.setApproved_amount(ledgerItem.getApproved_amount());
//	                budget.setRemark(budgetDto.getRemark());
//	                budget.setActive(budgetDto.getActive());
//
//	                budget.setCreated_by(budgetDto.getCreated_by());
//	                budget.setCreated_date(budgetDto.getCreated_date()); // Set the current date for created date
//	                budget.setCreated_username(budgetDto.getCreated_username());
//	            }
//
//	            // Save the budget (either newly created or updated)
//	            budgetRepository.save(budget);
//	            budgetList.add(budget);
//	        }
//
//	        return budgetList;
//	    }		
	
	
	public List<Budget> getAllBudget() {
		return budgetRepository.getAllPettyCash();
	}
	
	public List<Budget> get(List<Integer> id) {
		return budgetRepository.findAllById(id);
	}
	
	
	public List<Budget> updateBudget(List<Budget> cos) {
		return budgetRepository.saveAll(cos);
	}


	public void deactivate(Integer id) {
		Budget budget = budgetRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Budget Not Found:" + id));
		budgetRepository.deactivate(id);
	}

	public void activate(Integer id) {
		Budget dept = budgetRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Budget Not Found:" + id));
		budgetRepository.activate(id);
	}
	
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> oc_filtered_response = budgetRepository.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> oc_sorted_response = budgetRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, oc_sorted_response);
	}
	
	
	public List<Map<String,Object>> getDeptDetailsBasedOnSchoolId(Integer school_id) {
		return deptAssign_repository.getDeptDetailsBasedOnSchoolId(school_id);
	}
	
	
	public List<HashMap<String, Object>> getVoucherHeadDataBasedOnBudget() {
		return voucherHeadNewRepository.getVoucherHeadDataBasedOnBudget();
	}


	public List<Map<String, Object>> getAllBudgetDetailsData(Integer financial_year_id, Integer school_id, Integer dept_id) {
		return budgetRepository.getAllBudgetDetailsData(financial_year_id,school_id,dept_id);
	}
	
	
	
	public List<Budget> updateBudgetStatus(BudgetStatusDto dto, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		
		List<Budget> updatedBudgets = new ArrayList<>();
		
		dto.getBudget_id().stream().forEach(budgetDto ->{
			Budget budget = budgetRepository.findById(budgetDto).orElseThrow(()-> new ResourceNotFoundException("Employee not found"));
			
			budget.setLock_status(dto.getLock_status());
			budget.setModified_by(jwtDetails.getUserId());
			budget.setModified_username(jwtDetails.getUserName());
			LocalDate currentDate = LocalDate.now();
	        String currentDateString = currentDate.toString();
			budget.setLock_date(currentDateString);
			updatedBudgets.add(budgetRepository.save(budget));
		});
		return updatedBudgets; 
	}
	
	
}
