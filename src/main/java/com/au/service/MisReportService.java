package com.au.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.repository.StudentDetailsRepository;
import com.au.response.ResponseHandler;

@Service
public class MisReportService {

	@Autowired
	private StudentDetailsRepository studentDetailsRepository;

	public ResponseEntity<Object> misInstituteWiseReport(Integer acYearId) {
		try {
			List<Map<String, Object>> list=studentDetailsRepository.misInstituteWiseReport(acYearId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);

		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> misYearWiseReport() {
		try {
			List<Map<String, Object>> list=studentDetailsRepository.misYearWiseReport();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);

		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> misDayWiseReport(Integer month, Integer year) {
		try {
			List<Map<String, Object>> list=studentDetailsRepository.misDayWiseReport(month,year);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);

		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> misProgramWiseReport(Integer acYearId, Integer schoolId) {
		try {
			List<Map<String, Object>> list=studentDetailsRepository.misProgramWiseReport(acYearId,schoolId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);

		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> misGenderWiseReport(Integer acYearId, Integer schoolId) {
		try {
			List<Map<String, Object>> list=studentDetailsRepository.misGenderWiseReport(acYearId,schoolId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);

		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> misGeolocationWiseReport(Integer schoolId, Integer countryId,
			Integer stateId, Integer cityId) {
		try {
			if(countryId != null){

				if(stateId != null){

					if (cityId != null){
						return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
								studentDetailsRepository.getGeoData(countryId,stateId, cityId, schoolId));
					}
					return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
							studentDetailsRepository.getGeoData(countryId, stateId, schoolId));
				}
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
						studentDetailsRepository.getGeoData(countryId, schoolId));
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
						studentDetailsRepository.getGeoData(schoolId));
			}

		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getStudentDetailsGeoloactionWise(Integer acYearId, Integer schoolId,
			Integer countryId, Integer stateId, Integer cityId) {
		try {
			List<Map<String, Object>> list=studentDetailsRepository.getStudentDetailsGeoloactionWise(schoolId, acYearId, countryId,stateId,cityId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}
	
	
}
