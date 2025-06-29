package com.au.controller;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.au.config.JwtTokenUtil;
import com.au.exception.CredentialFailures;
import com.au.model.JwtRequest;
import com.au.model.JwtResponse;
import com.au.model.UserAuthentication;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;

import com.au.model.Roles;
import com.au.repository.RolesRepository;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private UserAuthenticationRepository authentication;
	
	@Autowired
	private RolesRepository rolesRepository;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {
		
		UserAuthentication user = authentication.findByUsernameCaseSensitive(authenticationRequest.getUsername());
		if(ObjectUtils.isEmpty(user)) {
			throw new CredentialFailures("Incrorrect Username");
		}
		
		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
		
		String password=userDetails.getPassword();
		System.out.println("Retrieved password hash: " + password);
		
		PasswordEncoder encoder=new BCryptPasswordEncoder();
		String defaultPassword="acharya1234";
		System.out.println("Encoded acharya1234: " + encoder.encode("acharya1234"));
		
		if(encoder.matches(defaultPassword, password)) {
			ResponseEntity<Object> authenticateResponse= ResponseHandler.generateResponse(true, HttpStatus.NOT_ACCEPTABLE, null);
			return authenticateResponse;
    	}
		
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		Roles roles =rolesRepository.getUserRolebyuserId(user.getId());

		final String token = jwtTokenUtil.generateToken(userDetails, user);
		JwtResponse jwtResponse=new JwtResponse(token);
		jwtResponse.setUserName(user.getUsername());
		jwtResponse.setUserId(user.getId());
		jwtResponse.setUserType(user.getUsertype());
		jwtResponse.setBook_chapter_approver_designation(user.getBook_chapter_approver_designation());
		jwtResponse.setRole(ObjectUtils.isNotEmpty(roles) && ObjectUtils.isNotEmpty(roles.getRole_name())?roles.getRole_name():"");
		jwtResponse.setAdpStatus(roles.getAdp_status());
		ResponseEntity<Object> authenticateResponse= ResponseHandler.generateResponse(true, HttpStatus.OK, jwtResponse);
		return authenticateResponse;
		//return ResponseEntity.ok(jwtResponse);
	}

	private void authenticate(String username, String password) throws CredentialFailures {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new CredentialFailures("USER_DISABLED");
		} catch (BadCredentialsException e) {
			throw new CredentialFailures("Incorrect Password");
		}
	}

}