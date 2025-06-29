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

import com.au.dto.JwtDetails;
import com.au.model.EnvItemsInStores;
import com.au.response.ResponseHandler;
import com.au.service.EnvItemsInStoresService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/${secretkey5}")
@CrossOrigin
public class EnvItemsInStoresController {
	
		Logger log= LoggerFactory.getLogger(EnvItemsInStoresController.class);
		
		@Autowired
		private EnvItemsInStoresService eiis_ser;
		
		@Autowired
		private JwtTokenService jwt_service;
		
		@PostMapping("/envItemsStores")
		public ResponseEntity<Object> create(@RequestBody @Valid EnvItemsInStores eis , @RequestHeader("Authorization") String jwtToken)  {
			if(RateLimitController.bucket.tryConsume(1)) {
				try {
					JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
					eis.setCreated_by(jwtDetails.getUserId());
					eis.setCreated_username(jwtDetails.getUserName());
					EnvItemsInStores env = eiis_ser.createEnv(eis);
					return ResponseHandler.generateResponse(true, HttpStatus.CREATED, env);
				}catch (JsonParseException | JsonMappingException  e) {
					return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
				}catch( IOException  e) {
					return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
				}catch(Exception  e) {
					return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
				}
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		}
		
		@GetMapping("/activeEnvItemsStores")
		public ResponseEntity<Object> activeList(){
			if(RateLimitController.bucket.tryConsume(1)) {
				List<EnvItemsInStores> env = eiis_ser.getActiveList();
				return ResponseHandler.generateResponse(true, HttpStatus.OK, env);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		}
		
		@GetMapping("/fetchAllEnvItemsStores")
		public ResponseEntity<Object> getAllRolesDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
				@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
			
			if(RateLimitController.bucket.tryConsume(1)) {
			Sort sorted = Sort.by(Direction.DESC, sort );
			if(keyword != null) {	
				Pageable pageable = PageRequest.of(page, page_size,sorted);
				return eiis_ser.getAllDataFilteredByKeyword(pageable, keyword);
			} else {
				Pageable pageable1 = PageRequest.of(page, page_size,sorted);
				return eiis_ser.getAllSortedData(pageable1);
			}
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		}
		
		@GetMapping("/envItemsStores/{env_item_id}")
		public  ResponseEntity<Object> get(@PathVariable Integer env_item_id){
			if(RateLimitController.bucket.tryConsume(1)) {
			try {
				EnvItemsInStores ei = eiis_ser.get(env_item_id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, ei);

			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		}
		
		@PutMapping("/envItemsStores/{env_item_id}")
		public  ResponseEntity<Object> update(@RequestBody EnvItemsInStores ei , @PathVariable Integer env_item_id , 
				@RequestHeader("Authorization") String jwtToken)
						throws Exception, JsonParseException, JsonMappingException, IOException {
			if(RateLimitController.bucket.tryConsume(1)) {
			try {
				JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
				ei.setModified_by(jwtDetails.getUserId());
				ei.setModified_username(jwtDetails.getUserName());
				eiis_ser.update(ei);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} catch (NoSuchElementException e) {
				return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			}
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
		}
		
		@DeleteMapping("/envItemsStores/{env_item_id}")
		public ResponseEntity<Object> delete(@PathVariable Integer env_item_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
			eiis_ser.delete(env_item_id);
				return  ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}
			
		}
		
		@DeleteMapping("/activateEnvItemsStores/{env_item_id}")
		public ResponseEntity<Object> delete1(@PathVariable Integer env_item_id) {
			if(RateLimitController.bucket.tryConsume(1)) {
				eiis_ser.delete1(env_item_id);
				return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			}

		}
		
		@GetMapping("/getItemNameConcatWithdescriptionAndMake")
		public ResponseEntity<Object> getItemNameConcatWithdescriptionAndMake() {
			if(RateLimitController.bucket.tryConsume(1)) {
				List<Map<String,Object>> cos = eiis_ser.getItemNameConcatWithdescriptionAndMake();
				return ResponseHandler.generateResponse(true, HttpStatus.OK, cos);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
		}
		
}
