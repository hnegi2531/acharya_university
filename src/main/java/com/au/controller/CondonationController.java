package com.au.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.CondonationDTO;
import com.au.service.CondonationService;



@RestController
@RequestMapping("/api/condonation")
@CrossOrigin
public class CondonationController {

	
	@Autowired
	private CondonationService condonationSerivce;
	
	@PostMapping("/saveCondonation")
	public ResponseEntity<Object> saveCondonation(@RequestBody CondonationDTO condonationDTO){
		return condonationSerivce.saveCondonation(condonationDTO);
	}
	
	@GetMapping("/getCondonationList")
	public ResponseEntity<Object> getCondonationList(){
		return condonationSerivce.getCondonationList();
	}
	
	@PostMapping("/selectCondonationForPrincipalScreen")
	public ResponseEntity<Object> selectCondonationForPrincipalScreen(@RequestBody CondonationDTO condonationDTO){
		return condonationSerivce.selectCondonationForPrincipalScreen(condonationDTO);
	}
	
	@GetMapping("/getCondonationListForPrincipalScreen")
	public ResponseEntity<Object> getCondonationListForPrincipalScreen(@RequestParam("condonationId") Long condonationId){
		return condonationSerivce.getCondonationListForPrincipalScreen(condonationId);
	}
	
	@PostMapping("/approveCondonationListForPrincipalScreen")
	public ResponseEntity<Object> approveCondonationListForPrincipalScreen(@RequestBody CondonationDTO condonationDTOs){
		return condonationSerivce.approveCondonationListForPrincipalScreen(condonationDTOs);
	}
}

