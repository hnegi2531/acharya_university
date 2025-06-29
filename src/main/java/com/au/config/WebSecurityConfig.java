
package com.au.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.cors().and().csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers("/api/authenticate").permitAll().
				//antMatchers("/**").permitAll().
				// all other requests need to be authenticated
				anyRequest().authenticated().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);	

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/UserCreation").antMatchers("/api/employee/fetchAllJobProfileDetails","/api/employee/JobProfile",
				"/api/employee/higherEducationUploadFile","/api/employee/phdDetails","/api/employee/JobProfileReferenceNo/{job_id}",
				"/api/academic/academic_year","/api/institute/school","/api/academic/FetchProgramSpecialization/{school_id}/{program_id}",
				"/api/academic/fetchAllProgramsWithProgramType/{school_id}","/api/employee/uploadEducationDetailsAttachment","/api/employee/uploadEmployeeIDsAttachment",
				"/api/employee/employeeDetailsFileDownload","/api/employee/fileDownloadOfEducationDetailsAttachment",
				"/api/employee/EducationDetails","/api/employee/ExperienceDetails","/api/employee/checkEmail/{email}",
				"/api/employee/JobUploadFile","/api/Country","/api/employee/graduation","/api/State1/{countryId}",
				"/api/forgotPassword","/api/forgotPasswordWhatsapp","/api/resetPassword","/api/UserAuthentication","/api/Roles","/api/City1/{state_id}/{country_id}",
				"/api/court","/api/courtCases","/api/courtCases/{court_cases_id}","/api/fetchAllCourtCasesDetails",
				"/api/activateCourtCases/{court_cases_id}","/api/courtCaseDetailsHistoryOnCaseNo","/api/stageOfTheCase",
				"/api/fetchAllStageOfTheCaseDetail","/api/stageOfTheCase/{stage_of_the_case_id}","/api/student/Candidate_Walkin1",
				"/api/student/Candidate_WalkinForLsq","/api/finance/getFeeTemplateRemarksDetails/{fee_template}",
				"/api/callOutbound","/api/getCallOutbound",
				"/api/student/bulkPayment","/api/academic/academicYearGT",
				"/api/student/bulkPaymentStatus",
				"/api/State",
				"/api/student/getBulkTransactionDetails",
				"/api/finance/getFeePaymentWindow/{id}",
				"/api/employee/jobProfileDetailsOnDate/{created_date}",
				"/api/stageOfTheCase/{stage_of_the_case_id}","/api/stageOfTheCase/{stage_of_the_case_id}","/api/student/saveCandidateWalkinDump",
				"/api/legalDepartmentUsers","/api/regenrationOfToken","/api/activateStageOfTheCase/{stage_of_the_case_id}",
				"/api/courtCasesUpdateOnly/{court_cases_id}","/api/updatelegalDepartmentUsers","/api/employee/updateOfferAfterAccepting",
				"/api/legalDepartmentUsers/{legal_department_user_name}","/api/courtCaseDetailsHistoryOnId/{court_cases_history_id}","/api/student/paymePayment",
				"/api/student/startingOfPayment","/api/feedback/verificationOfTelegramIdAndUsername","/api/feedback/verificationProcessOfTelegramIdAndUsername",
				"/api/feedback/verificationProcessOfTelegramIdAndUsernameByWebhooks","/api/student/startingOfClickPayment",
				"/api/student/prepareClickPayment","/api/student/completeClickPayment","/api/academic/fetchAllBatchDetails",
				"/api/employee/getEducationDocsAttachmentById/{empId}","/api/employee/uploadEmployeeContractsAttachment",
				"/api/student/feeHeadAmountRestrictionDetailsForPayment/{feeHeadAmountRestrictionId}",
				"/api/student/getPaymentStatus",
				"/api/student/studentOfferAcceptance",
				"/api/student/findAllDetailsPreAdmission/{candidate_id}",
				"/api/student/getClickPaymentStatus",
				"/api/student/getFeeDetails",
				"/api/student/emailToCandidateRegardingOfferLetter",
				"/api/employee/getEmployeeIDsAttachmentById/{empId}",
				"/api/employee/getEmployeeContractssAttachmentById/{empId}",
				"/api/academic/fetchAllProgramsAndSpecializationWithProgramType",
				"/api/academic/programsDetailsWithProgramType",
				"/api/employee/newEducationDetails",
				"/api/resetPasswordOtpCheckr",
				"/api/lms/login",
				"/api/lms/getImage",
        		"/api/lms/fetchTimeTableDetailsByCurrentDate",
        		"/api/lms/courses",
        		"/api/lms/getAcademicYears",
        		"/api/lms/getBatchAndSectionbyAcademicYear",
        		"/api/lms/getStudentBatchAndSectionList/{time_table_id}",
        		"/api/lms/fetchTimeTableDetailsBySelectDateForStudents",
        		"/api/lms/fetchTimeTableDetailsBySelectDateForEmployees",
        		"/api/lms//getCurrentYearSectionAndBatchOfStudent",
        		"/api/student/getRegistrationFeeDetails",
        		"/api/student/registrationFee",
        		"/api/student/registrationFeePaymentStatus",
        		"/api/student/getCandidateTransactionDetails","/api/health",
        		"/api/lms/studentDetailsByAuidForLms",
        		"/api/lms/schoolDetailsForLms",
        		"/api/lms/studentListBySchoolSpecializationAcYearAndTokenFlag",
        		"/api/lms/studentSubjects",
        		"/api/lms/acYearDetails",
        		"/api/lms/assignedStudentDetailsToProctor",
        		"/api/lms/specializationDetails",
        		"/api/lms/batchOrSectionAssignedStudentDetails",
        		"/api/student/candidateWalkinForLsqApplicationStatus",
        		"/api/callOutbound",
        		"/api/student/getTransactionDetailsForPhp",
				"/api/whatsapp/**", "/api/studentTagBoardAmount",
				"/api/student/uniformnames","/api/student/uniformfeereceipts",
				"/api/student/validateauid","/api/student/uniformdetailstemplate"
				,"/api/student/uniformadmission-count"
				,"/api/student/acyear","/api/student/institutes"
		        ,"/api/transaction","/api/student/paymentCaptureFromRazorpay")
		.antMatchers("/v2/api-docs",
				"/configuration/ui",
				"/swagger-resources/**",
				"/configuration/security",
				"/swagger-ui.html",
				"/webjars/**");
	}
	
	
}
