package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.FileSystems;
import java.nio.file.NoSuchFileException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.transaction.Transactional;

import com.au.dto.*;
import com.au.model.*;
import com.au.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.config.RazorPayConfig;
import com.au.dto.UniformFeeDTO;
import com.au.dto.UniformFeeDetails;
import com.au.dto.YearlyData;
import com.au.event.StudentDueEvent;
import com.au.exception.ResourceNotFoundException;
import com.au.response.ResponseHandler;
import com.au.util.AmzonS3PresignedUrlGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class StudentDetailsService {

	private Logger log = LoggerFactory.getLogger(StudentDetailsService.class);

	public static final String value = "StudentImageBucket";
	public static final String studentPermission = "StudentPermissionBucket";
	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@Autowired
	private StudentDetailsRepository studentDetailsRepository;

	@Autowired
	private Academic_year_repository academicYearRepository;

	@Autowired
	private ApplicantDetailsRepository applicantDetailsRepository;

	@Autowired
	private PGApplicableRepository pGApplicableRepository;

	@Autowired
	private StdEntranceExamRepository stdEntranceExamRepository;

	@Autowired
	private StudentTranscriptSubmissionRepository studentTranscriptSubmissionRepository;

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private StdReportingStudentsHistoryRepository stdReportingStudentsHistoryRepository;

	@Autowired
	private ProctorStudentAssignmentRepository proctorStudentAssignmentRepository;

	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;

	@Autowired
	private SectionAssignmentRepository sectionAssignmentRepository;

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;

	@Autowired
	private StudentDueRepository studentDueRepo;

	@Autowired
	private CourseRepository course_repository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private StudentDueRepository studentDueRepository;

	@Autowired
	private CandidateWalkinRepository candidateWalkinRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private FeeTemplateSubAmountRepository feeTemplateSubAmountRepository;

	@Autowired
	private OtherFeeDetailsRepository otherFeeDetailsRepository;

	@Autowired
	private AmzonS3PresignedUrlGenerator amzonS3PresignedUrlGenerator;

	@Autowired
	private School_Repository schoolRepository;

	@Autowired
	private ProgramAssigmentRepository programAssigmentRepository;

	@Autowired
	private ProgramSpecilizationRepository programSpecilizationRepository;

	@Autowired
	private StudentPermissionRepository studentPermissionRepository;

	@Autowired
	private ExamPermitRepository examPermitRepository;

	@Autowired
	private FeeTemplateRepository feeTemplateRepository;

	@Autowired
	private StudentPaymentHistoryRepository studentPaymentHistoryRepository;

	@Autowired
	private ScholarshipApprovalStatusRepository scholarshipApprovalStatusRepository;

	@Autowired
	private AcerpAmountRepository acerpAmountRepository;

	@Autowired
	private HostelDueRepository hostelDueRepository;

//	@Autowired
//	private RazorpayClient razorpayClient;

	@Autowired
	private RazorPayTransactionRepository razorPayTransactionRepository;

	@Autowired
	private RazorPayPaymentDetailsRepository razorPayPaymentDetailsRepository;

	@Autowired
	private ClassCommencementDetailsRepository classCommencementDetailsRepository;

	@Autowired
	private DollarToInrConversionRepository dollarToInrConversionRepository;

	@Autowired
	private FinancialYearRepository financialYearRepository;

	@Autowired
	private FeeReceiptRepository feeReceiptRepository;

//	@Autowired
//	private JavaMailSender mailSender;

	@Autowired
	@Qualifier("mailSenderPrimary")  // ✅ Explicitly use the primary mail sender
	private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@Autowired
	private CandidateWalkinRepository can_repo;

	@Autowired
	private PreAdmissionProcessRepository preadmission;

	@Autowired
	private ResponseHandler response_handler;

	@Autowired
	private RegistrationFeeTrsactionRepository registrationFeeTrsactionRepository;

	@Autowired
	private CandidateWalkInService candidateWalkInService;

	@Autowired
	private RazorPayConfig razorPayConfig;

	@Autowired
	private StudentOfferAcceptanceRepository studentOfferAcceptanceRepository;

	@Autowired
	private UniformFeeTransactionDetailsRepository uniformFeeTransactionDetailsRepository;

	@Autowired
	private UniformTransactionRepository uniformTransactionRepository;

	@Autowired
	private UniformReceiptRepository uniformReceiptRepository;

	@Autowired
	private PaidBoardDueRepository paidBoardDueRepository;

	@Autowired
	private BulkTransactionRepository bulkTransactionRepository;

	@Autowired
	private BulkFeeReceiptRepository bulkFeeReceiptRepository;

	@Autowired
	private TemporaryRazorPayTransactionRepository temporaryRazorPayTransactionRepository;

	@Autowired
	private TemporaryRazorPayPaymentDetailsRepository temporaryRazorPayPaymentDetailsRepository;

	@Autowired
	private RouteAccountDetailsRepository routeAccountDetailsRepository;

	@Autowired
	private CmaFeeReceiptRepository cmaFeeReceiptRepository;

	@Autowired
	private ReadmissionRepository readmissionRepository;

	@Autowired
	private FineSlabRepository fineSlabRepository;

	@Autowired
	private FineConcessionRepository fineConcessionRepository;

	@Autowired
	private ProgramTypeRepository programTypeRepository;

	@Autowired
	private VoucherHeadNewRepository voucherHeadNewRepository;

	@Autowired
	private OtherFeeTemplateRepository otherFeeTemplateRepository;

	@Autowired
	private PreAdmissionProcessRepository preAdmissionProcessRepository;

	@Autowired
	private BoardTagAmountRepository tagBoardAmountRepository;

	@Autowired
	private FeePaymentWindowRepository feePaymentWindowRepository;

	@Autowired
	private User_Auth_Repository userAuthRepository;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	private static final Integer EXPIRE_TIME_FOR_PRESIGNED_URL = 10;

	private static final String PHP_URL_FOR_STUDENT_DETAILS = "https://acharyainstitutes.in/index.php?r=acerp-api-std/student_info_migrate&auid=";

	private static final String OFFER_ACCEPTANCE_URL = "https://www.acharyaerptech.in/offer-acceptance/";

	private static final String PAYMENT_URL = "https://www.acharyaerptech.in/registration-payment/";

	private static final String PENDING = "P";

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	public List<YearlyData> getStudentDues(int studentId) {

		StudentDues studentDues = studentDueRepo.callStudentDueDetails(studentId);
		List<YearlyData> yearlyDataList = new ArrayList<>();

		for (int year = 1; year <= 8; year++) {
			YearlyData yearlyData = new YearlyData();
			yearlyData.setYear(year);

			yearlyData.setFixed(getYearlyFixed(studentDues, year));
			yearlyData.setPaid(getYearlyPaid(studentDues, year));
			yearlyData.setDue(getYearlyDue(studentDues, year));
			yearlyData.setScholarship(getYearlyScholarship(studentDues, year));
			yearlyData.setWaiver(getYearlyTuitionFee(studentDues, year));

			yearlyDataList.add(yearlyData);
		}
		return yearlyDataList;

	}

//	public StudentDues getStudentDuesByStudent(int studentId) {
//		StudentDues stu = new StudentDues();
//		StudentDueEvent studDue=new StudentDueEvent(null,null,null,"studentDue");
//		applicationEventPublisher.publishEvent(studDue);
//		return stu;
//
//	}

	private double getYearlyFixed(StudentDues studentDues, int year) {
		switch (year) {
			case 1:
				return studentDues.getS1fxd();
			case 2:
				return studentDues.getS2fxd();
			case 3:
				return studentDues.getS3fxd();
			case 4:
				return studentDues.getS4fxd();
			case 5:
				return studentDues.getS5fxd();
			case 6:
				return studentDues.getS6fxd();
			case 7:
				return studentDues.getS7fxd();
			case 8:
				return studentDues.getS8fxd();
			default:
				return 0.0;
		}
	}

	private double getYearlyPaid(StudentDues studentDues, int year) {
		switch (year) {
			case 1:
				return studentDues.getS1paid();
			case 2:
				return studentDues.getS2paid();
			case 3:
				return studentDues.getS3paid();
			case 4:
				return studentDues.getS4paid();
			case 5:
				return studentDues.getS5paid();
			case 6:
				return studentDues.getS6paid();
			case 7:
				return studentDues.getS7paid();
			case 8:
				return studentDues.getS8paid();

			default:
				return 0.0;
		}
	}

	private double getYearlyDue(StudentDues studentDues, int year) {
		switch (year) {
			case 1:
				return studentDues.getS1due();
			case 2:
				return studentDues.getS2due();
			case 3:
				return studentDues.getS3due();
			case 4:
				return studentDues.getS4due();
			case 5:
				return studentDues.getS5due();
			case 6:
				return studentDues.getS6due();
			case 7:
				return studentDues.getS7due();
			case 8:
				return studentDues.getS8due();
			default:
				return 0.0;
		}
	}

	private double getYearlyScholarship(StudentDues studentDues, int year) {
		switch (year) {
			case 1:
				return studentDues.getS1sch();
			case 2:
				return studentDues.getS2sch();
			case 3:
				return studentDues.getS3sch();
			case 4:
				return studentDues.getS4sch();
			case 5:
				return studentDues.getS5sch();
			case 6:
				return studentDues.getS6sch();
			case 7:
				return studentDues.getS7sch();
			case 8:
				return studentDues.getS8sch();

			default:
				return 0.0;
		}
	}

	private double getYearlyTuitionFee(StudentDues studentDues, int year) {
		switch (year) {
			case 1:
				return studentDues.getS1waivr();
			case 2:
				return studentDues.getS2waivr();
			case 3:
				return studentDues.getS3waivr();
			case 4:
				return studentDues.getS4waivr();
			case 5:
				return studentDues.getS5waivr();
			case 6:
				return studentDues.getS6waivr();
			case 7:
				return studentDues.getS7waivr();
			case 8:
				return studentDues.getS8waivr();

			default:
				return 0.0;
		}
	}

	public List<Student_Details> listAll() {
		return studentDetailsRepository.findAll();
	}

	/*
	 * public String emailCreation(Student_Details stu) {
	 *
	 * String email = ""; String fn = stu.getFirstname().substring(0, 3); String
	 * fafn = stu.getFather_name().substring(0, 1); String ac_year_id =
	 * academicYearRepository.findByAcYearId1().toString().substring/(2, 4); String
	 * program_name =
	 * programSpecilizationRepository.getProgramAuid(stu.getProgram_specilization_id
	 * ());
	 *
	 * email =
	 * fn.concat(fafn).concat(".").concat(ac_year_id).concat(program_name).concat(
	 * "acharya.ac.in"); return email; }
	 */

	String id = "0000";

	/*
	 * public String getcount(Integer ac_year_id, Integer school_id, Integer
	 * program_id, Integer program_specialization_id) {
	 *
	 * Integer x = null; x = studentDetailsRepository.getMaxStudentCount(ac_year_id,
	 * school_id, program_id, program_specialization_id);
	 *
	 * if (x == null) { x = 0; }
	 *
	 * if (getAuidFilterCount(ac_year_id, school_id, program_id,
	 * program_specialization_id) == 0) { x = 0; }
	 *
	 * System.out.println("before==" + x); String p2 = id.substring(2); // x =
	 * Integer.parseInt(p2); x = x + 1; System.out.println("=====after============"
	 * + x); if (x <= 9) { id = "00" + x; } else if (x <= 99) { id = "0" + x; } else
	 * if (x <= 999) { id = "" + x; } else if (x <= 9999) { id = "" + x; } return
	 * id; }
	 */

	public String getcount() {

		Integer x = null;
		x = studentDetailsRepository.getMaxStudentCount();

		if (x == null) {
			x = 0;
		}

		/*
		 * if (getAuidFilterCount(ac_year_id, school_id, program_id,
		 * program_specialization_id) == 0) { x = 0; }
		 */
		System.out.println("before==" + x);
		String p2 = id.substring(2);

		x = x + 1;
		System.out.println("=====after============" + x);
		if (x <= 9) {
			id = "000" + x;
		} else if (x <= 99) {
			id = "00" + x;
		} else if (x <= 999) {
			id = "0" + x;
		} else if (x <= 9999) {
			id = "0" + x;
		}
		return id;
	}

	public Student_Details saveStudent_Details(Student_Details st) {
		return studentDetailsRepository.save(st);
	}

	public Student_Details getStuDetails(ApplicationDto app, @RequestHeader("Authorization") String jwtToken) {
		UserAuthentication creating_student_user_details = new UserAuthentication();
		try {

			if (studentDetailsRepository.getCountOfCandidateID(app.getSd().getCandidate_id()) >= 1) {
				throw new Exception("Auid already created for candidate !!!");
			}

			JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			Optional<Program> program = programRepository.findById(app.getSd().getProgram_id());
			String program_code_for_auid = program.get().getProgram_code();
			if (program_code_for_auid == null) {
				throw new RuntimeException("Program Code IS NULL");
			}
			Academic_year academicYearsDetails = academicYearRepository.findByAcademicId(app.getSd().getAc_year_id());
			if (academicYearsDetails == null) {
				throw new RuntimeException("Academic Year IS NULL");
			}
			String ac_year_id = academicYearsDetails.getCurrent_year().toString().substring(2, 4);
			String specialization_auid = programSpecilizationRepository
					.getProgramAuid(app.getSd().getProgram_specialization_id());
			if (specialization_auid == null) {
				throw new RuntimeException("Specialization Auid Format IS NULL");
			}

			Integer count_for_auid;
			String last_auid = studentDetailsRepository.getLastStudentDetailsByProgramSpecializationId(
					app.getSd().getAc_year_id(), app.getSd().getProgram_id(),
					app.getSd().getProgram_specialization_id());
			if (last_auid != null) {
				count_for_auid = Integer.parseInt(last_auid.substring(9)) + 1;
			} else {
				count_for_auid = 1;
			}

			String auid_formattedStrr = String.format("%03d", count_for_auid);
//			String schoolShortName = schoolRepository.getSchoolShortName(app.getSd().getSchool_id()).substring(2, 5);
			String schoolShortName = schoolRepository.getSchoolShortName(app.getSd().getSchool_id());
			String auid_format = schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr;
			app.getSd().setAuid(schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr);

			Integer master_code_count = studentDetailsRepository.maxStudentMasterCode();
			String formattedStrr = String.format("%04d", master_code_count + 1);
			app.getSd().setStudent_master_code("ASC" + formattedStrr);
			String acharya_email = app.getSd().getEmail_preferred_name().toLowerCase() + "_" + ac_year_id
					+ specialization_auid.toLowerCase() + "@acharya.ac.in";
			app.getSd().setAcharya_email(acharya_email);

			studentDetailsRepository.save(app.getSd());

			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String passwordEncoder1 = passwordEncoder.encode("acharya1234");

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

			Student_Details st = getStudentByStudentId(app.getSd().getStudent_id());

			app.getAp().parallelStream().forEach(ap1 -> {
				ApplicantDetails ap = new ApplicantDetails();
				Student_Details st1 = get(app.getSd().getStudent_id());

				ap.setStd_id(st1.getStudent_id());
				ap.setCandidate_id(st1.getCandidate_id());
				ap.setAuid(st1.getAuid());

				ap.setBoard_university(ap1.getBoard_university());
				ap.setCollege_name(ap1.getCollege_name());
				ap.setCourse(ap1.getCourse());
				ap.setQualifying_exam_year(ap1.getQualifying_exam_year());
				ap.setSubjects_studied(ap1.getSubjects_studied());
				ap.setMarks_total(ap1.getMarks_total());
				ap.setTotal_obtained(ap1.getTotal_obtained());
				ap.setPercentage_scored(ap1.getPercentage_scored());
				ap.setApplicant_id(ap1.getApplicant_id());
				ap.setCreated_by(jwtDetails.getUserId());
				ap.setCreated_username(jwtDetails.getUserName());
				ap.setEntrance_exam_name(ap1.getEntrance_exam_name());
				ap.setEntrance_score(ap1.getEntrance_score());
				ap.setYear_of_entrance(ap1.getYear_of_entrance());
				ap.setFirst_language(ap1.getFirst_language());
				ap.setSecond_language(ap1.getSecond_language());
				ap.setPassed_year(ap1.getPassed_year());
				ap.setQualifying_year(ap1.getQualifying_year());
				ap.setState(ap1.getState());
				ap.setYear_of_entrance(ap1.getYear_of_entrance());
				ap.setRemarks(ap1.getRemarks());
				ap.setActive(ap1.getActive());
				ap.setOptional_max_mark(ap1.getOptional_max_mark());
				ap.setOptional_min_mark(ap1.getOptional_min_mark());
				ap.setOptional_percentage(ap1.getOptional_percentage());
				ap.setOptional_subject(ap1.getOptional_subject());
				applicantDetailsRepository.save(ap);
			});

			PGApplicable pg = new PGApplicable();
			pg.setStd_id(st.getStudent_id());
			pg.setCandidate_id(app.getSd().getCandidate_id());
			pg.setAuid(app.getSd().getAuid());

			pg.setUg_board(app.getPgapp().getUg_board());
			pg.setPg_total_percentage(app.getPgapp().getPg_total_percentage());
			pg.setCreated_by(jwtDetails.getUserId());
			pg.setCreated_username(jwtDetails.getUserName());
			pg.setPg_exam_passed_name(app.getPgapp().getPg_exam_passed_name());
			pg.setUniversity(app.getPgapp().getUniversity());
			pg.setYear_of_passing(app.getPgapp().getYear_of_passing());
			pg.setSubject_studied_lang(app.getPgapp().getSubject_studied_lang());
			pg.setYear_1(app.getPgapp().getYear_1());
			pg.setYear_2(app.getPgapp().getYear_2());
			pg.setYear_3(app.getPgapp().getYear_3());
			pg.setYear_4(app.getPgapp().getYear_4());
			pg.setMarks_total(app.getPgapp().getMarks_total());
			pg.setTotal_obtained(app.getPgapp().getTotal_obtained());
			pg.setCollege_name(app.getPgapp().getCollege_name());
			pg.setActive(app.getPgapp().getActive());
			pGApplicableRepository.save(pg);

			StdEntranceExam see = new StdEntranceExam();
			see.setStudent_id(st.getStudent_id());
			see.setEntrance_exam_name(app.getSee().getEntrance_exam_name());
			see.setState(app.getSee().getState());
			see.setYear_month1(app.getSee().getYear_month1());
			see.setScore(app.getSee().getScore());
			see.setCreated_by(jwtDetails.getUserId());
			see.setCreated_username(jwtDetails.getUserName());
			see.setActive(app.getSee().getActive());
			stdEntranceExamRepository.save(see);

			app.getStreq().getTranscript_id().parallelStream().forEach(t -> {
				StudentTranscriptSubmission sts1 = new StudentTranscriptSubmission();
				LocalDate currentDate = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				String dateString = currentDate.format(formatter);
				Student_Details st2 = get(app.getSd().getStudent_id());
				sts1.setStudent_id(st2.getStudent_id());
				sts1.setActive(app.getStreq().getActive());
				sts1.setTranscript_id(t);
				sts1.setCreated_by(jwtDetails.getUserId());
				sts1.setCreated_username(jwtDetails.getUserName());
				sts1.setTranscript_locker_number(app.getStreq().getTranscript_locker_number());
				sts1.setIs_collected("YES");
				sts1.setSubmitted_date(dateString);
				sts1.setCollected_by(jwtDetails.getUserId());
				studentTranscriptSubmissionRepository.save(sts1);
			});

			app.getStreq().getNot_applicable().parallelStream().forEach(p -> {
				StudentTranscriptSubmission sts1 = new StudentTranscriptSubmission();
				Student_Details st2 = get(app.getSd().getStudent_id());

				sts1.setStudent_id(st2.getStudent_id());
				sts1.setActive(app.getStreq().getActive());
				sts1.setNot_applicable("YES");
				sts1.setTranscript_id(p);
				sts1.setCreated_by(jwtDetails.getUserId());
				sts1.setCreated_username(jwtDetails.getUserName());
				studentTranscriptSubmissionRepository.save(sts1);
			});
			app.getStreq().getSubmitted_date().entrySet().parallelStream().forEach(d -> {
				StudentTranscriptSubmission sts1 = new StudentTranscriptSubmission();
				Student_Details st3 = get(app.getSd().getStudent_id());

				sts1.setStudent_id(st3.getStudent_id());
				sts1.setActive(app.getStreq().getActive());
				sts1.setCreated_by(jwtDetails.getUserId());
				sts1.setCreated_username(jwtDetails.getUserName());
				sts1.setTranscript_id(d.getKey());
				sts1.setWill_submit_by(d.getValue());
				// sts1.setIs_collected("YES");
				studentTranscriptSubmissionRepository.save(sts1);

			});

			ReportingStudents rs1 = new ReportingStudents();
			rs1.setStudent_id(st.getStudent_id());
			rs1.setActive(app.getRs().getActive());
			rs1.setCreated_by(jwtDetails.getUserId());
			rs1.setModified_by(jwtDetails.getUserId());
			rs1.setCreated_username(jwtDetails.getUserName());
			rs1.setCurrent_sem(app.getRs().getCurrent_sem());
			rs1.setCurrent_year(app.getRs().getCurrent_year());
			rs1.setEligible_reported_status(app.getRs().getEligible_reported_status());
			rs1.setDistinct_status(app.getRs().getDistinct_status());
			rs1.setPrevious_sem(app.getRs().getPrevious_sem());
			rs1.setPrevious_year(app.getRs().getPrevious_year());
			rs1.setRemarks(app.getRs().getRemarks());
			rs1.setReported_ac_year_id(app.getRs().getReported_ac_year_id());
			rs1.setSection_id(app.getRs().getSection_id());
			rs1.setYear_back_status(app.getRs().getYear_back_status());
			reportingStudentsRepository.save(rs1);
//
//			RegistrationFeeTransaction registrationFeeTransaction = registrationFeeTrsactionRepository
//					.getTransactionDetailsByCandidateId(st.getCandidate_id());
//			if (ObjectUtils.isNotEmpty(registrationFeeTransaction)) {
//				FeeReceipt feeReceipt = new FeeReceipt();
//				feeReceipt.setAc_year_id(st.getAc_year_id());
//				feeReceipt.setActive(Boolean.TRUE);
//				feeReceipt.setCreated_by(st.getCreated_by());
//				feeReceipt.setPaid_year(String.valueOf(rs1.getCurrent_sem()));
//				feeReceipt.setPaid_amount(registrationFeeTransaction.getAmount());
//				feeReceipt.setStudent_id(st.getStudent_id());
//				feeReceipt.setReceipt_type("Registration Fee");
//				FinancialYear financialYear = financialYearRepository.getFinancialYearIdOnCurrentYear();
//				feeReceipt.setFinancial_year_id(financialYear.getFinancial_year_id());
//				feeReceiptRepository.save(feeReceipt);
//			}

			StudentDueEvent studDue = new StudentDueEvent(null, null, st.getStudent_id(), null);
			applicationEventPublisher.publishEvent(studDue);
//			StdReportingStudentsHistory srsh1 = new StdReportingStudentsHistory();
//			srsh1.setStudent_id(st.getStudent_id());
//			srsh1.setActive(app.getSrsh().getActive());
//			srsh1.setCreated_by(jwtDetails.getUserId());
//			srsh1.setCreated_username(jwtDetails.getUserName());
//			srsh1.setCurrent_sem(app.getSrsh().getCurrent_sem());
//
//			srsh1.setDistinct_status(app.getSrsh().getDistinct_status());
//			srsh1.setEligible_reported_status(app.getSrsh().getEligible_reported_status());
//			srsh1.setCurrent_year(app.getSrsh().getCurrent_year());
//			srsh1.setPrevious_sem(app.getSrsh().getPrevious_sem());
//			srsh1.setPrevious_year(app.getSrsh().getPrevious_year());
//			srsh1.setProgram_specialization_id(app.getSrsh().getProgram_specialization_id());
//			srsh1.setProgram_type_id(app.getSrsh().getProgram_type_id());
//			srsh1.setRemarks(app.getSrsh().getRemarks());
//			srsh1.setReported_ac_year_id(app.getSrsh().getReported_ac_year_id());
//			srsh1.setReporting_date(app.getSrsh().getReporting_date());
//			srsh1.setSchool_id(app.getSrsh().getSchool_id());
//			stdReportingStudentsHistoryRepository.save(srsh1);

//			StudentDues studentDues=studentDueRepository.callStudentDueDetails(st.getStudent_id());
			candidateWalkInService.updateLsqStatus(st.getCandidate_id(), 6);
			candidateWalkInService.callLeadSquaredApiForUpdateAuid(st.getCandidate_id(), st.getAuid());
			FeeTemplate feeTemplate = feeTemplateRepository.findById(st.getFee_template_id()).get();
			if (ObjectUtils.isNotEmpty(feeTemplate) && feeTemplate.getIs_paid_at_board() == true) {
				paidBoardDueRepository.callStudentPaidBoardDue(st.getStudent_id());
			}

			return app.getSd();
		} catch (Exception e) {
//			userAuthenticationRepository.deleteById(creating_student_user_details.getId());
			throw new RuntimeException(
					"Check Program Code Or Specialization Auid Format IS NULL Or Role Name Student Not Found Or "
							+ e.getMessage());
		}
	}

	public Student_Details get(Integer id) {
		return studentDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student_Details Not Found:" + id));
	}

	public void delete(Integer id) {
		Student_Details existingCourse = studentDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student_Details Not Found:" + id));
		this.studentDetailsRepository.delete(existingCourse);
	}

	public String studentEmailExistorNot(String email) {
		return studentDetailsRepository.studentEmailExistorNot(email);
	}

	public Student_Details getStudentByStudentId(Integer student_id) {
		return studentDetailsRepository.getStudentByStudentId(student_id);
	}

	public Integer getAuidFilterCount(Integer ac_year_id, Integer school_id, Integer program_id,
									  Integer program_specialization_id) {
		return studentDetailsRepository.getAuidFilterCount(ac_year_id, school_id, program_id,
				program_specialization_id);
	}

	public List<HashMap<String, Object>> auidNext(Integer student_id) {
		// TODO Auto-generated method stub
		return studentDetailsRepository.auidNext(student_id);
	}

	public List<Map<String, Object>> fetchSectionAssignDetails(Integer ac_year_id, Integer school_id,
															   Integer program_id, Integer program_specialization_id, Integer section_id, Integer current_sem,
															   Integer current_year) {
		return studentDetailsRepository.fetchSectionAssignDetails(ac_year_id, school_id, program_id,
				program_specialization_id, section_id, current_sem, current_year);
	}

	public List<HashMap<String, Object>> getStudentlist(Integer ac_year_id, Integer school_id, Integer program_id,
														Integer program_specialization_id) {
		return studentDetailsRepository.getstudentList(ac_year_id, school_id, program_id, program_specialization_id);
	}

	public void updateproctor(Integer student_id, Integer proctor_assign_id) {
		studentDetailsRepository.updateStudentDetail(student_id);
		proctorStudentAssignmentRepository.updateProctorStatus(proctor_assign_id);
	}

	public List<HashMap<String, Object>> getStudentIndex(Integer ac_year_id) {

		return studentDetailsRepository.getStudentIndex(ac_year_id);
	}

	public List<HashMap<String, Object>> getStudentIndex(Integer ac_year_id, Integer school_id) {

		return studentDetailsRepository.getStudentIndex(ac_year_id, school_id);
	}

	public List<HashMap<String, Object>> getStudentIndex(Integer ac_year_id, Integer school_id, Integer program_id) {

		return studentDetailsRepository.getStudentIndex(ac_year_id, school_id, program_id);
	}

	public List<HashMap<String, Object>> getStudentIndex(Integer ac_year_id, Integer school_id, Integer program_id,
														 Integer program_specialization_id) {

		return studentDetailsRepository.getStudentIndex(ac_year_id, school_id, program_id, program_specialization_id);
	}

	public List<Student_Details> activeStudentDetailsList() {
		return studentDetailsRepository.activeStudentDetailsList();
	}

	public HashMap<String, Object> studentDetailsForMobileApp(String email) {
		HashMap<String, Object> studentDetails = studentDetailsRepository.studentDetailsForMobileApp(email);
		String key = StringUtils.isBlank((CharSequence) studentDetails.get("student_image_path")) ? ""
				: (String) studentDetails.get("student_image_path");
		String key1 = value + "/" + key;
		String keyForPresignedUrl = amzonS3PresignedUrlGenerator.generatePresignedUrl(bucketName, key1,
				EXPIRE_TIME_FOR_PRESIGNED_URL);
		studentDetails.put("student_image_path", keyForPresignedUrl);
		return studentDetails;
	}

	public void sendMailForStudentOffer1(String pdf_content, Integer candidate_id) throws Exception {
//	        File htmlFile = new File("C:\\Users\\Admin\\git\\au\\Acharya_University\\test.html");
//	        
//	        
//	        
////	        File output = new File("C:\\Users\\Admin\\git\\au\\Acharya_University\\test.html");
//	        FileWriter writer = new FileWriter(htmlFile);
//
//	        writer.write(pdf_content);
//	        writer.flush();
//	        writer.close();

		Student_Details candidate_details = studentDetailsRepository.getStudentByStudentId(candidate_id);
		String candidate_email = candidate_details.getAcharya_email();

		Document doc = Jsoup.parse(pdf_content, "UTF-8");
		doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		try (FileOutputStream os = new FileOutputStream("C:\\Users\\Admin\\git\\au\\Acharya_University\\output.pdf")) {
			ITextRenderer renderer = new ITextRenderer();
			SharedContext cntxt = renderer.getSharedContext();
			cntxt.setPrint(true);
			cntxt.setInteractive(false);
			// renderer.getFontResolver().addFont(getClass().getClassLoader().getResource("fonts/PRISTINA.ttf").toString(),
			// true);
			String baseUrl = FileSystems.getDefault()
					.getPath("C:\\Users\\Admin\\git\\au\\Acharya_University\\Acharya_University").toUri().toURL()
					.toString();
			renderer.setDocumentFromString(doc.html(), baseUrl);
			renderer.layout();
			renderer.createPDF(os);
			System.out.println("done");
		}
	}

	public List<HashMap<String, Object>> getStudentDetail(String auid) {

		return studentDetailsRepository.getStudentDetail(auid);
	}

	public List<HashMap<String, Object>> inActiveStudentDetailsByAuid(String auid) {

		return studentDetailsRepository.inActiveStudentDetailsByAuid(auid);
	}

	public List<Map<String, Object>> fetchStudentDetailForSectionAssignmentOnSem(Integer ac_year_id, Integer school_id,
																				 Integer program_id, Integer program_specialization_id, Integer current_sem) {
		return studentDetailsRepository.fetchStudentDetailForSectionAssignmentOnSem(ac_year_id, school_id, program_id,
				program_specialization_id, current_sem);
	}

	public List<Map<String, Object>> fetchStudentDetailForSectionAssignmentOnYear(Integer ac_year_id, Integer school_id,
																				  Integer program_id, Integer program_specialization_id, Integer current_year) {
		return studentDetailsRepository.fetchStudentDetailForSectionAssignmentOnYear(ac_year_id, school_id, program_id,
				program_specialization_id, current_year);
	}

	public List<Map<String, Object>> fetchAllUnAssignedStudentDetailForSectionAssignmentFromIndex(Integer school_id,
																								  Integer program_id, Integer program_specialization_id, Integer current_year_sem,
																								  Integer program_assignment_id) {
		if (programAssigmentRepository.getProgramTypeByProgramAssignmentId(program_assignment_id)
				.equalsIgnoreCase("Semester")) {
			return studentDetailsRepository.fetchAllSectionUnAssignedStudentDetailOnSem(school_id, program_id,
					program_specialization_id, current_year_sem);
		} else {
			return studentDetailsRepository.fetchAllSectionUnAssignedStudentDetailOnYear(school_id, program_id,
					program_specialization_id, current_year_sem);
		}
	}

	public List<Map<String, Object>> fetchAllAssignedStudentDetailForSectionAssignmentForUpdate(Integer school_id,
																								Integer program_id, Integer program_specialization_id, Integer current_year_sem, Integer section_id,
																								Integer program_Assignment_id) {
		if (programAssigmentRepository.getProgramTypeByProgramAssignmentId(program_Assignment_id)
				.equalsIgnoreCase("Semester")) {
			return studentDetailsRepository.fetchAllSectionAssignedStudentDetailOnSemForUpdate(school_id, program_id,
					program_specialization_id, current_year_sem, section_id);
		} else {
			return studentDetailsRepository.fetchAllSectionAssignedStudentDetailOnYearForUpdate(school_id, program_id,
					program_specialization_id, current_year_sem, section_id);
		}
	}

//	public Object sendMailForStudentOffer(String pdf_content, Integer student_id) throws Exception {
//		Student_Details stu_details = studentDetailsRepository.getStudentByStudentId(student_id);
//		String stu_email = stu_details.getAcharya_email();
//		ByteArrayOutputStream outputStream = null;
//		
//    	//now write the PDF content to the output stream
//    	outputStream = new ByteArrayOutputStream();
//		generatePDFFromHTML(outputStream, pdf_content, student_id);
//		
//		byte[] bytes = outputStream.toByteArray();	             
//    	//construct the pdf body part
//    	DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
//    	
//    	MimeBodyPart pdfBodyPart = new MimeBodyPart();
//    	pdfBodyPart.setDataHandler(new DataHandler(dataSource));
//    	pdfBodyPart.setFileName("Offer_"+student_id+".pdf");
//
//    	
//    	String contents = "Dear Mr." + "<br/>" + "<br/>"
//				+ "Congratulations on your offer from Acharya Institutes ! We are delighted to offer you a position with us.!!<br/>"
//				+ "Please find attached your detailed offer letter. Kindly acknowledge and confirm your acceptance.<br/>"
//			//	+ "Looking forward for your reversion.<br/>" + "<br/>" + "Click on the below link to confirm your acceptance for reporting on " + reportOn + ".<br/>" + "<br/>"
//			//	+ "<a href= "+url_domain+" + "+"/"+offer_id+" style='background-color: #4A57A9;color:white;text-decoration:none;padding:6px'>Accept</a>" + "<br/>" + "<br/>" + "<br/>" + "--<br/>" + "Divya<br/>"
//				+ "Manager - ERP<br/>" + "Acharya Institutes<br/>" + "Bangalore";
//    	
//		MimeBodyPart textBodyPart = new MimeBodyPart();
//    	textBodyPart.setText(contents);
//	
//    	MimeMultipart mimeMultipart = new MimeMultipart();
//        mimeMultipart.addBodyPart(textBodyPart);
//        mimeMultipart.addBodyPart(pdfBodyPart);
//        
//        String subject = "Offer letter - " + student_id;
//        
//        MimeMessage message = mailSender.createMimeMessage();
//		try {
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//			helper.setFrom(env.getProperty("spring.mail.properties.mail.smtp.from"), "ERP-Info");
//			helper.setTo(stu_email);
//			helper.setSubject(subject);
//			helper.setText(contents, true);
//			File file = new File("D:\\Offer.pdf");
//			helper.addAttachment("Offer.pdf", file);
//		} catch (MessagingException e) {
//			throw new MailParseException(e);
//		}
//
//		mailSender.send(message);
//		System.out.println("PDF GENERATED...");
//		System.out.println("Mail Send...");
//		response_handler.delete(0, ".pdf");
//		//response_handler.sendMailsWithAttachment(null, pdf_content, pdf_content, null, stu_email);
//		return null;
//	}

	public void uploadFile(Integer student_id, MultipartFile imageFile) throws IOException {

		Student_Details student_detail = studentDetailsRepository.findById(student_id)
				.orElseThrow(() -> new ResourceNotFoundException("StudentDetails Not Found:" + student_id));
		;
		try {
			log.debug("Message For Student Image Attachment --------------");
			File image_file = convertMultiPartToFile(imageFile);
			String imageFileName = generateFileName(imageFile);
			log.debug("Student Image Attachment", image_file);
			System.out.println(student_id);
			System.out.println("(((((((((((((((((()))))))))))))))))) " + student_detail.getStudent_image_path());
			student_detail.setStudent_image_path(LocalDate.now() + "/" + student_id + "/" + imageFileName);
			uploadFileToS3Bucket(imageFileName, image_file, student_id);
			log.debug("Message For Attachment", image_file);
			System.out.println("(((((((((((((((((()))))))))))))))))) " + student_detail.getStudent_image_path());
			image_file.delete();
		} catch (AmazonServiceException ase) {

			log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			log.info("Error Message:    " + ase.getMessage());
			log.info("HTTP Status Code: " + ase.getStatusCode());
			log.info("AWS Error Code:   " + ase.getErrorCode());
			log.info("Error Type:       " + ase.getErrorType());
			log.info("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			log.info("Caught an AmazonClientException: ");
			log.info("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			log.info("IOE Error Message: " + ioe.getMessage());

		}
		studentDetailsRepository.save(student_detail);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(file.getBytes());
		fos.close();
		return convertFile;

	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileToS3Bucket(String fileName, File file, Integer student_id) {
		final String uniqueFileName = value + "/" + LocalDate.now() + "/" + student_id + "/" + fileName;
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

	}

	public byte[] downloadFile(final String keyName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, value + "/" + keyName);
			final S3ObjectInputStream stream = s3Object.getObjectContent();
			content = IOUtils.toByteArray(stream);
			System.out.println(content);
			s3Object.close();
			return content;

		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}

	public List<Map<String, Object>> fetchAllStudentDetailForStudentMarks(Integer school_id,
																		  Integer program_assignment_id, Integer program_id, Integer program_specialization_id, Integer section_id,
																		  Integer current_year_sem) {

		List<SectionAssignment> section_assigned = sectionAssignmentRepository.getSectionAssignmentDetailsForStudentIds(
				school_id, program_assignment_id, program_id, program_specialization_id, section_id, current_year_sem);

		String all_student_id = section_assigned.stream().map(SectionAssignment::getStudent_ids)
				.collect(Collectors.joining(","));

		List<Integer> student_ids = ResponseHandler.toConvertCommaSeperatedIdsAsList(all_student_id).stream().distinct()
				.map(e -> e).collect(Collectors.toList());

		System.out.println("{{{{{{{{{{{{{{{{{{{ " + student_ids);

		return studentDetailsRepository.fetchAllStudentDetailForStudentMarks(student_ids);

	}

	public List<HashMap<String, Object>> getStudentViewDetailsByAuid(String auid) {
		return studentDetailsRepository.getStudentViewDetailsByAuid(auid);
	}

	public HashMap<String, Object> getAllStudentDetailsData(Integer student_id) {

		HashMap<String, Object> formatedRersponse = new HashMap<>();

		HashMap<String, Object> reporting_data = reportingStudentsRepository.getreportingStudentData(student_id);
		List<HashMap<String, Object>> course_data = course_repository.getCourseData(student_id);

		HashMap<String, Object> stu = studentDetailsRepository.studentDataForTestimonials(student_id);
		List<HashMap<String, Object>> transcript_details = studentTranscriptSubmissionRepository
				.getTranscriptDetails(student_id);

		// repo_carepo.getCourseAssignmentData(student_id);

		formatedRersponse.put("reporting_students", reporting_data);
		formatedRersponse.put("course", course_data);
		formatedRersponse.put("Student_details", stu);
		formatedRersponse.put("Student_Transcript_Details", transcript_details);

		return formatedRersponse;
	}

	public List<Map<String, Object>> getStudentDetailsForIdCard() {
		return studentDetailsRepository.getStudentDetailsForIdCard();
	}

	public List<StudentFeeDetails> getStudentDueDetails(Integer student_id) {
		List<StudentFeeDetails> studentFeeDetails = studentDueRepo.getStudentDueDetails(student_id);
		return studentFeeDetails;

	}

	public StudentFeeDetailsDTO getStudentFeeDetails(Integer student_id) {
		StudentFeeDetailsDTO studentFeeDetailsDTO = studentDueRepo.getStudentFeeDetails(student_id);
		List<StudentFeeDetails> studentFeeDetails = studentDueRepo.getStudentDueDetails(student_id);
		if (ObjectUtils.isNotEmpty(studentFeeDetails)) {
			studentFeeDetailsDTO.setStudentFeeDetails(studentFeeDetails);
		}
		StudentDues studentDues = studentDueRepo.findById(student_id).orElse(null);
		ReportingStudents reportingStudents = reportingStudentsRepository.findById(student_id).orElse(null);
		if (ObjectUtils.isNotEmpty(reportingStudents) && ObjectUtils.isNotEmpty(studentDues)) {
			List<SemesterWiseFeeData> semesterWiseFeeDatas = getSemesterWiseData(studentDues, reportingStudents);
			studentFeeDetailsDTO.setSemesterWiseFeeDatas(semesterWiseFeeDatas);
		}
		return studentFeeDetailsDTO;
	}

	private List<SemesterWiseFeeData> getSemesterWiseData(StudentDues studentDues,
														  ReportingStudents reportingStudents) {
		List<SemesterWiseFeeData> semesterWiseFeeDatas = new ArrayList<>();
		for (int i = 1; i <= reportingStudents.getCurrent_sem(); i++) {
			setSemesterWiseData(semesterWiseFeeDatas, studentDues, i);
		}
		return semesterWiseFeeDatas;
	}

	private void setSemesterWiseData(List<SemesterWiseFeeData> semesterWiseFeeDatas, StudentDues studentDues, int i) {
		switch (i) {
			case 1:
				SemesterWiseFeeData sem1 = new SemesterWiseFeeData();
				sem1.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS1due()) ? studentDues.getS1due() : 0);
				sem1.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS1paid()) ? studentDues.getS1paid() : 0);
				sem1.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS1fxd()) ? studentDues.getS1fxd() : 0);
				sem1.setSem(i);
				semesterWiseFeeDatas.add(sem1);
				break;
			case 2:
				SemesterWiseFeeData sem2 = new SemesterWiseFeeData();
				sem2.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS2due()) ? studentDues.getS2due() : 0);
				sem2.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS2paid()) ? studentDues.getS2paid() : 0);
				sem2.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS2fxd()) ? studentDues.getS2fxd() : 0);
				sem2.setSem(i);
				semesterWiseFeeDatas.add(sem2);
				break;
			case 3:
				SemesterWiseFeeData sem3 = new SemesterWiseFeeData();
				sem3.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS3due()) ? studentDues.getS3due() : 0);
				sem3.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS3paid()) ? studentDues.getS3paid() : 0);
				sem3.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS3fxd()) ? studentDues.getS3fxd() : 0);
				sem3.setSem(i);
				semesterWiseFeeDatas.add(sem3);
				break;
			case 4:
				SemesterWiseFeeData sem4 = new SemesterWiseFeeData();
				sem4.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS4due()) ? studentDues.getS4due() : 0);
				sem4.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS4paid()) ? studentDues.getS4paid() : 0);
				sem4.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS4fxd()) ? studentDues.getS4fxd() : 0);
				sem4.setSem(i);
				semesterWiseFeeDatas.add(sem4);
				break;
			case 5:
				SemesterWiseFeeData sem5 = new SemesterWiseFeeData();
				sem5.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS5due()) ? studentDues.getS5due() : 0);
				sem5.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS5paid()) ? studentDues.getS5paid() : 0);
				sem5.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS5fxd()) ? studentDues.getS5fxd() : 0);
				sem5.setSem(i);
				semesterWiseFeeDatas.add(sem5);
				break;
			case 6:
				SemesterWiseFeeData sem6 = new SemesterWiseFeeData();
				sem6.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS6due()) ? studentDues.getS6due() : 0);
				sem6.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS6paid()) ? studentDues.getS6paid() : 0);
				sem6.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS6fxd()) ? studentDues.getS6fxd() : 0);
				sem6.setSem(i);
				semesterWiseFeeDatas.add(sem6);
				break;
			case 7:
				SemesterWiseFeeData sem7 = new SemesterWiseFeeData();
				sem7.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS7due()) ? studentDues.getS7due() : 0);
				sem7.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS7paid()) ? studentDues.getS7paid() : 0);
				sem7.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS7fxd()) ? studentDues.getS7fxd() : 0);
				sem7.setSem(i);
				semesterWiseFeeDatas.add(sem7);
				break;
			case 8:
				SemesterWiseFeeData sem8 = new SemesterWiseFeeData();
				sem8.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS8due()) ? studentDues.getS8due() : 0);
				sem8.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS8paid()) ? studentDues.getS8paid() : 0);
				sem8.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS8fxd()) ? studentDues.getS8fxd() : 0);
				sem8.setSem(i);
				semesterWiseFeeDatas.add(sem8);
				break;
			case 9:
				SemesterWiseFeeData sem9 = new SemesterWiseFeeData();
				sem9.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS9due()) ? studentDues.getS9due() : 0);
				sem9.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS9paid()) ? studentDues.getS9paid() : 0);
				sem9.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS9fxd()) ? studentDues.getS9fxd() : 0);
				sem9.setSem(i);
				semesterWiseFeeDatas.add(sem9);
				break;
			case 10:
				SemesterWiseFeeData sem10 = new SemesterWiseFeeData();
				sem10.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS10due()) ? studentDues.getS10due() : 0);
				sem10.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS10paid()) ? studentDues.getS10paid() : 0);
				sem10.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS10fxd()) ? studentDues.getS10fxd() : 0);
				sem10.setSem(i);
				semesterWiseFeeDatas.add(sem10);
				break;
			case 11:
				SemesterWiseFeeData sem11 = new SemesterWiseFeeData();
				sem11.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS11due()) ? studentDues.getS11due() : 0);
				sem11.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS11paid()) ? studentDues.getS11paid() : 0);
				sem11.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS11fxd()) ? studentDues.getS11fxd() : 0);
				sem11.setSem(i);
				semesterWiseFeeDatas.add(sem11);
				break;
			case 12:
				SemesterWiseFeeData sem12 = new SemesterWiseFeeData();
				sem12.setFeeDue(ObjectUtils.isNotEmpty(studentDues.getS12due()) ? studentDues.getS12due() : 0);
				sem12.setFeePaid(ObjectUtils.isNotEmpty(studentDues.getS12paid()) ? studentDues.getS12paid() : 0);
				sem12.setFeeFixed(ObjectUtils.isNotEmpty(studentDues.getS12fxd()) ? studentDues.getS12fxd() : 0);
				sem12.setSem(i);
				semesterWiseFeeDatas.add(sem12);
				break;
			default:
				break;
		}

	}

	public ResponseEntity<Object> getStudentDetailsDueForReportedAndEligibleIndex1(Pageable pageable,
																				   Integer ac_year_id) {
		Page<Object> response1 = studentDetailsRepository.getStudentDetailsDueForReportedAndEligibleIndex1(pageable,
				ac_year_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);

	}

	public ResponseEntity<Object> getStudentDetailsDueForReportedAndEligibleIndex2(Pageable pageable, Object keyword,
																				   Integer ac_year_id) {
		Page<Object> response = studentDetailsRepository.getStudentDetailsDueForReportedAndEligibleIndex2(pageable,
				keyword, ac_year_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> getStudentDetailsDueIndexForNotReportedAndNotEligibleIndex1(Pageable pageable,
																							  Integer ac_year_id) {
		Page<Object> response1 = studentDetailsRepository
				.getStudentDetailsDueIndexForNotReportedAndNotEligibleIndex1(pageable, ac_year_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);

	}

	public ResponseEntity<Object> getStudentDetailsDueIndexForNotReportedAndNotEligibleIndex2(Pageable pageable,
																							  Object keyword, Integer ac_year_id) {
		Page<Object> response = studentDetailsRepository
				.getStudentDetailsDueIndexForNotReportedAndNotEligibleIndex2(pageable, keyword, ac_year_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public List<Map<String, Object>> fetchStudentDetailForSectionAssignmentOnSem(Integer program_specialization_id,
																				 Integer current_sem) {
		return studentDetailsRepository.fetchStudentDetailForSectionAssignmentOnSem(program_specialization_id,
				current_sem);
	}

	public List<Map<String, Object>> fetchStudentDetailForSectionAssignmentOnYear(Integer program_specialization_id,
																				  Integer current_year) {
		return studentDetailsRepository.fetchStudentDetailForSectionAssignmentOnYear(program_specialization_id,
				current_year);
	}

	public List<Student_Details> studentDetailsByStudentIds(List<Integer> studentIds) {
		return studentDetailsRepository.findAllById(studentIds);
	}

	public List<TotalStudentCountAndFeeDueDto> getTotalStudentAndFeeDueForProgram(Integer programId,
																				  Integer categoryId) {
		List<TotalStudentCountAndFeeDueDto> studentList = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(programId)) {
			studentList = studentDueRepo.getTotalStudentAndFeeDueForProgram(programId);
		}
		if (ObjectUtils.isNotEmpty(categoryId)) {
			studentList = studentDueRepo.getTotalStudentAndFeeDueForCategory(categoryId);
		}
		return studentList;
	}

	public List<SumOfAllFromStudentDuesDto> getSumOfAllByCategoryId(Integer categoryId) {
		return studentDueRepo.getSumOfAllByCategoryId(categoryId);
	}

	public List<StudentListWithTotalDues> getStudentListWithTotalDues(Integer programId, Integer categoryId) {
		if (ObjectUtils.isNotEmpty(programId) && ObjectUtils.isEmpty(categoryId)) {
			return studentDueRepo.getStudentListWithTotalDues(programId).stream().filter(t -> t.getTotalDue() > 0)
					.collect(Collectors.toList());
		} else if (ObjectUtils.isNotEmpty(categoryId) && ObjectUtils.isEmpty(programId)) {
			return studentDueRepo.getStudentListWithTotalDuesForCategory(categoryId).stream()
					.filter(t -> t.getTotalDue() > 0).collect(Collectors.toList());
		}

		return null;
	}

	public List<Map<String, Object>> getCourseDetailData(Integer student_id) {
		return studentDetailsRepository.getCourseDetailData(student_id);
	}

	private static final String SEMESTER_PROGRAM_TYPE = "Semester";

	public List<StudenDetailsForIdCard> studenDetailsForIdCard(Integer schoolId, Integer programAssignmentId,
															   Integer programId, Integer programSpecializationId, Integer currentYearOrSem) {
		String programType = programAssigmentRepository.getProgramTypeByProgramAssignmentId(programAssignmentId);
		if (ObjectUtils.isNotEmpty(programType) && StringUtils.equalsIgnoreCase(programType, SEMESTER_PROGRAM_TYPE)) {
			return studentDetailsRepository.findStudentDetailsForIdCardBySem(schoolId, programAssignmentId, programId,
					programSpecializationId, currentYearOrSem);
		} else {
			return studentDetailsRepository.findStudentDetailsForIdCardByYear(schoolId, programAssignmentId, programId,
					programSpecializationId, currentYearOrSem);
		}
	}

	public Student_Details getSpotStuDetails(ApplicationDto app, @RequestHeader("Authorization") String jwtToken) {
		UserAuthentication creating_student_user_details = new UserAuthentication();
		Integer academicYear = app.getSd().getAc_year_id();
		if (academicYear > 6) {
			System.out.println(
					"------------------------this one where candidatewalkin data will insert----------------------- ");
			try {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				Optional<Program> program = programRepository.findById(app.getSd().getProgram_id());
				String program_code_for_auid = program.get().getProgram_code();
				if (program_code_for_auid == null) {
					throw new RuntimeException("Program Code IS NULL");
				}
				System.out.println("1111111111111111111111111111111111111111111111 ");
				Academic_year academicYearsDetails = academicYearRepository
						.findByAcademicId(app.getSd().getAc_year_id());
				if (academicYearsDetails == null) {
					throw new RuntimeException("Academic Year IS NULL");
				}
				System.out.println("22222222222222222222222222222222222222222222222222222 ");
				String ac_year_id = academicYearsDetails.getCurrent_year().toString().substring(2, 4);
				String specialization_auid = programSpecilizationRepository
						.getProgramAuid(app.getSd().getProgram_specialization_id());
				if (specialization_auid == null) {
					throw new RuntimeException("Specialization Auid Format IS NULL");
				}
				System.out.println("33333333333333333333333333333333333333333333333333333 ");
				Integer count_for_auid;
				String last_auid = studentDetailsRepository.getLastStudentDetailsByProgramSpecializationId(
						app.getSd().getAc_year_id(), app.getSd().getProgram_id(),
						app.getSd().getProgram_specialization_id());
				if (last_auid != null) {
					System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  " + last_auid.substring(9));
					count_for_auid = Integer.parseInt(last_auid.substring(9)) + 1;
				} else {
					count_for_auid = 1;
				}
				System.out.println("4444444444444444444444444444444444444444444444444 ");
				String schoolShortName = schoolRepository.getSchoolShortName(app.getSd().getSchool_id());
				String auid_formattedStrr = String.format("%03d", count_for_auid);
				String auid_format = schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr;

				String thisYear = String.valueOf(Year.now());
				String userName = null;
				if (thisYear.equalsIgnoreCase("2024")) {
					app.getSd().setAuid(app.getSd().getAuid());
					userName = app.getSd().getAuid();
				} else {
					app.getSd().setAuid(schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr);
					userName = schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr;
				}

				Integer master_code_count = studentDetailsRepository.maxStudentMasterCode();
				String formattedStrr = String.format("%04d", master_code_count + 1);
				app.getSd().setStudent_master_code("ASC" + formattedStrr);
				String acharya_email = null;

				if (thisYear.equalsIgnoreCase("2024")) {
					acharya_email = app.getSd().getAcharya_email();
				} else {
					acharya_email = app.getSd().getEmail_preferred_name().toLowerCase() + "_" + ac_year_id
							+ specialization_auid.toLowerCase() + "@acharya.ac.in";
				}

				app.getSd().setAcharya_email(acharya_email);

				Candidate_Walkin cw1 = new Candidate_Walkin();
				Integer current_year = academicYearRepository.getCurrentYear(app.getSd().getAc_year_id());
				String cw = candidateWalkinRepository.getApplicationNoNpf(app.getSd().getAc_year_id());
				String formattedStr1 = null;
				if (cw == null) {
					formattedStr1 = String.format("%04d", 1);
				} else {
					String countOfCW = cw.substring(6);
					Integer count = Integer.valueOf(countOfCW) + 1;
					formattedStr1 = String.format("%04d", count);
				}
				cw1.setApplication_no_npf("DA" + "/" + current_year.toString().substring(2) + "/" + formattedStr1);

				cw1.setAc_year_id(app.getCw().getAc_year_id());
				cw1.setActive(app.getCw().getActive());
				cw1.setCandidate_email(app.getCw().getCandidate_email());
				cw1.setCandidate_name(app.getCw().getCandidate_name());
				cw1.setCandidate_sex(app.getCw().getCandidate_sex());
				cw1.setCreated_by(jwtDetails.getUserId());
				cw1.setCreated_username(jwtDetails.getUserName());
//			cw1.setDate_of_admission(app.getCw().getDate_of_admission());
				cw1.setDate_of_birth(app.getCw().getDate_of_birth());
				cw1.setMarks_rank_obtain(app.getCw().getMarks_rank_obtain());
				cw1.setMobile_number(app.getCw().getMobile_number());
				cw1.setNationality(app.getCw().getNationality());
				cw1.setNpf_status(app.getCw().getNpf_status());
				cw1.setPermanent_address(app.getCw().getPermanent_address());
				cw1.setPermanent_city(app.getCw().getPermanent_city());
				cw1.setPermanent_country(app.getCw().getPermanent_country());
				cw1.setPermanent_state(app.getCw().getPermanent_state());
				cw1.setPresent_address(app.getCw().getPresent_address());
				cw1.setPresent_city_id(app.getCw().getPresent_city_id());
				cw1.setPresent_country(app.getCw().getPresent_country());
				cw1.setPresent_state(app.getCw().getPresent_state());
				cw1.setProgram_id(app.getCw().getProgram_id());
				cw1.setProgram_specilaization_id(app.getCw().getProgram_specilaization_id());
				cw1.setSchool_id(app.getCw().getSchool_id());
				cw1.setProgram_assignment_id(app.getCw().getProgram_assignment_id());
				cw1.setApplication_status(app.getCw().getApplication_status());
				cw1.setForm_filled_percentage(app.getCw().getForm_filled_percentage());
				cw1.setLead_status(app.getCw().getLead_status());
				cw1.setCounselor_id(app.getCw().getCounselor_id());
				cw1.setCounselor_name(app.getCw().getCounselor_name());
				cw1.setFather_name(app.getCw().getFather_name());
				cw1.setFather_mobile(app.getCw().getFather_mobile());
				cw1.setFather_annual_income(app.getCw().getFather_annual_income());
				cw1.setFather_email(app.getCw().getFather_email());
				cw1.setFather_occupation(app.getCw().getFather_occupation());
				cw1.setFather_qualification(app.getCw().getFather_qualification());
				cw1.setMother_name(app.getCw().getMother_name());
				cw1.setMother_mobile(app.getCw().getMother_mobile());
				cw1.setMother_annual_income(app.getCw().getMother_annual_income());
				cw1.setMother_email(app.getCw().getMother_email());
				cw1.setMother_occupation(app.getCw().getMother_occupation());
				cw1.setMother_qualification(app.getCw().getMother_qualification());
				cw1.setBlood_group(app.getCw().getBlood_group());
				cw1.setCandidate_sex(app.getCw().getCandidate_sex());
				cw1.setGuardian_name(app.getCw().getGuardian_name());
				cw1.setGuardian_mobile(app.getCw().getGuardian_mobile());
				cw1.setGuardian_email(app.getCw().getGuardian_email());
				StudentDueEvent studDue = new StudentDueEvent(null, null, app.getSd().getStudent_id(), null);
				applicationEventPublisher.publishEvent(studDue);
				System.out.println("----------------------------------------------------------------");
				candidateWalkinRepository.save(cw1);
				System.out.println(
						"-------------00000000000000000000000000000000000000000---------------------------------------------------");

//		StudentDues studentDues=studentDueRepository.callStudentDueDetails(st.getStudent_id());

				app.getSd().setCandidate_id(cw1.getCandidate_id());

				Student_Details createdStudentDetails = studentDetailsRepository.save(app.getSd());

				System.out.println("55555555555555555555555555555555555555555555555555555555555555 " + acharya_email);
				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String passwordEncoder1 = passwordEncoder.encode("acharya1234");

				creating_student_user_details.setUsername(userName);
				creating_student_user_details.setPassword(passwordEncoder1);
				creating_student_user_details.setEmail(acharya_email);
				creating_student_user_details.setUsertype("Student");
				creating_student_user_details.setCreated_by(jwtDetails.getUserId());
				creating_student_user_details.setCreated_username(jwtDetails.getUserName());
				creating_student_user_details.setActive(true);
				userAuthenticationRepository.save(creating_student_user_details);
				System.out.println("6666666666666666666666666666666666666666666666666666666666666 ");

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

				System.out.println("777777777777777777777777777777777777777777777777777777777777777 ");

				Student_Details st = getStudentByStudentId(app.getSd().getStudent_id());

				app.getAp().parallelStream().forEach(ap1 -> {
					ApplicantDetails ap = new ApplicantDetails();
					Student_Details st1 = get(app.getSd().getStudent_id());

					ap.setStd_id(st1.getStudent_id());
					ap.setCandidate_id(st1.getCandidate_id());
					ap.setAuid(st1.getAuid());

					ap.setBoard_university(ap1.getBoard_university());
					ap.setCollege_name(ap1.getCollege_name());
					ap.setCourse(ap1.getCourse());
					ap.setQualifying_exam_year(ap1.getQualifying_exam_year());
					ap.setSubjects_studied(ap1.getSubjects_studied());
					ap.setMarks_total(ap1.getMarks_total());
					ap.setTotal_obtained(ap1.getTotal_obtained());
					ap.setPercentage_scored(ap1.getPercentage_scored());
					ap.setApplicant_id(ap1.getApplicant_id());
					ap.setCreated_by(jwtDetails.getUserId());
					ap.setCreated_username(jwtDetails.getUserName());
					ap.setEntrance_exam_name(ap1.getEntrance_exam_name());
					ap.setEntrance_score(ap1.getEntrance_score());
					ap.setYear_of_entrance(ap1.getYear_of_entrance());
					ap.setFirst_language(ap1.getFirst_language());
					ap.setSecond_language(ap1.getSecond_language());
					ap.setPassed_year(ap1.getPassed_year());
					ap.setQualifying_year(ap1.getQualifying_year());
					ap.setState(ap1.getState());
					ap.setYear_of_entrance(ap1.getYear_of_entrance());
					ap.setRemarks(ap1.getRemarks());
					ap.setActive(ap1.getActive());
					applicantDetailsRepository.save(ap);
				});
				System.out.println("888888888888888888888888888888888888888888888888888888888888888888888 ");
				PGApplicable pg = new PGApplicable();
				pg.setStd_id(st.getStudent_id());
				pg.setCandidate_id(app.getSd().getCandidate_id());
				pg.setAuid(app.getSd().getAuid());

				pg.setUg_board(app.getPgapp().getUg_board());
				pg.setPg_total_percentage(app.getPgapp().getPg_total_percentage());
				pg.setCreated_by(jwtDetails.getUserId());
				pg.setCreated_username(jwtDetails.getUserName());
				pg.setPg_exam_passed_name(app.getPgapp().getPg_exam_passed_name());
				pg.setUniversity(app.getPgapp().getUniversity());
				pg.setYear_of_passing(app.getPgapp().getYear_of_passing());
				pg.setSubject_studied_lang(app.getPgapp().getSubject_studied_lang());
				pg.setYear_1(app.getPgapp().getYear_1());
				pg.setYear_2(app.getPgapp().getYear_2());
				pg.setYear_3(app.getPgapp().getYear_3());
				pg.setYear_4(app.getPgapp().getYear_4());
				pg.setMarks_total(app.getPgapp().getMarks_total());
				pg.setTotal_obtained(app.getPgapp().getTotal_obtained());
				pg.setCollege_name(app.getPgapp().getCollege_name());
				pg.setActive(app.getPgapp().getActive());
				pGApplicableRepository.save(pg);

				System.out.println(
						"99999999999999999999999999999999999999999999999999999999999999999999999999999999999 ");
				StdEntranceExam see = new StdEntranceExam();
				see.setStudent_id(st.getStudent_id());
				see.setEntrance_exam_name(app.getSee().getEntrance_exam_name());
				see.setState(app.getSee().getState());
				see.setYear_month1(app.getSee().getYear_month1());
				see.setScore(app.getSee().getScore());
				see.setCreated_by(jwtDetails.getUserId());
				see.setCreated_username(jwtDetails.getUserName());
				see.setActive(app.getSee().getActive());
				stdEntranceExamRepository.save(see);

				System.out
						.println("10101010100110011010101001101010011010011001010101011010101101001101010101010101010");
				app.getStreq().getTranscript_id().parallelStream().forEach(t -> {
					StudentTranscriptSubmission sts1 = new StudentTranscriptSubmission();

					LocalDate currentDate = LocalDate.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					String dateString = currentDate.format(formatter);

					Student_Details st2 = get(app.getSd().getStudent_id());
					sts1.setStudent_id(st2.getStudent_id());
					sts1.setActive(app.getStreq().getActive());
					sts1.setTranscript_id(t);
					sts1.setCreated_by(jwtDetails.getUserId());
					sts1.setCreated_username(jwtDetails.getUserName());
					sts1.setTranscript_locker_number(app.getStreq().getTranscript_locker_number());
					sts1.setIs_collected("YES");
					sts1.setSubmitted_date(dateString);
					sts1.setCollected_by(jwtDetails.getUserId());
					studentTranscriptSubmissionRepository.save(sts1);
				});
				System.out.println("12122121212121212121211221212121212121212121212121212121212121212121212121");
				app.getStreq().getNot_applicable().parallelStream().forEach(p -> {
					StudentTranscriptSubmission sts1 = new StudentTranscriptSubmission();
					Student_Details st2 = get(app.getSd().getStudent_id());

					sts1.setStudent_id(st2.getStudent_id());
					sts1.setActive(app.getStreq().getActive());
					sts1.setNot_applicable("YES");
					sts1.setTranscript_id(p);
					sts1.setCreated_by(jwtDetails.getUserId());
					sts1.setCreated_username(jwtDetails.getUserName());
					studentTranscriptSubmissionRepository.save(sts1);
				});
				app.getStreq().getSubmitted_date().entrySet().parallelStream().forEach(d -> {
					StudentTranscriptSubmission sts1 = new StudentTranscriptSubmission();
					Student_Details st3 = get(app.getSd().getStudent_id());

					sts1.setStudent_id(st3.getStudent_id());
					sts1.setActive(app.getStreq().getActive());
					sts1.setCreated_by(jwtDetails.getUserId());
					sts1.setCreated_username(jwtDetails.getUserName());
					sts1.setTranscript_id(d.getKey());
					sts1.setWill_submit_by(d.getValue());
					// sts1.setIs_collected("YES");
					studentTranscriptSubmissionRepository.save(sts1);

				});
				System.out.println("1313131313131313131313131313131313131313313131331313131313131133131 ");
				ReportingStudents rs1 = new ReportingStudents();
				rs1.setStudent_id(st.getStudent_id());
				rs1.setActive(app.getRs().getActive());
				rs1.setCreated_by(jwtDetails.getUserId());
				rs1.setModified_by(jwtDetails.getUserId());
				rs1.setCreated_username(jwtDetails.getUserName());
				rs1.setCurrent_sem(app.getRs().getCurrent_sem());
				rs1.setCurrent_year(app.getRs().getCurrent_year());
				rs1.setEligible_reported_status(app.getRs().getEligible_reported_status());
				rs1.setDistinct_status(app.getRs().getDistinct_status());
				rs1.setPrevious_sem(app.getRs().getPrevious_sem());
				rs1.setPrevious_year(app.getRs().getPrevious_year());
				rs1.setRemarks(app.getRs().getRemarks());
				rs1.setReported_ac_year_id(app.getRs().getReported_ac_year_id());
				rs1.setSection_id(app.getRs().getSection_id());
				rs1.setYear_back_status(app.getRs().getYear_back_status());
				reportingStudentsRepository.save(rs1);
				System.out.println(
						"**************************************************************************************************");
				updateCurrentYearAndSemAndStudentDetailToPhp(createdStudentDetails.getAuid(),
						createdStudentDetails.getStudent_id());
				return app.getSd();

			} catch (Exception e) {
//			userAuthenticationRepository.deleteById(creating_student_user_details.getId());
				throw new RuntimeException(
						"Check Program Code Or Specialization Auid Format IS NULL Or Role Name Student Not Found Or"
								+ e.getMessage());
			}
		} else {
			System.out.println(
					"------------------------this one where candidatewalkin data will not insert----------------------- ");
			try {
				JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
				Optional<Program> program = programRepository.findById(app.getSd().getProgram_id());
				String program_code_for_auid = program.get().getProgram_code();
				if (program_code_for_auid == null) {
					throw new RuntimeException("Program Code IS NULL");
				}
				System.out.println("1111111111111111111111111111111111111111111111 ");
				Academic_year academicYearsDetails = academicYearRepository
						.findByAcademicId(app.getSd().getAc_year_id());
				if (academicYearsDetails == null) {
					throw new RuntimeException("Academic Year IS NULL");
				}
				System.out.println("22222222222222222222222222222222222222222222222222222 ");
				String ac_year_id = academicYearsDetails.getCurrent_year().toString().substring(2, 4);
				String specialization_auid = programSpecilizationRepository
						.getProgramAuid(app.getSd().getProgram_specialization_id());
				if (specialization_auid == null) {
					throw new RuntimeException("Specialization Auid Format IS NULL");
				}
				System.out.println("33333333333333333333333333333333333333333333333333333 ");
				Integer count_for_auid;
				String last_auid = studentDetailsRepository.getLastStudentDetailsByProgramSpecializationId(
						app.getSd().getAc_year_id(), app.getSd().getProgram_id(),
						app.getSd().getProgram_specialization_id());
				if (last_auid != null) {
					System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  " + last_auid.substring(9));
					count_for_auid = Integer.parseInt(last_auid.substring(9)) + 1;
				} else {
					count_for_auid = 1;
				}
				System.out.println("4444444444444444444444444444444444444444444444444 ");
				String schoolShortName = schoolRepository.getSchoolShortName(app.getSd().getSchool_id());
				String auid_formattedStrr = String.format("%03d", count_for_auid);
				String auid_format = schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr;

				String thisYear = String.valueOf(Year.now());
				String userName = null;
				if (thisYear.equalsIgnoreCase("2024")) {
					app.getSd().setAuid(app.getSd().getAuid());
					userName = app.getSd().getAuid();
				} else {
					app.getSd().setAuid(schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr);
					userName = schoolShortName + ac_year_id + specialization_auid + auid_formattedStrr;
				}

				Integer master_code_count = studentDetailsRepository.maxStudentMasterCode();
				String formattedStrr = String.format("%04d", master_code_count + 1);
				app.getSd().setStudent_master_code("ASC" + formattedStrr);
				String acharya_email = null;

				if (thisYear.equalsIgnoreCase("2024")) {
					acharya_email = app.getSd().getAcharya_email();
				} else {
					acharya_email = app.getSd().getEmail_preferred_name().toLowerCase() + "_" + ac_year_id
							+ specialization_auid.toLowerCase() + "@acharya.ac.in";
				}

				app.getSd().setAcharya_email(acharya_email);

				System.out.println(
						"-------------00000000000000000000000000000000000000000---------------------------------------------------");

				Student_Details createdStudentDetails = studentDetailsRepository.save(app.getSd());

				System.out.println("55555555555555555555555555555555555555555555555555555555555555 " + acharya_email);
				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String passwordEncoder1 = passwordEncoder.encode("acharya1234");

				creating_student_user_details.setUsername(userName);
				creating_student_user_details.setPassword(passwordEncoder1);
				creating_student_user_details.setEmail(acharya_email);
				creating_student_user_details.setUsertype("Student");
				creating_student_user_details.setCreated_by(jwtDetails.getUserId());
				creating_student_user_details.setCreated_username(jwtDetails.getUserName());
				creating_student_user_details.setActive(true);
				userAuthenticationRepository.save(creating_student_user_details);
				System.out.println("6666666666666666666666666666666666666666666666666666666666666 ");

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

				System.out.println("777777777777777777777777777777777777777777777777777777777777777 ");

				Student_Details st = getStudentByStudentId(app.getSd().getStudent_id());

				app.getAp().parallelStream().forEach(ap1 -> {
					ApplicantDetails ap = new ApplicantDetails();
					Student_Details st1 = get(app.getSd().getStudent_id());

					ap.setStd_id(st1.getStudent_id());
					ap.setCandidate_id(st1.getCandidate_id());
					ap.setAuid(st1.getAuid());

					ap.setBoard_university(ap1.getBoard_university());
					ap.setCollege_name(ap1.getCollege_name());
					ap.setCourse(ap1.getCourse());
					ap.setQualifying_exam_year(ap1.getQualifying_exam_year());
					ap.setSubjects_studied(ap1.getSubjects_studied());
					ap.setMarks_total(ap1.getMarks_total());
					ap.setTotal_obtained(ap1.getTotal_obtained());
					ap.setPercentage_scored(ap1.getPercentage_scored());
					ap.setApplicant_id(ap1.getApplicant_id());
					ap.setCreated_by(jwtDetails.getUserId());
					ap.setCreated_username(jwtDetails.getUserName());
					ap.setEntrance_exam_name(ap1.getEntrance_exam_name());
					ap.setEntrance_score(ap1.getEntrance_score());
					ap.setYear_of_entrance(ap1.getYear_of_entrance());
					ap.setFirst_language(ap1.getFirst_language());
					ap.setSecond_language(ap1.getSecond_language());
					ap.setPassed_year(ap1.getPassed_year());
					ap.setQualifying_year(ap1.getQualifying_year());
					ap.setState(ap1.getState());
					ap.setYear_of_entrance(ap1.getYear_of_entrance());
					ap.setRemarks(ap1.getRemarks());
					ap.setActive(ap1.getActive());
					applicantDetailsRepository.save(ap);
				});
				System.out.println("888888888888888888888888888888888888888888888888888888888888888888888 ");
				PGApplicable pg = new PGApplicable();
				pg.setStd_id(st.getStudent_id());
				pg.setCandidate_id(app.getSd().getCandidate_id());
				pg.setAuid(app.getSd().getAuid());

				pg.setUg_board(app.getPgapp().getUg_board());
				pg.setPg_total_percentage(app.getPgapp().getPg_total_percentage());
				pg.setCreated_by(jwtDetails.getUserId());
				pg.setCreated_username(jwtDetails.getUserName());
				pg.setPg_exam_passed_name(app.getPgapp().getPg_exam_passed_name());
				pg.setUniversity(app.getPgapp().getUniversity());
				pg.setYear_of_passing(app.getPgapp().getYear_of_passing());
				pg.setSubject_studied_lang(app.getPgapp().getSubject_studied_lang());
				pg.setYear_1(app.getPgapp().getYear_1());
				pg.setYear_2(app.getPgapp().getYear_2());
				pg.setYear_3(app.getPgapp().getYear_3());
				pg.setYear_4(app.getPgapp().getYear_4());
				pg.setMarks_total(app.getPgapp().getMarks_total());
				pg.setTotal_obtained(app.getPgapp().getTotal_obtained());
				pg.setCollege_name(app.getPgapp().getCollege_name());
				pg.setActive(app.getPgapp().getActive());
				pGApplicableRepository.save(pg);

				System.out.println(
						"99999999999999999999999999999999999999999999999999999999999999999999999999999999999 ");
				StdEntranceExam see = new StdEntranceExam();
				see.setStudent_id(st.getStudent_id());
				see.setEntrance_exam_name(app.getSee().getEntrance_exam_name());
				see.setState(app.getSee().getState());
				see.setYear_month1(app.getSee().getYear_month1());
				see.setScore(app.getSee().getScore());
				see.setCreated_by(jwtDetails.getUserId());
				see.setCreated_username(jwtDetails.getUserName());
				see.setActive(app.getSee().getActive());
				stdEntranceExamRepository.save(see);

				System.out
						.println("10101010100110011010101001101010011010011001010101011010101101001101010101010101010");
				LocalDate currentDate = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				String dateString = currentDate.format(formatter);
				app.getStreq().getTranscript_id().parallelStream().forEach(t -> {
					StudentTranscriptSubmission sts1 = new StudentTranscriptSubmission();

					Student_Details st2 = get(app.getSd().getStudent_id());
					sts1.setStudent_id(st2.getStudent_id());
					sts1.setActive(app.getStreq().getActive());
					sts1.setTranscript_id(t);
					sts1.setCreated_by(jwtDetails.getUserId());
					sts1.setCreated_username(jwtDetails.getUserName());
					sts1.setTranscript_locker_number(app.getStreq().getTranscript_locker_number());
					sts1.setIs_collected("YES");
					sts1.setSubmitted_date(dateString);
					sts1.setCollected_by(jwtDetails.getUserId());
					studentTranscriptSubmissionRepository.save(sts1);
				});
				System.out.println("12122121212121212121211221212121212121212121212121212121212121212121212121");
				app.getStreq().getNot_applicable().parallelStream().forEach(p -> {
					StudentTranscriptSubmission sts1 = new StudentTranscriptSubmission();
					Student_Details st2 = get(app.getSd().getStudent_id());

					sts1.setStudent_id(st2.getStudent_id());
					sts1.setActive(app.getStreq().getActive());
					sts1.setNot_applicable("YES");
					sts1.setTranscript_id(p);
					sts1.setCreated_by(jwtDetails.getUserId());
					sts1.setCreated_username(jwtDetails.getUserName());
					studentTranscriptSubmissionRepository.save(sts1);
				});
				app.getStreq().getSubmitted_date().entrySet().parallelStream().forEach(d -> {
					StudentTranscriptSubmission sts1 = new StudentTranscriptSubmission();
					Student_Details st3 = get(app.getSd().getStudent_id());

					sts1.setStudent_id(st3.getStudent_id());
					sts1.setActive(app.getStreq().getActive());
					sts1.setCreated_by(jwtDetails.getUserId());
					sts1.setCreated_username(jwtDetails.getUserName());
					sts1.setTranscript_id(d.getKey());
					sts1.setWill_submit_by(d.getValue());
					// sts1.setIs_collected("YES");
					studentTranscriptSubmissionRepository.save(sts1);

				});
				System.out.println("1313131313131313131313131313131313131313313131331313131313131133131 ");
				ReportingStudents rs1 = new ReportingStudents();
				rs1.setStudent_id(st.getStudent_id());
				rs1.setActive(app.getRs().getActive());
				rs1.setCreated_by(jwtDetails.getUserId());
				rs1.setCreated_username(jwtDetails.getUserName());
				rs1.setCurrent_sem(app.getRs().getCurrent_sem());
				rs1.setCurrent_year(app.getRs().getCurrent_year());
				rs1.setEligible_reported_status(app.getRs().getEligible_reported_status());
				rs1.setDistinct_status(app.getRs().getDistinct_status());
				rs1.setPrevious_sem(app.getRs().getPrevious_sem());
				rs1.setPrevious_year(app.getRs().getPrevious_year());
				rs1.setRemarks(app.getRs().getRemarks());
				rs1.setReported_ac_year_id(app.getRs().getReported_ac_year_id());
				rs1.setSection_id(app.getRs().getSection_id());
				rs1.setYear_back_status(app.getRs().getYear_back_status());
				reportingStudentsRepository.save(rs1);
				System.out.println(
						"**************************************************************************************************");
				updateCurrentYearAndSemAndStudentDetailToPhp(createdStudentDetails.getAuid(),
						createdStudentDetails.getStudent_id());
				return app.getSd();

			} catch (Exception e) {
//				userAuthenticationRepository.deleteById(creating_student_user_details.getId());
				throw new RuntimeException(
						"Check Program Code Or Specialization Auid Format IS NULL Or Role Name Student Not Found Or"
								+ e.getMessage());
			}
		}
	}

	public void updateCurrentYearAndSemAndStudentDetailToPhp(String auid, Integer studentId) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(PHP_URL_FOR_STUDENT_DETAILS + auid,
				HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {
				});
		System.out.println(responseEntity.getBody());
		Map<String, Object> apiResponse = responseEntity.getBody();
		System.out.println(apiResponse);
		if (apiResponse != null && apiResponse.get("success") != null && (Boolean) apiResponse.get("success")) {
			Map<String, Object> data = (Map<String, Object>) apiResponse.get("data");

			if (data.get("current_year") != null && data.get("current_sem") != null) {
				Integer currentYear = Integer.parseInt((String) data.get("current_year"));
				Integer currentSem = Integer.parseInt((String) data.get("current_sem"));
				reportingStudentsRepository.updateCurrentYearOrSem(currentYear, currentSem, studentId);
			}
			if (data.get("usn") != null) {
				String usn = data.get("usn").toString();
				studentDetailsRepository.updateUsnOfStudentByStudentId(usn, studentId);
			}
		}

	}

	public String checkPreferredNameForEmail(String email_preferred_name) {
		if (studentDetailsRepository.getCountOfPreferredName(email_preferred_name) >= 1) {
			throw new RuntimeException("Email preferred name is already exit !!!");
		}
		String name = email_preferred_name;
		return name;
	}

	public ResponseEntity<Object> getStudentIndex11(Pageable pageable1, Integer ac_year_id, Integer school_id,
													Integer program_id, Integer program_specialization_id, Integer fee_admission_category_id, Integer userId) {
		Page<Object> response1 = studentDetailsRepository.getStudentIndex1(pageable1, ac_year_id, school_id, program_id,
				program_specialization_id, fee_admission_category_id, userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> getStudentIndex2(Pageable pageable, Object keyword, Integer ac_year_id,
												   Integer school_id, Integer program_id, Integer program_specialization_id, Integer fee_admission_category_id,
												   Integer userId) {
		Page<Object> response = studentDetailsRepository.getStudentIndex2(pageable, keyword, ac_year_id, school_id,
				program_id, program_specialization_id, fee_admission_category_id, userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> getStudentIndexCustomExpoet(Pageable pageable, Object keyword, Integer ac_year_id) {
		Page<Object> response = studentDetailsRepository.getStudentIndexCustomExpoet(pageable, keyword, ac_year_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> getStudentIndexCustomExpoet1(Pageable pageable1, Integer ac_year_id) {
		Page<Object> response1 = studentDetailsRepository.getStudentIndexCustomExpoet1(pageable1, ac_year_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public String checkAuidIsAlreadyPresentOrNot(String auid) {
		if (studentDetailsRepository.checkAuidIsAlreadyPresentOrNot(auid) >= 1) {
			throw new RuntimeException("Auid already exist !!!");
		}

		return auid;
	}

	public Student_Details updateUsnDetailsData(EmployeeUsnUpdateDto dto, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);

		Student_Details studentDetail = studentDetailsRepository.getStudentDetailsData(dto.getAuid())
				.orElseThrow(() -> new ResourceNotFoundException("Auid is not present"));

		if (studentDetailsRepository.checkUsn(dto.getUsn()) >= 1) {
			throw new RuntimeException("Usn already exist !!!");
		}

		studentDetail.setUsn(dto.getUsn());
		studentDetail.setModified_by(jwtDetails.getUserId());
		studentDetail.setModified_username(jwtDetails.getUserName());
		return studentDetailsRepository.save(studentDetail);
	}

	public ResponseEntity<Object> studentDueReportTrigger(Integer schoolId, Integer programId, Integer studentId) {
		try {
			StudentDueEvent studentDueEvent = null;
			if (ObjectUtils.isNotEmpty(studentId)) {
				studentDueEvent = new StudentDueEvent(null, null, studentId, null);
			} else if (ObjectUtils.isNotEmpty(schoolId)) {
				studentDueEvent = new StudentDueEvent(schoolId, null, null, null);
			} else if (ObjectUtils.isNotEmpty(programId)) {
				studentDueEvent = new StudentDueEvent(null, programId, null, null);
			} else {
				studentDueEvent = new StudentDueEvent(null, null, null, "studentDue");
			}
			applicationEventPublisher.publishEvent(studentDueEvent);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "Student trigger started", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);
		}
	}

	public List<Map<String, Object>> getStudentDetailsBasedOnAuid(String auid) {
		return studentDetailsRepository.getStudentDetailsBasedOnStrudentId(auid);
	}

	public List<Map<String, Object>> getStudentDetailsBasedOnStrudentId(Integer student_id) {
		return studentDetailsRepository.getStudentDetailsBasedOnStrudentId(student_id);

	}

	public ResponseEntity<Object> studentFeetemplate(Integer studentId) {
		try {
			StudentFeeTemplateDTO studentFeeTemplateDTO = studentDueRepository.getStudentDetails(studentId);

			List<FeeTemplateDTO> feeTemplateList = feeTemplateSubAmountRepository
					.getFeeTemplateDetails(studentFeeTemplateDTO.getTemplateId());
			OtherFeeTemplateForStudentDTO addOnProgramFeeList = otherFeeDetailsRepository.getAddOnProgramFeeDetails(
					studentFeeTemplateDTO.getSchoolId(), studentFeeTemplateDTO.getAcYearId(),
					studentFeeTemplateDTO.getProgramId(), studentFeeTemplateDTO.getProgramSpecilizationId());
			OtherFeeTemplateForStudentDTO uniformFeeList = otherFeeDetailsRepository.getUniformFeeDetails(
					studentFeeTemplateDTO.getSchoolId(), studentFeeTemplateDTO.getAcYearId(),
					studentFeeTemplateDTO.getProgramId(), studentFeeTemplateDTO.getProgramSpecilizationId());
			studentFeeTemplateDTO.setFeeTemplateList(feeTemplateList);
			studentFeeTemplateDTO.setAddOnProgramFeeList(addOnProgramFeeList);
			studentFeeTemplateDTO.setUniformFeeList(uniformFeeList);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentFeeTemplateDTO);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);
		}
	}

	public ResponseEntity<Object> schoolWiseDueReport(JwtDetails jwtDetails) {
		try {
			List<Schools> schools = new ArrayList<>();
			Integer userId = jwtDetails.getUserId();
			String email = userAuthRepository.getUserEmailByUserId(userId);
			EmployeeDetails employee = employeeDetailsRepository.findByEmailAndActiveTrue(email);
			if (ObjectUtils.isNotEmpty(employee)) {
				Schools school = schoolRepository.getSchoolBySchoolId(employee.getSchool_id());
				if (school != null) {
					schools.add(school);
				}
			}

			return fetchSchoolWiseDueReport(schools);
			//return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", schoolWiseDueReport);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);
		}
	}


	public ResponseEntity<Object> fetchSchoolWiseDueReport(List<Schools> schools) {
		try {

			SchoolWiseDueReport schoolWiseDueReport = new SchoolWiseDueReport();

			List<SchoolWiseDueReportList> schoolWiseDueReports = new ArrayList<>();

			schools.stream().forEach(sc -> {
				SchoolWiseDueReportList schoolWiseDueReportList = new SchoolWiseDueReportList();
				schoolWiseDueReportList.setSchoolId(sc.getSchool_id());
				schoolWiseDueReportList.setSchoolName(sc.getSchool_name());
				schoolWiseDueReportList.setSchoolShortName(sc.getSchool_name_short());
				Double dueTotal = studentDueRepository.getSumOfSchoolDue(sc.getSchool_id());
				Double addOn = otherFeeDetailsRepository.getSumOfAddOn(sc.getSchool_id());
				Double totalHostelDue = hostelDueRepository.getSumOfHostelDue(sc.getSchool_id());
				dueTotal = ObjectUtils.isNotEmpty(dueTotal) ? dueTotal : 0.0d;
				addOn = ObjectUtils.isNotEmpty(addOn) ? addOn : 0.0d;
				totalHostelDue = ObjectUtils.isNotEmpty(totalHostelDue) ? totalHostelDue : 0.0d;
				BigDecimal total = toBigDecimal(dueTotal).add(toBigDecimal(addOn)).add(toBigDecimal(totalHostelDue));
				schoolWiseDueReportList.setAddOn(toBigDecimal(addOn));
				schoolWiseDueReportList.setCollegeDue(toBigDecimal(dueTotal));
				schoolWiseDueReportList.setHostelFee(toBigDecimal(totalHostelDue));
				schoolWiseDueReportList.setTotal(total);
				schoolWiseDueReports.add(schoolWiseDueReportList);

			});

			BigDecimal grantTotalAddon = schoolWiseDueReports.stream()
					.map(SchoolWiseDueReportList::getAddOn)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal grantTotalDue = schoolWiseDueReports.stream()
					.map(SchoolWiseDueReportList::getCollegeDue)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal grantTotalHostelDue = schoolWiseDueReports.stream()
					.map(SchoolWiseDueReportList::getHostelFee)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			BigDecimal grantTotal = schoolWiseDueReports.stream()
					.map(SchoolWiseDueReportList::getTotal)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			schoolWiseDueReport.setSchoolWiseDueReportLists(schoolWiseDueReports);
			schoolWiseDueReport.setGrantAddOnTotal(grantTotalAddon);
			schoolWiseDueReport.setGrantDueTotal(grantTotalDue);
			schoolWiseDueReport.setGrantHostelFeeTotal(grantTotalHostelDue);
			schoolWiseDueReport.setGrantTotal(grantTotal);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", schoolWiseDueReport);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);
		}
	}

	public ResponseEntity<Object> branchWiseDueReport(Integer schoolId) {
		try {
			List<BranchWisedueReport> branchWisedueReport = new ArrayList<>();
			List<ProgramAssigment> programAssigments = programAssigmentRepository.getProgramsBySchoolId(schoolId);
			BranchDueReport branchDueReport = new BranchDueReport();
			BigDecimal[] grandTotal = {BigDecimal.ZERO};
			BigDecimal[] sem1Total = {BigDecimal.ZERO};
			BigDecimal[] sem2Total = {BigDecimal.ZERO};
			BigDecimal[] sem3Total = {BigDecimal.ZERO};
			BigDecimal[] sem4Total = {BigDecimal.ZERO};
			BigDecimal[] sem5Total = {BigDecimal.ZERO};
			BigDecimal[] sem6Total = {BigDecimal.ZERO};
			BigDecimal[] sem7Total = {BigDecimal.ZERO};
			BigDecimal[] sem8Total = {BigDecimal.ZERO};
			BigDecimal[] sem9Total = {BigDecimal.ZERO};
			BigDecimal[] sem10Total = {BigDecimal.ZERO};
			BigDecimal[] sem11Total = {BigDecimal.ZERO};
			BigDecimal[] sem12Total = {BigDecimal.ZERO};
			BigDecimal[] totalHostelDue = {BigDecimal.ZERO};

			programAssigments.stream().forEach(p -> {

				Map<String, Object> program = programRepository.getProgramDetails(p.getProgram_id());
				Integer programId = (Integer) program.get("programId");
				String programName = (String) program.get("programShortName");
				List<Map<String, Object>> programSpecializationDetails = programSpecilizationRepository
						.getProgramSpecializationDetails(programId, schoolId);
				programSpecializationDetails.stream().forEach(ps -> {
					BranchWisedueReport branchWiseReport = new BranchWisedueReport();
					Integer programSpecializationId = (Integer) ps.get("programSpecializationId");
					String programSpecializationName = (String) ps.get("programSpecializationShortName");
					branchWiseReport.setProgramSpecialization(programSpecializationName);
					branchWiseReport.setProgramSpecializationId(programSpecializationId);
					branchWiseReport.setProgram(programName);
					branchWiseReport.setProgramId(programId);
					branchWiseReport.setNumberOfSemester(p.getNumber_of_semester());
					Map<String, Object> dueReport = studentDueRepository
							.getProgramSpecializationWiseSemesterSum(programSpecializationId);
					Double semDue1 = ObjectUtils.isNotEmpty(dueReport.get("sem1")) ? (Double) dueReport.get("sem1") : 0;
					Double semDue2 = ObjectUtils.isNotEmpty(dueReport.get("sem2")) ? (Double) dueReport.get("sem2") : 0;
					Double semDue3 = ObjectUtils.isNotEmpty(dueReport.get("sem3")) ? (Double) dueReport.get("sem3") : 0;
					Double semDue4 = ObjectUtils.isNotEmpty(dueReport.get("sem4")) ? (Double) dueReport.get("sem4") : 0;
					Double semDue5 = ObjectUtils.isNotEmpty(dueReport.get("sem5")) ? (Double) dueReport.get("sem5") : 0;
					Double semDue6 = ObjectUtils.isNotEmpty(dueReport.get("sem6")) ? (Double) dueReport.get("sem6") : 0;
					Double semDue7 = ObjectUtils.isNotEmpty(dueReport.get("sem7")) ? (Double) dueReport.get("sem7") : 0;
					Double semDue8 = ObjectUtils.isNotEmpty(dueReport.get("sem8")) ? (Double) dueReport.get("sem8") : 0;
					Double semDue9 = ObjectUtils.isNotEmpty(dueReport.get("sem9")) ? (Double) dueReport.get("sem9") : 0;
					Double semDue10 = ObjectUtils.isNotEmpty(dueReport.get("sem10")) ? (Double) dueReport.get("sem10")
							: 0;
					Double semDue11 = ObjectUtils.isNotEmpty(dueReport.get("sem11")) ? (Double) dueReport.get("sem11")
							: 0;
					Double semDue12 = ObjectUtils.isNotEmpty(dueReport.get("sem12")) ? (Double) dueReport.get("sem12")
							: 0;
//					Map<String, Object> otherFeeReport = otherFeeDetailsRepository
//							.getAddOnProgramFeeDetailsSemesterWise(programSpecializationId);
//					Double semOtherFee1 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem1"))
//							? (Double) otherFeeReport.get("sem1")
//							: 0;
//					Double semOtherFee2 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem2"))
//							? (Double) otherFeeReport.get("sem2")
//							: 0;
//					Double semOtherFee3 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem3"))
//							? (Double) otherFeeReport.get("sem3")
//							: 0;
//					Double semOtherFee4 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem4"))
//							? (Double) otherFeeReport.get("sem4")
//							: 0;
//					Double semOtherFee5 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem5"))
//							? (Double) otherFeeReport.get("sem5")
//							: 0;
//					Double semOtherFee6 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem6"))
//							? (Double) otherFeeReport.get("sem6")
//							: 0;
//					Double semOtherFee7 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem7"))
//							? (Double) otherFeeReport.get("sem7")
//							: 0;
//					Double semOtherFee8 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem8"))
//							? (Double) otherFeeReport.get("sem8")
//							: 0;
//					Double semOtherFee9 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem9"))
//							? (Double) otherFeeReport.get("sem9")
//							: 0;
//					Double semOtherFee10 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem10"))
//							? (Double) otherFeeReport.get("sem10")
//							: 0;
//					Double semOtherFee11 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem11"))
//							? (Double) otherFeeReport.get("sem11")
//							: 0;
//					Double semOtherFee12 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem12"))
//							? (Double) otherFeeReport.get("sem12")
//							: 0;
					Map<String, Object> otherFeeReport = studentDueRepository
							.getProgramSpecializationWiseSemesterAddOnSum(programSpecializationId);
//					Map<String, Object> otherFeeReport = otherFeeDetailsRepository
//							.getAddOnProgramFeeDetailsSemesterWise(programSpecializationId);
					Double semOtherFee1 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem1"))
							? (Double) otherFeeReport.get("sem1")
							: 0;
					Double semOtherFee2 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem2"))
							? (Double) otherFeeReport.get("sem2")
							: 0;
					Double semOtherFee3 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem3"))
							? (Double) otherFeeReport.get("sem3")
							: 0;
					Double semOtherFee4 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem4"))
							? (Double) otherFeeReport.get("sem4")
							: 0;
					Double semOtherFee5 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem5"))
							? (Double) otherFeeReport.get("sem5")
							: 0;
					Double semOtherFee6 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem6"))
							? (Double) otherFeeReport.get("sem6")
							: 0;
					Double semOtherFee7 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem7"))
							? (Double) otherFeeReport.get("sem7")
							: 0;
					Double semOtherFee8 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem8"))
							? (Double) otherFeeReport.get("sem8")
							: 0;
					Double semOtherFee9 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem9"))
							? (Double) otherFeeReport.get("sem9")
							: 0;
					Double semOtherFee10 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem10"))
							? (Double) otherFeeReport.get("sem10")
							: 0;
					Double semOtherFee11 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem11"))
							? (Double) otherFeeReport.get("sem11")
							: 0;
					Double semOtherFee12 = ObjectUtils.isNotEmpty(otherFeeReport.get("sem12"))
							? (Double) otherFeeReport.get("sem12")
							: 0;

					BigDecimal sem1 = BigDecimal.valueOf(semDue1).add(BigDecimal.valueOf(semOtherFee1)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem2 = BigDecimal.valueOf(semDue2).add(BigDecimal.valueOf(semOtherFee2)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem3 = BigDecimal.valueOf(semDue3).add(BigDecimal.valueOf(semOtherFee3)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem4 = BigDecimal.valueOf(semDue4).add(BigDecimal.valueOf(semOtherFee4)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem5 = BigDecimal.valueOf(semDue5).add(BigDecimal.valueOf(semOtherFee5)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem6 = BigDecimal.valueOf(semDue6).add(BigDecimal.valueOf(semOtherFee6)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem7 = BigDecimal.valueOf(semDue7).add(BigDecimal.valueOf(semOtherFee7)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem8 = BigDecimal.valueOf(semDue8).add(BigDecimal.valueOf(semOtherFee8)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem9 = BigDecimal.valueOf(semDue9).add(BigDecimal.valueOf(semOtherFee9)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem10 = BigDecimal.valueOf(semDue10).add(BigDecimal.valueOf(semOtherFee10)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem11 = BigDecimal.valueOf(semDue11).add(BigDecimal.valueOf(semOtherFee11)).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sem12 = BigDecimal.valueOf(semDue12).add(BigDecimal.valueOf(semOtherFee12)).setScale(2, RoundingMode.HALF_UP);

					branchWiseReport.setSem1(ObjectUtils.isNotEmpty(sem1) ? sem1 : BigDecimal.valueOf(0));
					branchWiseReport.setSem2(ObjectUtils.isNotEmpty(sem2) ? sem2 : BigDecimal.valueOf(0));

					branchWiseReport.setSem3(ObjectUtils.isNotEmpty(sem3) ? sem3 : BigDecimal.valueOf(0));

					branchWiseReport.setSem4(ObjectUtils.isNotEmpty(sem4) ? sem4 : BigDecimal.valueOf(0));

					branchWiseReport.setSem5(ObjectUtils.isNotEmpty(sem5) ? sem5 : BigDecimal.valueOf(0));

					branchWiseReport.setSem6(ObjectUtils.isNotEmpty(sem6) ? sem6 : BigDecimal.valueOf(0));

					branchWiseReport.setSem7(ObjectUtils.isNotEmpty(sem7) ? sem7 : BigDecimal.valueOf(0));

					branchWiseReport.setSem8(ObjectUtils.isNotEmpty(sem8) ? sem8 : BigDecimal.valueOf(0));

					branchWiseReport.setSem9(ObjectUtils.isNotEmpty(sem9) ? sem9 : BigDecimal.valueOf(0));

					branchWiseReport.setSem10(ObjectUtils.isNotEmpty(sem10) ? sem10 : BigDecimal.valueOf(0));

					branchWiseReport.setSem11(ObjectUtils.isNotEmpty(sem11) ? sem11 : BigDecimal.valueOf(0));

					branchWiseReport.setSem12(ObjectUtils.isNotEmpty(sem12) ? sem12 : BigDecimal.valueOf(0));

					Double hostelDue = hostelDueRepository.getSumOfHostelDueByProgramSpecializationId(programSpecializationId);

					BigDecimal hosDue = ObjectUtils.isNotEmpty(hostelDue) ? BigDecimal.valueOf(hostelDue).setScale(2, RoundingMode.HALF_UP) : BigDecimal.valueOf(0);
					branchWiseReport.setHostelDue(hosDue);

					BigDecimal total = sem1.add(sem2).add(sem3).add(sem4).add(sem5).add(sem6)
							.add(sem7).add(sem8).add(sem9).add(sem10).add(sem11).add(sem12).add(hosDue)
							.setScale(2, RoundingMode.HALF_UP);

					grandTotal[0] = grandTotal[0].add(total);
					sem1Total[0] = sem1Total[0].add(sem1);
					sem2Total[0] = sem2Total[0].add(sem2);
					sem3Total[0] = sem3Total[0].add(sem3);
					sem4Total[0] = sem4Total[0].add(sem4);
					sem5Total[0] = sem5Total[0].add(sem5);
					sem6Total[0] = sem6Total[0].add(sem6);
					sem7Total[0] = sem7Total[0].add(sem7);
					sem8Total[0] = sem8Total[0].add(sem8);
					sem9Total[0] = sem9Total[0].add(sem9);
					sem10Total[0] = sem10Total[0].add(sem10);
					sem11Total[0] = sem10Total[0].add(sem11);
					sem12Total[0] = sem12Total[0].add(sem12);
					totalHostelDue[0] = totalHostelDue[0].add(hosDue);
					branchWiseReport.setTotal(total);
					branchWisedueReport.add(branchWiseReport);

				});

			});

			branchDueReport.setGrandTotal(grandTotal[0]);
			branchDueReport.setTotalSem1(sem1Total[0]);
			branchDueReport.setTotalSem2(sem2Total[0]);
			branchDueReport.setTotalSem3(sem3Total[0]);
			branchDueReport.setTotalSem4(sem4Total[0]);
			branchDueReport.setTotalSem5(sem5Total[0]);
			branchDueReport.setTotalSem6(sem6Total[0]);
			branchDueReport.setTotalSem7(sem7Total[0]);
			branchDueReport.setTotalSem8(sem8Total[0]);
			branchDueReport.setTotalSem9(sem9Total[0]);
			branchDueReport.setTotalSem10(sem10Total[0]);
			branchDueReport.setTotalSem11(sem11Total[0]);
			branchDueReport.setTotalSem12(sem12Total[0]);
			branchDueReport.setTotalHostelDue(totalHostelDue[0]);
			branchDueReport.setBranchWisedueReports(branchWisedueReport);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", branchDueReport);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);
		}
	}

	public ResponseEntity<Object> studentWiseDueReport(Integer sem, Integer sId, Integer pId, Integer programSpecializationId, Integer pageSize,
													   Integer offset) {
		try {

			StudentDueReport studentWiseDueReport = new StudentDueReport();
			List<StudentWiseDueReport> studentDetails = studentDetailsRepository
					.getStudentDetailsForDueReportBySchoolIdAndProgramId(sId, pId, programSpecializationId);

			BigDecimal[] grandTotal = {BigDecimal.ZERO};
			BigDecimal[] sem1Total = {BigDecimal.ZERO};
			BigDecimal[] sem2Total = {BigDecimal.ZERO};
			BigDecimal[] sem3Total = {BigDecimal.ZERO};
			BigDecimal[] sem4Total = {BigDecimal.ZERO};
			BigDecimal[] sem5Total = {BigDecimal.ZERO};
			BigDecimal[] sem6Total = {BigDecimal.ZERO};
			BigDecimal[] sem7Total = {BigDecimal.ZERO};
			BigDecimal[] sem8Total = {BigDecimal.ZERO};
			BigDecimal[] sem9Total = {BigDecimal.ZERO};
			BigDecimal[] sem10Total = {BigDecimal.ZERO};
			BigDecimal[] sem11Total = {BigDecimal.ZERO};
			BigDecimal[] sem12Total = {BigDecimal.ZERO};
			BigDecimal[] addOnTotal = {BigDecimal.ZERO};
//			Double[] uniform = { 0d };
//			Double[] thirdFee = { 0d };
			BigDecimal[] totalHostelDue = {BigDecimal.ZERO};

			studentDetails.stream().forEach(s -> {
//				Map<String, Object> addon = otherFeeDetailsRepository.getTotalOfSemAmountProgrammeFee(s.getCurrentSem(),
//						s.getSchoolId(), s.getProgramId(), s.getProgramSpecialiaztionId(), s.getAcYearId());
//				Map<String, Object> uniformAndStationary = otherFeeDetailsRepository
//						.getTotalOfSemAmountUniformStationary(s.getCurrentSem(), s.getSchoolId(), s.getProgramId(),
//								s.getProgramSpecialiaztionId(), s.getAcYearId());

				Double hostelDue = hostelDueRepository.getSumOfHostelDueByStudent(s.getAuid());

				grandTotal[0] = grandTotal[0].add(toBigDecimal(s.getTotalDue())).add(toBigDecimal(s.getAddOn())).add(toBigDecimal(hostelDue));
				sem1Total[0] = sem1Total[0].add(toBigDecimal(s.getSem1()));
				sem2Total[0] = sem2Total[0].add(toBigDecimal(s.getSem2()));
				sem3Total[0] = sem3Total[0].add(toBigDecimal(s.getSem3()));
				sem4Total[0] = sem4Total[0].add(toBigDecimal(s.getSem4()));
				sem5Total[0] = sem5Total[0].add(toBigDecimal(s.getSem5()));
				sem6Total[0] = sem6Total[0].add(toBigDecimal(s.getSem6()));
				sem7Total[0] = sem7Total[0].add(toBigDecimal(s.getSem7()));
				sem8Total[0] = sem8Total[0].add(toBigDecimal(s.getSem8()));
				sem9Total[0] = sem9Total[0].add(toBigDecimal(s.getSem9()));
				sem10Total[0] = sem10Total[0].add(toBigDecimal(s.getSem10()));
				sem11Total[0] = sem11Total[0].add(toBigDecimal(s.getSem11()));
				sem12Total[0] = sem12Total[0].add(toBigDecimal(s.getSem12()));

//				addOnTotal[0] += ObjectUtils.isNotEmpty(addon) && ObjectUtils.isNotEmpty(addon.get("totalSemAmount"))
//						? (double) addon.get("totalSemAmount")
//						: 0.0d;
//				uniform[0] += ObjectUtils.isNotEmpty(uniformAndStationary)
//						&& ObjectUtils.isNotEmpty(uniformAndStationary.get("totalSemAmount"))
//								? (double) uniformAndStationary.get("totalSemAmount")
//								: 0.0d;
//
//				thirdFee[0] += addOnTotal[0] + uniform[0];
				addOnTotal[0] = addOnTotal[0].add(toBigDecimal(s.getAddOn()));
				totalHostelDue[0] = totalHostelDue[0].add(toBigDecimal(hostelDue));
				s.setHostelFee(ObjectUtils.isNotEmpty(hostelDue) ? hostelDue : 0.0d);
				s.setTotalDue(s.getTotalDue().add(toBigDecimal(s.getAddOn())).add(toBigDecimal(hostelDue)));
			});

			studentWiseDueReport.setGrantTotalDue(grandTotal[0]);
			studentWiseDueReport.setTotalSem1(sem1Total[0]);
			studentWiseDueReport.setTotalSem2(sem2Total[0]);
			studentWiseDueReport.setTotalSem3(sem3Total[0]);
			studentWiseDueReport.setTotalSem4(sem4Total[0]);
			studentWiseDueReport.setTotalSem5(sem5Total[0]);
			studentWiseDueReport.setTotalSem6(sem6Total[0]);
			studentWiseDueReport.setTotalSem7(sem7Total[0]);
			studentWiseDueReport.setTotalSem8(sem8Total[0]);
			studentWiseDueReport.setTotalSem9(sem9Total[0]);
			studentWiseDueReport.setTotalSem10(sem10Total[0]);
			studentWiseDueReport.setTotalSem11(sem11Total[0]);
			studentWiseDueReport.setTotalSem12(sem12Total[0]);
//			studentWiseDueReport.setTotalAddOn(thirdFee[0]);
			studentWiseDueReport.setTotalAddOn(addOnTotal[0]);
			studentWiseDueReport.setTotalhostelDue(totalHostelDue[0]);
			studentWiseDueReport.setStudentWiseDueReports(studentDetails);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentWiseDueReport);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);
		}

	}

	private BigDecimal toBigDecimal(Number value) {
		return ObjectUtils.isNotEmpty(value)
				? BigDecimal.valueOf(value.doubleValue()).setScale(2, RoundingMode.HALF_UP)
				: BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
	}

	public void studentImageUploadFileFromPhpServer(String auid, MultipartFile imageFile) {
		Student_Details student_detail = studentDetailsRepository.getStudentDetailsData(auid)
				.orElseThrow(() -> new ResourceNotFoundException("Student Details Not Found:" + auid));
		try {
			log.debug("Message For Student Image Attachment --------------");
			File image_file = convertMultiPartToFile(imageFile);
			String imageFileName = generateFileName(imageFile);
			log.debug("Student Image Attachment", image_file);
			student_detail.setStudent_image_path(
					LocalDate.now() + "/" + student_detail.getStudent_id() + "/" + imageFileName);
			uploadFileToS3Bucket(imageFileName, image_file, student_detail.getStudent_id());
			log.debug("Message For Attachment", image_file);
			image_file.delete();
		} catch (AmazonServiceException ase) {

			log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			log.info("Error Message:    " + ase.getMessage());
			log.info("HTTP Status Code: " + ase.getStatusCode());
			log.info("AWS Error Code:   " + ase.getErrorCode());
			log.info("Error Type:       " + ase.getErrorType());
			log.info("Request ID:       " + ase.getRequestId());

		} catch (AmazonClientException ace) {
			log.info("Caught an AmazonClientException: ");
			log.info("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			log.info("IOE Error Message: " + ioe.getMessage());

		}
		studentDetailsRepository.save(student_detail);

	}

	public ResponseEntity<Object> getStudentDetailsForPermission(Integer studentId) {
		try {
			Map<String, Object> studentDetailsMap = studentDetailsRepository.getStudentDetailsForPermission(studentId);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentDetailsMap);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);

		}

	}

	public ResponseEntity<Object> saveStudentForPermission(List<StudentPermissionDTO> studentPermissionDtos, String token) {
		try {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(token);
			ArrayList<StudentPermission> studentPermissionList = new ArrayList<>();

			for (StudentPermissionDTO studentPermissionDTO : studentPermissionDtos) {
				Boolean isExists = studentPermissionRepository.existsByAuidAndCurrentSemAndPermissionTypeAndActive(
						studentPermissionDTO.getAuid(), studentPermissionDTO.getCurrentSem(),
						studentPermissionDTO.getPermissionType(), Boolean.TRUE);

//				if (isExists) {
//					return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", "Data Already exists for "
//							+ studentPermissionDTO.getAuid() + " and " + studentPermissionDTO.getCurrentSem());
//				}

				//if (!isExists) {


				StudentPermission studentPermission = new StudentPermission();
				studentPermission.setActive(Boolean.TRUE);
				studentPermission.setAuid(studentPermissionDTO.getAuid());
				studentPermission.setAllowSem(studentPermissionDTO.getAllowSem());
				studentPermission.setPermittedBy(studentPermissionDTO.getPermittedBy());
				studentPermission.setTotalDue(studentPermissionDTO.getTotalDue());
				studentPermission.setStudentName(studentPermissionDTO.getStudentName());
				studentPermission.setAttachment(studentPermissionDTO.getAttachment());
				studentPermission.setCurrentSem(studentPermissionDTO.getCurrentSem());
				studentPermission.setCurrentYear(studentPermissionDTO.getCurrentYear());
				studentPermission.setPermissionType(studentPermissionDTO.getPermissionType());
				studentPermission.setCreated_username(jwtDetails.getUserName());
				studentPermission.setCreated_by(jwtDetails.getUserId());
				studentPermission.setIsAllowAttendencePermit(
						ObjectUtils.isNotEmpty(studentPermissionDTO.getIsAllowAttendencePermit())
								&& studentPermissionDTO.getIsAllowAttendencePermit() == Boolean.TRUE ? Boolean.TRUE
								: Boolean.FALSE);
				studentPermission.setIsAllowExamPermit(ObjectUtils.isNotEmpty(studentPermissionDTO.getIsAllowExamPermit())
						&& studentPermissionDTO.getIsAllowExamPermit() == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE);
				studentPermission
						.setIsAllowPartFeePermit(ObjectUtils.isNotEmpty(studentPermissionDTO.getIsAllowPartFeePermit())
								&& studentPermissionDTO.getIsAllowPartFeePermit() == Boolean.TRUE ? Boolean.TRUE
								: Boolean.FALSE);
				studentPermission.setIsAllowFineWaiver(ObjectUtils.isNotEmpty(studentPermissionDTO.getIsAllowFineWaiver())
						&& studentPermissionDTO.getIsAllowFineWaiver() == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE);
				studentPermission.setRemarks(studentPermissionDTO.getRemarks());
				studentPermission.setTillDate(studentPermissionDTO.getTillDate());
				studentPermissionList.add(studentPermission);
				//	}
			}
			if (!studentPermissionList.isEmpty()) {
				studentPermissionRepository.saveAll(studentPermissionList);
			}
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> uploadStudentPermissionFile(MultipartFile multipartFile, String fileType,
															  String studentId) {
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			uploadStudentPermissonFileToS3Bucket(fileName, file, studentId, fileType);
			String attachmentPath = LocalDate.now() + "/" + studentId + "/" + fileType + "/" + fileName;
			int index = attachmentPath.indexOf(studentPermission);
//			String downloadAttachmentPath=null;
//			if(index!=-1) {
//				downloadAttachmentPath=attachmentPath.substring(index + attachmentPath.length() + 1);
//			}
//			
			Map<String, Object> responseMap = new HashMap<String, Object>();
			responseMap.put("attachmentPath", attachmentPath);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", responseMap);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	private void uploadStudentPermissonFileToS3Bucket(String fileName, File file, String studentId, String fileType) {
		final String uniqueFileName = studentPermission + "/" + LocalDate.now() + "/" + studentId + "/" + fileType + "/"
				+ fileName;
		s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

	}

	public ResponseEntity<Object> getTotalDueofStudent(Integer studentId, Integer currentSem) {
		try {
			StudentDues studentDues = studentDueRepository.getBySId(studentId);
			Float totalDue = 0.0f;
			if (currentSem == 1) {
				totalDue = studentDues.getS1due();
			}
			if (currentSem == 2) {
				totalDue = studentDues.getS2due();
			}
			if (currentSem == 3) {
				totalDue = studentDues.getS3due();
			}
			if (currentSem == 4) {
				totalDue = studentDues.getS4due();
			}
			if (currentSem == 5) {
				totalDue = studentDues.getS5due();
			}
			if (currentSem == 6) {
				totalDue = studentDues.getS6due();
			}
			if (currentSem == 7) {
				totalDue = studentDues.getS7due();
			}
			if (currentSem == 8) {
				totalDue = studentDues.getS8due();
			}

			if (currentSem == 9) {
				totalDue = studentDues.getS9due();
			}
			if (currentSem == 10) {
				totalDue = studentDues.getS10due();
			}
			if (currentSem == 11) {
				totalDue = studentDues.getS11due();
			}
			if (currentSem == 12) {
				totalDue = studentDues.getS12due();
			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", totalDue);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> saveExamPermission(ExamPermitDTO examPermitDTO) {

		try {
			Boolean isExists = examPermitRepository.existsByAuidAndAllowSem(examPermitDTO.getAuid(),
					examPermitDTO.getAllowSem());
			if (isExists) {
				return ResponseHandler.generateResponse(false, HttpStatus.OK, "SUCCESS", "Data Already exists");
			}

			ExamPermit examPermit = new ExamPermit();
			examPermit.setAllowSem(examPermitDTO.getAllowSem());
			examPermit.setAuid(examPermitDTO.getAuid());
			examPermit.setActive(Boolean.TRUE);
			examPermit.setPermittedBy(examPermitDTO.getPermittedBy());
			examPermit.setTotalDue(examPermitDTO.getTotalDue());
			examPermitRepository.save(examPermit);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> updateStudentForPermission(StudentPermissionDTO studentPermissionDTO, String token) {
		try {
			JwtDetails jwtDetails = jwtTokenService.callJwtToken(token);
			StudentPermission studentPermission = studentPermissionRepository.getByAuidAndCurrentSemAndPermissionType(
					studentPermissionDTO.getAuid(), studentPermissionDTO.getCurrentSem(),
					studentPermissionDTO.getPermissionType());

			studentPermission.setActive(Boolean.TRUE);
			studentPermission.setAuid(studentPermissionDTO.getAuid());
			studentPermission.setAttachment(studentPermissionDTO.getAttachment());
			studentPermission.setCurrentSem(studentPermissionDTO.getCurrentSem());
			studentPermission.setCurrentYear(studentPermissionDTO.getCurrentYear());
			studentPermission.setAllowSem(studentPermissionDTO.getAllowSem());
			studentPermission.setPermittedBy(studentPermissionDTO.getPermittedBy());
			studentPermission.setTotalDue(studentPermissionDTO.getTotalDue());
			studentPermission.setModified_by(jwtDetails.getUserId());
			studentPermission.setModified_username(jwtDetails.getUserName());
			studentPermission.setIsAllowAttendencePermit(
					ObjectUtils.isNotEmpty(studentPermissionDTO.getIsAllowAttendencePermit())
							&& studentPermissionDTO.getIsAllowAttendencePermit() == Boolean.TRUE ? Boolean.TRUE
							: Boolean.FALSE);
			studentPermission.setIsAllowExamPermit(ObjectUtils.isNotEmpty(studentPermissionDTO.getIsAllowExamPermit())
					&& studentPermissionDTO.getIsAllowExamPermit() == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE);
			studentPermission
					.setIsAllowPartFeePermit(ObjectUtils.isNotEmpty(studentPermissionDTO.getIsAllowPartFeePermit())
							&& studentPermissionDTO.getIsAllowPartFeePermit() == Boolean.TRUE ? Boolean.TRUE
							: Boolean.FALSE);
			studentPermission.setIsAllowFineWaiver(ObjectUtils.isNotEmpty(studentPermissionDTO.getIsAllowFineWaiver())
					&& studentPermissionDTO.getIsAllowFineWaiver() == Boolean.TRUE ? Boolean.TRUE : Boolean.FALSE);
			studentPermission.setRemarks(studentPermissionDTO.getRemarks());
			studentPermission.setTillDate(studentPermissionDTO.getTillDate());
			studentPermissionRepository.save(studentPermission);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);

		}

	}

	public ResponseEntity<Object> getStudentPermissionList() {
		try {
			List<StudentPermission> studentPermissions = studentPermissionRepository.getStudentPermissionList();

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentPermissions);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> deleteStudentPermission(String auid, Integer currentSem, String permissionType) {
		try {
			StudentPermission studentPermission = studentPermissionRepository
					.getByAuidAndCurrentSemAndPermissionType(auid, currentSem, permissionType);
			studentPermission.setActive(Boolean.FALSE);
			studentPermissionRepository.save(studentPermission);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);

		}
	}

	public ResponseEntity<Object> activateStudentPermission(String auid, Integer currentSem, String permissionType) {
		try {
			StudentPermission studentPermission = studentPermissionRepository
					.getByAuidAndCurrentSemAndPermissionType(auid, currentSem, permissionType);
			studentPermission.setActive(Boolean.TRUE);
			studentPermissionRepository.save(studentPermission);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);

		}
	}

	public byte[] studentPermissionFileDownload(String pathName) throws NoSuchFileException {
		try {
			byte[] content;
			final S3Object s3Object = s3client.getObject(bucketName, studentPermission + "/" + pathName);
			final S3ObjectInputStream stream = s3Object.getObjectContent();
			content = IOUtils.toByteArray(stream);
			System.out.println(content);
			s3Object.close();
			return content;

		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == org.apache.http.HttpStatus.SC_NOT_FOUND) {
				throw new NoSuchFileException("File Not Found");
			}
			throw new AmazonClientException("", e);
		} catch (IOException | AmazonClientException ex) {
			throw new AmazonClientException("", ex);
		}
	}

	public ResponseEntity<Object> studentWiseDueReportByStudentId(Integer studentId) {
		try {
			Map<String, Object> studentDetails = studentDetailsRepository
					.getStudentDetailsForDueReportByStudentId(studentId);

			StudentWiseDueReport studentWiseDue = new StudentWiseDueReport();
			String studentName = (String) studentDetails.get("studentName");
			String auid = (String) studentDetails.get("auid");
			Integer currentYear = (Integer) studentDetails.get("currentYear");
			Integer currentSem = (Integer) studentDetails.get("currentSem");
			String feeTemplateName = (String) studentDetails.get("feeTemplateName");
			Integer programId = (Integer) studentDetails.get("programId");
			Integer programSpecializationId = (Integer) studentDetails.get("programSpecializationId");
			Integer schoolId = (Integer) studentDetails.get("schoolId");
			Integer acYearId = (Integer) studentDetails.get("acYearId");

			studentWiseDue.setStudentName(studentName);
			studentWiseDue.setAuid(auid);
			studentWiseDue.setTemplateName(feeTemplateName);
			studentWiseDue.setCurrentSem(currentSem);
			studentWiseDue.setCurrentYear(currentYear);

			StudentDues studentDues = studentDueRepo.getStudentDueDetailsByStudentId(studentId);

			Long totalOtherFeeSemesterWise = null;
			Float semFixed = 0f;

			Float semPaid = 0f;

			Float semDue = 0f;

			Float semScholarship = 0f;

			Float semTuitionFee = 0f;

			Float semWaiver = 0f;

			Float semFeePaid = 0f;

			Float semAddOn = 0f;

			Float semSumAddOn = 0f;

			Float semSumUniform = 0f;

			Map<String, Object> scholarshipApprovalStatus = scholarshipApprovalStatusRepository
					.getApprovedScholarShipbyStudentId(studentDues.getStudentId());

			for (int sem = 1; sem <= 12; sem++) {

				OtherFeeTemplateForStudentDTO otherFeeDetailsAddOn = otherFeeDetailsRepository
						.getAddOnProgramFeeDetails(studentId, (Integer) studentDetails.get("acYearId"),
								(Integer) studentDetails.get("programId"),
								(Integer) studentDetails.get("programSpecializationId"));
				OtherFeeTemplateForStudentDTO otherFeeDetailsUniform = otherFeeDetailsRepository.getUniformFeeDetails(
						studentId, (Integer) studentDetails.get("acYearId"), (Integer) studentDetails.get("programId"),
						(Integer) studentDetails.get("programSpecializationId"));

				if (sem == 1) {
//						studentWiseDue.setSem1((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS1due()) ? studentDues.getS1due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year1_amt()) ? feeTemplate.getFee_year1_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem1Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear1()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem1Waiver)
							? sem1Waiver
							: 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year1"))
							? ((Number) scholarshipApprovalStatus.get("year1")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem1()) ? otherFeeDetailsAddOn.getSem1()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem1())
							? otherFeeDetailsUniform.getSem1()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem1(semDue);

				}
				if (sem == 2) {
//						studentWiseDue.setSem2((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS2due()) ? studentDues.getS2due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year2_amt()) ? feeTemplate.getFee_year2_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem2Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear2()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem2Waiver)
							? sem2Waiver
							: 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year2"))
							? ((Number) scholarshipApprovalStatus.get("year2")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem2()) ? otherFeeDetailsAddOn.getSem2()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem2())
							? otherFeeDetailsUniform.getSem2()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem2(semDue);

				}
				if (sem == 3) {
//						studentWiseDue.setSem3((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS3due()) ? studentDues.getS3due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year3_amt()) ? feeTemplate.getFee_year3_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem3Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear3()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem3Waiver)
							? sem3Waiver
							: 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year3"))
							? ((Number) scholarshipApprovalStatus.get("year3")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem3()) ? otherFeeDetailsAddOn.getSem3()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem3())
							? otherFeeDetailsUniform.getSem3()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem3(semDue);

				}
				if (sem == 4) {
//						studentWiseDue.setSem4((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS4due()) ? studentDues.getS4due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year4_amt()) ? feeTemplate.getFee_year4_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem4Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear4()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem4Waiver)
							? sem4Waiver
							: 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year4"))
							? ((Number) scholarshipApprovalStatus.get("year4")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem4()) ? otherFeeDetailsAddOn.getSem4()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem4())
							? otherFeeDetailsUniform.getSem4()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem4(semDue);
				}
				if (sem == 5) {
//						studentWiseDue.setSem5((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS5due()) ? studentDues.getS5due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year5_amt()) ? feeTemplate.getFee_year5_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem5Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear5()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem5Waiver)
							? sem5Waiver
							: 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year5"))
							? ((Number) scholarshipApprovalStatus.get("year5")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem5()) ? otherFeeDetailsAddOn.getSem5()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem5())
							? otherFeeDetailsUniform.getSem5()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem5(semDue);
				}
				if (sem == 6) {
//						studentWiseDue.setSem6((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS6due()) ? studentDues.getS6due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year6_amt()) ? feeTemplate.getFee_year6_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem6Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear6()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem6Waiver)
							? sem6Waiver
							: 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year6"))
							? ((Number) scholarshipApprovalStatus.get("year6")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem6()) ? otherFeeDetailsAddOn.getSem6()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem6())
							? otherFeeDetailsUniform.getSem6()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem6(semDue);
				}
				if (sem == 7) {
//						studentWiseDue.setSem7((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS7due()) ? studentDues.getS7due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year7_amt()) ? feeTemplate.getFee_year7_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem7Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear7()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem7Waiver)
							? sem7Waiver
							: 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year7"))
							? ((Number) scholarshipApprovalStatus.get("year7")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem7()) ? otherFeeDetailsAddOn.getSem7()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem7())
							? otherFeeDetailsUniform.getSem7()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem7(semDue);
				}

				if (sem == 8) {
//						studentWiseDue.setSem8((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS8due()) ? studentDues.getS8due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year8_amt()) ? feeTemplate.getFee_year8_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem8Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear8()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem8Waiver)
							? sem8Waiver
							: 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year8"))
							? ((Number) scholarshipApprovalStatus.get("year8")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem8()) ? otherFeeDetailsAddOn.getSem8()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem8())
							? otherFeeDetailsUniform.getSem8()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem8(semDue);
				}
				if (sem == 9) {
//						studentWiseDue.setSem9((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS9due()) ? studentDues.getS9due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year9_amt()) ? feeTemplate.getFee_year9_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem9Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear9()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem9Waiver)
							? sem9Waiver
							: 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year9"))
							? ((Number) scholarshipApprovalStatus.get("year9")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem9()) ? otherFeeDetailsAddOn.getSem9()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem9())
							? otherFeeDetailsUniform.getSem9()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem9(semDue);
				}
				if (sem == 10) {
//						studentWiseDue.setSem10((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS10due()) ? studentDues.getS10due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year10_amt()) ? feeTemplate.getFee_year10_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem10Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear10()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
							&& ObjectUtils.isNotEmpty(sem10Waiver) ? sem10Waiver : 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year10"))
							? ((Number) scholarshipApprovalStatus.get("year10")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem10()) ? otherFeeDetailsAddOn.getSem10()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem10())
							? otherFeeDetailsUniform.getSem10()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem10(semDue);
				}
				if (sem == 11) {
//						studentWiseDue.setSem11((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS11due()) ? studentDues.getS11due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year11_amt()) ? feeTemplate.getFee_year11_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem11Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear11()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
							&& ObjectUtils.isNotEmpty(sem11Waiver) ? sem11Waiver : 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year11"))
							? ((Number) scholarshipApprovalStatus.get("year11")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem11()) ? otherFeeDetailsAddOn.getSem11()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem11())
							? otherFeeDetailsUniform.getSem11()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem11(semDue);
				}
				if (sem == 12) {
//						studentWiseDue.setSem12((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS12due()) ? studentDues.getS12due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

					FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
					semFixed = ObjectUtils.isNotEmpty(feeTemplate)
							&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year12_amt()) ? feeTemplate.getFee_year12_amt()
							: 0f;
					semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

					List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
					Double sem12Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear12()).sum();
					semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver)
							&& ObjectUtils.isNotEmpty(sem12Waiver) ? sem12Waiver : 0f);

//					ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//							.getApprovedScholarShipbyYearAndStudentId(studentId);
					semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
							&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year12"))
							? ((Number) scholarshipApprovalStatus.get("year12")).floatValue()
							: 0f;

					semPaid = (semPaid != null) ? semPaid : 0f;
					semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
					semScholarship = (semScholarship != null) ? semScholarship : 0f;
					semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem1()) ? otherFeeDetailsAddOn.getSem12()
							: 0f);
					semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
							&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem1())
							? otherFeeDetailsUniform.getSem12()
							: 0f);
					semAddOn = (semSumAddOn + semSumUniform) - semPaid;
					semDue = semFixed - semScholarship - semTuitionFee - semPaid;

					studentWiseDue.setAddOn(semAddOn);
					studentWiseDue.setSem12(semDue);
				}

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", studentWiseDue);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);
		}

	}

	public StudentWiseDueReport studentWiseDueReportByStudentId1(Integer studentId) {

		Map<String, Object> studentDetails = studentDetailsRepository
				.getStudentDetailsForDueReportByStudentId(studentId);

		StudentWiseDueReport studentWiseDue = new StudentWiseDueReport();
		String studentName = (String) studentDetails.get("studentName");
		String auid = (String) studentDetails.get("auid");
		Integer currentYear = (Integer) studentDetails.get("currentYear");
		Integer currentSem = (Integer) studentDetails.get("currentSem");
		String feeTemplateName = (String) studentDetails.get("feeTemplateName");
		Integer programId = (Integer) studentDetails.get("programId");
		Integer programSpecializationId = (Integer) studentDetails.get("programSpecializationId");
		Integer schoolId = (Integer) studentDetails.get("schoolId");
		Integer acYearId = (Integer) studentDetails.get("acYearId");

		studentWiseDue.setStudentName(studentName);
		studentWiseDue.setAuid(auid);
		studentWiseDue.setTemplateName(feeTemplateName);
		studentWiseDue.setCurrentSem(currentSem);
		studentWiseDue.setCurrentYear(currentYear);

		StudentDues studentDues = studentDueRepo.getStudentDueDetailsByStudentId(studentId);

		Long totalOtherFeeSemesterWise = null;
		Float semFixed = 0f;

		Float semPaid = 0f;

		Float semDue = 0f;

		Float semScholarship = 0f;

		Float semTuitionFee = 0f;

		Float semWaiver = 0f;

		Float semFeePaid = 0f;

		Float semAddOn = 0f;

		Float semSumAddOn = 0f;

		Float semSumUniform = 0f;

		Map<String, Object> scholarshipApprovalStatus = scholarshipApprovalStatusRepository
				.getApprovedScholarShipbyStudentId(studentDues.getStudentId());
		for (int sem = 1; sem <= 12; sem++) {
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA111111111");
			OtherFeeTemplateForStudentDTO otherFeeDetailsAddOn = otherFeeDetailsRepository.getAddOnProgramFeeDetails(
					studentId, (Integer) studentDetails.get("acYearId"), (Integer) studentDetails.get("programId"),
					(Integer) studentDetails.get("programSpecializationId"));
			OtherFeeTemplateForStudentDTO otherFeeDetailsUniform = otherFeeDetailsRepository.getUniformFeeDetails(
					studentId, (Integer) studentDetails.get("acYearId"), (Integer) studentDetails.get("programId"),
					(Integer) studentDetails.get("programSpecializationId"));

			if (sem == 1) {
//						studentWiseDue.setSem1((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS1due()) ? studentDues.getS1due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getFee_year1_amt())
						? feeTemplate.getFee_year1_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem1Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear1()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem1Waiver)
						? sem1Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year1"))
						? ((Number) scholarshipApprovalStatus.get("year1")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem1()) ? otherFeeDetailsAddOn.getSem1()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem1()) ? otherFeeDetailsUniform.getSem1()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem1(semDue);

			}

			if (sem == 2) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA22222");
//						studentWiseDue.setSem2((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS2due()) ? studentDues.getS2due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getFee_year2_amt())
						? feeTemplate.getFee_year2_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem2Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear2()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem2Waiver)
						? sem2Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year2"))
						? ((Number) scholarshipApprovalStatus.get("year2")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem2()) ? otherFeeDetailsAddOn.getSem2()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem2()) ? otherFeeDetailsUniform.getSem2()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem2(semDue);

			}

			if (sem == 3) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA33333333");
//						studentWiseDue.setSem3((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS3due()) ? studentDues.getS3due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getFee_year3_amt())
						? feeTemplate.getFee_year3_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem3Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear3()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem3Waiver)
						? sem3Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year3"))
						? ((Number) scholarshipApprovalStatus.get("year3")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem3()) ? otherFeeDetailsAddOn.getSem3()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem3()) ? otherFeeDetailsUniform.getSem3()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem3(semDue);

			}

			if (sem == 4) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA44444444444");
//						studentWiseDue.setSem4((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS4due()) ? studentDues.getS4due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getFee_year4_amt())
						? feeTemplate.getFee_year4_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem4Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear4()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem4Waiver)
						? sem4Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year4"))
						? ((Number) scholarshipApprovalStatus.get("year4")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem4()) ? otherFeeDetailsAddOn.getSem4()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem4()) ? otherFeeDetailsUniform.getSem4()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem4(semDue);
			}

			if (sem == 5) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA5555555555");
//						studentWiseDue.setSem5((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS5due()) ? studentDues.getS5due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getFee_year5_amt())
						? feeTemplate.getFee_year5_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem5Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear5()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem5Waiver)
						? sem5Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year5"))
						? ((Number) scholarshipApprovalStatus.get("year5")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem5()) ? otherFeeDetailsAddOn.getSem5()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem5()) ? otherFeeDetailsUniform.getSem5()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem5(semDue);
			}

			if (sem == 6) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA666666666");
//						studentWiseDue.setSem6((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS6due()) ? studentDues.getS6due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getFee_year6_amt())
						? feeTemplate.getFee_year6_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem6Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear6()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem6Waiver)
						? sem6Waiver
						: 0f);
//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year6"))
						? ((Number) scholarshipApprovalStatus.get("year6")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem6()) ? otherFeeDetailsAddOn.getSem6()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem6()) ? otherFeeDetailsUniform.getSem6()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem6(semDue);
			}

			if (sem == 7) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA7777777777");
//						studentWiseDue.setSem7((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS7due()) ? studentDues.getS7due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getFee_year7_amt())
						? feeTemplate.getFee_year7_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem7Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear7()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem7Waiver)
						? sem7Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year7"))
						? ((Number) scholarshipApprovalStatus.get("year7")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem7()) ? otherFeeDetailsAddOn.getSem7()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem7()) ? otherFeeDetailsUniform.getSem7()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem7(semDue);
			}

			if (sem == 8) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA88888888");
//						studentWiseDue.setSem8((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS8due()) ? studentDues.getS8due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getFee_year8_amt())
						? feeTemplate.getFee_year8_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem8Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear8()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem8Waiver)
						? sem8Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year8"))
						? ((Number) scholarshipApprovalStatus.get("year8")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem8()) ? otherFeeDetailsAddOn.getSem8()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem8()) ? otherFeeDetailsUniform.getSem8()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem8(semDue);
			}

			if (sem == 9) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA99999999");
//						studentWiseDue.setSem9((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS9due()) ? studentDues.getS9due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getFee_year9_amt())
						? feeTemplate.getFee_year9_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem9Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear9()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem9Waiver)
						? sem9Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year9"))
						? ((Number) scholarshipApprovalStatus.get("year9")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem9()) ? otherFeeDetailsAddOn.getSem9()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem9()) ? otherFeeDetailsUniform.getSem9()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem9(semDue);
			}

			if (sem == 10) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA100000000");
//						studentWiseDue.setSem10((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS10due()) ? studentDues.getS10due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate)
						&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year10_amt()) ? feeTemplate.getFee_year10_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem10Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear10()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem10Waiver)
						? sem10Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year10"))
						? ((Number) scholarshipApprovalStatus.get("year10")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem10()) ? otherFeeDetailsAddOn.getSem10()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem10()) ? otherFeeDetailsUniform.getSem10()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem10(semDue);
			}

			if (sem == 11) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 11   11   11    11   11   11");
//						studentWiseDue.setSem11((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS11due()) ? studentDues.getS11due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate)
						&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year11_amt()) ? feeTemplate.getFee_year11_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem11Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear11()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem11Waiver)
						? sem11Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year11"))
						? ((Number) scholarshipApprovalStatus.get("year11")).floatValue()
						: 0f;

				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem11()) ? otherFeeDetailsAddOn.getSem11()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem11()) ? otherFeeDetailsUniform.getSem11()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem11(semDue);
			}

			if (sem == 12) {
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA 12   12    12   12");
//						studentWiseDue.setSem12((double) (ObjectUtils.isNotEmpty(studentDues)
//								&& ObjectUtils.isNotEmpty(studentDues.getS12due()) ? studentDues.getS12due() : 0));
//						Map<String, Object> otherFeeTemplateForStudent = otherFeeDetailsRepository.getTotalOfSemAmount(sem,
//								schoolId, programId, programSpecializationId, acYearId);
//						totalOtherFeeSemesterWise = (Long) otherFeeTemplateForStudent.get("totalSemAmount");
//						studentWiseDue.setAddOn(totalOtherFeeSemesterWise);

				FeeTemplate feeTemplate = feeTemplateRepository.getFeeTemplateByStudentId(studentId);
				semFixed = ObjectUtils.isNotEmpty(feeTemplate)
						&& ObjectUtils.isNotEmpty(feeTemplate.getFee_year12_amt()) ? feeTemplate.getFee_year12_amt()
						: 0f;
				semPaid = studentPaymentHistoryRepository.findYearPaidByStudentId(sem, studentId);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository.getDataByStudentIdForWaiver(studentId);
				Double sem12Waiver = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear12()).sum();
				semWaiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(sem12Waiver)
						? sem12Waiver
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentId);

				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX          " + scholarshipApprovalStatus);

				// Use Optional to handle nulls more safely
				semScholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year12"))
						? ((Number) scholarshipApprovalStatus.get("year12")).floatValue()
						: 0f;
				System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ         " + semScholarship);
				semPaid = (semPaid != null) ? semPaid : 0f;
				semTuitionFee = (semFeePaid != null) || (semWaiver != null) ? semFeePaid + semWaiver : 0f;
				semScholarship = (semScholarship != null) ? semScholarship : 0f;
				semSumAddOn = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsAddOn)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsAddOn.getSem12()) ? otherFeeDetailsAddOn.getSem12()
						: 0f);
				semSumUniform = (float) (ObjectUtils.isNotEmpty(otherFeeDetailsUniform)
						&& ObjectUtils.isNotEmpty(otherFeeDetailsUniform.getSem12()) ? otherFeeDetailsUniform.getSem12()
						: 0f);
				semAddOn = (semSumAddOn + semSumUniform) - semPaid;
				semDue = semFixed - semScholarship - semTuitionFee - semPaid;

				studentWiseDue.setAddOn(semAddOn);
				studentWiseDue.setSem12(semDue);
			}

		}

		return studentWiseDue;

	}

	public ResponseEntity<Object> getStudentDetailsForTransaction(Integer studentId) {
		try {
			Map<String, Object> responseMap = new HashMap<>();
			StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, studentId, null);
			applicationEventPublisher.publishEvent(studentDueEvent);

			Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(studentId);
			ReportingStudents reportingStudents = reportingStudentsRepository
					.getDetailsOfReportingStudentsByStudentId(studentId);
			ProgramAssigment programAssigment = programAssigmentRepository
					.getOne(studentDetails.getProgram_assignment_id());
			OtherFeeTemplateForStudentDTO uniformFee = otherFeeDetailsRepository.getUniformFeeDetails(
					studentDetails.getSchool_id(), studentDetails.getAc_year_id(), studentDetails.getProgram_id(),
					studentDetails.getProgram_specialization_id());
			FeeTemplate feeTemplateDetails = feeTemplateRepository
					.findByfee_template_id(studentDetails.getFee_template_id());
			String schoolName = schoolRepository.getSchoolShortName(studentDetails.getSchool_id());
			OtherFeeTemplateForStudentDTO addOn = otherFeeDetailsRepository.getAddOnProgramFeeDetailsByFeeTemplateId(
					studentDetails.getSchool_id(), studentDetails.getAc_year_id(), studentDetails.getProgram_id(),
					studentDetails.getFee_template_id());
			HostelDue hostelDue = hostelDueRepository
					.hostelDueByAcademicYearIdAndStudentId(studentDetails.getAc_year_id(), studentId);


			Integer concessionAmount = fineConcessionRepository
					.getFineConcessionAmountTillDate(studentDetails.getAuid());

			String programType = programTypeRepository.fetchProgramType(programAssigment.getProgram_type_id());

			StudentPermission studentPermission = studentPermissionRepository.getByAuidAndCurrentSemAndPermissionType(
					studentDetails.getAuid(), reportingStudents.getCurrent_sem(), "Part Fee");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = dateFormat.format(new Date());
			Date newDate = dateFormat.parse(formattedDate);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(newDate);
			int month = calendar.get(Calendar.MONTH) + 1;
			int year = calendar.get(Calendar.YEAR);

			DollarToInrConversion dollarToInrConversion = dollarToInrConversionRepository.findByDateAndActive(month,
					year);

			String currenyType = studentDetailsRepository.getCurrenyTypeByStudentId(studentId);
			responseMap.put("studentId", studentDetails.getStudent_id());
			responseMap.put("partFeeDate",
					ObjectUtils.isNotEmpty(studentPermission) && ObjectUtils.isNotEmpty(studentPermission.getTillDate())
							? studentPermission.getTillDate()
							: null);
			responseMap.put("allowSem",
					ObjectUtils.isNotEmpty(studentPermission) && ObjectUtils.isNotEmpty(studentPermission.getAllowSem())
							? studentPermission.getAllowSem()
							: null);
			responseMap.put("acYearId", studentDetails.getAc_year_id());
			responseMap.put("currentSem", reportingStudents.getCurrent_sem());
			responseMap.put("currentYear", reportingStudents.getCurrent_year());
			responseMap.put("programAssignmentId", studentDetails.getProgram_assignment_id());
			responseMap.put("programId", programAssigment.getProgram_id());
			responseMap.put("fatherName", studentDetails.getFather_name());
			responseMap.put("auid", studentDetails.getAuid());
			responseMap.put("numberOfSem", programAssigment.getNumber_of_semester());
			Integer remainingSemester = programAssigment.getNumber_of_semester() - reportingStudents.getCurrent_sem();
			responseMap.put("remainingSem", remainingSemester);
			responseMap.put("studentName", studentDetails.getStudent_name());
			responseMap.put("email", studentDetails.getAcharya_email());
			responseMap.put("lockTill", reportingStudents.getCurrent_sem());
			responseMap.put("schoolId", studentDetails.getSchool_id());
			responseMap.put("hostelDue", hostelDue);
			responseMap.put("schoolName", schoolName);
			responseMap.put("mobile", studentDetails.getMobile());

			Thread.sleep(1000);
			StudentDues studentDues = studentDueRepository
					.getStudentDueDetailsByStudentId(studentDetails.getStudent_id());
			Map<String, Object> uniformfeeDetails = getUniformFee(uniformFee, studentDues.getStudentId(),
					feeTemplateDetails);
			Map<String, Object> addonDetails = getAddOn(addOn, studentDues.getStudentId());
			responseMap.put("uniformAndStationary", uniformfeeDetails);
			responseMap.put("feeCma", addonDetails);
			Map<String, Object> feetemplateList = getSemesterWise(programAssigment.getNumber_of_semester(),
					feeTemplateDetails, studentDues, currenyType, dollarToInrConversion);
			responseMap.put("feeTemplate", feetemplateList);
			Float totalDue = getTotalDue(feetemplateList, reportingStudents.getCurrent_sem());
			responseMap.put("totalDue", totalDue);

			Map<String, Object> lateFee = getLatefee(feetemplateList, studentDetails.getSchool_id(),
					studentDetails.getProgram_assignment_id(), studentDetails.getProgram_specialization_id(),
					programAssigment.getNumber_of_semester(), reportingStudents.getCurrent_sem(), concessionAmount,
					studentDetails.getAc_year_id(), studentDetails.getStudent_id(), dollarToInrConversion, currenyType);
			responseMap.put("lateFee", lateFee);
			Double actualTotalFee = getActualTotalFee(feetemplateList, lateFee, addonDetails, uniformfeeDetails,
					reportingStudents.getCurrent_sem());
			responseMap.put("actualTotalFee", actualTotalFee);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", responseMap);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	private Double getActualTotalFee(Map<String, Object> feetemplateList, Map<String, Object> lateFee,
									 Map<String, Object> addonDetails, Map<String, Object> uniformfeeDetails, Integer currentSem) {

		Double totalDue = 0.0;
		Double collegeSem1 = 0.0;
		Double collegeSem2 = 0.0;
		Double collegeSem3 = 0.0;
		Double collegeSem4 = 0.0;
		Double collegeSem5 = 0.0;
		Double collegeSem6 = 0.0;
		Double collegeSem7 = 0.0;
		Double collegeSem8 = 0.0;
		Double collegeSem9 = 0.0;
		Double collegeSem10 = 0.0;
		Double collegeSem11 = 0.0;
		Double collegeSem12 = 0.0;
		Double addOnSem1 = 0.0;
		Double addOnSem2 = 0.0;
		Double addOnSem3 = 0.0;
		Double addOnSem4 = 0.0;
		Double addOnSem5 = 0.0;
		Double addOnSem6 = 0.0;
		Double addOnSem7 = 0.0;
		Double addOnSem8 = 0.0;
		Double addOnSem9 = 0.0;
		Double addOnSem10 = 0.0;
		Double addOnSem11 = 0.0;
		Double addOnSem12 = 0.0;
		Double uniformSem1 = 0.0;
		Double uniformSem2 = 0.0;
		Double uniformSem3 = 0.0;
		Double uniformSem4 = 0.0;
		Double uniformSem5 = 0.0;
		Double uniformSem6 = 0.0;
		Double uniformSem7 = 0.0;
		Double uniformSem8 = 0.0;
		Double uniformSem9 = 0.0;
		Double uniformSem10 = 0.0;
		Double uniformSem11 = 0.0;
		Double uniformSem12 = 0.0;
		Double lateSem1 = 0.0;
		Double lateSem2 = 0.0;
		Double lateSem3 = 0.0;
		Double lateSem4 = 0.0;
		Double lateSem5 = 0.0;
		Double lateSem6 = 0.0;
		Double lateSem7 = 0.0;
		Double lateSem8 = 0.0;
		Double lateSem9 = 0.0;
		Double lateSem10 = 0.0;
		Double lateSem11 = 0.0;
		Double lateSem12 = 0.0;
		if (currentSem >= 1) {
			collegeSem1 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem1"))
					? getNumericValue(feetemplateList.get("sem1"))
					: 0.0;
			addOnSem1 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem1"))
					? getNumericValue(addonDetails.get("sem1"))
					: 0.0;
			uniformSem1 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem1"))
					? getNumericValue(uniformfeeDetails.get("sem1"))
					: 0.0;
			lateSem1 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem1"))
					? getNumericValue(lateFee.get("sem1"))
					: 0.0;
		}
		if (currentSem >= 2) {
			collegeSem2 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem2"))
					? getNumericValue(feetemplateList.get("sem2"))
					: 0.0;
			addOnSem2 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem2"))
					? getNumericValue(addonDetails.get("sem2"))
					: 0.0;
			uniformSem2 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem2"))
					? getNumericValue(uniformfeeDetails.get("sem2"))
					: 0.0;
			lateSem2 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem2"))
					? getNumericValue(lateFee.get("sem2"))
					: 0.0;
		}
		if (currentSem >= 3) {
			collegeSem3 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem3"))
					? getNumericValue(feetemplateList.get("sem3"))
					: 0.0;
			addOnSem3 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem3"))
					? getNumericValue(addonDetails.get("sem3"))
					: 0.0;
			uniformSem3 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem3"))
					? getNumericValue(uniformfeeDetails.get("sem3"))
					: 0.0;
			lateSem3 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem3"))
					? getNumericValue(lateFee.get("sem3"))
					: 0.0;
		}
		if (currentSem >= 4) {
			collegeSem4 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem4"))
					? getNumericValue(feetemplateList.get("sem4"))
					: 0.0;
			addOnSem4 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem4"))
					? getNumericValue(addonDetails.get("sem4"))
					: 0.0;
			uniformSem4 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem4"))
					? getNumericValue(uniformfeeDetails.get("sem4"))
					: 0.0;
			lateSem4 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem4"))
					? getNumericValue(lateFee.get("sem4"))
					: 0.0;
		}
		if (currentSem >= 5) {
			collegeSem5 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem5"))
					? getNumericValue(feetemplateList.get("sem5"))
					: 0.0;
			addOnSem5 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem5"))
					? getNumericValue(addonDetails.get("sem5"))
					: 0.0;
			uniformSem5 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem5"))
					? getNumericValue(uniformfeeDetails.get("sem5"))
					: 0.0;
			lateSem5 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem5"))
					? getNumericValue(lateFee.get("sem5"))
					: 0.0;
		}
		if (currentSem >= 6) {
			collegeSem6 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem6"))
					? getNumericValue(feetemplateList.get("sem6"))
					: 0.0;
			addOnSem6 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem6"))
					? getNumericValue(addonDetails.get("sem6"))
					: 0.0;
			uniformSem6 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem6"))
					? getNumericValue(uniformfeeDetails.get("sem6"))
					: 0.0;
			lateSem6 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem6"))
					? getNumericValue(lateFee.get("sem6"))
					: 0.0;
		}
		if (currentSem >= 7) {
			collegeSem7 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem7"))
					? getNumericValue(feetemplateList.get("sem7"))
					: 0.0;
			addOnSem7 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem7"))
					? getNumericValue(addonDetails.get("sem7"))
					: 0.0;
			uniformSem7 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem7"))
					? getNumericValue(uniformfeeDetails.get("sem7"))
					: 0.0;
			lateSem7 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem7"))
					? getNumericValue(lateFee.get("sem7"))
					: 0.0;
		}
		if (currentSem >= 8) {
			collegeSem8 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem8"))
					? getNumericValue(feetemplateList.get("sem8"))
					: 0.0;
			addOnSem8 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem8"))
					? getNumericValue(addonDetails.get("sem8"))
					: 0.0;
			uniformSem8 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem8"))
					? getNumericValue(uniformfeeDetails.get("sem8"))
					: 0.0;
			lateSem8 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem8"))
					? getNumericValue(lateFee.get("sem8"))
					: 0.0;
		}
		if (currentSem >= 9) {
			collegeSem9 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem9"))
					? getNumericValue(feetemplateList.get("sem9"))
					: 0.0;
			addOnSem9 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem9"))
					? getNumericValue(addonDetails.get("sem9"))
					: 0.0;
			uniformSem9 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem9"))
					? getNumericValue(uniformfeeDetails.get("sem9"))
					: 0.0;
			lateSem9 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem9"))
					? getNumericValue(lateFee.get("sem9"))
					: 0.0;
		}
		if (currentSem >= 10) {
			collegeSem10 = ObjectUtils.isNotEmpty(feetemplateList)
					&& ObjectUtils.isNotEmpty(feetemplateList.get("sem10"))
					? getNumericValue(feetemplateList.get("sem10"))
					: 0.0;
			addOnSem10 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem10"))
					? getNumericValue(addonDetails.get("sem10"))
					: 0.0;
			uniformSem10 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem10"))
					? getNumericValue(uniformfeeDetails.get("sem10"))
					: 0.0;
			lateSem10 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem10"))
					? getNumericValue(lateFee.get("sem10"))
					: 0.0;
		}
		if (currentSem >= 11) {
			collegeSem11 = ObjectUtils.isNotEmpty(feetemplateList)
					&& ObjectUtils.isNotEmpty(feetemplateList.get("sem11"))
					? getNumericValue(feetemplateList.get("sem11"))
					: 0.0;
			addOnSem11 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem11"))
					? getNumericValue(addonDetails.get("sem11"))
					: 0.0;
			uniformSem11 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem11"))
					? getNumericValue(uniformfeeDetails.get("sem11"))
					: 0.0;
			lateSem11 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem11"))
					? getNumericValue(lateFee.get("sem11"))
					: 0.0;
		}
		if (currentSem >= 10) {
			collegeSem12 = ObjectUtils.isNotEmpty(feetemplateList)
					&& ObjectUtils.isNotEmpty(feetemplateList.get("sem12"))
					? getNumericValue(feetemplateList.get("sem12"))
					: 0.0;
			addOnSem12 = ObjectUtils.isNotEmpty(addonDetails) && ObjectUtils.isNotEmpty(addonDetails.get("sem12"))
					? getNumericValue(addonDetails.get("sem12"))
					: 0.0;
			uniformSem12 = ObjectUtils.isNotEmpty(uniformfeeDetails)
					&& ObjectUtils.isNotEmpty(uniformfeeDetails.get("sem12"))
					? getNumericValue(uniformfeeDetails.get("sem12"))
					: 0.0;
			lateSem12 = ObjectUtils.isNotEmpty(lateFee) && ObjectUtils.isNotEmpty(lateFee.get("sem12"))
					? getNumericValue(lateFee.get("sem12"))
					: 0.0;
		}

		totalDue = collegeSem1 + collegeSem2 + collegeSem3 + collegeSem4 + collegeSem5 + collegeSem6 + collegeSem7
				+ collegeSem8 + collegeSem9 + collegeSem10 + collegeSem11 + collegeSem12 + addOnSem1 + addOnSem2
				+ addOnSem3 + addOnSem4 + addOnSem5 + addOnSem6 + addOnSem7 + addOnSem8 + addOnSem9 + addOnSem10
				+ addOnSem11 + addOnSem12 + uniformSem1 + uniformSem2 + uniformSem3 + uniformSem4 + uniformSem5
				+ uniformSem6 + uniformSem7 + uniformSem8 + uniformSem9 + uniformSem10 + uniformSem11 + uniformSem12
				+ lateSem1 + lateSem2 + lateSem3 + lateSem4 + lateSem5 + lateSem6 + lateSem7 + lateSem8 + lateSem9
				+ lateSem10 + lateSem11 + lateSem12;
		return totalDue;
	}

	private Double getNumericValue(Object value) {
		if (value instanceof Double) {
			return (Double) value;
		} else if (value instanceof Float) {
			return ((Float) value).doubleValue();
		} else {
			return 0.0;
		}
	}

	private Float getTotalDue(Map<String, Object> feetemplateList, Integer currentSem) {
		Float totalDue = 0.0f;
		Float sem1 = 0.0f;
		Float sem2 = 0.0f;
		Float sem3 = 0.0f;
		Float sem4 = 0.0f;
		Float sem5 = 0.0f;
		Float sem6 = 0.0f;
		Float sem7 = 0.0f;
		Float sem8 = 0.0f;
		Float sem9 = 0.0f;
		Float sem10 = 0.0f;
		Float sem11 = 0.0f;
		Float sem12 = 0.0f;
		if (currentSem >= 1) {
			sem1 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem1"))
					? (Float) feetemplateList.get("sem1")
					: 0.0f;
		}
		if (currentSem >= 2) {
			sem2 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem2"))
					? (Float) feetemplateList.get("sem2")
					: 0.0f;

		}
		if (currentSem >= 3) {
			sem3 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem3"))
					? (Float) feetemplateList.get("sem3")
					: 0.0f;

		}
		if (currentSem >= 4) {
			sem4 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem4"))
					? (Float) feetemplateList.get("sem4")
					: 0.0f;

		}
		if (currentSem >= 5) {
			sem5 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem5"))
					? (Float) feetemplateList.get("sem5")
					: 0.0f;

		}
		if (currentSem >= 6) {
			sem6 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem6"))
					? (Float) feetemplateList.get("sem6")
					: 0.0f;

		}
		if (currentSem >= 7) {
			sem7 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem7"))
					? (Float) feetemplateList.get("sem7")
					: 0.0f;

		}
		if (currentSem >= 8) {
			sem8 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem8"))
					? (Float) feetemplateList.get("sem8")
					: 0.0f;

		}
		if (currentSem >= 9) {
			sem9 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem9"))
					? (Float) feetemplateList.get("sem9")
					: 0.0f;

		}
		if (currentSem >= 10) {
			sem10 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem10"))
					? (Float) feetemplateList.get("sem10")
					: 0.0f;

		}
		if (currentSem >= 11) {
			sem11 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem11"))
					? (Float) feetemplateList.get("sem11")
					: 0.0f;

		}
		if (currentSem >= 12) {
			sem12 = ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem12"))
					? (Float) feetemplateList.get("sem12")
					: 0.0f;

		}

		totalDue = sem1 + sem2 + sem3 + sem4 + sem5 + sem6 + sem7 + sem8 + sem9 + sem10 + sem11 + sem12;
		return totalDue;
	}

	private Map<String, Object> getAddOn(OtherFeeTemplateForStudentDTO addOn, Integer studentId) {
		Map<String, Object> addonMap = new LinkedHashMap<>();

		Float sem1AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(1, studentId))
				? getAddOnPaid(1, studentId).floatValue()
				: 0.0f;
		;
		Float sem2AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(2, studentId))
				? getAddOnPaid(2, studentId).floatValue()
				: 0.0f;
		;
		Float sem3AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(3, studentId))
				? getAddOnPaid(3, studentId).floatValue()
				: 0.0f;
		;
		Float sem4AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(4, studentId))
				? getAddOnPaid(4, studentId).floatValue()
				: 0.0f;
		;
		Float sem5AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(5, studentId))
				? getAddOnPaid(5, studentId).floatValue()
				: 0.0f;
		;
		Float sem6AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(6, studentId))
				? getAddOnPaid(6, studentId).floatValue()
				: 0.0f;
		;
		Float sem7AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(7, studentId))
				? getAddOnPaid(7, studentId).floatValue()
				: 0.0f;
		;
		Float sem8AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(8, studentId))
				? getAddOnPaid(8, studentId).floatValue()
				: 0.0f;
		;
		Float sem9AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(9, studentId))
				? getAddOnPaid(9, studentId).floatValue()
				: 0.0f;
		;
		Float sem10AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(10, studentId))
				? getAddOnPaid(10, studentId).floatValue()
				: 0.0f;
		;
		Float sem11AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(11, studentId))
				? getAddOnPaid(11, studentId).floatValue()
				: 0.0f;
		;
		Float sem12AddonPaid = ObjectUtils.isNotEmpty(getAddOnPaid(12, studentId))
				? getAddOnPaid(12, studentId).floatValue()
				: 0.0f;
		;
		addonMap.put("sem1",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem1())
						? addOn.getSem1() - sem1AddonPaid
						: 0.0);
		addonMap.put("sem2",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem2())
						? addOn.getSem2() - sem2AddonPaid
						: 0.0);
		addonMap.put("sem3",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem3())
						? addOn.getSem3() - sem3AddonPaid
						: 0.0);
		addonMap.put("sem4",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem4())
						? addOn.getSem4() - sem4AddonPaid
						: 0.0);
		addonMap.put("sem5",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem5())
						? addOn.getSem5() - sem5AddonPaid
						: 0.0);
		addonMap.put("sem6",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem6())
						? addOn.getSem6() - sem6AddonPaid
						: 0.0);
		addonMap.put("sem7",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem7())
						? addOn.getSem7() - sem7AddonPaid
						: 0.0);
		addonMap.put("sem8",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem8())
						? addOn.getSem8() - sem8AddonPaid
						: 0.0);
		addonMap.put("sem9",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem9())
						? addOn.getSem9() - sem9AddonPaid
						: 0.0);
		addonMap.put("sem10",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem10())
						? addOn.getSem10() - sem10AddonPaid
						: 0.0);
		addonMap.put("sem11",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem11())
						? addOn.getSem11() - sem11AddonPaid
						: 0.0);
		addonMap.put("sem12",
				ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem12())
						? addOn.getSem12() - sem12AddonPaid
						: 0.0);
		return addonMap;
	}

	private Map<String, Object> getUniformFee(OtherFeeTemplateForStudentDTO uniformFee, Integer studentId,
											  FeeTemplate feeTemplate) {
		Map<String, Object> uniformFeeMap = new LinkedHashMap<>();

		if (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(feeTemplate.getUniform_status())
				&& feeTemplate.getUniform_status() == Boolean.TRUE) {
			Float sem1UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(1, studentId))
					? getUniformPaid(1, studentId).floatValue()
					: 0.0f;
			Float sem2UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(2, studentId))
					? getUniformPaid(2, studentId).floatValue()
					: 0.0f;
			Float sem3UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(3, studentId))
					? getUniformPaid(3, studentId).floatValue()
					: 0.0f;
			Float sem4UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(4, studentId))
					? getUniformPaid(4, studentId).floatValue()
					: 0.0f;
			Float sem5UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(5, studentId))
					? getUniformPaid(5, studentId).floatValue()
					: 0.0f;
			Float sem6UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(6, studentId))
					? getUniformPaid(6, studentId).floatValue()
					: 0.0f;
			Float sem7UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(7, studentId))
					? getUniformPaid(7, studentId).floatValue()
					: 0.0f;
			Float sem8UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(8, studentId))
					? getUniformPaid(8, studentId).floatValue()
					: 0.0f;
			Float sem9UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(9, studentId))
					? getUniformPaid(9, studentId).floatValue()
					: 0.0f;
			Float sem10UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(10, studentId))
					? getUniformPaid(10, studentId).floatValue()
					: 0.0f;
			Float sem11UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(11, studentId))
					? getUniformPaid(11, studentId).floatValue()
					: 0.0f;
			Float sem12UniformPaid = ObjectUtils.isNotEmpty(getUniformPaid(12, studentId))
					? getUniformPaid(12, studentId).floatValue()
					: 0.0f;

			uniformFeeMap.put("sem1",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem1())
							? uniformFee.getSem1() - sem1UniformPaid
							: 0.0);
			uniformFeeMap.put("sem2",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem2())
							? uniformFee.getSem2() - sem2UniformPaid
							: 0.0);
			uniformFeeMap.put("sem3",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem3())
							? uniformFee.getSem3() - sem3UniformPaid
							: 0.0);
			uniformFeeMap.put("sem4",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem4())
							? uniformFee.getSem4() - sem4UniformPaid
							: 0.0);
			uniformFeeMap.put("sem5",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem5())
							? uniformFee.getSem5() - sem5UniformPaid
							: 0.0);
			uniformFeeMap.put("sem6",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem6())
							? uniformFee.getSem6() - sem6UniformPaid
							: 0.0);
			uniformFeeMap.put("sem7",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem7())
							? uniformFee.getSem7() - sem7UniformPaid
							: 0.0);
			uniformFeeMap.put("sem8",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem8())
							? uniformFee.getSem8() - sem8UniformPaid
							: 0.0);
			uniformFeeMap.put("sem9",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem9())
							? uniformFee.getSem9() - sem9UniformPaid
							: 0.0);
			uniformFeeMap.put("sem10",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem10())
							? uniformFee.getSem10() - sem10UniformPaid
							: 0.0);
			uniformFeeMap.put("sem11",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem11())
							? uniformFee.getSem11() - sem11UniformPaid
							: 0.0);
			uniformFeeMap.put("sem12",
					ObjectUtils.isNotEmpty(uniformFee) && ObjectUtils.isNotEmpty(uniformFee.getSem12())
							? uniformFee.getSem12() - sem12UniformPaid
							: 0.0);

		}
		return uniformFeeMap;
	}

	private Double getConvertValue(Double amount, DollarToInrConversion dollarToInrConversion) {
		if (ObjectUtils.isNotEmpty(dollarToInrConversion)) {
			double conversionRate = dollarToInrConversion.getInr();
			double amountInINR = amount * conversionRate;
			return amountInINR;
		}
		return amount;
	}

	private Map<String, Object> getLatefee(Map<String, Object> feetemplateList, Integer schoolId,
										   Integer programAssignmentId, Integer programSpecializationId, Integer numberOfSem, Integer currentSem,
										   Integer concessionAmount, Integer acYearId, Integer studentId, DollarToInrConversion dollarToInrConversion, String currenyType) {

		Float cAmount = (float) (ObjectUtils.isNotEmpty(concessionAmount) ? concessionAmount > 0 ? concessionAmount : 0
				: 0);
		List<FineSlabDTO> fineSlabDTOs = fineSlabRepository.getAllFines();

		Map<String, Object> latefee = new LinkedHashMap<String, Object>();
		StudentDues studentDues = studentDueRepo.getStudentDueDetailsByStudentId(studentId);

		for (int sem = 1; sem <= currentSem; sem++) {
			double penaltyPercentage = 0.0;
			if (currentSem == sem) {
				ClassCommencementDetails classCommencementDetails = classCommencementDetailsRepository
						.getClassCommencementDetailsForStudentLateFee(schoolId, programAssignmentId,
								programSpecializationId, sem);

				penaltyPercentage = getPenalty(classCommencementDetails, fineSlabDTOs);
			} else {
				penaltyPercentage = 5.0;
			}
			float fineAmount = getLateDue(studentDues, sem) != null && getLateDue(studentDues, sem) > 0
					&& (currentSem >= sem && acYearId < 7) ? getFineValue(penaltyPercentage, studentDues, sem) : 0;

//			latefee.put("sem" + sem, getLateDue(studentDues, sem) != null && getLateDue(studentDues, sem) > 0
//					&& (currentSem >= sem && acYearId < 7) ? getFineValue(penaltyPercentage, studentDues, sem) : 0);


			latefee.put("sem" + sem, (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR") ? fineAmount
					: getConvertValue((double) fineAmount, dollarToInrConversion).floatValue()));
		}

		for (Map.Entry<String, Object> m : latefee.entrySet()) {
			if (cAmount <= 0) {
				break;
			}

			Float semValue = (Float) m.getValue();
			if (cAmount >= semValue && semValue > 0) {
				latefee.put(m.getKey(), 0.0f);
			} else if (semValue > cAmount && semValue > 0) {
				Float remainingDue = semValue - cAmount;
				latefee.put(m.getKey(), remainingDue);
			}
			cAmount = semValue - cAmount;
		}

		return latefee;

	}

	private Float getFineValue(double penaltyPercentage, Map<String, Object> feetemplateList, int sem) {
		Float value = (float) ((penaltyPercentage / 100) * getLateDue(feetemplateList, sem));
		return (float) Math.round(value);
	}

	private Float getLateDue(Map<String, Object> feetemplateList, int sem) {
		switch (sem) {
			case 1:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem1"))
						? (Float) feetemplateList.get("sem1")
						: 0.0f;
			case 2:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem2"))
						? (Float) feetemplateList.get("sem2")
						: 0.0f;
			case 3:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem3"))
						? (Float) feetemplateList.get("sem3")
						: 0.0f;
			case 4:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem4"))
						? (Float) feetemplateList.get("sem4")
						: 0.0f;
			case 5:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem5"))
						? (Float) feetemplateList.get("sem5")
						: 0.0f;
			case 6:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem6"))
						? (Float) feetemplateList.get("sem6")
						: 0.0f;
			case 7:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem7"))
						? (Float) feetemplateList.get("sem7")
						: 0.0f;
			case 8:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem8"))
						? (Float) feetemplateList.get("sem8")
						: 0.0f;
			case 9:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem9"))
						? (Float) feetemplateList.get("sem9")
						: 0.0f;
			case 10:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem10"))
						? (Float) feetemplateList.get("sem10")
						: 0.0f;
			case 11:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem11"))
						? (Float) feetemplateList.get("sem11")
						: 0.0f;
			case 12:
				return ObjectUtils.isNotEmpty(feetemplateList) && ObjectUtils.isNotEmpty(feetemplateList.get("sem12"))
						? (Float) feetemplateList.get("sem12")
						: 0.0f;

			default:
				return null;
		}

	}

	private double getPenalty(ClassCommencementDetails classCommencementDetails, List<FineSlabDTO> fineSlabDTOs) {
		Integer week1 = 0;
		Integer week2 = 0;
		Integer week3 = 0;
		if (ObjectUtils.isNotEmpty(fineSlabDTOs)) {
			week1 = fineSlabDTOs.stream().filter(f -> f.getWeek() == 1).findFirst().get().getPercentage();
			week2 = fineSlabDTOs.stream().filter(f -> f.getWeek() == 2).findFirst().get().getPercentage();
			week3 = fineSlabDTOs.stream().filter(f -> f.getWeek() == 3).findFirst().get().getPercentage();

		}

		double penaltyPercentage = 0.0;
		if (ObjectUtils.isNotEmpty(classCommencementDetails)) {
			Date fromDate = classCommencementDetails.getFrom_date();
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata")); // Explicit timezone
			calendar.setTime(fromDate);
			//calendar.add(Calendar.DAY_OF_MONTH, 1);

			// Convert using a fixed ZoneId
			ZoneId fixedZone = ZoneId.of("Asia/Kolkata");
			LocalDate updatedFromDate = calendar.getTime().toInstant().atZone(fixedZone).toLocalDate();
			LocalDate cDate = LocalDate.now(fixedZone); // Ensure LocalDate uses the same timezone

			if (cDate.isAfter(updatedFromDate)) {
				long diffInDays = ChronoUnit.DAYS.between(updatedFromDate, cDate) - 1;
				if (diffInDays == 0) {
					penaltyPercentage = 0.0;
				} else if (diffInDays <= 7) {
					penaltyPercentage = week1;
				} else if (diffInDays > 7 && diffInDays <= 14) {
					penaltyPercentage = week2;
				} else if (diffInDays > 14 && diffInDays <= 35) {
					penaltyPercentage = week3;
				} else {
					penaltyPercentage = 5.0;
				}
			}
		}
		return penaltyPercentage;

	}

	private Map<String, Object> getSemesterWise(Integer numberOfSem, FeeTemplate feeTemplate, StudentDues studentDues,
												String currenyType, DollarToInrConversion dollarToInrConversion) {
		Float sem1Fixed = 0f;

		Float sem1Paid = 0f;

		Float sem1Due = 0f;

		Float sem1Scholarship = 0f;

		Float sem1TuitionFee = 0f;

		Float sem2Fixed = 0f;

		Float sem2Paid = 0f;

		Float sem2Due = 0f;

		Float sem2Scholarship = 0f;

		Float sem2TuitionFee = 0f;

		Float sem3Fixed = 0f;

		Float sem3Paid = 0f;

		Float sem3Due = 0f;

		Float sem3Scholarship = 0f;

		Float sem3TuitionFee = 0f;

		Float sem4Fixed = 0f;

		Float sem4Paid = 0f;

		Float sem4Due = 0f;

		Float sem4Scholarship = 0f;

		Float sem4TuitionFee = 0f;

		Float sem5Fixed = 0f;

		Float sem5Paid = 0f;

		Float sem5Due = 0f;

		Float sem5Scholarship = 0f;

		Float sem5TuitionFee = 0f;

		Float sem6Fixed = 0f;

		Float sem6Paid = 0f;

		Float sem6Due = 0f;

		Float sem6Scholarship = 0f;

		Float sem6TuitionFee = 0f;

		Float sem7Fixed = 0f;

		Float sem7Paid = 0f;

		Float sem7Due = 0f;

		Float sem7Scholarship = 0f;

		Float sem7TuitionFee = 0f;

		Float sem8Fixed = 0f;

		Float sem8Paid = 0f;

		Float sem8Due = 0f;

		Float sem8Scholarship = 0f;

		Float sem8TuitionFee = 0f;

		Float sem9Fixed = 0f;

		Float sem9Paid = 0f;

		Float sem9Due = 0f;

		Float sem9Scholarship = 0f;

		Float sem9TuitionFee = 0f;

		Float sem10Fixed = 0f;

		Float sem10Paid = 0f;

		Float sem10Due = 0f;

		Float sem10Scholarship = 0f;

		Float sem10TuitionFee = 0f;

		Float sem11Fixed = 0f;

		Float sem11Paid = 0f;

		Float sem11Due = 0f;

		Float sem11Scholarship = 0f;
		Float sem11TuitionFee = 0f;

		Float sem12Fixed = 0f;

		Float sem12Paid = 0f;

		Float sem12Due = 0f;

		Float sem12Scholarship = 0f;

		Float sem12TuitionFee = 0f;
		Float sem1Waiver = 0f;

		Float sem2Waiver = 0f;

		Float sem3Waiver = 0f;

		Float sem4Waiver = 0f;

		Float sem5Waiver = 0f;

		Float sem6Waiver = 0f;

		Float sem7Waiver = 0f;

		Float sem8Waiver = 0f;

		Float sem9Waiver = 0f;

		Float sem10Waiver = 0f;
		Float sem11Waiver = 0f;

		Float sem12Waiver = 0f;

		Float sem1FeePaid = 0f;

		Float sem2FeePaid = 0f;

		Float sem3FeePaid = 0f;

		Float sem4FeePaid = 0f;

		Float sem5FeePaid = 0f;

		Float sem6FeePaid = 0f;

		Float sem7FeePaid = 0f;

		Float sem8FeePaid = 0f;

		Float sem9FeePaid = 0f;

		Float sem10FeePaid = 0f;
		Float sem11FeePaid = 0f;

		Float sem12FeePaid = 0f;
		Float totalDue = 0.0f;
		Double swoSem1 = 0d;
		Double swoSemWise = 0d;

		Float sem1ReadmissionFixed = 0f;
		Float sem2ReadmissionFixed = 0f;
		Float sem3ReadmissionFixed = 0f;
		Float sem4ReadmissionFixed = 0f;
		Float sem5ReadmissionFixed = 0f;
		Float sem6ReadmissionFixed = 0f;
		Float sem7ReadmissionFixed = 0f;
		Float sem8ReadmissionFixed = 0f;
		Float sem9ReadmissionFixed = 0f;
		Float sem10ReadmissionFixed = 0f;
		Float sem11ReadmissionFixed = 0f;
		Float sem12ReadmissionFixed = 0f;

		Readmission readmission = readmissionRepository.getReadmittedStudent(studentDues.getStudentId());
		Map<String, Object> response = new LinkedHashMap<>();

		Map<String, Object> scholarshipApprovalStatus = scholarshipApprovalStatusRepository
				.getApprovedScholarShipbyStudentId(studentDues.getStudentId());

		for (int i = 1; i <= numberOfSem; i++) {

			if (i == 1) {
				swoSem1 = feeTemplateRepository.getSwoSem1(feeTemplate.getFee_template_id());

				sem1Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSem1)
						? (swoSem1 > 0 ? swoSem1
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year1_amt())
						&& feeTemplate.getFee_year1_amt() > 0 ? feeTemplate.getFee_year1_amt() : 0f))
						: 0f);
				sem1Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(1, studentDues.getStudentId());
				sem1ReadmissionFixed = getReadmissionAmount(readmission, sem1Fixed, 1, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear1()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem1Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());


				sem1Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year1"))
						? ((Number) scholarshipApprovalStatus.get("year1")).floatValue()
						: 0f;

				sem1Paid = (sem1Paid != null) ? sem1Paid : 0f;
				sem1TuitionFee = (sem1FeePaid != null) || (sem1Waiver != null) ? sem1FeePaid + sem1Waiver : 0f;
				sem1Scholarship = (sem1Scholarship != null) ? sem1Scholarship : 0f;
				sem1Due = ObjectUtils.isEmpty(readmission) ? sem1Fixed - sem1Scholarship - sem1TuitionFee - sem1Paid
						: sem1ReadmissionFixed - sem1Scholarship - sem1TuitionFee - sem1Paid;
				sem1Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem1Due
						: getConvertValue(sem1Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem1Due);
			} else if (i == 2) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 2);
				sem2Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year2_amt())
						&& feeTemplate.getFee_year2_amt() > 0 ? feeTemplate.getFee_year2_amt() : 0f))
						: 0f);
				sem2Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(2, studentDues.getStudentId());
				sem2ReadmissionFixed = getReadmissionAmount(readmission, sem2Fixed, 2, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear2()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem2Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem2Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year2"))
						? ((Number) scholarshipApprovalStatus.get("year2")).floatValue()
						: 0f;

				sem2Paid = (sem2Paid != null) ? sem2Paid : 0f;
				sem2TuitionFee = (sem2FeePaid != null) || (sem2Waiver != null) ? sem2FeePaid + sem2Waiver : 0f;
				sem2Scholarship = (sem2Scholarship != null) ? sem2Scholarship : 0f;
				sem2Due = ObjectUtils.isEmpty(readmission) ? sem2Fixed - sem2Scholarship - sem2TuitionFee - sem2Paid
						: sem2ReadmissionFixed - sem2Scholarship - sem2TuitionFee - sem2Paid;
				sem2Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem2Due
						: getConvertValue(sem2Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem2Due);
			} else if (i == 3) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 3);
				sem3Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year3_amt())
						&& feeTemplate.getFee_year3_amt() > 0 ? feeTemplate.getFee_year3_amt() : 0f))
						: 0f);
				sem3Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(3, studentDues.getStudentId());
				sem3ReadmissionFixed = getReadmissionAmount(readmission, sem3Fixed, 3, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear3()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem3Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem3Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year3"))
						? ((Number) scholarshipApprovalStatus.get("year3")).floatValue()
						: 0f;

				sem3Paid = (sem3Paid != null) ? sem3Paid : 0f;
				sem3TuitionFee = (sem3FeePaid != null) || (sem3Waiver != null) ? sem3FeePaid + sem3Waiver : 0f;
				sem3Scholarship = (sem3Scholarship != null) ? sem3Scholarship : 0f;
				sem3Due = ObjectUtils.isEmpty(readmission) ? sem3Fixed - sem3Scholarship - sem3TuitionFee - sem3Paid
						: sem3ReadmissionFixed - sem3Scholarship - sem3TuitionFee - sem3Paid;
				sem3Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem3Due
						: getConvertValue(sem3Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem3Due);
			} else if (i == 4) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 4);

				sem4Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year4_amt())
						&& feeTemplate.getFee_year4_amt() > 0 ? feeTemplate.getFee_year4_amt() : 0f))
						: 0f);
				sem4Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(4, studentDues.getStudentId());
				sem4ReadmissionFixed = getReadmissionAmount(readmission, sem4Fixed, 4, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear4()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem4Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem4Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year4"))
						? ((Number) scholarshipApprovalStatus.get("year4")).floatValue()
						: 0f;

				sem4Paid = (sem4Paid != null) ? sem4Paid : 0f;
				sem4TuitionFee = (sem4FeePaid != null) || (sem4Waiver != null) ? sem4FeePaid + sem4Waiver : 0f;
				sem4Scholarship = (sem4Scholarship != null) ? sem4Scholarship : 0f;
				sem4Due = ObjectUtils.isEmpty(readmission) ? sem4Fixed - sem4Scholarship - sem4TuitionFee - sem4Paid
						: sem4ReadmissionFixed - sem4Scholarship - sem4TuitionFee - sem4Paid;
				sem4Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem4Due
						: getConvertValue(sem4Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem4Due);
			} else if (i == 5) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 5);

				sem5Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year5_amt())
						&& feeTemplate.getFee_year5_amt() > 0 ? feeTemplate.getFee_year5_amt() : 0f))
						: 0f);
				sem5Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(5, studentDues.getStudentId());
				sem5ReadmissionFixed = getReadmissionAmount(readmission, sem5Fixed, 5, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear5()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem5Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem5Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year5"))
						? ((Number) scholarshipApprovalStatus.get("year5")).floatValue()
						: 0f;

				sem5Paid = (sem5Paid != null) ? sem5Paid : 0f;
				sem5TuitionFee = (sem5FeePaid != null) || (sem5Waiver != null) ? sem5FeePaid + sem5Waiver : 0f;
				sem5Scholarship = (sem5Scholarship != null) ? sem5Scholarship : 0f;
				sem5Due = ObjectUtils.isEmpty(readmission) ? sem5Fixed - sem5Scholarship - sem5TuitionFee - sem5Paid
						: sem5ReadmissionFixed - sem5Scholarship - sem5TuitionFee - sem5Paid;
				sem5Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem5Due
						: getConvertValue(sem5Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem5Due);
			} else if (i == 6) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 6);

				sem6Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year6_amt())
						&& feeTemplate.getFee_year6_amt() > 0 ? feeTemplate.getFee_year6_amt() : 0f))
						: 0f);
				sem6Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(6, studentDues.getStudentId());
				sem6ReadmissionFixed = getReadmissionAmount(readmission, sem6Fixed, 6, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear6()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem6Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem6Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year6"))
						? ((Number) scholarshipApprovalStatus.get("year6")).floatValue()
						: 0f;

				sem6Paid = (sem6Paid != null) ? sem6Paid : 0f;
				sem6TuitionFee = (sem6FeePaid != null) || (sem6Waiver != null) ? sem6FeePaid + sem6Waiver : 0f;
				sem6Scholarship = (sem6Scholarship != null) ? sem6Scholarship : 0f;
				sem6Due = ObjectUtils.isEmpty(readmission) ? sem6Fixed - sem6Scholarship - sem6TuitionFee - sem6Paid
						: sem6ReadmissionFixed - sem6Scholarship - sem6TuitionFee - sem6Paid;
				sem6Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem6Due
						: getConvertValue(sem6Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem6Due);
			} else if (i == 7) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 7);

				sem7Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year7_amt())
						&& feeTemplate.getFee_year7_amt() > 0 ? feeTemplate.getFee_year7_amt() : 0f))
						: 0f);

				sem7Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(7, studentDues.getStudentId());
				sem7ReadmissionFixed = getReadmissionAmount(readmission, sem7Fixed, 7, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear7()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem7Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem7Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year7"))
						? ((Number) scholarshipApprovalStatus.get("year7")).floatValue()
						: 0f;

				sem7Paid = (sem7Paid != null) ? sem7Paid : 0f;
				sem7TuitionFee = (sem7FeePaid != null) || (sem7Waiver != null) ? sem7FeePaid + sem7Waiver : 0f;
				sem7Scholarship = (sem7Scholarship != null) ? sem7Scholarship : 0f;
				sem7Due = ObjectUtils.isEmpty(readmission) ? sem7Fixed - sem7Scholarship - sem7TuitionFee - sem7Paid
						: sem7ReadmissionFixed - sem7Scholarship - sem7TuitionFee - sem7Paid;
				sem7Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem7Due
						: getConvertValue(sem7Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem7Due);
			} else if (i == 8) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 8);

				sem8Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year8_amt())
						&& feeTemplate.getFee_year8_amt() > 0 ? feeTemplate.getFee_year8_amt() : 0f))
						: 0f);

				sem8Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(8, studentDues.getStudentId());
				sem8ReadmissionFixed = getReadmissionAmount(readmission, sem8Fixed, 8, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear8()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem8Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem8Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year8"))
						? ((Number) scholarshipApprovalStatus.get("year8")).floatValue()
						: 0f;

				sem8Paid = (sem8Paid != null) ? sem8Paid : 0f;
				sem8TuitionFee = (sem8FeePaid != null) || (sem8Waiver != null) ? sem8FeePaid + sem8Waiver : 0f;
				sem8Scholarship = (sem8Scholarship != null) ? sem8Scholarship : 0f;
				sem8Due = ObjectUtils.isEmpty(readmission) ? sem8Fixed - sem8Scholarship - sem8TuitionFee - sem8Paid
						: sem8ReadmissionFixed - sem8Scholarship - sem8TuitionFee - sem8Paid;
				sem8Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem8Due
						: getConvertValue(sem8Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem8Due);
			} else if (i == 9) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 9);

				sem9Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year9_amt())
						&& feeTemplate.getFee_year9_amt() > 0 ? feeTemplate.getFee_year9_amt() : 0f))
						: 0f);

				sem9Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(9, studentDues.getStudentId());
				sem9ReadmissionFixed = getReadmissionAmount(readmission, sem9Fixed, 9, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear9()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem9Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);
//
//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem9Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year9"))
						? ((Number) scholarshipApprovalStatus.get("year9")).floatValue()
						: 0f;

				sem9Paid = (sem9Paid != null) ? sem9Paid : 0f;
				sem9TuitionFee = (sem9FeePaid != null) || (sem9Waiver != null) ? sem9FeePaid + sem9Waiver : 0f;
				sem9Scholarship = (sem9Scholarship != null) ? sem9Scholarship : 0f;
				sem9Due = ObjectUtils.isEmpty(readmission) ? sem9Fixed - sem9Scholarship - sem9TuitionFee - sem9Paid
						: sem9ReadmissionFixed - sem9Scholarship - sem9TuitionFee - sem9Paid;
				sem9Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem9Due
						: getConvertValue(sem9Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem9Due);
			} else if (i == 10) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 10);

				sem10Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year10_amt())
						&& feeTemplate.getFee_year10_amt() > 0 ? feeTemplate.getFee_year10_amt() : 0f))
						: 0f);

				sem10Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(10, studentDues.getStudentId());
				sem10ReadmissionFixed = getReadmissionAmount(readmission, sem10Fixed, 10, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear10()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem10Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem10Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year10"))
						? ((Number) scholarshipApprovalStatus.get("year10")).floatValue()
						: 0f;

				sem10Paid = (sem10Paid != null) ? sem10Paid : 0f;
				sem10TuitionFee = (sem10FeePaid != null) || (sem10Waiver != null) ? sem10FeePaid + sem10Waiver : 0f;
				sem10Scholarship = (sem10Scholarship != null) ? sem10Scholarship : 0f;
				sem10Due = ObjectUtils.isEmpty(readmission)
						? sem10Fixed - sem10Scholarship - sem10TuitionFee - sem10Paid
						: sem10ReadmissionFixed - sem10Scholarship - sem10TuitionFee - sem10Paid;
				sem10Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem10Due
						: getConvertValue(sem10Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem10Due);
			} else if (i == 11) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 11);

				sem11Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year11_amt())
						&& feeTemplate.getFee_year11_amt() > 0 ? feeTemplate.getFee_year11_amt() : 0f))
						: 0f);
				sem11Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(11, studentDues.getStudentId());
				sem11ReadmissionFixed = getReadmissionAmount(readmission, sem11Fixed, 11, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear11()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem11Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);
//
//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem11Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year11"))
						? ((Number) scholarshipApprovalStatus.get("year11")).floatValue()
						: 0f;

				sem11Paid = (sem11Paid != null) ? sem11Paid : 0f;
				sem11TuitionFee = (sem11FeePaid != null) || (sem11Waiver != null) ? sem11FeePaid + sem11Waiver : 0f;
				sem11Scholarship = (sem11Scholarship != null) ? sem11Scholarship : 0f;
				sem11Due = ObjectUtils.isEmpty(readmission)
						? sem11Fixed - sem11Scholarship - sem11TuitionFee - sem11Paid
						: sem11ReadmissionFixed - sem11Scholarship - sem11TuitionFee - sem11Paid;
				sem11Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem11Due
						: getConvertValue(sem11Due.doubleValue(), dollarToInrConversion));

				response.put("sem" + i, sem11Due);
			} else if (i == 12) {
				swoSemWise = feeTemplateRepository.getSwoSemWise(feeTemplate.getFee_template_id(), 12);

				sem12Fixed = (float) (ObjectUtils.isNotEmpty(feeTemplate) && ObjectUtils.isNotEmpty(swoSemWise)
						? (swoSemWise > 0 ? swoSemWise
						: (ObjectUtils.isNotEmpty(feeTemplate.getFee_year12_amt())
						&& feeTemplate.getFee_year12_amt() > 0 ? feeTemplate.getFee_year12_amt() : 0f))
						: 0f);
				sem12Paid = studentPaymentHistoryRepository.findYearPaidByStudentId(12, studentDues.getStudentId());
				sem12ReadmissionFixed = getReadmissionAmount(readmission, sem12Fixed, 12, currenyType,
						dollarToInrConversion);

				List<AcerpAmount> acerpAmountWaiver = acerpAmountRepository
						.getDataByStudentIdForWaiver(studentDues.getStudentId());
				Double waiverSum = acerpAmountWaiver.stream().mapToDouble(t -> t.getPaidYear12()).sum();
//				AcerpAmount acerpAmountFeePaid = acerpAmountRepository
//						.getDataByStudentIdForFeePaid(std.getStudentId());

				sem12Waiver = (float) (ObjectUtils.isNotEmpty(acerpAmountWaiver) && ObjectUtils.isNotEmpty(waiverSum)
						? waiverSum
						: 0f);

//				ScholarshipApprovalStatus scholarshipApprovalStatus = scholarshipApprovalStatusRepository
//						.getApprovedScholarShipbyYearAndStudentId(studentDues.getStudentId());
				sem12Scholarship = ObjectUtils.isNotEmpty(scholarshipApprovalStatus)
						&& ObjectUtils.isNotEmpty(scholarshipApprovalStatus.get("year12"))
						? ((Number) scholarshipApprovalStatus.get("year12")).floatValue()
						: 0f;

				sem12Paid = (sem12Paid != null) ? sem12Paid : 0f;
				sem12TuitionFee = (sem12FeePaid != null) || (sem12Waiver != null) ? sem12FeePaid + sem12Waiver : 0f;
				sem12Scholarship = (sem12Scholarship != null) ? sem12Scholarship : 0f;
				sem12Due = ObjectUtils.isEmpty(readmission)
						? sem12Fixed - sem12Scholarship - sem12TuitionFee - sem12Paid
						: sem12ReadmissionFixed - sem12Scholarship - sem12TuitionFee - sem12Paid;

				sem12Due = (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR")
						? sem12Due
						: getConvertValue(sem12Due.doubleValue(), dollarToInrConversion));
				response.put("sem" + i, sem12Due);

			}

		}
		return response;
	}

	public Double getUniformPaid(int sem, Integer studentId) {
		return uniformReceiptRepository.getSumOfPaidUniformFee(sem, studentId);

	}

	private Double getAddOnPaid(int sem, Integer studentId) {
		return cmaFeeReceiptRepository.getCmaFeeReceiptForAddOnFeePaidDetails(sem, studentId);
	}

	private Float getReadmissionAmount(Readmission readmission, Float semFixed, int i, String currenyType,
									   DollarToInrConversion dollarToInrConversion) {
		Float due = 0f;
		if (ObjectUtils.isNotEmpty(readmission) && ObjectUtils.isNotEmpty(readmission.getSemOrYear())) {
			int semOrYear = readmission.getSemOrYear();
			if (semOrYear > i) {
				due = 0f;
			} else if (semOrYear == i) {
				due = readmission.getTotalAmount().floatValue();
			} else {
				due = semFixed;
			}
		}
		return (float) (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR") ? due
				: getConvertValue(due.doubleValue(), dollarToInrConversion));
	}

	public ResponseEntity<Object> studentTransaction(StudentTransactionDTO studentTransactionDTO) {
		try {

			RazorpayClient razorpayClient = razorPayConfig.getRazorpayClient(studentTransactionDTO.getSchoolId());

			List<RouteAccountDetails> routeAccountDetails = routeAccountDetailsRepository
					.getAccountDetails(studentTransactionDTO.getSchoolId());
			RouteAccountDetails uniformAccount = routeAccountDetails.stream()
					.filter(r -> r.getRouteType().equals("UNIFORM")).findFirst().get();
			RouteAccountDetails addOnAccount = routeAccountDetails.stream()
					.filter(r -> r.getRouteType().equals("ADDON")).findFirst().get();
			RouteAccountDetails hostelAccount = routeAccountDetails.stream()
					.filter(r -> r.getRouteType().equals("HOSTEL")).findFirst().get();

			Student_Details student = studentDetailsRepository
					.getStudentByStudentId(studentTransactionDTO.getStudentId());

			TemporaryRazorPayTransaction razorPayTransaction = new TemporaryRazorPayTransaction();

			razorPayTransaction.setAcYearId(studentTransactionDTO.getAcYearId());
			razorPayTransaction.setAmount(studentTransactionDTO.getTotalDue());
			razorPayTransaction.setCurrentSem(studentTransactionDTO.getCurrentSem());
			razorPayTransaction.setCurrentYear(studentTransactionDTO.getCurrentYear());
			razorPayTransaction.setTransactionType("College Fees");
			razorPayTransaction.setPaidYear(Year.now().toString());

			Integer uniformSum = calculateUniformAndStationarySum(studentTransactionDTO);
			Integer addOnSum = calculateAddOnSum(studentTransactionDTO);

			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getPartFeeDate())
					&& ObjectUtils.isNotEmpty(studentTransactionDTO.getAllowSem())) {
				Integer sumOfUniformAndAddOn = calculateSumOfUniformAndAddOn(studentTransactionDTO);
				if (studentTransactionDTO.getTotalDue() < sumOfUniformAndAddOn) {
					return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE",
							"Can't Pay less than " + sumOfUniformAndAddOn);

				}
			}

			String receiptId = "txn_" + System.currentTimeMillis();
			razorPayTransaction.setReceiptId(receiptId);
			int amountInPaise = (int) (studentTransactionDTO.getTotalDue() * 100);

			JSONObject options = new JSONObject();
			options.put("amount", amountInPaise);
			options.put("currency", "INR");
			options.put("receipt", receiptId);

			JSONObject notes = new JSONObject();
			notes.put("UniformFee", uniformSum);
			notes.put("CMAFee", addOnSum);
			notes.put("HostelFee", studentTransactionDTO.getHostelDue());
			notes.put("TotalAmount", studentTransactionDTO.getTotalDue());
			notes.put("email",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
							? student.getAcharya_email()
							: "");
			notes.put("mobile",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
							? student.getMobile()
							: "");
			notes.put("auid",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAuid()) ? student.getAuid()
							: "");
			options.put("notes", notes);
			JSONArray transfers = new JSONArray();

			if (ObjectUtils.isNotEmpty(uniformAccount) && uniformSum > 0) {
				JSONObject uniformTransfer = new JSONObject();
				uniformTransfer.put("account", uniformAccount.getRouteAccountId());
				uniformTransfer.put("amount", uniformSum * 100);
				uniformTransfer.put("currency", "INR");
				JSONObject uniformNotes = new JSONObject();
				uniformNotes.put("UniformFee", uniformSum);
				uniformNotes.put("email",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
								? student.getAcharya_email()
								: "");
				uniformNotes.put("mobile",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
								? student.getMobile()
								: "");
				uniformNotes.put("auid",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAuid()) ? student.getAuid()
								: "");
				uniformTransfer.put("notes", uniformNotes);
				transfers.put(uniformTransfer);
			}

			if (ObjectUtils.isNotEmpty(addOnAccount) && addOnSum > 0) {
				JSONObject addOnAccountTransfer = new JSONObject();
				addOnAccountTransfer.put("account", addOnAccount.getRouteAccountId());
				addOnAccountTransfer.put("amount", addOnSum * 100);
				addOnAccountTransfer.put("currency", "INR");
				JSONObject addOnNotes = new JSONObject();
				addOnNotes.put("CMAFee", addOnSum);
				addOnNotes.put("email",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
								? student.getAcharya_email()
								: "");
				addOnNotes.put("mobile",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
								? student.getMobile()
								: "");
				addOnNotes.put("auid",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAuid()) ? student.getAuid()
								: "");
				addOnAccountTransfer.put("notes", addOnNotes);

				transfers.put(addOnAccountTransfer);
			}

			if (ObjectUtils.isNotEmpty(hostelAccount) && ObjectUtils.isNotEmpty(studentTransactionDTO.getHostelDue())
					&& studentTransactionDTO.getHostelDue() > 0) {
				JSONObject hostelAccountTransfer = new JSONObject();
				hostelAccountTransfer.put("account", hostelAccount.getRouteAccountId());
				hostelAccountTransfer.put("amount", studentTransactionDTO.getHostelDue() * 100);
				hostelAccountTransfer.put("currency", "INR");
				JSONObject hostelNotes = new JSONObject();
				hostelNotes.put("HostelFee", studentTransactionDTO.getHostelDue());
				hostelNotes.put("email",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
								? student.getAcharya_email()
								: "");
				hostelNotes.put("mobile",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
								? student.getMobile()
								: "");
				hostelNotes.put("auid",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAuid()) ? student.getAuid()
								: "");
				hostelAccountTransfer.put("notes", hostelNotes);

				transfers.put(hostelAccountTransfer);
			}

			options.put("transfers", transfers);
			Order order = razorpayClient.Orders.create(options);
			String jsonObject = order.toJson().toString();
			System.out.println(jsonObject);
			ObjectMapper objectMapper = new ObjectMapper();
			RazorPayOrder razorpayOrder = objectMapper.readValue(jsonObject, RazorPayOrder.class);

			String orderId = order.get("id");
			razorPayTransaction.setOrderId(orderId);
			razorPayTransaction.setStudentId(studentTransactionDTO.getStudentId());
			temporaryRazorPayTransactionRepository.save(razorPayTransaction);

			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getHostelDue())) {
				TemporaryRazorPayPaymentDetails hostelPay = new TemporaryRazorPayPaymentDetails();
				hostelPay.setAmount((double) studentTransactionDTO.getHostelDue());
				hostelPay.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				hostelPay.setPaymentType("College Fee");
				hostelPay.setReceiptType("Hostel Fee");
				hostelPay.setYear(studentTransactionDTO.getCurrentYear());
				hostelPay.setSem(studentTransactionDTO.getCurrentSem());
				hostelPay.setAcYearId(razorPayTransaction.getAcYearId());

				temporaryRazorPayPaymentDetailsRepository.save(hostelPay);
			}
			Float totalAmount = studentTransactionDTO.getTotalDue();
			totalAmount = getUniformFeeForPayment(studentTransactionDTO, razorPayTransaction, totalAmount);

			totalAmount = getAddOnForPayment(studentTransactionDTO, razorPayTransaction, totalAmount);

			totalAmount = getLateFeeForPayment(studentTransactionDTO, razorPayTransaction, totalAmount);

			totalAmount = getCollegeFeeForPayment(studentTransactionDTO, razorPayTransaction, totalAmount);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", razorpayOrder);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", e.getMessage());

		}
	}

	private Integer calculateSumOfUniformAndAddOn(StudentTransactionDTO studentTransactionDTO) throws ParseException {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		Date tillDate = simpleDateFormat.parse(studentTransactionDTO.getPartFeeDate());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tillDate);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date newTillDate = calendar.getTime();
//		Integer total = 0;
//		if (newTillDate.after(date) || newTillDate.equals(date)) {
//			if (studentTransactionDTO.getAllowSem() >= 1) {
//				total = ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem1()
//						+ studentTransactionDTO.getUniformAndStationary().getSem1();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 2) {
//				total = studentTransactionDTO.getFeeCma().getSem2()
//						+ studentTransactionDTO.getUniformAndStationary().getSem2();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 3) {
//				total = studentTransactionDTO.getFeeCma().getSem3()
//						+ studentTransactionDTO.getUniformAndStationary().getSem3();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 4) {
//				total = studentTransactionDTO.getFeeCma().getSem4()
//						+ studentTransactionDTO.getUniformAndStationary().getSem4();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 5) {
//				total = studentTransactionDTO.getFeeCma().getSem5()
//						+ studentTransactionDTO.getUniformAndStationary().getSem5();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 6) {
//				total = studentTransactionDTO.getFeeCma().getSem6()
//						+ studentTransactionDTO.getUniformAndStationary().getSem6();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 7) {
//				total = studentTransactionDTO.getFeeCma().getSem7()
//						+ studentTransactionDTO.getUniformAndStationary().getSem7();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 8) {
//				total = studentTransactionDTO.getFeeCma().getSem8()
//						+ studentTransactionDTO.getUniformAndStationary().getSem8();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 9) {
//				total = studentTransactionDTO.getFeeCma().getSem9()
//						+ studentTransactionDTO.getUniformAndStationary().getSem9();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 10) {
//				total = studentTransactionDTO.getFeeCma().getSem10()
//						+ studentTransactionDTO.getUniformAndStationary().getSem10();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 11) {
//				total = studentTransactionDTO.getFeeCma().getSem11()
//						+ studentTransactionDTO.getUniformAndStationary().getSem11();
//			}
//			if (studentTransactionDTO.getAllowSem() >= 12) {
//				total = studentTransactionDTO.getFeeCma().getSem12()
//						+ studentTransactionDTO.getUniformAndStationary().getSem12();
//			}
//		}
//
//		return total;
		Integer total = 0;

		if (newTillDate.after(date) || newTillDate.equals(date)) {
			for (int i = 1; i <= studentTransactionDTO.getAllowSem(); i++) {
				total += getAddOnSemValue(studentTransactionDTO.getFeeCma(), i) +
						getUniformSemValue(studentTransactionDTO.getUniformAndStationary(), i);
			}
		}
		return total;
	}

	private Integer getAddOnSemValue(FeeTemplateDTO feeCma, int semester) {
		if (feeCma == null) return 0;
		switch (semester) {
			case 1:
				return ObjectUtils.defaultIfNull(feeCma.getSem1(), 0);
			case 2:
				return ObjectUtils.defaultIfNull(feeCma.getSem2(), 0);
			case 3:
				return ObjectUtils.defaultIfNull(feeCma.getSem3(), 0);
			case 4:
				return ObjectUtils.defaultIfNull(feeCma.getSem4(), 0);
			case 5:
				return ObjectUtils.defaultIfNull(feeCma.getSem5(), 0);
			case 6:
				return ObjectUtils.defaultIfNull(feeCma.getSem6(), 0);
			case 7:
				return ObjectUtils.defaultIfNull(feeCma.getSem7(), 0);
			case 8:
				return ObjectUtils.defaultIfNull(feeCma.getSem8(), 0);
			case 9:
				return ObjectUtils.defaultIfNull(feeCma.getSem9(), 0);
			case 10:
				return ObjectUtils.defaultIfNull(feeCma.getSem10(), 0);
			case 11:
				return ObjectUtils.defaultIfNull(feeCma.getSem11(), 0);
			case 12:
				return ObjectUtils.defaultIfNull(feeCma.getSem12(), 0);
			default:
				return 0;
		}
	}

	private Integer getUniformSemValue(FeeTemplateDTO uniformAndStationary, int semester) {
		if (uniformAndStationary == null) return 0;
		switch (semester) {
			case 1:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem1(), 0);
			case 2:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem2(), 0);
			case 3:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem3(), 0);
			case 4:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem4(), 0);
			case 5:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem5(), 0);
			case 6:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem6(), 0);
			case 7:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem7(), 0);
			case 8:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem8(), 0);
			case 9:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem9(), 0);
			case 10:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem10(), 0);
			case 11:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem11(), 0);
			case 12:
				return ObjectUtils.defaultIfNull(uniformAndStationary.getSem12(), 0);
			default:
				return 0;
		}
	}


	public Integer calculateUniformAndStationarySum(StudentTransactionDTO studentTransactionDTO) {
		FeeTemplateDTO uniformAndStationary = studentTransactionDTO.getUniformAndStationary();
		if (uniformAndStationary == null) {
			return 0;
		}

		return (uniformAndStationary.getSem1() == null ? 0 : uniformAndStationary.getSem1())
				+ (uniformAndStationary.getSem2() == null ? 0 : uniformAndStationary.getSem2())
				+ (uniformAndStationary.getSem3() == null ? 0 : uniformAndStationary.getSem3())
				+ (uniformAndStationary.getSem4() == null ? 0 : uniformAndStationary.getSem4())
				+ (uniformAndStationary.getSem5() == null ? 0 : uniformAndStationary.getSem5())
				+ (uniformAndStationary.getSem6() == null ? 0 : uniformAndStationary.getSem6())
				+ (uniformAndStationary.getSem7() == null ? 0 : uniformAndStationary.getSem7())
				+ (uniformAndStationary.getSem8() == null ? 0 : uniformAndStationary.getSem8())
				+ (uniformAndStationary.getSem9() == null ? 0 : uniformAndStationary.getSem9())
				+ (uniformAndStationary.getSem10() == null ? 0 : uniformAndStationary.getSem10())
				+ (uniformAndStationary.getSem11() == null ? 0 : uniformAndStationary.getSem11())
				+ (uniformAndStationary.getSem12() == null ? 0 : uniformAndStationary.getSem12());
	}

	public Integer calculateAddOnSum(StudentTransactionDTO studentTransactionDTO) {
		FeeTemplateDTO addOn = studentTransactionDTO.getFeeCma();
		if (addOn == null) {
			return 0;
		}

		return (addOn.getSem1() == null ? 0 : addOn.getSem1()) + (addOn.getSem2() == null ? 0 : addOn.getSem2())
				+ (addOn.getSem3() == null ? 0 : addOn.getSem3()) + (addOn.getSem4() == null ? 0 : addOn.getSem4())
				+ (addOn.getSem5() == null ? 0 : addOn.getSem5()) + (addOn.getSem6() == null ? 0 : addOn.getSem6())
				+ (addOn.getSem7() == null ? 0 : addOn.getSem7()) + (addOn.getSem8() == null ? 0 : addOn.getSem8())
				+ (addOn.getSem9() == null ? 0 : addOn.getSem9()) + (addOn.getSem10() == null ? 0 : addOn.getSem10())
				+ (addOn.getSem11() == null ? 0 : addOn.getSem11()) + (addOn.getSem12() == null ? 0 : addOn.getSem12());
	}

	private Float getLateFeeForPayment(StudentTransactionDTO studentTransactionDTO,
									   TemporaryRazorPayTransaction razorPayTransaction, Float totalAmount) {
		List<TemporaryRazorPayPaymentDetails> temporaryRazorPayPaymentDetail = new LinkedList<>();

		float totalLateFee = 0;
		Integer lateSem1 = 0;
		Integer lateSem2 = 0;
		Integer lateSem3 = 0;
		Integer lateSem4 = 0;
		Integer lateSem5 = 0;
		Integer lateSem6 = 0;
		Integer lateSem7 = 0;
		Integer lateSem8 = 0;
		Integer lateSem9 = 0;
		Integer lateSem10 = 0;
		Integer lateSem11 = 0;
		Integer lateSem12 = 0;
		if (ObjectUtils.isNotEmpty(razorPayTransaction) && ObjectUtils.isNotEmpty(studentTransactionDTO)
				&& ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee()) && totalAmount > 0) {

			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem1())
					&& studentTransactionDTO.getLateFee().getSem1() > 0 && totalAmount > 0) {
				lateSem1 = studentTransactionDTO.getLateFee().getSem1();
				TemporaryRazorPayPaymentDetails lateFee1 = new TemporaryRazorPayPaymentDetails();
				lateFee1.setYear(1);
				lateFee1.setSem(1);
				lateFee1.setReceiptType("Bulk Fee");
				lateFee1.setPaymentType("College Fee");
				lateFee1.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee1.setAmount((double) lateSem1);
				lateFee1.setPaidYear(lateFee1.getSem());
				lateFee1.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee1);
				totalAmount = lateSem1 > totalAmount ? lateSem1 - totalAmount : totalAmount - lateSem1;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem2())
					&& studentTransactionDTO.getLateFee().getSem2() > 0 && totalAmount > 0) {
				lateSem2 = studentTransactionDTO.getLateFee().getSem2();
				TemporaryRazorPayPaymentDetails lateFee2 = new TemporaryRazorPayPaymentDetails();
				lateFee2.setYear(1);
				lateFee2.setSem(2);
				lateFee2.setReceiptType("Bulk Fee");
				lateFee2.setPaymentType("College Fee");
				lateFee2.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee2.setAmount((double) lateSem2);
				lateFee2.setPaidYear(lateFee2.getSem());
				lateFee2.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee2);
				totalAmount = lateSem2 > totalAmount ? lateSem2 - totalAmount : totalAmount - lateSem2;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem3())
					&& studentTransactionDTO.getLateFee().getSem3() > 0 && totalAmount > 0) {
				lateSem3 = studentTransactionDTO.getLateFee().getSem3();
				TemporaryRazorPayPaymentDetails lateFee3 = new TemporaryRazorPayPaymentDetails();
				lateFee3.setYear(2);
				lateFee3.setSem(3);
				lateFee3.setReceiptType("Bulk Fee");
				lateFee3.setPaymentType("College Fee");
				lateFee3.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee3.setAmount((double) lateSem3);
				lateFee3.setPaidYear(lateFee3.getSem());
				lateFee3.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee3);
				totalAmount = lateSem3 > totalAmount ? lateSem3 - totalAmount : totalAmount - lateSem3;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem4())
					&& studentTransactionDTO.getLateFee().getSem4() > 0 && totalAmount > 0) {
				lateSem4 = studentTransactionDTO.getLateFee().getSem4();
				TemporaryRazorPayPaymentDetails lateFee4 = new TemporaryRazorPayPaymentDetails();
				lateFee4.setYear(2);
				lateFee4.setSem(4);
				lateFee4.setReceiptType("Bulk Fee");
				lateFee4.setPaymentType("College Fee");
				lateFee4.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee4.setAmount((double) lateSem4);
				lateFee4.setPaidYear(lateFee4.getSem());
				lateFee4.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee4);
				totalAmount = lateSem4 > totalAmount ? lateSem4 - totalAmount : totalAmount - lateSem4;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem5())
					&& studentTransactionDTO.getLateFee().getSem5() > 0 && totalAmount > 0) {
				lateSem5 = studentTransactionDTO.getLateFee().getSem5();
				TemporaryRazorPayPaymentDetails lateFee5 = new TemporaryRazorPayPaymentDetails();
				lateFee5.setYear(3);
				lateFee5.setSem(5);
				lateFee5.setReceiptType("Bulk Fee");
				lateFee5.setPaymentType("College Fee");
				lateFee5.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee5.setAmount((double) lateSem5);
				lateFee5.setPaidYear(lateFee5.getSem());
				lateFee5.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee5);
				totalAmount = lateSem5 > totalAmount ? lateSem5 - totalAmount : totalAmount - lateSem5;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem6())
					&& studentTransactionDTO.getLateFee().getSem6() > 0 && totalAmount > 0) {
				lateSem6 = studentTransactionDTO.getLateFee().getSem6();
				TemporaryRazorPayPaymentDetails lateFee6 = new TemporaryRazorPayPaymentDetails();
				lateFee6.setYear(3);
				lateFee6.setSem(6);
				lateFee6.setReceiptType("Bulk Fee");
				lateFee6.setPaymentType("College Fee");
				lateFee6.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee6.setAmount((double) lateSem6);
				lateFee6.setPaidYear(lateFee6.getSem());
				lateFee6.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee6);
				totalAmount = lateSem6 > totalAmount ? lateSem6 - totalAmount : totalAmount - lateSem6;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem7())
					&& studentTransactionDTO.getLateFee().getSem7() > 0 && totalAmount > 0) {
				lateSem7 = studentTransactionDTO.getLateFee().getSem7();
				TemporaryRazorPayPaymentDetails lateFee7 = new TemporaryRazorPayPaymentDetails();
				lateFee7.setYear(4);
				lateFee7.setSem(7);
				lateFee7.setReceiptType("Bulk Fee");
				lateFee7.setPaymentType("College Fee");
				lateFee7.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee7.setAmount((double) lateSem7);
				lateFee7.setPaidYear(lateFee7.getSem());
				lateFee7.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee7);
				totalAmount = lateSem7 > totalAmount ? lateSem7 - totalAmount : totalAmount - lateSem7;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem8())
					&& studentTransactionDTO.getLateFee().getSem8() > 0 && totalAmount > 0) {
				lateSem8 = studentTransactionDTO.getLateFee().getSem8();
				TemporaryRazorPayPaymentDetails lateFee8 = new TemporaryRazorPayPaymentDetails();
				lateFee8.setYear(4);
				lateFee8.setSem(8);
				lateFee8.setReceiptType("Bulk Fee");
				lateFee8.setPaymentType("College Fee");
				lateFee8.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee8.setAmount((double) lateSem8);
				lateFee8.setPaidYear(lateFee8.getSem());
				lateFee8.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee8);
				totalAmount = lateSem8 > totalAmount ? lateSem8 - totalAmount : totalAmount - lateSem8;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem9())
					&& studentTransactionDTO.getLateFee().getSem9() > 0 && totalAmount > 0) {
				lateSem9 = studentTransactionDTO.getLateFee().getSem9();
				TemporaryRazorPayPaymentDetails lateFee9 = new TemporaryRazorPayPaymentDetails();
				lateFee9.setYear(5);
				lateFee9.setSem(9);
				lateFee9.setReceiptType("Bulk Fee");
				lateFee9.setPaymentType("College Fee");
				lateFee9.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee9.setAmount((double) lateSem9);
				lateFee9.setPaidYear(lateFee9.getSem());
				lateFee9.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee9);
				totalAmount = lateSem9 > totalAmount ? lateSem9 - totalAmount : totalAmount - lateSem9;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem10())
					&& studentTransactionDTO.getLateFee().getSem10() > 0 && totalAmount > 0) {
				lateSem10 = studentTransactionDTO.getLateFee().getSem10();
				TemporaryRazorPayPaymentDetails lateFee10 = new TemporaryRazorPayPaymentDetails();
				lateFee10.setYear(5);
				lateFee10.setSem(10);
				lateFee10.setReceiptType("Bulk Fee");
				lateFee10.setPaymentType("College Fee");
				lateFee10.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee10.setAmount((double) lateSem10);
				lateFee10.setPaidYear(lateFee10.getSem());
				lateFee10.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee10);
				totalAmount = lateSem10 > totalAmount ? lateSem10 - totalAmount : totalAmount - lateSem10;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem11())
					&& studentTransactionDTO.getLateFee().getSem11() > 0 && totalAmount > 0) {
				lateSem11 = studentTransactionDTO.getLateFee().getSem11();
				TemporaryRazorPayPaymentDetails lateFee11 = new TemporaryRazorPayPaymentDetails();
				lateFee11.setYear(6);
				lateFee11.setSem(11);
				lateFee11.setReceiptType("Bulk Fee");
				lateFee11.setPaymentType("College Fee");
				lateFee11.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee11.setAmount((double) lateSem11);
				lateFee11.setPaidYear(lateFee11.getSem());
				lateFee11.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee11);
				totalAmount = lateSem11 > totalAmount ? lateSem11 - totalAmount : totalAmount - lateSem11;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getLateFee().getSem12())
					&& studentTransactionDTO.getLateFee().getSem12() > 0 && totalAmount > 0) {
				lateSem12 = studentTransactionDTO.getLateFee().getSem12();
				TemporaryRazorPayPaymentDetails lateFee12 = new TemporaryRazorPayPaymentDetails();
				lateFee12.setYear(6);
				lateFee12.setSem(12);
				lateFee12.setReceiptType("Bulk Fee");
				lateFee12.setPaymentType("College Fee");
				lateFee12.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				lateFee12.setAmount((double) lateSem12);
				lateFee12.setPaidYear(lateFee12.getSem());
				lateFee12.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(lateFee12);
				totalAmount = lateSem12 > totalAmount ? lateSem12 - totalAmount : totalAmount - lateSem12;

			}
		}

		temporaryRazorPayPaymentDetailsRepository.saveAll(temporaryRazorPayPaymentDetail);

		return totalAmount;

	}

	private Float getCollegeFeeForPayment(StudentTransactionDTO studentTransactionDTO,
										  TemporaryRazorPayTransaction razorPayTransaction, Float totalAmount) {
		List<TemporaryRazorPayPaymentDetails> temporaryRazorPayPaymentDetail = new LinkedList<>();

		Integer balanceSem1 = 0;
		Integer balanceSem2 = 0;
		Integer balanceSem3 = 0;
		Integer balanceSem4 = 0;
		Integer balanceSem5 = 0;
		Integer balanceSem6 = 0;
		Integer balanceSem7 = 0;
		Integer balanceSem8 = 0;
		Integer balanceSem9 = 0;
		Integer balanceSem10 = 0;
		Integer balanceSem11 = 0;
		Integer balanceSem12 = 0;
		if (ObjectUtils.isNotEmpty(razorPayTransaction) && ObjectUtils.isNotEmpty(studentTransactionDTO)
				&& ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate()) && totalAmount > 0) {

			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem1())
					&& studentTransactionDTO.getFeeTemplate().getSem1() > 0 && totalAmount > 0) {
				balanceSem1 = studentTransactionDTO.getFeeTemplate().getSem1();
				TemporaryRazorPayPaymentDetails addOnFee1 = new TemporaryRazorPayPaymentDetails();
				addOnFee1.setYear(1);
				addOnFee1.setSem(1);
				addOnFee1.setReceiptType("College Fee");
				addOnFee1.setPaymentType("College Fee");
				addOnFee1.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee1.setAmount(
						(double) ((double) totalAmount > 0 && totalAmount >= balanceSem1 ? balanceSem1 : totalAmount));
				totalAmount = totalAmount - balanceSem1;
				addOnFee1.setPaidYear(addOnFee1.getSem());
				addOnFee1.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee1);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem2())
					&& studentTransactionDTO.getFeeTemplate().getSem2() > 0 && totalAmount > 0) {
				balanceSem2 = studentTransactionDTO.getFeeTemplate().getSem2();
				TemporaryRazorPayPaymentDetails addOnFee2 = new TemporaryRazorPayPaymentDetails();
				addOnFee2.setYear(1);
				addOnFee2.setSem(2);
				addOnFee2.setReceiptType("College Fee");
				addOnFee2.setPaymentType("College Fee");
				addOnFee2.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee2.setAmount(
						(double) ((double) totalAmount > 0 && totalAmount >= balanceSem2 ? balanceSem2 : totalAmount));
				totalAmount = totalAmount - balanceSem2;
				addOnFee2.setPaidYear(addOnFee2.getSem());
				addOnFee2.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee2);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem3())
					&& studentTransactionDTO.getFeeTemplate().getSem3() > 0 && totalAmount > 0) {
				balanceSem3 = studentTransactionDTO.getFeeTemplate().getSem3();
				TemporaryRazorPayPaymentDetails addOnFee3 = new TemporaryRazorPayPaymentDetails();
				addOnFee3.setYear(2);
				addOnFee3.setSem(3);
				addOnFee3.setReceiptType("College Fee");
				addOnFee3.setPaymentType("College Fee");
				addOnFee3.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee3.setAmount(
						(double) ((double) totalAmount > 0 && totalAmount >= balanceSem3 ? balanceSem3 : totalAmount));
				totalAmount = totalAmount - balanceSem3;
				addOnFee3.setPaidYear(addOnFee3.getSem());
				addOnFee3.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee3);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem4())
					&& studentTransactionDTO.getFeeTemplate().getSem4() > 0 && totalAmount > 0) {
				balanceSem4 = studentTransactionDTO.getFeeTemplate().getSem4();
				TemporaryRazorPayPaymentDetails addOnFee4 = new TemporaryRazorPayPaymentDetails();
				addOnFee4.setYear(2);
				addOnFee4.setSem(4);
				addOnFee4.setReceiptType("College Fee");
				addOnFee4.setPaymentType("College Fee");
				addOnFee4.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee4.setAmount(
						(double) ((double) totalAmount > 0 && totalAmount >= balanceSem4 ? balanceSem4 : totalAmount));
				totalAmount = totalAmount - balanceSem4;
				addOnFee4.setPaidYear(addOnFee4.getSem());
				addOnFee4.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee4);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem5())
					&& studentTransactionDTO.getFeeTemplate().getSem5() > 0 && totalAmount > 0) {
				balanceSem5 = studentTransactionDTO.getFeeTemplate().getSem5();
				TemporaryRazorPayPaymentDetails addOnFee5 = new TemporaryRazorPayPaymentDetails();
				addOnFee5.setYear(3);
				addOnFee5.setSem(5);
				addOnFee5.setReceiptType("College Fee");
				addOnFee5.setPaymentType("College Fee");
				addOnFee5.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee5.setAmount(
						(double) ((double) totalAmount > 0 && totalAmount >= balanceSem5 ? balanceSem5 : totalAmount));
				totalAmount = totalAmount - balanceSem5;
				addOnFee5.setPaidYear(addOnFee5.getSem());
				addOnFee5.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee5);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem6())
					&& studentTransactionDTO.getFeeTemplate().getSem6() > 0 && totalAmount > 0) {
				balanceSem6 = studentTransactionDTO.getFeeTemplate().getSem6();
				TemporaryRazorPayPaymentDetails addOnFee6 = new TemporaryRazorPayPaymentDetails();
				addOnFee6.setYear(3);
				addOnFee6.setSem(6);
				addOnFee6.setReceiptType("College Fee");
				addOnFee6.setPaymentType("College Fee");
				addOnFee6.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee6.setAmount(
						(double) ((double) totalAmount > 0 && totalAmount >= balanceSem6 ? balanceSem6 : totalAmount));
				totalAmount = totalAmount - balanceSem6;
				addOnFee6.setPaidYear(addOnFee6.getSem());
				addOnFee6.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee6);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem7())
					&& studentTransactionDTO.getFeeTemplate().getSem7() > 0 && totalAmount > 0) {
				balanceSem7 = studentTransactionDTO.getFeeTemplate().getSem7();
				TemporaryRazorPayPaymentDetails addOnFee7 = new TemporaryRazorPayPaymentDetails();
				addOnFee7.setYear(4);
				addOnFee7.setSem(7);
				addOnFee7.setReceiptType("College Fee");
				addOnFee7.setPaymentType("College Fee");
				addOnFee7.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee7.setAmount(
						(double) ((double) totalAmount > 0 && totalAmount >= balanceSem7 ? balanceSem7 : totalAmount));
				totalAmount = totalAmount - balanceSem7;
				addOnFee7.setPaidYear(addOnFee7.getSem());
				addOnFee7.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee7);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem8())
					&& studentTransactionDTO.getFeeTemplate().getSem8() > 0 && totalAmount > 0) {
				balanceSem8 = studentTransactionDTO.getFeeTemplate().getSem8();
				TemporaryRazorPayPaymentDetails addOnFee8 = new TemporaryRazorPayPaymentDetails();
				addOnFee8.setYear(4);
				addOnFee8.setSem(8);
				addOnFee8.setReceiptType("College Fee");
				addOnFee8.setPaymentType("College Fee");
				addOnFee8.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee8.setAmount(
						(double) ((double) totalAmount > 0 && totalAmount >= balanceSem8 ? balanceSem8 : totalAmount));
				totalAmount = totalAmount - balanceSem8;
				addOnFee8.setPaidYear(addOnFee8.getSem());
				addOnFee8.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee8);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem9())
					&& studentTransactionDTO.getFeeTemplate().getSem9() > 0 && totalAmount > 0) {
				balanceSem9 = studentTransactionDTO.getFeeTemplate().getSem9();
				TemporaryRazorPayPaymentDetails addOnFee9 = new TemporaryRazorPayPaymentDetails();
				addOnFee9.setYear(5);
				addOnFee9.setSem(9);
				addOnFee9.setReceiptType("College Fee");
				addOnFee9.setPaymentType("College Fee");
				addOnFee9.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee9.setAmount(
						(double) ((double) totalAmount > 0 && totalAmount >= balanceSem9 ? balanceSem9 : totalAmount));
				totalAmount = totalAmount - balanceSem9;
				addOnFee9.setPaidYear(addOnFee9.getSem());
				addOnFee9.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee9);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem10())
					&& studentTransactionDTO.getFeeTemplate().getSem10() > 0 && totalAmount > 0) {
				balanceSem10 = studentTransactionDTO.getFeeTemplate().getSem10();
				TemporaryRazorPayPaymentDetails addOnFee10 = new TemporaryRazorPayPaymentDetails();
				addOnFee10.setYear(5);
				addOnFee10.setSem(10);
				addOnFee10.setReceiptType("College Fee");
				addOnFee10.setPaymentType("College Fee");
				addOnFee10.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee10.setAmount((double) ((double) totalAmount > 0 && totalAmount >= balanceSem10 ? balanceSem10
						: totalAmount));
				totalAmount = totalAmount - balanceSem10;
				addOnFee10.setPaidYear(addOnFee10.getSem());
				addOnFee10.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee10);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem11())
					&& studentTransactionDTO.getFeeTemplate().getSem11() > 0 && totalAmount > 0) {
				balanceSem11 = studentTransactionDTO.getFeeTemplate().getSem11();
				TemporaryRazorPayPaymentDetails addOnFee11 = new TemporaryRazorPayPaymentDetails();
				addOnFee11.setYear(6);
				addOnFee11.setSem(11);
				addOnFee11.setReceiptType("College Fee");
				addOnFee11.setPaymentType("College Fee");
				addOnFee11.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee11.setAmount((double) ((double) totalAmount > 0 && totalAmount >= balanceSem11 ? balanceSem11
						: totalAmount));
				totalAmount = totalAmount - balanceSem11;
				addOnFee11.setPaidYear(addOnFee11.getSem());
				addOnFee11.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee11);

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeTemplate().getSem12())
					&& studentTransactionDTO.getFeeTemplate().getSem12() > 0 && totalAmount > 0) {
				balanceSem12 = studentTransactionDTO.getFeeTemplate().getSem12();
				TemporaryRazorPayPaymentDetails addOnFee12 = new TemporaryRazorPayPaymentDetails();
				addOnFee12.setYear(6);
				addOnFee12.setSem(12);
				addOnFee12.setReceiptType("College Fee");
				addOnFee12.setPaymentType("College Fee");
				addOnFee12.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee12.setAmount((double) ((double) totalAmount > 0 && totalAmount >= balanceSem12 ? balanceSem12
						: totalAmount));
				totalAmount = totalAmount - balanceSem12;
				addOnFee12.setPaidYear(addOnFee12.getSem());
				addOnFee12.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee12);

			}
		}

		temporaryRazorPayPaymentDetailsRepository.saveAll(temporaryRazorPayPaymentDetail);
		return totalAmount;

	}

	private Float getAddOnForPayment(StudentTransactionDTO studentTransactionDTO,
									 TemporaryRazorPayTransaction razorPayTransaction, Float totalAmount) {
		List<TemporaryRazorPayPaymentDetails> temporaryRazorPayPaymentDetail = new LinkedList<>();

		Integer addOnSem1 = 0;
		Integer addOnSem2 = 0;
		Integer addOnSem3 = 0;
		Integer addOnSem4 = 0;
		Integer addOnSem5 = 0;
		Integer addOnSem6 = 0;
		Integer addOnSem7 = 0;
		Integer addOnSem8 = 0;
		Integer addOnSem9 = 0;
		Integer addOnSem10 = 0;
		Integer addOnSem11 = 0;
		Integer addOnSem12 = 0;
		if (ObjectUtils.isNotEmpty(razorPayTransaction) && ObjectUtils.isNotEmpty(studentTransactionDTO)
				&& ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma()) && totalAmount > 0) {

			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem1())
					&& studentTransactionDTO.getFeeCma().getSem1() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 1) {
				addOnSem1 = studentTransactionDTO.getFeeCma().getSem1();
				TemporaryRazorPayPaymentDetails addOnFee1 = new TemporaryRazorPayPaymentDetails();
				addOnFee1.setYear(1);
				addOnFee1.setSem(1);
				addOnFee1.setReceiptType("Add On Fee");
				addOnFee1.setPaymentType("College Fee");
				addOnFee1.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee1.setAmount((double) addOnSem1);
				addOnFee1.setPaidYear(addOnFee1.getSem());
				addOnFee1.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee1);
				totalAmount = addOnSem1 > totalAmount ? addOnSem1 - totalAmount : totalAmount - addOnSem1;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem2())
					&& studentTransactionDTO.getFeeCma().getSem2() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 2) {
				addOnSem2 = studentTransactionDTO.getFeeCma().getSem2();
				TemporaryRazorPayPaymentDetails addOnFee2 = new TemporaryRazorPayPaymentDetails();
				addOnFee2.setYear(1);
				addOnFee2.setSem(2);
				addOnFee2.setReceiptType("Add On Fee");
				addOnFee2.setPaymentType("College Fee");
				addOnFee2.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee2.setAmount((double) addOnSem2);
				addOnFee2.setPaidYear(addOnFee2.getSem());
				addOnFee2.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee2);
				totalAmount = addOnSem2 > totalAmount ? addOnSem2 - totalAmount : totalAmount - addOnSem2;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem3())
					&& studentTransactionDTO.getFeeCma().getSem3() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 3) {
				addOnSem3 = studentTransactionDTO.getFeeCma().getSem3();
				TemporaryRazorPayPaymentDetails addOnFee3 = new TemporaryRazorPayPaymentDetails();
				addOnFee3.setYear(2);
				addOnFee3.setSem(3);
				addOnFee3.setReceiptType("Add On Fee");
				addOnFee3.setPaymentType("College Fee");
				addOnFee3.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee3.setAmount((double) addOnSem3);
				addOnFee3.setPaidYear(addOnFee3.getSem());
				addOnFee3.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee3);
				totalAmount = addOnSem3 > totalAmount ? addOnSem3 - totalAmount : totalAmount - addOnSem3;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem4())
					&& studentTransactionDTO.getFeeCma().getSem4() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 4) {
				addOnSem4 = studentTransactionDTO.getFeeCma().getSem4();
				TemporaryRazorPayPaymentDetails addOnFee4 = new TemporaryRazorPayPaymentDetails();
				addOnFee4.setYear(2);
				addOnFee4.setSem(4);
				addOnFee4.setReceiptType("Add On Fee");
				addOnFee4.setPaymentType("College Fee");
				addOnFee4.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee4.setAmount((double) addOnSem4);
				addOnFee4.setPaidYear(addOnFee4.getSem());
				addOnFee4.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee4);
				totalAmount = addOnSem4 > totalAmount ? addOnSem4 - totalAmount : totalAmount - addOnSem4;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem5())
					&& studentTransactionDTO.getFeeCma().getSem5() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 5) {
				addOnSem5 = studentTransactionDTO.getFeeCma().getSem5();
				TemporaryRazorPayPaymentDetails addOnFee5 = new TemporaryRazorPayPaymentDetails();
				addOnFee5.setYear(3);
				addOnFee5.setSem(5);
				addOnFee5.setReceiptType("Add On Fee");
				addOnFee5.setPaymentType("College Fee");
				addOnFee5.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee5.setAmount((double) addOnSem5);
				addOnFee5.setPaidYear(addOnFee5.getSem());
				addOnFee5.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee5);
				totalAmount = addOnSem5 > totalAmount ? addOnSem5 - totalAmount : totalAmount - addOnSem5;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem6())
					&& studentTransactionDTO.getFeeCma().getSem6() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 6) {
				addOnSem6 = studentTransactionDTO.getFeeCma().getSem6();
				TemporaryRazorPayPaymentDetails addOnFee6 = new TemporaryRazorPayPaymentDetails();
				addOnFee6.setYear(3);
				addOnFee6.setSem(6);
				addOnFee6.setReceiptType("Add On Fee");
				addOnFee6.setPaymentType("College Fee");
				addOnFee6.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee6.setAmount((double) addOnSem6);
				addOnFee6.setPaidYear(addOnFee6.getSem());
				addOnFee6.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee6);
				totalAmount = addOnSem6 > totalAmount ? addOnSem6 - totalAmount : totalAmount - addOnSem6;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem7())
					&& studentTransactionDTO.getFeeCma().getSem7() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 7) {
				addOnSem7 = studentTransactionDTO.getFeeCma().getSem7();
				TemporaryRazorPayPaymentDetails addOnFee7 = new TemporaryRazorPayPaymentDetails();
				addOnFee7.setYear(4);
				addOnFee7.setSem(7);
				addOnFee7.setReceiptType("Add On Fee");
				addOnFee7.setPaymentType("College Fee");
				addOnFee7.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee7.setAmount((double) addOnSem7);
				addOnFee7.setPaidYear(addOnFee7.getSem());
				addOnFee7.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee7);
				totalAmount = addOnSem7 > totalAmount ? addOnSem7 - totalAmount : totalAmount - addOnSem7;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem8())
					&& studentTransactionDTO.getFeeCma().getSem8() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 8) {
				addOnSem8 = studentTransactionDTO.getFeeCma().getSem8();
				TemporaryRazorPayPaymentDetails addOnFee8 = new TemporaryRazorPayPaymentDetails();
				addOnFee8.setYear(4);
				addOnFee8.setSem(8);
				addOnFee8.setReceiptType("Add On Fee");
				addOnFee8.setPaymentType("College Fee");
				addOnFee8.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee8.setAmount((double) addOnSem8);
				addOnFee8.setPaidYear(addOnFee8.getSem());
				addOnFee8.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee8);
				totalAmount = addOnSem8 > totalAmount ? addOnSem8 - totalAmount : totalAmount - addOnSem8;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem9())
					&& studentTransactionDTO.getFeeCma().getSem9() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 9) {
				addOnSem9 = studentTransactionDTO.getFeeCma().getSem9();
				TemporaryRazorPayPaymentDetails addOnFee9 = new TemporaryRazorPayPaymentDetails();
				addOnFee9.setYear(5);
				addOnFee9.setSem(9);
				addOnFee9.setReceiptType("Add On Fee");
				addOnFee9.setPaymentType("College Fee");
				addOnFee9.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee9.setAmount((double) addOnSem9);
				addOnFee9.setPaidYear(addOnFee9.getSem());
				addOnFee9.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee9);
				totalAmount = addOnSem9 > totalAmount ? addOnSem9 - totalAmount : totalAmount - addOnSem9;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem10())
					&& studentTransactionDTO.getFeeCma().getSem10() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 10) {
				addOnSem10 = studentTransactionDTO.getFeeCma().getSem10();
				TemporaryRazorPayPaymentDetails addOnFee10 = new TemporaryRazorPayPaymentDetails();
				addOnFee10.setYear(5);
				addOnFee10.setSem(10);
				addOnFee10.setReceiptType("Add On Fee");
				addOnFee10.setPaymentType("College Fee");
				addOnFee10.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee10.setAmount((double) addOnSem10);
				addOnFee10.setPaidYear(addOnFee10.getSem());
				addOnFee10.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee10);
				totalAmount = addOnSem10 > totalAmount ? addOnSem10 - totalAmount : totalAmount - addOnSem10;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem11())
					&& studentTransactionDTO.getFeeCma().getSem11() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 11) {
				addOnSem11 = studentTransactionDTO.getFeeCma().getSem11();
				TemporaryRazorPayPaymentDetails addOnFee11 = new TemporaryRazorPayPaymentDetails();
				addOnFee11.setYear(6);
				addOnFee11.setSem(11);
				addOnFee11.setReceiptType("Add On Fee");
				addOnFee11.setPaymentType("College Fee");
				addOnFee11.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee11.setAmount((double) addOnSem11);
				addOnFee11.setPaidYear(addOnFee11.getSem());
				addOnFee11.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee11);
				totalAmount = addOnSem11 > totalAmount ? addOnSem11 - totalAmount : totalAmount - addOnSem11;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getFeeCma().getSem12())
					&& studentTransactionDTO.getFeeCma().getSem12() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 12) {
				addOnSem12 = studentTransactionDTO.getFeeCma().getSem12();
				TemporaryRazorPayPaymentDetails addOnFee12 = new TemporaryRazorPayPaymentDetails();
				addOnFee12.setYear(6);
				addOnFee12.setSem(12);
				addOnFee12.setReceiptType("Add On Fee");
				addOnFee12.setPaymentType("College Fee");
				addOnFee12.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				addOnFee12.setAmount((double) addOnSem12);
				addOnFee12.setPaidYear(addOnFee12.getSem());
				addOnFee12.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(addOnFee12);
				totalAmount = addOnSem12 > totalAmount ? addOnSem12 - totalAmount : totalAmount - addOnSem12;

			}
		}

		temporaryRazorPayPaymentDetailsRepository.saveAll(temporaryRazorPayPaymentDetail);
		return totalAmount;

	}

	@SuppressWarnings("unused")
	private Float getUniformFeeForPayment(StudentTransactionDTO studentTransactionDTO,
										  TemporaryRazorPayTransaction razorPayTransaction, Float totalAmount) {
		List<TemporaryRazorPayPaymentDetails> temporaryRazorPayPaymentDetail = new LinkedList<>();
		float totalUniformFee = 0;
		Integer uniformSem1 = 0;
		Integer uniformSem2 = 0;
		Integer uniformSem3 = 0;
		Integer uniformSem4 = 0;
		Integer uniformSem5 = 0;
		Integer uniformSem6 = 0;
		Integer uniformSem7 = 0;
		Integer uniformSem8 = 0;
		Integer uniformSem9 = 0;
		Integer uniformSem10 = 0;
		Integer uniformSem11 = 0;
		Integer uniformSem12 = 0;
		if (ObjectUtils.isNotEmpty(razorPayTransaction) && ObjectUtils.isNotEmpty(studentTransactionDTO)
				&& ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary())) {

			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem1())
					&& studentTransactionDTO.getUniformAndStationary().getSem1() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 1) {
				uniformSem1 = studentTransactionDTO.getUniformAndStationary().getSem1();
				TemporaryRazorPayPaymentDetails uniformFee1 = new TemporaryRazorPayPaymentDetails();
				uniformFee1.setYear(1);
				uniformFee1.setSem(1);
				uniformFee1.setReceiptType("Uniform Fee");
				uniformFee1.setPaymentType("College Fee");
				uniformFee1.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee1.setAmount((double) uniformSem1);
				uniformFee1.setPaidYear(uniformFee1.getSem());
				uniformFee1.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee1);
				totalAmount = uniformSem1 > totalAmount ? uniformSem1 - totalAmount : totalAmount - uniformSem1;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem2())
					&& studentTransactionDTO.getUniformAndStationary().getSem2() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 2) {
				uniformSem2 = studentTransactionDTO.getUniformAndStationary().getSem2();
				TemporaryRazorPayPaymentDetails uniformFee2 = new TemporaryRazorPayPaymentDetails();
				uniformFee2.setYear(1);
				uniformFee2.setSem(2);
				uniformFee2.setReceiptType("Uniform Fee");
				uniformFee2.setPaymentType("College Fee");
				uniformFee2.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee2.setAmount((double) uniformSem2);
				uniformFee2.setPaidYear(uniformFee2.getSem());
				uniformFee2.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee2);
				totalAmount = uniformSem2 > totalAmount ? uniformSem2 - totalAmount : totalAmount - uniformSem2;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem3())
					&& studentTransactionDTO.getUniformAndStationary().getSem3() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 3) {
				uniformSem3 = studentTransactionDTO.getUniformAndStationary().getSem3();
				TemporaryRazorPayPaymentDetails uniformFee3 = new TemporaryRazorPayPaymentDetails();
				uniformFee3.setYear(2);
				uniformFee3.setSem(3);
				uniformFee3.setReceiptType("Uniform Fee");
				uniformFee3.setPaymentType("College Fee");
				uniformFee3.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee3.setAmount((double) uniformSem3);
				uniformFee3.setPaidYear(uniformFee3.getSem());
				uniformFee3.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee3);
				totalAmount = uniformSem3 > totalAmount ? uniformSem3 - totalAmount : totalAmount - uniformSem3;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem4())
					&& studentTransactionDTO.getUniformAndStationary().getSem4() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 4) {
				uniformSem4 = studentTransactionDTO.getUniformAndStationary().getSem4();
				TemporaryRazorPayPaymentDetails uniformFee4 = new TemporaryRazorPayPaymentDetails();
				uniformFee4.setYear(2);
				uniformFee4.setSem(4);
				uniformFee4.setReceiptType("Uniform Fee");
				uniformFee4.setPaymentType("College Fee");
				uniformFee4.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee4.setAmount((double) uniformSem4);
				uniformFee4.setPaidYear(uniformFee4.getSem());
				uniformFee4.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee4);
				totalAmount = uniformSem4 > totalAmount ? uniformSem4 - totalAmount : totalAmount - uniformSem4;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem5())
					&& studentTransactionDTO.getUniformAndStationary().getSem5() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 5) {
				uniformSem5 = studentTransactionDTO.getUniformAndStationary().getSem5();
				TemporaryRazorPayPaymentDetails uniformFee5 = new TemporaryRazorPayPaymentDetails();
				uniformFee5.setYear(3);
				uniformFee5.setSem(5);
				uniformFee5.setReceiptType("Uniform Fee");
				uniformFee5.setPaymentType("College Fee");
				uniformFee5.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee5.setAmount((double) uniformSem5);
				uniformFee5.setPaidYear(uniformFee5.getSem());
				uniformFee5.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee5);
				totalAmount = uniformSem5 > totalAmount ? uniformSem5 - totalAmount : totalAmount - uniformSem5;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem6())
					&& studentTransactionDTO.getUniformAndStationary().getSem6() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 6) {
				uniformSem6 = studentTransactionDTO.getUniformAndStationary().getSem6();
				TemporaryRazorPayPaymentDetails uniformFee6 = new TemporaryRazorPayPaymentDetails();
				uniformFee6.setYear(3);
				uniformFee6.setSem(6);
				uniformFee6.setReceiptType("Uniform Fee");
				uniformFee6.setPaymentType("College Fee");
				uniformFee6.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee6.setAmount((double) uniformSem6);
				uniformFee6.setPaidYear(uniformFee6.getSem());
				uniformFee6.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee6);
				totalAmount = uniformSem6 > totalAmount ? uniformSem6 - totalAmount : totalAmount - uniformSem6;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem7())
					&& studentTransactionDTO.getUniformAndStationary().getSem7() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 7) {
				uniformSem7 = studentTransactionDTO.getUniformAndStationary().getSem7();
				TemporaryRazorPayPaymentDetails uniformFee7 = new TemporaryRazorPayPaymentDetails();
				uniformFee7.setYear(4);
				uniformFee7.setSem(7);
				uniformFee7.setReceiptType("Uniform Fee");
				uniformFee7.setPaymentType("College Fee");
				uniformFee7.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee7.setAmount((double) uniformSem7);
				uniformFee7.setPaidYear(uniformFee7.getSem());
				uniformFee7.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee7);
				totalAmount = uniformSem7 > totalAmount ? uniformSem7 - totalAmount : totalAmount - uniformSem7;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem8())
					&& studentTransactionDTO.getUniformAndStationary().getSem8() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 8) {
				uniformSem8 = studentTransactionDTO.getUniformAndStationary().getSem8();
				TemporaryRazorPayPaymentDetails uniformFee8 = new TemporaryRazorPayPaymentDetails();
				uniformFee8.setYear(4);
				uniformFee8.setSem(8);
				uniformFee8.setReceiptType("Uniform Fee");
				uniformFee8.setPaymentType("College Fee");
				uniformFee8.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee8.setAmount((double) uniformSem8);
				uniformFee8.setPaidYear(uniformFee8.getSem());
				uniformFee8.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee8);
				totalAmount = uniformSem8 > totalAmount ? uniformSem8 - totalAmount : totalAmount - uniformSem8;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem9())
					&& studentTransactionDTO.getUniformAndStationary().getSem9() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 9) {
				uniformSem9 = studentTransactionDTO.getUniformAndStationary().getSem9();
				TemporaryRazorPayPaymentDetails uniformFee9 = new TemporaryRazorPayPaymentDetails();
				uniformFee9.setYear(5);
				uniformFee9.setSem(9);
				uniformFee9.setReceiptType("Uniform Fee");
				uniformFee9.setPaymentType("College Fee");
				uniformFee9.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee9.setAmount((double) uniformSem9);
				uniformFee9.setPaidYear(uniformFee9.getSem());
				uniformFee9.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee9);
				totalAmount = uniformSem9 > totalAmount ? uniformSem9 - totalAmount : totalAmount - uniformSem9;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem10())
					&& studentTransactionDTO.getUniformAndStationary().getSem10() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 10) {
				uniformSem10 = studentTransactionDTO.getUniformAndStationary().getSem10();
				TemporaryRazorPayPaymentDetails uniformFee10 = new TemporaryRazorPayPaymentDetails();
				uniformFee10.setYear(5);
				uniformFee10.setSem(10);
				uniformFee10.setReceiptType("Uniform Fee");
				uniformFee10.setPaymentType("College Fee");
				uniformFee10.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee10.setAmount((double) uniformSem10);
				uniformFee10.setPaidYear(uniformFee10.getSem());
				uniformFee10.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee10);
				totalAmount = uniformSem10 > totalAmount ? uniformSem10 - totalAmount : totalAmount - uniformSem10;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem11())
					&& studentTransactionDTO.getUniformAndStationary().getSem11() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 11) {
				uniformSem11 = studentTransactionDTO.getUniformAndStationary().getSem11();
				TemporaryRazorPayPaymentDetails uniformFee11 = new TemporaryRazorPayPaymentDetails();
				uniformFee11.setYear(6);
				uniformFee11.setSem(11);
				uniformFee11.setReceiptType("Uniform Fee");
				uniformFee11.setPaymentType("College Fee");
				uniformFee11.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee11.setAmount((double) uniformSem11);
				uniformFee11.setPaidYear(uniformFee11.getSem());
				uniformFee11.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee11);
				totalAmount = uniformSem11 > totalAmount ? uniformSem11 - totalAmount : totalAmount - uniformSem11;

			}
			if (ObjectUtils.isNotEmpty(studentTransactionDTO.getUniformAndStationary().getSem12())
					&& studentTransactionDTO.getUniformAndStationary().getSem12() > 0 && totalAmount > 0 && studentTransactionDTO.getAllowSem() >= 12) {
				uniformSem12 = studentTransactionDTO.getUniformAndStationary().getSem12();
				TemporaryRazorPayPaymentDetails uniformFee12 = new TemporaryRazorPayPaymentDetails();
				uniformFee12.setYear(6);
				uniformFee12.setSem(12);
				uniformFee12.setReceiptType("Uniform Fee");
				uniformFee12.setPaymentType("College Fee");
				uniformFee12.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				uniformFee12.setAmount((double) uniformSem12);
				uniformFee12.setPaidYear(uniformFee12.getSem());
				uniformFee12.setAcYearId(razorPayTransaction.getAcYearId());
				temporaryRazorPayPaymentDetail.add(uniformFee12);
				totalAmount = uniformSem12 > totalAmount ? uniformSem12 - totalAmount : totalAmount - uniformSem12;

			}
		}

		temporaryRazorPayPaymentDetailsRepository.saveAll(temporaryRazorPayPaymentDetail);
		return totalAmount;

	}

	public ResponseEntity<Object> bulkPayment(BulkPaymentDTO bulkPaymentDTO) {
		try {
			RazorpayClient razorpayClient = razorPayConfig.getRazorpayClient(bulkPaymentDTO.getSchoolId());

			//Temporary razorpay transaction
			TemporaryRazorPayTransaction temporaryRazorPayTransaction = new TemporaryRazorPayTransaction();

			BulkTransaction razorPayTransaction = new BulkTransaction();
			if (bulkPaymentDTO.getStudentId() != null) {
				Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(bulkPaymentDTO.getStudentId());
				razorPayTransaction.setName(studentDetails.getStudent_name());
				razorPayTransaction.setEmail(studentDetails.getAcharya_email());
				temporaryRazorPayTransaction.setStudentId(bulkPaymentDTO.getStudentId());
			} else {
				razorPayTransaction.setName(bulkPaymentDTO.getName());
				razorPayTransaction.setEmail(bulkPaymentDTO.getEmail());
			}
			razorPayTransaction.setAmount(bulkPaymentDTO.getAmount());
			razorPayTransaction.setMobile(bulkPaymentDTO.getMobile());

			razorPayTransaction.setRemarks(bulkPaymentDTO.getRemarks());
			razorPayTransaction.setTransactionId(String.valueOf(bulkPaymentDTO.getFeePaymentWindowId()));
			razorPayTransaction.setTransactionType("Bulk");
			razorPayTransaction.setPaidYear(Year.now().toString());
			razorPayTransaction.setPaymentType(bulkPaymentDTO.getFeehead());
			razorPayTransaction.setSchoolId(bulkPaymentDTO.getSchoolId());
			String receiptId = "txn_" + System.currentTimeMillis();
			razorPayTransaction.setReceiptId(receiptId);
			razorPayTransaction.setVoucherHeadId(bulkPaymentDTO.getVoucherHeadId());
			float amountInPaise = bulkPaymentDTO.getAmount() * 100;

			JSONObject options = new JSONObject();
			options.put("amount", amountInPaise);
			options.put("currency", "INR");

			options.put("receipt", receiptId);
			JSONObject notes = new JSONObject();
			notes.put("amount", bulkPaymentDTO.getAmount());
			notes.put("email", bulkPaymentDTO.getEmail());
			notes.put("mobile", bulkPaymentDTO.getMobile());
			notes.put("name", bulkPaymentDTO.getName());
			options.put("notes", notes);

			System.out.println("Transaction ID: " + razorPayTransaction.getTransactionId());
			Optional<FeePaymentWindow> feePaymentWindowDetail = feePaymentWindowRepository.findById(bulkPaymentDTO.getFeePaymentWindowId());
			JSONArray transfers = new JSONArray();
			RouteAccountDetails routeAccountDetail = new RouteAccountDetails();
			if (feePaymentWindowDetail.isPresent() && ObjectUtils.isNotEmpty(feePaymentWindowDetail.get().getTransfer_type())) {
				String category = feePaymentWindowDetail.get().getTransfer_type();
				List<RouteAccountDetails> accountDetails = routeAccountDetailsRepository.getAccountDetails(feePaymentWindowDetail.get().getSchool_id());
				switch (category.toUpperCase()) {
					case "HOSTEL": {
						routeAccountDetail = accountDetails.stream()
								.filter(r -> r.getRouteType().equals("HOSTEL")).findFirst().get();
						if (ObjectUtils.isNotEmpty(routeAccountDetail) && bulkPaymentDTO.getAmount() > 0) {
							JSONObject transfer = new JSONObject();
							transfer.put("account", routeAccountDetail.getRouteAccountId());
							transfer.put("amount", bulkPaymentDTO.getAmount() * 100);
							transfer.put("currency", "INR");
							JSONObject hostelNotes = new JSONObject();
							hostelNotes.put("HostelBUlkFee", bulkPaymentDTO.getAmount() * 100);
							hostelNotes.put("email",
									ObjectUtils.isNotEmpty(bulkPaymentDTO.getEmail())
											? bulkPaymentDTO.getEmail()
											: "");
							hostelNotes.put("mobile",
									ObjectUtils.isNotEmpty(bulkPaymentDTO.getMobile())
											? bulkPaymentDTO.getMobile()
											: "");
							hostelNotes.put("name",
									ObjectUtils.isNotEmpty(razorPayTransaction) && ObjectUtils.isNotEmpty(razorPayTransaction.getName()) ? razorPayTransaction.getName()
											: "");
							transfer.put("notes", hostelNotes);
							transfers.put(transfer);

							razorPayTransaction.setTransferType("HOSB");
						} else {
							throw new ResourceNotFoundException("Route account or amount is not present");
						}
					}
					break;
					case "ADDON": {
						routeAccountDetail = accountDetails.stream()
								.filter(r -> r.getRouteType().equals("ADDON")).findFirst().get();
						if (ObjectUtils.isNotEmpty(routeAccountDetail) && bulkPaymentDTO.getAmount() > 0) {
							JSONObject transfer = new JSONObject();
							transfer.put("account", routeAccountDetail.getRouteAccountId());
							transfer.put("amount", bulkPaymentDTO.getAmount() * 100);
							transfer.put("currency", "INR");
							JSONObject addOnNotes = new JSONObject();
							addOnNotes.put("CMA", bulkPaymentDTO.getAmount() * 100);
							addOnNotes.put("email",
									ObjectUtils.isNotEmpty(bulkPaymentDTO.getEmail())
											? bulkPaymentDTO.getEmail()
											: "");
							addOnNotes.put("mobile",
									ObjectUtils.isNotEmpty(bulkPaymentDTO.getMobile())
											? bulkPaymentDTO.getMobile()
											: "");
							addOnNotes.put("name",
									ObjectUtils.isNotEmpty(razorPayTransaction) && ObjectUtils.isNotEmpty(razorPayTransaction.getName()) ? razorPayTransaction.getName()
											: "");
							transfer.put("notes", addOnNotes);
							transfers.put(transfer);
							razorPayTransaction.setTransferType("ADDON");
						} else {
							throw new ResourceNotFoundException("Route account or amount is not present");
						}
					}
					break;
					case "ALUMINI": {
						routeAccountDetail = accountDetails.stream()
								.filter(r -> r.getRouteType().equals("ALUMINI")).findFirst().get();
						if (ObjectUtils.isNotEmpty(routeAccountDetail) && bulkPaymentDTO.getAmount() > 0) {
							JSONObject transfer = new JSONObject();
							transfer.put("account", routeAccountDetail.getRouteAccountId());
							transfer.put("amount", bulkPaymentDTO.getAmount() * 100);
							transfer.put("currency", "INR");
							JSONObject aluminiNotes = new JSONObject();
							aluminiNotes.put("Alumini", bulkPaymentDTO.getAmount() * 100);
							aluminiNotes.put("email",
									ObjectUtils.isNotEmpty(bulkPaymentDTO.getEmail())
											? bulkPaymentDTO.getEmail()
											: "");
							aluminiNotes.put("mobile",
									ObjectUtils.isNotEmpty(bulkPaymentDTO.getMobile())
											? bulkPaymentDTO.getMobile()
											: "");
							aluminiNotes.put("auid",
									ObjectUtils.isNotEmpty(razorPayTransaction) && ObjectUtils.isNotEmpty(razorPayTransaction.getName()) ? razorPayTransaction.getName()
											: "");
							transfer.put("notes", aluminiNotes);
							transfers.put(transfer);
							razorPayTransaction.setTransferType("ALUMINI");
						} else {
							throw new ResourceNotFoundException("Route account or amount is not present");
						}
					}
					break;

					default:
						razorPayTransaction.setTransferType("NONE");
						break;
				}
			}


			options.put("transfers", transfers);
			Order order = razorpayClient.Orders.create(options);
			String jsonObject = order.toJson().toString();
			System.out.println(jsonObject);
			ObjectMapper objectMapper = new ObjectMapper();
			RazorPayOrder razorpayOrder = objectMapper.readValue(jsonObject, RazorPayOrder.class);

			String orderId = order.get("id");
			String transfer = Optional.ofNullable(razorpayOrder.getTransfers())
					.orElse(Collections.emptyList())
					.stream()
					.map(Transfer::getId)
					.filter(Objects::nonNull)
					.collect(Collectors.joining(","));
			razorPayTransaction.setOrderId(orderId);
			razorPayTransaction.setTransferId(transfer);
			bulkTransactionRepository.save(razorPayTransaction);

			// razorpay


//			Student_Details student = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.get);
			temporaryRazorPayTransaction.setAcYearId(bulkPaymentDTO.getAcYearId());
			temporaryRazorPayTransaction.setAmount(bulkPaymentDTO.getAmount());
			temporaryRazorPayTransaction.setCurrentSem(bulkPaymentDTO.getCurrentSem());
			temporaryRazorPayTransaction.setCurrentYear(bulkPaymentDTO.getCurrentYear());
			temporaryRazorPayTransaction.setTransactionType("Bulk");
			//temporaryRazorPayTransaction.setReceiptId(razorPayTransaction.getReceiptId());
			temporaryRazorPayTransaction.setOrderId(razorPayTransaction.getOrderId());
			//temporaryRazorPayTransaction.setStudentId(bulkPaymentDTO.getStudentId());
			temporaryRazorPayTransaction.setPaidYear(String.valueOf(Calendar.YEAR));
			temporaryRazorPayTransaction.setReceiptId(receiptId);
			temporaryRazorPayTransaction.setRemarks(bulkPaymentDTO.getRemarks());
			temporaryRazorPayTransaction.setTransactionId(bulkPaymentDTO.getFeePaymentWindowId());
			temporaryRazorPayTransactionRepository.save(temporaryRazorPayTransaction);
			//	List<TemporaryRazorPayPaymentDetails> razorPayPaymentDetails = new ArrayList<>();


			TemporaryRazorPayPaymentDetails razorpay = new TemporaryRazorPayPaymentDetails();
			razorpay.setReceiptType("Bulk");
			razorpay.setPaymentType(bulkPaymentDTO.getFeehead());
			razorpay.setRazorPayTransactionId(temporaryRazorPayTransaction.getRazorPayTransactionId());
			razorpay.setAmount(Double.valueOf(bulkPaymentDTO.getAmount()));
			razorpay.setSem(bulkPaymentDTO.getCurrentSem());
			razorpay.setPaidYear(bulkPaymentDTO.getCurrentSem());
			razorpay.setAcYearId(bulkPaymentDTO.getAcYearId());
			razorpay.setVoucherHeadId(bulkPaymentDTO.getVoucherHeadId());
			razorpay.setYear(bulkPaymentDTO.getCurrentYear());

			temporaryRazorPayPaymentDetailsRepository.save(razorpay);

			//	temporaryRazorPayPaymentDetailsRepository.saveAll(razorPayPaymentDetails);


			//end

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", razorpayOrder);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> paymentStatus(PaymentRequestDTO paymentRequestDTO) {
		try {
			if (StringUtils.equals(paymentRequestDTO.getStatus(), "success")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

				TemporaryRazorPayTransaction temporaryrazorPayTransaction = temporaryRazorPayTransactionRepository
						.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				RazorPayTransaction razorPayTransaction1 = razorPayTransactionRepository.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				if (ObjectUtils.isNotEmpty(temporaryrazorPayTransaction) && ObjectUtils.isEmpty(razorPayTransaction1)) {
					RazorPayTransaction razorPayTransaction = new RazorPayTransaction();
					razorPayTransaction.setPaymentId(paymentRequestDTO.getRazorpayPaymentId());
					razorPayTransaction.setSignature(paymentRequestDTO.getRazorpaySignature());
					razorPayTransaction.setStatus(paymentRequestDTO.getStatus());
					razorPayTransaction.setTransactionDate(temporaryrazorPayTransaction.getCreated_date());
					razorPayTransaction.setAcYearId(temporaryrazorPayTransaction.getAcYearId());
					razorPayTransaction.setAmount(temporaryrazorPayTransaction.getAmount());
					razorPayTransaction.setCurrentSem(temporaryrazorPayTransaction.getCurrentSem());
					razorPayTransaction.setCurrentYear(temporaryrazorPayTransaction.getCurrentYear());
					razorPayTransaction.setTransactionType(temporaryrazorPayTransaction.getTransactionType());
					razorPayTransaction.setPaidYear(Year.now().toString());
					razorPayTransaction.setOrderId(temporaryrazorPayTransaction.getOrderId());
					razorPayTransaction.setStudentId(temporaryrazorPayTransaction.getStudentId());
					razorPayTransaction.setReceiptId(temporaryrazorPayTransaction.getReceiptId());
					String currencyType = studentDetailsRepository.getCurrenyTypeByStudentId(razorPayTransaction.getStudentId());
					DollarToInrConversion dollarToInrConversion = calculateDollarRate(currencyType);
					if (ObjectUtils.isNotEmpty(dollarToInrConversion)) {
						razorPayTransaction.setDollarValue(dollarToInrConversion.getInr());
					}
					razorPayTransactionRepository.save(razorPayTransaction);
					temporaryrazorPayTransaction.setPaymentId(paymentRequestDTO.getRazorpayPaymentId());
					temporaryrazorPayTransaction.setStatus("success");
					temporaryRazorPayTransactionRepository.save(temporaryrazorPayTransaction);

					List<TemporaryRazorPayPaymentDetails> temporaryRazorPayPaymentDetail = temporaryRazorPayPaymentDetailsRepository
							.getByRazorPayTransactionId(temporaryrazorPayTransaction.getRazorPayTransactionId());
					List<RazorPayPaymentDetails> razorPayPaymentDetailsList = new ArrayList<>();

					for (TemporaryRazorPayPaymentDetails tempPay : temporaryRazorPayPaymentDetail) {
						RazorPayPaymentDetails razorPayPaymentDetails = new RazorPayPaymentDetails();
						razorPayPaymentDetails.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
						razorPayPaymentDetails.setAmount(tempPay.getAmount());
						razorPayPaymentDetails.setPaymentType(tempPay.getPaymentType());
						razorPayPaymentDetails.setReceiptType(tempPay.getReceiptType());
						razorPayPaymentDetails.setSem(tempPay.getSem());
						razorPayPaymentDetails.setYear(tempPay.getYear());
//					razorPayPaymentDetails
//							.setAcYearId(ObjectUtils.isNotEmpty(tempPay.getAcYearId()) ? tempPay.getAcYearId() : null);
						razorPayPaymentDetails.setAcYearId(tempPay.getAcYearId());
						razorPayPaymentDetails.setPaidYear(tempPay.getPaidYear());
						razorPayPaymentDetails.setVoucherHeadId(tempPay.getVoucherHeadId());
						razorPayPaymentDetailsList.add(razorPayPaymentDetails);

					}

					razorPayPaymentDetailsRepository.saveAll(razorPayPaymentDetailsList);
					Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
					List<CmaFeeReceipt> cmaReceipts = cmaFeeReceiptRepository.getCmaFeeReceiptByOrderId(razorPayTransaction.getOrderId());
					if (ObjectUtils.isEmpty(cmaReceipts)) {
						List<RazorPayPaymentDetails> razorPayPaymentDetailsAddOn = razorPayPaymentDetailsRepository
								.getByRazorPayTransactionIdAndReceiptType(razorPayTransaction.getRazorPayTransactionId(),
										"Add On Fee");

						Integer cmaFeeReceiptNumber = cmaFeeReceiptRepository.getLatestReceitpNumber(financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id()) + 1;
						for (RazorPayPaymentDetails addOn : razorPayPaymentDetailsAddOn) {
							CmaFeeReceipt cmaFeeReceipt = new CmaFeeReceipt();
							cmaFeeReceipt.setActive(Boolean.TRUE);
							cmaFeeReceipt.setAmount(addOn.getAmount());
							cmaFeeReceipt.setFinancial_year_id(
									financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
							cmaFeeReceipt.setStudent_id(razorPayTransaction.getStudentId());
							cmaFeeReceipt.setPaid_year(String.valueOf(addOn.getPaidYear()));
							cmaFeeReceipt.setReceipt_type(addOn.getReceiptType());
							cmaFeeReceipt.setSchool_id(schoolId);
							cmaFeeReceipt
									.setCma_receipt_id(ObjectUtils.isNotEmpty(cmaFeeReceiptNumber) ? cmaFeeReceiptNumber : 1);
							cmaFeeReceipt.setOrderId(paymentRequestDTO.getRazorpayOrderId());
							cmaFeeReceiptRepository.save(cmaFeeReceipt);
						}
					}
					List<UniformReceipt> uniformReceipts = uniformReceiptRepository.getUniformFeeReceiptByOrderId(razorPayTransaction.getOrderId());
					if (ObjectUtils.isEmpty(uniformReceipts)) {
						List<RazorPayPaymentDetails> razorPayPaymentDetailsUniform = razorPayPaymentDetailsRepository
								.getByRazorPayTransactionIdAndReceiptType(razorPayTransaction.getRazorPayTransactionId(),
										"Uniform Fee");
						Integer uniformRecieptNumber = uniformReceiptRepository.getLatestReceitpNumber() + 1;
						for (RazorPayPaymentDetails uniform : razorPayPaymentDetailsUniform) {
							String date = simpleDateFormat.format(razorPayTransaction.getTransactionDate());

							UniformReceipt uniformFeeReceipt = new UniformReceipt();
							uniformFeeReceipt.setActive(Boolean.TRUE);
							uniformFeeReceipt.setAmount(uniform.getAmount());
							uniformFeeReceipt.setFcYearId(financialYearRepository.getFinancialYearIdOnCurrentYear()
									.getFinancial_year_id().longValue());
							uniformFeeReceipt.setStudentId(razorPayTransaction.getStudentId().longValue());
							uniformFeeReceipt.setSem(uniform.getSem());
							uniformFeeReceipt.setYear(uniform.getYear());
							uniformFeeReceipt.setType("package");
							uniformFeeReceipt.setSchoolId(schoolId);
							uniformFeeReceipt.setOrderId(razorPayTransaction.getOrderId());
							uniformFeeReceipt.setUniformReceiptNo(
									ObjectUtils.isNotEmpty(uniformRecieptNumber) ? uniformRecieptNumber : 1);
							uniformFeeReceipt.setTransactionDate(date);
							uniformFeeReceipt.setPaidYear(uniform.getPaidYear());
							uniformReceiptRepository.save(uniformFeeReceipt);
						}
					}
					StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, razorPayTransaction.getStudentId(),
							null);
					applicationEventPublisher.publishEvent(studentDueEvent);
				}
			} else {
				TemporaryRazorPayTransaction razorPayTransaction = temporaryRazorPayTransactionRepository
						.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				razorPayTransaction.setCode(paymentRequestDTO.getCode());
				razorPayTransaction.setDescription(paymentRequestDTO.getDescription());
				razorPayTransaction.setSource(paymentRequestDTO.getSource());
				razorPayTransaction.setStep(paymentRequestDTO.getStep());
				razorPayTransaction.setReason(paymentRequestDTO.getReason());
				razorPayTransaction.setStatus(paymentRequestDTO.getStatus());
				temporaryRazorPayTransactionRepository.save(razorPayTransaction);

				return ResponseHandler.generateResponse(false, HttpStatus.OK, "FAILURE", "Payment Failed");

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public Object triggerEmailToStudentWithPdfContent(Integer candidate_id, MultipartFile file)
			throws MessagingException, IOException {
		String candidate_email;
		List<HashMap<String, Object>> candidate_details = can_repo.getCandidateDetailsByCandidateId(candidate_id);
		Integer npfStatus = candidateWalkinRepository.getNpfStatus(candidate_id);

		if (npfStatus == 1) {

			String mailBody = "Congratulations " + candidate_details.get(0).get("candidate_name") + " !!!" + "<br><br>"
					+ "We are pleased to inform you that our selection committee has reviewed your application and you have been shortlisted "
					+ "for admissions at " + candidate_details.get(0).get("school_name").toString().toUpperCase() + "-"
					+ candidate_details.get(0).get("program_name").toString().toUpperCase() + " "
					+ "and the offer letter for the same is attached." + "<br><br>"
					+ "You are now one step away from joining an academic legacy of over 30 years. We wish you the very best in writing your "
					+ "success story with us at Acharya." + "<br><br>"
					+ "Be advised, this offer letter is valid as per terms and conditions mentioned in document within which the provisional "
					+ "admission is to be secured. To do so, you are requested to accept the offer and complete the Registration Fee Payment.<br><br>"
					+ "Click on the 'Accept' button below or in your offer letter after reading the offer terms & conditions to confirm your acceptance.<br>"
					+ "<br/>" + "<a href= " + OFFER_ACCEPTANCE_URL + candidate_id
					+ " style='background-color: #4A57A9;color:white;text-decoration:none;padding:6px'>Accept</a>"
					+ "<br><br>" + "Once again, congratulations and all the best." + "<br><br>" + "regards,"
					+ "<br><br>" + "Team Admissions" + "<br>" + "Acharya Institutes" + "<br>" + "Bangalore - 560107."
					+ "<br>" + "Email - admissions@acharya.ac.in" + "<br><br><br><br><br><br>"
					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";

			if (ObjectUtils.isEmpty(candidate_details) || candidate_details == null)
				throw new RuntimeException("Candidate details not found");
			else
				candidate_email = candidate_details.get(0).get("candidate_email").toString();

			response_handler.sendEmailWithAttachment(file, candidate_details.get(0).get("counselorEmail").toString(),
					candidate_email, mailBody, "Acharya Offer Letter");
			LocalDate today = LocalDate.now();
			LocalDate newDate = today.plusDays(10);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formattedDate = newDate.format(formatter);
			candidateWalkinRepository.updateNpfStatusOfBeforeAcceptingOffer(candidate_id, formattedDate);

		} else if (npfStatus == 3) {

			StudentOfferAcceptance detailsByCandidateId = studentOfferAcceptanceRepository
					.getDetailsByCandidateId(candidate_id);
			String mailBody = "Dear " + candidate_details.get(0).get("candidate_name") + "," + "<br><br>"
					+ "We are pleased to confirm that you have accepted offer for the "
					+ candidate_details.get(0).get("program_name").toString().toUpperCase() + " at "
					+ candidate_details.get(0).get("school_name").toString().toUpperCase() + " "
					+ "details are as mentioned below. To finalize your acceptance and secure your seat, please proceed with the payment of your Registration Fee. "
					+ "<br><br>" + "<b>Digital Acceptance Details</b><br>"
					+ " Date & Time : " + detailsByCandidateId.getAccepted_date() + "<br>"
					+ " IP Address : " + detailsByCandidateId.getIp_address() + "<br><br>"
					+ "You can complete the payment by clicking on the 'Pay Now' button below or in your offer letter to secure your seat.<br>"
					+ "<br/>" + "<a href= " + PAYMENT_URL + candidate_id
					+ " style='background-color: #4A57A9;color:white;text-decoration:none;padding:6px'>Pay Now</a>"
					+ "<br><br>" + "If you have any questions or encounter any issues with the payment process, please contact your Admission Councellor. "
					+ "<br><br>" + "We look forward to welcoming you to "
					+ candidate_details.get(0).get("school_name").toString().toUpperCase()
					+ " and to an exciting academic journey ahead!" + "<br><br><br><br>" + "regards," + "<br><br>"
					+ "Team Admissions" + "<br>" + "Acharya Institutes" + "<br>" + "Bangalore - 560107." + "<br>"
					+ "Email - admissions@acharya.ac.in" + "<br><br><br><br><br><br>"
					+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
					+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";

			if (ObjectUtils.isEmpty(candidate_details) || candidate_details == null)
				throw new RuntimeException("Candidate details not found");
			else
				candidate_email = candidate_details.get(0).get("candidate_email").toString();

			response_handler.sendEmailWithAttachment(file, candidate_details.get(0).get("counselorEmail").toString(),
					candidate_email, mailBody, "Offer Acceptance details - Pay fee");
			candidateWalkinRepository.updateNpfStatusOfAcceptingOffer(candidate_id);
		}

		preadmission.updateMailSentStatus(candidate_id);
//		candidateWalkInService.updateLsqStatus(candidate_id, 3);
		System.out.println("Mail Send...");
		return null;

	}

	public ResponseEntity<Object> examFee(ExamFeeDTO examFeeDTO) {
		try {
			RazorpayClient razorpayClient = razorPayConfig.getRazorpayClient(examFeeDTO.getSchoolId());

			TemporaryRazorPayTransaction razorPayTransaction = new TemporaryRazorPayTransaction();

			Student_Details student = studentDetailsRepository.getStudentByStudentId(examFeeDTO.getStudentId());
			razorPayTransaction.setAcYearId(examFeeDTO.getAcYearId());
			razorPayTransaction.setAmount((float) examFeeDTO.getTotal());
			razorPayTransaction.setCurrentSem(examFeeDTO.getCurrentSem());
			razorPayTransaction.setCurrentYear(examFeeDTO.getCurrentYear());
			razorPayTransaction.setTransactionType("Exam Fee");
			String receiptId = "txn_" + System.currentTimeMillis();
			razorPayTransaction.setReceiptId(receiptId);
			int amountInPaise = (int) examFeeDTO.getTotal() * 100;

			JSONObject options = new JSONObject();
			options.put("amount", amountInPaise);
			options.put("currency", "INR");

			options.put("receipt", receiptId);
			JSONObject notes = new JSONObject();
			notes.put("amount", examFeeDTO.getTotal());
			notes.put("email",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
							? student.getAcharya_email()
							: "");
			notes.put("mobile",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getMobile()) ? student.getMobile()
							: "");
			notes.put("auid",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAuid()) ? student.getAuid()
							: "");
			options.put("notes", notes);

			Order order = razorpayClient.Orders.create(options);
			String jsonObject = order.toJson().toString();
			System.out.println(jsonObject);
			ObjectMapper objectMapper = new ObjectMapper();
			RazorPayOrder razorpayOrder = objectMapper.readValue(jsonObject, RazorPayOrder.class);

			String orderId = order.get("id");
			razorPayTransaction.setOrderId(orderId);
			razorPayTransaction.setStudentId(examFeeDTO.getStudentId());
			temporaryRazorPayTransactionRepository.save(razorPayTransaction);
			List<TemporaryRazorPayPaymentDetails> razorPayPaymentDetails = new ArrayList<>();

			for (ExamFeeDetails examFeeDetails : examFeeDTO.getExamFeeDetails()) {
				TemporaryRazorPayPaymentDetails razorpay = new TemporaryRazorPayPaymentDetails();
				razorpay.setReceiptType("Exam Fee");
				razorpay.setPaymentType(examFeeDetails.getFeeType());
				razorpay.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				razorpay.setAmount(examFeeDetails.getAmount());
				razorpay.setSem(examFeeDetails.getSem());
				razorpay.setPaidYear(examFeeDetails.getPaidYear());
				razorpay.setVoucherHeadId(examFeeDetails.getVoucherHeadNewId());
				razorpay.setAcYearId(razorPayTransaction.getAcYearId());
				razorPayPaymentDetails.add(razorpay);

			}

			temporaryRazorPayPaymentDetailsRepository.saveAll(razorPayPaymentDetails);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", razorpayOrder);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> uniformFee(UniformFeeDTO uniformFeeDTO) {
		try {
			RazorpayClient razorpayClient = razorPayConfig.getRazorpayClient(uniformFeeDTO.getSchoolId());
			Student_Details student = studentDetailsRepository.getStudentByStudentId(uniformFeeDTO.getStudentId());
			List<RouteAccountDetails> routeAccountDetails = routeAccountDetailsRepository
					.getAccountDetails(uniformFeeDTO.getSchoolId());
			RouteAccountDetails uniformAccount = routeAccountDetails.stream()
					.filter(r -> r.getRouteType().equals("UNIFORM")).findFirst().get();
			UniformTransaction razorPayTransaction = new UniformTransaction();

			razorPayTransaction.setAcYearId(uniformFeeDTO.getAcYearId());
			razorPayTransaction.setAmount((float) uniformFeeDTO.getTotal());
			razorPayTransaction.setCurrentSem(uniformFeeDTO.getCurrentSem());
			razorPayTransaction.setCurrentYear(uniformFeeDTO.getCurrentYear());

			String receiptId = "txn_" + System.currentTimeMillis();
			razorPayTransaction.setReceiptId(receiptId);
			int amountInPaise = (int) uniformFeeDTO.getTotal() * 100;

			JSONObject options = new JSONObject();
			options.put("amount", amountInPaise);
			options.put("currency", "INR");
			JSONObject notes = new JSONObject();

			notes.put("amount", uniformFeeDTO.getTotal());
			notes.put("mobile", uniformFeeDTO.getMobile());
			notes.put("email",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
							? student.getAcharya_email()
							: "");
			notes.put("auid",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAuid()) ? student.getAuid()
							: "");


			JSONArray transfers = new JSONArray();
			if (ObjectUtils.isNotEmpty(uniformAccount) && uniformFeeDTO.getTotal() > 0) {
				JSONObject uniformTransfer = new JSONObject();
				uniformTransfer.put("account", uniformAccount.getRouteAccountId());
				uniformTransfer.put("amount", uniformFeeDTO.getTotal() * 100);
				uniformTransfer.put("currency", "INR");
				JSONObject uniformNotes = new JSONObject();
				uniformNotes.put("UniformFee", uniformFeeDTO.getTotal());
				uniformNotes.put("email",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
								? student.getAcharya_email()
								: "");
				uniformNotes.put("mobile",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
								? student.getMobile()
								: "");
				uniformNotes.put("auid",
						ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAuid()) ? student.getAuid()
								: "");
				uniformTransfer.put("notes", uniformNotes);
				uniformTransfer.put("on_hold", 0);
				transfers.put(uniformTransfer);
			}

			options.put("notes", notes);

			options.put("receipt", receiptId);
			options.put("transfers", transfers);
			Order order = razorpayClient.Orders.create(options);
			String jsonObject = order.toJson().toString();
			System.out.println(jsonObject);
			ObjectMapper objectMapper = new ObjectMapper();
			RazorPayOrder razorpayOrder = objectMapper.readValue(jsonObject, RazorPayOrder.class);

			String orderId = order.get("id");
			razorPayTransaction.setOrderId(orderId);
			razorPayTransaction.setStudentId(uniformFeeDTO.getStudentId());
			String transfer = Optional.ofNullable(razorpayOrder.getTransfers())
					.orElse(Collections.emptyList())
					.stream()
					.map(Transfer::getId)
					.filter(Objects::nonNull)
					.collect(Collectors.joining(","));
			razorPayTransaction.setTransferId(transfer);
			Optional<String> firstId = razorpayOrder.getTransfers().stream()
					.map(Transfer::getId)
					.findFirst();

			firstId.ifPresent(id -> razorPayTransaction.setTransactionId(id));

			uniformTransactionRepository.save(razorPayTransaction);

			List<UniformFeeTransactionDetails> razorPayPaymentDetails = new ArrayList<>();


			for (UniformFeeDetails uniformFeeDetails : uniformFeeDTO.getUniformFeeDetails()) {
				UniformFeeTransactionDetails razorpay = new UniformFeeTransactionDetails();
				razorpay.setPaymentType("Uniform Fee Loose");
				razorpay.setUniformTransactionId(razorPayTransaction.getUniformTransactionId());
				razorpay.setAmount((float) uniformFeeDetails.getAmount());
				razorpay.setCgst_output(uniformFeeDetails.getCgst_output());
				razorpay.setCgst_input(uniformFeeDetails.getCgst_input());
				razorpay.setSgst_output(uniformFeeDetails.getSgst_output());
				razorpay.setSgst_input(uniformFeeDetails.getSgst_input());
				razorpay.setQuantity(uniformFeeDetails.getQuantity());
				razorpay.setEnv_item_id(uniformFeeDetails.getEnv_item_id());
				razorpay.setGst(uniformFeeDetails.getGst());
				razorpay.setItemName(uniformFeeDetails.getItemName());
				razorpay.setPaidYear(uniformFeeDetails.getPaidYear());
				razorpay.setStudentId(uniformFeeDetails.getStudentId());
				razorPayPaymentDetails.add(razorpay);

			}

			uniformFeeTransactionDetailsRepository.saveAll(razorPayPaymentDetails);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", razorpayOrder);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getTransactionDetails(Integer studentId) {
		try {
			List<Map<String, Object>> transactions = razorPayTransactionRepository.getTransactionDetails(studentId);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", transactions);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getTransactionDetailsData() {
		try {
			List<Map<String, Object>> transactions = razorPayTransactionRepository.getTransactionDetailsData();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", transactions);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> registrationFee(BulkPaymentDTO bulkPaymentDTO) {
		try {

			RazorpayClient razorpayClient = razorPayConfig.getRazorpayClient(bulkPaymentDTO.getSchoolId());
			Candidate_Walkin candidate = candidateWalkinRepository.getByCandidateId(bulkPaymentDTO.getStudentId());
			RegistrationFeeTransaction razorPayTransaction = new RegistrationFeeTransaction();

			razorPayTransaction.setAmount((float) bulkPaymentDTO.getAmount());

			String receiptId = "txn_" + System.currentTimeMillis();
			razorPayTransaction.setReceiptId(receiptId);
			float amountInPaise = bulkPaymentDTO.getAmount() * 100;

			JSONObject options = new JSONObject();
			options.put("amount", amountInPaise);
			options.put("currency", "INR");

			options.put("receipt", receiptId);

			JSONObject notes = new JSONObject();
			notes.put("candidateId",
					ObjectUtils.isNotEmpty(candidate) && ObjectUtils.isNotEmpty(candidate.getCandidate_id())
							? candidate.getCandidate_id()
							: "");
			notes.put("receipt",
					ObjectUtils.isNotEmpty(candidate) && ObjectUtils.isNotEmpty(candidate.getApplication_no_npf())
							? candidate.getApplication_no_npf()
							: "");
			options.put("notes", notes);

			Order order = razorpayClient.Orders.create(options);
			String jsonObject = order.toJson().toString();
			System.out.println(jsonObject);
			ObjectMapper objectMapper = new ObjectMapper();
			RazorPayOrder razorpayOrder = objectMapper.readValue(jsonObject, RazorPayOrder.class);

			String orderId = order.get("id");
			razorPayTransaction.setOrderId(orderId);
			razorPayTransaction.setCandidateId(bulkPaymentDTO.getStudentId());
			razorPayTransaction.setReceiptStatus(PENDING);
			razorPayTransaction.setSchoolId(bulkPaymentDTO.getSchoolId());
			registrationFeeTrsactionRepository.save(razorPayTransaction);
			candidateWalkInService.updateLsqStatus(bulkPaymentDTO.getStudentId(), 5);

			// temporary razorpay

			TemporaryRazorPayTransaction temporaryRazorPayTransaction = new TemporaryRazorPayTransaction();


			temporaryRazorPayTransaction.setAcYearId(bulkPaymentDTO.getAcYearId());
			temporaryRazorPayTransaction.setAmount(razorPayTransaction.getAmount());

			temporaryRazorPayTransaction.setTransactionType("Registration Fee");

			temporaryRazorPayTransaction.setReceiptId(razorPayTransaction.getReceiptId());


			temporaryRazorPayTransaction.setOrderId(razorPayTransaction.getOrderId());
			temporaryRazorPayTransaction.setPaymentId(razorPayTransaction.getPaymentId());
			temporaryRazorPayTransaction.setDescription(razorPayTransaction.getDescription());
			temporaryRazorPayTransaction.setReason(razorPayTransaction.getReason());
			temporaryRazorPayTransaction.setSource(razorPayTransaction.getSource());
			temporaryRazorPayTransaction.setSignature(razorPayTransaction.getSignature());
			temporaryRazorPayTransaction.setStatus(razorPayTransaction.getStatus());
			temporaryRazorPayTransaction.setStep(razorPayTransaction.getStep());
			temporaryRazorPayTransaction.setTransactionDate(razorPayTransaction.getTransactionDate());
			temporaryRazorPayTransaction.setCurrentSem(bulkPaymentDTO.getCurrentSem());
			temporaryRazorPayTransaction.setCurrentYear(bulkPaymentDTO.getCurrentYear());

			temporaryRazorPayTransactionRepository.save(temporaryRazorPayTransaction);


			TemporaryRazorPayPaymentDetails razorpay = new TemporaryRazorPayPaymentDetails();
			razorpay.setReceiptType("Registration Fee");
			razorpay.setPaymentType("Registration Fee");
			razorpay.setRazorPayTransactionId(temporaryRazorPayTransaction.getRazorPayTransactionId());
			razorpay.setAmount(Double.valueOf(temporaryRazorPayTransaction.getAmount()));
			razorpay.setSem(bulkPaymentDTO.getCurrentYear());
			razorpay.setYear(bulkPaymentDTO.getCurrentYear());
			//	razorpay.setPaidYear();
			razorpay.setVoucherHeadId(13);
			razorpay.setAcYearId(temporaryRazorPayTransaction.getAcYearId());


			temporaryRazorPayPaymentDetailsRepository.save(razorpay);

			// end
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", razorpayOrder);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public List<Map<String, Object>> studentNoDueStudentDetails(Integer school_id, Integer program_id,
																Integer program_specialization_id, Integer current_sem, Integer current_year) {
		return studentDetailsRepository.studentNoDueStudentDetails(school_id, program_id, program_specialization_id,
				current_sem, current_year);
	}

	public ResponseEntity<Object> getRegistrationFeeDetails(Integer candidateId) {
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = dateFormat.format(new Date());
			Date newDate = dateFormat.parse(formattedDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(newDate);
			int month = calendar.get(Calendar.MONTH) + 1;
			int year = calendar.get(Calendar.YEAR);

			DollarToInrConversion dollarToInrConversion = dollarToInrConversionRepository.findByDateAndActive(month,
					year);

			Map<String, Object> registrationFeeDetails = studentDetailsRepository
					.getRegistrationFeeDetails(candidateId);
			Map<String, Object> mutableRegistrationFeeDetails = new HashMap<>(registrationFeeDetails);

			String currencyType = (String) mutableRegistrationFeeDetails.get("currencyType");
			if (StringUtils.equals(currencyType, "USD")) {
				Object amountObj = mutableRegistrationFeeDetails.get("amount");
				if (amountObj != null) {
					double amountInUSD = Double.parseDouble(amountObj.toString());
					double conversionRate = dollarToInrConversion.getInr();
					double amountInINR = amountInUSD * conversionRate;

					mutableRegistrationFeeDetails.put("amount", (int) amountInINR);
				}
			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", mutableRegistrationFeeDetails);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> registrationFeePaymentStatus(PaymentRequestDTO paymentRequestDTO) {
		try {
			if (StringUtils.equals(paymentRequestDTO.getStatus(), "success")) {

				RegistrationFeeTransaction razorPayTransaction = registrationFeeTrsactionRepository
						.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				razorPayTransaction.setPaymentId(paymentRequestDTO.getRazorpayPaymentId());
				razorPayTransaction.setSignature(paymentRequestDTO.getRazorpaySignature());
				razorPayTransaction.setStatus(paymentRequestDTO.getStatus());
				razorPayTransaction.setTransactionDate(new Date());

				String currencyType = preAdmissionProcessRepository.getCurrencyTypeByCandidateId(razorPayTransaction.getCandidateId());

				DollarToInrConversion dollarToInrConversion = calculateDollarRate(currencyType);
				if (ObjectUtils.isNotEmpty(dollarToInrConversion)) {
					razorPayTransaction.setDollar_value(dollarToInrConversion.getInr());
				}
				registrationFeeTrsactionRepository.save(razorPayTransaction);

//				PreAdmissionProcess preAdmissionProces=preadmission.findByCandidateId(razorPayTransaction.getCandidateId());
//				FeeTemplateSubAmount feeTemplateSubAmount=feeTemplateSubAmountRepository.findByFeeTemplateId(preAdmissionProces.getFee_template_id());
//				Integer amount=0;
//				if(feeTemplateSubAmount.getYear1_amt()>0) {
//				 amount=(int) ((int)feeTemplateSubAmount.getYear1_amt()-razorPayTransaction.getAmount());		
//				}
//				feeTemplateSubAmount.setYear1_amt(amount);
//				feeTemplateSubAmountRepository.save(feeTemplateSubAmount);


				//RazorPAy
				TemporaryRazorPayTransaction temporaryRazorPayTransaction = temporaryRazorPayTransactionRepository.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				List<TemporaryRazorPayPaymentDetails> temporaryRazorPayPaymentDetails = temporaryRazorPayPaymentDetailsRepository.getByRazorPayTransactionId(temporaryRazorPayTransaction.getRazorPayTransactionId());
				temporaryRazorPayTransaction.setStatus("Success");
				temporaryRazorPayTransaction.setTransactionType("Registration Fee");
				temporaryRazorPayTransaction.setPaymentId(razorPayTransaction.getPaymentId());
				temporaryRazorPayTransaction.setSignature(razorPayTransaction.getSignature());
				temporaryRazorPayTransactionRepository.save(temporaryRazorPayTransaction);
				RazorPayTransaction razorPay = razorPayTransactionRepository.findByOrderId(temporaryRazorPayTransaction.getOrderId());
				if (ObjectUtils.isEmpty(razorPay)) {
					razorPay.setAcYearId(temporaryRazorPayTransaction.getAcYearId());
					razorPay.setAmount(razorPayTransaction.getAmount());
					razorPay.setReceiptId(razorPayTransaction.getReceiptId());
					razorPay.setOrderId(razorPayTransaction.getOrderId());
					razorPay.setDescription(razorPayTransaction.getDescription());
					razorPay.setReason(razorPayTransaction.getReason());
					razorPay.setSource(razorPayTransaction.getSource());
					razorPay.setStep(razorPayTransaction.getStep());
					razorPay.setTransactionDate(razorPayTransaction.getTransactionDate());
					razorPay.setCurrentSem(temporaryRazorPayTransaction.getCurrentSem());
					razorPay.setCurrentYear(temporaryRazorPayTransaction.getCurrentYear());
					razorPay.setStatus("Success");
					razorPay.setTransactionType("Registration Fee");
					razorPay.setPaymentId(razorPayTransaction.getPaymentId());
					razorPay.setSignature(razorPayTransaction.getSignature());

					//temporaryRazorPayTransactionRepository.save(temporaryRazorPayTransaction);
					RazorPayTransaction updatedRazorPay = razorPayTransactionRepository.save(razorPay);

					List<RazorPayPaymentDetails> razorPayList = new ArrayList<>();
					for (TemporaryRazorPayPaymentDetails tempRazorPay : temporaryRazorPayPaymentDetails) {
						RazorPayPaymentDetails razorpay = new RazorPayPaymentDetails();
						razorpay.setReceiptType("Registration Fee");
						razorpay.setPaymentType("Registration Fee");
						razorpay.setRazorPayTransactionId(updatedRazorPay.getRazorPayTransactionId());
						razorpay.setAmount(tempRazorPay.getAmount());
						razorpay.setSem(tempRazorPay.getSem());
						razorpay.setYear(tempRazorPay.getYear());
						//	razorpay.setPaidYear();
						razorpay.setVoucherHeadId(13);
						razorpay.setAcYearId(temporaryRazorPayTransaction.getAcYearId());
						razorPayList.add(razorpay);

					}
					razorPayPaymentDetailsRepository.saveAll(razorPayList);
				}
				//end
				Candidate_Walkin candidate_Walkin = candidateWalkinRepository
						.getByCandidateId(razorPayTransaction.getCandidateId());
				candidate_Walkin.setNpf_status(4);
				candidateWalkinRepository.save(candidate_Walkin);
				candidateWalkInService.updateLsqStatus(candidate_Walkin.getCandidate_id(), 5);

				callLeadSquareApiforUpdatePaymentSuccess(candidate_Walkin, razorPayTransaction);

			} else {
				RegistrationFeeTransaction razorPayTransaction = registrationFeeTrsactionRepository
						.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				razorPayTransaction.setCode(paymentRequestDTO.getCode());
				razorPayTransaction.setDescription(paymentRequestDTO.getDescription());
				razorPayTransaction.setSource(paymentRequestDTO.getSource());
				razorPayTransaction.setStep(paymentRequestDTO.getStep());
				razorPayTransaction.setReason(paymentRequestDTO.getReason());
				razorPayTransaction.setStatus(paymentRequestDTO.getStatus());
				registrationFeeTrsactionRepository.save(razorPayTransaction);
				//Razorpay
				TemporaryRazorPayTransaction temporaryRazorPayTransaction = temporaryRazorPayTransactionRepository.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				temporaryRazorPayTransaction.setCode(paymentRequestDTO.getCode());
				temporaryRazorPayTransaction.setDescription(paymentRequestDTO.getDescription());
				temporaryRazorPayTransaction.setSource(paymentRequestDTO.getSource());
				temporaryRazorPayTransaction.setStep(paymentRequestDTO.getStep());
				temporaryRazorPayTransaction.setReason(paymentRequestDTO.getReason());
				temporaryRazorPayTransaction.setStatus(paymentRequestDTO.getStatus());
				temporaryRazorPayTransactionRepository.save(temporaryRazorPayTransaction);
				//end

				return ResponseHandler.generateResponse(false, HttpStatus.OK, "FAILURE", "Payment Failed");

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}

	}

	public void callLeadSquareApiforUpdatePaymentSuccess(Candidate_Walkin candidateWalkin,
														 RegistrationFeeTransaction razorPayTransaction) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String apiUrl = "https://api-in21.leadsquared.com/v2/ProspectActivity.svc/Create?accessKey=u$rf0116cbfffa617de778de29050a8565a&secretKey=871be21cf012083516bbf13555f20cbaa3b2091a";

		RestTemplate restTemplate = new RestTemplate();
		JSONObject payload = new JSONObject();
		payload.put("RelatedProspectId", candidateWalkin.getLead_id());
		payload.put("RelatedOpportunityId", candidateWalkin.getOpportunity_id());
		payload.put("ActivityEvent", razorPayTransaction.getRegistrationFeeTransactionId());
		payload.put("ActivityNote", "Registration fee");
		payload.put("ActivityDateTime", sdf.format(razorPayTransaction.getTransactionDate()));

		JSONArray fields = new JSONArray();
		fields.put(new JSONObject().put("SchemaName", "mx_Custom_1").put("Value", razorPayTransaction.getAmount()));
		fields.put(new JSONObject().put("SchemaName", "mx_Custom_2").put("Value", razorPayTransaction.getPaymentId()));
		fields.put(new JSONObject().put("SchemaName", "mx_Custom_3").put("Value", razorPayTransaction.getReceiptId()));
		payload.put("Fields", fields);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		// Create HTTP request
		HttpEntity<String> request = new HttpEntity<>(payload.toString(), headers);

		try {

			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

		} catch (Exception e) {
			System.out.println("Exception in Lead square api" + e.getMessage());
		}

	}

	public ResponseEntity<Object> getCandidateTransactionDetails(Integer candidateId) {
		try {
			List<CandidateTransactionDetailsDTO> candidateTransactionDetailsDTOs = registrationFeeTrsactionRepository
					.getCandidateTransactionDetails(candidateId);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", candidateTransactionDetailsDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}

	}

	public List<HashMap<String, Object>> studentDetailsForChangeOFCourse(Integer oldStudentId) {
		return studentDetailsRepository.studentDetailsForChangeOFCourse(oldStudentId);
	}

//	public void studentDetailsImageCheck(String auid) {
//		List<Map<String,Object>> studentDetails=studentDetailsRepository.getImageAndStudentId(auid);
//		studentDetails.stream().forEach(st -> {
//			String imagePath = (String) st.get("student_image_path");
//			if (! (checkFileInS3(imagePath))) {
//				try {
//				studentDetailsRepository.updateStudentImagePath((Integer) st.get("student_id"));
//				} catch (Exception e) {
//					System.out.println(e.getMessage());
//				}
//			} 
//		});
//
//	}

//	private  boolean checkFileInS3(String fileKey) {
//        try {
//            S3Object s3Object = s3client.getObject(bucketName, value + "/" + fileKey);
//            return s3Object != null;
//        } catch (Exception e) {
//            return false;
//        }
//    }

	@Transactional
	public void studentDetailsImageCheck(String auid) {
		List<Map<String, Object>> studentDetails = studentDetailsRepository.getImageAndStudentId(auid);
		System.out.println("__________________ " + studentDetails);

		studentDetails.forEach(st -> {
			String imagePath = (String) st.get("student_image_path");
			Integer studentId = (Integer) st.get("student_id");

			try {
				if (!checkFileInS3(imagePath)) {
					studentDetailsRepository.updateStudentImagePath(studentId);
				}
			} catch (Exception e) {
				System.out.println("Failed to update student ID " + studentId + ": " + e);
				e.printStackTrace();
				// You might want to rethrow the exception or handle it differently based on
				// your logic
				throw new RuntimeException("Transaction failed for student ID " + studentId, e);
			}
		});
	}

	private boolean checkFileInS3(String fileKey) {
		int retries = 3;
		int waitTime = 1000; // Initial wait time in ms

		for (int attempt = 0; attempt < retries; attempt++) {
			try {
				S3Object s3Object = s3client.getObject(bucketName, value + "/" + fileKey);
				return s3Object != null;
			} catch (AmazonS3Exception e) {
				if (e.getStatusCode() == 404) {
					System.out.println("File not found in S3: " + fileKey);
					return false;
				}
				System.out.println("S3 error: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("Error checking file in S3: " + e.getMessage());
			}

			// Wait before retrying
			try {
				Thread.sleep(waitTime);
				waitTime *= 2; // Exponential backoff
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
		}
		return false; // Return false if all retries fail
	}

	public ResponseEntity<Object> getStudentAddOnAndUniformStationaryAmount(Integer studentId) {
		try {
			Map<String, Object> responseData = new HashMap<String, Object>();

			Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(studentId);
			String programType = programAssigmentRepository
					.getProgramTypeByProgramAssignmentId(studentDetails.getProgram_assignment_id());

			Map<String, Object> semesterAddOnDue = studentDueRepository.getSemesterAddOnDue(studentId);
			Map<String, Object> razorPayData = new HashMap<String, Object>();

			for (int i = 1; i <= 12; i++) {

				if (programType.equalsIgnoreCase("Yearly")) {
					Double amount = razorPayTransactionRepository.getDataYearly(studentId,
							studentDetails.getAc_year_id(), i);
					razorPayData.put("year" + i, amount);
				} else {
					Double amount = razorPayTransactionRepository.getDataSemester(studentId,
							studentDetails.getAc_year_id(), i);
					System.out.println("AMOUNT : sem " + i + " : " + amount);
					razorPayData.put("sem" + i, amount);
				}

			}

			responseData.put("AddOnData", semesterAddOnDue);
			responseData.put("PaidDetails", razorPayData);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", responseData);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public Student_Details updateStudentDetails(Integer student_id, Map<String, Object> updates,
												JwtDetails jwtDetails) {
		Student_Details student = studentDetailsRepository.findById(student_id)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		student.setModified_by(jwtDetails.getUserId());
		student.setModified_username(jwtDetails.getUserName());
		updates.forEach((key, value) -> {
			try {
				Field field = Student_Details.class.getDeclaredField(key);
				field.setAccessible(true);
				field.set(student, value);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				throw new IllegalArgumentException("Invalid field: " + key);
			}
		});

		return studentDetailsRepository.save(student);
	}

	public ResponseEntity<Object> uniformFeePaymentStatus(PaymentRequestDTO paymentRequestDTO) {
		try {
			if (StringUtils.equals(paymentRequestDTO.getStatus(), "success")) {

				UniformTransaction razorPayTransaction = uniformTransactionRepository
						.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				razorPayTransaction.setPaymentId(paymentRequestDTO.getRazorpayPaymentId());
				razorPayTransaction.setSignature(paymentRequestDTO.getRazorpaySignature());
				razorPayTransaction.setStatus(paymentRequestDTO.getStatus());
				razorPayTransaction.setTransactionDate(new Date());
				uniformTransactionRepository.save(razorPayTransaction);

				List<UniformFeeTransactionDetails> uniformFeeTransactionDetails = uniformFeeTransactionDetailsRepository
						.getAllByUniformTransactionId(razorPayTransaction.getUniformTransactionId());


				generateUniFormReceipt(paymentRequestDTO.getRazorpayOrderId());


			} else {
				UniformTransaction razorPayTransaction = uniformTransactionRepository
						.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				razorPayTransaction.setCode(paymentRequestDTO.getCode());
				razorPayTransaction.setDescription(paymentRequestDTO.getDescription());
				razorPayTransaction.setSource(paymentRequestDTO.getSource());
				razorPayTransaction.setStep(paymentRequestDTO.getStep());
				razorPayTransaction.setReason(paymentRequestDTO.getReason());
				razorPayTransaction.setStatus(paymentRequestDTO.getStatus());
				uniformTransactionRepository.save(razorPayTransaction);

				return ResponseHandler.generateResponse(false, HttpStatus.OK, "FAILURE", "Payment Failed");

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}

	}

	public ResponseEntity<Object> getUniformTransactionDetails(Integer studentId) {
		try {
			List<CandidateTransactionDetailsDTO> candidateTransactionDetailsDTOs = uniformFeeTransactionDetailsRepository
					.getUniformTransactionDetails(studentId);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", candidateTransactionDetailsDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> bulkPaymentStatus(PaymentRequestDTO paymentRequestDTO) {
		try {
			if (StringUtils.equals(paymentRequestDTO.getStatus(), "success")) {

				BulkTransaction razorPayTransaction = bulkTransactionRepository
						.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				razorPayTransaction.setPaymentId(paymentRequestDTO.getRazorpayPaymentId());
				razorPayTransaction.setSignature(paymentRequestDTO.getRazorpaySignature());
				razorPayTransaction.setStatus(paymentRequestDTO.getStatus());
				razorPayTransaction.setTransactionDate(new Date());
				bulkTransactionRepository.save(razorPayTransaction);
				//RazorPAy
				TemporaryRazorPayTransaction temporaryRazorPayTransaction = temporaryRazorPayTransactionRepository.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				List<TemporaryRazorPayPaymentDetails> temporaryRazorPayPaymentDetails = temporaryRazorPayPaymentDetailsRepository.getByRazorPayTransactionId(temporaryRazorPayTransaction.getRazorPayTransactionId());

				temporaryRazorPayTransaction.setStatus("Success");
				temporaryRazorPayTransaction.setTransactionType("Bulk");
				temporaryRazorPayTransaction.setPaymentId(paymentRequestDTO.getRazorpayPaymentId());
				temporaryRazorPayTransaction.setTransactionDate(razorPayTransaction.getTransactionDate());
				temporaryRazorPayTransaction.setSignature(paymentRequestDTO.getRazorpaySignature());
				temporaryRazorPayTransactionRepository.save(temporaryRazorPayTransaction);

				RazorPayTransaction razorPay = razorPayTransactionRepository.findByOrderId(temporaryRazorPayTransaction.getOrderId());
				if (ObjectUtils.isEmpty(razorPay)) {
					razorPay.setAcYearId(temporaryRazorPayTransaction.getAcYearId());
					razorPay.setAmount(razorPayTransaction.getAmount());

					razorPay.setTransactionType(temporaryRazorPayTransaction.getTransactionType());

					razorPay.setReceiptId(temporaryRazorPayTransaction.getReceiptId());


					razorPay.setOrderId(temporaryRazorPayTransaction.getOrderId());
					razorPay.setPaymentId(temporaryRazorPayTransaction.getPaymentId());
					razorPay.setDescription(temporaryRazorPayTransaction.getDescription());
					razorPay.setReason(temporaryRazorPayTransaction.getReason());
					razorPay.setSource(temporaryRazorPayTransaction.getSource());
					razorPay.setSignature(temporaryRazorPayTransaction.getSignature());
					razorPay.setStatus(temporaryRazorPayTransaction.getStatus());
					razorPay.setStep(temporaryRazorPayTransaction.getStep());
					razorPay.setTransactionDate(temporaryRazorPayTransaction.getTransactionDate());
					razorPay.setCurrentSem(temporaryRazorPayTransaction.getCurrentSem());
					razorPay.setCurrentYear(temporaryRazorPayTransaction.getCurrentYear());
					razorPay.setTransactionId(String.valueOf(temporaryRazorPayTransaction.getTransactionId()));


					RazorPayTransaction updatedRazorPay = razorPayTransactionRepository.save(razorPay);

					List<RazorPayPaymentDetails> razorPayList = new ArrayList<>();
					for (TemporaryRazorPayPaymentDetails tempRazorPay : temporaryRazorPayPaymentDetails) {

						RazorPayPaymentDetails payPaymentDetails = new RazorPayPaymentDetails();
						payPaymentDetails.setReceiptType(tempRazorPay.getReceiptType());
						payPaymentDetails.setPaymentType(tempRazorPay.getPaymentType());
						payPaymentDetails.setRazorPayTransactionId(updatedRazorPay.getRazorPayTransactionId());
						payPaymentDetails.setAmount(tempRazorPay.getAmount());
						payPaymentDetails.setSem(tempRazorPay.getSem());
						payPaymentDetails.setYear(tempRazorPay.getYear());
						payPaymentDetails.setPaidYear(tempRazorPay.getPaidYear());
						payPaymentDetails.setVoucherHeadId(tempRazorPay.getVoucherHeadId());
						payPaymentDetails.setAcYearId(tempRazorPay.getAcYearId());
						razorPayList.add(payPaymentDetails);

					}
					razorPayPaymentDetailsRepository.saveAll(razorPayList);

				}

				if (razorPayTransaction.getTransferType().equalsIgnoreCase("ADDON")) {
					generateCmaReceiptForBulk(paymentRequestDTO.getRazorpayOrderId());
				}


			} else {
				BulkTransaction razorPayTransaction = bulkTransactionRepository
						.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				razorPayTransaction.setCode(paymentRequestDTO.getCode());
				razorPayTransaction.setDescription(paymentRequestDTO.getDescription());
				razorPayTransaction.setSource(paymentRequestDTO.getSource());
				razorPayTransaction.setStep(paymentRequestDTO.getStep());
				razorPayTransaction.setReason(paymentRequestDTO.getReason());
				razorPayTransaction.setStatus(paymentRequestDTO.getStatus());
				bulkTransactionRepository.save(razorPayTransaction);
				//Razorpay
				TemporaryRazorPayTransaction temporaryRazorPayTransaction = temporaryRazorPayTransactionRepository.findByOrderId(paymentRequestDTO.getRazorpayOrderId());
				temporaryRazorPayTransaction.setCode(paymentRequestDTO.getCode());
				temporaryRazorPayTransaction.setDescription(paymentRequestDTO.getDescription());
				temporaryRazorPayTransaction.setSource(paymentRequestDTO.getSource());
				temporaryRazorPayTransaction.setStep(paymentRequestDTO.getStep());
				temporaryRazorPayTransaction.setReason(paymentRequestDTO.getReason());
				temporaryRazorPayTransaction.setStatus(paymentRequestDTO.getStatus());
				temporaryRazorPayTransactionRepository.save(temporaryRazorPayTransaction);
				//end

				return ResponseHandler.generateResponse(false, HttpStatus.OK, "FAILURE", "Payment Failed");

			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}

	}


	public ResponseEntity<Object> getBulkTransactionDetails() {
		try {
			List<CandidateTransactionDetailsDTO> candidateTransactionDetailsDTOs = bulkTransactionRepository
					.getBulkTransactionDetails();

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", candidateTransactionDetailsDTOs);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> sortedStudentDetailsByUser(Pageable pageable1, Integer acYearId, Integer userId) {
		Page<Object> response1 = studentDetailsRepository.sortedStudentDetailsByUser(pageable1, acYearId, userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> searchedAndSortedStudentDetailsByUser(Pageable pageable, Object keyword,
																		Integer acYearId, Integer userId) {
		Page<Object> response = studentDetailsRepository.searchedAndSortedStudentDetailsByUser(pageable, keyword,
				acYearId, userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> searchedAndSortedStudentDetailsBySchoolId(Pageable pageable, Object keyword,
																			Integer acYearId, Integer school_id) {
		Page<Object> response = studentDetailsRepository.searchedAndSortedStudentDetailsBySchoolId(pageable, keyword,
				acYearId, school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> sortedStudentDetailsBySchoolId(Pageable pageable1, Integer acYearId,
																 Integer school_id) {
		Page<Object> response1 = studentDetailsRepository.sortedStudentDetailsBySchoolId(pageable1, acYearId,
				school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> searchedAndSortedStudentDetailsByAdmissionCategory(Pageable pageable, Object keyword,
																					 Integer acYearId, Integer fee_admission_category_id) {
		Page<Object> response = studentDetailsRepository.searchedAndSortedStudentDetailsByAdmissionCategory(pageable,
				keyword, acYearId, fee_admission_category_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> sortedStudentDetailsByAdmissionCategory(Pageable pageable1, Integer acYearId,
																		  Integer fee_admission_category_id) {
		Page<Object> response1 = studentDetailsRepository.sortedStudentDetailsByAdmissionCategory(pageable1, acYearId,
				fee_admission_category_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> studentDetailsByDept(Pageable pageable, Object keyword, Integer acYearId,
													   Integer dept_id, Integer school_id) {
		Page<Object> response = studentDetailsRepository.studentDetailsByDept(pageable, keyword, acYearId, dept_id, school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> studentDetailsByDept(Pageable pageable1, Integer acYearId, Integer dept_id, Integer school_id) {
		Page<Object> response1 = studentDetailsRepository.studentDetailsByDept(pageable1, acYearId, dept_id, school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> InactiveStudentDetailsByDept1(Pageable pageable, Object keyword, Integer ac_year_id,
																Integer dept_id, Integer school_id) {
		Page<Object> response = studentDetailsRepository.InactiveStudentDetailsByDept1(pageable, keyword, ac_year_id,
				dept_id, school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> InactiveStudentDetailsByDept2(Pageable pageable1, Integer ac_year_id,
																Integer dept_id, Integer school_id) {
		Page<Object> response1 = studentDetailsRepository.InactiveStudentDetailsByDept2(pageable1, ac_year_id, dept_id, school_id);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> getinActiveStudentDetailsIndex1(Pageable pageable, Object keyword, Integer ac_year_id,
																  Integer school_id, Integer program_id, Integer program_specialization_id, Integer fee_admission_category_id,
																  Integer userId) {
		Page<Object> response = studentDetailsRepository.getinActiveStudentDetailsIndex1(pageable, keyword, ac_year_id,
				school_id, program_id, program_specialization_id, fee_admission_category_id, userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
	}

	public ResponseEntity<Object> getinActiveStudentDetailsIndex2(Pageable pageable1, Integer ac_year_id,
																  Integer school_id, Integer program_id, Integer program_specialization_id, Integer fee_admission_category_id,
																  Integer userId) {
		Page<Object> response1 = studentDetailsRepository.getinActiveStudentDetailsIndex2(pageable1, ac_year_id,
				school_id, program_id, program_specialization_id, fee_admission_category_id, userId);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> getTransactionDetailsForPhp(Integer month, Integer year) {
		try {
			Map<String, Object> data = new HashMap<>();
			List<Map<String, Object>> transactionDetails = razorPayTransactionRepository
					.getTransactionDetailsForPhp(month, year);
			List<Map<String, Object>> registrationTransactionDetails = razorPayTransactionRepository
					.getRegistrationTransactionDetailsForPhp(month, year);
			data.put("transactionsDetails", transactionDetails);
			data.put("registrationTransactionsDetails", registrationTransactionDetails);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", data);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getStudentDetailsByAuid(String auid) {
		try {
			Student_Details byAuid = studentDetailsRepository.findByAuid(auid);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", byAuid);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> saveFineConcession(FineConcessionDTO fineConcessionDTO) {
		try {
			FineConcession fineConcession = fineConcessionRepository
					.getFineConcessionByAuid(fineConcessionDTO.getAuid());

			if (ObjectUtils.isEmpty(fineConcession)) {
				fineConcession = new FineConcession();
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.FOUND, "SUCCESS",
						"Data Already exists for " + fineConcessionDTO.getAuid());

			}
			fineConcession.setAuid(fineConcessionDTO.getAuid());
			fineConcession.setTillDate(fineConcessionDTO.getTillDate());
			fineConcession.setCurrentSem(fineConcessionDTO.getCurrentSem());
			fineConcession.setCurrentYear(fineConcessionDTO.getCurrentYear());
			fineConcession.setFile(fineConcessionDTO.getFile());
			fineConcession.setRemarks(fineConcessionDTO.getRemarks());
			fineConcession.setTotalDue(fineConcessionDTO.getTotalDue());
			fineConcession.setConcessionAmount(fineConcessionDTO.getConcessionAmount());

			fineConcessionRepository.save(fineConcession);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public ResponseEntity<Object> getTotalLateFee(String auid) {
		try {

			Student_Details studentDetails = studentDetailsRepository.getStudentByAuid(auid);
			StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, studentDetails.getStudent_id(), null);
			applicationEventPublisher.publishEvent(studentDueEvent);

			StudentDues studentDues = studentDueRepository
					.getStudentDueDetailsByStudentId(studentDetails.getStudent_id());
			ReportingStudents reportingStudents = reportingStudentsRepository
					.getDetailsOfReportingStudentsByStudentId(studentDetails.getStudent_id());
			ProgramAssigment programAssigment = programAssigmentRepository
					.getOne(studentDetails.getProgram_assignment_id());

			Map<String, Object> lateFee = getTotalLateFee(studentDues, studentDetails, programAssigment,
					reportingStudents);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", lateFee);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	private Map<String, Object> getTotalLateFee(StudentDues studentDues, Student_Details studentDetails,
												ProgramAssigment programAssigment, ReportingStudents reportingStudents) {

		List<FineSlabDTO> fineSlabDTOs = fineSlabRepository.getAllFines();

		Map<String, Object> latefee = new LinkedHashMap<String, Object>();
		Map<String, Object> totalLateFee = new LinkedHashMap<String, Object>();

		for (int sem = 1; sem <= programAssigment.getNumber_of_semester(); sem++) {
			ClassCommencementDetails classCommencementDetails = classCommencementDetailsRepository
					.getClassCommencementDetailsForStudentLateFee(studentDetails.getSchool_id(),
							studentDetails.getProgram_assignment_id(), studentDetails.getProgram_specialization_id(),
							sem);

			double penaltyPercentage = getPenalty(classCommencementDetails, fineSlabDTOs);
			latefee.put("sem" + sem,
					getLateDue(studentDues, sem) != null && getLateDue(studentDues, sem) > 0
							&& (reportingStudents.getCurrent_sem() >= sem || reportingStudents.getCurrent_year() >= sem)
							? getFineValue(penaltyPercentage, studentDues, sem)
							: 0);

		}

		Float total = 0.0f;

		for (Map.Entry<String, Object> m : latefee.entrySet()) {
			Float value = (Float) m.getValue();
			total = total + value;

		}
		totalLateFee.put("totalLateDue", total);
		return latefee;

	}

	private Float getFineValue(double penaltyPercentage, StudentDues studentDues, int sem) {
		Float value = (float) ((penaltyPercentage / 100) * getLateDue(studentDues, sem));
		return (float) Math.round(value);

	}

	private Float getLateDue(StudentDues studentDues, int sem) {
		switch (sem) {
			case 1:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS1due())
						? (Float) studentDues.getS1due()
						: 0.0f;
			case 2:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS2due())
						? (Float) studentDues.getS2due()
						: 0.0f;
			case 3:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS3due())
						? (Float) studentDues.getS3due()
						: 0.0f;
			case 4:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS4due())
						? (Float) studentDues.getS4due()
						: 0.0f;
			case 5:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS5due())
						? (Float) studentDues.getS5due()
						: 0.0f;
			case 6:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS6due())
						? (Float) studentDues.getS6due()
						: 0.0f;
			case 7:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS7due())
						? (Float) studentDues.getS7due()
						: 0.0f;
			case 8:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS8due())
						? (Float) studentDues.getS8due()
						: 0.0f;
			case 9:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS9due())
						? (Float) studentDues.getS9due()
						: 0.0f;
			case 10:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS10due())
						? (Float) studentDues.getS10due()
						: 0.0f;
			case 11:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS11due())
						? (Float) studentDues.getS11due()
						: 0.0f;
			case 12:
				return ObjectUtils.isNotEmpty(studentDues) && ObjectUtils.isNotEmpty(studentDues.getS12due())
						? (Float) studentDues.getS12due()
						: 0.0f;

			default:
				return null;
		}

	}

	public ResponseEntity<Object> getFineConcession() {

		try {
			List<Map<String, Object>> allConcessions = fineConcessionRepository.getAllConcessions();
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", allConcessions);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> getFineConcessionByAuid(String auid) {
		try {
			FineConcession fineConcessions = fineConcessionRepository.getFineConcessionByAuid(auid);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", fineConcessions);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> updateFineConcessionByAuid(FineConcessionDTO fineConcessionDTO) {
		try {

			FineConcession fineConcession = fineConcessionRepository
					.getFineConcessionByAuid(fineConcessionDTO.getAuid());
			fineConcession.setAuid(fineConcessionDTO.getAuid());
			fineConcession.setTillDate(fineConcessionDTO.getTillDate());
			fineConcession.setCurrentSem(fineConcessionDTO.getCurrentSem());
			fineConcession.setCurrentYear(fineConcessionDTO.getCurrentYear());
			fineConcession.setFile(fineConcessionDTO.getFile());
			fineConcession.setRemarks(fineConcessionDTO.getRemarks());
			fineConcession.setTotalDue(fineConcessionDTO.getTotalDue());
			fineConcession.setConcessionAmount(fineConcessionDTO.getConcessionAmount());

			fineConcessionRepository.save(fineConcession);

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> paymentCaptureFromRazorpay(String payload) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(payload);

			JsonNode paymentEntity = rootNode.path("payload").path("payment").path("entity");

			String paymentId = paymentEntity.path("id").asText();
			String orderId = paymentEntity.path("order_id").asText();
			String transactionId = paymentEntity.path("acquirer_data").path("upi_transaction_id").asText();
			String status = paymentEntity.path("status").asText();
			Integer studentId = null;
			if (status.equalsIgnoreCase("captured")) {

				TemporaryRazorPayTransaction temporaryrazorPayTransaction = temporaryRazorPayTransactionRepository
						.findByOrderId(orderId);


				RazorPayTransaction razorPayTransaction = razorPayTransactionRepository.findByOrderId(orderId);
				if (ObjectUtils.isNotEmpty(temporaryrazorPayTransaction) && ObjectUtils.isEmpty(razorPayTransaction) && !"success".equalsIgnoreCase(temporaryrazorPayTransaction.getStatus())) {

					temporaryrazorPayTransaction.setPaymentId(paymentId);
					temporaryrazorPayTransaction.setStatus("success");
					temporaryRazorPayTransactionRepository.save(temporaryrazorPayTransaction);

					razorPayTransaction = new RazorPayTransaction();
					razorPayTransaction.setPaymentId(paymentId);
					razorPayTransaction.setStatus("success");
					razorPayTransaction.setTransactionDate(temporaryrazorPayTransaction.getCreated_date());
					razorPayTransaction.setTransactionId(transactionId);
					razorPayTransaction.setOrderId(orderId);
					razorPayTransaction.setAcYearId(temporaryrazorPayTransaction.getAcYearId());
					razorPayTransaction.setAmount(temporaryrazorPayTransaction.getAmount());
					razorPayTransaction.setCurrentSem(temporaryrazorPayTransaction.getCurrentSem());
					razorPayTransaction.setCurrentYear(temporaryrazorPayTransaction.getCurrentYear());
					razorPayTransaction.setTransactionType(temporaryrazorPayTransaction.getTransactionType());
					razorPayTransaction.setPaidYear(Year.now().toString());
					razorPayTransaction.setOrderId(temporaryrazorPayTransaction.getOrderId());
					razorPayTransaction.setStudentId(temporaryrazorPayTransaction.getStudentId());
					razorPayTransaction.setReceiptId(temporaryrazorPayTransaction.getReceiptId());
					if (ObjectUtils.isNotEmpty(razorPayTransaction.getStudentId())) {
						String currencyType = studentDetailsRepository.getCurrenyTypeByStudentId(razorPayTransaction.getStudentId());
						DollarToInrConversion dollarToInrConversion = calculateDollarRate(currencyType);
						if (ObjectUtils.isNotEmpty(dollarToInrConversion)) {
							razorPayTransaction.setDollarValue(dollarToInrConversion.getInr());
						}
					}
					razorPayTransactionRepository.save(razorPayTransaction);
					List<TemporaryRazorPayPaymentDetails> temporaryRazorPayPaymentDetail = temporaryRazorPayPaymentDetailsRepository
							.getByRazorPayTransactionId(temporaryrazorPayTransaction.getRazorPayTransactionId());
					List<RazorPayPaymentDetails> razorPayPaymentDetailsList = new ArrayList<>();

					for (TemporaryRazorPayPaymentDetails tempPay : temporaryRazorPayPaymentDetail) {
						RazorPayPaymentDetails razorPayPaymentDetails = new RazorPayPaymentDetails();
						razorPayPaymentDetails.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
						razorPayPaymentDetails.setAmount(tempPay.getAmount());
						razorPayPaymentDetails.setPaymentType(tempPay.getPaymentType());
						razorPayPaymentDetails.setReceiptType(tempPay.getReceiptType());
						razorPayPaymentDetails.setSem(tempPay.getSem());
						razorPayPaymentDetails.setYear(tempPay.getYear());
						razorPayPaymentDetails.setVoucherHeadId(tempPay.getVoucherHeadId());
						razorPayPaymentDetails.setAcYearId(tempPay.getAcYearId());
						razorPayPaymentDetails.setPaidYear(tempPay.getPaidYear());
						razorPayPaymentDetailsList.add(razorPayPaymentDetails);

					}

					razorPayPaymentDetailsRepository.saveAll(razorPayPaymentDetailsList);
					List<CmaFeeReceipt> cmaFeeReceipts = cmaFeeReceiptRepository.getCmaFeeReceiptByOrderId(orderId);
					Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
					if (ObjectUtils.isEmpty(cmaFeeReceipts)) {
						List<RazorPayPaymentDetails> razorPayPaymentDetailsAddOn = razorPayPaymentDetailsRepository
								.getByRazorPayTransactionIdAndReceiptType(razorPayTransaction.getRazorPayTransactionId(),
										"Add On Fee");

						Integer cmaFeeReceiptNumber = cmaFeeReceiptRepository.getLatestReceitpNumber(financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id()) + 1;
						for (RazorPayPaymentDetails addOn : razorPayPaymentDetailsAddOn) {
							CmaFeeReceipt cmaFeeReceipt = new CmaFeeReceipt();
							cmaFeeReceipt.setActive(Boolean.TRUE);
							cmaFeeReceipt.setAmount(addOn.getAmount());
							cmaFeeReceipt.setFinancial_year_id(
									financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
							cmaFeeReceipt.setStudent_id(razorPayTransaction.getStudentId());
							cmaFeeReceipt.setPaid_year(String.valueOf(addOn.getPaidYear()));
							cmaFeeReceipt.setReceipt_type(addOn.getReceiptType());
							cmaFeeReceipt.setSchool_id(schoolId);
							cmaFeeReceipt
									.setCma_receipt_id(ObjectUtils.isNotEmpty(cmaFeeReceiptNumber) ? cmaFeeReceiptNumber : 1);
							cmaFeeReceipt.setOrderId(orderId);
							cmaFeeReceiptRepository.save(cmaFeeReceipt);
						}
					}
					List<UniformReceipt> uniformReceipts = uniformReceiptRepository.getUniformFeeReceiptByOrderId(orderId);
					if (ObjectUtils.isEmpty(uniformReceipts)) {
						List<RazorPayPaymentDetails> razorPayPaymentDetailsUniform = razorPayPaymentDetailsRepository
								.getByRazorPayTransactionIdAndReceiptType(razorPayTransaction.getRazorPayTransactionId(),
										"Uniform Fee");
						Integer uniformRecieptNumber = uniformReceiptRepository.getLatestReceitpNumber() + 1;
						for (RazorPayPaymentDetails uniform : razorPayPaymentDetailsUniform) {
							UniformReceipt uniformFeeReceipt = new UniformReceipt();
							uniformFeeReceipt.setActive(Boolean.TRUE);
							uniformFeeReceipt.setAmount(uniform.getAmount());
							uniformFeeReceipt.setFcYearId(financialYearRepository.getFinancialYearIdOnCurrentYear()
									.getFinancial_year_id().longValue());
							uniformFeeReceipt.setStudentId(razorPayTransaction.getStudentId().longValue());
							uniformFeeReceipt.setSem(uniform.getSem());
							uniformFeeReceipt.setYear(uniform.getYear());
							uniformFeeReceipt.setType("package");
							uniformFeeReceipt.setSchoolId(schoolId);
							uniformFeeReceipt.setOrderId(razorPayTransaction.getOrderId());
							uniformFeeReceipt.setPaidYear(uniform.getPaidYear());
							uniformFeeReceipt.setUniformReceiptNo(
									ObjectUtils.isNotEmpty(uniformRecieptNumber) ? uniformRecieptNumber : 1);
							uniformReceiptRepository.save(uniformFeeReceipt);
						}
					}
                   studentId = razorPayTransaction.getStudentId();
				}

				RegistrationFeeTransaction registrationPayTransaction = registrationFeeTrsactionRepository
						.findByOrderId(orderId);

				if (ObjectUtils.isNotEmpty(registrationPayTransaction)
						&& !"success".equalsIgnoreCase(registrationPayTransaction.getStatus())) {
//					registrationPayTransaction = new RegistrationFeeTransaction();
					registrationPayTransaction.setPaymentId(paymentId);
					registrationPayTransaction.setTransactionId(transactionId);
					registrationPayTransaction.setStatus("success");
					registrationPayTransaction.setTransactionDate(new Date());
					String currencyType = preAdmissionProcessRepository.getCurrencyTypeByCandidateId(registrationPayTransaction.getCandidateId());

					DollarToInrConversion dollarToInrConversion = calculateDollarRate(currencyType);
					if (ObjectUtils.isNotEmpty(dollarToInrConversion)) {
						registrationPayTransaction.setDollar_value(dollarToInrConversion.getInr());
					}
					registrationFeeTrsactionRepository.save(registrationPayTransaction);

					Candidate_Walkin candidate_Walkin = candidateWalkinRepository
							.getByCandidateId(registrationPayTransaction.getCandidateId());
					candidate_Walkin.setNpf_status(4);
					candidateWalkinRepository.save(candidate_Walkin);
					candidateWalkInService.updateLsqStatus(candidate_Walkin.getCandidate_id(), 5);
					callLeadSquareApiforUpdatePaymentSuccess(candidate_Walkin, registrationPayTransaction);
				}

				UniformTransaction uniformPayTransaction = uniformTransactionRepository.findByOrderId(orderId);

				if (ObjectUtils.isNotEmpty(uniformPayTransaction)
						&& !"success".equalsIgnoreCase(uniformPayTransaction.getStatus())) {

					uniformPayTransaction.setPaymentId(paymentId);
					uniformPayTransaction.setTransactionId(transactionId);
					uniformPayTransaction.setStatus("success");
					uniformPayTransaction.setTransactionDate(new Date());
					uniformTransactionRepository.save(uniformPayTransaction);

					generateUniFormReceipt(orderId);
                    studentId = uniformPayTransaction.getStudentId();
				}

				BulkTransaction bulkPayTransaction = bulkTransactionRepository.findByOrderId(orderId);

				if (ObjectUtils.isNotEmpty(bulkPayTransaction) && !"success".equalsIgnoreCase(bulkPayTransaction.getStatus())) {

					bulkPayTransaction.setPaymentId(paymentId);
					//	bulkPayTransaction.setTransactionId(transactionId);
					bulkPayTransaction.setStatus("success");
					bulkPayTransaction.setTransactionDate(new Date());
					bulkTransactionRepository.save(bulkPayTransaction);

					BulkFeeReceipt bulkFeeReceipt = new BulkFeeReceipt();
					bulkFeeReceipt.setActive(Boolean.TRUE);
					bulkFeeReceipt.setAmount(bulkPayTransaction.getAmount().doubleValue());
					bulkFeeReceipt.setFrom_name(bulkPayTransaction.getMobile());
					bulkFeeReceipt.setFinancial_year_id(
							financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
					Integer latestReceiptNumber = bulkFeeReceiptRepository.getMaxId();
					int receiptNo = (latestReceiptNumber != null) ? latestReceiptNumber : 0;
					if (receiptNo == 0) {
						receiptNo = 1;
					} else {
						receiptNo++;
					}

					bulkFeeReceipt.setBulk_fee_receipt(latestReceiptNumber);

				}

				if(studentId != null){
					StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, studentId,
							null);
					applicationEventPublisher.publishEvent(studentDueEvent);
				}
			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
		}
	}

	public ResponseEntity<Object> hostelFee(StudentTransactionDTO studentTransactionDTO) {
		try {
			RazorpayClient razorpayClient = razorPayConfig.getRazorpayClient(studentTransactionDTO.getSchoolId());
			List<RouteAccountDetails> routeAccountDetails = routeAccountDetailsRepository
					.getAccountDetails(studentTransactionDTO.getSchoolId());
			RouteAccountDetails hostelAccount = routeAccountDetails.stream()
					.filter(r -> r.getRouteType().equals("HOSTEL")).findFirst().get();

			TemporaryRazorPayTransaction razorPayTransaction = new TemporaryRazorPayTransaction();

			Student_Details student = studentDetailsRepository
					.getStudentByStudentId(studentTransactionDTO.getStudentId());
			razorPayTransaction.setAcYearId(studentTransactionDTO.getAcYearId());
			razorPayTransaction.setAmount((float) studentTransactionDTO.getTotalDue());
			razorPayTransaction.setCurrentSem(studentTransactionDTO.getCurrentSem());
			razorPayTransaction.setCurrentYear(studentTransactionDTO.getCurrentYear());
			razorPayTransaction.setTransactionType("Hostel Fee");
			String receiptId = "txn_" + System.currentTimeMillis();
			razorPayTransaction.setReceiptId(receiptId);
			int amountInPaise = (int) studentTransactionDTO.getTotalDue().intValue() * 100;
			JSONArray transfers = new JSONArray();

			JSONObject options = new JSONObject();
			options.put("amount", amountInPaise);
			options.put("currency", "INR");

			options.put("receipt", receiptId);
			JSONObject notes = new JSONObject();
			notes.put("amount", studentTransactionDTO.getTotalDue());
			notes.put("email",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
							? student.getAcharya_email()
							: "");
			notes.put("mobile",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getMobile()) ? student.getMobile()
							: "");
			notes.put("auid",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAuid()) ? student.getAuid()
							: "");
			JSONObject hostelAccountTransfer = new JSONObject();
			hostelAccountTransfer.put("account", hostelAccount.getRouteAccountId());
			hostelAccountTransfer.put("amount", studentTransactionDTO.getTotalDue() * 100);
			hostelAccountTransfer.put("currency", "INR");
			JSONObject hostelNotes = new JSONObject();
			hostelNotes.put("HostelFee", studentTransactionDTO.getTotalDue());
			hostelNotes.put("email",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
							? student.getAcharya_email()
							: "");
			hostelNotes.put("mobile",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAcharya_email())
							? student.getMobile()
							: "");
			hostelNotes.put("auid",
					ObjectUtils.isNotEmpty(student) && ObjectUtils.isNotEmpty(student.getAuid()) ? student.getAuid()
							: "");
			hostelAccountTransfer.put("notes", hostelNotes);

			transfers.put(hostelAccountTransfer);
			options.put("transfers", transfers);
			options.put("notes", notes);

			Order order = razorpayClient.Orders.create(options);
			String jsonObject = order.toJson().toString();
			System.out.println(jsonObject);
			ObjectMapper objectMapper = new ObjectMapper();
			RazorPayOrder razorpayOrder = objectMapper.readValue(jsonObject, RazorPayOrder.class);

			String orderId = order.get("id");
			razorPayTransaction.setOrderId(orderId);
			razorPayTransaction.setStudentId(studentTransactionDTO.getStudentId());
			temporaryRazorPayTransactionRepository.save(razorPayTransaction);

			for (HostelPayDTO hostel : studentTransactionDTO.getHostelPay()) {
				TemporaryRazorPayPaymentDetails hostelPay = new TemporaryRazorPayPaymentDetails();
				hostelPay.setAmount((double) hostel.getAmount());
				hostelPay.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
				hostelPay.setPaymentType("Hostel Fee");
				hostelPay.setReceiptType("Hostel Fee");
				hostelPay.setYear(hostel.getYear());
				hostelPay.setSem(hostel.getSem());
				hostelPay.setAcYearId(hostel.getAcYearId());
				temporaryRazorPayPaymentDetailsRepository.save(hostelPay);
			}

			return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", razorpayOrder);

		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}

	}

	public List<Map<String, Object>> getBulkPayTransaction(String transaction_id) {
		return studentDetailsRepository.getBulkPayTransaction(transaction_id);
	}

	public Map<String, Object> uniformfeereceipts(String date, String fDate, String tDate, Integer lot, String inname, String selectedType, String uid) {
		Map<String, Object> responseMap = new HashMap<>();

		try {
			Map<String, Object> response = new HashMap<>();
			LocalDate fromDate = (fDate != null && !fDate.isEmpty()) ? LocalDate.parse(fDate) : null;
			LocalDate toDate = (tDate != null && !tDate.isEmpty()) ? LocalDate.parse(tDate) : null;
			String datee = (date != null && !date.isEmpty()) ? date : null;
			String insname = (inname != null && !inname.isEmpty()) ? inname : null;
			String auid = (uid != null && !uid.isEmpty()) ? uid : null;
			String type = (selectedType != null && !selectedType.isEmpty()) ? selectedType : null;
			List<Map<String, Object>> uniformReceipts = null;
			if (ObjectUtils.isEmpty(lot)) {

				uniformReceipts = uniformReceiptRepository.getUniformFeeReceipts(datee, fromDate, toDate, insname, type, auid);
				response.put("receipts", uniformReceipts);

			} else {
				Pageable pageable = PageRequest.of(lot - 1, 100);


				long limit = pageable.getPageSize();

				long offset = pageable.getPageNumber() * pageable.getPageSize();
				List<Map<String, Object>> uniformPage = uniformReceiptRepository.getUniformFeeReceipts(datee, fromDate, toDate, insname, type, auid, limit, offset);
				long total = uniformReceiptRepository.countTotalReceipts(datee, fromDate, toDate, insname, type, auid);


				response.put("receipts", new PageImpl<>(uniformPage, pageable, total).getContent());

			}

			List<UniformDetailsDTO> uniformDetailsDTO = otherFeeDetailsRepository.getUniformFeeDetails();
			response.put("uniform_fees", uniformDetailsDTO);
			responseMap.put("success", true);
			responseMap.put("data", response);
			return responseMap;

		} catch (Exception e) {
			responseMap.put("success", false);
			responseMap.put("data", null);
			return responseMap;
		}

	}

	public Map<String, Object> validateauid(String auid) {
		Map<String, Object> responseMap = new HashMap<>();

		try {
			Map<String, Object> response = new HashMap<>();
			Student_Details studentDetails = studentDetailsRepository.findByAuid(auid);
			response.put("institute_id", studentDetails.getSchool_id());
			response.put("student_id", studentDetails.getStudent_id());
			response.put("auid", auid);
			response.put("student_name", studentDetails.getStudent_name());
			response.put("gender", studentDetails.getCandidate_sex());
			Integer uniformId = otherFeeDetailsRepository.getUniformId(studentDetails.getAc_year_id(),
					studentDetails.getProgram_id(), studentDetails.getProgram_specialization_id(),
					studentDetails.getSchool_id());
			response.put("uniform_id", uniformId);
			responseMap.put("success", true);
			responseMap.put("data", response);
			return responseMap;

		} catch (Exception e) {
			responseMap.put("success", false);
			responseMap.put("data", null);
			return responseMap;
		}
	}

	public Map<String, Object> uniformdetailstemplate(Integer utemp_id) {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			Map<String, Object> response = new HashMap<>();
			List<OtherFeeDetails> otherFeeDetails = otherFeeDetailsRepository.getOtherFeeDetails(utemp_id,
					"Uniform And Stationery Fee");
			Map<String, Object> otherFeeDetailsData = new HashMap<>();
			Map<String, Object> courses = new HashMap<>();

			Double year1 = otherFeeDetails.stream().mapToDouble(o -> o.getSem1() != null ? o.getSem1() : 0.0).sum();
			Double year2 = otherFeeDetails.stream().mapToDouble(o -> o.getSem2() != null ? o.getSem2() : 0.0).sum();
			Double year3 = otherFeeDetails.stream().mapToDouble(o -> o.getSem3() != null ? o.getSem3() : 0.0).sum();
			Double year4 = otherFeeDetails.stream().mapToDouble(o -> o.getSem4() != null ? o.getSem4() : 0.0).sum();
			Double year5 = otherFeeDetails.stream().mapToDouble(o -> o.getSem5() != null ? o.getSem5() : 0.0).sum();
			Double year6 = otherFeeDetails.stream().mapToDouble(o -> o.getSem6() != null ? o.getSem6() : 0.0).sum();
			Double year7 = otherFeeDetails.stream().mapToDouble(o -> o.getSem7() != null ? o.getSem7() : 0.0).sum();
			Double year8 = otherFeeDetails.stream().mapToDouble(o -> o.getSem8() != null ? o.getSem8() : 0.0).sum();
			Double year9 = otherFeeDetails.stream().mapToDouble(o -> o.getSem9() != null ? o.getSem9() : 0.0).sum();
			Double year10 = otherFeeDetails.stream().mapToDouble(o -> o.getSem10() != null ? o.getSem10() : 0.0).sum();
			Double year11 = otherFeeDetails.stream().mapToDouble(o -> o.getSem11() != null ? o.getSem11() : 0.0).sum();
			Double year12 = otherFeeDetails.stream().mapToDouble(o -> o.getSem12() != null ? o.getSem12() : 0.0).sum();


			otherFeeDetailsData.put("year1_amt", ObjectUtils.isNotEmpty(year1) ? year1 : 0);
			otherFeeDetailsData.put("year2_amt", ObjectUtils.isNotEmpty(year2) ? year2 : 0);
			otherFeeDetailsData.put("year3_amt", ObjectUtils.isNotEmpty(year3) ? year3 : 0);
			otherFeeDetailsData.put("year4_amt", ObjectUtils.isNotEmpty(year4) ? year4 : 0);
			otherFeeDetailsData.put("year5_amt", ObjectUtils.isNotEmpty(year5) ? year5 : 0);
			otherFeeDetailsData.put("year6_amt", ObjectUtils.isNotEmpty(year6) ? year6 : 0);
			otherFeeDetailsData.put("year7_amt", ObjectUtils.isNotEmpty(year7) ? year7 : 0);
			otherFeeDetailsData.put("year8_amt", ObjectUtils.isNotEmpty(year8) ? year8 : 0);
			otherFeeDetailsData.put("year9_amt", ObjectUtils.isNotEmpty(year9) ? year9 : 0);
			otherFeeDetailsData.put("year10_amt", ObjectUtils.isNotEmpty(year10) ? year10 : 0);
			otherFeeDetailsData.put("year11_amt", ObjectUtils.isNotEmpty(year11) ? year11 : 0);
			otherFeeDetailsData.put("year12_amt", ObjectUtils.isNotEmpty(year12) ? year12 : 0);

			response.put("package_fee", otherFeeDetailsData);

			OtherFeeTemplate otherFeeTemplate = otherFeeTemplateRepository.findById(utemp_id).get();
			ProgramAssigment programAssigment = programAssigmentRepository
					.getByProgramId(otherFeeTemplate.getProgramId(), otherFeeTemplate.getSchoolId());
			courses.put("course_id", programAssigment.getProgram_id());
			courses.put("number_of_semester", programAssigment.getNumber_of_semester());
			courses.put("number_of_years", programAssigment.getNumber_of_years());
			response.put("course", courses);
			responseMap.put("data", response);

			return responseMap;

		} catch (Exception e) {
			responseMap.put("data", null);

			return responseMap;

		}
	}

	public Map<String, Object> uniformadmissioncount() {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			List<Map<String, Object>> response = otherFeeDetailsRepository.getUniformAdmissionCount();
			responseMap.put("success", true);
			responseMap.put("data", response);
			return responseMap;

		} catch (Exception e) {
			responseMap.put("success", false);
			responseMap.put("data", null);
			return responseMap;
		}
	}

	public Map<String, Object> uniformnames(Integer ac_year_id) {
		Map<String, Object> responseMap = new HashMap<>();

		try {
			List<Map<String, Object>> response = studentDetailsRepository
					.getStudentDetailsForUniformTemplate(ac_year_id);
			responseMap.put("success", true);
			responseMap.put("data", response);
			return responseMap;
		} catch (Exception e) {
			responseMap.put("success", false);
			responseMap.put("data", null);
			return responseMap;
		}
	}

	public Map<String, Object> acyear() {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			List<Map<String, Object>> response = academicYearRepository.getAcYears();
			responseMap.put("success", true);
			responseMap.put("data", response);
			return responseMap;

		} catch (Exception e) {
			responseMap.put("success", false);
			responseMap.put("data", null);
			return responseMap;

		}
	}

	public Map<String, Object> institutes() {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			List<Map<String, Object>> response = schoolRepository.getInstitutes();
			responseMap.put("success", true);
			responseMap.put("data", response);
			return responseMap;

		} catch (Exception e) {
			responseMap.put("success", false);
			responseMap.put("data", null);
			return responseMap;
		}
	}

	public List<Map<String, Object>> getCountOfAdmissionDate(String date_of_admission, Integer school_id) {
		// Get the current Academic Year Id based on the given date_of_admission
		Integer acYearId = academicYearRepository.getCurrentAcademicYearId(date_of_admission);
		if (school_id != null) {
			return getAdmissionCounts(acYearId, date_of_admission, school_id);
		} else {
			return getAdmissionCountsWOSclId(acYearId, date_of_admission);
		}

	}

	public List<Map<String, Object>> getAdmissionCounts(Integer acYearId, String date_of_admission, Integer school_id) {
		List<Map<String, Object>> results = new ArrayList<>();

		// Loop from the given acYearId down to 1 to generate the required queries
		for (int year = acYearId; year >= 1; year--) {
			// Get the cutoff date dynamically using the adjusted getDateOfAdmission method
			String cutoffDate = getDateOfAdmission(year, date_of_admission);
			System.out.println("cutoffDate -------- " + cutoffDate);
			// Call the repository method to get the count of students for this year and cutoff date
			List<Map<String, Object>> queryResults = studentDetailsRepository.getCountOfAdmissionDate(year, cutoffDate, school_id);

			// Add the results to the final list
			results.addAll(queryResults);
		}

		return results;
	}

	public List<Map<String, Object>> getAdmissionCountsWOSclId(Integer acYearId, String date_of_admission) {
		List<Map<String, Object>> results = new ArrayList<>();

		// Loop from the given acYearId down to 1 to generate the required queries
		for (int year = acYearId; year >= 1; year--) {
			// Get the cutoff date dynamically using the adjusted getDateOfAdmission method
			String cutoffDate = getDateOfAdmission(year, date_of_admission);

			// Call the repository method to get the count of students for this year and cutoff date
			List<Map<String, Object>> queryResults = studentDetailsRepository.getAdmissionCountsWOSclId(year, cutoffDate);

			// Add the results to the final list
			results.addAll(queryResults);
		}

		return results;
	}

	public String getDateOfAdmission(int acYearId, String date_of_admission) {

		Integer ac_yar_id = academicYearRepository.getLatestAcYearId();

		// Define the custom date format (DD-MM-YYYY)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		// Convert the date_of_admission (in DD-MM-YYYY format) to LocalDate
		LocalDate admissionDate = LocalDate.parse(date_of_admission, formatter);

		// Calculate the cutoff date by subtracting years from the date_of_admission
		int yearToSubtract = ac_yar_id - acYearId; // This will give us the correct years to subtract

		LocalDate cutoffDate = admissionDate.minusYears(yearToSubtract);

		// Format the cutoff date as a string and return it
		return cutoffDate.toString(); // Return the date in "YYYY-MM-DD" format
	}

	public List<Map<String, Object>> getRegistrationFeeDetailsOfStudent(Integer studentId) {
		return studentDetailsRepository.getRegistrationFeeDetailsOfStudent(studentId);
	}

	public Map<String, Object> getStudentDetailsBasedOnUserId(Integer userId) {
		return studentDetailsRepository.getStudentDetailsBasedOnUserId(userId);
	}

	private DollarToInrConversion calculateDollarRate(String currencyType) throws ParseException {


		if (StringUtils.isNotEmpty(currencyType) && !StringUtils.equals(currencyType, "INR")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = dateFormat.format(new Date());
			Date newDate = dateFormat.parse(formattedDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(newDate);
			int month = calendar.get(Calendar.MONTH) + 1;
			int year = calendar.get(Calendar.YEAR);

			return dollarToInrConversionRepository.findByDateAndActive(month,
					year);
		}
		return null;
	}

	public ResponseEntity<Object> studentsForPaidAtBoardTag(Integer feeTemplateId, Integer yearOrSem) {
		List<Map<String, Object>> response = new ArrayList<>();
		List<Map<String, Object>> students = studentDetailsRepository.studentsForPaidAtBoardTag(feeTemplateId);
		students.forEach(st -> {
			Map<String, Object> feeTemplateAmount = feeTemplateRepository.boardPaidAmount(feeTemplateId, yearOrSem);
			Map<String, Object> paidAmount = tagBoardAmountRepository.paidAmount((Integer) st.get("student_id"), feeTemplateId, yearOrSem);
			st.putAll(feeTemplateAmount);
			st.putAll(paidAmount);
		});
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	public void generateUniFormReceipt(String orderId) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		UniformTransaction razorPayTransaction = uniformTransactionRepository
				.findByOrderId(orderId);
		Integer school_id = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
		FinancialYear fcYear = financialYearRepository.getFinancialYearIdOnCurrentYear();
		List<UniformReceipt> uniformReceipts = uniformReceiptRepository.getUniformFeeReceiptByOrderId(orderId);
		if (ObjectUtils.isEmpty(uniformReceipts) && ObjectUtils.isNotEmpty(razorPayTransaction)) {
			List<UniformFeeTransactionDetails> uniformFeeTransactionDetails = uniformFeeTransactionDetailsRepository
					.getAllByUniformTransactionId(razorPayTransaction.getUniformTransactionId());
			Integer latestReceiptNumber = uniformReceiptRepository.getLatestReceitpNumber() + 1;
			for (UniformFeeTransactionDetails ut : uniformFeeTransactionDetails) {
				UniformReceipt uniformReceipt = new UniformReceipt();
				uniformReceipt.setAmount((double) (ut.getAmount() * ut.getQuantity()));
				uniformReceipt.setType("Loose");
				String date = simpleDateFormat.format(razorPayTransaction.getTransactionDate());
				uniformReceipt.setTransactionDate(date);
				uniformReceipt.setActive(Boolean.TRUE);
				uniformReceipt.setTransactionType(ut.getItemName());
				uniformReceipt.setCgst_input(ut.getCgst_input());
				uniformReceipt.setCgst_output(ut.getCgst_output());
				uniformReceipt.setSgst_input(ut.getSgst_input());
				uniformReceipt.setSgst_output(ut.getSgst_output());
				uniformReceipt.setGst(ut.getGst());
				uniformReceipt.setEnv_item_id(ut.getEnv_item_id());
				uniformReceipt.setQuantity(ut.getQuantity());
				uniformReceipt.setStudentId((long) razorPayTransaction.getStudentId());
				uniformReceipt.setOrderId(razorPayTransaction.getOrderId());
				uniformReceipt.setUniformReceiptNo(latestReceiptNumber);
				uniformReceipt.setFcYearId((long) fcYear.getFinancial_year_id());
				uniformReceipt.setSchoolId(school_id);
				uniformReceipt.setTotal_amount(Double.valueOf(razorPayTransaction.getAmount()));
				uniformReceiptRepository.save(uniformReceipt);
			}
		}
	}

	public ResponseEntity<Object> generateCmaReceipt(String orderId) {
		try {
			List<CmaFeeReceipt> cmaFeeReceipts = cmaFeeReceiptRepository.getCmaFeeReceiptByOrderId(orderId);
			if (ObjectUtils.isEmpty(cmaFeeReceipts)) {
				RazorPayTransaction razorPayTransaction = razorPayTransactionRepository.getByOrderId(orderId);
				List<RazorPayPaymentDetails> razorPayPaymentDetailsAddOn = razorPayPaymentDetailsRepository
						.getByRazorPayTransactionIdAndReceiptType(razorPayTransaction.getRazorPayTransactionId(),
								"Add On Fee");
				Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
				Integer cmaFeeReceiptNumber = cmaFeeReceiptRepository.getLatestReceitpNumber(financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id()) + 1;
				for (RazorPayPaymentDetails addOn : razorPayPaymentDetailsAddOn) {
					CmaFeeReceipt cmaFeeReceipt = new CmaFeeReceipt();
					cmaFeeReceipt.setActive(Boolean.TRUE);
					cmaFeeReceipt.setAmount(addOn.getAmount());
					cmaFeeReceipt.setFinancial_year_id(
							financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
					cmaFeeReceipt.setStudent_id(razorPayTransaction.getStudentId());
					cmaFeeReceipt.setPaid_year(String.valueOf(addOn.getSem()));
					cmaFeeReceipt.setReceipt_type(addOn.getReceiptType());
					cmaFeeReceipt.setSchool_id(schoolId);
					cmaFeeReceipt
							.setCma_receipt_id(ObjectUtils.isNotEmpty(cmaFeeReceiptNumber) ? cmaFeeReceiptNumber : 1);
					cmaFeeReceipt.setOrderId(orderId);
					cmaFeeReceiptRepository.save(cmaFeeReceipt);
				}


				StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, razorPayTransaction.getStudentId(),
						null);
				applicationEventPublisher.publishEvent(studentDueEvent);

				return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", orderId);

			}

			return ResponseHandler.generateResponse1(true, HttpStatus.OK, "Add On receipt is already present");
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);

		}
	}

	public void generateUniFormPackageReceipt(String orderId) {
		List<UniformReceipt> uniformReceipts = uniformReceiptRepository.getUniformFeeReceiptByOrderId(orderId);
		if (ObjectUtils.isEmpty(uniformReceipts)) {
			RazorPayTransaction razorPayTransaction = razorPayTransactionRepository.getByOrderId(orderId);
			Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
			List<RazorPayPaymentDetails> razorPayPaymentDetailsUniform = razorPayPaymentDetailsRepository
					.getByRazorPayTransactionIdAndReceiptType(razorPayTransaction.getRazorPayTransactionId(),
							"Uniform Fee");
			Integer uniformRecieptNumber = uniformReceiptRepository.getLatestReceitpNumber() + 1;
			for (RazorPayPaymentDetails uniform : razorPayPaymentDetailsUniform) {
				UniformReceipt uniformFeeReceipt = new UniformReceipt();
				uniformFeeReceipt.setActive(Boolean.TRUE);
				uniformFeeReceipt.setAmount(uniform.getAmount());
				uniformFeeReceipt.setFcYearId(financialYearRepository.getFinancialYearIdOnCurrentYear()
						.getFinancial_year_id().longValue());
				uniformFeeReceipt.setStudentId(razorPayTransaction.getStudentId().longValue());
				uniformFeeReceipt.setSem(uniform.getSem());
				uniformFeeReceipt.setYear(uniform.getYear());
				uniformFeeReceipt.setType("package");
				uniformFeeReceipt.setSchoolId(schoolId);
				uniformFeeReceipt.setOrderId(razorPayTransaction.getOrderId());
				uniformFeeReceipt.setPaidYear(uniform.getPaidYear());
				uniformFeeReceipt.setUniformReceiptNo(
						ObjectUtils.isNotEmpty(uniformRecieptNumber) ? uniformRecieptNumber : 1);
				uniformReceiptRepository.save(uniformFeeReceipt);
			}
		}
	}


	public void generateCmaReceiptForBulk(String orderId) {
		List<CmaFeeReceipt> cmaFeeReceipts = cmaFeeReceiptRepository.getCmaFeeReceiptByOrderId(orderId);
		BulkTransaction bulkTransaction = bulkTransactionRepository.getBulkAddOnTransactionByOrderId(orderId);
		if (ObjectUtils.isEmpty(cmaFeeReceipts) && ObjectUtils.isNotEmpty(bulkTransaction)) {


			Integer cmaFeeReceiptNumber = cmaFeeReceiptRepository.getLatestReceitpNumber(financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id()) + 1;
				CmaFeeReceipt cmaFeeReceipt = new CmaFeeReceipt();
				cmaFeeReceipt.setActive(Boolean.TRUE);
				cmaFeeReceipt.setAmount(bulkTransaction.getAmount().doubleValue());
				cmaFeeReceipt.setFinancial_year_id(
						financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
				cmaFeeReceipt.setPaid_year(bulkTransaction.getPaidYear());
				cmaFeeReceipt.setReceipt_type("BULK");
				cmaFeeReceipt.setSchool_id(bulkTransaction.getSchoolId());
				cmaFeeReceipt
						.setCma_receipt_id(ObjectUtils.isNotEmpty(cmaFeeReceiptNumber) ? cmaFeeReceiptNumber : 1);
				cmaFeeReceipt.setOrderId(orderId);
				cmaFeeReceipt.setName(bulkTransaction.getName());
				cmaFeeReceipt.setEmail(bulkTransaction.getEmail());
				cmaFeeReceipt.setVoucherHeadId(bulkTransaction.getVoucherHeadId());
				cmaFeeReceipt.setTransactionId(Integer.valueOf(bulkTransaction.getTransactionId()));
				cmaFeeReceiptRepository.save(cmaFeeReceipt);


		}

	}

	public ResponseEntity<Object> allSchoolWiseDueReport() {
		try {
			List<Schools> schools = schoolRepository.findAll();
			return fetchSchoolWiseDueReport(schools);
		} catch (Exception e) {
		return ResponseHandler.generateResponse(false, HttpStatus.OK, e.getMessage(), null);
	}
	}
}
