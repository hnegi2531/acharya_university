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
import com.au.model.ItemsCreation;
import com.au.response.ResponseHandler;
import com.au.service.ItemsCreationService;
import com.au.service.JwtTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.au.dto.ActiveitemsDetailsResponseDto;

@RestController
//@RequestMapping("/api")
@RequestMapping("/api/${secretkey5}")
@CrossOrigin
public class ItemsCreationController {
	
	Logger log = LoggerFactory.getLogger(ItemsCreationController.class);
	
	@Autowired
	private ItemsCreationService itemsCreationService;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@PostMapping("/itemsCreation")
	public ResponseEntity<Object> saveItemsCreation(@RequestBody @Valid ItemsCreation item, @RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException, Exception{
		if(RateLimitController.bucket.tryConsume(1)) {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			item.setCreated_by(jwtDetails.getUserId());
			item.setCreated_username(jwtDetails.getUserName());
			ItemsCreation itemsCreation = itemsCreationService.saveItemsCreation(item);
			return ResponseHandler.generateResponse(true, HttpStatus.CREATED, itemsCreation);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				
			}
	}
	
	@GetMapping("/itemsCreation")
	public ResponseEntity<Object> listAll1() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ItemsCreation> itemsCreationList = itemsCreationService.listAll1();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, itemsCreationList);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}	
	
	@GetMapping("/fetchAllItemsCreationDetails")
	public ResponseEntity<Object> getAllItemCreationDetails(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			return  itemsCreationService.getAllDataFilteredByKeyword(pageable, keyword);
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			return itemsCreationService.getAllSortedData(pageable1);
		}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/itemsCreation/{item_id}")
	public ResponseEntity<Object> get(@PathVariable Integer item_id ) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			ItemsCreation itemsCreation = itemsCreationService.get(item_id);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, itemsCreation);

		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
		}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@PutMapping("/itemsCreation/{item_id}")
	public ResponseEntity<Object> updateItemCreation(@RequestBody ItemsCreation item , 
			@PathVariable Integer item_id , @RequestHeader("Authorization") String jwtToken )
					throws JsonParseException, JsonMappingException, IOException, Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			item.setModified_by(jwtDetails.getUserId());
			item.setModified_username(jwtDetails.getUserName());
			itemsCreationService.saveUpdateItemCreation(item);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
		}
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@DeleteMapping("/itemsCreation/{item_id}")
	public ResponseEntity<Object> delete(@PathVariable Integer item_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			itemsCreationService.delete(item_id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

		
	@DeleteMapping("/activateItemsCreation/{item_id}")
	public ResponseEntity<Object> delete1(@PathVariable Integer item_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			itemsCreationService.delete1(item_id);
			return ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}
	
	@GetMapping("/allActiveitemsDetails")
	public ResponseEntity<Object> allActiveitemsDetails() {
		if(RateLimitController.bucket.tryConsume(1)) {
			List<ActiveitemsDetailsResponseDto> itemsCreationList = itemsCreationService.allActiveitemsDetails();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, itemsCreationList);
		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
		}
	}

}
