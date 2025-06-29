package com.au.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.au.dto.JwtDetails;
import com.au.dto.MessMealRequestDto;
import com.au.model.MealRefreshmentRequest;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.MealRefreshmentRequestService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class MealRefreshmentRequestController {
	
Logger log = LoggerFactory.getLogger(MealRefreshmentRequestController.class);
	
	@Autowired
	private MealRefreshmentRequestService mealref_ser;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@PostMapping("/MealRefreshmentRequest")
	public ResponseEntity<Object> saveMealRefreshmentRequest(@RequestBody MealRefreshmentRequest meal, @RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException{
		if(RateLimitController.bucket.tryConsume(1)) {
		
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		meal.setCreated_by(jwtDetails.getUserId());
		meal.setCreated_username(jwtDetails.getUserName());
		MealRefreshmentRequest mrr = mealref_ser.saveMealRefreshmentRequest(meal);
		ResponseEntity<Object> mrr_response1= ResponseHandler.generateResponse(true, HttpStatus.CREATED, mrr);
		return mrr_response1 ;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}	
	}
	
	@PostMapping("/MealRefreshmentRequestForMultipleDates")
	public ResponseEntity<Object> MealRefreshmentRequestForMultipleDates(@RequestBody MessMealRequestDto meal_dto, @RequestHeader("Authorization") String jwtToken)
			throws Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		meal_dto.setCreated_by(jwtDetails.getUserId());
		meal_dto.setCreated_username(jwtDetails.getUserName());
		List<MealRefreshmentRequest> mrr = mealref_ser.MealRefreshmentRequestForMultipleDates(meal_dto);
		ResponseEntity<Object> mrr_response1= ResponseHandler.generateResponse(true, HttpStatus.CREATED, mrr);
		return mrr_response1 ;
	}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}	
	}
	
	@GetMapping("/getActiveMealRefreshmentRequest")
	public ResponseEntity<Object> listAll1() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<MealRefreshmentRequest> mrr_list = mealref_ser.listAll1();
			ResponseEntity<Object> mrr_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, mrr_list);
			return mrr_list_response;
		}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}	
	}	
	
	/*  FOR INDENT PAGE   */
	@GetMapping("/fetchAllMealRefreshmentRequestDetails")
	public ResponseEntity<Object> getAllMealRefreshmentRequestDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword,@RequestParam(value="user_id") Integer user_id) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> mealRefreshment_filtered =  mealref_ser.getAllDataFilteredByKeyword(pageable, keyword ,user_id);//,column,value);
			return mealRefreshment_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> mealRefreshment_sorted = mealref_ser.getAllSortedData(pageable1,user_id);
			return mealRefreshment_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/MealRefreshmentRequest/{refreshment_id}")
	public ResponseEntity<Object> get(@PathVariable Integer refreshment_id ) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			MealRefreshmentRequest mrr = mealref_ser.get(refreshment_id);
			ResponseEntity<Object> mrr_list= ResponseHandler.generateResponse(true, HttpStatus.OK, mrr);
			return mrr_list;
			} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@GetMapping("/getMealRefreshmentRequestByRefreshmentId/{refreshment_id}")
	public ResponseEntity<Object> getMealRefreshmentRequestByRefreshmentId(@PathVariable Integer refreshment_id ) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			HashMap<String, Object> mrr = mealref_ser.getMealRefreshmentRequestByRefreshmentId(refreshment_id);
			ResponseEntity<Object> mrr_list= ResponseHandler.generateResponse(true, HttpStatus.OK, mrr);
			return mrr_list;
			} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	@PutMapping("/updateMealRefreshmentRequest/{refreshment_id}")
	public ResponseEntity<Object> updateMealRefreshmentRequest(@RequestBody MealRefreshmentRequest meal , 
			@PathVariable Integer refreshment_id , @RequestHeader("Authorization") String jwtToken )
					throws Exception, JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			meal.setModified_by(jwtDetails.getUserId());
			meal.setModified_username(jwtDetails.getUserName());
			mealref_ser.saveUpdateMealRefreshmentRequest(meal);
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
	
	@DeleteMapping("/deactivateMealRefreshmentRequest/{refreshment_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer refreshment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		mealref_ser.delete(refreshment_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

		
	@DeleteMapping("/activateMealRefreshmentRequest/{refreshment_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer refreshment_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		mealref_ser.delete1(refreshment_id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllMealRefreshmentRequestDetailsEndUserOnly")
	public ResponseEntity<Object> fetchAllMealRefreshmentRequestDetailsEndUserOnly(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> mealRefreshment_filtered =  mealref_ser.getAllDataFilteredByKeyword11(pageable, keyword);//,column,value);
			return mealRefreshment_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> mealRefreshment_sorted = mealref_ser.getAllSortedData11(pageable1);
			return mealRefreshment_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	
	@GetMapping("/fetchAllMealRefreshmentRequestDetailsForMess")
	public ResponseEntity<Object> fetchAllMealRefreshmentRequestDetailsForMess(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> mealRefreshment_filtered =  mealref_ser.getAllDataFilteredByKeyword2(pageable, keyword);//,column,value);
			return mealRefreshment_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> mealRefreshment_sorted = mealref_ser.getAllSortedData2(pageable1);
			return mealRefreshment_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	
	@GetMapping("/fetchAllMealRefreshmentRequestDetailsForEmailIndex")
	public ResponseEntity<Object> fetchAllMealRefreshmentRequestDetailsForEmailIndex(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> mrr_list = mealref_ser.fetchAllMealRefreshmentRequestDetailsForEmailIndex();
			ResponseEntity<Object> mrr_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, mrr_list);
			return mrr_list_response;
		}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}	
	}
	
	@GetMapping("/getFilteredEndUserData/{date}")
	public ResponseEntity<Object> getFilteredEndUserData(@PathVariable String date) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> mrr_list = mealref_ser.getFilteredEndUserData(date);
			ResponseEntity<Object> mrr_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, mrr_list);
			return mrr_list_response;
		}else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
		}	
	}
	
	@PostMapping("/emailToEndUSerForApprovalOfFoodRequest/{meal_date}")
	public Object emailToEndUSerForApprovalOfFoodRequest(@PathVariable String meal_date) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				mealref_ser.emailToEndUSerForApprovalOfFoodRequest(meal_date);
				ResponseEntity<Object> offer_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return offer_response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response;
				}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@PostMapping("/emailToVendorForSupplyOfFoodRequest/{meal_date}")
	public Object emailToVendorForSupplyOfFoodRequest(@PathVariable String meal_date) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				mealref_ser.emailToVendorForSupplyOfFoodRequest(meal_date);
				mealref_ser.updateMailStatus(meal_date);
				ResponseEntity<Object> offer_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return offer_response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response;
				}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}

	@GetMapping("/messDataForCalendarView/{date}")
	public Object messDataForCalendarView(@PathVariable String date) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				List<Map<String, Object>> mrr = mealref_ser.messDataForCalendarView(date);
				ResponseEntity<Object> offer_response=ResponseHandler.generateResponse(true, HttpStatus.OK, mrr);
				return offer_response;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response;
				}
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/getMealRefreshmentRequestById/{refreshment_id}")
	public ResponseEntity<Object> getMealRefreshmentRequestById(@PathVariable Integer refreshment_id ) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			HashMap<String, Object> mrr = mealref_ser.getMealRefreshmentRequestById(refreshment_id);
			ResponseEntity<Object> mrr_list= ResponseHandler.generateResponse(true, HttpStatus.OK, mrr);
			return mrr_list;
			} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}	
	
	@GetMapping("/getRatePerCount/{meal_id}/{voucher_head_new_id}")
	public ResponseEntity<Object> getMealRefreshmentRequestById(@PathVariable Integer meal_id, @PathVariable Integer voucher_head_new_id ) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			Integer rate = mealref_ser.getMealRefreshmentRequestById(meal_id, voucher_head_new_id);
			ResponseEntity<Object> mrr_list= ResponseHandler.generateResponse(true, HttpStatus.OK, rate);
			return mrr_list;
			} catch (NoSuchElementException e) {
			ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}	
	
	
//	@GetMapping("/fetchAllMealRefreshmentRequestDetailsForEmailIndexReport")
//	public ResponseEntity<Object> fetchAllMealRefreshmentRequestDetailsForEmailIndexReport(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
//			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
//		
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<HashMap<String, Object>> mrr_list = mealref_ser.fetchAllMealRefreshmentRequestDetailsForEmailIndexReport();
//			ResponseEntity<Object> mrr_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, mrr_list);
//			return mrr_list_response;
//		}else {
//		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//		return rs;
//		}	
//	}	
	
	/*  FOR REPORT PAGE   */
	@GetMapping("/fetchAllMealRefreshmentRequestDetailsForEmailIndexReport")
	public ResponseEntity<Object> fetchAllMealRefreshmentRequestDetailsForEmailIndexReport(
			@RequestParam(value="page") Integer page,
			@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort,
			@RequestParam(value = "approved_status",required = true) Integer approved_status,
			@RequestParam(value="date") String date,                                                                          /* 06-05-2024 ( month = 05 & year=2024) */
			@RequestParam(value="keyword",required = false) Object keyword) throws ParseException{
		
		String month = (date.substring(3, 5));
		String year  = (date.substring(6, 10));
		
		if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(approved_status == 1) {
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> mealRefreshment_filtered =  mealref_ser.getAllDataFilteredByKeyword1(pageable,keyword,month,year,approved_status);//,column,value);
				return mealRefreshment_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> mealRefreshment_sorted = mealref_ser.getAllSortedData1(pageable1,month,year,approved_status);
				return mealRefreshment_sorted;
			}
			}else if(approved_status == 2) {
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> mealRefreshment_filtered =  mealref_ser.getAllDataFilteredByKeyword1Pending(pageable,keyword,month,year,approved_status);//,column,value);
					return mealRefreshment_filtered;
				} else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> mealRefreshment_sorted = mealref_ser.getAllSortedData1Pending(pageable1,month,year,approved_status);
					return mealRefreshment_sorted;
				}
				}
			else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		
	}else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}

	}
//		if(RateLimitController.bucket.tryConsume(1)) {
//			List<HashMap<String, Object>> mrr_list = mealref_ser.fetchAllMealRefreshmentRequestDetailsForEmailIndexReport();
//			ResponseEntity<Object> mrr_list_response = ResponseHandler.generateResponse(true, HttpStatus.OK, mrr_list);
//			return mrr_list_response;
//		}else {
//		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
//		return rs;
//		}	
		
	
	@GetMapping("/getVendorData/{meal_id}")
	public ResponseEntity<Object> getVendorData(@PathVariable Integer meal_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> list_voucher_head_new = mealref_ser.getVendorData(meal_id);
					ResponseEntity<Object> list_voucher_head_new_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_voucher_head_new);
					return list_voucher_head_new_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}			
	}	
	
	
	@GetMapping("/getMealRefreshmentRequests")
	public ResponseEntity<Object> getMealRefreshmentRequests(@RequestParam("voucher_head_new_id") Integer voucher_head_new_id,
															 @RequestParam("month") Integer month,@RequestParam("year") Integer year) {
		return mealref_ser.getMealRefreshmentRequests(voucher_head_new_id,month,year);
				
	}	


}
