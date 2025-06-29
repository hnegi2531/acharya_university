package com.au.service;

import com.amazonaws.services.support.model.InternalServerErrorException;
import com.au.dto.JwtDetails;
import com.au.dto.WhatsappDto;
import com.au.model.Whatsapp;
import com.au.repository.UserAuthenticationRepository;
import com.au.repository.WhatsappRepository;
import com.au.response.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

/**
 * Author: Anjan
 * Date: 14-12-2024
 * Description: WhatsappService class
 */

@Service
@RequiredArgsConstructor
public class WhatsappService {

    public static final String NOT_VERIFIED = "NOT-VERIFIED";
    public static final String VERIFIED = "VERIFIED";
    private final WhatsappRepository whatsappRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final UserAuthenticationRepository userAuthenticationRepository;


    @Value("${CHANNEL_ID}")
    private String CHANNEL_ID;
    @Value("${API_KEY}")
    private String API_KEY;
    @Value("${GALLABOX_URL}")
    private String GALLABOX_URL;
    @Value("${API_SECRET}")
    private String API_SECRET;


    public ResponseEntity<Object> saveAndSendOtp(WhatsappDto whatsappDto) {
        
        String phone = whatsappDto.getPhone();

        Whatsapp whatsapp = whatsappRepository.findByPhone(phone);
        if (whatsapp == null)
            whatsapp = modelMapper.map(whatsappDto, Whatsapp.class);

        String otp = 100000 + new Random().nextInt(900000) + "";
        
        // Build the request payload
        String payLoad = "{"
                + "\"channelId\": \"" + CHANNEL_ID + "\","
                + "\"channelType\": \"whatsapp\","
                + "\"recipient\": {"
                + "\"name\": \"" + whatsappDto.getUserName() + "\","
                + "\"phone\": \"" + phone + "\""
                + "},"
                + "\"whatsapp\": {"
                + "\"type\": \"template\","
                + "\"template\": {"
                + "\"templateName\": \"verify_otp_clone\","
                + "\"bodyValues\": {"
                + "\"otp\": \"" + otp + "\""
                + "}"
                + "}"
                + "}"
                + "}";
        HttpStatus httpStatus;
        try {
            httpStatus = sendMessageToWhatsapp(payLoad);
        }catch (Exception e){
            throw new InternalServerErrorException("Unable to send otp to: " + phone);
        }
        if (httpStatus == HttpStatus.ACCEPTED) {
            LocalDateTime reset_password_time_gen = LocalDateTime.now();
            userAuthenticationRepository.updatePasswordResetToken(whatsappDto.getUserId(), reset_password_time_gen, otp);
            whatsapp.setTime(LocalDateTime.now());
            whatsapp.setOtp(otp);
            whatsapp.setStatus(NOT_VERIFIED);
            whatsappRepository.save(whatsapp);
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "otp sent successfully to: " + phone);
        }

        return ResponseHandler.generateResponse(true, HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send otp to: " + phone);
    }

    private HttpStatus sendMessageToWhatsapp(String payLoad) {
        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("apiKey", API_KEY);
        headers.set("apiSecret", API_SECRET);
        headers.setContentType(MediaType.APPLICATION_JSON);


        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(payLoad, headers);

        // Send the request using RestTemplate
        ResponseEntity<String> response = restTemplate.exchange(GALLABOX_URL, HttpMethod.POST, requestEntity, String.class);
        return response.getStatusCode();
    }

    public ResponseEntity<Object> verifyOtp(String phone, String otp) {

        Whatsapp existingWhatsapp = whatsappRepository.findByPhone(phone);

        if (existingWhatsapp == null)
            return ResponseHandler.generateResponse(true, HttpStatus.BAD_REQUEST, "Enter valid mobile number");

        if (existingWhatsapp.getStatus().equals(VERIFIED))
            return ResponseHandler.generateResponse(true, HttpStatus.CONFLICT, "otp already verified.");

        LocalDateTime existingTime = existingWhatsapp.getTime();
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(existingTime, currentTime);

        long minutes = duration.toMinutes();
        if (minutes > 10)
            return ResponseHandler.generateResponse(true, HttpStatus.BAD_REQUEST, "otp expired.");

        if (!existingWhatsapp.getOtp().equals(otp))
            return ResponseHandler.generateResponse(true, HttpStatus.BAD_REQUEST, "Enter valid otp.");

        existingWhatsapp.setStatus(VERIFIED);
        whatsappRepository.save(existingWhatsapp);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, "otp verified successfully!");
    }


    public ResponseEntity<Object> sendMeeting(String jwtToken) {

        String name = "Anjan";
        String phone = "919492569701";
        String date = "Date";
        String timeslot = "TIMESLOT";
        String loc = "Bangalore";
        String prc_name = "Virat";
        String payLoad = "{"
                + "\"channelId\": \"" + CHANNEL_ID + "\","
                + "\"channelType\": \"whatsapp\","
                + "\"recipient\": {"
                + "\"name\": \"" + name + "\","
                + "\"phone\": \"" + phone + "\""
                + "},"
                + "\"whatsapp\": {"
                + "\"type\": \"template\","
                + "\"template\": {"
                + "\"templateName\": \"prc_monthly_meet\","
                + "\"bodyValues\": {"
                + "\"name\": \"" + name + "\","
                + "\"date\": \"" + date + "\","
                + "\"timeslot\": \"" + timeslot + "\","
                + "\"loc\": \"" + loc + "\","
                + "\"prc_name\": \"" + prc_name + "\""
                + "}"
                + "}"
                + "}"
                + "}";

        HttpStatus httpStatus = sendMessageToWhatsapp(payLoad);
        if (httpStatus == HttpStatus.ACCEPTED) {
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "Meeting details sent successfully!");

        }
        return ResponseHandler.generateResponse(true, HttpStatus.BAD_REQUEST, "unable to send Meeting details");

    }
}
