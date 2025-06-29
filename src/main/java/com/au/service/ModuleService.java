package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.Module;
import com.au.repository.ModuleRepository;
import com.au.response.ResponseHandler;

@Service
public class ModuleService {

	@Autowired
	private ModuleRepository module_repo;

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> response1 = module_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
		}
	
	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
	
		Page<Object> response = module_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<Module> listAll1() {
		return module_repo.findAll1();
	}

	public Module saveModule(Module module) throws Exception {
		if(module_repo.getCountForMaximumModule() >= 6) {
			throw new Exception("Module Limit Exceeded");
		} else {
			if (module_repo.countModuleName(module.getModule_name()) >= 1) {
				throw new Exception("Module Name Already Exist");
			} else if (module_repo.countgetModuleShortName(module.getModule_short_name()) >= 1) {
				throw new Exception("Short Name Already Exist");
			} else {
				return module_repo.save(module);
			}
		}
	}
	
	public Module saveModules(@Valid Module module) {
		return module_repo.save(module);		
	}


	public Module get(Integer id) {
		return module_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Module Id Not Found:" + id));
	}

	public void delete(Integer id) {
		Module ms = module_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Module Id Not Found:" + id));
		module_repo.update(id);
	}

	public void delete1(Integer id) {
		Module ms = module_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Module Id Not Found:" + id));
		module_repo.update1(id);
		
	}	
}
