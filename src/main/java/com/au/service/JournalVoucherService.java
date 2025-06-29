package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.au.dto.JwtDetails;
import com.au.exception.ResourceNotFoundException;
import com.au.model.FinancialYear;
import com.au.model.JournalVoucher;
import com.au.model.LockDates;
import com.au.model.VoucherHeadNew;
import com.au.repository.EmployeePayHistoryRepository;
import com.au.repository.FinancialYearRepository;
import com.au.repository.JournalVoucherRepository;
import com.au.repository.LockDateRepository;
import com.au.repository.SalaryStructureHeadRepository;
import com.au.repository.School_Repository;
import com.au.repository.VoucherHeadNewRepository;
import com.au.response.ResponseHandler;

import java.util.Date;
import java.util.Optional;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Service
public class JournalVoucherService {

    private static final String SALARIES = "SALARIES";
    private static final String PF = "PF";
    private static final String ESI = "ESI";
    @Autowired
    private JournalVoucherRepository jv_repo;

    @Autowired
    private FinancialYearRepository financial_year_repo;

    private Logger log = LoggerFactory.getLogger(JournalVoucherService.class);

    public static final String value = "JournalVoucher";

    private AmazonS3 s3client;
    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon1() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    @Autowired
    private LockDateRepository lockDateRepository;

    @Autowired
    private School_Repository schoolRepository;

    @Autowired
    private SalaryStructureHeadRepository salaryStructureHeadRepository;

    @Autowired
    private VoucherHeadNewRepository voucherHeadNewRepository;

    @Autowired
    private EmployeePayHistoryRepository employeePayHistoryRepository;

    private static final Integer DEFAULT_VALUE = 0;

    Integer count = 0;

    public List<JournalVoucher> saveJournalVoucher(@Valid List<JournalVoucher> jv2) throws Exception {
//		List<JournalVoucher> list_Journal_oucher = new  ArrayList<JournalVoucher>();

        jv2.stream().forEach(jv -> {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            try {
                date1 = df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            FinancialYear f_year = financial_year_repo.getFinancialYearData(date1); // financial year details of one
            // particular id.
            JournalVoucher id = jv_repo.getLatestJournalVoucher(f_year.getFinancial_year_id(), jv.getSchool_id()); // id
            // will
            // come
            Integer journal_voucher_number = jv_repo.getLatestData(f_year.getFinancial_year_id(), jv.getSchool_id());
            try {
                f_year.equals(null);
            } catch (NullPointerException e) {
                throw new NullPointerException("Finanacial Year Is Not Created Yet !!!");
            }
            if (journal_voucher_number == null) {
                jv.setJournal_voucher_number(1);
                jv.setFinancial_year_id(f_year.getFinancial_year_id());
            } else if (f_year.getFinancial_year_id() == id.getFinancial_year_id()) {
                jv.setJournal_voucher_number(journal_voucher_number + 1);
                jv.setFinancial_year_id(f_year.getFinancial_year_id());
            } else {
                jv.setJournal_voucher_number(1);
                jv.setFinancial_year_id(f_year.getFinancial_year_id());
            }
        });

        return jv_repo.saveAll(jv2);

    }

    public List<JournalVoucher> listAll1() {
        return jv_repo.findAll11();
    }

    public JournalVoucher get(Integer id) {
        return jv_repo.findById(id).orElseThrow(() -> new ResourceNotFoundException(" Journal Voucher:" + id));
    }

    public List<JournalVoucher> updateJournalVoucher(List<JournalVoucher> dpv) {
        return jv_repo.saveAll(dpv);
    }

    public void delete(Integer id) {
        JournalVoucher ed = jv_repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Voucher Not Found:" + id));
        jv_repo.updateJournalVoucher(id);
    }

    public void delete1(Integer id) {
        JournalVoucher ed = jv_repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Voucher Not Found:" + id));
        jv_repo.updateJournalVoucher1(id);
    }

    public ResponseEntity<Object> getAllSortedData(Pageable pageable, LocalDate start, LocalDate end,
                                                   LocalDate minDate) {
        Page<Object> response = null;
        try {
            Date startDate = (start != null) ? java.sql.Date.valueOf(start) : null;
            Date endDate = (end != null) ? java.sql.Date.valueOf(end) : null;
            Date minStartDate = (minDate != null) ? java.sql.Date.valueOf(minDate) : null;

            response = jv_repo.getAllSortedData(pageable, startDate, endDate, minStartDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
    }

    public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, LocalDate start,
                                                              LocalDate end, LocalDate minDate) {
        Page<Object> response = null;
        try {
            Date startDate = (start != null) ? java.sql.Date.valueOf(start) : null;
            Date endDate = (end != null) ? java.sql.Date.valueOf(end) : null;
            Date minStartDate = (minDate != null) ? java.sql.Date.valueOf(minDate) : null;
            response = jv_repo.getAllDataFilteredByKeyword(pageable, keyword, startDate, endDate,
                    minStartDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, response);
    }

    public LocalDate getMinimumJournalVoucherDate() {
        Optional<Date> minCreatedDate = jv_repo.findMinCreatedDate();
        return minCreatedDate.map((Date date) -> date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate())
                .orElse(LocalDate.of(2024, 1, 1)); // fallback
    }


    public List<HashMap<String, Object>> getJournalVoucherData(Integer journal_voucher_number, Integer school_id,
                                                               Integer financial_year_id) {
        return jv_repo.getJournalVoucherData(journal_voucher_number, school_id, financial_year_id);
    }

    public void uploadFile(MultipartFile multipartFile, Integer journal_voucher_number) {

        String attachmentPath = null;
        String attachmentFileType = null;

        try {
            log.debug("Message For Payment voucher Attachment --------------");

            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);

            log.debug("Journal voucher Attachment", multipartFile);
            attachmentPath = LocalDate.now() + "/" + journal_voucher_number + "/" + fileName;
            attachmentFileType = endpointUrl + "/" + bucketName + "/" + value + "/" + LocalDate.now() + "/"
                    + journal_voucher_number + "/" + fileName;
            uploadFileTos3bucket(fileName, file, journal_voucher_number);
            log.debug("Message For Attachment", file);
            file.delete();
        } catch (AmazonServiceException ase) {

            log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            log.info("Error Message:    " + ase.getMessage());
            log.info("HTTP Status Code: " + ase.getStatusCode());
            log.info("AWS Error Code:   " + ase.getErrorCode());
            log.info("Error Type:       " + ase.getErrorType());
            log.info("Request ID:       " + ase.getRequestId());

        } catch (AmazonClientException ace) {
            log.info("Caught an AmazonClientException: ");
            log.info("Error Message: " + ace.getMessage());
        } catch (IOException ioe) {
            log.info("IOE Error Message: " + ioe.getMessage());

        }
        jv_repo.updateJournalVoucherFilePath(attachmentPath, attachmentFileType, journal_voucher_number);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file, Integer payment_voucher_id) {
        final String uniqueFileName = value + "/" + LocalDate.now() + "/" + payment_voucher_id + "/" + fileName; // file.getName()
        s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));
    }

    public byte[] journalVoucherFileDownload(final String keyName) throws NoSuchFileException {
        try {
            byte[] content;
            final S3Object s3Object = s3client.getObject(bucketName, value + "/" + keyName);
            final S3ObjectInputStream stream = s3Object.getObjectContent();
            content = IOUtils.toByteArray(stream);
            System.out.println(content);
            s3Object.close();
            return content;

        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new NoSuchFileException("File Not Found");
            }
            throw new AmazonClientException("", e);
        } catch (IOException | AmazonClientException ex) {
            throw new AmazonClientException("", ex);
        }
    }

    public List<HashMap<String, Object>> getjournalVoucherAttachmentByVoucherNo(Integer journal_voucher_number,
                                                                                Integer financial_year_id) {
        List<HashMap<String, Object>> findByPaymentVoucherNo = jv_repo.findByPaymentVoucherNo(journal_voucher_number,
                financial_year_id);
        return findByPaymentVoucherNo;
    }

    public List<Map<String, Object>> getJournalVoucherByVoucherNumber(Integer journal_voucher_number,
                                                                      Integer school_id, Integer financial_year_id) {
        return jv_repo.getJournalVoucherByVoucherNumber(journal_voucher_number, school_id, financial_year_id);
    }

    public Map<String, Object> getJournalVoucherDataById(Integer journal_voucher_id) {
        return jv_repo.getJournalVoucherDataById(journal_voucher_id);
    }

    String currentDateForJournal;

    public List<JournalVoucher> journalVoucherCreationByMonthAndYear(Integer month, Integer year, JwtDetails jwtDetails) throws ParseException {
        List<JournalVoucher> journalVouchers = new ArrayList<>();

        LockDates salaryLockDate = lockDateRepository.getFirstByMonthandYear(month, year);

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = dtf.format(localDateTime);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = df.parse(date);

        FinancialYear currentFinancialYear = financial_year_repo.getFinancialYearData(currentDate);

        if (ObjectUtils.isNotEmpty(salaryLockDate)) {
            List<Integer> createdSchoolIds = jv_repo.journalVoucherCreatedSchoolIds(month, year);
            if (createdSchoolIds.isEmpty()) {
                createdSchoolIds.add(DEFAULT_VALUE);
            }
            List<Integer> schoolIdsForJournalVoucherCreation = schoolRepository.schoolIdsForJournalVoucherCreation(createdSchoolIds);
            List<VoucherHeadNew> staticVoucherHeadNewDetails = voucherHeadNewRepository.getVoucherHeadNew();

            if (ObjectUtils.isNotEmpty(schoolIdsForJournalVoucherCreation)) {
                schoolIdsForJournalVoucherCreation.forEach(schoolId -> {
                    int jvNum = jv_repo.getMaxJournalVoucherNumber(schoolId, currentFinancialYear.getFinancial_year_id()) + 1;

                    Map<String, Object> employeePayHistoryData = employeePayHistoryRepository.employeePayHistoryDataForJournalVoucher(month, year, schoolId);

                    if (ObjectUtils.isNotEmpty(employeePayHistoryData)) {
                        Double totalDebit = (Double) employeePayHistoryData.get("s439");
                        Double totalCredit = (Double) employeePayHistoryData.get("totalCredit");
                        double epsilon = 3.0;
                        if (Math.abs(totalDebit - totalCredit) <= epsilon) {
                            totalDebit = totalCredit;
                        }
                        if (Objects.equals(totalDebit, totalCredit) && totalDebit != 0 && totalCredit != 0) {
                            // 3 vouchers 1.salaries 2.PF 3.ESI
                            generateSalariesVoucher(staticVoucherHeadNewDetails, totalDebit, totalCredit, localDateTime, employeePayHistoryData,
                                    month, schoolId, date, jwtDetails, currentFinancialYear, year, journalVouchers, jvNum);
                            Double value;
                            value = (Double) employeePayHistoryData.get("s486");
                            if (value != 0.0) {
                                jvNum = jv_repo.getMaxJournalVoucherNumber(schoolId, currentFinancialYear.getFinancial_year_id()) + 1;
                                generatePFVoucher(staticVoucherHeadNewDetails, totalDebit, totalCredit, localDateTime, employeePayHistoryData,
                                        month, schoolId, date, jwtDetails, currentFinancialYear, year, journalVouchers, jvNum);
                            }
                            value = (Double) employeePayHistoryData.get("s485");
                            if (value != 0.0) {
                                jvNum = jv_repo.getMaxJournalVoucherNumber(schoolId, currentFinancialYear.getFinancial_year_id()) + 1;
                                generateESIVoucher(staticVoucherHeadNewDetails, totalDebit, totalCredit, localDateTime, employeePayHistoryData,
                                        month, schoolId, date, jwtDetails, currentFinancialYear, year, journalVouchers, jvNum);
                            }
                        }
                    } else {
                        throw new RuntimeException("Employee Pay History Data is empty.");
                    }
                });
            } else {
                throw new RuntimeException("Vouchers are already generated for all the Institutes.");
            }
        } else {
            throw new RuntimeException("Salary is not locked !! Please Contact Admin.");
        }
        return journalVouchers;
    }

    private void generateESIVoucher(List<VoucherHeadNew> staticVoucherHeadNewDetails, Double finalTotalDebit, Double totalCredit, LocalDateTime localDateTime,
                                    Map<String, Object> employeePayHistoryData, Integer month, Integer schoolId, String date,
                                    JwtDetails jwtDetails, FinancialYear currentFinancialYear, Integer year, List<JournalVoucher> journalVouchers,
                                    int jvNum) {

        List<Integer> staticHeadsForESI = Arrays.asList(10, 9, 485);
        generate(staticHeadsForESI, finalTotalDebit, totalCredit, localDateTime, employeePayHistoryData, month, schoolId, date, jwtDetails,
                currentFinancialYear, year, journalVouchers, jvNum, JournalVoucherService.ESI);

    }

    private void generatePFVoucher(List<VoucherHeadNew> staticVoucherHeadNewDetails, Double finalTotalDebit, Double totalCredit,
                                   LocalDateTime localDateTime, Map<String, Object> employeePayHistoryData, Integer month, Integer schoolId,
                                   String date, JwtDetails jwtDetails, FinancialYear currentFinancialYear,
                                   Integer year, List<JournalVoucher> journalVouchers, int jvNum) {

        List<Integer> staticHeadsForPF = Arrays.asList(8, 7, 486);
        generate(staticHeadsForPF, finalTotalDebit, totalCredit, localDateTime, employeePayHistoryData, month, schoolId, date, jwtDetails,
                currentFinancialYear, year, journalVouchers, jvNum, JournalVoucherService.PF);
    }

    private void generateSalariesVoucher(List<VoucherHeadNew> staticVoucherHeadNewDetails, Double finalTotalDebit, Double totalCredit,
                                         LocalDateTime localDateTime, Map<String, Object> employeePayHistoryData, Integer month, Integer schoolId,
                                         String date, JwtDetails jwtDetails, FinancialYear currentFinancialYear,
                                         Integer year, List<JournalVoucher> journalVouchers, int jvNum) {

        List<Integer> staticHeadsForSalaries = Arrays.asList(439, 441, 440, 332, 330, 11, 331, 9, 7);
        generate(staticHeadsForSalaries, finalTotalDebit, totalCredit, localDateTime, employeePayHistoryData, month, schoolId, date, jwtDetails,
                currentFinancialYear, year, journalVouchers, jvNum, JournalVoucherService.SALARIES);

    }

    private void generate(List<Integer> staticVoucherHeadNewDetails, Double finalTotalDebit, Double totalCredit,
                          LocalDateTime localDateTime, Map<String, Object> employeePayHistoryData,
                          Integer month, Integer schoolId, String date, JwtDetails jwtDetails,
                          FinancialYear currentFinancialYear, Integer year,
                          List<JournalVoucher> journalVouchers, int jvNum, String type) {

        String currentDateForJournal = (month == 3)
                ? localDateTime.getYear() + "-03-31T00:00:00.000Z"
                : localDateTime.toString();

        for (Integer id : staticVoucherHeadNewDetails) {
            VoucherHeadNew vhn = voucherHeadNewRepository.findById(id).get();
            Integer voucherHeadNewId = vhn.getVoucher_head_new_id();
            Double value = (Double) employeePayHistoryData.get("s" + voucherHeadNewId);
            if(value == 0.0) continue;
            JournalVoucher jv = new JournalVoucher();
            jv.setJournal_voucher_number(jvNum);
            jv.setSchool_id(schoolId);
            jv.setDate(date);
            jv.setActive(true);
            jv.setCreated_by(jwtDetails.getUserId());
            jv.setExpensense_head(vhn.getVoucher_head());
            jv.setVoucher_head_id(voucherHeadNewId);
            jv.setCreated_username(jwtDetails.getUserName());
            jv.setVendor_active(0);
            jv.setSalary_status(1);
            jv.setFinancial_year_id(currentFinancialYear.getFinancial_year_id());
            jv.setSalary_structure_head_id(voucherHeadNewId);
            jv.setMonth(month);
            jv.setYear(year);
            jv.setType("Salary-JV");

            switch (type.toUpperCase()) {
                case SALARIES:
                    jv.setRemarks("Being Salaries for the month of " + localDateTime.getMonth() + " accumulated for.");
                    jv.setPay_to("STAFF SALARIES");
                    jv.setDebit_total(finalTotalDebit);
                    jv.setCredit_total(totalCredit);
                    if (voucherHeadNewId == 439 || voucherHeadNewId == 441) {
                        jv.setDebit(value);
                        jv.setCredit(0.0);
                    } else {
                        jv.setDebit(0.0);
                        jv.setCredit(value);
                    }
                    break;

                case PF:
                    jv.setRemarks("Being PF Payable for the month of " + localDateTime.getMonth() + " accumulated for.");
                    jv.setPay_to("PF PAYABLE");
                    jv.setDebit_total((Double) employeePayHistoryData.get("s486"));
                    jv.setCredit_total((Double) employeePayHistoryData.get("s486"));
                    if (voucherHeadNewId == 486) {
                        jv.setDebit(0.0);
                        jv.setCredit(value);
                    } else {
                        jv.setDebit(value);
                        jv.setCredit(0.0);
                    }
                    break;

                case ESI:
                    jv.setRemarks("Being ESI Payable for the month of " + localDateTime.getMonth() + " accumulated for.");
                    jv.setPay_to("ESI PAYABLE");
                    jv.setDebit_total((Double) employeePayHistoryData.get("s485"));
                    jv.setCredit_total((Double) employeePayHistoryData.get("s485"));
                    if (voucherHeadNewId == 485) {
                        jv.setDebit(0.0);
                        jv.setCredit(value);
                    } else {
                        jv.setDebit(value);
                        jv.setCredit(0.0);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported type: " + type);
            }

            jv_repo.save(jv);
            journalVouchers.add(jv);
        }
    }


//	public Date getMinimumJvDate() {
//	    return jv_repo.findMinCreatedDate();
//	}

}
