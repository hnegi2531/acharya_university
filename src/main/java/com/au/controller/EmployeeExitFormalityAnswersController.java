package com.au.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.au.dto.EmployeeExitFormalityAnswersDto;
import com.au.dto.JwtDetails;
import com.au.model.EmployeeExitFormalityAnswers;
import com.au.response.ResponseHandler;
import com.au.service.EmployeeExitFormalityAnswersService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey2}")
@CrossOrigin
public class EmployeeExitFormalityAnswersController {

	
	@Autowired
	private EmployeeExitFormalityAnswersService eefa_service;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	Logger log = LoggerFactory.getLogger(EmployeeExitFormalityAnswersController.class);
	
	
	@PostMapping("/employeeExitFormalityAnswers")
	public ResponseEntity<Object> saveFeedbackQuestions(@RequestBody @Valid EmployeeExitFormalityAnswersDto eefa,@RequestHeader("Authorization") String jwtToken)
			throws  Exception,JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				eefa.setCreated_by(jwtDetails.getUserId());
				eefa.setCreated_username(jwtDetails.getUserName());
				List<EmployeeExitFormalityAnswers> list_employeeExitFormalityAnswers = eefa_service.getFormalityAnswers(eefa);
				ResponseEntity<Object> list_employeeExitFormalityAnswers_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, list_employeeExitFormalityAnswers);
				return list_employeeExitFormalityAnswers_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/employeeExitFormalityAnswers")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<EmployeeExitFormalityAnswers> list_employeeExitFormalityAnswers = eefa_service.listAll();
				ResponseEntity<Object> list_employeeExitFormalityAnswers_response1= ResponseHandler.generateResponse(true, HttpStatus.OK, list_employeeExitFormalityAnswers);
				return list_employeeExitFormalityAnswers_response1;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	@GetMapping("/fetchemployeeExitFormalityAnswersDetail")
	public ResponseEntity<Object> listAll1(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
						Pageable pageable = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted, keyword");
						ResponseEntity<Object> employeeExitFormalityAnswers_details = eefa_service.listAll1(pageable, keyword);//,column,value);
						return employeeExitFormalityAnswers_details;
				}else {
						Pageable pageable1 = PageRequest.of(page, page_size,sorted);
						System.out.println("page, page_size, sorted");
						ResponseEntity<Object> employeeExitFormalityAnswers_details1 = eefa_service.listAll2(pageable1);
						return employeeExitFormalityAnswers_details1;
			}
			
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}
	
	
	
	@GetMapping("/employeeExitFormalityAnswers/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
					try {

						EmployeeExitFormalityAnswers product = eefa_service.get(id);
							ResponseEntity<Object> list_employeeExitFormalityAnswers= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
							return list_employeeExitFormalityAnswers;

					} catch (NoSuchElementException e) {
							ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
							return response1;
					}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@PutMapping("/employeeExitFormalityAnswers/{id}")
	public ResponseEntity<Object> update(@RequestBody EmployeeExitFormalityAnswers eefas, @PathVariable Integer id,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {

				try {
					EmployeeExitFormalityAnswers existProduct = eefa_service.get(id);
						JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
						eefas.setModified_by(jwtDetails.getUserId());
						eefas.setModified_username(jwtDetails.getUserName());
						eefa_service.save_answers(eefas);
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

	
	@DeleteMapping("/employeeExitFormalityAnswers/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return eefa_service.delete(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@DeleteMapping("/activateEmployeeExitFormalityAnswers/{id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				return eefa_service.delete1(id);
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	@GetMapping("/employeeExitFormalityAnswersAllData/{emp_id}")
	public ResponseEntity<Object> listAllAnswers(@PathVariable Integer emp_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<HashMap<String, Object>> eefa1  = eefa_service.listAllAnswers(emp_id);
				ResponseEntity<Object> eefa1_response1= ResponseHandler.generateResponse(true, HttpStatus.OK, eefa1);
				return eefa1_response1;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
	
	
}
