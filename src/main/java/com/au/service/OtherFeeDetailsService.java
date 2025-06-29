package com.au.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.au.event.StudentDueEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.JwtDetails;
import com.au.dto.OtherFeeDetailsDTO;
import com.au.dto.OtherFeeDetailsSemWiseDTO;
import com.au.dto.OtherFeeTemplateDTO;
import com.au.dto.PaginationDTO;
import com.au.model.EnvBillDetails;
import com.au.model.OtherFeeDetails;
import com.au.model.OtherFeeTemplate;
import com.au.repository.Academic_year_repository;
import com.au.repository.OtherFeeDetailsRepository;
import com.au.repository.OtherFeeTemplateRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.VoucherHeadRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class OtherFeeDetailsService {

	@Autowired
	private OtherFeeDetailsRepository otherFeeDetailsRepository;

	@Autowired
	private OtherFeeTemplateRepository otherFeeTemplateRepository;

	@Autowired
	private ProgramSpecilizationRepository programSpecilizationRepository;

	@Autowired
	private Academic_year_repository academicYearRepository;

	@Autowired
	private VoucherHeadRepository voucherHeadRepository;

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;



//	public ResponseEntity<Object> createOtherFees(OtherFeeTemplateDTO otherFeeTemplateDTO, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
//		
//			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
//			List<String> alreadyExistUniformList = new ArrayList<String>();
//			
//			if(otherFeeTemplateDTO.getFee_template_id() ==null) {
//				try {
//			otherFeeTemplateDTO.getProgramSpecializationId().stream().forEach(oft -> {
//				String auidFormat = programSpecilizationRepository.getProgramSpecilizationAuidName(oft);
//				Integer acYearCode = academicYearRepository.findAcademicYearById(otherFeeTemplateDTO.getAcYearId());
//				String year = String.valueOf(acYearCode);
//				String uniformNumber = year.substring(2, 4) + auidFormat;
//
//				Boolean isExists = otherFeeTemplateRepository
//						.existsByUniformNumberAndFeetypeAndAcYearIdAndSchoolIdAndProgramIdAndActive(uniformNumber,
//								otherFeeTemplateDTO.getFeetype(), otherFeeTemplateDTO.getAcYearId(),
//								otherFeeTemplateDTO.getSchoolId(), otherFeeTemplateDTO.getProgramId(), true);
//				if (!isExists) {
//					OtherFeeTemplate otherFeeTemplate = new OtherFeeTemplate();
//					otherFeeTemplate.setActive(Boolean.TRUE);
//					otherFeeTemplate.setAcYearId(otherFeeTemplateDTO.getAcYearId());
//					otherFeeTemplate.setFeetype(otherFeeTemplateDTO.getFeetype());
//					otherFeeTemplate.setProgramId(otherFeeTemplateDTO.getProgramId());
//					otherFeeTemplate.setProgramSpecializationId(oft);
//					otherFeeTemplate.setSchoolId(otherFeeTemplate.getSchoolId());
//					otherFeeTemplate.setFeetype(otherFeeTemplateDTO.getFeetype());
//					otherFeeTemplate.setCreatedBy(jwtDetails.getUserName());
//					otherFeeTemplate.setSchoolId(otherFeeTemplateDTO.getSchoolId());
//					otherFeeTemplate.setFee_admission_category_id(otherFeeTemplateDTO.getFee_admission_category_id());
//					otherFeeTemplate.setCurrency_type_id(otherFeeTemplateDTO.getCurrency_type_id());
//					otherFeeTemplate.setFee_template_id(otherFeeTemplateDTO.getFee_template_id());
//					otherFeeTemplate.setUniformNumber(uniformNumber);
//
//					otherFeeTemplateRepository.save(otherFeeTemplate);
//
//					otherFeeTemplateDTO.getOtherFeeDetailsDTOs().stream().forEach(ofd -> {
//						OtherFeeDetails otherFeeDetails = new OtherFeeDetails();
//						Integer vhId = ofd.getVoucherHeadId();
//						otherFeeDetails.setVoucherHeadId(vhId);
//						otherFeeDetails.setTemplateId(otherFeeTemplate.getOtherFeeTemplateId());
//						otherFeeDetails.setCreatedBy(jwtDetails.getUserName());
//						otherFeeDetails.setSem1(ofd.getSem1());
//						otherFeeDetails.setSem2(ofd.getSem2());
//						otherFeeDetails.setSem3(ofd.getSem3());
//						otherFeeDetails.setSem4(ofd.getSem4());
//						otherFeeDetails.setSem5(ofd.getSem5());
//						otherFeeDetails.setSem6(ofd.getSem6());
//						otherFeeDetails.setSem7(ofd.getSem7());
//						otherFeeDetails.setSem8(ofd.getSem8());
//						otherFeeDetails.setYear1(ofd.getYear1());
//						otherFeeDetails.setYear2(ofd.getYear2());
//						otherFeeDetails.setYear3(ofd.getYear3());
//						otherFeeDetails.setYear4(ofd.getYear4());
//						otherFeeDetails.setTotal(ofd.getTotal());	
//						otherFeeDetails.setActive(Boolean.TRUE);
//						otherFeeDetailsRepository.save(otherFeeDetails);
//
//					});
//				} else {
//					alreadyExistUniformList.add(uniformNumber + " " + otherFeeTemplateDTO.getFeetype());
//				}
//			});
//			if (ObjectUtils.isNotEmpty(alreadyExistUniformList)) {
//				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
//						"Can't created for " + alreadyExistUniformList);
//			}
//			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
//
//				}catch (Exception e) {
//			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
//		}
//			}else {
//				try {
//				otherFeeTemplateDTO.getProgramSpecializationId().stream().forEach(oft -> {
//					Boolean isExists = otherFeeTemplateRepository
//							.existsByUniformNumberAndFeetypeAndAcYearIdAndSchoolIdAndProgramId(
//									otherFeeTemplateDTO.getFeetype(), otherFeeTemplateDTO.getAcYearId(),
//									otherFeeTemplateDTO.getSchoolId(), otherFeeTemplateDTO.getProgramId() , true);
//					if (!isExists) {
//						OtherFeeTemplate otherFeeTemplate = new OtherFeeTemplate();
//						otherFeeTemplate.setActive(Boolean.TRUE);
//						otherFeeTemplate.setAcYearId(otherFeeTemplateDTO.getAcYearId());
//						otherFeeTemplate.setFeetype(otherFeeTemplateDTO.getFeetype());
//						otherFeeTemplate.setProgramId(otherFeeTemplateDTO.getProgramId());
//						otherFeeTemplate.setProgramSpecializationId(oft);
//						otherFeeTemplate.setSchoolId(otherFeeTemplate.getSchoolId());
//						otherFeeTemplate.setFeetype(otherFeeTemplateDTO.getFeetype());
//						otherFeeTemplate.setCreatedBy(jwtDetails.getUserName());
//						otherFeeTemplate.setSchoolId(otherFeeTemplateDTO.getSchoolId());
//						otherFeeTemplate.setFee_admission_category_id(otherFeeTemplateDTO.getFee_admission_category_id());
//						otherFeeTemplate.setCurrency_type_id(otherFeeTemplateDTO.getCurrency_type_id());
//						otherFeeTemplate.setFee_template_id(otherFeeTemplateDTO.getFee_template_id());
//						
//
//						otherFeeTemplateRepository.save(otherFeeTemplate);
//
//						otherFeeTemplateDTO.getOtherFeeDetailsDTOs().stream().forEach(ofd -> {
//							OtherFeeDetails otherFeeDetails = new OtherFeeDetails();
//							Integer vhId = ofd.getVoucherHeadId();
//							otherFeeDetails.setVoucherHeadId(vhId);
//							otherFeeDetails.setTemplateId(otherFeeTemplate.getOtherFeeTemplateId());
//							otherFeeDetails.setCreatedBy(jwtDetails.getUserName());
//							otherFeeDetails.setSem1(ofd.getSem1());
//							otherFeeDetails.setSem2(ofd.getSem2());
//							otherFeeDetails.setSem3(ofd.getSem3());
//							otherFeeDetails.setSem4(ofd.getSem4());
//							otherFeeDetails.setSem5(ofd.getSem5());
//							otherFeeDetails.setSem6(ofd.getSem6());
//							otherFeeDetails.setSem7(ofd.getSem7());
//							otherFeeDetails.setSem8(ofd.getSem8());
//							otherFeeDetails.setYear1(ofd.getYear1());
//							otherFeeDetails.setYear2(ofd.getYear2());
//							otherFeeDetails.setYear3(ofd.getYear3());
//							otherFeeDetails.setYear4(ofd.getYear4());
//							otherFeeDetails.setTotal(ofd.getTotal());	
//							otherFeeDetails.setActive(Boolean.TRUE);
//							otherFeeDetailsRepository.save(otherFeeDetails);
//
//						});
//					} 
//				});
//				if (ObjectUtils.isNotEmpty(alreadyExistUniformList)) {
//					return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
//							"Can't created for " + alreadyExistUniformList);
//				}
//				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
//
//			} catch (Exception e) {
//				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
//			}
//			}
//
//			
//	}
	
	
	public ResponseEntity<Object> createOtherFees(OtherFeeTemplateDTO otherFeeTemplateDTO, String jwtToken) throws JsonParseException, JsonMappingException, IOException {
	    JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
	    List<String> alreadyExistUniformList = new ArrayList<>();

	    try {
	        if (otherFeeTemplateDTO.getFee_template_id() == null) {
	            // Logic for creating a new fee template when fee_template_id is null
	            handleFeeTemplateCreation(otherFeeTemplateDTO, jwtDetails, alreadyExistUniformList);
	        } else {
	            // Logic for updating or checking existing fee template
	            handleFeeTemplateUpdate(otherFeeTemplateDTO, jwtDetails, alreadyExistUniformList);
	        }

	        if (!alreadyExistUniformList.isEmpty()) {
	            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS",
	                    "Can't create for: " + alreadyExistUniformList);
	        }
	        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
	        
	    } catch (Exception e) {
	        return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", e.getMessage());
	    }
	}

	private void handleFeeTemplateCreation(OtherFeeTemplateDTO dto, JwtDetails jwtDetails, List<String> alreadyExistUniformList) {
	    dto.getProgramSpecializationId().forEach(oft -> {
	        String auidFormat = programSpecilizationRepository.getProgramSpecilizationAuidName(oft);
	        Integer acYearCode = academicYearRepository.findAcademicYearById(dto.getAcYearId());
	        String year = String.valueOf(acYearCode);
	        String uniformNumber = year.substring(2, 4) + auidFormat;

	        Integer isExists = otherFeeTemplateRepository.existsByUniformNumberAndFeetypeAndAcYearIdAndSchoolIdAndProgramIdAndActive(
	                uniformNumber, dto.getFeetype(), dto.getAcYearId(),
	                dto.getSchoolId(), dto.getProgramId(), true);

	        if (isExists != null) {
	            saveOtherFeeTemplate(dto, jwtDetails, uniformNumber,oft);
	        } else {
	            alreadyExistUniformList.add(uniformNumber + " " + dto.getFeetype());
	        }
	    });
	}

	private void handleFeeTemplateUpdate(OtherFeeTemplateDTO dto, JwtDetails jwtDetails, List<String> alreadyExistUniformList) {
	    // Logic similar to creation, but you may want to check for existing records differently
//	    dto.getProgramSpecializationId().forEach(oft -> {
	    	Integer isExists = otherFeeTemplateRepository.existsByUniformNumberAndFeetypeAndAcYearIdAndSchoolIdAndProgramId1(
	                dto.getFeetype(), dto.getAcYearId(),
	                dto.getSchoolId(), dto.getProgramId(), true);

	    	 if (isExists != null) {
	            saveOtherFeeTemplate1(dto, jwtDetails, null);
	        } else {
	            alreadyExistUniformList.add("Template exists for specialization ID: " );
	        }
//	    });
	}

	private void saveOtherFeeTemplate(OtherFeeTemplateDTO dto, JwtDetails jwtDetails, String uniformNumber, Integer programSpecializationId) {
	    OtherFeeTemplate otherFeeTemplate = new OtherFeeTemplate();
	    otherFeeTemplate.setActive(true);
	    otherFeeTemplate.setAcYearId(dto.getAcYearId());
	    otherFeeTemplate.setFeetype(dto.getFeetype());
	    otherFeeTemplate.setProgramId(dto.getProgramId());
	    otherFeeTemplate.setProgramSpecializationId(programSpecializationId);
	    otherFeeTemplate.setSchoolId(dto.getSchoolId());
	    otherFeeTemplate.setCreatedBy(jwtDetails.getUserName());
	    otherFeeTemplate.setFee_admission_category_id(dto.getFee_admission_category_id());
	    otherFeeTemplate.setCurrency_type_id(dto.getCurrency_type_id());
	    otherFeeTemplate.setFee_template_id(dto.getFee_template_id());
	    if (uniformNumber != null) {
	        otherFeeTemplate.setUniformNumber(uniformNumber);
	    }

	    otherFeeTemplateRepository.save(otherFeeTemplate);
	    saveOtherFeeDetails(dto.getOtherFeeDetailsDTOs(), otherFeeTemplate, jwtDetails);
	}
	
	private void saveOtherFeeTemplate1(OtherFeeTemplateDTO dto, JwtDetails jwtDetails, String uniformNumber) {
	    OtherFeeTemplate otherFeeTemplate = new OtherFeeTemplate();
	    otherFeeTemplate.setActive(true);
	    otherFeeTemplate.setAcYearId(dto.getAcYearId());
	    otherFeeTemplate.setFeetype(dto.getFeetype());
	    otherFeeTemplate.setProgramId(dto.getProgramId());
//	    otherFeeTemplate.setProgramSpecializationId(programSpecializationId);
	    otherFeeTemplate.setSchoolId(dto.getSchoolId());
	    otherFeeTemplate.setCreatedBy(jwtDetails.getUserName());
	    otherFeeTemplate.setFee_admission_category_id(dto.getFee_admission_category_id());
	    otherFeeTemplate.setCurrency_type_id(dto.getCurrency_type_id());
	    otherFeeTemplate.setFee_template_id(dto.getFee_template_id());
	    if (uniformNumber != null) {
	        otherFeeTemplate.setUniformNumber(uniformNumber);
	    }

	    otherFeeTemplateRepository.save(otherFeeTemplate);
	    saveOtherFeeDetails(dto.getOtherFeeDetailsDTOs(), otherFeeTemplate, jwtDetails);
	}

	private void saveOtherFeeDetails(List<OtherFeeDetailsDTO> detailsDTOs, OtherFeeTemplate otherFeeTemplate, JwtDetails jwtDetails) {
	    detailsDTOs.forEach(ofd -> {
	        OtherFeeDetails otherFeeDetails = new OtherFeeDetails();
	        otherFeeDetails.setVoucherHeadId(ofd.getVoucherHeadId());
	        otherFeeDetails.setTemplateId(otherFeeTemplate.getOtherFeeTemplateId());
	        otherFeeDetails.setCreatedBy(jwtDetails.getUserName());
	        otherFeeDetails.setSem1(ofd.getSem1());
	        otherFeeDetails.setSem2(ofd.getSem2());
	        otherFeeDetails.setSem3(ofd.getSem3());
	        otherFeeDetails.setSem4(ofd.getSem4());
	        otherFeeDetails.setSem5(ofd.getSem5());
	        otherFeeDetails.setSem6(ofd.getSem6());
	        otherFeeDetails.setSem7(ofd.getSem7());
	        otherFeeDetails.setSem8(ofd.getSem8());
	        otherFeeDetails.setYear1(ofd.getYear1());
	        otherFeeDetails.setYear2(ofd.getYear2());
	        otherFeeDetails.setYear3(ofd.getYear3());
	        otherFeeDetails.setYear4(ofd.getYear4());
	        otherFeeDetails.setTotal(ofd.getTotal());
	        otherFeeDetails.setActive(true);
	        otherFeeDetailsRepository.save(otherFeeDetails);
	    });
	}


	public ResponseEntity<Object> getOtherFeetemplate(Integer pageNo, Integer pageSize) {
		try {
			Pageable pageable = PageRequest.of(pageNo, pageSize);

			Page<Map<String, Object>> otherFeeTemplates = otherFeeTemplateRepository.getOtherFeeDetails(pageable);
			PaginationDTO paginationDTO = new PaginationDTO();
			paginationDTO.setContent(otherFeeTemplates.getContent());
			paginationDTO.setIslast(otherFeeTemplates.isLast());
			paginationDTO.setPageNo(pageNo);
			paginationDTO.setPageSize(pageSize);
			paginationDTO.setTotalElement(otherFeeTemplates.getTotalElements());
			paginationDTO.setTotalPage(otherFeeTemplates.getTotalPages());
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", paginationDTO);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getOtherFeeDetails(Integer otherFeeTemplateId, String feeType) {
		try {
			List<OtherFeeDetails> otherFeeDetails = otherFeeDetailsRepository.getOtherFeeDetails(otherFeeTemplateId,
					feeType);
//			OtherFeeDetailsSemWiseDTO otherFeeDetailsSemWiseDTO = new OtherFeeDetailsSemWiseDTO();
//			Double grantTotalSem1 = otherFeeDetails.stream().filter(fee -> fee.getSem1() != null).mapToDouble(OtherFeeDetails::getSem1).sum();
//			Double grantTotalSem2 = otherFeeDetails.stream().filter(fee -> fee.getSem2() != null).mapToDouble(OtherFeeDetails::getSem2).sum();
//			Double grantTotalSem3 = otherFeeDetails.stream().filter(fee -> fee.getSem3() != null).mapToDouble(OtherFeeDetails::getSem3).sum();
//			Double grantTotalSem4 = otherFeeDetails.stream().filter(fee -> fee.getSem4() != null).mapToDouble(OtherFeeDetails::getSem4).sum();
//			Double grantTotalSem5 = otherFeeDetails.stream().filter(fee -> fee.getSem5() != null).mapToDouble(OtherFeeDetails::getSem5).sum();
//			Double grantTotalSem6 = otherFeeDetails.stream().filter(fee -> fee.getSem6() != null).mapToDouble(OtherFeeDetails::getSem6).sum();
//			Double grantTotalSem7 = otherFeeDetails.stream().filter(fee -> fee.getSem7() != null).mapToDouble(OtherFeeDetails::getSem7).sum();
//			Double grantTotalSem8 = otherFeeDetails.stream().filter(fee -> fee.getSem8() != null).mapToDouble(OtherFeeDetails::getSem8).sum();
//			Double grantTotalSem9 = otherFeeDetails.stream().filter(fee -> fee.getSem9() != null).mapToDouble(OtherFeeDetails::getSem9).sum();
//			Double grantTotalSem10 = otherFeeDetails.stream().filter(fee -> fee.getSem10() != null).mapToDouble(OtherFeeDetails::getSem10).sum();
//			Double grantTotalSem11 = otherFeeDetails.stream().filter(fee -> fee.getSem11() != null).mapToDouble(OtherFeeDetails::getSem11).sum();
//			Double grantTotalSem12 = otherFeeDetails.stream().filter(fee -> fee.getSem12() != null).mapToDouble(OtherFeeDetails::getSem12).sum();
//
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem1(grantTotalSem1.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem2(grantTotalSem2.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem3(grantTotalSem3.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem4(grantTotalSem4.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem5(grantTotalSem5.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem6(grantTotalSem6.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem7(grantTotalSem7.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem8(grantTotalSem8.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem9(grantTotalSem9.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem10(grantTotalSem10.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem11(grantTotalSem11.floatValue());
//			otherFeeDetailsSemWiseDTO.setGrantTotalSem12(grantTotalSem12.floatValue());
//			
//			otherFeeDetailsSemWiseDTO.setOtherFeeDetails(otherFeeDetails);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", otherFeeDetails);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> updateOtherFeeDetails(List<OtherFeeDetailsDTO> otherFeeTemplateDTOs,
			Integer otherFeeTemplateId, String jwtToken) {
		try {
			List<OtherFeeDetails> existingOtherFeeDetails = otherFeeDetailsRepository
					.getOtherFeeDetailsById(otherFeeTemplateId);

			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

			if (ObjectUtils.isNotEmpty(existingOtherFeeDetails)) {

				otherFeeTemplateDTOs.forEach(dto -> {
					if (ObjectUtils.isNotEmpty(dto.getOtherFeeDetailsId())) {
						OtherFeeDetails existingDetail = otherFeeDetailsRepository.findById(dto.getOtherFeeDetailsId())
								.get();
						if (existingDetail != null) {
							existingDetail.setSem1(dto.getSem1());
							existingDetail.setSem2(dto.getSem2());
							existingDetail.setSem3(dto.getSem3());
							existingDetail.setSem4(dto.getSem4());
							existingDetail.setSem5(dto.getSem5());
							existingDetail.setSem6(dto.getSem6());
							existingDetail.setSem7(dto.getSem7());
							existingDetail.setSem8(dto.getSem8());
							existingDetail.setSem9(dto.getSem9());
							existingDetail.setSem10(dto.getSem10());
							existingDetail.setSem11(dto.getSem11());
							existingDetail.setSem12(dto.getSem12());
							existingDetail.setYear1(dto.getYear1());
							existingDetail.setYear2(dto.getYear2());
							existingDetail.setYear3(dto.getYear3());
							existingDetail.setYear4(dto.getYear4());
							existingDetail.setTotal(dto.getTotal());
							existingDetail.setVoucherHeadId(dto.getVoucherHeadId());
							existingDetail.setModifiedBy(jwtDetails.getUserName());
						}
					} else {
						OtherFeeDetails otherFeeDetails = new OtherFeeDetails();
						otherFeeDetails.setSem1(dto.getSem1());
						otherFeeDetails.setSem2(dto.getSem2());
						otherFeeDetails.setSem3(dto.getSem3());
						otherFeeDetails.setSem4(dto.getSem4());
						otherFeeDetails.setSem5(dto.getSem5());
						otherFeeDetails.setSem6(dto.getSem6());
						otherFeeDetails.setSem7(dto.getSem7());
						otherFeeDetails.setSem8(dto.getSem8());
						otherFeeDetails.setSem9(dto.getSem9());
						otherFeeDetails.setSem10(dto.getSem10());
						otherFeeDetails.setSem11(dto.getSem11());
						otherFeeDetails.setSem12(dto.getSem12());
						otherFeeDetails.setYear1(dto.getYear1());
						otherFeeDetails.setYear2(dto.getYear2());
						otherFeeDetails.setYear3(dto.getYear3());
						otherFeeDetails.setYear4(dto.getYear4());
						otherFeeDetails.setTotal(dto.getTotal());
						otherFeeDetails.setModifiedBy(jwtDetails.getUserName());
						otherFeeDetails.setCreatedBy(jwtDetails.getUserName());
						otherFeeDetails.setVoucherHeadId(dto.getVoucherHeadId());
						otherFeeDetails.setTemplateId(otherFeeTemplateId);
						otherFeeDetails.setActive(Boolean.TRUE);
						otherFeeDetailsRepository.save(otherFeeDetails);
					}
				});

				otherFeeDetailsRepository.saveAll(existingOtherFeeDetails);

				Integer programId = otherFeeTemplateRepository.getProgramIdByOtherFeeTemplateId(otherFeeTemplateId);
				StudentDueEvent studentDueEvent=new StudentDueEvent(null,programId,null,null);
				applicationEventPublisher.publishEvent(studentDueEvent);
			}


			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> deleteOtherFeeDetails(Integer otherFeeDetailId, String jwtToken) {
		try {
			OtherFeeDetails otherFeeDetails = otherFeeDetailsRepository.findById(otherFeeDetailId).get();
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			if (ObjectUtils.isNotEmpty(otherFeeDetails)) {
				otherFeeDetails.setModifiedBy(jwtDetails.getUserName());
				otherFeeDetails.setActive(Boolean.FALSE);
				otherFeeDetailsRepository.save(otherFeeDetails);
			}
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getProgramsDetails(Integer schoolId, Integer acYearId) {
		try {
			List<Map<String, Object>> programDetails = programSpecilizationRepository.getProgramsDetails(schoolId,
					acYearId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", programDetails);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getVoucherHeads() {
		try {
			List<Map<String, Object>> voucherHeadDetails = voucherHeadRepository.getVoucherHeads();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", voucherHeadDetails);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> deleteOtherFeeTemplate(Integer otherFeeTemplateId, String jwtToken) {
		try {
			OtherFeeTemplate otherFeeTemplate = otherFeeTemplateRepository.findById(otherFeeTemplateId).get();
			otherFeeTemplate.setActive(Boolean.FALSE);
			otherFeeTemplateRepository.save(otherFeeTemplate);

			List<OtherFeeDetails> otherFeeDetails = otherFeeDetailsRepository
					.getOtherFeeDetailsById(otherFeeTemplateId);
			otherFeeDetails.stream().forEach(e -> {
				e.setActive(Boolean.FALSE);
				otherFeeDetailsRepository.save(e);
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> reactiveOtherFeetemplate(Integer otherFeeTemplateId, String jwtToken) {
		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			OtherFeeTemplate otherFeeTemplate = otherFeeTemplateRepository.findById(otherFeeTemplateId).get();
			otherFeeTemplate.setActive(Boolean.TRUE);
			otherFeeTemplate.setModifiedBy(jwtDetails.getUserName());
			otherFeeTemplateRepository.save(otherFeeTemplate);

			List<OtherFeeDetails> otherFeeDetails = otherFeeDetailsRepository
					.getOtherFeeDetailsDeactiveRecordsById(otherFeeTemplateId);
			otherFeeDetails.stream().forEach(e -> {
				e.setActive(Boolean.TRUE);
				e.setModifiedBy(jwtDetails.getUserName());
				otherFeeDetailsRepository.save(e);
			});

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}

	}

	public List<Map<String, Object>> getOtherFeeDetailsData(Integer schoolId, Integer acYearId, Integer programId,
			List<Integer> programSpecializationId) {

		List<Map<String, Object>> arr = new ArrayList<Map<String, Object>>();

		programSpecializationId.stream().forEach(ps -> {

			List<Map<String, Object>> detailsData = otherFeeDetailsRepository.getOtherFeeDetailsData(schoolId, acYearId,
					programId, ps);
			arr.addAll(detailsData);
		});
		return arr;
	}

	public List<Map<String, Object>> getOtherFeeDetailsData1(Integer fee_template_id) {
		// TODO Auto-generated method stub
		return otherFeeDetailsRepository.getOtherFeeDetailsData1(fee_template_id);
	}

		public void deactivate(Integer id) {
			OtherFeeDetails ademail = otherFeeDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EnvBillDetails Not Found:" + id));
		otherFeeDetailsRepository.deactivate(id);
	}
	
	public void activate(Integer id) {
		OtherFeeDetails ademail = otherFeeDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("EnvBillDetails Not Found:" + id));
		otherFeeDetailsRepository.activate(id);
	}

}
