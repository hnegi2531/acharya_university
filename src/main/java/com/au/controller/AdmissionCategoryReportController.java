package com.au.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.au.service.AdmissionCategoryReportService;

@RestController
@RequestMapping("/api/admissionCategoryReport")
@CrossOrigin
public class AdmissionCategoryReportController {

	@Autowired
	private AdmissionCategoryReportService admissionCategoryReportService;

	@GetMapping("/getAdmissionCategoryReportAcademicYearWise")
	public ResponseEntity<Object> getAdmissionCategoryReportAcademicYearWise(
			@RequestParam("acYearId") Integer acYearId) {
		return admissionCategoryReportService.getAdmissionCategoryReportAcademicYearWise(acYearId);
	}
	
	@GetMapping("/getAdmissionCategoryTotalReportAcademicYearWise")
	public ResponseEntity<Object> getAdmissionCategoryTotalReportAcademicYearWise(
			@RequestParam("acYearId") Integer acYearId) {
		return admissionCategoryReportService.getAdmissionCategoryTotalReportAcademicYearWise(acYearId);
	}
	
	@GetMapping("/getAdmissionCategoryReportInstituteWiseWithCategory")
	public ResponseEntity<Object> getAdmissionCategoryReportInstituteWiseWithCategory(
			@RequestParam("feeAdmissionId") Integer feeAdmissionId) {
		return admissionCategoryReportService.getAdmissionCategoryReportInstituteWiseWithCategory(feeAdmissionId);

	}
	
	@GetMapping("/getAdmissionCategoryReportSchoolWiseWithCategory")
	public ResponseEntity<Object> getAdmissionCategoryReportSchoolWiseWithCategory(
			@RequestParam("feeAdmissionId") Integer feeAdmissionId,@RequestParam("acYearId") Integer acYearId) {
		return admissionCategoryReportService.getAdmissionCategoryReportSchoolWiseWithCategory(acYearId,feeAdmissionId);

	}
	
	@GetMapping("/getAdmissionCategoryReportSpecializationWise")
	public ResponseEntity<Object> getAdmissionCategoryReportSpecializationWise(
			@RequestParam("feeAdmissionId") Integer feeAdmissionId,@RequestParam("acYearId") Integer acYearId,
			@RequestParam("schoolId") Integer schoolId) {
		return admissionCategoryReportService.getAdmissionCategoryReportSpecializationWise(feeAdmissionId,acYearId,schoolId);

	}
	
	@GetMapping("/getAdmissionCategoryReportInstituteWise")
	public ResponseEntity<Object> getAdmissionCategoryReportInstituteWise(
			@RequestParam("acYearId") Integer acYearId) {
		return admissionCategoryReportService.getAdmissionCategoryReportInstituteWise(acYearId);

	}
	

	@GetMapping("/getAdmissionCategoryProgramWiseTotalReportAcademicYearWise")
	public ResponseEntity<Object> getAdmissionCategoryProgramWiseTotalReportAcademicYearWise(
			@RequestParam("acYearId") Integer acYearId) {
		return admissionCategoryReportService.getAdmissionCategoryProgramWiseTotalReportAcademicYearWise(acYearId);
	}
	
	

	@GetMapping("/getAdmissionCategoryTotalReportAcademicYearAndSchoolWise")
	public ResponseEntity<Object> getAdmissionCategoryTotalReportAcademicYearAndSchoolWise(
			@RequestParam("acYearId") Integer acYearId ,@RequestParam("schoolId") Integer schoolId) {
		return admissionCategoryReportService.getAdmissionCategoryTotalReportAcademicYearAndSchoolWise(acYearId ,schoolId);
	}
}
