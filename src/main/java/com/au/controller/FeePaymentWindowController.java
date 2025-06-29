package com.au.controller;


import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.au.controller.RateLimitController;
import com.au.dto.FeePaymentWindowDto;
import com.au.dto.HostelBedAssignmentDto;
import com.au.dto.JwtDetails;
import com.au.model.FeePaymentWindow;
import com.au.model.VoucherHeadNew;
import com.au.response.ResponseHandler;
import com.au.service.FeePaymentWindowService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class FeePaymentWindowController {
	
	Logger log=LoggerFactory.getLogger(FeePaymentWindowController.class);
	
	@Autowired
	private FeePaymentWindowService feePaymentWindowService;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/feePaymentWindow")
	public ResponseEntity<Object> createFeePaymentWindow(@RequestBody @Valid FeePaymentWindow feePayment,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			feePayment.setCreated_by(jwtDetails.getUserId());
			feePayment.setCreated_username(jwtDetails.getUserName());
			FeePaymentWindow fee = feePaymentWindowService.createFeePayment(feePayment);
			ResponseEntity<Object> fee_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, fee);
		return fee_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getvoucherHeadDetailsBasedOnSchoolId")
	public ResponseEntity<Object> getvoucherHeadDetailsBasedOnSchoolId(@RequestParam(value="school_id") Integer school_id,@RequestParam(value="from_date") String from_date,
			@RequestParam(value="to_date") String to_date) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<VoucherHeadNew> list_voucher = feePaymentWindowService.getvoucherHeadDetailsBasedOnSchoolId(school_id,from_date,to_date);
			ResponseEntity<Object> list_voucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_voucher);
			return list_voucher_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
		
	
	@PostMapping(value = "/feePaymentWindowUploadFile")
	public ResponseEntity<Object> uploadFile(@RequestParam MultipartFile multipartFile,
			@RequestParam Integer fee_payment_window_id) throws IOException {
		feePaymentWindowService.uploadFile(multipartFile, fee_payment_window_id);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(path = "/feePaymentWindowFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = feePaymentWindowService.viewFiles(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	
	@GetMapping("/fetchAllFeePaymentWindow")
	public ResponseEntity<Object> getAllDdDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value="user_id",required = false) String user_id, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> dd_filtered =  feePaymentWindowService.getAllDataFilteredByKeyword(pageable,user_id, keyword);//,column,value);
			return dd_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> dd_sorted = feePaymentWindowService.getAllSortedData(pageable1,user_id);
			return dd_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/feePaymentDetailsForPayment/{paymentType}")
	public ResponseEntity<Object> feePaymentDetailsForPayment( @PathVariable String paymentType,@RequestHeader("Authorization") String jwtToken) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return feePaymentWindowService.feePaymentDetailsForPayment(paymentType,jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	@GetMapping("/feePaymentDetailsForExamFeeByStudentId/{studentId}")
	public ResponseEntity<Object> feePaymentDetailsForExamFeeByStudetId( @PathVariable Integer studentId,@RequestHeader("Authorization") String jwtToken) {

		if (RateLimitController.bucket.tryConsume(1)) {

			return feePaymentWindowService.feePaymentDetailsForExamFeeByStudetId(studentId,jwtToken);

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}

	}
	
	
	@GetMapping("/getFeePaymentWindow/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	FeePaymentWindow fpw = feePaymentWindowService.get(id);
	    	ResponseEntity<Object> fpw_response= ResponseHandler.generateResponse(true, HttpStatus.OK, fpw);
			return fpw_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/updateFeePaymentWindow/{id}")
	public ResponseEntity<Object> updateFeePaymentWindow(@RequestBody @Valid FeePaymentWindow feePaymentWindow, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	feePaymentWindow.setModified_by(jwtDetails.getUserId());
	    	feePaymentWindow.setModified_username(jwtDetails.getUserName());
	    	feePaymentWindowService.updateFeePaymentWindow(feePaymentWindow);
	    	ResponseEntity<Object> feePaymentWindow_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return feePaymentWindow_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@GetMapping("/getFeePaymentWindowBasedOnUserId")
	public ResponseEntity<Object> getFeePaymentWindowBasedOnUserId(@RequestParam(value="user_id") Integer user_id) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> list_voucher = feePaymentWindowService.getFeePaymentWindowBasedOnUserId(user_id);
			ResponseEntity<Object> list_voucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_voucher);
			return list_voucher_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/getBulkPayTransaction")
	public ResponseEntity<Object> getBulkPayTransaction(@RequestParam(value="fee_payment_window_id") Integer fee_payment_window_id) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> list_voucher = feePaymentWindowService.getBulkPayTransaction(fee_payment_window_id);
			ResponseEntity<Object> list_voucher_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_voucher);
			return list_voucher_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

}
