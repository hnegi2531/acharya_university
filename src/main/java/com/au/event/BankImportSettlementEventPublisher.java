package com.au.event;

import com.au.dto.SettlementList;
import com.au.dto.StudentDetailsbyEmail;
import com.au.dto.TransactionSettlementDTO;
import com.au.model.*;
import com.au.repository.*;


import com.au.service.FeeReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class BankImportSettlementEventPublisher {

    @Autowired
    private RazorPayTransactionRepository razorPayTransactionRepository;

    @Autowired
    private RazorPayPaymentDetailsRepository razorPayPaymentDetailsRepository;

    @Autowired
    private BankImportTransactionRepository bankImportTransactionRepository;

    @Autowired
    private School_Repository schoolRepository;

    @Autowired
    private ReceiptCounterRepository receiptCounterRepository;

    @Autowired
    private RTGSFeeHistoryRepository rtgsFeeHistoryRepository;

    @Autowired
    private FeeReceiptRepository feeReceiptRepository;

    @Autowired
    private StudentPaymentHistoryRepository studentPaymentHistoryRepository;

    @Autowired
    private FinancialYearRepository financialYearRepository;

    @Autowired
    private StudentDetailsRepository studentDetailsRepository;

    @Autowired
    private BulkFeeReceiptRepository bulkFeeReceiptRepository;

    @Autowired
    private RazorPaySecretKeyRepository razorPaySecretKeyRepository;

    @Autowired
    private BulkTransactionRepository bulkTransactionRepository;

    @Autowired
    private FeeTemplateSubAmountRepository feeTemplateSubAmountRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private RegistrationFeeTrsactionRepository registrationFeeTransactionRepository;

    @Autowired
    private ExamFeeReceiptRepository examFeeReceiptRepository;

    @Autowired
    Academic_year_repository academicYearRepository;

    @Autowired
    TallyReceiptRepository tallyReceiptRepository;

    @Autowired
    VoucherHeadNewRepository voucherHeadNewRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    FeeReceiptService feeReceiptService;

    @Autowired
    ReportingStudentsRepository reportingStudentsRepository;

    private static final Set<String> processingTransactions = ConcurrentHashMap.newKeySet(); // Prevents duplicate processing


    private static final String PAYMENT_GATEWAY = "p_gateway";
    private static final String AUTO_RECEIPT = "auto_receipt";
    private static final String EXAM_FEE = "Exam Fee";
    private static final String COLLEGE_FEE = "College Fees";
    private static final String REGISTRATION_FEE = "Registration Fee";
    private static final String BULK = "Bulk";
    private static final String BULK_FEE = "Bulk Fee";
    private static final String GENERAL = "General";
    private static final String RTGS = "RTGS";
    private static final String INR = "INR";
    private static final String HOSTEL_FEE = "Hostel Fee";
    private static final List<String> RECEIPT_TYPE = new ArrayList<>(Arrays.asList("ADDON", "UNIFORM","ALUMINI"));

    @Async
    @EventListener
    public void handleAttendenceSheetEventPublisher(TransactionSettlementDTO transactionSettlementDTO) {

        try {
            transactionSettlementDTO.getItems().stream().filter(SettlementList::isSettled).forEach(settlement -> {
                if (ObjectUtils.isNotEmpty(settlement) && ObjectUtils.isNotEmpty(settlement.getOrderId()) && ObjectUtils.isNotEmpty(settlement.getSettlementId())) {


                    processTransaction(settlement);

                }
            });

        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private void processTransaction(SettlementList settlement) {
        if (!processingTransactions.add(settlement.getOrderId())) {
          System.out.println("Bank Import Transaction {} is already being processed " +settlement.getOrderId());
            return;
        }

        try {
            System.out.println("Process for Bank Import Transaction: " + settlement.getOrderId());
            generateBankImportTransation(settlement);
        } catch (Exception e) {
            System.out.println("Error in Bank Import Transaction: " +e.getMessage());
        } finally {
            System.out.println("Remove  Bank Import Transaction: " + settlement.getOrderId());
            processingTransactions.remove(settlement.getOrderId());
        }
    }

    @Transactional
    public synchronized  void generateBankImportTransation(SettlementList settlement) {

        //college Fee
        saveBankImportForCollegeFee(settlement);


        //Exam Fee
        saveBankImportForExamFee(settlement);


        // Bulk
        saveBankImportForBulkFee(settlement);

        // Registration Fee
        saveBankImportForRegistration(settlement);

        saveBankImportForHostel(settlement);

    }

    @Transactional
    private void saveBankImportForHostel(SettlementList settlement) {
        RazorPayTransaction razorPayTransaction = getRazorPayTransaction(settlement, HOSTEL_FEE);
       boolean bankImportTransactionExsists  = bankImportTransactionRepository.existsBankImportTransactionByOrderId(settlement.getOrderId());
        if (ObjectUtils.isNotEmpty(razorPayTransaction) && ! bankImportTransactionExsists) {

            Double totalAmount = razorPayPaymentDetailsRepository.getTotalAmountByRazorpayTransactionId(razorPayTransaction.getRazorPayTransactionId());
            if (ObjectUtils.isNotEmpty(totalAmount) && totalAmount > 0.0) {
                // school id
                Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
                //student details

                Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.getStudentId());

//            //RazorPay payment Details
//            List<RazorPayPaymentDetails> razorPayPaymentDetails = getAllRazorPayPaymentDetailsExceptAddOnAndUniform(razorPayTransaction.getRazorPayTransactionId());

                // Bank Import Transaction
                BankImportTransaction bankImportTransaction = getBankImportTransaction(settlement, razorPayTransaction, schoolId, studentDetails);

                System.out.println("Bank import transaction is insert successfully for hostel fee " + bankImportTransaction.getOrder_id());
            }
        }
        else if(bankImportTransactionExsists && !settlement.getSettlementUtr().isEmpty() && ObjectUtils.isNotEmpty(razorPayTransaction))
        {
            BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getByOrderId(settlement.getOrderId());
            bankImportTransaction.setCheque_dd_no(bankImportTransaction.getOrder_id() + "-"
                    + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
            bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
            bankImportTransactionRepository.save(bankImportTransaction);
            System.out.println("Bank import transaction is update successfully for hostel fee "+bankImportTransaction.getOrder_id());
        }
    }

    @Transactional
    public void saveBankImportForRegistration(SettlementList settlement) {

        RegistrationFeeTransaction registrationFeeTransaction = registrationFeeTransactionRepository.findByOrderId(settlement.getOrderId());
        boolean bankImportTransactionExsists  = bankImportTransactionRepository.existsBankImportTransactionByOrderId(settlement.getOrderId());

        if (ObjectUtils.isNotEmpty(registrationFeeTransaction) && !bankImportTransactionExsists && registrationFeeTransaction.getAmount() > 0) {
            BankImportTransaction bankImportTransaction = bankImportTransactionRepository
                    .getByOrderId(settlement.getOrderId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            Student_Details studentDetails = studentDetailsRepository.getStudentByCandidateId(registrationFeeTransaction.getCandidateId());

            if (ObjectUtils.isEmpty(bankImportTransaction)) {
                bankImportTransaction = new BankImportTransaction();
              //  bankImportTransaction.setReceipt_no(generateNextReceiptNumber());


                bankImportTransaction.setSettlement_id(settlement.getSettlementId());
                bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
                bankImportTransaction.setOrder_id(settlement.getOrderId());
                bankImportTransaction.setPaid((double) settlement.getCredit() / 100);
                bankImportTransaction.setActive(Boolean.TRUE);
                bankImportTransaction.setCreated_by(1);
                bankImportTransaction.setCreated_username(AUTO_RECEIPT);
                bankImportTransaction.setFc_year_id(
                        financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());

                bankImportTransaction.setAmount((double) settlement.getCredit() / 100);
                if(ObjectUtils.isNotEmpty(settlement.getSettlementUtr())){
                    bankImportTransaction.setCheque_dd_no(registrationFeeTransaction.getOrderId() + "-"
                            + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
                }else {
                    bankImportTransaction.setCheque_dd_no(registrationFeeTransaction.getOrderId() + "-"
                            + settlement.getSettlementId());
                }
                bankImportTransaction.setTransaction_date(sdf.format(registrationFeeTransaction.getTransactionDate()));
                bankImportTransaction.setTransaction_no(registrationFeeTransaction.getPaymentId());
                bankImportTransaction.setBank_inr_amt(bankImportTransaction.getPaid());

                bankImportTransaction.setBalance(bankImportTransaction.getPaid());
                bankImportTransaction.setReceiptStatus("P");
                bankImportTransaction.setDollor_rate(registrationFeeTransaction.getDollar_value());
                bankImportTransaction.setExachange_rate(String.valueOf(registrationFeeTransaction.getDollar_value()));
                bankImportTransaction.setSchool_id(registrationFeeTransaction.getSchoolId());
                RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository.getRazorPaySecretKeysBySchoolId(registrationFeeTransaction.getSchoolId());
                bankImportTransaction.setDeposited_bank_id(ObjectUtils.isNotEmpty(razorPaySecretKeys)
                        && ObjectUtils.isNotEmpty(razorPaySecretKeys.getBankId())
                        ? razorPaySecretKeys.getBankId()
                        : null);
                if(ObjectUtils.isNotEmpty(studentDetails))
                {
                    bankImportTransaction.setStudent_id(studentDetails.getStudent_id());
                    bankImportTransaction.setAuid(studentDetails.getAuid());
                }

                bankImportTransaction.setTransactionType(REGISTRATION_FEE);
                bankImportTransactionRepository.save(bankImportTransaction);

                registrationFeeTransaction.setSettlementId(bankImportTransaction.getSettlement_id());
                registrationFeeTransaction.setSettlementUTR(bankImportTransaction.getSettlement_utr());
                registrationFeeTransactionRepository.save(registrationFeeTransaction);
                //  registrationFee(registrationFeeTransaction);
                System.out.println("Bank import transaction is insert successfully for registration fee "+bankImportTransaction.getOrder_id());
            }
        }
        else if(bankImportTransactionExsists && !settlement.getSettlementUtr().isEmpty() && ObjectUtils.isNotEmpty(registrationFeeTransaction))
        {
            BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getByOrderId(settlement.getOrderId());
            bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
            bankImportTransaction.setCheque_dd_no(bankImportTransaction.getOrder_id() + "-"
                    + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
            Student_Details studentDetails = studentDetailsRepository.getStudentByCandidateId(registrationFeeTransaction.getCandidateId());
            if(ObjectUtils.isNotEmpty(studentDetails))
            {
                bankImportTransaction.setStudent_id(studentDetails.getStudent_id());
                bankImportTransaction.setAuid(studentDetails.getAuid());
            }
            bankImportTransactionRepository.save(bankImportTransaction);
            registrationFeeTransaction.setSettlementId(bankImportTransaction.getSettlement_id());
            registrationFeeTransaction.setSettlementUTR(bankImportTransaction.getSettlement_utr());
            registrationFeeTransactionRepository.save(registrationFeeTransaction);
            System.out.println("Bank import transaction is insert successfully for registration fee "+bankImportTransaction.getOrder_id());
        }


    }


    @Transactional
    public void saveBankImportForBulkFee(SettlementList settlement) {

        // RazorPay Transaction
        //   RazorPayTransaction razorPayTransaction = getRazorPayTransaction(settlement, BULK);
        BulkTransaction bulkTransaction = bulkTransactionRepository.getBulkTransactionByOrderId(settlement.getOrderId());
        boolean bankImportTransactionExsists  = bankImportTransactionRepository.existsBankImportTransactionByOrderId(settlement.getOrderId());

        if (ObjectUtils.isNotEmpty(bulkTransaction) && !bankImportTransactionExsists && bulkTransaction.getAmount() > 0 && !RECEIPT_TYPE.contains(bulkTransaction.getTransferType().toUpperCase())) {

            // school id
            //   Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
            //student details


            // Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.getStudentId());
            StudentDetailsbyEmail studentDetails = studentDetailsRepository.getStudentByEmail(bulkTransaction.getEmail());

            //RazorPay payment Details

            // Bank Import Transaction
            BankImportTransaction bankImportTransaction = getBankImportTransactionForBulk(settlement, bulkTransaction,studentDetails);
            bulkTransaction.setSettlementId(bankImportTransaction.getSettlement_id());
            bulkTransaction.setSettlementUTR(bankImportTransaction.getSettlement_utr());
            bulkTransactionRepository.save(bulkTransaction);
            System.out.println("Bank import transaction is insert successfully for bulk fee "+bankImportTransaction.getOrder_id());


        }
        else if(bankImportTransactionExsists && !settlement.getSettlementUtr().isEmpty() && ObjectUtils.isNotEmpty(bulkTransaction) && !RECEIPT_TYPE.contains(bulkTransaction.getTransferType().toUpperCase()))
        {
            BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getByOrderId(settlement.getOrderId());
            bankImportTransaction.setCheque_dd_no(bankImportTransaction.getOrder_id() + "-"
                    + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
            bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
            bankImportTransactionRepository.save(bankImportTransaction);
            bulkTransaction.setSettlementId(bankImportTransaction.getSettlement_id());
            bulkTransaction.setSettlementUTR(bankImportTransaction.getSettlement_utr());
            bulkTransactionRepository.save(bulkTransaction);
            System.out.println("Bank import transaction is update successfully for bulk fee "+bankImportTransaction.getOrder_id());
        }
    }



    @Transactional
    public void saveBankImportForExamFee(SettlementList settlement) {
        // RazorPay Transaction
        RazorPayTransaction razorPayTransaction = getRazorPayTransaction(settlement, EXAM_FEE);
       boolean bankImportTransactionExsists  = bankImportTransactionRepository.existsBankImportTransactionByOrderId(settlement.getOrderId());

        if (ObjectUtils.isNotEmpty(razorPayTransaction) && !bankImportTransactionExsists) {

            Double totalAmount = razorPayPaymentDetailsRepository.getTotalAmountByRazorpayTransactionId(razorPayTransaction.getRazorPayTransactionId());
            if (ObjectUtils.isNotEmpty(totalAmount) && totalAmount > 0.0) {
                // school id
                Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
                //student details

                Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.getStudentId());

//            //RazorPay payment Details
//            List<RazorPayPaymentDetails> razorPayPaymentDetails = getAllRazorPayPaymentDetailsExceptAddOnAndUniform(razorPayTransaction.getRazorPayTransactionId());

                // Bank Import Transaction
                BankImportTransaction bankImportTransaction = getBankImportTransaction(settlement, razorPayTransaction, schoolId, studentDetails);
                System.out.println("Bank import transaction is insert successfully for Exam fee " + bankImportTransaction.getOrder_id());

            }
        }
        else if(bankImportTransactionExsists && !settlement.getSettlementUtr().isEmpty() && ObjectUtils.isNotEmpty(razorPayTransaction))
        {
            BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getByOrderId(settlement.getOrderId());
            bankImportTransaction.setCheque_dd_no(bankImportTransaction.getOrder_id() + "-"
                    + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
            bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
            bankImportTransactionRepository.save(bankImportTransaction);
            System.out.println("Bank import transaction is update successfully for exam fee "+bankImportTransaction.getOrder_id());
        }
    }

    @Transactional
    public void saveBankImportForCollegeFee(SettlementList settlement) {
        // RazorPay Transaction
        RazorPayTransaction razorPayTransaction = getRazorPayTransaction(settlement, COLLEGE_FEE);

        boolean bankImportTransactionExsists  = bankImportTransactionRepository.existsBankImportTransactionByOrderId(settlement.getOrderId());

        if (ObjectUtils.isNotEmpty(razorPayTransaction) && ! bankImportTransactionExsists ) {
            Double totalAmount = razorPayPaymentDetailsRepository.getTotalAmountByRazorpayTransactionId(razorPayTransaction.getRazorPayTransactionId());
            if (ObjectUtils.isNotEmpty(totalAmount) && totalAmount > 0.0) {
                // school id
                Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
                //student details

                Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.getStudentId());

//            //RazorPay payment Details
//            List<RazorPayPaymentDetails> razorPayPaymentDetails = getAllRazorPayPaymentDetailsExceptAddOnAndUniform(razorPayTransaction.getRazorPayTransactionId());

                // Bank Import Transaction
                BankImportTransaction bankImportTransaction = getBankImportTransaction(settlement, razorPayTransaction, schoolId, studentDetails);

                System.out.println("Bank import transaction is insert successfully for college fee " + bankImportTransaction.getOrder_id());
            }
        }
        else if(bankImportTransactionExsists && !settlement.getSettlementUtr().isEmpty() && ObjectUtils.isNotEmpty(razorPayTransaction))
        {
            BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getByOrderId(settlement.getOrderId());
            bankImportTransaction.setCheque_dd_no(bankImportTransaction.getOrder_id() + "-"
                    + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
            bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
            bankImportTransactionRepository.save(bankImportTransaction);
            System.out.println("Bank import transaction is update successfully for college fee "+bankImportTransaction.getOrder_id());
        }
    }

    public RazorPayTransaction getRazorPayTransaction(SettlementList settlement, String transactionType) {
        return razorPayTransactionRepository.getByOrderIdAndTransactionType(settlement.getOrderId(), transactionType);
    }

    public List<RazorPayPaymentDetails> getRazorPayPaymentDetailsByReceiptType(RazorPayTransaction razorPayTransaction, String receiptType) {
        return razorPayPaymentDetailsRepository.getByRazorPayTransactionIdAndReceiptType(
                razorPayTransaction.getRazorPayTransactionId(), receiptType);
    }

    @Transactional
    public BankImportTransaction getBankImportTransaction(SettlementList settlement, RazorPayTransaction razorPayTransaction, Integer schoolId, Student_Details studentDetails) {
        BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getByOrderId(settlement.getOrderId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        if (ObjectUtils.isNotEmpty(razorPayTransaction) && ObjectUtils.isEmpty(bankImportTransaction)) {


        RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository.getRazorPaySecretKeysBySchoolId(schoolId);

        //Razor Pay Payment Details for Add On Fee
        List<RazorPayPaymentDetails> razorPayPaymentDetailsForAddOnFee = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, "Add On Fee");

        //Razor Pay Payment Details for uniform Fee
        List<RazorPayPaymentDetails> razorPayPaymentDetailsForUniformFee = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, "Uniform Fee");

        double uniformAmount = (razorPayPaymentDetailsForUniformFee != null) ?
                razorPayPaymentDetailsForUniformFee.stream()
                        .mapToDouble(RazorPayPaymentDetails::getAmount)
                        .sum() : 0.0;

        double addOnAmount = (razorPayPaymentDetailsForUniformFee != null) ? razorPayPaymentDetailsForAddOnFee.stream()
                .mapToDouble(RazorPayPaymentDetails::getAmount).sum() : 0.0;

        double amount = ((double) settlement.getCredit() / 100) - (uniformAmount + addOnAmount);
        double paid = ((double) settlement.getCredit() / 100) - (uniformAmount + addOnAmount);


                bankImportTransaction = new BankImportTransaction();
              //  bankImportTransaction.setReceipt_no(generateNextReceiptNumber());


            bankImportTransaction.setSettlement_id(settlement.getSettlementId());

            bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
            bankImportTransaction.setOrder_id(settlement.getOrderId());
            bankImportTransaction.setPaid(paid);
            bankImportTransaction.setActive(Boolean.TRUE);
            bankImportTransaction.setCreated_by(1);
            bankImportTransaction.setCreated_username(AUTO_RECEIPT);
            bankImportTransaction.setFc_year_id(financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
            bankImportTransaction.setStudent_id(razorPayTransaction.getStudentId());
            bankImportTransaction.setTransaction_remarks(razorPayTransaction.getRemarks());
            bankImportTransaction.setSchool_id(schoolId);
            bankImportTransaction.setAmount(amount);
            bankImportTransaction.setTransactionType(razorPayTransaction.getTransactionType());
            if(ObjectUtils.isNotEmpty(settlement.getSettlementUtr())){
                bankImportTransaction.setCheque_dd_no(razorPayTransaction.getOrderId() + "-"
                        + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
            }else {
                bankImportTransaction.setCheque_dd_no(razorPayTransaction.getOrderId() + "-"
                        + settlement.getSettlementId());
            }
//            bankImportTransaction.setCheque_dd_no(razorPayTransaction.getOrderId() + "-"
//                    + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
            bankImportTransaction.setTransaction_date(sdf.format(razorPayTransaction.getTransactionDate()));
            bankImportTransaction.setTransaction_no(razorPayTransaction.getPaymentId());
            bankImportTransaction.setDeposited_bank_id(ObjectUtils.isNotEmpty(razorPaySecretKeys)
                    && ObjectUtils.isNotEmpty(razorPaySecretKeys.getBankId())
                    ? razorPaySecretKeys.getBankId()
                    : null);
            bankImportTransaction.setBalance(bankImportTransaction.getPaid());
            bankImportTransaction.setBank_inr_amt(amount);
            bankImportTransaction.setAuid(studentDetails.getAuid());
            bankImportTransaction.setReceiptStatus("P");
            bankImportTransaction.setDollor_rate(razorPayTransaction.getDollarValue());
            bankImportTransaction.setExachange_rate(String.valueOf(razorPayTransaction.getDollarValue()));

            return bankImportTransactionRepository.save(bankImportTransaction);
        }

        return null;
    }

    public String generateNextReceiptNumber() {
        ReceiptCounter receiptCounter = receiptCounterRepository.findById(1L).orElse(new ReceiptCounter());

        Integer nextReceiptNumber = receiptCounter.getLastReceiptNumber() == null ? 1
                : receiptCounter.getLastReceiptNumber() + 1;
        receiptCounter.setLastReceiptNumber(nextReceiptNumber);
        receiptCounterRepository.save(receiptCounter);

        return String.format("%05d", nextReceiptNumber);
    }



    public BankImportTransaction getBankImportTransactionForBulk(SettlementList settlement, BulkTransaction bulkTransaction, StudentDetailsbyEmail studentDetails) {
        BankImportTransaction bankImportTransaction = bankImportTransactionRepository
                .getByOrderId(settlement.getOrderId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        if (ObjectUtils.isEmpty(bankImportTransaction) && ObjectUtils.isNotEmpty(bulkTransaction)) {
            bankImportTransaction = new BankImportTransaction();
          //  bankImportTransaction.setReceipt_no(generateNextReceiptNumber());

            bankImportTransaction.setSettlement_id(settlement.getSettlementId());
            bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
            bankImportTransaction.setOrder_id(settlement.getOrderId());
            bankImportTransaction.setPaid((double) settlement.getCredit() / 100);
            bankImportTransaction.setActive(Boolean.TRUE);
            bankImportTransaction.setCreated_by(1);
            bankImportTransaction.setCreated_username(AUTO_RECEIPT);
            bankImportTransaction.setFc_year_id(
                    financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());


            bankImportTransaction.setAmount((double) settlement.getCredit() / 100);
            if(ObjectUtils.isNotEmpty(settlement.getSettlementUtr())){
                bankImportTransaction.setCheque_dd_no(bulkTransaction.getOrderId() + "-"
                        + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
            }else {
                bankImportTransaction.setCheque_dd_no(bulkTransaction.getOrderId() + "-"
                        + settlement.getSettlementId());
            }
//            bankImportTransaction.setCheque_dd_no(bulkTransaction.getOrderId() + "-"
//                    + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
            bankImportTransaction.setTransaction_date(sdf.format(bulkTransaction.getTransactionDate()));
            bankImportTransaction.setTransaction_no(bulkTransaction.getPaymentId());
            bankImportTransaction.setBank_inr_amt(bankImportTransaction.getPaid());
            RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository
                    .getRazorPaySecretKeysBySchoolId(bulkTransaction.getSchoolId());
            bankImportTransaction.setDeposited_bank_id(ObjectUtils.isNotEmpty(razorPaySecretKeys)
                    && ObjectUtils.isNotEmpty(razorPaySecretKeys.getBankId())
                    ? razorPaySecretKeys.getBankId()
                    : null);
            bankImportTransaction.setBalance(bankImportTransaction.getPaid());

            bankImportTransaction.setVoucher_head_new_id(bulkTransaction.getVoucherHeadId());

            bankImportTransaction.setSchool_id(bulkTransaction.getSchoolId());
            bankImportTransaction.setTransaction_remarks((ObjectUtils.isNotEmpty(bulkTransaction.getRemarks())) ? bulkTransaction.getRemarks() : "");
            bankImportTransaction.setTransactionType(bulkTransaction.getTransactionType());
            if (ObjectUtils.isNotEmpty(studentDetails)) {
                bankImportTransaction.setStudent_id(studentDetails.getStudentId());
                bankImportTransaction.setAuid(studentDetails.getAuid());
            }

            bankImportTransaction.setTransactionType(bulkTransaction.getTransactionType().equalsIgnoreCase("HOSB")?bulkTransaction.getTransactionType():BULK);
            bankImportTransaction.setReceiptStatus("P");
            bankImportTransactionRepository.save(bankImportTransaction);
        }
        return bankImportTransaction;
    }



}