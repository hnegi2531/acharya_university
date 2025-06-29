package com.au.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import com.au.model.*;
import com.au.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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
import com.au.dto.EmployeeDetailsForLeavePattern;
import com.au.dto.JwtDetails;
import com.au.dto.LeaveApplyDto;
import com.au.dto.LeaveApplyUpdateDto;
import com.au.dto.LeaveCancelDTO;
import com.au.exception.ResourceNotFoundException;
import com.au.repository.LeaveKittyRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaveApplyService {

    private Logger logger = LoggerFactory.getLogger(LeaveApplyService.class);

    public static final String value = "LeaveApplyBucket";
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Autowired
    private LeaveTypeRepository s_repo;

    @Autowired
    private LeaveApplyRepository leaveapplyrepo;

    @Autowired
    private EmployeeDetailsRepository empDetail_repo;

    @Autowired
    private LeaveKittyRepository el_repo;

    @Autowired
    private LeavePatternRepository leave_pattern_repo;

    @Autowired
    private BiometricAttendanceRepository bio_repo;

    @Autowired
    private ShiftRepository shift_repo;

    @Autowired
    private UserAuthenticationRepository uar_repo;

    @Autowired
    private CalenderYearRepository calender_year_repo;

    @Autowired
    private BiometricAttendanceRepository biometricAttendanceRepository;

    @Autowired
    private HolidayCalenderRepository holiday_repo;

    @Autowired
    private LeaveKittyRepository leaveKittyRepository;

    @Autowired
    private VacationHolidayCalendarRepository vhc_repo;

    @Autowired
    private ResponseHandler response_handler;

    @Autowired
    private JwtTokenService jwt_service;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    /*
     * ---------------------------for calculate local time
     * difference----------------------------
     */
//	LocalTime firstEntryTime = null;
//	LocalTime modifiedEntryTime = null;

//	long diffH = lastEntryTime.until(modifiedExitTime, ChronoUnit.HOURS);
//	String timeTakingH = Long.toString(diffH);
//	long diffM = lastEntryTime.until(modifiedExitTime, ChronoUnit.MINUTES);
//	String timeTakingM = Long.toString(diffM);
//	long diffS = lastEntryTime.until(modifiedExitTime, ChronoUnit.SECONDS);
//	String timeTakingS = Long.toString(diffS);
//String timeTaking = timeTakingH +":"+timeTakingM +":"+timeTakingS;
//	biometricAttendance.setDuration(timeTaking);
    /*
     * ---------------------------for calculate local time
     * difference----------------------------
     */

    Date date1;
    Date date2;
    Date date3;
    Date date4;
    Date date_to_increment;
    List<LeaveApply> list_leaveApply;
    LeaveApply leaveApplyData = new LeaveApply();
    String stringTypefromDateWithYmdFormat;
    String stringTypeToDateWithYmdFormat;

    public List<LeaveApply> save_LeaveApply(@Valid LeaveApplyDto la, JwtDetails jwtDetails) throws Exception {

        List<LeaveApply> list = new ArrayList<>();

        String leaveTypeData = s_repo.getleaveTypeData(la.getLeave_id());

        Integer roleId = getRoleId(jwtDetails.getUserId());

        la.getEmp_id().forEach(emp_id -> {
            // check for consecutive CL's
            if(la.getLeave_id() == 1 && roleId != 1 && roleId != 5 && roleId != 6){
                if (la.getNo_of_days_applied() > 3)
                    throw new RuntimeException("Cannot apply more than 3 CL consecutively!!!");
                Boolean isAppliedCL = checkPreviousDaysCL(emp_id, la.getLeave_id(), la.getFrom_date());
                if(isAppliedCL)
                    throw new RuntimeException("Cannot apply more than 3 CL consecutively!!!");
            }

            if(la.getNo_of_days_applied() != null && la.getNo_of_days_applied() == 0.5){
                int count =  leaveapplyrepo.getCountOfLeavesAppliedOnTheGivenDate(la.getFrom_date(),emp_id);
                if(count > 1){
                    throw new RuntimeException("Leave already applied for the day: " + la.getFrom_date() + ", for empId: " + emp_id);
                }
                LeaveApply leaveApply = leaveapplyrepo.getLeaveAppliedByFromDateAndEmpId(la.getFrom_date(), emp_id);
                if (leaveApply != null){
                    if(la.getShift().equalsIgnoreCase(leaveApply.getShift())){
                        throw new RuntimeException("Leave already applied on the selected shift: " + la.getShift() + ", for empId: " + emp_id);
                    }
                    LeaveType typeOfExistingLeave = getLeaveType(la.getLeave_id());
                    LeaveType typeOfNewLeave = getLeaveType(la.getLeave_id());
                    if(!Objects.equals(la.getLeave_id(), leaveApply.getLeave_id())){
                        if(typeOfExistingLeave.getType().equalsIgnoreCase("Leave") && typeOfNewLeave.getType().equalsIgnoreCase("Leave")){
                            throw new RuntimeException("Two different leaves can't be applied on the same day");
                        }
                    }
                }
            }
            String date = leaveAppliedOnGivenDates(la, emp_id);
            if (date != null) {
                throw new RuntimeException("Leave already applied on the date: " + date + " for empId: " + la.getEmp_id());

            }
            LeaveApply leaveApply = new LeaveApply();

            list_leaveApply = leaveapplyrepo.checkValidationForLeaveOnFromDate(la.getFrom_date(), emp_id);

            System.out.println("--------------------------- START -----------------------" + list_leaveApply.size());

            if (leaveTypeData.equalsIgnoreCase("PR")) {
                list.add(processOneHourPermission(la, leaveApply, emp_id));
            } else if (leaveTypeData.equalsIgnoreCase("CP")) {
                list.add(processComponsatoryLeave(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("CL")) {
                list.add(processCasualOrEarnedLeave(la, leaveApply, emp_id, leaveTypeData, roleId));
            } else if (leaveTypeData.equalsIgnoreCase("EL")) {
                list.add(processCasualOrEarnedLeave(la, leaveApply, emp_id, leaveTypeData, roleId));
            } else if (leaveTypeData.equalsIgnoreCase("VL")) {
                list.add(processVacationLeave(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("MA")) {
                list.add(processManualAttendance(la, leaveApply, emp_id));
            } else if (leaveTypeData.equalsIgnoreCase("OD")) {
                list.add(processOnDutyOrExamDutyOrUniversityDuty(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("ED")) {
                list.add(processOnDutyOrExamDutyOrUniversityDuty(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("UD")) {
                list.add(processOnDutyOrExamDutyOrUniversityDuty(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("VD")) {
                list.add(processOnDutyOrExamDutyOrUniversityDuty(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("SD")) {
                list.add(processOnDutyOrExamDutyOrUniversityDuty(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("RH")) {
                list.add(processRestrictedHoliday(la, leaveApply, emp_id));
            } else if (leaveTypeData.equalsIgnoreCase("AL")) {
                list.add(processAbsentLeave(la, leaveApply, emp_id));
            } else if (leaveTypeData.equalsIgnoreCase("PL")) {
                list.add(processPaternityLeaveOrMaternityLeave(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("ML")) {
                list.add(processPaternityLeaveOrMaternityLeave(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("WL")) {
                list.add(processMarriageLeave(la, leaveApply, emp_id));
            } else if (leaveTypeData.equalsIgnoreCase("SL")) {
                list.add(processMedicalOrSickLeave(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("SP")) {
                list.add(processSpecialLeave(la, leaveApply, emp_id, leaveTypeData));
            } else if (leaveTypeData.equalsIgnoreCase("RL")) {
                list.add(processResearchLeave(la, leaveApply, emp_id, leaveTypeData));
            } else {
                throw new RuntimeException("Selected leave type is not yet implemented!!!");
            }

            leaveapplyrepo.saveAll(list);

        });

        System.out.println("--------------------------- END -----------------------" + list_leaveApply.size());
        return list;

    }

    private Boolean checkPreviousDaysCL(Integer empId, Integer leaveId, String fromDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        Date formattedFromDate = null;
        try {
            formattedFromDate = new SimpleDateFormat("dd-MM-yyyy").parse(fromDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formattedFromDate);
        int leaveCount = 0;
        int direction = -1;
        for (int i = 1; i <= 3; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, direction);
            Date date = calendar.getTime();
            String formattedDate = sdf.format(date);
            LeaveApply leaveApply = leaveapplyrepo.getLeaveByFromDateAndEmpIdAndLeaveId(formattedDate, empId, leaveId);
            if (leaveApply != null)
                leaveCount ++;
            if (leaveCount != i)
                return false;
            else if(leaveCount == 3)
                return true;
        }
        return false;
    }

    private Integer getRoleId(Integer userId) {
        Integer empId = empDetail_repo.findByUserId(userId);
        return rolesRepository.findByEmpId(empId);
    }

    private LeaveType getLeaveType(Integer leaveId) {
        return leaveTypeRepository.findById(leaveId).get();
    }

    private String leaveAppliedOnGivenDates(LeaveApplyDto la, Integer empId) {
        String fromDate = la.getFrom_date();
        String toDate = la.getTo_date();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        java.time.LocalDate startDate = java.time.LocalDate.parse(fromDate, formatter);
        java.time.LocalDate endDate = java.time.LocalDate.parse(toDate, formatter);

        java.time.LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            Integer count = 0;
            try {
                String formattedDate = currentDate.format(formatter);
                count = leaveapplyrepo.countOfLeavesAppliedOnTheDate(formattedDate, empId, la.getYear());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (count > 0)
                return currentDate.toString();
            currentDate = currentDate.plusDays(1);
        }
        return null;
    }

    private LeaveApply processResearchLeave(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id, String leaveTypeData) {

        if (leaveapplyrepo.checkForAlreadyAppliedOrNot(la.getFrom_date(), la.getLeave_id(), la.getEmp_id()) >= 1) {
            throw new RuntimeException("Research Leave already applied for the selected date");
        }

        EmployeeDetailsForLeavePattern employeeDetails = empDetail_repo.getEmployeeDetailsForLeavePatternWithEmployeeId(emp_id);

        if (ObjectUtils.isNotEmpty(employeeDetails)) {
            Integer leavePatternId = leave_pattern_repo.getLeavePatternIdEmpTypeIdAndJobTypeId(
                    employeeDetails.getEmpTypeId(), employeeDetails.getJobTypeId(), la.getLeave_id(),
                    employeeDetails.getSchoolId());
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + employeeDetails.getEmpTypeId() + "---" + employeeDetails.getJobTypeId() + "---" + la.getLeave_id() + "---" + employeeDetails.getSchoolId() + "---" + leavePatternId);
            Double availableLeaves = el_repo.getAvailableLeaves(leavePatternId, emp_id);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + availableLeaves);
            if (availableLeaves < la.getNo_of_days_applied()) {
                throw new RuntimeException("Available leaves are less than the applied days!");
            }
        }

        leaveApply = processLeaveApply(la, leaveApply, emp_id);
        return leaveApply;


    }

    private LeaveApply processMedicalOrSickLeave(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id, String leaveTypeData) {

        if (leaveapplyrepo.checkForAlreadyAppliedOrNot(la.getFrom_date(), la.getLeave_id(), la.getEmp_id()) >= 1) {
            throw new RuntimeException("Sick Leave already applied for the selected date");
        }

        if (la.getNo_of_days_applied() > 10) {
            throw new RuntimeException("SL cannot be applied more than 10 days!");
        }

        EmployeeDetailsForLeavePattern employeeDetails = empDetail_repo
                .getEmployeeDetailsForLeavePatternWithEmployeeId(emp_id);

        if (ObjectUtils.isNotEmpty(employeeDetails)) {
            Integer leavePatternId = leave_pattern_repo.getLeavePatternIdEmpTypeIdAndJobTypeId(
                    employeeDetails.getEmpTypeId(), employeeDetails.getJobTypeId(), la.getLeave_id(),
                    employeeDetails.getSchoolId());
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + employeeDetails.getEmpId() + "---" + employeeDetails.getJobTypeId() + "---" + la.getLeave_id() + "---" + employeeDetails.getSchoolId() + "---" + leavePatternId);

            Double availableLeaves = el_repo.getAvailableLeaves(leavePatternId, emp_id);

            if (la.getNo_of_days_applied() > availableLeaves) {
                throw new RuntimeException("Available leaves are less than the applied days!");
            }
        }

        Integer checkForPermanentStatus = empDetail_repo.checkForPermanentStatus(emp_id);
        if (checkForPermanentStatus != 2) {
            throw new RuntimeException(leaveTypeData + " can only be availed by permanent employees!");
        }

        leaveApply = processLeaveApply(la, leaveApply, emp_id);

        return leaveApply;
    }

    private LeaveApply processVacationLeave(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id, String leaveTypeData) {

        if (la.getNo_of_days_applied() > 7) {
            throw new RuntimeException("Vacation Leave cannot be applied for more than 7 days!");
        }

        if (leaveapplyrepo.checkVacationLeaveAlreadyAppliedOrNot(la.getFrom_date(), la.getLeave_id(), emp_id) >= 1) {
            throw new RuntimeException("Vacation Leave already applied for the selected month");
        }

        EmployeeDetailsForLeavePattern employeeDetails = empDetail_repo.getEmployeeDetailsForLeavePatternWithEmployeeId(emp_id);

        if (ObjectUtils.isNotEmpty(employeeDetails)) {
            Integer leavePatternId = leave_pattern_repo.getLeavePatternIdEmpTypeIdAndJobTypeId(
                    employeeDetails.getEmpTypeId(), employeeDetails.getJobTypeId(), la.getLeave_id(),
                    employeeDetails.getSchoolId());
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + employeeDetails.getEmpTypeId() + "---" + employeeDetails.getJobTypeId() + "---" + la.getLeave_id() + "---" + employeeDetails.getSchoolId() + "---" + leavePatternId);
            Double availableLeaves = el_repo.getAvailableLeaves(leavePatternId, emp_id);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + availableLeaves);
            if (availableLeaves < la.getNo_of_days_applied()) {
                throw new RuntimeException("Available leaves are less than the applied days!");
            }
        }

        if (vhc_repo.getFromDateAndSchoolIdAndLeaveId(la.getFrom_date(), la.getTo_date(), employeeDetails.getSchoolId(), la.getLeave_id()) == 0) {
            throw new RuntimeException("Selected dates are not in between the vacation window!!");
        }

        leaveApply = processLeaveApply(la, leaveApply, emp_id);
        return leaveApply;
    }

    private LeaveApply processMarriageLeave(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id) {

        if (la.getNo_of_days_applied() > 10) {
            throw new RuntimeException("Marriage leave cannot be applied for more than 10 days!");
        }

        EmployeeDetails employeeData = empDetail_repo.findById(emp_id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + emp_id));

        if (employeeData.getPermanent_status() == 2) {

            if (employeeData.getMartial_status().equalsIgnoreCase("M")) {
                throw new RuntimeException("Marriage leave can only be availed by unmarried employees!");
            }

            LeaveApply list_leaveApplyByLeaveType = leaveapplyrepo.checkValidationForLeaveByLeaveType(emp_id, la.getLeave_id());

            if (ObjectUtils.isEmpty(list_leaveApplyByLeaveType)) {
                leaveApply = createLeaveApplyData(leaveApply, emp_id, la);
            } else {
                throw new RuntimeException("Marriage leave already applied!!!");
            }

        } else {
            throw new RuntimeException("Marriage leave can only be availed by permanent employees!!!");
        }

        return leaveApply;
    }

    private LeaveApply processPaternityLeaveOrMaternityLeave(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id, String leaveTypeData) {
        EmployeeDetails employeeData = empDetail_repo.findById(emp_id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + emp_id));

        if (leaveTypeData.equalsIgnoreCase("ML")) {
            if (la.getNo_of_days_applied() > 180) {
                throw new RuntimeException("Maternity leave cannot be applied for more than 180 days!");
            }

            if(employeeData.getPaternity_status() != null && employeeData.getMaternity_status()){
                throw new RuntimeException("Maternity leave already applied!!!");
            }
        }

        if (leaveTypeData.equalsIgnoreCase("PL")) {
            if (la.getNo_of_days_applied() > 5) {
                throw new RuntimeException("Paternity leave cannot be applied for more than 5 days!");
            }

            if(employeeData.getPaternity_status() != null && employeeData.getPaternity_status()){
                throw new RuntimeException("Paternity leave already applied!!!");
            }
        }


        if (employeeData.getMartial_status().equalsIgnoreCase("U")) {
            throw new RuntimeException(leaveTypeData + " can only be availed by married employees!");
        }

        if (leaveTypeData.equalsIgnoreCase("PL") && employeeData.getGender().toString().equalsIgnoreCase("F")) {
            throw new RuntimeException(leaveTypeData + " can only be availed by male employees!");
        }

        if (leaveTypeData.equalsIgnoreCase("ML") && employeeData.getGender().toString().equalsIgnoreCase("M")) {
            throw new RuntimeException(leaveTypeData + " can only be availed by female employees!");
        }


        LeaveApply list_leaveApplyByLeaveType = leaveapplyrepo.checkValidationForLeaveByLeaveType(emp_id, la.getLeave_id());

        if (ObjectUtils.isEmpty(list_leaveApplyByLeaveType)) {

            String stringTypeOneDayPreviousDate = generateDateBeforeForValidation(la, 1);
            LeaveApply checkOneDayBeforeLeaveAppliedOrNot = leaveapplyrepo.checkOneDayBeforeLeaveAppliedOrNot(stringTypeOneDayPreviousDate, emp_id);
            System.out.println("stringTypeOneDayPreviousDate----------------------> " + stringTypeOneDayPreviousDate);
            if (ObjectUtils.isNotEmpty(checkOneDayBeforeLeaveAppliedOrNot) || checkOneDayBeforeLeaveAppliedOrNot != null) {
                throw new RuntimeException(leaveTypeData + " can't be clubbed with other leaves. Please check leave history!");
            }

            String stringTypeOneDayAfterDate = generateOneDayAfterDateForValidationOfClubbedLeaves(la, emp_id);
            LeaveApply checkOneDayAfterLeaveAppliedOrNot = leaveapplyrepo.checkOneDayAfterLeaveAppliedOrNot(stringTypeOneDayAfterDate, emp_id);
            System.out.println("stringTypeOneDayAfterDate----------------------> " + stringTypeOneDayAfterDate);
            if (ObjectUtils.isNotEmpty(checkOneDayAfterLeaveAppliedOrNot) || checkOneDayAfterLeaveAppliedOrNot != null) {
                throw new RuntimeException(leaveTypeData + " can't be clubbed with other leaves. Please check leave history!");
            }
            leaveApply = createLeaveApplyData(leaveApply, emp_id, la);
            getLeavePatternIdAndUpdateAccumulatedCount(la, emp_id);

            if(leaveTypeData.equalsIgnoreCase("PL")) updatePaternityStatus(emp_id);
            else updateMaternityStatus(emp_id);

        } else {
            throw new RuntimeException("Paternity leave already applied!!!");
        }
        return leaveApply;
    }

    private void updateMaternityStatus(Integer empId) {
        empDetail_repo.updateMaternityStatus(empId);
    }

    private void updatePaternityStatus(Integer empId) {
        empDetail_repo.updatePaternityStatus(empId);
    }

    private LeaveApply processAbsentLeave(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id) {

        if (leaveapplyrepo.checkForAlreadyAppliedOrNot(la.getFrom_date(), la.getLeave_id(), la.getEmp_id()) >= 1) {
            throw new RuntimeException("Absent Leave already applied for the selected date");
        }
        return createLeaveApplyData(leaveApply, emp_id, la);
    }

    private LeaveApply processOneHourPermission(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id) {

        EmployeeDetails employeeData = empDetail_repo.findById(emp_id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + emp_id));
        Integer month = Integer.parseInt(la.getFrom_date().substring(3, 5));
        Integer count = leaveapplyrepo.getCountOfAppliedOneHourPermission(emp_id, month, la.getLeave_id());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = df.parse(la.getFrom_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        if (count < 1) {
            if (la.getShift().equalsIgnoreCase("FirstHalf")) {

                Time punchInTime = bio_repo.getPunchInTiming(emp_id, la.getFrom_date());
                LocalTime firstEntryTime = null;
                LocalTime modifiedEntryTime = null;
                if (ObjectUtils.isNotEmpty(punchInTime)) {
                    firstEntryTime = punchInTime.toLocalTime().withSecond(0);
                    modifiedEntryTime = firstEntryTime.plusHours(5).plusMinutes(30).withSecond(0);
                }

                if (punchInTime == null) {
                    throw new RuntimeException("Punch-in data is not present !!!");
                } else {
                    String shiftStartTime = shift_repo.getShiftData(employeeData.getShift_category_id())
                            .getShiftStartTime();
                    Date d = null;
                    Date d1 = null;
                    try {
                        d = dateFormat.parse(modifiedEntryTime.toString());
                        d1 = dateFormat.parse(shiftStartTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Pppppppppppppppppppppppppppppppppppppppppppppppp " + shiftStartTime);
                    System.out.println("Pppppppppppppppppppppppppppppppppppppppppppppppp " + d);
                    System.out.println("Pppppppppppppppppppppppppppppppppppppppppppppppp " + d1);
                    long difference = d.getTime() - d1.getTime();
                    long differenceHours = difference / (60 * 60 * 1000) % 24;
                    long differenceMinutes;
                    if (differenceHours == 0) {
                        differenceMinutes = difference / (60 * 1000) % 60;
                    } else {
                        differenceMinutes = (difference / (60 * 1000) % 60) + 60 * differenceHours;
                    }
                    System.out.println("Pppppppppppppppppppppppppppppppppppppppppppppppp " + differenceMinutes);
                    if (differenceMinutes <= 60) {

                        leaveApply = createLeaveApplyData(leaveApply, emp_id, la);

                    } else {
                        throw new RuntimeException(
                                "Not eligible to apply for 1 hr permission as punch-in time is more than an hour from shift start time");
                    }
                }
            } else {

                Time punchOutTime = bio_repo.getPunchOutTiming(emp_id, la.getFrom_date());

                LocalTime firstExitTime = null;
                LocalTime modifiedExitTime = null;
                if (ObjectUtils.isNotEmpty(punchOutTime)) {
                    firstExitTime = punchOutTime.toLocalTime().withSecond(0);
                    modifiedExitTime = firstExitTime.plusHours(5).plusMinutes(30).withSecond(0);
                }

                if (punchOutTime == null) {
                    throw new RuntimeException("Punch-out data is not present !!!");
                } else {
                    String shiftEndTime = shift_repo.getShiftData(employeeData.getShift_category_id())
                            .getShiftEndTime(); // .substring(0, 2));

                    Date d = null;
                    Date d1 = null;
                    try {
                        d = dateFormat.parse(modifiedExitTime.toString());
                        d1 = dateFormat.parse(shiftEndTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long difference = d1.getTime() - d.getTime();
                    long differenceHours = difference / (60 * 60 * 1000) % 24;
                    long differenceMinutes;
                    if (differenceHours == 0) {
                        differenceMinutes = difference / (60 * 1000) % 60;
                    } else {
                        differenceMinutes = (difference / (60 * 1000) % 60) + 60 * differenceHours;
                    }

                    if (differenceMinutes <= 60) {
                        leaveApply = createLeaveApplyData(leaveApply, emp_id, la);

                    } else {
                        throw new RuntimeException(
                                "Not eligible to apply for 1 hr permission as punch-out time is less than an hour from shift end time");
                    }
                }
            }

        } else {
            throw new RuntimeException("Already applied for one hour permission this month");
        }
        return leaveApply;
    }

    private LeaveApply processComponsatoryLeave(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer l, String leaveTypeData) {

        if (leaveapplyrepo.checkForAlreadyAppliedOrNot(la.getFrom_date(), la.getLeave_id(), la.getEmp_id()) >= 1) {
            throw new RuntimeException("Leave already applied for the selected date");
        }

        if (leaveapplyrepo.checkForCompOffAlreadyAppliedOrNot(la.getCompoff_worked_date(), la.getLeave_id(), la.getEmp_id()) >= 1) {
            throw new RuntimeException("Comp-off has already been applied for the selected compoff worked date.");
        }

        stringTypefromDateWithYmdFormat = formatDateYmdFormat(la.getFrom_date());
        stringTypeToDateWithYmdFormat = formatDateYmdFormat(la.getTo_date());

        // again convert to Date format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = df.parse(stringTypefromDateWithYmdFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            date2 = df.parse(stringTypeToDateWithYmdFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//		LocalDate todaysDate = LocalDate.now();
        LocalDate fromDate = LocalDate.fromDateFields(date1);
        LocalDate toDate = LocalDate.fromDateFields(date2);
        Integer daysDiff = Days.daysBetween(fromDate, toDate).getDays();
        System.out.println("DAAAAAAAAAAAAAAAAAAAAAYYYYYYYYYYYYYYYYYYYYYSSSSSSSSSSSSSS " + daysDiff);
        if (daysDiff <= 90) {

            BiometricAttendance attendanceData = biometricAttendanceRepository.getAttendanceData(la.getCompoff_worked_date(), l);

            if (ObjectUtils.isEmpty(attendanceData)) {
//				Integer leaveTypeId = s_repo.getLeaveIdOfVacationLeave(leaveTypeData);
                Integer onDutyLeaveId = s_repo.getOnDutyLeaveId();
                System.out.println("000000000000000000000 " + leaveapplyrepo.checkForAppliedOnDuty(la.getCompoff_worked_date(), l, onDutyLeaveId));
                if (leaveapplyrepo.checkForAppliedOnDuty(la.getCompoff_worked_date(), l, onDutyLeaveId) >= 1) {

                    leaveApply.setActive(la.getActive());
                    leaveApply = createLeaveApplyData(leaveApply, l, la);

                } else {
                    throw new RuntimeException(
                            "Comp-Off can't be applied! Biometric data not found or OD not applied/approved!");
                }

            } else {
                if (attendanceData.getCompoffStatus() == Boolean.TRUE) {
                    System.out.println("222222222222222222222222222 " + attendanceData.getCompoffStatus());
                    leaveApply = createLeaveApplyData(leaveApply, l, la);
                } else {
                    throw new RuntimeException(
                            "Selected date is not assigned as GH, DH, Week-Off or punching duration is less than 4 hours 10 minutes!");
                }
            }
        } else {
            throw new RuntimeException("Comp-Off expired as it exceeded the limit of 90 days!");
        }
        return leaveApply;
    }

    private LeaveApply processRestrictedHoliday(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id) {

        EmployeeDetailsForLeavePattern employeeDetails = empDetail_repo
                .getEmployeeDetailsForLeavePatternWithEmployeeId(emp_id);

        HolidayCalender holidayCalendar = holiday_repo.getHolidayDetailsFromDateAndLeaveId(la.getFrom_date(), la.getLeave_id());

        if (ObjectUtils.isEmpty(holidayCalendar)) {
            throw new RuntimeException("Selected date is not declared as Restricted Holiday !");
        }

        if (leaveapplyrepo.checkForAlreadyAppliedOrNot(la.getFrom_date(), la.getLeave_id(), la.getEmp_id()) >= 1) {
            throw new RuntimeException("RH already applied for the selected date");
        }

        if (ObjectUtils.isNotEmpty(employeeDetails)) {
            Integer leavePatternId = leave_pattern_repo.getLeavePatternIdEmpTypeIdAndJobTypeId(
                    employeeDetails.getEmpTypeId(), employeeDetails.getJobTypeId(), la.getLeave_id(),
                    employeeDetails.getSchoolId());
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + employeeDetails.getEmpTypeId() + "---" + employeeDetails.getJobTypeId() + "---" + la.getLeave_id() + "---" + employeeDetails.getSchoolId() + "---" + leavePatternId);

            Double availableLeaves = el_repo.getAvailableLeaves(leavePatternId, emp_id);

            if (la.getNo_of_days_applied() > availableLeaves) {
                throw new RuntimeException("Available leaves are less than the applied days!");
            }
        }

        System.out.println("REACHED - 111111111111111 ---------------------------> ");
        leaveApply = processLeaveApply(la, leaveApply, emp_id);

        return leaveApply;
    }

    private LeaveApply processManualAttendance(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id) {

        LeaveApply list_leaveApplyByLeaveType = leaveapplyrepo
                .checkValidationForLeaveOnFromDateByLeaveType(la.getFrom_date(), emp_id, la.getLeave_id());
        if (ObjectUtils.isEmpty(list_leaveApplyByLeaveType)) {
            leaveApply = createLeaveApplyData(leaveApply, emp_id, la);

        } else {
            throw new RuntimeException("Manual Attendance(MA) is already applied for the selected date!");
        }
        return leaveApply;
    }

    private LeaveApply processSpecialLeave(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id, String leaveTypeData) {

        LeaveApply list_leaveApplyByLeaveType = leaveapplyrepo
                .checkValidationForLeaveOnFromDateByLeaveType(la.getFrom_date(), emp_id, la.getLeave_id());
        if (ObjectUtils.isEmpty(list_leaveApplyByLeaveType)) {
            leaveApply = createLeaveApplyData(leaveApply, emp_id, la);

        } else {
            throw new RuntimeException("Special Leave(SP) is already applied for the selected date!");
        }
        return leaveApply;
    }

    private LeaveApply processOnDutyOrExamDutyOrUniversityDuty(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer l, String leaveTypeData) {

        LeaveApply list_leaveApplyByLeaveType = leaveapplyrepo
                .checkValidationForLeaveOnFromDateByLeaveType(la.getFrom_date(), l, la.getLeave_id());

        if (ObjectUtils.isEmpty(list_leaveApplyByLeaveType) || list_leaveApplyByLeaveType == null) {

            stringTypefromDateWithYmdFormat = formatDateYmdFormat(la.getFrom_date());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date1 = df.parse(stringTypefromDateWithYmdFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCC " + date1);
            BiometricAttendance attendanceData = biometricAttendanceRepository.getAttendanceData(la.getFrom_date(), l);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " + attendanceData);
            if (ObjectUtils.isNotEmpty(attendanceData) || attendanceData != null) {
                if (attendanceData.getAttendanceStatus() == 1) {
                    throw new RuntimeException("Oops! can't apply for " + leaveTypeData + " as punching details of 8 hours is present for the selected date");
                } else if (attendanceData.getAttendanceStatus() == 0.5) {
                    if (la.getNo_of_days_applied() == 0.5) {
                        leaveApply = createLeaveApplyData(leaveApply, l, la);
                    } else {
                        throw new RuntimeException("Half day punching found, please initiate for half day leave!");
                    }
                } else if (attendanceData.getAttendanceStatus() == 0) {
                    leaveApply = createLeaveApplyData(leaveApply, l, la);
                }


            } else {
                leaveApply = createLeaveApplyData(leaveApply, l, la);
            }
        } else {
            throw new RuntimeException("Oops! " + leaveTypeData + " is already applied for the selected date!");
        }

        return leaveApply;

    }


    Integer maxLeaveDays = 7;
    String[] daysOfWeek = {"", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
    LeaveApply leaveApplied = null;
    DateTime date = null;
    String dayOfWeekString = null;
    Integer dayOfWeekInt = 0;
    boolean leaveAppliedSuccessfully = false;
    Integer coutNoOfDaysApplied = 0;

    private LeaveApply processCasualOrEarnedLeave(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id, String leaveTypeData, Integer roleId) {
        
        System.out.println("LEAVE ID : " + la.getLeave_id());
        EmployeeDetailsForLeavePattern employeeDetails = empDetail_repo.getEmployeeDetailsForLeavePatternWithEmployeeId(emp_id);

        if (ObjectUtils.isNotEmpty(employeeDetails)) {
            Integer leavePatternId = leave_pattern_repo.getLeavePatternIdEmpTypeIdAndJobTypeId(
                    employeeDetails.getEmpTypeId(), employeeDetails.getJobTypeId(), la.getLeave_id(),
                    employeeDetails.getSchoolId());
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + employeeDetails.getEmpTypeId() + "---" + employeeDetails.getJobTypeId() + "---" + la.getLeave_id() + "---" + employeeDetails.getSchoolId() + "---" + leavePatternId);
            Double availableLeaves = el_repo.getAvailableLeaves(leavePatternId, emp_id);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA " + availableLeaves);
            if (availableLeaves < la.getNo_of_days_applied()) {
                throw new RuntimeException("Available leaves are less than the applied days!");
            }
        }

        if (ObjectUtils.isEmpty(list_leaveApply) || list_leaveApply == null) {

            System.out.println("REACHED - 0000000000000000000000000000000 ---------------------------> ");
            leaveApply = processLeaveApply(la, leaveApply, emp_id);

        } else {

//			validateOneByOneDate(la, emp_id);
            for (int i = 1; i <= maxLeaveDays; i++) {

                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA " + i);
                String dateToCheck = generateDateBeforeForValidation(la, i);
                System.out.println("111111111111111111111 " + dateToCheck);

                // Check if leave is applied on the generated date
                leaveApplied = leaveapplyrepo.checkOneDayBeforeLeaveAppliedOrNot(dateToCheck, emp_id);

                if (i == 1) {

                    formatter = DateTimeFormat.forPattern("dd-MM-yyyy");
                    date = formatter.parseDateTime(dateToCheck);
                    dayOfWeekInt = date.getDayOfWeek();  // 1 = Monday, 7 = Sunday
                    System.out.println("AAAAAAAAAAAAA " + dayOfWeekInt);
                    dayOfWeekString = daysOfWeek[dayOfWeekInt];
                    System.out.println("BBBBBBBBBBBBBBBBBBBB " + dayOfWeekString);
                    System.out.println("************** " + date);

                    if (ObjectUtils.isEmpty(leaveApplied) && !dayOfWeekString.equalsIgnoreCase("SUNDAY")) {

                        leaveAppliedSuccessfully = true;
                        break;
                    }

                    if (ObjectUtils.isEmpty(leaveApplied) && dayOfWeekString.equalsIgnoreCase("SUNDAY")) {

                        dateToCheck = generateDateBeforeForValidation(la, i + 1);
                        System.out.println("2222222222222222222 " + dateToCheck);
                        LeaveApply leaveApplied2 = leaveapplyrepo.checkOneDayBeforeLeaveAppliedOrNot(dateToCheck, emp_id);

                        if (ObjectUtils.isNotEmpty(leaveApplied2)) {
                            String leaveType2 = s_repo.getleaveTypeData(leaveApplied2.getLeave_id());
                            if (!leaveTypeData.equalsIgnoreCase(leaveType2)) {
                                throw new RuntimeException(leaveTypeData + " cannot be clubbed with " + leaveType2 + ".");
                            }

                        }
                        leaveAppliedSuccessfully = true;
                        break;
                    }


                    if (ObjectUtils.isNotEmpty(leaveApplied)) {
                        System.out.println("FROMMMMMMMMMMMMMMM " + leaveApplied.getFrom_date());
                        String leaveType = s_repo.getleaveTypeData(leaveApplied.getLeave_id());
                        if (leaveApplied.getNo_of_days_applied() == 0.5) {
                            BiometricAttendance attendanceData = bio_repo.getAttendanceData(leaveApplied.getFrom_date(), emp_id);
                            if (attendanceData != null && !attendanceData.getPresentStatus().equalsIgnoreCase("a/p") &&
                                    !attendanceData.getPresentStatus().equalsIgnoreCase("p/a")) {
                                if (!leaveTypeData.equalsIgnoreCase(leaveType)) {
                                    throw new RuntimeException(leaveTypeData + " cannot be clubbed with " + leaveType + ".");
                                }
                            }
                        } else if (!leaveTypeData.equalsIgnoreCase(leaveType)) {
                            throw new RuntimeException(leaveTypeData + " cannot be clubbed with " + leaveType + ".");
                        }

                        if (leaveApplied.getNo_of_days_applied() + la.getNo_of_days_applied() > maxLeaveDays) {
                            throw new RuntimeException(leaveTypeData + " cannot be applied for more than " + maxLeaveDays + "consecutive days.");
                        } else {
                            leaveAppliedSuccessfully = true;
                            break;
                        }
                    }

                }


                // If leave was applied on this date, continue with the validation logic
                if (ObjectUtils.isNotEmpty(leaveApplied)) {
                    // Validate if the leave type can be clubbed
                    String leaveType = s_repo.getleaveTypeData(leaveApplied.getLeave_id());
                    // For other days, validate the leave type
                    System.out.println("ZZZZZZZZZZZZZZZZZZZZZ " + i);
                    String dateToCheck3 = generateDateBeforeForValidation(la, i);
                    System.out.println("6666666666666666666 " + dateToCheck3);
                    LeaveApply leaveApplied3 = leaveapplyrepo.checkOneDayBeforeLeaveAppliedOrNot(dateToCheck3, emp_id);

                    if (ObjectUtils.isNotEmpty(leaveApplied3)) {
                        String leaveType3 = s_repo.getleaveTypeData(leaveApplied.getLeave_id());
                        if (!leaveTypeData.equalsIgnoreCase(leaveType3)) {
                            throw new RuntimeException(leaveTypeData + " cannot be clubbed with " + leaveType + ".");
                        }
                    }
                }

            }
        }

        // If all validations pass, process the leave application
        if (leaveAppliedSuccessfully) {
            leaveApply = processLeaveApply(la, leaveApply, emp_id);
        }

        return leaveApply;
    }

    private LeaveApply processLeaveApply(@Valid LeaveApplyDto la, LeaveApply leaveApply, Integer emp_id) {

        la.setLeave_approved_by1(empDetail_repo.getaApprover1_id(emp_id) == null ? null
                : empDetail_repo.getaApprover1_id(emp_id));
        la.setLeave_approved_by2(empDetail_repo.getaApprover2_id(emp_id) == null ? null
                : empDetail_repo.getaApprover2_id(emp_id).toString());
        leaveApply = createLeaveApplyData(leaveApply, emp_id, la);
        getLeavePatternIdAndUpdateAccumulatedCount(la, emp_id);

//		LeaveKittyEvent leaveKittyEvent = new LeaveKittyEvent("leaveKitty");
//		applicationEventPublisher.publishEvent(leaveKittyEvent);
        return leaveApply;

    }

    private String generateDateBeforeForValidation(LeaveApplyDto la, Integer daysBefore) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date givenDate = null;
        try {
            givenDate = dateFormat.parse(la.getFrom_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(givenDate);
        calendar.add(Calendar.DAY_OF_MONTH, -daysBefore);
        Date previousDay = calendar.getTime();
        String previousDayString = dateFormat.format(previousDay);
//		String previousDayStringYmdFormat = formatDateYmdFormat(previousDayString);

        return previousDayString;
    }

    private String generateTwoDayBeforeDateForValidationoFClubbedLeaves(LeaveApplyDto la, Integer l) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date givenDate = null;
        try {
            givenDate = dateFormat.parse(la.getFrom_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(givenDate);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        Date previousDay = calendar.getTime();
        String previousDayString = dateFormat.format(previousDay);
//		String previousDayStringYmdFormat = formatDateYmdFormat(previousDayString);

        return previousDayString;
    }

    private String generateThreeDayBeforeDateForValidationoFClubbedLeaves(LeaveApplyDto la, Integer l) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date givenDate = null;
        try {
            givenDate = dateFormat.parse(la.getFrom_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(givenDate);
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        Date previousDay = calendar.getTime();
        String previousDayString = dateFormat.format(previousDay);
//		String previousDayStringYmdFormat = formatDateYmdFormat(previousDayString);

        return previousDayString;
    }

    private String generateOneDayAfterDateForValidationOfClubbedLeaves(LeaveApplyDto la, Integer l) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date givenDate = null;
        try {
            givenDate = dateFormat.parse(la.getTo_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(givenDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date nextDay = calendar.getTime();
        String nextDayString = dateFormat.format(nextDay);
//		String previousDayStringYmdFormat = formatDateYmdFormat(previousDayString);

        return nextDayString;
    }

    private Boolean validateOneByOneDate(LeaveApplyDto la, Integer emp_id) {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date1 = df.parse(la.getFrom_date());
            date2 = df.parse(la.getTo_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LocalDate fromDate = LocalDate.fromDateFields(date1);
        LocalDate toDate = LocalDate.fromDateFields(date2);
        Integer daysDiff = Days.daysBetween(fromDate, toDate).getDays();

        date_to_increment = date1;
        for (int i = 0; i <= daysDiff; i++) {
            String dfdf = new SimpleDateFormat("dd-MM-yyyy").format(date_to_increment);
            System.out.println("FORMATTTED DATE ----------------------------------- > " + date_to_increment);
            list_leaveApply.forEach(leave -> {
                int count = leaveapplyrepo.checkValidationForDatesAppliedAndLeaveType(dfdf, leave.getFrom_date(), leave.getTo_date(), emp_id);
                if (count >= 1) {
                    throw new RuntimeException("Leave already applied for the selected date!!!");
                }
            });

            // increment date by 1
            Calendar c = Calendar.getInstance();
            c.setTime(date_to_increment);
            c.add(Calendar.DATE, 1);
            date_to_increment = c.getTime();
            System.out.println("DATE ----------------------------------- > " + date_to_increment);

            //we can use LocalDate also as it provides several utility methods
//			LocalDate previousDay = fromDate.minusDays(1);
//			System.out.println("LOCAL DATE ----------------------------------- > "+previousDay);

        }

        return true;
    }

    private void getLeavePatternIdAndUpdateAccumulatedCount(LeaveApplyDto la, Integer emp_id) {

        EmployeeDetailsForLeavePattern elp = empDetail_repo
                .getEmployeeDetailsForLeavePatternWithEmployeeId(emp_id);

        if (ObjectUtils.isNotEmpty(elp)) {
            Integer leavePatternId = leave_pattern_repo.getLeavePatternIdEmpTypeIdAndJobTypeId(elp.getEmpTypeId(), elp.getJobTypeId(),
                    la.getLeave_id(), elp.getSchoolId());
            if (ObjectUtils.isNotEmpty(leavePatternId)) {
                updateAccumulatedDays(leavePatternId, la, emp_id);
            }
        }
    }

    private void updateAccumulatedDays(Integer leavePatternId, LeaveApplyDto la, Integer emp_id) {

        String leaveTypeData = s_repo.getleaveTypeData(la.getLeave_id());

        Double accumulatedDays = el_repo.getAcculamatedDaysCount(leavePatternId, emp_id);
        if (accumulatedDays < la.getNo_of_days_applied()){
            throw new RuntimeException("Available leaves are less than applied leaves!!!");
        }
        System.out.println("AVAILABLE LEAVES : ======== " + accumulatedDays);
        Double updatedCount;
        if (leaveTypeData.equalsIgnoreCase("VL")) {
            updatedCount = accumulatedDays - 7;
        } else {
            updatedCount = accumulatedDays - la.getNo_of_days_applied();
        }
        System.out.println("UPDATED LEAVES 	 : ======== " + updatedCount);
        if (updatedCount >= 0.0) {
            el_repo.updateAcculamatedDaysCoun(leavePatternId, emp_id, updatedCount);
        }
    }

    // convert to yyyy-mm-dd Date Format
    private String formatDateYmdFormat(String from_date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the input date string into a Date object
            Date date = inputFormat.parse(from_date);

            // Format the Date object into the desired output format
            stringTypefromDateWithYmdFormat = outputFormat.format(date);

            // Print the output date
            System.out.println("Converted date yyyy-mm-dd: " + stringTypefromDateWithYmdFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringTypefromDateWithYmdFormat;

    }

    private LeaveApply createLeaveApplyData(LeaveApply leaveApply, Integer l, @Valid LeaveApplyDto la) {

        Integer leaveApprover1 = empDetail_repo.getaApprover1_id(l);
        Integer leaveApprover2 = empDetail_repo.getaApprover2_id(l);

        leaveApply.setActive(la.getActive());
        leaveApply.setAlternative_emails(la.getAlternative_emails());
        leaveApply.setAlternative_lecturer_name(la.getAlternative_lecturer_name());
        leaveApply.setApproved_status(la.getApproved_status());
        leaveApply.setCancel_by(la.getCancel_by());
        leaveApply.setCancel_comments(la.getCancel_comments());
        leaveApply.setCancel_date(la.getCancel_date());
        leaveApply.setCompoff_worked_date(la.getCompoff_worked_date());
        leaveApply.setContact_no(la.getContact_no());
        leaveApply.setCreated_by(la.getCreated_by());
        leaveApply.setCreated_date(la.getCreated_date());
        leaveApply.setCreated_username(la.getCreated_username());
        leaveApply.setDept_name_short(la.getDept_name_short());
        leaveApply.setEmp_id(l);
        leaveApply.setEmployee_leave_id(la.getEmployee_leave_id());
        leaveApply.setFrom_date(la.getFrom_date());
        leaveApply.setLeave_app1_status(la.getLeave_app1_status());
        leaveApply.setLeave_app2_status(la.getLeave_app2_status());
        leaveApply.setLeave_apply_attachment_path(la.getLeave_apply_attachment_path());
        leaveApply.setLeave_approved2_date(la.getLeave_approved2_date());
        leaveApply.setLeave_approved_date(la.getLeave_approved_date());
        leaveApply.setLeave_comments(la.getLeave_comments());
        leaveApply.setLeave_id(la.getLeave_id());
        leaveApply.setModified_by(la.getModified_by());
        leaveApply.setModified_date(la.getModified_date());
        leaveApply.setModified_username(la.getModified_username());
        leaveApply.setNo_of_days_applied(la.getNo_of_days_applied());
        leaveApply.setRemaining_days_left(la.getRemaining_days_left());
        leaveApply.setReporting_approver1_comment(la.getReporting_approver1_comment());
        leaveApply.setReporting_approver_comment(la.getReporting_approver_comment());
        leaveApply.setShift(la.getShift());
        leaveApply.setSpecial_leave_id(la.getSpecial_leave_id());
        leaveApply.setTo_date(la.getTo_date());
        leaveApply.setTotal_leaves_applicable(la.getTotal_leaves_applicable());
        leaveApply.setWeb_status(la.getWeb_status());
        leaveApply.setYear(la.getYear());
        leaveApply.setLeave_approved_by1(leaveApprover1);
        leaveApply.setLeave_approved_by2(leaveApprover2.toString());

//		leaveapplyrepo.save(leaveApply);
//		list.add(leaveApply);
        return leaveApply;

    }

    private int getEmp_id(Integer emp_id) {

        return leaveapplyrepo.getcountOfEmp_id(emp_id);
    }

    private int getEmployee_leave_id(Integer employee_leave_id) {

        return leaveapplyrepo.getcountOfEmployee_leave_id(employee_leave_id);
    }

    public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword) {
        Page<Object> roles_filtered_response = leaveapplyrepo.getAllDataFilteredByKeyword(pageable, keyword);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
    }

    public ResponseEntity<Object> getAllSortedData(Pageable pageable) {
        Page<Object> roles_sorted_response = leaveapplyrepo.getAllSortedData(pageable);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
    }

    public List<LeaveApply> listAll1() {
        return leaveapplyrepo.findAll11();
    }

    public LeaveApply get(Integer id) {
        return leaveapplyrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Apply Not Found:" + id));
    }

    public LeaveApply saveLeaveApply(LeaveApply leaveApply) {
        EmployeeDetailsForLeavePattern employeeDetailsForLeavePattern = empDetail_repo
                .getEmployeeDetailsForLeavePatternWithEmployeeId(leaveApply.getEmp_id());
        if (leaveApply.getApproved_status() == 3) {
            if (ObjectUtils.isNotEmpty(employeeDetailsForLeavePattern)) {
                Integer leavePatternId = leave_pattern_repo.getLeavePatternIdEmpTypeIdAndJobTypeId(
                        employeeDetailsForLeavePattern.getEmpTypeId(),
                        employeeDetailsForLeavePattern.getJobTypeId(), leaveApply.getLeave_id(),
                        employeeDetailsForLeavePattern.getSchoolId());
                if (ObjectUtils.isNotEmpty(leavePatternId)) {
                    System.out.println("11111111111111111111111111111111111111 " + leavePatternId);
                    System.out.println(
                            "0000000000000000000000000000000000000000000000 " + leaveApply.getLeave_id());
                    System.out.println("22222222222222222222222222222222222222 " + leaveApply.getEmp_id());
                    Double ac = el_repo.getAcculamatedDaysCount(leavePatternId, leaveApply.getEmp_id());
                    System.out.println("333333333333333333333333333333333333333333333333333333 " + ac);
                    Double updateCount = ac + leaveApply.getNo_of_days_applied();
                    System.out.println(
                            "$$$$$$$$$$44444444444444444444444444444444444444444444444444 " + updateCount);
                    el_repo.updateAcculamatedDaysCountAfterCancel(leavePatternId, leaveApply.getEmp_id(),
                            updateCount);

                }
            }

        }
        return leaveapplyrepo.save(leaveApply);
    }

    public void delete(Integer id) {
        LeaveApply la = leaveapplyrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Apply Not Found:" + id));
        leaveapplyrepo.updateDept(id);
    }

    public void delete1(Integer id) {
        LeaveApply la = leaveapplyrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Apply Not Found:" + id));
        leaveapplyrepo.updateDept1(id);
    }

    public void uploadFile(MultipartFile multipartFile, List<Integer> leave_apply_id) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + leave_apply_id);

        leave_apply_id.stream().forEach(id -> {

            LeaveApply leaveapply = new LeaveApply();
            try {
                File file = convertMultiPartToFile(multipartFile);
                String fileName = generateFileName(multipartFile);
                String t2 = leaveapply.setLeave_apply_attachment_path(LocalDate.now() + "/" + id + "/" + fileName);
                System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] " + t2);
                uploadFileTos3bucket(fileName, file, id);
                file.delete();
                leaveapplyrepo.updatePath(id, t2);
            } catch (AmazonServiceException ase) {

                logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
                logger.info("Error Message:    " + ase.getMessage());
                logger.info("HTTP Status Code: " + ase.getStatusCode());
                logger.info("AWS Error Code:   " + ase.getErrorCode());
                logger.info("Error Type:       " + ase.getErrorType());
                logger.info("Request ID:       " + ase.getRequestId());

            } catch (AmazonClientException ace) {
                logger.info("Caught an AmazonClientException: ");
                logger.info("Error Message: " + ace.getMessage());
            } catch (IOException ioe) {
                logger.info("IOE Error Message: " + ioe.getMessage());

            }

        });

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

    private void uploadFileTos3bucket(String fileName, File file, Integer leave_apply_id) {
        final String uniqueFileName = value + "/" + LocalDate.now() + "/" + leave_apply_id + "/" + fileName; // file.getName()
        s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

    }

    public byte[] viewFiles(String fileName) throws NoSuchFileException {
        try {
            byte[] content;
            final S3Object s3Object = s3client.getObject(bucketName, value + "/" + fileName);
            final S3ObjectInputStream stream = s3Object.getObjectContent();
            content = IOUtils.toByteArray(stream);
            System.out.println(content);
            s3Object.close();
            return content;

        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new NoSuchFileException("File Not Found");
            }
            throw new AmazonClientException("", e);
        } catch (IOException | AmazonClientException ex) {
            throw new AmazonClientException("", ex);
        }

    }

    public void uploadFile2(MultipartFile multipartFile, List<Integer> leave_apply_id) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + leave_apply_id);

        leave_apply_id.stream().forEach(id -> {

            LeaveApply leaveapply = new LeaveApply();
            try {
                File file = convertMultiPartToFile2(multipartFile);
                String fileName = generateFileName2(multipartFile);
                String t2 = leaveapply.setLeave_apply_attachment_path2(LocalDate.now() + "/" + id + "/" + fileName);
                System.out.println("[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]] " + t2);
                uploadFileTos3bucket2(fileName, file, id);
                file.delete();
                leaveapplyrepo.updatePath2(id, t2);
            } catch (AmazonServiceException ase) {

                logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
                logger.info("Error Message:    " + ase.getMessage());
                logger.info("HTTP Status Code: " + ase.getStatusCode());
                logger.info("AWS Error Code:   " + ase.getErrorCode());
                logger.info("Error Type:       " + ase.getErrorType());
                logger.info("Request ID:       " + ase.getRequestId());

            } catch (AmazonClientException ace) {
                logger.info("Caught an AmazonClientException: ");
                logger.info("Error Message: " + ace.getMessage());
            } catch (IOException ioe) {
                logger.info("IOE Error Message: " + ioe.getMessage());

            }

        });

    }

    private File convertMultiPartToFile2(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName2(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket2(String fileName, File file, Integer leave_apply_id) {
        final String uniqueFileName = value + "/" + LocalDate.now() + "/" + leave_apply_id + "/" + fileName; // file.getName()
        s3client.putObject(new PutObjectRequest(bucketName, uniqueFileName, file));

    }

    public byte[] viewFiles2(String fileName) throws NoSuchFileException {
        try {
            byte[] content;
            final S3Object s3Object = s3client.getObject(bucketName, value + "/" + fileName);
            final S3ObjectInputStream stream = s3Object.getObjectContent();
            content = IOUtils.toByteArray(stream);
            System.out.println(content);
            s3Object.close();
            return content;

        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new NoSuchFileException("File Not Found");
            }
            throw new AmazonClientException("", e);
        } catch (IOException | AmazonClientException ex) {
            throw new AmazonClientException("", ex);
        }

    }

    public List<EmployeeDetails> listEmailByDept1(Integer emp_id) {
        return empDetail_repo.listEmailByDept11(emp_id);
    }

    public Double getUpdatedDaysCount(Integer emp_id, Integer leave_id) {
        EmployeeDetails emp_details = empDetail_repo.getEmployeeDetailsByEmployeeId(emp_id);
        java.time.LocalDate date = java.time.LocalDate.now();
        // LocalDate date1 = LocalDate.fromDateFields(date);
        Integer leave_pattern_id = leave_pattern_repo.fetchLeavePatternId1(emp_details.getEmp_type_id(),
                emp_details.getJob_type_id(), emp_details.getSchool_id(), date.getYear(), leave_id);
        return leaveKittyRepository.getUpdatedDaysCount(emp_id, leave_pattern_id);
    }

    public Boolean checkAvailabilityOfCompOff(Integer emp_id, Date date) {
        return bio_repo.checkAvailabilityOfCompOff(emp_id, date);
    }

    public List<HashMap<String, Object>> getLeaveKettyDetails(Integer user_id) {

        List<HashMap<String, Object>> sh3 = new ArrayList<HashMap<String, Object>>();

        Integer emp_id = uar_repo.getEmployee_id(user_id);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + emp_id);
//		List<String>  fromDateDetails = leaveapplyrepo.getFromDateDetails(user_id);
        List<Integer> calenderYear = calender_year_repo.getCalenderYear();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + calenderYear);

        calenderYear.parallelStream().forEachOrdered(fdd -> {
            List<Integer> leaveIdDetails = leaveapplyrepo.getLeaveIdByUserId(emp_id, fdd);
            System.out.println("################################################### " + leaveIdDetails);
            System.out.println("*********************************************** " + fdd);
            List<HashMap<String, Object>> sh1 = new ArrayList<HashMap<String, Object>>();
            leaveIdDetails.parallelStream().forEachOrdered(ld -> {
                HashMap<String, Object> sh = new HashMap<String, Object>();
                Double count = leaveapplyrepo.getCountOfLeaveType(ld, fdd, emp_id);
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + count);
                String leaveName = leaveapplyrepo.getNameOfLeave(ld);
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + leaveName);

                if (count != null) {
                    sh.put("leave_count", count);
                    sh.put("year", fdd);
                    sh.put("leave_id", ld);
                    sh.put("leaveName", leaveName);
                    sh1.add(sh);
                }
            });

            sh3.addAll(sh1);
        });

        return sh3;
    }

    public ResponseEntity<Object> getAllEmployeesForLeaveApply() {
        List<Map<String, Object>> employeeDetail = leaveapplyrepo.getAllEmployeesForLeaveApply();
        return ResponseHandler.generateResponse(true, HttpStatus.OK, employeeDetail);
    }

    public List<Map<String, Object>> getLeaveKettyDetailsByUserIdAndLeaveId(Integer user_id, Integer leave_id) {
        Integer emp_id = uar_repo.getEmployee_id(user_id);
        return leaveapplyrepo.getLeaveKettyDetailsByUserIdAndLeaveId(emp_id, leave_id);
    }

    public ResponseEntity<Object> cancelLeavesOfEmployee(LeaveCancelDTO leaveCancel, String jwtToken) throws JsonParseException, JsonMappingException, IOException {

        JwtDetails jwtDetails = jwt_service.callJwtToken(jwtToken);
        String checkIfUserIsPrincipal = uar_repo.checkIfUserIsPrincipal(jwtDetails.getUserId());

        try {

            LeaveApply leaveApply = leaveapplyrepo.findById(leaveCancel.getLeaveApplyId()).get();

            if (checkIfUserIsPrincipal.equalsIgnoreCase("Super Admin")) {

//			if (leaveApply != null && (leaveApply.getApproved_status() == 1 || leaveApply.getApproved_status() == null)) {
                Date currentDate = new Date();

                EmployeeDetailsForLeavePattern employeeDetailsForLeavePattern = empDetail_repo
                        .getEmployeeDetailsForLeavePatternWithEmployeeId(leaveApply.getEmp_id());
                if (ObjectUtils.isNotEmpty(employeeDetailsForLeavePattern)) {
                    Integer leavePatternId = leave_pattern_repo.getLeavePatternIdEmpTypeIdAndJobTypeId(
                            employeeDetailsForLeavePattern.getEmpTypeId(),
                            employeeDetailsForLeavePattern.getJobTypeId(), leaveApply.getLeave_id(),
                            employeeDetailsForLeavePattern.getSchoolId());
                    if (ObjectUtils.isNotEmpty(leavePatternId)) {
                        System.out.println("11111111111111111111111111111111111111 " + leavePatternId);
                        System.out.println(
                                "0000000000000000000000000000000000000000000000 " + leaveApply.getLeave_id());
                        System.out.println("22222222222222222222222222222222222222 " + leaveApply.getEmp_id());
                        Double ac = el_repo.getAcculamatedDaysCount(leavePatternId, leaveApply.getEmp_id());
                        System.out.println("333333333333333333333333333333333333333333333333333333 " + ac);
                        Double updateCount = ac + leaveApply.getNo_of_days_applied();
                        System.out.println(
                                "$$$$$$$$$$44444444444444444444444444444444444444444444444444 " + updateCount);
                        el_repo.updateAcculamatedDaysCountAfterCancel(leavePatternId, leaveApply.getEmp_id(),
                                updateCount);

                    }
                }

                leaveApply.setCancel_by(leaveCancel.getCancelBy());
                leaveApply.setCancel_comments(leaveCancel.getCancelComment());
                leaveApply.setCancel_date(currentDate);
                leaveApply.setApproved_status(3);
                leaveapplyrepo.save(leaveApply);

                return ResponseHandler.generateResponse(true, HttpStatus.OK, "Leave Cancelled successfully", null);

//			} else {
//				return ResponseHandler.generateResponse(false, HttpStatus.BAD_REQUEST,
//						"Leave Already Approved or not under cancellation period", null);
//			}
            }


//			if (leaveApply != null
//					&& (leaveApply.getApproved_status() == 1 || leaveApply.getApproved_status() == null)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = new Date();
            Date leaveStartDate = simpleDateFormat.parse(leaveApply.getFrom_date());

            Calendar bufferDate = Calendar.getInstance();
            bufferDate.setTime(currentDate);
            bufferDate.add(Calendar.DATE, -45);
            Date bufferStartDate = bufferDate.getTime();

            if (leaveStartDate.after(bufferStartDate)) {
                EmployeeDetailsForLeavePattern employeeDetailsForLeavePattern = empDetail_repo
                        .getEmployeeDetailsForLeavePatternWithEmployeeId(leaveApply.getEmp_id());
                if (ObjectUtils.isNotEmpty(employeeDetailsForLeavePattern)) {
                    Integer leavePatternId = leave_pattern_repo.getLeavePatternIdEmpTypeIdAndJobTypeId(
                            employeeDetailsForLeavePattern.getEmpTypeId(),
                            employeeDetailsForLeavePattern.getJobTypeId(), leaveApply.getLeave_id(),
                            employeeDetailsForLeavePattern.getSchoolId());
                    if (ObjectUtils.isNotEmpty(leavePatternId)) {
                        System.out.println("11111111111111111111111111111111111111 " + leavePatternId);
                        System.out.println(
                                "0000000000000000000000000000000000000000000000 " + leaveApply.getLeave_id());
                        System.out.println("22222222222222222222222222222222222222 " + leaveApply.getEmp_id());
                        Double ac = el_repo.getAcculamatedDaysCount(leavePatternId, leaveApply.getEmp_id());
                        System.out.println("333333333333333333333333333333333333333333333333333333 " + ac);
                        Double updateCount = ac + leaveApply.getNo_of_days_applied();
                        System.out.println(
                                "$$$$$$$$$$44444444444444444444444444444444444444444444444444 " + updateCount);
                        el_repo.updateAcculamatedDaysCountAfterCancel(leavePatternId, leaveApply.getEmp_id(),
                                updateCount);

                    }
                }

                leaveApply.setCancel_by(leaveCancel.getCancelBy());
                leaveApply.setCancel_comments(leaveCancel.getCancelComment());
                leaveApply.setCancel_date(currentDate);
                leaveApply.setApproved_status(3);
                leaveapplyrepo.save(leaveApply);

                return ResponseHandler.generateResponse(true, HttpStatus.OK, "Leave Cancelled successfully", null);
            } else {
                return ResponseHandler.generateResponse(false, HttpStatus.BAD_REQUEST,
                        "Leave cancellation is not allowed beyond the buffer period", null);
            }
//			} else {
//				return ResponseHandler.generateResponse(false, HttpStatus.BAD_REQUEST,
//						"Leave Already Approved or not under cancellation period", null);
//			}

        } catch (Exception e) {
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    public List<String> getDistinctYear() {

        return leaveapplyrepo.getDistinctYear();
    }

    public ResponseEntity<Object> getAllDataFilteredByKeywordLeaveApplyDetails(Pageable pageable, Object keyword,
                                                                               Integer emp_id, String role, String year) {

        Page<Object> roles_filtered_response = null;

        if (role.equalsIgnoreCase("SAA") || role.equalsIgnoreCase("HRD") || role.equalsIgnoreCase("Director") || role.equalsIgnoreCase("HRR")) {
            roles_filtered_response = leaveapplyrepo.getAllDataFilteredByKeywordLeaveApplyDetailsByYear(pageable,
                    keyword, year);
        } else if (role.equalsIgnoreCase("Principal")) {
            List<Integer> employeeIdsList = leaveapplyrepo.getEmployeeIdsList(emp_id);
            roles_filtered_response = leaveapplyrepo.getAllDataFilteredByKeywordLeaveApplyDetailsByInstitueAndYear(
                    pageable, keyword, year, employeeIdsList);
        } else {
            roles_filtered_response = leaveapplyrepo.getAllDataFilteredByKeywordLeaveApplyDetailsByApproverId(pageable,
                    keyword, year, emp_id);
        }
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
    }

    public ResponseEntity<Object> getAllSortedLeaveApplyDetails(Pageable pageable, Integer emp_id, String role,
                                                                String year) {

        Page<Object> roles_sorted_response = null;

        if (role.equalsIgnoreCase("SAA") || role.equalsIgnoreCase("HRD") || role.equalsIgnoreCase("Director") || role.equalsIgnoreCase("HRR")) {
            roles_sorted_response = leaveapplyrepo.getAllSortedLeaveApplyDetailsByYear(pageable, year);
        } else if (role.equalsIgnoreCase("Principal")) {
            List<Integer> employeeIdsList = leaveapplyrepo.getEmployeeIdsList(emp_id);
            roles_sorted_response = leaveapplyrepo.getAllSortedLeaveApplyDetailsByInstitueAndYear(pageable, year,
                    employeeIdsList);
        } else {
            roles_sorted_response = leaveapplyrepo.getAllSortedLeaveApplyDetailsByApproverId(pageable, year, emp_id);
        }
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
    }

    public ResponseEntity<Object> getAllDataFilteredByKeywordForApprovers1(Pageable pageable, Object keyword,
                                                                           java.time.LocalDate localDate, Integer approver_id) {
        Page<Object> roles_filtered_response = leaveapplyrepo.getAllDataFilteredByKeywordForApprovers1(pageable,
                keyword, localDate, approver_id);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
    }

    public ResponseEntity<Object> getAllSortedDataForApprovers2(Pageable pageable, java.time.LocalDate minusDate,
                                                                Integer approver_id) {
        Page<Object> roles_sorted_response = leaveapplyrepo.getAllSortedDataForApprovers2(pageable, minusDate,
                approver_id);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
    }

    public ResponseEntity<Object> getAllDataFilteredByKeywordForApprovers3(Pageable pageable, Object keyword,
                                                                           java.time.LocalDate localDate, Integer approver_id) {
        Page<Object> roles_filtered_response = leaveapplyrepo.getAllDataFilteredByKeywordForApprovers3(pageable,
                keyword, localDate, approver_id);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
    }

    public ResponseEntity<Object> getAllSortedDataForApprovers4(Pageable pageable, java.time.LocalDate localDate,
                                                                Integer approver_id) {
        Page<Object> roles_sorted_response = leaveapplyrepo.getAllSortedDataForApprovers4(pageable, localDate,
                approver_id);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
    }

    public Object emailToEmployeeForApprovalOfLeaveRequest(Integer leave_apply_id) {

//		LeaveApply leaveApply = leaveapplyrepo.getOne(leave_apply_id);

        HashMap<String, Object> data = leaveapplyrepo.getApproverDataSendingEmail(leave_apply_id);

        String no_of_days = data.get("no_of_days_applied").toString();

        if (no_of_days.contains(".0")) {
            String[] nd = data.get("no_of_days_applied").toString().split("\\.");
            no_of_days = nd[0];
        }

        String content = "Dear Mr/Ms." + data.get("employee_name") + "<br/> " + "<br/> "
                + "In response to your request for leave from " + data.get("from_date") + " to " + data.get("to_date")
                + " for " + no_of_days + " days." + "<br/> " + "<br/> "
                + "We are pleased to inform you that your leave request is approved.However, do delegate your daily work "
                + "responsibilities to your assistants or subordinates  to ensure a smooth flow of operation in your absence."
                + "<br/> " + "<br/> " + "Check your dashboard for further details." + "<br/> " + "<br/> " + "Regards"
                + "<br/> " + "Team Acharya" + "<br/> " + "<br/> "
                + "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
                + "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";

        System.out.println(data.get("employee_email").toString());
        try {
            response_handler.sendSimpleEmailWithHtmlContent(data.get("employee_email").toString(), content,
                    "Leave Request Approved");
        } catch (Exception e) {
            throw new RuntimeException("Leave approved but email not sent as limit exceeded!");
        }

        return null;

    }

    public Object emailToEmployeeForLeaveCancellation(Integer leave_apply_id) {

//		LeaveApply leaveApply = leaveapplyrepo.getOne(leave_apply_id);

        HashMap<String, Object> data = leaveapplyrepo.getApproverDataSendingEmail(leave_apply_id);

        String no_of_days = data.get("no_of_days_applied").toString();

        if (no_of_days.contains(".0")) {
            String[] nd = data.get("no_of_days_applied").toString().split("\\.");
            no_of_days = nd[0];
        }

        String content = "Dear Sir/Madam," + "<br/> " + "<br/> " + "This is to notify that below leave is cancelled. "
                + "<br/> " + "<br/> " + data.get("employee_name") + "-" + data.get("empcode") + " applied "
                + data.get("leave_type_short") + " from " + data.get("from_date") + " to " + data.get("to_date")
                + " for " + no_of_days + " days." + "<br/> " + "<br/> " + "Check your dashboard for further details."
                + "<br/> " + "<br/> " + "Regards" + "<br/> " + "Team Acharya"
                + "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
                + "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";
        System.out.println(data.get("employee_email").toString());
        response_handler.sendSimpleEmailWithHtmlContent(data.get("employee_email").toString(), content,
                "Leave Cancelled");

        return null;

    }

    public Object emailToApproverForLeaveRequest(Integer emp_id) {

        String approverEmail = empDetail_repo.getApproveEmail(emp_id);

        String content = "Dear Sir/Madam " + "<br/> " + "<br/> "
                + "You have a request for Leave/attendance approval, Please approve @ the earliest." + "<br/> "
                + "<br/> " + "Regards" + "<br/> " + "Team Acharya" + "<br/> " + "<br/> "
                + "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
                + "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";

        System.out.println("Sending email to: " + approverEmail);

        // ✅ Pass 'true' to send from 2nd mail (acerp@acharya.ac.in)
        response_handler.sendSimpleEmailWithHtmlContent(approverEmail, content, "Leave Application", true);

        return null;
    }

    public Object emailToAlternateStaffSelectedByLeaveApplier(Integer emp_id, Integer leave_apply_id) {
//		LeaveApply leaveApply = leaveapplyrepo.getOne(leave_apply_id);

        HashMap<String, Object> data = leaveapplyrepo.getApproverDataSendingEmail(leave_apply_id);
        String no_of_days = data.get("no_of_days_applied").toString();

        if (no_of_days.contains(".0")) {
            String[] nd = data.get("no_of_days_applied").toString().split("\\.");
            no_of_days = nd[0];
        }

        String content = "Dear Colleague," + "<br/> " + "<br/> "
                + "You have been identified as alternative staff by your colleague mentioned below." + "<br/> "
                + "<br/> "
                + "If you have any reservations on this alternate arrangement, kindly get in touch with Head of your Department."
                + "<br/> " + "<br/> " + "Reffered by " + data.get("employee_name") + "-" + data.get("empcode")
                + " applied " + data.get("leave_type_short") + " from " + data.get("from_date") + " to "
                + data.get("to_date") + " for " + no_of_days + " days." + "<br/> " + "\r\n"
                + "Check your dashboard for further details." + "<br/> " + "<br/> " + "Regards" + "<br/> "
                + "Team Acharya" + "<br/> " + "<br/> "
                + "<div style='background-color:#FAF9F6;padding:15px;width:40%;margin:5px'> <div style='color:#89CFF0;font-size: 15px;'>Privacy Statement</div> <br/> "
                + "<div style='color:#899499;font-size: 17px;'>This is an automated email that cannot accept replies.</div> </div> ";

        System.out.println(empDetail_repo.getOne(emp_id).getEmail());
        response_handler.sendSimpleEmailWithHtmlContent(empDetail_repo.getOne(emp_id).getEmail(), content,
                "Important - Identified as Alternative Staff", true);

        return null;

    }

    public ResponseEntity<Object> getAllDataFilteredByKeywordForEndUser(Pageable pageable, Object keyword,
                                                                        Integer emp_id, String year) {
        Page<Object> roles_filtered_response = leaveapplyrepo.getAllDataFilteredByKeywordForEndUser(pageable, keyword,
                emp_id, year);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
    }

    public ResponseEntity<Object> getAllSortedDataForEndUser(Pageable pageable, Integer emp_id, String year) {
        Page<Object> roles_sorted_response = leaveapplyrepo.getAllSortedDataForEndUser(pageable, emp_id, year);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
    }

    public List<Map<String, Object>> getDataByLeaveApproversId(Integer user_id) {

        if (ObjectUtils.isEmpty(user_id) || user_id == null) {
            return empDetail_repo.getAllEmployeeData();

        } else {
            Integer empId = empDetail_repo.getEmployeeId(user_id);
            return empDetail_repo.getDataByLeaveApproversId(empId);
        }
    }


//		try {
//			RestTemplate restTemplate = new RestTemplate();
//			String urlString = "acharyainstitutes.in/index.php?r=acerp-api/ach-leaves&datee="
//					+ leaveApplyEventDTO.getDate();
//			HttpHeaders headers = new HttpHeaders();
//			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//			HttpEntity<String> entity = new HttpEntity<>(headers);
//			ResponseEntity<ApiResponseLeaveApply> responseEntity = restTemplate.exchange(
//					API_URL + leaveApplyEventDTO.getDate(), HttpMethod.GET, entity, ApiResponseLeaveApply.class);
//			ApiResponseLeaveApply apiResponse = responseEntity.getBody();
//
//			if (apiResponse != null && apiResponse.isSuccess()) {
//
//				for (ApiResponseLeaveApplyDTO apiResponseLeaveApply : apiResponse.getData()) {


//				try {
//					System.out.println(apiResponseLeaveApply.getContract_empcode());
//					EmployeeDetails employeeDetails = employeeDetailsReportRepository
//							.findByContractCode(apiResponseLeaveApply.getContract_empcode());


    public LeaveApply handleLeaveApplyEvent(LeaveApplyDto la) {
        LeaveApply leaveApply = new LeaveApply();
        la.getEmp_id().stream().forEach(emp -> {

            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = null;
            try {
                fromDate = originalFormat.parse(la.getFrom_date());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Date toDate = null;
            try {
                toDate = originalFormat.parse(la.getTo_date());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
            String newFromDate = newFormat.format(fromDate);
            String newToDate = newFormat.format(toDate);
//					if (ObjectUtils.isNotEmpty(employeeDetails)) {
            System.out.println("/////////////////////////////// " + emp);
            System.out.println("/////////////////////////////// " + newFromDate);
            System.out.println("/////////////////////////////// " + newToDate);
            LeaveApply isExists = leaveapplyrepo.existsBetweenDate(
                    newFromDate, newToDate,
                    emp);
            System.out.println("?????????????????????????????" + isExists.getLeave_apply_id() + " from date : " + isExists.getFrom_date());

            if (ObjectUtils.isEmpty(isExists)) {
                System.out.println("9999999999999999999999999999");

                leaveApply.setActive(Boolean.TRUE);
                leaveApply.setEmp_id(emp);
                leaveApply.setApproved_status(2);
                leaveApply.setFrom_date(newFromDate);
                leaveApply.setTo_date(newToDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(toDate);
                Integer year = calendar.get(Calendar.YEAR);
                leaveApply.setYear(String.valueOf(year));
                leaveApply.setNo_of_days_applied(
                        Float.valueOf(la.getNo_of_days_applied()));

                leaveApply.setLeave_id(la.getLeave_id());
                leaveapplyrepo.save(leaveApply);
            }
        });
        return leaveApply;
    }

    private Integer getLeaveId(String leave_short_name) {

        return s_repo.getLeaveIdByLeaveTypeShort(leave_short_name);
    }


    public void updateLeaveApplyApprover(LeaveApplyUpdateDto leaveApply, String jwtToken) {

        leaveApply.getLeaveApprover1().entrySet().forEach(emp -> {
            leaveapplyrepo.updateLeaveApplyApprover1(emp.getKey(), emp.getValue());
        });
        leaveApply.getLeaveApprover2().entrySet().forEach(emp -> {
            leaveapplyrepo.updateLeaveApplyApprover2(emp.getKey(), emp.getValue());
        });
    }

    public ResponseEntity<Object> getAllApprovedOrCancelledLeavesDataFilteredByKeyword(Pageable pageable,
                                                                                       Object keyword, List<Integer> emp_ids) {
        Page<Object> roles_filtered_response = leaveapplyrepo
                .getAllApprovedOrCancelledLeavesDataFilteredByKeyword(pageable, keyword, emp_ids);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_filtered_response);
    }

    public ResponseEntity<Object> getAllApprovedOrCancelledLeavesgetAllSortedData(Pageable pageable,
                                                                                  List<Integer> emp_ids) {
        Page<Object> roles_sorted_response = leaveapplyrepo.getAllApprovedOrCancelledLeavesgetAllSortedData(pageable,
                emp_ids);
        return ResponseHandler.generateResponseForIndex(true, HttpStatus.OK, roles_sorted_response);
    }

    public ResponseEntity<Object> getLeaveDetails(Integer year, Integer schoolId, Integer deptId, Integer leaveId) {
        try {
            List<Map<String, Object>> leaveApplyList = leaveapplyrepo.getAllLeaves(year, schoolId, deptId, leaveId);
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", leaveApplyList);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }

    public ResponseEntity<Object> getLeaveDetailsByLeaveId(String leaveShortName, Integer empId, Integer year) {

        try {
            Integer leaveId = leaveTypeRepository.findByLeaveShortName(leaveShortName);
            List<Map<String, String>> leaveApplyList = leaveapplyrepo.findAllByLeaveIdAndYearAndEmpId(leaveId, empId, year);
            return ResponseHandler.generateResponse(true, HttpStatus.OK, "SUCCESS", leaveApplyList);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "FAILURE", null);
        }
    }
}
