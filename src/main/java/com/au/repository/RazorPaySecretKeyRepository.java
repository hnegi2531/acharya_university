package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.au.model.RazorPaySecretKeys;

@Repository
public interface RazorPaySecretKeyRepository  extends JpaRepository<RazorPaySecretKeys, Integer>{
	
	@Query(value=" select r from RazorPaySecretKeys r where r.schoolId=:schoolId and r.active = true ")
	RazorPaySecretKeys getRazorPaySecretKeysBySchoolId(Integer schoolId);

	
}
