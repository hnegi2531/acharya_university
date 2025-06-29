package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.JwtDetails;
import com.au.dto.MenuDTO;
import com.au.model.Menu;
import com.au.repository.MenuRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class MenuService {
	private Logger logger = LoggerFactory.getLogger(MenuImageService.class);

	@Autowired
	private MenuRepository menu_repo;
	
	@Autowired
	private JwtTokenService jwt_service;

	
	public List<Menu> saveMenu(@Valid MenuDTO me,@RequestHeader("Authorization") String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		List<Menu> lda = new ArrayList<>();
		me.getModule_id().stream().forEach(r -> {
			if(menu_repo.countOfMenuName(r,me.getMenu_name())>=1) {
				throw new RuntimeException("Menu Name Combination With Module Already Exist");
			}else if(menu_repo.countOfMenuShortName(r,me.getMenu_short_name())>=1) {
				throw new RuntimeException("Short Name Combination With Module Already Exist");
			}else {
				Menu menu = new Menu();
				menu.setMenu_desc(me.getMenu_desc());
				menu.setModule_id(r);
				menu.setActive(me.getActive());
				menu.setCreated_by(jwtDetails.getUserId());
				menu.setCreated_username(jwtDetails.getUserName());
				menu.setMenu_name(me.getMenu_name());
				menu.setMenu_short_name(me.getMenu_short_name());
				menu.setMenu_icon_name(me.getMenu_icon_name());
				saveMenus(menu);
				lda.add(menu);
			}	
				
		});
		return lda;
	}

	public Menu saveMenus(@Valid Menu menu) {
		if(menu_repo.countOfUpdatMenuName(menu.getMenu_id(),menu.getMenu_name())>=1) {
			throw new RuntimeException("Menu Name Already Exist");
		}else if(menu_repo.countOfUpdateMenuShortName(menu.getMenu_id(),menu.getMenu_short_name())>=1) {
			throw new RuntimeException("Short Name Already Exist");
		}else {
			return menu_repo.save(menu);
		}	
	}

	public Menu get(Integer id) {
		return menu_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu Id Not Found:" + id));
	}

	public ResponseEntity<Object> delete1(Integer id) {
		Menu ms = menu_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu Id Not Found:" + id));
		menu_repo.update(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		
	}

	public ResponseEntity<Object> delete2(Integer id) {
		Menu ms = menu_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu Id Not Found:" + id));
		menu_repo.update1(id);
		ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
		return response;
		
	}

	public List<Menu> listAll() {
		return menu_repo.findAll1();
	}
	
	public List<Map<String,Object>> menuNameConcatWithModule() {
		return menu_repo.menuNameConcatWithModule();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> filtered_response = menu_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, filtered_response);
		}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
	
		Page<Object> response = menu_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<HashMap<String, Object>> fetchMenuDetails(Integer module_id) {
		return menu_repo.fetchMenuDetail(module_id);
	}
	
	public void checkMenuNameAndShortName(String menu_name, String menu_short_name){
		if (menu_repo.countOfMenuName(menu_name) >= 1)
			throw new RuntimeException("Menu Name Already Exist");
		else if(menu_repo.countOfMenuShortName(menu_short_name) >= 1)
			throw new RuntimeException("Short Name Already Exist");
		}
	
}
