package com.au.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.au.dto.JwtDetails;
import com.au.dto.OtherFeeDetailsDTO;
import com.au.dto.OtherFeeTemplateDTO;
import com.au.model.AcademicsProgramVision;
import com.au.response.ResponseHandler;
import com.au.service.OtherFeeDetailsService;

@RestController
@RequestMapping("/api/otherFeeDetails")
@CrossOrigin
public class OtherFeeDetailsController {
	
	@Autowired
	private OtherFeeDetailsService otherFeeDetailsService;

	@PostMapping("/createOtherFees")
	public ResponseEntity<Object> createOtherFees(@RequestBody  OtherFeeTemplateDTO otherFeeTemplateDTO,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		return otherFeeDetailsService.createOtherFees(otherFeeTemplateDTO,jwtToken);
	}
	
	@GetMapping("/getOtherFeetemplate")
	public ResponseEntity<Object> getOtherFeetemplate(@RequestParam("pageNo") Integer pageNo,@RequestParam("pageSize") Integer pageSize)
			throws Exception {
		return otherFeeDetailsService.getOtherFeetemplate(pageNo,pageSize);
	}
	
	@GetMapping("/getOtherFeeDetails")
	public ResponseEntity<Object> getOtherFeeDetails(@RequestParam("otherFeeTemplateId") Integer otherFeeTemplateId,@RequestParam("feeType") String feeType)
			throws Exception {
		return otherFeeDetailsService.getOtherFeeDetails(otherFeeTemplateId,feeType);
	}

	@PutMapping("/updateOtherFeeDetails")
	public ResponseEntity<Object> updateOtherFeeDetails(@RequestBody List<OtherFeeDetailsDTO> otherFeeTemplateDTOs,@RequestParam("otherFeeTemplateId") Integer otherFeeTemplateId,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		return otherFeeDetailsService.updateOtherFeeDetails(otherFeeTemplateDTOs,otherFeeTemplateId,jwtToken);
	}
	
	@DeleteMapping("/deleteOtherFeeDetails")
	public ResponseEntity<Object> deleteOtherFeeDetails(@RequestParam("otherFeeDetailId") Integer otherFeeDetailId,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		return otherFeeDetailsService.deleteOtherFeeDetails(otherFeeDetailId,jwtToken);
	}
	
	@GetMapping("/getProgramsDetails")
	public ResponseEntity<Object> getProgramsDetails(@RequestParam(value =  "schoolId", required = false) Integer schoolId,@RequestParam(value="acYearId", required = false) Integer acYearId)
			throws Exception {
		return otherFeeDetailsService.getProgramsDetails(schoolId,acYearId);
	}
	
	@GetMapping("/getVoucherHeads")
	public ResponseEntity<Object> getVoucherHeads()
			throws Exception {
		return otherFeeDetailsService.getVoucherHeads();
	}
	
	@DeleteMapping("/deleteOtherFeeTemplate")
	public ResponseEntity<Object> deleteOtherFeeTemplate(@RequestParam("otherFeeTemplateId") Integer otherFeeTemplateId,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		return otherFeeDetailsService.deleteOtherFeeTemplate(otherFeeTemplateId,jwtToken);
	}
	
	@GetMapping("/reactiveOtherFeetemplate")
	public ResponseEntity<Object> reactiveOtherFeetemplate(@RequestParam("otherFeeTemplateId") Integer otherFeeTemplateId,@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		return otherFeeDetailsService.reactiveOtherFeetemplate(otherFeeTemplateId,jwtToken);
	}
	
	
	@GetMapping("/getOtherFeeDetailsData")
	public List<Map<String, Object>> getOtherFeeDetailsData(@RequestParam(value =  "schoolId", required = false) Integer schoolId,@RequestParam(value="acYearId", required = false) Integer acYearId ,
			@RequestParam(value="programId", required = false) Integer programId , @RequestParam(value="programSpecializationId", required = false) List<Integer> programSpecializationId)
			throws Exception {
		return otherFeeDetailsService.getOtherFeeDetailsData(schoolId,acYearId,programId,programSpecializationId);
	}
	
	
	@GetMapping("/getOtherFeeDetailsData1")
	public List<Map<String, Object>> getOtherFeeDetailsData1(@RequestParam(value =  "fee_template_id", required = false) Integer fee_template_id )
			throws Exception {
		return otherFeeDetailsService.getOtherFeeDetailsData1(fee_template_id);
	}
	
	
	@DeleteMapping("/deActivateOtherFeeDetails/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			otherFeeDetailsService.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateOtherFeeDetails/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			otherFeeDetailsService.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
}
