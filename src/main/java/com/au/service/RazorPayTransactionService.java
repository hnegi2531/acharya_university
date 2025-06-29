package com.au.service;


import com.au.dto.RazorPayTransactionDetails;

import com.au.dto.TransactionSettlementDTO;
import com.au.model.*;
import com.au.repository.*;
import com.au.response.ResponseHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

import java.time.LocalDate;
import java.util.*;


@Service
public class RazorPayTransactionService {

    @Autowired
    private RazorPayTransactionRepository razorPayTransactionRepository;

    @Autowired
    private BulkTransactionRepository bulkTransactionRepository;

    @Autowired
    private StudentDetailsRepository studentDetailsRepository;

    @Autowired
    private RazorPayPaymentDetailsRepository razorPayPaymentDetailsRepository;

    @Autowired
    private RazorPaySecretKeyRepository razorPaySecretKeyRepository;

    @Autowired
    private TemporaryRazorPayTransactionRepository temporaryRazorPayTransactionRepository;

    @Autowired
    private DollarToInrConversionRepository dollarToInrConversionRepository;

    @Autowired
    private RegistrationFeeTrsactionRepository registrationFeeTrsactionRepository;

    @Autowired
    private TemporaryRazorPayPaymentDetailsRepository temporaryRazorPayPaymentDetailsRepository;

    @Autowired
    private CmaFeeReceiptRepository cmaFeeReceiptRepository;

    @Autowired
    private FinancialYearRepository financialYearRepository;

    @Autowired
    private UniformReceiptRepository uniformReceiptRepository;

    @Autowired
    private PreAdmissionProcessRepository preAdmissionProcessRepository;

    @Autowired
    private CandidateWalkinRepository candidateWalkinRepository;

    @Autowired
    private CandidateWalkInService candidateWalkInService;

    @Autowired
    private UniformTransactionRepository uniformTransactionRepository;

    @Autowired
    private UniformFeeTransactionDetailsRepository uniformFeeTransactionDetailsRepository;

    @Autowired
    private StudentDetailsService studentDetailsService;


    public ResponseEntity<Object> getTransactionDetailsByType(String type, String date ) {

        switch(type)
        {
            case "CollegeFees" :  {
                List<RazorPayTransaction> transactionsList = razorPayTransactionRepository.getRazorPayTransactionByDateAndType(date,"College Fees");
                System.out.println(transactionsList);
                ArrayList<RazorPayTransactionDetails> razorPayTransactionDetailsList = new ArrayList<>();
                for(RazorPayTransaction razorPayTransaction : transactionsList)
                {
                    RazorPayTransactionDetails razorPayTransactionDetails = new RazorPayTransactionDetails();

                    razorPayTransactionDetails.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
                    razorPayTransactionDetails.setReceiptId(razorPayTransaction.getReceiptId());
                    razorPayTransactionDetails.setOrderId(razorPayTransaction.getOrderId());
                    razorPayTransactionDetails.setAmount(razorPayTransaction.getAmount());
                    razorPayTransactionDetails.setStatus(razorPayTransaction.getStatus());
                    razorPayTransactionDetails.setStudentId(razorPayTransaction.getStudentId());
                    razorPayTransactionDetails.setCurrentSem(razorPayTransaction.getCurrentSem());
                    razorPayTransactionDetails.setCurrentYear(razorPayTransaction.getCurrentYear());
                    razorPayTransactionDetails.setAcYearId(razorPayTransaction.getAcYearId());
                    razorPayTransactionDetails.setPaidYear(razorPayTransaction.getPaidYear());
                    razorPayTransactionDetails.setTransactionType(razorPayTransaction.getTransactionType());
                    razorPayTransactionDetails.setPaymentId(razorPayTransaction.getPaymentId());
                    razorPayTransactionDetails.setTransactionId(razorPayTransaction.getTransactionId());
                    razorPayTransactionDetails.setSignature(razorPayTransaction.getSignature());
                    razorPayTransactionDetails.setCode(razorPayTransaction.getCode());
                    razorPayTransactionDetails.setDescription(razorPayTransaction.getDescription());
                    razorPayTransactionDetails.setSource(razorPayTransaction.getSource());
                    razorPayTransactionDetails.setStep(razorPayTransaction.getStep());
                    razorPayTransactionDetails.setReason(razorPayTransaction.getReason());

                    razorPayTransactionDetails.setCreated_date(razorPayTransaction.getCreated_date());
                    razorPayTransactionDetails.setModified_date(razorPayTransaction.getModified_date());
                    razorPayTransactionDetails.setTransactionDate(razorPayTransaction.getTransactionDate());
                    razorPayTransactionDetails.setRemarks(razorPayTransaction.getRemarks());

                    razorPayTransactionDetails.setAuid(studentDetailsRepository.getAuidByStudentId(razorPayTransaction.getStudentId()));

// Setting fee details as a list of RazorPayPaymentDetails
                    razorPayTransactionDetails.setCollegeFee(razorPayPaymentDetailsRepository.getByRazorPayTransactionIdAndReceiptType(
                            razorPayTransaction.getRazorPayTransactionId(), "College Fee"));

                    razorPayTransactionDetails.setAddOnFee(razorPayPaymentDetailsRepository.getByRazorPayTransactionIdAndReceiptType(
                            razorPayTransaction.getRazorPayTransactionId(), "Add On Fee"));
                    razorPayTransactionDetails.setUniformFee(razorPayPaymentDetailsRepository.getByRazorPayTransactionIdAndReceiptType(
                            razorPayTransaction.getRazorPayTransactionId(), "Uniform Fee"));
                    razorPayTransactionDetails.setBulkFee(razorPayPaymentDetailsRepository.getByRazorPayTransactionIdAndReceiptType(
                            razorPayTransaction.getRazorPayTransactionId(), "Bulk Fee"));


                    razorPayTransactionDetailsList.add(razorPayTransactionDetails);
                }

                return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", razorPayTransactionDetailsList);
            }

            case "Bulk": {
                List<BulkTransaction> transactionsList = bulkTransactionRepository.getBulkFeeTransactionDetails(date);
                return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", transactionsList);
            }
            case "Registration" : {
                List<Map<String,Object>> transactionsList = razorPayTransactionRepository.getRegistrationFeeTransactionDetails(date);
                return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", transactionsList);
            }

            case "ExamFees" : {
                List<RazorPayTransaction> transactionsList = razorPayTransactionRepository.getRazorPayTransactionByDateAndType(date,"Exam Fee");
                System.out.println(transactionsList);
                ArrayList<RazorPayTransactionDetails> razorPayTransactionDetailsList = new ArrayList<>();
                for(RazorPayTransaction razorPayTransaction : transactionsList) {
                    RazorPayTransactionDetails razorPayTransactionDetails = new RazorPayTransactionDetails();

                    razorPayTransactionDetails.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
                    razorPayTransactionDetails.setReceiptId(razorPayTransaction.getReceiptId());
                    razorPayTransactionDetails.setOrderId(razorPayTransaction.getOrderId());
                    razorPayTransactionDetails.setAmount(razorPayTransaction.getAmount());
                    razorPayTransactionDetails.setStatus(razorPayTransaction.getStatus());
                    razorPayTransactionDetails.setStudentId(razorPayTransaction.getStudentId());
                    razorPayTransactionDetails.setCurrentSem(razorPayTransaction.getCurrentSem());
                    razorPayTransactionDetails.setCurrentYear(razorPayTransaction.getCurrentYear());
                    razorPayTransactionDetails.setAcYearId(razorPayTransaction.getAcYearId());
                    razorPayTransactionDetails.setPaidYear(razorPayTransaction.getPaidYear());
                    razorPayTransactionDetails.setTransactionType(razorPayTransaction.getTransactionType());
                    razorPayTransactionDetails.setPaymentId(razorPayTransaction.getPaymentId());
                    razorPayTransactionDetails.setTransactionId(razorPayTransaction.getTransactionId());
                    razorPayTransactionDetails.setSignature(razorPayTransaction.getSignature());
                    razorPayTransactionDetails.setCode(razorPayTransaction.getCode());
                    razorPayTransactionDetails.setDescription(razorPayTransaction.getDescription());
                    razorPayTransactionDetails.setSource(razorPayTransaction.getSource());
                    razorPayTransactionDetails.setStep(razorPayTransaction.getStep());
                    razorPayTransactionDetails.setReason(razorPayTransaction.getReason());

                    razorPayTransactionDetails.setCreated_date(razorPayTransaction.getCreated_date());
                    razorPayTransactionDetails.setModified_date(razorPayTransaction.getModified_date());
                    razorPayTransactionDetails.setTransactionDate(razorPayTransaction.getTransactionDate());
                    razorPayTransactionDetails.setRemarks(razorPayTransaction.getRemarks());

                    razorPayTransactionDetails.setAuid(studentDetailsRepository.getAuidByStudentId(razorPayTransaction.getStudentId()));

// Setting fee details as a list of RazorPayPaymentDetails
                    razorPayTransactionDetails.setExamFee(razorPayPaymentDetailsRepository.getByRazorPayTransactionIdAndReceiptType(
                            razorPayTransaction.getRazorPayTransactionId(), "Exam Fee"));




                    razorPayTransactionDetailsList.add(razorPayTransactionDetails);
                }
                return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", razorPayTransactionDetailsList);
            }
        }
      return null;
    }
    private static String createBasicAuthHeader(String keyId, String keySecret) {
        String auth = keyId + ":" + keySecret;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }

    public void transferToRazorPay(Integer year, Integer month, Integer day, Integer schoolId) {
            try {
                String url = null;
                RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository
                        .getRazorPaySecretKeysBySchoolId(schoolId);
                if (ObjectUtils.isEmpty(day) || day == 0) {
                    url = "https://api.razorpay.com/v1/settlements/recon/combined?year=" + year + "&month=" + month;
                }else {
                    url = "https://api.razorpay.com/v1/settlements/recon/combined?year=" + year + "&month=" + month+"&day="+day;
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
                    paymentCaptureFromRazorpay(transactionSettlementDTO);
                } else {
                    throw new RuntimeException("HTTP error: " + response.getStatusCode());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    public ResponseEntity<Object> paymentCaptureFromRazorpay(TransactionSettlementDTO transactionSettlementDTO) {
        try {

            transactionSettlementDTO.getItems().stream().forEach( item ->
                    {
                TemporaryRazorPayTransaction temporaryrazorPayTransaction = temporaryRazorPayTransactionRepository
                        .findByOrderId(item.getOrderId());

                RazorPayTransaction razorPayTransaction = razorPayTransactionRepository.findByOrderId(item.getOrderId());
                if (ObjectUtils.isNotEmpty(temporaryrazorPayTransaction) && ObjectUtils.isEmpty(razorPayTransaction) && !"success".equalsIgnoreCase(temporaryrazorPayTransaction.getStatus())) {
                    razorPayTransaction = new RazorPayTransaction();
                    razorPayTransaction.setPaymentId(item.getEntityId());
                    razorPayTransaction.setStatus("success");
                    razorPayTransaction.setTransactionDate(temporaryrazorPayTransaction.getCreated_date());
                    razorPayTransaction.setOrderId(item.getOrderId());
                    razorPayTransaction.setAcYearId(temporaryrazorPayTransaction.getAcYearId());
                    razorPayTransaction.setAmount(temporaryrazorPayTransaction.getAmount());
                    razorPayTransaction.setCurrentSem(temporaryrazorPayTransaction.getCurrentSem());
                    razorPayTransaction.setCurrentYear(temporaryrazorPayTransaction.getCurrentYear());
                    razorPayTransaction.setTransactionType(temporaryrazorPayTransaction.getTransactionType());
                    razorPayTransaction.setPaidYear(temporaryrazorPayTransaction.getPaidYear());
                    razorPayTransaction.setOrderId(temporaryrazorPayTransaction.getOrderId());
                    razorPayTransaction.setStudentId(temporaryrazorPayTransaction.getStudentId());
                    razorPayTransaction.setReceiptId(temporaryrazorPayTransaction.getReceiptId());
                    if(ObjectUtils.isNotEmpty(razorPayTransaction.getStudentId())) {
                        String currencyType = studentDetailsRepository.getCurrenyTypeByStudentId(razorPayTransaction.getStudentId());
                        DollarToInrConversion dollarToInrConversion = null;
                        try {
                            dollarToInrConversion = calculateDollarRate(currencyType);
                        } catch (ParseException e) {
                            System.out.println("exception in dollar conversion in razopay - "+e.getMessage());
                        }
                        if (ObjectUtils.isNotEmpty(dollarToInrConversion)) {
                            razorPayTransaction.setDollarValue(dollarToInrConversion.getInr());
                        }
                    }
                    razorPayTransactionRepository.save(razorPayTransaction);
                    List<TemporaryRazorPayPaymentDetails> temporaryRazorPayPaymentDetail = temporaryRazorPayPaymentDetailsRepository
                            .getByRazorPayTransactionId(temporaryrazorPayTransaction.getRazorPayTransactionId());
                    List<RazorPayPaymentDetails> razorPayPaymentDetailsList = new ArrayList<>();

                    for (TemporaryRazorPayPaymentDetails tempPay : temporaryRazorPayPaymentDetail) {
                        RazorPayPaymentDetails razorPayPaymentDetails = new RazorPayPaymentDetails();
                        razorPayPaymentDetails.setRazorPayTransactionId(razorPayTransaction.getRazorPayTransactionId());
                        razorPayPaymentDetails.setAmount(tempPay.getAmount());
                        razorPayPaymentDetails.setPaymentType(tempPay.getPaymentType());
                        razorPayPaymentDetails.setReceiptType(tempPay.getReceiptType());
                        razorPayPaymentDetails.setSem(tempPay.getSem());
                        razorPayPaymentDetails.setYear(tempPay.getYear());
                        razorPayPaymentDetails.setVoucherHeadId(tempPay.getVoucherHeadId());
                        razorPayPaymentDetails.setAcYearId(tempPay.getAcYearId());
                        razorPayPaymentDetails.setPaidYear(tempPay.getPaidYear());
                        razorPayPaymentDetailsList.add(razorPayPaymentDetails);

                    }

                    razorPayPaymentDetailsRepository.saveAll(razorPayPaymentDetailsList);
                    List<CmaFeeReceipt> cmaFeeReceipts = cmaFeeReceiptRepository.getCmaFeeReceiptByOrderId(item.getOrderId());
                    Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
                    if(ObjectUtils.isEmpty(cmaFeeReceipts)) {
                        List<RazorPayPaymentDetails> razorPayPaymentDetailsAddOn = razorPayPaymentDetailsRepository
                                .getByRazorPayTransactionIdAndReceiptType(razorPayTransaction.getRazorPayTransactionId(),
                                        "Add On Fee");

                        Integer cmaFeeReceiptNumber = cmaFeeReceiptRepository.getLatestReceitpNumber(financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id()) + 1;
                        for (RazorPayPaymentDetails addOn : razorPayPaymentDetailsAddOn) {
                            CmaFeeReceipt cmaFeeReceipt = new CmaFeeReceipt();
                            cmaFeeReceipt.setActive(Boolean.TRUE);
                            cmaFeeReceipt.setAmount(addOn.getAmount());
                            cmaFeeReceipt.setFinancial_year_id(
                                    financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
                            cmaFeeReceipt.setStudent_id(razorPayTransaction.getStudentId());
                            cmaFeeReceipt.setPaid_year(String.valueOf(addOn.getPaidYear()));
                            cmaFeeReceipt.setReceipt_type(addOn.getReceiptType());
                            cmaFeeReceipt.setSchool_id(schoolId);
                            cmaFeeReceipt
                                    .setCma_receipt_id(org.apache.commons.lang3.ObjectUtils.isNotEmpty(cmaFeeReceiptNumber) ? cmaFeeReceiptNumber : 1);
                            cmaFeeReceipt.setOrderId(item.getOrderId());
                            cmaFeeReceiptRepository.save(cmaFeeReceipt);
                        }
                    }
                    List<UniformReceipt> uniformReceipts = uniformReceiptRepository.getUniformFeeReceiptByOrderId(item.getOrderId());
                    if(ObjectUtils.isEmpty(uniformReceipts)) {
                        List<RazorPayPaymentDetails> razorPayPaymentDetailsUniform = razorPayPaymentDetailsRepository
                                .getByRazorPayTransactionIdAndReceiptType(razorPayTransaction.getRazorPayTransactionId(),
                                        "Uniform Fee");
                        Integer uniformRecieptNumber = uniformReceiptRepository.getLatestReceitpNumber() + 1;
                        for (RazorPayPaymentDetails uniform : razorPayPaymentDetailsUniform) {
                            UniformReceipt uniformFeeReceipt = new UniformReceipt();
                            uniformFeeReceipt.setActive(Boolean.TRUE);
                            uniformFeeReceipt.setAmount(uniform.getAmount());
                            uniformFeeReceipt.setFcYearId(financialYearRepository.getFinancialYearIdOnCurrentYear()
                                    .getFinancial_year_id().longValue());
                            uniformFeeReceipt.setStudentId(razorPayTransaction.getStudentId().longValue());
                            uniformFeeReceipt.setSem(uniform.getSem());
                            uniformFeeReceipt.setYear(uniform.getYear());
                            uniformFeeReceipt.setType("package");
                            uniformFeeReceipt.setSchoolId(schoolId);
                            uniformFeeReceipt.setOrderId(razorPayTransaction.getOrderId());
                            uniformFeeReceipt.setPaidYear(uniform.getPaidYear());
                            uniformFeeReceipt.setUniformReceiptNo(ObjectUtils.isNotEmpty(uniformRecieptNumber) ? uniformRecieptNumber : 1);
                            uniformReceiptRepository.save(uniformFeeReceipt);
                        }
                    }

                }

                RegistrationFeeTransaction registrationPayTransaction = registrationFeeTrsactionRepository
                        .findByOrderId(item.getOrderId());

                if (ObjectUtils.isNotEmpty(registrationPayTransaction)
                      && !"success".equalsIgnoreCase(registrationPayTransaction.getStatus())) {

                    registrationPayTransaction.setPaymentId(item.getPaymentId());

                    registrationPayTransaction.setStatus("success");
                    LocalDate date  = LocalDate.from(Instant.ofEpochSecond(item.getCreatedAt()));
                    registrationPayTransaction.setTransactionDate(registrationPayTransaction.getCreated_date());
                    String currencyType = preAdmissionProcessRepository.getCurrencyTypeByCandidateId(registrationPayTransaction.getCandidateId());

                    DollarToInrConversion dollarToInrConversion = null;
                    try {
                        dollarToInrConversion = calculateDollarRate(currencyType);
                    } catch (ParseException e) {
                        System.out.println("exception in dollar conversion in registration - "+ e.getMessage());
                    }
                    if(ObjectUtils.isNotEmpty(dollarToInrConversion))
                    {
                        registrationPayTransaction.setDollar_value(dollarToInrConversion.getInr());
                    }
                    registrationFeeTrsactionRepository.save(registrationPayTransaction);

                    Candidate_Walkin candidate_Walkin = candidateWalkinRepository
                            .getByCandidateId(registrationPayTransaction.getCandidateId());
                    candidate_Walkin.setNpf_status(4);
                    candidateWalkinRepository.save(candidate_Walkin);
                    candidateWalkInService.updateLsqStatus(candidate_Walkin.getCandidate_id(), 5);
                    callLeadSquareApiforUpdatePaymentSuccess(candidate_Walkin, registrationPayTransaction);
                }

                UniformTransaction uniformPayTransaction = uniformTransactionRepository.findByOrderId(item.getOrderId());

                if (ObjectUtils.isNotEmpty(uniformPayTransaction)
                       && !"success".equalsIgnoreCase(uniformPayTransaction.getStatus())) {

                    uniformPayTransaction.setPaymentId(item.getPaymentId());

                    uniformPayTransaction.setStatus("success");
                    uniformPayTransaction.setTransactionDate(uniformPayTransaction.getCreated_date());
                    uniformTransactionRepository.save(uniformPayTransaction);

                    studentDetailsService.generateUniFormReceipt(item.getOrderId());

                }

                BulkTransaction bulkPayTransaction = bulkTransactionRepository.findByOrderId(item.getOrderId());

                if (ObjectUtils.isNotEmpty(bulkPayTransaction)  && !"success".equalsIgnoreCase(bulkPayTransaction.getStatus())) {

                    bulkPayTransaction.setPaymentId(item.getPaymentId());

                    bulkPayTransaction.setStatus("success");
                    bulkPayTransaction.setTransactionDate(bulkPayTransaction.getCreated_date());
                    bulkTransactionRepository.save(bulkPayTransaction);

                }
            });

            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }

    private DollarToInrConversion calculateDollarRate(String currencyType) throws ParseException {


        if(StringUtils.isNotEmpty(currencyType) && !StringUtils.equals(currencyType, "INR"))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(new Date());
            Date newDate = dateFormat.parse(formattedDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(newDate);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);

            return dollarToInrConversionRepository.findByDateAndActive(month,
                    year);
        }
        return null;
    }

    public void callLeadSquareApiforUpdatePaymentSuccess(Candidate_Walkin candidateWalkin,
                                                         RegistrationFeeTransaction razorPayTransaction) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String apiUrl = "https://api-in21.leadsquared.com/v2/ProspectActivity.svc/Create?accessKey=u$rf0116cbfffa617de778de29050a8565a&secretKey=871be21cf012083516bbf13555f20cbaa3b2091a";

        RestTemplate restTemplate = new RestTemplate();
        JSONObject payload = new JSONObject();
        payload.put("RelatedProspectId", candidateWalkin.getLead_id());
        payload.put("RelatedOpportunityId", candidateWalkin.getOpportunity_id());
        payload.put("ActivityEvent", razorPayTransaction.getRegistrationFeeTransactionId());
        payload.put("ActivityNote", "Registration fee");
        payload.put("ActivityDateTime", sdf.format(razorPayTransaction.getTransactionDate()));

        JSONArray fields = new JSONArray();
        fields.put(new JSONObject().put("SchemaName", "mx_Custom_1").put("Value", razorPayTransaction.getAmount()));
        fields.put(new JSONObject().put("SchemaName", "mx_Custom_2").put("Value", razorPayTransaction.getPaymentId()));
        fields.put(new JSONObject().put("SchemaName", "mx_Custom_3").put("Value", razorPayTransaction.getReceiptId()));
        payload.put("Fields", fields);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create HTTP request
        HttpEntity<String> request = new HttpEntity<>(payload.toString(), headers);

        try {

            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        } catch (Exception e) {
            System.out.println("Exception in Lead square api" + e.getMessage());
        }

    }


}
