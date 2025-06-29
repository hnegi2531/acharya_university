package com.au.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.FineSlabDTO;
import com.au.service.FineSlabService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FineSlabController {
	
	@Autowired
	private FineSlabService fineSlabService;
	
	@PostMapping("/createFineSlab")
	public ResponseEntity<Object> createFineSlab(@RequestBody FineSlabDTO fineSlabDTO) {
		 return  fineSlabService.createFineSlab(fineSlabDTO);
		
	}
	
	@GetMapping("/getFineSlab")
	public ResponseEntity<Object> getFineSlab() {
		 return  fineSlabService.getFineSlab();
		
	}
	
	@GetMapping("/getFineSlabById")
	public ResponseEntity<Object> getFineSlabById(@RequestParam("fineSlabId") Integer fineSlabId) {
		 return  fineSlabService.getFineSlabById(fineSlabId);
		
	}

	@PutMapping("/updateFineSlab")
	public ResponseEntity<Object> updateFineSlab(@RequestBody FineSlabDTO fineSlabDTO) {
		 return  fineSlabService.updateFineSlab(fineSlabDTO);
		
	}
}
