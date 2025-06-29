package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
import org.springframework.web.multipart.MultipartFile;

import com.au.dto.JwtDetails;
import com.au.model.EnvBillDetails;
import com.au.response.ResponseHandler;
import com.au.service.EnvBillDetailsService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
public class EnvBillDetailsController {


	Logger log = LoggerFactory.getLogger(EnvBillDetailsController.class);
	
	@Autowired
	private EnvBillDetailsService envBillDetailsService;

	@Autowired
	private JwtTokenService jwt_service;
	
	
	@PostMapping("/saveEnvBillDetails")
	public ResponseEntity<Object> saveEnvBillDetails(@RequestBody @Valid EnvBillDetails envBillDetails,
			@RequestHeader("Authorization") String jwtToken) throws Exception {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			envBillDetails.setCreated_by(jwtDetails.getUserId());
			envBillDetails.setCreated_username(jwtDetails.getUserName());

			EnvBillDetails envBillDetail = envBillDetailsService.saveEnvBillDetails(envBillDetails);
			ResponseEntity<Object> envBillDetail_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					envBillDetail);
			return envBillDetail_response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getAllActiveEnvBillDetails")
	public ResponseEntity<Object> getAllActiveEnvBillDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<EnvBillDetails> envBillDetails = envBillDetailsService.getAllActiveEnvBillDetails();
		ResponseEntity<Object> envBillDetails_response= ResponseHandler.generateResponse(true, HttpStatus.OK, envBillDetails);
		return envBillDetails_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getEnvBillDetails/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	EnvBillDetails envBillDetails = envBillDetailsService.get(id);
	    	ResponseEntity<Object> envBillDetails_response= ResponseHandler.generateResponse(true, HttpStatus.OK, envBillDetails);
			return envBillDetails_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@PutMapping("/updateEnvBillDetails/{id}")
	public ResponseEntity<Object> updateEnvBillDetails(@RequestBody @Valid EnvBillDetails envBillDetails,
			@PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    	envBillDetails.setModified_by(jwtDetails.getUserId());
	    	envBillDetails.setModified_username(jwtDetails.getUserName());
	    	envBillDetailsService.updateEnvBillDetails(envBillDetails);
	    	ResponseEntity<Object> envBillDetails_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return envBillDetails_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@DeleteMapping("/deActivateEnvBillDetails/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			envBillDetailsService.deactivate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@DeleteMapping("/activateEnvBillDetails/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			envBillDetailsService.activate(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllEnvBillDetails")
	public ResponseEntity<Object> fetchAllAdministrationEmailIdsDetail(@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value = "created_by", required = false) Integer created_by,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,
			 @RequestParam(value = "date_range", required = false) String dateRange,
		        @RequestParam(value = "start_date", required = false) String startDate,
		        @RequestParam(value = "end_date", required = false) String endDate) {
		
//		if(RateLimitController.bucket.tryConsume(1)) {
//		Sort sorted = Sort.by(Direction.DESC, sort );
//		if(keyword != null) {	
//			Pageable pageable = PageRequest.of(page, page_size,sorted);
//			System.out.println("page, page_size, sorted, keyword");
//			ResponseEntity<Object> oc_filtered =  envBillDetailsService.getAllDataFilteredByKeyword(pageable,created_by, keyword);
//			return oc_filtered;
//		} else {
//			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
//			System.out.println("page, page_size, sorted");
//			ResponseEntity<Object> oc_sorted = envBillDetailsService.getAllSortedData(pageable1,created_by);
//			return oc_sorted;
//		}
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
	
		 Sort sorted = Sort.by(Direction.DESC, sort);
		    Pageable pageable = PageRequest.of(page, page_size, sorted);

		    LocalDate start = null;
		    LocalDate end = null;
		    LocalDate minDate = envBillDetailsService.getMinimumPaymentVoucherDate(); // dynamic minDate

		    if (dateRange != null) {
		        switch (dateRange) {
		            case "week":
		                start = LocalDate.now().minusWeeks(1);
		                if (start.isBefore(minDate)) start = minDate;
		                end = LocalDate.now();
		                break;
		            case "month":
		                start = LocalDate.now().minusMonths(1);
		                if (start.isBefore(minDate)) start = minDate;
		                end = LocalDate.now();
		                break;
		            case "custom":
		                try {
		                    if (startDate != null && endDate != null) {
		                        start = LocalDate.parse(startDate);
		                        if (start.isBefore(minDate)) start = minDate;
		                        end = LocalDate.parse(endDate);
		                    }
		                } catch (DateTimeParseException e) {
		                    return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd");
		                }
		                break;
		            case "today":
		                start = LocalDate.now();
		                end = LocalDate.now();
		                break;
		        }
		    }

		    return (keyword != null)
		            ? envBillDetailsService.getAllDataFilteredByKeyword(pageable, keyword, start, end, minDate,created_by)
		            : envBillDetailsService.getAllSortedData(pageable, start, end, minDate,created_by);
		}		
	
	@PostMapping(value = "/EnvBillDetailsUploadFile")
	public ResponseEntity<Object> uploadFile(@RequestParam MultipartFile multipartFile,
			@RequestParam Integer env_bill_details_id) throws IOException {
		envBillDetailsService.uploadFile(multipartFile, env_bill_details_id);
		ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}	
	
	
	@GetMapping(path = "/EnvBillDetailsFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = envBillDetailsService.viewFiles(fileName);
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
	
	@GetMapping("/getEnvBillDetailsdata")
	public ResponseEntity<Object> getEnvBillDetailsdata() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> shift_details = envBillDetailsService.getEnvBillDetailsdata();
			ResponseEntity<Object> shift_details_data = ResponseHandler.generateResponse(true, HttpStatus.OK, shift_details);
			return shift_details_data;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getEnvBillDetailsId")
	public ResponseEntity<Object> getEnvBillDetailsId(@RequestParam(value = "journal_voucher_id", required = false) Integer journal_voucher_id,
			@RequestParam(value = "payment_voucher_id", required = false) Integer payment_voucher_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String, Object>> shift_details = envBillDetailsService.getEnvBillDetailsId(journal_voucher_id,payment_voucher_id);
			ResponseEntity<Object> shift_details_data = ResponseHandler.generateResponse(true, HttpStatus.OK, shift_details);
			return shift_details_data;
			
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
}
