package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.CoursePriceRequest;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.CoursePrice;
import com.au.repository.CoursePriceRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class CoursePriceService {

	@Autowired
	private CoursePriceRepository cpr_repo;

	@Autowired
	private JwtTokenService jwt_service;

	public List<CoursePrice> listAll() {
		return cpr_repo.findAll1();
	}

//	public List<HashMap<String, Object>> listAll1() {
//		return cpr_repo.findAl();
//	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = cpr_repo.findAll2(pageable, keyword );
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	
	}
	
	public ResponseEntity<Object> listAll2(Pageable pageable) {
		Page<Object> response = cpr_repo.findAll3(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
		
	}

	public List<CoursePrice> saveCoursePrice(CoursePriceRequest cpr, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		List<CoursePrice> list = new ArrayList<CoursePrice>();
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		// if (cpr.getCourse_mode().getOnline() != null) {
		CoursePrice coursePrice = new CoursePrice();
		coursePrice.setCourse_assignment_id(cpr.getCourse_assignment_id());
		coursePrice.setAc_year_id(cpr.getAc_year_id());
		coursePrice.setSchool_id(cpr.getSchool_id());
		coursePrice.setProgram_id(cpr.getProgram_id());
		coursePrice.setDept_id(cpr.getDept_id());
		coursePrice.setProgram_specialization_id(cpr.getProgram_specialization_id());
		coursePrice.setCourse_id(cpr.getCourse_id());
		coursePrice.setCourse_category_id(cpr.getCourse_category_id());
		coursePrice.setYear_sem(cpr.getYear_sem());
		coursePrice.setCourse_mode("Online");
		coursePrice.setCourse_price(cpr.getCourse_mode().getOnline().getCourse_price());
		coursePrice.setCourse_price_usd(cpr.getCourse_mode().getOnline().getCourse_price_usd());
		coursePrice.setActive(cpr.getActive());
		coursePrice.setCreated_by(jwtDetails.getUserId());
		coursePrice.setCreated_username(jwtDetails.getUserName());
		saveCourses(coursePrice);
		list.add(coursePrice);
		// } else if (cpr.getCourse_mode().getOffline() != null) {
		CoursePrice coursePrice1 = new CoursePrice();
		coursePrice1.setCourse_assignment_id(cpr.getCourse_assignment_id());
		coursePrice1.setAc_year_id(cpr.getAc_year_id());
		coursePrice1.setSchool_id(cpr.getSchool_id());
		coursePrice1.setProgram_id(cpr.getProgram_id());
		coursePrice1.setDept_id(cpr.getDept_id());
		coursePrice1.setProgram_specialization_id(cpr.getProgram_specialization_id());
		coursePrice1.setCourse_id(cpr.getCourse_id());
		coursePrice1.setCourse_category_id(cpr.getCourse_category_id());
		coursePrice1.setYear_sem(cpr.getYear_sem());
		coursePrice1.setCourse_mode("Offline");
		coursePrice1.setCourse_price(cpr.getCourse_mode().getOffline().getCourse_price());
		coursePrice1.setCourse_price_usd(cpr.getCourse_mode().getOffline().getCourse_price_usd());
		coursePrice1.setActive(cpr.getActive());
		coursePrice1.setCreated_by(jwtDetails.getUserId());
		coursePrice1.setCreated_username(jwtDetails.getUserName());
		saveCourses(coursePrice1);
		list.add(coursePrice1);
		return list;
	}

	public CoursePrice saveCourses(CoursePrice cp) {
		return cpr_repo.save(cp);
	}

	public CoursePrice get(Integer id) {
		return cpr_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CoursePrice id Not Found:" + id));
	}

	public void delete(Integer id) {
		CoursePrice cc = cpr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CoursePrice Not Found:" + id));
		cpr_repo.updateCoursePrice(id);
	}

	public void delete1(Integer id) {
		CoursePrice cc = cpr_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CoursePrice Not Found:" + id));
		cpr_repo.updateCoursePrice1(id);

	}
}
