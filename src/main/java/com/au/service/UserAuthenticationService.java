package com.au.service;

import com.au.config.JwtTokenUtil;
import com.au.dto.*;
import com.au.exception.ResourceNotFoundException;
import com.au.model.EmployeeDetails;
import com.au.model.Student_Details;
import com.au.model.UserAuthentication;
import com.au.model.UserRole;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.repository.UserRoleRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.validation.Valid;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;



@Service
public class UserAuthenticationService {

	@Autowired
	private UserAuthenticationRepository uar_repo;

	@Autowired
	private UserRoleRepository urr_repo;
	
	@Autowired
	private EmployeeDetailsRepository employeeRepo;
	
	@Autowired
	private StudentDetailsRepository studentsRepo;

//	@Autowired
//	private JavaMailSender mailSender;
	
	  @Autowired
	    @Qualifier("mailSenderPrimary")  // ✅ Explicitly use the primary mail sender
	    private JavaMailSender mailSender;
	
	@Autowired
	private JwtTokenService jwt_service;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private UserAuthenticationRepository authentication;
	
	@Autowired
	private ResponseHandler response_handler;

	@Autowired
	private WhatsappService whatsappService;
	
	private static final String DEFAULT_VALUE="acharya1234";

	public List<UserAuthentication> listAll() {
		return uar_repo.findAll1();
	}
	
	public List<UserAuthentication> getGuestDetailsData() {
		return uar_repo.getGuestDetailsData();
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
		//String dynamicQuery1 = CONCAT(cw.ac_year_id, ' ',cw.candidate_name) LIKE %:keyword%;
		//String dynamicQuery1 = column +"="+keyword +" and "+column+"="+ value;
		Page<Object> roles_filtered_response = uar_repo.getAllDataFilteredByKeyword(pageable, keyword );
		
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
		}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable) {

		Page<Object> roles_sorted_response = uar_repo.getAllSortedData(pageable);
		return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
	}
	
	public UserAuthentication saveUserAuthentication(UserAuthentication userauthentication) {
		return uar_repo.save(userauthentication);
	}
	
	public UserAuthentication updateUserPassword(UserAuthenticationDto dto, String jwtToken, Integer userId) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);

		UserAuthentication userAuthentication = uar_repo.findById(dto.getUserId())
		.orElseThrow(()-> new ResourceNotFoundException("User not found"));
		String pass = "acharya12345";
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwordEncoder1 = passwordEncoder.encode(pass);
		userAuthentication.setPassword(passwordEncoder1);
		userAuthentication.setId(dto.getUserId());
		userAuthentication.setModified_by(jwtDetails.getUserId());
		userAuthentication.setModified_username(jwtDetails.getUserName());
		
		return uar_repo.save(userAuthentication);
	}

	public UserAuthentication get(Integer id) {
		return uar_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found:" + id));
	}

	public ResponseEntity<Object> delete(Integer id) {
		UserAuthentication ay = uar_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found:" + id));
		uar_repo.update(id);
		urr_repo.deactivateRoleOfUser(id);
		UserAuthentication user_auth = uar_repo.findById1(id);
		if (user_auth.getActive() == false) {
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
					HttpStatus.NOT_MODIFIED);
			return response1;
		}
	}

	public ResponseEntity<Object> delete1(Integer id) {
		UserAuthentication ay = uar_repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found:" + id));
		uar_repo.update1(id);
		UserAuthentication user_auth1=uar_repo.findById1(id);
		if(user_auth1.getActive()==true) {
			ResponseEntity<Object> response= ResponseHandler.generateResponseForPutApiAndDeleteApi(true, HttpStatus.OK);
			return response;
		}else {
			ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false, HttpStatus.NOT_MODIFIED);
			return response1;
		}
	}

	public UserAuthentication saveUserRole(UserRoleRequest userrolerequest, String jwtToken)
			throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		if (uar_repo.countOfUsername(userrolerequest.getUsername()) >= 1) {
			throw new RuntimeException("Username already exist");
		} else {
			UserAuthentication userauthentication = new UserAuthentication();
			userauthentication.setId(userrolerequest.getId());
			userauthentication.setEmail(userrolerequest.getEmail());
			userauthentication.setUsercode(userrolerequest.getUsercode());
			userauthentication.setUsertype(userrolerequest.getUsertype());
			userauthentication.setUsername(userrolerequest.getUsername());
			userauthentication.setActive(userrolerequest.getActive());
			userauthentication.setCreated_by(jwtDetails.getUserId());
			userauthentication.setCreated_username(jwtDetails.getUserName());
			userauthentication.setGuest_type(userrolerequest.getGuest_type());
			String pass = "acharya1234";
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String passwordEncoder1 = passwordEncoder.encode(pass);
			System.out.println(passwordEncoder1);
			userauthentication.setPassword(passwordEncoder1);

			UserAuthentication user = saveUserAuthentication(userauthentication);
			userrolerequest.getRole_id().stream().forEach(u -> {
				UserRole userrole = new UserRole();
				userrole.setId(user.getId());
				userrole.setRole_id(u);
				userrole.setCreated_by(jwtDetails.getUserId());
				userrole.setCreated_username(jwtDetails.getUserName());
				userrole.setActive(userrolerequest.getActive());
				urr_repo.save(userrole);
			});

			System.out.println("(((((((1))))))) ");
//			String content = "Dear " + ResponseHandler.nameCapital(userauthentication.getUsername()) + "!!\r\n" + "\r\n"
//					+ "Thank you for completing your registration." + "\r\n" + "Below is the credentials for log in : "
//					+ "\r\n\n" + "Username :" + userauthentication.getUsername() + "\r\n" + "Password :" + pass + "\r\n"
//					+ "Email :" + userauthentication.getEmail() + "\r\n\n" + "Team ERP";
//			response_handler.sendSimpleEmail(userrolerequest.getEmail(), content, "Don't Reply");
			System.out.println("(((((((2))))))) ");
			return user;
		}

	}

	public HashMap<String, Object> getSubMenuDetails(Integer id) {
		HashMap<String, Object> response=new HashMap<>();
		String submenu_ids1 = "%,"+id + ",%";
		String submenu_ids2=  id+",%";
		String submenu_ids3= "%,"+id;
		String submenu_ids4 =id.toString();
		response.put("SubMenuList",uar_repo.getSubMenuDetail(submenu_ids1, submenu_ids2, submenu_ids3,submenu_ids4));
		return response;	
	}
	
	public ResponseEntity<Object> processForgotPassword(String url_domain, String username) throws JsonProcessingException{

		UserAuthentication user=uar_repo.userDetailsByUsername(username);
		String nameOfUser="";
		String userName="";
		if((user.getUsertype()).equalsIgnoreCase("Staff")) {
			nameOfUser=ObjectUtils.isNotEmpty(employeeRepo.getEmpNameByEmail(user.getEmail()))?employeeRepo.getEmpName(user.getEmail()):user.getUsername();
			 userName = "Dear " + nameOfUser;
		} else {
			nameOfUser=ObjectUtils.isNotEmpty(studentsRepo.studentNameByEmail(user.getEmail()))?studentsRepo.studentNameByEmail(user.getEmail()):user.getUsername();
			 userName = "Dear Student";
		}

		HashMap<String,String> response_message=new HashMap<>();

		if (url_domain == null) {
			if (user == null) {

				response_message.put("message","Username Not Found");
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,response_message);

			} else {
				String name_of_email = user.getEmail().substring(0, 3);
				String[] email=user.getEmail().split("@");
				if(ObjectUtils.isNotEmpty(user.getReset_password_time())) {
					LocalDateTime old_time=user.getReset_password_time();
					Duration duration=Duration.between(old_time, LocalDateTime.now()); 
					if(duration.toMinutes() <= 2) {
						response_message.put("message", "OTP is already sent to email "+name_of_email + "******@" + email[1]);
						return ResponseHandler.generateResponse(true, HttpStatus.OK,response_message);
					} else {

						int otp = (int) (Math.random() * 900000) + 100000;
						System.out.println("+++++++++++++++++ " + otp);
						final String token = String.valueOf(otp);
						LocalDateTime reset_password_time_gen = LocalDateTime.now();
						uar_repo.updatePasswordResetToken(user.getId(), reset_password_time_gen, token);
						String resetPasswordLink = token;

						String content = userName  + "<br/> " + "<br/> "
								+ "You have requested to reset password for your ERP account. To proceed with this request,please use the following OTP (One-Time Password) to verify your identity : "  
								+ "<br/> " + "<br/> "
								+ "<b> OTP : " + resetPasswordLink   + "</b><br/> " + "<br/> "
								+ "Please enter this OTP on the password reset page to complete the verification process. This OTP is valid for 2 Minnutes only." + "<br/> "
								+ "If you did not request this password reset or believe you have received this email in error,please ignore it. No changes will be made to your account. " + "<br/> " + "<br/> "
								+ "Thank you for choosing our service. " + "<br/> " + "<br/> "
								+ "Team - ERP " + "<br/> " + "Acharya Institutes"
								+ "<br/> " + "<br/> " 
								+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
								+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
						String subject = " Reset ERP Password ";
						response_handler.sendSimpleEmailWithHtmlContent(user.getEmail(), content, subject);
						response_message.put("message",
								"A OTP to reset password is sent to " + name_of_email + "******@" + email[1]);
						return ResponseHandler.generateResponse(true, HttpStatus.OK,response_message);
					}
					
				} else {

					int otp = (int) (Math.random() * 900000) + 100000;
					System.out.println("+++++++++++++++++ " + otp);
					final String token = String.valueOf(otp);
					LocalDateTime reset_password_time_gen = LocalDateTime.now();
					uar_repo.updatePasswordResetToken(user.getId(), reset_password_time_gen, token);
					String resetPasswordLink = token;

					String content = userName  + "<br/> " + "<br/> "
							+ "You have requested to reset password for your ERP account. To proceed with this request,please use the following OTP (One-Time Password) to verify your identity : "  
							+ "<br/> " + "<br/> "
							+ "<b> OTP : " + resetPasswordLink   + "</b><br/> " + "<br/> "
							+ "Please enter this OTP on the password reset page to complete the verification process. This OTP is valid for 2 Minnutes only." + "<br/> "
							+ "If you did not request this password reset or believe you have received this email in error,please ignore it. No changes will be made to your account. " + "<br/> " + "<br/> "
							+ "Thank you for choosing our service. " + "<br/> " + "<br/> "
							+ "Team - ERP " + "<br/> " + "Acharya Institutes"
							+ "<br/> " + "<br/> " 
							+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
							+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
					String subject = " Reset ERP Password ";
					response_handler.sendSimpleEmailWithHtmlContent(user.getEmail(), content, subject);
					response_message.put("message",
							"A OTP to reset password is sent to " + name_of_email + "******@" + email[1]);
					return ResponseHandler.generateResponse(true, HttpStatus.OK,response_message);
				}
			}

		} else {

			if (user == null) {

				response_message.put("message","Username Not Found");
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,response_message);

			} else {

				String name_of_email = user.getEmail().substring(0, 3);
				String[] email=user.getEmail().split("@");
				if(ObjectUtils.isNotEmpty(user.getReset_password_time())) {
					LocalDateTime old_time=user.getReset_password_time();
					Duration duration=Duration.between(old_time, LocalDateTime.now()); 
					if(duration.toMinutes() <= 2) {
						response_message.put("message", "Link is already sent to email "+name_of_email + "******@" + email[1]);
						return ResponseHandler.generateResponse(true, HttpStatus.OK,response_message);
					}else {

						final String token = RandomString.make(30);
						LocalDateTime reset_password_time_gen = LocalDateTime.now();
						uar_repo.updatePasswordResetToken(user.getId(), reset_password_time_gen, token);
						String resetPasswordLink = url_domain + token;

						String content = userName  + "<br/> " + "<br/> "
								+ "You have requested to reset password for your ERP account. To proceed with this request,please use the following OTP (One-Time Password) to verify your identity : "  
								+ "<br/> " + "<br/> "
								+ "<b> OTP : " + resetPasswordLink   + "</b><br/> " + "<br/> "
								+ "Please enter this OTP on the password reset page to complete the verification process. This OTP is valid for 2 Minnutes only." + "<br/> "
								+ "If you did not request this password reset or believe you have received this email in error,please ignore it. No changes will be made to your account. " + "<br/> " + "<br/> "
								+ "Thank you for choosing our service. " + "<br/> " + "<br/> "
								+ "Team - ERP " + "<br/> " + "Acharya Institutes"
								+ "<br/> " + "<br/> " 
								+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
								+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
						String subject = " Reset ERP Password ";
						response_handler.sendSimpleEmailWithHtmlContent(user.getEmail(), content, subject);
						response_message.put("message",
								"A link to reset password is sent to " + name_of_email + "******@" +email[1]);
						return ResponseHandler.generateResponse(true, HttpStatus.OK,response_message);
					}
				} else {

					final String token = RandomString.make(30);
					LocalDateTime reset_password_time_gen = LocalDateTime.now();
					uar_repo.updatePasswordResetToken(user.getId(), reset_password_time_gen, token);
					String resetPasswordLink = url_domain + token;

					String content = userName  + "<br/> " + "<br/> "
							+ "You have requested to reset password for your ERP account. To proceed with this request,please use the following OTP (One-Time Password) to verify your identity: "  
							+ "<br/> " + "<br/> "
							+ "<b> OTP : " + resetPasswordLink   + "</b><br/> " + "<br/> "
							+ "Please enter this OTP on the password reset page to complete the verification process. This OTP is valid for 2 Minnutes only." + "<br/> "
							+ "If you did not request this password reset or believe you have received this email in error,please ignore it. No changes will be made to your account. " + "<br/> " + "<br/> "
							+ "Thank you for choosing our service. " + "<br/> " + "<br/> "
							+ "Team - ERP " + "<br/> " + "Acharya Institutes"
							+ "<br/> " + "<br/> " 
							+ "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
							+ "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
					String subject = " Reset ERP Password ";
					response_handler.sendSimpleEmailWithHtmlContent(user.getEmail(), content, subject);
					response_message.put("message",
							"A link to reset password is sent to " + name_of_email + "******@" +email[1]);
					return ResponseHandler.generateResponse(true, HttpStatus.OK,response_message);
				}
			}

		}
	}
	
	public ResponseEntity<Object> resetPassword(String token,String new_password){

		String old_reset_password_token=uar_repo.fetchResetTokenByToken(token);
		HashMap<String,String> response=new HashMap<>();

		if(ObjectUtils.isNotEmpty(old_reset_password_token)) {
			LocalDateTime old_time=uar_repo.fetchOldResetTimeByToken(token);
			Duration duration=Duration.between(old_time, LocalDateTime.now());


			if(old_reset_password_token != null && duration.toMinutes() <= 2) {

				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String passwordEncoder1 = passwordEncoder.encode(new_password);
				uar_repo.resetPasswordAndToken(token,passwordEncoder1);
				response.put("message", "password reset Successfully !!");
				return  ResponseHandler.generateResponse(true, HttpStatus.OK,response);
				
			}else {
				response.put("message", "Link OR OTP Expired");
				return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,response);
			}
		}else {
			response.put("message", "Reset Password is not generated");
			return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR,response);
			
		}


	}
	
	public List<HashMap<String, Object>> userDetailswithDepartment() {
		return uar_repo.userDetailswithDepartment();	
	}
	
	public HashMap<String, String> changePassword(String username, String new_password) {
		UserAuthentication user = uar_repo.userDetailsByUsername(username);
		HashMap<String, String> response = new HashMap<>();
		if (user != null) {
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String passwordEncoder1 = passwordEncoder.encode(new_password);
			uar_repo.changePassword(username, passwordEncoder1);

			response.put("message", "Password changed Successfully !!");
			return response;
		} else {
			response.put("message", "Username Not Found !! ");
			return response;
		}
	}
	
	public List<HashMap<String,Object>> staffUserDetails() {
		return uar_repo.staffUserDetails();
	}
	

	public UserDetailsDto getUserDetailsById(Integer id) {
		UserAuthentication user = authentication.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id not found."));
		UserDetailsDto dto = new UserDetailsDto();
		if (user.getUsertype().equalsIgnoreCase("Staff")) {
			EmployeeDetails emp = employeeRepo.findByEmailAndActiveTrue(user.getEmail());
			if (emp == null) {
				throw new ResourceNotFoundException("Employee not present");
			}
			dto.setPreferredName(emp.getPreferred_name_for_email());
			dto.setEmpOrStdId(emp.getEmp_id());
			dto.setName(emp.getEmployee_name());
			dto.setEmail(emp.getEmail());
			dto.setPhotoAttachmentPath(emp.getEmp_image_attachment_path());
			dto.setMobileNumber(emp.getMobile());
			dto.setUserId(user.getId());
			dto.setUsertype(user.getUsertype());
			dto.setFirstName(emp.getFirstname());
			dto.setLastName(emp.getLastname());
		} else if (user.getUsertype().equalsIgnoreCase("student")) {
			Student_Details student = studentsRepo.findByAcharyaEmail(user.getEmail());
			if (student == null) {
				throw new ResourceNotFoundException("Student not present");
			}
			dto.setEmpOrStdId(student.getStudent_id());
			dto.setName(student.getStudent_name());
			dto.setPreferredName(student.getEmail_preferred_name());
			dto.setFirstName(student.getFirstname());
			dto.setLastName(student.getLastname());
			dto.setEmail(student.getAcharya_email());
			dto.setPhotoAttachmentPath(student.getStudent_image_path());
			dto.setUserId(user.getId());
			dto.setUsertype(user.getUsertype());
			dto.setMobileNumber(student.getMobile());
		}
		return dto;
	}

	public ResponseEntity<Object> userPasswordUpdateByDefaultPassword(Integer userId, String jwtToken) {

		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			UserAuthentication userAuthentication = get(userId);
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String passwordEncoder1 = passwordEncoder.encode(DEFAULT_VALUE);
			userAuthentication.setPassword(passwordEncoder1);
			userAuthentication.setModified_by(jwtDetails.getUserId());
			userAuthentication.setModified_username(jwtDetails.getUserName());
			uar_repo.save(userAuthentication);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "User Password is updated", null);
		} catch(Exception  e) {

			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}

	}

	public List<Map<String, Object>> getDeptIdBasedOnUserId(Integer user_id) {
		return uar_repo.getDeptIdBasedOnUserId(user_id);
	}
	
	
	public Map<String, Object> getSchoolIdBasedOnUserId(Integer user_id) {
		return uar_repo.getSchoolIdBasedOnUserId(user_id);
	}
	
	public ResponseEntity<Object> activateUserByEmployeeId(Integer id) {
		EmployeeDetails ay = employeeRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found:" + id));
		UserAuthentication userDetails=uar_repo.getUserByEmail(ay.getEmail());
		uar_repo.update1(userDetails.getId());
		urr_repo.activateRoleOfUser(id);
		UserAuthentication user_auth1 = uar_repo.findById1(userDetails.getId());
		if (user_auth1.getActive() == true) {
			ResponseEntity<Object> response = ResponseHandler.generateResponseForPutApiAndDeleteApi(true,
					HttpStatus.OK);
			return response;
		} else {
			ResponseEntity<Object> response1 = ResponseHandler.generateResponseForPutApiAndDeleteApiWithFalse(false,
					HttpStatus.NOT_MODIFIED);
			return response1;
		}
	}

	public ResponseEntity<Object> passwordChecker(@Valid UserRoleRequest user) {
	try {
		UserAuthentication userAuthentication=uar_repo.findById1(user.getId());
		String existingPassword=userAuthentication.getPassword();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Map<String,Object> response=new HashMap<>();
		if(passwordEncoder.matches(user.getPassword(), existingPassword)) {
			response.put("isCheck", true);
		}else {
			response.put("isCheck", false);
		}
		return ResponseHandler.generateResponse(true,
				HttpStatus.OK,"SUCCESS",response);
	}catch(Exception e) {
		return ResponseHandler.generateResponse(true,
				HttpStatus.INTERNAL_SERVER_ERROR,"FAILURE",null);
	}
	}

	public ResponseEntity<Object> resetPasswordOtpCheckr(@Valid OtpCheckRequest otp) {

		try {
			UserAuthentication userAuthentication=uar_repo.findByOtp(otp.getOtp());
			Map<String,Object> response=new HashMap<>();
			if(ObjectUtils.isNotEmpty(userAuthentication)) {
				response.put("isCheck", true);
			}else {
				response.put("isCheck", false);
			}
			return ResponseHandler.generateResponse(true,
					HttpStatus.OK,"SUCCESS",response);
		}catch(Exception e) {
			return ResponseHandler.generateResponse(true,
					HttpStatus.INTERNAL_SERVER_ERROR,"FAILURE",null);
		}
	}
	
	public ResponseEntity<Object> updatebookChapterApproverDesignationForUser(Integer userId,String bookChapterApproverDesignation, String jwtToken) {

		try {
			JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
			UserAuthentication userAuthentication = get(userId);
			userAuthentication.setModified_by(jwtDetails.getUserId());
			userAuthentication.setModified_username(jwtDetails.getUserName());
			if(bookChapterApproverDesignation.equalsIgnoreCase("null")){
				userAuthentication.setBook_chapter_approver_designation(null);
			}else {
				if(uar_repo.approverDesignationAssignmentcheck(bookChapterApproverDesignation) >=1){
					return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "Approver designation is already assigned to someone.", null);
				}else {
					userAuthentication.setBook_chapter_approver_designation(bookChapterApproverDesignation);
				}
			}
			uar_repo.save(userAuthentication);
			return ResponseHandler.generateResponse(true, HttpStatus.OK, "User Approver designation is updated", null);
		} catch(Exception  e) {

			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
		}

	}

	public ResponseEntity<Object> getAllUserListForLibrary() {
		List<UserListForLibrary> userListForLibraries = uar_repo.getAllUserTypes();
		List<UserForLibrary> userForLibraries = new ArrayList<>();
		userListForLibraries.stream().forEach(u -> {
			UserForLibrary user = new UserForLibrary();
			if (u.getEmail() != null
					&& (StringUtils.equals(u.getUserType(), "Staff") || StringUtils.equals(u.getUserType(), "staff"))) {
				EmployeeDetailsByEmail employee = employeeRepo.getEmployeeByEmail(u.getEmail());
				if (ObjectUtils.isNotEmpty(employee)) {
					user.setUserId(employee.getEmpId());
					user.setUserName(employee.getEmployeeName());
					user.setName(employee.getEmployeeName());
					user.setUserCode(employee.getEmpCode());
					user.setUserType(u.getUserType());
					userForLibraries.add(user);
				}
			}
			if (u.getEmail() != null && (StringUtils.equals(u.getUserType(), "Student")
					|| StringUtils.equals(u.getUserType(), "student"))) {
				StudentDetailsbyEmail student = studentsRepo.getStudentByEmail(u.getEmail());
				if (ObjectUtils.isNotEmpty(student)) {
					user.setUserId(student.getStudentId());
					user.setUserName(student.getStudentName());
					user.setName(student.getStudentName());
					user.setUserCode(student.getAuid());
					user.setUserType(u.getUserType());
					userForLibraries.add(user);
				}
			}

		});

		return ResponseHandler.generateResponse(true, HttpStatus.OK, "User Details List", userForLibraries);
	}

	public ResponseEntity<Object> processForgotPassword(String username) {
		try {
			UserAuthentication user = uar_repo.userDetailsByUsername(username);
			if (user == null)
				return ResponseHandler.generateResponse(true, HttpStatus.NOT_FOUND, "Invalid Username");

			Optional<Tuple> optionalTuple;
			if (user.getUsertype().equalsIgnoreCase("Staff")) {
				optionalTuple = employeeRepo.findMobileByActiveTrueAndEmail(user.getEmail());
			} else {
				optionalTuple = studentsRepo.findMobileByActiveTrueAndAcharyaEmail(user.getEmail());
			}
			if (!optionalTuple.isPresent())
				return ResponseHandler.generateResponse(true, HttpStatus.NOT_FOUND, "Mobile number not available for: " + username);

			String name = optionalTuple.get().get(0, String.class);
			String phone = optionalTuple.get().get(1, String.class);
			if (phone.contains("+") && phone.contains("-")) {
				phone = phone.replace("+", "").replace("-", "");
			} else {
				phone = "91" + phone;
			}
			WhatsappDto whatsappDto = new WhatsappDto();
			whatsappDto.setPhone(phone);
			whatsappDto.setUserId(user.getId());
			whatsappDto.setUserName(name);

			return whatsappService.saveAndSendOtp(whatsappDto);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public UserAuthentication UserAuthenticationWithUserRole(UserRoleRequestWithUserRole userrolerequest,
			String jwtToken) throws JsonParseException, JsonMappingException, IOException {
		JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
		if (uar_repo.countOfUsername1(userrolerequest.getEmail()) >= 1) {
			throw new RuntimeException("User already exist");
		} else {
			UserAuthentication userauthentication = new UserAuthentication();
			userauthentication.setId(userrolerequest.getId());
			userauthentication.setEmail(userrolerequest.getEmail());
			userauthentication.setUsercode(userrolerequest.getUsercode());
			userauthentication.setUsertype(userrolerequest.getUsertype());
			userauthentication.setUsername(userrolerequest.getUsername());
			userauthentication.setActive(userrolerequest.getActive());
			userauthentication.setCreated_by(jwtDetails.getUserId());
			userauthentication.setCreated_username(jwtDetails.getUserName());
			userauthentication.setGuest_type(userrolerequest.getGuest_type());
			String pass = "acharya1234";
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String passwordEncoder1 = passwordEncoder.encode(pass);
			System.out.println(passwordEncoder1);
			userauthentication.setPassword(passwordEncoder1);
			
			UserAuthentication savedUser = uar_repo.save(userauthentication);
			
			UserAuthentication user = saveUserAuthentication(userauthentication);
			UserRole userrole = new UserRole();
			userrole.setId(user.getId());
			userrole.setRole_id(userrolerequest.getRole_id());
			userrole.setCreated_by(jwtDetails.getUserId());
			userrole.setCreated_username(jwtDetails.getUserName());
			userrole.setActive(userrolerequest.getActive());
			urr_repo.save(userrole);
			return savedUser;
		}

	}
}