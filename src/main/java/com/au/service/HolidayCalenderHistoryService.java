package com.au.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.exception.ResourceNotFoundException;
import com.au.model.HolidayCalender;
import com.au.model.HolidayCalenderHistory;
import com.au.repository.HolidayCalenderHistoryRepository;
import com.au.response.ResponseHandler;

@Service
public class HolidayCalenderHistoryService {
	
	@Autowired
	private HolidayCalenderHistoryRepository hchr_repo;

	public HolidayCalenderHistory saveHolidayCalenderHistory(@Valid HolidayCalenderHistory h) throws Exception {
		HolidayCalenderHistory hch = hchr_repo.save(h);
		return hch;
	}
	
//	public void saveToHolidayCalenderHistory(HolidayCalender existProduct) throws Exception {
//		hchr_repo.save(existProduct);
//	}
	
	public List<HolidayCalenderHistory> listAll() {
		List<HolidayCalenderHistory> h = hchr_repo.findAll1();
		return h;
	}

	public List<HolidayCalenderHistory> listAllHolidayCalenderHistory() {
		List<HolidayCalenderHistory> hch=hchr_repo.findAll();
		return hch;
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		
		Page<Object> hch_filtered_response = hchr_repo.getAllDataFilteredByKeyword(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, hch_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> hch_sorted_response = hchr_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, hch_sorted_response);
	}
	
	

	public HolidayCalenderHistory saveHolidayCalenderHistory1(HolidayCalenderHistory hch) {
		return hchr_repo.save(hch);
	}

	public HolidayCalenderHistory get(Integer hch_id) {
		return hchr_repo.findById(hch_id).orElseThrow(() -> new ResourceNotFoundException("Roles Id Not Found:" + hch_id));
		
	}

	public void deactivate(Integer hch_id) {
		HolidayCalenderHistory hc = hchr_repo.findById(hch_id).orElseThrow(() -> new ResourceNotFoundException("Roles Id Not Found:" + hch_id));
		hchr_repo.updateToDeactive(hch_id);
	
		
	}

	public void activate(Integer hch_id) {
		HolidayCalenderHistory hc = hchr_repo.findById(hch_id).orElseThrow(() -> new ResourceNotFoundException("Roles Id Not Found:" + hch_id));
		hchr_repo.updateToActive(hch_id);
		
	}

	public void saveToHolidayCalenderHistory(@Valid HolidayCalender existProduct) {
		HolidayCalenderHistory h1 = new HolidayCalenderHistory();
			//HolidayCalenderHistory hhhh = new HolidayCalenderHistory(existProduct);
		h1.setActive(existProduct.getActive());
		h1.setCreatedBy(existProduct.getCreatedBy());
			//h1.setCreatedDate(existProduct.getCreatedDate());
		h1.setCreatedUsername(existProduct.getCreatedUsername());
		h1.setDaysCount(existProduct.getDaysCount());
		h1.setFromDate(existProduct.getFromDate());
		h1.setHolidayCalendarId(existProduct.getHolidayCalendarId());
		h1.setHolidayName(existProduct.getHolidayName());
		//h1.setHolidayTypeId(existProduct.getHolidayTypeId());
		h1.setLeave_type_short(existProduct.getLeave_type_short());
		h1.setLeave_type(existProduct.getLeave_type());
		h1.setJobTypeId(existProduct.getJobTypeId());
		h1.setModifiedBy(existProduct.getModifiedBy());
		h1.setModifiedDate(existProduct.getModifiedDate());
		h1.setModifiedUsername(existProduct.getModifiedUsername());
		h1.setSchoolId(existProduct.getSchoolId());
		hchr_repo.save(h1);
	}
}
