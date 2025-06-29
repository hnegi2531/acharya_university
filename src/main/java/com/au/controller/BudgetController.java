package com.au.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

import com.au.dto.BudgetDto;
import com.au.dto.BudgetItemDTO;
import com.au.dto.BudgetStatusDto;
import com.au.dto.EmployeeMedicalHistoryDto;
import com.au.dto.JwtDetails;
import com.au.model.Budget;
import com.au.model.CourseObjective;
import com.au.response.ResponseHandler;
import com.au.service.BudgetService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class BudgetController {

	Logger log = LoggerFactory.getLogger(BudgetController.class);
	
	@Autowired
	private BudgetService budgetService;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/createBudget")
	public ResponseEntity<Object> createBudget(@RequestBody @Valid BudgetDto budgetDTO,
			@RequestHeader("Authorization") String jwtToken)
					throws JsonParseException, JsonMappingException, IOException,Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			budgetDTO.setCreated_by(jwtDetails.getUserId());
			budgetDTO.setCreated_username(jwtDetails.getUserName());
			List<Budget> budget = budgetService.createBudget(budgetDTO,jwtToken);
			ResponseEntity<Object> budget_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, budget);
		return budget_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getAllBudget")
	public ResponseEntity<Object> getAllBudget() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Budget> bu = budgetService.getAllBudget();
		ResponseEntity<Object> budget_response= ResponseHandler.generateResponse(true, HttpStatus.OK, bu);
		return budget_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	
	@GetMapping("/getBudget/{id}")
	public ResponseEntity<Object> get(@PathVariable List<Integer> id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	List<Budget> cos = budgetService.get(id);
	    	ResponseEntity<Object> co_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
			return co_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/updateBudget/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<Budget> budget,
			@PathVariable List<Integer> id, @RequestHeader("Authorization") String jwtToken) throws Exception{
		if (RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				budget.stream().forEach(p -> {

					p.setCreated_by(jwtDetails.getUserId());
					p.setCreated_username(jwtDetails.getUserName());
				});
				budgetService.updateBudget(budget);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (JsonParseException | JsonMappingException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e) {
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@DeleteMapping("/deactivateBudget/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			budgetService.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateBudget/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			budgetService.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllBudget")
	public ResponseEntity<Object> getAllDept(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> oc_filtered =  budgetService.getAllDataFilteredByKeyword(pageable, keyword);
			return oc_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> oc_sorted = budgetService.getAllSortedData(pageable1);
			return oc_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getDeptDetailsBasedOnSchoolId/{school_id}")
	public ResponseEntity<Object> getDeptDetailsBasedOnSchoolId(@PathVariable Integer school_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> list_syllabus = budgetService.getDeptDetailsBasedOnSchoolId(school_id);
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/getVoucherHeadDataBasedOnBudget")
	public ResponseEntity<Object> getVoucherHeadDataBasedOnBudget() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> listOfVoucherHeadNew = budgetService.getVoucherHeadDataBasedOnBudget();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, listOfVoucherHeadNew);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	
	@GetMapping("/getAllBudgetDetailsData/{financial_year_id}/{school_id}/{dept_id}")
	public ResponseEntity<Object> getAllBudgetDetailsData(@PathVariable Integer financial_year_id,@PathVariable Integer school_id,@PathVariable Integer dept_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> list_syllabus = budgetService.getAllBudgetDetailsData(financial_year_id,school_id,dept_id);
			ResponseEntity<Object> list_syllabus_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_syllabus);
			return list_syllabus_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	
	@PutMapping("/updateBudgetStatus")
	public ResponseEntity<Object> updateBudgetStatus(@RequestBody BudgetStatusDto dto, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			budgetService.updateBudgetStatus(dto, jwtToken);
			ResponseEntity<Object> emp_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return emp_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
}
