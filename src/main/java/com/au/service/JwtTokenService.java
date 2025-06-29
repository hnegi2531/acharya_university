package com.au.service;

import java.io.IOException;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.au.dto.JwtAccessTokenDetails;
import com.au.dto.JwtDetails;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JwtTokenService {

public JwtDetails callJwtToken(@RequestHeader("Authorization") String token)
throws JsonParseException, JsonMappingException, IOException {
System.out.println(token);
String payload = token.split("\\.")[1];
//System.out.println("payload"+payload);
byte[] readerPayload = Base64.getDecoder().decode(payload.getBytes());
JwtDetails jwtToken = new ObjectMapper().readValue(readerPayload, new TypeReference<JwtDetails>() {});
System.out.println(jwtToken.getUserName());
System.out.println(jwtToken.getUserId());
return jwtToken;

}



public JwtAccessTokenDetails getDetailsFromAccessToken(String token)
		throws JsonParseException, JsonMappingException, IOException {

	String payload = token.split("\\.")[1];
	//System.out.println("payload"+payload);
	byte[] readerPayload = Base64.getDecoder().decode(payload.getBytes());
	JwtAccessTokenDetails jwtAccessTokenDetails = new ObjectMapper().readValue(readerPayload,new TypeReference<JwtAccessTokenDetails>() {
	 });
	return jwtAccessTokenDetails;
}

}