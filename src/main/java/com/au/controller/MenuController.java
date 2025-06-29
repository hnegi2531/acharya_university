package com.au.controller;


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
import com.au.dto.MenuDTO;
import com.au.model.Menu;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.MenuService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MenuController {

	Logger log = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private MenuService m_service;

	@Autowired
	private JwtTokenService jwt_service;

	@PostMapping("/Menu")
	public ResponseEntity<Object> saveMenu(@Valid @RequestBody MenuDTO menu, @RequestHeader("Authorization") String jwtToken) throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Menu> created_menu= m_service.saveMenu(menu,jwtToken);
		ResponseEntity<Object> menu_response= ResponseHandler.generateResponse(true, HttpStatus.CREATED, created_menu);
		return menu_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@GetMapping("/Menu")
	public ResponseEntity<Object> listAll() {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Menu> menu_list= m_service.listAll();
		ResponseEntity<Object> menu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, menu_list);
		return menu_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}
	
	@GetMapping("/MenuForSubmenu")
	public ResponseEntity<Object> menuNameConcatWithModule() {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<Map<String,Object>> menu_list= m_service.menuNameConcatWithModule();
		ResponseEntity<Object> menu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, menu_list);
		return menu_response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}

	@GetMapping("/fetchAllMenuDetails")
	public ResponseEntity<Object> getAllMenuData(@RequestParam(value="page") Integer page,@RequestParam(value="page_size") Integer page_size,
			@RequestParam(value="sort") String sort, @RequestParam(value="keyword",required = false) Object keyword) {
		if(RateLimitController.bucket.tryConsume(1)) {
		Sort sorted = Sort.by(Direction.DESC, sort );
		if(keyword != null) {	
			Pageable pageable = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted, keyword");
			ResponseEntity<Object> menu_filtered_response =  m_service.getAllDataFilteredByKeyword(pageable, keyword);//,column,value);
			return menu_filtered_response;
		} else {
			Pageable pageable1 = PageRequest.of(page, page_size,sorted);
			System.out.println("page, page_size, sorted");
			ResponseEntity<Object> menu_sorted_response = m_service.getAllSortedData(pageable1);
			return menu_sorted_response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
		
	}

	@GetMapping("/Menu/{id}")
	public ResponseEntity<Object> get(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			Menu menu = m_service.get(id);
			ResponseEntity<Object> roles_response= ResponseHandler.generateResponse(true, HttpStatus.OK, menu);
			return roles_response;

		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		} 
		}else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}
	@PutMapping("/Menu/{id}")
	public ResponseEntity<Object> update(@RequestBody @Valid Menu menu, @PathVariable Integer id,
			@RequestHeader("Authorization") String jwtToken)throws Exception {
		if(RateLimitController.bucket.tryConsume(1)) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			menu.setModified_by(jwtDetails.getUserId());
			menu.setModified_username(jwtDetails.getUserName());
			m_service.saveMenus(menu);
			ResponseEntity<Object> menu_response=ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return menu_response;
		} catch (NoSuchElementException e) {
			ResponseEntity<Object> response=ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_FOUND);
			return response;
		}
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/Menu/{id}")
	public ResponseEntity<Object> deactivateMenu(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
			m_service.delete1(id);
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
			return rs;
		}
	}

	@DeleteMapping("/activteMenu/{id}")
	public ResponseEntity<Object> activateMenu(@PathVariable Integer id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		 m_service.delete2(id);
		 ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/menuDetails/{module_id}")
	public ResponseEntity<Object> fetchdetails2(@RequestBody @PathVariable Integer module_id) {
		if(RateLimitController.bucket.tryConsume(1)) {
		List<HashMap<String, Object>> menu_list = m_service.fetchMenuDetails(module_id);
		ResponseEntity<Object> menu_response= ResponseHandler.generateResponse(true, HttpStatus.OK, menu_list);
		return menu_response;
			} else {
				ResponseEntity<Object> rs=ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
				return rs;
			}
	}
	
	@GetMapping("/checkMenuNameAndShortName")
	public ResponseEntity<Object> checkMenuNameAndShortName(@RequestParam(value = "menu_name",required = false) String menu_name,
			@RequestParam(value = "menu_short_name",required = false) String menu_short_name){
		if (RateLimitController.bucket.tryConsume(1)) {
				m_service.checkMenuNameAndShortName(menu_name,menu_short_name);
				ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
				return response;
		} else {
			ResponseEntity<Object> rs = ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS,
					ResponseHandler.message1);
			return rs;
		}		
	}
	
	
	
}
