package com.au.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.EmployeeListForIncrementCreationrequestDTO;
import com.au.dto.IncrementCreationDTO;
import com.au.dto.IncrementCreationResponseDTO;
import com.au.model.IncrementCreation;
import com.au.response.ResponseHandler;
import com.au.service.IncrementCreationService;


@RestController
@RequestMapping("/api/incrementCreation")
@CrossOrigin
public class IncrementCreationController {
	
     @Autowired
	private IncrementCreationService incrementCreationService;
	
	@PostMapping("/getEmployeeListForIncrementCreation")
	public ResponseEntity<Object> getEmployeeListForIncrementCreation(@RequestBody EmployeeListForIncrementCreationrequestDTO employeeListForIncrementCreationrequestDTO) throws ParseException{
		return incrementCreationService.getEmployeeListForIncrementCreation(employeeListForIncrementCreationrequestDTO);
	}
	
	@PostMapping("/getEmployeeOrrListForIncrementCreation")
	public ResponseEntity<Object> getEmployeeOrrListForIncrementCreation(@RequestBody EmployeeListForIncrementCreationrequestDTO employeeListForIncrementCreationrequestDTO) throws ParseException{
		return incrementCreationService.getEmployeeOrrListForIncrementCreation(employeeListForIncrementCreationrequestDTO);
	}
	
	@GetMapping("/getEmployeeDetailForIncrementCreation")
	public ResponseEntity<Object> getEmployeeDetailForIncrementCreation(@RequestParam("empId") Integer empId){
		return incrementCreationService.getEmployeeDetailForIncrementCreation(empId);
	}
	
	@GetMapping("/getIncrementCreation/{incrementCreationId}")
	public ResponseEntity<Object> getIncrementCreation(@PathVariable Long incrementCreationId){
		return incrementCreationService.getIncrementCreation(incrementCreationId);
	}
	
	@PostMapping("/saveIncrementCreationDetails")
	public ResponseEntity<Object> saveIncrementCreationDetails(@RequestBody IncrementCreationDTO incrementCreationDTO){
		return incrementCreationService.saveIncrementCreationDetails(incrementCreationDTO);
	}
	
	@GetMapping("/getDepartments")
	public ResponseEntity<Object> getDepartments(){
		return incrementCreationService.getDepartments();
	}
	
	@GetMapping("/getDesignations")
	public ResponseEntity<Object> getDesignations(){
		return incrementCreationService.getDesignations();
	}
	
	@GetMapping("/getsalaryStructures")
	public ResponseEntity<Object> getsalaryStructures(){
		return incrementCreationService.getsalaryStructures();
	}
	
	@PostMapping(value = "/uploadIncrementCreationFile",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadIncrementCreationFile(@RequestPart("file") MultipartFile file,@RequestParam("month") Integer month,@RequestParam("year") Integer year,
			@RequestHeader("Authorization") String jwtToken) {
		return incrementCreationService.uploadIncrementCreationFile(file,month,year,jwtToken);
		
	}
	
	@GetMapping("/getTemporaryIncrementCreationList")
	public ResponseEntity<Object> getTemporaryIncrementCreationList(){
		return incrementCreationService.getTemporaryIncrementCreationList();
	}
	
	@PostMapping("/saveIncrementCreationDetailsList")
	public ResponseEntity<Object> saveIncrementCreationDetailsList(@RequestBody List<IncrementCreationDTO> incrementCreationDTO){
		return incrementCreationService.saveIncrementCreationDetailsList(incrementCreationDTO);
	}
	
	@PostMapping("/incrementIsFinalize/{lisOfIds}")
	public ResponseEntity<Object> incrementIsFinalize(@PathVariable List<Long> lisOfIds){
		return incrementCreationService.incrementIsFinalize(lisOfIds);
	}
	
	@PostMapping("/incrementIsApproved/{lisOfIds}")
	public ResponseEntity<Object> incrementIsApproved(@PathVariable List<Long> lisOfIds){
		return incrementCreationService.incrementIsApproved(lisOfIds);
	}
	
	@PostMapping("/incrementIsRejected/{lisOfIds}")
	public ResponseEntity<Object> incrementIsRejected(@PathVariable List<Long> lisOfIds){
		return incrementCreationService.incrementIsRejected(lisOfIds);
	}
	
	@PutMapping("/updateIncrementCreationDetails")
	public ResponseEntity<Object> updateIncrementCreationDetails(@RequestBody List<IncrementCreation> incrementCreation){
		return incrementCreationService.updateIncrementCreationDetails(incrementCreation);
	}
	
	@GetMapping("/getIncrementFinalizeList")
	public ResponseEntity<Object> getIncrementFinalizeList(@RequestParam(value="school_id",required = false) Integer school_id,
			@RequestParam(value="dept_id",required = false) Integer dept_id,
			@RequestParam(value="month",required = false) Integer month){
		return incrementCreationService.getIncrementFinalizeList(school_id,dept_id,month);
	}
		
	@GetMapping("/getIncrementCreationList")
	public ResponseEntity<Object> getIncrementCreationList( @RequestParam(value="school_id",required = false) Integer school_id,
			@RequestParam(value="dept_id",required = false) Integer dept_id,
			@RequestParam(value="month",required = false) Integer month){
		return incrementCreationService.getIncrementCreationList(school_id,dept_id,month);
	}
	
	@GetMapping("/getIncrementApprovedList")
	public ResponseEntity<Object> getIncrementApprovedList( @RequestParam(value="school_id",required = false) Integer school_id,
			@RequestParam(value="dept_id",required = false) Integer dept_id,
			@RequestParam(value="month",required = false) Integer month){
		return incrementCreationService.getIncrementApprovedList(school_id,dept_id,month);
	}
	
	@GetMapping("/getIncrementByIncrementId")
	public ResponseEntity<Object> getIncrementByIncrementId(@RequestParam("incrementId") Long getIncrementByIncrementId){
		return incrementCreationService.getIncrementByIncrementId(getIncrementByIncrementId);
	}
	
	@PostMapping(value = "/uploadIncrementFile",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadIncrementCreationFile(@RequestPart("file") MultipartFile file,@RequestPart("request") String request) {
		return incrementCreationService.uploadIncrementCreationFile(file,request);
		
	}
	

	@GetMapping(path = "/downloadIncrementCreationFile")
	public ResponseEntity<ByteArrayResource> downloadIncrementCreationFile(@RequestParam("fileName") final String pathName) {
		try {
			final byte[] data = incrementCreationService.downloadIncrementCreationFile(pathName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + pathName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (Exception e) {
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	
	@DeleteMapping("/deactiveIncrementCreation/{incrementCreationId}")
	public ResponseEntity<Object> deactivate(@PathVariable Long incrementCreationId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			incrementCreationService.deactivate(incrementCreationId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateIncrementCreation/{incrementCreationId}")
	public ResponseEntity<Object> activate(@PathVariable Long incrementCreationId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			incrementCreationService.activate(incrementCreationId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/getAllIncrementsOfMonthYear")
	public ResponseEntity<Object> getAllIncrementsOfMonthYear(@RequestParam Integer month, @RequestParam Integer year) throws ParseException{
		return incrementCreationService.getAllIncrementsOfMonthYear(month, year);
	}

}
