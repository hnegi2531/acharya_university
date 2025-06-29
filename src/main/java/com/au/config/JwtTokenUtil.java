package com.au.config;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.au.dto.DepartmentDTO;
import com.au.dto.JwtDetails;
import com.au.dto.LmsLoginResponse;
import com.au.model.Academic_year;
import com.au.model.EmployeeDetails;
import com.au.model.Program;
import com.au.model.ReportingStudents;
import com.au.model.Roles;
import com.au.model.Student_Details;
import com.au.model.TokenDetails;
import com.au.model.UserAuthentication;
import com.au.repository.Academic_year_repository;
import com.au.repository.DepartmentRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.ProgramAssigmentRepository;
import com.au.repository.ProgramRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.RolesRepository;
import com.au.repository.School_Repository;
import com.au.repository.SectionAssignmentRepository;
import com.au.repository.SectionRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.StudentDueRepository;
import com.au.repository.TokenDetailsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.service.EncryptionDecriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.au.config.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {
	
	Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
	
	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY_FOR_STUDENT = 100 * 60 * 60; // 100 hours
	
	public static final long JWT_TOKEN_VALIDITY_FOR_STAFF = 100 * 60 * 60; // 1 hr

	@Value("${jwt.secret}")
	private String secret;
	
	@Autowired
	private TokenDetailsRepository tokenDetailsRepository;
	
	@Autowired
	private EmployeeDetailsRepository employeeDetailsHistoryRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private EncryptionDecriptionService encryptionDecriptionService;
	
	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;
	
	@Autowired
	private School_Repository schoolRepository;
	
	@Autowired
	private ProgramSpecilizationRepository programSpecilizationRepository;
	
	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;
	
	@Autowired
	private SectionAssignmentRepository sectionAssignmentRepository;
	
	@Autowired
	private SectionRepository sectionRepository;
	
	@Autowired
	private ProgramRepository programRepository;
	
	@Autowired
	private Academic_year_repository academicYeaRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private StudentDueRepository studentDueRepository;
	
	@Autowired
	private ProgramAssigmentRepository programAssigmentRepository;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		/* here you specify tokens, for that the expiration is ignored */
		return false;
	}

//	public String generateToken(UserDetails userDetails, UserAuthentication users) throws JsonProcessingException {
//		Map<String, Object> claims = new HashMap<>();
//		claims.put("userId", users.getId());
//		claims.put("userName", users.getUsername());
//		claims.put("userType",users.getUsertype());
//		return doGenerateToken(claims, userDetails.getUsername());
//	}
//
//	private String doGenerateToken(Map<String, Object> claims, String subject) throws JsonProcessingException {
//		return Jwts.builder().setClaims(claims).setSubject(new ObjectMapper().writeValueAsString(claims))
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//				.signWith(SignatureAlgorithm.HS512, secret).compact();
//	}
	
	public String generateToken(UserDetails userDetails, UserAuthentication users) throws JsonProcessingException {
		Map<String, Object> claims = new HashMap<>();
		if (users.getUsertype().equals("Student")) {
			
			claims.put("userId", users.getId());
			claims.put("userName", users.getUsername());
			claims.put("userType", users.getUsertype());
			return doGenerateTokenForStudent(claims, userDetails.getUsername());
			
		} else {
			
			claims.put("userId", users.getId());
			claims.put("userName", users.getUsername());
			claims.put("userType", users.getUsertype());
			return doGenerateTokenForStaff(claims, userDetails.getUsername());
		}
	}
	
	private String doGenerateTokenForStudent(Map<String, Object> claims, String subject) throws JsonProcessingException {
		String jwtToken=Jwts.builder().setClaims(claims).setSubject(new ObjectMapper().writeValueAsString(claims))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_FOR_STUDENT * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
		 
		
		return jwtToken;
	}
	
	private String doGenerateTokenForStaff(Map<String, Object> claims, String subject) throws JsonProcessingException {
		String jwtToken = Jwts.builder().setClaims(claims).setSubject(new ObjectMapper().writeValueAsString(claims))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_FOR_STAFF * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();

		return jwtToken;
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	/* validate token */
	public Boolean validateToken(String token, UserDetails userDetails) throws JsonProcessingException {
		final String username = getUsernameFromToken(token);
		JwtDetails details = new ObjectMapper().readValue(username, new TypeReference<JwtDetails>() {});
		return (details.getUserName().equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public LmsLoginResponse generateLmsToken(JwtDetails jwtDetails, String token, TokenDetails tokenDetails) throws JsonProcessingException {
		try {
		LmsLoginResponse lmsLoginResponse=new LmsLoginResponse();
		Roles roles =rolesRepository.getUserRolebyuserId(jwtDetails.getUserId());
		
		Map<String, Object> claims = new HashMap<>();		
		if(StringUtils.equalsIgnoreCase(jwtDetails.getUserType(), "Staff") ) {
			EmployeeDetails employeeDetails=employeeDetailsHistoryRepository.getEmployeeDataByUserID(jwtDetails.getUserId());
			if(ObjectUtils.isEmpty(employeeDetails)) {
				return null;
			}
			
			claims.put("user_id", ObjectUtils.isNotEmpty(jwtDetails.getUserId())?jwtDetails.getUserId():"");	
			claims.put("emp_id", ObjectUtils.isNotEmpty(employeeDetails.getEmp_id())?employeeDetails.getEmp_id():"");
			claims.put("role_id",ObjectUtils.isNotEmpty(roles) && ObjectUtils.isNotEmpty(roles.getRole_id())?roles.getRole_id():null);	
			claims.put("lms_role",ObjectUtils.isNotEmpty(roles) &&  ObjectUtils.isNotEmpty(roles.getLms_role())?roles.getLms_role():null);	
			setLmsresponseForEmployee(lmsLoginResponse,employeeDetails,jwtDetails,roles);
		}
		if(StringUtils.equalsIgnoreCase(jwtDetails.getUserType(), "Student") ) {
			Student_Details studentDetails=studentDetailsRepository.getStudentDataByUserID(jwtDetails.getUserId());
			String instituteShortName=null;
			String courseBranchShortName=null;
			ReportingStudents reportingStudents=null;
//			Integer sectionAssignmentId=null;
			String sectionName=null;
			String courseBranchName=null;
			Program program=null;
			Academic_year academicYear=null;
			if(ObjectUtils.isNotEmpty(studentDetails) && ObjectUtils.isNotEmpty(studentDetails.getStudent_id()) ) {
			 instituteShortName= schoolRepository.getSchoolShortName(studentDetails.getSchool_id());
			 courseBranchShortName=programSpecilizationRepository.getProgramSpecilizationShortName(studentDetails.getProgram_specialization_id());
			 reportingStudents=reportingStudentsRepository.getDetailsOfReportingStudentsByStudentId(studentDetails.getStudent_id());
//			 sectionAssignmentId=sectionAssignmentRepository.getSectionAssignmentIdByStudentId(studentDetails.getStudent_id());
			 courseBranchName=programSpecilizationRepository.getProgramSpecilizationName(studentDetails.getProgram_specialization_id());
			 program=programRepository.findById(studentDetails.getProgram_id()).get();
			 academicYear=academicYeaRepository.findById(studentDetails.getAc_year_id()).get();
			}
			if(ObjectUtils.isNotEmpty(reportingStudents.getSection_id())) {
			 sectionName=sectionRepository.getSectionNamebySectionId(reportingStudents.getSection_id());
			}
			if(ObjectUtils.isEmpty(studentDetails)) {
				return null;
			}
			claims.put("student_id", ObjectUtils.isNotEmpty(studentDetails.getStudent_id())?studentDetails.getStudent_id():"");	
			claims.put("role_id",ObjectUtils.isNotEmpty(roles) && ObjectUtils.isNotEmpty(roles.getRole_id())?roles.getRole_id():null);	
			claims.put("lms_role",ObjectUtils.isNotEmpty(roles) &&  ObjectUtils.isNotEmpty(roles.getLms_role())?roles.getLms_role():null);	
			claims.put("user_id", ObjectUtils.isNotEmpty(jwtDetails.getUserId())?jwtDetails.getUserId():"");	
			claims.put("current_year",ObjectUtils.isNotEmpty(reportingStudents) && ObjectUtils.isNotEmpty(reportingStudents.getCurrent_year())?reportingStudents.getCurrent_year():"");	
			claims.put("current_sem",ObjectUtils.isNotEmpty(reportingStudents) && ObjectUtils.isNotEmpty(reportingStudents.getCurrent_sem())?reportingStudents.getCurrent_sem():"");	
			
			setLmsresponseForStudent(lmsLoginResponse,studentDetails,jwtDetails,roles,instituteShortName,courseBranchShortName,reportingStudents,sectionName,courseBranchName,program,academicYear);
		}
		String accessToken=  doGenerateLmsToken(claims);
		tokenDetails.setToken(token);
		tokenDetails.setAccessToken(accessToken);
		tokenDetailsRepository.save(tokenDetails);
		lmsLoginResponse.setAccessToken(accessToken);
		lmsLoginResponse.setToken(accessToken);
		return lmsLoginResponse;
		}catch(Exception e) {
			log.error("Exception occur in LmsLoginResponse --> generateLmsToken "+ ExceptionUtils.getMessage(e));
			return null;
		}

	}
	
	
	  private void setLmsresponseForStudent(LmsLoginResponse lmsLoginResponse, Student_Details studentDetails,
				JwtDetails jwtDetails,Roles roles, String instituteShortName, String courseBranchShortName, ReportingStudents reportingStudents, String sectionName,
				String courseBranchName, Program program, Academic_year academicYear ) {
		   	lmsLoginResponse.setStudent_name(ObjectUtils.isNotEmpty(studentDetails.getStudent_name())?studentDetails.getStudent_name():"");
		   	lmsLoginResponse.setAuid(ObjectUtils.isNotEmpty(studentDetails.getAuid())?studentDetails.getAuid():"");
		 	lmsLoginResponse.setImage_path(ObjectUtils.isNotEmpty(studentDetails.getStudent_image_path())?studentDetails.getStudent_image_path():"");
			lmsLoginResponse.setAcerp_email(ObjectUtils.isNotEmpty(studentDetails.getAcharya_email())?studentDetails.getAcharya_email():"");
			lmsLoginResponse.setUsn(ObjectUtils.isNotEmpty(studentDetails.getUsn())?studentDetails.getUsn():"");
		 	

		   	if(ObjectUtils.isNotEmpty(roles)) {	
		   		lmsLoginResponse.setLms_role_id(ObjectUtils.isNotEmpty(roles.getRole_id())?roles.getRole_id():null);	
		   		lmsLoginResponse.setLms_role(ObjectUtils.isNotEmpty(roles.getLms_role())?roles.getLms_role().toUpperCase():null);
		   	} 
		   	if(ObjectUtils.isNotEmpty(program)) {	
			 lmsLoginResponse.setCourse_name(ObjectUtils.isNotEmpty(program.getProgram_name())?program.getProgram_name():null);	
			 lmsLoginResponse.setCourse_short_name(ObjectUtils.isNotEmpty(program.getProgram_short_name())?program.getProgram_short_name():null);
			}
		   	if(ObjectUtils.isNotEmpty(academicYear)) {	
		   		lmsLoginResponse.setAc_year(ObjectUtils.isNotEmpty(academicYear.getAc_year())?academicYear.getAc_year():null);
		   	}
			 lmsLoginResponse.setInstitute_name_short(ObjectUtils.isNotEmpty(instituteShortName)?instituteShortName:"");	
			 lmsLoginResponse.setCourse_branch_short_name(ObjectUtils.isNotEmpty(courseBranchShortName)?courseBranchShortName:null);
			 lmsLoginResponse.setCurrent_year(ObjectUtils.isNotEmpty(reportingStudents) && ObjectUtils.isNotEmpty(reportingStudents.getCurrent_year())?reportingStudents.getCurrent_year():null);
			 lmsLoginResponse.setCurrent_sem(ObjectUtils.isNotEmpty(reportingStudents) && ObjectUtils.isNotEmpty(reportingStudents.getCurrent_sem())?reportingStudents.getCurrent_sem():null);
			 lmsLoginResponse.setSection_short_name(ObjectUtils.isNotEmpty(sectionName) ?sectionName:null);
			 lmsLoginResponse.setCourse_branch_name(ObjectUtils.isNotEmpty(courseBranchName)?courseBranchName:null);
			 lmsLoginResponse.setCourse_branch_assignment_id(ObjectUtils.isNotEmpty(studentDetails.getProgram_specialization_id())?studentDetails.getProgram_specialization_id():null);
			 lmsLoginResponse.setSection_status(ObjectUtils.isNotEmpty(sectionName) ?true:false);
			 lmsLoginResponse.setInstitute_id(studentDetails.getSchool_id());
			 setEligibleStatus(reportingStudents,lmsLoginResponse);
			 dueStatusUpdate(lmsLoginResponse,studentDetails,reportingStudents);
			 
			 setUserDetails(jwtDetails.getUserId(),lmsLoginResponse);
		}
	  
	  private static final Integer NO_STATUS=1; 
	  private static final Integer ELIGIBLE=2; 
	  private void setEligibleStatus(ReportingStudents reportingStudents, LmsLoginResponse lmsLoginResponse) {
		if(ObjectUtils.isNotEmpty(reportingStudents) && ObjectUtils.isNotEmpty(reportingStudents.getEligible_reported_status())) {
			if(reportingStudents.getEligible_reported_status().equals( NO_STATUS) || reportingStudents.getEligible_reported_status().equals(ELIGIBLE)) {
				lmsLoginResponse.setEligible_status(true);
			}else {
				lmsLoginResponse.setEligible_status(false);
			}
		}else {
			lmsLoginResponse.setEligible_status(null);
		}
		
	}
	private static final String SEMESTER_PROGRAM_TYPE="Semester";
	private static final Double NO_DUE_VALUE=0.0;
	private static final Integer NEW_ADMISSION_DUE_EXEMPT_DAYS=15;
	private void dueStatusUpdate(LmsLoginResponse lmsLoginResponse, Student_Details studentDetails,
			ReportingStudents reportingStudents) {
		  Double dues=null;
		  String programType=programAssigmentRepository.getProgramTypeByProgramAssignmentId(studentDetails.getProgram_assignment_id());
		  if(ObjectUtils.isNotEmpty(programType) && StringUtils.equalsIgnoreCase(programType, SEMESTER_PROGRAM_TYPE))
			  dues=studentDueRepository.totalDueTillCurrentSemOfStudent(reportingStudents.getCurrent_sem(),studentDetails.getStudent_id());
		  else {
			  dues=studentDueRepository.totalDueTillCurrentSemOfStudent(reportingStudents.getCurrent_year(),studentDetails.getStudent_id());
		  }
		  LocalDate dateOfAdmissionLocatDate=LocalDate.parse(studentDetails.getDate_of_admission().toString());
		  Period period=Period.between(dateOfAdmissionLocatDate ,LocalDate.now());
		  lmsLoginResponse.setDue_status(dues.equals(NO_DUE_VALUE) || period.getDays() <= NEW_ADMISSION_DUE_EXEMPT_DAYS ? true :false);
		  
		
	}

	private void setLmsresponseForEmployee(LmsLoginResponse lmsLoginResponse, EmployeeDetails employeeDetails, JwtDetails jwtDetails,Roles roles ) {
			String  instituteShortName= schoolRepository.getSchoolShortName(employeeDetails.getSchool_id());
			  DepartmentDTO departmentDTO=departmentRepository.departmentDetailsByDepartmentId(employeeDetails.getDept_id());	
			  lmsLoginResponse.setEmpcode(ObjectUtils.isNotEmpty(employeeDetails.getEmpcode())?employeeDetails.getEmpcode():"");
			  lmsLoginResponse.setEmployee_name(ObjectUtils.isNotEmpty(employeeDetails.getEmployee_name())?employeeDetails.getEmployee_name():"");
			  lmsLoginResponse.setEmp_id(ObjectUtils.isNotEmpty(employeeDetails.getEmp_id())?employeeDetails.getEmp_id():null);
			  lmsLoginResponse.setContract_empcode(ObjectUtils.isNotEmpty(employeeDetails.getContract_empcode())?employeeDetails.getContract_empcode():"");
			  lmsLoginResponse.setMaster_code(ObjectUtils.isNotEmpty(employeeDetails.getMaster_code())?employeeDetails.getMaster_code():"");
			  lmsLoginResponse.setInstitute_name_short(ObjectUtils.isNotEmpty(instituteShortName)?instituteShortName:"");
			  lmsLoginResponse.setImage_path(ObjectUtils.isNotEmpty(employeeDetails.getEmp_image_attachment_path())?employeeDetails.getEmp_image_attachment_path():"");
			  lmsLoginResponse.setInstitute_id(employeeDetails.getSchool_id());
			  if(ObjectUtils.isNotEmpty(roles)) {	
				  lmsLoginResponse.setLms_role_id(ObjectUtils.isNotEmpty(roles.getRole_id())?roles.getRole_id():null);	
				  lmsLoginResponse.setLms_role(ObjectUtils.isNotEmpty(roles.getLms_role())?roles.getLms_role().toUpperCase():null);
			  }
			  
			  if(ObjectUtils.isNotEmpty(departmentDTO)) {	
				  lmsLoginResponse.setBranch_id(ObjectUtils.isNotEmpty(departmentDTO.getDepartmentId())?departmentDTO.getDepartmentId():null);
				  lmsLoginResponse.setBranch_name(ObjectUtils.isNotEmpty(departmentDTO.getDepartmentName())?departmentDTO.getDepartmentName():"");
			  }

			  setUserDetails(jwtDetails.getUserId(),lmsLoginResponse);
		  }
	  
	  
	  private UserAuthentication setUserDetails(Integer userId, LmsLoginResponse lmsLoginResponse) {
		  UserAuthentication userDetails=userAuthenticationRepository.findById(userId).get();
		  lmsLoginResponse.setUsername(ObjectUtils.isNotEmpty(userDetails) && ObjectUtils.isNotEmpty(userDetails.getUsername())?userDetails.getUsername():null);
		  lmsLoginResponse.setEmail(ObjectUtils.isNotEmpty(userDetails) && ObjectUtils.isNotEmpty(userDetails.getEmail())?userDetails.getEmail():null);
		  lmsLoginResponse.setIs_auditor(ObjectUtils.isNotEmpty(userDetails) && ObjectUtils.isNotEmpty(userDetails.getIs_lms_auditor())?userDetails.getIs_lms_auditor():null);

		  return userDetails;
	  } 
	  
	  
	  private String doGenerateLmsToken(Map<String, Object> claims) throws JsonProcessingException {

		  return 	Jwts.builder().setClaims(claims).setSubject(new ObjectMapper().writeValueAsString(claims))
				  .setIssuedAt(new Date(System.currentTimeMillis()))
				  .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_FOR_STUDENT * 1000))
				  .signWith(SignatureAlgorithm.HS512, secret).compact();

	  }
}