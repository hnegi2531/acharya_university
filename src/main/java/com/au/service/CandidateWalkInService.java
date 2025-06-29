package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.amazonaws.services.s3.AmazonS3;
import com.au.controller.CandidateWalkinController;
import com.au.dto.CandidateWalkinLsqDto;
import com.au.dto.Candidate_walkinRequest;
import com.au.dto.LSQStatusDTO;
import com.au.dto.LSQStatusUpdateDTO;
import com.au.exception.ResourceNotFoundException;
import com.au.model.AttachmentSubCategory;
import com.au.model.Candidate_Walkin;
import com.au.model.PGApplicable;
import com.au.model.StudentAttachments;
import com.au.model.UserAuthentication;
import com.au.repository.Academic_year_repository;
import com.au.repository.ApplicantDetailsRepository;
import com.au.repository.AttachmentSubCategoryRepository;
import com.au.repository.CandidateWalkinRepository;
import com.au.repository.LeadAssignmentRepository;
import com.au.repository.PGApplicableRepository;
import com.au.repository.School_Repository;
import com.au.repository.StudentAttachmentsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.repository.UserRoleRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class CandidateWalkInService {

	Logger log = LoggerFactory.getLogger(CandidateWalkInService.class);

	@Autowired
	private CandidateWalkinRepository can_repo;
	
	
	@Autowired
	private JwtTokenService jwt_service;

	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;

	@Autowired
	ApplicantDetailsRepository ap_repo;

	@Autowired
	StudentAttachmentsRepository sa_repo;

	@Autowired
	PGApplicableRepository pg_repo;

	@Autowired
	AttachmentSubCategoryRepository attach_cat_repo;

	@Autowired
	private School_Repository sc_repo;

	@Autowired
	private Academic_year_repository ac_repo;

//	@Autowired
//	private JavaMailSender mailSender;
	
	  @Autowired
	    @Qualifier("mailSenderPrimary")  // ✅ Explicitly use the primary mail sender
	    private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@Autowired
	private UserRoleRepository user_role_repo;

	@Autowired
	private ResponseHandler response_handler;

	@Autowired
	private LeadAssignmentRepository lead_assignment_repo;

	private AmazonS3 s3client;

	private Logger logger = LoggerFactory.getLogger(AmazonClientService.class);


	public List<Candidate_Walkin> listAll() {
		return can_repo.findAll1();
	}

	public List<Candidate_Walkin> getCandidateWalkinDataLsqWithLeadId(String lead_id) {
		return can_repo.getCandidateWalkinDataLsqWithLeadId(lead_id);
	}

	public List<Candidate_Walkin> getCandidateWalkinDataLsqWithOpportunityId(String opportunity_id) {
		return can_repo.getCandidateWalkinDataLsqWithOpportunityId(opportunity_id);

	}

	public ResponseEntity<Object> listAll6() {
		HashMap<String, Object> hs = new HashMap<String, Object>();
		List<Candidate_Walkin> response = can_repo.findAll6();
		hs.put("candidate_walkin", response);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, hs);

	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		// String dynamicQuery1 = CONCAT(cw.ac_year_id, ' ',cw.candidate_name) LIKE
		// %:keyword%;
		// String dynamicQuery1 = column +"="+keyword +" and "+column+"="+ value;
		Page<Object> response1 = can_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> response = can_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

//	public ResponseEntity<Object> getAllData2(Pageable pageable,String column,Object value)
//			{
//		String dynamicQuery = column+"="+ value;
//		System.out.println("(((((((((((((((((((_______)))))))))))))))))))))  " +dynamicQuery);
//		Page<Object> response2 = can_repo.getRecords2(pageable, dynamicQuery);
//		return ResponseHandler.generateResponse1(true, HttpStatus.OK, response2);
//	} 

	public Candidate_Walkin save_Candidate_Walkin(Candidate_Walkin c) {
		List<Candidate_Walkin> candidate_list = listAll();
		if (candidate_list.stream().anyMatch(list -> c.getMobile_number().equals(list.getMobile_number()))) {
			throw new RuntimeException("Candidate Mobile Number is Already Exist");
		} else if (candidate_list.stream().anyMatch(list -> c.getCandidate_email().equals(list.getCandidate_email()))) {
			throw new RuntimeException("Candidate Email is Already Exist");
		} else {
			if (c.getApplication_no_npf() == null) {
				String school_short_name = sc_repo.getSchoolShortName(c.getSchool_id());
				Integer current_year = ac_repo.getCurrentYear(c.getAc_year_id());
				String applicationNoNpfWithoutCount = school_short_name + "/" + current_year + "/" + "DA";
				Integer maxApplicationNoNpf = can_repo.countOfCandidateWalkin(c.getAc_year_id(),applicationNoNpfWithoutCount); 
				if(maxApplicationNoNpf == null) {
					c.setApplication_no_npf(applicationNoNpfWithoutCount + 1);
				} else {
			        maxApplicationNoNpf++;
			        c.setApplication_no_npf(applicationNoNpfWithoutCount + maxApplicationNoNpf);
				}
				System.out.println("=====service " + c.getApplication_no_npf());
				return can_repo.save(c);
			} else {
				System.out.println("=====service CRM");
				return can_repo.save(c);
			}

		}
	}
	
	 Candidate_Walkin existingCandidate = null;
	 private static Integer lsqFlag =0;
	private static String lsqStatus="Paid";
	public ResponseEntity<Object> Candidate_WalkinForLsq(Candidate_Walkin c) throws Exception {

		 log.debug("Received request for Candidate Walkin: {}", c);

			if(c.getRemarks().equalsIgnoreCase("All documents verified.") || c.getRemarks().equalsIgnoreCase("Personal Details filled")) {
				lsqFlag =1;
				lsqStatus="Submitted";

			 } else {
				 lsqFlag =0;
				 lsqStatus="Paid";
			 }
		 
		// Handle the case where both lead_id and opportunity_id are provided
		if (c.getLead_id() != null && c.getOpportunity_id() != null) {
			if (can_repo.getcountOfLeadIdAndOpportunityId(c.getLead_id(), c.getOpportunity_id()) >= 1) {
				// If candidate already exists based on lead_id and opportunity_id, update the
				// existing candidate
				Integer candidateId = can_repo.getByCandidateId(c.getLead_id(), c.getOpportunity_id());
				Candidate_Walkin existingCandidate = can_repo.findById(candidateId)
						.orElseThrow(() -> new Exception("Candidate not found"));
				
				// Update only the fields that are provided in the request, leaving others
				// unchanged
				updateFieldIfPresent(c.getCounsellor_email(), email -> {
					UserAuthentication userDetailsData = userAuthenticationRepository.getUserDetailsData(email);
					if (userDetailsData == null) {
						 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Counsellor");
				    }
					existingCandidate.setCounselor_id(userDetailsData.getId());
					existingCandidate.setCounselor_name(userDetailsData.getUsername());
				
				});
			
				updateFieldIfPresent(c.getBlood_group(), existingCandidate::setBlood_group);

				updateFieldIfPresent(c.getCandidate_email(), existingCandidate::setCandidate_email);

				updateFieldIfPresent(c.getCandidate_sex(), existingCandidate::setCandidate_sex);

				updateFieldIfPresent(c.getCaste(), existingCandidate::setCaste);

				updateFieldIfPresent(c.getCategory(), existingCandidate::setCategory);

				updateFieldIfPresent(c.getCity_id(), existingCandidate::setCity_id);

				updateFieldIfPresent(c.getCountry_id(), existingCandidate::setCountry_id);

				updateFieldIfPresent(c.getDate_of_birth(), existingCandidate::setDate_of_birth);

				updateFieldIfPresent(c.getFather_annual_income(), existingCandidate::setFather_annual_income);

				updateFieldIfPresent(c.getFather_email(), existingCandidate::setFather_email);

				updateFieldIfPresent(c.getFather_mobile(), existingCandidate::setFather_mobile);

				updateFieldIfPresent(c.getFather_name(), existingCandidate::setFather_name);

				updateFieldIfPresent(c.getFather_occupation(), existingCandidate::setFather_occupation);

				updateFieldIfPresent(c.getFather_qualification(), existingCandidate::setFather_qualification);

				updateFieldIfPresent(c.getGpa_or_percentage(), existingCandidate::setGpa_or_percentage);

				updateFieldIfPresent(c.getGuardian_address(), existingCandidate::setGuardian_address);

				updateFieldIfPresent(c.getGuardian_city(), existingCandidate::setGuardian_city);

				updateFieldIfPresent(c.getGuardian_email(), existingCandidate::setGuardian_email);

				updateFieldIfPresent(c.getGuardian_mobile(), existingCandidate::setGuardian_mobile);

				updateFieldIfPresent(c.getGuardian_name(), existingCandidate::setGuardian_name);

				updateFieldIfPresent(c.getGuardian_occupation(), existingCandidate::setGuardian_occupation);

				updateFieldIfPresent(c.getGuardian_pincode(), existingCandidate::setGuardian_pincode);

				updateFieldIfPresent(c.getGuardian_relation_to_student(),
						existingCandidate::setGuardian_relation_to_student);

				updateFieldIfPresent(c.getIs_puc_result(), existingCandidate::setIs_puc_result);

				updateFieldIfPresent(c.getMarks_rank_obtain(), existingCandidate::setMarks_rank_obtain);

				updateFieldIfPresent(c.getMobile_number(), existingCandidate::setMobile_number);

				updateFieldIfPresent(c.getMother_annual_income(), existingCandidate::setMother_annual_income);

				updateFieldIfPresent(c.getMother_email(), existingCandidate::setMother_email);

				updateFieldIfPresent(c.getMother_mobile(), existingCandidate::setMother_mobile);

				updateFieldIfPresent(c.getMother_name(), existingCandidate::setMother_name);

				updateFieldIfPresent(c.getMother_occupation(), existingCandidate::setMother_occupation);

				updateFieldIfPresent(c.getMother_qualification(), existingCandidate::setMother_qualification);

				updateFieldIfPresent(c.getNationality(), existingCandidate::setNationality);

				updateFieldIfPresent(c.getPassport_number(), existingCandidate::setPassport_number);

				updateFieldIfPresent(c.getPassport_expiry_date(), existingCandidate::setPassport_expiry_date);

				updateFieldIfPresent(c.getPassport_issued_by(), existingCandidate::setPassport_issued_by);

				updateFieldIfPresent(c.getPermanant_adress1(), existingCandidate::setPermanant_adress1);

				updateFieldIfPresent(c.getPermanent_address(), existingCandidate::setPermanent_address);

				updateFieldIfPresent(c.getPermanent_city(), existingCandidate::setPermanent_city);

				updateFieldIfPresent(c.getPermanent_country(), existingCandidate::setPermanent_country);

				updateFieldIfPresent(c.getPermanent_pincode(), existingCandidate::setPermanent_pincode);

				updateFieldIfPresent(c.getPermanent_state(), existingCandidate::setPermanent_state);

				updateFieldIfPresent(c.getPlace_of_birth(), existingCandidate::setPlace_of_birth);

				updateFieldIfPresent(c.getPresent_address(), existingCandidate::setPresent_address);

				updateFieldIfPresent(c.getPresent_address1(), existingCandidate::setPresent_address1);

				updateFieldIfPresent(c.getPresent_city_id(), existingCandidate::setPresent_city_id);

				updateFieldIfPresent(c.getPresent_country(), existingCandidate::setPresent_country);

				updateFieldIfPresent(c.getPresent_pincode(), existingCandidate::setPresent_pincode);

				updateFieldIfPresent(c.getPresent_state(), existingCandidate::setPresent_state);

				updateFieldIfPresent(c.getPuc_board(), existingCandidate::setPuc_board);

				updateFieldIfPresent(c.getPuc_mode_of_study(), existingCandidate::setPuc_mode_of_study);

				updateFieldIfPresent(c.getPuc_percentage_grade(), existingCandidate::setPuc_percentage_grade);

				updateFieldIfPresent(c.getPuc_percentage_obtain(), existingCandidate::setPuc_percentage_obtain);

				updateFieldIfPresent(c.getPuc_registration_number(), existingCandidate::setPuc_registration_number);

				updateFieldIfPresent(c.getPuc_school_name(), existingCandidate::setPuc_school_name);

				updateFieldIfPresent(c.getPuc_subject_marks_obtain(), existingCandidate::setPuc_subject_marks_obtain);

				updateFieldIfPresent(c.getPuc_subject_max_marks(), existingCandidate::setPuc_subject_max_marks);

				updateFieldIfPresent(c.getPuc_subjects(), existingCandidate::setPuc_subjects);

				updateFieldIfPresent(c.getPuc_year_of_passing(), existingCandidate::setPuc_year_of_passing);

				updateFieldIfPresent(c.getReligion(), existingCandidate::setReligion);

				updateFieldIfPresent(c.getRemarks(), existingCandidate::setRemarks);

				updateFieldIfPresent(c.getResult_score(), existingCandidate::setResult_score);

				updateFieldIfPresent(c.getResult_status(), existingCandidate::setResult_status);

				updateFieldIfPresent(c.getSslc_board(), existingCandidate::setSslc_board);

				updateFieldIfPresent(c.getSslc_percentage_grade(), existingCandidate::setSslc_percentage_grade);

				updateFieldIfPresent(c.getSslc_registration_number(), existingCandidate::setSslc_registration_number);

				updateFieldIfPresent(c.getSslc_school_name(), existingCandidate::setSslc_school_name);

				updateFieldIfPresent(c.getSslc_year_of_passing(), existingCandidate::setSslc_year_of_passing);

				updateFieldIfPresent(c.getState_id(), existingCandidate::setState_id);

				updateFieldIfPresent(c.getCounsellor_email(), existingCandidate::setCounsellor_email);

				updateFieldIfPresent(c.getUg_board(), existingCandidate::setUg_board);

				updateFieldIfPresent(c.getUg_registration_number(), existingCandidate::setUg_registration_number);

				updateFieldIfPresent(c.getUg_school_name(), existingCandidate::setUg_school_name);

				updateFieldIfPresent(c.getUg_subject_marks_obtain(), existingCandidate::setUg_subject_marks_obtain);

				updateFieldIfPresent(c.getUg_subject_max_marks(), existingCandidate::setUg_subject_max_marks);

				updateFieldIfPresent(c.getUg_year_of_passing(), existingCandidate::setUg_year_of_passing);

				updateFieldIfPresent(c.getUg_percentage_grade(), existingCandidate::setUg_percentage_grade);

				updateFieldIfPresent(c.getAadhar(), existingCandidate::setAadhar);

				updateFieldIfPresent(c.getMarital_status(), existingCandidate::setMarital_status);

				updateFieldIfPresent(c.getWhatsapp_number(), existingCandidate::setWhatsapp_number);

				updateFieldIfPresent(c.getEntrance_exam_name(), existingCandidate::setEntrance_exam_name);

				updateFieldIfPresent(c.getEntrance_exam_date(), existingCandidate::setEntrance_exam_date);

				updateFieldIfPresent(c.getEntrance_exam_result(), existingCandidate::setEntrance_exam_result);

				updateFieldIfPresent(c.getRural_urban(), existingCandidate::setRural_urban);

				updateFieldIfPresent(c.getAlternate_number(), existingCandidate::setAlternate_number);

				updateFieldIfPresent(c.getIs_nri(), existingCandidate::setIs_nri);

				updateFieldIfPresent(c.getEntrance_exam_score(), existingCandidate::setEntrance_exam_score);

				updateFieldIfPresent(c.getAet_result(), existingCandidate::setAet_result);

				updateFieldIfPresent(c.getAet_date(), existingCandidate::setAet_date);

				updateFieldIfPresent(c.getRank_obtained(), existingCandidate::setRank_obtained);
				
				updateFieldIfPresent(c.getSource(), existingCandidate::setSource);
				updateFieldIfPresent(c.getCampaign_name(), existingCandidate::setCampaign_name);
				updateFieldIfPresent(c.getUtm_value(), existingCandidate::setUtm_value);
				
				
				existingCandidate.setApplication_status(lsqStatus);
				
				Candidate_Walkin savedCandidate = can_repo.save(existingCandidate); // Save the updated data
				if(lsqFlag == 1) {
					updateLsqStatus(savedCandidate.getCandidate_id(),1);
				}

				return ResponseHandler.generateResponse1(true, HttpStatus.OK, "Data created successfully");

			} else {
				// If no existing candidate, create a new candidate
				System.out.println("SSSSSSSSSS :"+lsqStatus);
				String counsellorEmail = c.getCounsellor_email();
				UserAuthentication userDetailsData = userAuthenticationRepository.getUserDetailsData(counsellorEmail);
				if (userDetailsData == null) {
					 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Counsellor");
			    }
				c.setCounselor_id(userDetailsData.getId());
				c.setCounselor_name(userDetailsData.getUsername());
				c.setApplication_status(lsqStatus);
				c.setActive(true);
				Candidate_Walkin savedCandidate = can_repo.save(c);
				if(lsqFlag == 1) {
					updateLsqStatus(savedCandidate.getCandidate_id(),1);
				}

				return ResponseHandler.generateResponse1(true, HttpStatus.OK, "Data created successfully");
			}
		}

		// Handle case when only lead_id is provided
		else if (c.getLead_id() != null) {

			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +c.getLead_id());
			if (can_repo.getcountOfLeadId(c.getLead_id()) >= 1) {
				// Update the existing candidate with only lead_id (when opportunity_id is not
				// provided)
				
				List<Integer> candidateId = can_repo.getByCandidateIdLeadId(c.getLead_id());
				candidateId.stream().forEach(candId ->{
				
				try {
					existingCandidate = can_repo.findById(candId)
							.orElseThrow(() -> new Exception("Candidate not found"));
				} catch (Exception e) {
				
					e.printStackTrace();
				}

				// Update only the fields that are provided in the request, leaving others
				// unchanged
				updateFieldIfPresent(c.getCounsellor_email(), email -> {
					UserAuthentication userDetailsData = userAuthenticationRepository.getUserDetailsData(email);
					if (userDetailsData == null) {
						 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Counsellor");
				    }
					existingCandidate.setCounselor_id(userDetailsData.getId());
					existingCandidate.setCounselor_name(userDetailsData.getUsername());
				});
				updateFieldIfPresent(c.getBlood_group(), existingCandidate::setBlood_group);

				updateFieldIfPresent(c.getCandidate_email(), existingCandidate::setCandidate_email);

				updateFieldIfPresent(c.getCandidate_sex(), existingCandidate::setCandidate_sex);

				updateFieldIfPresent(c.getCaste(), existingCandidate::setCaste);

				updateFieldIfPresent(c.getCategory(), existingCandidate::setCategory);

				updateFieldIfPresent(c.getCity_id(), existingCandidate::setCity_id);

				updateFieldIfPresent(c.getCountry_id(), existingCandidate::setCountry_id);

				updateFieldIfPresent(c.getDate_of_birth(), existingCandidate::setDate_of_birth);

				updateFieldIfPresent(c.getFather_annual_income(), existingCandidate::setFather_annual_income);

				updateFieldIfPresent(c.getFather_email(), existingCandidate::setFather_email);

				updateFieldIfPresent(c.getFather_mobile(), existingCandidate::setFather_mobile);

				updateFieldIfPresent(c.getFather_name(), existingCandidate::setFather_name);

				updateFieldIfPresent(c.getFather_occupation(), existingCandidate::setFather_occupation);

				updateFieldIfPresent(c.getFather_qualification(), existingCandidate::setFather_qualification);

				updateFieldIfPresent(c.getGpa_or_percentage(), existingCandidate::setGpa_or_percentage);

				updateFieldIfPresent(c.getGuardian_address(), existingCandidate::setGuardian_address);

				updateFieldIfPresent(c.getGuardian_city(), existingCandidate::setGuardian_city);

				updateFieldIfPresent(c.getGuardian_email(), existingCandidate::setGuardian_email);

				updateFieldIfPresent(c.getGuardian_mobile(), existingCandidate::setGuardian_mobile);

				updateFieldIfPresent(c.getGuardian_name(), existingCandidate::setGuardian_name);

				updateFieldIfPresent(c.getGuardian_occupation(), existingCandidate::setGuardian_occupation);

				updateFieldIfPresent(c.getGuardian_pincode(), existingCandidate::setGuardian_pincode);

				updateFieldIfPresent(c.getGuardian_relation_to_student(), existingCandidate::setGuardian_relation_to_student);

				updateFieldIfPresent(c.getIs_puc_result(), existingCandidate::setIs_puc_result);

				updateFieldIfPresent(c.getMarks_rank_obtain(), existingCandidate::setMarks_rank_obtain);

				updateFieldIfPresent(c.getMobile_number(), existingCandidate::setMobile_number);

				updateFieldIfPresent(c.getMother_annual_income(), existingCandidate::setMother_annual_income);

				updateFieldIfPresent(c.getMother_email(), existingCandidate::setMother_email);

				updateFieldIfPresent(c.getMother_mobile(), existingCandidate::setMother_mobile);

				updateFieldIfPresent(c.getMother_name(), existingCandidate::setMother_name);

				updateFieldIfPresent(c.getMother_occupation(), existingCandidate::setMother_occupation);

				updateFieldIfPresent(c.getMother_qualification(), existingCandidate::setMother_qualification);

				updateFieldIfPresent(c.getNationality(), existingCandidate::setNationality);

				updateFieldIfPresent(c.getPassport_number(), existingCandidate::setPassport_number);

				updateFieldIfPresent(c.getPassport_expiry_date(), existingCandidate::setPassport_expiry_date);

				updateFieldIfPresent(c.getPassport_issued_by(), existingCandidate::setPassport_issued_by);

				updateFieldIfPresent(c.getPermanant_adress1(), existingCandidate::setPermanant_adress1);

				updateFieldIfPresent(c.getPermanent_address(), existingCandidate::setPermanent_address);

				updateFieldIfPresent(c.getPermanent_city(), existingCandidate::setPermanent_city);

				updateFieldIfPresent(c.getPermanent_country(), existingCandidate::setPermanent_country);

				updateFieldIfPresent(c.getPermanent_pincode(), existingCandidate::setPermanent_pincode);

				updateFieldIfPresent(c.getPermanent_state(), existingCandidate::setPermanent_state);

				updateFieldIfPresent(c.getPlace_of_birth(), existingCandidate::setPlace_of_birth);

				updateFieldIfPresent(c.getPresent_address(), existingCandidate::setPresent_address);

				updateFieldIfPresent(c.getPresent_address1(), existingCandidate::setPresent_address1);

				updateFieldIfPresent(c.getPresent_city_id(), existingCandidate::setPresent_city_id);

				updateFieldIfPresent(c.getPresent_country(), existingCandidate::setPresent_country);

				updateFieldIfPresent(c.getPresent_pincode(), existingCandidate::setPresent_pincode);

				updateFieldIfPresent(c.getPresent_state(), existingCandidate::setPresent_state);

				updateFieldIfPresent(c.getPuc_board(), existingCandidate::setPuc_board);

				updateFieldIfPresent(c.getPuc_mode_of_study(), existingCandidate::setPuc_mode_of_study);

				updateFieldIfPresent(c.getPuc_percentage_grade(), existingCandidate::setPuc_percentage_grade);

				updateFieldIfPresent(c.getPuc_percentage_obtain(), existingCandidate::setPuc_percentage_obtain);

				updateFieldIfPresent(c.getPuc_registration_number(), existingCandidate::setPuc_registration_number);

				updateFieldIfPresent(c.getPuc_school_name(), existingCandidate::setPuc_school_name);

				updateFieldIfPresent(c.getPuc_subject_marks_obtain(), existingCandidate::setPuc_subject_marks_obtain);

				updateFieldIfPresent(c.getPuc_subject_max_marks(), existingCandidate::setPuc_subject_max_marks);

				updateFieldIfPresent(c.getPuc_subjects(), existingCandidate::setPuc_subjects);

				updateFieldIfPresent(c.getPuc_year_of_passing(), existingCandidate::setPuc_year_of_passing);

				updateFieldIfPresent(c.getReligion(), existingCandidate::setReligion);

				updateFieldIfPresent(c.getRemarks(), existingCandidate::setRemarks);

				updateFieldIfPresent(c.getResult_score(), existingCandidate::setResult_score);

				updateFieldIfPresent(c.getResult_status(), existingCandidate::setResult_status);

				updateFieldIfPresent(c.getSslc_board(), existingCandidate::setSslc_board);

				updateFieldIfPresent(c.getSslc_percentage_grade(), existingCandidate::setSslc_percentage_grade);

				updateFieldIfPresent(c.getSslc_registration_number(), existingCandidate::setSslc_registration_number);

				updateFieldIfPresent(c.getSslc_school_name(), existingCandidate::setSslc_school_name);

				updateFieldIfPresent(c.getSslc_year_of_passing(), existingCandidate::setSslc_year_of_passing);

				updateFieldIfPresent(c.getState_id(), existingCandidate::setState_id);

				updateFieldIfPresent(c.getCounsellor_email(), existingCandidate::setCounsellor_email);

				updateFieldIfPresent(c.getUg_board(), existingCandidate::setUg_board);

				updateFieldIfPresent(c.getUg_registration_number(), existingCandidate::setUg_registration_number);

				updateFieldIfPresent(c.getUg_school_name(), existingCandidate::setUg_school_name);

				updateFieldIfPresent(c.getUg_subject_marks_obtain(), existingCandidate::setUg_subject_marks_obtain);

				updateFieldIfPresent(c.getUg_subject_max_marks(), existingCandidate::setUg_subject_max_marks);

				updateFieldIfPresent(c.getUg_year_of_passing(), existingCandidate::setUg_year_of_passing);

				updateFieldIfPresent(c.getUg_percentage_grade(), existingCandidate::setUg_percentage_grade);

				updateFieldIfPresent(c.getAadhar(), existingCandidate::setAadhar);

				updateFieldIfPresent(c.getMarital_status(), existingCandidate::setMarital_status);

				updateFieldIfPresent(c.getWhatsapp_number(), existingCandidate::setWhatsapp_number);

				updateFieldIfPresent(c.getEntrance_exam_name(), existingCandidate::setEntrance_exam_name);

				updateFieldIfPresent(c.getEntrance_exam_date(), existingCandidate::setEntrance_exam_date);

				updateFieldIfPresent(c.getEntrance_exam_result(), existingCandidate::setEntrance_exam_result);

				updateFieldIfPresent(c.getRural_urban(), existingCandidate::setRural_urban);

				updateFieldIfPresent(c.getAlternate_number(), existingCandidate::setAlternate_number);

				updateFieldIfPresent(c.getIs_nri(), existingCandidate::setIs_nri);

				updateFieldIfPresent(c.getEntrance_exam_score(), existingCandidate::setEntrance_exam_score);

				updateFieldIfPresent(c.getAet_result(), existingCandidate::setAet_result);

				updateFieldIfPresent(c.getAet_date(), existingCandidate::setAet_date);

				updateFieldIfPresent(c.getRank_obtained(), existingCandidate::setRank_obtained);
				
				updateFieldIfPresent(c.getSource(), existingCandidate::setSource);
				updateFieldIfPresent(c.getCampaign_name(), existingCandidate::setCampaign_name);
				updateFieldIfPresent(c.getUtm_value(), existingCandidate::setUtm_value);

				// Save the updated data
				System.out.println("AAAAAAAAAAAAA "+lsqStatus);
				existingCandidate.setApplication_status(lsqStatus);
				Candidate_Walkin savedCandidate = can_repo.save(existingCandidate); // Update existing data
				if(lsqFlag == 1) {
					updateLsqStatus(savedCandidate.getCandidate_id(),1);
				}
				});
				return ResponseHandler.generateResponse1(true, HttpStatus.OK, "Data updated successfully");
			} else {
				return ResponseHandler.generateResponse1(false, HttpStatus.NOT_FOUND,
						"No candidate found for the provided Lead Id");
			}
		}
		// Default return in case no valid condition is met
		return ResponseHandler.generateResponse1(false, HttpStatus.BAD_REQUEST, "Invalid request!");
	}

	private <T> void updateFieldIfPresent(T fieldValue, Consumer<T> setter) {
	    if (fieldValue != null) {
	        setter.accept(fieldValue);
	    }
	}

	public Candidate_Walkin updateCandidateWalkin(Candidate_Walkin c) {
		return can_repo.save(c);

	}

	public void updateCounselorInLeadAssignment(Integer counselor_id, Integer candidate_id) {
		lead_assignment_repo.updateCounselorInLeadAssignment(counselor_id, candidate_id);
	}

	public Candidate_Walkin getcandidates(Candidate_walkinRequest cd) {

		Candidate_Walkin c1 = can_repo.save(cd.getCd());

		PGApplicable pg = new PGApplicable();
		c1 = can_repo.findByCandidateId(cd.getCd().getCandidate_id());

		pg.setCandidate_id(c1.getCandidate_id());
		pg.setPg_exam_passed_name(cd.getPgapp().getPg_exam_passed_name());
		pg.setPg_total_percentage(cd.getPgapp().getPg_total_percentage());
		pg.setUg_degree(cd.getPgapp().getUg_degree());
		pg.setUg_board(cd.getPgapp().getUg_board());
		pg.setStd_id(cd.getPgapp().getStd_id());
		pg.setSubject_studied_lang(cd.getPgapp().getSubject_studied_lang());
		pg.setUniversity(cd.getPgapp().getUniversity());
		pg.setYear_1(cd.getPgapp().getYear_1());
		pg.setYear_2(cd.getPgapp().getYear_2());
		pg.setYear_3(cd.getPgapp().getYear_3());
		pg.setYear_4(cd.getPgapp().getYear_4());
		pg.setYear_of_passing(cd.getPgapp().getYear_of_passing());
		pg.setAuid(cd.getPgapp().getAuid());
		pg.setCreated_by(cd.getPgapp().getCreated_by());
		pg.setModified_by(cd.getPgapp().getModified_by());
		pg.setModified_Date(cd.getPgapp().getModified_Date());
		pg.setRemarks(cd.getPgapp().getRemarks());
		pg_repo.save(pg);

		Collection<String> str1 = cd.getStu_attach().getAttachments_file_path1().values();
//		System.out.println("=======================================" + str1);

		/*
		 * HashMap map = cd.getStu_attach().getEducational_attach(); Set<Integer> keys =
		 * map.keySet(); Collection<String> values = map.values();
		 */
		cd.getStu_attach().getAttachments_file_path1().entrySet().forEach(p -> {

			StudentAttachments stu_attach = new StudentAttachments();
			Candidate_Walkin c2 = can_repo.findByCandidateId(cd.getCd().getCandidate_id());

			AttachmentSubCategory asub = attach_cat_repo.getAttachCategory(p.getKey());

			// try {
			stu_attach.setCandidate_id(c2.getCandidate_id());
			stu_attach.setAttachments_subcategory_id(asub.getAttachments_subcategory_id());

			stu_attach.setEducational_attach1(p.getKey());

			stu_attach.setAttachments_file_path(p.getValue());

			// stu_attach.setAttachments_file_name(p.getKey());

			stu_attach.setAttachments_file_name(p.getKey() + ".pdf");

//				stu_attach.setPersonal_attach(cd.getStu_attach().getPersonal_attach());
			// stu_attach.setQualification_attach(cd.getStu_attach().getQualification_attach());
//				stu_attach.setStudent_id(cd.getStu_attach().getStudent_id());
			sa_repo.save(stu_attach);

		});

		return c1;
	}

	public Candidate_Walkin get(Integer id) {
		return can_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate_Walkin Not Found:" + id));
	}

//	public void delete(Integer id) {
//		Candidate_Walkin ay = can_repo.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("Candidate_Walkin Not Found:" + id));
//		can_repo.updateCandidateWakin(ay);
//	}

	public void delete(Integer id) {
		Candidate_Walkin ay = can_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate_Walkin Not Found:" + id));
		can_repo.updateCandidateWakin(id);
	}

	public void delete1(Integer id) {
		Candidate_Walkin ay = can_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Candidate_Walkin Not Found:" + id));
		can_repo.updateCandidateWakin1(id);
	}

	public ResponseEntity<Object> fetchNonIndianDataFromCandidateWalkin1(Pageable pageable, Object keyword) {
		Page<Object> response1 = can_repo.fetchNonIndianDataFromCandidateWalkinSearchData(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> fetchNonIndianDataFromCandidateWalkin2(Pageable pageable, Object keyword, Integer user_id) {
		Page<Object> response1 = can_repo.fetchNonIndianDataFromCandidateWalkinSortData(pageable, keyword, user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> fetchNonIndianDataFromCandidateWalkin3(Pageable pageable) {
		Page<Object> response = can_repo.fetchNonIndianDataFromCandidateWalkinSearchData1(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> fetchNonIndianDataFromCandidateWalkin4(Pageable pageable, Integer user_id) {
		Page<Object> response = can_repo.fetchNonIndianDataFromCandidateWalkinSortData1(pageable, user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<HashMap<String, Object>> listAll2(Integer cid) {
		return can_repo.listAll2(cid);
	}

	public Object sendMailForStudentOffer(String pdf_content, Integer candidate_id) throws Exception {
		List<HashMap<String, Object>> candidate_details = can_repo.getCandidateDetailsByCandidateId(candidate_id);
		String candidate_email = candidate_details.get(0).get("candidate_email").toString();
		// ByteArrayOutputStream outputStream = null;

		Document doc = Jsoup.parse(pdf_content, "UTF-8");
		doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		try (FileOutputStream os = new FileOutputStream("C:\\Offer and Salary Breakup\\Candidate_Offer.pdf")) {
			ITextRenderer renderer = new ITextRenderer();
			SharedContext cntxt = renderer.getSharedContext();
			cntxt.setPrint(true);
			cntxt.setInteractive(false);
			// renderer.getFontResolver().addFont(getClass().getClassLoader().getResource("fonts/PRISTINA.ttf").toString(),
			// true);
			String baseUrl = FileSystems.getDefault().getPath("C:\\Offer and Salary Breakup").toUri().toURL()
					.toString();
			renderer.setDocumentFromString(doc.html(), baseUrl);
			renderer.layout();
			renderer.createPDF(os);
			System.out.println("done");

			// now write the PDF content to the output stream
			// outputStream = new ByteArrayOutputStream();
			// generatePDFFromHTML(outputStream, pdf_content, candidate_id);

			// byte[] bytes = outputStream.toByteArray();
			// construct the pdf body part
			// DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			// pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName("Candidate_Offer - " + candidate_id + ".pdf");

			String contents = "Dear Mr." + "<br/>" + "<br/>"
					+ "Congratulations on your offer from Acharya Institutes ! We are delighted to offer you a position with us.!!<br/>"
					+ "Please find attached your detailed offer letter. Kindly acknowledge and confirm your acceptance.<br/>"
					// + "Looking forward for your reversion.<br/>" + "<br/>" + "Click on the below
					// link to confirm your acceptance for reporting on " + reportOn + ".<br/>" +
					// "<br/>"
					// + "<a href= "+url_domain+" + "+"/"+offer_id+" style='background-color:
					// #4A57A9;color:white;text-decoration:none;padding:6px'>Accept</a>" + "<br/>" +
					// "<br/>" + "<br/>" + "--<br/>" + "Divya<br/>"
					+ "Manager - ERP<br/>" + "Acharya Institutes<br/>" + "Bangalore" + "<br/> " + "<br/> "
					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";

			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(contents);

			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);

			String subject = "Candidate Offer letter - " + candidate_id;

			File file = new File("C:\\Offer and Salary Breakup\\Candidate_Offer.pdf");
			MimeMessage message = mailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
				helper.setTo(candidate_email);
				helper.setSubject(subject);
				helper.setText(contents, true);
				helper.addAttachment("Candidate_Offer.pdf", file);
			} catch (MessagingException e) {
				throw new MailParseException(e);
			}

			mailSender.send(message);
			System.out.println("PDF GENERATED...");
			System.out.println("Mail Send...");
			file.delete();
			// response_handler.delete(0, ".pdf");
			// response_handler.sendMailsWithAttachment(null, pdf_content, pdf_content,
			// null, stu_email);
			return null;
		}

	}

	public void updateLeadStatus(Integer candidate_id, String lead_status) {
		can_repo.updateLeadStatus(candidate_id, lead_status);

	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword1(Pageable pageable, Object keyword) {
		Page<Object> response1 = can_repo.getAllDataFilteredByKeyword1(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> getAllSortedData1(Pageable pageable) {
		Page<Object> response = can_repo.getAllSortedData1(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}


	public void updateLsqStatus(Integer candidateId, int status) {

       log.debug("Update LSQ STATUS Candidate id(1) : {}", status);
       
		Candidate_Walkin candidate_Walkin= can_repo.getByCandidateId(candidateId);
        if(ObjectUtils.isNotEmpty(candidate_Walkin) && ObjectUtils.isNotEmpty(candidate_Walkin.getOpportunity_id())) {
        	String offerStatus=getOfferStatus(candidate_Walkin,status);
        	System.out.println("Update LSQ STATUS Candidate id "+candidateId+" status "+offerStatus);
        	  log.debug("Update LSQ STATUS Candidate id(2) : {}", status);
        	  log.debug("check LSQ STATUS(OFFER) Candidate id(2) : {}", offerStatus);
        	if(StringUtils.isNotEmpty(offerStatus)) {
        	LSQStatusUpdateDTO lsqStatusUpdateDTO=new LSQStatusUpdateDTO();
        	lsqStatusUpdateDTO.setProspectOpportunityId(candidate_Walkin.getOpportunity_id());
        	lsqStatusUpdateDTO.setOpportunityNote("Updating Stage");
            List<LSQStatusDTO> lsqStatusDTOs=new ArrayList<>();
            LSQStatusDTO lsqStatusDTO=new LSQStatusDTO();
            lsqStatusDTO.setSchemaName("mx_Custom_2");
            lsqStatusDTO.setValue(offerStatus);
            lsqStatusDTOs.add(lsqStatusDTO);
        	lsqStatusUpdateDTO.setFields(lsqStatusDTOs);
        	
        	
        	   log.debug(" LSQ STATUS DTO payload(1) : {}", lsqStatusUpdateDTO.getOpportunityNote());
        	   log.debug(" LSQ STATUS DTO payload(1) : {}", lsqStatusUpdateDTO.getProspectOpportunityId());
        	   log.debug(" LSQ STATUS DTO payload(1) : {}", lsqStatusUpdateDTO.getFields().toString());
        	    
        	 String url = "https://api-in21.leadsquared.com/v2/OpportunityManagement.svc/Update?accessKey=u$rf0116cbfffa617de778de29050a8565a&secretKey=871be21cf012083516bbf13555f20cbaa3b2091a";
        	  log.debug("Received request for URL: {}", url);
      
             RestTemplate restTemplate = new RestTemplate();
             HttpHeaders headers = new HttpHeaders();
             headers.set("Content-Type", "application/json");
             
             HttpEntity<LSQStatusUpdateDTO> requestEntity = new HttpEntity<>(lsqStatusUpdateDTO, headers);
             
             try {
                 ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                 
               
                 log.debug("Update LSQ STATUS payload (2): {}",  requestEntity);

                 if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.print("Success");
                 } else {
                	    System.out.print("Failure");
                 }
             } catch (Exception e) {
            	  System.out.print("Exception: "+e.getMessage());  }
        	
        	}
        	
        }
		 
		
	}

	private String getOfferStatus(Candidate_Walkin candidate_Walkin, int status) {
		
			switch (status) {
			case 1:
				return "Offer Not Created";
			case 2:
				return "Offer Created Not Sent";
			case 3:
				return "Offer Sent Acceptance Pending";
			case 4:
				return "Offer Accepted Payment pending";
			case 5:
				return "Paid AUID Not Created";
			case 6:
				return "AUID Created";
			case 7:
				return "Offer Not Created";	
				
			
			
		}
		return null;
	}

	public ResponseEntity<Object> updateCandidate_WalkinForLsq(CandidateWalkinLsqDto dto)
			throws JsonParseException, JsonMappingException, IOException {

		Candidate_Walkin candidateWalkin = can_repo.getCandidateWalkinDetails(dto.getLead_id(),
				dto.getOpportunity_id());
		candidateWalkin.setCounsellor_email(dto.getCounsellor_email());
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + dto.getCounsellor_email());
		UserAuthentication userDetailsData = userAuthenticationRepository.getUserDetailsData(dto.getCounsellor_email());

		candidateWalkin.setCounselor_id(userDetailsData.getId());
		candidateWalkin.setCounselor_name(userDetailsData.getUsername());

		System.out.println("(updateCandidate_WalkinForLsq) Candidate Walkin details..." + candidateWalkin);
		can_repo.save(candidateWalkin);
		System.out.println("Data updated successfully(updateCandidate_WalkinForLsq)..." + candidateWalkin);
		return ResponseHandler.generateResponse1(true, HttpStatus.OK, "Data updated successfully");
	}
	
	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		Page<Object> response1 = can_repo.getCandidateDetailsSearchData(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAll2(Pageable pageable, Object keyword, Integer user_id) {
		Page<Object> response1 = can_repo.getCandidateDetailsSortData(pageable, keyword, user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAll3(Pageable pageable) {
		Page<Object> response = can_repo.getCandidateDetailsSearchData1(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> listAll4(Pageable pageable, Integer user_id) {
		Page<Object> response = can_repo.getCandidateDetailsSortData1(pageable, user_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> candidateWalkinForLsqApplicationStatus(@Valid Candidate_Walkin c) {
		
		log.info("candidateWalkinForLsqApplicationStatus : " +  c.toString());
		
		try {
		List<Candidate_Walkin> candidate_Walkins = can_repo.getAllByLeadId(c.getLead_id());
		if (ObjectUtils.isEmpty(candidate_Walkins) || candidate_Walkins == null) {
			return ResponseHandler.generateResponse(false, HttpStatus.NOT_FOUND, "Candidate not found", null);
		}
		
			for (Candidate_Walkin candidate_Walkin : candidate_Walkins) {

				candidate_Walkin.setApplication_status("Submitted");
				candidate_Walkin.setBlood_group(ObjectUtils.isNotEmpty(c.getBlood_group()) ? c.getBlood_group() : null);
				candidate_Walkin.setCandidate_email(
						ObjectUtils.isNotEmpty(c.getCandidate_email()) ? c.getCandidate_email() : null);

				candidate_Walkin
						.setCandidate_sex(ObjectUtils.isNotEmpty(c.getCandidate_sex()) ? c.getCandidate_sex() : null);

				candidate_Walkin.setCaste(ObjectUtils.isNotEmpty(c.getCaste()) ? c.getCaste() : null);

				candidate_Walkin.setCategory(ObjectUtils.isNotEmpty(c.getCategory()) ? c.getCategory() : null);

				candidate_Walkin.setCity_id(c.getCity_id() != null ? c.getCity_id() : null);

				candidate_Walkin.setCountry_id(c.getCountry_id() != null ? c.getCountry_id() : null);

				candidate_Walkin.setDate_of_birth(c.getDate_of_birth() != null ? c.getDate_of_birth() : null);

				candidate_Walkin.setFather_annual_income(
						c.getFather_annual_income() != null ? c.getFather_annual_income() : null);

				candidate_Walkin
						.setFather_email(ObjectUtils.isNotEmpty(c.getFather_email()) ? c.getFather_email() : null);

				candidate_Walkin
						.setFather_mobile(ObjectUtils.isNotEmpty(c.getFather_mobile()) ? c.getFather_mobile() : null);

				candidate_Walkin.setFather_name(ObjectUtils.isNotEmpty(c.getFather_name()) ? c.getFather_name() : null);

				candidate_Walkin.setFather_occupation(
						ObjectUtils.isNotEmpty(c.getFather_occupation()) ? c.getFather_occupation() : null);

				candidate_Walkin.setFather_qualification(
						ObjectUtils.isNotEmpty(c.getFather_qualification()) ? c.getFather_qualification() : null);

				candidate_Walkin
						.setGpa_or_percentage(c.getGpa_or_percentage() != null ? c.getGpa_or_percentage() : null);

				candidate_Walkin.setGuardian_address(
						ObjectUtils.isNotEmpty(c.getGuardian_address()) ? c.getGuardian_address() : null);

				candidate_Walkin.setGuardian_city(c.getGuardian_city() != null ? c.getGuardian_city() : null);

				candidate_Walkin.setGuardian_email(
						ObjectUtils.isNotEmpty(c.getGuardian_email()) ? c.getGuardian_email() : null);

				candidate_Walkin.setGuardian_mobile(
						ObjectUtils.isNotEmpty(c.getGuardian_mobile()) ? c.getGuardian_mobile() : null);

				candidate_Walkin
						.setGuardian_name(ObjectUtils.isNotEmpty(c.getGuardian_name()) ? c.getGuardian_name() : null);

				candidate_Walkin.setGuardian_occupation(
						ObjectUtils.isNotEmpty(c.getGuardian_occupation()) ? c.getGuardian_occupation() : null);

				candidate_Walkin.setGuardian_pincode(
						ObjectUtils.isNotEmpty(c.getGuardian_pincode()) ? c.getGuardian_pincode() : null);

				candidate_Walkin
						.setGuardian_relation_to_student(ObjectUtils.isNotEmpty(c.getGuardian_relation_to_student())
								? c.getGuardian_relation_to_student()
								: null);

				candidate_Walkin.setIs_puc_result(c.getIs_puc_result() != null ? c.getIs_puc_result() : null);

				candidate_Walkin
						.setMobile_number(ObjectUtils.isNotEmpty(c.getMobile_number()) ? c.getMobile_number() : null);

				candidate_Walkin.setMother_annual_income(
						c.getMother_annual_income() != null ? c.getMother_annual_income() : null);

				candidate_Walkin
						.setMother_email(ObjectUtils.isNotEmpty(c.getMother_email()) ? c.getMother_email() : null);

				candidate_Walkin
						.setMother_mobile(ObjectUtils.isNotEmpty(c.getMother_mobile()) ? c.getMother_mobile() : null);

				candidate_Walkin.setMother_name(ObjectUtils.isNotEmpty(c.getMother_name()) ? c.getMother_name() : null);

				candidate_Walkin.setMother_occupation(
						ObjectUtils.isNotEmpty(c.getMother_occupation()) ? c.getMother_occupation() : null);

				candidate_Walkin.setMother_qualification(
						ObjectUtils.isNotEmpty(c.getMother_qualification()) ? c.getMother_qualification() : null);

				candidate_Walkin.setNationality(c.getNationality() != null ? c.getNationality() : null);

				candidate_Walkin.setPassport_number(
						ObjectUtils.isNotEmpty(c.getPassport_number()) ? c.getPassport_number() : null);

				candidate_Walkin.setPassport_expiry_date(
						ObjectUtils.isNotEmpty(c.getPassport_expiry_date()) ? c.getPassport_expiry_date() : null);

				candidate_Walkin.setPassport_issued_by(
						ObjectUtils.isNotEmpty(c.getPassport_issued_by()) ? c.getPassport_issued_by() : null);

				candidate_Walkin.setPermanant_adress1(
						ObjectUtils.isNotEmpty(c.getPermanant_adress1()) ? c.getPermanant_adress1() : null);

				candidate_Walkin.setPermanent_address(
						ObjectUtils.isNotEmpty(c.getPermanent_address()) ? c.getPermanent_address() : null);

				candidate_Walkin.setPermanent_city(c.getPermanent_city() != null ? c.getPermanent_city() : null);

				candidate_Walkin
						.setPermanent_country(c.getPermanent_country() != null ? c.getPermanent_country() : null);

				candidate_Walkin.setPermanent_pincode(
						ObjectUtils.isNotEmpty(c.getPermanent_pincode()) ? c.getPermanent_pincode() : null);

				candidate_Walkin.setPermanent_state(c.getPermanent_state() != null ? c.getPermanent_state() : null);

				candidate_Walkin.setPlace_of_birth(
						ObjectUtils.isNotEmpty(c.getPlace_of_birth()) ? c.getPlace_of_birth() : null);

				candidate_Walkin.setPresent_address(
						ObjectUtils.isNotEmpty(c.getPresent_address()) ? c.getPresent_address() : null);

				candidate_Walkin.setPresent_address1(
						ObjectUtils.isNotEmpty(c.getPresent_address1()) ? c.getPresent_address1() : null);

				candidate_Walkin.setPresent_city_id(c.getPresent_city_id() != null ? c.getPresent_city_id() : null);

				candidate_Walkin.setPresent_country(c.getPresent_country() != null ? c.getPresent_country() : null);

				candidate_Walkin.setPresent_pincode(
						ObjectUtils.isNotEmpty(c.getPresent_pincode()) ? c.getPresent_pincode() : null);

				candidate_Walkin.setPresent_state(c.getPresent_state() != null ? c.getPresent_state() : null);

				candidate_Walkin.setPuc_board(ObjectUtils.isNotEmpty(c.getPuc_board()) ? c.getPuc_board() : null);

				candidate_Walkin.setPuc_mode_of_study(
						ObjectUtils.isNotEmpty(c.getPuc_mode_of_study()) ? c.getPuc_mode_of_study() : null);

				candidate_Walkin.setPuc_percentage_grade(
						ObjectUtils.isNotEmpty(c.getPuc_percentage_grade()) ? c.getPuc_percentage_grade() : null);

				candidate_Walkin.setPuc_registration_number(
						ObjectUtils.isNotEmpty(c.getPuc_registration_number()) ? c.getPuc_registration_number() : null);

				candidate_Walkin.setPuc_school_name(
						ObjectUtils.isNotEmpty(c.getPuc_school_name()) ? c.getPuc_school_name() : null);

				candidate_Walkin
						.setPuc_subjects(ObjectUtils.isNotEmpty(c.getPuc_subjects()) ? c.getPuc_subjects() : null);

				candidate_Walkin
						.setPuc_year_of_passing(c.getPuc_year_of_passing() != null ? c.getPuc_year_of_passing() : null);

				candidate_Walkin.setReligion(ObjectUtils.isNotEmpty(c.getReligion()) ? c.getReligion() : null);

				candidate_Walkin.setRemarks(ObjectUtils.isNotEmpty(c.getRemarks()) ? c.getRemarks() : null);

				candidate_Walkin.setResult_score(c.getResult_score() != null ? c.getResult_score() : null);

				candidate_Walkin
						.setResult_status(ObjectUtils.isNotEmpty(c.getResult_status()) ? c.getResult_status() : null);

				candidate_Walkin.setSslc_board(ObjectUtils.isNotEmpty(c.getSslc_board()) ? c.getSslc_board() : null);

				candidate_Walkin.setSslc_percentage_grade(
						ObjectUtils.isNotEmpty(c.getSslc_percentage_grade()) ? c.getSslc_percentage_grade() : null);

				candidate_Walkin.setSslc_registration_number(
						ObjectUtils.isNotEmpty(c.getSslc_registration_number()) ? c.getSslc_registration_number()
								: null);

				candidate_Walkin.setSslc_school_name(
						ObjectUtils.isNotEmpty(c.getSslc_school_name()) ? c.getSslc_school_name() : null);

				candidate_Walkin.setSslc_year_of_passing(
						c.getSslc_year_of_passing() != null ? c.getSslc_year_of_passing() : null);

				candidate_Walkin.setState_id(c.getState_id() != null ? c.getState_id() : null);

				candidate_Walkin.setCounsellor_email(
						ObjectUtils.isNotEmpty(c.getCounsellor_email()) ? c.getCounsellor_email() : null);

				candidate_Walkin.setPuc_percentage_obtain(
						(float) (c.getPuc_percentage_obtain() != 0.0 ? c.getPuc_percentage_obtain() : 0.0));

				candidate_Walkin.setMarks_rank_obtain(
						(float) (c.getMarks_rank_obtain() != 0.0 ? c.getMarks_rank_obtain() : 0.0));

				candidate_Walkin.setPuc_subject_marks_obtain(
						(float) (c.getPuc_subject_marks_obtain() != 0.0 ? c.getPuc_subject_marks_obtain() : 0.0));

				candidate_Walkin.setPuc_subject_max_marks(
						(float) (c.getPuc_subject_max_marks() != 0.0 ? c.getPuc_subject_max_marks() : 0.0));

				can_repo.save(candidate_Walkin);
				System.out.println("Data updated successfully(After else statement)" + candidate_Walkin);

			}
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", "Candidate Saved Successfully");
		} catch (Exception e) {
			log.info("Error occurred while saving candidateWalkin For Lsq Application: " +  e.getMessage() + c.toString(), e);
			log.info("candidateWalkin For Lsq Application Data: " + c.toString());
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public void callLeadSquaredApiForUpdateAuid(Integer candidate_id, String auid) {
		 Candidate_Walkin candidate_Walkin= can_repo.getByCandidateId(candidate_id);
	      System.out.println("callLeadSquaredApiForUpdateAuid CandidateId "+candidate_id+" Auid "+auid);
		String url = "https://api-in21.leadsquared.com/v2/LeadManagement.svc/Lead.Capture?accessKey=u$rf0116cbfffa617de778de29050a8565a&secretKey=871be21cf012083516bbf13555f20cbaa3b2091a";
         RestTemplate restTemplate = new RestTemplate();
         HttpHeaders headers = new HttpHeaders();
         headers.set("Content-Type", "application/json");
         
         List<Map<String, String>> payload = new ArrayList<>();

         Map<String, String> attribute1 = new HashMap<>();
         attribute1.put("Attribute", "mx_AU_ID");
         attribute1.put("Value", auid);
         payload.add(attribute1);

         Map<String, String> attribute2 = new HashMap<>();
         attribute2.put("Attribute", "ProspectID");
         attribute2.put("Value", candidate_Walkin.getLead_id());
         payload.add(attribute2);

         Map<String, String> attribute3 = new HashMap<>();
         attribute3.put("Attribute", "SearchBy");
         attribute3.put("Value", "ProspectID");
         payload.add(attribute3);

        
         System.out.println("callLeadSquaredApiForUpdateAuid Payload "+payload);
 		

         HttpEntity<List<Map<String, String>>> requestEntity = new HttpEntity<>(payload, headers);
         
         try {
             ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
             System.out.println("callLeadSquaredApiForUpdateAuid response "+response.getBody());
      		
             if (response.getStatusCode().is2xxSuccessful()) {
                System.out.print("Success");
             } else {
            	    System.out.print("Failure");
             }
        
		
         }  catch (Exception e) {
       	  System.out.print("Exception: "+e.getMessage());  }
     	
   	}
   	
   

}