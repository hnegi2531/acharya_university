package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.model.Conferences;
import com.au.model.Nationality;
import com.au.repository.NationalityRepository;

@Service
public class NationalityService {
	
	@Autowired
	private NationalityRepository nation_repo;
	
	public Nationality saveNationality(Nationality nation) {
		return nation_repo.save(nation);
	}
	
	public List<Nationality> allActiveDetailsList(){
		List<Nationality> nation_list = nation_repo.allActiveDetailsList();
		return nation_list;
	}

	public List<Map<String, Object>> getAllActiveNationality() {
		return nation_repo.getAllActiveNationality();
		}
	

}
