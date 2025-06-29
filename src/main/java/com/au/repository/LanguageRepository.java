package com.au.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.au.model.Language;




@Transactional
@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer>{

	
	@Query(value = "SELECT lang from Language lang where lang.active=true")
	List<Language> listAll();

	
	@Query(value = "SELECT lang from Language lang where lang.emp_id=?1 And lang.active=true")
	List<Language> getLanguageBasedOnEmpId(Integer emp_id);

}