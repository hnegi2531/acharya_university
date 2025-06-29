package com.au.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.bind.annotation.RestController;
import com.au.dto.JwtDetails;
import com.au.model.Offer;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.OfferService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class OfferController {

	Logger log = LoggerFactory.getLogger(OfferController.class);

	@Autowired
	private OfferService os_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/Offer")
	public ResponseEntity<Object> saveOffer(@RequestBody @Valid Offer offer,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			offer.setCreated_by(jwtDetails.getUserId());
			offer.setCreatedUsername(jwtDetails.getUserName());
			Offer off = os_service.saveOffer(offer);
			ResponseEntity<Object> offer_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, off);
			return offer_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
		}

	@GetMapping("/Offer")
	public ResponseEntity<Object> getAllOfferDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> offer_filtered =  os_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return offer_filtered;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> offer_sorted = os_service.getAllSortedData(pageable1);
			return offer_sorted;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllOfferDetails/{offer_id}")
	public ResponseEntity<Object> listAll1(@PathVariable Integer offer_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> offer = os_service.listAll1(offer_id);
		ResponseEntity<Object> offer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, offer);
		return offer_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/fetchAllOfferDetailsByEmployeeId/{employeeId}")
	public ResponseEntity<Object> fetchAllOfferDetailsByEmployeeId(@PathVariable Integer employeeId) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> offer = os_service.fetchAllOfferDetailsByEmployeeId(employeeId);
		ResponseEntity<Object> offer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, offer);
		return offer_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/encryptedFetchAllOfferDetailsByEmployeeId/{employeeId}")
	public ResponseEntity<Object> encryptedFetchAllOfferDetailsByEmployeeId(@PathVariable Integer employeeId) {
		if(RateLimitController.bucket.tryConsume(1)) {
		Map<String, Object> offer = os_service.encryptedFetchAllOfferDetailsByEmployeeId(employeeId);
		ResponseEntity<Object> offer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, offer);
		return offer_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}

	@GetMapping("/Offer/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {

			Offer offer = os_service.get(id);
//			LocalDate date1 = offer.getDate_of_joining().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//			LocalDate repotDate = date1.plusDays(1);//while fetching date from DB, minus 1 day we are getting 
//			ZoneId defaultZoneId = ZoneId.systemDefault();
//			Date date = Date.from(repotDate.atStartOfDay(defaultZoneId).toInstant());
//			
//			DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//			String reportingDatestring = repotDate.format(pattern);
//			offer.setDate_of_joining(date);
			
			ResponseEntity<Object> offer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, offer);
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

	@PutMapping("/Offer/{id}")
	public ResponseEntity<Object> update(@RequestBody Offer offer, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		try {
			// Offer existProduct = os_service.get(id);
			offer.setModified_by(jwtDetails.getUserId());
			offer.setModifiedUsername(jwtDetails.getUserName());
			os_service.saveOffer(offer);
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

	@DeleteMapping("/Offer/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		os_service.delete(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/OfferLetter/{offer_id}")
	public ResponseEntity<Object> saveOfferLetter(@RequestBody @Valid Offer offer, @PathVariable Integer offer_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		Offer off = os_service.getDetails(offer);
		ResponseEntity<Object> offer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, off);
		return offer_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}

	@PostMapping("/emailForOffer") 
	public Object emailForOffer(@RequestParam("url_domain") String url_domain,@RequestParam("job_id") Integer job_id, @RequestParam("offer_id") Integer offer_id) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
				os_service.sendMailForOffer1(url_domain, job_id, offer_id);
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

	@PostMapping("/emailForStaff/{job_id}/{offer_id}")
	public Object emailForStaff(@PathVariable Integer job_id,@PathVariable Integer offer_id) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {
		        os_service.sendMailForStaff(job_id,offer_id);
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
	
	@PutMapping("/updateOfferAfterAccepting")
	public ResponseEntity<Object> updateOfferAfterAccepting(@RequestParam(value ="offer_id") Integer offer_id, 
			@RequestParam(value ="offerstatus") Boolean offerstatus, @RequestParam(value ="ip_address") String ip_address) {
		if(RateLimitController.bucket.tryConsume(1)) {
		os_service.updateOfferAfterAccepting(offer_id, offerstatus, ip_address);
		ResponseEntity<Object> offer_response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return offer_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@GetMapping("/offerDetailsByJobId/{job_id}")
	public ResponseEntity<Object> offerDetailsByJobId(@PathVariable Integer job_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<Offer> offer = os_service.offerDetailsByJobId(job_id);
		ResponseEntity<Object> offer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, offer);
		return offer_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}
	
	@PutMapping("/updateMultipleOfferDetails/{offer_ids}")
	public ResponseEntity<Object> updateMultipleOfferDetails(@RequestBody @Valid List<Offer> offer, @PathVariable List<Integer> offer_ids) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Offer> off = os_service.getMultipleOfferDetails(offer_ids);
		os_service.updateMultipleOfferDetails(offer);
		ResponseEntity<Object> offer_response= ResponseHandler.generateResponse(true, HttpStatus.OK, off);
		return offer_response;
	} else {
		ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		return rs;
	}
	}

}
