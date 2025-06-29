package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.YearSem;
import com.au.repository.YearSemRepository;

@Service
public class YearSemService {

	@Autowired
	private YearSemRepository YearSem_repo;

	public List<YearSem> listAll() {
		return YearSem_repo.findAll1();
	}

	public List<YearSem> listAll1() {
		return YearSem_repo.findAll();
	}

	public YearSem saveYearSem(YearSem s) {
		return YearSem_repo.save(s);
	}

	public YearSem get(Integer id) {
		return YearSem_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("YearSem Not Found:" + id));
	}

	public void delete(Integer id) {
		YearSem cc = YearSem_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("YearSem Not Found:" + id));
		YearSem_repo.updateYearSem(id);
	}

	public void delete1(Integer id) {
		YearSem cc = YearSem_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("DoctorWarden Not Found:" + id));
		YearSem_repo.updateYearSem1(id);

	}

}
