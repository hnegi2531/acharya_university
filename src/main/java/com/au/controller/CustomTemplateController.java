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
import com.au.dto.CustomTemplateDTO;
import com.au.service.CustomTemplateService;



@RestController
@RequestMapping("/api/customtemplate")
@CrossOrigin
public class CustomTemplateController {

	
	@Autowired
	private CustomTemplateService customTemplateService;
	
	@PostMapping("/createCustomTemplate")
	public ResponseEntity<Object> createCustomTemplate(@RequestBody CustomTemplateDTO customTemplateDTO){
		return customTemplateService.createCustomTemplate(customTemplateDTO);
	}
	
	@GetMapping("/getCustomTemplateList")
	public ResponseEntity<Object> getCustomTemplateList(@RequestParam("pageNo") Integer pageNo,@RequestParam("pageSize") Integer pageSize){
		return customTemplateService.getCustomTemplateList(pageNo,pageSize);
	}
	
	@GetMapping("/getCustomTemplateByReferenceNo")
	public ResponseEntity<Object> getCustomTemplateByReferenceNo(@RequestParam("referenceNo") String referenceNo){
		return customTemplateService.getCustomTemplateByReferenceNo(referenceNo);
	}
}
