package com.au.controller;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.ParseException;
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

import com.au.dto.JwtDetails;
import com.au.dto.VendorFileRequest;
import com.au.dto.storeIndentDto;
import com.au.model.EmployeeDetails;
import com.au.model.StoreIndentRequest;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.StoreIndentRequestService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey5}")
@CrossOrigin
public class StoreIndentRequestController {
	
	Logger log = LoggerFactory.getLogger(StoreIndentRequestController.class);
	
 	@Autowired
    private StoreIndentRequestService sir_Service;

    @Autowired
    private JwtTokenService jwt_service;

    
    @PostMapping("/storeIndentRequest")
	public ResponseEntity<Object> saveStoreIndentRequest(@RequestBody @Valid List<StoreIndentRequest> sir,@RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		sir.stream().forEach(a -> {

			a.setCreated_by(jwtDetails.getUserId());
			a.setCreated_username(jwtDetails.getUserName());
			});
		List<StoreIndentRequest> store_indent_req = sir_Service.saveStoreIndentRequest(sir);
		ResponseEntity<Object> store_indent_req_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, store_indent_req);
		return store_indent_req_response;
	} else {
		ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return response;
		}
	}
    
	@GetMapping("/allActiveStoreIndentRequest")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<StoreIndentRequest> sir = sir_Service.listAll1();
		ResponseEntity<Object> store_indent_req_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sir);
		return store_indent_req_response;
	} else {
		ResponseEntity<Object> response=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return response;
	}
	}
	
	
	@GetMapping("/storeIndentRequest/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
	    try {
	    	
	    	StoreIndentRequest sir = sir_Service.get(id);
	    	ResponseEntity<Object> store_indent_req_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sir);
			return store_indent_req_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllStoreIndentRequest")
	public ResponseEntity<Object> getAllExamDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="created_by" , required = false) Integer created_by,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> store_indent_req_filtered =  sir_Service.getAllDataFilteredByKeyword(pageable, keyword , created_by);
			return store_indent_req_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> store_indent_req_sorted = sir_Service.getAllSortedData(pageable1, created_by);
			return store_indent_req_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllStoreIndentRequestBasedOnUserId")
	public ResponseEntity<Object> fetchAllStoreIndentRequestBasedOnUserId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,@RequestParam(value = "created_by") Integer created_by, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> store_indent_req_filtered =  sir_Service.fetchAllStoreIndentRequestBasedOnUserIdByKeyword(pageable, keyword,created_by);
			return store_indent_req_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> store_indent_req_sorted = sir_Service.fetchAllStoreIndentRequestBasedOnUserIdSortedData(pageable1,created_by);
			return store_indent_req_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@PutMapping("/updateStoreIndentRequest/{ids}")
	public ResponseEntity<Object> update(@RequestBody @Valid List<StoreIndentRequest> sir, @PathVariable List<Integer> ids,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			  try {
	    	sir.stream().forEach(b -> {

	    		b.setCreated_by(jwtDetails.getUserId());
				b.setCreated_username(jwtDetails.getUserName());
				});
	    	
	    	List<StoreIndentRequest> store_indent_req =	sir_Service.updateStoreIndentRequest(sir);
	    	
	    	ResponseEntity<Object> store_indent_req_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return store_indent_req_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	
	@PutMapping("/updateReceiveStatus/{store_indent_request_id}")
	public ResponseEntity<Object> updateReceiveStatus(storeIndentDto sir, 
			@PathVariable List<Integer> store_indent_request_id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			  try {

				  storeIndentDto store_indent_req = sir_Service.updateReceiveStatus(sir);
			ResponseEntity<Object> sir_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return sir_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@DeleteMapping("/storeIndentRequest/{id}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			sir_Service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateStoreIndentRequest/{id}")
	public ResponseEntity<Object> activate(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			sir_Service.delete1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getEmpNameConcatWithDate")
	public ResponseEntity<Object> getEmpNameConcatWithDate() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Map<String,Object>> cos = sir_Service.getEmpNameConcatWithDate();
		ResponseEntity<Object> store_indent_req_response= ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		return store_indent_req_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
//	@GetMapping("/getItemApproverdata1")
//	public ResponseEntity<Object> getItemApprover() {
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<Map<String,Object>> exam_details_for_hall_ticket = sir_Service.getItemApprover();
//			ResponseEntity<Object> exam_details_for_hall_ticket_response = ResponseHandler.generateResponse(true, HttpStatus.OK, exam_details_for_hall_ticket);
//			return exam_details_for_hall_ticket_response;
//		} else {
//			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//			return rs;
//		}
//	}
	
	@GetMapping("/getItemApproverdata")
	public ResponseEntity<Object> getItemApproverdata(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> store_indent_req_filtered =  sir_Service.getAllDataFilteredByKeyword1(pageable, keyword);
			return store_indent_req_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> store_indent_req_sorted = sir_Service.getAllSortedData1(pageable1);
			return store_indent_req_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getDataForDisplaying/{store_indent_request_id}")
	public ResponseEntity<Object> getDataForDisplaying(@PathVariable Integer store_indent_request_id) 
			throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
			List<Map<String, Object>> sir = sir_Service.getDataForDisplaying(store_indent_request_id);
			ResponseEntity<Object> store_indent_req_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sir);
			return store_indent_req_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/getDataForDisplaying2")
	public ResponseEntity<Object> getDataForDisplaying2(@RequestParam String indent_ticket) 
			throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
			List<Map<String, Object>> sir = sir_Service.getDataForDisplaying2(indent_ticket);
			ResponseEntity<Object> store_indent_req_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sir);
			return store_indent_req_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@PutMapping("/updateAvailableItemInStore/{ids}")
	public ResponseEntity<Object> updateAvailableItemInStore(@RequestBody @Valid List<StoreIndentRequest> sir, 
			@PathVariable List<Integer> ids,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			  try {
	    	sir.stream().forEach(b -> {

	    		b.setCreated_by(jwtDetails.getUserId());
				b.setCreated_username(jwtDetails.getUserName());
				});
	    	
	    	List<StoreIndentRequest> store_indent_req =	sir_Service.updateAvailableItemInStore(sir);
	    	
	    	ResponseEntity<Object> store_indent_req_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return store_indent_req_response;
	    } catch (NoSuchElementException e) {
	    	ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		} 
	}
	
	@GetMapping("/getItemApproverDataBasedOnIndentTicket")
	public ResponseEntity<Object> getItemApproverDataBasedOnIndentTicket(@RequestParam String indent_ticket )
			throws ParseException {
	if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String,Object>> get_item_approver_data = sir_Service.getItemApproverDataBasedOnIndentTicket(indent_ticket);
			ResponseEntity<Object> store_indent_req_response = ResponseHandler.generateResponse(true, HttpStatus.OK, get_item_approver_data);
		return store_indent_req_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/getApprovedData")
	public ResponseEntity<Object> getApproveddata(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> store_indent_req_filtered =  sir_Service.getApprovedDataFilteredByKeyword1(pageable, keyword);
			return store_indent_req_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> store_indent_req_sorted = sir_Service.getApprovedDataSortedData1(pageable1);
			return store_indent_req_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getItemApproverdataBasedOnUserId")
	public ResponseEntity<Object> getItemApproverdataBasedOnUserId(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,
			@RequestParam(value="user_id") Integer user_id) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> store_indent_req_filtered =  sir_Service.getItemApproverdataBasedOnUserIdByKeyword1(pageable,user_id, keyword);
			return store_indent_req_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> store_indent_req_sorted = sir_Service.getItemApproverdataBasedOnUserId(pageable1,user_id);
			return store_indent_req_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/getApprovedData/{id}")
	public ResponseEntity<Object> getApprovedData(@PathVariable Integer id) 
			throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
			List<Map<String, Object>> sir = sir_Service.getApprovedData(id);
			ResponseEntity<Object> store_indent_req_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sir);
			return store_indent_req_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@GetMapping("/getDataForDisplayingAfterIssued")
	public ResponseEntity<Object> getDataForDisplaying(@RequestParam String indent_ticket) 
			throws ParseException {
		if(RateLimitController.bucket.tryConsume(1)) {
			
			List<Map<String, Object>> sir = sir_Service.getDataForDisplaying(indent_ticket);
			ResponseEntity<Object> store_indent_req_response= ResponseHandler.generateResponse(true, HttpStatus.OK, sir);
			return store_indent_req_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	@PostMapping(value = "/storeIndentRequestUploadFile")
	public ResponseEntity<Object> uploadFile(@ModelAttribute VendorFileRequest vendorFileRequest) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("Message For Attachment");
		sir_Service.uploadFile(vendorFileRequest.getFile(), vendorFileRequest.getIndent_ticket());
		 ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
	}
	
	@GetMapping(path = "/storeIndentRequestFileviews")
	public ResponseEntity<ByteArrayResource> viewFiles(@RequestParam("fileName") final String fileName) {
		try {
			final byte[] data = sir_Service.viewFiles(fileName);
			final ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "store_indent_request_id/pdf")
					.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
					.header("Cache-Control", "no-cache").body(resource);
		} catch (NoSuchFileException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.badRequest().contentLength(0).body(null);
		}

	}
	
	
	@GetMapping("/storeIndentRequestApprovedData")
	public ResponseEntity<Object> storeIndentRequestApprovedData(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {

		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> storeIndentRequestApprovedData =  sir_Service.storeIndentRequestApprovedDataByKeyword(pageable, keyword);
				return storeIndentRequestApprovedData;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> storeIndentRequestApprovedData = sir_Service.storeIndentRequestApprovedDataSortedData(pageable1);
				return storeIndentRequestApprovedData;
			}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}	
	
	
	@GetMapping("/getApprovedStoreIndentRequestByIndentTicket")
	public ResponseEntity<Object> getApprovedStoreIndentRequestByIndentTicket(@RequestParam String indentTicket)
			throws ParseException {
	if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String,Object>> approvedStoreIndentRequest= sir_Service.getApprovedStoreIndentRequestByIndentTicket(indentTicket);
			ResponseEntity<Object> approvedStoreIndentRequestResponse = ResponseHandler.generateResponse(true, HttpStatus.OK, approvedStoreIndentRequest);
		return approvedStoreIndentRequestResponse;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}	
	
	@GetMapping("/getStoreIndentdetailsByStoreIndentId")
	public ResponseEntity<Object> getStoreIndentdetailsByStoreIndentId(@RequestParam("env_item_id") Integer  envItemId)
			{
	  return sir_Service.getStoreIndentdetailsByStoreIndentId(envItemId);
	}	
	
	
	
}

