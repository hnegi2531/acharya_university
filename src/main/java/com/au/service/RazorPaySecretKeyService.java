package com.au.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.model.RazorPaySecretKeys;
import com.au.repository.RazorPaySecretKeyRepository;

@Service
public class RazorPaySecretKeyService {
	
	@Autowired
	private RazorPaySecretKeyRepository razorPaySecretKeyRepository;
	
	 public RazorPaySecretKeys getSecretKeysBySchoolId(Integer schoolId) {
	        return razorPaySecretKeyRepository.getRazorPaySecretKeysBySchoolId(schoolId);
	    }

}
