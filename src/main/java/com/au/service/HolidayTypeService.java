package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.HolidayType;
import com.au.repository.HolidayTypeRepository;
import com.au.response.ResponseHandler;

@Service
public class HolidayTypeService {

	@Autowired
	private HolidayTypeRepository s_repo;

	public List<HolidayType> listAll() {
		return s_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> htype_filtered_response = s_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, htype_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> htype_sorted_response = s_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, htype_sorted_response);
	}

	public HolidayType saveHolidayType(HolidayType h) {
		if (s_repo.existsByholidayType(h.getHolidayType())) {
			throw new RuntimeException("Holiday Type  already exist");
		}
		if (s_repo.existsByholidayTypeShort(h.getHolidayTypeShort())) {
			throw new RuntimeException("Short Name  already exist");
		}
		return s_repo.save(h);
	}

	public HolidayType get(Integer id) {
		return s_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("HolidayType Not Found:" + id));
	}

	public void delete(Integer id) {
		HolidayType cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HolidayType Not Found:" + id));
		s_repo.updateHolidayType(id);
	}

	public void delete1(Integer id) {
		HolidayType cc = s_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("HolidayType Not Found:" + id));
		s_repo.updateHolidayType1(id);

	}

}
