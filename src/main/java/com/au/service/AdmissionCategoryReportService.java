package com.au.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.repository.FeeAdmissionCategoryRepository;
import com.au.response.ResponseHandler;

@Service
public class AdmissionCategoryReportService {
	@Autowired
	private FeeAdmissionCategoryRepository feeAdmissionCategoryRepository;

	public ResponseEntity<Object> getAdmissionCategoryReportAcademicYearWise(Integer acYearId) {
		try {
			List<Map<String, Object>> list=feeAdmissionCategoryRepository.getAdmissionCategoryReportAcademicYearWise(acYearId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);
			
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getAdmissionCategoryReportInstituteWiseWithCategory(Integer feeAdmissionId) {
		try {
			List<Map<String, Object>> list=feeAdmissionCategoryRepository.getAdmissionCategoryReportInstituteWiseWithCategory(feeAdmissionId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);
			
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}
	
	public ResponseEntity<Object> getAdmissionCategoryReportSchoolWiseWithCategory(Integer acYearId, Integer feeAdmissionId) {
		try {
			List<Map<String, Object>> list=feeAdmissionCategoryRepository.getAdmissionCategoryReportSchoolWiseWithCategory(acYearId,feeAdmissionId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);
			
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}
	
	public ResponseEntity<Object> getAdmissionCategoryReportSpecializationWise(Integer feeAdmissionId, Integer acYearId, Integer schoolId) {
		try {
			List<Map<String, Object>> list=feeAdmissionCategoryRepository.getAdmissionCategoryReportSpecializationWise(feeAdmissionId,acYearId,schoolId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);
			
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getAdmissionCategoryReportInstituteWise(Integer acYearId) {
		try {
			List<Map<String, Object>> list=feeAdmissionCategoryRepository.getAdmissionCategoryReportInstituteWise(acYearId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);
			
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getAdmissionCategoryTotalReportAcademicYearWise(Integer acYearId) {
		try {
			List<Map<String, Object>> list=feeAdmissionCategoryRepository.getAdmissionCategoryTotalReportAcademicYearWise(acYearId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);
			
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}
	
	
	public ResponseEntity<Object> getAdmissionCategoryProgramWiseTotalReportAcademicYearWise(Integer acYearId) {
		try {
			List<Map<String, Object>> list=feeAdmissionCategoryRepository.getAdmissionCategoryProgramWiseTotalReportAcademicYearWise(acYearId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);
			
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getAdmissionCategoryTotalReportAcademicYearAndSchoolWise(Integer acYearId, Integer schoolId) {
		try {
			List<Map<String, Object>> list=feeAdmissionCategoryRepository.getAdmissionCategoryTotalReportAcademicYearAndSchoolWise(acYearId ,schoolId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", list);
			
		}catch(Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

}
