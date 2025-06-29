package com.au.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.service.MisReportService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MisReportController {
	
	@Autowired
	private MisReportService misReportService;
	
	@GetMapping("/misInstituteWiseReport")
	public ResponseEntity<Object> misInstituteWiseReport(@RequestParam("acYearId") Integer acYearId) {
		 return  misReportService.misInstituteWiseReport(acYearId);
		
	} 
	
	@GetMapping("/misYearWiseReport")
	public ResponseEntity<Object> misYearWiseReport() {
		 return  misReportService.misYearWiseReport();
		
	} 
	
	@GetMapping("/misDayWiseReport")
	public ResponseEntity<Object> misDayWiseReport(@RequestParam("month") Integer month,@RequestParam("year") Integer year) {
		 return  misReportService.misDayWiseReport(month,year);
		
	} 
	
	@GetMapping("/misProgramWiseReport")
	public ResponseEntity<Object> misProgramWiseReport(@RequestParam("acYearId") Integer acYearId,@RequestParam("schoolId") Integer schoolId) {
		 return  misReportService.misProgramWiseReport(acYearId,schoolId);
		
	} 
	
	@GetMapping("/misGenderWiseReport")
	public ResponseEntity<Object> misGenderWiseReport(@RequestParam(value="acYearId",required=false) Integer acYearId,@RequestParam(value="schoolId", required=false) Integer schoolId) {
		 return  misReportService.misGenderWiseReport(acYearId,schoolId);
		
	} 
	
	@GetMapping("/misGeolocationWiseReport")
	public ResponseEntity<Object> misGeolocationWiseReport(@RequestParam(value="schoolId", required=false) Integer schoolId,@RequestParam(value="countryId", required=false) Integer countryId,@RequestParam(value="stateId", required=false) Integer stateId,@RequestParam(value="cityId", required=false) Integer cityId) {
		 return  misReportService.misGeolocationWiseReport(schoolId,countryId,stateId,cityId);
		
	} 

	@GetMapping("/getStudentDetailsGeoloactionWise")
	public ResponseEntity<Object> getStudentDetailsGeoloactionWise(@RequestParam(value="acYearId",required=false) Integer acYearId,@RequestParam(value="schoolId", required=false) Integer schoolId,@RequestParam(value="countryId", required=false) Integer countryId,@RequestParam(value="stateId", required=false) Integer stateId,@RequestParam(value="cityId", required=false) Integer cityId) {
		 return  misReportService.getStudentDetailsGeoloactionWise(acYearId,schoolId,countryId,stateId,cityId);
		
	} 
}
