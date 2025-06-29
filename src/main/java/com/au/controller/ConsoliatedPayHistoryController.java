package com.au.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.au.dto.ConsoliatedAmountDTO;
import com.au.dto.ConsoliatedPayHistoryDTO;
import com.au.service.ConsoliatedPayHistoryService;

@RestController
@RequestMapping("/api/consoliation")
@CrossOrigin
public class ConsoliatedPayHistoryController {
	
	@Autowired
	private ConsoliatedPayHistoryService consoliatedPayHistoryService;
	
	@GetMapping("/getEmployeeDetailsForConsoliation")
	public ResponseEntity<Object> getEmployeeDetailsForConsoliation(@RequestParam("empId" ) Integer empId,@RequestParam("month" ) Integer month,@RequestParam("year" ) Integer year){
		return consoliatedPayHistoryService.getEmployeeDetailsForConsoliation(empId,month,year);
	}
	
	@PostMapping("/saveConsoliation")
	public ResponseEntity<Object> saveConsoliation(@RequestBody ConsoliatedPayHistoryDTO consoliatedPayHistoryDTO){
		return consoliatedPayHistoryService.saveConsoliation(consoliatedPayHistoryDTO);
	}

	@GetMapping("/getConsoliationList")
	public ResponseEntity<Object> getConsoliationList(@RequestParam("month" ) Integer month,@RequestParam("year" ) Integer year,@RequestParam(name="schoolId",required = false ) Integer schoolId){
		return consoliatedPayHistoryService.getConsoliationList(month,year,schoolId);
	}
	
	@PostMapping("/updateConsoliation")
	public ResponseEntity<Object> updateConsoliation(@RequestBody ConsoliatedPayHistoryDTO consoliatedPayHistoryDTO){
		return consoliatedPayHistoryService.updateConsoliation(consoliatedPayHistoryDTO);
	}
	
	@GetMapping("/getConsultants")
	public ResponseEntity<Object> getConsultants(@RequestParam("month") Integer month,@RequestParam("year") Integer year){
		return consoliatedPayHistoryService.getConsultants(month,year);
	}
	
	@PostMapping("/saveAdditionAmount")
	public ResponseEntity<Object> saveAdditionAmount(@RequestBody ConsoliatedAmountDTO consoliatedAmountDTO){
		return consoliatedPayHistoryService.saveAdditionAmount(consoliatedAmountDTO);
	}
	
	@GetMapping("/getConsoliationListByEmpId")
	public ResponseEntity<Object> getConsoliationListByEmpId(@RequestParam("empId" ) Integer empId){
		return consoliatedPayHistoryService.getConsoliationListByEmpId(empId);
	}

	@GetMapping("/getMonthWisePaymentHistory/{consoliatedAmountId}")
	public ResponseEntity<Object> getMonthWisePaymentHistory(@PathVariable Integer consoliatedAmountId){
		return consoliatedPayHistoryService.getMonthWisePaymentHistory(consoliatedAmountId);
	}
}
