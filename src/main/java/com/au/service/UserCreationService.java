package com.au.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.au.model.UserAuthentication;
import com.au.repository.UserAuthenticationRepository;

@Service
public class UserCreationService {

	@Autowired
	private UserAuthenticationRepository uar_repo;

	public UserAuthentication saveUserAuthentication(UserAuthentication userauthentication) {
		String pass = userauthentication.getPassword();
		System.out.println(pass);
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwordEncoder1 = passwordEncoder.encode(pass);
		System.out.println(passwordEncoder1);
		userauthentication.setPassword(passwordEncoder1);
		return uar_repo.save(userauthentication);

	}
}
