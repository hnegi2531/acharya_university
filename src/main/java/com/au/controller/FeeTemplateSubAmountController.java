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
import com.au.dto.FeeTemplateAmount;
import com.au.dto.JwtDetails;
import com.au.model.FeeTemplateSubAmount;
import com.au.response.ResponseHandler;
import com.au.service.FeeTemplateSubAmountService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
//@RequestMapping("/api")
public class FeeTemplateSubAmountController {

	Logger log = LoggerFactory.getLogger(FeeTemplateSubAmountController.class);

	@Autowired
	private FeeTemplateSubAmountService ftsa_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/FeeTemplateSubAmount1")
	public ResponseEntity<Object> saveFeeTemplateSubAmount(
			@RequestBody @Valid FeeTemplateAmount feetemplatesubamount, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				feetemplatesubamount.getFtsa().stream().forEach(model -> {
					model.setCreated_by(jwtDetails.getUserId());
					model.setCreated_username(jwtDetails.getUserName());
				});

				List<FeeTemplateSubAmount> list_ftsa = ftsa_service.saveFeeTemplateTotalAmount(feetemplatesubamount, jwtToken);
				ResponseEntity<Object> fee_template_sub_amount_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, list_ftsa);
				return fee_template_sub_amount_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PostMapping("/FeeTemplateSubAmount2")
	public ResponseEntity<Object> saveFeeTemplateSubAmount2(
			@RequestBody @Valid List<FeeTemplateSubAmount> feetemplatesubamount, @RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				log.debug("Request {}", feetemplatesubamount);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				feetemplatesubamount.stream().forEach(ftsa_list -> {
					ftsa_list.setCreated_by(jwtDetails.getUserId());
					ftsa_list.setCreated_username(jwtDetails.getUserName());
				});
				List<FeeTemplateSubAmount> list_ftsa = ftsa_service.saveFeeTemplateTotalAmount2(feetemplatesubamount);
				ResponseEntity<Object> fee_template_sub_amount_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, list_ftsa);
				return fee_template_sub_amount_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@PostMapping("/FeeTemplateSubAmount")
	public ResponseEntity<Object> saveFeeTemplateSubAmount(
			@RequestBody @Valid FeeTemplateSubAmount feetemplatesubamount , @RequestHeader("Authorization") String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				log.debug("Request {}", feetemplatesubamount);
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				feetemplatesubamount.setCreated_by(jwtDetails.getUserId());
				feetemplatesubamount.setCreated_username(jwtDetails.getUserName());
				FeeTemplateSubAmount ftsa = ftsa_service.saveFeeTemplateSubAmount1(feetemplatesubamount);
				ResponseEntity<Object> fee_template_sub_amount_response = ResponseHandler.generateResponse(true, HttpStatus.CREATED, ftsa);
				return fee_template_sub_amount_response;
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

//	@GetMapping("/FeeTemplateSubAmount")
//	public List<FeeTemplateSubAmount> listAll() {
//		return ftsa_service.listAll();
//	}
	
	@GetMapping("/FeeTemplateSubAmount")
	public ResponseEntity<Object> listAll(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
				Sort sorted = Sort.by(Direction.DESC, sort );
				if(keyword != null) {	
					Pageable pageable = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted, keyword");
					ResponseEntity<Object> ftsa_sorted = ftsa_service.listAll1(pageable, keyword);//,column,value);
					return ftsa_sorted ;
				}else {
					Pageable pageable1 = PageRequest.of(page, page_size,sorted);
					System.out.println("page, page_size, sorted");
					ResponseEntity<Object> ftsa_pageable = ftsa_service.listAll2(pageable1);
					return ftsa_pageable;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
		//return ftsa_service.listAll();
	}

	@GetMapping("/FeeTemplateSubAmount/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			try {

				FeeTemplateSubAmount product = ftsa_service.get(id);
				log.debug("Request {}", id);
				ResponseEntity<Object> ftsa_response_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, product);
				return ftsa_response_by_id;

			} catch (NoSuchElementException e) {

				ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
				return response1;
			}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}	
	}

	@PutMapping("/FeeTemplateSubAmount/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid FeeTemplateSubAmount feetemplatesubamount,
			@PathVariable Integer id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				try {
						// FeeTemplateSubAmount existProduct = ftsa_service.get(id);
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					feetemplatesubamount.setModified_by(jwtDetails.getUserId());
					feetemplatesubamount.setModified_username(jwtDetails.getUserName());
					FeeTemplateSubAmount ftsa_update = ftsa_service.saveFeeTemplateSubAmount1(feetemplatesubamount);
					ResponseEntity<Object> ftsa_response_update_by_id= ResponseHandler.generateResponse(true, HttpStatus.OK, ftsa_update);
					return ftsa_response_update_by_id;
				} catch (NoSuchElementException e) {
					ResponseEntity<Object> response1=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
					return response1;
				}
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@DeleteMapping("/FeeTemplateSubAmount/{id}")
	public ResponseEntity<Object> delete(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				ftsa_service.delete(id);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}
	}

	@GetMapping("/FetchFeeTemplateSubAmountDetail/{fee_template_id}") // (Behalf of fee_template_id)
	public ResponseEntity<Object> fetch1(@RequestBody @PathVariable Integer fee_template_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String, Object>> ftsa_by_ft_id = ftsa_service.findByFeeTemplate(fee_template_id);
				ResponseEntity<Object> ftsa_by_ft_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, ftsa_by_ft_id);
				return ftsa_by_ft_id_response;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}

	@GetMapping("/FeeTemplateSubAmountDetail/{fee_template_id}") // (Behalf of fee_template_id)
	public ResponseEntity<Object> fetch2(@RequestBody @PathVariable Integer fee_template_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
				List<FeeTemplateSubAmount> list_ftsa_by_ft_id = ftsa_service.findByFeeTemplate1(fee_template_id);
				ResponseEntity<Object> list_ftsa_by_ft_id_response = ResponseHandler.generateResponse(true, HttpStatus.OK, list_ftsa_by_ft_id);
				return list_ftsa_by_ft_id_response;
		}else {	
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
		}	
	}

	@PutMapping("/EditFeeTemplateSubAmount/{fee_template_id}")
	public ResponseEntity<Object> update1(@RequestBody @Valid FeeTemplateAmount feetemplatesubamount,
			@PathVariable Integer fee_template_id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					feetemplatesubamount.getFtsa().stream().forEach(model -> {
						model.setModified_by(jwtDetails.getUserId());
						model.setModified_username(jwtDetails.getUserName());
				});
				List<FeeTemplateSubAmount> list_ftsa = ftsa_service.saveFeeTemplateTotalAmount(feetemplatesubamount, jwtToken);
				ResponseEntity<Object> ftsa_updated_reponse_by_ft_id= ResponseHandler.generateResponse(true, HttpStatus.OK, list_ftsa);
				return ftsa_updated_reponse_by_ft_id;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		

	}

	@PutMapping("/historyFeeTemplateSubAmount/{fee_template_id}")
	public ResponseEntity<Object> updates(@RequestBody @Valid FeeTemplateAmount feetemplatesubamount,
			@PathVariable Integer fee_template_id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				feetemplatesubamount.getFtsa().stream().forEach(model -> {
					model.setModified_by(jwtDetails.getUserId());
					model.setModified_username(jwtDetails.getUserName());
				});
				feetemplatesubamount.getFtsah().stream().forEach(model -> {
					model.setCreated_by(jwtDetails.getUserId());
					model.setCreated_username(jwtDetails.getUserName());
				});
				List<FeeTemplateSubAmount> update_list_ftsa_by_ft_id = ftsa_service.saveFeeTemplateTotalAmounts(feetemplatesubamount, jwtToken);
				ResponseEntity<Object> update_list_ftsa_by_ft_id_reponse= ResponseHandler.generateResponse(true, HttpStatus.OK, update_list_ftsa_by_ft_id);
				return update_list_ftsa_by_ft_id_reponse;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		

	}

	@PutMapping("/approveFeeTemplateSubAmount/{fee_template_id}")
	public ResponseEntity<Object> update(@RequestBody @Valid FeeTemplateAmount feetemplateamount,
			@PathVariable Integer fee_template_id, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		if(RateLimitController.bucket.tryConsume(1)) {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				feetemplateamount.getFtsa().stream().forEach(model -> {
						model.setModified_by(jwtDetails.getUserId());
						model.setModified_username(jwtDetails.getUserName());
				});
				List<FeeTemplateSubAmount> ftsa_updated_list_by_ft_id = ftsa_service.approveFeeTemplateTotalAmount(feetemplateamount, jwtToken);
				ResponseEntity<Object> update_list_ftsa_by_ft_id_reponse= ResponseHandler.generateResponse(true, HttpStatus.OK, ftsa_updated_list_by_ft_id);
				return update_list_ftsa_by_ft_id_reponse;
		}else {	
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}		
	}
}
