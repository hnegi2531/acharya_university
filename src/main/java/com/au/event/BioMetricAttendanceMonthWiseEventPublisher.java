package com.au.event;

import com.au.controller.TriggerRepository;
import com.au.model.BiometricAttendance;
import com.au.model.BiometricTransaction;
import com.au.model.Shift;
import com.au.model.Triggers;
import com.au.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BioMetricAttendanceMonthWiseEventPublisher {

	@Autowired
	private BiometricTransactionRepository biometricTransactionRepository;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	private ShiftRepository shiftRepository;

	@Autowired
	private BiometricAttendanceRepository biometricAttendanceRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ResignationRepository resignationRepository;

	@Autowired
	private EmployeeDetailsHistoryRepository employeeDetailsHistoryRepository;

	@Autowired
	private School_Repository schoolRepository;

	@Autowired
	private HolidayCalenderRepository holidayCalenderRepository;

	@Autowired
	private TriggerRepository triggerRepository;

	@Async
	@EventListener
	public void handleBioMetricAttendanceEvent(BioMetricAttendanceEvent bioMetricAttendanceEvent) {

		String triggerName = "biometricAttendanceTrigger";
		Triggers trigger = triggerRepository.findByName(triggerName).get();
		trigger.setStatus("ACTIVE");
		triggerRepository.save(trigger);

		System.out.println("biometricAttendanceTrigger started ...... " + " KEYWORD ");

		try {
			Integer month;
			Integer year;
			Integer empId = bioMetricAttendanceEvent.getEmpId();
			String empCode = null;

			Calendar currentCalendar = Calendar.getInstance();

			int givenMonth = bioMetricAttendanceEvent.getMonth();
			int givenYear = bioMetricAttendanceEvent.getYear();
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
			// calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
			int currentDay;
			if (year < currentCalendar.get(Calendar.YEAR)
					|| (year == currentCalendar.get(Calendar.YEAR) && month < currentCalendar.get(Calendar.MONTH))) {
				calendar.set(Calendar.MONTH, month);
				currentDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			} else {
				currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			int intialCount = 1;
			while (calendar.get(Calendar.MONTH) == month && intialCount <= currentDay) {
				String formattedDate = formatter.format(calendar.getTime());
				System.out.println(formattedDate);
				LocalDate today = LocalDate.parse(formattedDate);
				LocalDate targetDate = LocalDate.of(2025, 5, 2);
				boolean isBeforeTarget = today.isBefore(targetDate);

				if (empId != null) {
					empCode = isBeforeTarget
							? employeeDetailsRepository.findContractEmpcodeByEmpId(empId)
							: employeeDetailsRepository.findEmpcodeByEmpId(empId);
				}
				Date trnDate = formatter.parse(formattedDate);
				System.out.println(month + " [[[[[[[[[[[[[[[[[[ " + calendar.get(Calendar.MONTH));
				System.out.println(trnDate);

				List<BiometricTransaction> biometricTransactions;
				if(empCode != null) biometricTransactions = biometricTransactionRepository.getByTrnDateAndEmpcode(formattedDate, empCode);
				else biometricTransactions = biometricTransactionRepository.getByTrnDate(formattedDate);

				setBioTransDataOnBasisOfTrnDate(biometricTransactions, formattedDate, month, year, calendar, bioMetricAttendanceEvent,
						isBeforeTarget);

				intialCount++;
				System.out.println("Days------> " + intialCount);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
		} catch (Exception e) {
			log.error("Exception occur " + e.getMessage());
		}finally {
			trigger = triggerRepository.findByName(triggerName).get();
			trigger.setStatus("INACTIVE");
			triggerRepository.save(trigger);
			System.out.println("biometricAttendanceTrigger Ended ......" + " KEYWORD ");
		}

	}

	@SuppressWarnings("null")
	private void setBioTransDataOnBasisOfTrnDate(List<BiometricTransaction> biometricTransactions, String formattedDate,
												 Integer month, Integer year, Calendar calendar, BioMetricAttendanceEvent bioMetricAttendanceEvent, boolean isBeforeTarget) {
		try {
			if (ObjectUtils.isNotEmpty(biometricTransactions)) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
				Map<String, List<BiometricTransaction>> bioTransData = biometricTransactions.stream()
						.sorted(Comparator.comparing(BiometricTransaction::getTrnTime)).collect(Collectors
								.groupingBy(BiometricTransaction::getEmpcode, LinkedHashMap::new, Collectors.toList()));
				bioTransData.forEach((k, v) -> {
					try {
						if (ObjectUtils.isNotEmpty(v) && !checkReleivingDate(k, formattedDate)) {
							Integer empId;
							Integer shiftId;
							Integer deptId;
							String employeeName;
							Integer schoolId;
							String deptname;
							String punchCardStatus;
							String empcode;
							if (isBeforeTarget) {
								// CONTRACT CODE ===> PHP
								empId = employeeDetailsRepository.getEmpIdByEmpCode(k);
								shiftId = employeeDetailsRepository.getShiftId(k);
								deptId = employeeDetailsRepository.getDeptIdByEmpCode(k);
								employeeName = employeeDetailsRepository.getEmployeeNameByEmpCode(k);
								schoolId = schoolRepository.getSchoolId(k);
								deptname = departmentRepository.getDepartment(deptId);
								punchCardStatus = employeeDetailsRepository.getPunchCardStatusByEmpCode(k);
								empcode = employeeDetailsRepository.getEmpCodeByContractEmpCode(k);
							} else {
								// CONTRACT CODE ===> JAVA
								empId = employeeDetailsRepository.findByJavaEmpcode(k);
								shiftId = employeeDetailsRepository.getShiftIdByJavaEmpcode(k);
								deptId = employeeDetailsRepository.getDeptIdByJavaEmpCode(k);
								employeeName = employeeDetailsRepository.getEmployeeNameByJavaEmpCode(k);
								schoolId = schoolRepository.getSchoolIdByJavaEmpcode(k);
								deptname = departmentRepository.getDepartment(deptId);
								punchCardStatus = employeeDetailsRepository.getPunchCardStatusByJavaEmpCode(k);
								empcode = k;
							}
							BiometricTransaction firstEntry = v.get(0);
							Time firstEntryTrnTime = ObjectUtils.isNotEmpty(firstEntry.getTrnTime())
									? firstEntry.getTrnTime()
									: null;
							LocalTime firstEntryTime = null;
							LocalTime modifiedEntryTime = null;
							if (ObjectUtils.isNotEmpty(firstEntryTrnTime)) {
								firstEntryTime = firstEntryTrnTime.toLocalTime().withSecond(0);
								modifiedEntryTime = firstEntryTime.plusHours(5).plusMinutes(30).withSecond(0);
							}

							BiometricTransaction lastEntry = !v.isEmpty() ? v.get(v.size() - 1) : null;
							Time lastEntryTrnTime = ObjectUtils.isNotEmpty(lastEntry)
									&& ObjectUtils.isNotEmpty(lastEntry.getTrnTime()) ? lastEntry.getTrnTime() : null;
							LocalTime lastEntryTime = null;
							LocalTime modifiedExitTime = null;

							if (ObjectUtils.isNotEmpty(lastEntryTrnTime)) {
								lastEntryTime = lastEntryTrnTime.toLocalTime().withSecond(0);
								modifiedExitTime = lastEntryTime.plusHours(5).plusMinutes(30).withSecond(0);

							}

							if (ObjectUtils.isNotEmpty(shiftId)) {
								BiometricAttendance biometricAttendance = biometricAttendanceRepository
										.findFirstByDateAndEmpCode(formattedDate, empcode);
								if(ObjectUtils.isNotEmpty(biometricAttendance)){
									shiftId = biometricAttendance.getShiftId();
								}
								if (ObjectUtils.isEmpty(biometricAttendance)) {
									biometricAttendance = new BiometricAttendance();

								}
								Shift shift = shiftRepository.getShiftData(shiftId);
								String realStartTime = shift.getShiftStartTime();
								Date rDateTime = dateFormat.parse(realStartTime);
								Time realTime = new Time(rDateTime.getTime());
								String shiftStartTime = shift.getGrace_time();
								Date startDateTime = dateFormat.parse(shiftStartTime);
								Time startTime = new Time(startDateTime.getTime());
								String shiftEndTime = shift.getShiftEndTime();
								Date endDateTime = dateFormat.parse(shiftEndTime);
								Time endTime = new Time(endDateTime.getTime());

								LocalTime start = LocalTime.parse(shift.getShiftStartTime());
								LocalTime end = LocalTime.parse(shift.getShiftEndTime());
								Duration totalTime = Duration.between(start, end);
								long totalHours = totalTime.toHours();
								long totalMinutes = totalTime.toMinutes();
								long totalSeconds = totalTime.getSeconds();
								long halfHours = totalHours / 2;
								long halfMinutes = (totalMinutes / 2) % 60;  // Half of total minutes (excluding the hours part)
								long halfSeconds = (totalSeconds / 2) % 60;  // Half of total seconds (excluding the minutes part)

								LocalTime firstHalf = realTime.toLocalTime().plusHours(halfHours).plusMinutes(halfMinutes).plusSeconds(halfSeconds);

								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

								if(ObjectUtils.isNotEmpty(shift.getFhPunchIn())) {

									Date fhStartTime = dateFormat.parse(shift.getFhPunchIn());
									startTime = new Time(fhStartTime.getTime());
								}
								if(ObjectUtils.isNotEmpty(shift.getFhPunchOut())) {
									Date fHDateTime = dateFormat.parse(shift.getFhPunchOut());
									Time fHTime = new Time(fHDateTime.getTime());
									firstHalf = fHTime.toLocalTime();
								}
								if(ObjectUtils.isNotEmpty(shift.getShPunchIn())) {
									Date sHDateTime = dateFormat.parse(shift.getShPunchIn());
									Time sHInTime = new Time(sHDateTime.getTime());
									firstHalf = sHInTime.toLocalTime();
								}
								if(ObjectUtils.isNotEmpty(shift.getShPunchOut())){
									Date shEndTime = dateFormat.parse(shift.getShPunchOut());
									endTime = new Time(shEndTime.getTime());

								}
								biometricAttendance
										.setShiftStartTime(ObjectUtils.isNotEmpty(shift.getGrace_time())
												? shift.getGrace_time()
												: null);
								biometricAttendance.setShiftEndTime(
										ObjectUtils.isNotEmpty(shift.getShiftEndTime()) ? shift.getShiftEndTime()
												: null);
								biometricAttendance.setGrace_start_time(
										ObjectUtils.isNotEmpty(shift.getShiftStartTime()) ? shift.getShiftStartTime()
												: null);
								biometricAttendance.setShiftId(shiftId);
								biometricAttendance.setStartTime(
										ObjectUtils.isNotEmpty(modifiedEntryTime) ? formatter.format(modifiedEntryTime)
												: null);
								biometricAttendance.setEndTime(
										ObjectUtils.isNotEmpty(modifiedExitTime) ? formatter.format(modifiedExitTime)
												: null);

								biometricAttendance.setEmpId(empId);
								biometricAttendance.setMonth(bioMetricAttendanceEvent.getMonth());
								biometricAttendance.setYear(bioMetricAttendanceEvent.getYear());
								biometricAttendance.setDeptId(deptId);
								biometricAttendance.setEmployeeName(employeeName);
								biometricAttendance.setDeptName(deptname);
								biometricAttendance.setDate(formattedDate);
								biometricAttendance.setEmpCode(empcode);
								biometricAttendance.setInstituteId(schoolId);
								biometricAttendance.setStartTime(
										ObjectUtils.isNotEmpty(modifiedEntryTime) ? formatter.format(modifiedEntryTime)
												: null);
								biometricAttendance.setEndTime(
										ObjectUtils.isNotEmpty(modifiedExitTime) ? formatter.format(modifiedExitTime)
												: null);
								biometricAttendance.setEmpId(empId);

								/* Flex Shift */
								if (!punchCardStatus.isEmpty() && punchCardStatus.equalsIgnoreCase("flexible")) {

									Duration duration = Duration.between(modifiedEntryTime, modifiedExitTime);
									long hours = duration.toHours();
									long minutes = duration.toMinutes() % 60; // Remaining minutes after extracting full hours
									long seconds = duration.getSeconds() % 60; // Remaining seconds after extracting full minutes
									long durationTotalMinutes = (hours * 60) + minutes;
                                    long targetTotalMinutes = totalMinutes - 10;
									long forHalfDayPresent = totalMinutes / 2;
									if (durationTotalMinutes >= targetTotalMinutes) {
										biometricAttendance.setAttendanceStatus(1d);
										biometricAttendance.setPresentStatus("P");
									} else if (durationTotalMinutes >= forHalfDayPresent) {
										biometricAttendance.setAttendanceStatus(0.5);
										biometricAttendance.setPresentStatus("a/p");
									} else {
										biometricAttendance.setAttendanceStatus(0d);
										biometricAttendance.setPresentStatus("A");
									}

									// Format the result as "hours:minutes:seconds"
									String formattedTime = String.format("%d:%02d:%02d", hours, minutes, seconds);
									biometricAttendance.setDuration(formattedTime);
									long targetHoursForCompOff = 4;
									long targetMinutesForCompOff = 10;
									long targetTotalMinutesForCompOff = (targetHoursForCompOff * 60) + targetMinutesForCompOff;
									String leave_type_short = holidayCalenderRepository.getleaveType(formattedDate);
									String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
									if (StringUtils.isNotEmpty(leave_type_short) && (durationTotalMinutes >= targetTotalMinutesForCompOff) &&
											(
												leave_type_short.equalsIgnoreCase("GH") ||
												leave_type_short.equalsIgnoreCase("DH") ||
												day.equalsIgnoreCase("Sunday")
											)
									) {
										biometricAttendance.setCompoffStatus(true);
									}
								}

								if (StringUtils.isNotEmpty(punchCardStatus)
										&& StringUtils.equalsIgnoreCase(punchCardStatus, "Optional")) {
									if ((ObjectUtils.isNotEmpty(modifiedEntryTime)
											&& !modifiedEntryTime.equals(modifiedExitTime))
											&& ObjectUtils.isNotEmpty(modifiedExitTime)) {
										biometricAttendance.setAttendanceStatus(1d);
										biometricAttendance.setPresentStatus("P");
									} else {
										biometricAttendance.setAttendanceStatus(1d);
										biometricAttendance.setPresentStatus("A");
									}
									Date d = dateFormat.parse(biometricAttendance.getStartTime());
									Date d1 = dateFormat.parse(biometricAttendance.getEndTime());
									long difference = d1.getTime() - d.getTime();

									long differenceSeconds = difference / 1000 % 60;
									long differenceMinutes = difference / (60 * 1000) % 60;
									long differenceHours = difference / (60 * 60 * 1000) % 24;
									String timeTaken = String.format("%d:%02d:%02d", differenceHours, differenceMinutes,
											differenceSeconds);
									biometricAttendance.setDuration(timeTaken);

								}

								if (StringUtils.isNotEmpty(punchCardStatus)
										&& StringUtils.equalsIgnoreCase(punchCardStatus, "No Swipe")) {
									biometricAttendance.setAttendanceStatus(1d);
									biometricAttendance.setPresentStatus("P");
									Date d = dateFormat.parse(biometricAttendance.getStartTime());
									Date d1 = dateFormat.parse(biometricAttendance.getEndTime());
									long difference = d1.getTime() - d.getTime();

									long differenceSeconds = difference / 1000 % 60;
									long differenceMinutes = difference / (60 * 1000) % 60;
									long differenceHours = difference / (60 * 60 * 1000) % 24;
									String timetaken = String.format("%d:%02d:%02d", differenceHours, differenceMinutes,
											differenceSeconds);
									biometricAttendance.setDuration(timetaken);

								}

								if (StringUtils.isEmpty(punchCardStatus)
										|| StringUtils.equalsIgnoreCase(punchCardStatus, "Mandatory")) {

									System.out.println(k + " modifiedExitTime " + modifiedExitTime + " endTime "
											+ endTime + " modifiedEntryTime " + modifiedEntryTime + " startTime "
											+ startTime + " formattedDate " + formattedDate);

									if (ObjectUtils.isNotEmpty(biometricAttendance.getShiftId())
											&& biometricAttendance.getShiftId() != shiftId) {
										Shift shiftChange = shiftRepository
												.getShiftData(biometricAttendance.getShiftId());
										biometricAttendance.setShiftStartTime(
												ObjectUtils.isNotEmpty(shiftChange.getGrace_time())
														? shiftChange.getShiftStartTime()
														: null);
										biometricAttendance
												.setShiftEndTime(ObjectUtils.isNotEmpty(shiftChange.getShiftEndTime())
														? shiftChange.getShiftEndTime()
														: null);
										shiftStartTime = shiftChange.getGrace_time();
										startDateTime = dateFormat.parse(shiftStartTime);
										startTime = new Time(startDateTime.getTime());
										shiftEndTime = shiftChange.getShiftEndTime();
										endDateTime = dateFormat.parse(shiftEndTime);
										endTime = new Time(endDateTime.getTime());

									}

									System.out.println(k + " modifiedExitTime " + modifiedExitTime + " endTime "
											+ endTime + " modifiedEntryTime " + modifiedEntryTime + " startTime "
											+ startTime + " formattedDate " + formattedDate);

									if ((modifiedExitTime.equals(endTime.toLocalTime().withSecond(0))
											|| modifiedExitTime.isAfter(endTime.toLocalTime().withSecond(0)))
											&& (modifiedEntryTime.isBefore(startTime.toLocalTime().withSecond(0))
													|| modifiedEntryTime
															.equals(startTime.toLocalTime().withSecond(0)))) {
										biometricAttendance.setAttendanceStatus(1d);
										biometricAttendance.setPresentStatus("P");
									} else if ((modifiedExitTime.isAfter(firstHalf) || modifiedExitTime.equals(firstHalf)) &&
											((modifiedExitTime.isBefore(endTime.toLocalTime().withSecond(0)) &&
													modifiedEntryTime.isAfter(modifiedExitTime) &&
													!modifiedEntryTime.equals(modifiedExitTime)
											) || (
													(modifiedEntryTime.isBefore(startTime.toLocalTime().withSecond(0)) ||
															(modifiedEntryTime.equals(startTime.toLocalTime().withSecond(0)))
													) &&
															modifiedExitTime.isBefore(endTime.toLocalTime().withSecond(0)) &&
															!modifiedEntryTime.equals(modifiedExitTime) && !
															modifiedExitTime.isBefore(firstHalf)
											)
											)
									) {
										biometricAttendance.setAttendanceStatus(0.5);
										biometricAttendance.setPresentStatus("p/a");
									} else if ((modifiedExitTime.isAfter(endTime.toLocalTime().withSecond(0))
											|| modifiedExitTime.equals(endTime.toLocalTime().withSecond(0)))
											&& (modifiedEntryTime.isBefore(firstHalf) || modifiedEntryTime.equals(firstHalf))) {
										biometricAttendance.setAttendanceStatus(0.5);
										biometricAttendance.setPresentStatus("a/p");
									} else {
										biometricAttendance.setAttendanceStatus(0d);
										biometricAttendance.setPresentStatus("A");
									}
									Date d = dateFormat.parse(biometricAttendance.getStartTime());
									Date d1 = dateFormat.parse(biometricAttendance.getEndTime());
									long difference = d1.getTime() - d.getTime();

									long differenceSeconds = difference / 1000 % 60;
									long differenceMinutes = difference / (60 * 1000) % 60;
									long differenceHours = difference / (60 * 60 * 1000) % 24;
									long durationTotalMinutes = (differenceHours * 60) + differenceMinutes;
									long targetHours = 4;
									long targetMinutes = 10;
									long targetTotalMinutes = (targetHours * 60) + targetMinutes;
									String timetaken = String.format("%d:%02d:%02d", differenceHours, differenceMinutes, differenceSeconds);
									biometricAttendance.setDuration(timetaken);
									long targetHoursForCompOff = 4;
									long targetMinutesForCompOff = 10;
									long targetTotalMinutesForCompOff = (targetHoursForCompOff * 60) + targetMinutesForCompOff;
									String leave_type_short = holidayCalenderRepository.getleaveType(formattedDate);
									String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
									if ((StringUtils.isNotEmpty(leave_type_short) || day.equalsIgnoreCase("Sunday")) &&
											(durationTotalMinutes >= targetTotalMinutesForCompOff) &&
											(
													leave_type_short != null && leave_type_short.equalsIgnoreCase("GH") ||
													leave_type_short != null && leave_type_short.equalsIgnoreCase("DH") ||
													day.equalsIgnoreCase("Sunday")
											)
									)  {
										biometricAttendance.setCompoffStatus(true);
									}
								}

								biometricAttendanceRepository.save(biometricAttendance);

							}
						}
					} catch (Exception e) {
						System.out.println("Exception occur " + e.getMessage());
					}
				});

			}
		} catch (Exception e) {
			log.error("Exception occur " + e.getMessage());
		}

	}

	private boolean checkReleivingDate(String empcode, String formattedDate) {
		try {
			String dateOfRelieving = resignationRepository.getReleivingDateByEmpCode(empcode);
			if (StringUtils.isNotEmpty(dateOfRelieving)) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date dateOfRelieve = new SimpleDateFormat("dd-MM-yyyy").parse(dateOfRelieving);
				Date currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
				String date = simpleDateFormat.format(currentDate);
				Date cDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
				Calendar cl = Calendar.getInstance();
				cl.setTime(dateOfRelieve);
				cl.add(Calendar.DAY_OF_MONTH, 1);
				Date incrementedRelieveDate = cl.getTime();
				boolean result = !cDate.before(cl.getTime());
				return !cDate.before(cl.getTime());
			}
			return false;
		} catch (ParseException e) {
			return false;
		}

	}
}
