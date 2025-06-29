package com.au.service;

import com.au.dto.JwtDetails;
import com.au.model.MbaBrsAmount;
import com.au.repository.FinancialYearRepository;
import com.au.repository.MbaBrsAmountRepository;
import com.au.response.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

/**
 * Author: Anjan
 * Date: 12-04-2025
 * Description: MbaBrsAmountService class
 */
@Service
@RequiredArgsConstructor
public class MbaBrsAmountService {

    private final  MbaBrsAmountRepository mbaBrsAmountRepository;

    private final FinancialYearRepository financialYearRepository;

    private final JwtTokenService jwtTokenService;

    public ResponseEntity<Object> saveMbaBrsAmount(String transactionDate, Float bankAmount, Float adjustmentAmount, String jwtToken) throws IOException {
        MbaBrsAmount mbaBrsAmount = mbaBrsAmountRepository.findByTransactionDate(transactionDate);
        Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentDate(LocalDate.now());
        JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
        if(ObjectUtils.isEmpty(mbaBrsAmount)) {
            mbaBrsAmount = new MbaBrsAmount();
            mbaBrsAmount.setActive(true);
            mbaBrsAmount.setTransactionDate(transactionDate);
            mbaBrsAmount.setCreatedDate(new Date());
            mbaBrsAmount.setFcYearId(fcYearId);
            mbaBrsAmount.setCreatedBy(jwtDetails.getUserId());
        }
        mbaBrsAmount.setBankAmount(bankAmount);
        mbaBrsAmount.setAdjustmentAmount(adjustmentAmount);
        mbaBrsAmount.setModifiedBy(jwtDetails.getUserId());
        mbaBrsAmount.setModifiedDate(new Date());
        try {
            mbaBrsAmountRepository.save(mbaBrsAmount);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseHandler.generateResponse(true, HttpStatus.CREATED, "saved successfully!!!");
    }
}
