package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.dto.FeeHeadAmountRestrictionAttachmentRequest;
import com.au.dto.FeeHeadAmountRestrictionResponse;
import com.au.model.FeeHeadAmountRestriction;
import com.au.response.ResponseHandler;
import com.au.service.FeeHeadAmountRestrictionService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/${secretkey1}")
@CrossOrigin
public class FeeHeadAmountRestrictionController {
	
	Logger log = LoggerFactory.getLogger(FeeHeadAmountRestrictionController.class);
	
	@Autowired
	private FeeHeadAmountRestrictionService feeHeadAmountRestrictionService;
	

	
	@PostMapping("/createFeeHeadAmountRestriction")
	public ResponseEntity<Object> saveFeeHeadAmountRestriction(@RequestBody @Valid List<FeeHeadAmountRestriction> feeHeadAmountRestriction,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<FeeHeadAmountRestrictionResponse> feeHeadAmountRestrictionResponse =feeHeadAmountRestrictionService.saveFeeHeadAmountRestriction(feeHeadAmountRestriction,jwtToken);
		ResponseEntity<Object> feeHeadAmountRestrictionDetailsResponse= ResponseHandler.generateResponse(true, HttpStatus.CREATED, feeHeadAmountRestrictionResponse);
		return feeHeadAmountRestrictionDetailsResponse;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}
	}
	
	@GetMapping("/getActiveFeeHeadAmountRestriction")
	public ResponseEntity<Object>  getActiveFeeHeadAmountRestriction(){
		if(RateLimitController.bucket.tryConsume(1)) {
			List<FeeHeadAmountRestriction> feeHeadAmountRestrictionResponseList = feeHeadAmountRestrictionService.getActiveFeeHeadAmountRestriction();
				ResponseEntity<Object>  feeHeadAmountRestrictionResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, feeHeadAmountRestrictionResponseList);
				return feeHeadAmountRestrictionResponse;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/fetchAllFeeHeadAmountRestrictionDetails")
	public ResponseEntity<Object> fetchAllFeeHeadAmountRestrictionDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,@RequestParam(value="user_id",required = false) Integer user_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {
				if(user_id != null) { 
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> searchedAndSortedFeeHeadAmountRestrictionDetails = feeHeadAmountRestrictionService.fetchAllFeeHeadAmountRestrictionDetailsByKeywordAndUserId(pageable, keyword,user_id);//,column,value);
					return searchedAndSortedFeeHeadAmountRestrictionDetails;
				} else {
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> searchedAndSortedFeeHeadAmountRestrictionDetails = feeHeadAmountRestrictionService.fetchAllFeeHeadAmountRestrictionDetailsByKeyword(pageable, keyword);//,column,value);
					return searchedAndSortedFeeHeadAmountRestrictionDetails;
				} 
				
			}else {
				if(user_id != null) {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> sortedFeeHeadAmountRestrictionDetails = feeHeadAmountRestrictionService.fetchAllSortedFeeHeadAmountRestrictionDetailsAndUserId(pageable1,user_id);
					return sortedFeeHeadAmountRestrictionDetails;
				} else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> sortedFeeHeadAmountRestrictionDetails = feeHeadAmountRestrictionService.fetchAllSortedFeeHeadAmountRestrictionDetails(pageable1);
					return sortedFeeHeadAmountRestrictionDetails;
				}
				
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/FeeHeadAmountRestrictionDetailByFeeHeadAmountRestrictionId/{feeHeadAmountRestrictionId}")
	public ResponseEntity<Object> FeeHeadAmountRestrictionDetailByfeeHeadAmountRestrictionId(@PathVariable Integer feeHeadAmountRestrictionId) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						FeeHeadAmountRestriction feeHeadAmountRestrictionDetail = feeHeadAmountRestrictionService.FeeHeadAmountRestrictionDetailByfeeHeadAmountRestrictionId(feeHeadAmountRestrictionId);
						ResponseEntity<Object> feeHeadAmountRestrictionDetailResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, feeHeadAmountRestrictionDetail);
						return feeHeadAmountRestrictionDetailResponse;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/updateFeeHeadAmountRestrictionDetailByFeeHeadAmountRestrictionId/{feeHeadAmountRestrictionId}")
	public ResponseEntity<?> updateFeeHeadAmountRestrictionDetailByfeeHeadAmountRestrictionId(@RequestBody @Valid FeeHeadAmountRestriction updateRequest, @PathVariable Integer feeHeadAmountRestrictionId,@RequestHeader("Authorization")  String jwtToken)
	      throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
					
					feeHeadAmountRestrictionService.updateFeeHeadAmountRestrictionDetailByfeeHeadAmountRestrictionId(updateRequest,feeHeadAmountRestrictionId,jwtToken);
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
					return response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/deactivateFeeHeadAmountRestrictionDetail/{feeHeadAmountRestrictionId}")
	public ResponseEntity<Object> deactivateFeeHeadAmountRestrictionDetail(@PathVariable Integer feeHeadAmountRestrictionId) {
		if(RateLimitController.bucket.tryConsume(1)) {
				feeHeadAmountRestrictionService.deactivateFeeHeadAmountRestrictionDetail(feeHeadAmountRestrictionId);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@DeleteMapping("/activateFeeHeadAmountRestrictionDetail/{feeHeadAmountRestrictionId}")
	public ResponseEntity<Object> activateFeeHeadAmountRestrictionDetail(@PathVariable Integer feeHeadAmountRestrictionId) {
		if(RateLimitController.bucket.tryConsume(1)) {
				feeHeadAmountRestrictionService.activateFeeHeadAmountRestrictionDetail(feeHeadAmountRestrictionId);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}
	
	@PostMapping(value="/feeHeadAmountRestrictionUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute FeeHeadAmountRestrictionAttachmentRequest feeHeadAmountRestrictionAttachmentRequest) throws IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			System.out.println("Hello-------" + auth.getDetails());
			log.debug("Message For VendorAttachment");
			List<FeeHeadAmountRestriction> feeHeadAmountRestriction = feeHeadAmountRestrictionService.uploadFile(feeHeadAmountRestrictionAttachmentRequest.getFile() , feeHeadAmountRestrictionAttachmentRequest.getFeeHeadAmountRestrictionIds());
			ResponseEntity<Object> va_response= ResponseHandler.generateResponse(true, HttpStatus.OK, feeHeadAmountRestriction);
			return va_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}
	
	@GetMapping(path = "/feeHeadAmountRestrictionFileDownload")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("attachmentPath") final String attachmentPath) {
		try {
			final byte[] data = feeHeadAmountRestrictionService.downloadFile(attachmentPath);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type","application/pdf")
					.header("Content-disposition", "attachment; filename=\"" + attachmentPath + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}
	}
	
	@GetMapping("/feeHeadAmountRestrictionDetailsForPayment/{feeHeadAmountRestrictionId}")
	public ResponseEntity<Object> feeHeadAmountRestrictionDetailsForPayment(@PathVariable Integer feeHeadAmountRestrictionId) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						List<HashMap<String,Object>> feeHeadAmountRestrictionDetail= feeHeadAmountRestrictionService.feeHeadAmountRestrictionDetailsForPayment(feeHeadAmountRestrictionId);
						ResponseEntity<Object> feeHeadAmountRestrictionDetailResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, feeHeadAmountRestrictionDetail);
						return feeHeadAmountRestrictionDetailResponse;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/feeHeadAmountRestrictionPaidDetails/{feeHeadAmountRestrictionId}")
	public ResponseEntity<Object> feeHeadAmountRestrictionPaidDetails(@PathVariable Integer feeHeadAmountRestrictionId) {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
	    	
						Map<String,Object> feeHeadAmountRestrictionDetail= feeHeadAmountRestrictionService.feeHeadAmountRestrictionPaidDetails(feeHeadAmountRestrictionId);
						ResponseEntity<Object> feeHeadAmountRestrictionDetailResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, feeHeadAmountRestrictionDetail);
						return feeHeadAmountRestrictionDetailResponse;
				} catch (NoSuchElementException e) {
						ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
						return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
}	
