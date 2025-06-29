package com.au.service;

import com.au.dto.*;
import com.au.event.StudentDueEvent;
import com.au.exception.ResourceNotFoundException;
import com.au.model.*;
import com.au.repository.*;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class FeeReceiptService {

    private Logger logger = LoggerFactory.getLogger(FeeReceiptService.class);

    @Autowired
    private FeeReceiptRepository feeReceiptRepository;

    @Autowired
    private RTGSFeeHistoryRepository rtgsFeeHistoryRepository;

    @Autowired
    private StudentDetailsRepository studentDetailsRepository;

    @Autowired
    private FeeTemplateSubAmountRepository feeTemplateSubAmountRepository;

    @Autowired
    private ScholarshipApprovalStatusRepository scholarshipApprovalStatusRepository;

    @Autowired
    private StudentPaymentHistoryRepository studentPaymentHistoryRepository;

    @Autowired
    private TutionFeeWaiverRepository tutionFeeWaiverRepository;

    @Autowired
    private TutionFeeWaiverReportRepository tutionFeeWaiverReportRepository;

    @Autowired
    private FeeTemplateRepository feeTemplateRepository;

    @Autowired
    private TallyReceiptRepository tallyReceiptRepository;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private ScholarshipRepository preScholarshipRepository;

    @Autowired
    private ScholarshipVocherHeadWiseAmountDetailsRepository scholarshipVocherHeadWiseAmountDetailsRepository;

    @Autowired
    private TutionFeeWaiverSubAmountRepository tutionFeeWaiverSubAmountRepository;

    @Autowired
    private FinancialYearRepository financialYearRepository;

    @Autowired
    private BankImportTransactionRepository bankImportTransactionRepository;

    @Autowired
    private StudentDueRepository studentDueRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CancelledFeeReceiptsRepository cancelledFeeReceiptsRepository;

    @Autowired
    private HostelDueRepository hostelDueRepository;

    @Autowired
    private StudentDetailsService studentDetailsService;

    @Autowired
    private AcademicYearService academicYearService;

    @Autowired
    private HostelBedAssignmentRepository hostelBedAssignmentRepository;

    @Autowired
    private HostelFeeTemplateService hostelFeeTemplateService;

    @Autowired
    private VoucherHeadNewRepository voucherHeadNewRepository;

    @Autowired
    private HostelBedsRepository hostelBedsRepository;

    @Autowired
    private CurrencyTypeRepository currencyTypeRepository;

    @Autowired
    private DollarToInrConversionRepository dollarToInrConversionRepository;

    @Autowired
    private AcerpAmountRepository acerp_amount_repo;

    @Autowired
    private AcerpAmountService acerp_amount_service;

    @Autowired
    private OtherFeeDetailsRepository otherFeeDetailsRepository;

    @Autowired
    private ReadmissionRepository re_repo;

    @Autowired
    private HostelWaiverRepository hostelWaiverRepository;

    @Autowired
    private CmaFeeReceiptRepository cmaFeeReceiptRepository;

    @Autowired
    private School_Repository schoolRepository;

    @Autowired
    private HostelFeeReceiptVoucherHeadWiseRepository hstlFeeReciptRepository;


    @Autowired
    private HostelBulkFeeReceiptVocherHeadWiseRepository hstlBulkFeeReciptRepository;

    @Autowired
    private CancelledHostelFeeReceiptsRepository cancelledHostelFeeReceiptsRepository;

    @Autowired
    private PettyCashRepository pettyCashRepository;


    @Autowired
    private CancelledTallyHostelReceiptRepository cancelledTallyHostelReceiptRepository;

    @Autowired
    private RegistrationFeeTrsactionRepository registrationFeeTrsactionRepository;

    @Autowired
    private BulkFeeReceiptRepository bulkFeeReceiptRepository;

    @Autowired
    private ExamFeeReceiptRepository examFeeReceiptRepository;

    @Autowired
    private BankRepository bankRepository;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> saveFeeReceipt(FeeReceiptDto frd, String jwtToken) {
        try {
            JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = null;
            try {

                date1 = df.parse(date);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            FinancialYear f_year = financialYearRepository.getFinancialYearData(date1);

            FeeReceipt fee_receipt_response = createFeeReceipt(frd, jwtDetails, f_year);
            saveTallyReceipt(frd, f_year, fee_receipt_response, jwtDetails);

            if (ObjectUtils.isNotEmpty(frd.getSph()))
                saveStudentPaymentHistory(frd, fee_receipt_response, jwtDetails);

            if (frd.getFee_rec().getTransaction_type().equalsIgnoreCase("RTGS"))
                saveRTGSTransaction(frd, f_year, fee_receipt_response, jwtDetails);

            if (frd.getFee_rec().getReceipt_type().equalsIgnoreCase("HOS") ||
                    frd.getFee_rec().getReceipt_type().equalsIgnoreCase("HOSB"))
                saveHostelFeeReceiptVoucherHeadWise(frd, fee_receipt_response);

//			if (frd.getFee_rec().getReceipt_type().equalsIgnoreCase("HOSB"))
//			     saveHostelBulkFeeReceiptVoucherHeadWise(frd,fee_receipt_response,f_year);

            if (ObjectUtils.isNotEmpty(frd.getFee_rec().getStudent_id())
                    && ObjectUtils.isNotEmpty(frd.getFee_rec().getAc_year_id())
                    && (frd.getFee_rec().getReceipt_type()).equalsIgnoreCase("HOS")) {
                hostelDueRepository.hostelDueProcedureByStudentIdAndAcYearId(frd.getFee_rec().getStudent_id(),
                        frd.getFee_rec().getAc_year_id());
                Student_Details studentDetails = studentDetailsService.get(frd.getFee_rec().getStudent_id());
                Academic_year academicYear = academicYearService.get(frd.getFee_rec().getAc_year_id());
                updateHostelBedStatusForStudentAndAcademicYear(studentDetails, academicYear);

            }

            if (ObjectUtils.isNotEmpty(frd.getFee_rec().getStudent_id())) {
                StudentDueEvent studentDueEvent = new StudentDueEvent(null, null, frd.getFee_rec().getStudent_id(), "student due");
                applicationEventPublisher.publishEvent(studentDueEvent);
            }

            return ResponseHandler.generateResponse(true, HttpStatus.CREATED, fee_receipt_response);
        } catch (Exception e) {
            logger.error("Fee Receipt " + e.getMessage());
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        }

    }


    private void saveHostelBulkFeeReceiptVoucherHeadWise(FeeReceiptDto feeReceiptDto, FeeReceipt feeReceipt, FinancialYear financialYear) {
        List<HostelBulkFeeReceiptVocherHeadWise> listHstlBulkRecipt = new ArrayList<>();
        feeReceiptDto.getHostelBulkFeeReciptVocherHead().stream().forEach(hfrvh -> {
            HostelBulkFeeReceiptVocherHeadWise hstlBulkRecipt = new HostelBulkFeeReceiptVocherHeadWise();
            hstlBulkRecipt.setCreatedBy(feeReceipt.getCreated_by());
            hstlBulkRecipt.setCreatedUsername(feeReceipt.getCreated_username());
            hstlBulkRecipt.setActive(feeReceipt.getActive());
            hstlBulkRecipt.setFeeReceipt(feeReceipt);
            hstlBulkRecipt.setTotalAmount(hfrvh.getTotalAmount());
            hstlBulkRecipt.setPayingAmount(hfrvh.getPayingAmount());
            hstlBulkRecipt.setBalanceAmount(hfrvh.getBalanceAmount());
            Optional<HostelBedAssignment> hostelBedAssign = hostelBedAssignmentRepository.findById(hfrvh.getHostelBedAssignmentId());
            if (!hostelBedAssign.isPresent()) {
                throw new ResourceNotFoundException("Hostel Bed Assignment is not present");
            } else {
                hstlBulkRecipt.setHostelBedAssign(hostelBedAssign.get());
            }
            Academic_year acYear = academicYearService.get(hfrvh.getAcYearId());
            hstlBulkRecipt.setAcYear(acYear);
            Optional<Schools> sch = schoolRepository.findById(hfrvh.getSchoolId());
            if (!sch.isPresent()) {
                throw new ResourceNotFoundException("School is not present");
            } else {
                hstlBulkRecipt.setSchool(sch.get());
            }

            Optional<Student_Details> stu = studentDetailsRepository.findById(hfrvh.getStudentId());
            hstlBulkRecipt.setStudent(stu.isPresent() ? stu.get() : hostelBedAssign.get().getStudent());
            hstlBulkRecipt.setFinancialYear(financialYear);
            Optional<VoucherHeadNew> vhn = voucherHeadNewRepository.findById(hfrvh.getVoucherHeadNewId());
            if (!vhn.isPresent()) {
                throw new ResourceNotFoundException("Voucher head is not present");
            } else {
                hstlBulkRecipt.setVoucherHead(vhn.get());
            }
            listHstlBulkRecipt.add(hstlBulkRecipt);
        });
        hstlBulkFeeReciptRepository.saveAll(listHstlBulkRecipt);
    }


    private void saveHostelFeeReceiptVoucherHeadWise(FeeReceiptDto feeReceiptDto, FeeReceipt feeReceipt) {
        List<HostelFeeReceiptVoucherHeadWise> listHstlRecipt = new ArrayList<>();
        feeReceiptDto.getHostelFeeReceiptVocherHead().stream().forEach(hfrvh -> {
            HostelFeeReceiptVoucherHeadWise hstlRecipt = new HostelFeeReceiptVoucherHeadWise();
            hstlRecipt.setCreatedBy(feeReceipt.getCreated_by());
            hstlRecipt.setCreatedUsername(feeReceipt.getCreated_username());
            hstlRecipt.setActive(feeReceipt.getActive());
            hstlRecipt.setFeeReceipt(feeReceipt);
            hstlRecipt.setTotalAmount(hfrvh.getTotalAmount());
            hstlRecipt.setPayingAmount(hfrvh.getPayingAmount());
            hstlRecipt.setBalanceAmount(hfrvh.getBalanceAmount());
            hstlRecipt.setReceiptType(feeReceiptDto.getFee_rec().getReceipt_type());
            hstlRecipt.setReceivedFrom(hfrvh.getReceivedFrom());
            hstlRecipt.setCashier(feeReceipt.getCreated_username());
            hstlRecipt.setRemarks(feeReceipt.getRemarks());
            if (feeReceiptDto.getFee_rec().getReceipt_type().equalsIgnoreCase("HOS")) {
                HostelBedAssignmentRequest hostelBedAssign = hostelBedAssignmentRepository.hostelBedAssignmentById(hfrvh.getHostelBedAssignmentId());
                if (ObjectUtils.isEmpty(hostelBedAssign)) {
                    throw new ResourceNotFoundException("Hostel Bed Assignment is not present");
                } else {
                    hstlRecipt.setHostelBedAssign(hostelBedAssign.getHostelBedAssignment());
                }
                HostelBeds hostelBed = hostelBedAssign.getHostelBeds();
                hstlRecipt.setHostelBed(hostelBed);
                HostelFeeTemplate hostelFeeTemplate = hostelBedAssign.getHostelFeeTemplate();
                hstlRecipt.setHostelFeeTemplate(hostelFeeTemplate);
            }

            if (ObjectUtils.isNotEmpty(hfrvh.getAcYearId())) {
                Academic_year acYear = academicYearService.get(hfrvh.getAcYearId());
                hstlRecipt.setAcYear(acYear);

            }

            Optional<Schools> sch = schoolRepository.findById(hfrvh.getSchoolId());
            if (!sch.isPresent()) {
                throw new ResourceNotFoundException("School is not present");
            } else {
                hstlRecipt.setSchool(sch.get());
            }
            if (ObjectUtils.isNotEmpty(feeReceiptDto.getFee_rec().getStudent_id())) {
                Optional<Student_Details> stu = studentDetailsRepository.findById(feeReceiptDto.getFee_rec().getStudent_id());
                hstlRecipt.setStudent(stu.get());
            }
            Optional<VoucherHeadNew> vhn = voucherHeadNewRepository.findById(hfrvh.getVoucherHeadNewId());
            if (!vhn.isPresent()) {
                throw new ResourceNotFoundException("Voucher head is not present");
            } else {
                hstlRecipt.setVoucherHead(vhn.get());
            }
            listHstlRecipt.add(hstlRecipt);
        });
        hstlFeeReciptRepository.saveAll(listHstlRecipt);
    }


    private void saveRTGSTransaction(FeeReceiptDto frd, FinancialYear f_year, FeeReceipt fee_receipt_response,
                                     JwtDetails jwtDetails) {
        if (frd.getFee_rec().getTransaction_type().equalsIgnoreCase("RTGS")) {
            frd.getBit().setModified_by(jwtDetails.getUserId());
            frd.getBit().setModified_username(jwtDetails.getUserName());
            frd.getBit().setReceipt_no(fee_receipt_response.getFee_receipt());
            frd.getBit().setFc_year_id(f_year.getFinancial_year_id());

            bankImportTransactionRepository.save(frd.getBit());

            RTGSFeeHistory rfh = new RTGSFeeHistory();

            rfh.setBank_transaction_history_id(frd.getBit().getBank_import_transaction_id());
            rfh.setStudent_id(frd.getBit().getStudent_id());
            rfh.setSchool_id(frd.getBit().getSchool_id());
            rfh.setReceipt_no(fee_receipt_response.getFee_receipt());
            rfh.setFc_year_id(f_year.getFinancial_year_id());
            rfh.setRtgs_net_amount(frd.getBit().getAmount());
            rfh.setPaid(frd.getBit().getPaid());
            rfh.setRtgs_balance_amount(frd.getBit().getBalance());
            rfh.setReceipt_type(frd.getFee_rec().getReceipt_type());
            rfh.setRemarks(frd.getBit().getTransaction_remarks());
            rfh.setCreated_by(jwtDetails.getUserId());
            rfh.setCreated_username(jwtDetails.getUserName());
            rfh.setActive(true);
            rtgsFeeHistoryRepository.save(rfh);
        }

    }

    private void saveTallyReceipt(FeeReceiptDto frd, FinancialYear f_year, FeeReceipt fee_receipt_response,
                                  JwtDetails jwtDetails) {
        frd.getTr().stream().forEach(tr -> {
            tr.setFee_receipt(fee_receipt_response.getFee_receipt());
            tr.setCreated_by(jwtDetails.getUserId());
            tr.setFee_receipt_id(fee_receipt_response.getFee_receipt_id());
            tr.setCreated_username(jwtDetails.getUserName());
            tr.setFinancial_year(f_year.getFinancial_year());
            tr.setRemarks(frd.getFee_rec().getRemarks());
            tr.setTotal_amount(fee_receipt_response.getInr_value());
            tr.setTotal(fee_receipt_response.getInr_value());
        });
        tallyReceiptRepository.saveAll(frd.getTr());

    }

    private void saveStudentPaymentHistory(FeeReceiptDto frd, FeeReceipt fee_receipt_response, JwtDetails jwtDetails) {
        frd.getSph().stream().forEach(sph -> {
            System.out.println("Fee Rcpt ID : " + fee_receipt_response.getFee_receipt_id());
            sph.setFee_receipt(fee_receipt_response.getFee_receipt());
            sph.setCreated_by(jwtDetails.getUserId());
            sph.setFinancial_year_id(fee_receipt_response.getFinancial_year_id());
            sph.setFee_receipt_id(fee_receipt_response.getFee_receipt_id());
            sph.setCreated_username(jwtDetails.getUserName());
        });
        studentPaymentHistoryRepository.saveAll(frd.getSph());

    }

    private FeeReceipt createFeeReceipt(FeeReceiptDto frd, JwtDetails jwtDetails, FinancialYear f_year) {
        String rt = frd.getFee_rec().getReceipt_type().substring(0, 1);
        String str = ResponseHandler.nameCapital(rt);

        String number1;
        FeeReceipt lastFeeReceipt;
        if (frd.getFee_rec().getReceipt_type().equalsIgnoreCase("HOS") ||
                frd.getFee_rec().getReceipt_type().equalsIgnoreCase("HOSB")) {
            lastFeeReceipt = feeReceiptRepository
                    .getLastFeeReceiptByFinancialIdAndHostelStatus(f_year.getFinancial_year_id(),frd.getFee_rec().getHostel_status());
        } else {

            lastFeeReceipt = feeReceiptRepository
                    .getLastFeeReceiptByFinancialIdAndSchoolIdAndHostelStatus(f_year.getFinancial_year_id(), frd.getFee_rec().getSchool_id(),frd.getFee_rec().getHostel_status());
        }
        String number = lastFeeReceipt != null ? lastFeeReceipt.getFee_receipt() : null;

        if (ObjectUtils.isNotEmpty(lastFeeReceipt)
                && f_year.getFinancial_year_id() == lastFeeReceipt.getFinancial_year_id()
                && lastFeeReceipt.getSchool_id() == frd.getFee_rec().getSchool_id()) {
            Integer count = Integer.valueOf(number) + 1;
            number1 = String.format("%01d", count);

        } else if (ObjectUtils.isNotEmpty(lastFeeReceipt)
                && f_year.getFinancial_year_id() == lastFeeReceipt.getFinancial_year_id()) {
            Integer count = Integer.valueOf(number) + 1;
            number1 = String.format("%01d", count);
        } else {
            number1 = String.format("%01d", 1);
        }

        String fee_receipt1 = number1;

        frd.getFee_rec().setFee_receipt(fee_receipt1);
        frd.getFee_rec().setFinancial_year_id(f_year.getFinancial_year_id());
        frd.getFee_rec().setCreated_by(jwtDetails.getUserId());
        frd.getFee_rec().setCreated_username(jwtDetails.getUserName());
        frd.getFee_rec().setFinancial_year_id(f_year.getFinancial_year_id());
        frd.getFee_rec().setCreated_by(jwtDetails.getUserId());
        frd.getFee_rec().setCreated_username(jwtDetails.getUserName());
        FeeReceipt feeReceiptResponse = feeReceiptRepository.save(frd.getFee_rec());
        return feeReceiptResponse;
    }

    public void updateHostelBedStatusForStudentAndAcademicYear(Student_Details studentDetails,
                                                                Academic_year academicYear) {
        HostelBedAssignment hostelBedAssignment = hostelBedAssignmentRepository
                .findByAcYearAndStudentAndCancelledRemarksIsNullAndActiveTrue(academicYear, studentDetails);
        HostelBedAssignment otherStudentAssignedHostelBed = hostelBedAssignmentRepository
                .findByAcYearAndHostelBedAndCancelledRemarksIsNullAndActiveTrue(academicYear,
                        hostelBedAssignment.getHostelBed());
        if (ObjectUtils.isEmpty(otherStudentAssignedHostelBed) && ObjectUtils.isNotEmpty(hostelBedAssignment)) {
            hostelBedsRepository.updateBedStatusByBedId("Assigned",
                    hostelBedAssignment.getHostelBed().getHostelBedId());
        } else {
            hostelBedsRepository.updateBedStatusByBedId("Occupied-Assigned",
                    hostelBedAssignment.getHostelBed().getHostelBedId());
        }
    }

    public List<FeeReceipt> getActiveFeeReceipt() {
        return feeReceiptRepository.getActiveFeeReceipt();
    }

//		public List<FeeReceipt> getAllFeeReceipt(){
//			return feeReceiptRepository.findAll();
//		}

    public ResponseEntity<Object> getAllFeeReceipt1(Pageable pageable, Object keyword) {

        Page<Object> response1 = feeReceiptRepository.findAll1(pageable, keyword);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response1);
    }

    public ResponseEntity<Object> getAllFeeReceipt2(Pageable pageable) {

        Page<Object> response = feeReceiptRepository.findAll2(pageable);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
    }


    public ResponseEntity<Object> FeeReceiptListAll1(Pageable pageable, Object keyword, Integer school_id, LocalDate start, LocalDate end, LocalDate minDate) {
        List<Map<String, Object>> response1 = feeReceiptRepository.FeeReceiptListAll1(pageable, keyword, school_id, start, end, minDate);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
    }

    public ResponseEntity<Object> FeeReceiptListAll12(Pageable pageable, Integer school_id, LocalDate start, LocalDate end, LocalDate minDate) {
        List<Map<String, Object>> response = feeReceiptRepository.FeeReceiptListAll12(pageable, school_id, start, end, minDate);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
    }


    public FeeReceipt getFeeReceiptById(Integer fee_receipt_id) {
        return feeReceiptRepository.findById(fee_receipt_id)
                .orElseThrow(() -> new ResourceNotFoundException("vendor_Details Not Found:" + fee_receipt_id));
    }

    public FeeReceipt updateFeeReceipt(FeeReceipt frc) {
        return feeReceiptRepository.save(frc);
    }

    public void delete(Integer fee_receipt_id) {
        feeReceiptRepository.delete1(fee_receipt_id);
    }

    public void activateFeeReceipt(Integer fee_receipt_id) {
        feeReceiptRepository.activateFeeReceipt(fee_receipt_id);
    }

    public Integer getMaxId(Integer school_id, Integer financial_year_id, Integer hostel_status) {
        return feeReceiptRepository.getMaxId(school_id, financial_year_id, hostel_status);
    }

    public HashMap<String, Object> getAllDataOfFeeReceiptForFormating(Integer student_id) {

        HashMap<String, Object> formatedRersponse = new HashMap<>();
        HashMap<String, Object> dueAmount = new HashMap<>();
        Student_Details st = studentDetailsRepository.findById(student_id)
                .orElseThrow(() -> new ResourceNotFoundException("student_Details Not Found:" + student_id));
        List<HashMap<String, Object>> studentDetailsInformation = studentDetailsRepository
                .studentDetailsForFeeReceipt(student_id);
        List<HashMap<String, Object>> fts = feeTemplateSubAmountRepository
                .feeTemplateSubAmountForFeeReceipt(st.getFee_template_id());
        List<HashMap<String, Object>> fts1 = feeTemplateSubAmountRepository
                .feeTemplateSubAmountForFeeReceipt1(st.getFee_template_id());
        List<Map<String, Object>> fts2 = feeTemplateSubAmountRepository
                .feeTemplateSubAmountForFeeReceiptOnPaidAtBoard(st.getFee_template_id());
        List<HashMap<String, Object>> FeeReceipt = feeReceiptRepository.getAllDataOfFeeReceiptForFormating(student_id);
        List<HashMap<String, Object>> sas1 = scholarshipApprovalStatusRepository
                .scholarshipApprovalStatusForFeeReceipt(student_id);
        List<HashMap<String, Object>> tut_fee_report1 = tutionFeeWaiverRepository
                .tutionFeeWaiverForStudentLedger(student_id);
        List<HashMap<String, Object>> fee_template1 = feeTemplateRepository
                .feeTemplateDetailsForFeeReceiptDueAmount(st.getFee_template_id());
        formatedRersponse.put("Student_info", studentDetailsInformation);
        formatedRersponse.put("fee_template_sub_amount_info", fts);
        formatedRersponse.put("fee_receipt_student_pay_his", FeeReceipt);
        formatedRersponse.put("scholarship_approval_amount", sas1);
        formatedRersponse.put("tutionFeeWaiverReport", tut_fee_report1);
        formatedRersponse.put("fee_template_fixed_amount", fee_template1);
        // Float year1_amount=(Float) sas1.get(0).get("year1_amount");
        // Double paid_amount=(Double) FeeReceipt.get(0).get("paid_amount");
        // Integer year1_amt=(Integer) fts1.get(0).get("year1_amt");
        // formatedRersponse.put("due_Amt", year1_amt-year1_amount- paid_amount);
        Integer y = (Integer) studentDetailsInformation.get(0).get("number_of_years");
        if (((String) studentDetailsInformation.get(0).get("fee_template_program_type_name"))
                .equalsIgnoreCase("YEARLY")) {

            for (int i = 1; i <= y; i++) {

                if (((boolean) fts1.get(0).get("is_paid_at_board")) == true) {
                    logger.debug("Message For due amount--------------");

                    if (sas1.size() == 0) {
                        if (FeeReceipt.size() == 0) {
                            if (tut_fee_report1.size() == 0) {
                                System.out.println("**********************  1 **" + i);
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println("***************************" + paidamount + i);
                                Double x = ((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue();
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()));
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type Yearly if ================================================="
                                                + x);
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{"
                                                + paidamount);

                            } else {
                                System.out.println("************************* 2 ** " + i);
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println("*********************** 3 ** " + paidamount + i);
                                Double x = ((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type Yearly if ================================================="
                                                + x);
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{"
                                                + paidamount);
                            }

                        } else {

                            if (tut_fee_report1.size() == 0) {
                                System.out.println("*************************** 4 ***" + i);
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println("*************************** 5 **" + paidamount + i);
                                Double x = ((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                        - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                        (Integer) FeeReceipt.get(0).get("fee_receipt"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)));
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type Yearly if ================================================="
                                                + x);
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{"
                                                + paidamount);

                            } else {
                                System.out.println("*************************** 6 **" + i);
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println("*************************** 7 ***" + paidamount + i);
                                Double x = ((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                        - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                        (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type Yearly if ================================================="
                                                + x);
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{"
                                                + paidamount);

                            }

                        }
                    } else {
                        if (FeeReceipt.size() == 0) {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);

                                Double x = ((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                        - (Float) sas1.get(0).get("year" + i + "_amount");
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - (Float) sas1.get(0).get("year" + i + "_amount")));
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero else  program Type Yearly if ========================================= 8 == "
                                                + x);
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero schoolarship else  Statement program Type Yearly if{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 9 {{{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);

                                Double x = ((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                        - (Float) sas1.get(0).get("year" + i + "_amount")
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - (Float) sas1.get(0).get("year" + i + "_amount")
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero else  program Type Yearly if ===================================== 10 ==="
                                                + x);
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero schoolarship else  Statement program Type Yearly if{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 11 {{{{"
                                                + paidamount);

                            }

                        } else {

                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);

                                Double x = ((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                        - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                        (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                        - (Float) sas1.get(0).get("year" + i + "_amount");
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).intValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - (Float) sas1.get(0).get("year" + i + "_amount")));
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero else  program Type Yearly if ===================================== 12 ==="
                                                + x);
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero schoolarship else  Statement program Type Yearly if{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 13 {{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);

                                Double x = ((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                        - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                        (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                        - (Float) sas1.get(0).get("year" + i + "_amount")
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).intValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - (Float) sas1.get(0).get("year" + i + "_amount")
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero else  program Type Yearly if ===================================== 14 ===="
                                                + x);
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero schoolarship else  Statement program Type Yearly if{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 15 {{{{{"
                                                + paidamount);
                            }

                        }

                    }

                } else {
                    if (sas1.size() == 0) {
                        if (FeeReceipt.size() == 0) {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);

                                System.out.println(
                                        "****Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 16 {{{{"
                                                + paidamount + i);
                                Double x = ((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue();
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()));
                                System.out.println(
                                        "IF paid at booard false and scholarship zero if Statement program Type Yearly==================================== 17 ===="
                                                + x);
                                System.out.println(
                                        "IF paid at booard false and scholarship zero if Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 18 {{{{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println(
                                        "#####Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 19 {{{{"
                                                + paidamount + i);
                                Double x = ((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard false and scholarship zero if Statement program Type Yearly======================================= 20 ===="
                                                + x);
                                System.out.println(
                                        "IF paid at booard false and scholarship zero if Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 21 {{{{{{"
                                                + paidamount);
                            }

                        } else {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println(
                                        "***&&&Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 22 {{{"
                                                + paidamount + i);
                                Double x = ((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                        - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                        (Integer) FeeReceipt.get(0).get("fee_receipt"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)));
                                System.out.println(
                                        "IF paid at booard false and scholarship zero if Statement program Type Yearly=================================== 23 ===="
                                                + x);
                                System.out.println(
                                        "IF paid at booard false and scholarship zero if Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 24 {{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println(
                                        "Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 25 {{{{{{"
                                                + paidamount + i);
                                Double x = ((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                        - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                        (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard false and scholarship zero if Statement program Type Yearly=================================== 26 ===="
                                                + x);
                                System.out.println(
                                        "IF paid at booard false and scholarship zero if Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 27 {{{{{"
                                                + paidamount);
                            }

                        }

                    } else {
                        if (FeeReceipt.size() == 0) {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println(
                                        "Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 28 {{{{{{"
                                                + paidamount + i);
                                Double x = ((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                        - (Float) sas1.get(0).get("year" + i + "_amount");
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                                - (Float) sas1.get(0).get("year" + i + "_amount")));
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type Yearly if ================================ 29 ===="
                                                + x);
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 30 {{{{{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println(
                                        "Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{"
                                                + paidamount + i);
                                Double x = ((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                        - (Float) sas1.get(0).get("year" + i + "_amount")
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                                - (Float) sas1.get(0).get("year" + i + "_amount")
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type Yearly if =================================== 31 ====="
                                                + x);
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 32 {{{{{"
                                                + paidamount);
                            }

                        } else {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println(
                                        "Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 33 {{{{"
                                                + paidamount + i);
                                Double x = ((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                        - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                        (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                        - (Float) sas1.get(0).get("year" + i + "_amount");
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - (Float) sas1.get(0).get("year" + i + "_amount")));
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type Yearly if ================================= 34 ==="
                                                + x);
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 35 {{{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                System.out.println(
                                        "Else Statement program Type Yearly{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 36 {{{{{"
                                                + paidamount + i);
                                Double x = ((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                        - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                        (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                        - (Float) sas1.get(0).get("year" + i + "_amount")
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts1.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - (Float) sas1.get(0).get("year" + i + "_amount")
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type Yearly if ================================= 37 ===="
                                                + x);
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type Yearly if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 38 {{{{{"
                                                + paidamount);
                            }

                        }

                    }
                }
            }

        } else if (((String) studentDetailsInformation.get(0).get("fee_template_program_type_name"))
                .equalsIgnoreCase("SEMESTER")) {
            System.out.println("=============================SEMESTER=================================" + y);
            for (int i = 1; i <= (Integer) studentDetailsInformation.get(0).get("number_of_semester"); i++) {
                // int z=i-1;
                if (((boolean) fts1.get(0).get("is_paid_at_board")) == true) {
                    if (sas1.size() == 0) {
                        if (FeeReceipt.size() == 0) {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()));
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 39 {{{{{"
                                                + paidamount);
                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 40 {{{"
                                                + paidamount);
                            }

                        } else {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)));
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 50 {{{{"
                                                + paidamount);
                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard true and scholarship zero Statement program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 51 {{{{"
                                                + paidamount);
                            }

                        }

                    } else {
                        if (FeeReceipt.size() == 0) {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - (Float) sas1.get(0).get("year" + i + "_amount")));
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 52 {{{{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - (Float) sas1.get(0).get("year" + i + "_amount")
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 53 {{{{"
                                                + paidamount);
                            }

                        } else {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - (Float) sas1.get(0).get("year" + i + "_amount")));
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 54 {{{"
                                                + paidamount);
                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        (((Number) fts2.get(0).get("year" + i + "_amt")).doubleValue()
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - (Float) sas1.get(0).get("year" + i + "_amount")
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard true schoolarship size not zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 55 {{{"
                                                + paidamount);
                            }

                        }

                    }

                } else {

                    if (sas1.size() == 0) {
                        if (FeeReceipt.size() == 0) {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        ((Integer) fts1.get(0).get("year" + i + "_amt")));
                                System.out.println(
                                        "IF paid at booard false schoolarship size  zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 55 {{{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt", ((Integer) fts1.get(0).get("year" + i + "_amt")
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i)));
                                System.out.println(
                                        "IF paid at booard false schoolarship size  zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 56 {{{{{"
                                                + paidamount);
                            }

                        } else {

                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        ((Integer) fts1.get(0).get("year" + i + "_amt")
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)));
                                System.out.println(
                                        "IF paid at booard false schoolarship size  zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 57 {{{{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt", ((Integer) fts1.get(0).get("year" + i + "_amt")
                                        - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                        (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                        - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                        (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"), i)));
                                System.out.println(
                                        "IF paid at booard false schoolarship size  zero else  program Type semster if {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 58 {{{{{"
                                                + paidamount);
                            }

                        }

                    } else {
                        if (FeeReceipt.size() == 0) {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt", ((Integer) fts1.get(0).get("year" + i + "_amt")
                                        - (Float) sas1.get(0).get("year" + i + "_amount")));
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type semster {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 59 {{{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        ((Integer) fts1.get(0).get("year" + i + "_amt")
                                                - (Float) sas1.get(0).get("year" + i + "_amount")
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type semster {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 60 {{{{{"
                                                + paidamount);
                            }

                        } else {
                            if (tut_fee_report1.size() == 0) {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        ((Integer) fts1.get(0).get("year" + i + "_amt")
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - (Float) sas1.get(0).get("year" + i + "_amount")));
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type semster {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 61 {{{"
                                                + paidamount);

                            } else {
                                Integer paidamount = 0;// studentPaymentHistoryRepository.sumOfPaidAmount(student_id,(Integer)FeeReceipt.get(0).get("fee_receipt"),i);
                                dueAmount.put("year" + i + "_due_Amt",
                                        ((Integer) fts1.get(0).get("year" + i + "_amt")
                                                - studentPaymentHistoryRepository.sumOfPaidAmount(student_id,
                                                (Integer) FeeReceipt.get(0).get("fee_receipt"), i)
                                                - (Float) sas1.get(0).get("year" + i + "_amount")
                                                - tutionFeeWaiverReportRepository.waiverAmountForCalculationOfDueAmount(
                                                (Integer) tut_fee_report1.get(0).get("tution_fee_waiver_id"),
                                                i)));
                                System.out.println(
                                        "IF paid at booard false and scholarship non zero if Statement program Type semster {{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{{ 62 {{{{"
                                                + paidamount);
                            }
                        }

                    }
                }
            }
        }

        formatedRersponse.put("dueAmount", dueAmount);
        return formatedRersponse;
    }

    /**
     * By Default All Program Semester,so yearly validation is removed
     */

    public HashMap<String, Object> cmaDueAmountCalculationOnYearWiseForFeeReceipt(String auid) {

        Student_Details studentDetails = studentDetailsRepository.findByAuid(auid);

//		Student_Details studentDetails = studentDetailsRepository.findById(studentDetails.getStudent_id())
//				.orElseThrow(() -> new ResourceNotFoundException("student_Details Not Found:" + studentDetails.getStudent_id()));


        HashMap<String, Object> formatedRersponse = new HashMap<>();
        HashMap<String, Object> addOnFeeDetails = new HashMap<String, Object>();
        Map<String, Object> addOnFeePaidAmount = new HashMap<String, Object>();
        Map<String, Object> addOnFeeDueAmount = new HashMap<String, Object>();

        OtherFeeTemplateForStudentDTO addOnFeeData = otherFeeDetailsRepository.getAddOnProgramFeeDetailsByFeeTemplateId(
                studentDetails.getSchool_id(), studentDetails.getAc_year_id(), studentDetails.getProgram_id(),
                studentDetails.getFee_template_id());
        Map<String, Object> addOnDetails = getAddOn(addOnFeeData);

        for (int i = 1; i <= 12; i++) {

            Double addOnFeePaid = cmaFeeReceiptRepository.getCmaFeeReceiptForAddOnFeePaidDetails(i, studentDetails.getStudent_id()) == null ? 0.0 : cmaFeeReceiptRepository.getCmaFeeReceiptForAddOnFeePaidDetails(i, studentDetails.getStudent_id());
            addOnFeePaidAmount.put("sem" + i, addOnFeePaid);

            Double fixedAmount = (Double) addOnDetails.get("sem" + i) == null ? 0.0 : (Double) addOnDetails.get("sem" + i);
            Double dueAmount1 = fixedAmount - addOnFeePaid;
            addOnFeeDueAmount.put("sem" + i + "due", dueAmount1);
        }

        addOnFeeDetails.put("school_id", studentDetails.getSchool_id());
        addOnFeeDetails.put("student_id", studentDetails.getStudent_id());
        addOnFeeDetails.put("addOnFeeData", addOnFeeData);
        addOnFeeDetails.put("addOnFeeSemWisePaidAmount", addOnFeePaidAmount);
        addOnFeeDetails.put("addOnFeeSemWiseDueAmount", addOnFeeDueAmount);

        formatedRersponse.put("addOnFeeData", addOnFeeDetails);
        return formatedRersponse;
    }

    public HashMap<String, Object> uniformAndStationaryDueAmountCalculationOnYearWiseForFeeReceipt(String auid) {

        Student_Details studentDetails = studentDetailsRepository.findByAuid(auid);

        HashMap<String, Object> formatedRersponse = new HashMap<>();
        HashMap<String, Object> uniformAndStationaryData = new HashMap<String, Object>();
        Map<String, Object> uniformAndStationaryPaidAmount = new HashMap<String, Object>();
        Map<String, Object> uniformAndStationaryDueAmount = new HashMap<String, Object>();

        Map<String, Object> detailsData = otherFeeDetailsRepository.getOtherFeeDetailsForStudentLedger(studentDetails.getSchool_id(), studentDetails.getAc_year_id(),
                studentDetails.getProgram_id(), studentDetails.getProgram_specialization_id());

        for (int i = 1; i <= 12; i++) {

            double uniformAndStationaryPaid = studentDetailsService.getUniformPaid(i, studentDetails.getStudent_id()) == null ? 0.0 : studentDetailsService.getUniformPaid(i, studentDetails.getStudent_id());
            uniformAndStationaryPaidAmount.put("sem" + i + "paid", uniformAndStationaryPaid);

            Double fixedAmount = (Float) detailsData.get("sem" + i) == null ? 0.0 : (Float) detailsData.get("sem" + i);
            Double dueAmount1 = fixedAmount - uniformAndStationaryPaid;
            uniformAndStationaryDueAmount.put("sem" + i + "due", dueAmount1);
        }

        uniformAndStationaryData.put("school_id", studentDetails.getSchool_id());
        uniformAndStationaryData.put("student_id", studentDetails.getStudent_id());
        uniformAndStationaryData.put("otherFeeDetailsData", detailsData);
        uniformAndStationaryData.put("semWisePaidAmount", uniformAndStationaryPaidAmount);
        uniformAndStationaryData.put("semWiseDueAmount", uniformAndStationaryDueAmount);

        formatedRersponse.put("uniformAndStationaryData", uniformAndStationaryData);
        return formatedRersponse;
    }

    @SuppressWarnings("null")
    public HashMap<String, Object> dueAmountCalculationOnVocherHeadWiseAndYearWiseForFeeReceipt(Integer student_id) {

        HashMap<String, Object> formatedRersponse = new HashMap<>();
        HashMap<String, Object> dueAmount = new HashMap<>();
        HashMap<String, Object> feeTemplateSubAmountFormat = new HashMap<>();
        HashMap<String, Object> voucherHeadWiseFeeReceiptFormat = new HashMap<>();
        HashMap<String, Object> tutionFeeWaiverSubAmountFormat = new HashMap<>();

        Student_Details studentDetails = studentDetailsRepository.findById(student_id)
                .orElseThrow(() -> new ResourceNotFoundException("student_Details Not Found:" + student_id));
        List<HashMap<String, Object>> studentDetailsInformation = studentDetailsRepository
                .studentDetailsForFeeReceipt(student_id);
        List<HashMap<String, Object>> feeTemplateSubAmount = feeTemplateSubAmountRepository
                .feeTemplateSubAmountForFeeReceipt(studentDetails.getFee_template_id());
        List<HashMap<String, Object>> feeReceipt = feeReceiptRepository.getAllDataOfFeeReceiptForFormating(student_id);
        List<HashMap<String, Object>> scholarshipApprovalStatus = ObjectUtils
                .isNotEmpty(scholarshipApprovalStatusRepository.scholarshipApprovalStatusForFeeReceipt(student_id))
                ? scholarshipApprovalStatusRepository.scholarshipApprovalStatusForFeeReceipt(student_id)
                : null;
        List<HashMap<String, Object>> tutionFeeWaiver = tutionFeeWaiverRepository
                .tutionFeeWaiverForStudentLedger(student_id);
        List<Map<String, Object>> tutionFeeWaiverSubAmount = tutionFeeWaiverSubAmountRepository
                .getTutionFeeWaiverSubAmountOnStudentId(student_id);
        Map<String, Object> acerpAmountData = acerp_amount_repo.getAcerpAmountData(student_id);
        HashMap<String, HashMap<String, Object>> paidAtBoardData = getPaidAtBoardAmountForLedger(
                studentDetails.getStudent_id(), studentDetails.getFee_template_id());

        Map<String, Object> detailsData = otherFeeDetailsRepository.getOtherFeeDetailsForStudentLedger(studentDetails.getSchool_id(), studentDetails.getAc_year_id(),
                studentDetails.getProgram_id(), studentDetails.getProgram_specialization_id());

        OtherFeeTemplateForStudentDTO addOnFeeData = otherFeeDetailsRepository.getAddOnProgramFeeDetailsByFeeTemplateId(
                studentDetails.getSchool_id(), studentDetails.getAc_year_id(), studentDetails.getProgram_id(),
                studentDetails.getFee_template_id());
        Map<String, Object> addOnDetails = getAddOn(addOnFeeData);

        HashMap<String, Object> uniformAndStationaryData = new HashMap<String, Object>();
        Map<String, Object> uniformAndStationaryPaidAmount = new HashMap<String, Object>();
        Map<String, Object> uniformAndStationaryDueAmount = new HashMap<String, Object>();


        HashMap<String, Object> addOnFeeDetails = new HashMap<String, Object>();
        Map<String, Object> addOnFeePaidAmount = new HashMap<String, Object>();
        Map<String, Object> addOnFeeDueAmount = new HashMap<String, Object>();


        Map<String, Object> readmissionDataByStudentId = new HashMap<String, Object>();


//		Map<String, Object> hostelDueCalculationVocherHeadWise = new HashMap<String, Object>();
//		
//		Integer assignedHostelBedId = hostelBedAssignmentRepository.getAssignedHostelBedId(student_id, studentDetails.getAc_year_id());
//		if(assignedHostelBedId != null) {
//			hostelDueCalculationVocherHeadWise = hostelDueCalculationVocherHeadWiseForLedger(studentDetails.getAc_year_id(), student_id);
//		}

        if ((studentDetails.getRe_admission_status() == null ? 0 : studentDetails.getRe_admission_status()) == 1) {
            readmissionDataByStudentId = re_repo.getReadmissionDataByStudentId(student_id);
        }

        for (int i = 1; i <= 12; i++) {

            double uniformAndStationaryPaid = otherFeeDetailsRepository.getUniformAndStationaryPaid(i, student_id) == null ? 0.0 : otherFeeDetailsRepository.getUniformAndStationaryPaid(i, student_id);
            uniformAndStationaryPaidAmount.put("sem" + i + "paid", uniformAndStationaryPaid);

            Double fixedAmount = (Float) detailsData.get("sem" + i) == null ? 0.0 : (Float) detailsData.get("sem" + i);
            Double dueAmount1 = fixedAmount - uniformAndStationaryPaid;
            uniformAndStationaryDueAmount.put("sem" + i + "due", dueAmount1);
        }

        uniformAndStationaryData.put("otherFeeDetailsData", detailsData);
        uniformAndStationaryData.put("semWisePaidAmount", uniformAndStationaryPaidAmount);
        uniformAndStationaryData.put("semWiseDueAmount", uniformAndStationaryDueAmount);

        for (int i = 1; i <= 12; i++) {

            Double addOnFeePaid = cmaFeeReceiptRepository.getCmaFeeReceiptForAddOnFeePaidDetails(i, student_id) == null ? 0.0 : cmaFeeReceiptRepository.getCmaFeeReceiptForAddOnFeePaidDetails(i, student_id);
            addOnFeePaidAmount.put("sem" + i, addOnFeePaid);

            Double fixedAmount = (Double) addOnDetails.get("sem" + i) == null ? 0.0 : (Double) addOnDetails.get("sem" + i);
            Double dueAmount1 = fixedAmount - addOnFeePaid;
            addOnFeeDueAmount.put("sem" + i + "due", dueAmount1);
        }

        addOnFeeDetails.put("addOnFeeData", addOnFeeData);
        addOnFeeDetails.put("addOnFeeSemWisePaidAmount", addOnFeePaidAmount);
        addOnFeeDetails.put("addOnFeeSemWiseDueAmount", addOnFeeDueAmount);

        formatedRersponse.put("Student_info", studentDetailsInformation);
        formatedRersponse.put("fee_template_sub_amount_info", feeTemplateSubAmount);
        formatedRersponse.put("fee_receipt_student_pay_his", feeReceipt);
        formatedRersponse.put("scholarship_approval_amount", scholarshipApprovalStatus);
        formatedRersponse.put("tutionFeeWaiverReport", tutionFeeWaiver);
        formatedRersponse.put("acerpAmountData", acerpAmountData);
        formatedRersponse.put("paidAtBoardData", paidAtBoardData);
        formatedRersponse.put("uniformAndStationaryData", uniformAndStationaryData);
        formatedRersponse.put("addOnFeeData", addOnFeeDetails);
        formatedRersponse.put("readmissionData", readmissionDataByStudentId);
//		formatedRersponse.put("hostelDueCalculationVocherHeadWise", hostelDueCalculationVocherHeadWise); //discarded, and new separate api given

        List<Integer> scholarshipIds = preScholarshipRepository.getAllScholarshipIdOFStudent(student_id);
        System.out.println("scholarship ids ::::::::::::: " + scholarshipIds);
        Integer y = (Integer) studentDetailsInformation.get(0).get("number_of_semester");
        System.out.println("scholarship ids ::::::::::::: " + y);
        if (((String) studentDetailsInformation.get(0).get("fee_template_program_type_name"))
                .equalsIgnoreCase("YEARLY")) {

            System.out.println("information ::::::::::::: "
                    + (String) studentDetailsInformation.get(0).get("fee_template_program_type_name"));
            for (int i = 1; i <= y; i++) {
                HashMap<Integer, Object> vocherHeadWiseDueAmount = new HashMap<>();
                HashMap<Integer, Object> vocherHeadWiseFeeTemplateSubAmount = new HashMap<>();
                HashMap<Integer, Object> vocherHeadWiseFeeReceiptAmount = new HashMap<>();
                HashMap<Integer, Object> vocherHeadWiseTutionFeeWaiverSubAmount = new HashMap<>();

                if ((Boolean) feeTemplateSubAmount.get(0).get("Is_paid_at_board") == true) {
                    Integer yearOrSem = i;
                    calculationPaidAtBoardTrue(yearOrSem, feeTemplateSubAmount, studentDetails, acerpAmountData,
                            paidAtBoardData, studentDetails.getFee_template_id(), scholarshipApprovalStatus,
                            tutionFeeWaiverSubAmount, scholarshipIds, vocherHeadWiseDueAmount,
                            vocherHeadWiseFeeTemplateSubAmount, vocherHeadWiseTutionFeeWaiverSubAmount, feeReceipt,
                            vocherHeadWiseFeeReceiptAmount);
                } else {
                    Integer yearOrSem = i;
                    calculationPaidAtBoardFalse(yearOrSem, feeTemplateSubAmount, studentDetails, acerpAmountData,
                            paidAtBoardData, studentDetails.getFee_template_id(), scholarshipApprovalStatus,
                            tutionFeeWaiverSubAmount, scholarshipIds, vocherHeadWiseDueAmount,
                            vocherHeadWiseFeeTemplateSubAmount, vocherHeadWiseTutionFeeWaiverSubAmount, feeReceipt,
                            vocherHeadWiseFeeReceiptAmount);
                }
                dueAmount.put(String.valueOf(i), vocherHeadWiseDueAmount);
                feeTemplateSubAmountFormat.put(String.valueOf(i), vocherHeadWiseFeeTemplateSubAmount);
                tutionFeeWaiverSubAmountFormat.put(String.valueOf(i), vocherHeadWiseTutionFeeWaiverSubAmount);
                voucherHeadWiseFeeReceiptFormat.put(String.valueOf(i), vocherHeadWiseFeeReceiptAmount);
            }

        } else if (((String) studentDetailsInformation.get(0).get("fee_template_program_type_name"))
                .equalsIgnoreCase("SEMESTER")) {
            for (int i = 1; i <= (Integer) studentDetailsInformation.get(0).get("number_of_semester"); i++) {
                HashMap<Integer, Object> vocherHeadWiseDueAmount = new HashMap<>();
                HashMap<Integer, Object> vocherHeadWiseFeeTemplateSubAmount = new HashMap<>();
                HashMap<Integer, Object> vocherHeadWiseFeeReceiptAmount = new HashMap<>();
                HashMap<Integer, Object> vocherHeadWiseTutionFeeWaiverSubAmount = new HashMap<>();

                if ((Boolean) studentDetailsInformation.get(0).get("Is_paid_at_board") == true) {

                    Integer yearOrSem = i;
                    calculationPaidAtBoardTrue(yearOrSem, feeTemplateSubAmount, studentDetails, acerpAmountData,
                            paidAtBoardData, studentDetails.getFee_template_id(), scholarshipApprovalStatus,
                            tutionFeeWaiverSubAmount, scholarshipIds, vocherHeadWiseDueAmount,
                            vocherHeadWiseFeeTemplateSubAmount, vocherHeadWiseTutionFeeWaiverSubAmount, feeReceipt,
                            vocherHeadWiseFeeReceiptAmount);
                } else {
                    Integer yearOrSem = i;
                    calculationPaidAtBoardFalse(yearOrSem, feeTemplateSubAmount, studentDetails, acerpAmountData,
                            paidAtBoardData, studentDetails.getFee_template_id(), scholarshipApprovalStatus,
                            tutionFeeWaiverSubAmount, scholarshipIds, vocherHeadWiseDueAmount,
                            vocherHeadWiseFeeTemplateSubAmount, vocherHeadWiseTutionFeeWaiverSubAmount, feeReceipt,
                            vocherHeadWiseFeeReceiptAmount);
                }
                dueAmount.put(String.valueOf(i), vocherHeadWiseDueAmount);
                feeTemplateSubAmountFormat.put(String.valueOf(i), vocherHeadWiseFeeTemplateSubAmount);
                tutionFeeWaiverSubAmountFormat.put(String.valueOf(i), vocherHeadWiseTutionFeeWaiverSubAmount);
                voucherHeadWiseFeeReceiptFormat.put(String.valueOf(i), vocherHeadWiseFeeReceiptAmount);
            }

        }
        formatedRersponse.put("dueAmount", dueAmount);
        formatedRersponse.put("fee_template_sub_amount_format", feeTemplateSubAmountFormat);
        formatedRersponse.put("fee_receipt_student_pay_his_format", voucherHeadWiseFeeReceiptFormat);
        formatedRersponse.put("vocher_head_wise_tution_fee_waiver_sub_amount", tutionFeeWaiverSubAmountFormat);
        return formatedRersponse;

    }

    private void calculationPaidAtBoardFalse(Integer yearOrSem, List<HashMap<String, Object>> feeTemplateSubAmount,
                                             Student_Details studentDetails, Map<String, Object> addOnData,
                                             HashMap<String, HashMap<String, Object>> paidAtBoardData, Integer fee_template_id,
                                             List<HashMap<String, Object>> scholarshipApprovalStatus, List<Map<String, Object>> tutionFeeWaiverSubAmount,
                                             List<Integer> scholarshipIds, HashMap<Integer, Object> vocherHeadWiseDueAmount,
                                             HashMap<Integer, Object> vocherHeadWiseFeeTemplateSubAmount,
                                             HashMap<Integer, Object> vocherHeadWiseTutionFeeWaiverSubAmount, List<HashMap<String, Object>> FeeReceipt,
                                             HashMap<Integer, Object> vocherHeadWiseFeeReceiptAmount) {

        final int[] voucherHeadNewIdCount = {1};
        final double[] excessAmount = {0};
        feeTemplateSubAmount.stream().forEach(vhn -> {
            Double dueAmount = 0.0;
            if (yearOrSem == 1) {

//					Integer fixedAmount = (vhn.get("year"+yearOrSem+"_amt") != null ? ((Integer) vhn.get("year"+yearOrSem+"_amt")):0);
//					Double addOnValue = (addOnData.get("paidYear" + yearOrSem) == null) ? 0.0 : (Double) addOnData.get("paidYear" + yearOrSem);
//					Double paidAmount = studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(),studentDetails.getFee_template_id(),(Integer)vhn.get("voucher_head_new_id"),yearOrSem,studentDetails.getSchool_id());					

                Integer fixedAmount = Optional.ofNullable((Integer) vhn.get("year" + yearOrSem + "_amt")).orElse(0);
                Double addOnValue = Optional.ofNullable((Double) addOnData.get("paidYear" + yearOrSem)).orElse(0.0);
                Double paidAmount = Optional
                        .ofNullable(studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(), (Integer) vhn.get("voucher_head_new_id"),
                                yearOrSem, studentDetails.getSchool_id()))
                        .orElse(0.0);
                Double scholarshipAmount = (scholarshipApprovalStatus != null && !scholarshipApprovalStatus.isEmpty()
                        && scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount") != null)
                        ? ((Float) scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount"))
                        .doubleValue()
                        : 0.0;

//                if (voucherHeadNewIdCount[0] == 1) {
//                    dueAmount = fixedAmount - (scholarshipAmount + addOnValue + paidAmount);
//                    voucherHeadNewIdCount[0]++;
//                } else {
//                    dueAmount = fixedAmount - (paidAmount);
//                }
                if (voucherHeadNewIdCount[0] == 1) {
                    dueAmount = fixedAmount - (scholarshipAmount + addOnValue + paidAmount);

                    if (dueAmount < 0) {
                        excessAmount[0] = Math.abs(dueAmount); // carry forward this excess
                        dueAmount = 0.0d; // no due left for this round
                    }

                    voucherHeadNewIdCount[0]++;
                } else {
                    dueAmount = fixedAmount - paidAmount;

                    if (excessAmount[0] > 0) {
                        dueAmount -= excessAmount[0];
                        if (dueAmount < 0) {
                            excessAmount[0] = Math.abs(dueAmount); // still excess left
                            dueAmount = 0.0d;
                        } else {
                            excessAmount[0] = 0;
                        }
                    }
                }

                System.out.println((Integer) vhn.get("voucher_head_new_id") + "--->" + fixedAmount + " + " + addOnValue
                        + " + " + paidAmount + " + " + scholarshipAmount + " = " + dueAmount);
                vocherHeadWiseDueAmount.put((Integer) vhn.get("voucher_head_new_id"), dueAmount);

                feeTemplateSubAmount.stream()
                        .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                        .forEach(fsa -> {
                            vocherHeadWiseFeeTemplateSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                    fsa.get("year" + yearOrSem + "_amt"));
                        });

                if (tutionFeeWaiverSubAmount != null) {
                    tutionFeeWaiverSubAmount.stream()
                            .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                            .forEach(tfwsa1 -> {
                                vocherHeadWiseTutionFeeWaiverSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                        tfwsa1.get("year" + yearOrSem + "_amt"));
                            });
                }

            } else {

//					Integer fixedAmount = (vhn.get("year"+yearOrSem+"_amt") != null ? ((Integer) vhn.get("year"+yearOrSem+"_amt")):0);
//					Double addOnValue = (addOnData.get("paidYear" + yearOrSem) == null) ? 0.0 : (Double) addOnData.get("paidYear" + yearOrSem);
//					Double paidAmount = studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(),studentDetails.getFee_template_id(),(Integer)vhn.get("voucher_head_new_id"),yearOrSem,studentDetails.getSchool_id());

                Integer fixedAmount = Optional.ofNullable((Integer) vhn.get("year" + yearOrSem + "_amt")).orElse(0);
                Double addOnValue = Optional.ofNullable((Double) addOnData.get("paidYear" + yearOrSem)).orElse(0.0);
                Double paidAmount = Optional
                        .ofNullable(studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(), (Integer) vhn.get("voucher_head_new_id"),
                                yearOrSem, studentDetails.getSchool_id()))
                        .orElse(0.0);
                Double scholarshipAmount = (scholarshipApprovalStatus != null && !scholarshipApprovalStatus.isEmpty()
                        && scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount") != null)
                        ? ((Float) scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount"))
                        .doubleValue()
                        : 0.0;

//                if (voucherHeadNewIdCount[0] == 1) {
//                    dueAmount = fixedAmount - (scholarshipAmount + addOnValue + paidAmount);
//                    voucherHeadNewIdCount[0]++;
//                } else {
//                    dueAmount = fixedAmount - (paidAmount);
//                }

                if (voucherHeadNewIdCount[0] == 1) {
                    dueAmount = fixedAmount - (scholarshipAmount + addOnValue + paidAmount);

                    if (dueAmount < 0) {
                        excessAmount[0] = Math.abs(dueAmount); // carry forward this excess
                        dueAmount = 0.0d; // no due left for this round
                    }

                    voucherHeadNewIdCount[0]++;
                } else {
                    dueAmount = fixedAmount - paidAmount;

                    if (excessAmount[0] > 0) {
                        dueAmount -= excessAmount[0];
                        if (dueAmount < 0) {
                            excessAmount[0] = Math.abs(dueAmount); // still excess left
                            dueAmount = 0.0d;
                        } else {
                            excessAmount[0] = 0;
                        }
                    }
                }

                System.out.println((Integer) vhn.get("voucher_head_new_id") + "--->" + fixedAmount + " + " + addOnValue
                        + " + " + paidAmount + " + " + scholarshipAmount + " = " + dueAmount);
                vocherHeadWiseDueAmount.put((Integer) vhn.get("voucher_head_new_id"), dueAmount);

                feeTemplateSubAmount.stream()
                        .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                        .forEach(fsa -> {
                            vocherHeadWiseFeeTemplateSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                    fsa.get("year" + yearOrSem + "_amt"));
                        });

                if (tutionFeeWaiverSubAmount != null) {
                    tutionFeeWaiverSubAmount.stream()
                            .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                            .forEach(tfwsa1 -> {
                                vocherHeadWiseTutionFeeWaiverSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                        tfwsa1.get("year" + yearOrSem + "_amt"));
                            });
                }

            }

        });

        List<Integer> uniqueVoucherHeadIdList = FeeReceipt.stream().map(fr -> (Integer) fr.get("voucher_head_new_id"))
                .distinct().collect(Collectors.toList());

        uniqueVoucherHeadIdList.stream().forEach(feeByVhn -> {

            Double paidAmount = studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(), feeByVhn, yearOrSem, studentDetails.getSchool_id());
            vocherHeadWiseFeeReceiptAmount.put(feeByVhn, paidAmount);
        });

    }

    private void calculationPaidAtBoardTrue(Integer yearOrSem, List<HashMap<String, Object>> feeTemplateSubAmount,
                                            Student_Details studentDetails, Map<String, Object> addOnData,
                                            HashMap<String, HashMap<String, Object>> paidAtBoardData, Integer fee_template_id,
                                            List<HashMap<String, Object>> scholarshipApprovalStatus, List<Map<String, Object>> tutionFeeWaiverSubAmount,
                                            List<Integer> scholarshipIds, HashMap<Integer, Object> vocherHeadWiseDueAmount,
                                            HashMap<Integer, Object> vocherHeadWiseFeeTemplateSubAmount,
                                            HashMap<Integer, Object> vocherHeadWiseTutionFeeWaiverSubAmount, List<HashMap<String, Object>> FeeReceipt,
                                            HashMap<Integer, Object> vocherHeadWiseFeeReceiptAmount) {

        final int[] voucherHeadNewIdCount = {1};
        final double[] excessAmount = {0};
        feeTemplateSubAmount.stream().forEach(vhn -> {

            if ((Boolean) vhn.get("receive_for_all_year") == true) {

                Double dueAmount = 0.0;
                if (yearOrSem == 1) {

//					Integer fixedAmount = (vhn.get("year"+yearOrSem+"_amt") != null ? ((Integer) vhn.get("year"+yearOrSem+"_amt")):0);
//					Double addOnValue = (addOnData.get("paidYear" + yearOrSem) == null) ? 0.0 : (Double) addOnData.get("paidYear" + yearOrSem);
//					Double paidAtBoardAmount = (Double)feeTemplateSubAmountRepository.getPaidAtBoardValue((Integer) vhn.get("voucher_head_new_id"), yearOrSem , fee_template_id);
//					Double paidAmount = studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(),studentDetails.getFee_template_id(),(Integer)vhn.get("voucher_head_new_id"),yearOrSem,studentDetails.getSchool_id());

                    Integer fixedAmount = Optional.ofNullable((Integer) vhn.get("year" + yearOrSem + "_amt")).orElse(0);
                    Double addOnValue = Optional.ofNullable((Double) addOnData.get("paidYear" + yearOrSem)).orElse(0.0);
                    Double paidAtBoardAmount = Optional.ofNullable((Double) feeTemplateSubAmountRepository
                                    .getPaidAtBoardValue((Integer) vhn.get("voucher_head_new_id"), yearOrSem, fee_template_id))
                            .orElse(0.0);
                    Double paidAmount = Optional
                            .ofNullable(studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(), (Integer) vhn.get("voucher_head_new_id"),
                                    yearOrSem, studentDetails.getSchool_id()))
                            .orElse(0.0);
                    Double scholarshipAmount = (scholarshipApprovalStatus != null
                            && !scholarshipApprovalStatus.isEmpty()
                            && scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount") != null)
                            ? ((Float) scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount"))
                            .doubleValue()
                            : 0.0;

//                    if (voucherHeadNewIdCount[0] == 1) {
//                        dueAmount = fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount + scholarshipAmount);
//                        voucherHeadNewIdCount[0]++;
//                    } else {
//                        dueAmount = fixedAmount - (paidAtBoardAmount + paidAmount);
//                    }

                    if (voucherHeadNewIdCount[0] == 1) {
                        dueAmount = fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount + scholarshipAmount);

                        if (dueAmount < 0) {
                            excessAmount[0] = Math.abs(dueAmount); // carry forward this excess
                            dueAmount = 0.0d; // no due left for this round
                        }

                        voucherHeadNewIdCount[0]++;
                    } else {
                        dueAmount = fixedAmount - (paidAtBoardAmount + paidAmount);

                        if (excessAmount[0] > 0) {
                            dueAmount -= excessAmount[0];
                            if (dueAmount < 0) {
                                excessAmount[0] = Math.abs(dueAmount); // still excess left
                                dueAmount = 0.0d;
                            } else {
                                excessAmount[0] = 0;
                            }
                        }
                    }


                    System.out.println((Integer) vhn.get("voucher_head_new_id") + " voucher_head_new_id --->"
                            + fixedAmount + " - (" + addOnValue + "+" + paidAtBoardAmount + "+" + paidAmount + "+"
                            + scholarshipAmount + ") = " + dueAmount);

                    vocherHeadWiseDueAmount.put((Integer) vhn.get("voucher_head_new_id"), dueAmount);

                    feeTemplateSubAmount.stream()
                            .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                            .forEach(fsa -> {
                                vocherHeadWiseFeeTemplateSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                        fsa.get("year" + yearOrSem + "_amt"));
                            });

                    if (tutionFeeWaiverSubAmount != null) {
                        tutionFeeWaiverSubAmount.stream()
                                .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                .forEach(tfwsa1 -> {
                                    vocherHeadWiseTutionFeeWaiverSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                            tfwsa1.get("year" + yearOrSem + "_amt"));
                                });
                    }

                } else {
//					Integer fixedAmount = (vhn.get("year"+yearOrSem+"_amt") != null ? ((Integer) vhn.get("year"+yearOrSem+"_amt")):0);
//					Double addOnValue = (addOnData.get("paidYear" + yearOrSem) == null) ? 0.0 : (Double) addOnData.get("paidYear" + yearOrSem);
//					Double paidAtBoardAmount = (Double)feeTemplateSubAmountRepository.getPaidAtBoardValue((Integer) vhn.get("voucher_head_new_id"), yearOrSem , fee_template_id);
//					Double paidAmount = studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(),studentDetails.getFee_template_id(),(Integer)vhn.get("voucher_head_new_id"),yearOrSem,studentDetails.getSchool_id());

                    Integer fixedAmount = Optional.ofNullable((Integer) vhn.get("year" + yearOrSem + "_amt")).orElse(0);
                    Double addOnValue = Optional.ofNullable((Double) addOnData.get("paidYear" + yearOrSem)).orElse(0.0);
                    Double paidAtBoardAmount = Optional.ofNullable((Double) feeTemplateSubAmountRepository
                                    .getPaidAtBoardValue((Integer) vhn.get("voucher_head_new_id"), yearOrSem, fee_template_id))
                            .orElse(0.0);
                    Double paidAmount = Optional
                            .ofNullable(studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(), (Integer) vhn.get("voucher_head_new_id"),
                                    yearOrSem, studentDetails.getSchool_id()))
                            .orElse(0.0);
                    Double scholarshipAmount = (scholarshipApprovalStatus != null
                            && !scholarshipApprovalStatus.isEmpty()
                            && scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount") != null)
                            ? ((Float) scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount"))
                            .doubleValue()
                            : 0.0;

//                    if (voucherHeadNewIdCount[0] == 1) {
//                        dueAmount = fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount + scholarshipAmount);
//                        voucherHeadNewIdCount[0]++;
//                    } else {
//                        dueAmount = fixedAmount - (paidAtBoardAmount + paidAmount);
//                    }
                    if (voucherHeadNewIdCount[0] == 1) {
                        dueAmount = fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount + scholarshipAmount);

                        if (dueAmount < 0) {
                            excessAmount[0] = Math.abs(dueAmount); // carry forward this excess
                            dueAmount = 0.0d; // no due left for this round
                        }

                        voucherHeadNewIdCount[0]++;
                    } else {
                        dueAmount = fixedAmount - (paidAtBoardAmount + paidAmount);

                        if (excessAmount[0] > 0) {
                            dueAmount -= excessAmount[0];
                            if (dueAmount < 0) {
                                excessAmount[0] = Math.abs(dueAmount); // still excess left
                                dueAmount = 0.0d;
                            } else {
                                excessAmount[0] = 0;
                            }
                        }
                    }


                    System.out.println((Integer) vhn.get("voucher_head_new_id") + " voucher_head_new_id --->"
                            + fixedAmount + " - (" + addOnValue + "+" + paidAtBoardAmount + "+" + paidAmount + "+"
                            + scholarshipAmount + ") = " + dueAmount);
                    vocherHeadWiseDueAmount.put((Integer) vhn.get("voucher_head_new_id"), dueAmount);

                    feeTemplateSubAmount.stream()
                            .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                            .forEach(fsa -> {
                                vocherHeadWiseFeeTemplateSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                        fsa.get("year" + yearOrSem + "_amt"));
                            });

                    if (tutionFeeWaiverSubAmount != null) {
                        tutionFeeWaiverSubAmount.stream()
                                .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                .forEach(tfwsa1 -> {
                                    vocherHeadWiseTutionFeeWaiverSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                            tfwsa1.get("year" + yearOrSem + "_amt"));
                                });
                    }

                }
            } else {

                FeeTemplate feeTemplateData = feeTemplateRepository.getOne(fee_template_id);

                Boolean checkIsRegularOrNot = feeTemplateRepository
                        .checkIsRegularOrNot(feeTemplateData.getFee_admission_category_id());

                if (checkIsRegularOrNot == true) {

                    if (yearOrSem == 1) {

//							Integer fixedAmount = (vhn.get("year"+yearOrSem+"_amt") != null ? ((Integer) vhn.get("year"+yearOrSem+"_amt")):0);
//							Double addOnValue = (addOnData.get("paidYear" + yearOrSem) == null) ? 0.0 : (Double) addOnData.get("paidYear" + yearOrSem);
//							Double paidAtBoardAmount = (Double)feeTemplateSubAmountRepository.getPaidAtBoardValue((Integer) vhn.get("voucher_head_new_id"), yearOrSem , fee_template_id);
//							Double paidAmount = studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(),studentDetails.getFee_template_id(),(Integer)vhn.get("voucher_head_new_id"),yearOrSem,studentDetails.getSchool_id());
                        Double dueAmount = 0.0;
                        Integer fixedAmount = Optional.ofNullable((Integer) vhn.get("year" + yearOrSem + "_amt"))
                                .orElse(0);
                        Double addOnValue = Optional.ofNullable((Double) addOnData.get("paidYear" + yearOrSem))
                                .orElse(0.0);
                        Double paidAtBoardAmount = Optional
                                .ofNullable((Double) feeTemplateSubAmountRepository.getPaidAtBoardValue(
                                        (Integer) vhn.get("voucher_head_new_id"), yearOrSem, fee_template_id))
                                .orElse(0.0);
                        Double paidAmount = Optional.ofNullable(studentPaymentHistoryRepository.getPaidAmount(
                                        studentDetails.getStudent_id(), (Integer) vhn.get("voucher_head_new_id"), yearOrSem, studentDetails.getSchool_id()))
                                .orElse(0.0);
                        Double scholarshipAmount = (scholarshipApprovalStatus != null
                                && !scholarshipApprovalStatus.isEmpty()
                                && scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount") != null)
                                ? ((Float) scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount"))
                                .doubleValue()
                                : 0.0;

//                        if (voucherHeadNewIdCount[0] == 1) {
//                            dueAmount = fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount + scholarshipAmount);
//                            voucherHeadNewIdCount[0]++;
//                        } else {
//                            dueAmount = fixedAmount - (paidAtBoardAmount + paidAmount);
//                        }
                        if (voucherHeadNewIdCount[0] == 1) {
                            dueAmount = fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount + scholarshipAmount);

                            if (dueAmount < 0) {
                                excessAmount[0] = Math.abs(dueAmount); // carry forward this excess
                                dueAmount = 0.0d; // no due left for this round
                            }

                            voucherHeadNewIdCount[0]++;
                        } else {
                            dueAmount = fixedAmount - (paidAtBoardAmount + paidAmount);

                            if (excessAmount[0] > 0) {
                                dueAmount -= excessAmount[0];
                                if (dueAmount < 0) {
                                    excessAmount[0] = Math.abs(dueAmount); // still excess left
                                    dueAmount = 0.0d;
                                } else {
                                    excessAmount[0] = 0;
                                }
                            }
                        }

                        System.out.println((Integer) vhn.get("voucher_head_new_id") + " voucher_head_new_id --->"
                                + fixedAmount + " - (" + addOnValue + "+" + paidAtBoardAmount + "+" + paidAmount + "+"
                                + scholarshipAmount + ") = " + dueAmount);

                        vocherHeadWiseDueAmount.put((Integer) vhn.get("voucher_head_new_id"), dueAmount);

                        feeTemplateSubAmount.stream()
                                .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                .forEach(fsa -> {
                                    vocherHeadWiseFeeTemplateSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                            fsa.get("year" + yearOrSem + "_amt"));
                                });

                        if (tutionFeeWaiverSubAmount != null) {
                            tutionFeeWaiverSubAmount.stream().filter(
                                            e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                    .forEach(tfwsa1 -> {
                                        vocherHeadWiseTutionFeeWaiverSubAmount.put(
                                                (Integer) vhn.get("voucher_head_new_id"),
                                                tfwsa1.get("year" + yearOrSem + "_amt"));
                                    });
                        }
                    } else {

                        Double dueAmount = 0.0;
                        Integer fixedAmount = Optional.ofNullable((Integer) vhn.get("year" + yearOrSem + "_amt"))
                                .orElse(0);
                        Double addOnValue = Optional.ofNullable((Double) addOnData.get("paidYear" + yearOrSem))
                                .orElse(0.0);
                        Double paidAtBoardAmount = Optional
                                .ofNullable((Double) feeTemplateSubAmountRepository.getPaidAtBoardValue(
                                        (Integer) vhn.get("voucher_head_new_id"), yearOrSem, fee_template_id))
                                .orElse(0.0);
                        Double paidAmount = Optional.ofNullable(studentPaymentHistoryRepository.getPaidAmount(
                                        studentDetails.getStudent_id(), (Integer) vhn.get("voucher_head_new_id"), yearOrSem, studentDetails.getSchool_id()))
                                .orElse(0.0);
                        Double scholarshipAmount = (scholarshipApprovalStatus != null
                                && !scholarshipApprovalStatus.isEmpty()
                                && scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount") != null)
                                ? ((Float) scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount"))
                                .doubleValue()
                                : 0.0;

//                        if (voucherHeadNewIdCount[0] == 1) {
//                            // dueAmount=fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount +
//                            // scholarshipAmount);
//                            dueAmount = fixedAmount - (addOnValue + paidAmount + scholarshipAmount);
//                            voucherHeadNewIdCount[0]++;
//                        } else {
//                            // dueAmount=fixedAmount - (paidAtBoardAmount + paidAmount);
//                            dueAmount = fixedAmount - (paidAmount);
//                        }

                        if (voucherHeadNewIdCount[0] == 1) {
                            dueAmount = fixedAmount - (addOnValue + paidAmount + scholarshipAmount);

                            if (dueAmount < 0) {
                                excessAmount[0] = Math.abs(dueAmount); // carry forward this excess
                                dueAmount = 0.0d; // no due left for this round
                            }

                            voucherHeadNewIdCount[0]++;
                        } else {
                            dueAmount = fixedAmount - (paidAmount);

                            if (excessAmount[0] > 0) {
                                dueAmount -= excessAmount[0];
                                if (dueAmount < 0) {
                                    excessAmount[0] = Math.abs(dueAmount); // still excess left
                                    dueAmount = 0.0d;
                                } else {
                                    excessAmount[0] = 0;
                                }
                            }
                        }

                        System.out.println((Integer) vhn.get("voucher_head_new_id") + " voucher_head_new_id --->"
                                + fixedAmount + " - (" + addOnValue + "+" + paidAtBoardAmount + "+" + paidAmount + "+"
                                + scholarshipAmount + ") = " + dueAmount);
                        vocherHeadWiseDueAmount.put((Integer) vhn.get("voucher_head_new_id"), dueAmount);

                        feeTemplateSubAmount.stream()
                                .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                .forEach(fsa -> {
                                    vocherHeadWiseFeeTemplateSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                            fsa.get("year" + yearOrSem + "_amt"));
                                });

                        if (tutionFeeWaiverSubAmount != null) {
                            tutionFeeWaiverSubAmount.stream().filter(
                                            e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                    .forEach(tfwsa1 -> {
                                        vocherHeadWiseTutionFeeWaiverSubAmount.put(
                                                (Integer) vhn.get("voucher_head_new_id"),
                                                tfwsa1.get("year" + yearOrSem + "_amt"));
                                    });
                        }
                    }

                } else {
                    Integer lat_year_sem = feeTemplateData.getLat_year_sem();
                    if (yearOrSem == lat_year_sem) {
                        Double dueAmount = 0.0;
                        Integer fixedAmount = Optional.ofNullable((Integer) vhn.get("year" + yearOrSem + "_amt"))
                                .orElse(0);
                        Double addOnValue = Optional.ofNullable((Double) addOnData.get("paidYear" + yearOrSem))
                                .orElse(0.0);
                        Double paidAtBoardAmount = Optional
                                .ofNullable((Double) feeTemplateSubAmountRepository.getPaidAtBoardValue(
                                        (Integer) vhn.get("voucher_head_new_id"), yearOrSem, fee_template_id))
                                .orElse(0.0);
                        Double paidAmount = Optional.ofNullable(studentPaymentHistoryRepository.getPaidAmount(
                                        studentDetails.getStudent_id(), (Integer) vhn.get("voucher_head_new_id"), yearOrSem, studentDetails.getSchool_id()))
                                .orElse(0.0);
                        Double scholarshipAmount = (scholarshipApprovalStatus != null
                                && !scholarshipApprovalStatus.isEmpty()
                                && scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount") != null)
                                ? ((Float) scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount"))
                                .doubleValue()
                                : 0.0;

//                        if (voucherHeadNewIdCount[0] == 1) {
//                            dueAmount = fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount + scholarshipAmount);
////								dueAmount=fixedAmount - (addOnValue  + paidAmount + scholarshipAmount);
//                            voucherHeadNewIdCount[0]++;
//                        } else {
//                            dueAmount = fixedAmount - (paidAtBoardAmount + paidAmount);
////								dueAmount=fixedAmount - (paidAmount);
//                        }

                        if (voucherHeadNewIdCount[0] == 1) {
                            dueAmount = fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount + scholarshipAmount);

                            if (dueAmount < 0) {
                                excessAmount[0] = Math.abs(dueAmount); // carry forward this excess
                                dueAmount = 0.0d; // no due left for this round
                            }

                            voucherHeadNewIdCount[0]++;
                        } else {
                            dueAmount = fixedAmount - (paidAtBoardAmount + paidAmount);

                            if (excessAmount[0] > 0) {
                                dueAmount -= excessAmount[0];
                                if (dueAmount < 0) {
                                    excessAmount[0] = Math.abs(dueAmount); // still excess left
                                    dueAmount = 0.0d;
                                } else {
                                    excessAmount[0] = 0;
                                }
                            }
                        }

                        System.out.println((Integer) vhn.get("voucher_head_new_id") + " voucher_head_new_id --->"
                                + fixedAmount + " - (" + addOnValue + "+" + paidAtBoardAmount + "+" + paidAmount + "+"
                                + scholarshipAmount + ") = " + dueAmount);
                        vocherHeadWiseDueAmount.put((Integer) vhn.get("voucher_head_new_id"), dueAmount);

                        feeTemplateSubAmount.stream()
                                .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                .forEach(fsa -> {
                                    vocherHeadWiseFeeTemplateSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                            fsa.get("year" + yearOrSem + "_amt"));
                                });

                        if (tutionFeeWaiverSubAmount != null) {
                            tutionFeeWaiverSubAmount.stream().filter(
                                            e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                    .forEach(tfwsa1 -> {
                                        vocherHeadWiseTutionFeeWaiverSubAmount.put(
                                                (Integer) vhn.get("voucher_head_new_id"),
                                                tfwsa1.get("year" + yearOrSem + "_amt"));
                                    });
                        }
                    } else {
                        Double dueAmount = 0.0;
                        Integer fixedAmount = Optional.ofNullable((Integer) vhn.get("year" + yearOrSem + "_amt"))
                                .orElse(0);
                        Double addOnValue = Optional.ofNullable((Double) addOnData.get("paidYear" + yearOrSem))
                                .orElse(0.0);
                        Double paidAtBoardAmount = Optional
                                .ofNullable((Double) feeTemplateSubAmountRepository.getPaidAtBoardValue(
                                        (Integer) vhn.get("voucher_head_new_id"), yearOrSem, fee_template_id))
                                .orElse(0.0);
                        Double paidAmount = Optional.ofNullable(studentPaymentHistoryRepository.getPaidAmount(
                                        studentDetails.getStudent_id(), (Integer) vhn.get("voucher_head_new_id"), yearOrSem, studentDetails.getSchool_id()))
                                .orElse(0.0);
                        Double scholarshipAmount = (scholarshipApprovalStatus != null
                                && !scholarshipApprovalStatus.isEmpty()
                                && scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount") != null)
                                ? ((Float) scholarshipApprovalStatus.get(0).get("year" + yearOrSem + "_amount"))
                                .doubleValue()
                                : 0.0;

//                        if (voucherHeadNewIdCount[0] == 1) {
////								dueAmount=fixedAmount - (addOnValue + paidAtBoardAmount + paidAmount + scholarshipAmount);
//                            dueAmount = fixedAmount - (addOnValue + paidAmount + scholarshipAmount);
//                            voucherHeadNewIdCount[0]++;
//                        } else {
////								dueAmount=fixedAmount - (paidAtBoardAmount + paidAmount);
//                            dueAmount = fixedAmount - (paidAmount);
//                        }

                        if (voucherHeadNewIdCount[0] == 1) {
                            dueAmount = fixedAmount - (addOnValue + paidAmount + scholarshipAmount);

                            if (dueAmount < 0) {
                                excessAmount[0] = Math.abs(dueAmount); // carry forward this excess
                                dueAmount = 0.0d; // no due left for this round
                            }

                            voucherHeadNewIdCount[0]++;
                        } else {
                            dueAmount = fixedAmount - (paidAmount);

                            if (excessAmount[0] > 0) {
                                dueAmount -= excessAmount[0];
                                if (dueAmount < 0) {
                                    excessAmount[0] = Math.abs(dueAmount); // still excess left
                                    dueAmount = 0.0d;
                                } else {
                                    excessAmount[0] = 0;
                                }
                            }
                        }

                        System.out.println((Integer) vhn.get("voucher_head_new_id") + " voucher_head_new_id --->"
                                + fixedAmount + " - (" + addOnValue + "+" + paidAtBoardAmount + "+" + paidAmount + "+"
                                + scholarshipAmount + ") = " + dueAmount);
                        vocherHeadWiseDueAmount.put((Integer) vhn.get("voucher_head_new_id"), dueAmount);

                        feeTemplateSubAmount.stream()
                                .filter(e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                .forEach(fsa -> {
                                    vocherHeadWiseFeeTemplateSubAmount.put((Integer) vhn.get("voucher_head_new_id"),
                                            fsa.get("year" + yearOrSem + "_amt"));
                                });

                        if (tutionFeeWaiverSubAmount != null) {
                            tutionFeeWaiverSubAmount.stream().filter(
                                            e -> e.get("voucher_head_new_id") == (Integer) vhn.get("voucher_head_new_id"))
                                    .forEach(tfwsa1 -> {
                                        vocherHeadWiseTutionFeeWaiverSubAmount.put(
                                                (Integer) vhn.get("voucher_head_new_id"),
                                                tfwsa1.get("year" + yearOrSem + "_amt"));
                                    });
                        }
                    }
                }

            }
        });

        List<Integer> uniqueVoucherHeadIdList = FeeReceipt.stream().map(fr -> (Integer) fr.get("voucher_head_new_id"))
                .distinct().collect(Collectors.toList());

        uniqueVoucherHeadIdList.stream().forEach(feeByVhn -> {

            Double paidAmount = studentPaymentHistoryRepository.getPaidAmount(studentDetails.getStudent_id(), feeByVhn, yearOrSem, studentDetails.getSchool_id());
            vocherHeadWiseFeeReceiptAmount.put(feeByVhn, paidAmount);
        });
    }


    private Map<String, Object> getAddOn(OtherFeeTemplateForStudentDTO addOn) {
        Map<String, Object> addonMap = new LinkedHashMap<>();

        addonMap.put("sem1",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem1()) ? addOn.getSem1() : 0.0);
        addonMap.put("sem2",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem2()) ? addOn.getSem2() : 0.0);
        addonMap.put("sem3",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem3()) ? addOn.getSem3() : 0.0);
        addonMap.put("sem4",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem4()) ? addOn.getSem4() : 0.0);
        addonMap.put("sem5",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem5()) ? addOn.getSem5() : 0.0);
        addonMap.put("sem6",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem6()) ? addOn.getSem6() : 0.0);
        addonMap.put("sem7",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem7()) ? addOn.getSem7() : 0.0);
        addonMap.put("sem8",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem8()) ? addOn.getSem8() : 0.0);
        addonMap.put("sem9",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem9()) ? addOn.getSem9() : 0.0);
        addonMap.put("sem10",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem10()) ? addOn.getSem10() : 0.0);
        addonMap.put("sem11",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem11()) ? addOn.getSem11() : 0.0);
        addonMap.put("sem12",
                ObjectUtils.isNotEmpty(addOn) && ObjectUtils.isNotEmpty(addOn.getSem12()) ? addOn.getSem12() : 0.0);
        return addonMap;
    }

    public HashMap<String, HashMap<String, Object>> getPaidAtBoardAmountForLedger(Integer student_id,
                                                                                  Integer fee_template_id) {

        FeeTemplate feeTemplateData = feeTemplateRepository.findById(fee_template_id).orElse(null);

        HashMap<String, HashMap<String, Object>> paidAtBoardData = new HashMap<String, HashMap<String, Object>>();
        if (feeTemplateData.getIs_paid_at_board() == true) {
            List<Map<String, Object>> feeTemplateSubAmountForLedger = feeTemplateSubAmountRepository
                    .feeTemplateSubAmountForFeeReceiptPaidAtBoardForLedger(fee_template_id);

            feeTemplateSubAmountForLedger.stream().forEach(ftsa -> {
                HashMap<String, Object> data = new HashMap<String, Object>();

                if ((Boolean) ftsa.get("receive_for_all_year") == false) {

                    Boolean checkIsRegularOrNot = feeTemplateRepository
                            .checkIsRegularOrNot(feeTemplateData.getFee_admission_category_id());

                    if (checkIsRegularOrNot) {
                        data.put("year1_amt", ftsa.get("year1_amt"));
                    } else {
                        Integer lat_year_sem = feeTemplateData.getLat_year_sem();
                        data.put("year" + lat_year_sem + "_amt", ftsa.get("year" + lat_year_sem + "_amt"));
                    }

                } else {

                    data.put("year1_amt", ftsa.get("year1_amt"));
                    data.put("year2_amt", ftsa.get("year2_amt"));
                    data.put("year3_amt", ftsa.get("year3_amt"));
                    data.put("year4_amt", ftsa.get("year4_amt"));
                    data.put("year5_amt", ftsa.get("year5_amt"));
                    data.put("year6_amt", ftsa.get("year6_amt"));
                    data.put("year7_amt", ftsa.get("year7_amt"));
                    data.put("year8_amt", ftsa.get("year8_amt"));
                    data.put("year9_amt", ftsa.get("year9_amt"));
                    data.put("year10_amt", ftsa.get("year10_amt"));
                    data.put("year11_amt", ftsa.get("year11_amt"));
                    data.put("year12_amt", ftsa.get("year12_amt"));

                }
                paidAtBoardData.put(ftsa.get("voucher_head_new_id").toString(), data);
            });

        }
        return paidAtBoardData;
    }

    public HashMap<String, Object> getDataForDisplayingFeeReceipt1(Integer student_id, Integer financial_year_id,
                                                                   String fee_receipt, String transaction_type, Integer hostel_status) {

        HashMap<String, Object> hs2 = new HashMap<String, Object>();
        List<Map<String, Object>> hs1 = new ArrayList<Map<String, Object>>();

        String fee_receipt1 = fee_receipt.replace('_', '/');
        // Double som_value =
        // dollar_to_uzb_currency_conv_repo.getDollarToUzbekistaniCurrencyConversion().getUz_som();

        if (transaction_type.equalsIgnoreCase("CASH") || transaction_type.equalsIgnoreCase("RTGS")
                || transaction_type.equalsIgnoreCase("DD") || transaction_type.equalsIgnoreCase("ONLINE") || transaction_type.equalsIgnoreCase("p_gateway")) {

            List<Integer> voucher_ids = studentPaymentHistoryRepository.getVoucherHeadNewId(student_id,
                    financial_year_id, fee_receipt1, transaction_type, hostel_status);
            Integer maxPaidYear = studentPaymentHistoryRepository.maxPaidYearOfStudent(student_id, financial_year_id,
                    fee_receipt1, transaction_type, hostel_status);
            voucher_ids.stream().forEach(vou -> {
                HashMap<String, Object> hs = new HashMap<String, Object>();
                List<Integer> ids = studentPaymentHistoryRepository.getPaidYears(student_id, financial_year_id,
                        fee_receipt1, transaction_type, hostel_status, vou);
                for (int i = 1; i <= maxPaidYear; i++) {
                    if (!(ids.contains(i))) {
                        ids.add(i);
                    }
                }
                ids.stream().forEach(paid_year -> {
                    Double paid_amount = studentPaymentHistoryRepository.getAmount(student_id, financial_year_id,
                            fee_receipt1, transaction_type, hostel_status, paid_year, vou);
                    hs.put(paid_year.toString(), paid_amount == null ? 0 : paid_amount);
                });
                hs2.put(vou.toString(), hs);
            });
            List<Map<String, Object>> data = studentPaymentHistoryRepository.getDataForDisplayingFeeReceipt11(
                    student_id, financial_year_id, fee_receipt1, transaction_type, hostel_status);
            hs2.put("student_details", data);
            return hs2;
        } else {
            throw new RuntimeException("transaction_type is not in cash, RTGS, DD, Or Online ");
        }
    }

    public HashMap<String, Object> getDataForDisplayingAndCancelFeeReceipt(Integer financial_year_id, Integer school_id,
                                                                           String fee_receipt) {
        String rcpt_type = feeReceiptRepository.getReceiptType(fee_receipt, financial_year_id,school_id);

        HashMap<String, Object> hs = new HashMap<String, Object>();
        HashMap<String, Object> hs2 = new HashMap<String, Object>();

        if (rcpt_type.equalsIgnoreCase("General")) {
            List<Integer> voucher_ids = studentPaymentHistoryRepository.getVoucherHeadNewId(financial_year_id,
                    school_id, fee_receipt);
            voucher_ids.stream().forEach(vou -> {
                List<Integer> ids = studentPaymentHistoryRepository.getPaidYears(financial_year_id, school_id,
                        fee_receipt, vou);
                ids.stream().forEach(paid_year -> {
                    Integer paid_amount = studentPaymentHistoryRepository.getAmount(financial_year_id, school_id,
                            fee_receipt, paid_year, vou);
                    hs.put(paid_year.toString(), paid_amount);
                });
                hs2.put(vou.toString(), hs);
            });

            List<Map<String, Object>> data = studentPaymentHistoryRepository
                    .getDataForDisplayingAndCancelFeeReceipt(financial_year_id, school_id, fee_receipt);
            List<Map<String, Object>> data1 = studentPaymentHistoryRepository
                    .getDataForDisplayingAndCancelFeeReceipt1(financial_year_id, school_id, fee_receipt);
            hs2.put("student_details", data);
            hs2.put("payment_details", data1);

            return hs2;

        } else {
            List<Map<String, Object>> bulk_fee_data = feeReceiptRepository
                    .getDataForDisplayingBulkFeeReceiptAndCancel(financial_year_id, school_id, fee_receipt);
            List<Map<String, Object>> student_data = feeReceiptRepository
                    .getDataForDisplayingStudentDetailsBulkFeeRcptAndCancel(fee_receipt, school_id,financial_year_id);
            System.out.println("54534545454545454545454545454 " + bulk_fee_data);
            hs.put("Voucher_Head_Wise_Amount", bulk_fee_data);
            hs.put("student_details", student_data);
            return hs;
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public String cancelFeeReceipt1(CancelledFeeReceipts cancelReceipt, String jwtToken)
            throws JsonParseException, JsonMappingException, IOException {
        JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
        Integer bankImportId = feeReceiptRepository.getBankImportByFeeReceiptId(cancelReceipt.getFee_receipt_id());
        List<FeeReceipt> feeReceipts = feeReceiptRepository.getFeeReceiptsByBankImportId(bankImportId);
        for (FeeReceipt receipt : feeReceipts)
        {

            CancelledFeeReceipts cfr = new CancelledFeeReceipts();
            cfr.setActive(Boolean.TRUE);
            cfr.setFee_receipt(receipt.getFee_receipt());
            cfr.setFee_receipt_id(receipt.getFee_receipt_id());
            cfr.setAmount(Double.valueOf(receipt.getPaid_amount()));
            Bank bank = bankRepository.findByBankID(receipt.getBank_id());
            cfr.setBank_name(bank.getBank_name());
            Optional<BankImportTransaction> bankImportTransaction = bankImportTransactionRepository.findById(bankImportId);
            bankImportTransaction.ifPresent(importTransaction -> cfr.setCheque_dd_no(importTransaction.getCheque_dd_no()));
            cfr.setCreated_by(jwtDetails.getUserId());
            cfr.setCreated_username(jwtDetails.getUserName());
            cfr.setStudent_id(receipt.getStudent_id());
            cfr.setSchool_id(receipt.getSchool_id());
            cfr.setFinancial_year_id(receipt.getFinancial_year_id());
            cfr.setRemarks(cancelReceipt.getRemarks());
            FeeTemplate template = feeTemplateRepository.getFeeTemplateByStudentId(receipt.getStudent_id());
            if (template != null) {
                cfr.setFee_template(template.getFee_template_name());
            }
            cfr.setTransaction_type(receipt.getTransaction_type());
            feeReceiptRepository.cancelFeeReceipt1(cfr.getFee_receipt_id());
        tallyReceiptRepository.updateTallyReceipt(cfr.getFee_receipt_id());
        studentPaymentHistoryRepository.updateStudentPaymentHistory(cfr.getFee_receipt_id());

        if (cfr.getTransaction_type().equalsIgnoreCase("RTGS") || cfr.getTransaction_type().equalsIgnoreCase("p_gateway")) {
            rtgsFeeHistoryRepository.updateRTGSFeeHistory(cfr.getFee_receipt(), cfr.getFinancial_year_id(), cfr.getSchool_id());
            Double balance = bankImportTransactionRepository.getBalanceAmount(cfr.getFee_receipt(),
                    cfr.getFinancial_year_id(), cfr.getSchool_id()) == null ? 0.0
                    : bankImportTransactionRepository.getBalanceAmount(cfr.getFee_receipt(),
                    cfr.getFinancial_year_id(), cfr.getSchool_id());
            Double new_balance = cfr.getAmount() + balance;
            bankImportTransaction.ifPresent(importTransaction -> {
                if(new_balance <= importTransaction.getAmount())
                {
                    bankImportTransactionRepository.updateBalanceAmountInBankImportTransaction(new_balance,
                            cfr.getFee_receipt(), cfr.getFinancial_year_id(), cfr.getSchool_id());
                }
            });

        }
        cfr.setCreated_by(jwtDetails.getUserId());
        cfr.setCreated_username(jwtDetails.getUserName());

      // Optional<BankImportTransaction> bankImportTransaction = bankImportTransactionRepository.findById(bankImportId);
       switch(receipt.getReceipt_type().toUpperCase()) {
           case "BULK FEE":
           case "BULK":
           {
               bulkFeeReceiptRepository.updateBulkFeeReceiptByBulkId(Integer.valueOf(receipt.getBulk_id()));
               cfr.setBulk_fee_receipt(Integer.valueOf(receipt.getBulk_id()));
           }
           break;

           case "REGISTRATION FEE": bankImportTransaction.ifPresent(importTransaction -> registrationFeeTrsactionRepository.updateRegistrationFeeTransactionByBankImportTransactionId(importTransaction.getOrder_id()));
           break;

           case "EXAM FEE": examFeeReceiptRepository.deactivateExamReceiptByFeeReceiptId(receipt.getFee_receipt_id());
           break;
       }
       cancelledFeeReceiptsRepository.save(cfr);


    }
        return "Receipt cancelled successfully";
    }

    public void updationOfBankTransactionHistoryId() {
        List<HashMap<String, Object>> receiptNoAndBankImportTransactionIdData = bankImportTransactionRepository
                .getReceiptNoAndBankImportTransactionId();
        receiptNoAndBankImportTransactionIdData.stream().forEach(receiptNoAndBankImportTransactionId -> {
            feeReceiptRepository.updationOfBankTransactionHistoryId(
                    (Integer) receiptNoAndBankImportTransactionId.get("bank_import_transaction_id"),
                    receiptNoAndBankImportTransactionId.get("receipt_no").toString());
        });

    }

    public void feeReceiptTransfer(List<StudentPaymentHistory> studentPaymentHistory, Integer oldStudentId,
                                   Integer newStudentId) {
        studentPaymentHistory.stream().forEach(sph -> {
            feeReceiptRepository.updateFeeReceiptStudentId(sph.getFee_receipt_id(), sph.getFee_receipt(), oldStudentId,
                    newStudentId);
            studentPaymentHistoryRepository.updateStudentPaymentHistory(sph.getFee_receipt_id());
        });
        studentPaymentHistoryRepository.saveAll(studentPaymentHistory);
        StudentDueEvent studDue = new StudentDueEvent(null, null, newStudentId, "studentDue");
        applicationEventPublisher.publishEvent(studDue);
    }

    public List<HashMap<String, Object>> feeReceiptByStudenId(Integer oldStudentId) {
        return feeReceiptRepository.feeReceiptByStudenId(oldStudentId);
    }

    public List<HashMap<String, Object>> getFeeReceiptDetailsData(Integer fee_receipt_id) {
        return feeReceiptRepository.getFeeReceiptDetailsData(fee_receipt_id);
    }

    public Map<String, Object> hostelDueCalculationVocherHeadWiseForLedger(Integer acYearId, Integer studentId) {

        Map<String, Object> response = new HashMap<>();
        Student_Details studentDetails = studentDetailsService.get(studentId);
        Academic_year academicYear = academicYearService.get(acYearId);
        HostelBedAssignment hostelBedAssignment = hostelBedAssignmentRepository
                .findByAcYearAndStudentAndCancelledRemarksIsNullAndActiveTrue(academicYear, studentDetails);
        List<HostelFeeTemplate> hostelFeeTemplate = hostelFeeTemplateService
                .get(hostelBedAssignment.getHostelFeeTemplate().getHostel_fee_template_id());
        List<Map<String, Object>> templateDetails = new ArrayList<>();
        Map<String, Object> voucherheadwiseDueAmount = new HashMap<>();
        Map<String, Object> voucherheadwisePaidAmount = new HashMap<>();
        hostelFeeTemplate.stream().forEach(temp -> {
            Map<String, Object> map = new HashMap<>();
            Double dueAmount = 0.0;
            Double paidAmount = 0.0;

            VoucherHeadNew voucherHead = voucherHeadNewRepository.findById(temp.getFee_head_id()).get();
            Integer hostelWaiverAmount = hostelWaiverRepository.getDataForLedger(acYearId, studentId);
//			if (ObjectUtils.isNotEmpty(temp.getAdvance_amount())) {

            paidAmount = studentPaymentHistoryRepository.hostelPaidAmount(acYearId, temp.getHostel_fee_template_id(), temp.getFee_head_id(), studentId);
            dueAmount = temp.getTotal_amount() - (hostelWaiverAmount + paidAmount);
//			} else {
//				throw new RuntimeException("Hostel Fee Template Individual Voucher Head Wise Amount is null");
//			}

            voucherheadwisePaidAmount.put(temp.getFee_head_id().toString(), paidAmount);
            voucherheadwiseDueAmount.put(temp.getFee_head_id().toString(), dueAmount);
            map.put("hostel_fee_template_id", temp.getHostel_fee_template_id());
            map.put("template_name", temp.getTemplate_name());
            map.put("hostelWaiverAmount", hostelWaiverAmount);
            conversionOfAmountToDollarOrINR(temp, map);
            map.put("voucher_head_new_id", voucherHead.getVoucher_head_new_id());
            map.put("voucher_head", voucherHead.getVoucher_head());
            templateDetails.add(map);

        });
        response.put("hostelFeeTemplate", templateDetails);
        response.put("voucherheadwisePaidAmount", voucherheadwisePaidAmount);
        response.put("voucherheadwiseDueAmount", voucherheadwiseDueAmount);
//		response.put("hostelDetails", hostelBedAssignment);

        return response;
    }

    public ResponseEntity<Object> hostelDueCalculationVocherHeadWise(Integer acYearId, Integer studentId) {
        try {
            Map<String, Object> response = new HashMap<>();
            Student_Details studentDetails = studentDetailsService.get(studentId);
            Academic_year academicYear = academicYearService.get(acYearId);
            HostelBedAssignment hostelBedAssignment = hostelBedAssignmentRepository
                    .findByAcYearAndStudentAndCancelledRemarksIsNullAndActiveTrue(academicYear, studentDetails);
            List<HostelFeeTemplate> hostelFeeTemplate = hostelFeeTemplateService
                    .get(hostelBedAssignment.getHostelFeeTemplate().getHostel_fee_template_id());
            List<Map<String, Object>> templateDetails = new ArrayList<>();
            Map<String, Object> voucherheadwiseDueAmount = new HashMap<>();
            hostelFeeTemplate.stream().forEach(temp -> {
                Map<String, Object> map = new HashMap<>();
                Double dueAmount = 0.0;
                VoucherHeadNew voucherHead = voucherHeadNewRepository.findById(temp.getFee_head_id()).get();
                if (ObjectUtils.isNotEmpty(temp.getAdvance_amount())) {
                    dueAmount = temp.getAdvance_amount() - studentPaymentHistoryRepository.hostelPaidAmount(acYearId,
                            temp.getHostel_fee_template_id(), temp.getFee_head_id(), studentId);
                } else {
                    throw new RuntimeException("Hostel Fee Template Individual Voucher Head Wise Amount is null");
                }
                voucherheadwiseDueAmount.put(temp.getFee_head_id().toString(), dueAmount);
                map.put("hostel_fee_template_id", temp.getHostel_fee_template_id());
                map.put("template_name", temp.getTemplate_name());
                conversionOfAmountToDollarOrINR(temp, map);
                map.put("voucher_head_new_id", voucherHead.getVoucher_head_new_id());
                map.put("voucher_head", voucherHead.getVoucher_head());
                map.put("hostel_bed_assignment_id", hostelBedAssignment.getHostelBedAssignmentId());
                templateDetails.add(map);

            });
            response.put("hostelFeeTemplate", templateDetails);
            response.put("voucherheadwiseDueAmount", voucherheadwiseDueAmount);

            return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void conversionOfAmountToDollarOrINR(HostelFeeTemplate temp, Map<String, Object> map) {
        if (temp.getCurrency_type_id() == currencyTypeRepository.getCurrencyTypeIdOfUSD()) {
            DollarToInrConversion dollarValue = dollarToInrConversionRepository.getDollarToINRCurrencyConversion();
            if (ObjectUtils.isNotEmpty(dollarValue)) {
                map.put("template_amount", (temp.getTemplate_amount() == null ? 0 : temp.getTemplate_amount()) * dollarValue.getDollar_value());
                map.put("advance_amount", temp.getAdvance_amount() * dollarValue.getDollar_value());
                map.put("minimum_amount", temp.getMinimum_amount() * dollarValue.getDollar_value());
                map.put("total_amount", temp.getTotal_amount() * dollarValue.getDollar_value());
            }
        } else {
            map.put("template_amount", temp.getTemplate_amount());
            map.put("advance_amount", temp.getAdvance_amount());
            map.put("minimum_amount", temp.getMinimum_amount());
            map.put("total_amount", temp.getTotal_amount());
        }

    }

    public ResponseEntity<Object> getFeeReceiptDetails(Integer studentId) {
        List<Map<String, Object>> feeRecieptsList = feeReceiptRepository.getFeeReceiptDetails(studentId);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", feeRecieptsList);
    }

    public ResponseEntity<Object> changeOfCourseFeePaidStatusByStudentId(Integer studentId) {
        List<Map<String, Object>> cocPaidDetails = feeReceiptRepository
                .changeOfCourseFeePaidStatusByStudentId(studentId);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", cocPaidDetails);
    }

    public ResponseEntity<Object> checkDuesOnSemForGeneratingNoDues(Integer studentId) {
        if (ObjectUtils.isNotEmpty(studentDueRepository.checkDataPresentOrNot(studentId))) {
            for (int i = 1; i <= 12; i++) {
                Double checkDuesOnSemForGeneratingNoDues = studentDueRepository
                        .checkDuesOnSemForGeneratingNoDues(studentId, "s" + i + "due").doubleValue();
                System.out
                        .println("checkDuesOnSemForGeneratingNoDues     " + (checkDuesOnSemForGeneratingNoDues != 0.0));
                if (checkDuesOnSemForGeneratingNoDues != 0.0 || checkDuesOnSemForGeneratingNoDues == null) {
                    System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiii " + i);
                    return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", (i - 1));
                }
            }

        } else {
            return ResponseHandler.generateResponse(true, HttpStatus.NOT_FOUND, "SUCCESS",
                    "Data not found in student dues for student id : " + studentId);
        }
        return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", "No Dues");
    }

    public List<Map<String, Object>> getFeeReceipyWiseAndUserWiseData(LocalDate start, LocalDate end) {
        return feeReceiptRepository.getFeeReceipyWiseAndUserWiseData(start, end);
    }


    public ResponseEntity<Object> hostelFeeReceiptDetailsByFeeReceiptId(Integer feeReceiptId) {
        List<HostelFeeReceiptDto> hostelFeeReceipt = new ArrayList<>();
//		if(type.equalsIgnoreCase("HOS")) {
        hostelFeeReceipt = hstlFeeReciptRepository.hostelFeeReceiptDetailsByFeeReceiptId(feeReceiptId);
//		}
//		else {
//			hostelFeeReceipt = hstlBulkFeeReciptRepository.hostelBulkFeeReceiptDetailsByFeeReceiptId(feeReceiptId);
//		}
        return ResponseHandler.generateResponse(true, HttpStatus.OK, hostelFeeReceipt);
    }


    public ResponseEntity<Object> hostelBulkFeeReceiptDetailsByFeeReceiptId(Integer feeReceiptId) {
//		List<HostelFeeReceiptDto> hostelFeeReceipt = hstlBulkFeeReciptRepository.hostelFeeReceiptDetailsByFeeReceiptId(feeReceiptId);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, " ");
    }


    public ResponseEntity<Object> paymentInformationByOrderId(String orderId) {
        List<Map<String, Object>> paymentDetails = bankImportTransactionRepository.getpaymentInformationByOrderId(orderId);
        return ResponseHandler.generateResponse(true, HttpStatus.OK, paymentDetails);
    }

//	public ResponseEntity<Object> getFeeReceiptsByDate(String date) {
//	List<Map<String,Object>> feeReceipts = feeReceiptRepository.getFeeReceiptsByDate(date);
//		return ResponseHandler.generateResponse(true, HttpStatus.OK, feeReceipts);
//	}


    public void inActivateFeeReceiptNumber(Integer financial_year_id, Integer fee_receipt, String receipt_type) {
        FeeReceipt feeReceipt = feeReceiptRepository.getFeeReceiptDetails(financial_year_id, fee_receipt, receipt_type);
        String transaction_type = feeReceipt.getTransaction_type();

        if ("p_gateway".equalsIgnoreCase(transaction_type) || "rtgs".equalsIgnoreCase(transaction_type)) {
            Integer bank_import_transaction_id = feeReceipt.getBank_transaction_history_id();
            Float paid_amount = feeReceipt.getPaid_amount();
            BankImportTransaction bankImportTransaction = bankImportTransactionRepository.getBankImportTransaction(bank_import_transaction_id);
            Double amount = bankImportTransaction.getAmount();
            Double balance = amount + paid_amount;
            bankImportTransaction.setBalance(balance);
            System.out.println("11111balance111111 " + balance);
            System.out.println("1111amount111111 " + amount);
            System.out.println("11111paid_amount1111 " + paid_amount);
            bankImportTransactionRepository.save(bankImportTransaction);
        }

        feeReceiptRepository.delete1(feeReceipt.getFee_receipt_id());
        hstlFeeReciptRepository.deactive(feeReceipt.getFee_receipt_id(), feeReceipt.getStudent_id());
        rtgsFeeHistoryRepository.deactive(feeReceipt.getBank_transaction_history_id(), feeReceipt.getFee_receipt());
        tallyReceiptRepository.deactive(feeReceipt.getFee_receipt(), feeReceipt.getReceipt_type(), feeReceipt.getStudent_id());

        List<TallyReceipt> tallyReceipt = tallyReceiptRepository.getTallyReceiptData(feeReceipt.getFee_receipt_id());

        for (TallyReceipt tally : tallyReceipt) {
            CancelledTallyHostelReceipt cancelledTallyHostelReceipt = new CancelledTallyHostelReceipt();

            // Mapping fields from TallyReceipt to CancelledTallyHostelReceipt fee_receipt_id
            cancelledTallyHostelReceipt.setTally_receipt_id(tally.getTally_receipt_id());
            cancelledTallyHostelReceipt.setStudent_id(tally.getStudent_id());
            cancelledTallyHostelReceipt.setStudent_name(tally.getStudent_name());  // first name + last name
            cancelledTallyHostelReceipt.setAuid(tally.getAuid());
            cancelledTallyHostelReceipt.setUsn(tally.getUsn());
            cancelledTallyHostelReceipt.setParticulars(tally.getParticulars());
            cancelledTallyHostelReceipt.setTotal(tally.getTotal());
            cancelledTallyHostelReceipt.setFee_receipt(tally.getFee_receipt());
            cancelledTallyHostelReceipt.setTransaction_type(tally.getTransaction_type());
            cancelledTallyHostelReceipt.setSchool_name(tally.getSchool_name());
            cancelledTallyHostelReceipt.setTransaction_no(tally.getTransaction_no());
            cancelledTallyHostelReceipt.setDeposited_bank(tally.getDeposited_bank());
            cancelledTallyHostelReceipt.setDd_no(tally.getDd_no());
            cancelledTallyHostelReceipt.setDd_bank_name(tally.getDd_bank_name());
            cancelledTallyHostelReceipt.setFinancial_year(tally.getFinancial_year());
            cancelledTallyHostelReceipt.setTotal_amount(tally.getTotal_amount());
            cancelledTallyHostelReceipt.setReceived_from(tally.getReceived_from());
            cancelledTallyHostelReceipt.setReceived_type(tally.getReceived_type());
            cancelledTallyHostelReceipt.setReceived_in(tally.getReceived_in());
            cancelledTallyHostelReceipt.setRemarks(tally.getRemarks());
            cancelledTallyHostelReceipt.setTransaction_date(tally.getTransaction_date());
            cancelledTallyHostelReceipt.setBank_institute(tally.getBank_institute());
            cancelledTallyHostelReceipt.setVendor_id(tally.getVendor_id());
            cancelledTallyHostelReceipt.setFee_receipt_id(tally.getFee_receipt_id());

            Date currentDate = new Date();
            cancelledTallyHostelReceipt.setCreated_date(currentDate);  // Set current date-time
            cancelledTallyHostelReceipt.setModified_date(currentDate);
            cancelledTallyHostelReceipt.setActive(true);

            cancelledTallyHostelReceiptRepository.save(cancelledTallyHostelReceipt);


        }
    }


    public ResponseEntity<Object> getHostelFeeReceiptVoucherHeadWiseDetails(Integer financial_year_id,
                                                                            Integer fee_receipt, String receipt_type) {
        Map<String, Object> feeReceipt = feeReceiptRepository.getFeeReceiptDetailsForHeadwise(financial_year_id, fee_receipt, receipt_type);
        Integer feeReceiptId = (Integer) feeReceipt.get("fee_receipt_id");
        Integer studentId = (Integer) feeReceipt.get("student_id");
        List<Map<String, Object>> headwiseData = hstlFeeReciptRepository.getHeadwiseData(feeReceiptId, studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("feeReceipt", feeReceipt);
        response.put("voucherheadwiseData", headwiseData);

        // Return the combined response in a ResponseEntity
        return ResponseEntity.ok(response);
    }


    public CancelledHostelFeeReceipts saveCancelledHostelFeeReceipts(@Valid CancelledHostelFeeReceipts cancelledHostelFeeReceipts) {
        return cancelledHostelFeeReceiptsRepository.save(cancelledHostelFeeReceipts);
    }

    public ResponseEntity<Object> getCounterSummary(String fromDate, String toDate) {
        List<Map<String, Object>> summaryDetails;
        List<Map<Object, Object>> dynamicObjectList = new LinkedList<>();

        try {
            summaryDetails = feeReceiptRepository.findByDateAndTransactionType(fromDate, toDate);

            // Group by createdUsername
            Map<String, List<Map<String, Object>>> groupByCreatedUsername = summaryDetails.parallelStream()
                    .filter(summary -> summary.get("createdUsername") != null)
                    .collect(Collectors.groupingBy(summary -> summary.get("createdUsername").toString()));

            // Process each group by createdUsername
            groupByCreatedUsername.forEach((createdUsername, records) -> {
                Map<Object, Object> dynamicObject = new LinkedHashMap<>();
                dynamicObject.put("createdUsername", createdUsername);

                // Fetch the payment for createdUsername
                Double payment = pettyCashRepository.fetchByCreatedUsernameAndDates(createdUsername, fromDate, toDate);
                if (payment == null) payment = 0.0;
                dynamicObject.put("payment", payment);

                Double inrCash = 0.0, inrDD = 0.0, inrOnline = 0.0, usdCash = 0.0, usdDD = 0.0, usdOnline = 0.0;


                // Iterate over the records to sum up the amounts by transaction type
                for (Map<String, Object> map : records) {
                    String transType = (String) map.get("transactionType");
                    Double inrValue = (Double) map.get("inrValue");
                    String receivedIn = (String) map.get("receivedIn");
                    if (transType != null) {
                        switch (transType.toUpperCase()) {
                            case "CASH":
                                if("INR".equalsIgnoreCase(receivedIn)) inrCash = inrValue;
                                else usdCash = inrValue;
                                break;
                            case "DD":
                                if("INR".equalsIgnoreCase(receivedIn)) inrDD = inrValue;
                                else usdDD = inrValue;
                                break;
                            case "RTGS":
                            case "ONLINE":
                            case "AUTO_RECEIPT":
                            case "P_GATEWAY":
                                if("INR".equalsIgnoreCase(receivedIn)) inrOnline += inrValue;
                                else usdOnline += inrValue;
                                break;
                        }
                    }
                }

                // Add the amounts to the dynamicObject
                dynamicObject.put("INRCASH", inrCash);
                dynamicObject.put("INRDD", inrDD);
                dynamicObject.put("INRONLINE", inrOnline);
                dynamicObject.put("USDCASH", usdCash);
                dynamicObject.put("USDDD", usdDD);
                dynamicObject.put("USDONLINE", usdOnline);

                // Add the processed object to the dynamicObjectList
                dynamicObjectList.add(dynamicObject);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseHandler.generateResponse(true, HttpStatus.OK, dynamicObjectList);
    }

    public ResponseEntity<Object> getCounterSummaryBySchools(String fromDate, String toDate) {

        List<Map<String, Object>> summaryDetails = Collections.emptyList();
        List<Map<Object, Object>> dynamicObjectList = new LinkedList<>();
        try {
            summaryDetails = feeReceiptRepository.getFeeReceiptDetailsBySchools(fromDate, toDate);
            // Group by schoolName
            Map<String, List<Map<String, Object>>> groupBySchoolIds = summaryDetails.parallelStream()
                    .filter(summary -> summary.get("schoolId") != null)
                    .collect(Collectors.groupingBy(summary -> summary.get("schoolId").toString()));

            groupBySchoolIds.forEach((schoolId, records) -> {
                Map<Object, Object> dynamicObject = new LinkedHashMap<>();

                // Fetch the payment for createdUsername
                Double payment = pettyCashRepository.fetchBasedOnSchools(Integer.valueOf(schoolId), fromDate, toDate);
                if (payment == null) payment = 0.0;
                dynamicObject.put("payment", payment);

                // Initialize keys for different transaction types
                Double inrCash = 0.0, inrDD = 0.0, inrOnline = 0.0, usdCash = 0.0, usdDD = 0.0, usdOnline = 0.0;

                // Iterate over the records to sum up the amounts by transaction type
                for (Map<String, Object> map : records) {
                    dynamicObject.put("schoolName", map.get("schoolName"));
                    String transType = (String) map.get("transactionType");
                    Double inrValue = (Double) map.get("inrValue");
                    String receivedIn = (String) map.get("receivedIn");
                    if (transType != null) {
                        switch (transType.toUpperCase()) {
                            case "CASH":
                                if("INR".equalsIgnoreCase(receivedIn)) inrCash = inrValue;
                                else usdCash = inrValue;
                                break;
                            case "DD":
                                if("INR".equalsIgnoreCase(receivedIn)) inrDD = inrValue;
                                else usdDD = inrValue;
                                break;
                            case "RTGS":
                            case "ONLINE":
                            case "AUTO_RECEIPT":
                            case "P_GATEWAY":
                                if("INR".equalsIgnoreCase(receivedIn)) inrOnline += inrValue;
                                else usdOnline += inrValue;
                                break;
                        }
                    }
                }

                // Add the amounts to the dynamicObject
                dynamicObject.put("INRCASH", inrCash);
                dynamicObject.put("INRDD", inrDD);
                dynamicObject.put("INRONLINE", inrOnline);
                dynamicObject.put("USDCASH", usdCash);
                dynamicObject.put("USDDD", usdDD);
                dynamicObject.put("USDONLINE", usdOnline);

                // Add the processed object to the dynamicObjectList
                dynamicObjectList.add(dynamicObject);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseHandler.generateResponse(true, HttpStatus.OK, dynamicObjectList);
    }

	public ResponseEntity<Object> getFeeReceiptId(Integer feeReceipt, Integer fcYear, Integer bulkId) {

		Integer feeReceiptId = feeReceiptRepository.getFeeReceiptIdByFeeReceiptAndFcYearAndBulkId(feeReceipt,fcYear,bulkId);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, feeReceiptId);
	}


//	public CancelledTallyHostelReceipt saveCancelledTallyHostelReceipt(@Valid CancelledTallyHostelReceipt cancelledTallyHostelReceipt) {
//		return	cancelledTallyHostelReceiptRepository.save(cancelledTallyHostelReceipt);
//	}

}
