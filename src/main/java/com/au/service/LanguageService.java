package com.au.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.CourseObjective;
import com.au.model.Language;
import com.au.repository.LanguageRepository;



@Service
public class LanguageService {
	
	
	@Autowired
	private LanguageRepository lang_repo;
	
	public List<Language> saveLanguage(@Valid List<Language> la) throws Exception {
		return	lang_repo.saveAll(la);
	}
	
	
	public List<Language> listAll() {
		return lang_repo.listAll();
	}
		
	
	public Language get(Integer id) {
		return lang_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Language Not Found:" + id));
	}	
	
	public Language updateLanguage(Language lang) {
		return lang_repo.save(lang);
	}


	public List<Language> getLanguageBasedOnEmpId(Integer emp_id) {
		return lang_repo.getLanguageBasedOnEmpId(emp_id);
	}


	public List<Language> updateLanguage(@Valid List<Language> lang) {
		return lang_repo.saveAll(lang);
	}	

}
