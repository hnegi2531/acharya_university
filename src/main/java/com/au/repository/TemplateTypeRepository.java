package com.au.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.au.model.TemplateType;

@Transactional
@Repository
public interface TemplateTypeRepository extends JpaRepository<TemplateType, Integer> {

	@Query(value = "select ty from TemplateType ty where ty.active=true")
	public List<TemplateType> findAll1();

}
