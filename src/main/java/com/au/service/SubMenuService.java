package com.au.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.SubMenu;
import com.au.repository.SubMenuRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class SubMenuService {

	@Autowired
	private SubMenuRepository subMenu_repo;
	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;

	public List<SubMenu> listAll() {
		return subMenu_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> response1 = subMenu_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
	
		Page<Object> response = subMenu_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public HashMap<String, Object> listAll10() {
		HashMap<String, Object> response = new HashMap<>();
		response.put("doc", subMenu_repo.findAll2());
		return response;
	}

	public HashMap<String, Object> listAll11(Integer submenu_ids) {
		HashMap<String, Object> response1 = new HashMap<>();
		String submenu_ids1 = submenu_ids + ",%";
		String submenu_ids2 = "%," + submenu_ids + ",%";
		String submenu_ids3 = "%," + submenu_ids;
		String submenu_ids8 = submenu_ids.toString();
		response1.put("RoleName", subMenu_repo.findAll9(submenu_ids1, submenu_ids2, submenu_ids3, submenu_ids8));
		String submenu_ids4 = submenu_ids + ",%";
		String submenu_ids5 = "%," + submenu_ids + ",%";
		String submenu_ids6 = "%," + submenu_ids;
		String submenu_ids7 =submenu_ids.toString();
		response1.put("count", subMenu_repo.findAll8(submenu_ids4, submenu_ids5, submenu_ids6, submenu_ids7));
		return response1;
	}
	
	public SubMenu saveSubMenu(SubMenu submenu) throws Exception {

		if(subMenu_repo.getSubMenuName(submenu.getSubmenu_name()) >= 1) {
			throw new Exception("SubMenu Name Already Exist");
		} else if (subMenu_repo.getSubMenuUrl(submenu.getSubmenu_url()) >= 1) {
				throw new Exception("Url Already Exist");
		}
		else {
			return subMenu_repo.save(submenu);
		}
	}


	
	public SubMenu saveSubMenus(SubMenu sm) {
		return subMenu_repo.save(sm);	
	}
	
	public SubMenu get(Integer id) {
		return subMenu_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SubMenu Id Not Found:" + id));
	}

	public void delete(Integer id) {
		SubMenu ys = subMenu_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubMenu Id Not Found:" + id));
		subMenu_repo.update(id);
	}

	public void delete1(Integer id) {
		SubMenu ys = subMenu_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SubMenu Id Not Found:" + id));
		subMenu_repo.update1(id);
	}

	public List<HashMap<String, Object>> fetchdetails(List<Integer> submenu_id) {
		return subMenu_repo.findAllDetails(submenu_id);
	}

	public List<HashMap<String, Object>> fetchUncheckedSubMenudetails(List<Integer> submenu_id) {
		return subMenu_repo.fetchUncheckedSubMenudetails(submenu_id);
	}

	public List<Integer> fetchSubMenu(Integer menu_id) {
		List<Integer> subMenuResponse = new ArrayList<Integer>();
		subMenuResponse.addAll(subMenu_repo.fetchSubMenuDetails(menu_id));
		System.out.println(subMenuResponse);
		return subMenuResponse;
	}

	public String fetchSubMenu1(Integer role_id) {
		String subMenuResponse1 = subMenu_repo.findSubMenuDetails(role_id);
		System.out.println(subMenuResponse1);
		return subMenuResponse1;
	}

	public HashMap<String, Object> getCommons(Integer menu_id, Integer role_id) {

		List<Integer> list1 = fetchSubMenu(menu_id);
		String str = fetchSubMenu1(role_id);
		if (str == null) {
			return null;
		} else {
			Pattern pattern = Pattern.compile(",");
			List<Integer> list = pattern.splitAsStream(str).map(Integer::valueOf).collect(Collectors.toList());
			list1.retainAll(list);
		}
		System.out.println("Final Data" + list1);
		HashMap<String, Object> response1 = new HashMap<>();
		response1.put("SubMenuName", subMenu_repo.finalSubMenuDetails(list1));
		System.out.println(response1);
		return response1;
	}

	public HashMap<String, Object> getSubMenuRelatedUsers(Integer submenu_id) {
		HashMap<String, Object> response = new HashMap<>();
		String st = subMenu_repo.getAssignedUser(submenu_id);
		if (st == null || st.trim().isEmpty()) {
			List<Integer> check = new ArrayList<>();
			check.add(0);
			List<String> check1 = Arrays.asList(st);
			response.put("AssignedUser", check1);
			response.put("UnassignedUser", subMenu_repo.getUnassignedUser(check));		
		} else {
			List<String> myList = new ArrayList<String>(Arrays.asList(st.split(",")));
			Pattern pattern = Pattern.compile(",");
			List<Integer> list = pattern.splitAsStream(st).map(Integer::valueOf).collect(Collectors.toList());
			response.put("AssignedUser", myList);
			response.put("UnassignedUser", subMenu_repo.getUnassignedUser(list));
		}
		return response;
	}

	public Object getUserDetails(String user_ids, Integer submenu_id) {
		return subMenu_repo.saveDetails(user_ids, submenu_id);
	}

	public HashMap<String, Object> getAllAssignedUserBySubmenuId(Integer submenu_id) {
		HashMap<String, Object> response = new HashMap<>();
		String st = subMenu_repo.getAssignedUser(submenu_id);
		if (st == null) {
			response.put("AssignedUser", new HashMap<>());	
		} else {
			List<String> myList = new ArrayList<String>(Arrays.asList(st.split(",")));
			Pattern pattern = Pattern.compile(",");
			List<Integer> list = pattern.splitAsStream(st).map(Integer::valueOf).collect(Collectors.toList());
			response.put("AssignedUser",userAuthenticationRepository.getAllSubmenuAssignedUser(list));
		}
		return response;
	}

	public HashMap<String, Object> getAssignedSubMenuDetailsByUserId(Integer id) {
		HashMap<String, Object> response = new HashMap<>();
		String userIdCase1 = "%," + id + ",%";
		String userIdCase2 = id + ",%";
		String userIdCase3 = "%," + id;
		String userIdCase4 = id.toString();
		response.put("assignedSubMenuList", subMenu_repo.getAssignedSubMenuDetailsByUserId(userIdCase1, userIdCase2, userIdCase3, userIdCase4));
		return response;
	}

}
