package com.au.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.au.dto.SectionDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Section;
import com.au.repository.SectionRepository;
import com.au.response.ResponseHandler;

@Service
public class SectionService {

	@Autowired
	private SectionRepository sectionRepository;

	public List<Section> listAll() {
		return sectionRepository.findAll1();
	}

	public List<Map<String, Object>> fetchAllSectionDetails() {
		return sectionRepository.fetchAllSectionDetails();
	}

	public List<Section> saveSection(SectionDto s) {
		List<Section> list = new ArrayList<>();
		s.getSchool_id().stream().forEach(sid -> {
			if(sectionRepository.countSectionName(s.getSectionName(),sid)>=1) {
				throw new RuntimeException("Section Already Exist in School");
			}else {
				Section s1 = new Section();
				s1.setSection_name(s.getSectionName());
				s1.setCreated_by(s.getCreatedBy());
				s1.setCreated_date(s.getCreatedDate());
				s1.setCreated_username(s.getCreatedUsername());
				s1.setActive(s.getActive());
				s1.setSchool_id(sid);
				s1.setVolume(s.getVolume());
				s1.setRemarks(s.getRemarks());
				list.add(s1);
				sectionRepository.save(s1);
			}
		});

		return list;
	}
	
	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		Page<Object> section_filtered_response = sectionRepository.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, section_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
		Page<Object> section_sorted_response = sectionRepository.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, section_sorted_response);
	}

	public Section get(Integer id) {
		return sectionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Section Not Found:" + id));
	}

	public void delete(Integer id) {
		Section cc = sectionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Section Not Found:" + id));
		sectionRepository.updateSection(id);
	}

	public void delete1(Integer id) {
		Section cc = sectionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Section Not Found:" + id));
		sectionRepository.updateSection1(id);
	}

	public void saveSection1(@Valid Section s) {
		if(sectionRepository.countOfSectionNameOnSchoolId(s.getSection_id(),s.getSection_name(),s.getSchool_id())>=1) {
			throw new RuntimeException("Section Already Exist in School");
		}else {
			sectionRepository.save(s);
		}	
	}

	public List<Section> fetchSectionBySchool(Integer school_id){
		return sectionRepository.fetchSectionBySchool(school_id);
	}
	
	 public void checkSectionNameOnSchoolId(String section_name, Integer school_id){
			
		 if(sectionRepository.countSectionName(section_name,school_id) >= 1) {
			 throw new RuntimeException("Section Already Exist in School");
		}
		
	 }
}
