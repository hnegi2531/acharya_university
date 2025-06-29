package com.au.event;

import com.au.dto.HostelBedAssignmentRequest;
import com.au.dto.StudentDetailsbyEmail;
import com.au.exception.ResourceNotFoundException;
import com.au.model.*;
import com.au.repository.*;
import com.au.service.AcademicYearService;
import com.au.service.FeeReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GenerateFeeReceiptEventPublisher {


    @Autowired
    private StudentDetailsRepository studentDetailsRepository;

    @Autowired
    private RazorPayTransactionRepository razorPayTransactionRepository;

    @Autowired
    private FeeReceiptRepository feeReceiptRepository;

    @Autowired
   private FinancialYearRepository financialYearRepository;

    @Autowired
    private RegistrationFeeTrsactionRepository registrationFeeTransactionRepository;

    @Autowired
    private BankImportTransactionRepository bankImportTransactionRepository;

    @Autowired
    private StudentPaymentHistoryRepository studentPaymentHistoryRepository;

    @Autowired
    private TallyReceiptRepository tallyReceiptRepository;

    @Autowired
    private FeeReceiptService feeReceiptService;

    @Autowired
    private FeeTemplateSubAmountRepository feeTemplateSubAmountRepository;

    @Autowired
    private School_Repository schoolRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private VoucherHeadNewRepository voucherHeadNewRepository;

    @Autowired
    private RTGSFeeHistoryRepository rtgsFeeHistoryRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private RazorPayPaymentDetailsRepository razorPayPaymentDetailsRepository;

    @Autowired
    private BulkFeeReceiptRepository bulkFeeReceiptRepository;

    @Autowired
    private Academic_year_repository academicYearRepository;

    @Autowired
    private ExamFeeReceiptRepository examFeeReceiptRepository;

    @Autowired
    private BulkTransactionRepository bulkTransactionRepository;

    @Autowired
    private RazorPaySecretKeyRepository razorPaySecretKeyRepository;

    @Autowired
    private ReportingStudentsRepository reportingStudentsRepository;

    @Autowired
    private FeePaymentWindowRepository feePaymentWindowRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private HostelFeeReceiptVoucherHeadWiseRepository hostelFeeReceiptVoucherHeadWiseRepository;

    @Autowired
    private HostelBedAssignmentRepository hostelBedAssignmentRepository;

    @Autowired
    private HostelDueRepository hostelDueRepository;

    @Autowired
    private AcademicYearService academicYearService;

    @Autowired
    private HostelBulkFeeReceiptVocherHeadWiseRepository hostelBulkFeeReceiptVocherHeadWiseRepository;

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
    private static final Set<String> processingTransactions = ConcurrentHashMap.newKeySet(); // Prevents duplicate processing
    private static final String HOS = "HOS" ;
    private static final String HOSB = "HOSB" ;
    private static final List<String> RECEIPT_TYPE = new ArrayList<>(Arrays.asList("ADDON", "UNIFORM","ALUMINI","HOSB"));

//    @EventListener
//    public void generateFeeReceiptEvent(GenerateFeeReceiptEvent generateFeeReceiptEvent) {
//        generateFeeReceiptEvent.getBankImportTransactionList().stream()
//                .filter(this::isValidTransaction) // Filter non-empty UTR
//                .forEach(this::processTransaction); // Process transaction
//
//            bankImportTransactionRepository.updateBankImportInprogressToPending();
//
//    }
@EventListener
public void generateFeeReceiptEvent(GenerateFeeReceiptEvent generateFeeReceiptEvent) {
    List<CompletableFuture<Void>> futures = generateFeeReceiptEvent.getBankImportTransactionList().stream()
            .filter(this::isValidTransaction)
            .map(this::processTransaction)  // Store CompletableFuture for each transaction
            .collect(Collectors.toList());

    // Wait for all async tasks to complete before updating status
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRunAsync(this::updateStatus);


}

    private boolean isValidTransaction(BankImportTransaction transaction) {
        return transaction != null &&
                transaction.getSettlement_utr() != null &&
                !transaction.getSettlement_utr().isEmpty();
    }


    @Async
    public CompletableFuture<Void> processTransaction(BankImportTransaction bankImportTransaction) {
        if (!processingTransactions.add(bankImportTransaction.getOrder_id())) {
            log.info("Transaction {} is already being processed", bankImportTransaction.getBank_import_transaction_id());
            return CompletableFuture.completedFuture(null);
        }

        try {
            System.out.println("start process for fee receipt for bank import order id "+bankImportTransaction.getOrder_id());
            generateFeeReceipts(bankImportTransaction);
        } catch (Exception e) {
            System.out.println("Error generating fee receipt: "+e.getMessage());
        } finally {
            processingTransactions.remove(bankImportTransaction.getOrder_id());
            System.out.println("remove order id "+bankImportTransaction.getOrder_id());
        }

        return CompletableFuture.completedFuture(null);
    }

    public  void generateFeeReceipts(BankImportTransaction bankImportTransaction) {
        if (!"P".equals(bankImportTransaction.getReceiptStatus())) {
            System.out.println("Skipping transaction  as it is already processed "+bankImportTransaction.getReceiptStatus());
            return;
        }

        // Mark as 'Processing' to avoid duplicate execution
        bankImportTransaction.setReceiptStatus("I"); // 'I' for In-Progress
        System.out.println("Processing transaction "+ bankImportTransaction.getReceiptStatus());
        transactionTemplate.execute(status -> {
            try{
                    // Process different fee receipts
                    saveCollegeFee(bankImportTransaction);
                    saveExamFee(bankImportTransaction);
                    saveBulkFee(bankImportTransaction);
                    saveRegistration(bankImportTransaction);
                    saveHostelFee(bankImportTransaction);

        } catch (Exception e) {
                // Rollback manually
                System.out.println("Error generating fee receipt: "+e.getMessage());
                status.setRollbackOnly();

            }
            return null;

        });
//
//        // Mark as completed
//        bankImportTransaction.setStatus("C"); // 'C' for Completed
//        log.info("Completed transaction {}", bankImportTransaction.getId());
    }

    @Transactional
    private void saveHostelFee(BankImportTransaction bankImportTransaction) {
        // RazorPay Transaction
        RazorPayTransaction razorPayTransaction = getRazorPayTransaction(bankImportTransaction.getOrder_id(), HOSTEL_FEE);
        boolean isFeeReceiptExists = feeReceiptRepository.existsByBankTransactionHistoryId(bankImportTransaction.getOrder_id());
        String currenyType = studentDetailsRepository.getCurrenyTypeByStudentId(bankImportTransaction.getStudent_id());
        if (ObjectUtils.isNotEmpty(razorPayTransaction) && !isFeeReceiptExists && (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR"))) {
            Double totalAmount = razorPayPaymentDetailsRepository.getTotalAmountByRazorpayTransactionId(razorPayTransaction.getRazorPayTransactionId());
            if (Objects.equals(bankImportTransaction.getAmount(), totalAmount)) {

                System.out.println("start process for college fee bank import id - "+ bankImportTransaction.getBank_import_transaction_id()+" order id "+bankImportTransaction.getOrder_id());
                // school id
                Integer schoolId = studentDetailsRepository.getStudentSchoolId(bankImportTransaction.getStudent_id());
                //student details

                Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.getStudentId());

                //RazorPay payment Details
//                List<RazorPayPaymentDetails> razorPayPaymentDetails = getAllRazorPayPaymentDetailsExceptAddOnAndUniform(razorPayTransaction.getRazorPayTransactionId());

                // Bank Import Transaction
//            BankImportTransaction bankImportTransaction = getBankImportTransaction(settlement, razorPayTransaction, schoolId, studentDetails);

                //Fee Receipt
            //    List<FeeReceipt> feeReceipts = saveAllReceiptsInFeeReceipt(razorPayTransaction, bankImportTransaction, schoolId, studentDetails);
                List<RazorPayPaymentDetails> razorPayHostelPaymentDetails = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, HOSTEL_FEE);
                if (ObjectUtils.isNotEmpty(razorPayHostelPaymentDetails)) {

                    List<FeeReceipt> feeReceipts = saveFeeReceiptForHostel(bankImportTransaction, razorPayTransaction, HOSTEL_FEE,razorPayHostelPaymentDetails);

                    List<HostelFeeReceiptVoucherHeadWise> hostelFeeReceiptVoucherHeadWises = saveHostelFeeReceiptHeadWise(razorPayTransaction, schoolId, feeReceipts, razorPayHostelPaymentDetails);



                    saveTallyReceiptForHostel(hostelFeeReceiptVoucherHeadWises, studentDetails, bankImportTransaction);


                    saveRTGSReceiptForHostel(feeReceipts,bankImportTransaction,schoolId,HOS);
                    BankImportTransaction updatedBankImportTransaction = bankImportTransactionRepository.getByOrderId(bankImportTransaction.getOrder_id());
                    updatedBankImportTransaction.setReceiptStatus("S");
                    bankImportTransactionRepository.save(updatedBankImportTransaction);
                    List<Integer> acYears = razorPayHostelPaymentDetails.stream()
                            .map(RazorPayPaymentDetails::getAcYearId)
                            .filter(Objects::nonNull)
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList());
                    for(Integer acYear : acYears) {
                        hostelDueRepository.hostelDueProcedureByStudentIdAndAcYearId(studentDetails.getStudent_id(), acYear);
                        // Student_Details studentDetails = studentDetailsService.get(frd.getFee_rec().getStudent_id());
                        Academic_year academicYear = academicYearService.get(acYear);
                        feeReceiptService.updateHostelBedStatusForStudentAndAcademicYear(studentDetails, academicYear);
                    }

                    System.out.println("Insert fee receipt for Hostel fee order id " + bankImportTransaction.getOrder_id());
                    //student due event

                }
            }
        } else {
            if (ObjectUtils.isNotEmpty(razorPayTransaction) && isFeeReceiptExists) {
                bankImportTransaction.setReceiptStatus("D");
                bankImportTransactionRepository.save(bankImportTransaction);
                System.out.println("duplicate bank import order id  "+bankImportTransaction.getOrder_id());
            }
        }
        log.info("Successfully processed College Fee Receipt for Order ID: {}", bankImportTransaction.getOrder_id());


    }

    @Transactional
    private void saveRTGSReceiptForHostel(List<FeeReceipt> feeReceipts, BankImportTransaction bankImportTransaction, Integer schoolId, String receiptType) {
        List<RTGSFeeHistory> rtgsFeeHistories = rtgsFeeHistoryRepository
                .getRtgsFeeHistories(bankImportTransaction.getBank_import_transaction_id(),receiptType);

        if (ObjectUtils.isEmpty(rtgsFeeHistories)) {
            double balance = bankImportTransaction.getPaid();
            for(FeeReceipt feeReceipt : feeReceipts){

                RTGSFeeHistory rtgsFeeHistory = new RTGSFeeHistory();
                balance = balance - feeReceipt.getPaid_amount();
                rtgsFeeHistory.setActive(Boolean.TRUE);
                rtgsFeeHistory.setBank_transaction_history_id(
                        bankImportTransaction.getBank_import_transaction_id());
                rtgsFeeHistory.setPaid(Double.valueOf(feeReceipt.getPaid_amount()));
                rtgsFeeHistory.setSchool_id(schoolId);
                rtgsFeeHistory.setStudent_id(bankImportTransaction.getStudent_id());
                rtgsFeeHistory.setRtgs_balance_amount(balance);
                rtgsFeeHistory.setRtgs_net_amount(Double.valueOf(feeReceipt.getPaid_amount()));
                rtgsFeeHistory.setYear(reportingStudentsRepository.getYear(bankImportTransaction.getStudent_id()));
                rtgsFeeHistory.setSem(reportingStudentsRepository.getSem(bankImportTransaction.getStudent_id()));
                rtgsFeeHistory.setFc_year_id(financialYearRepository.getFinancialYearIdOnCurrentYear()
                        .getFinancial_year_id());

                rtgsFeeHistory.setReceipt_no(feeReceipt.getFee_receipt());
                rtgsFeeHistory.setReceipt_type(feeReceipt.getReceipt_type());

                rtgsFeeHistory.setCreated_by(1);
                rtgsFeeHistory.setCreated_username(AUTO_RECEIPT);
                rtgsFeeHistories.add(rtgsFeeHistory);
            }


            rtgsFeeHistoryRepository.saveAll(rtgsFeeHistories);
        }
    }

    @Transactional
    private void saveTallyReceiptForHostel(List<HostelFeeReceiptVoucherHeadWise> hostelFeeReceiptVoucherHeadWises, Student_Details studentDetails, BankImportTransaction bankImportTransaction) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //Bank

        Bank depositeBank = bankRepository.findByBankID(bankImportTransaction.getDeposited_bank_id());
        //FinancialYear financialYear = financialYearRepository.getFinancialYearByFinancialYearId(bankImportTransaction.getFc_year_id());
        FinancialYear financialYear = financialYearRepository.getFinancialYearIdOnCurrentYear();
        Schools school = schoolRepository.getSchoolBySchoolId(studentDetails.getSchool_id());


        if (ObjectUtils.isNotEmpty(hostelFeeReceiptVoucherHeadWises)) {
            for (HostelFeeReceiptVoucherHeadWise receipt : hostelFeeReceiptVoucherHeadWises) {
                List<TallyReceipt> tallyReceipt = tallyReceiptRepository.findByFeeReceiptID(receipt.getFeeReceipt().getFee_receipt_id());
                if (ObjectUtils.isEmpty(tallyReceipt)) {
                    TallyReceipt tallyReceipt1 = new TallyReceipt();
                    String voucherHead = voucherHeadNewRepository.getVoucherName(receipt.getHostelFeeTemplate().getFee_head_id());

                    tallyReceipt1.setFee_receipt(receipt.getFeeReceipt().getFee_receipt());
                    tallyReceipt1.setFee_receipt_id(receipt.getFeeReceipt().getFee_receipt_id());
                    tallyReceipt1.setAuid(studentDetails.getAuid());

                    tallyReceipt1.setRemarks(RTGS);

                    tallyReceipt1.setDeposited_bank(depositeBank.getBank_name());
                    tallyReceipt1.setParticulars(voucherHead);
                    tallyReceipt1.setCreated_username(AUTO_RECEIPT);
                    tallyReceipt1.setCreated_by(1);
                    tallyReceipt1.setFinancial_year(financialYear.getFinancial_year());
                    tallyReceipt1.setCreated_date(receipt.getFeeReceipt().getCreated_date());

                    tallyReceipt1.setReceived_from(receipt.getFeeReceipt().getTransaction_type());
                    tallyReceipt1.setReceived_in(INR);
                    tallyReceipt1.setReceived_type(receipt.getFeeReceipt().getReceipt_type());
                    tallyReceipt1.setSchool_name(school.getSchool_name_short());
                    tallyReceipt1.setStudent_id(studentDetails.getStudent_id());
                    tallyReceipt1.setStudent_name(studentDetails.getStudent_name());
                    tallyReceipt1.setTotal(Double.valueOf(receipt.getPayingAmount()));
                    tallyReceipt1.setTransaction_date(bankImportTransaction.getTransaction_date());
                    tallyReceipt1.setTotal_amount(bankImportTransaction.getAmount());
                    tallyReceipt1.setTransaction_no(bankImportTransaction.getTransaction_no());
                    tallyReceipt1.setTransaction_type(receipt.getFeeReceipt().getTransaction_type());
                    tallyReceipt1.setUsn(studentDetails.getUsn());
                    tallyReceipt1.setActive(Boolean.TRUE);
                    if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK") && tallyReceipt1.getSchool_name().equalsIgnoreCase("ASN"))
                    {
                        tallyReceipt1.setBank_institute("ANR");
                    } else{
                        if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK CMS"))
                        {
                            tallyReceipt1.setBank_institute("JMJ");
                        }
                        else{
                            tallyReceipt1.setBank_institute(tallyReceipt1.getSchool_name());
                        }
                    }

                    tallyReceiptRepository.save(tallyReceipt1);
                }

            }

        }
    }


    @Transactional
    public void saveRegistration(BankImportTransaction bankImportTransaction) {

            RegistrationFeeTransaction registrationFeeTransaction = registrationFeeTransactionRepository.findByOrderId(bankImportTransaction.getOrder_id());
            boolean isFeeReceiptExists = feeReceiptRepository.existsByBankTransactionHistoryId(bankImportTransaction.getOrder_id());
            if (ObjectUtils.isNotEmpty(registrationFeeTransaction) && !isFeeReceiptExists) {
                if (bankImportTransaction.getAmount().floatValue() == registrationFeeTransaction.getAmount()) {
                    System.out.println("start process for registration fee bank import id - "+ bankImportTransaction.getBank_import_transaction_id()+" order id "+bankImportTransaction.getOrder_id());

                    registrationFee(registrationFeeTransaction);

                }
            } else {
                if (ObjectUtils.isNotEmpty(registrationFeeTransaction) && isFeeReceiptExists) {
                    bankImportTransaction.setReceiptStatus("D");
                    bankImportTransactionRepository.save(bankImportTransaction);

                    System.out.println("duplicate bank import order id "+bankImportTransaction.getOrder_id());
                }
            }
            log.info("Successfully processed Registration Fee Receipt for Order ID: {}", bankImportTransaction.getOrder_id());




    }

    @Transactional
    public void saveBulkFee(BankImportTransaction bankImportTransaction) {

           // RazorPay Transaction
           //   RazorPayTransaction razorPayTransaction = getRazorPayTransaction(settlement, BULK);
           BulkTransaction bulkTransaction = bulkTransactionRepository.getBulkTransactionByOrderId(bankImportTransaction.getOrder_id());
           boolean isFeeReceiptExists = feeReceiptRepository.existsByBankTransactionHistoryId(bankImportTransaction.getOrder_id());
           if (ObjectUtils.isNotEmpty(bulkTransaction) && !isFeeReceiptExists && !bulkTransaction.getTransferType().equalsIgnoreCase(HOSB) && !bankImportTransaction.getTransactionType().equalsIgnoreCase(HOSB)) {
               if (bankImportTransaction.getAmount().floatValue() == bulkTransaction.getAmount()) {

                   System.out.println("start process for Bulk fee bank import id - "+ bankImportTransaction.getBank_import_transaction_id()+" order id "+bankImportTransaction.getOrder_id());

                   // school id
                   //   Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
                   //student details


                   // Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.getStudentId());
                   StudentDetailsbyEmail studentDetails = studentDetailsRepository.getStudentByEmail(bulkTransaction.getEmail());


                   //Fee Receipt

                   List<FeeReceipt> feeReceipts = saveBulkTypeFeeReceipt(bankImportTransaction, bulkTransaction);

                   //Bulk

                   saveBulkFeeReceiptForBulk(bankImportTransaction, bulkTransaction, feeReceipts);

//                   if(bulkTransaction.getTransferType().equalsIgnoreCase(HOSB))
//                   {
//
//                       List<HostelBulkFeeReceiptVocherHeadWise> hostelBulkFeeReceiptVoucherHeadWises = saveHostelBulkFeeReceiptVoucherHeadWise(bulkTransaction,feeReceipts,studentDetails);
//                     //  Student_Details student = studentDetailsRepository.getStudentByStudentId(studentDetails.getStudentId());
//                       saveTallyReceiptForHostelBulk(hostelBulkFeeReceiptVoucherHeadWises, bulkTransaction, bankImportTransaction);
//                       saveRTGSReceiptForHostel(feeReceipts,bankImportTransaction,bulkTransaction.getSchoolId(),HOSB);
//
//                   }
//                   else{


                   //tally


                   saveTallyReceiptForBulk(feeReceipts, bankImportTransaction, studentDetails, bulkTransaction);

                   //RTGS

                   saveRTGSReceiptForBulk(bankImportTransaction, bulkTransaction,BULK);
                //   }

                   BankImportTransaction updatedBankImportTransaction = bankImportTransactionRepository.getByOrderId(bankImportTransaction.getOrder_id());
                   updatedBankImportTransaction.setReceiptStatus("S");
                   bankImportTransactionRepository.save(updatedBankImportTransaction);
                   System.out.println("Insert fee receipt for Bulk fee order id "+bankImportTransaction.getOrder_id());

//            //student due event
//            StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, bankImportTransaction.getStudent_id(), null);
//            applicationEventPublisher.publishEvent(studentDueEvent);

               }
           } else {
               if (ObjectUtils.isNotEmpty(bulkTransaction) && isFeeReceiptExists) {
                   bankImportTransaction.setReceiptStatus("D");
                   bankImportTransactionRepository.save(bankImportTransaction);

                   System.out.println("duplicate bank import order id "+bankImportTransaction.getOrder_id());
               }
           }
           log.info("Successfully processed Bulk Fee Receipt for Order ID: {}", bankImportTransaction.getOrder_id());



    }




    @Transactional
    public void saveExamFee(BankImportTransaction bankImportTransaction) {

         // RazorPay Transaction
         RazorPayTransaction razorPayTransaction = getRazorPayTransaction(bankImportTransaction.getOrder_id(), EXAM_FEE);
         boolean isFeeReceiptExists = feeReceiptRepository.existsByBankTransactionHistoryId(bankImportTransaction.getOrder_id());
         if (ObjectUtils.isNotEmpty(razorPayTransaction) && !isFeeReceiptExists) {
             Double totalAmount = razorPayPaymentDetailsRepository.getTotalAmountByRazorpayTransactionId(razorPayTransaction.getRazorPayTransactionId());
             if (Objects.equals(bankImportTransaction.getAmount(), totalAmount)) {
                 System.out.println("start process for Exam fee bank import id - "+ bankImportTransaction.getBank_import_transaction_id()+" order id "+bankImportTransaction.getOrder_id());

                 // school id
                 Integer schoolId = studentDetailsRepository.getStudentSchoolId(razorPayTransaction.getStudentId());
                 //student details

                 Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.getStudentId());

                 //RazorPay payment Details
                 List<RazorPayPaymentDetails> razorPayPaymentDetails = getAllRazorPayPaymentDetailsExceptAddOnAndUniform(razorPayTransaction.getRazorPayTransactionId());

//            // Bank Import Transaction
//            BankImportTransaction bankImportTransaction = getBankImportTransaction(settlement, razorPayTransaction, schoolId, studentDetails);

                 //Fee Receipt
                 FeeReceipt feeReceipt = saveAllReceiptsInFeeReceipt(razorPayTransaction, bankImportTransaction, schoolId, studentDetails);

                 Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id();
                 List<ExamFeeReceipt> examFeeReceipts = examFeeReceiptRepository.findByFeeReceiptId(feeReceipt.getFee_receipt_id());
                 //tally
                 saveTallyReceiptForExam(examFeeReceipts, studentDetails, bankImportTransaction, feeReceipt.getFee_receipt_id());

                 //RTGS
                 saveRTGSReceipt(feeReceipt, bankImportTransaction, schoolId);

                 BankImportTransaction updatedBankImportTransaction = bankImportTransactionRepository.getByOrderId(bankImportTransaction.getOrder_id());
                 updatedBankImportTransaction.setReceiptStatus("S");
                 bankImportTransactionRepository.save(updatedBankImportTransaction);
                 System.out.println("Insert fee receipt for exam fee order id "+bankImportTransaction.getOrder_id());
//            //student due event
//            StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, razorPayTransaction.getStudentId(), null);
//            applicationEventPublisher.publishEvent(studentDueEvent);
             }
         } else {
             if (ObjectUtils.isNotEmpty(razorPayTransaction) && isFeeReceiptExists) {
                 bankImportTransaction.setReceiptStatus("D");
                 bankImportTransactionRepository.save(bankImportTransaction);
                 System.out.println("duplicate bank import order id "+bankImportTransaction.getOrder_id());
             }
         }
         log.info("Successfully processed Exam Fee Receipt for Order ID: {}", bankImportTransaction.getOrder_id());




    }

    private void saveTallyReceiptForExam(List<ExamFeeReceipt> examFeeReceipts, Student_Details studentDetails, BankImportTransaction bankImportTransaction, Integer feeReceiptId) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //Bank

        Bank depositeBank = bankRepository.findByBankID(bankImportTransaction.getDeposited_bank_id());
     //   FinancialYear financialYear = financialYearRepository.getFinancialYearByFinancialYearId(bankImportTransaction.getFc_year_id());
        FinancialYear financialYear = financialYearRepository.getFinancialYearIdOnCurrentYear();
        Schools school = schoolRepository.getSchoolBySchoolId(studentDetails.getSchool_id());
       // List<TallyReceipt> tallyReceipt = tallyReceiptRepository.findByTransactionNo(bankImportTransaction.getTransaction_no());
        List<TallyReceipt> tallyReceipt = tallyReceiptRepository.findByFeeReceiptID(feeReceiptId);
        if (ObjectUtils.isNotEmpty(examFeeReceipts) && ObjectUtils.isEmpty(tallyReceipt)) {
            for (ExamFeeReceipt receipt : examFeeReceipts) {

                    TallyReceipt tallyReceipt1 = new TallyReceipt();
                    String voucherHead = voucherHeadNewRepository.getVoucherName(receipt.getVoucherHeadNew().getVoucher_head_new_id());

                    tallyReceipt1.setFee_receipt(receipt.getFeeReceipt().getFee_receipt());
                    tallyReceipt1.setFee_receipt_id(receipt.getFeeReceipt().getFee_receipt_id());
                    tallyReceipt1.setAuid(studentDetails.getAuid());

                    tallyReceipt1.setRemarks(RTGS);

                    tallyReceipt1.setDeposited_bank(depositeBank.getBank_name());
                    tallyReceipt1.setParticulars(voucherHead);
                    tallyReceipt1.setCreated_username(AUTO_RECEIPT);
                    tallyReceipt1.setCreated_by(1);
                    tallyReceipt1.setFinancial_year(financialYear.getFinancial_year());

                    tallyReceipt1.setReceived_from(receipt.getFeeReceipt().getTransaction_type());
                    tallyReceipt1.setReceived_in(INR);
                    tallyReceipt1.setReceived_type(receipt.getFeeReceipt().getReceipt_type());
                    tallyReceipt1.setSchool_name(school.getSchool_name_short());
                    tallyReceipt1.setStudent_id(studentDetails.getStudent_id());
                    tallyReceipt1.setStudent_name(studentDetails.getStudent_name());
                    tallyReceipt1.setTotal(receipt.getAmount());
                    tallyReceipt1.setTransaction_date(bankImportTransaction.getTransaction_date());
                    tallyReceipt1.setTotal_amount(bankImportTransaction.getAmount());
                    tallyReceipt1.setTransaction_no(bankImportTransaction.getTransaction_no());
                    tallyReceipt1.setTransaction_type(receipt.getFeeReceipt().getTransaction_type());
                    tallyReceipt1.setUsn(studentDetails.getUsn());
                    tallyReceipt1.setActive(Boolean.TRUE);
                    if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK") && tallyReceipt1.getSchool_name().equalsIgnoreCase("ASN"))
                    {
                        tallyReceipt1.setBank_institute("ANR");
                    } else{
                        if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK CMS"))
                        {
                            tallyReceipt1.setBank_institute("JMJ");
                        }
                        else{
                            tallyReceipt1.setBank_institute(tallyReceipt1.getSchool_name());
                        }
                    }

                    tallyReceiptRepository.save(tallyReceipt1);


            }

        }
    }



    @Transactional
    public void saveCollegeFee(BankImportTransaction bankImportTransaction) {

            // RazorPay Transaction
            RazorPayTransaction razorPayTransaction = getRazorPayTransaction(bankImportTransaction.getOrder_id(), COLLEGE_FEE);
            boolean isFeeReceiptExists = feeReceiptRepository.existsByBankTransactionHistoryId(bankImportTransaction.getOrder_id());
            String currenyType = studentDetailsRepository.getCurrenyTypeByStudentId(bankImportTransaction.getStudent_id());
            if (ObjectUtils.isNotEmpty(razorPayTransaction) && !isFeeReceiptExists) {
                Double totalAmount = razorPayPaymentDetailsRepository.getTotalAmountByRazorpayTransactionId(razorPayTransaction.getRazorPayTransactionId());
                if (Objects.equals(bankImportTransaction.getAmount(), totalAmount)){

                    System.out.println("start process for college fee bank import id - "+ bankImportTransaction.getBank_import_transaction_id()+" order id "+bankImportTransaction.getOrder_id());


                    // school id
                    Integer schoolId = studentDetailsRepository.getStudentSchoolId(bankImportTransaction.getStudent_id());
                    //student details

                    Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.getStudentId());


                    // Bank Import Transaction
//            BankImportTransaction bankImportTransaction = getBankImportTransaction(settlement, razorPayTransaction, schoolId, studentDetails);

                    //Fee Receipt
                    FeeReceipt feeReceipt = saveAllReceiptsInFeeReceipt(razorPayTransaction, bankImportTransaction, schoolId, studentDetails);

                    //Student Payment History
                    List<RazorPayPaymentDetails> razorPayCollegePaymentDetails = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, "College Fee");
                    List<StudentPaymentHistory> studentPaymentHistories = saveStudentPaymentHistory(bankImportTransaction, razorPayTransaction.getStudentId(), schoolId, feeReceipt, razorPayCollegePaymentDetails);

                    //tally
                    saveTallyReceiptForCollegeFee(studentPaymentHistories, studentDetails, bankImportTransaction, feeReceipt.getFee_receipt_id());

                    List<FeeReceipt> receipts = feeReceiptRepository.getFeeReceiptsByBankImportIdAndReceiptType(bankImportTransaction.getBank_import_transaction_id(), BULK_FEE);

                    //RTGS
                    saveRTGSReceipt(feeReceipt, bankImportTransaction, schoolId);
                    //tally for bulk fee

                    if (ObjectUtils.isNotEmpty(receipts)) {
                        saveTallyReceipt(receipts, studentDetails, bankImportTransaction);
                        for(FeeReceipt feeReceipt1 : receipts) {
                            saveRTGSReceipt(feeReceipt1, bankImportTransaction, schoolId);
                        }
                    }


                    BankImportTransaction updatedBankImportTransaction = bankImportTransactionRepository.getByOrderId(bankImportTransaction.getOrder_id());
                    updatedBankImportTransaction.setReceiptStatus("S");
                    bankImportTransactionRepository.save(updatedBankImportTransaction);

                    System.out.println("Insert fee receipt for college fee order id "+bankImportTransaction.getOrder_id());
                    //student due event
                    StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, razorPayTransaction.getStudentId(), null);
                    applicationEventPublisher.publishEvent(studentDueEvent);
                }
            } else {
                if (ObjectUtils.isNotEmpty(razorPayTransaction) && isFeeReceiptExists) {
                    bankImportTransaction.setReceiptStatus("D");
                    bankImportTransactionRepository.save(bankImportTransaction);
                    System.out.println("duplicate bank import order id  "+bankImportTransaction.getOrder_id());
                }
            }
            log.info("Successfully processed College Fee Receipt for Order ID: {}", bankImportTransaction.getOrder_id());


    }

    public void saveRTGSReceipt(FeeReceipt feeReceipt, BankImportTransaction bankImportTransaction, Integer schoolId) {
        List<RTGSFeeHistory> rtgsFeeHistories = rtgsFeeHistoryRepository
                .getRtgsFeeHistories(bankImportTransaction.getBank_import_transaction_id(),feeReceipt.getReceipt_type());


        if (ObjectUtils.isEmpty(rtgsFeeHistories) && ObjectUtils.isNotEmpty(feeReceipt)) {
            rtgsFeeHistories = new ArrayList<>();
            double balance = feeReceipt.getInr_value();
//            for (RazorPayPaymentDetails razorPay : razorPayPaymentDetails) {
                RTGSFeeHistory rtgsFeeHistory = new RTGSFeeHistory();
                balance = balance - feeReceipt.getInr_value();
                rtgsFeeHistory.setActive(Boolean.TRUE);
                rtgsFeeHistory.setBank_transaction_history_id(
                        bankImportTransaction.getBank_import_transaction_id());
                rtgsFeeHistory.setPaid(feeReceipt.getInr_value());
                rtgsFeeHistory.setSchool_id(schoolId);
                rtgsFeeHistory.setStudent_id(bankImportTransaction.getStudent_id());
                rtgsFeeHistory.setRtgs_balance_amount(balance);
                rtgsFeeHistory.setRtgs_net_amount(bankImportTransaction.getAmount());
                rtgsFeeHistory.setYear(reportingStudentsRepository.getYear(feeReceipt.getStudent_id()));
                rtgsFeeHistory.setSem(reportingStudentsRepository.getSem(feeReceipt.getStudent_id()));
                rtgsFeeHistory.setFc_year_id(financialYearRepository.getFinancialYearIdOnCurrentYear()
                        .getFinancial_year_id());

                rtgsFeeHistory.setReceipt_no(bankImportTransaction.getReceipt_no());
                rtgsFeeHistory.setReceipt_type(feeReceipt.getReceipt_type());

                rtgsFeeHistory.setCreated_by(1);
                rtgsFeeHistory.setCreated_username(AUTO_RECEIPT);


                rtgsFeeHistories.add(rtgsFeeHistory);

          //  }

            rtgsFeeHistoryRepository.saveAll(rtgsFeeHistories);
        }
    }

    public void saveTallyReceipt(List<FeeReceipt> feeReceipts, Student_Details studentDetails, BankImportTransaction bankImportTransaction) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //Bank

        Bank depositeBank = bankRepository.findByBankID(bankImportTransaction.getDeposited_bank_id());
      //  FinancialYear financialYear = financialYearRepository.getFinancialYearByFinancialYearId(bankImportTransaction.getFc_year_id());
        FinancialYear financialYear = financialYearRepository.getFinancialYearIdOnCurrentYear();
        Schools school = schoolRepository.getSchoolBySchoolId(studentDetails.getSchool_id());

        if (ObjectUtils.isNotEmpty(feeReceipts)) {
            for (FeeReceipt receipt : feeReceipts) {
            //    List<TallyReceipt> tallyReceipt = tallyReceiptRepository.findByTransactionNo(bankImportTransaction.getTransaction_no());
                List<TallyReceipt> tallyReceipt = tallyReceiptRepository.findByFeeReceiptID(receipt.getFee_receipt_id());
                if (ObjectUtils.isEmpty(tallyReceipt)) {
                    TallyReceipt tallyReceipt1 = new TallyReceipt();
                    String voucherHead = voucherHeadNewRepository.getVoucherName(receipt.getVoucher_head_new_id());

                    tallyReceipt1.setFee_receipt(receipt.getFee_receipt());
                    tallyReceipt1.setFee_receipt_id(receipt.getFee_receipt_id());
                    tallyReceipt1.setAuid(studentDetails.getAuid());

                    tallyReceipt1.setRemarks(RTGS);

                    tallyReceipt1.setDeposited_bank(depositeBank.getBank_name());
                    tallyReceipt1.setParticulars(voucherHead);
                    tallyReceipt1.setCreated_username(AUTO_RECEIPT);
                    tallyReceipt1.setCreated_by(1);
                    tallyReceipt1.setFinancial_year(financialYear.getFinancial_year());
                    tallyReceipt1.setCreated_date(receipt.getCreated_date());

                    tallyReceipt1.setReceived_from(receipt.getTransaction_type());
                    tallyReceipt1.setReceived_in(INR);
                    tallyReceipt1.setReceived_type(receipt.getReceipt_type());
                    tallyReceipt1.setSchool_name(school.getSchool_name_short());
                    tallyReceipt1.setStudent_id(studentDetails.getStudent_id());
                    tallyReceipt1.setStudent_name(studentDetails.getStudent_name());
                    tallyReceipt1.setTotal(receipt.getPaid_amount().doubleValue());
                    tallyReceipt1.setTransaction_date(bankImportTransaction.getTransaction_date());
                    tallyReceipt1.setTotal_amount(bankImportTransaction.getAmount());
                    tallyReceipt1.setTransaction_no(bankImportTransaction.getTransaction_no());
                    tallyReceipt1.setTransaction_type(receipt.getTransaction_type());
                    tallyReceipt1.setUsn(studentDetails.getUsn());
                    tallyReceipt1.setActive(Boolean.TRUE);
                    if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK") && tallyReceipt1.getSchool_name().equalsIgnoreCase("ASN"))
                    {
                        tallyReceipt1.setBank_institute("ANR");
                    } else{
                        if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK CMS"))
                        {
                            tallyReceipt1.setBank_institute("JMJ");
                        }
                        else{
                            tallyReceipt1.setBank_institute(tallyReceipt1.getSchool_name());
                        }
                    }

                    tallyReceiptRepository.save(tallyReceipt1);
                }

            }

        }
    }

    public void saveTallyReceiptForCollegeFee(List<StudentPaymentHistory> feeReceipts, Student_Details studentDetails, BankImportTransaction bankImportTransaction, Integer feeReceiptId) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //Bank

        Bank depositeBank = bankRepository.findByBankID(bankImportTransaction.getDeposited_bank_id());
       // FinancialYear financialYear = financialYearRepository.getFinancialYearByFinancialYearId(bankImportTransaction.getFc_year_id());
        FinancialYear financialYear = financialYearRepository.getFinancialYearIdOnCurrentYear();
        Schools school = schoolRepository.getSchoolBySchoolId(studentDetails.getSchool_id());
       // List<TallyReceipt> tallyReceipts = tallyReceiptRepository.findByTransactionNo(bankImportTransaction.getTransaction_no());
        List<TallyReceipt> tallyReceipts = tallyReceiptRepository.findByFeeReceiptID(feeReceiptId);
        if (ObjectUtils.isNotEmpty(feeReceipts) && ObjectUtils.isEmpty(tallyReceipts)) {
            tallyReceipts = new ArrayList<>();
            for (StudentPaymentHistory receipt : feeReceipts) {


                TallyReceipt tallyReceipt1 = new TallyReceipt();
                String voucherHead = voucherHeadNewRepository.getVoucherName(receipt.getVoucher_head_new_id());

                tallyReceipt1.setFee_receipt(receipt.getFee_receipt());
                tallyReceipt1.setFee_receipt_id(receipt.getFee_receipt_id());
                tallyReceipt1.setAuid(studentDetails.getAuid());

                tallyReceipt1.setRemarks(RTGS);

                tallyReceipt1.setDeposited_bank(depositeBank.getBank_name());
                tallyReceipt1.setParticulars(voucherHead);
                tallyReceipt1.setCreated_username(AUTO_RECEIPT);
                tallyReceipt1.setCreated_by(1);
                tallyReceipt1.setFinancial_year(financialYear.getFinancial_year());
                tallyReceipt1.setCreated_date(receipt.getCreated_date());

                tallyReceipt1.setReceived_from(receipt.getTranscation_type());
                tallyReceipt1.setReceived_in(INR);
                tallyReceipt1.setReceived_type(receipt.getType());
                tallyReceipt1.setSchool_name(school.getSchool_name_short());
                tallyReceipt1.setStudent_id(studentDetails.getStudent_id());
                tallyReceipt1.setStudent_name(studentDetails.getStudent_name());
                tallyReceipt1.setTotal(receipt.getInr_value());
                tallyReceipt1.setTransaction_date(bankImportTransaction.getTransaction_date());
                tallyReceipt1.setTotal_amount(bankImportTransaction.getAmount());
                tallyReceipt1.setTransaction_no(bankImportTransaction.getTransaction_no());
                tallyReceipt1.setTransaction_type(receipt.getTranscation_type());
                tallyReceipt1.setUsn(studentDetails.getUsn());

                tallyReceipt1.setActive(Boolean.TRUE);

                if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK") && tallyReceipt1.getSchool_name().equalsIgnoreCase("ASN"))
                {
                    tallyReceipt1.setBank_institute("ANR");
                } else{
                    if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK CMS"))
                    {
                        tallyReceipt1.setBank_institute("JMJ");
                    }
                    else{
                        tallyReceipt1.setBank_institute(tallyReceipt1.getSchool_name());
                    }
                }

                tallyReceipts.add(tallyReceipt1);

            }

            tallyReceiptRepository.saveAll(tallyReceipts);

        }
    }

    public List<StudentPaymentHistory> saveStudentPaymentHistory(BankImportTransaction bankImportTransaction, Integer studentId, Integer schoolId, FeeReceipt feeReceipt,List<RazorPayPaymentDetails> razorPayPaymentDetails) {
        List<StudentPaymentHistory> studentPaymentHistories = studentPaymentHistoryRepository
                .getStudnentPaymentHistoryByBankImportId(
                        bankImportTransaction.getBank_import_transaction_id());
        Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id();
        Integer feetemplateId = studentDetailsRepository.getFeeTemplateIdByStudentId(studentId);
        List<FeeTemplateSubAmount> feeTemplateSubAmounts = feeTemplateSubAmountRepository
                .getFeeTemplateSubAmounts(studentId);
        String currenyType = studentDetailsRepository.getCurrenyTypeByStudentId(bankImportTransaction.getStudent_id());


        if (ObjectUtils.isEmpty(studentPaymentHistories) && ObjectUtils.isNotEmpty(feeTemplateSubAmounts) && ObjectUtils.isNotEmpty(feeReceipt)) {
            studentPaymentHistories = new ArrayList<>();
           //due Amount
            HashMap<String, Object> dueObject = feeReceiptService.dueAmountCalculationOnVocherHeadWiseAndYearWiseForFeeReceipt(studentId);

            Map<String, Object> dueAmount = (Map<String, Object>) dueObject.get("dueAmount");

            String[] paidYear = feeReceipt.getPaid_year().split(",");
            Double paidAmount = Double.valueOf(feeReceipt.getPaid_amount());
            for (String pYear : paidYear) {
       //         if (ObjectUtils.isNotEmpty(feeHistory.getReceiptType()) && feeHistory.getReceipt_type().equalsIgnoreCase(GENERAL)) {

                    for (FeeTemplateSubAmount feeTemplateSubAmount : feeTemplateSubAmounts) {
                        StudentPaymentHistory studentPaymentHistory = new StudentPaymentHistory();
                        studentPaymentHistory.setActive(Boolean.TRUE);
                        studentPaymentHistory.setPaid_amount(getPaidAmount(feeTemplateSubAmount, pYear, studentPaymentHistory, paidAmount, dueAmount));
                        paidAmount = BigDecimal.valueOf(paidAmount)
                                .subtract(BigDecimal.valueOf(studentPaymentHistory.getPaid_amount()))
                                .setScale(2, RoundingMode.HALF_UP).doubleValue();
                      //  paidAmount = paidAmount - studentPaymentHistory.getPaid_amount();
                        if (paidAmount < 0) {
                            break;
                        }
                        studentPaymentHistory.setStudent_id(bankImportTransaction.getStudent_id());
                        studentPaymentHistory.setPaid_year(Integer.valueOf(pYear));
                        studentPaymentHistory.setSchool_id(schoolId);
                        studentPaymentHistory.setTranscation_type(PAYMENT_GATEWAY);
                        studentPaymentHistory.setTotal_amount(Double.valueOf(feeReceipt.getPaid_amount()));
                        studentPaymentHistory.setBankImportTransactionId(feeReceipt.getBank_transaction_history_id());

                        studentPaymentHistory.setFee_receipt(feeReceipt.getFee_receipt());
                        studentPaymentHistory.setFee_receipt_id(feeReceipt.getFee_receipt_id());
                        studentPaymentHistory.setVoucher_head_new_id(feeTemplateSubAmount.getvoucher_head_new_id());
                        studentPaymentHistory.setFee_template_id(feetemplateId);
                        studentPaymentHistory.setFinancial_year_id(fcYearId);
                        studentPaymentHistory.setCreated_by(1);
                        studentPaymentHistory.setCreated_username(AUTO_RECEIPT);
                        studentPaymentHistory.setType(feeReceipt.getReceipt_type());
                        if(StringUtils.isNotEmpty(currenyType) && !StringUtils.equalsIgnoreCase(currenyType, "INR") && ObjectUtils.isNotEmpty(bankImportTransaction.getExachange_rate()))
                        {
                            BigDecimal exchangeRate = new BigDecimal(bankImportTransaction.getExachange_rate());
                            BigDecimal inrPaidAmount = BigDecimal.valueOf(studentPaymentHistory.getPaid_amount())
                                    .multiply(exchangeRate)
                                    .setScale(2, RoundingMode.HALF_UP);
                            studentPaymentHistory.setInr_value(inrPaidAmount.doubleValue());
                            studentPaymentHistory.setDollar_value(bankImportTransaction.getExachange_rate());
                        }
                        else{
                            studentPaymentHistory.setInr_value(studentPaymentHistory.getPaid_amount());
                        }

                        if (ObjectUtils.isNotEmpty(studentPaymentHistory.getPaid_amount()) && studentPaymentHistory.getPaid_amount() > 0) {
                            studentPaymentHistories.add(studentPaymentHistory);
                        }


                    }

           //     }
            }
            if (ObjectUtils.isNotEmpty(studentPaymentHistories)) {
                return studentPaymentHistoryRepository.saveAll(studentPaymentHistories);
            }
        }
        return studentPaymentHistories;
    }

    public List<RazorPayPaymentDetails> getAllRazorPayPaymentDetailsExceptAddOnAndUniform(Long razorPayTransactionId) {
        String addOn = "Add On Fee";
        String uniForm = "Uniform Fee";
        return razorPayPaymentDetailsRepository.getByRazorPayPaymentForFeeReceipt(razorPayTransactionId, addOn, uniForm);
    }


    public RazorPayTransaction getRazorPayTransaction(String orderId, String transactionType) {
        return razorPayTransactionRepository.getByOrderIdAndTransactionType(orderId, transactionType);
    }

    public List<RazorPayPaymentDetails> getRazorPayPaymentDetailsByReceiptType(RazorPayTransaction razorPayTransaction, String receiptType) {
        return razorPayPaymentDetailsRepository.getByRazorPayTransactionIdAndReceiptType(
                razorPayTransaction.getRazorPayTransactionId(), receiptType);
    }

//    public BankImportTransaction getBankImportTransaction(SettlementList settlement, RazorPayTransaction razorPayTransaction, Integer schoolId, Student_Details studentDetails) {
//        BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getByOrderId(settlement.getOrderId());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
//
//        RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository.getRazorPaySecretKeysBySchoolId(schoolId);
//
//        //Razor Pay Payment Details for Add On Fee
//        List<RazorPayPaymentDetails> razorPayPaymentDetailsForAddOnFee = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, "Add On Fee");
//
//        //Razor Pay Payment Details for uniform Fee
//        List<RazorPayPaymentDetails> razorPayPaymentDetailsForUniformFee = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, "Uniform Fee");
//
//        double uniformAmount = (razorPayPaymentDetailsForUniformFee != null) ?
//                razorPayPaymentDetailsForUniformFee.stream()
//                        .mapToDouble(RazorPayPaymentDetails::getAmount)
//                        .sum() : 0.0;
//
//        double addOnAmount = (razorPayPaymentDetailsForUniformFee != null) ? razorPayPaymentDetailsForAddOnFee.stream()
//                .mapToDouble(RazorPayPaymentDetails::getAmount).sum() : 0.0;
//
//        double amount = ((double) settlement.getAmount() / 100) - (uniformAmount + addOnAmount);
//        double paid = ((double) settlement.getCredit() / 100) - (uniformAmount + addOnAmount);
//
//        if (ObjectUtils.isNotEmpty(razorPayTransaction)) {
//
//            if (ObjectUtils.isEmpty(bankImportTransaction)) {
//                bankImportTransaction = new BankImportTransaction();
//                bankImportTransaction.setReceipt_no(generateNextReceiptNumber());
//            }
//
//            bankImportTransaction.setSettlement_id(settlement.getSettlementId());
//            bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
//            bankImportTransaction.setOrder_id(settlement.getOrderId());
//            bankImportTransaction.setPaid(paid);
//            bankImportTransaction.setActive(Boolean.TRUE);
//            bankImportTransaction.setCreated_by(1);
//            bankImportTransaction.setCreated_username(AUTO_RECEIPT);
//            bankImportTransaction.setFc_year_id(financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
//            bankImportTransaction.setStudent_id(razorPayTransaction.getStudentId());
//            bankImportTransaction.setTransaction_remarks(razorPayTransaction.getRemarks());
//            bankImportTransaction.setSchool_id(schoolId);
//            bankImportTransaction.setAmount(amount);
//            bankImportTransaction.setCheque_dd_no(razorPayTransaction.getOrderId() + "-"
//                    + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
//            bankImportTransaction.setTransaction_date(sdf.format(razorPayTransaction.getTransactionDate()));
//            bankImportTransaction.setTransaction_no(razorPayTransaction.getPaymentId());
//            bankImportTransaction.setDeposited_bank_id(ObjectUtils.isNotEmpty(razorPaySecretKeys)
//                    && ObjectUtils.isNotEmpty(razorPaySecretKeys.getBankId())
//                    ? razorPaySecretKeys.getBankId()
//                    : null);
//            bankImportTransaction.setBalance(bankImportTransaction.getAmount() - bankImportTransaction.getPaid());
//            bankImportTransaction.setBank_inr_amt(amount);
//            bankImportTransaction.setAuid(studentDetails.getAuid());
//
//            return bankImportTransactionRepository.save(bankImportTransaction);
//        }
//
//        return null;
//    }

    public FeeReceipt saveAllReceiptsInFeeReceipt(RazorPayTransaction razorPayTransaction, BankImportTransaction bankImportTransaction, Integer schoolId, Student_Details studentDetails) {
        String receiptType;
       FeeReceipt feeReceipt = null;
        switch (razorPayTransaction.getTransactionType()) {
            case COLLEGE_FEE: {
                List<RazorPayPaymentDetails> razorPayCollegePaymentDetails = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, "College Fee");
                if (ObjectUtils.isNotEmpty(razorPayCollegePaymentDetails)) {
                    receiptType = GENERAL;
                    feeReceipt = saveFeeReceipt(bankImportTransaction, razorPayCollegePaymentDetails, schoolId, razorPayTransaction, receiptType);


                }
                List<RazorPayPaymentDetails> razorPayLatePaymentDetails = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, BULK_FEE);
                if (ObjectUtils.isNotEmpty(razorPayLatePaymentDetails)) {
                    receiptType = BULK_FEE;
                    saveFeeReceipt(bankImportTransaction, razorPayLatePaymentDetails, schoolId, razorPayTransaction, receiptType);
                }

            }

            case EXAM_FEE: {
                List<RazorPayPaymentDetails> razorPayExamPaymentDetails = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, EXAM_FEE);
                if (ObjectUtils.isNotEmpty(razorPayExamPaymentDetails)) {
                    receiptType = EXAM_FEE;
                    feeReceipt = saveFeeReceipt(bankImportTransaction, razorPayExamPaymentDetails, schoolId, razorPayTransaction, receiptType);
                    saveExamFeeReceipt(bankImportTransaction, feeReceipt, studentDetails, razorPayExamPaymentDetails);
                }
            }

//            case HOSTEL_FEE: {
//                List<RazorPayPaymentDetails> razorPayExamPaymentDetails = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, HOSTEL_FEE);
//                if (ObjectUtils.isNotEmpty(razorPayExamPaymentDetails)) {
//                    receiptType = HOSTEL_FEE;
//             //       feeReceipts = saveFeeReceipt(bankImportTransaction, razorPayExamPaymentDetails, schoolId, razorPayTransaction, receiptType);
//                  //  saveExamFeeReceipt(bankImportTransaction, feeReceipts, studentDetails);
//                     saveFeeReceiptForHostel(bankImportTransaction, razorPayExamPaymentDetails, schoolId, razorPayTransaction, receiptType);
//                }
//            }


        }



        return feeReceipt;
    }

    @Transactional
    private List<FeeReceipt> saveFeeReceiptForHostel(BankImportTransaction bankImportTransaction, RazorPayTransaction razorPayTransaction, String receiptType, List<RazorPayPaymentDetails> razorPayHostelPaymentDetails) {

        List<FeeReceipt> feeReceipts = feeReceiptRepository
                .getFeeReceiptsByBankImportIdAndReceiptType(bankImportTransaction.getBank_import_transaction_id(), receiptType);
        Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear()
                .getFinancial_year_id();

        if (ObjectUtils.isEmpty(feeReceipts)) {
            String number1;
            Integer hostelStatus = 1;
            FeeReceipt lastFeeReceipt = feeReceiptRepository
                    .getLastFeeReceiptByFinancialIdAndHostelStatus(fcYearId,hostelStatus);

            String number = lastFeeReceipt != null ? lastFeeReceipt.getFee_receipt() : null;

            if (number == null) {
                 number1 = String.format("%01d", 1);
//                number1  = generateFeeReceiptNumber(fcYearId, schoolId);

            } else if (ObjectUtils.isNotEmpty(lastFeeReceipt)) {

                Integer count = Integer.parseInt(number) + 1;
                number1 = String.format("%01d", count);

            } else {
                number1 = String.format("%01d", 1);
            }

            String fee_receipt1 = number1;

            double paidAmountSum = 0.0;
            for (RazorPayPaymentDetails razorPay : razorPayHostelPaymentDetails) {

                FeeReceipt feeReceipt = new FeeReceipt();
                feeReceipt.setBank_transaction_history_id(bankImportTransaction.getBank_import_transaction_id());
                feeReceipt.setPaid_amount(razorPay.getAmount().floatValue());
                feeReceipt.setFinancial_year_id(financialYearRepository
                        .getFinancialYearIdOnCurrentYear().getFinancial_year_id());
                feeReceipt.setActive(Boolean.TRUE);

                feeReceipt.setReceipt_type(HOS);
                feeReceipt.setStudent_id(bankImportTransaction.getStudent_id());
                //  feeReceipt.setPaid_year(String.valueOf(razorPay.getSem()));
                feeReceipt.setAc_year_id(razorPay.getAcYearId());
                feeReceipt.setFee_receipt(fee_receipt1);
                feeReceipt.setCreated_by(1);
                feeReceipt.setCreated_username(AUTO_RECEIPT);
                feeReceipt.setRemarks("AUTORECEIPT");
                feeReceipt.setTransaction_type(PAYMENT_GATEWAY);
                // feeReceipt.setTransactionMode(settlement.getMethod().toUpperCase());
                feeReceipt.setReceived_in(INR);
                feeReceipt.setSchool_id(bankImportTransaction.getSchool_id());
                feeReceipt.setHostel_status(1);
                feeReceipt.setInr_value(razorPay.getAmount());
                feeReceipt.setVoucher_head_new_id(razorPay.getVoucherHeadId());
                feeReceipt.setBank_id(bankImportTransaction.getDeposited_bank_id());

                feeReceipts.add(feeReceipt);
                paidAmountSum = paidAmountSum + feeReceipt.getPaid_amount();

            }



            bankImportTransaction.setBalance(bankImportTransaction.getBalance() - paidAmountSum);
            bankImportTransaction.setReceipt_no(fee_receipt1);
            bankImportTransactionRepository.save(bankImportTransaction);

            return  feeReceiptRepository.saveAll(feeReceipts);
        }

       return null;


    }

    @Transactional
    private List<HostelFeeReceiptVoucherHeadWise> saveHostelFeeReceiptHeadWise(RazorPayTransaction razorPayTransaction, Integer schoolId, List<FeeReceipt> feeReceipts, List<RazorPayPaymentDetails> razorPayPaymentDetails) {

       if(ObjectUtils.isNotEmpty(razorPayPaymentDetails)) {
           List<HostelFeeReceiptVoucherHeadWise> listHstlRecipt = new ArrayList<>();
           for (FeeReceipt feeReceipt : feeReceipts) {


               List<HostelFeeReceiptVoucherHeadWise> hostelFeeReceiptVoucherHeadWiseList = hostelFeeReceiptVoucherHeadWiseRepository.getVoucherHeadBYFeeReceipt(feeReceipt.getFee_receipt_id(),razorPayTransaction.getStudentId(),feeReceipt.getAc_year_id());
               //hostel due
            if(ObjectUtils.isEmpty(hostelFeeReceiptVoucherHeadWiseList))   {
               HostelDue hostelDue = hostelDueRepository.hostelDueByAcademicYearIdAndStudentId(feeReceipt.getAc_year_id(), razorPayTransaction.getStudentId());
               if (ObjectUtils.isNotEmpty(hostelDue)) {
                   HostelFeeReceiptVoucherHeadWise hstlRecipt = new HostelFeeReceiptVoucherHeadWise();
                   hstlRecipt.setCreatedBy(1);
                   hstlRecipt.setCreatedUsername(AUTO_RECEIPT);
                   hstlRecipt.setActive(true);
                   hstlRecipt.setFeeReceipt(feeReceipt);
                   hstlRecipt.setReceiptType("HOS");
                   hstlRecipt.setTotalAmount((hostelDue.getDue().floatValue() == 0f)? feeReceipt.getPaid_amount() : hostelDue.getDue().floatValue());
                   hstlRecipt.setPayingAmount(feeReceipt.getPaid_amount());
                   hstlRecipt.setBalanceAmount(hstlRecipt.getTotalAmount() - hstlRecipt.getPayingAmount());
                   HostelBedAssignmentRequest hostelBedAssign = hostelBedAssignmentRepository.getHostelBedAssignmentDataByStudentIdAndAcYearId(feeReceipt.getAc_year_id(), razorPayTransaction.getStudentId());
                   if (ObjectUtils.isEmpty(hostelBedAssign)) {
                       throw new ResourceNotFoundException("Hostel Bed Assignment is not present");
                   } else {
                       hstlRecipt.setHostelBedAssign(hostelBedAssign.getHostelBedAssignment());
                   }
                   HostelBeds hostelBed = hostelBedAssign.getHostelBeds();
                   hstlRecipt.setHostelBed(hostelBed);
                   HostelFeeTemplate hostelFeeTemplate = hostelBedAssign.getHostelFeeTemplate();
                   hstlRecipt.setHostelFeeTemplate(hostelFeeTemplate);
                        Academic_year acYear = academicYearService.get(feeReceipt.getAc_year_id());
                        hstlRecipt.setAcYear(acYear);
                   Optional<Schools> sch = schoolRepository.findById(schoolId);
                   if (!sch.isPresent()) {
                       throw new ResourceNotFoundException("School is not present");
                   } else {
                       hstlRecipt.setSchool(sch.get());
                   }
                   Optional<Student_Details> stu = studentDetailsRepository.findById(razorPayTransaction.getStudentId());
                   hstlRecipt.setStudent(stu.isPresent() ? stu.get() : hostelBedAssign.getStudent());
                   Optional<VoucherHeadNew> vhn = voucherHeadNewRepository.findById(hostelFeeTemplate.getFee_head_id());
                   if (!vhn.isPresent()) {
                       throw new ResourceNotFoundException("Voucher head is not present");
                   } else {
                       hstlRecipt.setVoucherHead(vhn.get());
                   }
                          listHstlRecipt.add(hstlRecipt);
               }
               else {
                   throw new ResourceNotFoundException("Hostel Due is not present");
               }
           }
            }
           return hostelFeeReceiptVoucherHeadWiseRepository.saveAll(listHstlRecipt);
       }
      return null;
       }







//    public void saveFeeReceiptForExam(BankImportTransaction bankImportTransaction, List<RazorPayPaymentDetails> razorPayExamPaymentDetails, Integer schoolId, SettlementList settlement, RazorPayTransaction razorPayTransaction, Student_Details studentDetails, String receiptType) {
//        List<FeeReceipt> feeReceipts = feeReceiptRepository
//                .getFeeReceiptsByBankImportIdAndReceiptType(bankImportTransaction.getBank_import_transaction_id(), receiptType);
//        Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear()
//                .getFinancial_year_id();
//
//        if (ObjectUtils.isEmpty(feeReceipts)) {
//            String number1;
//            FeeReceipt lastFeeReceipt = feeReceiptRepository
//                    .getLastFeeReceiptByFinancialIdAndSchoolId(fcYearId, schoolId);
//
//            String number = lastFeeReceipt != null ? lastFeeReceipt.getFee_receipt() : null;
//
//            if (number == null) {
//                number1 = String.format("%01d", 1);
//
//            } else if (ObjectUtils.isNotEmpty(lastFeeReceipt)
//                    && Objects.equals(fcYearId, lastFeeReceipt.getFinancial_year_id())
//                    && Objects.equals(lastFeeReceipt.getSchool_id(), schoolId)) {
//
//                // String abc = number.substring(2);
//                Integer count = Integer.parseInt(number) + 1;
//                number1 = String.format("%01d", count);
//
//            } else {
//                number1 = String.format("%01d", 1);
//            }
//
//            String fee_receipt1 = number1;
//
//
//            for (RazorPayPaymentDetails razorPay : razorPayExamPaymentDetails) {
//                FeeReceipt feeReceipt = new FeeReceipt();
//                feeReceipt.setBank_transaction_history_id(bankImportTransaction.getBank_import_transaction_id());
//                feeReceipt.setPaid_amount(razorPay.getAmount().floatValue());
//                feeReceipt.setFinancial_year_id(financialYearRepository
//                        .getFinancialYearIdOnCurrentYear().getFinancial_year_id());
//                feeReceipt.setActive(Boolean.TRUE);
//                //feeReceipt.setFee_receipt(rtgsFeeHistory.getReceipt_no());
//                feeReceipt.setReceipt_type((razorPay.getReceiptType().equalsIgnoreCase("College Fee") ? GENERAL : razorPay.getReceiptType()));
//                feeReceipt.setStudent_id(bankImportTransaction.getStudent_id());
//                feeReceipt.setPaid_year(String.valueOf(razorPay.getSem()));
//                feeReceipt.setAc_year_id(razorPay.getAcYearId());
//                feeReceipt.setFee_receipt(fee_receipt1);
//                feeReceipt.setCreated_by(1);
//                feeReceipt.setCreated_username("Amadmin");
//                feeReceipt.setRemarks("AUTORECEIPT");
//                feeReceipt.setTransaction_type("ONLINE");
//                feeReceipt.setTransactionMode(settlement.getMethod().toUpperCase());
//                feeReceipt.setReceived_in(INR);
//                feeReceipt.setSchool_id(bankImportTransaction.getSchool_id());
//                feeReceipt.setHostel_status(0);
//                feeReceipt.setInr_value(razorPay.getAmount());
//                feeReceipt.setVoucher_head_new_id(razorPay.getVoucherHeadId());
//                feeReceipts.add(feeReceipt);
//                FeeReceipt receipt = feeReceiptRepository.save(feeReceipt);
//               // feeReceiptRepository.saveAll(feeReceipts);
//
//                ExamFeeReceipt examFeeReceipt = saveExamFeeReceipt(bankImportTransaction, razorPay, feeReceipt,studentDetails);
//                if (examFeeReceipt != null) {
//                    receipt.setExam_id(String.valueOf(examFeeReceipt.getExamFeeReceiptId()));
//                    feeReceiptRepository.save(receipt);
//                }
//
//            }
//        }
//    }

    public Integer saveBulkFeeReceipt(RazorPayTransaction razorPayTransaction, BankImportTransaction bankImportTransaction, Integer schoolId, String feeReceipt, Double paidAmountSum) {



      //  List<RazorPayPaymentDetails> razorPayLatePaymentDetails = getRazorPayPaymentDetailsByReceiptType(razorPayTransaction, receiptType);

        List<BulkFeeReceipt> bulkFeeReceipts = bulkFeeReceiptRepository
                .getBulkFeeReceiptByBankImportId(bankImportTransaction.getBank_import_transaction_id());
        if (ObjectUtils.isEmpty(bulkFeeReceipts)) {

            Integer bulkreceiptNo = bulkFeeReceiptRepository.getMaxId() + 1;
//            for (RazorPayPaymentDetails latefee : razorPayLatePaymentDetails) {
                BulkFeeReceipt bulkFeeReceipt = new BulkFeeReceipt();
                bulkFeeReceipt.setActive(Boolean.TRUE);
                bulkFeeReceipt.setAmount(paidAmountSum);
                bulkFeeReceipt.setStudent_id(razorPayTransaction.getStudentId());
                bulkFeeReceipt
                        .setBankImportTransactionId(bankImportTransaction.getBank_import_transaction_id());
                bulkFeeReceipt.setFinancial_year_id(
                        financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
                bulkFeeReceipt.setSchool_id(schoolId);
                bulkFeeReceipt.setTransaction_type("Late Fee");
                bulkFeeReceipt.setBulk_fee_receipt(bulkreceiptNo);
                bulkFeeReceipt.setOrderId(razorPayTransaction.getOrderId());
                bulkFeeReceipt.setFee_receipt_id(Integer.valueOf(feeReceipt));
                Student_Details studentDetails = studentDetailsRepository.getStudentByStudentId(razorPayTransaction.getStudentId());
                bulkFeeReceipt.setFrom_name(studentDetails.getStudent_name());
                bulkFeeReceipt.setVoucher_head_new_id(335);
                bulkFeeReceipt.setReceived_in(INR);
                bulkFeeReceipt.setTransaction_type(PAYMENT_GATEWAY);
             //   bulkFeeReceipts.add(bulkFeeReceipt);

          //  }
            bulkFeeReceiptRepository.save(bulkFeeReceipt);

            return bulkreceiptNo;
        }

        return null;
    }

    public void saveExamFeeReceipt(BankImportTransaction bankImportTransaction, FeeReceipt feeReceipt, Student_Details studentDetails, List<RazorPayPaymentDetails> razorPayPaymentDetails) {

        if (ObjectUtils.isNotEmpty(feeReceipt)) {
          //  Optional<String> feeReceiptId = feeReceipts.stream().map(FeeReceipt::getFee_receipt).findFirst();
            Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear()
                    .getFinancial_year_id();
         //   if (feeReceiptId.isPresent()) {
                List<ExamFeeReceipt> examFeeReceipts = examFeeReceiptRepository.findByFeeReceiptId(feeReceipt.getFee_receipt_id());
                if (ObjectUtils.isEmpty(examFeeReceipts)) {
                    Academic_year academicYear = academicYearRepository.findByAcademicId(studentDetails.getAc_year_id());

                    for (RazorPayPaymentDetails receipt : razorPayPaymentDetails) {
                        if (receipt.getAmount() > 0) {
                            ExamFeeReceipt examFeeReceipt = new ExamFeeReceipt();
                            examFeeReceipt.setFeeReceipt(feeReceipt);
                            examFeeReceipt.setAmount(receipt.getAmount());
                            examFeeReceipt.setActive(Boolean.TRUE);

                            examFeeReceipt.setStudent(studentDetails);
                            examFeeReceipt.setAcYear(academicYear);

                            examFeeReceipt.setSchool(schoolRepository.getSchoolBySchoolId(studentDetails.getSchool_id()));
                            examFeeReceipt.setCreatedBy(1);
                            examFeeReceipt.setCreatedUsername(AUTO_RECEIPT);
                            examFeeReceipt.setFinancialYear(financialYearRepository.getFinancialYearIdOnCurrentYear());
                            examFeeReceipt.setVoucherHeadNew(voucherHeadNewRepository.getVoucherHeadBYVoucherID(receipt.getVoucherHeadId()));
                            examFeeReceipt.setPaidYear(receipt.getPaidYear());
                            examFeeReceipt.setFeeReceiptNo(feeReceipt.getFee_receipt());
                            ExamFeeReceipt examReceipt = examFeeReceiptRepository.save(examFeeReceipt);
//                            feeReceipt.setExam_id(String.valueOf(examReceipt.getExamFeeReceiptId()));
//                            feeReceiptRepository.save(feeReceipt);
                        }
                    }
                }

           // }
        }


    }

    public FeeReceipt saveFeeReceipt(BankImportTransaction bankImportTransaction, List<RazorPayPaymentDetails> razorPayPaymentDetails, Integer schoolId, RazorPayTransaction razorPayTransaction, String receiptType) {
         System.out.println("razorpay payment details - "+razorPayPaymentDetails);
        List<FeeReceipt> feeReceipts = feeReceiptRepository
                .getFeeReceiptsByBankImportIdAndReceiptType(bankImportTransaction.getBank_import_transaction_id(), receiptType);
        Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear()
                .getFinancial_year_id();
        String currenyType = studentDetailsRepository.getCurrenyTypeByStudentId(bankImportTransaction.getStudent_id());


        if (ObjectUtils.isEmpty(feeReceipts)) {
            String number1;
            Integer hostelStatus = 0;
            FeeReceipt lastFeeReceipt = feeReceiptRepository
                    .getLastFeeReceiptByFinancialIdAndSchoolIdAndHostelStatus(fcYearId, schoolId,hostelStatus);

            String number = lastFeeReceipt != null ? lastFeeReceipt.getFee_receipt() : null;

            if (number == null) {
               // number1 = String.format("%01d", 1);
               number1  = generateFeeReceiptNumber(fcYearId, schoolId);

            } else if (ObjectUtils.isNotEmpty(lastFeeReceipt)
                    && Objects.equals(fcYearId, lastFeeReceipt.getFinancial_year_id())
                    && Objects.equals(lastFeeReceipt.getSchool_id(), schoolId)) {

                Integer count = Integer.parseInt(number) + 1;
                number1 = String.format("%01d", count);

            } else {
                number1 = generateFeeReceiptNumber(fcYearId, schoolId);
            }

            String fee_receipt1 = number1;

            DoubleSummaryStatistics amount = razorPayPaymentDetails.stream()
                    .collect(Collectors.summarizingDouble(RazorPayPaymentDetails::getAmount));
            Double paidAmountSum = amount.getSum();
            String paidYear = razorPayPaymentDetails.stream()
                    .map(RazorPayPaymentDetails::getPaidYear)
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
          //  double paidAmountSum = 0.0;
//            for (RazorPayPaymentDetails razorPay : razorPayPaymentDetails) {
                FeeReceipt feeReceipt = new FeeReceipt();
                feeReceipt.setBank_transaction_history_id(bankImportTransaction.getBank_import_transaction_id());
                feeReceipt.setPaid_amount(paidAmountSum.floatValue());
                feeReceipt.setFinancial_year_id(financialYearRepository
                        .getFinancialYearIdOnCurrentYear().getFinancial_year_id());
                feeReceipt.setActive(Boolean.TRUE);

                feeReceipt.setReceipt_type(receiptType);
                feeReceipt.setStudent_id(bankImportTransaction.getStudent_id());
                feeReceipt.setPaid_year(paidYear);
                feeReceipt.setAc_year_id(razorPayTransaction.getAcYearId());
                feeReceipt.setFee_receipt(fee_receipt1);
                feeReceipt.setCreated_by(1);
                feeReceipt.setCreated_username(AUTO_RECEIPT);
                feeReceipt.setRemarks("AUTORECEIPT");
                feeReceipt.setTransaction_type(PAYMENT_GATEWAY);
               // feeReceipt.setTransactionMode(settlement.getMethod().toUpperCase());
                feeReceipt.setReceived_in(INR);
                feeReceipt.setSchool_id(bankImportTransaction.getSchool_id());
                feeReceipt.setHostel_status(0);
                feeReceipt.setInr_value(paidAmountSum);
              //  feeReceipt.setVoucher_head_new_id(razorPay.getVoucherHeadId());
                feeReceipt.setBank_id(bankImportTransaction.getDeposited_bank_id());
              //  feeReceipts.add(feeReceipt);
             //   paidAmountSum = paidAmountSum + feeReceipt.getPaid_amount();
        //    }
            if(StringUtils.isNotEmpty(currenyType) && !StringUtils.equalsIgnoreCase(currenyType, "INR") && ObjectUtils.isNotEmpty(bankImportTransaction.getExachange_rate()) && StringUtils.equalsIgnoreCase(receiptType, GENERAL))
            {

                BigDecimal exchangeRate = new BigDecimal(bankImportTransaction.getExachange_rate());
                BigDecimal usdPaidAmount = BigDecimal.valueOf(paidAmountSum).divide(exchangeRate, 2, RoundingMode.HALF_UP);
                feeReceipt.setPaid_amount(usdPaidAmount.floatValue());

            }
            else{
                if(StringUtils.isNotEmpty(currenyType) && !StringUtils.equalsIgnoreCase(currenyType, "INR") && ObjectUtils.isEmpty(bankImportTransaction.getExachange_rate()) && StringUtils.equalsIgnoreCase(receiptType, GENERAL))
                {
                    throw new ResourceNotFoundException("Exchange rate is not available for receipt generation");
                }
            }
              feeReceipt = feeReceiptRepository.save(feeReceipt);


            if (receiptType.equalsIgnoreCase(BULK_FEE)) {

                Integer bulkReceiptNo = saveBulkFeeReceipt(razorPayTransaction, bankImportTransaction, schoolId, fee_receipt1,paidAmountSum);
                List<FeeReceipt> bulkFeeReceipts = feeReceiptRepository.getFeeReceiptsByBankImportIdAndReceiptType(bankImportTransaction.getBank_import_transaction_id(), BULK_FEE);
                if (ObjectUtils.isNotEmpty(bulkFeeReceipts)) {
                    for (FeeReceipt bulkFeeReceipt : bulkFeeReceipts) {
                        bulkFeeReceipt.setBulk_id(String.valueOf(bulkReceiptNo));
                        bulkFeeReceipt.setVoucher_head_new_id(335);
                        feeReceiptRepository.save(bulkFeeReceipt);
                    }
                }

            }
            bankImportTransaction.setBalance(bankImportTransaction.getBalance() - paidAmountSum);
            bankImportTransaction.setReceipt_no(fee_receipt1);
            bankImportTransactionRepository.save(bankImportTransaction);

            return feeReceipt;
        }

        return null;


    }

    @Transactional
    public void registrationFee(RegistrationFeeTransaction registrationFeeTransaction) {

            List<RegistrationFeeTransaction> registrationFeeTransactionList = new ArrayList<>();
            if (ObjectUtils.isEmpty(registrationFeeTransaction)) {
                registrationFeeTransactionList = registrationFeeTransactionRepository.findAllPendingReceipt();
            } else {
                if(registrationFeeTransaction.getReceiptStatus().equalsIgnoreCase("P"))
                {
                    registrationFeeTransactionList.add(registrationFeeTransaction);
                }

            }

            // fee receipt
            for (RegistrationFeeTransaction registrationFee : registrationFeeTransactionList) {

                Student_Details studentDetails = studentDetailsRepository.getStudentByCandidateId(registrationFee.getCandidateId());

                if (ObjectUtils.isNotEmpty(studentDetails) && ObjectUtils.isNotEmpty(studentDetails.getAuid())) {
                    String currenyType = studentDetailsRepository.getCurrenyTypeByStudentId(studentDetails.getStudent_id());
//                    if (StringUtils.isNotEmpty(currenyType) && StringUtils.equals(currenyType, "INR"))
//
//                     {
                        BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getByOrderId(registrationFee.getOrderId());
                    if (ObjectUtils.isNotEmpty(bankImportTransaction)) {
                        ReportingStudents studentReport = reportingStudentsRepository.getDetailsOfReportingStudentsByStudentId(studentDetails.getStudent_id());
                        int yearOrSem = studentReport.getCurrent_sem() == 0 ? studentReport.getCurrent_year() : studentReport.getCurrent_sem();

                        RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository
                                .getRazorPaySecretKeysBySchoolId(studentDetails.getSchool_id());
                        bankImportTransaction.setDeposited_bank_id(ObjectUtils.isNotEmpty(razorPaySecretKeys)
                                && ObjectUtils.isNotEmpty(razorPaySecretKeys.getBankId())
                                ? razorPaySecretKeys.getBankId()
                                : null);

                        bankImportTransaction.setStudent_id(studentDetails.getStudent_id());
                        bankImportTransaction.setVoucher_head_new_id(13);
                        bankImportTransaction.setAuid(studentDetails.getAuid());

                        bankImportTransaction.setSchool_id(studentDetails.getSchool_id());
                        //  bankImportTransactionRepository.save(bankImportTransaction);
//                        RazorPayTransaction razorPayTransaction = razorPayTransactionRepository.getByOrderId(bankImportTransaction.getOrder_id());
//                        razorPayTransaction.setStudentId(studentDetails.getStudent_id());
//                        razorPayTransaction.setPaidYear(String.valueOf(yearOrSem));
//                        razorPayTransactionRepository.save(razorPayTransaction);
                        // Fee receipt
                        List<FeeReceipt> feeReceipts = feeReceiptRepository
                                .getFeeReceiptsByBankImportId(bankImportTransaction.getBank_import_transaction_id());
                        Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear()
                                .getFinancial_year_id();


                        if (ObjectUtils.isEmpty(feeReceipts)) {
                            String number1;
                            Integer hostelStatus = 0;
                            FeeReceipt lastFeeReceipt = feeReceiptRepository
                                    .getLastFeeReceiptByFinancialIdAndSchoolIdAndHostelStatus(fcYearId, studentDetails.getSchool_id(), hostelStatus);

                            String number = lastFeeReceipt != null ? lastFeeReceipt.getFee_receipt() : null;

                            if (number == null) {
                                number1 = generateFeeReceiptNumber(fcYearId, studentDetails.getSchool_id());

                            } else if (ObjectUtils.isNotEmpty(lastFeeReceipt)
                                    && fcYearId == lastFeeReceipt.getFinancial_year_id()
                                    && lastFeeReceipt.getSchool_id() == studentDetails.getSchool_id()) {


                                Integer count = Integer.valueOf(number) + 1;
                                number1 = String.format("%01d", count);

                            } else {
                                number1 = generateFeeReceiptNumber(fcYearId, studentDetails.getSchool_id());
                            }

                            String fee_receipt1 = number1;


                            FeeReceipt feeReceipt = new FeeReceipt();
                            feeReceipt.setBank_transaction_history_id(
                                    bankImportTransaction.getBank_import_transaction_id());
                            feeReceipt.setPaid_amount(bankImportTransaction.getPaid().floatValue());
                            feeReceipt.setFinancial_year_id(financialYearRepository
                                    .getFinancialYearIdOnCurrentYear().getFinancial_year_id());
                            feeReceipt.setActive(Boolean.TRUE);

                            feeReceipt.setReceipt_type(REGISTRATION_FEE);
                            feeReceipt.setStudent_id(studentDetails.getStudent_id());


                            feeReceipt.setFee_receipt(fee_receipt1);
                            feeReceipt.setCreated_by(1);
                            feeReceipt.setCreated_username(AUTO_RECEIPT);
                            feeReceipt.setRemarks("AUTORECEIPT");
                            feeReceipt.setTransaction_type(PAYMENT_GATEWAY);

                            feeReceipt.setReceived_in(INR);
                            feeReceipt.setSchool_id(bankImportTransaction.getSchool_id());
                            feeReceipt.setHostel_status(0);
                            feeReceipt.setVoucher_head_new_id(bankImportTransaction.getVoucher_head_new_id());
                            feeReceipt.setAc_year_id(studentDetails.getAc_year_id());

                            feeReceipt.setInr_value(Double.valueOf(feeReceipt.getPaid_amount()));
                            feeReceipt.setBank_id(bankImportTransaction.getDeposited_bank_id());
                            feeReceipt.setVoucher_head_new_id(bankImportTransaction.getVoucher_head_new_id());
                            feeReceipt.setPaid_year(String.valueOf(yearOrSem));

                            if(StringUtils.isNotEmpty(currenyType) && !StringUtils.equalsIgnoreCase(currenyType, "INR") && ObjectUtils.isNotEmpty(bankImportTransaction.getExachange_rate()))
                            {

                                BigDecimal exchangeRate = new BigDecimal(bankImportTransaction.getExachange_rate());
                                BigDecimal usdPaidAmount = BigDecimal.valueOf(bankImportTransaction.getPaid()).divide(exchangeRate, 2, RoundingMode.HALF_UP);
                                feeReceipt.setPaid_amount(usdPaidAmount.floatValue());

                            }
                            else{
                                if(StringUtils.isNotEmpty(currenyType) && !StringUtils.equalsIgnoreCase(currenyType, "INR") && ObjectUtils.isEmpty(bankImportTransaction.getExachange_rate()))
                                {
                                    throw new ResourceNotFoundException("Exchange rate is not available for receipt generation");
                                }
                            }

                            feeReceipts.add(feeReceiptRepository.save(feeReceipt));
                            bankImportTransaction.setReceipt_no(fee_receipt1);
                            bankImportTransaction.setBalance(bankImportTransaction.getBalance() - feeReceipt.getPaid_amount());
                        }


                        List<StudentPaymentHistory> studentPaymentHistories = studentPaymentHistoryRepository
                                .getStudnentPaymentHistoryByBankImportId(
                                        bankImportTransaction.getBank_import_transaction_id());

                        if (ObjectUtils.isEmpty(studentPaymentHistories)) {

                            double paidAmount = bankImportTransaction.getPaid();


                            //due
                            HashMap<String, Object> dueObject = feeReceiptService.dueAmountCalculationOnVocherHeadWiseAndYearWiseForFeeReceipt(studentDetails.getStudent_id());
                            Map<String, Object> dueAmount = (Map<String, Object>) dueObject.get("dueAmount");
                            Map<Integer, Object> headWiseDue = (Map<Integer, Object>) dueAmount.get(String.valueOf(yearOrSem));
                            Double toPay = (Double) headWiseDue.get(bankImportTransaction.getVoucher_head_new_id());


                            for (FeeReceipt receipt : feeReceipts) {

                                StudentPaymentHistory studentPaymentHistory = new StudentPaymentHistory();
                                studentPaymentHistory.setActive(Boolean.TRUE);
                                studentPaymentHistory.setPaid_amount(Double.valueOf(receipt.getPaid_amount()));
                                studentPaymentHistory.setStudent_id(bankImportTransaction.getStudent_id());
                                studentPaymentHistory.setSchool_id(studentDetails.getSchool_id());
                                studentPaymentHistory.setTranscation_type(PAYMENT_GATEWAY);
                                studentPaymentHistory.setTotal_amount(Double.valueOf(receipt.getPaid_amount()));
                                studentPaymentHistory.setBankImportTransactionId(
                                        bankImportTransaction.getBank_import_transaction_id());
                                studentPaymentHistory.setFee_receipt(receipt.getFee_receipt());
                                studentPaymentHistory.setFee_receipt_id(receipt.getFee_receipt_id());
                                studentPaymentHistory.setVoucher_head_new_id(bankImportTransaction.getVoucher_head_new_id());
                                studentPaymentHistory.setFinancial_year_id(fcYearId);

                                studentPaymentHistory.setCreated_by(1);
                                studentPaymentHistory.setCreated_username(AUTO_RECEIPT);
                                studentPaymentHistory.setTo_pay(toPay);
                                studentPaymentHistory.setInr_value(studentPaymentHistory.getPaid_amount());
                                studentPaymentHistory.setFee_template_id(studentDetails.getFee_template_id());
                                studentPaymentHistory.setPaid_year(yearOrSem);
                                Double balance = BigDecimal.valueOf(studentPaymentHistory.getTo_pay())
                                        .subtract(BigDecimal.valueOf(studentPaymentHistory.getPaid_amount()))
                                        .setScale(2, RoundingMode.HALF_UP).doubleValue();
                                studentPaymentHistory.setBalance_amount(balance);

                                studentPaymentHistory.setType(REGISTRATION_FEE);

                                if(StringUtils.isNotEmpty(currenyType) && !StringUtils.equalsIgnoreCase(currenyType, "INR") && ObjectUtils.isNotEmpty(bankImportTransaction.getExachange_rate()))
                                {
                                    BigDecimal exchangeRate = new BigDecimal(bankImportTransaction.getExachange_rate());
                                    BigDecimal inrPaidAmount = BigDecimal.valueOf(studentPaymentHistory.getPaid_amount())
                                            .multiply(exchangeRate)
                                            .setScale(2, RoundingMode.HALF_UP);
                                    studentPaymentHistory.setInr_value(inrPaidAmount.doubleValue());
                                    studentPaymentHistory.setDollar_value(bankImportTransaction.getExachange_rate());
                                }
                                else{
                                    studentPaymentHistory.setInr_value(studentPaymentHistory.getPaid_amount());
                                }


                                studentPaymentHistories.add(studentPaymentHistoryRepository.save(studentPaymentHistory));

                            }
                        }

                        Integer feeReceiptId = feeReceiptRepository.getFeeReceiptId(studentDetails.getStudent_id(),bankImportTransaction.getBank_import_transaction_id(),fcYearId,studentDetails.getSchool_id());
                        // Tally

                       //saveTallyReceipt(feeReceipts, studentDetails, bankImportTransaction);

                        saveTallyReceiptForCollegeFee(studentPaymentHistories, studentDetails, bankImportTransaction, feeReceiptId);



                        //RTGS

                        List<RTGSFeeHistory> rtgsFeeHistories = rtgsFeeHistoryRepository
                                .getRtgsFeeHistories(bankImportTransaction.getBank_import_transaction_id(),REGISTRATION_FEE);

                        if (ObjectUtils.isEmpty(rtgsFeeHistories)) {


                            RTGSFeeHistory rtgsFeeHistory = new RTGSFeeHistory();
                            rtgsFeeHistory.setActive(Boolean.TRUE);
                            rtgsFeeHistory.setBank_transaction_history_id(
                                    bankImportTransaction.getBank_import_transaction_id());
                            rtgsFeeHistory.setPaid(registrationFee.getAmount().doubleValue());
                            rtgsFeeHistory.setSchool_id(studentDetails.getSchool_id());
                            rtgsFeeHistory.setStudent_id(bankImportTransaction.getStudent_id());

                            rtgsFeeHistory.setRtgs_net_amount(bankImportTransaction.getAmount());
                            rtgsFeeHistory.setCreated_by(1);
                            rtgsFeeHistory.setCreated_username(AUTO_RECEIPT);

                            rtgsFeeHistory.setFc_year_id(bankImportTransaction.getFc_year_id());
                            rtgsFeeHistory.setStudent_id(studentDetails.getStudent_id());
                            rtgsFeeHistory.setRtgs_balance_amount(rtgsFeeHistory.getRtgs_net_amount() - rtgsFeeHistory.getPaid());
                            rtgsFeeHistory.setFc_year_id(financialYearRepository.getFinancialYearIdOnCurrentYear()
                                    .getFinancial_year_id());

                            rtgsFeeHistory.setReceipt_no(bankImportTransaction.getReceipt_no());
                            rtgsFeeHistory.setReceipt_type(REGISTRATION_FEE);
                            rtgsFeeHistory.setSem(studentDetails.getJoining_sem());
                            rtgsFeeHistory.setYear(studentDetails.getJoining_year());
                            rtgsFeeHistoryRepository.save(rtgsFeeHistory);
                        }


                        registrationFee.setReceiptStatus("S");
                        registrationFeeTransactionRepository.save(registrationFee);

                        bankImportTransaction.setReceiptStatus("S");
                        bankImportTransactionRepository.save(bankImportTransaction);
                        System.out.println("Insert fee receipt for registration fee order id " + bankImportTransaction.getOrder_id());
                    }
                    //student due event
                    StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, studentDetails.getStudent_id(), null);
                    applicationEventPublisher.publishEvent(studentDueEvent);
              //  }
             }
            }


    }
    //end of registration


//    public String generateNextReceiptNumber() {
//        ReceiptCounter receiptCounter = receiptCounterRepository.findById(1L).orElse(new ReceiptCounter());
//
//        Integer nextReceiptNumber = receiptCounter.getLastReceiptNumber() == null ? 1
//                : receiptCounter.getLastReceiptNumber() + 1;
//        receiptCounter.setLastReceiptNumber(nextReceiptNumber);
//        receiptCounterRepository.save(receiptCounter);
//
//        return String.format("%05d", nextReceiptNumber);
//    }


    public Double getPaidAmount(FeeTemplateSubAmount feeTemplateSubAmount, String pYear, StudentPaymentHistory studentPaymentHistory, Double totalAmount, Map<String, Object> dueAmount) {
        int paidYear = Integer.parseInt(pYear);
        //dues

        Map<Integer, Object> headWiseDue = (Map<Integer, Object>) dueAmount.get(pYear);
        Double headWiseDueAmount = (Double) headWiseDue.get(feeTemplateSubAmount.getvoucher_head_new_id());


        double balance = 0;
//        double year1 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear1_amt()) ? feeTemplateSubAmount.getYear1_amt() : 0;
//        double year2 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear2_amt()) ? feeTemplateSubAmount.getYear2_amt() : 0;
//        double year3 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear3_amt()) ? feeTemplateSubAmount.getYear3_amt() : 0;
//        double year4 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear4_amt()) ? feeTemplateSubAmount.getYear4_amt() : 0;
//        double year5 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear5_amt()) ? feeTemplateSubAmount.getYear5_amt() : 0;
//        double year6 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear6_amt()) ? feeTemplateSubAmount.getYear6_amt() : 0;
//        double year7 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear7_amt()) ? feeTemplateSubAmount.getYear7_amt() : 0;
//        double year8 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear8_amt()) ? feeTemplateSubAmount.getYear8_amt() : 0;
//        double year9 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear9_amt()) ? feeTemplateSubAmount.getYear9_amt() : 0;
//        double year10 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear10_amt()) ? feeTemplateSubAmount.getYear10_amt() : 0;
//        double year11 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear11_amt()) ? feeTemplateSubAmount.getYear11_amt() : 0;
//        double year12 = ObjectUtils.isNotEmpty(feeTemplateSubAmount.getYear12_amt()) ? feeTemplateSubAmount.getYear12_amt() : 0;

        if (paidYear == 1 && totalAmount > 0 && headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);

        }

        if (paidYear == 2 && totalAmount > 0 && headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 3 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 4 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 5 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 6 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 7 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 8 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 9 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 10 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount =  totalAmount - headWiseDueAmount ;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 11 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (paidYear == 12 && totalAmount > 0 & headWiseDueAmount > 0) {
            studentPaymentHistory.setPaid_amount(totalAmount >= headWiseDueAmount ? headWiseDueAmount : totalAmount);
            totalAmount = totalAmount - headWiseDueAmount;
            studentPaymentHistory.setTo_pay(headWiseDueAmount);
        }

        if (ObjectUtils.isNotEmpty(studentPaymentHistory) && ObjectUtils.isNotEmpty(studentPaymentHistory.getTo_pay()) && ObjectUtils.isNotEmpty(studentPaymentHistory.getPaid_amount()) && studentPaymentHistory.getTo_pay() > 0 && studentPaymentHistory.getPaid_amount() > 0) {
            balance = studentPaymentHistory.getTo_pay() - studentPaymentHistory.getPaid_amount();


            balance = BigDecimal.valueOf(studentPaymentHistory.getTo_pay())
                    .subtract(BigDecimal.valueOf(studentPaymentHistory.getPaid_amount()))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();

        }
        studentPaymentHistory.setBalance_amount(balance > 0 ? balance : 0);

        return ObjectUtils.isNotEmpty(studentPaymentHistory.getPaid_amount()) && studentPaymentHistory.getPaid_amount() > 0 ? studentPaymentHistory.getPaid_amount() : 0;
    }

//    private BankImportTransaction getBankImportTransactionForBulk(SettlementList settlement, BulkTransaction bulkTransaction, StudentDetailsbyEmail studentDetails) {
//        BankImportTransaction bankImportTransaction = bankImportTransactionRepository
//                .getByOrderId(settlement.getOrderId());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
//
//        if (ObjectUtils.isEmpty(bankImportTransaction)) {
//            bankImportTransaction = new BankImportTransaction();
//            bankImportTransaction.setReceipt_no(generateNextReceiptNumber());
//        }
//
//        bankImportTransaction.setSettlement_id(settlement.getSettlementId());
//        bankImportTransaction.setSettlement_utr(settlement.getSettlementUtr());
//        bankImportTransaction.setOrder_id(settlement.getOrderId());
//        bankImportTransaction.setPaid((double) settlement.getCredit() / 100);
//        bankImportTransaction.setActive(Boolean.TRUE);
//        bankImportTransaction.setCreated_by(1);
//        bankImportTransaction.setCreated_username(AUTO_RECEIPT);
//        bankImportTransaction.setFc_year_id(
//                financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
//
//
//        bankImportTransaction.setAmount((double) settlement.getAmount() / 100);
//        bankImportTransaction.setCheque_dd_no(bulkTransaction.getOrderId() + "-"
//                + settlement.getSettlementId() + "-" + settlement.getSettlementUtr());
//        bankImportTransaction.setTransaction_date(sdf.format(bulkTransaction.getTransactionDate()));
//        bankImportTransaction.setTransaction_no(bulkTransaction.getPaymentId());
//        bankImportTransaction.setBank_inr_amt(bankImportTransaction.getPaid());
//        RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository
//                .getRazorPaySecretKeysBySchoolId(bulkTransaction.getSchoolId());
//        bankImportTransaction.setDeposited_bank_id(ObjectUtils.isNotEmpty(razorPaySecretKeys)
//                && ObjectUtils.isNotEmpty(razorPaySecretKeys.getBankId())
//                ? razorPaySecretKeys.getBankId()
//                : null);
//        bankImportTransaction.setBalance(bankImportTransaction.getAmount() - bankImportTransaction.getPaid());
//
//        bankImportTransaction.setVoucher_head_new_id(bulkTransaction.getVoucherHeadId());
//
//        bankImportTransaction.setSchool_id(bulkTransaction.getSchoolId());
//        bankImportTransaction.setTransaction_remarks((ObjectUtils.isNotEmpty(bulkTransaction.getRemarks()))?bulkTransaction.getRemarks(): "");
//
//        if(ObjectUtils.isNotEmpty(studentDetails))
//        {
//            bankImportTransaction.setStudent_id(studentDetails.getStudentId());
//            bankImportTransaction.setAuid(studentDetails.getAuid());
//        }
//        bankImportTransactionRepository.save(bankImportTransaction);
//        return bankImportTransaction;
//    }

    public List<FeeReceipt> saveBulkTypeFeeReceipt(BankImportTransaction bankImportTransaction, BulkTransaction bulkTransaction) {
        List<FeeReceipt> feeReceipts = feeReceiptRepository
                .getFeeReceiptsByBankImportIdAndReceiptType(bankImportTransaction.getBank_import_transaction_id(), BULK);
        Integer fcYearId = financialYearRepository.getFinancialYearIdOnCurrentYear()
                .getFinancial_year_id();
       System.out.println("bulk transaction - "+bulkTransaction);

        if (ObjectUtils.isEmpty(feeReceipts)) {
            feeReceipts = new ArrayList<>();
            String number1;
            Integer hostelStatus =0;
            FeeReceipt lastFeeReceipt = feeReceiptRepository
                    .getLastFeeReceiptByFinancialIdAndSchoolIdAndHostelStatus(fcYearId, bulkTransaction.getSchoolId(),hostelStatus);

            String number = lastFeeReceipt != null ? lastFeeReceipt.getFee_receipt() : null;

            if (number == null) {
                number1 = generateFeeReceiptNumber(fcYearId, bulkTransaction.getSchoolId());

            } else if (ObjectUtils.isNotEmpty(lastFeeReceipt)
                    && Objects.equals(fcYearId, lastFeeReceipt.getFinancial_year_id())
                    && Objects.equals(lastFeeReceipt.getSchool_id(), bulkTransaction.getSchoolId())) {


                Integer count = Integer.parseInt(number) + 1;
                number1 = String.format("%01d", count);

            } else {
                number1 = generateFeeReceiptNumber(fcYearId, bulkTransaction.getSchoolId());
            }

            String fee_receipt1 = number1;


            FeeReceipt feeReceipt = new FeeReceipt();
            feeReceipt.setBank_transaction_history_id(bankImportTransaction.getBank_import_transaction_id());
            feeReceipt.setPaid_amount(bankImportTransaction.getAmount().floatValue());
            feeReceipt.setFinancial_year_id(financialYearRepository
                    .getFinancialYearIdOnCurrentYear().getFinancial_year_id());
            feeReceipt.setActive(Boolean.TRUE);

            feeReceipt.setReceipt_type(BULK);
            feeReceipt.setStudent_id(bankImportTransaction.getStudent_id());

            feeReceipt.setFee_receipt(fee_receipt1);
            feeReceipt.setCreated_by(1);
            feeReceipt.setCreated_username(AUTO_RECEIPT);
            feeReceipt.setRemarks("AUTORECEIPT");
            feeReceipt.setTransaction_type(PAYMENT_GATEWAY);
        //    feeReceipt.setTransactionMode(settlement.getMethod().toUpperCase());
            feeReceipt.setReceived_in(INR);
            feeReceipt.setSchool_id(bankImportTransaction.getSchool_id());
            feeReceipt.setHostel_status(0);
            feeReceipt.setInr_value(bankImportTransaction.getAmount());
            feeReceipt.setVoucher_head_new_id(bulkTransaction.getVoucherHeadId());
            feeReceipt.setBank_id(bankImportTransaction.getDeposited_bank_id());
            if(ObjectUtils.isNotEmpty(bankImportTransaction.getAuid())) {
                Student_Details studentDetails = studentDetailsRepository.getStudentByAuid(bankImportTransaction.getAuid());

                feeReceipt.setAc_year_id(studentDetails.getAc_year_id());

            }
            feeReceipts.add(feeReceipt);

            feeReceiptRepository.saveAll(feeReceipts);

            bankImportTransaction.setBalance(bankImportTransaction.getBalance() - feeReceipt.getPaid_amount());
            bankImportTransaction.setReceipt_no(fee_receipt1);
            bankImportTransactionRepository.save(bankImportTransaction);

        }

        return feeReceipts;


    }

    public void saveBulkFeeReceiptForBulk(BankImportTransaction bankImportTransaction, BulkTransaction bulkTransaction, List<FeeReceipt> feeReceipts) {
        List<BulkFeeReceipt> bulkFeeReceipts = bulkFeeReceiptRepository.getBulkFeeReceiptByBankImportId(bankImportTransaction.getBank_import_transaction_id());

        if(ObjectUtils.isEmpty(bulkFeeReceipts) && ObjectUtils.isNotEmpty(feeReceipts))
        {
            Integer bulkreceiptNo = bulkFeeReceiptRepository.getMaxId() + 1;
            RazorPaySecretKeys razorPaySecretKeys = razorPaySecretKeyRepository.getRazorPaySecretKeysBySchoolId(bulkTransaction.getSchoolId());
            for(FeeReceipt receipt : feeReceipts)
            {
                BulkFeeReceipt bulkFeeReceipt = new BulkFeeReceipt();
                bulkFeeReceipt.setActive(Boolean.TRUE);
                bulkFeeReceipt.setAmount(Double.valueOf(receipt.getPaid_amount()));
                bulkFeeReceipt.setStudent_id((ObjectUtils.isNotEmpty(bankImportTransaction.getStudent_id()))?bankImportTransaction.getStudent_id():0);
                bulkFeeReceipt
                        .setBankImportTransactionId(bankImportTransaction.getBank_import_transaction_id());
                bulkFeeReceipt.setFinancial_year_id(
                        financialYearRepository.getFinancialYearIdOnCurrentYear().getFinancial_year_id());
                bulkFeeReceipt.setSchool_id(bankImportTransaction.getSchool_id());
                bulkFeeReceipt.setTransaction_type(PAYMENT_GATEWAY);
                bulkFeeReceipt.setBulk_fee_receipt(bulkreceiptNo);
                bulkFeeReceipt.setOrderId(bankImportTransaction.getOrder_id());
                bulkFeeReceipt.setFee_receipt_id(Integer.valueOf(receipt.getFee_receipt()));
                bulkFeeReceipt.setMobile(bulkTransaction.getMobile());
                bulkFeeReceipt.setVoucher_head_new_id(bulkTransaction.getVoucherHeadId());
                bulkFeeReceipt.setRemarks(bulkTransaction.getRemarks());
                bulkFeeReceipt.setCreated_username(AUTO_RECEIPT);
                bulkFeeReceipt.setCreated_by(1);
                bulkFeeReceipt.setBank_id(razorPaySecretKeys.getBankId());
                bulkFeeReceipt.setFrom_name(bulkTransaction.getName());
                bulkFeeReceipt.setTransferType(bulkTransaction.getTransferType());
                bulkFeeReceipts.add(bulkFeeReceipt);

            }
            bulkFeeReceiptRepository.saveAll(bulkFeeReceipts);

            for(FeeReceipt receipt : feeReceipts)
            {
                receipt.setBulk_id(String.valueOf(bulkreceiptNo));
                feeReceiptRepository.save(receipt);
            }
        }

    }

    public void saveTallyReceiptForBulk(List<FeeReceipt> feeReceipts, BankImportTransaction bankImportTransaction, StudentDetailsbyEmail studentDetails, BulkTransaction bulkTransaction) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //Bank

        Bank depositeBank = bankRepository.findByBankID(bankImportTransaction.getDeposited_bank_id());
      //  FinancialYear financialYear = financialYearRepository.getFinancialYearByFinancialYearId(bankImportTransaction.getFc_year_id());
        FinancialYear financialYear = financialYearRepository.getFinancialYearIdOnCurrentYear();
        Schools school = schoolRepository.getSchoolBySchoolId(bankImportTransaction.getSchool_id());


        if (ObjectUtils.isNotEmpty(feeReceipts)) {
            for (FeeReceipt receipt : feeReceipts) {
                List<TallyReceipt> tallyReceipt = tallyReceiptRepository.findByTransactionNo(bankImportTransaction.getTransaction_no());
                if (ObjectUtils.isEmpty(tallyReceipt)) {
                    TallyReceipt tallyReceipt1 = new TallyReceipt();
                    String voucherHead = voucherHeadNewRepository.getVoucherName(receipt.getVoucher_head_new_id());

                    tallyReceipt1.setFee_receipt(receipt.getFee_receipt());
                    tallyReceipt1.setFee_receipt_id(receipt.getFee_receipt_id());
                    tallyReceipt1.setAuid(ObjectUtils.isNotEmpty(bankImportTransaction.getAuid())?bankImportTransaction.getAuid():"");

                   // tallyReceipt1.setRemarks(RTGS);

                    tallyReceipt1.setDeposited_bank(depositeBank.getBank_name());
                    tallyReceipt1.setParticulars(voucherHead);
                    tallyReceipt1.setCreated_username(AUTO_RECEIPT);
                    tallyReceipt1.setCreated_by(1);
                    tallyReceipt1.setFinancial_year(financialYear.getFinancial_year());
                    tallyReceipt1.setCreated_date(receipt.getCreated_date());

                    tallyReceipt1.setReceived_from(receipt.getTransaction_type());
                    tallyReceipt1.setReceived_in(INR);
                    tallyReceipt1.setReceived_type(receipt.getReceipt_type());
                    tallyReceipt1.setSchool_name(school.getSchool_name_short());

                    tallyReceipt1.setTotal(receipt.getPaid_amount().doubleValue());
                    tallyReceipt1.setTransaction_date(bankImportTransaction.getTransaction_date());
                    tallyReceipt1.setTotal_amount(bankImportTransaction.getAmount());
                    tallyReceipt1.setTransaction_no(bankImportTransaction.getTransaction_no());
                    tallyReceipt1.setTransaction_type(receipt.getTransaction_type());

                    if(ObjectUtils.isNotEmpty(studentDetails))
                    {
                        Student_Details student = studentDetailsRepository.getStudentByStudentId(studentDetails.getStudentId());
                        tallyReceipt1.setStudent_id(studentDetails.getStudentId());
                        tallyReceipt1.setStudent_name(student.getStudent_name());
                        tallyReceipt1.setUsn(student.getUsn());
                    }
                    else{
                        tallyReceipt1.setStudent_name(bulkTransaction.getName());
                    }

                    String remark = bulkTransaction.getEmail()+"/"+bulkTransaction.getMobile()+"/"+bulkTransaction.getRemarks();
                    tallyReceipt1.setRemarks(remark);
                    tallyReceipt1.setActive(Boolean.TRUE);
                    if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK") && tallyReceipt1.getSchool_name().equalsIgnoreCase("ASN"))
                    {
                        tallyReceipt1.setBank_institute("ANR");
                    } else{
                        if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK CMS"))
                        {
                            tallyReceipt1.setBank_institute("JMJ");
                        }
                        else{
                            tallyReceipt1.setBank_institute(tallyReceipt1.getSchool_name());
                        }
                    }

                    tallyReceiptRepository.save(tallyReceipt1);
                }

            }

        }


    }

    public void saveRTGSReceiptForBulk(BankImportTransaction bankImportTransaction, BulkTransaction bulkTransaction, String receiptType) {
        List<RTGSFeeHistory> rtgsFeeHistories = rtgsFeeHistoryRepository
                .getRtgsFeeHistories(bankImportTransaction.getBank_import_transaction_id(),receiptType);

        if (ObjectUtils.isEmpty(rtgsFeeHistories)) {

            double balance = bankImportTransaction.getAmount();

            RTGSFeeHistory rtgsFeeHistory = new RTGSFeeHistory();
            balance = balance - bankImportTransaction.getPaid();
            rtgsFeeHistory.setActive(Boolean.TRUE);
            rtgsFeeHistory.setBank_transaction_history_id(
                    bankImportTransaction.getBank_import_transaction_id());
            rtgsFeeHistory.setPaid(bankImportTransaction.getPaid());
            rtgsFeeHistory.setSchool_id(bankImportTransaction.getSchool_id());

            rtgsFeeHistory.setRtgs_balance_amount(balance);
            rtgsFeeHistory.setRtgs_net_amount(bankImportTransaction.getAmount());

            rtgsFeeHistory.setFc_year_id(financialYearRepository.getFinancialYearIdOnCurrentYear()
                    .getFinancial_year_id());

            rtgsFeeHistory.setReceipt_no(bankImportTransaction.getReceipt_no());
            rtgsFeeHistory.setReceipt_type(BULK);
            rtgsFeeHistory.setRemarks(bulkTransaction.getRemarks());
            rtgsFeeHistory.setCreated_by(1);
            rtgsFeeHistory.setCreated_username(AUTO_RECEIPT);
            if(ObjectUtils.isNotEmpty(bankImportTransaction.getStudent_id()))
            {
                ReportingStudents studentReport =  reportingStudentsRepository.getDetailsOfReportingStudentsByStudentId(bankImportTransaction.getStudent_id());
                rtgsFeeHistory.setStudent_id(bankImportTransaction.getStudent_id());
                rtgsFeeHistory.setYear(studentReport.getCurrent_year());
                rtgsFeeHistory.setSem(studentReport.getCurrent_sem());
            }

            rtgsFeeHistoryRepository.save(rtgsFeeHistory);


        }
    }

    @Async
    public void updateStatus() {
        try {
            System.out.println("Updating bank import status...");
            bankImportTransactionRepository.updateBankImportInprogressToPending();
            System.out.println("Status update completed.");
        } catch (Exception e) {
            System.err.println("Error updating status: " + e.getMessage());
        }
    }

private String generateFeeReceiptNumber(Integer fcYearId, Integer schoolId){
    if(fcYearId == 1) {
        String schoolShortName = schoolRepository.getSchoolShortName(schoolId);
        switch (schoolShortName) {
            case "AIT":
                return String.format("%01d",31281);
            case "AGS":
                return  String.format("%01d",15829);
            case "APT":
                return String.format("%01d",2640);
            case "ACP":
                return String.format("%01d",4359);
            case "ASA":
                return String.format("%01d",1121);
            case "ANR":
                return String.format("%01d",1933);
            case "ASD":
                return String.format("%01d",2973);
            case "JMJ":
                return String.format("%01d",35);
            case "AHS":
                return String.format("%01d",2949);
            case "ASN":
                return String.format("%01d",1022);
            case "APS":
                return String.format("%01d",889);
            default:
                return String.format("%01d", 1);
        }
    }
    else{
        return  String.format("%01d", 1);
    }
}

    public List<HostelBulkFeeReceiptVocherHeadWise> saveHostelBulkFeeReceiptVoucherHeadWise(BulkTransaction bulkTransaction, List<FeeReceipt> feeReceipts, StudentDetailsbyEmail studentDetails) {

        if(ObjectUtils.isNotEmpty(bulkTransaction)) {
            List<HostelBulkFeeReceiptVocherHeadWise> listHstlRecipt = new ArrayList<>();
            for (FeeReceipt feeReceipt : feeReceipts) {


                List<HostelFeeReceiptVoucherHeadWise> hostelFeeReceiptVoucherHeadWiseList = hostelBulkFeeReceiptVocherHeadWiseRepository.getVoucherHeadByHostelBulkFeeReceipt(feeReceipt.getFee_receipt_id(),studentDetails.getStudentId(),feeReceipt.getAc_year_id());
                //hostel due
                if(ObjectUtils.isEmpty(hostelFeeReceiptVoucherHeadWiseList))   {
//                    HostelDue hostelDue = hostelDueRepository.hostelDueByAcademicYearIdAndStudentId(feeReceipt.getAc_year_id(), studentDetails.getStudentId());
//                    if (ObjectUtils.isNotEmpty(hostelDue)) {
                        HostelBulkFeeReceiptVocherHeadWise hstlRecipt = new HostelBulkFeeReceiptVocherHeadWise();
                        hstlRecipt.setCreatedBy(1);
                        hstlRecipt.setCreatedUsername(AUTO_RECEIPT);
                        hstlRecipt.setActive(true);
                        hstlRecipt.setFeeReceipt(feeReceipt);

                        hstlRecipt.setTotalAmount(feeReceipt.getPaid_amount());
                        hstlRecipt.setPayingAmount(feeReceipt.getPaid_amount());
                        hstlRecipt.setBalanceAmount(hstlRecipt.getTotalAmount() - hstlRecipt.getPayingAmount());
//                        HostelBedAssignmentRequest hostelBedAssign = hostelBedAssignmentRepository.getHostelBedAssignmentDataByStudentIdAndAcYearId(feeReceipt.getAc_year_id(), studentDetails.getStudentId());
//                        if (ObjectUtils.isEmpty(hostelBedAssign)) {
//                            throw new ResourceNotFoundException("Hostel Bed Assignment is not present");
//                        } else {
//                            hstlRecipt.setHostelBedAssign(hostelBedAssign.getHostelBedAssignment());
//                        }
//                        HostelBeds hostelBed = hostelBedAssign.getHostelBeds();
//                        hstlRecipt.setHostelBed(hostelBed);
                     //   HostelFeeTemplate hostelFeeTemplate = hostelBedAssign.getHostelFeeTemplate();
                     //   hstlRecipt.setHostelFeeTemplate(hostelFeeTemplate);
                        Academic_year acYear = academicYearService.get(feeReceipt.getAc_year_id());
                        hstlRecipt.setAcYear(acYear);
                        Optional<Schools> sch = schoolRepository.findById(bulkTransaction.getSchoolId());
                        if (!sch.isPresent()) {
                            throw new ResourceNotFoundException("School is not present");
                        } else {
                            hstlRecipt.setSchool(sch.get());
                        }
//                        Optional<Student_Details> stu = studentDetailsRepository.findById(studentDetails.getStudentId());
                      //  hstlRecipt.setStudent(stu.isPresent() ? stu.get() : hostelBedAssign.getStudent());
                        Optional<VoucherHeadNew> vhn = voucherHeadNewRepository.findById(bulkTransaction.getVoucherHeadId());
                        if (!vhn.isPresent()) {
                            throw new ResourceNotFoundException("Voucher head is not present");
                        } else {
                            hstlRecipt.setVoucherHead(vhn.get());
                        }
                        listHstlRecipt.add(hstlRecipt);
//                    }
//                    else {
//                        throw new ResourceNotFoundException("Hostel Due is not present");
//                    }
                }
            }
            return hostelBulkFeeReceiptVocherHeadWiseRepository.saveAll(listHstlRecipt);
        }
        return null;
    }

    @Transactional
    private void saveTallyReceiptForHostelBulk(List<HostelBulkFeeReceiptVocherHeadWise> hostelFeeReceiptVoucherHeadWises, BulkTransaction bulkTransaction, BankImportTransaction bankImportTransaction) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        //Bank

        Bank depositeBank = bankRepository.findByBankID(bankImportTransaction.getDeposited_bank_id());
        //FinancialYear financialYear = financialYearRepository.getFinancialYearByFinancialYearId(bankImportTransaction.getFc_year_id());
        FinancialYear financialYear = financialYearRepository.getFinancialYearIdOnCurrentYear();
        Schools school = schoolRepository.getSchoolBySchoolId(bulkTransaction.getSchoolId());


        if (ObjectUtils.isNotEmpty(hostelFeeReceiptVoucherHeadWises)) {
            for (HostelBulkFeeReceiptVocherHeadWise receipt : hostelFeeReceiptVoucherHeadWises) {
                List<TallyReceipt> tallyReceipt = tallyReceiptRepository.findByFeeReceiptID(receipt.getFeeReceipt().getFee_receipt_id());
                if (ObjectUtils.isEmpty(tallyReceipt)) {
                    TallyReceipt tallyReceipt1 = new TallyReceipt();
                    String voucherHead = voucherHeadNewRepository.getVoucherName(receipt.getVoucherHead().getVoucher_head_new_id());

                    tallyReceipt1.setFee_receipt(receipt.getFeeReceipt().getFee_receipt());
                    tallyReceipt1.setFee_receipt_id(receipt.getFeeReceipt().getFee_receipt_id());
                    //tallyReceipt1.setAuid(studentDetails.getAuid());

                    tallyReceipt1.setRemarks(RTGS);

                    tallyReceipt1.setDeposited_bank(depositeBank.getBank_name());
                    tallyReceipt1.setParticulars(voucherHead);
                    tallyReceipt1.setCreated_username(AUTO_RECEIPT);
                    tallyReceipt1.setCreated_by(1);
                    tallyReceipt1.setFinancial_year(financialYear.getFinancial_year());
                    tallyReceipt1.setCreated_date(receipt.getFeeReceipt().getCreated_date());

                    tallyReceipt1.setReceived_from(receipt.getFeeReceipt().getTransaction_type());
                    tallyReceipt1.setReceived_in(INR);
                    tallyReceipt1.setReceived_type(receipt.getFeeReceipt().getReceipt_type());
                    tallyReceipt1.setSchool_name(school.getSchool_name_short());
                //    tallyReceipt1.setStudent_id(studentDetails.getStudent_id());
                    tallyReceipt1.setStudent_name(bulkTransaction.getName());
                    tallyReceipt1.setTotal(Double.valueOf(receipt.getPayingAmount()));
                    tallyReceipt1.setTransaction_date(bankImportTransaction.getTransaction_date());
                    tallyReceipt1.setTotal_amount(bankImportTransaction.getAmount());
                    tallyReceipt1.setTransaction_no(bankImportTransaction.getTransaction_no());
                    tallyReceipt1.setTransaction_type(receipt.getFeeReceipt().getTransaction_type());
                   // tallyReceipt1.setUsn(studentDetails.getUsn());
                    tallyReceipt1.setActive(Boolean.TRUE);
                    if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK") && tallyReceipt1.getSchool_name().equalsIgnoreCase("ASN"))
                    {
                        tallyReceipt1.setBank_institute("ANR");
                    } else{
                        if(tallyReceipt1.getDeposited_bank().equalsIgnoreCase("YES BANK CMS"))
                        {
                            tallyReceipt1.setBank_institute("JMJ");
                        }
                        else{
                            tallyReceipt1.setBank_institute(tallyReceipt1.getSchool_name());
                        }
                    }

                    tallyReceiptRepository.save(tallyReceipt1);
                }

            }

        }
    }

}