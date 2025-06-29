package com.au.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.au.model.TokenDetails;


@Repository
public interface TokenDetailsRepository extends JpaRepository<TokenDetails, Integer>{

	
	TokenDetails findByUserId(Integer userId);

	TokenDetails findByAccessToken(String accessToken);
}
