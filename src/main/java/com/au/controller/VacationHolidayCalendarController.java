package com.au.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

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
import com.au.model.VacationHolidayCalendar;
import com.au.response.ResponseHandler;
import com.au.service.VacationHolidayCalendarService;
import com.au.service.JwtTokenService;
import com.au.service.VacationHolidayCalendarService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class VacationHolidayCalendarController {
	
	Logger log = LoggerFactory.getLogger(VacationHolidayCalendarController.class);

	@Autowired
	private VacationHolidayCalendarService vhc_Service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/createVacationHolidayCalendar")
	public ResponseEntity<Object> createVacationHolidayCalendar(@RequestBody @Valid VacationHolidayCalendar fts,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception, JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			fts.setCreatedBy(jwtDetails.getUserId());
			fts.setCreatedUsername(jwtDetails.getUserName());
			VacationHolidayCalendar vacation = vhc_Service.createVacationHolidayCalendar(fts);
			ResponseEntity<Object> vacationresponse = ResponseHandler.generateResponse(true, HttpStatus.CREATED,
					vacation);
			return vacationresponse;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/allActiveVacationHolidayCalendar")
	public ResponseEntity<Object> listAll() {
		if (RateLimitController.bucket.tryConsume(1)) {
			List<VacationHolidayCalendar> vacation = vhc_Service.listAll();
			ResponseEntity<Object> vacation_response = ResponseHandler.generateResponse(true, HttpStatus.OK, vacation);
			return vacation_response;
		} else {
			ResponseEntity<Object> response = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return response;
		}
	}

	@GetMapping("/getVacationHolidayCalendar/{vacationId}")
	public ResponseEntity<Object> get(@PathVariable Integer vacationId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			try {

				VacationHolidayCalendar vacation = vhc_Service.get(vacationId);
				ResponseEntity<Object> vacation_response = ResponseHandler.generateResponse(true, HttpStatus.OK,
						vacation);
				return vacation_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/fetchAllVacationHolidayCalendar")
	public ResponseEntity<Object> fetchAllVacationHolidayCalendar(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page_size") Integer page_size, @RequestParam(value = "sort") String sort,
			@RequestParam(value = "keyword", required = false) Object keyword)
			throws JsonParseException, JsonMappingException, IOException {

		if (RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort);
			if (keyword != null) {
				Pageable pageable = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted, keyword");
				ResponseEntity<Object> vacation_filtered = vhc_Service.getAllDataFilteredByKeyword(pageable, keyword);
				return vacation_filtered;
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size, sorted);
				System.out.println("page, page_size, sorted");
				ResponseEntity<Object> vacation_sorted = vhc_Service.getAllSortedData(pageable1);
				return vacation_sorted;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@PutMapping("/updateVacationHolidayCalendar/{vacationId}")
	public ResponseEntity<Object> update(@RequestBody @Valid VacationHolidayCalendar pr,
			@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if (RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			try {
				pr.setModifiedBy(jwtDetails.getUserId());
				pr.setModifiedUsername(jwtDetails.getUserName());
				VacationHolidayCalendar vacation_req = vhc_Service.updateVacationHolidayCalendar(pr);

				ResponseEntity<Object> vacation_response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
						HttpStatus.OK);
				return vacation_response;
			} catch (NoSuchElementException e) {
				ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
						HttpStatus.NOT_FOUND);
				return response;
			}
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/deactivateVacationHolidayCalendar/{vacationId}")
	public ResponseEntity<Object> deactivate(@PathVariable Integer vacationId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			vhc_Service.delete(vacationId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activateVacationHolidayCalendar/{vacationId}")
	public ResponseEntity<Object> activate(@PathVariable Integer vacationId) {
		if (RateLimitController.bucket.tryConsume(1)) {
			vhc_Service.delete1(vacationId);
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}
	}

}
