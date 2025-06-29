package com.au.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.au.model.LegalDepartmentUsers;
import com.au.repository.LegalDepartmentUsersRepository;

import net.bytebuddy.utility.RandomString;

@Service
public class LegalDepartmentUsersService {
	
	
	@Autowired
	private LegalDepartmentUsersRepository legal_users_repo;
	
	
	public LegalDepartmentUsers saveLegalDepartmentUsers(LegalDepartmentUsers legal_users){
		if(legal_users_repo.countOfLegalDepartmentUserName(legal_users.getLegal_department_user_name())>=1) {
			throw new RuntimeException("Username Name Already Exist");
		}else if(legal_users_repo.countOfLegalDepartmentUserPassword(legal_users.getLegal_department_user_password())>=1) {
			throw new RuntimeException("Password Already Exist");
		}else {
			final String legal_validation_token =RandomString.make(30);
			legal_users.setTime_of_validation_token(LocalDateTime.now());
			legal_users.setLegal_validation_token(legal_validation_token);
			return legal_users_repo.save(legal_users);
		}
	}
	
	public LegalDepartmentUsers regenrationOfToken(String legal_department_user_name,String legal_department_user_password){
		
		if(legal_users_repo.countOfLegalDepartmentUserName(legal_department_user_name)<1) {
			throw new RuntimeException("Please Enter Valid Username !!!");
		}else if(legal_users_repo.countOfLegalDepartmentUserPassword(legal_department_user_password)<1) {
			throw new RuntimeException("Please Enter Valid password !!!");
		}else {
			LegalDepartmentUsers legal_department_user=legal_users_repo.getLegalDepartmentUsers(legal_department_user_name,legal_department_user_password);
			legal_department_user.setLegal_validation_token(RandomString.make(30));
			legal_department_user.setTime_of_validation_token(LocalDateTime.now());
			return legal_users_repo.save(legal_department_user);
		}
			
	}
	
	public LegalDepartmentUsers getLegalDepartmentUsersByToken(String legal_validation_token) {
		
		LegalDepartmentUsers legal_department_user=legal_users_repo.getLegalDepartmentUsersByToken(legal_validation_token);
		Duration duration=Duration.between(legal_department_user.getTime_of_validation_token(), LocalDateTime.now());
		if(legal_department_user != null && duration.toHours() <= 8) {
			return legal_users_repo.getLegalDepartmentUsersByToken(legal_validation_token);
		}else {
			throw new RuntimeException("Token Expired");
		}
	}
	
	public void updateLegalDepartmentUsers(String legal_department_user_name,String legal_department_user_password){
		if(legal_users_repo.legalDepartmentUserName(legal_department_user_name).equals(legal_department_user_name)) {
			legal_users_repo.updateLegalDepartmentUsers(legal_department_user_name,legal_department_user_password);
		}else {
			throw new RuntimeException("Username Name Already Exist");
		}
	}
	
	public LegalDepartmentUsers getLegalDepartmentUsers(String legal_department_user_name) {
		return legal_users_repo.getLegalDepartmentUsers(legal_department_user_name);
	}

}
