package com.au.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.au.dto.SettlementList;
import com.au.dto.SettlementReportProjection;
import com.au.dto.TransactionSettlementDTO;
import com.au.model.BankImportTransaction;
import com.au.model.RazorPaySecretKeys;
import com.au.model.RazorPaySettelments;
import com.au.model.Schools;
import com.au.repository.BankImportTransactionRepository;
import com.au.repository.RazorPaySecretKeyRepository;
import com.au.repository.RazorPaySettlementRepository;
import com.au.repository.School_Repository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.DataInput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RazorPaySettlementService {

    @Autowired
    private RazorPaySecretKeyRepository razorPaySecretKeyRepository;

    @Autowired
    private RazorPaySettlementRepository razorPaySettlementRepository;

    @Autowired
    private School_Repository schoolRepository;

    @Autowired
    private BankImportTransactionRepository bankImportTransactionRepository;

    public void razorPaySettlements(Integer year, Integer month, Integer day, Integer schoolId) {
        try {
            ZoneId zone = ZoneId.of("Asia/Kolkata");
            List<Schools> schools = schoolRepository.findAll();

            for (RazorPaySecretKeys s : razorPaySecretKeyRepository.findAll()) {
                RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository
                        .getRazorPaySecretKeysBySchoolId(s.getSchoolId());
                String url = null;
                if (ObjectUtils.isEmpty(day) || day == 0) {
                    url = "https://api.razorpay.com/v1/settlements/recon/combined?year=" + year + "&month=" + month;
                } else {
                    url = "https://api.razorpay.com/v1/settlements/recon/combined?year=" + year + "&month=" + month + "&day=" + day;
                }

                String username = razorPaySecretKeys.getRazorPayKey();
                String password = razorPaySecretKeys.getSecretKey();
                RestTemplate restTemplate = new RestTemplate();

                // Prepare headers with Basic Authentication
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", createBasicAuthHeader(username, password));

                // Create HttpEntity with headers
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ObjectMapper objectMapper = new ObjectMapper();
                // Make GET request
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

                TransactionSettlementDTO transactionSettlementDTO = objectMapper.readValue(response.getBody(),
                        TransactionSettlementDTO.class);

                if (response.getStatusCode() == HttpStatus.OK) {
//                   List<String> orderIds =  temporaryRazorPayTransactionRepository.getPendingOrderIds();
//                    List<SettlementList> items = transactionSettlementDTO.getItems().stream()
//                            .filter(item -> orderIds.contains(item.getOrderId()))
//                            .collect(Collectors.toList());
//                    transactionSettlementDTO.setItems(items);
                    // paymentCaptureFromRazorpay(transactionSettlementDTO);
                    List<SettlementList> items = new ArrayList<>(transactionSettlementDTO.getItems());
                   // List<SettlementList> items = transactionSettlementDTO.getItems().stream().filter(item -> item.getType().equalsIgnoreCase("payment")).collect(Collectors.toList());
                    transactionSettlementDTO.setItems(items);
                    ModelMapper modelMapper = new ModelMapper();
                    List<String> orderIds = razorPaySettlementRepository.findAllDistictOrderId();
                    List<RazorPaySettelments> settlementLists = transactionSettlementDTO.getItems().stream()
                            .filter(item -> !orderIds.contains(item.getOrderId()))
                            .map(item -> {
                                LocalDate createAt = Instant.ofEpochSecond(item.getCreatedAt())
                                        .atZone(zone)
                                        .toLocalDate();
                                LocalDate settelAt = Instant.ofEpochSecond(item.getSettledAt())
                                        .atZone(zone)
                                        .toLocalDate();
                                RazorPaySettelments settelment =  modelMapper.map(item, RazorPaySettelments.class);
                                System.out.println(createAt);
                                settelment.setCreatedAt(String.valueOf(createAt));
                                settelment.setSettledAt(String.valueOf(settelAt));
                                settelment.setSchoolId(s.getSchoolId());
                                settelment.setInstName(schoolRepository.getSchoolShortName(s.getSchoolId()));
                                settelment.setCredit(item.getCredit()/100);
                                settelment.setDebit(item.getDebit()/100);
                                settelment.setAmount(item.getAmount()/100);
                                return settelment;

                            })
                            .filter(Objects::nonNull) // remove nulls in case of failed parsing
                            .collect(Collectors.toList());

                    razorPaySettlementRepository.saveAll(settlementLists);

                } else {
                    throw new RuntimeException("HTTP error: " + response.getStatusCode());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static String createBasicAuthHeader(String keyId, String keySecret) {
        String auth = keyId + ":" + keySecret;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }

    public List<SettlementReportProjection> getSettelmentSummaryReport(String settledDate) {
        return razorPaySettlementRepository.getSettlementReport(settledDate);
    }

    public ResponseEntity<Object> settelmentBySettlementIdAndDate(String settlementId, String date) {
        List<RazorPaySettelments> settelments = razorPaySettlementRepository.getSettlementsBySettlementIdAndDate(settlementId,date);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, settelments);
    }

    public ResponseEntity<Object> getTransferSettlementsBySettlementIdAndDate(String settlementId, String date) {
        List<RazorPaySettelments> settelments = razorPaySettlementRepository.getTransferSettlementsBySettlementIdAndDate(settlementId,date);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, settelments);
    }

    public ResponseEntity<Object> getPendingSettlements(String settlementId) {
        List<BankImportTransaction> settlements =  bankImportTransactionRepository.getPendingBankImportTransactionBySettlementId(settlementId);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, settlements);
    }

    public ResponseEntity<Object> getReceiptGeneratedBankImportTransactionBySettlementId(String settlementId) {
        List<BankImportTransaction> settlements =  bankImportTransactionRepository.getReceiptGeneratedBankImportTransactionBySettlementId(settlementId);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, settlements);
    }

    public ResponseEntity<Object> allPendingSettlements(String date) {
        List<RazorPaySettelments> settlements =  razorPaySettlementRepository.allPendingSettlements(date);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, settlements);
    }
}
