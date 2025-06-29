package com.au.service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.applicationdiscovery.model.ResourceNotFoundException;
import com.au.dto.JwtDetails;
import com.au.event.StudentDueEvent;
import com.au.model.Academic_year;
import com.au.model.Program;
import com.au.model.Readmission;
import com.au.model.ReportingStudents;
import com.au.model.Roles;
import com.au.model.Student_Details;
import com.au.model.UserAuthentication;
import com.au.model.UserRole;
import com.au.repository.Academic_year_repository;
import com.au.repository.ApplicantDetailsRepository;
import com.au.repository.FeeTemplateRepository;
import com.au.repository.PGApplicableRepository;
import com.au.repository.ProgramRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.ReadmissionAmountPaidRepository;
import com.au.repository.ReadmissionRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.RolesRepository;
import com.au.repository.School_Repository;
import com.au.repository.StdEntranceExamRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentTranscriptSubmissionRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.repository.UserRoleRepository;
import com.au.response.ResponseHandler;

@Service
public class ReadmissionService {
	
	@Autowired
	private ReadmissionRepository re_repo;
	
	@Autowired
	private ReadmissionAmountPaidRepository re_amount_repo;
	
	@Autowired
	private FeeTemplateRepository ft_repo;
	
	@Autowired
	private StudentDetailsRepository stu_repo;
	
	@Autowired
	private Academic_year_repository ac_repo;

	@Autowired
	private ProgramSpecilizationRepository ps_repo;
	
	@Autowired
	private ProgramRepository program_repo;
	
	@Autowired
	private School_Repository sc_repo;
	
	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	
	@Autowired
	private ApplicantDetailsRepository a_repo;

	@Autowired
	private PGApplicableRepository pg_repo;

	@Autowired
	private StdEntranceExamRepository std_en_repo;

	@Autowired
	private StudentTranscriptSubmissionRepository sts_repo;
	
	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	private Logger logger = LoggerFactory.getLogger(ReadmissionService.class);
	
	
	public ResponseEntity<Object> createReadmission(Readmission read, JwtDetails jwtDetails) throws Exception {
		
		try {

				Student_Details studentData = stu_repo.getOne(read.getOldStudentId());
				read.setTotalAmount(read.getTotalAmount());
				read.setBalance(read.getBalance());
				read.setType("Re-admission");
				Student_Details newAuidForReadmission = createNewAuidForReadmission(studentData,read.getAcYearId(),read, jwtDetails);

				stu_repo.updateStudentTranscriptSubmission(read.getOldStudentId(),newAuidForReadmission.getStudent_id());
				stu_repo.updateApplicantDetails(read.getOldStudentId(),newAuidForReadmission.getStudent_id());
				stu_repo.updatePGApplicable(read.getOldStudentId(),newAuidForReadmission.getStudent_id());
				stu_repo.updateStdEntranceExam(read.getOldStudentId(),newAuidForReadmission.getStudent_id());
//				stu_repo.updateReportingStudents(read.getOldStudentId(),newAuidForReadmission.getStudent_id());
				stu_repo.updateStdReportingStudentsHistory(read.getOldStudentId(),newAuidForReadmission.getStudent_id());
				stu_repo.updateStudentMarks(read.getOldStudentId(),newAuidForReadmission.getStudent_id());
				stu_repo.updateProctorStudentAssignment(read.getOldStudentId(),newAuidForReadmission.getStudent_id());
				

			 re_repo.save(read);	
			   StudentDueEvent studDue = new StudentDueEvent(null, null, newAuidForReadmission.getStudent_id(), null);
				applicationEventPublisher.publishEvent(studDue);
			 
	} catch (Exception e) {
		return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, 
				"FAILURE", e.getMessage());

	}
		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", read);
		
	}
	
	Session session;
	public ResponseEntity<Object> checkDues(Integer semOrYear, Integer studentId) {
//		String query = "select * from StudentDues where " + "s" + semOrYear + "due";
		
//		String field = "s" + semOrYear + "due";
//		String hqlQuery = "from StudentDues where " + field + " = :value";
//
//		Query query = session.createQuery(hqlQuery);
//		query.setParameter("value", 1);  // Set the value you are searching for
//		List<StudentDues> results = query.list();
		
		
		Float checkDuesClearOrNot = 0f;
		for(int i=1 ;i<=semOrYear;i++) {
			checkDuesClearOrNot = checkDuesClearOrNot + re_repo.checkDuesClearOrNot(i, studentId);
		}
		if(ObjectUtils.isEmpty(checkDuesClearOrNot) || checkDuesClearOrNot == 0) {
			return ResponseHandler.generateResponse(true, HttpStatus.OK, 
					"No Dues.");

		} else {
			return ResponseHandler.generateResponse(true, HttpStatus.PAYMENT_REQUIRED, 
			"Please clear all the dues till the selected year/sem to apply for re-admission.",null);
		}

	}

	private Student_Details createNewAuidForReadmission(Student_Details studentData, Integer newAcYearId, Readmission read, JwtDetails jwtDetails) {
		
		Student_Details readmissionStudentData = new Student_Details();
		
			Optional<Program> program = program_repo.findById(studentData.getProgram_id());
			String program_code_for_auid = program.get().getProgram_code();
			if (program_code_for_auid == null) {
				throw new RuntimeException("Program Code IS NULL");
			}

			Academic_year academicYearsDetails = ac_repo.findByAcademicId(newAcYearId);
			if (academicYearsDetails == null) {
				throw new RuntimeException("Academic Year IS NULL");
			}

			String ac_year_id = academicYearsDetails.getCurrent_year().toString().substring(2, 4);
			String specialization_auid = ps_repo.getProgramAuid(studentData.getProgram_specialization_id());
			if (specialization_auid == null) {
				throw new RuntimeException("Specialization Auid Format IS NULL");
			}

			Integer count_for_auid;
//			Student_Details last_student_details = stu_repo.getLastStudentDetailsByProgramSpecializationId(
//					newAcYearId, studentData.getProgram_id(),
//					studentData.getProgram_specialization_id());
			Integer lastAuidCount=stu_repo.getLastStudentDetailsAuidCount(
					newAcYearId, studentData.getProgram_id(),
					studentData.getProgram_specialization_id());
			if (lastAuidCount != null) {
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  " + lastAuidCount);
//				count_for_auid = Integer.parseInt(last_student_details.getAuid().substring(9)) + 1;
				count_for_auid =lastAuidCount+1;
			} else {
				count_for_auid = 1;
			}

			String schoolShortName = sc_repo.getSchoolShortName(studentData.getSchool_id());
			String auid_formattedStrr = String.format("%03d", count_for_auid);
			String auid_format = schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr;

			String thisYear = String.valueOf(Year.now());
			String userName = null;
//			if (thisYear.equalsIgnoreCase("2024")) {
//				studentData.setAuid(studentData.getAuid());
//				userName = studentData.getAuid();
//			} else {
			readmissionStudentData.setAuid(schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr);
				userName = schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr;
//			}

			Integer master_code_count = stu_repo.maxStudentMasterCode();
			String formattedStrr = String.format("%04d", master_code_count + 1);
			readmissionStudentData.setStudent_master_code("ASC" + formattedStrr);
			String acharya_email = null;

//			if (thisYear.equalsIgnoreCase("2024")) {
//				acharya_email = studentData.getAcharya_email();
//			} else {
				acharya_email = studentData.getEmail_preferred_name().toLowerCase() + ac_year_id
						+ specialization_auid.toLowerCase() + "@acharya.ac.in";
//			}

		readmissionStudentData.setAcharya_email(acharya_email);
		readmissionStudentData.setAdhar_number(studentData.getAdhar_number());
		readmissionStudentData.setFirstname(studentData.getFirstname());
		readmissionStudentData.setLastname(studentData.getLastname());
		readmissionStudentData.setStudent_name(studentData.getStudent_name());
		readmissionStudentData.setCandidate_sex(studentData.getCandidate_sex());
		readmissionStudentData.setNationality(studentData.getNationality());
		readmissionStudentData.setPhoto(studentData.getPhoto());
		readmissionStudentData.setMobile(studentData.getMobile());
		readmissionStudentData.setDateofbirth(studentData.getDateofbirth());
		readmissionStudentData.setFather_name(studentData.getFather_name());
		readmissionStudentData.setMother_name(studentData.getMother_name());
		readmissionStudentData.setParents_mobile(studentData.getParents_mobile());
		readmissionStudentData.setParents_email(studentData.getParents_email());
		readmissionStudentData.setCurrent_address(studentData.getCurrent_address());
		readmissionStudentData.setPermanent_address(studentData.getPermanent_address());
		readmissionStudentData.setGuardian_name(studentData.getGuardian_name());
		readmissionStudentData.setGuardian_phone(studentData.getGuardian_phone());
		readmissionStudentData.setBlood_group(studentData.getBlood_group());
		readmissionStudentData.setReligion(studentData.getReligion());
		readmissionStudentData.setCaste(studentData.getCaste());
		readmissionStudentData.setSurname(studentData.getSurname());
		readmissionStudentData.setPermanant_country(studentData.getPermanant_country());
		readmissionStudentData.setPermanant_pincode(studentData.getPermanant_pincode());
		readmissionStudentData.setPermanant_city(studentData.getPermanant_city());
		readmissionStudentData.setPermanant_adress1(studentData.getPermanant_adress1());
		readmissionStudentData.setLocal_email(studentData.getLocal_email());
		readmissionStudentData.setLocal_phone(studentData.getLocal_phone());
		readmissionStudentData.setLocal_mobile(studentData.getLocal_mobile());
		readmissionStudentData.setLocal_state(studentData.getLocal_state());
		readmissionStudentData.setLocal_country(studentData.getLocal_country());
		readmissionStudentData.setLocal_pincode(studentData.getLocal_pincode());
		readmissionStudentData.setLocal_city(studentData.getLocal_city());
		readmissionStudentData.setLocal_adress1(studentData.getLocal_adress1());
		readmissionStudentData.setCurrent_city(studentData.getCurrent_city());
		readmissionStudentData.setCurrent_email(studentData.getCurrent_email());
		readmissionStudentData.setCurrent_phone(studentData.getCurrent_phone());
		readmissionStudentData.setCurrent_mobile(studentData.getCurrent_mobile());
		readmissionStudentData.setCurrent_state(studentData.getCurrent_state());
		readmissionStudentData.setCurrent_country(studentData.getCurrent_country());
		readmissionStudentData.setCurrent_pincode(studentData.getCurrent_pincode());
		readmissionStudentData.setCurrent_adress1(studentData.getCurrent_adress1());
		readmissionStudentData.setPermanant_state(studentData.getPermanant_state());
		readmissionStudentData.setPermanant_mobile(studentData.getPermanant_mobile());
		readmissionStudentData.setPermanant_phone(studentData.getPermanant_phone());
		readmissionStudentData.setPermanant_email(studentData.getPermanant_email());
		readmissionStudentData.setAccount_holder_name(studentData.getAccount_holder_name());
		readmissionStudentData.setBank_name(studentData.getBank_name());
		readmissionStudentData.setAccount_number(studentData.getAccount_number());
		readmissionStudentData.setBank_branch(studentData.getBank_branch());
		readmissionStudentData.setIfsc_code(studentData.getIfsc_code());
		readmissionStudentData.setP_city(studentData.getP_city());
		readmissionStudentData.setC_city(studentData.getC_city());
		readmissionStudentData.setL_city(studentData.getL_city());
		readmissionStudentData.setFather_occupation(studentData.getFather_occupation());
		readmissionStudentData.setMother_occupation(studentData.getMother_occupation());
		readmissionStudentData.setFather_income(studentData.getFather_income());
		readmissionStudentData.setMother_income(studentData.getMother_income());
		readmissionStudentData.setFather_email(studentData.getFather_email());
		readmissionStudentData.setMother_email(studentData.getMother_email());
		readmissionStudentData.setFather_mobile(studentData.getFather_mobile());
		readmissionStudentData.setMother_mobile(studentData.getMother_mobile());
		readmissionStudentData.setUsn(studentData.getUsn());
		readmissionStudentData.setCandidate_id(studentData.getCandidate_id());
		readmissionStudentData.setDateofjoining(studentData.getDateofjoining());
		readmissionStudentData.setSchool_id(studentData.getSchool_id());
		readmissionStudentData.setCreated_date(studentData.getCreated_date());
		readmissionStudentData.setModified_date(studentData.getModified_date());
		readmissionStudentData.setCreated_by(studentData.getCreated_by());
		readmissionStudentData.setModified_by(studentData.getModified_by());
		readmissionStudentData.setActive(true);
		readmissionStudentData.setAc_year_id(newAcYearId);
		readmissionStudentData.setFee_admission_category_id(studentData.getFee_admission_category_id());
		readmissionStudentData.setProgram_id(studentData.getProgram_id());
		readmissionStudentData.setProgram_specialization_id(studentData.getProgram_specialization_id());
		readmissionStudentData.setFee_template_id(studentData.getFee_template_id());
		readmissionStudentData.setAllotment_number(studentData.getAllotment_number());
		readmissionStudentData.setVisitor_id(studentData.getVisitor_id());
		readmissionStudentData.setLateral_sem(studentData.getLateral_sem());
		readmissionStudentData.setDeassign_status(studentData.getDeassign_status());
		readmissionStudentData.setCourse_approver_status(studentData.getCourse_approver_status());
		readmissionStudentData.setDate_of_admission(studentData.getDate_of_admission());
		readmissionStudentData.setJoining_year(studentData.getJoining_year());
		readmissionStudentData.setJoining_sem(studentData.getJoining_sem());
		readmissionStudentData.setStudent_email(studentData.getStudent_email());
		readmissionStudentData.setScholarship_status(studentData.getScholarship_status());
		readmissionStudentData.setEntrance_test_no(studentData.getEntrance_test_no());
		readmissionStudentData.setRank1(studentData.getRank1());
		readmissionStudentData.setOrder_no_date(studentData.getOrder_no_date());
		readmissionStudentData.setCategory_reserved(studentData.getCategory_reserved());
		readmissionStudentData.setCategory_alloted(studentData.getCategory_alloted());
		readmissionStudentData.setRural_urban(studentData.getRural_urban());
		readmissionStudentData.setSpecial_category(studentData.getSpecial_category());
		readmissionStudentData.setKarnataka_medium(studentData.getKarnataka_medium());
		readmissionStudentData.setBoard_admission_status(studentData.getBoard_admission_status());
		readmissionStudentData.setEntranct_test_type(studentData.getEntranct_test_type());
		readmissionStudentData.setBoard_admission_date(studentData.getBoard_admission_date());
		readmissionStudentData.setPhoto_upload_status(studentData.getPhoto_upload_status());
		readmissionStudentData.setRe_admission_status(1);
		readmissionStudentData.setProctor_assign_status(studentData.getProctor_assign_status());
		readmissionStudentData.setOld_student_id(studentData.getStudent_id());
		readmissionStudentData.setIdcard_ac_status(studentData.getIdcard_ac_status());
		readmissionStudentData.setPassed_status(studentData.getPassed_status());
		readmissionStudentData.setOld_std_id_readmn(studentData.getStudent_id());
		readmissionStudentData.setCreated_username(studentData.getCreated_username());
		readmissionStudentData.setModified_username(studentData.getModified_username());
		readmissionStudentData.setPassport_no(studentData.getPassport_no());
		readmissionStudentData.setVisa_no(studentData.getVisa_no());
		readmissionStudentData.setStudent_image_path(studentData.getStudent_image_path());
		readmissionStudentData.setProgram_assignment_id(studentData.getProgram_assignment_id());
		readmissionStudentData.setBoard_university_id(studentData.getBoard_university_id());
		readmissionStudentData.setMother_qualification(studentData.getMother_qualification());
		readmissionStudentData.setFather_qualification(studentData.getFather_qualification());
		readmissionStudentData.setCaste_category(studentData.getCaste_category());
		readmissionStudentData.setOld_auid_format(studentData.getOld_auid_format());
		readmissionStudentData.setEmail_preferred_name(studentData.getEmail_preferred_name());
		readmissionStudentData.setId_barcode_generated(studentData.getId_barcode_generated());
		readmissionStudentData.setLaptop_issued_status(studentData.getLaptop_issued_status());
		readmissionStudentData.setLaptop_issued_date(studentData.getLaptop_issued_date());
		readmissionStudentData.setId_card_issued_year(studentData.getId_card_issued_year());

		stu_repo.setCandidateIdToNullForReadmission(studentData.getStudent_id());
//		stu_repo.setAuidToNullForReadmission(studentData.getStudent_id());
		stu_repo.deactivateStudentIdForReadmission(studentData.getStudent_id());
		stu_repo.deactivateReportingStudentForReadmission(studentData.getStudent_id());
		
		readmissionStudentData=stu_repo.save(readmissionStudentData);

		 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String passwordEncoder1 = passwordEncoder.encode("acharya1234");
			UserAuthentication creating_student_user_details = new UserAuthentication();
			creating_student_user_details.setUsername(auid_format);
			creating_student_user_details.setPassword(passwordEncoder1);
			creating_student_user_details.setEmail(acharya_email);
			creating_student_user_details.setUsertype("Student");
			creating_student_user_details.setCreated_by(jwtDetails.getUserId());
			creating_student_user_details.setCreated_username(jwtDetails.getUserName());
			creating_student_user_details.setActive(true);
			userAuthenticationRepository.save(creating_student_user_details);

			Roles roles = rolesRepository.findRoleByRoleName("Student");
			if (roles != null) {
				UserRole userRole = new UserRole();
				userRole.setId(creating_student_user_details.getId());
				userRole.setRole_id(roles.getRole_id());
				userRole.setCreated_by(jwtDetails.getUserId());
				userRole.setCreated_username(jwtDetails.getUserName());
				userRole.setActive(true);
				userRoleRepository.save(userRole);
			} else {
				userAuthenticationRepository.deleteById(creating_student_user_details.getId());
				throw new RuntimeException("Role Name Student Not Found");
			}
		
			ReportingStudents rs1 = new ReportingStudents();
			rs1.setStudent_id(readmissionStudentData.getStudent_id());
			rs1.setActive(true);
			rs1.setCreated_by(jwtDetails.getUserId());
			rs1.setCreated_username(jwtDetails.getUserName());
			
			String programType = program_repo.getProgramType(readmissionStudentData.getProgram_assignment_id());
			if(programType.equalsIgnoreCase("SEMESTER")) {
				if(read.getSemOrYear() == 1) {
					rs1.setCurrent_sem(read.getSemOrYear());
					rs1.setCurrent_year(read.getSemOrYear());
				} else {
					rs1.setCurrent_sem(read.getSemOrYear());
					rs1.setCurrent_year(read.getSemOrYear() / 2);
				}
			} else {
				rs1.setCurrent_sem(0);
				rs1.setCurrent_year(read.getSemOrYear());
			}
			
			rs1.setEligible_reported_status(1);
			rs1.setDistinct_status(true);
//			rs1.setPrevious_sem(app.getRs().getPrevious_sem());
//			rs1.setPrevious_year(app.getRs().getPrevious_year());
//			rs1.setRemarks(app.getRs().getRemarks());
//			rs1.setReported_ac_year_id(app.getRs().getReported_ac_year_id());
//			rs1.setSection_id(app.getRs().getSection_id());
//			rs1.setYear_back_status(app.getRs().getYear_back_status());
			reportingStudentsRepository.save(rs1);
			
			userAuthenticationRepository.deavtivateByEmail(studentData.getAcharya_email());
			read.setNewAuid(readmissionStudentData.getAuid());
			read.setNewStudentId(readmissionStudentData.getStudent_id());
			
			return readmissionStudentData;
		
	}

	public List<Readmission> listAll() {
		return re_repo.findAll1();
	}

	public Readmission get(Integer readmissionId) {
		return re_repo.findById(readmissionId)
				.orElseThrow(() -> new ResourceNotFoundException("Readmission Not Found:" + readmissionId));
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {

		Page<Object> profile_research_filtered_response = re_repo.getAllDataFilteredByKeyword(pageable, keyword);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> profile_research_response = re_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, profile_research_response);
	}

	public Readmission updateReadmission(Readmission pr) {
		return re_repo.save(pr);
	}

	public void delete(Integer readmission) {
		Readmission sir = re_repo.findById(readmission)
				.orElseThrow(() -> new ResourceNotFoundException("Readmission Not Found:" + readmission));
		re_repo.updateReadmission(readmission);
	}

	public void delete1(Integer readmission) {
		Readmission sir = re_repo.findById(readmission)
				.orElseThrow(() -> new ResourceNotFoundException("Readmission Not Found:" + readmission));
		re_repo.updateReadmission1(readmission);
	}

//	public ResponseEntity<Object> createReadmission(Readmission read, JwtDetails jwtDetails) throws Exception {
//		
//		try {
////		Float checkDuesClearOrNot = re_repo.checkDuesClearOrNot(read.getSemOrYear(), read.getStudentId());
////		System.out.println("((((((((((((((((( : "+read.getStudentId());
////System.out.println("((((((((((((((((( : "+checkDuesClearOrNot);
////		if(checkDuesClearOrNot == 0) {
////			if(read.getSemOrYear() % 2 == 1) {
//				Student_Details studentData = stu_repo.getOne(read.getStudentId());
////				Integer semOrYearFee = ft_repo.getsemOrYearFeeForReadmission(read.getSemOrYear(), read.getFeeTemplateId());
//				read.setTotalAmount(read.getTotalAmount());
//				read.setBalance(read.getBalance());
//				read.setType("Re-admission");
//				Student_Details newAuidForReadmission = createNewAuidForReadmission(studentData, jwtDetails);
//				stu_repo.deactivateStudentIdForReadmission(read.getStudentId());
//				stu_repo.updateStudentTranscriptSubmission(read.getStudentId(),newAuidForReadmission.getStudent_id());
//				stu_repo.updateApplicantDetails(read.getStudentId(),newAuidForReadmission.getStudent_id());
//				stu_repo.updatePGApplicable(read.getStudentId(),newAuidForReadmission.getStudent_id());
//				stu_repo.updateStdEntranceExam(read.getStudentId(),newAuidForReadmission.getStudent_id());
//				stu_repo.updateReportingStudents(read.getStudentId(),newAuidForReadmission.getStudent_id());
//				stu_repo.updateStdReportingStudentsHistory(read.getStudentId(),newAuidForReadmission.getStudent_id());
//				stu_repo.updateStudentMarks(read.getStudentId(),newAuidForReadmission.getStudent_id());
//				stu_repo.updateProctorStudentAssignment(read.getStudentId(),newAuidForReadmission.getStudent_id());
//				
////			} else {
////				Student_Details studentData = stu_repo.getOne(read.getStudentId());
////				Integer semOrYearFee = ft_repo.getsemOrYearFeeForReadmission(read.getSemOrYear(), read.getFeeTemplateId());
////				read.setTotalAmount((double) (semOrYearFee/4));
////				read.setBalance((double) (semOrYearFee/4));
////				read.setType("Re-admission");
////				Student_Details newAuidForReadmission = createNewAuidForReadmission(studentData, jwtDetails);
////				stu_repo.deactivateStudentIdForReadmission(read.getStudentId());
////				stu_repo.updateStudentTranscriptSubmission(read.getStudentId(),newAuidForReadmission.getStudent_id());
////				stu_repo.updateApplicantDetails(read.getStudentId(),newAuidForReadmission.getStudent_id());
////				stu_repo.updatePGApplicable(read.getStudentId(),newAuidForReadmission.getStudent_id());
////				stu_repo.updateStdEntranceExam(read.getStudentId(),newAuidForReadmission.getStudent_id());
////				stu_repo.updateReportingStudents(read.getStudentId(),newAuidForReadmission.getStudent_id());
////				stu_repo.updateStdReportingStudentsHistory(read.getStudentId(),newAuidForReadmission.getStudent_id());
////				
////			}
//			 re_repo.save(read);	
////		} else {
////			return ResponseHandler.generateResponse(true, HttpStatus.PAYMENT_REQUIRED, 
////					"Re-admission form submission failed! Please clear all the dues for the selected year/sem to apply for re-admission.", null);
////		}
//	} catch (Exception e) {
//		return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, 
//				"FAILURE", null);
//
//	}
//		return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", read);
//		
//	}

}
