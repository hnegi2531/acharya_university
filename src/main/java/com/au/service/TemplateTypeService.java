package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.TemplateType;
import com.au.repository.TemplateTypeRepository;

@Service
public class TemplateTypeService {

	@Autowired
	private TemplateTypeRepository ttr_repo;

	public List<TemplateType> listAll() {
		return ttr_repo.findAll1();
	}

	public TemplateType saveTemplateType(TemplateType templateType) {
		return ttr_repo.save(templateType);
	}

	public TemplateType get(Integer id) {
		return ttr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Template Type Id Not Found:" + id));
	}

	public void delete(Integer id) {
		TemplateType s = ttr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Template Type Id Not Found:" + id));
		ttr_repo.delete(s);
	}

}
