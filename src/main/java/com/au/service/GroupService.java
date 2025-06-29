package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Group;
import com.au.repository.GroupRepository;
import com.au.response.ResponseHandler;

@Service
public class GroupService {

	@Autowired
	private GroupRepository gp;

	public List<Group> listAll() {
		return gp.findAll11();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		//return gp.findAll12();
		Page<Object> response1 = gp.findAll12(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		//return gp.findAll13();
		Page<Object> response = gp.findAll13(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}
	
	public Group save_Group(Group group) throws Exception {
		if(gp.getCountGroupName(group.getGroup_name()) >= 1) {
			throw new Exception("Group Name Already Exist");
		} else if(gp.getCountGroupShortName(group.getGroup_short_name()) >= 1) {
			throw new Exception("Short Name Already Exist");
		} else {
			return gp.save(group);
		}
	}

//	public Group save_Group(Group group) throws Exception {
//		try {
//			gp.save(group);
//		} catch (Exception e) {
//			if (e.getMessage().contains("group_name_UNIQUE")) {
//				throw new Exception("Group Name already present");
//			} else if (e.getMessage().contains("group_short_name_UNIQUE")) {
//				throw new Exception("Group short name already present");
//			}
//
//			else {
//				e.printStackTrace();
//			}
//			// catch (Exception e) {
//			// e.printStackTrace();
//
//		}
//		return group;
//	}

	public Group get(Integer id) {
		return gp.findById(id).orElseThrow(() -> new ResourceNotFoundException("Group Not Found:" + id));
	}

	public void delete(Integer id) {
		Group ay = gp.findById(id).orElseThrow(() -> new ResourceNotFoundException("Group Not Found:" + id));
		gp.update(id);
	}

	public void delete1(Integer id) {
		Group ay = gp.findById(id).orElseThrow(() -> new ResourceNotFoundException("Group Not Found:" + id));
		gp.update1(id);
	}

	public Group save_Group1(Group g) {
		return gp.save(g);

	}
}
