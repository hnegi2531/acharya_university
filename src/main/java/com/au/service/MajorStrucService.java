package com.au.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.exception.ResourceNotFoundException;
import com.au.model.MajorStruc;
import com.au.repository.MajorStrucRepository;
import com.au.response.ResponseHandler;

@Service
public class MajorStrucService {

	@Autowired
	private MajorStrucRepository m_repo;

	public List<MajorStruc> listAll() {
		return m_repo.findAll1();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> major_filtered_response = m_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, major_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> major_sorted_response = m_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, major_sorted_response);
	}

	public MajorStruc saveMajorStruc(MajorStruc s) {

		return m_repo.save(s);

	}

	public MajorStruc get(Integer id) {
		return m_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("MajorStruc Not Found:" + id));
	}

	public void delete(Integer id) {
		MajorStruc cc = m_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MajorStruc Not Found:" + id));
		m_repo.updateMajor_struc(id);
	}

	public void delete1(Integer id) {
		MajorStruc cc = m_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("MajorStruc Not Found:" + id));
		m_repo.updateMajor_struc1(id);

	}

	public List<MajorStruc> fetchMajorByDept(Integer dept_id) {
		return m_repo.fetchMajorByDept(dept_id);
	}
}
