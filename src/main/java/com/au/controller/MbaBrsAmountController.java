package com.au.controller;

import com.au.dto.JwtDetails;
import com.au.model.UniformReceipt;
import com.au.response.ResponseHandler;
import com.au.service.JwtTokenService;
import com.au.service.MbaBrsAmountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Author: Anjan
 * Date: 12-04-2025
 * Description: MbaBrsAmountController class
 */

@RestController
@RequestMapping("/api/${secretkey9}")
@CrossOrigin
@RequiredArgsConstructor
public class MbaBrsAmountController {

    private final MbaBrsAmountService mbaBrsAmountService;

    @PostMapping("/save")
    public ResponseEntity<Object> saveMbaBrsAmount(@RequestParam String transactionDate, @RequestParam Float bankAmount, @RequestParam Float adjustmentAmount,
            @RequestHeader("Authorization") String jwtToken) throws IOException {
        if (RateLimitController.bucket.tryConsume(1)) {
            return mbaBrsAmountService.saveMbaBrsAmount(transactionDate, bankAmount, adjustmentAmount, jwtToken);
        } else {
            return ResponseHandler.generateResponse(true, HttpStatus.TOO_MANY_REQUESTS, ResponseHandler.message1);
        }
    }
}
