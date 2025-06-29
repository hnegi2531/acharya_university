package com.au.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.au.repository.FeeTemplateRepository;
import com.au.repository.PaidBoardDueRepository;
import com.au.response.ResponseHandler;



@Service
public class PaidBoardDueService {

	Logger log = LoggerFactory.getLogger(PaidBoardDueService.class);
	
	@Autowired
	private PaidBoardDueRepository paidBoardDueRepository;
	
	@Autowired
	private FeeTemplateRepository feeTemplateRepository;

	public ResponseEntity<Object> paidBoardReportBasedOnFeeAdmissionCategory() {
		List<Map<String, Object>> PaidBoardDuereport=paidBoardDueRepository.paidBoardReportBasedOnFeeAdmissionCategory();
		return ResponseHandler.generateResponse(true, HttpStatus.OK, PaidBoardDuereport);
	}

	public ResponseEntity<Object> paidBoardReportBasedOnSchoolByBoard(Integer boardUniqueId) {
		List<Map<String, Object>>  PaidBoardDuereport=paidBoardDueRepository.paidBoardReportBasedOnSchoolByBoard(boardUniqueId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, PaidBoardDuereport);
	}

	public ResponseEntity<Object> paidBoardReportBasedOnAcademicYearByBoardAndSchool(Integer boardUniqueId,
			Integer schoolId) {
		List<Map<String, Object>>  PaidBoardDuereport=paidBoardDueRepository.paidBoardReportBasedOnAcademicYearByBoardAndSchool(boardUniqueId,schoolId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, PaidBoardDuereport);
	}

	public List<Map<String, Object>> studentDetailsByBoardSchoolAcademicYear(Integer boardUniqueId, Integer schoolId,
			Integer academicYearId) {
		List<Map<String, Object>>  PaidBoardDuereport= paidBoardDueRepository.studentDetailsByBoardSchoolAcademicYear(boardUniqueId,schoolId,academicYearId);
		return PaidBoardDuereport;
	}

	public ResponseEntity<Object> feeTemplateDetailsByAcademicYearAndYearSem(Integer acYearId, Integer yearAndSem) {
		List<Map<String, Object>> feeTemplateDetails=feeTemplateRepository.feeTemplateDetailsByAcademicYearAndYearSem(acYearId,yearAndSem);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, feeTemplateDetails);
	}
}
