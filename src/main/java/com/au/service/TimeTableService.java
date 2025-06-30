package com.au.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.au.dto.FacultyDetailsForTimeTable;
import com.au.dto.JwtDetails;
import com.au.dto.StudentDetailsResponse;
import com.au.dto.TimeTableDto;
import com.au.exception.ResourceNotFoundException;
import com.au.model.EmployeeDetails;
import com.au.model.EventCreation;
import com.au.model.ProgramSpecilization;
import com.au.model.ReportingStudents;
import com.au.model.TimeTable;
import com.au.model.TimeTableEmployee;
import com.au.model.TimeTablePrograms;
import com.au.repository.Academic_year_repository;
import com.au.repository.BatchAssignmentRepository;
import com.au.repository.BatchProgramAssignmentRepository;
import com.au.repository.ClassCommencementDetailsRepository;
import com.au.repository.EmployeeDetailsRepository;
import com.au.repository.InfrastructureRoomsRepository;
import com.au.repository.InternalStudentAssignmentRepository;
import com.au.repository.ProgramAssigmentRepository;
import com.au.repository.ProgramSpecilizationRepository;
import com.au.repository.ReportingStudentsRepository;
import com.au.repository.SectionAssignmentRepository;
import com.au.repository.StudentDetailsRepository;
import com.au.repository.TimeSlotsRepository;
import com.au.repository.TimeTableEmployeeRepository;
import com.au.repository.TimeTableProgramsRepository;
import com.au.repository.TimeTableRepository;
import com.au.repository.UserAuthenticationRepository;
import com.au.response.ResponseHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.au.model.TimeSlots;

import com.au.model.UserAuthentication;

@Service
public class TimeTableService {

	@Autowired
	private TimeTableRepository timeTableRepository;

	@Autowired
	private TimeSlotsRepository timeSlotsRepository;

	@Autowired
	private EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	private BatchAssignmentRepository batchAssignmentRepository;

	@Autowired
	private SectionAssignmentRepository sectionAssignmentRepository;

	@Autowired
	private TimeTableEmployeeRepository timeTableEmployeeRepository;

	@Autowired
	private ProgramAssigmentRepository programAssigmentRepository;

	@Autowired
	private BatchProgramAssignmentRepository batchProgramAssignmentRepository;

	@Autowired
	private TimeTableProgramsRepository timeTableProgramsRepository;

	@Autowired
	private StudentDetailsRepository studentDetailsRepository;

	@Autowired
	private Academic_year_repository academicYearRepository;

	@Autowired
	private ReportingStudentsRepository reportingStudentsRepository;

	@Autowired
	private UserAuthenticationRepository userAuthenticationRepository;

	@Autowired
	public InfrastructureRoomsRepository infrastructureRoomsRepository;

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private EventCreationService eventCreationService;

	@Autowired
	private HolidayCalenderService holidayCalenderService;

	@Autowired
	private InternalStudentAssignmentRepository internalStudentAssignmentRepository;

	@Autowired
	public ClassCommencementDetailsRepository classCommencementDetailsRepository;

	@Autowired
	private ProgramSpecilizationRepository programSpecilizationRepository;

	@Autowired
	private BatchProgramAssignmentRepository bpa_repo;

	@Autowired
	private SectionAssignmentRepository sa_repo;
	
	private final ModelMapper modelMapper=new ModelMapper();

	public List<TimeTable> listAll() {
		return timeTableRepository.findAll1();
	}

	public ResponseEntity<Object> listAll1(Pageable pageable, Object keyword) {
		List<Map<String, Object>> response1 = timeTableRepository.findAll2(pageable, keyword);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response1);
	}

	public ResponseEntity<Object> listAll2(Pageable pageable) {
		List<Map<String, Object>> response = timeTableRepository.findAll3(pageable);
		return ResponseHandler.generateResponse(true, HttpStatus.OK, response);
	}

	Date date1;
	Date date2;
	Date date3;
	Date date4;
	TimeTable created_time_Table = new TimeTable();

	public List<TimeTable> saveTimeTableOnSec(@Valid TimeTableDto r) {
		Date date = new Date();
		Integer countOfData = 0;
		date = r.getFrom_date();
		if (r.getSection_assignment_id() != null) {
			countOfData = timeTableEmployeeRepository.getCount1(r.getSection_assignment_id(), r.getTime_slots_id(),
					date);
		} else {
			countOfData = timeTableEmployeeRepository.getCountByBatchAssignment(r.getBatch_assignment_id(),
					r.getTime_slots_id(), date);
		}

		List<TimeTable> tt_list = new ArrayList<TimeTable>();
		if (timeTableEmployeeRepository.getCount(r.getEmp_id(), r.getTime_slots_id(), date) >= 1) {
			throw new RuntimeException("Faculty already assigned for the given Date and Time !");
		} else if (countOfData >= 1) {
			throw new RuntimeException("Faculty already assigned for the given Date, Time and Section OR Batch!");
		} else if (countOfData >= 1) {
			throw new RuntimeException("Faculty already assigned for the given Date, Time and Section OR Batch!");
		} else {

			List<TimeSlots> ts = timeSlotsRepository.getTimeSlotsAssignedForTheDay(r.getFrom_date(), r.getEmp_id());
			List<TimeSlots> tr = timeSlotsRepository.getTimeSlotsOfRoomsAssignedForTheDay(r.getFrom_date(),
					r.getRoom_id());
//			Integer ts2 = timeSlotsRepository.getTimeSlotsAssignedForTheDay1(r.getFrom_date(), r.getEmp_id());

			// TimeTable created_time_Table = new TimeTable();

			if (!ts.isEmpty()) {
				TimeSlots ts1 = timeSlotsRepository.getOne(r.getTime_slots_id());

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");

				// Parsing the Time Period
				try {
					date1 = simpleDateFormat.parse(ts1.getStarting_time());
					date2 = simpleDateFormat.parse(ts1.getEnding_time());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// ts1.getStarting_time_for_fornted().getHours();
				ts.stream().forEach(tss -> {

					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("h:mm a");

					// Parsing the Time Period
					try {
						date3 = simpleDateFormat1.parse(tss.getStarting_time());
						date4 = simpleDateFormat1.parse(tss.getEnding_time());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					System.out.println("");
					Integer count = 0;
					if ((date2.getTime() <= date3.getTime())) {
						System.out.println("doing nothing! just passing the control");
						count = 0;
					} else if ((date1.getTime() >= date4.getTime())) {
						System.out.println("doing nothing! just passing the control");
						count = 0;
					} else {
						throw new RuntimeException("Faculty already assigned between the time slot!");
					}

				});

				if (!tr.isEmpty()) {
					TimeSlots tr1 = timeSlotsRepository.getOne(r.getTime_slots_id());

					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("h:mm a");

					// Parsing the Time Period
					try {
						date1 = simpleDateFormat1.parse(ts1.getStarting_time());
						date2 = simpleDateFormat1.parse(ts1.getEnding_time());
					} catch (ParseException e) {
						e.printStackTrace();
					}

					// ts1.getStarting_time_for_fornted().getHours();
					ts.stream().forEach(tss -> {

						SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("h:mm a");

						// Parsing the Time Period
						try {
							date3 = simpleDateFormat2.parse(tss.getStarting_time());
							date4 = simpleDateFormat2.parse(tss.getEnding_time());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Integer count = 0;
						if ((date2.getTime() <= date3.getTime())) {
							System.out.println("doing nothing! just passing the control");
							count = 0;
						} else if ((date1.getTime() >= date4.getTime())) {
							System.out.println("doing nothing! just passing the control");
							count = 0;
						} else {
							throw new RuntimeException("Room overlapping the already assigned time slot!");
						}

					});
				}
				r.setSelected_date(r.getFrom_date());
				created_time_Table = saveTimeTableData(r);
				tt_list.add(created_time_Table);
			} else {
				r.setSelected_date(r.getFrom_date());
				created_time_Table = saveTimeTableData(r);
				tt_list.add(created_time_Table);
			}

			// created_time_Table=null;
			return timeTableRepository.saveAll(tt_list);
		}
	}

	public TimeTable saveTimeTableData(@Valid TimeTableDto r) {
		// r.getEmp_id().stream().forEach(emp -> {Faculty already assigned for the given
		// Date, Time and Section !
		TimeTable tt = new TimeTable();
		System.out.println("Given WeekDay Matched.. Insert Data..!! ");
		tt.setAc_year_id(r.getAc_year_id());
		tt.setSelected_date(r.getSelected_date());
		tt.setActive(r.getActive());
		tt.setBlock_id(r.getBlock_id());
		tt.setClass_interval(r.getClass_interval());
		tt.setColumn_of_date_time_for_fronted_use(r.getColumn_of_date_time_for_fronted_use());
		tt.setCreated_by(r.getCreated_by());
		tt.setCreated_date(r.getCreated_date());
		tt.setCreated_username(r.getCreated_username());
		tt.setCurrent_sem(r.getCurrent_sem());
		tt.setCurrent_year(r.getCurrent_year());
		tt.setFloor_id(r.getFloor_id());
		tt.setFrom_date(r.getFrom_date());
		tt.setIntervals(r.getIntervals());
		tt.setInterval_type_id(r.getInterval_type_id());
		tt.setModified_by(r.getModified_by());
		tt.setModified_date(r.getModified_date());
		tt.setModified_username(r.getModified_username());
		tt.setProgram_assignment_id(r.getProgram_assignment_id());
		tt.setProgram_id(r.getProgram_id());
		tt.setProgram_specialization_id(r.getProgram_specialization_id());
		tt.setRoom_id(r.getRoom_id());
		tt.setSchool_id(r.getSchool_id());
		tt.setTime_slots_id(r.getTime_slots_id());
		tt.setTo_date(r.getTo_date());
		tt.setWeek_day(r.getWeek_day());
		tt.setYear(r.getYear());
		tt.setSubject_assignment_id(r.getSubject_assignment_id());
		tt.setSection_assignment_id(r.getSection_assignment_id());
		tt.setBatch_assignment_id(r.getBatch_assignment_id());
		tt.setIs_online(r.getIs_online());
		tt.setFromDate_for_fronted_use(r.getFromDate_for_fronted_use());
		tt.setToDate_for_fronted_use(r.getToDate_for_fronted_use());
		// timeTableRepository.save(tt);
		//
		return tt;
	}

	public void updateTimetableEmployee(List<TimeTable> time_table, TimeTableDto r) {
		r.getEmp_id().forEach(ttbl1 -> {
			time_table.stream().forEach(ttbl -> {
				TimeTableEmployee tte = new TimeTableEmployee();
				tte.setActive(ttbl.getActive());
				tte.setCreated_by(ttbl.getCreated_by());
				tte.setCreated_date(ttbl.getCreated_date());
				tte.setCreated_username(ttbl.getCreated_username());
				tte.setEmp_id(ttbl1);
				tte.setModified_by(ttbl.getModified_by());
				tte.setModified_date(ttbl.getModified_date());
				tte.setModified_username(ttbl.getModified_username());
				tte.setTime_table_id(ttbl.getTime_table_id());
				tte.setSection_assignment_id(ttbl.getSection_assignment_id());
				tte.setBatch_assignment_id(ttbl.getBatch_assignment_id());
				tte.setTime_slots_id(ttbl.getTime_slots_id());
				tte.setSelected_date(ttbl.getSelected_date());
				tte.setSubject_assignment_id(ttbl.getSubject_assignment_id());
				tte.setProgram_assignment_id(ttbl.getProgram_assignment_id());
				timeTableEmployeeRepository.save(tte);
			});
		});

	}

	public void updateTimetablePrograms(List<TimeTable> time_table, TimeTableDto r) {

		List<Integer> program_ids = batchProgramAssignmentRepository
				.getProgramIdFromBatchProgramAssignment(r.getBatch_assignment_id());
		System.out.println("((((((((((((((((((((())))))))))))))))))))) " + program_ids);
		program_ids.stream().forEach(pi -> {
			TimeTablePrograms ttp = new TimeTablePrograms();
			ttp.setTime_table_id(time_table.get(0).getTime_table_id());
			System.out.println("[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]] " + time_table.get(0).getTime_table_id());
			ttp.setActive(time_table.get(0).getActive());
			ttp.setCreated_by(time_table.get(0).getCreated_by());
			ttp.setCreated_date(time_table.get(0).getCreated_date());
			ttp.setCreated_username(time_table.get(0).getCreated_username());
			ttp.setModified_by(time_table.get(0).getModified_by());
			ttp.setModified_date(time_table.get(0).getModified_date());
			ttp.setModified_username(time_table.get(0).getModified_username());
			ttp.setProgram_id(pi);

			timeTableProgramsRepository.save(ttp);
		});
	}

	Date date;

	public List<TimeTable> saveMultipleTimeTableOnSec(@Valid TimeTableDto s) {
		String start_time = null;
		String end_time = null;

		List<TimeTable> tt_list = new ArrayList<TimeTable>();

		LocalDate fromDate = LocalDate.fromDateFields(s.getFrom_date());
		LocalDate toDate = LocalDate.fromDateFields(s.getTo_date());
		System.out.println("000000000000000000000 " + fromDate);
		// get no of days between given dates
		long daysDiff = Days.daysBetween(fromDate, toDate).getDays();
		System.out.println("(((((((((((((((((()))))))))))))))))) " + daysDiff);

		date = s.getFrom_date();
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  " + s.getFrom_date());

		System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB " + date);
		for (int i = 0; i <= daysDiff; i++) {
			System.out.println("++++++++++++++++++++++++++++++++ " + date);
			// get DayName by date
			Format f = new SimpleDateFormat("EEEE");
			String dayName = f.format(date);
			if (s.getWeek_day().equalsIgnoreCase(dayName)) {
//					s.getEmp_id().stream().forEach(emp -> {
				SimpleDateFormat twelweHourFormat = new SimpleDateFormat("hh:mm a");
				SimpleDateFormat twentyFourHourFormat = new SimpleDateFormat("HH:mm");
				TimeSlots ts1 = timeSlotsRepository.getOne(s.getTime_slots_id());
				try {
						start_time = twentyFourHourFormat.format(twelweHourFormat.parse(ts1.getStarting_time()));
						end_time = twentyFourHourFormat.format(twelweHourFormat.parse(ts1.getEnding_time()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				

				if (timeTableEmployeeRepository.getEmployeesCountForOverlappingTimeSlot(s.getEmp_id(), date, start_time, end_time) >= 1) {
					throw new RuntimeException("Faculty already assigned for the given Date and Time !");
				} 
				
				else if (s.getSection_assignment_id() != null && timeTableEmployeeRepository.getSectionCountForOverlappingTimeSlot(s.getSection_assignment_id(), date, 
				start_time, end_time) >= 1) {
					throw new RuntimeException("Section is already assigned a course during this time slot !");
				}

				else if (timeTableEmployeeRepository.getRoomCountForSameDateAndTime(s.getRoom_id(),
						 date, start_time, end_time) >= 1) {
					throw new RuntimeException("Room is busy during this time slot !");
				}

				else if (s.getBatch_assignment_id() != null && timeTableEmployeeRepository.getBatchCountForOverlappingTimeSlot(s.getBatch_assignment_id(),
						 date, start_time, end_time) >= 1) {
					throw new RuntimeException("Batch is already assigned a course during this time slot !");
				}
				// List<TimeSlots> ts = timeSlotsRepository.getTimeSlotsAssignedForTheDay(date, s.getEmp_id());
				// Integer ts2 = timeSlotsRepository.getTimeSlotsAssignedForTheDay1(date, s.getEmp_id());
				// System.out.println("{{{{{{{{{{{{{{{{1111111111}}}}}}}}}}}}}}}} " + ts.size());

				// TimeTable created_time_Table = new TimeTable();

// 				if (!ts.isEmpty()) {

// 					// System.out.println("{{{{{{{{{{{{{{{{GGGGGGGGGGGGGGGGGG}}}}}}}}}}}}}}}} " + ts2);
// 					System.out.println("{{{{{{{{{{{{{{{{@@@@@@@@@@@@@@W}}}}}}}}}}}}}}}} " + s.getTime_slots_id());

// 					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");

// 					// Parsing the Time Period
// 					try {
// 						date1 = simpleDateFormat.parse(ts1.getStarting_time());
// 						date2 = simpleDateFormat.parse(ts1.getEnding_time());
// 					} catch (ParseException e) {
// 						// TODO Auto-generated catch block
// 						e.printStackTrace();
// 					}

// 					System.out.println("START ID:" + ts1.getTime_slots_id() + " Time = " + date1);
// 					System.out.println("START ID:" + ts1.getTime_slots_id() + " Time = " + date1.getTime());
// 					System.out.println("EENDDD ID:" + ts1.getTime_slots_id() + " Time = " + date2);
// 					System.out.println("EENDDD  ID:" + ts1.getTime_slots_id() + " Time = " + date2.getTime());

// 					System.out.println("{{{{{{{{{{{{{{{{1}}}}}}}}}}}}}}}} " + ts1.getStarting_time());
// 					System.out.println("{{{{{{{{{{{{{{{{++++++++++++}}}}}}}}}}}}}}}} "
// 							+ ts1.getStarting_time_for_fornted().getTime());
// 					System.out.println("{{{{{{{{{{{{{{{{1}}}}}}}}}}}}}}}} " + s.getFrom_date());

// 					// ts1.getStarting_time_for_fornted().getHours();
// 					ts.stream().forEach(tss -> {

// 						SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("h:mm a");

// 						// Parsing the Time Period
// 						try {
// 							date3 = simpleDateFormat1.parse(tss.getStarting_time());
// 							date4 = simpleDateFormat1.parse(tss.getEnding_time());
// 						} catch (ParseException e) {
// 							// TODO Auto-generated catch block
// 							e.printStackTrace();
// 						}

// 						System.out.println("START ID:" + tss.getTime_slots_id() + " Time = " + date3);
// 						System.out.println("START ID:" + tss.getTime_slots_id() + " Time = " + date3.getTime());
// 						System.out.println("EENDDD ID:" + tss.getTime_slots_id() + " Time = " + date4);
// 						System.out.println("EENDDD ID:" + tss.getTime_slots_id() + " Time = " + date4.getTime());

// //			If(frontedSendingEndTime <= AlreadyCreatedStartingTime)
// //			{
// //				//create
// //			}else if(frontedSendingStartingTime >= AlreadyCreatedEndingTime){
// //			  //create
// //			}else{
// //			   //error
// //			}
// //						TimeTable created_time_Table  = new TimeTable();
// 						System.out.println("");
// 						Integer count = 0;
// 						if ((date2.getTime() <= date3.getTime())) {
// 							System.out.println("doing nothing! just passing the control");
// 							count = 0;
// 						} else if ((date1.getTime() >= date4.getTime())) {
// 							System.out.println("doing nothing! just passing the control");
// 							count = 0;
// 						} else {
// 							throw new RuntimeException("Faculty ovelapping the already assigned time slot!");
// 						}

// 					});
// 					s.setSelected_date(date);
// 					System.out.println("--- : "+date);
// 					System.out.println("+++ : "+s.getSelected_date());
// 					created_time_Table = saveTimeTableData(s);
// 					tt_list.add(created_time_Table);
// 				} else {
					s.setSelected_date(date);
					System.out.println("--- : "+date);
					System.out.println("+++ : "+s.getSelected_date());
					created_time_Table = saveTimeTableData(s);
					tt_list.add(created_time_Table);
				// }

				// created_time_Table=null;
				timeTableRepository.saveAll(tt_list);

//					});
			}

			// increment date by 1
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			date = c.getTime();

		}
		return tt_list;
	}

//	public List<TimeTable> saveMultipleTimeTableOnSec(@Valid TimeTableDto s) {
//		Date date=new Date();
//		List<TimeTable> tt_list = new ArrayList<TimeTable>();
//		Integer countOfData=0;
//		LocalDate fromDate = LocalDate.fromDateFields(s.getFrom_date());
//		LocalDate toDate = LocalDate.fromDateFields(s.getTo_date());
//			//get no of days between given dates
//			long daysDiff = Days.daysBetween(fromDate, toDate).getDays();
//			
//			 date = s.getFrom_date();
//			
//			for(int i = 0 ; i<=daysDiff ; i++) {
//				//get DayName by date
//				Format f = new SimpleDateFormat("EEEE");  
//				String dayName = f.format(date); 
//				if(s.getWeek_day().equalsIgnoreCase(dayName)) {
//					 
//					if(s.getSection_assignment_id() != null) {
//						countOfData=timeTableEmployeeRepository.getCount3(s.getSection_assignment_id(),s.getTime_slots_id(),date);
//					}else {
//						countOfData=timeTableEmployeeRepository.getCountByBatchAssignment(s.getBatch_assignment_id(),s.getTime_slots_id(),date);
//					}
//					
//					if(timeTableEmployeeRepository.getCount2(s.getEmp_id(),s.getTime_slots_id(),date) >=1) {
//						throw new RuntimeException("Faculty already assigned for the given Date and Time !");
//					} else if(countOfData >=1) {
//						throw new RuntimeException("Faculty already assigned for the given Date, Time and Section !");
//					} else {
//						
//						List<TimeSlots> ts = timeSlotsRepository.getTimeSlotsAssignedForTheDay(s.getFrom_date(), s.getEmp_id());
////						Integer ts2 = timeSlotsRepository.getTimeSlotsAssignedForTheDay1(s.getFrom_date(), s.getEmp_id());
//						List<TimeSlots> tr = timeSlotsRepository.getTimeSlotsOfRoomsAssignedForTheDay(s.getFrom_date(), s.getRoom_id());
//
//						//		TimeTable created_time_Table  = new TimeTable();
//
//						if(!ts.isEmpty()) {
//							TimeSlots ts1 = timeSlotsRepository.getOne(s.getTime_slots_id());
//
//
//							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
//
//							// Parsing the Time Period
//							try {
//								date1 = simpleDateFormat.parse(ts1.getStarting_time());
//								date2 = simpleDateFormat.parse(ts1.getEnding_time());
//							} catch (ParseException e) {
//								e.printStackTrace();
//							}
//
//							//ts1.getStarting_time_for_fornted().getHours();
//							ts.stream().forEach(tss -> {
//
//								SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("h:mm a");
//
//								// Parsing the Time Period
//								try {
//									date3 = simpleDateFormat1.parse(tss.getStarting_time());
//									date4 = simpleDateFormat1.parse(tss.getEnding_time());
//								} catch (ParseException e) {
//									e.printStackTrace();
//								}
//								System.out.println("");
//								Integer count = 0;
//								if((date2.getTime() <= date3.getTime())) {
//									System.out.println("doing nothing! just passing the control");
//									count=0;
//								} else if((date1.getTime() >= date4.getTime())) {
//									System.out.println("doing nothing! just passing the control");
//									count=0;
//								} else {
//									throw new RuntimeException("Faculty ovelapping the already assigned time slot!");
//								}
//
//							});
//							if(!tr.isEmpty()) {
//								TimeSlots tr1 = timeSlotsRepository.getOne(s.getTime_slots_id());
//
//
//								SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("h:mm a");
//
//								// Parsing the Time Period
//								try {
//									date1 = simpleDateFormat1.parse(ts1.getStarting_time());
//									date2 = simpleDateFormat1.parse(ts1.getEnding_time());
//								} catch (ParseException e) {
//									e.printStackTrace();
//								}
//
//								//ts1.getStarting_time_for_fornted().getHours();
//								ts.stream().forEach(tss -> {
//
//									SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("h:mm a");
//
//									// Parsing the Time Period
//									try {
//										date3 = simpleDateFormat2.parse(tss.getStarting_time());
//										date4 = simpleDateFormat2.parse(tss.getEnding_time());
//									} catch (ParseException e) {
//										e.printStackTrace();
//									}
//									System.out.println("");
//									Integer count = 0;
//									if((date2.getTime() <= date3.getTime())) {
//										System.out.println("doing nothing! just passing the control");
//										count=0;
//									} else if((date1.getTime() >= date4.getTime())) {
//										System.out.println("doing nothing! just passing the control");
//										count=0;
//									} else {
//										throw new RuntimeException("Room ovelapping the already assigned time slot!");
//									}
//
//								});
//							}
//							s.setSelected_date(s.getFrom_date());
//							created_time_Table  = saveTimeTableData(s);
//							tt_list.add(created_time_Table);
//						} else {
//							s.setSelected_date(s.getFrom_date());
//							created_time_Table  = saveTimeTableData(s);
//							tt_list.add(created_time_Table);
//						}
//
//						//	created_time_Table=null;
//						timeTableRepository.saveAll(tt_list);
//					}
//					
//				}
//				//increment date by 1
//				Calendar c = Calendar.getInstance();
//				c.setTime(date);
//				c.add(Calendar.DATE, 1);
//				date = c.getTime();
//
//			
//			}
//		return tt_list;
//	}

	public TimeTable get(Integer id) {
		return timeTableRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TimeTable Not Found:" + id));
	}

	public void delete(List<Integer> time_table_id) {

		timeTableRepository.updateTimeTable(time_table_id);
	}

	public void delete1(List<Integer> time_table_id) {

		timeTableRepository.updateTimeTable1(time_table_id);

	}

	public List<Map<String, Object>> getTimeSlots(Integer school_id) {
		return timeSlotsRepository.getTimeSlots(school_id);
	}

	public List<Map<String, Object>> getTimeSlotsForInternals(Integer school_id) {
		return timeSlotsRepository.getTimeSlotsForInternals(school_id);
	}

	public List<Map<String, Object>> getAllEmployeesForTimeTable(Date date1, Date date2, Integer time_slots_id,
			String day) {
		return employeeDetailsRepository.getAllEmployeesForTimeTable(date1, date2, time_slots_id, day);
	}

	public List<Map<String, Object>> getAllBatchesForTimeTableOnSem(Integer school_id, Integer ac_year_id,
			Integer current_sem, Integer interval_type_id) {
		return batchAssignmentRepository.getAllBatchesForTimeTableOnSem(school_id, ac_year_id, current_sem,
				interval_type_id);
	}

	public List<Map<String, Object>> getAllBatchesForTimeTableOnYear(Integer school_id, Integer ac_year_id,
			Integer current_year, Integer interval_type_id) {
		return batchAssignmentRepository.getAllBatchesForTimeTableOnYear(school_id, ac_year_id, current_year,
				interval_type_id);
	}

	public List<HashMap<String, Object>> getAllSectionsForTimeTable(Integer school_id,
			Integer program_specialization_id, Integer ac_year_id, Integer current_year) {
		return sectionAssignmentRepository.getAllSectionsForTimeTable(school_id, program_specialization_id, ac_year_id,
				current_year);
	}

	public TimeTable saveTimeTable(TimeTable s) {
		return timeTableRepository.save(s);

	}

	public List<Map<String, Object>> getEmployeeTimetableSchedule(List<Integer> emp_ids, Date from_date, Date to_date,
			String day) {
		return timeTableRepository.getEmployeeTimetableSchedule(emp_ids, from_date, to_date, day);
	}

	public List<Map<String, Object>> fetchAllTimeTableDetails(Integer ac_year_id) {
		return timeTableRepository.fetchAllTimeTableDetails(ac_year_id);
	}

	public ResponseEntity<Object> getAllDataFilteredByKeyword(Pageable pageable, Object keyword, Integer ac_year_id, Integer current_sem, Integer current_year) {
		Page<Map<String, Object>> Notifications_filtered_response = timeTableRepository
				.getAllDataFilteredByKeyword(pageable, keyword, ac_year_id,current_sem,current_year);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_filtered_response);
	}

	public ResponseEntity<Object> getAllSortedData(Pageable pageable1, Integer ac_year_id, Integer current_sem, Integer current_year) {
		Page<Map<String, Object>> Notifications_sorted_response = timeTableRepository.getAllSortedData(pageable1,
				ac_year_id,current_sem,current_year);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_sorted_response);
	}

	public List<Map<String, Object>> fetchAllTimeTableDetailsBasedOnAcYearIdAndUserId(Integer ac_year_id,
			Integer user_id) {
		return timeTableRepository.fetchAllTimeTableDetailsBasedOnAcYearIdAndUserId(ac_year_id, user_id);
	}

	public List<Map<String, Object>> fetchAllTimeTableDetailsBasedOnAcYearIdAndDeptId(Integer ac_year_id,
			Integer dept_id) {
		return timeTableRepository.fetchAllTimeTableDetailsBasedOnAcYearIdAndDeptId(ac_year_id, dept_id);
	}

	public List<Map<String, Object>> fetchAllTimeTableDetailsBasedOnAcYearIdAndSchoolId(Integer ac_year_id,
			Integer school_id) {
		return timeTableRepository.fetchAllTimeTableDetailsBasedOnAcYearIdAndSchoolId(ac_year_id, school_id);
	}

	public List<Map<String, Object>> fetchAllTimeTableDetailsForIndex(Integer ac_year_id) {
		return timeTableRepository.fetchAllTimeTableDetailsForIndex(ac_year_id);
	}

	public Integer fetchYearSemForBatchTimeTable(Integer ac_year_id, Integer school_id) {
		return programAssigmentRepository.fetchYearSemForBatchTimeTable(ac_year_id, school_id);
	}

	public List<Map<String, Object>> fetchAllBatchTimeTableDetailsForIndex(Integer ac_year_id) {
		return timeTableRepository.fetchAllBatchTimeTableDetailsForIndex(ac_year_id);
	}

	public ResponseEntity<Object> timeTableDetailsOfStudentOrEmployeeForMobile(Integer student_id, Integer year,
			Integer month, Integer employee_id, String jwtToken)
			throws ParseException, JsonParseException, JsonMappingException, IOException {

		JwtDetails jwtDetails = jwtTokenService.callJwtToken(jwtToken);
		if (year == null && month == null) {
			year = LocalDate.now().getYear();
			year = LocalDate.now().getMonthOfYear();
		}
		if (jwtDetails.getUserType().equalsIgnoreCase("Student")) {
			if (ObjectUtils.isNotEmpty(student_id)) {
				List<List<Map<String, Object>>> studentTimeTable = timeTableDetailsOfStudentMobile(student_id, year,
						month);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, studentTimeTable);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.OK,
						"Mismatch Combination Of Token And ID, Passing Token Of Student With Employee Id");
			}

		} else {
			if (ObjectUtils.isNotEmpty(employee_id)) {
				List<List<Map<String, Object>>> employeeTimeTableLeaveAndHolidayData = timeTableDetailsOfEmployeeForMobile(
						year, month, employee_id);
				return ResponseHandler.generateResponse(true, HttpStatus.OK, employeeTimeTableLeaveAndHolidayData);
			} else {
				return ResponseHandler.generateResponse(true, HttpStatus.OK,
						"Mismatch Combination Of Token And ID, Passing Token Of Employee With Student Id");
			}
		}
	}

	public List<List<Map<String, Object>>> timeTableDetailsOfStudentMobile(Integer student_id, Integer year,
			Integer month) {
		List<List<Map<String, Object>>> finalResponeofStudentTimeTable = new ArrayList<List<Map<String, Object>>>();
		Integer schoolId = studentDetailsRepository.schoolIdOfStudentByStudentId(student_id);
		List<StudentDetailsResponse> studentDetails = studentDetailsRepository
				.getStudentDetailsFromBatchAssignment(Stream.of(student_id).collect(Collectors.toList()));
		String program_type = studentDetailsRepository.getProgramTypeOfStudent(student_id);

		Integer academic_year_id = academicYearRepository.getCurrentAcademicYearId();
//		System.out.println("111111111111111111111111111111111111111111111111111111 " + academic_year_id);
		ReportingStudents reporting_student = reportingStudentsRepository
				.getDetailsOfReportingStudentsByStudentId(student_id);

		List<Map<String, Object>> classCommencementDetails = new ArrayList<>();

		List<Integer> section_assignment_id;
		List<Integer> batch_assignment_id;
		if (program_type.equalsIgnoreCase("Semester")) {
			section_assignment_id = sectionAssignmentRepository.getSectionAssignmentId(student_id,
					reporting_student.getCurrent_sem());
			
//			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " +section_assignment_id);

			batch_assignment_id = batchAssignmentRepository.getBatchAssignmentIdOnSem(student_id,
					reporting_student.getCurrent_sem());
			
//			System.out.println("################################################################## " +batch_assignment_id);
			classCommencementDetails = classCommencementDetailsRepository
					.getClassCommencementDetailsForStudentTimetable(schoolId,
							studentDetails.get(0).getProgram_specialization_id(), reporting_student.getCurrent_sem());
		} else {
			section_assignment_id = sectionAssignmentRepository.getSectionAssignmentId(student_id,
					reporting_student.getCurrent_year());

			batch_assignment_id = batchAssignmentRepository.getBatchAssignmentIdOnYear(student_id,
					reporting_student.getCurrent_year());
			classCommencementDetails = classCommencementDetailsRepository
					.getClassCommencementDetailsForStudentTimetable(schoolId,
							studentDetails.get(0).getProgram_specialization_id(), reporting_student.getCurrent_year());

		}

		LocalDate startDate;
		LocalDate endDate;
		if (year != null && month != null) {
			startDate = new LocalDate(year, month, 1);
			endDate = new LocalDate(year, month, startDate.dayOfMonth().withMaximumValue().getDayOfMonth());
		} else {
			startDate = LocalDate.now();
			endDate = new LocalDate(LocalDate.now().getYear(), LocalDate.now().getMonthOfYear(),
					LocalDate.now().dayOfMonth().withMaximumValue().getDayOfMonth());
		}
		
		List<Map<String, Object>> studentTimeTable = timeTableRepository.timeTableDetailsOfStudentWithAttendance1(
				section_assignment_id, batch_assignment_id, month ,year, student_id);

		
		AtomicReference<LocalDate> currentDate = new AtomicReference<>(startDate);
		while (!currentDate.get().isAfter(endDate)) {
			List<Map<String, Object>> studentTimeTableResponse = new ArrayList<>();
			Date date = currentDate.get().toDate();
		
		List<Map<String, Object>> declaredAndGeneralHoliday = declaredHolidayAndGeneralHolidayDateWise(schoolId,
					currentDate.get());
			studentTimeTableResponse.addAll(declaredAndGeneralHoliday);

			List<Map<String, Object>> filteredClassCommencementDetails = classCommencementDetails.stream()
					.filter(e -> (new DateTime(e.get("from_date"))).toLocalDate().equals(currentDate.get()))
					.collect(Collectors.toList());

			studentTimeTableResponse.addAll(filteredClassCommencementDetails);
			List<Map<String, Object>> dateFilteredTimeTable = studentTimeTable.stream()
					.filter(e -> (new DateTime(e.get("selected_date"))).toLocalDate().equals(currentDate.get()))
					.collect(Collectors.toList());
			if (ObjectUtils.isNotEmpty(dateFilteredTimeTable)) {
				studentTimeTableResponse.addAll(dateFilteredTimeTable);
			}

			currentDate.set(currentDate.get().plusDays(1));
			finalResponeofStudentTimeTable.add(studentTimeTableResponse);
		}

		return finalResponeofStudentTimeTable;
	}

	public List<Map<String, Object>> declaredHolidayAndGeneralHolidayDateWise(Integer school_id, LocalDate date) {
		List<Map<String, Object>> declaredHoliday = holidayCalenderService.listAllHolidayCalenderData(school_id);
		List<Map<String, Object>> generalHoliday = holidayCalenderService
				.listAllHolidayCalenderDataForStudentWithOutSchoolId();
		List<Map<String, Object>> dateWiseData = new ArrayList<>();
		List<Map<String, Object>> dateFilteredDeclaredHoliday = declaredHoliday.stream()
				.filter(e -> (new DateTime(e.get("from_date"))).toLocalDate().equals(date))
				.collect(Collectors.toList());
		if (ObjectUtils.isNotEmpty(dateFilteredDeclaredHoliday)) {
			dateWiseData.addAll(dateFilteredDeclaredHoliday);
		}

		List<Map<String, Object>> dateFilteredGeneralHoliday = generalHoliday.stream()
				.filter(e -> (new DateTime(e.get("from_date"))).toLocalDate().equals(date))
				.collect(Collectors.toList());
		if (ObjectUtils.isNotEmpty(dateFilteredGeneralHoliday)) {
			dateWiseData.addAll(dateFilteredGeneralHoliday);
		}
		return dateWiseData;
	}

	public List<List<Map<String, Object>>> timeTableDetailsOfEmployeeForMobile(Integer year, Integer month,
			Integer employee_id) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTimeFormatter formatterForDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		Optional<EmployeeDetails> employeeDetails = employeeDetailsRepository.findById(employee_id);
		List<List<Map<String, Object>>> finalResponeofTimeTable = new ArrayList<List<Map<String, Object>>>();
		List<EventCreation> eventCreation = eventCreationService.listAll();
		List<Map<String, Object>> declaredHoliday = holidayCalenderService.listAllHolidayCalenderDataWith(
				employeeDetails.get().getSchool_id(), employeeDetails.get().getDept_id(),
				employeeDetails.get().getJob_type_id());
		List<Map<String, Object>> generalHoliday = holidayCalenderService.listAllHolidayCalenderDataWithOutSchoolId();
		List<Map<String, Object>> internalStudentAssignmentDetails = internalStudentAssignmentRepository
				.InternalStudentAssignmentDetailsByEmployeeId(employee_id);
		List<Map<String, Object>> timeTableOfEmployee = fetchTimeTableDetailsByEmployeeId(employee_id);
		System.out.println("Employee ID :" + employee_id);
		System.out.println("All Data :" + timeTableOfEmployee.size());
		LocalDate startDate;
		LocalDate endDate;
		if (year != null && month != null) {
			startDate = new LocalDate(year, month, 1);
			endDate = new LocalDate(year, month, startDate.dayOfMonth().withMaximumValue().getDayOfMonth());
		} else {
			startDate = LocalDate.now();
			endDate = new LocalDate(LocalDate.now().getYear(), LocalDate.now().getMonthOfYear(),
					LocalDate.now().dayOfMonth().withMaximumValue().getDayOfMonth());
		}
		AtomicReference<LocalDate> currentDate = new AtomicReference<>(startDate);
		while (!currentDate.get().isAfter(endDate)) {
			List<Map<String, Object>> employeeTimeTableResponse = new ArrayList<>();

			LocalDate date = currentDate.get();
			System.out.println("DATE DATE " + date);
	        DateTimeFormatter formatter1 = DateTimeFormat.forPattern("yyyy-MM-dd");
	        String startDateString = date.toString(formatter1);
	        System.out.println("!!!!!!!!!!@@@@@@@@@@@@@" + startDateString);
	
			List<Map<String, Object>> employeeDateWiseData = new ArrayList<>();
			List<EventCreation> dateFilteredEvent = eventCreation.stream().filter(e -> {
				String eventStartTime =  e.getEvent_start_time().substring(0, 10);
				 
				return eventStartTime.equals(startDateString);
			}).collect(Collectors.toList());

			dateFilteredEvent.forEach(event -> {
				Map<String, Object> dayWiseEvent = new HashMap<>();
				// Populate the map with event details
				dayWiseEvent.put("event_name", event.getEvent_name());
				dayWiseEvent.put("event_sub_name", event.getEvent_sub_name());
				dayWiseEvent.put("event_description", event.getEvent_description());
				dayWiseEvent.put("event_start_time", event.getEvent_start_time()); // Keep event_start_time as string
				dayWiseEvent.put("event_end_time", event.getEvent_end_time()); // Keep event_end_time as string
				employeeDateWiseData.add(dayWiseEvent);
			});

			List<Map<String, Object>> dateFilteredDeclaredHoliday = declaredHoliday.stream()
					.filter(e -> (new DateTime(e.get("from_date"))).toLocalDate().equals(date))
					.collect(Collectors.toList());
//			System.out.println("DATEEEEEEE : " + date);
//			System.out.println("Data on " + date + " : " + dateFilteredDeclaredHoliday.size());
			if (ObjectUtils.isNotEmpty(dateFilteredDeclaredHoliday)) {
				employeeDateWiseData.addAll(dateFilteredDeclaredHoliday);
			}

			List<Map<String, Object>> dateFilteredGeneralHoliday = generalHoliday.stream()
					.filter(e -> (new DateTime(e.get("from_date"))).toLocalDate().equals(date))
					.collect(Collectors.toList());
			if (ObjectUtils.isNotEmpty(dateFilteredGeneralHoliday)) {
				employeeDateWiseData.addAll(dateFilteredGeneralHoliday);
			}

			List<Map<String, Object>> dateFilteredInternalDetails = internalStudentAssignmentDetails.stream()
					.filter(e -> {

						if (e.get("from_date") != null) {
							return LocalDate.parse(e.get("from_date").toString(), formatter).equals(date);
						}
						return false; // Return false if 'from_date' is null
					}).collect(Collectors.toList());
//			 System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+dateFilteredInternalDetails );
			if (ObjectUtils.isNotEmpty(dateFilteredInternalDetails)) {
				dateFilteredInternalDetails.stream().forEach(internal -> {
					Map<String, Object> dayWiseInternal = new HashMap<>();
					dayWiseInternal.put("timeSlots", internal.get("timeSlots"));
					dayWiseInternal.put("course_with_coursecode", internal.get("course_with_coursecode"));
					dayWiseInternal.put("roomcode", internal.get("roomcode"));
					dayWiseInternal.put("program_name", internal.get("program_name"));
					dayWiseInternal.put("school_name", internal.get("school_name"));
					dayWiseInternal.put("program_specialization_name", internal.get("program_specialization_name"));
					dayWiseInternal.put("course_name", internal.get("course_name"));
					dayWiseInternal.put("exam_time", internal.get("exam_time"));
					dayWiseInternal.put("date_of_exam", internal.get("date_of_exam"));
					dayWiseInternal.put("internal_name", internal.get("internal_name"));
					employeeDateWiseData.add(dayWiseInternal);
				});
			}

			List<Map<String, Object>> dateFilteredTimeTable = timeTableOfEmployee.stream()
					.filter(e -> (new DateTime(e.get("selected_date"))).toLocalDate().equals(date))
					.collect(Collectors.toList());
			if (ObjectUtils.isNotEmpty(dateFilteredTimeTable)) {
				employeeDateWiseData.addAll(dateFilteredTimeTable);
			}
			employeeTimeTableResponse.addAll(employeeDateWiseData);
			currentDate.set(currentDate.get().plusDays(1));
			finalResponeofTimeTable.add(employeeTimeTableResponse);
		}

		return finalResponeofTimeTable;
	}

	public void updateEmployeeIdForSwapping(Integer time_table_id, Integer old_emp_id, Integer new_emp_id,
			Integer subject_assignment_id) {

		timeTableRepository.updateEmployeeIdForSwapping(time_table_id, old_emp_id, new_emp_id, subject_assignment_id);
		timeTableRepository.updateSubjectAssignmentIdForSwapping(time_table_id, subject_assignment_id);
		return;

	}

	public List<Map<String, Object>> courseAssignedToEmployeeForStudentMarks(Integer user_id, Integer ac_year_id,
			Integer school_id, Integer program_id, Integer program_specialization_id, Integer section_id,
			Integer current_sem, Integer current_year) {
		Integer emp_id = employeeDetailsRepository
				.getEmployeeIdOnUserMail(userAuthenticationRepository.getEmail(user_id));
		List<Map<String, Object>> assigned_course_of_employee;
		if (current_sem != null) {
			assigned_course_of_employee = timeTableRepository.courseAssignedToEmployeeForStudentMarksOnSem(emp_id,
					ac_year_id, school_id, program_id, program_specialization_id, section_id, current_sem);
		} else {
			assigned_course_of_employee = timeTableRepository.courseAssignedToEmployeeForStudentMarksOnYear(emp_id,
					ac_year_id, school_id, program_id, program_specialization_id, section_id, current_year);
		}
		return assigned_course_of_employee;

	}

	public List<HashMap<Object, Object>> getEmployeeMonthlyWorkedHours(Date from_month, Date to_month,
			List<Integer> dept_id, Integer school_id) throws ParseException {

		List<Integer> list_emp_ids;

		if (dept_id != null)
			list_emp_ids = employeeDetailsRepository.getAllEmployeesByDeptId(dept_id);
		else
			list_emp_ids = employeeDetailsRepository.getAllEmployeesBySchoolId(school_id);
		List<HashMap<Object, Object>> Employee_Monthly_Worked_Hours_data = new ArrayList<HashMap<Object, Object>>();

		list_emp_ids.stream().forEach(id -> {
			HashMap<Object, Object> data = new HashMap<>();
			List<Map<String, Object>> total_minutes = timeTableEmployeeRepository
					.getEmployeeMonthlyWorkedHours(from_month, to_month, id);

			if (total_minutes.size() != 0) {
				data.put("emp_id", total_minutes.get(0).get("emp_id"));
				data.put("emp_type", total_minutes.get(0).get("emp_type"));
				data.put("employee_name", total_minutes.get(0).get("employee_name"));
			}
			total_minutes.stream().forEach(tm -> {
				data.put(tm.get("month").toString(), tm.get("duration"));
			});
			if (data.size() != 0) {
				Employee_Monthly_Worked_Hours_data.add(data);
			}

		});

		return Employee_Monthly_Worked_Hours_data;

	}

	public List<Map<String, Object>> coursesAssignedDetailsForTimeTAbleView(Integer ac_year_id, Integer school_id,
			Integer program_id, Integer program_specialization_id, Integer section_id, Integer current_sem,
			Integer current_year, String selected_date) throws ParseException {

		List<Map<String, Object>> assigned_course_of_employee;

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date selected_date1 = sdf.parse(selected_date);

		if (current_sem != null) {
			assigned_course_of_employee = timeTableRepository.coursesAssignedDetailsForTimeTableViewOnSem(ac_year_id,
					school_id, program_id, program_specialization_id, section_id, current_sem, selected_date1);
		} else {
			assigned_course_of_employee = timeTableRepository.coursesAssignedDetailsForTimeTableViewOnYear(ac_year_id,
					school_id, program_id, program_specialization_id, section_id, current_year, selected_date1);
		}
		return assigned_course_of_employee;

	}

	public List<HashMap<Object, Object>> getEmployeeSubjectDailyWorkedHours(Date date1, Date date2, Integer emp_id)
			throws ParseException {

		List<Integer> list_subject_assignment_ids = timeTableEmployeeRepository
				.getAllEmployeesSubjectFromTimeTable(date1, emp_id);
		List<HashMap<Object, Object>> Employee_Monthly_Worked_Hours_data = new ArrayList<HashMap<Object, Object>>();

		list_subject_assignment_ids.stream().forEach(id -> {
			HashMap<Object, Object> data = new HashMap<>();
			List<Map<String, Object>> total_minutes_subject_wise = timeTableEmployeeRepository
					.getEmployeeSubjectDailyWorkedHours(date1, date2, id, emp_id);
			if (total_minutes_subject_wise.size() != 0) {
				data.put("course_name", total_minutes_subject_wise.get(0).get("course_name"));
				data.put("subject_assignment_id", total_minutes_subject_wise.get(0).get("subject_assignment_id"));
			}
			total_minutes_subject_wise.stream().forEach(tm -> {
				data.put(tm.get("day").toString(), tm.get("duration"));
			});
			if (data.size() != 0) {
				Employee_Monthly_Worked_Hours_data.add(data);
			}
		});

		return Employee_Monthly_Worked_Hours_data;
	}

	public List<HashMap<Object, Object>> getEmployeesDailyWorkedHours(Date from_month, Date to_month, Integer dept_id)
			throws ParseException {

		List<Integer> list_emp_ids = employeeDetailsRepository.getAllEmployeesById(dept_id);
		System.out.println("((((((((((((((((())))))))))))))))) " + list_emp_ids);
		List<HashMap<Object, Object>> Employee_Monthly_Worked_Hours_data = new ArrayList<HashMap<Object, Object>>();

		list_emp_ids.stream().forEach(id -> {

			List<Integer> list_subject_assignment_ids = timeTableEmployeeRepository
					.getAllEmployeesSubjectFromTimeTable(from_month, id);

			list_subject_assignment_ids.stream().forEach(s_id -> {

				HashMap<Object, Object> data = new HashMap<>();

				List<Map<String, Object>> total_minutes = timeTableEmployeeRepository
						.getEmployeesDailyWorkedHours(from_month, to_month, id, s_id);

				if (total_minutes.size() != 0) {
					data.put("emp_id", total_minutes.get(0).get("emp_id"));
					data.put("course_name", total_minutes.get(0).get("course_name"));
					data.put("employee_name", total_minutes.get(0).get("employee_name"));
					data.put("subject_assignment_id", total_minutes.get(0).get("subject_assignment_id"));

				}

				total_minutes.stream().forEach(tm -> {
					data.put(tm.get("day").toString(), tm.get("duration"));
				});

				if (data.size() != 0) {
					Employee_Monthly_Worked_Hours_data.add(data);
				}

			});

		});

		return Employee_Monthly_Worked_Hours_data;
	}

	public List<Map<String, Object>> getEmployeesDailyWorkedHours(Integer emp_id, Date date,
			Integer subject_assignment_id) {
		List<Map<String, Object>> list_data = timeTableEmployeeRepository.getEmployeesDailyWorkedHours(emp_id, date,
				subject_assignment_id);
		return list_data;

	}

	public List<HashMap<Object, Object>> getTimeTableDataRoomWiseByFacilityType(Date from_date, Date to_date,
			Integer facility_type_id) throws ParseException {

		List<Integer> list_room_ids = timeTableRepository.getAllRoomIds(facility_type_id);
		System.out.println("((((((((((((((((())))))))))))))))) " + list_room_ids);
		List<HashMap<Object, Object>> time_table_data_room_wise = new ArrayList<HashMap<Object, Object>>();

		list_room_ids.stream().forEach(room_id -> {

			HashMap<Object, Object> data = new HashMap<>();

			List<Map<String, Object>> room_wise_data = timeTableEmployeeRepository
					.getTimeTableDataRoomWiseByFacilityType(from_date, to_date, room_id);

			if (room_wise_data.size() != 0) {
				data.put("room_id", room_wise_data.get(0).get("room_id"));
				data.put("manual_room_no", room_wise_data.get(0).get("manual_room_no"));
				data.put("roomcode", room_wise_data.get(0).get("roomcode"));
			}

			room_wise_data.stream().forEach(tm -> {
				data.put(tm.get("day").toString(), tm.get("count"));
			});

			if (data.size() != 0) {
				time_table_data_room_wise.add(data);
			}

		});

		return time_table_data_room_wise;
	}

	public List<HashMap<Object, Object>> getTimeTableDataRoomWiseByFacilityType1(Date from_date,
			Integer facility_type_id) throws ParseException {

		List<Integer> list_room_ids = timeTableRepository.getAllRoomIds(facility_type_id);
		System.out.println("((((((((((((((((())))))))))))))))) " + list_room_ids);
		List<HashMap<Object, Object>> time_table_data_room_wise = new ArrayList<HashMap<Object, Object>>();

		list_room_ids.stream().forEach(room_id -> {

			HashMap<String, Object> data = new HashMap<>();

			List<HashMap<Object, Object>> room_wise_data = timeTableEmployeeRepository
					.getTimeTableDataRoomWiseByFacilityType1(from_date, room_id);
			System.out.println("(((((((((((((((((1111))))))))))))))))) " + room_wise_data);

			time_table_data_room_wise.addAll(room_wise_data);
		});

		return time_table_data_room_wise;
	}

	public Map<Object, Object> getTimeTableData(Date from_date, Date to_date, Integer facility_type_id)
			throws ParseException {

		List<Integer> list_room_ids = timeTableRepository.getAllRoomIds(facility_type_id);
		System.out.println("((((((((((((((((())))))))))))))))) " + list_room_ids);
		Map<Object, Object> time_table_data_room_wise = new HashMap<Object, Object>();

		list_room_ids.stream().forEach(room_id -> {

			HashMap<Object, HashMap<String, Object>> data = new HashMap<>();

			List<Map<String, Object>> room_wise_data = timeTableEmployeeRepository.getTimeTableData(from_date, to_date,
					room_id);
			time_table_data_room_wise.put(room_id, room_wise_data);
		});

		return time_table_data_room_wise;
	}

	public List<Map<String, Object>> timeTableDetailsOfUserForCalender(Integer userId) {
		UserAuthentication userDetails = userAuthenticationRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found: " + userId));
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + userId);
		Integer employeeId = employeeDetailsRepository.getEmpId(userDetails.getEmail());
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ " + employeeId);
		if (employeeId == null) {
			throw new ResourceNotFoundException("Employee Not Found: " + employeeId);
		}
		return timeTableRepository.timeTableDetailsOfUserForCalender(employeeId);
	}

	public List<Map<String, Object>> fetchTimeTableDetailsByEmployeeId(Integer employeeId) {
		return timeTableRepository.timeTableDetailsOfUserForCalender(employeeId);
	}

	public List<Map<String, Object>> timeTableDetailsOfStudentForWeb(Integer id, JwtDetails jwtDetails)
			throws ParseException {
		Integer student_id = userAuthenticationRepository.getStudentEmail(id);
		String program_type = studentDetailsRepository.getProgramTypeOfStudent(student_id);
		Integer academic_year_id = academicYearRepository.getCurrentAcademicYearId();
		ReportingStudents reporting_student = reportingStudentsRepository
				.getDetailsOfReportingStudentsByStudentId(student_id);
		List<Integer> section_assignment_id;
		List<Integer> batch_assignment_id;
		if (program_type.equalsIgnoreCase("Semester")) {
			section_assignment_id = sectionAssignmentRepository.getSectionAssignmentIdBySem(student_id,
					reporting_student.getCurrent_sem());
			batch_assignment_id = batchAssignmentRepository.batchAssignmentIdOnSem(student_id,
					reporting_student.getCurrent_sem());
		} else {
			section_assignment_id = sectionAssignmentRepository.getSectionAssignmentIdByYear(student_id,
					reporting_student.getCurrent_year());
			batch_assignment_id = batchAssignmentRepository.batchAssignmentIdOnYear(student_id,
					reporting_student.getCurrent_year());

		}

		return timeTableRepository.timeTableDetailsOfStudentForWeb(student_id, section_assignment_id,
				batch_assignment_id);
	}

	public List<Map<String, Object>> getAllEmployeesForTimeTable(Date date1, Date date2, Integer time_slots_id) {
		return employeeDetailsRepository.getAllEmployeesForTimeTable(date1, date2, time_slots_id);
	}

	public List<Map<String, Object>> getEmployeesForSectionTimeTable(Integer school_id, Integer program_specialization_id) {
		return employeeDetailsRepository.getEmployeesForSectionTimeTable(school_id, program_specialization_id);
	}

	public List<Map<String, Object>> getEmployeesForBatchTimeTable(Integer batch_assignment_id) {
		return employeeDetailsRepository.getEmployeesForBatchTimeTable(batch_assignment_id);
	}

	public void updateRoomForSwapping(Integer timeTableId, Integer roomId) {
		timeTableRepository.updateRoomForSwapping(timeTableId, roomId);

	}

	public Map<String, Object> checkAttendanceStatus(Integer time_table_id) {
		return timeTableRepository.checkAttendanceStatus(time_table_id);
	}

	public List<FacultyDetailsForTimeTable> fetchDetailsOfFacultyByTimeTableId(Integer time_table_id, Integer emp_id) {
		List<FacultyDetailsForTimeTable> facultyDetailsListForTimeTable = new ArrayList<>();
		List<Map<String, Object>> timeTableDetailsOfFaculty = timeTableRepository
				.fetchDetailsOfFacultyByTimeTableId(time_table_id, emp_id);
		if (!(timeTableDetailsOfFaculty.isEmpty())) {
			timeTableDetailsOfFaculty.parallelStream().forEach(timeTableDetailOfFaculty -> {
				FacultyDetailsForTimeTable facultyDetailsForTimeTable = new FacultyDetailsForTimeTable();
				Integer sectionAssignedStudentCount = 0;
				Integer batchAssignedStudentCount = 0;
				if (ObjectUtils.isNotEmpty(timeTableDetailOfFaculty.get("section_assigned_student_ids")))
					sectionAssignedStudentCount = (int) Arrays
							.stream(timeTableDetailOfFaculty.get("section_assigned_student_ids").toString().split(","))
							.count();
				if (ObjectUtils.isNotEmpty(timeTableDetailOfFaculty.get("batch_assigned_student_ids")))
					batchAssignedStudentCount = (int) Arrays
							.stream(timeTableDetailOfFaculty.get("batch_assigned_student_ids").toString().split(","))
							.count();

				facultyDetailsForTimeTable.setBatchName(timeTableDetailOfFaculty.get("batch_name") == null ? null
						: timeTableDetailOfFaculty.get("batch_name").toString() + " - " + batchAssignedStudentCount);
				facultyDetailsForTimeTable.setSelectedDate(timeTableDetailOfFaculty.get("selected_date") == null ? null
						: (Date) timeTableDetailOfFaculty.get("selected_date"));
				facultyDetailsForTimeTable.setEmploeeName(timeTableDetailOfFaculty.get("employee_name") == null ? null
						: timeTableDetailOfFaculty.get("employee_name").toString());
				facultyDetailsForTimeTable.setEmployeeCode(timeTableDetailOfFaculty.get("empcode") == null ? null
						: timeTableDetailOfFaculty.get("empcode").toString());
				facultyDetailsForTimeTable
						.setSubjectCode(timeTableDetailOfFaculty.get("course_assignment_coursecode") == null ? null
								: timeTableDetailOfFaculty.get("course_assignment_coursecode").toString());
				facultyDetailsForTimeTable.setCourse(timeTableDetailOfFaculty.get("course_name") == null ? null
						: timeTableDetailOfFaculty.get("course_name").toString());
				facultyDetailsForTimeTable.setCourseCode(timeTableDetailOfFaculty.get("course_code") == null ? null
						: timeTableDetailOfFaculty.get("course_code").toString());
				facultyDetailsForTimeTable
						.setSchoolShortName(timeTableDetailOfFaculty.get("school_name_short") == null ? null
								: timeTableDetailOfFaculty.get("school_name_short").toString());
				facultyDetailsForTimeTable.setWeekDay(timeTableDetailOfFaculty.get("week_day") == null ? null
						: timeTableDetailOfFaculty.get("week_day").toString());
				facultyDetailsForTimeTable.setInterval(timeTableDetailOfFaculty.get("timeSlots") == null ? null
						: timeTableDetailOfFaculty.get("timeSlots").toString());
				facultyDetailsForTimeTable.setBlockCode(timeTableDetailOfFaculty.get("blockcode") == null ? null
						: timeTableDetailOfFaculty.get("blockcode").toString());
				facultyDetailsForTimeTable.setRoomCode(timeTableDetailOfFaculty.get("roomcode") == null ? null
						: timeTableDetailOfFaculty.get("roomcode").toString());
				facultyDetailsForTimeTable.setYear(timeTableDetailOfFaculty.get("current_year") == null ? null
						: (Integer) timeTableDetailOfFaculty.get("current_year"));
				facultyDetailsForTimeTable.setSem(timeTableDetailOfFaculty.get("current_sem") == null ? null
						: (Integer) timeTableDetailOfFaculty.get("current_sem"));
				facultyDetailsForTimeTable.setEmpId(timeTableDetailOfFaculty.get("emp_id") == null ? null
						: (Integer) timeTableDetailOfFaculty.get("emp_id"));
				facultyDetailsForTimeTable.setSectionName(timeTableDetailOfFaculty.get("section_name") == null ? null
						: timeTableDetailOfFaculty.get("section_name").toString() + " - "
								+ sectionAssignedStudentCount);
				facultyDetailsForTimeTable.setProgramSpecializationName(
						timeTableDetailOfFaculty.get("program_specialization_name") == null ? null
								: timeTableDetailOfFaculty.get("program_specialization_name").toString());
				facultyDetailsForTimeTable.setProgramSpecializationShortName(
						timeTableDetailOfFaculty.get("program_specialization_short_name") == null ? null
								: timeTableDetailOfFaculty.get("program_specialization_short_name").toString());
				facultyDetailsListForTimeTable.add(facultyDetailsForTimeTable);
			});
		} else {
			throw new ResourceNotFoundException("TimeTable Not Found:" + time_table_id);
		}
		return facultyDetailsListForTimeTable;
	}

	public List<ProgramSpecilization> programSpecilizationForOnBatchAssignmentIdOrSectionAssignmentId(
			Integer batchAssignmentId, Integer sectionAssignmentId) {

		List<ProgramSpecilization> programSpecilizationDetails = new ArrayList<>();
		List<Integer> programSpecilizationIds = new ArrayList<>();
		if (batchAssignmentId != null) {
			programSpecilizationIds = bpa_repo.assignedProgramSpecilizationByBatchAssignmentId(batchAssignmentId);
			programSpecilizationDetails = programSpecilizationRepository.findAllById(programSpecilizationIds);
		} else if (sectionAssignmentId != null) {
			programSpecilizationIds = sa_repo.programSpecilizationIdsBySectionAssignmentId(sectionAssignmentId);
			programSpecilizationDetails = programSpecilizationRepository.findAllById(programSpecilizationIds);
		} else {
			throw new NullArgumentException("Sned Either batchAssignmentId Or sectionAssignmentId For Specilization");
		}

		return programSpecilizationDetails;

	}

	public ResponseEntity<Object> employeeTimeTableDetailsForMobile(Integer year, Integer month, String jwtToken) {
		JwtDetails jwtDetails;
		try {
			jwtDetails = jwtTokenService.callJwtToken(jwtToken);
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + jwtDetails.getUserId());
			HashMap<String, Object> employeeDetails = employeeDetailsRepository
					.getEmployeeDetailsByUserID(jwtDetails.getUserId());
			Integer employeeId = ObjectUtils.isNotEmpty(employeeDetails) ? (Integer) employeeDetails.get("emp_id")
					: null;
			System.out.println(employeeDetails.get("emp_id"));
			List<Map<String, Object>> timeTable = timeTableRepository.employeeTimeTableDetailsForMobile(employeeId,
					year, month);
			List<Map<String, Object>> result = new ArrayList<>();

			timeTable.stream().forEach(batchAss -> {
				if(ObjectUtils.isNotEmpty(batchAss.get("batch_assignment_id"))) {
					@SuppressWarnings("unchecked")
					Map<String, Object> batch=modelMapper.map(batchAss, Map.class);
					Map<String,String> programAndSpecialtionNames=batchProgramAssignmentRepository.specializationAndProgramNames((Integer)batchAss.get("batch_assignment_id"));
					batch.put("program_specialization_short_name", programAndSpecialtionNames.get("specializationShortNames"));
					batch.put("program_short_name", programAndSpecialtionNames.get("programShortNames"));
					batch.put("program_name", programAndSpecialtionNames.get("programNames"));
					batch.put("program_specialization_name", programAndSpecialtionNames.get("specializationNames"));
//					List<Integer> programIds=ResponseHandler.toConvertCommaSeperatedIdsAsList(programAndSpecialtionNames.get("programIds"));
//					List<Integer> specializationsIds=ResponseHandler.toConvertCommaSeperatedIdsAsList(programAndSpecialtionNames.get("specializationIds"));
					batch.put("program_id", programAndSpecialtionNames.get("programIds"));
					batch.put("program_specialization_id", programAndSpecialtionNames.get("specializationIds"));
					result.add(batch);
				}else {
					result.add(batchAss);
				}
			});
			return ResponseHandler.generateResponse(true, HttpStatus.OK, result);
		} catch (Exception e) {
			return ResponseHandler.generateResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	public ResponseEntity<Object> fetchTimeTableDetailsForIndexByKeyword(Pageable pageable, Object keyword,
			Integer ac_year_id, Integer school_id, Integer dept_id, Integer program_id,Integer program_specialization_id, String formattedDate,String formattedFromDate,String formattedToDate,
			 Integer current_year, Integer current_sem,Integer userId, Boolean active) {
		Page<Map<String, Object>> Notifications_filtered_response = timeTableRepository
				.fetchTimeTableDetailsForIndexByKeyword(pageable, keyword,
						ac_year_id, school_id, dept_id, program_id,program_specialization_id, formattedDate,formattedFromDate, formattedToDate,current_sem,current_year ,userId ,active);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_filtered_response);
	}

	public ResponseEntity<Object> fetchTimeTableDetailsForIndexWOKeyword(Pageable pageable1,
			Integer ac_year_id, Integer school_id, Integer dept_id, Integer program_id,Integer program_specialization_id, String formattedDate,String formattedFromDate,String formattedToDate,
			 Integer current_year, Integer current_sem,Integer userId, Boolean active) {
		Page<Map<String, Object>> Notifications_sorted_response = timeTableRepository.fetchTimeTableDetailsForIndexWOKeyword(pageable1,
				ac_year_id, school_id, dept_id, program_id,program_specialization_id, formattedDate,formattedFromDate, formattedToDate,current_sem,current_year ,userId ,active);
		return ResponseHandler.generateResponseForIndex1(true, HttpStatus.OK, Notifications_sorted_response);
	}


}
