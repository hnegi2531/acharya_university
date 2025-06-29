package com.au.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.model.VacationHolidayCalendar;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.VacationHolidayCalendarRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

@Service
public class VacationHolidayCalendarService {
	
	@Autowired
	private VacationHolidayCalendarRepository vhc_repo;
	
	@Autowired
	private UserAuthenticationRepository uar_repo;
	
	private Logger logger = LoggerFactory.getLogger(VacationHolidayCalendarService.class);
	
	
	public VacationHolidayCalendar createVacationHolidayCalendar(VacationHolidayCalendar fts) throws Exception {

		//vhc_repo.getDataForValidation()
		if(vhc_repo.checkValidation(fts.getFromDate(), fts.getToDate(), fts.getSchoolId()) >=1) 
			throw new RuntimeException("Window already created for the selected dates!!!");
			
		return vhc_repo.save(fts);
	}

	public List<VacationHolidayCalendar> listAll() {
		return vhc_repo.findAll1();
	}

	public VacationHolidayCalendar get(Integer vacationId) {
		return vhc_repo.findById(vacationId)
				.orElseThrow(() -> new ResourceNotFoundException("VacationHolidayCalendar Not Found:" + vacationId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = vhc_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = vhc_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public VacationHolidayCalendar updateVacationHolidayCalendar(VacationHolidayCalendar pr) {
		return vhc_repo.save(pr);
	}

	public void delete(Integer vacationId) {
		VacationHolidayCalendar sir = vhc_repo.findById(vacationId)
				.orElseThrow(() -> new ResourceNotFoundException("VacationHolidayCalendar Not Found:" + vacationId));
		vhc_repo.updateVacationHolidayCalendar(vacationId);
	}

	public void delete1(Integer vacationId) {
		VacationHolidayCalendar sir = vhc_repo.findById(vacationId)
				.orElseThrow(() -> new ResourceNotFoundException("VacationHolidayCalendar Not Found:" + vacationId));
		vhc_repo.updateVacationHolidayCalendar1(vacationId);
	}

}
