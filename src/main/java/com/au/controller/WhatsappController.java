package com.au.controller;

import com.au.dto.WhatsappDto;
import com.au.response.ResponseHandler;
import com.au.service.WhatsappService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Author: Anjan
 * Date: 14-12-2024
 * Description: WhatsappController class
 */
@RestController
@RequestMapping("/api/${secretKey13}")
@RequiredArgsConstructor
@CrossOrigin
public class WhatsappController {

    private final WhatsappService whatsappService;

    @PostMapping("/sendOtp")
    public ResponseEntity<Object> saveAndSendOtp(@RequestBody @Valid  WhatsappDto whatsappDto){
        if(RateLimitController.bucket.tryConsume(1)) {
            return whatsappService.saveAndSendOtp(whatsappDto);
        }
        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
    }


    @PostMapping("/verifyOtp")
    public ResponseEntity<Object> verifyOtp(@RequestParam String phone, @RequestParam String otp){
        if(RateLimitController.bucket.tryConsume(1)) {
            return whatsappService.verifyOtp(phone, otp);
        }
        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
    }

    @PostMapping("/sendMeeting")
    public ResponseEntity<Object> sendMeeting(@RequestHeader("Authorization") String jwtToken){
        if(RateLimitController.bucket.tryConsume(1)) {
            return whatsappService.sendMeeting(jwtToken);
        }
        return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
    }
}
