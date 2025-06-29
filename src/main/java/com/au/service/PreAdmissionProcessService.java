package com.au.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.au.dto.FeeDetailsForCandidate;
import com.au.dto.FeeDetailsResponseForCandidate;
import com.au.dto.FeeSubAmountDetailsForCandidate;
import com.au.dto.FeeTemplateDetailsForCandidate;
import com.au.dto.JwtDetails;
import com.au.dto.OtherFeeTemplateForStudentDTO;
import com.au.dto.PreadmissionDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.Candidate_Walkin;
import com.au.model.PreAdmissionProcess;
import com.au.model.Scholarship;
import com.au.model.ScholarshipApprovalStatus;
import com.au.model.ScholarshipAttachment;
import com.au.repository.CandidateWalkinRepository;
import com.au.repository.OtherFeeDetailsRepository;
import com.au.repository.PreAdmissionProcessRepository;
import com.au.repository.ScholarshipApprovalStatusRepository;
import com.au.repository.ScholarshipRepository;
import com.au.response.ResponseHandler;
import com.google.common.base.Optional;

@Service
public class PreAdmissionProcessService {

	@Autowired
	private PreAdmissionProcessRepository p_repo;

	@Autowired
	private ScholarshipRepository s_repo;

	@Autowired
	private ScholarshipApprovalStatusRepository sas_repo;

	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private OtherFeeDetailsRepository otherFeeDetailsRepository;

	@Autowired
	private ScholarshipApprovalStatusRepository scholarshipApprovalStatusRepository;

	@Autowired
	private CandidateWalkInService candidateWalkInService;
	
	@Autowired
	private CandidateWalkinRepository can_repo;

//	public List<PreAdmissionProcess> listAll() {
//		return p_repo.findAll();
//	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {

		Page<Object> response1 = p_repo.findAll1(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAll2(Pageable pageable) {

		Page<Object> response = p_repo.findAll2(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public PreAdmissionProcess save_PreAdmissionProcess(PreAdmissionProcess academic) throws Exception {
		if (p_repo.getCheckCandidateDetails(academic.getCandidate_id()) >= 1) {
			throw new Exception("Offer already created !!!");
		}	
		Candidate_Walkin existingCandidate = can_repo.getExistingCandidate(academic.getCandidate_id());
		existingCandidate.setNpf_status(1);
		candidateWalkInService.updateLsqStatus(academic.getCandidate_id(),2);
		return p_repo.save(academic);
	}

	public ScholarshipApprovalStatus getPreadmissionProcess(PreadmissionDto pdto,
			@RequestHeader("Authorization") String jwtToken)
			throws Exception {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		
		
		if (p_repo.getCheckCandidateDetails(pdto.getPap().getCandidate_id()) >= 1) {
			throw new Exception("Offer already created !!!");
		}
		
		PreAdmissionProcess pre_adm_pro = p_repo.save(pdto.getPap());
		
		Scholarship s1 = new Scholarship();
		s1.setCandidate_id(pdto.getPap().getCandidate_id());
		s1.setRequested_scholarship(pdto.getS().getRequested_scholarship());
		s1.setActive(pdto.getS().getActive());
		s1.setAward(pdto.getS().getAward());
		s1.setAward_details(pdto.getS().getAward_details());
		s1.setCreated_by(jwtDetails.getUserId());
		s1.setCreated_date(pdto.getS().getCreated_date());
		s1.setCreated_username(pdto.getPap().getCreated_username());
		// s1.setModified_username(pdto.getS().getModified_username());
		s1.setExemption_received(pdto.getS().getExemption_received());
		s1.setExemption_type(pdto.getS().getExemption_type());
		s1.setParent_income(pdto.getS().getParent_income());
		s1.setReason(pdto.getS().getReason());
		s1.setResidence(pdto.getS().getResidence());
		s1.setStudent_id(pdto.getS().getStudent_id());
		s1.setOccupation(pdto.getS().getOccupation());
		s1.setPre_admission_id(pre_adm_pro.getPre_admission_id());
		s_repo.save(s1);

		ScholarshipApprovalStatus sasa = new ScholarshipApprovalStatus();
		String todaysDate = ResponseHandler.getStringTypeTodaysDateDDMMYYYY();
        
		sasa.setScholarship_id(s1.getScholarship_id());
		sasa.setCandidate_id(pdto.getPap().getCandidate_id());

		sasa.setCreated_by(jwtDetails.getUserId());

		sasa.setActive(pdto.getSas().getActive());
		sasa.setApplied_date(pdto.getSas().getApplied_date());
//			sasa.setApproval(pdto.getSas().getApproval());
		sasa.setApproved_by(pdto.getSas().getApproved_by());
//			sasa.setApproved_date(pdto.getSas().getApplied_date());
//			sasa.setCancel_date(pdto.getSas().getCancel_date());
//			sasa.setCancel_remarks(pdto.getSas().getCancel_remarks());
		sasa.setModified_date(pdto.getSas().getModified_date());
		sasa.setCreated_username(pdto.getPap().getCreated_username());
		// sasa.setModified_username(pdto.getSas().getModified_username());
		sasa.setComments(pdto.getSas().getComments());
		sasa.setCounselor_id(pdto.getSas().getCounselor_id());
		sasa.setIs_approved(pdto.getSas().getIs_approved());
		sasa.setIs_verified(pdto.getSas().getIs_verified());
		sasa.setPrev_approved_amount(pdto.getSas().getPrev_approved_amount());
		sasa.setPre_approval_status(pdto.getSas().getPre_approval_status());
		sasa.setPre_approval_date(todaysDate);
		sasa.setStudent_id(pdto.getSas().getStudent_id());
		sasa.setUpdated_approved_amount_date(pdto.getSas().getUpdated_approved_amount_date());
		sasa.setVerified_amount(pdto.getSas().getVerified_amount());
		sasa.setVerified_by(pdto.getSas().getVerified_by());
		sasa.setVerified_date(pdto.getSas().getVerified_date());
		sasa.setApproved_amount(pdto.getSas().getApproved_amount());

		sasa.setYear1_amount(pdto.getSas().getYear1_amount());
		sasa.setYear2_amount(pdto.getSas().getYear2_amount());
		sasa.setYear3_amount(pdto.getSas().getYear3_amount());
		sasa.setYear4_amount(pdto.getSas().getYear4_amount());
		sasa.setYear5_amount(pdto.getSas().getYear5_amount());
		sasa.setYear6_amount(pdto.getSas().getYear6_amount());
		sasa.setYear7_amount(pdto.getSas().getYear7_amount());
		sasa.setYear8_amount(pdto.getSas().getYear8_amount());
		sasa.setYear9_amount(pdto.getSas().getYear9_amount());
		sasa.setYear10_amount(pdto.getSas().getYear10_amount());
		sasa.setYear11_amount(pdto.getSas().getYear11_amount());
		sasa.setYear12_amount(pdto.getSas().getYear12_amount());

		sas_repo.save(sasa);
		candidateWalkInService.updateLsqStatus(pre_adm_pro.getCandidate_id(),2);

		return sasa;
	}

	public PreAdmissionProcess get(Integer id) {
		return p_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PreAdmissionProcess Not Found:" + id));
	}

	public void delete(Integer id) {
		PreAdmissionProcess ay = p_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("PreAdmissionProcess Not Found:" + id));
		p_repo.delete(ay);
	}

	public List<HashMap<String, Object>> findAllCandidate() {
		return p_repo.findAllcandidateId();
	}

	public List<Map<String, Object>> findAllDetails(Integer candidate_id) {
		return p_repo.findAllCandidateDetails(candidate_id);
	}
	
	public List<Map<String, Object>> findAllDetailsPreAdmission1(Integer candidate_id) {
		return p_repo.findAllDetailsPreAdmission1(candidate_id);
	}

	public List<HashMap<String, Object>> listAll2(Integer cid) {
		return p_repo.listAll2(cid);
	}

	public void deactivatePreAdmissionProcess(Integer id) {

		p_repo.update(id);
		Candidate_Walkin existingCandidate = can_repo.getExistingCandidate(id);
		existingCandidate.setNpf_status(null);
		candidateWalkInService.updateLsqStatus(id,7);

	}

	public void activatePreAdmissionProcess(Integer id) {

		p_repo.update1(id);
	}

	public ResponseEntity<Object> getFeeDetails(Integer candidateId) {
		try {
			FeeTemplateDetailsForCandidate feeTemplateDetailsForCandidate = p_repo.getFeeTemplateDetails(candidateId);
			List<FeeSubAmountDetailsForCandidate> feeSubAmountDetailsForCandidates = p_repo
					.getFeeSubAmountDetails(candidateId);
			OtherFeeTemplateForStudentDTO uniformAndStationary = otherFeeDetailsRepository.getUniformFeeDetails(
					feeTemplateDetailsForCandidate.getSchoolId(), feeTemplateDetailsForCandidate.getAcYearId(),
					feeTemplateDetailsForCandidate.getProgramId(),
					feeTemplateDetailsForCandidate.getProgramSpecializationId());
			OtherFeeTemplateForStudentDTO addOn = otherFeeDetailsRepository.getAddOnProgramFeeDetailsByFeeTemplateId(
					feeTemplateDetailsForCandidate.getSchoolId(), feeTemplateDetailsForCandidate.getAcYearId(),
					feeTemplateDetailsForCandidate.getProgramId(), feeTemplateDetailsForCandidate.getFeeTemplateId());
			ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
					.getScholarshipDetailsByCandidateId(candidateId);
			FeeDetailsResponseForCandidate feeDetailsResponseForCandidate = new FeeDetailsResponseForCandidate();
			FeeSubAmountDetailsForCandidate campusFee = feeSubAmountDetailsForCandidates.stream()
					.filter(t -> t.getFeetype().trim().equals("Campus Development Fee")).findFirst().orElse(null);
			FeeSubAmountDetailsForCandidate registrationFee = feeSubAmountDetailsForCandidates.stream()
					.filter(t -> t.getFeetype().trim().equals("Registration Fee")).findFirst().orElse(null);
			FeeSubAmountDetailsForCandidate tutionFee = feeSubAmountDetailsForCandidates.stream()
					.filter(t -> t.getFeetype().trim().equals("Tuition Fee")).findFirst().orElse(null);
			FeeSubAmountDetailsForCandidate universityAndOtherFee = feeSubAmountDetailsForCandidates.stream()
					.filter(t -> t.getFeetype().trim().equals("University and Other Fees")).findFirst().orElse(null);

			Optional<PreAdmissionProcess> preadmissionData = Optional.of(p_repo.findByCandidateId(candidateId));
			FeeDetailsForCandidate getScholarShip = null;
			if (preadmissionData.get().getIs_scholarship() == true) {
				getScholarShip = getScholarShip(scholarshipApprovalStatus);
			}

			FeeDetailsForCandidate getRegistrationFees = getRegistrationFees(registrationFee);
			feeDetailsResponseForCandidate.setRegistrationFee(getRegistrationFees);
			FeeDetailsForCandidate getUniformAndStationaryFees = null;
			if (ObjectUtils.isNotEmpty(uniformAndStationary)) {
				getUniformAndStationaryFees = getUniformAndStationaryFees(uniformAndStationary);
				feeDetailsResponseForCandidate.setUniformFee(getUniformAndStationaryFees);
			}
			FeeDetailsForCandidate getCampusFees = getCampusFee(campusFee);
			feeDetailsResponseForCandidate.setCampusFee(getCampusFees);
			FeeDetailsForCandidate getCompositeFees = null;
			if (StringUtils.equals(feeTemplateDetailsForCandidate.getCurrenyType(), "INR")) {
				getCompositeFees = getCompositeFees(feeSubAmountDetailsForCandidates, addOn);
				feeDetailsResponseForCandidate.setCompositeFee(getCompositeFees);

			} else {
				getCompositeFees = getCompositeFeesForUsd(feeSubAmountDetailsForCandidates);
				feeDetailsResponseForCandidate.setCompositeFee(getCompositeFees);
				FeeDetailsForCandidate getAddOnFee = null;
				if (ObjectUtils.isNotEmpty(addOn)) {
					getAddOnFee = getAddOnFee(addOn);
					feeDetailsResponseForCandidate.setAddOnFee(getAddOnFee);
				}

			}

			if (ObjectUtils.isNotEmpty(getScholarShip)) {
				feeDetailsResponseForCandidate.setScholarShip(getScholarShip);
			}

			Integer totalYear1 = getCampusFees.getYear1() + getRegistrationFees.getYear1()
					+ getCompositeFees.getYear1();
			Integer totalYear2 = getCampusFees.getYear2() + getRegistrationFees.getYear2()
					+ getCompositeFees.getYear2();
			Integer totalYear3 = getCampusFees.getYear3() + getRegistrationFees.getYear3()
					+ getCompositeFees.getYear3();
			Integer totalYear4 = getCampusFees.getYear4() + getRegistrationFees.getYear4()
					+ getCompositeFees.getYear4();
			Integer totalYear5 = getCampusFees.getYear5() + getRegistrationFees.getYear5()
					+ getCompositeFees.getYear5();
			Integer totalYear6 = getCampusFees.getYear6() + getRegistrationFees.getYear6()
					+ getCompositeFees.getYear6();
			Integer totalYear7 = getCampusFees.getYear7() + getRegistrationFees.getYear7()
					+ getCompositeFees.getYear7();
			Integer totalYear8 = getCampusFees.getYear8() + getRegistrationFees.getYear8()
					+ getCompositeFees.getYear8();
			Integer totalYear9 = getCampusFees.getYear9() + getRegistrationFees.getYear9()
			+ getCompositeFees.getYear9();
			Integer totalYear10 = getCampusFees.getYear10() + getRegistrationFees.getYear10()
			+ getCompositeFees.getYear10();
			Integer totalYear11 = getCampusFees.getYear11() + getRegistrationFees.getYear11()
			+ getCompositeFees.getYear11();
			Integer totalYear12 = getCampusFees.getYear12() + getRegistrationFees.getYear12()
			+ getCompositeFees.getYear12();

			Integer uniformAndStationaryYear1 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear1())
							? getUniformAndStationaryFees.getYear1()
							: 0;
			Integer uniformAndStationaryYear2 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear2())
							? getUniformAndStationaryFees.getYear2()
							: 0;
			Integer uniformAndStationaryYear3 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear3())
							? getUniformAndStationaryFees.getYear3()
							: 0;
			Integer uniformAndStationaryYear4 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear4())
							? getUniformAndStationaryFees.getYear4()
							: 0;
			Integer uniformAndStationaryYear5 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear5())
							? getUniformAndStationaryFees.getYear5()
							: 0;
			Integer uniformAndStationaryYear6 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear6())
							? getUniformAndStationaryFees.getYear6()
							: 0;
			Integer uniformAndStationaryYear7 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear7())
							? getUniformAndStationaryFees.getYear7()
							: 0;
			Integer uniformAndStationaryYear8 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear8())
							? getUniformAndStationaryFees.getYear8()
							: 0;
			Integer uniformAndStationaryYear9 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear9())
							? getUniformAndStationaryFees.getYear9()
							: 0;
			Integer uniformAndStationaryYear10 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear10())
							? getUniformAndStationaryFees.getYear10()
							: 0;
			Integer uniformAndStationaryYear11 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear11())
							? getUniformAndStationaryFees.getYear11()
							: 0;
			Integer uniformAndStationaryYear12 = ObjectUtils.isNotEmpty(getUniformAndStationaryFees)
					&& ObjectUtils.isNotEmpty(getUniformAndStationaryFees.getYear12())
							? getUniformAndStationaryFees.getYear12()
							: 0;
			Integer scholarShipYear1 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear1()) ? getScholarShip.getYear1() : 0;
			Integer scholarShipYear2 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear2()) ? getScholarShip.getYear2() : 0;
			Integer scholarShipYear3 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear3()) ? getScholarShip.getYear3() : 0;
			Integer scholarShipYear4 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear4()) ? getScholarShip.getYear4() : 0;
			Integer scholarShipYear5 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear5()) ? getScholarShip.getYear5() : 0;
			Integer scholarShipYear6 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear6()) ? getScholarShip.getYear6() : 0;
			Integer scholarShipYear7 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear7()) ? getScholarShip.getYear7() : 0;
			Integer scholarShipYear8 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear8()) ? getScholarShip.getYear8() : 0;
			Integer scholarShipYear9 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear9()) ? getScholarShip.getYear9() : 0;
			Integer scholarShipYear10 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear10()) ? getScholarShip.getYear10() : 0;
			Integer scholarShipYear11 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear11()) ? getScholarShip.getYear11() : 0;
			Integer scholarShipYear12 = ObjectUtils.isNotEmpty(getScholarShip)
					&& ObjectUtils.isNotEmpty(getScholarShip.getYear12()) ? getScholarShip.getYear12() : 0;
			
			Integer grantTotalYear1 = 0;
			Integer grantTotalYear2 = 0;
			Integer grantTotalYear3 = 0;
			Integer grantTotalYear4 = 0;
			Integer grantTotalYear5 = 0;
			Integer grantTotalYear6 = 0;
			Integer grantTotalYear7 = 0;
			Integer grantTotalYear8 = 0;
			Integer grantTotalYear9 = 0;
			Integer grantTotalYear10 = 0;
			Integer grantTotalYear11 = 0;
			Integer grantTotalYear12 = 0;
			Integer finalGrantTotalYear1 = 0;

			Integer finalGrantTotalYear2 = 0;

			Integer finalGrantTotalYear3 = 0;

			Integer finalGrantTotalYear4 = 0;

			Integer finalGrantTotalYear5 = 0;

			Integer finalGrantTotalYear6 = 0;

			Integer finalGrantTotalYear7 = 0;

			Integer finalGrantTotalYear8 = 0;
			Integer finalGrantTotalYear9 = 0;
			Integer finalGrantTotalYear10 = 0;
			Integer finalGrantTotalYear11 = 0;
			Integer finalGrantTotalYear12 = 0;

			if (StringUtils.equals(feeTemplateDetailsForCandidate.getCurrenyType(), "INR")) {

				grantTotalYear1 = totalYear1 - scholarShipYear1;
				grantTotalYear2 = totalYear2 - scholarShipYear2;
				grantTotalYear3 = totalYear3 - scholarShipYear3;
				grantTotalYear4 = totalYear4 - scholarShipYear4;
				grantTotalYear5 = totalYear5 - scholarShipYear5;
				grantTotalYear6 = totalYear6 - scholarShipYear6;
				grantTotalYear7 = totalYear7 - scholarShipYear7;
				grantTotalYear8 = totalYear8 - scholarShipYear8;
				grantTotalYear9 = totalYear9 - scholarShipYear9;
				grantTotalYear10 = totalYear10 - scholarShipYear10;
				grantTotalYear11 = totalYear11 - scholarShipYear11;
				grantTotalYear12 = totalYear12 - scholarShipYear12;
				finalGrantTotalYear1 = totalYear1 - scholarShipYear1 + uniformAndStationaryYear1;
				finalGrantTotalYear2 = totalYear2 - scholarShipYear2 + uniformAndStationaryYear2;
				finalGrantTotalYear3 = totalYear3 - scholarShipYear3 + uniformAndStationaryYear3;
				finalGrantTotalYear4 = totalYear4 - scholarShipYear4 + uniformAndStationaryYear4;
				finalGrantTotalYear5 = totalYear5 - scholarShipYear5 + uniformAndStationaryYear5;
				finalGrantTotalYear6 = totalYear6 - scholarShipYear6 + uniformAndStationaryYear6;
				finalGrantTotalYear7 = totalYear7 - scholarShipYear7 + uniformAndStationaryYear7;
				finalGrantTotalYear8 = totalYear8 - scholarShipYear8 + uniformAndStationaryYear8;
				finalGrantTotalYear9 = totalYear9 - scholarShipYear9 + uniformAndStationaryYear9;
				finalGrantTotalYear10 = totalYear10 - scholarShipYear10 + uniformAndStationaryYear10;
				finalGrantTotalYear11 = totalYear11 - scholarShipYear11 + uniformAndStationaryYear11;
				finalGrantTotalYear12 = totalYear12 - scholarShipYear12 + uniformAndStationaryYear12;

			} else {
				grantTotalYear1 = totalYear1 - scholarShipYear1;
				grantTotalYear2 = totalYear2 - scholarShipYear2;
				grantTotalYear3 = totalYear3 - scholarShipYear3;
				grantTotalYear4 = totalYear4 - scholarShipYear4;
				grantTotalYear5 = totalYear5 - scholarShipYear5;
				grantTotalYear6 = totalYear6 - scholarShipYear6;
				grantTotalYear7 = totalYear7 - scholarShipYear7;
				grantTotalYear8 = totalYear8 - scholarShipYear8;
			}

			feeDetailsResponseForCandidate.setSem1Total(totalYear1);
			feeDetailsResponseForCandidate.setSem2Total(totalYear2);
			feeDetailsResponseForCandidate.setSem3Total(totalYear3);
			feeDetailsResponseForCandidate.setSem4Total(totalYear4);
			feeDetailsResponseForCandidate.setSem5Total(totalYear5);
			feeDetailsResponseForCandidate.setSem6Total(totalYear6);
			feeDetailsResponseForCandidate.setSem7Total(totalYear7);
			feeDetailsResponseForCandidate.setSem8Total(totalYear8);
			feeDetailsResponseForCandidate.setSem9Total(totalYear9);
			feeDetailsResponseForCandidate.setSem10Total(totalYear10);
			feeDetailsResponseForCandidate.setSem11Total(totalYear11);
			feeDetailsResponseForCandidate.setSem12Total(totalYear12);

			feeDetailsResponseForCandidate.setSem1GrantTotal(grantTotalYear1);
			feeDetailsResponseForCandidate.setSem2GrantTotal(grantTotalYear2);
			feeDetailsResponseForCandidate.setSem3GrantTotal(grantTotalYear3);
			feeDetailsResponseForCandidate.setSem4GrantTotal(grantTotalYear4);
			feeDetailsResponseForCandidate.setSem5GrantTotal(grantTotalYear5);
			feeDetailsResponseForCandidate.setSem6GrantTotal(grantTotalYear6);
			feeDetailsResponseForCandidate.setSem7GrantTotal(grantTotalYear7);
			feeDetailsResponseForCandidate.setSem8GrantTotal(grantTotalYear8);
			feeDetailsResponseForCandidate.setSem9GrantTotal(grantTotalYear9);
			feeDetailsResponseForCandidate.setSem10GrantTotal(grantTotalYear10);
			feeDetailsResponseForCandidate.setSem11GrantTotal(grantTotalYear11);
			feeDetailsResponseForCandidate.setSem12GrantTotal(grantTotalYear12);

			feeDetailsResponseForCandidate.setSem1FinalGrantTotal(finalGrantTotalYear1);
			feeDetailsResponseForCandidate.setSem2FinalGrantTotal(finalGrantTotalYear2);
			feeDetailsResponseForCandidate.setSem3FinalGrantTotal(finalGrantTotalYear3);
			feeDetailsResponseForCandidate.setSem4FinalGrantTotal(finalGrantTotalYear4);
			feeDetailsResponseForCandidate.setSem5FinalGrantTotal(finalGrantTotalYear5);
			feeDetailsResponseForCandidate.setSem6FinalGrantTotal(finalGrantTotalYear6);
			feeDetailsResponseForCandidate.setSem7FinalGrantTotal(finalGrantTotalYear7);
			feeDetailsResponseForCandidate.setSem8FinalGrantTotal(finalGrantTotalYear8);
			feeDetailsResponseForCandidate.setSem9FinalGrantTotal(finalGrantTotalYear9);
			feeDetailsResponseForCandidate.setSem10FinalGrantTotal(finalGrantTotalYear10);
			feeDetailsResponseForCandidate.setSem11FinalGrantTotal(finalGrantTotalYear11);
			feeDetailsResponseForCandidate.setSem12FinalGrantTotal(finalGrantTotalYear12);
			
			feeDetailsResponseForCandidate.setCurreny(feeTemplateDetailsForCandidate.getCurrenyType());

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", feeDetailsResponseForCandidate);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", e.getMessage());

		}
	}

	private FeeDetailsForCandidate getCompositeFeesForUsd(
			List<FeeSubAmountDetailsForCandidate> feeSubAmountDetailsForCandidates) {
		FeeDetailsForCandidate feeDetailsForCandidate = new FeeDetailsForCandidate();
		int year1 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear1() != null ? t.getYear1() : 0).sum();

		Integer totalYear1 = year1;

		int year2 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear2() != null ? t.getYear2() : 0).sum();

		Integer totalYear2 = year2;

		int year3 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear3() != null ? t.getYear3() : 0).sum();

		Integer totalYear3 = year3;

		int year4 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear4() != null ? t.getYear4() : 0).sum();

		Integer totalYear4 = year4;

		int year5 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear5() != null ? t.getYear5() : 0).sum();

		Integer totalYear5 = year5;

		int year6 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear6() != null ? t.getYear6() : 0).sum();

		Integer totalYear6 = year6;

		int year7 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear7() != null ? t.getYear7() : 0).sum();

		Integer totalYear7 = year7;

		int year8 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear8() != null ? t.getYear8() : 0).sum();

		Integer totalYear8 = year8;
		
		int year9 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear9() != null ? t.getYear9() : 0).sum();

		Integer totalYear9 = year9;
		int year10 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear10() != null ? t.getYear10() : 0).sum();

		Integer totalYear10 = year10;
		int year11= feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear11() != null ? t.getYear11() : 0).sum();

		Integer totalYear11 = year11;
		int year12 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear12() != null ? t.getYear12() : 0).sum();

		Integer totalYear12 = year12;

		feeDetailsForCandidate.setFeeType("Composite Fee");
		feeDetailsForCandidate.setYear1(totalYear1);
		feeDetailsForCandidate.setYear2(totalYear2);
		feeDetailsForCandidate.setYear3(totalYear3);
		feeDetailsForCandidate.setYear4(totalYear4);
		feeDetailsForCandidate.setYear5(totalYear5);
		feeDetailsForCandidate.setYear6(totalYear6);
		feeDetailsForCandidate.setYear7(totalYear7);
		feeDetailsForCandidate.setYear8(totalYear8);
		feeDetailsForCandidate.setYear9(totalYear9);
		feeDetailsForCandidate.setYear10(totalYear10);
		feeDetailsForCandidate.setYear11(totalYear11);
		feeDetailsForCandidate.setYear12(totalYear12);

		return feeDetailsForCandidate;

	}

	private FeeDetailsForCandidate getCompositeFees(
			List<FeeSubAmountDetailsForCandidate> feeSubAmountDetailsForCandidates,
			OtherFeeTemplateForStudentDTO addOn) {
		FeeDetailsForCandidate feeDetailsForCandidate = new FeeDetailsForCandidate();
		int year1 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear1() != null ? t.getYear1() : 0).sum();
		Integer addOnYear1 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem1())
				? addOn.getSem1()
				: 0);

		Integer totalYear1 = year1 + addOnYear1;

		int year2 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear2() != null ? t.getYear2() : 0).sum();
		Integer addOnYear2 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem2())
				? addOn.getSem2()
				: 0);

		Integer totalYear2 = year2 + addOnYear2;

		int year3 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear3() != null ? t.getYear3() : 0).sum();
		Integer addOnYear3 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem3())
				? addOn.getSem3()
				: 0);

		Integer totalYear3 = year3 + addOnYear3;

		int year4 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear4() != null ? t.getYear4() : 0).sum();
		Integer addOnYear4 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem4())
				? addOn.getSem4()
				: 0);

		Integer totalYear4 = year4 + addOnYear4;

		int year5 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear5() != null ? t.getYear5() : 0).sum();
		Integer addOnYear5 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem5())
				? addOn.getSem5()
				: 0);

		Integer totalYear5 = year5 + addOnYear5;

		int year6 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear6() != null ? t.getYear6() : 0).sum();
		Integer addOnYear6 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem6())
				? addOn.getSem6()
				: 0);

		Integer totalYear6 = year6 + addOnYear6;

		int year7 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear7() != null ? t.getYear7() : 0).sum();
		Integer addOnYear7 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem7())
				? addOn.getSem7()
				: 0);

		Integer totalYear7 = year7 + addOnYear7;

		int year8 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear8() != null ? t.getYear8() : 0).sum();
		Integer addOnYear8 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem8())
				? addOn.getSem8()
				: 0);

		Integer totalYear8 = year8 + addOnYear8;
		
		int year9 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear9() != null ? t.getYear9() : 0).sum();
		Integer addOnYear9 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem9())
				? addOn.getSem9()
				: 0);

		Integer totalYear9 = year9 + addOnYear9;
		int year10 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear10() != null ? t.getYear10() : 0).sum();
		Integer addOnYear10 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem10())
				? addOn.getSem10()
				: 0);

		Integer totalYear10 = year10 + addOnYear10;
		int year11 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear11() != null ? t.getYear11() : 0).sum();
		Integer addOnYear11 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem11())
				? addOn.getSem11()
				: 0);

		Integer totalYear11 = year11 + addOnYear11;
		
		int year12 = feeSubAmountDetailsForCandidates.stream()
				.filter(t -> !t.getFeetype().trim().equals("Campus Development Fee")
						&& !t.getFeetype().trim().equals("Registration Fee"))
				.mapToInt(t -> t.getYear12() != null ? t.getYear12() : 0).sum();
		Integer addOnYear12 = (int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem12())
				? addOn.getSem12()
				: 0);

		Integer totalYear12 = year12 + addOnYear12;

		if (totalYear1 == 0 && totalYear2 == 0 && totalYear3 == 0 && totalYear4 == 0 && totalYear5 == 0
				&& totalYear6 == 0 && totalYear7 == 0 && totalYear8 == 0 && totalYear9 == 0 && totalYear10 == 0 && totalYear11 == 0 && totalYear12 == 0) {
			return null;
		}

		feeDetailsForCandidate.setFeeType("Composite Fee");
		feeDetailsForCandidate.setYear1(totalYear1);
		feeDetailsForCandidate.setYear2(totalYear2);
		feeDetailsForCandidate.setYear3(totalYear3);
		feeDetailsForCandidate.setYear4(totalYear4);
		feeDetailsForCandidate.setYear5(totalYear5);
		feeDetailsForCandidate.setYear6(totalYear6);
		feeDetailsForCandidate.setYear7(totalYear7);
		feeDetailsForCandidate.setYear8(totalYear8);
		feeDetailsForCandidate.setYear9(totalYear9);
		feeDetailsForCandidate.setYear10(totalYear10);
		feeDetailsForCandidate.setYear11(totalYear11);
		feeDetailsForCandidate.setYear12(totalYear12);

		return feeDetailsForCandidate;

	}

	private FeeDetailsForCandidate getScholarShip(ScholarshipApprovalStatus scholarshipApprovalStatus) {
		FeeDetailsForCandidate feeDetailsForCandidate = new FeeDetailsForCandidate();

		feeDetailsForCandidate.setFeeType("Scholarship");
		feeDetailsForCandidate.setYear1((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear1_amount())
						? scholarshipApprovalStatus.getYear1_amount()
						: 0));
		feeDetailsForCandidate.setYear2((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear2_amount())
						? scholarshipApprovalStatus.getYear2_amount()
						: 0));
		feeDetailsForCandidate.setYear3((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear3_amount())
						? scholarshipApprovalStatus.getYear3_amount()
						: 0));
		feeDetailsForCandidate.setYear4((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear4_amount())
						? scholarshipApprovalStatus.getYear4_amount()
						: 0));
		feeDetailsForCandidate.setYear5((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear5_amount())
						? scholarshipApprovalStatus.getYear5_amount()
						: 0));
		feeDetailsForCandidate.setYear6((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear6_amount())
						? scholarshipApprovalStatus.getYear6_amount()
						: 0));
		feeDetailsForCandidate.setYear7((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear7_amount())
						? scholarshipApprovalStatus.getYear7_amount()
						: 0));
		feeDetailsForCandidate.setYear8((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear8_amount())
						? scholarshipApprovalStatus.getYear8_amount()
						: 0));
		feeDetailsForCandidate.setYear9((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear9_amount())
						? scholarshipApprovalStatus.getYear9_amount()
						: 0));
		feeDetailsForCandidate.setYear10((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear10_amount())
						? scholarshipApprovalStatus.getYear10_amount()
						: 0));
		feeDetailsForCandidate.setYear11((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear11_amount())
						? scholarshipApprovalStatus.getYear11_amount()
						: 0));
		feeDetailsForCandidate.setYear12((int) (ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
				&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.getYear12_amount())
						? scholarshipApprovalStatus.getYear12_amount()
						: 0));

		return feeDetailsForCandidate;

	}

	private FeeDetailsForCandidate getAddOnFee(OtherFeeTemplateForStudentDTO addOn) {

		FeeDetailsForCandidate feeDetailsForCandidate = null;
		if (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getFeeName())) {
			feeDetailsForCandidate = new FeeDetailsForCandidate();

			feeDetailsForCandidate.setFeeType(
					ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getFeeName()) ? addOn.getFeeName()
							: null);
			feeDetailsForCandidate.setYear1(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem1()) ? addOn.getSem1()
							: 0));
			feeDetailsForCandidate.setYear2(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem2()) ? addOn.getSem2()
							: 0));
			feeDetailsForCandidate.setYear3(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem3()) ? addOn.getSem3()
							: 0));
			feeDetailsForCandidate.setYear4(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem4()) ? addOn.getSem4()
							: 0));
			feeDetailsForCandidate.setYear5(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem5()) ? addOn.getSem5()
							: 0));
			feeDetailsForCandidate.setYear6(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem6()) ? addOn.getSem6()
							: 0));
			feeDetailsForCandidate.setYear7(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem7()) ? addOn.getSem7()
							: 0));
			feeDetailsForCandidate.setYear8(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem8()) ? addOn.getSem8()
							: 0));
			feeDetailsForCandidate.setYear9(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem9()) ? addOn.getSem9()
							: 0));
			feeDetailsForCandidate.setYear10(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem10()) ? addOn.getSem10()
							: 0));
			feeDetailsForCandidate.setYear11(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem11()) ? addOn.getSem11()
							: 0));
			feeDetailsForCandidate.setYear12(
					(int) (ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem12()) ? addOn.getSem12()
							: 0));
		}
		return feeDetailsForCandidate;

	}

	private FeeDetailsForCandidate getUniformAndStationaryFees(OtherFeeTemplateForStudentDTO uniformAndStationary) {
		FeeDetailsForCandidate feeDetailsForCandidate = null;
		if (ObjectUtils.isNotEmpty(uniformAndStationary) && ObjectUtils.isNotEmpty(uniformAndStationary.getFeeName())) {
			feeDetailsForCandidate = new FeeDetailsForCandidate();
			feeDetailsForCandidate.setFeeType(ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getFeeName()) ? uniformAndStationary.getFeeName()
							: null);
			feeDetailsForCandidate.setYear1((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem1()) ? uniformAndStationary.getSem1() : 0));
			feeDetailsForCandidate.setYear2((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem2()) ? uniformAndStationary.getSem2() : 0));
			feeDetailsForCandidate.setYear3((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem3()) ? uniformAndStationary.getSem3() : 0));
			feeDetailsForCandidate.setYear4((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem4()) ? uniformAndStationary.getSem4() : 0));
			feeDetailsForCandidate.setYear5((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem5()) ? uniformAndStationary.getSem5() : 0));
			feeDetailsForCandidate.setYear6((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem6()) ? uniformAndStationary.getSem6() : 0));
			feeDetailsForCandidate.setYear7((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem7()) ? uniformAndStationary.getSem7() : 0));
			feeDetailsForCandidate.setYear8((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem8()) ? uniformAndStationary.getSem8() : 0));
			feeDetailsForCandidate.setYear9((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem9()) ? uniformAndStationary.getSem9() : 0));
			feeDetailsForCandidate.setYear10((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem10()) ? uniformAndStationary.getSem10() : 0));
			feeDetailsForCandidate.setYear11((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem11()) ? uniformAndStationary.getSem11() : 0));
			feeDetailsForCandidate.setYear12((int) (ObjectUtils.isNotEmpty(uniformAndStationary)
					&& ObjectUtils.isNotEmpty(uniformAndStationary.getSem12()) ? uniformAndStationary.getSem12() : 0));

		}
		return feeDetailsForCandidate;

	}

	private FeeDetailsForCandidate getRegistrationFees(FeeSubAmountDetailsForCandidate registrationFee) {
		FeeDetailsForCandidate feeDetailsForCandidate = null;
		if (ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getFeetype())) {
			feeDetailsForCandidate = new FeeDetailsForCandidate();
			feeDetailsForCandidate.setFeeType(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getFeetype())
							? registrationFee.getFeetype()
							: null);
			feeDetailsForCandidate.setYear1(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear1())
							? registrationFee.getYear1()
							: 0);
			feeDetailsForCandidate.setYear2(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear2())
							? registrationFee.getYear2()
							: 0);
			feeDetailsForCandidate
					.setYear3(ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee)
							&& ObjectUtils.isNotEmpty(registrationFee.getYear3()) ? registrationFee.getYear3() : 0);
			feeDetailsForCandidate.setYear4(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear4())
							? registrationFee.getYear4()
							: 0);
			feeDetailsForCandidate.setYear5(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear5())
							? registrationFee.getYear5()
							: 0);
			feeDetailsForCandidate.setYear6(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear6())
							? registrationFee.getYear6()
							: 0);
			feeDetailsForCandidate.setYear7(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear7())
							? registrationFee.getYear7()
							: 0);
			feeDetailsForCandidate.setYear8(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear8())
							? registrationFee.getYear8()
							: 0);
			feeDetailsForCandidate.setYear9(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear9())
							? registrationFee.getYear9()
							: 0);
			feeDetailsForCandidate.setYear10(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear10())
							? registrationFee.getYear10()
							: 0);
			feeDetailsForCandidate.setYear11(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear11())
							? registrationFee.getYear11()
							: 0);
			feeDetailsForCandidate.setYear12(
					ObjectUtils.isNotEmpty(registrationFee) && ObjectUtils.isNotEmpty(registrationFee.getYear12())
							? registrationFee.getYear12()
							: 0);
		}
		return feeDetailsForCandidate;
	}

	private FeeDetailsForCandidate getCampusFee(FeeSubAmountDetailsForCandidate campusFee) {
		FeeDetailsForCandidate feeDetailsForCandidate = null;
		if (ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getFeetype())) {
			feeDetailsForCandidate = new FeeDetailsForCandidate();
			feeDetailsForCandidate
					.setFeeType(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getFeetype())
							? campusFee.getFeetype()
							: null);
			feeDetailsForCandidate
					.setYear1(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear1())
							? campusFee.getYear1()
							: 0);
			feeDetailsForCandidate
					.setYear2(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear2())
							? campusFee.getYear2()
							: 0);
			feeDetailsForCandidate
					.setYear3(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear3())
							? campusFee.getYear3()
							: 0);
			feeDetailsForCandidate
					.setYear4(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear4())
							? campusFee.getYear4()
							: 0);
			feeDetailsForCandidate
					.setYear5(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear5())
							? campusFee.getYear5()
							: 0);
			feeDetailsForCandidate
					.setYear6(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear6())
							? campusFee.getYear6()
							: 0);
			feeDetailsForCandidate
					.setYear7(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear7())
							? campusFee.getYear7()
							: 0);
			feeDetailsForCandidate
					.setYear8(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear8())
							? campusFee.getYear8()
							: 0);
			feeDetailsForCandidate
			.setYear9(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear9())
					? campusFee.getYear9()
					: 0);
			feeDetailsForCandidate
			.setYear10(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear10())
					? campusFee.getYear10()
					: 0);
			feeDetailsForCandidate
			.setYear11(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear11())
					? campusFee.getYear11()
					: 0);
			feeDetailsForCandidate
			.setYear12(ObjectUtils.isNotEmpty(campusFee) && ObjectUtils.isNotEmpty(campusFee.getYear12())
					? campusFee.getYear12()
					: 0);
		}
		return feeDetailsForCandidate;
	}

}
