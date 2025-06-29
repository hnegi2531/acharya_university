package com.au.event;

import com.au.controller.TriggerRepository;
import com.au.model.*;
import com.au.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class AttendanceSheetEventPublisher {

    public static final String ATTENDENCE = "Attendence";
    @Autowired
    private Environment environment;

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Autowired
    private ResignationRepository resignationRepository;

    @Autowired
    private EmployeeSheetRepository employeeSheetRepository;

    @Autowired
    private LeaveApplyRepository leaveApplyRepository;

    @Autowired
    private BiometricAttendanceRepository biometricAttendanceRepository;

    @Autowired
    private HolidayCalenderRepository holidayCalenderRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private LockDateRepository lockDateRepository;

    @Async
    @EventListener
    public void handleAttendanceSheetEventPublisher(AttendanceSheetEvent attendanceSheetEvent) {
        LockDates lockDates = lockDateRepository.getFirstByMonthandYear(attendanceSheetEvent.getMonth(), attendanceSheetEvent.getYear());

        if (ObjectUtils.isEmpty(lockDates)) {
            lockDates = new LockDates();
            Date leaveLockDate = getFifthDateOfTheMonth(attendanceSheetEvent.getMonth(), attendanceSheetEvent.getYear());
            lockDates.setLeave_lock_date(leaveLockDate);
        }

        Date lockDate = lockDates.getLeave_lock_date();
        Date date = new Date();

        // Convert Date to LocalDate (ignores time part)
        LocalDate lockLocalDate = lockDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate currentLocalDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        // Compare dates (ignores time)
        if (lockLocalDate.isEqual(currentLocalDate) || lockLocalDate.isAfter(currentLocalDate)) {

            List<Integer> employeeIdList = null;
            if (ObjectUtils.isNotEmpty(attendanceSheetEvent.getEmpId())) {
                employeeIdList = employeeDetailsRepository.getAllEmployeeIdByEmpId(attendanceSheetEvent.getEmpId());
            } else if (ObjectUtils.isNotEmpty(attendanceSheetEvent.getSchoolId())) {
                employeeIdList = employeeDetailsRepository.getAllEmployeeIdBySchoolId(attendanceSheetEvent.getSchoolId());

            } else {
                employeeIdList = employeeDetailsRepository.getAllEmployeeId();
            }
            if (employeeIdList == null || employeeIdList.isEmpty()) return;
            Calendar currentCalendar = Calendar.getInstance();
            int year;
            int month;
            int givenMonth = attendanceSheetEvent.getMonth();
            int givenYear = attendanceSheetEvent.getYear();
            if (ObjectUtils.isNotEmpty(givenMonth) && ObjectUtils.isNotEmpty(givenYear)) {
                month = givenMonth - 1;
                year = givenYear;
            } else {
                month = currentCalendar.get(Calendar.MONTH);
                year = currentCalendar.get(Calendar.YEAR);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Calendar noSwipecalendar = Calendar.getInstance();
            noSwipecalendar.set(Calendar.YEAR, year);
            noSwipecalendar.set(Calendar.MONTH, month);
            noSwipecalendar.set(Calendar.DAY_OF_MONTH, 1);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            int currentDay;
            if (year < currentCalendar.get(Calendar.YEAR)
                    || (year == currentCalendar.get(Calendar.YEAR) && month < currentCalendar.get(Calendar.MONTH))) {
                calendar.set(Calendar.MONTH, month);
                currentDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            } else {
                currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
            }

            int intialCount1 = 1;
            Set<Integer> employee = new HashSet<>();
            int totalDayInAMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            while (calendar.get(Calendar.MONTH) == month && intialCount1 <= currentDay) {
                String formattedDate = formatter.format(calendar.getTime());
                System.out.println(formattedDate);

                setWorkingAndNotWorkingDays(employeeIdList, intialCount1, calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()), formattedDate, month, year, employee, totalDayInAMonth);

                intialCount1++;
                System.out.println("Days------> " + intialCount1);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            List<Integer> noSwipeEmployeeIdList = null;

            if (ObjectUtils.isNotEmpty(attendanceSheetEvent.getEmpId())) {
                noSwipeEmployeeIdList = employeeDetailsRepository
                        .getAllNoSwipeEmployeeIdByEmpId(attendanceSheetEvent.getEmpId());
            } else if (ObjectUtils.isNotEmpty(attendanceSheetEvent.getSchoolId())) {
                noSwipeEmployeeIdList = employeeDetailsRepository
                        .getAllNoSwipeEmployeeIdBySchoolId(attendanceSheetEvent.getSchoolId());

            } else {
                noSwipeEmployeeIdList = employeeDetailsRepository.getAllNoSwipeEmployeeId();

            }
            int intialCount2 = 1;
            Set<Integer> noSwipeEmployee = new HashSet<>();

            int totalDayInAMonthForNoSwipe = noSwipecalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            while (noSwipecalendar.get(Calendar.MONTH) == month && intialCount2 <= currentDay) {
                String formattedDate = formatter.format(noSwipecalendar.getTime());
                System.out.println(formattedDate);

                setNoSwipeWorkingDays(noSwipeEmployeeIdList, intialCount2,
                        noSwipecalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()),
                        formattedDate, month, year, noSwipeEmployee, totalDayInAMonthForNoSwipe);

                intialCount2++;
                System.out.println("Days------> " + intialCount2);
                noSwipecalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        System.out.println("Employee Attendance Trigger Completed");
    }

    public Date getFifthDateOfTheMonth(Integer month, Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month); // Adjust month to be zero-based
        calendar.set(Calendar.DAY_OF_MONTH, 5); // Set day to the fifth day of the month
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    private void setNoSwipeWorkingDays(List<Integer> noSwipeEmployeeIdList, int intialCount2, String displayName,
                                       String formattedDate, int month, int year, Set<Integer> noSwipeEmployee, int totalDayInAMonthForNoSwipe) {
        try {
            if (ObjectUtils.isNotEmpty(noSwipeEmployeeIdList)) {

                AtomicReference<com.au.model.EmployeeSheet> employees = new AtomicReference<>(null);
                noSwipeEmployeeIdList.stream().forEach(empId -> {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        Date fromDate = new SimpleDateFormat("dd-MM-yyyy").parse(formattedDate);
                        calendar.setTime(fromDate);
                        int currentMonth = month + 1;
                        int currentyear = calendar.get(Calendar.YEAR);
                        int toDay = calendar.get(Calendar.DAY_OF_MONTH);
                        int totalDaysInAMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        String dateOfJoining = employeeDetailsRepository.getDateofJoining(empId);
                        String dateOfRelieving = resignationRepository.getRelievingDate(empId);

                        Calendar doj = Calendar.getInstance();
                        Calendar dor = Calendar.getInstance();
                        Date dateOfJoin = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfJoining);
                        doj.setTime(dateOfJoin);
                        int dorMonth = 0;
                        int dorYear = 0;
                        if (StringUtils.isNotEmpty(dateOfRelieving)) {
                            Date dateOfRelieve = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfRelieving);

                            dor.setTime(dateOfRelieve);
                            dorMonth = dor.get(Calendar.MONTH) + 1;
                            dorYear = dor.get(Calendar.YEAR);
                        } else {
                            dorMonth = month + 1;
                        }
                        int dojMonth = 0;

                        if (dateOfJoin.before(fromDate)) {
                            dojMonth = month + 1;
                        } else {
                            dojMonth = doj.get(Calendar.MONTH) + 1;
                        }

                        if ((dojMonth <= currentMonth && StringUtils.isEmpty(dateOfRelieving))
                                || (dojMonth <= currentMonth && dorMonth >= currentMonth && dorYear == year)) {

                            EmployeeSheet employeeSheet = employeeSheetRepository.findByEmpIdAndMonthAndYear(empId,
                                    currentMonth, year);

                            if (ObjectUtils.isNotEmpty(employeeSheet) && ObjectUtils.isEmpty(employees.get())
                                    && noSwipeEmployee.add(empId)) {
                                employeeSheet.setPresentdays(0d);
                                employeeSheet.setLeavetaken(0d);
                                employeeSheet.setGeneralWO(0d);
                                employeeSheet.setAbsentdays(0d);
                                employeeSheet.setDeclaredHoliday(0d);
                                employeeSheet.setWorkingDays(0d);
                                employeeSheet.setPaydays(0d);
                                employeeSheet.setTotalDays(totalDayInAMonthForNoSwipe);
                            }

                            if (ObjectUtils.isEmpty(employeeSheet)) {
                                employees.set(new EmployeeSheet());
                                employees.get().setEmpId(empId);
                                employees.get().setYear(currentyear);
                                employees.get().setMonth(currentMonth);
                                employees.get().setPresentdays(0d);
                                employees.get().setLeavetaken(0d);
                                employees.get().setGeneralWO(0d);
                                employees.get().setAbsentdays(0d);
                                employees.get().setDeclaredHoliday(0d);
                                employees.get().setTotalDays(totalDayInAMonthForNoSwipe);
                                employees.get().setWorkingDays(0d);
                                employees.get().setPaydays(0d);
                                noSwipeEmployee.add(empId);
                                setNoSwipeEmployeeWorkingDays(employees.get(), empId, toDay,
                                        employees.get().getPresentdays(), employees.get().getLeavetaken(),
                                        employees.get().getGeneralWO(), employees.get().getAbsentdays(), formattedDate,
                                        displayName);
                            } else {
                                setNoSwipeEmployeeWorkingDays(employeeSheet, empId, toDay,
                                        employeeSheet.getPresentdays(), employeeSheet.getLeavetaken(),
                                        employeeSheet.getGeneralWO(), employeeSheet.getAbsentdays(), formattedDate,
                                        displayName);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Exception occur in scheduler " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Exception occur in scheduler " + e.getMessage());
        }

    }

    private void setNoSwipeEmployeeWorkingDays(EmployeeSheet employeeSheet, Integer empId, int toDay, double presentdays,
                                               double leavetaken, double generalWO, double absentdays, String formattedDate, String displayName) {
        try {
            EmployeeDetails employeeDetails = employeeDetailsRepository.findById(employeeSheet.getEmpId()).get();
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = originalFormat.parse(formattedDate);
            String bioDate = targetFormat.format(date);
            System.out.println(bioDate);

            Boolean isJoiningDate = isJoiningDate(empId, formattedDate);
            Boolean isRelievingDate = checkDateOfRelieving(empId, formattedDate);

            setNoSwipePayDays(employeeSheet, empId, toDay, presentdays, leavetaken, generalWO, absentdays,
                    formattedDate, isJoiningDate, isRelievingDate, displayName);
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
            String dateString = dateFormat.format(currentDate);
            employeeSheet.setDept_id(employeeDetails.getDept_id());
            employeeSheet.setSchool_id(employeeDetails.getSchool_id());
            employeeSheet.setEmpCode(employeeDetails.getEmpcode());
            employeeSheet.setDesignation_id(employeeDetails.getDesignation_id());

            if (!isJoiningDate && ObjectUtils.isNotEmpty(isRelievingDate) && !isRelievingDate) {
                double paydays = incrementPayDays(employeeSheet);
                employeeSheet.setPaydays(paydays);
            }

            employeeSheetRepository.save(employeeSheet);
        } catch (Exception e) {
            System.out.print("Exception occur " + e.getMessage());
        }

    }

    private void setNoSwipePayDays(EmployeeSheet employeeSheet, Integer empId, int currentDay, double presentdays,
                                   double leavetaken, double generalWO, double absentdays, String formattedDate, Boolean isJoiningDate,
                                   Boolean isRelievingDate, String displayName) throws ParseException {

        String today = displayName;
        leavetaken = employeeSheet.getLeavetaken();
        generalWO = employeeSheet.getGeneralWO();
        absentdays = employeeSheet.getAbsentdays();
        presentdays = employeeSheet.getPresentdays();
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = originalFormat.parse(formattedDate);
        String hDate = targetFormat.format(date);
        System.out.println(hDate);

        String holiday = holidayCalenderRepository.getHolidayFromFromDateForGH(hDate);

        switch (currentDay) {
            case 1:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay1("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay1(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay1("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 2:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay2("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay2(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay2("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 3:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay3("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay3(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay3("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 4:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay4("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay4(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay4("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 5:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay5("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay5(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay5("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 6:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay6("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay6(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay6("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 7:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay7("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay7(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay7("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 8:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay8("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay8(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay8("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 9:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay9("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay9(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay9("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 10:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay10("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay10(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay10("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 11:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay11("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay11(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay11("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 12:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay12("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay12(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay12("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 13:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay13("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay13(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay13("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 14:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay14("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay14(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay14("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 15:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay15("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay15(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay15("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 16:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay16("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay16(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay16("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 17:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay17("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay17(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay17("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 18:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay18("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay18(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay18("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 19:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay19("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay19(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay19("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 20:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay20("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay20(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay20("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 21:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay21("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay21(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay21("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 22:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay22("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay22(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay22("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 23:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay23("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay23(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay23("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 24:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay24("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay24(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay24("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 25:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay25("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay25(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay25("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 26:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay26("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay26(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay26("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 27:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay27("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay27(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay27("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 28:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay28("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay28(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay28("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 29:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay29("P");
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay29("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 30:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay30("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay30(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay30("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            case 31:
                if (!today.equals("Sunday") && StringUtils.isEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay31("P");
                    employeeSheet.setPresentdays(presentdays);
                } else if (!today.equals("Sunday") && StringUtils.isNotEmpty(holiday)) {
                    presentdays++;
                    employeeSheet.setDay31(holiday);
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    generalWO++;
                    employeeSheet.setDay31("WO");
                    employeeSheet.setGeneralWO(generalWO);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + currentDay);
        }

    }

    private void setWorkingAndNotWorkingDays(List<Integer> employeeIdList, int intialCount1, String displayName,
                                             String formattedDate, int month, int year, Set<Integer> employee, int totalDayInAMonth) {
        try {
            if (ObjectUtils.isNotEmpty(employeeIdList)) {

                AtomicReference<com.au.model.EmployeeSheet> employees = new AtomicReference<>(null);
                employeeIdList.stream().forEach(empId -> {
                    try {

                        Calendar calendar = Calendar.getInstance();
                        Date fromDate = new SimpleDateFormat("dd-MM-yyyy").parse(formattedDate);
                        calendar.setTime(fromDate);
                        int currentMonth = month + 1;
                        int currentyear = calendar.get(Calendar.YEAR);
                        int toDay = calendar.get(Calendar.DAY_OF_MONTH);
                        int totalDaysInAMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        String dateOfJoining = employeeDetailsRepository.getDateofJoining(empId);
                        String dateOfRelieving = resignationRepository.getRelievingDate(empId);

                        Calendar doj = Calendar.getInstance();
                        Calendar dor = Calendar.getInstance();
                        Date dateOfJoin = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfJoining);
                        doj.setTime(dateOfJoin);
                        int dorMonth = 0;
                        int dorYear = 0;
                        if (StringUtils.isNotEmpty(dateOfRelieving)) {
                            Date dateOfRelieve = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfRelieving);

                            dor.setTime(dateOfRelieve);
                            dorMonth = dor.get(Calendar.MONTH) + 1;
                            dorYear = dor.get(Calendar.YEAR);
                        } else {
                            dorMonth = month + 1;
                        }
                        int dojMonth = 0;

                        if (dateOfJoin.before(fromDate)) {
                            dojMonth = month + 1;
                        } else {
                            dojMonth = doj.get(Calendar.MONTH) + 1;
                        }

                        if ((dojMonth <= currentMonth)
                                || (dojMonth <= currentMonth && dorMonth >= currentMonth && dorYear == year)) {

                            EmployeeSheet employeeSheet = employeeSheetRepository.findByEmpIdAndMonthAndYear(empId,
                                    currentMonth, year);

                            if (ObjectUtils.isNotEmpty(employeeSheet) && ObjectUtils.isEmpty(employees.get())
                                    && employee.add(empId)) {
                                employeeSheet.setPresentdays(0d);
                                employeeSheet.setLeavetaken(0d);
                                employeeSheet.setGeneralWO(0d);
                                employeeSheet.setAbsentdays(0d);
                                employeeSheet.setDeclaredHoliday(0d);
                                employeeSheet.setWorkingDays(0d);
                                employeeSheet.setPaydays(0d);
                                employeeSheet.setTotalDays(totalDaysInAMonth);
                                System.out.println("EmpId: " + empId + " PayDays: " + employeeSheet.getPaydays() + " Present days: " + employeeSheet.getPresentdays() + " Absent days: " + employeeSheet.getAbsentdays() +
                                        " General WO: " + employeeSheet.getGeneralWO() + " Leave taken: " + employeeSheet.getLeavetaken());
                            }

                            if (ObjectUtils.isEmpty(employeeSheet)) {
                                employees.set(new EmployeeSheet());
                                employees.get().setEmpId(empId);
                                employees.get().setYear(currentyear);
                                employees.get().setMonth(currentMonth);
                                employees.get().setPresentdays(0d);
                                employees.get().setLeavetaken(0d);
                                employees.get().setGeneralWO(0d);
                                employees.get().setAbsentdays(0d);
                                employees.get().setDeclaredHoliday(0d);
                                employees.get().setTotalDays(totalDayInAMonth);
                                employees.get().setWorkingDays(0d);
                                employees.get().setPaydays(0d);
                                employee.add(empId);
                                setEmployeeWorkingDays(employees.get(), empId, toDay, employees.get().getPresentdays(),
                                        employees.get().getLeavetaken(), employees.get().getGeneralWO(),
                                        employees.get().getAbsentdays(), formattedDate, displayName);
                            } else {
                                System.out.println("EmpId: " + empId + " PayDays: " + employeeSheet.getPaydays() + " Present days: " + employeeSheet.getPresentdays() + " Absent days: " + employeeSheet.getAbsentdays() +
                                        " General WO: " + employeeSheet.getGeneralWO() + " Leave taken: " + employeeSheet.getLeavetaken());
                                setEmployeeWorkingDays(employeeSheet, empId, toDay, employeeSheet.getPresentdays(),
                                        employeeSheet.getLeavetaken(), employeeSheet.getGeneralWO(),
                                        employeeSheet.getAbsentdays(), formattedDate, displayName);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Exception occur in scheduler " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Exception occur in scheduler " + e.getMessage());
        }

    }

    private void setEmployeeWorkingDays(EmployeeSheet employeeSheet, Integer empId, int toDay, double presentdays,
                                        double leavetaken, double generalWO, double absentdays, String formattedDate, String displayName) {
        try {
            EmployeeDetails employeeDetails = employeeDetailsRepository.findById(employeeSheet.getEmpId()).get();
            List<LeaveApply> leaveApplyList = leaveApplyRepository.getAllLeavesByFromDateAndEmpId(formattedDate, empId);
            LeaveApply leaveApply = null;
            if (leaveApplyList.size() == 1)
                leaveApply = leaveApplyRepository.getLeaveByFromDateAndEmpId(formattedDate, empId);
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = originalFormat.parse(formattedDate);
            String bioDate = targetFormat.format(date);
            System.out.println(bioDate);

            BiometricAttendance biometricAttendance = biometricAttendanceRepository.getByDateAndEmpId(bioDate, empId);
            Boolean isJoiningDate = isJoiningDate(empId, formattedDate);
            Boolean isRelievingDate = checkDateOfRelieving(empId, formattedDate);
            employeeSheet.setDept_id(employeeDetails.getDept_id());
            employeeSheet.setSchool_id(employeeDetails.getSchool_id());
            employeeSheet.setEmpCode(employeeDetails.getEmpcode());
            employeeSheet.setDesignation_id(employeeDetails.getDesignation_id());

            setPayDays(leaveApply, biometricAttendance, employeeSheet, empId, toDay, presentdays, leavetaken, generalWO,
                    absentdays, formattedDate, isJoiningDate, isRelievingDate, displayName, bioDate, leaveApplyList);
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
            String dateString = dateFormat.format(currentDate);

            if (!isJoiningDate && ObjectUtils.isNotEmpty(isRelievingDate) && !isRelievingDate) {
                double paydays = incrementPayDays(employeeSheet);
                employeeSheet.setPaydays(paydays);
                System.out.println("EmpId: " + empId + " Pay Days : " + paydays);
            }
            employeeSheetRepository.save(employeeSheet);
        } catch (Exception e) {
            System.out.print("Exception occur " + e.getMessage());
        }

    }

    private void setPayDays(LeaveApply leaveApply, BiometricAttendance biometricAttendance, EmployeeSheet employeeSheet,
                            Integer empId, int currentDay, double presentdays, double leavetaken, double generalWO, double absentdays,
                            String formattedDate, Boolean isJoiningDate, Boolean isRelievingDate, String displayName,
                            String hDate, List<LeaveApply> leaveApplyList) throws ParseException {
        String today = displayName;
        leavetaken = employeeSheet.getLeavetaken();
        generalWO = employeeSheet.getGeneralWO();
        absentdays = employeeSheet.getAbsentdays();
        presentdays = employeeSheet.getPresentdays();

        String holiday = holidayCalenderRepository.getHolidayFromFromDateForGH(hDate);
        if (ObjectUtils.isEmpty(holiday)) {
            Integer deptId = employeeDetailsRepository.getDeptIdByEmpId(employeeSheet.getEmpId());
            Integer jobTypeId = employeeDetailsRepository.getJobTypeIdByEmpId(employeeSheet.getEmpId());
            holiday = holidayCalenderRepository.getHolidayFromFromDate(hDate, deptId, jobTypeId,
                    employeeSheet.getSchool_id());

        }

        LeaveType leaveType = null;
        Date fromDate = null;
        Date toDate = null;
        if (ObjectUtils.isNotEmpty(leaveApply)) {
            leaveType = leaveTypeRepository.fetchLeaveType(leaveApply.getLeave_id());
            fromDate = new SimpleDateFormat("dd-MM-yyyy").parse(leaveApply.getFrom_date());
            toDate = new SimpleDateFormat("dd-MM-yyyy").parse(leaveApply.getTo_date());
        }

        switch (currentDay) {
            case 1:
                employeeSheet.setDay1(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;

            case 2:
                employeeSheet.setDay2(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 3:
                employeeSheet.setDay3(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 4:
                employeeSheet.setDay4(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 5:
                employeeSheet.setDay5(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;

            case 6:
                employeeSheet.setDay6(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;

            case 7:
                employeeSheet.setDay7(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 8:
                employeeSheet.setDay8(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 9:
                employeeSheet.setDay9(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 10:
                employeeSheet.setDay10(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 11:
                employeeSheet.setDay11(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;

            case 12:
                employeeSheet.setDay12(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 13:
                employeeSheet.setDay13(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 14:
                employeeSheet.setDay14(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 15:
                employeeSheet.setDay15(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 16:
                employeeSheet.setDay16(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;

            case 17:
                employeeSheet.setDay17(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 18:
                employeeSheet.setDay18(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 19:
                employeeSheet.setDay19(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 20:
                employeeSheet.setDay20(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 21:
                employeeSheet.setDay21(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;

            case 22:
                employeeSheet.setDay22(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 23:
                employeeSheet.setDay23(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 24:
                employeeSheet.setDay24(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 25:
                employeeSheet.setDay25(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 26:
                employeeSheet.setDay26(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;

            case 27:
                employeeSheet.setDay27(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 28:
                employeeSheet.setDay28(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 29:
                employeeSheet.setDay29(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 30:
                employeeSheet.setDay30(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
            case 31:
                employeeSheet.setDay31(setStatusOfTheDay(today, biometricAttendance, leaveApply, isJoiningDate, isRelievingDate, absentdays,
                        generalWO, presentdays, leavetaken, employeeSheet, holiday, fromDate, toDate, currentDay, leaveType,
                        formattedDate, leaveApplyList));
                break;
        }

    }

    private String checkHalfDayLeaveApply(BiometricAttendance biometricAttendance, LeaveApply leaveApply,
                                          EmployeeSheet employeeSheet, LeaveType leaveType, double presentdays, double absentdays,
                                          double leavetaken) {
        List<String> leaveTypes = leaveTypeRepository.getLeaveTypeShortName();
        if (leaveApply.getNo_of_days_applied() == null) {
            if (StringUtils.equals(biometricAttendance.getPresentStatus(), "a/p")
                    && leaveTypes.contains(leaveType.getLeave_type_short())
                    && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")) {
                if (leaveType.getType().equals(ATTENDENCE)) {
                    presentdays++;
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    leavetaken = leavetaken + 0.5;
                    presentdays = presentdays + 0.5;
                    employeeSheet.setLeavetaken(leavetaken);
                    employeeSheet.setPresentdays(presentdays);
                }
                return leaveType.getLeave_type_short().toLowerCase() + "/" + "p";
            }
            if (StringUtils.equals(biometricAttendance.getPresentStatus(), "p/a")
                    && leaveTypes.contains(leaveType.getLeave_type_short())
                    && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")) {
                if (leaveType.getType().equals(ATTENDENCE)) {
                    presentdays++;
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    leavetaken = leavetaken + 0.5;
                    presentdays = presentdays + 0.5;
                    employeeSheet.setLeavetaken(leavetaken);
                    employeeSheet.setPresentdays(presentdays);
                }
                return "p" + "/" + leaveType.getLeave_type_short().toLowerCase();
            }
            if (StringUtils.equals(biometricAttendance.getPresentStatus(), "A")
                    && leaveTypes.contains(leaveType.getLeave_type_short())
                    && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")) {
                if (leaveType.getType().equals(ATTENDENCE)) {
                    presentdays++;
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    leavetaken++;
                    employeeSheet.setLeavetaken(leavetaken);
                }
                return leaveType.getLeave_type_short();
            }
            if (StringUtils.equals(biometricAttendance.getPresentStatus(), "P")
                    && leaveTypes.contains(leaveType.getLeave_type_short())
                    && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")) {
                if (leaveType.getType().equals(ATTENDENCE)) {
                    presentdays++;
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    leavetaken++;
                    employeeSheet.setLeavetaken(leavetaken);
                }
                return leaveType.getLeave_type_short();
            }
        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "a/p")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() == 0.5) {
            if (leaveType.getType().equals(ATTENDENCE)) {
                presentdays++;
                employeeSheet.setPresentdays(presentdays);
            } else {
                leavetaken = leavetaken + 0.5;
                presentdays = presentdays + 0.5;
                employeeSheet.setLeavetaken(leavetaken);
                employeeSheet.setPresentdays(presentdays);
            }
            return leaveType.getLeave_type_short().toLowerCase() + "/" + "p";
        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "p/a")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() == 0.5) {
            if (leaveType.getType().equals(ATTENDENCE)) {
                presentdays++;
                employeeSheet.setPresentdays(presentdays);
            } else {
                leavetaken = leavetaken + 0.5;
                presentdays = presentdays + 0.5;
                employeeSheet.setLeavetaken(leavetaken);
                employeeSheet.setPresentdays(presentdays);
            }
            return "p" + "/" + leaveType.getLeave_type_short().toLowerCase();

        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "a/p")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() > 0.5) {
            if (leaveType.getType().equals(ATTENDENCE)) {
                presentdays++;
                employeeSheet.setPresentdays(presentdays);
            } else {
                leavetaken++;
                employeeSheet.setLeavetaken(leavetaken);
            }
            return leaveType.getLeave_type_short();

        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "p/a")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() > 0.5) {
            if (leaveType.getType().equals(ATTENDENCE)) {
                presentdays++;
                employeeSheet.setPresentdays(presentdays);
            } else {
                leavetaken++;
                employeeSheet.setLeavetaken(leavetaken);
            }
            return leaveType.getLeave_type_short();

        }

        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "A")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() > 0.5) {
            if (leaveType.getType().equals(ATTENDENCE)) {
                presentdays++;
                employeeSheet.setPresentdays(presentdays);
            } else {
                leavetaken++;
                employeeSheet.setLeavetaken(leavetaken);
            }
            return leaveType.getLeave_type_short();
        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "P")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() > 0.5) {
            if (leaveType.getType().equals(ATTENDENCE)) {
                presentdays++;
                employeeSheet.setPresentdays(presentdays);
            } else {
                leavetaken++;
                employeeSheet.setLeavetaken(leavetaken);
            }
            return leaveType.getLeave_type_short();
        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "A")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() == 0.5) {
            if (leaveType.getType().equals(ATTENDENCE)) {
                presentdays = presentdays + 0.5;
                employeeSheet.setPresentdays(presentdays);
            } else {
                leavetaken = leavetaken + 0.5;
                employeeSheet.setLeavetaken(leavetaken);
            }
            absentdays = absentdays + 0.5;
            employeeSheet.setAbsentdays(absentdays);
            return "a" + "/" + leaveType.getLeave_type_short().toLowerCase();

        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "P")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && !StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() == 0.5) {
            if (leaveType.getType().equals(ATTENDENCE)) {
                presentdays = presentdays + 0.5;
                employeeSheet.setPresentdays(presentdays);
            } else {
                leavetaken = leavetaken + 0.5;
                employeeSheet.setLeavetaken(leavetaken);
            }
            presentdays = presentdays + 0.5;
            employeeSheet.setPresentdays(presentdays);
            return "p" + "/" + leaveType.getLeave_type_short();

        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "a/p")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() == 0.5) {
            absentdays = absentdays + 0.5;
            employeeSheet.setAbsentdays(absentdays);
            presentdays = presentdays + 0.5;
            employeeSheet.setPresentdays(presentdays);
            return leaveType.getLeave_type_short().toLowerCase() + "/" + "p";

        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "p/a")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() == 0.5) {
            absentdays = absentdays + 0.5;
            employeeSheet.setAbsentdays(absentdays);
            presentdays = presentdays + 0.5;
            employeeSheet.setPresentdays(presentdays);
            return "p" + "/" + leaveType.getLeave_type_short().toLowerCase();

        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "A")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() > 0.5) {
            absentdays++;
            employeeSheet.setAbsentdays(absentdays);
            return leaveType.getLeave_type_short();

        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "A")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() == 0.5) {
            absentdays++;
            employeeSheet.setAbsentdays(absentdays);
            return "a" + "/" + leaveType.getLeave_type_short().toLowerCase();

        }

        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "a/p")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() > 0.5) {
            absentdays++;
            employeeSheet.setAbsentdays(absentdays);
            return "AL";

        }
        if (StringUtils.equals(biometricAttendance.getPresentStatus(), "p/a")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() > 0.5) {
            absentdays++;
            employeeSheet.setAbsentdays(absentdays);
            return "AL";

        }if (StringUtils.equals(biometricAttendance.getPresentStatus(), "P")
                && leaveTypes.contains(leaveType.getLeave_type_short())
                && ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL")
                && leaveApply.getNo_of_days_applied() > 0.5) {
            absentdays++;
            employeeSheet.setAbsentdays(absentdays);
            return "AL";

        }
        return null;

    }

    private double incrementPayDays(EmployeeSheet employeeSheet) {
        double presentDays = employeeSheet.getPresentdays();
        double generalWO = employeeSheet.getGeneralWO();
        double leaveTaken = employeeSheet.getLeavetaken();
        return presentDays + leaveTaken + generalWO;
    }

    private Boolean checkDateOfRelieving(Integer empId, String formattedDate) throws ParseException {
        String dateOfRelieving = resignationRepository.getRelievingDate(empId);
        if (StringUtils.isNotEmpty(dateOfRelieving)) {

            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = originalFormat.parse(dateOfRelieving);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy");
            String requiredFormattedDate = targetFormat.format(date);

            Date dateOfRelieved = new SimpleDateFormat("dd-MM-yyyy").parse(requiredFormattedDate);
            Date formattedCurrentDay = new SimpleDateFormat("dd-MM-yyyy").parse(formattedDate);

            return formattedCurrentDay.after(dateOfRelieved);
        }
        return false;

    }

    private Boolean isJoiningDate(Integer empId, String formattedDate) throws ParseException {
        String dateOfJoining = employeeDetailsRepository.getDateofJoining(empId);
        if (StringUtils.isNotEmpty(dateOfJoining)) {
            Date doj = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfJoining);
            Date fd = new SimpleDateFormat("dd-MM-yyyy").parse(formattedDate);

            Calendar fdCalendar = Calendar.getInstance();
            fdCalendar.setTime(fd);
            int fdYear = fdCalendar.get(Calendar.YEAR);
            int fdMonth = fdCalendar.get(Calendar.MONTH);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(doj);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            int dojYear = calendar.get(Calendar.YEAR);
            int dojMonth = calendar.get(Calendar.MONTH);
            doj = calendar.getTime();
            boolean result = fd.before(doj) && fdCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH);
            System.out.println("Result: " + result);

            return fd.before(calendar.getTime()) && fdCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                    && fdCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
        }
        return false;

    }

    private boolean checkConsecutiveAbsences(EmployeeSheet employeeSheet, int currentDay, int currentMonth,
                                             int currentYear, Calendar calendar, int direction) {
        int consecutiveAbsentCounter = 0;

        for (int i = 1; i <= 7; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, direction);

            Date day = calendar.getTime();
            System.out.println(day);

            Calendar dayCalendar = Calendar.getInstance();
            dayCalendar.setTime(day);

            int month = dayCalendar.get(Calendar.MONTH) + 1;
            int dayOfMonth = dayCalendar.get(Calendar.DAY_OF_MONTH);
            int year = dayCalendar.get(Calendar.YEAR);

            if (month != currentMonth) {
                Map<String, String> dayValue = employeeSheetRepository.getDayValue(employeeSheet.getEmpId(), month, dayOfMonth, year);
                String result = dayValue != null ? dayValue.get("result") : null;
                if (ObjectUtils.isNotEmpty(result) && StringUtils.equals(result, "A")) {
                    consecutiveAbsentCounter++;
                } else {
                    consecutiveAbsentCounter = 0;
                }
            } else {
                Map<Integer, String> values = getDayValues(employeeSheet);
                if (ObjectUtils.isNotEmpty(values) && ObjectUtils.isNotEmpty(values.get(dayOfMonth))
                        && StringUtils.equals(values.get(dayOfMonth), "A")) {
                    consecutiveAbsentCounter++;
                } else {
                    consecutiveAbsentCounter = 0;
                }
            }
            if (consecutiveAbsentCounter == 7) {
                return true;
            } else if (consecutiveAbsentCounter != i) {
                return false;
            }

        }

        return false;
    }

    private Map<Integer, String> getDayValues(EmployeeSheet employeeSheet) {
        Map<Integer, String> values = new LinkedHashMap<>();
        values.put(1, employeeSheet.getDay1());
        values.put(2, employeeSheet.getDay2());
        values.put(3, employeeSheet.getDay3());
        values.put(4, employeeSheet.getDay4());
        values.put(5, employeeSheet.getDay5());
        values.put(6, employeeSheet.getDay6());
        values.put(7, employeeSheet.getDay7());
        values.put(8, employeeSheet.getDay8());
        values.put(9, employeeSheet.getDay9());
        values.put(10, employeeSheet.getDay10());
        values.put(11, employeeSheet.getDay11());
        values.put(12, employeeSheet.getDay12());
        values.put(13, employeeSheet.getDay13());
        values.put(14, employeeSheet.getDay14());
        values.put(15, employeeSheet.getDay15());
        values.put(16, employeeSheet.getDay16());
        values.put(17, employeeSheet.getDay17());
        values.put(18, employeeSheet.getDay18());
        values.put(19, employeeSheet.getDay19());
        values.put(20, employeeSheet.getDay20());
        values.put(21, employeeSheet.getDay21());
        values.put(22, employeeSheet.getDay22());
        values.put(23, employeeSheet.getDay23());
        values.put(24, employeeSheet.getDay24());
        values.put(25, employeeSheet.getDay25());
        values.put(26, employeeSheet.getDay26());
        values.put(27, employeeSheet.getDay27());
        values.put(28, employeeSheet.getDay28());
        values.put(29, employeeSheet.getDay29());
        values.put(30, employeeSheet.getDay30());
        values.put(31, employeeSheet.getDay31());

        return values;
    }

    private String setStatusOfTheDay(String today, BiometricAttendance biometricAttendance, LeaveApply leaveApply,
                                     Boolean isJoiningDate, Boolean isRelievingDate, double absentdays, double generalWO,
                                     double presentdays, double leavetaken, EmployeeSheet employeeSheet, String holiday, Date fromDate,
                                     Date toDate, int currentDay, LeaveType leaveType, String formattedDate, List<LeaveApply> leaveApplyList) throws ParseException {

        if (isRelievingDate) return "R";
        if (isJoiningDate) return "N";
        double payDays = presentdays + leavetaken + generalWO;

        if (leaveApplyList.size() > 1) {
            StringBuilder firstHalf = new StringBuilder();
            StringBuilder secondHalf = new StringBuilder();
            for (LeaveApply la : leaveApplyList) {
                LeaveType lT = leaveTypeRepository.fetchLeaveType(la.getLeave_id());
                if (lT.getLeave_type_short().equalsIgnoreCase("AL")) {
                    absentdays = absentdays + 0.5;
                    employeeSheet.setAbsentdays(absentdays);
                } else if (lT.getType().equalsIgnoreCase("Leave")) {
                    leavetaken = leavetaken + 0.5;
                    employeeSheet.setLeavetaken(leavetaken);
                } else if (lT.getType().equalsIgnoreCase("Attendence")) {
                    presentdays = presentdays + 0.5;
                    employeeSheet.setPresentdays(presentdays);
                }
                if (la.getShift().equalsIgnoreCase("FirstHalf")) {
                    firstHalf.append(lT.getLeave_type_short());
                } else {
                    secondHalf.append(lT.getLeave_type_short());
                }
            }
            return firstHalf.toString().toLowerCase() + "/" + secondHalf.toString().toLowerCase();
        }

        // 7 prev Abs 7 after   ===> A WO A
        if ((holiday != null || today.equals("Sunday")) && ObjectUtils.isEmpty(leaveApply)) {
            String status = employeeAbsentCheckerForGivenWeekOff1(employeeSheet, currentDay, formattedDate, leaveType);
            if (status != null) {
                return status;
            }
        }

        if (!today.equals("Sunday") && ObjectUtils.isEmpty(biometricAttendance) && ObjectUtils.isEmpty(leaveApply)
                && !isJoiningDate && ObjectUtils.isNotEmpty(isRelievingDate) && !isRelievingDate
                && StringUtils.isEmpty(holiday) && ObjectUtils.isEmpty(leaveApply)) {
            absentdays++;
            employeeSheet.setAbsentdays(absentdays + prefixPostfixAbsent(employeeSheet, currentDay, generalWO, formattedDate));
            return "A";
        } else if (!today.equals("Sunday") && ObjectUtils.isNotEmpty(biometricAttendance) && ObjectUtils.isEmpty(leaveApply)
                && !isJoiningDate && ObjectUtils.isNotEmpty(isRelievingDate) && !isRelievingDate
                && StringUtils.isEmpty(holiday) && ObjectUtils.isEmpty(leaveApply)) {
            if (StringUtils.equals(biometricAttendance.getPresentStatus(), "P")) {
                presentdays++;
                employeeSheet.setPresentdays(presentdays);
                return "P";
            } else if (StringUtils.equals(biometricAttendance.getPresentStatus(), "A")) {
                absentdays++;
                employeeSheet.setAbsentdays(absentdays + prefixPostfixAbsent(employeeSheet, currentDay, generalWO, formattedDate));
                return "A";
            } else if (StringUtils.equals(biometricAttendance.getPresentStatus(), "p/a")) {
                double presentDays = presentdays + 0.5d;
                double absentDays = absentdays + 0.5d;
                employeeSheet.setPresentdays(presentDays);
                employeeSheet.setAbsentdays(absentDays);
                return "p/a";
            } else if (StringUtils.equals(biometricAttendance.getPresentStatus(), "a/p")) {
                double absentDays = absentdays + 0.5d;
                double presentDays = presentdays + 0.5d;
                employeeSheet.setPresentdays(presentDays);
                employeeSheet.setAbsentdays(absentDays);
                return "a/p";
            }
        } else if (!today.equals("Sunday") && ObjectUtils.isNotEmpty(holiday) && !isJoiningDate
                && ObjectUtils.isNotEmpty(isRelievingDate) && !isRelievingDate
                && (ObjectUtils.isEmpty(leaveType) || ObjectUtils.isNotEmpty(leaveType))) {
            if (ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL")) {
                absentdays++;
                employeeSheet.setAbsentdays(absentdays);
                return leaveType.getLeave_type_short();
            } else if (ObjectUtils.isNotEmpty(leaveType) && !StringUtils.equals(leaveType.getLeave_type_short(), "ML")) {
                if (StringUtils.equals(leaveType.getType(), ATTENDENCE)) {
                    presentdays++;
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    leavetaken++;
                    employeeSheet.setLeavetaken(leavetaken);
                }
                return leaveType.getLeave_type_short();
            } else {
                if (isBetweenMLLeaveDates(employeeSheet, formattedDate)) return "ML";
                generalWO++;
                employeeSheet.setGeneralWO(generalWO);
                return holiday;
            }
        } else if (today.equals("Sunday") && !isJoiningDate && ObjectUtils.isNotEmpty(isRelievingDate)
                && !isRelievingDate && (StringUtils.isEmpty(holiday) || StringUtils.isNotEmpty(holiday))
                && (ObjectUtils.isEmpty(leaveType) || ObjectUtils.isNotEmpty(leaveType))) {
            if (ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL")) {
                absentdays++;
                employeeSheet.setAbsentdays(absentdays);
                return leaveType.getLeave_type_short();
            } else if (ObjectUtils.isNotEmpty(leaveType) && !StringUtils.equals(leaveType.getLeave_type_short(), "ML")) {
                if (StringUtils.equals(leaveType.getType(), ATTENDENCE)) {
                    presentdays++;
                    employeeSheet.setPresentdays(presentdays);
                } else {
                    leavetaken++;
                    employeeSheet.setLeavetaken(leavetaken);
                }
                return leaveType.getLeave_type_short();
            } else {
                if (isBetweenMLLeaveDates(employeeSheet, formattedDate)) return "ML";
                generalWO++;
                employeeSheet.setGeneralWO(generalWO);
                return "WO";
            }


        } else if (!today.equals("Sunday") && ObjectUtils.isEmpty(biometricAttendance)
                && (ObjectUtils.isNotEmpty(leaveApply) && leaveApply.getNo_of_days_applied() != null && leaveApply.getNo_of_days_applied() != 0.5)
                && !isJoiningDate && ObjectUtils.isNotEmpty(isRelievingDate) && !isRelievingDate
                && StringUtils.isEmpty(holiday)) {
            if (StringUtils.equals(leaveType.getLeave_type_short(), "AL")) {
                absentdays++;
                employeeSheet.setAbsentdays(absentdays + prefixPostfixAbsent(employeeSheet, currentDay, generalWO, formattedDate));
            } else if (StringUtils.equalsIgnoreCase(leaveType.getLeave_type_short(), "ML")) {
                return leaveType.getLeave_type_short(); // For ML, we don't need to add any WO/GH/P/A/PAYDAYS must be 0
            } else if (StringUtils.equals(leaveType.getType(), ATTENDENCE)) {
                presentdays++;
                employeeSheet.setPresentdays(presentdays);
            } else {
                leavetaken++;
                employeeSheet.setLeavetaken(leavetaken);
            }
            return leaveType.getLeave_type_short();

        } else if (!today.equals("Sunday") && ObjectUtils.isNotEmpty(biometricAttendance)
                && ObjectUtils.isNotEmpty(leaveApply) && !isJoiningDate
                && ObjectUtils.isNotEmpty(isRelievingDate) && !isRelievingDate && StringUtils.isEmpty(holiday)) {
            return checkHalfDayLeaveApply(biometricAttendance, leaveApply, employeeSheet, leaveType,
                    presentdays, absentdays, leavetaken);
        } else if (ObjectUtils.isEmpty(biometricAttendance) && ObjectUtils.isNotEmpty(leaveApply)) {
            return halfDayWithoutBiometricStatus(employeeSheet, leaveApply, presentdays, absentdays, leavetaken, leaveType);
        } else if (isJoiningDate && ObjectUtils.isNotEmpty(isRelievingDate) && !isRelievingDate) {
            return "N";
        } else if (ObjectUtils.isNotEmpty(isRelievingDate) && isRelievingDate) {
            return "R";
        }
        return null;
    }

    private String halfDayWithoutBiometricStatus(EmployeeSheet employeeSheet, LeaveApply leaveApply, double presentdays,
                                                 double absentdays, double leavetaken, LeaveType leaveType) {
        String firstHalf = "a";
        String secondHalf = "a";

        if (leaveType.getType().equalsIgnoreCase("AL")) {
            absentdays = absentdays + 0.5;
            employeeSheet.setAbsentdays(absentdays);
        } else if (leaveType.getType().equalsIgnoreCase("Attendence")) {
            presentdays = presentdays + 0.5;
            employeeSheet.setPresentdays(presentdays);
        } else {
            leavetaken = leavetaken + 0.5;
            employeeSheet.setLeavetaken(leavetaken);
        }

        if (leaveApply.getShift().equalsIgnoreCase("FirstHalf"))
            firstHalf = leaveType.getLeave_type_short().toLowerCase();
        else
            secondHalf = leaveType.getLeave_type_short().toLowerCase();

        return firstHalf + "/" + secondHalf;
    }

    private boolean isBetweenMLLeaveDates(EmployeeSheet employeeSheet, String formattedDate) {
        Integer leaveId = 4; // ML leaveId as per the date 07-02-2025
        Map<String, String> maternityLeave = leaveApplyRepository.findByEmp_IdAndLeave_Id(employeeSheet.getEmpId(), leaveId);
        if (maternityLeave.isEmpty()) return false;
        String mLFromDate = maternityLeave.get("fromDate");
        String mLTomDate = maternityLeave.get("toDate");

        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Convert String dates to LocalDate
        LocalDate localFromDate = LocalDate.parse(mLFromDate, formatter);
        LocalDate localToDate = LocalDate.parse(mLTomDate, formatter);
        LocalDate localFormattedDate = LocalDate.parse(formattedDate, formatter);

        // Check if formattedDate is between fromDate and toDate (inclusive)
        if ((localFormattedDate.isEqual(localFromDate) || localFormattedDate.isAfter(localFromDate)) &&
                (localFormattedDate.isEqual(localToDate) || localFormattedDate.isBefore(localToDate))) {
            return true;
        }
        return false;
    }

    private double prefixPostfixAbsent(EmployeeSheet employeeSheet, int currentDay, double generalWO, String formatedDate) {

        double count = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(formatedDate, formatter);
        int currentMonth = date.getMonthValue();
        int currentYear = date.getYear();
        EmployeeSheet tempEmployeeSheet = employeeSheet;
        int tempMonth = currentMonth;
        int tempYear = currentYear;

        int tempDay = currentDay - 1;

        if (tempDay == 0) {
            if (tempMonth == 1) {
                tempMonth = 12;
                tempYear = tempYear - 1;
            } else tempMonth = tempMonth - 1;
            tempEmployeeSheet = employeeSheetRepository.findByEmpIdAndMonthAndYear(employeeSheet.getEmpId(), tempMonth, tempYear);
            tempDay = YearMonth.of(tempYear, tempMonth).lengthOfMonth();
        }
        if (tempEmployeeSheet != null) {
            while ((getStatusOfTheDay(tempEmployeeSheet, tempDay).equals("GH")) ||
                    (getStatusOfTheDay(tempEmployeeSheet, tempDay).equals("WO")) ||
                    (getStatusOfTheDay(tempEmployeeSheet, tempDay).equals("DH"))) {
                tempDay = tempDay - 1;

                if (tempDay == 0) {

                    if (tempMonth == 1) {
                        tempMonth = 12;
                        tempYear = tempYear - 1;
                    } else tempMonth = tempMonth - 1;
                    tempEmployeeSheet = employeeSheetRepository.findByEmpIdAndMonthAndYear(employeeSheet.getEmpId(), tempMonth, tempYear);
                    if (tempEmployeeSheet == null) {
                        break;
                    }
                    tempDay = YearMonth.of(tempYear, tempMonth).lengthOfMonth();
                }
            }

            if (tempEmployeeSheet != null &&
                    (
                            getStatusOfTheDay(tempEmployeeSheet, tempDay).equals("A") ||
                                    getStatusOfTheDay(tempEmployeeSheet, tempDay).equals("AL")
                    )
            ) {

                if (tempMonth != currentMonth) {

                    tempDay = tempDay + 1;
                    int totalDays = YearMonth.of(tempYear, tempMonth).lengthOfMonth();
                    while (tempDay <= totalDays) {
                        setAbsentForTheDay(tempEmployeeSheet, tempDay);
                        tempEmployeeSheet.setGeneralWO(tempEmployeeSheet.getGeneralWO() - 1);
                        tempEmployeeSheet.setAbsentdays(tempEmployeeSheet.getAbsentdays() + 1);
                        tempEmployeeSheet.setPaydays(tempEmployeeSheet.getPaydays() - 1);
                        employeeSheetRepository.save(tempEmployeeSheet);
                        tempDay++;
                    }
                    tempDay = 0;
                    if (tempMonth == 12) tempMonth = 1;
                    else tempMonth = tempMonth + 1;
                }

                if (tempMonth == currentMonth) {
                    tempEmployeeSheet = employeeSheet;

                    tempDay = tempDay + 1;
                    while (tempDay < currentDay) {
                        setAbsentForTheDay(tempEmployeeSheet, tempDay);
                        count++;
                        if (generalWO > 0)
                            generalWO--;
                        tempEmployeeSheet.setGeneralWO(generalWO);
                        tempDay++;
                    }
                }
            }

        }
        return count;

    }

    private void setAbsentForTheDay(EmployeeSheet employeeSheet, int tempDay) {

        switch (tempDay) {
            case 1:
                employeeSheet.setDay1("A");
                break;
            case 2:
                employeeSheet.setDay2("A");
                break;
            case 3:
                employeeSheet.setDay3("A");
                break;
            case 4:
                employeeSheet.setDay4("A");
                break;
            case 5:
                employeeSheet.setDay5("A");
                break;
            case 6:
                employeeSheet.setDay6("A");
                break;
            case 7:
                employeeSheet.setDay7("A");
                break;
            case 8:
                employeeSheet.setDay8("A");
                break;
            case 9:
                employeeSheet.setDay9("A");
                break;
            case 10:
                employeeSheet.setDay10("A");
                break;
            case 11:
                employeeSheet.setDay11("A");
                break;
            case 12:
                employeeSheet.setDay12("A");
                break;
            case 13:
                employeeSheet.setDay13("A");
                break;
            case 14:
                employeeSheet.setDay14("A");
                break;
            case 15:
                employeeSheet.setDay15("A");
                break;
            case 16:
                employeeSheet.setDay16("A");
                break;
            case 17:
                employeeSheet.setDay17("A");
                break;
            case 18:
                employeeSheet.setDay18("A");
                break;
            case 19:
                employeeSheet.setDay19("A");
                break;
            case 20:
                employeeSheet.setDay20("A");
                break;
            case 21:
                employeeSheet.setDay21("A");
                break;
            case 22:
                employeeSheet.setDay22("A");
                break;
            case 23:
                employeeSheet.setDay23("A");
                break;
            case 24:
                employeeSheet.setDay24("A");
                break;
            case 25:
                employeeSheet.setDay25("A");
                break;
            case 26:
                employeeSheet.setDay26("A");
                break;
            case 27:
                employeeSheet.setDay27("A");
                break;
            case 28:
                employeeSheet.setDay28("A");
                break;
            case 29:
                employeeSheet.setDay29("A");
                break;
            case 30:
                employeeSheet.setDay30("A");
                break;
            case 31:
                employeeSheet.setDay31("A");
                break;
        }
    }

    private String getStatusOfTheDay(EmployeeSheet employeeSheet, int i) {

        String result = "";
        switch (i) {
            case 1:
                result = employeeSheet.getDay1();
                break;
            case 2:
                result = employeeSheet.getDay2();
                break;
            case 3:
                result = employeeSheet.getDay3();
                break;
            case 4:
                result = employeeSheet.getDay4();
                break;
            case 5:
                result = employeeSheet.getDay5();
                break;
            case 6:
                result = employeeSheet.getDay6();
                break;
            case 7:
                result = employeeSheet.getDay7();
                break;
            case 8:
                result = employeeSheet.getDay8();
                break;
            case 9:
                result = employeeSheet.getDay9();
                break;
            case 10:
                result = employeeSheet.getDay10();
                break;
            case 11:
                result = employeeSheet.getDay11();
                break;
            case 12:
                result = employeeSheet.getDay12();
                break;
            case 13:
                result = employeeSheet.getDay13();
                break;
            case 14:
                result = employeeSheet.getDay14();
                break;
            case 15:
                result = employeeSheet.getDay15();
                break;
            case 16:
                result = employeeSheet.getDay16();
                break;
            case 17:
                result = employeeSheet.getDay17();
                break;
            case 18:
                result = employeeSheet.getDay18();
                break;
            case 19:
                result = employeeSheet.getDay19();
                break;
            case 20:
                result = employeeSheet.getDay20();
                break;
            case 21:
                result = employeeSheet.getDay21();
                break;
            case 22:
                result = employeeSheet.getDay22();
                break;
            case 23:
                result = employeeSheet.getDay23();
                break;
            case 24:
                result = employeeSheet.getDay24();
                break;
            case 25:
                result = employeeSheet.getDay25();
                break;
            case 26:
                result = employeeSheet.getDay26();
                break;
            case 27:
                result = employeeSheet.getDay27();
                break;
            case 28:
                result = employeeSheet.getDay28();
                break;
            case 29:
                result = employeeSheet.getDay29();
                break;
            case 30:
                result = employeeSheet.getDay30();
                break;
            case 31:
                result = employeeSheet.getDay31();
                break;
            default:
                result = "Invalid day"; // Fallback for any issues (like day number less than 1 or more than 31)
                break;
        }

        return result;

    }

    private String employeeAbsentCheckerForGivenWeekOff1(EmployeeSheet employeeSheet, int currentDay, String formattedDate,
                                                         LeaveType leaveType) throws ParseException {

        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(formattedDate);
        Calendar previousDaycalendar = Calendar.getInstance();
        previousDaycalendar.setTime(date);
        int currentMonth = previousDaycalendar.get(Calendar.MONTH) + 1;
        int currentYear = previousDaycalendar.get(Calendar.YEAR);
        Calendar nextDaycalendar = Calendar.getInstance();
        nextDaycalendar.setTime(date);
        boolean sevenDaysPreviousAbsent = checkConsecutiveAbsences(employeeSheet, currentDay, currentMonth, currentYear,
                previousDaycalendar, -1);
        boolean sevenDaysLaterAbsent = checkConsecutiveAbsences(employeeSheet, currentDay, currentMonth, currentYear,
                nextDaycalendar, 1);

        // If seven continuous absences are found in previous or later days, mark as
        // absent.
        if (sevenDaysPreviousAbsent || sevenDaysLaterAbsent) {
            return markCurrentDayAsAbsent1(currentDay, employeeSheet, leaveType);
        }
        return null;
    }

    private String markCurrentDayAsAbsent1(int currentDay, EmployeeSheet employeeSheet, LeaveType leaveType) {
        employeeSheet.setAbsentdays(employeeSheet.getAbsentdays() + 1);
        return ObjectUtils.isNotEmpty(leaveType) && StringUtils.equals(leaveType.getLeave_type_short(), "AL") ? "AL" : "A";
    }
}
